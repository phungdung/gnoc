package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CR_SEARCH_TYPE;
import com.viettel.gnoc.commons.utils.Constants.CR_STATE;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentInsiteDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrUpdateMopStatusHisDTO;
import com.viettel.gnoc.cr.dto.ObjResponse;
import com.viettel.gnoc.cr.model.CrEntity;
import com.viettel.gnoc.wo.model.WorkLogEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import viettel.passport.client.UserToken;

@Repository
@Slf4j
public class CrRepositoryImpl extends BaseRepository implements
    CrRepository {

  @Autowired
  UserRepository userRepository;

  @Autowired
  CrDBRepository crDBRepository;

  @Autowired
  SmsDBRepository smsDBRepository;

  @Autowired
  CrMobileRepository crMobileRepository;

  @Autowired
  CrApprovalDepartmentRepository crApprovalDepartmentRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Override
  public Datatable getListCRBySearchType(CrInsiteDTO crDTO, String locale) {
    try {
      if (crDTO != null) {
        List<List<Long>> searchCrIds = crDTO.getSearchCrIds();
        String userIdStr = crDTO.getUserLogin();
        String userIdDeptStr = crDTO.getUserLoginUnit();//R_xxx uyquyen
        Long userId = null;
        Long userDept = null;
        if (userIdStr != null) {
          userId = Long.valueOf(userIdStr);
        }
        if (userIdDeptStr != null) {
          userDept = Long.valueOf(userIdDeptStr);
        }
        if (userId != null && userDept != null && crDTO.getSearchType() != null) {
          boolean isManager = userRepository.isManagerOfUnits(userId);
          String sql = SQLBuilder
              .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "on-search-cr-by-type");
          BaseDto baseDto = genConditionSearchCr(crDTO, userId, userDept, isManager);
          Map<String, Object> params = baseDto.getParameters();
          sql += baseDto.getSqlQuery();
          if (searchCrIds != null && searchCrIds.size() > 0) {
            sql += " and ( cr.cr_id in (:searchCrIds0) ";
            params.put("searchCrIds0", searchCrIds.get(0));
            for (int i = 1; i < searchCrIds.size(); i++) {
              sql += " or cr.cr_id in (:searchCrIds" + i + ") ";
              params.put("searchCrIds" + i, searchCrIds.get(i));
            }
            sql += " ) ";
          }
          sql += " order by cr.UPDATE_TIME desc ";

          Datatable datatable = getListDataTableBySqlQuery(sql, params, crDTO.getPage(),
              crDTO.getPageSize(), CrInsiteDTO.class,
              crDTO.getSortName(), crDTO.getSortType());
          List<CrInsiteDTO> lstData = (List<CrInsiteDTO>) datatable.getData();
          datatable.setData(crDBRepository
              .processListToGenGr(lstData, crDTO, isManager, userId, userDept, locale));
          return datatable;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new Datatable();
  }

  @Override
  public List<String> getSequenseCr(String seqName, int... size) {
    return getListSequense(seqName, size);
  }

  @Override
  public ResultInSideDto insertCr(CrInsiteDTO crDTO) {
    CrEntity crEntity = crDTO.toEntity();
    if (StringUtils.isNotNullOrEmpty(crDTO.getCrId())) {
      getEntityManager().persist(crEntity);
    } else {
      getEntityManager().merge(crEntity);
    }
    return new ResultInSideDto(crEntity.getCrId(), RESULT.SUCCESS, RESULT.SUCCESS);
  }

  @Override
  public String updateCr(CrInsiteDTO crDTO) {
    CrEntity entity = getEntityManager().find(CrEntity.class, Long.valueOf(crDTO.getCrId()));
    CrEntity crModel = crDTO.toEntity();
    if (entity != null) {
      crModel.setOriginalLatestStartTime(entity.getOriginalLatestStartTime());
      crModel.setOriginalEarliestStartTime(entity.getOriginalEarliestStartTime());
      crModel.setManageUserId(entity.getManageUserId());
      crModel.setManageUnitId(entity.getManageUnitId());
      crModel.setConsiderUserId(entity.getConsiderUserId());
      crModel.setConsiderUnitId(entity.getConsiderUnitId());
    }
    getEntityManager().merge(crModel);
    return RESULT.SUCCESS;
  }

  @Override
  public void actionUpdateOriginalTimeOfCR(CrInsiteDTO crDTO) {
    try {
      Date startDate = crDTO.getEarliestStartTime();
      Date endDate = crDTO.getLatestStartTime();
      StringBuilder sql = new StringBuilder("");
      sql.append(
          " update cr set original_earliest_start_time = :ear_start_time, original_latest_start_time = :last_start_time ");
      sql.append(" where cr_id  =  :cr_id ");
      Map<String, Object> params = new HashMap<>();
      params.put("ear_start_time", startDate);
      params.put("last_start_time", endDate);
      params.put("cr_id", Long.parseLong(crDTO.getCrId().trim()));
      getNamedParameterJdbcTemplate().update(sql.toString(), params);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public void actionUpdateRelatedCR(CrInsiteDTO crDTO) {
    try {
//            Session sess = getSession();
      if (crDTO.getRelateToPrimaryCr() != null) {
        crDBRepository.actionUpdateRelatedCR(crDTO);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private BaseDto genConditionSearchCr(CrInsiteDTO crDTO, Long userId, Long userDept,
      boolean isManager) throws Exception {
    StringBuffer sql = new StringBuffer("");
    Map<String, Object> params = new HashMap<>();
    Double offset = TimezoneContextHolder.getOffsetDouble();
    params.put("offset", offset);
    params.put("state_close", Constants.CR_STATE.CLOSE);
    params.put("action_reject", Constants.CR_ACTION_CODE.REJECT);
    params.put("act_close", Constants.CR_ACTION_CODE.CLOSE);
    params.put("act_close_by_appr", Constants.CR_ACTION_CODE.CLOSE_BY_APPRAISER);
    params.put("act_close_by_man", Constants.CR_ACTION_CODE.CLOSE_BY_MANAGER_SCH);
    params.put("act_close_by_emer", Constants.CR_ACTION_CODE.CLOSE_EXCUTE_EMERGENCY);
    params.put("act_close_cr", Constants.CR_ACTION_CODE.CLOSECR);
    params.put("act_close_cr_appr", Constants.CR_ACTION_CODE.CLOSECR_APPROVE_STD);
    params.put("act_close_excu", Constants.CR_ACTION_CODE.CLOSE_EXCUTE_STD);
    params.put("state_draft", Constants.CR_STATE.DRAFT);

    if (Constants.CR_SEARCH_TYPE.CREATE_EDIT.toString().equals(crDTO.getSearchType())) {
      sql.append(" and ( ( ");
      // <editor-fold desc="Cho phep nguoi tao CR co the sua CR khi CR chua phe duyet hoac INCOMPLETE">
      sql.append(" cr.state in (:state_open, :state_incom, :state_draft) ");
      params.put("state_open", Constants.CR_STATE.OPEN);
      params.put("state_incom", Constants.CR_STATE.INCOMPLETE);
      sql.append(" and cr.change_orginator_unit = :org_unit ");
      params.put("org_unit", userDept);
      sql.append(" and cr.change_orginator = :change_orginator ");
      params.put("change_orginator", userId);
      // </editor-fold>

      sql.append(" ) or (");
      // <editor-fold desc="Cho phep tat ca nhan vien trong don vi them WLog khi CR RESOLVE">
      sql.append(" cr.state = :state_resolve ");
      params.put("state_resolve", Constants.CR_STATE.RESOLVE);
      sql.append(" and cr.change_orginator_unit in ( ");
      sql.append(" SELECT unit_id");
      sql.append(" FROM common_gnoc.unit ");
      sql.append(" START WITH  unit_id = :unit_id ");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" )");
      params.put("unit_id", userDept);
      // </editor-fold>
      sql.append(" ) )");
    }
//        if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.LOOKUP.toString()) &&
//                crDTO.getState() != null && !crDTO.getState().trim().equals("")) {
//                sql.append(" and cr.state = ? ");
//                paramList.add(crDTO.getState());
//        }

    if (Constants.CR_SEARCH_TYPE.LOOKUP.toString().equals(crDTO.getSearchType())) {
      if (StringUtils.isNotNullOrEmpty(crDTO.getState())) {
        String[] arrState = crDTO.getState().split(",");
        List<String> lstState = Arrays.asList(arrState);
        if (lstState == null) {
          lstState = new ArrayList<>();
        }
        List<String> lstStateAll = new ArrayList<>();
        lstStateAll.addAll(lstState);
        lstStateAll.add("-1");
        sql.append(" and cr.state in (:lst_state) ");
        params.put("lst_state", lstStateAll);
      } else {
        sql.append(" and cr.state in (-1) ");
      }
    }

    if (Constants.CR_SEARCH_TYPE.APPROVE.toString().equals(crDTO.getSearchType())) {
      sql.append(" and cr.state = :state_open ");
      params.put("state_open", Constants.CR_STATE.OPEN);
      sql.append(
          " and exists (select 1 from common_gnoc.v_user_role where user_id = :user_id and role_code = :role_code )");
      params.put("user_id", userId);
      params.put("role_code", Constants.CR_ROLE.roleTP);
//                        if(crDTO.getState().equals(Constants.CR_STATE.OPEN.toString())){
      if (crDTO.getIsSearchChildDeptToApprove() != null
          && "1".equals(crDTO.getIsSearchChildDeptToApprove().trim())) {
        sql.append(" and cr.cr_id in (");
        sql.append(" select cr_id from ");
        sql.append(" cr_approval_department d ");
        sql.append(" where ");
        sql.append(" d.cr_id = cr.cr_id and d.unit_id = :unit_id ");
        sql.append(" and d.status = 0 )");
        params.put("unit_id", userDept);
      } else {
        sql.append(" and cr.cr_id in (");
        sql.append(" with tbl as (");
        sql.append(" select d.unit_id,d.cr_id,d.cadt_level,");
        sql.append(" (select min(b.cadt_level) from cr_approval_department b");
        sql.append(" where  b.cr_id = d.cr_id and b.status = 0) as num");
        sql.append(" from cr_approval_department d");
        sql.append(" where d.unit_id = :unit_id  and d.status = 0)");
        sql.append(" select tb.cr_id   from tbl tb where tb.cadt_level <= tb.num");
        sql.append(" ) ");
        params.put("unit_id", userDept);
      }
//            sql.append(" and ( (cr.is_primary_cr is null) ");
//            sql.append(" OR (cr.is_primary_cr is not null ");
//            sql.append(" and exists (select 1 from cr cr1 where cr1.relate_to_primary_cr = cr.cr_id))");
//            sql.append(" )");
//                        }else{
//                            sql.append(" and cr.cr_id in (select d.cr_id from cr_approval_department d ");
//                            sql.append(" where unit_id =  ? )");
//                            paramList.add(userDept);
//                        }
    }
    if (Constants.CR_SEARCH_TYPE.VERIFY.toString().equals(crDTO.getSearchType())) {
      sql.append(" and cr.state = :state_queue ");
      params.put("state_queue", Constants.CR_STATE.QUEUE);
      sql.append(" and ( cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit = :manage_unit");
      sql.append(" and scope_id = :scope_id AND DEVICE_TYPE  IS NULL ");
      sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 ");
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
          + "  FROM common_gnoc.unit ut "
          + "    START WITH ut.unit_id IN "
          + "    (SELECT excute_unit_id "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = :manage_unit "
          + "    AND scope_id      = :scope_id "
          + "    and IS_SCHEDULE_CR_EMERGENCY <> 1 AND DEVICE_TYPE  IS NOT NULL "
          + "    ) "
          + "    CONNECT BY PRIOR unit_id = parent_unit_id "
          + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = :manage_unit  AND scope_id = :scope_id AND DEVICE_TYPE  IS NOT NULL )))");
      sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit)");
      params.put("manage_unit", userDept);
      params.put("scope_id", Long.valueOf(crDTO.getScopeId()));

    }
    if (Constants.CR_SEARCH_TYPE.WAIT_CAB.toString().equals(crDTO.getSearchType())) {
      //chi CAB cr thuong
      sql.append(" and cr.state in( :state_wait_cab,:state_cab) ");
      params.put("state_wait_cab", Constants.CR_STATE.WAIT_CAB);
      params.put("state_cab", Constants.CR_STATE.CAB);
      sql.append(" and ( cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" scope_id = :scope_id AND DEVICE_TYPE IS NULL ");

      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
          + "  FROM common_gnoc.unit ut "
          + "    START WITH ut.unit_id IN "
          + "    (SELECT excute_unit_id "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = :manage_unit "
          + "    AND scope_id      = :scope_id "
          + "    AND DEVICE_TYPE  IS NOT NULL "
          + "    ) "
          + "    CONNECT BY PRIOR unit_id = parent_unit_id "
          + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = :manage_unit  AND scope_id = :scope_id AND DEVICE_TYPE  IS NOT NULL)))");
      params.put("manage_unit", userDept);
      params.put("scope_id", Long.valueOf(crDTO.getScopeId()));
    }

    if (Constants.CR_SEARCH_TYPE.CAB.toString().equals(crDTO.getSearchType())) {

      sql.append(" and (");
      sql.append(" ( cr.state = :state_cab ");
      sql.append(" and cr.user_cab =:user_cab) ");

      //cr khan cho phep user duoc cau hinh co the cab khi sap lich
      sql.append(" OR (cr.cr_type = :cr_type_emer ");
      sql.append(" and cr.state = :state_eval ");

      sql.append(" and cr.change_responsible_unit in( ");
      sql.append(" select cab.execute_unit_id from open_pm.cr_cab_users cab");
      sql.append(" where cab.user_id= :user_id)");
      params.put("user_id", userId);

      sql.append(" and cr.impact_segment in ( ");
      sql.append(" select cab.impact_segment_id from open_pm.cr_cab_users cab");
      sql.append(" where cab.user_id= :user_id)");
      sql.append(" )  ");

      params.put("state_cab", Constants.CR_STATE.CAB);
      params.put("user_cab", userId);
      params.put("cr_type_emer", Constants.CR_TYPE.EMERGENCY);
      params.put("state_eval", Constants.CR_STATE.EVALUATE);
      //cr khan cho phep user duoc cau hinh co the cab khi sap lich

      if (crDTO.getScopeId() != null) {
        sql.append(" OR (  cr.state in ( :state_approve, :state_accept,:state_resolve) ");
        params.put("state_approve", Constants.CR_STATE.APPROVE);
        params.put("state_accept", Constants.CR_STATE.ACCEPT);
        params.put("state_resolve", Constants.CR_STATE.RESOLVE);
        sql.append(" and cr.cr_type = :cr_type_emer ");
        params.put("cr_type_emer", Constants.CR_TYPE.EMERGENCY);
        sql.append(" and cr.change_responsible_unit in ");
        sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
        sql.append(" start with ut.unit_id in ");
        sql.append(" ( ");
        sql.append(" select unit_id ");
        sql.append(" from cr_manager_units_of_scope cmnose ");
        sql.append(" where cmnose.cmse_id = :scope_id");
        params.put("scope_id", Long.valueOf(crDTO.getScopeId()));
        sql.append(" )");
        sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
        sql.append(" ) ) ");
      }
      sql.append(" )");

    }
    if (Constants.CR_SEARCH_TYPE.QLTD.toString().equals(crDTO.getSearchType())) {
      sql.append(
          " and cr.state in(:state_wait_cab,:state_cab,:state_queue,:state_coor,:state_eval) ");
      params.put("state_wait_cab", Constants.CR_STATE.WAIT_CAB);
      params.put("state_cab", Constants.CR_STATE.CAB);
      params.put("state_queue", Constants.CR_STATE.QUEUE);
      params.put("state_coor", Constants.CR_STATE.COORDINATE);
      params.put("state_eval", Constants.CR_STATE.EVALUATE);
      sql.append(" and ( cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit = :manage_unit ");
      sql.append(" and scope_id = :scope_id AND DEVICE_TYPE IS NULL ");
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
          + "  FROM common_gnoc.unit ut "
          + "    START WITH ut.unit_id IN "
          + "    (SELECT excute_unit_id "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = :manage_unit  "
          + "    AND scope_id      = :scope_id "
          + "    AND DEVICE_TYPE  IS NOT NULL "
          + "    ) "
          + "    CONNECT BY PRIOR unit_id = parent_unit_id "
          + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = :manage_unit   AND scope_id = :scope_id AND DEVICE_TYPE  IS NOT NULL )))");
      sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit )");
      params.put("manage_unit", userDept);
      params.put("scope_id", Long.valueOf(crDTO.getScopeId()));
    }

    if (Constants.CR_SEARCH_TYPE.QLTD_RENEW.toString().equals(crDTO.getSearchType())) {
      sql.append(
          " and cr.state in(:state_accept) ");
      params.put("state_accept", CR_STATE.ACCEPT);
      sql.append(" and ( cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit = :manage_unit ");
      sql.append(" and scope_id = :scope_id AND DEVICE_TYPE IS NULL ");
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
          + "  FROM common_gnoc.unit ut "
          + "    START WITH ut.unit_id IN "
          + "    (SELECT excute_unit_id "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = :manage_unit  "
          + "    AND scope_id      = :scope_id "
          + "    AND DEVICE_TYPE  IS NOT NULL "
          + "    ) "
          + "    CONNECT BY PRIOR unit_id = parent_unit_id "
          + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = :manage_unit   AND scope_id = :scope_id AND DEVICE_TYPE  IS NOT NULL )))");
//      sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit )");
      params.put("manage_unit", userDept);
      params.put("scope_id", Long.valueOf(crDTO.getScopeId()));
    }

    if (Constants.CR_SEARCH_TYPE.Z78.toString().equals(crDTO.getSearchType())) {
      sql.append(" and cr.state = :state_accept ");
      params.put("state_accept", Constants.CR_STATE.ACCEPT);
      sql.append(" and cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" ( ");
      sql.append(" select unit_id ");
      sql.append(" from cr_manager_units_of_scope cmnose ");
      sql.append(" where cmnose.cmse_id = :scope_id ");
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" )");
      params.put("scope_id", Long.valueOf(crDTO.getScopeId()));

    }
    if (Constants.CR_SEARCH_TYPE.CONSIDER.toString().equals(crDTO.getSearchType())) {
      //tuanpv edit cho phep nhan vien nhin thay CR can tham dinh cua ca don vi

//            if (isManager) {
      sql.append(" and cr.state = :state_coor ");
      params.put("state_coor", Constants.CR_STATE.COORDINATE);
      sql.append(" and ( cr.consider_unit_id = :unit_id ");
      params.put("unit_id", userDept);
      sql.append(" OR  cr.consider_user_id = :user_id )");
      params.put("user_id", userId);
//            } else {
//                sql.append(" and cr.state = ? ");
//                paramList.add(Constants.CR_STATE.COORDINATE);
//                sql.append(" and cr.consider_user_id = ? ");
//                paramList.add(userId);
//            }
    }
    if (Constants.CR_SEARCH_TYPE.SCHEDULE.toString().equals(crDTO.getSearchType())) {
      sql.append(" and cr.state = :state_eval ");
      params.put("state_eval", Constants.CR_STATE.EVALUATE);
      sql.append(" and ((cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit = :manage_unit ");
      sql.append(" and scope_id = :scope_id AND DEVICE_TYPE IS NULL ");
      if (Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY = 1 ");
      } else {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 ");
      }

      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
          + "  FROM common_gnoc.unit ut "
          + "    START WITH ut.unit_id IN "
          + "    (SELECT excute_unit_id "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = :manage_unit  "
          + "    AND scope_id      = :scope_id_notnull "
          + "    AND DEVICE_TYPE IS NOT NULL ");
      if (Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY = 1 ");
      } else {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 ");
      }
      sql.append("    ) "
          + "    CONNECT BY PRIOR unit_id = parent_unit_id "
          + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = :manage_unit   AND scope_id = :scope_id AND DEVICE_TYPE  IS NOT NULL ");
      if (Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY = 1 )) ");
      } else {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 )) ");
      }

      sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit ))");
      params.put("manage_unit", userDept);
      params.put("scope_id_notnull",
          Long.valueOf(crDTO.getScopeId() != null ? crDTO.getScopeId() : "-1"));
      params.put("scope_id", Long.valueOf(crDTO.getScopeId()));

      sql.append(
          " or (cr.cr_type=:cr_type_emer and cr.process_type_id in (select cr_process_id from cr_ocs_schedule ");
      sql.append(" where user_id=:user_id)) ");
      params.put("cr_type_emer", Constants.CR_TYPE.EMERGENCY);
      params.put("user_id", userId);
      sql.append(" ) ");

    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.EXCUTE.toString())) {
      if (isManager) {
        sql.append(
            " and ( cr.state = :state_approve or (cr.state = :state_accept and cr.earliest_start_time > sysdate)) ");
        params.put("state_approve", Constants.CR_STATE.APPROVE);
        params.put("state_accept", Constants.CR_STATE.ACCEPT);
        sql.append(" and (cr.change_responsible_unit = :responsible_unit ");
        params.put("responsible_unit", userDept);
        sql.append(" or cr.change_responsible = :change_responsible )");
        params.put("change_responsible", userId);
      } else {
        sql.append(" and cr.state = :state_approve ");
        params.put("state_approve", Constants.CR_STATE.APPROVE);
        sql.append(" and ((cr.change_responsible_unit = :responsible_unit and cr.cr_type = 0 )");
        params.put("responsible_unit", userDept);
        sql.append(" or cr.change_responsible = :change_responsible) ");
        params.put("change_responsible", userId);
      }
    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.RESOLVE.toString())) {
      sql.append(" and cr.state = :state_accept ");
      params.put("state_accept", Constants.CR_STATE.ACCEPT);
      sql.append(
          " and (cr.change_responsible = :change_responsible or cr.HANDOVER_CA = :handover_ca )");
      params.put("change_responsible", userId);
      params.put("handover_ca", userId);
    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.CLOSE.toString())) {
      if (StringUtils.isStringNullOrEmpty(crDTO.getScopeId())) {
        if (isManager) {
          sql.append(" and cr.state = :state_resolve ");
          params.put("state_resolve", Constants.CR_STATE.RESOLVE);
          sql.append(" and cr.manage_unit_id = :manage_unit ");
          params.put("manage_unit", userDept);
        } else {
          sql.append(" and 1=2");//Nhan vien khong co quyen tac dong den CR cho` dong
        }
      } else {
        sql.append(" and cr.state = :state_resolve ");
        params.put("state_resolve", Constants.CR_STATE.RESOLVE);
        sql.append(" and (cr.change_responsible_unit in ");
        sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
        sql.append(" start with ut.unit_id in ");
        sql.append(" (select ");
        sql.append(" excute_unit_id  from v_manage_cr_config ");
        sql.append(" where ");
        sql.append(" manage_unit = :manage_unit ");
        sql.append(" and scope_id = :scope_id ");
        sql.append(" AND DEVICE_TYPE  IS NULL and IS_SCHEDULE_CR_EMERGENCY <> 1 )");
        sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
        sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
            + "  FROM common_gnoc.unit ut "
            + "    START WITH ut.unit_id IN "
            + "    (SELECT excute_unit_id "
            + "    FROM OPEN_PM.v_manage_cr_config "
            + "    WHERE manage_unit = :manage_unit "
            + "    AND scope_id      = :scope_id "
            + "    AND DEVICE_TYPE IS NOT NULL and IS_SCHEDULE_CR_EMERGENCY <> 1 "
            + "    ) "
            + "    CONNECT BY PRIOR unit_id = parent_unit_id "
            + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
            + "    FROM OPEN_PM.v_manage_cr_config "
            + "    WHERE manage_unit = :manage_unit  AND scope_id = :scope_id  AND DEVICE_TYPE IS NOT NULL and IS_SCHEDULE_CR_EMERGENCY <> 1 ) ))");

        sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit)");

        sql.append(" and (cr.manage_unit_id = :manage_unit ");
        sql.append(" OR (cr.manage_unit_id is null))");

        params.put("manage_unit", userDept);
        params.put("scope_id", Long.valueOf(crDTO.getScopeId()));
      }
    }

    if (StringUtils.isNotNullOrEmpty(crDTO.getCrNumber())) {
      String[] arrCrNumber = crDTO.getCrNumber().split(",");
      if (arrCrNumber.length > 1) {
        List<String> lstCrNumber = Arrays.asList(arrCrNumber);

        sql.append(" and cr.cr_number in(:lst_cr) ");
        params.put("lst_cr", lstCrNumber);
      } else {
        sql.append(" and LOWER(cr.cr_number) like :cr_number  ESCAPE '\\' ");
        params.put("cr_number", StringUtils.convertLowerParamContains(crDTO.getCrNumber()));
      }
    }

    //tuanpv14_R483701_end
    if (StringUtils.isNotNullOrEmpty(crDTO.getTitle())) {
      sql.append(" and lower(cr.title) like :cr_title ESCAPE '\\' ");
      params.put("cr_title", StringUtils.convertLowerParamContains(crDTO.getTitle()));
    }

    if (crDTO.getEarliestStartTime() != null) {
      sql.append(" and cr.earliest_start_time >= :earliest_start_time ");
//            paramList.add(dtu.convertStringToDateTime(crDTO.getEarliestStartTime().trim()));
      params.put("earliest_start_time", DateTimeUtils.convertDateToOffset(
          crDTO.getEarliestStartTime(), offset, true));
    }

    if (StringUtils.isNotNullOrEmpty(crDTO.getEarliestStartTimeTo())) {
      sql.append(" and cr.earliest_start_time <= :earliest_start_time_to ");
//            paramList.add(dtu.convertStringToDateTime(crDTO.getEarliestStartTimeTo().trim()));
      params.put("earliest_start_time_to", DateTimeUtils.converStringClientToServerDate(
          crDTO.getEarliestStartTimeTo().trim(), offset));
    }
    if (crDTO.getLatestStartTime() != null) {
      sql.append(" and cr.latest_start_time >= :latest_start_time ");
//            paramList.add(dtu.convertStringToDateTime(crDTO.getLatestStartTime().trim()));
      params.put("latest_start_time", DateTimeUtils.convertDateToOffset(
          crDTO.getLatestStartTime(), offset, true));
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getLatestStartTimeTo())) {
      sql.append(" and cr.latest_start_time <= :latest_start_time_to ");
//            paramList.add(dtu.convertStringToDateTime(crDTO.getLatestStartTimeTo().trim()));
      params.put("latest_start_time_to", DateTimeUtils.converStringClientToServerDate(
          crDTO.getLatestStartTimeTo().trim(), offset));
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getIsOutOfDate())) {
      if (Constants.CR_OUTDATE_TYPE.OUTOFDATE.equalsIgnoreCase(crDTO.getIsOutOfDate())) {
        sql.append(" and cr.LATEST_START_TIME < sysdate ");
      } else if (Constants.CR_OUTDATE_TYPE.ONTIME.equalsIgnoreCase(crDTO.getIsOutOfDate())) {
        sql.append(" and cr.LATEST_START_TIME >= sysdate ");
      }
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getCrType())
        && !"-1".equals(crDTO.getCrType().trim())) {
      sql.append(" and cr.cr_type = :cr_type ");
      params.put("cr_type", Long.parseLong(crDTO.getCrType().trim()));
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getSubcategory())
        && !"-1".equals(crDTO.getSubcategory().trim())) {
      sql.append(" and cr.Subcategory = :sub_category ");
      params.put("sub_category", Long.parseLong(crDTO.getSubcategory().trim()));
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getRisk())
        && !"-1".equals(crDTO.getRisk().trim())) {
      sql.append(" and cr.risk = :risk ");
      params.put("risk", Long.parseLong(crDTO.getRisk().trim()));
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getImpactSegment())
        && !"-1".equals(crDTO.getImpactSegment().trim())) {
      sql.append(" and cr.impact_segment = :impact_segment ");
      params.put("impact_segment", Long.parseLong(crDTO.getImpactSegment().trim()));
    }

    if (StringUtils.isNotNullOrEmpty(crDTO.getCountry())
        && !"-1".equals(crDTO.getCountry().trim())) {
      if ("4001000000".equals(crDTO.getCountry()) || "5000289722".equals(crDTO.getCountry())
          || "1000014581".equals(crDTO.getCountry())
          || "3500289726".equals(crDTO.getCountry()) || "4500000001".equals(crDTO.getCountry())
          || "6000289723".equals(crDTO.getCountry())
          || "3000289724".equals(crDTO.getCountry()) || "2000289729".equals(crDTO.getCountry())
          || "1500289728".equals(crDTO.getCountry())) {
        sql.append(" and cr.country in( :country1, :country2 )");
        params.put("country1", Long.parseLong(crDTO.getCountry().trim()));
        if ("1500289728".equals(crDTO.getCountry())) {//timor
          params.put("country2", 289728L);
        } else if ("2000289729".equals(crDTO.getCountry())) {//timor
          params.put("country2", 289729L);
        } else if ("3000289724".equals(crDTO.getCountry())) {//mozam
          params.put("country2", 289724L);
        } else if ("6000289723".equals(crDTO.getCountry())) {//timor
          params.put("country2", 289723L);
        } else if ("4500000001".equals(crDTO.getCountry())) {//myanmar
          params.put("country2", 300656L);
        } else if ("4001000000".equals(crDTO.getCountry())) {//tanz
          params.put("country2", 289727L);
        } else if ("5000289722".equals(crDTO.getCountry())) {//lao
          params.put("country2", 289722L);
        } else if ("1000014581".equals(crDTO.getCountry())) {//cam
          params.put("country2", 289721L);
        } else if ("3500289726".equals(crDTO.getCountry())) {//burundi
          params.put("country2", 289726L);
        }
      } else {
        sql.append(" and cr.country = :country ");
        params.put("country", Long.parseLong(crDTO.getCountry().trim()));
      }
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getChangeOrginator())
        && !"-1".equals(crDTO.getChangeOrginator().trim())) {
      sql.append(" and cr.Change_Orginator = :change_orginator ");
      params.put("change_orginator", Long.parseLong(crDTO.getChangeOrginator().trim()));
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getChangeResponsible())
        && !"-1".equals(crDTO.getChangeResponsible().trim())) {
      sql.append(" and cr.Change_Responsible = :change_responsible ");
      params.put("change_responsible", Long.parseLong(crDTO.getChangeResponsible().trim()));
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getChangeOrginatorUnit())
        && !"-1".equals(crDTO.getChangeOrginatorUnit().trim())) {
      if (crDTO.getSubDeptOri() != null && "1".equals(crDTO.getSubDeptOri().trim())) {
        sql.append(" and cr.Change_Orginator_Unit in ( ");
        sql.append(" SELECT unit_id");
        sql.append(" FROM common_gnoc.unit ");
        sql.append(" START WITH  unit_id = :change_unit ");
        sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
        sql.append(" )");
        params.put("change_unit", Long.parseLong(crDTO.getChangeOrginatorUnit().trim()));
      } else {
        sql.append(" and cr.Change_Orginator_Unit = :change_unit ");
        params.put("change_unit", Long.parseLong(crDTO.getChangeOrginatorUnit().trim()));
      }
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getChangeResponsibleUnit())
        && !"-1".equals(crDTO.getChangeResponsibleUnit().trim())) {
      if (crDTO.getSubDeptResp() != null && "1".equals(crDTO.getSubDeptResp().trim())) {
        sql.append(" and cr.Change_Responsible_Unit in ( ");
        sql.append(" SELECT unit_id");
        sql.append(" FROM common_gnoc.unit ");
        sql.append(" START WITH  unit_id = :change_res_unit ");
        sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
        sql.append(" )");
        params.put("change_res_unit", Long.parseLong(crDTO.getChangeResponsibleUnit().trim()));
      } else {
        sql.append(" and cr.Change_Responsible_Unit = :change_res_unit ");
        params.put("change_res_unit", Long.parseLong(crDTO.getChangeResponsibleUnit().trim()));
      }
    }

    BaseDto baseDto = new BaseDto();
    baseDto.setParameters(params);
    baseDto.setSqlQuery(sql.toString());

    return baseDto;
  }

  @Override
  public String actionCreateMappingCRwithOtherSys(CrInsiteDTO crDTO) {
    String result = Constants.CR_RETURN_MESSAGE.SUCCESS;
    try {

      if (crDTO.getCrCreatedFromOtherSysDTO() != null) {
        crDTO.getCrCreatedFromOtherSysDTO().setCrId(crDTO.getCrId());
        crDBRepository.createCrMappingFromOtherSystem(crDTO.getCrCreatedFromOtherSysDTO());
      }
      if (crDTO.getLstCrCreatedFromOtherSysDTO() != null
          && crDTO.getLstCrCreatedFromOtherSysDTO().size() > 0) {
        crDBRepository.actionDeleteCRFormOtherSystem(Long.valueOf(crDTO.getCrId()));
        List<CrCreatedFromOtherSysDTO> lst = crDTO.getLstCrCreatedFromOtherSysDTO();
        for (CrCreatedFromOtherSysDTO dto : lst) {
          dto.setCcfosmId(null);
          dto.setCrId(crDTO.getCrId());
          crDBRepository.createCrMappingFromOtherSystem(dto);
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result = Constants.CR_RETURN_MESSAGE.ERROR;
    }
    return result;
  }

  /**
   * * tự động reset phê duyệt khi nguoi dùng cập nhật CR
   */
  @Override
  public String actionResetApproveCRIncaseOfEditCR(CrInsiteDTO crDTO) {
    String result = Constants.CR_RETURN_MESSAGE.SUCCESS;
    try {
      if (!Constants.CR_ACTION_CODE.UPDATE_CR_WHEN_RECEIVE_STD.toString()
          .equals(crDTO.getActionType())) {
        crDBRepository.resetApprovalDeptByCr(Long.valueOf(crDTO.getCrId()));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result = Constants.CR_RETURN_MESSAGE.ERROR;
    }
    return result;
  }

  /**
   * Tự động phê duyệt CR trong truong hợp trưởng đơn vị tạo/cập nhật CR
   */
  @Override
  public ResultDTO actionAutomaticApproveCrIncaseOfManagerCreateOrEditCR(
      CrInsiteDTO crDTO, String locale) {
    //R_xxx uyquyen _ Check lai, goi ham nay khi BGD phe duyet level 1
    ResultDTO result = new ResultDTO();
    result.setKey(Constants.CR_RETURN_MESSAGE.SUCCESS);
    try {
      boolean isManager = userRepository.isManagerOfUnits(
          Long.valueOf(crDTO.getUserLogin()));
      if (Constants.CR_TYPE.EMERGENCY.equals(crDTO.getCrType())) {
        result.setKey(Constants.CR_RETURN_MESSAGE.SUCCESS);
        return result;
      }
      if (isManager) {
        Long actionType = Constants.CR_ACTION_CODE.APPROVE;
        Long crId = Long.parseLong(crDTO.getCrId());
        Long userId = Long.parseLong(crDTO.getUserLogin());
        Long userDept = Long.parseLong(crDTO.getUserLoginUnit());
        // truongnt add process dayoff
        if (locale == null) {
          processDayOff(crDTO, Constants.CR_ACTION_CODE.APPROVE.toString());
        }
        CrApprovalDepartmentInsiteDTO crApprovalDepartmentDTO
            = crDBRepository.getCurrentCrApprovalDepartmentDTO(crId, userDept);
        if (crApprovalDepartmentDTO != null) {
          if (!crDBRepository.isLastDepartment(crId)) {
            result.setKey(Constants.CR_RETURN_MESSAGE.APPROVEINFIRSTPLACE);
            crDBRepository.updateCurentApprovalDepartment(crApprovalDepartmentDTO,
                crDTO,
                crId,
                actionType,
                null,
                userId,
                userDept);
            List<CrApprovalDepartmentInsiteDTO> lst
                = crDBRepository.getNextApprovalDepartment(
                crId,
                Long.valueOf(crApprovalDepartmentDTO.getCadtLevel()));
            crDBRepository.updateNextApprovalDepartment(lst);
            crDBRepository.insertIntoHistoryOfCr(crDTO, crId, actionType, null,
                userId, userDept,
                Constants.CR_STATE.OPEN, locale
            );
          } else {
            result.setKey(Constants.CR_RETURN_MESSAGE.APPROVECRINFIRSTPLACE);
            crDBRepository.updateCurentApprovalDepartment(crApprovalDepartmentDTO,
                crDTO,
                crId,
                actionType,
                null,
                userId,
                userDept);
            crDBRepository.updateCrStatusInCaseOfApproveNoSession(actionType, crId, crDTO, locale);
          }
        }
      }
      getEntityManager()
          .flush();// Do not Remove. tiennv them de load du lieu cap nhat moi nhat trong session.
      ResultDTO retSMS = smsDBRepository.actionSendSmsIncaseOfCreateCROrUpdateCR(result, crDTO);
      if (retSMS != null) {
        result.setMessage(retSMS.getMessage());
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result.setKey(Constants.CR_RETURN_MESSAGE.ERROR);
    }
    return result;
  }

  @Override
  public void processDayOff(CrInsiteDTO crDTO, String actionRight) {
    try {
      Date now = new Date();
      Date earliestStartTime = crDTO.getEarliestStartTime();
      Date latestStartTime = crDTO.getLatestStartTime();
      Date disturbanceStartTime = null;
      Date disturbanceEndTime = null;
      if (crDTO.getDisturbanceStartTime() != null && crDTO.getDisturbanceEndTime() != null) {
        disturbanceStartTime = crDTO.getDisturbanceStartTime();
        disturbanceEndTime = crDTO.getDisturbanceEndTime();
      }
      String count = catItemRepository.countDayOff("0", DateTimeUtils.convertDateToString(now),
          DateTimeUtils.convertDateToString(DateUtil.addDay(now, 2)), null);
      if (Constants.CR_TYPE.STANDARD.toString().equals(crDTO.getCrType()) && "121"
          .equals(crDTO.getImpactSegment()) && (
          Constants.CR_ACTION_RIGHT.CAN_APPROVE_STANDARD.equals(actionRight)
              || Constants.CR_ACTION_RIGHT.CAN_APPROVE.equals(actionRight))
          && DateUtil.addDay(now, 2 + Integer.parseInt(count)).after(earliestStartTime)) {
        boolean checkTacDongDem = false;
        if (earliestStartTime.getHours() > latestStartTime.getHours()) {
          checkTacDongDem = true;
        }
        earliestStartTime = DateUtil.addDay(now, 2 + Integer.parseInt(count));
        latestStartTime = DateUtil.addDay(now, 2 + Integer.parseInt(count) + (checkTacDongDem ? 1 : 0));
        if (crDTO.getDisturbanceStartTime() != null && crDTO.getDisturbanceEndTime() != null) {
          disturbanceStartTime = DateUtil.addDay(now, 2 + Integer.parseInt(count));
          disturbanceEndTime = DateUtil.addDay(now, 2 + Integer.parseInt(count) + (checkTacDongDem ? 1 : 0));
        }

        String temp =
            DateTimeUtils.convertDateToString(earliestStartTime, "dd/MM/yyyy") + " " +
                DateTimeUtils.convertDateToString(crDTO.getEarliestStartTime(), "HH:mm:ss");
        earliestStartTime = DateTimeUtils.convertStringToDate(temp);

        temp = DateTimeUtils.convertDateToString(latestStartTime, "dd/MM/yyyy") + " " +
            DateTimeUtils.convertDateToString(crDTO.getLatestStartTime(), "HH:mm:ss");
        latestStartTime = DateTimeUtils.convertStringToDate(temp);

        if (crDTO.getDisturbanceStartTime() != null && crDTO.getDisturbanceEndTime() != null) {
          temp =
              DateTimeUtils.convertDateToString(disturbanceStartTime, "dd/MM/yyyy") + " " +
                  DateTimeUtils.convertDateToString(crDTO.getDisturbanceStartTime(), "HH:mm:ss");
          disturbanceStartTime = DateTimeUtils.convertStringToDate(temp);

          temp = DateTimeUtils.convertDateToString(disturbanceEndTime, "dd/MM/yyyy") + " " +
              DateTimeUtils.convertDateToString(crDTO.getDisturbanceEndTime(), "HH:mm:ss");
          disturbanceEndTime = DateTimeUtils.convertStringToDate(temp);
        }
      }
      crDTO.setEarliestStartTime(earliestStartTime);
      crDTO.setLatestStartTime(latestStartTime);
      if (crDTO.getDisturbanceStartTime() != null && crDTO.getDisturbanceEndTime() != null) {
        crDTO.setDisturbanceStartTime(disturbanceStartTime);
        crDTO.setDisturbanceEndTime(disturbanceEndTime);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public CrInsiteDTO findCrById(Long id, UserToken userTokenGNOC) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "find-cr-by-id");
    Double offset = 0D;
    if (userTokenGNOC != null) {
      offset = userRepository.getOffsetFromUser(userTokenGNOC.getUserName());
    }
    Map<String, Object> params = new HashMap<>();
    params.put("offset", offset);
    params.put("cr_id", id);
    List<CrInsiteDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(CrInsiteDTO.class));
    if (list != null && !list.isEmpty()) {
      CrInsiteDTO crDTO = list.get(0);
      crDTO = crDBRepository.getActiveWOController(crDTO);
      return crDTO;
    }
    return null;
  }

  @Override
  public int insertMopUpdateHis(CrUpdateMopStatusHisDTO hisDTO) {
    try {

      StringBuilder sql = new StringBuilder();
      sql.append(
          "INSERT INTO CR_UPDATE_MOP_STATUS_HIS(ID,CR_NUMBER,CR_STATUS,LIST_MOP,RESULT,DETAIL,INSERT_TIME)");
      sql.append(
          " VALUES(open_pm.CR_UPDATE_MOP_STATUS_HIS_SEQ.nextval,:crNumber,:crStatus,:lstMop,:result,:detail,:inserTime) ");

      String crNumber = hisDTO.getCrNumber() == null ? "" : hisDTO.getCrNumber();
      String crStatus = hisDTO.getCrStatus() == null ? "" : hisDTO.getCrStatus();
      String lstMop = hisDTO.getLstMop() == null ? "" : hisDTO.getLstMop();
      String result = hisDTO.getResult() == null ? "" : hisDTO.getResult();
      String detail = hisDTO.getDetail() == null ? "" : hisDTO.getDetail();

      Map<String, Object> params = new HashMap<>();
      params.put("crNumber", crNumber);
      params.put("crStatus", crStatus);
      params.put("lstMop", lstMop);
      params.put("result", result);
      params.put("detail", detail);
      params.put("inserTime", new Date());
      getNamedParameterJdbcTemplate().update(sql.toString(), params);
      return 1;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      getEntityManager().flush();
      getEntityManager().clear();
    }

    return 0;
  }

  /**
   * Xử lý phê duyệt CR
   */
  @Override
  public String actionApproveCR(CrInsiteDTO crDTO, String locale) {
    try {
      if (crDTO == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      String actionTypeStr = crDTO.getActionType();
      String crIdStr = crDTO.getCrId();
      String userIdStr = crDTO.getUserLogin();
      String userIdDeptStr = crDTO.getUserLoginUnit();//R_xxx uyquyen
      //20160530 daitt1 chinh sua chuc nang phe duyet cr
      List<CrApprovalDepartmentInsiteDTO> listLowerLevelDept;
      //20160530 daitt1 chinh sua chuc nang phe duyet cr
      Long userId = null;
      Long userDept = null;
      if (userIdStr != null) {
        userId = Long.valueOf(userIdStr);
      }
      if (userIdDeptStr != null) {
        userDept = Long.valueOf(userIdDeptStr);
      }
      if (StringUtils.isNotNullOrEmpty(actionTypeStr) && StringUtils.isNotNullOrEmpty(crIdStr)
          && userId != null
          && userDept != null) {
//        boolean isApproveLv2CallbackUpApprovalDep = false;//tiennv them check TH ma ko gui sms
        Long actionType = Long.parseLong(actionTypeStr.trim());
        Long crId = Long.parseLong(crIdStr);
        CrApprovalDepartmentInsiteDTO crApprovalDepartmentDTO
            = crDBRepository.getCurrentCrApprovalDepartmentDTO(crId, userDept);
        if (crApprovalDepartmentDTO == null) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        //20160530 daitt1 chinh sua chuc nang phe duyet
        Long currentPosition = Long.valueOf(crApprovalDepartmentDTO.getCadtLevel());
        listLowerLevelDept = crDBRepository.getLowerLevelUnAppovedRecords(crId, currentPosition);
        for (CrApprovalDepartmentInsiteDTO lowerCrAppDptDTO : listLowerLevelDept) {
          crDBRepository.updateLowerLevelUnApprovedRecord(lowerCrAppDptDTO,
              crDTO,
              crId, actionType,
              crDTO.getActionReturnCodeId() == null ? null
                  : Long.valueOf(crDTO.getActionReturnCodeId()),
              userId);
        }
        //tiennv them -> hien tai chuc nang sms phe duyet dang chua chinh xac
        boolean isLastDepartment = crDBRepository.isLastDepartment(crId);
        //20160530 daitt1 chinh sua chuc nang phe duyet
        if (Constants.CR_ACTION_CODE.APPROVE.equals(actionType)) {
          if (!isLastDepartment) {
            crDBRepository.updateCurentApprovalDepartment(crApprovalDepartmentDTO,
                crDTO,
                crId,
                actionType,
                null,
                userId,
                userDept);
            List<CrApprovalDepartmentInsiteDTO> lst
                = crDBRepository.getNextApprovalDepartment(
                crId,
                Long.valueOf(crApprovalDepartmentDTO.getCadtLevel()));
            crDBRepository.updateNextApprovalDepartment(lst);
            //R_xxx uyquyen _ Neu la cap BGD phe duyet thi tu dong phe duyet level 2
            //R_xxx uyquyen _ Ghi lich su chu thich ro rang
            crDBRepository.insertIntoHistoryOfCr(crDTO, crId, actionType, null,
                userId, userDept,
                Constants.CR_STATE.OPEN, locale
                //                                Long.valueOf(crDTO.getState().trim())
            );
          } else {
            crDBRepository.updateCurentApprovalDepartment(crApprovalDepartmentDTO,
                crDTO,
                crId,
                actionType,
                null,
                userId,
                userDept);
//            isApproveLv2CallbackUpApprovalDep = true;

            crDBRepository.updateCrStatusInCaseOfApproveNoSession(actionType, crId, crDTO, locale);
          }
        }
        if (Constants.CR_ACTION_CODE.REJECT.equals(actionType)) {
          String returnCodeStr = crDTO.getActionReturnCodeId();
          Long returnCode = null;
          if (StringUtils.isStringNullOrEmpty(returnCodeStr)) {
            return Constants.CR_RETURN_MESSAGE.ERROR;
          }
          returnCode = Long.valueOf(returnCodeStr);
          crDBRepository.updateCurentApprovalDepartment(crApprovalDepartmentDTO,
              crDTO,
              crId,
              actionType,
              returnCode,
              userId,
              userDept);

          crDBRepository.updateCrStatusInCaseOfApproveNoSession(actionType, crId, crDTO, locale);
          crDBRepository.actionCloseCrIncaseOfRejectPrimaryCR(crDTO, actionType, locale);
        }
        if (Constants.CR_ACTION_CODE.CLOSECR_APPROVE_STD.equals(actionType)) {
          String returnCodeStr = crDTO.getActionReturnCodeId();

          if (StringUtils.isStringNullOrEmpty(returnCodeStr)) {
            return "ERROR";
          }
          crDBRepository.updateCrStatusInCaseOfApproveNoSession(actionType, crId, crDTO, locale);
          crDBRepository.actionCloseCrIncaseOfRejectPrimaryCR(crDTO, actionType, locale);
        }
        if (Constants.CR_ACTION_CODE.RESOLVE_APPROVE_STD.equals(actionType)) {
          String returnCodeStr = crDTO.getActionReturnCodeId();
          Long returnCode = null;
          if (StringUtils.isStringNullOrEmpty(returnCodeStr)) {
            return "ERROR";
          }
          returnCode = Long.valueOf(returnCodeStr);
          crDBRepository.updateCurentApprovalDepartment(crApprovalDepartmentDTO,
              crDTO,
              crId,
              actionType,
              returnCode,
              userId,
              userDept);

          crDBRepository.updateCrStatusInCaseOfApproveNoSession(actionType, crId, crDTO, locale);
        }
        if (Constants.CR_ACTION_CODE.INCOMPLETE_APPROVE_STD.equals(actionType)) {
          String returnCodeStr = crDTO.getActionReturnCodeId();
          if (StringUtils.isStringNullOrEmpty(returnCodeStr)) {
            return "ERROR";
          }
          crDBRepository.updateCrStatusInCaseOfApproveNoSession(actionType, crId, crDTO, locale);
        }

        smsDBRepository.actionSendSmsIncaseOfApproveCR(actionType, crDTO, listLowerLevelDept,
            isLastDepartment);
//        if(isApproveLv2CallbackUpApprovalDep) {//tiennv them
//            crDBRepository.updateCurentApprovalDepartment(crApprovalDepartmentDTO,
//                crDTO,
//                crId,
//                actionType,
//                null,
//                userId,
//                userDept);
//        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return "EXCEPTION";
    }
    return Constants.CR_RETURN_MESSAGE.SUCCESS;
  }

  /**
   * Tham dinh CR
   */
  @Override
  public String actionAppraiseCr(CrInsiteDTO crDTO, String locale) {
    try {
      if (crDTO == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      Long userId = stringToLong(crDTO.getUserLogin());
      Long deptId = stringToLong(crDTO.getUserLoginUnit());//R_xxx uyquyen
      if (userId == null || deptId == null || crDTO.getActionType() == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      String actionType = crDTO.getActionType();
      String returnCode = crDTO.getActionReturnCodeId();
      String crIdStr = crDTO.getCrId();
      Long crId = Long.parseLong(crIdStr.trim());
      CrInsiteDTO crDTOtoSendSMS = smsDBRepository.getCrById(crId);
      if (Constants.CR_ACTION_CODE.RETURN_TO_MANAGER_BY_APPRAISER.toString()
          .equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (StringUtils.isStringNullOrEmpty(returnCode)
            || "0".equals(returnCode.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfAppraisal(Long.parseLong(actionType), crId, crDTO, locale);
      }
      if (Constants.CR_ACTION_CODE.CLOSE_BY_APPRAISER.toString().equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(returnCode)
            || "0".equals(returnCode.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfAppraisal(Long.parseLong(actionType), crId, crDTO, locale);
      }
      if (Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_APPRAISER.toString()
          .equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(returnCode)
            || "0".equals(returnCode.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfAppraisal(Long.parseLong(actionType), crId, crDTO, locale);
        //Clear state approval department
        crDBRepository.resetApprovalDeptByCr(crId);
      }
      if (Constants.CR_ACTION_CODE.ASSIGN_TO_EMPLOYEE_APPRAISAL.toString()
          .equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .assignAppraiseToEmployee(Long.parseLong(actionType), crId, crDTO, true, locale);

      }
      if (Constants.CR_ACTION_CODE.APPRAISE.toString().equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        //Trưởng phòng tiếp nhận thì set nguoi thẩm định là trưởng phòng
        if (crDTO.getConsiderUserId() == null) {
          crDTO.setAssignUserId(crDTO.getUserLogin());
          crDBRepository
              .assignAppraiseToEmployee(Long.parseLong(actionType), crId, crDTO, false, locale);
        }
        //Trưởng phòng tiếp nhận thì set nguoi thẩm định là trưởng phòng
        crDBRepository
            .updateCrStatusInCaseOfAppraisal(Long.parseLong(actionType), crId, crDTO, locale);
      }
      getEntityManager().flush();
      smsDBRepository.actionSendSms(Long.parseLong(actionType), crDTO, crDTOtoSendSMS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return "EXCEPTION";
    }
    return Constants.CR_RETURN_MESSAGE.SUCCESS;
  }

  /**
   * Xử lý kiểm tra đầu vào Gồm 3 action: *** Dóng CR: chuyển CR ve Close + return code + ghi chú
   * *** Trả ve nguoi tạo: chuyển CR ve Incomplete + return code + ghi chú *** Giao thẩm định: chon
   * đơn vị thẩm định, chuyển trạng thái CR sang INCOMPLETE Ràng buộc: ** Các hành động kèm theo:
   * *** Lưu lịch sử *** Gửi tin nhắn
   */
  @Override
  public String actionVerify(CrInsiteDTO crDTO, String locale) {
    try {
      if (crDTO == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      Long userId = stringToLong(crDTO.getUserLogin());
      Long deptId = stringToLong(crDTO.getUserLoginUnit());
      if (userId == null || deptId == null || crDTO.getActionType() == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      String actionType = crDTO.getActionType();
      String returnCode = crDTO.getActionReturnCodeId();
      String crIdStr = crDTO.getCrId();
      Long crId = Long.parseLong(crIdStr.trim());
      CrInsiteDTO crDTOtoSendSMS = smsDBRepository.getCrById(crId);
      if (Constants.CR_ACTION_CODE.CHANGE_CR_TYPE.toString().equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository.updateCrTypeInCaseOfVerify(crId, crDTO);
        crDBRepository.insertIntoHistoryOfCr(crDTO, crId,
            Long.parseLong(actionType),
            Long.parseLong(returnCode),
            userId, deptId,
            //                    Long.valueOf(crDTO.getState().trim())
            Constants.CR_STATE.QUEUE, locale
        );
      }
      if (Constants.CR_ACTION_CODE.CLOSE_BY_MANAGER.toString().equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(returnCode)
            || "0".equals(returnCode.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfVerify(Long.parseLong(actionType), crId, crDTO, locale);
      }
      if (Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER.toString()
          .equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(returnCode)
            || "0".equals(returnCode.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfVerify(Long.parseLong(actionType), crId, crDTO, locale);
        //Clear state approval department
        crDBRepository.resetApprovalDeptByCr(crId);
      }
      if (Constants.CR_ACTION_CODE.ASSIGN_TO_CONSIDER.toString().equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfVerify(Long.parseLong(actionType), crId, crDTO, locale);
      }
      if (Constants.CR_ACTION_CODE.ACCEPT_BY_MANAGER_PRE.toString().equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfVerify(Long.parseLong(actionType), crId, crDTO, locale);
      }
      if (Constants.CR_ACTION_CODE.CHANGE_TO_CAB.toString().equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfVerify(Long.parseLong(actionType), crId, crDTO, locale);
      }
      if (Constants.CR_ACTION_CODE.CHANGE_TO_SCHEDULE.toString().equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfVerify(Long.parseLong(actionType), crId, crDTO, locale);
      }

      smsDBRepository.actionSendSms(Long.parseLong(actionType), crDTO, crDTOtoSendSMS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return "EXCEPTION";
    }
    return Constants.CR_RETURN_MESSAGE.SUCCESS;
  }

  /**
   * Thẩm định CR
   */
  @Override
  public String actionScheduleCr(CrInsiteDTO crDTO, String locale) {
    try {
      if (crDTO == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      Long userId = stringToLong(crDTO.getUserLogin());
      Long deptId = stringToLong(crDTO.getUserLoginUnit());
      if (userId == null || deptId == null || crDTO.getActionType() == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      String actionType = crDTO.getActionType();
      String returnCode = crDTO.getActionReturnCodeId();
      String crIdStr = crDTO.getCrId();
      Long crId = null;
      try {
        crId = Long.parseLong(crIdStr.trim());
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      CrInsiteDTO crDTOtoSendSMS = smsDBRepository.getCrById(crId);
      if (Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER_SCH.toString()
          .equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(returnCode)
            || "0".equals(returnCode.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (crId == null
            || "".equals(crIdStr.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfScheduleNoSession(Long.parseLong(actionType), crId, crDTO,
                locale);
        //Clear state approval department
        crDBRepository.resetApprovalDeptByCr(crId);
      }
      if (Constants.CR_ACTION_CODE.RETURN_TO_APPRAISE_BY_MANAGER_SCH.toString()
          .equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(returnCode)
            || "0".equals(returnCode.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (crId == null
            || "".equals(crIdStr.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfScheduleNoSession(Long.parseLong(actionType), crId, crDTO,
                locale);
      }
      if (Constants.CR_ACTION_CODE.RETURN_TO_CAB_WHEN_SCHEDULE.toString()
          .equals(actionType.trim())) {
//                if (returnCode == null
//                        || returnCode.trim().equals("")
//                        || returnCode.trim().equals("0")) {
//                    return Constants.CR_RETURN_MESSAGE.ERROR;
//                }
        if (crId == null
            || "".equals(crIdStr.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfScheduleNoSession(Long.parseLong(actionType), crId, crDTO,
                locale);
      }
      if (Constants.CR_ACTION_CODE.CLOSE_BY_MANAGER_SCH.toString().equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(returnCode)
            || "0".equals(returnCode.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (crId == null
            || "".equals(crIdStr.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfScheduleNoSession(Long.parseLong(actionType), crId, crDTO,
                locale);
      }
      if (Constants.CR_ACTION_CODE.SCHEDULE.toString().equals(actionType.trim())) {
        if (crId == null
            || "".equals(crIdStr.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfScheduleNoSession(Long.parseLong(actionType), crId, crDTO,
                locale);

      }
      smsDBRepository.actionSendSms(Long.parseLong(actionType), crDTO, crDTOtoSendSMS);
    } catch (Exception e) {
      //e.printStackTrace();
      log.error(e.getMessage(), e);
      return "EXCEPTION";
    }
    return Constants.CR_RETURN_MESSAGE.SUCCESS;
  }

  /**
   * Tiếp nhận CR
   */
  @Override
  public String actionReceiveCr(CrInsiteDTO crDTO, String locale) {
    //Date start = new Date();
    try {
      if (crDTO == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      Long userId = stringToLong(crDTO.getUserLogin());
      Long deptId = stringToLong(crDTO.getUserLoginUnit());//R_xxx uyquyen
      if (userId == null || deptId == null || crDTO.getActionType() == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      String actionType = crDTO.getActionType();
      String returnCode = crDTO.getActionReturnCodeId();
      String crIdStr = crDTO.getCrId();
      Long crId = null;
      try {
        crId = Long.parseLong(crIdStr.trim());
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      CrInsiteDTO crDTOtoSendSMS = smsDBRepository.getCrById(crId);

      if (Constants.CR_ACTION_CODE.RETURN_TO_MANAGER_BY_IMPL.toString().equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(returnCode)
            || "0".equals(returnCode.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (crId == null) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfReceivingNoSession(Long.parseLong(actionType), crId, crDTO,
                locale);
      } else if (Constants.CR_ACTION_CODE.RETURN_TO_APPRAISER_BY_IMPL.toString()
          .equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(returnCode)
            || "0".equals(returnCode.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (crId == null) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfReceivingNoSession(Long.parseLong(actionType), crId, crDTO,
                locale);
      } else if (Constants.CR_ACTION_CODE.ASSIGN_EXC_TO_EMPLOYEE.toString()
          .equals(actionType.trim())) {
        if (crId == null) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfReceivingNoSession(Long.parseLong(actionType), crId, crDTO,
                locale);
      } else if (Constants.CR_ACTION_CODE.ACCEPT.toString().equals(actionType.trim())) {
        if (crId == null) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfReceivingNoSession(Long.parseLong(actionType), crId, crDTO,
                locale);
      } else if (Constants.CR_ACTION_CODE.CLOSE_EXCUTE_STD.toString().equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(returnCode)
            || "0".equals(returnCode.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (crId == null) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfReceivingNoSession(Long.parseLong(actionType), crId, crDTO,
                locale);
      } else if (Constants.CR_ACTION_CODE.CLOSE_EXCUTE_EMERGENCY.toString()
          .equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(returnCode)
            || "0".equals(returnCode.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (crId == null) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfReceivingNoSession(Long.parseLong(actionType), crId, crDTO,
                locale);
      } else if (Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_WHEN_EXCUTE_STD.toString()
          .equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(returnCode)
            || "0".equals(returnCode.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (crId == null) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfReceivingNoSession(Long.parseLong(actionType), crId, crDTO,
                locale);

      } else if (Constants.CR_ACTION_CODE.RESOLVE_WITH_FAILT_STATUS_DUE_TO_WO.toString()
          .equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(returnCode)
            || "0".equals(returnCode.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (crId == null) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfReceivingNoSession(Long.parseLong(actionType), crId, crDTO,
                locale);
      }
            /*
             if (actionType.trim().equals(Constants.CR_ACTION_CODE.REJECT_EXCUTE_STD.toString())) {
             if (returnCode == null
             || returnCode.trim().equals("")
             || returnCode.trim().equals("0")) {
             return Constants.CR_RETURN_MESSAGE.ERROR;
             }
             if (crIdStr == null
             || crIdStr.trim().equals("")) {
             return Constants.CR_RETURN_MESSAGE.ERROR;
             }
             cdbdao.updateCrStatusInCaseOfReceiving(session, Long.parseLong(actionType), crId, crDTO);
             }
             */
      smsDBRepository.actionSendSms(Long.parseLong(actionType), crDTO, crDTOtoSendSMS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return "EXCEPTION";
    }
//    Date end = new Date();
//    LogDBHandler.getMapLogWS().put("dbTime", "" + (end.getTime() - start.getTime()));
    return Constants.CR_RETURN_MESSAGE.SUCCESS;
  }

  /**
   * Resolve CR
   */
  @Override
  public String actionResolveCr(CrInsiteDTO crDTO, String locale) {
    //Date start = new Date();
    try {
      if (crDTO == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      Long userId = stringToLong(crDTO.getUserLogin());
      Long deptId = stringToLong(crDTO.getUserLoginUnit());
      if (userId == null || deptId == null || crDTO.getActionType() == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      String actionType = crDTO.getActionType();
      String returnCode = crDTO.getActionReturnCodeId();
      String crIdStr = crDTO.getCrId();
      Long crId = Long.parseLong(crIdStr.trim());
      CrInsiteDTO crDTOtoSendSMS = smsDBRepository.getCrById(crId);
      if (Constants.CR_ACTION_CODE.RESOLVE.toString().equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(returnCode)
            || "0".equals(returnCode.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (!crDBRepository.validateCloseWoWhenResolveCR(crDTO)) {
          return Constants.CR_RETURN_MESSAGE.MUSTCLOSEALLWO;
        }
        //start = new Date();
        crDBRepository
            .updateCrStatusInCaseOfResolve(Long.parseLong(actionType), crId, crDTO, locale);
      }
      smsDBRepository.actionSendSms(Long.parseLong(actionType), crDTO, crDTOtoSendSMS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return "EXCEPTION";
    }
    // Date end = new Date();
    // LogDBHandler.getMapLogWS().put("dbTime", "" + (end.getTime() - start.getTime()));
    return Constants.CR_RETURN_MESSAGE.SUCCESS;
  }

  @Override
  public String actionCloseCr(CrInsiteDTO crDTO, String locale) {
    //Date start = new Date();
    try {
      if (crDTO == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      Long userId = stringToLong(crDTO.getUserLogin());
      Long deptId = stringToLong(crDTO.getUserLoginUnit());//R_xxx uyquyen
      if (userId == null || deptId == null || crDTO.getActionType() == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      String actionType = crDTO.getActionType();
      String returnCode = crDTO.getActionReturnCodeId();
      String crIdStr = crDTO.getCrId();
      CrInsiteDTO crDTOtoSendSMS = smsDBRepository.getCrById(stringToLong(crIdStr));
      if (Constants.CR_ACTION_CODE.CLOSECR.toString().equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(returnCode)
            || "0".equals(returnCode.trim())) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        Long crId = Long.parseLong(crIdStr.trim());
        //start = new Date();
        crDBRepository
            .updateCrStatusInCaseOfCloseCR(Long.parseLong(actionType), crId, crDTO, locale);
      }
      smsDBRepository.actionSendSms(Long.parseLong(actionType), crDTO, crDTOtoSendSMS);
    } catch (Exception e) {
      //
      log.error(e.getMessage(), e);
      return "EXCEPTION";
    }
    //Date end = new Date();
    //LogDBHandler.getMapLogWS().put("dbTime", "" + (end.getTime() - start.getTime()));
    return Constants.CR_RETURN_MESSAGE.SUCCESS;
  }

  @Override
  public String actionAssignCab(CrInsiteDTO crDTO, String locale) {
    try {
      if (crDTO == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      Long userId = stringToLong(crDTO.getUserLogin());
      Long deptId = stringToLong(crDTO.getUserLoginUnit());//R_xxx uyquyen
      if (userId == null || deptId == null || crDTO.getActionType() == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      String actionType = crDTO.getActionType();
      //String returnCode = crDTO.getActionReturnCodeId();
      String crIdStr = crDTO.getCrId();
      CrInsiteDTO crDTOtoSendSMS = smsDBRepository.getCrById(stringToLong(crIdStr));
      if (Constants.CR_ACTION_CODE.CLOSE_BY_MANAGER_CAB.toString().equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        Long crId = Long.parseLong(crIdStr.trim());
        crDBRepository
            .updateCrStatusInCaseOfAssignCabCR(Long.parseLong(actionType), crId, crDTO, locale);
      }
      if (Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER_CAB.toString()
          .equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        Long crId = Long.parseLong(crIdStr.trim());
        crDBRepository
            .updateCrStatusInCaseOfAssignCabCR(Long.parseLong(actionType), crId, crDTO, locale);
      }
      if (Constants.CR_ACTION_CODE.RETURN_TO_APPRAISE_BY_MANAGER_CAB.toString()
          .equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        Long crId = Long.parseLong(crIdStr.trim());
        crDBRepository
            .updateCrStatusInCaseOfAssignCabCR(Long.parseLong(actionType), crId, crDTO, locale);
      }
      if (Constants.CR_ACTION_CODE.ASSIGN_TO_CAB.toString().equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        Long crId = Long.parseLong(crIdStr.trim());
        crDBRepository
            .updateCrStatusInCaseOfAssignCabCR(Long.parseLong(actionType), crId, crDTO, locale);
      }

      smsDBRepository.actionSendSms(Long.parseLong(actionType), crDTO, crDTOtoSendSMS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return "ERROR";
    }
    return Constants.CR_RETURN_MESSAGE.SUCCESS;
  }

  @Override
  public String actionCab(CrInsiteDTO crDTO, String locale) {
    try {
      if (crDTO == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      Long userId = stringToLong(crDTO.getUserLogin());
      Long deptId = stringToLong(crDTO.getUserLoginUnit());//R_xxx uyquyen
      if (userId == null || deptId == null || crDTO.getActionType() == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      String actionType = crDTO.getActionType();
      //String returnCode = crDTO.getActionReturnCodeId();
      String crIdStr = crDTO.getCrId();
      CrInsiteDTO crDTOtoSendSMS = smsDBRepository.getCrById(stringToLong(crIdStr));
      if (Constants.CR_ACTION_CODE.CAB.toString().equals(actionType.trim())
          || Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_WHEN_CAB.toString()
          .equals(actionType.trim())
          || Constants.CR_ACTION_CODE.RETURN_TO_CONSIDER_WHEN_CAB.toString()
          .equals(actionType.trim())
          || Constants.CR_ACTION_CODE.RETURN_TO_MANAGE_WHEN_CAB.toString()
          .equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        Long crId = Long.parseLong(crIdStr.trim());
        crDBRepository.updateCrStatusInCaseOfCabCR(Long.parseLong(actionType), crId, crDTO, locale);
      }
//            else if(actionType.trim().equals(Constants.CR_ACTION_CODE.RETURN_TO_MANAGE_WHEN_CAB.toString())){
//                List<UserCabCrForm> lstCab=crGeneralDAO.getListUserCab(crDTO.getImpactSegmentId(), crDTO.getChangeOrginatorUnit());
//                if(lstCab!=null&&!lstCab.isEmpty()){
//                    UserCabCrForm form1=lstCab.get(0);
//                    if
//                }
//            }
      smsDBRepository.actionSendSms(Long.parseLong(actionType), crDTO, crDTOtoSendSMS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return "EXCEPTION";
    }
    return Constants.CR_RETURN_MESSAGE.SUCCESS;
  }

  @Override
  public String actionEditCr(CrInsiteDTO crDTO, String locale) {
    try {
      if (crDTO == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      Long userId = stringToLong(crDTO.getUserLogin());
      Long deptId = stringToLong(crDTO.getUserLoginUnit());//R_xxx uyquyen
      if (userId == null || deptId == null || crDTO.getActionType() == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      String actionType = crDTO.getActionType();
//            String returnCode = crDTO.getActionReturnCodeId();
      String crIdStr = crDTO.getCrId();
      if (Constants.CR_ACTION_CODE.EDIT_CR_BY_QLTD.toString().equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        Long crId = Long.parseLong(crIdStr.trim());
        crDBRepository
            .updateCrInCaseOfQLTDChangeCR(Long.parseLong(actionType), crId, crDTO, locale);
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return "EXCEPTION";
    }
    return Constants.CR_RETURN_MESSAGE.SUCCESS;
  }

  @Override
  public String actionCancelCR(CrInsiteDTO crDTO) {
    String ret = Constants.CR_RETURN_MESSAGE.SUCCESS;
    try {
      if (crDTO != null && crDTO.getCrId() != null) {
        StringBuilder sql = new StringBuilder("");
        Map<String, Object> params = new HashMap<>();
        sql.append(" update cr set state = 10 ");
        if (crDTO.getNotes() != null) {
          sql.append(" , notes = :notes");
          params.put("notes", crDTO.getNotes().trim());
        }
        sql.append(" where cr_id = :cr_id ");
        params.put("cr_id", crDTO.getCrId().trim());
        int rowUp = getNamedParameterJdbcTemplate().update(sql.toString(), params);
        if (rowUp <= 0) {
          return RESULT.ERROR;
        }
        getEntityManager().flush();
      }
    } catch (Exception e) {
      ret = Constants.CR_RETURN_MESSAGE.ERROR;
      log.error(e.getMessage(), e);
    }
    return ret;
  }

  @Override
  public List<CrInsiteDTO> getListCRIdForExport(CrInsiteDTO crDTO) {

    List<List<Long>> searchCrIds = null;
    List<Long> allIps = null;
    try {
      if (crDTO != null) {
        if (crDTO.getSearchImpactedNodeIpIds() != null && !crDTO.getSearchImpactedNodeIpIds()
            .isEmpty()) {
          Date startDate = crDTO.getEarliestStartTime();
          Date endDate = DateTimeUtils.convertStringToDate(crDTO.getEarliestStartTimeTo());
          if (startDate == null) {
            startDate = new Date();
          }
          if (endDate == null) {
            endDate = new Date();
          }
          Calendar cal = Calendar.getInstance();
          cal.setTime(startDate);
          cal.add(Calendar.DATE, -7);
          startDate = cal.getTime();
          allIps = getlistCrIdsByNodeInfo(startDate, endDate, crDTO.getSearchImpactedNodeIpIds());
          if (allIps == null || allIps.isEmpty()) {
            return new ArrayList<>();
          }
          int count = allIps.size() / 500;
          if (allIps.size() % 500 != 0) {
            count++;
          }

          List<Long> crids;
          searchCrIds = new ArrayList<>();
          for (int p = 1; p <= count; p++) {
            if (p < count) {
              crids = allIps.subList((p - 1) * 500, p * 500);
            } else {
              crids = allIps.subList((p - 1) * 500, allIps.size());
            }
            if (crids != null && !crids.isEmpty()) {
              searchCrIds.add(crids);
            }
          }

        }

        String userIdStr = crDTO.getUserLogin();
        String userIdDeptStr = crDTO.getUserLoginUnit();
        Long userId = null;
        Long userDept = null;
        if (userIdStr != null) {
          userId = Long.valueOf(userIdStr);
        }
        if (userIdDeptStr != null) {
          userDept = Long.valueOf(userIdDeptStr);
        }
        if (userId != null && userDept != null && crDTO.getSearchType() != null) {
          List<CrInsiteDTO> list = new ArrayList<>();
          Map<String, Object> params = new HashMap<>();
          StringBuffer sql = new StringBuffer();

          Double offset = TimezoneContextHolder.getOffsetDouble();

          sql.append("select  cr.cr_id crId, cr.cr_number crNumber "
              + " , cr.CREATED_DATE as createdDate "
              + " , cr.UPDATE_TIME as updateTime  "
              + " , cr.EARLIEST_START_TIME as earliestStartTime  "
              + " , cr.LATEST_START_TIME as latestStartTime "
              + " from   open_pm.cr cr    "
              + " left join common_gnoc.users usOri "
              + " on cr.change_orginator = usOri.user_id"
              + " where usOri.is_enable = 1 ");
          if (Constants.CR_SEARCH_TYPE.CREATE_EDIT.toString().equals(crDTO.getSearchType())) {
            sql.append(" and ( ( ");
            // <editor-fold desc="Cho phep nguoi tao CR co the sua CR khi CR chua phe duyet hoac INCOMPLETE">
            sql.append(" cr.state in (:state_open, :state_incom, :state_draft) ");
            params.put("state_draft", Constants.CR_STATE.DRAFT);
            params.put("state_open", Constants.CR_STATE.OPEN);
            params.put("state_incom", Constants.CR_STATE.INCOMPLETE);
            sql.append(" and cr.change_orginator_unit = :userDept ");
            params.put("userDept", userDept);
            sql.append(" and cr.change_orginator = :userId ");
            params.put("userId", userId);
            // </editor-fold>

            sql.append(" ) or (");
            // <editor-fold desc="Cho phep tat ca nhan vien trong don vi them WLog khi CR RESOLVE">
            sql.append(" cr.state = :state_resolve ");
            params.put("state_resolve", Constants.CR_STATE.RESOLVE);
            sql.append(" and cr.change_orginator_unit in ( ");
            sql.append(" SELECT unit_id");
            sql.append(" FROM common_gnoc.unit ");
            sql.append(" START WITH  unit_id = :userDept ");
            sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
            sql.append(" )");
            params.put("userDept", userDept);
            // </editor-fold>
            sql.append(" ) )");
          }

          if (Constants.CR_SEARCH_TYPE.LOOKUP.toString().equals(crDTO.getSearchType())) {
            if (StringUtils.isNotNullOrEmpty(crDTO.getState())) {
              String[] arrState = crDTO.getState().split(",");
              List<String> lstState = Arrays.asList(arrState);
              if (lstState == null) {
                lstState = new ArrayList<>();
              }
              List<String> lstStateAll = new ArrayList<>();
              lstStateAll.addAll(lstState);
              lstStateAll.add("-1");

              sql.append(" and cr.state in (:lst_state) ");
              params.put("lst_state", lstStateAll);
            } else {
              sql.append(" and cr.state in (-1) ");
            }
          }

          if (Constants.CR_SEARCH_TYPE.APPROVE.toString().equals(crDTO.getSearchType())) {
            sql.append(" and cr.state = :state_open ");
            params.put("state_open", Constants.CR_STATE.OPEN);
            sql.append(
                " and exists (select 1 from common_gnoc.v_user_role where user_id = :user_id and role_code = :role_code )");
            params.put("user_id", userId);
            params.put("role_code", Constants.CR_ROLE.roleTP);

//                        if(crDTO.getState().equals(Constants.CR_STATE.OPEN.toString())){
            if ("1".equals(crDTO.getIsSearchChildDeptToApprove().trim())) {
              sql.append(" and cr.cr_id in (");
              sql.append(" select cr_id from ");
              sql.append(" cr_approval_department d ");
              sql.append(" where ");
              sql.append(" d.cr_id = cr.cr_id and d.unit_id = :unit_id ");
              sql.append(" and d.status = 0 )");
              params.put("unit_id", userDept);
            } else {
              sql.append(" and cr.cr_id in (");
              sql.append(" with tbl as (");
              sql.append(" select d.unit_id,d.cr_id,d.cadt_level,");
              sql.append(" (select min(b.cadt_level) from cr_approval_department b");
              sql.append(" where  b.cr_id = d.cr_id and b.status = 0) as num");
              sql.append(" from cr_approval_department d");
              sql.append(" where d.unit_id = :unit_id  and d.status = 0)");
              sql.append(" select tb.cr_id   from tbl tb where tb.cadt_level <= tb.num");
              sql.append(" ) ");
              params.put("unit_id", userDept);
            }
          }
          if (Constants.CR_SEARCH_TYPE.VERIFY.toString().equals(crDTO.getSearchType())) {
            sql.append(" and cr.state = :state_queue ");
            params.put("state_queue", Constants.CR_STATE.QUEUE);
            sql.append(" and (cr.change_responsible_unit in ");
            sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
            sql.append(" start with ut.unit_id in ");
            sql.append(" (select ");
            sql.append(" excute_unit_id  from v_manage_cr_config ");
            sql.append(" where ");
            sql.append(" manage_unit = :manage_unit ");
            sql.append(" and scope_id = :scope_id  ");
            sql.append("  AND DEVICE_TYPE IS NULL ");
            sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 ");
            sql.append(" )");
            sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
            sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
                + "  FROM common_gnoc.unit ut "
                + "    START WITH ut.unit_id IN "
                + "    (SELECT excute_unit_id "
                + "    FROM OPEN_PM.v_manage_cr_config "
                + "    WHERE manage_unit = :manage_unit "
                + "    AND scope_id      = :scope_id "
                + "    and IS_SCHEDULE_CR_EMERGENCY <> 1 AND DEVICE_TYPE  IS NOT NULL "
                + "    ) "
                + "    CONNECT BY PRIOR unit_id = parent_unit_id "
                + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
                + "    FROM OPEN_PM.v_manage_cr_config "
                + "    WHERE manage_unit = :manage_unit  AND scope_id = :scope_id AND DEVICE_TYPE  IS NOT NULL)))");
            sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit )");
            params.put("manage_unit", userDept);
            params.put("scope_id", Long.valueOf(crDTO.getScopeId()));

          }
          if (Constants.CR_SEARCH_TYPE.WAIT_CAB.toString().equals(crDTO.getSearchType())) {
            //chi CAB cr thuong
            sql.append(" and cr.state in( :state_wait_cab,:state_cab) ");
            params.put("state_wait_cab", Constants.CR_STATE.WAIT_CAB);
            params.put("state_cab", Constants.CR_STATE.CAB);
            sql.append(" and ( cr.change_responsible_unit in ");
            sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
            sql.append(" start with ut.unit_id in ");
            sql.append(" (select ");
            sql.append(" excute_unit_id  from v_manage_cr_config ");
            sql.append(" where ");
            sql.append(" manage_unit = :manage_unit ");
            sql.append(" and scope_id = :scope_id ");
            sql.append(" )");
            sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
            sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
                + "  FROM common_gnoc.unit ut "
                + "    START WITH ut.unit_id IN "
                + "    (SELECT excute_unit_id "
                + "    FROM OPEN_PM.v_manage_cr_config "
                + "    WHERE manage_unit = :manage_unit "
                + "    AND scope_id      = :scope_id "
                + "    and IS_SCHEDULE_CR_EMERGENCY <> 1 AND DEVICE_TYPE  IS NOT NULL "
                + "    ) "
                + "    CONNECT BY PRIOR unit_id = parent_unit_id "
                + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
                + "    FROM OPEN_PM.v_manage_cr_config "
                + "    WHERE manage_unit = :manage_unit  AND scope_id = :scope_id AND DEVICE_TYPE  IS NOT NULL )))");

//                        sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit )");
            params.put("manage_unit", userDept);
            params.put("scope_id", Long.valueOf(crDTO.getScopeId()));
//                        paramList.add(userDept);

          }

          if (Constants.CR_SEARCH_TYPE.CAB.toString().equals(crDTO.getSearchType())) {
            sql.append(" and (");
            sql.append("( cr.state = :state_cab ");
            params.put("state_cab", Constants.CR_STATE.CAB);
            sql.append(" and cr.user_cab =:user_cab) ");
            params.put("user_cab", userId);

            //cr khan cho phep user duoc cau hinh co the cab khi sap lich
            sql.append(" OR (cr.cr_type = :cr_type_emer ");
            params.put("cr_type_emer", Constants.CR_TYPE.EMERGENCY);
            sql.append(" and cr.state = :state_queue ");
            params.put("state_queue", Constants.CR_STATE.QUEUE);

            sql.append(" and cr.change_responsible_unit in( ");
            sql.append(" select cab.execute_unit_id from open_pm.cr_cab_users cab");
            sql.append(" where cab.user_id=:user_id)");

            sql.append(" and cr.impact_segment in ( ");
            sql.append(" select cab.impact_segment_id from open_pm.cr_cab_users cab");
            sql.append(" where cab.user_id= :user_id)");
            params.put("user_id", userId);
            sql.append(" )  ");
            //cr khan cho phep user duoc cau hinh co the cab khi sap lich

            if (crDTO.getScopeId() != null) {
              sql.append(" OR (  cr.state in ( :state_approve, :state_accept,:state_resolve) ");
              params.put("state_approve", Constants.CR_STATE.APPROVE);
              params.put("state_accept", Constants.CR_STATE.ACCEPT);
              params.put("state_resolve", Constants.CR_STATE.RESOLVE);
              sql.append(" and cr.cr_type = :cr_type_emer ");
              params.put("cr_type_emer", Constants.CR_TYPE.EMERGENCY);
              sql.append(" and cr.change_responsible_unit in ");
              sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
              sql.append(" start with ut.unit_id in ");
              sql.append(" ( ");
              sql.append(" select unit_id ");
              sql.append(" from cr_manager_units_of_scope cmnose ");
              sql.append(" where cmnose.cmse_id = :scope_id");
              params.put("scope_id", Long.valueOf(crDTO.getScopeId()));
              sql.append(" )");
              sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
              sql.append(" ) ) ");
            }
            sql.append(" )");

          }
          if (Constants.CR_SEARCH_TYPE.QLTD.toString().equals(crDTO.getSearchType())) {
            //chi CAB cr thuong
            sql.append(
                " and cr.state in(:state_wait_cab,:state_cab,:state_queue,:state_coor,:state_eval) ");
            params.put("state_wait_cab", Constants.CR_STATE.WAIT_CAB);
            params.put("state_cab", Constants.CR_STATE.CAB);
            params.put("state_queue", Constants.CR_STATE.QUEUE);
            params.put("state_coor", Constants.CR_STATE.COORDINATE);
            params.put("state_eval", Constants.CR_STATE.EVALUATE);
            sql.append(" and ( cr.change_responsible_unit in ");
            sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
            sql.append(" start with ut.unit_id in ");
            sql.append(" (select ");
            sql.append(" excute_unit_id  from v_manage_cr_config ");
            sql.append(" where ");
            sql.append(" manage_unit = :manage_unit");
            sql.append(" and scope_id = :scope_id ");
            sql.append(" )");
            sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
            sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
                + "  FROM common_gnoc.unit ut "
                + "    START WITH ut.unit_id IN "
                + "    (SELECT excute_unit_id "
                + "    FROM OPEN_PM.v_manage_cr_config "
                + "    WHERE manage_unit = :manage_unit "
                + "    AND scope_id      = :scope_id "
                + "    and IS_SCHEDULE_CR_EMERGENCY <> 1 AND DEVICE_TYPE  IS NOT NULL "
                + "    ) "
                + "    CONNECT BY PRIOR unit_id = parent_unit_id "
                + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
                + "    FROM OPEN_PM.v_manage_cr_config "
                + "    WHERE manage_unit = :manage_unit  AND scope_id = :scope_id AND DEVICE_TYPE  IS NOT NULL )))");
            sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit )");
            params.put("manage_unit", userDept);
            params.put("scope_id", Long.valueOf(crDTO.getScopeId()));

          }

          if (CR_SEARCH_TYPE.QLTD_RENEW.toString().equals(crDTO.getSearchType())) {
            sql.append(
                " and cr.state in(:state_accept) ");
            params.put("state_accept", CR_STATE.ACCEPT);
            sql.append(" and ( cr.change_responsible_unit in ");
            sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
            sql.append(" start with ut.unit_id in ");
            sql.append(" (select ");
            sql.append(" excute_unit_id  from v_manage_cr_config ");
            sql.append(" where ");
            sql.append(" manage_unit = :manage_unit");
            sql.append(" and scope_id = :scope_id ");
            sql.append(" )");
            sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
            sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
                + "  FROM common_gnoc.unit ut "
                + "    START WITH ut.unit_id IN "
                + "    (SELECT excute_unit_id "
                + "    FROM OPEN_PM.v_manage_cr_config "
                + "    WHERE manage_unit = :manage_unit "
                + "    AND scope_id      = :scope_id "
                + "    and IS_SCHEDULE_CR_EMERGENCY <> 1 AND DEVICE_TYPE  IS NOT NULL "
                + "    ) "
                + "    CONNECT BY PRIOR unit_id = parent_unit_id "
                + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
                + "    FROM OPEN_PM.v_manage_cr_config "
                + "    WHERE manage_unit = :manage_unit  AND scope_id = :scope_id AND DEVICE_TYPE  IS NOT NULL )))");
            sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit )");
            params.put("manage_unit", userDept);
            params.put("scope_id", Long.valueOf(crDTO.getScopeId()));

          }
          if (Constants.CR_SEARCH_TYPE.Z78.toString().equals(crDTO.getSearchType())) {
            sql.append(" and cr.state = :state_accept ");
            params.put("state_accept", Constants.CR_STATE.ACCEPT);
            sql.append(" and cr.change_responsible_unit in ");
            sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
            sql.append(" start with ut.unit_id in ");
            sql.append(" ( ");
            sql.append(" select unit_id ");
            sql.append(" from cr_manager_units_of_scope cmnose ");
            sql.append(" where cmnose.cmse_id = :scope_id");
            sql.append(" )");
            sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
            sql.append(" )");
            params.put("scope_id", Long.valueOf(crDTO.getScopeId()));

          }
          if (Constants.CR_SEARCH_TYPE.CONSIDER.toString().equals(crDTO.getSearchType())) {
//                        boolean isManager = cdbdao.isManagerOfUnits(sess, userId);
            //tuanpv edit cho phep nhan vien nhin thay CR can tham dinh cua ca don vi
            boolean isManager = true;
            if (isManager) {
              sql.append(" and cr.state = :state_coor ");
              params.put("state_coor", Constants.CR_STATE.COORDINATE);
              sql.append(" and ( cr.consider_unit_id = :unit_id ");
              params.put("unit_id", userDept);
              sql.append(" OR  cr.consider_user_id = :user_id )");
              params.put("user_id", userId);
            } else {
              sql.append(" and cr.state = :state_coor ");
              params.put("state_coor", Constants.CR_STATE.COORDINATE);
              sql.append(" and cr.consider_user_id = :user_id ");
              params.put("user_id", userId);
            }
          }
          if (Constants.CR_SEARCH_TYPE.SCHEDULE.toString().equals(crDTO.getSearchType())) {
            sql.append(" and cr.state = :state_eval ");
            params.put("state_eval", Constants.CR_STATE.EVALUATE);
            sql.append(" and ((cr.change_responsible_unit in ");
            sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
            sql.append(" start with ut.unit_id in ");
            sql.append(" (select ");
            sql.append(" excute_unit_id  from v_manage_cr_config ");
            sql.append(" where ");
            sql.append(" manage_unit = :manage_unit ");
            sql.append(" and scope_id = :scope_id ");
            if (Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
              sql.append(" and IS_SCHEDULE_CR_EMERGENCY = 1 ");
            } else {
              sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 ");
            }
            sql.append(" )");
            sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
            sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
                + "  FROM common_gnoc.unit ut "
                + "    START WITH ut.unit_id IN "
                + "    (SELECT excute_unit_id "
                + "    FROM OPEN_PM.v_manage_cr_config "
                + "    WHERE manage_unit = :manage_unit "
                + "    AND scope_id      = :scope_id "
                + "    AND DEVICE_TYPE  IS NOT NULL ");
            if (Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
              sql.append(" and IS_SCHEDULE_CR_EMERGENCY = 1 ");
            } else {
              sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 ");
            }
            sql.append("    ) "
                + "    CONNECT BY PRIOR unit_id = parent_unit_id "
                + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
                + "    FROM OPEN_PM.v_manage_cr_config "
                + "    WHERE manage_unit = :manage_unit  AND scope_id = :scope_id ");
            if (Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
              sql.append(" and IS_SCHEDULE_CR_EMERGENCY = 1 )) ");
            } else {
              sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 ))");
            }

            sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit))");
            params.put("manage_unit", userDept);
            params.put("scope_id", Long.valueOf(crDTO.getScopeId()));
            sql.append(
                " or (cr.cr_type=:cr_type_emer and cr.process_type_id in (select cr_process_id from cr_ocs_schedule ");
            sql.append(" where user_id=:user_id)) ");
            params.put("cr_type_emer", Constants.CR_TYPE.EMERGENCY);
            params.put("user_id", userId);
            sql.append(" ) ");

          }
          if (Constants.CR_SEARCH_TYPE.EXCUTE.toString().equals(crDTO.getSearchType())) {
            boolean isManager = userRepository.isManagerOfUnits(userId);
            if (isManager) {
              sql.append(
                  " and ( cr.state = :state_approve or (cr.state = :state_accept and cr.earliest_start_time > sysdate)) ");
              params.put("state_approve", Constants.CR_STATE.APPROVE);
              params.put("state_accept", Constants.CR_STATE.ACCEPT);
              sql.append(" and (cr.change_responsible_unit = :responsible_unit ");
              params.put("responsible_unit", userDept);
              sql.append(" or cr.change_responsible = :change_responsible )");
              params.put("change_responsible", userId);
            } else {
              sql.append(" and cr.state = :state_approve ");
              params.put("state_approve", Constants.CR_STATE.APPROVE);
              sql.append(
                  " and ((cr.change_responsible_unit = :responsible_unit and cr.cr_type = 0 )");
              params.put("responsible_unit", userDept);
              sql.append(" or cr.change_responsible = :change_responsible )");
              params.put("change_responsible", userId);
            }
          }
          if (Constants.CR_SEARCH_TYPE.RESOLVE.toString().equals(crDTO.getSearchType())) {
            sql.append(" and cr.state = :state_accept ");
            params.put("state_accept", Constants.CR_STATE.ACCEPT);
            sql.append(" and cr.change_responsible = :change_responsible ");
            params.put("change_responsible", userId);
          }
          if (Constants.CR_SEARCH_TYPE.CLOSE.toString().equals(crDTO.getSearchType())) {
            boolean isManager = userRepository.isManagerOfUnits(userId);
            if (StringUtils.isStringNullOrEmpty(crDTO.getScopeId())) {
              if (isManager) {
                sql.append(" and cr.state = :state_resolve ");
                params.put("state_resolve", Constants.CR_STATE.RESOLVE);
                sql.append(" and cr.manage_unit_id = :manage_unit ");
                params.put("manage_unit", userDept);
              } else {
                sql.append(" and 1=2");//Nhan vien khong co quyen tac dong den CR cho dong
              }
            } else {
              sql.append(" and cr.state = :state_resolve ");
              params.put("state_resolve", Constants.CR_STATE.RESOLVE);
              sql.append(" and ( cr.change_responsible_unit in ");
              sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
              sql.append(" start with ut.unit_id in ");
              sql.append(" (select ");
              sql.append(" excute_unit_id  from v_manage_cr_config ");
              sql.append(" where ");
              sql.append(" manage_unit = :manage_unit ");
              sql.append(" and scope_id = :scope_id ");
              sql.append("  and IS_SCHEDULE_CR_EMERGENCY <> 1 )");
              sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
              sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
                  + "  FROM common_gnoc.unit ut "
                  + "    START WITH ut.unit_id IN "
                  + "    (SELECT excute_unit_id "
                  + "    FROM OPEN_PM.v_manage_cr_config "
                  + "    WHERE manage_unit = :manage_unit "
                  + "    AND scope_id      = :scope_id "
                  + "    AND DEVICE_TYPE  IS NOT NULL "
                  + "    ) "
                  + "    CONNECT BY PRIOR unit_id = parent_unit_id "
                  + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
                  + "    FROM OPEN_PM.v_manage_cr_config "
                  + "    WHERE manage_unit = :manage_unit  AND scope_id = :scope_id AND DEVICE_TYPE  IS NOT NULL )))");
              sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit)");
              sql.append(" and (cr.manage_unit_id = :manage_unit ");
              sql.append(" OR (cr.manage_unit_id is null))");
              params.put("manage_unit", userDept);
              params.put("scope_id", Long.valueOf(crDTO.getScopeId()));
            }
          }
          if (StringUtils.isNotNullOrEmpty(crDTO.getCrNumber())) {
            String[] arrCrNumber = crDTO.getCrNumber().split(",");
            if (arrCrNumber.length > 1) {
              List<String> lstCrNumber = Arrays.asList(arrCrNumber);
              sql.append(" and cr.cr_number in(:lst_cr) ");
              params.put("lst_cr", lstCrNumber);
            } else {
              sql.append(" and LOWER(cr.cr_number) like :cr_number  ESCAPE '\\' ");
              params.put("cr_number", StringUtils.convertLowerParamContains(crDTO.getCrNumber()));
            }
          }

          if (StringUtils.isNotNullOrEmpty(crDTO.getTitle())) {
            sql.append(" and lower(cr.title) like :cr_title  ESCAPE '\\' ");
            params.put("cr_title", StringUtils.convertLowerParamContains(crDTO.getTitle()));
          }
          if (crDTO.getEarliestStartTime() != null) {
            sql.append(" and cr.earliest_start_time >= :earliest_start_time ");
            //            paramList.add(dtu.convertStringToDateTime(crDTO.getEarliestStartTime().trim()));
            params.put("earliest_start_time", DateTimeUtils.convertDateToOffset(
                crDTO.getEarliestStartTime(), offset, true));
          }
          if (StringUtils.isNotNullOrEmpty(crDTO.getEarliestStartTimeTo())) {
            sql.append(" and cr.earliest_start_time <= :earliest_start_time_to ");
            //            paramList.add(dtu.convertStringToDateTime(crDTO.getEarliestStartTimeTo().trim()));
            params.put("earliest_start_time_to", DateTimeUtils.converStringClientToServerDate(
                crDTO.getEarliestStartTimeTo().trim(), offset));
          }
          if (crDTO.getLatestStartTime() != null) {
            sql.append(" and cr.latest_start_time >= :latest_start_time ");
            //            paramList.add(dtu.convertStringToDateTime(crDTO.getLatestStartTime().trim()));
            params.put("latest_start_time", DateTimeUtils.convertDateToOffset(
                crDTO.getLatestStartTime(), offset, true));
          }
          if (StringUtils.isNotNullOrEmpty(crDTO.getLatestStartTimeTo())) {
            sql.append(" and cr.latest_start_time <= :latest_start_time_to ");
            //            paramList.add(dtu.convertStringToDateTime(crDTO.getLatestStartTimeTo().trim()));
            params.put("latest_start_time_to", DateTimeUtils.converStringClientToServerDate(
                crDTO.getLatestStartTimeTo().trim(), offset));
          }
          if (StringUtils.isNotNullOrEmpty(crDTO.getIsOutOfDate())) {
            if (Constants.CR_OUTDATE_TYPE.OUTOFDATE.equalsIgnoreCase(crDTO.getIsOutOfDate())) {
              sql.append(" and cr.LATEST_START_TIME < sysdate ");
            } else if (Constants.CR_OUTDATE_TYPE.ONTIME.equalsIgnoreCase(crDTO.getIsOutOfDate())) {
              sql.append(" and cr.LATEST_START_TIME >= sysdate ");
            }
          }
          if (StringUtils.isNotNullOrEmpty(crDTO.getCrType())
              && !"-1".equals(crDTO.getCrType().trim())) {
            sql.append(" and cr.cr_type = :cr_type ");
            params.put("cr_type", Long.parseLong(crDTO.getCrType().trim()));
          }
          if (StringUtils.isNotNullOrEmpty(crDTO.getSubcategory())
              && !"-1".equals(crDTO.getSubcategory().trim())) {
            sql.append(" and cr.Subcategory = :sub_category ");
            params.put("sub_category", Long.parseLong(crDTO.getSubcategory().trim()));
          }
          if (StringUtils.isNotNullOrEmpty(crDTO.getRisk())
              && !"-1".equals(crDTO.getRisk().trim())) {
            sql.append(" and cr.risk = :risk ");
            params.put("risk", Long.parseLong(crDTO.getRisk().trim()));
          }
          if (StringUtils.isNotNullOrEmpty(crDTO.getImpactSegment())
              && !"-1".equals(crDTO.getImpactSegment().trim())) {
            sql.append(" and cr.impact_segment = :impact_segment ");
            params.put("impact_segment", Long.parseLong(crDTO.getImpactSegment().trim()));
          }

          if (StringUtils.isNotNullOrEmpty(crDTO.getCountry())
              && !"-1".equals(crDTO.getCountry().trim())) {
            if ("4001000000".equals(crDTO.getCountry()) || "5000289722".equals(crDTO.getCountry())
                || "1000014581".equals(crDTO.getCountry())
                || "3500289726".equals(crDTO.getCountry()) || "4500000001"
                .equals(crDTO.getCountry()) || "6000289723".equals(crDTO.getCountry())
                || "3000289724".equals(crDTO.getCountry()) || "2000289729"
                .equals(crDTO.getCountry()) || "1500289728".equals(crDTO.getCountry())) {
              sql.append(" and cr.country in( :country1, :country2 )");
              params.put("country1", Long.parseLong(crDTO.getCountry().trim()));
              if ("1500289728".equals(crDTO.getCountry())) {//timor
                params.put("country2", 289728L);
              } else if ("2000289729".equals(crDTO.getCountry())) {//timor
                params.put("country2", 289729L);
              } else if ("3000289724".equals(crDTO.getCountry())) {//mozam
                params.put("country2", 289724L);
              } else if ("6000289723".equals(crDTO.getCountry())) {//timor
                params.put("country2", 289723L);
              } else if ("4500000001".equals(crDTO.getCountry())) {//myanmar
                params.put("country2", 300656L);
              } else if ("4001000000".equals(crDTO.getCountry())) {//tanz
                params.put("country2", 289727L);
              } else if ("5000289722".equals(crDTO.getCountry())) {//lao
                params.put("country2", 289722L);
              } else if ("1000014581".equals(crDTO.getCountry())) {//cam
                params.put("country2", 289721L);
              } else if ("3500289726".equals(crDTO.getCountry())) {//burundi
                params.put("country2", 289726L);
              }
            } else {
              sql.append(" and cr.country = :country ");
              params.put("country", Long.parseLong(crDTO.getCountry().trim()));
            }
          }
          if (StringUtils.isNotNullOrEmpty(crDTO.getChangeOrginator())
              && !"-1".equals(crDTO.getChangeOrginator().trim())) {
            sql.append(" and cr.Change_Orginator = :change_orginator ");
            params.put("change_orginator", Long.parseLong(crDTO.getChangeOrginator().trim()));
          }
          if (StringUtils.isNotNullOrEmpty(crDTO.getChangeResponsible())
              && !"-1".equals(crDTO.getChangeResponsible().trim())) {
            sql.append(" and cr.Change_Responsible = :change_responsible ");
            params.put("change_responsible", Long.parseLong(crDTO.getChangeResponsible().trim()));
          }
          if (StringUtils.isNotNullOrEmpty(crDTO.getChangeOrginatorUnit())
              && !"-1".equals(crDTO.getChangeOrginatorUnit().trim())) {
            if (crDTO.getSubDeptOri() != null && "1".equals(crDTO.getSubDeptOri().trim())) {
              sql.append(" and cr.Change_Orginator_Unit in ( ");
              sql.append(" SELECT unit_id");
              sql.append(" FROM common_gnoc.unit ");
              sql.append(" START WITH  unit_id = :change_unit ");
              sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
              sql.append(" )");
              params.put("change_unit", Long.parseLong(crDTO.getChangeOrginatorUnit().trim()));
            } else {
              sql.append(" and cr.Change_Orginator_Unit = :change_unit ");
              params.put("change_unit", Long.parseLong(crDTO.getChangeOrginatorUnit().trim()));
            }
          }
          if (StringUtils.isNotNullOrEmpty(crDTO.getChangeResponsibleUnit())
              && !"-1".equals(crDTO.getChangeResponsibleUnit().trim())) {
            if (crDTO.getSubDeptResp() != null && "1".equals(crDTO.getSubDeptResp().trim())) {
              sql.append(" and cr.Change_Responsible_Unit in ( ");
              sql.append(" SELECT unit_id");
              sql.append(" FROM common_gnoc.unit ");
              sql.append(" START WITH  unit_id = :change_res_unit ");
              sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
              sql.append(" )");
              params
                  .put("change_res_unit", Long.parseLong(crDTO.getChangeResponsibleUnit().trim()));
            } else {
              sql.append(" and cr.Change_Responsible_Unit = :change_res_unit ");
              params
                  .put("change_res_unit", Long.parseLong(crDTO.getChangeResponsibleUnit().trim()));
            }
          }

          if (searchCrIds != null && searchCrIds.size() > 0) {
            sql.append(" and (  cr.cr_id in (:searchCrIds0) ");
            for (int i = 1; i < searchCrIds.size(); i++) {
              sql.append(" or cr.cr_id in (:searchCrIds").append(i).append(") ");
            }
            sql.append(" ) ");
          }

          if (searchCrIds != null && searchCrIds.size() > 0) {
            for (int i = 0; i < searchCrIds.size(); i++) {
              params.put("searchCrIds" + i, searchCrIds.get(i));
            }
          }

          list = getNamedParameterJdbcTemplate()
              .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(CrInsiteDTO.class));
          return list;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      if (searchCrIds != null) {
        searchCrIds.clear();
      }
      if (allIps != null) {
        allIps.clear();
      }
    }
    return null;
  }

  @Override
  public List<CrInsiteDTO> getListCRForExport(CrInsiteDTO crDTO, String lstCrId,
      Date earliestCrCreatedTime, Date earliestCrStartTime, Date lastestCrStartTime,
      Date latestCrUpdateTime, String locale) {
    try {

      if (lastestCrStartTime == null) {
        lastestCrStartTime = new Date();
      }

      if (latestCrUpdateTime == null || latestCrUpdateTime.compareTo(lastestCrStartTime) < 0) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastestCrStartTime);
        cal.add(Calendar.DATE, 3);
        latestCrUpdateTime = cal.getTime();
      }

      if (crDTO != null) {
        String userIdStr = crDTO.getUserLogin();
        String userIdDeptStr = crDTO.getUserLoginUnit();//R_xxx uyquyen
        Long userId = null;
        Long userDept = null;
        if (userIdStr != null) {
          userId = Long.valueOf(userIdStr);
        }
        if (userIdDeptStr != null) {
          userDept = Long.valueOf(userIdDeptStr);
        }
        if (userId != null && userDept != null && crDTO.getSearchType() != null) {
          boolean isManager = userRepository.isManagerOfUnits(userId);
          List<CrInsiteDTO> list;
          StringBuffer sql = new StringBuffer();
          String language = I18n.getLocale();
          if (!StringUtils.isStringNullOrEmpty(locale)) {
            language = locale;
          }
          sql.append("select cr.title title, cr.cr_id crId,  "
              + " cr.cr_number crNumber, "
              + " (cr.update_time + :offset * interval '1' hour) updateTime, "
              + " (cr.earliest_start_time  + :offset * interval '1' hour) earliestStartTime, "
              + " (cr.latest_start_time + :offset * interval '1' hour) latestStartTime, "
              + " (cr.created_date + :offset * interval '1' hour) createdDate, "
              /*longlt6 thay doi ngay 2017-05-20 fix bug hien thi thoi gian theo locate - START */
              + " (cr.DISTURBANCE_START_TIME + :offset  * interval '1' hour) disturbanceStartTime,  "
              + " (cr.DISTURBANCE_END_TIME + :offset  * interval '1' hour) disturbanceEndTime, "
              + " ((select max(chs.change_date) from open_pm.cr_his chs where cr.cr_id = chs.cr_id  "
              + " and chs.change_date >= cr.created_date and chs.action_code = 24) "
              + " + :offset * interval '1' hour) sentDate, "
              /*longlt6 thay doi ngay 2017-05-20 fix bug hien thi thoi gian theo locate -END  */
              + " cr.priority priority, "
              + " case when utOri.unit_code is null then '' "
              + " else TO_CHAR(utOri.unit_code || ' ('||utOri.unit_name||')') end as changeOrginatorUnitName, "
              + " case when usOri.username is null then '' "
              + " else TO_CHAR(usOri.username || ' ('||usOri.fullname||')') end as changeOrginatorName, "
              + " case when utResp.unit_code is null then '' "
              + " else TO_CHAR(utResp.unit_code || ' ('||utResp.unit_name||')') end as changeResponsibleUnitName, "
              + " case when usResp.username is null then '' "
              + " else TO_CHAR(usResp.username || ' ('||usResp.fullname||')') end as changeResponsibleName, "
              + " case when utConsi.unit_code is null then '' "
              + " else TO_CHAR(utConsi.unit_code || ' ('||utConsi.unit_name||')') end as considerUnitName, "
              + " case when usConsi.username is null then '' "
              + " else TO_CHAR(usConsi.username || ' ('||usConsi.fullname||')') end as considerUserName, "
              + " cr.state, "
              + " cr.cr_type_cat crTypeCat, "
              + " cr.relate_to_pre_approved_cr relateToPreApprovedCr, "
              + " (case cr.cr_type  "
              + " when 0 then 'CR Normal' "
              + " when 1 then 'CR Emergency' "
              + " when 2 then 'CR Standard' "
              + " else TO_CHAR(cr.cr_type) end) crType, "
              + " cr.change_responsible as changeResponsible, "
              + " cr.change_responsible_unit as changeResponsibleUnit, "
              + " cr.change_orginator as changeOrginator, "
              + " cr.change_orginator_unit as changeOrginatorUnit, "
              + " cr.consider_unit_id as considerUnitId, "
              + " cr.consider_user_id as considerUserId, "
              + " cr.process_type_id as crProcessId, "
              + " (case cr.process_type_id ||'@'||cr.cr_type "
              + " when '0@" + Constants.CR_TYPE.STANDARD + "' then '" + I18n
              .getChangeManagement("cr.specialProcessStandard") + "' "
              + " when '0@" + Constants.CR_TYPE.NORMAL + "' then '" + I18n
              .getChangeManagement("cr.specialProcess") + "' "
              + " when '0@" + Constants.CR_TYPE.EMERGENCY + "' then '" + I18n
              .getChangeManagement("cr.specialProcess") + "' "
              + " when '0@' then '" + I18n.getChangeManagement("cr.specialProcess") + "' "
              + " else TO_CHAR(replace(replace(cps.cr_process_name,CHR(10),'_'), ',', '_')) end) crProcessName, "
              + " sy.subcategory_id subcategoryId, "
              + " sy.sy_name subcategory, "
              + " dts.device_type_id deviceTypeId, "
              + " REPLACE(dts.device_type_name, '/',', ') deviceType, "
              + " ist.impact_segment_id impactSegmentId, "
              + " ist.impact_segment_name impactSegment, "
              + " cr.risk risk, "
              + " (case when service_affecting = 1 then '" + I18n.getChangeManagement("common.yes")
              + "' "
              + " else '" + I18n.getChangeManagement("common.no") + "' "
              + " end) affectedServiceList, "
              + " cr.description description, "
              + " (select us.username || ' - ' || TO_CHAR(wlg.created_date, 'dd/MM/yyyy HH24:mi:ss') || ' - ' || TO_CLOB(wlg.wlg_text)  "
              + " from open_pm.work_log wlg "
              + " left join open_pm.user_group_category ugcy on wlg.user_group_action = ugcy.ugcy_id "
              + " left join common_gnoc.users us on us.user_id = wlg.user_id "
              + " where wlg.wlg_object_type=2 and ugcy_code = 'CR_CAB' "
              + " and wlg.wlg_object_id = cr.cr_id "
              + " and rownum<2 "
              + " ) commentCAB , "
              + " (select wlg_text from open_pm.v_worklog_content vwct "
              + " where ugcy_code = 'CR_QLTD' "
              + " and vwct.wlg_object_id = cr.cr_id "
              + " and rownum < 2 "
              + " ) commentQLTD , "
              + " (select wlg_text from open_pm.v_worklog_content vwct "
              + " where ugcy_code = 'CR_Z78' "
              + " and vwct.wlg_object_id = cr.cr_id "
              + " and rownum < 2 "
              + " ) commentZ78, "
              + " (select wlg_text from open_pm.v_worklog_content vwct "
              + " where ugcy_code = 'CR_CREATOR' "
              + " and vwct.wlg_object_id = cr.cr_id "
              + " and rownum < 2 "
              + " ) commentCreator, "
              + " '(' || (case "
              + " ccfoss.system_id "
              + " when 1 then 'MR' "
              + " when 2 then 'PT' "
              + " when 3 then 'TT' "
              + " when 4 then 'WO' "
              + " when 5 then 'SR' "
              + " when 6 then 'RDM' "
              + " when 7 then 'RR' "
              + " else '' end) || ') ' || (case "
              + " ccfoss.system_id "
              + " when 1 then mr.mr_code "
              + " when 2 then TO_NCHAR(ps.problem_code) "
              + " when 3 then TO_NCHAR(ts.trouble_code) "
              + " when 4 then TO_NCHAR(wo.wo_code) "
              + " else TO_NCHAR('') end)  "
              + " as relateCr, "
              + " case "
              //                            + " cps.impact_type "
              + " cr.duty_type "
              + " when 2 then '" + I18n.getChangeManagement("common.day") + "' "
              + " else '" + I18n.getChangeManagement("common.night") + "' "
              + " end dutyType, "
              + " case "
              + " cps.impact_characteristic "
              + " when 1 then 'Logic' "
              + " else '" + I18n.getChangeManagement("cr.physic") + "' "
              + " end relatedTt, "
                            /*longlt6 thay doi ngay 2017-05-20 fix bug hien thi thoi gian theo locate - START
                             + " TO_CHAR( "
                             + " ((select max(chs.change_date) from open_pm.cr_his chs where cr.cr_id = chs.cr_id  "
                             + " and chs.change_date >= cr.created_date and chs.action_code = 24) "
                             + " + 0 * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss') sentDate, "
                             longlt6 thay doi ngay 2017-05-20 fix bug hien thi thoi gian theo locate - START */
              //20160622 daitr1 bo sung cot thoi gian qua han
              + " case cr.state "
              + " when :state_close "
              + " then "
              + " case"
              + " when exists (select 1 from "
              + " open_pm.cr_his chs1 "
              + " where cr.cr_id = chs1.cr_id and chs1.action_code in (:action_reject,:act_close,:act_close_by_appr,:act_close_by_man,:act_close_by_emer)) then '' "
              + " else   TO_CHAR(((select max(chs2.change_date) from open_pm.cr_his chs2 where cr.cr_id = chs2.cr_id"
              + " and chs2.change_date >= cr.created_date "
              + " and chs2.action_code in (:act_close_cr,:act_close_cr_appr,:act_close_excu))"
              + " + :offset * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss') end"
              + " when :state_draft then '' "
              + " else to_char(sysdate + :offset * interval '1' hour, 'dd/MM/yyyy HH24:mi:ss')"
              + " end as compareDate,"
              + " vcawlct.worklogcontent as allComment,"
              + " cr.is_primary_cr isPrimaryCr,"
              + " cr.relate_to_primary_cr relateToPrimaryCrNumber,"
              + " cr.relate_to_pre_approved_cr relateToPreApprovedCrNumber,"
              //20160622 daitr1 bo sung cot thoi gian qua han
              + " case when usCab.username is null then ''"
              + " else TO_CHAR(usCab.username || ' ('||usCab.fullname||')') end as userCab"
              //LONGLT6 add 2017-08-22 start
              + " , history.RETURN_TITLE as resolveTitle, history.RETURN_CODE as resolveCodeId "
              + " , history1.RETURN_TITLE as closeTitle,  history1.RETURN_CODE as closeCodeId "
              //LONGLT6 add 2017-08-22 end
              //                            + " , affectService.SERVICE_NAME as affectedService "
              + " , cr.CIRCLE_ADDITIONAL_INFO as circleAdditionalInfo "
              + " , cr.TOTAL_AFFECTED_CUSTOMERS as totalAffectedCustomers "
              + " , cr.TOTAL_AFFECTED_MINUTES as totalAffectedMinutes "
              + " , cr.RANK_GATE rankGate "
              + " , cr.IS_RUN_TYPE isRunType "
              + " from open_pm.cr cr "
              + " left join common_gnoc.unit utOri on cr.change_orginator_unit = utOri.unit_id "
              + " left join common_gnoc.users usOri on cr.change_orginator = usOri.user_id "
              + " left join common_gnoc.unit utResp on cr.change_responsible_unit = utResp.unit_id "
              + " left join common_gnoc.users usResp on cr.change_responsible = usResp.user_id "
              + " left join common_gnoc.unit utConsi on cr.consider_unit_id = utConsi.unit_id "
              + " left join common_gnoc.users usConsi on cr.consider_user_id = usConsi.user_id "
              + " left join open_pm.cr_process cps on cr.process_type_id = cps.cr_process_id "
              + " left join open_pm.subcategory sy on cr.subcategory = sy.subcategory_id "
              + " left join open_pm.device_types dts on cr.device_type = dts.device_type_id "
              + " left join open_pm.impact_segment ist on cr.impact_segment = ist.impact_segment_id "
              + " left join open_pm.cr_created_from_other_sys ccfoss on (ccfoss.cr_id = cr.cr_id and ccfoss.is_active  = 1) "
              + " left join one_tm.problems ps on ps.problem_id = ccfoss.object_id "
              + " left join one_tm.troubles ts on ts.trouble_id = ccfoss.object_id "
              + " left join open_pm.mr mr on mr.mr_id = ccfoss.object_id "
              + " left join wfm.wo wo on wo.wo_id = ccfoss.object_id "
              + " left join common_gnoc.users usCab on cr.user_cab = usCab.user_id"
              //LONGLT6 add 2017-08-22 start
              + "  LEFT JOIN ( "
              + "  select CR_ID , catalog.RETURN_TITLE  , his.RETURN_CODE from open_pm.CR_HIS his "
              + "  left join open_pm.RETURN_CODE_CATALOG catalog on his.RETURN_CODE = catalog.RCCG_ID "
              + "  where his.STATUS = 7 "
              + (StringUtils.isStringNullOrEmpty(crDTO.getEarliestStartTime()) ? ""
              : "  and his.CHANGE_DATE >= :earliestStartTime ")
              + " ) history on cr.CR_ID = history.CR_ID "
              //LONGLT6 add 2017-08-22 end

              + "  LEFT JOIN ( "
              + "  select CR_ID , catalog.RETURN_TITLE , his1.RETURN_CODE from open_pm.CR_HIS his1 "
              + "  left join open_pm.RETURN_CODE_CATALOG catalog on his1.RETURN_CODE = catalog.RCCG_ID "
              + "  where his1.STATUS = 9 "
              + (StringUtils.isStringNullOrEmpty(crDTO.getEarliestStartTime()) ? ""
              : "  and his1.CHANGE_DATE >= :earliestStartTime ")
              + " ) history1 on cr.CR_ID = history1.CR_ID"
              //                            + " LEFT JOIN ("
              //                            + " select tbl.CR_ID, rtrim(xmlagg(XMLELEMENT(e,tbl.SERVICE_NAME,',').EXTRACT('//text()')).GetClobVal(),',') as SERVICE_NAME "
              //                            + " from ( "
              //                            + " select casd.CR_ID, '['||to_char(casd.INSERT_TIME,'dd/MM/yyyy HH24:mi:ss')||'] - '||ass.SERVICE_NAME as SERVICE_NAME  "
              //                            + " from OPEN_PM.AFFECTED_SERVICES ass  inner join OPEN_PM.CR_AFFECTED_SERVICE_DETAILS casd "
              //                            + " on casd.AFFECTED_SERVICE_ID = ass.AFFECTED_SERVICE_ID "
              //                            + " where 1=1 "
              //                            + (earliestCrCreatedTime == null ? "" : "  AND casd.INSERT_TIME >= TO_DATE( '" + dateFormat.format(earliestCrCreatedTime) + "' , 'DD/MM/YYYY HH24:MI:SS' ) ")
              //                            + (latestCrUpdateTime == null ? " " : "  AND casd.INSERT_TIME <=   TO_DATE( '" + dateFormat.format(latestCrUpdateTime) + "' , 'DD/MM/YYYY HH24:MI:SS' )    ")
              //                            + " ) tbl "
              //                            + " group by tbl.cr_id "
              //                            + " ) affectService on cr.CR_ID = affectService.CR_ID "
              //+ " left join v_cr_all_work_log_content vcawlct on vcawlct.cr_id = cr.cr_id "
              + " LEFT JOIN ( "
              + "     select cr_id,rtrim(xmlagg(XMLELEMENT(e,wlgCont,',').EXTRACT('//text()') ORDER BY created_date desc ).GetClobVal(),',') as worklogcontent "
              + "     from ( "
              + "       select cr_id, TO_CLOB(CONCAT(TO_CLOB(CONCAT('[' || created_date_String || '] - ' || WLAY_NAME,' - [' || username || '][' || ugcy_name)),'] ' || state || ': ' || TO_CLOB(wlg_text))) as wlgCont,created_date "
              + "       from (            "
              + "             select ugcy.ugcy_code,ugcy.ugcy_name,wlg.wlg_object_id as cr_id,wlg.wlg_text,wlg.created_date, "
              + "             to_char(created_date,'dd/MM/yyyy HH24:mi:ss') as created_date_String,wlg.WLG_OBJECT_STATE,us.username, "
              + "             case WLG_OBJECT_STATE "
              + "             when 0 then '- [" + I18n.getLanguage("cr.state.0") + "]' "
              + "             when 1 then '- [" + I18n.getLanguage("cr.state.1") + "]' "
              + "             when 2 then '- [" + I18n.getLanguage("cr.state.2") + "]' "
              + "             when 3 then '- [" + I18n.getLanguage("cr.state.3") + "]' "
              + "             when 4 then '- [" + I18n.getLanguage("cr.state.4") + "]' "
              + "             when 5 then '- [" + I18n.getLanguage("cr.state.5") + "]' "
              + "             when 6 then '- [" + I18n.getLanguage("cr.state.6") + "]' "
              + "             when 7 then '- [" + I18n.getLanguage("cr.state.7") + "]' "
              + "             when 8 then '- [" + I18n.getLanguage("cr.state.8") + "]' "
              + "             when 9 then '- [" + I18n.getLanguage("cr.state.9") + "]' "
              + "             when 10 then '- [" + I18n.getLanguage("cr.state.10") + "]' "
              + "             else '' end as state, "
              + "             wlc.WLAY_NAME "
              + "             from open_pm.work_log wlg "
              + "             left join open_pm.user_group_category ugcy on wlg.user_group_action = ugcy.ugcy_id "
              + "             left join open_pm.WORK_LOG_CATEGORY wlc on wlg.WLAY_ID = wlc.WLAY_ID "
              + "             left join COMMON_GNOC.users us on us.USER_ID = wlg.USER_ID "
              + "             where wlg.wlg_object_type=2  "
              + (earliestCrCreatedTime == null ? " "
              : "  and wlg.CREATED_DATE >=  TO_DATE( '" + DateTimeUtils
                  .date2ddMMyyyyHHMMss(earliestCrCreatedTime) + "' , 'DD/MM/YYYY HH24:MI:SS' )    ")
              + (latestCrUpdateTime == null ? " "
              : "  and wlg.CREATED_DATE <=   TO_DATE( '" + DateTimeUtils
                  .date2ddMMyyyyHHMMss(latestCrUpdateTime) + "' , 'DD/MM/YYYY HH24:MI:SS' )    ")
              + "           order by wlg.WORK_LOG_ID desc ) "
              + "        ) "
              + "        group by cr_id "
              + " ) vcawlct  on  cr.CR_ID = vcawlct.CR_ID "
              + " where usOri.is_enable = 1");

          BaseDto searchForm = genConditionSearchCrV2(crDTO, userId, userDept, isManager,
              earliestCrCreatedTime, earliestCrStartTime, lastestCrStartTime, latestCrUpdateTime);
          sql.append(searchForm.getSqlQuery());
          sql.append("  and cr.cr_id in(").append(lstCrId).append("-1)");
          sql.append(" order by  cr.relate_to_primary_cr, cr.update_time desc ");

          list = getNamedParameterJdbcTemplate().query(sql.toString(), searchForm.getParameters(),
              BeanPropertyRowMapper.newInstance(CrInsiteDTO.class));
          list = crDBRepository.processListToGenGr(list, crDTO, language);
          //tuanpv14_multi languges

          try {
            Map<String, Object> map = getSqlLanguageExchange(
                Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR,
                Constants.APPLIED_BUSSINESS.CR_PROCESS, language);
            String sqlLanguage = (String) map.get("sql");
            Map mapParam = (Map) map.get("mapParam");
            List<LanguageExchangeDTO> lstLanguage = findBySql(sqlLanguage, mapParam);
            list = setLanguage(list, lstLanguage, "crProcessId", "crProcessName");
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
          try {
            Map<String, Object> map = getSqlLanguageExchange(
                Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR,
                Constants.APPLIED_BUSSINESS.DEVICE_TYPES, language);
            String sqlLanguage = (String) map.get("sql");
            Map mapParam = (Map) map.get("mapParam");
            List<LanguageExchangeDTO> lstLanguage = findBySql(sqlLanguage, mapParam);
            list = setLanguage(list, lstLanguage, "crProcessId", "crProcessName");
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
          try {
            Map<String, Object> map = getSqlLanguageExchange(
                Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR,
                Constants.APPLIED_BUSSINESS.IMPACT_SEGMENT, language);
            String sqlLanguage = (String) map.get("sql");
            Map mapParam = (Map) map.get("mapParam");
            List<LanguageExchangeDTO> lstLanguage = findBySql(sqlLanguage, mapParam);
            list = setLanguage(list, lstLanguage, "impactSegmentId", "impactSegment");
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
          try {
            Map<String, Object> map = getSqlLanguageExchange(
                Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR,
                Constants.APPLIED_BUSSINESS.SUBCATEGORY, language);
            String sqlLanguage = (String) map.get("sql");
            Map mapParam = (Map) map.get("mapParam");
            List<LanguageExchangeDTO> lstLanguage = findBySql(sqlLanguage, mapParam);
            list = setLanguage(list, lstLanguage, "subcategoryId", "subcategory");
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }

          try {
            Map<String, Object> map = getSqlLanguageExchange(
                Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR,
                Constants.APPLIED_BUSSINESS.RETURN_CODE_CATALOG, language);
            String sqlLanguage = (String) map.get("sql");
            Map mapParam = (Map) map.get("mapParam");
            List<LanguageExchangeDTO> lstLanguage = findBySql(sqlLanguage, mapParam);

            try {
              list = setLanguage(list, lstLanguage, "resolveCodeId", "resolveTitle");
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
            try {
              list = setLanguage(list, lstLanguage, "closeCodeId", "closeTitle");
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }

          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
          //tuanpv14_multi languges
          return list;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<Long> getlistCrIdsByNodeInfo(Date startDate, Date endDate, List<Long> ipIds) {
    try {
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-list-cr-id-by-node");
      Map<String, Object> params = new HashMap<>();
      params.put("startDate", startDate);
      params.put("endDate", endDate);
      params.put("ipIds", ipIds);
      List<Long> results = getNamedParameterJdbcTemplate().queryForList(sql, params, Long.class);
      return results;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  private BaseDto genConditionSearchCrV2(CrInsiteDTO crDTO, Long userId, Long userDept,
      boolean isManager, Date earliestCrCreatedTime, Date earliestCrStartTime,
      Date lastestCrStartTime, Date latestCrUpdateTime) throws Exception {
    StringBuffer sql = new StringBuffer("");
    Map<String, Object> params = new HashMap<>();
    Double offset = TimezoneContextHolder.getOffsetDouble();
    params.put("offset", offset);

    params.put("state_close", Constants.CR_STATE.CLOSE);
    params.put("action_reject", Constants.CR_ACTION_CODE.REJECT);
    params.put("act_close", Constants.CR_ACTION_CODE.CLOSE);
    params.put("act_close_by_appr", Constants.CR_ACTION_CODE.CLOSE_BY_APPRAISER);
    params.put("act_close_by_man", Constants.CR_ACTION_CODE.CLOSE_BY_MANAGER_SCH);
    params.put("act_close_by_emer", Constants.CR_ACTION_CODE.CLOSE_EXCUTE_EMERGENCY);
    params.put("act_close_cr", Constants.CR_ACTION_CODE.CLOSECR);
    params.put("act_close_cr_appr", Constants.CR_ACTION_CODE.CLOSECR_APPROVE_STD);
    params.put("act_close_excu", Constants.CR_ACTION_CODE.CLOSE_EXCUTE_STD);
    params.put("state_draft", Constants.CR_STATE.DRAFT);

    if (crDTO.getEarliestStartTime() != null) {
      Date dateStr = DateTimeUtils.convertDateToOffset(crDTO.getEarliestStartTime(), offset, true);
      params.put("earliestStartTime", dateStr);
    }

//        if (earliestCrStartTime != null) {
//            paramList.add(sdf.format(earliestCrStartTime));
//        }
//        if (latestCrUpdateTime != null) {
//            paramList.add(sdf.format(latestCrUpdateTime));
//        }
    if (Constants.CR_SEARCH_TYPE.CREATE_EDIT.toString().equals(crDTO.getSearchType())) {
      sql.append(" and ( ( ");
      // <editor-fold desc="Cho phep nguoi tao CR co the sua CR khi CR chua phe duyet hoac INCOMPLETE">
      sql.append(" cr.state in (:state_open, :state_incom, :state_draft) ");
      params.put("state_open", Constants.CR_STATE.OPEN);
      params.put("state_incom", Constants.CR_STATE.INCOMPLETE);

      sql.append(" and cr.change_orginator_unit = :org_unit ");
      params.put("org_unit", userDept);
      sql.append(" and cr.change_orginator = :change_orginator ");
      params.put("change_orginator", userId);
      // </editor-fold>

      sql.append(" ) or (");
      // <editor-fold desc="Cho phep tat ca nhan vien trong don vi them WLog khi CR RESOLVE">
      sql.append(" cr.state = :state_resolve ");
      params.put("state_resolve", Constants.CR_STATE.RESOLVE);
      sql.append(" and cr.change_orginator_unit in ( ");
      sql.append(" SELECT unit_id");
      sql.append(" FROM common_gnoc.unit ");
      sql.append(" START WITH  unit_id = :unit_id ");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" )");
      params.put("unit_id", userDept);
      // </editor-fold>
      sql.append(" ) )");
    }
//        if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.LOOKUP.toString()) &&
//                crDTO.getState() != null && !crDTO.getState().trim().equals("")) {
//                sql.append(" and cr.state = ? ");
//                paramList.add(crDTO.getState());
//        }

    if (Constants.CR_SEARCH_TYPE.LOOKUP.toString().equals(crDTO.getSearchType())) {
      if (StringUtils.isNotNullOrEmpty(crDTO.getState())) {
        String[] arrState = crDTO.getState().split(",");
        List<String> lstState = Arrays.asList(arrState);
        if (lstState == null) {
          lstState = new ArrayList<>();
        }
        List<String> lstStateAll = new ArrayList<>();
        lstStateAll.addAll(lstState);
        lstStateAll.add("-1");
        sql.append(" and cr.state in (:lst_state) ");
        params.put("lst_state", lstStateAll);
      } else {
        sql.append(" and cr.state in (-1) ");
      }
    }

    if (Constants.CR_SEARCH_TYPE.APPROVE.toString().equals(crDTO.getSearchType())) {
      sql.append(" and cr.state = :state_open ");
      params.put("state_open", Constants.CR_STATE.OPEN);
      sql.append(
          " and exists (select 1 from common_gnoc.v_user_role where user_id = :user_id and role_code = :role_code )");
      params.put("user_id", userId);
      params.put("role_code", Constants.CR_ROLE.roleTP);
//                        if(crDTO.getState().equals(Constants.CR_STATE.OPEN.toString())){
      if (crDTO.getIsSearchChildDeptToApprove() != null
          && "1".equals(crDTO.getIsSearchChildDeptToApprove().trim())) {
        sql.append(" and cr.cr_id in (");
        sql.append(" select cr_id from ");
        sql.append(" cr_approval_department d ");
        sql.append(" where ");
        sql.append(" d.cr_id = cr.cr_id and d.unit_id = :unit_id ");
        sql.append(" and d.status = 0 )");
        params.put("unit_id", userDept);
      } else {
        sql.append(" and cr.cr_id in (");
        sql.append(" with tbl as (");
        sql.append(" select d.unit_id,d.cr_id,d.cadt_level,");
        sql.append(" (select min(b.cadt_level) from cr_approval_department b");
        sql.append(" where  b.cr_id = d.cr_id and b.status = 0) as num");
        sql.append(" from cr_approval_department d");
        sql.append(" where d.unit_id = :unit_id   and d.status = 0)");
        sql.append(" select tb.cr_id   from tbl tb where tb.cadt_level <= tb.num");
        sql.append(" ) ");
        params.put("unit_id", userDept);
      }
//            sql.append(" and ( (cr.is_primary_cr is null) ");
//            sql.append(" OR (cr.is_primary_cr is not null ");
//            sql.append(" and exists (select 1 from cr cr1 where cr1.relate_to_primary_cr = cr.cr_id))");
//            sql.append(" )");
//                        }else{
//                            sql.append(" and cr.cr_id in (select d.cr_id from cr_approval_department d ");
//                            sql.append(" where unit_id =  ? )");
//                            paramList.add(userDept);
//                        }
    }
    if (Constants.CR_SEARCH_TYPE.VERIFY.toString().equals(crDTO.getSearchType())) {
      sql.append(" and cr.state = :state_queue ");
      params.put("state_queue", Constants.CR_STATE.QUEUE);
      sql.append(" and (cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit = :manage_unit ");
      sql.append(" and scope_id = :scope_id ");
      sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 ");
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
          + "  FROM common_gnoc.unit ut "
          + "    START WITH ut.unit_id IN "
          + "    (SELECT excute_unit_id "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = :manage_unit "
          + "    AND scope_id      = :scope_id "
          + "    and IS_SCHEDULE_CR_EMERGENCY <> 1 AND DEVICE_TYPE  IS NOT NULL "
          + "    ) "
          + "    CONNECT BY PRIOR unit_id = parent_unit_id "
          + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = :manage_unit  AND scope_id = :scope_id AND DEVICE_TYPE  IS NOT NULL)))");
      sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit)");
      params.put("manage_unit", userDept);
      params.put("scope_id", Long.valueOf(crDTO.getScopeId()));

    }
    if (Constants.CR_SEARCH_TYPE.WAIT_CAB.toString().equals(crDTO.getSearchType())) {

      sql.append(" and cr.state in( :state_wait_cab,:state_cab) ");
      params.put("state_wait_cab", Constants.CR_STATE.WAIT_CAB);
      params.put("state_cab", Constants.CR_STATE.CAB);
      sql.append(" and ( cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit = :manage_unit ");
      sql.append(" and scope_id = :scope_id ");
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
          + "  FROM common_gnoc.unit ut "
          + "    START WITH ut.unit_id IN "
          + "    (SELECT excute_unit_id "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = :manage_unit "
          + "    AND scope_id      = :scope_id "
          + "    and IS_SCHEDULE_CR_EMERGENCY <> 1 AND DEVICE_TYPE  IS NOT NULL "
          + "    ) "
          + "    CONNECT BY PRIOR unit_id = parent_unit_id "
          + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = :manage_unit  AND scope_id = :scope_id AND DEVICE_TYPE  IS NOT NULL )))");

      params.put("manage_unit", userDept);
      params.put("scope_id", Long.valueOf(crDTO.getScopeId()));
    }

    if (Constants.CR_SEARCH_TYPE.CAB.toString().equals(crDTO.getSearchType())) {

      sql.append(" and (");
      sql.append("( cr.state = :state_cab ");
      params.put("state_cab", Constants.CR_STATE.CAB);
      sql.append(" and cr.user_cab =:user_cab) ");
      params.put("user_cab", userId);

      //cr khan cho phep user duoc cau hinh co the cab khi sap lich
      sql.append(" OR (cr.cr_type = :cr_type_emer ");
      params.put("cr_type_emer", Constants.CR_TYPE.EMERGENCY);
      sql.append(" and cr.state = :state_eval ");
      params.put("state_eval", Constants.CR_STATE.EVALUATE);

      sql.append(" and cr.change_responsible_unit in( ");
      sql.append(" select cab.execute_unit_id from open_pm.cr_cab_users cab");
      sql.append(" where cab.user_id= :user_id)");

      sql.append(" and cr.impact_segment in ( ");
      sql.append(" select cab.impact_segment_id from open_pm.cr_cab_users cab");
      sql.append(" where cab.user_id= :user_id)");
      params.put("user_id", userId);
      sql.append(" )  ");
      //cr khan cho phep user duoc cau hinh co the cab khi sap lich

      if (crDTO.getScopeId() != null) {
        sql.append(" OR (  cr.state in (:state_approve, :state_accept,:state_resolve) ");
        params.put("state_approve", Constants.CR_STATE.APPROVE);
        params.put("state_accept", Constants.CR_STATE.ACCEPT);
        params.put("state_resolve", Constants.CR_STATE.RESOLVE);
        sql.append(" and cr.cr_type = :cr_type_emer ");
        params.put("cr_type_emer", Constants.CR_TYPE.EMERGENCY);
        sql.append(" and cr.change_responsible_unit in ");
        sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
        sql.append(" start with ut.unit_id in ");
        sql.append(" ( ");
        sql.append(" select unit_id ");
        sql.append(" from cr_manager_units_of_scope cmnose ");
        sql.append(" where cmnose.cmse_id = :scope_id");
        params.put("scope_id", Long.valueOf(crDTO.getScopeId()));
        sql.append(" )");
        sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
        sql.append(" ) ) ");
      }
      sql.append(" )");

    }
    if (Constants.CR_SEARCH_TYPE.QLTD.toString().equals(crDTO.getSearchType())) {
      sql.append(
          " and cr.state in(:state_wait_cab,:state_cab,:state_queue,:state_coor,:state_eval) ");
      params.put("state_wait_cab", Constants.CR_STATE.WAIT_CAB);
      params.put("state_cab", Constants.CR_STATE.CAB);
      params.put("state_queue", Constants.CR_STATE.QUEUE);
      params.put("state_coor", Constants.CR_STATE.COORDINATE);
      params.put("state_eval", Constants.CR_STATE.EVALUATE);
      sql.append(" and cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit = :manage_unit");
      sql.append(" and scope_id = :scope_id ");
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" )");
      sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit )");
      params.put("manage_unit", userDept);
      params.put("scope_id", Long.valueOf(crDTO.getScopeId()));

    }

    if (CR_SEARCH_TYPE.QLTD_RENEW.toString().equals(crDTO.getSearchType())) {
      sql.append(
          " and cr.state in(:state_accept) ");
      params.put("state_accept", CR_STATE.ACCEPT);
      sql.append(" and cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit = :manage_unit");
      sql.append(" and scope_id = :scope_id ");
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" )");
      sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit )");
      params.put("manage_unit", userDept);
      params.put("scope_id", Long.valueOf(crDTO.getScopeId()));

    }

    if (Constants.CR_SEARCH_TYPE.Z78.toString().equals(crDTO.getSearchType())) {
      sql.append(" and cr.state = :state_accept ");
      params.put("state_accept", Constants.CR_STATE.ACCEPT);
      sql.append(" and cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" ( ");
      sql.append(" select unit_id ");
      sql.append(" from cr_manager_units_of_scope cmnose ");
      sql.append(" where cmnose.cmse_id = :scope_id");
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" )");
      params.put("scope_id", Long.valueOf(crDTO.getScopeId()));

    }
    if (Constants.CR_SEARCH_TYPE.CONSIDER.toString().equals(crDTO.getSearchType())) {
      //tuanpv edit cho phep nhan vien nhin thay CR can tham dinh cua ca don vi

//            if (isManager) {
      sql.append(" and cr.state = :state_coor ");
      params.put("state_coor", Constants.CR_STATE.COORDINATE);
      sql.append(" and ( cr.consider_unit_id = :unit_id ");
      params.put("unit_id", userDept);
      sql.append(" OR  cr.consider_user_id = :user_id )");
      params.put("user_id", userId);
    }

    if (Constants.CR_SEARCH_TYPE.SCHEDULE.toString().equals(crDTO.getSearchType())) {
      sql.append(" and cr.state = :state_eval ");
      params.put("state_eval", Constants.CR_STATE.EVALUATE);
      sql.append(" and ((cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit = :manage_unit");
      sql.append(" and scope_id = :scope_id ");
      if (Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY = 1 ");
      } else {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 ");
      }
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
          + "  FROM common_gnoc.unit ut "
          + "    START WITH ut.unit_id IN "
          + "    (SELECT excute_unit_id "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = :manage_unit "
          + "    AND scope_id      = :scope_id "
          + "    AND DEVICE_TYPE  IS NOT NULL ");
      if (Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY = 1 ");
      } else {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 ");
      }
      sql.append("    ) "
          + "    CONNECT BY PRIOR unit_id = parent_unit_id "
          + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = :manage_unit  AND scope_id = :scope_id ");
      if (Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY = 1 )) ");
      } else {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 ))");
      }

      sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit))");
      params.put("manage_unit", userDept);
      params.put("scope_id", Long.valueOf(crDTO.getScopeId()));
      sql.append(
          " or (cr.cr_type= :cr_type_emer and cr.process_type_id in (select cr_process_id from cr_ocs_schedule ");
      sql.append(" where user_id= :user_id)) ");
      params.put("cr_type_emer", Constants.CR_TYPE.EMERGENCY);
      params.put("user_id", userId);
      sql.append(" ) ");

    }
    if (Constants.CR_SEARCH_TYPE.EXCUTE.toString().equals(crDTO.getSearchType())) {
      if (isManager) {
        //20160622 daitr1 chinh sua chuc nang giao viec
//                sql.append(" and cr.state = ? ");
        sql.append(
            " and ( cr.state = :state_approve  or (cr.state = :state_accept and cr.earliest_start_time > sysdate)) ");
        params.put("state_approve", Constants.CR_STATE.APPROVE);
        params.put("state_accept", Constants.CR_STATE.ACCEPT);
        //20160622 daitr1 chinh sua chuc nang giao viec
        sql.append(" and (cr.change_responsible_unit = :responsible_unit ");
        params.put("responsible_unit", userDept);
        sql.append(" or cr.change_responsible = :change_responsible )");
        params.put("change_responsible", userId);
      } else {
        sql.append(" and cr.state = :state_approve ");
        params.put("state_approve", Constants.CR_STATE.APPROVE);
        sql.append(" and ((cr.change_responsible_unit = :responsible_unit and cr.cr_type = 0 )");
        params.put("responsible_unit", userDept);
        sql.append(" or cr.change_responsible = :change_responsible) ");
        params.put("change_responsible", userId);
      }
    }
    if (Constants.CR_SEARCH_TYPE.RESOLVE.toString().equals(crDTO.getSearchType())) {
      sql.append(" and cr.state = :state_accept ");
      params.put("state_accept", Constants.CR_STATE.ACCEPT);
      sql.append(" and cr.change_responsible = :change_responsible ");
      params.put("change_responsible", userId);
    }
    if (Constants.CR_SEARCH_TYPE.CLOSE.toString().equals(crDTO.getSearchType())) {
//            if (isManager) {
      if (StringUtils.isStringNullOrEmpty(crDTO.getScopeId())) {
        if (isManager) {
          sql.append(" and cr.state = :state_resolve ");
          params.put("state_resolve", Constants.CR_STATE.RESOLVE);
          sql.append(" and cr.manage_unit_id = :manage_unit ");
          params.put("manage_unit", userDept);
        } else {
          sql.append(" and 1=2");//Nhan vien khong co quyen tac dong den CR cho` dong
        }
      } else {

        sql.append(" and cr.state = :state_resolve ");
        params.put("state_resolve", Constants.CR_STATE.RESOLVE);
        sql.append(" and ( cr.change_responsible_unit in ");
        sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
        sql.append(" start with ut.unit_id in ");
        sql.append(" (select ");
        sql.append(" excute_unit_id  from v_manage_cr_config ");
        sql.append(" where ");
        sql.append(" manage_unit = :manage_unit");
        sql.append(" and scope_id = :scope_id ");
        sql.append("  and IS_SCHEDULE_CR_EMERGENCY <> 1 )");
        sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
        sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
            + "  FROM common_gnoc.unit ut "
            + "    START WITH ut.unit_id IN "
            + "    (SELECT excute_unit_id "
            + "    FROM OPEN_PM.v_manage_cr_config "
            + "    WHERE manage_unit = :manage_unit "
            + "    AND scope_id      = :scope_id "
            + "    AND DEVICE_TYPE  IS NOT NULL "
            + "    ) "
            + "    CONNECT BY PRIOR unit_id = parent_unit_id "
            + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
            + "    FROM OPEN_PM.v_manage_cr_config "
            + "    WHERE manage_unit = :manage_unit  AND scope_id = :scope_id AND DEVICE_TYPE  IS NOT NULL )))");
        sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit)");
        sql.append(" and (cr.manage_unit_id = :manage_unit ");
        sql.append(" OR (cr.manage_unit_id is null))");

        params.put("manage_unit", userDept);
        params.put("scope_id", Long.valueOf(crDTO.getScopeId()));
      }
    }

    if (StringUtils.isNotNullOrEmpty(crDTO.getCrNumber())) {
      String[] arrCrNumber = crDTO.getCrNumber().split(",");
      if (arrCrNumber.length > 1) {
        List<String> lstCrNumber = Arrays.asList(arrCrNumber);
        sql.append(" and cr.cr_number in(:lst_cr) ");
        params.put("lst_cr", lstCrNumber);
      } else {
        sql.append(" and LOWER(cr.cr_number) like :cr_number  ESCAPE '\\' ");
        params.put("cr_number", StringUtils.convertLowerParamContains(crDTO.getCrNumber()));
      }
    }

    if (StringUtils.isNotNullOrEmpty(crDTO.getTitle())) {
      sql.append(" and lower(cr.title) like :cr_title  ESCAPE '\\' ");
      params.put("cr_title", StringUtils.convertLowerParamContains(crDTO.getTitle()));
    }

    if (crDTO.getEarliestStartTime() != null) {
      sql.append(" and cr.earliest_start_time >= :earliest_start_time ");
//            paramList.add(dtu.convertStringToDateTime(crDTO.getEarliestStartTime().trim()));
      params.put("earliest_start_time", DateTimeUtils.convertDateToOffset(
          crDTO.getEarliestStartTime(), offset, true));
    }

    if (StringUtils.isNotNullOrEmpty(crDTO.getEarliestStartTimeTo())) {
      sql.append(" and cr.earliest_start_time <= :earliest_start_time_to ");
//            paramList.add(dtu.convertStringToDateTime(crDTO.getEarliestStartTimeTo().trim()));
      params.put("earliest_start_time_to", DateTimeUtils.converStringClientToServerDate(
          crDTO.getEarliestStartTimeTo().trim(), offset));
    }

    if (crDTO.getLatestStartTime() != null) {
      sql.append(" and cr.latest_start_time >= :latest_start_time ");
//            paramList.add(dtu.convertStringToDateTime(crDTO.getLatestStartTime().trim()));
      params.put("latest_start_time", DateTimeUtils.convertDateToOffset(
          crDTO.getLatestStartTime(), offset, true));
    }

    if (StringUtils.isNotNullOrEmpty(crDTO.getLatestStartTimeTo())) {
      sql.append(" and cr.latest_start_time <= :latest_start_time_to ");
//            paramList.add(dtu.convertStringToDateTime(crDTO.getLatestStartTimeTo().trim()));
      params.put("latest_start_time_to", DateTimeUtils.converStringClientToServerDate(
          crDTO.getLatestStartTimeTo().trim(), offset));
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getIsOutOfDate())) {
      if (Constants.CR_OUTDATE_TYPE.OUTOFDATE.equalsIgnoreCase(crDTO.getIsOutOfDate())) {
        sql.append(" and cr.LATEST_START_TIME < sysdate ");
      } else if (Constants.CR_OUTDATE_TYPE.ONTIME.equalsIgnoreCase(crDTO.getIsOutOfDate())) {
        sql.append(" and cr.LATEST_START_TIME >= sysdate ");
      }
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getCrType())
        && !"-1".equals(crDTO.getCrType().trim())) {
      sql.append(" and cr.cr_type = :cr_type ");
      params.put("cr_type", Long.parseLong(crDTO.getCrType().trim()));
    }

    if (StringUtils.isNotNullOrEmpty(crDTO.getSubcategory())
        && !"-1".equals(crDTO.getSubcategory().trim())) {
      sql.append(" and cr.Subcategory = :sub_category ");
      params.put("sub_category", Long.parseLong(crDTO.getSubcategory().trim()));
    }

    if (StringUtils.isNotNullOrEmpty(crDTO.getRisk())
        && !"-1".equals(crDTO.getRisk().trim())) {
      sql.append(" and cr.risk = :risk ");
      params.put("risk", Long.parseLong(crDTO.getRisk().trim()));
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getImpactSegment())
        && !"-1".equals(crDTO.getImpactSegment().trim())) {
      sql.append(" and cr.impact_segment = :impact_segment ");
      params.put("impact_segment", Long.parseLong(crDTO.getImpactSegment().trim()));
    }

    if (StringUtils.isNotNullOrEmpty(crDTO.getCountry())
        && !"-1".equals(crDTO.getCountry().trim())) {
      if ("4001000000".equals(crDTO.getCountry()) || "5000289722".equals(crDTO.getCountry())
          || "1000014581".equals(crDTO.getCountry())
          || "3500289726".equals(crDTO.getCountry()) || "4500000001".equals(crDTO.getCountry())
          || "6000289723".equals(crDTO.getCountry())
          || "3000289724".equals(crDTO.getCountry()) || "2000289729".equals(crDTO.getCountry())
          || "1500289728".equals(crDTO.getCountry())) {
        sql.append(" and cr.country in( :country1, :country2 )");
        params.put("country1", Long.parseLong(crDTO.getCountry().trim()));
        if ("1500289728".equals(crDTO.getCountry())) {//timor
          params.put("country2", 289728L);
        } else if ("2000289729".equals(crDTO.getCountry())) {//timor
          params.put("country2", 289729L);
        } else if ("3000289724".equals(crDTO.getCountry())) {//mozam
          params.put("country2", 289724L);
        } else if ("6000289723".equals(crDTO.getCountry())) {//timor
          params.put("country2", 289723L);
        } else if ("4500000001".equals(crDTO.getCountry())) {//myanmar
          params.put("country2", 300656L);
        } else if ("4001000000".equals(crDTO.getCountry())) {//tanz
          params.put("country2", 289727L);
        } else if ("5000289722".equals(crDTO.getCountry())) {//lao
          params.put("country2", 289722L);
        } else if ("1000014581".equals(crDTO.getCountry())) {//cam
          params.put("country2", 289721L);
        } else if ("3500289726".equals(crDTO.getCountry())) {//burundi
          params.put("country2", 289726L);
        }
      } else {
        sql.append(" and cr.country = :country ");
        params.put("country", Long.parseLong(crDTO.getCountry().trim()));
      }
    }

    if (StringUtils.isNotNullOrEmpty(crDTO.getChangeOrginator())
        && !"-1".equals(crDTO.getChangeOrginator().trim())) {
      sql.append(" and cr.Change_Orginator = :change_orginator ");
      params.put("change_orginator", Long.parseLong(crDTO.getChangeOrginator().trim()));
    }

    if (StringUtils.isNotNullOrEmpty(crDTO.getChangeResponsible())
        && !"-1".equals(crDTO.getChangeResponsible().trim())) {
      sql.append(" and cr.Change_Responsible = :change_responsible ");
      params.put("change_responsible", Long.parseLong(crDTO.getChangeResponsible().trim()));
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getChangeOrginatorUnit())
        && !"-1".equals(crDTO.getChangeOrginatorUnit().trim())) {
      if (crDTO.getSubDeptOri() != null && "1".equals(crDTO.getSubDeptOri().trim())) {
        sql.append(" and cr.Change_Orginator_Unit in ( ");
        sql.append(" SELECT unit_id");
        sql.append(" FROM common_gnoc.unit ");
        sql.append(" START WITH  unit_id = :change_unit ");
        sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
        sql.append(" )");
        params.put("change_unit", Long.parseLong(crDTO.getChangeOrginatorUnit().trim()));
      } else {
        sql.append(" and cr.Change_Orginator_Unit = :change_unit ");
        params.put("change_unit", Long.parseLong(crDTO.getChangeOrginatorUnit().trim()));
      }
    }
    if (StringUtils.isNotNullOrEmpty(crDTO.getChangeResponsibleUnit())
        && !"-1".equals(crDTO.getChangeResponsibleUnit().trim())) {
      if (crDTO.getSubDeptResp() != null && "1".equals(crDTO.getSubDeptResp().trim())) {
        sql.append(" and cr.Change_Responsible_Unit in ( ");
        sql.append(" SELECT unit_id");
        sql.append(" FROM common_gnoc.unit ");
        sql.append(" START WITH  unit_id = :change_res_unit ");
        sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
        sql.append(" )");
        params.put("change_res_unit", Long.parseLong(crDTO.getChangeResponsibleUnit().trim()));
      } else {
        sql.append(" and cr.Change_Responsible_Unit = :change_res_unit ");
        params.put("change_res_unit", Long.parseLong(crDTO.getChangeResponsibleUnit().trim()));
      }
    }
    BaseDto baseDto = new BaseDto();
    baseDto.setParameters(params);
    baseDto.setSqlQuery(sql.toString());
    return baseDto;
  }

  @Override
  public List<CrInsiteDTO> getListCRByLstId(String lstId) {
    try {

      Map<String, Object> params = new HashMap<>();
      List<String> lstIds = new ArrayList<>();
      if (StringUtils.isNotNullOrEmpty(lstId)) {
        String[] arrIds = lstId.split(",");
        lstIds = Arrays.asList(arrIds);
      }
      params.put("lstId", lstIds);
      StringBuffer sql = new StringBuffer();
      sql.append("select cr.cr_id crId, cr.cr_number crNumber from open_pm.cr cr ")
          .append(" where cr.cr_id in (:lstId) and cr.earliest_start_time>=sysdate-165 and ")
          .append(" cr.earliest_start_time<=sysdate+15");

      List<CrInsiteDTO> list = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(CrInsiteDTO.class));
      return list;

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<CrInsiteDTO> getListPreApprovedCr(CrInsiteDTO crInsiteDTO) {
    List<CrInsiteDTO> result = new ArrayList<>();
    try {
      result = crDBRepository.getListPreApprovedCr(crInsiteDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  @Override
  public List<CrInsiteDTO> getListSecondaryCr(CrInsiteDTO crInsiteDTO) {
    List<CrInsiteDTO> result = new ArrayList<>();
    try {
      result = crDBRepository.getListSecondaryCr(crInsiteDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  @Override
  public List<CrInsiteDTO> getListCrByCondition(List<ConditionBean> lstCondition, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new CrEntity(), lstCondition, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public String actionLoadMopFromCRParent(String crId, String isLoadMop) {
    try {
      String sqlUser = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "action-load-mop-from-cr-parent");
      Map<String, Object> params = new HashMap<>();
      params.put("is_load_mop", isLoadMop);
      params.put("cr_id", crId);
      getNamedParameterJdbcTemplate().update(sqlUser, params);
      getEntityManager().flush();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Constants.CR_RETURN_MESSAGE.ERROR;
    }
    return Constants.CR_RETURN_MESSAGE.SUCCESS;
  }


  @Override
  public List<CrCreatedFromOtherSysDTO> getCrCreatedFromOtherSys(
      CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-cr-create-from-other-sys");
      Map<String, Object> params = new HashMap<>();
      params.put("crId", crCreatedFromOtherSysDTO.getCrId());
      params.put("systemId", crCreatedFromOtherSysDTO.getSystemId());
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrCreatedFromOtherSysDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;

  }

  @Override
  public Datatable getListCRFromOtherSystem(CrInsiteDTO crDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-List-CR-From-Other-System");
    Map<String, Object> params = new HashMap<>();
    if (crDTO.getSystemId() != null) {
      sql += " and cr.cr_id in ("
          + "select cs.cr_id "
          + "from cr_created_from_other_sys cs "
          + "where cs.system_id = :systemId";
      params.put("systemId", crDTO.getSystemId());
    }
    if (crDTO.getObjectId() != null) {
      sql += " and cs.object_id = :objectId";
      params.put("objectId", crDTO.getObjectId());
    }
    if (crDTO.getStepId() != null) {
      sql += " and cs.step_id = :stepId";
      params.put("stepId", crDTO.getStepId());
    }
    sql += " and cs.is_active = 1)";
    return getListDataTableBySqlQuery(sql, params, crDTO.getPage(), crDTO.getPageSize(),
        CrInsiteDTO.class, crDTO.getSortName(), crDTO.getSortType());
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    CrEntity entity = getEntityManager().find(CrEntity.class, id);
    if (entity != null) {
      getEntityManager().remove(entity);
    }
    return resultInSideDTO;
  }

  @Override
  public CrDTO getCrByNumber(String crNumber, Date startCreatedDate, Date endCreatedDate) {
    if (crNumber != null && !crNumber.trim().isEmpty()) {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "getCrByNumber");
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("crNumber", crNumber);
      if (startCreatedDate != null) {
        sql += " and  cr.CREATED_DATE >= :startCreatedDate ";
        parameters.put("startCreatedDate", startCreatedDate);
      }
      if (endCreatedDate != null) {
        sql += " and  cr.CREATED_DATE <= :endCreatedDate ";
        parameters.put("startCreatedDate", endCreatedDate);
      }
      List<CrDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(CrDTO.class));
      return lst.isEmpty() ? null : lst.get(0);
    }
    return null;
  }

  @Override
  public String actionUpdateNotify(CrInsiteDTO crDTO, Long actionCode) {
    return smsDBRepository.actionUpdateNotify(crDTO, actionCode);
  }

  @Override
  public UsersDTO getUserInfo(String userName) {
    String sqlUser = "select a.user_id userId, a.username username,a.unit_id unitId from common_gnoc.users a where a.IS_ENABLE = 1 and a.username =:username ";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("username", userName);
    List<UsersDTO> lstUser = getNamedParameterJdbcTemplate()
        .query(sqlUser, parameters, BeanPropertyRowMapper.newInstance(UsersDTO.class));
    return lstUser.isEmpty() ? null : lstUser.get(0);
  }

  @Override
  public UnitDTO getUnitInfo(String unitId) {
    String sqlUnit = " select UNIT_ID unitId, UNIT_CODE unitCode, UNIT_NAME unitName from common_gnoc.unit where STATUS =  1 and unit_id =:unit_id ";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("unit_id", unitId);
    List<UnitDTO> lstUnit = getNamedParameterJdbcTemplate()
        .query(sqlUnit, parameters, BeanPropertyRowMapper.newInstance(UnitDTO.class));
    return lstUnit.isEmpty() ? null : lstUnit.get(0);
  }


  public List<CrDTO> getListCRBySearchTypeOutSide(CrDTO crDTO, List<List<Long>> searchCrIds,
      int start, int maxResult,
      String locale) {
    try {
      if (crDTO != null) {
        String userIdStr = crDTO.getUserLogin();
        String userIdDeptStr = crDTO.getUserLoginUnit();//R_xxx uyquyen
        Long userId = null;
        Long userDept = null;
        if (userIdStr != null) {
          userId = Long.valueOf(userIdStr);
        }
        if (userIdDeptStr != null) {
          userDept = Long.valueOf(userIdDeptStr);
        }
        if (userId != null && userDept != null && crDTO.getSearchType() != null) {
          boolean isManager = userRepository.isManagerOfUnits(userId);
          List<CrDTO> list;
          List paramList = new ArrayList();
          StringBuffer sql = new StringBuffer();
          sql.append("select cr.title title, cr.cr_id crId, "
              + " cr.cr_number crNumber,"
              + " TO_CHAR((cr.update_time + ? * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss') updateTime,"
              + " TO_CHAR((cr.earliest_start_time + ? * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss') earliestStartTime,"
              + " TO_CHAR((cr.latest_start_time + ? * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss') latestStartTime,"
              + " TO_CHAR((cr.created_date + ? * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss') createdDate,"
              + " cr.priority priority,"
              + " case when utOri.unit_code is null then ''"
              + " else TO_CHAR(utOri.unit_code || ' ('||utOri.unit_name||')') end as changeOrginatorUnitName,"
              + " case when usOri.username is null then ''"
              + " else TO_CHAR(usOri.username || ' ('||usOri.fullname||')') end as changeOrginatorName,"
              + " case when utResp.unit_code is null then ''"
              + " else TO_CHAR(utResp.unit_code || ' ('||utResp.unit_name||')') end as changeResponsibleUnitName,"
              + " case when usResp.username is null then ''"
              + " else TO_CHAR(usResp.username || ' ('||usResp.fullname||')') end as changeResponsibleName,"
              + " case when utConsi.unit_code is null then ''"
              + " else TO_CHAR(utConsi.unit_code || ' ('||utConsi.unit_name||')') end as considerUnitName,"
              + " case when usConsi.username is null then ''"
              + " else TO_CHAR(usConsi.username || ' ('||usConsi.fullname||')') end as considerUserName,"
              + " cr.state,"
              + " cr.cr_type_cat crTypeCat,"
              + " cr.relate_to_pre_approved_cr relateToPreApprovedCr,"
              + " cr.cr_type crType,"
              + " cr.change_responsible as changeResponsible,"
              + " cr.change_responsible_unit as changeResponsibleUnit,"
              + " cr.change_orginator as changeOrginator,"
              + " cr.change_orginator_unit as changeOrginatorUnit,"
              + " cr.consider_unit_id as considerUnitId,"
              + " cr.consider_user_id as considerUserId,"
              + " cr.IMPACT_SEGMENT impactSegment,"
              + " cr.CHILD_IMPACT_SEGMENT childImpactSegment,"
              //20160622 daitr1 bo sung cot thoi gian qua han
              + " case cr.state "
              + " when ? "
              + " then "
              + " case"
              + " when exists (select 1 from "
              + " open_pm.cr_his chs1 "
              + " where cr.cr_id = chs1.cr_id and chs1.action_code in (?,?,?,?,?)) then '' "
              + " else   TO_CHAR(((select max(chs2.change_date) from open_pm.cr_his chs2 where cr.cr_id = chs2.cr_id"
              + " and chs2.change_date >= cr.created_date "
              + " and chs2.action_code in (?,?,?))"
              + " + ? * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss') end"
              + " when ? then '' "
              + " else to_char(sysdate + ? * interval '1' hour, 'dd/MM/yyyy HH24:mi:ss')"
              + " end as compareDate,"
              + " cr.user_cab userCab, TO_CHAR(cr.AUTO_EXECUTE) as autoExecute, cr.respone_time responeTime "
              + " from open_pm.cr cr"
              + " left join common_gnoc.unit utOri on cr.change_orginator_unit = utOri.unit_id"
              + " left join common_gnoc.users usOri on cr.change_orginator = usOri.user_id"
              + " left join common_gnoc.unit utResp on cr.change_responsible_unit = utResp.unit_id"
              + " left join common_gnoc.users usResp on cr.change_responsible = usResp.user_id"
              + " left join common_gnoc.unit utConsi on cr.consider_unit_id = utConsi.unit_id"
              + " left join common_gnoc.users usConsi on cr.consider_user_id = usConsi.user_id"
              + " where usOri.is_enable = 1 ");

          BaseDto searchForm = genConditionSearchCrOutSide(crDTO, userId, userDept, isManager);
          sql.append(searchForm.getSqlQuery());
          paramList.addAll((List) searchForm.getParameters().get("paramList"));

          if (searchCrIds != null && searchCrIds.size() > 0) {
            sql.append(" and (  cr.cr_id in (:searchCrIds0) ");
            for (int i = 1; i < searchCrIds.size(); i++) {
              sql.append(" or cr.cr_id in (:searchCrIds").append(i).append(") ");
            }
            sql.append(" ) ");
          }

          sql.append(" order by cr.UPDATE_TIME desc ");
          Query query = getEntityManager().createNativeQuery(sql.toString());
          query.unwrap(NativeQuery.class).
              addScalar("crId", new StringType()).
              addScalar("title", new StringType()).
              addScalar("crNumber", new StringType()).
              addScalar("crType", new StringType()).
              addScalar("earliestStartTime", new StringType()).
              addScalar("latestStartTime", new StringType()).
              addScalar("createdDate", new StringType()).
              addScalar("priority", new StringType()).
              addScalar("changeOrginator", new StringType()).
              addScalar("changeOrginatorUnit", new StringType()).
              addScalar("changeResponsible", new StringType()).
              addScalar("changeResponsibleUnit", new StringType()).
              addScalar("changeOrginatorName", new StringType()).
              addScalar("changeOrginatorUnitName", new StringType()).
              addScalar("changeResponsibleName", new StringType()).
              addScalar("changeResponsibleUnitName", new StringType()).
              addScalar("considerUnitId", new StringType()).
              addScalar("considerUserId", new StringType()).
              addScalar("considerUnitName", new StringType()).
              addScalar("considerUserName", new StringType()).
              addScalar("updateTime", new StringType()).
              addScalar("state", new StringType()).
              addScalar("crTypeCat", new StringType()).
              addScalar("relateToPreApprovedCr", new StringType()).
              addScalar("impactSegment", new StringType()).
              addScalar("childImpactSegment", new StringType()).
              //20160622 daitr1 bo sung cot thoi gian qua han
                  addScalar("compareDate", new StringType()).
              //20160622 daitr1 bo sung cot thoi gian qua han
                  addScalar("userCab", new StringType()).

              //longlt6 add 2017-07-12
                  addScalar("autoExecute", new StringType()).
              addScalar("responeTime", new StringType())
              .setResultTransformer(Transformers.aliasToBean(CrDTO.class));
          for (int i = 0; i < paramList.size(); i++) {
            query.setParameter(i + 1, paramList.get(i));
          }
          if (searchCrIds != null && searchCrIds.size() > 0) {
            for (int i = 0; i < searchCrIds.size(); i++) {
              query.setParameter("searchCrIds" + i, searchCrIds.get(i));
            }
          }

          query.setFirstResult(start);
          query.setMaxResults(maxResult);

          list = query.getResultList();
          list = crDBRepository
              .processListToGenGrOutSide(list, crDTO, isManager, userId, userDept, locale);
          return list;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public ObjResponse getListCRBySearchTypePagging(CrDTO crDTO, int start, int maxResult,
      String locale) {
    ObjResponse obj = new ObjResponse();

    List<List<Long>> searchCrIds = null;
    List<Long> allIps = null;
    try {

      if (crDTO.getSearchImpactedNodeIpIds() != null && !crDTO.getSearchImpactedNodeIpIds()
          .isEmpty()) {
        Date startDate = DateTimeUtils.convertStringToDate(crDTO.getEarliestStartTime());
        Date endDate = DateTimeUtils.convertStringToDate(crDTO.getEarliestStartTimeTo());

        if (startDate == null) {
          startDate = new Date();
        }
        if (endDate == null) {
          endDate = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DATE, -7);
        startDate = cal.getTime();

        allIps = getlistCrIdsByNodeInfo(startDate, endDate, crDTO.getSearchImpactedNodeIpIds());
        if (allIps == null || allIps.isEmpty()) {
          obj.setTotalRow(0);
          obj.setLstCrDTO(new ArrayList<>());
          return obj;
        }

        int count = allIps.size() / 500;
        if (allIps.size() % 500 != 0) {
          count++;
        }

        List<Long> crids;
        searchCrIds = new ArrayList<>();
        for (int p = 1; p <= count; p++) {
          if (p < count) {
            crids = allIps.subList((p - 1) * 500, p * 500);
          } else {
            crids = allIps.subList((p - 1) * 500, allIps.size());
          }
          if (crids != null && !crids.isEmpty()) {
            searchCrIds.add(crids);
          }
        }

      }

      List<CrDTO> lst = getListCRBySearchTypeOutSide(crDTO, searchCrIds, start, maxResult, locale);
      CrInsiteDTO crInsiteDTO = crDTO.toModelInsiteDTO();
      crInsiteDTO.setSearchCrIds(searchCrIds);
      long count = countCRBySearchType(crInsiteDTO);
      obj.setLstCrDTO(lst);
      obj.setTotalRow(count);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      if (searchCrIds != null) {
        searchCrIds.clear();
      }

      if (allIps != null) {
        allIps.clear();
      }
    }

    return obj;
  }

  private BaseDto genConditionSearchCrOutSide(CrDTO crDTO, Long userId, Long userDept,
      boolean isManager) throws Exception {
    StringBuffer sql = new StringBuffer("");
    List paramList = new ArrayList();

    Double offset = userRepository.getOffsetFromUser(userId);
    paramList.add(offset);
    paramList.add(offset);
    paramList.add(offset);
    paramList.add(offset);
    paramList.add(Constants.CR_STATE.CLOSE);
    paramList.add(Constants.CR_ACTION_CODE.REJECT);
    paramList.add(Constants.CR_ACTION_CODE.CLOSE);
    paramList.add(Constants.CR_ACTION_CODE.CLOSE_BY_APPRAISER);
    paramList.add(Constants.CR_ACTION_CODE.CLOSE_BY_MANAGER_SCH);
    paramList.add(Constants.CR_ACTION_CODE.CLOSE_EXCUTE_EMERGENCY);
    paramList.add(Constants.CR_ACTION_CODE.CLOSECR);
    paramList.add(Constants.CR_ACTION_CODE.CLOSECR_APPROVE_STD);
    paramList.add(Constants.CR_ACTION_CODE.CLOSE_EXCUTE_STD);
    paramList.add(offset);
    paramList.add(Constants.CR_STATE.DRAFT);
    paramList.add(offset);
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.CREATE_EDIT.toString())) {
      sql.append(" and ( ( ");
      // <editor-fold desc="Cho phep nguoi tao CR co the sua CR khi CR chua phe duyet hoac INCOMPLETE">
      sql.append(" cr.state in (?, ?, ?) ");
      paramList.add(Constants.CR_STATE.OPEN);
      paramList.add(Constants.CR_STATE.INCOMPLETE);
      paramList.add(Constants.CR_STATE.DRAFT);
      sql.append(" and cr.change_orginator_unit = ? ");
      paramList.add(userDept);
      sql.append(" and cr.change_orginator = ? ");
      paramList.add(userId);
      // </editor-fold>

      sql.append(" ) or (");
      // <editor-fold desc="Cho phep tat ca nhan vien trong don vi them WLog khi CR RESOLVE">
      sql.append(" cr.state = ? ");
      paramList.add(Constants.CR_STATE.RESOLVE);
      sql.append(" and cr.change_orginator_unit in ( ");
      sql.append(" SELECT unit_id");
      sql.append(" FROM common_gnoc.unit ");
      sql.append(" START WITH  unit_id = ? ");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" )");
      paramList.add(userDept);
      // </editor-fold>
      sql.append(" ) )");
    }
//        if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.LOOKUP.toString()) &&
//                crDTO.getState() != null && !crDTO.getState().trim().equals("")) {
//                sql.append(" and cr.state = ? ");
//                paramList.add(crDTO.getState());
//        }

    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.LOOKUP.toString())) {
      if (crDTO.getState() != null && !"".equals(crDTO.getState().trim())) {
        String[] arrState = crDTO.getState().split(",");
        sql.append(" and cr.state in ( ");
        for (int i = 0; i < arrState.length; i++) {
          sql.append("?, ");
          paramList.add(arrState[i]);
        }
        sql.append("-1) ");
      } else {
        sql.append(" and cr.state in (-1) ");
      }
    }

    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.APPROVE.toString())) {
      sql.append(" and cr.state = ? ");
      paramList.add(Constants.CR_STATE.OPEN);
      sql.append(
          " and exists (select 1 from common_gnoc.v_user_role where user_id = ? and role_code = ? )");
      paramList.add(userId);
      paramList.add(Constants.CR_ROLE.roleTP);
//                        if(crDTO.getState().equals(Constants.CR_STATE.OPEN.toString())){
      if (crDTO.getIsSearchChildDeptToApprove() != null
          && "1".equals(crDTO.getIsSearchChildDeptToApprove().trim())) {
        sql.append(" and cr.cr_id in (");
        sql.append(" select cr_id from ");
        sql.append(" cr_approval_department d ");
        sql.append(" where ");
        sql.append(" d.cr_id = cr.cr_id and d.unit_id = ? ");
        sql.append(" and d.status = 0 )");
        paramList.add(userDept);
      } else {
        sql.append(" and cr.cr_id in (");
        sql.append(" with tbl as (");
        sql.append(" select d.unit_id,d.cr_id,d.cadt_level,");
        sql.append(" (select min(b.cadt_level) from cr_approval_department b");
        sql.append(" where  b.cr_id = d.cr_id and b.status = 0) as num");
        sql.append(" from cr_approval_department d");
        sql.append(" where d.unit_id = ?   and d.status = 0)");
        sql.append(" select tb.cr_id   from tbl tb where tb.cadt_level <= tb.num");
        sql.append(" ) ");
        paramList.add(userDept);
      }
//            sql.append(" and ( (cr.is_primary_cr is null) ");
//            sql.append(" OR (cr.is_primary_cr is not null ");
//            sql.append(" and exists (select 1 from cr cr1 where cr1.relate_to_primary_cr = cr.cr_id))");
//            sql.append(" )");
//                        }else{
//                            sql.append(" and cr.cr_id in (select d.cr_id from cr_approval_department d ");
//                            sql.append(" where unit_id =  ? )");
//                            paramList.add(userDept);
//                        }
    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.VERIFY.toString())) {
      sql.append(" and cr.state = ? ");
      paramList.add(Constants.CR_STATE.QUEUE);
      sql.append(" and ( cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit = ?");
      sql.append(" and scope_id = ? AND DEVICE_TYPE  IS NULL ");
      sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 ");
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
          + "  FROM common_gnoc.unit ut "
          + "    START WITH ut.unit_id IN "
          + "    (SELECT excute_unit_id "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = ? "
          + "    AND scope_id      = ? "
          + "    and IS_SCHEDULE_CR_EMERGENCY <> 1 AND DEVICE_TYPE  IS NOT NULL "
          + "    ) "
          + "    CONNECT BY PRIOR unit_id = parent_unit_id "
          + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = ?  AND scope_id = ? AND DEVICE_TYPE  IS NOT NULL )))");
      sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = ?)");
      paramList.add(userDept);
      paramList.add(Long.valueOf(crDTO.getScopeId()));
      paramList.add(userDept);
      paramList.add(Long.valueOf(crDTO.getScopeId()));
      paramList.add(userDept);
      paramList.add(Long.valueOf(crDTO.getScopeId()));
      paramList.add(userDept);

    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.WAIT_CAB.toString())) {
      //chi CAB cr thuong
      sql.append(" and cr.state in( ?,?) ");
      paramList.add(Constants.CR_STATE.WAIT_CAB);
      paramList.add(Constants.CR_STATE.CAB);
      sql.append(" and ( cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" scope_id = ? AND DEVICE_TYPE IS NULL ");

      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
          + "  FROM common_gnoc.unit ut "
          + "    START WITH ut.unit_id IN "
          + "    (SELECT excute_unit_id "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = ? "
          + "    AND scope_id      = ? "
          + "    AND DEVICE_TYPE  IS NOT NULL "
          + "    ) "
          + "    CONNECT BY PRIOR unit_id = parent_unit_id "
          + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = ?  AND scope_id = ? AND DEVICE_TYPE  IS NOT NULL)))");
      paramList.add(Long.valueOf(crDTO.getScopeId()));
      paramList.add(userDept);
      paramList.add(Long.valueOf(crDTO.getScopeId()));
      paramList.add(userDept);
      paramList.add(Long.valueOf(crDTO.getScopeId()));

    }

    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.CAB.toString())) {

      sql.append(" and (");
      sql.append("( cr.state = ? ");
      paramList.add(Constants.CR_STATE.CAB);
      sql.append(" and cr.user_cab =?) ");
      paramList.add(userId);

      //cr khan cho phep user duoc cau hinh co the cab khi sap lich
      sql.append(" OR (cr.cr_type = ? ");
      paramList.add(Constants.CR_TYPE.EMERGENCY);
      sql.append(" and cr.state = ? ");
      paramList.add(Constants.CR_STATE.EVALUATE);

      sql.append(" and cr.change_responsible_unit in( ");
      sql.append(" select cab.execute_unit_id from open_pm.cr_cab_users cab");
      sql.append(" where cab.user_id=?)");
      paramList.add(userId);

      sql.append(" and cr.impact_segment in ( ");
      sql.append(" select cab.impact_segment_id from open_pm.cr_cab_users cab");
      sql.append(" where cab.user_id=?)");
      paramList.add(userId);
      sql.append(" )  ");
      //cr khan cho phep user duoc cau hinh co the cab khi sap lich

      if (crDTO.getScopeId() != null) {
        sql.append(" OR (  cr.state in ( ?,?,?) ");
        paramList.add(Constants.CR_STATE.APPROVE);
        paramList.add(Constants.CR_STATE.ACCEPT);
        paramList.add(Constants.CR_STATE.RESOLVE);
        sql.append(" and cr.cr_type = ? ");
        paramList.add(Constants.CR_TYPE.EMERGENCY);
        sql.append(" and cr.change_responsible_unit in ");
        sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
        sql.append(" start with ut.unit_id in ");
        sql.append(" ( ");
        sql.append(" select unit_id ");
        sql.append(" from cr_manager_units_of_scope cmnose ");
        sql.append(" where cmnose.cmse_id = ?");
        paramList.add(Long.valueOf(crDTO.getScopeId()));
        sql.append(" )");
        sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
        sql.append(" ) ) ");
      }
      sql.append(" )");

    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.QLTD.toString())) {
      sql.append(" and cr.state in(?,?,?,?,?) ");
      paramList.add(Constants.CR_STATE.WAIT_CAB);
      paramList.add(Constants.CR_STATE.CAB);
      paramList.add(Constants.CR_STATE.QUEUE);
      paramList.add(Constants.CR_STATE.COORDINATE);
      paramList.add(Constants.CR_STATE.EVALUATE);
      sql.append(" and ( cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit = ?");
      sql.append(" and scope_id = ? AND DEVICE_TYPE IS NULL ");
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
          + "  FROM common_gnoc.unit ut "
          + "    START WITH ut.unit_id IN "
          + "    (SELECT excute_unit_id "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = ? "
          + "    AND scope_id      = ? "
          + "    AND DEVICE_TYPE  IS NOT NULL "
          + "    ) "
          + "    CONNECT BY PRIOR unit_id = parent_unit_id "
          + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = ?  AND scope_id = ? AND DEVICE_TYPE  IS NOT NULL )))");
      sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = ?)");
      paramList.add(userDept);
      paramList.add(Long.valueOf(crDTO.getScopeId()));
      paramList.add(userDept);
      paramList.add(Long.valueOf(crDTO.getScopeId()));
      paramList.add(userDept);
      paramList.add(Long.valueOf(crDTO.getScopeId()));
      paramList.add(userDept);

    }

    if (crDTO.getSearchType().equals(CR_SEARCH_TYPE.QLTD_RENEW.toString())) {
      sql.append(" and cr.state in(?) ");
      paramList.add(CR_STATE.ACCEPT);
      sql.append(" and ( cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit = ?");
      sql.append(" and scope_id = ? AND DEVICE_TYPE IS NULL ");
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
          + "  FROM common_gnoc.unit ut "
          + "    START WITH ut.unit_id IN "
          + "    (SELECT excute_unit_id "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = ? "
          + "    AND scope_id      = ? "
          + "    AND DEVICE_TYPE  IS NOT NULL "
          + "    ) "
          + "    CONNECT BY PRIOR unit_id = parent_unit_id "
          + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = ?  AND scope_id = ? AND DEVICE_TYPE  IS NOT NULL )))");
      sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = ?)");
      paramList.add(userDept);
      paramList.add(Long.valueOf(crDTO.getScopeId()));
      paramList.add(userDept);
      paramList.add(Long.valueOf(crDTO.getScopeId()));
      paramList.add(userDept);
      paramList.add(Long.valueOf(crDTO.getScopeId()));
      paramList.add(userDept);

    }

    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.Z78.toString())) {
      sql.append(" and cr.state = ? ");
      paramList.add(Constants.CR_STATE.ACCEPT);
      sql.append(" and cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" ( ");
      sql.append(" select unit_id ");
      sql.append(" from cr_manager_units_of_scope cmnose ");
      sql.append(" where cmnose.cmse_id = ?");
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" )");
      paramList.add(Long.valueOf(crDTO.getScopeId()));

    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.CONSIDER.toString())) {
      //tuanpv edit cho phep nhan vien nhin thay CR can tham dinh cua ca don vi

//            if (isManager) {
      sql.append(" and cr.state = ? ");
      paramList.add(Constants.CR_STATE.COORDINATE);
      sql.append(" and ( cr.consider_unit_id = ? ");
      paramList.add(userDept);
      sql.append(" OR  cr.consider_user_id = ? )");
      paramList.add(userId);
//            } else {
//                sql.append(" and cr.state = ? ");
//                paramList.add(Constants.CR_STATE.COORDINATE);
//                sql.append(" and cr.consider_user_id = ? ");
//                paramList.add(userId);
//            }
    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.SCHEDULE.toString())) {
      sql.append(" and cr.state = ? ");
      paramList.add(Constants.CR_STATE.EVALUATE);
      sql.append(" and ((cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit = ?");
      sql.append(" and scope_id = ? AND DEVICE_TYPE IS NULL ");
      if (Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY = 1 ");
      } else {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 ");
      }

      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
          + "  FROM common_gnoc.unit ut "
          + "    START WITH ut.unit_id IN "
          + "    (SELECT excute_unit_id "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = ? "
          + "    AND scope_id      = ? "
          + "    AND DEVICE_TYPE IS NOT NULL ");
      if (Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY = 1 ");
      } else {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 ");
      }
      sql.append("    ) "
          + "    CONNECT BY PRIOR unit_id = parent_unit_id "
          + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
          + "    FROM OPEN_PM.v_manage_cr_config "
          + "    WHERE manage_unit = ?  AND scope_id = ? AND DEVICE_TYPE  IS NOT NULL ");
      if (Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY = 1 )) ");
      } else {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 )) ");
      }

      sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = ?))");

      paramList.add(userDept);
      paramList.add(Long.valueOf(crDTO.getScopeId() != null ? crDTO.getScopeId() : "-1"));
      paramList.add(userDept);
      paramList.add(Long.valueOf(crDTO.getScopeId()));
      paramList.add(userDept);
      paramList.add(Long.valueOf(crDTO.getScopeId()));
      paramList.add(userDept);

      sql.append(
          " or (cr.cr_type=? and cr.process_type_id in (select cr_process_id from cr_ocs_schedule ");
      sql.append(" where user_id=?)) ");
      paramList.add(Constants.CR_TYPE.EMERGENCY);
      paramList.add(userId);
      sql.append(" ) ");

    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.EXCUTE.toString())) {
      if (isManager) {
        sql.append(" and ( cr.state = ? or (cr.state = ? and cr.earliest_start_time > sysdate)) ");
        paramList.add(Constants.CR_STATE.APPROVE);
        paramList.add(Constants.CR_STATE.ACCEPT);
        sql.append(" and (cr.change_responsible_unit = ? ");
        paramList.add(userDept);
        sql.append(" or cr.change_responsible = ? )");
        paramList.add(userId);
      } else {
        sql.append(" and cr.state = ? ");
        paramList.add(Constants.CR_STATE.APPROVE);
        sql.append(" and ((cr.change_responsible_unit = ? and cr.cr_type = 0 )");
        paramList.add(userDept);
        sql.append(" or cr.change_responsible = ?) ");
        paramList.add(userId);
      }
    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.RESOLVE.toString())) {
      sql.append(" and cr.state = ? ");
      paramList.add(Constants.CR_STATE.ACCEPT);
      sql.append(" and (cr.change_responsible = ? or cr.HANDOVER_CA = ?)");
      paramList.add(userId);
      paramList.add(userId);
    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.CLOSE.toString())) {
      if (crDTO.getScopeId() == null || "".equals(crDTO.getScopeId().trim())) {
        if (isManager) {
          sql.append(" and cr.state = ? ");
          paramList.add(Constants.CR_STATE.RESOLVE);
          sql.append(" and cr.manage_unit_id = ? ");
          paramList.add(userDept);
        } else {
          sql.append(" and 1=2");//Nhan vien khong co quyen tac dong den CR cho` dong
        }
      } else {
        sql.append(" and cr.state = ? ");
        paramList.add(Constants.CR_STATE.RESOLVE);
        sql.append(" and (cr.change_responsible_unit in ");
        sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
        sql.append(" start with ut.unit_id in ");
        sql.append(" (select ");
        sql.append(" excute_unit_id  from v_manage_cr_config ");
        sql.append(" where ");
        sql.append(" manage_unit = ?");
        sql.append(" and scope_id = ? ");
        sql.append(" AND DEVICE_TYPE  IS NULL and IS_SCHEDULE_CR_EMERGENCY <> 1 )");
        sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
        sql.append(" ) or (cr.change_responsible_unit in ( SELECT ut.unit_id "
            + "  FROM common_gnoc.unit ut "
            + "    START WITH ut.unit_id IN "
            + "    (SELECT excute_unit_id "
            + "    FROM OPEN_PM.v_manage_cr_config "
            + "    WHERE manage_unit = ? "
            + "    AND scope_id      = ? "
            + "    AND DEVICE_TYPE IS NOT NULL and IS_SCHEDULE_CR_EMERGENCY <> 1 "
            + "    ) "
            + "    CONNECT BY PRIOR unit_id = parent_unit_id "
            + "   ) and cr.DEVICE_TYPE in (SELECT DEVICE_TYPE "
            + "    FROM OPEN_PM.v_manage_cr_config "
            + "    WHERE manage_unit = ?  AND scope_id = ?  AND DEVICE_TYPE IS NOT NULL and IS_SCHEDULE_CR_EMERGENCY <> 1 ) ))");

        sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = ?)");
        paramList.add(userDept);
        paramList.add(Long.valueOf(crDTO.getScopeId()));
        paramList.add(userDept);
        paramList.add(Long.valueOf(crDTO.getScopeId()));
        paramList.add(userDept);
        paramList.add(Long.valueOf(crDTO.getScopeId()));
        paramList.add(userDept);
        sql.append(" and (cr.manage_unit_id = ? ");
        sql.append(" OR (cr.manage_unit_id is null))");
        paramList.add(userDept);
      }
    }

    if (crDTO.getCrNumber() != null
        && !"".equals(crDTO.getCrNumber().trim())) {
      String[] lstCrNumber = crDTO.getCrNumber().split(",");
      if (lstCrNumber.length > 1) {
        sql.append(" and cr.cr_number in( ");
        for (String str : lstCrNumber) {
          sql.append("?,");
          paramList.add(str);
        }
        sql.replace(sql.length() - 1, sql.length(), "");
        sql.append(")");
      } else {
        sql.append(" and cr.cr_number like ? ");

        paramList.add(
            "%" + StringUtils.replaceSpecicalChracterSQL(crDTO.getCrNumber()).toUpperCase() + "%");
        sql.append(" ESCAPE '\\' ");
      }
    }

    if (crDTO.getTitle() != null
        && !"".equals(crDTO.getTitle().trim())) {
      sql.append(" and lower(cr.title) like ? ");
      paramList
          .add("%" + StringUtils.replaceSpecicalChracterSQL(crDTO.getTitle()).toLowerCase() + "%");
      sql.append(" ESCAPE '\\' ");
    }

    if (crDTO.getEarliestStartTime() != null
        && !"".equals(crDTO.getEarliestStartTime().trim())) {
      sql.append(" and cr.earliest_start_time >= ? ");
//            paramList.add(dtu.convertStringToDateTime(crDTO.getEarliestStartTime().trim()));
      paramList.add(DateTimeUtils.convertStringToDateTime(
          DateTimeUtils.converClientDateToServerDate(crDTO.getEarliestStartTime().trim(), offset)));
    }

    if (crDTO.getEarliestStartTimeTo() != null
        && !"".equals(crDTO.getEarliestStartTimeTo().trim())) {
      sql.append(" and cr.earliest_start_time <= ? ");
//            paramList.add(dtu.convertStringToDateTime(crDTO.getEarliestStartTimeTo().trim()));
      paramList.add(DateTimeUtils.convertStringToDateTime(DateTimeUtils
          .converClientDateToServerDate(crDTO.getEarliestStartTimeTo().trim(), offset)));
    }
    if (crDTO.getLatestStartTime() != null
        && !"".equals(crDTO.getLatestStartTime().trim())) {
      sql.append(" and cr.latest_start_time >= ? ");
//            paramList.add(dtu.convertStringToDateTime(crDTO.getLatestStartTime().trim()));
      paramList.add(DateTimeUtils.convertStringToDateTime(
          DateTimeUtils.converClientDateToServerDate(crDTO.getLatestStartTime().trim(), offset)));
    }
    if (crDTO.getLatestStartTimeTo() != null
        && !"".equals(crDTO.getLatestStartTimeTo().trim())) {
      sql.append(" and cr.latest_start_time <= ? ");
//            paramList.add(dtu.convertStringToDateTime(crDTO.getLatestStartTimeTo().trim()));
      paramList.add(DateTimeUtils.convertStringToDateTime(
          DateTimeUtils.converClientDateToServerDate(crDTO.getLatestStartTimeTo().trim(), offset)));
    }
    if (crDTO.getIsOutOfDate() != null
        && !"".equals(crDTO.getIsOutOfDate().trim())) {
      if (Constants.CR_OUTDATE_TYPE.OUTOFDATE.equalsIgnoreCase(crDTO.getIsOutOfDate())) {
        sql.append(" and cr.LATEST_START_TIME < sysdate ");
      } else if (Constants.CR_OUTDATE_TYPE.ONTIME.equalsIgnoreCase(crDTO.getIsOutOfDate())) {
        sql.append(" and cr.LATEST_START_TIME >= sysdate ");
      }
    }
    if (crDTO.getCrType() != null
        && !"".equals(crDTO.getCrType().trim())
        && !"-1".equals(crDTO.getCrType().trim())) {
      sql.append(" and cr.cr_type = ? ");
      paramList.add(Long.parseLong(crDTO.getCrType().trim()));
    }
    if (crDTO.getSubcategory() != null
        && !"".equals(crDTO.getSubcategory().trim())
        && !"-1".equals(crDTO.getSubcategory().trim())) {
      sql.append(" and cr.Subcategory = ? ");
      paramList.add(Long.parseLong(crDTO.getSubcategory().trim()));
    }
    if (crDTO.getRisk() != null
        && !"".equals(crDTO.getRisk().trim())
        && !"-1".equals(crDTO.getRisk().trim())) {
      sql.append(" and cr.risk = ? ");
      paramList.add(Long.parseLong(crDTO.getRisk().trim()));
    }
    if (crDTO.getImpactSegment() != null
        && !"".equals(crDTO.getImpactSegment().trim())
        && !"-1".equals(crDTO.getImpactSegment().trim())) {
      sql.append(" and cr.impact_segment = ? ");
      paramList.add(Long.parseLong(crDTO.getImpactSegment().trim()));
    }

    if (crDTO.getCountry() != null
        && !"".equals(crDTO.getCountry().trim())
        && !"-1".equals(crDTO.getCountry().trim())) {
      if ("4001000000".equals(crDTO.getCountry()) || "5000289722".equals(crDTO.getCountry())
          || "1000014581".equals(crDTO.getCountry())
          || "3500289726".equals(crDTO.getCountry()) || "4500000001".equals(crDTO.getCountry())
          || "6000289723".equals(crDTO.getCountry())
          || "3000289724".equals(crDTO.getCountry()) || "2000289729".equals(crDTO.getCountry())
          || "1500289728".equals(crDTO.getCountry())) {
        sql.append(" and cr.country in( ?, ? )");
        paramList.add(Long.parseLong(crDTO.getCountry().trim()));
        if ("1500289728".equals(crDTO.getCountry())) {//timor
          paramList.add(289728L);
        } else if ("2000289729".equals(crDTO.getCountry())) {//timor
          paramList.add(289729L);
        } else if ("3000289724".equals(crDTO.getCountry())) {//mozam
          paramList.add(289724L);
        } else if ("6000289723".equals(crDTO.getCountry())) {//timor
          paramList.add(289723L);
        } else if ("4500000001".equals(crDTO.getCountry())) {//myanmar
          paramList.add(300656L);
        } else if ("4001000000".equals(crDTO.getCountry())) {//tanz
          paramList.add(289727L);
        } else if ("5000289722".equals(crDTO.getCountry())) {//lao
          paramList.add(289722L);
        } else if ("1000014581".equals(crDTO.getCountry())) {//cam
          paramList.add(289721L);
        } else if ("3500289726".equals(crDTO.getCountry())) {//burundi
          paramList.add(289726L);
        }
      } else {
        sql.append(" and cr.country = ? ");
        paramList.add(Long.parseLong(crDTO.getCountry().trim()));
      }
    }
    if (crDTO.getChangeOrginator() != null
        && !"".equals(crDTO.getChangeOrginator().trim())
        && !"-1".equals(crDTO.getChangeOrginator().trim())) {
      sql.append(" and cr.Change_Orginator = ? ");
      paramList.add(Long.parseLong(crDTO.getChangeOrginator().trim()));
    }
    if (crDTO.getChangeResponsible() != null
        && !"".equals(crDTO.getChangeResponsible().trim())
        && !"-1".equals(crDTO.getChangeResponsible().trim())) {
      sql.append(" and cr.Change_Responsible = ? ");
      paramList.add(Long.parseLong(crDTO.getChangeResponsible().trim()));
    }
    if (crDTO.getChangeOrginatorUnit() != null
        && !"".equals(crDTO.getChangeOrginatorUnit().trim())
        && !"-1".equals(crDTO.getChangeOrginatorUnit().trim())) {
      if (crDTO.getSubDeptOri() != null && "1".equals(crDTO.getSubDeptOri().trim())) {
        sql.append(" and cr.Change_Orginator_Unit in ( ");
        sql.append(" SELECT unit_id");
        sql.append(" FROM common_gnoc.unit ");
        sql.append(" START WITH  unit_id = ? ");
        sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
        sql.append(" )");
        paramList.add(Long.parseLong(crDTO.getChangeOrginatorUnit().trim()));
      } else {
        sql.append(" and cr.Change_Orginator_Unit = ? ");
        paramList.add(Long.parseLong(crDTO.getChangeOrginatorUnit().trim()));
      }
    }
    if (crDTO.getChangeResponsibleUnit() != null
        && !"".equals(crDTO.getChangeResponsibleUnit().trim())
        && !"-1".equals(crDTO.getChangeResponsibleUnit().trim())) {
      if (crDTO.getSubDeptResp() != null && "1".equals(crDTO.getSubDeptResp().trim())) {
        sql.append(" and cr.Change_Responsible_Unit in ( ");
        sql.append(" SELECT unit_id");
        sql.append(" FROM common_gnoc.unit ");
        sql.append(" START WITH  unit_id = ? ");
        sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
        sql.append(" )");
        paramList.add(Long.parseLong(crDTO.getChangeResponsibleUnit().trim()));
      } else {
        sql.append(" and cr.Change_Responsible_Unit = ? ");
        paramList.add(Long.parseLong(crDTO.getChangeResponsibleUnit().trim()));
      }
    }
    BaseDto baseDto = new BaseDto();
    Map<String, Object> params = new HashMap<>();
    params.put("paramList", paramList);
    baseDto.setSqlQuery(sql.toString());
    baseDto.setParameters(params);
    return baseDto;
  }

  //tim tong ban ghi
  public long countCRBySearchType(CrInsiteDTO crDTO) {
    try {
      if (crDTO != null) {
        List<List<Long>> searchCrIds = crDTO.getSearchCrIds();
        String userIdStr = crDTO.getUserLogin();
        String userIdDeptStr = crDTO.getUserLoginUnit();//R_xxx uyquyen
        Long userId = null;
        Long userDept = null;
        if (userIdStr != null) {
          userId = Long.valueOf(userIdStr);
        }
        if (userIdDeptStr != null) {
          userDept = Long.valueOf(userIdDeptStr);
        }
        if (userId != null && userDept != null && crDTO.getSearchType() != null) {
          boolean isManager = userRepository.isManagerOfUnits(userId);
          String sql = SQLBuilder
              .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "on-search-cr-by-type");
          BaseDto baseDto = genConditionSearchCr(crDTO, userId, userDept, isManager);
          Map<String, Object> params = baseDto.getParameters();
          sql += baseDto.getSqlQuery();
          if (searchCrIds != null && searchCrIds.size() > 0) {
            sql += " and ( cr.cr_id in (:searchCrIds0) ";
            params.put("searchCrIds0", searchCrIds.get(0));
            for (int i = 1; i < searchCrIds.size(); i++) {
              sql += " or cr.cr_id in (:searchCrIds" + i + ") ";
              params.put("searchCrIds" + i, searchCrIds.get(i));
            }
            sql += " ) ";
          }
          sql += " order by cr.UPDATE_TIME desc ";
          String sqlFinal = "select count(crId) as totalRow from ( " + sql + ")";
          List<BaseDto> data = getNamedParameterJdbcTemplate()
              .query(sqlFinal, params, BeanPropertyRowMapper.newInstance(BaseDto.class));
          if (data != null && data.size() > 0) {
            return data.get(0).getTotalRow().longValue();
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return 0;
  }

  public List<ResultDTO> actionGetProvinceMonitoringParamFix(String userId, String unitId,
      String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> list = new ArrayList<>();
    try {
      List<ResultDTO> listApprove = getListApprove(userId, unitId, searchChild, startDate, endDate);
      List<ResultDTO> listVerify = getListVerify(userId, unitId, searchChild, startDate, endDate);
      List<ResultDTO> listConsider = getListConsider(userId, unitId, searchChild, startDate,
          endDate);
      List<ResultDTO> listSchedule = getListSchedule(userId, unitId, searchChild, startDate,
          endDate);
      List<ResultDTO> listReceive = getListReceive(userId, unitId, searchChild, startDate, endDate);
      List<ResultDTO> listResolve = getListResolve(userId, unitId, searchChild, startDate, endDate);
      List<ResultDTO> listClose = getListClose(userId, unitId, searchChild, startDate, endDate);
      List<ResultDTO> lisCab = getListCab(userId, unitId, searchChild, startDate, endDate);
      List<ResultDTO> lisWaitCab = getListWaitCab(userId, unitId, searchChild, startDate, endDate);

      list.addAll(listApprove);
      list.addAll(listVerify);
      list.addAll(listConsider);
      list.addAll(listSchedule);
      list.addAll(listReceive);
      list.addAll(listResolve);
      list.addAll(listClose);
      list.addAll(lisCab);
      list.addAll(lisWaitCab);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public List<ResultDTO> getListApprove(String userId, String unit, String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> list = new ArrayList<>();
    try {
      String total = Constants.NOTIFY_TYPE.TOTAL_APPROVE;
      String totalEmg = Constants.NOTIFY_TYPE.TOTAL_APPROVE_EMG;
      String totalStandard = Constants.NOTIFY_TYPE.TOTAL_APPROVE_STANDARD;

      Long countTotal;
      Long countTotalEmg;
      Long countTotalStandard;

      CrDTO crDTO = getSearchData(userId, unit, searchChild, startDate, endDate,
          Constants.CR_SEARCH_TYPE.APPROVE.toString());
      List<CrDTO> listApproveOnTime = crMobileRepository.getListCRBySearchTypeCount(crDTO);

      countTotal = getCount(listApproveOnTime, Constants.CR_TYPE.NORMAL);
      countTotalEmg = getCount(listApproveOnTime, Constants.CR_TYPE.EMERGENCY);
      countTotalStandard = getCount(listApproveOnTime, Constants.CR_TYPE.STANDARD);

      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setId(total);
      resultDTO.setQuantitySucc(countTotal == null ? 0 : countTotal.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.APPROVE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalEmg);
      resultDTO.setQuantitySucc(countTotalEmg == null ? 0 : countTotalEmg.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.APPROVE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalStandard);
      resultDTO.setQuantitySucc(countTotalStandard == null ? 0 : countTotalStandard.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.APPROVE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public List<ResultDTO> getListVerify(String userId, String unit, String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> list = new ArrayList<>();
    try {
      String total = Constants.NOTIFY_TYPE.TOTAL_VERIFY;
      String totalEmg = Constants.NOTIFY_TYPE.TOTAL_VERIFY_EMG;
      String totalStandard = Constants.NOTIFY_TYPE.TOTAL_VERIFY_STANDARD;
      Long countTotal;
      Long countTotalEmg;
      Long countTotalStandard;
      CrDTO crDTO = getSearchData(userId, unit, searchChild, startDate, endDate,
          Constants.CR_SEARCH_TYPE.VERIFY.toString());
      List<CrDTO> listVerifyOnTime = crMobileRepository.getListCRBySearchTypeCount(crDTO);

      countTotal = getCount(listVerifyOnTime, Constants.CR_TYPE.NORMAL);
      countTotalEmg = getCount(listVerifyOnTime, Constants.CR_TYPE.EMERGENCY);
      countTotalStandard = getCount(listVerifyOnTime, Constants.CR_TYPE.STANDARD);
      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setId(total);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.VERIFY.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      resultDTO.setQuantitySucc(countTotal == null ? 0 : countTotal.intValue());
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalEmg);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.VERIFY.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      resultDTO.setQuantitySucc(countTotalEmg == null ? 0 : countTotalEmg.intValue());
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalStandard);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.VERIFY.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      resultDTO.setQuantitySucc(countTotalStandard == null ? 0 : countTotalStandard.intValue());
      list.add(resultDTO);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public List<ResultDTO> getListConsider(String userId, String unit, String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> list = new ArrayList<>();
    try {

      String total = Constants.NOTIFY_TYPE.TOTAL_CONSIDER;
      String totalEmg = Constants.NOTIFY_TYPE.TOTAL_CONSIDER_EMG;
      String totalStandard = Constants.NOTIFY_TYPE.TOTAL_CONSIDER_STANDARD;

      Long countTotal;
      Long countTotalEmg;
      Long countTotalStandard;

      CrDTO crDTO = getSearchData(userId, unit, searchChild, startDate, endDate,
          Constants.CR_SEARCH_TYPE.CONSIDER.toString());
      List<CrDTO> listConsiderOnTime = crMobileRepository.getListCRBySearchTypeCount(crDTO);
//
      countTotal = getCount(listConsiderOnTime, Constants.CR_TYPE.NORMAL);
      countTotalEmg = getCount(listConsiderOnTime, Constants.CR_TYPE.EMERGENCY);
      countTotalStandard = getCount(listConsiderOnTime, Constants.CR_TYPE.STANDARD);
      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setId(total);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.CONSIDER.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      resultDTO.setQuantitySucc(countTotal == null ? 0 : countTotal.intValue());
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalEmg);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.CONSIDER.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      resultDTO.setQuantitySucc(countTotalEmg == null ? 0 : countTotalEmg.intValue());
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalStandard);
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.CONSIDER.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      resultDTO.setQuantitySucc(countTotalStandard == null ? 0 : countTotalStandard.intValue());
      list.add(resultDTO);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public List<ResultDTO> getListSchedule(String userId, String unit, String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> list = new ArrayList<>();
    try {
      String total = Constants.NOTIFY_TYPE.TOTAL_SCHEDULE;
      String totalEmg = Constants.NOTIFY_TYPE.TOTAL_SCHEDULE_EMG;
      String totalStandard = Constants.NOTIFY_TYPE.TOTAL_SCHEDULE_STANDARD;
      Long countTotal;
      Long countTotalEmg;
      Long countTotalStandard;

      CrDTO crDTO = getSearchData(userId, unit, searchChild, startDate, endDate,
          Constants.CR_SEARCH_TYPE.SCHEDULE.toString());
      List<CrDTO> listScheduleOnTime = crMobileRepository.getListCRBySearchTypeCount(crDTO);
      countTotal = getCount(listScheduleOnTime, Constants.CR_TYPE.NORMAL);
      countTotalEmg = getCount(listScheduleOnTime, Constants.CR_TYPE.EMERGENCY);
      countTotalStandard = getCount(listScheduleOnTime, Constants.CR_TYPE.STANDARD);
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

  public List<ResultDTO> getListResolve(String userId, String unit, String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> list = new ArrayList<>();
    try {

      String total = Constants.NOTIFY_TYPE.TOTAL_RESOLVE;
      String totalEmg = Constants.NOTIFY_TYPE.TOTAL_RESOLVE_EMG;
      String totalStandard = Constants.NOTIFY_TYPE.TOTAL_RESOLVE_STANDARD;
      Long countTotalNormal;
      Long countTotalEmg;
      Long countTotalStandard;

      CrDTO crDTO = getSearchData(userId, unit, searchChild, startDate, endDate,
          Constants.CR_SEARCH_TYPE.RESOLVE.toString());
      List<CrDTO> listResolveOnTime = crMobileRepository.getListCRBySearchTypeCount(crDTO);
      countTotalNormal = getCount(listResolveOnTime, Constants.CR_TYPE.NORMAL);
      countTotalEmg = getCount(listResolveOnTime, Constants.CR_TYPE.EMERGENCY);
      countTotalStandard = getCount(listResolveOnTime, Constants.CR_TYPE.STANDARD);
      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setId(total);
      resultDTO.setQuantitySucc(countTotalNormal.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.RESOLVE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalEmg);
      resultDTO.setQuantitySucc(countTotalEmg.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.RESOLVE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalStandard);
      resultDTO.setQuantitySucc(countTotalStandard.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.RESOLVE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public List<ResultDTO> getListClose(String userId, String unit, String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> list = new ArrayList<>();
    try {

      String total = Constants.NOTIFY_TYPE.TOTAL_CLOSE;
      String totalEmg = Constants.NOTIFY_TYPE.TOTAL_CLOSE_EMG;
      String totalStandard = Constants.NOTIFY_TYPE.TOTAL_CLOSE_STANDARD;

      Long countTotalNormal;
      Long countTotalEmg;
      Long countTotalStandard;

      CrDTO crDTO = getSearchData(userId, unit, searchChild, startDate, endDate,
          Constants.CR_SEARCH_TYPE.CLOSE.toString());
      List<CrDTO> listCloseOnTime = crMobileRepository.getListCRBySearchTypeCount(crDTO);

      countTotalNormal = getCount(listCloseOnTime, Constants.CR_TYPE.NORMAL);
      countTotalEmg = getCount(listCloseOnTime, Constants.CR_TYPE.EMERGENCY);
      countTotalStandard = getCount(listCloseOnTime, Constants.CR_TYPE.STANDARD);

      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setId(total);
      resultDTO.setQuantitySucc(countTotalNormal.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.CLOSE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalEmg);
      resultDTO.setQuantitySucc(countTotalEmg.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.CLOSE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalStandard);
      resultDTO.setQuantitySucc(countTotalStandard.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.CLOSE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public List<ResultDTO> getListReceive(String userId, String unit, String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> list = new ArrayList<ResultDTO>();
    try {

      String total = Constants.NOTIFY_TYPE.TOTAL_RECEIVE;
      String totalEmg = Constants.NOTIFY_TYPE.TOTAL_RECEIVE_EMG;
      String totalStandard = Constants.NOTIFY_TYPE.TOTAL_RECEIVE_STANDARD;

      Long countTotalNormal;
      Long countTotalEmg;
      Long countTotalStandard;

      CrDTO crDTO = getSearchData(userId, unit, searchChild, startDate, endDate,
          Constants.CR_SEARCH_TYPE.EXCUTE.toString());
      List<CrDTO> listReceiveOnTime = crMobileRepository.getListCRBySearchTypeCount(crDTO);
      countTotalNormal = getCount(listReceiveOnTime, Constants.CR_TYPE.NORMAL);
      countTotalEmg = getCount(listReceiveOnTime, Constants.CR_TYPE.EMERGENCY);
      countTotalStandard = getCount(listReceiveOnTime, Constants.CR_TYPE.STANDARD);

      ResultDTO resultDTO;
      resultDTO = new ResultDTO();
      resultDTO.setId(total);
      resultDTO.setQuantitySucc(countTotalNormal.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.EXCUTE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalEmg);
      resultDTO.setQuantitySucc(countTotalEmg.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.EXCUTE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalStandard);
      resultDTO.setQuantitySucc(countTotalStandard.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.EXCUTE.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public List<ResultDTO> getListCab(String userId, String unit, String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> list = new ArrayList<>();
    try {

      String total = Constants.NOTIFY_TYPE.TOTAL_CAB;
      String totalEmg = Constants.NOTIFY_TYPE.TOTAL_CAB_EMG;
      String totalStandard = Constants.NOTIFY_TYPE.TOTAL_CAB_STANDARD;

      Long countTotal;
      Long countTotalEmg;
      Long countTotalStandard;

      CrDTO crDTO = getSearchData(userId, unit, searchChild, startDate, endDate,
          Constants.CR_SEARCH_TYPE.CAB.toString());
      List<CrDTO> listCabOnTime = crMobileRepository.getListCRBySearchTypeCount(crDTO);

      countTotal = getCount(listCabOnTime, Constants.CR_TYPE.NORMAL);
      countTotalEmg = getCount(listCabOnTime, Constants.CR_TYPE.EMERGENCY);
      countTotalStandard = getCount(listCabOnTime, Constants.CR_TYPE.STANDARD);
      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setId(total);
      resultDTO.setQuantitySucc(countTotal == null ? 0 : countTotal.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.CAB.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalEmg);
      resultDTO.setQuantitySucc(countTotalEmg == null ? 0 : countTotalEmg.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.CAB.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalStandard);
      resultDTO.setQuantitySucc(countTotalStandard == null ? 0 : countTotalStandard.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.CAB.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public List<ResultDTO> getListWaitCab(String userId, String unit, String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> list = new ArrayList<>();
    try {

      String total = Constants.NOTIFY_TYPE.TOTAL_WAIT_CAB;
      String totalEmg = Constants.NOTIFY_TYPE.TOTAL_WAIT_CAB_EMG;
      String totalStandard = Constants.NOTIFY_TYPE.TOTAL_WAIT_CAB_STANDARD;

      Long countTotal;
      Long countTotalEmg;
      Long countTotalStandard;

      CrDTO crDTO = getSearchData(userId, unit, searchChild, startDate, endDate,
          Constants.CR_SEARCH_TYPE.WAIT_CAB.toString());
      List<CrDTO> listCabOnTime = crMobileRepository.getListCRBySearchTypeCount(crDTO);

      countTotal = getCount(listCabOnTime, Constants.CR_TYPE.NORMAL);
      countTotalEmg = getCount(listCabOnTime, Constants.CR_TYPE.EMERGENCY);
      countTotalStandard = getCount(listCabOnTime, Constants.CR_TYPE.STANDARD);
      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setId(total);
      resultDTO.setQuantitySucc(countTotal == null ? 0 : countTotal.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.WAIT_CAB.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalEmg);
      resultDTO.setQuantitySucc(countTotalEmg == null ? 0 : countTotalEmg.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.WAIT_CAB.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

      resultDTO = new ResultDTO();
      resultDTO.setId(totalStandard);
      resultDTO.setQuantitySucc(countTotalStandard == null ? 0 : countTotalStandard.intValue());
      resultDTO.setKey(Constants.CR_SEARCH_TYPE.WAIT_CAB.toString());
      resultDTO.setMessage(Constants.CR_OUTDATE_TYPE.TOTAL);
      list.add(resultDTO);

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  private CrDTO getSearchData(String userId, String unit, String searchChild, String startDate,
      String endDate, String searchType) {
    CrDTO crDTO = new CrDTO();
    if (StringUtils.isNotNullOrEmpty(userId)) {
      crDTO.setUserLogin(userId);
    }
    if (StringUtils.isNotNullOrEmpty(unit)) {
      crDTO.setUserLoginUnit(unit);
    }
    if (StringUtils.isNotNullOrEmpty(searchChild)) {
      if ("yes".equals(searchChild)) {
        crDTO.setIsSearchChildDeptToApprove("1");
      }
    }
    if (StringUtils.isNotNullOrEmpty(searchType)) {
      crDTO.setSearchType(searchType);
    }
    if (StringUtils.isNotNullOrEmpty(startDate)) {
      crDTO.setEarliestStartTime(startDate);
    }
    if (StringUtils.isNotNullOrEmpty(endDate)) {
      crDTO.setEarliestStartTimeTo(endDate);
    }
    return crDTO;
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

  @Override
  public List<CrDTO> getCrByIdAndResolveStatuṣ̣(List<Long> crIds, Long resolveStatus) {

    List<CrDTO> list = null;
    try {
      String sql = "select CR_ID as crId , CR_NUMBER as crNumber  from cr where cr_id in (:crIds) and RESOLVE_RESTURN_CODE = :resolveStatus ";
      Map<String, Object> params = new HashMap<>();
      params.put("crIds", crIds);
      params.put("resolveStatus", resolveStatus);
      list = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrDTO.class));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return list;
  }

  @Override
  public List<CrDTO> getListCrInfo(String crNumber, Date startCreatedDate, Date endCreatedDate) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-List-Cr-Info");
    Map<String, Object> params = new HashMap<>();
    params.put("crNumber", StringUtils.convertLowerParamContains(crNumber));
    if (startCreatedDate != null) {
      sql += " and cr.CREATED_DATE >= :startCreatedDate";
      params.put("startCreatedDate", startCreatedDate);
    }
    if (endCreatedDate != null) {
      sql += " and cr.CREATED_DATE <= :endCreatedDate";
      params.put("endCreatedDate", endCreatedDate);
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(CrDTO.class));
  }

  public Long stringToLong(String txt) {
    try {
      if (txt == null || txt.trim().isEmpty()) {
        return null;
      }
      return Long.parseLong(txt.trim());
    } catch (NumberFormatException e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public List<CrDTO> getListCRForExportServiceV2(CrInsiteDTO crDTO, String lstCrId,
      Date earliestCrCreatedTime, Date earliestCrStartTime, Date lastestCrStartTime,
      Date latestCrUpdateTime, int start, int end, String locale) {
    try {

      if (lastestCrStartTime == null) {
        lastestCrStartTime = new Date();
      }

      if (latestCrUpdateTime == null || latestCrUpdateTime.compareTo(lastestCrStartTime) < 0) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastestCrStartTime);
        cal.add(Calendar.DATE, 3);
        latestCrUpdateTime = cal.getTime();
      }

      if (crDTO != null) {
        String userIdStr = crDTO.getUserLogin();
        String userIdDeptStr = crDTO.getUserLoginUnit();//R_xxx uyquyen
        Long userId = null;
        Long userDept = null;
        if (userIdStr != null) {
          userId = Long.valueOf(userIdStr);
        }
        if (userIdDeptStr != null) {
          userDept = Long.valueOf(userIdDeptStr);
        }
        if (userId != null && userDept != null && crDTO.getSearchType() != null) {
          boolean isManager = userRepository.isManagerOfUnits(userId);
          List<CrDTO> list;
          StringBuffer sql = new StringBuffer();
          String language = I18n.getLocale();
          if (!StringUtils.isStringNullOrEmpty(locale)) {
            language = locale;
          }
          sql.append("select cr.title title, cr.cr_id crId,  "
              + " cr.cr_number crNumber, "
              + " TO_CHAR((cr.update_time + :offset * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss') updateTime, "
              + " TO_CHAR((cr.earliest_start_time  + :offset * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss') earliestStartTime, "
              + " TO_CHAR((cr.latest_start_time + :offset * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss') latestStartTime, "
              + " TO_CHAR((cr.created_date + :offset * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss') createdDate, "
              /*longlt6 thay doi ngay 2017-05-20 fix bug hien thi thoi gian theo locate - START */
              + " TO_CHAR((cr.DISTURBANCE_START_TIME + :offset  * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss') disturbanceStartTime,  "
              + " TO_CHAR((cr.DISTURBANCE_END_TIME + :offset  * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss') disturbanceEndTime, "
              + " ((select max(chs.change_date) from open_pm.cr_his chs where cr.cr_id = chs.cr_id  "
              + " and chs.change_date >= cr.created_date and chs.action_code = 24) "
              + " + :offset * interval '1' hour) sentDate, "
              /*longlt6 thay doi ngay 2017-05-20 fix bug hien thi thoi gian theo locate -END  */
              + " cr.priority priority, "
              + " case when utOri.unit_code is null then '' "
              + " else TO_CHAR(utOri.unit_code || ' ('||utOri.unit_name||')') end as changeOrginatorUnitName, "
              + " case when usOri.username is null then '' "
              + " else TO_CHAR(usOri.username || ' ('||usOri.fullname||')') end as changeOrginatorName, "
              + " case when utResp.unit_code is null then '' "
              + " else TO_CHAR(utResp.unit_code || ' ('||utResp.unit_name||')') end as changeResponsibleUnitName, "
              + " case when usResp.username is null then '' "
              + " else TO_CHAR(usResp.username || ' ('||usResp.fullname||')') end as changeResponsibleName, "
              + " case when utConsi.unit_code is null then '' "
              + " else TO_CHAR(utConsi.unit_code || ' ('||utConsi.unit_name||')') end as considerUnitName, "
              + " case when usConsi.username is null then '' "
              + " else TO_CHAR(usConsi.username || ' ('||usConsi.fullname||')') end as considerUserName, "
              + " cr.state, "
              + " cr.cr_type_cat crTypeCat, "
              + " cr.relate_to_pre_approved_cr relateToPreApprovedCr, "
              + " (case cr.cr_type  "
              + " when 0 then 'CR Normal' "
              + " when 1 then 'CR Emergency' "
              + " when 2 then 'CR Standard' "
              + " else TO_CHAR(cr.cr_type) end) crType, "
              + " cr.change_responsible as changeResponsible, "
              + " cr.change_responsible_unit as changeResponsibleUnit, "
              + " cr.change_orginator as changeOrginator, "
              + " cr.change_orginator_unit as changeOrginatorUnit, "
              + " cr.consider_unit_id as considerUnitId, "
              + " cr.consider_user_id as considerUserId, "
              + " cr.process_type_id as crProcessId, "
              + " (case cr.process_type_id ||'@'||cr.cr_type "
              + " when '0@" + Constants.CR_TYPE.STANDARD + "' then '" + I18n
              .getChangeManagement("cr.specialProcessStandard") + "' "
              + " when '0@" + Constants.CR_TYPE.NORMAL + "' then '" + I18n
              .getChangeManagement("cr.specialProcess") + "' "
              + " when '0@" + Constants.CR_TYPE.EMERGENCY + "' then '" + I18n
              .getChangeManagement("cr.specialProcess") + "' "
              + " when '0@' then '" + I18n.getChangeManagement("cr.specialProcess") + "' "
              + " else TO_CHAR(replace(replace(cps.cr_process_name,CHR(10),'_'), ',', '_')) end) crProcessName, "
              + " sy.subcategory_id subcategoryId, "
              + " sy.sy_name subcategory, "
              + " dts.device_type_id deviceTypeId, "
              + " REPLACE(dts.device_type_name, '/',', ') deviceType, "
              + " ist.impact_segment_id impactSegmentId, "
              + " ist.impact_segment_name impactSegment, "
              + " cr.risk risk, "
              + " (case when service_affecting = 1 then '" + I18n.getChangeManagement("common.yes")
              + "' "
              + " else '" + I18n.getChangeManagement("common.no") + "' "
              + " end) affectedServiceList, "
              + " cr.description description, "
              + " (select us.username || ' - ' || TO_CHAR(wlg.created_date, 'dd/MM/yyyy HH24:mi:ss') || ' - ' || wlg.wlg_text  "
              + " from open_pm.work_log wlg "
              + " left join open_pm.user_group_category ugcy on wlg.user_group_action = ugcy.ugcy_id "
              + " left join common_gnoc.users us on us.user_id = wlg.user_id "
              + " where wlg.wlg_object_type=2 and ugcy_code = 'CR_CAB' "
              + " and wlg.wlg_object_id = cr.cr_id "
              + " and rownum<2 "
              + " ) commentCAB , "
              + " (select wlg_text from open_pm.v_worklog_content vwct "
              + " where ugcy_code = 'CR_QLTD' "
              + " and vwct.wlg_object_id = cr.cr_id "
              + " and rownum < 2 "
              + " ) commentQLTD , "
              + " (select wlg_text from open_pm.v_worklog_content vwct "
              + " where ugcy_code = 'CR_Z78' "
              + " and vwct.wlg_object_id = cr.cr_id "
              + " and rownum < 2 "
              + " ) commentZ78, "
              + " (select wlg_text from open_pm.v_worklog_content vwct "
              + " where ugcy_code = 'CR_CREATOR' "
              + " and vwct.wlg_object_id = cr.cr_id "
              + " and rownum < 2 "
              + " ) commentCreator, "
              + " '(' || (case "
              + " ccfoss.system_id "
              + " when 1 then 'MR' "
              + " when 2 then 'PT' "
              + " when 3 then 'TT' "
              + " when 4 then 'WO' "
              + " when 5 then 'SR' "
              + " when 6 then 'RDM' "
              + " when 7 then 'RR' "
              + " else '' end) || ') ' || (case "
              + " ccfoss.system_id "
              + " when 1 then mr.mr_code "
              + " when 2 then TO_NCHAR(ps.problem_code) "
              + " when 3 then TO_NCHAR(ts.trouble_code) "
              + " when 4 then TO_NCHAR(wo.wo_code) "
              + " else TO_NCHAR('') end)  "
              + " as relateCr, "
              + " case "
              //                            + " cps.impact_type "
              + " cr.duty_type "
              + " when 2 then '" + I18n.getChangeManagement("common.day") + "' "
              + " else '" + I18n.getChangeManagement("common.night") + "' "
              + " end dutyType, "
              + " case "
              + " cps.impact_characteristic "
              + " when 1 then 'Logic' "
              + " else '" + I18n.getChangeManagement("cr.physic") + "' "
              + " end relatedTt, "
                            /*longlt6 thay doi ngay 2017-05-20 fix bug hien thi thoi gian theo locate - START
                             + " TO_CHAR( "
                             + " ((select max(chs.change_date) from open_pm.cr_his chs where cr.cr_id = chs.cr_id  "
                             + " and chs.change_date >= cr.created_date and chs.action_code = 24) "
                             + " + 0 * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss') sentDate, "
                             longlt6 thay doi ngay 2017-05-20 fix bug hien thi thoi gian theo locate - START */
              //20160622 daitr1 bo sung cot thoi gian qua han
              + " case cr.state "
              + " when :state_close "
              + " then "
              + " case"
              + " when exists (select 1 from "
              + " open_pm.cr_his chs1 "
              + " where cr.cr_id = chs1.cr_id and chs1.action_code in (:action_reject,:act_close,:act_close_by_appr,:act_close_by_man,:act_close_by_emer)) then '' "
              + " else   TO_CHAR(((select max(chs2.change_date) from open_pm.cr_his chs2 where cr.cr_id = chs2.cr_id"
              + " and chs2.change_date >= cr.created_date "
              + " and chs2.action_code in (:act_close_cr,:act_close_cr_appr,:act_close_excu))"
              + " + :offset * interval '1' hour), 'dd/MM/yyyy HH24:mi:ss') end"
              + " when :state_draft then '' "
              + " else to_char(sysdate + :offset * interval '1' hour, 'dd/MM/yyyy HH24:mi:ss')"
              + " end as compareDate,"
              + " vcawlct.worklogcontent as allComment,"
              + " cr.is_primary_cr isPrimaryCr,"
              + " cr.relate_to_primary_cr relateToPrimaryCrNumber,"
              + " cr.relate_to_pre_approved_cr relateToPreApprovedCrNumber,"
              //20160622 daitr1 bo sung cot thoi gian qua han
              + " case when usCab.username is null then ''"
              + " else TO_CHAR(usCab.username || ' ('||usCab.fullname||')') end as userCab"
              //LONGLT6 add 2017-08-22 start
              + " , history.RETURN_TITLE as resolveTitle, history.RETURN_CODE as resolveCodeId "
              + " , history1.RETURN_TITLE as closeTitle,  history1.RETURN_CODE as closeCodeId "
              //LONGLT6 add 2017-08-22 end
              //                            + " , affectService.SERVICE_NAME as affectedService "
              + " , cr.CIRCLE_ADDITIONAL_INFO as circleAdditionalInfo "
              + " , cr.TOTAL_AFFECTED_CUSTOMERS as totalAffectedCustomers "
              + " , cr.TOTAL_AFFECTED_MINUTES as totalAffectedMinutes "
              + " , cr.RANK_GATE rankGate "
              + " , cr.IS_CONFIRM_ACTION isConfirmAction "
              + " , (case cr.AUTO_EXECUTE when 1 then '1' else '0' end) autoExecute "
              + " , (case cr.AUTO_EXECUTE when 1 then (case cr.IS_RUN_TYPE when 0 then '" + I18n
              .getChangeManagement("cr.checkIsRunType.0") + "'"
              + " when 1 then '" + I18n.getChangeManagement("cr.checkIsRunType.1")
              + "' else ' ' end) else ' ' end) isRunType "
              + " from open_pm.cr cr "
              + " left join common_gnoc.unit utOri on cr.change_orginator_unit = utOri.unit_id "
              + " left join common_gnoc.users usOri on cr.change_orginator = usOri.user_id "
              + " left join common_gnoc.unit utResp on cr.change_responsible_unit = utResp.unit_id "
              + " left join common_gnoc.users usResp on cr.change_responsible = usResp.user_id "
              + " left join common_gnoc.unit utConsi on cr.consider_unit_id = utConsi.unit_id "
              + " left join common_gnoc.users usConsi on cr.consider_user_id = usConsi.user_id "
              + " left join open_pm.cr_process cps on cr.process_type_id = cps.cr_process_id "
              + " left join open_pm.subcategory sy on cr.subcategory = sy.subcategory_id "
              + " left join open_pm.device_types dts on cr.device_type = dts.device_type_id "
              + " left join open_pm.impact_segment ist on cr.impact_segment = ist.impact_segment_id "
              + " left join open_pm.cr_created_from_other_sys ccfoss on (ccfoss.cr_id = cr.cr_id and ccfoss.is_active  = 1) "
              + " left join one_tm.problems ps on ps.problem_id = ccfoss.object_id "
              + " left join one_tm.troubles ts on ts.trouble_id = ccfoss.object_id "
              + " left join open_pm.mr mr on mr.mr_id = ccfoss.object_id "
              + " left join wfm.wo wo on wo.wo_id = ccfoss.object_id "
              + " left join common_gnoc.users usCab on cr.user_cab = usCab.user_id"
              //LONGLT6 add 2017-08-22 start
              + "  LEFT JOIN ( "
              + "  select CR_ID , catalog.RETURN_TITLE  , his.RETURN_CODE from open_pm.CR_HIS his "
              + "  left join open_pm.RETURN_CODE_CATALOG catalog on his.RETURN_CODE = catalog.RCCG_ID "
              + "  where his.STATUS = 7 "
              + (StringUtils.isStringNullOrEmpty(crDTO.getEarliestStartTime()) ? ""
              : "  and his.CHANGE_DATE >= :earliestStartTime ")
              + " ) history on cr.CR_ID = history.CR_ID "
              //LONGLT6 add 2017-08-22 end

              + "  LEFT JOIN ( "
              + "  select CR_ID , catalog.RETURN_TITLE , his1.RETURN_CODE from open_pm.CR_HIS his1 "
              + "  left join open_pm.RETURN_CODE_CATALOG catalog on his1.RETURN_CODE = catalog.RCCG_ID "
              + "  where his1.STATUS = 9 "
              + (StringUtils.isStringNullOrEmpty(crDTO.getEarliestStartTime()) ? ""
              : "  and his1.CHANGE_DATE >= :earliestStartTime ")
              + " ) history1 on cr.CR_ID = history1.CR_ID"
              //                            + " LEFT JOIN ("
              //                            + " select tbl.CR_ID, rtrim(xmlagg(XMLELEMENT(e,tbl.SERVICE_NAME,',').EXTRACT('//text()')).GetClobVal(),',') as SERVICE_NAME "
              //                            + " from ( "
              //                            + " select casd.CR_ID, '['||to_char(casd.INSERT_TIME,'dd/MM/yyyy HH24:mi:ss')||'] - '||ass.SERVICE_NAME as SERVICE_NAME  "
              //                            + " from OPEN_PM.AFFECTED_SERVICES ass  inner join OPEN_PM.CR_AFFECTED_SERVICE_DETAILS casd "
              //                            + " on casd.AFFECTED_SERVICE_ID = ass.AFFECTED_SERVICE_ID "
              //                            + " where 1=1 "
              //                            + (earliestCrCreatedTime == null ? "" : "  AND casd.INSERT_TIME >= TO_DATE( '" + dateFormat.format(earliestCrCreatedTime) + "' , 'DD/MM/YYYY HH24:MI:SS' ) ")
              //                            + (latestCrUpdateTime == null ? " " : "  AND casd.INSERT_TIME <=   TO_DATE( '" + dateFormat.format(latestCrUpdateTime) + "' , 'DD/MM/YYYY HH24:MI:SS' )    ")
              //                            + " ) tbl "
              //                            + " group by tbl.cr_id "
              //                            + " ) affectService on cr.CR_ID = affectService.CR_ID "
              //+ " left join v_cr_all_work_log_content vcawlct on vcawlct.cr_id = cr.cr_id "
              + " LEFT JOIN ( "
              + "     select cr_id,rtrim(xmlagg(XMLELEMENT(e,wlgCont,',').EXTRACT('//text()') ORDER BY created_date desc ).GetClobVal(),',') as worklogcontent "
              + "     from ( "
              + "       select cr_id,'['||created_date_String||'] - '||WLAY_NAME||' - ['||username||']'||'['||ugcy_name||'] '||state||': '||wlg_text as wlgCont,created_date "
              + "       from (            "
              + "             select ugcy.ugcy_code,ugcy.ugcy_name,wlg.wlg_object_id as cr_id,wlg.wlg_text,wlg.created_date, "
              + "             to_char(created_date,'dd/MM/yyyy HH24:mi:ss') as created_date_String,wlg.WLG_OBJECT_STATE,us.username, "
              + "             case WLG_OBJECT_STATE "
              + "             when 0 then '- [" + I18n.getLanguage("cr.state.0") + "]' "
              + "             when 1 then '- [" + I18n.getLanguage("cr.state.1") + "]' "
              + "             when 2 then '- [" + I18n.getLanguage("cr.state.2") + "]' "
              + "             when 3 then '- [" + I18n.getLanguage("cr.state.3") + "]' "
              + "             when 4 then '- [" + I18n.getLanguage("cr.state.4") + "]' "
              + "             when 5 then '- [" + I18n.getLanguage("cr.state.5") + "]' "
              + "             when 6 then '- [" + I18n.getLanguage("cr.state.6") + "]' "
              + "             when 7 then '- [" + I18n.getLanguage("cr.state.7") + "]' "
              + "             when 8 then '- [" + I18n.getLanguage("cr.state.8") + "]' "
              + "             when 9 then '- [" + I18n.getLanguage("cr.state.9") + "]' "
              + "             when 10 then '- [" + I18n.getLanguage("cr.state.10") + "]' "
              + "             else '' end as state, "
              + "             wlc.WLAY_NAME "
              + "             from open_pm.work_log wlg "
              + "             left join user_group_category ugcy on wlg.user_group_action = ugcy.ugcy_id "
              + "             left join open_pm.WORK_LOG_CATEGORY wlc on wlg.WLAY_ID = wlc.WLAY_ID "
              + "             left join COMMON_GNOC.users us on us.USER_ID = wlg.USER_ID "
              + "             where wlg.wlg_object_type=2  "
              + (earliestCrCreatedTime == null ? " "
              : "  and wlg.CREATED_DATE >=  TO_DATE( '" + DateTimeUtils
                  .date2ddMMyyyyHHMMss(earliestCrCreatedTime) + "' , 'DD/MM/YYYY HH24:MI:SS' )    ")
              + (latestCrUpdateTime == null ? " "
              : "  and wlg.CREATED_DATE <=   TO_DATE( '" + DateTimeUtils
                  .date2ddMMyyyyHHMMss(latestCrUpdateTime) + "' , 'DD/MM/YYYY HH24:MI:SS' )    ")
              + "           order by wlg.WORK_LOG_ID desc ) "
              + "        ) "
              + "        group by cr_id "
              + " ) vcawlct  on  cr.CR_ID = vcawlct.CR_ID "
              + " where usOri.is_enable = 1");

          BaseDto searchForm = genConditionSearchCrV2(crDTO, userId, userDept, isManager,
              earliestCrCreatedTime, earliestCrStartTime, lastestCrStartTime, latestCrUpdateTime);
          sql.append(searchForm.getSqlQuery());
          sql.append("  and cr.cr_id in(").append(lstCrId).append("-1)");
          sql.append(" order by  cr.relate_to_primary_cr, cr.update_time desc ");

          Map<String, Object> params = searchForm.getParameters();
          if (end == 0) {
            list = getNamedParameterJdbcTemplate()
                .query(sql.toString(), params,
                    BeanPropertyRowMapper.newInstance(CrDTO.class));
          } else {
            params.put("indexEnd", start + end);
            params.put("indextStart", start);
            list = getNamedParameterJdbcTemplate()
                .query(SQLBuilder.getSQLPagination(sql.toString()), params,
                    BeanPropertyRowMapper.newInstance(CrDTO.class));
          }
          list = crDBRepository.processListToGenGrService(list, crDTO, language);
          //tuanpv14_multi languges

          try {
            Map<String, Object> map = getSqlLanguageExchange(
                Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR,
                Constants.APPLIED_BUSSINESS.CR_PROCESS, language);
            String sqlLanguage = (String) map.get("sql");
            Map mapParam = (Map) map.get("mapParam");
            List<LanguageExchangeDTO> lstLanguage = findBySql(sqlLanguage, mapParam);
            list = setLanguage(list, lstLanguage, "crProcessId", "crProcessName");
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
          try {
            Map<String, Object> map = getSqlLanguageExchange(
                Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR,
                Constants.APPLIED_BUSSINESS.DEVICE_TYPES, language);
            String sqlLanguage = (String) map.get("sql");
            Map mapParam = (Map) map.get("mapParam");
            List<LanguageExchangeDTO> lstLanguage = findBySql(sqlLanguage, mapParam);
            list = setLanguage(list, lstLanguage, "crProcessId", "crProcessName");
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
          try {
            Map<String, Object> map = getSqlLanguageExchange(
                Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR,
                Constants.APPLIED_BUSSINESS.IMPACT_SEGMENT, language);
            String sqlLanguage = (String) map.get("sql");
            Map mapParam = (Map) map.get("mapParam");
            List<LanguageExchangeDTO> lstLanguage = findBySql(sqlLanguage, mapParam);
            list = setLanguage(list, lstLanguage, "impactSegmentId", "impactSegment");
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
          try {
            Map<String, Object> map = getSqlLanguageExchange(
                Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR,
                Constants.APPLIED_BUSSINESS.SUBCATEGORY, language);
            String sqlLanguage = (String) map.get("sql");
            Map mapParam = (Map) map.get("mapParam");
            List<LanguageExchangeDTO> lstLanguage = findBySql(sqlLanguage, mapParam);
            list = setLanguage(list, lstLanguage, "subcategoryId", "subcategory");
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }

          try {
            Map<String, Object> map = getSqlLanguageExchange(
                Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR,
                Constants.APPLIED_BUSSINESS.RETURN_CODE_CATALOG, language);
            String sqlLanguage = (String) map.get("sql");
            Map mapParam = (Map) map.get("mapParam");
            List<LanguageExchangeDTO> lstLanguage = findBySql(sqlLanguage, mapParam);

            try {
              list = setLanguage(list, lstLanguage, "resolveCodeId", "resolveTitle");
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
            try {
              list = setLanguage(list, lstLanguage, "closeCodeId", "closeTitle");
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }

          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
          //tuanpv14_multi languges
          return list;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }


  /**
   * Hiển thị danh sách CR secondary
   */
  @Override
  public List<CrDTO> getListSecondaryCrOutSide(CrDTO crDTO) {
    try {
      return crDBRepository.getListSecondaryCrOutSide(crDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public CrInsiteDTO findCrById(Long id) {
    return getEntityManager().find(CrEntity.class, id).toDTO();
  }

  @Override
  public void flushSession() {
    getEntityManager().flush();
  }

  @Override
  public String actionVerifyMrIT(CrInsiteDTO crDTO, String locale) {
    try {
      if (crDTO == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      Long userId = Long.valueOf(crDTO.getUserLogin());
      Long deptId = Long.valueOf(crDTO.getUserLoginUnit());

      if (userId == null || deptId == null || crDTO.getActionType() == null) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      String actionType = crDTO.getActionType();
      String returnCode = crDTO.getActionReturnCodeId();
      String crIdStr = crDTO.getCrId();
      CrInsiteDTO crDTOtoSendSMS = smsDBRepository.getCrById(stringToLong(crIdStr));
      Long crId = Long.parseLong(crIdStr.trim());
      if (Constants.CR_ACTION_CODE.ASSIGN_TO_CONSIDER.toString().equals(actionType.trim())) {
        if (StringUtils.isStringNullOrEmpty(crIdStr)) {
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        crDBRepository
            .updateCrStatusInCaseOfVerifyMrIT(Long.parseLong(actionType), crId, crDTO, locale);
      }

      smsDBRepository.actionSendSms(Long.parseLong(actionType), crDTO, crDTOtoSendSMS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return "EXCEPTION";
    }
    return Constants.CR_RETURN_MESSAGE.SUCCESS;
  }


  @Override
  public Datatable getListCrByIp(CrInsiteDTO crInsiteDTO) {
    BaseDto baseDto = sqlSearch(crInsiteDTO, false);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        crInsiteDTO.getPage(), crInsiteDTO.getPageSize(),
        CrInsiteDTO.class,
        crInsiteDTO.getSortName(), crInsiteDTO.getSortType());
  }

  @Override
  public List<CrDTO> getCRbyImpactIP(CrInsiteDTO crInsiteDTO) {
    BaseDto baseDto = sqlSearch(crInsiteDTO, true);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(CrDTO.class));
  }

  public BaseDto sqlSearch(CrInsiteDTO crInsiteDTO, boolean ws) {
    BaseDto baseDto = new BaseDto();
    String sql = "";
    Map<String, Object> parameters = new HashMap<>();
    if (!ws && !StringUtils.isStringNullOrEmpty(crInsiteDTO.getNodeIp())) {
      sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-list-cr-by-ip");
      parameters.put("ip", crInsiteDTO.getNodeIp());
    } else if (ws && crInsiteDTO.getListImpactedNode() != null && !crInsiteDTO
        .getListImpactedNode().isEmpty()) {
      sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-list-cr-by-ip-ws");
      parameters.put("ips", crInsiteDTO.getListImpactedNode());
      parameters.put("impactedTimeFrom", crInsiteDTO.getImpactedTimeFrom());
      parameters.put("impactedTimeTo", crInsiteDTO.getImpactedTimeTo());
    }

    parameters.put("duThao", I18n.getLanguage("cr.state.draft"));//0
    parameters.put("choPheDuyet", I18n.getLanguage("cr.state.open"));//1
    parameters.put("choKiemTraDauVao", I18n.getLanguage("cr.state.queue"));//2
    parameters.put("choThamDinh", I18n.getLanguage("cr.state.coordinate"));//3
    parameters.put("choSapLich", I18n.getLanguage("cr.state.evaluate"));//4
    parameters.put("choTiepNhan", I18n.getLanguage("cr.state.approve"));//5
    parameters.put("daTiepNhan", I18n.getLanguage("cr.state.accept"));//6
    parameters.put("hoanThanh", I18n.getLanguage("cr.state.resolve"));//7
    parameters.put("thieuThongTin", I18n.getLanguage("cr.state.incomplete"));//8
    parameters.put("dong", I18n.getLanguage("cr.state.close"));//9
    parameters.put("thieuThongTin_Huy", I18n.getLanguage("cr.state.cancel"));//10
    parameters.put("choGiaoCab", I18n.getLanguage("cr.state.wait_cab"));//11
    parameters.put("dangCab", I18n.getLanguage("cr.state.cab"));//12

    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }


  @Override
  public ResultInSideDto insertWorkLog(WorkLogEntity workLogEntity) {
    log.debug("Request to insertWorkLog: {}", workLogEntity);
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    workLogEntity = getEntityManager().merge(workLogEntity);
    resultInSideDto.setId(workLogEntity.getWorkLogId());
    return resultInSideDto;
  }
}
