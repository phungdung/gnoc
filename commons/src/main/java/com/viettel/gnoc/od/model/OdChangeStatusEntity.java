package com.viettel.gnoc.od.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.od.dto.OdChangeStatusDTO;
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

/**
 * @author TienNV
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "OD_CHANGE_STATUS")
public class OdChangeStatusEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OD_CHANGE_STATUS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true)
  private Long id;

  @Column(name = "OD_TYPE_ID")
  private Long odTypeId;

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

  @Column(name = "SEND_RECEIVE_UNIT")
  private Long sendReceiveUnit;

  @Column(name = "RECEIVE_UNIT_CONTENT")
  private String receiveUnitContent;

  @Column(name = "IS_DEFAULT")
  private Long isDefault;

  @Column(name = "OD_PRIORITY")
  private Long odPriority;

  @Column(name = "NEXT_ACTION")
  private String nextAction;

  @Column(name = "SEND_APPROVER")
  private Long sendApprover;

  @Column(name = "APPROVER_CONTENT")
  private String approverContent;

  public OdChangeStatusDTO toDTO() {
    return new OdChangeStatusDTO(id, odTypeId, oldStatus, newStatus,
        sendCreate, createContent, sendReceiveUser, receiveUserContent,
        sendReceiveUnit, receiveUnitContent, isDefault, odPriority, nextAction, sendApprover, approverContent);
  }

}
