package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.sr.model.SRCatalogChildEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SRCatalogChildDTO extends BaseDto {

  private Long childId;
  private Long serviceId;
  private String serviceCode;
  private Long serviceIdChild;
  private String serviceCodeChild;
  private Long autoCreateSR;
  private String updatedUser;
  private Date updatedTime;
  private String serviceNameChild;
  private String executionUnitChild;
  private String status;
  private String defaultSortField;
  private String executionUnitChildName;
  private String executionUnitParent;
  private String action;
  private String actionName;
  private String resultImport;

  private String executionUnitDesc;
  //  dung add
  private String commentAuto;
// dung add 17082020
  private Long generateNo;
//  end

  public SRCatalogChildDTO(Long childId, Long serviceId, String serviceCode,
      Long serviceIdChild, String serviceCodeChild, Long autoCreateSR, String updatedUser,
      Date updatedTime, String commentAuto,Long generateNo) {
    this.childId = childId;
    this.serviceId = serviceId;
    this.serviceCode = serviceCode;
    this.serviceIdChild = serviceIdChild;
    this.serviceCodeChild = serviceCodeChild;
    this.autoCreateSR = autoCreateSR;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
    this.commentAuto = commentAuto;
    this.generateNo = generateNo;
  }

  public SRCatalogChildEntity toEntity() {
    SRCatalogChildEntity entity = new SRCatalogChildEntity(
        childId
        , serviceId
        , serviceCode
        , serviceIdChild
        , serviceCodeChild
        , autoCreateSR
        , updatedUser
        , updatedTime
        , commentAuto
        ,generateNo
    );
    return entity;
  }
}
