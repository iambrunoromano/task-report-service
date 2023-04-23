package com.service.taskreport.rest;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.enums.StatusEnum;
import com.service.taskreport.exception.TaskExecutionReportNotFoundException;
import com.service.taskreport.exception.TaskStepExecutionReportBadRequestException;
import com.service.taskreport.exception.TaskStepExecutionReportNotFoundException;
import com.service.taskreport.exception.UndefinedStatusException;
import com.service.taskreport.mapper.EntityResponseMapper;
import com.service.taskreport.mapper.RequestEntityMapper;
import com.service.taskreport.request.TaskExecutionReportRequest;
import com.service.taskreport.request.TaskStepExecutionReportRequest;
import com.service.taskreport.response.TaskExecutionReportResponse;
import com.service.taskreport.service.TaskExecutionReportService;
import com.service.taskreport.service.TaskStepExecutionReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "task-execution-reports")
public class TaskExecutionReportController {
  private final TaskExecutionReportService taskExecutionReportService;
  private final TaskStepExecutionReportService taskStepExecutionReportService;
  private final RequestEntityMapper requestEntityMapper;
  private final EntityResponseMapper entityResponseMapper;

  @Autowired
  TaskExecutionReportController(
      TaskExecutionReportService taskExecutionReportService,
      TaskStepExecutionReportService taskStepExecutionReportService,
      RequestEntityMapper requestEntityMapper,
      EntityResponseMapper entityResponseMapper) {
    this.taskExecutionReportService = taskExecutionReportService;
    this.taskStepExecutionReportService = taskStepExecutionReportService;
    this.requestEntityMapper = requestEntityMapper;
    this.entityResponseMapper = entityResponseMapper;
  }

  @GetMapping
  public ResponseEntity<List<TaskExecutionReportResponse>> getAll() {
    List<TaskExecutionReport> taskExecutionReportList = taskExecutionReportService.getAll();
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        taskStepExecutionReportService.getAll();
    List<TaskExecutionReportResponse> taskExecutionReportResponseList =
        mapListToResponse(taskExecutionReportList, taskStepExecutionReportList);
    return ResponseEntity.ok(taskExecutionReportResponseList);
  }

  @GetMapping(value = "/status/{status}")
  public ResponseEntity<List<TaskExecutionReportResponse>> getAllByStatus(
      @PathVariable StatusEnum status)
      throws TaskExecutionReportNotFoundException, TaskStepExecutionReportBadRequestException,
          TaskStepExecutionReportNotFoundException {
    List<TaskExecutionReport> taskExecutionReportList =
        taskExecutionReportService.getByStatus(status);
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        taskStepExecutionReportService.getByTaskExecutionReportList(taskExecutionReportList);
    List<TaskExecutionReportResponse> taskExecutionReportResponseList =
        mapListToResponse(taskExecutionReportList, taskStepExecutionReportList);
    return ResponseEntity.ok(taskExecutionReportResponseList);
  }

  @GetMapping(value = "/execution-time")
  public ResponseEntity<List<TaskExecutionReportResponse>> getAllOrderByExecutionTime()
      throws TaskStepExecutionReportBadRequestException, TaskStepExecutionReportNotFoundException {
    List<TaskExecutionReport> taskExecutionReportList =
        taskExecutionReportService.getAllOrderByExecutionTimeSeconds();
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        taskStepExecutionReportService.getByTaskExecutionReportList(taskExecutionReportList);
    List<TaskExecutionReportResponse> taskExecutionReportResponseList =
        mapListToResponse(taskExecutionReportList, taskStepExecutionReportList);
    return ResponseEntity.ok(taskExecutionReportResponseList);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<TaskExecutionReportResponse> getById(@PathVariable Integer id)
      throws TaskExecutionReportNotFoundException, TaskStepExecutionReportNotFoundException {
    TaskExecutionReport taskExecutionReport = taskExecutionReportService.getById(id);
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        taskStepExecutionReportService.getByTaskExecutionId(id);
    TaskExecutionReportResponse taskExecutionReportResponse =
        mapToResponse(taskExecutionReport, taskStepExecutionReportList);
    return ResponseEntity.ok(taskExecutionReportResponse);
  }

  @PostMapping
  public ResponseEntity<TaskExecutionReportResponse> create(
      @RequestBody TaskExecutionReportRequest taskExecutionReportRequest)
      throws UndefinedStatusException, TaskStepExecutionReportNotFoundException {
    TaskExecutionReport taskExecutionReport = mapToEntity(taskExecutionReportRequest);
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        mapListToEntity(taskExecutionReportRequest.getTaskStepExecutionReports());
    taskExecutionReport =
        taskExecutionReportService.save(taskExecutionReport, taskStepExecutionReportList);
    taskStepExecutionReportList =
        taskStepExecutionReportService.getByTaskExecutionId(taskExecutionReport.getId());
    TaskExecutionReportResponse taskExecutionReportResponse =
        mapToResponse(taskExecutionReport, taskStepExecutionReportList);
    return ResponseEntity.ok(taskExecutionReportResponse);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id)
      throws TaskExecutionReportNotFoundException {
    taskExecutionReportService.delete(id);
    return ResponseEntity.ok().build();
  }

  private List<TaskExecutionReportResponse> mapListToResponse(
      List<TaskExecutionReport> taskExecutionReportList,
      List<TaskStepExecutionReport> taskStepExecutionReportList) {
    return entityResponseMapper.taskExecutionReportListToTaskExecutionReportResponseList(
        taskExecutionReportList, taskStepExecutionReportList);
  }

  private List<TaskStepExecutionReport> mapListToEntity(
      List<TaskStepExecutionReportRequest> taskStepExecutionReportRequestList) {
    return requestEntityMapper.taskStepExecutionReportRequestListToTaskStepExecutionReportList(
        taskStepExecutionReportRequestList);
  }

  private TaskExecutionReport mapToEntity(TaskExecutionReportRequest taskExecutionReportRequest) {
    return requestEntityMapper.taskExecutionReportRequestToTaskExecutionReport(
        taskExecutionReportRequest);
  }

  private TaskExecutionReportResponse mapToResponse(
      TaskExecutionReport taskExecutionReport,
      List<TaskStepExecutionReport> taskStepExecutionReportList) {
    return entityResponseMapper.taskExecutionReportToTaskExecutionReportResponse(
        taskExecutionReport, taskStepExecutionReportList);
  }
}
