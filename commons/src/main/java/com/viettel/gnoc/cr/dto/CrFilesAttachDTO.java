/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.model.CrFilesAttachEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class CrFilesAttachDTO {

  private String fileId;
  private String tempImportId;
  private String fileName;
  private String timeAttack;
  private String userId;
  private String fileType;
  private String crId;
  private String filePath;
  private String filePathFtp;
  private String fileSize;
  private String dtCode;
  private String dtFileHistory;
  private String fileContent;
  private String isRun;

  private String colId;
  private String colName;
  private String uniqueColumn;
  private String defaultSortField;

  public CrFilesAttachDTO() {
//    setColId("fileId");
//    setColName("fileName");
//    setUniqueColumn("fileId");
    setDefaultSortField("name");
  }

  public CrFilesAttachDTO(String fileId, String tempImportId, String fileName,
      String timeAttack, String userId, String fileType, String crId, String filePath,
      String fileSize, String dtCode, String dtFileHistory, String fileContent,
      String isRun) {
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
    this.fileContent = fileContent;
    setDefaultSortField("fileId");
  }


  public CrFilesAttachDTO(String fileId, String tempImportId, String fileName,
      String timeAttack, String userId, String fileType, String crId, String filePath,
      String filePathFtp,
      String fileSize, String dtCode, String dtFileHistory, String fileContent,
      String isRun) {
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
    this.fileContent = fileContent;
  }


  public CrFilesAttachInsiteDTO toModelOutSide() {
    CrFilesAttachInsiteDTO model = new CrFilesAttachInsiteDTO(
        this.fileId == null ? null : Long.valueOf(this.fileId),
        this.tempImportId == null ? null : Long.valueOf(this.tempImportId),
        this.fileName,
        DateTimeUtils.convertStringToDate(timeAttack),
        this.userId == null ? null : Long.valueOf(this.userId),
        this.fileType,
        this.crId == null ? null : Long.valueOf(this.crId),
        this.filePath,
        this.filePathFtp,
        this.fileSize == null ? null : Long.valueOf(this.fileSize),
        this.dtCode,
        this.dtFileHistory,
        this.isRun == null ? null : Long.valueOf(this.isRun)
    );
    return model;
  }

  public CrFilesAttachEntity toEntity() {
    try {
      return new CrFilesAttachEntity(this.fileId == null ? null : Long.valueOf(this.fileId),
          this.tempImportId == null ? null : Long.valueOf(this.tempImportId),
          this.fileName,
          StringUtils.isStringNullOrEmpty(timeAttack) ? null
              : DateTimeUtils.convertStringToDate1(timeAttack),
          this.userId == null ? null : Long.valueOf(this.userId),
          this.fileType,
          this.crId == null ? null : Long.valueOf(this.crId),
          this.filePath,
          this.fileSize == null ? null : Long.valueOf(this.fileSize),
          this.dtCode,
          this.dtFileHistory,
          this.isRun == null ? null : Long.valueOf(this.isRun));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }
}
