package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
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
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "OPEN_PM", name = "IMPACT_SEGMENT")
public class ImpactSegmentEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "open_pm.IMPACT_SEGMENT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "IMPACT_SEGMENT_ID", unique = true, nullable = false)
  private Long impactSegmentId;
  @Column(name = "IMPACT_SEGMENT_CODE")
  private String impactSegmentCode;
  @Column(name = "IMPACT_SEGMENT_NAME")
  private String impactSegmentName;
  @Column(name = "APPLIED_SYSTEM")
  private Long appliedSystem;
  @Column(name = "IS_ACTIVE")
  private Long isActive;

  public ImpactSegmentDTO toDTO() {
    return new ImpactSegmentDTO(impactSegmentId, impactSegmentCode, impactSegmentName,
        appliedSystem, isActive);
  }
}
