package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoCdGroupUnitDTO;
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
@Table(schema = "WFM", name = "WO_CD_GROUP_UNIT")
public class WoCdGroupUnitEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_CD_GROUP_UNIT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CD_GROUP_UNIT_ID", unique = true, nullable = false)
  private Long cdGroupUnitId;

  @Column(name = "CD_GROUP_ID")
  private Long cdGroupId;

  @Column(name = "UNIT_ID")
  private Long unitId;

  @Column(name = "IS_ROOT")
  private Long isRoot;

  public WoCdGroupUnitDTO toDTO() {
    return new WoCdGroupUnitDTO(cdGroupUnitId, cdGroupId, unitId, isRoot);
  }
}
