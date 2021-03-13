package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoConfigPropertyDTO;
import com.viettel.gnoc.wo.model.WoConfigPropertyEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Slf4j
public class WoConfigPropertyRepositoryImpl extends BaseRepository implements
    WoConfigPropertyRepository {

  @Override
  public BaseDto sqlSearch(WoConfigPropertyDTO configPropertyDTO, String export) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = " select cp.KEY key,cp.VALUE value,cp.DESCRIPTION description from COMMON_GNOC.CONFIG_PROPERTY cp where 1=1 ";
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(configPropertyDTO.getSearchAll())) {
      sqlQuery += " AND (lower(cp.KEY) LIKE :searchAll ESCAPE '\\' ";
      sqlQuery += " OR lower(cp.VALUE) LIKE :searchAll ESCAPE '\\' ";
      sqlQuery += " OR lower(cp.DESCRIPTION) LIKE :searchAll ESCAPE '\\' ) ";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(configPropertyDTO.getSearchAll()));
    }
    if (StringUtils.isNotNullOrEmpty(configPropertyDTO.getKey())) {
      sqlQuery += " AND lower(cp.KEY) LIKE :KEY ESCAPE '\\' ";
      parameters.put("KEY", StringUtils.convertLowerParamContains(configPropertyDTO.getKey()));
    }
    if (StringUtils.isNotNullOrEmpty(configPropertyDTO.getValue())) {
      sqlQuery += " AND lower(cp.VALUE) LIKE :VALUE ESCAPE '\\' ";
      parameters.put("VALUE", StringUtils.convertLowerParamContains(configPropertyDTO.getValue()));
    }
    if (StringUtils.isNotNullOrEmpty(configPropertyDTO.getDescription())) {
      sqlQuery += " AND lower(cp.DESCRIPTION) LIKE :DESCRIPTION ESCAPE '\\' ";
      parameters.put("DESCRIPTION",
          StringUtils.convertLowerParamContains(configPropertyDTO.getDescription()));
    }
    baseDto.setParameters(parameters);
    baseDto.setSqlQuery(sqlQuery);
    return baseDto;
  }

  @Override
  public Datatable getListConfigPropertyDTO(WoConfigPropertyDTO configPropertyDTO) {
    BaseDto baseDto = sqlSearch(configPropertyDTO, "");
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        configPropertyDTO.getPage(),
        configPropertyDTO.getPageSize(), WoConfigPropertyDTO.class
        , configPropertyDTO.getSortName(), configPropertyDTO.getSortType());
  }

  @Override
  public ResultInSideDto addConfigProperty(WoConfigPropertyDTO configPropertyDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    WoConfigPropertyEntity entity = getEntityManager()
        .find(WoConfigPropertyEntity.class, configPropertyDTO.getKey());
    if (entity != null) {
      resultInSideDto.setKey(RESULT.DUPLICATE);
      return resultInSideDto;
    }
    getEntityManager().merge(configPropertyDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateConfigProperty(WoConfigPropertyDTO configPropertyDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(configPropertyDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(String key) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    WoConfigPropertyEntity entity = getEntityManager().find(WoConfigPropertyEntity.class, key);
    getEntityManager().remove(entity);
    return resultInSideDto;
  }

  @Override
  public WoConfigPropertyDTO getDetail(String key) {
    List<WoConfigPropertyEntity> lstEntity = findByMultilParam(WoConfigPropertyEntity.class, "key",
        key);
    return lstEntity.isEmpty() ? null : lstEntity.get(0).toDTO();
  }

  @Override
  public List<WoConfigPropertyDTO> getListDataExport(WoConfigPropertyDTO configPropertyDTO) {
    BaseDto baseDto = sqlSearch(configPropertyDTO, "");
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(WoConfigPropertyDTO.class));
  }

}
