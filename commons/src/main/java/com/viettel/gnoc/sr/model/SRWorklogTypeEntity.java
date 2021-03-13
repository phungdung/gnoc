package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRWorklogTypeDTO;
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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "SR_WORKLOG_TYPE")
public class SRWorklogTypeEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_WORKLOG_TYPE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WL_TYPE_ID", unique = true, nullable = false)
  private Long wlTypeId;
  @Column(name = "WL_TYPE_CODE")
  private String wlTypeCode;
  @Column(name = "WL_TYPE_NAME")
  private String wlTypeName;
  @Column(name = "SR_STATUS")
  private String srStatus;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATED_USER")
  private String createdUser;
  @Column(name = "CREATED_TIME")
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date createdTime;
  @Column(name = "UPDATED_USER")
  private String updatedUser;
  @Column(name = "UPDATED_TIME")
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date updatedTime;

  public SRWorklogTypeDTO toDTO() {
    return new SRWorklogTypeDTO(wlTypeId, wlTypeCode, wlTypeName, srStatus, status, createdUser,
        createdTime, updatedUser, updatedTime);
  }
}
