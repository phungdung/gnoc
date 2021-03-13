package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.maintenance.model.MrScheduleBtsHisDetailEntity;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MrScheduleBtsHisDetailInsiteDTO extends BaseDto {

  //Fields

  private Long scheduleBtsHisDetailId;
  private Long checkListId;
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
  private String imesErrorCode;
  private String imesMessage;
  private String woCodeOriginal;
  private Long taskApprove;
  private String approveUser;
  private Date approveDate;
  private String reason;

  private String contentEN;
  private String isHaveLstFile;
  private String woStatus;
  private String ftName;
  private List<MrScheduleBtsHisFileDTO> lstFile;
  private String woResult;
  private String woFinishTime;

  //tiennv them
  private Boolean isShowRed;
  private List<MrScheduleBtsHisDetailInsiteDTO> lstWorkInTab;
  private Boolean isApproveAble;

  //TrungDuong them
  private Long taskApproveArea;
  private String approveUserArea;
  private Date approveDateArea;
  private String reasonArea;
  private MrScheduleBtsHisDTO mrScheduleBtsHisDTO;
  private String deviceTypeStr;
  private String taskApproveStr;
  private String taskApproveAreaStr;
  private String woCodeStr;
  private Double scoreChecklist;
  private Long isImportaint;
  private Long valueAI;
  private Long photoRate;
  //Constructor
  public MrScheduleBtsHisDetailInsiteDTO(Long scheduleBtsHisDetailId, Long checkListId,
      String content, String deviceType, String serial, String photoReq, String minPhoto,
      String maxPhoto, String cycle, String captureGuide, String woCode, String taskStatus,
      String imesErrorCode, String imesMessage, String woCodeOriginal,
      Long taskApprove, String approveUser, Date approveDate, String reason,
      Long taskApproveArea, String approveUserArea, Date approveDateArea, String reasonArea, Double scoreChecklist, Long isImportaint) {
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
    this.scoreChecklist = scoreChecklist;
    this.isImportaint = isImportaint;
  }

  public MrScheduleBtsHisDetailEntity toEntity() {
    MrScheduleBtsHisDetailEntity model = new MrScheduleBtsHisDetailEntity(
        scheduleBtsHisDetailId,
        checkListId,
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
        taskApprove,
        approveUser,
        approveDate,
        reason,
        taskApproveArea,
        approveUserArea,
        approveDateArea,
        reasonArea,
        scoreChecklist,
        isImportaint
    );
    return model;
  }
}
