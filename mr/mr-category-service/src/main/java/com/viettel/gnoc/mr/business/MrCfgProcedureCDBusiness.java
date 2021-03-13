package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureCDDTO;
import java.io.File;
import java.util.List;

public interface MrCfgProcedureCDBusiness {

  public Datatable onSearch(MrCfgProcedureCDDTO mrCfgProcedureCDDTO);

  File exportSearchData(MrCfgProcedureCDDTO mrCfgProcedureCDDTO) throws Exception;

  List<CatItemDTO> getMrSubCategory();

  List<CatItemDTO> getMrPriority();

  List<CatItemDTO> getMrImpact();

  ResultInSideDto onInsertOrUpdate(MrCfgProcedureCDDTO mrCfgProcedureCDDTO);

  MrCfgProcedureCDDTO findById(Long id);

  ResultInSideDto deleteById(Long id);

  List<CatItemDTO> getOgListWorks(Long parentItemId);
}
