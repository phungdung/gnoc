package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveRolesDTO;
import com.viettel.gnoc.maintenance.dto.MrClientDetail;
import com.viettel.gnoc.maintenance.dto.MrClientItem;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrForNocSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrMobileDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.maintenance.dto.MrSearchDTO;
import com.viettel.gnoc.maintenance.model.MrEntity;
import com.viettel.gnoc.sr.dto.InsertFileDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrRepositoryImpl extends BaseRepository implements MrRepository {

  @Override
  public MrInsideDTO findMrById(Long mrId) {
    if (mrId != null) {
      MrEntity mrEntity = getEntityManager().find(MrEntity.class, mrId);
      if (mrEntity != null) {
        return mrEntity.toDTO();
      }
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteMr(Long mrId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    MrEntity entity = getEntityManager().find(MrEntity.class, mrId);
    getEntityManager().remove(entity);
    return resultInSideDTO;
  }


  @Override
  public ResultInSideDto insertListMr(List<MrInsideDTO> mrDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    if (mrDTO != null) {
      for (MrInsideDTO dto : mrDTO) {
        getEntityManager().merge(dto.toEntity());
      }
    }
    return resultInSideDTO;
  }

  @Override
  public List<MrDTO> checkExistCrId(String crId) {
    try {
      String sqlMr = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "check-exist-cr-id");
      Map<String, Object> params = new HashMap<>();
      params.put("crId", crId);
      return getNamedParameterJdbcTemplate()
          .query(sqlMr, params, BeanPropertyRowMapper.newInstance(MrDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<MrDTO> checkExistWoId(String woId) {
    try {
      String sqlMr = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "check-exist-wo-id");
      Map<String, Object> params = new HashMap<>();
      params.put("woId", woId);
      return getNamedParameterJdbcTemplate()
          .query(sqlMr, params, BeanPropertyRowMapper.newInstance(MrDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public ResultDTO updateMrStatus(MrDTO dtoUpdate) {
    try {
      String sql = " update OPEN_PM.MR set state = 4 where mr_id = :mrId ";
      Map<String, Object> params = new HashMap<>();
      params.put("mrId", dtoUpdate.getMrId());
      getNamedParameterJdbcTemplate().update(sql, params);
      return new ResultDTO("1", RESULT.SUCCESS, "Update success");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO("0", RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public boolean checkCrIdInMrNode(String crId) {
    try {
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "check-cr-in-mr-node");
      Map<String, Object> params = new HashMap<>();
      params.put("crId", crId);
      List lstData = getNamedParameterJdbcTemplate().queryForList(sql, params, String.class);
      return (lstData == null || lstData.size() < 1) ? false : true;
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public MrDTO getMrForClose(String crId) {
    try {
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "get-mr-for-close");
      Map<String, Object> params = new HashMap<>();
      params.put("crId", crId);

      List<MrDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrDTO.class));
      if (lst != null && !lst.isEmpty()) {
        return lst.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public int getCountCrNotClose(String mrId) {
    try {
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "cout-cr-not-close");
      Map<String, Object> params = new HashMap<>();
      params.put("objectId", mrId);
      List lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrDTO.class));
      if (lst != null && !lst.isEmpty()) {
        return lst.size();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return 0;
  }

  @Override
  public UsersDTO getUsersOfUnit(Long unitId) {
    try {
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "get-user-of-unit");
      Map<String, Object> params = new HashMap<>();
      params.put("unitId", unitId);
      List<UsersDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(UsersDTO.class));
      if (lst != null && !lst.isEmpty()) {
        return lst.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public InsertFileDTO getCrFileTelInsert(MrScheduleTelDTO objSchedule) throws Exception {
    InsertFileDTO insertFile = null;
    try {
      StringBuffer sql = new StringBuffer();
      Map<String, Object> params = new HashMap<>();
      sql.append(SQLBuilder.getSqlQueryById("cfgProcedureTelAttach", "get-files"));
      sql.append(" and a.BUSINESS_CODE = :businessCode ");
      params.put("businessCode", GNOC_FILE_BUSSINESS.MR_CFG_PROCEDURE_TEL);

      if (objSchedule.getProcedureId() != null) {
        sql.append(" and a.BUSINESS_ID = :businessId ");
        params.put("businessId", objSchedule.getProcedureId());
      }
      List<GnocFileDto> lst = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(
              GnocFileDto.class));

      if (lst != null && !lst.isEmpty()) {
        if (!StringUtils.isStringNullOrEmpty(lst.get(0).getFileName()) && !StringUtils
            .isStringNullOrEmpty(lst.get(0).getPath())) {
          insertFile = new InsertFileDTO();
          insertFile.setUserService("cr_service");
          insertFile.setPassService("gnoc_cr#123");
          insertFile.setFileType("100");// Loai file khac
          insertFile.setFileName(lst.get(0).getFileName());
          insertFile.setFileContent(lst.get(0).getPath());
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
    return insertFile;
  }

  @Override
  public List<WoDTO> getListWoOfCr(String crId) {
    List<WoDTO> list = new ArrayList<>();
    try {
      StringBuffer sql = new StringBuffer();
      sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "get-list-wo-of-cr"));
      Map<String, Object> params = new HashMap<>();
      params.put("wo_system_id", crId);

      list = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(WoDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  @Override
  public List<CrDTO> getListCrOfMr(String mrId) {
    try {
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MR_SERVICE, "get-list-cr-of-mr");
      Map<String, Object> params = new HashMap<>();
      params.put("objectId", mrId);
      params.put("mrId", mrId);
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public InsertFileDTO getCrFileInsert(MrITSoftScheduleDTO objSchedule) throws Exception {
    InsertFileDTO insertFile = null;
    try {
      StringBuffer sql = new StringBuffer();
      Map<String, Object> params = new HashMap<>();
      sql.append(
          SQLBuilder.getSqlQueryById(SQLBuilder.MR_FILES_ATTACH, "get-list-mr-files-search"));
      sql.append(" and a.BUSINESS_CODE = :businessCode ");
      params.put("businessCode", GNOC_FILE_BUSSINESS.MR_CFG_PROCEDURE_IT_SOFT);

      if (objSchedule.getProcedureId() != null) {
        sql.append(" and a.BUSINESS_ID = :businessId ");
        params.put("businessId", objSchedule.getProcedureId());
      }
      List<GnocFileDto> lst = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(
              GnocFileDto.class));

      if (lst != null && !lst.isEmpty()) {
        if (!StringUtils.isStringNullOrEmpty(lst.get(0).getFileName()) && !StringUtils
            .isStringNullOrEmpty(lst.get(0).getPath())) {
          insertFile = new InsertFileDTO();
          insertFile.setUserService("cr_service");
          insertFile.setPassService("gnoc_cr#123");
          insertFile.setFileType("100");// Loai file khac
          insertFile.setFileName(lst.get(0).getFileName());
          insertFile.setFileContent(lst.get(0).getPath());
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
    return insertFile;
  }

  @Override
  public boolean checkCrExists(String mrId, String crId) {
    try {
      StringBuffer sql = new StringBuffer();
      sql.append(
          "SELECT CR_ID crId FROM CR_CREATED_FROM_OTHER_SYS WHERE OBJECT_ID = :mrId AND CR_ID = :crId ");
      Map<String, Object> params = new HashMap<>();
      params.put("mrId", mrId);
      params.put("crId", crId);
      List<CrCreatedFromOtherSysDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(
              CrCreatedFromOtherSysDTO.class));

      if (lst != null && !lst.isEmpty()) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return false;
    }
  }

  @Override
  public MrClientDetail getMrChartInfoForNOC(MrForNocSearchDTO mrSearchDTO) {
    MrClientDetail dto = new MrClientDetail();
    dto.setLstState(getListState(false, mrSearchDTO));
    dto.setLstStateOutDate(getListState(true, mrSearchDTO));
    return dto;
  }

  @Override
  public List<MrDTO> getListMrForMobile(MrMobileDTO dto) {
    UserTokenGNOCSimple userForm = new UserTokenGNOCSimple();
    userForm.setUserId(Long.valueOf(dto.getUserId()));
    userForm.setUnitId(Long.valueOf(dto.getUnitId()));
    List<MrApproveRolesDTO> lstRole = getLstUserByRole(userForm);
    if (lstRole == null || lstRole.isEmpty()) {
      return null;
    } else {
      MrSearchDTO searchDTO = dto.convertDTO(dto);
      return getListMrDTOSearch(searchDTO, true);
    }
  }

  public List<MrApproveRolesDTO> getLstUserByRole(UserTokenGNOCSimple approveRoles) {
    try {
      StringBuffer sql = new StringBuffer();
      Map<String, Object> params = new HashMap<>();
      sql.append("select us.user_id userId, us.username userName,ut.unit_code unitCode,"
          + "         ut.unit_id unitId, ut.unit_name unitName,rs.role_code roleCode,"
          + "         rs.role_name roleName  "
          + " from common_gnoc.users us  "
          + " left join common_gnoc.unit ut on us.unit_id = ut.unit_id "
          + " left join common_gnoc.role_user rur on rur.user_id = us.user_id "
          + " left join common_gnoc.roles rs on rs.role_id = rur.role_id ");
      sql.append(" where 1=1 ");
      if (approveRoles != null) {
        if (!StringUtils.isStringNullOrEmpty(approveRoles.getUserId())) {
          sql.append(" and us.user_id = :userId");
          params.put("userId", String.valueOf(approveRoles.getUserId()));
        }
        if (!StringUtils.isStringNullOrEmpty(approveRoles.getUnitCode())) {
          sql.append(" and ut.unit_code = :unitCode");
          params.put("unitCode", approveRoles.getUnitCode());
        }
        if (!StringUtils.isStringNullOrEmpty(approveRoles.getUserName())) {
          sql.append(" and (lower(us.username) = lower(:userName) or us.staff_code= :userName)");
          params.put("userName", approveRoles.getUserName());
        }
        if (!StringUtils.isStringNullOrEmpty(approveRoles.getUnitId())) {
          sql.append(" and ut.unit_id = :unitId");
          params.put("unitId", String.valueOf(approveRoles.getUnitId()));
        }
//                if (!StringUtils.isStringNullOrEmpty(approveRoles.getRoleCode())) {
        sql.append(" and rs.role_code = 'TP' ");
//                    listParams.add(approveRoles.getRoleCode());
//                }
        if (!StringUtils.isStringNullOrEmpty(approveRoles.getUnitName())) {
          sql.append(" and ut.unit_name = :unitName ");
          params.put("unitName", approveRoles.getUnitName());
        }
      }

      return getNamedParameterJdbcTemplate().query(sql.toString(), params,
          BeanPropertyRowMapper.newInstance(MrApproveRolesDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  private List<MrClientItem> getListState(boolean outOfDate, MrForNocSearchDTO mrSearchDTO) {
    List<MrClientItem> list = new ArrayList<>();
    try {
      StringBuilder sql = new StringBuilder();
      Map<String, Object> params = new HashMap<>();
      Map<String, String> map = new HashMap<>();
      map.put("OPEN", "1");
      map.put("INACTIVE_WAITTING", "2");
      map.put("QUEUE", "3");
      map.put("ACTIVE", "4");
      map.put("INACTIVE", "5");
      map.put("CLOSE", "6");
      sql.append("   "
          + "SELECT state state, priority_code priorityCode, count count  "
          + "from ( ");
      setCondition(outOfDate, sql, "OPEN", map, params, mrSearchDTO);
      sql.append("UNION ALL ");
      setCondition(outOfDate, sql, "QUEUE", map, params, mrSearchDTO);
      sql.append("UNION ALL ");
      setCondition(outOfDate, sql, "ACTIVE", map, params, mrSearchDTO);
      sql.append("UNION ALL ");
      setCondition(outOfDate, sql, "CLOSE", map, params, mrSearchDTO);
      sql.append(") ");
      list = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(
              MrClientItem.class));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public void setCondition(boolean outOfDate, StringBuilder sql,
      String state, Map<String, String> map, Map<String, Object> params,
      MrForNocSearchDTO mrSearchDTO) {
    sql.append("    SELECT '").append(state).append("' ");
    sql.append(" as state , a.priority_code,  "
        + "    case when b.count is null then 0 else b.count end as count "
        + "  FROM mr a "
        + "  left join (  SELECT priority_code, count(*) as count FROM mr a  "
        + "               left join ( "
        + "                     select a.mr_id, max(a.change_date) as updateTime "
        + "                     from mr_his a group by mr_id "
        + "               ) b on a.mr_id = b.mr_id "
        + "               where state in (");
    sql.append("                     '").append(map.get(state)).append("',");
    sql.append("                     '").append(state).append("' ) ");
    if ("CLOSE".equals(state)) {
      if (outOfDate) {
        sql.append("  and b.updateTime >= lastest_time   ");
      } else {
        sql.append("  and b.updateTime <= lastest_time  ");
      }
    } else if (outOfDate) {
      sql.append("  and sysdate >= lastest_time   ");
    } else {
      sql.append("  and sysdate <= lastest_time  ");
    }

    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getUnitId())
        && !StringUtils.isStringNullOrEmpty(mrSearchDTO.getIsContainChild())
        && "1".equals(mrSearchDTO.getIsContainChild())) {
      sql.append("and (a.unit_execute in ( "
          + "      select cps.unit_code  "
          + "          FROM  common_gnoc.unit cps "
          + "          START WITH (cps.parent_unit_id =   "
          + "                        (select unit_id from common_gnoc.unit where  unit_id = :unitId) "
          + "                        and status =1  "
          + "              ) "
          + "          CONNECT BY parent_unit_id = PRIOR unit_id "
          + "    )  or a.unit_execute = (select unit_code from common_gnoc.unit where  unit_id = :unitId) "
          + ")");
      params.put("unitId", mrSearchDTO.getUnitId());
    } else if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getUnitId())
        && !StringUtils.isStringNullOrEmpty(mrSearchDTO.getIsContainChild())
        && "0".equals(mrSearchDTO.getIsContainChild())) {
      sql.append(
          "and ( a.unit_execute = (select unit_code from common_gnoc.unit where  unit_id = :unitId) "
              + ")");
      params.put("unitId", mrSearchDTO.getUnitId());
    }
    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getCreateTimeFrom())) {
      sql.append(" and a.created_time >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss')");
      params.put("createTimeFrom", mrSearchDTO.getCreateTimeFrom());
    }
    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getCreateTimeTo())) {
      sql.append(" and a.created_time <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss')");
      params.put("createTimeTo", mrSearchDTO.getCreateTimeTo());

    }

    sql.append("              group by priority_code "
        + "               order by priority_code) b on a.priority_code = b.priority_code "
        + "  group by  a.priority_code, b.count ");
  }

  public List<MrDTO> getListMrDTOSearch(MrSearchDTO mrSearchDTO, boolean isForMobile) {
    try {
      StringBuilder sql = new StringBuilder();
      Map<String, Object> params = new HashMap<>();
      sql.append("SELECT a.mr_id mrId, a.mr_technichcal mrTechnichcal, a.mr_title mrTitle, "
          + "         a.mr_type mrType, a.subcategory subcategory, "
          + "         a.description description, a.mr_works mrWorks, a.unit_approve unitApprove, "
          + "         a.unit_execute unitExecute, "
          + "         a.assign_to_person assignToPerson, a.person_accept personAccept, "
          + "         a.state state, "
          + "         TO_CHAR(a.earliest_time, 'dd/MM/yyyy HH24:mi:ss') earliestTime, "
          + "         TO_CHAR(a.lastest_time, 'dd/MM/yyyy HH24:mi:ss') lastestTime, "
          + "         a.interval interval, "
          + "         a.next_wo_create nextWoCreate, a.priority_code priorityCode, "
          + "         a.country country, a.region region, a.circle circle, "
          + "         a.impact impact, a.is_service_affected isServiceAffected, "
          + "         a.affected_service_id affectedServiceId, a.node_type nodeType, "
          //                    + "         mrNode.nodeName nodeName,"
          + " a.notes notes, "
          + "         a.wo_id woId, a.cr_id crId, a.mr_code mrCode,  "
          + "         a.create_person_id createPersonId,  "
          + "         TO_CHAR(a.created_time, 'dd/MM/yyyy HH24:mi:ss') createdTime  "
          //namtn add crNumber,countryName,regionName to Export function start
          + "       ,cr.cr_number crNumber"
          + "       ,cl.location_name countryName"
          + "       ,cl1.location_name regionName"
          //namtn add crNumber,countryName,regionName to Export function end
          + "    FROM mr a "
          + " left join ( "
          + "    SELECT mr_id"
          //                    + "        ,SUBSTR( rtrim (xmlagg (xmlelement(e,device_name||',')).extract ('//text()'), ' '),"
          //                    + "        1,"
          //                    + "        LENGTH(rtrim (xmlagg (xmlelement(e,device_name||',')).extract ('//text()'), ' ')) - 1) nodeName"
          + "    FROM "
          + "    (   select a.mr_id, a.ip_id, b.device_name, a.mins_id from mr_impacted_nodes a"
          + "        left join common_gnoc.infra_device b on a.device_id = b.device_id  "
          + "    ) "
          + "    GROUP BY mr_id "
          + ") mrNode on a.mr_id = mrNode.mr_id"
          //namtn add crNumber,countryName,regionName to Export function start
          + "  left join common_gnoc.cat_location cl on a.country = TO_CHAR(cl.LOCATION_ID)"
          + "  left join common_gnoc.cat_location cl1 on a.region = TO_CHAR(cl1.LOCATION_ID)"
          + "  left join open_pm.cr_created_from_other_sys mc on mc.system_id = 1 and a.mr_id = mc.object_id "
          + "  left join open_pm.cr cr on mc.cr_id = cr.cr_id ");
      //namtn add crNumber,countryName,regionName to Export function end
      if (StringUtils.isStringNullOrEmpty(mrSearchDTO.getIsApprove())) {
        //back_up
        //back_up
        //tim kiem theo don vi thuc hien khi co chon don vi trong phan auto complete
        if (StringUtils.isStringNullOrEmpty(mrSearchDTO.getUnitCode())) {
          //khi khong chon don vi trong phan auto complete => tim kiem mac dinh theo phong cua nguoi dung
          sql.append(" where 1=1 ");
          sql.append("and a.unit_execute in ( "
              + "   select cps.unit_code  "
              + "       FROM  common_gnoc.unit cps "
              + "       START WITH ( (   cps.parent_unit_id =   "
              + "                            (select unit_id from common_gnoc.unit where unit_code = 'VIETTEL') "
              + "                        or cps.parent_unit_id is null "
              + "                     ) "
              + "                     and status =1  "
              + "           ) "
              + "       CONNECT BY parent_unit_id = PRIOR unit_id "
              + " ) ");
        } else {
          sql.append(" left join common_gnoc.unit ut on ut.unit_code = a.unit_execute ");
          sql.append(" where 1=1 ");
          sql.append("and (a.unit_execute in ( "
              + "   select cps.unit_code  "
              + "       FROM  common_gnoc.unit cps "
              + "       START WITH ( (   cps.parent_unit_id = :unitCode  "
              + "                     ) "
              + "                     and status =1  "
              + "           ) "
              + "       CONNECT BY parent_unit_id = PRIOR unit_id "
              + " ) or a.unit_execute = (select unit_code from common_gnoc.unit where unit_id = :unitCode))  ");
          params.put("unitCode", mrSearchDTO.getUnitCode());
        }

      } else if ("1".equals(mrSearchDTO.getIsApprove())) {

        sql.append(" where 1=1 and (a.state = 'OPEN' or a.state = '1') and a.mr_id in ( "
            + "         with tbl as ( "
            + "         select d.unit_id,d.mr_id,d.madt_level, "
            + "             (select min(b.madt_level) from mr_approval_department b "
            + "                 where  b.mr_id = d.mr_id and b.status = 0) as num "
            + "         from mr_approval_department d "
            + "         where 1 = 1 ");
        if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getUnitId())) {
          sql.append(" and d.unit_id = :unitId ");
          params.put("unitId", mrSearchDTO.getUnitId());
        }
        sql.append("   and d.status = 0) "
            + "     select tb.mr_id   from tbl tb where tb.madt_level <= tb.num "
            + " )");
      } else if ("0".equals(mrSearchDTO.getIsApprove())) {
        sql.append(" LEFT JOIN mr_approval_department b on a.mr_id = b.mr_id ");
        sql.append(" where 1=1 and b.status = 1 ");
//                    sql.append(" and a.state <> '1' ");
        sql.append(" and a.state <> 'OPEN' and a.state <> '1' ");
        if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getUnitId())) {
          sql.append(" and b.unit_id = :unitId ");
          params.put("unitId", mrSearchDTO.getUnitId());
        }
      }

      setParams(sql, params, mrSearchDTO, isForMobile);
      sql.append(" order by a.mr_code desc ");
      return getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(MrDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  private void setParams(StringBuilder sql, Map<String, Object> params, MrSearchDTO mrSearchDTO,
      boolean isForMobile) {
    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getMrTechnichcal())) {
      sql.append(" and upper(a.mr_technichcal) like :mrTechnichcal ");
      params.put("mrTechnichcal", "%" + mrSearchDTO.getMrTechnichcal().toUpperCase() + "%");
    }
    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getMrTitle())) {
      sql.append(" and upper(a.mr_title) like :mr_title escape '\\'");
//            sql.append(" ESCAPE '");
//            sql.append(SQL_SPECIAL_CHARACTER);
//            sql.append("' ");
//            listParams.add("%" + replaceString(mrSearchDTO.getMrTitle()).toUpperCase() + "%");
      params.put("mr_title",
          "%" + mrSearchDTO.getMrTitle().trim().toUpperCase().replace("\\", "\\\\")
              .replaceAll("%", "\\\\%").replaceAll("_", "\\\\_") + "%");
    }
    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getMrType())) {
      sql.append(" and upper(a.mr_type) like :mr_type");
      params.put("mr_type", "%" + mrSearchDTO.getMrType().toUpperCase() + "%");
    }
    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getSubcategory())) {
      sql.append(" and upper(a.subcategory) like :subcategory ");
      params.put("subcategory", "%" + mrSearchDTO.getSubcategory().toUpperCase() + "%");
    }
    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getMrWorks())) {
      sql.append(" and upper(a.mr_works) like :mr_works ");
      params.put("mr_works", "%" + mrSearchDTO.getMrType().toUpperCase() + "%");
    }
    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getState())) {
      sql.append(" and upper(a.state) = :state ");
      params.put("state", mrSearchDTO.getState().toUpperCase());
    }
//        if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getCreateTimeFrom())) {
//            sql.append(" and a.created_time >= TO_DATE(?,'dd/MM/yyyy HH24:mi:ss')");
//            listParams.add(mrSearchDTO.getCreateTimeFrom());
//        }
//        if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getCreateTimeTo())) {
//            sql.append(" and a.created_time <= TO_DATE(?,'dd/MM/yyyy HH24:mi:ss')");
//            listParams.add(mrSearchDTO.getCreateTimeTo());
//        }
    if (isForMobile) {
      if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getEarliestTimeFrom())) {
        sql.append(" and a.earliest_time >= TO_DATE(:earliest_timeFrom,'dd/MM/yyyy HH24:mi:ss')");
        params.put("earliest_timeFrom", mrSearchDTO.getEarliestTimeFrom());
      }
      if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getEarliestTimeTo())) {
        sql.append(" and a.earliest_time <= TO_DATE(:earliest_timeTo,'dd/MM/yyyy HH24:mi:ss')");
        params.put("earliest_timeTo", mrSearchDTO.getEarliestTimeTo());
      }
      if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getLastestTimeFrom())) {
        sql.append(" and a.lastest_time >= TO_DATE(:lastest_timeFrom,'dd/MM/yyyy HH24:mi:ss')");
        params.put("lastest_timeFrom", mrSearchDTO.getLastestTimeFrom());
      }
      if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getLastestTimeTo())) {
        sql.append(" and a.lastest_time <= TO_DATE(:lastest_timeTo,'dd/MM/yyyy HH24:mi:ss')");
        params.put("lastest_timeTo", mrSearchDTO.getLastestTimeTo());
      }
    } else {
      if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getEarliestTimeFrom())) {
        sql.append(" and a.earliest_time >= TO_DATE(:earliest_timeFrom,'dd/MM/yyyy')");
        params.put("earliest_timeFrom", mrSearchDTO.getEarliestTimeFrom());
      }
      if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getEarliestTimeTo())) {
        sql.append(" and a.earliest_time <= TO_DATE(:earliest_timeTo,'dd/MM/yyyy')");
        params.put("earliest_timeTo", mrSearchDTO.getEarliestTimeTo());
      }
      if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getLastestTimeFrom())) {
        sql.append(" and a.lastest_time >= TO_DATE(:lastest_timeFrom,'dd/MM/yyyy')");
        params.put("lastest_timeFrom", mrSearchDTO.getLastestTimeFrom());
      }
      if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getLastestTimeTo())) {
        sql.append(" and a.lastest_time <= TO_DATE(:lastest_timeTo,'dd/MM/yyyy')");
        params.put("lastest_timeTo", mrSearchDTO.getLastestTimeTo());
      }
    }

    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getPriorityCode())) {
      sql.append(" and a.priority_code like :priority_code ");
      params.put("priority_code", "%" + mrSearchDTO.getPriorityCode().toUpperCase() + "%");
    }
    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getCountry())) {
      sql.append(" and a.country like :country ");
      params.put("country", "%" + mrSearchDTO.getCountry().toUpperCase() + "%");
    }
    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getRegion())) {
      sql.append(" and upper(a.region) like :region");
      params.put("region", "%" + mrSearchDTO.getRegion().toUpperCase() + "%");
    }
    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getCircle())) {
      sql.append(" and upper(a.circle) like :circle");
      params.put("circle", "%" + mrSearchDTO.getCircle().toUpperCase() + "%");
    }
    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getImpact())) {
      sql.append(" and upper(a.impact) like :impact ");
      params.put("impact", "%" + mrSearchDTO.getImpact().toUpperCase() + "%");
    }

    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getMrId())) {
      sql.append(" and a.mr_id = :mrId");
      params.put("mrId", mrSearchDTO.getMrId());
    }
    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getNodeType())) {
      sql.append(" and upper(a.node_type) like :node_type ");
      params.put("node_type", "%" + mrSearchDTO.getNodeType().toUpperCase() + "%");
    }
    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getNodeName())) {
      sql.append(" and upper(mrNode.nodeName) like :nodeName");
      sql.append(" ESCAPE '");
      sql.append("\\");
      sql.append("' ");
//            listParams.add("%" + replaceString(mrSearchDTO.getNodeName()).toUpperCase() + "%");
      params.put("nodeName",
          "%" + mrSearchDTO.getNodeName().trim().toUpperCase().replace("\\", "\\\\")
              .replaceAll("%", "\\\\%").replaceAll("_", "\\\\_") + "%");
    }
    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getMrCode())) {
      sql.append(" and upper(a.mr_code) like :mrCode escape '\\'");
//            sql.append(" ESCAPE '");
//            sql.append("\\");
//            sql.append("' ");
//            listParams.add("%" + replaceString(mrSearchDTO.getMrCode()).toUpperCase() + "%");
//            listParams.add("%" + replaceString(mrSearchDTO.getMrCode()).toUpperCase() + "%");
      params.put("mrCode", "%" + mrSearchDTO.getMrCode().trim().toUpperCase().replace("\\", "\\\\")
          .replaceAll("%", "\\\\%").replaceAll("_", "\\\\_") + "%");
    }

    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getInterval())) {
      sql.append(" and a.INTERVAL = :interval ");
      params.put("interval", mrSearchDTO.getInterval());
    }
    if (!StringUtils.isStringNullOrEmpty(mrSearchDTO.getCreatePersonId())) {
      sql.append(" and a.CREATE_PERSON_ID = :createPersonId ");
      params.put("createPersonId", mrSearchDTO.getCreatePersonId());
    }
  }
}
