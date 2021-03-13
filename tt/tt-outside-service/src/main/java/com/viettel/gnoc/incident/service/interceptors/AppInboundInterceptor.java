package com.viettel.gnoc.incident.service.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.InInterceptors;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.message.Message;

@Slf4j
@InInterceptors(interceptors = "InInterceptors")
public class AppInboundInterceptor extends LoggingInInterceptor {

  @Override
  public void handleMessage(Message message) throws Fault {
    processPayLoad(message);
    super.handleMessage(message);
  }

  private void processPayLoad(Message messsage) {
    log.info("*** PROCESSING PAYLOAD AT IN-INTERCEPTOR ***");

  }
}
