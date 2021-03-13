package com.viettel.gnoc.cr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class V_TempImportColDTO {

  private Long tempImportColId;
  private String code;
  private String title;
  private Long isMerge;
  private Long colPosition;
  private Long tempImportId;
  private int lastRow;
  private Long methodParameterId;
  private String parameterName;
  private Long dataType;
}
