package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.cr.dto.ShiftWorkOtherDTO;
import com.viettel.gnoc.cr.model.ShiftWorkOtherEntity;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ShiftWorkOtherRepositoryImpl extends BaseRepository implements
    ShiftWorkOtherRepository {

  @Override
  public ResultInSideDto deleteShiftWorkOther(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    ShiftWorkOtherEntity staftEntity = getEntityManager()
        .find(ShiftWorkOtherEntity.class, id);
    if (staftEntity != null) {
      getEntityManager().remove(staftEntity);
    }
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto updateShiftWorkOther(ShiftWorkOtherDTO dto) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(dto.toEntity());
    return resultInSideDTO;
  }


  @Override
  public Datatable getListShiftWorkOther(ShiftWorkOtherDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SHIFT_WORK_OTHER, "getListShiftWorkOther");
    Map<String, Object> parameters = new HashMap<>();
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(),
        dto.getPageSize(), ShiftWorkOtherDTO.class,
        dto.getSortName(),
        dto.getSortType());
  }


  @Override
  public ShiftWorkOtherDTO findShiftWorkOtherById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(ShiftWorkOtherEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertShiftWorkOther(ShiftWorkOtherDTO shiftWorkOtherDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    ShiftWorkOtherEntity shiftItEntity = getEntityManager().merge(shiftWorkOtherDTO.toEntity());
    resultInSideDTO.setId(shiftItEntity.getId());
    return resultInSideDTO;
  }

}
