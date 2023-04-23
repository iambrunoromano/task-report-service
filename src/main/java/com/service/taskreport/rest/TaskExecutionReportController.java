package com.service.taskreport.rest;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.enums.StatusEnum;
import com.service.taskreport.exception.TaskExecutionReportNotFoundException;
import com.service.taskreport.exception.TaskStepExecutionReportNotFoundException;
import com.service.taskreport.exception.UndefinedStatusException;
import com.service.taskreport.mapper.EntityResponseMapper;
import com.service.taskreport.mapper.RequestEntityMapper;
import com.service.taskreport.request.TaskExecutionReportRequest;
import com.service.taskreport.response.TaskExecutionReportResponse;
import com.service.taskreport.service.TaskExecutionReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "task-execution-reports")
public class TaskExecutionReportController {
  private final TaskExecutionReportService taskExecutionReportService;
  private final RequestEntityMapper requestEntityMapper;
  private final EntityResponseMapper entityResponseMapper;

  @Autowired
  TaskExecutionReportController(
      TaskExecutionReportService taskExecutionReportService,
      RequestEntityMapper requestEntityMapper,
      EntityResponseMapper entityResponseMapper) {
    this.taskExecutionReportService = taskExecutionReportService;
    this.requestEntityMapper = requestEntityMapper;
    this.entityResponseMapper = entityResponseMapper;
  }

  @GetMapping
  public ResponseEntity<List<TaskExecutionReportResponse>> getAll() {
    List<TaskExecutionReport> taskExecutionReportList = taskExecutionReportService.getAll();
    List<TaskExecutionReportResponse> taskExecutionReportResponseList =
        mapListToResponse(taskExecutionReportList);
    return ResponseEntity.ok(taskExecutionReportResponseList);
  }

  @GetMapping(value = "/status/{status}")
  public ResponseEntity<List<TaskExecutionReportResponse>> getAllByStatus(
      @PathVariable StatusEnum status) throws TaskExecutionReportNotFoundException {
    List<TaskExecutionReport> taskExecutionReportList =
        taskExecutionReportService.getByStatus(status);
    List<TaskExecutionReportResponse> taskExecutionReportResponseList =
        mapListToResponse(taskExecutionReportList);
    return ResponseEntity.ok(taskExecutionReportResponseList);
  }

  @GetMapping(value = "/execution-time")
  public ResponseEntity<List<TaskExecutionReportResponse>> getAllOrderByExecutionTime() {
    List<TaskExecutionReport> taskExecutionReportList =
        taskExecutionReportService.getAllOrderByExecutionTimeSeconds();
    List<TaskExecutionReportResponse> taskExecutionReportResponseList =
        mapListToResponse(taskExecutionReportList);
    return ResponseEntity.ok(taskExecutionReportResponseList);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<TaskExecutionReportResponse> getById(@PathVariable Integer id)
      throws TaskExecutionReportNotFoundException {
    TaskExecutionReport taskExecutionReport = taskExecutionReportService.getById(id);
    TaskExecutionReportResponse taskExecutionReportResponse = mapToResponse(taskExecutionReport);
    return ResponseEntity.ok(taskExecutionReportResponse);
  }

  @PostMapping
  public ResponseEntity<TaskExecutionReportResponse> create(
      @RequestBody TaskExecutionReportRequest taskExecutionReportRequest)
      throws TaskStepExecutionReportNotFoundException, UndefinedStatusException {
    TaskExecutionReport taskExecutionReport = mapToEntity(taskExecutionReportRequest);
    taskExecutionReport = taskExecutionReportService.save(taskExecutionReport);
    TaskExecutionReportResponse taskExecutionReportResponse = mapToResponse(taskExecutionReport);
    return ResponseEntity.ok(taskExecutionReportResponse);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id)
      throws TaskExecutionReportNotFoundException {
    taskExecutionReportService.delete(id);
    return ResponseEntity.ok().build();
  }

  private List<TaskExecutionReportResponse> mapListToResponse(
      List<TaskExecutionReport> taskExecutionReportList) {
    return entityResponseMapper.taskExecutionReportListToTaskExecutionReportResponseList(
        taskExecutionReportList);
  }

  private TaskExecutionReport mapToEntity(TaskExecutionReportRequest taskExecutionReportRequest) {
    return requestEntityMapper.taskExecutionReportRequestToTaskExecutionReport(
        taskExecutionReportRequest);
  }

  private TaskExecutionReportResponse mapToResponse(TaskExecutionReport taskExecutionReport) {
    return entityResponseMapper.taskExecutionReportToTaskExecutionReportResponse(
        taskExecutionReport);
  }
}
