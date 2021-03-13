/**
 * @(#)CrForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ITSOL
 */

@Getter
@Setter
@Slf4j
@AllArgsConstructor
public class CrDTO {

  private String defaultSortField;
  private String crId;
  private String title;
  private String description;
  private String notes;
  private String crType;
  private String subcategory;
  private String priority;
  private String risk;
  private String state;
  private String processTypeId;
  private String country;
  private String region;
  private String circle;
  private String serviceAffecting;
  private String affectedService;
  private String relatedTt;
  private String relatedPt;
  private String relatedPtStepNumber;
  private String changeOrginator;
  private String changeOrginatorUnit;
  private String changeResponsible;
  private String changeResponsibleUnit;
  private String changeOrginatorName;
  private String changeOrginatorUnitName;
  private String changeResponsibleName;
  private String changeResponsibleUnitName;
  private String dutyType;
  private String earliestStartTime;
  private String latestStartTime;
  private String earliestStartTimeTo;
  private String latestStartTimeTo;
  private String manageUnitId;
  private String manageUserId;
  private String considerUnitName;
  private String considerUserName;
  private String activeWOControllSignal;
  private String disturbanceStartTime;
  private String disturbanceEndTime;
  private String relateToPrimaryCr;
  private String relateToPreApprovedCr;
  private String impactAffect;
  private String impactSegment;
  private String childImpactSegment;
  private String deviceType;
  private String createdDate;
  private String updateTime;
  private String crNumber;
  private String searchType;
  private String nodeCode;
  private String nodeIp;
  private String subDeptOri;
  private String subDeptResp;
  private String userLogin;
  private String userLoginUnit;
  private String scopeId;
  private String actionRight;
  private String crReturnCodeId;
  private String crReturnResolve;
  private String crReturnResolveTitle;
  private String actionType;
  private String actionNotes;
  private String actionReturnCodeId;
  private String considerUnitId;
  private String considerUserId;
  private String assignUserId;
  private String isPrimaryCr;
  private String relateToPrimaryCrNumber;
  private String relateToPreApprovedCrNumber;
  private String relateCr;
  private String crTypeCat;
  private String crActionCodeTittle;
  private String crProcessName;
  private String affectedServiceList;
  private String commentCAB;
  private String commentZ78;
  private String commentQLTD;
  private String commentCreator;
  private String isSearchChildDeptToApprove;
  private String locale;
  private String compareDate;
  private String onTimeAmount;
  private String allComment;
  private List<CrAffectedServiceDetailsDTO> lstAffectedService;
  private List<CrApprovalDepartmentDTO> lstAppDept;
  private List<CrImpactedNodesDTO> lstNetworkNodeId;
  private List<CrAffectedNodesDTO> lstNetworkNodeIdAffected;
  private CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO;
  private List<CrCreatedFromOtherSysDTO> lstCrCreatedFromOtherSysDTO;

  private Integer isClickedToAlarmTag = 0;
  private List<CrAlarmDTO> lstAlarn = new ArrayList<>();
  private Integer isClickedToModuleTag = 0;
  private List<CrModuleDetailDTO> lstModuleDetail = new ArrayList<>();
  private Integer isClickedToVendorTag = 0;
  private List<CrVendorDetailDTO> lstVendorDetail = new ArrayList<>();
  private Integer isClickedToCableTag = 0;
  List<CrCableDTO> lstCable = new ArrayList<>();

  private String sentDate;
  private String isClickedNode;
  private String isClickedNodeAffected;
  private String isOutOfDate;
  private String crProcessId;
  private String deviceTypeId;
  private String impactSegmentId;
  private String subcategoryId;
  private String userCab;
  private String autoExecute;

  //longlt6 add 2017-08-22 start
  private String resolveCodeId;
  private String resolveTitle;
  private String resolveReasonTitle;
  //longlt6 add 2017-08-22 end

  //longlt6 add 2017-08-22 start
  private String closeCodeId;
  private String closeTitle;
  private String closeReasonTitle;
  //longlt6 add 2017-08-22 end

  private String circleAdditionalInfo;
  private String closeCrAuto;
  private String resolveReturnCode;

  //longlt6 add 2017-11-28 end
  private String waitingMopStatus;
  private String vMSAValidateKey;
  private String totalAffectedCustomers;
  private String totalAffectedMinutes;

  private String isTracingCr;
  private String listWoId;

  private String createdDateFrom;
  private String createdDateTo;
  private List<Long> searchImpactedNodeIpIds;
  private String failDueToFT;
  private String nodeSavingMode;
  private String handoverCa;
  private String isHandoverCa;
  private String isLoadMop;
  private String responeTime;
  private String isConfirmAction;

  //tiennv bo sung truong dau viec
  private String processTypeLv3Id;

  private String rankGate;
  private String isRunType;
  private String changeDate; //Thời gian đóng CR
  private String kq_cr; //Kết quả CR
//  private Long isApproveCr;
//  private Long taDescription;
//  private String cbbCRProcess;
//  private String cbbDutyType;
//  private String cbbRelated;

  //Constructor
  public CrDTO() {
    setDefaultSortField("title");
  }

  public CrDTO(String crId, String title, String description, String notes, String crType,
      String subcategory, String priority, String risk,
      String state, String processTypeId, String country, String region, String circle,
      String serviceAffecting, String affectedService,
      String relatedTt, String relatedPt, String relatedPtStepNumber, String changeOrginator,
      String changeOrginatorUnit,
      String changeResponsible, String changeResponsibleUnit, String dutyType,
      String earliestStartTime, String latestStartTime,
      String disturbanceStartTime, String disturbanceEndTime, String isPrimaryCr,
      String relateToPrimaryCr, String relateToPreApprovedCr,
      String impactAffect, String impactSegment, String deviceType, String createdDate,
      String updateTime, String crNumber,
      String crReturnCodeId, String crTypeCat, String sentDate, String userCab, String autoExecute,
      String circleAdditionalInfo,
      String waitingMopStatus, String vMSAValidateKey, String totalAffectedCustomers,
      String totalAffectedMinutes, String handoverCa,
      String isHandoverCa, String isLoadMop, String responeTime, String childImpactSegment,
      String isConfirmAction, String processTypeLv3Id, String rankGate, String isRunType, String changeDate, String kq_cr) {
    this.crId = crId;
    this.title = title;
    this.description = description;
    this.notes = notes;
    this.crType = crType;
    this.subcategory = subcategory;
    this.priority = priority;
    this.risk = risk;
    this.state = state;
    this.processTypeId = processTypeId;
    this.country = country;
    this.region = region;
    this.circle = circle;
    this.serviceAffecting = serviceAffecting;
    this.affectedService = affectedService;
    this.relatedTt = relatedTt;
    this.relatedPt = relatedPt;
    this.relatedPtStepNumber = relatedPtStepNumber;
    this.changeOrginator = changeOrginator;
    this.changeOrginatorUnit = changeOrginatorUnit;
    this.changeResponsible = changeResponsible;
    this.changeResponsibleUnit = changeResponsibleUnit;
    this.dutyType = dutyType;
    this.earliestStartTime = earliestStartTime;
    this.latestStartTime = latestStartTime;
    this.disturbanceStartTime = disturbanceStartTime;
    this.disturbanceEndTime = disturbanceEndTime;
    this.isPrimaryCr = isPrimaryCr;
    this.relateToPrimaryCr = relateToPrimaryCr;
    this.relateToPreApprovedCr = relateToPreApprovedCr;
    this.impactAffect = impactAffect;
    this.impactSegment = impactSegment;
    this.deviceType = deviceType;
    this.createdDate = createdDate;
    this.updateTime = updateTime;
    this.crNumber = crNumber;
    this.crReturnCodeId = crReturnCodeId;
    this.crTypeCat = crTypeCat;
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
    this.isConfirmAction = isConfirmAction;
    this.processTypeLv3Id = processTypeLv3Id;
    this.rankGate = rankGate;
    this.isRunType = isRunType;
    this.changeDate = changeDate;
    this.kq_cr = kq_cr;
  }

  public CrInsiteDTO toModelInsiteDTO() {
    try {
      CrInsiteDTO crInsiteDTO = new CrInsiteDTO(
          null,
          this.crId,
          this.title,
          this.description,
          this.notes,
          this.crType,
          this.subcategory,
          this.priority,
          this.risk,
          this.state,
          this.processTypeId,
          this.country,
          this.region,
          this.circle,
          this.serviceAffecting,
          this.affectedService,
          this.relatedTt,
          this.relatedPt,
          this.relatedPtStepNumber,
          this.changeOrginator,
          this.changeOrginatorUnit,
          this.changeResponsible,
          this.changeResponsibleUnit,
          this.changeOrginatorName,
          this.changeOrginatorUnitName,
          this.changeResponsibleName,
          this.changeResponsibleUnitName,
          this.dutyType,
          StringUtils.isStringNullOrEmpty(this.earliestStartTime) ? null
              : DateTimeUtils.convertStringToDate1(this.earliestStartTime),
          StringUtils.isStringNullOrEmpty(this.latestStartTime) ? null
              : DateTimeUtils.convertStringToDate1(this.latestStartTime),
          this.earliestStartTimeTo,
          this.latestStartTimeTo,
          StringUtils.isStringNullOrEmpty(this.disturbanceStartTime) ? null
              : DateTimeUtils.convertStringToDate(disturbanceStartTime),
          StringUtils.isStringNullOrEmpty(this.disturbanceEndTime) ? null
              : DateTimeUtils.convertStringToDate(disturbanceEndTime),
          this.relateToPrimaryCr,
          this.relateToPreApprovedCr,
          this.impactAffect,
          this.impactSegment,
          this.deviceType,
          StringUtils.isStringNullOrEmpty(this.createdDate) ? null
              : DateTimeUtils.convertStringToDate(this.createdDate),
          StringUtils.isStringNullOrEmpty(this.updateTime) ? null
              : DateTimeUtils.convertStringToDate(this.updateTime),
          this.crNumber,
          this.searchType,
          this.nodeCode,
          this.nodeIp,
          this.subDeptOri,
          this.subDeptResp,
          this.userLogin,
          null,
          this.userLoginUnit,
          this.scopeId,
          this.actionRight,
          this.crReturnCodeId,
          this.crReturnResolve,
          this.crReturnResolveTitle,
          this.actionType,
          this.actionNotes,
          this.actionReturnCodeId,
          this.considerUnitId,
          this.considerUserId,
          this.considerUnitName,
          this.considerUserName,
          this.assignUserId,
          this.isPrimaryCr,
          this.relateToPrimaryCrNumber,
          this.relateToPreApprovedCrNumber,
          this.relateCr,
          this.crTypeCat,
          this.crActionCodeTittle,
          this.manageUnitId,
          this.manageUserId,
          this.crProcessName,
          this.affectedServiceList,
          this.commentCAB,
          this.commentZ78,
          this.commentQLTD,
          this.commentCreator,
          StringUtils.isStringNullOrEmpty(this.sentDate) ? null
              : DateTimeUtils.convertStringToDate(this.sentDate),
          this.isClickedNode,
          this.isClickedNodeAffected,
          this.isOutOfDate,
          this.isSearchChildDeptToApprove,
          this.locale,
          this.compareDate,
          this.onTimeAmount,
          this.allComment,
          this.crProcessId,
          this.deviceTypeId,
          this.impactSegmentId,
          this.subcategoryId,
          this.userCab,
          this.autoExecute,
          this.resolveTitle,
          null,
          this.circleAdditionalInfo,
          this.createdDateFrom,
          this.createdDateTo,
          this.waitingMopStatus,
          this.vMSAValidateKey,
          this.totalAffectedCustomers,
          this.totalAffectedMinutes,
          null,
          this.isClickedToAlarmTag,
          this.isClickedToModuleTag,
          this.isClickedToVendorTag,
          this.nodeSavingMode,
          this.resolveCodeId,
          this.resolveReasonTitle,
          this.closeCodeId,
          this.closeTitle,
          this.closeReasonTitle,
          this.closeCrAuto,
          this.resolveReturnCode,
          this.isTracingCr,
          this.listWoId,
          this.failDueToFT,
          this.searchImpactedNodeIpIds,
          this.activeWOControllSignal,
          this.handoverCa,
          this.isHandoverCa,
          null,
          this.isLoadMop,
          this.responeTime,
          this.childImpactSegment,
          this.isClickedToCableTag,
          null,
          null,
          this.crCreatedFromOtherSysDTO,
          toApprovalDepartmentInsiteDTO(this.lstAppDept),
          this.lstAffectedService,
          this.lstNetworkNodeId,
          this.lstNetworkNodeIdAffected,
          this.lstCrCreatedFromOtherSysDTO,
          toCrAlarmInsiteDTO(this.lstAlarn),
          this.lstVendorDetail,
          this.lstModuleDetail,
          this.lstCable,
          this.processTypeLv3Id,
          null,
          null,
          null,
          false,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          StringUtils.isStringNullOrEmpty(this.isConfirmAction) ? null
              : Long.valueOf(this.isConfirmAction),
          null,
          null,
          StringUtils.isStringNullOrEmpty(this.rankGate) ? null
              : Long.valueOf(this.rankGate),
          StringUtils.isStringNullOrEmpty(this.isRunType) ? null
              : Long.valueOf(this.isRunType)
      );
      return crInsiteDTO;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  public List<CrAlarmInsiteDTO> toCrAlarmInsiteDTO(List<CrAlarmDTO> crAlarmDTO) {
    if (crAlarmDTO != null) {
      List<CrAlarmInsiteDTO> crAlarmInsiteDTOS = new ArrayList<>();
      for (CrAlarmDTO dto : crAlarmDTO) {
        crAlarmInsiteDTOS.add(dto.toInsiteDTO());
      }
      return crAlarmInsiteDTOS;
    }
    return null;
  }

  public List<CrApprovalDepartmentInsiteDTO> toApprovalDepartmentInsiteDTO(
      List<CrApprovalDepartmentDTO> crApprovalDepartmentDTOS) {
    if (crApprovalDepartmentDTOS != null) {
      List<CrApprovalDepartmentInsiteDTO> lstCrApprovalDepartment = new ArrayList<>();
      for (CrApprovalDepartmentDTO dto : crApprovalDepartmentDTOS) {
        lstCrApprovalDepartment.add(dto.toInsiteDTO());
      }
      return lstCrApprovalDepartment;
    }
    return null;
  }
}
