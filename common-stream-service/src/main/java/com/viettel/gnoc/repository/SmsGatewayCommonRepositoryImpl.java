package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.SmsGatewayDTO;
import com.viettel.gnoc.commons.model.IpccServiceEntity;
import com.viettel.gnoc.commons.model.SmsGatewayEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
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
public class SmsGatewayCommonRepositoryImpl extends BaseRepository implements
    SmsGatewayCommonRepository {

  public String getSqlSms(SmsGatewayDTO smsGatewayDTO, Map<String, Object> parameters) {

    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_SMS, "getListSms");

    if (StringUtils.isNotNullOrEmpty(smsGatewayDTO.getSearchAll())) {
      sql += " AND  lower(sgw.ALIAS) LIKE :searchAll ESCAPE '\\' ";
      parameters
          .put("searchAll", StringUtils.convertLowerParamContains(smsGatewayDTO.getSearchAll()));
    }

    if (!StringUtils.isStringNullOrEmpty(smsGatewayDTO.getAlias())) {
      sql += " AND  lower(sgw.ALIAS) LIKE :alias ESCAPE '\\' ";
      parameters
          .put("alias", StringUtils.convertLowerParamContains(smsGatewayDTO.getAlias()));
    }

    if (!StringUtils.isStringNullOrEmpty(smsGatewayDTO.getSender())) {
      sql += " AND  lower(sgw.SENDER) LIKE :sender ESCAPE '\\' ";
      parameters
          .put("sender", StringUtils.convertLowerParamContains(smsGatewayDTO.getSender()));
    }

    sql += " ORDER BY sgw.ALIAS asc";

    return sql;
  }

  @Override
  public Datatable getListSmsGatewayDTO(SmsGatewayDTO smsGatewayDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = getSqlSms(smsGatewayDTO, parameters);

    return getListDataTableBySqlQuery(sql, parameters,
        smsGatewayDTO.getPage(), smsGatewayDTO.getPageSize(), SmsGatewayDTO.class,
        smsGatewayDTO.getSortName(), smsGatewayDTO.getSortType());
  }

  @Override
  public List<SmsGatewayDTO> getListSmsGatewayAll(SmsGatewayDTO smsGatewayDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = getSqlSms(smsGatewayDTO, parameters);

    List<SmsGatewayDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(SmsGatewayDTO.class));

    return list;
  }

  public ResultInSideDto insertOrUpdate(SmsGatewayDTO smsGatewayDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    SmsGatewayEntity entity = getEntityManager().merge(smsGatewayDTO.toEntity());
    resultDto.setId(entity.getSmsGatewayId());

    resultDto.setKey(Constants.RESULT.SUCCESS);
    return resultDto;
  }

  @Override
  public ResultInSideDto updateSmsGateway(SmsGatewayDTO smsGatewayDTO) {
    return insertOrUpdate(smsGatewayDTO);
  }

  @Override
  public ResultInSideDto deleteSmsGateway(Long smsGatewayId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    SmsGatewayEntity entity = getEntityManager().find(SmsGatewayEntity.class, smsGatewayId);
    if (entity != null) {
      getEntityManager().remove(entity);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public SmsGatewayDTO findSmsGatewayById(Long smsGatewayId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    SmsGatewayEntity entity = getEntityManager().find(SmsGatewayEntity.class, smsGatewayId);
    if (entity != null) {
      return entity.toDTO();
    }

    return null;
  }

  @Override
  public ResultInSideDto insertSmsGateway(SmsGatewayDTO smsGatewayDTO) {
    return insertOrUpdate(smsGatewayDTO);
  }

  public String getSQL(IpccServiceDTO ipccServiceDTO, Map<String, Object> parameters) {
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

    return sql.toString();
  }

  @Override
  public Datatable getListIpccServiceDatatable(IpccServiceDTO ipccServiceDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = getSQL(ipccServiceDTO, parameters);

    return getListDataTableBySqlQuery(sql, parameters,
        ipccServiceDTO.getPage(), ipccServiceDTO.getPageSize(), IpccServiceDTO.class,
        ipccServiceDTO.getSortName(), ipccServiceDTO.getSortType());
  }

  @Override
  public List<IpccServiceDTO> getListIpccServiceDTO(IpccServiceDTO ipccServiceDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = getSQL(ipccServiceDTO, parameters);

    List<IpccServiceDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(IpccServiceDTO.class));

    return list;
  }

  public ResultInSideDto insertOrUpdateIpcc(IpccServiceDTO ipccServiceDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    IpccServiceEntity entity = getEntityManager().merge(ipccServiceDTO.toEntity());
    resultDto.setId(entity.getIpccServiceId());

    resultDto.setKey(Constants.RESULT.SUCCESS);
    return resultDto;
  }

  @Override
  public ResultInSideDto insertIpccServiceDTO(IpccServiceDTO ipccServiceDTO) {
    return insertOrUpdateIpcc(ipccServiceDTO);
  }

  @Override
  public ResultInSideDto updateIpccServiceDTO(IpccServiceDTO ipccServiceDTO) {
    return updateIpccServiceDTO(ipccServiceDTO);
  }

  @Override
  public ResultInSideDto deleteIpccServiceDTO(Long ipccServiceId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    IpccServiceEntity entity = getEntityManager().find(IpccServiceEntity.class, ipccServiceId);
    if (entity != null) {
      getEntityManager().remove(entity);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }
}
