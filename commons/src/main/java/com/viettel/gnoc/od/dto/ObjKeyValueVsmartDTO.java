/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.od.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Getter
@Setter
@Slf4j
public class ObjKeyValueVsmartDTO {

  private String key;
  private String value;
  private String controlType;
  private String keyCode;
  private String format;
  private String dataCode;
  private String dataType;
  private String type;
  private String defaultValue;
  private List<byte[]> lstFileArr;
  private List<String> lstFileName;

  @Override
  public ObjKeyValueVsmartDTO clone() {
    try {
      return (ObjKeyValueVsmartDTO) super.clone();
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }
  }

}
