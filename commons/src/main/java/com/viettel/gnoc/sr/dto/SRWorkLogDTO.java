package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.sr.model.SRWorkLogEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SRWorkLogDTO extends BaseDto {

  private Long wlId;
  private Long wlTypeId;
  private String wlText;
  private String createdUser;
  private Date createdTime;
  private Long srId;
  private Long reasonRejectId;
  private String unitName;
  private String wlTypeName;

  public SRWorkLogDTO(Long wlId, Long wlTypeId, String wlText, String createdUser,
      Date createdTime, Long srId, Long reasonRejectId) {
    this.wlId = wlId;
    this.wlTypeId = wlTypeId;
    this.wlText = wlText;
    this.createdUser = createdUser;
    this.createdTime = createdTime;
    this.srId = srId;
    this.reasonRejectId = reasonRejectId;
  }

  public SRWorkLogEntity toEntity() {
    SRWorkLogEntity entity = new SRWorkLogEntity(
        wlId,
        wlTypeId,
        wlText,
        createdUser,
        createdTime,
        srId,
        reasonRejectId
    );
    return entity;
  }
}
