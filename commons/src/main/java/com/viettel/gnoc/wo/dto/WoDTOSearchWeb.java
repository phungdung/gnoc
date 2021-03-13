/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.dto;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author KienPV
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WoDTOSearchWeb {

  //Fields
  private Long woId;
  private Long parentId;
  private String parentName;
  private String woCode;
  private String woContent;
  private String woSystem;
  private String woSystemId;
  private Long woSystemOutId;
  private Long createPersonId;
  private String createPersonName;
  private Date createDate;
  private Long woTypeId;
  private String woTypeName;
  private Long cdId;
  private String cdName;
  private Long ftId;
  private String ftName;
  private Long status;
  private String statusName;
  private Long priorityId;
  private String priorityName;
  private Date startTime;
  private Date startTimeFrom;
  private String startTimeTo;
  private String startDateFrom;
  private String startDateTo;
  private Date endTime;
  private Date endTimeFrom;
  private Date endTimeTo;
  private Date finishTime;
  private Long result;
  private String resultName;
  private Long stationId;
  private String stationCode;
  private Date lastUpdateTime;
  private String fileName;
  private String comments;
  private Long userId;
  private String defaultSortField;
  private Boolean isCreated;
  private Boolean isCd;
  private Boolean isFt;
  private Long createUnitId;
  private Long processUnitId;
  private Boolean isContainChildUnit;
  private String accountIsdn;
  private Long serviceId;
  private String infraType;
  private Long ccServiceId;
  private Long ccGroupId;
  private String isCcResult;
  private String ccResult;
  private String checkQosInternetResult;
  private String checkQosTHResult;
  private String checkQrCode;
  //R13099_new_namndh_18022016_start
  private String woWorklog;
  private Date completeTimeFrom;
  private Date completeTimeTo;
  //ThanhLV12_start
  private String numPending;
  private Date endPendingTime;
  private String reasonOverdueLV1Id;
  private String reasonOverdueLV1Name;
  private String reasonOverdueLV2Id;
  private String reasonOverdueLV2Name;

  private Date completedTime;
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
  private Long isNeedSupport;
  //20160810 daitt1 start

  //R574473_start
  private String isValidWoTrouble;
  private Long solutionGroupId;
  private String solutionGroupName;
  private String solution;
  private Long reasonTroubleId;
  private String reasonTroubleName;
  private Date clearTime;
  private Long cdAssignId;
  //R574473_end

  private String kedbCode;
  private Long kedbId;
  private Long contractId;
  private Long processActionId;
  private Long scripId;
  private String scriptName;
  private Double polesDistance;

  private Double deltaCloseWo;
  private Long confirmNotCreateAlarm;
  private List<String> cdIdList;

  private String closuresReplace; // mang xong thay the
  private String lineCutCode;// ma tuyen dut
  private String codeSnippetOff;// ma doan dut

  private String createUnitName;// ten don vi tao
  private String accountUplink;         // accountUplink
  private String ableMop;                 // co cho phep tac dong Mop
  private String select;
  private String planCode; //ma ke hoach
  private String isEnable; //ma ke hoach

  private String numSupport;//Id he thong
  private List<String> lstStationCode;//danh sach tram

  private Date ftAcceptedTime;
  private Long woTypeGroupId;

  private Long reasonInterference;
  private String woDescription;

  private String deviceType;
  private String deviceTypeName;
  private String failureReason;
  private String newFailureReason;

  private String hasCost;
  private Long callToCd;

  private String hasWorklog;
  private Boolean showMaterialInfo;
  private Boolean isShowReasonInfo;
  private Boolean isShowReasonOverdueInfo;
  private Boolean checkRefresh;

}
