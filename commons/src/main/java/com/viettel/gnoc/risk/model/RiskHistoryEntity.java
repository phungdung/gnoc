package com.viettel.gnoc.risk.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.risk.dto.RiskHistoryDTO;
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
@Table(schema = "WFM", name = "RISK_HISTORY")
public class RiskHistoryEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "RISK_HISTORY_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "RISK_HIS_ID", unique = true, nullable = false)
  private Long riskHisId;

  @Column(name = "RISK_ID")
  private Long riskId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATE_TIME")
  private Date updateTime;

  @Column(name = "OLD_STATUS")
  private Long oldStatus;

  @Column(name = "NEW_STATUS")
  private Long newStatus;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "UNIT_ID")
  private Long unitId;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "IS_SEND_MESSAGE")
  private Long isSendMesssage;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "INFOR_MATION")
  private String inforMation;


  public RiskHistoryDTO toDTO() {
    return new RiskHistoryDTO(riskHisId, riskId, updateTime, oldStatus, newStatus, userId,
        unitId, content, isSendMesssage, userName, inforMation);
  }

}
