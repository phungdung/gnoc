package com.viettel.gnoc.pt.business;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.pt.dto.ProblemConfigTimeDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ProblemConfigTimeBusiness {
  List<CatItemDTO> getListComboboxGroupReasonOrSolution(String codeCategory);
  Datatable onSearchProbleConfigTime(ProblemConfigTimeDTO dto);
  ResultInSideDto insertProblemConfigTime(ProblemConfigTimeDTO dto);
  ResultInSideDto updateProblemConfigTime(ProblemConfigTimeDTO dto);
  ProblemConfigTimeDTO findProblemConfigTimeById(Long id);
  String deleteProblemConfigTime(Long id);
  File getListProblemConfigTimeSearchExport(ProblemConfigTimeDTO dto) throws Exception;
  ResultInSideDto importProblemConfigTime(MultipartFile fileImport) throws Exception;
  File getFileTemplate() throws Exception;
}

