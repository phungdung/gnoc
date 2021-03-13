package com.viettel.gnoc.wfm.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WoSearchDTO extends BaseDto {

  private String woId;
  private String parentId;
  private String parentName;
  private String woCode;
  private String woContent;
  private String woSystem;
  private String woSystemId;
  private String woSystemOutId;
  private String createPersonId;
  private String createPersonName;
  private Date createDate;
  private String woTypeId;
  private String woTypeName;
  private String cdId;
  private String cdName;
  private String ftId;
  private String ftName;
  private String status;
  private String statusName;
  private String priorityId;
  private String priorityName;
  private String startTime;
  private String startTimeFrom;
  private String startDateFrom;
  private String startTimeTo;
  private String startDateTo;
  private String endTime;
  private String endTimeFrom;
  private String endTimeTo;
  private String finishTime;
  private String result;
  private String stationId;
  private String stationCode;
  private String lastUpdateTime;
  private String fileName;
  private String comments;
  private static long changedTime = 0;

  private String userId;
  private String createPeronEmail;
  private String createPersonMobile;
  private String woGroupName;
  private String woGroupEmail;
  private String woGroupMobile;
  private String woDescription;
  private List<ResultInSideDto> listFileName;

  private Boolean isCreated;
  private Boolean isCd;
  private Boolean isFt;
  private String createUnitId;
  private String processUnitId;
  private Boolean isContainChildUnit;

  private String accountIsdn;
  private String serviceId;
  private String infraType;
  private String ccServiceId;
  private String ccGroupId;
  private String isCcResult;
  private String ccResult;
  private String checkQosInternetResult;
  private String checkQosTHResult;
  private String checkQrCode;
  //ThanhLV12_start_bo xung ma loai cong viec
  private String woTypeCode;
  //ThanhLV12_end_bo xung ma loai cong viec
  //ThanhLV12_tam dong _start
  private String endPendingTime;
  private String reasonName;
  private String reasonId;
  private String customer;
  private String phone;

  private String reasonOverdueLV1Id;
  private String reasonOverdueLV1Name;
  private String reasonOverdueLV2Id;
  private String reasonOverdueLV2Name;

  private String completedTime;
  private String commentComplete;

  private String remainTime;
  private String ratioCall;
  private String searchOverDue;

  private String lineCode;
  private String isCompletedOnVsmart;
  private Long isCall;
  private String warehouseCode;
  private String constructionCode;
  //20160810 daitt1 start
  private Boolean needSupport;
  private String isNeedSupport;
  //20160810 daitt1 start
  //R574473_start
  private String isValidWoTrouble;
  private String solutionGroupId;
  private String solutionGroupName;
  private String solution;
  private String reasonTroubleId;
  private String reasonTroubleName;
  private String clearTime;
  private String cdAssignId;
  //R574473_end

  private String kedbCode;
  private String kedbId;
  private String contractId;
  private String processActionId;
  private String deltaCloseWo;
  private String confirmNotCreateAlarm;

  private List<String> cdIdList;

  private String createUnitName;// ten don vi tao
  private String ableMop;
  private String planCode;
  private String numSupport;//Id he thong
  private Long offset;
  private String completeTimeFrom;
  private String completeTimeTo;

  private WoDTOSearch woDTOSearch;
  private String crId;
  private String crCreateDate;

  public Boolean isIsCreated() {
    return isCreated;
  }

  public Boolean isIsCd() {
    return isCd;
  }

  public Boolean isIsFt() {
    return isFt;
  }

}
