package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrChecklistsBtsDTO;
import com.viettel.gnoc.maintenance.model.MrChecklistsBtsEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrChecklistBtsRepositoryImpl extends BaseRepository implements
    MrChecklistBtsRepository {

  @Override
  public Datatable getListDataSearchWeb(MrChecklistsBtsDTO mrChecklistsBtsDTO) {
    BaseDto baseDto = sqlGetListDataSearchWeb(mrChecklistsBtsDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrChecklistsBtsDTO.getPage(), mrChecklistsBtsDTO.getPageSize(), MrChecklistsBtsDTO.class,
        mrChecklistsBtsDTO.getSortName(), mrChecklistsBtsDTO.getSortType());
    return datatable;
  }

  @Override
  public ResultInSideDto insertMrChecklistBts(MrChecklistsBtsDTO mrChecklistBtsDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrChecklistsBtsEntity entity = getEntityManager().merge(mrChecklistBtsDTO.toEntity());
    resultInSideDto.setId(entity.getChecklistId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateMrChecklistBts(MrChecklistsBtsDTO mrChecklistBtsDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(mrChecklistBtsDTO.toEntity());
    resultInSideDto.setId(mrChecklistBtsDTO.getChecklistId());
    return resultInSideDto;
  }

  @Override
  public MrChecklistsBtsDTO findMrChecklistBtsByIdFromWeb(Long checklistId) {
    String leeLocale = I18n.getLocale();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CHECKLIST_BTS, "get-List-Data-Search-Web");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("systemMrCheck", Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
    parameters.put("bussinessMrCheck", Constants.APPLIED_BUSSINESS.CHECKLIST_BTS);
    parameters.put("leeLocale", leeLocale);
    parameters.put("bussinessContent", Constants.MR_CHECKLIST_BTS_BUSSINESS_CODE.CONTENT);
    parameters.put("bussinessCapture", Constants.MR_CHECKLIST_BTS_BUSSINESS_CODE.CAPTURE_GUIDE);
    sql += " AND m.CHECKLIST_ID = :checklistId";
    parameters.put("checklistId", checklistId);
    List<MrChecklistsBtsDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(MrChecklistsBtsDTO.class));
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  @Override
  public MrChecklistsBtsDTO findMrChecklistBtsById(Long checklistId) {
    MrChecklistsBtsEntity entity = getEntityManager()
        .find(MrChecklistsBtsEntity.class, checklistId);
    if (entity != null) {
      return entity.toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteMrChecklistBts(Long checklistId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    MrChecklistsBtsEntity entity = getEntityManager()
        .find(MrChecklistsBtsEntity.class, checklistId);
    getEntityManager().remove(entity);
    return resultInSideDTO;
  }

  @Override
  public List<MrChecklistsBtsDTO> getListMrChecklistBtsExport(
      MrChecklistsBtsDTO mrChecklistsBtsDTO) {
    mrChecklistsBtsDTO.setCheckExport(true);
    BaseDto baseDto = sqlGetListDataSearchWeb(mrChecklistsBtsDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrChecklistsBtsDTO.class));
  }

  @Override
  public MrChecklistsBtsDTO checkMrChecklistBtsExit(MrChecklistsBtsDTO mrChecklistBtsDTO) {
    List<MrChecklistsBtsDTO> dataEntity;
    if (Constants.MARKET_CODE.HAITI.equals(String.valueOf(mrChecklistBtsDTO.getMarketCode()))) {
      MrChecklistsBtsDTO objectSearch = new MrChecklistsBtsDTO();
      objectSearch.setMarketCode(mrChecklistBtsDTO.getMarketCode());
      objectSearch.setDeviceType(mrChecklistBtsDTO.getDeviceType());
      objectSearch.setCycle(mrChecklistBtsDTO.getCycle());
      objectSearch.setSupplierCode(mrChecklistBtsDTO.getSupplierCode());
      objectSearch.setMaterialType(mrChecklistBtsDTO.getMaterialType());
      objectSearch.setContent(mrChecklistBtsDTO.getContent());
      dataEntity = getListMrChecklistBtsDTO(objectSearch);
    } else {
      MrChecklistsBtsDTO objectSearch = new MrChecklistsBtsDTO();
      objectSearch.setMarketCode(mrChecklistBtsDTO.getMarketCode());
      objectSearch.setDeviceType(mrChecklistBtsDTO.getDeviceType());
      objectSearch.setCycle(mrChecklistBtsDTO.getCycle());
      objectSearch.setMaterialType(mrChecklistBtsDTO.getMaterialType());
      objectSearch.setContent(mrChecklistBtsDTO.getContent());
      dataEntity = getListMrChecklistBtsDTO(objectSearch);
    }
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0);
    }

    return null;
  }

  public List<MrChecklistsBtsDTO> getListMrChecklistBtsDTO(MrChecklistsBtsDTO dto) {
      Map<String, Object> parameters = new HashMap<>();
      StringBuilder sql = new StringBuilder();

      sql.append("SELECT T1.CHECKLIST_ID checklistId , "
          + "  T1.MARKET_CODE marketCode , "
          + "  T1.ARRAY_CODE arrayCode , "
          + "  T1.DEVICE_TYPE deviceType , "
          + "  T1.MATERIAL_TYPE materialType , "
          + "  T2.CONTENT content , "
          + "  T1.CREATED_USER createdUser , "
          + "  T1.CREATED_TIME createdTime , "
          + "  T1.UPDATED_USER updatedUser , "
          + "  T1.UPDATED_TIME updatedTime , "
          + "  T1.CYCLE cycle , "
          + "  T1.SUPPLIER_CODE supplierCode , "
          + "  T2.PHOTO_REQ photoReq , "
          + "  T2.MIN_PHOTO minPhoto , "
          + "  T2.MAX_PHOTO maxPhoto , "
          + "  T2.PHOTO_REQ photoReq , "
          + "  T2.CAPTURE_GUIDE captureGuide "
          + "FROM OPEN_PM.MR_CHECKLISTS_BTS T1 "
          + "LEFT JOIN OPEN_PM.MR_CHECKLISTS_BTS_DETAIL T2 "
          + "ON T1.CHECKLIST_ID = T2.CHECKLIST_ID "
          + "WHERE 1            =1"
      );
      if (!StringUtils.isStringNullOrEmpty(dto.getMarketCode())) {
        sql.append(" AND T1.MARKET_CODE = :marketCode");
        parameters.put("marketCode", dto.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getArrayCode())) {
        sql.append(" AND T1.ARRAY_CODE = :arrayCode");
        parameters.put("arrayCode", dto.getArrayCode());
      }

      if (!StringUtils.isStringNullOrEmpty(dto.getDeviceType())) {
        sql.append(" AND T1.DEVICE_TYPE = :deviceType");
        parameters.put("deviceType", dto.getDeviceType());
      }

      if (!StringUtils.isStringNullOrEmpty(dto.getMaterialType())) {
        sql.append(" AND T1.MATERIAL_TYPE = :materialType");
        parameters.put("materialType", dto.getMaterialType());
      }

      if (!StringUtils.isStringNullOrEmpty(dto.getCycle())) {
        sql.append(" AND T1.CYCLE = :cycle");
        parameters.put("cycle", dto.getCycle());
      }

      if (!StringUtils.isStringNullOrEmpty(dto.getSupplierCode())) {
        sql.append(" AND T1.SUPPLIER_CODE = :supplierCode");
        parameters.put("supplierCode", dto.getSupplierCode());
      }

      if (!StringUtils.isStringNullOrEmpty(dto.getContent())) {
        sql.append(" AND T2.CONTENT = :content");
        parameters.put("content", dto.getContent());
      }

      if (!StringUtils.isStringNullOrEmpty(dto.getChecklistId())) {
        sql.append(" AND T1.CHECKLIST_ID = :checklistId");
        parameters.put("checklistId", dto.getChecklistId());
      }

      sql.append(" ORDER BY UPDATED_TIME,CREATED_TIME DESC");
      return getNamedParameterJdbcTemplate().query(sql.toString(), parameters, BeanPropertyRowMapper.newInstance(MrChecklistsBtsDTO.class));
  }

  private BaseDto sqlGetListDataSearchWeb(MrChecklistsBtsDTO mrChecklistsBtsDTO) {
    BaseDto baseDto = new BaseDto();
    String sql;
    String sql2 = "";
    String leeLocale = I18n.getLocale();
    Map<String, Object> parameters = new HashMap<>();
    if (mrChecklistsBtsDTO.getCheckExport() != null && mrChecklistsBtsDTO.getCheckExport()) {
      sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CHECKLIST_BTS,
              "get-List-Data-Search-Web-Export");
      parameters.put("systemMrCheck", Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
      parameters.put("bussinessMrCheck", Constants.APPLIED_BUSSINESS.CHECKLIST_BTS);
      parameters.put("leeLocale", leeLocale);
      parameters.put("bussinessContent", Constants.MR_CHECKLIST_BTS_BUSSINESS_CODE.CONTENT);
      parameters.put("bussinessCapture", Constants.MR_CHECKLIST_BTS_BUSSINESS_CODE.CAPTURE_GUIDE);
      sql2 += " ,d.CHECKLIST_DETAIL_ID ASC";
    } else {
      sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CHECKLIST_BTS, "get-List-Data-Search-Web");
    }

    if (StringUtils.isNotNullOrEmpty(mrChecklistsBtsDTO.getMarketCode())) {
      sql += " AND m.MARKET_CODE = :marketCode";
      parameters.put("marketCode", mrChecklistsBtsDTO.getMarketCode());
    }
    if (StringUtils.isNotNullOrEmpty(mrChecklistsBtsDTO.getArrayCode())) {
      sql += " AND m.ARRAY_CODE = :arrayCode";
      parameters.put("arrayCode", mrChecklistsBtsDTO.getArrayCode());
    }
    if (StringUtils.isNotNullOrEmpty(mrChecklistsBtsDTO.getDeviceType())) {
      sql += " AND m.DEVICE_TYPE = :deviceType";
      parameters.put("deviceType", mrChecklistsBtsDTO.getDeviceType());
    }
    if (StringUtils.isNotNullOrEmpty(mrChecklistsBtsDTO.getMaterialType())) {
      sql += " AND m.MATERIAL_TYPE = :materialType";
      parameters.put("materialType", mrChecklistsBtsDTO.getMaterialType());
    }
    if (mrChecklistsBtsDTO.getCycle() != null) {
      sql += " AND m.CYCLE = :cycle";
      parameters.put("cycle", mrChecklistsBtsDTO.getCycle());
    }
    if (StringUtils.isNotNullOrEmpty(mrChecklistsBtsDTO.getSupplierCode())) {
      sql += " AND m.SUPPLIER_CODE = :supplierCode";
      parameters.put("supplierCode", mrChecklistsBtsDTO.getSupplierCode());
    }
    sql += " ORDER BY m.UPDATED_TIME, m.CREATED_TIME DESC";
    sql += sql2;

    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
