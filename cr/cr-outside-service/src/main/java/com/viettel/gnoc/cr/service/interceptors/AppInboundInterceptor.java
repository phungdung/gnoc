package com.viettel.gnoc.cr.service.interceptors;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.InInterceptors;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.message.Message;
import org.springframework.context.i18n.LocaleContextHolder;

@Slf4j
@InInterceptors(interceptors = "InInterceptors")
public class AppInboundInterceptor extends LoggingInInterceptor {

  @Override
  public void handleMessage(Message message) throws Fault {
    processPayLoad(message);
    String locale = "";
    try {
      Map<String, List<String>> protocolHeader = (TreeMap) message.get(Message.PROTOCOL_HEADERS);
      if (protocolHeader != null && protocolHeader.get("locale") != null) {
        locale = protocolHeader.get("locale").get(0);
        if ("en".equalsIgnoreCase(locale) || "en_us".equalsIgnoreCase(locale)) {
          LocaleContextHolder.setLocale(new Locale("en", "US"));
        } else if ("vi".equalsIgnoreCase(locale) || "vi_vn"
            .equalsIgnoreCase(locale)) {
          LocaleContextHolder.setLocale(new Locale("vi", "VN"));
        } else if ("lo".equalsIgnoreCase(locale) || "lo_la"
            .equalsIgnoreCase(locale)) {
          LocaleContextHolder.setLocale(new Locale("lo", "LA"));
        }

      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      locale = "en_US";
    }
    super.handleMessage(message);
  }

  private void processPayLoad(Message messsage) {
    log.info("*** PROCESSING PAYLOAD AT IN-INTERCEPTOR ***");

  }

  @Override
  public void handleFault(Message message) {
    super.handleFault(message);
  }
}
