/**
 * @(#)SmsGatewayForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.SmsGatewayEntity;
import com.viettel.gnoc.commons.validator.Unique;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Unique(message = "{validation.SmsGateway.smsCode.unique}", clazz = SmsGatewayEntity.class, uniqueField = "alias", idField = "smsGatewayId")
public class SmsGatewayDTO extends BaseDto {

  private Long smsGatewayId;
  private String contentTypeText;
  private String defaultSessionId;
  private String sender;
  private String serviceId;
  private String statusNotCharge;
  private String userName;
  private String passWord;
  private String url;
  private String xmlns;
  @NotNull(message = "{validation.smsGateway.null.allias}")
  @Size(max = 250, message = "{validation.smsGateway.allias.tooLong}")
  private String alias;
  @NotNull(message = "{validation.smsGateway.null.prefix}")
  @Size(max = 250, message = "{validation.smsGateway.prefix.tooLong}")
  private String prefix;
  private Long numOfThread;


  public SmsGatewayEntity toEntity() {
    SmsGatewayEntity entity = new SmsGatewayEntity(smsGatewayId, contentTypeText, defaultSessionId,
        sender, serviceId, statusNotCharge, userName, passWord, url, xmlns, alias, prefix,
        numOfThread);
    return entity;
  }
}
