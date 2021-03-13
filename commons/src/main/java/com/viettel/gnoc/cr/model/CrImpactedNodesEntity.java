/**
 * @(#)CrImpactedNodesBO.java 9/4/2015 5:26 PM, Copyright 2011 Viettel Telecom. All rights reserved
 * VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "CR_IMPACTED_NODES")
public class CrImpactedNodesEntity {

  //Fields

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CR_IMPACTED_NODES_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CINS_ID", unique = true, nullable = false)
  private Long cinsId;

  @Column(name = "CR_ID")
  private Long crId;

  @Column(name = "IP_ID")
  private Long ipId;

  @Column(name = "DEVICE_ID")
  private Long deviceId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "INSERT_TIME")
  private Date insertTime;

  @Column(name = "DT_CODE")
  private String dtCode;

  @Column(name = "IP")
  private String ip;

  @Column(name = "DEVICE_NAME")
  private String deviceName;

  @Column(name = "DEVICE_CODE")
  private String deviceCode;

  @Column(name = "NATION_CODE")
  private String nationCode;

  public CrImpactedNodesEntity(Long cinsId, Long crId, Long ipId, Long deviceId, Date insertTime,
      String dtCode) {
    this.cinsId = cinsId;
    this.crId = crId;
    this.ipId = ipId;
    this.deviceId = deviceId;
    this.insertTime = insertTime;
    this.dtCode = dtCode;
  }

  public CrImpactedNodesDTO toDTO() {
    CrImpactedNodesDTO dto = new CrImpactedNodesDTO(
        cinsId == null ? null : cinsId.toString(), crId == null ? null : crId.toString(),
        ipId == null ? null : ipId.toString(), deviceId == null ? null : deviceId.toString(),
        insertTime == null ? null : DateTimeUtils
            .convertDateToString(insertTime, Constants.ddMMyyyy), dtCode
    );
    dto.setIp(ip);
    dto.setDeviceCode(deviceCode);
    dto.setDeviceName(deviceName);
    dto.setNationCode(nationCode);
    return dto;
  }
}
