/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class CrProcessGroup extends BaseDto {

  private Long crProcessId;
  private Long cpteId;
  private Long tempImportId;
  private Long fileType;
  private String code;
  private String name;
  private String totalColumn;
  private String title;
  private Long createrId;
  private String createrTime;
  private Long processTypeId;
  private Long isActive;
  private Long webserviceMethodId;
  private Long isValidateInput;
  private Long isRevert;
  private Long isMecFile;
  private Long cpdgpId;
  private Long cpdgpType;
  private Long groupUnitId;
  private String groupUnitCode;
  private String groupUnitName;
  private String checkBox;

}
