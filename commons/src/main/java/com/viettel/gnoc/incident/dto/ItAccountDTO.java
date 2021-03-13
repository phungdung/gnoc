package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.incident.model.ItAccountEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class ItAccountDTO extends BaseDto {

  //Fields
  private Long id;
  private Long incidentId;
  private String account;
  private static long changedTime = 0;

  public ItAccountDTO(Long id, Long incidentId, String account) {
    this.id = id;
    this.incidentId = incidentId;
    this.account = account;
  }

  public ItAccountEntity toEntity() {
    ItAccountEntity model = new ItAccountEntity(
        id,
        incidentId,
        account
    );
    return model;
  }
}
