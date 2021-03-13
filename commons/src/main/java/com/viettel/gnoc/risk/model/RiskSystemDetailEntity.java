package com.viettel.gnoc.risk.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.risk.dto.RiskSystemDetailDTO;
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
@Table(schema = "WFM", name = "RISK_SYSTEM_DETAIL")
public class RiskSystemDetailEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "RISK_SYSTEM_DETAIL_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "SYSTEM_ID")
  private Long systemId;

  @Column(name = "MANAGE_UNIT_ID")
  private Long manageUnitId;

  @Column(name = "MANAGE_USER_ID")
  private Long manageUserId;

  public RiskSystemDetailDTO toDTO() {
    return new RiskSystemDetailDTO(id, systemId, manageUnitId, manageUserId);
  }

}
