package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoPostInspectionDTO;
import com.viettel.gnoc.wo.dto.WoPostInspectionInsideDTO;
import com.viettel.gnoc.wo.model.WoPostInspectionEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoPostInspectionRepositoryImpl extends BaseRepository implements
    WoPostInspectionRepository {

  @Override
  public ResultInSideDto add(WoPostInspectionInsideDTO woPostInspectionDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    WoPostInspectionEntity woPostInspectionEntity = getEntityManager()
        .merge(woPostInspectionDTO.toEntity());
    resultInSideDTO.setId(woPostInspectionEntity.getId());
    return resultInSideDTO;
  }


  @Override
  public ResultInSideDto insertListInsideDTO(List<WoPostInspectionInsideDTO> woPostInspectionDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    for (WoPostInspectionInsideDTO dto : woPostInspectionDTO) {
      resultInSideDTO = add(dto);
    }
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto insertList(List<WoPostInspectionDTO> woPostInspectionDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    for (WoPostInspectionDTO dto : woPostInspectionDTO) {
      resultInSideDTO = add(dto.toInsideDto());
    }
    return resultInSideDTO;
  }

  @Override
  public String getSeqPostInspection(String sequense) {
    return getSeqTableBase(sequense);
  }

  @Override
  public List<WoPostInspectionInsideDTO> getListExistedWoPostInspection(
      WoPostInspectionInsideDTO woPostInspectionDTO) {
    try {
      String sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_POST_INSPECTION,
              "get-list-existed-wo-post-inspection-vsmart");
      Map<String, Object> parameters = new HashMap<>();
      if (!StringUtils.isStringNullOrEmpty(woPostInspectionDTO.getLocationCode())) {
        sqlQuery += " and b.wo_system_id = :locationCode ";
        parameters.put("locationCode", woPostInspectionDTO.getLocationCode() + "_HAU_KIEM");
      }
      if (!StringUtils.isStringNullOrEmpty(woPostInspectionDTO.getWoType()) && "1"
          .equals(woPostInspectionDTO.getWoType())) {
        sqlQuery += " and c.wo_type_code = :woType";
        parameters.put("woType", "VTT_HAU_KIEM_SU_CO");
      } else {
        sqlQuery += " and c.wo_type_code = :woType ";
        parameters.put("woType", "VTT_HAU_KIEM_TRIEN_KHAI");
      }

      return getNamedParameterJdbcTemplate()
          .query(sqlQuery, parameters,
              BeanPropertyRowMapper.newInstance(WoPostInspectionInsideDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<WoPostInspectionInsideDTO> onSearch(WoPostInspectionInsideDTO woPostInspectionDTO,
      int startRow, int pageLength) {
    List<WoPostInspectionInsideDTO> lst = new ArrayList<>();
    try {
      String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO_POST_INSPECTION,
          "get-list-on-search-wo-post-inspection-vsmart");
      Map<String, Object> parameters = new HashMap<>();
      if (!StringUtils.isStringNullOrEmpty(woPostInspectionDTO.getAccount())) {
        sqlQuery += " AND account = :account ";
        parameters.put("account", woPostInspectionDTO.getAccount());
      }
      if (!StringUtils.isStringNullOrEmpty(woPostInspectionDTO.getWoId())) {
        sqlQuery += " AND WO_ID = :woId ";
        parameters.put("woId", woPostInspectionDTO.getWoId());
      }
      sqlQuery += " ORDER BY CREATED_TIME DESC ";

      Query query = getEntityManager().createNativeQuery(sqlQuery);
      query.unwrap(SQLQuery.class).
          addScalar("id", new LongType()).
          addScalar("woId", new LongType()).
          addScalar("account", new StringType()).
          addScalar("point", new LongType()).
          addScalar("note", new StringType()).
          addScalar("checkUserName", new StringType()).
          addScalar("receiveUserName", new StringType()).
          addScalar("result", new StringType()).
          addScalar("createdTime", new DateType()).
          addScalar("woCodePin", new StringType()).
          addScalar("filename", new StringType()).
          setResultTransformer(Transformers.aliasToBean(WoPostInspectionInsideDTO.class));
      if (!parameters.isEmpty()) {
        for (Map.Entry<String, Object> map : parameters.entrySet()) {
          query.setParameter(map.getKey(), map.getValue());
        }
      }
      query.setFirstResult(startRow);
      query.setMaxResults(pageLength);

      lst = query.getResultList();

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public ResultInSideDto deleteListWoPostInspection(
      List<WoPostInspectionInsideDTO> woPostInspectionListDTO) {
    ResultInSideDto res = new ResultInSideDto();
    try {
      if (woPostInspectionListDTO != null && !woPostInspectionListDTO.isEmpty()) {
        for (WoPostInspectionInsideDTO woPostInspectionDTO : woPostInspectionListDTO) {
          WoPostInspectionEntity woPostInspectionEntity = getEntityManager()
              .find(WoPostInspectionEntity.class, woPostInspectionDTO.getId());
          if (woPostInspectionEntity != null) {
            getEntityManager().remove(woPostInspectionEntity);
          }
        }
        res.setKey(Constants.RESULT.SUCCESS);
        res.setMessage(Constants.RESULT.SUCCESS);
      } else {
        res.setKey(Constants.RESULT.ERROR);
        res.setMessage(Constants.RESULT.ERROR);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res.setKey(Constants.RESULT.ERROR);
      res.setMessage(e.getMessage());
    }
    return res;
  }

  @Override
  public ResultInSideDto updateWOPostInspection(WoPostInspectionInsideDTO inspectionDTO) {
    ResultInSideDto res = new ResultInSideDto();
    try {
      res.setKey(Constants.RESULT.SUCCESS);
      res.setMessage(Constants.RESULT.SUCCESS);
      getEntityManager().merge(inspectionDTO.toEntity());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res.setKey(Constants.RESULT.ERROR);
      res.setMessage(e.getMessage());
    }
    return res;
  }

  @Override
  public WoPostInspectionInsideDTO findWoInspectionById(Long woId) {
    WoPostInspectionEntity woPostInspectionEntity = getEntityManager()
        .find(WoPostInspectionEntity.class, woId);
    if (woPostInspectionEntity != null) {
      return woPostInspectionEntity.toDTO();
    }
    return null;
  }


  @Override
  public List<WoPostInspectionInsideDTO> getListWOPostInspection(
      WoPostInspectionInsideDTO woPostInspectionDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(WoPostInspectionEntity.class, woPostInspectionDTO, rowStart, maxRow,
        sortType,
        sortFieldList);
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    WoPostInspectionEntity woEntity = getEntityManager().find(WoPostInspectionEntity.class, id);
    if (woEntity != null) {
      getEntityManager().remove(woEntity);
    } else {
      resultInSideDTO.setKey(RESULT.ERROR);
    }
    return resultInSideDTO;
  }


}
