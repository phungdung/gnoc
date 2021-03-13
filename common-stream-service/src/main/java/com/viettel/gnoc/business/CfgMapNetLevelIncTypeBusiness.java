package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
import java.util.List;
import java.util.Map;

public interface CfgMapNetLevelIncTypeBusiness {

  List<CfgMapNetLevelIncTypeDTO> getListCfgMapNetLevelIncTypeDTO(
      CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO);

  Datatable getListCfgMapNetLevelIncTypeDatatable(
      CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO);

  ResultInSideDto updateCfgMapNetLevelIncType(CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO);

  ResultInSideDto deleteCfgMapNetLevelIncType(Long id);

  CfgMapNetLevelIncTypeDTO findCfgMapNetLevelIncTypeById(Long id);

  ResultInSideDto insertCfgMapNetLevelIncType(CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO);

  Map<?, ?> netWorkRiskList();

  Map<?, ?> netWorkLevelList();
}
