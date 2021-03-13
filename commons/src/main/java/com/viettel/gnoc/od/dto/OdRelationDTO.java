/**
 * @(#)OdRelationForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.od.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.od.model.OdRelationEntity;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OdRelationDTO extends BaseDto {

  //Fields
  private Long id;
  private Long odId;
  private String system;
  private String systemCode;
  private Long systemId;
  private Date createTime;
  private Date endTime;
  private String content;
  private Long createPersonId;
  private Long receiveUnitId;
  private String createPersonName;
  private String receiveUnitName;
  private String isEnable;
  private String status;

  private String startTimeFrom;
  private String startTimeTo;
  private Long offset;

  public OdRelationEntity toEntity() {
    return new OdRelationEntity(id, odId, system, systemCode, systemId, createTime, endTime,
        content, createPersonId, receiveUnitId);
  }

  public OdRelationDTO(String title, String createPersonName, Date createTime, Date endTime,
      String system, String status,
      String systemCode, Long systemId, String receiveUnitName, Long createPersonId,
      Long receiveUnitId, int totalRow) {
    this.content = title;
    this.createPersonName = createPersonName;
    this.createTime = createTime;
    this.endTime = endTime;
    this.system = system;
    this.status = status;
    this.systemCode = systemCode;
    this.systemId = systemId;
    this.receiveUnitName = receiveUnitName;
    this.createPersonId = createPersonId;
    this.receiveUnitId = receiveUnitId;
    this.setTotalRow(totalRow);
  }

  public OdRelationDTO(Long id, Long odId, String system, String systemCode, Long systemId,
      Date createTime, Date endTime, String content, Long createPersonId,
      Long receiveUnitId) {
    this.id = id;
    this.odId = odId;
    this.system = system;
    this.systemCode = systemCode;
    this.systemId = systemId;
    this.createTime = createTime;
    this.endTime = endTime;
    this.content = content;
    this.createPersonId = createPersonId;
    this.receiveUnitId = receiveUnitId;
  }

  public WoInsideDTO toWoInsideDTO() {
    WoInsideDTO woInsideDTO = new WoInsideDTO();
    woInsideDTO.setPage(this.getPage());
    woInsideDTO.setPageSize(this.getPageSize());
    woInsideDTO.setOffset(this.offset);
    woInsideDTO.setWoCode(this.systemCode);
    woInsideDTO.setStartTimeFrom(StringUtils.validString(this.startTimeFrom) ? DateTimeUtils
        .convertStringToDate(this.startTimeFrom)
        : null);
    woInsideDTO.setStartTimeTo(StringUtils.validString(this.startTimeTo) ? DateTimeUtils
        .convertStringToDate(this.startTimeTo)
        : null);
    return woInsideDTO;
  }

  public CrDTO toCrDTO() {
    CrDTO crDTO = new CrDTO();
    crDTO.setCreatedDateFrom(this.startTimeFrom);
    crDTO.setCreatedDateTo(this.startTimeTo);
    crDTO.setCrNumber(this.systemCode);
    return crDTO;
  }

  public SRDTO toSrDTO() {
    SRDTO srdto = new SRDTO();
    srdto.setCreateFromDate(this.startTimeFrom);
    srdto.setCreateToDate(this.startTimeTo);
    srdto.setSrCode(this.systemCode);
    return srdto;
  }
}
