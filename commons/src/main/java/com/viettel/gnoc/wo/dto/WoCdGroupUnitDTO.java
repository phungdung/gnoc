package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.WoCdGroupUnitEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoCdGroupUnitDTO extends BaseDto {

  private Long cdGroupUnitId;
  private Long cdGroupId;
  private Long unitId;
  private Long isRoot;

  private List<Long> listUnitIdDel;
  private List<Long> listUnitIdInsert;

  public WoCdGroupUnitDTO(Long cdGroupUnitId, Long cdGroupId, Long unitId, Long isRoot) {
    this.cdGroupUnitId = cdGroupUnitId;
    this.cdGroupId = cdGroupId;
    this.unitId = unitId;
    this.isRoot = isRoot;
  }

  public WoCdGroupUnitEntity toEntity() {
    return new WoCdGroupUnitEntity(cdGroupUnitId, cdGroupId, unitId, isRoot);
  }

}
