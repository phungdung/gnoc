package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.ReturnCodeCatalogDTO;
import java.util.List;

public interface CfgReturnCodeCatalogBusiness {

  ReturnCodeCatalogDTO findCfgReturnCodeCatalogById(Long id);

  ResultInSideDto insertCfgReturnCodeCatalog(ReturnCodeCatalogDTO returnCodeCatalogDTO);

  String deleteCfgReturnCodeCatalogById(Long id);

  String updateCfgReturnCodeCatalog(ReturnCodeCatalogDTO dto);

  Datatable getListReturnCodeCatalog(ReturnCodeCatalogDTO returnCodeCatalogDTO);

  List<ReturnCodeCatalogDTO> getListReturnCategory();
}
