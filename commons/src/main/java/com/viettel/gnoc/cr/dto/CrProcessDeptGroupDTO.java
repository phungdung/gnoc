/**
 * @(#)CrProcessDeptGroupBO.java 11/16/2015 5:28 PM, Copyright 2011 Viettel Telecom. All rights
 * reserved VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.CrProcessDeptGroupEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kienpv
 * @version 1.0
 * @since 11/16/2015 5:28 PM
 */

@Setter
@Getter
@NoArgsConstructor
@Slf4j
public class CrProcessDeptGroupDTO extends BaseDto {

  private Long cpdgpId;
  private Long groupUnitId;
  private Long crProcessId;
  private Long cpdgpType;

  private String groupUnitName;
  private String groupUnitCode;

  public CrProcessDeptGroupDTO(Long cpdgpId, Long groupUnitId, Long crProcessId, Long cpdgpType) {
    this.cpdgpId = cpdgpId;
    this.groupUnitId = groupUnitId;
    this.crProcessId = crProcessId;
    this.cpdgpType = cpdgpType;
  }

  public CrProcessDeptGroupEntity toEntity() {
    CrProcessDeptGroupEntity entity = new CrProcessDeptGroupEntity(cpdgpId, groupUnitId,
        crProcessId, cpdgpType);

    return entity;
  }
}

