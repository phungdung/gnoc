package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.cr.model.DeviceTypesEntity;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Unique(message = "{validation.deviceTypesDTO.deviceTypeCode.unique}", clazz = DeviceTypesEntity.class, uniqueField = "deviceTypeCode", idField = "deviceTypeId")
public class DeviceTypesDTO extends BaseDto {

  private Long deviceTypeId;
  @NotEmpty(message = "{validation.deviceTypesDTO.deviceTypeCode.NotEmpty}")
  private String deviceTypeCode;
  @NotEmpty(message = "{validation.deviceTypesDTO.deviceTypeName.NotEmpty}")
  private String deviceTypeName;
  private String description;
  private Long isActive;
  private List<LanguageExchangeDTO> listDeviceTypeName;

  public DeviceTypesDTO(Long deviceTypeId, String deviceTypeCode, String deviceTypeName,
      String description, Long isActive) {
    this.deviceTypeId = deviceTypeId;
    this.deviceTypeCode = deviceTypeCode;
    this.deviceTypeName = deviceTypeName;
    this.description = description;
    this.isActive = isActive;
  }

  public DeviceTypesEntity toEntity() {
    return new DeviceTypesEntity(deviceTypeId, deviceTypeCode, deviceTypeName,
        description, isActive);
  }
}
