package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.sr.model.SRConfigEntity;
import groovy.util.logging.Slf4j;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author kienpv
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
//@Unique(message = "{validation.SrServiceArray.isDuplicate.configCode}", clazz = SRConfigEntity.class, uniqueField = "configCode",idField = "configId")
public class SRConfigDTO extends BaseDto {

  private Long configId;

  private String configGroup;
  @NotEmpty(message = "{validation.srProcess.null.configCode}")
  @Size(max = 4000, message = "validation.srProcess.configCode.tooLong")
  private String configCode;
  @Size(max = 200, message = "validation.srProcess.configName.tooLong")
  @NotEmpty(message = "{validation.srProcess.null.configName}")
  private String configName;
  private String createdUser;
  private String createdTime;
  private String updatedUser;
  private String updatedTime;
  private String status;
  private String parentGroup;
  private String parentCode;

  private String parentName;
  private String serviceName;
  private String serviceCode;
  private String serviceGroup;
  private String configDes;

  //  @NotEmpty(message = "{validation.SrServiceArray.country.isNull}")
  private String country;
  private String countryDisplay;
  private String automation;
  private String configGroupName;
  private Boolean btnDelete;
  private String resultImport;
  private String action;
  private String defaultSortField = "configId";
  private String srCfgServiceIds;

  public SRConfigDTO(Long configId, String configGroup, String configCode, String configName,
      String createdUser, String createdTime,
      String updatedUser, String updatedTime, String status, String parentGroup, String parentCode,
      String country, String automation, String srCfgServiceIds) {
    this.configId = configId;
    this.configGroup = configGroup;
    this.configCode = configCode;
    this.configName = configName;
    this.createdUser = createdUser;
    this.createdTime = createdTime;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
    this.status = status;
    this.parentGroup = parentGroup;
    this.parentCode = parentCode;
    this.country = country;
    this.automation = automation;
    this.srCfgServiceIds = srCfgServiceIds;
  }

  public SRConfigEntity toEntity() {
    SRConfigEntity entity = new SRConfigEntity(
        configId,
        configGroup,
        configCode,
        configName,
        createdUser,
        createdTime == null ? null
            : DateTimeUtils.convertStringToTimestamp(createdTime),
        updatedUser,
        updatedTime == null ? null
            : DateTimeUtils.convertStringToTimestamp(updatedTime),
        status,
        parentGroup,
        parentCode,
        country == null ? null : Long.parseLong(country),
        automation,
        srCfgServiceIds
    );
    return entity;
  }

  private String key;
  private String message;

  public SRConfigDTO(String key, String message) {
    this.key = key;
    this.message = message;
  }
}
