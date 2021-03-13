package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistFilesDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import com.viettel.gnoc.maintenance.model.MrNodeChecklistEntity;
import com.viettel.gnoc.maintenance.model.MrNodesEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrNodesRepositoryImpl extends BaseRepository implements
    MrNodesRepository {

  @Override
  public ResultDTO updateWoStatus(String woId, String crId, String nodeCode, String status,
      String comment, boolean completeWo) {
    ResultDTO result = new ResultDTO();
    try {
      StringBuffer sql = new StringBuffer();
      Map<String, Object> params = new HashMap<>();
      sql.append("UPDATE MR_NODES SET STATUS = :status, UPDATE_DATE = sysdate, COMMENTS = :comment "
          + " WHERE 1 = 1  ");
      params.put("status", status);
      params.put("comment", comment);
      if (!StringUtils.isStringNullOrEmpty(woId)) {
        sql.append(" AND WO_ID = :woId");
        params.put("woId", woId);
      } else if (!StringUtils.isStringNullOrEmpty(crId)) {
        sql.append(" AND CR_ID = :crId");
        params.put("crId", crId);
      }
      if (!StringUtils.isStringNullOrEmpty(nodeCode)) {
        sql.append(" AND NODE_CODE = :nodeCode");
        params.put("nodeCode", nodeCode);
      }
      if (completeWo) {
        sql.append(" AND status = 0 ");
      }
      getNamedParameterJdbcTemplate().update(sql.toString(), params);
      result.setId("1");
      result.setKey("SUCCESS");
    } catch (Exception e) {
      result.setId("0");
      result.setMessage("FAIL");
      log.error(e.getMessage(), e);
    }
    return result;
  }

  @Override
  public List<MrNodesDTO> getListNodeNOK(String crId) {
    try {
      StringBuilder sql = new StringBuilder(
          SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_NODE, "get-list-node-nok"));
      StringBuilder sql2 = new StringBuilder(
          SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_NODE, "get-list-node-nok"));
      Map<String, Object> params = new HashMap<>();
      sql.append(" and status = '2' ");
      if (!StringUtils.isStringNullOrEmpty(crId)) {
        sql.append(" AND UPPER(MN.CR_ID) = :crId ");
        sql2.append(" AND UPPER(MN.CR_ID) = :crId ");
        params.put("crId", StringUtils.replaceSpecicalChracterSQL(crId).toUpperCase());
      }
      List<MrNodesDTO> list = getNamedParameterJdbcTemplate().query(sql.toString(), params,
          BeanPropertyRowMapper.newInstance(MrNodesDTO.class));
      if (list != null && list.size() > 0) {
        return list;
      } else {
        list = getNamedParameterJdbcTemplate().query(sql2.toString(), params,
            BeanPropertyRowMapper.newInstance(MrNodesDTO.class));
        return list;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public void insertMrNodeByDTO(List<MrNodesDTO> node) {
    try {
      for (MrNodesDTO item : node) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sb.append("INSERT INTO MR_NODES "
            + "(MR_NODE_ID,SCHEDULE_TEL_ID,MR_ID,CR_ID,WO_ID,"
            + "NODE_IP,NODE_CODE,NODE_NAME,STATUS,COMMENTS,UPDATE_DATE)"
            + "values(MR_NODES_SEQ.NEXTVAL, :scheduleTelId, :mrId, :crId, :woId,"
            + " :nodeIp, :nodeCode, :nodeName,0, :comment,SYSDATE)");
        params.put("scheduleTelId", item.getScheduleTelId() != null ? item.getScheduleTelId() : 0L);
        params.put("mrId", item.getMrId() != null ? item.getMrId() : 0L);
        params.put("crId", item.getCrId() != null ? item.getCrId() : 0L);
        params.put("woId", item.getWoId() != null ? item.getWoId() : 0L);
        params.put("nodeIp", item.getNodeIp() != null ? item.getNodeIp() : "");
        params.put("nodeCode", item.getNodeCode() != null ? item.getNodeCode() : "");
        params.put("nodeName", item.getNodeName() != null ? item.getNodeName() : "");
        params.put("comment", item.getComments() != null ? item.getComments() : "");
        getNamedParameterJdbcTemplate().update(sb.toString(), params);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public ResultInSideDto deleteMrNodeByWoIdAndNodeName(String woId, String nodeName) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    List<MrNodesEntity> lstDetete = findByMultilParam(MrNodesEntity.class, "woId", woId, "nodeName",
        nodeName);
    if (lstDetete != null && !lstDetete.isEmpty()) {
      for (MrNodesEntity entity : lstDetete) {
        getEntityManager().remove(entity);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateMrNodeByWoIdAndNodeName(String woId, String nodeName) {
    ResultInSideDto result = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    if (StringUtils.isNotNullOrEmpty(woId) && StringUtils.isNotNullOrEmpty(nodeName)) {
      try {
        Map<String, Object> params = new HashMap<>();
        String sql = " UPDATE OPEN_PM.MR_NODES SET STATUS = 1, UPDATE_DATE = sysdate, COMMENTS =:comment  WHERE 1 = 1  AND WO_ID = :woId AND NODE_NAME = :nodeName ";
        params.put("comment", I18n.getLanguage("mrNodes.updateByWoIdAndNodeName.comment"));
        params.put("woId", woId);
        params.put("nodeName", nodeName);
        getNamedParameterJdbcTemplate().update(sql, params);
      } catch (Exception e) {
        result.setMessage(RESULT.FAIL);
        result.setKey(RESULT.FAIL);
        log.error(e.getMessage(), e);
      }
    }
    return result;
  }

  @Override
  public List<MrNodesDTO> getWoCrNodeList(String woId, String crId) {
    List<MrNodesDTO> lstMrNodesDTO = new ArrayList<>();
    Map<String, Object> params = new HashMap<>();
    String sql;
    try {
      sql = "SELECT T1.MR_NODE_ID mrNodeId , "
          + "  T1.SCHEDULE_TEL_ID scheduleTelId , "
          + "  T1.MR_ID mrId , "
          + "  T1.CR_ID crId , "
          + "  T1.WO_ID woId , "
          + "  T1.NODE_IP nodeIp , "
          + "  T1.NODE_CODE nodeCode , "
          + "  T1.NODE_NAME nodeName , "
          + "  T1.STATUS status , "
          + "  T1.COMMENTS comments , "
          + "  T1.UPDATE_DATE updateDate "
          + "FROM MR_NODES T1 "
          + "WHERE 1      = 1 ";

      if (!StringUtils.isStringNullOrEmpty(woId)) {
        sql += "AND T1.WO_ID = :woId ";
        params.put("woId", woId);
      } else if (!StringUtils.isStringNullOrEmpty(crId)) {
        sql += "AND T1.CR_ID = :crId";
        params.put("crId", crId);
      }

      lstMrNodesDTO = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrNodesDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return lstMrNodesDTO;
  }

  @Override
  public List<MrNodeChecklistDTO> getListMrNodeChecklistForPopUp(String woId, String mrNodeId) {
    String sql;
    Map<String, Object> params = new HashMap<>();
    List<MrNodeChecklistDTO> lstMrNodeChecklist = new ArrayList<>();
    try {
      sql = " SELECT * "
          + " FROM "
          + "  (SELECT n.mr_node_id mrNodeId , "
          + "    NVL(cl.device_type, cl.DEVICE_TYPE_ALL) deviceTypeAll , "
          + "    cl.CHECKLIST_ID checklistId , "
          + "    cl.CONTENT content , "
          + "    b2.STATUS status , "
          + "    b2.COMMENTS comments , "
          + "    b2.NODE_CHECKLIST_ID nodeChecklistId , "
          + "    n.wo_id, "
          + "    n.MR_NODE_ID , "
          + "    cl.TARGET "
          + "  FROM open_pm.MR_CHECKLIST cl "
          + "  INNER JOIN open_pm.mr_schedule_tel s "
          + "  ON cl.market_code    = s.market_code "
          + "  AND (cl.device_type IS NULL "
          + "  OR cl.device_type    = s.device_type) "
          + "  AND cl.array_code    = s.array_code "
          + "  AND cl.cycle         = s.MR_HARD_CYCLE "
          + "  INNER JOIN MR_NODES n "
          + "  ON n.mr_id      = s.MR_ID "
          + "  AND n.node_code = s.device_code "
          + "  LEFT JOIN MR_NODE_CHECKLIST b2 "
          + "  ON b2.MR_NODE_ID    = n.MR_NODE_ID "
          + "  AND b2.CHECKLIST_ID = cl.CHECKLIST_ID "
          + "  UNION "
          + "  SELECT n.mr_node_id mrNodeId , "
          + "    NVL(cl.device_type, cl.DEVICE_TYPE_ALL) deviceTypeAll , "
          + "    cl.CHECKLIST_ID checklistId , "
          + "    cl.CONTENT content , "
          + "    b2.STATUS status , "
          + "    b2.COMMENTS comments , "
          + "    b2.NODE_CHECKLIST_ID nodeChecklistId , "
          + "    n.wo_id, "
          + "    n.MR_NODE_ID , "
          + "    cl.TARGET "
          + "  FROM open_pm.MR_CHECKLIST cl "
          + "  INNER JOIN open_pm.mr_schedule_cd s "
          + "  ON cl.market_code    = s.market_code "
          + "  AND (cl.device_type IS NULL "
          + "  OR cl.device_type    = s.device_type) "
          + "  AND cl.array_code    = 'Hệ thống nguồn và hệ thống phụ trợ tại Tổng trạm KV' "
          + "  AND cl.cycle         = s.CYCLE "
          + "  INNER JOIN MR_NODES n "
          + "  ON n.mr_id      = s.MR_ID "
          + "  AND n.node_code = s.device_name "
          + "  LEFT JOIN MR_NODE_CHECKLIST b2 "
          + "  ON b2.MR_NODE_ID    = n.MR_NODE_ID "
          + "  AND b2.CHECKLIST_ID = cl.CHECKLIST_ID "
          + "  ) "
          + " WHERE 1        = 1 ";

      if (!StringUtils.isStringNullOrEmpty(woId)) {
        sql += " AND wo_id      = :woId ";
        params.put("woId", woId);
      }

      if (!StringUtils.isStringNullOrEmpty(mrNodeId)) {
        sql += " AND MR_NODE_ID = :mrNodeId ";
        params.put("mrNodeId", mrNodeId);
      }

      lstMrNodeChecklist = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrNodeChecklistDTO.class));

    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return lstMrNodeChecklist;
  }

  @Override
  public ResultInSideDto updateMrNodeChecklistForPopUp(
      List<MrNodeChecklistDTO> lstMrNodeChecklistDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    for (MrNodeChecklistDTO dto : lstMrNodeChecklistDTO) {
      if (StringUtils.isNotNullOrEmpty(dto.getChecklistId()) && StringUtils
          .isNotNullOrEmpty(dto.getNodeChecklistId())) {
        List<MrNodeChecklistEntity> lst = findByMultilParam(MrNodeChecklistEntity.class,
            "nodeChecklistId", Long.valueOf(dto.getNodeChecklistId()), "checklistId",
            Long.valueOf(dto.getChecklistId()));
        if (lst != null && !lst.isEmpty()) {
          MrNodeChecklistEntity mrNodeChecklistEntity = lst.get(0);
          mrNodeChecklistEntity
              .setStatus(StringUtils.isNotNullOrEmpty(dto.getStatus()) ? dto.getStatus() : "NOK");
          mrNodeChecklistEntity.setComments(dto.getComments());
          mrNodeChecklistEntity.setCreatedTime(new Date());
          getEntityManager().merge(mrNodeChecklistEntity);
        } else {
          MrNodeChecklistEntity mrNodeChecklistEntity = dto.toEntity();
          mrNodeChecklistEntity
              .setStatus(StringUtils.isNotNullOrEmpty(dto.getStatus()) ? dto.getStatus() : "NOK");
          mrNodeChecklistEntity.setCreatedTime(new Date());
          getEntityManager().persist(mrNodeChecklistEntity);
        }
      } else {
        MrNodeChecklistEntity mrNodeChecklistEntity = dto.toEntity();
        mrNodeChecklistEntity
            .setStatus(StringUtils.isNotNullOrEmpty(dto.getStatus()) ? dto.getStatus() : "NOK");
        mrNodeChecklistEntity.setCreatedTime(new Date());
        getEntityManager().persist(mrNodeChecklistEntity);
      }
    }
    return resultInSideDto;
  }

  @Override
  public List<MrNodeChecklistFilesDTO> getListFileMrNodeChecklist_VS(String nodeChecklistId) {
    List<MrNodeChecklistFilesDTO> lstNodeChecklistFiles = new ArrayList<>();
    if (StringUtils.isNotNullOrEmpty(nodeChecklistId)) {
      Map<String, Object> params = new HashMap<>();
      String sql;
      try {
        sql = "SELECT cf.FILE_ID fileId , "
            + "  cf.FILE_NAME fileName , "
            + "  gf.PATH filePath , "
            + "  cf.NODE_CHECKLIST_ID nodeChecklistId , "
            + "  cf.CREATED_USER createdUser , "
            + "  cf.CREATED_TIME createdTime "
            + "FROM OPEN_PM.MR_NODE_CHECKLIST_FILES cf "
            + "JOIN COMMON_GNOC.GNOC_FILE gf "
            + "ON cf.FILE_ID           =  gf.MAPPING_ID "
            + "AND cf.NODE_CHECKLIST_ID = gf.BUSINESS_ID "
            + "AND gf.BUSINESS_CODE    = :businessCode "
            + "WHERE NODE_CHECKLIST_ID = :nodeChecklistId";
        params.put("businessCode", GNOC_FILE_BUSSINESS.MR_NODE_CHECKLIST);
        params.put("nodeChecklistId", nodeChecklistId);
        lstNodeChecklistFiles = getNamedParameterJdbcTemplate()
            .query(sql, params, BeanPropertyRowMapper.newInstance(MrNodeChecklistFilesDTO.class));
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
    return lstNodeChecklistFiles;
  }
}
