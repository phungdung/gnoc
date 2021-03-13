package com.viettel.gnoc.wo.utils;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Setter
@Getter
@Scope(value = "prototype")
public class WsPoolableObjectFactory<T> extends BasePoolableObjectFactory<T> {

  private String serviceName;
  private String wsLink;
  private String nation;

  @Autowired
  WsInterface wsInterface;

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public void setNation(String nation) {
    this.nation = nation;
  }

  @Override
  public T makeObject() throws Exception {
    T t;
    if (!"BCCS_CC_GLOBAL".equals(serviceName) && !"IMInventory_Global".equals(serviceName)) {
      t = (T) wsInterface.createPort(serviceName, wsLink);
    } else {
      t = (T) wsInterface.createPort(serviceName, wsLink, nation);
    }
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
