/**
 * @(#)OdRelationBO.java 5/8/2015 4:40 PM, Copyright 2011 Viettel Telecom. All rights reserved
 * VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.od.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.od.dto.OdRelationDTO;
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
@Table(schema = "WFM", name = "OD_RELATION")
public class OdRelationEntity {

  static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OD_RELATION_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;
  @Column(name = "OD_ID")
  private Long odId;
  @Column(name = "SYSTEM")
  private String system;
  @Column(name = "SYSTEM_CODE")
  private String systemCode;
  @Column(name = "SYSTEM_ID")
  private Long systemId;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATE_TIME")
  private Date createTime;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "END_TIME")
  private Date endTime;
  @Column(name = "CONTENT")
  private String content;
  @Column(name = "CREATE_PERSON_ID")
  private Long createPersonId;
  @Column(name = "RECEIVE_UNIT_ID")
  private Long receiveUnitId;

  public OdRelationDTO toDTO() {
    return new OdRelationDTO(id, odId, system, systemCode, systemId, createTime, endTime, content,
        createPersonId, receiveUnitId);
  }


}
