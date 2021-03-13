package com.viettel.gnoc.pt.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.pt.dto.ProblemWoDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import java.util.List;

public interface ProblemWoBusiness {

  ResultInSideDto insertProblemWo(ProblemWoDTO problemWoDTO);

  String updateProblemWo(ProblemWoDTO problemWoTDTO);

  String deleteProblemWo(Long id);

//
//  List<ProblemWoDTO> getListProblemWoDTO(ProblemWoDTO problemWoDTO,
//      int rowStart, int maxRow, String sortType, String sortFieldList);

  String deleteListProblemWo(List<ProblemWoDTO> problemWoDTOList);

  Datatable getListProblemWo(ProblemWoDTO problemWoDTO);

  String insertOrUpdateListProblemWo(List<ProblemWoDTO> problemWoDTO);

  ProblemWoDTO findProblemWoById(Long id);

  List<ProblemWoDTO> getListProblemWoByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  //String getSequenseProblemWo();

  List<String> getSeqProblemWo(String seqName, int... size);

  //Wo
  Datatable getListDataSearchWeb(ProblemsInsideDTO problemsInsideDTO);

  //Datatable getListDataWo(WoDTO wotDTO);

}
