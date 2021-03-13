package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.wo.dto.MaterialThresDTO;
import com.viettel.gnoc.wo.dto.MaterialThresInsideDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialThresRepository {

  List<MaterialThresDTO> getDataList(MaterialThresInsideDTO materialThresDTO);

  List<MaterialThresInsideDTO> getListMaterialDTOByAction(
      MaterialThresInsideDTO materialThresDTO, boolean isEnable, String nationCode);

  List<MaterialThresDTO> getListMaterialDTOByAction(/*Long woId, */Long actionId,
      Long serviceId, Long infraType, boolean isEnable, String nationCode, Long type);
}
