package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.SR_STATUS;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRChildAutoDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.sr.model.SRChildAutoEntity;
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
public class SRChildAutoRepositoryImpl extends BaseRepository implements SRChildAutoRepository {

  @Override
  public ResultInSideDto insertOrUpdateSRChildAuto(SRChildAutoDTO srChildAutoDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    SRChildAutoEntity entity = getEntityManager().merge(srChildAutoDTO.toEntity());
    resultInSideDto.setId(entity.getId());
    return resultInSideDto;
  }

  @Override
  public List<SRChildAutoDTO> getListSRChildCheckClosed(String srCode) {
    if (StringUtils.isNotNullOrEmpty(srCode)) {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_CHILD_AUTO, "getListSRChildCheckClosed");
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("srCode", srCode);
      parameters.put("status", SR_STATUS.CONCLUDED);
      return getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRChildAutoDTO.class));
    }
    return null;
  }

  @Override
  public List<SrInsiteDTO> getListSRChildAutoByGennerateNo(String srParentCode, Long generateNo) {
    if (StringUtils.isNotNullOrEmpty(srParentCode) && generateNo != null && generateNo > 0L) {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_CHILD_AUTO, "getListSRChildAutoByGennerateNo");
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("srParentCode", srParentCode);
      parameters.put("generateNo", generateNo);
      return getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(SrInsiteDTO.class));
    }
    return null;
  }

  @Override
  public SRChildAutoDTO getDetailSRChildAuto(Long id) {
    if (id != null && id > 0) {
      SRChildAutoEntity entity = getEntityManager().find(SRChildAutoEntity.class, id);
      if (entity != null && entity.getId() != null && entity.getId() > 0) {
        return entity.toDTO();
      }
    }
    return null;
  }
}
