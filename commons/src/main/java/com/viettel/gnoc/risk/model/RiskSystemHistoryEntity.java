package com.viettel.gnoc.risk.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.risk.dto.RiskSystemHistoryDTO;
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
@Table(schema = "WFM", name = "RISK_SYSTEM_HISTORY")
public class RiskSystemHistoryEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "RISK_SYSTEM_HISTORY_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "RISK_SYSTEM_HISTORY_ID", unique = true, nullable = false)
  private Long riskSystemHistoryId;

  @Column(name = "SYSTEM_ID")
  private Long systemId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATE_TIME")
  private Date updateTime;

  @Column(name = "OLD_FILE")
  private String oldFile;

  @Column(name = "NEW_FILE")
  private String newFile;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "UNIT_ID")
  private Long unitId;

  @Column(name = "IS_SEND_SMS_MESSAGE")
  private Long isSendSMSMessage;

  @Column(name = "CONTENT")
  private String content;

  public RiskSystemHistoryDTO toDTO() {
    return new RiskSystemHistoryDTO(riskSystemHistoryId, systemId, updateTime, oldFile, newFile,
        userId, unitId, isSendSMSMessage, content);
  }

}
