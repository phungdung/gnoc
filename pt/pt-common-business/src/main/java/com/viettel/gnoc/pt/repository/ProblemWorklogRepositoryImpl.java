package com.viettel.gnoc.pt.repository;

import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.pt.dto.ProblemWorklogDTO;
import com.viettel.gnoc.pt.model.ProblemWorklogEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ProblemWorklogRepositoryImpl extends BaseRepository implements
    ProblemWorklogRepository {

  @Override
  public ResultInSideDto onInsert(ProblemWorklogDTO problemWorklogDTO) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      //String currDate = DateTimeUtils.convertDateTimeStampToString(new Date());
      Date currDate = new Date();
      setTimeZoneForProblemWorklog(problemWorklogDTO,
          -1L * TimezoneContextHolder.getOffsetDouble());
      problemWorklogDTO.setCreatedTime(currDate);
      return insertByModel(problemWorklogDTO.toEntity(), colId);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.ERROR);
      return resultInSideDto;
    }
  }

  // time zone
  public void setTimeZoneForProblemWorklog(ProblemWorklogDTO problemWorklogDTO, Double offset)
      throws Exception {
    try {
      if (!StringUtils.isStringNullOrEmpty(problemWorklogDTO.getCreatedTime()) && !Double.valueOf(0)
          .equals(offset) && problemWorklogDTO.getProblemId() != null) {
        //Convert thoi gian ve thoi gian theo timezone nguoi dung
        Date createDate = new Date(
            DateTimeUtils.convertStringToDateTime(problemWorklogDTO.getCreatedTime())
                + (long) (
                offset * 60 * 60 * 1000));
        problemWorklogDTO.setCreatedTime(createDate);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public List getListProblemWorklogByCondition(
      List<ConditionBean> lstConditionBean, int start, int maxResult, String sortType,
      String sortField) {
    return onSearchByConditionBean(new ProblemWorklogEntity(), lstConditionBean, start, maxResult,
        sortType, sortField);
  }

  @Override
  public List<String> getSequenceProblemWorklog(String seqName, int... size) {
    seqName = "problem_worklog_seq";
    return getListSequense(seqName, size);
  }

  @Override
  public ProblemWorklogDTO findProblemWorklogById(Long id) throws Exception {
    if (id != null && id > 0) {
      ProblemWorklogDTO problemWorklogDTO = getEntityManager().find(ProblemWorklogEntity.class, id)
          .toDTO();
      Double offset = TimezoneContextHolder.getOffsetDouble();
      setTimeZoneForProblemWorklog(problemWorklogDTO, offset);
      return problemWorklogDTO;
    }
    return null;
  }

  @Override
  public String deleteProblemWorklog(Long problemWorklogId) {
    if (problemWorklogId != null && problemWorklogId > 0) {
      return deleteById(ProblemWorklogEntity.class, problemWorklogId, colId);
    }
    return null;
  }

  @Override
  public String deleteListProblemWorklog(List<ProblemWorklogDTO> problemWorklogDTOS) {
    return deleteByListDTO(problemWorklogDTOS, ProblemWorklogEntity.class, colId);
  }

  @Override
  public String updateProblemWorklog(ProblemWorklogDTO problemWorklogDTO) throws Exception {
    try {
      setTimeZoneForProblemWorklog(problemWorklogDTO,
          -1L * TimezoneContextHolder.getOffsetDouble());
      ProblemWorklogEntity problemWorklogEntity = problemWorklogDTO.toEntity();
      getEntityManager().merge(problemWorklogEntity);
      return RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.ERROR;
    }
  }

  @Override
  public String insertOrUpdateListProblemWorklog(List<ProblemWorklogDTO> problemWorklogDTOS)
      throws Exception {
    try {
      for (ProblemWorklogDTO dto : problemWorklogDTOS) {
        ProblemWorklogEntity entity = dto.toEntity();
        if (entity.getProblemWorklogId() != null && entity.getProblemWorklogId() > 0) {
          //getEntityManager().merge(entity);
          updateProblemWorklog(entity.toDTO());
        } else {
          //getEntityManager().persist(entity);
          onInsert(entity.toDTO());
        }
      }
      return RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.ERROR;
    }
  }

  @Override
  public Datatable getListProblemWorklogDTO(ProblemWorklogDTO problemWorklogDTO) {
    try {
      if (StringUtils.isNotNullOrEmpty(problemWorklogDTO.getSortName()) && StringUtils
          .isNotNullOrEmpty(problemWorklogDTO.getSortType())) {
        if ("worklog".equalsIgnoreCase(problemWorklogDTO.getSortName())) {
          problemWorklogDTO.setSortName("(SYS.DBMS_LOB.SUBSTR(WORKLOG, 4000, 1))");
        }
      }
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_PT_PROBLEMS_WORKLOG, "problemWorklog-get-list");
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("problemId", problemWorklogDTO.getProblemId());
      Double offset = TimezoneContextHolder.getOffsetDouble();
      parameters.put("offset", offset);
      return getListDataTableBySqlQuery(sql, parameters, problemWorklogDTO.getPage(),
          problemWorklogDTO.getPageSize(), ProblemWorklogDTO.class, problemWorklogDTO.getSortName(),
          problemWorklogDTO.getSortType());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  private static final String colId = "problemWorklogId";

}
