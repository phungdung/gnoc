package com.viettel.gnoc.wo.utils;

import com.viettel.gnoc.wo.dto.HeaderForm;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WsHandler implements SOAPHandler<SOAPMessageContext> {

  List<HeaderForm> lstHeader;
  String system;

  public WsHandler(List<HeaderForm> lstHeader) {
    this.lstHeader = lstHeader;
  }

  public boolean handleMessage(SOAPMessageContext messageContext) {
    SOAPMessage msg = messageContext.getMessage();
    if ((Boolean) messageContext
        .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)) {// goi di WS doi khac
      try {
        SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
        if (envelope.getHeader() != null) {
          envelope.getHeader().detachNode();
        }
        SOAPHeader header = envelope.addHeader();
        if (lstHeader != null && lstHeader.size() > 0) {
          for (HeaderForm h : lstHeader) {
            try {
              if (h != null && h.getValue() != null) {
                SOAPElement el = header.addChildElement(envelope
                    .createName(h.getKey(), envelope.getPrefix(), envelope.getNamespaceURI()));
                if (el != null) {
                  el.setValue(h.getValue());
                }
              }
            } catch (Exception e) {
              log.error(e.getMessage(), e);
              System.out.println("Exception khi them header vao WS");
            }
          }
        }
        msg.saveChanges();
        OutputStream os = new ByteArrayOutputStream();
        msg.writeTo(os);
        System.out.println("" + os.toString());
      } catch (SOAPException e) {
        log.error(e.getMessage(), e);
        return false;
      } catch (Exception e) {
        log.info(e.getMessage(), e);
      }
    } else {  // ket qua WS doi khac tra vek

    }
    return true;
  }

  public boolean handleFault(SOAPMessageContext messageContext) {
    return true;
  }

  public void close(MessageContext messageContext) {
  }

  public Set getHeaders() {
    return new HashSet();
  }

}
