package com.viettel.gnoc.cr.model;

import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "OPEN_PM", name = "CR")
@Getter
@Setter
@NoArgsConstructor
public class CrEntity {

  @Id
  @Column(name = "CR_ID", unique = true, nullable = false)
  private Long crId;

  @Column(name = "TITLE")
  private String title;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "NOTES")
  private String notes;

  @Column(name = "CR_TYPE")
  private Long crType;

  @Column(name = "SUBCATEGORY")
  private Long subcategory;

  @Column(name = "PRIORITY")
  private Long priority;

  @Column(name = "RISK")
  private Long risk;

  @Column(name = "STATE")
  private Long state;

  @Column(name = "PROCESS_TYPE_ID")
  private Long processTypeId;

  @Column(name = "COUNTRY")
  private Long country;

  @Column(name = "REGION")
  private Long region;

  @Column(name = "CIRCLE")
  private Long circle;

  @Column(name = "SERVICE_AFFECTING")
  private Long serviceAffecting;

  @Column(name = "AFFECTED_SERVICE")
  private Long affectedService;

  @Column(name = "RELATED_TT")
  private Long relatedTt;

  @Column(name = "RELATED_PT")
  private Long relatedPt;

  @Column(name = "RELATED_PT_STEP_NUMBER")
  private Long relatedPtStepNumber;

  @Column(name = "CHANGE_ORGINATOR")
  private Long changeOrginator;

  @Column(name = "CHANGE_ORGINATOR_UNIT")
  private Long changeOrginatorUnit;

  @Column(name = "CHANGE_RESPONSIBLE")
  private Long changeResponsible;

  @Column(name = "CHANGE_RESPONSIBLE_UNIT")
  private Long changeResponsibleUnit;

  @Column(name = "DUTY_TYPE")
  private Long dutyType;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "EARLIEST_START_TIME")
  private Date earliestStartTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "LATEST_START_TIME")
  private Date latestStartTime;


  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "DISTURBANCE_START_TIME")
  private Date disturbanceStartTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "DISTURBANCE_END_TIME")
  private Date disturbanceEndTime;

  @Column(name = "IS_PRIMARY_CR")
  private Long isPrimaryCr;

  @Column(name = "RELATE_TO_PRIMARY_CR")
  private Long relateToPrimaryCr;

  @Column(name = "RELATE_TO_PRE_APPROVED_CR")
  private Long relateToPreApprovedCr;

  @Column(name = "IMPACT_AFFECT")
  private Long impactAffect;

  @Column(name = "IMPACT_SEGMENT")
  private Long impactSegment;

  @Column(name = "CHILD_IMPACT_SEGMENT")
  private Long childImpactSegment;

  @Column(name = "DEVICE_TYPE")
  private Long deviceType;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "UPDATE_TIME")
  private Date updateTime;

  @Column(name = "CR_NUMBER")
  private String crNumber;

  @Column(name = "CR_RETURN_CODE_ID")
  private Long crReturnCodeId;

  @Column(name = "CR_TYPE_CAT")
  private Long crTypeCat;

  @Column(name = "USER_CAB")
  private Long userCab;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "SENT_DATE")
  private Date sentDate;

  @Column(name = "AUTO_EXECUTE")
  private Long autoExecute;

  //longlt6 add 2017-09-05
  @Column(name = "CIRCLE_ADDITIONAL_INFO")
  private String circleAdditionalInfo;

  @Column(name = "RESOLVE_RESTURN_CODE")
  private Long resolveReturnCode;

  //longlt6 add 2017-28-11
  @Column(name = "WAITING_MOP_STATUS")
  private Long waitingMopStatus = 0L;

  @Column(name = "VMSA_VALIDATE_KEY")
  private Long vMSAValidateKey;

  @Column(name = "TOTAL_AFFECTED_CUSTOMERS")
  private Long totalAffectedCustomers;

  @Column(name = "TOTAL_AFFECTED_MINUTES")
  private Long totalAffectedMinutes;

  @Column(name = "IS_TRACING_CR")
  private Long isTracingCr;

  @Column(name = "LIST_WO_ID")
  private String listWoId;

  @Column(name = "FAIL_DUE_TO_FT")
  private Long failDueToFT;

  @Column(name = "NODE_SAVING_MODE")
  private Long nodeSavingMode;

  @Column(name = "handover_ca")
  private Long handoverCa;

  @Column(name = "is_handover_ca")
  private Long isHandoverCa;

  @Column(name = "is_load_mop")
  private Long isLoadMop;

  @Column(name = "RESPONE_TIME")
  private String responeTime;

  //tiennv bo sung truong dau viec
  @Column(name = "PROCESS_TYPE_LV3_ID")
  private String processTypeLv3Id;

  //bo sung truong isconfirmAction
  @Column(name = "IS_CONFIRM_ACTION")
  private Long isConfirmAction;

  //bo sung truong trong DB
  @Column(name = "ORIGINAL_EARLIEST_START_TIME")
  @Temporal(TemporalType.TIMESTAMP)
  private Date originalEarliestStartTime;

  //bo sung truong trong DB
  @Column(name = "ORIGINAL_LATEST_START_TIME")
  @Temporal(TemporalType.TIMESTAMP)
  private Date originalLatestStartTime;

  @Column(name = "MANAGE_UNIT_ID")
  private Long manageUnitId;

  @Column(name = "MANAGE_USER_ID")
  private Long manageUserId;

  @Column(name = "CONSIDER_UNIT_ID")
  private Long considerUnitId;

  @Column(name = "CONSIDER_USER_ID")
  private Long considerUserId;

  @Column(name = "RANK_GATE")
  private Long rankGate;

  @Column(name = "IS_RUN_TYPE")
  private Long isRunType;

  public CrEntity(
      Long crId1, String title, String description1, String notes, Long crType, Long subcategory1,
      Long priority, Long risk,
      Long state1, Long processTypeId, Long country1, Long region, Long circle,
      Long serviceAffecting1, Long affectedService,
      Long relatedTt, Long relatedPt1, Long relatedPtStepNumber, Long changeOrginator,
      Long changeOrginatorUnit1, Long changeResponsible,
      Long changeResponsibleUnit, Long dutyType1, Date earliestStartTime, Date latestStartTime,
      Date disturbanceStartTime1,
      Date disturbanceEndTime, Long isPrimaryCr, Long relateToPrimaryCr1,
      Long relateToPreApprovedCr, Long impactAffect1,
      Long impactSegment, Long deviceType, Date createdDate1, Date updateTime, String crNumber,
      Long crReturnCodeId, Long crTypeCat1,
      Date sentDate, Long userCab, Long autoExecute, String circleAdditionalInfo,
      Long waitingMopStatus, Long vMSAValidateKey,
      Long totalAffectedCustomers, Long totalAffectedMinutes, Long handoverCa, Long isHandoverCa,
      Long isLoadMop, String responeTime,
      Long childImpactSegment, String processTypeLv3Id, Long isConfirmAction,
      Date originalEarliestStartTime, Date originalLatestStartTime, Long rankGate, Long isRunType) {
    this.crId = crId1;
    this.title = title;
    this.description = description1;
    this.notes = notes;
    this.crType = crType;
    this.subcategory = subcategory1;
    this.priority = priority;
    this.risk = risk;
    this.state = state1;
    this.processTypeId = processTypeId;
    this.country = country1;
    this.region = region;
    this.circle = circle;
    this.serviceAffecting = serviceAffecting1;
    this.affectedService = affectedService;
    this.relatedTt = relatedTt;
    this.relatedPt = relatedPt1;
    this.relatedPtStepNumber = relatedPtStepNumber;
    this.changeOrginator = changeOrginator;
    this.changeOrginatorUnit = changeOrginatorUnit1;
    this.changeResponsible = changeResponsible;
    this.changeResponsibleUnit = changeResponsibleUnit;
    this.dutyType = dutyType1;
    this.earliestStartTime = earliestStartTime;
    this.latestStartTime = latestStartTime;
    this.disturbanceStartTime = disturbanceStartTime1;
    this.disturbanceEndTime = disturbanceEndTime;
    this.isPrimaryCr = isPrimaryCr;
    this.relateToPrimaryCr = relateToPrimaryCr1;
    this.relateToPreApprovedCr = relateToPreApprovedCr;
    this.impactAffect = impactAffect1;
    this.impactSegment = impactSegment;
    this.deviceType = deviceType;
    this.createdDate = createdDate1;
    this.updateTime = updateTime;
    this.crNumber = crNumber;
    this.crReturnCodeId = crReturnCodeId;
    this.crTypeCat = crTypeCat1;
    this.sentDate = sentDate;
    this.userCab = userCab;
    this.autoExecute = autoExecute;
    this.circleAdditionalInfo = circleAdditionalInfo;
    this.waitingMopStatus = waitingMopStatus;
    this.vMSAValidateKey = vMSAValidateKey;
    this.totalAffectedCustomers = totalAffectedCustomers;
    this.totalAffectedMinutes = totalAffectedMinutes;
    this.handoverCa = handoverCa;
    this.isHandoverCa = isHandoverCa;
    this.isLoadMop = isLoadMop;
    this.responeTime = responeTime;
    this.childImpactSegment = childImpactSegment;
    this.processTypeLv3Id = processTypeLv3Id;
    this.isConfirmAction = isConfirmAction;
    this.originalEarliestStartTime = originalEarliestStartTime;
    this.originalLatestStartTime = originalLatestStartTime;
    this.rankGate = rankGate;
    this.isRunType = isRunType;
  }

  public CrInsiteDTO toDTO() {
    CrInsiteDTO dto = new CrInsiteDTO(this.crId == null ? null : this.crId.toString(),
        this.title, this.description, this.notes,
        this.crType == null ? null : this.crType.toString(),
        this.subcategory == null ? null : this.subcategory.toString(),
        this.priority == null ? null : this.priority.toString(),
        this.risk == null ? null : this.risk.toString(),
        this.state == null ? null : this.state.toString(),
        this.processTypeId == null ? null : this.processTypeId.toString(),
        this.country == null ? null : this.country.toString(),
        this.region == null ? null : this.region.toString(),
        this.circle == null ? null : this.circle.toString(),
        this.serviceAffecting == null ? null : this.serviceAffecting.toString(),
        this.affectedService == null ? null : this.affectedService.toString(),
        this.relatedTt == null ? null : this.relatedTt.toString(),
        this.relatedPt == null ? null : this.relatedPt.toString(),
        this.relatedPtStepNumber == null ? null : this.relatedPtStepNumber.toString(),
        this.changeOrginator == null ? null : this.changeOrginator.toString(),
        this.changeOrginatorUnit == null ? null : this.changeOrginatorUnit.toString(),
        this.changeResponsible == null ? null : this.changeResponsible.toString(),
        this.changeResponsibleUnit == null ? null : this.changeResponsibleUnit.toString(),
        this.dutyType == null ? null : this.dutyType.toString(),
        this.earliestStartTime,
        this.latestStartTime,
        this.disturbanceStartTime,
        this.disturbanceEndTime,
        this.isPrimaryCr == null ? null : this.isPrimaryCr.toString(),
        this.relateToPrimaryCr == null ? null : this.relateToPrimaryCr.toString(),
        this.relateToPreApprovedCr == null ? null : this.relateToPreApprovedCr.toString(),
        this.impactAffect == null ? null : this.impactAffect.toString(),
        this.impactSegment == null ? null : this.impactSegment.toString(),
        this.deviceType == null ? null : this.deviceType.toString(),
        this.createdDate,
        this.updateTime,
        this.crNumber, this.crReturnCodeId == null ? null : this.crReturnCodeId.toString(),
        this.crTypeCat == null ? null : this.crTypeCat.toString(),
        this.sentDate,
        this.userCab == null ? null : this.userCab.toString(),
        this.autoExecute == null ? null : this.autoExecute.toString(),
        this.circleAdditionalInfo,
        this.waitingMopStatus == null ? null : this.waitingMopStatus.toString(),
        this.vMSAValidateKey == null ? null : this.vMSAValidateKey.toString(),
        this.totalAffectedCustomers == null ? null : this.totalAffectedCustomers.toString(),
        this.totalAffectedMinutes == null ? null : this.totalAffectedMinutes.toString(),
        this.handoverCa == null ? null : this.handoverCa.toString(),
        this.isHandoverCa == null ? null : this.isHandoverCa.toString(),
        this.isLoadMop == null ? null : this.isLoadMop.toString(),
        responeTime, this.childImpactSegment == null ? null : this.childImpactSegment.toString(),
        this.processTypeLv3Id,
        this.isConfirmAction,
        this.originalEarliestStartTime,
        this.originalLatestStartTime,
        this.rankGate,
        this.isRunType,
        this.considerUnitId == null ? null : this.considerUnitId.toString(),
        this.manageUnitId == null ? null : this.manageUnitId.toString()
    );
    if (this.isTracingCr != null) {
      dto.setIsTracingCr(String.valueOf(this.isTracingCr));
    }
    dto.setListWoId(this.listWoId);
    if (this.failDueToFT != null) {
      dto.setFailDueToFT(String.valueOf(this.failDueToFT));
    }
    if (this.nodeSavingMode != null) {
      dto.setNodeSavingMode(String.valueOf(this.nodeSavingMode));
    }

    return dto;
  }
}
