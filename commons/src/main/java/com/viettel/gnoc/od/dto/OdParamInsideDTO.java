package com.viettel.gnoc.od.dto;


import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.od.model.OdParamEntity;
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
public class OdParamInsideDTO extends BaseDto {

  private Long odParamId;
  private Long odId;
  private Date updatedTime;
  private String value;
  private String paramType;
  private String updatedUser;
  private String key;
  private String getKey;

  public OdParamEntity toEntity() {
    return new OdParamEntity(odParamId, odId, updatedTime, value, paramType, updatedUser, key);
  }

  public OdParamInsideDTO(Long odParamId, Long odId, Date updatedTime, String value,
      String paramType, String updatedUser, String key) {
    this.odParamId = odParamId;
    this.odId = odId;
    this.updatedTime = updatedTime;
    this.value = value;
    this.paramType = paramType;
    this.updatedUser = updatedUser;
    this.key = key;
  }

  public OdParamDTO toDtoOutside() {
    OdParamDTO model = new OdParamDTO(
        StringUtils.validString(odParamId) ? String.valueOf(odParamId) : null,
        StringUtils.validString(odId) ? String.valueOf(odId) : null,
        StringUtils.validString(updatedTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(updatedTime)
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
