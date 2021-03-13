package com.viettel.gnoc.risk.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.risk.model.RiskPendingEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskPendingDTO extends BaseDto {

  private Long riskPendingId;
  private String cusPhone;
  private String customer;
  private String description;
  private Date endPendingTime;
  private Date insertTime;
  private Date openTime;
  private String openUser;
  private Long pendingType;
  private Long reasonPendingId;
  private String reasonPendingName;
  private Long riskId;

  public RiskPendingEntity toEntity() {
    return new RiskPendingEntity(riskPendingId, cusPhone, customer, description, endPendingTime,
        insertTime, openTime, openUser, pendingType, reasonPendingId, reasonPendingName, riskId);
  }

}
