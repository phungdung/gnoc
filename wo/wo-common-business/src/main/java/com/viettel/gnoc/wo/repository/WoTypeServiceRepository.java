package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.wo.dto.WoTypeServiceInsideDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface WoTypeServiceRepository {

  WoTypeServiceInsideDTO getTypeService(Long woTypeId, Long serviceId);

}
