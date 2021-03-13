package com.viettel.gnoc.commons.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.model.UserCommentEntity;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.ROLE;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wfm.dto.ConfigPropertyDTO;
import com.viettel.gnoc.wo.dto.CatLocationDTO;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

/**
 * @author TungPV
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
@Transactional
public class CommonRepositoryImpl extends BaseRepository implements CommonRepository {

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  RoleUserRepository roleUserRepository;

  @Autowired
  UserRepository userRepository;

  @Override
  public ResultInSideDto getDBSysDate() {
    ResultInSideDto data = new ResultInSideDto();
    String strQuery = "SELECT sysdate() systemDate FROM DUAL";

    List<ResultInSideDto> list = getNamedParameterJdbcTemplate()
        .query(strQuery, BeanPropertyRowMapper.newInstance(ResultInSideDto.class));

    if (list != null && list.size() > 0) {
      data = list.get(0);
    }
    return data;
  }

  @Override
  public List<GnocTimezoneDto> getAllGnocTimezone() {
    UserToken userToken = ticketProvider.getUserToken();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-all-gnoc-timezone");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_username", userToken.getUserName());
    return getNamedParameterJdbcTemplate().query(sqlQuery, parameters,
        BeanPropertyRowMapper.newInstance(GnocTimezoneDto.class));
  }

  @Override
  public List<GnocLanguageDto> getAllGnocLanguage() {
    UserToken userToken = ticketProvider.getUserToken();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-all-gnoc-language");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_username", userToken.getUserName());
    return getNamedParameterJdbcTemplate().query(sqlQuery, parameters,
        BeanPropertyRowMapper.newInstance(GnocLanguageDto.class));
  }

  @Override
  public List<DataItemDTO> getListCombobox(ObjectSearchDto objectSearchDto) {
    UserToken userToken = ticketProvider.getUserToken();
    String sqlQuery = "";
    Map<String, Object> parameters = new HashMap<>();
    switch (objectSearchDto.getModuleName()) {
      case "GNOC_PT_STATION_CODE":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-list-station-code-of-pt");
        String nation = getNationFromUnit(userToken.getDeptId());
        parameters.put("nationCode", nation);
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery + " and lower(station_code) like :p_value escape '\\' ";
          parameters.put("p_value", StringUtils.convertLowerParamContains(objectSearchDto.getParam()));
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "GNOC_OD_TYPE":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "od-type-item-by-code-or-name");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(ot.OD_TYPE_CODE) LIKE :p_param ESCAPE '\\' OR lower(ot.OD_TYPE_NAME) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          sqlQuery = sqlQuery + " AND ot.OD_GROUP_TYPE_ID = :p_parent ";
          parameters.put("p_parent", objectSearchDto.getParent());
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND ot.OD_TYPE_ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "GNOC_WO_MATERIAL":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "wo-material-item-by-code-or-name");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(wm.MATERIAL_CODE) LIKE :p_param ESCAPE '\\' OR lower(wm.MATERIAL_NAME) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
//        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
//          sqlQuery = sqlQuery + " AND wm.MATERIAL_GROUP_ID = :p_parent ";
//          parameters.put("p_parent", objectSearchDto.getParent());
//        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND wm.MATERIAL_ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "GNOC_WO_TYPE":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "wo-type-item-by-code-or-name");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(wt.WO_TYPE_CODE) LIKE :p_param ESCAPE '\\' OR lower(wt.WO_TYPE_NAME) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          sqlQuery = sqlQuery + " AND wt.WO_GROUP_TYPE = :p_parent ";
          parameters.put("p_parent", objectSearchDto.getParent());
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND wt.WO_TYPE_ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        sqlQuery = sqlQuery + " ORDER BY wt.WO_TYPE_NAME ASC";
        break;
      case "GNOC_WO_TYPE_FOR_WO":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "wo-type-item-for-wo-by-code-or-name");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(wt.WO_TYPE_CODE) LIKE :p_param ESCAPE '\\' OR lower(wt.WO_TYPE_NAME) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          sqlQuery = sqlQuery + " AND wt.WO_GROUP_TYPE = :p_parent ";
          parameters.put("p_parent", objectSearchDto.getParent());
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND wt.WO_TYPE_ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        sqlQuery = sqlQuery + " ORDER BY wt.WO_TYPE_NAME ASC";
        break;
      case "GNOC_WO_CD_GROUP":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "wo-cd-group-item-by-code-or-name");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(wcg.WO_GROUP_CODE) LIKE :p_param ESCAPE '\\' OR lower(wcg.WO_GROUP_NAME) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          sqlQuery = sqlQuery + " AND wcg.GROUP_TYPE_ID = :p_parent ";
          parameters.put("p_parent", objectSearchDto.getParent());
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND wcg.WO_GROUP_ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "GNOC_DICH_VU_WO_HELP":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "dich-vu-sr-item-by-code-or-name");
        parameters.put("p_config_group", "DICH_VU_WO_HELP");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(b1.service_code) LIKE :p_param ESCAPE '\\' OR lower(b1.service_name) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND b1.service_code = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "GNOC_CR_GROUP_UNIT_CODE":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON,
                "wo-group-unit-code-item-by-code-or-name");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(GROUP_UNIT_CODE) LIKE :p_param ESCAPE '\\' OR lower(GROUP_UNIT_NAME) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND GROUP_UNIT_ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "GNOC_CR_GROUP_UNIT":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "wo-group-unit-item-by-code-or-name");
        parameters.put("p_leeLocale", I18n.getLocale());
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(GROUP_UNIT_CODE) LIKE :p_param ESCAPE '\\' OR lower(GROUP_UNIT_NAME) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND GROUP_UNIT_ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "GNOC_CR_IMPACT_SEGMENT":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON,
                "cr-impact-segment-item-by-code-or-name");
        parameters.put("p_leeLocale", I18n.getLocale());

        sqlQuery = sqlQuery
            + " AND APPLIED_SYSTEM = 2 ";

        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(IMPACT_SEGMENT_CODE) LIKE :p_param ESCAPE '\\' OR lower(IMPACT_SEGMENT_NAME) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND IMPACT_SEGMENT_ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        sqlQuery += " ORDER BY IMPACT_SEGMENT_NAME asc";

        break;
      case "GNOC_CR_IMPACT_FRAME":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "cr-impact-frame-item-by-code-or-name");
        parameters.put("p_leeLocale", I18n.getLocale());
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(IFE_CODE) LIKE :p_param ESCAPE '\\' OR lower(IFE_NAME) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND IFE_ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "GNOC_CR_DEVICE_TYPES":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "cr-device-types-item-by-code-or-name");
        parameters.put("p_leeLocale", I18n.getLocale());
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(DEVICE_TYPE_CODE) LIKE :p_param ESCAPE '\\' OR lower(DEVICE_TYPE_NAME) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND DEVICE_TYPE_ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        sqlQuery += " ORDER BY DEVICE_TYPE_NAME asc";

        break;
      case "GNOC_CR_METHOD_PARAMETER":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON,
                "cr-method-parameter-item-by-code-or-name");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND lower(PARAMETER_NAME) LIKE :p_param ESCAPE '\\' ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND METHOD_PARAMETER_ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "GNOC_CR_WEBSERVICE_METHOD":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON,
                "cr-webservice-method-item-by-code-or-name");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND lower(METHOD_NAME) LIKE :p_param ESCAPE '\\' ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND WEBSERVICE_METHOD_ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "UNIT":
        sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "unit-by-code-or-name");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(u.UNIT_CODE) LIKE :p_param ESCAPE '\\' OR lower(u.UNIT_NAME) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          sqlQuery = sqlQuery + " AND u.PARENT_UNIT_ID = :p_parent ";
          parameters.put("p_parent", objectSearchDto.getParent());
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND u.UNIT_ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "UNIT_BY_ROLE_USER":
        List<RolesDTO> roles = roleUserRepository.getListRoleCodeByUserId(userToken.getUserID());
        List<String> lstUnit = new ArrayList<>();
        boolean checkAdmin = false;
        if (userToken != null && roles != null) {
          if (roles.stream().filter(
              o -> ROLE.ROLE_USER_ADMIN.equals(o.getRoleCode()) || ROLE.ROLE_USER_GNOC_EDIT_USER
                  .equals(o.getRoleCode())).findFirst().isPresent()) {
            checkAdmin = true;
          } else if (roles.stream().filter(o -> ROLE.ROLE_USER_ROLE_TP.equals(o.getRoleCode()))
              .findFirst().isPresent()) {
            checkAdmin = false;
            UsersEntity usersEntity = userRepository.getUserByUserName(userToken.getUserName());
            if (usersEntity != null) {
              if (usersEntity.getUnitId() != null && usersEntity.getUnitId() > 0) {
                lstUnit.add(usersEntity.getUnitId().toString());
              }
              if (StringUtils.isNotNullOrEmpty(usersEntity.getRelateUnits())) {
                lstUnit.add(usersEntity.getRelateUnits());
              }
            }
          }
        }
        sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "unit-by-code-or-name");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(u.UNIT_CODE) LIKE :p_param ESCAPE '\\' OR lower(u.UNIT_NAME) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          sqlQuery = sqlQuery + " AND u.PARENT_UNIT_ID = :p_parent ";
          parameters.put("p_parent", objectSearchDto.getParent());
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND u.UNIT_ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (!checkAdmin && lstUnit != null && lstUnit.size() > 0) {
          sqlQuery =
              sqlQuery + " AND u.UNIT_ID in (select DISTINCT T1.UNIT_ID from COMMON_GNOC.UNIT T1 "
                  + "  START WITH T1.UNIT_ID in (:lstUnit) "
                  + "  connect by prior  T1.UNIT_ID = T1.PARENT_UNIT_ID) ";
          parameters.put("lstUnit", lstUnit);
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "UNIT_BY_LOCATION":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "unit-location-by-code-or-name");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND lower(lr.itemName) LIKE :p_param ESCAPE '\\' ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          sqlQuery = sqlQuery + " AND lr.parenItemId = :p_parent ";
          parameters.put("p_parent", objectSearchDto.getParent());
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND lr.itemId = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        sqlQuery = sqlQuery + " ) ls ";
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + "WHERE ls.RO BETWEEN 0 AND :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        parameters.put("p_location", objectSearchDto.getLocationId());
        break;
      case "UNIT_CR_MAIN":
        if (objectSearchDto.getProcessForCr() == null) {
          return new ArrayList<>();
        }
        if (objectSearchDto.getProcessForCr() == 0) {
          sqlQuery = SQLBuilder
              .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "unit-by-code-or-name");
          if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
            sqlQuery = sqlQuery
                + " AND (lower(u.UNIT_CODE) LIKE :p_param ESCAPE '\\' OR lower(u.UNIT_NAME) LIKE :p_param ESCAPE '\\') ";
            parameters.put("p_param",
                StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
          }
          if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
            sqlQuery = sqlQuery + " AND u.PARENT_UNIT_ID = :p_parent ";
            parameters.put("p_parent", objectSearchDto.getParent());
          }
          if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
            sqlQuery = sqlQuery + " AND u.UNIT_ID = :p_value ";
            parameters.put("p_value", objectSearchDto.getValue());
          }
          if (objectSearchDto.getRownum() != 0L) {
            sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
            parameters.put("p_rownum", objectSearchDto.getRownum());
          }
        } else {
          sqlQuery = SQLBuilder
              .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-list-responsible-by-process");
          parameters.put("cr_process_id", objectSearchDto.getProcessForCr());
          if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
            sqlQuery = sqlQuery
                + " AND (lower(ut.UNIT_CODE) LIKE :p_param ESCAPE '\\' OR lower(ut.UNIT_NAME) LIKE :p_param ESCAPE '\\') ";
            parameters.put("p_param",
                StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
          }
          if (objectSearchDto.getRownum() != 0L) {
            sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
            parameters.put("p_rownum", objectSearchDto.getRownum());
          }
        }
        break;
      case "UNIT_CR_MAIN_ALL":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "unit-cr-all-by-code-or-name");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(ut.UNIT_CODE) LIKE :p_param ESCAPE '\\' OR lower(ut.UNIT_NAME) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND ut.UNIT_ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "USERS":
        if (objectSearchDto.getIsHasChildren() == 1L) {
          sqlQuery = SQLBuilder
              .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "users-has-children-by-code-or-name");
        } else {
          sqlQuery = SQLBuilder
              .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "users-by-code-or-name");
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND ( lower(s.username) like :p_param escape '\\' or lower(s.fullname) like :p_param  escape '\\' or lower(s.mobile) like :p_param escape '\\'  or lower(s.email) like :p_param escape '\\'  or  lower(u.dept_full) like :p_param escape '\\' ) ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          parameters.put("p_parent", objectSearchDto.getParent());
        } else {
          parameters.put("p_parent", null);
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND s.user_id = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "USERS_ALL":
          sqlQuery = SQLBuilder
              .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "users-all-by-code-or-name");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND ( lower(s.username) like :p_param escape '\\' or lower(s.fullname) like :p_param  escape '\\' or lower(s.mobile) like :p_param escape '\\'  or lower(s.email) like :p_param escape '\\'  or  lower(u.dept_full) like :p_param escape '\\' ) ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          parameters.put("p_parent", objectSearchDto.getParent());
        } else {
          parameters.put("p_parent", null);
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND s.user_id = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "USERS_BY_USERNAME":
        if (objectSearchDto.getIsHasChildren() == 1L) {
          sqlQuery = SQLBuilder
              .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON,
                  "users-username-has-children-by-code-or-name");
        } else {
          sqlQuery = SQLBuilder
              .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "users-username-by-code-or-name");
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND ( lower(s.username) like :p_param escape '\\' or lower(s.fullname) like :p_param  escape '\\' or lower(s.mobile) like :p_param escape '\\'  or lower(s.email) like :p_param escape '\\'  or  lower(u.dept_full) like :p_param escape '\\' ) ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          parameters.put("p_parent", objectSearchDto.getParent());
        } else {
          parameters.put("p_parent", null);
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND s.username = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "USERS_FT":
        if (objectSearchDto.getIsHasChildren() == 1L) {
          sqlQuery = SQLBuilder
              .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON,
                  "users-ft-has-children-by-code-or-name");
        } else {
          sqlQuery = SQLBuilder
              .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "users-ft-by-code-or-name");
        }
        parameters.put("p_userId", userToken.getUserID());
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND ( lower(s.username) like :p_param escape '\\' or lower(s.fullname) like :p_param  escape '\\' or lower(s.mobile) like :p_param escape '\\'  or lower(s.email) like :p_param escape '\\'  or  lower(u.dept_full) like :p_param escape '\\' ) ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          parameters.put("p_parent", objectSearchDto.getParent());
        } else {
          parameters.put("p_parent", null);
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND s.user_id = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "USERS_BY_USERNAME_SR_ROLE_USE":
          sqlQuery = SQLBuilder
              .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON,
                  "get-list-users-by-username-sr-role-use");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND ( lower(s.username) like :p_param escape '\\' or lower(s.fullname) like :p_param  escape '\\' or lower(s.mobile) like :p_param escape '\\'  or lower(s.email) like :p_param escape '\\'  or  lower(u.dept_full) like :p_param escape '\\' ) ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          parameters.put("p_parent", objectSearchDto.getParent());
        } else {
          parameters.put("p_parent", null);
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND s.username = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "ROLE":
        sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "role-by-code-or-name");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(ROLE_CODE) LIKE :p_param ESCAPE '\\' OR lower(ROLE_NAME) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND ROLE_ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      // dungpv add cbb services
      case "SERVICES_BY_SERVICES_GROUP":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-services-by-services-group");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(data.itemCode) LIKE :p_param ESCAPE '\\' OR lower(data.itemName) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND data.itemId = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          sqlQuery = sqlQuery + " AND data.serviceGroup = :p_parent ";
          parameters.put("p_parent", objectSearchDto.getParent());
        }
        parameters.put("locale", I18n.getLocale());
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
//        end
      case "RISK_SYSTEM":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-risk-system");
        parameters.put("categoryCode", Constants.CATEGORY.RISK_SYSTEM_PRIORITY);
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(s.SYSTEM_CODE) LIKE :p_param ESCAPE '\\' OR lower(s.SYSTEM_NAME) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND s.ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          sqlQuery = sqlQuery + " AND s.COUNTRY_ID = :p_parent ";
          parameters.put("p_parent", objectSearchDto.getParent());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
      case "CR_PROCESS":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "cr-process-cbb");
        parameters.put("p_leeLocale", I18n.getLocale());
        parameters.put("p_system", "OPEN_PM");
        parameters.put("p_bussiness", "OPEN_PM.CR_PROCESS");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(itemName) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND ld.itemId = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        sqlQuery += " order by ld.itemName ASC ";
        break;
      case "COUNTRY":
      case "COUNTRY_AREA":
      case "COUNTRY_PROVINCE":
      case "COUNTRY_DISTRICT":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "country-by-code-or-name");
        sqlQuery = sqlQuery + " AND LEVEL = :p_level ";
        if ("COUNTRY".equals(objectSearchDto.getModuleName())) {
          parameters.put("p_level", 1);
        } else if ("COUNTRY_AREA".equals(objectSearchDto.getModuleName())) {
          parameters.put("p_level", 2);
        } else if ("COUNTRY_PROVINCE".equals(objectSearchDto.getModuleName())) {
          parameters.put("p_level", 3);
        } else if ("COUNTRY_DISTRICT".equals(objectSearchDto.getModuleName())) {
          parameters.put("p_level", 4);
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(LOCATION_CODE) LIKE :p_param ESCAPE '\\' OR lower(LOCATION_NAME) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND LOCATION_ID = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          sqlQuery = sqlQuery + " AND parent_id = :p_parent ";
          parameters.put("p_parent", objectSearchDto.getParent());
        } else {
          if (!"COUNTRY".equals(objectSearchDto.getModuleName())) {
            return new ArrayList<>();
          }
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        sqlQuery += " START WITH parent_id IS NULL CONNECT BY PRIOR location_id = parent_id ";

        sqlQuery += " ORDER BY location_name ";

        break;
      case "COUNTRY_RETURN_CODE":
      case "COUNTRY_AREA_RETURN_CODE":
      case "COUNTRY_PROVINCE_RETURN_CODE":
      case "COUNTRY_DISTRICT_RETURN_CODE":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "country-by-code-or-name-return-code");
        sqlQuery = sqlQuery + " AND LEVEL = :p_level ";
        if ("COUNTRY_RETURN_CODE".equals(objectSearchDto.getModuleName())) {
          parameters.put("p_level", 1);
        } else if ("COUNTRY_AREA_RETURN_CODE".equals(objectSearchDto.getModuleName())) {
          parameters.put("p_level", 2);
        } else if ("COUNTRY_PROVINCE_RETURN_CODE".equals(objectSearchDto.getModuleName())) {
          parameters.put("p_level", 3);
        } else if ("COUNTRY_DISTRICT_RETURN_CODE".equals(objectSearchDto.getModuleName())) {
          parameters.put("p_level", 4);
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(LOCATION_CODE) LIKE :p_param ESCAPE '\\' OR lower(LOCATION_NAME) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
          sqlQuery = sqlQuery + " AND LOCATION_CODE = :p_value ";
          parameters.put("p_value", objectSearchDto.getValue());
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          sqlQuery = sqlQuery
              + " AND PARENT_ID IN ( SELECT location_id FROM common_gnoc.cat_location where LOCATION_CODE = :p_parent ) ";
          parameters.put("p_parent", objectSearchDto.getParent());
        } else {
          if (!"COUNTRY_RETURN_CODE".equals(objectSearchDto.getModuleName())) {
            return new ArrayList<>();
          }
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        sqlQuery += " START WITH parent_id IS NULL CONNECT BY PRIOR location_id = parent_id ";

        sqlQuery += " ORDER BY location_name ";

        break;
      case "REGION":
        break;
      case "REGION_COUNTRY":
        parameters.put("p_level", 1);
        break;
      case "REGION_AREA":
        parameters.put("p_level", 2);
        break;
      case "REGION_PROVINCE":
        parameters.put("p_level", 3);
        break;
      case "REGION_DISTRICT":
        parameters.put("p_level", 4);
        break;
      case "STATION":
        sqlQuery = "SELECT DISTINCT s.STATION_CODE itemId, s.STATION_CODE itemName FROM OPEN_PM.MR_CD_BATTERY s WHERE 1 = 1";
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          sqlQuery = sqlQuery
              + " AND (lower(s.STATION_CODE) LIKE :p_param ESCAPE '\\' OR lower(s.STATION_ID) LIKE :p_param ESCAPE '\\') ";
          parameters.put("p_param",
              StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
        }
        if (objectSearchDto.getRownum() != 0L) {
          sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
          parameters.put("p_rownum", objectSearchDto.getRownum());
        }
        break;
    }
    if ("REGION".equals(objectSearchDto.getModuleName())
        || "REGION_COUNTRY".equals(objectSearchDto.getModuleName())
        || "REGION_AREA".equals(objectSearchDto.getModuleName())
        || "REGION_PROVINCE".equals(objectSearchDto.getModuleName())
        || "REGION_DISTRICT".equals(objectSearchDto.getModuleName())) {
      sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "region-by-code-or-name");
      if ("REGION".equals(objectSearchDto.getModuleName())) {

      } else {
        sqlQuery = sqlQuery + " AND LEVEL = :p_level ";
      }
      if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
        sqlQuery = sqlQuery
            + " AND (lower(location_code) LIKE :p_param ESCAPE '\\' OR lower(location_name) LIKE :p_param ESCAPE '\\') ";
        parameters.put("p_param",
            StringUtils.convertLowerParamContains(objectSearchDto.getParam().toLowerCase()));
      }
      if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
        sqlQuery = sqlQuery + " AND parent_id = :p_parent ";
        parameters.put("p_parent", objectSearchDto.getParent());
      }
      if (StringUtils.isNotNullOrEmpty(objectSearchDto.getValue())) {
        sqlQuery = sqlQuery + " AND location_id = :p_value ";
        parameters.put("p_value", objectSearchDto.getValue());
      }
      if (objectSearchDto.getRownum() != 0L) {
        sqlQuery = sqlQuery + " AND ROWNUM <= :p_rownum";
        parameters.put("p_rownum", objectSearchDto.getRownum());
      }
      sqlQuery =
          sqlQuery + " START WITH parent_id IS NULL CONNECT BY PRIOR location_id = parent_id ";
    }
    if (StringUtils.isNotNullOrEmpty(objectSearchDto.getModuleName())
        && "SERVICES_BY_SERVICES_GROUP".equals(objectSearchDto.getModuleName())) {
      List<DataItemDTO> lstServices = getNamedParameterJdbcTemplate().query(sqlQuery, parameters,
          BeanPropertyRowMapper.newInstance(DataItemDTO.class));
      List<DataItemDTO> lstResult = new ArrayList<>();
      if (lstServices != null && lstServices.size() > 0) {
        Map<String, DataItemDTO> map = new HashMap<>();
        for (DataItemDTO dto : lstServices) {
          if (!map.containsKey(dto.getItemCode() + "_" + dto.getItemName())) {
            map.put(dto.getItemCode() + "_" + dto.getItemName(), dto);
            lstResult.add(dto);
          }
        }
        return lstResult;
      }
      return lstServices;
    }
    return getNamedParameterJdbcTemplate().query(sqlQuery, parameters,
        BeanPropertyRowMapper.newInstance(DataItemDTO.class));
  }

  @Override
  public List<TreeDTO> getTreeData(ObjectSearchDto objectSearchDto) {
    String sqlQuery = "";
    Map<String, Object> parameters = new HashMap<>();
    switch (objectSearchDto.getModuleName()) {
      case "PT_RCA_TYPE":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "rca-type-tree-by-parent");
        parameters.put("p_leeLocale", I18n.getLocale());
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          parameters.put("p_parent", objectSearchDto.getParent());
        } else {
          parameters.put("p_parent", null);
        }
        break;
      case "PT_GROUP_REASON":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "problem-group-reason-tree");
        parameters.put("p_leeLocale", I18n.getLocale());
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          parameters.put("p_parent", objectSearchDto.getParent());
        } else {
          parameters.put("p_parent", null);
        }
        break;
      case "UNIT":
        sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "unit-tree-by-parent");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          parameters.put("p_parent", objectSearchDto.getParent());
        } else {
          parameters.put("p_parent", null);
        }
        break;
      case "UNIT_VSA":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "unit-vsa-tree-by-parent");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          parameters.put("p_parent", objectSearchDto.getParent());
        } else {
          parameters.put("p_parent", null);
        }
        break;
      case "CAT_REASON":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "tt-reason-tree-by-parent");
        parameters.put("leeLocale", I18n.getLocale());
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          parameters.put("p_parent", objectSearchDto.getParent());
        } else {
          parameters.put("p_parent", null);
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          parameters.put("typeId", objectSearchDto.getParam());
        } else {
          parameters.put("typeId", null);
        }
        break;
      case "CR_PROCESS":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "cr-process-tree-by-parent");
        parameters.put("p_leeLocale", I18n.getLocale());
        parameters.put("p_system", "OPEN_PM");
        parameters.put("p_bussiness", "OPEN_PM.CR_PROCESS");
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          parameters.put("p_parent", objectSearchDto.getParent());
        } else {
          parameters.put("p_parent", null);
        }
        break;
      case "TROUBLE_REASON":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "tt-important-reason-tree-by-parent");
        parameters.put("leeLocale", I18n.getLocale());
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          parameters.put("p_parent", objectSearchDto.getParent());
        } else {
          parameters.put("p_parent", null);
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          parameters.put("typeId", objectSearchDto.getParam());
        } else {
          parameters.put("typeId", null);
        }
        break;
      case "TROUBLE_ARRAY":
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "tt-important-array-tree-by-parent");
        parameters.put("leeLocale", I18n.getLocale());
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParent())) {
          parameters.put("p_parent", objectSearchDto.getParent());
        } else {
          parameters.put("p_parent", null);
        }
        if (StringUtils.isNotNullOrEmpty(objectSearchDto.getParam())) {
          parameters.put("typeId", objectSearchDto.getParam());
        } else {
          parameters.put("typeId", null);
        }
        break;
    }
    return getNamedParameterJdbcTemplate().query(sqlQuery, parameters,
        BeanPropertyRowMapper.newInstance(TreeDTO.class));
  }

  @Override
  public UsersInsideDto getUserByUserName(String userName) {
    List<UsersEntity> datas = findByMultilParam(UsersEntity.class, "username", userName);
    if (datas != null && datas.size() > 0) {
      return datas.get(0).toDTO();
    }
    return null;
  }

  @Override
  public List<UsersEntity> getListUserOfUnit(Long unitId) {
    return findByMultilParam(UsersEntity.class,
        "unitId", unitId);
  }

  @Override
  public UsersEntity getUserByUserId(Long userId) {
    if (userId == null) {
      return null;
    }
    return getEntityManager().find(UsersEntity.class, userId);
  }

  @Override
  public List<DataItemDTO> getListItemByDataCode(String dataCode) {
    String sqlQuery = "";
    switch (dataCode) {
      case Constants.CATEGORY.GNOC_OD_PRIORITY:
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "od-priority-item-by-data-code");
        break;
      case Constants.CATEGORY.GNOC_UNIT:
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "od-unit-item-by-data-code");
        break;
      case Constants.CATEGORY.GNOC_OD_TYPE:
        sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "od-type-item-by-data-code");
        break;
    }
    return getEntityManager().createNativeQuery(sqlQuery).getResultList();
  }

  @Override
  public Map<String, String> getConfigProperty() {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "config-property-sql");
    Map<String, String> mapResult = new HashMap<>();
    List<ConfigPropertyDTO> result = getNamedParameterJdbcTemplate()
        .query(sqlQuery, mapResult, BeanPropertyRowMapper.newInstance(ConfigPropertyDTO.class));
    if (result != null && !result.isEmpty()) {
      for (ConfigPropertyDTO cfg : result) {
        mapResult.put(cfg.getKey(), cfg.getValue());
      }
    }
    return mapResult;
  }

  @Override
  public String getConfigPropertyValue(String key) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-Config-Property-Value");
    Map<String, String> mapResult = new HashMap<>();
    mapResult.put("key", key);
    List<ConfigPropertyDTO> result = getNamedParameterJdbcTemplate()
        .query(sqlQuery, mapResult, BeanPropertyRowMapper.newInstance(ConfigPropertyDTO.class));
    if (result != null && !result.isEmpty()) {
      return result.get(0).getValue();
    }
    return null;
  }

  @Override
  public ConfigPropertyDTO getConfigPropertyObj(String key) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-Config-Property-Obj");
    Map<String, String> mapResult = new HashMap<>();
    mapResult.put("key", key);
    List<ConfigPropertyDTO> result = getNamedParameterJdbcTemplate()
        .query(sqlQuery, mapResult, BeanPropertyRowMapper.newInstance(ConfigPropertyDTO.class));
    if (result != null && !result.isEmpty()) {
      return result.get(0);
    }
    return null;
  }

  @Override
  public List<DashboardDTO> searchDataDashboard(DashboardDTO dashboardDTO) {
    String sql = " select column1, value1, date_time from wfm.dashboard where dashboard_code = 1 ";
           sql += " and date_time between to_date(:time_start, 'DD/MM/YYYY') and to_date(:time_end, 'DD/MM/YYYY') ";
           sql += " order by date_time desc ";
    Map<String, String> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(dashboardDTO.getTime_start())) {
      parameters.put("time_start", dashboardDTO.getTime_start());
    } else {
      parameters.put("time_start", null);
    }
    if (StringUtils.isNotNullOrEmpty(dashboardDTO.getTime_end())) {
      parameters.put("time_end", dashboardDTO.getTime_end());
    } else {
      parameters.put("time_end", null);
    }
    List<DashboardDTO> result = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(DashboardDTO.class));
    if(result != null && !result.isEmpty()){
      return result;
    }
    return null;
  }

  @Override
  public  List<DashboardDTO> getDataTableDashboard(){
    String sql = " select column1, column2, column3, value1, value2 from wfm.dashboard where dashboard_code = 2 ";
           sql += " and column1='NUM_CR' and TRUNC(SYSDATE) = trunc(date_time) ";
           sql += " order by column3 asc ";
    Map<String, String> parameters = new HashMap<>();
    List<DashboardDTO> result = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(DashboardDTO.class));
    if(result != null && !result.isEmpty()){
      return result;
    }
    return null;
  }

  @Override
  public  List<RolesDTO> getListRole(){
    String sql = "select * from common_gnoc.roles order by role_name asc";
    Map<String, String> parameters = new HashMap<>();
    List<RolesDTO> result = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(RolesDTO.class));
    if(result != null && !result.isEmpty()){
      return result;
    }
    return null;
  }

  @Override
  public Datatable getListComment(UserCommentDTO userCommentDTO){
    try{
      Datatable datatable = new Datatable();
      String sql = "SELECT * FROM COMMON_GNOC.USER_COMMENT ORDER BY create_time DESC";
      Map<String, String> parameters = new HashMap<>();
      parameters.put("start", userCommentDTO.getPageStart());
      parameters.put("end", userCommentDTO.getPageEnd());
      List<UserCommentDTO> lstComment = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(UserCommentDTO.class));
      if(lstComment != null && !lstComment.isEmpty()){
        List<UserCommentDTO> result;
        if(Integer.parseInt(userCommentDTO.getPageEnd()) > lstComment.size()){
          result = lstComment.subList(Integer.parseInt(userCommentDTO.getPageStart()),
              lstComment.size());
        }
        else {
          result = lstComment.subList(Integer.parseInt(userCommentDTO.getPageStart()),
              Integer.parseInt(userCommentDTO.getPageEnd()));
        }

        datatable.setTotal(lstComment.size());
        if(result != null){
          datatable.setData(result);
        }
        return datatable;
      }
    }catch (Exception err){
      log.error(err.getMessage(), err);
    }
    return null;
  }

  @Override
  public ResultInSideDto addComment(UserCommentDTO userCommentDTO){
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try{
      userCommentDTO.setCreateTime(new Date());

      UserCommentEntity u = userCommentDTO.toEntity();

      getEntityManager().persist(u);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
      return  resultInSideDto;
    }catch (Exception err){
      log.error(err.getMessage(), err);
      resultInSideDto.setKey(Constants.RESULT.FAIL);
      return resultInSideDto;
    }
  }

  @Override
  public List<ContactDTO> getListContact(){
    try{
      String sql = "select * from COMMON_GNOC.CONTACT order by CONTACT_ID asc";
      Map<String, String> parameters = new HashMap<>();
      List<ContactDTO> result = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(ContactDTO.class));
      if(result != null && !result.isEmpty()){
        return result;
      }
    }catch (Exception err){
      log.error(err.getMessage(), err);
    }
    return null;
  }

  public String getNationFromUnit(Long unitId) {
    UnitDTO unitDTO = unitRepository.findUnitById(unitId);
    if (unitDTO != null && unitDTO.getLocationId() != null) {
      CatLocationDTO catLocationDTO = getCatLocationById(unitDTO.getLocationId());
      if (catLocationDTO != null) {
        if (StringUtils.isNotNullOrEmpty(catLocationDTO.getNationCode())) {
          return catLocationDTO.getNationCode();
        }
      }
    }
    return "VNM";
  }

  public CatLocationDTO getCatLocationById(Long locationId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-cat-location-by-id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("locationId", locationId);
    List<CatLocationDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(CatLocationDTO.class));
    if (list != null && !list.isEmpty()) {
      CatLocationDTO dto = list.get(0);
      return dto;
    }
    return null;
  }

  //thanhlv12 add 22-09-2020
  @Override
  public Boolean checkMaxRowExport(Integer row){
    Boolean check = null;
    try {
      ConfigPropertyDTO configPropertyDTO = getConfigPropertyByKey();
      if(configPropertyDTO != null){
        if(row > Integer.parseInt(configPropertyDTO.getValue())){
          check = true;
        }
        else {
          check = false;
        }
      }
    }catch (Exception error) {
      log.error(error.getMessage());
    }
    return check;
  }

  @Override
  public ConfigPropertyDTO getConfigPropertyByKey () {
    try {
      String sql = "select value from COMMON_GNOC.CONFIG_PROPERTY where key = :key ";
      Map<String, Object> paramters = new HashMap<>();
      paramters.put("key", "MAXROWEXPORT");
      List<ConfigPropertyDTO> lstConfig = getNamedParameterJdbcTemplate().query(sql, paramters,
          BeanPropertyRowMapper.newInstance(ConfigPropertyDTO.class));
      if(lstConfig != null && !lstConfig.isEmpty()){
        return lstConfig.get(0);
      }
    } catch (Exception error) {
      log.error(error.getMessage());
    }
    return null;
  }


  @Override
  public ResultInSideDto insertHisUserImpact(DataHistoryChange dataHistoryChange) {
    ResultInSideDto resultInSideDto = new  ResultInSideDto();
    try {
      String result = compareObject(dataHistoryChange.getOldObject(), dataHistoryChange.getNewObject(),
          dataHistoryChange.getKeys());
      if (StringUtils.isNotNullOrEmpty(result) && !"{}".equals(result)) {
        HisUserImpactDTO hisUserImpactDTO = new HisUserImpactDTO();
        hisUserImpactDTO.setNewObject(dataHistoryChange.getRecordId());
        hisUserImpactDTO.setType(dataHistoryChange.getType());
        hisUserImpactDTO.setUserId(dataHistoryChange.getUserId());
        hisUserImpactDTO.setResult(result);
        hisUserImpactDTO.setCreateTime(new Date());
        hisUserImpactDTO.setActionType(dataHistoryChange.getActionType());
        resultInSideDto.setKey(Constants.RESULT.SUCCESS);
        getEntityManager().persist(hisUserImpactDTO.toEntity());
      }
    } catch (Exception error) {
      log.error(error.getMessage());
      resultInSideDto.setKey(Constants.RESULT.FAIL);
    }
    return  resultInSideDto;
  }

  @Override
  public  Datatable getListHistory(HisUserImpactDTO hisUserImpactDTO) {
    Datatable datatable = new Datatable();
    try{
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-List-Data-History");
      Map<String, String> parameters = new HashMap<>();
      if (StringUtils.isNotNullOrEmpty(hisUserImpactDTO.getUserId())) {
        sql += " AND his.user_id = :userId";
        parameters.put("userId", hisUserImpactDTO.getUserId());
      }
      if (StringUtils.isNotNullOrEmpty(hisUserImpactDTO.getType())) {
        sql += " AND his.type = :type";
        parameters.put("type", hisUserImpactDTO.getType());
      }
      if (StringUtils.isNotNullOrEmpty(hisUserImpactDTO.getSearchAll())) {
        sql += " AND LOWER(his.result) LIKE :searchAll";
        parameters.put("searchAll", StringUtils.convertLowerParamContains(hisUserImpactDTO.getSearchAll()));
      }
      if (StringUtils.isNotNullOrEmpty(hisUserImpactDTO.getActionType())) {
        sql += " AND his.action_type = :actionType";
        parameters.put("actionType", hisUserImpactDTO.getActionType());
      }
      sql += " order by CREATE_TIME DESC";
      datatable = getListDataTableBySqlQuery(sql, parameters,
          hisUserImpactDTO.getPage(), hisUserImpactDTO.getPageSize(), HisUserImpactDTO.class, hisUserImpactDTO.getSortName(),
          hisUserImpactDTO.getSortType());
      if (datatable != null) {
        return datatable;
      }
    }catch (Exception err){
      log.error(err.getMessage(), err);
    }
    return datatable;
  }

  @Override
  public List<CatItemDTO> getListCommonLink(String locale) {
    try {
      String sql = "select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = " +
          "(select cat.CATEGORY_ID from COMMON_GNOC.CATEGORY cat where cat.CATEGORY_CODE = :category) AND DESCRIPTION = :locale" +
          " order by UPDATE_TIME DESC";
      Map<String, Object> paramters = new HashMap<>();
      paramters.put("category", "COMMON_LINK");
      paramters.put("locale", locale);
      List<CatItemDTO> lst = getNamedParameterJdbcTemplate().query(sql, paramters,
          BeanPropertyRowMapper.newInstance(CatItemDTO.class));
      if (lst != null && !lst.isEmpty()) {
        return  lst;
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }

  @Override
  public CatItemDTO getConfigIconDislay(String keyCode) {
    try {
      String sql = "select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = " +
          "(select c.CATEGORY_ID from COMMON_GNOC.CATEGORY c where c.CATEGORY_CODE = :categoryCode)";
      Map<String, Object> paramters = new HashMap<>();
      paramters.put("categoryCode", keyCode);
      List<CatItemDTO> lst = getNamedParameterJdbcTemplate().query(sql, paramters,
          BeanPropertyRowMapper.newInstance(CatItemDTO.class));
      if (lst != null && !lst.isEmpty()) {
        return lst.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return null;
  }

  private String compareObject (Object oldObject, Object newObject, List<String> keys) {
    try {
      Map<String, Object> json = new HashMap<>();
      String value = null;
      for (String key : keys) {
        if (((Map) oldObject).containsKey(key) && ((Map) newObject).containsKey(key)) {
          var oldValue = ((Map) oldObject).get(key) == "" ? null : ((Map) oldObject).get(key);
          var newValue = ((Map) newObject).get(key) == "" ? null : ((Map) newObject).get(key);
          if (oldValue != null && newValue != null) {
            if (!oldValue.equals(newValue)) {
              value = "{" + '"' + "oldValue" + '"' + ":" + '"' + oldValue + '"' + ","
                  + '"' + "newValue" + '"' + ":" + '"' + newValue + '"' + "}";
            }
          }
          else if (oldValue == null && newValue != null) {
            value = "{" + '"' + "oldValue" + '"' + ":" + '"' + "null" + '"' + ","
                + '"' + "newValue" + '"' + ":" + '"' + newValue + '"' + "}";
          }
          else if (oldValue != null && newValue == null) {
            value = "{" + '"' + "oldValue" + '"' + ":" + '"' + oldValue + '"' + ","
                + '"' + "newValue" + '"' + ":" + '"' + "null" + '"' + "}";
          }
        }
        if (value != null) {
          json.put(key, value);
          value = null;
        }
      }
      ObjectMapper mapper = new ObjectMapper();
      String result = mapper.writeValueAsString(json);
      if (StringUtils.isNotNullOrEmpty(result)) {
        return result;
      }
    } catch (Exception error) {
      log.error(error.getMessage());
    }
    return  null;
  }
  //end
}
