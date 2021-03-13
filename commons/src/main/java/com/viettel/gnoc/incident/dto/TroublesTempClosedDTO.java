package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.incident.model.TroublesTempClosedEntity;
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
public class TroublesTempClosedDTO extends BaseDto {

  private Long troublesTempClosedId;
  private String troubleCode;
  private Date lastUpdateTime;
  private Date assignTimeTemp;
  private Long state;
  private String relatedKedb;
  private Date closedTime;
  private Date clearTime;
  private Date endTroubleTime;
  private String closeCode;
  private Long reasonId;
  private String reasonName;
  private String rootCause;
  private Long solutionType;
  private String workArround;
  private Date createdTime;

  public TroublesTempClosedEntity toEntity() {
    return new TroublesTempClosedEntity(troublesTempClosedId, troubleCode, lastUpdateTime,
        assignTimeTemp, state, relatedKedb, closedTime, clearTime, endTroubleTime, closeCode,
        reasonId, reasonName, rootCause, solutionType, workArround, createdTime);
  }

}
