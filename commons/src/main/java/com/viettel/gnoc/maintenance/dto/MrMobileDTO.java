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
public class MrMobileDTO {

  //Fields

  private String mrId;
  private String subcategory;
  private String mrWorks;
  private String mrTechnichcal;
  private String mrTitle;
  private String mrType;
  private String state;
  private String priorityCode;
  private String earliestTimeFrom;
  private String earliestTimeTo;
  private String lastestTimeFrom;
  private String lastestTimeTo;
  private String country;
  private String region;
  private String circle;
  private String isServiceAffected;
  private String affectedServiceId;
  private String impact;
  private String mrCode;
  private String nodeType;
  private String nodeName;
  private String isApprove;
  private String unitCode;

  private String userName;
  private String unitId;
  private String userId;

  public MrSearchDTO convertDTO(MrMobileDTO mdto) {
    MrSearchDTO dto = new MrSearchDTO();
    dto.setMrId(mdto.getMrId());
    dto.setMrTechnichcal(mdto.getMrTechnichcal());
    dto.setMrTitle(mdto.getMrTitle());
    dto.setMrType(mdto.getMrType());
    dto.setSubcategory(mdto.getSubcategory());
    dto.setMrWorks(mdto.getMrWorks());
    dto.setState(mdto.getState());
    dto.setEarliestTimeFrom(mdto.getEarliestTimeFrom());
    dto.setEarliestTimeTo(mdto.getEarliestTimeTo());
    dto.setLastestTimeFrom(mdto.getLastestTimeFrom());
    dto.setLastestTimeTo(mdto.getLastestTimeTo());
    dto.setPriorityCode(mdto.getPriorityCode());
    dto.setCountry(mdto.getCountry());
    dto.setRegion(mdto.getRegion());
    dto.setCircle(mdto.getCircle());
    dto.setImpact(mdto.getImpact());
    dto.setIsServiceAffected(mdto.getIsServiceAffected());
    dto.setAffectedServiceId(mdto.getAffectedServiceId());
    dto.setNodeType(mdto.getNodeType());
    dto.setNodeName(mdto.getNodeName());
    dto.setMrCode(mdto.getMrCode());
    dto.setIsApprove(mdto.getIsApprove());
    dto.setUnitCode(mdto.getUnitCode());
    dto.setUnitId(mdto.getUnitId());
    return dto;
  }
}
