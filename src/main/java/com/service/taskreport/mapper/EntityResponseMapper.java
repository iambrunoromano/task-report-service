package com.service.taskreport.mapper;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.response.TaskExecutionReportResponse;
import com.service.taskreport.response.TaskStepExecutionReportResponse;
import org.mapstruct.Mapper;

@Mapper
public interface EntityResponseMapper {
  TaskExecutionReportResponse taskExecutionReportToTaskExecutionReportResponse(
      TaskExecutionReport taskExecutionReport);

  TaskStepExecutionReportResponse taskStepExecutionReportToTaskStepExecutionReportResponse(
      TaskStepExecutionReport taskStepExecutionReport);
}
