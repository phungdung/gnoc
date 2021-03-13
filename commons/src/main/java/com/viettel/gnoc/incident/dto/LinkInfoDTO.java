package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class LinkInfoDTO extends BaseDto {

  private String linkName;
  private String device1;
  private String port1;
  private String port2;
  private String device2;
  private String fiber;
  private String capacity;
  private String createTime;
  private String modifyTime;
}
