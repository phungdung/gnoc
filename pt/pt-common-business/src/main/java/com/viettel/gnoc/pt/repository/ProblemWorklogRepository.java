package com.viettel.gnoc.pt.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.pt.dto.ProblemWorklogDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemWorklogRepository {

  ResultInSideDto onInsert(ProblemWorklogDTO problemWorklogDTO) throws Exception;

  List getListProblemWorklogByCondition(List<ConditionBean> lstConditionBean, int start,
      int maxResult, String sortType, String sortField);

  List<String> getSequenceProblemWorklog(String seqName, int[] size);

  ProblemWorklogDTO findProblemWorklogById(Long id) throws Exception;

  String deleteProblemWorklog(Long problemWorklogId);

  String deleteListProblemWorklog(List<ProblemWorklogDTO> problemWorklogDTOS);

  String updateProblemWorklog(ProblemWorklogDTO problemWorklogDTO) throws Exception;

  String insertOrUpdateListProblemWorklog(List<ProblemWorklogDTO> problemWorklogDTOS)
      throws Exception;

  Datatable getListProblemWorklogDTO(ProblemWorklogDTO problemWorklogDTO);
}
