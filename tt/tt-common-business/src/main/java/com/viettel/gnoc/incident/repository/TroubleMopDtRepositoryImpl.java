package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.incident.dto.TroubleMopDtDTO;
import com.viettel.gnoc.incident.dto.TroubleMopDtInSideDTO;
import com.viettel.gnoc.incident.model.TroubleMopDtEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TroubleMopDtRepositoryImpl extends BaseRepository implements TroubleMopDtRepository {

  @Override
  public ResultDTO insertTroubleMopDt(TroubleMopDtDTO troubleMopDtDTO) {
    ResultInSideDto resultInSideDto = insertByModel(troubleMopDtDTO.toEntity(), "troubleDtId");
    ResultDTO resultDTO = new ResultDTO();
    resultDTO
        .setId(resultInSideDto.getId() != null ? String.valueOf(resultInSideDto.getId()) : null);
    resultDTO.setKey(resultInSideDto.getKey());
    resultDTO.setMessage(resultInSideDto.getMessage());
    return resultDTO;
  }

  @Override
  public ResultDTO update(TroubleMopDtInSideDTO troubleMopDtDTO) {
    getEntityManager().merge(troubleMopDtDTO.toEntity());
    ResultDTO resultDTO = new ResultDTO();
    resultDTO.setId(null);
    resultDTO.setKey(RESULT.SUCCESS);
    resultDTO.setMessage(RESULT.SUCCESS);
    return resultDTO;
  }

  @Override
  public List<TroubleMopDtInSideDTO> getListTroubleMopDtByCondition(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortName) {
    return onSearchByConditionBean(new TroubleMopDtEntity(), lstCondition, rowStart, maxRow,
        sortType, sortName);
  }

}
