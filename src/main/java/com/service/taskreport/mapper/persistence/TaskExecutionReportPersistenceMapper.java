package com.service.taskreport.mapper.persistence;

import com.service.taskreport.entity.TaskExecutionReport;
import com.service.taskreport.enums.StatusEnum;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

public interface TaskExecutionReportPersistenceMapper {
  // TODO: implement delete & save methods
  @Select("SELECT * FROM task_execution_report WHERE task_execution_id = #{id}")
  @Results({
    @Result(property = "id", column = "task_execution_id"),
    @Result(property = "taskId", column = "task_id"),
    @Result(property = "startDateTime", column = "start_date_time"),
    @Result(property = "endDateTime", column = "end_date_time"),
    @Result(property = "executionTimeSeconds", column = "execution_time_seconds"),
    @Result(property = "errorMessage", column = "error_message"),
    @Result(property = "status", column = "status"),
    @Result(property = "insertDate", column = "insert_date"),
    @Result(property = "updateDate", column = "update_date")
  })
  Optional<TaskExecutionReport> findById(@Param("id") Integer id);

  @Select("SELECT * FROM task_execution_report WHERE status = #{status}")
  @Results({
    @Result(property = "id", column = "task_execution_id"),
    @Result(property = "taskId", column = "task_id"),
    @Result(property = "startDateTime", column = "start_date_time"),
    @Result(property = "endDateTime", column = "end_date_time"),
    @Result(property = "executionTimeSeconds", column = "execution_time_seconds"),
    @Result(property = "errorMessage", column = "error_message"),
    @Result(property = "status", column = "status"),
    @Result(property = "insertDate", column = "insert_date"),
    @Result(property = "updateDate", column = "update_date")
  })
  List<TaskExecutionReport> findByStatus(@Param("status") StatusEnum status);

  @Select(
      "SELECT * FROM task_execution_report WHERE execution_time_seconds IS NOT NULL ORDER BY execution_time_seconds DESC")
  @Results({
    @Result(property = "id", column = "task_execution_id"),
    @Result(property = "taskId", column = "task_id"),
    @Result(property = "startDateTime", column = "start_date_time"),
    @Result(property = "endDateTime", column = "end_date_time"),
    @Result(property = "executionTimeSeconds", column = "execution_time_seconds"),
    @Result(property = "errorMessage", column = "error_message"),
    @Result(property = "status", column = "status"),
    @Result(property = "insertDate", column = "insert_date"),
    @Result(property = "updateDate", column = "update_date")
  })
  List<TaskExecutionReport> findByExecutionTimeSecondsIsNotNullOrderByExecutionTimeSecondsDesc();

  @Select(
      "SELECT * FROM task_execution_report WHERE start_date_time IS NOT NULL AND end_date_time IS NOT NULL AND execution_time_seconds IS NULL")
  @Results({
    @Result(property = "id", column = "task_execution_id"),
    @Result(property = "taskId", column = "task_id"),
    @Result(property = "startDateTime", column = "start_date_time"),
    @Result(property = "endDateTime", column = "end_date_time"),
    @Result(property = "executionTimeSeconds", column = "execution_time_seconds"),
    @Result(property = "errorMessage", column = "error_message"),
    @Result(property = "status", column = "status"),
    @Result(property = "insertDate", column = "insert_date"),
    @Result(property = "updateDate", column = "update_date")
  })
  List<TaskExecutionReport>
      findByStartDateTimeIsNotNullAndEndDateTimeIsNotNullAndExecutionTimeSecondsIsNull();
}
