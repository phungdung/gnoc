package com.viettel.gnoc.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrModuleDraftDTO extends BaseDto {

  private String SERVICE_ID;
  private String SERVICE_CODE;
  private String SERVICE_NAME;
  private String MODULE_ID;
  private String MODULE_CODE;
  private String MODULE_NAME;
  private String MODULE_STATUS;
  private String GROUP_MODULE_ID;
  private String MODULE_TYPE;
  private String MODULE_TYPE_CODE;
  private String MODULE_TYPE_NAME;
  private String EXECUTE_PATH;
  private String LOG_START_PATH;
  private String LOG_START_FILE_NAME;
  private String IP_SERVER;
  private String OS_TYPE;
  private String OS_NAME;
  private String USERNAME;
  private String PASSWORD;
  private String START_SERVICE;
  private String RESTART_SERVICE;
  private String STOP_SERVICE;
  private String DELETE_CACHE;
  private String VIEW_STATUS;
  private String USER_MANAGER;
  private String UNIT_ID;
  private String GROUP_MODULE_NAME;
  private String GROUP_MODULE_CODE;
  private String FUNCTION_CODE;
  private String nationCode;
  private String checkbox;
}
