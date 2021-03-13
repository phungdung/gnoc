package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.cr.dto.SessionResponse;

public interface CrGeneralForMobileBusiness {

  SessionResponse getSessionInfo(String sessionId);

  String getLocationByUnitId(String unitId);

  String checkSession(String sessionId);
}

