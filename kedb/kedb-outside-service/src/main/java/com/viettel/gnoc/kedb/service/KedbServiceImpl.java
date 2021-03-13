package com.viettel.gnoc.kedb.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.kedb.business.KedbBusiness;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author TrungDuong
 */
@Service
@Slf4j
public class KedbServiceImpl implements KedbService {

  @Autowired
  KedbBusiness kedbBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public List<com.viettel.gnoc.ws.dto.KedbDTO> synchKedbByCreateTime(String fromDate,
      String toDate) {
    log.debug("Request to synchKedbByCreateTime : {}", fromDate, toDate);
    I18n.setLocaleForService(wsContext);
    return kedbBusiness.synchKedbByCreateTime2(fromDate, toDate);
  }
}
