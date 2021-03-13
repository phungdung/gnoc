package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.model.SRMopEntity;
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
public class SRMopDTO extends BaseDto {

  private String id;
  private String srId;
  private String dtId;
  private String dtName;
  private String templateId;
  private String type;
  private String ipNode;
  private String processId;

  public SRMopEntity toEntity() {
    try {
      SRMopEntity model = new SRMopEntity(
          StringUtils.validString(id) ? Long.valueOf(id) : null,
          StringUtils.validString(srId) ? Long.valueOf(srId) : null,
          StringUtils.validString(dtId) ? Long.valueOf(dtId) : null,
          dtName,
          StringUtils.validString(templateId) ? Long.valueOf(templateId) : null,
          type, ipNode, processId);
      return model;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
