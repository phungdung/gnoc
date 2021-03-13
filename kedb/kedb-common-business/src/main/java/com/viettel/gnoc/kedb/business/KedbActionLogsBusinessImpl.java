package com.viettel.gnoc.kedb.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.kedb.dto.KedbActionLogsDTO;
import com.viettel.gnoc.kedb.repository.KedbActionLogsRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class KedbActionLogsBusinessImpl implements KedbActionLogsBusiness {

  @Autowired
  KedbActionLogsRepository kedbActionLogsRepository;

  @Override
  public List<KedbActionLogsDTO> getListKedbActionLogsDTO(KedbActionLogsDTO kedbActionLogsDTO) {
    log.debug("Request to getListKedbActionLogsDTO: {}", kedbActionLogsDTO);
    return kedbActionLogsRepository.getListKedbActionLogsDTO(kedbActionLogsDTO);
  }

  @Override
  public List<KedbActionLogsDTO> getListKedbActionLogsByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    log.debug("Request to getListKedbActionLogsByCondition: {}", lstCondition);
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    return kedbActionLogsRepository
        .getListKedbActionLogsByCondition(lstCondition, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<String> getSequenseKedbActionLogs(String seqName, int... size) {
    log.debug("Request to getSequenseKedbActionLogs: {}", seqName);
    return kedbActionLogsRepository.getSequenseKedbActionLogs(seqName, size);
  }

  @Override
  public String updateKedbActionLogs(KedbActionLogsDTO kedbActionLogsDTO) {
    log.debug("Request to updateKedbActionLogs: {}", kedbActionLogsDTO);
    return kedbActionLogsRepository.updateKedbActionLogs(kedbActionLogsDTO);
  }

  @Override
  public ResultInSideDto insertKedbActionLogs(KedbActionLogsDTO kedbActionLogsDTO) {
    log.debug("Request to insertKedbActionLogs: {}", kedbActionLogsDTO);
    return kedbActionLogsRepository.insertKedbActionLogs(kedbActionLogsDTO);
  }

  @Override
  public String insertOrUpdateListKedbActionLogs(List<KedbActionLogsDTO> kedbActionLogsDTO) {
    log.debug("Request to insertOrUpdateListKedbActionLogs: {}", kedbActionLogsDTO);
    return kedbActionLogsRepository.insertOrUpdateListKedbActionLogs(kedbActionLogsDTO);
  }

  @Override
  public KedbActionLogsDTO findKedbActionLogsById(Long id) {
    log.debug("Request to findKedbActionLogsById: {}", id);
    return kedbActionLogsRepository.findKedbActionLogsById(id);
  }

  @Override
  public String deleteKedbActionLogs(Long id) {
    log.debug("Request to deleteKedbActionLogs: {}", id);
    return kedbActionLogsRepository.deleteKedbActionLogs(id);
  }

  @Override
  public String deleteListKedbActionLogs(List<KedbActionLogsDTO> kedbActionLogsListDTO) {
    log.debug("Request to deleteListKedbActionLogs: {}", kedbActionLogsListDTO);
    return kedbActionLogsRepository.deleteListKedbActionLogs(kedbActionLogsListDTO);
  }

  @Override
  public Datatable onSearchKedbActionLogs(KedbActionLogsDTO dto) {
    log.debug("Request to onSearchKedbActionLogs: {}", dto);
    return kedbActionLogsRepository.onSearchKedbActionLogs(dto);
  }
}
