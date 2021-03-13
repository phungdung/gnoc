package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.ShiftItSeriousEntity;
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
public class ShiftItSeriousDTO extends BaseDto {

  private Long id;
  private String infoTicket;
  private String description;
  private String affect;
  private String reason;
  private String correctiveAction;
  private String nextAction;
  private Long country;
  private Long shiftHandoverId;

  public ShiftItSeriousEntity toEntity() {
    return new ShiftItSeriousEntity(
        id,
        infoTicket,
        description,
        affect,
        reason,
        correctiveAction,
        nextAction,
        country,
        shiftHandoverId
    );
  }
}
