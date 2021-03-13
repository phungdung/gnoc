package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.CfgFtOnTimeDTO;
import com.viettel.gnoc.wo.model.CfgFtOnTimeEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgFtOnTimeRepositoryImpl extends BaseRepository implements
    CfgFtOnTimeRepository {

  @Override
  public List<UsersInsideDto> getListUserByCdGroup(String cdGroupId) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CFG_FT_ONTIME, "get-user-by-cdgroup");
    Map<String, Object> params = new HashMap<>();
    params.put("cdGroupId", cdGroupId);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
  }

  @Override
  public Datatable onSearch(CfgFtOnTimeDTO cfgFtOnTimeDTO) {
    BaseDto baseDto = sqlSearch(cfgFtOnTimeDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        cfgFtOnTimeDTO.getPage(), cfgFtOnTimeDTO.getPageSize(),
        CfgFtOnTimeDTO.class, baseDto.getSortName(), baseDto.getSortType());
  }

  @Override
  public List<CfgFtOnTimeDTO> getListCfgFtOnTimeDTO(CfgFtOnTimeDTO cfgFtOnTimeDTO) {
    BaseDto baseDto = sqlSearch(cfgFtOnTimeDTO);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(CfgFtOnTimeDTO.class));
  }

  @Override
  public ResultInSideDto insertOrUpdate(CfgFtOnTimeDTO cfgFtOnTimeDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgFtOnTimeEntity cfgFtOnTimeEntity = getEntityManager()
        .merge(cfgFtOnTimeDTO.toEntity());
    resultInSideDto.setId(cfgFtOnTimeEntity.getId());
    return resultInSideDto;
  }

  BaseDto sqlSearch(CfgFtOnTimeDTO cfgFtOnTimeDTO) {
    Map<String, Object> param = new HashMap<>();
    BaseDto baseDto = new BaseDto();
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder
        .append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CFG_FT_ONTIME, "on-Search"));
    if (StringUtils.isNotNullOrEmpty(cfgFtOnTimeDTO.getFromDate())) {
      sqlBuilder.append(" and c.CFG_TIME >= to_date(:fromDate, 'dd/MM/YYYY HH24:mi:ss') ");
      param.put("fromDate", cfgFtOnTimeDTO.getFromDate());
    }
    if (StringUtils.isNotNullOrEmpty(cfgFtOnTimeDTO.getToDate())) {
      sqlBuilder.append(" and c.CFG_TIME <= to_date(:toDate, 'dd/MM/YYYY HH24:mi:ss') ");
      param.put("toDate", cfgFtOnTimeDTO.getToDate());
    }
    if (StringUtils.isNotNullOrEmpty(cfgFtOnTimeDTO.getCdId())) {
      sqlBuilder.append(" and c.cd_id =:cdId ");
      param.put("cdId", cfgFtOnTimeDTO.getCdId());
    }
    if (StringUtils.isNotNullOrEmpty(cfgFtOnTimeDTO.getBusinessId())) {
      sqlBuilder.append(" and c.BUSINESS_ID = :bussinessId ");
      param.put("bussinessId", cfgFtOnTimeDTO.getBusinessId());
    }

    if (StringUtils.isNotNullOrEmpty(cfgFtOnTimeDTO.getUserId())) {
      sqlBuilder.append(" and c.USER_ID = :userId ");
      param.put("userId", cfgFtOnTimeDTO.getUserId());
    }
    baseDto.setParameters(param);
    baseDto.setSqlQuery(sqlBuilder.toString());
    return baseDto;
  }

  @Override
  public CfgFtOnTimeDTO findById(Long id) {
    CfgFtOnTimeEntity entity = getEntityManager().find(CfgFtOnTimeEntity.class, id);
    if (entity != null) {
      return entity.toDTO();
    }
    return null;
  }

  @Override
  public boolean isDupplicate(CfgFtOnTimeDTO cfgFtOnTimeDTO) {
    CfgFtOnTimeEntity entity = cfgFtOnTimeDTO.toEntity();
    List<CfgFtOnTimeEntity> lst = findByMultilParam(CfgFtOnTimeEntity.class,
        "cdId", entity.getCdId(), "cfgTime", entity.getCfgTime(), "userId", entity.getUserId(),
        "businessId", entity.getBusinessId());
    if (lst == null || lst.isEmpty()) {
      return false;
    }
    if (cfgFtOnTimeDTO.getId() != null) {
      if (lst.get(0).getId() == cfgFtOnTimeDTO.getId()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CfgFtOnTimeEntity entity = getEntityManager()
        .find(CfgFtOnTimeEntity.class, id);
    getEntityManager().remove(entity);
    return resultInSideDto;
  }
}
