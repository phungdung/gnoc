package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.viettel.gnoc.maintenance.model.MrNodesEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author tripm
 * @version 2.0
 * @since 18/06/2020 14:05:00
 */
@Repository
@Slf4j
public class MrNodesRepositoryImpl extends BaseRepository implements MrNodesRepository {

  @Override
  public List<MrNodesDTO> getWoCrNodeList(String woId, String crId) {
    List<MrNodesDTO> lstMrNodesDTO = new ArrayList<>();
    Map<String, Object> params = new HashMap<>();
    String sql;
    try {
      sql = "SELECT T1.MR_NODE_ID mrNodeId ,\n"
          + "  T1.SCHEDULE_TEL_ID scheduleTelId ,\n"
          + "  T1.MR_ID mrId ,\n"
          + "  T1.CR_ID crId ,\n"
          + "  T1.WO_ID woId ,\n"
          + "  T1.NODE_IP nodeIp ,\n"
          + "  T1.NODE_CODE nodeCode ,\n"
          + "  T1.NODE_NAME nodeName ,\n"
          + "  T1.STATUS status ,\n"
          + "  T1.COMMENTS comments ,\n"
          + "  T1.UPDATE_DATE updateDate\n"
          + "FROM MR_NODES T1\n"
          + "WHERE 1      = 1\n";

      if (!StringUtils.isStringNullOrEmpty(woId)) {
        sql += "AND T1.WO_ID = :woId\n";
        params.put("woId", woId);
      } else if (!StringUtils.isStringNullOrEmpty(crId)) {
        sql += "AND T1.CR_ID = :crId";
        params.put("crId", crId);
      }

      lstMrNodesDTO = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrNodesDTO.class)) ;
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return lstMrNodesDTO;
  }

  @Override
  public ResultInSideDto updateWoStatus(String woId, String crId, String nodeCode, String status,
      String comment, boolean completeWo) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    Map<String, Object> params = new HashMap<>();
    String sql;
    try {
      sql = "UPDATE MR_NODES\n"
          + "SET STATUS    = :status,\n"
          + "  UPDATE_DATE = sysdate,\n"
          + "  COMMENTS    = :comments\n";

      params.put("status", status);
      params.put("comments", comment);

      sql += "WHERE 1       = 1\n";

      if (!StringUtils.isStringNullOrEmpty(woId)) {
        sql += "AND WO_ID     = :woId\n";
        params.put("woId", woId);
      }

      if (!StringUtils.isStringNullOrEmpty(crId)) {
        sql += "AND CR_ID     = :crId\n";
        params.put("crId", crId);
      }

      if (!StringUtils.isStringNullOrEmpty(nodeCode)) {
        sql += "AND NODE_CODE = :nodeCode\n";
        params.put("nodeCode", nodeCode);
      }

      if (completeWo) {
        sql += "AND status    = 0";
      }

      int checkUpdate = getNamedParameterJdbcTemplateNormal().update(sql, params);

      if (checkUpdate != 0) {
        resultInSideDto.setId(1L);
        resultInSideDto.setKey(RESULT.SUCCESS);
        resultInSideDto.setMessage(RESULT.SUCCESS);
      } else {
        resultInSideDto.setId(0L);
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage(RESULT.FAIL);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      resultInSideDto.setId(0L);
      resultInSideDto.setKey(RESULT.FAIL);
      resultInSideDto.setMessage(RESULT.FAIL);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long Id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (!StringUtils.isStringNullOrEmpty(Id)) {
      MrNodesEntity entity = getEntityManager()
          .find(MrNodesEntity.class, Id);
      if (entity == null) {
        resultInSideDto.setKey(RESULT.FAIL);
      }
      getEntityManager().remove(entity);
    }
    return resultInSideDto;
  }
}
