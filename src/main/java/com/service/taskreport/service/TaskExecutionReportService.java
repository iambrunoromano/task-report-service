package com.service.taskreport.service;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.enums.StatusEnum;
import com.service.taskreport.exception.TaskExecutionReportNotFoundException;
import com.service.taskreport.exception.TaskStepExecutionReportNotFoundException;
import com.service.taskreport.exception.UndefinedStatusException;
import com.service.taskreport.repository.TaskExecutionReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskExecutionReportService {

  private final TaskExecutionReportRepository taskExecutionReportRepository;

  @Autowired
  TaskExecutionReportService(TaskExecutionReportRepository taskExecutionReportRepository) {
    this.taskExecutionReportRepository = taskExecutionReportRepository;
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

  public TaskExecutionReport save(TaskExecutionReport taskExecutionReport)
      throws TaskStepExecutionReportNotFoundException, UndefinedStatusException {
    taskExecutionReport = setTaskExecutionReportStatus(taskExecutionReport);
    return taskExecutionReportRepository.save(taskExecutionReport);
  }

  public void delete(Integer id) throws TaskExecutionReportNotFoundException {
    TaskExecutionReport taskExecutionReport = getById(id);
    taskExecutionReportRepository.delete(taskExecutionReport);
  }

  public TaskExecutionReport setTaskExecutionReportStatus(TaskExecutionReport taskExecutionReport)
      throws TaskStepExecutionReportNotFoundException, UndefinedStatusException {
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        checkTaskStepExecutionReportList(taskExecutionReport);
    boolean success = true;
    boolean running = false;
    boolean failure = false;
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
    return setTaskExecutionReportStatus(taskExecutionReport, success, running, failure);
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

  private TaskExecutionReport setTaskExecutionReportStatus(
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
            "Status for TaskExecutionReport with id [%s] is undefined",
            taskExecutionReport.getId()));
  }

  private List<TaskStepExecutionReport> checkTaskStepExecutionReportList(
      TaskExecutionReport taskExecutionReport) throws TaskStepExecutionReportNotFoundException {
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        taskExecutionReport.getTaskStepExecutionReports();
    if (taskStepExecutionReportList.isEmpty()) {
      throw new TaskStepExecutionReportNotFoundException(
          String.format(
              "TaskStepExecutionReport for TaskExecutionReport with id [%s] not found",
              taskExecutionReport.getId()));
    }
    return taskStepExecutionReportList;
  }
}
