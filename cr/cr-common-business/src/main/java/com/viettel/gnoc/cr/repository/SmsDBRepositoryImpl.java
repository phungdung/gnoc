package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CR_SEARCH_TYPE;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGES;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.SYSTEM;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentInsiteDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.MessageForm;
import com.viettel.gnoc.cr.model.UserReceiveMsgEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SmsDBRepositoryImpl extends BaseRepository implements SmsDBRepository {

  @Autowired
  CrDBRepository crDBRepository;

  @Autowired
  MessagesRepository messagesRepository;

  public List<MessageForm> getListUsersByUserId(List<Long> listUserId,
      boolean getManager) {
    List<MessageForm> lst = new ArrayList<MessageForm>();
    try {
      if (listUserId.isEmpty()) {
        return lst;
      }
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DB_DAO, "get-list-user-by-userids");
      Map<String, Object> param = new HashMap<>();
      if (!getManager) {
        sql += " and (us.IS_NOT_RECEIVE_MESSAGE is null or us.IS_NOT_RECEIVE_MESSAGE<>1) ";//tuanpv14 bo sung cau hinh nhan tin nhan
      }
      sql += " and us.is_enable = 1 ";
      sql += " and ut.status = 1 ";
      sql += " and us.user_id in (:listUserId)";
      param.put("listUserId", listUserId);
      lst = getNamedParameterJdbcTemplate()
          .query(sql, param, BeanPropertyRowMapper.newInstance(MessageForm.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public void actionInsertIntoMess(MessageForm messageForm) {
    if (messageForm.getUserId() == null) {
      return;
    }
    if (messageForm.getUserName() == null) {
      return;
    }
    if (messageForm.getCellPhone() == null) {
      return;
    }
    if (messageForm.getSmsGatewayId() == null) {
      return;
    }
    if (messageForm.getMessageContent() == null) {
      return;
    }
    try {
      MessagesDTO messagesDTO = new MessagesDTO();
      messagesDTO.setSmsGatewayId(messageForm.getSmsGatewayId() == null ? null
          : String.valueOf(messageForm.getSmsGatewayId()));
      messagesDTO.setContent(messageForm.getMessageContent());
      messagesDTO.setReceiverId(
          messageForm.getUserId() == null ? null : String.valueOf(messageForm.getUserId()));
      messagesDTO.setCreateTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
      messagesDTO.setReceiverUsername(messageForm.getUserName());
      messagesDTO.setReceiverPhone(messageForm.getCellPhone());
      messagesDTO.setAlias(messageForm.getAlias());
      messagesDTO.setStatus("0");
      messagesRepository.insertOrUpdateCommon(messagesDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public ResultDTO actionSendSmsIncaseOfCreateCROrUpdateCR(ResultDTO actionResult,
      CrInsiteDTO crDTO) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      List<MessageForm> lstScope = actionGetAllUnitOfScope();
      Long userIdTosendMS = Long.parseLong(crDTO.getUserLogin());
      List<Long> lstFlong = new ArrayList<Long>();
      lstFlong.add(userIdTosendMS);
      List<MessageForm> listFRST = getListUsersByUserId(lstFlong, true);
      if (listFRST.isEmpty()) {
        return null;
      }
      MessageForm userTK = listFRST.get(0);
      List<MessageForm> listUsertToSendSMSTo = new ArrayList<MessageForm>();
//            List<MessageForm> listUsertToCreateNotiTo = new ArrayList<MessageForm>();
      CrInsiteDTO crDTOtoSendSMS = getCrById(Long.parseLong(crDTO.getCrId()));
      if (actionResult.getKey().equals(Constants.CR_RETURN_MESSAGE.APPROVECRINFIRSTPLACE)) {
        List<Long> listDept = new ArrayList<Long>();
        List<Long> listUser = new ArrayList<Long>();
        //LinhVM: neu la CR chuan va user tao la TP --> pass qua buoc phe duyet
        //CR chuyen sang trang thai cho tiep nhan thuc thi --> nhan tin cho don vi tiep nhan
        if (crDTOtoSendSMS.getCrType().equals(Constants.CR_TYPE.STANDARD.toString())
            && crDTOtoSendSMS.getState().equals(Constants.CR_STATE.APPROVE.toString())) {
          if (crDTOtoSendSMS.getChangeResponsibleUnit() != null) {
            listDept.add(Long.parseLong(crDTOtoSendSMS.getChangeResponsibleUnit()));
            listUsertToSendSMSTo = getListUsersByDepartment(listDept, true);
            //20160531-daitt1: bo sung chuc nang tim lanh dao don vi cha
            if (listUsertToSendSMSTo.isEmpty()) {
              listUsertToSendSMSTo = getListHigherBoss(
                  Long.parseLong(crDTOtoSendSMS.getChangeResponsibleUnit()));
            }
            //20160531-daitt1: bo sung chuc nang tim lanh dao don vi cha

          }
          for (MessageForm messageForm : listUsertToSendSMSTo) {
            String content = getMessageForImpBoss(crDTOtoSendSMS, messageForm, userTK);
            messageForm.setMessageContent(content);
            messageForm.setActionCode(Constants.CR_SEARCH_TYPE.EXCUTE);
            messageForm.setSaveNotify(true);
          }
        } else {
          if (crDTOtoSendSMS.getManageUnitId() != null
              && crDTOtoSendSMS.getManageUserId() == null) {
            listDept.add(Long.parseLong(crDTOtoSendSMS.getManageUnitId()));
            listUsertToSendSMSTo = getListUsersByDepartment(listDept, false);
          } else if (crDTOtoSendSMS.getManageUnitId() != null
              && crDTOtoSendSMS.getManageUserId() != null) {
            listUser.add(Long.parseLong(crDTOtoSendSMS.getManageUserId()));
            listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
          } else if (crDTOtoSendSMS.getManageUnitId() == null
              && crDTOtoSendSMS.getManageUserId() == null) {
            listDept = actionGetListManager(crDTOtoSendSMS, lstScope, true);
            listUsertToSendSMSTo = getListUsersByDepartment(listDept, false);
          }
          for (MessageForm messageForm : listUsertToSendSMSTo) {
            String content = getMessageForVerifyCR(crDTOtoSendSMS, messageForm, userTK);
            messageForm.setMessageContent(content);
            messageForm.setActionCode(Constants.CR_SEARCH_TYPE.VERIFY);
            messageForm.setSaveNotify(true);
          }
        }

      } else if (actionResult.getKey().equals(Constants.CR_RETURN_MESSAGE.APPROVEINFIRSTPLACE)) {
        CrApprovalDepartmentInsiteDTO currentCrApprovalDepartmentDTO
            = getCurrentCrApprovalDepartmentDTO(
            Long.parseLong(crDTO.getCrId()),
            Long.parseLong(crDTO.getUserLogin()),
            Long.parseLong(crDTO.getUserLoginUnit()));
        if (isLastDeptOfApproveLevel(
            Long.parseLong(currentCrApprovalDepartmentDTO.getCadtLevel()),
            Long.parseLong(currentCrApprovalDepartmentDTO.getCadtId()),
            Long.parseLong(currentCrApprovalDepartmentDTO.getCrId()))) {
          List<CrApprovalDepartmentInsiteDTO> lst
              = getNextApprovalDepartment(
              Long.parseLong(currentCrApprovalDepartmentDTO.getCrId()),
              Long.parseLong(currentCrApprovalDepartmentDTO.getCadtLevel()),
              true);
          //20160531 daitt1 sua chuc ang nhan tin
//                    List<Long> listDeptId = new ArrayList<Long>();
          for (CrApprovalDepartmentInsiteDTO crApprovalDepartmentDTO : lst) {

            List<Long> listTMp = new ArrayList<>();
            listTMp.add(Long.parseLong(crApprovalDepartmentDTO.getUnitId()));
            List<MessageForm> listTMpMss
                = getListUsersByDepartment(
                listTMp, true);
            if (listTMpMss.isEmpty()) {
              listTMpMss = getListHigherBoss(
                  Long.parseLong(crApprovalDepartmentDTO.getUnitId()));
            }
            if (!listTMpMss.isEmpty()) {
              listUsertToSendSMSTo.addAll(listTMpMss);
            }
          }
//                    listUsertToSendSMSTo
//                            = getListUsersByDepartment(session,
//                                    listDeptId, true);
          //20160531 daitt1 sua chuc ang nhan tin
//                    listUsertToCreateNotiTo
//                            = getListUsersByDepartment(session,
//                                    listDeptId, true);
          for (MessageForm messageForm : listUsertToSendSMSTo) {
            String content = getMessageForApprove(crDTOtoSendSMS, messageForm, userTK);
            messageForm.setMessageContent(content);
            messageForm.setActionCode(Constants.CR_SEARCH_TYPE.APPROVE);
            messageForm.setSaveNotify(true);
          }
//                    for (MessageForm messageForm : listUsertToCreateNotiTo) {
//                        String content = getMessageForNotification(crDTOtoSendSMS, messageForm, userTK);
//                        messageForm.setMessageContent(content);
//                    }

        } else {
          List<CrApprovalDepartmentInsiteDTO> lst
              = getNextApprovalDepartment(
              Long.parseLong(currentCrApprovalDepartmentDTO.getCrId()),
              Long.parseLong(currentCrApprovalDepartmentDTO.getCadtLevel()),
              false);
//                    List<Long> listDeptId = new ArrayList<Long>();
          //20160531 daitt1 sua chuc ang nhan tin
          for (CrApprovalDepartmentInsiteDTO crApprovalDepartmentDTO : lst) {
//                        listDeptId.add(Long.parseLong(crApprovalDepartmentDTO.getUnitId()));
            List<Long> listTMp = new ArrayList<>();
            listTMp.add(Long.parseLong(crApprovalDepartmentDTO.getUnitId()));
            List<MessageForm> listTMpMss
                = getListUsersByDepartment(
                listTMp, true);
            if (listTMpMss.isEmpty()) {
              listTMpMss = getListHigherBoss(
                  Long.parseLong(crApprovalDepartmentDTO.getUnitId()));
            }
            if (!listTMpMss.isEmpty()) {
              listUsertToSendSMSTo.addAll(listTMpMss);
            }
          }
          //20160531 daitt1 sua chuc ang nhan tin
//                    listUsertToCreateNotiTo
//                            = getListUsersByDepartment(session,
//                                    listDeptId, true);
//                    listUsertToSendSMSTo
//                            = getListUsersByDepartment(session,
//                                    listDeptId, true);
          for (MessageForm messageForm : listUsertToSendSMSTo) {
            String content = getMessageForApprove(crDTOtoSendSMS, messageForm, userTK);
            messageForm.setMessageContent(content);
            messageForm.setActionCode(Constants.CR_SEARCH_TYPE.APPROVE);
            messageForm.setSaveNotify(true);
          }
//                    for (MessageForm messageForm : listUsertToCreateNotiTo) {
//                        String content = getMessageForNotification(crDTOtoSendSMS, messageForm, userTK);
//                        messageForm.setMessageContent(content);
//                    }
        }
      } else if (actionResult.getKey().equals(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
        List<CrApprovalDepartmentInsiteDTO> lst
            = getNextApprovalDepartment(Long.parseLong(crDTO.getCrId()), null, true);
//                List<Long> listDeptId = new ArrayList<Long>();
        //20160531 daitt1 sua chuc ang nhan tin
        for (CrApprovalDepartmentInsiteDTO crApprovalDepartmentDTO : lst) {
//                    listDeptId.add(Long.parseLong(crApprovalDepartmentDTO.getUnitId()));
          List<Long> listTMp = new ArrayList<Long>();
          listTMp.add(Long.parseLong(crApprovalDepartmentDTO.getUnitId()));
          List<MessageForm> listTMpMss
              = getListUsersByDepartment(listTMp, true);
          if (listTMpMss.isEmpty()) {
            listTMpMss = getListHigherBoss(Long.parseLong(crApprovalDepartmentDTO.getUnitId()));
          }
          if (!listTMpMss.isEmpty()) {
            listUsertToSendSMSTo.addAll(listTMpMss);
          }
        }
        //20160531 daitt1 sua chuc ang nhan tin
//                listUsertToCreateNotiTo
//                        = getListUsersByDepartment(session,
//                                listDeptId, true);
//                listUsertToSendSMSTo
//                        = getListUsersByDepartment(session,
//                                listDeptId, true);
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageForApprove(crDTOtoSendSMS, messageForm, userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.APPROVE);
          messageForm.setSaveNotify(true);
        }
//                for (MessageForm messageForm : listUsertToCreateNotiTo) {
//                    String content = getMessageForNotification(crDTOtoSendSMS, messageForm, userTK);
//                    messageForm.setMessageContent(content);
//                }
      }
      String contentToSendToCreator = getMessageIncaseOfCreateOrUpdateCR(crDTOtoSendSMS, userTK,
          userTK);
      userTK.setMessageContent(contentToSendToCreator);
      actionInsertIntoMess(userTK);
//            listUsertToSendSMSTo.add(userTK);
      StringBuilder retMess = new StringBuilder();
      String messContent = "";

      //R941966_quangdx_start
      List<UserReceiveMsgEntity> lstReceiveMsg = getLstConfigReceiveSmS(crDTOtoSendSMS.getCrId());
      if (lstReceiveMsg != null && !lstReceiveMsg.isEmpty() && listUsertToSendSMSTo != null
          && !listUsertToSendSMSTo.isEmpty()) {
        List<Long> listUser = new ArrayList<Long>();
        for (UserReceiveMsgEntity user : lstReceiveMsg) {
          listUser.add(user.getUserId());
        }
        List<MessageForm> listTemp = getListUsersByUserId(listUser, false);
        if (listTemp != null && !listTemp.isEmpty()) {
          for (MessageForm messageForm : listTemp) {
            messageForm.setActionCode(listUsertToSendSMSTo.get(0).getActionCode());
            messageForm.setSaveNotify(false);
            messageForm.setMessageContent(listUsertToSendSMSTo.get(0).getMessageContent());
            listUsertToSendSMSTo.add(messageForm);
          }
        }
      }
      //R941966_quangdx_end
      for (MessageForm messageForm : listUsertToSendSMSTo) {
        actionInsertIntoMess(messageForm);
        if (messageForm.isSaveNotify()) {
          actionInsertIntoNotification(messageForm, crDTO);
          retMess.append(messageForm.getUserId() + ",");
          messContent = messageForm.getMessageContent();
        }
      }
      retMess.append(Constants.SPLIT2 + messContent);
      resultDTO.setMessage(retMess.toString());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return resultDTO;
  }

  public List<MessageForm> actionGetAllUnitOfScope() {
    List<MessageForm> lst = new ArrayList<MessageForm>();
    try {
      StringBuilder sql = new StringBuilder("");
      sql.append(" select cmse_id cmseId,unit_id unitId "
          + " from cr_manager_units_of_scope ");
      lst = getNamedParameterJdbcTemplate().query(sql.toString(), new HashMap<>(),
          BeanPropertyRowMapper.newInstance(MessageForm.class));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public CrInsiteDTO getCrById(Long crId) {
    try {
      if (crId != null) {
        String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SMS,
            "get-cr-by-id");
        Map<String, Object> params = new HashMap<>();
        params.put("cr_id", crId);
        List<CrInsiteDTO> list = getNamedParameterJdbcTemplate().query(sql, params,
            BeanPropertyRowMapper.newInstance(CrInsiteDTO.class));
        if (list != null && !list.isEmpty()) {
          return list.get(0);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public CrInsiteDTO getCrInfoById(Long crId, Date startCreatedDate, Date endCreatedDate) {
    try {
      if (crId != null) {
        String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SMS,
            "get-cr-info-by-id");
        Map<String, Object> params = new HashMap<>();
        params.put("crId", crId);
        if (startCreatedDate != null) {
          sql += " and  cr.CREATED_DATE >= :startCreatedDate ";
          params.put("startCreatedDate", startCreatedDate);
        }
        if (endCreatedDate != null) {
          sql += " and  cr.CREATED_DATE <= :endCreatedDate ";
          params.put("endCreatedDate", endCreatedDate);
        }
        List<CrInsiteDTO> list = getNamedParameterJdbcTemplate().query(sql, params,
            BeanPropertyRowMapper.newInstance(CrInsiteDTO.class));
        if (list != null && !list.isEmpty()) {
          return list.get(0);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public List<MessageForm> getListUsersByDepartment(List<Long> listDepartment,
      boolean getManager) {
    List<MessageForm> lst = new ArrayList<MessageForm>();
    try {
      if (listDepartment.isEmpty()) {
        return lst;
      }
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SMS, "get-list-user-by-department");
      if (getManager) {
        sql += " and exists (select 1 from common_gnoc.v_user_role "
            + " where is_manager = 1 "
            + " and user_id = us.user_id )";
      }
//            //log.info(sql.toString());
      sql += " and ut.unit_id in (:listDepartment)";
      Map<String, Object> params = new HashMap<>();
      params.put("listDepartment", listDepartment);

      lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MessageForm.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public List<MessageForm> getListHigherBoss(Long deptId) {
    List<MessageForm> lst = new ArrayList<MessageForm>();
    try {
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SMS, "get-list-higher-boss");
      Map<String, Object> params = new HashMap<>();
      params.put("unit_id", deptId);

      List<MessageForm> listTemp = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MessageForm.class));
      if (!listTemp.isEmpty()) {
        MessageForm messageForm = listTemp.get(0);
        String path = messageForm.getDeptCode();
        for (MessageForm mess : listTemp) {
          if (path != null
              && mess.getDeptCode() != null
              && mess.getDeptCode().equals(path)) {
            lst.add(mess);
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public String getMessageForImpBoss(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR]";
    mess += I18n.getLanguageByLocale(locale, "CR.boss.implement");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  public List<Long> actionGetListManager(CrInsiteDTO crDTOtoSendSMS,
      List<MessageForm> listScope, boolean isActionApprove) {
    List<Long> list = new ArrayList<Long>();
    try {
      Long receiveDept = Long.parseLong(crDTOtoSendSMS.getChangeResponsibleUnit());
      String path = actionGetPathOfDept(receiveDept);
      List<Long> listScopeId = new ArrayList<Long>();
      for (MessageForm messageForm : listScope) {
        if (path != null && path.contains("|" + messageForm.getUnitId() + "|")) {
//                    //log.info("===actionGetListManager unit_id :" + messageForm.getUnitId());
//                    //log.info("===actionGetListManager cmse_id :" + messageForm.getCmseId());
          listScopeId.add(messageForm.getCmseId());
        }
      }
      if (listScopeId.isEmpty()) {
        return list;
      }
      StringBuilder sql = new StringBuilder("");
      sql.append(" select distinct(manage_unit) as unitId \n");
      sql.append(" from v_manage_cr_config \n");
      sql.append(" where scope_id in (:listScopeId)");
      //tuanpv14_sap lich cr khan start
      if (isActionApprove) {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 ");
      }
      Map<String, Object> params = new HashMap<>();
      params.put("listScopeId", listScopeId);
      List<UnitDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(UnitDTO.class));
      //log.info("=======  actionGetListManager #!@5:" + lst.size());
      String unitLst = getListUnitNotSendSmsBd();
      for (UnitDTO unitDTO : lst) {
        if (crDTOtoSendSMS.getCrType().equals(Constants.CR_TYPE.NORMAL.toString())
            && crDTOtoSendSMS.getTitle().toUpperCase()
            .contains("GNOC MR")) {//su co bao duong ko nhan cho Z78
          if (unitLst.contains(String.valueOf(unitDTO.getUnitId()))) {
            continue;
          }
        }
        list.add(unitDTO.getUnitId());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public String actionGetPathOfDept(Long receiveDept) {
    String result = "";
    try {
      StringBuilder sql = new StringBuilder();
      sql.append("select '|'||path||'|' as deptCode "
          + " from common_gnoc.v_unit_as_tree"
          + " where unit_id = :unit_id");
      Map<String, Object> params = new HashMap<>();
      params.put("unit_id", receiveDept);
      List<MessageForm> lst = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(MessageForm.class));

      if (!lst.isEmpty()) {
        return lst.get(0).getDeptCode();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  private String getListUnitNotSendSmsBd() {
    StringBuilder sql = new StringBuilder("");
    sql.append(
        " select VALUE unitId from COMMON_GNOC.CONFIG_PROPERTY where key = 'unit_cr_not_send_sms_bd' ");
    List<UnitDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql.toString(), new HashMap<>(), BeanPropertyRowMapper.newInstance(UnitDTO.class));
    if (lst != null && lst.size() > 0) {
      return StringUtils.isStringNullOrEmpty(lst.get(0).getUnitId()) ? ""
          : String.valueOf(lst.get(0).getUnitId());
    }
    return "";
  }

  public String getMessageForVerifyCR(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR]  ";
    mess += I18n.getLanguageByLocale(locale, "CR.you.have.a.CR.toVerify");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  public CrApprovalDepartmentInsiteDTO getCurrentCrApprovalDepartmentDTO(Long crId, Long userId,
      Long deptId) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SMS, "get-current-approval-department");
      Map<String, Object> params = new HashMap<>();
      params.put("unit_id", deptId);
      params.put("cr_id", crId);
      List<CrApprovalDepartmentInsiteDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, params,
              BeanPropertyRowMapper.newInstance(CrApprovalDepartmentInsiteDTO.class));
      if (!lst.isEmpty()) {
        return lst.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public boolean isLastDeptOfApproveLevel(Long cadtLevel, Long cadtId, Long crId) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SMS, "is-last-dept-of-approval-level");
      Map<String, Object> params = new HashMap<>();
      params.put("cadt_level", cadtLevel);
      params.put("cadt_id", cadtId);
      params.put("cr_id", crId);
      List lst = getNamedParameterJdbcTemplate()
          .query(sql, params,
              BeanPropertyRowMapper.newInstance(CrApprovalDepartmentInsiteDTO.class));

      if (!lst.isEmpty()) {
        return false;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return true;
  }

  public List<CrApprovalDepartmentInsiteDTO> getNextApprovalDepartment(Long crId, Long cadtLevel,
      boolean lastDeptOfItLevel) {
    List<CrApprovalDepartmentInsiteDTO> lst = new ArrayList<CrApprovalDepartmentInsiteDTO>();
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_SMS, "get-next-approval-department");
      Map<String, Object> params = new HashMap<>();
      params.put("cr_id", crId);
      if (cadtLevel != null && lastDeptOfItLevel) {
        sql += " and cadt_level > :cadt_level \n";
        params.put("cadt_level", cadtLevel);
      } else if (cadtLevel != null) {
        sql += "and cadt_level = :cadt_level \n ";
        params.put("cadt_level", cadtLevel);
      }
      sql += " and cr_id = :cr_id and status = 0 ";
      lst = getNamedParameterJdbcTemplate()
          .query(sql, params,
              BeanPropertyRowMapper.newInstance(CrApprovalDepartmentInsiteDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public List<UserReceiveMsgEntity> getLstConfigReceiveSmS(String crId) {
    List<UserReceiveMsgEntity> lst = new ArrayList<>();
    if (crId != null && !"".equals(crId)) {
      lst = findByMultilParam(UserReceiveMsgEntity.class, "crId", Long.valueOf(crId));
    }
    return lst;
  }

  public void actionInsertIntoNotification(MessageForm messageForm, CrInsiteDTO crDTO) {
    if (messageForm.getUserId() == null || messageForm.getUserName() == null
        || messageForm.getCellPhone() == null
        || messageForm.getSmsGatewayId() == null || messageForm.getMessageContent() == null
        || messageForm.getActionCode() == null
    ) {
      return;
    }

    try {
      StringBuilder sql = new StringBuilder("");
      sql.append(" insert into common_gnoc.gnoc_notification values(");
      sql.append(
          " common_gnoc.gnoc_notification_seq.nextval, :system_cr,:cr_id,:action_code,:message_content, :message_content,"
              + " :user_id,sysdate,:zeros)");
      Map<String, Object> params = new HashMap<>();
      params.put("system_cr", SYSTEM.CR);
      params.put("cr_id", Long.parseLong(crDTO.getCrId()));
      params.put("action_code", messageForm.getActionCode());
      params.put("message_content", messageForm.getMessageContent());
      params.put("user_id", messageForm.getUserId());
      params.put("zeros", 0L);
      getNamedParameterJdbcTemplate().update(sql.toString(), params);
      getEntityManager().flush();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public String getMessageForApprove(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] ";
    mess += I18n.getLanguageByLocale(locale, "CR.TO.APPROVE");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  public String getMessageIncaseOfCreateOrUpdateCR(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] ";
    mess += I18n.getLanguageByLocale(locale, "CR.i.created.it");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  public Locale getLocale(MessageForm messageForm) {
    if (messageForm == null) {
      return new Locale("vi", "VN");
    } else if (messageForm.getUserLanguage() != null
        && messageForm.getUserLanguage().equals(LANGUAGES.VIETNAMESE)) {
      return new Locale("vi", "VN");
    }
    if (messageForm.getUserLanguage() != null
        && messageForm.getUserLanguage().equals(LANGUAGES.ENGLISH)) {
      return new Locale("en", "US");
    } else {
      return new Locale("vi", "VN");
    }
  }

  @Override
  public void actionSendSmsIncaseOfApproveCR(Long actionType, CrInsiteDTO crDTO,
      List<CrApprovalDepartmentInsiteDTO> listLowerLevelDept, boolean isLastDepartment) {
    if (crDTO == null) {
      return;
    }

    try {
      if (crDTO.getCrNumber() == null || crDTO.getCrNumber().trim().isEmpty()) {
        if (crDTO.getCrId() != null) {
          Long id = objectToLong(crDTO.getCrId());
          if (id != null) {
            CrInsiteDTO crDTOnew = getCrInfoById(id, null, null);
            if (crDTOnew != null) {
              crDTO.setCrNumber(crDTOnew.getCrNumber());
              crDTO.setTitle(crDTOnew.getTitle());
            }
          }
        }

      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    try {
      List<MessageForm> lstScope = actionGetAllUnitOfScope();
      Long userIdTosendMS = Long.parseLong(crDTO.getUserLogin());
      List<Long> lstFlong = new ArrayList<Long>();
      lstFlong.add(userIdTosendMS);
      List<MessageForm> listFRST = getListUsersByUserId(lstFlong, true);
      if (listFRST.isEmpty()) {
        return;
      }
      MessageForm userTK = listFRST.get(0);
      CrInsiteDTO crDTOtoSendSMS = getCrById(Long.parseLong(crDTO.getCrId()));

      List<MessageForm> listUsertToSendSMSTo = new ArrayList<>();

      //<editor-fold defaultstate="collapsed" desc="Gui tin nhan theo luong nghiep vu binh thuong">
      if (actionType.equals(Constants.CR_ACTION_CODE.APPROVE)) {
        if (isLastDepartment) {
          List<Long> listDept = new ArrayList<Long>();
          List<Long> listUser = new ArrayList<Long>();

          //LinhVM: Neu la CR chuan --> Nhan tin cho don vi thuc thi tiep nhan
          if (crDTOtoSendSMS.getCrType().equals(Constants.CR_TYPE.STANDARD.toString())) {
            if (crDTOtoSendSMS.getChangeResponsibleUnit() != null) {
              listDept.add(Long.parseLong(crDTOtoSendSMS.getChangeResponsibleUnit()));
              listUsertToSendSMSTo = getListUsersByDepartment(listDept, true);
            }
            for (MessageForm messageForm : listUsertToSendSMSTo) {
              String content = getMessageForImpBoss(crDTOtoSendSMS, messageForm, userTK);
              messageForm.setMessageContent(content);
              messageForm.setActionCode(Constants.CR_SEARCH_TYPE.EXCUTE);
              messageForm.setSaveNotify(true);
            }
          } else {
            //Con lai: nhan tin cho QLTD kiem tra dau vao
            if (!Constants.CR_TYPE.EMERGENCY.toString().equals(crDTOtoSendSMS.getCrType())
                && crDTOtoSendSMS.getManageUnitId() != null
                && crDTOtoSendSMS.getManageUserId() == null) {
              listDept.add(Long.parseLong(crDTOtoSendSMS.getManageUnitId()));
              listUsertToSendSMSTo = getListUsersByDepartment(listDept, false);
            } else if (!Constants.CR_TYPE.EMERGENCY.toString().equals(crDTOtoSendSMS.getCrType())
                && crDTOtoSendSMS.getManageUnitId() != null
                && crDTOtoSendSMS.getManageUserId() != null) {
              listUser.add(Long.parseLong(crDTOtoSendSMS.getManageUserId()));
              listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
            } else if (!Constants.CR_TYPE.EMERGENCY.toString().equals(crDTOtoSendSMS.getCrType())
                && crDTOtoSendSMS.getManageUnitId() == null
                && crDTOtoSendSMS.getManageUserId() == null) {
              listDept = actionGetListManager(crDTOtoSendSMS, lstScope, true);
              listUsertToSendSMSTo = getListUsersByDepartment(listDept, false);
            }
            for (MessageForm messageForm : listUsertToSendSMSTo) {
              String content = getMessageForVerifyCR(crDTOtoSendSMS, messageForm, userTK);
              messageForm.setMessageContent(content);
              messageForm.setActionCode(Constants.CR_SEARCH_TYPE.VERIFY);
              messageForm.setSaveNotify(true);
            }
          }

          if (crDTOtoSendSMS.getChangeOrginator() != null) {
            List<MessageForm> listUserCreatedCR;
            List<Long> listUserCrCr = new ArrayList<>();
            listUserCrCr.add(Long.parseLong(crDTOtoSendSMS.getChangeOrginator()));
            listUserCreatedCR = getListUsersByUserId(listUserCrCr, false);
            for (MessageForm messForm : listUserCreatedCR) {
              messForm.setMessageContent(
                  getMessageForCreatorWhenApproveAndChangeToVerify(crDTO, messForm, userTK));
              messForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
              messForm.setSaveNotify(false);
            }
            if (!listUserCreatedCR.isEmpty()) {
              listUsertToSendSMSTo.addAll(listUserCreatedCR);
            }
          }

        } else {
          CrApprovalDepartmentInsiteDTO currentCrApprovalDepartmentDTO
              = getCurrentCrApprovalDepartmentDTO(
              Long.parseLong(crDTO.getCrId()),
              Long.parseLong(crDTO.getUserLogin()),
              Long.parseLong(crDTO.getUserLoginUnit()));
          if (isLastDeptOfApproveLevel(
              Long.parseLong(currentCrApprovalDepartmentDTO.getCadtLevel()),
              Long.parseLong(currentCrApprovalDepartmentDTO.getCadtId()),
              Long.parseLong(currentCrApprovalDepartmentDTO.getCrId()))) {
            List<CrApprovalDepartmentInsiteDTO> lst
                = getNextApprovalDepartment(
                Long.parseLong(currentCrApprovalDepartmentDTO.getCrId()),
                Long.parseLong(currentCrApprovalDepartmentDTO.getCadtLevel()),
                true);
//                        List<Long> listDeptId = new ArrayList<Long>();
            //20160531 daitt1 sua chuc ang nhan tin
            for (CrApprovalDepartmentInsiteDTO crApprovalDepartmentDTO : lst) {
              List<Long> listDptTmp = new ArrayList<Long>();
              listDptTmp.add(Long.parseLong(crApprovalDepartmentDTO.getUnitId()));
              List<MessageForm> lstTmp = getListUsersByDepartment(listDptTmp, true);
              if (lstTmp.isEmpty()) {
                lstTmp = getListHigherBoss(Long.parseLong(crApprovalDepartmentDTO.getUnitId()));
              }
              if (!lstTmp.isEmpty()) {
                listUsertToSendSMSTo.addAll(lstTmp);
              }
            }
//                        listUsertToSendSMSTo
//                                = getListUsersByDepartment(session,
//                                        listDeptId, true);
            //20160531 daitt1 sua chuc ang nhan tin
            for (MessageForm messageForm : listUsertToSendSMSTo) {
              String content = getMessageForApprove(crDTOtoSendSMS, messageForm, userTK);
              messageForm.setMessageContent(content);
              messageForm.setActionCode(Constants.CR_SEARCH_TYPE.APPROVE);
              messageForm.setSaveNotify(true);
            }

          }
          if (crDTOtoSendSMS.getChangeOrginator() != null) {
            List<MessageForm> listUserCreatedCR;
            List<Long> listUserCrCr = new ArrayList<>();
            listUserCrCr.add(Long.parseLong(crDTOtoSendSMS.getChangeOrginator()));
            listUserCreatedCR = getListUsersByUserId(listUserCrCr, false);
            for (MessageForm messForm : listUserCreatedCR) {
              if (crDTO.getCrNumber() == null
                  || "".equals(crDTO.getCrNumber().trim())
                  || "null".equals(crDTO.getCrNumber().trim().toLowerCase())) {
                crDTO.setCrNumber(crDTOtoSendSMS.getCrNumber());
              }
              if (crDTO.getTitle() == null
                  || "".equals(crDTO.getTitle().trim())
                  || "null".equals(crDTO.getTitle().trim().toLowerCase())) {
                crDTO.setTitle(crDTOtoSendSMS.getTitle());
              }
              messForm.setMessageContent(getMessageForCreatorWhenApprove(crDTO, messForm, userTK));
              messForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
              messForm.setSaveNotify(false);
            }
            if (!listUserCreatedCR.isEmpty()) {
              listUsertToSendSMSTo.addAll(listUserCreatedCR);
            }
          }
        }
      } else if (actionType.equals(Constants.CR_ACTION_CODE.REJECT)) {
        List<Long> listUser = new ArrayList<Long>();
        if (crDTOtoSendSMS.getChangeOrginator() != null) {
          listUser.add(Long.parseLong(crDTOtoSendSMS.getChangeOrginator()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageForCreatorWhenRejected(crDTOtoSendSMS, messageForm, userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
          messageForm.setSaveNotify(false);
        }
      }
//</editor-fold>
      //R941966_quangdx_start
      List<UserReceiveMsgEntity> lstReceiveMsg = getLstConfigReceiveSmS(crDTOtoSendSMS.getCrId());
      if (lstReceiveMsg != null && !lstReceiveMsg.isEmpty() && listUsertToSendSMSTo != null
          && !listUsertToSendSMSTo.isEmpty()) {
        List<Long> listUser = new ArrayList<Long>();
        for (UserReceiveMsgEntity user : lstReceiveMsg) {
          listUser.add(user.getUserId());
        }
        List<MessageForm> listTemp = getListUsersByUserId(listUser, false);
        if (listTemp != null && !listTemp.isEmpty()) {
          for (MessageForm messageForm : listTemp) {
            messageForm.setActionCode(listUsertToSendSMSTo.get(0).getActionCode());
            messageForm.setSaveNotify(false);
            messageForm.setMessageContent(listUsertToSendSMSTo.get(0).getMessageContent());
            listUsertToSendSMSTo.add(messageForm);
          }
        }
      }
      //R941966_quangdx_end

      //<editor-fold defaultstate="collapsed" desc="Gui tin nhan cho cac don vi ben duoi neu don vi cao hon duyet CR khong qua cac don vi nay">
      if (listLowerLevelDept != null && !listLowerLevelDept.isEmpty()) {
        List<Long> listDepartment = new ArrayList<Long>();
        for (CrApprovalDepartmentInsiteDTO crApprovalDeparmentDTO : listLowerLevelDept) {
          String deptIdUnitString = crApprovalDeparmentDTO.getUnitId();
          Long deptId = Long.valueOf(deptIdUnitString);
          listDepartment.add(deptId);
        }
        List<MessageForm> listMess = getListUsersByDepartment(listDepartment, true);
        for (MessageForm messageForm : listMess) {
          messageForm.setMessageContent(getMessageTextToSendToLowerLevelDeptManager(
              crDTO, userTK, messageForm, actionType));
        }
        if (!listMess.isEmpty()) {
          listUsertToSendSMSTo.addAll(listMess);
        }
      }
      //</editor-fold>
      for (MessageForm messageForm : listUsertToSendSMSTo) {
        actionInsertIntoMess(messageForm);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }


  public String getMessageTextToSendToLowerLevelDeptManager(CrInsiteDTO crDTO,
      MessageForm userApprove, MessageForm lowerLevelManagerUser, Long actionType) {
    String mess = "";
    Locale locale = getLocale(lowerLevelManagerUser);
    mess += "[GNOC-CR] user: ";
    mess += userApprove.getUserName() + " ";
    if (actionType.equals(Constants.CR_ACTION_CODE.REJECT)) {
      mess += I18n.getLanguageByLocale(locale,
          "cr.mess.to.send.to.lower.department.disapproved");
    } else {
      mess += I18n
          .getLanguageByLocale(locale, "cr.mess.to.send.to.lower.department.approved");
    }
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  @Override
  public void actionSendSms(Long actionType, CrInsiteDTO crDTO, CrInsiteDTO crDTOtoSendSMS) {
    try {
      if (actionType == null || crDTO == null) {
        return;
      }
      if (crDTO.getCrNumber() == null || crDTO.getCrNumber().trim().isEmpty()) {
        if (crDTO.getCrId() != null) {
          Long id = this.objectToLong(crDTO.getCrId());
          if (id != null) {
            CrInsiteDTO crDTOnew = getCrInfoById(id, null, null);
            if (crDTOnew != null) {
              crDTO.setCrNumber(crDTOnew.getCrNumber());
              crDTO.setTitle(crDTOnew.getTitle());
            }
          }
        }

      }

      List<MessageForm> lstScope
          = actionGetAllUnitOfScope();
      Long userIdTosendMS = Long.parseLong(crDTO.getUserLogin());
      List<Long> lstFlong = new ArrayList<Long>();
      lstFlong.add(userIdTosendMS);
      List<MessageForm> listFRST = getListUsersByUserId(lstFlong, true);
      if (listFRST.isEmpty()) {
        return;
      }
      MessageForm userTK = listFRST.get(0);
//      CrInsiteDTO crDTOtoSendSMS = getCrById(Long.parseLong(crDTO.getCrId()));

      List<MessageForm> listUsertToSendSMSTo = new ArrayList<MessageForm>();
      if (Constants.CR_ACTION_CODE.ADDNEW.equals(actionType)) {
//                //log.info("====ADNEEEW====");
        List<Long> listUser = new ArrayList<Long>();
        if (StringUtils.isNotNullOrEmpty(crDTO.getUserLogin())) {
          listUser.add(Long.parseLong(crDTO.getUserLogin()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
          for (MessageForm messageForm : listUsertToSendSMSTo) {
            messageForm.setActionCode(Constants.CR_SEARCH_TYPE.CREATE_EDIT);
            messageForm.setSaveNotify(false);
          }
        }
      } else if (Constants.CR_ACTION_CODE.UPDATE.equals(actionType)) {
        List<Long> listUser = new ArrayList<Long>();
        if (StringUtils.isNotNullOrEmpty(crDTO.getUserLogin())) {
          listUser.add(Long.parseLong(crDTO.getUserLogin()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.CREATE_EDIT);
          messageForm.setSaveNotify(false);
        }
      } else if (Constants.CR_ACTION_CODE.APPROVE.equals(actionType)) {
        if (crDBRepository.isLastDepartment(Long.parseLong(crDTO.getCrId()))) {
//                    //log.info("==@1#@%!#%=== LastDepartment");
          List<Long> listDept = new ArrayList<Long>();
          List<Long> listUser = new ArrayList<Long>();

          //LinhVM: Neu la CR chuan --> Nhan tin cho don vi thuc thi tiep nhan
          if (crDTOtoSendSMS.getCrType().equals(Constants.CR_TYPE.STANDARD.toString())) {
            if (crDTOtoSendSMS.getChangeResponsibleUnit() != null) {
              listDept.add(Long.parseLong(crDTOtoSendSMS.getChangeResponsibleUnit()));
              listUsertToSendSMSTo = getListUsersByDepartment(listDept, true);
            }
            for (MessageForm messageForm : listUsertToSendSMSTo) {
              String content = getMessageForImpBoss(crDTOtoSendSMS, messageForm, userTK);
              messageForm.setMessageContent(content);
              messageForm.setActionCode(Constants.CR_SEARCH_TYPE.EXCUTE);
              messageForm.setSaveNotify(true);
            }
          } else {
            //Con lai: nhan tin cho QLTD kiem tra dau vao
            if (!Constants.CR_TYPE.EMERGENCY.toString().equals(crDTOtoSendSMS.getCrType())
                && crDTOtoSendSMS.getManageUnitId() != null
                && crDTOtoSendSMS.getManageUserId() == null) {
              listDept.add(Long.parseLong(crDTOtoSendSMS.getManageUnitId()));
              listUsertToSendSMSTo = getListUsersByDepartment(listDept, false);
//                        //log.info("==@1#@%!#%=== LastDepartment one");
            } else if (!Constants.CR_TYPE.EMERGENCY.toString().equals(crDTOtoSendSMS.getCrType())
                && crDTOtoSendSMS.getManageUnitId() != null
                && crDTOtoSendSMS.getManageUserId() != null) {
              listUser.add(Long.parseLong(crDTOtoSendSMS.getManageUserId()));
              listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
//                        //log.info("==@1#@%!#%=== LastDepartment two");
            } else if (!Constants.CR_TYPE.EMERGENCY.toString().equals(crDTOtoSendSMS.getCrType())
                && crDTOtoSendSMS.getManageUnitId() == null
                && crDTOtoSendSMS.getManageUserId() == null) {
              listDept = actionGetListManager(
                  crDTOtoSendSMS, lstScope, true);
              listUsertToSendSMSTo = getListUsersByDepartment(listDept, false);
//                        //log.info("==@1#@%!#%=== LastDepartment three");
            }
//                    //log.info("==@1#@%!#%=== LastDepartment listsize" + listUsertToSendSMSTo.size());
            for (MessageForm messageForm : listUsertToSendSMSTo) {
              String content = getMessageForVerifyCR(crDTOtoSendSMS, messageForm, userTK);
              messageForm.setMessageContent(content);
              messageForm.setActionCode(Constants.CR_SEARCH_TYPE.VERIFY);
              messageForm.setSaveNotify(true);
            }
          }

          //Nhan cho user tao CR
          if (crDTOtoSendSMS.getChangeOrginator() != null) {
            List<MessageForm> listUserCreatedCR;
            List<Long> listUserCrCr = new ArrayList<>();
            listUserCrCr.add(Long.parseLong(crDTOtoSendSMS.getChangeOrginator()));
            listUserCreatedCR = getListUsersByUserId(listUserCrCr, false);
            for (MessageForm messForm : listUserCreatedCR) {
              messForm.setMessageContent(
                  getMessageForCreatorWhenApproveAndChangeToVerify(crDTO, messForm, userTK));
              messForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
              messForm.setSaveNotify(false);
            }
            if (!listUserCreatedCR.isEmpty()) {
              listUsertToSendSMSTo.addAll(listUserCreatedCR);
            }
          }

        } else {
          CrApprovalDepartmentInsiteDTO currentCrApprovalDepartmentDTO
              = getCurrentCrApprovalDepartmentDTO(
              Long.parseLong(crDTO.getCrId()),
              Long.parseLong(crDTO.getUserLogin()),
              Long.parseLong(crDTO.getUserLoginUnit()));
          if (currentCrApprovalDepartmentDTO != null) {
            log.debug("===asasd" + currentCrApprovalDepartmentDTO.getCadtLevel());
            log.debug("===asasd" + currentCrApprovalDepartmentDTO.getCadtId());
            log.debug("===asasd" + currentCrApprovalDepartmentDTO.getCrId());
          }
          if (isLastDeptOfApproveLevel(
              Long.parseLong(currentCrApprovalDepartmentDTO.getCadtLevel()),
              Long.parseLong(currentCrApprovalDepartmentDTO.getCadtId()),
              Long.parseLong(currentCrApprovalDepartmentDTO.getCrId()))) {
            List<CrApprovalDepartmentInsiteDTO> lst
                = getNextApprovalDepartment(
                Long.parseLong(currentCrApprovalDepartmentDTO.getCrId()),
                Long.parseLong(currentCrApprovalDepartmentDTO.getCadtLevel()),
                true);
            List<Long> listDeptId = new ArrayList<Long>();
            for (CrApprovalDepartmentInsiteDTO crApprovalDepartmentDTO : lst) {
              listDeptId.add(Long.parseLong(crApprovalDepartmentDTO.getUnitId()));
            }
            listUsertToSendSMSTo
                = getListUsersByDepartment(
                listDeptId, true);
            for (MessageForm messageForm : listUsertToSendSMSTo) {
              String content = getMessageForApprove(crDTOtoSendSMS, messageForm, userTK);
              messageForm.setMessageContent(content);
              messageForm.setActionCode(Constants.CR_SEARCH_TYPE.APPROVE);
              messageForm.setSaveNotify(true);
            }

          }
          if (crDTOtoSendSMS.getChangeOrginator() != null) {
            List<MessageForm> listUserCreatedCR;
            List<Long> listUserCrCr = new ArrayList<>();
            listUserCrCr.add(Long.parseLong(crDTOtoSendSMS.getChangeOrginator()));
            listUserCreatedCR = getListUsersByUserId(listUserCrCr, false);
            for (MessageForm messForm : listUserCreatedCR) {
              if (crDTO.getCrNumber() == null
                  || "".equals(crDTO.getCrNumber().trim())
                  || "null".equals(crDTO.getCrNumber().trim().toLowerCase())) {
                crDTO.setCrNumber(crDTOtoSendSMS.getCrNumber());
              }
              if (crDTO.getTitle() == null
                  || "".equals(crDTO.getTitle().trim())
                  || "null".equals(crDTO.getTitle().trim().toLowerCase())) {
                crDTO.setTitle(crDTOtoSendSMS.getTitle());
              }
              messForm.setMessageContent(getMessageForCreatorWhenApprove(crDTO, messForm, userTK));
              messForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
              messForm.setSaveNotify(false);
            }
            if (!listUserCreatedCR.isEmpty()) {
              listUsertToSendSMSTo.addAll(listUserCreatedCR);
            }
          }
        }

      } else if (Constants.CR_ACTION_CODE.REJECT.equals(actionType)) {
        List<Long> listUser = new ArrayList<Long>();
        if (crDTOtoSendSMS.getChangeOrginator() != null) {
          listUser.add(Long.parseLong(crDTOtoSendSMS.getChangeOrginator()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageForCreatorWhenRejected(crDTOtoSendSMS, messageForm, userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
          messageForm.setSaveNotify(false);
        }
      } else if (Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER.equals(actionType)) {
        List<Long> listUser = new ArrayList<Long>();
        if (crDTOtoSendSMS.getChangeOrginator() != null) {
          listUser.add(Long.parseLong(crDTOtoSendSMS.getChangeOrginator()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageForCreatorWhenManagerIncompleteCR(crDTOtoSendSMS, messageForm,
              userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
          messageForm.setSaveNotify(false);
        }
      } else if (Constants.CR_ACTION_CODE.CLOSE_BY_MANAGER.equals(actionType)) {
        List<Long> listUser = new ArrayList<Long>();
        if (crDTOtoSendSMS.getChangeOrginator() != null) {
          listUser.add(Long.parseLong(crDTOtoSendSMS.getChangeOrginator()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageForCreatorWhenManagerVerifyAndCloseIt(crDTOtoSendSMS,
              messageForm, userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
          messageForm.setSaveNotify(false);
        }
      } else if (Constants.CR_ACTION_CODE.ASSIGN_TO_CONSIDER.equals(actionType)) {
        List<Long> listDept = new ArrayList<Long>();
        if (crDTO.getConsiderUnitId() != null) {
          listDept.add(Long.parseLong(crDTO.getConsiderUnitId()));
          // tiennv nang cap:  Giao thẩm định thì tất cả các nhân viên trong đơn vị đều nhận đc SMS
//          listUsertToSendSMSTo = getListUsersByDepartment(listDept, true);
          listUsertToSendSMSTo = getListUsersByDepartment(listDept, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageForAppraiserBoss(crDTOtoSendSMS, messageForm, userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.CONSIDER);
          messageForm.setSaveNotify(true);
        }
      } else if (Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_APPRAISER.equals(actionType)) {
        List<Long> listUser = new ArrayList<Long>();
        if (crDTOtoSendSMS.getChangeOrginator() != null) {
          listUser.add(Long.parseLong(crDTOtoSendSMS.getChangeOrginator()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageForCreatorWhenManagerVerifyAndCloseIt(crDTOtoSendSMS,
              messageForm, userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
          messageForm.setSaveNotify(false);
        }
      } else if (Constants.CR_ACTION_CODE.RETURN_TO_MANAGER_BY_APPRAISER.equals(actionType)) {
        List<Long> listUser = new ArrayList<Long>();
        if (crDTOtoSendSMS.getManageUnitId() != null) {//tiennv fix lai before: getManageUserId
          listUser.add(Long.parseLong(crDTOtoSendSMS.getManageUnitId()));
          listUsertToSendSMSTo = getListUsersByDepartment(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageForManagerWhenAppraiserReject(crDTOtoSendSMS, messageForm,
              userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
          messageForm.setSaveNotify(false);
        }
      } else if (Constants.CR_ACTION_CODE.ASSIGN_TO_EMPLOYEE_APPRAISAL.equals(actionType)) {
        List<Long> listUser = new ArrayList<Long>();
        if (crDTO.getAssignUserId() != null) {
          listUser.add(Long.parseLong(crDTO.getAssignUserId()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageForAppraiserEmployee(crDTOtoSendSMS, messageForm, userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
          messageForm.setSaveNotify(false);
        }
      } else if (Constants.CR_ACTION_CODE.APPRAISE.equals(actionType)) {
        List<Long> listDept = new ArrayList<Long>();
        List<Long> listUser = new ArrayList<Long>();
        if (crDTOtoSendSMS.getManageUnitId() != null
            && crDTOtoSendSMS.getManageUserId() == null) {
          listDept.add(Long.parseLong(crDTOtoSendSMS.getManageUnitId()));
          listUsertToSendSMSTo = getListUsersByDepartment(listDept, false);
        } else if (crDTOtoSendSMS.getManageUnitId() != null
            && crDTOtoSendSMS.getManageUserId() != null) {
          listUser.add(Long.parseLong(crDTOtoSendSMS.getManageUserId()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
        } else if (crDTOtoSendSMS.getManageUnitId() == null
            && crDTOtoSendSMS.getManageUserId() == null) {
          listDept = actionGetListManager(
              crDTOtoSendSMS, lstScope, false);
          listUsertToSendSMSTo = getListUsersByDepartment(listDept, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageForScheduler(crDTOtoSendSMS, messageForm, userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.SCHEDULE);
          messageForm.setSaveNotify(true);
        }
        if (crDTOtoSendSMS.getChangeOrginator() != null) {
          List<MessageForm> listUserCreatedCR;
          List<Long> listUserCrCr = new ArrayList<>();
          listUserCrCr.add(Long.parseLong(crDTOtoSendSMS.getChangeOrginator()));
          listUserCreatedCR = getListUsersByUserId(listUserCrCr, false);
          for (MessageForm messForm : listUserCreatedCR) {
            messForm.setMessageContent(getMessageForCreatorWhenAppraise(crDTO, messForm, userTK));
            messForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
            messForm.setSaveNotify(false);
          }
          if (!listUserCreatedCR.isEmpty()) {
            listUsertToSendSMSTo.addAll(listUserCreatedCR);
          }
        }
      } else if (Constants.CR_ACTION_CODE.CLOSE_BY_APPRAISER.equals(actionType)) {
        List<Long> listUser = new ArrayList<Long>();
        if (crDTOtoSendSMS.getChangeOrginator() != null) {
          listUser.add(Long.parseLong(crDTOtoSendSMS.getChangeOrginator()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageForCreatorWhenManagerVerifyAndCloseIt(crDTOtoSendSMS,
              messageForm, userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
          messageForm.setSaveNotify(false);
        }
      } else if (Constants.CR_ACTION_CODE.SCHEDULE.equals(actionType)) {
        List<Long> listDept = new ArrayList<Long>();
        if (crDTOtoSendSMS.getChangeResponsibleUnit() != null) {
          listDept.add(Long.parseLong(crDTOtoSendSMS.getChangeResponsibleUnit()));
          listUsertToSendSMSTo = getListUsersByDepartment(listDept, true);
          //20160531-daitt1: bo sung chuc nang tim lanh dao don vi cha
          if (listUsertToSendSMSTo.isEmpty()) {
            listUsertToSendSMSTo = getListHigherBoss(
                Long.parseLong(crDTOtoSendSMS.getChangeResponsibleUnit()));
          }
          //20160531-daitt1: bo sung chuc nang tim lanh dao don vi cha
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageForImpBoss(crDTOtoSendSMS, messageForm, userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.EXCUTE);
          messageForm.setSaveNotify(true);
        }
        if (crDTOtoSendSMS.getChangeOrginator() != null) {
          List<MessageForm> listUserCreatedCR;
          List<Long> listUserCrCr = new ArrayList<>();
          listUserCrCr.add(Long.parseLong(crDTOtoSendSMS.getChangeOrginator()));
          listUserCreatedCR = getListUsersByUserId(listUserCrCr, false);
          for (MessageForm messForm : listUserCreatedCR) {
            messForm.setMessageContent(getMessageForCreatorWhenSchedule(crDTO, messForm, userTK));
            messForm.setActionCode(Constants.CR_ACTION_CODE.SCHEDULE);
            messForm.setSaveNotify(false);
          }
          if (!listUserCreatedCR.isEmpty()) {
            listUsertToSendSMSTo.addAll(listUserCreatedCR);
          }
        }

      } else if (Constants.CR_ACTION_CODE.RETURN_TO_APPRAISE_BY_MANAGER_SCH.equals(actionType)) {
        List<Long> listUser = new ArrayList<>();
        if (crDTOtoSendSMS.getConsiderUserId()
            != null) {//tiennv sua lai cho nay crDTOsendSMS -> crDTO truoc khi gui
          listUser.add(Long.parseLong(crDTOtoSendSMS.getConsiderUserId()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageIncaseOfReturnToAppraiserByManager(crDTOtoSendSMS, messageForm,
              userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
          messageForm.setSaveNotify(false);
        }
      } else if (Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER_SCH.equals(actionType)) {
        List<Long> listUser = new ArrayList<Long>();
        if (crDTOtoSendSMS.getChangeOrginator()
            != null) {//tiennv sua lai cho nay crDTOsendSMS -> crDTO truoc khi gui
          listUser.add(Long.parseLong(crDTOtoSendSMS.getChangeOrginator()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageForCreatorWhenManagerIncompleteCR(crDTOtoSendSMS, messageForm,
              userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
          messageForm.setSaveNotify(false);
        }
      } else if (Constants.CR_ACTION_CODE.CLOSE_BY_MANAGER_SCH.equals(actionType)) {
        List<Long> listUser = new ArrayList<Long>();
        if (crDTOtoSendSMS.getChangeOrginator() != null) {
          listUser.add(Long.parseLong(crDTOtoSendSMS.getChangeOrginator()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageForCreatorWhenManagerVerifyAndCloseIt(crDTOtoSendSMS,
              messageForm, userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
          messageForm.setSaveNotify(false);
        }
      } else if (Constants.CR_ACTION_CODE.RETURN_TO_APPRAISER_BY_IMPL.equals(actionType)) {
        List<Long> listUser = new ArrayList<Long>();
        if (crDTOtoSendSMS.getConsiderUserId() != null) {     //cau sql dang ko tra ve truong nay
          listUser.add(Long.parseLong(crDTOtoSendSMS.getConsiderUserId()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageIncaseOfReturnToAppraiserByImpl(crDTOtoSendSMS, messageForm,
              userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
          messageForm.setSaveNotify(false);
        }
      } else if (Constants.CR_ACTION_CODE.RETURN_TO_MANAGER_BY_IMPL.equals(actionType)) {
        List<Long> listUser = new ArrayList<Long>();
        if (crDTOtoSendSMS.getManageUnitId() != null) {
          listUser.add(Long.parseLong(crDTOtoSendSMS.getManageUnitId()));
          listUsertToSendSMSTo = getListUsersByDepartment(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageIncaseOfReturnToManagerByImpl(crDTOtoSendSMS, messageForm,
              userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
          messageForm.setSaveNotify(false);
        }
      } else if (Constants.CR_ACTION_CODE.ASSIGN_EXC_TO_EMPLOYEE.equals(actionType)) {
        List<Long> listUser = new ArrayList<Long>();
        if (StringUtils.isNotNullOrEmpty(crDTO.getAssignUserId())) {
          listUser.add(Long.parseLong(crDTO.getAssignUserId()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageIncaseOfAssignTask(crDTOtoSendSMS, messageForm, userTK);
          messageForm.setMessageContent(content);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.EXCUTE);
          messageForm.setSaveNotify(true);
        }
      } else if (Constants.CR_ACTION_CODE.ACCEPT.equals(actionType)) {
        List<Long> listUser = new ArrayList<Long>();
        if (StringUtils.isNotNullOrEmpty(crDTO.getUserLogin())) {
          listUser.add(Long.parseLong(crDTO.getUserLogin()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.LOOKUP);
          messageForm.setSaveNotify(false);
        }
      } else if (Constants.CR_ACTION_CODE.RESOLVE.equals(actionType)) {
        List<Long> listDept = new ArrayList<Long>();
        if (StringUtils.isNotNullOrEmpty(crDTOtoSendSMS.getManageUnitId())) {
          //LinhVM: CR chuan --> chi nhan tin cho lanh dao phong.
          if (Constants.CR_TYPE.STANDARD.toString().equals(crDTOtoSendSMS.getCrType())) {
            listDept.add(Long.parseLong(crDTOtoSendSMS.getManageUnitId()));
            listUsertToSendSMSTo = getListUsersByDepartment(listDept, true);
            //20160531-daitt1: bo sung chuc nang tim lanh dao don vi cha
            if (listUsertToSendSMSTo.isEmpty()) {
              listUsertToSendSMSTo = getListHigherBoss(
                  Long.parseLong(crDTOtoSendSMS.getChangeResponsibleUnit()));
            }
            //20160531-daitt1: bo sung chuc nang tim lanh dao don vi cha
          } else {
            //Nhan cho tat ca nhan vien trong don vi QLTD
            listDept.add(Long.parseLong(crDTOtoSendSMS.getManageUnitId()));
            listUsertToSendSMSTo = getListUsersByDepartment(listDept, false);
          }

        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageForManagerToClose(crDTOtoSendSMS, messageForm, userTK);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.CLOSE);
          messageForm.setSaveNotify(true);
          messageForm.setMessageContent(content);
        }
      } else if (Constants.CR_ACTION_CODE.INCOMPLETE_APPROVE_STD.equals(actionType)) {

      } else if (Constants.CR_ACTION_CODE.ASSIGN_TO_CAB.equals(actionType)) {
        List<Long> listUser = new ArrayList<Long>();
        if (crDTO.getUserCab() != null) {
          listUser.add(Long.parseLong(crDTO.getUserCab()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageIncaseOfAssignCabCR(crDTOtoSendSMS, messageForm, userTK);
          messageForm.setActionCode(Constants.CR_SEARCH_TYPE.CAB);
          messageForm.setSaveNotify(false);
          messageForm.setMessageContent(content);
        }
      } else if (Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER_CAB.equals(actionType) ||
          Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_WHEN_CAB.equals(
              actionType)) { //tiennv bo sung gui sms khi tra ve don vi tao khi cab, cho giao cab
        List<Long> listUser = new ArrayList<Long>();
        if (crDTOtoSendSMS.getChangeOrginator() != null) {
          listUser.add(Long.parseLong(crDTOtoSendSMS.getChangeOrginator()));
          listUsertToSendSMSTo = getListUsersByUserId(listUser, false);
        }
        for (MessageForm messageForm : listUsertToSendSMSTo) {
          String content = getMessageForCreatorWhenManagerIncompleteCR(crDTOtoSendSMS, messageForm,
              userTK);
          messageForm.setMessageContent(content);
          if (Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER_CAB.equals(actionType)) {
            messageForm.setActionCode(CR_SEARCH_TYPE.WAIT_CAB);
          } else {
            messageForm.setActionCode(Constants.CR_SEARCH_TYPE.CAB);
          }
          messageForm.setSaveNotify(false);
        }
      }
      //R941966_quangdx_start
      List<UserReceiveMsgEntity> lstReceiveMsg = getLstConfigReceiveSmS(crDTOtoSendSMS.getCrId());
      if (lstReceiveMsg != null && !lstReceiveMsg.isEmpty() && listUsertToSendSMSTo != null
          && !listUsertToSendSMSTo.isEmpty()) {
        List<Long> listUser = new ArrayList<>();
        for (UserReceiveMsgEntity user : lstReceiveMsg) {
          listUser.add(user.getUserId());
        }
        List<MessageForm> listTemp = getListUsersByUserId(listUser, false);
        if (listTemp != null && !listTemp.isEmpty()) {
          for (MessageForm messageForm : listTemp) {
            messageForm.setActionCode(listUsertToSendSMSTo.get(0).getActionCode());
            messageForm.setSaveNotify(false);
            messageForm.setMessageContent(listUsertToSendSMSTo.get(0).getMessageContent());
            listUsertToSendSMSTo.add(messageForm);
          }

        }
      }
      //R941966_quangdx_end

      for (MessageForm messageForm : listUsertToSendSMSTo) {
        actionInsertIntoMess(messageForm);
        if (messageForm.isSaveNotify()) {
          actionInsertIntoNotification(messageForm, crDTO);
        }
      }
      getEntityManager().flush();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public String actionUpdateNotify(CrInsiteDTO crDTO, Long actionCode) {
    try {
      StringBuilder sql = new StringBuilder("");
      sql.append(" update common_gnoc.gnoc_notification set gnn_state = :state_read ");
      sql.append(" where gnn_system =  :system_cr ");
      sql.append(" and GNN_OBJECT_ID = :cr_id ");
      sql.append(" and GNN_ACTION_ID =  :action_code ");
      sql.append(" and GNN_USER_ID =  :user_login ");
      Map<String, Object> params = new HashMap<>();
      params.put("state_read", Constants.NOTIFY_STATE.READ);
      params.put("system_cr", Constants.SYSTEM.CR);
      params.put("cr_id", Long.valueOf(crDTO.getCrId()));
      params.put("action_code", actionCode);
      params.put("user_login", crDTO.getUserLogin());

      getNamedParameterJdbcTemplate().update(sql.toString(), params);
      return RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return RESULT.ERROR;
  }

  public String getMessageForCreatorWhenApproveAndChangeToVerify(CrInsiteDTO crDTO,
      MessageForm messageForm, MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] Cr: ";
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ") ";

    mess += I18n.getLanguageByLocale(locale, "CR.he.approve.and.change.status.2");
    mess += " " + messageFormTK2.getUserName();

    return mess;
  }

  public String getMessageForCreatorWhenApprove(CrInsiteDTO crDTO, MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] user: ";
    mess += messageFormTK2.getUserName() + " ";
    mess += I18n.getLanguageByLocale(locale, "CR.he.approve.it");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }


  public String getMessageForCreatorWhenManagerIncompleteCR(CrInsiteDTO crDTO,
      MessageForm messageForm, MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] user: ";
    mess += messageFormTK2.getUserName() + " ";
    mess += I18n.getLanguageByLocale(locale, "CR.manager.incomplte");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  /**
   *
   */
  public String getMessageForCreatorWhenManagerVerifyAndCloseIt(CrInsiteDTO crDTO,
      MessageForm messageForm, MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] user: ";
    mess += messageFormTK2.getUserName() + " ";
    mess += I18n.getLanguageByLocale(locale, "CR.manger.close.it");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  /**
   *
   */
  public String getMessageForNotification(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] ";
    mess += I18n.getLanguageByLocale(locale, "CR.TO.APPROVE");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  /**
   *
   */
  public String getTitleForNotification(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] ";
    mess += I18n.getLanguageByLocale(locale, "CR.TO.APPROVE");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  /**
   *
   */
  public String getMessageForCreator(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] ";
    mess += I18n.getLanguageByLocale(locale, "CR.I.Create");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  /**
   *
   */
  public String getMessageForUpdater(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] ";
    mess += I18n.getLanguageByLocale(locale, "CR.I.Update");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  /**
   *
   */
  public String getMessageForCreatorWhenRejected(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] user: ";
    mess += messageFormTK2.getUserName() + " ";
    mess += I18n.getLanguageByLocale(locale, "CR.ofMine.was.reject");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  /**
   *
   */
  public String getMessageForManagerVerifyAndCloseCR(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] user: ";
    mess += messageFormTK2.getUserName() + " ";
    mess += I18n.getLanguageByLocale(locale, "CR.i.manager.close");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  /**
   *
   */
  public String getMessageForAppraiserBoss(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] user: ";
    mess += messageFormTK2.getUserName() + " ";
    mess += I18n.getLanguageByLocale(locale, "CR.boss.appraise");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  /**
   *
   */
  public String getMessageForAppraiserEmployee(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] user ";
    mess += messageFormTK2.getUserName() + " ";
    mess += I18n.getLanguageByLocale(locale, "CR.i.recieve.appraise");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  /**
   *
   */
  public String getMessageForScheduler(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR]: ";
    mess += I18n.getLanguageByLocale(locale, "CR.i.need.schedule");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }


  /**
   *
   */
  public String getMessageIncaseOfReturnToAppraiserByManager(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] user: ";
    mess += messageFormTK2.getUserName() + " ";
    mess += I18n.getLanguageByLocale(locale, "CR.mang.return.to.Appraise");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  /**
   *
   */
  public String getMessageIncaseOfReturnToAppraiserByImpl(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] user: ";
    mess += messageFormTK2.getUserName() + " ";
    mess += I18n.getLanguageByLocale(locale, "CR.impl.return.to.Appraise");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  /**
   *
   */
  public String getMessageIncaseOfReturnToManagerByImpl(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] user: ";
    mess += messageFormTK2.getUserName() + " ";
    mess += I18n.getLanguageByLocale(locale, "CR.impl.return.to.Appraise");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  /**
   *
   */
  public String getMessageForImpImployee(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR]";
    mess += I18n.getLanguageByLocale(locale, "CR.employee.implement");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  /**
   *
   */
  public String getMessageForManagerToClose(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR]";
    mess += I18n.getLanguageByLocale(locale, "CR.i.manager.close");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  /**
   *
   */
  public String getMessageForManagerWhenAppraiserReject(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] user: ";
    mess += messageFormTK2.getUserName() + " ";
    mess += I18n.getLanguageByLocale(locale, "CR.i.appaiser.return.to.manager");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  /**
   *
   */
  public String getMessageIncaseOfAssignTask(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] user: ";
    mess += messageFormTK2.getUserName() + " ";
    mess += I18n.getLanguageByLocale(locale, "CR.manager.assg.to.me");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  public String getMessageForCreatorWhenSchedule(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] user: ";
    mess += messageFormTK2.getUserName() + " ";
    mess += I18n.getLanguageByLocale(locale, "CR.he.schedule.it");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  public String getMessageIncaseOfAssignCabCR(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] ";
    mess += I18n.getLanguageByLocale(locale, "CR.i.assign.cab");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  public String getMessageForCreatorWhenAppraise(CrInsiteDTO crDTO,
      MessageForm messageForm,
      MessageForm messageFormTK2) {
    String mess = "";
    Locale locale = getLocale(messageForm);
    mess += "[GNOC-CR] user: ";
    mess += messageFormTK2.getUserName() + " ";
    mess += I18n.getLanguageByLocale(locale, "CR.he.appraise.it");
    mess += " " + crDTO.getCrNumber();
    mess += "(" + crDTO.getTitle() + ")";
    return mess;
  }

  public Long objectToLong(Object o) {
    try {
      return Long.parseLong(o.toString());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public String sendSMSToLstUserConfig(String crId, String contentType) {
    String result = "SUCCESS";
    try {
      List<UserReceiveMsgEntity> lstReceiveMsg = getLstConfigReceiveSmS(crId);
      if (lstReceiveMsg != null && !lstReceiveMsg.isEmpty()) {
        List<Long> lstUserIdReceiveMsg = new ArrayList<Long>();
        for (UserReceiveMsgEntity user : lstReceiveMsg) {
          lstUserIdReceiveMsg.add(user.getUserId());
        }
        List<MessageForm> lst = getListUsersByUserId(lstUserIdReceiveMsg, true);
        if (lst != null && !lst.isEmpty()) {
          for (MessageForm msg : lst) {
            Locale locale = getLocale(msg);
            String content = getMsgContentUserConfCR(locale.getLanguage(),
                lstReceiveMsg.get(0).getCrNumber());
            msg.setMessageContent(content);
            actionInsertIntoMess(msg);
          }
        }
      }
    } catch (Exception e) {
      result = "FAIL";
      log.error(e.getMessage(), e);
    }
    return result;
  }

  public String getMsgContentUserConfCR(String locale, String crNumber) {
    String content = "";
    content += "[GNOC-CR] CR ";
    content += crNumber;
    content += " " + I18n.getChangeManagement("cr.wo.conplete");
    return content;
  }

//  @Override
//  public List<UserReceiveMsgDTO> getLstConfigReceiveSmS(String crId) {
//    List<UserReceiveMsgDTO> lst = new ArrayList<UserReceiveMsgDTO>();
//    StringBuilder sql = new StringBuilder();
//    sql.append(" from UserReceiveMsg");
//    sql.append(" where crId= :crId ");
//    Map<String, Object> parameters = new HashMap();
//    if (crId != null && !"".equals(crId)) {
//      parameters.put("crId", crId);
//    }
//    return getNamedParameterJdbcTemplate()
//        .query(sql.toString(), parameters,
//            BeanPropertyRowMapper.newInstance(UserReceiveMsgDTO.class));
//  }
}
