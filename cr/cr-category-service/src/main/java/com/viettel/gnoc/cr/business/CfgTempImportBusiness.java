package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.MethodParameterDTO;
import com.viettel.gnoc.cr.dto.TempImportDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CfgTempImportBusiness {

  Datatable getListTempImport(TempImportDTO tempImportDTO);

  TempImportDTO findTempImportById(Long tempImportId);

  ResultInSideDto insertTempImport(List<MultipartFile> files, TempImportDTO tempImportDTO)
      throws IOException;

  ResultInSideDto updateTempImport(List<MultipartFile> files, TempImportDTO tempImportDTO)
      throws IOException;

  String deleteTempImportById(Long tempImportId);

  List<MethodParameterDTO> getMethodPrameter();

  File exportData(TempImportDTO tempImportDTO) throws Exception;
}
