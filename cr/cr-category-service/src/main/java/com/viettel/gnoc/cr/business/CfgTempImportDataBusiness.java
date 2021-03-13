package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.TempImportDataDTO;

public interface CfgTempImportDataBusiness {

  TempImportDataDTO findCfgTempImportDataById(Long id);

  ResultInSideDto insertTempImportData(TempImportDataDTO tempImportDataDTO);

  String deleteTempImportDataById(Long id);

  String updateTempImportData(TempImportDataDTO dto);

  Datatable getListTempImportData(TempImportDataDTO tempImportDataDTO);
}
