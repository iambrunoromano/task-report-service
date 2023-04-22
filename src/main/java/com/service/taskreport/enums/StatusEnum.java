package com.service.taskreport.enums;

public enum StatusEnum {
  SUCCESS("SUCCESS"),
  FAILURE("FAILURE"),
  RUNNING("RUNNING");

  private final String value;

  StatusEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
