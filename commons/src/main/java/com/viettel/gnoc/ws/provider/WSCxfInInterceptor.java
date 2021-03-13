/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.ws.provider;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import java.io.InputStream;
import java.io.Reader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.cxf.common.util.PropertyUtils;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.service.model.InterfaceInfo;

/**
 * @author dungnv50
 */
//public class WSCxfInInterceptor extends AbstractPhaseInterceptor<Message> {
public class WSCxfInInterceptor extends LoggingInInterceptor {

  private static final int PROPERTIES_SIZE = 128;
  private static final Map<String, String> mapLogWS = new HashMap();
  private static final Map<String, String> mapDateWS = new HashMap();
  static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
      .getLogger(WSCxfInInterceptor.class);
  private Boolean isDefaultLocale = true;

  public WSCxfInInterceptor() {
    super(Phase.RECEIVE);
  }

  @Override
  public void handleMessage(Message message) throws Fault {

    java.util.logging.Logger logger = getMessageLogger(message);
    if (logger != null && (writer != null || logger.isLoggable(Level.INFO))) {
      logging(logger, message);
    }
  }

  protected void logging(Logger logger, Message message) throws Fault {
    if (message.containsKey(LoggingMessage.ID_KEY)) {
      return;
    }
    String id = (String) message.getExchange().get(LoggingMessage.ID_KEY);
    if (id == null) {
      id = LoggingMessage.nextId();
      message.getExchange().put(LoggingMessage.ID_KEY, id);
    }

    message.put(LoggingMessage.ID_KEY, id);
    final LoggingMessage buffer
        = new LoggingMessage("Inbound Message\n----------------------------", id);

    if (!Boolean.TRUE.equals(message.get(Message.DECOUPLED_CHANNEL_MESSAGE))) {
      // avoid logging the default responseCode 200 for the decoupled responses
      Integer responseCode = (Integer) message.get(Message.RESPONSE_CODE);
      if (responseCode != null) {
        buffer.getResponseCode().append(responseCode);
      }
    }

    String encoding = (String) message.get(Message.ENCODING);

    if (encoding != null) {
      buffer.getEncoding().append(encoding);
    }
    String httpMethod = (String) message.get(Message.HTTP_REQUEST_METHOD);
    if (httpMethod != null) {
      buffer.getHttpMethod().append(httpMethod);
    }
    String ct = (String) message.get(Message.CONTENT_TYPE);
    if (ct != null) {
      buffer.getContentType().append(ct);
    }
    Object headers = message.get(Message.PROTOCOL_HEADERS);

    if (headers != null) {
      buffer.getHeader().append(headers);
    }

    Map<String, List<String>> protocolHeader = (TreeMap) message.get(Message.PROTOCOL_HEADERS);
    if (protocolHeader != null && protocolHeader.get("locale") != null) {

      String language = protocolHeader.get("locale").get(0);
      System.out.println("---language--- " + language);
      if (language == null || "".equalsIgnoreCase(language)) {
        isDefaultLocale = false;
      }
      if ("en".equalsIgnoreCase(language) || "en_us".equalsIgnoreCase(language)) {
        language = "en_US";
      } else {
        language = "vi_VN";
      }
      mapLogWS.put("language", language);
    } else {
      isDefaultLocale = false;
    }
    if (protocolHeader != null && protocolHeader.get("RequestId") != null) {
      String requestId = protocolHeader.get("RequestId").get(0);
      mapLogWS.put(requestId, buffer.toString());
    }

    String uri = (String) message.get(Message.REQUEST_URL);
    if (uri == null) {
      String address = (String) message.get(Message.ENDPOINT_ADDRESS);
      uri = (String) message.get(Message.REQUEST_URI);
      if (uri != null && uri.startsWith("/")) {
        if (address != null && !address.startsWith(uri)) {
          if (address.endsWith("/") && address.length() > 1) {
            address = address.substring(0, address.length());
          }
          uri = address + uri;
        }
      } else {
        uri = address;
      }
    }
    if (uri != null) {
      buffer.getAddress().append(uri);
      String query = (String) message.get(Message.QUERY_STRING);
      if (query != null) {
        buffer.getAddress().append("?").append(query);
      }
    }

    if (!isShowBinaryContent() && isBinaryContent(ct)) {
      buffer.getMessage().append(BINARY_CONTENT_MESSAGE).append('\n');
      log(logger, buffer.toString());
      return;
    }

    InputStream is = message.getContent(InputStream.class);
    if (is != null) {
      logInputStream(message, is, buffer, encoding, ct);
    } else {
      Reader reader = message.getContent(Reader.class);
      if (reader != null) {
        logReader(message, reader, buffer);
      }
    }
    //System.out.println("==================== "+ id + " Request : " + buffer.getPayload().toString());
    mapLogWS.put(id, buffer.getPayload().toString());
    mapDateWS.put(id, DateTimeUtils.convertDateToString(new Date(), "dd/MM/yyyy HH:mm:ss.SSS"));

  }

  Logger getMessageLogger(Message message) {
    Object liveLoggingProp = message.getContextualProperty("org.apache.cxf.logging.enable");
    boolean isLoggingDisabledNow =
        liveLoggingProp != null && PropertyUtils.isFalse(liveLoggingProp);
    if (isLoggingDisabledNow) {
      return null;
    }
    Endpoint ep = message.getExchange().getEndpoint();
    if (ep == null || ep.getEndpointInfo() == null) {
      return getLogger();
    }
    EndpointInfo endpoint = ep.getEndpointInfo();
    if (endpoint.getService() == null) {
      return getLogger();
    }
    Logger logger = endpoint.getProperty("MessageLogger", Logger.class);
    if (logger == null) {
      String serviceName = endpoint.getService().getName().getLocalPart();
      InterfaceInfo iface = endpoint.getService().getInterface();
      String portName = endpoint.getName().getLocalPart();
      String portTypeName = iface.getName().getLocalPart();
      String logName = "org.apache.cxf.services." + serviceName + "."
          + portName + "." + portTypeName;
      logger = org.apache.cxf.common.logging.LogUtils.getL7dLogger(this.getClass(), null, logName);
      endpoint.setProperty("MessageLogger", logger);
    }
    return logger;
  }

  public static String getLanguage() {
    return mapLogWS.get("language");
  }

  public static void setLanguage(String language) {
    mapLogWS.put("language", language);
  }

  public static String getLog(String requestId) {
    return mapLogWS.get(requestId);
  }

  public static String removeLog(String requestId) {
    return mapLogWS.remove(requestId);
  }

  public static String getRequestTime(String requestId) {
    return mapDateWS.get(requestId);
  }

  public static String removeRequestTime(String requestId) {
    return mapDateWS.remove(requestId);
  }

  public Boolean getIsDefaultLocale() {
    return isDefaultLocale;
  }

}
