package com.service.taskreport.entity;

import com.service.taskreport.enums.StatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public abstract class AbstractEntity {
  @Id @GeneratedValue private Integer id;

  @Column(name = "star_date_time")
  private Timestamp startDateTime;

  @Column(name = "end_date_time")
  private Timestamp endDateTime;

  @Column(name = "execution_time_seconds")
  private Integer executionTimeSeconds;

  @Column(name = "error_message")
  private String errorMessage;

  @CreationTimestamp
  @Column(
      name = "insert_date",
      updatable = false,
      insertable = false,
      columnDefinition = " DATETIME DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime insertDate;

  @UpdateTimestamp
  @Column(
      name = "update_date",
      insertable = false,
      columnDefinition = " DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  private LocalDateTime updateDate;
}
