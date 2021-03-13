package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.maintenance.model.MrHardGroupConfigEntity;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@MultiFieldUnique(message = "{validation.mrHardGroupConfig.null.unique}", clazz = MrHardGroupConfigEntity.class, uniqueFields = "marketCode,region,arrayCode,networkType,deviceType,stationCode", idField = "id")
public class MrHardGroupConfigDTO extends BaseDto {

  //Fields
  private Long id;
  @NotEmpty(message = "{validation.mrHardGroupConfig.null.marketCode}")
  private String marketCode;
  @NotEmpty(message = "{validation.mrHardGroupConfig.null.region}")
  private String region;
  @NotEmpty(message = "{validation.mrHardGroupConfig.null.arrayCode}")
  private String arrayCode;
  private String networkType;
  private String deviceType;
  @NotNull(message = "{validation.mrHardGroupConfig.null.cdIdHard}")
  private Long cdIdHard;
  @NotEmpty(message = "{validation.mrHardGroupConfig.null.stationCode}")
  private String stationCode;
  private String updateUser;
  private Date updateDate;
  private String userMrHard;

  private String resultImport;
  private String cdIdHardStr;
  private String marketCodeStr;
  private String arrayCodeStr;
  private String networkTypeStr;
  private String stationCodeStr;
  private String deviceTypeStr;
  private String regionStr;
  private String idStr;
  private String updateDateStr;

  //Constructor
  public MrHardGroupConfigDTO(Long id, String marketCode, String region, String arrayCode,
      String networkType, String deviceType, Long cdIdHard, String stationCode,
      String updateUser, Date updateDate, String userMrHard) {
    this.id = id;
    this.marketCode = marketCode;
    this.region = region;
    this.arrayCode = arrayCode;
    this.networkType = networkType;
    this.deviceType = deviceType;
    this.cdIdHard = cdIdHard;
    this.stationCode = stationCode;
    this.updateUser = updateUser;
    this.updateDate = updateDate;
    this.userMrHard = userMrHard;
  }

  public MrHardGroupConfigEntity toEntity() {
    MrHardGroupConfigEntity model = new MrHardGroupConfigEntity(
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
    return model;
  }
}
