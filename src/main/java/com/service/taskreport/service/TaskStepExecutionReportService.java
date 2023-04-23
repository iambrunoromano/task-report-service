package com.service.taskreport.service;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.exception.TaskStepExecutionReportBadRequestException;
import com.service.taskreport.exception.TaskStepExecutionReportNotFoundException;
import com.service.taskreport.repository.TaskStepExecutionReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

  public List<TaskStepExecutionReport> getByTaskExecutionId(Integer taskExecutionId)
      throws TaskStepExecutionReportNotFoundException {
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        taskStepExecutionReportRepository.findByTaskExecutionId(taskExecutionId);
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
              .map(taskExecutionReport -> taskExecutionReport.getTaskId())
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

  private List<TaskStepExecutionReport> getByTaskExecutionReports(
      List<TaskExecutionReport> taskExecutionReportList) {
    List<TaskStepExecutionReport> taskStepExecutionReportList = new ArrayList<>();
    for (TaskExecutionReport taskExecutionReport : taskExecutionReportList) {
      taskStepExecutionReportList.addAll(
          taskStepExecutionReportRepository.findByTaskExecutionId(taskExecutionReport.getId()));
    }
    return taskStepExecutionReportList;
  }
}
