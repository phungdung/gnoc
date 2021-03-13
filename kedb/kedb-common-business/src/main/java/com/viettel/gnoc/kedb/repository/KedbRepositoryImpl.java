package com.viettel.gnoc.kedb.repository;


import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.model.CatItemEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.KEDB_MASTER_CODE;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.kedb.dto.KedbDTO;
import com.viettel.gnoc.kedb.dto.KedbFilesDTO;
import com.viettel.gnoc.kedb.model.KedbEntity;
import com.viettel.gnoc.kedb.model.KedbFilesEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class KedbRepositoryImpl extends BaseRepository implements KedbRepository {

  @Override
  public Datatable getListKedbDTO(KedbDTO kedbDTO) {
    BaseDto baseDto = sqlGetListKedbDTO(kedbDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        kedbDTO.getPage(), kedbDTO.getPageSize(), KedbDTO.class, kedbDTO.getSortName(),
        kedbDTO.getSortType());
    List<KedbDTO> list = (List<KedbDTO>) datatable.getData();
    list = setLanguageKedb(list);
    datatable.setData(list);
    return datatable;
  }

  public List<KedbDTO> setLanguageKedb(List<KedbDTO> list) {
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
        Constants.APPLIED_BUSSINESS.CAT_ITEM.toString());
    try {
      list = setLanguage(list, lstLanguage, "kedbState", "kedbStateName");
      list = setLanguage(list, lstLanguage, "vendor", "vendorName");
      list = setLanguage(list, lstLanguage, "typeId", "typeName");
      list = setLanguage(list, lstLanguage, "subCategoryId", "subCategoryName");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  @Override
  public List<KedbDTO> getListKedbExport(KedbDTO kedbDTO) {
    BaseDto baseDto = sqlGetListKedbDTO(kedbDTO);
    List<KedbDTO> list = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(KedbDTO.class));
    list = setLanguageKedb(list);
    return list;
  }

  @Override
  public KedbDTO findKedbById(Long kedbId, String userName) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_KEDB, "find-Kedb-By-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("userName", userName);
    sql += " AND k.KEDB_ID = :kedbId";
    parameters.put("kedbId", kedbId);
    List<KedbDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(KedbDTO.class));
    if (list != null && !list.isEmpty()) {
      KedbDTO dto = list.get(0);
      return dto;
    }
    return null;
  }

  @Override
  public String setStateName(KedbDTO kedbDTO) {
    List<CatItemDTO> list = new ArrayList<>();
    List<CatItemDTO> listOld = new ArrayList<>();
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
        Constants.APPLIED_BUSSINESS.CAT_ITEM.toString());
    String stateName = "";
    CatItemDTO catItemDTO = getEntityManager()
        .find(CatItemEntity.class, kedbDTO.getKedbStateBeforeUpdate()).toDTO();
    if (catItemDTO != null) {
      listOld = setLanguageState(listOld, lstLanguage, catItemDTO);
      stateName = listOld.get(0).getItemName();
    }
    if (kedbDTO.getKedbState() != null) {
      catItemDTO = getEntityManager().find(CatItemEntity.class, kedbDTO.getKedbState()).toDTO();
      if (catItemDTO != null) {
        list = setLanguageState(list, lstLanguage, catItemDTO);
        stateName = stateName + " --> " + list.get(0).getItemName();
      }
    }
    return stateName;
  }

  private List<CatItemDTO> setLanguageState(List<CatItemDTO> listCat,
      List<LanguageExchangeDTO> lstLanguage, CatItemDTO catItemDTO) {
    listCat.add(catItemDTO);
    try {
      listCat = setLanguage(listCat, lstLanguage, "itemId", "itemName");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return listCat;
  }

  @Override
  public ResultInSideDto doInsertKedb(KedbDTO kedbDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    KedbEntity kedbEntity = getEntityManager().merge(kedbDTO.toEntity());
    resultInSideDto.setId(kedbEntity.getKedbId());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto doUpdateKedb(KedbDTO kedbDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().merge(kedbDTO.toEntity());
    resultInSideDto.setId(kedbDTO.getKedbId());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteKedb(Long kedbId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    KedbEntity entity = getEntityManager().find(KedbEntity.class, kedbId);
    getEntityManager().remove(entity);
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public List<String> getSequenseKedb(String seqName, int... size) {
    int number = (size[0] > 0 ? size[0] : 1);
    return getListSequense(seqName, number);
  }

  @Override
  public String getSeqTableKedb(String seq) {
    return getSeqTableBase(seq);
  }

  @Override
  public ResultInSideDto insertOrUpdateListKedb(List<KedbDTO> listKedbDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (KedbDTO dto : listKedbDTO) {
      if (dto.getKedbId() != null) {
        resultInSideDto = doUpdateKedb(dto);
      } else {
        resultInSideDto = doInsertKedb(dto);
      }
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public String getOffset(UserTokenGNOCSimple userTokenGNOC) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_KEDB, "get-Offset");
    Query query = getEntityManager().createNativeQuery(sql);
    query.setParameter("userName", userTokenGNOC.getUserName());
    BigDecimal bigDecimal = (BigDecimal) query.getSingleResult();
    return bigDecimal.toString();
  }

  @Override
  public List<KedbDTO> synchKedbByCreateTime(String fromDate, String toDate) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_KEDB, "get-List-Kedb-DTO");
    Map<String, Object> parameters = new HashMap<>();
    if (fromDate != null && !"".equals(fromDate)) {
      sql += " and createdTime >= to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss')";
      parameters.put("fromDate", fromDate);
    }
    if (toDate != null && !"".equals(toDate)) {
      sql += " and createdTime <= to_date(:toDate,'dd/MM/yyyy HH24:mi:ss')";
      parameters.put("toDate", toDate);
    }
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(KedbDTO.class));
  }

  @Override
  public List<KedbDTO> getListKedbByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    return onSearchByConditionBean(new KedbEntity(),
        lstCondition, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<CatItemDTO> getListSubCategory(Long typeId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_KEDB, "get-List-SubCategory");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("ptSubCategory", KEDB_MASTER_CODE.PT_SUB_CATEGORY);
    if (typeId != null) {
      sql += " AND PARENT_ITEM_ID = :typeId";
      parameters.put("typeId", typeId);
    }
    sql += " ORDER BY NLSSORT(ITEM_NAME,'NLS_SORT=vietnamese') ASC";
    List<CatItemDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
        Constants.APPLIED_BUSSINESS.CAT_ITEM.toString());
    try {
      list = setLanguage(list, lstLanguage, Constants.ITEM_ID, Constants.ITEM_NAME);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  @Override
  public List<CatItemDTO> getListCatItemDTO(CatItemDTO catItemDTO) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_KEDB, "get-List-Cat-Item-DTO");
    Map<String, Object> parameters = new HashMap<>();
    if (catItemDTO != null) {
      if (catItemDTO.getItemId() != null) {
        sql += " AND i.item_id = :itemId ";
        parameters.put("itemId", catItemDTO.getItemId());
      }
      if (StringUtils.isNotNullOrEmpty(catItemDTO.getItemCode())) {
        sql += " AND lower(i.item_code) = :itemCode ";
        parameters.put("itemCode", catItemDTO.getItemCode().toLowerCase());
      }
      if (StringUtils.isNotNullOrEmpty(catItemDTO.getCategoryCode())) {
        sql += " AND lower(c.category_code) = :categoryCode ";
        parameters.put("categoryCode", catItemDTO.getCategoryCode().toLowerCase());
      }
      if (catItemDTO.getStatus() != null) {
        sql += " AND i.status = :status ";
        parameters.put("status", catItemDTO.getStatus());
      }
    }
    sql += " ORDER BY c.category_code ASC";
    List<CatItemDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
        Constants.APPLIED_BUSSINESS.CAT_ITEM.toString());
    try {
      list = setLanguage(list, lstLanguage, "itemId", "itemName");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public BaseDto sqlGetListKedbDTO(KedbDTO kedbDTO) {
    BaseDto baseDto = new BaseDto();
    Double offset = kedbDTO.getOffset();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_KEDB, "get-List-Kedb-DTO");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(kedbDTO.getSoftwareVersion())) {
      sql += " AND k.SOFTWARE_VERSION = :softwareVersion";
      parameters.put("softwareVersion", kedbDTO.getSoftwareVersion());
    }
    if (StringUtils.isNotNullOrEmpty(kedbDTO.getHardwareVersion())) {
      sql += " AND k.HARDWARE_VERSION = :hardwareVersion";
      parameters.put("hardwareVersion", kedbDTO.getHardwareVersion());
    }
    if (StringUtils.isNotNullOrEmpty(kedbDTO.getCompleter())) {
      sql += " AND LOWER(k.COMPLETER) LIKE :completer ESCAPE '\\'";
      parameters.put("completer", StringUtils.convertLowerParamContains(kedbDTO.getCompleter()));
    }
    if (StringUtils.isNotNullOrEmpty(kedbDTO.getContentFile())) {
      sql += " AND k.KEDB_ID IN (SELECT kf.KEDB_ID FROM KEDB_FILES kf WHERE LOWER(kf.CONTENT) LIKE :contentFile ESCAPE '\\')";
      parameters
          .put("contentFile", StringUtils.convertLowerParamContains(kedbDTO.getContentFile()));
    }
    if (StringUtils.isNotNullOrEmpty(kedbDTO.getSearchAll())) {
      sql += " AND (LOWER(k.KEDB_CODE) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(k.KEDB_NAME) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(k.DESCRIPTION) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(k.RCA) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(k.TT_WA) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(k.PT_WA) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(k.SOLUTION) LIKE :searchAll ESCAPE '\\')";
      parameters.put("searchAll", StringUtils.convertLowerParamContains(kedbDTO.getSearchAll()));
    }
    if (StringUtils.isNotNullOrEmpty(kedbDTO.getKedbCode())) {
      sql += " AND LOWER(k.KEDB_CODE) LIKE :kedbCode ESCAPE '\\'";
      parameters.put("kedbCode", StringUtils.convertLowerParamContains(kedbDTO.getKedbCode()));
    }
    if (StringUtils.isNotNullOrEmpty(kedbDTO.getKedbName())) {
      sql += " AND LOWER(k.KEDB_NAME) LIKE :kedbName ESCAPE '\\'";
      parameters.put("kedbName", StringUtils.convertLowerParamContains(kedbDTO.getKedbName()));
    }
    if (StringUtils.isNotNullOrEmpty(kedbDTO.getVendor())) {
      sql += " AND k.VENDOR = :vendor2";
      parameters.put("vendor2", kedbDTO.getVendor());
    }
    if (kedbDTO.getParentTypeId() != null) {
      sql += " AND k.PARENT_TYPE_ID = :parentTypeId";
      parameters.put("parentTypeId", kedbDTO.getParentTypeId());
    }
    if (kedbDTO.getTypeId() != null) {
      sql += " AND k.TYPE_ID = :typeId";
      parameters.put("typeId", kedbDTO.getTypeId());
    }
    if (kedbDTO.getSubCategoryId() != null) {
      sql += " AND k.SUB_CATEGORY_ID = :subCategoryId";
      parameters.put("subCategoryId", kedbDTO.getSubCategoryId());
    }
    if (kedbDTO.getListKedbState() != null && !kedbDTO.getListKedbState().isEmpty()) {
      sql += " AND k.KEDB_STATE IN (:listKedbState)";
      parameters.put("listKedbState", kedbDTO.getListKedbState());
    }
    if (StringUtils.isNotNullOrEmpty(kedbDTO.getCreateUserName())) {
      sql += " AND LOWER(k.CREATE_USER_NAME) LIKE :createUserName ESCAPE '\\'";
      parameters.put("createUserName",
          StringUtils.convertLowerParamContains(kedbDTO.getCreateUserName()));
    }
    if (StringUtils.isNotNullOrEmpty(kedbDTO.getFromDate())) {
      sql += " AND k.CREATED_TIME >= TO_DATE(:fromDate,'dd/MM/yyyy HH24:mi:ss') - :offset/24";
      parameters.put("fromDate", kedbDTO.getFromDate());
    }
    if (StringUtils.isNotNullOrEmpty(kedbDTO.getToDate())) {
      sql += " AND k.CREATED_TIME <= TO_DATE(:toDate,'dd/MM/yyyy HH24:mi:ss') - :offset/24";
      parameters.put("toDate", kedbDTO.getToDate());
    }
    parameters.put("offset", offset);
    sql += " ORDER BY k.CREATED_TIME DESC";
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  public BaseDto sqlGetListKedbSearch(String keyword, String parentTypeId, String typeId,
      String subCategoryId,
      String vendor, String softwareVersion, String hardwareVersion, String kedbState,
      String fromDate, String toDate,
      String creater, String completer) {
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_KEDB, "get-List-Kedb-Search");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("kedbCode", StringUtils.convertLowerParamContains(keyword));
    parameters.put("kedbName", StringUtils.convertLowerParamContains(keyword));
    parameters.put("description", StringUtils.convertLowerParamContains(keyword));
    parameters.put("ttWa", StringUtils.convertLowerParamContains(keyword));
    parameters.put("rca", StringUtils.convertLowerParamContains(keyword));
    parameters.put("ptWa", StringUtils.convertLowerParamContains(keyword));
    parameters.put("solution", StringUtils.convertLowerParamContains(keyword));
    if (parentTypeId != null && !"".equals(parentTypeId)) {
      sql += " and parentTypeId = :parentTypeId ";
      parameters.put("parentTypeId", Long.valueOf(parentTypeId));
    }
    if (typeId != null && !"".equals(typeId)) {
      sql += " and typeId = :typeId";
      parameters.put("typeId", Long.valueOf(typeId));
    }
    if (subCategoryId != null && !"".equals(subCategoryId)) {
      sql += " and subCategoryId = :subCategoryId ";
      parameters.put("subCategoryId", Long.valueOf(subCategoryId));
    }
    if (vendor != null && !"".equals(vendor)) {
      sql += " and vendor = :vendor ";
      parameters.put("vendor", vendor);
    }
    if (softwareVersion != null && !"".equals(softwareVersion)) {
      sql += " and softwareVersion = :softwareVersion ";
      parameters.put("softwareVersion", softwareVersion);
    }
    if (hardwareVersion != null && !"".equals(hardwareVersion)) {
      sql += " and hardwareVersion = :hardwareVersion ";
      parameters.put("hardwareVersion", hardwareVersion);
    }
    if (kedbState != null && !"".equals(kedbState)) {
      sql += " and kedbState in (";
      String strParam = "";
      String[] stState = kedbState.split(",");
      for (int i = 0; i < stState.length; i++) {
        strParam += ":kedbState" + i + ",";
        parameters.put("kedbState" + i, Long.valueOf(stState[i]));
      }
      sql += strParam.substring(0, strParam.length() - 1);
      sql += ")";
    }
    if (fromDate != null && !"".equals(fromDate)) {
      sql += " and createdTime >= to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss') - :offset/24 ";
      parameters.put("fromDate", fromDate);
    }
    if (toDate != null && !"".equals(toDate)) {
      sql += " and createdTime <= to_date(:toDate,'dd/MM/yyyy HH24:mi:ss') - :offset/24 ";
      parameters.put("toDate", toDate);
    }
    if (creater != null && !"".equals(creater)) {
      sql += " and lower(CREATE_USER_NAME) like :creater escape '\\' ";
      parameters.put("creater", StringUtils.convertLowerParamContains(creater));
    }
    if (completer != null && !"".equals(completer)) {
      sql += " and lower(COMPLETER) like :completer escape '\\' ";
      parameters.put("completer", StringUtils.convertLowerParamContains(completer));
    }
    sql += " order by createdTime desc";
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public List<KedbFilesEntity> getListKedbFilesByKedbId(Long kedbId) {
    return findByMultilParam(KedbFilesEntity.class, "kedbId", kedbId);
  }

  @Override
  public KedbFilesEntity findKedbFilesById(Long kedbFileId) {
    return getEntityManager().find(KedbFilesEntity.class, kedbFileId);
  }

  @Override
  public ResultInSideDto insertKedbFiles(KedbFilesDTO kedbFilesDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    KedbFilesEntity kedbFilesEntity = getEntityManager().merge(kedbFilesDTO.toEntity());
    resultInSideDto.setId(kedbFilesEntity.getKedbFileId());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteKedbFiles(KedbFilesEntity kedbFilesEntity) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().remove(kedbFilesEntity);
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public List<com.viettel.gnoc.ws.dto.KedbDTO> synchKedbByCreateTime2(String fromDate,
      String toDate) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_KEDB, "get-List-Kedb-DTO-Webservice");
    Map<String, Object> parameters = new HashMap<>();
    if (fromDate != null && !"".equals(fromDate)) {
      sql += " and ke.CREATED_TIME >= to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss')";
      parameters.put("fromDate", fromDate);
    }
    if (toDate != null && !"".equals(toDate)) {
      sql += " and ke.CREATED_TIME <= to_date(:toDate,'dd/MM/yyyy HH24:mi:ss')";
      parameters.put("toDate", toDate);
    }
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(com.viettel.gnoc.ws.dto.KedbDTO.class));
  }
}
