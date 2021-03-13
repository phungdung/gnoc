package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.WO_CD_GROUP_MASTER_CODE;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.CfgMapUnitGnocNimsDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.model.WoCdGroupEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoCdGroupRepositoryImpl extends BaseRepository implements WoCdGroupRepository {

  @Override
  public Datatable getListWoCdGroupDTO(WoCdGroupInsideDTO woCdGroupInsideDTO) {
    BaseDto baseDto = sqlGetListWoCdGroupDTO(woCdGroupInsideDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        woCdGroupInsideDTO.getPage(), woCdGroupInsideDTO.getPageSize(), WoCdGroupInsideDTO.class,
        woCdGroupInsideDTO.getSortName(), woCdGroupInsideDTO.getSortType());
    List<WoCdGroupInsideDTO> list = (List<WoCdGroupInsideDTO>) datatable.getData();
    list = setLanguageListWoCdGroup(list);
    datatable.setData(list);
    return datatable;
  }


  @Override
  public List<WoCdGroupInsideDTO> getListCdGroupByDTO(WoCdGroupInsideDTO woCdGroupDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_GROUP, "get-List-Cd-Group-By-DTO");
    Map<String, Object> parameters = new HashMap<>();

    if (!StringUtils.isStringNullOrEmpty(woCdGroupDTO.getWoGroupName())) {
      sql += " AND lower(a.wo_group_name) like :woGroupName escape '\\'";
      parameters
          .put("woGroupName", StringUtils.convertLowerParamContains(woCdGroupDTO.getWoGroupName()));
    }
    // ma nhom dieu phoi
    if (!StringUtils.isStringNullOrEmpty(woCdGroupDTO.getWoGroupCode())) {
      sql = sql + " AND lower(a.wo_group_code) like :woGroupCode escape '\\'";
      parameters
          .put("woGroupCode", StringUtils.convertLowerParamContains(woCdGroupDTO.getWoGroupCode()));
    }

    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));

  }

  @Override
  public WoCdGroupInsideDTO findWoCdGroupById(Long woCdGroupDTOId) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_GROUP, "get-List-Wo-Cd-Group-DTO");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    sql += " AND g.WO_GROUP_ID = :woGroupId";
    parameters.put("woGroupId", woCdGroupDTOId);
    List<WoCdGroupInsideDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));
    if (list != null && !list.isEmpty()) {
      list = setLanguageListWoCdGroup(list);
      WoCdGroupInsideDTO dto = list.get(0);
      return dto;
    }
    return null;
  }

  private List<WoCdGroupInsideDTO> setLanguageListWoCdGroup(List<WoCdGroupInsideDTO> list) {
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
        APPLIED_BUSSINESS.CAT_ITEM.toString());
    try {
      list = setLanguage(list, lstLanguage, "nationId", "nationName");
//      list = setLanguage(list, lstLanguage, "groupTypeId", "groupTypeName");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  @Override
  public ResultInSideDto insertWoCdGroup(WoCdGroupInsideDTO woCdGroupInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    WoCdGroupEntity woCdGroupEntity = getEntityManager().merge(woCdGroupInsideDTO.toEntity());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    if(woCdGroupEntity != null) {
      resultInSideDto.setId(woCdGroupEntity.getWoGroupId());
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateWoCdGroup(WoCdGroupInsideDTO woCdGroupInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().merge(woCdGroupInsideDTO.toEntity());
    resultInSideDto.setId(woCdGroupInsideDTO.getWoGroupId());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteWoCdGroup(Long woGroupId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    WoCdGroupEntity entity = getEntityManager().find(WoCdGroupEntity.class, woGroupId);
    getEntityManager().remove(entity);
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public List<WoCdGroupInsideDTO> getListCdGroupByUser(WoCdGroupTypeUserDTO woCdGroupTypeUserDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_GROUP, "get-List-Cd-Group-By-User");
    Map<String, Object> parameters = new HashMap<>();
    if (woCdGroupTypeUserDTO != null) {
      if (woCdGroupTypeUserDTO.getWoTypeId() != null) {
        sql += " AND g.WO_GROUP_ID in (SELECT WO_GROUP_ID FROM wo_type_group WHERE wo_type_id = :woTypeId)";
        parameters.put("woTypeId", woCdGroupTypeUserDTO.getWoTypeId());
      }
      if (woCdGroupTypeUserDTO.getUserId() != null) {
        sql += " AND g.WO_GROUP_ID in (SELECT WO_GROUP_ID FROM WO_CD WHERE USER_ID = :userId)";
        parameters.put("userId", woCdGroupTypeUserDTO.getUserId());
      }
      if (woCdGroupTypeUserDTO.getGroupTypeId() != null) {
        sql += " AND g.group_type_id = :groupTypeId";
        parameters.put("groupTypeId", woCdGroupTypeUserDTO.getGroupTypeId());
      }
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));
  }

  @Override
  public List<WoCdGroupInsideDTO> getListWoCdGroupExport(WoCdGroupInsideDTO woCdGroupInsideDTO) {
    BaseDto baseDto = sqlGetListWoCdGroupDTO(woCdGroupInsideDTO);
    List<WoCdGroupInsideDTO> list = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));
    list = setLanguageListWoCdGroup(list);
    return list;
  }

  @Override
  public WoCdGroupInsideDTO checkWoCdGroupExit(WoCdGroupInsideDTO woCdGroupInsideDTO) {
    List<WoCdGroupEntity> dataEntity = (List<WoCdGroupEntity>) findByMultilParam(
        WoCdGroupEntity.class, "woGroupCode", woCdGroupInsideDTO.getWoGroupCode());
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto updateStatusCdGroup(WoCdGroupInsideDTO woCdGroupInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    WoCdGroupEntity woCdGroupEntity = getEntityManager()
        .find(WoCdGroupEntity.class, woCdGroupInsideDTO.getWoGroupId());
    woCdGroupEntity.setIsEnable(woCdGroupInsideDTO.getIsEnable());
    getEntityManager().merge(woCdGroupEntity);
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  public BaseDto sqlGetListWoCdGroupDTO(WoCdGroupInsideDTO woCdGroupInsideDTO) {
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_GROUP, "get-List-Wo-Cd-Group-DTO");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    parameters.put("categoryCode", WO_CD_GROUP_MASTER_CODE.WO_CD_GROUP_TYPE);
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
    if (woCdGroupInsideDTO.getWoGroupId() != null) {
      sql += " AND g.WO_GROUP_ID = :woGroupId ";
      parameters.put("woGroupId", woCdGroupInsideDTO.getWoGroupId().toString());
    }
    if (StringUtils.isNotNullOrEmpty(woCdGroupInsideDTO.getWoGroupName())) {
      sql += " AND LOWER(g.WO_GROUP_NAME) LIKE :woGroupName ESCAPE '\\'";
      parameters
          .put("woGroupName",
              StringUtils.convertLowerParamContains(woCdGroupInsideDTO.getWoGroupName()));
    }
    if (woCdGroupInsideDTO.getGroupTypeId() != null) {
      sql += " AND llex.ITEM_VALUE = :groupTypeId";
      parameters.put("groupTypeId", woCdGroupInsideDTO.getGroupTypeId().toString());
    }
    if (woCdGroupInsideDTO.getNationId() != null) {
      sql += " AND g.NATION_ID = :nationId";
      parameters.put("nationId", woCdGroupInsideDTO.getNationId());
    }
    if (StringUtils.isNotNullOrEmpty(woCdGroupInsideDTO.getEmail())) {
      sql += " AND LOWER(g.EMAIL) LIKE :email ESCAPE '\\'";
      parameters.put("email", StringUtils.convertLowerParamContains(woCdGroupInsideDTO.getEmail()));
    }
    if (StringUtils.isNotNullOrEmpty(woCdGroupInsideDTO.getMobile())) {
      sql += " AND LOWER(g.MOBILE) LIKE :mobile ESCAPE '\\'";
      parameters
          .put("mobile", StringUtils.convertLowerParamContains(woCdGroupInsideDTO.getMobile()));
    }
    if (woCdGroupInsideDTO.getIsEnable() != null) {
      sql += " AND g.IS_ENABLE = :isEnable";
      parameters.put("isEnable", woCdGroupInsideDTO.getIsEnable());
    }
    if (woCdGroupInsideDTO.getWoTypeId() != null) {
      sql += " AND g.WO_GROUP_ID in ( SELECT WO_GROUP_ID FROM wo_type_group WHERE wo_type_id = :wo_type_id )";
      parameters.put("wo_type_id", woCdGroupInsideDTO.getWoTypeId());
    }
    sql += " ORDER BY g.WO_GROUP_ID DESC";
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public List<WoCdGroupInsideDTO> getListCdGroup(String userName) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_GROUP, "get-List-Cd-Group");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("userName", userName.toLowerCase());

    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));
  }

  @Override
  public List<WoCdGroupInsideDTO> getListGroupDispatch(Long woTypeId, Long groupTypeId,
      String locale) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_GROUP, "get-list-group-dispatch");
    Map<String, Object> parameters = new HashMap<>();
    if (woTypeId != null) {
      sql += " and wo.WO_GROUP_ID in (select WO_GROUP_ID from WO_TYPE_GROUP a where "
          + "a.wo_type_id =:p_wo_type_id) ";
      parameters.put("p_wo_type_id", woTypeId);
    }
    if (groupTypeId != null) {
      sql += " and wo.GROUP_TYPE_ID =:p_group_type_id ";
      parameters.put("p_group_type_id", groupTypeId);
    }
    sql += " order by wo.WO_GROUP_NAME asc ";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));
  }

  @Override
  public UnitDTO getUnitCodeMapNims(String unitNimsCode, String businessName) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_GROUP, "get-unit-code-map-nims");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_unitNimsCode", unitNimsCode);
    parameters.put("p_businessName", businessName);
    List<CfgMapUnitGnocNimsDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CfgMapUnitGnocNimsDTO.class));

    if (lst != null && lst.size() > 0) {
      CfgMapUnitGnocNimsDTO o = lst.get(0);
      if (!StringUtils.isStringNullOrEmpty(o.getUnitGnocCode())) {
        return getUnitByUnitCode(o.getUnitGnocCode());
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  @Override
  public UnitDTO getUnitByUnitCode(String unitCode) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_GROUP, "get-unit-by-unit-code");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_unit_code", unitCode);
    List<UnitDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(UnitDTO.class));
    if (lst != null && lst.size() > 0) {
      return lst.get(0);
    } else {
      return null;
    }
  }

  @Override
  public WoCdGroupInsideDTO getCdByUnitCode(String unitCode, String woTypeId, String cdGroupType) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_GROUP, "get-cd-by-unit-code");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(cdGroupType)) {
      sql += " and wo.GROUP_TYPE_ID=:p_group_type_id ";
      parameters.put("p_group_type_id", cdGroupType);
    }
    sql += "  and wo.wo_group_id in (select distinct cd_group_id from wfm.wo_cd_group_unit "
        + "  where unit_id in (select unit_id from common_gnoc.unit where lower(unit_code) =  lower(:p_unit_code )))";
    parameters.put("p_unit_code", unitCode);
    if (!StringUtils.isStringNullOrEmpty(woTypeId)) {
      sql += " and wo.wo_group_id in (select distinct wo_group_id from wfm.wo_type_group t  "
          + " where t.wo_type_id =:p_wo_type_id ) ";
      parameters.put("p_wo_type_id", woTypeId);
    }
    List<WoCdGroupInsideDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));

    return lst.isEmpty() ? null : lst.get(0);
  }

  @Override
  public List<WoCdGroupInsideDTO> getListCdGroupByUserDTO(WoCdGroupInsideDTO woCdGroupInsideDTO,
      Long woTypeId,
      Long groupTypeId, Long userId, String locale) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_GROUP, "get-cd-by-unit-code");
    Map<String, Object> parameters = new HashMap<>();
    if (woTypeId != null) {
      sql += " AND wo.WO_GROUP_ID in (SELECT WO_GROUP_ID FROM wo_type_group WHERE wo_type_id =:p_wo_type_id ) ";
      parameters.put("p_wo_type_id", woTypeId);
    }

    if (userId != null) {
      sql += " AND wo.WO_GROUP_ID in (SELECT WO_GROUP_ID FROM WO_CD WHERE USER_ID =:p_user_id ) ";
      parameters.put("p_user_id", userId);
    }
    if (groupTypeId != null) {
      sql += " AND wo.group_type_id =:p_group_type_id ";
      parameters.put("p_group_type_id", groupTypeId);
    }
    if (woCdGroupInsideDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(woCdGroupInsideDTO.getWoGroupId())) {
        sql += " AND wo.WO_GROUP_ID =:p_wo_group_id ";
        parameters.put("p_wo_group_id", woCdGroupInsideDTO.getWoGroupId());
      }
      if (!StringUtils.isStringNullOrEmpty(woCdGroupInsideDTO.getWoGroupName())) {
        sql += " AND lower(wo.wo_group_name) like '%'|| lower(:p_wo_group_name) ||'%' escape '\\'";
        parameters.put("p_wo_group_name", woCdGroupInsideDTO.getWoGroupName());
      }
      if (!StringUtils.isStringNullOrEmpty(woCdGroupInsideDTO.getWoGroupCode())) {
        sql += " AND lower(wo.wo_group_code) like '%'|| lower(:p_wo_group_code) ||'%' escape '\\' ";
        parameters.put("p_wo_group_code", woCdGroupInsideDTO.getWoGroupCode());
      }
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));
  }

  @Override
  public WoCdGroupInsideDTO getWoCdGroupWoByCdGroupCode(String woGroupCode) {
    if (woGroupCode == null) {
      return null;
    }
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_GROUP, "get-cd-by-unit-code");
    Map<String, Object> parameters = new HashMap<>();

    sql += " and lower(wo.WO_GROUP_CODE) = :p_wo_group_code";
    parameters.put("p_wo_group_code", woGroupCode.toLowerCase());

    List<WoCdGroupInsideDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));
    return lst.isEmpty() ? null : lst.get(0);
  }

  @Override
  public List<WoCdGroupInsideDTO> getListWoCdGroupActive(WoCdGroupInsideDTO woCdGroupInsideDTO,
      int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(WoCdGroupEntity.class, woCdGroupInsideDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public List<UsersInsideDto> getListFtByUser(String userId, String keyword, int rowStart,
      int maxRow) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_GROUP, "get-list-ft-by-user");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("userId", Long.valueOf(userId));
    parameters.put("keyword", StringUtils.convertLowerParamContains(keyword));
    List<UsersInsideDto> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
    list = (List<UsersInsideDto>) DataUtil.subPageListFromTo(list, rowStart, rowStart + maxRow);
    return list;
  }

  @Override
  public List<WoCdGroupInsideDTO> getListCdGroupByCodeNotLike(String cdGroupCode) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_GROUP, "get-List-Cd-Group-By-Code-Not-Like");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("woGroupCode", cdGroupCode.toLowerCase());
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));
  }

  @Override
  public List<WoCdGroupInsideDTO> getListWoCdGroupDTO(WoCdGroupInsideDTO woCdGroupInsideDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(WoCdGroupEntity.class, woCdGroupInsideDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }

}
