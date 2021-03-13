package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface TroubleBRCDRepository {

  TroublesInSideDTO getInfoBRCDByTroubleId(Long troubleId);


  ResultInSideDto getInsertOrUpdateInfoBRCD(TroublesInSideDTO troublesDTO);
}
