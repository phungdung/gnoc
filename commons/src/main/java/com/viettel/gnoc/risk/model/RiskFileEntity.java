package com.viettel.gnoc.risk.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.risk.dto.RiskFileDTO;
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
@Table(schema = "WFM", name = "RISK_FILE")
public class RiskFileEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "RISK_FILE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "RISK_FILE_ID", unique = true, nullable = false)
  private Long riskFileId;

  @Column(name = "RISK_ID")
  private Long riskId;

  @Column(name = "FILE_NAME")
  private String fileName;

  @Column(name = "PATH")
  private String path;

  public RiskFileDTO toDTO() {
    return new RiskFileDTO(riskFileId, riskId, fileName, path);
  }

}
