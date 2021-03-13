package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrAlarmSettingDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.model.CrAlarmSettingEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DungPV
 */
@Repository
@Transactional
@Slf4j
public class CrAlarmSettingRepositoryImpl extends BaseRepository implements
    CrAlarmSettingRepository {

  @Override
  public BaseDto sqlSearch(CrAlarmSettingDTO crAlarmSettingDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder.getSqlQueryById("CrAlarmSetting", "cr-alarm-setting-list");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    if (StringUtils.isNotNullOrEmpty(crAlarmSettingDTO.getSearchAll())) {
      sqlQuery += " and lower(FAULT_NAME) like :searchAll escape '\\' ";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(crAlarmSettingDTO.getSearchAll()));
    }
    if (crAlarmSettingDTO.getCrProcessId() != null && crAlarmSettingDTO.getCrProcessId() > 0) {
      sqlQuery += " and CR_PROCESS_ID = :crProcessId ";
      parameters.put("crProcessId", crAlarmSettingDTO.getCrProcessId());
    }
    if (StringUtils.isNotNullOrEmpty(crAlarmSettingDTO.getCrProcessName())) {
      sqlQuery += " and lower(CR_PROCESS_NAME) like :CR_PROCESS_NAME escape '\\' ";
      parameters.put("CR_PROCESS_NAME",
          StringUtils.convertLowerParamContains(crAlarmSettingDTO.getCrProcessName()));
    }
    if (crAlarmSettingDTO.getApprovalStatus() != null
        && crAlarmSettingDTO.getApprovalStatus() > -1) {
      sqlQuery += " and APPROVAL_STATUS = :approvalStatus ";
      parameters.put("approvalStatus", crAlarmSettingDTO.getApprovalStatus());
    }
    if (StringUtils.isNotNullOrEmpty(crAlarmSettingDTO.getNationCode())
        && "NOC".equalsIgnoreCase(crAlarmSettingDTO.getNationCode().trim())) {
      sqlQuery += " and (NATION_CODE = 'NOC' or a.NATION_CODE is null) ";
    } else if (StringUtils.isNotNullOrEmpty(crAlarmSettingDTO.getNationCode())) {
      sqlQuery += " and NATION_CODE = :nationCode ";
      parameters.put("nationCode", crAlarmSettingDTO.getNationCode());
    }
    if (StringUtils.isNotNullOrEmpty(crAlarmSettingDTO.getVendorCode())) {
      sqlQuery += " and lower(VENDOR_CODE) like :vendorCode escape '\\' ";
      parameters.put("vendorCode",
          StringUtils.convertLowerParamContains(crAlarmSettingDTO.getVendorCode()));
    }
    if (StringUtils.isNotNullOrEmpty(crAlarmSettingDTO.getModuleCode())) {
      sqlQuery += " and lower(MODULE_CODE) like :moduleCode escape '\\' ";
      parameters.put("moduleCode",
          StringUtils.convertLowerParamContains(crAlarmSettingDTO.getModuleCode()));
    }
    if (StringUtils.isNotNullOrEmpty(crAlarmSettingDTO.getCreatedUser())) {
      sqlQuery += " and lower(CREATED_USER) like :createdUser escape '\\' ";
      parameters.put("createdUser",
          StringUtils.convertLowerParamContains(crAlarmSettingDTO.getCreatedUser()));
    }
    if (crAlarmSettingDTO.getCrDomain() != null && crAlarmSettingDTO.getCrDomain() > 0) {
      sqlQuery += " and IMPACT_SEGMENT_ID = :impactSegmentId ";
      parameters.put("impactSegmentId", crAlarmSettingDTO.getCrDomain());
    }
    if (crAlarmSettingDTO.getDeviceType() != null && crAlarmSettingDTO.getDeviceType() > 0) {
      sqlQuery += " and DEVICE_TYPE_ID = :deviceType ";
      parameters.put("deviceType", crAlarmSettingDTO.getDeviceType());
    }
    if (StringUtils.isNotNullOrEmpty(crAlarmSettingDTO.getFaultName())) {
      sqlQuery += " and lower(FAULT_NAME) like :alarmName escape '\\' ";
      parameters.put("alarmName",
          StringUtils.convertLowerParamContains(crAlarmSettingDTO.getFaultName()));
    }
    if (crAlarmSettingDTO.getFaultId() != null && crAlarmSettingDTO.getFaultId() > 0) {
      sqlQuery += " and FAULT_ID = :faultId ";
      parameters.put("faultId", crAlarmSettingDTO.getFaultId());
    }
    sqlQuery += " order by CAS_ID DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public Datatable getCrAlarmSetting(CrAlarmSettingDTO crAlarmSettingDTO) {
    BaseDto baseDto = sqlSearch(crAlarmSettingDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        crAlarmSettingDTO.getPage(), crAlarmSettingDTO.getPageSize(), CrAlarmSettingDTO.class,
        crAlarmSettingDTO.getSortName(), crAlarmSettingDTO.getSortType());
  }

  @Override
  public ResultInSideDto saveOrUpdateAlarmSetting(CrAlarmSettingDTO crAlarmSettingDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    CrAlarmSettingEntity crAlarmSettingEntity = getEntityManager()
        .merge(crAlarmSettingDTO.toModel());
    resultInSideDto.setId(crAlarmSettingEntity.getCasId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteAlarmSetting(Long casId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    CrAlarmSettingEntity entity = getEntityManager()
        .find(CrAlarmSettingEntity.class, casId);
    if (entity != null) {
      getEntityManager().remove(entity);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public List<CrAlarmSettingDTO> getCrAlarmSettingExport(CrAlarmSettingDTO crAlarmSettingDTO) {
    BaseDto baseDto = sqlSearch(crAlarmSettingDTO);
    List<CrAlarmSettingDTO> data = getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(),
        baseDto.getParameters(), BeanPropertyRowMapper.newInstance(CrAlarmSettingDTO.class));
    return data;
  }

  @Override
  public CrAlarmSettingDTO findCrAlarmSettingById(Long casId) {
    String sqlQuery = SQLBuilder.getSqlQueryById("CrAlarmSetting", "cr-alarm-setting-list");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    sqlQuery += " and CAS_ID = :casId ";
    parameters.put("casId", casId);
    List<CrAlarmSettingDTO> lst = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CrAlarmSettingDTO.class));
    return lst.isEmpty() ? null : lst.get(0);
  }

  @Override
  public List<CrAlarmSettingDTO> findCrAlarmSettingByProcessId(Long processId) {
    String sqlQuery = SQLBuilder.getSqlQueryById("CrAlarmSetting", "cr-alarm-setting-list");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    sqlQuery += " and CR_PROCESS_ID = :crProcessId ";
    parameters.put("crProcessId", processId);
    List<CrAlarmSettingDTO> lst = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CrAlarmSettingDTO.class));
    return lst;
  }

  @Override
  public List<ItemDataCRInside> listCrProcessCBB(CrProcessInsideDTO crProcessDTO) {
    List<ItemDataCRInside> lst = new ArrayList();
    List<CrProcessInsideDTO> list = actionGetListProcessType(crProcessDTO);
    if ((list == null) || (list.isEmpty())) {
      return lst;
    }

    StringBuilder sqlQuery = new StringBuilder();
    sqlQuery.append(SQLBuilder.getSqlQueryById("CrAlarmSetting", "cr-process-tree-by-parent"));

    String temp = "";
    for (CrProcessInsideDTO processDTO : list) {
      temp = temp + "  (1," + processDTO.getCrProcessId() + "),";
    }
    temp = temp.substring(0, temp.length() - 1);
    sqlQuery.append(temp);
    sqlQuery.append(" ) CONNECT BY PRIOR parent_id = cr_process_id ) ");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(crProcessDTO.getCrProcessName())) {
      sqlQuery.append(" and lower(ld.cr_process_name) like :crProessName escape '\\' ");
      parameters.put("crProessName",
          StringUtils.convertLowerParamContains(crProcessDTO.getCrProcessName()));
    }
    if (StringUtils.isNotNullOrEmpty(crProcessDTO.getCrProcessCode())) {
      sqlQuery.append(" and lower(ld.cr_process_code) like :crProessCode escape '\\' ");
      parameters.put("crProessCode",
          StringUtils.convertLowerParamContains(crProcessDTO.getCrProcessCode()));
    }
    sqlQuery.append("ORDER BY ld.path");
    parameters.put("p_leeLocale", I18n.getLocale());
    parameters.put("p_system", "OPEN_PM");
    parameters.put("p_bussiness", "OPEN_PM.CR_PROCESS");

    return getNamedParameterJdbcTemplate().query(sqlQuery.toString(), parameters,
        BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
  }

  @Override
  public ResultInSideDto checkDuplicate(Long faultId, Long processId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    if (faultId != null && processId != null) {
      List<CrAlarmSettingEntity> lst = findByMultilParam(CrAlarmSettingEntity.class, "faultId",
          faultId,
          "crProcessId",
          processId);
      if (lst.size() > 0) {
        resultInSideDto.setKey(Constants.RESULT.DUPLICATE);
      }
    }
    return resultInSideDto;
  }

  private List<CrProcessInsideDTO> actionGetListProcessType(CrProcessInsideDTO crProcessDTO) {
    List<CrProcessInsideDTO> lst = new ArrayList();
    if (//(StringUtils.isStringNullOrEmpty(form.getCrTypeId()))
      //   || (StringUtils.isStringNullOrEmpty(form.getRiskLevel())) ||
        (StringUtils.isStringNullOrEmpty(crProcessDTO.getDeviceTypeId()))
            || (StringUtils.isStringNullOrEmpty(crProcessDTO.getImpactSegmentId()))) {
      return lst;
    }

    StringBuilder sqlQuery = new StringBuilder();
    sqlQuery.append(SQLBuilder.getSqlQueryById("CrAlarmSetting", "actionGetListProcessType"));

    Map<String, Object> parameters = new HashMap<>();
    if (crProcessDTO.getCrTypeId() != null) {
      sqlQuery.append(" and cps.CR_TYPE_ID =:CR_TYPE_ID ");
      parameters.put("CR_TYPE_ID", crProcessDTO.getCrTypeId());
    }

    if (crProcessDTO.getRiskLevel() != null && crProcessDTO.getRiskLevel() > 0) {
      sqlQuery.append(" and cps.RISK_LEVEL =:RISK_LEVEL ");
      parameters.put("RISK_LEVEL", crProcessDTO.getRiskLevel());
    }

    if (crProcessDTO.getDeviceTypeId() != null && crProcessDTO.getDeviceTypeId() > 0) {
      sqlQuery.append(" and cps.DEVICE_TYPE_ID =:DEVICE_TYPE_ID ");
      parameters.put("DEVICE_TYPE_ID", crProcessDTO.getDeviceTypeId());
    }
    if (crProcessDTO.getImpactSegmentId() != null && crProcessDTO.getImpactSegmentId() > 0) {
      sqlQuery.append(" and cps.IMPACT_SEGMENT_ID =:IMPACT_SEGMENT_ID ");
      parameters.put("IMPACT_SEGMENT_ID", crProcessDTO.getImpactSegmentId());
    }
    lst = getNamedParameterJdbcTemplate().query(
        sqlQuery.toString(), parameters, BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class)
    );
    return lst;
  }
}
