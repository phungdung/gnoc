package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import com.viettel.gnoc.od.dto.OdDTO;
import com.viettel.gnoc.risk.dto.RiskDTO;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author itsol
 */
@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class TroublesDTO {

  //Fields
  private String troubleId;
  private String troubleCode;
  private String troubleName;
  private String description;
  private String typeId;
  private String subCategoryId;
  private String priorityId;
  private String impactId;
  private String state;
  private String affectedNode;
  private String affectedService;
  private String location;
  private String locationCode;
  private String locationId;
  private String createdTime;
  private String lastUpdateTime;
  private String assignTime;
  private String beginTroubleTime;
  private String endTroubleTime;
  private String deferredTime;
  private String createUserId;
  private String createUnitId;
  private String createUserName;
  private String createUnitName;
  private String insertSource;
  private String relatedPt;
  private String vendorId;
  private String relatedKedb;
  private String receiveUnitId;
  private String responeseUnitId;
  private String receiveUserId;
  private String receiveUnitName;
  private String receiveUserName;
  private String rootCause;
  private String workArround;
  private String solutionType;
  private String workLog;
  private String rejectedCode;
  private String rejectReason;
  private String risk;
  private String supportUnitId;
  private String queueTime;
  private String clearTime;
  private String closedTime;
  private String reasonOverdue;
  private String timeZoneCreate;
  private String changedTime;
  private String priorityName;
  private String priorityCode;
  private String stateName;
  private String typeName;
  private String riskName;
  private String timeUsed;
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
  private String closeCode;
  private String reasonId;
  private String attachFileList;
  private String assignTimeTemp;
  private String timeProcess;
  private String supportUnitName;
  private String reasonName;
  private String timeCreateCfg;
  private String pathFileAttach;
  private String tblCurr;
  private String tblHis;
  private String tblClear;
  private String alarmId;
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
  private String closeTtTime;
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
  private String reOccur; //HaiNV20 bo sung so lan xuat lai
  private String ccGroupId;
  private String checkbox;
  private String relatedTt;
  private String isMove;
  private String dateMove;
  private String unitMove;
  private String unitMoveName;
  private String isUpdateAfterClosed;
  private String isStationVip;
  private String isStationVipName;
  private String isTickHelp;
  private String numHelp;
  private String countReopen;

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
  private String numPending;
  private String CdId;
  private String isCheck;
  private String customerPhone;
  private String linkCode;//ma tuyen do noc day sang cho WO
  private String linkId;
  private String amiId;
  // phuc vu WO luu du thua
  private String woContent;
  private List<byte[]> fileDocumentByteArray;
  private List<String> arrFileName;
  List<String> listAccount;
  private String numAon;
  private String numGpon;
  private String numNexttv;
  private String numThc;
  private String technology;    //cong nghe
  private String infoTicket;  //thong tin su co
  private String reasonExist;
  private String catchingTime; // thoi gian bat duoc canh bao
  private String cameraId;
  private String relatedCr;
  private String nationCode;
  private String isChat;
  private String complaintId;
  private String numReassign;
  private String deferType;//loai tam dong
  private String estimateTime;//thoi gian du kien
  private String longitude;//kinh do
  private String latitude;//vi do
  private String groupSolution; //nhom giai phap
  private String cellService;//cell phuc vu
  private String concave; //vong lom
  private String errorCode;
  private String typeUnit;
  private String isSendTktu;
  private String troubleAssignId;
  //luu cho cc
  private String createUnitIdByCC;
  private String complaintTypeId;
  private String complaintParentId;
  private String isdn;
  private String createUserByCC;
  private String isOverdue;
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
  private String warnLevel;
  private String isFailDevice;
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
  private List<CrDTO> crDTOS;
  private String unitId;
  private String startTime;
  private String endTime;
  private String defaultSortField;

  private List<String> troubleCodes;
  private List<String> amiIds;
  private String status;
  private List<String> listState;
  private String userLogin;
  private List<OdDTO> odDTOS;
  private List<RiskDTO> riskDTOS;
  // thang dt add
  private String estimatedTimeToEndFrom;
  private String estimatedTimeToEndTo;

  //hungtv add
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

  //Constructor
  public TroublesDTO() {
    this.defaultSortField = "troubleCode";
    // This constructor is intentionally empty. Nothing special is needed here.
  }

  public TroublesInSideDTO toModelInSide() {
    TroublesInSideDTO model = new TroublesInSideDTO(
        StringUtils.validString(troubleId) ? Long.valueOf(troubleId) : null,
        troubleCode,
        troubleName,
        description,
        StringUtils.validString(typeId) ? Long.valueOf(typeId) : null,
        StringUtils.validString(subCategoryId) ? Long.valueOf(subCategoryId) : null,
        StringUtils.validString(priorityId) ? Long.valueOf(priorityId) : null,
        StringUtils.validString(impactId) ? Long.valueOf(impactId) : null,
        StringUtils.validString(state) ? Long.valueOf(state) : null,
        affectedNode,
        affectedService,
        location,
        locationCode,
        StringUtils.validString(locationId) ? Long.valueOf(locationId) : null,
        StringUtils.validString(createdTime) ? DateTimeUtils.convertStringToDate(createdTime)
            : null,
        StringUtils.validString(lastUpdateTime) ? DateTimeUtils
            .convertStringToDate(lastUpdateTime) : null,
        StringUtils.validString(assignTime) ? DateTimeUtils.convertStringToDate(assignTime)
            : null,
        StringUtils.validString(beginTroubleTime) ? DateTimeUtils
            .convertStringToDate(beginTroubleTime) : null,
        StringUtils.validString(endTroubleTime) ? DateTimeUtils
            .convertStringToDate(endTroubleTime) : null,
        StringUtils.validString(deferredTime) ? DateTimeUtils.convertStringToDate(deferredTime)
            : null,
        StringUtils.validString(createUserId) ? Long.valueOf(createUserId) : null,
        StringUtils.validString(createUnitId) ? Long.valueOf(createUnitId) : null,
        createUserName,
        createUnitName,
        insertSource,
        relatedPt,
        StringUtils.validString(vendorId) ? Long.valueOf(vendorId) : null,
        relatedKedb,
        StringUtils.validString(receiveUnitId) ? Long.valueOf(receiveUnitId) : null,
        responeseUnitId,
        StringUtils.validString(receiveUserId) ? Long.valueOf(receiveUserId) : null,
        receiveUnitName,
        receiveUserName,
        rootCause,
        workArround,
        StringUtils.validString(solutionType) ? Long.valueOf(solutionType) : null,
        workLog,
        StringUtils.validString(rejectedCode) ? Long.valueOf(rejectedCode) : null,
        rejectReason,
        StringUtils.validString(risk) ? Long.valueOf(risk) : null,

        StringUtils.validString(supportUnitId) ? Long.valueOf(supportUnitId) : null,
        StringUtils.validString(queueTime) ? DateTimeUtils.convertStringToDate(queueTime) : null,
        StringUtils.validString(clearTime) ? DateTimeUtils.convertStringToDate(clearTime) : null,
        StringUtils.validString(closedTime) ? DateTimeUtils.convertStringToDate(closedTime)
            : null,
        reasonOverdue,
        StringUtils.validString(timeZoneCreate) ? Long.valueOf(timeZoneCreate) : null,
        changedTime,
        priorityName,
        priorityCode,
        stateName,
        typeName,
        riskName,
        StringUtils.validString(timeUsed) ? Double.valueOf(timeUsed) : null,
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
        StringUtils.validString(closeCode) ? Long.valueOf(closeCode) : null,
        StringUtils.validString(reasonId) ? Long.valueOf(reasonId) : null,
        attachFileList,
        StringUtils.validString(assignTimeTemp) ? DateTimeUtils
            .convertStringToDate(assignTimeTemp) : null,
        StringUtils.validString(timeProcess) ? Double.valueOf(timeProcess) : null,
        supportUnitName,
        reasonName,
        StringUtils.validString(timeCreateCfg) ? Double.valueOf(timeCreateCfg) : null,
        pathFileAttach,
        tblCurr,
        tblHis,
        tblClear,
        StringUtils.validString(alarmId) ? Long.valueOf(alarmId) : null,
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
        StringUtils.validString(closeTtTime) ? Double.valueOf(closeTtTime) : null,
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
        StringUtils.validString(reOccur) ? Long.valueOf(reOccur) : null,
        ccGroupId,
        checkbox,
        relatedTt,
        StringUtils.validString(isMove) ? Long.valueOf(isMove) : null,
        StringUtils.validString(dateMove) ? DateTimeUtils.convertStringToDate(dateMove) : null,
        StringUtils.validString(unitMove) ? Long.valueOf(unitMove) : null,
        unitMoveName,
        isUpdateAfterClosed,
        StringUtils.validString(isStationVip) ? Long.valueOf(isStationVip) : null,
        isStationVipName,
        StringUtils.validString(isTickHelp) ? Long.valueOf(isTickHelp) : null,
        StringUtils.validString(numHelp) ? Long.valueOf(numHelp) : null,
        StringUtils.validString(countReopen) ? Long.valueOf(countReopen) : null,
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
        StringUtils.validString(numPending) ? Long.valueOf(numPending) : null,
        CdId,
        isCheck,
        customerPhone,
        linkCode,
        linkId,
        StringUtils.validString(amiId) ? Long.valueOf(amiId) : null,
        woContent,
        fileDocumentByteArray,
        arrFileName,
        listAccount,
        StringUtils.validString(numAon) ? Long.valueOf(numAon) : null,
        StringUtils.validString(numGpon) ? Long.valueOf(numGpon) : null,
        StringUtils.validString(numNexttv) ? Long.valueOf(numNexttv) : null,
        StringUtils.validString(numThc) ? Long.valueOf(numThc) : null,
        technology,
        infoTicket,
        reasonExist,
        StringUtils.validString(catchingTime) ? DateTimeUtils.convertStringToDate(catchingTime)
            : null,
        cameraId,
        relatedCr,
        nationCode,
        StringUtils.validString(isChat) ? Long.valueOf(isChat) : null,
        StringUtils.validString(complaintId) ? Long.valueOf(complaintId) : null,
        StringUtils.validString(numReassign) ? Long.valueOf(numReassign) : null,
        StringUtils.validString(deferType) ? Long.valueOf(deferType) : null,
        StringUtils.validString(estimateTime) ? DateTimeUtils.convertStringToDate(estimateTime)
            : null,
        longitude,
        latitude,
        StringUtils.validString(groupSolution) ? Long.valueOf(groupSolution) : null,
        cellService,
        concave,
        StringUtils.validString(errorCode) ? Long.valueOf(errorCode) : null,
        StringUtils.validString(typeUnit) ? Long.valueOf(typeUnit) : null,
        "TKTU".equalsIgnoreCase(isSendTktu) ? Long.valueOf(Constants.TROUBLE.isTKTU) : null,
        StringUtils.validString(troubleAssignId) ? Long.valueOf(troubleAssignId) : null,
        StringUtils.validString(createUnitIdByCC) ? Long.valueOf(createUnitIdByCC) : null,
        StringUtils.validString(complaintTypeId) ? Long.valueOf(complaintTypeId) : null,
        StringUtils.validString(complaintParentId) ? Long.valueOf(complaintParentId) : null,
        isdn,
        createUserByCC,
        StringUtils.validString(isOverdue) ? Long.valueOf(isOverdue) : null,
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
        StringUtils.validString(warnLevel) ? Long.valueOf(warnLevel) : null,
        StringUtils.validString(isFailDevice) ? Long.valueOf(isFailDevice) : null,
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
        0,
        crDTOS,
        unitId,
        startTime,
        endTime,
        null,
        null,
        null,
        odDTOS,
        riskDTOS,
        estimatedTimeToEndFrom,
        estimatedTimeToEndTo
        ,typeCode
        , subCategoryCode
        , countryCode
        , impactCode
        , receiveUnitCode
        , message
        , unitApproval
        , resultApproval
        , sendSmsApproval
        , isUnitGnoc
    );
    return model;
  }


}
