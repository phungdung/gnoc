package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCfgCrUnitTelDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrCfgCrUnitTelRepository {

  Datatable getListDataMrCfgCrUnitTelSearchWeb(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO);

  ResultInSideDto insertMrCfgCrUnitTel(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO);

  ResultInSideDto updateMrCfgCrUnitTel(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO);

  MrCfgCrUnitTelDTO findMrCfgCrUnitTelById(Long cfgId);

  ResultInSideDto deleteMrCfgCrUnitTel(Long cfgId);

  List<MrCfgCrUnitTelDTO> getListMrCfgCrUnitTelExport(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO);

  MrCfgCrUnitTelDTO checkMrCfgCrUnitTelExit(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO);

  List<MrCfgCrUnitTelDTO> getListMrCfgCrUnitTel(MrCfgCrUnitTelDTO mrCfgCrUnitTelDTO);

  MrCfgCrUnitTelDTO findMrCfgCrUnitTelSoftWeb(Long cfgId);
}
