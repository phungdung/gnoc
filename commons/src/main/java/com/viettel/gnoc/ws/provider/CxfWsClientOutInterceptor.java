/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.ws.provider;

import com.viettel.gnoc.commons.dto.UserTokenGNOC;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * @author dungnv50
 */
@Slf4j
public class CxfWsClientOutInterceptor extends LoggingOutInterceptor {

  static final Logger logger = Logger.getLogger(CxfWsClientInInterceptor.class.getName());

  public CxfWsClientOutInterceptor() {
    super(Phase.PRE_STREAM);
  }

  private static final Map<String, String> mapLogWS = new HashMap();
  private static final Map<String, Date> mapDateWS = new HashMap();
  private static final Map<String, String> mapMethodWS = new HashMap();

  @Override
  public void handleMessage(Message message) {
    try {
      RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
      ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
      HttpServletRequest servletRequest = attributes.getRequest();
      HttpSession httpSession = servletRequest.getSession(true);

      UserTokenGNOC user = (UserTokenGNOC) httpSession
          .getAttribute(Constants.vsaUserTokenAttribute);
      String id = (String) message.getExchange().get(LoggingMessage.ID_KEY);
      if (id == null) {
        id = LoggingMessage.nextId();
        message.getExchange().put(LoggingMessage.ID_KEY, id);
//                System.out.println("id: " + id);
      }

      try {
        List<String> listRequest = new ArrayList<>();

        //listRequest.add(token.getToken() + "_" + id);
        Map<String, List<String>> protocolHeader = (TreeMap) message.get(Message.PROTOCOL_HEADERS);
        if (protocolHeader == null) {
          protocolHeader = new TreeMap<>();
        }
        protocolHeader.put("RequestId", listRequest);
        //ThanhLv12_them da ngon ngu server_start
        Locale locale = RequestContextUtils.getLocale(servletRequest);
//        Locale locale = servletRequest.getLocale();
        List<String> listLocale = new ArrayList<>();
        if (locale != null) {
          if ("vi".equalsIgnoreCase(locale.getLanguage()) || "vi_vn"
              .equalsIgnoreCase(locale.getLanguage())) {
            locale = new Locale("vi", "VN");
          }
          listLocale.add(locale.toString());
        }
        protocolHeader.put("locale", listLocale);

        //ThanhLv12_them da ngon ngu server_end
        List<String> listUserName = new ArrayList<String>();
        Object userName = httpSession.getAttribute("userName");
        listUserName.add(userName != null ? userName.toString() : " ");
        protocolHeader.put("userName", listUserName);
        List<String> listFunction = new ArrayList<String>();
        if (servletRequest.getAttribute("function") != null) {
          listFunction.add(servletRequest.getAttribute("function").toString());
        }
        listFunction.add(getMethod(message));
        protocolHeader.put("function", listFunction);
        List<String> listFunctionId = new ArrayList<String>();
        Object funcId = servletRequest.getAttribute("functionId");
        listFunctionId.add(funcId != null ? funcId.toString() : " ");
        protocolHeader.put("functionId", listFunctionId);
        message.put(Message.PROTOCOL_HEADERS, protocolHeader);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      Date startDate = new Date();
      String log = "start_action|GNOC"
          //    + "|" + DateTimeUtils.convertDateToString(new Date(), "dd/MM/yyyy HH:mm:ss.sss")
          + "|" + DateTimeUtils.convertDateToString(startDate, "yyyy/MM/dd HH:mm:ss:sss")
          + "|" + (user != null ? user.getUserName() : "")
          + "|" + servletRequest.getRemoteAddr()
          + "|" + servletRequest.getRemoteHost()
          + "|" + servletRequest.getRemotePort()
          + "|" + getMethod(message) + ".do"
          + "|" + ""
          + "|" + getMethod(message)
          + "|{responeTime}|[RequestId=" + "_" + id
          + "][Unit=" + (user != null ? user.getUnitCode() : "")
          + "][VTS-KPIID=null]"
          + "|" + servletRequest.getAttribute("function")
          + "|" + servletRequest.getAttribute("functionId");

      mapDateWS.put(id, startDate);
      mapMethodWS.put(id, getMethod(message));
      mapLogWS.put(id, log);

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    super.handleMessage(message);
  }

  @Override
  protected Logger getLogger() {
    return logger;
  }

  private String getMethod(Message message) {
    return StringUtils.getStringParttern(message.get("java.lang.reflect.Method").toString(),
        "com.viettel.[\\w\\.]+\\(").replace("(", "");
  }

  public static Map<String, String> getMapLogWS() {
    return mapLogWS;
  }

  public static Map<String, Date> getMapDateWS() {
    return mapDateWS;
  }

  public static Map<String, String> getMapMethodWS() {
    return mapMethodWS;
  }

  public static void removeMapLogWS(String id) {
    mapLogWS.remove(id);
  }

  public static void removeMapDateWS(String id) {
    mapDateWS.remove(id);
  }

  public static void removeMapMethodWS(String id) {
    mapMethodWS.remove(id);
  }
}
