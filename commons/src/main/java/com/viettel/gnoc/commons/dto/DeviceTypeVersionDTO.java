package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.DeviceTypeVersionEntity;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MultiFieldUnique(message = "{validation.version.duplicate}", clazz = DeviceTypeVersionEntity.class,
    uniqueFields = "vendorId,typeId,softwareVersion,hardwareVersion", idField = "deviceTypeVersionId")
public class DeviceTypeVersionDTO extends BaseDto {

  private Long deviceTypeVersionId;
  @NotNull(message = "validation.deviceTypeVersion.vendor.isRequired")
  private Long vendorId;
  @NotNull(message = "validation.deviceTypeVersion.typeNode.isRequired ")
  private Long typeId;
  @NotNull(message = "validation.deviceTypeVersion.softwareVersion.isRequired")
  private String softwareVersion;
  @NotNull(message = "validation.deviceTypeVersion.hardwareVersion.isRequired")
  private String hardwareVersion;
  private String temp;
  private String vendorIdStr;
  private String typeIdStr;
  private Long subTypeId;
  private String subTypeIdStr;

  public DeviceTypeVersionDTO(Long deviceTypeVersionId, Long vendorId, Long typeId,
      String softwareVersion,
      String hardwareVersion, String temp) {
    this.deviceTypeVersionId = deviceTypeVersionId;
    this.vendorId = vendorId;
    this.typeId = typeId;
    this.softwareVersion = softwareVersion;
    this.hardwareVersion = hardwareVersion;
    this.temp = temp;
  }

  public DeviceTypeVersionEntity toEntity() {
    DeviceTypeVersionEntity model = new DeviceTypeVersionEntity(
        deviceTypeVersionId,
        vendorId,
        typeId,
        softwareVersion,
        hardwareVersion,
        temp
    );
    return model;
  }
}
