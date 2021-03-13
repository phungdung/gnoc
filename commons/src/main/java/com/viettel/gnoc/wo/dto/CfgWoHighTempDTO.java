package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.maintenance.model.MrCauseWoWasCompletedEntity;
import com.viettel.gnoc.wo.model.AutoCreateWoOsEntity;
import com.viettel.gnoc.wo.model.CfgWoHighTempEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.cfgWoHighTemp.null.unique}", clazz = CfgWoHighTempEntity.class, uniqueFields = "reasonLv1Id,reasonLv2Id,actionId", idField = "id")
public class CfgWoHighTempDTO extends BaseDto {

  private Long id;
  private Long reasonLv1Id;
  private Long reasonLv2Id;
  private Long actionId;
  private String actionNameId;
  private Long priorityId;
  private String cdType;
  private Long processTime;
  private Long isAssignFt;
  private String reportFailureType;
  private Long status;

  private String statusName;
  private String cdTypeName;
  private String reasonLv1Name;
  private String reasonLv2Name;
  private String actionIdStr;
  private String actionNameIdStr;
  private String priorityName;
  private String isAssignFtName;
  private String reportFailureTypeName;
  private String processTimeStr;
  private String resultImport;

  public CfgWoHighTempDTO(Long id, Long reasonLv1Id, Long reasonLv2Id, Long actionId,
      String actionNameId, Long priorityId, String cdType, Long processTime, Long isAssignFt,
      String reportFailureType, Long status) {
    this.id = id;
    this.reasonLv1Id = reasonLv1Id;
    this.reasonLv2Id = reasonLv2Id;
    this.actionId = actionId;
    this.actionNameId = actionNameId;
    this.priorityId = priorityId;
    this.cdType = cdType;
    this.processTime = processTime;
    this.isAssignFt = isAssignFt;
    this.reportFailureType = reportFailureType;
    this.status = status;
  }

  public CfgWoHighTempEntity toEntity() {
    return new CfgWoHighTempEntity(id, reasonLv1Id, reasonLv2Id, actionId, actionNameId, priorityId,
        cdType, processTime, isAssignFt, reportFailureType, status);
  }
}
