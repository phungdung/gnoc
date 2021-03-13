package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.IpccServiceEntity;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class IpccServiceRepositoryImpl extends BaseRepository implements IpccServiceRepository {

  @Override
  public BaseDto sqlSearch(IpccServiceDTO ipccServiceDTO) {
    StringBuilder sql = new StringBuilder();
    sql.append("select a.ipcc_service_id ipccServiceId, a.ipcc_service_code ipccServiceCode,"
        + " a.url, a.function,a.is_default isDefault, a.user_name userName, a.password password")
        .append(" FROM COMMON_GNOC.ipcc_service a ")
        .append(" where 1=1 ");
    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(ipccServiceDTO.getSearchAll())) {
      sql.append(" AND lower(a.ipcc_service_code) LIKE :searchAll ESCAPE '\\' ");
      params.put("searchAll", StringUtils.convertLowerParamContains(ipccServiceDTO.getSearchAll()));
    }
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
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql.toString());
    baseDto.setParameters(params);
    return baseDto;
  }

  @Override
  public Datatable getListIpccServiceDTOPage(IpccServiceDTO ipccServiceDTO) {
    BaseDto baseDto = sqlSearch(ipccServiceDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        ipccServiceDTO.getPage(), ipccServiceDTO.getPageSize(), IpccServiceDTO.class,
        ipccServiceDTO.getSortName(), ipccServiceDTO.getSortType());
  }

  @Override
  public List<IpccServiceDTO> getListIpccServiceAll() {
    IpccServiceDTO searchDTO = new IpccServiceDTO();
    BaseDto baseDto = sqlSearch(searchDTO);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(IpccServiceDTO.class));
  }

  @Override
  public IpccServiceDTO getDeatilIpccServiceById(Long id) {
    IpccServiceDTO ipccServiceDTO = new IpccServiceDTO();
    ipccServiceDTO.setIpccServiceId(id);
    BaseDto baseDto = sqlSearch(ipccServiceDTO);
    List<IpccServiceDTO> lst = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(IpccServiceDTO.class));
    return lst.isEmpty() ? null : lst.get(0);
  }

  @Override
  public ResultInSideDto addOrUpdateIpccService(IpccServiceDTO ipccServiceDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    IpccServiceEntity entity = getEntityManager().merge(ipccServiceDTO.toEntity());
    resultInSideDto.setId(entity.getIpccServiceId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteIpccService(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    IpccServiceEntity entity = getEntityManager().find(IpccServiceEntity.class, id);
    getEntityManager().remove(entity);
    resultInSideDto.setId(entity.getIpccServiceId());
    return resultInSideDto;
  }

  @Override
  public List<IpccServiceEntity> findByIsDefault(Long isDefault) {
    return findByMultilParam(IpccServiceEntity.class, "isDefault",
        isDefault);
  }
}
