/**
 * @(#)RoleUserForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */

package com.viettel.gnoc.commons.dto;


import com.viettel.gnoc.commons.model.CfgBusinessCallSmsEntity;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.CfgBusinessCallSms.null.unique}", clazz = CfgBusinessCallSmsEntity.class, uniqueFields = "cdId,cfgTypeId,cfgLevel", idField = "id")
public class CfgBusinessCallSmsDTO extends BaseDto {

  //Fields
  private Long id;
  @NotNull(message = "{validation.CfgBusinessCallSms.null.cfgTypeId}")
  private Long cfgTypeId;
  @NotNull(message = "{validation.CfgBusinessCallSms.null.cdId}")
  private Long cdId;
  @NotNull(message = "{validation.CfgBusinessCallSms.null.cfgLevel}")
  private Long cfgLevel;
  private Long status;

  private String username;
  private String fullName;
  private String mobileNumber;
  private Long userId;
  private String configType;
  private String cdName;
  private String cfgLevelStr;
  private String action;
  private String actionName;
  private String resultImport;
  private List<CfgBusinessCallSmsUserDTO> lstCfgBusinessCallSmsUser;
  private List<Long> lstDelete;

  public CfgBusinessCallSmsDTO(Long id, Long cfgTypeId, Long cdId, Long cfgLevel) {
    this.id = id;
    this.cfgTypeId = cfgTypeId;
    this.cdId = cdId;
    this.cfgLevel = cfgLevel;
  }

  public CfgBusinessCallSmsEntity toEntity() {
    CfgBusinessCallSmsEntity entity = new CfgBusinessCallSmsEntity(
        id, cfgTypeId, cdId, cfgLevel);
    return entity;
  }
}

