package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureBtsDTO;
import java.io.File;
import org.springframework.web.multipart.MultipartFile;

public interface MrCfgProcedureBtsBusiness {

  Datatable onSearch(MrCfgProcedureBtsDTO mrCfgProcedureBtsDTO);

  File exportSearchData(MrCfgProcedureBtsDTO mrCfgProcedureBtsDTO) throws Exception;

  File getFileTemplate();

  ResultInSideDto importMrCfgProcedureBTS(MultipartFile fileImport) throws Exception;

  ResultInSideDto insertOrUpdateMrCfgProcedureBts(MrCfgProcedureBtsDTO dto);

  ResultInSideDto deleteById(Long id);

  MrCfgProcedureBtsDTO findById(Long id);
}
