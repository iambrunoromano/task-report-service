package com.service.taskreport.scheduler;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.service.TaskExecutionReportService;
import com.service.taskreport.service.TaskStepExecutionReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
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
    log.info("Start scheduled computeMissingExecutionTime job");
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        taskStepExecutionReportService.getMissingExecutionTime();
    log.info("Found [{}] taskStepExecutionReport to compute", taskStepExecutionReportList.size());
    for (TaskStepExecutionReport taskStepExecutionReport : taskStepExecutionReportList) {
      taskStepExecutionReportService.save(taskStepExecutionReport);
    }
    List<TaskExecutionReport> taskExecutionReportList =
        taskExecutionReportService.getMissingExecutionTime();
    log.info("Found [{}] taskExecutionReport to compute", taskExecutionReportList.size());
    for (TaskExecutionReport taskExecutionReport : taskExecutionReportList) {
      taskExecutionReportService.save(taskExecutionReport);
    }
    log.info("End scheduled computeMissingExecutionTime job");
  }
}
