package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.InfraIpDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.model.CrImpactedNodesEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CrImpactedNodesRepositoryImpl extends BaseRepository implements
    CrImpactedNodesRepository {

  @Override
  public String saveListDTONoIdSession(List<CrImpactedNodesDTO> obj, Date crCreateTime) {
    try {
      if (obj == null) {
        return RESULT.SUCCESS;
      }
      int i = 0;
      for (CrImpactedNodesDTO item : obj) {
        CrImpactedNodesEntity node = item.toEntity();
        if (node == null) {
          continue;
        }
        if (StringUtils.isStringNullOrEmpty(node.getDeviceCode()) && !StringUtils
            .isLongNullOrEmpty(node.getDeviceId())) {
          InfraDeviceDTO deviceDTO = getDeviceById(node.getDeviceId());
          if (deviceDTO != null) {
            node.setDeviceCode(deviceDTO.getDeviceCode());
            node.setDeviceName(deviceDTO.getDeviceName());
          }
        }

        if (StringUtils.isStringNullOrEmpty(node.getIp()) && !StringUtils
            .isLongNullOrEmpty(node.getIpId())) {
          InfraIpDTO ipDTO = getIpById(node.getIpId());
          if (ipDTO != null) {
            node.setIp(ipDTO.getIp());
          }
        }

        CrImpactedNodesEntity crImpactedNodesEntity = item.toEntity();

        if (crCreateTime != null) {
          node.setInsertTime(crCreateTime);
          crImpactedNodesEntity.setInsertTime(crCreateTime);
        } else {
          node.setInsertTime(new Date());
          crImpactedNodesEntity.setInsertTime(new Date());
        }
        getEntityManager().merge(crImpactedNodesEntity);

        i++;
        if (i % 100 == 0) {
          getEntityManager().flush();
        }
      }
      return RESULT.SUCCESS;
    } catch (SecurityException ex) {
      log.error(ex.getMessage(), ex);
      return ex.getMessage();
    }
  }

  @Override
  public void deleteImpactNodeByCrId(String crId, Date crCreateDate) {
    StringBuilder sql = new StringBuilder();
    if (crId == null) {
      return;
    }
    Map<String, Object> params = new HashMap<>();
    params.put("cr_id", crId);
    sql.append(" delete from cr_impacted_nodes ");
    sql.append(" where cr_id = :cr_id");
    if (crCreateDate == null) {
      sql.append(" and insert_time >= trunc(sysdate) - 15");
    } else {
      sql.append(" and insert_time >= trunc(:crDate)");
      params.put("crDate", crCreateDate);
    }
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
    getEntityManager().flush();
  }

  @Override
  public List<InfraDeviceDTO> getListInfraDeviceIpByListIP(List<InfraDeviceDTO> listIp) {
    String sql = SQLBuilder.getSqlQueryById(
        SQLBuilder.SQL_MODULE_CR_IMPACTED_NODES, "get-list-infra-device-ip-by-listIP");
    Map<String, Object> parameters = new HashMap<>();
    if (listIp != null && !listIp.isEmpty()) {
      parameters.put("listIP", listIp);
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(InfraDeviceDTO.class));

  }

  @Override
  public List<InfraDeviceDTO> getListInfraDeviceIpV2(List<String> listIp, String nationCode) {
    if (listIp == null || listIp.isEmpty()) {
      return new ArrayList<>();
    }

    if (nationCode == null || nationCode.trim().isEmpty()) {
      nationCode = "VNM";
    }
    try {
      StringBuilder sql = new StringBuilder();
      sql.append("select vdid.ipId ipId, "
          + " vdid.ip ip, "
          + " vdid.device_Id deviceId, "
          + " vdid.device_Code deviceCode, "
          + " vdid.device_Name deviceName, "
          + " case when vdid.device_Code_Old is null then ''"
          + " else vdid.device_Code_Old end as deviceCodeOld, "
          + " vdid.network_Type networkType"
          + " , decode(vdid.nationCode ,null,'VNM', vdid.nationCode )nationCode "
          + " from ( ");
      sql.append(
          " select iip.ip_id ipId,iip.ip_id ,iip.ip ip,ide.device_id ,ide.device_code ,to_char(ide.device_name) device_Name,ide.device_code_old ,ide.network_type network_Type , ide.NATION_CODE nationCode ");
      sql.append(" from common_gnoc.infra_ip iip ");
      sql.append(" left join common_gnoc.infra_device ide on ide.device_id = iip.device_id ");
      if (!"VNM".equals(nationCode)) {
        sql.append(" and ide.nation_code = iip.nation_code and iip.status = 1 and ide.status = 1 ");
      } else {
        sql.append(
            " where ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null ) and  ( iip.NATION_CODE = 'VNM' or iip.NATION_CODE is null ) ");
      }
      sql.append(" and iip.IP in (:listIp) ");

      sql.append(" UNION ");
      sql.append(" select (1000000 + iisr.server_id) ipId,iisr.server_id ipIdOrgi,iisr.ip ip, ");
      sql.append(
          " ide.device_id deviceId,ide.device_code deviceCode,to_char(ide.device_name) deviceName,ide.device_code_old deviceCodeOld,ide.network_type networkType , ide.NATION_CODE nationCode ");
      sql.append(" from common_gnoc.infra_it_server iisr ");
      sql.append(" left join common_gnoc.infra_device ide on ide.device_id = iisr.server_id ");
      if (!"VNM".equals(nationCode)) {
        sql.append(" and ide.nation_code = iisr.nation_code and ide.status = 1 ");
      } else {
        sql.append(
            " where ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null) and  ( iisr.NATION_CODE = 'VNM' or iisr.NATION_CODE is null ) ");
      }
      sql.append(" and iisr.IP in (:listIp) ");

      sql.append(" UNION ");
      sql.append(
          " select (2000000 + icmsr.server_id) ipId,icmsr.server_id ipIdOrgi,icmsr.dcn_ip ip,ide.device_id deviceId,ide.device_code deviceCode,to_char(ide.device_name) deviceName,ide.device_code_old deviceCodeOld,ide.network_type networkType , ide.NATION_CODE nationCode ");
      sql.append(" from common_gnoc.infra_core_mobile_server icmsr ");
      sql.append(" left join common_gnoc.infra_device ide on ide.device_id = icmsr.device_id ");
      if (!"VNM".equals(nationCode)) {
        sql.append(" and ide.nation_code = icmsr.nation_code and ide.status = 1 ");
      } else {
        sql.append(
            " and ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null  ) and  ( icmsr.NATION_CODE = 'VNM' or icmsr.NATION_CODE is null ) ");
      }
      sql.append(" where mpbn_ip is not null and icmsr.DCN_IP in (:listIp) ");

      sql.append(" UNION ");
      sql.append(
          " select (3000000 + icmsr.server_id) ipId,icmsr.server_id ipIdOrgi,icmsr.mpbn_ip ip,ide.device_id deviceId,ide.device_code deviceCode,to_char(ide.device_name) deviceName,ide.device_code_old deviceCodeOld,ide.network_type networkType , ide.NATION_CODE nationCode ");
      sql.append(" from common_gnoc.infra_core_mobile_server icmsr ");
      sql.append(" left join common_gnoc.infra_device ide on ide.device_id = icmsr.device_id ");
      if (!"VNM".equals(nationCode)) {
        sql.append(" and ide.nation_code = icmsr.nation_code and ide.status = 1 ");
      } else {
        sql.append(
            " and ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null )  and  ( icmsr.NATION_CODE = 'VNM' or icmsr.NATION_CODE is null ) ");
      }
      sql.append(" where mpbn_ip is not null and icmsr.MPBN_IP in (:listIp) ");

      sql.append(" UNION ");
      sql.append(
          " select (4000000 + ipsr.ip_id) ipId,ipsr.ip_id ipIdOrgi,ipsr.ip ip,ipsr.server_id deviceId,ipsr.server_code deviceCode,to_char(ipsr.server_code) deviceName,ipsr.server_code deviceCodeOld,ipsr.server_type networkType ,ipsr.NATION_CODE AS nationCode  ");
      sql.append(" from common_gnoc.infra_pstn_server ipsr ");
      if ("VNM".equals(nationCode)) {
        sql.append(
            " where  ( ipsr.NATION_CODE = 'VNM' or ipsr.NATION_CODE is null ) and  ( ipsr.NATION_CODE = 'VNM' or ipsr.NATION_CODE is null ) ");
      } else {
        sql.append(" where  ipsr.NATION_CODE = :nationCode ");
      }
      sql.append(" and ipsr.IP in (:listIp) ");

      sql.append(" ) vdid ");
      if (!"VNM".equals(nationCode)) {
        sql.append(" where vdid.nationCode =:nationCode1 ");
      }
      Map<String, Object> parameters = new HashMap<>();
      if (!"VNM".equals(nationCode)) {
        parameters.put("nationCode", nationCode);
        parameters.put("nationCode1", nationCode);
      }
      parameters.put("listIp", listIp);
      return getNamedParameterJdbcTemplate()
          .query(sql.toString(), parameters,
              BeanPropertyRowMapper.newInstance(InfraDeviceDTO.class));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return null;
  }

  @Override
  public List<InfraDeviceDTO> getListDeviceByListDevice(List listDevice, String nationCode) {
    List<InfraDeviceDTO> list = new ArrayList<InfraDeviceDTO>();
    List<InfraDeviceDTO> listTemp = new ArrayList<InfraDeviceDTO>();
    if (StringUtils.isStringNullOrEmpty(nationCode)) {
      nationCode = "VNM";
    }

    StringBuffer sql = new StringBuffer();
    sql.append("SELECT ide.device_Id deviceId, "
        + "  ide.device_Code deviceCode, "
        + "  ide.device_Name deviceName, "
        + "  iip.ip, "
        + "  iip.IP_ID ipId,"
        + "  case when ide.device_Code_Old is null then '' else ide.device_Code_Old end as deviceCodeOld,"
        + "  ide.network_Type networkType, decode(ide.nation_Code ,null,'VNM', ide.nation_Code ) nationCode  "
        + " FROM common_gnoc.infra_device ide left join common_gnoc.infra_ip iip on ide.DEVICE_ID = iip.DEVICE_ID "
        + " WHERE 1 = 1 and lower(ide.device_Code) in (:listDevice)  "
        + " AND (ide.NETWORK_CLASS NOT IN ('GPON_NODE', 'GPON_ODF', 'GPON_SPLITER') OR ide.NETWORK_CLASS IS NULL) ");
//        + " AND iip.IP_ID is null ");
    if (!"VNM".equals(nationCode)) {
      sql.append(" AND ide.NATION_CODE =:nationCode and ide.status = 1 and iip.status = 1 ");
    } else {
      sql.append(
          " AND (ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null) and  ( iip.NATION_CODE = 'VNM' or iip.NATION_CODE is null ) ");
    }

    sql.append(" ORDER BY ide.device_Code ");
    Map<String, Object> parameters = new HashMap<>();

    if (!"VNM".equals(nationCode)) {
      parameters.put("nationCode", nationCode);
    }

    parameters.put("listDevice", listDevice);

    Map<String, String> map = new HashMap<String, String>();
    list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(InfraDeviceDTO.class));
    if (list != null) {
      for (InfraDeviceDTO infraDeviceDTO : list) {
        if (infraDeviceDTO.getIp() != null
            && map.get(infraDeviceDTO.getIp().trim()) == null) {
          map.put(infraDeviceDTO.getIp(), infraDeviceDTO.getDeviceCode());
          listTemp.add(infraDeviceDTO);
        }
      }
      if (!listTemp.isEmpty()) {
        list = listTemp;
      }
      return list;
    }
    return list;
  }


  public InfraDeviceDTO getDeviceById(Long deviceId) {
    try {
      if (deviceId != null) {
        StringBuilder sql = new StringBuilder(" select  "
            + "  DEVICE_NAME deviceName,"
            + "  DEVICE_CODE deviceCode"
            + "  from common_gnoc.infra_device "
            + "  where device_id =:deviceId ");
        Map<String, Object> params = new HashMap<>();
        params.put("deviceId", deviceId);
        List<InfraDeviceDTO> lst = getNamedParameterJdbcTemplate()
            .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(InfraDeviceDTO.class));
        if (lst != null && !lst.isEmpty()) {
          lst.get(0);
        }
        return null;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public InfraIpDTO getIpById(Long ipId) {
    try {
      if (ipId != null) {
        StringBuilder sql = new StringBuilder(
            " select ip  from common_gnoc.infra_ip where ip_id =:ipId ");
        Map<String, Object> params = new HashMap<>();
        params.put("ipId", ipId);
        List<InfraIpDTO> lst = getNamedParameterJdbcTemplate()
            .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(InfraIpDTO.class));
        if (lst != null && !lst.isEmpty()) {
          lst.get(0);
        }
        return null;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<CrImpactedNodesDTO> getImpactedNodesByCrIdV2(Long crId, Date startDate,
      Date earlierStartTime, String type) {
    try {
      if (crId != null) {
        StringBuilder sql = new StringBuilder();
        if (Constants.CR_NODE_TYPE.AFFECTED.equals(type)) {
          sql.append("select cins.cans_id cinsId, ");
        } else {
          sql.append("select cins.cins_id cinsId, ");
        }
        sql.append(" cins.ip_id ipId, "
            + "  cins.ip ip, "
            + "  cins.DEVICE_NAME deviceName,"
            + "  cins.DEVICE_CODE deviceCode, "
            + "  cins.DEVICE_ID deviceId, "
            + "  cins.NATION_CODE nationCode  , cins.DT_CODE as dtCode \n");

        if (Constants.CR_NODE_TYPE.AFFECTED.equals(type)) {
          sql.append(" from open_pm.cr_affected_nodes cins\n");
        } else {
          sql.append(" from open_pm.cr_impacted_nodes cins\n");
        }

        sql.append(" where cr_id =:crId \n");

        if (startDate != null) {
          sql.append(" and cins.insert_time >= :startDate ");
        }
        if (earlierStartTime != null) {
          sql.append(" and cins.insert_time <= :earlierStartTime ");
        }
        Map<String, Object> parameters = new HashMap();
        parameters.put("crId", crId);
        if (startDate != null) {
          parameters.put("startDate", startDate);
        }
        if (earlierStartTime != null) {
          parameters.put("earlierStartTime", earlierStartTime);
        }
        return getNamedParameterJdbcTemplate()
            .query(sql.toString(), parameters,
                BeanPropertyRowMapper.newInstance(CrImpactedNodesDTO.class));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<CrImpactedNodesDTO> getImpactedNodesByCrId(Long crId, Date startDate,
      Date earlierStartTime, String type, String deviceCode, String deviceName, String ip) {
    try {
      StringBuilder sql = new StringBuilder();
      Map<String, Object> parameters = new HashMap<>();
      if (Constants.CR_NODE_TYPE.AFFECTED.equals(type)) {
        sql.append("select cins.cans_id cinsId,  ");
      } else {
        sql.append("select cins.cins_id cinsId,  ");
      }
      sql.append(" cins.ip_id ipId,  "
          + " iip.ip ip,  "
          + " iip.device_Name deviceName, "
          + " iip.device_Code deviceCode, "
          + " iip.device_Id deviceId, "
          + " iip.device_Code_Old deviceCodeOld "
          + " ,decode(iip.nationCode,null,'VNM',iip.nationCode) nationCode  , cins.DT_CODE as dtCode  ");
      if (Constants.CR_NODE_TYPE.AFFECTED.equals(type)) {
        sql.append(" from open_pm.cr_affected_nodes cins ");
      } else {
        sql.append(" from open_pm.cr_impacted_nodes cins ");
      }
      sql.append(" left join "
          + " ( "
          + " select iip.ip_id ipId, "
          + " iip.ip_id , "
          + " iip.ip ip, "
          + " ide.device_id , "
          + " ide.device_code , "
          + " to_char(ide.device_name) device_Name, "
          + " ide.device_code_old , "
          + " ide.network_type "
          + " ,ide.nation_code as nationCode "
          + " from common_gnoc.infra_ip iip "
          + " left join common_gnoc.infra_device ide on ide.device_id = iip.device_id) iip "
          + " on iip.ip_id = cins.ip_id  "
          + " where 1 = 1 ");
      if (crId != null) {
        sql.append(" and cr_id = :crId ");
        parameters.put("crId", crId);
      }
      if (startDate != null) {
        sql.append(" and cins.insert_time >= :startDate ");
      }
      if (earlierStartTime != null) {
        sql.append(" and cins.insert_time <= :earlierStartTime ");
      }

      if (StringUtils.isNotNullOrEmpty(deviceCode)) {
        sql.append(" and lower(iip.device_code) like :deviceCode escape '\\' ");
        parameters.put("deviceCode", StringUtils.convertLowerParamContains(deviceCode));
      }
      if (StringUtils.isNotNullOrEmpty(deviceName)) {
        sql.append(" and lower(iip.device_Name) like :deviceName escape '\\' ");
        parameters.put("deviceName", StringUtils.convertLowerParamContains(deviceName));
      }
      if (StringUtils.isNotNullOrEmpty(ip)) {
        sql.append(" and iip.ip like :ip  escape '\\' ");
        parameters.put("ip", StringUtils.convertLowerParamContains(ip));
      }

      sql.append(" and iip.ip is not null "
          + " UNION ");
      //+ "--2 "
      if (Constants.CR_NODE_TYPE.AFFECTED.equals(type)) {
        sql.append("select cins.cans_id cinsId,  ");
      } else {
        sql.append("select cins.cins_id cinsId,  ");
      }
      sql.append(" cins.ip_id ipId,  "
          + " iip.ip ip,  "
          + " iip.device_Name deviceName, "
          + " iip.device_Code deviceCode, "
          + " iip.device_Id deviceId, "
          + " iip.device_Code_Old deviceCodeOld "
          + " ,decode(iip.nationCode,null,'VNM',iip.nationCode) nationCode  , cins.DT_CODE as dtCode   ");
      if (Constants.CR_NODE_TYPE.AFFECTED.equals(type)) {
        sql.append(" from open_pm.cr_affected_nodes cins ");
      } else {
        sql.append(" from open_pm.cr_impacted_nodes cins ");
      }
      sql.append(" left join  "
          + " (select (1000000 + iisr.server_id) ip_Id, "
          + " iisr.server_id ipIdOrgi, "
          + " iisr.ip ip, "
          + " ide.device_id , "
          + " ide.device_code , "
          + " to_char(ide.device_name) device_Name, "
          + " ide.device_code_old , "
          + " ide.network_type networkType "
          + " ,ide.nation_code as nationCode "
          + " from common_gnoc.infra_it_server iisr "
          + " left join common_gnoc.infra_device ide on ide.device_id = iisr.server_id) "
          + " iip  "
          + " on iip.ip_id = cins.ip_id  "
          + " where 1 = 1 \n ");
      if (crId != null) {
        sql.append(" and cr_id = :crId ");
      }
      if (startDate != null) {
        sql.append(" and cins.insert_time >= :startDate ");
      }
      if (earlierStartTime != null) {
        sql.append(" and cins.insert_time <= :earlierStartTime ");
      }

      if (StringUtils.isNotNullOrEmpty(deviceCode)) {
        sql.append(" and lower(iip.device_code) like :deviceCode escape '\\' ");
      }
      if (StringUtils.isNotNullOrEmpty(deviceName)) {
        sql.append(" and lower(iip.device_Name) like :deviceName escape '\\' ");
      }
      if (StringUtils.isNotNullOrEmpty(ip)) {
        sql.append(" and iip.ip like :ip  escape '\\' ");
      }

      sql.append(" and iip.ip is not null "
          + " UNION ");
      //+ "--3 "
      if (Constants.CR_NODE_TYPE.AFFECTED.equals(type)) {
        sql.append("select cins.cans_id cinsId,  ");
      } else {
        sql.append("select cins.cins_id cinsId,  ");
      }
      sql.append(" cins.ip_id ipId,  "
          + " iip.ip ip,  "
          + " iip.device_Name deviceName, "
          + " iip.device_Code deviceCode, "
          + " iip.device_Id deviceId, "
          + " iip.device_Code_Old deviceCodeOld "
          + " ,decode(iip.nationCode,null,'VNM',iip.nationCode) nationCode  , cins.DT_CODE as dtCode  ");
      if (Constants.CR_NODE_TYPE.AFFECTED.equals(type)) {
        sql.append(" from open_pm.cr_affected_nodes cins ");
      } else {
        sql.append(" from open_pm.cr_impacted_nodes cins ");
      }
      sql.append(" left join  "
          + " (select (2000000 + icmsr.server_id) ip_Id, "
          + " icmsr.server_id ipIdOrgi, "
          + " icmsr.dcn_ip ip, "
          + " ide.device_id , "
          + " ide.device_code , "
          + " to_char(ide.device_name) device_Name, "
          + " ide.device_code_old , "
          + " ide.network_type network_Type "
          + " ,ide.nation_code as nationCode "
          + " from common_gnoc.infra_core_mobile_server icmsr "
          + " left join common_gnoc.infra_device ide on ide.device_id = icmsr.device_id "
          + " where mpbn_ip is not null "
          + " UNION "
          + " select (3000000 + icmsr.server_id) ipId, "
          + " icmsr.server_id ipIdOrgi, "
          + " icmsr.mpbn_ip ip, "
          + " ide.device_id deviceId, "
          + " ide.device_code deviceCode, "
          + " to_char(ide.device_name) deviceName, "
          + " ide.device_code_old deviceCodeOld, "
          + " ide.network_type networkType "
          + " ,ide.nation_code as nationCode "
          + " from common_gnoc.infra_core_mobile_server icmsr "
          + " left join common_gnoc.infra_device ide on ide.device_id = icmsr.device_id "
          + " where mpbn_ip is not null "
          + " UNION "
          + " select (4000000 + ipsr.ip_id) ipId, "
          + " ipsr.ip_id ipIdOrgi, "
          + " ipsr.ip ip, "
          + " ipsr.server_id deviceId, "
          + " ipsr.server_code deviceCode, "
          + " to_char(ipsr.server_code) deviceName, "
          + " ipsr.server_code deviceCodeOld, "
          + " ipsr.server_type networkType "
          + " ,ipsr.nation_code as nationCode "
          + " from common_gnoc.infra_pstn_server ipsr) "
          + " iip  "
          + " on iip.ip_id = cins.ip_id  "
          + " where 1=1 \n ");
      if (crId != null) {
        sql.append(" and cr_id = :crId ");
      }
      if (startDate != null) {
        sql.append(" and cins.insert_time >= :startDate ");
      }
      if (earlierStartTime != null) {
        sql.append(" and cins.insert_time <= :earlierStartTime ");
      }
      if (StringUtils.isNotNullOrEmpty(deviceCode)) {
        sql.append(" and lower(iip.device_code) like :deviceCode escape '\\' ");
      }
      if (StringUtils.isNotNullOrEmpty(deviceName)) {
        sql.append(" and lower(iip.device_Name) like :deviceName escape '\\' ");
      }
      if (StringUtils.isNotNullOrEmpty(ip)) {
        sql.append(" and iip.ip like :ip  escape '\\' ");
      }

      sql.append(" and iip.ip is not null  ");
      if (startDate != null) {
        parameters.put("startDate", startDate);
      }
      if (earlierStartTime != null) {
        parameters.put("earlierStartTime", earlierStartTime);
      }
      return getNamedParameterJdbcTemplate()
          .query(sql.toString(), parameters,
              BeanPropertyRowMapper.newInstance(CrImpactedNodesDTO.class));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<CrImpactedNodesDTO> getListCrImpactedNodesDTO(CrImpactedNodesDTO crImpactedNodesDTO) {
    String sql = "SELECT n.* FROM CR_IMPACTED_NODES n WHERE 1 = 1";
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(crImpactedNodesDTO.getCrId())) {
      sql += " AND n.CR_ID = :crId";
      parameters.put("crId", Long.valueOf(crImpactedNodesDTO.getCrId()));
    }
    if (StringUtils.isNotNullOrEmpty(crImpactedNodesDTO.getInsertTime())) {
      sql += " AND n.INSERT_TIME = TO_TIMESTAMP(:insertTime,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("insertTime", crImpactedNodesDTO.getInsertTime());
    }
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(CrImpactedNodesDTO.class));
  }

  @Override
  public List<CrImpactedNodesDTO> onSearch(CrImpactedNodesDTO tDTO, int start, int maxResult,
      String sortType, String sortField) {
    return onSearchEntity(CrImpactedNodesEntity.class, tDTO, start, maxResult, sortType, sortField);
  }
}
