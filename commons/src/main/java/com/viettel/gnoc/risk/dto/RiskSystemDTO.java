package com.viettel.gnoc.risk.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import com.viettel.gnoc.commons.validator.SizeByte;
import com.viettel.gnoc.risk.model.RiskSystemEntity;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.riskSystemDTO.multiple.unique}", clazz = RiskSystemEntity.class,
    uniqueFields = "systemCode", idField = "id")
public class RiskSystemDTO extends BaseDto {

  private Long id;
  @SizeByte(max = 200, message = "validation.riskSystemDTO.systemCode.tooLong")
  private String systemCode;
  @SizeByte(max = 200, message = "validation.riskSystemDTO.systemName.tooLong")
  private String systemName;
  private Long schedule;
  private Long systemPriority;
  private Date lastUpdateTime;
  private Long countryId;

  private String countryName;
  private Double offSetFromUser;
  private Long manageUnitId;
  private Long manageUserId;
  private String systemPriorityName;
  private List<RiskSystemDetailDTO> listRiskSystemDetail;
  private String fileName;
  private List<GnocFileDto> gnocFileDtos;
  private String userUnit;
  private String lastUpdateTimeStr;
  private String manageUserName;
  private Long action;
  private String actionName;
  private String resultImport;
  private String scheduleStr;
  private List<String> lstFileName;
  private List<Long> idDeleteList;

  public RiskSystemDTO(Long id, String systemCode, String systemName, Long schedule,
      Long systemPriority, Date lastUpdateTime, Long countryId) {
    this.id = id;
    this.systemCode = systemCode;
    this.systemName = systemName;
    this.schedule = schedule;
    this.systemPriority = systemPriority;
    this.lastUpdateTime = lastUpdateTime;
    this.countryId = countryId;
  }

  public RiskSystemEntity toEntity() {
    return new RiskSystemEntity(id, systemCode, systemName, schedule, systemPriority,
        lastUpdateTime, countryId);
  }

  public RiskSystemTempDTO tempDTO() {
    try {
      RiskSystemTempDTO tempDTO = new RiskSystemTempDTO(
          StringUtils.validString(id) ? String.valueOf(id) : null,
          systemCode,
          systemName,
          StringUtils.validString(schedule) ? String.valueOf(schedule) : null,
          StringUtils.validString(systemPriority) ? String.valueOf(systemPriority) : null,
          StringUtils.validString(lastUpdateTime) ? DateTimeUtils
              .date2ddMMyyyyHHMMss(lastUpdateTime) : null,
          StringUtils.validString(countryId) ? String.valueOf(countryId) : null,
          lstFileName
      );
      return tempDTO;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

}
