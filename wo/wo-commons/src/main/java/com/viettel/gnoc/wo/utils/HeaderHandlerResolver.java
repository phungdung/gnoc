package com.viettel.gnoc.wo.utils;

import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

public class HeaderHandlerResolver implements HandlerResolver {

  private String user;
  private String pass;
  private String system;

  public HeaderHandlerResolver(String user, String pass, String system) {
    this.user = user;
    this.pass = pass;
    this.system = system;
  }

  public HeaderHandlerResolver() {
  }

  @Override
  public List<Handler> getHandlerChain(PortInfo portInfo) {
    List<Handler> handlerChain = new ArrayList<Handler>();
    HeaderHandler hh = new HeaderHandler(user, pass, system);

    handlerChain.add(hh);

    return handlerChain;
  }
}
