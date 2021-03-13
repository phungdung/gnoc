/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wfm.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.model.LogCallIpccEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class LogCallIpccDTO extends BaseDto {

  //Fields
  private String id;
  private String startCallTime;
  private String resultTime;
  private String woId;
  private String transactionId;
  private String userName;
  private String phone;
  private String recordFileCode;
  private String result;
  private String description;
  private String userCall;
  private String defaultSortField;

  public LogCallIpccDTO(String id, String startCallTime, String resultTime,
      String woId, String transactionId, String userName, String phone,
      String recordFileCode, String result, String description, String userCall) {
    this.id = id;
    this.startCallTime = startCallTime;
    this.resultTime = resultTime;
    this.woId = woId;
    this.transactionId = transactionId;
    this.userName = userName;
    this.phone = phone;
    this.recordFileCode = recordFileCode;
    this.result = result;
    this.description = description;
    this.userCall = userCall;
  }

  public LogCallIpccEntity toEntity() {
    LogCallIpccEntity model = new LogCallIpccEntity(
        !StringUtils.validString(id) ? null : Long.valueOf(id),
        !StringUtils.validString(startCallTime) ? null
            : DateTimeUtils.convertStringToDate(startCallTime),
        !StringUtils.validString(resultTime) ? null
            : DateTimeUtils.convertStringToDate(resultTime),
        !StringUtils.validString(woId) ? null : Long.valueOf(woId),
        transactionId,
        userName,
        phone,
        recordFileCode,
        result,
        description,
        userCall
    );
    return model;
  }
}
