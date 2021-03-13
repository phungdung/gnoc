package com.viettel.gnoc.cr.repository;


import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.GroupUnitDetailDTO;
import com.viettel.gnoc.cr.dto.GroupUnitDetailNameDTO;
import com.viettel.gnoc.cr.model.GroupUnitDetailEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class GroupUnitDetailRepositoryImpl extends BaseRepository implements
    GroupUnitDetailRepository {

  @Override
  public Datatable getListGroupUnitDetailDTO(GroupUnitDetailDTO dto) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = "select a.GROUP_UNIT_DETAIL_ID groupUnitDetailId,a.GROUP_UNIT_ID groupUnitId,a.UNIT_ID unitId from GROUP_UNIT_DETAIL a";
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(),
        dto.getPageSize(),
        GroupUnitDetailDTO.class, dto.getSortName(), dto.getSortType());
  }

  @Override
  public ResultInSideDto insertGroupUnitDetail(GroupUnitDetailDTO groupUnitDetailDTO) {
    return insertByModel(groupUnitDetailDTO.toEntity(), colId);
  }

  @Override
  public ResultInSideDto updateGroupUnitDetail(GroupUnitDetailDTO groupUnitDetailDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(groupUnitDetailDTO.toEntity());
    return resultDto;
  }

  @Override
  public ResultInSideDto deleteGroupUnitDetail(Long id) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(deleteById(GroupUnitDetailEntity.class, id, colId));
    return resultDto;
  }

  @Override
  public ResultInSideDto deleteListGroupUnitDetail(
      List<GroupUnitDetailDTO> groupUnitDetailListDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    String result = deleteByListDTO(groupUnitDetailListDTO, GroupUnitDetailEntity.class, colId);
    resultDto.setKey(result);
    return resultDto;
  }

  @Override
  public GroupUnitDetailDTO findById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(GroupUnitDetailEntity.class, id).toDTO();
    }
    return null;

  }

  @Override
  public Datatable getListUnitOfGroup(GroupUnitDetailNameDTO groupUnitDetailNameDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById("groupUnitDetail", "getListUnitOfGroup");
    String locale = I18n.getLocale();
    parameters.put("leeLocale", locale);
    if (!StringUtils.isStringNullOrEmpty(groupUnitDetailNameDTO.getUnitCode())) {
      sql += " and LOWER(re.unitcode) like LOWER(:unitcode) escape '\\'";
      parameters.put("unitcode",
          StringUtils.convertLowerParamContains(groupUnitDetailNameDTO.getUnitCode()));
    }
    if (!StringUtils.isStringNullOrEmpty(groupUnitDetailNameDTO.getUnitName())) {
      sql += " and LOWER(re.unitname) like LOWER(:unitname) escape '\\'";
      parameters.put("unitname",
          StringUtils.convertLowerParamContains(groupUnitDetailNameDTO.getUnitName()));
    }
    if (!StringUtils.isStringNullOrEmpty(groupUnitDetailNameDTO.getGroupUnitCode())) {
      sql += " and LOWER(re.groupunitcode) like LOWER(:groupunitcode) escape '\\'";
      parameters.put("groupunitcode",
          StringUtils.convertLowerParamContains(groupUnitDetailNameDTO.getGroupUnitCode()));

    }
    if (!StringUtils.isStringNullOrEmpty(groupUnitDetailNameDTO.getGroupUnitName())) {
      sql += " and LOWER(re.groupunitname) like LOWER(:groupunitname) escape '\\'";
      parameters.put("groupunitname",
          StringUtils.convertLowerParamContains(groupUnitDetailNameDTO.getGroupUnitName()));
    }
    if (!StringUtils.isStringNullOrEmpty(groupUnitDetailNameDTO.getSearchAll())) {
      sql += " and (LOWER(re.groupunitname) like LOWER(:searchAll) escape '\\'";
      sql += " or LOWER(re.groupunitcode) like LOWER(:searchAll) escape '\\')";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(groupUnitDetailNameDTO.getSearchAll()));
    }
    sql += " ORDER BY re.groupUnitDetailId DESC ";
    return getListDataTableBySqlQuery(sql, parameters, groupUnitDetailNameDTO.getPage(),
        groupUnitDetailNameDTO.getPageSize(),
        GroupUnitDetailNameDTO.class, groupUnitDetailNameDTO.getSortName(),
        groupUnitDetailNameDTO.getSortType());
  }

  @Override
  public Datatable getIDGroup(GroupUnitDetailDTO groupUnitDetailDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = "select group_unit_detail_id groupUnitDetailId from OPEN_PM.GROUP_UNIT_DETAIl where 1=1";
    if (!StringUtils.isStringNullOrEmpty(groupUnitDetailDTO.getGroupUnitId())) {
      sql += " and group_unit_id =:groupUnitId";
      parameters.put("groupUnitId", groupUnitDetailDTO.getGroupUnitId());
    }
    if (!StringUtils.isStringNullOrEmpty(groupUnitDetailDTO.getUnitId())) {
      sql += " and UNIT_ID =:unitId";
      parameters.put("unitId", groupUnitDetailDTO.getUnitId());
    }
    return getListDataTableBySqlQuery(sql, parameters, groupUnitDetailDTO.getPage(),
        groupUnitDetailDTO.getPageSize(),
        GroupUnitDetailDTO.class, groupUnitDetailDTO.getSortName(),
        groupUnitDetailDTO.getSortType());
  }

  @Override
  public GroupUnitDetailDTO getDetail(Long id) {
    return null;
  }

  @Override
  public List<GroupUnitDetailDTO> getAllListGroupUnitDetailDTO(GroupUnitDetailDTO dto) {
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append("Select * from GROUP_UNIT_DETAIL where 1=1 ");

    List<GroupUnitDetailDTO> list = getNamedParameterJdbcTemplate().query(
        sql.toString(), parameters, BeanPropertyRowMapper.newInstance(GroupUnitDetailDTO.class)
    );

    return list;
  }

  private static final String colId = "groupUnitDetailId";
}
