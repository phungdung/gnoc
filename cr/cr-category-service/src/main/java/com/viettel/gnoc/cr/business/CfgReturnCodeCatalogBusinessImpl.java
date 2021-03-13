package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.ReturnCodeCatalogDTO;
import com.viettel.gnoc.cr.repository.CfgReturnCodeCatalogRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class CfgReturnCodeCatalogBusinessImpl implements CfgReturnCodeCatalogBusiness {

  @Autowired
  private CfgReturnCodeCatalogRepository cfgReturnCodeCatalogRepository;

  @Override
  public ReturnCodeCatalogDTO findCfgReturnCodeCatalogById(Long id) {
    log.debug("Request to findCfgReturnCodeCatalogById : {}", id);
    return cfgReturnCodeCatalogRepository.findCfgReturnCodeCatalogById(id);
  }

  @Override
  public ResultInSideDto insertCfgReturnCodeCatalog(ReturnCodeCatalogDTO returnCodeCatalogDTO) {
    log.debug("Request to insertCfgReturnCodeCatalog : {}", returnCodeCatalogDTO);
    return cfgReturnCodeCatalogRepository.insertCfgReturnCodeCatalog(returnCodeCatalogDTO);
  }

  @Override
  public String deleteCfgReturnCodeCatalogById(Long id) {
    log.debug("Request to deleteCfgReturnCodeCatalogById : {}", id);
    return cfgReturnCodeCatalogRepository.deleteCfgReturnCodeCatalogById(id);
  }

  @Override
  public String updateCfgReturnCodeCatalog(ReturnCodeCatalogDTO dto) {
    log.debug("Request to updateCfgReturnCodeCatalog : {}", dto);
    return cfgReturnCodeCatalogRepository.updateCfgReturnCodeCatalog(dto);
  }

  @Override
  public Datatable getListReturnCodeCatalog(ReturnCodeCatalogDTO dto) {
    log.debug("Request to getListReturnCodeCatalog : {}", dto);
    return cfgReturnCodeCatalogRepository.getListReturnCodeCatalog(dto);
  }

  @Override
  public List<ReturnCodeCatalogDTO> getListReturnCategory() {
    log.debug("Request to getListReturnCategory");
    return cfgReturnCodeCatalogRepository.getListReturnCategory();
  }
}
