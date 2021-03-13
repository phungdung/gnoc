/**
 * @(#)OdForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.od.dto;

import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ITSOL
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class OdDTOSearch {

  //Fields
  private String odId;
  private String odCode;
  private String odName;
  private String createTime;
  private String description;
  private String lastUpdateTime;
  private String odTypeId;
  private String odTypeName;
  private String arrTypeId;
  private String priorityId;
  private String startTime;
  private String endTime;
  private String receiveUserId;
  private String receiveUnitId;
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
  private String createPersonId;
  private List<OdRelationDTO> lstOdRelation; // danh sach lien kết
  private String createPersonName;
  private String clearCodeId;
  private String closeCodeId;
  private String closeTime;
  private String comment;
  private String oldStatus;
  private String createUnitId;
  private String childCreateUnit;
  private String childReceiveUnit;
  private String odGroupTypeId;
  private String endPendingTime;

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

  //phatnt
  private List<OdParamInsideDTO> lstOdParamInside;
  private List<OdParamDTO> lstOdParam;
  private String woId;

  private String createUnitName;
  private List<String> lstReceiveUnitId;
  private List<GnocFileDto> gnocFileDtos;
  private Long otherSystemId;
  private String otherSystemType;
  private Long reasonGroup;
  private String reasonDetail;
  private Long solutionGroup;
  private String solutionDetail;
  private String solutionCompleteTime;



  private Long approverId;
  private String approverName;
  private String createUnitCode;
  private String finishedTime;
  private String reasonPause;
  private String resultApproval;

  public OdSearchInsideDTO toOdSearchInsideDTO() {
    OdSearchInsideDTO model = new OdSearchInsideDTO(
        StringUtils.validString(odId) ? Long.valueOf(odId) : null,
        odCode,
        odName,
        StringUtils.validString(createTime) ? DateTimeUtils.convertStringToDate(createTime)
            : null,
        description,
        StringUtils.validString(lastUpdateTime) ? DateTimeUtils
            .convertStringToDate(lastUpdateTime) : null,
        StringUtils.validString(odTypeId) ? Long.valueOf(odTypeId) : null,
        odTypeName,
        arrTypeId,
        StringUtils.validString(priorityId) ? Long.valueOf(priorityId) : null,
        StringUtils.validString(startTime) ? DateTimeUtils.convertStringToDate(startTime) : null,
        StringUtils.validString(endTime) ? DateTimeUtils.convertStringToDate(endTime) : null,
        StringUtils.validString(receiveUserId) ? Long.valueOf(receiveUserId) : null,
        StringUtils.validString(receiveUnitId) ? Long.valueOf(receiveUnitId) : null,
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
        StringUtils.validString(createPersonId) ? Long.valueOf(createPersonId) : null,
        lstOdRelation,
        createPersonName,
        StringUtils.validString(clearCodeId) ? Long.valueOf(clearCodeId) : null,
        StringUtils.validString(closeCodeId) ? Long.valueOf(closeCodeId) : null,
        StringUtils.validString(closeTime) ? DateTimeUtils.convertStringToDate(closeTime) : null,
        comment,
        StringUtils.validString(oldStatus) ? Long.valueOf(oldStatus) : null,
        StringUtils.validString(createUnitId) ? Long.valueOf(createUnitId) : null,
        childCreateUnit,
        childReceiveUnit,
        StringUtils.validString(odGroupTypeId) ? Long.valueOf(odGroupTypeId) : null,
        StringUtils.validString(endPendingTime) ? DateTimeUtils
            .convertStringToDate(endPendingTime) : null,
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
        StringUtils.validString(woId) ? Long.valueOf(woId) : null,
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
            .convertStringToDate(solutionCompleteTime) : null,
        null,
        approverId,
        approverName,
        createUnitCode,
        finishedTime,
        reasonPause,
        resultApproval
    );
    return model;
  }

  public OdDTOSearch(String odTypeId, String locationCode) {
    this.odTypeId = odTypeId;
    this.locationCode = locationCode;
  }
}
