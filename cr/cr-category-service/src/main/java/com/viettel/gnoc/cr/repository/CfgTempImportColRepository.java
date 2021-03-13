package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.TempImportColDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgTempImportColRepository {

  TempImportColDTO findTempImportColById(Long id);

  List<TempImportColDTO> findTempImportColByTempImportId(Long tempImportId);

  ResultInSideDto insertTempImportCol(TempImportColDTO tempImportColDTO);

  String deleteTempImportColById(Long id);

  String updateTempImportCol(TempImportColDTO dto);

  Datatable getListTempImportCol(TempImportColDTO dto);

  String deleteListTempImportColByTempImportId(List<TempImportColDTO> tempImportColDTOS);
}
