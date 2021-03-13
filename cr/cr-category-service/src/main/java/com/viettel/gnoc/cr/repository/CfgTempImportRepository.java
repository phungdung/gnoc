package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.MethodParameterDTO;
import com.viettel.gnoc.cr.dto.TempImportDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgTempImportRepository {

  Datatable getListTempImport(TempImportDTO tempImportDTO);

  TempImportDTO findTempImportById(Long id);

  ResultInSideDto insertTempImport(TempImportDTO tempImportDTO);

  ResultInSideDto updateTempImport(TempImportDTO tempImportDTO);

  String deleteTempImportById(Long id);

  List<MethodParameterDTO> getMethodPrameter();

  Datatable getListDataExport(TempImportDTO tempImportDTO);

  List<TempImportDTO> getAllListTempImport(TempImportDTO tempImportDTO);

  TempImportDTO findTempImportDtoById(Long id);
}
