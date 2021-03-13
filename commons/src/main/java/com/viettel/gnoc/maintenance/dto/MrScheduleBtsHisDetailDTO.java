package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrScheduleBtsHisDetailEntity;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class MrScheduleBtsHisDetailDTO extends BaseDto {

  //Fields
  private String checkListId;
  private String content;
  private String deviceType;
  private String serial;
  private String photoReq;
  private String minPhoto;
  private String maxPhoto;
  private String cycle;
  private String captureGuide;
  private String woCode;
  private String taskStatus;
  private String scheduleBtsHisDetailId;
  private String contentEN;
  private String taskApprove;
  private String approveUser;
  private String approveDate;
  private String isHaveLstFile;
  private String woStatus;
  private String ftName;
  private List<MrScheduleBtsHisFileDTO> lstFile;
  private String woResult;
  private String woFinishTime;
  private String reason;
  private String imesErrorCode;
  private String imesMessage;
  private String woCodeOriginal;

  //TrungDuong them
  private String taskApproveArea;
  private String approveUserArea;
  private String approveDateArea;
  private String reasonArea;
  private String defaultSortField;
  private String scoreChecklist;
  private String isImportaint;

  //tiennv them
  private String mobile;
  private String mobileArea;

  //Constructor
  public MrScheduleBtsHisDetailDTO() {
    setDefaultSortField("name");
    //constructor
  }

  //Constructor
  public MrScheduleBtsHisDetailDTO(String checkListId, String content, String deviceType,
      String serial, String photoReq, String minPhoto, String maxPhoto, String cycle,
      String captureGuide, String woCode, String taskStatus, String imesErrorCode,
      String imesMessage, String woCodeOriginal, String scheduleBtsHisDetailId,
      String taskApprove, String approveUser, String approveDate, String reason,
      String taskApproveArea, String approveUserArea, String approveDateArea, String reasonArea) {
    this.scheduleBtsHisDetailId = scheduleBtsHisDetailId;
    this.checkListId = checkListId;
    this.content = content;
    this.deviceType = deviceType;
    this.serial = serial;
    this.photoReq = photoReq;
    this.minPhoto = minPhoto;
    this.maxPhoto = maxPhoto;
    this.cycle = cycle;
    this.captureGuide = captureGuide;
    this.woCode = woCode;
    this.taskStatus = taskStatus;
    this.imesErrorCode = imesErrorCode;
    this.imesMessage = imesMessage;
    this.woCodeOriginal = woCodeOriginal;
    this.taskApprove = taskApprove;
    this.approveUser = approveUser;
    this.approveDate = approveDate;
    this.reason = reason;
    this.taskApproveArea = taskApproveArea;
    this.approveUserArea = approveUserArea;
    this.approveDateArea = approveDateArea;
    this.reasonArea = reasonArea;
  }

  public MrScheduleBtsHisDetailEntity toEntity() {
    try {
      MrScheduleBtsHisDetailEntity model = new MrScheduleBtsHisDetailEntity(
          !StringUtils.validString(scheduleBtsHisDetailId) ? null
              : Long.valueOf(scheduleBtsHisDetailId),
          !StringUtils.validString(checkListId) ? null : Long.valueOf(checkListId),
          content,
          deviceType,
          serial,
          photoReq,
          minPhoto,
          maxPhoto,
          cycle,
          captureGuide,
          woCode,
          taskStatus,
          imesErrorCode,
          imesMessage,
          woCodeOriginal,
          !StringUtils.validString(taskApprove) ? null : Long.valueOf(taskApprove),
          approveUser,
          !StringUtils.validString(approveDate) ? null
              : DateTimeUtils.convertStringToDateTime(approveDate),
          reason,
          !StringUtils.validString(taskApproveArea) ? null : Long.valueOf(taskApproveArea),
          approveUserArea,
          !StringUtils.validString(approveDateArea) ? null
              : DateTimeUtils.convertStringToDateTime(approveDateArea),
          reasonArea,
          !StringUtils.validString(scoreChecklist) ? null
              : Double.valueOf(scoreChecklist),
          !StringUtils.validString(isImportaint) ? null
              : Long.valueOf(isImportaint)
      );
      return model;
    } catch (Exception ex) {
      log.error(ex.getMessage());
      return null;
    }
  }
}
