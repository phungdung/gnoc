/**
 * @(#)RolesBO.java 8/27/2015 5:34 PM, Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.RoleUserDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author tuanpv14
 * @version 1.0
 * @since 8/27/2015 5:34 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "ROLE_USER")
public class RoleUserEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "role_user_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ROLE_USER_ID", unique = true, nullable = false)
  private Long roleUserId;

  @Column(name = "IS_ADMIN", nullable = false)
  private Long isAdmin;

  @Column(name = "USER_ID", nullable = false)
  private Long userId;

  @Column(name = "IS_ACTIVE")
  private Long isActive;

  @Column(name = "ROLE_ID", nullable = false)
  private Long roleId;

  public RoleUserDTO toDTO() {
    RoleUserDTO dto = new RoleUserDTO(
        roleUserId == null ? null : roleUserId.toString(),
        isAdmin == null ? null : isAdmin.toString(),
        userId == null ? null : userId.toString(),
        isActive == null ? null : isActive.toString(),
        roleId == null ? null : roleId.toString()
    );
    return dto;
  }
}

