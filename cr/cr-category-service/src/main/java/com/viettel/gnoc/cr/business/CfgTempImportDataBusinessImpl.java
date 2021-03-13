package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.TempImportDataDTO;
import com.viettel.gnoc.cr.repository.CfgTempImportDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class CfgTempImportDataBusinessImpl implements CfgTempImportDataBusiness {

  @Autowired
  private CfgTempImportDataRepository cfgTempImportDataRepository;

  @Override
  public TempImportDataDTO findCfgTempImportDataById(Long id) {
    log.debug("Request to findCfgTempImportDataById : {}", id);
    return cfgTempImportDataRepository.findCfgTempImportDataById(id);
  }

  @Override
  public ResultInSideDto insertTempImportData(TempImportDataDTO tempImportDataDTO) {
    log.debug("Request to insertTempImportData : {}", tempImportDataDTO);
    return cfgTempImportDataRepository.insertTempImportData(tempImportDataDTO);
  }

  @Override
  public String deleteTempImportDataById(Long id) {
    log.debug("Request to deleteTempImportDataById : {}", id);
    return cfgTempImportDataRepository.deleteTempImportDataById(id);
  }

  @Override
  public String updateTempImportData(TempImportDataDTO tempImportDataDTO) {
    log.debug("Request to updateTempImportData : {}", tempImportDataDTO);
    return cfgTempImportDataRepository.updateTempImportData(tempImportDataDTO);
  }

  @Override
  public Datatable getListTempImportData(TempImportDataDTO tempImportDataDTO) {
    log.debug("Request to getListTempImportData : {}", tempImportDataDTO);
    return cfgTempImportDataRepository.getListTempImportData(tempImportDataDTO);
  }
}
