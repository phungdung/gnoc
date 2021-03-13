package com.viettel.gnoc.kedb.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.kedb.model.KedbFilesEntity;
import java.util.Date;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KedbFilesDTO extends BaseDto {

  private Long kedbFileId;
  @Size(max = 255, message = "validation.kedbFilesDTO.kedbFileName.tooLong")
  private String kedbFileName;
  private Long createUnitId;
  private String createUnitName;
  private Long createUserId;
  private String createUserName;
  private Date createTime;
  private Long kedbId;
  private String content;

  public KedbFilesEntity toEntity() {
    return new KedbFilesEntity(kedbFileId, kedbFileName, createUnitId, createUnitName, createUserId,
        createUserName, createTime, kedbId, content);
  }
}
