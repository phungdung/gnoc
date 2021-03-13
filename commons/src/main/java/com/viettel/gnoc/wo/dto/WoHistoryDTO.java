package com.viettel.gnoc.wo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WoHistoryDTO {

  private String woHistoryId;
  private String oldStatus;
  private String newStatus;
  private String woId;
  private String woCode;
  private String woContent;
  private String userId;
  private String userName;
  private String fileName;
  private String comments;
  private String updateTime;

  private String createPersonId;
  private String cdId;
  private String ftId;
  private String createMessage;
  private String defaultSortField;
  private String isSendIbm;
  private String nation;

  public WoHistoryDTO(String woHistoryId, String oldStatus, String newStatus, String woId,
      String woCode, String woContent, String userId, String userName, String fileName,
      String comments, String updateTime, String createMessage, String createPersonId, String cdId,
      String ftId, String isSendIbm, String nation) {
    this.woHistoryId = woHistoryId;
    this.oldStatus = oldStatus;
    this.newStatus = newStatus;
    this.woId = woId;
    this.woCode = woCode;
    this.woContent = woContent;
    this.userId = userId;
    this.userName = userName;
    this.fileName = fileName;
    this.comments = comments;
    this.updateTime = updateTime;
    this.createMessage = createMessage;
    this.createPersonId = createPersonId;
    this.cdId = cdId;
    this.ftId = ftId;
    this.isSendIbm = isSendIbm;
    this.nation = nation;
  }
}
