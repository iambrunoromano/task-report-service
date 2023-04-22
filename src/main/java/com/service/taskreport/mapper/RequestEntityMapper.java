package com.service.taskreport.mapper;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.request.TaskExecutionReportRequest;
import com.service.taskreport.request.TaskStepExecutionReportRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RequestEntityMapper {
  TaskExecutionReport taskExecutionReportRequestToTaskExecutionReport(
      TaskExecutionReportRequest taskExecutionReportRequest);

  TaskStepExecutionReport taskStepExecutionReportRequestToTaskStepExecutionReport(
      TaskStepExecutionReportRequest taskExecutionReportRequest);
}
