package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoUpdateServiceInfraDTO;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoUpdateServiceInfraRepositoryImpl extends BaseRepository implements
    WoUpdateServiceInfraRepository {


  @Override
  public Datatable getListWoUpdateServiceInfraPage(
      WoUpdateServiceInfraDTO woUpdateServiceInfraDTO) {
    BaseDto baseDto = sqlSearch(woUpdateServiceInfraDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        woUpdateServiceInfraDTO.getPage(), woUpdateServiceInfraDTO.getPageSize(),
        WoUpdateServiceInfraDTO.class,
        woUpdateServiceInfraDTO.getSortName(), woUpdateServiceInfraDTO.getSortType());
  }

  public BaseDto sqlSearch(WoUpdateServiceInfraDTO woUpdateServiceInfraDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO_UPDATE_SERVICE_INFRA,
        "wo-Update-Service-Infra-getList");
    Map<String, Object> parameters = new HashMap<>();
    if (woUpdateServiceInfraDTO != null) {
      if (StringUtils.isNotNullOrEmpty(woUpdateServiceInfraDTO.getSearchAll())) {
        sqlQuery += " AND (lower(a.WO_CODE) LIKE :searchAll ESCAPE '\\' OR lower(b.ACCOUNT_ISDN) LIKE :searchAll ESCAPE '\\')";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(woUpdateServiceInfraDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(woUpdateServiceInfraDTO.getWoCode())) {
        sqlQuery += " AND (lower(a.WO_CODE) LIKE :woCode ESCAPE '\\')";
        parameters.put("woCode",
            StringUtils.convertLowerParamContains(woUpdateServiceInfraDTO.getWoCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(woUpdateServiceInfraDTO.getAccountIsdn())) {
        sqlQuery += " AND (lower(b.ACCOUNT_ISDN) LIKE :accountIsdn ESCAPE '\\')";
        parameters.put("accountIsdn",
            StringUtils.convertLowerParamContains(woUpdateServiceInfraDTO.getAccountIsdn()));
      }

      parameters.put("rejectCd", I18n.getLanguage("wo.status.REJECT_CD"));
      parameters.put("closedCd", I18n.getLanguage("wo.status.CLOSED_CD"));
      parameters.put("p_leeLocale", I18n.getLocale());
    }
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

}
