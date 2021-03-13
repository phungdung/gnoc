package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.UsersInsideDto;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author TungPV
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "USERS")
public class UsersEntity implements Serializable {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "USERS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "USER_ID", unique = true)
  private Long userId;

  @Column(name = "USERNAME", unique = true)
  private String username;

  @Column(name = "FULLNAME")
  private String fullname;

  @Column(name = "BIRTH_DAY")
  private Date birthDay;

  @Column(name = "SEX")
  private Long sex;

  @Column(name = "UNIT_ID")
  private Long unitId;

  @Column(name = "IS_ENABLE")
  private Long isEnable;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "MOBILE", nullable = false)
  private String mobile;

  @Column(name = "GROUP_OPERATE_ID")
  private Long groupOperateId;

  @Column(name = "OFFICE_ID")
  private Long officeId;

  @Column(name = "SHOP_ID")
  private String shopId;

  @Column(name = "SHOP_NAME")
  private String shopName;

  @Column(name = "USER_LANGUAGE")
  private String userLanguage;

  @Column(name = "USER_TIME_ZONE")
  private Long userTimeZone;

  @Column(name = "STAFF_CODE")
  private String staffCode;

  @Column(name = "IS_USE_VSMART")
  private Long isUseVsmart;

  @Column(name = "IS_ADMIN")
  private Long isAdmin;

  @Column(name = "ARRAY_ACTION")
  private Long arrayAction;

  @Column(name = "IS_NOT_RECEIVE_MESSAGE")
  private Long isNotReceiveMessage;

  @Column(name = "DEPARTMENT_ID")
  private Long departmentId;

  @Column(name = "NATION_CODE")
  private String nationCode;

  @Column(name = "NATION_ID")
  private Long nationId;

  @Column(name = "RELATE_UNITS")
  private String relateUnits;

  @Column(name = "FUNCTION_NIAM")
  private String functionNiam;

  @Column(name = "UNIT_ID_NEW")
  private Long unitIdNew;

  @Column(name = "DELETE_GROUP")
  private Long deleteGroup;

  @Column(name = "HR_UNIT_ID")
  private Long hrUnitId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATE_TIME_UNIT")
  private Date updateTimeUnit;

  @Column(name = "APPROVE_STATUS")
  private Long approveStatus;

  @Column(name = "APPROVE_USER")
  private String approveUser;

  @Column(name = "RESOURCE_CODE")
  private String resourceCode;

  public UsersInsideDto toDTO() {
    return new UsersInsideDto(userId, username, fullname, birthDay, sex, unitId, isEnable, email,
        mobile, groupOperateId, officeId, shopId, shopName, userLanguage, userTimeZone, staffCode,
        isUseVsmart, isAdmin,
        arrayAction, isNotReceiveMessage, departmentId, nationCode, nationId, relateUnits,
        functionNiam, unitIdNew, deleteGroup, hrUnitId, updateTimeUnit, approveStatus, approveUser, resourceCode);
  }
}
