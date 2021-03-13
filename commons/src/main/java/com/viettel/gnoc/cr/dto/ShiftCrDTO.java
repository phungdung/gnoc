package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.ShiftCrEntity;
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
public class ShiftCrDTO extends BaseDto {

  private Long id;
  private String crNumber;
  private String crNumberError;
  private String crName;
  private String purpose;
  private String unitName;
  private String userName;
  private String userCheckName;
  private String result;
  private String note;
  private Long shiftHandoverId;
  private Long country;
  private String countryName;
  private Boolean isDeleteShiftCr;
  private String statusName;
  private String changeResponsible;
  private String resultImport;

  public ShiftCrDTO(Long id, String crNumber, String crName, String purpose, String unitName,
      String userName, String userCheckName, String result, String note,
      Long shiftHandoverId, Long country) {
    this.id = id;
    this.crNumber = crNumber;
    this.crName = crName;
    this.purpose = purpose;
    this.unitName = unitName;
    this.userName = userName;
    this.userCheckName = userCheckName;
    this.result = result;
    this.note = note;
    this.shiftHandoverId = shiftHandoverId;
    this.country = country;
  }

  public ShiftCrEntity toEntity() {
    return new ShiftCrEntity(
        id,
        crNumber,
        crName,
        purpose,
        unitName,
        userName,
        userCheckName,
        result,
        note,
        shiftHandoverId,
        country
    );
  }
}
