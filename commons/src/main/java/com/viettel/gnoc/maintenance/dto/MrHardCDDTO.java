package com.viettel.gnoc.maintenance.dto;


import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.maintenance.model.MrHardCdEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author trungduong
 */
@Getter
@Setter
@NoArgsConstructor
@Slf4j
@MultiFieldUnique(message = "{validation.MrHardCDDTO.null.unique}", clazz = MrHardCdEntity.class, uniqueFields = "countryCode,region,cdId,stationCode", idField = "hardCDId")
public class MrHardCDDTO extends BaseDto {

  private Long hardCDId;
  private String countryCode;
  private Long cdId;
  private String stationCode;
  private String createUser;
  private String region;

  private String regionName;
  private String countryName;
  private String woGroupName;

  public MrHardCDDTO(Long hardCDId, String countryCode, Long cdId, String stationCode,
      String createUser, String region) {
    this.hardCDId = hardCDId;
    this.countryCode = countryCode;
    this.cdId = cdId;
    this.stationCode = stationCode;
    this.createUser = createUser;
    this.region = region;
  }

  public MrHardCdEntity toEntity() {
    MrHardCdEntity model = new MrHardCdEntity(
        hardCDId
        , countryCode
        , cdId
        , stationCode
        , createUser
        , region
    );
    return model;
  }

}
