package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.TT_MASTER_CODE;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.incident.dto.TTChangeStatusRoleDTO;
import com.viettel.gnoc.incident.model.TTChangeStatusRoleEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TTChangeStatusRoleRepositoryImpl extends BaseRepository implements
    TTChangeStatusRoleRepository {

  @Override
  public List<TTChangeStatusRoleDTO> getListRoleByTTChangeStatusId(Long ttChangeStatusId) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_TT_CHANGE_STATUS_ROLE_BUSINESS,
            "get-List-Role-By-TT-Change-Status-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("categoryCode", TT_MASTER_CODE.TT_CHANGE_STATUS_ROLE);
    parameters.put("ttChangeStatusId", ttChangeStatusId);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(TTChangeStatusRoleDTO.class));
  }

  @Override
  public ResultInSideDto deleteListTTChangeStatusRole(Long ttChangeStatusId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<TTChangeStatusRoleEntity> listEntity = findByMultilParam(
        TTChangeStatusRoleEntity.class, "ttChangeStatusId", ttChangeStatusId);
    if (listEntity != null && listEntity.size() > 0) {
      for (TTChangeStatusRoleEntity entity : listEntity) {
        getEntityManager().remove(entity);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertTTChangeStatusRole(TTChangeStatusRoleDTO ttChangeStatusRoleDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().persist(ttChangeStatusRoleDTO.toEntity());
    return resultInSideDTO;
  }
}
