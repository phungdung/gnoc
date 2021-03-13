/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.cr.model.CrModuleDetailEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CrModuleDetailDTO {

  private Long cmdId;
  private Long crId;
  private String serviceCode;
  private String serviceName;
  private String moduleCode;
  private String moduleName;
  private String createTime;
  private String nationCode;
  private String defaultSortField = "name";

  public CrModuleDetailEntity toEntity() {
    CrModuleDetailEntity model = new CrModuleDetailEntity();
    model.setCmdId(cmdId);
    model.setCrId(crId);
    model.setModuleCode(moduleCode);
    model.setModuleName(moduleName);
    model.setNationCode(nationCode);
    model.setServiceCode(serviceCode);
    model.setServiceName(serviceName);

    return model;
  }


}
