/**
 * @(#)InfraIpBO.java 8/27/2015 3:03 PM, Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.InfraIpDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(schema = "COMMON_GNOC", name = "INFRA_IP")
@Getter
@Setter
@NoArgsConstructor
public class InfraIpEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "INFRA_IP_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "IP_ID", nullable = false)
  private Long ipId;
  //Fields
  @Column(name = "DEVICE_ID", columnDefinition = "InfraDevice")
  private Long deviceId;

  @Column(name = "IP_TYPE")
  private String ipType;

  @Column(name = "IP_VERSION")
  private Long ipVersion;

  @Column(name = "IP")
  private String ip;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_TIME")
  private Date lastUpdateTime;

  public InfraIpEntity(Long deviceId, String ipType, Long ipVersion, String ip, Long ipId,
      Date lastUpdateTime) {
    this.deviceId = deviceId;
    this.ipType = ipType;
    this.ipVersion = ipVersion;
    this.ip = ip;
    this.ipId = ipId;
    this.lastUpdateTime = lastUpdateTime;
  }

  public InfraIpDTO toDTO() {
    InfraIpDTO dto = new InfraIpDTO(
        deviceId == null ? null : deviceId.toString(),
        ipType,
        ipVersion == null ? null : ipVersion.toString(),
        ip,
        ipId == null ? null : ipId.toString(),
        lastUpdateTime == null ? null
            : DateTimeUtils.convertDateToString(lastUpdateTime, Constants.ddMMyyyy)
    );
    return dto;
  }
}

