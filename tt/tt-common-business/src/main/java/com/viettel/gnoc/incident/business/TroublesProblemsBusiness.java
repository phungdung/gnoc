package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;

public interface TroublesProblemsBusiness {

  Datatable getListProblems(ProblemsInsideDTO problemsInsideDTO);
}
