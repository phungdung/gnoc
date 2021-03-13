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
public class Node {

  private String nodeCode;
  private String netWorkClass;

  @Override
  public int hashCode() {
    int hash = 5;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Node other = (Node) obj;
    if (!Objects.equals(this.nodeCode, other.nodeCode)) {
      return false;
    }
    if (!Objects.equals(this.netWorkClass, other.netWorkClass)) {
      return false;
    }
    return true;
  }
}
