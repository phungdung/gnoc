package com.viettel.gnoc.cr.dto;

import java.util.List;
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
public class CrOutputDataDTO {

  private String crId;
  private String crNumber;
  private String title;
  private String creatorId;
  private String excutorId;
  private String changeResponsibleUnitId;
  private String changeResponsibleUnit;
  private String changeResponsibleUserId;
  private String changeResposibleUserName;
  private String startDate;
  private String endDate;
  private String updateTime;
  private String tempImportCode;
  private String tempImportName;
  private String tempImportId;
  private String changeOriginator;
  private String changeOriginatorUnit;
  private String state;
  private List<CrTemplateDataDTO> listData;
}
