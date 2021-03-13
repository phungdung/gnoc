package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRHisDTO;
import com.viettel.gnoc.sr.model.SRHisEntity;
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
public class SRHisRepositoryImpl extends BaseRepository implements SRHisRepository {

  @Override
  public ResultInSideDto createSRHis(SRHisDTO srHisDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRHisEntity srHisEntity = getEntityManager().merge(srHisDTO.toEntity());
    resultInSideDto.setId(srHisEntity.getHisId());
    return resultInSideDto;
  }

  @Override
  public List<SRHisDTO> getListSRHisDTO(SRHisDTO dto, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    return onSearchEntity(SRHisEntity.class, dto, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<SRHisDTO> getListSRHisDTOBySrId(Long srId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_HIS, "getListSrHisDTOBySrId");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(srId)) {
      sqlQuery += (" AND T1.SR_ID = :srId ");
      parameters.put("srId", srId);
    }
    sqlQuery += " order by T1.CREATED_TIME DESC ";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRHisDTO.class));
  }
}
