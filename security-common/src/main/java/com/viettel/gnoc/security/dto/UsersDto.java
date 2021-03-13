package com.viettel.gnoc.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
/**
 * @author TungPV
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersDto {

  private Long userId;
  private String username;
  private String fullname;
  private Timestamp birthDay;
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
  private String unitName;
  private List<String> lstUserName;
  private List<String> lstUserId;
  private String smsGatewayId;
  private String languageKey;

}
