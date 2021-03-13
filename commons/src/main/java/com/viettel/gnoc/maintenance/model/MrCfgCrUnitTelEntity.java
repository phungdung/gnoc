package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrCfgCrUnitTelDTO;
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

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "OPEN_PM", name = "MR_CFG_CR_UNIT_TEL")
public class MrCfgCrUnitTelEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_CFG_CR_IMPL_UNIT_TEL_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CFG_ID", unique = true, nullable = false)
  private Long cfgId;

  @Column(name = "MARKET_CODE", nullable = false)
  private String marketCode;

  @Column(name = "ARRAY_CODE", nullable = false)
  private String arrayCode;

  @Column(name = "DEVICE_TYPE", nullable = false)
  private String deviceType;

  @Column(name = "IMPLEMENT_UNIT", nullable = false)
  private Long implementUnit;

  @Column(name = "CHECKING_UNIT", nullable = false)
  private Long checkingUnit;

  @Column(name = "CREATED_USER")
  private String createdUser;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "UPATED_USER")
  private String updatedUser;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATED_TIME")
  private Date updatedTime;

  @Column(name = "REGION")
  private String region;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "NETWORK_TYPE")
  private String networkType;

  public MrCfgCrUnitTelDTO toDTO() {
    return new MrCfgCrUnitTelDTO(cfgId, marketCode, arrayCode, deviceType, implementUnit,
        checkingUnit, createdUser, createdTime, updatedUser, updatedTime, region, createdDate,
        networkType);
  }

}
