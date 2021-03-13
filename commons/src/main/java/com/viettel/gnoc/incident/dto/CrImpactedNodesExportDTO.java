package com.viettel.gnoc.incident.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrImpactedNodesExportDTO {

  private String cinsId;
  private String crId;
  private String ipId;
  private String deviceId;
  private String ip;
  private String deviceName;
  private String deviceCode;
  private String deviceCodeOld;
  private String insertTime;
  private String type;
  private String nationCode;
  private String dtCode;
  private String resultImport;

}
