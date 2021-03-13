package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@Repository
public interface MessagesRepository {

  ResultInSideDto insertOrUpdateCommon(MessagesDTO odDTO);

  ResultInSideDto insertOrUpdateListMessagesCommon(List<MessagesDTO> listMessagesDTO);

  ResultInSideDto insertOrUpdateWfm(MessagesDTO odDTO);

  ResultInSideDto insertOrUpdateListMessagesWfm(List<MessagesDTO> listMessagesDTO);

  String insertList(List<MessagesDTO> messagesDTO);

  String insertListWS(List<MessagesDTO> messagesDTO);
}
