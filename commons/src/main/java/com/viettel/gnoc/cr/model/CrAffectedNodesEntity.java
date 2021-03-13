/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.model;

/**
 * @author ITSOL
 */

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CrAffectedNodesDTO;
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
@Table(schema = "OPEN_PM", name = "CR_AFFECTED_NODES")
public class CrAffectedNodesEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "cr_affected_nodes_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CANS_ID", unique = true, nullable = false)
  private Long cansId;

  @Column(name = "CR_ID")
  private Long crId;

  @Column(name = "IP_ID")
  private Long ipId;

  @Column(name = "DEVICE_ID")
  private Long deviceId;

  @Column(name = "INSERT_TIME")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date insertTime;

  @Column(name = "IP_ID_STR")
  private String ipIdStr;

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

  public CrAffectedNodesEntity(Long cansId, Long crId, Long ipId, Long deviceId, Date insertTime,
      String dtCode) {
    this.cansId = cansId;
    this.crId = crId;
    this.ipId = ipId;
    this.deviceId = deviceId;
    this.insertTime = insertTime;
    this.dtCode = dtCode;
  }

  public CrAffectedNodesDTO toDTO() {
    CrAffectedNodesDTO dto = new CrAffectedNodesDTO(
        cansId == null ? null : cansId.toString(),
        crId == null ? null : crId.toString(),
        ipId == null ? null : ipId.toString(),
        deviceId == null ? null : deviceId.toString(),
        insertTime == null ? null : insertTime.toString(), dtCode
    );
    dto.setIp(ip);
    dto.setDeviceCode(deviceCode);
    dto.setDeviceName(deviceName);
    dto.setNationCode(nationCode);
    return dto;
  }
}
