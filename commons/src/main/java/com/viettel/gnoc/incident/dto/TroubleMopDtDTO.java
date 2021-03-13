package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.model.TroubleMopDtEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author itsol
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class TroubleMopDtDTO extends BaseDto {

  //Fields
  private String troubleDtId;
  private String troubleMopId;
  private String dtId;
  private String dtName;
  private String path;
  private String createTime;
  private String state;
  private String stateName;
  private String nodes;
  private String resultDetail;
  private byte[] fileDocumentByteArray;
  private String dtFileType;
  private String troubleId;


  public TroubleMopDtEntity toEntity() {
    TroubleMopDtEntity model = new TroubleMopDtEntity(
        !StringUtils.validString(troubleDtId) ? null : Long.valueOf(troubleDtId),
        !StringUtils.validString(troubleMopId) ? null : Long.valueOf(troubleMopId),
        !StringUtils.validString(dtId) ? null : Long.valueOf(dtId),
        dtName,
        path,
        !StringUtils.validString(createTime) ? null
            : DateTimeUtils.convertStringToDate(createTime),
        !StringUtils.validString(state) ? null : Long.valueOf(state),
        nodes,
        resultDetail,
        dtFileType
    );
    return model;
  }
}
