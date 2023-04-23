package com.service.taskreport.mapper;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.response.TaskExecutionReportResponse;
import com.service.taskreport.response.TaskStepExecutionReportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EntityResponseMapper {

  TaskStepExecutionReportResponse taskStepExecutionReportToTaskStepExecutionReportResponse(
      TaskStepExecutionReport taskStepExecutionReport);

  List<TaskStepExecutionReportResponse>
      taskStepExecutionReportListToTaskStepExecutionReportResponseList(
          List<TaskStepExecutionReport> taskStepExecutionReportList);

  @Mapping(target = "taskStepExecutionReports", source = "taskStepExecutionReportList")
  TaskExecutionReportResponse taskExecutionReportToTaskExecutionReportResponse(
      TaskExecutionReport taskExecutionReport,
      List<TaskStepExecutionReport> taskStepExecutionReportList);

  default List<TaskExecutionReportResponse>
      taskExecutionReportListToTaskExecutionReportResponseList(
          List<TaskExecutionReport> taskExecutionReportList,
          List<TaskStepExecutionReport> taskStepExecutionReportList) {
    List<TaskExecutionReportResponse> taskExecutionReportResponseList = new ArrayList<>();
    for (TaskExecutionReport taskExecutionReport : taskExecutionReportList) {
      List<TaskStepExecutionReport> filteredTaskStepExecutionReport =
          taskStepExecutionReportList.stream()
              .filter(
                  taskStepExecutionReport ->
                      taskExecutionReport
                          .getId()
                          .equals(taskStepExecutionReport.getTaskExecutionId()))
              .collect(Collectors.toList());
      taskExecutionReportResponseList.add(
          taskExecutionReportToTaskExecutionReportResponse(
              taskExecutionReport, filteredTaskStepExecutionReport));
    }
    return taskExecutionReportResponseList;
  }
}
