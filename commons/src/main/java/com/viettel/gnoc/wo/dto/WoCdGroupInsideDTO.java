package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.SizeByte;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.wo.model.WoCdGroupEntity;
import java.util.List;
import javax.validation.constraints.NotNull;
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
@Unique(message = "{validation.woCdGroupInsideDTO.multiple.unique}", clazz = WoCdGroupEntity.class,
    uniqueField = "woGroupCode", idField = "woGroupId")
public class WoCdGroupInsideDTO extends BaseDto {

  private Long woGroupId;
  @NotNull(message = "validation.woCdGroupDTO.woGroupCode.NotNull")
  @SizeByte(max = 50, message = "validation.woCdGroupDTO.woGroupCode.tooLong")
  private String woGroupCode;
  @NotNull(message = "validation.woCdGroupDTO.woGroupName.NotNull")
  @Size(max = 100, message = "validation.woCdGroupDTO.woGroupName.tooLong")
  private String woGroupName;
  @SizeByte(max = 100, message = "validation.woCdGroupDTO.email.tooLong")
  private String email;
  @SizeByte(max = 100, message = "validation.woCdGroupDTO.mobile.tooLong")
  private String mobile;
  @NotNull(message = "validation.woCdGroupDTO.groupTypeId.NotNull")
  private Long groupTypeId;
  private Long isEnable;
  @NotNull(message = "validation.woCdGroupDTO.nationId.NotNull")
  private Long nationId;

  private String groupTypeName;
  private String nationName;
  private String isEnableName;
  private Long action;
  private String actionName;
  private Long woTypeId;
  private List<Long> listWoGroupId;

  private String resultImport;

  public WoCdGroupInsideDTO(Long woGroupId, String woGroupCode, String woGroupName, String email,
      String mobile, Long groupTypeId, Long isEnable, Long nationId) {
    this.woGroupId = woGroupId;
    this.woGroupCode = woGroupCode;
    this.woGroupName = woGroupName;
    this.email = email;
    this.mobile = mobile;
    this.groupTypeId = groupTypeId;
    this.isEnable = isEnable;
    this.nationId = nationId;
  }

  public WoCdGroupInsideDTO(Long woGroupId, String woGroupCode, String woGroupName, String email,
      String mobile, Long groupTypeId, Long isEnable) {
    this.woGroupId = woGroupId;
    this.woGroupCode = woGroupCode;
    this.woGroupName = woGroupName;
    this.email = email;
    this.mobile = mobile;
    this.groupTypeId = groupTypeId;
    this.isEnable = isEnable;
  }

  public WoCdGroupEntity toEntity() {
    return new WoCdGroupEntity(woGroupId, woGroupCode, woGroupName, email, mobile, groupTypeId,
        isEnable, nationId);
  }

  public WoCdGroupDTO toDtoOutSide() {
    WoCdGroupDTO model = new WoCdGroupDTO(
        StringUtils.validString(woGroupId) ? String.valueOf(woGroupId) : null,
        woGroupCode,
        woGroupName,
        email,
        mobile,
        StringUtils.validString(groupTypeId) ? String.valueOf(groupTypeId) : null,
        StringUtils.validString(isEnable) ? String.valueOf(isEnable) : null,
        "woGroupName"
    );
    return model;
  }
}
