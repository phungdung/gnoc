package com.viettel.gnoc.risk.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.risk.dto.RiskChangeStatusDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "RISK_CHANGE_STATUS")
public class RiskChangeStatusEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "RISK_CHANGE_STATUS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "RISK_TYPE_ID")
  private Long riskTypeId;

  @Column(name = "OLD_STATUS")
  private Long oldStatus;

  @Column(name = "NEW_STATUS")
  private Long newStatus;

  @Column(name = "SEND_CREATE")
  private Long sendCreate;

  @Column(name = "CREATE_CONTENT")
  private String createContent;

  @Column(name = "SEND_RECEIVE_USER")
  private Long sendReceiveUser;

  @Column(name = "RECEIVE_USER_CONTENT")
  private String receiveUserContent;

  @Column(name = "IS_DEFAULT")
  private Long isDefault;

  @Column(name = "SEND_RECEIVE_UNIT")
  private Long sendReceiveUnit;

  @Column(name = "RECEIVE_UNIT_CONTENT")
  private String receiveUnitContent;

  @Column(name = "RISK_PRIORITY")
  private Long riskPriority;

  @Column(name = "NEXT_ACTION")
  private String nextAction;

  @Column(name = "SEND_NV_QLRR")
  private Long sendNvQlrr;

  @Column(name = "NV_QLRR_CONTENT")
  private String nvQlrrContent;

  @Column(name = "SEND_RISK_MANAGEMENT_UNIT")
  private Long sendRiskManagementUnit;

  @Column(name = "RISK_MANAGEMENT_UNIT_CONTENT")
  private String riskManagementUnitContent;

  @Column(name = "SEND_NV_QLRRTT")
  private Long sendNvQlrrtt;

  @Column(name = "NV_QLRRTT_CONTENT")
  private String nvQlrrttContent;

  @Column(name = "SEND_RISK_MANAGEMENT")
  private Long sendRiskManagement;

  @Column(name = "RISK_MANAGEMENT_CONTENT")
  private String riskManagementContent;

//  private Long sendRiskManagementStaff;
//  private String riskManagementStaffContent;

  public RiskChangeStatusDTO toDTO() {
    return new RiskChangeStatusDTO(id, riskTypeId, oldStatus, newStatus, sendCreate,
        createContent, sendReceiveUser, receiveUserContent, isDefault, sendReceiveUnit,
        receiveUnitContent, riskPriority, nextAction, sendNvQlrr, nvQlrrContent,
        sendRiskManagementUnit, riskManagementUnitContent, sendNvQlrrtt, nvQlrrttContent,
        sendRiskManagement, riskManagementContent);
  }

}
