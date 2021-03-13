package com.viettel.gnoc.kedb.service.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.interceptor.OutInterceptors;
import org.apache.cxf.message.Message;

@Slf4j
@OutInterceptors(interceptors = "OutInterceptors")
public class AppOutboundInterceptor extends LoggingOutInterceptor {

  @Override
  public void handleMessage(Message message) throws Fault {
    processPayLoad(message);
    super.handleMessage(message);
  }

  private void processPayLoad(Message messsage) {
    log.info("*** PROCESSING PAYLOAD AT OUT-INTERCEPTOR ***");

  }
}
