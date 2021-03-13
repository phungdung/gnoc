package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.cr.dto.SessionResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface CrGeneralForMobileRepository {

  SessionResponse getSessionInfo(String sessionId);

  String getLocationByUnitId(String unitId);

  String checkSession(String sessionId);

}
