package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ITSOL
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class CfgWoTickHelpDTO {

  //Fields
  private String id;
  private String woId;
  private String systemName;
  private String tickHelpId;
  private String status;

  public CfgWoTickHelpInsideDTO toInsideDto() {
    CfgWoTickHelpInsideDTO model = new CfgWoTickHelpInsideDTO(
        StringUtils.validString(id) ? Long.valueOf(id) : null,
        StringUtils.validString(woId) ? Long.valueOf(woId) : null,
        systemName,
        StringUtils.validString(tickHelpId) ? Long.valueOf(tickHelpId) : null,
        StringUtils.validString(status) ? Long.valueOf(status) : null
    );
    return model;
  }

}
