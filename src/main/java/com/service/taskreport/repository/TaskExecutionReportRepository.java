package com.service.taskreport.repository;

import com.service.taskreport.entity.TaskExecutionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskExecutionReportRepository extends JpaRepository<TaskExecutionReport, Integer> {
  Optional<TaskExecutionReport> findById(Integer id);
}
