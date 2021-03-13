package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.AutoCreateWoOsEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AutoCreateWoOsDTO extends BaseDto {
  private Long id;
  private String cdCode;
  private String cdId;
  private String woTypeCode;
  private String content;
  private Date createUpdateTime;
  private String typeId;
  private String result;
  private String arrayFile;

  public AutoCreateWoOsEntity toEntity() {
    return new AutoCreateWoOsEntity(id, cdCode, cdId, woTypeCode, content, createUpdateTime, typeId, result, arrayFile);
  }
}
