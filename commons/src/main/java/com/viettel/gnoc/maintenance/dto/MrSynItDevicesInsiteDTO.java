package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.maintenance.model.MrSynItDevicesEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MrSynItDevicesInsiteDTO extends BaseDto {

  private Long id;
  private String marketCode;
  private String objectCode;
  private Long mrConfirmHard;
  private Date updateDate;
  private Long levelImportant;
  private Long mrSoft;
  private Long isCompleteSoft;
  private Long mrHard;
  private Long mrConfirmSoft;
  private String ud;
  private String regionSoft;
  private String notes;
  private Long status;
  private String arrayCode;
  private String ipNode;
  private Long objectId;
  private String regionHard;
  private Long isComplete1m;
  private String groupCode;
  private String createUserSoft;
  private Long isComplete3m;
  private String createUserHard;
  private Long isComplete12m;
  private String deviceType;
  private String userMrHard;
  private String station;
  private Date synDate;
  private Long upTime;
  private String updateUser;
  private Long cdId;
  private Date lastDate6m;
  private Date lastDate1m;
  private Date lastDateSoft;
  private Date lastDate3m;
  private String objectName;
  private String vendor;
  private Date lastDate12m;
  private Long isComplete6m;
  private String db;
  private String nodeAffected;
  private Long boUnit;
  private Long approveStatus;
  private String approveReason;
  private String statusIIM;
  //dungpv add
  private Long boUnitHard;
  private Long approveStatusHard;
  private String approveReasonHard;
  private Long isRunMop;
  private String marketCodeIIM;

  //end
  public MrSynItDevicesEntity toEntity() {
    return new MrSynItDevicesEntity(id, marketCode, objectCode, mrConfirmHard, updateDate,
        levelImportant, mrSoft,
        isCompleteSoft, mrHard, mrConfirmSoft, ud, regionSoft, notes, status, arrayCode
        , ipNode, objectId, regionHard, isComplete1m, groupCode, createUserSoft,
        isComplete3m, createUserHard, isComplete12m, deviceType, userMrHard, station,
        synDate, upTime, updateUser, cdId, lastDate6m, lastDate1m, lastDateSoft,
        lastDate3m, objectName, vendor, lastDate12m, isComplete6m, db, nodeAffected,
        boUnit, approveStatus, approveReason, statusIIM, boUnitHard, approveStatusHard,
        approveReasonHard, isRunMop, marketCodeIIM);
  }
}
