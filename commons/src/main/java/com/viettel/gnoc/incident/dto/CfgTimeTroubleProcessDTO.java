package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.commons.validator.SizeByte;
import com.viettel.gnoc.incident.model.CfgTimeTroubleProcessEntity;
import java.util.Date;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.CfgTimeTroubleProcessDTO.multiple.unique}", clazz = CfgTimeTroubleProcessEntity.class,
    uniqueFields = "country,typeId,subCategoryId,priorityId", idField = "id")
public class CfgTimeTroubleProcessDTO extends BaseDto {

  private Long id;
  @NotNull(message = "validation.CfgTimeTroubleProcessDTO.typeId.NotNull")
  private Long typeId;
  @NotNull(message = "validation.CfgTimeTroubleProcessDTO.subCategoryId.NotNull")
  private Long subCategoryId;
  @NotNull(message = "validation.CfgTimeTroubleProcessDTO.priorityId.NotNull")
  private Long priorityId;
  @NotNull(message = "validation.CfgTimeTroubleProcessDTO.createTtTime.NotNull")
  @DecimalMax(value = "10000.00", message = "validation.CfgTimeTroubleProcessDTO.createTtTime.max")
  private Double createTtTime;
  @NotNull(message = "validation.CfgTimeTroubleProcessDTO.processTtTime.NotNull")
  @DecimalMax(value = "10000.00", message = "validation.CfgTimeTroubleProcessDTO.processTtTime.max")
  private Double processTtTime;
  @DecimalMax(value = "10000.00", message = "validation.CfgTimeTroubleProcessDTO.closeTtTime.max")
  private Double closeTtTime;
  @DecimalMax(value = "10000.00", message = "validation.CfgTimeTroubleProcessDTO.processWoTime.max")
  private Double processWoTime;
  private Long isCall;
  private Date lastUpdateTime;
  @SizeByte(max = 255, message = "validation.CfgTimeTroubleProcessDTO.country.tooLong")
  private String country;
  private Double timeAboutOverdue;
  private Long isStationVip;
  @DecimalMax(value = "10000.00", message = "validation.CfgTimeTroubleProcessDTO.timeStationVip.max")
  private Double timeStationVip;
  @DecimalMax(value = "10000.00", message = "validation.CfgTimeTroubleProcessDTO.timeWoVip.max")
  private Double timeWoVip;
  @Size(max = 256, message = "validation.CfgTimeTroubleProcessDTO.cdAudioName.tooLong")
  private String cdAudioName;
  @Size(max = 256, message = "validation.CfgTimeTroubleProcessDTO.ftAudioName.tooLong")
  private String ftAudioName;

  private String typeName;
  private String subCategoryName;
  private String priorityName;
  private String countryName;
  private Long countryId;
  private String isCallName;
  private String stationVip;

  public CfgTimeTroubleProcessDTO(Long id, Long typeId, Long subCategoryId, Long priorityId,
      Double createTtTime, Double processTtTime, Double closeTtTime, Double processWoTime,
      Long isCall, Date lastUpdateTime, String country, Double timeAboutOverdue,
      Long isStationVip, Double timeStationVip, Double timeWoVip, String cdAudioName,
      String ftAudioName) {
    this.id = id;
    this.typeId = typeId;
    this.subCategoryId = subCategoryId;
    this.priorityId = priorityId;
    this.createTtTime = createTtTime;
    this.processTtTime = processTtTime;
    this.closeTtTime = closeTtTime;
    this.processWoTime = processWoTime;
    this.isCall = isCall;
    this.lastUpdateTime = lastUpdateTime;
    this.country = country;
    this.timeAboutOverdue = timeAboutOverdue;
    this.isStationVip = isStationVip;
    this.timeStationVip = timeStationVip;
    this.timeWoVip = timeWoVip;
    this.cdAudioName = cdAudioName;
    this.ftAudioName = ftAudioName;
  }

  public CfgTimeTroubleProcessEntity toEntity() {
    return new CfgTimeTroubleProcessEntity(id, typeId, subCategoryId, priorityId, createTtTime,
        processTtTime, closeTtTime, processWoTime, isCall, lastUpdateTime, country,
        timeAboutOverdue, isStationVip, timeStationVip, timeWoVip, cdAudioName, ftAudioName);
  }
}
