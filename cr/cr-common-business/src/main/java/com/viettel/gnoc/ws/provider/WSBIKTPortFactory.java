/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.ws.provider;

import com.viettel.bikt.webservices.ServiceGNOC_Service;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WSBIKTPortFactory {

  public WsGenericObjectPool wsFactory = null;

  @Value("${application.ws.WSPOOL.maxTotal:null}")
  String maxTotal;

  @Autowired
  @Qualifier("WsCrPoolObjectFactory")
  WsPoolableObjectFactory wsPoolableObjectFactory;

  @PostConstruct
  public void init() {
    if (wsFactory == null) {
      int maxActive = 20;
      if (maxTotal != null && !maxTotal.isEmpty()) {
        maxActive = Integer.parseInt(maxTotal);
      }
      wsPoolableObjectFactory.setServiceName("BIKTWS");
      wsFactory = new WsGenericObjectPool<ServiceGNOC_Service>(wsPoolableObjectFactory, maxActive);
    }
  }

  public WsGenericObjectPool getWSFactory() {
    return wsFactory;
  }

}
