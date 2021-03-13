package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoDetailRepository {

  ResultInSideDto insertWoDetail(WoDetailDTO woDetailDTO);

  ResultInSideDto update(WoDetailDTO woDetailDTO);

  WoDetailDTO findById(Long woId);

  List<WoDetailDTO> getListWoDetailDTO(WoDetailDTO woDetailDTO);
}
