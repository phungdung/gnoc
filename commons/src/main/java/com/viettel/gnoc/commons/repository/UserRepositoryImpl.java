package com.viettel.gnoc.commons.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UserImpactSegmentDTO;
import com.viettel.gnoc.commons.dto.UserTokenGNOC;
import com.viettel.gnoc.commons.dto.UserUpdateHisDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UnitEntity;
import com.viettel.gnoc.commons.model.UserImpactSegmentEntity;
import com.viettel.gnoc.commons.model.UserUpdateHisEntity;
import com.viettel.gnoc.commons.model.Users;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.kedb.dto.UserSmsDTO;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import viettel.passport.client.RoleToken;
import viettel.passport.client.UserToken;

/**
 * @author TungPV
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
public class UserRepositoryImpl extends BaseRepository implements UserRepository {

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  RoleUserRepository roleUserRepository;

  @Override
  public UsersEntity getUserByUserId(Long userId) {
    return getEntityManager().find(UsersEntity.class, userId);
  }

  @Override
  public UsersEntity getUserByUserName(String userName) {
    List<UsersEntity> datas = findByMultilParam(UsersEntity.class, "username", userName);
    if (datas != null && datas.size() > 0) {
      return datas.get(0);
    }
    return null;
  }

  @Override
  public UsersEntity getUserByUserNameCheckDupblicate(String userName) {
    String sql = " select * from COMMON_GNOC.USERS where LOWER(USERNAME) =:userName  ";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("userName", userName.toLowerCase());
    List<UsersEntity> datas = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(UsersEntity.class));
    if (datas != null && datas.size() > 0) {
      return datas.get(0);
    }
    return null;

  }

  @Override
  public UsersInsideDto getUserByStaffCode(String staffCode) {
    List<UsersEntity> datas = findByMultilParam(UsersEntity.class, "staffCode", staffCode);
    if (datas != null && datas.size() > 0) {
      return datas.get(0).toDTO();
    }
    return null;
  }


  @Override
  public UsersInsideDto getUserDTOByUserName(String userName) {
    StringBuffer sql = new StringBuffer();
    Map<String, Object> parameters = new HashMap<>();
    sql.append("select us.username username, us.fullname fullname, us.user_id userId, "
        + "   us.unit_id unitId, ut.unit_name unitName, "
        + "   us.mobile, us.email email "
        + "   from common_gnoc.users us"
        + "   left join common_gnoc.unit ut on us.unit_id = ut.unit_id"
        + "   where us.is_enable=1 ");
    if (StringUtils.isNotNullOrEmpty(userName)) {
      sql.append(" and lower(us.userName) = :userName ");
      parameters.put("userName", userName.toLowerCase());
    }
    List<UsersInsideDto> datas = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters, BeanPropertyRowMapper
            .newInstance(UsersInsideDto.class));
    if (datas != null && datas.size() > 0) {
      return datas.get(0);
    }
    return null;
  }

  @Override
  public UsersInsideDto getUserDTOByUserNameInnerJoint(String userName) {
    StringBuffer sql = new StringBuffer();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(
        "select a.USER_ID userId, a.USERNAME username, a.FULLNAME fullname, a.BIRTH_DAY birthDay, a.SEX sex, \n"
            + "a.UNIT_ID unitId, a.IS_ENABLE isEnable, a.EMAIL email, a.MOBILE mobile, \n"
            + "a.GROUP_OPERATE_ID groupOperateId, a.OFFICE_ID officeId, a.SHOP_ID shopId, a.SHOP_NAME shopName, a.RELATE_UNITS relateUnits, \n"
            + "a.USER_LANGUAGE userLanguage, a.USER_TIME_ZONE userTimeZone, a.STAFF_CODE staffCode, a.IS_USE_VSMART isUseVsmart,\n"
            + "a.IS_NOT_RECEIVE_MESSAGE isNotReceiveMessage, b.UNIT_NAME unitName \n"
            + "from COMMON_GNOC.USERS a inner join COMMON_GNOC.UNIT b on a.unit_id = b.unit_id where 1 = 1 ");
    if (StringUtils.isNotNullOrEmpty(userName)) {
      sql.append(" AND lower(a.USERNAME) =:userName   ");
      parameters.put("userName", userName.toLowerCase());
    }
    List<UsersInsideDto> datas = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters, BeanPropertyRowMapper
            .newInstance(UsersInsideDto.class));
    if (datas != null && datas.size() > 0) {
      return datas.get(0);
    }
    return null;
  }


  @Override
  public List<UsersInsideDto> getListUsersByCondition(List<ConditionBean> conditionBeans,
      int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new UsersEntity(), conditionBeans, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public Datatable getListUsersDTO(UsersInsideDto dto) {
    try {
      BaseDto baseDto = sqlSearch(dto);
      Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(),
          baseDto.getParameters(), dto.getPage(), dto.getPageSize(),
          UsersInsideDto.class, dto.getSortName(), dto.getSortType());
      return datatable;
    } catch (Exception err) {
      log.error(err.getMessage(), err);
      return null;
    }
  }

  @Override
  public List<UsersInsideDto> getListUsersDTOS(UsersInsideDto dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
  }

  @Override
  public Datatable getListUsersByList(UsersInsideDto dto) {
    StringBuffer sql = new StringBuffer();
    sql.append("  select us.username username, us.fullname fullname, us.user_id userId, "
        + "   us.unit_id unitId, ut.unit_name unitName, "
        + "   us.mobile, us.email email "
        + "   from common_gnoc.users us"
        + "   left join common_gnoc.unit ut on us.unit_id = ut.unit_id"
        + "   where us.is_enable=1 ");
    if (dto.getLstUserName() != null && !dto.getLstUserName().isEmpty()) {
      sql.append(" and us.userName in (:lstUserName) ");
    }
    if (dto.getLstUserId() != null && !dto.getLstUserId().isEmpty()) {
      sql.append(" and us.user_id in (:lstUserId) ");
    }

    Map<String, Object> parameters = new HashMap<>();
    if (dto.getLstUserName() != null && !dto.getLstUserName().isEmpty()) {
      parameters.put("lstUserName", dto.getLstUserName());
    }
    if (dto.getLstUserId() != null && !dto.getLstUserId().isEmpty()) {
      parameters.put("lstUserId", dto.getLstUserId());
    }

    return getListDataTableBySqlQuery(sql.toString(),
        parameters, dto.getPage(), dto.getPageSize(),
        UsersInsideDto.class, dto.getSortName(), dto.getSortType());
  }

  @Override
  public List<UsersInsideDto> getListUsersDTO(String input, String type) {
    List<ConditionBean> lstCondition = new ArrayList<>();
    if ("user".equals(type)) {
      lstCondition
          .add(new ConditionBean("username", input, Constants.NAME_EQUAL, Constants.STRING));
      lstCondition.add(new ConditionBean("isEnable", "1", Constants.NAME_EQUAL, Constants.NUMBER));
    } else {
      lstCondition.add(new ConditionBean("unitId", input, Constants.NAME_EQUAL, Constants.NUMBER));
      lstCondition.add(new ConditionBean("isEnable", "1", Constants.NAME_EQUAL, Constants.NUMBER));
    }
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    List<UsersInsideDto> lstUsersInsideDTO = getListUsersByCondition(lstCondition, 0, 1000,
        Constants.ASC,
        "username");
    if (lstUsersInsideDTO != null && !lstUsersInsideDTO.isEmpty()) {
      return lstUsersInsideDTO;
    }
    return null;
  }

  @Override
  public Double getOffsetFromUser(Long userId) {
    if (userId == null) {
      return 0D;
    }
    StringBuilder sql = new StringBuilder();
    sql.append("  select "
        + " CASE WHEN gte.timezone_offset is null THEN 0 \n"
        + " ELSE gte.timezone_offset \n"
        + " END timeOffset \n"
        + " from Common_gnoc.USERS us \n"
        + "  left join Common_gnoc.gnoc_timezone gte \n"
        + "  on gte.GNOC_TIMEZONE_ID = us.user_time_zone\n"
        + "  WHERE\n"
        + "  us.user_id = :userId "
        + "  and rownum < 2");
    Map<String, Object> params = new HashMap<>();
    params.put("userId", userId);
    List<UsersInsideDto> userDtos = getNamedParameterJdbcTemplate().query(sql.toString(),
        params, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
    try {
      return Double.parseDouble(userDtos.get(0).getTimeOffset());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return 0D;
  }

  @Override
  public Double getOffsetFromUser(String userName) {
    if (!StringUtils.isNotNullOrEmpty(userName)) {
      return 0D;
    }
    StringBuilder sql = new StringBuilder();
    sql.append("  select "
        + " CASE WHEN gte.timezone_offset is null THEN 0 \n"
        + " ELSE gte.timezone_offset \n"
        + " END timeOffset \n"
        + " from Common_gnoc.USERS us \n"
        + "  left join Common_gnoc.gnoc_timezone gte \n"
        + "  on gte.GNOC_TIMEZONE_ID = us.user_time_zone\n"
        + "  WHERE\n"
        + "  us.username = :username "
        + "  and rownum < 2");
    Map<String, Object> params = new HashMap<>();
    params.put("username", userName);
    List<UsersInsideDto> userDtos = getNamedParameterJdbcTemplate().query(sql.toString(),
        params, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
    try {
      return Double.parseDouble(userDtos.get(0).getTimeOffset());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return 0D;
  }

  private BaseDto sqlSearch(UsersInsideDto dto) {
    BaseDto baseDto = new BaseDto();
    //Start --27082020 hieunv1 bổ sung sql export
    String sqlQuery;
    Map<String, Object> parameters = new HashMap<>();
    if (dto != null && !StringUtils.isStringNullOrEmpty(dto.getCheckingExport()) && "1"
        .equals(dto.getCheckingExport())) {
      sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-users-by-user-dto-export");
    } else {
      sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-users-by-user-dto");
    }
    //End --27082020 hieunv1 bổ sung sql export
    if (dto != null) {
      if (StringUtils.isNotNullOrEmpty(dto.getSearchAll())) {
        sqlQuery += " AND LOWER(us.USERNAME) like :searchAll ESCAPE '\\' ";
        parameters.put("searchAll", StringUtils.convertLowerParamContains(dto.getSearchAll()));
      }
      if (StringUtils.isNotNullOrEmpty(dto.getUsername())) {
        sqlQuery += " AND LOWER(us.USERNAME) like :userName ESCAPE '\\' ";
        parameters.put("userName", StringUtils.convertLowerParamContains(dto.getUsername()));
      }
      if (StringUtils.isNotNullOrEmpty(dto.getFullname())) {
        sqlQuery += " AND LOWER(us.FULLNAME) like :fullname ESCAPE '\\' ";
        parameters.put("fullname", StringUtils.convertLowerParamContains(dto.getFullname()));
      }
      if (dto.getIsEnable() != null) {
        sqlQuery += " AND us.is_enable = :isEnable ";
        parameters.put("isEnable", dto.getIsEnable());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getUnitId())) {
        sqlQuery += " AND us.UNIT_ID =:unitId ";
        parameters.put("unitId", dto.getUnitId());
      }
      if (StringUtils.isNotNullOrEmpty(dto.getRelateUnits())) {
        sqlQuery += " AND to_char(us.RELATE_UNITS) IN (SELECT u.unit_id "
            + " FROM common_gnoc.unit u "
            + " WHERE LEVEL                  < 50 "
            + "  START WITH to_char(u.unit_id)       = :relateUnits "
            + "  CONNECT BY PRIOR u.unit_id = u.parent_unit_id) ";
        parameters.put("relateUnits", dto.getRelateUnits());
      }
      if (StringUtils.isNotNullOrEmpty(dto.getMobile())) {
        sqlQuery += " AND LOWER(us.mobile) like :mobile ESCAPE '\\' ";
        parameters.put("mobile", StringUtils.convertLowerParamContains(dto.getMobile()));
      }
      if (StringUtils.isNotNullOrEmpty(dto.getEmail())) {
        sqlQuery += " AND LOWER(us.email) like :email ESCAPE '\\' ";
        parameters.put("email", StringUtils.convertLowerParamContains(dto.getEmail()));
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getIsNotReceiveMessage())) {
        sqlQuery += " AND us.IS_NOT_RECEIVE_MESSAGE =:isNotReceiveMessage ";
        parameters.put("isNotReceiveMessage", dto.getIsNotReceiveMessage());
      }
      if (StringUtils.isNotNullOrEmpty(dto.getUnitName())) {
        sqlQuery += " AND LOWER(ut.unit_name) like :unitName ESCAPE '\\' ";
        parameters.put("unitName", StringUtils.convertLowerParamContains(dto.getUnitName()));
      }

      if (StringUtils.isNotNullOrEmpty(dto.getStaffCode())) {
        sqlQuery += " AND LOWER(us.staff_code) like :staffcode ESCAPE '\\' ";
        parameters.put("staffcode", StringUtils.convertLowerParamContains(dto.getStaffCode()));
      }
      if (dto.getLstRole() != null && dto.getLstRole().size() > 0) {
        sqlQuery += " AND us.USER_ID IN (select rs.USER_ID from COMMON_GNOC.ROLE_USER rs where rs.ROLE_ID in (";
        for (int i = 0; i < dto.getLstRole().size(); i++) {
          if (i != dto.getLstRole().size() - 1) {
            sqlQuery += dto.getLstRole().get(i) + ",";
          } else {
            sqlQuery += dto.getLstRole().get(i) + ") )";
          }
        }
      }
      if (dto.getLstImpact() != null && dto.getLstImpact().size() > 0) {
        sqlQuery += " AND us.USER_ID IN (select im.USER_ID from COMMON_GNOC.USER_IMPACT_SEGMENT im where im.IMPACT_SEGMENT_ID in (";
        for (int i = 0; i < dto.getLstImpact().size(); i++) {
          if (i != dto.getLstImpact().size() - 1) {
            sqlQuery += dto.getLstImpact().get(i) + ",";
          } else {
            sqlQuery += dto.getLstImpact().get(i) + " ) )";
          }
        }
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getUnitIdNew())) {
        sqlQuery += " AND us.UNIT_ID_NEW =:unitIdNew ";
        parameters.put("unitIdNew", dto.getUnitIdNew());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getDeleteGroup())) {
        sqlQuery += " AND NVL(us.DELETE_GROUP,0) =:deleteGroup ";
        parameters.put("deleteGroup", dto.getDeleteGroup());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getApproveStatus())) {
        sqlQuery += " AND us.APPROVE_STATUS =:approveStatus ";
        parameters.put("approveStatus", dto.getApproveStatus());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getCheckingAdmin()) && "1"
          .equals(dto.getCheckingAdmin())) {
        sqlQuery += " AND us.RELATE_UNITS IS NOT NULL ";
      }
      if (dto.getLstUnitLogin() != null && !dto.getLstUnitLogin().isEmpty()) {
        sqlQuery += " AND us.UNIT_ID in (select DISTINCT T1.UNIT_ID from COMMON_GNOC.UNIT T1  "
            + " START WITH T1.UNIT_ID in (:lstUnitLogin) "
            + " connect by prior  T1.UNIT_ID=T1.PARENT_UNIT_ID) ";
        parameters.put("lstUnitLogin", dto.getLstUnitLogin());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getUserId())) {
        sqlQuery += " AND us.USER_ID =:userId ";
        parameters.put("userId", dto.getUserId());
      }
    }
    sqlQuery += " order by us.USER_ID desc";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    //  }
    return baseDto;
  }

  @Override
  public List<UsersInsideDto> getListUsersByListUserId(List<Long> ids) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = "select us.* "
        + " from common_gnoc.users us"
        + " where us.is_enable=1 and us.user_id in (:ids) ";
    parameters.put("ids", ids);
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper
            .newInstance(UsersInsideDto.class));
  }

  @Override
  public ResultInSideDto updateUserTimeZone(String userId, String timeZoneId) {
    String sql = " UPDATE common_gnoc.users t\n"
        + " SET t.USER_TIME_ZONE = (select GNOC_TIMEZONE_ID from common_gnoc.gnoc_timezone where GNOC_TIMEZONE_ID = :timezoneId) \n"
        + " WHERE t.user_id = :userId \n";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("timezoneId", timeZoneId);
    parameters.put("userId", userId);
    int result = getNamedParameterJdbcTemplate().update(sql, parameters);
    if (result > 0) {
      return new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    }
    return new ResultInSideDto(null, RESULT.ERROR, RESULT.ERROR);
  }

  @Override
  public ResultInSideDto updateUserLanguage(String userId, String languageId) {
    String sql = " UPDATE common_gnoc.users t\n"
        + " SET t.USER_LANGUAGE = (select GNOC_LANGUAGE_ID from COMMON_GNOC.GNOC_LANGUAGE where GNOC_LANGUAGE_ID = :languageId) \n"
        + " WHERE t.user_id = :userId \n";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("languageId", languageId);
    parameters.put("userId", userId);
    int result = getNamedParameterJdbcTemplate().update(sql, parameters);
    if (result > 0) {
      return new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    }
    return new ResultInSideDto(null, RESULT.ERROR, RESULT.ERROR);
  }

  @Override
  public ResultInSideDto updateUserApprove(UsersInsideDto usersInsideDto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    UsersEntity usersEntity = getEntityManager().merge(usersInsideDto.toEntity());
    resultInSideDto.setId(usersEntity.getUserId());
    return resultInSideDto;
  }

  @Override
  public boolean isManagerOfUnits(Long userId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "users-check-is-manager");
    Map<String, Object> param = new HashMap<>();
    param.put("user_id", userId);
    List<UsersInsideDto> lstUser = getNamedParameterJdbcTemplate()
        .query(sqlQuery, param, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
    if (lstUser != null && lstUser.size() > 0) {
      return true;
    }
    return false;
  }

  @Override
  public UsersEntity getUserByUserIdCheck(Long userId) {
    if (userId != null && userId > 0) {
      return getEntityManager().find(UsersEntity.class, userId);
    }
    return null;
  }

  @Override
  public Long getUserId(String username) {
    String sql = "select u.user_id userId from common_gnoc.users u where lower(u.username) = :username and u.is_enable = 1";
    Map<String, Object> param = new HashMap<>();
    param.put("username", username);
    List<UsersInsideDto> lstUser = getNamedParameterJdbcTemplate()
        .query(sql, param, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
    if (lstUser != null && lstUser.size() > 0) {
      return lstUser.get(0).getUserId();
    }
    return null;
  }

  @Override
  public UsersDTO getUnitNameByUserName(String username) {
    String sql =
        "SELECT us.USERNAME username , us.FULLNAME fullname, un.UNIT_ID unitId, un.UNIT_NAME unitName, un.UNIT_CODE unitCode FROM common_gnoc.users us \n"
            + "LEFT JOIN common_gnoc.unit un on us.UNIT_ID = un.UNIT_ID\n"
            + "WHERE lower(us.username) = :username AND us.is_enable = 1";
    Map<String, Object> param = new HashMap<>();
    param.put("username", username.trim().toLowerCase());
    List<UsersDTO> lstUser = getNamedParameterJdbcTemplate()
        .query(sql, param, BeanPropertyRowMapper.newInstance(UsersDTO.class));
    if (lstUser != null && lstUser.size() > 0) {
      return lstUser.get(0);
    }
    return null;
  }

  @Override
  public String getUserName(Long userId) {
    String sql = "select u.username username from common_gnoc.users u where u.user_id = :userId";
    Map<String, Object> param = new HashMap<>();
    param.put("userId", userId);
    List<UsersInsideDto> lstUser = getNamedParameterJdbcTemplate()
        .query(sql, param, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
    if (lstUser != null && lstUser.size() > 0) {
      return lstUser.get(0).getUsername();
    }
    return null;
  }

  @Override
  public String checkAccountSubAdmin(String username) {
    StringBuilder sql = new StringBuilder(" select ITEM_ID itemId from COMMON_GNOC.cat_item ");
    sql.append(
        " where CATEGORY_ID in ( select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE = 'SUB_ADMIN_GNOC')");
    sql.append(" and STATUS = 1 ");
    Map<String, Object> param = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(username)) {
      sql.append(" AND lower(DESCRIPTION) LIKE :DESCRIPTION ESCAPE '\\' ");
      param.put("DESCRIPTION", StringUtils.convertLowerParamContains(username));
    }
    List<UsersInsideDto> lst = getNamedParameterJdbcTemplate()
        .query(sql.toString(), param, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
    if (lst != null && !lst.isEmpty()) {
      return "SUB_ADMIN";
    }
    return "NOT_SUB_ADMIN";
  }

  @Override
  public List<Users> getListUserByUnitCode(String unitCode, String allOfChildUnit) {
    if (StringUtils.isNotNullOrEmpty(allOfChildUnit) && ("true".equals(allOfChildUnit)
        || "false".equals(allOfChildUnit))) {
      Map<String, Object> parameters = new HashMap<>();
      List<Users> usersList = new ArrayList<>();
      String sqlQuery = " select b.username,b.STAFF_CODE staffCode,b.email,b.mobile,b.IS_ENABLE isEnable from COMMON_GNOC.users b where b.unit_id in (";
      if ("true".equals(allOfChildUnit)) {
        sqlQuery = sqlQuery
            + " select unit_id from common_gnoc.unit a CONNECT by prior a.UNIT_ID = a.PARENT_UNIT_ID START WITH a.UNIT_ID = ("
            + " select unit_id from common_gnoc.unit where unit_code =:unitCode and rownum =1"
            + " )";
      } else {
        sqlQuery = sqlQuery
            + " select unit_id from common_gnoc.unit where unit_code =:unitCode and rownum =1";
      }
      sqlQuery = sqlQuery + " )";
      parameters.put("unitCode", unitCode);
      List<UsersInsideDto> list = getNamedParameterJdbcTemplate()
          .query(sqlQuery, parameters, BeanPropertyRowMapper
              .newInstance(UsersInsideDto.class));
      if (list != null && list.size() > 0) {
        for (UsersInsideDto dto : list) {
          usersList.add(dto.toEntityOutside());
        }
      }
      return usersList;
    }
    return null;
  }

  @Override
  public List<UsersInsideDto> getListUserDTOByuserName(String userName) {
    if (StringUtils.isNotNullOrEmpty(userName)) {
      String sql = "select u.* from common_gnoc.users u where lower(u.username) = :userName";
      Map<String, Object> param = new HashMap<>();
      param.put("userName", userName.toLowerCase());
      return getNamedParameterJdbcTemplate()
          .query(sql, param, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
    }
    return null;
  }

  @Override
  public Long getLstUserOfUnitByRole(Long unitId, String roleCode) {
    if (unitId != null && StringUtils.isNotNullOrEmpty(roleCode)) {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-Lst-User-Of-Unit-By-Role");
      Map<String, Object> params = new HashMap<>();
      params.put("unitId", unitId);
      params.put("roleCode", roleCode);
      List<UsersInsideDto> list = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
      if (list != null && list.size() > 0) {
        return list.get(0).getUserId();
      }
    }
    return null;
  }

  @Override
  public UsersDTO getUserInfo(String userName, String staffCode) {
    List<UsersInsideDto> lstByUser = new ArrayList<>();
    UsersDTO u = null;
    if (StringUtils.isNotNullOrEmpty(userName)) {
      lstByUser = getListUserDTOByuserName(userName);
    }
    if (StringUtils.isNotNullOrEmpty(staffCode)) {
      if (lstByUser.isEmpty()) {
        String sql = "select *From common_gnoc.users where lower(STAFF_CODE) = :staffCode ";
        Map<String, Object> param = new HashMap<>();
        param.put("staffCode", staffCode.toLowerCase());
        lstByUser = getNamedParameterJdbcTemplate()
            .query(sql, param, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
      }
    }
    if (lstByUser != null && lstByUser.size() > 0) {
      u = lstByUser.get(0).toOutSideDto();
    }
    return u;
  }

  @Override
  public List<Users> getListUserOfUnit(Long unitId) {
    return findByMultilParam(Users.class, "unitId", unitId);
  }

  @Override
  public boolean checkRoleOfUser(String roleCode, Long userId) {
    String sql = "select user_id from COMMON_GNOC.ROLE_USER a where a.ROLE_ID in ("
        + " select role_id from COMMON_GNOC.roles where role_code = :roleCode"
        + " ) and a.USER_ID = :userId and a.IS_ACTIVE = 1";
    Map<String, Object> params = new HashMap<>();
    params.put("roleCode", roleCode);
    params.put("userId", userId);
    List<UsersInsideDto> list = getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
    if (list != null && !list.isEmpty()) {
      return true;
    }
    return false;
  }

  @Override
  public List<UsersInsideDto> getUsersByRoleCode(String roleCode) {
    String sql = "select ru.user_id userId"
        + " from common_gnoc.ROLE_USER ru, common_gnoc.ROLES r"
        + " where ru.ROLE_ID = r.ROLE_ID"
        + " and r.ROLE_CODE = :roleCode";
    Map<String, Object> params = new HashMap<>();
    params.put("roleCode", roleCode);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
  }

  //namtn add - kiem tra user co quyen tuong ung hay ko, input username va role
  @Override
  public boolean checkAccountHaveRole(UsersInsideDto dto) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "check-account-have-role");
      Map<String, Object> params = new HashMap<>();
      params.put("username", dto.getUsername());
      params.put("roleCode", dto.getRoleCode());

      List lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
      if (lst != null && !lst.isEmpty()) {
        return true;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return false;
  }

  @Override
  public Users getUserModelInfo(String userName, String staffCode) {
    List<Users> lst = new ArrayList<>();
    if (StringUtils.isNotNullOrEmpty(userName)) {
      String sql = "select * from common_gnoc.users where lower(username) = :userName ";
      Map<String, Object> param = new HashMap<>();
      param.put("userName", userName.toLowerCase());
      lst = getNamedParameterJdbcTemplate()
          .query(sql, param, BeanPropertyRowMapper.newInstance(Users.class));
    }
    if (lst == null || lst.size() == 0) {
      if (StringUtils.isNotNullOrEmpty(staffCode)) {
        String sql = "select * from common_gnoc.users where lower(STAFF_CODE) = :staffCode ";
        Map<String, Object> param = new HashMap<>();
        param.put("staffCode", staffCode.toLowerCase());
        lst = getNamedParameterJdbcTemplate()
            .query(sql, param, BeanPropertyRowMapper.newInstance(Users.class));
      }
    }
    if (lst != null && lst.size() > 0) {
      return lst.get(0);
    }
    return null;
  }

  public String getUnitParentForApprove(String type, String unitId) {
    String sql = "";
    if ("1".equals(type)) {
      sql = " select unit_id as unitId, is_committee as isCommittee from common_gnoc.unit where unit_id = :unitId ";
    } else if ("2".equals(type)) {
      sql =
          " select  b1.parent_unit_id as unitId , b2.is_committee as isCommittee from common_gnoc.unit b1  "
              + " join common_gnoc.unit b2  "
              + " on b1.parent_unit_id = b2.unit_id  "
              + " where b1.unit_id = :unitId ";
    }
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("unitId", unitId);
    List<UnitDTO> unitDTOList = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(UnitDTO.class));
    String unit = "";
    if (unitDTOList != null && !unitDTOList.isEmpty()) {
      if (StringUtils.isStringNullOrEmpty(unitDTOList.get(0).getIsCommittee())) {
        return null;
      }
      unit =
          unitDTOList.get(0).getUnitId() != null ? unitDTOList.get(0).getUnitId().toString() : null;
      if (1L == unitDTOList.get(0).getIsCommittee()) {
        //de quy thi lay parent
        return getUnitParentForApprove("2", unit);
      }
      return unit;
    }
    return null;
  }

  @Override
  public List<ImpactSegmentDTO> getImpactSegment(String system, String active) {
    String sql = "select * from OPEN_PM.IMPACT_SEGMENT ";
    sql += "where APPLIED_SYSTEM=:system and is_active=:active ";
    sql += "order by impact_segment_name";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("system", system);
    parameters.put("active", active);
    List<ImpactSegmentDTO> lstSeg = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(ImpactSegmentDTO.class));
    if (lstSeg != null && !lstSeg.isEmpty()) {
      return lstSeg;
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteUser(Long userId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      UserToken userToken = ticketProvider.getUserToken();
      if (!checkRoleUser(userToken)) {
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage(I18n.getLanguage("employee.role.disable.delete"));
        return resultInSideDto;
      }
      UsersEntity usersEntity = getEntityManager().find(UsersEntity.class, userId);
      if (usersEntity != null) {
        getEntityManager().remove(usersEntity);
        resultInSideDto.setKey(RESULT.SUCCESS);

        //Luu lich su
        UserUpdateHisDTO userUpdateHisDTO = new UserUpdateHisDTO();
        userUpdateHisDTO.setUserAction(userToken.getUserID());
        userUpdateHisDTO.setActionName("DELETE");
        userUpdateHisDTO.setUserId(userId);
        userUpdateHisDTO.setUpdateTime(new Date());
        getEntityManager().persist(userUpdateHisDTO.toEntity());
      }
      return resultInSideDto;
    } catch (Exception err) {
      resultInSideDto.setKey(RESULT.FAIL);
      log.error(err.getMessage(), err);
      return resultInSideDto;
    }
  }

  @Override
  public ResultInSideDto addUser(UsersInsideDto usersInsideDto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      UserToken userToken = ticketProvider.getUserToken();
      if (!checkRoleUser(userToken)) {
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage(I18n.getLanguage("employee.role.disable.add"));
        return resultInSideDto;
      }
      UsersEntity usersEntity = getUserByUserName(usersInsideDto.getUsername());
      if (usersEntity != null) {
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage(I18n.getLanguage("employee.err.exist"));
        return resultInSideDto;
      }
      if (checkStaffCode(usersInsideDto.getStaffCode())) {
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage(I18n.getLanguage("employee.staffCode.isExits"));
        return resultInSideDto;
      }
      if (usersInsideDto.getIsEnable() == null) {
        usersInsideDto.setIsEnable(1L);
      }
      getEntityManager().persist(usersInsideDto.toEntity());
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
      UserImpactSegmentDTO userImpactSegmentDTO = new UserImpactSegmentDTO();
      UsersEntity user = getUserByUserName(usersInsideDto.getUsername());

      //Add user impact
      List<String> lstImpact = usersInsideDto.getLstImpact();
      if (lstImpact != null && !lstImpact.isEmpty()) {
        for (String i : lstImpact) {
          userImpactSegmentDTO.setUserId(user.getUserId().toString());
          userImpactSegmentDTO.setImpactSegmentId(i);
          resultInSideDto = addUserImpactSegment(userImpactSegmentDTO);
        }
      }
      //Luu lich su user
      if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
        UsersInsideDto oldUser = new UsersInsideDto();
        UserUpdateHisDTO userUpdateHisDTO = new UserUpdateHisDTO();
        userUpdateHisDTO.setUserAction(userToken.getUserID());
        userUpdateHisDTO.setUserId(user.getUserId());
        userUpdateHisDTO.setUpdateTime(new Date());
        userUpdateHisDTO.setActionName("ADD");
        oldUser.setLstImpact(new ArrayList<>());
        oldUser.setLstRole(new ArrayList<>());
        userUpdateHisDTO.setContent(CompareUserObject(oldUser, usersInsideDto));
        getEntityManager().persist(userUpdateHisDTO.toEntity());
      }
      return resultInSideDto;
    } catch (Exception err) {
      resultInSideDto.setKey(RESULT.FAIL);
      log.error(err.getMessage(), err);
      return resultInSideDto;
    }
  }

  @Override
  public UsersInsideDto getUserDetaiById(Long id) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-detail-user-by-id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("userId", id);
    List<UsersInsideDto> lstUser = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
    if (lstUser != null && !lstUser.isEmpty()) {
      List<String> lstType = new ArrayList<>();
      String typeCode = lstUser.get(0).getTypeCode();
      if (typeCode != null) {
        String[] lstCode = typeCode.split(",");
        for (String code : lstCode) {
          String sqlItem = "select ITEM_ID from COMMON_GNOC.CAT_ITEM WHERE ITEM_CODE = :itemCode";
          parameters.put("itemCode", code.trim());
          List<CatItemDTO> catItemDTOS = getNamedParameterJdbcTemplate()
              .query(sqlItem, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
          if (catItemDTOS != null && !catItemDTOS.isEmpty()) {
            lstType.add(catItemDTOS.get(0).getItemId().toString());
          }
        }
      }
      lstUser.get(0).setLstType(lstType);
      return lstUser.get(0);
    }
    return null;
  }

  @Override
  public ResultInSideDto updateUser(UsersInsideDto usersInsideDto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      UsersInsideDto oldUser = getUserDetaiById(usersInsideDto.getUserId());
      List<UsersEntity> lst = findByMultilParam(UsersEntity.class, "userId",
          usersInsideDto.getUserId());
      if (lst != null && !lst.isEmpty() && StringUtils.isNotNullOrEmpty(lst.get(0).getStaffCode())
          && StringUtils.isNotNullOrEmpty(usersInsideDto.getStaffCode()) && !lst.get(0)
          .getStaffCode().toLowerCase().equals(usersInsideDto.getStaffCode().toLowerCase())) {
        if (checkStaffCode(usersInsideDto.getStaffCode())) {
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto.setMessage(I18n.getLanguage("employee.staffCode.isExits"));
          return resultInSideDto;
        }
      }
      UsersEntity usersEntity = getEntityManager().merge(usersInsideDto.toEntity());

      //Luu lich su user
      UserToken userToken = ticketProvider.getUserToken();
      UserUpdateHisDTO userUpdateHisDTO = new UserUpdateHisDTO();
      userUpdateHisDTO.setUserAction(userToken.getUserID());
      userUpdateHisDTO.setUserId(usersInsideDto.getUserId());
      userUpdateHisDTO.setUpdateTime(new Date());
      userUpdateHisDTO.setActionName("UPDATE");
      if (oldUser != null) {
        oldUser.setTypeCodeName(oldUser.getTypeCode());
      }
      //Compare listRole
      if (!CompareListString(oldUser.getLstRole(), usersInsideDto.getLstRole())) {
        usersInsideDto.setLstRole(oldUser.getLstRole());
      }
      //Compare listImpact
      if (!CompareListString(oldUser.getLstImpact(), usersInsideDto.getLstImpact())) {
        usersInsideDto.setLstImpact(oldUser.getLstImpact());
      }
      String content = CompareUserObject(oldUser, usersInsideDto);
      if (content != null && !content.equals("{}")) {
        userUpdateHisDTO.setContent(content);
        getEntityManager().persist(userUpdateHisDTO.toEntity());
        resultInSideDto.setKey(RESULT.SUCCESS);
      }

      //Update user impact
      List<String> lstImpact = usersInsideDto.getLstImpact();
      if (lstImpact != null && !lstImpact.isEmpty()) {
        UserImpactSegmentDTO userImpactSegmentDTO = new UserImpactSegmentDTO();
        List<UserImpactSegmentEntity> lstUserImpactSegment = findByMultilParam(
            UserImpactSegmentEntity.class, "userId",
            usersInsideDto.getUserId());
        if (lstUserImpactSegment != null && !lstUserImpactSegment.isEmpty()) {
          for (UserImpactSegmentEntity user : lstUserImpactSegment) {
            if (!lstImpact.contains(user.getImpactSegmentId().toString())) {
              getEntityManager().remove(user);
            } else {
              lstImpact.remove(user.getImpactSegmentId().toString());
            }
          }
          for (String impactSegment : lstImpact) {
            userImpactSegmentDTO.setUserId(usersInsideDto.getUserId().toString());
            userImpactSegmentDTO.setImpactSegmentId(impactSegment);
            resultInSideDto = addUserImpactSegment(userImpactSegmentDTO);
          }
        } else {
          for (String impactSegment : lstImpact) {
            userImpactSegmentDTO.setUserId(usersInsideDto.getUserId().toString());
            userImpactSegmentDTO.setImpactSegmentId(impactSegment);
            resultInSideDto = addUserImpactSegment(userImpactSegmentDTO);
          }
        }
      } else if (lstImpact != null && lstImpact.isEmpty()) {
        List<UserImpactSegmentEntity> lstUserImpactSegment = findByMultilParam(
            UserImpactSegmentEntity.class, "userId",
            usersInsideDto.getUserId());
        for (UserImpactSegmentEntity userImpact : lstUserImpactSegment) {
          getEntityManager().remove(userImpact);
        }
      }

      resultInSideDto.setId(usersEntity.getUserId());
      resultInSideDto.setKey(RESULT.SUCCESS);
      return resultInSideDto;
    } catch (Exception err) {
      resultInSideDto.setKey(RESULT.FAIL);
      log.error(err.getMessage(), err);
      return resultInSideDto;
    }
  }

  @Override
  public List<UsersInsideDto> listUserByDTO(UsersInsideDto usersInsideDto) {
    try {
      BaseDto baseDto = sqlSearch(usersInsideDto);
      List<UsersInsideDto> lstUserInside = getNamedParameterJdbcTemplate()
          .query(baseDto.getSqlQuery(), baseDto.getParameters(),
              BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
      return lstUserInside;
    } catch (Exception err) {
      log.error(err.getMessage(), err);
    }
    return null;
  }

  @Override
  public ResultInSideDto insertOrUpdateUser(UsersInsideDto usersInsideDto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    Map<String, Object> parameters = new HashMap<>();
    try {
      String unitCode = usersInsideDto.getUnitCode();
      parameters.put("unitCode", unitCode);
      UserToken userToken = ticketProvider.getUserToken();
      String sql = "select unit_id, unit_name from COMMON_GNOC.UNIT where unit_code = :unitCode";
      List<UnitDTO> unitDTO = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(UnitDTO.class));
      if (unitDTO != null && !unitDTO.isEmpty()) {
        usersInsideDto.setUnitId(unitDTO.get(0).getUnitId());
        usersInsideDto.setUnitName(unitDTO.get(0).getUnitName());
      }
      if (usersInsideDto.getAction() == 0L) {
        if (!checkRoleUser(userToken)) {
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto.setMessage(I18n.getLanguage("employee.role.disable.import"));
          return resultInSideDto;
        }
        getEntityManager().persist(usersInsideDto.toEntity());
        resultInSideDto.setKey(RESULT.SUCCESS);

        //Luu lich su user
        if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
          UsersEntity user = getUserByUserName(usersInsideDto.getUsername());
          UsersInsideDto oldUser = new UsersInsideDto();
          UserUpdateHisDTO userUpdateHisDTO = new UserUpdateHisDTO();
          userUpdateHisDTO.setUserAction(userToken.getUserID());
          userUpdateHisDTO.setUserId(user.getUserId());
          userUpdateHisDTO.setUpdateTime(new Date());
          userUpdateHisDTO.setActionName("IMPORT_ADD");
          if (usersInsideDto.getRoleCode() != null) {
            List<String> lstRoleImport = new ArrayList<>();
            String[] lstRoleCode = usersInsideDto.getRoleCode().trim().split(",");
            for (int i = 0; i < lstRoleCode.length; i++) {
              RolesDTO role = roleUserRepository.findRoleByCode(lstRoleCode[i]);
              if (role != null) {
                lstRoleImport.add(role.getRoleId());
              }
            }
            usersInsideDto.setLstRole(lstRoleImport);
          }
          userUpdateHisDTO.setContent(CompareUserObject(oldUser, usersInsideDto));
          getEntityManager().persist(userUpdateHisDTO.toEntity());
        }
      } else if (usersInsideDto.getAction() == 1L) {
        UsersInsideDto oldUser = getUserDetaiById(usersInsideDto.getUserId());
        usersInsideDto.setUsername(oldUser.getUsername());
        if (checkStaffCode(usersInsideDto.getStaffCode())) {
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto.setMessage(I18n.getLanguage("employee.staffCode.isExits"));
        }
        getEntityManager().merge(usersInsideDto.toEntity());
        resultInSideDto.setKey(RESULT.SUCCESS);

        //Luu lich su user
        if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
          UserUpdateHisDTO userUpdateHisDTO = new UserUpdateHisDTO();
          userUpdateHisDTO.setUserAction(userToken.getUserID());
          userUpdateHisDTO.setUserId(usersInsideDto.getUserId());
          userUpdateHisDTO.setUpdateTime(new Date());
          userUpdateHisDTO.setActionName("IMPORT_UPDATE");
          if (usersInsideDto.getRoleCode() != null) {
            List<String> lstRoleImport = new ArrayList<>();
            String[] lstRoleCode = usersInsideDto.getRoleCode().trim().split(",");
            for (int i = 0; i < lstRoleCode.length; i++) {
              RolesDTO role = roleUserRepository.findRoleByCode(lstRoleCode[i]);
              if (role != null) {
                lstRoleImport.add(role.getRoleId());
              }
            }
            usersInsideDto.setLstRole(lstRoleImport);
          }
          //Compare listRole
          if (!CompareListString(oldUser.getLstRole(), usersInsideDto.getLstRole())) {
            usersInsideDto.setLstRole(oldUser.getLstRole());
          }
          userUpdateHisDTO.setContent(CompareUserObject(oldUser, usersInsideDto));
          getEntityManager().persist(userUpdateHisDTO.toEntity());
        }
      }
      return resultInSideDto;
    } catch (Exception err) {
      resultInSideDto.setKey(RESULT.FAIL);
      log.error(err.getMessage(), err);
      return resultInSideDto;
    }
  }

  public ResultInSideDto addUserImpactSegment(UserImpactSegmentDTO userImpactSegmentDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      getEntityManager().persist(userImpactSegmentDTO.toEntity());
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
      return resultInSideDto;
    } catch (Exception err) {
      resultInSideDto.setKey(RESULT.FAIL);
      log.error(err.getMessage(), err);
      return resultInSideDto;
    }
  }

  //get typecode user
  public String getTypeCodeUser(Long userId) {
    String sql = "select * from ONE_TM.USER_SMS where user_id = :userId";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("userId", userId);
    List<UserSmsDTO> lstUserSms = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(UserSmsDTO.class));
    String result = new String();
    if (lstUserSms != null && !lstUserSms.isEmpty()) {
      String typeCode = lstUserSms.get(0).getTypeCode();
      if (typeCode != null) {
        String[] arrayCode = typeCode.split(",");
        for (String code : arrayCode) {
          code = code.trim();
          String sqlQuery = "select a.ITEM_NAME from COMMON_GNOC.CAT_ITEM a WHERE a.ITEM_CODE = :typeCode";
          parameters.put("typeCode", code);
          List<CatItemDTO> catItemDTO = getNamedParameterJdbcTemplate()
              .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
          if (catItemDTO != null && !catItemDTO.isEmpty()) {
            result = result + catItemDTO.get(0).getItemName() + ",";
          }
        }
      }
      if (result.length() > 0) {
        result = result.substring(0, result.length() - 1);
      }
      return result;
    }
    return null;
  }

  //Check role user
  public Boolean checkRoleUser(UserToken userToken) {
    Boolean check = false;
    try {
      List<RoleToken> lstRole = userToken.getRolesList();
      if (lstRole != null && !lstRole.isEmpty()) {
        for (RoleToken role : lstRole) {
          if (role.getRoleCode().equals("GNOC_ADMIN")) {
            check = true;
          }
        }
      }
    } catch (Exception err) {
      log.error(err.getMessage(), err);
    }
    return check;
  }

  //Check staff code
  @Override
  public Boolean checkStaffCode(String staffCode) {
    Boolean check = false;
    if (StringUtils.isNotNullOrEmpty(staffCode)) {
      String sql = " select USER_ID userId,USERNAME username from COMMON_GNOC.USERS where LOWER(STAFF_CODE) =:staffCode  ";
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("staffCode", staffCode);
      List<UsersInsideDto> lstUser = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
      if (lstUser != null && !lstUser.isEmpty()) {
        check = true;
      }
    }
    return check;
  }

  @Override
  public UnitEntity getUnitByCode(String unitCode) {
    try {
      List<UnitEntity> lstUnit = findByMultilParam(UnitEntity.class, "unitCode", unitCode);
      if (lstUnit != null && !lstUnit.isEmpty()) {
        return lstUnit.get(0);
      }
    } catch (Exception err) {
      log.error(err.getMessage(), err);
    }
    return null;
  }

  @Override
  public Datatable getListUserHistory(UserUpdateHisDTO userUpdateHisDTO) {
    try {
      Map<String, Object> parameters = new HashMap<>();
      String sqlQuery = "SELECT " +
          "us.ID id " +
          ", us.USER_ID userId " +
          ", us.USER_ACTION userAction " +
          ", us.UPDATE_TIME updateTime " +
          ", us.ACTION_NAME actionName " +
          ", us.CONTENT content " +
          ", us1.USERNAME userActionName " +
          ", us2.USERNAME userIdName " +
          "FROM COMMON_GNOC.USER_UPDATE_HIS us " +
          "    INNER JOIN  common_gnoc.users us1 ON us1.USER_ID = us.USER_ACTION " +
          "    INNER JOIN  common_gnoc.users us2 ON us2.USER_ID = us.USER_ID " +
          "where us.USER_ID = :userId " +
          "order by us.update_time desc";
      parameters.put("userId", userUpdateHisDTO.getUserId());
      Datatable datatable = getListDataTableBySqlQuery(sqlQuery, parameters,
          userUpdateHisDTO.getPage(), userUpdateHisDTO.getPageSize(), UserUpdateHisDTO.class,
          userUpdateHisDTO.getSortName(),
          userUpdateHisDTO.getSortType());
      return datatable;
    } catch (Exception err) {
      log.error(err.getMessage(), err);
      return null;
    }
  }

  @Override
  public UserUpdateHisDTO findUserHistoryById(Long hisId) {
    try {
      UserUpdateHisEntity userUpdateHisEntity = getEntityManager()
          .find(UserUpdateHisEntity.class, hisId);
      if (userUpdateHisEntity != null) {
        return userUpdateHisEntity.toDTO();
      }
    } catch (Exception err) {
      log.error(err.getMessage(), err);
    }
    return null;
  }

  @Override
  public List<UsersInsideDto> getLstUsersByUserNameOrStaffCode(List<String> lstUserName,
      List<String> lstStaffCode) {
    if ((lstUserName == null || lstUserName.size() == 0) && (lstStaffCode == null
        || lstStaffCode.size() == 0)) {
      return null;
    }
    String sql = " select USER_ID userId,USERNAME username,STAFF_CODE staffCode from COMMON_GNOC.USERS where 1=1  ";
    Map<String, Object> parameters = new HashMap<>();
    if (lstUserName != null && !lstUserName.isEmpty()) {
      sql += " AND LOWER(USERNAME) in (:lstUserName) ";
      parameters.put("lstUserName", lstUserName);
    }
    if (lstStaffCode != null && !lstStaffCode.isEmpty()) {
      sql += " AND LOWER(STAFF_CODE) in (:lstStaffCode) ";
      parameters.put("lstStaffCode", lstStaffCode);
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
  }

  private String CompareUserObject(UsersInsideDto oldUser, UsersInsideDto newUser)
      throws JsonProcessingException {
    SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
    Map<String, Object> parameters = new HashMap<>();
    List<String> keys = Arrays
        .asList("username", "staffCode", "mobile", "fullname", "birthDay", "email", "unitName",
            "isEnable",
            "userLanguage", "isNotReceiveMessage", "lstRole", "lstImpact", "message",
            "typeCodeName");
    Field[] Field1 = oldUser.getClass().getDeclaredFields();
    Field[] Field2 = newUser.getClass().getDeclaredFields();
    try {
      for (Field f1 : Field1) {
        f1.setAccessible(true);
        if (keys.contains(f1.getName())) {
          for (Field f2 : Field2) {
            f2.setAccessible(true);
            if (f1.getName().equals(f2.getName())) {
              Object v1 = f1.get(oldUser);
              Object v2 = f2.get(newUser);
              if (f1.getName().equals("birthDay")) {
                if (v1 != null) {
                  v1 = DateTimeUtils.converClientDateToServerDate(dfm.format(v1), 0.0);
                }
                if (v2 != null) {
                  v2 = DateTimeUtils.converClientDateToServerDate(dfm.format(v2), 0.0);
                }
              }
              if (v1 != null && v2 != null && !v2.equals(v1)) {
                String value =
                    "{" + '"' + "oldValue" + '"' + ":" + '"' + v1 + '"' + "," + '"' + "newValue" +
                        '"' + ":" + '"' + v2 + '"' + "}";
                parameters.put(f1.getName(), value);
              } else if (v1 == null && v2 != null) {
                String value =
                    "{" + '"' + "oldValue" + '"' + ":" + '"' + "null" + '"' + "," + '"' + "newValue"
                        +
                        '"' + ":" + '"' + v2 + '"' + "}";
                parameters.put(f1.getName(), value);
              } else if (v1 != null && v2 == null) {
                String value =
                    "{" + '"' + "oldValue" + '"' + ":" + '"' + v1 + '"' + "," + '"' + "newValue" +
                        '"' + ":" + '"' + "null" + '"' + "}";
                parameters.put(f1.getName(), value);
              }
            }
          }
        }
      }
    } catch (Exception err) {
      log.error(err.getMessage(), err);
    }
    ObjectMapper mapper = new ObjectMapper();
    String jsonString = mapper.writeValueAsString(parameters);
    return jsonString;
  }

  private Boolean CompareListString(List<String> oldList, List<String> newList) {
    if (oldList == null) {
      oldList = new ArrayList<>();
    }
    if (newList == null) {
      newList = new ArrayList<>();
    }
    if (oldList.size() != newList.size()) {
      return true;
    } else {
      for (String value : newList) {
        if (!oldList.contains(value)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public List<UsersDTO> getUserDTO() {
    String sql;
    Map<String, Object> parameters = new HashMap<>();
    try {
      sql = " SELECT user_id userId, "
          + "  username, "
          + "  sex, "
          + "  is_enable isEnable, "
          + "  staff_code staffCode, "
          + "  is_use_vsmart isUseVsmart, "
          + "  user_time_zone userTimeZone, "
          + "  unit_id unitId, "
          + "  email, "
          + "  mobile, "
          + "  user_language userLanguage, "
          + "  relate_units relateUnits, "
          + "  shop_name shopName, "
          + "  shop_id shopId, "
          + "  office_id officeId, "
          + "  group_operate_id groupOperateId, "
          + "  fullname, "
          + "  birth_day birthDay "
          + " FROM common_gnoc.users ";
      return getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(UsersDTO.class));
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }
    return null;
  }

  @Override
  public UsersDTO getUserDTOByUsernameLower(String username) {
    String sql;
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("username", String.valueOf(username).toLowerCase());
    try {
      sql = " SELECT user_id userId, "
          + "  username, "
          + "  sex, "
          + "  is_enable isEnable, "
          + "  staff_code staffCode, "
          + "  is_use_vsmart isUseVsmart, "
          + "  user_time_zone userTimeZone, "
          + "  unit_id unitId, "
          + "  email, "
          + "  mobile, "
          + "  user_language userLanguage, "
          + "  relate_units relateUnits, "
          + "  shop_name shopName, "
          + "  shop_id shopId, "
          + "  office_id officeId, "
          + "  group_operate_id groupOperateId, "
          + "  fullname, "
          + "  birth_day birthDay "
          + " FROM common_gnoc.users where lower(USERNAME) = :username and is_enable = 1 ";
      List<UsersDTO> usersDTOS = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(UsersDTO.class));
      if (usersDTOS != null && !usersDTOS.isEmpty()) {
        return usersDTOS.get(0);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }
    return null;
  }

  @Override
  public List<UsersInsideDto> getListUserByUnitId(Long unitId) {
    try {
      Map<String, Object> parameters = new HashMap<>();
      String sql = " SELECT us.unit_id unitId,\n"
          + "  us.user_id userId,\n"
          + "  us.mobile mobile,\n"
          + "  us.username username,\n"
          + "  us.user_language userLanguage,\n"
          + "  un.unit_name unitName\n"
          + "FROM COMMON_GNOC.users us\n"
          + "INNER JOIN COMMON_GNOC.unit un\n"
          + "ON un.unit_id    = us.unit_id\n"
          + "WHERE 1          =1\n"
          + "AND us.is_enable =1\n"
          + "AND un.status    =1\n"
          + "AND un.unit_id   =:unitId ";
      parameters.put("unitId", unitId);
      List<UsersInsideDto> lstUsers = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
      if (lstUsers != null && !lstUsers.isEmpty()) {
        return lstUsers;
      }
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }
    return null;
  }

  @Override
  public UserTokenGNOC getUserInfor(String userName) {
    UserTokenGNOC userTokenGNOC = new UserTokenGNOC();
    try {
      if (StringUtils.isNotNullOrEmpty(userName)) {
        Map<String, Object> parameters = new HashMap<>();
        String sql = "  select us.username userName, us.fullname fullName, us.user_id userId, "
            + "   us.unit_id unitId, ut.unit_code unitCode, ut.unit_name unitName, "
            + "   us.mobile, us.user_time_zone userTimeZone "
            + "   from common_gnoc.users us"
            + "   left join common_gnoc.unit ut on us.unit_id = ut.unit_id"
            + "   where us.is_enable=1 and lower(us.username) =:userName ";
        parameters.put("userName", userName.toLowerCase());
        List<UserTokenGNOC> lst = getNamedParameterJdbcTemplate()
            .query(sql, parameters, BeanPropertyRowMapper.newInstance(UserTokenGNOC.class));
        if (lst != null && lst.size() > 0) {
          userTokenGNOC = lst.get(0);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return userTokenGNOC;
  }

  @Override
  public List<UsersInsideDto> search(UsersInsideDto usersInsideDto, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    return onSearchEntity(UsersEntity.class, usersInsideDto, rowStart, maxRow, sortType,
        sortFieldList);
  }
}
