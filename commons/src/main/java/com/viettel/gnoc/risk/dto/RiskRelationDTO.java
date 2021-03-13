package com.viettel.gnoc.risk.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.risk.model.RiskRelationEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskRelationDTO extends BaseDto {

  private Long id;
  private Long riskId;
  private String system;
  private String systemCode;
  private Long systemId;
  private Date createTime;
  private Date endTime;
  private String content;
  private Long createPersonId;
  private Long receiveUnitId;

  private Double offset;
  private String createPersonName;
  private String status;
  private String receiveUnitName;

  public RiskRelationDTO(Long id, Long riskId, String system, String systemCode,
      Long systemId, Date createTime, Date endTime, String content, Long createPersonId,
      Long receiveUnitId) {
    this.id = id;
    this.riskId = riskId;
    this.system = system;
    this.systemCode = systemCode;
    this.systemId = systemId;
    this.createTime = createTime;
    this.endTime = endTime;
    this.content = content;
    this.createPersonId = createPersonId;
    this.receiveUnitId = receiveUnitId;
  }

  public RiskRelationEntity toEntity() {
    return new RiskRelationEntity(id, riskId, system, systemCode, systemId, createTime, endTime,
        content, createPersonId, receiveUnitId);
  }

}
