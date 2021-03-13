/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.wo.utils;

import com.viettel.ipcc.unitel.services.CallServicePortType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * @author TamdX
 */
@Getter
@Setter
@Service
public class IPCCSTLPortFactory {

  @Value("${application.ws.WSPOOL.maxTotal:100}")
  private String maxTotal;

  @Autowired
  WsPoolableObjectFactory wsPoolableObjectFactory;

  public WsGenericObjectPool init(String wsLink) {
    int maxActive = 20;
    if (maxTotal != null && !maxTotal.isEmpty()) {
      maxActive = Integer.parseInt(maxTotal);
    }
    wsPoolableObjectFactory.setServiceName("IPCC_STL");
    wsPoolableObjectFactory.setWsLink(wsLink);
    return new WsGenericObjectPool<CallServicePortType>(wsPoolableObjectFactory, maxActive);
  }
}
