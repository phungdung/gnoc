package com.viettel.gnoc.cr.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class LogActionBO {

  private Long lcnId;
  private String ipAddress;
  private String userName;
  private String action;
  private Date updateTime;
  private Long responeTime;
  private String module;
  private String description;
}
