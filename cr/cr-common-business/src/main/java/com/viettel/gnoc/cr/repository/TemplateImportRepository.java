package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.cr.dto.TempImportDTOResult;
import com.viettel.gnoc.cr.dto.TemplateImportDTO;
import com.viettel.gnoc.cr.dto.V_TempImportColDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateImportRepository {

  TemplateImportDTO validateTempImport(ExcelWriterUtils xls,
      TemplateImportDTO templateImportDTO1,
      String locale,
      String countryId);

  List<TempImportDTOResult> getTempImport(String tempImportId);

  List<V_TempImportColDTO> getTemplate(String tempImportId);

  void actionClearData(TemplateImportDTO templateImportDTO);

  void actionClearFileAttInput(TemplateImportDTO templateImportDTO);

  TemplateImportDTO insertIntoTempImport(ExcelWriterUtils xls, TemplateImportDTO templateImportDTO,
      String locale);

}
