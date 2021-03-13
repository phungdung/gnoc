/**
 * @(#)TransitionStateConfigBO.java 8/7/2015 4:19 PM, Copyright 2011 Viettel Telecom. All rights
 * reserved VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.TransitionStateConfigDTO;
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
@Table(schema = "COMMON_GNOC", name = "TRANSITION_STATE_CONFIG")
public class TransitionStateConfigEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TRANSITION_STATE_CONFIG_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "BEGIN_STATE_ID", nullable = false)
  private Long beginStateId;

  @Column(name = "END_STATE_ID", nullable = false)
  private Long endStateId;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "PROCESS", nullable = false)
  private Long process;

  @Column(name = "SKIP_STATUS")
  private String skipStatus;

  @Column(name = "ROLE_CODE")
  private Long roleCode;

  public TransitionStateConfigDTO toDTO() {
    return new TransitionStateConfigDTO(id, beginStateId, endStateId, description, process, skipStatus, roleCode);
  }

}
