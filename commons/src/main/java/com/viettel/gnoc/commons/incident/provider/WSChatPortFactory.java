/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.incident.provider;

/**
 * @author ITSOL
 */

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.viettel.smartoffice.SmartOfficeService;

@Service
public class WSChatPortFactory {

  @Value("${application.ws.WSPOOL.maxTotal:100}")
  private String maxTotal;
  private WsGenericObjectPool wsFactory;
  @Autowired
  @Qualifier("wsPoolableObjectFactoryChat")
  WsPoolableObjectFactory<SmartOfficeService> wsPoolableObjectFactoryChat;


  @PostConstruct
  public void init() {
    if (wsFactory == null) {
      int maxActive = 20;
      if (maxTotal != null && !maxTotal.isEmpty()) {
        maxActive = Integer.parseInt(maxTotal);
      }
      wsPoolableObjectFactoryChat.setServiceName("CHAT");
      wsFactory = new WsGenericObjectPool<SmartOfficeService>(wsPoolableObjectFactoryChat,
          maxActive);
    }
  }

  public WsGenericObjectPool getWsFactory() {
    return wsFactory;
  }
}
