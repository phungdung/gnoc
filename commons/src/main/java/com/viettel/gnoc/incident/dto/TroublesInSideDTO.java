/**
 * @(#)TroublesForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.incident.model.TroublesEntity;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import com.viettel.gnoc.od.dto.OdDTO;
import com.viettel.gnoc.risk.dto.RiskDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
public class TroublesInSideDTO extends BaseDto {

  //Fields
  private Long troubleId;
  private String troubleCode;
  private String troubleName;
  private String description;
  private Long typeId;
  private Long subCategoryId;
  private Long priorityId;
  private Long impactId;
  private Long state;
  private String affectedNode;
  private String affectedService;
  private String location;
  private String locationCode;
  private Long locationId;
  private Date createdTime;
  private Date lastUpdateTime;
  private Date assignTime;
  private Date beginTroubleTime;
  private Date endTroubleTime;
  private Date deferredTime;
  private Long createUserId;
  private Long createUnitId;
  private String createUserName;
  private String createUnitName;
  private String insertSource;
  private String relatedPt;
  private Long vendorId;
  private String relatedKedb;
  private Long receiveUnitId;
  private String responeseUnitId;
  private Long receiveUserId;
  private String receiveUnitName;
  private String receiveUserName;
  private String rootCause;
  private String workArround;
  private Long solutionType;
  private String workLog;
  private Long rejectedCode;
  private String rejectReason;
  private Long risk;
  private Long supportUnitId;
  private Date queueTime;
  private Date clearTime;
  private Date closedTime;
  private String reasonOverdue;
  private Long timeZoneCreate;
  private String changedTime;
  private String priorityName;
  private String priorityCode;
  private String stateName;
  private String typeName;
  private String riskName;
  private Double timeUsed;
  private String remainTime;
  private String haveOpenCondition;
  private List<String> lstState;
  private List<String> lstType;
  private String keyword;
  private String notFinishBefore;
  private String createdTimeFrom;
  private String createdTimeTo;
  private String closedTimeFrom;
  private String closedTimeTo;
  private String searchSubUnitCreate;
  private String searchSubUnitReceive;
  private String userUnitId;
  private String deferredReason;
  private Long closeCode;
  private Long reasonId;
  private String attachFileList;
  private Date assignTimeTemp;
  private Double timeProcess;
  private String supportUnitName;
  private String reasonName;
  private Double timeCreateCfg;
  private String pathFileAttach;
  private String tblCurr;
  private String tblHis;
  private String tblClear;
  private Long alarmId;
  private List<InfraDeviceDTO> lstNode;
  private List<TroubleMopDTO> lstMop;
  private String createLateStr;
  private String affectTime;
  private String fixTime;
  private String isOverdueFixTime;
  private String isOverdueCloseTime;
  private String networkLevel;
  private Long autoCreateWO;
  private String lineCutCode; // mã tuyến đứt
  private String codeSnippetOff; // mã đoạn đứt
  private Long cableType; // loại cáp
  private String closuresReplace; // măng xông thay thế
  private String whereWrong; //sai tai dau
  private Long asessmentData; // danh gia du lieu tren NIMS
  private String alarmGroupId; // nhóm cảnh báo
  private String alarmGroupCode; // nhóm cảnh báo
  private String complaintGroupId; // nhóm khiếu nại
  private Long serviceType;  // Loại dịch vụ
  private String spmId;  //  id spm
  private String spmCode; // spm code
  private String reasonLv1Id;
  private String reasonLv1Name;
  private String reasonLv2Id;
  private String reasonLv2Name;
  private String reasonLv3Id;
  private String reasonLv3Name;
  private String reasonOverdueId;
  private String reasonOverdueName;
  private String reasonOverdueId2;
  private String reasonOverdueName2;
  private String strAlarmGroupDescription;
  private Double closeTtTime;
  private Long transNetworkTypeId; //ma loai mang truyen dan
  private Long transReasonEffectiveId; //Ma nguyen nhan anh huong dich vu
  private String transReasonEffectiveContent; //Noi dung nguyen nhan anh huong dich vu.
  private Long autoClose; //Ticket co dong tu dong hay khong, tu Nocpro goi sang.
  private String woCode; //Thoi gian clear canh bao
  private Long clearUserId;
  private String clearUserName;
  private String alarmGroupName; //HaiNV20 bo sung them thong tin ve ten canh bao
  private String relatedTroubleCodes; //HaiNV20, thong tin su co lien quan
  private String autoCloseName; //Bo sung ten truong tu dong de xuat file
  private String rejectedCodeName;
  private String subCategoryName;
  private Long reOccur; //HaiNV20 bo sung so lan xuat lai
  private String ccGroupId;
  private String checkbox;
  private String relatedTt;
  private Long isMove;
  private Date dateMove;
  private Long unitMove;
  private String unitMoveName;
  private String isUpdateAfterClosed;
  private Long isStationVip;
  private String isStationVipName;
  private Long isTickHelp;
  private Long numHelp;
  private Long countReopen;

  private Map<String, String> map;
  // phuc vu WO luu du thua
  private String infraType;
  private String telServiceId;
  private String isCcResult;
  private String productCode;
  private String spmName;
  private String processingUnitName; //Tên đơn vị của nhân viên đang xử lý
  private String processingUnitId; //Id đơn vị của nhân viên đang xử lý
  private String processingUserName; //Tên nhân viên đang xử lý
  private String processingUserPhone; //Tên Số điện thoại nhân viên đang xử lý
  private String processingUserId;
  private String totalPendingTimeGnoc;
  private String totalProcessTimeGnoc;
  private String evaluateGnoc;
  private String stateWo;
  private Long numPending;
  private String CdId;
  private String isCheck;
  private String customerPhone;
  private String linkCode;//ma tuyen do noc day sang cho WO
  private String linkId;
  private Long amiId;
  // phuc vu WO luu du thua
  private String woContent;
  private List<byte[]> fileDocumentByteArray;
  private List<String> arrFileName;
  List<String> listAccount;
  private Long numAon;
  private Long numGpon;
  private Long numNexttv;
  private Long numThc;
  private String technology;    //cong nghe
  private String infoTicket;  //thong tin su co
  private String reasonExist;
  private Date catchingTime; // thoi gian bat duoc canh bao
  private String cameraId;
  private String relatedCr;
  private String nationCode;
  private Long isChat;
  private Long complaintId;
  private Long numReassign;
  private Long deferType;//loai tam dong
  private Date estimateTime;//thoi gian du kien
  private String longitude;//kinh do
  private String latitude;//vi do
  private Long groupSolution; //nhom giai phap
  private String cellService;//cell phuc vu
  private String concave; //vong lom
  private Long errorCode;
  private Long typeUnit;
  private Long isSendTktu;
  private Long troubleAssignId;
  //luu cho cc
  private Long createUnitIdByCC;
  private Long complaintTypeId;
  private Long complaintParentId;
  private String isdn;
  private String createUserByCC;
  private Long isOverdue;
  private String lstComplaint;
  private String downtime;
  private String woType;
  private String accountGline;

  private String customerTimeDesireFrom;
  private String customerTimeDesireTo;
  private String customerName;
  private String ftInfo;
  private String numAffect;
  private String subMin;
  private Long warnLevel;
  private Long isFailDevice;
  private String country;

  private String impactName;
  private String solutionTypeName;
  private String vendorName;
  private String warnLevelName;
  private String cableTypeName;
  private String transNetworkTypeName;
  private String transReasonEffectiveName;
  private List<String> listItAccount;
  private List<UsersInsideDto> usersInsideDtos;
  private AuthorityDTO authorityDTO;
  private List<Long> listTroubleId;

  private List<String> lstCreateUnitIdByCC;
  private List<String> lstComplaintTypeId;
  private List<String> lstComplaintParentId;
  private List<String> lstTroubleCode;
  private String[] arFileName;
  private byte[][] arFileData;

  private Long parentId;
  private int level;
  private List<CrDTO> crDTOS;
  private String unitId;
  private String startTime;
  private String endTime;
  //dunglv add
  private String checkClearTime;
  private String searchUnitMove;
  private String checkMoveUnit;
  private List<OdDTO> odDTOS;
  private List<RiskDTO> riskDTOS;
  // thang dt add
  private String estimatedTimeToEndFrom;
  private String estimatedTimeToEndTo;
  // hungtv add
  private String typeCode;
  private String subCategoryCode;
  private String countryCode;
  private String impactCode;
  private String receiveUnitCode;
  private String message;
  private String unitApproval;

  //hungtv77 add phe duyet tam dong
  private String resultApproval;
  private Boolean sendSmsApproval;

  private Boolean isUnitGnoc;

  public TroublesInSideDTO(Long troubleId, String troubleCode, String troubleName,
                           String description, Long typeId, Long subCategoryId, Long priorityId, Long impactId,
                           Long state, String affectedNode, String affectedService, String location, Long locationId,
                           Date createdTime, Date lastUpdateTime, Date assignTime, Date beginTroubleTime,
                           Date endTroubleTime, Date deferredTime, Long createUserId, Long createUnitId,
                           String createUserName, String createUnitName, String insertSource, String relatedPt,
                           Long vendorId, String relatedKedb, Long receiveUnitId, Long receiveUserId,
                           String receiveUnitName, String receiveUserName, String rootCause, String workArround,
                           Long solutionType, String workLog, Long rejectedCode, String rejectReason, Long risk,
                           Long supportUnitId, Date queueTime, Date clearTime, Date closedTime, String reasonOverdue,
                           Long timeZoneCreate, String deferredReason, Long closeCode, Long reasonId,
                           String supportUnitName, String reasonName, String tblCurr, String tblHis, String tblClear,
                           Long alarmId, String networkLevel,
                           Long autoCreateWO, String lineCutCode, String codeSnippetOff,
                           Long cableType, String closuresReplace, String whereWrong, Long asessmentData,
                           String alarmGroupId, Long serviceType, String spmId, String spmCode, String complaintGroupId,
                           String reasonLv1Id,
                           String reasonLv1Name, String reasonLv2Id, String reasonLv2Name, String reasonLv3Id,
                           String reasonLv3Name, String reasonOverdueId, String reasonOverdueName,
                           String reasonOverdueId2, String reasonOverdueName2, String relatedTt,
                           Long isMove, Date dateMove, Long unitMove, String unitMoveName, String isUpdateAfterClosed,
                           Long isStationVip, Long isTickHelp,
                           Long numPending, Long numAon, Long numGpon, Long numNexttv, Long numThc, String technology,
                           String infoTicket,
                           Date catchingTime, Long numHelp, Long amiId, String relatedCr, String nationCode, Long isChat,
                           Long complaintId,
                           Long numReassign, Long deferType, Date estimateTime, String longitude, String latitude,
                           Long groupSolution, String cellService,
                           String concave, Long errorCode, Long typeUnit, Long isSendTktu, Long troubleAssignId,
                           Long createUnitIdByCC,
                           Long complaintTypeId, Long complaintParentId, String isdn, String createUserByCC,
                           String lstComplaint, String downtime,
                           Long countReopen, String numAffect, String subMin, Long warnLevel, Long isFailDevice, String unitApproval,
                           String country, Long isOverdue, Date assignTimeTemp, Double timeProcess, Double timeUsed

  ) {
    this.troubleId = troubleId;
    this.troubleCode = troubleCode;
    this.troubleName = troubleName;
    this.description = description;
    this.typeId = typeId;
    this.subCategoryId = subCategoryId;
    this.priorityId = priorityId;
    this.impactId = impactId;
    this.state = state;
    this.affectedNode = affectedNode;
    this.affectedService = affectedService;
    this.location = location;
    this.locationId = locationId;
    this.createdTime = createdTime;
    this.lastUpdateTime = lastUpdateTime;
    this.assignTime = assignTime;
    this.beginTroubleTime = beginTroubleTime;
    this.endTroubleTime = endTroubleTime;
    this.deferredTime = deferredTime;
    this.createUserId = createUserId;
    this.createUnitId = createUnitId;
    this.createUserName = createUserName;
    this.createUnitName = createUnitName;
    this.insertSource = insertSource;
    this.relatedPt = relatedPt;
    this.vendorId = vendorId;
    this.relatedKedb = relatedKedb;
    this.receiveUnitId = receiveUnitId;
    this.receiveUserId = receiveUserId;
    this.receiveUnitName = receiveUnitName;
    this.receiveUserName = receiveUserName;
    this.rootCause = rootCause;
    this.workArround = workArround;
    this.solutionType = solutionType;
    this.workLog = workLog;
    this.rejectedCode = rejectedCode;
    this.rejectReason = rejectReason;
    this.risk = risk;
    this.supportUnitId = supportUnitId;
    this.queueTime = queueTime;
    this.clearTime = clearTime;
    this.closedTime = closedTime;
    this.reasonOverdue = reasonOverdue;
    this.timeZoneCreate = timeZoneCreate;
    this.deferredReason = deferredReason;
    this.closeCode = closeCode;
    this.reasonId = reasonId;
    this.supportUnitName = supportUnitName;
    this.reasonName = reasonName;
    this.tblCurr = tblCurr;
    this.tblHis = tblHis;
    this.tblClear = tblClear;
    this.alarmId = alarmId;
    this.networkLevel = networkLevel;
    this.autoCreateWO = autoCreateWO;
    this.lineCutCode = lineCutCode;
    this.codeSnippetOff = codeSnippetOff;
    this.cableType = cableType;
    this.closuresReplace = closuresReplace;
    this.whereWrong = whereWrong;
    this.asessmentData = asessmentData;
    this.alarmGroupId = alarmGroupId;
    this.serviceType = serviceType;
    this.spmId = spmId;
    this.spmCode = spmCode;
    this.complaintGroupId = complaintGroupId;
    this.reasonLv1Id = reasonLv1Id;
    this.reasonLv1Name = reasonLv1Name;
    this.reasonLv2Id = reasonLv2Id;
    this.reasonLv2Name = reasonLv2Name;
    this.reasonLv3Id = reasonLv3Id;
    this.reasonLv3Name = reasonLv3Name;
    this.reasonOverdueId = reasonOverdueId;
    this.reasonOverdueName = reasonOverdueName;
    this.reasonOverdueId2 = reasonOverdueId2;
    this.reasonOverdueName2 = reasonOverdueName2;
    this.relatedTt = relatedTt;
    this.isMove = isMove;
    this.dateMove = dateMove;
    this.unitMove = unitMove;
    this.unitMoveName = unitMoveName;
    this.isUpdateAfterClosed = isUpdateAfterClosed;
    this.isStationVip = isStationVip;
    this.isTickHelp = isTickHelp;
    this.numHelp = numHelp;
    this.numPending = numPending;
    this.numAon = numAon;
    this.numGpon = numGpon;
    this.numNexttv = numNexttv;
    this.numThc = numThc;
    this.technology = technology;
    this.infoTicket = infoTicket;
    this.catchingTime = catchingTime;
    this.amiId = amiId;
    this.relatedCr = relatedCr;
    this.nationCode = nationCode;
    this.isChat = isChat;
    this.complaintId = complaintId;
    this.numReassign = numReassign;
    this.deferType = deferType;
    this.estimateTime = estimateTime;
    this.longitude = longitude;
    this.latitude = latitude;
    this.groupSolution = groupSolution;
    this.cellService = cellService;
    this.errorCode = errorCode;
    this.typeUnit = typeUnit;
    this.isSendTktu = isSendTktu;
    this.troubleAssignId = troubleAssignId;
    this.createUnitIdByCC = createUnitIdByCC;
    this.complaintTypeId = complaintTypeId;
    this.complaintParentId = complaintParentId;
    this.isdn = isdn;
    this.createUserByCC = createUserByCC;
    this.lstComplaint = lstComplaint;
    this.downtime = downtime;
    this.countReopen = countReopen;
    this.numAffect = numAffect;
    this.subMin = subMin;
    this.warnLevel = warnLevel;
    this.isFailDevice = isFailDevice;
    this.country = country;
    this.isOverdue = isOverdue;
    this.assignTimeTemp = assignTimeTemp;
    this.concave = concave;
    this.timeProcess = timeProcess;
    this.timeUsed = timeUsed;
    this.unitApproval = unitApproval;
  }

  public TroublesEntity toEntity() {
    TroublesEntity model = new TroublesEntity(
        troubleId,
        troubleCode,
        troubleName,
        description,
        typeId,
        subCategoryId,
        priorityId,
        impactId,
        state,
        affectedNode,
        affectedService,
        location,
        locationId,
        createdTime,
        lastUpdateTime,
        assignTime,
        beginTroubleTime,
        endTroubleTime,
        deferredTime,
        createUserId,
        createUnitId,
        createUserName,
        createUnitName,
        insertSource,
        relatedPt,
        vendorId,
        relatedKedb,
        receiveUnitId,
        receiveUserId,
        receiveUnitName,
        receiveUserName,
        rootCause,
        workArround,
        solutionType,
        workLog,
        rejectedCode,
        rejectReason,
        risk,
        supportUnitId,
        queueTime,
        clearTime,
        closedTime,
        reasonOverdue,
        timeZoneCreate,
        deferredReason,
        closeCode,
        reasonId,
        timeUsed,
        assignTimeTemp,
        timeProcess,
        supportUnitName,
        reasonName,
        timeCreateCfg,
        tblCurr,
        tblHis,
        tblClear,
        alarmId,
        networkLevel,
        autoCreateWO,
        lineCutCode,
        codeSnippetOff,
        cableType,
        closuresReplace,
        whereWrong,
        asessmentData,
        alarmGroupId,
        serviceType,
        complaintGroupId,
        reasonLv1Id,
        reasonLv1Name,
        reasonLv2Id,
        reasonLv2Name,
        reasonLv3Id,
        reasonLv3Name,
        reasonOverdueId,
        reasonOverdueName,
        reasonOverdueId2,
        reasonOverdueName2,
        spmCode,
        relatedTt,
        isMove,
        dateMove,
        unitMove,
        unitMoveName,
        isUpdateAfterClosed,
        isStationVip,
        isTickHelp,
        numPending,
        numAon,
        numGpon,
        numNexttv,
        numThc,
        technology,
        infoTicket,
        catchingTime,
        numHelp,
        amiId,
        relatedCr,
        nationCode,
        isChat,
        complaintId,
        numReassign,
        deferType,
        estimateTime,
        longitude, latitude,
        groupSolution,
        cellService, concave, errorCode,
        typeUnit,
        isSendTktu,
        troubleAssignId,
        createUnitIdByCC,
        complaintTypeId,
        complaintParentId,
        isdn, createUserByCC, lstComplaint, downtime,
        countReopen,
        numAffect, subMin, warnLevel,
        isFailDevice,
        country, isOverdue
        , unitApproval
    );

    //HaiNV20 add
    model.setCloseTtTime(closeTtTime);
    model.setTransNetworkTypeId(transNetworkTypeId);
    model.setTransReasonEffectiveId(transReasonEffectiveId);
    model.setTransReasonEffectiveContent(transReasonEffectiveContent);
    model.setAutoClose(autoClose);
    model.setWoCode(woCode);
    model.setClearUserId(clearUserId);
    model.setClearUserName(clearUserName);
//            model.setProcessingGuidelines(processingGuidelines); //HaiNV20 add thong tin huong dan xu ly tu Nocpro.
    model.setRelatedTroubleCodes(relatedTroubleCodes); //HaiNV20 add thong tin
    model.setReOccur(reOccur);

    //HaiNV20 add end
    return model;
  }

  public String toString() {
    return "TroublesDTO{" + "troubleId=" + troubleId + ", \n"
        + "troubleCode=" + troubleCode + ", \n"
        + "troubleName=" + troubleName + ", \n"
        + "description=" + description + ", \n"
        + "typeId=" + typeId + ", \n"
        + "subCategoryId=" + subCategoryId + ", \n"
        + "priorityId=" + priorityId + ", \n"
        + "impactId=" + impactId + ", \n"
        + "state=" + state + ", \n"
        + "affectedNode=" + affectedNode + ", \n"
        + "affectedService=" + affectedService + ", \n"
        + "location=" + location + ", \n"
        + "locationId=" + locationId + ", \n"
        + "createdTime=" + createdTime + ", \n"
        + "lastUpdateTime=" + lastUpdateTime + ", \n"
        + "assignTime=" + assignTime + ", \n"
        + "beginTroubleTime=" + beginTroubleTime + ", \n"
        + "endTroubleTime=" + endTroubleTime + ", \n"
        + "deferredTime=" + deferredTime + ", \n"
        + "createUserId=" + createUserId + ", \n"
        + "createUnitId=" + createUnitId + ", \n"
        + "createUserName=" + createUserName + ", \n"
        + "createUnitName=" + createUnitName + ", \n"
        + "insertSource=" + insertSource + ", \n"
        + "relatedPt=" + relatedPt + ", \n"
        + "vendorId=" + vendorId + ", \n"
        + "relatedKedb=" + relatedKedb + ", \n"
        + "receiveUnitId=" + receiveUnitId + ", \n"
        + "receiveUserId=" + receiveUserId + ", \n"
        + "receiveUnitName=" + receiveUnitName + ", \n"
        + "receiveUserName=" + receiveUserName + ", \n"
        + "rootCause=" + rootCause + ", \n"
        + "workArround=" + workArround + ", \n"
        + "solutionType=" + solutionType + ", \n"
        + "workLog=" + workLog + ", \n"
        + "rejectedCode=" + rejectedCode + ", \n"
        + "rejectReason=" + rejectReason + ", \n"
        + "risk=" + risk + ", \n"
        + "supportUnitId=" + supportUnitId + ", \n"
        + "queueTime=" + queueTime + ", \n"
        + "clearTime=" + clearTime + ", \n"
        + "closedTime=" + closedTime + ", \n"
        + "reasonOverdue=" + reasonOverdue + ", \n"
        + "timeZoneCreate=" + timeZoneCreate + ", \n"
        + "priorityName=" + priorityName + ", \n"
        + "stateName=" + stateName + ", \n"
        + "typeName=" + typeName + ", \n"
        + "riskName=" + riskName + ", \n"
        + "timeUsed=" + timeUsed + ", \n"
        + "searchSubUnitCreate=" + searchSubUnitCreate + ", \n"
        + "searchSubUnitReceive=" + searchSubUnitReceive + ", \n"
        + "userUnitId=" + userUnitId + ", \n"
        + "deferredReason=" + deferredReason + ", \n"
        + "closeCode=" + closeCode + ", \n"
        + "reasonId=" + reasonId + ", \n"
        + "assignTimeTemp=" + assignTimeTemp + ", \n"
        + "timeProcess=" + timeProcess + ", \n"
        + "supportUnitName=" + supportUnitName + ", \n"
        + "reasonName=" + reasonName + ", \n"
        + "timeCreateCfg=" + timeCreateCfg + ", \n"
        + "networkLevel=" + networkLevel + ", \n"
        + "closeTtTime=" + closeTtTime + ", \n"
        + "transNetworkTypeId=" + transNetworkTypeId + ", \n"
        + "transReasonEffectiveId=" + transReasonEffectiveId + ", \n"
        + "transReasonEffectiveContent=" + transReasonEffectiveContent + ", \n"
        + "autoClose=" + autoClose + ", \n"
        + "woCode=" + woCode + ", \n"
        //                + "processingGuideLine=" + processingGuidelines + ", \n"
        + "relatedTroubleCodes=" + relatedTroubleCodes + '}';
  }

  public String compareContent(Object obj) {
    String ret = "";
    String before = "Before:\n";
    String after = "After:\n";
    if (obj == null) {
      return "";
    }
    final TroublesEntity other = (TroublesEntity) obj;
    if (!Objects.equals(this.troubleCode, other.getTroubleCode())) {
      before += "troubleCode:" + this.troubleCode + "\n";
      after += "troubleCode:" + other.getTroubleCode() + "\n";
    }
    if (!Objects.equals(this.troubleName, other.getTroubleName())) {
      before += "troubleName:" + this.troubleName + "\n";
      after += "troubleName:" + other.getTroubleName() + "\n";
    }
    if (!Objects.equals(this.description, other.getDescription())) {
      before += "description:" + this.description + "\n";
      after += "description:" + other.getDescription() + "\n";
    }
    if (!Objects.equals(this.typeId, other.getTypeId())) {
      before += "typeId:" + this.typeId + "\n";
      after += "typeId:" + other.getTypeId() + "\n";
    }
    if (!Objects.equals(this.subCategoryId, other.getSubCategoryId())) {
      before += "subCategoryId:" + this.subCategoryId + "\n";
      after += "subCategoryId:" + other.getTypeId() + "\n";
    }
    if (!Objects.equals(this.priorityId, other.getPriorityId())) {
      before += "priorityId:" + this.priorityId + "\n";
      after += "priorityId:" + other.getPriorityId() + "\n";
    }
    if (!Objects.equals(this.impactId, other.getImpactId())) {
      before += "impactId:" + this.impactId + "\n";
      after += "impactId:" + other.getImpactId() + "\n";
    }
    if (!Objects.equals(this.state, other.getState())) {
      before += "state:" + this.state + "\n";
      after += "state:" + other.getState() + "\n";
    }
    if (!Objects.equals(this.affectedNode, other.getAffectedNode())) {
      before += "affectedNode:" + this.affectedNode + "\n";
      after += "affectedNode:" + other.getAffectedNode() + "\n";
    }
    if (!Objects.equals(this.affectedService, other.getAffectedService())) {
      before += "affectedService:" + this.affectedService + "\n";
      after += "affectedService:" + other.getAffectedService() + "\n";
    }
    if (!Objects.equals(this.location, other.getLocation())) {
      before += "location:" + this.location + "\n";
      after += "location:" + other.getLocation() + "\n";
    }
    if (!Objects.equals(this.locationId, other.getLocationId())) {
      before += "locationId:" + this.locationId + "\n";
      after += "locationId:" + other.getLocationId() + "\n";
    }
    if (!Objects.equals(this.createdTime, other.getCreatedTime())) {
      before += "createdTime:" + this.createdTime + "\n";
      after += "createdTime:" + other.getCreatedTime() + "\n";
    }
    if (!Objects.equals(this.lastUpdateTime, other.getLastUpdateTime())) {
      before += "lastUpdateTime:" + this.lastUpdateTime + "\n";
      after += "lastUpdateTime:" + other.getLastUpdateTime() + "\n";
    }
    if (!Objects.equals(this.assignTime, other.getAssignTime())) {
      before += "assignTime:" + this.assignTime + "\n";
      after += "assignTime:" + other.getAssignTime() + "\n";
    }
    if (!Objects.equals(this.beginTroubleTime, other.getBeginTroubleTime())) {
      before += "beginTroubleTime:" + this.beginTroubleTime + "\n";
      after += "beginTroubleTime:" + other.getBeginTroubleTime() + "\n";
    }
    if (!Objects.equals(this.endTroubleTime, other.getEndTroubleTime())) {
      before += "endTroubleTime:" + this.endTroubleTime + "\n";
      after += "endTroubleTime:" + other.getEndTroubleTime() + "\n";
    }
    if (!Objects.equals(this.deferredTime, other.getDeferredTime())) {
      before += "deferredTime:" + this.deferredTime + "\n";
      after += "deferredTime:" + other.getDeferredTime() + "\n";
    }

    if (!Objects.equals(this.vendorId, other.getVendorId())) {
      before += "vendorId:" + this.vendorId + "\n";
      after += "vendorId:" + other.getVendorId() + "\n";
    }
    if (!Objects.equals(this.relatedKedb, other.getRelatedKedb())) {
      before += "relatedKedb:" + this.relatedKedb + "\n";
      after += "relatedKedb:" + other.getRelatedKedb() + "\n";
    }
    if (!Objects.equals(this.receiveUnitId, other.getReceiveUnitId())) {
      before += "receiveUnitId:" + this.receiveUnitId + "\n";
      after += "receiveUnitId:" + other.getReceiveUnitId() + "\n";
      before += "receiveUnitName:" + this.receiveUnitName + "\n";
      after += "receiveUnitName:" + other.getReceiveUnitName() + "\n";
    }
    if (!Objects.equals(this.receiveUserId, other.getReceiveUserId())) {
      before += "receiveUserId:" + this.receiveUserId + "\n";
      after += "receiveUserId:" + other.getReceiveUserId() + "\n";
      before += "receiveUserName:" + this.receiveUserName + "\n";
      after += "receiveUserName:" + other.getReceiveUserName() + "\n";
    }
    if (!Objects.equals(this.rootCause, other.getRootCause())) {
      before += "rootCause:" + this.rootCause + "\n";
      after += "rootCause:" + other.getRootCause() + "\n";
    }
    if (!Objects.equals(this.workArround, other.getWorkArround())) {
      before += "workArround:" + this.workArround + "\n";
      after += "workArround:" + other.getWorkArround() + "\n";
    }
    if (!Objects.equals(this.solutionType, other.getSolutionType())) {
      before += "solutionType:" + this.solutionType + "\n";
      after += "solutionType:" + other.getSolutionType() + "\n";
    }
    if (!Objects.equals(this.rejectedCode, other.getRejectedCode())) {
      before += "rejectedCode:" + this.rejectedCode + "\n";
      after += "rejectedCode:" + other.getRejectedCode() + "\n";
    }
    if (!Objects.equals(this.rejectReason, other.getRejectReason())) {
      before += "rejectReason:" + this.rejectReason + "\n";
      after += "rejectReason:" + other.getRejectReason() + "\n";
    }
    if (!Objects.equals(this.risk, other.getRisk())) {
      before += "risk:" + this.risk + "\n";
      after += "risk:" + other.getRisk() + "\n";
    }
    if (!Objects.equals(this.supportUnitId, other.getSupportUnitId())) {
      before += "supportUnitId:" + this.supportUnitId + "\n";
      after += "supportUnitId:" + other.getSupportUnitId() + "\n";
      before += "supportUnitName:" + this.supportUnitName + "\n";
      after += "supportUnitName:" + other.getSupportUnitName() + "\n";
    }
    if (!Objects.equals(this.queueTime, other.getQueueTime())) {
      before += "queueTime:" + this.queueTime + "\n";
      after += "queueTime:" + other.getQueueTime() + "\n";
    }
    if (!Objects.equals(this.clearTime, other.getClearTime())) {
      before += "clearTime:" + this.clearTime + "\n";
      after += "clearTime:" + other.getClearTime() + "\n";
    }
    if (!Objects.equals(this.closedTime, other.getClosedTime())) {
      before += "closedTime:" + this.closedTime + "\n";
      after += "closedTime:" + other.getClosedTime() + "\n";
    }
    if (!Objects.equals(this.reasonOverdue, other.getReasonOverdue())) {
      before += "reasonOverdue:" + this.reasonOverdue + "\n";
      after += "reasonOverdue:" + other.getReasonOverdue() + "\n";
    }
    if (!Objects.equals(this.timeZoneCreate, other.getTimeZoneCreate())) {
      before += "timeZoneCreate:" + this.timeZoneCreate + "\n";
      after += "timeZoneCreate:" + other.getTimeZoneCreate() + "\n";
    }

    if (!Objects.equals(this.deferredReason, other.getDeferredReason())) {
      before += "deferredReason:" + this.deferredReason + "\n";
      after += "deferredReason:" + other.getDeferredReason() + "\n";
    }
    if (!Objects.equals(this.closeCode, other.getCloseCode())) {
      before += "closeCode:" + this.closeCode + "\n";
      after += "closeCode:" + other.getCloseCode() + "\n";
    }
    if (!Objects.equals(this.reasonId, other.getReasonId())) {
      before += "reasonId:" + this.reasonId + "\n";
      after += "reasonId:" + other.getReasonId() + "\n";
    }

    if (!Objects.equals(this.supportUnitName, other.getSupportUnitName())) {
      before += "supportUnitName:" + this.supportUnitName + "\n";
      after += "supportUnitName:" + other.getSupportUnitName() + "\n";
    }
    if (!Objects.equals(this.reasonName, other.getReasonName())) {
      before += "reasonName:" + this.reasonName + "\n";
      after += "reasonName:" + other.getReasonName() + "\n";
    }
    if (!Objects.equals(this.networkLevel, other.getNetworkLevel())) {
      before += "networkLevel:" + this.networkLevel + "\n";
      after += "networkLevel:" + other.getNetworkLevel() + "\n";
    }

    ret += before + "\n";
    ret += "=====================================\n";
    ret += after;
    return ret;
  }

  public TroublesDTO toModelOutSide() {
    TroublesDTO model = new TroublesDTO(
        StringUtils.validString(troubleId) ? String.valueOf(troubleId) : null,
        troubleCode,
        troubleName,
        description,
        StringUtils.validString(typeId) ? String.valueOf(typeId) : null,
        StringUtils.validString(subCategoryId) ? String.valueOf(subCategoryId) : null,
        StringUtils.validString(priorityId) ? String.valueOf(priorityId) : null,
        StringUtils.validString(impactId) ? String.valueOf(impactId) : null,
        StringUtils.validString(state) ? String.valueOf(state) : null,
        affectedNode,
        affectedService,
        location,
        locationCode,
        StringUtils.validString(locationId) ? String.valueOf(locationId) : null,
        StringUtils.validString(createdTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(createdTime)
            : null,
        StringUtils.validString(lastUpdateTime) ? DateTimeUtils
            .date2ddMMyyyyHHMMss(lastUpdateTime) : null,
        StringUtils.validString(assignTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(assignTime)
            : null,
        StringUtils.validString(beginTroubleTime) ? DateTimeUtils
            .date2ddMMyyyyHHMMss(beginTroubleTime) : null,
        StringUtils.validString(endTroubleTime) ? DateTimeUtils
            .date2ddMMyyyyHHMMss(endTroubleTime) : null,
        StringUtils.validString(deferredTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(deferredTime)
            : null,
        StringUtils.validString(createUserId) ? String.valueOf(createUserId) : null,
        StringUtils.validString(createUnitId) ? String.valueOf(createUnitId) : null,
        createUserName,
        createUnitName,
        insertSource,
        relatedPt,
        StringUtils.validString(vendorId) ? String.valueOf(vendorId) : null,
        relatedKedb,
        StringUtils.validString(receiveUnitId) ? String.valueOf(receiveUnitId) : null,
        responeseUnitId,
        StringUtils.validString(receiveUserId) ? String.valueOf(receiveUserId) : null,
        receiveUnitName,
        receiveUserName,
        rootCause,
        workArround,
        StringUtils.validString(solutionType) ? String.valueOf(solutionType) : null,
        workLog,
        StringUtils.validString(rejectedCode) ? String.valueOf(rejectedCode) : null,
        rejectReason,
        StringUtils.validString(risk) ? String.valueOf(risk) : null,

        StringUtils.validString(supportUnitId) ? String.valueOf(supportUnitId) : null,
        StringUtils.validString(queueTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(queueTime) : null,
        StringUtils.validString(clearTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(clearTime) : null,
        StringUtils.validString(closedTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(closedTime)
            : null,
        reasonOverdue,
        StringUtils.validString(timeZoneCreate) ? String.valueOf(timeZoneCreate) : null,
        changedTime,
        priorityName,
        priorityCode,
        stateName,
        typeName,
        riskName,
        StringUtils.validString(timeUsed) ? String.valueOf(timeUsed) : null,
        remainTime,
        haveOpenCondition,
        lstState,
        lstType,
        keyword,
        notFinishBefore,
        createdTimeFrom,
        createdTimeTo,
        closedTimeFrom,
        closedTimeTo,
        searchSubUnitCreate,
        searchSubUnitReceive,
        userUnitId,
        deferredReason,
        StringUtils.validString(closeCode) ? String.valueOf(closeCode) : null,
        StringUtils.validString(reasonId) ? String.valueOf(reasonId) : null,
        attachFileList,
        StringUtils.validString(assignTimeTemp) ? DateTimeUtils
            .date2ddMMyyyyHHMMss(assignTimeTemp) : null,
        StringUtils.validString(timeProcess) ? String.valueOf(timeProcess) : null,
        supportUnitName,
        reasonName,
        StringUtils.validString(timeCreateCfg) ? String.valueOf(timeCreateCfg) : null,
        pathFileAttach,
        tblCurr,
        tblHis,
        tblClear,
        StringUtils.validString(alarmId) ? String.valueOf(alarmId) : null,
        lstNode,
        lstMop,
        createLateStr,
        affectTime,
        fixTime,
        isOverdueFixTime,
        isOverdueCloseTime,
        networkLevel,
        autoCreateWO,
        lineCutCode,
        codeSnippetOff,
        cableType,
        closuresReplace,
        whereWrong,
        asessmentData,
        alarmGroupId,
        alarmGroupCode,
        complaintGroupId,
        serviceType,
        spmId,
        spmCode,
        reasonLv1Id,
        reasonLv1Name,
        reasonLv2Id,
        reasonLv2Name,
        reasonLv3Id,
        reasonLv3Name,
        reasonOverdueId,
        reasonOverdueName,
        reasonOverdueId2,
        reasonOverdueName2,
        strAlarmGroupDescription,
        StringUtils.validString(closeTtTime) ? String.valueOf(closeTtTime) : null,
        transNetworkTypeId,
        transReasonEffectiveId,
        transReasonEffectiveContent,
        autoClose,
        woCode,
        clearUserId,
        clearUserName,
        alarmGroupName,
        relatedTroubleCodes,
        autoCloseName,
        rejectedCodeName,
        subCategoryName,
        StringUtils.validString(reOccur) ? String.valueOf(reOccur) : null,
        ccGroupId,
        checkbox,
        relatedTt,
        StringUtils.validString(isMove) ? String.valueOf(isMove) : null,
        StringUtils.validString(dateMove) ? DateTimeUtils.date2ddMMyyyyHHMMss(dateMove) : null,
        StringUtils.validString(unitMove) ? String.valueOf(unitMove) : null,
        unitMoveName,
        isUpdateAfterClosed,
        StringUtils.validString(isStationVip) ? String.valueOf(isStationVip) : null,
        isStationVipName,
        StringUtils.validString(isTickHelp) ? String.valueOf(isTickHelp) : null,
        StringUtils.validString(numHelp) ? String.valueOf(numHelp) : null,
        StringUtils.validString(countReopen) ? String.valueOf(countReopen) : null,
        map,
        infraType,
        telServiceId,
        isCcResult,
        productCode,
        spmName,
        processingUnitName,
        processingUnitId,
        processingUserName,
        processingUserPhone,
        processingUserId,
        totalPendingTimeGnoc,
        totalProcessTimeGnoc,
        evaluateGnoc,
        stateWo,
        StringUtils.validString(numPending) ? String.valueOf(numPending) : null,
        CdId,
        isCheck,
        customerPhone,
        linkCode,
        linkId,
        StringUtils.validString(amiId) ? String.valueOf(amiId) : null,
        woContent,
        fileDocumentByteArray,
        arrFileName,
        listAccount,
        StringUtils.validString(numAon) ? String.valueOf(numAon) : null,
        StringUtils.validString(numGpon) ? String.valueOf(numGpon) : null,
        StringUtils.validString(numNexttv) ? String.valueOf(numNexttv) : null,
        StringUtils.validString(numThc) ? String.valueOf(numThc) : null,
        technology,
        infoTicket,
        reasonExist,
        StringUtils.validString(catchingTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(catchingTime)
            : null,
        cameraId,
        relatedCr,
        nationCode,
        StringUtils.validString(isChat) ? String.valueOf(isChat) : null,
        StringUtils.validString(complaintId) ? String.valueOf(complaintId) : null,
        StringUtils.validString(numReassign) ? String.valueOf(numReassign) : null,
        StringUtils.validString(deferType) ? String.valueOf(deferType) : null,
        StringUtils.validString(estimateTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(estimateTime)
            : null,
        longitude,
        latitude,
        StringUtils.validString(groupSolution) ? String.valueOf(groupSolution) : null,
        cellService,
        concave,
        StringUtils.validString(errorCode) ? String.valueOf(errorCode) : null,
        StringUtils.validString(typeUnit) ? String.valueOf(typeUnit) : null,
        StringUtils.validString(isSendTktu) ? String.valueOf(isSendTktu) : null,
        StringUtils.validString(troubleAssignId) ? String.valueOf(troubleAssignId) : null,
        StringUtils.validString(createUnitIdByCC) ? String.valueOf(createUnitIdByCC) : null,
        StringUtils.validString(complaintTypeId) ? String.valueOf(complaintTypeId) : null,
        StringUtils.validString(complaintParentId) ? String.valueOf(complaintParentId) : null,
        isdn,
        createUserByCC,
        StringUtils.validString(isOverdue) ? String.valueOf(isOverdue) : null,
        lstComplaint,
        downtime,
        woType,
        accountGline,
        customerTimeDesireFrom,
        customerTimeDesireTo,
        customerName,
        ftInfo,
        numAffect,
        subMin,
        StringUtils.validString(warnLevel) ? String.valueOf(warnLevel) : null,
        StringUtils.validString(isFailDevice) ? String.valueOf(isFailDevice) : null,
        country,
        impactName,
        solutionTypeName,
        vendorName,
        warnLevelName,
        cableTypeName,
        transNetworkTypeName,
        transReasonEffectiveName,
        listItAccount,
        usersInsideDtos,
        authorityDTO,
        listTroubleId,
        lstCreateUnitIdByCC,
        lstComplaintTypeId,
        lstComplaintParentId,
        lstTroubleCode,
        arFileName,
        arFileData,
        parentId,
        crDTOS,
        unitId,
        startTime,
        endTime,
        "troubleCode",
        null,
        null,
        null,
        null,
        null,
        odDTOS,
        riskDTOS,
        estimatedTimeToEndFrom,
        estimatedTimeToEndTo
        , typeCode
        , subCategoryCode
        , countryCode
        , impactCode
        , receiveUnitCode
        , message
        , unitApproval
        , resultApproval
        , sendSmsApproval
        , isUnitGnoc
//        ,
//        odDTOS,
//        riskDTOS,
//        estimatedTimeToEndFrom,
//        estimatedTimeToEndTo
    );
    return model;
  }

}
