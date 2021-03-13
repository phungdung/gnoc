package com.viettel.gnoc.commons.incident.provider;

import com.viettel.gnoc.iim.webservice.IimServices;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class WSIIMPortFactory {

  public WsGenericObjectPool wsFactory;

  @Value("${application.ws.WSPOOL.maxTotal:20}")
  private String maxTotal;

  @Autowired
  @Qualifier("wsPoolableObjectFactoryChat")
  WsPoolableObjectFactory wsPoolableObjectFactory;

  @PostConstruct
  public void init() {
    if (wsFactory == null) {
      int maxActive = 20;
      if (maxTotal != null && !maxTotal.isEmpty()) {
        maxActive = Integer.parseInt(maxTotal);
      }
      wsPoolableObjectFactory.setServiceName("IIM");
      wsFactory = new WsGenericObjectPool<IimServices>(wsPoolableObjectFactory, maxActive);
    }
  }
}
