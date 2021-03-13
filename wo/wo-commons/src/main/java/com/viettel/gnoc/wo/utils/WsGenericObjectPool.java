package com.viettel.gnoc.wo.utils;

import org.apache.commons.pool.impl.GenericObjectPool;

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
