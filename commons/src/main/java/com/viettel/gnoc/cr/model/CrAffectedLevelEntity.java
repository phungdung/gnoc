package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.CrAffectedLevelDTO;
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
@Table(schema = "OPEN_PM", name = "AFFECTED_LEVEL")
public class CrAffectedLevelEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "open_pm.AFFECTED_LEVEL_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "AFFECTED_LEVEL_ID", unique = true, nullable = false)
  private Long affectedLevelId;
  @Column(name = "AFFECTED_LEVEL_CODE")
  private String affectedLevelCode;
  @Column(name = "AFFECTED_LEVEL_NAME")
  private String affectedLevelName;
  @Column(name = "TWO_APPROVE_LEVEL")
  private Long twoApproveLevel;
  @Column(name = "APPLIED_SYSTEM")
  private Long appliedSystem;
  @Column(name = "IS_ACTIVE")
  private Long isActive;

  public CrAffectedLevelDTO toDTO() {
    return new CrAffectedLevelDTO(affectedLevelId, affectedLevelCode, affectedLevelName,
        twoApproveLevel, appliedSystem, isActive);
  }
}
