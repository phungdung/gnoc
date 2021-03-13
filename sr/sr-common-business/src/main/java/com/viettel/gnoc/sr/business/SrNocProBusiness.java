package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import java.util.List;

public interface SrNocProBusiness {

  List<SRCatalogDTO> getListServiceArraySR(String countryId);

  List<SRCatalogDTO> getListServiceGroupSR(String countryId);

  List<SRCatalogDTO> getListServiceSR(String countryId);

  List<SRCatalogDTO> getListUnitServiceSR(String countryId);

  List<SRCatalogDTO> getListRoleServiceSR(String countryId);

  ResultDTO getListStatusSR(List<String> lstSrCode, List<String> lstStatus, String fromDate,
      String toDate);

  ResultDTO createSRForNoc(SRDTO srDTO);
}
