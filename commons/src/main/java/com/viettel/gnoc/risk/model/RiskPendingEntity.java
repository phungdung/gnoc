package com.viettel.gnoc.risk.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.risk.dto.RiskPendingDTO;
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
@Table(schema = "WFM", name = "RISK_PENDING")
public class RiskPendingEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "RISK_PENDING_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "RISK_PENDING_ID", unique = true, nullable = false)
  private Long riskPendingId;

  @Column(name = "CUS_PHONE")
  private String cusPhone;

  @Column(name = "CUSTOMER")
  private String customer;

  @Column(name = "DESCRIPTION")
  private String description;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "END_PENDING_TIME")
  private Date endPendingTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "INSERT_TIME")
  private Date insertTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "OPEN_TIME")
  private Date openTime;

  @Column(name = "OPEN_USER")
  private String openUser;

  @Column(name = "PENDING_TYPE")
  private Long pendingType;

  @Column(name = "REASON_PENDING_ID")
  private Long reasonPendingId;

  @Column(name = "REASON_PENDING_NAME")
  private String reasonPendingName;

  @Column(name = "RISK_ID")
  private Long riskId;

  public RiskPendingDTO toDTO() {
    return new RiskPendingDTO(riskPendingId, cusPhone, customer, description, endPendingTime,
        insertTime, openTime, openUser, pendingType, reasonPendingId, reasonPendingName, riskId);
  }
}
