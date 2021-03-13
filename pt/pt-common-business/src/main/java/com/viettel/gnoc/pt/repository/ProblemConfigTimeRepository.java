package com.viettel.gnoc.pt.repository;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.pt.dto.ProblemConfigTimeDTO;
import java.util.List;

public interface ProblemConfigTimeRepository {
  //  List<CatItemDTO> getListComboboxGroupReasonOrSolution(String codeCategory);
  Datatable onSearchProbleConfigTime(ProblemConfigTimeDTO dto);
  ResultInSideDto insertProblemConfigTime(ProblemConfigTimeDTO dto);
  ResultInSideDto updateProblemConfigTime(ProblemConfigTimeDTO dto);
  ProblemConfigTimeDTO checkProblemConfigTimeCreateExit(ProblemConfigTimeDTO dto);
  ProblemConfigTimeDTO findProblemConfigTimeById(Long id);
  String deleteProblemConfigTime(Long id);
  List<ProblemConfigTimeDTO> onSearchExport(ProblemConfigTimeDTO problemConfigTimeDTO);
  boolean checkExisted(String reasonGroupId, String solutionTypeId,String typeId, String subCatagoryId, String id);
}
