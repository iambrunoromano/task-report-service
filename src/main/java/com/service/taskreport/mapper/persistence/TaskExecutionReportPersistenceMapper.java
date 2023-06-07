package com.service.taskreport.mapper.persistence;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.enums.StatusEnum;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskExecutionReportPersistenceMapper {

  private SqlSessionFactory sqlSessionFactory;

  @Autowired
  public TaskExecutionReportPersistenceMapper(SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
  }

  public void insert(TaskExecutionReport taskExecutionReport) {
    SqlSession session = sqlSessionFactory.openSession();
    session.insert("insert", taskExecutionReport);
    session.commit();
    session.close();
  }

  public void update(TaskExecutionReport taskExecutionReport) {
    SqlSession session = sqlSessionFactory.openSession();
    session.insert("update", taskExecutionReport);
    session.commit();
    session.close();
  }

  public void delete(Integer id) {
    SqlSession session = sqlSessionFactory.openSession();
    session.insert("delete", id);
    session.commit();
    session.close();
  }

  public List<TaskExecutionReport> findAll() {
    SqlSession session = sqlSessionFactory.openSession();
    List<TaskExecutionReport> taskExecutionReportList = session.selectList("findAll");
    session.commit();
    session.close();
    return taskExecutionReportList;
  }

  public Optional<TaskExecutionReport> findById(Integer id) {
    SqlSession session = sqlSessionFactory.openSession();
    TaskExecutionReport taskExecutionReport = session.selectOne("findById", id);
    session.commit();
    session.close();
    if (taskExecutionReport == null) {
      return Optional.empty();
    }
    return Optional.of(taskExecutionReport);
  }

  public List<TaskExecutionReport> findByStatus(StatusEnum status) {
    SqlSession session = sqlSessionFactory.openSession();
    List<TaskExecutionReport> taskExecutionReportList =
        session.selectList("findByStatus", status.getValue());
    session.commit();
    session.close();
    return taskExecutionReportList;
  }

  public List<TaskExecutionReport>
      findByExecutionTimeSecondsIsNotNullOrderByExecutionTimeSecondsDesc() {
    SqlSession session = sqlSessionFactory.openSession();
    List<TaskExecutionReport> taskExecutionReportList =
        session.selectList("findByExecutionTimeSecondsIsNotNullOrderByExecutionTimeSecondsDesc");
    session.commit();
    session.close();
    return taskExecutionReportList;
  }

  public List<TaskExecutionReport>
      findByStartDateTimeIsNotNullAndEndDateTimeIsNotNullAndExecutionTimeSecondsIsNull() {
    SqlSession session = sqlSessionFactory.openSession();
    List<TaskExecutionReport> taskExecutionReportList =
        session.selectList(
            "findByStartDateTimeIsNotNullAndEndDateTimeIsNotNullAndExecutionTimeSecondsIsNull");
    session.commit();
    session.close();
    return taskExecutionReportList;
  }
}
