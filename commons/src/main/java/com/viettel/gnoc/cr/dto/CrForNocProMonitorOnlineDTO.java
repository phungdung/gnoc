package com.viettel.gnoc.cr.dto;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CrForNocProMonitorOnlineDTO implements Comparable<CrForNocProMonitorOnlineDTO> {

  private String crNumber;
  private String crName;
  private String crId;
  private String earliestStartTime;
  private String latestStartTime;
  private String appliedSystem;
  private String affectedServiceId;
  private String affectedServiceCode;
  private String affectedServiceName;
  private String affectedServiceDescription;
  private String changeResponsibleId;
  private String changeResponsibleUserName;
  private String changeResponsibleFullName;
  private String changeResponsibleMobile;
  private String changeResponsibleUnitId;
  private String changeResponsibleUnitCode;
  private String changeResponsibleUnitName;
  private String impactSegmentId;
  private String impactSegmentCode;
  private String impactSegmentName;
  private String disturbanceStartTime;
  private String disturbanceEndTime;
  private String deviceId;
  private String insertTime;
  private String deviceCode;
  private String deviceName;
  private String deviceIp;
  private String stateName;
  private String state;
  private int crnodes;
  private Set<CrAffectedServiceDTO> setAffectedService;
  private Set<CrAffectedNodesDetailsDTO> setAffectedNode;
  private Map<String, CrAffectedNodesDetailsDTO> mapDeviceIdToAffectedNode;

  @Override
  public int compareTo(CrForNocProMonitorOnlineDTO o) {
    return this.crId.compareToIgnoreCase(o.getCrId());
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof CrForNocProMonitorOnlineDTO)) {
      return false;
    } else {
      return this.compareTo((CrForNocProMonitorOnlineDTO) obj) == 0;
    }
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 47 * hash + Objects.hashCode(this.crId);
    return hash;
  }
}
