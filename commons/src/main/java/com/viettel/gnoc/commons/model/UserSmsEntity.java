/**
 * @(#)UnitBO.java 8/5/2015 1:52 PM, Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.model;

import com.viettel.gnoc.kedb.dto.UserSmsDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "ONE_TM", name = "USER_SMS")
public class UserSmsEntity {

  @Id
  @Column(name = "USER_ID", nullable = false)
  private Long userId;

  @Column(name = "SMS_TYPE")
  private Long smsType;

  @Column(name = "TYPE_CODE")
  private String typeCode;

  @Column(name = "LAST_UPDATE_TIME")
  private Date lastUpdateTime;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "MOBILE")
  private String mobile;

  public UserSmsDTO toDTO() {
    return new UserSmsDTO(userId, smsType, typeCode, lastUpdateTime, email, mobile);
  }

}
