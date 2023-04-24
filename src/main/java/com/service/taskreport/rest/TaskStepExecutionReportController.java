package com.service.taskreport.rest;

import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.enums.TaskStepExecutionReportColumnNameEnum;
import com.service.taskreport.exception.TaskExecutionReportNotFoundException;
import com.service.taskreport.exception.TaskStepExecutionReportNotFoundException;
import com.service.taskreport.mapper.EntityResponseMapper;
import com.service.taskreport.mapper.RequestEntityMapper;
import com.service.taskreport.request.TaskStepExecutionReportRequest;
import com.service.taskreport.response.TaskStepExecutionReportResponse;
import com.service.taskreport.service.TaskExecutionReportService;
import com.service.taskreport.service.TaskStepExecutionReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "task-step-execution-reports")
public class TaskStepExecutionReportController {

  private TaskStepExecutionReportService taskStepExecutionReportService;
  private TaskExecutionReportService taskExecutionReportService;
  private final RequestEntityMapper requestEntityMapper;
  private final EntityResponseMapper entityResponseMapper;

  @Autowired
  TaskStepExecutionReportController(
      TaskStepExecutionReportService taskStepExecutionReportService,
      TaskExecutionReportService taskExecutionReportService,
      RequestEntityMapper requestEntityMapper,
      EntityResponseMapper entityResponseMapper) {
    this.taskStepExecutionReportService = taskStepExecutionReportService;
    this.taskExecutionReportService = taskExecutionReportService;
    this.requestEntityMapper = requestEntityMapper;
    this.entityResponseMapper = entityResponseMapper;
  }

  @GetMapping
  public ResponseEntity<List<TaskStepExecutionReportResponse>> getAll() {
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        taskStepExecutionReportService.getAll();
    List<TaskStepExecutionReportResponse> taskStepExecutionReportResponseList =
        mapListToResponse(taskStepExecutionReportList);
    return ResponseEntity.ok(taskStepExecutionReportResponseList);
  }

  @GetMapping(value = "/id/{id}")
  public ResponseEntity<TaskStepExecutionReportResponse> getById(@PathVariable Integer id)
      throws TaskStepExecutionReportNotFoundException {
    TaskStepExecutionReport taskStepExecutionReport = taskStepExecutionReportService.getById(id);
    TaskStepExecutionReportResponse taskStepExecutionReportResponse =
        mapToResponse(taskStepExecutionReport);
    return ResponseEntity.ok(taskStepExecutionReportResponse);
  }

  @PostMapping
  public ResponseEntity<TaskStepExecutionReportResponse> create(
      @RequestBody @Valid TaskStepExecutionReportRequest taskStepExecutionReportRequest)
      throws TaskExecutionReportNotFoundException {
    taskExecutionReportService.getById(taskStepExecutionReportRequest.getTaskExecutionId());
    TaskStepExecutionReport taskStepExecutionReport = mapToEntity(taskStepExecutionReportRequest);
    taskStepExecutionReport = taskStepExecutionReportService.save(taskStepExecutionReport);
    TaskStepExecutionReportResponse taskStepExecutionReportResponse =
        mapToResponse(taskStepExecutionReport);
    return ResponseEntity.ok(taskStepExecutionReportResponse);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id)
      throws TaskStepExecutionReportNotFoundException {
    taskStepExecutionReportService.delete(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/task-execution-id/{task_execution_id}")
  public ResponseEntity<List<TaskStepExecutionReportResponse>> getByTaskExecutionId(
      @PathVariable(name = "task_execution_id") Integer taskExecutionId,
      @RequestParam(name = "direction", defaultValue = "ASC") Sort.Direction direction,
      @RequestParam(name = "columnName", defaultValue = "id")
          TaskStepExecutionReportColumnNameEnum columnName)
      throws TaskStepExecutionReportNotFoundException {
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        taskStepExecutionReportService.getSortedByTaskExecutionId(
            taskExecutionId, direction, columnName.getValue());
    List<TaskStepExecutionReportResponse> taskStepExecutionReportListResponse =
        mapListToResponse(taskStepExecutionReportList);
    return ResponseEntity.ok(taskStepExecutionReportListResponse);
  }

  private List<TaskStepExecutionReportResponse> mapListToResponse(
      List<TaskStepExecutionReport> taskStepExecutionReportList) {
    return entityResponseMapper.taskStepExecutionReportListToTaskStepExecutionReportResponseList(
        taskStepExecutionReportList);
  }

  private TaskStepExecutionReportResponse mapToResponse(
      TaskStepExecutionReport taskStepExecutionReport) {
    return entityResponseMapper.taskStepExecutionReportToTaskStepExecutionReportResponse(
        taskStepExecutionReport);
  }

  private TaskStepExecutionReport mapToEntity(
      TaskStepExecutionReportRequest taskStepExecutionReportRequest) {
    return requestEntityMapper.taskStepExecutionReportRequestToTaskStepExecutionReport(
        taskStepExecutionReportRequest);
  }
}
