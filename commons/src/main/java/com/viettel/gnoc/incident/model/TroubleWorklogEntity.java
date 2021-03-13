/**
 * @(#)TroubleWorklogBO.java 9/14/2015 2:36 PM, Copyright 2011 Viettel Telecom. All rights reserved
 * VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.TroubleWorklogInsiteDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(schema = "ONE_TM", name = "TROUBLE_WORKLOG")
public class TroubleWorklogEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TROUBLE_WORKLOG_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "CREATE_USER_ID")
  private Long createUserId;

  @Column(name = "CREATE_USER_NAME")
  private String createUserName;

  @Column(name = "CREATE_UNIT_ID")
  private Long createUnitId;

  @Column(name = "CREATE_UNIT_NAME")
  private String createUnitName;

  @Column(name = "WORKLOG")
  private String worklog;

  @Column(name = "DESCRIPTION")
  private String description;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "TROUBLE_ID", nullable = false)
  private Long troubleId;

  public TroubleWorklogEntity(Long id, Long createUserId, String createUserName, Long createUnitId,
      String createUnitName, String worklog, String description, Date createdTime, Long troubleId) {
    this.id = id;
    this.createUserId = createUserId;
    this.createUserName = createUserName;
    this.createUnitId = createUnitId;
    this.createUnitName = createUnitName;
    this.worklog = worklog;
    this.description = description;
    this.createdTime = createdTime;
    this.troubleId = troubleId;
  }

  public TroubleWorklogInsiteDTO toDTO() {
    TroubleWorklogInsiteDTO dto = new TroubleWorklogInsiteDTO(
        id,
        createUserId,
        createUserName,
        createUnitId,
        createUnitName,
        worklog,
        description,
        createdTime,
        troubleId
    );
    return dto;
  }
}
