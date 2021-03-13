package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.wo.dto.WorkTimeDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.Setter;

/**
 * @author thanhlv12
 * @version 1.0
 * @since 03/06/2016 10:53:08
 */
@Entity
@Table(schema = "WFM", name = "WORK_TIME")
@Getter
@Setter
public class WorkTimeEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WORK_TIME_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "WO_ID")
  private Long woId;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "CONTENT")
  private String content;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "INSERT_TIME")
  private Date insertTime;

  @Column(name = "LONGTITUDE")
  private String longtitude;

  @Column(name = "LATITUDE")
  private String latitude;

  @Column(name = "IS_ACTIVE")
  private Long isActive;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATE_TIME")
  private Date updateTime;

  public WorkTimeEntity(Long id) {
    this.id = id;
  }

  public WorkTimeEntity(Long id, Long woId, Long userId,
      String content, Date insertTime, String longtitude,
      String latitude, Long isActive, Date updateTime
  ) {
    this.id = id;
    this.woId = woId;
    this.userId = userId;
    this.content = content;
    this.insertTime = insertTime;
    this.longtitude = longtitude;
    this.latitude = latitude;
    this.isActive = isActive;
    this.updateTime = updateTime;

  }

  public WorkTimeDTO toDTO() {
    WorkTimeDTO dto = new WorkTimeDTO(
        id == null ? null : id.toString(),
        woId == null ? null : woId.toString(),
        userId == null ? null : userId.toString(),
        content,
        insertTime == null ? null
            : DateTimeUtils.date2ddMMyyyyHHMMss(insertTime),
        longtitude,
        latitude,
        isActive == null ? null : isActive.toString(),
        updateTime == null ? null
            : DateTimeUtils.date2ddMMyyyyHHMMss(insertTime)
    );
    return dto;
  }
}
