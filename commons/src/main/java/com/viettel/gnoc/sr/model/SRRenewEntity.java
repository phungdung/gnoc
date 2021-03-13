package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRRenewDTO;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "SR_RENEW")
public class SRRenewEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_RENEW_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "RENEW_ID")
  private Long renewId;

  @Column(name = "SR_ID")
  private Long srId;

  @Column(name = "END_TIME")
  private Date endTime;

  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "REASON_RENEW")
  private String reasonRenew;

  @Column(name = "STATUS_RENEW")
  private Long statusRenew;

  @Column(name = "END_TIME_RENEW")
  private Date endTimeRenew;

  @Column(name = "REASON_REJECT")
  private String reasonReject;

  @Column(name = "UPDATED_TIME")
  private Date updatedTime;

  @Column(name = "CREATED_USER")
  private String createdUser;

  public SRRenewDTO toDTO() {
    return new SRRenewDTO(renewId, srId, endTime, createdTime, reasonRenew, statusRenew
        , endTimeRenew, reasonReject, updatedTime, createdUser, null, null
    );
  }
}
