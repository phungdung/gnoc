package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.InfraIpDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrAffectedNodesDTO;
import com.viettel.gnoc.cr.model.CrAffectedNodesEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CrAffectedNodeRepositoryImpl extends BaseRepository implements
    CrAffectedNodeRepository {

  @Override
  public void deleteAffectedNodeByCrId(String crId, Date insertTime) {
    StringBuilder sql = new StringBuilder();
    if (crId == null) {
      return;
    }
    Map<String, Object> params = new HashMap<>();
    sql.append(" delete from cr_affected_nodes ");
    sql.append(" where cr_id = :cr_id ");
    params.put("cr_id", crId);
    if (insertTime == null) {
      sql.append(" and insert_time >= trunc(sysdate) - 16");
    } else {
      sql.append(" and insert_time >= trunc(:insertTime) ");
      params.put("insertTime", insertTime);
    }

    getNamedParameterJdbcTemplate().update(sql.toString(), params);
    getEntityManager().flush();
  }

  @Override
  public String saveListDTONoIdSession(List<CrAffectedNodesDTO> obj, Date createDate) {
    try {
      if (obj == null) {
        return RESULT.SUCCESS;
      }
      int i = 0;
      for (CrAffectedNodesDTO item : obj) {
        CrAffectedNodesEntity node = item.toEntity();
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

        if (createDate == null) {
          node.setInsertTime(createDate);
          item.setInsertTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
        } else {
          node.setInsertTime(new Date());
          item.setInsertTime(DateUtil.date2ddMMyyyyHHMMss(createDate));
        }
        getEntityManager().merge(item.toEntity());
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
        List<InfraDeviceDTO> lst = getNamedParameterJdbcTemplate().query(sql.toString(), params,
            BeanPropertyRowMapper.newInstance(InfraDeviceDTO.class));
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
}
