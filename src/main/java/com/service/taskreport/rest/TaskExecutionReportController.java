package com.service.taskreport.rest;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.service.TaskExecutionReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "task-execution-reports")
public class TaskExecutionReportController {
  private final TaskExecutionReportService taskExecutionReportService;

  @Autowired
  TaskExecutionReportController(TaskExecutionReportService taskExecutionReportService) {
    this.taskExecutionReportService = taskExecutionReportService;
  }

  @GetMapping(value = "/all")
  public ResponseEntity<List<TaskExecutionReport>> getAll() {
    return ResponseEntity.ok(taskExecutionReportService.getAll());
  }
}
