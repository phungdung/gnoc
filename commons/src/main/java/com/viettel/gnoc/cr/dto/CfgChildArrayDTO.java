package com.viettel.gnoc.cr.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.Unique;
import com.viettel.gnoc.cr.model.CfgChildArrayEntity;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author DungPV
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Unique(message = "{validation.cfgChildArray.null.unique}", clazz = CfgChildArrayEntity.class, uniqueField = "childrenCode", idField = "childrenId")
public class CfgChildArrayDTO extends BaseDto {

  private Long childrenId;
  @NotNull(message = "{validation.cfgChildArray.null.parentId}")
  private Long parentId;
  @NotEmpty(message = "{validation.cfgChildArray.null.childrenCode}")
  @Size(max = 200, message = "{validation.cfgChildArray.childrenCode.tooLong}")
  private String childrenCode;
  @NotEmpty(message = "{validation.cfgChildArray.null.childrenName}")
  @Size(max = 200, message = "{validation.cfgChildArray.childrenName.tooLong}")
  private String childrenName;
  @NotNull(message = "{validation.cfgChildArray.null.status}")
  private Long status;
  private String updatedUser;
  private String updatedTime;
  private String parentCode;
  private String parentName;
  private String createTimeFrom;
  private String createTimeTo;
  private List<LanguageExchangeDTO> listChildrenName;

  public CfgChildArrayDTO(Long childrenId, Long parentId, String childrenName, String childrenCode,
      Long status, String updatedUser, String updatedTime) {
    this.childrenId = childrenId;
    this.parentId = parentId;
    this.childrenCode = childrenCode;
    this.childrenName = childrenName;
    this.status = status;
    this.updatedUser = updatedUser;
    this.updatedTime = updatedTime;
  }

  public CfgChildArrayEntity toEntity() {
    return new CfgChildArrayEntity(childrenId, parentId, childrenCode,
        childrenName, status, updatedUser,
        StringUtils.validString(updatedTime) ? DateTimeUtils.convertStringToDate(updatedTime)
            : null);
  }

  public com.viettel.gnoc.ws.dto.CfgChildArrayDTO toOutside() {
    return new com.viettel.gnoc.ws.dto.CfgChildArrayDTO(
        StringUtils.isLongNullOrEmpty(childrenId) ? null : childrenId.toString(),
        StringUtils.isLongNullOrEmpty(parentId) ? null : parentId.toString(),
        childrenCode,
        childrenName,
        StringUtils.isLongNullOrEmpty(status) ? null : status.toString(),
        updatedUser,
        updatedTime,
        parentName
    );
  }
}
