package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.incident.model.WoSupportEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WoSupportDTO extends BaseDto {

  private Long id;
  private Long woID;
  private Long cfgSupportCaseID;
  private Long cfgSupportCaseTestID;
  private Long result;
  private String description;
  private String fileName;
  private Date updateTime;

  public WoSupportEntity toEntity() {
    return new WoSupportEntity(id, woID, cfgSupportCaseID, cfgSupportCaseTestID, result,
        description, fileName, updateTime);
  }
}
