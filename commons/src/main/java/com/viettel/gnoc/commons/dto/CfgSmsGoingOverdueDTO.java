package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CfgSmsGoingOverdueEntity;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CfgSmsGoingOverdueDTO extends BaseDto {

  private String cfgId;
  private String cfgName;
  private String unitId;
  private String unitName;
  private String locationId;
  private String locationName;
  private String priorityId;
  private String priorityName;
  private String levelId;
  private String levelName;
  private String timeProcess;
  private ArrayList<CfgSmsUserGoingOverdueDTO> list;
  private Long levelMaxId;
  private Long levelMinId;
  private Long countElementWithMaxLevelID;
  private Long countElementWithMinLevelID;
  private String userId;

  public CfgSmsGoingOverdueDTO(String cfgId, String cfgName, String unitId, String unitName,
      String locationId, String locationName, String priorityId, String priorityName,
      String levelId, String timeProcess) {
    this.cfgId = cfgId;
    this.cfgName = cfgName;
    this.unitId = unitId;
    this.unitName = unitName;
    this.locationId = locationId;
    this.locationName = locationName;
    this.priorityId = priorityId;
    this.priorityName = priorityName;
    this.levelId = levelId;
    this.timeProcess = timeProcess;
  }

  public CfgSmsGoingOverdueEntity toEntity() {
    CfgSmsGoingOverdueEntity model = new CfgSmsGoingOverdueEntity(
        !StringUtils.validString(cfgId) ? null : Long.valueOf(cfgId),
        cfgName,
        !StringUtils.validString(unitId) ? null : Long.valueOf(unitId),
        unitName,
        !StringUtils.validString(locationId) ? null : Long.valueOf(locationId),
        locationName,
        !StringUtils.validString(priorityId) ? null : Long.valueOf(priorityId),
        priorityName,
        !StringUtils.validString(levelId) ? null : Long.valueOf(levelId),
        !StringUtils.validString(timeProcess) ? null : Double.valueOf(timeProcess)
    );
    return model;
  }

}
