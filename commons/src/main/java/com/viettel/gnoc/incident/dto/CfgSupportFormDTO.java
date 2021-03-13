package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CfgSupportFormDTO extends BaseDto {

  private String id;
  private String woCode;
  private String cfgSupportCaseID;
  private String caseName;
  private String testCaseName;
  private String testCaseId;
  private String result;
  private String description;
  private String fileName;
  private Date updateTime;
}
