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
import com.viettel.gnoc.maintenance.dto.ConfigPropertyDTO;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrSearchDTO;
import com.viettel.gnoc.maintenance.model.MrEntity;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrServiceRepositoryImpl extends BaseRepository implements MrServiceRepository {

  @Override
  public Datatable getListMrCrWoNew(MrSearchDTO dto) {
    try {
      BaseDto baseDto = sqlGetListMrCrWoNew(dto);
      return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(), dto.getPage(), dto.getPageSize(), MrDTO.class, dto.getSortName(), dto.getSortType());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<MrDTO> getListMrCrWoNewForExport(MrSearchDTO dto) {
    try {
      BaseDto baseDto = sqlGetListMrCrWoNew(dto);
      return getNamedParameterJdbcTemplate()
          .query(baseDto.getSqlQuery(), baseDto.getParameters(),
              BeanPropertyRowMapper.newInstance(MrDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<MrDTO> getWorklogFromWo(MrSearchDTO dto) {
    BaseDto baseDto = sqlGetWorklogFromWo(dto);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrDTO.class));
  }

  @Override
  public List<String> getIdSequence() {
    BaseDto baseDto = sqlGetIdSequence();
    List<MrDTO> list = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrDTO.class));
    String mrId = list.get(0).getMrId();
    List<String> lst = new ArrayList<>();
    lst.add(mrId);
    return lst;
  }

  public BaseDto sqlGetIdSequence() {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "get-id-sequence");
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  public BaseDto sqlGetWorklogFromWo(MrSearchDTO dto) {
    Map<String, Object> parameter = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "get-worklog-from-wo");
    parameter.put("fromDateWo", dto.getDateCreateWoFrom());
    parameter.put("toDateWo", dto.getDateCreateWoTo());
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameter);
    return baseDto;
  }

  public BaseDto sqlGetListMrCrWoNew(MrSearchDTO dto) {
    Map<String, Object> params = new HashMap<>();
    String sql = "";
    String sqlDB = "";
    if ("S".equals(dto.getMrTypeName())) {
      sqlDB = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "get-List-Mr-Cr-Wo-New");
      sql += sqlDB;
      //MR
      params.put("startDate", dto.getStartDateMr());
      params.put("endDate", dto.getEndDateMr() + " 23:59:59");

      if (!StringUtils.isStringNullOrEmpty(dto.getConsiderUnitCR())) {
        sql += " and (considerUnitId IN(SELECT UNIT_ID FROM COMMON_GNOC.UNIT START WITH UNIT_ID = :considerUnitId CONNECT BY prior PARENT_UNIT_ID = UNIT_ID and LEVEL <= 2";
        params.put("considerUnitId", dto.getConsiderUnitCR());
        if (!StringUtils.isStringNullOrEmpty(dto.getParent_Consider())) {
          sql +=
              " UNION SELECT UNIT_ID FROM COMMON_GNOC.UNIT START WITH UNIT_ID = :considerUnitId CONNECT BY prior UNIT_ID = PARENT_UNIT_ID)";
          params.put("considerUnitId", dto.getConsiderUnitCR());
        } else {
          sql += ")";
        }
        sql += " ) ";

      }
      if (!StringUtils.isStringNullOrEmpty(dto.getResponsibleUnitCR())) {
        sql +=
            " and (changeResponsibleUnit IN (SELECT UNIT_ID FROM COMMON_GNOC.UNIT START WITH UNIT_ID = :responsibleUnitCR CONNECT BY prior PARENT_UNIT_ID = UNIT_ID and LEVEL <= 2";
        params.put("responsibleUnitCR", dto.getResponsibleUnitCR());
        if (!StringUtils.isStringNullOrEmpty(dto.getParent_Responsible())) {
          sql +=
              " UNION SELECT UNIT_ID FROM COMMON_GNOC.UNIT START WITH UNIT_ID = :responsibleUnitCR CONNECT BY prior UNIT_ID = PARENT_UNIT_ID)";
          params.put("responsibleUnitCR", dto.getResponsibleUnitCR());
        } else {
          sql += " )";
        }
        sql += " ) ";
      }
    } else {
//      sqlDB = getConfigUCTTForCreateWo(Constants.MR_MANAGEMENT_SQL.MR_MANAGEMENT,
//          Constants.MR_MANAGEMENT_SQL.SQL_BDC);
      sqlDB = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "get-List-Mr-Cr-Wo-New-Hard");

//      sqlDB = sqlDB.replaceFirst("[?]", " :startDate ")
//          .replaceFirst("[?]", " :startDate ")
//          .replaceFirst("[?]", " :endDate ")
//          .replaceFirst("[?]", " :endDate ")
//          .replaceFirst("[?]", " :startDate ")
//          .replaceFirst("[?]", " :dateCreateWoFrom ")
//          .replaceFirst("[?]", " :dateCreateWoTo ")
//      ;
      sql += sqlDB;


      params.put("startDate", dto.getStartDateMr());
      params.put("endDate", dto.getEndDateMr() + " 23:59:59");
      params.put("dateCreateWoFrom", dto.getDateCreateWoFrom());
      params.put("dateCreateWoTo", dto.getDateCreateWoTo() + " 23:59:59");

//      //MR
//      parameter.add(dto.getStartDateMr());
//      parameter.add(dto.getStartDateMr());
//      parameter.add(dto.getEndDateMr() + " 23:59:59");
//      parameter.add(dto.getEndDateMr() + " 23:59:59");
//
//      //MR_HIS
//      parameter.add(dto.getStartDateMr());
//
//      //WO
//      parameter.add(dto.getDateCreateWoFrom());
//      parameter.add(dto.getDateCreateWoTo() + " 23:59:59");

      if (!StringUtils.isStringNullOrEmpty(dto.getCdGroupWo())) {
        sql += " and a.cdId = :cdGroupWO ";
        params.put("cdGroupWO", dto.getCdGroupWo());
      }
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getCountry())) {
      sql += " and a.country = :country ";
      params.put("country", dto.getCountry());
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getRegion())) {
      sql += " and a.region = :region ";
      params.put("region", dto.getRegion());
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getSubcategory())) {
      sql += " and a.subcategory = :subCategory ";
      params.put("subCategory", dto.getSubcategory());
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getMrCode())) {
      sql += " and LOWER(a.mrCode) like :mrCode escape '\\' ";
      params.put("mrCode", StringUtils.convertLowerParamContains(dto.getMrCode().toLowerCase()));
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getSearchAll())) {
      sql += " and LOWER(a.mrCode) like :searchAll escape '\\' ";
      params.put("searchAll", StringUtils.convertLowerParamContains(dto.getSearchAll().toLowerCase()));
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getMrTitle())) {
      sql += " and LOWER(a.mrTitle) like :title escape '\\' ";
      params.put("title", StringUtils.convertLowerParamContains(dto.getMrTitle().toLowerCase()));
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getState())) {
      sql += " and a.state = :state ";
      params.put("state", dto.getState());
    }
    //duongnt start 27/03/2019
    if ("S".equals(dto.getMrTypeName())) {
      if ("0".equals(dto.getStatus_CR_WO())) {
        sql += " AND crId is null";
      }
      if ("1".equals(dto.getStatus_CR_WO())) {
        sql += " AND crId is not null";
      }

    } else {
      if ("0".equals(dto.getStatus_CR_WO())) {
        sql += " AND woId is null";
      }
      if ("1".equals(dto.getStatus_CR_WO())) {
        sql += " AND woId is not null";
      }
    }
    //duongnt end 27/03/2019
    sql += " order by a.mrCode desc";
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(params);
    return baseDto;
  }

  public String getConfigUCTTForCreateWo(String configGroup, String configCode) {
    List<ConfigPropertyDTO> lst = new ArrayList<>();
    try {
      BaseDto baseDto = sqlGetConfigUCTTForCreateWo(configGroup, configCode);
      lst = getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
          BeanPropertyRowMapper.newInstance(ConfigPropertyDTO.class));
      return lst.get(0).getValue();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public BaseDto sqlGetConfigUCTTForCreateWo(
      String configGroup, String configCode) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "get-Config-UCTT-For-Create-Wo");
    parameters.put("configGroup", configGroup);
    parameters.put("configCode", configCode);
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public ResultInSideDto insertMr(MrInsideDTO mrDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    MrEntity entity = getEntityManager().merge(mrDTO.toEntity());
    resultInSideDTO.setId(entity.getMrId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto updateMr(MrInsideDTO mrDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrEntity entity = mrDTO.toEntity();
    getEntityManager().merge(entity);
    return resultInSideDto;
  }

  @Override
  public List<WorkLogInsiteDTO> getListWorklogSearch(WorkLogInsiteDTO workLogInsiteDTO) {
    BaseDto baseDto = sqlGetListWorklogSearch(workLogInsiteDTO);
    List<WorkLogInsiteDTO> lst = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(WorkLogInsiteDTO.class));
    return lst;
  }

  @Override
  public MrInsideDTO findMrById(Long mrId) {
    MrInsideDTO mrInsideDTO = new MrInsideDTO();
    try {
      if (!StringUtils.isStringNullOrEmpty(mrId)) {
        mrInsideDTO = getEntityManager().find(MrEntity.class, mrId).toDTO();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return mrInsideDTO;
  }

  private BaseDto sqlGetListWorklogSearch(WorkLogInsiteDTO workLogInsiteDTO) {
    String locale = I18n.getLocale();
    String sql;
    Map<String, Object> params = new HashMap<>();
    if (locale != null && locale.toLowerCase().contains("en")) {
      sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "get-list-worklog-search-en");
    } else {
      sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "get-list-worklog-search");
    }
    //tim kiem theo ma don vi
    if (workLogInsiteDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(workLogInsiteDTO.getWlgObjectId())) {
        sql += " and a.wlg_object_id = :wlgObjectId ";
        params.put("wlgObjectId", workLogInsiteDTO.getWlgObjectId());
      }
      if (!StringUtils.isStringNullOrEmpty(workLogInsiteDTO.getWlgObjectType())) {
        sql += " and a.wlg_object_type = :wlgObjectType ";
        params.put("wlgObjectType", workLogInsiteDTO.getWlgObjectType());
      }
    }
    sql += " order by a.created_date desc ";
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(params);
    return baseDto;
  }
}
