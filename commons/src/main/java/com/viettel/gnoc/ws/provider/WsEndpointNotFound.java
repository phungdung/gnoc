package com.viettel.gnoc.ws.provider;

/**
 * Created by thiendn1 on 4/3/2015.
 */
public class WsEndpointNotFound extends Exception {

  private String wsName;

  public WsEndpointNotFound(String wsName) {
    this.wsName = wsName;
  }

  @Override
  public String toString() {
    return "Chua dinh nghia WS: " + wsName + " (Can dinh nghia ws nay trong file ws-endpoint.xml)";
  }
}
