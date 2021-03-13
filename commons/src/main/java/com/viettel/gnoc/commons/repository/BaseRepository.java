package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Base repository.
 *
 * @author TungPV
 */
@Repository
@Slf4j
public abstract class BaseRepository<TDTO, T> {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplateLarge;

  protected JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplateNormal() {
    return namedParameterJdbcTemplate;
  }

  protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    namedParameterJdbcTemplateLarge.getJdbcTemplate().setFetchSize(1000);
    return namedParameterJdbcTemplateLarge;
  }

  protected EntityManager getEntityManager() {
    return entityManager;
  }

  public Datatable getListDataTableBySqlQuery(String sqlQuery,
      Map<String, Object> parameters,
      int page, int pageSize,
      Class<?> mappedClass,
      String sortName, String sortType) {
    log.info("--- Start request to search data {} ---");
    Date startTime = new Date();
    Datatable dataReturn = new Datatable();
    //Oracle
    String sqlQueryResult = " SELECT * FROM ( "
        + " SELECT * FROM ( SELECT a.*, rownum indexRow FROM ( "
        + " SELECT * FROM ( "
        + sqlQuery
        + " ) ";
    if (StringUtils.isNotNullOrEmpty(sortName)) {
      Field[] fields = FieldUtils.getAllFields(mappedClass);
      Map<String, String> mapField = new HashMap<>();
      for (Field field : fields) {
        mapField.put(field.getName(), field.getName());
      }
      if ("asc".equalsIgnoreCase(sortType)) {
        sqlQueryResult += " ORDER BY " + mapField.get(sortName) + " asc";
      } else if ("desc".equalsIgnoreCase(sortType)) {
        sqlQueryResult += " ORDER BY " + mapField.get(sortName) + " desc";
      } else {
        sqlQueryResult += " ORDER BY " + mapField.get(sortName);
      }
    }
    sqlQueryResult +=
        " ) a WHERE rownum < ((:p_page_number * :p_page_length) + 1 )) WHERE indexRow >= (((:p_page_number-1) * :p_page_length) + 1) "
            + " ) T_TABLE_NAME, ";
    sqlQueryResult += " ( SELECT COUNT(*) totalRow FROM ( "
        + sqlQuery
        + " ) T_TABLE_TOTAL ) ";
    sqlQueryResult += " T_TABLE_NAME_TOTAL ";
    parameters.put("p_page_number", page);
    parameters.put("p_page_length", pageSize);
    List<?> list = getNamedParameterJdbcTemplateNormal()
        .query(sqlQueryResult, parameters, BeanPropertyRowMapper.newInstance(mappedClass));
    int count = 0;
    if (list.isEmpty()) {
      dataReturn.setTotal(count);
    } else {
      try {
        Object obj = list.get(0);
        Field field = obj.getClass().getSuperclass().getDeclaredField("totalRow");
        field.setAccessible(true);
        count = Integer.parseInt(field.get(obj).toString());
        dataReturn.setTotal(count);
      } catch (NoSuchFieldException e) {
        log.debug(e.getMessage(), e);
      } catch (IllegalAccessException e) {
        log.debug(e.getMessage(), e);
      }
    }
    if (pageSize > 0) {
      if (count % pageSize == 0) {
        dataReturn.setPages(count / pageSize);
      } else {
        dataReturn.setPages((count / pageSize) + 1);
      }
    }
    dataReturn.setData(list);
    log.info(
        "------End search : time " + (new Date().getTime() - startTime.getTime()) + " miliseconds");
    return dataReturn;
  }

  @SuppressWarnings("unchecked")
  public List<T> findAll(Class<T> persistentClass) {
    String sqlQuery = " Select t from " + persistentClass.getSimpleName() + " t";
    return entityManager.createQuery(sqlQuery).getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<T> findByMultilParam(Class<T> persistentClass, Object... params) {
    Map<String, Object> mapParams = new HashMap<>();
    String sqlQuery = " Select t from " + persistentClass.getSimpleName() + " t WHERE 1=1 ";
    if (params.length > 0) {
      for (int i = 0; i < params.length; i++) {
        if (i % 2 == 0) {
          sqlQuery += " AND t." + params[i] + " = :p_" + params[i] + " ";
          mapParams.put("p_" + params[i], params[i + 1]);
        }
      }
    }
    Query query = entityManager.createQuery(sqlQuery);
    for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
      query.setParameter(entry.getKey(), entry.getValue());
    }
    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<T> findByMultilParamNull(Class<T> persistentClass, Object... params) {
    Map<String, Object> mapParams = new HashMap<>();
    String sqlQuery = " Select t from " + persistentClass.getSimpleName() + " t WHERE 1=1 ";
    if (params.length > 0) {
      for (int i = 0; i < params.length; i++) {
        if (i % 2 == 0) {
          if (params[i + 1] != null) {
            sqlQuery += " AND t." + params[i] + " = :p_" + params[i] + " ";
            mapParams.put("p_" + params[i], params[i + 1]);
          } else {
            sqlQuery += " AND t." + params[i] + " is null ";
          }
        }
      }
    }
    Query query = entityManager.createQuery(sqlQuery);
    for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
      query.setParameter(entry.getKey(), entry.getValue());
    }
    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  public int deleteByMultilParam(Class<T> persistentClass, Object... params) {
    Map<String, Object> mapParams = new HashMap<>();
    String sqlQuery = " Delete from " + persistentClass.getSimpleName() + " t WHERE 1=1 ";
    if (params.length > 0) {
      for (int i = 0; i < params.length; i++) {
        if (i % 2 == 0) {
          sqlQuery += " AND t." + params[i] + " = :p_" + params[i] + " ";
          mapParams.put("p_" + params[i], params[i + 1]);
        }
      }
    }
    Query query = entityManager.createQuery(sqlQuery);
    for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
      query.setParameter(entry.getKey(), entry.getValue());
    }
    return query.executeUpdate();
  }

  public static Map<String, Object> getSqlLanguageExchange(String appliedSystem,
      String appliedBussiness, String leeLocale) {

    Map<String, Object> result = new HashMap<String, Object>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_BASE, "base-language-exchange");
    result.put("sql", sql);
    Map mapParam = new HashMap();
    mapParam.put("appliedSystem", appliedSystem);
    mapParam.put("appliedBussiness", appliedBussiness);
    mapParam.put("leeLocale", "" + leeLocale.toLowerCase() + "%");

    Map mapType = new HashMap();
    mapType.put("appliedSystem", "Long");
    mapType.put("appliedBussiness", "Long");
    mapType.put("leeLocale", "String");

    result.put("mapParam", mapParam);
    result.put("mapType", mapType);
    return result;
  }

  public static List setLanguage(List lstInput, List<LanguageExchangeDTO> lstLangguageExchange,
      String fieldId, String fieldName) throws Exception {
    List result = new ArrayList();
    try {

      Map<String, String> mapLan = new HashMap<String, String>();
      for (LanguageExchangeDTO dto : lstLangguageExchange) {
        mapLan.put(dto.getBussinessId().toString(), dto.getLeeValue());
      }
      for (Object row : lstInput) {
        try {
          Field fId = row.getClass().getDeclaredField(fieldId);
          Field fName = row.getClass().getDeclaredField(fieldName);
          fId.setAccessible(true);
          fName.setAccessible(true);
          String value = mapLan.get(fId.get(row).toString());
          if (value != null && !"".equals(value)) {
            fName.set(row, value);
          }
          result.add(row);
        } catch (Exception e) {
          log.debug(e.getMessage(), e);
          result.add(row);
        }
      }
    } catch (Exception e) {
      log.debug(e.getMessage(), e);
      result = lstInput;
    }
    return result;
  }

  /**
   * Check unique with singe field
   */
  public boolean checkUnique(Class<T> persistentClass, String uniqueField, Object uniqueValue,
      String idField, Long idValue) {
    String sqlQuery = " Select t from " + persistentClass.getSimpleName() + " t WHERE 1=1 ";
    if (uniqueValue instanceof String) {
      sqlQuery += " AND lower(t." + uniqueField + ") = :p_" + uniqueField + " ";
    } else {
      sqlQuery += " AND t." + uniqueField + " = :p_" + uniqueField + " ";
    }
    sqlQuery += " AND t." + idField + " <> :p_" + idField;
    Query query = entityManager.createQuery(sqlQuery);
    if (uniqueValue instanceof String) {
      query.setParameter("p_" + uniqueField, uniqueValue.toString().trim().toLowerCase());
    } else {
      query.setParameter("p_" + uniqueField, uniqueValue);
    }
    query.setParameter("p_" + idField, idValue);
    List<T> lst = query.getResultList();
    return lst == null || lst.size() == 0;
  }

  /**
   * Check unique with multiple field
   */
  public boolean checkUniqueWithMultiFields(Class<T> persistentClass, String idField, Long idValue,
      Object... params) {
    Map<String, Object> mapParams = new HashMap<>();
    String sqlQuery = " Select t from " + persistentClass.getSimpleName() + " t WHERE 1=1 ";
    if (params.length > 0) {
      for (int i = 0; i < params.length; i++) {
        if (i % 2 == 0) {
          if (params[i + 1] != null) {
            boolean isNumber = false;
            try {
              Field field = persistentClass.getDeclaredField(String.valueOf(params[i]));
              String returnType = field.getType().getSimpleName().toUpperCase();
              if (Constants.TYPE_NUMBER.indexOf(returnType) >= 0) {
                isNumber = true;
              }
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
            if (params[i + 1] instanceof String && !isNumber) {
              sqlQuery += " AND lower(t." + params[i] + ") = :p_" + params[i] + " ";
              mapParams.put("p_" + params[i], params[i + 1].toString().trim().toLowerCase());
            } else {
              sqlQuery += " AND t." + params[i] + " = :p_" + params[i] + " ";
              mapParams.put("p_" + params[i], params[i + 1]);
            }
          }
        }
      }
    }
    sqlQuery += " AND t." + idField + " <> :p_" + idField;
    Query query = entityManager.createQuery(sqlQuery);
    for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
      try {
        String fieldName = entry.getKey().split("_")[1];
        Field field = persistentClass.getDeclaredField(fieldName);
        String returnType = field.getType().getSimpleName().toUpperCase();
        if ("Double".equalsIgnoreCase(returnType)) {
          query.setParameter(entry.getKey(), Double.valueOf(String.valueOf(entry.getValue())));
        } else if ("Integer".equalsIgnoreCase(returnType)) {
          query.setParameter(entry.getKey(), Integer.valueOf(String.valueOf(entry.getValue())));
        } else if ("Float".equalsIgnoreCase(returnType)) {
          query.setParameter(entry.getKey(), Float.valueOf(String.valueOf(entry.getValue())));
        } else if ("Short".equalsIgnoreCase(returnType)) {
          query.setParameter(entry.getKey(), Short.valueOf(String.valueOf(entry.getValue())));
        } else if ("Long".equalsIgnoreCase(returnType)) {
          query.setParameter(entry.getKey(), Long.valueOf(String.valueOf(entry.getValue())));
        } else {
          query.setParameter(entry.getKey(), entry.getValue());
        }
        continue;
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      query.setParameter(entry.getKey(), entry.getValue());
    }
    query.setParameter("p_" + idField, idValue);
    List<T> lst = query.getResultList();
    return lst == null || lst.size() == 0;
  }

  /**
   * Check unique with multiple field has field null
   */
  public boolean checkUniqueWithMultiFieldsHasNull(Class<T> persistentClass, String idField,
      Long idValue, Map<String, Object> mapFields, Object... params) {
    Map<String, Object> mapParams = new HashMap<>();
    String sqlQuery = " Select t from " + persistentClass.getSimpleName() + " t WHERE 1=1 ";
    if (params.length > 0) {
      for (int i = 0; i < params.length; i++) {
        boolean isNumber = false;
        try {
          Field field = persistentClass.getDeclaredField(String.valueOf(params[i]));
          String returnType = field.getType().getSimpleName().toUpperCase();
          if (Constants.TYPE_NUMBER.indexOf(returnType) >= 0) {
            isNumber = true;
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        if (mapFields.get(params[i]) != null) {
          if (mapFields.get(params[i]) instanceof String && !isNumber) {
            sqlQuery += " AND lower(t." + params[i] + ") = :p_" + params[i] + " ";
            mapParams
                .put("p_" + params[i], mapFields.get(params[i]).toString().trim().toLowerCase());
          } else {
            sqlQuery += " AND t." + params[i] + " = :p_" + params[i] + " ";
            mapParams.put("p_" + params[i], mapFields.get(params[i]));
          }
        } else {
          sqlQuery += " AND t." + params[i] + " is null";
        }
      }
    }
    sqlQuery += " AND t." + idField + " <> :p_" + idField;
    Query query = entityManager.createQuery(sqlQuery);
    for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
      try {
        String fieldName = entry.getKey().split("_")[1];
        Field field = persistentClass.getDeclaredField(fieldName);
        String returnType = field.getType().getSimpleName().toUpperCase();
        if ("Double".equalsIgnoreCase(returnType)) {
          query.setParameter(entry.getKey(), Double.valueOf(String.valueOf(entry.getValue())));
        } else if ("Integer".equalsIgnoreCase(returnType)) {
          query.setParameter(entry.getKey(), Integer.valueOf(String.valueOf(entry.getValue())));
        } else if ("Float".equalsIgnoreCase(returnType)) {
          query.setParameter(entry.getKey(), Float.valueOf(String.valueOf(entry.getValue())));
        } else if ("Short".equalsIgnoreCase(returnType)) {
          query.setParameter(entry.getKey(), Short.valueOf(String.valueOf(entry.getValue())));
        } else if ("Long".equalsIgnoreCase(returnType)) {
          query.setParameter(entry.getKey(), Long.valueOf(String.valueOf(entry.getValue())));
        } else {
          query.setParameter(entry.getKey(), entry.getValue());
        }
        continue;
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      query.setParameter(entry.getKey(), entry.getValue());
    }
    query.setParameter("p_" + idField, idValue);
    List<T> lst = query.getResultList();
    return lst == null || lst.size() == 0;
  }

  public List<LanguageExchangeDTO> getLanguageExchange(String appliedSystem,
      String appliedBussiness) {
    String leeLocale = I18n.getLocale();
    Map<String, Object> map = getSqlLanguageExchange(appliedSystem, appliedBussiness, leeLocale);
    String sqlLanguage = (String) map.get("sql");
    Map mapParam = (Map) map.get("mapParam");
    List<LanguageExchangeDTO> lstLanguage = findBySql(sqlLanguage, mapParam);
    return lstLanguage;
  }

  public List<LanguageExchangeDTO> getLanguageExchangeByLocale(String appliedSystem,
      String appliedBussiness, String locale) {
    String leeLocale = I18n.getLocale();
    if (!StringUtils.isStringNullOrEmpty(locale)) {
      leeLocale = locale;
    }
    Map<String, Object> map = getSqlLanguageExchange(appliedSystem, appliedBussiness, leeLocale);
    String sqlLanguage = (String) map.get("sql");
    Map mapParam = (Map) map.get("mapParam");
    List<LanguageExchangeDTO> lstLanguage = findBySql(sqlLanguage, mapParam);
    return lstLanguage;
  }

  public List<LanguageExchangeDTO> findBySql(String sql, Map<String, String> mapParam) {
    List<LanguageExchangeDTO> exchangeDTOList = getNamedParameterJdbcTemplate()
        .query(sql, mapParam, BeanPropertyRowMapper.newInstance(LanguageExchangeDTO.class));
    return exchangeDTOList;
  }

  /**
   * TienNV
   */
  public String getSeqTableBase(String sequense) {
    String sql = "SELECT " + sequense + ".nextval from dual";
    Query query = getEntityManager().createNativeQuery(sql);
    BigDecimal bigDecimal = (BigDecimal) query.getSingleResult();
    return bigDecimal.toString();
  }

  public Long getMaxRowTable(String tableCode) {
    String sql = "select value from common_gnoc.config_property where key = :code and ROWNUM = 1";
    Query query = getEntityManager().createNativeQuery(sql);
    query.setParameter("code", tableCode);
    String data = (String) query.getSingleResult();
    try {
      return Long.parseLong(data);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return 0L;
  }

  public List<String> getListSequense(String sequense, int... size) {
    int number = (size[0] > 0 ? size[0] : 1);
    List<String> lstSequense = new ArrayList<>();
    for (int i = 0; i < number; i++) {
      lstSequense.add(getSeqTableBase(sequense));
    }
    return lstSequense;
  }

  public ResultInSideDto insertByModel(T model, String colId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setMessage(RESULT.SUCCESS);
    getEntityManager().persist(model);
    try {
      Method method = model.getClass()
          .getMethod("get" + colId.substring(0, 1).toUpperCase() + colId.substring(1));
      Long idValue = (Long) method.invoke(model);
      resultInSideDto.setId(idValue);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return resultInSideDto;
  }

  public String deleteById(Class<T> entity, Long id, String colId) {
    String sqlDelete = " DELETE FROM " + entity.getSimpleName() + " t" +
        " WHERE t." + colId + " = :id ";
    Query query = getEntityManager().createQuery(sqlDelete);
    query.setParameter("id", id);
    int result = query.executeUpdate();
    if (result > 0) {
      return RESULT.SUCCESS;
    }
    return RESULT.ERROR;
  }

  public String deleteByListDTO(List<TDTO> dtos, Class<T> entity, String colId) {
    List<Object> lstId = new ArrayList<>();
    for (TDTO obj : dtos) {
      try {
        Method mtoEntity = obj.getClass().getMethod("toEntity");
        Object model = mtoEntity.invoke(obj);
        Method method = model.getClass()
            .getMethod("get" + colId.substring(0, 1).toUpperCase() + colId.substring(1));
        Long idValue = (Long) method.invoke(model);
        lstId.add(idValue);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    String sqlDelete = " DELETE FROM " + entity.getSimpleName() + " t" +
        " WHERE t." + colId + " in (:idx) ";
    Query query = getEntityManager().createQuery(sqlDelete);
    query.setParameter("idx", lstId);
    int result = query.executeUpdate();
    if (result > 0) {
      return RESULT.SUCCESS;
    }
    return RESULT.ERROR;
  }

  public List<TDTO> onSearchEntity(Class<T> entity, TDTO tForm, int start, int maxResult,
      String sortType, String sortField) {
    try {
      Method mtoEntity = tForm.getClass().getMethod("toEntity");
      Object model = mtoEntity.invoke(tForm);
      return convertListModeltoDTO(
          findByCondition(entity.getSimpleName(), prepareCondition((T) model, tForm),
              buildOrder((T) model, sortType, sortField), start, maxResult));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public List<TDTO> onSearchByConditionBean(T entity, List<ConditionBean> lstConditionBean,
      int start, int maxResult, String sortType, String sortField) {
    try {
      return convertListModeltoDTO(
          findByCondition(entity.getClass().getSimpleName(),
              lstConditionBean,
              buildOrder(entity, sortType, sortField),
              start, maxResult));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public List<TDTO> searchBySql(String sql, Map<String, String> mapParam,
      Map<String, String> mapType) {
    Query query = getEntityManager().createQuery(sql);
    for (String paramName : mapType.keySet()) {
      if ("String".equals(mapType.get(paramName))) {
        query.setParameter(paramName, mapParam.get(paramName));
      }
      if ("Long".equals(mapType.get(paramName))) {
        query.setParameter(paramName, Long.valueOf(mapParam.get(paramName)));
      }
    }
    return convertListModeltoDTO(query.getResultList());
  }

  public List<T> findByCondition(String boName, List<ConditionBean> lstCondition, String order,
      int start, int maxResult) {
    try {
      StringBuilder sql = new StringBuilder();
      sql.append(" from ");
      sql.append(boName);
      sql.append(" where 1=1 ");
      if (lstCondition != null && lstCondition.size() > 0) {
        ConditionBeanUtil.buildConditionQuery(sql, lstCondition);
      }
      if (order != null && !"".equals(order)) {
        sql.append(" order by ");
        sql.append(order);
      }
      Query query = getEntityManager().createQuery(sql.toString());
      if (maxResult != 0) {
        query.setFirstResult(start);
        query.setMaxResults(maxResult);
      }
      ConditionBeanUtil.fillConditionQuery(query, lstCondition);
      return query.getResultList();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  public List prepareCondition(T tmodel, TDTO tForm) {
    List<ConditionBean> lstCondition = new ArrayList<>();
    if (tForm == null) {
      return lstCondition;
    }
    Field fields[] = tmodel.getClass().getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {
      try {
        String returnType = fields[i].getType().getSimpleName().toUpperCase();
        String fieldName = fields[i].getName();
        Method method = tmodel.getClass()
            .getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
        Method mtoEntity = tForm.getClass().getMethod("toEntity");
        Object model = mtoEntity.invoke(tForm);
        Object value = method.invoke(model);
        if (value != null && !"".equals(value)) {
          if (Constants.STRING.indexOf(returnType) >= 0) {
            if (!value.equals(String.valueOf(Constants.OPERATOR_SQL.DEFAULT_VALUE))) {
              String stringValue = value.toString();
              String opCompare = Constants.OPERATOR_SQL.OP_LIKE;
              String valueCompare = StringUtils.formatLike(stringValue);
              Column column = fields[i].getAnnotation(Column.class);
              if (StringUtils.validString(column.columnDefinition())
                  && "param".equals(column.columnDefinition())) {
                opCompare = Constants.OPERATOR_SQL.OP_EQUAL;
                valueCompare = stringValue.toLowerCase();
              }

              if (!"".equals(stringValue.trim())) {
                lstCondition.add(new ConditionBean(
                    StringUtils.formatFunction("lower", fields[i].getName()),
                    valueCompare,
                    opCompare,
                    Constants.STRING));
              }
            }
          } else if (Constants.TYPE_NUMBER.indexOf(returnType) >= 0) {
            if (!value.toString().equals(String.valueOf(Constants.OPERATOR_SQL.DEFAULT_VALUE))) {
              lstCondition.add(new ConditionBean(
                  fields[i].getName(),
                  value.toString(),
                  Constants.OPERATOR_SQL.OP_EQUAL,
                  Constants.TYPE_NUMBER));
            }
          } else if (Constants.TYPE_DATE.indexOf(returnType) >= 0) {
            Date dateValue = (Date) value;
            String operator = Constants.OPERATOR_SQL.OP_EQUAL;
            if (fieldName.toLowerCase().indexOf("from") >= 0
                || fieldName.indexOf("begin") >= 0
                || fieldName.indexOf("sta") >= 0) {
              operator = Constants.OPERATOR_SQL.OP_GREATER_EQUAL;
            } else if (fieldName.toLowerCase().indexOf("to") >= 0
                || fieldName.toLowerCase().indexOf("end") >= 0
                || fieldName.toLowerCase().indexOf("last") >= 0) {
              operator = Constants.OPERATOR_SQL.OP_LESS_EQUAL;
            }
            lstCondition.add(new ConditionBean(
                StringUtils.formatFunction("trunc", fieldName),
                StringUtils.formatDate(dateValue),
                operator,
                Constants.TYPE_DATE));
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return lstCondition;
  }

  private String buildOrder(T tForm, String sortType, String sortField) {
    String order = "";
    try {
      Map<String, String> lstHashMap = buildHasMapOrder(sortType, sortField);
      if (tForm == null) {
        for (String item : lstHashMap.keySet()) {
          order = order + " , " + item + " " + lstHashMap.get(item);
        }
        return order.replaceFirst(",", "");
      }
      for (String item : lstHashMap.keySet()) {
        try {
          Field fields = tForm.getClass().getDeclaredField(item);
          String returnType = fields.getType().getSimpleName().toUpperCase();
          if (Constants.TYPE_NUMBER.indexOf(returnType) >= 0) {
            order = order + " , " + item + " " + lstHashMap.get(item);
          } else if (Constants.TYPE_DATE.indexOf(returnType) >= 0) {
            order = order + " , " + item + " " + lstHashMap.get(item);
          } else {
            order = order + " , " + StringUtils.formatOrder(item, lstHashMap.get(item));
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return order.replaceFirst(",", "");
  }

  private Map<String, String> buildHasMapOrder(String sortType, String sortField) {
    Map<String, String> lstHashMap = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(sortType) && StringUtils.isNotNullOrEmpty(sortField)) {
      List<String> lstSortType = DataUtil.parseInputList(sortType);
      List<String> lstSortField = DataUtil.parseInputList(sortField);
      for (int i = 0; i < lstSortField.size(); i++) {
        if (i < lstSortType.size()) {
          lstHashMap.put(lstSortField.get(i), lstSortType.get(i));
        } else {
          lstHashMap.put(lstSortField.get(i), "ASC");
        }
      }
    }
    return lstHashMap;
  }

  private List convertListModeltoDTO(List<T> listModel) {
    List<TDTO> lstForm = new ArrayList<>();
    if (listModel != null) {
      for (T model : listModel) {
        try {
          Method method = model.getClass().getMethod("toDTO");
          Object obj = method.invoke(model);
          lstForm.add((TDTO) obj);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    }
    return lstForm;
  }

  private Object CheckReturnType(String d, String returnType) {
    if ("Double".equalsIgnoreCase(returnType)) {
      return Double.valueOf(d);
    } else if ("Integer".equalsIgnoreCase(returnType)) {
      return Integer.valueOf(d);
    } else if ("Float".equalsIgnoreCase(returnType)) {
      return Float.valueOf(d);
    } else if ("Short".equalsIgnoreCase(returnType)) {
      return Short.valueOf(d);
    }
    return d;
  }

  public List<TDTO> onSearchOutSideDTO(Class<T> entity, TDTO tForm, int start, int maxResult,
      String sortType, String sortField) {
    try {
      Method mtoEntity = tForm.getClass().getMethod("toEntity");
      Object model = mtoEntity.invoke(tForm);
      return convertListModelToOutSideDTO(
          findByCondition(entity.getSimpleName(), prepareCondition((T) model, tForm),
              buildOrder((T) model, sortType, sortField), start, maxResult));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException();
    }
  }


  private List convertListModelToOutSideDTO(List<T> listModel) {
    List<TDTO> lstForm = new ArrayList<>();
    if (listModel != null) {
      for (T model : listModel) {
        try {
          Method method = model.getClass().getMethod("toOutSideDTO");
          Object obj = method.invoke(model);
          lstForm.add((TDTO) obj);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    }
    return lstForm;
  }

  public List<?> getListBySqlQuery2(String sqlQuery,
      Map<String, Object> parameters,
      int page, int pageSize,
      Class<?> mappedClass, boolean isPageParam) {
    log.info("--- Start request to search data {} ---");

    int rowEnd = page*pageSize + 1;
    int rowStart = (page - 1) * pageSize;
    //Oracle
    String sqlQueryResult = "select * from ( select row_.*, rownum rownum_ from (  select * from ( "
        + sqlQuery
        + ") ";
    String sqlAfter;
    if(!isPageParam) {
      sqlAfter =  " ) row_ where rownum < "  + rowEnd
          + ") where rownum_ >  " + rowStart;
    } else {
      sqlAfter =  " ) row_ where rownum < :rowEnd"
          + ") where rownum_ >  :rowStart";
      parameters.put("rowEnd", rowEnd);
      parameters.put("rowStart", rowStart);
    }

    sqlQueryResult += sqlAfter ;
    Date startTime = new Date();
    List<?> list = getNamedParameterJdbcTemplate()
        .query(sqlQueryResult, parameters, BeanPropertyRowMapper.newInstance(mappedClass));

    log.info(
        "------End search : time " + (new Date().getTime() - startTime.getTime()) + " miliseconds");
    return list;
  }

  public String buildSortCondition(Class<?> mappedClass,
      String sortName, String sortType, String defaultSortName, String defaultSortType) {
    String sqlQueryResult = "";
    Map<String, String> mapField = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(sortName)) {
      Field[] fields = FieldUtils.getAllFields(mappedClass);
      for (Field field : fields) {
        mapField.put(field.getName(), field.getName());
      }
    }
    String sortNameFinal = StringUtils.isNotNullOrEmpty(mapField.get(sortName)) ? mapField.get(sortName) : defaultSortName;
    if(StringUtils.isNotNullOrEmpty(sortNameFinal)) {
      if ("asc".equalsIgnoreCase(sortType)) {
        sqlQueryResult = " ORDER BY " + sortNameFinal + " asc";
      } else if ("desc".equalsIgnoreCase(sortType)) {
        sqlQueryResult = " ORDER BY " + sortNameFinal + " desc";
      } else {
        sqlQueryResult = " ORDER BY " + sortNameFinal + " " + defaultSortType;
      }
    }
    return sqlQueryResult;
  }

  public List<?> getListDataByParamList(Class<?> mapperClass,
      String sql, List<String> lstStrIdx, List<Long> lstLongIdx, int sizing) {
    List <String> tempStrList = new ArrayList<>();
    List resultFinal = new ArrayList();
    if (lstStrIdx != null && lstStrIdx.size() > 0) {
      for (int i = 0; i < lstStrIdx.size(); i++) {
        tempStrList.add(lstStrIdx.get(i));
        if ((i != 0 && i % sizing == 0) || i == lstStrIdx.size() - 1) {
          Map idMaps = Collections.singletonMap("idx", tempStrList);
           List lstData = getNamedParameterJdbcTemplate().query(sql, idMaps, BeanPropertyRowMapper.newInstance(mapperClass));
           if (lstData != null && lstData.size() > 0) {
             resultFinal.addAll(lstData);
           }
          tempStrList.clear();
        }
      }
    } else if (lstLongIdx != null && lstLongIdx.size() > 0) {
      for (int i = 0; i < lstLongIdx.size(); i++) {
        if (lstLongIdx.get(i) != null) {
          tempStrList.add(lstLongIdx.get(i).toString());
        }
        if ((i != 0 && i % sizing == 0) || i == lstLongIdx.size() - 1) {
          Map idMaps = Collections.singletonMap("idx", tempStrList);
          List lstData = getNamedParameterJdbcTemplate().query(sql, idMaps, BeanPropertyRowMapper.newInstance(mapperClass));
          if (lstData != null && lstData.size() > 0) {
            resultFinal.addAll(lstData);
          }
          tempStrList.clear();
        }
      }
    }
    return resultFinal;
  }
}
