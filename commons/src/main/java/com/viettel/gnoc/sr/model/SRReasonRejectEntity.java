package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRReasonRejectDTO;
import java.util.Date;
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

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "OPEN_PM", name = "SR_REASON_REJECT")
public class SRReasonRejectEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_REASON_REJECT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "reasonRejectId", unique = true, nullable = false)
  private Long reasonRejectId;

  @Column(name = "SR_STATUS")
  private String srStatus;

  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "CREATED_USER")
  private String createdUser;

  @Column(name = "REASON")
  private String reason;

  public SRReasonRejectDTO toDTO() {
    SRReasonRejectDTO dto = new SRReasonRejectDTO(
        reasonRejectId,
        srStatus,
        createdTime,
        createdUser,
        reason
    );
    return dto;
  }
}
