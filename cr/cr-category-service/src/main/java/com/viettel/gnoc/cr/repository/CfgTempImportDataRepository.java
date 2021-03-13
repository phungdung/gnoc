package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.TempImportDataDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgTempImportDataRepository {

  TempImportDataDTO findCfgTempImportDataById(Long id);

  ResultInSideDto insertTempImportData(TempImportDataDTO tempImportDataDTO);

  String deleteTempImportDataById(Long id);

  String updateTempImportData(TempImportDataDTO tempImportDataDTO);

  Datatable getListTempImportData(TempImportDataDTO tempImportDataDTO);
}
