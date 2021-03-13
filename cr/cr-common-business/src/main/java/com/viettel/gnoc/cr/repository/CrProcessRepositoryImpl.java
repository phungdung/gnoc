package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrProcessDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CrProcessRepositoryImpl extends BaseRepository implements CrProcessRepository {

  @Override
  public CrProcessInsideDTO findCrProcessById(Long id) {
    String sql = SQLBuilder.getSqlQueryById(
        SQLBuilder.SQL_MODULE_CR_PROCESS_WO, "find-All-CrProcess");
    Map<String, Object> parameters = new HashMap<>();
    if (id != null) {
      parameters.put("crProcessId", id);
    }
    List<CrProcessInsideDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class));
    if (lst != null && lst.size() > 0) {
      return lst.get(0);
    }
    return null;
  }

  @Override
  public List<CrProcessInsideDTO> findCrProcessIdByTDTT(String id) {
    String sql = SQLBuilder.getSqlQueryById(
        SQLBuilder.SQL_MODULE_CR_PROCESS_WO, "find-Cr-Process-Id-By-Tdtt");
    Map<String, Object> parameters = new HashMap<>();
    if (id != null) {
      sql += "where CR_PROCESS_ID in (:lst_crProcess_id)";
      parameters.put("lst_crProcess_id", Arrays.asList(id.split(",")));
    }
    List<CrProcessInsideDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class));
    if (lst != null && lst.size() > 0) {
      return lst;
    }
    return null;
  }

  @Override
  public List<ItemDataCRInside> getListCrProcessCBB(CrProcessInsideDTO crProcessDTO) {
    List<ItemDataCRInside> lst = new ArrayList();
    List<CrProcessInsideDTO> list = actionGetListProcessType(crProcessDTO);
    if ((list == null) || (list.isEmpty())) {
      return lst;
    }

    StringBuilder sqlQuery = new StringBuilder();
    sqlQuery.append(SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_WO, "cr-process-tree-by-parent"));

    String temp = "";
    for (CrProcessInsideDTO processDTO : list) {
      temp = temp + "  (1," + processDTO.getCrProcessId() + "),";
    }
    temp = temp.substring(0, temp.length() - 1);
    String sql = sqlQuery.toString().replace(":idx", temp);
    Map<String, Object> parameters = new HashMap<>();
    if (crProcessDTO.getParentId() != null) {
      parameters.put("p_parent", crProcessDTO.getParentId());
    } else {
      parameters.put("p_parent", null);
    }
    parameters.put("p_leeLocale", I18n.getLocale());
    parameters.put("p_system", "OPEN_PM");
    parameters.put("p_bussiness", "OPEN_PM.CR_PROCESS");

    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
  }

  @Override
  public List<CrProcessInsideDTO> getListCrProcessLevel3CBB(CrProcessInsideDTO crProcessDTO) {
    StringBuilder sqlQuery = new StringBuilder();
    sqlQuery.append(SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_WO, "get-cr_process-by-parent-id"));
    Map<String, Object> parameters = new HashMap<>();
    if (crProcessDTO.getParentId() != null) {
      parameters.put("parentId", crProcessDTO.getParentId());
    } else {
      return new ArrayList<>();
    }
    parameters.put("p_leeLocale", I18n.getLocale());
    parameters.put("p_system", "OPEN_PM");
    parameters.put("p_bussiness", "OPEN_PM.CR_PROCESS");

    return getNamedParameterJdbcTemplate().query(sqlQuery.toString(), parameters,
        BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class));
  }

  public List<CrProcessInsideDTO> actionGetListProcessType(CrProcessInsideDTO crProcessDTO) {
    List<CrProcessInsideDTO> lst = new ArrayList();
    if (//(StringUtils.isStringNullOrEmpty(form.getCrTypeId()))
      //   || (StringUtils.isStringNullOrEmpty(form.getRiskLevel())) ||
        (StringUtils.isStringNullOrEmpty(crProcessDTO.getDeviceTypeId()))
            || (StringUtils.isStringNullOrEmpty(crProcessDTO.getImpactSegmentId()))) {
      return lst;
    }

    StringBuilder sqlQuery = new StringBuilder();
    sqlQuery.append(SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_WO, "actionGetListProcessType"));

    Map<String, Object> parameters = new HashMap<>();
    if (crProcessDTO != null) {
      if (crProcessDTO.getCrTypeId() != null) {
        sqlQuery.append(" and cps.CR_TYPE_ID =:CR_TYPE_ID ");
        parameters.put("CR_TYPE_ID", crProcessDTO.getCrTypeId());
      }

      if (crProcessDTO.getRiskLevel() != null && crProcessDTO.getRiskLevel() > 0) {
        sqlQuery.append(" and cps.RISK_LEVEL =:RISK_LEVEL ");
        parameters.put("RISK_LEVEL", crProcessDTO.getRiskLevel());
      }

      if (crProcessDTO.getDeviceTypeId() != null && crProcessDTO.getDeviceTypeId() > 0) {
        sqlQuery.append(" and cps.DEVICE_TYPE_ID =:DEVICE_TYPE_ID ");
        parameters.put("DEVICE_TYPE_ID", crProcessDTO.getDeviceTypeId());
      }
      if (crProcessDTO.getImpactSegmentId() != null && crProcessDTO.getImpactSegmentId() > 0) {
        sqlQuery.append(" and cps.IMPACT_SEGMENT_ID =:IMPACT_SEGMENT_ID ");
        parameters.put("IMPACT_SEGMENT_ID", crProcessDTO.getImpactSegmentId());
      }
    }
    lst = getNamedParameterJdbcTemplate().query(
        sqlQuery.toString(), parameters, BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class)
    );

    return lst;
  }

  @Override
  public List<CrProcessDTO> synchCrProcess(List<Long> lstImpactSegment) {
    try {
      StringBuilder sql = new StringBuilder(
          SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_WO, "synch-crProcess"));

      Map<String, Object> params = new HashMap<>();
      if (lstImpactSegment != null && !lstImpactSegment.isEmpty()) {
        sql.append(" and a.IMPACT_SEGMENT_ID in (:impactSegmentId) ");
        params.put("impactSegmentId", lstImpactSegment);
      }
      return getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(CrProcessDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public List<CrProcessDTO> actionGetListProcessTypeOutSide(CrProcessDTO crProcessDTO) {
    List<CrProcessDTO> lst = new ArrayList();
    if (//(StringUtils.isStringNullOrEmpty(form.getCrTypeId()))
      //   || (StringUtils.isStringNullOrEmpty(form.getRiskLevel())) ||
        (StringUtils.isStringNullOrEmpty(crProcessDTO.getDeviceTypeId()))
            || (StringUtils.isStringNullOrEmpty(crProcessDTO.getImpactSegmentId()))) {
      return lst;
    }

    StringBuilder sqlQuery = new StringBuilder();
    sqlQuery.append(SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_WO, "actionGetListProcessType"));

    Map<String, Object> parameters = new HashMap<>();
    if (crProcessDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(crProcessDTO.getCrTypeId())) {
        sqlQuery.append(" and cps.CR_TYPE_ID =:CR_TYPE_ID ");
        parameters.put("CR_TYPE_ID", crProcessDTO.getCrTypeId());
      }

      if (!StringUtils.isStringNullOrEmpty(crProcessDTO.getRiskLevel())) {
        sqlQuery.append(" and cps.RISK_LEVEL =:RISK_LEVEL ");
        parameters.put("RISK_LEVEL", crProcessDTO.getRiskLevel());
      }

      if (!StringUtils.isStringNullOrEmpty(crProcessDTO.getDeviceTypeId())) {
        sqlQuery.append(" and cps.DEVICE_TYPE_ID =:DEVICE_TYPE_ID ");
        parameters.put("DEVICE_TYPE_ID", crProcessDTO.getDeviceTypeId());
      }

      if (!StringUtils.isStringNullOrEmpty(crProcessDTO.getImpactSegmentId())) {
        sqlQuery.append(" and cps.IMPACT_SEGMENT_ID =:IMPACT_SEGMENT_ID ");
        parameters.put("IMPACT_SEGMENT_ID", crProcessDTO.getImpactSegmentId());
      }
    }
    lst = getNamedParameterJdbcTemplate().query(
        sqlQuery.toString(), parameters, BeanPropertyRowMapper.newInstance(CrProcessDTO.class)
    );

    return lst;
  }

  @Override
  public List<ItemDataCR> getListCrProcessCBB(CrProcessDTO form, String locale) {
    List<ItemDataCR> lst = new ArrayList();
    try {
      List<CrProcessDTO> list = actionGetListProcessTypeOutSide(form);
      if ((list == null) || (list.isEmpty())) {
        return lst;
      }
      List<Long> listIds = new ArrayList();
      for (CrProcessDTO crProcessDTO : list) {
        listIds.add(Long.valueOf(crProcessDTO.getCrProcessId()));
      }

      StringBuilder sqlBuilder = new StringBuilder();
      sqlBuilder.append("WITH list_language_exchange AS\n"
          + "  (SELECT LE.LEE_ID,\n"
          + "    LE.APPLIED_SYSTEM,\n"
          + "    LE.APPLIED_BUSSINESS,\n"
          + "    LE.BUSSINESS_ID,\n"
          + "    LE.BUSSINESS_CODE,\n"
          + "    LE.LEE_LOCALE,\n"
          + "    LE.LEE_VALUE\n"
          + "  FROM COMMON_GNOC.LANGUAGE_EXCHANGE LE\n"
          + "  JOIN\n"
          + "    (SELECT *\n"
          + "    FROM COMMON_GNOC.CAT_ITEM\n"
          + "    WHERE CATEGORY_ID = 263\n"
          + "    AND ITEM_CODE     = :p_system\n"
          + "    ) CAT1\n"
          + "  ON LE.APPLIED_SYSTEM = CAT1.ITEM_VALUE\n"
          + "  JOIN\n"
          + "    (SELECT *\n"
          + "    FROM COMMON_GNOC.CAT_ITEM\n"
          + "    WHERE CATEGORY_ID = 262\n"
          + "    AND ITEM_CODE     = :p_bussiness\n"
          + "    ) CAT2\n"
          + "  ON LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE\n"
          + "  WHERE LE.LEE_LOCALE     = :p_leeLocale\n"
          + "  ),\n"
          + "  list_data AS\n"
          + "  (SELECT cr_process_id ,\n"
          + "    cr_process_name ,\n"
          + "    cr_process_code ,\n"
          + "    parent_id,\n"
          + "    RPAD('--|', (level-1)*3, '--|')\n"
          + "    || cr_process_name AS tree,\n"
          + "    level clevel,\n"
          + "    is_active,\n"
          + "    CONNECT_BY_ROOT cr_process_id                       AS root_id,\n"
          + "    LTRIM(SYS_CONNECT_BY_PATH(cr_process_id, '-'), '-') AS path,\n"
          + "    CONNECT_BY_ISLEAF                                   AS isLEAF,\n"
          + "    cps.impact_type ,\n"
          + "    cps.RISK_LEVEL AS thirdValue\n"
          + "    FROM ");

      if (form != null && "1".equals(form.getOtherDept())) {
        sqlBuilder.append(
            " (SELECT   *  FROM   open_pm.cr_process  WHERE    LEVEL < 3 START WITH PARENT_ID is null   CONNECT BY   PRIOR CR_PROCESS_ID = PARENT_ID) ");
      } else {
        sqlBuilder.append(" open_pm.cr_process ");
      }

      sqlBuilder.append("cps\n");
      sqlBuilder.append("  WHERE 1 = 1\n"
          + "    START WITH\n"
          + "    (\n"
          + "      cps.parent_id IS NULL\n"
          + "    AND is_active    =1\n"
          + "    )\n"
          + "    CONNECT BY parent_id = PRIOR cr_process_id\n"
          + "  ORDER SIBLINGS BY cr_process_id\n"
          + "  )\n"
          + "SELECT ld.cr_process_id AS valueStr,\n"
          + "  CASE\n"
          + "    WHEN llx.LEE_VALUE IS NULL\n"
          + "    THEN ld.tree\n"
          + "    ELSE RPAD('--|', (clevel-1)*3, '--|')\n"
          + "      || llx.LEE_VALUE\n"
          + "  END displayStr,\n"
          + "  ld.impact_type\n"
          + "  || ','\n"
          + "  || ld.isleaf AS secondValue ,\n"
          + "  thirdValue\n"
          + "FROM list_data ld\n"
          + "LEFT JOIN list_language_exchange llx\n"
          + "ON ld.cr_process_id     = llx.BUSSINESS_ID\n"
          + "WHERE ld.cr_process_id IN\n"
          + "  (SELECT cps.cr_process_id\n"
          + "  FROM open_pm.cr_process cps\n"
          + "    START WITH\n"
          + "    (1,cps.cr_process_id)"
          + "                              IN (:idx)\n"
          + "    CONNECT BY PRIOR parent_id = cr_process_id\n"
          + "  )\n");

      Map<String, Object> parameters = new HashMap<>();
      if (form != null && !StringUtils.isStringNullOrEmpty(form.getCrProcessName())) {
        sqlBuilder.append(" and lower(ld.CR_PROCESS_NAME) like :crProcessName escape '\\'  ");
        parameters
            .put("crProcessName", StringUtils.convertLowerParamContains(form.getCrProcessName()));
      }
      sqlBuilder.append(" ORDER BY ld.path");

      String temp = "";
      for (CrProcessDTO processDTO : list) {
        temp = temp + "  (1," + processDTO.getCrProcessId() + "),";
      }
      temp = temp.substring(0, temp.length() - 1);
      String sql = sqlBuilder.toString().replace(":idx", temp);
      parameters.put("p_leeLocale", locale);
      parameters.put("p_system", "OPEN_PM");
      parameters.put("p_bussiness", "OPEN_PM.CR_PROCESS");

      return getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(ItemDataCR.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }


}
