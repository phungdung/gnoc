package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.dto.UserReceiveMsgDTO;
import com.viettel.gnoc.cr.model.UserReceiveMsgEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserReceiveMsgRepositoryImpl extends BaseRepository implements
    UserReceiveMsgRepository {

  @Override
  public ResultInSideDto insertOrUpdate(UserReceiveMsgDTO userReceiveMsgDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    UserReceiveMsgEntity oEntity = userReceiveMsgDTO.toEntity();
    if (oEntity.getUserReceiveMsgId() != null) {
      getEntityManager().merge(oEntity);
    } else {
      getEntityManager().persist(oEntity);
    }
    resultInSideDTO.setId(oEntity.getUserReceiveMsgId());
    return resultInSideDTO;
  }

  @Override
  public List<UserReceiveMsgDTO> getListUserReceiveMsgDTO(UserReceiveMsgDTO userReceiveMsgDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(UserReceiveMsgEntity.class, userReceiveMsgDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public ResultInSideDto deleteUserReceiveMsg(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    UserReceiveMsgEntity problemsEntity = getEntityManager().find(UserReceiveMsgEntity.class, id);
    getEntityManager().remove(problemsEntity);
    return resultInSideDTO;
  }
}
