package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.cr.business.CrGeneralBusiness;
import com.viettel.gnoc.cr.dto.ObjResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CrGeneralServiceImpl implements CrGeneralService {

  @Autowired
  CrGeneralBusiness crGeneralBusiness;

  // anhlp add
  @Override
  public ObjResponse doLogin(String versionApp, String locale, String userName, String password) {
    log.debug("Request to doLogin : {}", versionApp, locale, userName, password);
    return crGeneralBusiness.doLogin(versionApp, locale, userName, password);
  }

  // anhlp add
  @Override
  public ObjResponse doLoginV2(String versionApp, String locale, String userName, String password) {
    log.debug("Request to doLoginV2 : {}", versionApp, locale, userName, password);
    return crGeneralBusiness.doLoginV2(versionApp, locale, userName, password);
  }
}
