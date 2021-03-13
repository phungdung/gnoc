package com.viettel.gnoc.wo.utils;

import com.viettel.spm.analys.webservice.AnalysProcessService;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class SPM_ANALYS_PortFactory {

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
      wsPoolableObjectFactory.setServiceName("SPM_ANALYS");
      wsFactory = new WsGenericObjectPool<AnalysProcessService>(wsPoolableObjectFactory, maxActive);
    }
  }

}
