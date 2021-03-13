package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCDCheckListBDDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
import com.viettel.gnoc.maintenance.model.MrCDCheckListBDEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrCDCheckListBDRepositoryImpl extends BaseRepository implements
    MrCDCheckListBDRepository {

  @Override
  public Datatable getListMrCDCheckListBDPage(MrCDCheckListBDDTO mrCDCheckListBDDTO) {
    BaseDto baseDto = sqlSearch(mrCDCheckListBDDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        mrCDCheckListBDDTO.getPage(), mrCDCheckListBDDTO.getPageSize(),
        MrCDCheckListBDDTO.class,
        mrCDCheckListBDDTO.getSortName(), mrCDCheckListBDDTO.getSortType());
  }

  @Override
  public ResultInSideDto insertOrUpdate(MrCDCheckListBDDTO mrCDCheckListBDDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrCDCheckListBDEntity mrCDCheckListBDEntity = getEntityManager()
        .merge(mrCDCheckListBDDTO.toEntity());
    resultInSideDto.setId(mrCDCheckListBDEntity.getCheckListId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long checkListId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrCDCheckListBDEntity mrCDCheckListBDEntity = getEntityManager()
        .find(MrCDCheckListBDEntity.class, checkListId);
    getEntityManager().remove(mrCDCheckListBDEntity);
    return resultInSideDto;
  }

  @Override
  public MrCDCheckListBDDTO getDetail(Long checkListId) {
    MrCDCheckListBDEntity entity = getEntityManager()
        .find(MrCDCheckListBDEntity.class, checkListId);
    MrCDCheckListBDDTO dto = entity.toDTO();
    return dto;
  }

  @Override
  public List<CatItemDTO> getComboboxArray() {
    String sqlQuery = " select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID =37 and status = 1 Order By Item_Name asc ";
    Map<String, Object> parameters = new HashMap<>();
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
  }

  @Override
  public List<MrDeviceCDDTO> getDeviceTypeCbb(String arrayCode) {
    String sqlQuery = " SELECT device_type deviceType FROM (SELECT DISTINCT 'Hệ thống nguồn và hệ thống phụ trợ tại Tổng trạm KV' AS array_code, device_type FROM open_pm.mr_device_cd UNION SELECT DISTINCT array_code, device_type FROM open_pm.mr_device ORDER BY array_code, device_type) WHERE 1=1 ";
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(arrayCode)) {
      sqlQuery += " AND array_code= :arrayCode ";
      parameters.put("arrayCode", arrayCode);
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(MrDeviceCDDTO.class));
  }

  @Override
  public List<CatItemDTO> getComboboxActivities(Long itemId) {
    String sqlQuery = " select * from COMMON_GNOC.CAT_ITEM where 1= 1 ";
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(itemId)) {
      sqlQuery += " AND PARENT_ITEM_ID= :itemId and status = 1 Order By Item_Name asc ";
      parameters.put("itemId", itemId);
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
  }

  @Override
  public List<MrCDCheckListBDDTO> getListMrCDCheckListBDDTO(MrCDCheckListBDDTO cDCheckListBDDTO,
      boolean isCheckDup) {
    StringBuilder sql = new StringBuilder(
        SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_HARD_CD, "get-list-MrCD-CheckListBD"));
    Map<String, Object> params = new HashMap<>();
    params.put("p_leeLocale", I18n.getLocale());
    params.put("applied_system", Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON);
    params.put("bussiness", Constants.COMMON_TRANSLATE_BUSINESS.CAT_ITEM);
    if (cDCheckListBDDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(cDCheckListBDDTO.getMarketCode())) {
        sql.append(" AND UPPER(CD.MARKET_CODE) = :marketCode ");
        params.put("marketCode", cDCheckListBDDTO.getMarketCode().trim().toUpperCase());
      }
      if (!StringUtils.isStringNullOrEmpty(cDCheckListBDDTO.getArrayCode())) {
        sql.append(" AND UPPER(CD.ARRAY_CODE) = :arrayCode ");
        params.put("arrayCode", cDCheckListBDDTO.getArrayCode().trim().toUpperCase());
      }
      if (!StringUtils.isStringNullOrEmpty(cDCheckListBDDTO.getDeviceType())) {
        sql.append(" AND UPPER(CD.DEVICE_TYPE) = :deviceType ");
        params.put("deviceType", cDCheckListBDDTO.getDeviceType().trim().toUpperCase());
      } else {
        if (isCheckDup) {
          sql.append(" AND CD.DEVICE_TYPE IS NULL ");
        }
      }
      if (StringUtils.isNotNullOrEmpty(cDCheckListBDDTO.getCycle())) {
        sql.append("AND CD.CYCLE = :cycle ");
        params.put("cycle", Long.parseLong(cDCheckListBDDTO.getCycle()));
      }

      if (StringUtils.isNotNullOrEmpty(cDCheckListBDDTO.getContent())) {
        sql.append(" AND UPPER(CD.CONTENT) = :content ");
        params.put("content", cDCheckListBDDTO.getContent().trim().toUpperCase());
      }
    }
    sql.append("ORDER BY UPDATED_DATE,CREATED_DATE DESC");
    return getNamedParameterJdbcTemplate()
        .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(MrCDCheckListBDDTO.class));
  }

  @Override
  public List<MrCDCheckListBDDTO> getListAll(MrCDCheckListBDDTO mrCDCheckListBDDTO) {
    BaseDto baseDto = sqlSearch(mrCDCheckListBDDTO);
    List<MrCDCheckListBDDTO> lstDTO = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrCDCheckListBDDTO.class));

    return lstDTO;
  }

  public BaseDto sqlSearch(MrCDCheckListBDDTO mrCDCheckListBDDTO) {
    BaseDto baseDto = new BaseDto();
    StringBuilder sql = new StringBuilder(
        SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_HARD_CD, "get-list-MrCD-CheckListBD"));
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    parameters.put("applied_system", Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON);
    parameters.put("bussiness", Constants.COMMON_TRANSLATE_BUSINESS.CAT_ITEM);
    if (mrCDCheckListBDDTO != null) {

      if (!StringUtils.isStringNullOrEmpty(mrCDCheckListBDDTO.getMarketCode())) {
        sql.append(" and LOWER(CD.MARKET_CODE) like :marketCode escape '\\' ");
        parameters
            .put("marketCode",
                StringUtils.convertLowerParamContains(mrCDCheckListBDDTO.getMarketCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrCDCheckListBDDTO.getArrayCode())) {
        sql.append(" and LOWER(CD.ARRAY_CODE) like :arrayCode escape '\\' ");
        parameters
            .put("arrayCode",
                StringUtils.convertLowerParamContains(mrCDCheckListBDDTO.getArrayCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrCDCheckListBDDTO.getDeviceType())) {
        sql.append(" and LOWER(CD.DEVICE_TYPE) like :deviceType escape '\\' ");
        parameters
            .put("deviceType",
                StringUtils.convertLowerParamContains(mrCDCheckListBDDTO.getDeviceType()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrCDCheckListBDDTO.getCycle())) {
        sql.append(" and CD.CYCLE = :cycle ");
        parameters
            .put("cycle", Long.parseLong(mrCDCheckListBDDTO.getCycle()));
      }
    }
    sql.append(" ORDER BY CD.UPDATED_DATE,CD.CREATED_DATE DESC ");
    baseDto.setSqlQuery(sql.toString());
    baseDto.setParameters(parameters);
    return baseDto;
  }

  public List<MrCDCheckListBDDTO> findDTO(MrCDCheckListBDDTO mrCDCheckListBDDTO) {
    List<MrCDCheckListBDDTO> list = findByMultilParam(MrCDCheckListBDEntity.class, "marketCode",
        mrCDCheckListBDDTO.getMarketCode(),
        "arrayCode", mrCDCheckListBDDTO.getArrayCode(), "deviceType",
        mrCDCheckListBDDTO.getDeviceType(),
        "cycle", Long.valueOf(mrCDCheckListBDDTO.getCycle()), "content",
        mrCDCheckListBDDTO.getContent());

    if (list != null && list.size() > 0) {
      return list;
    }

    return null;
  }

  public List<MrCDCheckListBDDTO> checkThreeParams(MrCDCheckListBDDTO mrCDCheckListBDDTO) {
    List<MrCDCheckListBDDTO> list = findByMultilParam(MrCDCheckListBDEntity.class, "marketCode",
        mrCDCheckListBDDTO.getMarketCode(),
        "arrayCode", mrCDCheckListBDDTO.getArrayCode(),
        "cycle", Long.valueOf(mrCDCheckListBDDTO.getCycle()), "content",
        mrCDCheckListBDDTO.getContent());

    if (list != null && list.size() > 0) {
      return list;
    }

    return null;
  }
}
