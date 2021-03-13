package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.UnitSRCatalogDTO;
import java.util.List;

public interface SRCatalogRepository2 {

  SRCatalogDTO findById(Long serviceId);

  List<UnitSRCatalogDTO> getListUnitSRCatalog(SRCatalogDTO dto);

  List<SRCatalogDTO> getListSRCatalogDTO(SRCatalogDTO dto);

  List<SRCatalogDTO> getListCatalogWithRoleAndUnit(String type);

  List<SRCatalogDTO> getListCatalog(SRCatalogDTO dto);
}
