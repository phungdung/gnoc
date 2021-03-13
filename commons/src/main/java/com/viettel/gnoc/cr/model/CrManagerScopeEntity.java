package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CrManagerScopeDTO;
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
@Table(schema = "OPEN_PM", name = "CR_MANAGER_SCOPE")
public class CrManagerScopeEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CR_MANAGER_SCOPE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CMSE_ID", nullable = false)
  private Long cmseId;
  @Column(name = "CMSE_CODE")
  private String cmseCode;
  @Column(name = "CMSE_NAME")
  private String cmseName;
  @Column(name = "IS_ACTIVE")
  private Long isActive;
  @Column(name = "DESCRIPTION")
  private String description;

  public CrManagerScopeDTO toDTO() {
    return new CrManagerScopeDTO(cmseId, cmseCode, cmseName, isActive,
        description);
  }
}
