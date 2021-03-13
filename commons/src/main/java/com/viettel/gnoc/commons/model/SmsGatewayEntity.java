/**
 * @(#)SmsGatewayBO.java 10/1/2015 5:17 PM, Copyright 2011 Viettel Telecom. All rights reserved
 * VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.SmsGatewayDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "SMS_GATEWAY")
public class SmsGatewayEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "COMMON_GNOC.sms_gateway_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "SMS_GATEWAY_ID", nullable = false)
  private Long smsGatewayId;

  @Column(name = "CONTENT_TYPE_TEXT")
  private String contentTypeText;

  @Column(name = "DEFAULT_SESSION_ID")
  private String defaultSessionId;

  @Column(name = "SENDER")
  private String sender;

  @Column(name = "SERVICE_ID")
  private String serviceId;

  @Column(name = "STATUS_NOT_CHARGE")
  private String statusNotCharge;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "PASS_WORD")
  private String passWord;

  @Column(name = "URL")
  private String url;

  @Column(name = "XMLNS")
  private String xmlns;

  @Column(name = "ALIAS")
  private String alias;

  @Column(name = "PREFIX")
  private String prefix;

  @Column(name = "NUM_OF_THREAD")
  private Long numOfThread;

  public SmsGatewayDTO toDTO() {
    SmsGatewayDTO dto = new SmsGatewayDTO(
        smsGatewayId, contentTypeText, defaultSessionId,
        sender, serviceId, statusNotCharge, userName, passWord, url, xmlns, alias, prefix,
        numOfThread
    );
    return dto;
  }
}
