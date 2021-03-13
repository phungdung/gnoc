package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrConfigTestXaDTO;
import java.util.Date;
import java.util.List;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_CONFIG_TESTXA")
public class MrConfigTestXaEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.MR_CONFIG_TESTXA_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CONFIG_ID", unique = true, nullable = false)
  private Long configId;

  @Column(name = "COUNTRY")
  private String country;

  @Column(name = "PROVINCE")
  private String province;

  @Column(name = "TIME_TESTXA")
  private Long timeTestXa;

  @Column(name = "STATION_AT_A_TIME")
  private Long stationAtATime;

  @Column(name = "EXCEP_DISTRICT")
  private String excepDistrict;

  @Column(name = "EXCEP_STATION")
  private String excepStation;

  @Column(name = "STATUS")
  private Long status;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATE_TIME")
  private Date createTime;

  @Column(name = "CREATE_USER")
  private String createUser;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATE_TIME")
  private Date updateTime;

  @Column(name = "UPDATE_USER")
  private String updateUser;

  public MrConfigTestXaDTO toDTO() {
    return new MrConfigTestXaDTO(
        String.valueOf(configId),
        country,
        province,
        String.valueOf(timeTestXa),
        String.valueOf(stationAtATime),
        StringUtils.validString(excepDistrict)? excepDistrict : null,
        StringUtils.validString(excepStation)? excepStation : null,
        String.valueOf(status),
        DateTimeUtils.convertDateToString(createTime),
        createUser,
        updateTime != null? DateTimeUtils.convertDateToString(updateTime, DateTimeUtils.patternDateTimeMs) : null,
        StringUtils.validString(updateUser)? updateUser : null
    );
  }
}
