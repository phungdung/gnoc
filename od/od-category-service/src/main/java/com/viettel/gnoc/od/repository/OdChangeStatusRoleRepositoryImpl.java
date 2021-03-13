package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.od.dto.OdChangeStatusDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusRoleDTO;
import com.viettel.gnoc.od.model.OdChangeStatusRoleEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class OdChangeStatusRoleRepositoryImpl extends BaseRepository implements
    OdChangeStatusRoleRepository {

  @Override
  public ResultInSideDto addList(OdChangeStatusDTO odChangeStatusDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    List<OdChangeStatusRoleDTO> odChangeStatusRoleDTOS = odChangeStatusDTO
        .getOdChangeStatusRoleDTO();
    if (odChangeStatusRoleDTOS != null) {
      odChangeStatusRoleDTOS.forEach(c -> {
        c.setId(null);
        c.setOdChangeStatusId(odChangeStatusDTO.getId());
        getEntityManager().persist(c.toEntity());
      });
    }
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDTO;
  }

  @Override
  public Long insertOrUpdate(OdChangeStatusRoleDTO odChangeStatusRoleDTO) {
    if (odChangeStatusRoleDTO.getId() != null && odChangeStatusRoleDTO.getId() > 0) {
      getEntityManager().merge(odChangeStatusRoleDTO.toEntity());
      return odChangeStatusRoleDTO.getId();
    } else {
      OdChangeStatusRoleEntity entity = odChangeStatusRoleDTO.toEntity();
      getEntityManager().persist(entity);
      return entity.getId();
    }
  }

  @Override
  public List<OdChangeStatusRoleDTO> getListByOdChangeStatusId(Long odChangeStatusId) {
    String sql = "select id, OD_CHANGE_STATUS_ID odChangeStatusId, ROLE_ID roleId from OD_CHANGE_STATUS_ROLE WHERE OD_CHANGE_STATUS_ID = :odChangeStatusId ";

    Map<String, String> params = new HashMap<>();
    params.put("odChangeStatusId", odChangeStatusId.toString());
    List<OdChangeStatusRoleDTO> datas = getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(OdChangeStatusRoleDTO.class));
    return datas;
  }

  @Override
  public ResultInSideDto deleteByOdChangeStatusId(Long odChangeStatusId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    List<OdChangeStatusRoleEntity> datas = findByMultilParam(OdChangeStatusRoleEntity.class,
        "odChangeStatusId", odChangeStatusId);
    for (OdChangeStatusRoleEntity data : datas) {
      getEntityManager().remove(data);
    }
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    resultInSideDTO.setMessage(Constants.RESULT.SUCCESS);
    return resultInSideDTO;
  }
}
