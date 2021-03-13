package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.maintenance.model.MrEntity;
import com.viettel.gnoc.wfm.dto.WoSearchDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class MrInsideDTO extends BaseDto {

  private Long mrId;
  private String mrTechnichcal;
  private String mrTitle;
  private String mrType;
  private String subcategory;
  private String description;
  private String mrWorks;
  private String unitApprove;
  private String unitExecute;
  private String assignToPerson;
  private String personAccept;
  private String state;
  private Date earliestTime;
  private Date lastestTime;
  private String interval;
  private Date nextWoCreate;
  private String priorityCode;
  private String country;
  private String region;
  private String circle;
  private String impact;
  private String isServiceAffected;
  private String affectedServiceId;
  private String nodeType;
  private String nodeName;
  private String notes;
  private String woId;
  private String crId;
  private String mrCode;
  private Long createPersonId;
  private Date createdTime;
  private Long cycle;
  private Double delta;
  private List<InfraDeviceDTO> lstNode;
  private String workOrderField;
  private String workContent;
  private String workParent;
  private String woType;
  private String cdGroupWoName;
  private String systemCode;
  private Date startDateMr;
  private Date endDateMr;
  private ArrayList<String> url;
  private Boolean isTP;
  private MrApprovalDepartmentDTO mrApprovalDepartmentDTO;
  private String mrContentId;
  private Date endTime;
  private Date startTime;
  private String reason;
  private String reasonDetail;
  private WoSearchDTO woSearchDTO;
  private String createPersonName;

  public MrInsideDTO(Long mrId) {
    this.mrId = mrId;
  }

  public MrInsideDTO(Long mrId, String mrTechnichcal, String mrTitle, String mrType,
      String subcategory, String description, String mrWorks, String unitApprove,
      String unitExecute, String assignToPerson, String personAccept, String state,
      Date earliestTime, Date lastestTime, String interval, Date nextWoCreate,
      String priorityCode, String country, String region, String circle, String impact,
      String isServiceAffected, String affectedServiceId, String nodeType, String nodeName,
      String notes, String woId, String crId, String mrCode, Long createPersonId,
      Date createdTime, Long cycle, Double delta) {
    this.mrId = mrId;
    this.mrTechnichcal = mrTechnichcal;
    this.mrTitle = mrTitle;
    this.mrType = mrType;
    this.subcategory = subcategory;
    this.description = description;
    this.mrWorks = mrWorks;
    this.unitApprove = unitApprove;
    this.unitExecute = unitExecute;
    this.assignToPerson = assignToPerson;
    this.personAccept = personAccept;
    this.state = state;
    this.earliestTime = earliestTime;
    this.lastestTime = lastestTime;
    this.interval = interval;
    this.nextWoCreate = nextWoCreate;
    this.priorityCode = priorityCode;
    this.country = country;
    this.region = region;
    this.circle = circle;
    this.impact = impact;
    this.isServiceAffected = isServiceAffected;
    this.affectedServiceId = affectedServiceId;
    this.nodeType = nodeType;
    this.nodeName = nodeName;
    this.notes = notes;
    this.woId = woId;
    this.crId = crId;
    this.mrCode = mrCode;
    this.createPersonId = createPersonId;
    this.createdTime = createdTime;
    this.cycle = cycle;
    this.delta = delta;
  }

  public MrEntity toEntity() {
    return new MrEntity(mrId, mrTechnichcal, mrTitle, mrType, subcategory, description, mrWorks,
        unitApprove, unitExecute, assignToPerson, personAccept, state, earliestTime, lastestTime,
        interval, nextWoCreate, priorityCode, country, region, circle, impact, isServiceAffected,
        affectedServiceId, nodeType, nodeName, notes, woId, crId, mrCode, createPersonId,
        createdTime, cycle, delta);
  }
}
