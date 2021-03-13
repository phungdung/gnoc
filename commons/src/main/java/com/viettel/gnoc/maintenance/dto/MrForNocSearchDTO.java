/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.maintenance.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ITSOL
 */
@Getter
@Setter
@NoArgsConstructor
public class MrForNocSearchDTO {

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
  private String createTimeFrom;
  private String createTimeTo;
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

  private String userName;
  private String locale;
  private String isContainChild;

}
