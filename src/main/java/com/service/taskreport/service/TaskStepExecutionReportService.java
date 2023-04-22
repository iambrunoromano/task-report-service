package com.service.taskreport.service;

import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.repository.TaskStepExecutionReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
