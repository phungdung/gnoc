package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CrRoleDeptDTO;
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
@Table(schema = "OPEN_PM", name = "CR_MANAGER_ROLES_OF_UNIT")
public class CrRoleDeptEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CR_MANAGER_ROLES_OF_UNIT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CMROUT_ID", nullable = false)
  private Long cmroutId;
  @Column(name = "CMRE_ID")
  private Long cmreId;
  @Column(name = "UNIT_ID")
  private Long unitId;

  public CrRoleDeptDTO toDTO() {
    return new CrRoleDeptDTO(cmroutId, cmreId, unitId);
  }
}
