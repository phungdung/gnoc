package com.viettel.gnoc.sr.model;

import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "OPEN_PM", name = "SR")
public class SREntity {

  @Id
//  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.SR", allocationSize = 1)
//  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "SR_ID")
  private Long srId;
  @Column(name = "SR_CODE")
  private String srCode;
  @Column(name = "COUNTRY")
  private String country;
  @Column(name = "TITLE")
  private String title;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "SERVICE_ARRAY")
  private String serviceArray;
  @Column(name = "SERVICE_GROUP")
  private String serviceGroup;
  @Column(name = "SERVICE_ID")
  private String serviceId;
  @Column(name = "START_TIME")
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date startTime;
  @Column(name = "END_TIME")
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date endTime;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATED_USER")
  private String createdUser;
  @Column(name = "CREATED_TIME")
  private Date createdTime;
  @Column(name = "UPDATED_USER")
  private String updatedUser;
  @Column(name = "UPDATED_TIME")
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date updatedTime;
  @Column(name = "SR_UNIT")
  private Long srUnit;
  @Column(name = "SR_USER")
  private String srUser;
  @Column(name = "SEND_DATE")
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date sendDate;
  @Column(name = "REVIEW_ID")
  private Long reviewId;

  //namtn add tab split SR
  @Column(name = "PARENT_CODE")
  private String parentCode;
  @Column(name = "ROLE_CODE")
  private String roleCode;
  @Column(name = "INSERT_SOURCE")
  private String insertSource;
  // thangdt nang cap pt
  @Column(name = "OTHER_SYSTEM_CODE")
  private String otherSystemCode;
  @Column(name = "COUNT_NOK")
  private Long countNok;
  @Column(name = "IS_FORCE_CLOSED")
  private Long isForceClosed;
  @Column(name = "CR_NUMBER")
  private String crNumber;
  @Column(name = "NOTE")
  private String note;
  @Column(name = "TIME_SAVE")
  private Date timeSave;

  public SrInsiteDTO toDTO() {
    return new SrInsiteDTO(srId, srCode, country, title, description, serviceArray, serviceGroup,
        serviceId, startTime, endTime, status, createdUser, createdTime, updatedUser, updatedTime,
        srUnit, srUser, sendDate, reviewId, parentCode, roleCode, insertSource, otherSystemCode,
        countNok, isForceClosed, crNumber, note, timeSave, false);
  }
}
