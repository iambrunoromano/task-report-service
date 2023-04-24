package com.service.taskreport.service;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.enums.StatusEnum;
import com.service.taskreport.exception.TaskExecutionReportNotFoundException;
import com.service.taskreport.exception.UndefinedStatusException;
import com.service.taskreport.repository.TaskExecutionReportRepository;
import com.service.taskreport.repository.TaskStepExecutionReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskExecutionReportService {

  private final TaskExecutionReportRepository taskExecutionReportRepository;
  private final TaskStepExecutionReportRepository taskStepExecutionReportRepository;
  private final TaskStepExecutionReportService taskStepExecutionReportService;

  @Autowired
  TaskExecutionReportService(
      TaskExecutionReportRepository taskExecutionReportRepository,
      TaskStepExecutionReportRepository taskStepExecutionReportRepository,
      TaskStepExecutionReportService taskStepExecutionReportService) {
    this.taskExecutionReportRepository = taskExecutionReportRepository;
    this.taskStepExecutionReportRepository = taskStepExecutionReportRepository;
    this.taskStepExecutionReportService = taskStepExecutionReportService;
  }

  public List<TaskExecutionReport> getAll() {
    return taskExecutionReportRepository.findAll();
  }

  public TaskExecutionReport getById(Integer id) throws TaskExecutionReportNotFoundException {
    Optional<TaskExecutionReport> optionalTaskExecutionReport =
        taskExecutionReportRepository.findById(id);
    if (optionalTaskExecutionReport.isPresent()) {
      return optionalTaskExecutionReport.get();
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
      for (TaskStepExecutionReport taskStepExecutionReport : taskStepExecutionReportList) {
        taskStepExecutionReport.setTaskExecutionId(taskExecutionReport.getId());
        taskStepExecutionReport =
            taskStepExecutionReportService.computeSeconds(taskStepExecutionReport);
        taskStepExecutionReportRepository.save(taskStepExecutionReport);
      }
    }
    return taskExecutionReport;
  }

  public void save(TaskExecutionReport taskExecutionReport) {
    taskExecutionReport = computeSeconds(taskExecutionReport);
    taskExecutionReportRepository.save(taskExecutionReport);
  }

  public TaskExecutionReport computeSeconds(TaskExecutionReport taskExecutionReport) {
    if (taskExecutionReport.getEndDateTime() != null
        && taskExecutionReport.getStartDateTime() != null) {
      long difference =
          taskExecutionReport.getEndDateTime().getTime()
              - taskExecutionReport.getStartDateTime().getTime();
      taskExecutionReport.setExecutionTimeSeconds(Math.round(difference / 1000));
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
      success = false;
    }
    for (TaskStepExecutionReport taskStepExecutionReport : taskStepExecutionReportList) {
      if (!StatusEnum.SUCCESS.equals(taskStepExecutionReport.getStatus())) {
        success = false;
      }
      if (StatusEnum.RUNNING.equals(taskStepExecutionReport.getStatus())) {
        running = true;
      }
      if (StatusEnum.FAILURE.equals(taskStepExecutionReport.getStatus())) {
        failure = true;
      }
    }
    return transferStatus(taskExecutionReport, success, running, failure);
  }

  public List<TaskExecutionReport> getByStatus(StatusEnum status)
      throws TaskExecutionReportNotFoundException {
    List<TaskExecutionReport> taskExecutionReportList =
        taskExecutionReportRepository.findByStatus(status);
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
      taskExecutionReport.setStatus(StatusEnum.RUNNING);
      return taskExecutionReport;
    } else {
      if (failure) {
        taskExecutionReport.setStatus(StatusEnum.FAILURE);
        return taskExecutionReport;
      }
      if (success) {
        taskExecutionReport.setStatus(StatusEnum.SUCCESS);
        return taskExecutionReport;
      }
    }
    throw new UndefinedStatusException(
        String.format(
            "Status for TaskExecutionReport with taskId [%s] is undefined",
            taskExecutionReport.getTaskId()));
  }

  public List<TaskExecutionReport> getMissingExecutionTime() {
    return taskExecutionReportRepository
        .findByStartDateTimeIsNotNullAndEndDateTimeIsNotNullAndExecutionTimeSecondsIsNull();
  }
}
