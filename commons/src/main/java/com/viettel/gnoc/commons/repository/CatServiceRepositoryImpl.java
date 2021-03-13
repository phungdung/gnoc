package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CatServiceDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.model.CatServiceEntity;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class CatServiceRepositoryImpl extends BaseRepository implements CatServiceRepository {

  @Override
  public List<CatServiceDTO> getServiceByInfraType(Long infraType) {
    Map<String, Object> lstParam = new HashMap<>();
    String sql =
        "SELECT  DISTINCT a.service_id serviceId, a.service_code serviceCode, a.service_name serviceName,"
            + "  a.service_code_cc1 serviceCodeCc1,"
            + "  a.service_code_cc2 serviceCodeCc2, a.is_check_qos_internet isCheckQosInternet, "
            + "  a.is_check_qos_th isCheckQosTh,"
            + "  a.is_check_qr_code isCheckQrCode, a.is_enable isEnable"
            + "  FROM common_gnoc.cat_service a, common_gnoc.map_technology_service b"
            + "  where a.service_id = b.service_id(+)";

    if (!StringUtils.isStringNullOrEmpty(infraType)) {
      sql += " AND b.technology = :infraType ";
      lstParam.put("infraType", infraType);
    }
    List<CatServiceDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, lstParam, BeanPropertyRowMapper.newInstance(CatServiceDTO.class));
    return list;
  }

  @Override
  public List<CatServiceDTO> getListCatServiceCBB() {
    List<CatServiceEntity> lstEntity = findAll(CatServiceEntity.class);
    List<CatServiceDTO> lstDTO = new ArrayList<>();
    for (CatServiceEntity entity : lstEntity) {
      lstDTO.add(entity.toDTO());
    }
    return lstDTO;
  }

  @Override
  public Datatable getItemServiceMaster(String system, String business,
      String idColName, String nameCol) {
    Datatable datatable = new Datatable();
    List<CatServiceDTO> lst = getListCatServiceCBB();
    if (StringUtils.isStringNullOrEmpty(system)) {
      system = LANGUAGUE_EXCHANGE_SYSTEM.COMMON;
    }
    if (StringUtils.isStringNullOrEmpty(business)) {
      business = APPLIED_BUSSINESS.CAT_SERVICE.toString();
    }
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        system,
        business);
    try {
      lst = setLanguage(lst, lstLanguage, idColName, nameCol);
      datatable.setData(lst);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return datatable;
  }

  @Override
  public Long getServiceIdByCcServiceId(String ccServiceId) {
    String sql = "SELECT c.service_id serviceId "
        + "FROM common_gnoc.cat_service c "
        + "WHERE c.service_code_cc1 = :ccServiceId";
    Map<String, Object> lstParam = new HashMap<>();
    lstParam.put("ccServiceId", ccServiceId);
    List<CatServiceDTO> listCat = getNamedParameterJdbcTemplate()
        .query(sql, lstParam, BeanPropertyRowMapper.newInstance(CatServiceDTO.class));
    if (listCat != null && listCat.size() > 0) {
      return listCat.get(0).getServiceId();
    }
    return null;
  }
}
