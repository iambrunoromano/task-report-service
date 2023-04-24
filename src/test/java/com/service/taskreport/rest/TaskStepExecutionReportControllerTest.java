package com.service.taskreport.rest;

import com.service.taskreport.TestUtility;
import com.service.taskreport.response.TaskExecutionReportResponse;
import com.service.taskreport.response.TaskStepExecutionReportResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
}
