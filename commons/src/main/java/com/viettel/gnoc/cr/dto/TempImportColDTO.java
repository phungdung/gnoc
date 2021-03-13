package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.cr.model.TempImportColEntity;
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
public class TempImportColDTO extends BaseDto {

  private Long tempImportColId;
  private String code;
  private String title;
  private Long isMerge;
  private Long colPosition;
  private Long tempImportId;
  private Long methodParameterId;
  private Long posBk;
  private int lastRow;

  public TempImportColDTO(Long tempImportColId, String code, String title, Long isMerge,
      Long colPosition, Long tempImportId, Long methodParameterId, Long posBk) {
    this.tempImportColId = tempImportColId;
    this.code = code;
    this.title = title;
    this.isMerge = isMerge;
    this.colPosition = colPosition;
    this.tempImportId = tempImportId;
    this.methodParameterId = methodParameterId;
    this.posBk = posBk;
  }

  public TempImportColEntity toEntity() {
    return new TempImportColEntity(
        tempImportColId,
        code,
        title,
        isMerge,
        colPosition,
        tempImportId,
        methodParameterId,
        posBk
    );
  }
}
