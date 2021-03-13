package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class UsersDTO {

  private String userId;
  private String username;
  private String fullname;
  private String birthDay;
  private String sex;
  private String unitId;
  private String isEnable;
  private String email;
  private String mobile;
  private String groupOperateId;
  private String officeId;
  private String shopId;
  private String shopName;
  private String userLanguage;
  private String userTimeZone;
  private String staffCode;
  private String isUseVsmart;
  private String isAdmin;
  private String cdId;
  private String unitName;
  private String relateUnits;
  private String defaultSortField;
  private String roleCode;
  private String roleName;
  private String aliasName;
  private String unitCode;

  public UsersDTO() {
    setDefaultSortField("username");
  }

  public UsersInsideDto toInSideDto() {
    UsersInsideDto model = null;
    try {
      model = new UsersInsideDto(
          StringUtils.validString(userId) ? Long.valueOf(userId) : null,
          username,
          fullname,
          StringUtils.validString(birthDay) ? DateTimeUtils.convertStringToDateTime(birthDay)
              : null,
          StringUtils.validString(sex) ? Long.valueOf(sex) : null,
          StringUtils.validString(unitId) ? Long.valueOf(unitId) : null,
          StringUtils.validString(isEnable) ? Long.valueOf(isEnable) : null,
          email,
          mobile,
          StringUtils.validString(groupOperateId) ? Long.valueOf(groupOperateId) : null,
          StringUtils.validString(officeId) ? Long.valueOf(officeId) : null,
          shopId,
          shopName,
          userLanguage,
          StringUtils.validString(userTimeZone) ? Long.valueOf(userTimeZone) : null,
          staffCode,
          StringUtils.validString(isUseVsmart) ? Long.valueOf(isUseVsmart) : null,
          StringUtils.validString(isAdmin) ? Long.valueOf(isAdmin) : null,
          relateUnits, unitName, cdId);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return model;
  }

}
