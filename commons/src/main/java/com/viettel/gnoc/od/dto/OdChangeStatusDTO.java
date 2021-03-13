package com.viettel.gnoc.od.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.od.model.OdChangeStatusEntity;
import java.util.List;
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
public class OdChangeStatusDTO extends BaseDto {

  //Fields
  private Long id;
  private Long odTypeId;
  @NotNull(message = "{validation.odCfgBusiness.oldStatus.isRequired}")
  private Long oldStatus;
  @NotNull(message = "{validation.odCfgBusiness.newStatus.isRequired}")
  private Long newStatus;
  private Long sendCreate;
  @Size(max = 500, message = "{validation.odCfgBusiness.createContent.tooLong}")
  private String createContent;
  private Long sendReceiveUser;
  @Size(max = 500, message = "{validation.odCfgBusiness.receiveUserContent.tooLong}")
  private String receiveUserContent;
  private Long sendReceiveUnit;
  @Size(max = 750, message = "{validation.odCfgBusiness.receiveUnitContent.tooLong}")
  private String receiveUnitContent;
  private Long isDefault;
  private String isDefaultName;
  private String odTypeName;
  private String oldStatusName;
  private String newStatusName;
  @NotNull(message = "{validation.odCfgBusiness.odPriority.isRequired}")
  private Long odPriority;
  private String odPriorityName;
  @Size(max = 2000, message = "{validation.odCfgBusiness.nextAction.tooLong}")
  private String nextAction;
  private List<OdCfgBusinessDTO> odCfgBusinessDTO;
  private List<OdChangeStatusRoleDTO> odChangeStatusRoleDTO;


  private Long sendApprover;
  @Size(max = 500, message = "{validation.odCfgBusiness.approverContent.tooLong}")
  private String approverContent;

  public OdChangeStatusDTO(Long odTypeId, Long oldStatus, Long newStatus, Long odPriority) {
    this.odTypeId = odTypeId;
    this.oldStatus = oldStatus;
    this.newStatus = newStatus;
    this.odPriority = odPriority;
  }

  public OdChangeStatusDTO(Long id, Long odTypeId, Long oldStatus, Long newStatus,
      Long sendCreate, String createContent, Long sendReceiveUser, String receiveUserContent,
      Long sendReceiveUnit, String receiveUnitContent, Long isDefault, Long odPriority,
      String nextAction, Long sendApprover, String approverContent) {
    this.id = id;
    this.odTypeId = odTypeId;
    this.oldStatus = oldStatus;
    this.newStatus = newStatus;
    this.sendCreate = sendCreate;
    this.createContent = createContent;
    this.sendReceiveUser = sendReceiveUser;
    this.receiveUserContent = receiveUserContent;
    this.isDefault = isDefault;
    this.sendReceiveUnit = sendReceiveUnit;
    this.receiveUnitContent = receiveUnitContent;
    this.odPriority = odPriority;
    this.nextAction = nextAction;
    this.sendApprover = sendApprover;
    this.approverContent = approverContent;
  }

  public OdChangeStatusEntity toEntity() {
    return new OdChangeStatusEntity(id, odTypeId, oldStatus, newStatus, sendCreate, createContent,
        sendReceiveUser, receiveUserContent,
        sendReceiveUnit, receiveUnitContent, isDefault, odPriority, nextAction, sendApprover, approverContent);
  }
}
