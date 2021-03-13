package com.viettel.gnoc.pt.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.pt.dto.ProblemCrDTO;
import java.util.List;

public interface ProblemCrRepository {

  ResultInSideDto insertProblemCr(ProblemCrDTO problemCrDTO);

  String updateProblemCr(ProblemCrDTO problemCrDTO);

  String deleteProblemCr(Long id);

  List<ProblemCrDTO> getListProblemCrDTO(ProblemCrDTO problemCrDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  String deleteListProblemCr(List<ProblemCrDTO> problemCrListDTO);

  List<ProblemCrDTO> getListProblemCrByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<String> getSequenseProblemCr(String seqName, int... size);

  String insertOrUpdateListProblemCr(List<ProblemCrDTO> problemCrDTO);

  ProblemCrDTO findProblemCrById(Long id);

  List<ProblemCrDTO> onSearchProblemCrDTO(ProblemCrDTO dto);
}
