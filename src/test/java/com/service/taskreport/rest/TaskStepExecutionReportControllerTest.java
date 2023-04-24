package com.service.taskreport.rest;

import com.service.taskreport.TestUtility;
import com.service.taskreport.enums.TaskStepExecutionReportColumnNameEnum;
import com.service.taskreport.exception.TaskExecutionReportNotFoundException;
import com.service.taskreport.exception.TaskStepExecutionReportNotFoundException;
import com.service.taskreport.response.TaskStepExecutionReportResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class TaskStepExecutionReportControllerTest extends TestUtility {

  private final TaskStepExecutionReportController taskStepExecutionReportController;

  @Autowired
  TaskStepExecutionReportControllerTest(
      TaskStepExecutionReportController taskStepExecutionReportController) {
    this.taskStepExecutionReportController = taskStepExecutionReportController;
  }

  @Test
  @Sql("classpath:step/get_all.sql")
  void getAllTest() {
    ResponseEntity<List<TaskStepExecutionReportResponse>> actualResponse =
        taskStepExecutionReportController.getAll();
    assertEquals(buildGetAllTaskStepResponses(), actualResponse.getBody());
    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
  }

  @Test
  @Sql("classpath:step/get_by_id.sql")
  void getByIdTest() throws TaskStepExecutionReportNotFoundException {
    ResponseEntity<TaskStepExecutionReportResponse> actualResponse =
        taskStepExecutionReportController.getById(FIFTH_ID);
    assertEquals(buildFirstTaskStepResponses(FIFTH_ID).get(0), actualResponse.getBody());
    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
  }

  @Test
  @Sql("classpath:task/get_by_id_task_step_not_found.sql")
  void getByIdTaskStepNotFoundTest() {
    TaskStepExecutionReportNotFoundException actualException =
        assertThrows(
            TaskStepExecutionReportNotFoundException.class,
            () -> {
              taskStepExecutionReportController.getById(SIXTH_ID);
            });
    assertEquals(
        String.format("TaskStepExecutionReport for id [%s] not found", SIXTH_ID),
        actualException.getMessage());
  }

  @Test
  @Sql("classpath:step/create.sql")
  void createTest() throws TaskExecutionReportNotFoundException {
    ResponseEntity<TaskStepExecutionReportResponse> actualResponse =
        taskStepExecutionReportController.create(buildTaskStepExecutionReportRequest(FOURTH_ID));
    assertTaskStepCreateTestEquals(
        buildFirstTaskStepResponses(FOURTH_ID).get(0), actualResponse.getBody());
    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
  }

  @Test
  @Sql("classpath:step/create_task_not_found.sql")
  void createTaskNotFoundTest() {
    TaskExecutionReportNotFoundException actualException =
        assertThrows(
            TaskExecutionReportNotFoundException.class,
            () -> {
              taskStepExecutionReportController.create(
                  buildTaskStepExecutionReportRequest(SEVENTH_ID));
            });
    assertEquals(
        String.format("TaskExecutionReport for id [%s] not found", SEVENTH_ID),
        actualException.getMessage());
  }

  @Test
  @Sql("classpath:step/delete.sql")
  void deleteTest() throws TaskStepExecutionReportNotFoundException {
    ResponseEntity<Void> actualResponse = taskStepExecutionReportController.delete(EIGHTH_ID);
    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
  }

  @Test
  @Sql("classpath:step/delete_step_not_found.sql")
  void deleteStepNotFoundTest() {
    TaskStepExecutionReportNotFoundException actualException =
        assertThrows(
            TaskStepExecutionReportNotFoundException.class,
            () -> {
              taskStepExecutionReportController.delete(EIGHTH_ID);
            });
    assertEquals(
        String.format("TaskStepExecutionReport for id [%s] not found", EIGHTH_ID),
        actualException.getMessage());
  }

  @Test
  @Sql("classpath:step/get_by_task_execution_id.sql")
  void getByTaskExecutionIdTest() throws TaskStepExecutionReportNotFoundException {
    ResponseEntity<List<TaskStepExecutionReportResponse>> actualResponse =
        taskStepExecutionReportController.getByTaskExecutionId(
            SECOND_ID,
            Sort.Direction.ASC,
            TaskStepExecutionReportColumnNameEnum.executionTimeSeconds);
    assertEquals(buildGetByTaskExecutionIdResponses(SECOND_ID), actualResponse.getBody());
    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
  }

  @Test
  @Sql("classpath:step/get_by_task_execution_id_task_step_not_found.sql")
  void getByTaskExecutionIdNotFoundTest() {
    TaskStepExecutionReportNotFoundException actualException =
        assertThrows(
            TaskStepExecutionReportNotFoundException.class,
            () -> {
              taskStepExecutionReportController.getByTaskExecutionId(
                  THIRD_ID,
                  Sort.Direction.ASC,
                  TaskStepExecutionReportColumnNameEnum.executionTimeSeconds);
            });
    assertEquals(
        String.format("TaskStepExecutionReport for taskExecutionId [%s] not found", THIRD_ID),
        actualException.getMessage());
  }
}
