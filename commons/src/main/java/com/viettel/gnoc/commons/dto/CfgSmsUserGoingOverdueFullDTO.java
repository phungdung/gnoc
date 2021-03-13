package com.viettel.gnoc.commons.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CfgSmsUserGoingOverdueFullDTO extends BaseDto {

  private String userName;
  private String fullName;
  private String email;
  private String mobile;
  private String unitName;
  private String userId;
  private String id;
  private String cfgId;
  private String cfgType;
  private String cfgTypeName;
  private String locationId;
  private String locationName;
  private String priorityId;
  private String priorityName;

}
