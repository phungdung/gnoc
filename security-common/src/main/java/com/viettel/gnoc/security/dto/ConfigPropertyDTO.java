
package com.viettel.gnoc.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConfigPropertyDTO{

  private String key;
  private String value;
  private String description;

  public ConfigPropertyDTO(String key, String value, String description) {
    this.key = key;
    this.value = value;
    this.description = description;
  }

  public ConfigPropertyDTO(String key, String value) {
    this.key = key;
    this.value = value;
  }
}
