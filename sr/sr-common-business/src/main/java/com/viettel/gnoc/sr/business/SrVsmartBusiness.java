package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserDTO;
import com.viettel.gnoc.sr.dto.UnitSRCatalogDTO;
import java.util.List;

public interface SrVsmartBusiness {

  SRDTO getDetailSRForVSmart(String srId, String loginUser);

  ResultDTO updateSRForVSmart(SRDTO srInputDTO);

  List<SRConfigDTO> getListSRStatusForVSmart(String userName);

  List<SRDTO> getListSRForVSmart(SRDTO dto);

  List<SRRoleUserDTO> getListSRUserForVSmart(String serviceCode, Long unitId, String roleCode,
      String country);

  List<SRCatalogDTO> getListSRCatalogForVSmart(String userName, String serviceGroup);

  List<SRConfigDTO> getSRReviewForVSmart(String userName);

  List<SRRoleUserDTO> getListRoleUserForVsmart(SRRoleUserDTO srRoleUserDTO);

  List<UnitSRCatalogDTO> getListUnitSRCatalogForVsmart(SRCatalogDTO dto);

  List<SRConfigDTO> getListServiceGrouprForVsmart();
}
