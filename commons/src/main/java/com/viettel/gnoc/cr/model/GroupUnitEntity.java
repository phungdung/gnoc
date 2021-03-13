package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.GroupUnitDTO;
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
@Table(schema = "OPEN_PM", name = "GROUP_UNIT")
public class GroupUnitEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "GROUP_UNIT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "GROUP_UNIT_ID", nullable = false)
  private Long groupUnitId;
  @Column(name = "GROUP_UNIT_CODE")
  private String groupUnitCode;
  @Column(name = "GROUP_UNIT_NAME")
  private String groupUnitName;
  @Column(name = "IS_ACTIVE")
  private Long isActive;

  public GroupUnitDTO toDTO() {
    return new GroupUnitDTO(groupUnitId, groupUnitCode, groupUnitName, isActive);
  }
}
