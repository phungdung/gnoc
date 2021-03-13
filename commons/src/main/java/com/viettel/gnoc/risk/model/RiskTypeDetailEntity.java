package com.viettel.gnoc.risk.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.risk.dto.RiskTypeDetailDTO;
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
@Table(schema = "WFM", name = "RISK_TYPE_DETAIL")
public class RiskTypeDetailEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "RISK_TYPE_DETAIL_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "RISK_TYPE_ID")
  private Long riskTypeId;

  @Column(name = "PRIORITY_ID")
  private Long priorityId;

  @Column(name = "PROCESS_TIME")
  private Double processTime;

  @Column(name = "TIME_WARNING_OVERDUE")
  private Long timeWarningOverdue;

  @Column(name = "WARNING_SCHEDULE")
  private Long warningSchedule;

  public RiskTypeDetailDTO toDTO() {
    return new RiskTypeDetailDTO(id, riskTypeId, priorityId, processTime, timeWarningOverdue,
        warningSchedule);
  }

}
