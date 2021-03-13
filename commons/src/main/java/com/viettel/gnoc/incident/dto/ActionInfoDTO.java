package com.viettel.gnoc.incident.dto;

import com.google.gson.Gson;
import com.viettel.gnoc.commons.utils.DateUtil;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class ActionInfoDTO {

  private String evaluateGnoc;
  private String totalPendingTimeGnoc;
  private String totalProcessTimeGnoc;
  private String reasonLv3Id;
  private String reasonLv3Name;
  private String reasonLv2Id;
  private String reasonLv2Name;
  private String reasonLv1Id;
  private String reasonLv1Name;
  private String isCheck;
  private String stateWo;
  private String troubleCode;
  private String numPending;
  private String receiveUnitId;
  private String receiveUnitName;
  private String receiveUserId;
  private String receiveUserName;
  private String ftInfo;
  private String deferredReason;
  private String deferredTime;
  private String reasonOverdueId;
  private String reasonOverdueName;
  private String reasonOverdueId2;
  private String reasonOverdueName2;
  private String rootCause;
  private String workArround;
  private String stateName;
  private String processingUserPhone;
  private String timeFrom;
  private String timeTo;
  private String insertSource;
  private String updateTime;
  private String woCode;
  private String workLogs;
  private String state;

  public ActionInfoDTO(TroublesDTO o) {
    this.troubleCode = o.getTroubleCode();
    this.updateTime = DateUtil.date2ddMMyyyyHHMMss(new Date());
    this.workLogs = o.getWorkLog();
    this.state = o.getState();
  }

  public String toString() {
    return new Gson().toJson(this);
  }

}
