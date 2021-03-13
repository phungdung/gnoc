/**
 * @(#)RolesBO.java 8/27/2015 5:34 PM, Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.RolesDTO;
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
@Table(schema = "COMMON_GNOC", name = "ROLES")
public class RolesEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "roles_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ROLE_ID", unique = true)
  private Long roleId;

  @Column(name = "STATUS", nullable = false)
  private Long status;

  @Column(name = "ROLE_NAME", nullable = false)
  private String roleName;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "ROLE_CODE")
  private String roleCode;

  public RolesDTO toDTO() {
    RolesDTO dto = new RolesDTO(
        roleId == null ? null : roleId.toString(),
        status == null ? null : status.toString(),
        roleName,
        description,
        roleCode
    );
    return dto;
  }
}

