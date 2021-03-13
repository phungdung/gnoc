package com.viettel.gnoc.risk.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.risk.model.RiskSystemHistoryEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskSystemHistoryDTO extends BaseDto {

  private Long riskSystemHistoryId;
  private Long systemId;
  private Date updateTime;
  private String oldFile;
  private String newFile;
  private Long userId;
  private Long unitId;
  private Long isSendSMSMessage;
  private String content;

  private Double offset;
  private String userName;

  public RiskSystemHistoryDTO(Long riskSystemHistoryId, Long systemId, Date updateTime,
      String oldFile, String newFile, Long userId, Long unitId, Long isSendSMSMessage,
      String content) {
    this.riskSystemHistoryId = riskSystemHistoryId;
    this.systemId = systemId;
    this.updateTime = updateTime;
    this.oldFile = oldFile;
    this.newFile = newFile;
    this.userId = userId;
    this.unitId = unitId;
    this.isSendSMSMessage = isSendSMSMessage;
    this.content = content;
  }

  public RiskSystemHistoryEntity toEntity() {
    return new RiskSystemHistoryEntity(riskSystemHistoryId, systemId, updateTime, oldFile, newFile,
        userId, unitId, isSendSMSMessage, content);
  }

}
