package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ShiftItDTO;
import com.viettel.gnoc.cr.model.ShiftItEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ShiftItRepositoryImpl extends BaseRepository implements
    ShiftItRepository {

  @Override
  public ResultInSideDto deleteShiftIt(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    ShiftItEntity staftEntity = getEntityManager()
        .find(ShiftItEntity.class, id);
    if (staftEntity != null) {
      getEntityManager().remove(staftEntity);
    }
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto updateShiftIt(ShiftItDTO dto) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(dto.toEntity());
    return resultInSideDTO;
  }

  @Override
  public Datatable getListShiftIt(ShiftItDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SHIFT_IT, "getListShiftIt");
    Map<String, Object> parameters = new HashMap<>();
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(),
        dto.getPageSize(), ShiftItDTO.class,
        dto.getSortName(),
        dto.getSortType());
  }


  @Override
  public ShiftItDTO findShiftItById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(ShiftItEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertShiftIt(ShiftItDTO shiftItDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    ShiftItEntity shiftItEntity = getEntityManager().merge(shiftItDTO.toEntity());
    resultInSideDTO.setId(shiftItEntity.getId());
    return resultInSideDTO;
  }

  @Override
  public List<ShiftItDTO> getListShiftItByShiftID(ShiftItDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SHIFT_IT, "getListShiftIt");
    Map<String, Object> parameters = new HashMap<>();
    if (dto != null) {
      if (!StringUtils.isLongNullOrEmpty(dto.getShiftHandoverId())) {
        sql += " AND t1.SHIFT_HANDOVER_ID = :p_shift_handover_id ";
        parameters.put("p_shift_handover_id", dto.getShiftHandoverId());
      }
      if (!StringUtils.isLongNullOrEmpty(dto.getKpi())) {
        sql += " AND t1.KPI = :p_kpi ";
        parameters.put("p_kpi", dto.getKpi());
      }
    }
    List<ShiftItDTO> shiftItDTOS = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(ShiftItDTO.class));
    return shiftItDTOS;
  }
}
