package com.viettel.gnoc.common.service;

import com.viettel.gnoc.commons.dto.MessagesDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "MessagesService")
public interface MessagesService {

  @WebMethod(operationName = "insertOrUpdateListMessages")
  public String insertOrUpdateListMessages(
      @WebParam(name = "messagesDTO") List<MessagesDTO> messagesDTO);

  @WebMethod(operationName = "insertOrUpdateListMessagesCommonWfm")
  public String insertOrUpdateListMessagesCommonWfm(
      @WebParam(name = "messagesDTO") List<MessagesDTO> messagesDTO);
}
