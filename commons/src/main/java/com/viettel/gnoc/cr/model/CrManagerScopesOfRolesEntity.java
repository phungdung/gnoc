package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CrManagerScopesOfRolesDTO;
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

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "OPEN_PM", name = "CR_MANAGER_SCOPES_OF_ROLES")
public class CrManagerScopesOfRolesEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "open_pm.cr_manager_scopes_of_roles_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CMSORS_ID", unique = true, nullable = false)
  private Long cmsorsId;
  @Column(name = "CMSE_ID")
  private Long cmseId;
  @Column(name = "CMRE_ID")
  private Long cmreId;

  public CrManagerScopesOfRolesDTO toDTO() {
    return new CrManagerScopesOfRolesDTO(cmsorsId, cmseId, cmreId);
  }

}
