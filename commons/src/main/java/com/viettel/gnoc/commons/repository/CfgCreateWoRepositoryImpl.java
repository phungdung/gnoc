package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CfgCreateWoDTO;
import com.viettel.gnoc.commons.dto.CfgCreateWoInsiteDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.CfgCreateWoEntity;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author TienNV
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
public class CfgCreateWoRepositoryImpl extends BaseRepository implements CfgCreateWoRepository {

  @Override
  public BaseDto sqlSearch(CfgCreateWoInsiteDTO cfgCreateWoDTO) {
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON,
        "getListCfgCreateWoDTOPage");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
//    if (StringUtils.isNotNullOrEmpty(cfgCreateWoDTO.getSearchAll())) {
//      sql += " AND (lower(b.itemName) LIKE :searchAll ESCAPE '\\')";
//      parameters
//          .put("searchAll",
//              StringUtils.convertLowerParamContains(cfgCreateWoDTO.getSearchAll()));
//    }
    if (cfgCreateWoDTO.getTypeId() != null && cfgCreateWoDTO.getTypeId() > 0) {
      sql += " and TYPE_ID =:typeId ";
      parameters.put("typeId", cfgCreateWoDTO.getTypeId());
    }
    if (cfgCreateWoDTO.getAlarmGroupId() != null && cfgCreateWoDTO.getAlarmGroupId() > 0) {
      sql += " and ALARM_GROUP_ID =:alarmGroupId ";
      parameters.put("alarmGroupId", cfgCreateWoDTO.getAlarmGroupId());
    }
    if (StringUtils.isNotNullOrEmpty(cfgCreateWoDTO.getTypeName())) {
      sql += " and lower(b.itemName) LIKE :typeName ESCAPE '\\' ";
      parameters
          .put("typeName", StringUtils.convertLowerParamContains(cfgCreateWoDTO.getTypeName()));
    }
    if (StringUtils.isNotNullOrEmpty(cfgCreateWoDTO.getAlarmGroupName())) {
      sql += " and lower(c.alarmGroupName) LIKE :alarmGroupName ESCAPE '\\' ";
      parameters.put("alarmGroupName",
          StringUtils.convertLowerParamContains(cfgCreateWoDTO.getAlarmGroupName()));
    }
    if (cfgCreateWoDTO.getId() != null && cfgCreateWoDTO.getId() > 0) {
      sql += " and a.ID =:id ";
      parameters.put("id", cfgCreateWoDTO.getId());
    }
    sql += " Order by a.LAST_UPDATE_TIME DESC ";
    baseDto.setSqlQuery(sql);
    baseDto.setPage(cfgCreateWoDTO.getPage());
    baseDto.setPageSize(cfgCreateWoDTO.getPageSize());
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public Datatable getListCfgCreateWoDTOPage(CfgCreateWoInsiteDTO cfgCreateWoDTO) {
    BaseDto baseDto = sqlSearch(cfgCreateWoDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        cfgCreateWoDTO.getPage(), cfgCreateWoDTO.getPageSize(), CfgCreateWoInsiteDTO.class,
        cfgCreateWoDTO.getSortName(), cfgCreateWoDTO.getSortType());
  }

  @Override
  public CfgCreateWoInsiteDTO getDetailById(Long id) {
    if (id == null || id == 0) {
      return null;
    }
    CfgCreateWoInsiteDTO dto = new CfgCreateWoInsiteDTO();
    dto.setId(id);
    BaseDto baseDto = sqlSearch(dto);
    List<CfgCreateWoInsiteDTO> lst = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(CfgCreateWoInsiteDTO.class));
    return lst.isEmpty() ? null : lst.get(0);
  }

  @Override
  public List<CfgCreateWoDTO> getListCfgCreateWoDTO(CfgCreateWoDTO cfgCreateWoDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(CfgCreateWoEntity.class, cfgCreateWoDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public ResultInSideDto deleteCfgCreateWo(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgCreateWoEntity createWoEntity = getEntityManager().find(CfgCreateWoEntity.class, id);
    getEntityManager().remove(createWoEntity);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto addOrEditCfgCreate(CfgCreateWoInsiteDTO cfgCreateWoDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgCreateWoEntity createWoEntity = getEntityManager().merge(cfgCreateWoDTO.toEntity());
    resultInSideDto.setId(createWoEntity.getId());
    return resultInSideDto;
  }
}
