package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.WoCdTempEntity;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
//@MultiFieldUnique(message = "{validation.materialThres.null.unique}", clazz = MaterialThresEntity.class,
//    uniqueFields = "materialId,actionId,infraType,serviceId", idField = "materialThresId")
public class WoCdTempInsideDTO extends BaseDto implements Cloneable {

  private Long woCdTempId;
  private Long userId;
  private Long woGroupId;
  private Date startTime;
  private Date endTime;
  private Long isCd;
  private Long status;

  //String
  private String userIdStr;
  private String username;
  private String woGroupIdStr;
  private String woGroupCode;
  private String woGroupName;
  private String statusName;
  private String isCdStr;
  private String resultImport;
  private Date startDayFrom;
  private Date startDayTo;
  private Date endDayFrom;
  private Date endDayTo;

  private String startTimeStr;
  private String endTimeStr;

  //Constructor
  public WoCdTempInsideDTO(Long woCdTempId, Long userId, Long woGroupId, Date startTime,
      Date endTime, Long isCd, Long status) {
    this.woCdTempId = woCdTempId;
    this.userId = userId;
    this.woGroupId = woGroupId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.isCd = isCd;
    this.status = status;
  }

  public WoCdTempEntity toEntity() {
    WoCdTempEntity model = new WoCdTempEntity(
        woCdTempId
        , userId
        , woGroupId
        , startTime
        , endTime
        , isCd
        , status
    );
    return model;
  }

  @Override
  public WoCdTempInsideDTO clone() {
    try {
      return (WoCdTempInsideDTO) super.clone();
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }
  }
}
