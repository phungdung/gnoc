package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRWorkLogDTO;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "SR_WORKLOG")
public class SRWorkLogEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_WORKLOG_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WL_ID", unique = true, nullable = false)
  private Long wlId;

  @Column(name = "WL_TYPE_ID")
  private Long wlTypeId;

  @Column(name = "WL_TEXT")
  private String wlText;

  @Column(name = "CREATED_USER")
  private String createdUser;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "SR_ID")
  private Long srId;

  @Column(name = "REASON_REJECT_ID")
  private Long reasonRejectId;

  public SRWorkLogDTO toDTO() {
    SRWorkLogDTO dto = new SRWorkLogDTO(
        wlId,
        wlTypeId,
        wlText,
        createdUser,
        createdTime,
        srId,
        reasonRejectId
    );
    return dto;
  }
}
