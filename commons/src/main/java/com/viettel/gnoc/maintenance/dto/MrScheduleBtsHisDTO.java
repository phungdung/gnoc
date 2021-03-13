package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.maintenance.model.MrScheduleBtsHisEntity;
import java.util.Date;
import java.util.List;
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
public class MrScheduleBtsHisDTO extends BaseDto {

  //Fields
  private Long mrDeviceHisId;
  private String marketCode;
  private String areaCode;
  private String provinceCode;
  private String deviceType;
  private String deviceId;
  private String deviceCode;
  private String serial;
  private String cycle;
  private Date completeDate;
  private String woCode;
  private String userManager;
  private Date mrDate;
  private String stationCode;
  private Long isComplete;
  private String status;

  private String mrDateFrom;
  private String mrDateTo;
  private String completeDateFrom;
  private String completeDateTo;
  private String content;
  private String createUser;
  private String checkListId;
  private String photoReq;
  private String minPhoto;
  private String maxPhoto;
  private String captureGuide;
  private String taskStatus;
  private String materialName;
  private String quantity;
  private String woId;
  private String statusStr;

  private String marketCodeStr;
  private String areaCodeStr;
  private String provinceCodeStr;
  private String deviceTypeStr;

  private String statusWoGoc;
  private String statusWoGL;
  private String wogiaoLai;
  private String statusName;

  private String createDateGL;
  private String createDateGoc;

  private String statusNameGL;
  private String checkListFail;

  private String woLstTag;

  private List<MrScheduleBtsHisDetailInsiteDTO> mrScheduleBtsHisDetailDTOList;

  //TrungDuong them
  private String taskApprove;
  private String taskApproveArea;

  private String approveUser;
  private String approveUserArea;

  private String approveDate;
  private String approveDateArea;

  private String reason;
  private String reasonArea;

  private Long isCompleteArea;

  //Constructor
  public MrScheduleBtsHisDTO(Long mrDeviceHisId, String marketCode, String areaCode,
      String provinceCode, String deviceType, String deviceId, String deviceCode,
      String serial, String cycle, Date completeDate, String woCode, String userManager,
      Date mrDate, String stationCode, Long isComplete, String status) {
    this.mrDeviceHisId = mrDeviceHisId;
    this.marketCode = marketCode;
    this.areaCode = areaCode;
    this.provinceCode = provinceCode;
    this.deviceType = deviceType;
    this.deviceId = deviceId;
    this.deviceCode = deviceCode;
    this.serial = serial;
    this.cycle = cycle;
    this.completeDate = completeDate;
    this.woCode = woCode;
    this.userManager = userManager;
    this.mrDate = mrDate;
    this.stationCode = stationCode;
    this.isComplete = isComplete;
    this.status = status;
  }

  public MrScheduleBtsHisEntity toEntity() {
    MrScheduleBtsHisEntity model = new MrScheduleBtsHisEntity(
        mrDeviceHisId,
        marketCode,
        areaCode,
        provinceCode,
        deviceType,
        deviceId,
        deviceCode,
        serial,
        cycle,
        completeDate,
        woCode,
        userManager,
        mrDate,
        stationCode,
        isComplete,
        status
    );
    return model;
  }
}
