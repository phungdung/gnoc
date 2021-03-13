/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.od.wsclient;


import lombok.Getter;
import lombok.Setter;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author KhiemVK
 * @version 1.0
 * @since: 11/12/13 10:28 AM
 */

@Service
@Getter
@Setter
public class WsPoolableObjectFactory<T> extends BasePoolableObjectFactory<T> {

  private String serviceName;
  private String wsLink;

  @Autowired
  WsInterface wsInterface;

//    public WsPoolableObjectFactory(String serviceName, String wsLink) {
//        this.serviceName = serviceName;
//        this.wsLink = wsLink;
//    }

  @Override
  public T makeObject() throws Exception {
    T t = (T) wsInterface.createPort(serviceName, wsLink);
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
