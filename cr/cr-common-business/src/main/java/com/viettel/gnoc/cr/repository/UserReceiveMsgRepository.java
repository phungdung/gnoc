package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.UserReceiveMsgDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReceiveMsgRepository {

  ResultInSideDto insertOrUpdate(UserReceiveMsgDTO userReceiveMsgDTO);

  List<UserReceiveMsgDTO> getListUserReceiveMsgDTO(UserReceiveMsgDTO userReceiveMsgDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  ResultInSideDto deleteUserReceiveMsg(Long id);
}
