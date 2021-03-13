package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.incident.model.TTCfgBusinessEntity;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class TTCfgBusinessDTO {

  private Long id;
  private Long ttChangeStatusId;
  private String columnName;
  private Long isRequired;
  private Long editable;
  private Long isVisible;
  private String defaultValue;
  private Long typeControl;
  private Long isParent;
  private Long stt;

  private Long typeControlName;
  private String columnId;
  private String columnNameValue;
  private String itemValue;
  private String keyCode;
  private Long scopeOfUse;
  private String colNameRelate;
  private String itemNameValue;

  private List<ItemDataTT> lstData;

  public TTCfgBusinessDTO(Long id, Long ttChangeStatusId, String columnName,
      Long isRequired, Long editable, Long isVisible, String defaultValue, Long typeControl,
      Long isParent, Long stt, Long scopeOfUse, String colNameRelate) {
    this.id = id;
    this.ttChangeStatusId = ttChangeStatusId;
    this.columnName = columnName;
    this.isRequired = isRequired;
    this.editable = editable;
    this.isVisible = isVisible;
    this.defaultValue = defaultValue;
    this.typeControl = typeControl;
    this.isParent = isParent;
    this.stt = stt;
    this.scopeOfUse = scopeOfUse;
    this.colNameRelate = colNameRelate;
  }

  public TTCfgBusinessEntity toEntity() {
    return new TTCfgBusinessEntity(id, ttChangeStatusId, columnName, isRequired, editable,
        isVisible, defaultValue, typeControl, isParent, stt, scopeOfUse, colNameRelate);
  }
}
