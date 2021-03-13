/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.ws.provider;

import com.viettel.vipa.WSForGNOCImplService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WSVipaIpPortFactory {

  public WsGenericObjectPool wsFactory = null;

  @Autowired
  @Qualifier("WsCrPoolObjectFactory")
  WsPoolableObjectFactory wsPoolableObjectFactory;

  @Value("${application.ws.WSPOOL.maxTotal:null}")
  String maxTotal;

  @PostConstruct
  public void init() {
    if (wsFactory == null) {
      int maxActive = 20;
      if (maxTotal != null && !maxTotal.isEmpty()) {
        maxActive = Integer.parseInt(maxTotal);
      }
      wsPoolableObjectFactory.setServiceName("VIPAIP");
      wsFactory = new WsGenericObjectPool<WSForGNOCImplService>(wsPoolableObjectFactory, maxActive);
    }
  }

  public WsGenericObjectPool getWSFactory() {
    return wsFactory;
  }

}
