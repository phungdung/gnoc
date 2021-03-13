package com.viettel.gnoc.od.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class OdSearchInsideDTO extends BaseDto {

  //Fields
  private Long odId;
  private String odCode;
  @NotNull(message = "{validation.wo.notify.woName}")
  @Size(max = 750, message = "{validation.od.Content.750}")
  private String odName;
  private Date createTime;
  private String description;
  private Date lastUpdateTime;
  @NotNull(message = "{validation.wo.notify.woType}")
  private Long odTypeId;
  private String odTypeName;
  private String arrTypeId;
  @NotNull(message = "{validation.wo.notify.priority}")
  private Long priorityId;
  private Date startTime;
  private Date endTime;
  private Long receiveUserId;
  private Long receiveUnitId;
  private String otherSystemCode;
  private String planCode;
  private String locationCode;
  private String insertSource;
  private String status;
  private String statusName;
  private String defaultSortField;
  private String userId; // nhan vien thuc hien tim kiem
  private Boolean isCreated; // nhan vien tao
  private Boolean isReceiveUser;// nhan vien xu ly
  private Boolean isReceiveUnit;// nhan vien xu ly
  private String createTimeFrom;
  private String createTimeTo;
  private String startTimeFrom;
  private String startTimeTo;
  private String endTimeFrom;
  private String endTimeTo;
  private String receiveUserName; // ten nhan vien xu ly
  private String receiveUnitName; // ten don vi xu ly
  private String receiveUnitCode; // ten don vi xu ly
  private String priorityName; // ten don vi xu ly
  private String remainTime; // ten don vi xu ly
  private List<String> lstFileName;
  private String fileName;
  private Long createPersonId;
  private List<OdRelationDTO> lstOdRelation; // danh sach lien kết
  private String createPersonName;
  private Long clearCodeId;
  private Long closeCodeId;
  private Date closeTime;
  private String comment;
  private Long oldStatus;
  private Long createUnitId;
  private String childCreateUnit;
  private String childReceiveUnit;
  private Long odGroupTypeId;
  private Date endPendingTime;

  private List<byte[]> lstFileArr;
  private String odTypeCode;
  private Boolean isUnitOfGnoc;
  private String priorityCode;
  private String userName;
  private String password;
  private String signUser;
  private String docTitle;
  private String vofficeTransCode;
  private Long offset;
  private List<OdParamInsideDTO> lstOdParamInside;
  private List<OdParamDTO> lstOdParam;
  private Long woId;
  private String createUnitName;
  private List<String> lstReceiveUnitId;
  private List<GnocFileDto> gnocFileDtos;
  private Long otherSystemId;
  private String otherSystemType;
  Long reasonGroup;
  String reasonDetail;
  Long solutionGroup;
  String solutionDetail;
  Date solutionCompleteTime;
  private List<Long> idDeleteList;

  private Long approverId;
  private String approverName;
  private String createUnitCode;
  private String finishedTime;
  private String reasonPause;
  private String resultApproval;

  public OdDTO toDTO() {
    return new OdDTO(
        odId,
        odCode, odName,
        createTime,
        description,
        lastUpdateTime,
        odTypeId,
        odGroupTypeId,
        priorityId,
        startTime,
        endTime,
        receiveUserId,
        receiveUnitId,
        otherSystemCode,
        this.planCode, this.insertSource,
        !StringUtils.validString(this.status) ? null : Long.parseLong(status),
        createPersonId,
        closeTime,
        closeCodeId,
        clearCodeId,
        endPendingTime,
        null, vofficeTransCode,
        woId, userName,
        fileName, lstOdRelation,
        lstReceiveUnitId,
        gnocFileDtos,
        otherSystemId,
        otherSystemType,
        this.lstFileName,
        createPersonName,
        oldStatus,
        reasonGroup,
        reasonDetail,
        solutionGroup,
        solutionDetail,
        solutionCompleteTime,
        createUnitId,
        approverId,
        finishedTime,
        reasonPause,
        resultApproval
    );
  }

  public OdDTOSearch toOdSearchDTO() {
    OdDTOSearch model = new OdDTOSearch(
        StringUtils.validString(odId) ? String.valueOf(odId) : null,
        odCode,
        odName,
        StringUtils.validString(createTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(createTime)
            : null,
        description,
        StringUtils.validString(lastUpdateTime) ? DateTimeUtils
            .date2ddMMyyyyHHMMss(lastUpdateTime) : null,
        StringUtils.validString(odTypeId) ? String.valueOf(odTypeId) : null,
        odTypeName,
        arrTypeId,
        StringUtils.validString(priorityId) ? String.valueOf(priorityId) : null,
        StringUtils.validString(startTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(startTime) : null,
        StringUtils.validString(endTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(endTime) : null,
        StringUtils.validString(receiveUserId) ? String.valueOf(receiveUserId) : null,
        StringUtils.validString(receiveUnitId) ? String.valueOf(receiveUnitId) : null,
        otherSystemCode,
        planCode,
        locationCode,
        insertSource,
        status,
        statusName,
        defaultSortField,
        userId,
        isCreated,
        isReceiveUser,
        isReceiveUnit,
        createTimeFrom,
        createTimeTo,
        startTimeFrom,
        startTimeTo,
        endTimeFrom,
        endTimeTo,
        receiveUserName,
        receiveUnitName,
        receiveUnitCode,
        priorityName,
        remainTime,
        lstFileName,
        fileName,
        StringUtils.validString(createPersonId) ? String.valueOf(createPersonId) : null,
        lstOdRelation,
        createPersonName,
        StringUtils.validString(clearCodeId) ? String.valueOf(clearCodeId) : null,
        StringUtils.validString(closeCodeId) ? String.valueOf(closeCodeId) : null,
        StringUtils.validString(closeTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(closeTime) : null,
        comment,
        StringUtils.validString(oldStatus) ? String.valueOf(oldStatus) : null,
        StringUtils.validString(createUnitId) ? String.valueOf(createUnitId) : null,
        childCreateUnit,
        childReceiveUnit,
        StringUtils.validString(odGroupTypeId) ? String.valueOf(odGroupTypeId) : null,
        StringUtils.validString(endPendingTime) ? DateTimeUtils
            .date2ddMMyyyyHHMMss(endPendingTime) : null,
        lstFileArr,
        odTypeCode,
        isUnitOfGnoc,
        priorityCode,
        userName,
        password,
        signUser,
        docTitle,
        vofficeTransCode,
        offset,
        lstOdParamInside,
        lstOdParam,
        StringUtils.validString(woId) ? String.valueOf(woId) : null,
        createUnitName,
        lstReceiveUnitId,
        gnocFileDtos,
        otherSystemId,
        otherSystemType,
        reasonGroup,
        reasonDetail,
        solutionGroup,
        solutionDetail,
        StringUtils.validString(solutionCompleteTime) ? DateTimeUtils
            .date2ddMMyyyyHHMMss(solutionCompleteTime) : null,
        approverId,
        approverName,
        createUnitCode,
        finishedTime,
        reasonPause,
        resultApproval
    );
    return model;
  }
}
