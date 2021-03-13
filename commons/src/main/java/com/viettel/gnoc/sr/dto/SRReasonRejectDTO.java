package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.sr.model.SRReasonRejectEntity;
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
public class SRReasonRejectDTO extends BaseDto {

  private Long reasonRejectId;
  private String srStatus;
  private Date createdTime;
  private String createdUser;
  private String reason;

  public SRReasonRejectEntity toEntity() {
    SRReasonRejectEntity entity = new SRReasonRejectEntity(
        reasonRejectId,
        srStatus,
        createdTime,
        createdUser,
        reason
    );
    return entity;
  }
}
