package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.WoPendingEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoPendingDTO extends BaseDto {

  private Long woPendingId;
  private Date insertTime;
  private Date endPendingTime;
  private Long reasonPendingId;
  private String reasonPendingName;
  private String customer;
  private String cusPhone;
  private Long woId;
  private Date openTime;
  private String openUser;
  private String description;
  private Long pendingType;

  public WoPendingEntity toEntity() {
    return new WoPendingEntity(woPendingId, insertTime, endPendingTime, reasonPendingId,
        reasonPendingName, customer, cusPhone, woId, openTime, openUser, description, pendingType);
  }

}
