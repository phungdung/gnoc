package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.incident.model.ItSpmInfoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class ItSpmInfoDTO {

  //Fields

  private Long id;
  private Long incidentId;
  private Long spmId;
  private String spmCode;
  private String incidentCode;
  private static long changedTime = 0;

  public ItSpmInfoDTO(
      Long id, Long incidentId, Long spmId, String spmCode, String incidentCode) {
    this.id = id;
    this.incidentId = incidentId;
    this.spmId = spmId;
    this.spmCode = spmCode;
    this.incidentCode = incidentCode;
  }

  public ItSpmInfoEntity toEntity() {
    ItSpmInfoEntity model = new ItSpmInfoEntity(
        id,
        incidentId,
        spmId,
        spmCode,
        incidentCode
    );
    return model;
  }

}
