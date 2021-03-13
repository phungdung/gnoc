/**
 * @(#)ProblemActionLogsBO.java 8/4/2015 6:00 PM, Copyright 2011 Viettel Telecom. All rights
 * reserved VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.pt.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.pt.dto.ProblemActionLogsDTO;
import java.io.Serializable;
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

/**
 * @author tuanpv14
 * @version 1.0
 * @since 8/4/2015 6:00 PM
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "ONE_TM", name = "PROBLEM_ACTION_LOGS")
public class ProblemActionLogsEntity implements Serializable {

  static final long serialVersionUID = 1L;
  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "problem_action_logs_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "PROBLEM_ACTION_LOGS_ID", unique = true, nullable = false)
  private Long problemActionLogsId;

  @Column(name = "CONTENT", nullable = false)
  private String content;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATE_TIME", nullable = false)
  private Date createTime;

  @Column(name = "CREATE_UNIT_ID", nullable = false)
  private Long createUnitId;

  @Column(name = "CREATE_USER_ID", nullable = false)
  private Long createUserId;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "PROBLEM_ID", nullable = false)
  private Long problemId;

  @Column(name = "CREATER_UNIT_NAME")
  private String createrUnitName;

  @Column(name = "CREATER_USER_NAME")
  private String createrUserName;

  @Column(name = "NOTE")
  private String note;

  public ProblemActionLogsDTO toDTO() {
    ProblemActionLogsDTO dto = new ProblemActionLogsDTO(
        problemActionLogsId, content,
        createTime,
        createUnitId,
        createUserId, type,
        problemId, createrUnitName, createrUserName, note
    );
    return dto;
  }
}
