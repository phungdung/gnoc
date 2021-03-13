package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.TempImportColDTO;

public interface CfgTempImportColBusiness {

  TempImportColDTO findTempImportColById(Long id);

  ResultInSideDto insertTempImportCol(TempImportColDTO tempImportColDTO);

  String deleteTempImportColById(Long id);

  String updateTempImportCol(TempImportColDTO dto);

  Datatable getListTempImportCol(TempImportColDTO tempImportColDTO);
}
