package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrITSoftCrImplUnitDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrITSoftCrImplUnitRepository {

  Datatable getListDataMrCfgCrUnitITSoft(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO);

  ResultInSideDto insertOrUpdateMrCfgCrUnitIT(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO);

  MrITSoftCrImplUnitDTO findMrCfgCrUnitITById(Long cfgId);

  ResultInSideDto deleteMrCfgCrUnitIT(Long cfgId);

  List<MrITSoftCrImplUnitDTO> getDataExport(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO);

  MrITSoftCrImplUnitDTO checkMrCfgCrUnitITExit(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO);

}
