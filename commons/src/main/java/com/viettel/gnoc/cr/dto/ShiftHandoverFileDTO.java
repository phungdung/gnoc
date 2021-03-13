package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.ShiftHandoverFileEntity;
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
public class ShiftHandoverFileDTO extends BaseDto {

  private Long id;
  private Long fileId;
  private Long shiftHandoverId;

  public ShiftHandoverFileEntity toEntity() {
    return new ShiftHandoverFileEntity(
        id,
        fileId,
        shiftHandoverId
    );
  }
}
