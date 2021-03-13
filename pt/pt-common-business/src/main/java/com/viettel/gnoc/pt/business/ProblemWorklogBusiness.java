package com.viettel.gnoc.pt.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.pt.dto.ProblemWorklogDTO;
import java.util.List;

public interface ProblemWorklogBusiness {

  ResultInSideDto onInsert(ProblemWorklogDTO problemWorklogDTO) throws Exception;

  List getListProblemWorklogByCondition(List<ConditionBean> lstConditionBean, int start,
      int maxResult, String sortType, String sortField);

  List<String> getSequenseProblemWorklog(String seqName, int... size);

  ProblemWorklogDTO findProblemWorklogById(Long id) throws Exception;

  String deleteProblemWorklog(Long problemWorklogId);

  String deleteListProblemWorklog(List<ProblemWorklogDTO> problemWorklogDTOS);

  String updateProblemWorklog(ProblemWorklogDTO problemWorklogDTO) throws Exception;

  String insertOrUpdateListProblemWorklog(List<ProblemWorklogDTO> problemWorklogDTOS)
      throws Exception;

  Datatable getListProblemWorklogDTO(ProblemWorklogDTO problemWorklogDTO);
}
