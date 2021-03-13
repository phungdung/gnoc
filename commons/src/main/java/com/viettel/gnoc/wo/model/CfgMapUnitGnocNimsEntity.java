package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.CfgMapUnitGnocNimsDTO;
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
 * @author itsol
 * @version 2.0
 * @since 12/03/2018
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "CFG_MAP_UNIT_GNOC_NIMS")
public class CfgMapUnitGnocNimsEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CFG_MAP_UNIT_GNOC_NIMS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "id", unique = true, nullable = false)
  private Long id;
  @Column(name = "UNIT_NIMS_CODE")
  private String unitNimsCode;
  @Column(name = "UNIT_GNOC_CODE")
  private String unitGnocCode;
  @Column(name = "BUSINESS_CODE")
  private Long businessCode;
  @Column(name = "BUSINESS_NAME")
  private String businessName;

  public CfgMapUnitGnocNimsDTO toDTO() {
    return new CfgMapUnitGnocNimsDTO(id, unitNimsCode, unitGnocCode, businessCode, businessName);
  }


}
