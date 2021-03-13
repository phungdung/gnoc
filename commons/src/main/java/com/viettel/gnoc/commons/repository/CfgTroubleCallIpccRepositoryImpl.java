package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CfgTroubleCallIpccDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.CfgTroubleCallIpccEntity;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
@Transactional
public class CfgTroubleCallIpccRepositoryImpl extends BaseRepository implements
    CfgTroubleCallIpccRepository {

  @Override
  public BaseDto sqlSearch(CfgTroubleCallIpccDTO cfgTroubleCallIpccDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "getListCfgTroubleCallIpccDTO");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(cfgTroubleCallIpccDTO.getSearchAll())) {
      sql += " AND (lower(a.receive_user_name) LIKE :searchAll ESCAPE '\\' ) ";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(cfgTroubleCallIpccDTO.getSearchAll()));
    }
    if (cfgTroubleCallIpccDTO.getId() != null && cfgTroubleCallIpccDTO.getId() > 0) {
      sql += " and a.ID =:id ";
      parameters.put("id", cfgTroubleCallIpccDTO.getId());
    }
    if (cfgTroubleCallIpccDTO.getCfgId() != null && cfgTroubleCallIpccDTO.getCfgId() > 0) {
      sql += " and a.CFG_ID  =:cfgId ";
      parameters.put("cfgId", cfgTroubleCallIpccDTO.getCfgId());
    }
    if (cfgTroubleCallIpccDTO.getLevelCall() != null && cfgTroubleCallIpccDTO.getLevelCall() > 0) {
      sql += " and a.LEVEL_CALL  =:levelCall ";
      parameters.put("levelCall", cfgTroubleCallIpccDTO.getLevelCall());
    }
    if (cfgTroubleCallIpccDTO.getTimeProcess() != null
        && cfgTroubleCallIpccDTO.getTimeProcess() > 0) {
      sql += " and a.TIME_PROCESS =:timeProcess ";
      parameters.put("timeProcess", cfgTroubleCallIpccDTO.getTimeProcess());
    }
    if (StringUtils.isNotNullOrEmpty(cfgTroubleCallIpccDTO.getReceiveUserName())) {
      sql += " and LOWER(a.receive_user_name) like :receiveUserName ESCAPE '\\' ";
      parameters.put("receiveUserName",
          StringUtils.convertLowerParamContains(cfgTroubleCallIpccDTO.getReceiveUserName()));
    }
    if (StringUtils.isNotNullOrEmpty(cfgTroubleCallIpccDTO.getFileName())) {
      sql += " and LOWER(a.FILE_NAME) like :fileName ESCAPE '\\' ";
      parameters.put("fileName",
          StringUtils.convertLowerParamContains(cfgTroubleCallIpccDTO.getFileName()));
    }
    sql += " order by a.ID DESC ";
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    baseDto.setPage(cfgTroubleCallIpccDTO.getPage());
    baseDto.setPageSize(cfgTroubleCallIpccDTO.getPageSize());
    return baseDto;
  }

  @Override
  public Datatable getListCfgTroubleCallIpccDTO(CfgTroubleCallIpccDTO cfgTroubleCallIpccDTO) {
    BaseDto baseDto = sqlSearch(cfgTroubleCallIpccDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        cfgTroubleCallIpccDTO.getPage(), cfgTroubleCallIpccDTO.getPageSize(),
        CfgTroubleCallIpccDTO.class, cfgTroubleCallIpccDTO.getSortName(),
        cfgTroubleCallIpccDTO.getSortType());
  }

  @Override
  public CfgTroubleCallIpccDTO getDetailById(Long id) {
    CfgTroubleCallIpccDTO cfgTroubleCallIpccDTO = new CfgTroubleCallIpccDTO();
    cfgTroubleCallIpccDTO.setId(id);
    BaseDto baseDto = sqlSearch(cfgTroubleCallIpccDTO);
    List<CfgTroubleCallIpccDTO> lst = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(CfgTroubleCallIpccDTO.class));
    return lst.isEmpty() ? null : lst.get(0);
  }

  @Override
  public ResultInSideDto insertOrUpdateCfgTroubleCallIpcc(
      CfgTroubleCallIpccDTO cfgTroubleCallIpccDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgTroubleCallIpccEntity entity = getEntityManager().merge(cfgTroubleCallIpccDTO.toEntity());
    resultInSideDto.setId(entity.getId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteCfgTroubleCallIpcc(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgTroubleCallIpccEntity entity = getEntityManager().find(CfgTroubleCallIpccEntity.class, id);
    getEntityManager().remove(entity);
    resultInSideDto.setId(id);
    return resultInSideDto;
  }
}
