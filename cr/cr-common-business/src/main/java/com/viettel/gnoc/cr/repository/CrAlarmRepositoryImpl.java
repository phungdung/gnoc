package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrAlarmDTO;
import com.viettel.gnoc.cr.dto.CrAlarmInsiteDTO;
import com.viettel.gnoc.cr.dto.CrAlarmSettingDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrModuleDetailDTO;
import com.viettel.gnoc.cr.dto.CrVendorDetailDTO;
import com.viettel.gnoc.cr.model.CrAlarmEntity;
import com.viettel.gnoc.cr.model.CrModuleDetailEntity;
import com.viettel.gnoc.cr.model.CrVendorDetailEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CrAlarmRepositoryImpl extends BaseRepository implements CrAlarmRepository {

  @Override
  public boolean saveOrUpdateList(List<CrAlarmInsiteDTO> dtoList, Long crId, Date crCreateTime) {
    try {
      Map<String, Object> params = new HashMap<>();
      params.put("crId", crId);
      String sql = "delete CR_ALARM WHERE CR_ID = :crId ";
      if (crCreateTime != null) {
        sql = sql.concat(" and CREATE_DATE >= :crCreateTime ");
        params.put("crCreateTime", crCreateTime);

      }
      getNamedParameterJdbcTemplate().update(sql, params);
      getEntityManager().flush();

      if (dtoList == null || dtoList.isEmpty()) {
        return true;
      }

      for (CrAlarmInsiteDTO dto : dtoList) {
        CrAlarmEntity obj = dto.toEntity();
        obj.setId(null);
        obj.setCrId(crId);
        obj.setCreateDate(new Date());
        getEntityManager().merge(obj);
      }
      getEntityManager().flush();
      getEntityManager().clear();

      return true;
    } catch (Exception e) {
      log.error(e.getMessage(), e);

    }
    return false;
  }

  @Override
  public boolean saveOrUpdateVendorDetail(List<CrVendorDetailDTO> dtoList, Long crId,
      Date crCreateTime) {
    try {

      Map<String, Object> params = new HashMap<>();
      params.put("crId", crId);
      String sql = "delete CR_VENDOR_DETAIL WHERE CR_ID = :crId ";
      if (crCreateTime != null) {
        sql = sql.concat(" and CREATE_TIME >= :crCreateTime ");
        crCreateTime = DateUtils.addDays(crCreateTime, -2);
        params.put("crCreateTime", crCreateTime);
      }
      getNamedParameterJdbcTemplate().update(sql, params);
      getEntityManager().flush();
      getEntityManager().clear();

      if (dtoList == null || dtoList.isEmpty()) {
        return true;
      }

      for (CrVendorDetailDTO dto : dtoList) {
        CrVendorDetailEntity obj = dto.toEntity();
        obj.setCvdId(null);
        obj.setCrId(crId);
        obj.setCreateTime(new Date());
        getEntityManager().merge(obj);
      }
      getEntityManager().flush();
      getEntityManager().clear();
      return true;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      //e.printStackTrace();
    } finally {
      getEntityManager().flush();
      getEntityManager().clear();
    }
    return false;
  }

  @Override
  public boolean saveOrUpdateModuleDetail(List<CrModuleDetailDTO> dtoList, Long crId,
      Date crCreateTime) {
    try {
      Map<String, Object> params = new HashMap<>();
      params.put("crId", crId);
      String sql = "delete CR_MODULE_DETAIL WHERE CR_ID = :crId ";
      if (crCreateTime != null) {
        sql = sql.concat(" and CREATE_TIME >= :crCreateTime ");
        crCreateTime = DateUtils.addDays(crCreateTime, -2);
        params.put("crCreateTime", crCreateTime);
      }

      getNamedParameterJdbcTemplate().update(sql, params);
      getEntityManager().flush();
      getEntityManager().clear();

      if (dtoList == null || dtoList.isEmpty()) {
        return true;
      }

      for (CrModuleDetailDTO dto : dtoList) {
        CrModuleDetailEntity obj = dto.toEntity();
        obj.setCmdId(null);
        obj.setCrId(crId);
        obj.setCreateTime(new Date());
        getEntityManager().merge(obj);
      }
      getEntityManager().flush();
      getEntityManager().clear();
      return true;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      getEntityManager().flush();
      getEntityManager().clear();
    }
    return false;
  }

  @Override
  public List<CrAlarmInsiteDTO> getListAlarmByCr(CrInsiteDTO crInsiteDTO) {
    List<CrAlarmInsiteDTO> lst = new ArrayList<>();
    if (crInsiteDTO.getCrId() == null) {
      return lst;
    }
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_ALARM, "on-search-crAlarm-by-cr-upgrade");
    Map<String, Object> param = new HashMap<>();
    sql += " and cr_id = :p_cr_id ";
    param.put("p_cr_id", crInsiteDTO.getCrId());
    if (crInsiteDTO.getCreatedDate() != null) {
      Date createDate = DateUtils.addDays(crInsiteDTO.getCreatedDate(), -2);
      sql += " and create_date >= :p_create_date  ";
      param.put("p_create_date", createDate);
    }
    if (crInsiteDTO.getEarliestStartTime() != null && crInsiteDTO.getCreatedDate() != null
        && crInsiteDTO.getEarliestStartTime().compareTo(crInsiteDTO.getCreatedDate()) > 0) {
      Date earliestStartDate = DateUtils.addDays(crInsiteDTO.getEarliestStartTime(), 30);
      sql += " and create_date <= :p_earLiest_start_time ";
      param.put("p_earLiest_start_time", earliestStartDate);
    }
    return getNamedParameterJdbcTemplate().query(sql, param,
        BeanPropertyRowMapper.newInstance(CrAlarmInsiteDTO.class));
  }

  @Override
  public List<CrAlarmSettingDTO> getAlarmSettingByVendor(CrAlarmSettingDTO crAlarmSettingDTO) {
    List<CrAlarmSettingDTO> lst;
    Map<Long, CrAlarmSettingDTO> map = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_ALARM, "get-alarm-setting-by-vendor");
    Map<String, Object> param = new HashMap<>();
    if (crAlarmSettingDTO.getCrProcessName() != null) {
      List<String> lstId = new ArrayList<>(
          Arrays.asList(crAlarmSettingDTO.getCrProcessName().split(",")));
      sql += " and  a.CR_PROCESS_ID IN(:crProcessId)";
      param.put("crProcessId", lstId);
    }
    lst = getNamedParameterJdbcTemplate().query(sql, param,
        BeanPropertyRowMapper.newInstance(CrAlarmSettingDTO.class));
    List<CrAlarmSettingDTO> lstTemp = new ArrayList<>();
    if (crAlarmSettingDTO.getLstvendorCode() != null && !crAlarmSettingDTO.getLstvendorCode()
        .isEmpty()) {
      if (lst != null && !lst.isEmpty()) {
        for (CrAlarmSettingDTO alarmSettingDTO : lst) {
          for (String vendor : crAlarmSettingDTO.getLstvendorCode()) {
            if (!StringUtils.isStringNullOrEmpty(alarmSettingDTO.getVendorCode())
                && alarmSettingDTO.getVendorCode().contains(vendor)) {

              if (map.get(alarmSettingDTO.getCasId()) == null) {
                map.put(alarmSettingDTO.getCasId(), alarmSettingDTO);
                lstTemp.add(alarmSettingDTO);
              }
            }
          }
        }
      }
      return lstTemp;
    } else {
      return lst;
    }
  }

  @Override
  public List<CrAlarmSettingDTO> getAlarmSettingByModule(CrAlarmSettingDTO crAlarmSettingDTO) {
    List<CrAlarmSettingDTO> lst;
    Map<Long, CrAlarmSettingDTO> map = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_ALARM, "get-alarm-setting-by-module");
    Map<String, Object> param = new HashMap<>();
    List<String> lstCrProcessId = new ArrayList<>();
    if (StringUtils.isNotNullOrEmpty(crAlarmSettingDTO.getCrProcessName())) {
      lstCrProcessId.addAll(
          Arrays.asList(crAlarmSettingDTO.getCrProcessName().replaceAll(" ", "").split(",")));
    }
    param.put("crProcessId", lstCrProcessId);
    if (StringUtils.isNotNullOrEmpty(crAlarmSettingDTO.getNationCode())) {
      sql += " and a.NATION_CODE = :nationCode ";
      param.put("nationCode", crAlarmSettingDTO.getNationCode());
    }

    lst = getNamedParameterJdbcTemplate().query(sql, param,
        BeanPropertyRowMapper.newInstance(CrAlarmSettingDTO.class));
    Map<String, Long> mapFaultNation = new HashMap<>();
    List<CrAlarmSettingDTO> lstAfter = new ArrayList();
    if (lst != null && lst.size() > 0) {
      lst.forEach(c -> {
        String key = String.valueOf(c.getFaultId()) + String.valueOf(c.getNationCode());
        if (!mapFaultNation.containsKey(key)) {
          lstAfter.add(c);
          mapFaultNation.put(key, c.getCasId());
        }
      });
    }
    List<CrAlarmSettingDTO> lstTemp = new ArrayList<>();
    if (crAlarmSettingDTO.getLstModuleCode() != null && !crAlarmSettingDTO.getLstModuleCode()
        .isEmpty()) {
      if (lstAfter != null && !lstAfter.isEmpty()) {
        for (CrAlarmSettingDTO alarmSettingDTO : lstAfter) {
          for (String module : crAlarmSettingDTO.getLstModuleCode()) {
            if (!StringUtils.isStringNullOrEmpty(alarmSettingDTO.getModuleCode()) && alarmSettingDTO
                .getModuleCode().contains(module)) {
              if (map.get(alarmSettingDTO.getCasId()) == null) {
                map.put(alarmSettingDTO.getCasId(), alarmSettingDTO);
                lstTemp.add(alarmSettingDTO);
              }
            }
          }
        }
      }
      return lstTemp;
    } else {
      return lstAfter;
    }
  }

  @Override
  public List<CrModuleDetailDTO> getListModuleByCr(CrInsiteDTO crInsiteDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_ALARM, "get-list-module-by-cr");
    Map<String, Object> param = new HashMap<>();
    Date createDate = crInsiteDTO.getCreatedDate();

    Date earliestStartDate = crInsiteDTO.getEarliestStartTime();
    if (crInsiteDTO.getCrId() != null) {
      sql += " and cr.cr_id =:p_cr_id  ";
      param.put("p_cr_id", crInsiteDTO.getCrId());
    }
    if (createDate != null) {
      createDate = DateUtils.addDays(createDate, -2);
      sql += " and cr.create_time >= :p_create_date  ";
      param.put("p_create_date", createDate);
    }

    if (earliestStartDate != null && createDate != null
        && earliestStartDate.compareTo(createDate) > 0) {
      earliestStartDate = DateUtils.addDays(earliestStartDate, 2);
      sql += " and cr.create_time <= :p_earLiest_start_time ";
      param.put("p_earLiest_start_time", earliestStartDate);
    }
    return getNamedParameterJdbcTemplate().query(sql, param,
        BeanPropertyRowMapper.newInstance(CrModuleDetailDTO.class));
  }

  @Override
  public List<CrVendorDetailDTO> getListVendorByCr(CrInsiteDTO crInsiteDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_ALARM, "get-list-vendor-by-cr");
    Map<String, Object> param = new HashMap<>();
    if (crInsiteDTO.getCrId() != null) {
      sql += " and cr_id =:p_cr_id  ";
      param.put("p_cr_id", crInsiteDTO.getCrId());
    }
    Date createDate = crInsiteDTO.getCreatedDate();
    if (createDate != null) {
      createDate = DateUtils.addDays(crInsiteDTO.getCreatedDate(), -2);
      sql += " and create_time >= :p_create_date  ";
      param.put("p_create_date", createDate);
    }

    Date earliestStartDate = crInsiteDTO.getEarliestStartTime();
    if (earliestStartDate != null && createDate != null
        && earliestStartDate.compareTo(createDate) > 0) {
      earliestStartDate = DateUtils.addDays(earliestStartDate, 2);
      sql += " and create_time <= :p_earLiest_start_time ";
      param.put("p_earLiest_start_time", earliestStartDate);
    }
    return getNamedParameterJdbcTemplate().query(sql, param,
        BeanPropertyRowMapper.newInstance(CrVendorDetailDTO.class));
  }

  @Override
  public Datatable getAlarmSetting(CrAlarmSettingDTO crAlarmSettingDTO) {
    BaseDto baseDto = sqlSearch(crAlarmSettingDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        crAlarmSettingDTO.getPage(), crAlarmSettingDTO.getPageSize(),
        CrAlarmSettingDTO.class, crAlarmSettingDTO.getSortName(),
        crAlarmSettingDTO.getSortType());

  }

  @Override
  public List<CrAlarmSettingDTO> getListAlarm(List<Long> lstId) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_ALARM, "get-alarm-export");
    if (!lstId.isEmpty()) {
      sql += " and a.ID  IN (:idx) ";
      params.put("idx", lstId);
    }
    return getNamedParameterJdbcTemplate().query(sql, params,
        BeanPropertyRowMapper.newInstance(CrAlarmSettingDTO.class));
  }

  @Override
  public List<CrAlarmSettingDTO> getAlarmSetting(String vendorCode, String moduleCode,
      String nationCode, Long crProcessId, String createdUser, Long array, Long deviceType,
      String alarmName, Long approvalStatus, Long faultId) {
    CrAlarmSettingDTO crAlarmSettingDTO = new CrAlarmSettingDTO();
    crAlarmSettingDTO.setVendorCode(vendorCode);
    crAlarmSettingDTO.setModuleCode(moduleCode);
    crAlarmSettingDTO.setNationCode(nationCode);
    crAlarmSettingDTO.setCrProcessId(crProcessId);
    crAlarmSettingDTO.setCreatedUser(createdUser);
    crAlarmSettingDTO.setArray(array);
    crAlarmSettingDTO.setDeviceType(deviceType);
    crAlarmSettingDTO.setAlarmName(alarmName);
    crAlarmSettingDTO.setApprovalStatus(approvalStatus);
    crAlarmSettingDTO.setFaultId(faultId);
    BaseDto baseDto = sqlSearch(crAlarmSettingDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(CrAlarmSettingDTO.class));
  }

  @Override
  public BaseDto sqlSearch(CrAlarmSettingDTO crAlarmSettingDTO) {
    BaseDto baseDto = new BaseDto();
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_ALARM, "get-alarm-settings");
    if (crAlarmSettingDTO.getCrProcessId() != null) {
      sql += " and a.CR_PROCESS_ID = :crProcessId ";
      params.put("crProcessId", crAlarmSettingDTO.getCrProcessId());
    }
    if (crAlarmSettingDTO.getApprovalStatus() != null) {
      sql += " and a.APPROVAL_STATUS = :approvalStatus ";
      params.put("approvalStatus", crAlarmSettingDTO.getApprovalStatus());
    }
    if (crAlarmSettingDTO.getNationCode() != null && "NOC"
        .equalsIgnoreCase(crAlarmSettingDTO.getNationCode().trim())) {
      sql += " and (a.NATION_CODE = 'NOC' or a.NATION_CODE is null) ";
    } else if (crAlarmSettingDTO.getNationCode() != null && !""
        .equals(crAlarmSettingDTO.getNationCode().trim())) {
      sql += " and a.NATION_CODE = :nationCode ";
      params.put("nationCode", crAlarmSettingDTO.getNationCode());
    }
    if (crAlarmSettingDTO.getVendorCode() != null && !crAlarmSettingDTO.getVendorCode().trim()
        .isEmpty()) {
      sql += " and lower(a.VENDOR_CODE) like :vendorCode escape '\\' ";
      params.put("vendorCode",
          StringUtils.convertLowerParamContains(crAlarmSettingDTO.getVendorCode()));
    }
    if (crAlarmSettingDTO.getModuleCode() != null && !crAlarmSettingDTO.getModuleCode().trim()
        .isEmpty()) {
      sql += " and lower(a.MODULE_CODE) like :moduleCode escape '\\' ";
      params.put("moduleCode",
          StringUtils.convertLowerParamContains(crAlarmSettingDTO.getModuleCode()));
    }
    if (crAlarmSettingDTO.getCreatedUser() != null && !""
        .equals(crAlarmSettingDTO.getCreatedUser())) {
      sql += " and lower(a.CREATED_USER) like :createdUser escape '\\' ";
      params.put("createdUser",
          StringUtils.convertLowerParamContains(crAlarmSettingDTO.getCreatedUser()));
    }
    if (crAlarmSettingDTO.getArray() != null) {
      sql += " and b.IMPACT_SEGMENT_ID = :array ";
      params.put("array", crAlarmSettingDTO.getArray());
    }
    if (crAlarmSettingDTO.getDeviceType() != null) {
      sql += " and b.DEVICE_TYPE_ID = :deviceType ";
      params.put("deviceType", crAlarmSettingDTO.getDeviceType());
    }
    if (crAlarmSettingDTO.getAlarmName() != null && !""
        .equals(crAlarmSettingDTO.getAlarmName())) {
      sql += " and lower(a.FAULT_NAME) like :alarmName escape '\\' ";
    }
    if (crAlarmSettingDTO.getFaultId() != null) {
      sql += " and a.FAULT_ID = :faultId ";
      params.put("faultId", crAlarmSettingDTO.getFaultId());
    }
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(params);
    return baseDto;
  }

  @Override
  public boolean insertListAlarmSetting(List<CrAlarmSettingDTO> dtoList) {
    try {
      if (dtoList == null || dtoList.isEmpty()) {
        return true;
      }
      for (CrAlarmSettingDTO dto : dtoList) {
        dto.setCasId(null);
        getEntityManager().persist(dto.toModel());
      }
      return true;
    } catch (Exception e) {
      log.info(e.getMessage(), e);
    }
    return false;
  }

  @Override
  public List<CrAlarmDTO> getListObjectByCr(CrDTO crDTO) {

    List<CrAlarmDTO> dtoList = new ArrayList<>();
    if (crDTO.getCrId() == null) {
      return dtoList;
    }

    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_ALARM, "on-search-crAlarm-by-cr");
    Map<String, Object> param = new HashMap<>();
    sql += " and cr_id = :p_cr_id ";
    param.put("p_cr_id", crDTO.getCrId());
    Date createDate = StringUtils.isStringNullOrEmpty(crDTO.getCreatedDate()) ? null
        : DateTimeUtils.convertStringToDate(crDTO.getCreatedDate());
    Date earliestStartDate = StringUtils.isStringNullOrEmpty(crDTO.getEarliestStartTime()) ? null
        : DateTimeUtils.convertStringToDate(crDTO.getEarliestStartTime());
    if (crDTO.getCreatedDate() != null) {
      createDate = DateUtils.addDays(createDate, -2);
      sql += " and create_date >= :p_create_date  ";
      param.put("p_create_date", createDate);
    }
    if (earliestStartDate != null && createDate != null
        && earliestStartDate.compareTo(createDate) > 0) {
      earliestStartDate = DateUtils.addDays(earliestStartDate, 30);
      sql += " and create_date <= :p_earLiest_start_time ";
      param.put("p_earLiest_start_time", earliestStartDate);
    }
    return getNamedParameterJdbcTemplate().query(sql, param,
        BeanPropertyRowMapper.newInstance(CrAlarmDTO.class));
  }

  @Override
  public List<CrAlarmInsiteDTO> getListAlarmByProcess(CrInsiteDTO crInsiteDTO) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_ALARM, "get-cr-alarm-by-process-for-sr");
      Map<String, Object> params = new HashMap<>();
      if (StringUtils.isStringNullOrEmpty(crInsiteDTO.getCrProcessId())) {
        return new ArrayList<>();
      }
      sql += " AND a.CR_PROCESS_ID = :crProcessId";
      params.put("crProcessId", crInsiteDTO.getCrProcessId());
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrAlarmInsiteDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }
}
