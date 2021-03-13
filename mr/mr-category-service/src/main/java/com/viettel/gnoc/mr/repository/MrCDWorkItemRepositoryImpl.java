package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCDWorkItemDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
import com.viettel.gnoc.maintenance.model.MrCDWorkItemEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrCDWorkItemRepositoryImpl extends BaseRepository implements MrCDWorkItemRepository {

  @Override
  public Datatable getListMrCDWorkItemPage(MrCDWorkItemDTO mrCDWorkItemDTO) {
    BaseDto baseDto = sqlSearch(mrCDWorkItemDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        mrCDWorkItemDTO.getPage(), mrCDWorkItemDTO.getPageSize(),
        MrCDWorkItemDTO.class,
        mrCDWorkItemDTO.getSortName(), mrCDWorkItemDTO.getSortType());
  }

  @Override
  public ResultInSideDto insertOrUpdate(MrCDWorkItemDTO mrCDWorkItemDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrCDWorkItemEntity mrCDWorkItemEntity = getEntityManager()
        .merge(mrCDWorkItemDTO.toEntity());
    resultInSideDto.setId(mrCDWorkItemEntity.getWiId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long wiId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrCDWorkItemEntity mrCDWorkItemEntity = getEntityManager()
        .find(MrCDWorkItemEntity.class, wiId);
    getEntityManager().remove(mrCDWorkItemEntity);
    return resultInSideDto;
  }

  @Override
  public MrCDWorkItemDTO getDetail(Long wiId) {
    MrCDWorkItemEntity entity = getEntityManager()
        .find(MrCDWorkItemEntity.class, wiId);
    MrCDWorkItemDTO dto = entity.toDTO();
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
  public List<MrCDWorkItemDTO> getListMrCDWorkItemDTO(MrCDWorkItemDTO cDWorkItemDTO) {
    StringBuilder sql = new StringBuilder(
        SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_HARD_CD, "get-list-MrCD-WorkItem"));
    Map<String, Object> params = new HashMap<>();
    if (cDWorkItemDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(cDWorkItemDTO.getMarketCode())) {
        sql.append(" AND UPPER(CD.MARKET_CODE) = :marketCode ");
        params.put("marketCode",
            StringUtils.replaceSpecicalChracterSQL(cDWorkItemDTO.getMarketCode()).toUpperCase());
      }
      if (!StringUtils.isStringNullOrEmpty(cDWorkItemDTO.getArrayCode())) {
        sql.append(" AND UPPER(CD.ARRAY_CODE) = :arrayCode ");
        params.put("arrayCode",
            StringUtils.replaceSpecicalChracterSQL(cDWorkItemDTO.getArrayCode()).toUpperCase());
      }
      if (!StringUtils.isStringNullOrEmpty(cDWorkItemDTO.getDeviceType())) {
        sql.append(" AND UPPER(CD.DEVICE_TYPE) = :deviceType ");
        params.put("deviceType",
            StringUtils.replaceSpecicalChracterSQL(cDWorkItemDTO.getDeviceType()).toUpperCase());
      }
      if (cDWorkItemDTO.getCycle() != null) {
        sql.append("AND UPPER(CD.CYCLE) = :cycle ");
        params.put("deviceType",
            StringUtils.replaceSpecicalChracterSQL(cDWorkItemDTO.getCycle().toString())
                .toUpperCase());
      }
    }
    sql.append("ORDER BY UPDATED_DATE,CREATED_DATE DESC");
    return getNamedParameterJdbcTemplate()
        .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(MrCDWorkItemDTO.class));
  }

  public BaseDto sqlSearch(MrCDWorkItemDTO mrCDWorkItemDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = " select a.*, B.LOCATION_NAME from MR_CD_WORKITEM a left join COMMON_GNOC.CAT_LOCATION b on A.MARKET_CODE = B.LOCATION_ID WHERE 1 = 1";
    Map<String, Object> parameters = new HashMap<>();
    if (mrCDWorkItemDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(mrCDWorkItemDTO.getMarketCode())) {
        sqlQuery += " and LOWER(a.MARKET_CODE) like :marketCode escape '\\' ";
        parameters
            .put("marketCode",
                StringUtils.convertLowerParamContains(mrCDWorkItemDTO.getMarketCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrCDWorkItemDTO.getArrayCode())) {
        sqlQuery += " and LOWER(a.ARRAY_CODE) like :arrayCode escape '\\' ";
        parameters
            .put("arrayCode",
                StringUtils.convertLowerParamContains(mrCDWorkItemDTO.getArrayCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrCDWorkItemDTO.getDeviceType())) {
        sqlQuery += " and LOWER(a.DEVICE_TYPE) like :deviceType escape '\\' ";
        parameters
            .put("deviceType",
                StringUtils.convertLowerParamContains(mrCDWorkItemDTO.getDeviceType()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrCDWorkItemDTO.getCycle())) {
        sqlQuery += " and a.CYCLE = :cycle ";
        parameters
            .put("cycle", mrCDWorkItemDTO.getCycle());
      }
    }
    sqlQuery += " ORDER BY a.UPDATED_DATE,a.CREATED_DATE DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
