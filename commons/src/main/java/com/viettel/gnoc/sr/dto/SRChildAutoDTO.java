package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.sr.model.SRChildAutoEntity;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SRChildAutoDTO extends BaseDto {

  private Long id;
  private String srParentCode;
  private Long serviceId;
  private String roleCode;
  private Long generateNo;
  private String srChildCode;
  private String createUser;
  private String serviceGroup;
  private String serviceArray;
  private String country;
  private String title;
  private String description;
  private Long createSR;
  private Date endTime;
  private Long srUnit;
  private String status;
  private String commentAuto;

  public SRChildAutoDTO(Long id, String srParentCode, Long serviceId, String roleCode,
      Long generateNo, String srChildCode, String createUser, String serviceGroup,
      String serviceArray, String country, String title, String description, Long createSR,
      Date endTime, Long srUnit, String commentAuto) {
    this.id = id;
    this.srParentCode = srParentCode;
    this.serviceId = serviceId;
    this.roleCode = roleCode;
    this.generateNo = generateNo;
    this.srChildCode = srChildCode;
    this.createUser = createUser;
    this.serviceGroup = serviceGroup;
    this.serviceArray = serviceArray;
    this.country = country;
    this.title = title;
    this.description = description;
    this.createSR = createSR;
    this.endTime = endTime;
    this.srUnit = srUnit;
    this.commentAuto = commentAuto;
  }

  public SRChildAutoEntity toEntity() {
    return new SRChildAutoEntity(id, srParentCode, serviceId, roleCode, generateNo, srChildCode,
        createUser, serviceGroup, serviceArray, country, title, description, createSR, endTime,
        srUnit, commentAuto);
  }
}
