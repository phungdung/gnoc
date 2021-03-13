package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
@NoArgsConstructor
public class SearchDeviceNIMSDTO extends BaseDto {

  private String networkType;
  private String networkClass;
  private String ipNode;
  private String deviceCode;
  private String deviceName;
  private String stationId;
  private String stationCode;
  private String stationName;
  private String creatDate;
  private String dateFrom;
  private String dateTo;

  public SearchDeviceNIMSDTO(String networkType, String networkClass, String ipNode,
      String deviceCode, String deviceName, String stationCode, String creatDate) {
    this.networkType = networkType;
    this.networkClass = networkClass;
    this.ipNode = ipNode;
    this.deviceCode = deviceCode;
    this.deviceName = deviceName;
    this.stationCode = stationCode;
    this.creatDate = creatDate;
  }
}
