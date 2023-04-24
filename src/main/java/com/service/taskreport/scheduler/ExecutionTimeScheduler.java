package com.service.taskreport.scheduler;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.repository.TaskExecutionReportRepository;
import com.service.taskreport.repository.TaskStepExecutionReportRepository;
import com.service.taskreport.service.TaskExecutionReportService;
import com.service.taskreport.service.TaskStepExecutionReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExecutionTimeScheduler {

  private final TaskExecutionReportService taskExecutionReportService;
  private final TaskStepExecutionReportService taskStepExecutionReportService;

  @Autowired
  public ExecutionTimeScheduler(
      TaskExecutionReportService taskExecutionReportService,
      TaskStepExecutionReportService taskStepExecutionReportService) {
    this.taskExecutionReportService = taskExecutionReportService;
    this.taskStepExecutionReportService = taskStepExecutionReportService;
  }

  @Scheduled(cron = "${scheduled.execution-time.cron}")
  public void computeMissingExecutionTime() {
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        taskStepExecutionReportService.getMissingExecutionTime();
    for (TaskStepExecutionReport taskStepExecutionReport : taskStepExecutionReportList) {
      taskStepExecutionReportService.save(taskStepExecutionReport);
    }
    List<TaskExecutionReport> taskExecutionReportList =
        taskExecutionReportService.getMissingExecutionTime();
    for (TaskExecutionReport taskExecutionReport : taskExecutionReportList) {
      taskExecutionReportService.save(taskExecutionReport);
    }
  }
}
