/**
 * @(#)ProblemActionLogsBO.java 8/4/2015 6:00 PM, Copyright 2011 Viettel Telecom. All rights
 * reserved VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.pt.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.pt.dto.ProblemCrDTO;
import java.io.Serializable;
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
@Table(schema = "ONE_TM", name = "PROBLEM_CR")
public class ProblemCrEntity implements Serializable {

  static final long serialVersionUID = 1L;
  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "problem_cr_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "PROBLEM_CR_ID", unique = true, nullable = false)
  private Long problemCrId;

  @Column(name = "PROBLEM_ID")
  private Long problemId;

  @Column(name = "CR_ID")
  private Long crId;

  @Column(name = "PT_STATUS_ID")
  private Long ptStatusId;

  public ProblemCrDTO toDTO() {
    ProblemCrDTO dto = new ProblemCrDTO(
        problemCrId,
        problemId,
        crId,
        ptStatusId
    );
    return dto;
  }
}
