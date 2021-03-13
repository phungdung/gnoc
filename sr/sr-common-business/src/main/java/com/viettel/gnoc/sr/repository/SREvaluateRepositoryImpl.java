package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.sr.dto.SREvaluateDTO;
import com.viettel.gnoc.sr.model.SREvaluateEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SREvaluateRepositoryImpl extends BaseRepository implements SREvaluateRepository {

  @Override
  public ResultInSideDto insertSREvaluate(SREvaluateDTO srEvaluateDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    SREvaluateEntity srEvaluateEntity = getEntityManager().merge(srEvaluateDTO.toEntity());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    resultInSideDto.setId(srEvaluateEntity.getEvaluateId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateSREvaluate(SREvaluateDTO srEvaluateDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    SREvaluateEntity srEvaluateEntity = getEntityManager().merge(srEvaluateDTO.toEntity());
    resultInSideDto.setId(srEvaluateEntity.getEvaluateId());
    return resultInSideDto;
  }

  @Override
  public List<SREvaluateDTO> findSREvaluateBySrId(Long srId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_EVALUATE, "findSREvaluateBySrId");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("srId", srId);
    sqlQuery += " order by a.create_date DESC ";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SREvaluateDTO.class));
  }
}
