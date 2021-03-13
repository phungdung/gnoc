package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCfgMarketDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.maintenance.model.MrSynItDevicesEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import viettel.passport.client.UserToken;

@Repository
@Slf4j
public class MrSynItHardDevicesRepositoryImpl extends BaseRepository implements
    MrSynItHardDevicesRepository {

  @Override
  public List<MrSynItDevicesDTO> getListMrSynITDeviceSoftDTO(MrSynItDevicesDTO dto, int rowStart,
      int maxRow) {
    List<MrSynItDevicesDTO> lst = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    try {
      Map<String, Object> listParams = new HashMap<>();
      sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SYN_IT_DEVICES_SOFT,
          "get-List-MrSyn-ITDevice-Soft-DTO"));
      listParams.put("userLogin", dto.getUserLogin());
      listParams.put("unitLogin", dto.getUnitLogin());

      if (!StringUtils.isStringNullOrEmpty(dto.getIsNotSchedule()) && "1"
          .equals(dto.getIsNotSchedule()) && !StringUtils.isStringNullOrEmpty(dto.getMrSoft())) {
        sql.append(" AND (T1.IS_COMPLETE_SOFT = 1 OR T1.IS_COMPLETE_SOFT IS NULL) ");
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getIsNotSchedule()) && "1"
          .equals(dto.getIsNotSchedule()) && !StringUtils.isStringNullOrEmpty(dto.getMrHard())) {
        sql.append(" AND ("
            + "(T1.IS_COMPLETE_1M = 1 OR T1.IS_COMPLETE_1M IS NULL) OR "
            + "(T1.IS_COMPLETE_3M = 1 OR T1.IS_COMPLETE_3M IS NULL) OR "
            + "(T1.IS_COMPLETE_6M = 1 OR T1.IS_COMPLETE_6M IS NULL) OR "
            + "(T1.IS_COMPLETE_12M = 1 OR T1.IS_COMPLETE_12M IS NULL) "
            + ") ");
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getMarketCode())) {
        sql.append(" and T2.COUNTRY = :marketCode");
        listParams.put("marketCode", dto.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getRegionSoft())) {
        sql.append(" AND T1.REGION_SOFT = :regionSoft ");
        listParams.put("regionSoft", dto.getRegionSoft());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getRegionHard())) {
        sql.append(" AND T1.REGION_HARD = :regionHard ");
        listParams.put("regionHard", dto.getRegionHard());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getArrayCode())) {
        sql.append(" and T1.ARRAY_CODE = :arrayCode");
        listParams.put("arrayCode", dto.getArrayCode());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getDeviceType())) {
        sql.append(" and T1.DEVICE_TYPE = :deviceType");
        listParams.put("deviceType", dto.getDeviceType());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getObjectCode())) {
        sql.append(" and lower(T1.OBJECT_CODE) like :objectCode escape '\\' ");
        listParams.put("objectCode",
            "%" + StringUtils.replaceSpecicalChracterSQL(dto.getObjectCode()) + "%");
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getObjectName())) {
        sql.append(" and lower(T1.OBJECT_NAME) like :objectName escape '\\' ");
        listParams.put("objectName",
            "%" + StringUtils.replaceSpecicalChracterSQL(dto.getObjectName()) + "%");
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getIpNode())) {
        sql.append(" and lower(T1.IP_NODE) like :ipNode escape '\\' ");
        listParams
            .put("ipNode", "%" + StringUtils.replaceSpecicalChracterSQL(dto.getIpNode()) + "%");
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getVendor())) {
        sql.append(" and lower(T1.VENDOR) like :vendor escape '\\' ");
        listParams
            .put("vendor", "%" + StringUtils.replaceSpecicalChracterSQL(dto.getVendor()) + "%");
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getGroupCode())) {
        sql.append(" and lower(T1.GROUP_CODE) like :groupCode escape '\\' ");
        listParams.put("groupCode",
            "%" + StringUtils.replaceSpecicalChracterSQL(dto.getGroupCode()) + "%");
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getMrSoft())) {
        sql.append(" and T1.MR_SOFT = :mrSoft ");
        listParams.put("mrSoft", dto.getMrSoft());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getMrHard())) {
        sql.append(" and T1.MR_HARD = :mrHard ");
        listParams.put("mrHard", dto.getMrHard());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getObjectId())) {
        sql.append(" and T1.OBJECT_ID in (:listId) ");
        listParams.put("listId", dto.getObjectId());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getImplementUnit())) {
        sql.append(" and T3.IMPLEMENT_UNIT = :implementUnit ");
        listParams.put("implementUnit", dto.getImplementUnit());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getCheckingUnit())) {
        sql.append(" and T3.CHECKING_UNIT = :checkingUnit ");
        listParams.put("checkingUnit", dto.getCheckingUnit());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getBoUnit())) {
        sql.append(" and T1.BO_UNIT = :boUnit ");
        listParams.put("boUnit", dto.getBoUnit());
      }

      sql.append(" ORDER BY T1.ID DESC");
      String sqlStr = sql.toString();
      if (!StringUtils.isStringNullOrEmpty(dto.getMrSoft())) {
        sqlStr = sqlStr
            .replace("{soft}", " when :mrSoft = 1 and T1.REGION_SOFT = T3.REGION then 1 ");
        sqlStr = sqlStr.replace("{hard}", "");
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getMrHard())) {
        sqlStr = sqlStr.replace("{soft}", "");
        sqlStr = sqlStr
            .replace("{hard}", " when :mrHard = 1 and T1.REGION_HARD = T3.REGION then 1 ");
      }
      lst = getNamedParameterJdbcTemplate().query(sqlStr, listParams,
          BeanPropertyRowMapper.newInstance(MrSynItDevicesDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public Datatable getListMrSynITDeviceHardPage(MrSynItDevicesDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(), dto.getPage(),
        dto.getPageSize(), MrSynItDevicesDTO.class, dto.getSortName(), dto.getSortType());
  }

  @Override
  public List<MrSynItDevicesDTO> getListMrSynItDeviceHardExport(MrSynItDevicesDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrSynItDevicesDTO.class));
  }

  @Override
  public BaseDto sqlSearch(MrSynItDevicesDTO mrSynItDevicesDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = null;
    sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SYN_IT_DEVICES_HARD,
            "getListMrSynItDevicesHardDTO");
    Map<String, Object> listParams = new HashMap<>();
    if (mrSynItDevicesDTO != null) {
      if (StringUtils.isNotNullOrEmpty(mrSynItDevicesDTO.getSearchAll())) {
        sqlQuery += " and (lower(T1.OBJECT_CODE) like :searchAll escape '\\' "
            + " or lower(T1.OBJECT_NAME) like :searchAll escape '\\' "
            + " or lower(T1.IP_NODE) like :searchAll escape '\\'   "
            + " or lower(T1.GROUP_CODE) like :searchAll escape '\\' "
            + " or lower(T1.VENDOR) like :searchAll escape '\\')  ";
        listParams.put("searchAll",
            StringUtils.convertLowerParamContains(mrSynItDevicesDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getApproveStatusHard())) {
        if ("-1".equals(mrSynItDevicesDTO.getApproveStatus())) {
          sqlQuery += " AND T1.APPROVE_STATUS_HARD IS NULL ";
        } else {
          sqlQuery += " AND ( T1.APPROVE_STATUS_HARD  = :approvaeStatusHard ) ";
          listParams.put("approvaeStatusHard", mrSynItDevicesDTO.getApproveStatusHard());
        }
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getIsNotSchedule()) && "1"
          .equals(mrSynItDevicesDTO.getIsNotSchedule()) && !StringUtils
          .isStringNullOrEmpty(mrSynItDevicesDTO.getMrHard())) {
        sqlQuery += " AND ("
            + "(T1.IS_COMPLETE_1M = 1 OR T1.IS_COMPLETE_1M IS NULL) OR "
            + "(T1.IS_COMPLETE_3M = 1 OR T1.IS_COMPLETE_3M IS NULL) OR "
            + "(T1.IS_COMPLETE_6M = 1 OR T1.IS_COMPLETE_6M IS NULL) OR "
            + "(T1.IS_COMPLETE_12M = 1 OR T1.IS_COMPLETE_12M IS NULL) "
            + ") ";
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMarketCode())) {
        sqlQuery += " and T1.MARKET_CODE = :marketCode";
        listParams.put("marketCode", mrSynItDevicesDTO.getMarketCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getRegionHard())) {
        sqlQuery += " AND T1.REGION_HARD = :regionHard ";
        listParams.put("regionHard", mrSynItDevicesDTO.getRegionHard());
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getArrayCode())) {
        sqlQuery += " and T1.ARRAY_CODE = :arrayCode";
        listParams.put("arrayCode", mrSynItDevicesDTO.getArrayCode());
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getDeviceType())) {
        sqlQuery += " and T1.DEVICE_TYPE = :deviceType";
        listParams.put("deviceType", mrSynItDevicesDTO.getDeviceType());
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getObjectCode())) {
        sqlQuery += " and lower(T1.OBJECT_CODE) like :objectCode escape '\\' ";
        listParams.put("objectCode",
            StringUtils.convertLowerParamContains(mrSynItDevicesDTO.getObjectCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getObjectName())) {
        sqlQuery += " and lower(T1.OBJECT_NAME) like :objectName escape '\\' ";
        listParams.put("objectName",
            StringUtils.convertLowerParamContains(mrSynItDevicesDTO.getObjectName()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getIpNode())) {
        sqlQuery += " and lower(T1.IP_NODE) like :ipNode escape '\\' ";
        listParams
            .put("ipNode",
                StringUtils.convertLowerParamContains(mrSynItDevicesDTO.getIpNode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getVendor())) {
        sqlQuery += " and lower(T1.VENDOR) like :vendor escape '\\' ";
        listParams
            .put("vendor",
                StringUtils.convertLowerParamContains(mrSynItDevicesDTO.getVendor()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getGroupCode())) {
        sqlQuery += " and lower(T1.GROUP_CODE) like :groupCode escape '\\' ";
        listParams.put("groupCode",
            StringUtils.convertLowerParamContains(mrSynItDevicesDTO.getGroupCode()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMrHard())) {
        sqlQuery += " and T1.MR_HARD = :mrHard ";
        listParams.put("mrHard", mrSynItDevicesDTO.getMrHard());
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getObjectId())) {
        sqlQuery += " and T1.OBJECT_ID in (:listId) ";
        listParams.put("listId", mrSynItDevicesDTO.getObjectId());
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getImplementUnit())) {
        sqlQuery += " and T3.IMPLEMENT_UNIT = :implementUnit ";
        listParams.put("implementUnit", mrSynItDevicesDTO.getImplementUnit());
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getCheckingUnit())) {
        sqlQuery += " and T3.CHECKING_UNIT = :checkingUnit ";
        listParams.put("checkingUnit", mrSynItDevicesDTO.getCheckingUnit());
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getBoUnitHard())) {
        sqlQuery += " and T1.BO_UNIT_HARD = :boUnitHard ";
        listParams.put("boUnitHard", mrSynItDevicesDTO.getBoUnitHard());
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getApproveStatus())) {
        sqlQuery += " and T1.APPROVE_STATUS_HARD = :approveStatusHard ";
        listParams.put("approveStatusHard", mrSynItDevicesDTO.getApproveStatusHard());
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getCreateUserHard())) {
        sqlQuery += " and lower(T1.CREATE_USER_HARD) like :CREATE_USER_HARD escape '\\' ";
        listParams.put("CREATE_USER_HARD",
            StringUtils.convertLowerParamContains(mrSynItDevicesDTO.getCreateUserHard()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getNodeAffected())) {
        sqlQuery += " and lower( T1.NODE_AFFECTED) like :NODE_AFFECTED escape '\\' ";
        listParams.put("NODE_AFFECTED",
            StringUtils.convertLowerParamContains(mrSynItDevicesDTO.getNodeAffected()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getUserLogin())) {
        sqlQuery += " AND (T1.CREATE_USER_HARD = :userLogin\n"
            + "OR T1.BO_UNIT_HARD       IN\n"
            + "  (SELECT unit_id\n"
            + "  FROM common_gnoc.users a\n"
            + "  LEFT JOIN COMMON_GNOC.ROLE_USER b\n"
            + "  ON a.USER_ID     = b.USER_ID\n"
            + "  WHERE a.username = :userLogin\n"
            + "  AND b.ROLE_ID    =\n"
            + "    (SELECT role_id FROM COMMON_GNOC.roles WHERE role_code = 'TP'\n"
            + "    )\n"
            + "  AND b.IS_ACTIVE = 1)\n"
            + "  ) ";
        listParams.put("userLogin", mrSynItDevicesDTO.getUserLogin());
      }

      sqlQuery += " ORDER BY T1.ID DESC";
    }

    listParams.put("p_leeLocale", I18n.getLocale());
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(listParams);
    return baseDto;
  }

  @Override
  public MrSynItDevicesDTO getMrSynItDevicesHardDetail(Long id) {
    if (!StringUtils.isStringNullOrEmpty(id)) {
      Map<String, Object> parameters = new HashMap<>();
      String sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SYN_IT_DEVICES_HARD,
              "getListMrSynItDevicesHardDTO");
      sqlQuery += " and T1.ID = :id";
      parameters.put("id", id);
      parameters.put("p_leeLocale", I18n.getLocale());
      List<MrSynItDevicesDTO> lst = getNamedParameterJdbcTemplate()
          .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(MrSynItDevicesDTO.class));
      return lst.isEmpty() ? null : lst.get(0);
    }
    return null;
  }

  public List<MrSynItDevicesDTO> getListDeviceTypeByArrayCode(String arrayCode) {
    List<MrSynItDevicesDTO> lst = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    try {
      Map<String, Object> listParams = new HashMap<>();
      sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SYN_IT_DEVICES_SOFT,
          "get-List-ITDevice-Soft-By-ArrayCode"));
      sql.append(" AND ( lower(ARRAY_CODE) LIKE :arrayCode ESCAPE '\\' )");
      listParams.put("arrayCode", StringUtils.convertLowerParamContains(arrayCode));
      String sqlStr = sql.toString();
      lst = getNamedParameterJdbcTemplate().query(sqlStr, listParams,
          BeanPropertyRowMapper.newInstance(MrSynItDevicesDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public MrSynItDevicesDTO findMrDeviceByObjectId(MrSynItDevicesDTO dto) {
    List<MrSynItDevicesEntity> lst = findByMultilParam(MrSynItDevicesEntity.class, "deviceType",
        dto.getDeviceType(), "objectId", Long.valueOf(dto.getObjectId()));
    return lst.size() > 0 ? lst.get(0).toDTO() : null;
  }


  @Override
  public ResultInSideDto updateList(List<MrSynItDevicesDTO> lstMrDeviceDto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (lstMrDeviceDto != null && !lstMrDeviceDto.isEmpty()) {
      for (MrSynItDevicesDTO dto : lstMrDeviceDto) {
        getEntityManager().merge(dto.toEntity());
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto add(MrSynItDevicesDTO mrSynItDevicesDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    MrSynItDevicesEntity mrSynItDevicesEntity = getEntityManager()
        .merge(mrSynItDevicesDTO.toEntity());
    resultInSideDTO.setId(mrSynItDevicesEntity.getId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto edit(MrSynItDevicesDTO mrSynItDevicesDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(mrSynItDevicesDTO.toEntity());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    MrSynItDevicesEntity mrSynItDevicesEntity = getEntityManager()
        .find(MrSynItDevicesEntity.class, id);
    getEntityManager().remove(mrSynItDevicesEntity);
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto updateMrSynItDevice(MrSynItDevicesDTO mrSynItDevicesDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(mrSynItDevicesDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateCreateUserByMarket(MrCfgMarketDTO mrCfgMarketDTO, String type) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    String sql = "";
    Map<String, Object> parameters = new HashMap<>();
    if (mrCfgMarketDTO != null && type != null) {
      if ("BDM".equals(type)) {
        sql = "UPDATE OPEN_PM.MR_SYN_IT_DEVICES SET CREATE_USER_SOFT = :createdUser where MARKET_CODE = :marketCode ";
        parameters.put("createdUser", mrCfgMarketDTO.getCreatedUserSoftName());
      } else {
        sql = "UPDATE OPEN_PM.MR_SYN_IT_DEVICES SET CREATE_USER_HARD = :createdUser where MARKET_CODE = :marketCode ";
        parameters.put("createdUser", mrCfgMarketDTO.getCreatedUserHardName());
      }
      if (!StringUtils.isStringNullOrEmpty(mrCfgMarketDTO.getMarketCode())) {
        parameters.put("marketCode", mrCfgMarketDTO.getMarketCode());
      }
    }
    int row = getNamedParameterJdbcTemplate().update(sql, parameters);
    resultInSideDto.setQuantitySucc(row);
    return resultInSideDto;
  }
}
