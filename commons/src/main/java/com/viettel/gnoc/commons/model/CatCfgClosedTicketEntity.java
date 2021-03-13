package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.CatCfgClosedTicketDTO;
import com.viettel.gnoc.commons.utils.Constants;
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
@Getter
@Setter
@NoArgsConstructor
@Table(schema = "COMMON_GNOC", name = "CAT_CFG_CLOSED_TICKET")
public class CatCfgClosedTicketEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CAT_CFG_CLOSED_TICKET_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "WO_TYPE_ID", nullable = false)
  private Long woTypeId;

  @Column(name = "TYPE_ID", nullable = false)
  private Long typeId;

  @Column(name = "ALARM_GROUP_ID", nullable = false)
  private Long alarmGroupId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_TIME")
  private Date lastUpdateTime;

  public CatCfgClosedTicketEntity(Long id, Long woTypeId, Long typeId, Long alarmGroupId,
      Date lastUpdateTime) {
    this.id = id;
    this.woTypeId = woTypeId;
    this.typeId = typeId;
    this.alarmGroupId = alarmGroupId;
    this.lastUpdateTime = lastUpdateTime;
  }

  public CatCfgClosedTicketDTO toDTO() {
    CatCfgClosedTicketDTO dto = new CatCfgClosedTicketDTO(
        id == null ? null : id.toString(),
        woTypeId == null ? null : woTypeId.toString(),
        typeId == null ? null : typeId.toString(),
        alarmGroupId == null ? null : alarmGroupId.toString(),
        lastUpdateTime == null ? null
            : DateTimeUtils.convertDateToString(lastUpdateTime, Constants.ddMMyyyy)
    );
    return dto;
  }
}
