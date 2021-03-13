package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoDeclareServiceDTO;
import com.viettel.gnoc.wo.model.WoDeclareServiceEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface WoDeclareServiceRepository {

  ResultInSideDto insertOrUpdateWoDeclareService(WoDeclareServiceDTO woDeclareServiceDTO);

  WoDeclareServiceEntity findById(Long woId);
}
