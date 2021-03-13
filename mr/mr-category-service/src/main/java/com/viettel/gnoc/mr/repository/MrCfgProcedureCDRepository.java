package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureCDDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrCfgProcedureCDRepository {

  Datatable onSearch(MrCfgProcedureCDDTO mrCfgProcedureCDDTO);

  List<MrCfgProcedureCDDTO> onSearchExport(MrCfgProcedureCDDTO mrCfgProcedureCDDTO);

  ResultInSideDto onInsertOrUpdate(MrCfgProcedureCDDTO mrCfgProcedureCDDTO);

  MrCfgProcedureCDDTO findById(Long id);

  ResultInSideDto deleteById(Long id);
}
