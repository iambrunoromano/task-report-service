package com.service.taskreport.service;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.repository.TaskExecutionReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
