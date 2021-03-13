package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.model.SRParamEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SRParamDTO {

  private String srId;
  private String updatedTime;
  private String srParamId;
  private String value;
  private String paramType;
  private String updatedUser;
  private String key;
  private String defaultSoftField = "name";

  public SRParamDTO(String srId, String updatedTime, String srParamId, String value,
      String paramType, String updatedUser, String key) {
    this.srId = srId;
    this.updatedTime = updatedTime;
    this.srParamId = srParamId;
    this.value = value;
    this.paramType = paramType;
    this.updatedUser = updatedUser;
    this.key = key;
  }

  public SRParamEntity toEntity() {
    try {
      SRParamEntity model = new SRParamEntity(
          StringUtils.validString(srId) ? Long.valueOf(srId) : null
          , StringUtils.validString(updatedTime) ? DateTimeUtils.convertStringToDate(updatedTime)
          : null
          , StringUtils.validString(srParamId) ? Long.valueOf(srParamId) : null
          , value
          , paramType
          , updatedUser
          , key
      );
      return model;
    } catch (Exception e) {

    }
    return null;
  }
}
