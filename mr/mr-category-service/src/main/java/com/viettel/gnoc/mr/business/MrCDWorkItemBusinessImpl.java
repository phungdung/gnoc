package com.viettel.gnoc.mr.business;


import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.DEVICE_TYPE_MAP_MULTI_LANG;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCDWorkItemDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
import com.viettel.gnoc.mr.repository.MrCDWorkItemRepository;
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
public class MrCDWorkItemBusinessImpl implements MrCDWorkItemBusiness {

  @Autowired
  MrCDWorkItemRepository mrCDWorkItemRepository;

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

  private final static String CR_PROCESS_MANAGER_EXPORT = "Mr_CD_WORK_ITEM";
  Map<String, CatItemDTO> mapArray = new HashMap<>();

  @Override
  public Datatable getListMrCDWorkItemPage(MrCDWorkItemDTO mrCDWorkItemDTO) {
    log.debug("Request to getListMrCDWorkItemPage : {}", mrCDWorkItemDTO);
    return mrCDWorkItemRepository.getListMrCDWorkItemPage(mrCDWorkItemDTO);
  }

  @Override
  public ResultInSideDto insert(MrCDWorkItemDTO mrCDWorkItemDTO) {
    log.debug("Request to insert : {}", mrCDWorkItemDTO);
    return mrCDWorkItemRepository.insertOrUpdate(mrCDWorkItemDTO);
  }

  @Override
  public ResultInSideDto update(MrCDWorkItemDTO mrCDWorkItemDTO) {
    log.debug("Request to update : {}", mrCDWorkItemDTO);
    return mrCDWorkItemRepository.insertOrUpdate(mrCDWorkItemDTO);
  }

  @Override
  public ResultInSideDto delete(Long wiId) {
    log.debug("Request to delete : {}", wiId);
    return mrCDWorkItemRepository.delete(wiId);
  }

  @Override
  public MrCDWorkItemDTO getDetail(Long wiId) {
    log.debug("Request to getDetail : {}", wiId);
    return mrCDWorkItemRepository.getDetail(wiId);
  }

  @Override
  public List<CatItemDTO> getComboboxArray() {
    log.debug("Request to getComboboxArray : {}");
    return mrCDWorkItemRepository.getComboboxArray();
  }

  @Override
  public List<MrDeviceCDDTO> getDeviceTypeCbb(String arrayCode) {
    log.debug("Request to getDeviceTypeCbb : {}", arrayCode);
    return mrCDWorkItemRepository.getDeviceTypeCbb(arrayCode);
  }

  @Override
  public List<CatItemDTO> getComboboxActivities(Long itemId) {
    log.debug("Request to getComboboxActivities : {}", itemId);
    return mrCDWorkItemRepository.getComboboxActivities(itemId);
  }

  @Override
  public ResultInSideDto importMrCDWorkItem(MultipartFile multipartFile) throws Exception {
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
            5,
            2
        );

        if (headerList.size() == 0 || !validFileFormat(headerList)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }
        //Lấy dữ liệu import
        List<Object[]> lstDataAll = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            8,
            0,
            1,
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

  private boolean validFileFormat(List<Object[]> headerList) {
    Object[] objects = headerList.get(0);
    if (objects == null) {
      return false;
    }
    int count = 0;

    for (Object data : objects) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 8) {
      return false;
    }

    if (objects[0] == null) {
      return false;
    }

    if (!(I18n.getLanguage("mrDeviceCD.list.marketCode") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrCDWorkItem.title.Array") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrCDWorkItem.title.DeviceType"))
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrCDWorkItem.title.Cycle") + "*")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrCDWorkItem.policy"))
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrCDWorkItem.content") + "*")
        .equalsIgnoreCase(objects[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrCDWorkItem.goal"))
        .equalsIgnoreCase(objects[7].toString().trim())) {
      return false;
    }
    return true;
  }

  private ResultInSideDto validateDataImport(List<Object[]> lstData) throws Exception {

    for (int i = lstData.size() - 1; i >= 0; i--) {
      if (lstData.get(i)[0] == null && lstData.get(i)[1] == null && lstData.get(i)[2] == null
          && lstData.get(i)[3] == null && lstData.get(i)[4] == null && lstData.get(i)[5] == null
          && lstData.get(i)[6] == null && lstData.get(i)[7] == null && lstData.get(i)[8] == null
          && lstData.get(i)[9] == null && lstData.get(i)[10] == null
          && lstData.get(i)[11] == null) {
        lstData.remove(i);
      }
    }
    int row = 8;
    boolean allTrue = true;
    List<MrCDWorkItemDTO> lstError = new ArrayList<>();
    List<MrCDWorkItemDTO> lstAddOrUpdate = new ArrayList<>();
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    for (Object[] obj : lstData) {
      MrCDWorkItemDTO dto = new MrCDWorkItemDTO();

      if (obj[1] != null) {
        dto.setMarketCode(obj[1].toString().trim());
      } else {
        dto.setMarketCode("");
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
        dto.setCycle(Long.parseLong(obj[4].toString()));
      } else {
        dto.setCycle(0l);
      }
      if (obj[5] != null) {
        dto.setWorkItems(obj[5].toString().trim());
      } else {
        dto.setWorkItems("");
      }

      if (validateDataChecklist(dto)) {
//        if ("1".equalsIgnoreCase(dto.getIsAdd())) {
//          if (mapAddObject.get(dto.getCrProcessCode()) == null) {
//            mapAddObject.put(dto.getCrProcessCode(), dto.getCrProcessName());
//            lstAddOrUpdate.add(dto);
//          } else {
//            dto.setResultImport(
//                I18n.getLanguage("CrProcessManager.err.processCode.dupplicate.inFile"));
//          }
//        } else {
//          CrProcessDTO temp = crManagerProcessRepository.checkCrProcessExist(dto);
//          if (temp != null && temp.getCrProcessId() != null) {
//            dto.setCrProcessId(temp.getCrProcessId());
//            if (mapAddObject.get(dto.getCrProcessCode()) == null) {
//              mapAddObject.put(dto.getCrProcessCode(), dto.getCrProcessName());
//
//              lstAddOrUpdate.add(dto);
//            } else {
//              dto.setResultImport(
//                  I18n.getLanguage("CrProcessManager.err.processCode.dupplicate.inFile"));
//            }
//          }
//        }
        lstAddOrUpdate.add(dto);
      }
      lstError.add(dto);
    }

    boolean check = false;
    boolean checkSheetTwo = false;
    for (MrCDWorkItemDTO item : lstError) {
      if (!StringUtils.isStringNullOrEmpty(item.getResultImport())) {
        check = true;
        break;
      }
    }

    UserToken userToken = ticketProvider.getUserToken();
    if (check) {
      for (MrCDWorkItemDTO item : lstError) {
        if (StringUtils.isStringNullOrEmpty(item.getResultImport())) {
          item.setResultImport(I18n.getLanguage("MrCDWorkItem.record"));
        }
      }

      File expFile = exportFileEx(lstError, "2", true, lstError);
      resultInSideDTO.setKey(RESULT.ERROR);
      resultInSideDTO.setFile(expFile);
    } else {
      for (MrCDWorkItemDTO itemDTO : lstAddOrUpdate) {
        resultInSideDTO = mrCDWorkItemRepository
            .insertOrUpdate(itemDTO);

        if (resultInSideDTO != null && "SUCCESS".equals(resultInSideDTO.getKey())) {
          commonStreamServiceProxy
              .insertLog(new LogChangeConfigDTO(userToken.getUserName(),
                  "Import Data", "Import Data MrCDWorkItemDTO: " + resultInSideDTO.getId(),
                  itemDTO, null));
        }
      }
    }

    return resultInSideDTO;
  }

  private File exportFileEx(List<MrCDWorkItemDTO> lstCrEx, String key, boolean isImport,
      List<MrCDWorkItemDTO> lstErr) throws
      Exception {
    String fileNameOut = "";
    String subTitle = "";
    String sheetName = I18n.getLanguage("MrCDWorkItem.export.title");
    String title = I18n.getLanguage("MrCDWorkItem.export.title");
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("marketCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
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
    columnSheet = new ConfigHeaderExport("policy", "LEFT", false, 0, 0, new String[]{}, null,
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
          .getLanguage("CrProcessManager.export.eportDate", dateFormat.format(new Date()));
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
        "language.CrProcessManager",
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

  public void setMapArray() {
    Datatable dataArray = catItemRepository.getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataArray.getData();
    if (list != null && list.size() > 0) {
      for (CatItemDTO dto : list) {
        mapArray.put(dto.getItemName(), dto);
      }
    }
  }

  private boolean validateDataChecklist(MrCDWorkItemDTO dto) {

    List<ItemDataCRInside> listLocation = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);

    if (StringUtils.isStringNullOrEmpty(dto.getMarketCode())) {
      dto.setResultImport(I18n.getLanguage("mrChecklistBTS.list.grid.marketCode") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } else {
      boolean check = false;
      for (ItemDataCRInside inside : listLocation) {
        String temp = String.valueOf(inside.getValueStr());
        if (temp.equalsIgnoreCase(dto.getMarketCode())) {
          check = true;
          break;
        }
      }

      if (check == false) {
        dto.setResultImport(I18n.getLanguage("MrCDWorkItem.title.Country") + " " + I18n
            .getLanguage("notify.invalid"));
        return false;
      }
    }
    if (StringUtils.isStringNullOrEmpty(dto.getArrayCode())) {
      dto.setResultImport(
          I18n.getLanguage("MrCDWorkItem.arrayCode") + " " + I18n.getLanguage("common.notnull"));
      return false;
    } else {
      boolean checkArray = false;
      for (String array : mapArray.keySet()) {
//        if (dto.getArrayCode().equals(mapArray.get(array))) {
//          checkArray = true;
//          break;
//        }
        //tiennv fix
        if (mapArray.containsKey(dto.getArrayCode())) {
          checkArray = true;
          break;
        }
      }
      if (checkArray == false) {
        dto.setResultImport(I18n.getLanguage("MrCDWorkItem.title.Array") + " " + I18n
            .getLanguage("notify.invalid"));
        return false;
      }
    }

    if (StringUtils.isStringNullOrEmpty(dto.getCycle())) {
      dto.setResultImport(
          I18n.getLanguage("MrCDWorkItem.cycle") + " " + I18n.getLanguage("common.notnull"));
      return false;
    } else {

    }

    if (StringUtils.isStringNullOrEmpty(dto.getArrayCode())) {
      dto.setResultImport(
          I18n.getLanguage("MrCDWorkItem.content") + " " + I18n.getLanguage("common.notnull"));
      return false;
    } else {

    }

    return true;
  }

  @Override
  public File exportData(MrCDWorkItemDTO mrCDWorkItemDTO) throws Exception {
    return null;
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
//    XSSFSheet sheetParam1 = workbook.createSheet("param1");
    XSSFSheet sheetParam2 = workbook.createSheet("param2");

    //Tạo 1 mảng lưu header từng cột
    String[] header = new String[]{
        I18n.getLanguage("MrCDWorkItem.list.stt"),
        I18n.getLanguage("MrCDWorkItem.marketCode"),
        I18n.getLanguage("MrCDWorkItem.arrayCode"),
        I18n.getLanguage("MrCDWorkItem.deviceType"),
        I18n.getLanguage("MrCDWorkItem.cycle"),
        I18n.getLanguage("MrCDWorkItem.policy"),
        I18n.getLanguage("MrCDWorkItem.content"),
        I18n.getLanguage("MrCDWorkItem.goal")
    };

    String[] header_LstCountry = new String[]{
        I18n.getLanguage("MrCDWorkItem.list.stt"),
        I18n.getLanguage("MrCDWorkItem.country"),
        I18n.getLanguage("MrCDWorkItem.countryName")
    };
    String[] header_LstWorkItem = new String[]{
        I18n.getLanguage("MrCDWorkItem.list.stt"),
        I18n.getLanguage("MrCDWorkItem.arrayCode"),
        I18n.getLanguage("MrCDWorkItem.wiId"),
        I18n.getLanguage("MrCDWorkItem.workItemsName")
    };

    String[] header_LstDeviceType = new String[]{
        I18n.getLanguage("MrCDWorkItem.list.stt"),
        I18n.getLanguage("MrCDWorkItem.arrayCode"),
        I18n.getLanguage("MrCDWorkItem.deviceType")
    };

    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("MrCDWorkItem.marketCode"),
        I18n.getLanguage("MrCDWorkItem.arrayCode"),
        I18n.getLanguage("MrCDWorkItem.content"),
        I18n.getLanguage("MrCDWorkItem.cycle")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);
    List<String> listHeaderLstCountry = Arrays.asList(header_LstCountry);
    List<String> listHeaderLstWorkItem = Arrays.asList(header_LstWorkItem);
    List<String> listHeaderLstDeviceType = Arrays.asList(header_LstDeviceType);
    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(3, 3, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(3);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("MrCDWorkItem.export.title"));
    titleCell.setCellStyle(style.get("title"));

    sheetParam.addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderLstCountry.size() - 1));
    Row titleRow_lstcountry = sheetParam.createRow(1);
    titleRow_lstcountry.setHeightInPoints(25);
    Cell titleCell1 = titleRow_lstcountry.createCell(0);
    titleCell1.setCellValue(I18n.getLanguage("MrCDWorkItem.LstMarketName.export.title"));
    titleCell1.setCellStyle(style.get("title"));

//    sheetParam1.addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderLstWorkItem.size() - 1));
//    Row titleRow_lstWorkItem = sheetParam1.createRow(1);
//    titleRow_lstWorkItem.setHeightInPoints(25);
//    Cell titleCell2 = titleRow_lstWorkItem.createCell(0);
//    titleCell2.setCellValue(I18n.getLanguage("MrCDWorkItem.LstWorkItems.export.title"));
//    titleCell2.setCellStyle(style.get("title"));

    sheetParam2.addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderLstDeviceType.size() - 1));
    Row titleRow_lstDeviceType = sheetParam2.createRow(1);
    titleRow_lstDeviceType.setHeightInPoints(25);
    Cell titleCell3 = titleRow_lstDeviceType.createCell(0);
    titleCell3.setCellValue(I18n.getLanguage("MrCDWorkItem.LstDeviceType.export.title"));
    titleCell3.setCellStyle(style.get("title"));

    Row firstLeftHeaderRow = sheetOne.createRow(0);
    Cell firstLeftHeaderCell = firstLeftHeaderRow.createCell(1);
    firstLeftHeaderRow.setHeightInPoints(16);
    firstLeftHeaderCell.setCellValue(I18n.getLanguage("cfgProcedureView.export.firstLeftHeader"));
    firstLeftHeaderCell.setCellStyle(style.get("indexTitle"));
    Row secondLeftHeaderRow = sheetOne.createRow(1);
    Cell secondLeftHeaderCell = secondLeftHeaderRow.createCell(1);
    secondLeftHeaderRow.setHeightInPoints(16);
    secondLeftHeaderCell.setCellValue(I18n.getLanguage("cfgProcedureView.export.secondLeftHeader"));
    secondLeftHeaderCell.setCellStyle(style.get("indexTitle"));
    Cell firstRightHeaderCell = firstLeftHeaderRow.createCell(4);
    firstLeftHeaderRow.setHeightInPoints(16);
    firstRightHeaderCell.setCellValue(I18n.getLanguage("cfgProcedureView.export.firstRightHeader"));
    firstRightHeaderCell.setCellStyle(style.get("indexTitle"));
    Cell secondRightHeaderCell = secondLeftHeaderRow.createCell(4);
    secondLeftHeaderRow.setHeightInPoints(16);
    secondRightHeaderCell
        .setCellValue(I18n.getLanguage("cfgProcedureView.export.secondRightHeader"));
    secondRightHeaderCell.setCellStyle(style.get("indexTitle"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header
    Row headerRow = sheetOne.createRow(7);
    Row headerListCountry = sheetParam.createRow(3);
//    Row headerListWorkItem = sheetParam1.createRow(3);
    Row headerListDeviceType = sheetParam2.createRow(3);
    headerRow.setHeightInPoints(16);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
      for (String headerCheck : listHeaderStar) {
        if (headerCheck.equalsIgnoreCase(listHeader.get(i))) {
          richTextString.append("*", starFont);
        }
      }
      headerCell.setCellValue(richTextString);
      headerCell.setCellStyle(style.get("header"));
      sheetOne.setColumnWidth(i, 6000);
    }
    //Của sheet Country
    Cell headerCellStt = headerListCountry.createCell(0);
    Cell headerCellCountry = headerListCountry.createCell(1);
    Cell headerCellCountryName = headerListCountry.createCell(2);

    //Của sheet ListWorkItem
//    Cell headerCellStt1 = headerListWorkItem.createCell(0);
//    Cell headerCellArray = headerListWorkItem.createCell(1);
//    Cell headerCellWorkItemID = headerListWorkItem.createCell(2);
//    Cell headerCellWorkItem = headerListWorkItem.createCell(3);

    //Của sheet ListDeviceType
    Cell headerCellStt2 = headerListDeviceType.createCell(0);
    Cell headerCellArrayy = headerListDeviceType.createCell(1);
    Cell headerCellDeviceType = headerListDeviceType.createCell(2);

    XSSFRichTextString stt = new XSSFRichTextString(I18n.getLanguage("MrCDWorkItem.list.stt"));
    XSSFRichTextString country = new XSSFRichTextString(I18n.getLanguage("MrCDWorkItem.country"));
    XSSFRichTextString marketName = new XSSFRichTextString(
        I18n.getLanguage("MrCDWorkItem.marketName"));
    XSSFRichTextString countryName = new XSSFRichTextString(
        I18n.getLanguage("MrCDWorkItem.countryName"));
    XSSFRichTextString array = new XSSFRichTextString(I18n.getLanguage("MrCDWorkItem.arrayCode"));
    XSSFRichTextString idWorkItem = new XSSFRichTextString(I18n.getLanguage("MrCDWorkItem.wiId"));
    XSSFRichTextString nameWorkItems = new XSSFRichTextString(
        I18n.getLanguage("MrCDWorkItem.workItemsName"));
    XSSFRichTextString deviceType = new XSSFRichTextString(
        I18n.getLanguage("MrCDWorkItem.deviceType"));

    headerCellStt.setCellValue(stt);
    headerCellStt.setCellStyle(style.get("header"));
    headerCellCountry.setCellValue(country);
    headerCellCountry.setCellStyle(style.get("header"));
    headerCellCountryName.setCellValue(countryName);
    headerCellCountryName.setCellStyle(style.get("header"));

//    headerCellStt1.setCellValue(stt);
//    headerCellStt1.setCellStyle(style.get("header"));
//    headerCellArray.setCellValue(array);
//    headerCellArray.setCellStyle(style.get("header"));
//    headerCellWorkItemID.setCellValue(idWorkItem);
//    headerCellWorkItemID.setCellStyle(style.get("header"));
//    headerCellWorkItem.setCellValue(nameWorkItems);
//    headerCellWorkItem.setCellStyle(style.get("header"));

    headerCellStt2.setCellValue(stt);
    headerCellStt2.setCellStyle(style.get("header"));
    headerCellArrayy.setCellValue(array);
    headerCellArrayy.setCellStyle(style.get("header"));
    headerCellDeviceType.setCellValue(deviceType);
    headerCellDeviceType.setCellStyle(style.get("header"));

    sheetParam.setColumnWidth(1, 5000);
    sheetParam.setColumnWidth(2, 5000);

//    sheetParam1.setColumnWidth(1, 15000);
//    sheetParam1.setColumnWidth(2, 5000);
//    sheetParam1.setColumnWidth(3, 15000);

    sheetParam2.setColumnWidth(1, 15000);
    sheetParam2.setColumnWidth(2, 7000);

    sheetOne.setColumnWidth(0, 6000);

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

    //-->sheet List Dau Viec
//    row = 4;
//    k=1;
//    List<CatItemDTO> listWorkItem = new ArrayList<>();
//    List<CatItemDTO> listArray = mrCDWorkItemRepository.getComboboxArray();
//    for (CatItemDTO dtoArray : listArray) {
//      listWorkItem = mrCDWorkItemRepository.getComboboxActivities(dtoArray.getItemId());
//      for (CatItemDTO dtoWorkItem : listWorkItem) {
//        excelWriterUtils.createCell(sheetParam1, 0, row, String.valueOf(k), style.get("cell"));
//        excelWriterUtils.createCell(sheetParam1, 1, row, dtoArray.getItemName(), style.get("cell"));
//        excelWriterUtils.createCell(sheetParam1, 2, row, String.valueOf(dtoWorkItem.getItemId()), style.get("cell"));
//        excelWriterUtils.createCell(sheetParam1, 3, row, dtoWorkItem.getItemName(), style.get("cell"));
//        row++;
//        k++;
//      }
//    }
//    sheetParam1.autoSizeColumn(0);

    // danh sach thiet bi
    row = 4;
    k = 1;
    for (Map.Entry<String, String> entry : DEVICE_TYPE_MAP_MULTI_LANG.getDeviceName().entrySet()) {
      excelWriterUtils.createCell(sheetParam2, 0, row, String.valueOf(k), style.get("cell"));
      excelWriterUtils.createCell(sheetParam2, 1, row, entry.getKey(), style.get("cell"));
      excelWriterUtils
          .createCell(sheetParam2, 2, row, I18n.getLanguage(entry.getValue()), style.get("cell"));
      row++;
      k++;
    }

    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("MrCDWorkItem.workItems"));
    workbook.setSheetName(1, I18n.getLanguage("MrCDWorkItem.ListMarketName"));
    workbook.setSheetName(2, I18n.getLanguage("MrCDWorkItem.ListDeviceType"));

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "MR_CD_WORKITEM_TEMPLATE_EXPORT.xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

//  private File exportFileTemplate(List<MrCDWorkItemDTO> dtoList, String key) throws Exception {
//
//    String fileNameOut;
//    String subTitle;
//    String sheetName = I18n.getLanguage("MrCDWorkItem.export.title");
//    String title = I18n.getLanguage("MrCDWorkItem.export.title");
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
//    columnSheet = new ConfigHeaderExport("workItems", "LEFT", false, 0, 0, new String[]{}, null,
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
  public List<MrCDWorkItemDTO> getListMrCDWorkItemDTO(MrCDWorkItemDTO cDWorkItemDTO) {
    return mrCDWorkItemRepository.getListMrCDWorkItemDTO(cDWorkItemDTO);
  }
}
