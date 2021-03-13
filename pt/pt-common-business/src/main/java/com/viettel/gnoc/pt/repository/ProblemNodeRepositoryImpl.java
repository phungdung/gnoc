package com.viettel.gnoc.pt.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.pt.dto.ProblemNodeDTO;
import com.viettel.gnoc.pt.model.ProblemNodeEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ProblemNodeRepositoryImpl extends BaseRepository<ProblemNodeDTO, ProblemNodeEntity>
    implements ProblemNodeRepository {

  @Override
  public ResultInSideDto insertProblemNode(ProblemNodeDTO problemNodeDTO) {
    return insertByModel(problemNodeDTO.toEntity(), colId);
  }

  @Override
  public String updateProblemNode(ProblemNodeDTO problemNodeDTO) {
    ProblemNodeEntity entity = problemNodeDTO.toEntity();
    getEntityManager().merge(entity);
    return RESULT.SUCCESS;
  }

  @Override
  public String deleteProblemNode(Long id) {
    return deleteById(ProblemNodeEntity.class, id, colId);
  }

  @Override
  public List<ProblemNodeDTO> getListProblemNodeDTO(ProblemNodeDTO problemNodeDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(ProblemNodeEntity.class, problemNodeDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public String deleteListProblemNode(List<ProblemNodeDTO> problemNodeListDTO) {
    return deleteByListDTO(problemNodeListDTO, ProblemNodeEntity.class, colId);
  }

  @Override
  public List<ProblemNodeDTO> getListProblemNodeByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new ProblemNodeEntity(), lstCondition, rowStart, maxRow,
        sortType, sortFieldList);
  }

  @Override
  public List<String> getSequenseProblemNode(String seqName, int... size) {
    return getListSequense(seqName, size);
  }

  @Override
  public String insertOrUpdateListProblemNode(List<ProblemNodeDTO> problemNodeDTO) {
    for (ProblemNodeDTO item : problemNodeDTO) {
      ProblemNodeEntity entity = item.toEntity();
      if (entity.getProblemNodeId() != null && entity.getProblemNodeId() > 0) {
        getEntityManager().merge(entity);
      } else {
        getEntityManager().persist(entity);
      }
    }
    return RESULT.SUCCESS;
  }

  @Override
  public ProblemNodeDTO findProblemNodeById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(ProblemNodeEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public Datatable onSearchProblemNodeDTO(ProblemNodeDTO dto) {
    BaseDto baseDto = sqlSearchByProblemId(dto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(), dto.getPage(), dto.getPageSize(), ProblemNodeDTO.class
        , dto.getSortName(), dto.getSortType());
  }

  private BaseDto sqlSearchByProblemId(ProblemNodeDTO dto) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_PROBLEM_NODE, "search-by-problem-id");
    Map<String, Object> parameters = new HashMap<>();

    if (dto != null) {
      if ((dto.getProblemId()) != null && dto.getProblemId() > 0) {
        sqlQuery += " AND PROBLEM_ID = :problemId ";
        parameters.put("problemId", dto.getProblemId());
      }
      sqlQuery += " order by PROBLEM_NODE_ID ";
      baseDto.setSqlQuery(sqlQuery);
      baseDto.setParameters(parameters);
    }
    return baseDto;
  }

  private static final String colId = "problemNodeId";
}
