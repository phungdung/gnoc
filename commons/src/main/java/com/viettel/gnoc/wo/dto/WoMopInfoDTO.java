package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.WoMopInfoEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ITSOL
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoMopInfoDTO extends BaseDto {

  private Long woMopId;
  private Long woId;
  private Long mopId;
  private String mopCode;
  private Date runTime;
  private Date responseTime;
  private String result;

  public WoMopInfoEntity toEntity() {
    return new WoMopInfoEntity(woMopId, woId, mopId, mopCode, runTime, responseTime, result);
  }

}
