package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoPendingDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoPendingRepository {

  List<WoPendingDTO> getListWoPendingByWoId(Long woId);

  ResultInSideDto insertWoPending(WoPendingDTO woPendingDTO);

  ResultInSideDto updateWoPending(WoPendingDTO woPendingDTO);

  ResultInSideDto deleteWoPending(Long woPendingId);
}
