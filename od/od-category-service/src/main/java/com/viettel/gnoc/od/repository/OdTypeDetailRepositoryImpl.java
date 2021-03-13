package com.viettel.gnoc.od.repository;


import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.od.dto.OdTypeDetailDTO;
import com.viettel.gnoc.od.model.OdTypeDetailEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@SuppressWarnings("rawtypes")
@Repository
public class OdTypeDetailRepositoryImpl extends BaseRepository implements OdTypeDetailRepository {

  @SuppressWarnings("unchecked")
  @Override
  public Datatable getListOdTypeDetailPage(OdTypeDetailDTO odTypeDetailDTO) {
    BaseDto baseDto = sqlSearch(odTypeDetailDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        odTypeDetailDTO.getPage(), odTypeDetailDTO.getPageSize(),
        OdTypeDetailDTO.class,
        odTypeDetailDTO.getSortName(), odTypeDetailDTO.getSortType());
  }

  @Override
  public Datatable getListOdTypeDetail(OdTypeDetailDTO odTypeDetailDTO) {
    Datatable dataReturn = new Datatable();
    BaseDto baseDto = sqlSearch(odTypeDetailDTO);
    List<OdTypeDetailDTO> odTypeDetailDTOList = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(),
            baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(OdTypeDetailDTO.class));
    dataReturn.setData(odTypeDetailDTOList);
    return dataReturn;
  }

  @Override
  public OdTypeDetailDTO checkOdTypeDetailExist(Long odTypeId, Long priorityId) {
    List<OdTypeDetailEntity> dataEntity = (List<OdTypeDetailEntity>) findByMultilParam(
        OdTypeDetailEntity.class,
        "odTypeId", odTypeId,
        "priorityId", priorityId);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public List<OdTypeDetailDTO> getListOdTypeDetailByOdTypeId(Long odTypeId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_ODTYPE, "od-type-list-detail");
    Map<String, Object> parameters = new HashMap<>();
    if (odTypeId != null) {
      parameters.put("odTypeId", odTypeId);
    } else {
      return null;
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(OdTypeDetailDTO.class));
  }

  @Override
  public ResultInSideDto add(OdTypeDetailDTO odTypeDetailDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    OdTypeDetailEntity odTypeDetailEntity = getEntityManager().merge(odTypeDetailDTO.toEntity());
    resultInSideDTO.setId(odTypeDetailEntity.getOdTypeId());
    return resultInSideDTO;
  }


  @Override
  public ResultInSideDto insertOrUpdateListOdTypeDetail(List<OdTypeDetailDTO> detailDTOList) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    for (OdTypeDetailDTO odTypeDetailTmp : detailDTOList) {
      if (odTypeDetailTmp.getId() != null) {
        resultInSideDTO = edit(odTypeDetailTmp);
      } else {
        resultInSideDTO = add(odTypeDetailTmp);
      }
    }
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto edit(OdTypeDetailDTO odTypeDetailDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(odTypeDetailDTO.toEntity());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto updateListOdTypeDetail(List<OdTypeDetailDTO> detailDTOList) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    for (OdTypeDetailDTO odTypeDetailTmp : detailDTOList) {
      resultInSideDTO = edit(odTypeDetailTmp);
    }
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    OdTypeDetailEntity odTypeEntity = getEntityManager().find(OdTypeDetailEntity.class, id);
    getEntityManager().remove(odTypeEntity);
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteOdTypeDetail(List<OdTypeDetailDTO> lstOdTypeDetailDTO,
      Long odTypeId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    for (OdTypeDetailDTO dto : lstOdTypeDetailDTO) {
      OdTypeDetailDTO odTypeDetailDTO = checkOdTypeDetailExist(odTypeId, dto.getPriorityId());
      resultInSideDTO = delete(odTypeDetailDTO.getId());
    }
    return resultInSideDTO;
  }

  public BaseDto sqlSearch(OdTypeDetailDTO odTypeDetailDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_ODTYPE, "od-type-detail");
    Map<String, Object> parameters = new HashMap<>();
    if (odTypeDetailDTO != null) {
      if (odTypeDetailDTO.getOdTypeId() != null) {
        sqlQuery += " and OD_TYPE_ID = :odTypeId ";
        parameters.put("odTypeId", odTypeDetailDTO.getOdTypeId());
      }
      if (!StringUtils.isStringNullOrEmpty(odTypeDetailDTO.getPriorityId())) {
        sqlQuery += " and PRIORITY_ID = :priorityId ";
        parameters.put("priorityId", odTypeDetailDTO.getPriorityId());
      }
    }
    sqlQuery += " order by PRIORITY_ID";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
