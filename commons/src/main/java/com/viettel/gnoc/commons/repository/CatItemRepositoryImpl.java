package com.viettel.gnoc.commons.repository;


import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CategoryDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.InfraCellServiceDetailDTO;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.WoFileTempDto;
import com.viettel.gnoc.commons.model.CatItemEntity;
import com.viettel.gnoc.commons.model.CategoryEntity;
import com.viettel.gnoc.commons.model.InfraDeviceEntity;
import com.viettel.gnoc.commons.model.LanguageExchangeEntity;
import com.viettel.gnoc.commons.model.WoFileTempEntity;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.MASTER_DATA;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.ws.dto.WoFileTempDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
public class CatItemRepositoryImpl extends BaseRepository implements CatItemRepository {

  @Autowired
  protected LanguageExchangeRepository languageExchangeRepository;

  @Override
  public List<CatItemDTO> getListItemByCategory(String categoryCode, String itemCode) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = "select * from COMMON_GNOC.CAT_ITEM where STATUS = 1 and PARENT_ITEM_ID is null ";
    if (categoryCode != null && !"".equals(categoryCode)) {
//      sqlQuery += " and CATEGORY_ID = (Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE=:categoryCode and EDITABLE = 1 ) ";
      sqlQuery += " and CATEGORY_ID = (Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE=:categoryCode ) ";
      parameters.put("categoryCode", categoryCode);
    }
    if (StringUtils.isNotNullOrEmpty(itemCode)) {
      sqlQuery += "and ITEM_CODE = :itemCode";
      parameters.put("itemCode", itemCode);
    }
    sqlQuery += " order by position,NLSSORT(ITEM_NAME,'NLS_SORT=vietnamese')";
    List<CatItemDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    return list;
  }

  @Override
  public List<WoCdGroupInsideDTO> getListWoCdGroupByCatitem(String categoryCode, String itemCode) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = "SELECT wcg.* FROM WFM.WO_CD_GROUP wcg JOIN COMMON_GNOC.CAT_ITEM cat ON wcg.WO_GROUP_CODE = cat.ITEM_VALUE where  cat.STATUS = 1";
    if (categoryCode != null && !"".equals(categoryCode)) {
      sqlQuery += " AND cat.CATEGORY_ID  = (SELECT CATEGORY_ID FROM COMMON_GNOC.CATEGORY WHERE CATEGORY_CODE='OTHER') ";
      parameters.put("categoryCode", categoryCode);
    }
    if (StringUtils.isNotNullOrEmpty(itemCode)) {
      sqlQuery += "and cat.ITEM_CODE = :itemCode ESCAPE '\\'";
      parameters.put("itemCode", StringUtils.convertLowerParamContains(itemCode));
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));
  }

  @Override
  public Datatable getItemMaster(String categoryCode, String system,
      String business,
      String idColName, String nameCol) {
    Datatable datatable = new Datatable();
    List<CatItemDTO> lst = getListItemByCategory(categoryCode, null);
    if (StringUtils.isStringNullOrEmpty(system)) {
      system = LANGUAGUE_EXCHANGE_SYSTEM.COMMON;
    }
    if (StringUtils.isStringNullOrEmpty(business)) {
      business = APPLIED_BUSSINESS.CAT_ITEM.toString();
    }
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        system,
        business);
    try {
      lst = setLanguage(lst, lstLanguage, idColName, nameCol);
      datatable.setData(lst);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return datatable;
  }

  @Override
  public Datatable getItemMasterHasParent(String categoryCode, String system,
      String business,
      String idColName, String nameCol) {
    Datatable datatable = new Datatable();
    List<CatItemDTO> lst = getListItemByCategoryHasParent(categoryCode, null);
    if (StringUtils.isStringNullOrEmpty(system)) {
      system = LANGUAGUE_EXCHANGE_SYSTEM.COMMON;
    }
    if (StringUtils.isStringNullOrEmpty(business)) {
      business = APPLIED_BUSSINESS.CAT_ITEM.toString();
    }
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        system,
        business);
    try {
      lst = setLanguage(lst, lstLanguage, idColName, nameCol);
      datatable.setData(lst);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return datatable;
  }

  @Override
  public List<CatItemDTO> getDataItem(String categoryCode) {
    List<CatItemDTO> lst = new ArrayList<>();
    if (categoryCode.equals(CATEGORY.OD_PRIORITY)) {
      return getItemByCategory(CATEGORY.OD_PRIORITY, MASTER_DATA.OD, MASTER_DATA.OD_PRIORITY,
          Constants.ITEM_ID, Constants.ITEM_NAME);
    } else if (categoryCode.equals(CATEGORY.OD_STATUS)) {
      return getItemByCategory(CATEGORY.OD_STATUS, MASTER_DATA.OD, MASTER_DATA.OD_STATUS,
          Constants.ITEM_VALUE, Constants.ITEM_NAME);
    } else if (categoryCode.equals(CATEGORY.OD_GROUP_TYPE)) {
      return getItemByCategory(CATEGORY.OD_GROUP_TYPE, MASTER_DATA.OD, MASTER_DATA.OD_GROUP_TYPE,
          Constants.ITEM_ID, Constants.ITEM_NAME);
    } else if (categoryCode.equals(CATEGORY.OD_TYPE)) {
      return getItemByCategory(CATEGORY.OD_TYPE, MASTER_DATA.OD, MASTER_DATA.OD_TYPE,
          Constants.ITEM_ID, Constants.ITEM_NAME);
    } else if (categoryCode.equals(CATEGORY.OD_SCHEDULE)) {
      return getItemByCategory(CATEGORY.OD_SCHEDULE, MASTER_DATA.OD, MASTER_DATA.OD_SCHEDULE,
          Constants.ITEM_ID, Constants.ITEM_NAME);
    }
    return lst;
  }

  public List<CatItemDTO> getItemByCategory(String categoryCode, String system, String type,
      String idColName, String nameCol) {
    Datatable datatable = getItemMaster(categoryCode, system, type, idColName, nameCol);
    List<CatItemDTO> list = (List<CatItemDTO>) datatable.getData();
    return list;
  }

  @Override
  public CatItemEntity getItemByItemCode(String unitCode) {
    List<CatItemEntity> dataEntity = (List<CatItemEntity>) findByMultilParam(
        CatItemEntity.class,
        "itemCode",
        unitCode);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0);
    }
    return null;
  }

  @Override
  public CatItemDTO getCatItemById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(CatItemEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public List<CatItemDTO> getListCatItemDTOLE(String lan, String appliedSystem,
      String appliedBussiness, CatItemDTO catItemDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    if (catItemDTO != null) {
      List<CatItemDTO> result = onSearchEntity(CatItemEntity.class, catItemDTO, rowStart, maxRow,
          sortType, sortFieldList);
      setLan(result);
      return result;
    }
    return null;
  }

  @Override
  public List<CatItemDTO> getListCatItemDTO(CatItemDTO catItem) {
    Map<String, Object> lstParam = new HashMap<>();
    String sql = "SELECT   i.item_id itemId,"
        + "           i.item_code itemCode,"
        + "           i.item_name itemName,"
        + "           i.item_value itemValue,"
        + "           i.category_id categoryId,"
        + "           c.category_name categoryIdName,"
        + "           c.category_code categoryCode,"
        + "           c.description description,"
        + "           i.parent_item_id parentItemId,"
        + "           i.status status"
        + "    FROM   common_gnoc.cat_item i, common_gnoc.category c"
        + "   WHERE   i.category_id = c.category_id";
    if (catItem != null) {
      if (!StringUtils.isStringNullOrEmpty(catItem.getItemId())) {
        sql += " AND i.item_id = :itemId ";
        lstParam.put("itemId", catItem.getItemId());
      }
      if (!StringUtils.isStringNullOrEmpty(catItem.getItemCode())) {
        sql += " AND lower(i.item_code) = :itemCode ";
        lstParam.put("itemCode", catItem.getItemCode().toLowerCase());
      }
      if (!StringUtils.isStringNullOrEmpty(catItem.getCategoryCode())) {
        sql += " AND lower(c.category_code) = :categoryCode ";
        lstParam.put("categoryCode", catItem.getCategoryCode().toLowerCase());
      }

      if (!StringUtils.isStringNullOrEmpty(catItem.getStatus())) {
        sql += " AND i.status = :status ";
        lstParam.put("status", catItem.getStatus());
      }
    }
    sql += " ORDER BY c.category_code ASC";

    List<CatItemDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, lstParam, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    return list;

  }

  @Override
  public Datatable getListCellService(InfraCellServiceDetailDTO dto) {
    Map<String, Object> lstParam = new HashMap<>();
    String cellCode = dto.getCellCode();
    String cellType = dto.getCellType();
    if (!StringUtils.isStringNullOrEmpty(cellCode) && !StringUtils.isStringNullOrEmpty(cellType)) {
      String sql = "";
      if ("Cell 2G".equalsIgnoreCase(cellType)) {
        sql = sql
            + "SELECT a.cell_code cellCode, a.bts_code btsCode, d.device_code deviceCode, d.device_name deviceName, to_char(d.STATUS) active "
            + "   FROM common_gnoc.infra_cell_2g_detail";
      } else if ("Cell 3G".equalsIgnoreCase(cellType)) {
        sql = sql
            + "SELECT a.cell_code cellCode, a.nodeb_code btsCode, d.device_code deviceCode, d.device_name deviceName, to_char(d.STATUS) active "
            + "   FROM common_gnoc.infra_cell_3g_detail";
      } else {
        sql = sql
            + "SELECT a.cell_code cellCode, a.LTE_CODE btsCode, d.device_code deviceCode, d.device_name deviceName, to_char(d.STATUS) active "
            + "   FROM common_gnoc.infra_cell_4g_detail";
      }
      sql = sql + " a, common_gnoc.INFRA_DEVICE d "
          + "   where a.device_id = d.device_id(+) "
          + "     and lower(a.cell_code) like  :cellCode  escape '\\' ";

      sql = sql + " order by a.cell_code";

      lstParam.put("cellCode", "%" + cellCode.trim()
          .toLowerCase().replace("\\", "\\\\")
          .replaceAll("%", "\\\\%")
          .replaceAll("_", "\\\\_")
          + "%");

      return getListDataTableBySqlQuery(sql, lstParam, dto.getPage(), dto.getPageSize(),
          InfraCellServiceDetailDTO.class,
          dto.getSortName(), dto.getSortType());
    }

    return new Datatable();
  }

  @Override
  public List<CatItemDTO> getListCatItemByItemCode(String itemCode) {
    List<CatItemEntity> listEntity = findByMultilParam(CatItemEntity.class, "itemCode", itemCode);
    List<CatItemDTO> listDTO = new ArrayList<>();
    if (listEntity != null && listEntity.size() > 0) {
      for (CatItemEntity entity : listEntity) {
        listDTO.add(entity.toDTO());
      }
    }
    return listDTO;
  }

  @Override
  public void setLan(List<CatItemDTO> result) {
    LanguageExchangeDTO languageExchangeDTO = new LanguageExchangeDTO();
    languageExchangeDTO.setLeeLocale(I18n.getLocale());
    languageExchangeDTO
        .setAppliedSystem(Long.parseLong(Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON));
    languageExchangeDTO.setAppliedBussiness(Constants.COMMON_TRANSLATE_BUSINESS.CAT_ITEM);
    List<LanguageExchangeDTO> lst = onSearchEntity(LanguageExchangeEntity.class,
        languageExchangeDTO, 0, 0, "", "");
    Map<String, String> mapLan = new HashMap<>();
    for (LanguageExchangeDTO dto : lst) {
      if (dto.getBussinessId() != null) {
        mapLan.put(dto.getBussinessId().toString(), dto.getLeeValue());
      }
    }
    for (CatItemDTO dto : result) {
      String value = mapLan.get(dto.getItemId().toString());
      if (StringUtils.isNotNullOrEmpty(value)) {
        dto.setItemName(value);
      }
    }
  }

  //tt
  //cuongtm
  @Override
  public List<CatItemDTO> getListItemByCategoryAndParent(String categoryCode, String parentId) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = "select * from COMMON_GNOC.CAT_ITEM where STATUS = 1 ";
    if (categoryCode != null && !"".equals(categoryCode)) {
      sqlQuery += "and CATEGORY_ID = (Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE=:categoryCode and STATUS = 1) ";
      parameters.put("categoryCode", categoryCode);
    }
    if (parentId != null && !"".equals(parentId)) {
      sqlQuery += " and PARENT_ITEM_ID=:parentId ";
      parameters.put("parentId", parentId);
    }
    sqlQuery += " order by position,NLSSORT(ITEM_NAME,'NLS_SORT=vietnamese')";
    List<CatItemDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    setLan(list);
    return list;
  }

  @Override
  public List<CatItemDTO> getListCatItemDTOByListCategory(List<String> lstCategoryCode) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = "select * from COMMON_GNOC.CAT_ITEM where STATUS = 1 ";
    for (int i = 0; i < lstCategoryCode.size(); i++) {
      String categoryCode = lstCategoryCode.get(i);
      if (categoryCode != null && !"".equals(categoryCode)) {
        sqlQuery += " and CATEGORY_ID = (Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE=:categoryCode and STATUS = 1) ";
        parameters.put("categoryCode", categoryCode);
      }
    }
    sqlQuery += " ORDER BY NLSSORT(ITEM_NAME,'NLS_SORT=vietnamese') ";
    List<CatItemDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    setLan(list);
    return list;
  }

  @Override
  public List<CatItemDTO> getListCatItemDTOByListCategoryCode(List<String> lstCategoryCode) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = "select * from COMMON_GNOC.CAT_ITEM where 1 = 1 ";
    for (int i = 0; i < lstCategoryCode.size(); i++) {
      String categoryCode = lstCategoryCode.get(i);
      if (categoryCode != null && !"".equals(categoryCode)) {
        sqlQuery += " and CATEGORY_ID = (Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE=:categoryCode ) ";
        parameters.put("categoryCode", categoryCode);
      }
    }
    sqlQuery += " ORDER BY NLSSORT(ITEM_NAME,'NLS_SORT=vietnamese') ";

    sqlQuery = "select * from (" + sqlQuery
        + ") ctgView left join COMMON_GNOC.CATEGORY ctg on ctgView.CATEGORY_ID = ctg.CATEGORY_ID";

    sqlQuery += " ORDER BY NLSSORT(ITEM_NAME,'NLS_SORT=vietnamese') ASC";

    List<CatItemDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    setLan(list);
    return list;
  }

  @Override
  public List<CatItemDTO> getListCatItemDTO(List<String> lstCategoryCode, String status) {
    String sql = "SELECT   i.item_id itemId,"
        + "           i.item_code itemCode,"
        + "           i.item_name itemName,"
        + "           i.item_value itemValue,"
        + "           i.category_id categoryId,"
        + "           c.category_name categoryIdName,"
        + "           c.category_code categoryCode,"
        + "           c.description description,"
        + "           i.parent_item_id parentItemId,"
        + "           i.status status"
        + "    FROM   common_gnoc.cat_item i, common_gnoc.category c"
        + "   WHERE   i.category_id = c.category_id AND i.parent_item_id is null  ";
    if (lstCategoryCode != null && !lstCategoryCode.isEmpty()) {
      sql += " AND c.category_code in (:lstCategory) ";
    }
    if (status != null && !"".equals(status)) {
      sql += " and status = 1 ";
    }
    sql += " ORDER BY   i.item_name ASC";
    Map<String, Object> params = new HashMap<>();

    if (lstCategoryCode != null && !lstCategoryCode.isEmpty()) {
      params.put("lstCategory", lstCategoryCode);
    }
    List<CatItemDTO> lstCatItemDTO = getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    setLan(lstCatItemDTO);
    return lstCatItemDTO;
  }

  @Override
  public List<CatItemDTO> getListCatItemByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortName) {
    return onSearchByConditionBean(new CatItemEntity(), lstCondition, rowStart, maxRow,
        sortType, sortName);
  }

  @Override
  public List<CfgMapNetLevelIncTypeDTO> getListCfgMapNetLevelIncTypeDTO(
      CfgMapNetLevelIncTypeDTO dto) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_MAP_NET_LEVEL_INC_TYPE,
        "get_cfg_map_net_level_inc_type");
    Map<String, Object> parameter = new HashMap<>();
    parameter.put("ID", dto.getId());
    return getNamedParameterJdbcTemplate()
        .query(sql, parameter, BeanPropertyRowMapper.newInstance(CfgMapNetLevelIncTypeDTO.class));
  }

  public String countDayOff(String resultOld, String fromDate, String toDate, String nation) {
    String result = "0";
    try {
      String sql =
          " select count(*) itemId  from COMMON_GNOC.DAY_OFF where trunc(DATE_OFF) >=TO_DATE(:fromDate, 'DD/MM/YYYY') "
              + " AND trunc(DATE_OFF) <=TO_DATE(:toDate, 'DD/MM/YYYY') ";
      if (StringUtils.isStringNullOrEmpty(nation) || "VN".equals(nation)) {
        sql = sql + " AND (NATION is null or NATION = 'VN') ";
      } else {
        sql = sql + " AND NATION =:nation ";
      }

      Map<String, Object> params = new HashMap<>();
      params.put("fromDate", fromDate);
      params.put("toDate", toDate);

      if (!StringUtils.isStringNullOrEmpty(nation) && !"VN".equals(nation)) {
        params.put("nation", nation);
      }

      List<CatItemDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
      if (lst != null && !lst.isEmpty()) {
        result = String.valueOf(lst.get(0).getItemId());
        if ("0".equals(result)) {
          return resultOld;
        }

        resultOld = (Long.parseLong(resultOld) + Long.parseLong(result)) + "";
        Date tempToDate = DateTimeUtils.convertStringToTime(toDate, "dd/MM/yyyy");
        fromDate = DateTimeUtils.convertDateToString(DateUtil.addDay(tempToDate, 1));
        toDate = DateTimeUtils
            .convertDateToString(DateUtil.addDay(tempToDate, Integer.parseInt(result)));
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return resultOld;
    }

    return countDayOff(resultOld, fromDate, toDate, nation);
  }

  @Override
  public Datatable getListCatItemSearch(CatItemDTO catItemDTO) {
    Datatable datatable = new Datatable();
    BaseDto baseDto = sqlSearch(catItemDTO);
    datatable = getListDataTableBySqlQuery(
        baseDto.getSqlQuery(), baseDto.getParameters(),
        catItemDTO.getPage(),
        catItemDTO.getPageSize(), CatItemDTO.class,
        catItemDTO.getSortName(), catItemDTO.getSortType());
    List<CatItemDTO> catItemDTOList = (List<CatItemDTO>) datatable.getData();
    String system = LANGUAGUE_EXCHANGE_SYSTEM.COMMON;
    String business = APPLIED_BUSSINESS.CAT_ITEM.toString();
    String businessCategory = APPLIED_BUSSINESS.CATEGORY.toString();
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        system,
        business);
    List<LanguageExchangeDTO> lstLanguageCategory = getLanguageExchange(
        system,
        businessCategory);
    try {
      catItemDTOList = setLanguage(catItemDTOList, lstLanguage, "itemId", "itemName");
      catItemDTOList = setLanguage(catItemDTOList, lstLanguageCategory, "categoryId",
          "categoryName");
      datatable.setData(catItemDTOList);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return datatable;
  }

  @Override
  public List<CatItemDTO> getListCatItemExport(CatItemDTO catItemDTO) {
    BaseDto baseDto = sqlSearch(catItemDTO);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(CatItemDTO.class));
  }

  @Override
  public ResultInSideDto deleteCatItem(Long itemId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CatItemEntity catItemEntity = getEntityManager().find(CatItemEntity.class, itemId);
    if (catItemEntity != null) {
      getEntityManager().remove(catItemEntity);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertCatItem(CatItemDTO catItemDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CatItemEntity catItemEntity = getEntityManager().merge(catItemDTO.toEntity());
    resultInSideDto.setId(catItemEntity.getItemId());
    return resultInSideDto;
  }

  @Override
  public List<CategoryDTO> getListCategoryByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortName) {
    return onSearchByConditionBean(new CategoryEntity(), lstCondition, rowStart, maxRow,
        sortType, sortName);
  }

  public BaseDto sqlSearch(CatItemDTO catItemDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CAT_ITEM, "getListCatItem");
    Map<String, Object> parameters = new HashMap<>();
    if (catItemDTO != null) {
      if (StringUtils.isNotNullOrEmpty(catItemDTO.getSearchAll())) {
        sqlQuery += " AND (lower(c.ITEM_CODE) LIKE :searchAll ESCAPE '\\' OR lower(c.ITEM_NAME) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(catItemDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(catItemDTO.getCategoryId())) {
        sqlQuery += " AND c.CATEGORY_ID = :categoryId ";
        parameters.put("categoryId", catItemDTO.getCategoryId());
      }
      if (StringUtils.isNotNullOrEmpty(catItemDTO.getItemCode())) {
        sqlQuery += " AND (lower(c.ITEM_CODE) LIKE :itemCode ESCAPE '\\' )";
        parameters
            .put("itemCode",
                StringUtils.convertLowerParamContains(catItemDTO.getItemCode()));
      }
      if (StringUtils.isNotNullOrEmpty(catItemDTO.getItemName())) {
        sqlQuery += " AND (lower(c.ITEM_NAME) LIKE :itemName ESCAPE '\\' )";
        parameters
            .put("itemName",
                StringUtils.convertLowerParamContains(catItemDTO.getItemName()));
      }
      if (!StringUtils.isStringNullOrEmpty(catItemDTO.getItemValue())) {
        sqlQuery += " AND (lower(c.ITEM_VALUE) LIKE :itemValue ESCAPE '\\' ) ";
        parameters
            .put("itemValue", StringUtils.convertLowerParamContains(catItemDTO.getItemValue()));
      }
      if (!StringUtils.isStringNullOrEmpty(catItemDTO.getDescription())) {
        sqlQuery += " AND (lower(c.DESCRIPTION) LIKE :description ESCAPE '\\' ) ";
        parameters
            .put("description", StringUtils.convertLowerParamContains(catItemDTO.getDescription()));
      }
      if (!StringUtils.isStringNullOrEmpty(catItemDTO.getParentItemName())) {
        sqlQuery += " AND (lower(c1.ITEM_NAME) LIKE :parentItemName ESCAPE '\\' ) ";
        parameters.put("parentItemName",
            StringUtils.convertLowerParamContains(catItemDTO.getParentItemName()));
      }
      if (!StringUtils.isStringNullOrEmpty(catItemDTO.getStatus())) {
        sqlQuery += " AND c.STATUS= :status ";
        parameters.put("status", catItemDTO.getStatus());
      }
    }
    sqlQuery += " ORDER BY NLSSORT(ca.category_name,'NLS_SORT=vietnamese') ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public List<CatItemDTO> getListCatItemTransDTO(CatItemDTO catItemDTO) {
    if (catItemDTO != null) {
      List<CatItemDTO> result = onSearchEntity(CatItemEntity.class, catItemDTO, 0, 0,
          catItemDTO.getSortType(), catItemDTO.getSortName());
      setLan(result);
      return result;
    }
    return null;
  }

  public List<CatItemDTO> getListItemByCategoryHasParent(String categoryCode, String itemCode) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = "select * from COMMON_GNOC.CAT_ITEM where STATUS = 1 ";
    if (categoryCode != null && !"".equals(categoryCode)) {
//      sqlQuery += " and CATEGORY_ID = (Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE=:categoryCode and EDITABLE = 1 ) ";
      sqlQuery += " and CATEGORY_ID = (Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE=:categoryCode ) ";
      parameters.put("categoryCode", categoryCode);
    }
    if (StringUtils.isNotNullOrEmpty(itemCode)) {
      sqlQuery += "and ITEM_CODE = :itemCode";
      parameters.put("itemCode", itemCode);
    }
    sqlQuery += " order by position,NLSSORT(ITEM_NAME,'NLS_SORT=vietnamese')";
    List<CatItemDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    return list;
  }

  public List<CatItemDTO> getListItemByCategoryOrderValueNumber(String categoryCode,
      String itemCode) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = "select * from COMMON_GNOC.CAT_ITEM where STATUS = 1 and PARENT_ITEM_ID is null ";
    if (categoryCode != null && !"".equals(categoryCode)) {
//      sqlQuery += " and CATEGORY_ID = (Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE=:categoryCode and EDITABLE = 1 ) ";
      sqlQuery += " and CATEGORY_ID = (Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE=:categoryCode ) ";
      parameters.put("categoryCode", categoryCode);
    }
    if (StringUtils.isNotNullOrEmpty(itemCode)) {
      sqlQuery += "and ITEM_CODE = :itemCode";
      parameters.put("itemCode", itemCode);
    }
    sqlQuery += " order by position, length(ITEM_VALUE), ITEM_VALUE";
    List<CatItemDTO> list = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    return list;
  }

  @Override
  public Datatable getItemMasterOrderValueNumber(String categoryCode, String system,
      String business,
      String idColName, String nameCol) {
    Datatable datatable = new Datatable();
    List<CatItemDTO> lst = getListItemByCategoryOrderValueNumber(categoryCode, null);
    if (StringUtils.isStringNullOrEmpty(system)) {
      system = LANGUAGUE_EXCHANGE_SYSTEM.COMMON;
    }
    if (StringUtils.isStringNullOrEmpty(business)) {
      business = APPLIED_BUSSINESS.CAT_ITEM.toString();
    }
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
        system,
        business);
    try {
      lst = setLanguage(lst, lstLanguage, idColName, nameCol);
      datatable.setData(lst);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return datatable;
  }

  @Override
  public List<com.viettel.gnoc.ws.dto.CatItemDTO> getListCateItem(String category, String fromDate,
      String toDate) {
    String sql;
    Map<String, Object> parameters = new HashMap<>();
    try {
      sql = " SELECT a.item_id itemId, "
          + " a.item_code itemCode, "
          + " a.item_name itemName, "
          + " a.item_value itemValue, "
          + " a.category_id categoryId, "
          + " a.position , "
          + " a.description, "
          + " a.parent_item_id parentItemId, "
          + " TO_CHAR(a.update_time,'dd/MM/yyyy HH24:MI:ss') updateTime, "
          + " b.category_code categoryCode, "
          + " b.category_name categoryName "
          + " FROM common_gnoc.cat_item a, "
          + " common_gnoc.category b "
          + " WHERE a.category_id = b.category_id ";

      if (!StringUtils.isStringNullOrEmpty(category)) {
        sql += " AND b.category_code = :category ";
        parameters.put("category", category);
      }

      if (!StringUtils.isStringNullOrEmpty(fromDate)) {
        if (DateTimeUtils.checkDateFormat(fromDate, "dd/MM/yyyy")) {
          sql += " AND a.update_time <= to_date(:fromDate,'dd/MM/yyyy HH24:mi:ss')";
          parameters.put("fromDate", fromDate);
        }
      }

      if (!StringUtils.isStringNullOrEmpty(toDate)) {
        if (DateTimeUtils.checkDateFormat(toDate, "dd/MM/yyyy")) {
          sql += " AND a.update_time >= to_date(:toDate,'dd/MM/yyyy HH24:mi:ss')";
          parameters.put("toDate", toDate);
        }
      }
      sql += " ORDER BY a.item_name ASC ";
      return getNamedParameterJdbcTemplate().query(sql, parameters,
          BeanPropertyRowMapper.newInstance(com.viettel.gnoc.ws.dto.CatItemDTO.class));
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }
    return null;
  }

  /**
   * @author tripm
   */
  @Override
  public List<CatItemDTO> getListCatItemDTOSearch(
      com.viettel.gnoc.ws.dto.CatItemDTO catItemDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    List<CatItemDTO> catItemDTOList = onSearchEntity(CatItemEntity.class,
        catItemDTO.toInsideDTO(),
        rowStart, maxRow, sortType, sortFieldList);
    if (catItemDTOList != null && !catItemDTOList.isEmpty()) {
      String system = LANGUAGUE_EXCHANGE_SYSTEM.COMMON;
      String business = APPLIED_BUSSINESS.CAT_ITEM.toString();
      String businessCategory = APPLIED_BUSSINESS.CATEGORY.toString();
      List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
          system,
          business);
      List<LanguageExchangeDTO> lstLanguageCategory = getLanguageExchange(
          system,
          businessCategory);
      try {
        catItemDTOList = setLanguage(catItemDTOList, lstLanguage, "itemId", "itemName");
        catItemDTOList = setLanguage(catItemDTOList, lstLanguageCategory, "categoryId",
            "categoryName");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return catItemDTOList;
  }

  @Override
  public List<com.viettel.gnoc.ws.dto.CatItemDTO> getListActionByCategory(String categoryCode,
      Long serviceId, Long infraType) {
    Map<String, Object> parameters = new HashMap<>();
    String sql =
        "select a.item_code itemCode, '[' || a.item_code || '] ' || a.item_name itemName, a.item_id itemId "
            + " from cat_item a, cat_item p "
            + " where a.category_id = (Select category_Id from Category where category_Code = :categoryCode) "
            + " and a.status = 1 "
            + " and p.parent_item_id is null "
            + " and a.parent_item_id is not null "
            + " and a.parent_item_id = p.item_id (+) ";
    if (!StringUtils.isLongNullOrEmpty(serviceId) && !StringUtils.isLongNullOrEmpty(infraType)) {
      sql += " and a.item_id in ( select f.action_id from wfm.material_thres f where f.service_id = :serviceId and f.infra_Type = :infraType ) ";
      parameters.put("serviceId", serviceId);
      parameters.put("infraType", infraType);
    }
    sql += " order by a.position,NLSSORT(a.item_name,'NLS_SORT=vietnamese')";

    if (StringUtils.isNotNullOrEmpty(categoryCode)) {
      parameters.put("categoryCode", categoryCode);
    }
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(com.viettel.gnoc.ws.dto.CatItemDTO.class));
  }

  @Override
  public List<WoFileTempDTO> searchWoFileTempDTO(WoFileTempDTO woFileTempDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    List<WoFileTempDto> woFileTempDTOList = onSearchEntity(WoFileTempEntity.class,
        woFileTempDTO.toInsideDTO(),
        rowStart, maxRow, sortType, sortFieldList);
    List<WoFileTempDTO> lstResult = new ArrayList<>();
    if (woFileTempDTOList != null && !woFileTempDTOList.isEmpty()) {
      for (WoFileTempDto dto : woFileTempDTOList) {
        lstResult.add(dto.toOutsideDTO());
      }
    }
    return lstResult;
  }

  @Override
  public List<InfraCellServiceDetailDTO> getListCellService(String cellCode, String cellType) {
    Map<String, Object> lstParam = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(cellCode) && !StringUtils.isStringNullOrEmpty(cellType)) {
      String sql = "";
      if ("Cell 2G".equalsIgnoreCase(cellType)) {
        sql = sql
            + "SELECT a.cell_code cellCode, a.bts_code btsCode, d.device_code deviceCode, d.device_name deviceName, to_char(d.STATUS) active "
            + "   FROM common_gnoc.infra_cell_2g_detail";
      } else if ("Cell 3G".equalsIgnoreCase(cellType)) {
        sql = sql
            + "SELECT a.cell_code cellCode, a.nodeb_code btsCode, d.device_code deviceCode, d.device_name deviceName, to_char(d.STATUS) active "
            + "   FROM common_gnoc.infra_cell_3g_detail";
      } else {
        sql = sql
            + "SELECT a.cell_code cellCode, a.LTE_CODE btsCode, d.device_code deviceCode, d.device_name deviceName, to_char(d.STATUS) active "
            + "   FROM common_gnoc.infra_cell_4g_detail";
      }
      sql = sql + " a, common_gnoc.INFRA_DEVICE d "
          + "   where a.device_id = d.device_id(+) "
          + "     and lower(a.cell_code) like  :cellCode  escape '\\' ";

      sql = sql + " order by a.cell_code";
      lstParam.put("cellCode", StringUtils.convertLowerParamContains(cellCode));
      return getNamedParameterJdbcTemplate().query(sql, lstParam,
          BeanPropertyRowMapper.newInstance(InfraCellServiceDetailDTO.class));
    }
    return null;
  }

  @Override
  public List<InfraDeviceDTO> searchNonIp(InfraDeviceDTO infraDeviceDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(InfraDeviceEntity.class,
        infraDeviceDTO,
        rowStart, maxRow, sortType, sortFieldList);
  }
}
