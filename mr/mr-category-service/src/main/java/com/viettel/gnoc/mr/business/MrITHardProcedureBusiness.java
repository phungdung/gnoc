package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureITHardDTO;
import java.io.File;
import org.springframework.web.multipart.MultipartFile;

public interface MrITHardProcedureBusiness {

  Datatable getListMrHardITProcedure(MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO);

  MrCfgProcedureITHardDTO getDetail(Long procedureId);

  ResultInSideDto insertMrCfgProcedureITHard(MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO);

  ResultInSideDto updateMrCfgProcedureITHard(MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO);

  ResultInSideDto deleteMrCfgProcedureITHard(Long procedureId);

  ResultInSideDto importData(MultipartFile uploadFile) throws Exception;

  File exportData(MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO) throws Exception;

  File getTemplate();
}
