
package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoTypeCfgRequiredDTO;
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

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "WFM", name = "WO_TYPE_CFG_REQUIRED")
public class WoTypeCfgRequiredEntity {

  static final long serialVersionUID = 1L;

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_TYPE_CFG_REQUIRED_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "WO_TYPE_ID")
  private Long woTypeId;

  @Column(name = "CFG_ID")
  private Long cfgId;

  @Column(name = "VALUE")
  private Long value;

  public WoTypeCfgRequiredDTO toDTO() {
    WoTypeCfgRequiredDTO dto = new WoTypeCfgRequiredDTO(
        id
        , woTypeId
        , cfgId
        , value
    );
    return dto;
  }
}

