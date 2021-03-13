package com.viettel.gnoc.risk.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.validator.MultiFieldUniqueHasNull;
import com.viettel.gnoc.risk.model.RiskChangeStatusEntity;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUniqueHasNull(message = "{validation.riskChangeStatusDTO.multiple.unique}", clazz = RiskChangeStatusEntity.class,
    uniqueFields = "riskTypeId,riskPriority,oldStatus,newStatus", idField = "id")
public class RiskChangeStatusDTO extends BaseDto {

  private Long id;
  private Long riskTypeId;
  @NotNull(message = "validation.riskChangeStatusDTO.oldStatus.notNull")
  private Long oldStatus;
  @NotNull(message = "validation.riskChangeStatusDTO.newStatus.notNull")
  private Long newStatus;
  private Long sendCreate;
  private String createContent;
  private Long sendReceiveUser;
  private String receiveUserContent;
  private Long isDefault;
  private Long sendReceiveUnit;
  private String receiveUnitContent;
  @NotNull(message = "validation.riskChangeStatusDTO.riskPriority.notNull")
  private Long riskPriority;
  private String nextAction;
  private Long sendNvQlrr;
  private String nvQlrrContent;
  private Long sendRiskManagementUnit;
  private String riskManagementUnitContent;
  private Long sendNvQlrrtt;
  private String nvQlrrttContent;
  private Long sendRiskManagement;
  private String riskManagementContent;

  private String riskTypeName;
  private String oldStatusName;
  private String newStatusName;
  private String isDefaultName;
  private String riskPriorityName;
  private Boolean isSearch;
  private List<RiskCfgBusinessDTO> lstCfgBusiness;
  private List<RiskChangeStatusRoleDTO> lstRole;
  private String changeStatusRole;
  private List<GnocFileDto> gnocFileDtos;
  private List<Long> idDeleteList;

  public RiskChangeStatusDTO(Long riskTypeId, Long oldStatus, Long newStatus,
      Long riskPriority) {
    this.riskTypeId = riskTypeId;
    this.oldStatus = oldStatus;
    this.newStatus = newStatus;
    this.riskPriority = riskPriority;
  }

  public RiskChangeStatusDTO(Long id, Long riskTypeId, Long oldStatus, Long newStatus,
      Long sendCreate, String createContent, Long sendReceiveUser,
      String receiveUserContent, Long isDefault, Long sendReceiveUnit,
      String receiveUnitContent, Long riskPriority, String nextAction, Long sendNvQlrr,
      String nvQlrrContent, Long sendRiskManagementUnit, String riskManagementUnitContent,
      Long sendNvQlrrtt,
      String nvQlrrttContent, Long sendRiskManagement, String riskManagementContent) {
    this.id = id;
    this.riskTypeId = riskTypeId;
    this.oldStatus = oldStatus;
    this.newStatus = newStatus;
    this.sendCreate = sendCreate;
    this.createContent = createContent;
    this.sendReceiveUser = sendReceiveUser;
    this.receiveUserContent = receiveUserContent;
    this.isDefault = isDefault;
    this.sendReceiveUnit = sendReceiveUnit;
    this.receiveUnitContent = receiveUnitContent;
    this.riskPriority = riskPriority;
    this.nextAction = nextAction;
    this.sendNvQlrr = sendNvQlrr;
    this.nvQlrrContent = nvQlrrContent;
    this.sendRiskManagementUnit = sendRiskManagementUnit;
    this.riskManagementUnitContent = riskManagementUnitContent;
    this.sendNvQlrrtt = sendNvQlrrtt;
    this.nvQlrrttContent = nvQlrrttContent;
    this.sendRiskManagement = sendRiskManagement;
    this.riskManagementContent = riskManagementContent;
  }

  public RiskChangeStatusEntity toEntity() {
    return new RiskChangeStatusEntity(id, riskTypeId, oldStatus, newStatus, sendCreate,
        createContent, sendReceiveUser, receiveUserContent, isDefault, sendReceiveUnit,
        receiveUnitContent, riskPriority, nextAction, sendNvQlrr, nvQlrrContent,
        sendRiskManagementUnit, riskManagementUnitContent, sendNvQlrrtt, nvQlrrttContent,
        sendRiskManagement, riskManagementContent);
  }
}
