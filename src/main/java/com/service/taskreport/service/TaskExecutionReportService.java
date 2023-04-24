package com.service.taskreport.service;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.enums.StatusEnum;
import com.service.taskreport.exception.TaskExecutionReportNotFoundException;
import com.service.taskreport.exception.UndefinedStatusException;
import com.service.taskreport.repository.TaskExecutionReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TaskExecutionReportService {

  private final TaskExecutionReportRepository taskExecutionReportRepository;
  private final TaskStepExecutionReportService taskStepExecutionReportService;

  @Autowired
  TaskExecutionReportService(
      TaskExecutionReportRepository taskExecutionReportRepository,
      TaskStepExecutionReportService taskStepExecutionReportService) {
    this.taskExecutionReportRepository = taskExecutionReportRepository;
    this.taskStepExecutionReportService = taskStepExecutionReportService;
  }

  public List<TaskExecutionReport> getAll() {
    List<TaskExecutionReport> taskExecutionReportList = taskExecutionReportRepository.findAll();
    log.info("Found [{}] taskExecutionReports", taskExecutionReportList.size());
    return taskExecutionReportList;
  }

  public TaskExecutionReport getById(Integer id) throws TaskExecutionReportNotFoundException {
    Optional<TaskExecutionReport> optionalTaskExecutionReport =
        taskExecutionReportRepository.findById(id);
    if (optionalTaskExecutionReport.isPresent()) {
      TaskExecutionReport taskExecutionReport = optionalTaskExecutionReport.get();
      log.info("Found one taskExecutionReport for id [{}]: [{}]", id, taskExecutionReport);
      return taskExecutionReport;
    }
    throw new TaskExecutionReportNotFoundException(
        String.format("TaskExecutionReport for id [%s] not found", id));
  }

  public TaskExecutionReport saveRequest(
      TaskExecutionReport taskExecutionReport,
      List<TaskStepExecutionReport> taskStepExecutionReportList)
      throws UndefinedStatusException {
    taskExecutionReport =
        setTaskExecutionReportStatus(taskExecutionReport, taskStepExecutionReportList);
    save(taskExecutionReport);
    if (!taskStepExecutionReportList.isEmpty()) {
      log.info(
          "Saving taskStepExecutionReports for taskExecutionReport with id [{}]",
          taskExecutionReport.getId());
      for (TaskStepExecutionReport taskStepExecutionReport : taskStepExecutionReportList) {
        taskStepExecutionReport.setTaskExecutionId(taskExecutionReport.getId());
        taskStepExecutionReportService.save(taskStepExecutionReport);
      }
    }
    return taskExecutionReport;
  }

  public void save(TaskExecutionReport taskExecutionReport) {
    taskExecutionReport = computeSeconds(taskExecutionReport);
    taskExecutionReport = taskExecutionReportRepository.save(taskExecutionReport);
    log.info(
        "Saved taskExecutionReport with taskId [{}] - id [{}]",
        taskExecutionReport.getTaskId(),
        taskExecutionReport.getId());
  }

  public TaskExecutionReport computeSeconds(TaskExecutionReport taskExecutionReport) {
    log.info(
        "Computing executionTimeSeconds for taskExecutionReport with taskId [{}]",
        taskExecutionReport.getTaskId());
    if (taskExecutionReport.getEndDateTime() != null
        && taskExecutionReport.getStartDateTime() != null) {
      long difference =
          taskExecutionReport.getEndDateTime().getTime()
              - taskExecutionReport.getStartDateTime().getTime();
      int computedSeconds = Math.round(difference / 1000);
      log.info(
          "Computed executionTimeSeconds as [{}] for taskExecutionReport with taskId [{}]",
          computedSeconds,
          taskExecutionReport.getTaskId());
      taskExecutionReport.setExecutionTimeSeconds(computedSeconds);
    }
    return taskExecutionReport;
  }

  public void delete(Integer id) throws TaskExecutionReportNotFoundException {
    TaskExecutionReport taskExecutionReport = getById(id);
    taskStepExecutionReportService.deleteByTaskExecutionId(id);
    taskExecutionReportRepository.delete(taskExecutionReport);
  }

  public TaskExecutionReport setTaskExecutionReportStatus(
      TaskExecutionReport taskExecutionReport,
      List<TaskStepExecutionReport> taskStepExecutionReportList)
      throws UndefinedStatusException {
    boolean success = true;
    boolean running = false;
    boolean failure = false;
    if (taskStepExecutionReportList.isEmpty()) {
      log.info("TaskStepExecutionReportList is empty");
      success = false;
    }
    for (TaskStepExecutionReport taskStepExecutionReport : taskStepExecutionReportList) {
      if (!StatusEnum.SUCCESS.equals(taskStepExecutionReport.getStatus())) {
        log.info("Not all taskStepExecutionReports are in status [{}]", StatusEnum.SUCCESS);
        success = false;
      }
      if (StatusEnum.RUNNING.equals(taskStepExecutionReport.getStatus())) {
        log.info("One taskStepExecutionReport has been found in status [{}]", StatusEnum.RUNNING);
        running = true;
      }
      if (StatusEnum.FAILURE.equals(taskStepExecutionReport.getStatus())) {
        log.info("One taskStepExecutionReport has been found in status [{}]", StatusEnum.FAILURE);
        failure = true;
      }
    }
    return transferStatus(taskExecutionReport, success, running, failure);
  }

  public List<TaskExecutionReport> getByStatus(StatusEnum status)
      throws TaskExecutionReportNotFoundException {
    List<TaskExecutionReport> taskExecutionReportList =
        taskExecutionReportRepository.findByStatus(status);
    log.info(
        "Found [{}] taskExecutionReports for status [{}]", taskExecutionReportList.size(), status);
    if (taskExecutionReportList.isEmpty()) {
      throw new TaskExecutionReportNotFoundException(
          String.format("TaskExecutionReport for status [%s] not found", status.getValue()));
    }
    return taskExecutionReportList;
  }

  public List<TaskExecutionReport> getAllOrderByExecutionTimeSeconds() {
    return taskExecutionReportRepository.findAllByOrderByExecutionTimeSecondsAsc();
  }

  public TaskExecutionReport transferStatus(
      TaskExecutionReport taskExecutionReport, boolean success, boolean running, boolean failure)
      throws UndefinedStatusException {
    if (running) {
      return logAndSetStatus(taskExecutionReport, StatusEnum.RUNNING);
    } else {
      if (failure) {
        return logAndSetStatus(taskExecutionReport, StatusEnum.FAILURE);
      }
      if (success) {
        return logAndSetStatus(taskExecutionReport, StatusEnum.SUCCESS);
      }
    }
    throw new UndefinedStatusException(
        String.format(
            "Status for TaskExecutionReport with taskId [%s] is undefined",
            taskExecutionReport.getTaskId()));
  }

  private TaskExecutionReport logAndSetStatus(
      TaskExecutionReport taskExecutionReport, StatusEnum statusEnum) {
    log.info(
        "Setting taskExecutionReport status to [{}] for taskId [{}]",
        statusEnum,
        taskExecutionReport.getTaskId());
    taskExecutionReport.setStatus(statusEnum);
    return taskExecutionReport;
  }

  public List<TaskExecutionReport> getMissingExecutionTime() {
    List<TaskExecutionReport> taskExecutionReportList =
        taskExecutionReportRepository
            .findByStartDateTimeIsNotNullAndEndDateTimeIsNotNullAndExecutionTimeSecondsIsNull();
    log.info(
        "Found [{}] taskExecutionReport with non-null startDateTime, non-null endDateTime, null executionTimeSeconds",
        taskExecutionReportList.size());
    return taskExecutionReportList;
  }
}
