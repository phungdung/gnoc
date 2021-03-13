package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.validator.SizeByte;
import com.viettel.gnoc.incident.model.TroubleFileIbmEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TroubleFileIbmDTO {

  private Long fileId;
  @SizeByte(max = 500, message = "validation.troubleFileIbmDTO.fileName.tooLong")
  private String fileName;
  @SizeByte(max = 1000, message = "validation.troubleFileIbmDTO.path.tooLong")
  private String path;
  private Date createTime;
  private Long createUserId;
  private Long troubleIdIbm;

  public TroubleFileIbmEntity toEntity() {
    return new TroubleFileIbmEntity(fileId, fileName, path, createTime, createUserId, troubleIdIbm);
  }

}
