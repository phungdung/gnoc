package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.wo.dto.MaterialThresDTO;
import java.util.List;

public interface MaterialThresBusiness {

  public List<MaterialThresDTO> getListMaterialDTOByAction(Long actionId, Long serviceId,
      Long infraType, boolean isEnable, String nationCode, Long type);
}
