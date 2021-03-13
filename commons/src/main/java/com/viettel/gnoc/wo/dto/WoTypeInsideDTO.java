package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.wfm.dto.WoTypeDTO;
import com.viettel.gnoc.wo.model.WoTypeEntity;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
@MultiFieldUnique(message = "{validation.woType.null.unique}", clazz = WoTypeEntity.class, uniqueFields = "woTypeCode", idField = "woTypeId")
public class WoTypeInsideDTO extends BaseDto {

  private Long woTypeId;

  @Size(max = 50, message = "{validation.woType.woTypeCode.tooLong}")
  @NotEmpty(message = "{validation.woType.null.woTypeCode}")
  @Pattern(regexp = "^([a-zA-Z0-9_]{1,50})?$", message = "{validation.woType.woTypeCode.pattern}")
  private String woTypeCode;

  @Size(max = 10000, message = "{validation.woType.woTypeName.tooLong}")
  @NotEmpty(message = "{validation.woType.null.woTypeName}")
  private String woTypeName;

  private Long isEnable;

  private Long woGroupType;

  @NotNull(message = "{validation.woType.null.enableCreate}")
  private Long enableCreate;

  @Max(value = 10000, message = "{validation.woType.timeOver.tooLong}")
  private Long timeOver;

  @Max(value = 10000, message = "{validation.woType.smsCycle.tooLong}")
  @Min(value = 0)
  private Long smsCycle;

  @Size(max = 5, message = "{validation.woType.woCloseAutomaticTime.tooLong}")
  @Pattern(regexp = "^([0-9]{1,4})?$", message = "{validation.woType.woCloseAutomaticTimeFoundTimeInvalidNumber}")
  private String woCloseAutomaticTime;

  private String userTypeCode;

  @NotNull(message = "{validation.woType.null.allowPending}")
  private Long allowPending;

  @NotNull(message = "{validation.woType.null.createFromOtherSys}")
  private Long createFromOtherSys;

  @Max(value = 10000, message = "{validation.woType.timeAutoCloseWhenOver.tooLong}")
  @Min(value = 0)
  private Long timeAutoCloseWhenOver;

  @Max(value = 9999999, message = "{validation.woType.processTime.tooLong}")
  @Min(value = 0)
  private Long processTime;

  //hieunv1 bổ xung dto
  private String isEnableName;
  private String woGroupTypeName;
  private String woGroupTypeCode;
  private String enableCreateStr;
  private String allowPendingStr;
  private String createFromOtherSysStr;
  private String resultImport;
  private String action;
  private String validate;
  private String locale;
  private String woTypeGroupId;
  private String woGroupId;
  List<WoTypeCfgRequiredDTO> woTypeCfgRequiredDTOList;
  List<WoTypeCheckListDTO> woTypeCheckListDTOList;
  List<GnocFileDto> gnocFileCreateWoDtos;
  List<GnocFileDto> gnocFilesGuideDtos;
  List<WoPriorityDTO> woPriorityDTOList;
  List<Long> checkListIdDeleteList;

  private List<String> lstCdGroup;
  private Long cdGroupId;
  private Boolean isOtherSys;
  private List<Long> idDeleteListFilesGuide;
  private List<Long> idDeleteListFileCreateWo;
  //Constructor

  public WoTypeInsideDTO(Long woTypeId, String woTypeCode, String woTypeName, Long isEnable,
      Long woGroupType, Long cdGroupId, Long enableCreate, Long timeOver, Long smsCycle,
      String woCloseAutomaticTime, String userTypeCode, Long allowPending,
      Long createFromOtherSys, Long timeAutoCloseWhenOver, Long processTime, Boolean isOtherSys,
      List<String> lstCdGroup, String woTypeGroupId, String woGroupId, String isEnableName) {
    this.woTypeId = woTypeId;
    this.woTypeCode = woTypeCode;
    this.woTypeName = woTypeName;
    this.isEnable = isEnable;
    this.woGroupType = woGroupType;
    this.cdGroupId = cdGroupId;
    this.enableCreate = enableCreate;
    this.timeOver = timeOver;
    this.smsCycle = smsCycle;
    this.woCloseAutomaticTime = woCloseAutomaticTime;
    this.userTypeCode = userTypeCode;
    this.allowPending = allowPending;
    this.createFromOtherSys = createFromOtherSys;
    this.timeAutoCloseWhenOver = timeAutoCloseWhenOver;
    this.processTime = processTime;
    this.isOtherSys = isOtherSys;
    this.lstCdGroup = lstCdGroup;
    this.woTypeGroupId = woTypeGroupId;
    this.woGroupId = woGroupId;
    this.isEnableName = isEnableName;
  }

  public WoTypeDTO toWoTypeDTO() {
    WoTypeDTO model = new WoTypeDTO(
        StringUtils.validString(woTypeId) ? String.valueOf(woTypeId) : null,
        woTypeCode,
        woTypeName,
        StringUtils.validString(isEnable) ? String.valueOf(isEnable) : null,
        StringUtils.validString(woGroupType) ? String.valueOf(woGroupType) : null,
        StringUtils.validString(enableCreate) ? String.valueOf(enableCreate) : null,
        StringUtils.validString(timeOver) ? String.valueOf(timeOver) : null,
        StringUtils.validString(smsCycle) ? String.valueOf(smsCycle) : null,
        woCloseAutomaticTime,
        userTypeCode,
        StringUtils.validString(allowPending) ? String.valueOf(allowPending) : null,
        StringUtils.validString(createFromOtherSys) ? String.valueOf(createFromOtherSys) : null,
        StringUtils.validString(timeAutoCloseWhenOver) ? String.valueOf(timeAutoCloseWhenOver)
            : null,
        StringUtils.validString(processTime) ? String.valueOf(processTime) : null,
        StringUtils.validString(isOtherSys) ? String.valueOf(isOtherSys) : null,
        lstCdGroup,
        woTypeGroupId,
        woGroupId,
        isEnableName
    );
    return model;
  }

  public WoTypeEntity toEntity() {
    return new WoTypeEntity(
        woTypeId
        , woTypeCode
        , woTypeName
        , isEnable
        , woGroupType
        , enableCreate
        , timeOver
        , smsCycle
        , woCloseAutomaticTime
        , userTypeCode
        , allowPending
        , createFromOtherSys
        , timeAutoCloseWhenOver
        , processTime);
  }

}
