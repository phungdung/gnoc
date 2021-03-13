/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tiennv
 */
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class MrCfgProcedureBtsExportDTO {

  private String cfgProcedureBtsId;
  private String marketCode;
  private String deviceType;
  private String cycle;
  private String genMrBefore;
  private String mrTime;
  private String materialType;
  private String maintenanceHour;
  private String supplierCode;
  private String materialTypeName;
  private String marketName;
  private String deviceTypeName;

  public MrCfgProcedureBtsDTO toDTO() {
    try {
      MrCfgProcedureBtsDTO dto = new MrCfgProcedureBtsDTO(
          !StringUtils.validString(cfgProcedureBtsId) ? null : Long.valueOf(cfgProcedureBtsId),
          !StringUtils.validString(marketCode) ? null : Long.valueOf(marketCode),
          deviceType,
          !StringUtils.validString(cycle) ? null : Long.valueOf(cycle),
          !StringUtils.validString(genMrBefore) ? null : Long.valueOf(genMrBefore),
          !StringUtils.validString(mrTime) ? null : Long.valueOf(mrTime),
          materialType,
          !StringUtils.validString(maintenanceHour) ? null : Long.valueOf(maintenanceHour),
          supplierCode
      );
      return dto;
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }
  }
}
