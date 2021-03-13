/**
 * @(#)CommonFileForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.incident.model.CommonFileEntity;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class CommonFileDTO extends BaseDto {

  //Fields
  private Long fileId;
  private String fileName;
  private String path;
  private Date createTime;
  private Long createUserId;
  private String createUserName;

  public CommonFileDTO(Long fileId) {
    this.fileId = fileId;
  }

  public CommonFileDTO(
      Long fileId, String fileName, String path, Date createTime, Long createUserId) {
    this.fileId = fileId;
    this.fileName = fileName;
    this.path = path;
    this.createTime = createTime;
    this.createUserId = createUserId;
  }

  public CommonFileEntity toEntity() {
    CommonFileEntity model = new CommonFileEntity(
        fileId,
        fileName,
        path,
        createTime,
        createUserId);
    return model;
  }

}
