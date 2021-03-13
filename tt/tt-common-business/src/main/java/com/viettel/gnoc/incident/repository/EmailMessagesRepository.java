package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.pt.dto.EmailMessagesDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailMessagesRepository {

  ResultInSideDto insertOrUpdate(EmailMessagesDTO emailMessagesDTO);
}
