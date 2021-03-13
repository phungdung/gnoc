package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.maintenance.dto.MrWoTempDTO;
import com.viettel.gnoc.maintenance.model.MrWoTempEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrWoTempRepositoryImpl extends BaseRepository implements MrWoTempRepository {

  @Override
  public List<MrWoTempDTO> search(MrWoTempDTO mrWoTempDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    return onSearchEntity(MrWoTempEntity.class, mrWoTempDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public ResultInSideDto updateMrWoTemp(MrWoTempDTO mrWoTemp) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrWoTempEntity entity = mrWoTemp.toEntity();
    getEntityManager().merge(entity);
    return resultInSideDto;
  }
}
