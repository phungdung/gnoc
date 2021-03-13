package com.viettel.gnoc.maintenance.model;

import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(schema = "OPEN_PM", name = "MR")
public class MrEntity {

  @Id
  @Column(name = "MR_ID", unique = true, nullable = false)
  private Long mrId;
  @Column(name = "MR_TECHNICHCAL")
  private String mrTechnichcal;
  @Column(name = "MR_TITLE")
  private String mrTitle;
  @Column(name = "MR_TYPE")
  private String mrType;
  @Column(name = "SUBCATEGORY")
  private String subcategory;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "MR_WORKS")
  private String mrWorks;
  @Column(name = "UNIT_APPROVE")
  private String unitApprove;
  @Column(name = "UNIT_EXECUTE")
  private String unitExecute;
  @Column(name = "ASSIGN_TO_PERSON")
  private String assignToPerson;
  @Column(name = "PERSON_ACCEPT")
  private String personAccept;
  @Column(name = "STATE")
  private String state;
  @Column(name = "EARLIEST_TIME")
  private Date earliestTime;
  @Column(name = "LASTEST_TIME")
  private Date lastestTime;
  @Column(name = "INTERVAL")
  private String interval;
  @Column(name = "NEXT_WO_CREATE")
  private Date nextWoCreate;
  @Column(name = "PRIORITY_CODE")
  private String priorityCode;
  @Column(name = "COUNTRY")
  private String country;
  @Column(name = "REGION")
  private String region;
  @Column(name = "CIRCLE")
  private String circle;
  @Column(name = "IMPACT")
  private String impact;
  @Column(name = "IS_SERVICE_AFFECTED")
  private String isServiceAffected;
  @Column(name = "AFFECTED_SERVICE_ID")
  private String affectedServiceId;
  @Column(name = "NODE_TYPE")
  private String nodeType;
  @Column(name = "NODE_NAME")
  private String nodeName;
  @Column(name = "NOTES")
  private String notes;
  @Column(name = "WO_ID")
  private String woId;
  @Column(name = "CR_ID")
  private String crId;
  @Column(name = "MR_CODE")
  private String mrCode;
  @Column(name = "CREATE_PERSON_ID")
  private Long createPersonId;
  @Column(name = "CREATED_TIME")
  private Date createdTime;
  @Column(name = "CYCLE")
  private Long cycle;
  @Column(name = "DELTA")
  private Double delta;

  public MrInsideDTO toDTO() {
    return new MrInsideDTO(mrId, mrTechnichcal, mrTitle, mrType, subcategory, description, mrWorks,
        unitApprove, unitExecute, assignToPerson, personAccept, state, earliestTime, lastestTime,
        interval, nextWoCreate, priorityCode, country, region, circle, impact, isServiceAffected,
        affectedServiceId, nodeType, nodeName, notes, woId, crId, mrCode, createPersonId,
        createdTime, cycle, delta);
  }
}
