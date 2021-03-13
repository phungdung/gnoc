package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.UserReceiveMsgDTO;
import java.util.List;

public interface UserReceiveMsgBusiness {

  ResultInSideDto insertOrUpdate(UserReceiveMsgDTO userReceiveMsgDTO);

  List<UserReceiveMsgDTO> getListUserReceiveMsgDTO(UserReceiveMsgDTO userReceiveMsgDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  ResultInSideDto deleteUserReceiveMsg(Long id);
}
