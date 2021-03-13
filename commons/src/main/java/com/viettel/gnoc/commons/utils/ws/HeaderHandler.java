/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.utils.ws;

import com.viettel.gnoc.wo.dto.HeaderForm;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.apache.log4j.Logger;

/**
 * Created Time : 11/05/2016 10:55:57 AM
 *
 * @author anhmv6
 */
public class HeaderHandler implements SOAPHandler<SOAPMessageContext> {

  Logger logger = Logger.getLogger(HeaderHandler.class);
  private String user;
  private String pass;
  final String wsse = "wsse";

  public HeaderHandler(String user, String pass) {
    this.user = user;
    this.pass = pass;
  }

  public static void setHeaderHandler(Object port, List<HeaderForm> lstHeader) {
    Binding binding = ((BindingProvider) port).getBinding();
    List<Handler> handlerChain = new ArrayList<>();
    handlerChain.add(new HeaderHandlerResolverSOAP(lstHeader));
    binding.setHandlerChain(handlerChain);
  }

  public boolean handleMessage(SOAPMessageContext smc) {

    Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

    if (outboundProperty.booleanValue()) {

      SOAPMessage message = smc.getMessage();

      try {

        SOAPEnvelope envelope = smc.getMessage().getSOAPPart().getEnvelope();

        if (envelope.getHeader() != null) {
          envelope.getHeader().detachNode();
        }
        SOAPHeader header = envelope.addHeader();

        SOAPElement security
            = header.addChildElement("Security", "wsse",
            "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        security.addAttribute(new QName("xmlns:wsu"),
            "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");

        if (user != null && pass != null) {
          SOAPElement usernameToken
              = security.addChildElement("UsernameToken", "wsse");
//                usernameToken.addAttribute(new QName("xmlns:wsu"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");

          SOAPElement username
              = usernameToken.addChildElement("Username", "wsse");
          username.addTextNode(user);

          SOAPElement password
              = usernameToken.addChildElement("Password", getWsse());
          password.setAttribute("Type",
              "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
          password.addTextNode(pass);
        }
        //ThanhLV12_ghi log goi ws du an khac_start
        //Print out the outbound SOAP message to System.out
        SecureRandom random = new SecureRandom();

        int n = random.nextInt(800) + 100;
        Long date = new Date().getTime();
        String messageId = "[" + date + "_" + n + "]";
        smc.put("messageId", messageId);
        OutputStream os = new ByteArrayOutputStream();
        message.writeTo(os);
        logger.info(messageId + os.toString());
        //ThanhLV12_ghi log goi ws du an khac_end

      } catch (Exception e) {
        logger.error(e.getMessage(), e);
      }

    } else {
      try {

        //This handler does nothing with the response from the Web Service so
        //we just print out the SOAP message.
        SOAPMessage message = smc.getMessage();
        OutputStream os = new ByteArrayOutputStream();
        message.writeTo(os);
        //String response = os.toString();
        //ThanhLV12_ghi log goi ws du an khac_end
        String messageId = (String) smc.get("messageId");
        smc.put("messageId", messageId);
        logger.info(messageId + os.toString());
        try {

          Long date = new Date().getTime();
          String[] startTime = messageId.split("_");
          if (startTime.length == 2) {
            String tmp = startTime[0].substring(1, startTime[0].length());
            Long total = date - Long.valueOf(tmp);
            if (total > 10000) {
              logger.info(messageId + "ResponseNOK:" + total);
            } else {
              logger.info(messageId + "ResponseOK:" + total);
            }
          }
        } catch (Exception e) {
          logger.info(e.getMessage(), e);
        }
        //ThanhLV12_ghi log goi ws du an khac_end
      } catch (Exception ex) {
        logger.error(ex.getMessage(), ex);
      }
    }

    return outboundProperty;

  }

  public Set getHeaders() {
    //throw new UnsupportedOperationException("Not supported yet.");
    return null;
  }

  public boolean handleFault(SOAPMessageContext context) {
    //throw new UnsupportedOperationException("Not supported yet.");
    return true;
  }

  public void close(MessageContext context) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  public String getWsse() {
    return wsse;
  }
}
