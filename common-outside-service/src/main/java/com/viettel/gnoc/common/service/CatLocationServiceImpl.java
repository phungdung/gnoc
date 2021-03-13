package com.viettel.gnoc.common.service;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.MrLocationDTO;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tripm
 * @version 2.0
 * @since 23/06/2020 15:00:00
 */
@Service
@Slf4j
public class CatLocationServiceImpl implements CatLocationService {

  @Autowired
  CatLocationBusiness catLocationBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public List<MrLocationDTO> getListLocationByStationCode(String stationCode) {
    log.info("Request to getListLocationByStationCode : {}", stationCode);
    I18n.setLocaleForService(wsContext);
    return catLocationBusiness.getListLocationByStationCode(stationCode);
  }
}
