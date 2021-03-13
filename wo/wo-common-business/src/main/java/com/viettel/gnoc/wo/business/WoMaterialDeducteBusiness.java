package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteInsideDTO;
import java.util.List;

public interface WoMaterialDeducteBusiness {

  ResultInSideDto putMaterialDeducteToIM(Long woId, Boolean isRollback);

  ResultInSideDto rollBackDeducteToIM(Long woId);

  ResultInSideDto putMaterialDeducte(
      List<WoMaterialDeducteInsideDTO> listWoMaterialDeducteInsideDTO);

  ResultInSideDto deleteMaterialDeducte(Long woId, Long userId);

  List<WoMaterialDeducteInsideDTO> getMaterialDeducteKeyByWO(Long woId);

  String validateMaterialCompleted(List<WoMaterialDeducteInsideDTO> listMaterial);

  List<WoMaterialDeducteInsideDTO> onSearch(WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO,
      int rowStart,
      int maxRow, String sortType, String sortFieldList);

  List<WoMaterialDeducteDTO> getMaterialDeducteKeyByWOOutSide(Long woId);

}
