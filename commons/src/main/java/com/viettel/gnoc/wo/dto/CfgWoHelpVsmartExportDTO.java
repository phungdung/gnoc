package com.viettel.gnoc.wo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CfgWoHelpVsmartExportDTO {

  private Long id;
  private Long systemId;
  private String typeId;
  private String fileId;
  private String typeName;
  private String systemName;
  private String resultImport;
}
