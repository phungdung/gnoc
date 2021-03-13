package com.viettel.gnoc.cr.dto;

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
public class CrAffectedNodesDetailsDTO implements Comparable<CrAffectedNodesDetailsDTO> {

  private String insertTime;
  private String deviceId;
  private String deviceCode;
  private String deviceName;
  private Set<String> setDeviceIp;

  @Override
  public int compareTo(CrAffectedNodesDetailsDTO o) {
    return this.deviceId.compareToIgnoreCase(o.getDeviceId());
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof CrAffectedNodesDetailsDTO)) {
      return false;
    }
    return this.deviceId.equalsIgnoreCase(((CrAffectedNodesDetailsDTO) obj).deviceId);
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 97 * hash + Objects.hashCode(this.deviceId);
    return hash;
  }
}
