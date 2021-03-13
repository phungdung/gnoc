package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.model.SRHisEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class SRHisDTO {

  private String hisId;
  private String createdUser;
  private String createdTime;
  private String actionCode;
  private String srStatus;
  private String comments;
  private String srId;
  private String unitName;

  public SRHisEntity toEntity() {
    SRHisEntity model = new SRHisEntity(
        StringUtils.validString(hisId) ? Long.valueOf(hisId) : null
        , createdUser
        , StringUtils.validString(createdTime) ? DateTimeUtils.convertStringToDate(createdTime)
        : null
        , actionCode
        , srStatus
        , comments
        , StringUtils.validString(srId) ? Long.valueOf(srId) : null
    );
    return model;
  }

  public SRHisDTO(String hisId, String createdUser, String createdTime, String actionCode,
      String srStatus, String comments, String srId) {
    this.hisId = hisId;
    this.createdUser = createdUser;
    this.createdTime = createdTime;
    this.actionCode = actionCode;
    this.srStatus = srStatus;
    this.comments = comments;
    this.srId = srId;
  }
}
