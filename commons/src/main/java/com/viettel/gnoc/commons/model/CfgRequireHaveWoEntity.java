package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.CfgRequireHaveWoDTO;
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


@Entity
@Table(schema = "COMMON_GNOC", name = "CFG_REQUIRE_HAVE_WO")
@NoArgsConstructor
@Setter
@Getter
public class CfgRequireHaveWoEntity {

  //Fields
  @Column(name = "ID", nullable = false)
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CFG_REQUIRE_HAVE_WO_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  private Long id;

  @Column(name = "TYPE_ID")
  private Long typeId;

  @Column(name = "ALARM_GROUP_ID")
  private Long alarmGroupId;

  @Column(name = "REASON_ID")
  private Long reasonId;

  @Column(name = "WO_TYPE_ID")
  private String woTypeId;

  @Column(name = "LAST_UPDATE_TIME")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date lastUpdateTime;

  public CfgRequireHaveWoEntity(
      Long id, Long typeId, Long alarmGroupId, Long reasonId, String woTypeId,
      Date lastUpdateTime) {
    this.id = id;
    this.typeId = typeId;
    this.alarmGroupId = alarmGroupId;
    this.reasonId = reasonId;
    this.woTypeId = woTypeId;
    this.lastUpdateTime = lastUpdateTime;
  }

  public CfgRequireHaveWoDTO toDTO() {
    CfgRequireHaveWoDTO dto = new CfgRequireHaveWoDTO(
        id, typeId, alarmGroupId, reasonId, woTypeId,
        lastUpdateTime == null ? null
            : DateTimeUtils.convertDateToString(lastUpdateTime, "dd/MM/yyyy HH:mm:ss")
    );
    return dto;
  }


}
