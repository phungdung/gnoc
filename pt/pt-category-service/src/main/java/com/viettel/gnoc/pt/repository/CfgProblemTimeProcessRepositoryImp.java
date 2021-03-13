package com.viettel.gnoc.pt.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.pt.dto.CfgProblemTimeProcessDTO;
import com.viettel.gnoc.pt.model.CfgProblemTimeProcessEntity;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgProblemTimeProcessRepositoryImp extends BaseRepository implements
    CfgProblemTimeProcessRepository {

  @Override
  public CfgProblemTimeProcessEntity findById(long id) {
    return getEntityManager().find(CfgProblemTimeProcessEntity.class, id);
  }

  @Override
  public ResultInSideDto onDeleteList(List<CfgProblemTimeProcessDTO> lstCfgProblemTimeProcess) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    List<String> lstCode = new ArrayList<>();
    for (CfgProblemTimeProcessDTO obj : lstCfgProblemTimeProcess) {
      lstCode.add(obj.getCfgCode());
    }

    String sqlDelete = " DELETE FROM " + CfgProblemTimeProcessEntity.class.getSimpleName() + " t" +
        " WHERE t.cfgCode in (:lstCfgCode) ";

    Query query = getEntityManager().createQuery(sqlDelete);
    query.setParameter("lstCfgCode", lstCode);
    int result = query.executeUpdate();
    if (result > 0) {
      resultInSideDto.setKey(RESULT.SUCCESS);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto onUpdateList(List<CfgProblemTimeProcessDTO> lstCfgProblemTimeProcess,
      CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    String sqlQuery = SQLBuilder.getSqlQueryById("cfgProblemTime", "update-list-by-code")
        .replace("$tbName", CfgProblemTimeProcessEntity.class.getSimpleName());
    List<String> lstCode = new ArrayList<>();
    if (null == lstCfgProblemTimeProcess || lstCfgProblemTimeProcess.size() < 1) {
      resultInSideDto.setMessage("Could not update empty list");
    } else {
      for (CfgProblemTimeProcessDTO cfgDTO : lstCfgProblemTimeProcess) {
        lstCode.add(cfgDTO.getCfgCode());
      }
      Date upDateTime = null;
      try {
        upDateTime = cfgProblemTimeProcessDTO.getLastUpdateTime();
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      Query query = getEntityManager().createQuery(sqlQuery);
      query.setParameter("rcaFoundTime",
          Double.parseDouble(cfgProblemTimeProcessDTO.getRcaFoundTime()));
      query.setParameter("waFoundTime",
          Double.parseDouble(cfgProblemTimeProcessDTO.getWaFoundTime()));
      query.setParameter("slFoundTime",
          Double.parseDouble(cfgProblemTimeProcessDTO.getSlFoundTime()));
      query.setParameter("lastUpdateTime", upDateTime);
      query.setParameter("lstCfgCode", lstCode);
      int result = query.executeUpdate();
      if (result > 0) {
        resultInSideDto.setKey(RESULT.SUCCESS);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto onInsert(CfgProblemTimeProcessDTO cfgProblemTimeProcessDTO) {
    return insertByModel(cfgProblemTimeProcessDTO.toEntity(), colId);
  }

  @Override
  public String getSequence() throws Exception {
    Field field = CfgProblemTimeProcessEntity.class.getDeclaredField("id");
    SequenceGenerator anno = field.getAnnotation(SequenceGenerator.class);
    String sequenceName = anno.sequenceName();
    return getSeqTableBase(sequenceName);
  }

  @Override
  public Datatable getDataTableCfgProblemTimeProcessDTO(
      CfgProblemTimeProcessDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(), dto.getPage(), dto.getPageSize(),
        CfgProblemTimeProcessDTO.class, dto.getSortName(), dto.getSortType());
  }

  @Override
  public List<CfgProblemTimeProcessDTO> getDataExportCfgProblemTimeProcessDTO(
      CfgProblemTimeProcessDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(), BeanPropertyRowMapper
            .newInstance(CfgProblemTimeProcessDTO.class));
  }

  @Override
  public CfgProblemTimeProcessDTO getCfgProblemTimeProcessByDTO(CfgProblemTimeProcessDTO dto) {
    List<CfgProblemTimeProcessDTO> cfgProblemTimeProcessDTOS = onSearchEntity(
        CfgProblemTimeProcessEntity.class, dto, 0, 1, null, null);
    if (cfgProblemTimeProcessDTOS != null && cfgProblemTimeProcessDTOS.size() > 0) {
      return cfgProblemTimeProcessDTOS.get(0);
    }
    return null;
  }

  private BaseDto sqlSearch(CfgProblemTimeProcessDTO dto) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_PT_CFG_PROBLEM_TIME, "search-by-dto");
    Map<String, Object> parameters = new HashMap<>();

    if (dto != null) {
      if (StringUtils.isNotNullOrEmpty(dto.getCfgCode())) {
        sqlQuery += " AND LOWER(cfg.cfg_code) like :cfgCode  ESCAPE '\\' ";
        parameters.put("cfgCode", StringUtils.convertLowerParamContains(dto.getCfgCode()));
      }
      if (dto.getLstPriorityId() != null && dto.getLstPriorityId().size() > 0) {
        sqlQuery += " AND cfg.priority_code in (:lstPriority) ";
        parameters.put("lstPriority", dto.getLstPriorityId());
      }
      if (dto.getLstTypeId() != null && dto.getLstTypeId().size() > 0) {
        sqlQuery += " AND cfg.type_code in (:lstType) ";
        parameters.put("lstType", dto.getLstTypeId());
      }

      if (StringUtils.isNotNullOrEmpty(dto.getLastUpdateTimeFrom())) {
        sqlQuery += "  AND cfg.last_update_time >= to_date(:lastUpdateFrom,'dd/mm/yyyy hh24:mi:ss')  ";
        parameters.put("lastUpdateFrom", dto.getLastUpdateTimeFrom());
      }

      if (StringUtils.isNotNullOrEmpty(dto.getLastUpdateTimeTo())) {
        sqlQuery += "  AND cfg.last_update_time <= to_date(:lastUpdateTo,'dd/mm/yyyy hh24:mi:ss')  ";
        parameters.put("lastUpdateTo", dto.getLastUpdateTimeTo());
      }

      if (StringUtils.isNotNullOrEmpty(dto.getRcaFoundTime())) {
        sqlQuery += "  AND cfg.RCA_FOUND_TIME LIKE :rcaFoundTime ESCAPE '\\'   ";
        parameters
            .put("rcaFoundTime", StringUtils.convertLowerParamContains(dto.getRcaFoundTime()));
      }

      if (StringUtils.isNotNullOrEmpty(dto.getWaFoundTime())) {
        sqlQuery += "  AND cfg.WA_FOUND_TIME LIKE :waFoundTime ESCAPE '\\'   ";
        parameters.put("waFoundTime", StringUtils.convertLowerParamContains(dto.getWaFoundTime()));
      }

      if (StringUtils.isNotNullOrEmpty(dto.getSlFoundTime())) {
        sqlQuery += "  AND cfg.SL_FOUND_TIME LIKE :slFoundTime ESCAPE '\\'   ";
        parameters.put("slFoundTime", StringUtils.convertLowerParamContains(dto.getSlFoundTime()));
      }

      if (StringUtils.isNotNullOrEmpty(dto.getSearchAll())) {
        sqlQuery += " AND (lower(cfg.cfg_code) LIKE :searchAll ESCAPE '\\'  ";
//        sqlQuery += " OR lower(tName.item_name) LIKE :searchAll ESCAPE '\\'  ";
//        sqlQuery += " OR lower(pName.item_name) LIKE :searchAll ESCAPE '\\'  ";
        sqlQuery += "  ) ";
        parameters
            .put("searchAll", StringUtils.convertLowerParamContains(dto.getSearchAll()));
      }

      sqlQuery += " order by cfg.last_update_time desc";
      baseDto.setSqlQuery(sqlQuery);
      baseDto.setParameters(parameters);
    }
    return baseDto;
  }

  private static final String colId = "id";
}
