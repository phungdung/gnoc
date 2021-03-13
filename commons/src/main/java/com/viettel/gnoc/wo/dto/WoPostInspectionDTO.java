package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ITSOL
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class WoPostInspectionDTO {

  //Fields
  private String id;
  private String woId;
  private String account;
  private String point;
  private String note;
  private String checkUserName;
  private String receiveUserName;
  private String result;
  private String createdTime;
  private String woCodePin;
  private String filename;
  private String dataJson;
  private String cdId;
  private String locationName;
  private String accountWoId;

  private List<byte[]> fileDocumentByteArray;
  private List<String> arrFileName;
  private String woTypeName;
  private String woType;
  private String locationCode;
  private String userName;
  private List<ObjKeyValue> lstObjKeyValue;
  private List<GnocFileDto> gnocFileDtos;

  private String defaultSortField;

  public WoPostInspectionInsideDTO toInsideDto() {
    WoPostInspectionInsideDTO model = new WoPostInspectionInsideDTO(
        StringUtils.validString(id) ? Long.valueOf(id) : null,
        StringUtils.validString(woId) ? Long.valueOf(woId) : null,
        account,
        StringUtils.validString(point) ? Long.valueOf(point) : null,
        note,
        checkUserName,
        receiveUserName,
        result,
        StringUtils.validString(createdTime) ? DateTimeUtils.convertStringToDate(createdTime)
            : null,
        woCodePin,
        filename,
        dataJson,
        StringUtils.validString(cdId) ? Long.valueOf(cdId) : null,
        locationName,
        StringUtils.validString(accountWoId) ? Long.valueOf(accountWoId) : null,
        fileDocumentByteArray,
        arrFileName,
        woTypeName,
        woType,
        locationCode,
        userName,
        lstObjKeyValue,
        gnocFileDtos
    );
    return model;
  }
}
