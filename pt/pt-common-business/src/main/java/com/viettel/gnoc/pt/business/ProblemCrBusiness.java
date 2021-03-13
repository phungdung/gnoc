package com.viettel.gnoc.pt.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.pt.dto.ProblemCrDTO;
import java.util.List;

public interface ProblemCrBusiness {

  String deleteProblemCr(Long id);

  String updateProblemCr(ProblemCrDTO problemCrDTO);

  ProblemCrDTO findProblemCrById(Long id);

  ResultInSideDto insertProblemCr(ProblemCrDTO problemCrDTO);

  String deleteListProblemCr(List<ProblemCrDTO> problemCrListDTO);

  List<String> getSequenseProblemCr(String seqName, int... size);

  List<ProblemCrDTO> getListProblemCrDTO(ProblemCrDTO problemCrDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<ProblemCrDTO> getListProblemCrByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  String insertOrUpdateListProblemCr(List<ProblemCrDTO> problemCrDTO);

  List<ProblemCrDTO> onSearchProblemCrDTO(ProblemCrDTO dto);

  Datatable searchProblemCr(ProblemCrDTO problemCrDTO);
}
