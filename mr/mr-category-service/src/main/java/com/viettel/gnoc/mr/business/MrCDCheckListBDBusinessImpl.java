package com.viettel.gnoc.mr.business;


import static com.viettel.gnoc.commons.repository.BaseRepository.getSqlLanguageExchange;
import static com.viettel.gnoc.commons.repository.BaseRepository.setLanguage;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCDCheckListBDDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
import com.viettel.gnoc.mr.repository.MrCDCheckListBDRepository;
import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class MrCDCheckListBDBusinessImpl implements MrCDCheckListBDBusiness {

  @Autowired
  MrCDCheckListBDRepository mrCDCheckListBDRepository;

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  protected CatLocationBusiness catLocationBusiness;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Autowired
  MrDeviceBusiness mrDeviceBusiness;

  @Autowired
  CatItemRepository catItemRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;
  @Autowired
  CatItemBusiness catItemBusiness;
  private final static String CR_PROCESS_MANAGER_EXPORT = "MR_CD_CHECK_LIST_BD";
  Map<String, CatItemDTO> mapArray = new HashMap<>();

  @Override
  public Datatable getListMrCDCheckListBDPage(MrCDCheckListBDDTO mrCDCheckListBDDTO) {
    log.debug("Request to getListMrCDCheckListBDPage : {}", mrCDCheckListBDDTO);
    Datatable datatable = mrCDCheckListBDRepository.getListMrCDCheckListBDPage(mrCDCheckListBDDTO);
    try {
      List<MrCDCheckListBDDTO> lstReturn = null;
      if (!datatable.getData().isEmpty()) {
        lstReturn = (List<MrCDCheckListBDDTO>) datatable.getData();
      }

      String locale = I18n.getLocale();
      Map<String, Object> map = getSqlLanguageExchange(LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
          APPLIED_BUSSINESS.CAT_ITEM.toString(), locale);
      String sqlLanguage = (String) map.get("sql");
      Map mapParam = (Map) map.get("mapParam");
      Map mapType = (Map) map.get("mapType");
      List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
          .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
      lstReturn = setLanguage(lstReturn, lstLanguage, "checkListId", "arrayName");
      datatable.setData(lstReturn);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return datatable;
  }

  @Override
  public ResultInSideDto insert(MrCDCheckListBDDTO mrCDCheckListBDDTO) {
    log.debug("Request to insert : {}", mrCDCheckListBDDTO);
    ResultInSideDto result = new ResultInSideDto();
    if (checkDuplicate(mrCDCheckListBDDTO)) {
      result.setKey(RESULT.DUPLICATE);
      result.setMessage(I18n.getValidation("MrCDWorkItemDTO.null.unique"));
      return result;
    }

    result = mrCDCheckListBDRepository.insertOrUpdate(mrCDCheckListBDDTO);

    if (result != null && "SUCCESS".equals(result.getKey())) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Insert", "Insert MrCDCheckListBD: ",
          result.getId(), null));
    }
    return result;
  }


  public boolean checkDuplicate(MrCDCheckListBDDTO mrCDCheckListBDDTO) {
    List<MrCDCheckListBDDTO> list = mrCDCheckListBDRepository
        .getListMrCDCheckListBDDTO(mrCDCheckListBDDTO, true);
    if (list == null || list.isEmpty()) {
      return false;
    }
    return true;
  }

  @Override
  public ResultInSideDto update(MrCDCheckListBDDTO mrCDCheckListBDDTO) {
    log.debug("Request to update : {}", mrCDCheckListBDDTO);
    ResultInSideDto result = new ResultInSideDto();
    MrCDCheckListBDDTO oldDTO = getDetail(mrCDCheckListBDDTO.getCheckListId());

    if (!StringUtils.isStringNullOrEmpty(mrCDCheckListBDDTO.getDeviceType())) {
      if (!oldDTO.getMarketCode().equalsIgnoreCase(mrCDCheckListBDDTO.getMarketCode())
          || !oldDTO.getArrayCode().equalsIgnoreCase(mrCDCheckListBDDTO.getArrayCode())
          || !oldDTO.getDeviceType().equalsIgnoreCase(mrCDCheckListBDDTO.getDeviceType())
          || !oldDTO.getCycle().equalsIgnoreCase(mrCDCheckListBDDTO.getCycle())
          || !oldDTO.getContent().equalsIgnoreCase(mrCDCheckListBDDTO.getContent())) {
        if (checkDuplicate(mrCDCheckListBDDTO)) {
          result.setKey(RESULT.DUPLICATE);
          result.setMessage(I18n.getValidation("MrCDWorkItemDTO.null.unique"));
          return result;
        }
      }
    } else {
      if (!oldDTO.getMarketCode().equalsIgnoreCase(mrCDCheckListBDDTO.getMarketCode())
          || !oldDTO.getArrayCode().equalsIgnoreCase(mrCDCheckListBDDTO.getArrayCode())
          || !oldDTO.getCycle().equalsIgnoreCase(mrCDCheckListBDDTO.getCycle())
          || !oldDTO.getContent().equalsIgnoreCase(mrCDCheckListBDDTO.getContent())) {
        if (checkDuplicate(mrCDCheckListBDDTO)) {
          result.setKey(RESULT.DUPLICATE);
          result.setMessage(I18n.getValidation("MrCDWorkItemDTO.null.unique"));
          return result;
        }
      }
    }

    result = mrCDCheckListBDRepository.insertOrUpdate(mrCDCheckListBDDTO);

    if (result != null && "SUCCESS".equals(result.getKey())) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Update", "Update MrCDCheckListBD: ",
          result.getId(), null));
    }
    return result;
  }

  @Override
  public ResultInSideDto delete(Long checkListId) {
    log.debug("Request to delete : {}", checkListId);
    return mrCDCheckListBDRepository.delete(checkListId);
  }

  @Override
  public MrCDCheckListBDDTO getDetail(Long checkListId) {
    log.debug("Request to getDetail : {}", checkListId);
    return mrCDCheckListBDRepository.getDetail(checkListId);
  }

  @Override
  public List<CatItemDTO> getComboboxArray() {
    log.debug("Request to getComboboxArray : {}");
    return mrCDCheckListBDRepository.getComboboxArray();
  }

  @Override
  public List<MrDeviceCDDTO> getDeviceTypeCbb(String arrayCode) {
    log.debug("Request to getDeviceTypeCbb : {}", arrayCode);
    return mrCDCheckListBDRepository.getDeviceTypeCbb(arrayCode);
  }

  @Override
  public List<CatItemDTO> getComboboxActivities(Long itemId) {
    log.debug("Request to getComboboxActivities : {}", itemId);
    return mrCDCheckListBDRepository.getComboboxActivities(itemId);
  }

  @Override
  public ResultInSideDto importMrCDCheckListBD(MultipartFile multipartFile) throws Exception {
    setMapArray();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDto;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          return resultInSideDto;
        }
        File fileImport = new File(filePath);

        List<Object[]> headerList;
        headerList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            7,
            0,
            8,
            2
        );

        if (headerList.size() == 0 || !validFileFormat(headerList)) {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("common.import.errorTemplate"));
          return resultInSideDto;
        }
        //Lấy dữ liệu import
        List<Object[]> lstDataAll = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            8,
            0,
            8,
            1000
        );

        if (lstDataAll.size() <= 0) {
          resultInSideDto.setKey(RESULT.NODATA);
          return resultInSideDto;
        }

        if (lstDataAll.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          return resultInSideDto;
        }

        if (!lstDataAll.isEmpty()) {
          resultInSideDto = validateDataImport(lstDataAll);
        }

      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(e.getMessage());
    }
    return resultInSideDto;
  }

  public boolean validFileFormat(List<Object[]> headerList) {
    Object[] objects = headerList.get(0);
    if (objects == null) {
      return false;
    }
    int count = 0;

    for (Object data : objects) {
      if (data != null) {
        count++;
      }
    }
    if (count != 8) {
      return false;
    }

    if (objects[0] == null) {
      return false;
    }

    if (!(I18n.getLanguage("MrCDCheckListBD.marketName") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrCDCheckListBD.title.Array") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrCDCheckListBD.title.DeviceType"))
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrCDCheckListBD.cycle") + "*")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrCDCheckListBD.purPose"))
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrCDCheckListBD.content") + "*")
        .equalsIgnoreCase(objects[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrCDCheckListBD.goal"))
        .equalsIgnoreCase(objects[7].toString().trim())) {
      return false;
    }
    return true;
  }

  public ResultInSideDto validateDataImport(List<Object[]> lstData) throws Exception {
    for (int i = lstData.size() - 1; i >= 0; i--) {
      if (lstData.get(i)[0] == null && lstData.get(i)[1] == null && lstData.get(i)[2] == null
          && lstData.get(i)[3] == null && lstData.get(i)[4] == null && lstData.get(i)[5] == null
          && lstData.get(i)[6] == null && lstData.get(i)[7] == null && lstData.get(i)[8] == null
          && lstData.get(i)[9] == null && lstData.get(i)[10] == null
          && lstData.get(i)[11] == null) {
        lstData.remove(i);
      }
    }
    int rowIndex = 1;
    boolean allTrue = true;
    List<MrCDCheckListBDDTO> lstError = new ArrayList<>();
    List<MrCDCheckListBDDTO> lstAddOrUpdate = new ArrayList<>();
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    Map<String, String> mapDuplicate = new HashMap<>();

    for (Object[] obj : lstData) {
      MrCDCheckListBDDTO dto = new MrCDCheckListBDDTO();

      if (obj[1] != null) {
        dto.setMarketName(obj[1].toString().trim());
      } else {
        dto.setMarketName("");
      }
      if (obj[2] != null) {
        dto.setArrayCode(obj[2].toString().trim());
      } else {
        dto.setArrayCode("");
      }
      if (obj[3] != null) {
        dto.setDeviceType(obj[3].toString().trim());
      } else {
        dto.setDeviceType("");
      }
      if (obj[4] != null) {
        dto.setCycle(obj[4].toString());
      } else {
        dto.setCycle("");
      }
      if (obj[5] != null) {
        dto.setPurPose(obj[5].toString().trim());
      } else {
        dto.setPurPose("");
      }
      if (obj[6] != null) {
        dto.setContent(obj[6].toString().trim());
      } else {
        dto.setContent("");
      }
      if (obj[7] != null) {
        dto.setGoal(obj[7].toString().trim());
      } else {
        dto.setGoal("");
      }
      if (obj[8] != null) {
        dto.setAction(obj[8].toString().trim());
      } else {
        dto.setAction("");
      }

      if (validateDataChecklist(dto)) {
        String keyCheckDup =
            dto.getMarketCode().trim() + dto.getArrayCode().trim() + dto.getCycle().trim() + String
                .valueOf(dto.getDeviceType()).trim() + dto.getContent().trim();
        if (mapDuplicate.get(keyCheckDup) == null) {
          mapDuplicate
              .put(keyCheckDup, String.valueOf(rowIndex));
          lstAddOrUpdate.add(dto);
        } else {
          dto.setResultImport(
              I18n.getValidation("import.unique.file").replace("0", mapDuplicate.get(keyCheckDup)));
          lstAddOrUpdate.add(dto);
        }
      }
      lstError.add(dto);
      rowIndex++;
    }

    boolean check = false;
    boolean checkSheetTwo = false;
    for (MrCDCheckListBDDTO item : lstError) {
      if (!StringUtils.isStringNullOrEmpty(item.getResultImport())) {
        check = true;
        break;
      }
    }

    UserToken userToken = ticketProvider.getUserToken();
    if (check) {
      for (MrCDCheckListBDDTO item : lstError) {
        if (StringUtils.isStringNullOrEmpty(item.getResultImport())) {
          item.setResultImport(I18n.getLanguage("MrCDCheckListBD.record"));
        }
      }

      File expFile = exportFileEx(lstError, "2", true, lstError);
      resultInSideDTO.setKey(RESULT.ERROR);
      resultInSideDTO.setMessage(I18n.getLanguage("import.common.fail"));
      resultInSideDTO.setFile(expFile);
      resultInSideDTO.setFilePath(expFile.getPath());
    } else {
      for (MrCDCheckListBDDTO itemDTO : lstAddOrUpdate) {
        itemDTO.setCreatedDate(new Date());
        itemDTO.setCreatedUser(userToken.getUserName());
//        itemDTO.setUpdatedDate(new Date());
//        itemDTO.setUpdatedUser(userToken.getUserName());

        resultInSideDTO = mrCDCheckListBDRepository
            .insertOrUpdate(itemDTO);

        if (resultInSideDTO != null && "SUCCESS".equals(resultInSideDTO.getKey())) {
          commonStreamServiceProxy
              .insertLog(new LogChangeConfigDTO(userToken.getUserName(),
                  "Import Data", "Import Data MrCDCheckListBDDTO: " + resultInSideDTO.getId(),
                  itemDTO, null));
        }
      }
    }

    return resultInSideDTO;
  }

  private File exportFileEx(List<MrCDCheckListBDDTO> lstCrEx, String key, boolean isImport,
      List<MrCDCheckListBDDTO> lstErr) throws
      Exception {
    String fileNameOut = "";
    String subTitle = "";
    String sheetName = I18n.getLanguage("MrCDCheckListBD.export.title");
    String title = I18n.getLanguage("MrCDCheckListBD.export.title");
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    if (isImport) {
      columnSheet = new ConfigHeaderExport("marketName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
    } else {
      columnSheet = new ConfigHeaderExport("marketCode", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
    }
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("arrayCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("deviceType", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("cycle", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("purPose", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("content", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("goal", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    if (Constants.RESULT_IMPORT.equals(key)) {

    } else {
      fileNameOut = CR_PROCESS_MANAGER_EXPORT;
      subTitle = I18n
          .getLanguage("MrCDCheckListBD.export.eportDate", dateFormat.format(new Date()));
    }

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstCrEx,
        sheetName,
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.MrCDCheckListBD",
        lstHeaderSheet,
        fieldSplit,
        "",
        I18n.getLanguage("common.export.firstLeftHeader")
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
    fileExports.add(configFileExport);

    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT_NOVALUE.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExports
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  public void setMapArray() {
    List<CatItemDTO> list = catItemBusiness
        .getListItemByCategoryAndParent(Constants.MR_ITEM_NAME.MR_SUBCATEGORY, null);
    ;
    if (list != null && list.size() > 0) {
      for (CatItemDTO dto : list) {
        mapArray.put(dto.getItemValue(), dto);
      }
    }
  }

  private boolean validateDataChecklist(MrCDCheckListBDDTO dto) {

    List<ItemDataCRInside> listLocation = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);

    if (StringUtils.isStringNullOrEmpty(dto.getMarketName())) {
      dto.setResultImport(I18n.getLanguage("MrCDCheckListBD.marketName") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } else {
      boolean check = false;
      for (ItemDataCRInside inside : listLocation) {
        String temp = String.valueOf(inside.getDisplayStr());
        if (temp.equalsIgnoreCase(dto.getMarketName())) {
          check = true;
          dto.setMarketCode(inside.getValueStr().toString());
          break;
        }
      }

      if (check == false) {
        dto.setResultImport(I18n.getLanguage("MrCDCheckListBD.marketName") + " " + I18n
            .getLanguage("notify.invalid"));
        return false;
      }
    }
    if (StringUtils.isStringNullOrEmpty(dto.getArrayCode())) {
      dto.setResultImport(
          I18n.getLanguage("MrCDCheckListBD.arrayCode") + " " + I18n.getLanguage("common.notnull"));
      return false;
    } else {
      boolean checkArray = false;
      for (String array : mapArray.keySet()) {
        if (dto.getArrayCode().equalsIgnoreCase(mapArray.get(array).getItemValue())) {
          checkArray = true;
          break;
        }
      }
      if (checkArray == false) {
        dto.setResultImport(I18n.getLanguage("MrCDCheckListBD.arrayCode") + " " + I18n
            .getLanguage("notify.invalid"));
        return false;
      }
    }

    if (StringUtils.isStringNullOrEmpty(dto.getDeviceType())) {
    } else {
      boolean check = false;
      List<MrDeviceCDDTO> list = getDeviceTypeCbb(dto.getArrayCode());
      if (list != null && list.size() > 0) {
        for (MrDeviceCDDTO item : list) {
          if (item.getDeviceType().equalsIgnoreCase(dto.getDeviceType())) {
            check = true;
            break;
          }
        }
      }

      if (check == false) {
        dto.setResultImport(I18n.getLanguage("MrCDCheckListBD.title.DeviceType") + " " + I18n
            .getLanguage("notify.invalid"));
        return false;
      }
    }

    if (StringUtils.isStringNullOrEmpty(dto.getCycle())) {
      dto.setResultImport(
          I18n.getLanguage("MrCDCheckListBD.cycle") + " " + I18n.getLanguage("common.notnull"));
      return false;
    } else {
      boolean check = false;
      Map<String, String> mapCycle = new HashMap<>();
      mapCycle.put(I18n.getLanguage("cfgProcedureView.procedureTab.cycle.1M"), "1");
      mapCycle.put(I18n.getLanguage("cfgProcedureView.procedureTab.cycle.3M"), "3");
      mapCycle.put(I18n.getLanguage("cfgProcedureView.procedureTab.cycle.6M"), "6");
      mapCycle.put(I18n.getLanguage("cfgProcedureView.procedureTab.cycle.12M"), "12");

      for (Map.Entry<String, String> entry : mapCycle.entrySet()) {
        if (entry.getValue().equalsIgnoreCase(dto.getCycle())) {
          check = true;
          break;
        }
      }

      if (check == false) {
        dto.setResultImport(I18n.getLanguage("MrCDCheckListBD.cycle") + " " + I18n
            .getLanguage("notify.invalid"));
        return false;
      }
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getPurPose())) {
      if (dto.getPurPose().length() > 200) {
        dto.setResultImport(
            I18n.getLanguage("MrCDCheckListBD.purPose") + " " + I18n
                .getLanguage("mrChecklist.purPose.overlength"));
        return false;
      }
    }

    if (StringUtils.isStringNullOrEmpty(dto.getContent())) {
      dto.setResultImport(
          I18n.getLanguage("MrCDCheckListBD.content") + " " + I18n.getLanguage("common.notnull"));
      return false;
    } else {
      if (dto.getContent().length() > 1000) {
        dto.setResultImport(
            I18n.getLanguage("mrChecklist.content.overlength"));
        return false;
      }
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getGoal())) {
      if (dto.getGoal().length() > 1000) {
        dto.setResultImport(
            I18n.getLanguage("mrChecklist.target.overlength"));
        return false;
      }
    }

    if (checkDuplicate(dto)) {
      dto.setResultImport(
          I18n.getValidation("import.multiple.unique"));
      return false;
    }

    return true;
  }

  @Override
  public File exportData(MrCDCheckListBDDTO mrCDCheckListBDDTO) throws Exception {
    List<MrCDCheckListBDDTO> lstCrEx =
        mrCDCheckListBDRepository
            .getListAll(mrCDCheckListBDDTO);

    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "exportData", "ExportData MrCDCheckListBD: ",
        mrCDCheckListBDDTO, null));

    return exportFileEx_NoParam(lstCrEx, "");
  }


  private File exportFileEx_NoParam(List<MrCDCheckListBDDTO> lstCrEx, String key) throws
      Exception {
    String fileNameOut = "";
    String subTitle = "";
    String sheetName = I18n.getLanguage("MrCDCheckListBD.export.title");
    String title = I18n.getLanguage("MrCDCheckListBD.export.title");
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();

    columnSheet = new ConfigHeaderExport("marketName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("arrayName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("deviceType", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("cycle", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("purPose", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("content", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    columnSheet = new ConfigHeaderExport("goal", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    if (Constants.RESULT_IMPORT.equals(key)) {

    } else {
      fileNameOut = CR_PROCESS_MANAGER_EXPORT;
      subTitle = I18n
          .getLanguage("MrCDCheckListBD.export.eportDate", dateFormat.format(new Date()));
    }

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstCrEx,
        sheetName,
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.MrCDCheckListBD",
        lstHeaderSheet,
        fieldSplit,
        "",
        I18n.getLanguage("common.export.firstLeftHeader")
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
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExports
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  @Override
  public File getTemplate() throws Exception {
    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    //apache POI XSSF
    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workbook.getSheetAt(0);
    XSSFSheet sheetParam = workbook.createSheet("param");
    XSSFSheet sheetParam1 = workbook.createSheet("param1");
    XSSFSheet sheetParam2 = workbook.createSheet("param2");
    XSSFSheet sheetParam3 = workbook.createSheet("param3");

    //Tạo 1 mảng lưu header từng cột
    String[] header = new String[]{
        I18n.getLanguage("MrCDCheckListBD.list.stt"),
        I18n.getLanguage("MrCDCheckListBD.marketName"),
        I18n.getLanguage("MrCDCheckListBD.arrayCode"),
        I18n.getLanguage("MrCDCheckListBD.deviceType"),
        I18n.getLanguage("MrCDCheckListBD.cycle"),
        I18n.getLanguage("MrCDCheckListBD.purPose"),
        I18n.getLanguage("MrCDCheckListBD.content"),
        I18n.getLanguage("MrCDCheckListBD.goal"),
//        I18n.getLanguage("MrCDCheckListBD.action")
    };

    String[] header_LstCountry = new String[]{
        I18n.getLanguage("MrCDCheckListBD.list.stt"),
        I18n.getLanguage("MrCDCheckListBD.country"),
        I18n.getLanguage("MrCDCheckListBD.countryName")
    };

    String[] header_LstArray = new String[]{
        I18n.getLanguage("MrCDCheckListBD.list.stt"),
        I18n.getLanguage("MrCDCheckListBD.arrayCode"),
        I18n.getLanguage("MrCDCheckListBD.arrayName.title")
    };

    String[] header_LstDeviceType = new String[]{
        I18n.getLanguage("MrCDCheckListBD.arrayCode"),
        I18n.getLanguage("MrCDCheckListBD.deviceType")
    };

    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("MrCDCheckListBD.marketName"),
        I18n.getLanguage("MrCDCheckListBD.arrayCode"),
        I18n.getLanguage("MrCDCheckListBD.content"),
        I18n.getLanguage("MrCDCheckListBD.cycle")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);
    List<String> listHeaderLstCountry = Arrays.asList(header_LstCountry);
    List<String> listHeaderLstArray = Arrays.asList(header_LstArray);
    List<String> listHeaderLstDeviceType = Arrays.asList(header_LstDeviceType);
    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

//    Row firstLeftHeaderRow = sheetOne.createRow(0);
//    Cell firstLeftHeaderCell = firstLeftHeaderRow.createCell(1);
//    firstLeftHeaderRow.setHeightInPoints(16);
//    firstLeftHeaderCell.setCellValue(I18n.getLanguage("cfgProcedureView.export.firstLeftHeader"));
//    firstLeftHeaderCell.setCellStyle(style.get("indexTitle"));
//    Row secondLeftHeaderRow = sheetOne.createRow(1);
//    Cell secondLeftHeaderCell = secondLeftHeaderRow.createCell(1);
//    secondLeftHeaderRow.setHeightInPoints(16);
//    secondLeftHeaderCell.setCellValue(I18n.getLanguage("cfgProcedureView.export.secondLeftHeader"));
//    secondLeftHeaderCell.setCellStyle(style.get("indexTitle"));
//    Cell firstRightHeaderCell = firstLeftHeaderRow.createCell(4);
//    firstLeftHeaderRow.setHeightInPoints(16);
//    firstRightHeaderCell.setCellValue(I18n.getLanguage("cfgProcedureView.export.firstRightHeader"));
//    firstRightHeaderCell.setCellStyle(style.get("indexTitle"));
//    Cell secondRightHeaderCell = secondLeftHeaderRow.createCell(4);
//    secondLeftHeaderRow.setHeightInPoints(16);
//    secondRightHeaderCell
//        .setCellValue(I18n.getLanguage("cfgProcedureView.export.secondRightHeader"));
//    secondRightHeaderCell.setCellStyle(style.get("indexTitle"));

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(3, 3, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(3);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("MrCDCheckListBD.export.title"));
    titleCell.setCellStyle(style.get("title"));

    sheetParam.addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderLstCountry.size() - 1));
    Row titleRow_lstcountry = sheetParam.createRow(1);
    titleRow_lstcountry.setHeightInPoints(25);
    Cell titleCell1 = titleRow_lstcountry.createCell(0);
    titleCell1.setCellValue(I18n.getLanguage("MrCDCheckListBD.LstMarketName.export.title"));
    titleCell1.setCellStyle(style.get("title"));

    sheetParam3.addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderLstArray.size() - 1));
    Row titleRow_lstCheckListBD = sheetParam3.createRow(1);
    titleRow_lstCheckListBD.setHeightInPoints(25);
    Cell titleCell2 = titleRow_lstCheckListBD.createCell(0);
    titleCell2.setCellValue(I18n.getLanguage("MrCDCheckListBD.cycle.export.title"));
    titleCell2.setCellStyle(style.get("title"));

    sheetParam2.addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderLstDeviceType.size() - 1));
    Row titleRow_lstDeviceType = sheetParam2.createRow(1);
    titleRow_lstDeviceType.setHeightInPoints(25);
    Cell titleCell3 = titleRow_lstDeviceType.createCell(0);
    titleCell3.setCellValue(I18n.getLanguage("MrCDCheckListBD.LstDeviceType.export.title"));
    titleCell3.setCellStyle(style.get("title"));

    sheetParam1.addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderLstArray.size() - 1));
    Row titleRow_lstArray = sheetParam1.createRow(1);
    titleRow_lstDeviceType.setHeightInPoints(25);
    Cell titleCell4 = titleRow_lstArray.createCell(0);
    titleCell4.setCellValue(I18n.getLanguage("MrCDCheckListBD.array.export.title"));
    titleCell4.setCellStyle(style.get("title"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header
    Row headerRow = sheetOne.createRow(7);
    Row headerListCountry = sheetParam.createRow(3);
    Row headerListArray = sheetParam1.createRow(3);
    Row headerListDeviceType = sheetParam2.createRow(3);
    Row headerListCycle = sheetParam3.createRow(3);
    headerRow.setHeightInPoints(16);

    String[] commentHeader = new String[]{
        I18n.getLanguage("MrCDCheckListBD.comment.action")
    };

    int len = listHeader.size();
    for (int i = 0; i < len; i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
      for (String headerCheck : listHeaderStar) {
        if (headerCheck.equalsIgnoreCase(listHeader.get(i))) {
          richTextString.append("*", starFont);
        }
      }

//      if (listHeader.get(i).equalsIgnoreCase(I18n.getLanguage("MrCDCheckListBD.action"))) {
//        XSSFDrawing drawing = sheetOne.createDrawingPatriarch();
//        XSSFClientAnchor anchor = drawing
//            .createAnchor(0, 0, 0, 0, 8, 7, 9, 8);
//        XSSFComment comment = drawing.createCellComment(anchor);
//        comment.setString(commentHeader[0]);
//        headerCell.setCellComment(comment);
//      }
      headerCell.setCellValue(richTextString);
      headerCell.setCellStyle(style.get("header"));
      if (i != 0) {
        sheetOne.setColumnWidth(i, 6000);
      }
    }
    //Của sheet Country
    Cell headerCellStt = headerListCountry.createCell(0);
    Cell headerCellCountry = headerListCountry.createCell(1);
    Cell headerCellCountryName = headerListCountry.createCell(2);

    Cell headerCellStt3 = headerListArray.createCell(0);
    Cell headerCellArrayyCode = headerListArray.createCell(1);
    Cell headerCellArrayyName = headerListArray.createCell(2);

    //Của sheet ListDeviceType
    Cell headerCellStt2 = headerListDeviceType.createCell(0);
    Cell headerCellArrayy = headerListDeviceType.createCell(1);
    Cell headerCellDeviceType = headerListDeviceType.createCell(2);

    //Của sheet cycle
    Cell headerCellStt1 = headerListCycle.createCell(0);
    Cell headerCellCycleName = headerListCycle.createCell(1);
    Cell headerCellCycle = headerListCycle.createCell(2);

    XSSFRichTextString stt = new XSSFRichTextString(I18n.getLanguage("MrCDCheckListBD.list.stt"));
    XSSFRichTextString country = new XSSFRichTextString(
        I18n.getLanguage("MrCDCheckListBD.country"));
    XSSFRichTextString marketName = new XSSFRichTextString(
        I18n.getLanguage("MrCDCheckListBD.marketName"));
    XSSFRichTextString countryName = new XSSFRichTextString(
        I18n.getLanguage("MrCDCheckListBD.countryName"));
    XSSFRichTextString array = new XSSFRichTextString(
        I18n.getLanguage("MrCDCheckListBD.arrayCode"));
    XSSFRichTextString cycleName = new XSSFRichTextString(
        I18n.getLanguage("MrCDCheckListBD.cycleName.name"));
    XSSFRichTextString cycle = new XSSFRichTextString(
        I18n.getLanguage("MrCDCheckListBD.cycleName.value"));
    XSSFRichTextString deviceType = new XSSFRichTextString(
        I18n.getLanguage("MrCDCheckListBD.deviceType"));
    XSSFRichTextString arrayCode = new XSSFRichTextString(
        I18n.getLanguage("MrCDCheckListBD.arrayCode.title"));
    XSSFRichTextString arrayName = new XSSFRichTextString(
        I18n.getLanguage("MrCDCheckListBD.arrayName.title"));

    headerCellStt.setCellValue(stt);
    headerCellStt.setCellStyle(style.get("header"));
    headerCellCountry.setCellValue(country);
    headerCellCountry.setCellStyle(style.get("header"));
    headerCellCountryName.setCellValue(countryName);
    headerCellCountryName.setCellStyle(style.get("header"));

    headerCellStt1.setCellValue(stt);
    headerCellStt1.setCellStyle(style.get("header"));
    headerCellCycleName.setCellValue(cycleName);
    headerCellCycleName.setCellStyle(style.get("header"));
    headerCellCycle.setCellValue(cycle);
    headerCellCycle.setCellStyle(style.get("header"));

    headerCellStt2.setCellValue(stt);
    headerCellStt2.setCellStyle(style.get("header"));
    headerCellArrayy.setCellValue(array);
    headerCellArrayy.setCellStyle(style.get("header"));
    headerCellDeviceType.setCellValue(deviceType);
    headerCellDeviceType.setCellStyle(style.get("header"));

    headerCellStt3.setCellValue(stt);
    headerCellStt3.setCellStyle(style.get("header"));
    headerCellArrayyCode.setCellValue(arrayCode);
    headerCellArrayyCode.setCellStyle(style.get("header"));
    headerCellArrayyName.setCellValue(arrayName);
    headerCellArrayyName.setCellStyle(style.get("header"));

    sheetParam.setColumnWidth(1, 5000);
    sheetParam.setColumnWidth(2, 5000);

//    sheetParam1.setColumnWidth(1, 15000);
//    sheetParam1.setColumnWidth(2, 5000);
//    sheetParam1.setColumnWidth(3, 15000);

    sheetParam2.setColumnWidth(1, 15000);
    sheetParam2.setColumnWidth(2, 7000);

    // Set dữ liệu vào từng sheet
    //-->sheet List Quoc Gia
    int row = 4;
    int k = 1;
    List<ItemDataCRInside> countryNameList = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    for (ItemDataCRInside dto : countryNameList) {

      excelWriterUtils.createCell(sheetParam, 0, row, String.valueOf(k), style.get("cell"));
      excelWriterUtils
          .createCell(sheetParam, 1, row, String.valueOf(dto.getValueStr()), style.get("cell"));
      excelWriterUtils.createCell(sheetParam, 2, row, dto.getDisplayStr(), style.get("cell"));
      row++;
      k++;
    }
    sheetParam.autoSizeColumn(0);

    //-->sheet danh sách mảng
    mapArray.clear();
    setMapArray();
    row = 4;
    k = 1;
    for (Map.Entry<String, CatItemDTO> entry : mapArray.entrySet()) {
      excelWriterUtils.createCell(sheetParam1, 0, row, String.valueOf(k), style.get("cell"));
      excelWriterUtils.createCell(sheetParam1, 1, row, entry.getKey(), style.get("cell"));
      excelWriterUtils
          .createCell(sheetParam1, 2, row, entry.getValue().getItemName(), style.get("cell"));
      row++;
      k++;
    }

    sheetParam1.setColumnWidth(1, 13000);
    sheetParam1.setColumnWidth(2, 13000);

    // danh sach thiet bi
    row = 4;
    k = 1;

    for (Map.Entry<String, CatItemDTO> entry : mapArray.entrySet()) {
      excelWriterUtils
          .createCell(sheetParam2, 1, row, entry.getValue().getItemName(), style.get("cell"));
      List<MrDeviceCDDTO> list = getDeviceTypeCbb(entry.getKey());
      if (list != null && list.size() > 0) {
        int lenArray = list.size();
        for (int i = 0; i < lenArray; i++) {
          excelWriterUtils.createCell(sheetParam2, 0, row, String.valueOf(k), style.get("cell"));
          excelWriterUtils
              .createCell(sheetParam2, 2, row, list.get(i).getDeviceType(), style.get("cell"));
          ++row;
          ++k;
        }
      }

    }

    row = 4;
    k = 1;
    Map<String, String> mapCycle = new HashMap<>();
    mapCycle.put(I18n.getLanguage("cfgProcedureView.procedureTab.cycle.1M"), "1");
    mapCycle.put(I18n.getLanguage("cfgProcedureView.procedureTab.cycle.3M"), "3");
    mapCycle.put(I18n.getLanguage("cfgProcedureView.procedureTab.cycle.6M"), "6");
    mapCycle.put(I18n.getLanguage("cfgProcedureView.procedureTab.cycle.12M"), "12");

    for (Map.Entry<String, String> entry : mapCycle.entrySet()) {
      excelWriterUtils.createCell(sheetParam3, 0, row, String.valueOf(k), style.get("cell"));
      excelWriterUtils.createCell(sheetParam3, 1, row, entry.getKey(), style.get("cell"));
      excelWriterUtils.createCell(sheetParam3, 2, row, entry.getValue());
      row++;
      k++;
    }

    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("MrCDCheckListBD.export.title"));
    workbook.setSheetName(1, I18n.getLanguage("MrCDCheckListBD.ListMarketName"));
    workbook.setSheetName(4, I18n.getLanguage("MrCDCheckListBD.cycle.export.title"));
    workbook.setSheetName(3, I18n.getLanguage("MrCDCheckListBD.ListDeviceType"));
    workbook.setSheetName(2, I18n.getLanguage("MrCDCheckListBD.array.export.title"));

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "MR_CD_CheckListBD_TEMPLATE_EXPORT.xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

//  private File exportFileTemplate(List<MrCDCheckListBDDTO> dtoList, String key) throws Exception {
//
//    String fileNameOut;
//    String subTitle;
//    String sheetName = I18n.getLanguage("MrCDCheckListBD.export.title");
//    String title = I18n.getLanguage("MrCDCheckListBD.export.title");
//    List<ConfigFileExport> fileExportList = new ArrayList<>();
//    ConfigHeaderExport columnSheet;
//    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
//    columnSheet = new ConfigHeaderExport("marketCode", "LEFT", false, 0, 0, new String[]{}, null,
//        "STRING");
//    headerExportList.add(columnSheet);
//    columnSheet = new ConfigHeaderExport("marketName", "LEFT", false, 0, 0, new String[]{}, null,
//        "STRING");
//    headerExportList.add(columnSheet);
//    columnSheet = new ConfigHeaderExport("arrayCode", "LEFT", false, 0, 0, new String[]{}, null,
//        "STRING");
//    headerExportList.add(columnSheet);
//    columnSheet = new ConfigHeaderExport("deviceType", "LEFT", false, 0, 0, new String[]{}, null,
//        "STRING");
//    headerExportList.add(columnSheet);
//    columnSheet = new ConfigHeaderExport("cycle", "LEFT", false, 0, 0, new String[]{}, null,
//        "STRING");
//    headerExportList.add(columnSheet);
//    columnSheet = new ConfigHeaderExport("wiId", "LEFT", false, 0, 0, new String[]{}, null,
//        "STRING");
//    headerExportList.add(columnSheet);
//    columnSheet = new ConfigHeaderExport("CheckListBDs", "LEFT", false, 0, 0, new String[]{}, null,
//        "STRING");
//    headerExportList.add(columnSheet);
//
//    //kiểm tra đầu vào
//    if (Constants.RESULT_IMPORT.equals(key)) {
//      fileNameOut = SR_ROLE_RESULT_IMPORT;
//      subTitle = I18n
//          .getLanguage("srRole.export.exportDate", DateTimeUtils.convertDateOffset());
//      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
//          null,
//          "STRING");
//      headerExportList.add(columnSheet);
//    } else {
//      fileNameOut = SR_ROLE_EXPORT;
//      subTitle = I18n
//          .getLanguage("srRole.export.exportDate", DateTimeUtils.convertDateOffset());
//    }
//    Map<String, String> fieldSplit = new HashMap<>();
//    ConfigFileExport configFileExport = new ConfigFileExport(
//        dtoList
//        , sheetName
//        , title
//        , subTitle
//        , 7
//        , 3
//        , 9
//        , true
//        , "language.srRole"
//        , headerExportList
//        , fieldSplit
//        , ""
//        , I18n.getLanguage("common.export.firstLeftHeader")
//        , I18n.getLanguage("common.export.secondLeftHeader")
//        , I18n.getLanguage("common.export.firstRightHeader")
//        , I18n.getLanguage("common.export.secondRightHeader")
//    );
//    configFileExport.setLangKey(I18n.getLocale());
//    List<CellConfigExport> lstCellSheet = new ArrayList<>();
//    CellConfigExport cellSheet;
//
//    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("common.STT"),
//        "HEAD", "STRING");
//    lstCellSheet.add(cellSheet);
//    configFileExport.setLstCreatCell(lstCellSheet);
//    fileExportList.add(configFileExport);
//    //Cấu hình đường dẫn
//    String fileTemplate = "templates" + File.separator
//        + "TEMPLATE_EXPORT.xlsx";
//    String rootPath = tempFolder + File.separator;
//    File fileExport = CommonExport.exportExcel(
//        fileTemplate
//        , fileNameOut
//        , fileExportList
//        , rootPath
//        , new String[]{}
//    );
//    return fileExport;
//  }


  @Override
  public List<MrCDCheckListBDDTO> getListMrCDCheckListBDDTO(MrCDCheckListBDDTO cDCheckListBDDTO) {
    return mrCDCheckListBDRepository.getListMrCDCheckListBDDTO(cDCheckListBDDTO, false);
  }
}
