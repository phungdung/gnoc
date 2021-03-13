package com.viettel.gnoc.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TroubleStatisticForm {

  private String priority;
  private String status;
  private String statusCode;
  private String priorityCode;
  private Double remainTime;
  private String createdTime;
  private String troubleId;
  private String receiveUnit;
  private String troubleCode;
  private String troubleName;
  private String createUnit;
  private String lastUpdateTime;
  private int numMajor;
  private int numMinor;
  private int numCritical;
  private int type;

  public TroubleStatisticForm(int type) {
    this.numCritical = 0;
    this.numMajor = 0;
    this.numMinor = 0;
    this.type = type;
  }
}
