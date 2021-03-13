/**
 * @(#)CrImpactFrameForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */

package com.viettel.gnoc.cr.dto;


import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;


@XmlRootElement(name = "CrImpactFrame")
@Getter
@Setter
public class CrImpactFrameDTO {

  //Fields
  private String ifeId;
  private String ifeCode;
  private String ifeName;
  private String startTime;
  private String endTime;
  private String description;
  private String defaultSortField;
  private String createdTime;
  private String updatedTime;
  private String createdUser;
  private String updatedUser;

  // private static long changedTime = 0;
  //Constructor
  public CrImpactFrameDTO() {
    setDefaultSortField("ifeName");
  }

  public CrImpactFrameDTO(String ifeId, String ifeCode, String ifeName, String startTime,
      String endTime, String description, String createdTime, String updatedTime, String createdUser, String updatedUser) {
    this.ifeId = ifeId;
    this.ifeCode = ifeCode;
    this.ifeName = ifeName;
    this.startTime = startTime;
    this.endTime = endTime;
    this.description = description;
    this.createdTime = createdTime;
    this.updatedTime = updatedTime;
    this.createdUser = createdUser;
    this.updatedUser = updatedUser;
  }
  //Getters and setters

  public CrImpactFrameInsiteDTO toInSiteDTO() throws Exception {
    CrImpactFrameInsiteDTO dto = new CrImpactFrameInsiteDTO();
    dto.setImpactFrameId(this.ifeId == null ? null : Long.valueOf(this.ifeId));
    dto.setImpactFrameCode(this.ifeCode);
    dto.setImpactFrameName(this.ifeName);
    dto.setStartTime(this.startTime);
    dto.setEndTime(this.endTime);
    dto.setDescription(this.description);
    dto.setCreatedTime(StringUtils.isStringNullOrEmpty(this.createdTime) ? null : DateUtil.string2DateTime(this.createdTime));
    dto.setUpdatedTime(StringUtils.isStringNullOrEmpty(this.updatedTime) ? null : DateUtil.string2DateTime(this.updatedTime));
    dto.setCreatedUser(this.createdUser);
    dto.setUpdatedUser(this.updatedUser);
    return dto;
  }

}

