package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CR_ACTION_RIGHT;
import com.viettel.gnoc.commons.utils.Constants.CR_SEARCH_TYPE;
import com.viettel.gnoc.commons.utils.Constants.CR_STATE;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CommonOutputDTO;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentInsiteDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrWoDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.model.CrEntity;
import com.viettel.gnoc.cr.util.CrGeneralUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
public class CrDBRepositoryImpl extends BaseRepository implements CrDBRepository {

  @Autowired
  CrGeneralRepository crGeneralRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  CrAffectedServiceDetailsRepository crAffectedServiceDetailsRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Override
  public List<CrInsiteDTO> processListToGenGr(List<CrInsiteDTO> lst, CrInsiteDTO crDTO,
      boolean isManager,
      Long userId, Long userDep, String locale) {
    for (CrInsiteDTO crDTO1 : lst) {
      String code = createActionRightByChoose(crDTO1,
          crDTO.getSearchType(),
          crDTO1.getState(),
          isManager,
          userId,
          userDep);
      crDTO1.setActionRight(code);
      crDTO1.setOnTimeAmount(generateTimeLeft(crDTO1, locale));
    }
    return lst;
  }

  @Override
  public List<CrDTO> processListToGenGrOutSide(List<CrDTO> lst, CrDTO crDTO,
      boolean isManager,
      Long userId, Long userDep, String locale) {
    for (CrDTO crDTO1 : lst) {
      String code = createActionRightByChoose(crDTO1.toModelInsiteDTO(),
          crDTO.getSearchType(),
          crDTO1.getState(),
          isManager,
          userId,
          userDep);
      crDTO1.setActionRight(code);
      crDTO1.setOnTimeAmount(generateTimeLeft(crDTO1.toModelInsiteDTO(), locale));
    }
    return lst;
  }

  @Override
  public Datatable getDataTableSecondaryCr(CrInsiteDTO crInsiteDTO) {
    try {
      if (crInsiteDTO.getCrId() == null
          || crInsiteDTO.getCrId().trim().isEmpty()) {
        return null;
      }
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DB_DAO, "get-list-secondary-cr");
      sql += " and (cr.relate_to_primary_cr = :primary_cr  ";
      Map<String, Object> params = new HashMap<>();
      params.put("primary_cr", Long.valueOf(crInsiteDTO.getCrId().trim()));
      if (StringUtils.isNotNullOrEmpty(crInsiteDTO.getRelateToPreApprovedCr())) {
        sql += "  or cr.cr_id = :cr_id) ";
        params.put("cr_id", Long.valueOf(crInsiteDTO.getRelateToPreApprovedCr().trim()));
      } else {
        sql += ")";
      }
      return getListDataTableBySqlQuery(sql, params, crInsiteDTO.getPage(),
          crInsiteDTO.getPageSize(), CrInsiteDTO.class,
          crInsiteDTO.getSortName(), crInsiteDTO.getSortType());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public Datatable getDataTablePreApprovedCr(CrInsiteDTO crInsiteDTO) {
    try {
      if (crInsiteDTO.getCrId() == null
          || crInsiteDTO.getCrId().trim().isEmpty()) {
        return null;
      }
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DB_DAO, "get-list-pre-approved-cr");
      sql += " and (cr.relate_to_pre_approved_cr = :primary_cr ";
      Map<String, Object> params = new HashMap<>();
      params.put("primary_cr", Long.valueOf(crInsiteDTO.getCrId().trim()));
      if (StringUtils.isNotNullOrEmpty(crInsiteDTO.getRelateToPreApprovedCr())) {
        sql += "  or cr.cr_id = :cr_id) ";
        params.put("cr_id", Long.valueOf(crInsiteDTO.getRelateToPreApprovedCr().trim()));
      } else {
        sql += ")";
      }

      return getListDataTableBySqlQuery(sql, params, crInsiteDTO.getPage(),
          crInsiteDTO.getPageSize(), CrInsiteDTO.class,
          crInsiteDTO.getSortName(), crInsiteDTO.getSortType());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public String createActionRightByChoose(CrInsiteDTO crDTO,
      String actionType,
      String state,
      boolean isManager,
      Long userId,
      Long userDept) {
    String returnCode = Constants.CR_ACTION_RIGHT.LOOKUP_ONLY;
    if (actionType.equals(Constants.CR_SEARCH_TYPE.CREATE_EDIT.toString())) {
      if (state.equals(Constants.CR_STATE.OPEN.toString())
          || state.equals(Constants.CR_STATE.INCOMPLETE.toString())
          || state.equals(Constants.CR_STATE.DRAFT.toString())) {
        returnCode = Constants.CR_ACTION_RIGHT.CAN_EDIT;
      } else if (state.equals(Constants.CR_STATE.RESOLVE.toString())) {
        returnCode = Constants.CR_ACTION_RIGHT.CAN_ONLY_ADDWORKLOG;
      }
    }
    if (actionType.equals(Constants.CR_SEARCH_TYPE.APPROVE.toString())) {
      if (state.equals(Constants.CR_STATE.OPEN.toString())) {
        if (crDTO.getCrType().equals(Constants.CR_TYPE.STANDARD.toString())) {
          returnCode = Constants.CR_ACTION_RIGHT.CAN_APPROVE_STANDARD;
        } else {
          returnCode = Constants.CR_ACTION_RIGHT.CAN_APPROVE;
        }
      }

    }
    if (actionType.equals(Constants.CR_SEARCH_TYPE.VERIFY.toString())) {
      if (state.equals(Constants.CR_STATE.QUEUE.toString())) {
        if (crDTO.getRelateToPreApprovedCr() != null) {
          returnCode = Constants.CR_ACTION_RIGHT.CAN_VERIFY_PREAPPROVE;
        } else {
          returnCode = Constants.CR_ACTION_RIGHT.CAN_VERIFY;
        }
      }
    }
    if (actionType.equals(Constants.CR_SEARCH_TYPE.CONSIDER.toString())) {
      if (state.equals(Constants.CR_STATE.COORDINATE.toString())) {
        if (isManager) {
          if (crDTO.getConsiderUserId() == null
              || crDTO.getConsiderUserId().equals(userId.toString())) {
            returnCode = Constants.CR_ACTION_RIGHT.CAN_CONSIDER;
          } else {
            returnCode = Constants.CR_ACTION_RIGHT.CAN_CONSIDER_NO_APPRAISE;
          }
        } else {
          // tiennv nang cap:  Giao thẩm định thì tất cả các nhân viên trong đơn vị đều có thể vào thẩm định
//          if (crDTO.getConsiderUserId() != null
//              && crDTO.getConsiderUserId().equals(userId.toString())) {
          returnCode = Constants.CR_ACTION_RIGHT.CAN_CONSIDER_NO_ASSIGNEE;
//          }
        }
      }
    }
    if (actionType.equals(Constants.CR_SEARCH_TYPE.SCHEDULE.toString())) {
      if (state.equals(Constants.CR_STATE.EVALUATE.toString())) {
        if (crDTO.getCrType().equals(Constants.CR_TYPE.EMERGENCY.toString())) {
          returnCode = Constants.CR_ACTION_RIGHT.CAN_SCHEDULE_EMR;
        } else if (crDTO.getRelateToPreApprovedCr() != null) {
          returnCode = Constants.CR_ACTION_RIGHT.CAN_SCHEDULE_PREAPPROVE;
        } else {
          returnCode = Constants.CR_ACTION_RIGHT.CAN_SCHEDULE;
        }
      }
    }
    if (actionType.equals(Constants.CR_SEARCH_TYPE.EXCUTE.toString())) {
      if (state.equals(Constants.CR_STATE.APPROVE.toString())) {
        if (isManager) {
          if (crDTO.getChangeResponsible() == null
              || crDTO.getChangeResponsible().equals(userId.toString())) {
            if (crDTO.getCrType().equals(Constants.CR_TYPE.STANDARD.toString())) {
              returnCode = Constants.CR_ACTION_RIGHT.CAN_RECEIVE_STANDARD;
            } else if (crDTO.getCrType().equals(Constants.CR_TYPE.EMERGENCY.toString())) {
              returnCode = Constants.CR_ACTION_RIGHT.CAN_RECEIVE_EMR;
            } else if (crDTO.getRelateToPreApprovedCr() != null) {
              returnCode = Constants.CR_ACTION_RIGHT.CAN_RECEIVE_PREAPPROVE;
            } else {
              returnCode = Constants.CR_ACTION_RIGHT.CAN_RECEIVE;
            }
          } else if (crDTO.getCrType().equals(Constants.CR_TYPE.STANDARD.toString())) {
            returnCode = Constants.CR_ACTION_RIGHT.CAN_RECEIVE_STANDARD_NO_ACCEPT;
          } else if (crDTO.getCrType().equals(Constants.CR_TYPE.EMERGENCY.toString())) {
            returnCode = Constants.CR_ACTION_RIGHT.CAN_RECEIVE_EMR_NO_ACCEPT;
          } else if (crDTO.getRelateToPreApprovedCr() != null) {
            returnCode = Constants.CR_ACTION_RIGHT.CAN_RECEIVE_PREAPPROVE_NO_ACCEPT;
          } else {
            returnCode = Constants.CR_ACTION_RIGHT.CAN_RECEIVE_NO_ACCEPT;
          }
        } else if (crDTO.getChangeResponsible() != null
            && crDTO.getChangeResponsible().equals(userId.toString())) {
          if (crDTO.getCrType().equals(Constants.CR_TYPE.STANDARD.toString())) {
            returnCode = Constants.CR_ACTION_RIGHT.CAN_RECEIVE_STANDARD_NO_ASSIGNEE;
          } else if (crDTO.getCrType().equals(Constants.CR_TYPE.EMERGENCY.toString())) {
            returnCode = Constants.CR_ACTION_RIGHT.CAN_RECEIVE_EMR_NO_ASSIGNEE;
          } else if (crDTO.getRelateToPreApprovedCr() != null) {
            returnCode = Constants.CR_ACTION_RIGHT.CAN_RECEIVE_PREAPPROVE_NO_ASSIGNEE;
          } else {
            returnCode = Constants.CR_ACTION_RIGHT.CAN_RECEIVE_NO_ASSIGNEE;
          }

        } else if (crDTO.getChangeResponsible() == null
            && crDTO.getCrType().equals(Constants.CR_TYPE.NORMAL.toString())
            && crDTO.getChangeResponsibleUnit().equals(userDept.toString())) {
          returnCode = Constants.CR_ACTION_RIGHT.CAN_RECEIVE;
        }
      } else if (state.equals(Constants.CR_STATE.ACCEPT.toString())) {
        returnCode = Constants.CR_ACTION_RIGHT.CAN_ONLY_REASSIGN;
      }
    }
    if (actionType.equals(Constants.CR_SEARCH_TYPE.RESOLVE.toString())) {
      if (state.equals(Constants.CR_STATE.ACCEPT.toString())) {
        returnCode = Constants.CR_ACTION_RIGHT.CAN_RESOLVE;
      }
    }
    if (actionType.equals(Constants.CR_SEARCH_TYPE.CLOSE.toString())) {
      if (state.equals(Constants.CR_STATE.RESOLVE.toString())) {
        returnCode = Constants.CR_ACTION_RIGHT.CAN_CLOSE;
      }
    }
//        if (actionType.equals(Constants.CR_SEARCH_TYPE.CAB.toString())) {
//            returnCode = Constants.CR_ACTION_RIGHT.CAN_ONLY_ADDWORKLOG;
//        }
    if (actionType.equals(Constants.CR_SEARCH_TYPE.QLTD.toString())) {
      returnCode = Constants.CR_ACTION_RIGHT.CAN_EDIT_CR_BY_QLTD;
    }
    if (actionType.equals(Constants.CR_SEARCH_TYPE.WAIT_CAB.toString())) {
      returnCode = Constants.CR_ACTION_RIGHT.CAN_ASSIGN_CAB;
    }
    if (actionType.equals(Constants.CR_SEARCH_TYPE.CAB.toString())) {
      if (crDTO.getCrType().equals(Constants.CR_TYPE.NORMAL.toString())) {
        returnCode = Constants.CR_ACTION_RIGHT.CAN_CAB;
      } else {
        returnCode = Constants.CR_ACTION_RIGHT.CAN_ONLY_ADDWORKLOG;
      }
    }
    if (actionType.equals(Constants.CR_SEARCH_TYPE.Z78.toString())) {
      returnCode = Constants.CR_ACTION_RIGHT.CAN_ONLY_ADDWORKLOG;
    }

    if ((actionType.equals(CR_SEARCH_TYPE.QLTD_RENEW.toString()) && state
        .equals(CR_STATE.ACCEPT.toString()))) {
      returnCode = Constants.CR_ACTION_RIGHT.RENEW_CR_BY_QLTD;
    }
    return returnCode;
  }

  @Override
  public String getResponeTimeCR(CrInsiteDTO crNew) {
    try {

      Calendar calendar = Calendar.getInstance();
      int hour = calendar.get(Calendar.HOUR_OF_DAY);
      int timeProcess = 0;
      if ("0".equals(crNew.getPriority())) { //cao
        timeProcess = 24;
      } else if ("1".equals(crNew.getPriority())) { //trung binh
        timeProcess = 48;
      } else if ("2".equals(crNew.getPriority())) { //thap
        timeProcess = 96;
      }
      if ("4".equals(crNew.getRisk()) && hour >= 11) {
        timeProcess = timeProcess + 24;
      }
      if (("1".equals(crNew.getRisk()) || "2".equals(crNew.getRisk()) || "3"
          .equals(crNew.getRisk())) && hour >= 9) {
        timeProcess = timeProcess + 24;
      }
      Calendar cal = Calendar.getInstance(); // creates calendar
      cal.setTime(new Date()); // sets calendar time/date
      cal.add(Calendar.HOUR_OF_DAY, timeProcess); // adds one hour
      Date responeTime = cal.getTime(); // returns new date object, one hour in the future
      Date lastTime = crNew.getLatestStartTime();
      if (responeTime.before(lastTime)) {
        responeTime = lastTime;
      }
      return DateTimeUtils.convertDateToString(responeTime, DateTimeUtils.patternDateTimeMs);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return "";
  }

  @Override
  public void actionUpdateRelatedCR(CrInsiteDTO crDTO) {
    try {
      if (crDTO.getRelateToPrimaryCr() != null) {
        StringBuilder sql = new StringBuilder("");
        sql.append(" update cr set is_primary_cr = 1 where cr_id = :cr_id");
        Map<String, Object> param = new HashMap<>();
        param.put("cr_id", Long.parseLong(crDTO.getRelateToPreApprovedCr().trim()));
        getNamedParameterJdbcTemplate().update(sql.toString(), param);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public String generateTimeLeft(CrInsiteDTO crDTO,
      String locale) {
    String result = "";
    try {
      String compareDate = crDTO.getCompareDate();
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      Date dateCompare = StringUtils.isStringNullOrEmpty(crDTO.getCompareDate()) ? null
          : sdf.parse(crDTO.getCompareDate());
//            if (compareDate == null || compareDate.trim().isEmpty()) {
//                return result;
//            }
      int amount = 0;
      String state = crDTO.getState();
      if (state.equals(Constants.CR_STATE.CANCEL.toString())
          || state.equals(Constants.CR_STATE.CLOSE.toString())
          || state.equals(Constants.CR_STATE.DRAFT.toString())
          || state.equals(Constants.CR_STATE.OPEN.toString())) {
        return result;
      }
      if (state.equals(Constants.CR_STATE.QUEUE.toString())
          || state.equals(Constants.CR_STATE.COORDINATE.toString())
          || state.equals(Constants.CR_STATE.EVALUATE.toString())
          || state.equals(Constants.CR_STATE.APPROVE.toString())) {
        dateCompare = crDTO.getEarliestStartTime();
      }
      if (state.equals(Constants.CR_STATE.ACCEPT.toString())) {
        dateCompare = crDTO.getLatestStartTime();
        amount = 4;
      }
      if (state.equals(Constants.CR_STATE.RESOLVE.toString())) {
        if (StringUtils.isStringNullOrEmpty(compareDate)) {
          dateCompare = crDTO.getLatestStartTime();
        }
        amount = 24;
      }
      boolean flag = true;

      dateCompare = plusNHours(dateCompare, amount);
      Date dateNow = new Date();
      long t = dateNow.getTime() - dateCompare.getTime();
      if (t < 0) {
        t = 0 - t;
        flag = false;
      }
      long days = (long) t / (24 * 3600 * 1000);
      long hours = (long) (t - (days * (24 * 3600 * 1000))) / (3600 * 1000);
      long minutes = (long) (t - (days * (24 * 3600 * 1000)) - hours * 3600 * 1000) / (60 * 1000);
      Locale local;
      if (StringUtils.isNotNullOrEmpty(locale)) {
        String[] lang_loc = locale.split("_");
        if (lang_loc.length > 1) {
          local = new Locale(lang_loc[0], lang_loc[1]);
        } else {
          local = new Locale(locale);
        }
      } else {
        local = new Locale("en", "US");
      }
      String dayslbl = I18n.getLanguageByLocale(local, "cr.days");
      String hourlbl = I18n.getLanguageByLocale(local, "cr.hours");
      String minlbl = I18n.getLanguageByLocale(local, "cr.minutes");
      if (!flag) {
        result += "-";
      }
      boolean flagD = days != 0;
      boolean flagH = hours != 0;
      if (days != 0) {
        result += days + " " + dayslbl;
      }
      if (flagD) {
        result += " " + hours + " " + hourlbl;
      } else if (hours != 0) {
        result += hours + " " + hourlbl;
      }
      if (flagH || flagD) {
        result += " " + minutes + " " + minlbl;
      } else {
        result += minutes + " " + minlbl;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  public Date plusNHours(Date date, int N) {
    try {
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + N);
      date = cal.getTime();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return date;
  }

  public void updateCrStatusInCaseOfCloseCR(Long actionType, Long crId, CrInsiteDTO crDTO,
      String locale) throws Exception {
    if (actionType == null || crId == null) {
      return;
    }
    Long status = CrGeneralUtil.generateStateForCloseCR(actionType);
    StringBuilder sql = new StringBuilder("");
    Map<String, Object> params = new HashMap<>();
    sql.append(" update cr set state = :state , update_time = sysdate ");
    params.put("state", status);
    if (crDTO.getCrReturnCodeId() != null) {
      sql.append(" ,cr_return_code_id = :cr_return_code_id ");
      params.put("cr_return_code_id", crDTO.getCrReturnCodeId());
    }
    if (Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())
        && crDTO.getUserLogin() != null && crDTO.getUserLoginUnit() != null) {//R_xxx uyquyen
      sql.append(" ,manage_unit_id = :manage_unit_id, manage_user_id = :manage_user_id ");
      params.put("manage_unit_id", crDTO.getUserLoginUnit());//R_xxx uyquyen
      params.put("manage_user_id", crDTO.getUserLogin());
    }
    sql.append(" where cr_id = :cr_id ");
    params.put("cr_id", crId);
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
    Long returnCode = null;
    try {
      if (StringUtils.isNotNullOrEmpty(crDTO.getActionReturnCodeId())) { //them tranh show log null
        returnCode = Long.valueOf(crDTO.getActionReturnCodeId());
      }
    } catch (Exception e) {
      this.log.error(e.getMessage(), e);
    }
    //R_xxx uyquyen _ Ghi chu ro rang khi BGD thay quyen
    insertIntoHistoryOfCr(crDTO, crId,
        actionType,
        returnCode,
        Long.valueOf(crDTO.getUserLogin()), Long.valueOf(crDTO.getUserLoginUnit()),//R_xxx uyquyen
        status, locale);
  }

  @Override
  public void insertIntoHistoryOfCr(
      CrInsiteDTO crDTO,
      Long crId,
      Long actionType,
      Long returnCode,
      Long userId,
      Long deptId,
      Long status, String locale) throws Exception {
    Date date = new Date();
    StringBuilder sql = new StringBuilder("");
    Map<String, Object> parameters = new HashMap<>();
    String crReturnCode =
        crDTO.getCrReturnCodeId() == null ? crDTO.getCrReturnResolve() : crDTO.getCrReturnCodeId();
    String note = crDTO.getActionNotes() == null ? "" : crDTO.getActionNotes().trim();
    if (actionType.equals(Constants.CR_ACTION_CODE.CLOSECR) && "1".equals(crDTO.getCloseCrAuto())) {
      note = "CR 's closed automatically by process setting";
    }

    try {
      if (crReturnCode != null && !crReturnCode.isEmpty()) {
        List<ItemDataCRInside> lstItem = crGeneralRepository
            .getListActionCodeById(crReturnCode, locale);
        if (lstItem != null && !lstItem.isEmpty()) {
          note = lstItem.get(0).getDisplayStr() + ": " + note;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    sql.append("insert into cr_his (chs_id, user_id, unit_id, status, change_date, "
        + "comments, cr_id, notes, action_code, return_code,EARLIEST_START_TIME,LATEST_END_TIME ");

    if (actionType.equals(Constants.CR_ACTION_CODE.CLOSECR) && "1".equals(crDTO.getCloseCrAuto())) {
      sql.append(" , CLOSE_AUTO ");
    }

    sql.append(" ) values (");

    if (actionType.equals(Constants.CR_ACTION_CODE.REJECT)
        || actionType.equals(Constants.CR_ACTION_CODE.CHANGE_CR_TYPE)
        || actionType.equals(Constants.CR_ACTION_CODE.CLOSE_BY_MANAGER)
        || actionType.equals(Constants.CR_ACTION_CODE.CLOSE_BY_APPRAISER)
        || actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_APPRAISER)
        || actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_APPRAISE_BY_MANAGER_SCH)
        || actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER_SCH)
        || actionType.equals(Constants.CR_ACTION_CODE.CLOSE_BY_MANAGER_SCH)
        || actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_APPRAISER_BY_IMPL)
        || actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_MANAGER_BY_IMPL)
        || actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_WHEN_EXCUTE_STD)
        || actionType.equals(Constants.CR_ACTION_CODE.CLOSE_EXCUTE_EMERGENCY)
        || actionType.equals(Constants.CR_ACTION_CODE.RESOLVE)
        || actionType.equals(Constants.CR_ACTION_CODE.CLOSECR)
        || actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_MANAGER_BY_APPRAISER)
        || actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER)
        || actionType.equals(Constants.CR_ACTION_CODE.RESOLVE_WITH_FAILT_STATUS_DUE_TO_WO)) {

      sql.append(
          " cr_his_seq.nextval,:user_id,:deptId,:status,:strdate,:note,:crId,null,:actionType,:returnCode,:earliestStartTime,:latestStartTime");
      if (actionType.equals(Constants.CR_ACTION_CODE.CLOSECR) && "1"
          .equals(crDTO.getCloseCrAuto())) {
        sql.append(" ,:actionCode ");
        parameters.put("actionCode", 1);
      }
      parameters.put("returnCode", returnCode);
    } else {
      sql.append(
          " cr_his_seq.nextval,:user_id,:deptId,:status,:strdate,:note,:crId,null,:actionType,null,:earliestStartTime,:latestStartTime");
    }
    sql.append(")");
    if (crDTO.getEarliestStartTime() == null || crDTO.getLatestStartTime() == null) {
      throw new RuntimeException("ERROR");
    }
    parameters.put("user_id", userId);
    parameters.put("deptId", deptId);
    parameters.put("status", status);
    parameters.put("strdate", date);
    parameters.put("note", note);
    parameters.put("crId", crId);
    parameters.put("actionType", actionType);
    parameters.put("earliestStartTime", crDTO.getEarliestStartTime());
    parameters.put("latestStartTime", crDTO.getLatestStartTime());

    getNamedParameterJdbcTemplate().update(sql.toString(), parameters);
//    getEntityManager().flush();
    actionUpdateResponeTimeCR(crDTO, status, actionType);
  }

  public void actionUpdateResponeTimeCR(CrInsiteDTO crDTO, Long status, Long actionType) {
    try {
      if (Constants.CR_ACTION_RIGHT.CAN_APPROVE.equals(crDTO.getActionRight())
          || Constants.CR_ACTION_RIGHT.CAN_APPROVE_STANDARD.equals(crDTO.getActionType())
          || (Constants.CR_ACTION_CODE.RETURN_TO_MANAGER_BY_APPRAISER.toString()
          .equals(crDTO.getActionType())
          && (Constants.CR_ACTION_RIGHT.CAN_CONSIDER.equalsIgnoreCase(crDTO.getActionRight())
          || Constants.CR_ACTION_RIGHT.CAN_CONSIDER_NO_APPRAISE
          .equalsIgnoreCase(crDTO.getActionRight())
          || Constants.CR_ACTION_RIGHT.CAN_CONSIDER_NO_ASSIGNEE
          .equalsIgnoreCase(crDTO.getActionRight())))
          || (Constants.CR_STATE.QUEUE.equals(status) && Constants.CR_ACTION_CODE.APPROVE
          .equals(actionType))) {
      } else {
        return;
      }
      CrInsiteDTO crNew = actionGetCrById(crDTO.getCrId());
      String responeTime = getResponeTimeCR(crNew);

      if (responeTime != null) {
        StringBuilder sql = new StringBuilder("");
        sql.append(" update open_pm.cr set respone_time = :rtime where cr_id = :cr_id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("rtime", responeTime);
        parameters.put("cr_id", Long.parseLong(crDTO.getCrId().trim()));
        getNamedParameterJdbcTemplate().update(sql.toString(), parameters);
//        getEntityManager().flush();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public CrInsiteDTO actionGetCrById(String crId) {

    try {
      if (crId == null) {
        return null;
      }
      StringBuilder sql = new StringBuilder("");
      sql.append(
          " select cr.cr_id as crId, cr.state, cr.priority, cr.risk, cr.LATEST_START_TIME latestStartTime ");
      sql.append(" from open_pm.cr cr");
      sql.append(" where cr.cr_id = :cr_id");

      Map<String, Object> parameters = new HashMap<>();
      parameters.put("cr_id", Long.parseLong(crId));
      List<CrInsiteDTO> lst = getNamedParameterJdbcTemplate().query(sql.toString(), parameters,
          BeanPropertyRowMapper.newInstance(CrInsiteDTO.class));
      if (!lst.isEmpty()) {
        return lst.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public void actionDeleteCRFormOtherSystem(Long crId) throws Exception {
    StringBuilder sql = new StringBuilder("");
    if (crId == null) {
      return;
    }
    sql.append(" delete cr_created_from_other_sys ");
    sql.append(" where ");
    sql.append(" cr_id = :cr_id");
    Map<String, Object> params = new HashMap<>();
    params.put("cr_id", crId);
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
//    getEntityManager().flush();
  }

  @Override
  public String createCrMappingFromOtherSystem(CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO)
      throws Exception {
    String result = Constants.CR_RETURN_MESSAGE.SUCCESS;
    StringBuilder sql = new StringBuilder("");
    if (crCreatedFromOtherSysDTO.getCrId() == null
        || crCreatedFromOtherSysDTO.getSystemId() == null
        || crCreatedFromOtherSysDTO.getObjectId() == null) {
      return Constants.CR_RETURN_MESSAGE.ERROR;
    }
    Map<String, Object> params = new HashMap<>();
    sql.append(" insert into cr_created_from_other_sys( ");
    sql.append(" ccfosm_id,cr_id,system_id,object_id,step_id,is_active,object_code)");
    sql.append(" values(");
    sql.append(" cr_created_from_other_sys_seq.nextval,:crId, :system_id, :objectId");

    params.put("crId", crCreatedFromOtherSysDTO.getCrId());
    params.put("system_id", crCreatedFromOtherSysDTO.getSystemId());
    params.put("objectId", crCreatedFromOtherSysDTO.getObjectId());

    if (crCreatedFromOtherSysDTO.getStepId() == null) {
      sql.append(",null,");
    } else {
      sql.append(",:stepId,");
      params.put("stepId", crCreatedFromOtherSysDTO.getStepId());
    }
    sql.append("1, :objectCode)");
    params.put("objectCode", crCreatedFromOtherSysDTO.getObjectCode());
    getNamedParameterJdbcTemplate().update(sql.toString(), params);

    try {
      if ("2".equals(crCreatedFromOtherSysDTO.getSystemId())) {

        StringBuilder sql2 = new StringBuilder();
        Map<String, Object> param2 = new HashMap<>();
        sql2.append(
            " insert into ONE_TM.PROBLEM_CR (PROBLEM_CR_ID, PROBLEM_ID, CR_ID, PT_STATUS_ID) "
                + " values (ONE_TM.PROBLEM_CR_seq.nextval, :objectId, :crId, :stepId) ");
        param2.put("objectId", Long.valueOf(crCreatedFromOtherSysDTO.getObjectId()));
        param2.put("crId", Long.valueOf(crCreatedFromOtherSysDTO.getCrId()));
        param2.put("stepId", Long.valueOf(
            crCreatedFromOtherSysDTO.getStepId() != null ? crCreatedFromOtherSysDTO.getStepId()
                : "0"));

        getNamedParameterJdbcTemplate().update(sql2.toString(), param2);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
//    getEntityManager().flush();
    return result;
  }

  @Override
  public void resetApprovalDeptByCr(Long crId) {
    if (crId == null) {
      return;
    }
    StringBuilder sql = new StringBuilder("");
    sql.append(" update cr_approval_department set approved_date = null,"
        + " status = 0, notes = null, return_code = null, "
        + " user_id = null, incomming_date = null ");
    sql.append(" where cr_id = :cr_id ");
//        sql.append(" update cr_approval_department set approved_date = null,"
//                + " status = 0, notes = null, return_code = null, "
//                + " user_id = null, incomming_date = ? ");
//        sql.append(" where cr_id = ? and cadt_level = 1 ");
    Map<String, Object> params = new HashMap<>();
    params.put("cr_id", crId);
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
//    getEntityManager().flush();
  }

  @Override
  public CrApprovalDepartmentInsiteDTO getCurrentCrApprovalDepartmentDTO(Long crId, Long deptId) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DB_DAO, "get-current-cr-approval-department");
      Map<String, Object> params = new HashMap<>();
      params.put("unit_id", deptId);
      params.put("cr_id", crId);

      List<CrApprovalDepartmentInsiteDTO> lst = getNamedParameterJdbcTemplate().
          query(sql, params,
              BeanPropertyRowMapper.newInstance(CrApprovalDepartmentInsiteDTO.class));

      if (!lst.isEmpty()) {
        return lst.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public boolean isLastDepartment(Long crId) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DB_DAO, "is-last-department");
      Map<String, Object> params = new HashMap<>();
      params.put("cr_id", crId);
      List lst = getNamedParameterJdbcTemplate()
          .query(sql, params,
              BeanPropertyRowMapper.newInstance(CrApprovalDepartmentInsiteDTO.class));
      if (lst != null && lst.size() == 1) {
        return true;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return false;
  }

  @Override
  public void updateCurentApprovalDepartment(CrApprovalDepartmentInsiteDTO crApprovalDepartmentDTO,
      CrInsiteDTO crDTO,
      Long crId,
      Long actionType,
      Long returnCode,
      Long userId,
      Long deptId) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> params = new HashMap<>();

    sql.append(" update cr_approval_department set approved_date = :approved_date ,");
    params.put("approved_date", new Date());
    sql.append(" user_id = :user_id,");
    params.put("user_id", userId);
    if (actionType.equals(Constants.CR_ACTION_CODE.REJECT)) {
      sql.append(" status = 2,");
      sql.append(" return_code = :return_code,");
      params.put("return_code", returnCode);
    } else {
      sql.append(" status = 1,");
      sql.append(" return_code = null,");
    }
    sql.append(" notes = :notes ");
    params.put("notes", crDTO.getActionNotes() == null ? "" : crDTO.getActionNotes().trim());
    sql.append(" where cadt_id = :cadt_id ");
    params.put("cadt_id", Long.valueOf(crApprovalDepartmentDTO.getCadtId()));
    getNamedParameterJdbcTemplate().update(sql.toString(), params);

//    getEntityManager().flush();

//    CrApprovalDepartmentEntity crApprovalDepartmentEntity = getEntityManager().find(CrApprovalDepartmentEntity.class, Long.valueOf(crApprovalDepartmentDTO.getCadtId()));
//    if(crApprovalDepartmentEntity != null) {
//      crApprovalDepartmentEntity.setApprovedDate(new Date());
//      crApprovalDepartmentEntity.setUserId(userId);
//      if (actionType.equals(Constants.CR_ACTION_CODE.REJECT)) {
//        crApprovalDepartmentEntity.setStatus(2L);
//        crApprovalDepartmentEntity.setReturnCode(returnCode);
//      } else {
//        crApprovalDepartmentEntity.setStatus(1L);
//        crApprovalDepartmentEntity.setReturnCode(null);
//      }
//      crApprovalDepartmentEntity.setNotes(crDTO.getActionNotes() == null ? "" : crDTO.getActionNotes().trim());
//      getEntityManager().merge(crApprovalDepartmentEntity);
//    }
  }

  @Override
  public List<CrApprovalDepartmentInsiteDTO> getNextApprovalDepartment(
      Long crId,
      Long cadtLevel) {
    List<CrApprovalDepartmentInsiteDTO> lst = new ArrayList<CrApprovalDepartmentInsiteDTO>();
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DB_DAO, "get-next-approval-department");
      Map<String, Object> params = new HashMap<>();
      params.put("cr_id", crId);
      params.put("cadt_level", cadtLevel);

      lst = getNamedParameterJdbcTemplate()
          .query(sql, params,
              BeanPropertyRowMapper.newInstance(CrApprovalDepartmentInsiteDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public void updateNextApprovalDepartment(List<CrApprovalDepartmentInsiteDTO> list) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> params = new HashMap<>();
    if (list == null || list.isEmpty()) {
      return;
    }
    sql.append(" update cr_approval_department set incomming_date = :incomming_date ");
    params.put("incomming_date", new Date());
    sql.append(" where cadt_id in (:lstIds)");
    List<Long> lstApprovals = new ArrayList<>();
    for (CrApprovalDepartmentInsiteDTO crApprovalDepartmentDTO : list) {
      lstApprovals.add(Long.valueOf(crApprovalDepartmentDTO.getCadtId()));
    }
    lstApprovals.add(0L);
    params.put("lstIds", lstApprovals);
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
//    getEntityManager().flush();
  }

  /**
   * Cáº­p nháº­t CR luc phe duyet
   */
  @Override
  public void updateCrStatusInCaseOfApprove(Long actionType,
      Long crId,
      CrInsiteDTO crDTO, String locale) throws Exception {
    if (actionType == null || crId == null) {
      return;
    }
    Map<String, Object> params = new HashMap<>();
    Long status = CrGeneralUtil.generateStateForApproveCr(crDTO, actionType);
    StringBuilder sql = new StringBuilder("");
    sql.append(" update cr set state = :state , update_time = :update_time ");
    params.put("state", status);
    params.put("update_time", new Date());
    if (status.equals(Constants.CR_STATE.APPROVE)
        || status.equals(Constants.CR_STATE.RESOLVE)) {
      sql.append(",manage_unit_id = :unit_id , manage_user_id = :manage_user_id ");
      params.put("unit_id", Long.parseLong(crDTO.getUserLoginUnit()));
      params.put("manage_user_id", Long.parseLong(crDTO.getUserLogin()));
    }
    if (Constants.CR_TYPE.STANDARD.toString().equals(crDTO.getCrType()) && "121"
        .equals(crDTO.getImpactSegment())
        && Constants.CR_ACTION_CODE.APPROVE.equals(actionType)) {
      CrEntity crModel = crDTO.toEntity();
      sql.append(
          " ,EARLIEST_START_TIME = :early_start_time, LATEST_START_TIME = :last_start_time ");
      params.put("early_start_time", crModel.getEarliestStartTime());
      params.put("last_start_time", crModel.getLatestStartTime());
      if (!StringUtils.isStringNullOrEmpty(crDTO.getDisturbanceEndTime()) && !StringUtils
          .isStringNullOrEmpty(crDTO.getDisturbanceStartTime())) {
        sql.append(
            " , DISTURBANCE_START_TIME = :disturbance_start, DISTURBANCE_END_TIME = :disturbance_end ");
        params.put("disturbance_start", crModel.getDisturbanceStartTime());
        params.put("disturbance_end", crModel.getDisturbanceEndTime());
      }
    }
    sql.append(" where cr_id = :cr_id ");
    params.put("cr_id", crId);
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
//    getEntityManager().flush();
    Long returnCode = null;

    try {
      if (StringUtils.isNotNullOrEmpty(crDTO.getActionReturnCodeId())) { //them tranh show log null
        returnCode = Long.valueOf(crDTO.getActionReturnCodeId());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    //R_xxx uyquyen _ TH BGD tu choi level 1 -> Ghi lich su chu thich ro rang
    insertIntoHistoryOfCr(crDTO, crId, actionType,
        returnCode,
        Long.valueOf(crDTO.getUserLogin()), Long.valueOf(crDTO.getUserLoginUnit()),//R_xxx uyquyen
        //                            Long.valueOf(crDTO.getState().trim())
        status, locale);
  }


  @Override
  public CrInsiteDTO getActiveWOController(CrInsiteDTO crDTO) {
    String controlcontent = getActionIds(crDTO);
    crDTO.setActiveWOControllSignal(controlcontent);
    return crDTO;
  }

  public String getActionIds(CrInsiteDTO crDTO) {
    String result = "";
    List<CommonOutputDTO> listCto;
    try {
      // List<CommonOutputDTO> listCto = new ArrayList<CommonOutputDTO>();
      if (crDTO.getSubcategory() == null || "".equals(crDTO.getSubcategory().trim())) {
        return result;
      }
      if (crDTO.getCrType() == null || "".equals(crDTO.getCrType().trim())) {
        return result;
      }
      Map<String, Object> params = new HashMap<>();
      String sql = "select action_id errorCode from \n"
          + "ACTIVE_WO_CONTROL \n"
          + "where CR_TYPE = :cr_type \n"
          + "AND SUBCATEGORY_ID = :sub_id ";
      params.put("cr_type", Long.parseLong(crDTO.getCrType()));
      params.put("sub_id", Long.parseLong(crDTO.getSubcategory()));
      listCto = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CommonOutputDTO.class));

      for (CommonOutputDTO codto : listCto) {
        result += "|" + codto.getErrorCode();
      }
      if (!result.isEmpty()) {
        result += "|";
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  @Override
  public List<CrApprovalDepartmentInsiteDTO> getLowerLevelUnAppovedRecords(Long crId,
      Long currentLevel) {
    List<CrApprovalDepartmentInsiteDTO> lst = new ArrayList<>();
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DB_DAO, "get-lower-level-unapproved");
      Map<String, Object> params = new HashMap<>();
      params.put("cr_id", crId);
      params.put("cadt_level", currentLevel);
      lst = getNamedParameterJdbcTemplate()
          .query(sql, params,
              BeanPropertyRowMapper.newInstance(CrApprovalDepartmentInsiteDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public void updateLowerLevelUnApprovedRecord(
      CrApprovalDepartmentInsiteDTO crApprovalDepartmentDTO,
      CrInsiteDTO crDTO, Long crId, Long actionType, Long returnCode, Long userId) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> params = new HashMap<>();
    sql.append(" update cr_approval_department set approved_date = :cur_date ,");
    params.put("cur_date", new Date());
    sql.append(" user_id = :user_id,");
    params.put("user_id", userId);
    if (actionType.equals(Constants.CR_ACTION_CODE.REJECT)) {
      sql.append(" status = 2,");
      sql.append(" return_code = :return_code,");
      params.put("return_code", returnCode);
      sql.append(" notes = :notes ");
      params.put("notes", crDTO.getActionNotes() == null ? "" : crDTO.getActionNotes().trim());
    } else {
      sql.append(" status = 1,");
      sql.append(" return_code = null,");
      sql.append(" notes = :notes ");
      params.put("notes", crDTO.getActionNotes() == null ? "" : crDTO.getActionNotes().trim());
    }
    sql.append(" where cadt_id = :cadt_id ");
    params.put("cadt_id", Long.valueOf(crApprovalDepartmentDTO.getCadtId()));
    getNamedParameterJdbcTemplate().update(sql.toString(), params);

//    getEntityManager().flush();
  }

  @Override
  public void actionCloseCrIncaseOfRejectPrimaryCR(CrInsiteDTO crDTO, Long actionType,
      String locale) throws Exception {
    List<CrInsiteDTO> listCrDTO = getListSecondaryCr(crDTO);
    if (!listCrDTO.isEmpty()) {
      for (CrInsiteDTO crDTO1 : listCrDTO) {
        updateCrStatusInCaseOfApprove(actionType, Long.valueOf(crDTO1.getCrId()), crDTO, locale);
      }
    }
  }

  @Override
  public List<CrInsiteDTO> getListSecondaryCr(CrInsiteDTO crDTO) {
    List<CrInsiteDTO> list;
    try {
      //List<CrDTO> list = new ArrayList<CrDTO>();
      if (crDTO.getCrId() == null
          || crDTO.getCrId().trim().isEmpty()) {
        return null;
      }
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DB_DAO, "get-list-secondary-cr");
      sql += " and cr.relate_to_primary_cr = :primary_cr ";
      Map<String, Object> params = new HashMap<>();
      params.put("primary_cr", Long.valueOf(crDTO.getCrId().trim()));
      list = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrInsiteDTO.class));
      return list;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  /**
   * Cap nhat CR luc tham dinh
   */
  @Override
  public void updateCrStatusInCaseOfAppraisal(Long actionType, Long crId, CrInsiteDTO crDTO,
      String locale) throws Exception {
    if (actionType == null || crId == null) {
      return;
    }
    Long status = CrGeneralUtil.generateStateForAppraisalCR(crDTO, actionType);
    StringBuilder sql = new StringBuilder("");
    Map<String, Object> params = new HashMap<>();
    sql.append(" update cr set state = :state , update_time = :update_time ");
    params.put("state", status);
    params.put("update_time", new Date());
    if (status.equals(Constants.CR_STATE.COORDINATE)) {
      sql.append(" ,consider_unit_id = :consider_unit_id ");
      params.put("consider_unit_id", Long.parseLong(crDTO.getConsiderUnitId()));
      sql.append(" ,consider_user_id = :consider_user_id ");
      params.put("consider_user_id", Long.parseLong(crDTO.getConsiderUserId()));
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_MANAGER_BY_APPRAISER)) {
      sql.append(" ,consider_user_id = null");
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_APPRAISER)) {
      sql.append(" ,manage_unit_id = null ");
      sql.append(" ,manage_user_id = null ");
      sql.append(" ,consider_unit_id = null ");
      sql.append(" ,consider_user_id = null ");
    }
    if (!"4".equals(crDTO.getRisk())) {
      if (crDTO.getUserCab() != null && !crDTO.getUserCab().trim().isEmpty()) {
        sql.append(" , user_cab = :user_cab ");
        params.put("user_cab", crDTO.getUserCab());
      }
    }
    if (crDTO.getIsConfirmAction() != null) {
      sql.append(" , IS_CONFIRM_ACTION = :isconfirmact ");
      params.put("isconfirmact", crDTO.getIsConfirmAction());
    }
    sql.append(" where cr_id = :cr_id ");
    params.put("cr_id", crId);
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
//    getEntityManager().flush();
    Long returnCode = null;
    try {
      if (StringUtils.isNotNullOrEmpty(crDTO.getActionReturnCodeId())) { //them tranh show log null
        returnCode = Long.valueOf(crDTO.getActionReturnCodeId());
      }
    } catch (Exception e) {
      this.log.error(e.getMessage(), e);
    }
    //R_xxx uyquyen Ghi chu ro rang khi BGD tham dinh
    insertIntoHistoryOfCr(crDTO, crId,
        actionType,
        returnCode,
        Long.valueOf(crDTO.getUserLogin()), Long.valueOf(crDTO.getUserLoginUnit()),//R_xxx uyquyen
        //                    Long.valueOf(crDTO.getState().trim())
        status, locale);
  }

  /**
   * Cap nhat Cr luc tham dinh
   */
  @Override
  public void assignAppraiseToEmployee(Long actionType, Long crId, CrInsiteDTO crDTO,
      boolean hasSaveHis, String locale) throws Exception {
    if (actionType == null || crId == null) {
      return;
    }
    StringBuilder sql = new StringBuilder("");
    Map<String, Object> params = new HashMap<>();
    sql.append(" update cr set update_time = :update_time, ");
    params.put("update_time", new Date());
    sql.append(" consider_user_id = :consider_user_id ");
    params.put("consider_user_id", Long.parseLong(crDTO.getAssignUserId()));
    sql.append(" where cr_id = :cr_id ");
    params.put("cr_id", crId);
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
//    getEntityManager().flush();
    if (hasSaveHis) {
      //R_xxx uyquyen Ghi chu ro rang khi BGD tham dinh
      insertIntoHistoryOfCr(crDTO, crId,
          actionType,
          null,
          Long.valueOf(crDTO.getUserLogin()), Long.valueOf(crDTO.getUserLoginUnit()),//R_xxx uyquyen
          Constants.CR_STATE.COORDINATE, locale);
    }
  }

  @Override
  public void updateCrTypeInCaseOfVerify(Long crId, CrInsiteDTO crDTO) {
    StringBuilder sql = new StringBuilder("");
    sql.append(" update cr set cr_type = :cr_type , update_time = :update_time ");
    sql.append(" where cr_id = :cr_id ");
    Map<String, Object> params = new HashMap<>();
    params.put("cr_type", Long.parseLong(crDTO.getCrType()));
    params.put("update_time", new Date());
    params.put("cr_id", crId);
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
//    getEntityManager().flush();
  }


  /**
   * Cap nhat Cr luc kiem tra dau vao
   */
  @Override
  public void updateCrStatusInCaseOfVerify(Long actionType, Long crId, CrInsiteDTO crDTO,
      String locale) throws Exception {
    if (actionType == null || crId == null) {
      return;
    }
    Map<String, Object> params = new HashMap<>();
    Long status = CrGeneralUtil.generateStateForVerifyCR(crDTO, actionType);
    StringBuilder sql = new StringBuilder("");
    sql.append(" update cr set state = :state , update_time = :update_time ");
    params.put("state", status);
    params.put("update_time", new Date());
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER)) {
      sql.append(", manage_unit_id = null, manage_user_id = null ");
    } else {
      sql.append(", manage_unit_id = :manage_unit_id, manage_user_id = :manage_user_id ");
      params.put("manage_unit_id", crDTO.getUserLoginUnit());
      params.put("manage_user_id", crDTO.getUserLogin());
    }
    if (crDTO.getDutyType() != null) {
      sql.append(" ,DUTY_TYPE = :duty_type ");
      params.put("duty_type", crDTO.getDutyType());
    }

    if (crDTO.getEarliestStartTime() != null) {
      sql.append(" , earliest_start_time = :earliest_start_time ");
      params.put("earliest_start_time", crDTO.getEarliestStartTime());
    }
    if (crDTO.getLatestStartTime() != null) {
      sql.append(" , latest_start_time = :latest_start_time ");
      params.put("latest_start_time", crDTO.getLatestStartTime());
    }
    if (crDTO.getDisturbanceStartTime() != null) {
      sql.append(" , disturbance_start_time = :disturbance_start_time ");
      params.put("disturbance_start_time", crDTO.getDisturbanceStartTime());
    }
    if (crDTO.getDisturbanceEndTime() != null) {
      sql.append(" , disturbance_end_time = :disturbance_end_time ");
      params.put("disturbance_end_time", crDTO.getDisturbanceEndTime());
    }
    //tuanpv14_cap nhat cr lien quan_start
    if (StringUtils.isNotNullOrEmpty(crDTO.getRelateToPrimaryCr())) {
      sql.append(" , relate_to_primary_cr = :relate_to_primary_cr ");
//            sql.append(" , is_primary_cr = null");
      params.put("relate_to_primary_cr", crDTO.getRelateToPrimaryCr());
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getIsPrimaryCr())) {
      sql.append(" , is_primary_cr = :is_primary_cr");
      sql.append(" , relate_to_primary_cr = null ");
      params.put("is_primary_cr", crDTO.getIsPrimaryCr());
    }
    //tuanpv14_cap nhat cr lien quan_end
    if (status.equals(Constants.CR_STATE.COORDINATE)) {
      sql.append("  ,consider_unit_id = :consider_unit_id ");
      params.put("consider_unit_id", Long.parseLong(crDTO.getConsiderUnitId()));
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getUserCab())) {
      sql.append(" , user_cab = :user_cab ");
      params.put("user_cab", crDTO.getUserCab());
    }

    if (crDTO.getCircleAdditionalInfo() != null) {
      sql.append(" , CIRCLE_ADDITIONAL_INFO = :ca_info ");
      params.put("ca_info", crDTO.getCircleAdditionalInfo());
    }

    if (crDTO.getIsConfirmAction() == null) {
      sql.append(" , IS_CONFIRM_ACTION = null ");
    } else {
      sql.append(" , IS_CONFIRM_ACTION = :isconfirmact ");
      params.put("isconfirmact", crDTO.getIsConfirmAction());
    }

    sql.append(" where cr_id = :cr_id ");
    params.put("cr_id", crId);
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
//    getEntityManager().flush();
    Long returnCode = null;
    try {
      if (StringUtils.isNotNullOrEmpty(crDTO.getActionReturnCodeId())) { //them tranh show log null
        returnCode = Long.valueOf(crDTO.getActionReturnCodeId());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    insertIntoHistoryOfCr(crDTO, crId,
        actionType,
        returnCode,
        Long.valueOf(crDTO.getUserLogin()), Long.valueOf(crDTO.getUserLoginUnit()),
        //                    Long.valueOf(crDTO.getState().trim())
        status, locale);
  }

  /**
   * Cap nhat Cr xap lich
   */
  @Override
  public void updateCrStatusInCaseOfSchedule(Long actionType, Long crId, CrInsiteDTO crDTO,
      String locale) throws Exception {
    if (actionType == null || crId == null) {
      return;
    }
    CrEntity crModel = crDTO.toEntity();
    Map<String, Object> params = new HashMap<>();
    Long status = CrGeneralUtil.generateStateForSchedule(crDTO, actionType);
    StringBuilder sql = new StringBuilder("");
    sql.append(" update cr set state = :state , update_time = :update_time ");
    params.put("state", status);
    params.put("update_time", new Date());
    if (crModel.getPriority() != null) {
      sql.append(" , priority = :priority ");
      params.put("priority", crModel.getPriority());
    }
    if (crModel.getEarliestStartTime() != null) {
      sql.append(" , earliest_start_time = :earliest_start_time ");
      params.put("earliest_start_time", crModel.getEarliestStartTime());
    }
    if (crModel.getLatestStartTime() != null) {
      sql.append(" , latest_start_time = :latest_start_time ");
      params.put("latest_start_time", crModel.getLatestStartTime());
    }

    if (crModel.getDisturbanceStartTime() != null) {
      sql.append(" , disturbance_start_time = :disturbance_start_time ");
      params.put("disturbance_start_time", crModel.getDisturbanceStartTime());
    } else {
      sql.append(" , disturbance_start_time = null ");
    }

    if (crModel.getDisturbanceEndTime() != null) {
      sql.append(" , disturbance_end_time = :disturbance_end_time ");
      params.put("disturbance_end_time", crModel.getDisturbanceEndTime());
    } else {
      sql.append(" , disturbance_end_time = null ");
    }
    if (crDTO.getDutyType() != null) {
      sql.append(" ,DUTY_TYPE = :duttyType ");
      params.put("duttyType", crDTO.getDutyType());
    }
    sql.append(" , SERVICE_AFFECTING = :service_affecting ");
    if ("1".equals(String.valueOf(crModel.getServiceAffecting()))) {
      params.put("service_affecting", 1);
    } else {
      params.put("service_affecting", 0);
    }

    if (crModel.getChangeResponsibleUnit() != null) {
      sql.append(" , change_responsible_unit = :change_responsible_unit ");
      params.put("change_responsible_unit", crModel.getChangeResponsibleUnit());
    }
    //R846092_start
    if (crModel.getChangeResponsible() != null) {
      sql.append(" , change_responsible = :change_responsible ");
      params.put("change_responsible", crModel.getChangeResponsible());
    }

    if (crDTO.getCircleAdditionalInfo() != null) {
      sql.append(" , CIRCLE_ADDITIONAL_INFO = :ca_info ");
      params.put("ca_info", crDTO.getCircleAdditionalInfo());
    }

    if (crDTO.getTotalAffectedCustomers() != null) {
      sql.append(" , TOTAL_AFFECTED_CUSTOMERS = :total_ac ");
      params.put("total_ac", crDTO.getTotalAffectedCustomers());
    } else {
      sql.append(" , TOTAL_AFFECTED_CUSTOMERS = null ");
    }

    if (crDTO.getTotalAffectedMinutes() != null) {
      sql.append(" , TOTAL_AFFECTED_MINUTES = :total_mi ");
      params.put("total_mi", crDTO.getTotalAffectedMinutes());
    } else {
      sql.append(" , TOTAL_AFFECTED_MINUTES = null ");
    }

    //R846092_end
        /*if (actionType.equals(Constants.CR_ACTION_CODE.SCHEDULE)
         && !Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
         sql.append(" ,manage_unit_id = ?");
         sql.append(" ,manage_user_id = ?");
         paramList.add(crDTO.getUserLoginUnit());
         paramList.add(crDTO.getUserLogin());

         }*/
    if (actionType.equals(Constants.CR_ACTION_CODE.SCHEDULE)) {

      if (!Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
        sql.append(" ,manage_unit_id = :manage_unit_id ");
        sql.append(" ,manage_user_id = :manage_user_id ");
        params.put("manage_unit_id", crDTO.getUserLoginUnit());
        params.put("manage_user_id", crDTO.getUserLogin());

        if ("1".equals(crDTO.getIsTracingCr())) {
          if (StringUtils.isStringNullOrEmpty(crDTO.getChangeResponsible())) {
            sql.append(" ,CHANGE_RESPONSIBLE = :change_res ");
            params.put("change_res", crDTO.getChangeResponsible());
          }
        }
      }

      if ((crDTO.getAutoExecute() == null) || (crDTO.getAutoExecute().trim().isEmpty())) {
        sql.append(" , AUTO_EXECUTE = null ");
      } else {
        sql.append(" , AUTO_EXECUTE = :auto_exe ");
        params.put("auto_exe", crDTO.getAutoExecute().trim());
      }

      if (crDTO.getIsConfirmAction() == null) {
        sql.append(" , IS_CONFIRM_ACTION = null ");
      } else {
        sql.append(" , IS_CONFIRM_ACTION = :isconfirmact ");
        params.put("isconfirmact", crDTO.getIsConfirmAction());
      }

      if (crDTO.getIsRunType() == null) {
        sql.append(" , IS_RUN_TYPE = null ");
      } else {
        sql.append(" , IS_RUN_TYPE = :isRuntype ");
        params.put("isRuntype", crDTO.getIsRunType());
      }
    }

    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER_SCH)) {
      sql.append(" ,manage_unit_id = null ");
      sql.append(" ,manage_user_id = null ");
      sql.append(" ,consider_unit_id = null ");
      sql.append(" ,consider_user_id = null ");
      sql.append(" ,user_cab = null ");
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_APPRAISE_BY_MANAGER_SCH)) {
      sql.append(" ,consider_user_id = null ");
      sql.append(" ,user_cab = null ");
    }
    //tuanpv14_cap nhat cr lien quan_start
    if (crDTO.getRelateToPrimaryCr() != null && !crDTO.getRelateToPrimaryCr().trim().isEmpty()) {
      sql.append(" , relate_to_primary_cr = :relate_to_primary_cr ");
//            sql.append(" , is_primary_cr = null");
      params.put("relate_to_primary_cr", crDTO.getRelateToPrimaryCr());
    }
    if (crDTO.getIsPrimaryCr() != null && !crDTO.getIsPrimaryCr().trim().isEmpty()) {
      sql.append(" , is_primary_cr = :is_primary_cr ");
      sql.append(" , relate_to_primary_cr = null ");
      params.put("is_primary_cr", crDTO.getIsPrimaryCr());
    }

    //tuanpv14_cap nhat cr lien quan_end
    sql.append(" where cr_id = :cr_id ");
    params.put("cr_id", crId);
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
//    getEntityManager().flush();
    Long returnCode = null;
    try {
      if (StringUtils.isNotNullOrEmpty(crDTO.getActionReturnCodeId())) { //them tranh show log null
        returnCode = Long.valueOf(crDTO.getActionReturnCodeId());
      }
    } catch (Exception e) {
      this.log.error(e.getMessage(), e);
    }
    insertIntoHistoryOfCr(crDTO, crId,
        actionType,
        returnCode,
        Long.valueOf(crDTO.getUserLogin()), Long.valueOf(crDTO.getUserLoginUnit()),
        //                    Long.valueOf(crDTO.getState().trim())
        status, locale);
  }

  /**
   * Cap nhat Cr tiep nhan thuc thi
   */
  @Override
  public void updateCrStatusInCaseOfReceiving(Long actionType, Long crId, CrInsiteDTO crDTO,
      String locale) throws Exception {
    if (actionType == null || crId == null) {
      return;
    }
    boolean isManager = false;
    try {
      isManager = userRepository.isManagerOfUnits(Long.valueOf(crDTO.getUserLogin()));
    } catch (Exception e1) {
      this.log.error(e1.getMessage(), e1);
    }

    Map<String, Object> params = new HashMap<>();
    Long status = CrGeneralUtil.generateStateForReceive(actionType);
    StringBuilder sql = new StringBuilder("");
    sql.append(" update cr set state = :state , update_time = :update_time ");
    params.put("state", status);
    params.put("update_time", new Date());
    if (actionType.equals(Constants.CR_ACTION_CODE.ASSIGN_EXC_TO_EMPLOYEE)) {
      sql.append(" ,change_responsible = :change_responsible ");
      params.put("change_responsible", crDTO.getAssignUserId());
      if (Constants.CR_TYPE.STANDARD.toString().equals(crDTO.getCrType())) {
        sql.append(",manage_unit_id = :user_login_unit , manage_user_id = :user_login ");
        params.put("user_login_unit", Long.parseLong(crDTO.getUserLoginUnit()));
        params.put("user_login", Long.parseLong(crDTO.getUserLogin()));
      }
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.ACCEPT)) {
      sql.append(" ,change_responsible = :accept_change_res ");
      params.put("accept_change_res", crDTO.getUserLogin());
      if (isManager && Constants.CR_TYPE.STANDARD.toString().equals(crDTO.getCrType())) {
        sql.append(",manage_unit_id = :user_login_unit , manage_user_id = :user_login ");
        params.put("user_login_unit", Long.parseLong(crDTO.getUserLoginUnit()));
        params.put("user_login", Long.parseLong(crDTO.getUserLogin()));
      }
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_MANAGER_BY_IMPL)) {
      sql.append(" ,MANAGE_UNIT_ID = null ");//xem lai case nay
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_APPRAISER_BY_IMPL)) {
      sql.append(" ,consider_user_id = null ");
      sql.append(" ,user_cab = null ");
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_WHEN_EXCUTE_STD)) {
      sql.append(" ,manage_unit_id = null ");
      sql.append(" ,manage_user_id = null ");
      sql.append(" ,consider_unit_id = null ");
      sql.append(" ,consider_user_id = null ");
      sql.append(" ,user_cab = null ");
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RESOLVE_WITH_FAILT_STATUS_DUE_TO_WO)) {
      sql.append(" ,FAIL_DUE_TO_FT = :fail_due_to_ft ");
      params.put("fail_due_to_ft", crDTO.getFailDueToFT());
    }

    sql.append(" where cr_id = :cr_id ");
    params.put("cr_id", crId);
    if (actionType.equals(Constants.CR_ACTION_CODE.ASSIGN_EXC_TO_EMPLOYEE)) {
      String oldCrResponse = crDTO.getChangeResponsible();
      String assignee = crDTO.getAssignUserId();
      if (oldCrResponse != null && assignee != null
          && assignee.trim().toLowerCase().
          equals(oldCrResponse.trim().toLowerCase())) {
        sql.append(" and 1 = 0 ");
      }
    }
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
//    getEntityManager().flush();
    Long returnCode = null;
    try {
      if (StringUtils.isNotNullOrEmpty(crDTO.getActionReturnCodeId())) { //them tranh show log null
        returnCode = Long.valueOf(crDTO.getActionReturnCodeId());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    //R_xxx uyquyen _ Ghi chu ro rang khi BGD thay quyen
    insertIntoHistoryOfCr(crDTO, crId,
        actionType,
        returnCode,
        Long.valueOf(crDTO.getUserLogin()), Long.valueOf(crDTO.getUserLoginUnit()),//R_xxx uyquyen
        //                    Long.valueOf(crDTO.getState().trim())
        status, locale);
  }

  @Override
  public List<CrInsiteDTO> getListPreApprovedCr(CrInsiteDTO crInsiteDTO) {
    List<CrInsiteDTO> list;
    try {
      if (crInsiteDTO.getCrId() == null
          || crInsiteDTO.getCrId().trim().isEmpty()) {
        return null;
      }
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DB_DAO, "get-list-pre-approved-cr");
      sql += " and cr.relate_to_pre_approved_cr = :primary_cr ";
      Map<String, Object> params = new HashMap<>();
      params.put("primary_cr", Long.valueOf(crInsiteDTO.getCrId().trim()));
      list = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrInsiteDTO.class));
      return list;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  /**
   * Cap nhat CR resolve
   */
  @Override
  public void updateCrStatusInCaseOfResolve(Long actionType, Long crId, CrInsiteDTO crDTO,
      String locale) throws Exception {
    if (actionType == null || crId == null) {
      return;
    }
    Map<String, Object> params = new HashMap<>();
    Long status = CrGeneralUtil.generateStateForResolve(actionType);
    log.debug("Cr Schedule status : " + status);

    StringBuilder sql = new StringBuilder("");
    sql.append(" update cr set state = :state , update_time = :u_time ");
    params.put("state", status);
    params.put("u_time", new Date());
    if (crDTO.getCrReturnResolve() != null && parseToLong(crDTO.getCrReturnResolve()) != null) {
      sql.append(" ,cr_return_resolve = :re_resolve ");
      params.put("re_resolve", crDTO.getCrReturnResolve());
    }

    if (crDTO.getResolveReturnCode() != null && parseToLong(crDTO.getResolveReturnCode()) != null) {
      sql.append(" ,RESOLVE_RESTURN_CODE = :re_code ");
      params.put("re_code", crDTO.getResolveReturnCode());
    }

    if (crDTO.getFailDueToFT() != null && parseToLong(crDTO.getCrReturnResolve()) != null) {
      sql.append(" ,FAIL_DUE_TO_FT = :fail_due_ft ");
      params.put("fail_due_ft", crDTO.getFailDueToFT());
    }

    sql.append(" where cr_id = :cr_id ");
    params.put("cr_id", crId);
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
//    getEntityManager().flush();
    Long returnCode = null;
    try {
      if (StringUtils.isNotNullOrEmpty(crDTO.getActionReturnCodeId())) { //them tranh show log null
        returnCode = Long.valueOf(crDTO.getActionReturnCodeId());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    insertIntoHistoryOfCr(crDTO, crId,
        actionType,
        returnCode,
        Long.valueOf(crDTO.getUserLogin()), Long.valueOf(crDTO.getUserLoginUnit()),
        status, locale);
  }

  /**
   * Giao Cab CR
   */
  @Override
  public void updateCrStatusInCaseOfAssignCabCR(Long actionType, Long crId, CrInsiteDTO crDTO,
      String locale) throws Exception {
    if (actionType == null || crId == null) {
      return;
    }
    Map<String, Object> params = new HashMap<>();
    Long status = CrGeneralUtil.generateStateForAssignCabCR(actionType);
    StringBuilder sql = new StringBuilder("");
    sql.append(" update cr set state = :state ");
    params.put("state", status);
    if (Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER_CAB.equals(actionType)) {
      sql.append(", MANAGE_UNIT_ID = null ");
      sql.append(", MANAGE_USER_ID = null ");
    }
    if (Constants.CR_ACTION_CODE.ASSIGN_TO_CAB.equals(actionType)) {
      sql.append(", user_cab = :user_cab ");
      params.put("user_cab", crDTO.getUserCab());

      if (crDTO.getCircleAdditionalInfo() != null) {
        sql.append(" , CIRCLE_ADDITIONAL_INFO = :c_a_info ");
        params.put("c_a_info", crDTO.getCircleAdditionalInfo());
      }
      if (crDTO.getEarliestStartTime() != null && crDTO.getLatestStartTime() != null) {
        sql.append(" ,EARLIEST_START_TIME = :e_start_time, LATEST_START_TIME = :l_start_time ");
        params.put("e_start_time", crDTO.getEarliestStartTime());
        params.put("l_start_time", crDTO.getLatestStartTime());
      }
      if (crDTO.getDisturbanceEndTime() != null && crDTO.getDisturbanceStartTime() != null) {
        sql.append(
            " , DISTURBANCE_START_TIME = :d_start_time, DISTURBANCE_END_TIME = :d_end_time ");
        params.put("d_start_time", crDTO.getDisturbanceStartTime());
        params.put("d_end_time", crDTO.getDisturbanceEndTime());
      }

    } else {
      sql.append(", user_cab = null");
    }
    sql.append(", update_time = sysdate ");
    if (crDTO.getCrReturnCodeId() != null) {
      sql.append(" ,cr_return_code_id = :cr_return_code ");
      params.put("cr_return_code", crDTO.getCrReturnCodeId());
    }
    if (crDTO.getIsConfirmAction() != null) {
      sql.append(" , IS_CONFIRM_ACTION = :isconfirmact ");
      params.put("isconfirmact", crDTO.getIsConfirmAction());
    }
    sql.append(" where cr_id = :cr_id ");
    params.put("cr_id", crId);
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
//    getEntityManager().flush();
    Long returnCode = null;
    try {
      if (StringUtils.isNotNullOrEmpty(crDTO.getActionReturnCodeId())) { //them tranh show log null
        returnCode = Long.valueOf(crDTO.getActionReturnCodeId());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    insertIntoHistoryOfCr(crDTO, crId,
        actionType,
        returnCode,
        Long.valueOf(crDTO.getUserLogin()), Long.valueOf(crDTO.getUserLoginUnit()),
        status, locale);

    //send sms
  }

  /**
   * Cab CR
   */
  @Override
  public void updateCrStatusInCaseOfCabCR(Long actionType, Long crId, CrInsiteDTO crDTO,
      String locale) throws Exception {
    if (actionType == null || crId == null) {
      return;
    }
    Long status = CrGeneralUtil.generateStateForCabCR(actionType);
    StringBuilder sql = new StringBuilder("");
    Map<String, Object> params = new HashMap<>();
    sql.append(" update cr set state = :status , update_time = sysdate ");
    params.put("status", status);
    if (crDTO.getCrReturnCodeId() != null) {
      sql.append(" ,cr_return_code_id = :cr_return_code_id ");
      params.put("cr_return_code_id", crDTO.getCrReturnCodeId());
    }
    if (crDTO.getDutyType() != null) {
      sql.append(" ,DUTY_TYPE = :dutty_type ");
      params.put("dutty_type", crDTO.getDutyType());
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.CAB)) {
      if (crDTO.getLstAffectedService() != null && !crDTO.getLstAffectedService().isEmpty()) {
        sql.append(" ,service_affecting = 1 ");
        //insert afect service
        crAffectedServiceDetailsRepository.saveListDTONoIdSession(crDTO.getLstAffectedService());
      } else {
        sql.append(" ,service_affecting = 0 ");
      }
      sql.append(" ,disturbance_start_time = :disturbanceStartTime ");
      params.put("disturbanceStartTime", crDTO.getDisturbanceStartTime());
      sql.append(" ,disturbance_end_time = :disturbanceEndTime ");
      params.put("disturbanceEndTime", crDTO.getDisturbanceEndTime());

      if (crDTO.getIsConfirmAction() == null) {
        sql.append(" , IS_CONFIRM_ACTION = null ");
      } else {
        sql.append(" , IS_CONFIRM_ACTION = :isconfirmact ");
        params.put("isconfirmact", crDTO.getIsConfirmAction());
      }
    }

    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_WHEN_CAB)) {
      sql.append(" ,user_cab = null, MANAGE_UNIT_ID = null, MANAGE_USER_ID = null  ");
    } else if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CONSIDER_WHEN_CAB)) {
      sql.append(" ,user_cab = null ");
    } else if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_MANAGE_WHEN_CAB)) {
      sql.append(" ,user_cab = :user_cab ");
      params.put("user_cab", Long.valueOf(crDTO.getUserCab()));
    } else if (actionType.equals(Constants.CR_ACTION_CODE.CAB)) {
      sql.append(" ,user_cab = :user_cab ");
      params.put("user_cab", Long.valueOf(crDTO.getUserLogin()));
    }

    sql.append(" where cr_id = :cr_id ");
    params.put("cr_id", crId);
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
//    getEntityManager().flush();
    Long returnCode = null;
    try {
      if (StringUtils.isNotNullOrEmpty(crDTO.getActionReturnCodeId())) { //them tranh show log null
        returnCode = Long.valueOf(crDTO.getActionReturnCodeId());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    insertIntoHistoryOfCr(crDTO, crId,
        actionType,
        returnCode,
        Long.valueOf(crDTO.getUserLogin()), Long.valueOf(crDTO.getUserLoginUnit()),
        status, locale);

  }

  @Override
  public void updateCrInCaseOfQLTDChangeCR(Long actionType, Long crId, CrInsiteDTO crDTO,
      String locale) throws Exception {
    if (actionType == null || crId == null) {
      return;
    }
    Map<String, Object> params = new HashMap<>();
    Long status = Long.valueOf(crDTO.getState());
    StringBuilder sql = new StringBuilder("");
    sql.append(" update cr set update_time = sysdate ");

    if (actionType.equals(Constants.CR_ACTION_CODE.EDIT_CR_BY_QLTD)) {
      if (crDTO.getLstAffectedService() != null && !crDTO.getLstAffectedService().isEmpty()) {
        sql.append(" ,service_affecting = 1 ");
        //insert afect service
        crAffectedServiceDetailsRepository.saveListDTONoIdSession(crDTO.getLstAffectedService());
      } else {
        sql.append(" ,service_affecting = 0 ");
      }
      if (crDTO.getDutyType() != null) {
        sql.append(" ,DUTY_TYPE = :dutty_type ");
        params.put("dutty_type", crDTO.getDutyType());
      }
      sql.append(" ,disturbance_start_time = :disturbanceStartTime ");
      params.put("disturbanceStartTime", crDTO.getDisturbanceStartTime());
      sql.append(" ,disturbance_end_time = :disturbanceEndTime ");
      params.put("disturbanceEndTime", crDTO.getDisturbanceEndTime());

      sql.append(" ,earliest_start_time = :earliest_start_time ");
      params.put("earliest_start_time", crDTO.getEarliestStartTime());
      sql.append(" ,latest_start_time = :latest_start_time ");
      params.put("latest_start_time", crDTO.getLatestStartTime());
    }

    sql.append(" where cr_id = :cr_id ");
    params.put("cr_id", crId);
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
//    getEntityManager().flush();
    Long returnCode = null;
    try {
      if (StringUtils.isNotNullOrEmpty(crDTO.getActionReturnCodeId())) { //them tranh show log null
        returnCode = Long.valueOf(crDTO.getActionReturnCodeId());
      }
    } catch (Exception e) {
      this.log.error(e.getMessage(), e);
    }

    insertIntoHistoryOfCr(crDTO, crId,
        actionType,
        returnCode,
        Long.valueOf(crDTO.getUserLogin()), Long.valueOf(crDTO.getUserLoginUnit()),
        status, locale);

  }

  @Override
  public boolean validateCloseWoWhenResolveCR(CrInsiteDTO crDTO) {
    boolean result = true;
    String processType = crDTO.getProcessTypeId() == null ? "" : crDTO.getProcessTypeId();
    if (StringUtils.isNotNullOrEmpty(crDTO.getProcessTypeLv3Id())) { //cai nay tiennv bo sung
      processType = crDTO.getProcessTypeLv3Id();
    }
    List<CrWoDTO> lst = getListWOFromCR(crDTO.getCrId(), processType);
    for (CrWoDTO crWoDTO : lst) {
      if (!"8".equals(crWoDTO.getStatus())
          && !"2".equals(crWoDTO.getStatus())) {
        result = false;
      } else if ("2".equals(crWoDTO.getStatus())
          && crWoDTO.getFtId() != null
          && !crWoDTO.getFtId().trim().isEmpty()) {
        result = false;
        break;
      }
      if (!result) {
        List<CatItemDTO> data = catItemRepository.getListItemByCategory("WO_CR_SPEC", "");
        if (data != null && data.size() > 0) {
          for (CatItemDTO item : data) {
            List<String> itemList =
                StringUtils.isStringNullOrEmpty(item.getItemValue()) ? new ArrayList<>()
                    : Arrays.asList(item.getItemValue().trim().split(";"));
            if (itemList.contains(crWoDTO.getWoTypeId())) {
              result = true;
              break;
            }
          }
        }
        if (!result) {
          break;
        }
      }
    }
    return result;
  }

  public List<CrWoDTO> getListWOFromCR(String crId,
      String crProcess) { //tiennv sua check theo dau viec
    List<CrWoDTO> lst = new ArrayList<CrWoDTO>();
    try {
      if (crId == null) {
        return lst;
      }
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DB_DAO, "get-list-wo-from-cr");
      Map<String, Object> params = new HashMap<>();
      params.put("wo_system_id", crId);
//      params.put("cr_id", crId);
      params.put("cr_process_id", Arrays.asList(crProcess.split(",")));

      lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrWoDTO.class));
    } catch (Exception e) {
      //e.printStackTrace();
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List<CrInsiteDTO> processListToGenGr(List<CrInsiteDTO> lst, CrInsiteDTO crDTO,
      String locale) {
//        String locale = crDTO.getLocale();
    for (CrInsiteDTO crDTO1 : lst) {
      crDTO1.setOnTimeAmount(generateTimeLeft(crDTO1, locale));
    }
    return lst;
  }

  @Override
  public Datatable getListCrForRelateOrPreApprove(CrInsiteDTO crDTO) {
    try {
      Map<String, Object> params = new HashMap<>();
      if (crDTO.getRelateCr() == null
          || crDTO.getRelateCr().trim().isEmpty()
          || (!crDTO.getRelateCr().trim().equals(Constants.CR_RELATED.PRE_APPROVE)
          && !crDTO.getRelateCr().trim().equals(Constants.CR_RELATED.SECONDARY))) {
        return null;
      }
      if (crDTO.getUserLogin() == null
          || crDTO.getUserLogin().trim().isEmpty()) {
        return null;
      }
      if (crDTO.getUserLoginUnit() == null
          || crDTO.getUserLoginUnit().trim().isEmpty()) {
        return null;
      }
      StringBuffer sql = new StringBuffer();
      sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DB_DAO, "get-list-cr-relate"));
      if (crDTO.getCrType() != null
          && !crDTO.getCrType().trim().isEmpty()) {
        sql.append(" and cr.cr_type = :cr_type ");
        params.put("cr_type", crDTO.getCrType());
      }
      if (Constants.CR_RELATED.PRE_APPROVE.equals(crDTO.getRelateCr().trim())) {
        sql.append(" and ( (cr.state in (:state_accept, :state_resolve)) ");
        sql.append(" OR ");
        sql.append(" (cr.state = :state_close and cr.cr_return_code_id  is not null))");
        params.put("state_accept", Constants.CR_STATE.ACCEPT);
        params.put("state_resolve", Constants.CR_STATE.RESOLVE);
        params.put("state_close", Constants.CR_STATE.CLOSE);

        sql.append(" and cr.change_orginator_unit in (");
        sql.append(" select utn.unit_id from common_gnoc.unit utn");
        sql.append(" start with unit_id = :unit_idsr ");
        sql.append(" connect by prior unit_id = parent_unit_id");
        sql.append(" )");
        Long unitIdStr = getRealUnit(crDTO.getUserLoginUnit().trim());
        params.put("unit_idsr", unitIdStr);
        sql.append(" and cr.cr_type_cat = :is_pre_approval ");
        params.put("is_pre_approval", Constants.CR_RELATED.IS_PRE_APPROVE);
      }
      if (crDTO.getRelateCr().trim().equals(Constants.CR_RELATED.SECONDARY)) {
//                sql.append(" and usOri.user_id = ?");
//                paramList.add(Long.valueOf(crDTO.getUserLogin().trim()));
        sql.append(" and ((cr.is_primary_cr is not null ");
        sql.append(" and (cr.state <> :state_close ");
        sql.append(" OR (cr.state = :state_close ");
        sql.append(" and cr.cr_return_code_id  is not null)))");
        //cr hoan thanh 1 phan coi la cr cap 1
        sql.append(" OR (cr.state = :state_close and cr.cr_return_code_id in (15,28,29)))");
        params.put("state_close", Constants.CR_STATE.CLOSE);
        //TuanPV14_483701_co the lien ket toi bat ki CR nao
//                sql.append(" and cr.change_orginator_unit = ?");
//                paramList.add(Long.valueOf(crDTO.getUserLoginUnit().trim()));
//                sql.append(" and (cr.cr_type_cat is null OR cr.cr_type_cat <> ? )");
//                paramList.add(Long.valueOf(Constants.CR_RELATED.IS_PRE_APPROVE));
      }
      if (crDTO.getCrNumber() != null
          && !crDTO.getCrNumber().trim().isEmpty()) {
        sql.append(" and lower(cr.cr_number) like :cr_number  ESCAPE '\\'  ");
        params.put("cr_number", StringUtils.convertLowerParamContains(crDTO.getCrNumber()));
      }
      if (crDTO.getTitle() != null
          && !crDTO.getTitle().trim().isEmpty()) {
        sql.append(" and lower(cr.title) like :cr_title  ESCAPE '\\'  ");
        params.put("cr_title", StringUtils.convertLowerParamContains(crDTO.getTitle()));
      }

      return getListDataTableBySqlQuery(sql.toString(), params, crDTO.getPage(),
          crDTO.getPageSize(), CrInsiteDTO.class,
          crDTO.getSortName(), crDTO.getSortType());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public Long getRealUnit(String unitId) {
    try {
      if (unitId != null
          && !"".equals(unitId)) {
        Long unitIdLong = Long.valueOf(unitId);
        List<UnitDTO> lst = getTreeUnit(unitIdLong);
        for (UnitDTO unitDTO : lst) {
          if (unitDTO.getStatus() != null
              && "1".equals(unitDTO.getStatus())) {
            if (unitDTO.getIsNoc() != null && "0".equals(unitDTO.getIsNoc())) {
              return unitDTO.getUnitId();
            }
          }
        }
      }
    } catch (Exception e) {
      this.log.error(e.getMessage(), e);
    }
    return Long.valueOf(unitId);
  }

  public List<UnitDTO> getTreeUnit(Long unitId) {
    List<UnitDTO> lst = new ArrayList<>();
    try {
      if (unitId == null) {
        return lst;
      }
      StringBuilder sql = new StringBuilder("");
      sql.append(
          SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DB_DAO, "get-tree-unit-by-unit-id"));
      Map<String, Object> params = new HashMap<>();
      params.put("unit_id", unitId);
      lst = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(UnitDTO.class));
    } catch (Exception e) {
      this.log.error(e.getMessage(), e);
    }
    return lst;
  }

  private Long parseToLong(Object o) {
    if (o == null || o.toString().isEmpty()) {
      return null;
    }
    try {
      return Long.parseLong(o.toString());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public List<CrInsiteDTO> actionGetListDuplicateCRImpactedNode(CrInsiteDTO crDTO)
      throws Exception {
    List<CrInsiteDTO> lst = new ArrayList<>();
    try {
      Long crId = Long.parseLong(crDTO.getCrId());
      List<Long> listIds = actionGetListIpId(crDTO);
      Date startDate = crDTO.getEarliestStartTime();
      Date endDate = crDTO.getLatestStartTime();
      if (listIds.isEmpty() || startDate == null || endDate == null) {
        return lst;
      }

      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DB_DAO,
          "action-get-list-dupplicate-cr-impact-node");
      Map<String, Object> params = new HashMap<>();
      List<Long> ipIds = new ArrayList<>();
      ipIds.addAll(listIds);
      ipIds.add(0L);
      params.put("startDate", startDate);
      params.put("endDate", endDate);
      params.put("ipx", ipIds);
      params.put("crId", crId);
      params.put("stateAccept", Constants.CR_STATE.ACCEPT);
      params.put("stateResolve", Constants.CR_STATE.RESOLVE);
      params.put("stateClose", Constants.CR_STATE.CLOSE);

      lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrInsiteDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
    return lst;
  }

  @Override
  public Datatable loadCRRelated(CrInsiteDTO crInsiteDTO) {
    Datatable datatable;
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DB_DAO, "load_cr_related");
    params.put("primary_cr", Long.valueOf(crInsiteDTO.getCrId().trim()));
    if (StringUtils.isNotNullOrEmpty(crInsiteDTO.getRelateToPreApprovedCr())) {
      sql += "UNION\n"
          + "select\n"
          + "  '(SECONDARY) ' || cr.title title, cr.cr_id ,\n"
          + "  cr.cr_number ,\n"
          + "  cr.earliest_start_time ,\n"
          + "  cr.latest_start_time ,\n"
          + "  cr.priority priority,\n"
          + "  case when utOri.unit_code is null then ''\n"
          + "      else TO_CHAR(utOri.unit_code || ' ('||utOri.unit_name||')') end as changeOrginatorUnitName,\n"
          + "  case when usOri.username is null then ''\n"
          + "      else TO_CHAR(usOri.username || ' ('||usOri.fullname||')') end as changeOrginatorName,\n"
          + "  case when utResp.unit_code is null then ''\n"
          + "      else TO_CHAR(utResp.unit_code || ' ('||utResp.unit_name||')') end as changeResponsibleUnitName,\n"
          + "  case when usResp.username is null then ''\n"
          + "      else TO_CHAR(usResp.username || ' ('||usResp.fullname||')') end as changeResponsibleName,\n"
          + "  cr.state,\n"
          + "  cr.change_responsible_unit ,\n"
          + "  cr.consider_unit_id ,\n"
          + "  cr.consider_user_id\n"
          + "from open_pm.cr cr\n"
          + "left join common_gnoc.unit utOri on cr.change_orginator_unit = utOri.unit_id\n"
          + "left join common_gnoc.users usOri on cr.change_orginator = usOri.user_id\n"
          + "left join common_gnoc.unit utResp on cr.change_responsible_unit = utResp.unit_id\n"
          + "left join common_gnoc.users usResp on cr.change_responsible = usResp.user_id\n"
          + "where usOri.is_enable = 1 and (cr_id = :cr_id) ";
      params.put("cr_id", Long.valueOf(crInsiteDTO.getRelateToPreApprovedCr().trim()));
    }

    datatable = getListDataTableBySqlQuery(sql, params,
        crInsiteDTO.getPage(), crInsiteDTO.getPageSize(),
        CrInsiteDTO.class, crInsiteDTO.getSortName(),
        crInsiteDTO.getSortType());
    return datatable;
  }

  /**
   *
   */
  public List<Long> actionGetListIpId(CrInsiteDTO crDTO) {
    List<Long> listIpId = new ArrayList<>();
    try {
      if (crDTO.getCrId() == null || crDTO.getCreatedDate() == null) {
        return listIpId;
      }
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DB_DAO, "action-get-list-ip-id");
      Map<String, Object> params = new HashMap<>();
      params.put("insert_time", DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getCreatedDate()));
      params.put("crId", crDTO.getCrId());
      List<CrInsiteDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrInsiteDTO.class));
      if (!lst.isEmpty()) {
        for (CrInsiteDTO dTO : lst) {
          String ipIdStr = dTO.getScopeId();
          Long ipId = Long.valueOf(ipIdStr);
          if (!listIpId.contains(ipId)) {
            listIpId.add(ipId);
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return listIpId;
  }

  @Override
  public void updateCrStatusInCaseOfScheduleNoSession(Long actionType, Long crId, CrInsiteDTO crDTO,
      String locale) throws Exception {
    if (actionType == null || crId == null) {
      return;
    }
    CrEntity crModel = crDTO.toEntity();
    Long status = CrGeneralUtil.generateStateForSchedule(crDTO, actionType);
    CrEntity crEntity = getEntityManager().find(CrEntity.class, crId);
    crEntity.setState(status);
    crEntity.setUpdateTime(new Date());
    if (crModel.getPriority() != null) {
      crEntity.setPriority(crModel.getPriority());
    }
    if (crModel.getEarliestStartTime() != null) {
      crEntity.setEarliestStartTime(crModel.getEarliestStartTime());
    }
    if (crModel.getLatestStartTime() != null) {
      crEntity.setLatestStartTime(crModel.getLatestStartTime());
    }

    if (crModel.getDisturbanceStartTime() != null) {
      crEntity.setDisturbanceStartTime(crModel.getDisturbanceStartTime());
    } else {
      crEntity.setDisturbanceStartTime(null);
    }

    if (crModel.getDisturbanceEndTime() != null) {
      crEntity.setDisturbanceEndTime(crModel.getDisturbanceEndTime());
    } else {
      crEntity.setDisturbanceEndTime(null);
    }
    if (crDTO.getDutyType() != null) {
      crEntity.setDutyType(crModel.getDutyType());
    }

    if ("1".equals(String.valueOf(crModel.getServiceAffecting()))) {
      crEntity.setServiceAffecting(1L);
    } else {
      crEntity.setServiceAffecting(0L);
    }

    if (crModel.getChangeResponsibleUnit() != null) {
      crEntity.setChangeResponsibleUnit(crModel.getChangeResponsibleUnit());
    }
    //R846092_start
    if (crModel.getChangeResponsible() != null) {
      crEntity.setChangeResponsible(crModel.getChangeResponsible());
    }

    if (StringUtils.isNotNullOrEmpty(crDTO.getCircleAdditionalInfo())) {
      crEntity.setCircleAdditionalInfo(crDTO.getCircleAdditionalInfo());
    }

    if (crDTO.getTotalAffectedCustomers() != null) {
      crEntity.setTotalAffectedCustomers(crModel.getTotalAffectedCustomers());
    } else {
      crEntity.setTotalAffectedCustomers(null);
    }

    if (StringUtils.isNotNullOrEmpty(crDTO.getTotalAffectedMinutes())) {
      crEntity.setTotalAffectedMinutes(crModel.getTotalAffectedMinutes());
    } else {
      crEntity.setTotalAffectedMinutes(null);
    }

    //R846092_end
        /*if (actionType.equals(Constants.CR_ACTION_CODE.SCHEDULE)
         && !Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
         sql.append(" ,manage_unit_id = ?");
         sql.append(" ,manage_user_id = ?");
         paramList.add(crDTO.getUserLoginUnit());
         paramList.add(crDTO.getUserLogin());

         }*/
    if (actionType.equals(Constants.CR_ACTION_CODE.SCHEDULE)) {

      if (!Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
        crEntity.setManageUnitId(Long.valueOf(crDTO.getUserLoginUnit()));
        crEntity.setManageUserId(Long.valueOf(crDTO.getUserLogin()));

        if ("1".equals(crDTO.getIsTracingCr())) {
          if (StringUtils.isStringNullOrEmpty(crDTO.getChangeResponsible())) {
            crEntity.setChangeResponsible(null);
          }
        }
      }

      if ((crDTO.getAutoExecute() == null) || (crDTO.getAutoExecute().trim().isEmpty())) {
        crEntity.setAutoExecute(null);
      } else {
        crEntity.setAutoExecute(Long.valueOf(crDTO.getAutoExecute().trim()));
      }

      if (crDTO.getIsRunType() == null) {
        crEntity.setIsRunType(null);
      } else {
        crEntity.setIsRunType(crDTO.getIsRunType());
      }
    }

    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER_SCH)) {
      crEntity.setManageUnitId(null);
      crEntity.setManageUserId(null);
      crEntity.setUserCab(null);
      crEntity.setConsiderUnitId(null);
      crEntity.setConsiderUserId(null);
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_APPRAISE_BY_MANAGER_SCH)) {
      crEntity.setUserCab(null);
      crEntity.setConsiderUserId(null);
    }
    //tuanpv14_cap nhat cr lien quan_start
    if (crDTO.getRelateToPrimaryCr() != null && !crDTO.getRelateToPrimaryCr().trim().isEmpty()) {
      crEntity.setRelateToPrimaryCr(Long.valueOf(crDTO.getRelateToPrimaryCr()));
    }
    if (crDTO.getIsPrimaryCr() != null && !crDTO.getIsPrimaryCr().trim().isEmpty()) {
      crEntity.setIsPrimaryCr(Long.valueOf(crDTO.getIsPrimaryCr()));
      crEntity.setRelateToPrimaryCr(null);
    }

    if (crDTO.getIsConfirmAction() != null) {
      crEntity.setIsConfirmAction(crDTO.getIsConfirmAction());
    }

    getEntityManager().merge(crEntity);
    Long returnCode = null;
    try {
      if (StringUtils.isNotNullOrEmpty(crDTO.getActionReturnCodeId())) { //them tranh show log null
        returnCode = Long.valueOf(crDTO.getActionReturnCodeId());
      }
    } catch (Exception e) {
      this.log.error(e.getMessage(), e);
    }
    insertIntoHistoryOfCr(crDTO, crId,
        actionType,
        returnCode,
        Long.valueOf(crDTO.getUserLogin()), Long.valueOf(crDTO.getUserLoginUnit()),
        //                    Long.valueOf(crDTO.getState().trim())
        status, locale);
  }

  /**
   * Cap nhat Cr tiep nhan thuc thi
   */
  @Override
  public void updateCrStatusInCaseOfReceivingNoSession(Long actionType, Long crId,
      CrInsiteDTO crDTO,
      String locale) throws Exception {
    if (actionType == null || crId == null) {
      return;
    }

    boolean isManager = false;
    try {
      isManager = userRepository.isManagerOfUnits(Long.valueOf(crDTO.getUserLogin()));
    } catch (Exception e1) {
      this.log.error(e1.getMessage(), e1);
    }

    Long status = CrGeneralUtil.generateStateForReceive(actionType);
    CrEntity crEntity = getEntityManager().find(CrEntity.class, crId);
    crEntity.setState(status);
    crEntity.setUpdateTime(new Date());

    if (actionType.equals(Constants.CR_ACTION_CODE.ASSIGN_EXC_TO_EMPLOYEE)) {
      crEntity.setChangeResponsible(
          crDTO.getAssignUserId() == null ? null : Long.valueOf(crDTO.getAssignUserId()));
      if (Constants.CR_TYPE.STANDARD.toString().equals(crDTO.getCrType())) {
        crEntity.setManageUnitId(Long.parseLong(crDTO.getUserLoginUnit()));
        crEntity.setManageUserId(Long.parseLong(crDTO.getUserLogin()));
      }
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.ACCEPT)) {
      crEntity.setChangeResponsible(
          crDTO.getUserLogin() == null ? null : Long.valueOf(crDTO.getUserLogin()));
      //bo sung them tiep nhan
      crEntity.setIsRunType(crDTO.getIsRunType());
      crEntity.setIsConfirmAction(crDTO.getIsConfirmAction());
      crEntity.setAutoExecute(StringUtils.isStringNullOrEmpty(crDTO.getAutoExecute()) ? null
          : Long.valueOf(crDTO.getAutoExecute()));

      if (isManager && Constants.CR_TYPE.STANDARD.toString().equals(crDTO.getCrType())) {
        crEntity.setManageUnitId(Long.parseLong(crDTO.getUserLoginUnit()));
        crEntity.setManageUserId(Long.parseLong(crDTO.getUserLogin()));
      }
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_MANAGER_BY_IMPL)) {
      crEntity.setManageUnitId(null);
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_APPRAISER_BY_IMPL)) {
      crEntity.setConsiderUserId(null);
      crEntity.setUserCab(null);
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_WHEN_EXCUTE_STD)) {
      crEntity.setManageUnitId(null);
      crEntity.setManageUserId(null);
      crEntity.setUserCab(null);
      crEntity.setConsiderUnitId(null);
      crEntity.setConsiderUserId(null);

    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RESOLVE_WITH_FAILT_STATUS_DUE_TO_WO)) {
      crEntity.setFailDueToFT(
          crDTO.getFailDueToFT() == null ? null : Long.valueOf(crDTO.getFailDueToFT()));
    }

    boolean isExcuteUpdate = true;
    if (actionType.equals(Constants.CR_ACTION_CODE.ASSIGN_EXC_TO_EMPLOYEE)) {
      String oldCrResponse = crDTO.getChangeResponsible();
      String assignee = crDTO.getAssignUserId();
      if (oldCrResponse != null && assignee != null
          && assignee.trim().toLowerCase().
          equals(oldCrResponse.trim().toLowerCase())) {
        isExcuteUpdate = false;
      }
    }
    if (isExcuteUpdate) {
      getEntityManager().merge(crEntity);
    }
    Long returnCode = null;
    try {
      if (StringUtils.isNotNullOrEmpty(crDTO.getActionReturnCodeId())) { //them tranh show log null
        returnCode = Long.valueOf(crDTO.getActionReturnCodeId());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    //R_xxx uyquyen _ Ghi chu ro rang khi BGD thay quyen
    insertIntoHistoryOfCr(crDTO, crId,
        actionType,
        returnCode,
        Long.valueOf(crDTO.getUserLogin()), Long.valueOf(crDTO.getUserLoginUnit()),//R_xxx uyquyen
        //                    Long.valueOf(crDTO.getState().trim())
        status, locale);
  }

  @Override
  public void updateCrStatusInCaseOfApproveNoSession(Long actionType,
      Long crId,
      CrInsiteDTO crDTO, String locale) throws Exception {
    if (actionType == null || crId == null) {
      return;
    }
    CrEntity crEntity = getEntityManager().find(CrEntity.class, crId);
    Long status = CrGeneralUtil.generateStateForApproveCr(crDTO, actionType);
    if (crEntity != null) {
      crEntity.setState(status);
      crEntity.setUpdateTime(new Date());
      StringBuilder sql = new StringBuilder("");

      if (status.equals(Constants.CR_STATE.APPROVE)
          || status.equals(Constants.CR_STATE.RESOLVE)) {
        crEntity.setManageUnitId(Long.parseLong(crDTO.getUserLoginUnit()));
        crEntity.setManageUserId(Long.parseLong(crDTO.getUserLogin()));
      }
      if (Constants.CR_TYPE.STANDARD.toString().equals(crDTO.getCrType()) && "121"
          .equals(crDTO.getImpactSegment())
          && Constants.CR_ACTION_CODE.APPROVE.equals(actionType)) {
        CrEntity crModel = crDTO.toEntity();
        crEntity.setEarliestStartTime(crModel.getEarliestStartTime());
        crEntity.setLatestStartTime(crModel.getLatestStartTime());

        if (!StringUtils.isStringNullOrEmpty(crDTO.getDisturbanceEndTime()) && !StringUtils
            .isStringNullOrEmpty(crDTO.getDisturbanceStartTime())) {
          crEntity.setDisturbanceEndTime(crModel.getDisturbanceEndTime());
          crEntity.setDisturbanceStartTime(crModel.getDisturbanceStartTime());
        }
      }
      getEntityManager().merge(crEntity);
    }
    Long returnCode = null;

    try {
      if (StringUtils.isNotNullOrEmpty(crDTO.getActionReturnCodeId())) { //them tranh show log null
        returnCode = Long.valueOf(crDTO.getActionReturnCodeId());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    //R_xxx uyquyen _ TH BGD tu choi level 1 -> Ghi lich su chu thich ro rang
    insertIntoHistoryOfCr(crDTO, crId, actionType,
        returnCode,
        Long.valueOf(crDTO.getUserLogin()), Long.valueOf(crDTO.getUserLoginUnit()),//R_xxx uyquyen
        //                            Long.valueOf(crDTO.getState().trim())
        status, locale);
  }

  @Override
  public List<CrDTO> processListToGenGrService(List<CrDTO> lst, CrInsiteDTO crDTO,
      String locale) {
//        String locale = crDTO.getLocale();
    for (CrDTO crDTO1 : lst) {
      crDTO1.setOnTimeAmount(generateTimeLeftService(crDTO1, locale));
    }
    return lst;
  }

  public String generateTimeLeftService(CrDTO crDTO,
      String locale) {
    String result = "";
    try {
      String compareDate = crDTO.getCompareDate();
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      Date dateCompare = StringUtils.isStringNullOrEmpty(crDTO.getCompareDate()) ? null
          : sdf.parse(crDTO.getCompareDate());
//            if (compareDate == null || compareDate.trim().isEmpty()) {
//                return result;
//            }
      int amount = 0;
      String state = crDTO.getState();
      if (state.equals(Constants.CR_STATE.CANCEL.toString())
          || state.equals(Constants.CR_STATE.CLOSE.toString())
          || state.equals(Constants.CR_STATE.DRAFT.toString())
          || state.equals(Constants.CR_STATE.OPEN.toString())) {
        return result;
      }
      if (state.equals(Constants.CR_STATE.QUEUE.toString())
          || state.equals(Constants.CR_STATE.COORDINATE.toString())
          || state.equals(Constants.CR_STATE.EVALUATE.toString())
          || state.equals(Constants.CR_STATE.APPROVE.toString())) {
        dateCompare = DateTimeUtils.convertStringToDate(crDTO.getEarliestStartTime());
      }
      if (state.equals(Constants.CR_STATE.ACCEPT.toString())) {
        dateCompare = DateTimeUtils.convertStringToDate(crDTO.getLatestStartTime());
        amount = 4;
      }
      if (state.equals(Constants.CR_STATE.RESOLVE.toString())) {
        if (StringUtils.isStringNullOrEmpty(compareDate)) {
          dateCompare = DateTimeUtils.convertStringToDate(crDTO.getLatestStartTime());
        }
        amount = 24;
      }
      boolean flag = true;

      dateCompare = plusNHours(dateCompare, amount);
      Date dateNow = new Date();
      long t = dateNow.getTime() - dateCompare.getTime();
      if (t < 0) {
        t = 0 - t;
        flag = false;
      }
      long days = (long) t / (24 * 3600 * 1000);
      long hours = (long) (t - (days * (24 * 3600 * 1000))) / (3600 * 1000);
      long minutes = (long) (t - (days * (24 * 3600 * 1000)) - hours * 3600 * 1000) / (60 * 1000);
      Locale local;
      if (StringUtils.isNotNullOrEmpty(locale)) {
        String[] lang_loc = locale.split("_");
        if (lang_loc.length > 1) {
          local = new Locale(lang_loc[0], lang_loc[1]);
        } else {
          local = new Locale(locale);
        }
      } else {
        local = new Locale("en", "US");
      }
      String dayslbl = I18n.getLanguageByLocale(local, "cr.days");
      String hourlbl = I18n.getLanguageByLocale(local, "cr.hours");
      String minlbl = I18n.getLanguageByLocale(local, "cr.minutes");
      if (!flag) {
        result += "-";
      }
      boolean flagD = days != 0;
      boolean flagH = hours != 0;
      if (days != 0) {
        result += days + " " + dayslbl;
      }
      if (flagD) {
        result += " " + hours + " " + hourlbl;
      } else if (hours != 0) {
        result += hours + " " + hourlbl;
      }
      if (flagH || flagD) {
        result += " " + minutes + " " + minlbl;
      } else {
        result += minutes + " " + minlbl;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  @Override
  public List<CrDTO> getListPreApprovedCrOutSide(CrDTO crDTO) {
    List<CrDTO> list;
    try {
      if (StringUtils.isStringNullOrEmpty(crDTO.getCrId())) {
        return null;
      }
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DB_DAO, "get-list-pre-approved-cr-outside");
      sql += " and cr.relate_to_pre_approved_cr = :primary_cr ";
      Map<String, Object> params = new HashMap<>();
      params.put("primary_cr", Long.valueOf(crDTO.getCrId().trim()));
      list = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrDTO.class));
      return list;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<CrDTO> getListSecondaryCrOutSide(CrDTO crDTO) {
    List<CrDTO> list;
    try {
      //List<CrDTO> list = new ArrayList<CrDTO>();
      if (StringUtils.isStringNullOrEmpty(crDTO.getCrId())) {
        return null;
      }
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DB_DAO, "get-list-secondary-cr-out-side");
      sql += " and cr.relate_to_primary_cr = :primary_cr ";
      Map<String, Object> params = new HashMap<>();
      params.put("primary_cr", Long.valueOf(crDTO.getCrId().trim()));
      list = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrDTO.class));
      return list;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }


  /**
   * Cap nhat Cr luc kiem tra dau vao
   */
  @Override
  public void updateCrStatusInCaseOfVerifyMrIT(Long actionType, Long crId, CrInsiteDTO crDTO,
      String locale) throws Exception {
    if (actionType == null || crId == null) {
      return;
    }
    Map<String, Object> params = new HashMap<>();
    Long status = CrGeneralUtil.generateStateForVerifyCR(crDTO, actionType);
    StringBuilder sql = new StringBuilder("");
    sql.append(" update cr set state = :state , update_time = :update_time ");
    params.put("state", status);
    params.put("update_time", new Date());
    sql.append(", manage_unit_id = null, manage_user_id = null ");

//    if (crDTO.getDutyType() != null) {
//      sql.append(" ,DUTY_TYPE = :duty_type ");
//      params.put("duty_type", crDTO.getDutyType());
//    }

    if (crDTO.getEarliestStartTime() != null) {
      sql.append(" , earliest_start_time = :earliest_start_time ");
      params.put("earliest_start_time", crDTO.getEarliestStartTime());
    }
    if (crDTO.getLatestStartTime() != null) {
      sql.append(" , latest_start_time = :latest_start_time ");
      params.put("latest_start_time", crDTO.getLatestStartTime());
    }
    if (crDTO.getDisturbanceStartTime() != null) {
      sql.append(" , disturbance_start_time = :disturbance_start_time ");
      params.put("disturbance_start_time", crDTO.getDisturbanceStartTime());
    }
    if (crDTO.getDisturbanceEndTime() != null) {
      sql.append(" , disturbance_end_time = :disturbance_end_time ");
      params.put("disturbance_end_time", crDTO.getDisturbanceEndTime());
    }
    //tuanpv14_cap nhat cr lien quan_start
    if (StringUtils.isNotNullOrEmpty(crDTO.getRelateToPrimaryCr())) {
      sql.append(" , relate_to_primary_cr = :relate_to_primary_cr ");
//            sql.append(" , is_primary_cr = null");
      params.put("relate_to_primary_cr", crDTO.getRelateToPrimaryCr());
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getIsPrimaryCr())) {
      sql.append(" , is_primary_cr = :is_primary_cr");
      sql.append(" , relate_to_primary_cr = null ");
      params.put("is_primary_cr", crDTO.getIsPrimaryCr());
    }
    //tuanpv14_cap nhat cr lien quan_end
    if (Constants.CR_STATE.COORDINATE.equals(status)) {
      sql.append("  ,consider_unit_id = :consider_unit_id ");
      params.put("consider_unit_id", Long.parseLong(crDTO.getConsiderUnitId()));
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getUserCab())) {
      sql.append(" , user_cab = :user_cab ");
      params.put("user_cab", crDTO.getUserCab());
    }

    sql.append(" where cr_id = :cr_id ");
    params.put("cr_id", crId);
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
//    getEntityManager().flush();
    Long returnCode = null;
    try {
      if (StringUtils.isNotNullOrEmpty(crDTO.getActionReturnCodeId())) { //them tranh show log null
        returnCode = Long.valueOf(crDTO.getActionReturnCodeId());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    insertIntoHistoryOfCr(crDTO, crId,
        actionType,
        returnCode,
        Long.valueOf(crDTO.getUserLogin()), Long.valueOf(crDTO.getUserLoginUnit()),
        //                    Long.valueOf(crDTO.getState().trim())
        status, locale);
  }

  /**
   * Cap nhat lai thoi gian CR khi hoan thanh CR(case update T92020)
   */
  @Override
  public void updateCrTimeInCaseResolve(CrInsiteDTO crDTO, boolean isRollback) {
    if (StringUtils.isStringNullOrEmpty(crDTO.getCrId())) {
      return;
    }
    Map<String, Object> params = new HashMap<>();
    StringBuilder sql = new StringBuilder("");
    sql.append(" update cr set update_time = :update_time ");
    if (crDTO.getEarliestStartTime() != null) {
      sql.append(" , earliest_start_time = :earliest_start_time ");
      params.put("earliest_start_time", crDTO.getEarliestStartTime());
    }
    if (crDTO.getLatestStartTime() != null) {
      sql.append(" , latest_start_time = :latest_start_time ");
      params.put("latest_start_time", crDTO.getLatestStartTime());
    }
    if (crDTO.getDisturbanceStartTime() != null) {
      sql.append(" , disturbance_start_time = :disturbance_start_time ");
      params.put("disturbance_start_time", crDTO.getDisturbanceStartTime());
    }
    if (crDTO.getDisturbanceEndTime() != null) {
      sql.append(" , disturbance_end_time = :disturbance_end_time ");
      params.put("disturbance_end_time", crDTO.getDisturbanceEndTime());
    }
    sql.append(" where cr_id = :cr_id ");
    params.put("cr_id", Long.valueOf(crDTO.getCrId()));
    params.put("update_time",
        (isRollback && crDTO.getUpdateTime() != null) ? crDTO.getUpdateTime() : new Date());
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
  }

}
