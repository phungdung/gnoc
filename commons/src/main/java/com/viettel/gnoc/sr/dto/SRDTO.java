/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author VTN-PTPM-NV04
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class SRDTO {

  private String srId;
  private String srCode;
  private String country;
  private String title;
  private String description;
  private String serviceArray;
  private String serviceGroup;
  private String serviceId;
  private String startTime;
  private String endTime;
  private String status;
  private String createdUser;
  private String createdTime;
  private String updatedUser;
  private String updatedTime;
  private String srUnit;
  private String srUser;
  private String sendDate;
  private String reviewId;
  private String checkingUnit;
  private String createFromDate;
  private String createToDate;
  private String serviceName;
  private String defaultSortField = "srId";
  private String unitName;
  private String statusName;

  private String userId;
  private String createdUnit;
  private String isOpenConnect;

  private String serviceArrayName;
  private String serviceGroupName;

  //namtn edit on may 2018
  private String executionTime;
  private String crNumber;
  private String remainExecutionTime;
  private String evaluate;
  private String subOrderId;
  private String serviceCode;
  private String loginUser;
  private String key;
  private String message;

  private String isCloseAble;
  private String isUpdateAble;

  private String evaluateReplyTime;
  private String slaReceiveTime;
  private String pathSrUnit;
  private String replyTime;
  private String actualExecutionTime;

  //namtn edit for process
  private String isSendMessage;
  private String smsContent;
  private String woCode;
  private String mobile;
  private String smsGateWayId;

  //namtn add tab split SR
  private String parentCode;
  private String roleCode;

  private String searchParentUnit;

  private int offset;

  private String state;
  private String actionCode;
  private String returnCode;
  private String stepId;

  private String username;

  private String flowExecute;
  private String flowExecuteName;
  private String evaluateReason;
  private String executionUnit;
  private String fileContent;
  private String insertSource;

  private List<SRParamDTO> lstParam;
  private List<SRFilesDTO> lstFile;
  private String dayRenew;
  private String crWoCreatTime;
  private String statusRenew;
  //dungpv add autoCreateCR
  List<CrFilesAttachInsiteDTO> lstUpload;
  UnitDTO unitToken;
  private String creatCRWO;
  private String wlText;
  private String workLog;
  private String locale;
  //thangdt nang cap pt
  private String otherSystemCode;
  private String countNok;
  private Long isForceClosed;
  private String stationCode;
  private String reCreateSR;
  private String note;
  private String timeSave;

  public SRDTO(String srId, String srCode, String country, String title, String description,
      String serviceArray, String serviceGroup, String serviceId, String startTime, String endTime,
      String status, String createdUser, String createdTime, String updatedUser, String updatedTime,
      String srUnit, String srUser, String sendDate, String reviewId, String roleCode,
      String parentCode, String insertSource, String otherSystemCode, String note) {
    this.srId = srId;
    this.srCode = srCode;
    this.country = country;
    this.title = title;
    this.description = description;
    this.serviceArray = serviceArray;
    this.serviceGroup = serviceGroup;
    this.serviceId = serviceId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.status = status;
    this.createdUser = createdUser;
    this.createdTime = createdTime;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
    this.srUnit = srUnit;
    this.srUser = srUser;
    this.sendDate = sendDate;
    this.reviewId = reviewId;
    this.roleCode = roleCode;
    this.parentCode = parentCode;
    this.insertSource = insertSource;
    this.otherSystemCode = otherSystemCode;
    this.note = note;
  }

  public SRDTO(String key, String message) {
    this.key = key;
    this.message = message;
    setDefaultSortField("name");
  }

  public SrInsiteDTO toInsideDTO() {
    try {
      return new SrInsiteDTO(
          srId == null ? null : Long.valueOf(srId)
          , srCode, country, title, description
          , serviceArray, serviceGroup, serviceId
          , StringUtils.isStringNullOrEmpty(startTime) ? null
          : DateTimeUtils.convertStringToDateTime(startTime)
          , StringUtils.isStringNullOrEmpty(endTime) ? null
          : DateTimeUtils.convertStringToDateTime(endTime)
          , status, createdUser
          , StringUtils.isStringNullOrEmpty(createdTime) ? null
          : DateTimeUtils.convertStringToDateTime(createdTime)
          , updatedUser
          , StringUtils.isStringNullOrEmpty(updatedTime) ? null
          : DateTimeUtils.convertStringToDateTime(updatedTime)
          , srUnit == null ? null : Long.valueOf(srUnit), srUser
          , StringUtils.isStringNullOrEmpty(sendDate) ? null
          : DateTimeUtils.convertStringToDateTime(sendDate)
          , reviewId == null ? null : Long.valueOf(reviewId)
          , parentCode, roleCode, insertSource, otherSystemCode,
          StringUtils.isNotNullOrEmpty(countNok) ? Long.parseLong(countNok) : null, isForceClosed,
          crNumber, note, StringUtils.isStringNullOrEmpty(timeSave) ? null
          : DateTimeUtils.convertStringToDateTime(timeSave),
          (StringUtils.isNotNullOrEmpty(isOpenConnect) && "1".equals(isOpenConnect)) ? true : false
      );
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return null;
    }
  }
}
