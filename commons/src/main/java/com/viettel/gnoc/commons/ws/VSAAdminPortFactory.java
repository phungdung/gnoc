/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.ws;

/**
 * @author ITSOL
 */

import com.viettel.vsaadmin.service.VsaadminService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VSAAdminPortFactory {

  @Value("${application.ws.WSPOOL.maxTotal:100}")
  private String maxTotal;
  private WsGenericObjectCommonPool wsFactory;
  @Autowired
  @Qualifier("wsCommonPoolObjectFactory")
  WsPoolableCommonFactory wsCommonPoolObjectFactory;

  @PostConstruct
  public void init() {
    if (wsFactory == null) {
      int maxActive = 20;
      if (maxTotal != null && !maxTotal.isEmpty()) {
        maxActive = Integer.parseInt(maxTotal);
      }
      wsCommonPoolObjectFactory.setServiceName("WS_VSA");
      wsFactory = new WsGenericObjectCommonPool<VsaadminService>(wsCommonPoolObjectFactory,
          maxActive);
    }
  }

  public WsGenericObjectCommonPool getWsFactory() {
    return wsFactory;
  }
}
