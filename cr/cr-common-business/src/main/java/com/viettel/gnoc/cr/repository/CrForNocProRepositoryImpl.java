package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrAffectedServiceInfo;
import com.viettel.gnoc.cr.dto.CrAlarmDTO;
import com.viettel.gnoc.cr.dto.CrDetailInfoDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrForNocProDTO;
import com.viettel.gnoc.cr.dto.MiniCrDTO;
import com.viettel.gnoc.cr.dto.MiniImpactedNode;
import com.viettel.gnoc.wo.dto.WorkLogDTO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@Transactional
public class CrForNocProRepositoryImpl extends BaseRepository implements CrForNocProRepository {

  private boolean validateString(String txt) {
    return (txt != null) && (!txt.trim().isEmpty());
  }

  @Override
  public List<Long> getNodeIpId(String deviceCode, String deviceName, String ip,
      String nationCode) {
    if (StringUtils.isNotNullOrEmpty(nationCode)) {
      nationCode = nationCode.trim().toUpperCase();
    }
    List<Long> lst;
    boolean validateDeviceCode = validateString(deviceCode);
    boolean validateDeviceName = validateString(deviceName);
    boolean validateIp = validateString(ip);
    boolean validateNationCode = validateString(nationCode);
    if ((!validateDeviceCode) && (!validateDeviceName) && (!validateIp)) {
      return null;
    }
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder("");
    sql.append(" select data.ipId as ipId from ( select iip.ip_id ipId,iip.ip_id ,iip.ip ip,");
    sql.append(" ide.device_id ,ide.device_code , to_char(ide.device_name) device_Name, ");
    sql.append(" ide.device_code_old, ide.network_type  from common_gnoc.infra_ip iip");
    sql.append(" left join common_gnoc.infra_device ide on ide.device_id = iip.device_id ");

    if ((validateNationCode) && (nationCode != null) && (!"VNM".equals(nationCode))) {
      sql.append("where ide.NATION_CODE = :nationCode and ( 1=0  ");
    } else {
      sql.append("where ( ide.NATION_CODE is null or ide.NATION_CODE ='VNM' ) and ( 1=0  ");
    }
    if (validateDeviceCode) {
      sql.append("or ide.device_code = :deviceCode ");
      parameters.put("deviceCode", deviceCode);
    }
    if (validateDeviceName) {
      sql.append("or to_char(ide.device_name) = :deviceName ");
    }
    if (validateIp) {
      sql.append("or iip.ip =:ip ");
    }

    sql.append(" ) UNION select (1000000 + iisr.server_id) ipId,iisr.server_id ipIdOrgi,");
    sql.append(" iisr.ip ip,ide.device_id deviceId,ide.device_code deviceCode, ");
    sql.append(" to_char(ide.device_name) deviceName,ide.device_code_old deviceCodeOld, ");
    sql.append(" ide.network_type networkType from common_gnoc.infra_it_server iisr ");
    sql.append("left join common_gnoc.infra_device ide on ide.device_id = iisr.server_id ");

    if ((validateNationCode) && (nationCode != null) && (!"VNM".equals(nationCode))) {
      sql.append("where ide.NATION_CODE = :nationCode and ( 1=0  ");
    } else {
      sql.append("where ( ide.NATION_CODE is null or ide.NATION_CODE ='VNM' ) and ( 1=0  ");
    }
    if (validateDeviceCode) {
      sql.append("or ide.device_code = :deviceCode ");
    }
    if (validateDeviceName) {
      sql.append("or to_char(ide.device_name) = :deviceName ");
    }
    if (validateIp) {
      sql.append("or iisr.ip =:ip ");
    }
    sql.append(" ) UNION select (2000000 + icmsr.server_id) ipId,icmsr.server_id ipIdOrgi, ");
    sql.append(" icmsr.dcn_ip ip,ide.device_id deviceId,ide.device_code deviceCode,  ");
    sql.append(" to_char(ide.device_name) deviceName,ide.device_code_old deviceCodeOld, ");
    sql.append(" ide.network_type networkType from common_gnoc.infra_core_mobile_server icmsr ");
    sql.append("left join common_gnoc.infra_device ide on ide.device_id = icmsr.device_id ");
    sql.append("where dcn_ip is not null  ");

    if ((validateNationCode) && (nationCode != null) && (!"VNM".equals(nationCode))) {
      sql.append("and ide.NATION_CODE = :nationCode and ( 1=0  ");
    } else {
      sql.append("and ( ide.NATION_CODE is null or ide.NATION_CODE ='VNM' ) and ( 1=0  ");
    }
    if (validateDeviceCode) {
      sql.append("or ide.device_code = :deviceCode ");
    }
    if (validateDeviceName) {
      sql.append("or to_char(ide.device_name) = :deviceName ");
    }
    if (validateIp) {
      sql.append("or icmsr.dcn_ip =:ip ");
    }

    sql.append(" ) UNION select (3000000 + icmsr.server_id) ipId,icmsr.server_id ipIdOrgi, ");
    sql.append(" icmsr.mpbn_ip ip,ide.device_id deviceId,ide.device_code deviceCode, ");
    sql.append(" to_char(ide.device_name) deviceName,ide.device_code_old deviceCodeOld, ");
    sql.append(" ide.network_type networkType from common_gnoc.infra_core_mobile_server icmsr ");
    sql.append("left join common_gnoc.infra_device ide on ide.device_id = icmsr.device_id ");
    sql.append("where mpbn_ip is not null ");

    if ((validateNationCode) && (nationCode != null) && (!"VNM".equals(nationCode))) {
      sql.append("and ide.NATION_CODE = :nationCode and ( 1=0  ");
      parameters.put("nationCode", nationCode);
    } else {
      sql.append("and ( ide.NATION_CODE is null or ide.NATION_CODE ='VNM' ) and ( 1=0  ");
    }
    if (validateDeviceCode) {
      sql.append("or ide.device_code = :deviceCode ");
    }
    if (validateDeviceName) {
      sql.append("or to_char(ide.device_name) = :deviceName  ");
    }
    if (validateIp) {
      sql.append("or icmsr.mpbn_ip =:ip ");
    }

    sql.append(" ) UNION select (4000000 + ipsr.ip_id) ipId,ipsr.ip_id ipIdOrgi,");
    sql.append(" ipsr.ip ip,ipsr.server_id deviceId,ipsr.server_code deviceCode, ");
    sql.append(" to_char(ipsr.server_code) deviceName,ipsr.server_code deviceCodeOld, ");
    sql.append(" ipsr.server_type networkType from common_gnoc.infra_pstn_server ipsr ");
    sql.append("where 1=0  ");

    if ((validateDeviceCode) && (nationCode != null) && (!"VNM".equals(nationCode))) {
      sql.append("or ipsr.server_code = :deviceCode ");

    }
    if (validateDeviceName) {
      sql.append("or to_char(ipsr.server_code) = :deviceName ");
      parameters.put("deviceName", deviceName);
    }
    if (validateIp) {
      sql.append("or ipsr.ip =:ip ");
      parameters.put("ip", ip);
    }
    sql.append(") data ");
    lst = getNamedParameterJdbcTemplate().queryForList(sql.toString(), parameters, Long.class);
    return lst;
  }

  @Override
  public List<MiniCrDTO> getCrByStateAndActiveTime(Long state, Date activeTime) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CrForNocPro, "getCrByStateAndActiveTime");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("state", state.longValue());
    parameters.put("activeTime", activeTime);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MiniCrDTO.class));
  }

  @Override
  public List<CrForNocProDTO> getCrFromImpactNode(List<Long> crIds, List<List<Long>> listIpIds,
      Date maxTime, Date minTime) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    if ((crIds == null) || (crIds.isEmpty()) || (listIpIds == null) || (listIpIds.isEmpty())) {
      return new ArrayList();
    }
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CrForNocPro, "getCrFromImpactNode");
    for (int i = 0; i < listIpIds.size(); i++) {
      sql += " or a.IP_ID in (:ipId" + i + ") ";
    }
    sql += " ) and a.INSERT_TIME >= TRUNC( TO_DATE( :strMinDAte , 'YYYY/MM/DD HH24:MI:SS')) and a.INSERT_TIME<= :maxTime ) ";

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("crIds", crIds);
    for (int i = 0; i < listIpIds.size(); i++) {
      parameters.put("ipId" + i, (Collection) listIpIds.get(i));
    }
    parameters.put("strMinDAte", dateFormat.format(minTime));
    parameters.put("maxTime", maxTime);
    List<CrForNocProDTO> dataList = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CrForNocProDTO.class));
    List<CrForNocProDTO> result = new ArrayList<>();
    if ((dataList != null) && (!dataList.isEmpty())) {
      for (CrForNocProDTO o : dataList) {
        o.setNodeFromImpactList(true);
        result.add(o);
      }
    }
    return result;
  }

  @Override
  public List<CrForNocProDTO> getCrFromAffectNode(List<Long> crIds, List<List<Long>> listIpIds,
      Date maxTime, Date minTime) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    if ((crIds == null) || (crIds.isEmpty()) || (listIpIds == null) || (listIpIds.isEmpty())) {
      return new ArrayList();
    }
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CrForNocPro, "getCrFromAffectNode");
    for (int i = 0; i < listIpIds.size(); i++) {
      sql += " or a.IP_ID in (:ipId" + i + ") ";
    }
    sql += " ) and a.INSERT_TIME >= TRUNC( TO_DATE( :strMinDAte , 'YYYY/MM/DD HH24:MI:SS')) and a.INSERT_TIME<= :maxTime ) ";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("crIds", crIds);
    for (int i = 0; i < listIpIds.size(); i++) {
      parameters.put("ipId" + i, (Collection) listIpIds.get(i));
    }
    parameters.put("maxTime", maxTime);
    parameters.put("strMinDAte", dateFormat.format(minTime));
    List<CrForNocProDTO> dataList = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CrForNocProDTO.class));
    List<CrForNocProDTO> result = new ArrayList<>();
    if ((dataList != null) && (!dataList.isEmpty())) {
      for (CrForNocProDTO o : dataList) {
        o.setNodeFromAffectList(true);
        result.add(o);
      }
    }
    return result;
  }

  @Override
  public List<CrForNocProDTO> getCrByImpactSegment(Long state, String impactSegmentCode,
      Date activeTime) {
    if (activeTime == null) {
      return new ArrayList();
    }
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CrForNocPro, "getCrByImpactSegment");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(impactSegmentCode)) {
      sql += "and ig.IMPACT_SEGMENT_code = :impactSegmentCode ";
      parameters.put("impactSegmentCode", impactSegmentCode);
    }
    sql += " and cr.EARLIEST_START_TIME <= :activeTime and cr.LATEST_START_TIME >= :activeTime ";
    parameters.put("state", state.longValue());
    parameters.put("activeTime", activeTime);
    List<CrForNocProDTO> dataList = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CrForNocProDTO.class));
    List<CrForNocProDTO> result = new ArrayList<>();
    if ((dataList != null) && (!dataList.isEmpty())) {
      for (CrForNocProDTO o : dataList) {
        o.setNodeFromImpactList(true);
        result.add(o);
      }
    }
    return result;
  }

  @Override
  public List<CrDetailInfoDTO> getListCrDetailInfoDTO(List<Long> stateList, Date earliestTime,
      Date latestTime, String nocType) {
    if (earliestTime == null || latestTime == null || stateList == null || stateList.isEmpty()) {
      return new ArrayList();
    }
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CrForNocPro, "getListCrDetailInfo");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(nocType)) {
      if ("NOC".equalsIgnoreCase(nocType)) {
        sql += " and cr.COUNTRY = 281 ";
      } else if ("NOC_VTP".equalsIgnoreCase(nocType)) {
        sql += " and cr.COUNTRY = 1500289728 ";
      } else if ("NOC_NAT".equalsIgnoreCase(nocType)) {
        sql += " and cr.COUNTRY = 2000289729 ";
      } else if ("NOC_MVT".equalsIgnoreCase(nocType)) {
        sql += " and cr.COUNTRY = 3000289724 ";
      } else if ("NOC_VTL".equalsIgnoreCase(nocType)) {
        sql += " and cr.COUNTRY = 6000289723 ";
      } else if ("NOC_MYT".equalsIgnoreCase(nocType)) {
        sql += " and cr.COUNTRY = 4500000001 ";
      } else if ("NOC_TZN".equalsIgnoreCase(nocType)) {
        sql += " and cr.COUNTRY = 4001000000 ";
      } else if ("NOC_STL".equalsIgnoreCase(nocType)) {
        sql += " and cr.COUNTRY = 5000289722 ";
      } else if ("NOC_VTC".equalsIgnoreCase(nocType)) {
        sql += " and cr.COUNTRY = 1000014581 ";
      } else if ("NOC_VTB".equalsIgnoreCase(nocType)) {
        sql += " and cr.COUNTRY = 3500289726 ";
      }
    }
    parameters.put("stateList", stateList);
    parameters.put("earliestTime", earliestTime);
    parameters.put("latestTime", latestTime);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CrDetailInfoDTO.class));
  }

  @Override
  public List<MiniImpactedNode> getImpactedNodeByCrIdsV2(List<List<Long>> crIds, Date startTime,
      Date endTime) {
    if ((crIds == null) || (crIds.isEmpty()) || startTime == null || endTime == null) {
      return new ArrayList();
    }
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CrForNocPro, "getImpactedNodeByCrIdsV2");
    Map<String, Object> parameters = new HashMap<>();
    if (crIds.size() > 1) {
      for (int i = 1; i < crIds.size(); i++) {
        sql += " or impn.CR_ID in (:crId" + i + ") ";
      }
    }
    sql += " ) and impn.INSERT_TIME >= :startTime and impn.INSERT_TIME <= :endTime ";
    parameters.put("startTime", startTime);
    parameters.put("endTime", endTime);
    for (int i = 0; i < crIds.size(); i++) {
      parameters.put("crId" + i, crIds.get(i));
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MiniImpactedNode.class));
  }

  @Override
  public List<MiniImpactedNode> getAffectedNodeByCrIdsV2(List<List<Long>> crIds, Date startTime,
      Date endTime) {
    if ((crIds == null) || (crIds.isEmpty()) || startTime == null || endTime == null) {
      return new ArrayList();
    }
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CrForNocPro, "getAffectedNodeByCrIdsV2");
    Map<String, Object> parameters = new HashMap<>();
    if (crIds.size() > 1) {
      for (int i = 1; i < crIds.size(); i++) {
        sql += " or impn.CR_ID in (:crId" + i + ") ";
      }
    }
    sql += " ) and impn.INSERT_TIME >= :startTime and impn.INSERT_TIME <= :endTime ";
    parameters.put("startTime", startTime);
    parameters.put("endTime", endTime);

    for (int i = 0; i < crIds.size(); i++) {
      parameters.put("crId" + i, crIds.get(i));
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MiniImpactedNode.class));
  }

  @Override
  public List<CrAlarmDTO> getListAlarm(List<List<Long>> crIds, Date startTime, Date endTime) {
    if ((crIds == null) || (crIds.isEmpty()) || startTime == null || endTime == null) {
      return new ArrayList();
    }
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CrForNocPro, "getListAlarm");
    Map<String, Object> parameters = new HashMap<>();
    if (crIds.size() > 1) {
      for (int i = 1; i < crIds.size(); i++) {
        sql += " or alarm.CR_ID in (:crId" + i + ") ";
      }
    }
    sql += " ) and alarm.CREATE_DATE >= :startTime and alarm.CREATE_DATE <= :endTime  ";
    parameters.put("startTime", startTime);
    parameters.put("endTime", endTime);
    for (int i = 0; i < crIds.size(); i++) {
      parameters.put("crId" + i, crIds.get(i));
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CrAlarmDTO.class));
  }

  @Override
  public List<CrAffectedServiceInfo> getListAffectService(List<List<Long>> crIds, Date startTime,
      Date endTime) {
    if ((crIds == null) || (crIds.isEmpty()) || startTime == null || endTime == null) {
      return new ArrayList();
    }
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CrForNocPro, "getListAffectService");
    Map<String, Object> parameters = new HashMap<>();
    if (crIds.size() > 1) {
      for (int i = 1; i < crIds.size(); i++) {
        sql += " or a.CR_ID in (:crId" + i + ") ";
      }
    }
    sql += " ) and a.INSERT_TIME >= :startTime and a.INSERT_TIME <= :endTime ";
    parameters.put("startTime", startTime);
    parameters.put("endTime", endTime);
    for (int i = 0; i < crIds.size(); i++) {
      parameters.put("crId" + i, crIds.get(i));
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CrAffectedServiceInfo.class));
  }

  @Override
  public UsersDTO getUserByUserName(String username) {
    if (username == null || username.trim().isEmpty()) {
      return null;
    }
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CrForNocPro, "getUserByUserName");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("username", username.trim().toUpperCase());
    List<UsersDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(UsersDTO.class));
    return lst.isEmpty() ? null : lst.get(0);
  }

  @Override
  public WorkLogDTO getWorkLog(String crId) {
    if (crId == null) {
      return new WorkLogDTO();
    }
    String sql = " select USER_GROUP_ACTION userGroupAction, WLG_OBJECT_ID wlgObjectId from OPEN_PM.WORK_LOG where  WLG_OBJECT_ID =:crId and WLG_OBJECT_TYPE = 2 order by WORK_LOG_ID desc ";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("crId", crId);
    List<WorkLogDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WorkLogDTO.class));
    return lst.isEmpty() ? null : lst.get(0);
  }

  @Override
  public List<CrFilesAttachDTO> getFileAttachCrIdsV2(List<List<Long>> crIds, Date startTime,
      Date endTime) {
    List<CrFilesAttachDTO> dataList = null;
    try {
      if ((crIds == null) || (crIds.isEmpty()) || startTime == null || endTime == null) {
        return new ArrayList();
      }
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CrForNocPro, "getFileAttachCrIdsV2");
      Map<String, Object> parameter = new HashMap<>();
      if (crIds.size() > 1) {
        for (int i = 1; i < crIds.size(); i++) {
          sql += " or CR_ID in (:crId" + i + ") ";
        }
      }
      sql += " ) and TIME_ATTACK >= :startTime and TIME_ATTACK <= :endTime ";
      parameter.put("startTime", startTime);
      parameter.put("endTime", endTime);
      for (int i = 0; i < crIds.size(); i++) {
        parameter.put("crId" + i, crIds.get(i));
      }
      dataList = getNamedParameterJdbcTemplate()
          .query(sql, parameter, BeanPropertyRowMapper.newInstance(CrFilesAttachDTO.class));

    } catch (Exception e) {
      log.info(e.getMessage(), e);
    }
    return dataList;
  }
}
