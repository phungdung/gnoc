package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.WorkLogEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkLogInsiteDTO extends BaseDto {

  private Long workLogId;
  private Long wlgObjectType;
  private Long wlgObjectId;
  private Long userId;
  private Long userGroupAction;
  private String wlgText;
  private Long wlgEffortHours;
  private Long wlgEffortMinutes;
  private Long wlgAccessType;
  private Date createdDate;
  private Long wlayId;
  private Long wlgObjectState;
  private String userName;
  private String userGroupActionName;
  private Double offset;

  public WorkLogInsiteDTO(Long workLogId, Long wlgObjectType, Long wlgObjectId, Long userId,
      Long userGroupAction, String wlgText, Long wlgEffortHours, Long wlgEffortMinutes,
      Long wlgAccessType, Date createdDate, Long wlayId, Long wlgObjectState,
      String userName, String userGroupActionName) {
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
    this.wlgObjectState = wlgObjectState;
    this.userName = userName;
    this.userGroupActionName = userGroupActionName;
  }

  public WorkLogEntity toEntity() {
    return new WorkLogEntity(workLogId,
        wlgObjectType,
        wlgObjectId,
        userId,
        userGroupAction,
        wlgText,
        wlgEffortHours,
        wlgEffortMinutes,
        wlgAccessType,
        createdDate,
        wlayId,
        wlgObjectState);
  }

  public WorkLogDTO toServiceDTO() {
    WorkLogDTO workLogDTO = new WorkLogDTO(
        workLogId, wlgObjectType, wlgObjectId, userId, userGroupAction, wlgText, wlgEffortHours,
        wlgEffortMinutes,
        wlgAccessType, createdDate, wlayId, wlgObjectState);
    return workLogDTO;
  }

}
