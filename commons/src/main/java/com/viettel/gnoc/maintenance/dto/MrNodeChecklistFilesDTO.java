package com.viettel.gnoc.maintenance.dto;


import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrNodeChecklistFilesEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author tripm
 * @version 2.0
 * @since 12/06/2020 09:31:00
 */
@Getter
@Setter
@NoArgsConstructor
public class MrNodeChecklistFilesDTO extends BaseDto {

  private String fileId;
  private String fileName;
  private String filePath;
  private String nodeChecklistId;
  private String createdUser;
  private String createdTime;
  private String updatedUser;
  private String updatedTime;
  private byte[] fileContent;
  private String filePathFtp;

  public MrNodeChecklistFilesDTO(String fileId, String fileName, String filePath, String nodeChecklistId, String createdUser, String createdTime, String updatedUser, String updatedTime) {
    this.fileId = fileId;
    this.fileName = fileName;
    this.filePath = filePath;
    this.nodeChecklistId = nodeChecklistId;
    this.createdUser = createdUser;
    this.createdTime = createdTime;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
  }

  public MrNodeChecklistFilesEntity toEntity() {
    MrNodeChecklistFilesEntity entity = new MrNodeChecklistFilesEntity(
        StringUtils.validString(fileId) ? Long.valueOf(fileId) : null,
        fileName,
        filePath,
        StringUtils.validString(nodeChecklistId) ? Long.valueOf(nodeChecklistId) : null,
        createdUser,
        StringUtils.validString(createdTime) ? DateTimeUtils.convertStringToDate(createdTime) : null,
        updatedUser,
        StringUtils.validString(updatedTime) ? DateTimeUtils.convertStringToDate(updatedTime) : null
    );
    return entity;
  }
}
