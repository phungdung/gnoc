package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoCdGroupUnitDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoCdGroupUnitRepository {

  List<WoCdGroupUnitDTO> getListWoCdGroupUnitDTO(WoCdGroupUnitDTO woCdGroupUnitDTO);

  ResultInSideDto insertWoCdGroupUnit(WoCdGroupUnitDTO woCdGroupUnitDTO);

  ResultInSideDto updateWoCdGroupUnit(WoCdGroupUnitDTO woCdGroupUnitDTO);

  ResultInSideDto deleteWoCdGroupUnit(Long cdGroupUnitId);

  ResultInSideDto deleteWoCdGroupUnitByCdGroupId(Long cdGroupId);

  List<WoCdGroupUnitDTO> onSearchEntity(WoCdGroupUnitDTO woCdGroupUnitDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList);
}
