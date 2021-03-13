package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.UserReceiveMsgDTO;
import com.viettel.gnoc.cr.repository.UserReceiveMsgRepository;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class UserReceiveMsgBusinessImpl implements UserReceiveMsgBusiness {

  @Autowired
  UserReceiveMsgRepository userReceiveMsgRepository;

  @Override
  public ResultInSideDto insertOrUpdate(UserReceiveMsgDTO userReceiveMsgDTO) {
    return userReceiveMsgRepository.insertOrUpdate(userReceiveMsgDTO);
  }

  @Override
  public List<UserReceiveMsgDTO> getListUserReceiveMsgDTO(UserReceiveMsgDTO userReceiveMsgDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return userReceiveMsgRepository
        .getListUserReceiveMsgDTO(userReceiveMsgDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public ResultInSideDto deleteUserReceiveMsg(Long id) {
    return userReceiveMsgRepository.deleteUserReceiveMsg(id);
  }
}
