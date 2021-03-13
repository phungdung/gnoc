package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.SizeByte;
import com.viettel.gnoc.wo.model.WoHistoryEntity;
import java.util.Date;
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
public class WoHistoryInsideDTO extends BaseDto {

  private Long woHistoryId;
  private Long oldStatus;
  private Long newStatus;
  private Long woId;
  private String woCode;
  private String woContent;
  private Long userId;
  private String userName;
  @SizeByte(max = 500, message = "validation.woHistoryDTO.fileName.tooLong")
  private String fileName;
  private String comments;
  private Date updateTime;
  private Long createMessage;
  private Long createPersonId;
  private Long cdId;
  private Long ftId;
  private Long isSendIbm;
  private String nation;

  private Double offset;

  public WoHistoryInsideDTO(Long woHistoryId, Long oldStatus, Long newStatus, Long woId,
      String woCode, String woContent, Long userId, String userName, String fileName,
      String comments, Date updateTime, Long createMessage, Long createPersonId, Long cdId,
      Long ftId, Long isSendIbm, String nation) {
    this.woHistoryId = woHistoryId;
    this.oldStatus = oldStatus;
    this.newStatus = newStatus;
    this.woId = woId;
    this.woCode = woCode;
    this.woContent = woContent;
    this.userId = userId;
    this.userName = userName;
    this.fileName = fileName;
    this.comments = comments;
    this.updateTime = updateTime;
    this.createMessage = createMessage;
    this.createPersonId = createPersonId;
    this.cdId = cdId;
    this.ftId = ftId;
    this.isSendIbm = isSendIbm;
    this.nation = nation;
  }

  public WoHistoryEntity toEntity() {
    return new WoHistoryEntity(woHistoryId, oldStatus, newStatus, woId, woCode, woContent, userId,
        userName, fileName, comments, updateTime, createMessage, createPersonId, cdId, ftId,
        isSendIbm, nation);
  }

  public WoHistoryDTO toModelOutSide() {
    WoHistoryDTO model = new WoHistoryDTO(
        StringUtils.validString(woHistoryId) ? String.valueOf(woHistoryId) : null,
        StringUtils.validString(oldStatus) ? String.valueOf(oldStatus) : null,
        StringUtils.validString(newStatus) ? String.valueOf(newStatus) : null,
        StringUtils.validString(woId) ? String.valueOf(woId) : null,
        woCode,
        woContent,
        StringUtils.validString(userId) ? String.valueOf(userId) : null,
        userName,
        fileName,
        comments,
        StringUtils.validString(updateTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(updateTime)
            : null,
        StringUtils.validString(createMessage) ? String.valueOf(createMessage) : null,
        StringUtils.validString(createPersonId) ? String.valueOf(createPersonId) : null,
        StringUtils.validString(cdId) ? String.valueOf(cdId) : null,
        StringUtils.validString(ftId) ? String.valueOf(ftId) : null,
        StringUtils.validString(isSendIbm) ? String.valueOf(isSendIbm) : null,
        nation
    );
    return model;
  }

}
