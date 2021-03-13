package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureBtsExportDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrCfgProcedureBtsRepository {

  Datatable onSearch(MrCfgProcedureBtsDTO mrCfgProcedureBtsDTO);

  List<MrCfgProcedureBtsExportDTO> onSearchExport(MrCfgProcedureBtsDTO mrCfgProcedureBtsDTO);

  String insertListMrCfgProcedureBts(List<MrCfgProcedureBtsDTO> lstData);

  List<MrCfgProcedureBtsDTO> checkDupp(MrCfgProcedureBtsDTO mrCfgProcedureBtsDTO);

  ResultInSideDto insertOrUpdateMrCfgProcedureBts(MrCfgProcedureBtsDTO dto);

  ResultInSideDto deleteById(Long id);

  MrCfgProcedureBtsDTO findById(Long id);
}
