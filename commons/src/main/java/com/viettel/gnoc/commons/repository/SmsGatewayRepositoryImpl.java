package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;


@SuppressWarnings("rawtypes")
@Repository
@Slf4j
public class SmsGatewayRepositoryImpl extends BaseRepository implements SmsGatewayRepository {

  @Override
  public List<IpccServiceDTO> getListIpccServiceDTO(IpccServiceDTO ipccServiceDTO) {
    StringBuilder sql = new StringBuilder();
    sql.append("select a.ipcc_service_id ipccServiceId, a.ipcc_service_code ipccServiceCode,"
        + " a.url, a.function,a.is_default isDefault, a.user_name userName, a.password password")
        .append(" FROM COMMON_GNOC.ipcc_service a ")
        .append(" where 1=1 ");
    Map<String, Object> params = new HashMap<>();
    if (ipccServiceDTO.getIpccServiceId() != null && !""
        .equals(ipccServiceDTO.getIpccServiceId())) {
      sql.append(" and a.ipcc_service_id= :ipcc_service_id");
      params.put("ipcc_service_id", ipccServiceDTO.getIpccServiceId());
    }
    if (ipccServiceDTO.getIpccServiceCode() != null && !""
        .equals(ipccServiceDTO.getIpccServiceCode())) {
      sql.append(" and lower(a.ipcc_service_code) like :ipcc_service_code ");
      params.put("ipcc_service_code",
          StringUtils.convertLowerParamContains(ipccServiceDTO.getIpccServiceCode()));
      sql.append(" escape '\\'");
    }
    if (ipccServiceDTO.getUrl() != null && !"".equals(ipccServiceDTO.getUrl())) {
      sql.append(" and lower(a.url) like :p_url ");
      params.put("p_url", StringUtils.convertLowerParamContains(ipccServiceDTO.getUrl()));
      sql.append(" escape '\\'");
    }
    if (ipccServiceDTO.getFunction() != null && !"".equals(ipccServiceDTO.getFunction())) {
      sql.append(" and lower(a.function) like :p_function ");
      params
          .put("p_function", StringUtils.convertLowerParamContains(ipccServiceDTO.getFunction()));
      sql.append(" escape '\\'");
    }
    if (ipccServiceDTO.getIsDefault() != null && !"".equals(ipccServiceDTO.getIsDefault())) {
      sql.append(" and a.is_default= :is_default");
      params.put("is_default", ipccServiceDTO.getIsDefault());
    }
    sql.append(" order by upper(a.ipcc_service_code) ");

    List<IpccServiceDTO> list = getNamedParameterJdbcTemplate().query(sql.toString(), params,
        BeanPropertyRowMapper.newInstance(IpccServiceDTO.class));

    return list;
  }
}
