/**
 * @(#)DeviceTypeVersionBO.java 9/10/2015 10:56 AM, Copyright 2011 Viettel Telecom. All rights
 * reserved VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.DeviceTypeVersionDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(schema = "COMMON_GNOC", name = "DEVICE_TYPE_VERSION")
@Getter
@Setter
@NoArgsConstructor
public class DeviceTypeVersionEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "device_type_version_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "DEVICE_TYPE_VERSION_ID", unique = true, nullable = false)
  private Long deviceTypeVersionId;

  @Column(name = "VENDOR_ID")
  private Long vendorId;

  @Column(name = "TYPE_ID")
  private Long typeId;

  @Column(name = "SOFTWARE_VERSION")
  private String softwareVersion;

  @Column(name = "HARDWARE_VERSION")
  private String hardwareVersion;

  @Column(name = "TEMP")
  private String temp;

  public DeviceTypeVersionEntity(
      Long deviceTypeVersionId, Long vendorId, Long typeId, String softwareVersion,
      String hardwareVersion, String temp) {
    this.deviceTypeVersionId = deviceTypeVersionId;
    this.vendorId = vendorId;
    this.typeId = typeId;
    this.softwareVersion = softwareVersion;
    this.hardwareVersion = hardwareVersion;
    this.temp = temp;
  }

  public DeviceTypeVersionDTO toDTO() {
    DeviceTypeVersionDTO dto = new DeviceTypeVersionDTO(
        deviceTypeVersionId,
        vendorId,
        typeId,
        softwareVersion,
        hardwareVersion,
        temp
    );
    return dto;
  }
}

