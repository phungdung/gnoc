package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.ShiftStaftEntity;
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
public class ShiftStaftDTO extends BaseDto {

  private Long id;
  private Long assignUserId;
  private String assignUserName;
  private Long receiveUserId;
  private String receiveUserName;
  private Long shiftHandoverId;
  private Boolean isDeleteShiftStaft;

  public ShiftStaftEntity toEntity() {
    return new ShiftStaftEntity(
        id,
        assignUserId,
        assignUserName,
        receiveUserId,
        receiveUserName,
        shiftHandoverId
    );
  }

  public ShiftStaftDTO(Long id, Long assignUserId, String assignUserName,
      Long receiveUserId, String receiveUserName, Long shiftHandoverId) {
    this.id = id;
    this.assignUserId = assignUserId;
    this.assignUserName = assignUserName;
    this.receiveUserId = receiveUserId;
    this.receiveUserName = receiveUserName;
    this.shiftHandoverId = shiftHandoverId;
  }
}
