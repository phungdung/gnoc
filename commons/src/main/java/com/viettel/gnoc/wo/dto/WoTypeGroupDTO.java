package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.WoTypeGroupEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoTypeGroupDTO extends BaseDto {

  private Long woGroupId;
  private Long woTypeId;
  private Long woTypeGroupId;

  private String woGroupCode;
  private String woGroupName;
  private String woTypeCode;
  private String woTypeName;
  private Long assign;
  private String assignName;
  private String resultImport;

  private List<Long> listWoTypeIdInsert;

  public WoTypeGroupDTO(Long woGroupId, Long woTypeId, Long woTypeGroupId) {
    this.woGroupId = woGroupId;
    this.woTypeId = woTypeId;
    this.woTypeGroupId = woTypeGroupId;
  }

  public WoTypeGroupEntity toEntity() {
    return new WoTypeGroupEntity(woGroupId, woTypeId, woTypeGroupId);
  }

}
