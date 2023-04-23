package com.service.taskreport.rest;

import com.service.taskreport.TestUtility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class TaskExecutionReportControllerTest extends TestUtility {

  private final TaskExecutionReportController taskExecutionReportController;

  @Autowired
  TaskExecutionReportControllerTest(TaskExecutionReportController taskExecutionReportController) {
    this.taskExecutionReportController = taskExecutionReportController;
  }

  @Test
  @Sql("classpath:task_get_all.sql")
  void getAllTest() {
    ResponseEntity actualResponse = taskExecutionReportController.getAll();
    assertEquals(buildGetAllTaskResponses(), actualResponse.getBody());
    assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
  }
}
