package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.sr.model.SRActionCodeEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SRActionCodeDTO {

  private String oldStatus;
  private String newStatus;
  private String actionCode;
  private String defaultComment;
  private String createdUser;
  private Date createdTime;
  private Long acId;

  public SRActionCodeEntity toEntity() {
    return new SRActionCodeEntity(oldStatus, newStatus, actionCode, defaultComment, createdUser,
        createdTime, acId);
  }
}
