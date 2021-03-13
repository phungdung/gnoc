package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ShiftCrDTO;
import com.viettel.gnoc.cr.model.ShiftCrEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ShiftCrRepositoryImpl extends BaseRepository implements
    ShiftCrRepository {

  @Override
  public ResultInSideDto deleteShiftCr(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    ShiftCrEntity staftEntity = getEntityManager()
        .find(ShiftCrEntity.class, id);
    if (staftEntity != null) {
      getEntityManager().remove(staftEntity);
    }
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto updateShiftCr(ShiftCrDTO dto) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(dto.toEntity());
    return resultInSideDTO;
  }

  @Override
  public Datatable getListShiftCr(ShiftCrDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SHIFT_CR, "getListShiftCr");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(dto)) {
      if (!StringUtils.isLongNullOrEmpty(dto.getShiftHandoverId())) {
        sql += " AND t1.SHIFT_HANDOVER_ID = :p_shift_handover_id ";
        parameters.put("p_shift_handover_id", dto.getShiftHandoverId());
      }
      return getListDataTableBySqlQuery(sql, parameters, dto.getPage(),
          dto.getPageSize(), ShiftCrDTO.class,
          dto.getSortName(),
          dto.getSortType());
    }
    return null;
  }

  @Override
  public ShiftCrDTO findShiftCrById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(ShiftCrEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertShiftCr(ShiftCrDTO shiftCrDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    ShiftCrEntity shiftCrEntity = getEntityManager().merge(shiftCrDTO.toEntity());
    resultInSideDTO.setId(shiftCrEntity.getId());
    return resultInSideDTO;
  }

  @Override
  public List<ShiftCrDTO> getListShiftCrByShiftID(ShiftCrDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SHIFT_CR, "getListShiftCr");
    Map<String, Object> parameters = new HashMap<>();
    if (dto != null) {
      if (!StringUtils.isLongNullOrEmpty(dto.getShiftHandoverId())) {
        sql += " AND t1.SHIFT_HANDOVER_ID = :p_shift_handover_id ";
        parameters.put("p_shift_handover_id", dto.getShiftHandoverId());
      }
    }
    List<ShiftCrDTO> shiftCrDTOS = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(ShiftCrDTO.class));
    return shiftCrDTOS;
  }
}
