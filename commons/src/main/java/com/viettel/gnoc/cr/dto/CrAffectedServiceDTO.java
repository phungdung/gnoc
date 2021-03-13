package com.viettel.gnoc.cr.dto;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CrAffectedServiceDTO implements Comparable<CrAffectedServiceDTO> {

  private String affectedServiceId;
  private String affectedServiceCode;
  private String affectedServiceName;
  private String affectedServiceDescription;

  @Override
  public int compareTo(CrAffectedServiceDTO o) {
    return this.affectedServiceId.compareToIgnoreCase(o.getAffectedServiceId());
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof CrAffectedServiceDTO)) {
      return false;
    }
    return this.affectedServiceId
        .equalsIgnoreCase(((CrAffectedServiceDTO) obj).getAffectedServiceId());
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 97 * hash + Objects.hashCode(this.affectedServiceId);
    return hash;
  }
}
