package com.service.taskreport.service;

import com.service.taskreport.TestUtility;
import com.service.taskreport.enums.TaskStepExecutionReportColumnNameEnum;
import com.service.taskreport.exception.TaskExecutionReportNotFoundException;
import com.service.taskreport.exception.TaskStepExecutionReportNotFoundException;
import com.service.taskreport.repository.TaskStepExecutionReportRepository;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaskStepExecutionReportServiceTest extends TestUtility {
  private final TaskStepExecutionReportRepository taskStepExecutionReportRepository =
      Mockito.mock(TaskStepExecutionReportRepository.class);
  private final TaskStepExecutionReportService taskStepExecutionReportService =
      new TaskStepExecutionReportService(taskStepExecutionReportRepository);

  @Test
  void getAllTest() {
    BDDMockito.given(taskStepExecutionReportRepository.findAll())
        .willReturn(buildTaskStepExecutionReportList());
    assertEquals(buildTaskStepExecutionReportList(), taskStepExecutionReportService.getAll());
  }

  @Test
  void getByIdFoundTest() throws TaskStepExecutionReportNotFoundException {
    BDDMockito.given(taskStepExecutionReportRepository.findById(Mockito.anyInt()))
        .willReturn(Optional.of(buildTaskStepExecutionReport()));
    assertEquals(buildTaskStepExecutionReport(), taskStepExecutionReportService.getById(ID));
  }

  @Test
  void getByIdNotFoundTest() {
    BDDMockito.given(taskStepExecutionReportRepository.findById(Mockito.anyInt()))
        .willReturn(Optional.empty());
    TaskStepExecutionReportNotFoundException actualException =
        assertThrows(
            TaskStepExecutionReportNotFoundException.class,
            () -> {
              taskStepExecutionReportService.getById(ID);
            });
    assertEquals(
        String.format("TaskStepExecutionReport for id [%s] not found", ID),
        actualException.getMessage());
  }

  @Test
  void checkListNotEmptyTest() throws TaskStepExecutionReportNotFoundException {
    assertEquals(
        buildTaskStepExecutionReportList(),
        taskStepExecutionReportService.checkListNotEmpty(
            buildTaskStepExecutionReportList(), TASK_EXECUTION_ID));
  }

  @Test
  void checkListNotEmptyEmptyTest() {
    TaskStepExecutionReportNotFoundException actualException =
        assertThrows(
            TaskStepExecutionReportNotFoundException.class,
            () -> {
              taskStepExecutionReportService.checkListNotEmpty(
                  new ArrayList<>(), TASK_EXECUTION_ID);
            });
    assertEquals(
        String.format(
            "TaskStepExecutionReport for taskExecutionId [%s] not found", TASK_EXECUTION_ID),
        actualException.getMessage());
  }

  @Test
  void getByTaskExecutionIdTest() throws TaskStepExecutionReportNotFoundException {
    BDDMockito.given(taskStepExecutionReportRepository.findByTaskExecutionId(TASK_EXECUTION_ID))
        .willReturn(buildTaskStepExecutionReportList());
    assertEquals(
        buildTaskStepExecutionReportList(),
        taskStepExecutionReportService.getByTaskExecutionId(TASK_EXECUTION_ID));
  }

  @Test
  void getSortedByTaskExecutionIdTest() throws TaskStepExecutionReportNotFoundException {
    Sort sort =
        Sort.by(Sort.Direction.ASC, TaskStepExecutionReportColumnNameEnum.startDateTime.getValue());
    BDDMockito.given(
            taskStepExecutionReportRepository.findByTaskExecutionId(TASK_EXECUTION_ID, sort))
        .willReturn(buildTaskStepExecutionReportList());
    assertEquals(
        buildTaskStepExecutionReportList(),
        taskStepExecutionReportService.getSortedByTaskExecutionId(
            TASK_EXECUTION_ID,
            Sort.Direction.ASC,
            TaskStepExecutionReportColumnNameEnum.startDateTime.getValue()));
  }
}
