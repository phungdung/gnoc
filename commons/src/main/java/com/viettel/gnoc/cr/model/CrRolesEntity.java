package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CrRolesDTO;
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
@Table(schema = "OPEN_PM", name = "CR_MANAGER_ROLE")
public class CrRolesEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "open_pm.CR_MANAGER_ROLE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CMRE_ID", unique = true, nullable = false)
  private Long cmreId;
  @Column(name = "CMRE_CODE", nullable = false)
  private String cmreCode;
  @Column(name = "CMRE_NAME", nullable = false)
  private String cmreName;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "IS_ACTIVE")
  private Long status;
  @Column(name = "IS_SCHEDULE_CR_EMERGENCY")
  private Long isScheduleCrEmergency;

  public CrRolesDTO toDTO() {
    return new CrRolesDTO(cmreId, cmreCode, cmreName, description, status, isScheduleCrEmergency);
  }
}
