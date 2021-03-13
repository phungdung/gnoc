package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCheckListDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisFileDTO;
import com.viettel.gnoc.maintenance.model.MrScheduleBtsHisFileEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrDeviceBtsRepositoryImpl extends BaseRepository implements MrDeviceBtsRepository {

  @Autowired
  UserRepository userRepository;

  @Autowired
  private GnocFileRepository gnocFileRepository;

  @Override
  public ResultInSideDto insertMrScheduleBtsHisFile(
      MrScheduleBtsHisFileDTO mrScheduleBtsHisFileDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    MrScheduleBtsHisFileEntity entity = getEntityManager()
        .merge(mrScheduleBtsHisFileDTO.toEntity());
    resultInSideDto.setId(entity.getIdFile());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public List<MrScheduleBtsHisFileDTO> getListFileByCheckListWo(String chedklistId, String woId) {
    String sql;
    try {
      Map<String, Object> params = new HashMap<>();
      sql = "SELECT ID_FILE idFile,\n"
          + "  WO_ID woId,\n"
          + "  T3.FILE_NAME fileName,\n"
          + "  T3.PATH filePath,\n"
          + "  USER_UPDATE userUpdate,\n"
          + "  CHECK_LIST_ID checklistId,\n"
          + "  TO_CHAR(CREATED_DATE,'dd/MM/yyyy HH24:mi:ss') createdDate\n"
          + "FROM MR_SCHEDULE_BTS_HIS_FILE T1\n"
          + "LEFT JOIN (SELECT * FROM common_gnoc.gnoc_file X WHERE X.BUSINESS_CODE = 'MR_SCHEDULE_BTS_HIS_FILE') T3\n"
          + "ON T1.ID_FILE = T3.MAPPING_ID\n"
          + "WHERE 1              =1";
      if (!StringUtils.isStringNullOrEmpty(chedklistId)) {
        sql += " AND T1.CHECK_LIST_ID =:checklistId ";
        params.put("checklistId", chedklistId);
      }
      if (!StringUtils.isStringNullOrEmpty(woId)) {
        sql += " AND T1.WO_ID =:woId ";
        params.put("woId", woId);
      }
      sql += " ORDER BY idFile ";
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrScheduleBtsHisFileDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return null;
  }

  @Override
  public String updateStatusTask(List<MrScheduleBtsHisDetailDTO> mrScheduleBtsHisDetailDTO) {
    for (MrScheduleBtsHisDetailDTO dto : mrScheduleBtsHisDetailDTO) {
      if (StringUtils.isStringNullOrEmpty(dto.getTaskStatus())) {
        return "Not allow parameters empty";
      }
      try {
        Map<String, Object> parameters = new HashMap<>();
        String sqlQuery = " UPDATE MR_SCHEDULE_BTS_HIS_DETAIL ";
        sqlQuery += " SET TASK_STATUS =:taskStatus ";
        parameters.put("taskStatus", dto.getTaskStatus());

        sqlQuery += " WHERE WO_CODE =:woCode ";
        parameters.put("woCode", dto.getWoCode());

        sqlQuery += " AND CHECKLIST_ID =:checkListId ";
        parameters.put("checkListId", dto.getCheckListId());

        sqlQuery += " AND SERIAL =:serial ";
        parameters.put("serial", dto.getSerial());

        getNamedParameterJdbcTemplateNormal().update(sqlQuery, parameters);

      } catch (Exception ex) {
        log.error(ex.getMessage());
      }
    }
    return RESULT.SUCCESS;
  }

  @Override
  public List<GnocFileDto> getListGnocFileByCheckListIdAndWoId(
      MrScheduleBtsHisFileDTO mrScheduleBtsHisFileDTO) {
    String sqlQuery;
    Map<String, Object> parameters = new HashMap<>();
    sqlQuery = " SELECT "
        + " T1.CHECK_LIST_ID checklistID, "
        + " T1.WO_ID woID, "
        + " T1.ID_FILE idFile, "
        + " gf.FILE_NAME fileName, "
        + " gf.path filePath "
        + " from common_gnoc.gnoc_file gf "
        + " LEFT JOIN MR_SCHEDULE_BTS_HIS_FILE T1 "
        + " ON (T1.ID_FILE = gf.MAPPING_ID) "
        + " WHERE 1 = 1 "
        + " AND gf.BUSINESS_CODE = 'MR_SCHEDULE_BTS_HIS_FILE' ";
    if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisFileDTO.getChecklistId())) {
      sqlQuery += " AND T1.CHECK_LIST_ID =:checklistId ";
      parameters.put("checklistId", mrScheduleBtsHisFileDTO.getChecklistId());
    }
    if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisFileDTO.getWoId())) {
      sqlQuery += " AND T1.WO_ID =:woId ";
      parameters.put("woId", mrScheduleBtsHisFileDTO.getWoId());
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(GnocFileDto.class));
  }

  @Override
  public String updateStatusAfterMaintenance(String woCode, Date lastMaintenanceTime,
      String status) {
    try {
      if (isHaveChecklitsNull(woCode)) {
        return I18n.getLanguage("MrDeviceBts.isHaveChecklitsNull");
      } else {
//        Map<String, Object> parameters = new HashMap<>();
//        //update MR_SCHEDULE_BTS_HIS
//        String sql = " UPDATE OPEN_PM.MR_SCHEDULE_BTS_HIS SET STATUS =:status, COMPLETE_DATE =:lastMaintenanceTime WHERE WO_CODE =:woCode ";
//        parameters.put("status", status);
//        parameters.put("lastMaintenanceTime", lastMaintenanceTime);
//        parameters.put("woCode", woCode);
//        getNamedParameterJdbcTemplate().update(sql, parameters);
//        //update MR_DEVICE_BTS
//        sql = " UPDATE OPEN_PM.MR_DEVICE_BTS SET "
//            + " MAINTENANCE_TIME =:lastMaintenanceTime "
//            + " WHERE DEVICE_ID = (SELECT DEVICE_ID FROM OPEN_PM.MR_SCHEDULE_BTS_HIS WHERE WO_CODE =:woCode) ";
//        getNamedParameterJdbcTemplate().update(sql, parameters);
//        //update MR_SCHEDULE_BTS
//        sql = " UPDATE OPEN_PM.MR_SCHEDULE_BTS SET DELETE_FLAG = 1 WHERE WO_CODE =:woCode ";
//
//        getNamedParameterJdbcTemplate().update(sql, parameters);
        return RESULT.SUCCESS;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  private boolean isHaveChecklitsNull(String woCode) {
    try {
      Map<String, Object> parameters = new HashMap<>();
      String sql = " SELECT CHECKLIST_ID checklistId FROM MR_SCHEDULE_BTS_HIS_DETAIL "
          + " WHERE (TASK_STATUS IS NULL OR TASK_STATUS = 'NOK') ";

      if (!StringUtils.isStringNullOrEmpty(woCode)) {
        sql += " AND WO_CODE =:woCode";
        parameters.put("woCode", woCode);
      }
      List<MrCheckListDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrCheckListDTO.class));
      if (lst != null && lst.size() > 0) {
        return true;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return false;
  }


  @Override
  public String updateAIByWoCode(String woCode, String checkListId, Long valueAI, Long photoRate) {
    try {
      Map<String, Object> params = new HashMap<>();
      String sqlQuery = " UPDATE MR_SCHEDULE_BTS_HIS_DETAIL ";
      boolean hasPrefix = false;
      if (valueAI != null) {
        sqlQuery += " SET VALUE_AI = :valueAI ";
        hasPrefix = true;
        params.put("valueAI", valueAI);
      }
      if (photoRate != null) {
        if (hasPrefix) {
          sqlQuery += " ,PHOTO_RATE = :photoRate ";
        } else {
          sqlQuery += " SET PHOTO_RATE = :photoRate ";
        }
        params.put("photoRate", photoRate);
      }

      sqlQuery += " WHERE WO_CODE = :woCode AND CHECKLIST_ID = :checklistId ";

      params.put("woCode", woCode);
      params.put("checklistId", checkListId);
      getNamedParameterJdbcTemplateNormal().update(sqlQuery, params);
      return RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.ERROR;
    }
  }

  @Override
  public List<MrDeviceBtsDTO> getMrBTSDeviceInfor(String woCode) {
    List<MrDeviceBtsDTO> lstResult = new ArrayList<>();
    try {
      Map<String, Object> parameters = new HashMap<>();
      String sql = " SELECT DISTINCT wo_code woCode ,\n"
          + "  device_type deviceType ,\n"
          + "  serial serial \n"
          + "FROM OPEN_PM.mr_schedule_bts_his_detail \n"
          + "WHERE 1=1 ";

      if (!StringUtils.isStringNullOrEmpty(woCode)) {
        sql += " AND wo_code =:woCode";
        parameters.put("woCode", woCode);
      }
      lstResult = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrDeviceBtsDTO.class));
      if (lstResult != null && lstResult.size() > 0) {
        return lstResult;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstResult;
  }
}
