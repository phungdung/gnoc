package com.viettel.gnoc.risk.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.risk.model.RiskHistoryEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskHistoryDTO extends BaseDto {

  private Long riskHisId;
  private Long riskId;
  private Date updateTime;
  private Long oldStatus;
  private Long newStatus;
  private Long userId;
  private Long unitId;
  private String content;
  private Long isSendMesssage;
  private String userName;
  private String inforMation;

  private Double offset;
  private String oldStatusName;
  private String newStatusName;
  private String bussiness;
  private String system;
  private String language;

  public RiskHistoryDTO(Long riskHisId, Long riskId, Date updateTime, Long oldStatus,
      Long newStatus, Long userId, Long unitId, String content, Long isSendMesssage,
      String userName, String inforMation) {
    this.riskHisId = riskHisId;
    this.riskId = riskId;
    this.updateTime = updateTime;
    this.oldStatus = oldStatus;
    this.newStatus = newStatus;
    this.userId = userId;
    this.unitId = unitId;
    this.content = content;
    this.isSendMesssage = isSendMesssage;
    this.userName = userName;
    this.inforMation = inforMation;
  }

  public RiskHistoryEntity toEntity() {
    return new RiskHistoryEntity(riskHisId, riskId, updateTime, oldStatus, newStatus, userId,
        unitId, content, isSendMesssage, userName, inforMation);
  }

}
