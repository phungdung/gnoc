package com.viettel.gnoc.pt.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.pt.dto.ProblemNodeDTO;
import java.util.List;

public interface ProblemNodeRepository {

  String deleteProblemNode(Long id);

  String updateProblemNode(ProblemNodeDTO problemNodeDTO);

  String deleteListProblemNode(List<ProblemNodeDTO> problemNodeListDTO);

  String insertOrUpdateListProblemNode(List<ProblemNodeDTO> problemNodeDTO);

  ResultInSideDto insertProblemNode(ProblemNodeDTO problemNodeDTO);

  ProblemNodeDTO findProblemNodeById(Long id);

  List<String> getSequenseProblemNode(String seqName, int... size);

  List<ProblemNodeDTO> getListProblemNodeDTO(ProblemNodeDTO problemNodeDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<ProblemNodeDTO> getListProblemNodeByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  Datatable onSearchProblemNodeDTO(ProblemNodeDTO dto);

}
