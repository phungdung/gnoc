package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import java.util.List;

public interface SrAddOnBusiness {

  ResultDTO createSRByConfigGroup(SRDTO srInputDTO, String configGroup);

  List<SRDTO> getListSRByConfigGroup(SRDTO dto, String configGroup);

  List<SRCatalogDTO> getListSRCatalogByConfigGroup(String configGroup);
}
