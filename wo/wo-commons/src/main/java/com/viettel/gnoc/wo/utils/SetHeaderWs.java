package com.viettel.gnoc.wo.utils;

import com.viettel.gnoc.wo.dto.HeaderForm;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

public class SetHeaderWs {

  public static void setHeaderHandler(Object port, List<HeaderForm> lstHeader) {
    Binding binding = ((BindingProvider) port).getBinding();
    List<Handler> handlerChain = new ArrayList<Handler>();
    handlerChain.add(new WsHandler(lstHeader));
    binding.setHandlerChain(handlerChain);
  }

}
