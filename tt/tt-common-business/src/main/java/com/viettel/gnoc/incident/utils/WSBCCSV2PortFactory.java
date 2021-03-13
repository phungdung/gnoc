/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.incident.utils;

/**
 * @author hungnd40
 */

import com.viettel.bccs2.SpmServiceImpl;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class WSBCCSV2PortFactory {

  private WsGenericObjectPool wsFactory;

  @Value("${application.ws.service_url_bccs2:null}")
  private String url;

  @Value("${application.ws.WSPOOL.maxTotal:20}")
  private String maxTotal;

  @Autowired
  WsPoolableObjectFactory wsPoolableObjectFactory;

  @PostConstruct
  public void init() {
    if (wsFactory == null) {
      int maxActive = 20;
      if (maxTotal != null && !maxTotal.isEmpty()) {
        maxActive = Integer.parseInt(maxTotal);
      }
      wsPoolableObjectFactory.setServiceName("CCV2###" + url);
      wsFactory = new WsGenericObjectPool<SpmServiceImpl>(wsPoolableObjectFactory, maxActive);
    }
  }
}
