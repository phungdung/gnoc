package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.WoTestServiceConfDTO;
import com.viettel.gnoc.commons.model.WoTestServiceConfEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class WoTestServiceConfRepositoryImpl extends BaseRepository implements
    WoTestServiceConfRepository {

  @Override
  public Datatable getListWoTestServiceConf(WoTestServiceConfDTO woTestServiceConfDTO) {
    BaseDto baseDto = sqlSearch(woTestServiceConfDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        woTestServiceConfDTO.getPage(), woTestServiceConfDTO.getPageSize(),
        WoTestServiceConfDTO.class,
        woTestServiceConfDTO.getSortName(), woTestServiceConfDTO.getSortType());
  }

  @Override
  public ResultInSideDto add(WoTestServiceConfDTO woTestServiceConfDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoTestServiceConfEntity woTestServiceConfEntity = getEntityManager()
        .merge(woTestServiceConfDTO.toEntity());
    resultInSideDto.setId(woTestServiceConfEntity.getId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto edit(WoTestServiceConfDTO woTestServiceConfDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(woTestServiceConfDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public WoTestServiceConfDTO getDetail(Long id) {
    WoTestServiceConfEntity woTestServiceConfEntity = getEntityManager()
        .find(WoTestServiceConfEntity.class, id);
    WoTestServiceConfDTO woTestServiceConfDTO = woTestServiceConfEntity.toDTO();
    return woTestServiceConfDTO;
  }


  public BaseDto sqlSearch(WoTestServiceConfDTO woTestServiceConfDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TEST_SERVICE_CONF, "getListWoTestServiceConf");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("leeLocale", I18n.getLocale());
    if (woTestServiceConfDTO != null) {
      if (StringUtils.isNotNullOrEmpty(woTestServiceConfDTO.getSearchAll())) {
        sqlQuery += " AND (lower(t.NAME) LIKE :searchAll ESCAPE '\\' OR lower(t.WO_CONTENT) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(woTestServiceConfDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(woTestServiceConfDTO.getName())) {
        sqlQuery += " AND (lower(t.NAME) LIKE :name ESCAPE '\\' )";
        parameters
            .put("name", StringUtils.convertLowerParamContains(woTestServiceConfDTO.getName()));
      }
      if (!StringUtils.isStringNullOrEmpty(woTestServiceConfDTO.getWoContent())) {
        sqlQuery += " AND (lower(t.WO_CONTENT) LIKE :woContent ESCAPE '\\' )";
        parameters.put("woContent",
            StringUtils.convertLowerParamContains(woTestServiceConfDTO.getWoContent()));
      }
      if (!StringUtils.isStringNullOrEmpty(woTestServiceConfDTO.getWoType())) {
        sqlQuery += " AND t.WO_TYPE = :woType ";
        parameters.put("woType", woTestServiceConfDTO.getWoType());
      }
      if (!StringUtils.isStringNullOrEmpty(woTestServiceConfDTO.getWoParentType())) {
        sqlQuery += " AND WO_PARENT_TYPE = :woParentType ";
        parameters.put("woParentType", woTestServiceConfDTO.getWoParentType());
      }
      if (!StringUtils.isStringNullOrEmpty(woTestServiceConfDTO.getWoPriority())) {
        sqlQuery += " AND t.WO_PRIORITY = :woPriority ";
        parameters.put("woPriority", woTestServiceConfDTO.getWoPriority());
      }
      if (!StringUtils.isStringNullOrEmpty(woTestServiceConfDTO.getCdId())) {
        sqlQuery += " AND t.CD_ID = :cdId ";
        parameters.put("cdId", woTestServiceConfDTO.getCdId());
      }
      if (!StringUtils.isStringNullOrEmpty(woTestServiceConfDTO.getDeltaTime1())) {
        sqlQuery += " AND t.DELTA_TIME1 = :deltaTime1 ";
        parameters.put("deltaTime1", woTestServiceConfDTO.getDeltaTime1());
      }
      if (!StringUtils.isStringNullOrEmpty(woTestServiceConfDTO.getDeltaTime2())) {
        sqlQuery += " AND t.DELTA_TIME2 = :deltaTime2 ";
        parameters.put("deltaTime2", woTestServiceConfDTO.getDeltaTime2());
      }
    }
    sqlQuery += " order by t.ID desc";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
