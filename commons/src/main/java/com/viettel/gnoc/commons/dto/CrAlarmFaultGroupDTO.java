package com.viettel.gnoc.commons.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CrAlarmFaultGroupDTO extends BaseDto {

  private String fault_src;
  private String fault_group_name;
  private Long fault_group_id;
}
