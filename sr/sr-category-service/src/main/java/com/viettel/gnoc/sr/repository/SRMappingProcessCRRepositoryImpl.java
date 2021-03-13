package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRCreateAutoCRDTO;
import com.viettel.gnoc.sr.dto.SRMappingProcessCRDTO;
import com.viettel.gnoc.sr.model.SRMappingProcessCREntity;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SRMappingProcessCRRepositoryImpl extends BaseRepository implements
    SRMappingProcessCRRepository {


  public BaseDto getSQL(SRMappingProcessCRDTO mappingProcessCRDTO) {
    BaseDto baseDto = new BaseDto();
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_MAPPING, "getListMappingProcessCR");
    if (!StringUtils.isStringNullOrEmpty(mappingProcessCRDTO.getProcess())) {
      sql += " and (:p_process is null or cr_parent.cr_process_id = :p_process) ";
      parameters.put("p_process", mappingProcessCRDTO.getProcess());
    }
    if (!StringUtils.isStringNullOrEmpty(mappingProcessCRDTO.getProcessTypeLv3Id())) {
      sql += " and NVL(cr_id.PROCESS_TYPE_LV3_ID,cr_children.CR_PROCESS_ID) like :processTypeLv3Id ESCAPE '\\' ";
      parameters.put("processTypeLv3Id",
          StringUtils.convertLowerParamContains(mappingProcessCRDTO.getProcessTypeLv3Id()));
    }
    if (!StringUtils.isStringNullOrEmpty(mappingProcessCRDTO.getServiceCode())) {
      sql += " and cr_id.SERVICE_CODE =:p_service_code ";
      parameters.put("p_service_code", mappingProcessCRDTO.getServiceCode());
    }
    if (!StringUtils.isStringNullOrEmpty(mappingProcessCRDTO.getServiceName())) {
      sql += " AND LOWER(src.SERVICE_NAME) like :p_service_name ESCAPE '\\' ";
      parameters.put("p_service_name",
          StringUtils.convertLowerParamContains(mappingProcessCRDTO.getServiceName()));
    }
    if (StringUtils.isNotNullOrEmpty(mappingProcessCRDTO.getSearchAll())) {
      sql += " AND (lower(src.SERVICE_NAME) LIKE :searchAll ESCAPE '\\' OR lower(cr_id.SERVICE_CODE) LIKE :searchAll ESCAPE '\\' ) ";
      parameters
          .put("searchAll",
              StringUtils.convertLowerParamContains(mappingProcessCRDTO.getSearchAll().trim()));
    }
    parameters.put("p_leeLocale", I18n.getLocale());
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);

    return baseDto;
  }

  @Override
  public Datatable getListMappingProcessCR(SRMappingProcessCRDTO srMappingProcessCRDTO) {
    BaseDto baseDto = getSQL(srMappingProcessCRDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        srMappingProcessCRDTO.getPage(), srMappingProcessCRDTO.getPageSize(),
        SRMappingProcessCRDTO.class,
        srMappingProcessCRDTO.getSortName(), srMappingProcessCRDTO.getSortType());
  }

  @Override
  public List<SRMappingProcessCRDTO> getListAllMappingProcessCR(
      SRMappingProcessCRDTO srMappingProcessCRDTO) {
    BaseDto baseDto = getSQL(srMappingProcessCRDTO);
    List<SRMappingProcessCRDTO> list = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(SRMappingProcessCRDTO.class));
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  @Override
  public ResultInSideDto insertOrUpdate(SRMappingProcessCRDTO srMappingProcessCRDTO) {
    ResultInSideDto resultDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    SRMappingProcessCREntity entity = getEntityManager().merge(srMappingProcessCRDTO.toEntity());
    resultDto.setId(entity.getId());
    return resultDto;
  }

  @Override
  public SRMappingProcessCRDTO getSRMappingProcessCRDetail(
      SRMappingProcessCRDTO srMappingProcessCRDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_MAPPING, "getListMappingProcessCR");
//    Double offset = 0.0;
//    if (!StringUtils.isStringNullOrEmpty(srMappingProcessCRDTO.getUserNameToken())) {
//      offset = userRepository.getOffsetFromUser(srMappingProcessCRDTO.getUserNameToken());
//    }
//    parameters.put("double_offset", Double.valueOf(offset));
    if (!StringUtils.isStringNullOrEmpty(srMappingProcessCRDTO.getId())) {
      sql += " and cr_id.ID = :id ";
      parameters.put("id", srMappingProcessCRDTO.getId());
    }
    parameters.put("p_leeLocale", I18n.getLocale());
    List<SRMappingProcessCRDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRMappingProcessCRDTO.class));
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return new SRMappingProcessCRDTO();
  }

  @Override
  public ResultInSideDto deleteSRMappingProcessCR(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    SRMappingProcessCREntity entity = getEntityManager().find(SRMappingProcessCREntity.class, id);
    if (entity != null) {
      getEntityManager().remove(entity);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public List<SRMappingProcessCRDTO> getListAllProcess() {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_MAPPING, "getListAllProcess");
    List<SRMappingProcessCRDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRMappingProcessCRDTO.class));
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  @Override
  public List<SRMappingProcessCRDTO> getListParentChilLevel2() {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_MAPPING, "getListParentChilLevel2");
    parameters.put("p_leeLocale", I18n.getLocale());
    parameters.put("p_system", "OPEN_PM");
    parameters.put("p_bussiness", "OPEN_PM.CR_PROCESS");
    List<SRMappingProcessCRDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRMappingProcessCRDTO.class));
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  @Override
  public List<SRMappingProcessCRDTO> getListAllWo() {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SR_MAPPING, "getListAllWo");
    parameters.put("p_leeLocale", I18n.getLocale());
    List<SRMappingProcessCRDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(SRMappingProcessCRDTO.class));
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  @Override
  public List<SRMappingProcessCRDTO> checkDeleteSRMappingProcess(
      SRMappingProcessCRDTO srMappingProcessCRDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_MAPPING, "checkDeleteSRMappingProcess");
    if (!StringUtils.isStringNullOrEmpty(srMappingProcessCRDTO.getServiceCode())) {
      sql += " and sm.service_code = :p_service_code ";
      parameters.put("p_service_code", srMappingProcessCRDTO.getServiceCode());
    }
    List<SRMappingProcessCRDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRMappingProcessCRDTO.class));

    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  @Override
  public List<SRMappingProcessCRDTO> exportSRMappingProcessCR(
      SRMappingProcessCRDTO srMappingProcessCRDTO) {
    BaseDto baseDto = getSQL(srMappingProcessCRDTO);
    List<SRMappingProcessCRDTO> list = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(SRMappingProcessCRDTO.class));
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  @Override
  public List<SRMappingProcessCRDTO> getCrProcessIdOrWoId(String crProcessCode) {
    Map<String, Object> parameters = new HashMap<>();
    StringBuffer sql = new StringBuffer();
    sql.append(
        "select cr_process_id crProcessId, cr_process_code crProcessCode, cr_process_name crProcessName "
            + "from cr_process where cr_process_code = :p_cr_process_code ");
    if (!StringUtils.isStringNullOrEmpty(crProcessCode)) {
      parameters.put("p_cr_process_code", crProcessCode);
    }
    List<SRMappingProcessCRDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(SRMappingProcessCRDTO.class));

    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  @Override
  public List<SRMappingProcessCRDTO> getListWo(Long crProcessId) {
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isLongNullOrEmpty(crProcessId)) {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_MAPPING, "getListWo");
      parameters.put("p_leeLocale", I18n.getLocale());
      parameters.put("p_system", "OPEN_PM");
      parameters.put("p_bussiness", "OPEN_PM.CR_PROCESS");
      parameters.put("p_cr_process_id", crProcessId);
      List<SRMappingProcessCRDTO> list = getNamedParameterJdbcTemplate()
          .query(sql, parameters,
              BeanPropertyRowMapper.newInstance(SRMappingProcessCRDTO.class));

      if (list != null && list.size() > 0) {
        return list;
      }
    }
    return null;
  }

  @Override
  public List<SRMappingProcessCRDTO> getListParentChil() {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_MAPPING, "getListParentChil");
    List<SRMappingProcessCRDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SRMappingProcessCRDTO.class));
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  @Override
  public List<SRMappingProcessCRDTO> getListSRMappingProcessCRDTO(SRMappingProcessCRDTO dto) {
    BaseDto baseDto = getSQL(dto);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(SRMappingProcessCRDTO.class));
  }

  @Override
  public SRMappingProcessCRDTO getStartTimeEndTimeFromCrImpact(SRCreateAutoCRDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_MAPPING, "getStartTimeEndTimeFromCrImpact");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(dto.getCrProcessId())) {
      parameters.put("p_cr_process_id", Long.parseLong(dto.getCrProcessId()));
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getSrId())) {
      parameters.put("p_sr_id", dto.getSrId());
    }
    parameters.put("p_execution_time", dto.getExecutionTime());
    parameters.put("p_execution_end_time", dto.getExecutionEndTime());
    List<SRMappingProcessCRDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRMappingProcessCRDTO.class));
    return lst.isEmpty() ? null : lst.get(0);
  }

  @Override
  public List<WoInsideDTO> getLstPriority() {
    String sql = " select DISTINCT PRIORITY_NAME priorityName,PRIORITY_ID priorityId,PRIORITY_CODE priorityCode from WFM.WO_PRIORITY where WO_TYPE_ID is null ";
    Map<String, Object> parameters = new HashMap<>();
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoInsideDTO.class));
  }

  @Override
  public List<ItemDataCRInside> getDutyTypeByProcessId(Long processId) {
    if (!StringUtils.isLongNullOrEmpty(processId)) {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_MAPPING, "getDutyTypeByProcessId");
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("CR_PROCESS_ID", processId);
      parameters.put("lee_locale", I18n.getLocale());
      List<ItemDataCRInside> lst = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
      return lst;
    }
    return null;
  }
}
