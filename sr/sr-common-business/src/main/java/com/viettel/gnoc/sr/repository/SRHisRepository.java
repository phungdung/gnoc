package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRHisDTO;
import java.util.List;

public interface SRHisRepository {

  ResultInSideDto createSRHis(SRHisDTO srHisDTO);

  List<SRHisDTO> getListSRHisDTO(SRHisDTO dto, int rowStart, int maxRow, String sortType,
      String sortFieldList);

  List<SRHisDTO> getListSRHisDTOBySrId(Long srID);
}
