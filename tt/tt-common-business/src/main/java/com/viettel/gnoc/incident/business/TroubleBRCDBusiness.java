package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;

public interface TroubleBRCDBusiness {

  TroublesInSideDTO getInfoBRCDByTroubleId(Long troubleId);

  ResultInSideDto getInsertOrUpdateInfoBRCD(TroublesInSideDTO troublesDTO);
}
