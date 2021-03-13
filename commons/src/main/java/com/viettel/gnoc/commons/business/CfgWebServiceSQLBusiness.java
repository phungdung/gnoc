package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.CfgWebServiceSQLDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;

/**
 * @author TienNV
 */
public interface CfgWebServiceSQLBusiness {

  ResultInSideDto insertOrUpdate(CfgWebServiceSQLDTO cfgWebServiceSQLDTO);

  CfgWebServiceSQLDTO getDetail(Long id);

  ResultInSideDto deleteById(Long id);

  Datatable onSearch(CfgWebServiceSQLDTO cfgWebServiceSQLDTO);
}
