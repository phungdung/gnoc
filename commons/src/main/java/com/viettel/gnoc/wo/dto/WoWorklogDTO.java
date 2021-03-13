package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class WoWorklogDTO {

  private String woWorklogId;
  private String woId;
  private String woWorklogContent;
  private String woSystem;
  private String woSystemId;
  private String userId;
  private String updateTime;
  private String username;
  private String nation;

  private String defaultSortField;

  public WoWorklogDTO(WoDTO woDTO) {
    this.woId = woDTO.getWoId();
    this.woWorklogContent = woDTO.getWoWorklog();
    try {
      this.updateTime = DateTimeUtils.convertDateToString(new Date());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      this.updateTime = woDTO.getLastUpdateTime();
    }
    this.woSystem = woDTO.getWoSystem();
    this.woSystemId = woDTO.getWoSystemId() == null ? null : woDTO.getWoSystemId().toString();
    this.userId = woDTO.getCreatePersonId() == null ? null : woDTO.getCreatePersonId().toString();
  }

  public WoWorklogInsideDTO toModelInSide() {
    WoWorklogInsideDTO model = new WoWorklogInsideDTO(
        StringUtils.validString(woWorklogId) ? Long.valueOf(woWorklogId) : null,
        StringUtils.validString(woId) ? Long.valueOf(woId) : null,
        woWorklogContent,
        woSystem,
        woSystemId,
        StringUtils.validString(userId) ? Long.valueOf(userId) : null,
        StringUtils.validString(updateTime) ? DateTimeUtils.convertStringToDate(updateTime)
            : null,
        username,
        nation
    );
    return model;
  }

}
