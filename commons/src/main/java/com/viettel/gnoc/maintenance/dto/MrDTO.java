
/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrEntity;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class MrDTO extends BaseDto implements Cloneable {

  private String affectedServiceId;
  private String assignToPerson;
  private String cdGroupWo;
  private String cdGroupWoName;
  private String cdId;
  private String changeResponsible;
  private String circle;
  private String considerUnitCR;
  private String considerUnitCRName;
  private String country;
  private String countryName;
  private String crId;
  private String crNumber;
  private String createPersonId;
  private String createdTime;
  private String cycle;
  private String dateCreateWoFrom;
  private String dateCreateWoTo;
  private String delta;
  private String description;
  private String earliestTime;
  private String endDateMr;
  private String ftId;
  private String impact;
  private String interval;
  private String isServiceAffected;
  private String lastestTime;
  private String mrCloseDate;
  private String mrCode;
  private String mrContentId;
  private String mrId;
  private String mrTechnichcal;
  private String mrTitle;
  private String mrType;
  private String mrTypeName;
  private String mrWorks;
  private String nextWoCreate;
  private String nodeName;
  private String nodeType;
  private String note;
  private String notes;
  private String personAccept;
  private String priorityCode;
  private String region;
  private String regionName;
  private String responsibleUnitCR;
  private String responsibleUnitCRName;
  private String startDateMr;
  private String state;
  private String subcategory;
  private String unitApprove;
  private String unitCreateMr;
  private String unitCreateMrName;
  private String unitExecute;
  private String woId;
  private String defaultSortField;
  private String assignToPersonName;
  private String unitName;
  private String unitParentName;
  private String unitParentName2;
  private WoDTOSearch woDTOSearch;
  private String nameCode;
  private String ip;
  private String nationCode;
  private String workOrderField;
  private String workContent;
  private String workParent;
  private String woType;
  private String systemCode;
  private ArrayList<String> url;
  private MrApprovalDepartmentDTO mrApprovalDepartmentDTO;
  private MrInsideDTO mrDto1;
  private MrInsideDTO mrDTO;
  private Boolean isTP;
  private List<InfraDeviceDTO> lstNode;
  private String woCode;

  //  duongnt
  private String changeResponsibleUnit;
  private String considerUnitId;
  private String woCreateDate;

  @Override
  public MrDTO clone() {
    try {
      return (MrDTO) super.clone();
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }
  }

  public MrEntity toEntity() {
    try {
      MrEntity model = new MrEntity(
          StringUtils.validString(mrId)
              ? Long.valueOf(mrId) : null,
          mrTechnichcal,
          mrTitle,
          mrType,
          subcategory,
          description,
          mrWorks,
          unitApprove,
          unitExecute,
          assignToPerson,
          personAccept,
          state,
          StringUtils.validString(earliestTime)
              ? DateTimeUtils.convertStringToDate(earliestTime) : null,
          StringUtils.validString(lastestTime)
              ? DateTimeUtils.convertStringToDate(lastestTime) : null,
          interval,
          StringUtils.validString(nextWoCreate)
              ? DateTimeUtils.convertStringToDate(nextWoCreate) : null,
          priorityCode,
          country,
          region,
          circle,
          impact,
          isServiceAffected,
          affectedServiceId,
          nodeType,
          nodeName,
          notes,
          woId,
          crId,
          mrCode,
          StringUtils.validString(createPersonId) ? Long.valueOf(createPersonId) : null,
          StringUtils.validString(createdTime) ? DateTimeUtils.convertStringToDate(createdTime)
              : null,
          StringUtils.validString(cycle) ? Long.valueOf(cycle) : null,
          StringUtils.validString(delta) ? Double.valueOf(delta) : null);
      return model;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }
}
