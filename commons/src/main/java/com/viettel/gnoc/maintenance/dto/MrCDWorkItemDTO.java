package com.viettel.gnoc.maintenance.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.maintenance.model.MrCDWorkItemEntity;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author trungduong
 */
@Getter
@Setter
@NoArgsConstructor
@Slf4j
@MultiFieldUnique(message = "{validation.MrCDWorkItemDTO.null.unique}", clazz = MrCDWorkItemEntity.class, uniqueFields = "marketCode,arrayCode", idField = "wiId")
public class MrCDWorkItemDTO extends BaseDto {

  private Long wiId;
  private String marketCode;
  private String marketName;
  private String arrayCode;
  private String deviceType;
  private Long cycle;
  private String mrMode;
  private String mrModeName;
  private String workItems;
  private Date createdDate;
  private String createdUser;
  private Date updatedDate;
  private String updatedUser;
  private String resultImport;
  private String policy;
  private String content;
  private String goal;

  public MrCDWorkItemDTO(Long wiId, String marketCode, String arrayCode,
      String deviceType, Long cycle, String mrMode, String workItems, Date createdDate,
      String createdUser, Date updatedDate, String updatedUser) {
    this.wiId = wiId;
    this.marketCode = marketCode;
    this.arrayCode = arrayCode;
    this.deviceType = deviceType;
    this.cycle = cycle;
    this.workItems = workItems;
    this.createdDate = createdDate;
    this.createdUser = createdUser;
    this.updatedDate = updatedDate;
    this.updatedUser = updatedUser;
  }

  public MrCDWorkItemEntity toEntity() {
    MrCDWorkItemEntity model = new MrCDWorkItemEntity(
        wiId
        , marketCode
        , arrayCode
        , deviceType
        , cycle
        , mrMode
        , workItems
        , createdDate
        , createdUser
        , updatedDate
        , updatedUser
    );
    return model;
  }

}
