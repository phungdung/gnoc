package com.viettel.gnoc.wo.utils;

import com.viettel.bccs.cc.service.SpmServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class WSIMInventory_MYT_PortFactory {

  private WsGenericObjectPool wsFactory = null;
  @Value("${application.ws.WSPOOL.maxTotal:20}")
  private String maxTotal;

  @Autowired
  WsPoolableObjectFactory wsPoolableObjectFactory;

  public WsGenericObjectPool init(String wsLink, String nationCode) {
    int maxActive = 20;
    if (maxTotal != null && !maxTotal.isEmpty()) {
      maxActive = Integer.parseInt(maxTotal);
    }
    wsPoolableObjectFactory.setServiceName("IMInventory_Global");
    wsPoolableObjectFactory.setWsLink(wsLink);
    wsPoolableObjectFactory.setNation(nationCode);
    wsFactory = new WsGenericObjectPool<SpmServiceImpl>(wsPoolableObjectFactory, maxActive);
    return wsFactory;
  }

}
