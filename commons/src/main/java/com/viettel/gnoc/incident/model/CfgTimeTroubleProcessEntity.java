package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "ONE_TM", name = "CFG_TIME_TROUBLE_PROCESS")
public class CfgTimeTroubleProcessEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CFG_TIME_TROUBLE_PROCESS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "TYPE_ID", nullable = false)
  private Long typeId;

  @Column(name = "SUB_CATEGORY_ID", nullable = false)
  private Long subCategoryId;

  @Column(name = "PRIORITY_ID", nullable = false)
  private Long priorityId;

  @Column(name = "CREATE_TT_TIME", nullable = false)
  private Double createTtTime;

  @Column(name = "PROCESS_TT_TIME", nullable = false)
  private Double processTtTime;

  @Column(name = "CLOSE_TT_TIME")
  private Double closeTtTime;

  @Column(name = "PROCESS_WO_TIME")
  private Double processWoTime;

  @Column(name = "IS_CALL")
  private Long isCall;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_TIME")
  private Date lastUpdateTime;

  @Column(name = "COUNTRY")
  private String country;

  @Column(name = "TIME_ABOUT_OVERDUE")
  private Double timeAboutOverdue;

  @Column(name = "IS_STATION_VIP")
  private Long isStationVip;

  @Column(name = "TIME_STATION_VIP")
  private Double timeStationVip;

  @Column(name = "TIME_WO_VIP")
  private Double timeWoVip;

  @Column(name = "CD_AUDIO_NAME")
  private String cdAudioName;

  @Column(name = "FT_AUDIO_NAME")
  private String ftAudioName;

  public CfgTimeTroubleProcessDTO toDTO() {
    return new CfgTimeTroubleProcessDTO(id, typeId, subCategoryId, priorityId, createTtTime,
        processTtTime, closeTtTime, processWoTime, isCall, lastUpdateTime, country,
        timeAboutOverdue, isStationVip, timeStationVip, timeWoVip, cdAudioName, ftAudioName);
  }
}
