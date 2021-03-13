package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.WoFileTempDto;
import com.viettel.gnoc.commons.model.WoFileTempEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class WoFileTempRepositoryImpl extends BaseRepository implements WoFileTempRepository {

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public Datatable getListWoFileTemp(WoFileTempDto dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        dto.getPage(), dto.getPageSize(),
        WoFileTempDto.class,
        dto.getSortName(), dto.getSortType());
  }

  @Override
  public WoFileTempDto getDetail(Long woFileTempId) {
    WoFileTempEntity woFileTempEntity = getEntityManager()
        .find(WoFileTempEntity.class, woFileTempId);
    WoFileTempDto woFileTempDto = woFileTempEntity.toDTO();
    return woFileTempDto;
  }

  @Override
  public List<WoTypeInsideDTO> getListWoTypeCBB() {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_FILE_TEMP, "getListWoTypeCBB");
    Map<String, Object> parameters = new HashMap<>();
    String leeLocale = I18n.getLocale();
    parameters.put("leeLocale", leeLocale);
    List<WoTypeInsideDTO> lstWoType = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoTypeInsideDTO.class));
    return lstWoType;
  }

  @Override
  public ResultInSideDto insertWoFileTemp(WoFileTempDto woFileTempDto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoFileTempEntity woFileTempEntity = getEntityManager().merge(woFileTempDto.toEntity());
    resultInSideDto.setId(woFileTempEntity.getWoFileTempId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateWoFileTemp(WoFileTempDto dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoFileTempEntity woFileTempEntity = getEntityManager().merge(dto.toEntity());
    resultInSideDto.setId(woFileTempEntity.getWoFileTempId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteWoFileTempById(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoFileTempEntity woFileTempEntity = getEntityManager()
        .find(WoFileTempEntity.class, id);
    if (woFileTempEntity != null) {
      getEntityManager().remove(woFileTempEntity);
    }
    return resultInSideDto;
  }

  public BaseDto sqlSearch(WoFileTempDto woFileTempDto) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_FILE_TEMP, "getListWoFileTemp");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("leeLocale", I18n.getLocale());
    if (woFileTempDto != null) {
      if (StringUtils.isNotNullOrEmpty(woFileTempDto.getSearchAll())) {
        sqlQuery += " AND (lower(a.label) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(woFileTempDto.getSearchAll()));
      }
      if (woFileTempDto.getWoTypeId() != null) {
        sqlQuery += " AND a.WO_TYPE_ID= :woTypeId ";
        parameters.put("woTypeId", woFileTempDto.getWoTypeId());
      }
      if (!StringUtils.isStringNullOrEmpty(woFileTempDto.getLabel())) {
        sqlQuery += " AND (lower(a.label) LIKE :label ESCAPE '\\' )";
        parameters.put("label", StringUtils.convertLowerParamContains(woFileTempDto.getLabel()));
      }
      if (woFileTempDto.getNumberFile() != null) {
        sqlQuery += " AND a.NUMBER_FILE= :numberFile";
        parameters.put("numberFile", woFileTempDto.getNumberFile());
      }


    }
    sqlQuery += " order by a.WO_FILE_TEMP_ID desc";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
