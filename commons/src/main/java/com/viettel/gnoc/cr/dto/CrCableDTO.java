package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.model.CrCableEntity;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class CrCableDTO extends BaseDto {

  //Fields
  private String id;
  private String crId;
  private String cableCode;
  private String startPoint;
  private String endPoint;
  private String nationCode;
  private String createdDate;
  private String checkbox;
  private String type;
  private String typeName;
  private String laneCode;
  private List<String> lstCableCode;
  private String defaultSortField;

  public CrCableDTO(String id, String crId, String cableCode, String startPoint, String endPoint,
      String nationCode, String createdDate,
      String type) {
    this.id = id;
    this.crId = crId;
    this.cableCode = cableCode;
    this.startPoint = startPoint;
    this.endPoint = endPoint;
    this.nationCode = nationCode;
    this.createdDate = createdDate;
    this.type = type;
    setDefaultSortField("name");
  }

  public CrCableEntity toEntity() {
    CrCableEntity model = new CrCableEntity(
        !StringUtils.validString(id) ? null : Long.valueOf(id),
        !StringUtils.validString(crId) ? null : Long.valueOf(crId),
        cableCode,
        startPoint,
        endPoint,
        nationCode,
        !StringUtils.validString(createdDate) ? null
            : DateTimeUtils.convertStringToDate(createdDate),
        !StringUtils.validString(type) ? null : Long.valueOf(type)
    );
    return model;
  }

  public CrCableDTO() {
    setDefaultSortField("name");
  }

}
