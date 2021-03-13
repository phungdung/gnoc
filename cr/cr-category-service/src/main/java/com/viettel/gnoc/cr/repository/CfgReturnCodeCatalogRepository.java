package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.ReturnCodeCatalogDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgReturnCodeCatalogRepository {

  ReturnCodeCatalogDTO findCfgReturnCodeCatalogById(Long id);

  ResultInSideDto insertCfgReturnCodeCatalog(ReturnCodeCatalogDTO returnCodeCatalogDTO);

  String deleteCfgReturnCodeCatalogById(Long id);

  String updateCfgReturnCodeCatalog(ReturnCodeCatalogDTO dto);

  Datatable getListReturnCodeCatalog(ReturnCodeCatalogDTO dto);

  List<ReturnCodeCatalogDTO> getListReturnCategory();
}
