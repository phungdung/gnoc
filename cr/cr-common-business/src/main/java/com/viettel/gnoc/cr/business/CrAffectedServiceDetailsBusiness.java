package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.cr.dto.CrAffectedServiceDetailsDTO;
import java.util.List;

public interface CrAffectedServiceDetailsBusiness {

  List<CrAffectedServiceDetailsDTO> search(CrAffectedServiceDetailsDTO serviceDetailsDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);
}
