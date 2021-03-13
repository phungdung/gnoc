package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserDTO;
import java.util.List;

public interface SROutsideRepository {

  List<SRDTO> getListSRForGatePro(String fromDate, String toDate, SRDTO srDTO);

  List<SRDTO> getListSRForWOTHVSmart(SRDTO srDTO, String woId);

  List<SRDTO> getListSRByConfigGroup(SRDTO dto, String configGroup);

  List<SRCatalogDTO> getListSRCatalogByConfigGroup(String configGroup);

  SRDTO getDetailSRForVSmart(String srId, String loginUser);

  List<SRDTO> getListSRForVSmart(SRDTO dto);

  List<SRRoleUserDTO> getListSRUserForVSmart(String serviceCode, Long unitId, String roleCode,
      String country);

  List<SRRoleUserDTO> getListRoleUserForVsmart(SRRoleUserDTO srRoleUserDTO);

  List<SRCatalogDTO> getListSRCatalogByConfigGroupIBPMS(String configGroup);

  List<SRCatalogDTO> getListSRCatalogByConfigGroupVSMAT(String configGroup, String serviceGroup);

  List<SRConfigDTO> getListServiceGrouprForVsmart();
}
