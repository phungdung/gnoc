package com.viettel.gnoc.business;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface CfgMapNetworkLevelIncBusiness {

  List<CfgMapNetLevelIncTypeDTO> getListCfgMapNetLevelIncTypeDTO(
      @RequestBody CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO);

  Datatable getListCfgMapNetLevelIncTypeDatatable(
      @RequestBody CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO);

  ResultInSideDto updateCfgMapNetLevelIncType(
      @RequestBody @Valid CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO);

  ResultInSideDto deleteCfgMapNetLevelIncType(Long id);

  CfgMapNetLevelIncTypeDTO findCfgMapNetLevelIncTypeById(Long id);

  ResultInSideDto insertCfgMapNetLevelIncType(
      @RequestBody @Valid CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO);

  Map<?, ?> netWorkRiskList();

  Map<?, ?> netWorkLevelList();
}
