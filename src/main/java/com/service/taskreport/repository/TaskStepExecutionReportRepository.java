package com.service.taskreport.repository;

import com.service.taskreport.entity.TaskStepExecutionReport;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskStepExecutionReportRepository
    extends JpaRepository<TaskStepExecutionReport, Integer> {
  Optional<TaskStepExecutionReport> findById(Integer id);

  List<TaskStepExecutionReport> findByTaskExecutionId(Integer taskExecutionId, Sort sort);

  List<TaskStepExecutionReport> findByTaskExecutionId(Integer taskExecutionId);

  List<TaskStepExecutionReport>
      findByStartDateTimeIsNotNullAndEndDateTimeIsNotNullAndExecutionTimeSecondsIsNull();
}
