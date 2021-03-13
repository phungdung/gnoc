package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.DeviceTypeVersionEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTypeVersionExportDTO extends BaseDto {

  private Long deviceTypeVersionId;
  private Long vendorId;
  private Long typeId;
  private String softwareVersion;
  private String hardwareVersion;
  private String temp;
  private String vendorIdStr;
  private String typeIdStr;
  private Long subTypeId;
  private String subTypeIdStr;
  private String resultImport;

  public DeviceTypeVersionExportDTO(String subTypeIdStr, String typeIdStr, String vendorIdStr,
      String hardwareVersion, String softwareVersion) {
    this.subTypeIdStr = subTypeIdStr;
    this.typeIdStr = typeIdStr;
    this.vendorIdStr = vendorIdStr;
    this.softwareVersion = softwareVersion;
    this.hardwareVersion = hardwareVersion;
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
