package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.ConfigPropertyDTO;
import com.viettel.gnoc.maintenance.dto.MrUngCuuTTDTO;
import com.viettel.gnoc.maintenance.dto.MrUngCuuTTFilesDTO;
import com.viettel.gnoc.maintenance.model.MrUngCuuTTEntity;
import com.viettel.gnoc.maintenance.model.MrUngCuuTTFilesEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrUngCuuTTRepositoryImpl extends BaseRepository implements MrUngCuuTTRepository {

  @Override
  public Datatable getListMrUctt(MrUngCuuTTDTO mrUngCuuTTDTO) {
    BaseDto baseDto = sqlSearch(mrUngCuuTTDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrUngCuuTTDTO.getPage(),
        mrUngCuuTTDTO.getPageSize(), MrUngCuuTTDTO.class,
        mrUngCuuTTDTO.getSortName(), mrUngCuuTTDTO.getSortType());
  }


  @Override
  public ResultInSideDto insertMrUcttDTO(MrUngCuuTTDTO mrUngCuuTTDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrUngCuuTTEntity mrUngCuuTTEntity = getEntityManager().merge(mrUngCuuTTDTO.toEntity());
    resultInSideDto.setId(mrUngCuuTTEntity.getUcttId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertMrUcttFilesDTO(MrUngCuuTTFilesDTO mrUngCuuTTFilesDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrUngCuuTTFilesEntity entity = getEntityManager().merge(mrUngCuuTTFilesDTO.toEntity());
    resultInSideDto.setId(entity.getFileId());
    return resultInSideDto;
  }

  public String getConfigUCTTForCreateWo(String configGroup, String configCode) {
    try {
      String sql = "SELECT CONFIG_VALUE value FROM OPEN_PM.MR_CONFIG where CONFIG_GROUP = :configGroup AND CONFIG_CODE = :configCode";
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("configGroup", configGroup);
      parameters.put("configCode", configCode);
      List<ConfigPropertyDTO> list = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(ConfigPropertyDTO.class));
      return list.get(0).getValue();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public List<MrUngCuuTTDTO> getDataExport(MrUngCuuTTDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrUngCuuTTDTO.class));
  }

  public BaseDto sqlSearch(MrUngCuuTTDTO mrUngCuuTTDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MR_UCTT, "get-list-mr-uctt");
    Map<String, Object> parameters = new HashMap<>();
    if (mrUngCuuTTDTO != null) {
      if (StringUtils.isNotNullOrEmpty(mrUngCuuTTDTO.getSearchAll())) {
        sql += " AND ( lower(uc.WO_CODE) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(mrUngCuuTTDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrUngCuuTTDTO.getCdId())) {
        String cdIdList = "";
        String[] arrCdId = mrUngCuuTTDTO.getCdId().split(",");
        for (int i = 0; i < arrCdId.length; i++) {
          if (i != arrCdId.length - 1) {
            cdIdList += arrCdId[i] + ",";
          } else {
            cdIdList += arrCdId[i];
          }
        }
        sql += " AND uc.CD_ID IN (" + cdIdList + ")";
      }
      if (!StringUtils.isStringNullOrEmpty(mrUngCuuTTDTO.getWoCode())) {
        sql += " AND (lower(uc.WO_CODE) LIKE :woCode ESCAPE '\\' )";
        parameters
            .put("woCode",
                StringUtils.convertLowerParamContains(mrUngCuuTTDTO.getWoCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrUngCuuTTDTO.getStartDateFrom()) && !StringUtils
          .isStringNullOrEmpty(mrUngCuuTTDTO.getStartDateTo())) {
        sql += " AND ("
            + "START_DATE >=  :startDateFrom "
            + " AND "
            + "START_DATE <=  :startDateTo "
            + ")";
        parameters.put("startDateFrom",
            DateTimeUtils.convertStringToDate(mrUngCuuTTDTO.getStartDateFrom()));
        parameters
            .put("startDateTo", DateTimeUtils.convertStringToDate(mrUngCuuTTDTO.getStartDateTo()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrUngCuuTTDTO.getEndDateFrom()) && !StringUtils
          .isStringNullOrEmpty(mrUngCuuTTDTO.getEndDateTo())) {
        sql += " AND ("
            + "END_DATE >=  :endDateFrom "
            + " AND "
            + "END_DATE <=  :endDateTo "
            + ")";
        parameters
            .put("endDateFrom", DateTimeUtils.convertStringToDate(mrUngCuuTTDTO.getEndDateFrom()));
        parameters
            .put("endDateTo", DateTimeUtils.convertStringToDate(mrUngCuuTTDTO.getEndDateTo()));
      }
    }
    sql += " ORDER BY CREATED_DATE DESC ";
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
