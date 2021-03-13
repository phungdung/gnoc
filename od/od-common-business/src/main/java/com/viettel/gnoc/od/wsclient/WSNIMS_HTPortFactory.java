/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.od.wsclient;

import com.viettel.nims.webservice.ht.InfraWS;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author TamdX
 */
@Service
@Getter
@Setter
public class WSNIMS_HTPortFactory {

  private WsGenericObjectPool wsFactory;
  @Autowired
  WsPoolableObjectFactory<InfraWS> wsPoolableObjectFactory;
  @Value("${application.ws.WSPOOL.maxTotal}")
  private String maxTotal;

  @PostConstruct
  public void init() {
    if (wsFactory == null) {
      int maxActive = 20;
      if (maxTotal != null && !maxTotal.isEmpty()) {
        maxActive = Integer.parseInt(maxTotal);
      }
      wsPoolableObjectFactory.setServiceName("NIMS_HT");
      wsFactory = new WsGenericObjectPool<InfraWS>(wsPoolableObjectFactory, maxActive);
    }
  }

}
