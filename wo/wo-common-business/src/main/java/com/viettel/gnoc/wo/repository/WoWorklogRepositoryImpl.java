package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoWorklogInsideDTO;
import com.viettel.gnoc.wo.model.WoWorklogEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoWorklogRepositoryImpl extends BaseRepository implements WoWorklogRepository {

  @Override
  public ResultInSideDto insertWoWorklog(WoWorklogInsideDTO woWorklogInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    WoWorklogEntity woWorklogEntity = getEntityManager().merge(woWorklogInsideDTO.toEntity());
    resultInSideDto.setId(woWorklogEntity.getWoWorklogId());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    resultInSideDto.setMessage(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public Datatable getListWorklogByWoIdPaging(WoWorklogInsideDTO woWorklogInsideDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_WORKLOG, "get-List-Worklog-By-Wo-Id-Paging");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("offset", woWorklogInsideDTO.getOffset());
    parameters.put("woId", woWorklogInsideDTO.getWoId());
    return getListDataTableBySqlQuery(sql, parameters, woWorklogInsideDTO.getPage(),
        woWorklogInsideDTO.getPageSize(), WoWorklogInsideDTO.class,
        woWorklogInsideDTO.getSortName(),
        woWorklogInsideDTO.getSortType());
  }

  @Override
  public List<WoWorklogInsideDTO> getListDataByWoId(Long woId) {
    String sql = "SELECT  w.* FROM WFM.WO_WORKLOG w WHERE w.WO_ID = :woId ";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("woId", woId);
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoWorklogInsideDTO.class));
  }

  @Override
  public List<WoWorklogInsideDTO> getListDataByWoIdPaging(String woId, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_WORKLOG,
            "get-List-Worklog-By-Wo-Id-Paging-Service");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(woId)) {
      sql += "and w.WO_ID = :woId ";
      parameters.put("woId", Long.valueOf(woId));
    }
    if (sortFieldList != null) {
      sql += " order by w.UPDATE_TIME desc";
    }
    Query query = getEntityManager().createNativeQuery(sql);
    query.unwrap(SQLQuery.class).
        addScalar("woWorklogId", new LongType()).
        addScalar("woId", new LongType()).
        addScalar("woWorklogContent", new StringType()).
        addScalar("woSystem", new StringType()).
        addScalar("woSystemId", new StringType()).
        addScalar("userId", new LongType()).
        addScalar("username", new StringType()).
        addScalar("updateTime", new DateType()).
        addScalar("nation", new StringType())
        .setResultTransformer(Transformers.aliasToBean(WoWorklogInsideDTO.class));
    if (!parameters.isEmpty()) {
      for (Map.Entry<String, Object> map : parameters.entrySet()) {
        query.setParameter(map.getKey(), map.getValue());
      }
    }
    query.setFirstResult(rowStart);
    query.setMaxResults(maxRow);

    return query.getResultList();
  }
}
