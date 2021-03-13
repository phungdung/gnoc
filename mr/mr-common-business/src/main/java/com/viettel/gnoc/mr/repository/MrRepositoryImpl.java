package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.proxy.MrCategoryProxy;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.ConfigPropertyDTO;
import com.viettel.gnoc.maintenance.dto.MrApprovalDepartmentDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveRolesDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrCauseWoWasCompletedDTO;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrHisDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistFilesDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.maintenance.dto.MrWoTempDTO;
import com.viettel.gnoc.maintenance.model.MrApprovalDepartmentEntity;
import com.viettel.gnoc.maintenance.model.MrDeviceEntity;
import com.viettel.gnoc.maintenance.model.MrEntity;
import com.viettel.gnoc.maintenance.model.MrHisEntity;
import com.viettel.gnoc.maintenance.model.MrImpactedNodesEntity;
import com.viettel.gnoc.maintenance.model.MrNodeChecklistEntity;
import com.viettel.gnoc.maintenance.model.MrNodeChecklistFilesEntity;
import com.viettel.gnoc.maintenance.model.MrScheduleTelEntity;
import com.viettel.gnoc.maintenance.model.MrWoTempEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author tripm
 * @version 2.0
 * @since 12/06/2020 09:31:00
 */
@Repository
@Slf4j
public class MrRepositoryImpl extends BaseRepository implements MrRepository {

  @Autowired
  MrCategoryProxy mrCategoryProxy;

  @Override
  public List<MrNodeChecklistDTO> getListMrNodeChecklistForPopUp_VS(String woId, String mrNodeId) {
    Map<String, Object> params = new HashMap<>();
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR, "getListMrNodeChecklistForPopUp_VS");
      params.put("woId", woId);
      params.put("mrNodeId", mrNodeId);
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrNodeChecklistDTO.class));
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  @Override
  public String getConfigUCTTForCreateWo(String configGroup, String configCode) {
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = "SELECT CONFIG_VALUE value FROM OPEN_PM.MR_CONFIG where 1 = 1";

      if (!StringUtils.isStringNullOrEmpty(configGroup)) {
        sql += " AND CONFIG_GROUP =:configGroup ";
        params.put("configGroup", configGroup);
      }

      if (!StringUtils.isStringNullOrEmpty(configCode)) {
        sql += " AND CONFIG_CODE =:configCode ";
        params.put("configCode", configCode);
      }
      List<ConfigPropertyDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(ConfigPropertyDTO.class));
      return lst.get(0).getValue();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return null;
  }

  @Override
  public ResultInSideDto updateMrNodeChecklistForVS(MrNodeChecklistDTO dtoUpdate) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    try {
      if (dtoUpdate != null) {
        MrNodeChecklistEntity entity = getEntityManager().merge(dtoUpdate.toEntity());
        resultInSideDto.setId(entity.getMrNodeId());
        resultInSideDto.setMessage(RESULT.SUCCESS);
      }
      return resultInSideDto;
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return null;
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
            + "LEFT JOIN COMMON_GNOC.GNOC_FILE gf "
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

  @Override
  public String deleteFileFromMrNodeChecklistForVS(String nodeChecklistId) {
    try {
      Map<String, Object> params = new HashMap<>();

      String sql = "DELETE OPEN_PM.MR_NODE_CHECKLIST_FILES\n"
          + "WHERE 1 = 1";

      if (!StringUtils.isStringNullOrEmpty(nodeChecklistId)) {
        sql += " AND NODE_CHECKLIST_ID = :nodeChecklistId";
        params.put("nodeChecklistId", nodeChecklistId);
      }

      getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrNodeChecklistFilesDTO.class));

      return RESULT.SUCCESS;
    } catch (HibernateException e) {
      log.error(e.getMessage());
      return RESULT.FAIL;
    }

  }

  @Override
  public List<MrNodeChecklistDTO> findMrNodeChecklistById(String nodeChecklistId) {
    List<MrNodeChecklistDTO> lst = new ArrayList<>();
    try {
      Map<String, Object> params = new HashMap<>();

      String sql = "SELECT NODE_CHECKLIST_ID nodeChecklistId ,\n"
          + "  MR_NODE_ID mrNodeId ,\n"
          + "  CHECKLIST_ID checklistId ,\n"
          + "  STATUS status ,\n"
          + "  COMMENTS comments ,\n"
          + "  CREATED_USER createdUser ,\n"
          + "  CREATED_TIME createdTime ,\n"
          + "  UPDATED_USER updatedUser ,\n"
          + "  UPDATED_TIME updatedTime\n"
          + "FROM MR_NODE_CHECKLIST\n"
          + "WHERE 1               =1\n";

      if (!StringUtils.isStringNullOrEmpty(nodeChecklistId)) {
        sql += "AND NODE_CHECKLIST_ID = :nodeChecklistId";
        params.put("nodeChecklistId", nodeChecklistId);
      }

      lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrNodeChecklistDTO.class));

    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return lst;
  }

  @Override
  public ResultInSideDto insertFileMrNodeChecklistForVS(MrNodeChecklistFilesDTO filesDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    MrNodeChecklistFilesEntity entity = getEntityManager().merge(filesDTO.toEntity());
    resultInSideDto.setId(entity.getFileId());
    return resultInSideDto;
  }

  @Override
  public String getIdSequenceMrNodeChecklist() {
    try {
      Map<String, Object> params = new HashMap<>();

      String sql = "SELECT MR_NODE_CHECKLIST_SEQ.NEXTVAL nodeChecklistId FROM DUAL";

      List<MrNodeChecklistDTO> list = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrNodeChecklistDTO.class));

      return list.get(0).getNodeChecklistId();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return null;
  }

  @Override
  public ResultInSideDto insertMrNodeChecklistForVS(MrNodeChecklistDTO dtoInsert) {
    try {
      MrNodeChecklistEntity entity = getEntityManager().merge(dtoInsert.toEntity());

      return new ResultInSideDto(entity.getNodeChecklistId(), RESULT.SUCCESS, RESULT.SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage());
      return new ResultInSideDto(0L, RESULT.FAIL, e.getMessage());
    }


  }

  @Override
  public List<MrNodeChecklistDTO> getListMrNodeChecklistForPopUp(String woId, String mrNodeId) {
    Map<String, Object> params = new HashMap<>();
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR, "getListMrNodeChecklistForPopUp");
      params.put("woId", woId);
      params.put("mrNodeId", mrNodeId);
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrNodeChecklistDTO.class));

    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return null;
  }

  @Override
  public String updateWODischargeBattery(List<MrNodesDTO> listMrNodeDTO) {
    try {
      for (MrNodesDTO dto : listMrNodeDTO) {
        Map<String, Object> params = new HashMap<>();

        String sql = "SELECT STATUS status\n"
            + "FROM OPEN_PM.MR_NODES\n"
            + "WHERE 1 = 1\n";

        String[] strArray = dto.getWoId().split("_");
        String woId = strArray[strArray.length - 1];
        if (!StringUtils.isStringNullOrEmpty(woId)) {
          sql += "AND WO_ID   = :wo_id\n";
          params.put("wo_id", woId);
        }
        if (!StringUtils.isStringNullOrEmpty(dto.getNodeIp())) {
          sql += "AND NODE_IP   = :node_ip\n";
          params.put("node_ip", dto.getNodeIp());
        }
        if (!StringUtils.isStringNullOrEmpty(dto.getNodeName())) {
          sql += "AND NODE_NAME = :node_name";
          params.put("node_name", dto.getNodeName());
        }

        List<MrNodesDTO> list = getNamedParameterJdbcTemplateNormal()
            .query(sql, params, BeanPropertyRowMapper.newInstance(MrNodesDTO.class));

        if (list.size() > 0) {
          MrNodesDTO mrNodesDTO = list.get(0);
          if (Integer.valueOf(mrNodesDTO.getStatus()) == 0
              || Integer.valueOf(mrNodesDTO.getStatus()) == 3) {
            String sql1 = "UPDATE OPEN_PM.MR_NODES \n"
                + " SET STATUS    = :status, \n"
                + "  UPDATE_DATE = SYSDATE \n"
                + " WHERE WO_ID   = :wo_id \n"
                + " AND NODE_IP   = :node_ip \n"
                + " AND NODE_NAME = :node_name";

            params.put("status", 1);
            params.put("wo_id", woId);
            params.put("node_ip", dto.getNodeIp());
            params.put("node_name", dto.getNodeName());

            getNamedParameterJdbcTemplateNormal().update(sql1, params);

            try {
              String sql2 = " UPDATE OPEN_PM.MR_CD_BATTERY \n"
                  + " SET STATUS_ACCEPT  = NULL \n"
                  + " WHERE STATION_CODE = :stationCode \n"
                  + " AND DC_POWER       = :dcPower ";
              params.put("stationCode", dto.getNodeIp());
              params.put("dcPower", dto.getNodeName());
              getNamedParameterJdbcTemplateNormal().update(sql2, params);
            } catch (Exception e) {
              log.error(e.getMessage());
              return RESULT.FAIL;
            }
          }
        }
      }
      return RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage());
      return RESULT.FAIL;
    }
  }

  @Override
  public Boolean checkWO(String wo_id) {
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = "SELECT STATUS status FROM MR_NODES WHERE 1 = 1 ";
      String[] strArray = wo_id.split("_");
      wo_id = strArray[strArray.length - 1];
      if (!StringUtils.isStringNullOrEmpty(wo_id)) {
        sql += " AND WO_ID = :wo_id AND STATUS = 0";
        params.put("wo_id", wo_id);
      }

      List<MrNodesDTO> list = getNamedParameterJdbcTemplateNormal()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrNodesDTO.class));
      System.out.println("Query: " + list.size());
      return list.size() > 0 ? false : true;
    } catch (Exception e) {
      log.error(e.getMessage());
      return false;
    }
  }

  @Override
  public ResultDTO updateMrAndWoCDBattery(String woId, Date recentDischargeCd, String nodeName,
      String nodeIp) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = " UPDATE OPEN_PM.MR_CD_BATTERY \n"
          + " SET UPDATED_TIME = sysdate, \n";
      if (!StringUtils.isStringNullOrEmpty(recentDischargeCd)) {
        sql += "  RECENT_DISCHARGE_CD =:recentDischargeCd ";
        params.put("recentDischargeCd", recentDischargeCd);
      } else {
        sql += "  RECENT_DISCHARGE_CD = sysdate ";
      }
      sql += " WHERE 1=1 ";
      if (StringUtils.isNotNullOrEmpty(woId)) {
        sql += " AND WO_CODE like :woId ";
        woId = "%" + woId;
        params.put("woId", woId);
      }
      if (!StringUtils.isStringNullOrEmpty(nodeName)) {
        sql += " AND DC_POWER=:dcPower ";
        params.put("dcPower", nodeName);
      }
      if (!StringUtils.isStringNullOrEmpty(nodeIp)) {
        sql += " AND STATION_CODE=:stationCode ";
        params.put("stationCode", nodeIp);
      }
      getNamedParameterJdbcTemplateNormal().update(sql, params);
      resultDTO.setKey("1");
      resultDTO.setMessage(RESULT.SUCCESS);
    } catch (Exception e) {
      resultDTO.setKey("0");
      resultDTO.setMessage(RESULT.FAIL);
      log.error(e.getMessage());
    }
    return resultDTO;
  }

  /**
   * @author Dunglv3
   */
  @Override
  public ResultDTO createMrDTO(MrDTO mrDTO, UsersDTO udto, List<MrWoTempDTO> listMrWoTempDTO) {
    ResultDTO result = new ResultDTO();
    try {
      MrEntity mrEntity = mrDTO.toEntity();
      String ip = mrDTO.getNodeName();
      mrDTO.setNodeName(null);

      String deviceId = mrDTO.getNodeType();
      mrDTO.setNodeType(null);

      getEntityManager().merge(mrEntity);

      String sequence = mrDTO.getMrId();
      String mrCode = mrDTO.getMrCode();

      List<MrApproveSearchDTO> lstApprove = mrCategoryProxy
          .getLstMrApproveDeptByUser(udto.getUserId());
      MrApproveRolesDTO mrApproveRolesDTO;

      if ((lstApprove != null) && (lstApprove.size() == 2)) {
        mrApproveRolesDTO = new MrApproveRolesDTO();
        mrApproveRolesDTO.setRoleCode("TP");

        mrApproveRolesDTO.setUnitId((lstApprove.get(0)).getUnitId());
        List lstLevel1 = mrCategoryProxy.getLstMrApproveUserByRole(mrApproveRolesDTO);

        mrApproveRolesDTO.setUnitId((lstApprove.get(1)).getUnitId());
        List lstLevel2 = mrCategoryProxy.getLstMrApproveUserByRole(mrApproveRolesDTO);

        String userId = udto.getUserId();
        String unitId = udto.getUnitId();
        String status = "1";

        String comment;
        comment = (I18n.getLanguage("mrService.mr.add")) + mrCode + (I18n
            .getLanguage("mrService.mr.succes"));
        String actionCode = "1";
        MrHisDTO mrHisDTO1 = createMrHis(userId, unitId, status, comment, sequence, actionCode);
        MrHisEntity mrHisEntity1 = mrHisDTO1.toEntity();
        Long mhsId = Long.valueOf(getSeqTableBase("MR_HIS_SEQ"));
        mrHisEntity1.setMhsId(mhsId);
        if (lstLevel1 != null && !lstLevel1.isEmpty()) {
          if (lstLevel1.size() > 0) {
            userId = ((MrApproveRolesDTO) lstLevel1.get(0)).getUserId();
            unitId = ((MrApproveRolesDTO) lstLevel1.get(0)).getUnitId();

            status = "1";

            MrApproveRolesDTO mrApproveRoleslv1 = (MrApproveRolesDTO) lstLevel1.get(0);

            comment =
                (I18n.getLanguage("mrService.mr.approved")) + (mrApproveRoleslv1.getUserName())
                    + "-- " + (mrApproveRoleslv1.getUnitName()) + ", " + (I18n
                    .getLanguage("mrService.mr.resultApp"));

            actionCode = "2";
            MrHisDTO mrHisDTO2 = createMrHis(userId, unitId, status, comment, sequence, actionCode);
            MrHisEntity mrHisEntity2 = mrHisDTO2.toEntity();
            mhsId = Long.valueOf(getSeqTableBase("MR_HIS_SEQ"));
            mrHisEntity2.setMhsId(mhsId);

            MrApprovalDepartmentDTO app1 = createMrAppro(sequence, unitId, "1", "1", userId,
                "Lý do: Thông tin đủ, chính xác, Chi tiết: Dong y", "3");
            MrApprovalDepartmentEntity appDept1 = app1.toEntiy();
            mhsId = Long.valueOf(getSeqTableBase("MR_APPROVE_DEPT__SEQ"));
            appDept1.setMadtId(mhsId);

            userId = ((MrApproveRolesDTO) lstLevel2.get(0)).getUserId();
            unitId = ((MrApproveRolesDTO) lstLevel2.get(0)).getUnitId();
            status = "3";

            MrApproveRolesDTO mrApproveRoleslv2 = (MrApproveRolesDTO) lstLevel2.get(0);
            comment =
                (I18n.getLanguage("mrService.mr.approved")) + (mrApproveRoleslv2.getUserName())
                    + "-- " + (mrApproveRoleslv2.getUnitName()) + ", " + (I18n
                    .getLanguage("mrService.mr.resultActivated"));

            MrApprovalDepartmentDTO app2 = createMrAppro(sequence, unitId, "2", "1", userId,
                "Lý do: Thông tin đủ, chính xác, Chi tiết: Dong y", "3");
            MrApprovalDepartmentEntity appDept2 = app2.toEntiy();
            mhsId = Long.valueOf(getSeqTableBase("MR_APPROVE_DEPT__SEQ"));
            appDept2.setMadtId(mhsId);

            MrHisDTO mrHisDTO3 = createMrHis(userId, unitId, status, comment, sequence, actionCode);
            MrHisEntity mrHisEntity3 = mrHisDTO3.toEntity();
            mhsId = Long.valueOf(getSeqTableBase("MR_HIS_SEQ"));
            mrHisEntity3.setMhsId(mhsId);

            userId = udto.getUserId();
            unitId = udto.getUnitId();
            status = "4";

            comment = (I18n.getLanguage("mrService.mr.perform")) + (udto.getUsername()) + ("-- ")
                + (udto.getUnitId()) + (I18n.getLanguage("mrService.mr.status"));
            actionCode = "4";

            MrHisDTO mrHisDTO4 = createMrHis(userId, unitId, status, comment, sequence, actionCode);
            MrHisEntity mrHisEntity4 = mrHisDTO4.toEntity();
            mhsId = Long.valueOf(getSeqTableBase("MR_HIS_SEQ"));
            mrHisEntity4.setMhsId(mhsId);
          }
        }
      }

      if ((listMrWoTempDTO != null) && (listMrWoTempDTO.isEmpty())) {
        for (MrWoTempDTO mrWoTempDTO : listMrWoTempDTO) {
          MrWoTempEntity mrWoTempEntity = mrWoTempDTO.toEntity();
          Long woId = Long.valueOf(getSeqTableBase("MR_WO_TEMP_SEQ"));
        }
      }
      if (ip == null) {
        MrImpactedNodesEntity impactedNodesEntity = new MrImpactedNodesEntity();
        impactedNodesEntity.setMrId(mrEntity.getMrId());
        impactedNodesEntity.setIpId(ip == null ? null : Long.valueOf(ip));
        impactedNodesEntity.setDeviceId(deviceId == null ? null : Long.valueOf(deviceId));
        impactedNodesEntity.setInsertTime(new Date());
      } else {
        String[] lstIp = ip.split(";");
        String[] lstDeviceId = deviceId.split(";");
        for (int i = 0; i < lstIp.length; i++) {
          if (i == lstIp.length - 1 && "".equals(lstIp[i])) {
            continue;
          }
          MrImpactedNodesEntity impactedNodesEntity = new MrImpactedNodesEntity();
          impactedNodesEntity.setMrId(mrEntity.getMrId());
          impactedNodesEntity
              .setIpId(!StringUtils.isStringNullOrEmpty(lstIp[i]) ? Long.valueOf(lstIp[i]) : null);
          impactedNodesEntity.setDeviceId(
              !StringUtils.isStringNullOrEmpty(lstDeviceId[i]) ? Long.valueOf(lstDeviceId[i])
                  : null);
          impactedNodesEntity.setInsertTime(new Date());
        }
      }
      result.setId(mrDTO.getMrId());
      result.setMessage(Constants.RESULT.SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return result;
  }

  /**
   * @author Dunglv3
   */
  private MrHisDTO createMrHis(String userId, String unitId, String status, String comment,
      String mrId, String acctionCode)
      throws Exception {
    MrHisDTO result = new MrHisDTO();
    result.setUserId(userId);
    result.setUnitId(unitId);
    result.setStatus(status);
    result.setComments(comment);
    result.setMrId(mrId);
    result.setActionCode(acctionCode);
    result.setChangeDate(DateTimeUtils.convertDateToString(new Date(), "dd/MM/yyyy HH:mm:ss"));
    return result;
  }

  /**
   * @author Dunglv3
   */
  public MrApprovalDepartmentDTO createMrAppro(String mrId, String unitId, String level,
      String status, String userId, String note, String returnCode)
      throws Exception {
    MrApprovalDepartmentDTO result = new MrApprovalDepartmentDTO();
    result.setMrId(mrId);
    result.setUnitId(unitId);
    result.setMadtLevel(level);
    result.setStatus(status);
    result.setUserId(userId);
    result.setNotes(note);
    result.setReturnCode(returnCode);
    result.setIncommingDate(DateTimeUtils.convertDateToString(new Date(), "dd/MM/yyyy HH:mm:ss"));
    result.setApprovedDate(DateTimeUtils.convertDateToString(new Date(), "dd/MM/yyyy HH:mm:ss"));
    return result;
  }

  @Override
  public boolean checkNodeStatusByWo(String woId) {
    try {
      Map<String, Object> params = new HashMap<>();
      String sql = " SELECT "
          + " STATUS status "
          + " FROM MR_NODES WHERE WO_ID = :wo_id AND STATUS != 1 ";
      String[] strArray = woId.split("_");
      params.put("wo_id", strArray[strArray.length - 1]);

      List<MrNodesDTO> list = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrNodesDTO.class));
      if (list != null && !list.isEmpty()) {
        return false;
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      return false;
    }
    return true;
  }

  @Override
  public ResultInSideDto delete(Long mrId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (!StringUtils.isStringNullOrEmpty(mrId)) {
      MrEntity entity = getEntityManager()
          .find(MrEntity.class, mrId);
      if (entity == null) {
        resultInSideDto.setKey(RESULT.FAIL);
      }
      getEntityManager().remove(entity);
    }
    return resultInSideDto;
  }

  @Override
  public boolean updateMrState(String mrId, String state, ResultDTO res) {
    try {
      if (mrId == null || state == null) {
        return false;
      }
      String sql = " update mr a set a.state =:state where a.mr_id =:mrId ";
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("state", state);
      parameters.put("mrId", mrId);
      res.setId("1");
      res.setKey("SUCCESS");
      getNamedParameterJdbcTemplateNormal().update(sql, parameters);
      return true;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res.setId("0");
      res.setKey("FAIL");
      res.setMessage(res.getMessage() + "==== update state : " + e.getMessage());
    }
    return false;
  }

  @Override
  public ResultInSideDto insertMrHis(MrHisDTO mrHisDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    MrHisEntity mrHisEntity = getEntityManager().merge(mrHisDTO.toEntity());
    resultInSideDto.setId(mrHisEntity.getMhsId());
    return resultInSideDto;
  }

  @Override
  public ResultDTO insertMrScheduleTelHis(MrScheduleTelHisDTO mrScheduleTelHisDTO) {
    ResultDTO resultInSideDto = new ResultDTO(RESULT.SUCCESS, RESULT.SUCCESS, RESULT.SUCCESS);
    getEntityManager().merge(mrScheduleTelHisDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public List<MrScheduleTelDTO> getByMrId(String mrId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR, "getByMrId");
    Map<String, Object> params = new HashMap<>();
    params.put("mrId", mrId);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(MrScheduleTelDTO.class));
  }

  @Override
  public ResultInSideDto deleteMrScheduleTel(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    MrScheduleTelEntity mrScheduleTelEntity = getEntityManager()
        .find(MrScheduleTelEntity.class, id);
    if (mrScheduleTelEntity != null && mrScheduleTelEntity.getScheduleId() != null) {
      getEntityManager().remove(mrScheduleTelEntity);
      resultInSideDto.setId(id);
    }
    return resultInSideDto;
  }

  @Override
  public MrDeviceDTO findDetailById(Long deviceId) {
    MrDeviceEntity mrDeviceEntity = getEntityManager()
        .find(MrDeviceEntity.class, deviceId);
    if (mrDeviceEntity != null) {
      return mrDeviceEntity.toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto updateMrDeviceDTO(MrDeviceDTO dtoUpdate) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    MrDeviceEntity mrDeviceEntity = getEntityManager().merge(dtoUpdate.toEntity());
    resultInSideDto.setId(mrDeviceEntity.getDeviceId());
    return resultInSideDto;
  }


  @Override
  public List<MrCauseWoWasCompletedDTO> getReasonWO(String reasonTypeId) {
    if (StringUtils.isStringNullOrEmpty(reasonTypeId)) {
      return new ArrayList<>();
    }
    String sql = "SELECT REASON_CODE reasonCode,REASON_NAME reasonName, WAITING_TIME waitingTime, VALIDATE_PROCESS validateProcess "
        + " FROM OPEN_PM.CAUSE_WO_WAS_COMPLETED"
        + " WHERE REASON_TYPE  IN "
        + "         (select ITEM_ID from COMMON_GNOC.CAT_ITEM where CATEGORY_ID IN (select CATEGORY_ID from COMMON_GNOC.CATEGORY "
        + "         where CATEGORY_CODE = 'BTS_REASON_WO_NOT_COMPLETE') AND STATUS = 1 AND ITEM_CODE = :reasonType ) ";
    Map<String, Object> params = new HashMap<>();
    params.put("reasonType", reasonTypeId);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(MrCauseWoWasCompletedDTO.class));
  }
}

