package com.service.taskreport.mapper;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.response.TaskExecutionReportResponse;
import com.service.taskreport.response.TaskStepExecutionReportResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EntityResponseMapper {

  TaskStepExecutionReportResponse taskStepExecutionReportToTaskStepExecutionReportResponse(
      TaskStepExecutionReport taskStepExecutionReport);

  List<TaskStepExecutionReportResponse>
      taskStepExecutionReportListToTaskStepExecutionReportResponseList(
          List<TaskStepExecutionReport> taskStepExecutionReportList);

  TaskExecutionReportResponse taskExecutionReportToTaskExecutionReportResponse(
      TaskExecutionReport taskExecutionReport);

  List<TaskExecutionReportResponse> taskExecutionReportListToTaskExecutionReportResponseList(
      List<TaskExecutionReport> taskExecutionReportList);
}
