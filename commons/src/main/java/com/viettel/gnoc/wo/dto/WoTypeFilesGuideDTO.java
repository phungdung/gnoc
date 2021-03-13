package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.WoTypeFilesGuideEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoTypeFilesGuideDTO extends BaseDto {

  private Long woTypeFilesGuideId;
  private Long woTypeId;
  private String fileName;
  private String filePath;

  public WoTypeFilesGuideEntity toEntity() {
    return new WoTypeFilesGuideEntity(woTypeFilesGuideId, woTypeId, fileName, filePath);
  }

}
