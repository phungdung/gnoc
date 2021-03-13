package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.ShiftWorkOtherEntity;
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
public class ShiftWorkOtherDTO extends BaseDto {

  private Long id;
  private String workName;
  private String description;
  private String purpose;
  private String reason;
  private String solution;
  private String time;
  private String result;
  private String note;
  private Long shiftHandoverId;
  private Long country;
  private String countryName;

  public ShiftWorkOtherDTO(Long id, String workName, String description, String purpose,
      String reason, String solution, String time, String result,
      String note, Long shiftHandoverId, Long country) {
    this.id = id;
    this.workName = workName;
    this.description = description;
    this.purpose = purpose;
    this.reason = reason;
    this.solution = solution;
    this.time = time;
    this.result = result;
    this.note = note;
    this.shiftHandoverId = shiftHandoverId;
    this.country = country;
  }

  public ShiftWorkOtherEntity toEntity() {
    return new ShiftWorkOtherEntity(
        id,
        workName,
        description,
        purpose,
        reason,
        solution,
        time,
        result,
        note,
        shiftHandoverId,
        country
    );
  }
}
