package com.service.taskreport.service;

import com.service.taskreport.TestUtility;
import com.service.taskreport.entity.TaskStepExecutionReport;
import com.service.taskreport.enums.TaskStepExecutionReportColumnNameEnum;
import com.service.taskreport.exception.TaskStepExecutionReportBadRequestException;
import com.service.taskreport.exception.TaskStepExecutionReportNotFoundException;
import com.service.taskreport.mapper.persistence.TaskStepExecutionReportPersistenceMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskStepExecutionReportServiceTest extends TestUtility {
  private final TaskStepExecutionReportPersistenceMapper taskStepExecutionReportPersistenceMapper =
      Mockito.mock(TaskStepExecutionReportPersistenceMapper.class);
  private final TaskStepExecutionReportService taskStepExecutionReportService =
      new TaskStepExecutionReportService(taskStepExecutionReportPersistenceMapper);

  @Test
  void getAllTest() {
    BDDMockito.given(taskStepExecutionReportPersistenceMapper.findAll())
        .willReturn(buildTaskStepExecutionReportList());
    assertEquals(buildTaskStepExecutionReportList(), taskStepExecutionReportService.getAll());
  }

  @Test
  void getByIdFoundTest() throws TaskStepExecutionReportNotFoundException {
    BDDMockito.given(taskStepExecutionReportPersistenceMapper.findById(Mockito.anyInt()))
        .willReturn(Optional.of(buildTaskStepExecutionReport()));
    assertEquals(buildTaskStepExecutionReport(), taskStepExecutionReportService.getById(ID));
  }

  @Test
  void getByIdNotFoundTest() {
    BDDMockito.given(taskStepExecutionReportPersistenceMapper.findById(Mockito.anyInt()))
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
    BDDMockito.given(
            taskStepExecutionReportPersistenceMapper.findByTaskExecutionId(TASK_EXECUTION_ID))
        .willReturn(buildTaskStepExecutionReportList());
    assertEquals(
        buildTaskStepExecutionReportList(),
        taskStepExecutionReportService.getByTaskExecutionId(TASK_EXECUTION_ID));
  }

  @Test
  void getSortedByTaskExecutionIdTest() throws TaskStepExecutionReportNotFoundException {
    BDDMockito.given(
            taskStepExecutionReportPersistenceMapper.findByTaskExecutionId(
                TASK_EXECUTION_ID,
                Sort.Direction.ASC.toString(),
                TaskStepExecutionReportColumnNameEnum.startDateTime.getValue()))
        .willReturn(buildTaskStepExecutionReportList());
    assertEquals(
        buildTaskStepExecutionReportList(),
        taskStepExecutionReportService.getSortedByTaskExecutionId(
            TASK_EXECUTION_ID,
            Sort.Direction.ASC,
            TaskStepExecutionReportColumnNameEnum.startDateTime.getValue()));
  }

  @Test
  void getByTaskExecutionReportListEmptyTaskExecutionReportListTest() {
    TaskStepExecutionReportBadRequestException actualException =
        assertThrows(
            TaskStepExecutionReportBadRequestException.class,
            () -> {
              taskStepExecutionReportService.getByTaskExecutionReportList(new ArrayList<>());
            });
    assertEquals("TaskExecutionReport empty list", actualException.getMessage());
  }

  @Test
  void getByTaskExecutionReportListEmptyTaskStepExecutionReportListTest() {
    BDDMockito.given(taskStepExecutionReportPersistenceMapper.findByTaskExecutionId(ID))
        .willReturn(new ArrayList<>());
    TaskStepExecutionReportNotFoundException actualException =
        assertThrows(
            TaskStepExecutionReportNotFoundException.class,
            () -> {
              taskStepExecutionReportService.getByTaskExecutionReportList(
                  buildTaskExecutionReportList());
            });
    assertEquals(
        String.format(
            "TaskStepExecutionReport for taskExecutionReportList with ids [%s] not found",
            Arrays.asList(ID, ID)),
        actualException.getMessage());
  }

  @Test
  void getByTaskExecutionReportListTest()
      throws TaskStepExecutionReportBadRequestException, TaskStepExecutionReportNotFoundException {
    BDDMockito.given(taskStepExecutionReportPersistenceMapper.findByTaskExecutionId(ID))
        .willReturn(buildTaskStepExecutionReportList());
    List<TaskStepExecutionReport> expectedTaskStepExecutionReportList = new ArrayList<>();
    expectedTaskStepExecutionReportList.addAll(buildTaskStepExecutionReportList());
    expectedTaskStepExecutionReportList.addAll(buildTaskStepExecutionReportList());
    assertEquals(
        expectedTaskStepExecutionReportList,
        taskStepExecutionReportService.getByTaskExecutionReportList(
            buildTaskExecutionReportList()));
  }

  @Test
  void saveTest() {
    assertEquals(
        buildTaskStepExecutionReport(),
        taskStepExecutionReportService.save(buildTaskStepExecutionReport()));
  }

  @Test
  void deleteNotFoundTest() {
    BDDMockito.given(taskStepExecutionReportPersistenceMapper.findById(Mockito.anyInt()))
        .willReturn(Optional.empty());
    TaskStepExecutionReportNotFoundException actualException =
        assertThrows(
            TaskStepExecutionReportNotFoundException.class,
            () -> {
              taskStepExecutionReportService.delete(ID);
            });
    assertEquals(
        String.format("TaskStepExecutionReport for id [%s] not found", ID),
        actualException.getMessage());
  }
}
