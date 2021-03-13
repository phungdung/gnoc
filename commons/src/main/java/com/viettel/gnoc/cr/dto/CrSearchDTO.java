/**
 * @(#)CrForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement(name = "Cr")
@Getter
@Setter
public class CrSearchDTO extends BaseDto {

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
  private String earliestStartTime;
  private String latestStartTime;
  private String earliestStartTimeTo;
  private String latestStartTimeTo;
  private String disturbanceStartTime;
  private String disturbanceEndTime;
  private String relateToPrimaryCr;
  private String relateToPreApprovedCr;
  private String impactAffect;
  private String impactSegment;
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

  private String sentDate;
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
  private String isClickedToAlarmTag;
  private String isClickedToModuleTag;
  private String isClickedToVendorTag;
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

  //Constructor
  public CrSearchDTO() {
    setDefaultSortField("title");
  }

  public CrSearchDTO(String crId, String title, String description,
      String notes, String crType, String subcategory,
      String priority, String risk, String state,
      String processTypeId, String country, String region,
      String circle, String serviceAffecting, String affectedService,
      String relatedTt, String relatedPt, String relatedPtStepNumber,
      String changeOrginator, String changeOrginatorUnit,
      String changeResponsible, String changeResponsibleUnit,
      String dutyType, String earliestStartTime,
      String latestStartTime, String disturbanceStartTime,
      String disturbanceEndTime, String isPrimaryCr,
      String relateToPrimaryCr, String relateToPreApprovedCr,
      String impactAffect, String impactSegment,
      String deviceType, String createdDate, String updateTime,
      String crNumber, String crReturnCodeId, String sentDate) {
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
    this.sentDate = sentDate;
  }

}
