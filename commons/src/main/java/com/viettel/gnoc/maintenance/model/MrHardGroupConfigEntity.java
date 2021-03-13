package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrHardGroupConfigDTO;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_HARD_GROUP_CONFIG")
public class MrHardGroupConfigEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_HARD_GROUP_CONFIG_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "MARKET_CODE")
  private String marketCode;

  @Column(name = "REGION")
  private String region;

  @Column(name = "ARRAY_CODE")
  private String arrayCode;

  @Column(name = "NETWORK_TYPE")
  private String networkType;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "CD_ID_HARD")
  private Long cdIdHard;

  @Column(name = "STATION_CODE")
  private String stationCode;

  @Column(name = "UPDATE_USER")
  private String updateUser;

  @Column(name = "UPDATE_DATE")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date updateDate;

  @Column(name = "USER_MR_HARD")
  private String userMrHard;

  public MrHardGroupConfigDTO toDTO() {
    MrHardGroupConfigDTO dto = new MrHardGroupConfigDTO(
        id,
        marketCode,
        region,
        arrayCode,
        networkType,
        deviceType,
        cdIdHard,
        stationCode,
        updateUser,
        updateDate,
        userMrHard
    );
    return dto;
  }
}
