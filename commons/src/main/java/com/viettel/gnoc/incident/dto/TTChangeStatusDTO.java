package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.validator.MultiFieldUniqueHasNull;
import com.viettel.gnoc.incident.model.TTChangeStatusEntity;
import com.viettel.gnoc.risk.model.RiskChangeStatusEntity;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@MultiFieldUniqueHasNull(message = "{validation.ttChangeStatusDTO.multiple.unique}", clazz = TTChangeStatusEntity.class,
    uniqueFields = "ttTypeId,alarmGroup,oldStatus,newStatus", idField = "id")
public class TTChangeStatusDTO extends BaseDto {

  private Long id;
  private Long ttTypeId;
  private String alarmGroup;
  @NotNull(message = "validation.ttChangeStatusDTO.oldStatus.notNull")
  private Long oldStatus;
  @NotNull(message = "validation.ttChangeStatusDTO.newStatus.notNull")
  private Long newStatus;
  private Long sendCreate;
  private String createContent;
  private Long sendReceiveUser;
  private String receiveUserContent;
  private Long sendCreateUnit;
  private String createUnitContent;
  private Long sendReceiveUnitLv1;
  private String receiveUnitLv1Content;
  private Long sendReceiveUnitLv2;
  private String receiveUnitLv2Content;
  private Long isDefault;
  private Long ttPriority;
  private String nextAction;
  private Long scopeOfUse;

  private String isDefaultName;
  private String typeName;
  private String oldStatusName;
  private String newStatusName;
  private String ttPriorityName;
  private String alarmGroupName;
  private List<TTCfgBusinessDTO> ttCfgBusinessDTO;
  private List<TTChangeStatusRoleDTO> ttChangeStatusRoleDTO;
  private Long troubleId;
  private String scopeOfUseName;
  private List<Long> lstScopeOfUse;
  private String userLogin;
  private String state;
  private List<String> lstFileName;
  private List<byte[]> lstFileArr;
  private String longitude;//kinh do
  private String latitude;//vi do

  public TTChangeStatusDTO(Long id, Long ttTypeId, String alarmGroup, Long oldStatus,
      Long newStatus, Long sendCreate, String createContent, Long sendReceiveUser,
      String receiveUserContent, Long sendCreateUnit, String createUnitContent,
      Long sendReceiveUnitLv1, String receiveUnitLv1Content, Long sendReceiveUnitLv2,
      String receiveUnitLv2Content, Long isDefault, Long ttPriority,
      String nextAction) {
    this.id = id;
    this.ttTypeId = ttTypeId;
    this.oldStatus = oldStatus;
    this.newStatus = newStatus;
    this.sendCreate = sendCreate;
    this.createContent = createContent;
    this.sendReceiveUser = sendReceiveUser;
    this.receiveUserContent = receiveUserContent;
    this.sendReceiveUnitLv1 = sendReceiveUnitLv1;
    this.receiveUnitLv1Content = receiveUnitLv1Content;
    this.sendReceiveUnitLv2 = sendReceiveUnitLv2;
    this.receiveUnitLv2Content = receiveUnitLv2Content;
    this.isDefault = isDefault;
    this.ttPriority = ttPriority;
    this.nextAction = nextAction;
    this.alarmGroup = alarmGroup;
    this.sendCreateUnit = sendCreateUnit;
    this.createUnitContent = createUnitContent;
  }

  public TTChangeStatusEntity toEntity() {
    try {
      return new TTChangeStatusEntity(id, ttTypeId, alarmGroup, oldStatus, newStatus, sendCreate,
          createContent,
          sendReceiveUser, receiveUserContent, sendCreateUnit, createUnitContent,
          sendReceiveUnitLv1,
          receiveUnitLv1Content, sendReceiveUnitLv2, receiveUnitLv2Content, isDefault,
          ttPriority, nextAction);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }
}
