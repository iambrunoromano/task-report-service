package com.service.taskreport.service;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.exception.TaskStepExecutionReportBadRequestException;
import com.service.taskreport.exception.TaskStepExecutionReportNotFoundException;
import com.service.taskreport.mapper.persistence.TaskExecutionReportPersistenceMapper;
import com.service.taskreport.mapper.persistence.TaskStepExecutionReportPersistenceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskStepExecutionReportService {

  private final TaskStepExecutionReportPersistenceMapper taskStepExecutionReportPersistenceMapper;

  @Autowired
  TaskStepExecutionReportService(
      TaskStepExecutionReportPersistenceMapper taskStepExecutionReportPersistenceMapper) {
    this.taskStepExecutionReportPersistenceMapper = taskStepExecutionReportPersistenceMapper;
  }

  public List<TaskStepExecutionReport> getAll() {
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        taskStepExecutionReportPersistenceMapper.findAll();
    log.info("Found [{}] taskStepExecutionReport", taskStepExecutionReportList.size());
    return taskStepExecutionReportList;
  }

  public TaskStepExecutionReport getById(Integer id)
      throws TaskStepExecutionReportNotFoundException {
    Optional<TaskStepExecutionReport> optionalTaskExecutionReport =
        taskStepExecutionReportPersistenceMapper.findById(id);
    if (optionalTaskExecutionReport.isPresent()) {
      TaskStepExecutionReport taskStepExecutionReport = optionalTaskExecutionReport.get();
      log.info("Found one taskStepExecutionReport for id [{}]: [{}]", id, taskStepExecutionReport);
      return taskStepExecutionReport;
    }
    throw new TaskStepExecutionReportNotFoundException(
        String.format("TaskStepExecutionReport for id [%s] not found", id));
  }

  public List<TaskStepExecutionReport> getByTaskExecutionId(Integer taskExecutionId)
      throws TaskStepExecutionReportNotFoundException {
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        taskStepExecutionReportPersistenceMapper.findByTaskExecutionId(taskExecutionId);
    return checkListNotEmpty(taskStepExecutionReportList, taskExecutionId);
  }

  public List<TaskStepExecutionReport> getSortedByTaskExecutionId(
      Integer taskExecutionId, Sort.Direction direction, String columnName)
      throws TaskStepExecutionReportNotFoundException {
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        taskStepExecutionReportPersistenceMapper.findByTaskExecutionId(taskExecutionId, direction.toString(), columnName);
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
    log.info(
        "Found [{}] taskStepExecutionReport for taskExecutionId [{}}]",
        taskStepExecutionReportList.size(),
        taskExecutionId);
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
        taskStepExecutionReportPersistenceMapper.findByTaskExecutionId(taskExecutionId);
    log.info(
        "Found [{}] taskStepExecutionReports for taskExecutionId [{}]",
        taskStepExecutionReportList.size(),
        taskExecutionId);
    if (!taskStepExecutionReportList.isEmpty()) {
      for (TaskStepExecutionReport taskStepExecutionReport : taskStepExecutionReportList) {
        taskStepExecutionReportPersistenceMapper.delete(taskStepExecutionReport.getId());
      }
      log.info(
          "Deleted [{}] taskStepExecutionReports for taskExecutionId [{}]",
          taskStepExecutionReportList.size(),
          taskExecutionId);
    }
  }

  public TaskStepExecutionReport save(TaskStepExecutionReport taskStepExecutionReport) {
    taskStepExecutionReport = computeSeconds(taskStepExecutionReport);
    taskStepExecutionReportPersistenceMapper.insert(taskStepExecutionReport);
    log.info(
        "Saved taskStepExecutionReport with taskExecutionId [{}] - id [{}]",
        taskStepExecutionReport.getTaskExecutionId(),
        taskStepExecutionReport.getId());
    return taskStepExecutionReport;
  }

  public void delete(Integer id) throws TaskStepExecutionReportNotFoundException {
    getById(id);
    taskStepExecutionReportPersistenceMapper.delete(id);
    log.info("Deleted taskStepExecutionReport for id [{}]", id);
  }

  private List<TaskStepExecutionReport> getByTaskExecutionReports(
      List<TaskExecutionReport> taskExecutionReportList) {
    log.info(
        "Creating list of taskStepExecutionReports related to [{}] taskExecutionReports",
        taskExecutionReportList.size());
    List<TaskStepExecutionReport> taskStepExecutionReportList = new ArrayList<>();
    for (TaskExecutionReport taskExecutionReport : taskExecutionReportList) {
      taskStepExecutionReportList.addAll(
          taskStepExecutionReportPersistenceMapper.findByTaskExecutionId(
              taskExecutionReport.getId()));
    }
    log.info(
        "Returning list of [{}] taskStepExecutionReports related to [{}] taskExecutionReports",
        taskStepExecutionReportList.size(),
        taskExecutionReportList.size());
    return taskStepExecutionReportList;
  }

  public TaskStepExecutionReport computeSeconds(TaskStepExecutionReport taskStepExecutionReport) {
    log.info(
        "Computing executionTimeSeconds for taskStepExecutionReport with taskExecutionId [{}]",
        taskStepExecutionReport.getTaskExecutionId());
    if (taskStepExecutionReport.getEndDateTime() != null
        && taskStepExecutionReport.getStartDateTime() != null) {
      long difference =
          taskStepExecutionReport.getEndDateTime().getTime()
              - taskStepExecutionReport.getStartDateTime().getTime();
      int computedSeconds = Math.round(difference / 1000);
      log.info(
          "Computed executionTimeSeconds as [{}] for taskExecutionReport with taskExecutionId [{}]",
          computedSeconds,
          taskStepExecutionReport.getTaskExecutionId());
      taskStepExecutionReport.setExecutionTimeSeconds(computedSeconds);
    }
    return taskStepExecutionReport;
  }

  public List<TaskStepExecutionReport> getMissingExecutionTime() {
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        taskStepExecutionReportPersistenceMapper
            .findByStartDateTimeIsNotNullAndEndDateTimeIsNotNullAndExecutionTimeSecondsIsNull();
    log.info(
        "Found [{}] taskStepExecutionReport with non-null startDateTime, non-null endDateTime, null executionTimeSeconds",
        taskStepExecutionReportList.size());
    return taskStepExecutionReportList;
  }
}
