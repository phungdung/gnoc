/**
 * @(#)OdHistoryBO.java 8/26/2015 10:42 , Copyright 2011 Viettel Telecom. All rights reserved
 * VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.od.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.od.dto.OdHistoryDTO;
import java.util.Date;
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
 * @author ThanhPT18
 * @version 1.0
 * @since 8/26/2015 10:42
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OD_HISTORY")
public class OdHistoryEntity {

  //Fields
  @Id
  @Column(name = "OD_HIS_ID")
  @SequenceGenerator(name = "generator", sequenceName = "OD_HISTORY_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  private Long odHisId;

  @Column(name = "OD_ID")
  private Long odId;

  @Column(name = "UPDATE_TIME")
  private Date updateTime;

  @Column(name = "OLD_STATUS")
  private Long oldStatus;

  @Column(name = "NEW_STATUS")
  private Long newStatus;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "UNIT_ID")
  private Long unitId;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "IS_SEND_MESSAGE")
  private Long isSendMesssage;

  @Column(name = "USER_NAME")
  private String userName;

  public OdHistoryEntity(Long odHisId) {
    this.odHisId = odHisId;
  }

  public OdHistoryDTO toDTO() {
    return new OdHistoryDTO(this.odHisId, this.odId, this.updateTime, this.oldStatus,
        this.newStatus, this.userId,
        this.unitId, this.content, this.isSendMesssage, this.userName);
  }
}
