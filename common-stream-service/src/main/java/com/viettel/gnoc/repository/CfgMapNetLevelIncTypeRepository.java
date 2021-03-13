package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgMapNetLevelIncTypeRepository {

  List<CfgMapNetLevelIncTypeDTO> getListCfgMapNetLevelIncTypeDTO(
      CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  Datatable getListCfgMapNetLevelIncTypeDatatable(
      CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO);

  ResultInSideDto updateCfgMapNetLevelIncType(CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO);

  ResultInSideDto deleteCfgMapNetLevelIncType(Long id);

  CfgMapNetLevelIncTypeDTO findCfgMapNetLevelIncTypeById(Long id);

  ResultInSideDto insertCfgMapNetLevelIncType(CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO);

}
