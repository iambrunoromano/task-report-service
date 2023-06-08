package com.service.taskreport.mapper.persistence;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.entity.TaskStepExecutionReport;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class TaskStepExecutionReportPersistenceMapper {

  // TODO: refactor with common parent class methods

  private SqlSessionFactory sqlSessionFactory;

  @Autowired
  public TaskStepExecutionReportPersistenceMapper(SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
  }

  public void insert(TaskStepExecutionReport taskStepExecutionReport) {
    SqlSession session = sqlSessionFactory.openSession();
    session.insert("insertTaskStepExecutionReport", taskStepExecutionReport);
    session.commit();
    session.close();
  }

  public void update(TaskExecutionReport taskExecutionReport) {
    SqlSession session = sqlSessionFactory.openSession();
    session.insert("updateTaskStepExecutionReport", taskExecutionReport);
    session.commit();
    session.close();
  }

  public void delete(Integer id) {
    SqlSession session = sqlSessionFactory.openSession();
    session.insert("deleteTaskStepExecutionReport", id);
    session.commit();
    session.close();
  }

  public List<TaskStepExecutionReport> findAll() {
    SqlSession session = sqlSessionFactory.openSession();
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        session.selectList("findAllTaskStepExecutionReport");
    session.commit();
    session.close();
    return taskStepExecutionReportList;
  }

  public Optional<TaskStepExecutionReport> findById(Integer id) {
    SqlSession session = sqlSessionFactory.openSession();
    TaskStepExecutionReport taskStepExecutionReport =
        session.selectOne("findByIdTaskStepExecutionReport", id);
    session.commit();
    session.close();
    if (taskStepExecutionReport == null) {
      return Optional.empty();
    }
    return Optional.of(taskStepExecutionReport);
  }

  public List<TaskStepExecutionReport> findByTaskExecutionId(Integer taskExecutionId) {
    SqlSession session = sqlSessionFactory.openSession();
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        session.selectList("findByTaskExecutionIdTaskStepExecutionReport", taskExecutionId);
    session.commit();
    session.close();
    return taskStepExecutionReportList;
  }

  public List<TaskStepExecutionReport> findByTaskExecutionId(Integer taskExecutionId, Sort sort) {
    Map<String, Object> params = new HashMap<>();
    params.put("taskExecutionId", taskExecutionId);
    params.put("sort", sort.toString());
    SqlSession session = sqlSessionFactory.openSession();
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        session.selectList("findByTaskExecutionIdSortTaskStepExecutionReport", params);
    session.commit();
    session.close();
    return taskStepExecutionReportList;
  }

  public List<TaskStepExecutionReport>
      findByStartDateTimeIsNotNullAndEndDateTimeIsNotNullAndExecutionTimeSecondsIsNull() {
    SqlSession session = sqlSessionFactory.openSession();
    List<TaskStepExecutionReport> taskStepExecutionReportList =
        session.selectList(
            "findByStartDateTimeIsNotNullAndEndDateTimeIsNotNullAndExecutionTimeSecondsIsNullTaskStepExecutionReport");
    session.commit();
    session.close();
    return taskStepExecutionReportList;
  }
}
