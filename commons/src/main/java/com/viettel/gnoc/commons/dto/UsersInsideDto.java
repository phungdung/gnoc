package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.Users;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TungPV
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class UsersInsideDto extends BaseDto {

  private Long userId;
  private String username;
  private String fullname;
  private Date birthDay;
  private Long sex;
  private Long unitId;
  private Long isEnable;
  private String email;
  private String mobile;
  private Long groupOperateId;
  private Long officeId;
  private String shopId;
  private String shopName;
  private String userLanguage;
  private Long userTimeZone;
  private String staffCode;
  private Long isUseVsmart;
  private Long isAdmin;
  private Long arrayAction;
  private Long isNotReceiveMessage;
  private Long departmentId;
  private String nationCode;
  private Long nationId;
  private String relateUnits;
  private String functionNiam;
  private Long unitIdNew;
  private Long deleteGroup;
  private Long hrUnitId;
  private Date updateTimeUnit;
  private Long approveStatus;
  private String approveUser;
  private String resourceCode;

  private String unitName;
  private List<String> lstUserName;
  private List<String> lstUserId;
  private String smsGatewayId;
  private String cdId;
  private String aliasName;
  private String roleCode;
  private String roleName;
  private Long smsType;
  private String typeCode;
  private String usernameShow;
  private List<String> lstRole;
  private List<String> lstType;
  private List<String> lstImpact;
  private Long message;
  private String smsTypeName;
  private String unitCode;
  private String actionName;
  private String resultImport;
  private Long action;
  private String enableName;
  private String typeCodeName;
  private String unitNameNew;
  private String deleteGroupName;
  private String relateUnitsName;
  private String hrUnitName;
  private String approveStatusName;
  private String checkingExport;
  private String checkingAdmin;
  private String reasonRefusal;
  private String userIdName;
  private String isNotReceiveMessageName;
  private String checkRoleButton;
  private String unitLogin;
  private List<String> lstUnitLogin;
  private List<RolesDTO> lstRolesDTO;
  private String actionLog;

  public UsersInsideDto(Long userId, String username, String fullname, Date birthDay,
      Long sex, Long unitId, Long isEnable, String email, String mobile,
      Long groupOperateId, Long officeId, String shopId, String shopName,
      String userLanguage, Long userTimeZone, String staffCode, Long isUseVsmart,
      Long isAdmin, Long arrayAction, Long isNotReceiveMessage, Long departmentId,
      String nationCode, Long nationId, String relateUnits, String functionNiam, Long unitIdNew,
      Long deleteGroup, Long hrUnitId, Date updateTimeUnit, Long approveStatus,
      String approveUser, String resourceCode) {
    this.userId = userId;
    this.username = username;
    this.fullname = fullname;
    this.birthDay = birthDay;
    this.sex = sex;
    this.unitId = unitId;
    this.isEnable = isEnable;
    this.email = email;
    this.mobile = mobile;
    this.groupOperateId = groupOperateId;
    this.officeId = officeId;
    this.shopId = shopId;
    this.shopName = shopName;
    this.userLanguage = userLanguage;
    this.userTimeZone = userTimeZone;
    this.staffCode = staffCode;
    this.isUseVsmart = isUseVsmart;
    this.isAdmin = isAdmin;
    this.arrayAction = arrayAction;
    this.isNotReceiveMessage = isNotReceiveMessage;
    this.departmentId = departmentId;
    this.nationCode = nationCode;
    this.nationId = nationId;
    this.relateUnits = relateUnits;
    this.functionNiam = functionNiam;
    this.unitIdNew = unitIdNew;
    this.deleteGroup = deleteGroup;
    this.hrUnitId = hrUnitId;
    this.updateTimeUnit = updateTimeUnit;
    this.approveStatus = approveStatus;
    this.approveUser = approveUser;
    this.resourceCode = resourceCode;
  }

  public UsersInsideDto(Long userId, String username, String fullname, Date birthDay,
      Long sex, Long unitId, Long isEnable, String email, String mobile,
      Long groupOperateId, Long officeId, String shopId, String shopName,
      String userLanguage, Long userTimeZone, String staffCode, Long isUseVsmart,
      Long isAdmin, String unitName, String relateUnits, String cdId) {
    this.userId = userId;
    this.username = username;
    this.fullname = fullname;
    this.birthDay = birthDay;
    this.sex = sex;
    this.unitId = unitId;
    this.isEnable = isEnable;
    this.email = email;
    this.mobile = mobile;
    this.groupOperateId = groupOperateId;
    this.officeId = officeId;
    this.shopId = shopId;
    this.shopName = shopName;
    this.userLanguage = userLanguage;
    this.userTimeZone = userTimeZone;
    this.staffCode = staffCode;
    this.isUseVsmart = isUseVsmart;
    this.isAdmin = isAdmin;
    this.unitName = unitName;
    this.relateUnits = relateUnits;
    this.cdId = cdId;
  }

  public UsersEntity toEntity() {
    return new UsersEntity(userId, username, fullname, birthDay, sex, unitId, isEnable, email,
        mobile, groupOperateId, officeId, shopId, shopName, userLanguage, userTimeZone, staffCode,
        isUseVsmart, isAdmin, arrayAction, isNotReceiveMessage, departmentId, nationCode, nationId,
        relateUnits, functionNiam, unitIdNew, deleteGroup, hrUnitId, updateTimeUnit, approveStatus,
        approveUser, resourceCode);
  }

  public Users toEntityOutside() {
    return new Users(userId, username, fullname, birthDay, sex, unitId, isEnable, email,
        mobile, groupOperateId, officeId, shopId, shopName, userLanguage, userTimeZone, staffCode,
        isUseVsmart, isAdmin, arrayAction, isNotReceiveMessage, departmentId, nationCode, nationId,
        relateUnits, functionNiam, unitIdNew, deleteGroup, hrUnitId, updateTimeUnit, approveStatus,
        approveUser, resourceCode);
  }

  public UsersDTO toOutSideDto() {
    UsersDTO model = new UsersDTO(
        StringUtils.validString(userId) ? String.valueOf(userId) : null,
        username,
        fullname,
        StringUtils.validString(birthDay) ? DateTimeUtils.date2ddMMyyyyString(birthDay) : null,
        StringUtils.validString(sex) ? String.valueOf(sex) : null,
        StringUtils.validString(unitId) ? String.valueOf(unitId) : null,
        StringUtils.validString(isEnable) ? String.valueOf(isEnable) : null,
        email,
        mobile,
        StringUtils.validString(groupOperateId) ? String.valueOf(groupOperateId) : null,
        StringUtils.validString(officeId) ? String.valueOf(officeId) : null,
        shopId,
        shopName,
        userLanguage,
        StringUtils.validString(userTimeZone) ? String.valueOf(userTimeZone) : null,
        staffCode,
        StringUtils.validString(isUseVsmart) ? String.valueOf(isUseVsmart) : null,
        StringUtils.validString(isAdmin) ? String.valueOf(isAdmin) : null,
        cdId,
        unitName,
        relateUnits,
        "username",
        roleCode,
        roleName,
        aliasName,
        null
    );
    return model;
  }
}
