package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CrUnitsScopeDeviceTypeDTO;
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

/**
 * @author DungPV
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "CR_UNITS_SCOPE_DEVICE_TYPE")
public class CrUnitsScopeDeviceTypeEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "open_pm.CR_UNITS_SCOPE_DEVICE_TYPE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;
  @Column(name = "DEVICE_TYPE_ID")
  private Long deviceTypeId;
  @Column(name = "CR_UNITS_SCOPE_ID")
  private Long crUnitsScopeId;

  public CrUnitsScopeDeviceTypeDTO toDTO() {
    return new CrUnitsScopeDeviceTypeDTO(id, deviceTypeId, crUnitsScopeId);
  }
}
