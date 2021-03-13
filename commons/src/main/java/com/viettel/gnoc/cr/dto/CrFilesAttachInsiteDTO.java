/**
 * @(#)CrFilesAttachForm.java , Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.model.CrFilesAttachEntity;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author anhmv6
 * @version 1.0
 * @since 9/24/2015 9:41 AM
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class CrFilesAttachInsiteDTO extends BaseDto {

  private Long fileId;
  private Long tempImportId;
  private String fileName;
  private Date timeAttack;
  private Long userId;
  private String fileType;
  private Long crId;
  private String filePath;
  private String filePathFtp;
  private Long fileSize;
  private String dtCode;
  private String dtFileHistory;
  private Long isRun;
  private String fileContent;
  private String userName;
  private List<CrFileObjectInsite> crFileObjects;
  private String crType;
  private String impactSegment;
  private String crProcessId;
  private String actionRight;

  private byte[] fileByte;
  private List<CrFilesAttachInsiteDTO> lstCustomerFile;
  private List<String> lstRemoveFileAttach;
  private String stateCr;

  public CrFilesAttachInsiteDTO(Long fileId, Long tempImportId, String fileName, Date timeAttack,
      Long userId, String fileType, Long crId, String filePath, Long fileSize,
      String dtCode, String dtFileHistory, Long isRun) {
    this.fileId = fileId;
    this.tempImportId = tempImportId;
    this.fileName = fileName;
    this.timeAttack = timeAttack;
    this.userId = userId;
    this.fileType = fileType;
    this.crId = crId;
    this.filePath = filePath;
    this.fileSize = fileSize;
    this.dtCode = dtCode;
    this.dtFileHistory = dtFileHistory;
    this.isRun = isRun;
  }

  public CrFilesAttachInsiteDTO(Long fileId, Long tempImportId, String fileName, Date timeAttack,
      Long userId, String fileType, Long crId, String filePath, String filePathFtp, Long fileSize,
      String dtCode, String dtFileHistory, Long isRun) {
    this.fileId = fileId;
    this.tempImportId = tempImportId;
    this.fileName = fileName;
    this.timeAttack = timeAttack;
    this.userId = userId;
    this.fileType = fileType;
    this.crId = crId;
    this.filePath = filePath;
    this.filePathFtp = filePathFtp;
    this.fileSize = fileSize;
    this.dtCode = dtCode;
    this.dtFileHistory = dtFileHistory;
    this.isRun = isRun;
  }

  public CrFilesAttachEntity toEntity() {
    return new CrFilesAttachEntity(fileId, tempImportId, fileName, timeAttack, userId, fileType,
        crId, filePath, fileSize, dtCode, dtFileHistory, isRun);
  }

  public CrFilesAttachDTO toModelInSide() {
    CrFilesAttachDTO model = new CrFilesAttachDTO(
        StringUtils.validString(fileId) ? String.valueOf(fileId) : null,
        StringUtils.validString(tempImportId) ? String.valueOf(tempImportId) : null,
        fileName,
        StringUtils.validString(timeAttack) ? DateTimeUtils.date2ddMMyyyyHHMMss(timeAttack)
            : null,
        StringUtils.validString(userId) ? String.valueOf(userId) : null,
        fileType,
        StringUtils.validString(crId) ? String.valueOf(crId) : null,
        filePath,
        filePathFtp,
        StringUtils.validString(fileSize) ? String.valueOf(fileSize) : null,
        dtCode,
        dtFileHistory,
        StringUtils.validString(isRun) ? String.valueOf(isRun) : null,
        fileContent
    );

    return model;
  }
}
