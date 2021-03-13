package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.model.WorkLogEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkLogDTO {

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
  private String wlgObjectState;
  private String defaultSortField;

  public WorkLogDTO(Long workLogId, Long wlgObjectType, Long wlgObjectId, Long userId,
      Long userGroupAction, String wlgText, Long wlgEffortHours, Long wlgEffortMinutes,
      Long wlgAccessType, Date createdDate,
      Long wlayId, Long wlgObjectState) {
    this.workLogId = workLogId == null ? null : String.valueOf(workLogId);
    this.wlgObjectType = wlgObjectType == null ? null : String.valueOf(wlgObjectType);
    this.wlgObjectId = wlgObjectId == null ? null : String.valueOf(wlgObjectId);
    this.userId = userId == null ? null : String.valueOf(userId);
    this.userGroupAction = userGroupAction == null ? null : String.valueOf(userGroupAction);
    this.wlgText = wlgText;
    this.wlgEffortHours = wlgEffortHours == null ? null : String.valueOf(wlgEffortHours);
    this.wlgEffortMinutes = wlgEffortMinutes == null ? null : String.valueOf(wlgEffortMinutes);
    this.wlgAccessType = wlgAccessType == null ? null : String.valueOf(wlgAccessType);
    this.createdDate = DateTimeUtils.date2ddMMyyyyHHMMss(createdDate);
    this.wlayId = wlayId == null ? null : String.valueOf(wlayId);
    this.wlgObjectState = wlgObjectState == null ? null : String.valueOf(wlgObjectState);
  }

  public WorkLogEntity toEntity() {
    WorkLogEntity model = new WorkLogEntity(
        workLogId == null ? null : Long.valueOf(workLogId),
        wlgObjectType == null ? null : Long.valueOf(wlgObjectType),
        wlgObjectId == null ? null : Long.valueOf(wlgObjectId),
        userId == null ? null : Long.valueOf(userId),
        userGroupAction == null ? null : Long.valueOf(userGroupAction),
        wlgText,
        wlgEffortHours == null ? null : Long.valueOf(wlgEffortHours),
        wlgEffortMinutes == null ? null : Long.valueOf(wlgEffortMinutes),
        wlgAccessType == null ? null : Long.valueOf(wlgAccessType),
        StringUtils.validString(createdDate) ? DateUtil
            .string2DateByPattern(createdDate, "dd/MM/yyyy HH:mm:ss") : null,
        wlayId == null ? null : Long.valueOf(wlayId),
        wlgObjectState == null ? null : Long.valueOf(wlgObjectState)
    );
    return model;
  }

  public WorkLogInsiteDTO toInsideDTO() {
    return new WorkLogInsiteDTO(
        StringUtils.isStringNullOrEmpty(workLogId) ? null : Long.valueOf(workLogId),
        StringUtils.isStringNullOrEmpty(wlgObjectType) ? null : Long.valueOf(wlgObjectType),
        StringUtils.isStringNullOrEmpty(wlgObjectId) ? null : Long.valueOf(wlgObjectId),
        StringUtils.isStringNullOrEmpty(userId) ? null : Long.valueOf(userId),
        StringUtils.isStringNullOrEmpty(userGroupAction) ? null : Long.valueOf(userGroupAction),
        wlgText,
        StringUtils.isStringNullOrEmpty(wlgEffortHours) ? null : Long.valueOf(wlgEffortHours),
        StringUtils.isStringNullOrEmpty(wlgEffortMinutes) ? null : Long.valueOf(wlgEffortMinutes),
        StringUtils.isStringNullOrEmpty(wlgAccessType) ? null : Long.valueOf(wlgAccessType),
        DateTimeUtils.convertStringToDate(createdDate),
        StringUtils.isStringNullOrEmpty(wlayId) ? null : Long.valueOf(wlayId),
        StringUtils.isStringNullOrEmpty(wlgObjectState) ? null : Long.valueOf(wlgObjectState),
        userGroupName,
        userGroupActionName
    );

  }
}
