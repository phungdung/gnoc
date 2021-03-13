/**
 * @(#)KedbFilesForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.ws.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KedbFilesDTO {

  //Fields
  private String kedbFileId;
  private String kedbFileName;
  private String createUnitId;
  private String createUnitName;
  private String createUserId;
  private String createUserName;
  private String createTime;
  private String kedbId;
  private String kedbIdName;
  private String content;
  private String syncStatus;

  public KedbFilesDTO(String kedbFileId, String kedbFileName, String createUnitId,
      String createUnitName, String createUserId, String createUserName,
      String createTime, String kedbId, String content) {
    this.kedbFileId = kedbFileId;
    this.kedbFileName = kedbFileName;
    this.createUnitId = createUnitId;
    this.createUnitName = createUnitName;
    this.createUserId = createUserId;
    this.createUserName = createUserName;
    this.createTime = createTime;
    this.kedbId = kedbId;
    this.content = content;
  }

  public com.viettel.gnoc.kedb.dto.KedbFilesDTO toInsideDTO() {
    return new com.viettel.gnoc.kedb.dto.KedbFilesDTO(
        StringUtils.isNotNullOrEmpty(kedbFileId) ? Long.parseLong(kedbFileId) : null,
        kedbFileName,
        StringUtils.isNotNullOrEmpty(createUnitId) ? Long.parseLong(createUnitId) : null,
        createUnitName,
        StringUtils.isNotNullOrEmpty(createUserId) ? Long.parseLong(createUserId) : null,
        createUserName,
        StringUtils.isNotNullOrEmpty(createTime) ? DateTimeUtils.convertStringToDate(createTime)
            : null,
        StringUtils.isNotNullOrEmpty(kedbId) ? Long.parseLong(kedbId) : null,
        content
    );
  }
}
