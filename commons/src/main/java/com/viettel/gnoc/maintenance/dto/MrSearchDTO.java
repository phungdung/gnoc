package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
@NoArgsConstructor
public class MrSearchDTO extends BaseDto {

  //Fields
  private String mrId;
  private String mrTechnichcal;
  private String mrTitle;
  private String mrType;
  private String subcategory;
  private String mrWorks;
  private String state;
  private String earliestTimeFrom;
  private String earliestTimeTo;
  private String lastestTimeFrom;
  private String lastestTimeTo;
  private String priorityCode;
  private String country;
  private String region;
  private String circle;
  private String impact;
  private String isServiceAffected;
  private String affectedServiceId;
  private String nodeType;
  private String nodeName;
  private String mrCode;
  private String isApprove;
  private String unitCode;
  private String unitId;
  private String createPersonId;
  private String interval;

  //<editor-fold desc="namtn adding Loai BD, chu ky, don vi thuc hien tham dinh CR, CD tao WO, ngay tao tu ngay den ngay,thoi gian bat dau ket thuc MR, don vi tao MR, ngay dong MR">
  private String mrTypeName;
  private String cycle;
  private String responsibleUnitCRName;
  private String responsibleUnitCR;
  private String considerUnitCRName;
  private String considerUnitCR;
  private String cdGroupWoName;
  private String cdGroupWo;
  private String dateCreateWoFrom;
  private String dateCreateWoTo;
  private String startDateMr;
  private String endDateMr;
  private String unitCreateMr;
  private String mrCloseDate;

  private String note;
  //duongnt edit
  private String parent_Consider;
  private String parent_Responsible;
  private String status_CR_WO;
  //duongnt edit

  private String ftId;
  private String unitCreateMrName;

  public MrSearchDTO(String mrId, String mrTechnichcal, String mrTitle, String mrType,
      String subcategory, String mrWorks, String state, String earliestTimeFrom,
      String earliestTimeTo, String lastestTimeFrom, String lastestTimeTo, String priorityCode,
      String country, String region, String circle, String impact, String isServiceAffected,
      String affectedServiceId, String nodeType, String nodeName, String mrCode, String isApprove,
      String unitCode, String unitId) {
    this.mrId = mrId;
    this.mrTechnichcal = mrTechnichcal;
    this.mrTitle = mrTitle;
    this.mrType = mrType;
    this.subcategory = subcategory;
    this.mrWorks = mrWorks;
    this.state = state;
    this.earliestTimeFrom = earliestTimeFrom;
    this.earliestTimeTo = earliestTimeTo;
    this.lastestTimeFrom = lastestTimeFrom;
    this.lastestTimeTo = lastestTimeTo;
    this.priorityCode = priorityCode;
    this.country = country;
    this.region = region;
    this.circle = circle;
    this.impact = impact;
    this.isServiceAffected = isServiceAffected;
    this.affectedServiceId = affectedServiceId;
    this.nodeType = nodeType;
    this.nodeName = nodeName;
    this.mrCode = mrCode;
    this.isApprove = isApprove;
    this.unitCode = unitCode;
    this.unitId = unitId;
  }

}
