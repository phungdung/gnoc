package com.viettel.gnoc.pt.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.pt.dto.ProblemWoDTO;
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

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "ONE_TM", name = "PROBLEM_WO")
public class ProblemWoEntity implements Serializable {

  static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "problem_wo_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "PROBLEM_WO_ID", nullable = false)
  private Long problemWoId;

  @Column(name = "PROBLEM_ID")
  private Long problemId;

  @Column(name = "WO_ID")
  private Long woId;

  @Column(name = "PT_STATUS_ID")
  private Long ptStatusId;


  public ProblemWoDTO toDTO() {
    ProblemWoDTO dto = new ProblemWoDTO(
        problemWoId == null ? null : problemWoId,
        problemId == null ? null : problemId,
        woId == null ? null : woId,
        ptStatusId == null ? null : ptStatusId
    );
    return dto;
  }
}

