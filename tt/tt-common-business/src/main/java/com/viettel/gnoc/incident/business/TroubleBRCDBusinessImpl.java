package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.repository.TroubleBRCDRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class TroubleBRCDBusinessImpl implements TroubleBRCDBusiness {

  @Autowired
  TroubleBRCDRepository troubleBRCDRepository;

  @Override
  public TroublesInSideDTO getInfoBRCDByTroubleId(Long troubleId) {
    log.debug("Request to getInfoBRCDByTroubleId : {}", troubleId);
    return troubleBRCDRepository.getInfoBRCDByTroubleId(troubleId);
  }

  @Override
  public ResultInSideDto getInsertOrUpdateInfoBRCD(TroublesInSideDTO troublesDTO) {
    log.debug("Request to getInsertOrUpdateInfoBRCD : {}", troublesDTO);
    return troubleBRCDRepository.getInsertOrUpdateInfoBRCD(troublesDTO);
  }
}
