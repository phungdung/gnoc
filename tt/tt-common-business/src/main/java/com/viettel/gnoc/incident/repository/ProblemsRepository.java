package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemsRepository {

  Datatable getListProblems(ProblemsInsideDTO problemsInsideDTO);
}
