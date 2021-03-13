package com.viettel.gnoc.cr.dto;

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
public class CrFilesAttachResultDTO {

  private String fileId;
  private String tempImportId;
  private String fileName;
  private String timeAttack;
  private String userId;
  private String fileType;
  private String crId;
  private String userName;
  private String filePath;

  public CrFilesAttachResultDTO(String fileId, String fileName, String timeAttack, String userId,
      String fileType, String crId, String userName) {
    this.fileId = fileId;
    this.fileName = fileName;
    this.timeAttack = timeAttack;
    this.userId = userId;
    this.fileType = fileType;
    this.crId = crId;
    this.userName = userName;
  }
}
