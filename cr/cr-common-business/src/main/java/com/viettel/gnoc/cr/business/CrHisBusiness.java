package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import java.util.List;

public interface CrHisBusiness {

  Datatable searchSql(CrHisDTO crhisdto);

  List<CrHisDTO> search(CrHisDTO tDTO, int start, int maxResult,
      String sortType, String sortField);
}
