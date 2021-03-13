package com.viettel.gnoc.kedb.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.kedb.dto.KedbActionLogsDTO;
import com.viettel.gnoc.kedb.model.KedbActionLogsEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class KedbActionLogsRepositoryImpl extends
    BaseRepository<KedbActionLogsDTO, KedbActionLogsEntity> implements KedbActionLogsRepository {

  @Override
  public List<KedbActionLogsDTO> getListKedbActionLogsDTO(KedbActionLogsDTO kedbActionLogsDTO) {
    return onSearchEntity(KedbActionLogsEntity.class, kedbActionLogsDTO,
        kedbActionLogsDTO.getPage(), kedbActionLogsDTO.getPageSize(),
        kedbActionLogsDTO.getSortType(), kedbActionLogsDTO.getSortName());
  }

  @Override
  public List<KedbActionLogsDTO> getListKedbActionLogsByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new KedbActionLogsEntity(), lstCondition, rowStart, maxRow,
        sortType, sortFieldList);
  }

  @Override
  public List<String> getSequenseKedbActionLogs(String seqName, int... size) {
    return getListSequense(seqName, size);
  }

  @Override
  public String updateKedbActionLogs(KedbActionLogsDTO kedbActionLogsDTO) {
    KedbActionLogsEntity entity = kedbActionLogsDTO.toEntity();
    getEntityManager().merge(entity);
    return RESULT.SUCCESS;
  }

  @Override
  public ResultInSideDto insertKedbActionLogs(KedbActionLogsDTO kedbActionLogsDTO) {
    return insertByModel(kedbActionLogsDTO.toEntity(), colId);
  }

  @Override
  public String insertOrUpdateListKedbActionLogs(List<KedbActionLogsDTO> kedbActionLogsDTO) {
    for (KedbActionLogsDTO item : kedbActionLogsDTO) {
      KedbActionLogsEntity entity = item.toEntity();
      if (entity.getId() != null && entity.getId() > 0) {
        getEntityManager().merge(entity);
      } else {
        getEntityManager().persist(entity);
      }
    }
    return RESULT.SUCCESS;
  }

  @Override
  public KedbActionLogsDTO findKedbActionLogsById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(KedbActionLogsEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public String deleteKedbActionLogs(Long id) {
    return deleteById(KedbActionLogsEntity.class, id, colId);
  }

  @Override
  public String deleteListKedbActionLogs(List<KedbActionLogsDTO> kedbActionLogsListDTO) {
    return deleteByListDTO(kedbActionLogsListDTO, KedbActionLogsEntity.class, colId);
  }

  @Override
  public Datatable onSearchKedbActionLogs(KedbActionLogsDTO dto) {
    BaseDto baseDto = sqlSearchByKedbId(dto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(), dto.getPage(), dto.getPageSize(), KedbActionLogsDTO.class
        , dto.getSortName(), dto.getSortType());
  }

  private BaseDto sqlSearchByKedbId(KedbActionLogsDTO dto) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_KEDB_ACTION_LOGS, "search-by-kedb-id");
    Map<String, Object> parameters = new HashMap<>();

    if (dto != null) {
      if ((dto.getKedbId() != null)) {
        sqlQuery += " AND KEDB_ID = :kedbId ";
        parameters.put("kedbId", dto.getKedbId());
      }
      sqlQuery += " order by ID ";
      baseDto.setSqlQuery(sqlQuery);
      baseDto.setParameters(parameters);
    }
    return baseDto;
  }

  private static final String colId = "id";
}
