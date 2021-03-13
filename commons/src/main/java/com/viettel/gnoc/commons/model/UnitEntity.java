/**
 * @(#)UnitBO.java 8/5/2015 1:52 PM, Copyright 2011 Viettel Telecom. All rights reserved VIETTEL
 * PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.UnitDTO;
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
 * @author NamTN
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "UNIT")
public class UnitEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "unit_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "UNIT_ID", unique = true, nullable = false)
  private Long unitId;
  @Column(name = "UNIT_NAME", nullable = false)
  private String unitName;
  @Column(name = "UNIT_CODE", unique = true, nullable = false)
  private String unitCode;
  @Column(name = "PARENT_UNIT_ID")
  private Long parentUnitId;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "STATUS")
  private Long status;
  @Column(name = "UNIT_TYPE")
  private Long unitType;
  @Column(name = "UNIT_LEVEL")
  private Long unitLevel;
  @Column(name = "LOCATION_ID")
  private Long locationId;
  @Column(name = "IS_NOC")
  private Long isNoc;
  @Column(name = "TIME_ZONE")
  private Long timeZone;
  @Column(name = "IS_COMMITTEE")
  private Long isCommittee;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATE_TIME")
  private Date updateTime;
  @Column(name = "SMS_GATEWAY_ID")
  private Long smsGatewayId;
  @Column(name = "IPCC_SERVICE_ID")
  private Long ipccServiceId;
  @Column(name = "NATION_CODE")
  private String nationCode;
  @Column(name = "NATION_ID")
  private Long nationId;
  @Column(name = "MOBILE")
  private String mobile;
  @Column(name = "ROLE_TYPE")
  private Long roleType;
  @Column(name = "EMAIL")
  private String email;

  public UnitDTO toDTO() {
    return new UnitDTO(unitId, unitName, unitCode, parentUnitId, description, status,
        unitType, unitLevel, locationId, isNoc, timeZone, isCommittee, updateTime, smsGatewayId,
        ipccServiceId, nationCode, nationId, mobile, roleType, email);
  }

}
