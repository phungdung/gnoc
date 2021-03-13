package com.viettel.gnoc.pt.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.pt.dto.ProblemActionLogsDTO;
import com.viettel.gnoc.pt.repository.ProblemActionLogsRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class ProblemActionLogsBusinessImpl implements ProblemActionLogsBusiness {

  @Autowired
  ProblemActionLogsRepository problemActionLogsRepository;

  @Override
  public List<ProblemActionLogsDTO> getListProblemActionLogsByCondition(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    log.debug("Request to getListProblemActionLogsByCondition: {}", lstCondition);
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    return problemActionLogsRepository
        .getListProblemActionLogsByCondition(lstCondition, rowStart, maxRow,
            sortType, sortFieldList);
  }

  @Override
  public List<ProblemActionLogsDTO> getListProblemActionLogsDTO(
      ProblemActionLogsDTO problemActionLogsDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    log.debug("Request to getListProblemActionLogsDTO: {}", problemActionLogsDTO);
    return problemActionLogsRepository.getListProblemActionLogsDTO(problemActionLogsDTO, rowStart,
        maxRow, sortType, sortFieldList);
  }

  @Override
  public String insertOrUpdateListProblemActionLogs(
      List<ProblemActionLogsDTO> problemActionLogsDTO) {
    log.debug("Request to insertOrUpdateListProblemActionLogs: {}", problemActionLogsDTO);
    return problemActionLogsRepository.insertOrUpdateListProblemActionLogs(problemActionLogsDTO);
  }

  @Override
  public ResultInSideDto insertProblemActionLogs(ProblemActionLogsDTO problemActionLogsDTO) {
    log.debug("Request to insertProblemActionLogs: {}", problemActionLogsDTO);
    return problemActionLogsRepository.insertProblemActionLogs(problemActionLogsDTO);
  }

  @Override
  public ProblemActionLogsDTO findProblemActionLogsById(Long id) {
    log.debug("Request to findProblemActionLogsById: {}", id);
    return problemActionLogsRepository.findProblemActionLogsById(id);
  }

  @Override
  public List<String> getSequenseProblemActionLogs(String seqName, int... size) {
    log.debug("Request to getSequenseProblemActionLogs: {}", seqName);
    return problemActionLogsRepository.getSequenseProblemActionLogs(seqName, size);
  }

  @Override
  public String updateProblemActionLogs(ProblemActionLogsDTO problemActionLogsDTO) {
    log.debug("Request to updateProblemActionLogs: {}", problemActionLogsDTO);
    return problemActionLogsRepository.updateProblemActionLogs(problemActionLogsDTO);
  }

  @Override
  public String deleteListProblemActionLogs(List<ProblemActionLogsDTO> problemActionLogsListDTO) {
    log.debug("Request to deleteListProblemActionLogs: {}", problemActionLogsListDTO);
    return problemActionLogsRepository.deleteListProblemActionLogs(problemActionLogsListDTO);
  }

  @Override
  public String deleteProblemActionLogs(Long id) {
    log.debug("Request to deleteProblemActionLogs: {}", id);
    return problemActionLogsRepository.deleteProblemActionLogs(id);
  }

  @Override
  public Datatable onSearchProblemActionLogsDTO(ProblemActionLogsDTO dto) {
    log.debug("Request to onSearchProblemActionLogsDTO: {}", dto);
    return problemActionLogsRepository.onSearchProblemActionLogsDTO(dto);
  }
}
