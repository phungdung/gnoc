package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_DEVICE")
public class MrDeviceEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.MR_DEVICE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "DEVICE_ID", length = 22)
  private Long deviceId;
  @Column(name = "NODE_CODE", length = 800)
  private String nodeCode;
  @Column(name = "NODE_IP", length = 800)
  private String nodeIp;
  @Column(name = "CD_ID", length = 22)
  private Long cdId;
  @Column(name = "CD_ID_HARD", length = 22)
  private Long cdIdHard;
  @Column(name = "CREATE_USER_SOFT", length = 200)
  private String createUserSoft;
  @Column(name = "CREATE_MR", length = 4)
  private String createMr;
  @Column(name = "IMPACT_NODE", length = 200)
  private String impactNode;
  @Column(name = "NUMBER_OF_CR", length = 22)
  private Long numberOfCr;
  @Column(name = "MR_HARD", length = 1)
  private String mrHard;
  @Column(name = "MR_12M", length = 1)
  private String mr12M;
  @Column(name = "COMMENTS", length = 4000)
  private String comments;
  @Column(name = "MR_SOFT", length = 1)
  private String mrSoft;
  @Column(name = "LAST_DATE")
  private Date lastDate;
  @Column(name = "ARRAY_CODE", length = 800)
  private String arrayCode;
  @Column(name = "DEVICE_TYPE", length = 800)
  private String deviceType;
  @Column(name = "GROUP_CODE", length = 4000)
  private String groupCode;
  @Column(name = "UPDATE_DATE")
  private Date updateDate;
  @Column(name = "UPDATE_USER", length = 800)
  private String updateUser;
  @Column(name = "STATION_CODE", length = 800)
  private String stationCode;
  @Column(name = "MARKET_CODE", length = 100)
  private String marketCode;
  @Column(name = "REGION_SOFT", length = 200)
  private String regionSoft;
  @Column(name = "IS_COMPLETE_1M", length = 22)
  private Long isComplete1m;
  @Column(name = "IS_COMPLETE_3M", length = 22)
  private Long isComplete3m;
  @Column(name = "IS_COMPLETE_6M", length = 22)
  private Long isComplete6m;
  @Column(name = "IS_COMPLETE_12M", length = 22)
  private Long isComplete12m;
  @Column(name = "IS_COMPLETE_SOFT", length = 22)
  private Long isCompleteSoft;
  @Column(name = "LAST_DATE_1M", length = 7)
  private Date lastDate1m;
  @Column(name = "LAST_DATE_3M", length = 7)
  private Date lastDate3m;
  @Column(name = "LAST_DATE_6M", length = 7)
  private Date lastDate6m;
  @Column(name = "LAST_DATE_12M", length = 7)
  private Date lastDate12m;
  @Column(name = "DEVICE_NAME", length = 1000)
  private String deviceName;
  @Column(name = "GROUP_ID", length = 22)
  private String groupId;
  @Column(name = "VENDOR", length = 2000)
  private String vendor;
  @Column(name = "NETWORK_TYPE", length = 50)
  private String networkType;
  @Column(name = "NETWORK_CLASS", length = 50)
  private String networkClass;
  @Column(name = "MR_CONFIRM_HARD", length = 1)
  private String mrConfirmHard;
  @Column(name = "MR_CONFIRM_SOFT", length = 1)
  private String mrConfirmSoft;
  @Column(name = "STATUS", length = 1)
  private String status;
  @Column(name = "DATE_INTEGRATED")
  private Date dateIntegrated;
  @Column(name = "CREATE_USER_HARD", length = 200)
  private String createUserHard;
  @Column(name = "REGION_HARD", length = 200)
  private String regionHard;
  @Column(name = "USER_MR_HARD", length = 200)
  private String userMrhard;

  //add new
  @Column(name = "BO_UNIT_SOFT")
  private Long boUnitSoft;
  @Column(name = "APPROVE_STATUS_SOFT")
  private Long approveStatusSoft;
  @Column(name = "APPROVE_REASON_SOFT")
  private String approveReasonSoft;
  @Column(name = "BO_UNIT_HARD")
  private Long boUnitHard;
  @Column(name = "APPROVE_STATUS_HARD")
  private Long approveStatusHard;
  @Column(name = "APPROVE_REASON_HARD")
  private String approveReasonHard;

  public MrDeviceDTO toDTO() {
    return new MrDeviceDTO(deviceId, nodeCode, nodeIp, cdId, cdIdHard, createUserSoft, createMr,
        impactNode, numberOfCr, mrHard, mr12M, comments, mrSoft, lastDate, arrayCode, deviceType,
        groupCode, updateDate, updateUser, stationCode, marketCode, regionSoft, isComplete1m,
        isComplete3m, isComplete6m, isComplete12m, isCompleteSoft, lastDate1m, lastDate3m,
        lastDate6m, lastDate12m, deviceName, groupId, vendor, networkType, networkClass,
        mrConfirmHard, mrConfirmSoft, status, dateIntegrated, createUserHard, regionHard,
        userMrhard, boUnitSoft, approveStatusSoft, approveReasonSoft, boUnitHard, approveStatusHard,
        approveReasonHard);
  }
}
