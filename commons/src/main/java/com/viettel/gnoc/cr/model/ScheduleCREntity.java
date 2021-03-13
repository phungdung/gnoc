package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.ScheduleCRDTO;
import java.util.Date;
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

@Entity
@Table(schema = "COMMON_GNOC", name = "SCHEDULE_CR")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleCREntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SCHEDULE_CR_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;
  @Column(name = "CR_ID", nullable = false)
  private Long idCr;
  @Column(name = "ID_SCHEDULE")
  private Long idSchedule;
  @Column(name = "CR_NUMBER")
  private String codeCR;
  @Column(name = "IMPACT_SEGMENT")
  private String crArray;
  @Column(name = "IMPACT_SEGMENT_CHILD")
  private String crChildren;
  @Column(name = "DEADLINE")
  private Date crDeadline;
  @Column(name = "CR_LEVEL")
  private Long crLevel;
  @Column(name = "EXECUTION_TIME")
  private String executionTime;
  @Column(name = "PRIORITY")
  private Long crPrioritize;
  @Column(name = "REGISTRATION_DATE")
  private Date registrationDate;
  @Column(name = "STATUS")
  private Long status;
  @Column(name = "START_DATE")
  private Date startDate;
  @Column(name = "END_DATE")
  private Date endDate;
  @Column(name = "CR_PERFORMER")
  private String crPerformer;
  @Column(name = "TYPE")
  private Long type;
  @Column(name = "FORBIDDEN_DATE")
  private String forbiddenDate;
  @Column(name = "IMPACT_NODE_LIST")
  private String impactNodeList;
  @Column(name = "AFFECT_SERVICE_LIST")
  private String affectServiceList;

  public ScheduleCREntity(Long idCr, Long idSchedule, String codeCR, String crArray,
      String crChildren, Date crDeadline, Long crLevel, String executionTime,
      Long crPrioritize, Date registrationDate, Long status, Date startDate, Date endDate,
      String crPerformer, Long type, String forbiddenDate, String impactNodeList,
      String affectServiceList, Long id) {
    this.idCr = idCr;
    this.idSchedule = idSchedule;
    this.codeCR = codeCR;
    this.crArray = crArray;
    this.crChildren = crChildren;
    this.crDeadline = crDeadline;
    this.crLevel = crLevel;
    this.executionTime = executionTime;
    this.crPrioritize = crPrioritize;
    this.registrationDate = registrationDate;
    this.status = status;
    this.startDate = startDate;
    this.endDate = endDate;
    this.crPerformer = crPerformer;
    this.type = type;
    this.forbiddenDate = forbiddenDate;
    this.impactNodeList = impactNodeList;
    this.affectServiceList = affectServiceList;
    this.id = id;
  }

  public ScheduleCRDTO toDTO() {
    return new ScheduleCRDTO(
        idCr,
        idSchedule,
        codeCR,
        crArray,
        crChildren,
        crDeadline,
        crLevel,
        executionTime,
        crPrioritize,
        registrationDate,
        status,
        startDate,
        endDate,
        crPerformer,
        type,
        forbiddenDate,
        impactNodeList,
        affectServiceList,
        id
    );
  }
}
