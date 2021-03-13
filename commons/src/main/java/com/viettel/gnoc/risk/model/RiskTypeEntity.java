package com.viettel.gnoc.risk.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.risk.dto.RiskTypeDTO;
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
@Table(schema = "WFM", name = "RISK_TYPE")
public class RiskTypeEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "RISK_TYPE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "RISK_TYPE_ID", unique = true, nullable = false)
  private Long riskTypeId;

  @Column(name = "RISK_TYPE_CODE")
  private String riskTypeCode;

  @Column(name = "RISK_TYPE_NAME")
  private String riskTypeName;

  @Column(name = "STATUS")
  private Long status;

  @Column(name = "RISK_GROUP_TYPE_ID")
  private Long riskGroupTypeId;

  public RiskTypeDTO toDTO() {
    return new RiskTypeDTO(riskTypeId, riskTypeCode, riskTypeName, status, riskGroupTypeId);
  }

}
