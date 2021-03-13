package com.viettel.gnoc.sr.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.sr.business.SrAomBusiness;
import java.util.List;
import java.util.Locale;
import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
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
public class SRAomServiceImpl implements SRAomService {

  @Autowired
  protected SrAomBusiness srAomBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public ResultDTO getListSRForGatePro(String fromDate, String toDate) {
    I18n.setLocaleForService(wsContext);
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance("com.viettel.gnoc.sr.dto");
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return srAomBusiness.getListSRForGatePro(fromDate, toDate);
  }

  @Override
  public ResultDTO updateSRForGatePro(String srCode, String status, String fileContent) {
    I18n.setLocaleForService(wsContext);
    return srAomBusiness.updateSRForGatePro(srCode, status, fileContent);
  }
}
