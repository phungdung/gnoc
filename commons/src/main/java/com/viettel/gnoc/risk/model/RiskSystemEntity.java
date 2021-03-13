package com.viettel.gnoc.risk.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.risk.dto.RiskSystemDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "RISK_SYSTEM")
public class RiskSystemEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "RISK_SYSTEM_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "SYSTEM_CODE")
  private String systemCode;

  @Column(name = "SYSTEM_NAME")
  private String systemName;

  @Column(name = "SCHEDULE")
  private Long schedule;

  @Column(name = "SYSTEM_PRIORITY")
  private Long systemPriority;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_TIME")
  private Date lastUpdateTime;

  @Column(name = "COUNTRY_ID")
  private Long countryId;

  public RiskSystemDTO toDTO() {
    return new RiskSystemDTO(id, systemCode, systemName, schedule, systemPriority,
        lastUpdateTime, countryId);
  }

}
