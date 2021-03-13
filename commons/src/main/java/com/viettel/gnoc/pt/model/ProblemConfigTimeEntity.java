package com.viettel.gnoc.pt.model;
import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.pt.dto.ProblemConfigTimeDTO;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "one_tm", name = "CFG_TIME_PROBLEM_PROCESS")
public class ProblemConfigTimeEntity {
  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CFG_TIME_PROBLEM_PROCESS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")

  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "REASON_GROUP_ID")
  private Long reasonGroupId;

  @Column(name = "SOLUTION_TYPE_ID")
  private Long solutionTypeId;

  @Column(name = "TIME_PROCESS")
  private Long timeProcess;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATE_DATE")
  private Date createDate;

  @Column(name = "TYPE_ID")
  private Long typeId;

  @Column(name = "SUB_CATEGORY_ID")
  private Long subCategoryId;

  public ProblemConfigTimeDTO toDTO(){
    return new ProblemConfigTimeDTO(id, reasonGroupId,solutionTypeId, timeProcess, createDate, typeId, subCategoryId);
  }
}
