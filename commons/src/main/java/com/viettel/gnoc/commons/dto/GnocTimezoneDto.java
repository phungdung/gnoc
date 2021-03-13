package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.GnocTimezoneEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GnocTimezoneDto extends BaseDto {

  private Long gnocTimezoneId;
  private String timezoneCode;
  private String timezoneName;
  private String nationCode;
  private Double timezoneOffset;
  private Boolean isActive;

  public GnocTimezoneDto(Long gnocTimezoneId, String timezoneCode, String timezoneName,
      String nationCode, Double timezoneOffset) {
    this.gnocTimezoneId = gnocTimezoneId;
    this.timezoneCode = timezoneCode;
    this.timezoneName = timezoneName;
    this.nationCode = nationCode;
    this.timezoneOffset = timezoneOffset;
  }

  public GnocTimezoneEntity toEntity() {
    return new GnocTimezoneEntity(gnocTimezoneId, timezoneCode, timezoneName, nationCode,
        timezoneOffset);
  }
}
