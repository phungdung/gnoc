/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.od.wsclient;

/**
 * @author thanhlv12
 */

import com.viettel.voffice.ws_autosign.service.Vo2AutoSignSystemImpl;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Setter
@Getter
@Service
public class WS_VOFFICE_PortFactory {

  private WsGenericObjectPool wsFactory;
  @Value("${application.ws.WSPOOL.maxTotal}")
  private String maxTotal;

  @Autowired
  WsPoolableObjectFactory<Vo2AutoSignSystemImpl> wsPoolableObjectFactory;

  @PostConstruct
  public void init() {
    if (wsFactory == null) {
      int maxActive = 20;
      if (maxTotal != null && !maxTotal.isEmpty()) {
        maxActive = Integer.parseInt(maxTotal);
      }
      wsPoolableObjectFactory.setServiceName("VOFFICE");

      wsFactory = new WsGenericObjectPool<Vo2AutoSignSystemImpl>(wsPoolableObjectFactory,
          maxActive);
    }
  }
}

