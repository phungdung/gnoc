package com.viettel.gnoc.maintenance.model;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.maintenance.dto.MrCdBatteryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * @author Dunglv3
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_CD_BATTERY")
public class MrCdBatteryEntity {
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_CD_BATTERY_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "DC_POWER_ID", unique = true, nullable = false)
  private Long dcPowerId;

  @Column(name = "STATION_ID")
  private Long stationId;

  @Column(name = "STATION_CODE")
  private String stationCode;

  @Column(name = "DC_POWER")
  private String dcPower;

  @Column(name = "PROVINCE")
  private String province;

  @Column(name = "STAFF_NAME")
  private String staffName;

  @Column(name = "STAFF_EMAIL")
  private String staffEmail;

  @Column(name = "STAFF_PHONE")
  private String staffPhone;

  @Column(name = "DISCHARGE_TYPE")
  private String dischargeType;

  @Column(name = "TIME_DISCHARGE")
  private String timeDischarge;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "RECENT_DISCHARGE_CD")
  private Date recentDischargeCd;

  @Column(name = "MR_CODE")
  private String mrCode;

  @Column(name = "WO_CODE")
  private String woCode;

  @Column(name = "STATUS")
  private String status = "1";

  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "UPDATED_TIME")
  private Date updatedTime;

  @Column(name = "UPDATED_USER")
  private String updatedUser;

  @Column(name = "RECENT_DISCHAGE_NOC")
  private Date recentDischageNoc;

  @Column(name = "RECENT_DISCHAGE_GNOC")
  private Date recentDischageGnoc;

  @Column(name = "PRODUCTION_TECHNOLOGY")
  private String productionTechnology;

  @Column(name = "DISTRICT_CODE")
  private String districtCode;

  @Column(name = "DISCHARGE_CONFIRM")
  private String dischargeConfirm;

  @Column(name = "RESULT_DISCHARGE")
  private Long resultDischarge = 0L;

  @Column(name = "DISCHARGE_NUMBER")
  private Long dischargeNumber = 0L;

  @Column(name = "DISCHARGE_REASON_FAIL")
  private String dischargeReasonFail;
  //Dunglv add

  @Column(name = "RECENT_DISCHARGE_GNOC")
  private String recentDischargeGnoc;

  @Column(name = "RECENT_DISCHARGE_NOC")
  private String recentDischargeNoc;

  @Column(name = "REASON_ACCEPT")
  private String reasonAccept;

  @Column(name = "STATUS_ACCEPT")
  private String statusAccept;

  @Column(name = "AREA_CODE")
  private String areaCode;

  @Column(name = "PROVINCE_CODE")
  private String provinceCode;

  @Column(name = "ISWOACCU")
  private String iswoAccu;

  @Column(name = "MARKET_CODE")
  private String marketCode;

  @Column(name = "DISTRICT_NAME")
  private String districtName;


  public MrCdBatteryEntity(Long dcPowerId) {
    this.dcPowerId = dcPowerId;
  }

//  public MrCdBatteryEntity(Long dc_power_id, Long station_id, String station_code, String dc_power, String province, String staff_name, String staff_email, String staff_phone, String discharge_type, String time_discharge, Date recent_discharge_cd, String mr_code, String wo_code, String status, Date created_time, Date updated_time, String updated_user, Date recent_dischage_noc, Date recent_dischage_gnoc, String production_technology, String district_code, String discharge_confirm, Long result_discharge, Long discharge_number, String discharge_reason_fail) {
//    this.dc_power_id = dc_power_id;
//    this.station_id = station_id;
//    this.station_code = station_code;
//    this.dc_power = dc_power;
//    this.province = province;
//    this.staff_name = staff_name;
//    this.staff_email = staff_email;
//    this.staff_phone = staff_phone;
//    this.discharge_type = discharge_type;
//    this.time_discharge = time_discharge;
//    this.recent_discharge_cd = recent_discharge_cd;
//    this.mr_code = mr_code;
//    this.wo_code = wo_code;
//    this.status = status;
//    this.created_time = created_time;
//    this.updated_time = updated_time;
//    this.updated_user = updated_user;
//    this.recent_dischage_noc = recent_dischage_noc;
//    this.recent_dischage_gnoc = recent_dischage_gnoc;
//    this.production_technology = production_technology;
//    this.district_code = district_code;
//    this.discharge_confirm = discharge_confirm;
//    this.result_discharge = result_discharge;
//    this.discharge_number = discharge_number;
//    this.discharge_reason_fail = discharge_reason_fail;
//  }

  public MrCdBatteryDTO toDTO() {
    MrCdBatteryDTO mrCdBatteryDTO = new MrCdBatteryDTO(
        dcPowerId == null ? null : dcPowerId.toString()
        , stationId == null ? null : stationId.toString()
        , stationCode
        , dcPower
        , province
        , staffName
        , staffEmail
        , staffPhone
        , dischargeType
        , timeDischarge
        , recentDischargeCd == null ? null : DateTimeUtils.convertDateTimeStampToString(recentDischargeCd)
        , mrCode
        , woCode
        , status
        , createdTime == null ? null : DateTimeUtils.convertDateToString(createdTime)
        , updatedTime == null ? null : DateTimeUtils.convertDateToString(updatedTime)
        , updatedUser
        , recentDischageNoc == null ? null : DateTimeUtils.convertDateTimeStampToString(recentDischageNoc)
        , recentDischageGnoc == null ? null : DateTimeUtils.convertDateTimeStampToString(recentDischageGnoc)
        , productionTechnology
        , districtCode
        , dischargeConfirm
        , resultDischarge == null ? null : resultDischarge.toString()
        , dischargeNumber == null ? null : dischargeNumber.toString()
        , dischargeReasonFail
        , recentDischargeGnoc
        , recentDischargeNoc
        , reasonAccept
        , statusAccept
        , areaCode
        , provinceCode
        , iswoAccu
        , marketCode
        , districtName
    );
    return mrCdBatteryDTO;
  }
}
