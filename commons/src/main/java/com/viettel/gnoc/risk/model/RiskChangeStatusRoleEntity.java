package com.viettel.gnoc.risk.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.risk.dto.RiskChangeStatusRoleDTO;
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
@Table(schema = "WFM", name = "RISK_CHANGE_STATUS_ROLE")
public class RiskChangeStatusRoleEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "RISK_CHANGE_STATUS_ROLE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "RISK_CHANGE_STATUS_ID")
  private Long riskChangeStatusId;

  @Column(name = "ROLE_ID")
  private Long roleId;

  public RiskChangeStatusRoleDTO toDTO() {
    return new RiskChangeStatusRoleDTO(id, riskChangeStatusId, roleId);
  }

}
