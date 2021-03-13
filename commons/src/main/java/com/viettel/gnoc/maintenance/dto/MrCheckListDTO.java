package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.model.MrCheckListEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TrungDuong
 */
@Getter
@Setter
@NoArgsConstructor
@Slf4j
//@MultiFieldUnique(message = "{validation.MrCheckListDTO.null.unique}", clazz = MrCheckListEntity.class, uniqueFields = "marketCode,arrayCode,networkType,deviceType,cycle", idField = "checkListId")
public class MrCheckListDTO extends BaseDto {

  private Long checkListId;
  private String marketCode;
  private String arrayCode;
  private String deviceType;
  private String deviceTypeAll;
  private String content;
  private String createdUser;
  private String createdTime;
  private String updatedUser;
  private String updatedTime;
  private String cycle;
  private String networkType;
  private String checklistType;
  private String target;

  private String marketName;
  private String resultImport;
  private String arrayCodeSTR;

  public MrCheckListDTO(Long checkListId, String marketCode, String arrayCode, String deviceType,
      String deviceTypeAll, String content, String createdUser, String createdTime,
      String updatedUser,
      String updatedTime, String cycle, String networkType, String checklistType, String target) {
    this.checkListId = checkListId;
    this.marketCode = marketCode;
    this.arrayCode = arrayCode;
    this.deviceType = deviceType;
    this.deviceTypeAll = deviceTypeAll;
    this.content = content;
    this.createdUser = createdUser;
    this.createdTime = createdTime;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
    this.cycle = cycle;
    this.networkType = networkType;
    this.checklistType = checklistType;
    this.target = target;
  }

  public MrCheckListEntity toEntity() {
    MrCheckListEntity model = new MrCheckListEntity(
        checkListId
        , marketCode
        , arrayCode
        , deviceType
        , deviceTypeAll
        , content
        , createdUser,
        !StringUtils.validString(createdTime) ? null
            : DateTimeUtils.convertStringToDate(createdTime)
        , updatedUser,
        !StringUtils.validString(updatedTime) ? null
            : DateTimeUtils.convertStringToDate(updatedTime),
        !StringUtils.validString(cycle) ? null : Long.valueOf(cycle)
        , networkType
        , checklistType
        , target
    );
    return model;
  }
}
