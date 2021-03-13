package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.maintenance.dto.MrCheckListDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author TrungDuong
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MR_CHECKLIST")
public class MrCheckListEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_CHECKLIST_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CHECKLIST_ID")
  private Long checkListId;

  @Column(name = "MARKET_CODE")
  private String marketCode;

  @Column(name = "ARRAY_CODE")
  private String arrayCode;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "DEVICE_TYPE_ALL")
  private String deviceTypeAll;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "CREATED_USER")
  private String createdUser;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "UPDATED_USER")
  private String updatedUser;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATED_TIME")
  private Date updatedTime;

  @Column(name = "CYCLE")
  private Long cycle;

  @Column(name = "NETWORK_TYPE")
  private String networkType;

  @Column(name = "CHECKLIST_TYPE")
  private String checklistType;

  @Column(name = "TARGET")
  private String target;

  public MrCheckListDTO toDTO() {
    MrCheckListDTO dto = new MrCheckListDTO(
        checkListId
        , marketCode
        , arrayCode
        , deviceType
        , deviceTypeAll
        , content
        , createdUser
        , createdTime == null ? null
        : DateTimeUtils.convertDateToString(createdTime, DateTimeUtils.patternDateTimeMs)
        , updatedUser
        , updatedTime == null ? null
        : DateTimeUtils.convertDateToString(updatedTime, DateTimeUtils.patternDateTimeMs)
        , cycle == null ? null : cycle.toString()
        , networkType
        , checklistType
        , target
    );
    return dto;
  }
}
