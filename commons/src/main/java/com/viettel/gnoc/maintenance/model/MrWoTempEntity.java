package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.maintenance.dto.MrWoTempDTO;
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
@Table(schema = "OPEN_PM", name = "MR_WO_TEMP")
public class MrWoTempEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_WO_TEMP_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WO_ID", unique = true, nullable = false)
  private Long woId;
  @Column(name = "WO_WFM_ID")
  private Long woWfmId;

  @Column(name = "PARENT_ID")
  private Long parentId;

  @Column(name = "WO_CODE")
  private String woCode;

  @Column(name = "WO_CONTENT")
  private String woContent;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATE_DATE")
  private Date createDate;

  @Column(name = "WO_SYSTEM")
  private String woSystem;

  @Column(name = "WO_SYSTEM_ID")
  private String woSystemId;

  @Column(name = "WO_TYPE_ID")
  private Long woTypeId;

  @Column(name = "CREATE_PERSON_ID")
  private Long createPersonId;

  @Column(name = "CD_ID")
  private Long cdId;

  @Column(name = "FT_ID")
  private Long ftId;

  @Column(name = "STATUS")

  private Long status;

  @Column(name = "PRIORITY_ID")
  private Long priorityId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "START_TIME")
  private Date startTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "END_TIME")
  private Date endTime;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "FINISH_TIME")
  private Date finishTime;

  @Column(name = "RESULT")
  private Long result;

  @Column(name = "STATION_ID")
  private Long stationId;

  @Column(name = "STATION_CODE")
  private String stationCode;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_TIME")
  private Date lastUpdateTime;

  @Column(name = "FILE_NAME")
  private String fileName;

  @Column(name = "WO_DESCRIPTION")
  private String woDescription;

  public MrWoTempDTO toDTO() {
    MrWoTempDTO dto = new MrWoTempDTO(
        woId == null ? null : woId.toString(), woWfmId == null ? null : woWfmId.toString(),
        parentId == null ? null : parentId.toString(), woCode, woContent, createDate == null ? null
        : DateTimeUtils.convertDateToString(createDate, "dd/MM/yyyy HH:mm:ss"), woSystem,
        woSystemId, woTypeId == null ? null : woTypeId.toString(),
        createPersonId == null ? null : createPersonId.toString(),
        cdId == null ? null : cdId.toString(), ftId == null ? null : ftId.toString(),
        status == null ? null : status.toString(),
        priorityId == null ? null : priorityId.toString(), startTime == null ? null
        : DateTimeUtils.convertDateToString(startTime, "dd/MM/yyyy HH:mm:ss"),
        endTime == null ? null
            : DateTimeUtils.convertDateToString(endTime, "dd/MM/yyyy HH:mm:ss"),
        finishTime == null ? null
            : DateTimeUtils.convertDateToString(finishTime, "dd/MM/yyyy HH:mm:ss"),
        result == null ? null : result.toString(), stationId == null ? null : stationId.toString(),
        stationCode, lastUpdateTime == null ? null
        : DateTimeUtils.convertDateToString(lastUpdateTime, "dd/MM/yyyy HH:mm:ss"), fileName,
        woDescription
    );
    return dto;
  }
}
