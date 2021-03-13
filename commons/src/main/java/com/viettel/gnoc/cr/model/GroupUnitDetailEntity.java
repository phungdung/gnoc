package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.GroupUnitDetailDTO;
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
@Table(schema = "OPEN_PM", name = "GROUP_UNIT_DETAIL")
public class GroupUnitDetailEntity {

  static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "GROUP_UNIT_DETAIL_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "GROUP_UNIT_DETAIL_ID", nullable = false)
  private Long groupUnitDetailId;
  @Column(name = "GROUP_UNIT_ID")
  private Long groupUnitId;
  @Column(name = "UNIT_ID")
  private Long unitId;

  public GroupUnitDetailDTO toDTO() {
    return new GroupUnitDetailDTO(groupUnitDetailId, groupUnitId, unitId);
  }
}
