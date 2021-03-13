package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.CfgUnitTtSpmDTO;
import java.io.Serializable;
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
@Table(schema = "COMMON_GNOC", name = "CFG_UNIT_TT_SPM")
public class CfgUnitTtSpmEntity implements Serializable {

  static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "cfg_unit_tt_spm_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;
  @Column(name = "TYPE_ID", nullable = false)
  private Long typeId;
  @Column(name = "LOCATION_ID")
  private Long locationId;
  @Column(name = "UNIT_ID")
  private Long unitId;
  @Column(name = "TYPE_UNIT")
  private Long typeUnit;

  public CfgUnitTtSpmDTO toDTO() {
    CfgUnitTtSpmDTO dto = new CfgUnitTtSpmDTO(
        id == null ? null : id.toString(),
        typeId == null ? null : typeId.toString(),
        locationId == null ? null : locationId.toString(),
        unitId == null ? null : unitId.toString(),
        typeUnit == null ? null : typeUnit.toString()
    );
    return dto;
  }
}
