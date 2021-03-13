package com.viettel.gnoc.pt.model;

import com.viettel.gnoc.pt.dto.ProblemWorklogDTO;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PROBLEM_WORKLOG")
public class ProblemWorklogEntity implements Serializable {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "problem_worklog_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
  @Column(name = "PROBLEM_WORKLOG_ID", nullable = false)
  private Long problemWorklogId;

  @Column(name = "CREATE_USER_ID", nullable = false)
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

  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "PROBLEM_ID", nullable = false)
  private Long problemId;

  public ProblemWorklogDTO toDTO() {
    ProblemWorklogDTO dto = new ProblemWorklogDTO(
        problemWorklogId,
        createUserId,
        createUserName,
        createUnitId,
        createUnitName,
        worklog,
        description,
        createdTime,
        problemId
    );
    return dto;
  }
}
