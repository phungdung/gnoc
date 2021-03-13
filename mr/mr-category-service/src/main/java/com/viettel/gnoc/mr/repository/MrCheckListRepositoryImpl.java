package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCheckListDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.model.MrCheckListEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrCheckListRepositoryImpl extends BaseRepository implements MrCheckListRepository {

  @Override
  public Datatable onSearch(MrCheckListDTO mrCheckListDTO) {
    BaseDto baseDto = sqlSearch(mrCheckListDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrCheckListDTO.getPage(),
        mrCheckListDTO.getPageSize(), MrCheckListDTO.class,
        mrCheckListDTO.getSortName(), mrCheckListDTO.getSortType());
  }

  @Override
  public List<MrCheckListDTO> onSearchExport(MrCheckListDTO mrCheckListDTO) {
    BaseDto baseDto = sqlSearch(mrCheckListDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrCheckListDTO.class));
  }

  @Override
  public ResultInSideDto insertOrUpdate(MrCheckListDTO mrCheckListDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrCheckListEntity mrCheckListEntity = getEntityManager().merge(mrCheckListDTO.toEntity());
    resultInSideDto.setId(mrCheckListEntity.getCheckListId());
    return resultInSideDto;
  }

  @Override
  public String insertOrUpdateList(List<MrCheckListDTO> listDTO) {
    if (listDTO != null && listDTO.size() > 0) {
      for (MrCheckListDTO checkListDTO : listDTO) {
        insertOrUpdate(checkListDTO);
      }
    }
    return RESULT.SUCCESS;
  }

  @Override
  public ResultInSideDto deleteMrCheckList(Long checkListId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrCheckListEntity entity = getEntityManager().find(MrCheckListEntity.class, checkListId);
    getEntityManager().remove(entity);
    return resultInSideDto;
  }

  @Override
  public MrCheckListDTO getDetail(Long checkListId) {
    MrCheckListEntity mrCheckListEntity = getEntityManager()
        .find(MrCheckListEntity.class, checkListId);
    MrCheckListDTO mrCheckListDTO = mrCheckListEntity.toDTO();
    return mrCheckListDTO;
  }

  @Override
  public List<MrCheckListDTO> getListArrayDeviceTypeNetworkType() {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CHECKLIST, "getListArray-DeviceType-NetworkType");
    Map<String, Object> parameters = new HashMap<>();
    sqlQuery += " order by NLSSORT(arrayCode,'NLS_SORT=vietnamese'),networkType,deviceType ";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters,
            BeanPropertyRowMapper.newInstance(MrCheckListDTO.class));
  }

  //TrungDuong thêm
  //Hàm check trùng trong trường hợp có trường bị null.
  @Override
  public List<MrCheckListDTO> checkListDTOExisted(MrCheckListDTO mrCheckListDTO) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CHECKLIST, "get-MrCheckList-Existed");
    Map<String, Object> parameters = new HashMap<>();
    if (mrCheckListDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(mrCheckListDTO.getMarketCode())) {
        sqlQuery += " AND T1.MARKET_CODE =:marketCode ";
        parameters.put("marketCode", mrCheckListDTO.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCheckListDTO.getArrayCode())) {
        sqlQuery += " AND T1.ARRAY_CODE =:arrayCode ";
        parameters.put("arrayCode", mrCheckListDTO.getArrayCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCheckListDTO.getNetworkType())) {
        sqlQuery += " AND T1.NETWORK_TYPE =:networkType ";
        parameters.put("networkType", mrCheckListDTO.getNetworkType());
      } else {
        sqlQuery += " AND T1.NETWORK_TYPE is null ";
      }
      if (!StringUtils.isStringNullOrEmpty(mrCheckListDTO.getDeviceType())) {
        sqlQuery += " AND T1.DEVICE_TYPE =:deviceType ";
        parameters.put("deviceType", mrCheckListDTO.getDeviceType());
      } else {
        sqlQuery += " AND T1.DEVICE_TYPE is null ";
      }
      if (!StringUtils.isStringNullOrEmpty(mrCheckListDTO.getCycle())) {
        sqlQuery += " AND T1.CYCLE =:cycle ";
        parameters.put("cycle", mrCheckListDTO.getCycle());
      }
//      if (!StringUtils.isStringNullOrEmpty(mrCheckListDTO.getCheckListId())) {
//        sqlQuery += " AND UPPER(T1.CHECKLIST_ID) <> :checklistId ";
//        parameters.put("checklistId", mrCheckListDTO.getCheckListId());
//      }

    }
    List<MrCheckListDTO> listDTOS = getNamedParameterJdbcTemplate().query(sqlQuery, parameters,
        BeanPropertyRowMapper.newInstance(MrCheckListDTO.class));
    if (listDTOS != null && !listDTOS.isEmpty()) {
      return listDTOS;
    }
    return null;
  }

  @Override
  public List<MrDeviceDTO> getListNetworkType() {
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CHECKLIST, "get-list-network-type");
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<MrDeviceDTO> getListDeviceType() {
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CHECKLIST, "get-list-device-type");
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrDeviceDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  public BaseDto sqlSearch(MrCheckListDTO dto) {
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CHECKLIST, "get-List-MrCheckList-Page");
    Map<String, Object> params = new HashMap<>();
    if (dto != null) {
      if (!StringUtils.isStringNullOrEmpty(dto.getMarketName())) {
        sql += " AND LOWER(cl.location_name) LIKE :marketName ESCAPE '\\'";
        params.put("marketName", StringUtils.convertLowerParamContains(dto.getMarketName()));
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getMarketCode())) {
        sql += " AND T1.MARKET_CODE =:marketCode ";
        params.put("marketCode", dto.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getArrayCode())) {
        sql += " AND T1.ARRAY_CODE =:arrayCode ";
        params.put("arrayCode", dto.getArrayCode());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getDeviceType())) {
        sql += " AND T1.DEVICE_TYPE =:deviceType ";
        params.put("deviceType", dto.getDeviceType());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getNetworkType())) {
        sql += " AND T1.NETWORK_TYPE =:networkType ";
        params.put("networkType", dto.getNetworkType());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getCycle())) {
        sql += " AND T1.CYCLE =:cycle ";
        params.put("cycle", dto.getCycle());
      }
    }
    params.put("p_leeLocale", I18n.getLocale());
    sql += " ORDER BY T1.UPDATED_TIME DESC nulls last";
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(params);
    return baseDto;
  }
}
