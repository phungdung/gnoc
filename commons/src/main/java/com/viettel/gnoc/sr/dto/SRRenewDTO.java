package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.sr.model.SRRenewEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SRRenewDTO {

  private Long renewId;
  private Long srId;
  private Date endTime;
  private Date createdTime;
  private String reasonRenew;
  private Long statusRenew;
  private Date endTimeRenew;
  private String reasonReject;
  private Date updatedTime;
  private String createdUser;
  private String updatedUser;
  private String statusRenewStr;

  public SRRenewEntity toEntity() {
    return new SRRenewEntity(renewId, srId, endTime, createdTime, reasonRenew, statusRenew
        , endTimeRenew, reasonReject, updatedTime, createdUser
    );
  }

}
