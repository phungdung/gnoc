package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.DeviceTypesDTO;
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

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "OPEN_PM", name = "DEVICE_TYPES")
public class DeviceTypesEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "DEVICE_TYPES_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "DEVICE_TYPE_ID", nullable = false)
  private Long deviceTypeId;
  @Column(name = "DEVICE_TYPE_CODE")
  private String deviceTypeCode;
  @Column(name = "DEVICE_TYPE_NAME")
  private String deviceTypeName;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "IS_ACTIVE")
  private Long isActive;

  public DeviceTypesDTO toDTO() {
    return new DeviceTypesDTO(deviceTypeId, deviceTypeCode, deviceTypeName,
        description, isActive);
  }
}
