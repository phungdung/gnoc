/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author tiennv
 */
@Getter
@Setter
@NoArgsConstructor
public class RestObjForBCCS {

  public String access_token;
  public boolean success;
  public String message;
  public List<String> errors;
  public Long causeErrExpId;
  public Long status;
  public String code;
  public String name;
  public String description;
  public String errorCode;
  public String result;

}
