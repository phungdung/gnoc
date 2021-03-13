package com.viettel.gnoc.cr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MiniImpactedNode {

  private Long crId;
  private Long ipId;
  private Long deviceId;
  private String deviceCode;
  private String deviceName;
  private String ip;
  private String diviceCode;
  private String nationCode;
}
