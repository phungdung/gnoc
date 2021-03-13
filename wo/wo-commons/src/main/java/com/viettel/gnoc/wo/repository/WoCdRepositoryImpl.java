package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoCdDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.model.WoCdEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoCdRepositoryImpl extends BaseRepository implements WoCdRepository {

  @Override
  public List<WoCdDTO> getListWoCdDTO(WoCdDTO woCdDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD, "get-List-Wo-Cd-DTO");
    Map<String, Object> parameters = new HashMap<>();
    if (woCdDTO.getWoGroupId() != null) {
      sql += " AND c.WO_GROUP_ID = :woGroupId";
      parameters.put("woGroupId", woCdDTO.getWoGroupId());
    }
    if (woCdDTO.getUserId() != null) {
      sql += " AND c.USER_ID = :userId";
      parameters.put("userId", woCdDTO.getUserId());
    }
    if (woCdDTO.getListUserIdDel() != null && !woCdDTO.getListUserIdDel().isEmpty()) {
      sql += " AND c.USER_ID IN (:listUserIdDel)";
      parameters.put("listUserIdDel", woCdDTO.getListUserIdDel());
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoCdDTO.class));
  }

  @Override
  public ResultInSideDto insertWoCd(WoCdDTO woCdDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().persist(woCdDTO.toEntity());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateWoCd(WoCdDTO woCdDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().merge(woCdDTO.toEntity());
    resultInSideDto.setId(woCdDTO.getCdId());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteWoCd(Long cdId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    WoCdEntity entity = getEntityManager().find(WoCdEntity.class, cdId);
    getEntityManager().remove(entity);
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteWoCdByWoGroupId(Long woGroupId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    List<WoCdEntity> listEntity = findByMultilParam(WoCdEntity.class, "woGroupId", woGroupId);
    if (listEntity != null && listEntity.size() > 0) {
      for (WoCdEntity entity : listEntity) {
        getEntityManager().remove(entity);
      }
    }
    return resultInSideDto;
  }

  @Override
  public List<WoCdDTO> getListWoCdExport(WoCdDTO woCdDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD, "get-List-Wo-Cd-Export");
    Map<String, Object> parameters = new HashMap<>();
    WoCdGroupInsideDTO woCdGroupInsideDTO = woCdDTO.getWoCdGroupInsideDTO();
    if (woCdGroupInsideDTO != null) {
      if (StringUtils.isNotNullOrEmpty(woCdGroupInsideDTO.getSearchAll())) {
        sql += " AND (LOWER(g.WO_GROUP_NAME) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(g.WO_GROUP_CODE) LIKE :searchAll ESCAPE '\\')";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(woCdGroupInsideDTO.getSearchAll()));
      }
      if (StringUtils.isNotNullOrEmpty(woCdGroupInsideDTO.getWoGroupCode())) {
        sql += " AND LOWER(g.WO_GROUP_CODE) LIKE :woGroupCode ESCAPE '\\'";
        parameters
            .put("woGroupCode",
                StringUtils.convertLowerParamContains(woCdGroupInsideDTO.getWoGroupCode()));
      }
      if (StringUtils.isNotNullOrEmpty(woCdGroupInsideDTO.getWoGroupName())) {
        sql += " AND LOWER(g.WO_GROUP_NAME) LIKE :woGroupName ESCAPE '\\'";
        parameters
            .put("woGroupName",
                StringUtils.convertLowerParamContains(woCdGroupInsideDTO.getWoGroupName()));
      }
      if (woCdGroupInsideDTO.getGroupTypeId() != null) {
        sql += " AND g.GROUP_TYPE_ID = :groupTypeId";
        parameters.put("groupTypeId", woCdGroupInsideDTO.getGroupTypeId());
      }
      if (woCdGroupInsideDTO.getNationId() != null) {
        sql += " AND g.NATION_ID = :nationId";
        parameters.put("nationId", woCdGroupInsideDTO.getNationId());
      }
      if (StringUtils.isNotNullOrEmpty(woCdGroupInsideDTO.getEmail())) {
        sql += " AND LOWER(g.EMAIL) LIKE :email ESCAPE '\\'";
        parameters
            .put("email", StringUtils.convertLowerParamContains(woCdGroupInsideDTO.getEmail()));
      }
      if (StringUtils.isNotNullOrEmpty(woCdGroupInsideDTO.getMobile())) {
        sql += " AND LOWER(g.MOBILE) LIKE :mobile ESCAPE '\\'";
        parameters
            .put("mobile", StringUtils.convertLowerParamContains(woCdGroupInsideDTO.getMobile()));
      }
    }
    sql += " AND c.USER_ID IS NOT NULL AND c.WO_GROUP_ID IS NOT NULL";
    sql += " ORDER BY g.WO_GROUP_NAME, c.CD_ID DESC";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoCdDTO.class));
  }

  @Override
  public WoCdDTO checkWoCdExitBy2Id(Long woGroupId, Long userId) {
    List<WoCdEntity> dataEntity = (List<WoCdEntity>) findByMultilParam(
        WoCdEntity.class, "woGroupId", woGroupId, "userId", userId);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public List<UsersInsideDto> getListCdByGroup(Long woGroupId) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD, "get-List-Cd-By-Group");
    Map<String, Object> parameters = new HashMap<>();
    if (woGroupId != null) {
      sql += " AND c.wo_group_id = :woGroupId";
      parameters.put("woGroupId", woGroupId);
    }
    sql += " ORDER BY u.username";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
  }
}
