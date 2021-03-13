package com.viettel.gnoc.pt.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.pt.dto.ProblemNodeDTO;
import com.viettel.gnoc.pt.repository.ProblemNodeRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class ProblemNodeBusinessImpl implements ProblemNodeBusiness {

  @Autowired
  ProblemNodeRepository problemNodeRepository;

  @Override
  public List<ProblemNodeDTO> getListProblemNodeByCondition(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    log.debug("Request to getListProblemNodeByCondition: {}", lstCondition);
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    return problemNodeRepository.getListProblemNodeByCondition(lstCondition, rowStart, maxRow,
        sortType, sortFieldList);
  }

  @Override
  public List<ProblemNodeDTO> getListProblemNodeDTO(ProblemNodeDTO problemNodeDTO,
      int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    log.debug("Request to getListProblemNodeDTO: {}", problemNodeDTO);
    return problemNodeRepository.getListProblemNodeDTO(problemNodeDTO, rowStart,
        maxRow, sortType, sortFieldList);
  }

  @Override
  public String insertOrUpdateListProblemNode(List<ProblemNodeDTO> problemNodeDTO) {
    log.debug("Request to insertOrUpdateListProblemNode: {}", problemNodeDTO);
    return problemNodeRepository.insertOrUpdateListProblemNode(problemNodeDTO);
  }

  @Override
  public ResultInSideDto insertProblemNode(ProblemNodeDTO problemNodeDTO) {
    log.debug("Request to insertProblemNode: {}", problemNodeDTO);
    return problemNodeRepository.insertProblemNode(problemNodeDTO);
  }

  @Override
  public ProblemNodeDTO findProblemNodeById(Long id) {
    log.debug("Request to findProblemNodeById: {}", id);
    return problemNodeRepository.findProblemNodeById(id);
  }

  @Override
  public List<String> getSequenseProblemNode(String seqName, int... size) {
    log.debug("Request to getSequenseProblemNode: {}", seqName);
    return problemNodeRepository.getSequenseProblemNode(seqName, size);
  }

  @Override
  public String updateProblemNode(ProblemNodeDTO problemNodeDTO) {
    log.debug("Request to updateProblemNode: {}", problemNodeDTO);
    return problemNodeRepository.updateProblemNode(problemNodeDTO);
  }

  @Override
  public String deleteListProblemNode(List<ProblemNodeDTO> problemNodeListDTO) {
    log.debug("Request to deleteListProblemNode: {}", problemNodeListDTO);
    return problemNodeRepository.deleteListProblemNode(problemNodeListDTO);
  }

  @Override
  public String deleteProblemNode(Long id) {
    log.debug("Request to deleteProblemNode: {}", id);
    return problemNodeRepository.deleteProblemNode(id);
  }

  @Override
  public Datatable onSearchProblemNodeDTO(ProblemNodeDTO dto) {
    log.debug("Request to onSearchProblemNodeDTO: {}", dto);
    return problemNodeRepository.onSearchProblemNodeDTO(dto);
  }
}
