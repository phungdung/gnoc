package com.viettel.gnoc.pt.repository;

import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.pt.dto.ProblemActionLogsDTO;
import com.viettel.gnoc.pt.model.ProblemActionLogsEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ProblemActionLogsRepositoryImpl extends
    BaseRepository<ProblemActionLogsDTO, ProblemActionLogsEntity>
    implements ProblemActionLogsRepository {

  @Override
  public List<ProblemActionLogsDTO> getListProblemActionLogsByCondition(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    return onSearchByConditionBean(new ProblemActionLogsEntity(), lstCondition, rowStart, maxRow,
        sortType, sortFieldList);
  }

  @Override
  public List<ProblemActionLogsDTO> getListProblemActionLogsDTO(
      ProblemActionLogsDTO problemActionLogsDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    return onSearchEntity(ProblemActionLogsEntity.class, problemActionLogsDTO, rowStart, maxRow,
        sortType, sortFieldList);
  }

  @Override
  public String insertOrUpdateListProblemActionLogs(
      List<ProblemActionLogsDTO> problemActionLogsDTO) {
    for (ProblemActionLogsDTO item : problemActionLogsDTO) {
      ProblemActionLogsEntity entity = item.toEntity();
      if (entity.getProblemActionLogsId() != null && entity.getProblemActionLogsId() > 0) {
        getEntityManager().merge(entity);
      } else {
        getEntityManager().persist(entity);
      }
    }
    return RESULT.SUCCESS;
  }

  @Override
  public ResultInSideDto insertProblemActionLogs(ProblemActionLogsDTO problemActionLogsDTO) {
    return insertByModel(problemActionLogsDTO.toEntity(), colId);
  }

  @Override
  public ProblemActionLogsDTO findProblemActionLogsById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(ProblemActionLogsEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public List<String> getSequenseProblemActionLogs(String seqName, int... size) {
    return getListSequense(seqName, size);
  }

  @Override
  public String updateProblemActionLogs(ProblemActionLogsDTO problemActionLogsDTO) {
    ProblemActionLogsEntity entity = problemActionLogsDTO.toEntity();
    getEntityManager().merge(entity);
    return RESULT.SUCCESS;
  }

  @Override
  public String deleteListProblemActionLogs(List<ProblemActionLogsDTO> problemActionLogsListDTO) {
    return deleteByListDTO(problemActionLogsListDTO, ProblemActionLogsEntity.class, colId);
  }

  @Override
  public String deleteProblemActionLogs(Long id) {
    return deleteById(ProblemActionLogsEntity.class, id, colId);
  }

  @Override
  public Datatable onSearchProblemActionLogsDTO(ProblemActionLogsDTO dto) {
    BaseDto baseDto = sqlSearchByProblemId(dto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(), dto.getPage(), dto.getPageSize(), ProblemActionLogsDTO.class
        , dto.getSortName(), dto.getSortType());
  }

  private BaseDto sqlSearchByProblemId(ProblemActionLogsDTO dto) {
    BaseDto baseDto = new BaseDto();
    Double offset = TimezoneContextHolder.getOffsetDouble();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_PROBLEM_ACTION_LOGS, "search-by-problem-id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("offset", offset);
    if (dto != null) {
      if (!(dto.getProblemId().toString().isEmpty())) {
        sqlQuery += " AND PROBLEM_ID = :problemId ";
        parameters.put("problemId", dto.getProblemId());
      }
      sqlQuery += " order by PROBLEM_ACTION_LOGS_ID ";
      baseDto.setSqlQuery(sqlQuery);
      baseDto.setParameters(parameters);
    }
    return baseDto;
  }

  private static final String colId = "problemActionLogsId";
}
