package com.service.taskreport.service;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.exception.TaskStepExecutionReportBadRequestException;
import com.service.taskreport.exception.TaskStepExecutionReportNotFoundException;
import com.service.taskreport.repository.TaskStepExecutionReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskStepExecutionReportService {

  private final TaskStepExecutionReportRepository taskStepExecutionReportRepository;

  @Autowired
  TaskStepExecutionReportService(
      TaskStepExecutionReportRepository taskStepExecutionReportRepository) {
    this.taskStepExecutionReportRepository = taskStepExecutionReportRepository;
  }

  public List<TaskStepExecutionReport> getAll() {
    return taskStepExecutionReportRepository.findAll();
  }

  public TaskStepExecutionReport getById(Integer id)
      throws TaskStepExecutionReportNotFoundException {
    Optional<TaskStepExecutionReport> optionalTaskExecutionReport =
        taskStepExecutionReportRepository.findById(id);
    if (optionalTaskExecutionReport.isPresent()) {
      return optionalTaskExecutionReport.get();
    }
    throw new TaskStepExecutionReportNotFoundException(
        String.format("TaskStepExecutionReport for id [%s] not found", id));
  }

  public List<TaskStepExecutionReport> getByTaskExecutionId(Integer taskExecutionId)
      throws TaskStepExecutionReportNotFoundException {
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        taskStepExecutionReportRepository.findByTaskExecutionId(taskExecutionId);
    return checkListNotEmpty(taskStepExecutionReportList, taskExecutionId);
  }

  public List<TaskStepExecutionReport> getSortedByTaskExecutionId(
      Integer taskExecutionId, Sort.Direction direction, String columnName)
      throws TaskStepExecutionReportNotFoundException {
    Sort sort = Sort.by(direction, columnName);
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        taskStepExecutionReportRepository.findByTaskExecutionId(taskExecutionId, sort);
    return checkListNotEmpty(taskStepExecutionReportList, taskExecutionId);
  }

  public List<TaskStepExecutionReport> checkListNotEmpty(
      List<TaskStepExecutionReport> taskStepExecutionReportList, Integer taskExecutionId)
      throws TaskStepExecutionReportNotFoundException {
    if (taskStepExecutionReportList.isEmpty()) {
      throw new TaskStepExecutionReportNotFoundException(
          String.format(
              "TaskStepExecutionReport for taskExecutionId [%s] not found", taskExecutionId));
    }
    return taskStepExecutionReportList;
  }

  public List<TaskStepExecutionReport> getByTaskExecutionReportList(
      List<TaskExecutionReport> taskExecutionReportList)
      throws TaskStepExecutionReportNotFoundException, TaskStepExecutionReportBadRequestException {
    if (taskExecutionReportList.isEmpty()) {
      throw new TaskStepExecutionReportBadRequestException("TaskExecutionReport empty list");
    }
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        getByTaskExecutionReports(taskExecutionReportList);
    if (taskStepExecutionReportList.isEmpty()) {
      List<Integer> idList =
          taskExecutionReportList.stream()
              .map(TaskExecutionReport::getTaskId)
              .collect(Collectors.toList());
      throw new TaskStepExecutionReportNotFoundException(
          String.format(
              "TaskStepExecutionReport for taskExecutionReportList with ids [%s] not found",
              idList));
    }
    return taskStepExecutionReportList;
  }

  public void deleteByTaskExecutionId(Integer taskExecutionId) {
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        taskStepExecutionReportRepository.findByTaskExecutionId(taskExecutionId);
    if (!taskStepExecutionReportList.isEmpty()) {
      for (TaskStepExecutionReport taskStepExecutionReport : taskStepExecutionReportList) {
        taskStepExecutionReportRepository.delete(taskStepExecutionReport);
      }
    }
  }

  public TaskStepExecutionReport save(TaskStepExecutionReport taskStepExecutionReport) {
    taskStepExecutionReport = computeSeconds(taskStepExecutionReport);
    return taskStepExecutionReportRepository.save(taskStepExecutionReport);
  }

  public void delete(Integer id) throws TaskStepExecutionReportNotFoundException {
    TaskStepExecutionReport taskStepExecutionReport = getById(id);
    taskStepExecutionReportRepository.delete(taskStepExecutionReport);
  }

  private List<TaskStepExecutionReport> getByTaskExecutionReports(
      List<TaskExecutionReport> taskExecutionReportList) {
    List<TaskStepExecutionReport> taskStepExecutionReportList = new ArrayList<>();
    for (TaskExecutionReport taskExecutionReport : taskExecutionReportList) {
      taskStepExecutionReportList.addAll(
          taskStepExecutionReportRepository.findByTaskExecutionId(taskExecutionReport.getId()));
    }
    return taskStepExecutionReportList;
  }

  public TaskStepExecutionReport computeSeconds(TaskStepExecutionReport taskStepExecutionReport) {
    if (taskStepExecutionReport.getEndDateTime() != null
        && taskStepExecutionReport.getStartDateTime() != null) {
      long difference =
          taskStepExecutionReport.getEndDateTime().getTime()
              - taskStepExecutionReport.getStartDateTime().getTime();
      taskStepExecutionReport.setExecutionTimeSeconds(Math.round(difference / 1000));
    }
    return taskStepExecutionReport;
  }
}
