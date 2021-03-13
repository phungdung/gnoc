package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCfgFileProcedureDTO;
import java.util.List;

public interface MrITSoftFileProcedureRepository {

  ResultInSideDto insertOrUpdateFiles(MrCfgFileProcedureDTO mrCfgFileProcedureDTO);

  int deleteByCfgProcedureId(Long cfgProcedureId);

  List<GnocFileDto> getCfgProcedureFileDetail(Long cfgProcedureId, String system);

  ResultInSideDto delete(Long mrCfgFileId);

}
