/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.commons.incident.provider;

import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * @author ITSOL
 */
public class WsGenericObjectPool<T> extends GenericObjectPool<T> {

  public WsGenericObjectPool(WsPoolableObjectFactory<T> factory) {
    super(factory);
    this.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_GROW);
  }

  public WsGenericObjectPool(WsPoolableObjectFactory<T> factory, int maxActive) {
    super(factory, maxActive);
    this.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_GROW);
  }

  public WsGenericObjectPool(WsPoolableObjectFactory<T> factory, int maxActive,
      byte whenExhaustedAction, long maxWait, int maxIdle) {
    super(factory, maxActive, whenExhaustedAction, maxWait, maxIdle);
  }

  @Override
  public T borrowObject() throws Exception {
    T t = super.borrowObject();
    return t;
  }
}
