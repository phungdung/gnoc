package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.CfgWebServiceSQLDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CfgWebServiceSQLRepository;
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
public class CfgWebServiceSQLBusinessImpl implements CfgWebServiceSQLBusiness {

  @Autowired
  protected CfgWebServiceSQLRepository cfgWebServiceSQLRepository;

  @Override
  public ResultInSideDto insertOrUpdate(CfgWebServiceSQLDTO cfgWebServiceSQLDTO) {
    return cfgWebServiceSQLRepository.insertOrUpdate(cfgWebServiceSQLDTO);
  }

  @Override
  public CfgWebServiceSQLDTO getDetail(Long id) {
    return cfgWebServiceSQLRepository.getDetail(id);
  }

  @Override
  public ResultInSideDto deleteById(Long id) {
    return cfgWebServiceSQLRepository.deleteById(id);
  }

  @Override
  public Datatable onSearch(CfgWebServiceSQLDTO cfgWebServiceSQLDTO) {
    return cfgWebServiceSQLRepository.onSearch(cfgWebServiceSQLDTO);
  }
}
