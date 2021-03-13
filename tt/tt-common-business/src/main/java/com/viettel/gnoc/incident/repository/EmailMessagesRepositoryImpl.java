package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.pt.dto.EmailMessagesDTO;
import com.viettel.gnoc.pt.model.EmailMessagesEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class EmailMessagesRepositoryImpl extends BaseRepository implements EmailMessagesRepository {

  @Override
  public ResultInSideDto insertOrUpdate(EmailMessagesDTO emailMessagesDTO) {
    if (emailMessagesDTO.getId() == null) {
      return insertByModel(emailMessagesDTO.toEntity(), "id");
    } else {
      EmailMessagesEntity entity = emailMessagesDTO.toEntity();
      getEntityManager().merge(entity);
      return new ResultInSideDto(entity.getId(), RESULT.SUCCESS, RESULT.SUCCESS);
    }
  }

}
