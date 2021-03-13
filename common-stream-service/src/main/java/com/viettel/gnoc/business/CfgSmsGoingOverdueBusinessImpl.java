package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.CfgSmsGoingOverdueDTO;
import com.viettel.gnoc.commons.dto.CfgSmsUserGoingOverdueFullDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.repository.CfgSmsGoingOverdueRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class CfgSmsGoingOverdueBusinessImpl implements CfgSmsGoingOverdueBusiness {

  @Autowired
  CfgSmsGoingOverdueRepository cfgSmsGoingOverdueRepository;

  @Override
  public Datatable getListCfgSmsGoingOverdueDTO(
      CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO) {
    log.debug("Request to getListCfgSmsGoingOverdueDTO : {}", cfgSmsGoingOverdueDTO);
    return cfgSmsGoingOverdueRepository.getListCfgSmsGoingOverdueDTO(cfgSmsGoingOverdueDTO);
  }

  @Override
  public String updateCfgSmsGoingOverdue(CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO) {
    log.debug("Request to updateCfgSmsGoingOverdue : {]", cfgSmsGoingOverdueDTO);
    return cfgSmsGoingOverdueRepository.updateCfgSmsGoingOverdue(cfgSmsGoingOverdueDTO);
  }

  @Override
  public String deleteCfgSmsGoingOverdue(Long id) {
    log.debug("Request to deleteCfgSmsGoingOverdue : {}", id);
    return cfgSmsGoingOverdueRepository.deleteCfgSmsGoingOverdue(id);
  }

  @Override
  public String deleteListCfgSmsGoingOverdue(
      List<CfgSmsGoingOverdueDTO> cfgSmsGoingOverdueListDTO) {
    return cfgSmsGoingOverdueRepository.deleteListCfgSmsGoingOverdue(cfgSmsGoingOverdueListDTO);
  }

  @Override
  public CfgSmsGoingOverdueDTO findCfgSmsGoingOverdueById(Long id) {
    log.debug("Request to findCfgSmsGoingOverdueById : {}", id);
    return cfgSmsGoingOverdueRepository.findCfgSmsGoingOverdueById(id);
  }

  @Override
  public ResultInSideDto insertCfgSmsGoingOverdue(CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO) {
    log.debug("Request to insertCfgSmsGoingOverdue : {}", cfgSmsGoingOverdueDTO);
    return cfgSmsGoingOverdueRepository.insertCfgSmsGoingOverdue(cfgSmsGoingOverdueDTO);
  }

  @Override
  public List<String> getSequenseCfgSmsGoingOverdue(String seqName, int... size) {
    log.debug("Request to getSequenseCfgSmsGoingOverdue : {}", seqName);
    return cfgSmsGoingOverdueRepository.getSequenseCfgSmsGoingOverdue(seqName, size);
  }

  @Override
  public List<CfgSmsGoingOverdueDTO> getListCfgSmsGoingOverdueByCondition(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortName) {
    log.debug("Request getListCfgSmsGoingOverdueByCondition : {}", lstCondition);
    return cfgSmsGoingOverdueRepository
        .getListCfgSmsGoingOverdueByCondition(lstCondition, rowStart, maxRow, sortType, sortName);
  }

  @Override
  public UsersInsideDto getUserInfo(Long userId) {
    log.debug("Request to getUserInfo : {}", userId);
    return cfgSmsGoingOverdueRepository.getUserInfo(userId);
  }

  @Override
  public String insertOrUpdateCfg(List<CfgSmsGoingOverdueDTO> cfgSmsGoingOverdueDTOS) {
    log.debug("Request to insertOrUpdateCfg : {}", cfgSmsGoingOverdueDTOS);
    return cfgSmsGoingOverdueRepository.insertOrUpdateCfg(cfgSmsGoingOverdueDTOS);
  }

  @Override
  public ResultInSideDto deleteCfgSmsGoingOverdueAndUserList(
      CfgSmsGoingOverdueDTO cfgSmsGoingOverdueDTO) {
    log.debug("Request to deleteCfgSmsGoingOverdueAndUserList : {}", cfgSmsGoingOverdueDTO);
    return cfgSmsGoingOverdueRepository.deleteCfgSmsGoingOverdueAndUserList(cfgSmsGoingOverdueDTO);
  }

  @Override
  public List<CfgSmsGoingOverdueDTO> getListCfgSmsGoingOverdueDTO_allFields(
      String cfgName, String unitId, String userId, String levelId) {
    log.debug("Request to getListCfgSmsGoingOverdueDTO_allFields : {}", cfgName, unitId, userId,
        levelId);
    return cfgSmsGoingOverdueRepository
        .getListCfgSmsGoingOverdueDTO_allFields(cfgName, unitId, userId, levelId);
  }

  @Override
  public Datatable getListCfgSmsUser(
      CfgSmsUserGoingOverdueFullDTO cfgSmsUserGoingOverdueFullDTO) {
    log.debug("Request to getListCfgSmsUser : {}", cfgSmsUserGoingOverdueFullDTO);
    return cfgSmsGoingOverdueRepository.getListCfgSmsUser(cfgSmsUserGoingOverdueFullDTO);
  }

  @Override
  public ResultInSideDto updateCfgSmsGoingOverdue2(CfgSmsGoingOverdueDTO dto) {
    log.debug("Request to updateCfgSmsGoingOverdue2 : {}", dto);
    return cfgSmsGoingOverdueRepository.updateCfgSmsGoingOverdue2(dto);
  }

  @Override
  public Long getMaxLevelIDByUnitID(CfgSmsGoingOverdueDTO dto) {
    log.debug("Request to getMaxLevelIDByUnitID : {}", dto);
    return cfgSmsGoingOverdueRepository.getMaxLevelIDByUnitID(dto);
  }

}
