package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatCfgClosedTicketDTO;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.CatCfgClosedTicketEntity;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.CFG_TIME_TROUBLE_PROCESS_MASTER_CODE;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
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
 * @author NamTN
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
public class CatCfgClosedTicketRepositoryImpl extends BaseRepository implements
    CatCfgClosedTicketRepository {

  @Override
  public List<CatCfgClosedTicketDTO> getListCatCfgClosedTicketDTO(
      CatCfgClosedTicketDTO catCfgClosedTicketDTO, int start, int maxResult, String sortType,
      String sortField) {
    return onSearchEntity(CatCfgClosedTicketEntity.class, catCfgClosedTicketDTO, start, maxResult,
        sortType, sortField);
  }

  @Override
  public Datatable getListCatCfgClosedTicket(CatCfgClosedTicketDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        dto.getPage(), dto.getPageSize(),
        CatCfgClosedTicketDTO.class,
        dto.getSortName(), dto.getSortType());
  }

  @Override
  public List<CatItemDTO> getListSubCategory(Long typeId) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_Cat_Cfg_Closed_Ticket, "getListSubCategory");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("ptSubCategory", CFG_TIME_TROUBLE_PROCESS_MASTER_CODE.ALARM_GROUP);
    if (typeId != null) {
      sql += " AND PARENT_ITEM_ID = :typeId";
      parameters.put("typeId", typeId);
    }
    sql += " ORDER BY a.ITEM_NAME asc";
    List<CatItemDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
        APPLIED_BUSSINESS.CAT_ITEM.toString());
    try {
      list = setLanguage(list, lstLanguage, Constants.ITEM_ID, Constants.ITEM_NAME);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  @Override
  public CatCfgClosedTicketDTO getDetail(Long id) {
    CatCfgClosedTicketEntity catCfgClosedTicketEntity = getEntityManager()
        .find(CatCfgClosedTicketEntity.class, id);
    CatCfgClosedTicketDTO catCfgClosedTicketDTO = catCfgClosedTicketEntity.toDTO();
    return catCfgClosedTicketDTO;
  }

  @Override
  public ResultInSideDto insert(CatCfgClosedTicketDTO catCfgClosedTicketDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CatCfgClosedTicketEntity catCfgClosedTicketEntity = getEntityManager()
        .merge(catCfgClosedTicketDTO.toEntity());
    resultInSideDto.setId(catCfgClosedTicketEntity.getId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto edit(CatCfgClosedTicketDTO catCfgClosedTicketDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(catCfgClosedTicketDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CatCfgClosedTicketEntity catCfgClosedTicketEntity = getEntityManager()
        .find(CatCfgClosedTicketEntity.class, id);
    if (catCfgClosedTicketEntity != null) {
      getEntityManager().remove(catCfgClosedTicketEntity);
    }
    return resultInSideDto;
  }

  @Override
  public CatCfgClosedTicketDTO checkExist(CatCfgClosedTicketDTO dto) {

    List<CatCfgClosedTicketEntity> dataEntity = (List<CatCfgClosedTicketEntity>) findByMultilParam(
        CatCfgClosedTicketEntity.class,
        "woTypeId", Long.valueOf(dto.getWoTypeId()),
        "typeId", Long.valueOf(dto.getTypeId()),
        "alarmGroupId", Long.valueOf(dto.getAlarmGroupId()));

    if (dataEntity != null && dataEntity.size() > 0 && dto.getId() != null) {
      if (dto.getId().equals(dataEntity.get(0).toDTO().getId())) {
        return null;
      }
      return dataEntity.get(0).toDTO();
    }
    if (dataEntity != null && dataEntity.size() > 0 && dto.getId() == null) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  public BaseDto sqlSearch(CatCfgClosedTicketDTO catCfgClosedTicketDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_Cat_Cfg_Closed_Ticket, "getListCatCfgClosedTicket");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("leeLocale", I18n.getLocale());
    if (catCfgClosedTicketDTO != null) {
//      if (StringUtils.isNotNullOrEmpty(woFileTempDto.getSearchAll())) {
//        sqlQuery += " AND (lower(T1.ROLE_CODE) LIKE :searchAll ESCAPE '\\' OR lower(T1.ROLE_NAME) LIKE :searchAll ESCAPE '\\' )";
//        parameters
//            .put("searchAll",
//                StringUtils.convertLowerParamContains(woFileTempDto.getSearchAll()));
//      }
      if (!StringUtils.isStringNullOrEmpty(catCfgClosedTicketDTO.getWoTypeId())) {
        sqlQuery += "AND a.wo_type_id = :woTypeId ";
        parameters.put("woTypeId", catCfgClosedTicketDTO.getWoTypeId());
      }
      if (!StringUtils.isStringNullOrEmpty(catCfgClosedTicketDTO.getTypeId())) {
        sqlQuery += " AND a.type_id = :typeId ";
        parameters.put("typeId", catCfgClosedTicketDTO.getTypeId());
      }
      if (!StringUtils.isStringNullOrEmpty(catCfgClosedTicketDTO.getAlarmGroupId())) {
        sqlQuery += " AND a.alarm_group_id= :alarmGroupId";
        parameters.put("alarmGroupId", catCfgClosedTicketDTO.getAlarmGroupId());
      }

//      parameters.put("p_leeLocale", I18n.getLocale());
    }
    sqlQuery += " order by a.LAST_UPDATE_TIME DESC";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
