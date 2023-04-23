package com.service.taskreport.repository;

import com.service.taskreport.entity.TaskStepExecutionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskStepExecutionReportRepository
    extends JpaRepository<TaskStepExecutionReport, Integer> {
  List<TaskStepExecutionReport> findByTaskExecutionId(Integer taskExecutionId);
}
