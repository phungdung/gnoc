package com.viettel.gnoc.common.service;

import com.viettel.gnoc.commons.business.MessagesBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessagesServiceImpl implements MessagesService {

  @Autowired
  MessagesBusiness messagesBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public String insertOrUpdateListMessages(List<MessagesDTO> messagesDTO) {
    log.info("Request to insertOrUpdateListMessages : {}", messagesDTO);
    I18n.setLocaleForService(wsContext);
    return messagesBusiness.insertOrUpdateListMessagesWS(messagesDTO);
  }

  @Override
  public String insertOrUpdateListMessagesCommonWfm(List<MessagesDTO> messagesDTO) {
    log.info("Request to insertOrUpdateListMessages : {}", messagesDTO);
    I18n.setLocaleForService(wsContext);
    return messagesBusiness.insertOrUpdateListMessages(messagesDTO);
  }
}
