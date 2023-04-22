package com.service.taskreport.repository;

import com.service.taskreport.entity.TaskStepExecutionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStepExecutionReportRepository
    extends JpaRepository<TaskStepExecutionReport, Integer> {}
