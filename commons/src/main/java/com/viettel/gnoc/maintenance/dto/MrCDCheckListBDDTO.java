package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.maintenance.model.MrCDCheckListBDEntity;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kienpv
 */
@Getter
@Setter
@NoArgsConstructor
@Slf4j
//@MultiFieldUnique(message = "{validation.MrCDWorkItemDTO.null.unique}", clazz = MrCDCheckListBDEntity.class, uniqueFields = "marketCode,arrayCode,deviceType,cycle,content", idField = "checkListId")
public class MrCDCheckListBDDTO extends BaseDto {

  private Long checkListId;
  private String marketCode;
  private String marketName;
  private String arrayCode;
  private String deviceType;
  private String cycle;
  private Date createdDate;
  private String createdUser;
  private Date updatedDate;
  private String updatedUser;
  private String resultImport;
  private String purPose;
  private String content;
  private String goal;
  private String action;
  private String arrayName;

  public MrCDCheckListBDDTO(Long checkListId, String marketCode, String arrayCode,
      String deviceType, String cycle, Date createdDate,
      String createdUser, Date updatedDate, String updatedUser, String purPose, String content,
      String goal) {
    this.checkListId = checkListId;
    this.marketCode = marketCode;
    this.arrayCode = arrayCode;
    this.deviceType = deviceType;
    this.cycle = cycle;
    this.createdDate = createdDate;
    this.createdUser = createdUser;
    this.updatedDate = updatedDate;
    this.updatedUser = updatedUser;
    this.purPose = purPose;
    this.content = content;
    this.goal = goal;
  }

  public MrCDCheckListBDEntity toEntity() {
    MrCDCheckListBDEntity entity = new MrCDCheckListBDEntity(
        checkListId
        , marketCode
        , arrayCode
        , deviceType
        , Long.parseLong(cycle)
        , createdDate
        , createdUser
        , updatedDate
        , updatedUser
        , purPose
        , content
        , goal
    );
    return entity;
  }

}
