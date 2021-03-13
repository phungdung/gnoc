package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_SCHEDULE_BTS_HIS_DETAIL")
public class MrScheduleBtsHisDetailEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_SCHEDULE_HIS_BTS_DETAIL_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "SCHEDULE_HIS_DETAIL_ID", unique = true, nullable = false)
  private Long scheduleBtsHisDetailId;

  @Column(name = "CHECKLIST_ID", nullable = false)
  private Long checkListId;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "SERIAL")
  private String serial;

  @Column(name = "PHOTO_REQ")
  private String photoReq;

  @Column(name = "MIN_PHOTO")
  private String minPhoto;

  @Column(name = "MAX_PHOTO")
  private String maxPhoto;

  @Column(name = "CYCLE")
  private String cycle;

  @Column(name = "CAPTURE_GUIDE")
  private String captureGuide;

  @Column(name = "WO_CODE")
  private String woCode;

  @Column(name = "TASK_STATUS")
  private String taskStatus;

  @Column(name = "IMES_ERROR_CODE")
  private String imesErrorCode;

  @Column(name = "IMES_MESSAGE")
  private String imesMessage;

  @Column(name = "WO_CODE_ORIGINAL")
  private String woCodeOriginal;

  @Column(name = "TASK_APPROVE")
  private Long taskApprove;

  @Column(name = "APPROVE_USER")
  private String approveUser;

  @Column(name = "APPROVE_DATE")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date approveDate;

  @Column(name = "REASON")
  private String reason;

  @Column(name = "TASK_APPROVE_AREA")
  private Long taskApproveArea;

  @Column(name = "APPROVE_USER_AREA")
  private String approveUserArea;

  @Column(name = "APPROVE_DATE_AREA")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date approveDateArea;

  @Column(name = "REASON_AREA")
  private String reasonArea;

  @Column(name = "SCORE_CHECKLIST")
  private Double scoreChecklist;

  @Column(name = "IS_IMPORTAINT")
  private Long isImportaint;

  public MrScheduleBtsHisDetailInsiteDTO toDTO() {
    MrScheduleBtsHisDetailInsiteDTO dto = new MrScheduleBtsHisDetailInsiteDTO(
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
    return dto;
  }
}
