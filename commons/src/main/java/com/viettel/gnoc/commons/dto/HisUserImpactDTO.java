package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.HisUserImpactEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HisUserImpactDTO  extends  BaseDto{

  //thanhlv12 add 25-09-2020
  private Long hisId;
  private String userId;
  private String type;
  private String actionType;
  private String newObject;
  private Date createTime;
  private String result;

  private String userName;
  private String createTimeDate;

  public HisUserImpactDTO(Long hisId, String userId, String type, String actionType, String newObject,
          Date createTime, String result) {
    this.hisId = hisId;
    this.userId = userId;
    this.type = type;
    this.actionType = actionType;
    this.newObject = newObject;
    this.createTime = createTime;
    this.result = result;
  }

  public HisUserImpactEntity toEntity() {
    return new HisUserImpactEntity(hisId, userId, type, actionType, newObject, createTime, result);
  }
}
