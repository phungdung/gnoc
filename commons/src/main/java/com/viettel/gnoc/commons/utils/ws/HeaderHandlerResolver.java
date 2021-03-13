/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.utils.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

/**
 * Created Time : 11/05/2016 10:57:34 AM
 *
 * @author anhmv6
 */
public class HeaderHandlerResolver implements HandlerResolver {

  private String user;
  private String pass;

  public HeaderHandlerResolver(String user, String pass) {
    this.user = user;
    this.pass = pass;
  }

  public HeaderHandlerResolver() {
  }


  public List<Handler> getHandlerChain(PortInfo portInfo) {
    List<Handler> handlerChain = new ArrayList<Handler>();
    HeaderHandler hh = new HeaderHandler(user, pass);

    handlerChain.add(hh);

    return handlerChain;
  }
}

