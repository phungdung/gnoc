package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.wo.model.CfgFtOnTimeEntity;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@MultiFieldUnique(message = "{validation.cfgFtOnTime.isExist}", clazz = CfgFtOnTimeEntity.class,
    uniqueFields = "cdId,cfgTime,userId,businessId", idField = "id")
public class CfgFtOnTimeDTO extends BaseDto {

  private Long id;
  @NotNull(message = "{validation.cfgFtOnTime.cdId.isRequired}")
  private String cdId;
  @NotNull(message = "{validation.cfgFtOnTime.cfgTime.isRequired}")
  private Date cfgTime;
  @NotNull(message = "{validation.cfgFtOnTime.userId.isRequired}")
  private String userId;
  @NotNull(message = "{validation.cfgFtOnTime.businessId.isRequired}")
  private String businessId;
  private String cdGroupName;
  private String userName;
  private String businessName;
  private String defaultSortField;
  private String isEnable;
  private String fromDate;
  private String toDate;
  private String cdGroupCode;
  private String cfgTimeStr;
  private String resultImport;

  public CfgFtOnTimeDTO(Long id, String cdId, Date cfgTime, String userId, String businessId) {
    this.id = id;
    this.cdId = cdId;
    this.cfgTime = cfgTime;
    this.userId = userId;
    this.businessId = businessId;
  }

  public CfgFtOnTimeEntity toEntity() {
    try {
      CfgFtOnTimeEntity model = new CfgFtOnTimeEntity(
          id,
          StringUtils.validString(cdId) ? Long.valueOf(cdId) : null,
          cfgTime,
          StringUtils.validString(userId) ? Long.valueOf(userId) : null,
          StringUtils.validString(businessId) ? Long.valueOf(businessId) : null
      );
      return model;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

}
