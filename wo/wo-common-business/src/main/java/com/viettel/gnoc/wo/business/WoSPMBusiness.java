package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.wo.dto.Wo;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import java.util.List;

public interface WoSPMBusiness {

  ResultDTO updateHelpFromSPM(String woCode, String description, Long result);

  List<WoDTO> getListWoForAccount(List<String> lstAccount);

  List<WoDTOSearch> getListWoByWoType(String woTypeCode, String createTimeFrom,
      String createTimeTo);

  ResultDTO updateDescriptionWoSPM(WoDTO woDTO);

  ResultDTO closeWoForSPM(List<Wo> lstWo, String system, String user, Long reasonLevel3Id);
}
