package com.viettel.gnoc.od.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TienNV
 */
@Getter
@Setter
public class OdExportCfgBusinessDTO extends OdChangeStatusDTO {

  private Long isRequired;
  private Long editable;
  private Long isVisible;
  private String isRequiredName;
  private String isEditableName;
  private String isVisibleName;
  private String message;
  private String columnName;
  private String columnNameValue;
  private String odTypeCode;
  private String resultImport;
  private String groupCfg;
}
