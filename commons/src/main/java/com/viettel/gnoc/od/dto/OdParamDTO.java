package com.viettel.gnoc.od.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


/**
 * @author ITSOL
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class OdParamDTO {

  private String odParamId;
  private String odId;
  private String updatedTime;
  private String value;
  private String paramType;
  private String updatedUser;
  private String key;
  private String getKey;

  public OdParamInsideDTO toDtoInside() {
    OdParamInsideDTO model = new OdParamInsideDTO(
        StringUtils.validString(odParamId) ? Long.valueOf(odParamId) : null,
        StringUtils.validString(odId) ? Long.valueOf(odId) : null,
        StringUtils.validString(updatedTime) ? DateTimeUtils.convertStringToDate(updatedTime)
            : null,
        value,
        paramType,
        updatedUser,
        key,
        getKey
    );
    return model;
  }
}
