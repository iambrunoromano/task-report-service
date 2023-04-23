package com.service.taskreport.mapper;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.request.TaskExecutionReportRequest;
import com.service.taskreport.request.TaskStepExecutionReportRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestEntityMapper {
  TaskExecutionReport taskExecutionReportRequestToTaskExecutionReport(
      TaskExecutionReportRequest taskExecutionReportRequest);

  TaskStepExecutionReport taskStepExecutionReportRequestToTaskStepExecutionReport(
      TaskStepExecutionReportRequest taskExecutionReportRequest);

  List<TaskStepExecutionReport> taskStepExecutionReportRequestListToTaskStepExecutionReportList(
      List<TaskStepExecutionReportRequest> taskExecutionReportRequestList);
}
