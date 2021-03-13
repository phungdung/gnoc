/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrNodesEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class MrNodesDTO {

  private String mrNodeId;
  private String scheduleTelId;
  private String mrId;
  private String crId;
  private String woId;
  private String nodeIp;
  private String nodeCode;
  private String nodeName;
  private String status;
  private String comments;
  private String updateDate;
  private String isChecklistExisted;
  private String checklistStatus;
  //add
  private String recentDischargeCd;
  private String defaultSortField;
  //Constructor
  public MrNodesDTO() {
    setDefaultSortField("mrNodeId");
  }
  //constructor

  public MrNodesDTO(String mrNodeId, String scheduleTelId, String mrId, String crId, String woId, String nodeIp, String nodeCode, String nodeName, String status, String comments, String updateDate) {
    this.mrNodeId = mrNodeId;
    this.scheduleTelId = scheduleTelId;
    this.mrId = mrId;
    this.crId = crId;
    this.woId = woId;
    this.nodeIp = nodeIp;
    this.nodeCode = nodeCode;
    this.nodeName = nodeName;
    this.status = status;
    this.comments = comments;
    this.updateDate = updateDate;
  }

  public MrNodesEntity toEntity(){
    try{
      MrNodesEntity model = new MrNodesEntity(
          StringUtils.isStringNullOrEmpty(mrNodeId)? null : Long.valueOf(mrNodeId),
          scheduleTelId,
          mrId,
          crId,
          woId,
          nodeIp,
          nodeCode,
          nodeName,
          status,
          comments,
          StringUtils.validString(updateDate) ? DateTimeUtils.convertStringToDate(updateDate) : null
      );
      return model;
    }catch (Exception e){
      log.error(e.getMessage());
    }
    return null;
  }





}
