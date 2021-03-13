package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.maintenance.model.MrCauseWoWasCompletedEntity;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.mrCauseWoWasCompleted.null.unique}", clazz = MrCauseWoWasCompletedEntity.class, uniqueFields = "reasonCode,reasonType", idField = "id")
public class MrCauseWoWasCompletedDTO extends BaseDto {

  private Long id;
  @NotNull(message = "{validation.mrCauseWoWasCompleted.reasonCode.NotNull}")
  private String reasonCode;
  private String reasonName;
  @NotNull(message = "{validation.mrCauseWoWasCompleted.reasonType.NotNull}")
  private String reasonType;
  private String waitingTime;
  private String validateProcess;
  private String updatedUser;
  private Date updatedTime;
  private String resultImport;
  private String reasonTypeName;

  public MrCauseWoWasCompletedDTO(Long id, String reasonCode, String reasonName, String reasonType,
      String waitingTime, String validateProcess, String updatedUser, Date updatedTime) {
    this.id = id;
    this.reasonCode = reasonCode;
    this.reasonName = reasonName;
    this.reasonType = reasonType;
    this.waitingTime = waitingTime;
    this.validateProcess = validateProcess;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
  }

  public MrCauseWoWasCompletedEntity toEntity() {
    MrCauseWoWasCompletedEntity model = new MrCauseWoWasCompletedEntity(
        id, reasonCode, reasonName, reasonType,
        StringUtils.validString(waitingTime) ? Double.valueOf(waitingTime) : null, validateProcess,
        updatedUser,
        updatedTime);
    return model;
  }

}
