package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.kedb.dto.UserSmsDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSmsRepository {

  ResultInSideDto add(UserSmsDTO userSmsDTO);

  UserSmsDTO getDetail(Long userId);

  List<UserSmsDTO> getUserReceiveSMSEmailByTypeCode(String typeCode, String emailSMS);

  ResultInSideDto insertUserSms(UserSmsDTO userSmsDTO);

  ResultInSideDto updateUserSms(UserSmsDTO userSmsDTO);
}
