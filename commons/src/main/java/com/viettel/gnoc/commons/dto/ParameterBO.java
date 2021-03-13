package com.viettel.gnoc.commons.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ParameterBO {

  private String name;
  private String value;
  private String type;
  private String separator;
  private String format;

  public ParameterBO() {
  }

  public ParameterBO(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public ParameterBO(String name, String value, String type) {
    this.name = name;
    this.value = value;
    this.type = type;
  }

  public ParameterBO(String name, String value, String type, String separator) {
    this.name = name;
    this.value = value;
    this.type = type;
    this.separator = separator;
  }

  public ParameterBO(String name, String value, String type, String separator, String format) {
    this.name = name;
    this.value = value;
    this.type = type;
    this.separator = separator;
    this.format = format;
  }
}
