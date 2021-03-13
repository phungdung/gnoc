package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CfgWebServiceSQLDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgWebServiceSQLRepository {

  ResultInSideDto insertOrUpdate(CfgWebServiceSQLDTO cfgWebServiceSQLDTO);

  CfgWebServiceSQLDTO getDetail(Long id);

  ResultInSideDto deleteById(Long id);

  Datatable onSearch(CfgWebServiceSQLDTO cfgWebServiceSQLDTO);
}
