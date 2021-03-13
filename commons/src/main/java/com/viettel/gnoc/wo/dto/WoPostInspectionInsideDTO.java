package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.model.WoPostInspectionEntity;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ITSOL
 */
@Slf4j
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WoPostInspectionInsideDTO extends BaseDto {

  private Long id;
  private Long woId;
  private String account;
  private Long point;
  private String note;
  private String checkUserName;
  private String receiveUserName;
  private String result;
  private Date createdTime;
  private String woCodePin;
  private String filename;
  private String dataJson;
  private Long cdId;
  private String locationName;
  private Long accountWoId;

  private List<byte[]> fileDocumentByteArray;
  private List<String> arrFileName;
  private String woTypeName;
  private String woType;
  private String locationCode;
  private String userName;
  private List<ObjKeyValue> lstObjKeyValue;
  private List<GnocFileDto> gnocFileDtos;

  public WoPostInspectionInsideDTO(Long id, Long woId, String account, Long point, String note,
      String checkUserName, String receiveUserName, String result, Date createdTime,
      String woCodePin, String filename, String dataJson, Long cdId, String locationName,
      Long accountWoId) {
    this.id = id;
    this.woId = woId;
    this.account = account;
    this.point = point;
    this.note = note;
    this.checkUserName = checkUserName;
    this.receiveUserName = receiveUserName;
    this.result = result;
    this.createdTime = createdTime;
    this.woCodePin = woCodePin;
    this.filename = filename;
    this.dataJson = dataJson;
    this.cdId = cdId;
    this.locationName = locationName;
    this.accountWoId = accountWoId;
  }

  public WoPostInspectionEntity toEntity() {
    return new WoPostInspectionEntity(id, woId, account, point, note, checkUserName,
        receiveUserName, result, createdTime, woCodePin, filename, dataJson, cdId, locationName,
        accountWoId);
  }

  public WoPostInspectionDTO toOutsideDto() {
    WoPostInspectionDTO model = new WoPostInspectionDTO(
        StringUtils.validString(id) ? String.valueOf(id) : null,
        StringUtils.validString(woId) ? String.valueOf(woId) : null,
        account,
        StringUtils.validString(point) ? String.valueOf(point) : null,
        note,
        checkUserName,
        receiveUserName,
        result,
        StringUtils.validString(createdTime) ? DateTimeUtils.date2ddMMyyyyHHMMss(createdTime)
            : null,
        woCodePin,
        filename,
        dataJson,
        StringUtils.validString(cdId) ? String.valueOf(cdId) : null,
        locationName,
        StringUtils.validString(accountWoId) ? String.valueOf(accountWoId) : null,
        fileDocumentByteArray,
        arrFileName,
        woTypeName,
        woType,
        locationCode,
        userName,
        lstObjKeyValue,
        gnocFileDtos,
        "name"
    );
    return model;
  }
}
