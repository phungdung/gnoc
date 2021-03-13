package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.CfgTimeExtendTtDTO;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ITSOL
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "COMMON_GNOC", name = "CFG_TIME_EXTEND_TT")
public class CfgTimeExtendTtEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "cfg_time_extend_tt_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID")
  private Long id;

  @Column(name = "TYPE_ID")
  private Long typeId;

  @Column(name = "ALARM_GROUP_ID")
  private Long alarmGroupId;

  @Column(name = "REASON_ID")
  private Long reasonId;

  @Column(name = "PRIORITY_ID")
  private Long priorityId;

  @Column(name = "TIME_EXTEND")
  private Double timeExtend;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_TIME")
  private Date lastUpdateTime;

  @Column(name = "COUNTRY")
  private String country;

  public CfgTimeExtendTtEntity(
      Long id, Long typeId, Long alarmGroupId, Long reasonId, Long priorityId, Double timeExtend,
      Date lastUpdateTime, String country) {
    this.id = id;
    this.typeId = typeId;
    this.alarmGroupId = alarmGroupId;
    this.reasonId = reasonId;
    this.priorityId = priorityId;
    this.timeExtend = timeExtend;
    this.lastUpdateTime = lastUpdateTime;
    this.country = country;
  }

  public CfgTimeExtendTtDTO toDTO() {
    CfgTimeExtendTtDTO dto = new CfgTimeExtendTtDTO(
        id == null ? null : id.toString(),
        typeId == null ? null : typeId.toString(),
        alarmGroupId == null ? null : alarmGroupId.toString(),
        reasonId == null ? null : reasonId.toString(),
        priorityId == null ? null : priorityId.toString(),
        timeExtend == null ? null : timeExtend.toString(),
        lastUpdateTime == null ? null : DateTimeUtils
            .convertDateToString(lastUpdateTime, "dd/MM/yyyy HH:mm:ss"),
        country == null ? null : country.toString()
    );
    return dto;
  }
}
