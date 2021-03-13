package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRCatalogChildDTO;
import com.viettel.gnoc.sr.model.SRCatalogChildEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SRCatalogChildRepositoryImpl extends BaseRepository implements
    SRCatalogChildRepository {


  @Override
  public List<SRCatalogChildDTO> getListCatalogChild(SRCatalogChildDTO srCatalogChildDTO) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_CATALOG_CHILD, "sr-catalog-child-list");
    Map<String, Object> parameters = new HashMap<>();
    if (srCatalogChildDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(srCatalogChildDTO.getServiceCode())) {
        sqlQuery += " AND b1.SERVICE_CODE = :serviceCode ";
        parameters.put("serviceCode", srCatalogChildDTO.getServiceCode());
      }
      if (!StringUtils.isStringNullOrEmpty(srCatalogChildDTO.getServiceId())) {
        sqlQuery += " AND b1.SERVICE_ID = :serviceId ";
        parameters.put("serviceId", srCatalogChildDTO.getServiceId());
      }
      if (!StringUtils.isStringNullOrEmpty(srCatalogChildDTO.getServiceIdChild())) {
        sqlQuery += " AND b1.SERVICE_ID_CHILD = :serviceIdChild ";
        parameters.put("serviceIdChild", srCatalogChildDTO.getServiceIdChild());
      }
      if (!StringUtils.isStringNullOrEmpty(srCatalogChildDTO.getStatus())) {
        sqlQuery += " AND b2.STATUS = :status ";
        parameters.put("status", srCatalogChildDTO.getStatus());
      }
    }
    sqlQuery += " order by b2.SERVICE_NAME desc ";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRCatalogChildDTO.class));
  }

  @Override
  public ResultInSideDto delete(Long childId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRCatalogChildEntity srCatalogChildEntity = getEntityManager()
        .find(SRCatalogChildEntity.class, childId);
    if (srCatalogChildEntity != null) {
      getEntityManager().remove(srCatalogChildEntity);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteList(List<Long> childIdList) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (childIdList != null && !childIdList.isEmpty()) {
      for (Long del : childIdList) {
        resultInSideDto = delete(del);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto add(SRCatalogChildDTO srCatalogChildDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRCatalogChildEntity srCatalogChildEntity = getEntityManager()
        .merge(srCatalogChildDTO.toEntity());
    resultInSideDto.setId(srCatalogChildEntity.getChildId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertList(List<SRCatalogChildDTO> srCatalogChildDTOList) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (srCatalogChildDTOList != null && !srCatalogChildDTOList.isEmpty()) {
      for (SRCatalogChildDTO dto : srCatalogChildDTOList) {
        resultInSideDto = add(dto);
      }
    }
    return resultInSideDto;
  }

  @Override
  public SRCatalogChildDTO getDetail(Long childId) {
    SRCatalogChildEntity srCatalogChildEntity = getEntityManager()
        .find(SRCatalogChildEntity.class, childId);
    SRCatalogChildDTO srCatalogChildDTO = srCatalogChildEntity.toDTO();
    return srCatalogChildDTO;
  }

}
