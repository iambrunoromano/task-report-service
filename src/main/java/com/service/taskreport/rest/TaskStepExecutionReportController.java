package com.service.taskreport.rest;

import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.exception.TaskStepExecutionReportNotFoundException;
import com.service.taskreport.mapper.EntityResponseMapper;
import com.service.taskreport.mapper.RequestEntityMapper;
import com.service.taskreport.response.TaskStepExecutionReportResponse;
import com.service.taskreport.service.TaskStepExecutionReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "task-step-execution-reports")
public class TaskStepExecutionReportController {

  private TaskStepExecutionReportService taskStepExecutionReportService;
  private final RequestEntityMapper requestEntityMapper;
  private final EntityResponseMapper entityResponseMapper;

  @Autowired
  TaskStepExecutionReportController(
      TaskStepExecutionReportService taskStepExecutionReportService,
      RequestEntityMapper requestEntityMapper,
      EntityResponseMapper entityResponseMapper) {
    this.taskStepExecutionReportService = taskStepExecutionReportService;
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

  @GetMapping(value = "/{id}")
  public ResponseEntity<TaskStepExecutionReportResponse> getById(@PathVariable Integer id)
      throws TaskStepExecutionReportNotFoundException {
    TaskStepExecutionReport taskStepExecutionReport = taskStepExecutionReportService.getById(id);
    TaskStepExecutionReportResponse taskStepExecutionReportResponse =
        mapToResponse(taskStepExecutionReport);
    return ResponseEntity.ok(taskStepExecutionReportResponse);
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
}
