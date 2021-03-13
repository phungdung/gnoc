/**
 * @(#)CatLocationBO.java 8/25/2015 2:47 PM, Copyright 2011 Viettel Telecom. All rights reserved
 * VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
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
 * @author ITSOL
 */

@Entity
@Table(schema = "COMMON_GNOC", name = "CAT_LOCATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatLocationEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CAT_LOCATION_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "LOCATION_ID", unique = true, nullable = false)
  private Long locationId;

  @Column(name = "LOCATION_CODE", nullable = false)
  private String locationCode;

  @Column(name = "LOCATION_NAME", nullable = false)
  private String locationName;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "PARENT_ID")
  private Long parentId;

  @Column(name = "PARENT_CODE")
  private String parentCode;

  @Column(name = "LOCATION_ADMIN_LEVEL")
  private String locationAdminLevel;

  @Column(name = "TERRAIN")
  private String terrain;

  @Column(name = "PRE_CODE_STATION")
  private String preCodeStation;

  @Column(name = "STATUS")
  private Long status;

  @Column(name = "PLACE")
  private Long place;

  @Column(name = "FEATURE_LOCATION")
  private Long featureLocation;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_TIME")
  private Date lastUpdateTime;

  @Column(name = "NATION_ID")
  private Long nationId;

  @Column(name = "NATION_CODE")
  private String nationCode;

  public CatLocationDTO toDTO() {
    CatLocationDTO dto = new CatLocationDTO(
        locationId == null ? null : locationId.toString(),
        locationCode,
        locationName,
        description,
        parentId == null ? null : parentId.toString(),
        parentCode,
        locationAdminLevel,
        terrain,
        preCodeStation,
        status == null ? null : status.toString(),
        place == null ? null : place.toString(),
        featureLocation == null ? null : featureLocation.toString(),
        lastUpdateTime == null ? null
            : DateTimeUtils.convertDateToString(lastUpdateTime, Constants.ddMMyyyy),
        nationId == null ? null : nationId.toString(),
        nationCode
    );
    return dto;
  }
}

