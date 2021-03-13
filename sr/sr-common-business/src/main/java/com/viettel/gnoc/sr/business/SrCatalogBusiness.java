package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import java.util.List;

public interface SrCatalogBusiness {

  List<SRCatalogDTO> getListCatalog(SRCatalogDTO dto);

  SRCatalogDTO findByIdCatalog(String serviceId);

}
