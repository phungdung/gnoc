package com.viettel.gnoc.wo.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Getter
@Setter
public class Gnoc1_WoPortFactory {

  private WsGenericObjectPool wsFactory = null;
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
      wsPoolableObjectFactory.setServiceName("GNOC1_WO");
      wsFactory = new WsGenericObjectPool<com.viettel.gnoc1.service.WoServices>(wsPoolableObjectFactory, maxActive);
    }
  }

}
