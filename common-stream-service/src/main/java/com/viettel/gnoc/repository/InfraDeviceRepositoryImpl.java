package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class InfraDeviceRepositoryImpl extends BaseRepository implements InfraDeviceRepository {

  @Override
  public List<InfraDeviceDTO> getListInfraDeviceIp(InfraDeviceDTO infraDeviceDTO) {
    BaseDto baseDto = sqlSearchToAddNotePT(infraDeviceDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(InfraDeviceDTO.class));
  }

  BaseDto sqlSearchToAddNotePT(InfraDeviceDTO infraDeviceDTO) {
    BaseDto baseDto = new BaseDto();
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append("select iip.ip_id ipId, "
        + " iip.ip ip, "
        + " ide.device_id deviceId, "
        + " ide.device_code deviceCode, "
        + " ide.device_name deviceName, "
        + " ide.device_code_old deviceCodeOld,"
        + " ide.network_type networkType, "
        + " ide.NATION_CODE nationCode "
        + " from infra_ip iip"
        + " right join infra_device ide on ide.device_id = iip.device_id"
        + " where 1=1 ");
    if (infraDeviceDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
        sql.append(
            " and (lower(ide.device_code) like :deviceCode ESCAPE '\\' or lower(ide.device_name) like :deviceName ESCAPE '\\')");
        parameters.put("deviceCode",
            StringUtils.convertLowerParamContains(infraDeviceDTO.getDeviceCode()));
        parameters.put("deviceName",
            StringUtils.convertLowerParamContains(infraDeviceDTO.getDeviceCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        sql.append(" and iip.ip like :ipValue ESCAPE '\\'");
        parameters
            .put("ipValue", "%" + StringUtils.convertLowerParamContains(infraDeviceDTO.getIp()));
      }
    }
    sql.append(" order by ide.device_code ");

    baseDto.setSqlQuery(sql.toString());
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public List<InfraDeviceDTO> getListInfraDeviceIpV2(InfraDeviceDTO infraDeviceDTO) {

    if (infraDeviceDTO == null || infraDeviceDTO.getNationCode() == null || infraDeviceDTO
        .getNationCode().trim().isEmpty()) {
      return new ArrayList<>();
    }

    if (StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())
        && StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
      return new ArrayList<>();
    }
    String nationCode = infraDeviceDTO.getNationCode();
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
          + " vdid.network_Type networkType "
          + " ,vdid.nation_code nationCode "
          + " from ( ");

      sql.append(
          " select iip.ip_id ipId,iip.ip_id ,iip.ip ip,ide.device_id ,ide.device_code ,to_char(ide.device_name) device_Name,ide.device_code_old ,ide.network_type network_Type , decode(ide.nation_code,null,'VNM',ide.nation_code) nation_code ");
      sql.append(" from common_gnoc.infra_ip iip ");
      sql.append(" right join common_gnoc.infra_device ide on ide.device_id = iip.device_id ");
      if (!"VNM".equals(nationCode)) {
        sql.append(" and ide.nation_code = iip.nation_code ");
      }

      if ("VNM".equals(nationCode)) {
        sql.append(
            " where ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null) and (ide.NETWORK_CLASS not in ('GPON_NODE', 'GPON_ODF', 'GPON_SPLITER') or ide.NETWORK_CLASS is null) ");
      } else {
        sql.append(
            " where  ide.NATION_CODE =:nationCode and (iip.STATUS = 1 or iip.STATUS is null) and (ide.STATUS = 1 or ide.STATUS is null) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
        sql.append(
            " and (lower(ide.DEVICE_CODE) like :deviceInfo ESCAPE '\\' or lower(ide.DEVICE_NAME) like :deviceInfo ESCAPE '\\' ) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        sql.append(" and iip.IP like :ip ESCAPE '\\' ");
      }

      sql.append(" UNION ");
      sql.append(" select (1000000 + iisr.server_id) ipId,iisr.server_id ipIdOrgi,iisr.ip ip, ");
      sql.append(
          " ide.device_id deviceId,ide.device_code deviceCode,to_char(ide.device_name) deviceName,"
              + " ide.device_code_old deviceCodeOld,ide.network_type networkType , "
              + " decode(ide.nation_code,null,'VNM',ide.nation_code) nation_code ");
      sql.append(" from common_gnoc.infra_it_server iisr ");
      sql.append(" right join common_gnoc.infra_device ide on ide.device_id = iisr.server_id ");
      if (!"VNM".equals(nationCode)) {
        sql.append(" and ide.nation_code = iisr.nation_code ");
      }

      if ("VNM".equals(nationCode)) {
        sql.append(
            " where ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null ) and (ide.NETWORK_CLASS not in ('GPON_NODE', 'GPON_ODF', 'GPON_SPLITER') or ide.NETWORK_CLASS is null) ");
      } else {
        sql.append(
            " where  ide.NATION_CODE =:nationCode and (ide.STATUS = 1 or ide.STATUS is null) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
        sql.append(
            " and (lower(ide.DEVICE_CODE) like :deviceInfo ESCAPE '\\' or lower(ide.DEVICE_NAME) like :deviceInfo ESCAPE '\\' ) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        sql.append(" and iisr.IP like :ip ESCAPE '\\' ");
      }

      sql.append(" UNION ");
      sql.append(
          " select (2000000 + icmsr.server_id) ipId,icmsr.server_id ipIdOrgi,icmsr.dcn_ip ip,"
              + " ide.device_id deviceId,ide.device_code deviceCode,to_char(ide.device_name) deviceName,"
              + " ide.device_code_old deviceCodeOld,ide.network_type networkType , "
              + " decode(ide.nation_code,null,'VNM',ide.nation_code) nation_code ");
      sql.append(" from common_gnoc.infra_core_mobile_server icmsr ");
      sql.append(" right join common_gnoc.infra_device ide on ide.device_id = icmsr.device_id ");
      if (!"VNM".equals(nationCode)) {
        sql.append(" and ide.nation_code = icmsr.nation_code ");
      }
      sql.append(" where mpbn_ip is not null ");

      if ("VNM".equals(nationCode)) {
        sql.append(
            " and ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null ) and (ide.NETWORK_CLASS not in ('GPON_NODE', 'GPON_ODF', 'GPON_SPLITER') or ide.NETWORK_CLASS is null) ");
      } else {
        sql.append(
            " and  ide.NATION_CODE =:nationCode and (ide.STATUS = 1 or ide.STATUS is null) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
        sql.append(
            " and (lower(ide.DEVICE_CODE) like :deviceInfo ESCAPE '\\' or lower(ide.DEVICE_NAME) like :deviceInfo ESCAPE '\\' ) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        sql.append(" and icmsr.DCN_IP like :ip ESCAPE '\\' ");
      }

      sql.append(" UNION ");
      sql.append(
          " select (3000000 + icmsr.server_id) ipId,icmsr.server_id ipIdOrgi,icmsr.mpbn_ip ip,"
              + " ide.device_id deviceId,ide.device_code deviceCode,to_char(ide.device_name) deviceName,"
              + " ide.device_code_old deviceCodeOld,ide.network_type networkType , "
              + " decode(ide.nation_code,null,'VNM',ide.nation_code) nation_code ");
      sql.append(" from common_gnoc.infra_core_mobile_server icmsr ");
      sql.append(" right join common_gnoc.infra_device ide on ide.device_id = icmsr.device_id ");
      if (!"VNM".equals(nationCode)) {
        sql.append(" and ide.nation_code = icmsr.nation_code ");
      }
      sql.append(" where mpbn_ip is not null ");

      if ("VNM".equals(nationCode)) {
        sql.append(
            " and ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null) and (ide.NETWORK_CLASS not in ('GPON_NODE', 'GPON_ODF', 'GPON_SPLITER') or ide.NETWORK_CLASS is null) ");
      } else {
        sql.append(
            " and  ide.NATION_CODE =:nationCode and (ide.STATUS = 1 or ide.STATUS is null) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
        sql.append(
            " and (lower(ide.DEVICE_CODE) like :deviceInfo ESCAPE '\\' or lower(ide.DEVICE_NAME) like :deviceInfo ESCAPE '\\' ) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        sql.append(" and icmsr.MPBN_IP like :ip ESCAPE '\\' ");
      }

      sql.append(" UNION ");
      sql.append(
          " select (4000000 + ipsr.ip_id) ipId,ipsr.ip_id ipIdOrgi,ipsr.ip ip,ipsr.server_id deviceId,"
              + " ipsr.server_code deviceCode,to_char(ipsr.server_code) deviceName,ipsr.server_code deviceCodeOld,"
              + " ipsr.server_type networkType ,decode(ipsr.nation_code,null,'VNM',ipsr.nation_code) nation_code  ");
      sql.append(" from common_gnoc.infra_pstn_server ipsr ");

      if ("VNM".equals(nationCode)) {
        sql.append(" where  ( ipsr.NATION_CODE = 'VNM' or ipsr.NATION_CODE is null  ) ");
      } else {
        sql.append(" where  ipsr.NATION_CODE = :nationCode ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
        sql.append(
            " and (lower(ipsr.SERVER_CODE) like :deviceInfo ESCAPE '\\' or lower(ipsr.SERVER_CODE) like :deviceInfo ESCAPE '\\' ) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        sql.append(" and ipsr.IP like :ip ESCAPE '\\' ");
      }

      sql.append(" ) vdid ");
      Map<String, Object> params = new HashMap<>();

      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
        params.put("deviceInfo",
            StringUtils.convertLowerParamContains(infraDeviceDTO.getDeviceCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        params.put("ip", StringUtils.convertLowerParamContains(infraDeviceDTO.getIp()));
      }
      if (!"VNM".equals(nationCode)) {
        params.put("nationCode", nationCode);
      }

      List<InfraDeviceDTO> list = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(InfraDeviceDTO.class));
      return list;

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<InfraDeviceDTO> getListInfraDeviceIpV2SrProxy(InfraDeviceDTO infraDeviceDTO) {
    if (infraDeviceDTO == null || infraDeviceDTO.getNationCode() == null || infraDeviceDTO
        .getNationCode().trim().isEmpty()) {
      return new ArrayList<>();
    }

    if (StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())
        && (infraDeviceDTO.getLstIps() == null || infraDeviceDTO.getLstIps().isEmpty())
        && StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
      return new ArrayList<>();
    }
    String nationCode = infraDeviceDTO.getNationCode();
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
          + " vdid.network_Type networkType "
          + " ,vdid.nation_code nationCode "
          + " from ( ");

      sql.append(
          " select iip.ip_id ipId,iip.ip_id ,iip.ip ip,ide.device_id ,ide.device_code ,to_char(ide.device_name) device_Name,ide.device_code_old ,ide.network_type network_Type , decode(ide.nation_code,null,'VNM',ide.nation_code) nation_code ");
      sql.append(" from common_gnoc.infra_ip iip ");
      sql.append(" right join common_gnoc.infra_device ide on ide.device_id = iip.device_id ");
      if (!"VNM".equals(nationCode)) {
        sql.append(" and ide.nation_code = iip.nation_code ");
      }

      if ("VNM".equals(nationCode)) {
        sql.append(
            " where ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null) and (ide.NETWORK_CLASS not in ('GPON_NODE', 'GPON_ODF', 'GPON_SPLITER') or ide.NETWORK_CLASS is null) ");
      } else {
        sql.append(
            " where  ide.NATION_CODE =:nationCode and (iip.STATUS = 1 or iip.STATUS is null) and (ide.STATUS = 1 or ide.STATUS is null) ");
      }
//      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
//        sql.append(
//            " and ( ide.DEVICE_CODE =:deviceInfo or ide.DEVICE_NAME =:deviceInfo ) ");
//      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        sql.append(" and iip.IP =:ip ");
      } else if (infraDeviceDTO.getLstIps() != null && !infraDeviceDTO.getLstIps().isEmpty()) {
        sql.append(" and iip.IP in (:ip) ");
      }

      sql.append(" UNION ");
      sql.append(" select (1000000 + iisr.server_id) ipId,iisr.server_id ipIdOrgi,iisr.ip ip, ");
      sql.append(
          " ide.device_id deviceId,ide.device_code deviceCode,to_char(ide.device_name) deviceName,"
              + " ide.device_code_old deviceCodeOld,ide.network_type networkType , "
              + " decode(ide.nation_code,null,'VNM',ide.nation_code) nation_code ");
      sql.append(" from common_gnoc.infra_it_server iisr ");
      sql.append(" right join common_gnoc.infra_device ide on ide.device_id = iisr.server_id ");
      if (!"VNM".equals(nationCode)) {
        sql.append(" and ide.nation_code = iisr.nation_code ");
      }

      if ("VNM".equals(nationCode)) {
        sql.append(
            " where ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null ) and (ide.NETWORK_CLASS not in ('GPON_NODE', 'GPON_ODF', 'GPON_SPLITER') or ide.NETWORK_CLASS is null) ");
      } else {
        sql.append(
            " where  ide.NATION_CODE =:nationCode and (ide.STATUS = 1 or ide.STATUS is null) ");
      }
//      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
//        sql.append(
//            " and ( ide.DEVICE_CODE =:deviceInfo or ide.DEVICE_NAME =:deviceInfo ) ");
//      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        sql.append(" and iisr.IP =:ip ");
      } else if (infraDeviceDTO.getLstIps() != null && !infraDeviceDTO.getLstIps().isEmpty()) {
        sql.append(" and iisr.IP in (:ip) ");
      }

      sql.append(" UNION ");
      sql.append(
          " select (2000000 + icmsr.server_id) ipId,icmsr.server_id ipIdOrgi,icmsr.dcn_ip ip,"
              + " ide.device_id deviceId,ide.device_code deviceCode,to_char(ide.device_name) deviceName,"
              + " ide.device_code_old deviceCodeOld,ide.network_type networkType , "
              + " decode(ide.nation_code,null,'VNM',ide.nation_code) nation_code ");
      sql.append(" from common_gnoc.infra_core_mobile_server icmsr ");
      sql.append(" right join common_gnoc.infra_device ide on ide.device_id = icmsr.device_id ");
      if (!"VNM".equals(nationCode)) {
        sql.append(" and ide.nation_code = icmsr.nation_code ");
      }
      sql.append(" where mpbn_ip is not null ");

      if ("VNM".equals(nationCode)) {
        sql.append(
            " and ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null ) and (ide.NETWORK_CLASS not in ('GPON_NODE', 'GPON_ODF', 'GPON_SPLITER') or ide.NETWORK_CLASS is null) ");
      } else {
        sql.append(
            " and  ide.NATION_CODE =:nationCode and (ide.STATUS = 1 or ide.STATUS is null) ");
      }
//      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
//        sql.append(
//            " and ( ide.DEVICE_CODE =:deviceInfo or ide.DEVICE_NAME =:deviceInfo ) ");
//      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        sql.append(" and icmsr.IP =:ip ");
      } else if (infraDeviceDTO.getLstIps() != null && !infraDeviceDTO.getLstIps().isEmpty()) {
        sql.append(" and icmsr.DCN_IP in (:ip) ");
      }

      sql.append(" UNION ");
      sql.append(
          " select (3000000 + icmsr.server_id) ipId,icmsr.server_id ipIdOrgi,icmsr.mpbn_ip ip,"
              + " ide.device_id deviceId,ide.device_code deviceCode,to_char(ide.device_name) deviceName,"
              + " ide.device_code_old deviceCodeOld,ide.network_type networkType , "
              + " decode(ide.nation_code,null,'VNM',ide.nation_code) nation_code ");
      sql.append(" from common_gnoc.infra_core_mobile_server icmsr ");
      sql.append(" right join common_gnoc.infra_device ide on ide.device_id = icmsr.device_id ");
      if (!"VNM".equals(nationCode)) {
        sql.append(" and ide.nation_code = icmsr.nation_code ");
      }
      sql.append(" where mpbn_ip is not null ");

      if ("VNM".equals(nationCode)) {
        sql.append(
            " and ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null) and (ide.NETWORK_CLASS not in ('GPON_NODE', 'GPON_ODF', 'GPON_SPLITER') or ide.NETWORK_CLASS is null) ");
      } else {
        sql.append(
            " and  ide.NATION_CODE =:nationCode and (ide.STATUS = 1 or ide.STATUS is null) ");
      }
//      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
//        sql.append(
//            " and ( ide.DEVICE_CODE =:deviceInfo or ide.DEVICE_NAME =:deviceInfo ) ");
//      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        sql.append(" and icmsr.IP =:ip ");
      } else if (infraDeviceDTO.getLstIps() != null && !infraDeviceDTO.getLstIps().isEmpty()) {
        sql.append(" and icmsr.MPBN_IP in (:ip) ");
      }

      sql.append(" UNION ");
      sql.append(
          " select (4000000 + ipsr.ip_id) ipId,ipsr.ip_id ipIdOrgi,ipsr.ip ip,ipsr.server_id deviceId,"
              + " ipsr.server_code deviceCode,to_char(ipsr.server_code) deviceName,ipsr.server_code deviceCodeOld,"
              + " ipsr.server_type networkType ,decode(ipsr.nation_code,null,'VNM',ipsr.nation_code) nation_code  ");
      sql.append(" from common_gnoc.infra_pstn_server ipsr ");

      if ("VNM".equals(nationCode)) {
        sql.append(" where  ( ipsr.NATION_CODE = 'VNM' or ipsr.NATION_CODE is null  ) ");
      } else {
        sql.append(" where  ipsr.NATION_CODE = :nationCode ");
      }
//      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
//        sql.append(
//            " and ( ipsr.SERVER_CODE =:deviceInfo  or ipsr.SERVER_CODE  =:deviceInfo ) ");
//      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        sql.append(" and ipsr.IP =:ip ");
      } else if (infraDeviceDTO.getLstIps() != null && !infraDeviceDTO.getLstIps().isEmpty()) {
        sql.append(" and ipsr.IP in (:ip) ");
      }

      sql.append(" ) vdid ");
      Map<String, Object> params = new HashMap<>();

//      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
//        params.put("deviceInfo", infraDeviceDTO.getDeviceCode());
//      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        params.put("ip", infraDeviceDTO.getIp());
      } else if (infraDeviceDTO.getLstIps() != null && !infraDeviceDTO.getLstIps().isEmpty()) {
        params.put("ip", infraDeviceDTO.getLstIps());
      }
      if (!"VNM".equals(nationCode)) {
        params.put("nationCode", nationCode);
      }

      List<InfraDeviceDTO> list = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(InfraDeviceDTO.class));
      return list;

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<InfraDeviceDTO> geInfraDeviceByIps(List<String> lstIp, String nationCode) {

    if (nationCode == null || nationCode.trim().isEmpty()) {
      nationCode = "VNM";
    }
    nationCode = nationCode.trim().toUpperCase();

    List<InfraDeviceDTO> list = new ArrayList<>();
    if (lstIp == null || lstIp.isEmpty()) {
      return list;
    }

    List<String> newIps = new ArrayList<>();
    for (String ip : lstIp) {
      if (ip == null) {
        continue;
      }
      newIps.add(ip.trim());
    }
    try {
      StringBuilder sql = new StringBuilder();
      Map<String, Object> params = new HashMap<>();
      sql.append(
          " select data.ipId as ipId , data.deviceId as deviceId , data.ip as ip , data.deviceCode as deviceCode , data.deviceName as deviceName , data.deviceCodeOld as deviceCodeOld , data.networkType as networkType , decode(data.nationCode,null,'VNM',data.nationCode) as nationCode ");
      sql.append(" from ( ");

      sql.append(
          " select iip.ip_id ipId, iip.ip_id  ipIdOrgi , iip.ip ip, ide.device_id deviceId , ide.device_code deviceCode,  ");
      sql.append(
          " to_char(ide.device_name) deviceName, ide.device_code_old deviceCodeOld , ide.network_type networkType , ide.NATION_CODE as nationCode  ");
      sql.append(" from common_gnoc.infra_ip iip  ");
      sql.append(
          " right join common_gnoc.infra_device ide on ide.device_id = iip.device_id where iip.ip in (:newIps)  ");
      if ("VNM".equals(nationCode)) {
        sql.append(
            " and ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null ) and (ide.NETWORK_CLASS not in ('GPON_NODE', 'GPON_ODF', 'GPON_SPLITER') or ide.NETWORK_CLASS is null) ");
      } else {
        sql.append(
            " and  ide.NATION_CODE =:nationCode and (iip.STATUS = 1 or iip.STATUS is null) and (ide.STATUS = 1 or ide.STATUS is null) ");
      }
      sql.append(" UNION  ");
      sql.append(
          " select (1000000 + iisr.server_id) ipId, iisr.server_id ipIdOrgi, iisr.ip ip, ide.device_id deviceId, ide.device_code deviceCode, to_char(ide.device_name) deviceName, ide.device_code_old deviceCodeOld, ide.network_type networkType , ide.NATION_CODE as nationCode ");
      sql.append(" from common_gnoc.infra_it_server iisr  ");
      sql.append(
          " right join common_gnoc.infra_device ide on ide.device_id = iisr.server_id where iisr.ip in (:newIps)  ");
      if ("VNM".equals(nationCode)) {
        sql.append(
            " and ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null ) and (ide.NETWORK_CLASS not in ('GPON_NODE', 'GPON_ODF', 'GPON_SPLITER') or ide.NETWORK_CLASS is null) ");
      } else {
        sql.append(
            " and  ide.NATION_CODE =:nationCode and (ide.STATUS = 1 or ide.STATUS is null) ");
      }
      sql.append(" UNION  ");
      sql.append(
          " select (2000000 + icmsr.server_id) ipId, icmsr.server_id ipIdOrgi, icmsr.dcn_ip ip, ide.device_id deviceId, ide.device_code deviceCode, to_char(ide.device_name) deviceName, ide.device_code_old deviceCodeOld, ide.network_type networkType , ide.NATION_CODE as nationCode  ");
      sql.append(" from common_gnoc.infra_core_mobile_server icmsr  ");
      sql.append(
          " right join common_gnoc.infra_device ide on ide.device_id = icmsr.device_id where dcn_ip is not null  and icmsr.dcn_ip in (:newIps) ");
      if ("VNM".equals(nationCode)) {
        sql.append(
            " and ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null ) and (ide.NETWORK_CLASS not in ('GPON_NODE', 'GPON_ODF', 'GPON_SPLITER') or ide.NETWORK_CLASS is null) ");
      } else {
        sql.append(
            " and  ide.NATION_CODE =:nationCode and (ide.STATUS = 1 or ide.STATUS is null) ");
      }

      sql.append(" UNION  ");
      sql.append(
          " select (3000000 + icmsr.server_id) ipId, icmsr.server_id ipIdOrgi, icmsr.mpbn_ip ip, ide.device_id deviceId, ide.device_code deviceCode, to_char(ide.device_name) deviceName, ide.device_code_old deviceCodeOld, ide.network_type networkType , ide.NATION_CODE as nationCode ");
      sql.append(" from common_gnoc.infra_core_mobile_server icmsr  ");
      sql.append(
          " right join common_gnoc.infra_device ide on ide.device_id = icmsr.device_id where mpbn_ip is not null and icmsr.mpbn_ip in (:newIps) ");
      if ("VNM".equals(nationCode)) {
        sql.append(
            " and ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null ) and (ide.NETWORK_CLASS not in ('GPON_NODE', 'GPON_ODF', 'GPON_SPLITER') or ide.NETWORK_CLASS is null) ");
      } else {
        sql.append(
            " and  ide.NATION_CODE =:nationCode and (ide.STATUS = 1 or ide.STATUS is null) ");
      }

      sql.append(" UNION ");

      sql.append(
          " select (4000000 + ipsr.ip_id) ipId, ipsr.ip_id ipIdOrgi, ipsr.ip ip, ipsr.server_id deviceId, ipsr.server_code deviceCode, to_char(ipsr.server_code) deviceName, ipsr.server_code deviceCodeOld, ipsr.server_type networkType ,  ipsr.NATION_CODE as nationCode ");
      sql.append(" from common_gnoc.infra_pstn_server ipsr  ");
      sql.append(" where ipsr.ip in (:newIps)  ");
      if ("VNM".equals(nationCode)) {
        sql.append(" and  ( ipsr.NATION_CODE = 'VNM' or ipsr.NATION_CODE is null  ) ");
      } else {
        sql.append(" and  ipsr.NATION_CODE = :nationCode ");
      }
      sql.append(" ) data  ");
      params.put("newIps", newIps);
      if (!"VNM".equals(nationCode)) {
        params.put("nationCode", nationCode);
      }
      list = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(InfraDeviceDTO.class));
      return list;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  @Override
  public List<InfraDeviceDTO> getListInfraDeviceNonIp(InfraDeviceDTO infraDeviceDTO) {
    String sql = SQLBuilder.getSqlQueryById(
        SQLBuilder.SQL_MODULE_INFRA_DEVICE, "get-list-InfraDevice-NonIp");
    Map<String, Object> parameters = new HashMap<>();
    if (infraDeviceDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
        sql += " and (lower(ide.device_code) like :deviceCode or lower(ide.device_name) like :deviceCode)";
        parameters.put("%" + "deviceCode" + "%", infraDeviceDTO.getDeviceCode());
        parameters.put("%" + "deviceCode" + "%", infraDeviceDTO.getDeviceCode());
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        sql += " and iip.ip like :ip ";
        parameters.put("%" + "ip" + "%", infraDeviceDTO.getIp());
      }
    }
    sql += " order by ide.device_code ";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(InfraDeviceDTO.class));
  }

  @Override
  public List<InfraDeviceDTO> getListInfraDeviceDTOV2(InfraDeviceDTO infraDeviceDTO) {
    List<InfraDeviceDTO> list = new ArrayList<>();
    Map<String, Object> param = new HashMap<>();
    if (infraDeviceDTO == null || infraDeviceDTO.getNationCode() == null || infraDeviceDTO
        .getNationCode().trim().isEmpty()) {
      return new ArrayList<>();
    }

    if (StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())
        && StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
      return new ArrayList<>();
    }
    String nationCode = infraDeviceDTO.getNationCode();
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
          + " vdid.network_Type networkType "
          + " ,vdid.nation_code nationCode "
          + " from ( ");

      sql.append(
          " select iip.ip_id ipId,iip.ip_id ,iip.ip ip,ide.device_id ,ide.device_code ,to_char(ide.device_name) device_Name,ide.device_code_old ,ide.network_type network_Type , decode(ide.nation_code,null,'VNM',ide.nation_code) nation_code ");
      sql.append(" from common_gnoc.infra_ip iip ");
      sql.append(" right join common_gnoc.infra_device ide on ide.device_id = iip.device_id ");
      if (!"VNM".equals(nationCode)) {
        sql.append(" and ide.nation_code = iip.nation_code ");
      }

      if ("VNM".equals(nationCode)) {
        sql.append(
            " where ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null) and (ide.NETWORK_CLASS not in ('GPON_NODE', 'GPON_ODF', 'GPON_SPLITER') or ide.NETWORK_CLASS is null) ");
      } else {
        sql.append(
            " where  ide.NATION_CODE =:nationCode and (iip.STATUS = 1 or iip.STATUS is null) and (ide.STATUS = 1 or ide.STATUS is null) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
        sql.append(
            " and (lower(ide.DEVICE_CODE) like :deviceInfo ESCAPE '\\' or lower(ide.DEVICE_NAME) like :deviceInfo ESCAPE '\\' ) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        sql.append(" and iip.IP like :ip ESCAPE '\\' ");
      }

      sql.append(" UNION ");
      sql.append(" select (1000000 + iisr.server_id) ipId,iisr.server_id ipIdOrgi,iisr.ip ip, ");
      sql.append(
          " ide.device_id deviceId,ide.device_code deviceCode,to_char(ide.device_name) deviceName,"
              + " ide.device_code_old deviceCodeOld,ide.network_type networkType , "
              + " decode(ide.nation_code,null,'VNM',ide.nation_code) nation_code ");
      sql.append(" from common_gnoc.infra_it_server iisr ");
      sql.append(" right join common_gnoc.infra_device ide on ide.device_id = iisr.server_id ");
      if (!"VNM".equals(nationCode)) {
        sql.append(" and ide.nation_code = iisr.nation_code ");
      }

      if ("VNM".equals(nationCode)) {
        sql.append(
            " where ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null ) and (ide.NETWORK_CLASS not in ('GPON_NODE', 'GPON_ODF', 'GPON_SPLITER') or ide.NETWORK_CLASS is null) ");
      } else {
        sql.append(
            " where  ide.NATION_CODE =:nationCode and (ide.STATUS = 1 or ide.STATUS is null) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
        sql.append(
            " and (lower(ide.DEVICE_CODE) like :deviceInfo ESCAPE '\\' or lower(ide.DEVICE_NAME) like :deviceInfo ESCAPE '\\' ) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        sql.append(" and iisr.IP like :ip ESCAPE '\\' ");
      }

      sql.append(" UNION ");
      sql.append(
          " select (2000000 + icmsr.server_id) ipId,icmsr.server_id ipIdOrgi,icmsr.dcn_ip ip,"
              + " ide.device_id deviceId,ide.device_code deviceCode,to_char(ide.device_name) deviceName,"
              + " ide.device_code_old deviceCodeOld,ide.network_type networkType , "
              + " decode(ide.nation_code,null,'VNM',ide.nation_code) nation_code ");
      sql.append(" from common_gnoc.infra_core_mobile_server icmsr ");
      sql.append(" right join common_gnoc.infra_device ide on ide.device_id = icmsr.device_id ");
      if (!"VNM".equals(nationCode)) {
        sql.append(" and ide.nation_code = icmsr.nation_code ");
      }
      sql.append(" where mpbn_ip is not null ");

      if ("VNM".equals(nationCode)) {
        sql.append(
            " and ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null ) and (ide.NETWORK_CLASS not in ('GPON_NODE', 'GPON_ODF', 'GPON_SPLITER') or ide.NETWORK_CLASS is null) ");
      } else {
        sql.append(
            " and  ide.NATION_CODE =:nationCode and (ide.STATUS = 1 or ide.STATUS is null) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
        sql.append(
            " and (lower(ide.DEVICE_CODE) like :deviceInfo ESCAPE '\\' or lower(ide.DEVICE_NAME) like :deviceInfo ESCAPE '\\' ) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        sql.append(" and icmsr.DCN_IP like :ip ESCAPE '\\' ");
      }

      sql.append(" UNION ");
      sql.append(
          " select (3000000 + icmsr.server_id) ipId,icmsr.server_id ipIdOrgi,icmsr.mpbn_ip ip,"
              + " ide.device_id deviceId,ide.device_code deviceCode,to_char(ide.device_name) deviceName,"
              + " ide.device_code_old deviceCodeOld,ide.network_type networkType , "
              + " decode(ide.nation_code,null,'VNM',ide.nation_code) nation_code ");
      sql.append(" from common_gnoc.infra_core_mobile_server icmsr ");
      sql.append(" right join common_gnoc.infra_device ide on ide.device_id = icmsr.device_id ");
      if (!"VNM".equals(nationCode)) {
        sql.append(" and ide.nation_code = icmsr.nation_code ");
      }
      sql.append(" where mpbn_ip is not null ");

      if ("VNM".equals(nationCode)) {
        sql.append(
            " and ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null) and (ide.NETWORK_CLASS not in ('GPON_NODE', 'GPON_ODF', 'GPON_SPLITER') or ide.NETWORK_CLASS is null) ");
      } else {
        sql.append(
            " and  ide.NATION_CODE =:nationCode and (ide.STATUS = 1 or ide.STATUS is null) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
        sql.append(
            " and (lower(ide.DEVICE_CODE) like :deviceInfo ESCAPE '\\' or lower(ide.DEVICE_NAME) like :deviceInfo ESCAPE '\\' ) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        sql.append(" and icmsr.MPBN_IP like :ip ESCAPE '\\' ");
      }

      sql.append(" UNION ");
      sql.append(
          " select (4000000 + ipsr.ip_id) ipId,ipsr.ip_id ipIdOrgi,ipsr.ip ip,ipsr.server_id deviceId,"
              + " ipsr.server_code deviceCode,to_char(ipsr.server_code) deviceName,ipsr.server_code deviceCodeOld,"
              + " ipsr.server_type networkType ,decode(ipsr.nation_code,null,'VNM',ipsr.nation_code) nation_code  ");
      sql.append(" from common_gnoc.infra_pstn_server ipsr ");

      if ("VNM".equals(nationCode)) {
        sql.append(" where  ( ipsr.NATION_CODE = 'VNM' or ipsr.NATION_CODE is null  ) ");
      } else {
        sql.append(" where  ipsr.NATION_CODE = :nationCode ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
        sql.append(
            " and (lower(ipsr.SERVER_CODE) like :deviceInfo ESCAPE '\\' or lower(ipsr.SERVER_CODE) like :deviceInfo ESCAPE '\\' ) ");
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        sql.append(" and ipsr.IP like :ip ESCAPE '\\' ");
      }

      sql.append(" ) vdid ");
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getDeviceCode())) {
        param.put("deviceInfo", StringUtils
            .convertLowerParamContains(infraDeviceDTO.getDeviceCode().trim().toLowerCase()));
      }
      if (!StringUtils.isStringNullOrEmpty(infraDeviceDTO.getIp())) {
        param.put("ip",
            StringUtils.convertLowerParamContains(infraDeviceDTO.getIp().trim().toLowerCase()));
      }
      if (!"VNM".equals(nationCode)) {
        param.put("nationCode", nationCode);

      }
      list = getNamedParameterJdbcTemplate()
          .query(sql.toString(), param, BeanPropertyRowMapper.newInstance(InfraDeviceDTO.class));
      return list;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }
}
