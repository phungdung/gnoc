package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRCreateAutoCRDTO;
import java.util.List;

public interface SRCreateAutoCrRepository {

  List<SRCreateAutoCRDTO> searchSRCreateAutoCR(SRCreateAutoCRDTO tDTO,
      int start, int maxResult, String sortType, String sortField);

  ResultInSideDto insertOrUpdateSRCreateAutoCr(SRCreateAutoCRDTO dto);

  List<SRCreateAutoCRDTO> getInforTemplate(SRCreateAutoCRDTO dto);
}
