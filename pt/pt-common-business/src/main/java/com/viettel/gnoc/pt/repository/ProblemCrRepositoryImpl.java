package com.viettel.gnoc.pt.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.pt.dto.ProblemCrDTO;
import com.viettel.gnoc.pt.model.ProblemCrEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ProblemCrRepositoryImpl extends BaseRepository<ProblemCrDTO, ProblemCrEntity>
    implements ProblemCrRepository {

  @Override
  public ResultInSideDto insertProblemCr(ProblemCrDTO problemCrDTO) {
    return insertByModel(problemCrDTO.toEntity(), colId);
  }

  @Override
  public String updateProblemCr(ProblemCrDTO problemCrDTO) {
    ProblemCrEntity entity = problemCrDTO.toEntity();
    getEntityManager().merge(entity);
    return RESULT.SUCCESS;
  }

  @Override
  public String deleteProblemCr(Long id) {
    return deleteById(ProblemCrEntity.class, id, colId);
  }

  @Override
  public List<ProblemCrDTO> getListProblemCrDTO(ProblemCrDTO problemCrDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(ProblemCrEntity.class, problemCrDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public String deleteListProblemCr(List<ProblemCrDTO> problemCrListDTOS) {
    return deleteByListDTO(problemCrListDTOS, ProblemCrEntity.class, colId);
  }

  @Override
  public List<ProblemCrDTO> getListProblemCrByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new ProblemCrEntity(), lstCondition, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public List<String> getSequenseProblemCr(String seqName, int... size) {
    return getListSequense(seqName, size);
  }

  @Override
  public String insertOrUpdateListProblemCr(List<ProblemCrDTO> problemCrDTOS) {
    for (ProblemCrDTO item : problemCrDTOS) {
      ProblemCrEntity entity = item.toEntity();
      if (entity.getProblemCrId() != null && entity.getProblemCrId() > 0) {
        getEntityManager().merge(entity);
      } else {
        getEntityManager().persist(entity);
      }
    }
    return RESULT.SUCCESS;
  }

  @Override
  public ProblemCrDTO findProblemCrById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(ProblemCrEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public List<ProblemCrDTO> onSearchProblemCrDTO(ProblemCrDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(ProblemCrDTO.class));
  }

  private BaseDto sqlSearch(ProblemCrDTO dto) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_PROBLEM_CR, "search-by-problem-id");
    Map<String, Object> parameters = new HashMap<>();

    if (dto != null) {
      if (!(dto.getProblemId().toString()).isEmpty()) {
        sqlQuery += " AND PROBLEM_ID = :problemId ";
        parameters.put("problemId", dto.getProblemId());
      }
      sqlQuery += " order by PROBLEM_CR_ID ";
      baseDto.setSqlQuery(sqlQuery);
      baseDto.setParameters(parameters);
    }
    return baseDto;
  }

  private static final String colId = "problemCrId";
}
