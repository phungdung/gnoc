package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CR_TYPE;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ObjResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CrMobileRepositoryImpl extends BaseRepository implements CrMobileRepository {

  @Autowired
  UserRepository userRepository;
  @Autowired
  CrDBRepository crDBRepository;

  @Override
  public List<CrDTO> getListCRBySearchTypeCount(CrDTO crDTO) {
    try {
      if (crDTO != null) {
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
          boolean isManager = userRepository.isManagerOfUnits(userId);
          String sql = SQLBuilder
              .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-listCr-by-search-type-count");
          BaseDto baseDto = genConditionSearchCrCount(crDTO, userId, userDept, isManager);
          Map<String, Object> params = baseDto.getParameters();
          sql += baseDto.getSqlQuery();
          sql += " group by cr.cr_type ";
          List<CrDTO> lst = getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper
              .newInstance(CrDTO.class));
          if (lst != null && lst.size() > 0) {
            return lst;
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  private BaseDto genConditionSearchCrCount(CrDTO crDTO, Long userId, Long userDept,
      boolean isManager) throws Exception {
    StringBuffer sql = new StringBuffer("");
    Map<String, Object> paramList = new HashMap<>();
    Double offset = TimezoneContextHolder.getOffsetDouble();
    paramList.put("offset", offset);
    paramList.put("state_close", Constants.CR_STATE.CLOSE);
    paramList.put("action_reject", Constants.CR_ACTION_CODE.REJECT);
    paramList.put("act_close", Constants.CR_ACTION_CODE.CLOSE);
    paramList.put("act_close_by_appr", Constants.CR_ACTION_CODE.CLOSE_BY_APPRAISER);
    paramList.put("act_close_by_man", Constants.CR_ACTION_CODE.CLOSE_BY_MANAGER_SCH);
    paramList.put("act_close_by_emer", Constants.CR_ACTION_CODE.CLOSE_EXCUTE_EMERGENCY);
    paramList.put("act_close_cr", Constants.CR_ACTION_CODE.CLOSECR);
    paramList.put("act_close_cr_appr", Constants.CR_ACTION_CODE.CLOSECR_APPROVE_STD);
    paramList.put("act_close_excu", Constants.CR_ACTION_CODE.CLOSE_EXCUTE_STD);
    paramList.put("state_draft", Constants.CR_STATE.DRAFT);
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.CREATE_EDIT.toString())) {
      sql.append(" and ( ( ");
      // <editor-fold desc="Cho phep nguoi tao CR co the sua CR khi CR chua phe duyet hoac INCOMPLETE">
      sql.append(" cr.state in (:state_open, :state_incom, :state_draft) ");
      paramList.put("state_open", Constants.CR_STATE.OPEN);
      paramList.put("state_incom", Constants.CR_STATE.INCOMPLETE);
      paramList.put("state_draft", Constants.CR_STATE.DRAFT);
      sql.append(" and cr.change_orginator_unit = :org_unit ");
      paramList.put("org_unit", userDept);
      sql.append(" and cr.change_orginator = :change_orginator ");
      paramList.put("change_orginator", userId);
      // </editor-fold>

      sql.append(" ) or (");
      // <editor-fold desc="Cho phep tat ca nhan vien trong don vi them WLog khi CR RESOLVE">
      sql.append(" cr.state = :state_resolve ");
      paramList.put("state_resolve", Constants.CR_STATE.RESOLVE);
      sql.append(" and cr.change_orginator_unit in ( ");
      sql.append(" SELECT unit_id");
      sql.append(" FROM common_gnoc.unit ");
      sql.append(" START WITH  unit_id = :unit_id ");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" )");
      paramList.put("unit_id", userDept);
      // </editor-fold>
      sql.append(" ) )");
    }

    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.LOOKUP.toString())) {
      if (crDTO.getState() != null && !"".equals(crDTO.getState().trim())) {
        String[] arrState = crDTO.getState().split(",");
        List<String> lstState = Arrays.asList(arrState);
        if (lstState == null) {
          lstState = new ArrayList<>();
        }
        List<String> lstStateAll = new ArrayList<>();
        lstStateAll.addAll(lstState);
        lstStateAll.add("-1");
        sql.append(" and cr.state in (:lst_state) ");
        paramList.put("lst_state", lstStateAll);
      } else {
        sql.append(" and cr.state in (-1) ");
      }
    }

    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.APPROVE.toString())) {
      sql.append(" and cr.state = :state_open ");
      paramList.put("state_open", Constants.CR_STATE.OPEN);
      sql.append(
          " and exists (select 1 from common_gnoc.v_user_role where user_id = :user_id and role_code = :role_code )");
      paramList.put("user_id", userId);
      paramList.put("role_code", Constants.CR_ROLE.roleTP);
//                        if(crDTO.getState().equals(Constants.CR_STATE.OPEN.toString())){
      if (crDTO.getIsSearchChildDeptToApprove() != null
          && "1".equals(crDTO.getIsSearchChildDeptToApprove().trim())) {
        sql.append(" and cr.cr_id in (");
        sql.append(" select cr_id from ");
        sql.append(" cr_approval_department d ");
        sql.append(" where ");
        sql.append(" d.cr_id = cr.cr_id and d.unit_id = :unit_id ");
        sql.append(" and d.status = 0 )");
        paramList.put("unit_id", userDept);
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
        paramList.put("unit_id", userDept);
      }
    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.VERIFY.toString())) {
      sql.append(" and cr.state = :state_queue ");
      paramList.put("state_queue", Constants.CR_STATE.QUEUE);
      sql.append(" and cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit = :manage_unit");
      sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 ");
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" )");
      sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :scope_id)");
      paramList.put("manage_unit", userDept);
      paramList.put("scope_id", userDept);

    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.WAIT_CAB.toString())) {
      //chi CAB cr thuong
      sql.append(" and cr.state in( :state_wait_cab,:state_cab) ");
      paramList.put("state_wait_cab", Constants.CR_STATE.WAIT_CAB);
      paramList.put("state_cab", Constants.CR_STATE.CAB);
      sql.append(" and cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit = :manage_unit");
      if (!StringUtils.isStringNullOrEmpty(crDTO.getScopeId())) {
        sql.append(" and scope_id = :scope_id ");
      }
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" )");
      sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit_id)");
      paramList.put("manage_unit", userDept);
      if (!StringUtils.isStringNullOrEmpty(crDTO.getScopeId())) {
        paramList.put("scope_id", Long.valueOf(crDTO.getScopeId()));
      }
      paramList.put("manage_unit_id", userDept);

    }

    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.CAB.toString())) {

      sql.append(" and (");
      sql.append("( cr.state = :state_cab ");
      paramList.put("state_cab", Constants.CR_STATE.CAB);
      sql.append(" and cr.user_cab =:user_cab) ");
      paramList.put("user_cab", userId);
      if (crDTO.getScopeId() != null) {
        sql.append(" OR (  cr.state in (:state_approve, :state_accept,:state_resolve) ");
        paramList.put("state_approve", Constants.CR_STATE.APPROVE);
        paramList.put("state_accept", Constants.CR_STATE.ACCEPT);
        paramList.put("state_resolve", Constants.CR_STATE.RESOLVE);
        sql.append(" and cr.cr_type = :cr_type_emer ");
        paramList.put("cr_type_emer", Constants.CR_TYPE.EMERGENCY);
        sql.append(" and cr.change_responsible_unit in ");
        sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
        sql.append(" start with ut.unit_id in ");
        sql.append(" ( ");
        sql.append(" select unit_id ");
        sql.append(" from cr_manager_units_of_scope cmnose ");
        if (!StringUtils.isStringNullOrEmpty(crDTO.getScopeId())) {
          sql.append(" where cmnose.cmse_id = :scope_id");
          paramList.put("scope_id", Long.valueOf(crDTO.getScopeId()));
        }
        sql.append(" )");
        sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
        sql.append(" ) ) ");
      }
      sql.append(" )");

    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.QLTD.toString())) {
      sql.append(
          " and cr.state in(:state_wait_cab,:state_cab,:state_queue,:state_coor,:state_eval) ");
      paramList.put("state_wait_cab", Constants.CR_STATE.WAIT_CAB);
      paramList.put("state_cab", Constants.CR_STATE.CAB);
      paramList.put("state_queue", Constants.CR_STATE.QUEUE);
      paramList.put("state_coor", Constants.CR_STATE.COORDINATE);
      paramList.put("state_eval", Constants.CR_STATE.EVALUATE);
      sql.append(" and cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit = :manage_unit");
      if (!StringUtils.isStringNullOrEmpty(crDTO.getScopeId())) {
        sql.append(" and scope_id = :scope_id ");
      }
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" )");
      sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit_id)");
      paramList.put("manage_unit", userDept);
      if (!StringUtils.isStringNullOrEmpty(crDTO.getScopeId())) {
        paramList.put("scope_id", Long.valueOf(crDTO.getScopeId()));
      }
      paramList.put("manage_unit_id", userDept);

    }

    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.Z78.toString())) {
      sql.append(" and cr.state = :state_accept ");
      paramList.put("state_accept", Constants.CR_STATE.ACCEPT);
      sql.append(" and cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" ( ");
      sql.append(" select unit_id ");
      sql.append(" from cr_manager_units_of_scope cmnose ");
      if (!StringUtils.isStringNullOrEmpty(crDTO.getScopeId())) {
        sql.append(" where cmnose.cmse_id = :scope_id");
      }
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" )");
      if (!StringUtils.isStringNullOrEmpty(crDTO.getScopeId())) {
        paramList.put("scope_id", Long.valueOf(crDTO.getScopeId()));
      }

    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.CONSIDER.toString())) {
//      if (isManager) {
      sql.append(" and cr.state = :state_coor ");
      paramList.put("state_coor", Constants.CR_STATE.COORDINATE);
      sql.append(" and ( cr.consider_unit_id = :unit_id ");
      paramList.put("unit_id", userDept);
      sql.append(" OR  cr.consider_user_id = :user_id )");
      paramList.put("user_id", userId);
//      } else {
//        sql.append(" and cr.state = :state_coor ");
//        paramList.put("state_coor", Constants.CR_STATE.COORDINATE);
//        sql.append(" and cr.consider_user_id = :user_id ");
//        paramList.put("user_id", userId);
//      }
    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.SCHEDULE.toString())) {
      sql.append(" and cr.state = :state_eval ");
      paramList.put("state_eval", Constants.CR_STATE.EVALUATE);
      sql.append(" and cr.change_responsible_unit in ");
      sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
      sql.append(" start with ut.unit_id in ");
      sql.append(" (select ");
      sql.append(" excute_unit_id  from v_manage_cr_config ");
      sql.append(" where ");
      sql.append(" manage_unit = :manage_unit");
      if (!StringUtils.isStringNullOrEmpty(crDTO.getScopeId())) {
        sql.append(" and scope_id = :scope_id ");
      }
      //tuanpv14_sap lich cr khan start
      if (Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())) {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY = 1 ");
      } else {
        sql.append(" and IS_SCHEDULE_CR_EMERGENCY <> 1 ");
      }
      //tuanpv14_sap lich cr khan end
      sql.append(" )");
      sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
      sql.append(" )");
      sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit)");
      paramList.put("manage_unit", userDept);
      if (!StringUtils.isStringNullOrEmpty(crDTO.getScopeId())) {
        paramList.put("scope_id", Long.valueOf(crDTO.getScopeId()));
      }
//      paramList.put("manage_unit",userDept);

    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.EXCUTE.toString())) {
      if (isManager) {
        //20160622 daitr1 chinh sua chuc nang giao viec
//                sql.append(" and cr.state = ? ");
        sql.append(
            " and ( cr.state = :state_approve or (cr.state = :state_accept and cr.earliest_start_time > sysdate)) ");
        paramList.put("state_approve", Constants.CR_STATE.APPROVE);
        paramList.put("state_accept", Constants.CR_STATE.ACCEPT);
        //20160622 daitr1 chinh sua chuc nang giao viec
        sql.append(" and (cr.change_responsible_unit = :responsible_unit ");
        paramList.put("responsible_unit", userDept);
        sql.append(" or cr.change_responsible = :change_responsible )");
        paramList.put("change_responsible", userId);
      } else {
        sql.append(" and cr.state = :state_approve ");
        paramList.put("state_approve", Constants.CR_STATE.APPROVE);
        sql.append(" and ((cr.change_responsible_unit = :responsible_unit and cr.cr_type = 0 )");
        paramList.put("responsible_unit", userDept);
        sql.append(" or cr.change_responsible = :change_responsible) ");
        paramList.put("change_responsible", userId);
      }
    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.RESOLVE.toString())) {
      sql.append(" and cr.state = :state_accept ");
      paramList.put("state_accept", Constants.CR_STATE.ACCEPT);
      sql.append(" and cr.change_responsible = :change_responsible ");
      paramList.put("change_responsible", userId);
    }
    if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.CLOSE.toString())) {
//            if (isManager) {
      if (crDTO.getScopeId() == null || "".equals(crDTO.getScopeId().trim())) {
        if (isManager) {
          sql.append(" and cr.state = :state_resolve ");
          paramList.put("state_resolve", Constants.CR_STATE.RESOLVE);
          sql.append(" and cr.manage_unit_id = :manage_unit ");
          paramList.put("manage_unit", userDept);
        } else {
          sql.append(" and 1=2");//Nhan vien khong co quyen tac dong den CR cho` dong
        }
      } else {
        sql.append(" and cr.state = :state_resolve ");
        paramList.put("state_resolve", Constants.CR_STATE.RESOLVE);
        sql.append(" and cr.change_responsible_unit in ");
        sql.append(" (select ut.unit_id from common_gnoc.unit ut ");
        sql.append(" start with ut.unit_id in ");
        sql.append(" (select ");
        sql.append(" excute_unit_id  from v_manage_cr_config ");
        sql.append(" where ");
        sql.append(" manage_unit = :manage_unit");
        if (!StringUtils.isStringNullOrEmpty(crDTO.getScopeId())) {
          sql.append(" and scope_id = :scope_id ");
        }
        sql.append("  and IS_SCHEDULE_CR_EMERGENCY <> 1 )");
        sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
        sql.append(" )");
        sql.append(" and (cr.manage_unit_id is null or cr.manage_unit_id = :manage_unit)");
        paramList.put("manage_unit", userDept);
        if (!StringUtils.isStringNullOrEmpty(crDTO.getScopeId())) {
          paramList.put("scope_id", Long.valueOf(crDTO.getScopeId()));
        }
//        paramList.put(userDept);

      }
    }

    if (crDTO.getCrNumber() != null
        && !"".equals(crDTO.getCrNumber().trim())) {
      List<String> arrCrNumber = Arrays.asList(crDTO.getCrNumber().split(","));
      if (arrCrNumber.size() > 1) {

        sql.append(" and cr.cr_number in(:lst_cr) ");
        paramList.put("lst_cr", arrCrNumber);
//        sql.append(" and cr.cr_number in( ");
//        for (String str : arrCrNumber) {
//          sql.append("?,");
//          paramList.put(str);
//        }
//        sql.replace(sql.length() - 1, sql.length(), "");
//        sql.append(")");
      } else {
        sql.append(" and cr.cr_number like :cr_number  ESCAPE '\\' ");
        paramList.put("cr_number",
            StringUtils.convertLowerParamContains(crDTO.getCrNumber()).toUpperCase());

      }
    }

    if (crDTO.getTitle() != null
        && !"".equals(crDTO.getTitle().trim())) {
      sql.append(" and lower(cr.title) like :cr_title  ESCAPE '\\' ");
      paramList
          .put("cr_title", StringUtils.convertLowerParamContains(crDTO.getTitle()).toLowerCase());
    }
    if (crDTO.getEarliestStartTime() != null
        && !"".equals(crDTO.getEarliestStartTime().trim())) {
      sql.append(" and cr.earliest_start_time >= :earliest_start_time ");
      paramList.put("earliest_start_time", DateTimeUtils.convertStringToDateTime(
          DateTimeUtils.converClientDateToServerDate(crDTO.getEarliestStartTime().trim(), offset)));
    }
    if (crDTO.getEarliestStartTimeTo() != null
        && !"".equals(crDTO.getEarliestStartTimeTo().trim())) {
      sql.append(" and cr.earliest_start_time <= :earliest_start_time_to ");
      paramList.put("earliest_start_time_to", DateTimeUtils.converStringClientToServerDate(
          crDTO.getEarliestStartTimeTo().trim(), offset));
      /*paramList.put(DateTimeUtils.convertStringToDateTime(
          DateTimeUtils.converClientDateToServerDate(crDTO.getEarliestStartTimeTo().trim(), offset)));*/
    }
    if (crDTO.getLatestStartTime() != null
        && !"".equals(crDTO.getLatestStartTime().trim())) {
      sql.append(" and cr.latest_start_time >= :latest_start_time ");
      paramList.put("latest_start_time", DateTimeUtils.converStringClientToServerDate(
          crDTO.getLatestStartTime().trim(), offset));
    }
    if (crDTO.getLatestStartTimeTo() != null
        && !"".equals(crDTO.getLatestStartTimeTo().trim())) {
      sql.append(" and cr.latest_start_time <= :latest_start_time_to ");
      paramList.put("latest_start_time_to", DateTimeUtils.convertStringToDateTime(
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
      sql.append(" and cr.cr_type = :cr_type ");
      paramList.put("cr_type", Long.parseLong(crDTO.getCrType().trim()));
    }
    if (crDTO.getSubcategory() != null
        && !"".equals(crDTO.getSubcategory().trim())
        && !"-1".equals(crDTO.getSubcategory().trim())) {
      sql.append(" and cr.Subcategory = :sub_category ");
      paramList.put("sub_category", Long.parseLong(crDTO.getSubcategory().trim()));
    }
    if (crDTO.getRisk() != null
        && !"".equals(crDTO.getRisk().trim())
        && !"-1".equals(crDTO.getRisk().trim())) {
      sql.append(" and cr.risk = :risk ");
      paramList.put("risk", Long.parseLong(crDTO.getRisk().trim()));
    }
    if (crDTO.getImpactSegment() != null
        && !"".equals(crDTO.getImpactSegment().trim())
        && !"-1".equals(crDTO.getImpactSegment().trim())) {
      sql.append(" and cr.impact_segment = :impact_segment ");
      paramList.put("impact_segment", Long.parseLong(crDTO.getImpactSegment().trim()));
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
        sql.append(" and cr.country in( :country1, :country2 )");
        paramList.put("country1", Long.parseLong(crDTO.getCountry().trim()));
        if ("1500289728".equals(crDTO.getCountry())) {//timor
          paramList.put("country2", 289728L);
        } else if ("2000289729".equals(crDTO.getCountry())) {//timor
          paramList.put("country2", 289729L);
        } else if ("3000289724".equals(crDTO.getCountry())) {//mozam
          paramList.put("country2", 289724L);
        } else if ("6000289723".equals(crDTO.getCountry())) {//timor
          paramList.put("country2", 289723L);
        } else if ("4500000001".equals(crDTO.getCountry())) {//myanmar
          paramList.put("country2", 300656L);
        } else if ("4001000000".equals(crDTO.getCountry())) {//tanz
          paramList.put("country2", 289727L);
        } else if ("5000289722".equals(crDTO.getCountry())) {//lao
          paramList.put("country2", 289722L);
        } else if ("1000014581".equals(crDTO.getCountry())) {//cam
          paramList.put("country2", 289721L);
        } else if ("3500289726".equals(crDTO.getCountry())) {//burundi
          paramList.put("country2", 289726L);
        }
      } else {
        sql.append(" and cr.country = :country ");
        paramList.put("country", Long.parseLong(crDTO.getCountry().trim()));
      }
    }
    if (crDTO.getChangeOrginator() != null
        && !"".equals(crDTO.getChangeOrginator().trim())
        && !"-1".equals(crDTO.getChangeOrginator().trim())) {
      sql.append(" and cr.Change_Orginator = :change_orginator ");
      paramList.put("change_orginator", Long.parseLong(crDTO.getChangeOrginator().trim()));
    }
    if (crDTO.getChangeResponsible() != null
        && !"".equals(crDTO.getChangeResponsible().trim())
        && !"-1".equals(crDTO.getChangeResponsible().trim())) {
      sql.append(" and cr.Change_Responsible = :change_responsible ");
      paramList.put("change_responsible", Long.parseLong(crDTO.getChangeResponsible().trim()));
    }
    if (crDTO.getChangeOrginatorUnit() != null
        && !"".equals(crDTO.getChangeOrginatorUnit().trim())
        && !"-1".equals(crDTO.getChangeOrginatorUnit().trim())) {
      if (crDTO.getSubDeptOri() != null && "1".equals(crDTO.getSubDeptOri().trim())) {
        sql.append(" and cr.Change_Orginator_Unit in ( ");
        sql.append(" SELECT unit_id");
        sql.append(" FROM common_gnoc.unit ");
        sql.append(" START WITH  unit_id = :change_unit ");
        sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
        sql.append(" )");
        paramList.put("change_unit", Long.parseLong(crDTO.getChangeOrginatorUnit().trim()));
      } else {
        sql.append(" and cr.Change_Orginator_Unit = :change_unit ");
        paramList.put("change_unit", Long.parseLong(crDTO.getChangeOrginatorUnit().trim()));
      }
    }
    if (crDTO.getChangeResponsibleUnit() != null
        && !"".equals(crDTO.getChangeResponsibleUnit().trim())
        && !"-1".equals(crDTO.getChangeResponsibleUnit().trim())) {
      if (crDTO.getSubDeptResp() != null && "1".equals(crDTO.getSubDeptResp().trim())) {
        sql.append(" and cr.Change_Responsible_Unit in ( ");
        sql.append(" SELECT unit_id");
        sql.append(" FROM common_gnoc.unit ");
        sql.append(" START WITH  unit_id = :change_res_unit ");
        sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id");
        sql.append(" )");
        paramList.put("change_res_unit", Long.parseLong(crDTO.getChangeResponsibleUnit().trim()));
      } else {
        sql.append(" and cr.Change_Responsible_Unit = :change_res_unit ");
        paramList.put("change_res_unit", Long.parseLong(crDTO.getChangeResponsibleUnit().trim()));
      }
    }
    BaseDto baseDto = new BaseDto();
    baseDto.setParameters(paramList);
    baseDto.setSqlQuery(sql.toString());
    return baseDto;
  }

  public List<CrDTO> getListCRBySearchTypeMobile(CrDTO crDTO, int start, int maxResult,
      String locale) {
    try {
      if (crDTO != null) {
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
          boolean isManager = userRepository.isManagerOfUnits(userId);
          List<CrDTO> list = new ArrayList<>();
          String sql = SQLBuilder
              .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-listCr-by-search-type-mobile");
          String CR_PROCESS_NAME_LABEL =
          "(case cr.process_type_id ||'@'||cr.cr_type   when '0@" + CR_TYPE.STANDARD + "' then '" + I18n.getChangeManagement("cr.specialProcessStandard")
              + "' " + "  when '0@" + CR_TYPE.NORMAL + "' then '" + I18n.getChangeManagement("cr.specialProcess")
              + "' " + "  when '0@" + CR_TYPE.EMERGENCY + "' then '" + I18n.getChangeManagement("cr.specialProcess")
              + "' " + "  when '0@' then '" + I18n.getChangeManagement("cr.specialProcess")
              + "' " + "  else TO_CHAR(replace(replace(cps.cr_process_name,CHR(10),'_'), ',', '_')) end) crProcessName ";
          sql = sql.replace(":CR_PROCESS_NAME", CR_PROCESS_NAME_LABEL);
          BaseDto baseDto = genConditionSearchCrCount(crDTO, userId, userDept, isManager);
          Map<String, Object> params = baseDto.getParameters();
          sql += baseDto.getSqlQuery();

          sql += " order by cr.update_time desc ";
          if (maxResult == 0) {
            maxResult = Integer.MAX_VALUE;
          }
          int page = (int) Math.ceil((start + 1) * 1.0 / maxResult);
          int pageSize = maxResult;
          Datatable datatable = getListDataTableBySqlQuery(sql, params, page,
              pageSize, CrInsiteDTO.class,
              null, null);
          List<CrInsiteDTO> lst = (List<CrInsiteDTO>) datatable.getData();
          String language = I18n.getLocale();
          if (!StringUtils.isStringNullOrEmpty(locale)) {
            language = locale;
          }
          List<CrInsiteDTO> lstInsite = crDBRepository
              .processListToGenGr(lst, crDTO.toModelInsiteDTO(), isManager, userId, userDept,
                  language);
          if (lstInsite != null && !lstInsite.isEmpty()) {
            for (CrInsiteDTO item : lstInsite) {
              list.add(item.toCrDTO());
            }
          }
          return list;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public ObjResponse getListCRBySearchTypePaggingMobile(CrDTO crDTO, int start, int maxResult,
      String locale) {
    ObjResponse obj = new ObjResponse();
    try {
      if (StringUtils.isNotNullOrEmpty(crDTO.getEarliestStartTime()) && StringUtils.isNotNullOrEmpty(crDTO.getEarliestStartTimeTo())) {
        List<CrDTO> lst = getListCRBySearchTypeMobile(crDTO, start, maxResult, locale);
        long count = countCRBySearchTypeMobile(crDTO);
        obj.setLstCrDTO(lst);
        obj.setTotalRow(count);
      } else {
        obj.setTotalRow(0l);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return obj;
  }

  public long countCRBySearchTypeMobile(CrDTO crDTO) {
    try {
      if (crDTO != null) {
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
          boolean isManager = userRepository.isManagerOfUnits(userId);
          StringBuffer sql = new StringBuffer();
          sql.append("select count(cr_id) totalRow "
              + " from open_pm.cr"
              + " where 1 = 1 ");
          BaseDto baseDto = genConditionSearchCrCount(crDTO, userId, userDept, isManager);
          sql.append(baseDto.getSqlQuery());
          Map<String, Object> params = baseDto.getParameters();
          List<BaseDto> data = getNamedParameterJdbcTemplate()
              .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(BaseDto.class));
          if (data != null && data.size() > 0) {
            return Long.valueOf(data.get(0).getTotalRow());
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return 0;
  }

  @Override
  public CrDTO getCrByIdExtends(String crId) {
    String sql = " select cr.CR_ID crId, REPLACE(dts.device_type_name, '/',', ') deviceType,\n"
        + "(case cr.process_type_id ||'@'||cr.cr_type   when '0@" + CR_TYPE.STANDARD + "' then '" + I18n.getChangeManagement("cr.specialProcessStandard")
        + "' " + "  when '0@" + CR_TYPE.NORMAL + "' then '" + I18n.getChangeManagement("cr.specialProcess")
        + "' " + "  when '0@" + CR_TYPE.EMERGENCY + "' then '" + I18n.getChangeManagement("cr.specialProcess")
        + "' " + "  when '0@' then '" + I18n.getChangeManagement("cr.specialProcess")
        + "' " + "  else TO_CHAR(replace(replace(cps.cr_process_name,CHR(10),'_'), ',', '_')) end) crProcessName, "
        + " case "
        + " cr.duty_type "
        + " when 2 then '" + I18n.getChangeManagement("common.day") + "' "
        + " else '" + I18n.getChangeManagement("common.night") + "' "
        + " end dutyType "
        + " from open_pm.cr cr\n"
        + " left join open_pm.device_types dts on cr.device_type = dts.device_type_id\n"
        + " left join (SELECT CR_PROCESS_ID, \n"
        + "  CR_PROCESS_CODE, \n"
        + "  DESCRIPTION, \n"
        + "  IMPACT_SEGMENT_ID, \n"
        + "  DEVICE_TYPE_ID, \n"
        + "  SUBCATEGORY_ID, \n"
        + "  RISK_LEVEL, \n"
        + "  IMPACT_TYPE, \n"
        + "  CR_TYPE_ID, \n"
        + "  IS_ACTIVE, \n"
        + "  PARENT_ID, \n"
        + "  IMPACT_CHARACTERISTIC, \n"
        + "  OTHER_DEPT, \n"
        + "  REQUIRE_MOP, \n"
        + "  REQUIRE_FILE_LOG, \n"
        + "  REQUIRE_FILE_TEST, \n"
        + "  APPROVAL_LEVEL, \n"
        + "  CLOSE_CR_WHEN_RESOLVE_SUCCESS, \n"
        + "  IS_VMSA_ACTIVE_CELL_PROCESS, \n"
        + "  VMSA_ACTIVE_CELL_PROCESS_KEY, \n"
        + "  IS_LANE_IMPACT, \n"
        + "  SYS_CONNECT_BY_PATH (CR_PROCESS_NAME, ' ; ') CR_PROCESS_NAME \n"
        + " FROM open_pm.cr_process START WITH PARENT_ID IS NULL CONNECT BY PRIOR CR_PROCESS_ID = PARENT_ID) cps\n"
        + " on cr.process_type_id = cps.cr_process_id \n"
        + " where cr_id = :crId         \n";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("crId", crId);
    return getNamedParameterJdbcTemplate().queryForObject(sql, parameters, BeanPropertyRowMapper.newInstance(CrDTO.class));
  }
}
