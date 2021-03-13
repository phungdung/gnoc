package com.viettel.gnoc.commons.incident.provider;

import com.viettel.nims.infra.webservice.InfraWS;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("WSNIMSInfraPortFactorys")
@Getter
@Setter
public class WSNIMSInfraPortFactory {

  private WsGenericObjectPool wsFactory;

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
      wsPoolableObjectFactory.setServiceName("NIMS_Infra");
      wsFactory = new WsGenericObjectPool<InfraWS>(wsPoolableObjectFactory, maxActive);
    }
  }
}

