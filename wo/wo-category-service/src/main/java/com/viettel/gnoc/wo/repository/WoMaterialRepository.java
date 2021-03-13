package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.MaterialThresInsideDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoMaterialRepository {

  Datatable getListWoMaterialPage(MaterialThresInsideDTO materialThresDTO);

  ResultInSideDto delete(Long materialThresId);

  ResultInSideDto insertOrUpdate(MaterialThresInsideDTO materialThresDTO);

  ResultInSideDto insertOrUpdateListMaterialThres(
      List<MaterialThresInsideDTO> materialThresDTOList);

  Datatable getListDataExport(MaterialThresInsideDTO materialThresDTO);

  ResultInSideDto add(MaterialThresInsideDTO materialThresDTO);

  ResultInSideDto edit(MaterialThresInsideDTO materialThresDTO);

  MaterialThresInsideDTO findByMaterialThresId(Long materialThresId);

  WoMaterialDTO findWoMaterialById(Long materialId);

  MaterialThresInsideDTO checkMaterialThresExist(Long infraType, Long actionId, Long serviceId,
      Long materialId);

  List<WoMaterialDTO> findAllMaterial(String materialName);
}
