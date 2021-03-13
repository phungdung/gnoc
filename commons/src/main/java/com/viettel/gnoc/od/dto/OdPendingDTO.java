/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.od.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.od.model.OdPendingEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author hungtv77
 * @version 2.0
 * @since 31/12/2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OdPendingDTO extends BaseDto {

  private Long odPendId;
  private Date insertTime;
  private Date endPendingTime;
  private String reasonPendingId;
  private String reasonPendingName;
  private String customer;
  private String cusPhone;
  private String odId;
  private Date openTime;
  private String openUser;
  private String description;
  private String pendingType;

  public OdPendingEntity toEntity() {
    return new OdPendingEntity(odPendId, insertTime, endPendingTime, reasonPendingId, reasonPendingName, customer,
        cusPhone, odId, openTime, openUser, description, pendingType);
  }

}
