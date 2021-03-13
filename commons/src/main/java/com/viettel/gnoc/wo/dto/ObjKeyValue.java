package com.viettel.gnoc.wo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class ObjKeyValue implements Cloneable {

  private String key;
  private String value;
  private int col;
  private int row;
  private String defaulValue;
  private String requiredImage;
  private String requiredComment;
  private String comment;

  @Override
  public ObjKeyValue clone() {
    try {
      return (ObjKeyValue) super.clone();
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }
  }

}
