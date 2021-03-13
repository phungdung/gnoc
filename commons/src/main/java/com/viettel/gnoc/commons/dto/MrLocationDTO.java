package com.viettel.gnoc.commons.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author tripm
 * @version 2.0
 * @since 23/06/2020 15:00:00
 */
@Getter
@Setter
@NoArgsConstructor
public class MrLocationDTO {

  private String locationId;
  private String locationName;
  private String locationCode;
  private String locationLevel;
}
