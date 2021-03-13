package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrHisRepository {

  ResultInSideDto insertOrUpdate(CrHisDTO dto);

  List<CrHisDTO> getListCrHis(CrHisDTO crhisdto);

  List<CrHisDTO> search(CrHisDTO tDTO, int start, int maxResult,
      String sortType, String sortField);
}
