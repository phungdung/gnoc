package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.maintenance.model.MrSynItDevicesEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrSynItSoftDevicesRepositoryImpl extends BaseRepository implements
    MrSynItSoftDevicesRepository {

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
        sqlStr = sqlStr.replace("{hard}", "");
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

  @Override
  public List<MrSynItDevicesDTO> getListDT_AC() {
    List<MrSynItDevicesDTO> lst = new ArrayList<>();
    String sql = "";
    try {
      Map<String, Object> listParams = new HashMap<>();
      sql += SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SYN_IT_DEVICES_SOFT, "getListDT_AC");
      lst = getNamedParameterJdbcTemplate().query(sql, listParams,
          BeanPropertyRowMapper.newInstance(MrSynItDevicesDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public MrSynItDevicesDTO findMrDeviceByObjectId(MrSynItDevicesDTO dto) {
    List<MrSynItDevicesEntity> dataEntity = (List<MrSynItDevicesEntity>) findByMultilParam(
        MrSynItDevicesEntity.class, "objectId", Long.valueOf(dto.getObjectId()),
        "deviceType", dto.getDeviceType());
    if (dataEntity != null && dataEntity.size() > 0) {
      MrSynItDevicesDTO mrSynItDevicesDTO = dataEntity.get(0).toDTO();
      return mrSynItDevicesDTO;
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteMrSynItSoftDevice(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrSynItDevicesEntity mrSynItDevicesEntity = getEntityManager()
        .find(MrSynItDevicesEntity.class, id);
    getEntityManager().remove(mrSynItDevicesEntity);
    return resultInSideDto;
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
  public List<MrSynItDevicesDTO> onSearchEntity(MrSynItDevicesDTO mrDeviceDTO, int rowStart,
      int maxRow,
      String sortType, String sortFieldList) {
    return onSearchEntity(MrSynItDevicesEntity.class, mrDeviceDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }
  @Override
  public MrSynItDevicesDTO findMrDeviceById(Long mrDeviceId) {
    if (!StringUtils.isStringNullOrEmpty(mrDeviceId)) {
      List<MrSynItDevicesEntity> lst = findByMultilParam(MrSynItDevicesEntity.class, "id",
          mrDeviceId);
      return lst.isEmpty() ? null : lst.get(0).toDTO();
    }
    return null;
  }

  @Override
  public MrSynItDevicesDTO getMrSynItDevicesDetail(Long id) {
    if (id != null && id != 0L) {
      Map<String, Object> parameters = new HashMap<>();
      String sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SYN_IT_DEVICES_SOFT,
              "getListMrSynItDeviceSoftDTO");
      sqlQuery += " and T1.ID = :id";
      parameters.put("id", id);
      parameters.put("p_leeLocale", I18n.getLocale());
      List<MrSynItDevicesDTO> lst = getNamedParameterJdbcTemplate()
          .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(MrSynItDevicesDTO.class));
      return lst.isEmpty() ? null : lst.get(0);
    }
    return null;
  }

  @Override
  public ResultInSideDto updateUserSoftByMarket(MrSynItDevicesDTO mrSynItDevicesDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    Map<String, Object> parameters = new HashMap<>();
    String sql = "";
    if (mrSynItDevicesDTO != null) {
      sql = "UPDATE OPEN_PM.MR_SYN_IT_DEVICES\n"
          + "SET CREATE_USER_SOFT = :createUserSoft ,\n"
          + "  UPDATE_DATE        = sysdate ,\n"
          + "  UPDATE_USER        = :updateUser\n"
          + "WHERE 1              =1\n"
          + "AND MARKET_CODE     =:country\n"
          + "AND (MR_SOFT = 1\n"
          + "OR MR_SOFT   = 0)";
      parameters.put("createUserSoft", mrSynItDevicesDTO.getCreateUserSoft());
      parameters.put("updateUser", mrSynItDevicesDTO.getUpdateUser());
      parameters.put("country", mrSynItDevicesDTO.getMarketCode());
    }
    int row = getNamedParameterJdbcTemplate().update(sql, parameters);
    resultInSideDto.setQuantitySucc(row);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateMrSynItDevice(MrSynItDevicesDTO mrSynItDevicesDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(mrSynItDevicesDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public Datatable getListMrSynItDeviceSoftDTO(MrSynItDevicesDTO mrSynItDevicesDTO) {
    BaseDto baseDto = sqlSearch(mrSynItDevicesDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrSynItDevicesDTO.getPage(), mrSynItDevicesDTO
            .getPageSize(), MrSynItDevicesDTO.class, mrSynItDevicesDTO.getSortName(),
        mrSynItDevicesDTO.getSortType());
  }

  @Override
  public List<MrSynItDevicesDTO> getListMrSynItDeviceSoftExport(
      MrSynItDevicesDTO mrSynItDevicesDTO) {
    BaseDto baseDto = sqlSearch(mrSynItDevicesDTO);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrSynItDevicesDTO.class));
  }

  @Override
  public MrSynItDevicesDTO checkDeviceTypeByArrayCode(String arrayCode, String deviceType) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SYN_IT_DEVICES_SOFT,
            "checkDeviceTypeByArrayCode");
    sql += " AND (lower(ARRAY_CODE) LIKE :arrayCode ESCAPE '\\' ) ";
    params.put("arrayCode", StringUtils.convertLowerParamContains(arrayCode));
    params.put("deviceType", deviceType);
    List<MrSynItDevicesDTO> mrDeviceDTOList = getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(MrSynItDevicesDTO.class));
    return mrDeviceDTOList.isEmpty() ? null : mrDeviceDTOList.get(0);
  }

  @Override
  public MrSynItDevicesDTO ckeckMrSynItDeviceSoftExist(MrSynItDevicesDTO mrSynItDevicesDTO) {
    List<MrSynItDevicesEntity> lstMrDeviceSoft = findByMultilParam(
        MrSynItDevicesEntity.class, "marketCode", mrSynItDevicesDTO.getMarketCode()
        , "objectCode", mrSynItDevicesDTO.getObjectCode());
    if (lstMrDeviceSoft != null && !lstMrDeviceSoft.isEmpty()) {
      return lstMrDeviceSoft.get(0).toDTO();
    }
    return null;
  }

  public BaseDto sqlSearch(MrSynItDevicesDTO mrSynItDevicesDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SYN_IT_DEVICES_SOFT,
            "getListMrSynItDeviceSoftDTO");
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
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getIsNotSchedule()) && "1"
          .equals(mrSynItDevicesDTO.getIsNotSchedule())
          && !StringUtils
          .isStringNullOrEmpty(mrSynItDevicesDTO.getMrSoft())) {
        sqlQuery += " AND (T1.IS_COMPLETE_SOFT = 1 OR T1.IS_COMPLETE_SOFT IS NULL) ";
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getApproveStatus())) {
        if ("-1".equals(mrSynItDevicesDTO.getApproveStatus())) {
          sqlQuery += " AND T1.APPROVE_STATUS IS NULL ";
        } else {
          sqlQuery += " AND ( T1.APPROVE_STATUS  = :approvaeStatus ) ";
          listParams.put("approvaeStatus", mrSynItDevicesDTO.getApproveStatus());
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
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getRegionSoft())) {
        sqlQuery += " AND T1.REGION_SOFT = :regionSoft ";
        listParams.put("regionSoft", mrSynItDevicesDTO.getRegionSoft());
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
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getMrSoft())) {
        sqlQuery += " and T1.MR_SOFT = :mrSoft ";
        listParams.put("mrSoft", mrSynItDevicesDTO.getMrSoft());
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
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getBoUnit())) {
        sqlQuery += " and T1.BO_UNIT = :boUnit ";
        listParams.put("boUnit", mrSynItDevicesDTO.getBoUnit());
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getCreateUserSoft())) {
        sqlQuery += " and lower(T1.CREATE_USER_SOFT) like :CREATE_USER_SOFT escape '\\' ";
        listParams.put("CREATE_USER_SOFT",
            StringUtils.convertLowerParamContains(mrSynItDevicesDTO.getCreateUserSoft()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getNodeAffected())) {
        sqlQuery += " and lower( T1.NODE_AFFECTED) like :NODE_AFFECTED escape '\\' ";
        listParams.put("NODE_AFFECTED",
            StringUtils.convertLowerParamContains(mrSynItDevicesDTO.getNodeAffected()));
      }
      if (!StringUtils.isStringNullOrEmpty(mrSynItDevicesDTO.getIsRunMop())) {
        if(mrSynItDevicesDTO.getIsRunMop() == 1L){
          sqlQuery += " AND (T1.IS_RUN_MOP = '1' OR T1.IS_RUN_MOP IS NULL)";
        }else {
          sqlQuery += " AND T1.IS_RUN_MOP = '0'";
        }
      }
      sqlQuery += " ORDER BY T1.ID DESC";
    }

    listParams.put("p_leeLocale", I18n.getLocale());
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(listParams);
    return baseDto;
  }

  @Override
  public List<MrSynItDevicesDTO> getDeviceITStationCodeCbb() {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_SYN_IT_DEVICES_HARD, "getDeviceITStationCode");
    List<MrSynItDevicesDTO> data = getNamedParameterJdbcTemplate()
        .query(sql, new HashMap<>(), BeanPropertyRowMapper.newInstance(MrSynItDevicesDTO.class));
    if (data != null && data.size() > 0) {
      return data;
    }
    return new ArrayList<>();
  }
}
