/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.gnoc.maintenance.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class WorkLogResultDTO {

  private String workLogId;
  private String wlgObjectType;
  private String wlgObjectId;
  private String userId;
  private String userGroupAction;
  private String wlgText;
  private String wlgEffortHours;
  private String wlgEffortMinutes;
  private String wlgAccessType;
  private String createdDate;
  private String wlayId;
  private String userName;
  private String userGroupName;
  private String userGroupActionName;
  //private static long changedTime = 0;

  public WorkLogResultDTO(String workLogId, String wlgObjectType, String wlgObjectId, String userId,
      String userGroupAction, String wlgText, String wlgEffortHours, String wlgEffortMinutes,
      String wlgAccessType, String createdDate, String wlayId, String userName,
      String userGroupName, String userGroupActionName) {
    this.workLogId = workLogId;
    this.wlgObjectType = wlgObjectType;
    this.wlgObjectId = wlgObjectId;
    this.userId = userId;
    this.userGroupAction = userGroupAction;
    this.wlgText = wlgText;
    this.wlgEffortHours = wlgEffortHours;
    this.wlgEffortMinutes = wlgEffortMinutes;
    this.wlgAccessType = wlgAccessType;
    this.createdDate = createdDate;
    this.wlayId = wlayId;
    this.userName = userName;
    this.userGroupName = userGroupName;
    this.userGroupActionName = userGroupActionName;
  }

}
