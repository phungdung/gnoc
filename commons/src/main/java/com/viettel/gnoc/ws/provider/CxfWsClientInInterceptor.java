/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.ws.provider;

import com.viettel.gnoc.commons.dto.UserTokenGNOC;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import java.util.Date;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.interceptor.AbstractLoggingInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author dungnv50
 */
@Slf4j
public class CxfWsClientInInterceptor extends AbstractLoggingInterceptor {

  static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(
      CxfWsClientInInterceptor.class);
  static Logger LOG = null;

  public CxfWsClientInInterceptor() {
    super(Phase.RECEIVE);
  }

  @Override
  public void handleMessage(Message message)
      throws Fault {

    try {
      RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
      ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
      HttpServletRequest servletRequest = attributes.getRequest();
      HttpSession httpSession = servletRequest.getSession(true);
      String id = (String) message.getExchange().get(LoggingMessage.ID_KEY);
      if (id == null) {
        id = LoggingMessage.nextId();
        message.getExchange().put(LoggingMessage.ID_KEY, id);
      }

      String method = CxfWsClientOutInterceptor.getMapMethodWS().get(id);
      String startLog = CxfWsClientOutInterceptor.getMapLogWS().get(id);
      Date startDate = CxfWsClientOutInterceptor.getMapDateWS().get(id);
      UserTokenGNOC user = (UserTokenGNOC) httpSession
          .getAttribute(Constants.vsaUserTokenAttribute);
      Date endDate = new Date();
      Long responeTime = (endDate.getTime() - startDate.getTime());
      String endLog = "end_action|GNOC"
//                    + "|" + DateTimeUtils.convertDateToString(new Date(), "dd/MM/yyyy HH:mm:ss.sss")
          + "|" + DateTimeUtils.convertDateToString(endDate, "yyyy/MM/dd HH:mm:ss:sss")
          + "|" + (user != null ? user.getUserName() : "")
          + "|" + servletRequest.getRemoteAddr()
          + "|" + servletRequest.getRemoteHost()
          + "|" + servletRequest.getRemotePort()
          + "|" + method + ".do"
          + "|" + ""
          + "|" + method
          + "|" + responeTime
//                    + "|[RequestId=" + token + "_" + id
          + "][Unit=" + (user != null ? user.getUnitCode() : "")
          + "][VTS-KPIID=null]"
          + "|" + servletRequest.getAttribute("function")
          + "|" + servletRequest.getAttribute("functionId");
      logger.info(startLog.replace("{responeTime}", responeTime.toString()));
      logger.info(endLog);
      CxfWsClientOutInterceptor.removeMapLogWS(id);
      CxfWsClientOutInterceptor.removeMapDateWS(id);
      CxfWsClientOutInterceptor.removeMapMethodWS(id);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  protected Logger getLogger() {
    return LOG;
  }
}
