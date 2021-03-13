package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CfgFollowWorkTimeDTO;
import com.viettel.gnoc.cr.model.CfgFollowWorkTimeEntity;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgFollowWorkTimeRepositoryImpl extends BaseRepository implements
    CfgFollowWorkTimeRepository {


  @Override
  public Datatable getListCfgFollowWorkTimePage(CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO) {
    BaseDto baseDto = sqlSearch(cfgFollowWorkTimeDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        cfgFollowWorkTimeDTO.getPage(), cfgFollowWorkTimeDTO.getPageSize(),
        CfgFollowWorkTimeDTO.class,
        cfgFollowWorkTimeDTO.getSortName(), cfgFollowWorkTimeDTO.getSortType());
  }

  @Override
  public ResultInSideDto insertOrUpdate(CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgFollowWorkTimeEntity cfgFollowWorkTimeEntity = getEntityManager()
        .merge(cfgFollowWorkTimeDTO.toEntity());
    resultInSideDto.setId(cfgFollowWorkTimeEntity.getConfigFollowWorkTimeId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long configFollowWorkTimeId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgFollowWorkTimeEntity cfgFollowWorkTimeEntity = getEntityManager()
        .find(CfgFollowWorkTimeEntity.class, configFollowWorkTimeId);
    getEntityManager().remove(cfgFollowWorkTimeEntity);
    return resultInSideDto;
  }

  @Override
  public CfgFollowWorkTimeDTO getDetail(Long configFollowWorkTimeId) {
    CfgFollowWorkTimeEntity entity = getEntityManager()
        .find(CfgFollowWorkTimeEntity.class, configFollowWorkTimeId);
    CfgFollowWorkTimeDTO dto = entity.toDTO();
    return dto;
  }

  public BaseDto sqlSearch(CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_FOLLOW_WORK_TIME,
            "get-list-Cfg-Follow-Work-Time");
    Map<String, Object> parameters = new HashMap<>();
    if (cfgFollowWorkTimeDTO != null) {
      if (StringUtils.isNotNullOrEmpty(cfgFollowWorkTimeDTO.getSearchAll())) {
        sqlQuery += " AND ( LOWER(a.SYSTEM) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(cfgFollowWorkTimeDTO.getSearchAll()));
      }

      if (!StringUtils.isStringNullOrEmpty(cfgFollowWorkTimeDTO.getSystem())) {
        sqlQuery += " and LOWER(a.SYSTEM) like :system escape '\\' ";
        parameters.put("system",
            StringUtils.convertLowerParamContains(cfgFollowWorkTimeDTO.getSystem()));
      }
      if (!StringUtils.isStringNullOrEmpty(cfgFollowWorkTimeDTO.getCategoryId())) {
        sqlQuery += " and a.CATEGORY_ID = :categoryId ";
        parameters
            .put("categoryId", cfgFollowWorkTimeDTO.getCategoryId());
      }
      if (!StringUtils.isStringNullOrEmpty(cfgFollowWorkTimeDTO.getCatItemId())) {
        sqlQuery += " and a.CAT_ITEM_ID = :catItemId ";
        parameters
            .put("catItemId", cfgFollowWorkTimeDTO.getCatItemId());
      }
    }
    sqlQuery += " order by a.CONFIG_FOLLOW_WORKTIME_ID desc ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
