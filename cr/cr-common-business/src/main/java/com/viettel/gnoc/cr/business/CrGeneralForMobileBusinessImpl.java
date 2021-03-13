package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.cr.dto.SessionResponse;
import com.viettel.gnoc.cr.repository.CrGeneralForMobileRepository;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class CrGeneralForMobileBusinessImpl implements CrGeneralForMobileBusiness {

  @Autowired
  CrGeneralForMobileRepository crGeneralForMobileRepository;

  @Override
  public SessionResponse getSessionInfo(String sessionId) {
    return crGeneralForMobileRepository.getSessionInfo(sessionId);
  }

  @Override
  public String getLocationByUnitId(String unitId) {
    return crGeneralForMobileRepository.getLocationByUnitId(unitId);
  }

  @Override
  public String checkSession(String sessionId) {
    return crGeneralForMobileRepository.checkSession(sessionId);
  }
}
