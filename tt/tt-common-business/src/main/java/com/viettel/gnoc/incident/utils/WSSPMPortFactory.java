/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.incident.utils;

/**
 * @author thanhlv12
 */

import com.viettel.soc.spm.service.ServiceProblemService;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class WSSPMPortFactory {

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
      wsPoolableObjectFactory.setServiceName("SPM");
      wsFactory = new WsGenericObjectPool<ServiceProblemService>(wsPoolableObjectFactory,
          maxActive);
    }
  }
}
