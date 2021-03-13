package com.viettel.gnoc.cr.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TempImportDTOResult {

  private Long tempImportId;
  private String code;
  private String name;
  private Long totalColumn;
  private String title;
  private Long createrId;
  private Date createrTime;
  private Long processTypeId;
  private Long isActive;
  private Long webserviceMethodId;
  private Long isValidateInput;
  private Long isRevert;
  private Long isMecFile;
  private String path;
  private Long isValidateOutput;
  private Long bayId;
  private Long ctkId;
}
