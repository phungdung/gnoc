package com.viettel.gnoc.maintenance.model;


import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MR_SCHEDULE_IT_HIS")
public class MrScheduleITHisEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_SCHEDULE_HIS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "MR_DEVICE_HIS_ID", unique = true, nullable = false)
  private Long mrDeviceHisId;

  @Column(name = "MARKET_CODE")
  private String marketCode;

  @Column(name = "ARRAY_CODE")
  private String arrayCode;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "DEVICE_ID")
  private String deviceId;

  @Column(name = "DEVICE_CODE")
  private String deviceCode;

  @Column(name = "DEVICE_NAME")
  private String deviceName;

  @Column(name = "MR_DATE")
  private Date mrDate;

  @Column(name = "MR_CONTENT")
  private String mrContent;

  @Column(name = "MR_MODE")
  private String mrMode;

  @Column(name = "MR_TYPE")
  private String mrType;

  @Column(name = "MR_ID")
  private Long mrId;

  @Column(name = "MR_CODE")
  private String mrCode;

  @Column(name = "CR_ID")
  private Long crId;

  @Column(name = "CR_NUMBER")
  private String crNumber;

  @Column(name = "IMPORTANT_LEVEL")
  private String importantLevel;

  @Column(name = "PROCEDURE_ID")
  private String procedureId;

  @Column(name = "PROCEDURE_NAME")
  private String procedureName;

  @Column(name = "NETWORK_TYPE")
  private String networkType;

  @Column(name = "NOTE")
  private String note;

  @Column(name = "CYCLE")
  private Long cycle;

  @Column(name = "TITLE")
  private String title;


  @Column(name = "REGION")
  private String region;


  public MrScheduleITHisDTO toDTO() {
    MrScheduleITHisDTO dto = new MrScheduleITHisDTO(
        mrDeviceHisId
        , marketCode
        , arrayCode
        , deviceType
        , deviceId
        , deviceCode
        , deviceName
        , mrDate == null ? null : mrDate.toString()
        , mrContent
        , mrMode
        , mrType
        , mrId == null ? null : mrId.toString()
        , mrCode
        , crId == null ? null : crId.toString()
        , crNumber
        , importantLevel
        , procedureId
        , procedureName
        , networkType
        , note
        , cycle == null ? null : cycle.toString()
        , title
        , region

    );
    return dto;
  }
}
