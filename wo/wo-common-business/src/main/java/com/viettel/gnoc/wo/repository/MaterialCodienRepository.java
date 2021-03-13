package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.MaterialCodienDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialCodienRepository {

  List<MaterialCodienDTO> getListMaterialCodienByWoId(Long woId);

  ResultInSideDto deleteMaterialCodien(Long id);

  ResultInSideDto inserOrUpdateMaterialCodien(MaterialCodienDTO materialCodienDTO);
}
