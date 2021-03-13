package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.MessagesCommonEntity;
import com.viettel.gnoc.commons.model.MessagesWfmEntity;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@SuppressWarnings("rawtypes")

@Repository
@Slf4j
public class MessagesRepositoryImpl extends BaseRepository implements MessagesRepository {

  @Override
  public ResultInSideDto insertOrUpdateCommon(MessagesDTO odDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    MessagesCommonEntity oEntity = odDTO.toCommonEntity();
    if (oEntity.getMessageId() != null) {
      getEntityManager().merge(oEntity);
    } else {
      getEntityManager().persist(oEntity);
    }
    resultInSideDTO.setId(oEntity.getMessageId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto insertOrUpdateListMessagesCommon(List<MessagesDTO> listMessagesDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    for (MessagesDTO dto : listMessagesDTO) {
      resultInSideDTO = insertOrUpdateCommon(dto);
    }
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto insertOrUpdateWfm(MessagesDTO odDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    MessagesWfmEntity oEntity = getEntityManager().merge(odDTO.toWfmEntity());
    resultInSideDTO.setId(oEntity.getMessageId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto insertOrUpdateListMessagesWfm(List<MessagesDTO> listMessagesDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    for (MessagesDTO dto : listMessagesDTO) {
      resultInSideDTO = insertOrUpdateWfm(dto);
    }
    return resultInSideDTO;
  }

  @Override
  public String insertList(List<MessagesDTO> messagesDTO) {
    for (MessagesDTO dto : messagesDTO) {
      MessagesCommonEntity entity = dto.toCommonEntity();
      if (entity.getMessageId() != null && entity.getMessageId() > 0) {
        getEntityManager().merge(entity);
      } else {
        getEntityManager().persist(entity);
      }

      MessagesWfmEntity messagesWfmEntity = dto.toWfmEntity();
      if (messagesWfmEntity.getMessageId() != null && messagesWfmEntity.getMessageId() > 0) {
        getEntityManager().merge(messagesWfmEntity);
      } else {
        getEntityManager().persist(messagesWfmEntity);
      }
    }
    return RESULT.SUCCESS;
  }

  @Override
  public String insertListWS(List<MessagesDTO> messagesDTO) {
    for (MessagesDTO dto : messagesDTO) {
      MessagesCommonEntity entity = dto.toCommonEntity();
      getEntityManager().merge(entity);
    }
    return RESULT.SUCCESS;
  }
}
