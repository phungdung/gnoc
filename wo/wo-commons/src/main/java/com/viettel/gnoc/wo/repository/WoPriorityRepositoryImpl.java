package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoPriorityDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.model.WoPriorityEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoPriorityRepositoryImpl extends BaseRepository implements WoPriorityRepository {

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  @Override
  public ResultInSideDto delete(Long priorityId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoPriorityEntity woPriorityEntity = getEntityManager().find(WoPriorityEntity.class, priorityId);
    getEntityManager().remove(woPriorityEntity);
    return resultInSideDto;
  }

  @Override
  public List<WoPriorityDTO> findAllByWoTypeID(Long woTypeId) {

    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_PRIORITY, "wo-priority");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    if (!StringUtils.isStringNullOrEmpty(woTypeId)) {
      sqlQuery += " AND p.WO_TYPE_ID= :woTypeId ";
      parameters.put("woTypeId", woTypeId);
    } else {
      sqlQuery += " AND p.WO_TYPE_ID IS NULL ";
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(WoPriorityDTO.class));

  }

  @Override
  public ResultInSideDto add(WoPriorityDTO woPriorityDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoPriorityEntity woPriorityEntity = getEntityManager().merge(woPriorityDTO.toEntity());
    resultInSideDto.setId(woPriorityEntity.getPriorityId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertList(WoTypeInsideDTO woTypeInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (WoPriorityDTO dto : woTypeInsideDTO.getWoPriorityDTOList()) {
      resultInSideDto = add(dto);
    }
    return resultInSideDto;
  }

  @Override
  public List<WoPriorityDTO> getListWoPriorityDTO(WoPriorityDTO woPriorityDTO) {
    if (woPriorityDTO != null) {
      List<WoPriorityDTO> list = onSearchEntity(WoPriorityEntity.class, woPriorityDTO, 0,
          Integer.MAX_VALUE, "", "priorityName");
      try {
        Map<String, Object> map = DataUtil
            .getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.WO,
                Constants.APPLIED_BUSSINESS.WO_PRIORITY, I18n.getLocale());
        String sqlLanguage = (String) map.get("sql");
        Map mapParam = (Map) map.get("mapParam");
        Map mapType = (Map) map.get("mapType");
        List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
            .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
        list = DataUtil.setLanguage(list, lstLanguage, "priorityId", "priorityName");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      return list;
    }
    return null;
  }
}

