package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
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
public class InfraSleevesDTO extends BaseDto {

  protected Date createDate;
  protected Long deptpartmentId;
  protected String description;
  protected Long designProjectId;
  protected Long isDesigning;
  protected Long isSplitted;
  protected Date modifyDate;
  protected Long ownerId;
  protected Long pillarId;
  protected Long poolId;
  protected Long projectId;
  protected Long purpose;
  protected Long remainPorts;
  protected String result;
  protected String serial;
  protected String sleeveCode;
  protected Long sleeveId;
  protected String sleeveName;
  protected Long sleeveTypeId;
  protected String status;
  protected Long supplierId;
  protected Long vendorId;

  public InfraSleevesDTO(Date modifyDate, String sleeveCode, Long sleeveId, String sleeveName,
      String status) {
    this.modifyDate = modifyDate;
    this.sleeveCode = sleeveCode;
    this.sleeveId = sleeveId;
    this.sleeveName = sleeveName;
    this.status = status;
  }
}

