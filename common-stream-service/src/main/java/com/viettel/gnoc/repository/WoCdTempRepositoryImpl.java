package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoCdDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupUnitDTO;
import com.viettel.gnoc.wo.dto.WoCdTempInsideDTO;
import com.viettel.gnoc.wo.model.WoCdEntity;
import com.viettel.gnoc.wo.model.WoCdTempEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoCdTempRepositoryImpl extends BaseRepository implements
    WoCdTempRepository {


  @Override
  public Datatable getListWoCdTempDTO(WoCdTempInsideDTO woCdTempInsideDTO) {
    BaseDto baseDto = sqlSearch(woCdTempInsideDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        woCdTempInsideDTO.getPage(), woCdTempInsideDTO.getPageSize(),
        WoCdTempInsideDTO.class,
        woCdTempInsideDTO.getSortName(), woCdTempInsideDTO.getSortType());
  }

  @Override
  public List<WoCdGroupInsideDTO> getListCdGroupByUser(WoCdGroupTypeUserDTO woCdGroupTypeUserDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_TEMP, "getListCdGroupByUser");
    Map<String, Object> parameters = new HashMap<>();
    if (woCdGroupTypeUserDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(woCdGroupTypeUserDTO.getWoTypeId())) {
        sql += " AND g.WO_GROUP_ID in (SELECT WO_GROUP_ID FROM WFM.wo_type_group WHERE wo_type_id = :woTypeId) ";
        parameters.put("woTypeId", woCdGroupTypeUserDTO.getWoTypeId());
      }
      if (woCdGroupTypeUserDTO.getUserId() != null) {
        sql += " AND g.WO_GROUP_ID in (SELECT WO_GROUP_ID FROM WFM.WO_CD WHERE USER_ID = :userId)";
        parameters.put("userId", woCdGroupTypeUserDTO.getUserId());
      }
      if (woCdGroupTypeUserDTO.getGroupTypeId() != null) {
        sql += " AND g.group_type_id = :groupTypeId";
        parameters.put("groupTypeId", woCdGroupTypeUserDTO.getGroupTypeId());
      }
    }
    sql += " ORDER BY g.WO_GROUP_NAME ASC ";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));
  }

  @Override
  public List<WoCdGroupUnitDTO> getListWoCdGroupUnitDTO(WoCdGroupUnitDTO woCdGroupUnitDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_TEMP, "getListWoCdGroupUnitDTO");
    Map<String, Object> parameters = new HashMap<>();
    if (woCdGroupUnitDTO.getCdGroupId() != null) {
      sql += " AND u.CD_GROUP_ID = :cdGroupId";
      parameters.put("cdGroupId", woCdGroupUnitDTO.getCdGroupId());
    }
    if (woCdGroupUnitDTO.getUnitId() != null) {
      sql += " AND u.UNIT_ID = :unitId";
      parameters.put("unitId", woCdGroupUnitDTO.getUnitId());
    }
    if (woCdGroupUnitDTO.getListUnitIdDel() != null && !woCdGroupUnitDTO.getListUnitIdDel()
        .isEmpty()) {
      sql += " AND u.UNIT_ID IN (:listUnitIdDel)";
      parameters.put("listUnitIdDel", woCdGroupUnitDTO.getListUnitIdDel());
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoCdGroupUnitDTO.class));
  }

  @Override
  public ResultInSideDto add(WoCdTempInsideDTO woCdTempInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoCdTempEntity woCdTempEntity = getEntityManager().merge(woCdTempInsideDTO.toEntity());
    resultInSideDto.setId(woCdTempEntity.getWoCdTempId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto edit(WoCdTempInsideDTO woCdTempInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(woCdTempInsideDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public WoCdTempInsideDTO getDetail(Long woCdTempId) {
    WoCdTempEntity woCdTempEntity = getEntityManager().find(WoCdTempEntity.class, woCdTempId);
    WoCdTempInsideDTO woCdTempInsideDTO = woCdTempEntity.toDTO();
    return woCdTempInsideDTO;
  }

  @Override
  public ResultInSideDto delete(Long woCdTempId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoCdTempEntity woCdTempEntity = getEntityManager()
        .find(WoCdTempEntity.class, woCdTempId);
    if (woCdTempEntity != null) {
      getEntityManager().remove(woCdTempEntity);
    }
    return resultInSideDto;
  }

  @Override
  public List<WoCdTempInsideDTO> getListDataExport(WoCdTempInsideDTO woCdTempInsideDTO) {
    BaseDto baseDto = sqlSearch(woCdTempInsideDTO);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(WoCdTempInsideDTO.class));
  }

  @Override
  public String getSeqWoCdTemp(String sequence) {
    return getSeqTableBase(sequence);
  }

  @Override
  public List<WoCdGroupUnitDTO> getListUserByCdGroupIsEnable(Long cdGroupId) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_TEMP, "get-List-User-By-Cd-Group-Is-Enable");
    Map<String, Object> parameters = new HashMap<>();
    if (cdGroupId != null) {
      sql += " AND u.CD_GROUP_ID = :cdGroupId";
      parameters.put("cdGroupId", cdGroupId);
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoCdGroupUnitDTO.class));
  }

  @Override
  public List<WoCdDTO> getListWoCdDTO(WoCdDTO woCdDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    return onSearchEntity(WoCdEntity.class, woCdDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  public BaseDto sqlSearch(WoCdTempInsideDTO woCdTempInsideDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_TEMP, "getListWoCdTemp");
    Map<String, Object> parameters = new HashMap<>();
    if (woCdTempInsideDTO != null) {
      if (StringUtils.isNotNullOrEmpty(woCdTempInsideDTO.getSearchAll())) {
        sqlQuery += " AND (lower(c.wo_group_name) LIKE :searchAll ESCAPE '\\' OR lower(b.username) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(woCdTempInsideDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(woCdTempInsideDTO.getWoGroupId())) {
        sqlQuery += " AND a.wo_group_id = :woGroupId ";
        parameters.put("woGroupId", woCdTempInsideDTO.getWoGroupId());
      }
      if (!StringUtils.isStringNullOrEmpty(woCdTempInsideDTO.getUserId())) {
        sqlQuery += " AND a.user_id = :userId ";
        parameters.put("userId", woCdTempInsideDTO.getUserId());
      }
      if (!StringUtils.isStringNullOrEmpty(woCdTempInsideDTO.getIsCd())) {
        sqlQuery += " AND a.is_cd = :isCd ";
        parameters.put("isCd", woCdTempInsideDTO.getIsCd());
      }
      if (!StringUtils.isStringNullOrEmpty(woCdTempInsideDTO.getStatus())) {
        sqlQuery += " AND a.STATUS= :status ";
        parameters.put("status", woCdTempInsideDTO.getStatus());
      }
    }
    sqlQuery += " ORDER BY a.wo_cd_temp_id DESC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
