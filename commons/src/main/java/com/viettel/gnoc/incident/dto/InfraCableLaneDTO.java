package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InfraCableLaneDTO extends BaseDto {

  private String laneId;
  private String startPoint;
  private String endPoint;
  private String laneCode;
  private String length;
  private String node;
  private String ownerId;
  private String projectId;
  private String departmentId;
  private String cableLevelId;
  private String sourceId;
  private String destId;
  private String sourceType;
  private String destType;
  private String mHang;
  private String mBury;
  private String mpipe;
  private String attenuation;
  private String criticalPoint;
  private String isDesigning;
  private String designProjectId;
  private String status;
  private String updateTime;
}

