package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CfgRequireHaveWoDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.CfgRequireHaveWoEntity;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.CatReasonDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgRequireHaveWoRepositoryImpl extends BaseRepository implements
    CfgRequireHaveWoRepository {

  @Override
  public List<CfgRequireHaveWoDTO> getListCfgRequireHaveWoDTO(
      CfgRequireHaveWoDTO cfgRequireHaveWoDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    return onSearchEntity(CfgRequireHaveWoEntity.class, cfgRequireHaveWoDTO, rowStart, maxRow,
        sortType,
        sortFieldList);
  }

  @Override
  public Datatable getListCfgRequireHaveWo(CfgRequireHaveWoDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        dto.getPage(), dto.getPageSize(),
        CfgRequireHaveWoDTO.class,
        dto.getSortName(), dto.getSortType());
  }

  @Override
  public CfgRequireHaveWoDTO getDetail(Long id) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_REQUIRE_HAVE_WO, "getDetail");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("id", id);
    parameters.put("leeLocale", I18n.getLocale());
    List<CfgRequireHaveWoDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(CfgRequireHaveWoDTO.class));

    if (list != null && !list.isEmpty()) {
      CfgRequireHaveWoDTO cfgRequireHaveWoDTO = list.get(0);
      return cfgRequireHaveWoDTO;
    }

    return null;
  }

  @Override
  public ResultInSideDto insert(CfgRequireHaveWoDTO dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgRequireHaveWoEntity cfgRequireHaveWoEntity = getEntityManager().merge(dto.toEntity());
    resultInSideDto.setId(cfgRequireHaveWoEntity.getId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto update(CfgRequireHaveWoDTO dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgRequireHaveWoEntity cfgRequireHaveWoEntity = getEntityManager().merge(dto.toEntity());
    resultInSideDto.setId(cfgRequireHaveWoEntity.getId());
    return resultInSideDto;
  }


  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgRequireHaveWoEntity cfgRequireHaveWoEntity = getEntityManager()
        .find(CfgRequireHaveWoEntity.class, id);
    if (cfgRequireHaveWoEntity != null) {
      getEntityManager().remove(cfgRequireHaveWoEntity);
    }
    return resultInSideDto;
  }

  @Override
  public List<CatReasonDTO> getReasonDTOForTree(CatReasonDTO catReasonDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_REQUIRE_HAVE_WO, "getListReasonForTree");
    Map<String, Object> parameters = new HashMap<>();
    if (catReasonDTO.getIsRoot() != null && catReasonDTO.getIsRoot()) {
      sql += " AND a.parent_id is null ";
    }
    if (!StringUtils.isStringNullOrEmpty(catReasonDTO.getTypeId())) {
      sql += "   AND a.type_id = :typeId ";
      parameters.put("typeId", catReasonDTO.getTypeId());

    }
    if (!StringUtils.isStringNullOrEmpty(catReasonDTO.getParentId())) {
      sql += "   AND a.parent_id = :parentId ";
      parameters.put("typeId", catReasonDTO.getParentId());
    }
    List<CatReasonDTO> lstReason = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CatReasonDTO.class));
    return lstReason;
  }

  public BaseDto sqlSearch(CfgRequireHaveWoDTO cfgRequireHaveWoDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_REQUIRE_HAVE_WO, "getListCfgRequireHaveWo");

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("leeLocale", I18n.getLocale());
    if (cfgRequireHaveWoDTO != null) {
//      if (StringUtils.isNotNullOrEmpty(woFileTempDto.getSearchAll())) {
//        sqlQuery += " AND (lower(T1.ROLE_CODE) LIKE :searchAll ESCAPE '\\' OR lower(T1.ROLE_NAME) LIKE :searchAll ESCAPE '\\' )";
//        parameters
//            .put("searchAll",
//                StringUtils.convertLowerParamContains(woFileTempDto.getSearchAll()));
//      }
      if (cfgRequireHaveWoDTO.getTypeId() != null) {
        sqlQuery += " AND a.TYPE_ID = :typeId  ";
        parameters.put("typeId", cfgRequireHaveWoDTO.getTypeId());

      }
      if (cfgRequireHaveWoDTO.getAlarmGroupId() != null) {
        sqlQuery += " AND a.ALARM_GROUP_ID = :alarmGroupId  ";
        parameters.put("alarmGroupId", cfgRequireHaveWoDTO.getAlarmGroupId());
      }
      if (cfgRequireHaveWoDTO.getReasonId() != null) {
        sqlQuery += " AND a.REASON_ID = :reasonId  ";
        parameters.put("reasonId", cfgRequireHaveWoDTO.getReasonId());
      }
      if (cfgRequireHaveWoDTO.getWoTypeId() != null) {
        sqlQuery += " AND a.WO_TYPE_ID = :woTypeId  ";
        parameters.put("woTypeId", cfgRequireHaveWoDTO.getWoTypeId());
      }

//      parameters.put("p_leeLocale", I18n.getLocale());
    }
    sqlQuery += " order by typeName desc";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
