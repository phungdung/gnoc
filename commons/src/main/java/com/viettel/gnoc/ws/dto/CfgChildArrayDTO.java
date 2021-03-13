package com.viettel.gnoc.ws.dto;

import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CfgChildArrayDTO {

  private String childrenId;
  private String parentId;
  private String childrenCode;
  private String childrenName;
  private String status;
  private String updatedUser;
  private String updatedTime;
  private String parentName;

  public com.viettel.gnoc.cr.dto.CfgChildArrayDTO toInsideDTO() {
    return new com.viettel.gnoc.cr.dto.CfgChildArrayDTO(
        StringUtils.isStringNullOrEmpty(childrenId) ? null : Long.parseLong(childrenId),
        StringUtils.isStringNullOrEmpty(parentId) ? null : Long.parseLong(parentId),
        childrenCode,
        childrenName,
        StringUtils.isStringNullOrEmpty(status) ? null : Long.parseLong(status),
        updatedUser,
        updatedTime
    );
  }
}
