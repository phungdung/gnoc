package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.TTChangeStatusDTO;
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
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Entity
@Table(schema = "ONE_TM", name = "TT_CHANGE_STATUS")
public class TTChangeStatusEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TT_CHANGE_STATUS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true)
  private Long id;

  @Column(name = "TT_TYPE_ID")
  private Long ttTypeId;

  @Column(name = "ALARM_GROUP")
  private String alarmGroup;

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

  @Column(name = "SEND_CREATE_UNIT")
  private Long sendCreateUnit;

  @Column(name = "CREATE_UNIT_CONTENT")
  private String createUnitContent;

  @Column(name = "SEND_RECEIVE_UNIT_LV1")
  private Long sendReceiveUnitLv1;

  @Column(name = "RECEIVE_UNIT_LV1_CONTENT")
  private String receiveUnitLv1Content;

  @Column(name = "SEND_RECEIVE_UNIT_LV2")
  private Long sendReceiveUnitLv2;

  @Column(name = "RECEIVE_UNIT_LV2_CONTENT")
  private String receiveUnitLv2Content;

  @Column(name = "IS_DEFAULT")
  private Long isDefault;

  @Column(name = "TT_PRIORITY")
  private Long ttPriority;

  @Column(name = "NEXT_ACTION")
  private String nextAction;

  public TTChangeStatusDTO toDTO() {
    try {
      return new TTChangeStatusDTO(id, ttTypeId, alarmGroup, oldStatus, newStatus, sendCreate,
          createContent, sendReceiveUser, receiveUserContent, sendCreateUnit, createUnitContent,
          sendReceiveUnitLv1, receiveUnitLv1Content, sendReceiveUnitLv2, receiveUnitLv2Content,
          isDefault, ttPriority, nextAction);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }
}
