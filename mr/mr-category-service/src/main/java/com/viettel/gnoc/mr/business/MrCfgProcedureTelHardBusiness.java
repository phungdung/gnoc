package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureTelDTO;
import java.io.File;
import org.springframework.web.multipart.MultipartFile;

public interface MrCfgProcedureTelHardBusiness {

  Datatable onSearch(MrCfgProcedureTelDTO mrCfgProcedureTelDTO);

  File exportSearchData(MrCfgProcedureTelDTO mrCfgProcedureTelDTO) throws Exception;

  ResultInSideDto insert(MrCfgProcedureTelDTO mrCfgProcedureTelDTO);

  ResultInSideDto update(MrCfgProcedureTelDTO mrCfgProcedureTelDTO);

  ResultInSideDto delete(Long procedureId);

  MrCfgProcedureTelDTO getDetail(Long procedureId);

  ResultInSideDto importMrCfgProcedureTelHard(MultipartFile fileImport) throws Exception;

  File getFileTemplate();

}
