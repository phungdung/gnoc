/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.ws.provider;

/**
 * @author thanhlv12
 */

import com.viettel.gate.webservice.UpdateECRForGNOCService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WSGatePortFactory {

  @Value("${application.ws.WSPOOL.maxTotal:null}")
  String maxTotal;

  @Autowired
  @Qualifier("WsCrPoolObjectFactory")
  WsPoolableObjectFactory wsPoolableObjectFactory;

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
      wsPoolableObjectFactory.setServiceName("GATE");
      wsFactory = new WsGenericObjectPool<UpdateECRForGNOCService>(wsPoolableObjectFactory,
          maxActive);
    }
  }

}
