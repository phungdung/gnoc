/**
 * @(#)TroubleNodeBO.java 9/24/2015 5:54 PM, Copyright 2011 Viettel Telecom. All rights reserved
 * VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.TroubleNodeDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "ONE_TM", name = "TROUBLE_NODE")
public class TroubleNodeEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TROUBLE_NODE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "TROUBLE_ID", nullable = false)
  private Long troubleId;

  @Column(name = "DEVICE_ID")
  private Long deviceId;

  @Column(name = "DEVICE_CODE")
  private String deviceCode;

  @Column(name = "DEVICE_NAME")
  private String deviceName;

  @Column(name = "IP")
  private String ip;

  @Column(name = "VENDOR")
  private String vendor;

  public TroubleNodeEntity(Long id, Long troubleId, Long deviceId) {
    this.id = id;
    this.troubleId = troubleId;
    this.deviceId = deviceId;
  }

  public TroubleNodeEntity(
      Long id, Long troubleId, Long deviceId, String deviceCode, String deviceName, String ip,
      String vendor) {
    this.id = id;
    this.troubleId = troubleId;
    this.deviceId = deviceId;
    this.deviceCode = deviceCode;
    this.deviceName = deviceName;
    this.ip = ip;
    this.vendor = vendor;
  }

  public TroubleNodeDTO toDTO() {
    TroubleNodeDTO dto = new TroubleNodeDTO(
        id,
        troubleId,
        deviceId,
        deviceCode,
        deviceName,
        ip,
        vendor
    );
    return dto;
  }
}
