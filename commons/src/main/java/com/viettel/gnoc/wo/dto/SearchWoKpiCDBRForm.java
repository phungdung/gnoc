package com.viettel.gnoc.wo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchWoKpiCDBRForm {

  private String username;
  private String accountIsdn;
  private String serviceId;
  private String ccServiceId;
  private String telServiceId;
  private String productCode;
  private String endTime;
  private String finishTime;

}
