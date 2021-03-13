package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrUngCuuTTFilesEntity;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MrUngCuuTTFilesDTO {

  private String fileId;
  private String fileName;
  private String filePath;
  private String ucttId;
  private String createdUser;
  private Date createdTime;
  private String updatedUser;
  private Date updatedTime;

  public MrUngCuuTTFilesDTO(String fileId, String fileName, String filePath, String ucttId,
      String createdUser, Date createdTime, String updatedUser, Date updatedTime) {
    this.fileId = fileId;
    this.fileName = fileName;
    this.filePath = filePath;
    this.ucttId = ucttId;
    this.createdUser = createdUser;
    this.createdTime = createdTime;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
  }

  public MrUngCuuTTFilesEntity toEntity() {
    try {
      MrUngCuuTTFilesEntity model = new MrUngCuuTTFilesEntity(
          StringUtils.validString(fileId) ? Long.valueOf(fileId) : null,
          fileName,
          filePath,
          StringUtils.validString(ucttId) ? Long.valueOf(ucttId) : null,
          createdUser,
          createdTime,
          updatedUser,
          updatedTime
      );
      return model;
    } catch (Exception e) {
      return null;
    }
  }
}
