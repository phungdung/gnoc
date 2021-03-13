package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoAutoCheckDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface WoAutoCheckRepository {

  ResultInSideDto actionUpdateWoAutoCheck(WoInsideDTO woInsideDTO, WoAutoCheckDTO woAutoCheckDTO,
      boolean isFalse);
}
