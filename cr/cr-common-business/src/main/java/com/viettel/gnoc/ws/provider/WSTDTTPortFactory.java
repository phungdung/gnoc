/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.ws.provider;

import com.viettel.aam.TdttWebserviceImplService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WSTDTTPortFactory {

  @Autowired
  @Qualifier("WsCrPoolObjectFactory")
  WsPoolableObjectFactory wsPoolableObjectFactory;

  @Value("${application.ws.WSPOOL.maxTotal:null}")
  String maxTotal;

  public WsGenericObjectPool wsFactory = null;

  public WsGenericObjectPool getWsFactory() {
    return wsFactory;
  }

  @PostConstruct
  public void init() {
    if (wsFactory == null) {
      int maxActive = 20;
      if (maxTotal != null && !maxTotal.isEmpty()) {
        maxActive = Integer.parseInt(maxTotal);
      }
      wsPoolableObjectFactory.setServiceName("TDTT");
      wsFactory = new WsGenericObjectPool<TdttWebserviceImplService>(wsPoolableObjectFactory,
          maxActive);
    }
  }
}

