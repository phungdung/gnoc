package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.cr.dto.ShiftItSeriousDTO;
import com.viettel.gnoc.cr.model.ShiftItSeriousEntity;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ShiftItSeriousRepositoryImpl extends BaseRepository implements
    ShiftItSeriousRepository {

  @Override
  public ResultInSideDto deleteShiftItSerious(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    ShiftItSeriousEntity staftEntity = getEntityManager()
        .find(ShiftItSeriousEntity.class, id);
    if (staftEntity != null) {
      getEntityManager().remove(staftEntity);
    }
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto updateShiftItSerious(ShiftItSeriousDTO dto) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(dto.toEntity());
    return resultInSideDTO;
  }

  @Override
  public Datatable getListShiftItSerious(ShiftItSeriousDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SHIFT_IT_SERIOUS, "getListShiftItSerious");
    Map<String, Object> parameters = new HashMap<>();
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(),
        dto.getPageSize(), ShiftItSeriousDTO.class,
        dto.getSortName(),
        dto.getSortType());
  }

  @Override
  public ShiftItSeriousDTO findShiftItSeriousById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(ShiftItSeriousEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertShiftItSerious(ShiftItSeriousDTO shiftItSeriousDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    ShiftItSeriousEntity seriousEntity = getEntityManager().merge(shiftItSeriousDTO.toEntity());
    resultInSideDTO.setId(seriousEntity.getId());
    return resultInSideDTO;
  }

}
