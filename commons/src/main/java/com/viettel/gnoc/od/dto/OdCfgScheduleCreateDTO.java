package com.viettel.gnoc.od.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ReceiveUnitDTO;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.od.model.OdCfgScheduleCreateEntity;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.odCfgScheduleCreateDTO.multiple.unique}", clazz = OdCfgScheduleCreateEntity.class,
    uniqueFields = "odTypeId,odPriority,receiveUnit", idField = "id")
public class OdCfgScheduleCreateDTO extends BaseDto {

  private Long id;
  @NotEmpty(message = "validation.odCfgScheduleCreateDTO.odName.NotEmpty")
  private String odName;
  private String odDescription;
  @NotNull(message = "validation.odCfgScheduleCreateDTO.odTypeId.NotNull")
  private Long odTypeId;
  @NotNull(message = "validation.odCfgScheduleCreateDTO.odPriority.NotNull")
  private Long odPriority;
  @NotNull(message = "validation.odCfgScheduleCreateDTO.schedule.NotNull")
  private Long schedule;
  @NotEmpty(message = "validation.odCfgScheduleCreateDTO.receiveUnit.NotEmpty")
  private String receiveUnit;
  private String odFileId;
  private Date lastUpdateTime;

  private String odGroupTypeName;
  private String odTypeName;
  private String odPriorityName;
  private String scheduleName;
  private String receiveUnitName;
  private List<ReceiveUnitDTO> receiveUnitDTOList;
  private List<GnocFileDto> gnocFileDtos;
  private List<Long> idDeleteList;

  public OdCfgScheduleCreateEntity toEntity() {
    return new OdCfgScheduleCreateEntity(id, odName, odDescription, odTypeId, odPriority, schedule,
        receiveUnit, odFileId, lastUpdateTime);
  }

  public OdCfgScheduleCreateDTO(Long id, String odName, String odDescription, Long odTypeId,
      Long odPriority,
      Long schedule, String receiveUnit, String odFileId, Date lastUpdateTime) {
    this.id = id;
    this.odName = odName;
    this.odDescription = odDescription;
    this.odTypeId = odTypeId;
    this.odPriority = odPriority;
    this.schedule = schedule;
    this.receiveUnit = receiveUnit;
    this.odFileId = odFileId;
    this.lastUpdateTime = lastUpdateTime;
  }
}
