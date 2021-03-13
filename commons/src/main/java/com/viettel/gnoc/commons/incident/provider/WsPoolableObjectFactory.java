/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.commons.incident.provider;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author ITSOL
 */

@Service("wsPoolableObjectFactoryChat")
@Scope(value = "prototype")
public class WsPoolableObjectFactory<T> extends BasePoolableObjectFactory<T> {

  @Autowired
  @Qualifier("wsInterfaceChat")
  WsInterface wsInterfaceChat;

  private String serviceName;

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  /*public WsPoolableObjectFactory(String serviceName) {
    this.serviceName = serviceName;
  }*/

  @Override
  public T makeObject() throws Exception {
    T t = (T) wsInterfaceChat.createPort(serviceName);
    return t;
  }

  @Override
  public boolean validateObject(T obj) {
    return super.validateObject(obj);
  }

  @Override
  public void destroyObject(T obj) throws Exception {
  }

  @Override
  public void activateObject(T obj) throws Exception {
  }

  @Override
  public void passivateObject(T obj) throws Exception {
  }
}
