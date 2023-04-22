package com.service.taskreport.rest;

import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.service.TaskStepExecutionReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "task-step-execution-reports")
public class TaskStepExecutionReportController {

  private TaskStepExecutionReportService taskStepExecutionReportService;

  @Autowired
  TaskStepExecutionReportController(TaskStepExecutionReportService taskStepExecutionReportService) {
    this.taskStepExecutionReportService = taskStepExecutionReportService;
  }

  @GetMapping(value = "/all")
  public ResponseEntity<List<TaskStepExecutionReport>> getAll() {
    return ResponseEntity.ok(taskStepExecutionReportService.getAll());
  }
}
