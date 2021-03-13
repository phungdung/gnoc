package com.viettel.gnoc.sr.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SRWorklogTypeDTO {

  private Long wlTypeId;
  private String wlTypeCode;
  private String wlTypeName;
  private String srStatus;
  private String status;
  private String createdUser;
  private Date createdTime;
  private String updatedUser;
  private Date updatedTime;

  public SRWorklogTypeDTO toEntity() {
    return new SRWorklogTypeDTO(wlTypeId, wlTypeCode, wlTypeName, srStatus, status, createdUser,
        createdTime, updatedUser, updatedTime);
  }
}
