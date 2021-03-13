package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.model.CfgWoTickHelpEntity;
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
public class CfgWoTickHelpInsideDTO extends BaseDto {

  private Long id;
  private Long woId;
  private String systemName;
  private Long tickHelpId;
  private Long status;

  public CfgWoTickHelpEntity toEntity() {
    return new CfgWoTickHelpEntity(id, woId, systemName, tickHelpId, status);
  }

  public CfgWoTickHelpDTO toOutsideDto() {
    CfgWoTickHelpDTO model = new CfgWoTickHelpDTO(
        StringUtils.validString(id) ? String.valueOf(id) : null,
        StringUtils.validString(woId) ? String.valueOf(woId) : null,
        systemName,
        StringUtils.validString(tickHelpId) ? String.valueOf(tickHelpId) : null,
        StringUtils.validString(status) ? String.valueOf(status) : null
    );
    return model;
  }


}
