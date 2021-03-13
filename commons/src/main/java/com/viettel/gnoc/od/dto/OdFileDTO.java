package com.viettel.gnoc.od.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.od.model.OdFileEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OdFileDTO extends BaseDto {

  private Long odFileId;
  private Long odId;
  private String fileName;
  private String path;

  public OdFileEntity toEntity() {
    return new OdFileEntity(odFileId, odId, fileName, path);
  }
}
