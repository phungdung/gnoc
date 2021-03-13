package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.wo.dto.MaterialThresDTO;
import com.viettel.gnoc.wo.repository.MaterialThresRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class MaterialThresBusinessImpl implements MaterialThresBusiness {

  @Autowired
  MaterialThresRepository materialThresRepository;

  @Override
  public List<MaterialThresDTO> getListMaterialDTOByAction(Long actionId, Long serviceId,
      Long infraType, boolean isEnable, String nationCode, Long type) {
    return materialThresRepository.getListMaterialDTOByAction(actionId, serviceId, infraType, isEnable, nationCode, type);
  }
}
