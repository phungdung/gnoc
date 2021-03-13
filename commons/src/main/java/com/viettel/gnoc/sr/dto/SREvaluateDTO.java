package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.model.SREvaluateEntity;
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
public class SREvaluateDTO extends BaseDto {

  private String evaluateId;
  private String evaluate;
  private String evaluateReason;
  private String srId;
  private Date createdTime;
  private String createdUser;
  private String review;
  private Date createFromDate;
  private Date createToDate;

  public SREvaluateDTO(String evaluateId, String evaluate, String evaluateReason,
      String srId, Date createdTime, String createdUser) {
    this.evaluateId = evaluateId;
    this.evaluate = evaluate;
    this.evaluateReason = evaluateReason;
    this.srId = srId;
    this.createdTime = createdTime;
    this.createdUser = createdUser;
  }

  public SREvaluateEntity toEntity() {
    SREvaluateEntity model = new SREvaluateEntity(
        StringUtils.validString(evaluateId) ? Long.valueOf(evaluateId) : null,
        evaluate,
        evaluateReason,
        StringUtils.validString(srId) ? Long.valueOf(srId) : null,
        createdTime,
        createdUser
    );
    return model;
  }
}
