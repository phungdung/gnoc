package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.cr.dto.ShiftStaftDTO;
import com.viettel.gnoc.cr.model.ShiftStaftEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ShiftStaftRepositoryImpl extends BaseRepository implements
    ShiftStaftRepository {

  @Override
  public ResultInSideDto deleteShiftUser(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    ShiftStaftEntity staftEntity = getEntityManager()
        .find(ShiftStaftEntity.class, id);
    if (staftEntity != null) {
      getEntityManager().remove(staftEntity);
    }
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto updateShiftUser(ShiftStaftDTO dto) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(dto.toEntity());
    return resultInSideDTO;
  }

  @Override
  public Datatable getListShiftUser(ShiftStaftDTO shiftStaftDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SHIFT_STAFT, "getListShiftUser");
    Map<String, Object> parameters = new HashMap<>();
    return getListDataTableBySqlQuery(sql, parameters, shiftStaftDTO.getPage(),
        shiftStaftDTO.getPageSize(), ShiftStaftDTO.class,
        shiftStaftDTO.getSortName(),
        shiftStaftDTO.getSortType());
  }

  @Override
  public ShiftStaftDTO findShiftUserById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(ShiftStaftEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertShiftUser(ShiftStaftDTO shiftStaftDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    ShiftStaftEntity shiftStaftEntity = getEntityManager().merge(shiftStaftDTO.toEntity());
    resultInSideDTO.setId(shiftStaftEntity.getId());
    return resultInSideDTO;
  }


  @Override
  public List<ShiftStaftDTO> getListShiftStaftById(Long id) {
    String sql = "SELECT t1.ID id,t1.ASSIGN_USER_ID assignUserId, t1.ASSIGN_USER_NAME assignUserName, t1.RECEIVE_USER_ID receiveUserId, t1.RECEIVE_USER_NAME receiveUserName, t1.SHIFT_HANDOVER_ID shiftHandoverId FROM COMMON_GNOC.SHIFT_STAFT t1 WHERE t1.SHIFT_HANDOVER_ID = :shiftHandOverId";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("shiftHandOverId", id);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(ShiftStaftDTO.class));
  }

}
