package com.viettel.gnoc.incident.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CfgTimeTroubleProcessExportDTO {

  private Long id;
  private Long typeId;
  private Long subCategoryId;
  private Long priorityId;
  private Double createTtTime;
  private Double processTtTime;
  private Double closeTtTime;
  private Double processWoTime;
  private Long isCall;
  private String lastUpdateTime;
  private String country;
  private Double timeAboutOverdue;
  private Long isStationVip;
  private Double timeStationVip;
  private Double timeWoVip;
  private String cdAudioName;
  private String ftAudioName;

  private String typeName;
  private String subCategoryName;
  private String priorityName;
  private String countryName;
  private String isCallName;
  private String stationVip;

  private String resultImport;
}
