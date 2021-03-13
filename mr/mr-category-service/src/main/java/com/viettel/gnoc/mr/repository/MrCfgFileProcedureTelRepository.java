package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCfgFileProcedureTelDTO;
import java.util.List;

public interface MrCfgFileProcedureTelRepository {

  ResultInSideDto insertOrUpdate(MrCfgFileProcedureTelDTO mrCfgFileProcedureTelDTO);

  ResultInSideDto delete(Long mrCfgFileId);

  MrCfgFileProcedureTelDTO getDetail(Long mrCfgFileId);

  List<MrCfgFileProcedureTelDTO> onSearchEntity(MrCfgFileProcedureTelDTO mrCfgFileProcedureTelDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<GnocFileDto> getCfgProcedureFileDetail(Long cfgProcedureId, String system);

  int deleteByCfgProcedureId(Long cfgProcedureId);
}
