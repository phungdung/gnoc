package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteInsideDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoMaterialDeducteRepository {

  List<WoMaterialDeducteInsideDTO> getListWoMaterialDeducteByWoId(Long woId);

  ResultInSideDto updateWoMaterialDeducte(WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO);

  List<WoMaterialDeducteInsideDTO> getMaterialDeducteKeyByWO(Long woId);

  List<WoMaterialDeducteInsideDTO> getListWoMaterialDeducteDeleteByWoIdAndUserId(Long woId,
      Long userId);

  ResultInSideDto deleteWoMaterialDeducte(Long woMaterialDeducteId);

  ResultInSideDto insertWoMaterialDeducte(WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO);

  List<WoMaterialDeducteInsideDTO> onSearch(WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO,
      int rowStart,
      int maxRow, String sortType, String sortFieldList);

  List<WoMaterialDeducteDTO> getMaterialDeducteKeyByWOOutSide(Long woId);
}
