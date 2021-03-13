package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.ShiftItEntity;
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
public class ShiftItDTO extends BaseDto {

  private Long id;
  private Long kpi;
  private Double emergency;
  private Double serious;
  private Double medium;
  private Double total;
  private String note;
  private Long country;
  private String countryName;
  private Long shiftHandoverId;
  private Long typeIncident;
  private String typeIncidentName;
  private String kpiName;
  private String itemValue;

  public ShiftItDTO(Long id, Long kpi, Double emergency, Double serious, Double medium,
      Double total, String note, Long country, Long shiftHandoverId, Long typeIncident) {
    this.id = id;
    this.kpi = kpi;
    this.emergency = emergency;
    this.serious = serious;
    this.medium = medium;
    this.total = total;
    this.note = note;
    this.country = country;
    this.shiftHandoverId = shiftHandoverId;
    this.typeIncident = typeIncident;
  }

  public void setEmergency(Double emergency) {
    if (emergency == null || emergency < 0D) {
      this.emergency = 0D;
    } else {
      this.emergency = emergency;
    }
  }

  public void setSerious(Double serious) {
    if (serious == null || serious < 0D) {
      this.serious = 0D;
    } else {
      this.serious = serious;
    }
  }

  public void setMedium(Double medium) {
    if (medium == null || medium < 0D) {
      this.medium = 0D;
    } else {
      this.medium = medium;
    }
  }

  public void setTotal(Double total) {
    if (total == null || total < 0D) {
      this.total = 0D;
    } else {
      this.total = total;
    }
  }

  public ShiftItEntity toEntity() {
    return new ShiftItEntity(
        id,
        kpi,
        emergency,
        serious,
        medium,
        total,
        note,
        country,
        shiftHandoverId,
        typeIncident
    );
  }
}
