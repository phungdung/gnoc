package com.viettel.gnoc.cr.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrAffectedServiceInfo {

  private Long crId;
  private Long affectedServiceId;
  private String affectedServiceCode;
  private String affectedServiceName;
  private Date insertTime;
}
