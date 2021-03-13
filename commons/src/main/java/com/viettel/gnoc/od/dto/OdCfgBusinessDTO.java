package com.viettel.gnoc.od.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.od.model.OdCfgBusinessEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OdCfgBusinessDTO extends BaseDto {

  private Long id;
  private Long odChangeStatusId;
  private String columnName;
  private Long isRequired;
  private Long editable;
  private Long isVisible;
  private String message;
  private String columnId;
  private String columnNameValue;


  public OdCfgBusinessDTO(Long id, Long odChangeStatusId, String columnName,
      Long isRequired, Long editable, Long isVisible, String message) {
    this.id = id;
    this.odChangeStatusId = odChangeStatusId;
    this.columnName = columnName;
    this.isRequired = isRequired;
    this.editable = editable;
    this.isVisible = isVisible;
    this.message = message;
  }

  public OdCfgBusinessEntity toEntity() {
    return new OdCfgBusinessEntity(id, odChangeStatusId, columnName, isRequired, editable,
        isVisible, message);
  }
}
