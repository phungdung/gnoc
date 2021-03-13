package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.ScheduleCREntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ScheduleCRDTO extends BaseDto {

  private Long id;
  private Long idCr;
  private Long idSchedule;
  private String codeCR;
  private String crArray;
  private String crChildren;
  private Date crDeadline;
  private Long crLevel;
  private String executionTime;
  private Long crPrioritize;
  private Date registrationDate;
  private Long status;
  private Date startDate;
  private Date endDate;
  private String crPerformer;
  private Long type;
  private String impactNodeList;//danh sach node mang
  private String affectServiceList;//danh sach dich vu
  private String crPrioritizeName;
  private String forbiddenDate;//ngay cam
  private String isFixedDay;
  private String crLevelName;

  //tiennv them
  private String crChildrenName;
  private String crArrayName;
  private String crPerformerName;

  public ScheduleCRDTO(Long idCr, Long idSchedule, String codeCR, String crArray, String crChildren,
      Date crDeadline, Long crLevel, String executionTime,
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

  public ScheduleCREntity toEntity() {
    return new ScheduleCREntity(
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
