package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrDTO;
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
public class CrProvinceMonitorRepositoryImpl extends BaseRepository implements
    CrProvinceMonitorRepository {

  @Override
  public List<ResultDTO> actionGetProvinceMonitoringParam(String unitId,
      String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> list = new ArrayList<>();
    try {
      List<ResultDTO> listApprove = getListApprove(unitId, searchChild, startDate, endDate);
      List<ResultDTO> listVerify = getListVerify(unitId, searchChild, startDate, endDate);
      List<ResultDTO> listConsider = getListConsider(unitId, searchChild, startDate, endDate);
      List<ResultDTO> listSchedule = getListSchedule(unitId, searchChild, startDate, endDate);
      List<ResultDTO> listReceive = getListReceive(unitId, searchChild, startDate, endDate);
      //tuanpv14 Gnoc mobile bo thong ke kpi cho hoan thanh
//            List<ResultDTO> listResolve = provinceMonitoringDBDAO.getListResolve(sess, unitId,searchChild,startDate,endDate);
      List<ResultDTO> listClose = getListClose(unitId, searchChild, startDate, endDate);
      list.addAll(listApprove);
      list.addAll(listVerify);
      list.addAll(listConsider);
      list.addAll(listSchedule);
      list.addAll(listReceive);
//            list.addAll(listResolve);
      list.addAll(listClose);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public List<ResultDTO> getListApprove(String unit, String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> list = new ArrayList<>();
    try {
//      String approveOntimeNormal = Constants.NOTIFY_TYPE.APPROVE_STILL_IN_TIME;
//      String approveOntimeStandard = Constants.NOTIFY_TYPE.APPROVE_STILL_IN_TIME_STANDARD;
//      String approveOntimeEmg = Constants.NOTIFY_TYPE.APPROVE_STILL_IN_TIME_EMG;
//      String approveOutOfDateNormal = Constants.NOTIFY_TYPE.APPROVE_OUT_OF_DATE;
//      String approveOutOfDateStandard = Constants.NOTIFY_TYPE.APPROVE_OUT_OF_DATE_STANDARD;
//      String approveOutOfDateEmg = Constants.NOTIFY_TYPE.APPROVE_OUT_OF_DATE_EMG;
      String total = Constants.NOTIFY_TYPE.TOTAL_APPROVE;
      String totalEmg = Constants.NOTIFY_TYPE.TOTAL_APPROVE_EMG;
      String totalStandard = Constants.NOTIFY_TYPE.TOTAL_APPROVE_STANDARD;
      Long countApproveOntimeNormal;
      Long countApproveOntimeStandard;
      Long countApproveOntimeEmg;
      Long countApproveOutOfDateNormal;
      Long countApproveOutOfDateStandard;
      Long countApproveOutOfDateEmg;
      Long countTotal;
      Long countTotalEmg;
      Long countTotalStandard;
      List<CrDTO> listApproveOnTime = actionGetTotalCRwaitingForApproveVer2(unit, false,
          searchChild, startDate, endDate);
      List<CrDTO> listApproveOutOfDate = actionGetTotalCRwaitingForApproveVer2(unit, true,
          searchChild, startDate, endDate);
      countApproveOntimeNormal = getCount(listApproveOnTime, Constants.CR_TYPE.NORMAL);
      countApproveOntimeStandard = getCount(listApproveOnTime, Constants.CR_TYPE.STANDARD);
      countApproveOntimeEmg = getCount(listApproveOnTime, Constants.CR_TYPE.EMERGENCY);
      countApproveOutOfDateNormal = getCount(listApproveOutOfDate, Constants.CR_TYPE.NORMAL);
      countApproveOutOfDateStandard = getCount(listApproveOutOfDate, Constants.CR_TYPE.STANDARD);
      countApproveOutOfDateEmg = getCount(listApproveOutOfDate, Constants.CR_TYPE.EMERGENCY);
      countTotal = countApproveOntimeNormal + countApproveOutOfDateNormal;
      countTotalEmg = countApproveOntimeEmg + countApproveOutOfDateEmg;
      countTotalStandard = countApproveOntimeStandard + countApproveOutOfDateStandard;
      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setId(total);
      resultDTO
          .setQuantitySucc(StringUtils.isStringNullOrEmpty(countTotal) ? 0 : countTotal.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.APPROVE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalEmg);
      resultDTO.setQuantitySucc(
          StringUtils.isStringNullOrEmpty(countTotalEmg) ? 0 : countTotalEmg.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.APPROVE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalStandard);
      resultDTO.setQuantitySucc(
          StringUtils.isStringNullOrEmpty(countTotalStandard) ? 0 : countTotalStandard.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.APPROVE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public List<ResultDTO> getListVerify(String unit, String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> list = new ArrayList<ResultDTO>();
    try {
//      String verifyOntimeNormal = Constants.NOTIFY_TYPE.VERIFY_IN_TIME;
//      String verifyOntimeStandard = Constants.NOTIFY_TYPE.VERIFY_IN_TIME_STANDARD;
//      String verifyOntimeEmg = Constants.NOTIFY_TYPE.VERIFY_IN_TIME_EMG;
//      String verifyOutOfDateNormal = Constants.NOTIFY_TYPE.VERIFY_OUT_OF_DATE;
//      String verifyOutOfDateStandard = Constants.NOTIFY_TYPE.VERIFY_OUT_OF_DATE_STANDARD;
//      String verifyOutOfDateEmg = Constants.NOTIFY_TYPE.VERIFY_OUT_OF_DATE_EMG;
      String total = Constants.NOTIFY_TYPE.TOTAL_VERIFY;
      String totalEmg = Constants.NOTIFY_TYPE.TOTAL_VERIFY_EMG;
      String totalStandard = Constants.NOTIFY_TYPE.TOTAL_VERIFY_STANDARD;
      Long countVerifyOntimeNormal;
      Long countVerifyOntimeStandard;
      Long countVerifyOntimeEmg;
      Long countVerifyOutOfDateNormal;
      Long countVerifyOutOfDateStandard;
      Long countVerifyOutOfDateEmg;
      Long countTotal;
      Long countTotalEmg;
      Long countTotalStandard;
      List<CrDTO> listVerifyOnTime = actionGetTotalCRwaitingForVerifyOrScheduleVer2(unit, false,
          Constants.CR_SEARCH_TYPE.VERIFY, searchChild, startDate, endDate);
      List<CrDTO> listVerifyOutOfDate = actionGetTotalCRwaitingForVerifyOrScheduleVer2(unit, true,
          Constants.CR_SEARCH_TYPE.VERIFY, searchChild, startDate, endDate);
      countVerifyOntimeNormal = getCount(listVerifyOnTime, Constants.CR_TYPE.NORMAL);
      countVerifyOntimeStandard = getCount(listVerifyOnTime, Constants.CR_TYPE.STANDARD);
      countVerifyOntimeEmg = getCount(listVerifyOnTime, Constants.CR_TYPE.EMERGENCY);
      countVerifyOutOfDateNormal = getCount(listVerifyOutOfDate, Constants.CR_TYPE.NORMAL);
      countVerifyOutOfDateStandard = getCount(listVerifyOutOfDate, Constants.CR_TYPE.STANDARD);
      countVerifyOutOfDateEmg = getCount(listVerifyOutOfDate, Constants.CR_TYPE.EMERGENCY);
      countTotal = countVerifyOntimeNormal + countVerifyOutOfDateNormal;
      countTotalEmg = countVerifyOntimeEmg + countVerifyOutOfDateEmg;
      countTotalStandard = countVerifyOntimeStandard + countVerifyOutOfDateStandard;
      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setId(total);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.VERIFY.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      resultDTO
          .setQuantitySucc(StringUtils.isStringNullOrEmpty(countTotal) ? 0 : countTotal.intValue());
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalEmg);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.VERIFY.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      resultDTO.setQuantitySucc(
          StringUtils.isStringNullOrEmpty(countTotalEmg) ? 0 : countTotalEmg.intValue());
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalStandard);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.VERIFY.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      resultDTO.setQuantitySucc(
          StringUtils.isStringNullOrEmpty(countTotalStandard) ? 0 : countTotalStandard.intValue());
      list.add(resultDTO);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public List<ResultDTO> getListConsider(String unit, String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> list = new ArrayList<ResultDTO>();
    try {
//      String considerOntimeNormal = Constants.NOTIFY_TYPE.WAITING_FOR_CONSIDER_INTIME;
//      String considerOntimeStandard = Constants.NOTIFY_TYPE.WAITING_FOR_CONSIDER_INTIME_STANDARD;
//      String considerOntimeEmg = Constants.NOTIFY_TYPE.WAITING_FOR_CONSIDER_INTIME_EMG;
//      String considerOutOfDateNormal = Constants.NOTIFY_TYPE.WAITING_FOR_CONSIDER_OUT_OF_DATE;
//      String considerOutOfDateStandard = Constants.NOTIFY_TYPE.WAITING_FOR_CONSIDER_OUT_OF_DATE_STANDARD;
//      String considerOutOfDateEmg = Constants.NOTIFY_TYPE.WAITING_FOR_CONSIDER_OUT_OF_DATE_EMG;
      String total = Constants.NOTIFY_TYPE.TOTAL_CONSIDER;
      String totalEmg = Constants.NOTIFY_TYPE.TOTAL_CONSIDER_EMG;
      String totalStandard = Constants.NOTIFY_TYPE.TOTAL_CONSIDER_STANDARD;
      Long countConsiderOntimeNormal;
      Long countConsiderOntimeStandard;
      Long countConsiderOntimeEmg;
      Long countConsiderOutOfDateNormal;
      Long countConsiderOutOfDateStandard;
      Long countConsiderOutOfDateEmg;
      Long countTotal;
      Long countTotalEmg;
      Long countTotalStandard;
      List<CrDTO> listConsiderOnTime = actionGetTotalCRwaitingForConsiderReceiveResolveOrCloseVer2(
          unit, false, true, Constants.CR_SEARCH_TYPE.CONSIDER, searchChild, startDate, endDate);
      List<CrDTO> listConsiderOutOfDate = actionGetTotalCRwaitingForConsiderReceiveResolveOrCloseVer2(
          unit, true, true, Constants.CR_SEARCH_TYPE.CONSIDER, searchChild, startDate, endDate);
      countConsiderOntimeNormal = getCount(listConsiderOnTime, Constants.CR_TYPE.NORMAL);
      countConsiderOntimeStandard = getCount(listConsiderOnTime, Constants.CR_TYPE.STANDARD);
      countConsiderOntimeEmg = getCount(listConsiderOnTime, Constants.CR_TYPE.EMERGENCY);
      countConsiderOutOfDateNormal = getCount(listConsiderOutOfDate, Constants.CR_TYPE.NORMAL);
      countConsiderOutOfDateStandard = getCount(listConsiderOutOfDate, Constants.CR_TYPE.STANDARD);
      countConsiderOutOfDateEmg = getCount(listConsiderOutOfDate, Constants.CR_TYPE.EMERGENCY);
      countTotal = countConsiderOntimeNormal + countConsiderOutOfDateNormal;
      countTotalEmg = countConsiderOntimeEmg + countConsiderOutOfDateEmg;
      countTotalStandard = countConsiderOntimeStandard + countConsiderOutOfDateStandard;
      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setId(total);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.CONSIDER.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      resultDTO
          .setQuantitySucc(StringUtils.isStringNullOrEmpty(countTotal) ? 0 : countTotal.intValue());
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalEmg);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.CONSIDER.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      resultDTO.setQuantitySucc(
          StringUtils.isStringNullOrEmpty(countTotalEmg) ? 0 : countTotalEmg.intValue());
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalStandard);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.CONSIDER.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      resultDTO.setQuantitySucc(
          StringUtils.isStringNullOrEmpty(countTotalStandard) ? 0 : countTotalStandard.intValue());
      list.add(resultDTO);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public List<ResultDTO> getListSchedule(String unit, String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> list = new ArrayList<>();
    try {
//      String scheduleOntimeNormal = Constants.NOTIFY_TYPE.WAITING_FOR_SCHEDULE_INTIME;
//      String scheduleOntimeStandard = Constants.NOTIFY_TYPE.WAITING_FOR_SCHEDULE_INTIME_STANDARD;
//      String scheduleOntimeEmg = Constants.NOTIFY_TYPE.WAITING_FOR_SCHEDULE_INTIME_EMG;
//      String scheduleOutOfDateNormal = Constants.NOTIFY_TYPE.WAITING_FOR_SCHEDULE_OUT_OF_DATE;
//      String scheduleOutOfDateStandard = Constants.NOTIFY_TYPE.WAITING_FOR_SCHEDULE_OUT_OF_DATE_STANDARD;
//      String scheduleOutOfDateEmg = Constants.NOTIFY_TYPE.WAITING_FOR_SCHEDULE_OUT_OF_DATE_EMG;
      String total = Constants.NOTIFY_TYPE.TOTAL_SCHEDULE;
      String totalEmg = Constants.NOTIFY_TYPE.TOTAL_SCHEDULE_EMG;
      String totalStandard = Constants.NOTIFY_TYPE.TOTAL_SCHEDULE_STANDARD;
      Long countScheduleOntimeNormal;
      Long countScheduleOntimeStandard;
      Long countScheduleOntimeEmg;
      Long countScheduleOutOfDateNormal;
      Long countScheduleOutOfDateStandard;
      Long countScheduleOutOfDateEmg;
      Long countTotal;
      Long countTotalEmg;
      Long countTotalStandard;
      List<CrDTO> listScheduleOnTime = actionGetTotalCRwaitingForVerifyOrScheduleVer2(unit, false,
          Constants.CR_SEARCH_TYPE.SCHEDULE, searchChild, startDate, endDate);
      List<CrDTO> listScheduleOutOfDate = actionGetTotalCRwaitingForVerifyOrScheduleVer2(unit, true,
          Constants.CR_SEARCH_TYPE.SCHEDULE, searchChild, startDate, endDate);
      countScheduleOntimeNormal = getCount(listScheduleOnTime, Constants.CR_TYPE.NORMAL);
      countScheduleOntimeStandard = getCount(listScheduleOnTime, Constants.CR_TYPE.STANDARD);
      countScheduleOntimeEmg = getCount(listScheduleOnTime, Constants.CR_TYPE.EMERGENCY);
      countScheduleOutOfDateNormal = getCount(listScheduleOutOfDate, Constants.CR_TYPE.NORMAL);
      countScheduleOutOfDateStandard = getCount(listScheduleOutOfDate, Constants.CR_TYPE.STANDARD);
      countScheduleOutOfDateEmg = getCount(listScheduleOutOfDate, Constants.CR_TYPE.EMERGENCY);
      countTotal = countScheduleOntimeNormal + countScheduleOutOfDateNormal;
      countTotalEmg = countScheduleOntimeEmg + countScheduleOutOfDateEmg;
      countTotalStandard = countScheduleOntimeStandard + countScheduleOutOfDateStandard;
      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setId(total);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.SCHEDULE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      resultDTO.setQuantitySucc(countTotal.intValue());
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalEmg);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.SCHEDULE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      resultDTO.setQuantitySucc(countTotalEmg.intValue());
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalStandard);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.SCHEDULE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      resultDTO.setQuantitySucc(countTotalStandard.intValue());
      list.add(resultDTO);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public List<ResultDTO> getListReceive(String unit, String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> list = new ArrayList<>();
    try {
      String receiveOntimeNormal = Constants.NOTIFY_TYPE.WAITING_FOR_RECEIVE;
      String receiveOntimeStandard = Constants.NOTIFY_TYPE.WAITING_FOR_RECEIVE_STANDARD;
      String receiveOntimeEmg = Constants.NOTIFY_TYPE.WAITING_FOR_RECEIVE_EMG;
//      String total = Constants.NOTIFY_TYPE.TOTAL_RECEIVE;
//      String totalEmg = Constants.NOTIFY_TYPE.TOTAL_RECEIVE_EMG;
//      String totalStandard = Constants.NOTIFY_TYPE.TOTAL_RECEIVE_STANDARD;
      Long countReceiveOntimeNormal;
      Long countReceiveOntimeStandard;
      Long countReceiveOntimeEmg;
      List<CrDTO> listReceiveOnTime = actionGetTotalCRwaitingForConsiderReceiveResolveOrCloseVer2(
          unit, false, false, Constants.CR_SEARCH_TYPE.EXCUTE, searchChild, startDate, endDate);
      countReceiveOntimeNormal = getCount(listReceiveOnTime, Constants.CR_TYPE.NORMAL);
      countReceiveOntimeStandard = getCount(listReceiveOnTime, Constants.CR_TYPE.STANDARD);
      countReceiveOntimeEmg = getCount(listReceiveOnTime, Constants.CR_TYPE.EMERGENCY);
      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setId(receiveOntimeNormal);
      resultDTO.setQuantitySucc(
          countReceiveOntimeNormal == null ? 0 : countReceiveOntimeNormal.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.EXCUTE.toString());
//            resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.ONTIME);
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(receiveOntimeStandard);
      resultDTO.setQuantitySucc(
          countReceiveOntimeStandard == null ? 0 : countReceiveOntimeStandard.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.EXCUTE.toString());
//            resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.ONTIME);
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(receiveOntimeEmg);
      resultDTO
          .setQuantitySucc(countReceiveOntimeEmg == null ? 0 : countReceiveOntimeEmg.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.EXCUTE.toString());
//            resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.ONTIME);
      list.add(resultDTO);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public List<ResultDTO> getListClose(String unit, String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> list = new ArrayList<ResultDTO>();
    try {
      String closeOntimeNormal = Constants.NOTIFY_TYPE.WAITING_FOR_CLOSE;
      String closeOntimeStandard = Constants.NOTIFY_TYPE.WAITING_FOR_CLOSE_STANDARD;
      String closeOntimeEmg = Constants.NOTIFY_TYPE.WAITING_FOR_CLOSE_EMG;
//      String total = Constants.NOTIFY_TYPE.TOTAL_CLOSE;
//      String totalEmg = Constants.NOTIFY_TYPE.TOTAL_CLOSE_EMG;
//      String totalStandard = Constants.NOTIFY_TYPE.TOTAL_CLOSE_STANDARD;
      Long countCloseOntimeNormal;
      Long countCloseOntimeStandard;
      Long countCloseOntimeEmg;
//      Long countTotalNormal;
//      Long countTotalEmg;
//      Long countTotalStandard;
      List<CrDTO> listCloseOnTime = actionGetTotalCRwaitingForConsiderReceiveResolveOrCloseVer2(
          unit, false, false, Constants.CR_SEARCH_TYPE.CLOSE, searchChild, startDate, endDate);
      countCloseOntimeNormal = getCount(listCloseOnTime, Constants.CR_TYPE.NORMAL);
      countCloseOntimeStandard = getCount(listCloseOnTime, Constants.CR_TYPE.STANDARD);
      countCloseOntimeEmg = getCount(listCloseOnTime, Constants.CR_TYPE.EMERGENCY);
//      countTotalNormal = countCloseOntimeNormal;
//      countTotalEmg = countCloseOntimeEmg;
//      countTotalStandard = countCloseOntimeStandard;
      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setId(closeOntimeNormal);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.CLOSE.toString());
//            resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.ONTIME);
      resultDTO
          .setQuantitySucc(countCloseOntimeNormal == null ? 0 : countCloseOntimeNormal.intValue());
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(closeOntimeStandard);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.CLOSE.toString());
//            resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.ONTIME);
      resultDTO.setQuantitySucc(
          countCloseOntimeStandard == null ? 0 : countCloseOntimeStandard.intValue());
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(closeOntimeEmg);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.CLOSE.toString());
//            resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.ONTIME);
      resultDTO.setQuantitySucc(countCloseOntimeEmg == null ? 0 : countCloseOntimeEmg.intValue());
      list.add(resultDTO);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public List<CrDTO> actionGetTotalCRwaitingForApproveVer2(String unit, boolean isOutOfDate,
      String searchChild,
      String startDate,
      String endDate) {
    List<CrDTO> result = new ArrayList<>();
    try {
      StringBuilder sql = new StringBuilder("");
      if (StringUtils.isStringNullOrEmpty(unit)) {
        return result;
      }
      Date startDateFrom = null;
      Date startDateTo = null;
      try {
        startDateFrom = DateTimeUtils.convertStringToDate(startDate);
        startDateTo = DateTimeUtils.convertStringToDate(endDate);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      if (startDateFrom == null || startDateTo == null) {
        return result;
      }
      Map<String, Object> params = new HashMap<>();
      sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL_FOR_MOBILE,
          "get-total-cr-waiting-for-approve-ver2"));
      params.put("cr_state", Constants.CR_STATE.OPEN);
      if (searchChild != null && "yes".equals(searchChild.trim().toLowerCase())) {
        sql.append(" select unit_id from Common_gnoc.unit \n"
            + " start with unit_id = :unit_id \n"
            + " connect by prior unit_id = parent_unit_id \n");
      } else {
        sql.append(" :unit_id");
      }
      params.put("unit_id", Long.parseLong(unit));
      sql.append(" )"
          + "  and d.status = 0)\n"
          + " select tb.cr_id   from tbl tb where tb.cadt_level <= tb.num\n"
          + " )");
//            paramList.add(userId);
//            paramList.add(Constants.CR_ROLE.roleTP);
      if (isOutOfDate) {
        sql.append(" and cr.LATEST_START_TIME < sysdate ");
      } else {
        sql.append(" and cr.LATEST_START_TIME >= sysdate ");
      }
      sql.append(" and cr.EARLIEST_START_TIME >= :start_date_from ");
      sql.append(" and cr.EARLIEST_START_TIME <= :start_date_to ");
      params.put("start_date_from", startDateFrom);
      params.put("start_date_to", startDateTo);

      sql.append(" group by cr.cr_type ");
      result = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(CrDTO.class));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  public List<CrDTO> actionGetTotalCRwaitingForVerifyOrScheduleVer2(
      String unit,
      boolean isOutOfDate,
      Long type,
      String searchChild,
      String startDate,
      String endDate) {
    List<CrDTO> result = new ArrayList<>();
    try {
      StringBuilder sql = new StringBuilder();
      Map<String, Object> params = new HashMap<>();
      if (unit == null || "".equals(unit.trim())) {
        return result;
      }
      Date startDateFrom = null;
      Date startDateTo = null;
      try {
        startDateFrom = DateTimeUtils.convertStringToDate(startDate);
        startDateTo = DateTimeUtils.convertStringToDate(endDate);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      if (startDateFrom == null || startDateTo == null) {
        return result;
      }
      sql.append(" select count(cr.cr_id) as crId,cr.cr_type crType \n"
          + " from open_pm.cr cr\n"
          + " where cr.state is not null\n");
      sql.append(" and cr.state = :cr_state ");
      if (type.equals(Constants.CR_SEARCH_TYPE.VERIFY)) {
        params.put("cr_state", Constants.CR_STATE.QUEUE);
      }
      if (type.equals(Constants.CR_SEARCH_TYPE.SCHEDULE)) {
        params.put("cr_state", Constants.CR_STATE.EVALUATE);
      }
      sql.append(" and cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit in (");

      if (searchChild != null && "yes".equals(searchChild.trim().toLowerCase())) {
        sql.append(" select unit_id from Common_gnoc.unit \n"
            + " start with unit_id = :unit_id \n"
            + " connect by prior unit_id = parent_unit_id \n");
      } else {
        sql.append(" :unit_id");
      }
      params.put("unit_id", Long.parseLong(unit));
      sql.append(" )");
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" )");
      sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id in (");
      sql.append(" select unit_id from Common_gnoc.unit \n"
          + " start with unit_id = :unit_id \n"
          + " connect by prior unit_id = parent_unit_id\n");
      sql.append(" )");
      sql.append(" )");
      if (isOutOfDate) {
        sql.append(" and cr.LATEST_START_TIME < sysdate ");
      } else {
        sql.append(" and cr.LATEST_START_TIME >= sysdate ");
      }
      sql.append(" and cr.EARLIEST_START_TIME >= :start_date_from ");
      sql.append(" and cr.EARLIEST_START_TIME <= :start_date_to ");

      params.put("start_date_from", startDateFrom);
      params.put("start_date_to", startDateTo);
      sql.append(" group by cr.cr_type ");
      result = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(CrDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  public List<CrDTO> actionGetTotalCRwaitingForConsiderReceiveResolveOrCloseVer2(String unit,
      boolean isOutOfDate,
      boolean checkTime,
      Long type,
      String searchChild,
      String startDate,
      String endDate) {
    List<CrDTO> result = new ArrayList<>();
    Map<String, Object> params = new HashMap<>();
    try {
      StringBuilder sql = new StringBuilder("");
      if (StringUtils.isStringNullOrEmpty(unit)) {
        return result;
      }
      Date startDateFrom = null;
      Date startDateTo = null;
      try {
        startDateFrom = DateTimeUtils.convertStringToDate(startDate);
        startDateTo = DateTimeUtils.convertStringToDate(endDate);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      if (startDateFrom == null || startDateTo == null) {
        return result;
      }
      sql.append(" select count(cr.cr_id) as crId, cr.cr_type as crType \n"
          + " from open_pm.cr cr\n"
          + " where cr.state is not null\n");
      sql.append(" and cr.state = :cr_state ");
      if (type.equals(Constants.CR_SEARCH_TYPE.CONSIDER)) {
        sql.append(" and cr.CONSIDER_UNIT_ID in ( ");
      } else if (type.equals(Constants.CR_SEARCH_TYPE.EXCUTE) || type
          .equals(Constants.CR_SEARCH_TYPE.RESOLVE)) {
        sql.append(" and cr.change_responsible_unit in ( ");
      } else if (type.equals(Constants.CR_SEARCH_TYPE.CLOSE)) {
        sql.append(" and cr.manage_unit_id in ( ");
      }
      if (searchChild != null
          && "yes".equals(searchChild.trim().toLowerCase())) {
        sql.append(" select unit_id from Common_gnoc.unit \n"
            + " start with unit_id = :unit_id \n"
            + " connect by prior unit_id = parent_unit_id\n");
      } else {
        sql.append(" :unit_id ");
      }
      params.put("unit_id", Long.parseLong(unit));
      sql.append(" )");
      if (type.equals(Constants.CR_SEARCH_TYPE.CONSIDER)) {
        params.put("cr_state", Constants.CR_STATE.COORDINATE);
      }
      if (type.equals(Constants.CR_SEARCH_TYPE.EXCUTE)) {
        params.put("cr_state", Constants.CR_STATE.APPROVE);
      }
      if (type.equals(Constants.CR_SEARCH_TYPE.RESOLVE)) {
        params.put("cr_state", Constants.CR_STATE.ACCEPT);
      }
      if (type.equals(Constants.CR_SEARCH_TYPE.CLOSE)) {
        params.put("cr_state", Constants.CR_STATE.RESOLVE);
      }

      if (checkTime) {
        if (isOutOfDate) {
          sql.append(" and cr.LATEST_START_TIME < sysdate ");
        } else {
          sql.append(" and cr.LATEST_START_TIME >= sysdate ");
        }
      }
      sql.append(" and cr.EARLIEST_START_TIME >= :start_date_from ");
      sql.append(" and cr.EARLIEST_START_TIME <= :start_date_to ");
      params.put("start_date_from", startDateFrom);
      params.put("start_date_to", startDateTo);
      sql.append(" group by cr.cr_type ");
      result = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(CrDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  public Long getCount(List<CrDTO> listCrDTO, Long type) {
    Long result = 0L;
    try {
      for (CrDTO crDTO : listCrDTO) {
        if (crDTO.getCrType() != null && crDTO.getCrType().trim().equals(type.toString())) {
          if (crDTO.getCrId() != null) {
            result += Long.parseLong(crDTO.getCrId().trim());
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

}
