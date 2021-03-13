package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.wo.model.WoChecklistDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoChecklistDetailDTO {

  private Long woChecklistDetailId;
  private Long woTypeChecklistId;
  private String checklistValue;
  private Long woId;

  public WoChecklistDetailEntity toEntity() {
    return new WoChecklistDetailEntity(woChecklistDetailId, woTypeChecklistId, checklistValue,
        woId);
  }

}
