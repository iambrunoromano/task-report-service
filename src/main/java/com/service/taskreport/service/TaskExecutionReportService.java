package com.service.taskreport.service;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.exception.TaskExecutionReportNotFoundException;
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

  public TaskExecutionReport save(TaskExecutionReport taskExecutionReport) {
    return taskExecutionReportRepository.save(taskExecutionReport);
  }

  public void delete(Integer id) throws TaskExecutionReportNotFoundException {
    TaskExecutionReport taskExecutionReport = getById(id);
    taskExecutionReportRepository.delete(taskExecutionReport);
  }
}
