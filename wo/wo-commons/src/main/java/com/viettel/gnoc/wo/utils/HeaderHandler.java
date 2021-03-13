package com.viettel.gnoc.wo.utils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeaderHandler implements SOAPHandler<SOAPMessageContext> {

  private String user;
  private String pass;
  private String system;

  public HeaderHandler(String user, String pass, String system) {
    this.user = user;
    this.pass = pass;
    this.system = system;
  }

  @Override
  public Set getHeaders() {
    return null;
  }

  @Override
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
          String encript = "Password1";
          SOAPElement password
              = usernameToken.addChildElement(encript.substring(0, 8), "wsse");
          password.setAttribute("Type",
              "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
          password.addTextNode(pass);
        }
        //ghi log goi ws du an khac_start
        //Print out the outbound SOAP message to System.out
        SecureRandom rand = new SecureRandom();

        int n = rand.nextInt(800) + 100;
        Long date = new Date().getTime();
        String messageId =
            "[" + date + "_" + n + "]" + (system != null ? ("[" + system + "]") : "");
        smc.put("messageId", messageId);
        OutputStream os = new ByteArrayOutputStream();
        message.writeTo(os);
        log.info(messageId + os.toString());

      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }

    } else {
      try {

        //This handler does nothing with the response from the Web Service so
        //we just print out the SOAP message.
        SOAPMessage message = smc.getMessage();
        OutputStream os = new ByteArrayOutputStream();
        message.writeTo(os);
        //String response = os.toString();
        //ghi log goi ws du an khac_end
        String messageId = (String) smc.get("messageId");
        smc.put("messageId", messageId);
        log.info(messageId + os.toString());
        try {

          Long date = new Date().getTime();
          String[] startTime = messageId.split("_");
          if (startTime.length == 2) {
            String tmp = startTime[0].substring(1, startTime[0].length());
            Long total = date - Long.valueOf(tmp);
            if (total > 10000) {
              log.info(messageId + "ResponseNOK:" + total);
            } else {
              log.info(messageId + "ResponseOK:" + total);
            }
          }
        } catch (Exception e) {
          log.info(e.getMessage(), e);
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }
    }

    return outboundProperty;
  }

  @Override
  public boolean handleFault(SOAPMessageContext soapMessageContext) {
    return false;
  }

  @Override
  public void close(MessageContext messageContext) {

  }
}
