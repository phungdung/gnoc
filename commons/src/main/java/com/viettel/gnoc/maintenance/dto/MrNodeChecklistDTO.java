package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrNodeChecklistEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author tripm
 * @version 2.0
 * @since 12/06/2020 09:31:00
 */
@Getter
@Setter
@NoArgsConstructor
public class MrNodeChecklistDTO extends BaseDto {

  private String nodeChecklistId;
  private String mrNodeId;
  private String checklistId;
  private String status;
  private String createdUser;
  private String createdTime;
  private String comments;
  private String updatedUser;
  private String updatedTime;

  private String deviceTypeAll;
  private String content;
  private String woId;

  private String isRequiredChecklist;
  private String fileId;
  private String filePath;
  private String fileName;
  private List<MrNodeChecklistFilesDTO> lstFile;
  private String target;

  public MrNodeChecklistDTO(String nodeChecklistId, String mrNodeId, String checklistId, String status, String createdUser, String createdTime, String comments, String updatedUser, String updatedTime) {
    this.nodeChecklistId = nodeChecklistId;
    this.mrNodeId = mrNodeId;
    this.checklistId = checklistId;
    this.status = status;
    this.createdUser = createdUser;
    this.createdTime = createdTime;
    this.comments = comments;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
  }

  public MrNodeChecklistEntity toEntity() {
    MrNodeChecklistEntity entity = new MrNodeChecklistEntity(
        StringUtils.validString(nodeChecklistId) ? Long.valueOf(nodeChecklistId) : null,
        StringUtils.validString(mrNodeId) ? Long.valueOf(mrNodeId) : null,
        StringUtils.validString(checklistId) ? Long.valueOf(checklistId) : null,
        status,
        comments,
        createdUser,
        StringUtils.validString(createdTime) ? DateTimeUtils.convertStringToDate(createdTime) : null,
        updatedUser,
        StringUtils.validString(updatedTime) ? DateTimeUtils.convertStringToDate(updatedTime) : null
    );
    return entity;
  }
}
