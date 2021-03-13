/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.utils;

import com.viettel.vmsa.WSForGNOCImplService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WS_SAP_PortFactory {

  public WsGenericObjectPool wsFactory = null;

  @Value("${application.ws.WSPOOL.maxTotal:null}")
  String maxTotal;

  @Autowired
  WsPoolableObjectFactory wsPoolableObjectFactory;

  @PostConstruct
  public void init() {
    if (wsFactory == null) {
      int maxActive = 20;
      if (maxTotal != null && !maxTotal.isEmpty()) {
        maxActive = Integer.parseInt(maxTotal);
      }
      wsPoolableObjectFactory.setServiceName("WS_SAP");
      wsFactory = new WsGenericObjectPool<WSForGNOCImplService>(wsPoolableObjectFactory, maxActive);
    }
  }

  public WsGenericObjectPool getWSFactory() {
    return wsFactory;
  }

}
