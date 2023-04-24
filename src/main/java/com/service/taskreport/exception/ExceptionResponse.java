package com.service.taskreport.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionResponse {
  private String message;
  private String code;
  private List<DetailError> detailErrorList;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
  private ZonedDateTime dateTime;
}
