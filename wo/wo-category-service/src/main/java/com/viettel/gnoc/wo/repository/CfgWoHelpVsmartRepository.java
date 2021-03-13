package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.CfgWoHelpVsmartDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgWoHelpVsmartRepository {

  ResultInSideDto insertCfgWoHelpVsmart(CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO);

  ResultInSideDto updateCfgWoHelpVsmart(CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO);

  CfgWoHelpVsmartDTO findCfgWoHelpVsmartsById(Long id);

  ResultInSideDto deleteCfgWoHelpVsmart(Long id);

  ResultInSideDto deleteListCfgWoHelpVsmart(List<CfgWoHelpVsmartDTO> cfgWoHelpVsmartDTOS);

  Datatable getListCfgWoHelpVsmartDTOSearchWeb(CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO);

  List<CatItemDTO> getListCbbSystem();

  String findCfgWoHelpVsmartDTO(Long systemId, String typeId);

  String getSequenseCfgWoHelpVsmart(String sequense);
}
