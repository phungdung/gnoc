package com.viettel.gnoc.kedb.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.kedb.dto.KedbRatingInsideDTO;
import com.viettel.gnoc.kedb.model.KedbRatingEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class KedbRatingRepositoryImpl extends BaseRepository implements KedbRatingRepository {

  @Override
  public Datatable getListKedbRatingDTO(KedbRatingInsideDTO kedbRatingInsideDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_KEDB_RATING, "get-List-Kedb-Rating-DTO");
    return getListDataTableBySqlQuery(sql, null, kedbRatingInsideDTO.getPage(),
        kedbRatingInsideDTO.getPageSize(), KedbRatingInsideDTO.class,
        kedbRatingInsideDTO.getSortName(),
        kedbRatingInsideDTO.getSortType());
  }

  @Override
  public KedbRatingInsideDTO findKedbRatingById(Long id) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_KEDB_RATING, "find-Kedb-Rating-By-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("id", id);
    List<KedbRatingInsideDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(KedbRatingInsideDTO.class));
    if (list != null && !list.isEmpty()) {
      KedbRatingInsideDTO dto = list.get(0);
      return dto;
    }
    return null;
  }

  @Override
  public KedbRatingInsideDTO getKedbRating(KedbRatingInsideDTO kedbRatingInsideDTO) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_KEDB_RATING, "get-Kedb-Rating");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("kedbId", kedbRatingInsideDTO.getKedbId());
    List<KedbRatingInsideDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(KedbRatingInsideDTO.class));
    if (list != null && !list.isEmpty()) {
      KedbRatingInsideDTO dto = list.get(0);
      dto.setResult(RESULT.SUCCESS);
      return dto;
    }
    return null;
  }

  @Override
  public ResultInSideDto insertKedbRating(KedbRatingInsideDTO kedbRatingInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    KedbRatingEntity kedbRatingEntity = getEntityManager().merge(kedbRatingInsideDTO.toEntity());
    resultInSideDto.setId(kedbRatingEntity.getId());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateKedbRating(KedbRatingInsideDTO kedbRatingInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().merge(kedbRatingInsideDTO.toEntity());
    resultInSideDto.setId(kedbRatingInsideDTO.getId());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public List<KedbRatingEntity> findKedbRatingEntityByMultilParam(
      KedbRatingInsideDTO kedbRatingInsideDTO) {
    List<KedbRatingEntity> listEntity = (List<KedbRatingEntity>) findByMultilParam(
        KedbRatingEntity.class,
        "kedbId", kedbRatingInsideDTO.getKedbId(),
        "userName", kedbRatingInsideDTO.getUserName());
    return listEntity;
  }

  @Override
  public ResultInSideDto insertOrUpdateListKedbRating(
      List<KedbRatingInsideDTO> listKedbRatingInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (KedbRatingInsideDTO dto : listKedbRatingInsideDTO) {
      if (dto.getId() != null) {
        resultInSideDto = updateKedbRating(dto);
      } else {
        resultInSideDto = insertKedbRating(dto);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteKedbRating(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    KedbRatingEntity entity = getEntityManager().find(KedbRatingEntity.class, id);
    getEntityManager().remove(entity);
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public List<String> getSequenseKedbRating(String seqName, int... size) {
    int number = (size[0] > 0 ? size[0] : 1);
    return getListSequense(seqName, number);
  }

  @Override
  public List<KedbRatingInsideDTO> getListKedbRatingByCondition(List<ConditionBean> lstCondition,
      int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    return onSearchByConditionBean(new KedbRatingEntity(),
        lstCondition, rowStart, maxRow, sortType, sortFieldList);
  }
}
