package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.CfgInfoTtSpmDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CfgInfoTtSpmRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author TienNV
 */
@Service
@Transactional
@Slf4j
public class CfgInfoTtSpmBusinessImpl implements CfgInfoTtSpmBusiness {

  @Autowired
  protected CfgInfoTtSpmRepository cfgInfoTtSpmRepository;

  @Override
  public List<CfgInfoTtSpmDTO> getListCfgInfoTtSpmDTO(CfgInfoTtSpmDTO cfgInfoTtSpmDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return cfgInfoTtSpmRepository
        .getListCfgInfoTtSpmDTO(cfgInfoTtSpmDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public String updateCfgInfoTtSpm(CfgInfoTtSpmDTO dto) {
    log.debug("Request to updateCfgInfoTtSpm : {}", dto);
    return cfgInfoTtSpmRepository.updateCfgInfoTtSpm(dto);
  }

  @Override
  public String deleteCfgInfoTtSpm(Long id) {
    log.debug("Request to deleteCfgInfoTtSpm : {}", id);
    return cfgInfoTtSpmRepository.deleteCfgInfoTtSpm(id);
  }

  @Override
  public String deleteListCfgInfoTtSpm(List<CfgInfoTtSpmDTO> dto) {
    log.debug("Request to deleteListCfgInfoTtSpm : {}", dto);
    return cfgInfoTtSpmRepository.deleteListCfgInfoTtSpm(dto);
  }

  @Override
  public CfgInfoTtSpmDTO findCfgInfoTtSpmById(Long id) {
    return cfgInfoTtSpmRepository.findCfgInfoTtSpmById(id);
  }

  @Override
  public ResultInSideDto insertCfgInfoTtSpm(CfgInfoTtSpmDTO cfgInfoTtSpmDTO) {
    return cfgInfoTtSpmRepository.insertCfgInfoTtSpm(cfgInfoTtSpmDTO);
  }

  @Override
  public String insertOrUpdateListCfgInfoTtSpm(List<CfgInfoTtSpmDTO> cfgInfoTtSpmDTOS) {
    return cfgInfoTtSpmRepository.insertOrUpdateListCfgInfoTtSpm(cfgInfoTtSpmDTOS);
  }

  @Override
  public List<String> getSequenseCfgInfoTtSpm(String seqName, int... i) {
    return cfgInfoTtSpmRepository.getSequenseCfgInfoTtSpm(seqName, i);
  }

  @Override
  public List<CfgInfoTtSpmDTO> getListCfgInfoTtSpmByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortName) {
    return cfgInfoTtSpmRepository
        .getListCfgInfoTtSpmByCondition(lstCondition, rowStart, maxRow, sortType, sortName);
  }

  @Override
  public Datatable getListCfgInfoTtSpmDTO2(CfgInfoTtSpmDTO cfgInfoTtSpmDTO) {
    log.debug("Request to getListCfgInfoTtSpmDTO2 : {}", cfgInfoTtSpmDTO);
    return cfgInfoTtSpmRepository.getListCfgInfoTtSpmDTO2(cfgInfoTtSpmDTO);
  }
}
