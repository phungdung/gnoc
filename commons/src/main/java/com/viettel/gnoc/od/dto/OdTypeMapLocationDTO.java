package com.viettel.gnoc.od.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.od.model.OdTypeMapLocationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OdTypeMapLocationDTO extends BaseDto {

  private Long id;
  private Long odTypeId;
  private String locationCode;
  private Long createUnitId;
  private String createUnitCode;
  private String createUnitName;
  private Long receiveUnitId;
  private String receiveUnitCode;
  private String receiveUnitName;
  private String locationName;

  public OdTypeMapLocationEntity toEntity() {
    return new OdTypeMapLocationEntity(id, odTypeId, locationCode, createUnitId, receiveUnitId);
  }

  public OdTypeMapLocationDTO(Long id, Long odTypeId, String locationCode, Long createUnitId,
      Long receiveUnitId) {
    this.id = id;
    this.odTypeId = odTypeId;
    this.locationCode = locationCode;
    this.createUnitId = createUnitId;
    this.receiveUnitId = receiveUnitId;
  }

}
