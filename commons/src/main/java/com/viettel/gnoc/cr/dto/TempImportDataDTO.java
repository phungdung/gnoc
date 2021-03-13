package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.TempImportDataEntity;
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
public class TempImportDataDTO extends BaseDto {

  private Long tidaId;
  private String tempImportId;
  private String tempImportColId;
  private String tempImportValue;
  private String crId;
  private String rowOrder;

  public TempImportDataEntity toEntity() {
    return new TempImportDataEntity(
        tidaId,
        tempImportId,
        tempImportColId,
        tempImportValue,
        crId,
        rowOrder
    );
  }
}
