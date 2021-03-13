package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
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
@Table(schema = "OPEN_PM", name = "MR_SYN_IT_DEVICES")
public class MrSynItDevicesEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.MR_SYN_IT_DEVICES_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  private Long id;
  @Column(name = "MARKET_CODE", precision = 22)
  private String marketCode;
  @Column(name = "OBJECT_CODE", length = 2000)
  private String objectCode;
  @Column(name = "MR_CONFIRM_HARD", precision = 22)
  private Long mrConfirmHard;
  @Column(name = "UPDATE_DATE", length = 11)
  private Date updateDate;
  @Column(name = "LEVEL_IMPORTANT", precision = 22)
  private Long levelImportant;
  @Column(name = "MR_SOFT", precision = 22)
  private Long mrSoft;
  @Column(name = "IS_COMPLETE_SOFT", precision = 22)
  private Long isCompleteSoft;
  @Column(name = "MR_HARD", precision = 22)
  private Long mrHard;
  @Column(name = "MR_CONFIRM_SOFT", precision = 22)
  private Long mrConfirmSoft;
  @Column(name = "UD", length = 2000)
  private String ud;
  @Column(name = "REGION_SOFT", length = 800)
  private String regionSoft;
  @Column(name = "NOTES", length = 4000)
  private String notes;
  @Column(name = "STATUS", precision = 22)
  private Long status;
  @Column(name = "ARRAY_CODE", length = 800)
  private String arrayCode;
  @Column(name = "IP_NODE", length = 800)
  private String ipNode;
  @Column(name = "OBJECT_ID", precision = 22)
  private Long objectId;
  @Column(name = "REGION_HARD", length = 800)
  private String regionHard;
  @Column(name = "IS_COMPLETE_1M", precision = 22)
  private Long isComplete1m;
  @Column(name = "GROUP_CODE", length = 4000)
  private String groupCode;
  @Column(name = "CREATE_USER_SOFT", length = 800)
  private String createUserSoft;
  @Column(name = "IS_COMPLETE_3M", precision = 22)
  private Long isComplete3m;
  @Column(name = "CREATE_USER_HARD", length = 800)
  private String createUserHard;
  @Column(name = "IS_COMPLETE_12M", precision = 22)
  private Long isComplete12m;
  @Column(name = "DEVICE_TYPE", length = 50)
  private String deviceType;
  @Column(name = "USER_MR_HARD", length = 800)
  private String userMrHard;
  @Column(name = "STATION", length = 2000)
  private String station;
  @Column(name = "SYN_DATE", length = 11)
  private Date synDate;
  @Column(name = "UP_TIME", length = 11)
  private Long upTime;
  @Column(name = "UPDATE_USER", length = 800)
  private String updateUser;
  @Column(name = "CD_ID", precision = 22)
  private Long cdId;
  @Column(name = "LAST_DATE_6M", length = 11)
  private Date lastDate6m;
  @Column(name = "LAST_DATE_1M", length = 11)
  private Date lastDate1m;
  @Column(name = "LAST_DATE_SOFT", length = 11)
  private Date lastDateSoft;
  @Column(name = "LAST_DATE_3M", length = 11)
  private Date lastDate3m;
  @Column(name = "OBJECT_NAME", length = 2000)
  private String objectName;
  @Column(name = "VENDOR", length = 800)
  private String vendor;
  @Column(name = "LAST_DATE_12M", length = 11)
  private Date lastDate12m;
  @Column(name = "IS_COMPLETE_6M", precision = 22)
  private Long isComplete6m;
  @Column(name = "DB", length = 2000)
  private String db;
  @Column(name = "NODE_AFFECTED", length = 4000)
  private String nodeAffected;
  @Column(name = "BO_UNIT")
  private Long boUnit;
  @Column(name = "APPROVE_STATUS")
  private Long approveStatus;
  @Column(name = "APPROVE_REASON", length = 500)
  private String approveReason;
  @Column(name = "STATUS_IIM", length = 100)
  private String statusIIM;
  @Column(name = "BO_UNIT_HARD")
  private Long boUnitHard;
  @Column(name = "APPROVE_STATUS_HARD")
  private Long approveStatusHard;
  @Column(name = "APPROVE_REASON_HARD", length = 500)
  private String approveReasonHard;
  @Column(name = "IS_RUN_MOP")
  private Long isRunMop;
  @Column(name = "MARKET_CODE_IIM", length = 100)
  private String marketCodeIIM;

  public MrSynItDevicesDTO toDTO() {
    return new MrSynItDevicesDTO(id == null ? null : id.toString(), marketCode, objectCode,
        mrConfirmHard == null ? null : mrConfirmHard.toString(), updateDate,
        levelImportant == null ? null : levelImportant.toString(),
        mrSoft == null ? null : mrSoft.toString(),
        isCompleteSoft == null ? null : isCompleteSoft.toString(),
        mrHard == null ? null : mrHard.toString(),
        mrConfirmSoft == null ? null : mrConfirmSoft.toString(), ud, regionSoft, notes,
        status == null ? null : status.toString(), arrayCode, ipNode,
        objectId == null ? null : objectId.toString(), regionHard,
        isComplete1m == null ? null : isComplete1m.toString(), groupCode, createUserSoft,
        isComplete3m == null ? null : isComplete3m.toString(), createUserHard,
        isComplete12m == null ? null : isComplete12m.toString(), deviceType, userMrHard, station,
        synDate, upTime, updateUser, cdId == null ? null : cdId.toString(), lastDate6m, lastDate1m,
        lastDateSoft, lastDate3m, objectName, vendor, lastDate12m,
        isComplete6m == null ? null : isComplete6m.toString(), db, nodeAffected,
        boUnit == null ? null : boUnit.toString(),
        approveStatus == null ? null : approveStatus.toString(), approveReason, statusIIM,
        boUnitHard == null ? null : boUnitHard.toString(),
        approveStatusHard == null ? null : approveStatusHard.toString(), approveReasonHard,
        isRunMop, marketCodeIIM
    );
  }

}
