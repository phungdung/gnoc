package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.wo.dto.CfgWoHelpVsmartDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CfgWoHelpVsmartBusiness {

  ResultInSideDto insertCfgWoHelpVsmart(List<MultipartFile> files,
      CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO) throws Exception;

  ResultInSideDto updateCfgWoHelpVsmart(List<MultipartFile> files,
      CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO) throws Exception;

  CfgWoHelpVsmartDTO findCfgWoHelpVsmartsById(Long id);

  ResultInSideDto deleteCfgWoHelpVsmart(Long id);

  ResultInSideDto deleteListCfgWoHelpVsmart(List<CfgWoHelpVsmartDTO> cfgWoHelpVsmartDTOS);

  Datatable getListCfgWoHelpVsmartDTOSearchWeb(CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO);

  File getTemplate(String systemId) throws Exception;

  List<CatItemDTO> getListCbbSystem();

  List<ObjKeyValueVsmartDTO> getDataHeader(Long systemId, String typeId);
}
