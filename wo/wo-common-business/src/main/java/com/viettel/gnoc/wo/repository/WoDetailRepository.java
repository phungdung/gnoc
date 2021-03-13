package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface WoDetailRepository {

  ResultInSideDto insertUpdateWoDetail(WoDetailDTO woDetailDTO);

  WoDetailDTO findWoDetailById(Long woId);

}
