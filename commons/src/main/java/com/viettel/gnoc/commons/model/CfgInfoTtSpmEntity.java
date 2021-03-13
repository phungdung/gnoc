package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.CfgInfoTtSpmDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "COMMON_GNOC", name = "CFG_INFO_TT_SPM")
@NoArgsConstructor
@Setter
@Getter
public class CfgInfoTtSpmEntity {

  //Fields
  @Column(name = "ID", nullable = false)
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "cfg_info_tt_spm_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  private Long id;

  @Column(name = "TYPE_ID")
  private Long typeId;

  @Column(name = "ALARM_GROUP_ID")
  private Long alarmGroupId;

  @Column(name = "SUB_CATEGORY_ID")
  private Long subCategoryId;

  @Column(name = "TROUBLE_NAME")
  private String troubleName;

  public CfgInfoTtSpmEntity(
      Long id, Long typeId, Long alarmGroupId, Long subCategoryId, String troubleName) {
    this.id = id;
    this.typeId = typeId;
    this.alarmGroupId = alarmGroupId;
    this.subCategoryId = subCategoryId;
    this.troubleName = troubleName;
  }

  public CfgInfoTtSpmDTO toDTO() {
    CfgInfoTtSpmDTO dto = new CfgInfoTtSpmDTO(
        id == null ? null : id.toString(),
        typeId == null ? null : typeId.toString(),
        alarmGroupId == null ? null : alarmGroupId.toString(),
        subCategoryId == null ? null : subCategoryId.toString(),
        troubleName
    );
    return dto;
  }
}
