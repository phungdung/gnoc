package com.viettel.gnoc.commons.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ScheduleCRFormDTO extends BaseDto {

  private String id;
  private Long idCr;
  private String idSchedule;
  private String codeCR;
  private String crArray;
  private String crChildren;
  private String crDeadline;
  private Long crLevel;
  private String executionTime;
  private Long crPrioritize;
  private String registrationDate;
  private String status;
  private Date startDate;
  private Date endDate;
  private String crPerformer;
  private String type;
  private String impactNodeList;//danh sach node mang
  private String affectServiceList;//danh sach dich vu
  private String crPrioritizeName;
  private String forbiddenDate;//ngay cam
  private Long isChecked;
  private String crLevelName;
  private String isFixedDay;
  private String crChildrenName;
  private String crArrayName;
}
