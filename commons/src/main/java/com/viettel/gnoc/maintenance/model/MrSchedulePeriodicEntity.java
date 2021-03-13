package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.maintenance.dto.MrSchedulePeriodicDTO;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_SCHEDULE_PERIODIC")
public class MrSchedulePeriodicEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_SCHEDULE_PERIODIC_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "MRSP_ID", unique = true, nullable = false)
  private Long mrspId;

  @Column(name = "MR_ID")
  private Long mrId;

  @Column(name = "WO_TEMP_ID")
  private Long woTempId;

  @Column(name = "USER_ID")
  private Long userId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "TIME_INSERT")
  private Date timeInsert;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "TIME_WO_START")
  private Date timeWoStart;

  @Column(name = "POSITION")
  private String position;

  @Column(name = "IS_SEND")
  private String isSend;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "TIME_SEND")
  private Date timeSend;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "TIME_WO_END")
  private Date timeWoEnd;

  @Column(name = "WO_ID")
  private Long woId;

  public MrSchedulePeriodicDTO toDTO() {
    MrSchedulePeriodicDTO dto = new MrSchedulePeriodicDTO(
        mrspId == null ? null : mrspId.toString(), mrId == null ? null : mrId.toString(),
        woTempId == null ? null : woTempId.toString(), userId == null ? null : userId.toString(),
        timeInsert == null ? null
            : DateTimeUtils.convertDateToString(timeInsert, "dd/MM/yyyy HH:mm:ss"),
        timeWoStart == null ? null
            : DateTimeUtils.convertDateToString(timeWoStart, "dd/MM/yyyy HH:mm:ss"), position,
        isSend, timeSend == null ? null
        : DateTimeUtils.convertDateToString(timeSend, "dd/MM/yyyy HH:mm:ss"),
        timeWoEnd == null ? null
            : DateTimeUtils.convertDateToString(timeWoEnd, "dd/MM/yyyy HH:mm:ss")
        , woId == null ? null : woId.toString()
    );
    return dto;
  }

}
