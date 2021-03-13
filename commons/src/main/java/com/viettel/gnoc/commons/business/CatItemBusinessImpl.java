package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CategoryDTO;
import com.viettel.gnoc.commons.dto.DataHistoryChange;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.InfraCellServiceDetailDTO;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
import com.viettel.gnoc.ws.dto.WoFileTempDTO;
import com.viettel.gnoc.ws.provider.WSCxfInInterceptor;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class CatItemBusinessImpl implements CatItemBusiness {

  @Autowired
  protected CatItemRepository catItemRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  protected TicketProvider ticketProvider;

  @Autowired
  protected LanguageExchangeRepository languageExchangeRepository;

  @Override
  public Datatable getItemMaster(String categoryCode, String system,
      String type,
      String idColName, String nameCol) {
    log.debug("Request to getItemMaster : {}", categoryCode);
    return catItemRepository.getItemMaster(categoryCode, system, type, idColName, nameCol);
  }

  @Override
  public List<CatItemDTO> getListCatItemDTOLE(String lan, String appliedSystem,
      String appliedBussiness, CatItemDTO catItemDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    return catItemRepository
        .getListCatItemDTOLE(lan, appliedSystem, appliedBussiness, catItemDTO, rowStart, maxRow,
            sortType, sortFieldList);
  }

  @Override
  public CatItemDTO getCatItemById(Long id) {
    return catItemRepository.getCatItemById(id);
  }

  @Override
  public List<CatItemDTO> getListItemByCategory(String categoryCode, String itemCode) {
    return catItemRepository.getListItemByCategory(categoryCode, itemCode);
  }

  @Override
  public List<CatItemDTO> getListCatItemDTO(CatItemDTO catItem) {
    return catItemRepository.getListCatItemDTO(catItem);
  }

  @Override
  public List<CatItemDTO> getListCatItemDTOByListCategory(List<String> lstCategory) {

    return catItemRepository.getListCatItemDTOByListCategory(lstCategory);
  }

  @Override
  public List<CatItemDTO> getListCatItemDTOByListCategoryCode(List<String> lstCategory) {

    return catItemRepository.getListCatItemDTOByListCategoryCode(lstCategory);
  }

  @Override
  public List<CatItemDTO> getListItemByCategoryAndParent(String categoryCode, String parentId) {
    return catItemRepository.getListItemByCategoryAndParent(categoryCode, parentId);
  }

  @Override
  public List<CatItemDTO> getListCatItemDTOByListCategoryLE(String lan, String appliedSystem,
      String appliedBussiness, List<String> lstCategoryCode) {
    List<CatItemDTO> lstResult = catItemRepository.getListCatItemDTO(lstCategoryCode, "1");
    if (lstResult != null) {
      if ("vi_VN".equalsIgnoreCase(I18n.getLocale())) {
        return lstResult;
      }
      return lstResult.stream().sorted(Comparator.comparing(CatItemDTO::getItemName)).collect(
          Collectors.toList());
    }
    return null;
  }

  @Override
  public List<CatItemDTO> getListCatItemByListParent(String lstParentItemId, String categoryId) {
    try {
      List<ConditionBean> lstCondition = new ArrayList<>();
      lstCondition.add(
          new ConditionBean("parentItemId", lstParentItemId, Constants.NAME_IN, Constants.NUMBER));
      lstCondition.add(new ConditionBean("status", "1", Constants.NAME_EQUAL, Constants.NUMBER));
      lstCondition
          .add(new ConditionBean("categoryId", categoryId, Constants.NAME_EQUAL, Constants.NUMBER));
      ConditionBeanUtil.sysToOwnListCondition(lstCondition);
      List<CatItemDTO> lstCatItemConditionDTO = catItemRepository
          .getListCatItemByCondition(lstCondition, 0, Integer.MAX_VALUE, Constants.ASC, "itemName");
      if (lstCatItemConditionDTO != null && lstCatItemConditionDTO.size() > 0) {
        return lstCatItemConditionDTO;
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

  @Override
  public List<CatItemDTO> getLstNetworkLevel(String typeId) {
    List<CatItemDTO> lst = new ArrayList<>();
    CfgMapNetLevelIncTypeDTO dto = new CfgMapNetLevelIncTypeDTO(null, Long.parseLong(typeId), null);
    List<CfgMapNetLevelIncTypeDTO> lstMap = catItemRepository.getListCfgMapNetLevelIncTypeDTO(dto);
    if (lstMap != null && lstMap.size() > 0 && lstMap.get(0).getNetworkLevelId() != null) {
      String[] tmp = lstMap.get(0).getNetworkLevelId().split(",");
      for (int i = 0; i < tmp.length; i++) {
        CatItemDTO catItemDTO = new CatItemDTO();
        catItemDTO.setItemCode(tmp[i].trim());
        catItemDTO.setItemName(tmp[i].trim());
        catItemDTO.setItemId((long) i);
        lst.add(catItemDTO);
      }
    }
    return lst;
  }

  @Override
  public void setLan(List<CatItemDTO> result) {
    catItemRepository.setLan(result);
  }

  @Override
  public String countDayOff(String fromDate, String toDate, String nation) {
    return catItemRepository.countDayOff("0", fromDate, toDate, nation);
  }

  @Override
  public Datatable getListCellService(InfraCellServiceDetailDTO dto) {
    return catItemRepository.getListCellService(dto);
  }

  @Override
  public Datatable getListCatItemSearch(CatItemDTO catItemDTO) {
    log.debug("Request to getListCatItemSearch : {}", catItemDTO);
    return catItemRepository.getListCatItemSearch(catItemDTO);
  }

  @Override
  public ResultInSideDto deleteCatItem(Long itemId) {
    log.debug("Request to deleteCatItem : {}", itemId);
    ResultInSideDto resultInSideDto;
    CatItemDTO oldHis = getCatItemById(itemId);
    resultInSideDto = catItemRepository.deleteCatItem(itemId);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(itemId.toString());
        dataHistoryChange.setType("UTILITY_CAT_ITEM");
        dataHistoryChange.setActionType("delete");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(new CatItemDTO());
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertCatItem(CatItemDTO catItemDTO) {
    log.debug("Request to deleteCatItem : {}", catItemDTO);
    ResultInSideDto resultInSideDto;
    catItemDTO.setUpdateTime(new Date());
    resultInSideDto = catItemRepository.insertCatItem(catItemDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      // Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
        dataHistoryChange.setType("UTILITY_CAT_ITEM");
        dataHistoryChange.setActionType("add");
        //Old Object History
        dataHistoryChange.setOldObject(new CatItemDTO());
        //New Object History
        dataHistoryChange.setNewObject(catItemDTO);
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateCatItem(CatItemDTO catItemDTO) {
    log.debug("Request to deleteCatItem : {}", catItemDTO);
    ResultInSideDto resultInSideDto;
    catItemDTO.setUpdateTime(new Date());
    CatItemDTO oldHis = getCatItemById(catItemDTO.getItemId());
    resultInSideDto = catItemRepository.insertCatItem(catItemDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(catItemDTO.getItemId().toString());
        dataHistoryChange.setActionType("update");
        dataHistoryChange.setType("UTILITY_CAT_ITEM");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(catItemDTO);
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        List<String> keys = getAllKeysDTO();
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  @Override
  public File exportData(CatItemDTO catItemDTO) throws Exception {
    List<CatItemDTO> catItemDTOList = catItemRepository.getListCatItemExport(catItemDTO);
    if (catItemDTOList != null && !catItemDTOList.isEmpty()) {
      for (int i = catItemDTOList.size() - 1; i > -1; i--) {
        CatItemDTO catItemExport = catItemDTOList.get(i);
        if (StringUtils.isStringNullOrEmpty(catItemExport.getEditable())) {
          catItemExport.setEditableStr(I18n.getLanguage("catItem.editable.0"));
        } else if ("1".equals(String.valueOf(catItemExport.getEditable()))) {
          catItemExport.setEditableStr(I18n.getLanguage("catItem.editable.1"));
        } else if ("0".equals(String.valueOf(catItemExport.getEditable()))) {
          catItemExport.setEditableStr(I18n.getLanguage("catItem.editable.0"));
        }
        if (StringUtils.isStringNullOrEmpty(catItemExport.getStatus())) {
          catItemExport.setStatusStr(I18n.getLanguage("catItem.status.0"));
        } else if ("1".equals(String.valueOf(catItemExport.getStatus()))) {
          catItemExport.setStatusStr(I18n.getLanguage("catItem.status.1"));
        } else if ("0".equals(String.valueOf(catItemExport.getStatus()))) {
          catItemExport.setStatusStr(I18n.getLanguage("catItem.status.0"));
        }
      }
    }
    return exportFileTemplate(catItemDTOList, "");
  }

  private File exportFileTemplate(List<CatItemDTO> dtoList, String key)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("catItem.export.title");
    String title = I18n.getLanguage("catItem.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("categoryName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("itemCode", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("itemName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("itemValue", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("description", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("parentItemName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("statusStr", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("position", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("editableStr", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    //kiểm tra đầu vào
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = "CATITEM_RESULT_IMPORT";
      subTitle = I18n
          .getLanguage("catItem.export.exportDate", DateTimeUtils.convertDateOffset());
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
    } else {
      fileNameOut = "CATITEM_EXPORT";
      subTitle = I18n
          .getLanguage("catItem.export.exportDate", DateTimeUtils.convertDateOffset());
    }
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        dtoList
        , sheetName
        , title
        , subTitle
        , 7
        , 3
        , 9
        , true
        , "language.catItem"
        , headerExportList
        , fieldSplit
        , ""
        , I18n.getLanguage("common.export.firstLeftHeader")
        , I18n.getLanguage("common.export.secondLeftHeader")
        , I18n.getLanguage("common.export.firstRightHeader")
        , I18n.getLanguage("common.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;

    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("common.STT"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configFileExport.setLstCreatCell(lstCellSheet);
    fileExportList.add(configFileExport);
    //Cấu hình đường dẫn
    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;
    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExportList
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  @Override
  public List<CatItemDTO> getListCatItemByListParentCBB(CatItemDTO catItemDTO) {
    try {
      List<CatItemDTO> catItemDTOList = new ArrayList<>();
      List<ConditionBean> lstCondition = new ArrayList<>();
      lstCondition.add(new ConditionBean("categoryId", catItemDTO.getCategoryId().toString(),
          Constants.NAME_EQUAL, Constants.NUMBER));
      ConditionBeanUtil.sysToOwnListCondition(lstCondition);
      List<CategoryDTO> lstResult = catItemRepository
          .getListCategoryByCondition(lstCondition, 0, Integer.MAX_VALUE, Constants.ASC,
              "categoryName");
      if (lstResult != null && lstResult.size() > 0) {
        List<ConditionBean> lstConditionBeans = new ArrayList<>();
        if (!StringUtils.isStringNullOrEmpty(lstResult.get(0).getParentCategoryId())) {
          lstConditionBeans.add(
              new ConditionBean("categoryId",
                  String.valueOf(lstResult.get(0).getParentCategoryId()), Constants.NAME_IN,
                  Constants.NUMBER));
        } else {
          lstConditionBeans.add(
              new ConditionBean("categoryId", catItemDTO.getCategoryId().toString(),
                  Constants.NAME_IN, Constants.NUMBER));
//          lstConditionBeans.add(new ConditionBean("itemId", itemId, Constants.NAME_IN, Constants.NUMBER));
        }
        ConditionBeanUtil.sysToOwnListCondition(lstConditionBeans);
        List<CatItemDTO> lstCatItemConditionDTO = catItemRepository
            .getListCatItemByCondition(lstConditionBeans, 0, Integer.MAX_VALUE, Constants.ASC,
                "itemName");
        if (lstCatItemConditionDTO != null && lstCatItemConditionDTO.size() > 0) {
          for (CatItemDTO dto : lstCatItemConditionDTO) {
            if (StringUtils.isStringNullOrEmpty(dto.getParentItemId())) {
              catItemDTOList.add(dto);
            }
          }
          return catItemDTOList;
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

  @Override
  public List<CatItemDTO> getListCatItemTransDTO(CatItemDTO catItemDTO) {
    return catItemRepository.getListCatItemTransDTO(catItemDTO);
  }

  @Override
  public Datatable getItemMasterHasParent(String categoryCode, String system,
      String type,
      String idColName, String nameCol) {
    log.debug("Request to getItemMaster : {}", categoryCode);
    return catItemRepository.getItemMasterHasParent(categoryCode, system, type, idColName, nameCol);
  }

  @Override
  public Datatable getItemMasterOrderValueNumber(String categoryCode, String system,
      String type,
      String idColName, String nameCol) {
    log.debug("Request to getItemMaster : {}", categoryCode);
    return catItemRepository
        .getItemMasterOrderValueNumber(categoryCode, system, type, idColName, nameCol);
  }

  //2020-10-03 hungtv add
  private List<String> getAllKeysDTO() {
    try {
      List<String> keys = new ArrayList<>();
      Field[] fields = CatItemDTO.class.getDeclaredFields();
      for (Field key : fields) {
        key.setAccessible(true);
        keys.add(key.getName());
      }
      if (keys != null && !keys.isEmpty()) {
        List<String> rmKeys = Arrays
            .asList("categoryIdName", "parentItemName", "categoryCode", "categoryName");
        for (String key : rmKeys) {
          keys.remove(key);
        }
        return keys;
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }

  //end
  @Override
  public List<com.viettel.gnoc.ws.dto.CatItemDTO> getListCateItem(String category, String fromDate,
      String toDate) {
    return catItemRepository.getListCateItem(category, fromDate, toDate);
  }

  /**
   * @author tripm
   */
  @Override
  public List<com.viettel.gnoc.ws.dto.CatItemDTO> search(
      com.viettel.gnoc.ws.dto.CatItemDTO catItemDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    List<CatItemDTO> lst = catItemRepository
        .getListCatItemDTOSearch(catItemDTO, rowStart, maxRow, sortType, sortFieldList);
    List<com.viettel.gnoc.ws.dto.CatItemDTO> lstResult = new ArrayList<>();
    if (lst != null && !lst.isEmpty()) {
      lst.forEach(item -> {
        com.viettel.gnoc.ws.dto.CatItemDTO dto = new com.viettel.gnoc.ws.dto.CatItemDTO();
        dto.setItemId(
            StringUtils.isLongNullOrEmpty(item.getItemId()) ? null : item.getItemId().toString());
        dto.setUpdateTime(!StringUtils.isStringNullOrEmpty(item.getUpdateTime()) ? DateUtil
            .date2ddMMyyyyHHMMss(item.getUpdateTime()) : null);
        dto.setItemCode(item.getItemCode());
        dto.setItemName(item.getItemName());
        dto.setItemValue(item.getItemValue());
        dto.setCategoryId(
            !StringUtils.isStringNullOrEmpty(item.getCategoryId()) ? item.getCategoryId().toString()
                : null);
        dto.setPosition(
            !StringUtils.isStringNullOrEmpty(item.getPosition()) ? item.getPosition().toString()
                : null);
        dto.setDescription(item.getDescription());
        dto.setEditable(
            !StringUtils.isStringNullOrEmpty(item.getEditable()) ? item.getEditable().toString()
                : null);
        dto.setParentItemId(
            !StringUtils.isStringNullOrEmpty(item.getParentItemId()) ? item.getParentItemId()
                .toString() : null);
        dto.setStatus(
            !StringUtils.isStringNullOrEmpty(item.getStatus()) ? item.getStatus().toString()
                : null);
        lstResult.add(dto);
      });
    }
    return lstResult;
  }

  @Override
  public List<com.viettel.gnoc.ws.dto.CatItemDTO> translateListDTO(
      List<com.viettel.gnoc.ws.dto.CatItemDTO> lst,
      String locale) {
    try {
      String myLanguage = WSCxfInInterceptor.getLanguage();
      if (!StringUtils.isStringNullOrEmpty(locale)) {
        myLanguage = locale;
      }

      String mySystem = Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON;
      String myBussiness = Constants.COMMON_TRANSLATE_BUSINESS.CAT_ITEM.toString();
      String myOption = Constants.TRANSLATE_OPTION.ID;
      Map<String, String> mapCatItem = languageExchangeRepository.translateMe(
          myLanguage,
          mySystem,
          myBussiness,
          myOption);
      myBussiness = Constants.COMMON_TRANSLATE_BUSINESS.CATEGORY.toString();
      Map<String, String> mapCategoryItem = languageExchangeRepository.translateMe(
          myLanguage,
          mySystem,
          myBussiness,
          myOption);
      for (com.viettel.gnoc.ws.dto.CatItemDTO catItemDTO : lst) {
        if (mapCatItem != null && !mapCatItem.isEmpty()) {
          String name = mapCatItem.get(catItemDTO.getItemId());
          if (name != null) {
            catItemDTO.setItemName(name);
          }
        }
        if (mapCategoryItem != null && !mapCategoryItem.isEmpty()
            && catItemDTO.getCategoryId() != null) {
          String name = mapCategoryItem.get(catItemDTO.getCategoryId());
          if (catItemDTO.getCategoryIdName() != null && name != null) {
            catItemDTO.setCategoryIdName(name);
          }
        }
        if (mapCategoryItem != null && !mapCategoryItem.isEmpty()
            && catItemDTO.getParentItemId() != null) {
          String name = mapCategoryItem.get(catItemDTO.getParentItemId());
          if (catItemDTO.getParentItemName() != null && name != null) {
            catItemDTO.setParentItemName(name);
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List<com.viettel.gnoc.ws.dto.CatItemDTO> getListActionByCategory(String category,
      Long serviceId, Long infraType) {
    return catItemRepository.getListActionByCategory(category, serviceId, infraType);
  }

  @Override
  public List<WoFileTempDTO> searchWoFileTempDTO(WoFileTempDTO woFileTempDTO, int rowStart,
      int maxRow,
      String sortType, String sortFieldList) {
    return catItemRepository
        .searchWoFileTempDTO(woFileTempDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<InfraCellServiceDetailDTO> getListCellService(String cellCode, String cellType) {
    return catItemRepository.getListCellService(cellCode, cellType);
  }

  @Override
  public List<InfraDeviceDTO> searchNonIp(InfraDeviceDTO infraDeviceDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    return catItemRepository
        .searchNonIp(infraDeviceDTO, rowStart, maxRow, sortType, sortFieldList);
  }
}
