package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrConfigTestXaEntity;
import java.util.List;
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
public class MrConfigTestXaDTO extends BaseDto implements Cloneable{

  private String configId;
  private String country;
  private String province;
  private String timeTestXa;
  private String stationAtATime;
  private String excepDistrict;
  private String excepStation;
  private String status;
  private String createTime;
  private String createUser;
  private String updateTime;
  private String updateUser;
  private String station;
  private String resultImport;

  public MrConfigTestXaDTO(String configId, String country, String province, String timeTestXa,
      String stationAtATime, String excepDistrict, String excepStation, String status,
      String createTime, String createUser, String updateTime, String updateUser) {
    this.configId = configId;
    this.country = country;
    this.province = province;
    this.timeTestXa = timeTestXa;
    this.stationAtATime = stationAtATime;
    this.excepDistrict = excepDistrict;
    this.excepStation = excepStation;
    this.status = status;
    this.createTime = createTime;
    this.createUser = createUser;
    this.updateTime = updateTime;
    this.updateUser = updateUser;
  }

  public MrConfigTestXaEntity toEntity() {
    return new MrConfigTestXaEntity(
        StringUtils.validString(configId)? Long.valueOf(configId) : null,
        country,
        province,
        Long.valueOf(timeTestXa),
        Long.valueOf(stationAtATime),
        StringUtils.validString(excepDistrict)? excepDistrict : null,
        StringUtils.validString(excepStation)? excepStation : null,
        Long.valueOf(status),
        DateTimeUtils.convertStringToDate(createTime),
        createUser,
        StringUtils.validString(updateTime)? DateTimeUtils.convertStringToDate(updateTime) : null,
        StringUtils.validString(updateUser)? updateUser : null
    );
  }

  @Override
  public MrConfigTestXaDTO clone() {
    try {
      return (MrConfigTestXaDTO) super.clone();
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }
  }
}
