package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import java.util.List;

public interface SrWOTickHelpBusiness {

  SRDTO getDetailSRForWOTHVSmart(String srId);

  List<SRDTO> getListSRForWOTHVSmart(SRDTO srDTO, String woId);

  ResultDTO createSRForWOTHVSmart(List<ObjKeyValueVsmartDTO> lstObjKeyValueVsmartDTO,
      String createUser, String serviceCode);
}
