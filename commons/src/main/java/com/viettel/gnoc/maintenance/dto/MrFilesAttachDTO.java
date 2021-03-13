package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrFilesAttachEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MrFilesAttachDTO extends BaseDto {

  private String fileId;
  private String fileName;
  private String timeAttack;
  private String userId;
  private String fileType;
  private String mrId;
  private String filePath;
  private String userName;

  public MrFilesAttachEntity toEntity() {
    try {
      MrFilesAttachEntity model = new MrFilesAttachEntity(
          StringUtils.validString(fileId)
              ? Long.valueOf(fileId) : null,
          fileName,
          StringUtils.validString(timeAttack)
              ? DateTimeUtils.convertStringToDate(timeAttack) : null,
          StringUtils.validString(userId)
              ? Long.valueOf(userId) : null,
          fileType,
          filePath,
          StringUtils.validString(mrId)
              ? Long.valueOf(mrId) : null
      );
      return model;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }
}
