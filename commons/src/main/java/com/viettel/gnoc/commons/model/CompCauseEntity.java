/**
 * @(#)CompCauseBO.java 04/12/2015 6:17 PM, Copyright 2011 Viettel Telecom. All rights reserved
 * VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.CompCauseDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "COMMON_GNOC", name = "COMP_CAUSE")
@Getter
@Setter
@NoArgsConstructor
public class CompCauseEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "COMP_CAUSE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "COMP_CAUSE_ID", nullable = false)
  private Long compCauseId;

  @Column(name = "NAME")
  private String name;

  @Column(name = "CODE")
  private String code;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "ISACTIVE")
  private Long isactive;

  @Column(name = "SERVICE_TYPE")
  private String serviceType;

  @Column(name = "PARENT_ID")
  private Long parentId;

  @Column(name = "LEVEL_ID")
  private Long levelId;

  @Column(name = "CFG_TYPE")
  private Long cfgType;

  @Column(name = "ERROR_TYPE")
  private Long errorType;

  @Column(name = "LINE_TYPE")
  private String lineType;

  @Column(name = "IS_SIGNAL")
  private Double isSignal;


  public CompCauseEntity(
      Long compCauseId, String name, String code, String description, Long isactive,
      String serviceType, Long parentId,
      Long levelId, Long cfgType, Long errorType, String lineType, Double isSignal) {
    this.compCauseId = compCauseId;
    this.name = name;
    this.code = code;
    this.description = description;
    this.isactive = isactive;
    this.serviceType = serviceType;
    this.parentId = parentId;
    this.levelId = levelId;
    this.cfgType = cfgType;
    this.errorType = errorType;
    this.lineType = lineType;
    this.isSignal = isSignal;
  }

  public CompCauseDTO toDTO() {
    CompCauseDTO dto = new CompCauseDTO(
        compCauseId == null ? null : compCauseId.toString()
        , name
        , code
        , description
        , isactive == null ? null : isactive.toString()
        , serviceType
        , parentId == null ? null : parentId.toString()
        , levelId == null ? null : levelId.toString()
        , cfgType == null ? null : cfgType.toString()
        , errorType == null ? null : errorType.toString()
        , lineType
        , isSignal == null ? null : isSignal.toString()
    );
    return dto;
  }
}
