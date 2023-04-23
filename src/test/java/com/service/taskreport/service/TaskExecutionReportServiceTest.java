package com.service.taskreport.service;

import com.service.taskreport.TestUtility;
import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.enums.StatusEnum;
import com.service.taskreport.exception.TaskExecutionReportNotFoundException;
import com.service.taskreport.exception.UndefinedStatusException;
import com.service.taskreport.repository.TaskExecutionReportRepository;
import com.service.taskreport.repository.TaskStepExecutionReportRepository;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskExecutionReportServiceTest extends TestUtility {
  private final TaskExecutionReportRepository taskExecutionReportRepository =
      Mockito.mock(TaskExecutionReportRepository.class);
  private final TaskStepExecutionReportRepository taskStepExecutionReportRepository =
      Mockito.mock(TaskStepExecutionReportRepository.class);
  private final TaskExecutionReportService taskExecutionReportService =
      new TaskExecutionReportService(
          taskExecutionReportRepository, taskStepExecutionReportRepository);

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

  private void doTransferStatusTest(
      StatusEnum expectedStatus, boolean success, boolean running, boolean failure)
      throws UndefinedStatusException {
    TaskExecutionReport taskExecutionReport =
        taskExecutionReportService.transferStatus(
            buildTaskExecutionReport(), success, running, failure);
    assertEquals(expectedStatus, taskExecutionReport.getStatus());
  }
}
