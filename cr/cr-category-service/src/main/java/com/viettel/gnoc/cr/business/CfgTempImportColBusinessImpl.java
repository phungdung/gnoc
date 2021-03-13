package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.TempImportColDTO;
import com.viettel.gnoc.cr.repository.CfgTempImportColRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class CfgTempImportColBusinessImpl implements CfgTempImportColBusiness {

  @Autowired
  private CfgTempImportColRepository cfgTempImportColRepository;

  @Override
  public TempImportColDTO findTempImportColById(Long id) {
    log.debug("Request to findTempImportById : {}", id);
    return cfgTempImportColRepository.findTempImportColById(id);
  }

  @Override
  public ResultInSideDto insertTempImportCol(TempImportColDTO tempImportColDTO) {
    log.debug("Request to insertTempImportCol : {}", tempImportColDTO);
    return cfgTempImportColRepository.insertTempImportCol(tempImportColDTO);
  }

  @Override
  public String deleteTempImportColById(Long id) {
    log.debug("Request to deleteTempImportColById : {}", id);
    return cfgTempImportColRepository.deleteTempImportColById(id);
  }

  @Override
  public String updateTempImportCol(TempImportColDTO dto) {
    log.debug("Request to updateTempImportCol : {}", dto);
    return cfgTempImportColRepository.updateTempImportCol(dto);
  }

  @Override
  public Datatable getListTempImportCol(TempImportColDTO dto) {
    log.debug("Request to getListTempImportCol : {}", dto);
    return cfgTempImportColRepository.getListTempImportCol(dto);
  }
}
