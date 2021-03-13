/**
 * @(#)CrForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.model.CrEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class CrInsiteDTO extends BaseDto {

  private String defaultSortField;
  private String crId;
  private String title;
  private String description;
  private String notes;
  private String crType;//0-Thuong, 1-Khan, 2-chuan
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
  private Date earliestStartTime;
  private Date latestStartTime;
  private String earliestStartTimeTo;
  private String latestStartTimeTo;
  private Date disturbanceStartTime;
  private Date disturbanceEndTime;
  private String relateToPrimaryCr;
  private String relateToPreApprovedCr;
  private String impactAffect;
  private String impactSegment;
  private String deviceType;
  private Date createdDate;
  private Date updateTime;
  private String crNumber;
  private String searchType;
  private String nodeCode;
  private String nodeIp;
  private String subDeptOri;
  private String subDeptResp;
  private String userLogin;
  private String button;
  private String userLoginUnit;
  private String scopeId;
  private String actionRight;
  private String crReturnCodeId;//Return code - ActionCode tren giao dien
  private String crReturnResolve;//Return code khi Resolve CR
  private String crReturnResolveTitle;
  private String actionType; //loại hành động: duyệt/phê duyệt. Ăn theo Constant.CR_ACTION_CODE
  private String actionNotes; //là comment mà người dùng gắn vào
  private String actionReturnCodeId; // là Id của actionCode trả ra
  private String considerUnitId;//Đơn vị thẩm định được giao
  private String considerUserId;//nhân viên thẩm định được giao
  private String considerUnitName;
  private String considerUserName;
  private String assignUserId; //là user được chọn để giao thẩm định
  private String isPrimaryCr;//Danh dau la CR Primary
  private String relateToPrimaryCrNumber;//Lien ket den CR Primary
  private String relateToPreApprovedCrNumber;//Lien ket den CR Pre-Approve
  private String relateCr;
  private String crTypeCat;//Danh dau la CR loai khac | VD: Pre-Approve
  private String crActionCodeTittle;
  private String manageUnitId;
  private String manageUserId;
  private String crProcessName;
  private String affectedServiceList;
  private String commentCAB;
  private String commentZ78;
  private String commentQLTD;
  private String commentCreator;

  private Date sentDate;
  private String isClickedNode;
  private String isClickedNodeAffected;
  private String isOutOfDate;
  //20160530 daitt1 bo sung checkbox tim kiem duyet theo don vi con
  private String isSearchChildDeptToApprove;
  //20160530 daitt1 bo sung checkbox tim kiem duyet theo don vi con
  //20160622 daitr1 bo sung cot thoi gian qua han
  private String locale;
  private String compareDate;
  private String onTimeAmount;
  private String allComment;
  //20160622 daitr1 bo sung cot thoi gian qua han
  //tuanpv14_multilanguage
  private String crProcessId;
  private String deviceTypeId;
  private String impactSegmentId;
  private String subcategoryId;
  //tuanpv14_multilanguage
  //tuanpv14_cab
  private String userCab;
  //tuanpv14_cab
  private String autoExecute;
  private String resolveTitle;
  private String reasonTitle;
  private String circleAdditionalInfo;
  private String createdDateFrom;
  private String createdDateTo;
  private String waitingMopStatus;
  private String vMSAValidateKey;
  private String totalAffectedCustomers;
  private String totalAffectedMinutes;
  private String status;

  //tiennv add some field
  private Integer isClickedToAlarmTag = 0;
  private Integer isClickedToModuleTag = 0;
  private Integer isClickedToVendorTag = 0;
  private String nodeSavingMode;


  private String resolveCodeId;
  private String resolveReasonTitle;
  //longlt6 add 2017-08-22 end

  //longlt6 add 2017-08-22 start
  private String closeCodeId;
  private String closeTitle;
  private String closeReasonTitle;


  private String closeCrAuto;
  private String resolveReturnCode;

  private String isTracingCr;
  private String listWoId;
  private String failDueToFT;
  private List<Long> searchImpactedNodeIpIds;
  private String activeWOControllSignal;
  private String handoverCa;
  private String isHandoverCa;
  private String checkbox;
  private String isLoadMop;
  private String responeTime;
  private String childImpactSegment;
  private Integer isClickedToCableTag = 0;

  private List<List<Long>> searchCrIds;
  private List<AttachDtDTO> attachDtDTO;

  private CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO;
  private List<CrApprovalDepartmentInsiteDTO> lstAppDept;
  private List<CrAffectedServiceDetailsDTO> lstAffectedService;
  private List<CrImpactedNodesDTO> lstNetworkNodeId;
  private List<CrAffectedNodesDTO> lstNetworkNodeIdAffected;
  private List<CrCreatedFromOtherSysDTO> lstCrCreatedFromOtherSysDTO;
  private List<CrAlarmInsiteDTO> lstAlarn = new ArrayList<>();
  private List<CrVendorDetailDTO> lstVendorDetail = new ArrayList<>();
  private List<CrModuleDetailDTO> lstModuleDetail = new ArrayList<>();
  private List<CrCableDTO> lstCable = new ArrayList<>();

  //tiennv bo sung truong dau viec
  private String processTypeLv3Id;
  //bo sung truong action truyen tu client khi goi update
  private String action;
  private List<String> failedWoList;
  private List<ItemDataCRInside> lstReturnCodeAll;
  //bo sung checkCRAuto va subDTCode action SchedulerCr
  private boolean checkCrAuto;
  private String subDTCode;

  private Long systemId;
  private Long objectId;
  private Long stepId;
  //bo sung danh sach userCab
  private List<ItemDataCRDTO> lstUserCab;
  //bo sung check CrAction
  private Boolean isCheckAction;
  private String crRelatedCbb; //bo sung crRelated validate phia client
  private List<CrCableDTO> lstLaneCable = new ArrayList<>(); //bo sung laneCable validate insert
  //bo sung de them ban giao ca cong viec
  private String workLog;
  //bo sung truong isconfirmAction
  private Long isConfirmAction;
  private Date originalEarliestStartTime;
  private Date originalLatestStartTime;
  //duongnt
  private String fileId;
  private String fileType;
  private String crNumberRelatedCbb;
  private Boolean createWoAfterClose;
  private Long otherSystemId;
  private String otherSystemType;

  private Long rankGate;
  private Long isRunType;
  private Date changeDate; //Thời gian đóng CR
  private String kq_cr; //Kết quả CR

//  dungpv 16/09/2020 add them input ws getCRbyImpactIP
  private Date impactedTimeFrom;
  private Date impactedTimeTo;
  private List<String> listImpactedNode;
//  end

  public CrInsiteDTO(String crId, String title, String description, String notes, String crType,
      String subcategory, String priority, String risk,
      String state, String processTypeId, String country, String region, String circle,
      String serviceAffecting, String affectedService,
      String relatedTt, String relatedPt, String relatedPtStepNumber, String changeOrginator,
      String changeOrginatorUnit,
      String changeResponsible, String changeResponsibleUnit, String dutyType,
      Date earliestStartTime, Date latestStartTime,
      Date disturbanceStartTime, Date disturbanceEndTime, String isPrimaryCr,
      String relateToPrimaryCr, String relateToPreApprovedCr,
      String impactAffect, String impactSegment, String deviceType, Date createdDate,
      Date updateTime, String crNumber,
      String crReturnCodeId, String crTypeCat, Date sentDate, String userCab, String autoExecute,
      String circleAdditionalInfo,
      String waitingMopStatus, String vMSAValidateKey, String totalAffectedCustomers,
      String totalAffectedMinutes, String handoverCa,
      String isHandoverCa, String isLoadMop, String responeTime, String childImpactSegment,
      String processTypeLv3Id, Long isConfirmAction,
      Date originalEarliestStartTime, Date originalLatestStartTime, Long rankGate, Long isRunType,
      String considerUnitId, String manageUnitId) {
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
    this.processTypeLv3Id = processTypeLv3Id;
    this.isConfirmAction = isConfirmAction;
    this.originalEarliestStartTime = originalEarliestStartTime;
    this.originalLatestStartTime = originalLatestStartTime;
    this.rankGate = rankGate;
    this.isRunType = isRunType;
    this.considerUnitId = considerUnitId;
    this.manageUnitId = manageUnitId;
  }

  public CrEntity toEntity() {
    CrEntity cr = new CrEntity(
        !StringUtils.validString(this.crId) ? null : Long.valueOf(this.crId), this.title,
        this.description, this.notes,
        !StringUtils.validString(this.crType) ? null : Long.valueOf(this.crType),
        !StringUtils.validString(this.subcategory) ? null : Long.valueOf(this.subcategory),
        !StringUtils.validString(this.priority) ? null : Long.valueOf(this.priority),
        !StringUtils.validString(this.risk) ? null : Long.valueOf(this.risk),
        !StringUtils.validString(this.state) ? null : Long.valueOf(this.state),
        !StringUtils.validString(this.processTypeId) ? null : Long.valueOf(this.processTypeId),
        !StringUtils.validString(this.country) ? null : Long.valueOf(this.country),
        !StringUtils.validString(this.region) ? null : Long.valueOf(this.region),
        !StringUtils.validString(this.circle) ? null : Long.valueOf(this.circle),
        !StringUtils.validString(this.serviceAffecting) ? null
            : Long.valueOf(this.serviceAffecting),
        !StringUtils.validString(this.affectedService) ? null
            : Long.valueOf(this.affectedService),
        !StringUtils.validString(this.relatedTt) ? null : Long.valueOf(this.relatedTt),
        !StringUtils.validString(this.relatedPt) ? null : Long.valueOf(this.relatedPt),
        !StringUtils.validString(this.relatedPtStepNumber) ? null
            : Long.valueOf(this.relatedPtStepNumber),
        !StringUtils.validString(this.changeOrginator) ? null
            : Long.valueOf(this.changeOrginator),
        !StringUtils.validString(this.changeOrginatorUnit) ? null
            : Long.valueOf(this.changeOrginatorUnit),
        !StringUtils.validString(this.changeResponsible) ? null
            : Long.valueOf(this.changeResponsible),
        !StringUtils.validString(this.changeResponsibleUnit) ? null
            : Long.valueOf(this.changeResponsibleUnit),
        !StringUtils.validString(this.dutyType) ? null : Long.valueOf(this.dutyType),
        this.earliestStartTime,
        this.latestStartTime,
        this.disturbanceStartTime,
        this.disturbanceEndTime,
        !StringUtils.validString(this.isPrimaryCr) ? null : Long.valueOf(this.isPrimaryCr),
        !StringUtils.validString(this.relateToPrimaryCr) ? null
            : Long.valueOf(this.relateToPrimaryCr),
        !StringUtils.validString(this.relateToPreApprovedCr) ? null
            : Long.valueOf(this.relateToPreApprovedCr),
        !StringUtils.validString(this.impactAffect) ? null : Long.valueOf(this.impactAffect),
        !StringUtils.validString(this.impactSegment) ? null : Long.valueOf(this.impactSegment),
        !StringUtils.validString(this.deviceType) ? null : Long.valueOf(this.deviceType),
        this.createdDate,
        this.updateTime,
        this.crNumber,
        !StringUtils.validString(this.crReturnCodeId) ? null : Long.valueOf(this.crReturnCodeId),
        !StringUtils.validString(this.crTypeCat) ? null : Long.valueOf(this.crTypeCat),
        this.sentDate,
        !StringUtils.validString(this.userCab) ? null : Long.valueOf(this.userCab),
        !StringUtils.validString(this.autoExecute) ? null : Long.valueOf(this.autoExecute),
        this.circleAdditionalInfo,
        !StringUtils.validString(this.waitingMopStatus) ? null
            : Long.valueOf(this.waitingMopStatus),
        !StringUtils.validString(this.vMSAValidateKey) ? null
            : Long.valueOf(this.vMSAValidateKey),
        !StringUtils.validString(this.totalAffectedCustomers) ? null
            : Long.valueOf(this.totalAffectedCustomers),
        !StringUtils.validString(this.totalAffectedMinutes) ? null
            : Long.valueOf(this.totalAffectedMinutes),
        !StringUtils.validString(this.handoverCa) ? null : Long.valueOf(this.handoverCa),
        !StringUtils.validString(this.isHandoverCa) ? null : Long.valueOf(this.isHandoverCa),
        !StringUtils.validString(this.isLoadMop) ? null : Long.valueOf(this.isLoadMop),
        responeTime,
        !StringUtils.validString(this.childImpactSegment) ? null
            : Long.valueOf(this.childImpactSegment),
        this.processTypeLv3Id,
        this.isConfirmAction,
        this.originalEarliestStartTime,
        this.originalLatestStartTime,
        this.rankGate,
        this.isRunType
    );
    if (StringUtils.validString(this.resolveReturnCode)) {
      cr.setResolveReturnCode(Long.valueOf(this.resolveReturnCode));
    }
    if (StringUtils.validString(this.isTracingCr)) {
      cr.setIsTracingCr(Long.valueOf(this.isTracingCr));
    }
    cr.setListWoId(this.listWoId);

    if (StringUtils.validString(this.failDueToFT)) {
      cr.setFailDueToFT(Long.valueOf(this.failDueToFT));
    }
    if (StringUtils.validString(this.nodeSavingMode)) {
      cr.setNodeSavingMode(Long.valueOf(this.nodeSavingMode));
    }

    return cr;
  }

  public CrDTO toCrDTO() {
    CrDTO dto = new CrDTO("title",
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
        StringUtils.isStringNullOrEmpty(earliestStartTime) ? null
            : DateTimeUtils.date2ddMMyyyyHHMMss(earliestStartTime),
        StringUtils.isStringNullOrEmpty(latestStartTime) ? null
            : DateTimeUtils.date2ddMMyyyyHHMMss(latestStartTime),
        this.earliestStartTimeTo,
        this.latestStartTimeTo,
        this.manageUnitId,
        this.manageUserId,
        this.considerUnitName,
        this.considerUserName,
        this.activeWOControllSignal,
        StringUtils.isStringNullOrEmpty(disturbanceStartTime) ? null
            : DateTimeUtils.date2ddMMyyyyHHMMss(disturbanceStartTime),
        StringUtils.isStringNullOrEmpty(disturbanceEndTime) ? null
            : DateTimeUtils.date2ddMMyyyyHHMMss(disturbanceEndTime),
        this.relateToPrimaryCr,
        this.relateToPreApprovedCr,
        this.impactAffect,
        this.impactSegment,
        this.childImpactSegment,
        this.deviceType,
        StringUtils.isStringNullOrEmpty(createdDate) ? null
            : DateTimeUtils.date2ddMMyyyyHHMMss(createdDate),
        StringUtils.isStringNullOrEmpty(updateTime) ? null
            : DateTimeUtils.date2ddMMyyyyHHMMss(updateTime),
        this.crNumber,
        this.searchType,
        this.nodeCode,
        this.nodeIp,
        this.subDeptOri,
        this.subDeptResp,
        this.userLogin,
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
        this.assignUserId,
        this.isPrimaryCr,
        this.relateToPrimaryCrNumber,
        this.relateToPreApprovedCrNumber,
        this.relateCr,
        this.crTypeCat,
        this.crActionCodeTittle,
        this.crProcessName,
        this.affectedServiceList,
        this.commentCAB,
        this.commentZ78,
        this.commentQLTD,
        this.commentCreator,
        this.isSearchChildDeptToApprove,
        this.locale,
        this.compareDate,
        this.onTimeAmount,
        this.allComment,
        this.lstAffectedService,
        toApprovalDepartmentDTO(this.lstAppDept),
        this.lstNetworkNodeId,
        this.lstNetworkNodeIdAffected,
        this.crCreatedFromOtherSysDTO,
        this.lstCrCreatedFromOtherSysDTO,
        this.isClickedToAlarmTag,
        toCrAlarmDTO(this.lstAlarn),
        this.isClickedToModuleTag,
        this.lstModuleDetail,
        this.isClickedToVendorTag,
        this.lstVendorDetail,
        this.isClickedToCableTag,
        this.lstCable,
        StringUtils.isStringNullOrEmpty(this.sentDate) ? null
            : DateTimeUtils.date2ddMMyyyyHHMMss(this.sentDate),
        this.isClickedNode,
        this.isClickedNodeAffected,
        this.isOutOfDate,
        this.crProcessId,
        this.deviceTypeId,
        this.impactSegmentId,
        this.subcategoryId,
        this.userCab,
        this.autoExecute,

        //longlt6 add 2017-08-22 start
        this.resolveCodeId,
        this.resolveTitle,
        this.resolveReasonTitle,
        //longlt6 add 2017-08-22 end

        //longlt6 add 2017-08-22 start
        this.closeCodeId,
        this.closeTitle,
        this.closeReasonTitle,
        //longlt6 add 2017-08-22 end

        this.circleAdditionalInfo,
        this.closeCrAuto,
        this.resolveReturnCode,

        //longlt6 add 2017-11-28 end
        this.waitingMopStatus,
        this.vMSAValidateKey,
        this.totalAffectedCustomers,
        this.totalAffectedMinutes,

        this.isTracingCr,
        this.listWoId,

        this.createdDateFrom,
        this.createdDateTo,
        this.searchImpactedNodeIpIds,
        this.failDueToFT,
        this.nodeSavingMode,
        this.handoverCa,
        this.isHandoverCa,
        this.isLoadMop,
        this.responeTime,
        this.isConfirmAction == null ? null : String.valueOf(this.isConfirmAction),
        this.processTypeLv3Id,
        this.rankGate == null ? null : String.valueOf(this.rankGate),
        this.isRunType == null ? null : String.valueOf(this.isRunType),
        StringUtils.isStringNullOrEmpty(this.changeDate) ? null
            : DateTimeUtils.date2ddMMyyyyHHMMss(this.changeDate),
        this.kq_cr = kq_cr
    );

    return dto;
  }


  public CrInsiteDTO(String defaultSortField, String crId, String title, String description,
      String notes, String crType, String subcategory, String priority, String risk,
      String state, String processTypeId, String country, String region, String circle,
      String serviceAffecting, String affectedService, String relatedTt, String relatedPt,
      String relatedPtStepNumber, String changeOrginator, String changeOrginatorUnit,
      String changeResponsible, String changeResponsibleUnit, String changeOrginatorName,
      String changeOrginatorUnitName, String changeResponsibleName,
      String changeResponsibleUnitName, String dutyType, Date earliestStartTime,
      Date latestStartTime, String earliestStartTimeTo, String latestStartTimeTo,
      Date disturbanceStartTime, Date disturbanceEndTime, String relateToPrimaryCr,
      String relateToPreApprovedCr, String impactAffect, String impactSegment,
      String deviceType, Date createdDate, Date updateTime, String crNumber,
      String searchType, String nodeCode, String nodeIp, String subDeptOri,
      String subDeptResp, String userLogin, String button, String userLoginUnit,
      String scopeId, String actionRight, String crReturnCodeId, String crReturnResolve,
      String crReturnResolveTitle, String actionType, String actionNotes,
      String actionReturnCodeId, String considerUnitId, String considerUserId,
      String considerUnitName, String considerUserName, String assignUserId,
      String isPrimaryCr, String relateToPrimaryCrNumber,
      String relateToPreApprovedCrNumber, String relateCr, String crTypeCat,
      String crActionCodeTittle, String manageUnitId, String manageUserId,
      String crProcessName, String affectedServiceList, String commentCAB,
      String commentZ78, String commentQLTD, String commentCreator, Date sentDate,
      String isClickedNode, String isClickedNodeAffected, String isOutOfDate,
      String isSearchChildDeptToApprove, String locale, String compareDate,
      String onTimeAmount, String allComment, String crProcessId, String deviceTypeId,
      String impactSegmentId, String subcategoryId, String userCab, String autoExecute,
      String resolveTitle, String reasonTitle, String circleAdditionalInfo,
      String createdDateFrom, String createdDateTo, String waitingMopStatus,
      String vMSAValidateKey, String totalAffectedCustomers, String totalAffectedMinutes,
      String status, Integer isClickedToAlarmTag, Integer isClickedToModuleTag,
      Integer isClickedToVendorTag, String nodeSavingMode, String resolveCodeId,
      String resolveReasonTitle, String closeCodeId, String closeTitle,
      String closeReasonTitle, String closeCrAuto, String resolveReturnCode,
      String isTracingCr, String listWoId, String failDueToFT,
      List<Long> searchImpactedNodeIpIds, String activeWOControllSignal, String handoverCa,
      String isHandoverCa, String checkbox, String isLoadMop, String responeTime,
      String childImpactSegment, Integer isClickedToCableTag,
      List<List<Long>> searchCrIds, List<AttachDtDTO> attachDtDTO,
      CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO,
      List<CrApprovalDepartmentInsiteDTO> lstAppDept,
      List<CrAffectedServiceDetailsDTO> lstAffectedService,
      List<CrImpactedNodesDTO> lstNetworkNodeId,
      List<CrAffectedNodesDTO> lstNetworkNodeIdAffected,
      List<CrCreatedFromOtherSysDTO> lstCrCreatedFromOtherSysDTO,
      List<CrAlarmInsiteDTO> lstAlarn,
      List<CrVendorDetailDTO> lstVendorDetail,
      List<CrModuleDetailDTO> lstModuleDetail,
      List<CrCableDTO> lstCable, String processTypeLv3Id, String action,
      List<String> failedWoList, List<ItemDataCRInside> lstReturnCodeAll, boolean checkCrAuto,
      String subDTCode, Long systemId, Long objectId, Long stepId,
      List<ItemDataCRDTO> lstUserCab, Boolean isCheckAction, String crRelatedCbb,
      List<CrCableDTO> lstLaneCable, String workLog, Long isConfirmAction,
      Date originalEarliestStartTime, Date originalLatestStartTime, Long rankGate, Long isRunType) {
    this.defaultSortField = defaultSortField;
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
    this.changeOrginatorName = changeOrginatorName;
    this.changeOrginatorUnitName = changeOrginatorUnitName;
    this.changeResponsibleName = changeResponsibleName;
    this.changeResponsibleUnitName = changeResponsibleUnitName;
    this.dutyType = dutyType;
    this.earliestStartTime = earliestStartTime;
    this.latestStartTime = latestStartTime;
    this.earliestStartTimeTo = earliestStartTimeTo;
    this.latestStartTimeTo = latestStartTimeTo;
    this.disturbanceStartTime = disturbanceStartTime;
    this.disturbanceEndTime = disturbanceEndTime;
    this.relateToPrimaryCr = relateToPrimaryCr;
    this.relateToPreApprovedCr = relateToPreApprovedCr;
    this.impactAffect = impactAffect;
    this.impactSegment = impactSegment;
    this.deviceType = deviceType;
    this.createdDate = createdDate;
    this.updateTime = updateTime;
    this.crNumber = crNumber;
    this.searchType = searchType;
    this.nodeCode = nodeCode;
    this.nodeIp = nodeIp;
    this.subDeptOri = subDeptOri;
    this.subDeptResp = subDeptResp;
    this.userLogin = userLogin;
    this.button = button;
    this.userLoginUnit = userLoginUnit;
    this.scopeId = scopeId;
    this.actionRight = actionRight;
    this.crReturnCodeId = crReturnCodeId;
    this.crReturnResolve = crReturnResolve;
    this.crReturnResolveTitle = crReturnResolveTitle;
    this.actionType = actionType;
    this.actionNotes = actionNotes;
    this.actionReturnCodeId = actionReturnCodeId;
    this.considerUnitId = considerUnitId;
    this.considerUserId = considerUserId;
    this.considerUnitName = considerUnitName;
    this.considerUserName = considerUserName;
    this.assignUserId = assignUserId;
    this.isPrimaryCr = isPrimaryCr;
    this.relateToPrimaryCrNumber = relateToPrimaryCrNumber;
    this.relateToPreApprovedCrNumber = relateToPreApprovedCrNumber;
    this.relateCr = relateCr;
    this.crTypeCat = crTypeCat;
    this.crActionCodeTittle = crActionCodeTittle;
    this.manageUnitId = manageUnitId;
    this.manageUserId = manageUserId;
    this.crProcessName = crProcessName;
    this.affectedServiceList = affectedServiceList;
    this.commentCAB = commentCAB;
    this.commentZ78 = commentZ78;
    this.commentQLTD = commentQLTD;
    this.commentCreator = commentCreator;
    this.sentDate = sentDate;
    this.isClickedNode = isClickedNode;
    this.isClickedNodeAffected = isClickedNodeAffected;
    this.isOutOfDate = isOutOfDate;
    this.isSearchChildDeptToApprove = isSearchChildDeptToApprove;
    this.locale = locale;
    this.compareDate = compareDate;
    this.onTimeAmount = onTimeAmount;
    this.allComment = allComment;
    this.crProcessId = crProcessId;
    this.deviceTypeId = deviceTypeId;
    this.impactSegmentId = impactSegmentId;
    this.subcategoryId = subcategoryId;
    this.userCab = userCab;
    this.autoExecute = autoExecute;
    this.resolveTitle = resolveTitle;
    this.reasonTitle = reasonTitle;
    this.circleAdditionalInfo = circleAdditionalInfo;
    this.createdDateFrom = createdDateFrom;
    this.createdDateTo = createdDateTo;
    this.waitingMopStatus = waitingMopStatus;
    this.vMSAValidateKey = vMSAValidateKey;
    this.totalAffectedCustomers = totalAffectedCustomers;
    this.totalAffectedMinutes = totalAffectedMinutes;
    this.status = status;
    this.isClickedToAlarmTag = isClickedToAlarmTag;
    this.isClickedToModuleTag = isClickedToModuleTag;
    this.isClickedToVendorTag = isClickedToVendorTag;
    this.nodeSavingMode = nodeSavingMode;
    this.resolveCodeId = resolveCodeId;
    this.resolveReasonTitle = resolveReasonTitle;
    this.closeCodeId = closeCodeId;
    this.closeTitle = closeTitle;
    this.closeReasonTitle = closeReasonTitle;
    this.closeCrAuto = closeCrAuto;
    this.resolveReturnCode = resolveReturnCode;
    this.isTracingCr = isTracingCr;
    this.listWoId = listWoId;
    this.failDueToFT = failDueToFT;
    this.searchImpactedNodeIpIds = searchImpactedNodeIpIds;
    this.activeWOControllSignal = activeWOControllSignal;
    this.handoverCa = handoverCa;
    this.isHandoverCa = isHandoverCa;
    this.checkbox = checkbox;
    this.isLoadMop = isLoadMop;
    this.responeTime = responeTime;
    this.childImpactSegment = childImpactSegment;
    this.isClickedToCableTag = isClickedToCableTag;
    this.searchCrIds = searchCrIds;
    this.attachDtDTO = attachDtDTO;
    this.crCreatedFromOtherSysDTO = crCreatedFromOtherSysDTO;
    this.lstAppDept = lstAppDept;
    this.lstAffectedService = lstAffectedService;
    this.lstNetworkNodeId = lstNetworkNodeId;
    this.lstNetworkNodeIdAffected = lstNetworkNodeIdAffected;
    this.lstCrCreatedFromOtherSysDTO = lstCrCreatedFromOtherSysDTO;
    this.lstAlarn = lstAlarn;
    this.lstVendorDetail = lstVendorDetail;
    this.lstModuleDetail = lstModuleDetail;
    this.lstCable = lstCable;
    this.processTypeLv3Id = processTypeLv3Id;
    this.action = action;
    this.failedWoList = failedWoList;
    this.lstReturnCodeAll = lstReturnCodeAll;
    this.checkCrAuto = checkCrAuto;
    this.subDTCode = subDTCode;
    this.systemId = systemId;
    this.objectId = objectId;
    this.stepId = stepId;
    this.lstUserCab = lstUserCab;
    this.isCheckAction = isCheckAction;
    this.crRelatedCbb = crRelatedCbb;
    this.lstLaneCable = lstLaneCable;
    this.workLog = workLog;
    this.isConfirmAction = isConfirmAction;
    this.originalEarliestStartTime = originalEarliestStartTime;
    this.originalLatestStartTime = originalLatestStartTime;
    this.rankGate = rankGate;
    this.isRunType = isRunType;
  }

  public List<CrAlarmDTO> toCrAlarmDTO(List<CrAlarmInsiteDTO> crAlarmDTO) {
    if (crAlarmDTO != null) {
      List<CrAlarmDTO> crAlarmDTOS = new ArrayList<>();
      for (CrAlarmInsiteDTO dto : crAlarmDTO) {
        crAlarmDTOS.add(dto.toOutSideDTO());
      }
      return crAlarmDTOS;
    }
    return null;
  }

  public List<CrApprovalDepartmentDTO> toApprovalDepartmentDTO(
      List<CrApprovalDepartmentInsiteDTO> crApprovalDepartmentDTOS) {
    if (crApprovalDepartmentDTOS != null) {
      List<CrApprovalDepartmentDTO> lstCrApprovalDepartment = new ArrayList<>();
      for (CrApprovalDepartmentInsiteDTO dto : crApprovalDepartmentDTOS) {
        lstCrApprovalDepartment.add(dto.toOutSideDTO());
      }
      return lstCrApprovalDepartment;
    }
    return null;
  }
}
