package com.viettel.gnoc.cr.repository;


import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.GroupUnitDTO;
import com.viettel.gnoc.cr.model.GroupUnitEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class GroupUnitRepositoryImpl extends BaseRepository implements GroupUnitRepository {

  @Override
  public Datatable getListGroupUnitDTO(GroupUnitDTO groupUnitDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById("groupUnit", "getListGroupUnitDTO");
    String locale = I18n.getLocale();
    parameters.put("leeLocale", locale);
    if (!StringUtils.isStringNullOrEmpty(groupUnitDTO.getGroupUnitCode())) {
      sql += " and LOWER(re.groupUnitCode) like LOWER(:groupUnitCode) escape '\\'";
      parameters.put("groupUnitCode",
          StringUtils.convertLowerParamContains(groupUnitDTO.getGroupUnitCode()));
    }
    if (!StringUtils.isStringNullOrEmpty(groupUnitDTO.getGroupUnitName())) {
      sql += " and LOWER(re.groupUnitName) like LOWER(:groupUnitName) escape '\\'";
      parameters.put("groupUnitName",
          StringUtils.convertLowerParamContains(groupUnitDTO.getGroupUnitName()));
    }
    if (!StringUtils.isStringNullOrEmpty(groupUnitDTO.getSearchAll())) {
      sql += " and (LOWER(re.groupUnitName) like LOWER(:searchAll) escape '\\'";
      sql += " or LOWER(re.groupUnitCode) like LOWER(:searchAll) escape '\\')";
      parameters
          .put("searchAll", StringUtils.convertLowerParamContains(groupUnitDTO.getSearchAll()));
    }
    sql += " order by re.groupUnitId DESC";

    return getListDataTableBySqlQuery(sql, parameters, groupUnitDTO.getPage(),
        groupUnitDTO.getPageSize(),
        GroupUnitDTO.class, groupUnitDTO.getSortName(), groupUnitDTO.getSortType());
  }

  @Override
  public ResultInSideDto insertGroupUnit(GroupUnitDTO groupUnitDTO) {
    groupUnitDTO.setIsActive(1L);
    return insertByModel(groupUnitDTO.toEntity(), colId);
  }

  @Override
  public ResultInSideDto updateGroupUnit(GroupUnitDTO groupUnitDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(RESULT.SUCCESS);
    resultDto.setId(groupUnitDTO.getGroupUnitId());
    getEntityManager().merge(groupUnitDTO.toEntity());
    return resultDto;
  }

  @Override
  public GroupUnitDTO findGroupUnitById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(GroupUnitEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteGroupUnit(Long id) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(deleteById(GroupUnitEntity.class, id, colId));
    return resultDto;
  }

  @Override
  public ResultInSideDto deleteListGroupUnit(List<GroupUnitDTO> groupUnitDTOS) {
    ResultInSideDto resultDto = new ResultInSideDto();
    String result = deleteByListDTO(groupUnitDTOS, GroupUnitEntity.class, colId);
    resultDto.setKey(result);
    return resultDto;
  }

  @SuppressWarnings("JpaQlInspection")
  @Override
  public ResultInSideDto updateListGroupUnit(List<Long> ids) {
    ResultInSideDto resultDto = new ResultInSideDto();
    String sqlUpdate = "update GroupUnitEntity set isActive = 0 where groupUnitId in ( :group_unit_id )";
    Query query = getEntityManager().createQuery(sqlUpdate);
    query.setParameter("group_unit_id", ids);
    int result = query.executeUpdate();
    if (result > 0) {
      resultDto.setKey(RESULT.SUCCESS);
    } else {
      resultDto.setKey(RESULT.ERROR);
    }
    return resultDto;
  }

  @Override
  public List<GroupUnitDTO> getAllListGroupUnitDTO(GroupUnitDTO groupUnitDTO) {
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append("Select * from GROUP_UNIT a where a.IS_ACTIVE = 1");

    List<GroupUnitDTO> list = getNamedParameterJdbcTemplate().query(
        sql.toString(), parameters, BeanPropertyRowMapper.newInstance(GroupUnitDTO.class)
    );

    return list;
  }


  private static final String colId = "groupUnitId";
}
