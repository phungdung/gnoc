package com.viettel.gnoc.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CrAlarmDraffDTO extends BaseDto {

  private String fault_name;
  private String fault_group_name;
  private String fault_level_code;
  private String device_type_code;
  private String fault_src;
  private String vendor_name;
  private String vendor_code;
  private Long vendor_id;
  private Long fault_level_id;
  private Long device_type_id;
  private Long fault_id;
  private Long fault_group_id;
  private String module_code;
  private String module_name;
  private String keyword;
  private Long number_occurences;//so lan xuat hien
}
