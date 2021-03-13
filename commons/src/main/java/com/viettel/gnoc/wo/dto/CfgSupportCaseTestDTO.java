package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.CfgSupportCaseTestEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ITSOL
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CfgSupportCaseTestDTO extends BaseDto {

  private Long id;
  private Long cfgSuppportCaseId;
  private String testCaseName;
  private Long fileRequired;
  private Boolean isDelete = false;

  public CfgSupportCaseTestEntity toEntity() {
    return new CfgSupportCaseTestEntity(id, cfgSuppportCaseId, testCaseName, fileRequired);
  }

  public CfgSupportCaseTestDTO(Long id, Long cfgSuppportCaseId, String testCaseName,
      Long fileRequired) {
    this.id = id;
    this.cfgSuppportCaseId = cfgSuppportCaseId;
    this.testCaseName = testCaseName;
    this.fileRequired = fileRequired;
  }
}
