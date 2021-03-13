package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.TransitionStateConfigDTO;
import com.viettel.gnoc.commons.model.TransitionStateConfigEntity;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author TienNV
 */

@Repository
@Slf4j
public class TransitionStateConfigRepositoryImpl extends BaseRepository implements
    TransitionStateConfigRepository {

  @Override
  public List<TransitionStateConfigDTO> onSearch(TransitionStateConfigDTO dto) {
    List<TransitionStateConfigDTO> ret = new ArrayList<>();
    Map<String, Object> paramMap = new HashMap<>();
    try {
      String sql = "SELECT   t.begin_state_id beginStateId,"
          + "         t.end_state_id endStateId,"
          + "         t.id,"
          + "         t.description,"
          + "         t.process,"
          + "         b.item_name beginStateName,"
          + "         b.item_code beginStateCode,"
          + "         e.item_name endStateName,"
          + "         e.item_code endStateCode,"
          + "         c.category_name processName,"
          + "         c.category_code processCode"
          + "  FROM   common_gnoc.transition_state_config t, "
          + " common_gnoc.cat_item b, "
          + " common_gnoc.cat_item e,"
          + " common_gnoc.category c"
          + " WHERE   t.begin_state_id = b.item_id AND t.end_state_id = e.item_id AND t.process = c.category_id";
      if (dto != null) {

        if (dto.getProcess() != null) {
          sql += " AND t.process = :process ";
          paramMap.put("process", dto.getProcess());
        }
        if (dto.getBeginStateId() != null && !"-1".equals(dto.getBeginStateId())) {
          sql += " AND t.begin_state_id = :begin_state_id ";
          paramMap.put("begin_state_id", dto.getBeginStateId());
        }
        if (dto.getEndStateId() != null && !"-1".equals(dto.getEndStateId())) {
          sql += " AND t.end_state_id = :end_state_id ";
          paramMap.put("end_state_id", dto.getEndStateId());
        }
      }
      sql += " order by t.process,b.item_name,e.item_name ";

      ret = getNamedParameterJdbcTemplate()
          .query(sql, paramMap, BeanPropertyRowMapper.newInstance(TransitionStateConfigDTO.class));
      ret = setLanguageTransitionStateConfig(ret);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }

    return ret;
  }

  @Override
  public Datatable getListTransitionStateConfigDTO(
      TransitionStateConfigDTO transitionStateConfigDTO) {
    BaseDto baseDto = sqlGetListTransitionStateConfigDTO(transitionStateConfigDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        transitionStateConfigDTO.getPage(), transitionStateConfigDTO.getPageSize(),
        TransitionStateConfigDTO.class, transitionStateConfigDTO.getSortName(),
        transitionStateConfigDTO.getSortType());
    List<TransitionStateConfigDTO> list = (List<TransitionStateConfigDTO>) datatable.getData();
    list = setLanguageTransitionStateConfig(list);
    datatable.setData(list);
    return datatable;
  }

  public List<TransitionStateConfigDTO> setLanguageTransitionStateConfig(
      List<TransitionStateConfigDTO> list) {
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
        Constants.APPLIED_BUSSINESS.CAT_ITEM.toString());
    try {
      list = setLanguage(list, lstLanguage, "beginStateId", "beginStateName");
      list = setLanguage(list, lstLanguage, "endStateId", "endStateName");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  @Override
  public ResultInSideDto insertTransitionStateConfig(
      TransitionStateConfigDTO transitionStateConfigDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    TransitionStateConfigEntity trans = getEntityManager().merge(transitionStateConfigDTO.toEntity());
    if (trans != null) {
      resultInSideDto.setId(trans.getId());
    }
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateTransitionStateConfig(
      TransitionStateConfigDTO transitionStateConfigDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().merge(transitionStateConfigDTO.toEntity());
    resultInSideDto.setId(transitionStateConfigDTO.getId());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteTransitionStateConfig(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    TransitionStateConfigEntity entity = getEntityManager()
        .find(TransitionStateConfigEntity.class, id);
    getEntityManager().remove(entity);
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public TransitionStateConfigDTO findTransitionStateConfigById(Long id) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON,
        "get-List-Transition-State-Config-DTO");
    Map<String, Object> parameters = new HashMap<>();
    sql += " AND t.id = :id";
    parameters.put("id", id);
    List<TransitionStateConfigDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(TransitionStateConfigDTO.class));
    if (list != null && !list.isEmpty()) {
      TransitionStateConfigDTO dto = list.get(0);
      return dto;
    }
    return null;
  }

  @Override
  public List<CatItemDTO> getListProcess() {
    List<CatItemDTO> listProcess = new ArrayList<>();
    listProcess.add(
        new CatItemDTO(Constants.PROCESS.CR_STATE, I18n.getLanguage("combo.deault.cr_process")));
    listProcess.add(
        new CatItemDTO(Constants.PROCESS.MR_STATE, I18n.getLanguage("combo.deault.mr_process")));
    listProcess.add(
        new CatItemDTO(Constants.PROCESS.PT_STATE, I18n.getLanguage("combo.deault.pt_process")));
    listProcess.add(
        new CatItemDTO(Constants.PROCESS.TT_STATE, I18n.getLanguage("combo.deault.tt_process")));
    listProcess.add(
        new CatItemDTO(Constants.PROCESS.WO_STATE, I18n.getLanguage("combo.deault.wo_process")));
    listProcess.add(new CatItemDTO(Constants.PROCESS.ORDER_STATE,
        I18n.getLanguage("combo.deault.order_process")));
    return listProcess;
  }

  @Override
  public List<CatItemDTO> getListState(Long process) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-List-State");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("process", process);
    List<CatItemDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
        Constants.APPLIED_BUSSINESS.CAT_ITEM.toString());
    try {
      list = setLanguage(list, lstLanguage, Constants.ITEM_ID, Constants.ITEM_NAME);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  @Override
  public List<TransitionStateConfigDTO> getListTransitionStateConfigExport(
      TransitionStateConfigDTO transitionStateConfigDTO) {
    BaseDto baseDto = sqlGetListTransitionStateConfigDTO(transitionStateConfigDTO);
    List<TransitionStateConfigDTO> list = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(TransitionStateConfigDTO.class));
    list = setLanguageTransitionStateConfig(list);
    return list;
  }

  private BaseDto sqlGetListTransitionStateConfigDTO(
      TransitionStateConfigDTO transitionStateConfigDTO) {
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON,
        "get-List-Transition-State-Config-DTO");
    Map<String, Object> parameters = new HashMap<>();
    if (transitionStateConfigDTO != null) {
      if (StringUtils.isNotNullOrEmpty(transitionStateConfigDTO.getSearchAll())) {
        sql += " AND (LOWER(b.item_name) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(e.item_name) LIKE :searchAll ESCAPE '\\'";
        sql += " OR LOWER(t.description) LIKE :searchAll ESCAPE '\\')";
        parameters.put("searchAll",
            StringUtils.convertLowerParamContains(transitionStateConfigDTO.getSearchAll()));
      }
      if (transitionStateConfigDTO.getProcess() != null) {
        sql += " AND t.process = :process";
        parameters.put("process", transitionStateConfigDTO.getProcess());
      }
      if (transitionStateConfigDTO.getBeginStateId() != null) {
        sql += " AND t.begin_state_id = :beginStateId";
        parameters.put("beginStateId", transitionStateConfigDTO.getBeginStateId());
      }
      if (transitionStateConfigDTO.getEndStateId() != null) {
        sql += " AND t.end_state_id = :endStateId";
        parameters.put("endStateId", transitionStateConfigDTO.getEndStateId());
      }
      if (StringUtils.isNotNullOrEmpty(transitionStateConfigDTO.getDescription())) {
        sql += " AND LOWER(t.description) LIKE :description ESCAPE '\\'";
        parameters.put("description",
            StringUtils.convertLowerParamContains(transitionStateConfigDTO.getDescription()));
      }
      if (transitionStateConfigDTO.getRoleCode() != null) {
        if(transitionStateConfigDTO.getRoleCode() == 0L){
          sql += " AND (t.role_code = 0 OR t.role_code is null) ";
          parameters.put("roleCode", transitionStateConfigDTO.getRoleCode());
        }else{
          sql += " AND t.role_code = :roleCode ";
          parameters.put("roleCode", transitionStateConfigDTO.getRoleCode());
        }
      }

    }
    sql += " ORDER BY t.process, b.item_name, e.item_name";
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
