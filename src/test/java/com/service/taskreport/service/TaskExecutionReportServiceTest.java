package com.service.taskreport.service;

import com.service.taskreport.TestUtility;
import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.enums.StatusEnum;
import com.service.taskreport.exception.TaskExecutionReportNotFoundException;
import com.service.taskreport.exception.UndefinedStatusException;
import com.service.taskreport.repository.TaskExecutionReportRepository;
import com.service.taskreport.repository.TaskStepExecutionReportRepository;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskExecutionReportServiceTest extends TestUtility {
  private final TaskExecutionReportRepository taskExecutionReportRepository =
      Mockito.mock(TaskExecutionReportRepository.class);
  private final TaskStepExecutionReportRepository taskStepExecutionReportRepository =
      Mockito.mock(TaskStepExecutionReportRepository.class);
  private final TaskStepExecutionReportService taskStepExecutionReportService =
      Mockito.mock(TaskStepExecutionReportService.class);
  private final TaskExecutionReportService taskExecutionReportService =
      new TaskExecutionReportService(
          taskExecutionReportRepository,
          taskStepExecutionReportRepository,
          taskStepExecutionReportService);

  @Test
  void getAllTest() {
    BDDMockito.given(taskExecutionReportRepository.findAll())
        .willReturn(buildTaskExecutionReportList());
    assertEquals(buildTaskExecutionReportList(), taskExecutionReportService.getAll());
  }

  @Test
  void getByIdFoundTest() throws TaskExecutionReportNotFoundException {
    BDDMockito.given(taskExecutionReportRepository.findById(Mockito.anyInt()))
        .willReturn(Optional.of(buildTaskExecutionReport()));
    assertEquals(buildTaskExecutionReport(), taskExecutionReportService.getById(ID));
  }

  @Test
  void getByIdNotFoundTest() {
    BDDMockito.given(taskExecutionReportRepository.findById(Mockito.anyInt()))
        .willReturn(Optional.empty());
    TaskExecutionReportNotFoundException actualException =
        assertThrows(
            TaskExecutionReportNotFoundException.class,
            () -> {
              taskExecutionReportService.getById(ID);
            });
    assertEquals(
        String.format("TaskExecutionReport for id [%s] not found", ID),
        actualException.getMessage());
  }

  @Test
  void transferStatusRunningTest() throws UndefinedStatusException {
    doTransferStatusTest(StatusEnum.RUNNING, true, true, true);
  }

  @Test
  void transferStatusFailureTest() throws UndefinedStatusException {
    doTransferStatusTest(StatusEnum.FAILURE, true, false, true);
  }

  @Test
  void transferStatusSuccessTest() throws UndefinedStatusException {
    doTransferStatusTest(StatusEnum.SUCCESS, true, false, false);
  }

  @Test
  void transferStatusUndefinedTest() {
    UndefinedStatusException actualException =
        assertThrows(
            UndefinedStatusException.class,
            () -> {
              doTransferStatusTest(StatusEnum.SUCCESS, false, false, false);
            });
    assertEquals(
        String.format("Status for TaskExecutionReport with id [%s] is undefined", ID),
        actualException.getMessage());
  }

  @Test
  void getAllOrderByExecutionTimeSecondsTest() {
    BDDMockito.given(taskExecutionReportRepository.findAllByOrderByExecutionTimeSecondsAsc())
        .willReturn(buildTaskExecutionReportList());
    assertEquals(
        buildTaskExecutionReportList(),
        taskExecutionReportService.getAllOrderByExecutionTimeSeconds());
  }

  @Test
  void getByStatusFoundTest() throws TaskExecutionReportNotFoundException {
    BDDMockito.given(taskExecutionReportRepository.findByStatus(Mockito.any(StatusEnum.class)))
        .willReturn(buildTaskExecutionReportList());
    assertEquals(
        buildTaskExecutionReportList(), taskExecutionReportService.getByStatus(StatusEnum.RUNNING));
  }

  @Test
  void getByStatusNotFoundTest() {
    BDDMockito.given(taskExecutionReportRepository.findByStatus(Mockito.any(StatusEnum.class)))
        .willReturn(new ArrayList<>());
    TaskExecutionReportNotFoundException actualException =
        assertThrows(
            TaskExecutionReportNotFoundException.class,
            () -> {
              taskExecutionReportService.getByStatus(StatusEnum.RUNNING);
            });
    assertEquals(
        String.format(
            "TaskExecutionReport for status [%s] not found", StatusEnum.RUNNING.getValue()),
        actualException.getMessage());
  }

  @Test
  void setTaskExecutionReportStatusSuccessTest() throws UndefinedStatusException {
    TaskExecutionReport taskExecutionReport =
        taskExecutionReportService.setTaskExecutionReportStatus(
            buildTaskExecutionReport(), buildTaskStepExecutionReportList());
    assertEquals(StatusEnum.SUCCESS, taskExecutionReport.getStatus());
  }

  @Test
  void setTaskExecutionReportStatusRunningTest() throws UndefinedStatusException {
    doNonSuccessTaskExecutionReportStatusTest(StatusEnum.RUNNING);
  }

  @Test
  void setTaskExecutionReportStatusFailureTest() throws UndefinedStatusException {
    doNonSuccessTaskExecutionReportStatusTest(StatusEnum.FAILURE);
  }

  @Test
  void setTaskExecutionReportStatusUndefinedTest() {
    UndefinedStatusException actualException =
        assertThrows(
            UndefinedStatusException.class,
            () -> {
              taskExecutionReportService.setTaskExecutionReportStatus(
                  buildTaskExecutionReport(), new ArrayList<>());
            });
    assertEquals(
        String.format("Status for TaskExecutionReport with id [%s] is undefined", ID),
        actualException.getMessage());
  }

  @Test
  void deleteNotFoundTest() {
    BDDMockito.given(taskExecutionReportRepository.findById(Mockito.anyInt()))
        .willReturn(Optional.empty());
    TaskExecutionReportNotFoundException actualException =
        assertThrows(
            TaskExecutionReportNotFoundException.class,
            () -> {
              taskExecutionReportService.delete(ID);
            });
    assertEquals(
        String.format("TaskExecutionReport for id [%s] not found", ID),
        actualException.getMessage());
  }

  private void doNonSuccessTaskExecutionReportStatusTest(StatusEnum status)
      throws UndefinedStatusException {
    List<TaskStepExecutionReport> taskStepExecutionReportList = buildTaskStepExecutionReportList();
    taskStepExecutionReportList.get(0).setStatus(status);
    TaskExecutionReport taskExecutionReport =
        taskExecutionReportService.setTaskExecutionReportStatus(
            buildTaskExecutionReport(), taskStepExecutionReportList);
    assertEquals(status, taskExecutionReport.getStatus());
  }

  private void doTransferStatusTest(
      StatusEnum expectedStatus, boolean success, boolean running, boolean failure)
      throws UndefinedStatusException {
    TaskExecutionReport taskExecutionReport =
        taskExecutionReportService.transferStatus(
            buildTaskExecutionReport(), success, running, failure);
    assertEquals(expectedStatus, taskExecutionReport.getStatus());
  }
}
