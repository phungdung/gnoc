package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.MaterialCodienDTO;
import java.util.List;

public interface MaterialCodienBusiness {

  List<MaterialCodienDTO> getListMaterialCodienByWoId(Long woId);

  ResultInSideDto deleteMaterialCodienByWoId(Long woId);

  ResultInSideDto inserOrUpdateMaterialCodien(MaterialCodienDTO materialCodienDTO);

}
