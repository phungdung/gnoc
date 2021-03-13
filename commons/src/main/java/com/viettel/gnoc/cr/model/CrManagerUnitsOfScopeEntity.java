package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CrManagerUnitsOfScopeDTO;
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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "OPEN_PM", name = "CR_MANAGER_UNITS_OF_SCOPE")
public class CrManagerUnitsOfScopeEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "open_pm.cr_manager_units_of_scope_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CMNOSE_ID", unique = true, nullable = false)
  private Long cmnoseId;
  @Column(name = "CMSE_ID")
  private Long cmseId;
  @Column(name = "UNIT_ID")
  private Long unitId;
  @Column(name = "CR_TYPE")
  private Long crTypeId;
  @Column(name = "DEVICE_TYPE")
  private Long deviceType;

  public CrManagerUnitsOfScopeDTO toDTO() {
    return new CrManagerUnitsOfScopeDTO(cmnoseId, cmseId, unitId, crTypeId, deviceType);
  }
}
