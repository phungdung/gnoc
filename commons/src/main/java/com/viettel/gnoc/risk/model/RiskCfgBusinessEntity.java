package com.viettel.gnoc.risk.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.risk.dto.RiskCfgBusinessDTO;
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
@Table(schema = "WFM", name = "RISK_CFG_BUSINESS")
public class RiskCfgBusinessEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "RISK_CFG_BUSINESS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "RISK_CHANGE_STATUS_ID")
  private Long riskChangeStatusId;

  @Column(name = "IS_REQUIRED")
  private Long isRequired;

  @Column(name = "COLUMN_NAME")
  private String columnName;

  @Column(name = "EDITABLE")
  private Long editable;

  @Column(name = "IS_VISIBLE")
  private Long isVisible;

  @Column(name = "MESSAGE")
  private String message;

  public RiskCfgBusinessDTO toDTO() {
    return new RiskCfgBusinessDTO(id, riskChangeStatusId, isRequired, columnName, editable,
        isVisible, message);
  }

}
