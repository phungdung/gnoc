package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class CrTemplateDataDTO extends BaseDto {

  private String crId;
  private String tempImportColumnId;
  private String tempImportId;
  private String tempImportCode;
  private String tempImportName;
  private String tempImportColName;
  private String tempImportColCode;
  private String tempImportColValue;
  private String tempImportRowOrder;
  private String tempImportColumnPos;
  private String templateResult;
  private String templateField;
  private String description;
}
