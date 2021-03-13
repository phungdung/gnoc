package com.viettel.gnoc.pt.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.pt.dto.ProblemWoDTO;
import com.viettel.gnoc.pt.model.ProblemWoEntity;
import com.viettel.gnoc.wfm.dto.WoSearchDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ProblemWoRepositoryImpl extends BaseRepository implements ProblemWoRepository {


  @Override
  public ResultInSideDto insertProblemWo(ProblemWoDTO problemWoDTO) {
    return insertByModel(problemWoDTO.toEntity(), colId);
  }

  @Override
  public String updateProblemWo(ProblemWoDTO problemWoDTO) {
    getEntityManager().merge(problemWoDTO.toEntity());
    return RESULT.SUCCESS;
  }

  @Override
  public String deleteProblemWo(Long id) {
    return deleteById(ProblemWoEntity.class, id, colId);
  }

  @Override
  public List<ProblemWoDTO> getListProblemWoDTO(ProblemWoDTO problemWoDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(ProblemWoEntity.class, problemWoDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public String deleteListProblemWo(List<ProblemWoDTO> problemWoDTO) {
    return deleteByListDTO(problemWoDTO, ProblemWoEntity.class, colId);
  }

  @Override
  public String insertOrUpdateListProblemWo(List<ProblemWoDTO> problemWoDTO) {
    for (ProblemWoDTO item : problemWoDTO) {
      ProblemWoEntity entity = item.toEntity();
      if (entity.getProblemWoId() != null && entity.getProblemWoId() > 0) {
        getEntityManager().merge(entity);
      } else {
        getEntityManager().persist(entity);
      }
    }
    return RESULT.SUCCESS;
  }

  @Override
  public ProblemWoDTO findProblemWoById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(ProblemWoEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public List<ProblemWoDTO> getListProblemWoByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new ProblemWoEntity(), lstCondition, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public String getSequenseProblemWo(String seqName) {

    return getSeqTableBase(seqName);
  }

  @Override
  public Datatable getListProblemWo(ProblemWoDTO problemWoDTO) {
    BaseDto baseDto = sqlSearch();
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(), problemWoDTO.getPage(), problemWoDTO.getPageSize(),
        ProblemWoDTO.class, problemWoDTO.getSortName(), problemWoDTO.getSortType());
  }

  public BaseDto sqlSearch() {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_PROBLEMWO, "problemWo");
    baseDto.setSqlQuery(sqlQuery);
    Map<String, Object> parameters = new HashMap<>();
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public List<String> getSeqProblemWo(String seqName, int... size) {
    return getListSequense(seqName, size);
  }

  private static final String colId = "problemWoId";

  //Wo
  @Override
  public Datatable getListDataSearchWeb(WoSearchDTO searchDto) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    Double offset = TimezoneContextHolder.getOffsetDouble();
    sql.append(" select w.wo_id woId");
    sql.append(" , w.parent_id parentId");
    sql.append(" , p.wo_code parentName");
    sql.append(" , w.wo_code woCode");
    sql.append(" , w.wo_content woContent");
    sql.append(" , w.wo_system woSystem");
    sql.append(" , w.wo_system_id woSystemId");
    sql.append(" , w.wo_system_out_id woSystemOutId");
    sql.append(" , w.create_person_id createPersonId");
    sql.append(" , w.create_date + :offset * interval '1' hour createDate");
    parameters.put("offset", offset);
    sql.append(" , w.wo_type_id woTypeId");
    sql.append(" , c.wo_type_name woTypeName");
    sql.append(" , w.cd_id cdId");
    sql.append(" , w.ft_id ftId");
    sql.append(" , w.status status, ");
    sql.append(" CASE w.status WHEN 0 THEN '")
        .append(I18n.getString("message.wo.status.UNASSIGNED")).append("' ");
    sql.append(" WHEN 1 THEN '").append(I18n.getString("message.wo.status.ASSIGNED")).append("' ");
    sql.append(" WHEN 2 THEN (case when w.ft_id is not null then '")
        .append(I18n.getString("message.wo.status.REJECT")).append("' ");
    sql.append(" else '").append(I18n.getString("message.wo.status.REJECT_CD")).append("' end) ");
    sql.append(" WHEN 3 THEN '").append(I18n.getString("message.wo.status.DISPATCH")).append("' ");
    sql.append(" WHEN 4 THEN '").append(I18n.getString("message.wo.status.ACCEPT")).append("' ");
    sql.append(" WHEN 5 THEN '").append(I18n.getString("message.wo.status.INPROCESS")).append("' ");
    sql.append(" WHEN 9 THEN '").append(I18n.getString("message.wo.status.PENDING")).append("' ");
    sql.append(" WHEN 8 THEN '").append(I18n.getString("message.wo.status.CLOSED_CD")).append("' ");

    //ThanhLV12_tach su co ft hoan thanh_start
    sql.append(" WHEN 6 THEN (case when w.result is null then '")
        .append(I18n.getString("message.wo.status.CLOSED_FT")).append("' ");
    sql.append(" else '").append(I18n.getString("message.wo.status.CLOSED_CD")).append("' end) ");

    //ThanhLV12_tach su co ft hoan thanh_end
    sql.append(" WHEN 7 THEN '").append(I18n.getString("message.wo.status.DRAFT")).append("' ");
    sql.append(" ELSE '' END statusName ");
    sql.append(" , w.priority_id priorityId");
    sql.append(" , wp.priority_name priorityName");
    sql.append(" , w.start_time + :offset * interval '1' hour startTime");
    sql.append(" , w.end_time + :offset * interval '1' hour endTime");
    sql.append(" , w.finish_time + :offset * interval '1' hour finishTime");
    sql.append(" , w.result result");
    sql.append(" , w.station_id stationId");
    sql.append(" , w.station_code stationCode");
    sql.append(" , w.last_update_time + :offset * interval '1' hour lastUpdateTime");
    sql.append(" , w.file_name fileName");
    sql.append(" , w.wo_description comments");
    sql.append(" , g.wo_group_name cdName");
    sql.append(" , cp.username createPersonName");
    sql.append(" , f.username ftName");
    sql.append(" , wd.account_isdn accountIsdn");
    //20160810 daitt1 start
    sql.append(" , w.need_support isNeedSupport");
    //20160810 daitt1 start
    sql.append(" , w.REASON_OVERDUE_LV1_ID reasonOverdueLV1Id");
    sql.append(" , w.REASON_OVERDUE_LV1_NAME reasonOverdueLV1Name");
    sql.append(" , w.REASON_OVERDUE_LV2_ID reasonOverdueLV2Id");
    sql.append(" , w.REASON_OVERDUE_LV2_NAME reasonOverdueLV2Name");
    sql.append(" , w.COMPLETED_TIME + :offset * interval '1' hour completedTime");
    sql.append(" , w.COMMENT_COMPLETE commentComplete");
    sql.append(" ,(case when w.status in (7,2) "
        + "    then null "
        + " else "
        + "    case when w.status = 8 "
        + "        then round((cast(w.end_time as date)-cast(w.finish_time as date))*24,2) "
        + "        else round((cast(w.end_time as date)-sysdate)*24,2) "
        + "    end "
        + " end "
        + " )remainTime "
    );
    sql.append(",w.line_Code lineCode");
    sql.append(",w.is_Completed_On_Vsmart isCompletedOnVsmart");
    sql.append(",w.is_Call isCall");
    sql.append(",w.WAREHOUSE_CODE warehouseCode");
    sql.append(",w.CONSTRUCTION_CODE constructionCode");
    sql.append(",w.cd_assign_id cdAssignId");
    sql.append(",wti.kedb_code kedbCode");
    sql.append(",wti.kedb_id kedbId");
    sql.append(",wki.contract_Id contractId");
    sql.append(",wki.process_Action_Id processActionId");
    sql.append(",wti.able_mop ableMop");
    sql.append(",w.delta_Close_Wo deltaCloseWo");
    sql.append(",w.confirm_Not_Create_Alarm confirmNotCreateAlarm");
    sql.append(",(uc.unit_name||'('||uc.unit_code||')') createUnitName");
    sql.append(", w.PLAN_CODE planCode ");
    sql.append(", w.NUM_SUPPORT numSupport ");
    sql.append(", g.WO_GROUP_NAME woGroupName ");
    sql.append(" from WFM.wo w, WFM.wo p, WFM.wo_type c, WFM.wo_cd_group g");
    sql.append(
        " , common_gnoc.users f, common_gnoc.users cp, WFM.wo_priority wp, WFM.wo_detail wd, WFM.wo_trouble_info wti, WFM.wo_ktts_info wki, common_gnoc.unit uc");
    sql.append(" where 1 = 1 ");
    sql.append(" and w.wo_type_id = c.wo_type_id");
    sql.append(" and w.create_person_id = cp.user_id");
    sql.append(" and cp.unit_id = uc.unit_id");
    sql.append(" and w.cd_id = g.wo_group_id");
    sql.append(" and w.ft_id = f.user_id(+)");
    sql.append(" and w.parent_id = p.wo_id(+)");
    sql.append(" and w.priority_id = wp.priority_id(+)");
    sql.append(" and w.wo_id = wd.wo_id(+)");
    sql.append(" and w.wo_id = wti.wo_id(+)");
    sql.append(" and w.wo_id = wki.wo_id(+)");
    // <editor-fold defaultstate="collapsed" desc="SET dieu kien tim kiem">
    // Tim theo he thong
    if (StringUtils.isNotNullOrEmpty(searchDto.getWoSystem())) {
      sql.append(" AND w.wo_system = :woSystem ");
      parameters.put("woSystem", searchDto.getWoSystem());
    }
    // Tim theo ma he thong
    if (StringUtils.isNotNullOrEmpty(searchDto.getWoSystemId())) {
      sql.append(" AND LOWER(w.wo_system_id) = :woSystemId ");
      parameters.put("woSystemId", searchDto.getWoSystemId().toLowerCase());
    }
    // Ma cong viec
    if (StringUtils.isNotNullOrEmpty(searchDto.getWoCode())) {
      sql.append(" AND LOWER(w.wo_code) LIKE :woCode ");
      parameters
          .put("woCode", StringUtils.convertLowerParamContains(searchDto.getWoCode()));
    }
    if (StringUtils.isNotNullOrEmpty(searchDto.getCdId())) {
      sql.append(" AND w.cd_id = :cdId ");
      parameters.put("cdId", searchDto.getCdId());
    }
    // Muc do uu tien
    if (StringUtils.isNotNullOrEmpty(searchDto.getPriorityId())) {
      sql.append(" AND w.priority_id = :priorityId ");
      parameters.put("priorityId", searchDto.getPriorityId());
    }
    // Loai cong viec
    if (StringUtils.isNotNullOrEmpty(searchDto.getWoTypeId())) {
      sql.append(" AND w.wo_type_id = :woTypeId ");
      parameters.put("woTypeId", searchDto.getWoTypeId());
    }
    // Noi dung cong viec
    if (StringUtils.isNotNullOrEmpty(searchDto.getWoContent())) {
      sql.append(" AND LOWER(w.wo_content) LIKE :woContent ");
      parameters
          .put("woContent", StringUtils.convertLowerParamContains(searchDto.getWoContent()));
    }
    // Trang thai hoan thanh
    if (StringUtils.isNotNullOrEmpty(searchDto.getResult())) {
      if ("3".equals(searchDto.getResult())) {
        sql.append(" AND w.result is null ");
      } else {
        sql.append(" AND w.result =:result ");
        parameters.put("result", searchDto.getResult());
      }
    }
    // Thoi gian khoi tao
    if (StringUtils.isNotNullOrEmpty(searchDto.getStartTimeFrom())) {
      sql.append(" AND w.create_date >= TO_TIMESTAMP(:startTimeFrom,'dd/mm/yyyy hh24:mi:ss') ");
      parameters.put("startTimeFrom", searchDto.getStartTimeFrom());
    }

//        //thanhlv12_fix loi tam thoi_end
    if (StringUtils.isNotNullOrEmpty(searchDto.getStartTimeTo())) {
      sql.append(" AND w.create_date <= TO_TIMESTAMP(:startTimeTo,'dd/mm/yyyy hh24:mi:ss') ");
      parameters.put("startTimeTo", searchDto.getStartTimeTo());
    }
    // Thoi gian thuc hien
    if (StringUtils.isNotNullOrEmpty(searchDto.getStartDateFrom())) {
      sql.append(" AND w.start_time >= TO_TIMESTAMP(:startDateFrom,'dd/mm/yyyy hh24:mi:ss') ");
      parameters.put("startDateFrom", searchDto.getStartDateFrom());
    }
    if (StringUtils.isNotNullOrEmpty(searchDto.getStartDateTo())) {
      sql.append(" AND w.start_time <= TO_TIMESTAMP(:startDateTo,'dd/mm/yyyy hh24:mi:ss') ");
      parameters.put("startDateTo", searchDto.getStartDateTo());
    }

    // Thoi gian ket thuc
    if (StringUtils.isNotNullOrEmpty(searchDto.getEndTimeFrom())) {
      sql.append(" AND w.end_time >= TO_TIMESTAMP(:endTimeFrom,'dd/mm/yyyy hh24:mi:ss') ");
      parameters.put("endTimeFrom", searchDto.getEndTimeFrom());
    }
    if (StringUtils.isNotNullOrEmpty(searchDto.getEndTimeTo())) {
      sql.append(" AND w.end_time <= TO_TIMESTAMP(:endTimeTo,'dd/mm/yyyy hh24:mi:ss') ");
      parameters.put("endTimeTo", searchDto.getEndTimeTo());
    }
    //R13099_new_namndh_19022016_start
    if (StringUtils.isNotNullOrEmpty(searchDto.getCompleteTimeFrom())) {

      sql.append(
          " And (w.finish_time >= TO_TIMESTAMP(:completeTimeFrom,'dd/mm/yyyy hh24:mi:ss') and w.result is not null)");
      parameters.put("completeTimeFrom", searchDto.getCompleteTimeFrom());
    }
    if (StringUtils.isNotNullOrEmpty(searchDto.getCompleteTimeTo())) {
      sql.append(
          " And (w.finish_time <= TO_TIMESTAMP(:completeTimeTo,'dd/mm/yyyy hh24:mi:ss') and w.result is not null)");
      parameters.put("completeTimeTo", searchDto.getCompleteTimeTo());
    }

    if (StringUtils.isNotNullOrEmpty(searchDto.getWoId())) {
      sql.append(" AND w.wo_id = :woId ");
      parameters.put("woId", Long.valueOf(searchDto.getWoId()));
    }
    //R13099_new_namndh_19022016_end
    if (StringUtils.isNotNullOrEmpty(searchDto.getParentId())) {
      sql.append(" AND w.parent_id = :parentId ");
      parameters.put("parentId", Long.valueOf(searchDto.getParentId()));
    }

    // Nhan vien tao
    if (StringUtils.isNotNullOrEmpty(searchDto.getCreatePersonName())) {
      sql.append(" AND LOWER(cp.username) LIKE :createPersonName ");
      parameters.put("createPersonName",
          StringUtils.convertLowerParamContains(searchDto.getCreatePersonName()));
    }
    // Nhan vien thuc hien
    if (StringUtils.isNotNullOrEmpty(searchDto.getFtName())) {
      sql.append(" AND LOWER(f.username) LIKE :ftName ");
      parameters.put("ftName", StringUtils.convertLowerParamContains(searchDto.getFtName()));
    }
    // nhom dieu phoi
    if (searchDto.getCdIdList() != null && searchDto.getCdIdList().size() > 0) {
      sql.append(" AND w.cd_id in( ");
      int i = 1;
      for (String cd : searchDto.getCdIdList()) {
        if (i == searchDto.getCdIdList().size()) {
          sql.append(":cd");
        } else {
          sql.append(":cd,");
        }
        parameters.put("startTimeTo", cd);
        i++;
      }
      sql.append(" ) ");
    }
    // Cong viec cha
    if (StringUtils.isNotNullOrEmpty(searchDto.getParentName())) {
      sql.append(" AND LOWER(p.wo_code) LIKE :parentName ");
      parameters
          .put("parentName", StringUtils.convertLowerParamContains(searchDto.getParentName()));
    }

    if (StringUtils.isNotNullOrEmpty(searchDto.getAccountIsdn())) {
      sql.append(" AND LOWER(wd.account_isdn) LIKE :accountIsdn ");
      parameters
          .put("accountIsdn", StringUtils.convertLowerParamContains(searchDto.getAccountIsdn()));
    }
    // Tim theo don vi
    if (searchDto.getIsContainChildUnit() != null && searchDto.getIsContainChildUnit()) {
      if (StringUtils.isNotNullOrEmpty(searchDto.getCreateUnitId())) {
        sql.append(" AND w.create_person_id in ( "
            + " select user_id from common_gnoc.users where unit_id in "
            + " (select unit_id from common_gnoc.unit where level < 50 and status = 1 "
            + " start with unit_id = :createUnitId "
            + " connect by prior unit_id = parent_unit_id)) "
        );
        parameters.put("createUnitId", searchDto.getCreateUnitId());

      }
      if (StringUtils.isNotNullOrEmpty(searchDto.getProcessUnitId())) {
        sql.append(" AND (w.ft_id in ( "
            + " select user_id from common_gnoc.users where unit_id in "
            + " (select unit_id from common_gnoc.unit where level < 50 and status = 1 "
            + " start with unit_id = :processUnitId "
            + " connect by prior unit_id = parent_unit_id)) "
            + ")");
        parameters.put("processUnitId", searchDto.getProcessUnitId());
      }
    } else {
      if (StringUtils.isNotNullOrEmpty(searchDto.getCreateUnitId())) {
        sql.append(
            " AND w.create_person_id in (select user_id from common_gnoc.users where unit_id = :createUnitId)  ");
        parameters.put("createUnitId", searchDto.getCreateUnitId());
      }
      if (StringUtils.isNotNullOrEmpty(searchDto.getProcessUnitId())) {
        sql.append(
            " AND (w.ft_id in (select user_id from common_gnoc.users where unit_id = :processUnitId)  "
                + ")");
        parameters.put("processUnitId", searchDto.getProcessUnitId());
      }
    }

    if (StringUtils.isNotNullOrEmpty(searchDto.getUserId())) {
      sql.append(" and ( ");
      Boolean isHasCondition = false;
      if (searchDto.isIsCreated() != null && searchDto.isIsCreated()) {
        sql.append(
            isHasCondition ? " or w.create_person_id = ? " : " w.create_person_id = :userId ");
        isHasCondition = true;
        parameters.put("userId", Long.valueOf(searchDto.getUserId()));
      }
      if (searchDto.isIsCd() != null && searchDto.isIsCd()) {
        sql.append(
            isHasCondition
                ? " or w.cd_id in (select wo_group_id from wo_cd where user_id = :userId) " //
                : "  w.cd_id in (select wo_group_id from wo_cd where user_id = :userId) ");
        isHasCondition = true;
        parameters.put("userId", Long.valueOf(searchDto.getUserId()));
      }
      if (searchDto.isIsFt() != null && searchDto.isIsFt()) {
        sql.append(isHasCondition ? " or w.ft_id = :userId  " : "  w.ft_id = :userId  ");
        isHasCondition = true;
        parameters.put("userId", Long.valueOf(searchDto.getUserId()));
      }
      if (!isHasCondition) {
        sql.append("1=1");
      }
      sql.append(" ) ");
    }
    //20160810 daitt1
    if (searchDto.getNeedSupport() != null && searchDto.getNeedSupport()) {
      sql.append(" AND w.need_support  = :needSupport ");
      parameters.put("needSupport", Constants.MASTER_DATA.YES);
    }
    //20160810 daitt1
    //day list xuong cuoi cung de ko bi sai lech thu tu
    if (StringUtils.isNotNullOrEmpty(searchDto.getStatus())) {
      if ("-1".equals(searchDto.getStatus())) {//Chua hoan thanh - Cho CD phe duyet
        sql.append(
            " AND w.status <> 7 and w.status <> 8 and ( w.status <> 6 or (w.status = 6 AND w.result is null))");
      }//ducdm1_them trang thai hoan thanh_start
      else if ("6".equals(searchDto.getStatus())) {//Da hoan thanh, CD chua phe duyet
        sql.append(" AND w.status = 6 AND w.result is null AND w.finish_time is null ");
      } else if (searchDto.getStatus().contains(",")) {
        if (searchDto.getStatus().contains("6")) {
          sql.append(
              " AND (w.status in (:lstStatus) or (w.status = 6 AND w.result is null AND w.finish_time is null )) ");
        } else {
          sql.append(" AND w.status in (:lstStatus)");
        }
        List<String> myList = new ArrayList<>(Arrays.asList(searchDto.getStatus().split(",")));
        List<Long> lstStatus = new ArrayList<>();
        for (String tmp : myList) {
//                    if (tmp.trim().equals("8")) {
//                        continue;
//                    }
          lstStatus.add(Long.parseLong(tmp.trim()));
        }
        parameters.put("lstStatus", lstStatus);
      } //ducdm1_them trang thai hoan thanh_end
      //chi chon trang thai dong
      else if ("8".equals(searchDto.getStatus())) {
        sql.append(" AND ((w.status = 8 or w.status = 6) AND w.result is not null) ");
      } else {
        sql.append(" AND w.status = :status  ");
        parameters.put("status", searchDto.getStatus());
      }
    }
    //thanhlv12_bo trang thai CD tu choi_start
    if (StringUtils.isStringNullOrEmpty(searchDto.getStatus()) || !searchDto.getStatus()
        .contains("2")) {
      sql.append(" and ( case when w.status = 2 and w.ft_id is null then 0 else 1 end )=1 ");
    }
    sql.append(" ORDER BY w.create_date DESC ");

    return getListDataTableBySqlQuery(sql.toString(), parameters, searchDto.getPage(),
        searchDto.getPageSize(),
        WoSearchDTO.class, searchDto.getSortName(), searchDto.getSortType());
  }

//  @Override
//  public Datatable getListDataWo(WoDTO wotDTO) {
//    Map<String, Object> parameters = new HashMap<>();
//    StringBuilder sql = new StringBuilder();
//    sql.append(" select w.WO_CODE woCode");
//    sql.append(" , w.WO_CONTENT woContent");
//    sql.append(" , t4.WO_GROUP_NAME woGroupName");
//    sql.append(" , t6.USERNAME createPersonName");
//    sql.append(" , t5.WO_TYPE_NAME woTypeName");
//    sql.append(" , w.status status, ");
//    sql.append(" CASE w.status WHEN 0 THEN '")
//        .append(I18n.getString("message.wo.status.UNASSIGNED")).append("' ");
//    sql.append(" WHEN 1 THEN '").append(I18n.getString("message.wo.status.ASSIGNED")).append("' ");
//    sql.append(" WHEN 2 THEN (case when w.ft_id is not null then '")
//        .append(I18n.getString("message.wo.status.REJECT")).append("' ");
//    sql.append(" else '").append(I18n.getString("message.wo.status.REJECT_CD")).append("' end) ");
//    sql.append(" WHEN 3 THEN '").append(I18n.getString("message.wo.status.DISPATCH")).append("' ");
//    sql.append(" WHEN 4 THEN '").append(I18n.getString("message.wo.status.ACCEPT")).append("' ");
//    sql.append(" WHEN 5 THEN '").append(I18n.getString("message.wo.status.INPROCESS")).append("' ");
//    sql.append(" WHEN 9 THEN '").append(I18n.getString("message.wo.status.PENDING")).append("' ");
//    sql.append(" WHEN 8 THEN '").append(I18n.getString("message.wo.status.CLOSED_CD")).append("' ");
//    sql.append(" WHEN 6 THEN (case when w.result is null then '")
//        .append(I18n.getString("message.wo.status.CLOSED_FT")).append("' ");
//    sql.append(" else '").append(I18n.getString("message.wo.status.CLOSED_CD")).append("' end) ");
//    sql.append(" WHEN 7 THEN '").append(I18n.getString("message.wo.status.DRAFT")).append("' ");
//    sql.append(" ELSE '' END statusName ");
//    sql.append(" , w.start_time startTime");
//    sql.append(" , w.end_time endTime");
//    sql.append(" , t7.PRIORITY_NAME priorityName");
//    sql.append("  from WFM.WO w \n"
//        + "                        JOIN ONE_TM.PROBLEM_WO t2 ON w.WO_ID=t2.WO_ID\n"
//        + "                        JOIN WFM.WO_CD t3 ON w.CD_ID=t3.CD_ID\n"
//        + "                        JOIN WFM.WO_CD_GROUP t4 ON t3.WO_GROUP_ID=t4.WO_GROUP_ID \n"
//        + "                        JOIN WFM.WO_TYPE t5 ON w.WO_TYPE_ID=t5.WO_TYPE_ID\n"
//        + "                        JOIN common_gnoc.users t6 ON w.CREATE_PERSON_ID=t6.USER_ID\n"
//        + "                        JOIN WFM.WO_PRIORITY t7 ON w.PRIORITY_ID=t7.PRIORITY_ID where 1=1");
//    if (StringUtils.isNotNullOrEmpty(wotDTO.getWoSystemId())) {
//      sql.append(" AND LOWER(w.wo_system_id) = :woSystemId ");
//      parameters.put("woSystemId", wotDTO.getWoSystemId().toLowerCase());
//    }
//    if (StringUtils.isNotNullOrEmpty(wotDTO.getUserId())) {
//      sql.append("and w.create_person_id = :userId ");
//      parameters.put("userId", Long.valueOf(wotDTO.getUserId()));
//
//    }
//    return getListDataTableBySqlQuery(sql.toString(), parameters, wotDTO.getPage(),
//        wotDTO.getPageSize(),
//        WoDTO.class, wotDTO.getSortName(), wotDTO.getSortType());
//  }
}



