package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.cr.business.CrOutSiteBusiness;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import java.util.List;
import java.util.Locale;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
@Slf4j
public class CrAutoServiceImpl implements CrAutoService {

  @Autowired
  CrOutSiteBusiness crOutSiteBusiness;

  @Resource
  private WebServiceContext wsContext;

  @Override
  public ResultDTO insertAutoCr(CrDTO crDTO, List<CrFilesAttachDTO> lstFile, String system,
      String nationCode, String ftId, String userService, String passService) {
    setLocale();
    return crOutSiteBusiness
        .insertAutoCr(crDTO, lstFile, system, nationCode, ftId, userService, passService);
  }

  @Override
  public String actionCloseAutoCr(CrDTO cr, String userService, String passService) {
    setLocale();
    return crOutSiteBusiness.actionCloseAutoCr(cr, userService, passService);
  }

  @Override
  public String actionResolveAutoCr(CrDTO crDTO, String userService, String passService) {
    setLocale();
    return crOutSiteBusiness.actionResolveAutoCr(crDTO, userService, passService);
  }

  @Override
  public String getCrNumber(String crProcessId, String userService, String passService) {
    setLocale();
    return crOutSiteBusiness.getCrNumber(crProcessId, userService, passService);
  }

  private void setLocale() {
    Locale locale = getLocale();
    if (locale != null) {
      LocaleContextHolder.setLocale(locale);
    } else {
      LocaleContextHolder.setLocale(new Locale("vi", "VN"));
    }
  }

  private Locale getLocale() {
    String locale = null;
    Locale localeCus = null;
    try {
      MessageContext mcc = wsContext.getMessageContext();
      Message message = ((WrappedMessageContext) mcc).getWrappedMessage();
      List<Header> headers = (List<Header>) message.get(Header.HEADER_LIST);
      for (Header h : headers) {
        Element e = (Element) h.getObject();
        NodeList nodelists = e.getElementsByTagName("language");
        if (nodelists != null && nodelists.getLength() > 0) {
          Node node = nodelists.item(0);
          locale = node.getTextContent();
          if (locale != null && "en".equalsIgnoreCase(locale) || "en_us"
              .equalsIgnoreCase(locale)) {
            localeCus = new Locale("en", "US");
          } else if (locale != null && "vi".equalsIgnoreCase(locale) || "vi_vn"
              .equalsIgnoreCase(locale)) {
            localeCus = new Locale("vi", "VN");
          } else if (locale != null && "lo".equalsIgnoreCase(locale) || "lo_la"
              .equalsIgnoreCase(locale)) {
            localeCus = new Locale("lo", "LA");
          }
          break;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return localeCus;
  }
}
