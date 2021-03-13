package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.ShiftHandoverDTO;
import com.viettel.gnoc.commons.model.GnocFileEntity;
import com.viettel.gnoc.commons.model.ShiftHandoverEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ShiftCrDTO;
import com.viettel.gnoc.cr.dto.ShiftHandoverFileDTO;
import com.viettel.gnoc.cr.dto.ShiftItDTO;
import com.viettel.gnoc.cr.dto.ShiftItSeriousDTO;
import com.viettel.gnoc.cr.dto.ShiftStaftDTO;
import com.viettel.gnoc.cr.dto.ShiftWorkDTO;
import com.viettel.gnoc.cr.dto.ShiftWorkOtherDTO;
import com.viettel.gnoc.cr.model.ShiftHandoverFileEntity;
import com.viettel.gnoc.incident.dto.CommonFileDTO;
import com.viettel.gnoc.incident.model.CommonFileEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import viettel.passport.client.UserToken;

@Repository
@Slf4j
public class ShiftHandoverRepositoryImpl extends BaseRepository implements ShiftHandoverRepository {

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public ShiftHandoverDTO findShiftHandoverById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(ShiftHandoverEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public List<ShiftHandoverDTO> getListShiftHandoverExport(ShiftHandoverDTO shiftHandoverDTO) {
    BaseDto baseDto = sqlSearchShiftHandover(shiftHandoverDTO);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(ShiftHandoverDTO.class));
  }

  @Override
  public Datatable getListShiftHandover(ShiftHandoverDTO shiftHandoverDTO) {
    BaseDto baseDto = sqlSearchShiftHandover(shiftHandoverDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        shiftHandoverDTO.getPage(),
        shiftHandoverDTO.getPageSize(), ShiftHandoverDTO.class,
        shiftHandoverDTO.getSortName(),
        shiftHandoverDTO.getSortType());
  }

  public BaseDto sqlSearchShiftHandover(ShiftHandoverDTO shiftHandoverDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SHIFT_HANDOVER, "getListShiftHandover");
    Map<String, Object> parameters = new HashMap<>();
    String locale = I18n.getLocale();
    parameters.put("p_leeLocale", locale);
    parameters.put("waitingShift", I18n.getLanguage("shiftHandover.waitingShift"));
    parameters.put("receivedShift", I18n.getLanguage("shiftHandover.receivedShift"));
    parameters.put("unitId", userToken.getDeptId());
    if (shiftHandoverDTO.getUserId() != null) {
      sql += "  AND t1.USER_ID =:userId";
      parameters.put("userId", shiftHandoverDTO.getUserId());
    }

    if (shiftHandoverDTO.getUnitId() != null) {
      sql += "  AND t1.UNIT_ID =:unitId";
      parameters.put("unitId", shiftHandoverDTO.getUnitId());
    }

    if (shiftHandoverDTO.getShiftId() != null) {
      sql += "  AND t1.SHIFT_ID =:shiftId";
      parameters.put("shiftId", shiftHandoverDTO.getShiftId());
    }

    if (shiftHandoverDTO.getStatus() != null) {
      sql += " AND t1.STATUS =:status";
      parameters.put("status", shiftHandoverDTO.getStatus());
    }

    if (!StringUtils.isStringNullOrEmpty(shiftHandoverDTO.getLastUpdateTimeFrom()) && !StringUtils
        .isStringNullOrEmpty(shiftHandoverDTO.getLastUpdateTimeTo())) {
      sql +=
          " AND (TO_DATE(TO_CHAR(t1.LAST_UPDATE_TIME, 'dd/mm/yyyy'), 'dd/mm/yyyy') BETWEEN TO_DATE(:lastUpdateTimeFrom, 'dd/mm/yyyy') AND TO_DATE(:lastUpdateTimeTo, 'dd/mm/yyyy'))";
      parameters.put("lastUpdateTimeFrom",
          DateUtil.date2ddMMyyyyString(shiftHandoverDTO.getLastUpdateTimeFrom()));
      parameters.put("lastUpdateTimeTo",
          DateUtil.date2ddMMyyyyString(shiftHandoverDTO.getLastUpdateTimeTo()));

    }

    if (!StringUtils.isStringNullOrEmpty(shiftHandoverDTO.getCreateTimeFrom()) && !StringUtils
        .isStringNullOrEmpty(shiftHandoverDTO.getCreateTimeTo())) {
      sql +=
          " AND (TO_DATE(TO_CHAR(t1.CREATED_TIME, 'dd/mm/yyyy'), 'dd/mm/yyyy') BETWEEN TO_DATE(:createTimeFrom, 'dd/mm/yyyy') AND TO_DATE(:createTimeTo, 'dd/mm/yyyy'))";
      parameters.put("createTimeFrom",
          DateUtil.date2ddMMyyyyString(shiftHandoverDTO.getCreateTimeFrom()));
      parameters
          .put("createTimeTo", DateUtil.date2ddMMyyyyString(shiftHandoverDTO.getCreateTimeTo()));
    }

    if (StringUtils.isNotNullOrEmpty(shiftHandoverDTO.getSearchAll())) {
      sql += " AND (LOWER(t1.USER_NAME) LIKE :searchAll ESCAPE '\\')";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(shiftHandoverDTO.getSearchAll()));
    }

    if (!StringUtils.isStringNullOrEmpty(shiftHandoverDTO.getCreatedTime())) {
      sql += " AND to_date(to_char(t1.CREATED_TIME, 'dd/MM/yyyy'), 'dd/MM/yyyy') " +
          "= to_date(to_char(:createdTime,'dd/MM/yyyy'),'dd/MM/yyyy') ";
      parameters.put("createdTime", shiftHandoverDTO.getCreatedTime());
    }

    sql += " ORDER BY t1.LAST_UPDATE_TIME DESC";
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public boolean checkDuplicateRecord(ShiftHandoverDTO shiftHandoverDTO) {
    String sql = "SELECT * FROM COMMON_GNOC.SHIFT_HANDOVER t1 WHERE t1.UNIT_ID = :unitId AND t1.SHIFT_ID = :shiftId AND TO_CHAR(t1.CREATED_TIME, 'dd/mm/yyyy') = :createdTime";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("unitId", shiftHandoverDTO.getUnitId());
    parameters.put("shiftId", shiftHandoverDTO.getShiftId());
    parameters.put("createdTime", DateUtil.date2ddMMyyyyString(shiftHandoverDTO.getCreatedTime()));
    List<ShiftHandoverDTO> shiftHandoverDTOS = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(ShiftHandoverDTO.class));
    if (!shiftHandoverDTOS.isEmpty() && shiftHandoverDTOS.size() > 0) {
      return false;
    }
    return true;
  }

  @Override
  public List<ShiftHandoverDTO> getListShiftID() {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SHIFT_HANDOVER, "getListShiftID");
    Map<String, Object> parameters = new HashMap<>();
    List<ShiftHandoverDTO> shiftHandoverDTOS = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper
            .newInstance(ShiftHandoverDTO.class));
    List<ShiftHandoverDTO> shiftHandoverDTOList = new ArrayList<>();
    if (shiftHandoverDTOS != null) {
      ArrayList<ShiftHandoverDTO> returnCodeCatalogDTOS = (ArrayList<ShiftHandoverDTO>) shiftHandoverDTOS;
      for (ShiftHandoverDTO dto1 : returnCodeCatalogDTOS) {
        Long returnCategory = dto1.getShiftId();
        ShiftHandoverDTO dto = new ShiftHandoverDTO();
        dto.setShiftId(returnCategory);
        if (dto.getShiftId() == 1341) {
          dto.setShiftName(I18n.getLanguage("shiftHandover.shiftName.ca1"));
        } else if (dto.getShiftId() == 1342) {
          dto.setShiftName(I18n.getLanguage("shiftHandover.shiftName.ca2"));
        } else if (dto.getShiftId() == 1343) {
          dto.setShiftName(I18n.getLanguage("shiftHandover.shiftName.ca3"));
        }
        shiftHandoverDTOList.add(dto);
      }
    }
    return shiftHandoverDTOList;
  }

  @Override
  public ResultInSideDto insertCfgShiftHandover(ShiftHandoverDTO shiftHandoverDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    ShiftHandoverEntity shiftHandoverEntity = getEntityManager().merge(shiftHandoverDTO.toEntity());
    resultInSideDTO.setId(shiftHandoverEntity.getId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto updateCfgShiftHandover(ShiftHandoverDTO dto) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(dto.toEntity());
    return resultInSideDTO;
  }

  @Override
  public ShiftHandoverDTO findListShiftHandOverById(Long id) {
    Map<String, Object> parameters = new HashMap<>();
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    List<ShiftStaftDTO> shiftStaftDTOList = new ArrayList<>();
    List<ShiftWorkDTO> shiftWorkDTOList = new ArrayList<>();
    List<ShiftItSeriousDTO> shiftItSeriousDTOList = new ArrayList<>();
    List<ShiftItDTO> shiftItDTOList = new ArrayList<>();
    List<ShiftCrDTO> shiftCrDTOList = new ArrayList<>();
    List<ShiftWorkOtherDTO> shiftWorkOtherDTOList = new ArrayList<>();
    if (id != null && id > 0) {
      List<GnocFileEntity> gnocFileEntities = findByMultilParam(GnocFileEntity.class,
          "businessCode", GNOC_FILE_BUSSINESS.SHIFT_HANDOVER,
          "businessId", id);
      if (!gnocFileEntities.isEmpty()) {
        for (GnocFileEntity gnocFileEntity : gnocFileEntities) {
          gnocFileDtos.add(gnocFileEntity.toDTO());
        }
      }

      String sql2 = "SELECT * FROM COMMON_GNOC.SHIFT_STAFT t1 WHERE t1.SHIFT_HANDOVER_ID = :shiftHandOverId";
      parameters.put("shiftHandOverId", id);
      shiftStaftDTOList = getNamedParameterJdbcTemplate()
          .query(sql2, parameters, BeanPropertyRowMapper.newInstance(ShiftStaftDTO.class));

      String sql3 = "SELECT * FROM COMMON_GNOC.SHIFT_WORK t1 WHERE t1.SHIFT_HANDOVER_ID = :shiftHandOverId";
      parameters.put("shiftHandOverId", id);
      shiftWorkDTOList = getNamedParameterJdbcTemplate()
          .query(sql3, parameters, BeanPropertyRowMapper.newInstance(ShiftWorkDTO.class));

      String sql4 = "SELECT * FROM COMMON_GNOC.SHIFT_IT_SERIOUS t1 WHERE t1.SHIFT_HANDOVER_ID = :shiftHandOverId";
      parameters.put("shiftHandOverId", id);
      shiftItSeriousDTOList = getNamedParameterJdbcTemplate()
          .query(sql4, parameters, BeanPropertyRowMapper.newInstance(ShiftItSeriousDTO.class));

      String sql5 = "SELECT * FROM COMMON_GNOC.SHIFT_IT t1 WHERE t1.SHIFT_HANDOVER_ID = :shiftHandOverId order by t1.KPI";
      parameters.put("shiftHandOverId", id);
      shiftItDTOList = getNamedParameterJdbcTemplate()
          .query(sql5, parameters, BeanPropertyRowMapper.newInstance(ShiftItDTO.class));

      String sql6 = "SELECT * FROM COMMON_GNOC.SHIFT_CR t1 WHERE t1.SHIFT_HANDOVER_ID = :shiftHandOverId";
      parameters.put("shiftHandOverId", id);
      shiftCrDTOList = getNamedParameterJdbcTemplate()
          .query(sql6, parameters, BeanPropertyRowMapper.newInstance(ShiftCrDTO.class));

      String sql7 = "SELECT * FROM COMMON_GNOC.SHIFT_WORK_OTHER t1 WHERE t1.SHIFT_HANDOVER_ID = :shiftHandOverId";
      parameters.put("shiftHandOverId", id);
      shiftWorkOtherDTOList = getNamedParameterJdbcTemplate()
          .query(sql7, parameters, BeanPropertyRowMapper.newInstance(ShiftWorkOtherDTO.class));
    }

    ShiftHandoverDTO shiftHandoverDTO = new ShiftHandoverDTO();
    if (id != null && id > 0) {
      shiftHandoverDTO = getEntityManager().find(ShiftHandoverEntity.class, id).toDTO();
    }
    shiftHandoverDTO.setGnocFileDtos(gnocFileDtos);
    shiftHandoverDTO.setShiftStaftDTOList(shiftStaftDTOList);
    shiftHandoverDTO.setShiftWorkDTOList(shiftWorkDTOList);
    shiftHandoverDTO.setShiftItSeriousDTOList(shiftItSeriousDTOList);
    shiftHandoverDTO.setShiftItDTOList(shiftItDTOList);
    shiftHandoverDTO.setShiftCrDTOList(shiftCrDTOList);
    shiftHandoverDTO.setShiftWorkOtherDTOList(shiftWorkOtherDTOList);

    return shiftHandoverDTO;
  }

  @Override
  public List<ShiftWorkDTO> getListShiftWorkByShiftID(ShiftWorkDTO shiftWorkDTO) {
    String sql3 = "SELECT t1.WORK_NAME workName, t1.START_TIME startTime, t1.DEADLINE deadLine, t1.OWNER owner, t1.HANDLE handle, t1.IMPORTANT_LEVEL importantLevel, t1.RESULT result, t1.NEXT_WORK nextWork, t1.PROCESS process, t1.CONTACT contact, t1.OPINION opinion FROM COMMON_GNOC.SHIFT_WORK t1 WHERE t1.SHIFT_HANDOVER_ID = :shiftHandoverId";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("shiftHandoverId", shiftWorkDTO.getShiftHandoverId());
    List<ShiftWorkDTO> shiftWorkDTOList = getNamedParameterJdbcTemplate()
        .query(sql3, parameters, BeanPropertyRowMapper.newInstance(ShiftWorkDTO.class));
    return shiftWorkDTOList;
  }

  @Override
  public List<ShiftCrDTO> getListShiftCrkByShiftID(ShiftCrDTO shiftCrDTO) {
    String sql = "SELECT t1.CR_NUMBER crNumber, t1.CR_NAME crName, t1.USER_NAME userName, t1.RESULT result, t1.NOTE note FROM COMMON_GNOC.SHIFT_CR t1 WHERE t1.SHIFT_HANDOVER_ID = :shiftHandoverId";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("shiftHandoverId", shiftCrDTO.getShiftHandoverId());
    List<ShiftCrDTO> shiftCrDTOS = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(ShiftCrDTO.class));
    return shiftCrDTOS;
  }

  @Override
  public List<String> getSequenseShiftHandover(String seqName, int size) {
    seqName = "SHIFT_HANDOVER_SEQ";
    return getListSequense(seqName, size);
  }

  @Override
  public ResultInSideDto insertOrUpdateCfgShiftHandover(ShiftHandoverDTO shiftHandoverDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    ShiftHandoverEntity shiftHandoverEntity = shiftHandoverDTO.toEntity();
    if (shiftHandoverEntity.getId() != null) {
      getEntityManager().merge(shiftHandoverEntity);
    } else {
      getEntityManager().persist(shiftHandoverEntity);
    }
    resultInSideDTO.setId(shiftHandoverEntity.getId());
    return resultInSideDTO;
  }

  @Override
  public List<ShiftHandoverFileEntity> findShiftHandOverFile(Long shiftHandoverId) {
    return findByMultilParam(ShiftHandoverFileEntity.class,
        "shiftHandoverId", shiftHandoverId);
  }

  @Override
  public ResultInSideDto insertShiftHandOverFile(ShiftHandoverFileDTO shiftHandoverFileDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    ShiftHandoverFileEntity shiftHandoverFileEntity = getEntityManager()
        .merge(shiftHandoverFileDTO.toEntity());
    resultInSideDTO.setId(shiftHandoverFileEntity.getId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteShiftHandOverFile(ShiftHandoverFileDTO shiftHandoverFileDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    deleteByMultilParam(ShiftHandoverFileEntity.class, "fileId", shiftHandoverFileDTO.getFileId(),
        "shiftHandoverId", shiftHandoverFileDTO.getShiftHandoverId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto insertCommonFile(CommonFileDTO commonFileDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    CommonFileEntity commonFileEntity = getEntityManager()
        .merge(commonFileDTO.toEntity());
    resultInSideDTO.setId(commonFileEntity.getFileId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteCommonFile(CommonFileDTO commonFileDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    CommonFileEntity commonFileEntity = getEntityManager()
        .find(CommonFileEntity.class, commonFileDTO.getFileId());
    getEntityManager().remove(commonFileEntity);
    return resultInSideDTO;
  }

  @Override
  public List<ShiftHandoverDTO> getListShiftHandoverNew(ShiftHandoverDTO shiftHandoverDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SHIFT_HANDOVER, "get-List-Shift-Handover-New");
    Map<String, Object> parameters = new HashMap<>();
    if (shiftHandoverDTO.getUnitId() != null) {
      sql += " AND a.UNIT_ID = :unitId";
      parameters.put("unitId", shiftHandoverDTO.getUnitId());
    }
    if (StringUtils.isNotNullOrEmpty(shiftHandoverDTO.getUserName())) {
      sql += " AND a.USER_NAME = :userName";
      parameters.put("userName", shiftHandoverDTO.getUserName());
    }
    if (StringUtils.isNotNullOrEmpty(shiftHandoverDTO.getUnitName())) {
      sql += " AND a.UNIT_NAME = :unitName";
      parameters.put("unitName", shiftHandoverDTO.getUnitName());
    }
    if (shiftHandoverDTO.getCreatedTime() != null) {
      sql += " AND trunc(a.CREATED_TIME) = to_date(:createdTime,'dd/MM/yyyy')";
      parameters.put("createdTime",
          DateTimeUtils.date2ddMMyyyyString(shiftHandoverDTO.getCreatedTime()));
    }
    if (shiftHandoverDTO.getShiftId() != null) {
      sql += " AND a.SHIFT_ID = :shiftId";
      parameters.put("shiftId", shiftHandoverDTO.getShiftId());
    }
    sql += " GROUP BY a.ID,"
        + " a.USER_NAME,"
        + " a.USER_ID,"
        + " a.UNIT_NAME,"
        + " a.UNIT_ID,"
        + " a.SHIFT_ID,"
        + " a.LAST_UPDATE_TIME,"
        + " a.STATUS,"
        + " a.CREATED_TIME";
    sql += " ORDER BY a.ID DESC";
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(ShiftHandoverDTO.class));
  }
}
