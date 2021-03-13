package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.validator.SizeByte;
import com.viettel.gnoc.incident.model.TroublesIbmEntity;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TroublesIbmDTO extends BaseDto {

  private Long id;
  @SizeByte(max = 1000, message = "validation.troublesIbmDTO.ibmName.tooLong")
  private String ibmName;
  @SizeByte(max = 500, message = "validation.troublesIbmDTO.receiveUnitCode.tooLong")
  private String receiveUnitCode;
  @SizeByte(max = 500, message = "validation.troublesIbmDTO.receiveUnitName.tooLong")
  private String receiveUnitName;
  @SizeByte(max = 500, message = "validation.troublesIbmDTO.productName.tooLong")
  private String productName;
  @SizeByte(max = 500, message = "validation.troublesIbmDTO.productCode.tooLong")
  private String productCode;
  @SizeByte(max = 200, message = "validation.troublesIbmDTO.receiveUserName.tooLong")
  private String receiveUserName;
  private Long createUserId;
  @SizeByte(max = 200, message = "validation.troublesIbmDTO.createUserName.tooLong")
  private String createUserName;
  @SizeByte(max = 1000, message = "validation.troublesIbmDTO.ipServer.tooLong")
  private String ipServer;
  @SizeByte(max = 400, message = "validation.troublesIbmDTO.accountError.tooLong")
  private String accountError;
  @SizeByte(max = 200, message = "validation.troublesIbmDTO.errorCode.tooLong")
  private String errorCode;
  private Date errorTime;
  private Long numComplaint;
  private String description;
  private String process;
  @SizeByte(max = 100, message = "validation.troublesIbmDTO.status.tooLong")
  private String status;
  @SizeByte(max = 50, message = "validation.troublesIbmDTO.timeExpectClose.tooLong")
  private String timeExpectClose;
  @SizeByte(max = 50, message = "validation.troublesIbmDTO.timeClose.tooLong")
  private String timeClose;
  @SizeByte(max = 200, message = "validation.troublesIbmDTO.unitLiable.tooLong")
  private String unitLiable;
  @SizeByte(max = 200, message = "validation.troublesIbmDTO.userLiable.tooLong")
  private String userLiable;
  private String reason;
  private String solution;
  private Long troubleId;
  private Date createdTime;
  private Long isMoveIbm;
  private Long ibmId;
  private Date insertIbmTime;
  private List<GnocFileDto> gnocFileDtos;


  public TroublesIbmEntity toEntity() {
    return new TroublesIbmEntity(id, ibmName, receiveUnitCode, receiveUnitName, productName,
        productCode, receiveUserName, createUserId, createUserName, ipServer, accountError,
        errorCode, errorTime, numComplaint, description, process, status, timeExpectClose,
        timeClose, unitLiable, userLiable, reason, solution, troubleId, createdTime,
        isMoveIbm, ibmId, insertIbmTime);
  }
}
