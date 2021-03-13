/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.ws.provider;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author KhiemVK
 * @version 1.0
 * @since: 11/12/13 10:28 AM
 */

@Service("WsCrPoolObjectFactory")
@Scope(value = "prototype")
public class WsPoolableObjectFactory<T> extends BasePoolableObjectFactory<T> {

  private String serviceName;

  @Autowired
  @Qualifier("wSCrInterface")
  WsInterface wsInterface;

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

//    public WsPoolableObjectFactory(String serviceName) {
//        this.serviceName = serviceName;
//    }

  @Override
  public T makeObject() throws Exception {
    T t = (T) wsInterface.createPort(serviceName);
    return t;
  }

  @Override
  public boolean validateObject(T obj) {
    return super.validateObject(obj);
  }
}
