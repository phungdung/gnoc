package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrScheduleBtsHisFileEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author tiennv
 * @version 1.0
 * @since 09/01/2020 09:43:25
 */
@Getter
@Setter
public class MrScheduleBtsHisFileDTO {

  //Fields
  private String woId;
  private String fileName;
  private String filePath;
  private String userUpdate;
  private String idFile;
  private String checklistId;
  private byte[] fileContent;
  private String createdDate;
  private String defaultSortField;

  public String getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }

  //Constructor
  public MrScheduleBtsHisFileDTO() {
    setDefaultSortField("name");
    //constructor
  }

  public MrScheduleBtsHisFileDTO(String woId, String fileName, String filePath, String userUpdate,
      String idFile, String checklistId) {
    this.woId = woId;
    this.fileName = fileName;
    this.filePath = filePath;
    this.userUpdate = userUpdate;
    this.idFile = idFile;
    this.checklistId = checklistId;
  }

  public MrScheduleBtsHisFileDTO(String woId, String fileName, String filePath, String userUpdate,
                                 String idFile, String checklistId, String createdDate) {
    this.woId = woId;
    this.fileName = fileName;
    this.filePath = filePath;
    this.userUpdate = userUpdate;
    this.idFile = idFile;
    this.checklistId = checklistId;
    this.createdDate = createdDate;
  }

  public MrScheduleBtsHisFileEntity toEntity() {
    MrScheduleBtsHisFileEntity entity = new MrScheduleBtsHisFileEntity(
        woId,
        fileName,
        filePath,
        userUpdate,
        !StringUtils.validString(idFile) ? null : Long.valueOf(idFile),
        checklistId,
        !StringUtils.validString(createdDate) ? null
            : DateTimeUtils.convertStringToDate(createdDate)
    );
    return entity;
  }

}
