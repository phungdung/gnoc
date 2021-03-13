package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoCdGroupTypeDTO;
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
@Table(schema = "WFM", name = "WO_CD_GROUP_TYPE")
public class WoCdGroupTypeEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_CD_GROUP_TYPE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "GROUP_TYPE_ID", unique = true, nullable = false)
  private Long groupTypeId;

  @Column(name = "GROUP_TYPE_CODE", nullable = false)
  private String groupTypeCode;

  @Column(name = "GROUP_TYPE_NAME", nullable = false)
  private String groupTypeName;

  @Column(name = "IS_ENABLE")
  private Long isEnable;

  public WoCdGroupTypeDTO toDTO() {
    return new WoCdGroupTypeDTO(groupTypeId, groupTypeCode, groupTypeName, isEnable);
  }

}
