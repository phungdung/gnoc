package com.viettel.gnoc.kedb.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.SizeByte;
import com.viettel.gnoc.kedb.model.KedbActionLogsEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KedbActionLogsDTO extends BaseDto {

  //Fields
  private Long id;

  @SizeByte(max = 2000, message = "{validation.kedb.actionlog.contentInvalidMaxLength}")
  private String content;

  private Date createTime;

  @SizeByte(max = 200, message = "{validation.kedb.actionlog.unitNameInvalidMaxLength}")
  private String createUnitName;

  @SizeByte(max = 200, message = "{validation.kedb.actionlog.createUserNameInvalidMaxLength}")
  private String createUserName;

  private Long kedbId;

  @SizeByte(max = 1000, message = "{validation.kedb.actionlog.statusInvalidMaxLength}")
  private String status;

  public KedbActionLogsEntity toEntity() {
    KedbActionLogsEntity entity = new KedbActionLogsEntity(
        id,
        content,
        createTime,
        createUnitName,
        createUserName,
        kedbId,
        status
    );
    return entity;
  }
}
