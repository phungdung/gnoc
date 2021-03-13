package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ShiftWorkDTO;
import com.viettel.gnoc.cr.model.ShiftWorkEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ShiftWorkRepositoryImpl extends BaseRepository implements
    ShiftWorkRepository {


  @Override
  public ResultInSideDto deleteShiftWork(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    ShiftWorkEntity staftEntity = getEntityManager()
        .find(ShiftWorkEntity.class, id);
    if (staftEntity != null) {
      getEntityManager().remove(staftEntity);
    }
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto updateShiftWork(ShiftWorkDTO dto) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(dto.toEntity());
    return resultInSideDTO;
  }

  @Override
  public Datatable getListShiftWork(ShiftWorkDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SHIFT_WORK, "getListShiftWork");
    Map<String, Object> parameters = new HashMap<>();
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(),
        dto.getPageSize(), ShiftWorkDTO.class,
        dto.getSortName(),
        dto.getSortType());
  }

  @Override
  public ShiftWorkDTO findShiftWorkById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(ShiftWorkEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertShiftWork(ShiftWorkDTO shiftWorkDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    ShiftWorkEntity shiftWorkEntity = getEntityManager().merge(shiftWorkDTO.toEntity());
    resultInSideDTO.setId(shiftWorkEntity.getId());
    return resultInSideDTO;
  }

  @Override
  public List<ShiftWorkDTO> getListShiftWorkByShiftID(ShiftWorkDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SHIFT_WORK, "getListShiftWork");
    Map<String, Object> parameters = new HashMap<>();
    if (dto != null) {
      if (!StringUtils.isLongNullOrEmpty(dto.getShiftHandoverId())) {
        sql += " AND t1.SHIFT_HANDOVER_ID = :pShiftHandoverId ";
        parameters.put("pShiftHandoverId", dto.getShiftHandoverId());
      }
    }
    List<ShiftWorkDTO> shiftWorkDTOS = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(ShiftWorkDTO.class));
    return shiftWorkDTOS;
  }

}
