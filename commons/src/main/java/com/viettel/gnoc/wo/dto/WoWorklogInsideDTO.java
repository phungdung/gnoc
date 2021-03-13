package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.model.WoWorklogEntity;
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
public class WoWorklogInsideDTO extends BaseDto {

  private Long woWorklogId;
  private Long woId;
  private String woWorklogContent;
  private String woSystem;
  private String woSystemId;
  private Long userId;
  private Date updateTime;
  private String username;
  private String nation;

  private Double offset;

  public WoWorklogInsideDTO(Long woWorklogId, Long woId, String woWorklogContent, String woSystem,
      String woSystemId, Long userId, Date updateTime, String username, String nation) {
    this.woWorklogId = woWorklogId;
    this.woId = woId;
    this.woWorklogContent = woWorklogContent;
    this.woSystem = woSystem;
    this.woSystemId = woSystemId;
    this.userId = userId;
    this.updateTime = updateTime;
    this.username = username;
    this.nation = nation;
  }

  public WoWorklogInsideDTO(WoInsideDTO woInsideDTO) {
    this.woId = woInsideDTO.getWoId();
    this.woWorklogContent = woInsideDTO.getWoWorklogContent();
    this.updateTime = woInsideDTO.getLastUpdateTime();
    this.woSystem = woInsideDTO.getWoSystem();
    this.woSystemId = woInsideDTO.getWoSystemId() == null ? null : woInsideDTO.getWoSystemId();
    this.userId = woInsideDTO.getCreatePersonId() == null ? null : woInsideDTO.getCreatePersonId();
  }

  public WoWorklogEntity toEntity() {
    return new WoWorklogEntity(woWorklogId, woId, woWorklogContent, woSystem, woSystemId, userId,
        updateTime, username, nation);
  }

  public WoWorklogDTO toModelOutSide() {
    WoWorklogDTO model = new WoWorklogDTO(
        StringUtils.validString(woWorklogId) ? String.valueOf(woWorklogId) : null,
        StringUtils.validString(woId) ? String.valueOf(woId) : null,
        woWorklogContent,
        woSystem,
        woSystemId,
        StringUtils.validString(userId) ? String.valueOf(userId) : null,
        StringUtils.validString(updateTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(updateTime)
            : null,
        username,
        nation,
        "name"
    );
    return model;
  }
}
