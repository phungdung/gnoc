package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.MR_ITEM_NAME;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCauseWoWasCompletedDTO;
import com.viettel.gnoc.mr.repository.MrCauseWoWasCompletedRepository;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
public class MrCauseWoWasCompletedBusinessImpl implements MrCauseWoWasCompletedBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  MrCauseWoWasCompletedRepository mrCauseWoWasCompletedRepository;

  @Autowired
  CatItemRepository catItemRepository;

  private int maxRecord = 1000;

  Map<String, CatItemDTO> mapReasonType = new HashMap<>();

  @Override
  public Datatable onSearch(MrCauseWoWasCompletedDTO dto) {
    return mrCauseWoWasCompletedRepository.onSearch(dto);
  }

  @Override
  public ResultInSideDto insertOrUpdate(MrCauseWoWasCompletedDTO dto) {
    UserToken userToken = ticketProvider.getUserToken();
    dto.setUpdatedUser(userToken.getUserName());
    dto.setUpdatedTime(new Date());

    ResultInSideDto resultInSideDto;
    if (dto.getId() != null) {
      resultInSideDto = mrCauseWoWasCompletedRepository
          .checkExisted(dto.getReasonCode(), dto.getReasonType(), String.valueOf(dto.getId()));
    } else {
      resultInSideDto = mrCauseWoWasCompletedRepository
          .checkExisted(dto.getReasonCode(), dto.getReasonType(), null);
    }
    if (!resultInSideDto.getCheck()) {
      resultInSideDto = mrCauseWoWasCompletedRepository.insertOrUpdate(dto);
    } else {
      resultInSideDto = new ResultInSideDto(null, RESULT.DUPLICATE, RESULT.DUPLICATE);
    }
    return resultInSideDto;
  }

  @Override
  public MrCauseWoWasCompletedDTO findById(Long id) {
    return mrCauseWoWasCompletedRepository.findById(id);
  }

  @Override
  public File exportData(MrCauseWoWasCompletedDTO dto) throws Exception {
    String[] header = new String[]{"reasonCode", "reasonName", "reasonTypeName",
        "waitingTime", "validateProcess", "updatedUser", "updatedTime"};
    List<MrCauseWoWasCompletedDTO> lstData = mrCauseWoWasCompletedRepository
        .onSearchExport(dto);
    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet(header);
    return exportFileEx(lstData, lstHeaderSheet, "");
  }

  private List<ConfigHeaderExport> renderHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  private File exportFileEx(List<MrCauseWoWasCompletedDTO> lstData,
      List<ConfigHeaderExport> lstHeaderSheet, String code) throws Exception {
    String title = I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.title");
    String sheetName = title;
    String fileNameOut;
    ConfigFileExport configfileExport = null;
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet = null;
    ConfigHeaderExport columnSheet1 = null;
    if (Constants.RESULT_IMPORT.equals(code)) {
      fileNameOut = "MR_CAUSE_WO_WAS_NOT_COMPLETED_IMPORT";
      columnSheet1 = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet.add(columnSheet1);
    } else {
      fileNameOut = "MR_CAUSE_WO_WAS_NOT_COMPLETED_EXPORT";
    }
    configfileExport = new ConfigFileExport(
        lstData
        , sheetName
        , title
        , null
        , 6
        , 3
        , lstHeaderSheet.size()
        , true
        , "language.mrCauseWoWasNotCompleted.export.grid"
        , lstHeaderSheet
        , fieldSplit
        , ""
        , I18n.getLanguage("common.export.firstLeftHeader")
        , I18n.getLanguage("common.export.secondLeftHeader")
        , I18n.getLanguage("common.export.firstRightHeader")
        , I18n.getLanguage("common.export.secondRightHeader")
    );
    cellSheet = new CellConfigExport(6, 0, 0, 0,
        I18n.getLanguage("common.STT"),
        "HEAD", "STRING");
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
    configfileExport.setLangKey(I18n.getLocale());
    fileExports.add(configfileExport);

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

  private void setMapReasonType() {
    Datatable datatable = catItemRepository.getItemMasterHasParent(MR_ITEM_NAME.BTS_REASON_WO_NOT_COMPLETE,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID,
        Constants.ITEM_NAME);

    List<CatItemDTO> list = (List<CatItemDTO>) datatable.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        if (!mapReasonType.containsKey(dto.getItemId())) {
          mapReasonType.put(String.valueOf(dto.getItemId()), dto);
        }
      }
    }
  }

  @Override
  public File getTemplate() throws IOException {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT_NOVALUE.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);
    XSSFSheet sheetParam2 = workBook
        .createSheet(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonType"));
    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetMain);
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonCode"),
        I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonName"),
        I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonType"),
        I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.waitingTime")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonCode"),
        I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonName"),
        I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonType"),
        I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.waitingTime")
    };
//    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int sttColumn = listHeader.indexOf(I18n.getLanguage("common.STT"));
    int reasonCodeColumn = listHeader
        .indexOf(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonCode"));
    int reasonNameColumn = listHeader
        .indexOf(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonName"));
    int reasonTypeColumn = listHeader.indexOf(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonType"));
    int waitingTimeColumn = listHeader.indexOf(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.waitingTime"));

    String firstLeftHeaderTitle = I18n.getLanguage("common.export.firstLeftHeader");
    String secondLeftHeaderTitle = I18n.getLanguage("common.export.secondLeftHeader");
    String firstRightHeaderTitle = I18n.getLanguage("common.export.firstRightHeader");
    String secondRightHeaderTitle = I18n.getLanguage("common.export.secondRightHeader");

    CellStyle cellStyleTopHeader = CommonExport.setCellStyleTopHeader(workBook);
    CellStyle cellStyleTopRightHeader = CommonExport.setCellStyleTopRightHeader(workBook);
    CellStyle cellStyleTitle = CommonExport.setCellStyleTitle(workBook);
    CellStyle cellStyleHeader = CommonExport.setCellStyleHeader(workBook);

    //Tao quoc hieu
    Row headerFirstTitle = sheetMain.createRow(0);
    Row headerSecondTitle = sheetMain.createRow(1);
    int sizeheaderTitle = 7;
    Cell firstLeftHeader = headerFirstTitle.createCell(1);
    firstLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell secondLeftHeader = headerSecondTitle.createCell(1);
    secondLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell firstRightHeader = headerFirstTitle.createCell(sizeheaderTitle - 2);
    firstRightHeader.setCellStyle(cellStyleTopRightHeader);
    Cell secondRightHeader = headerSecondTitle.createCell(sizeheaderTitle - 2);
    secondRightHeader.setCellStyle(cellStyleTopRightHeader);
    firstLeftHeader.setCellValue(firstLeftHeaderTitle);
    secondLeftHeader.setCellValue(secondLeftHeaderTitle);
    firstRightHeader.setCellValue(firstRightHeaderTitle);
    secondRightHeader.setCellValue(secondRightHeaderTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, 1, 3));
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, 1, 3));
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, sizeheaderTitle - 2,
        sizeheaderTitle + 1));
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, sizeheaderTitle - 2,
        sizeheaderTitle + 1));

    //Tao tieu de
    Row rowMainTitle = sheetMain.createRow(3);
    Cell mainCellTitle = rowMainTitle.createCell(1);
    mainCellTitle.setCellValue(I18n.getLanguage("mrCauseWoWasNotCompleted.import.title"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(3, 3, 1, 7));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao header
    Row headerRow = sheetMain.createRow(5);
    Row headerReasonType = sheetParam2.createRow(0);
    headerRow.setHeight((short) 600);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeaderStar : listHeaderStar) {
        if (checkHeaderStar.equalsIgnoreCase(listHeader.get(i))) {
          rt.append("(*)", fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(cellStyleHeader);
    }

    Cell headerCellReasonType = headerReasonType.createCell(0);
    Cell headerCellReasonTypeName = headerReasonType.createCell(1);
    XSSFRichTextString reasonTypeId = new XSSFRichTextString(
        I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonTypeId"));
    XSSFRichTextString reasonTypeName = new XSSFRichTextString(
        I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonTypeN"));
    headerCellReasonType.setCellValue(reasonTypeId);
    headerCellReasonType.setCellStyle(styles.get("header"));
    headerCellReasonTypeName.setCellValue(reasonTypeName);
    headerCellReasonTypeName.setCellStyle(styles.get("header"));
    sheetParam2.setColumnWidth(0, 4000);
    sheetParam2.setColumnWidth(1, 10000);

    // tạo set reasonType
    int row = 1;
    setMapReasonType();
    List<CatItemDTO> list = new ArrayList<>(mapReasonType.values());
    for (CatItemDTO dto : list) {
      ewu.createCell(sheetParam2, 0, row, dto.getItemId().toString(), styles.get("cell"));
      ewu.createCell(sheetParam2, 1, row++, dto.getItemName(), styles.get("cell"));
    }

    // set độ rông các cột
    sheetMain.setColumnWidth(sttColumn, 2000);
    sheetMain.setColumnWidth(reasonCodeColumn, 6000);
    sheetMain.setColumnWidth(reasonNameColumn, 6000);
    sheetMain.setColumnWidth(reasonTypeColumn, 6000);
    sheetMain.setColumnWidth(waitingTimeColumn, 6000);

//    workBook.setSheetHidden(1, true);
    workBook.setSheetName(0, I18n.getLanguage("mrCauseWoWasNotCompleted.import.title"));

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_MR_CAUSE_WO_WAS_NOT_COMPLETED" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    List<MrCauseWoWasCompletedDTO> listDto;
    List<MrCauseWoWasCompletedDTO> listImportDto;
    Map<String, String> mapImportDTO;

    String[] header = new String[]{"reasonCode", "reasonName", "reasonType",
        "waitingTime"};
    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet(header);
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

        File fileImp = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 5,
            0, 4, 1000);
        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }

        List<Object[]> lstData = CommonImport.getDataFromExcelFileNew(fileImp, 0, 6,
            0, 4, 1000);
        if (lstData.size() > maxRecord) {
          resultInSideDto.setKey("ERROR_NO_DOWNLOAD");
          resultInSideDto.setMessage(I18n.getLanguage("mrCauseWoWasNotCompleted.maxrow.import"));
          return resultInSideDto;
        }

        listDto = new ArrayList<>();
        listImportDto = new ArrayList<>();
        mapImportDTO = new HashMap<>();

        if (!lstData.isEmpty()) {
          int index = 0;
          for (Object[] obj : lstData) {
            MrCauseWoWasCompletedDTO importDTO = new MrCauseWoWasCompletedDTO();
            if (obj[1] != null) {
              importDTO.setReasonCode(obj[1].toString().trim());
            }
            if (obj[2] != null) {
              importDTO.setReasonName(obj[2].toString().trim());
            }
            if (obj[3] != null) {
              importDTO.setReasonType(obj[3].toString().trim());
            }
            if (obj[4] != null) {
              try {
                importDTO.setWaitingTime(obj[4].toString().trim());
              } catch (Exception e) {
                importDTO.setResultImport(I18n.getLanguage("common.import.validRecord"));
              }
            }
//            if (obj[4] != null) {
//              List<ItemDataCRInside> list = itemData("SYSTEM");
//              HashMap<String, Long> map = new HashMap<>();
//              for (ItemDataCRInside itemDataCRInside : list) {
//                map.put(itemDataCRInside.getDisplayStr(), itemDataCRInside.getValueStr());
//              }
//              importDTO.setSystem(map.get(obj[4].toString().trim()));
//              importDTO.setSystemName(obj[4].toString().trim());
//            }

            MrCauseWoWasCompletedDTO tempImportDTO = StringUtils.isNotNullOrEmpty(importDTO.getResultImport()) ? importDTO : validateImportInfo(importDTO, mapImportDTO);
            if (tempImportDTO.getResultImport() == null) {
              tempImportDTO
                  .setResultImport(I18n.getLanguage("common.import.validRecord"));
              listImportDto.add(tempImportDTO);
              listDto.add(tempImportDTO);
            } else {
              listImportDto.add(tempImportDTO);
              index++;
            }
            String reasonCode = importDTO.getReasonCode();
            String reasonType = importDTO.getReasonType();

            String key = reasonCode + "_" + reasonType;
            mapImportDTO.put(key, key);
          }
          if (index == 0) {
            if (!listDto.isEmpty()) {
              boolean check = false;
              for (MrCauseWoWasCompletedDTO dto : listDto) {
                resultInSideDto = insertOrUpdate(dto);
                if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                  check = true;
                  break;
                }
              }
              if (!check == true) {
                File fileExport = exportFileEx(listImportDto, lstHeaderSheet, "RESULT_IMPORT");
                resultInSideDto.setFile(fileExport);
                resultInSideDto.setFilePath(fileExport.getPath());
                resultInSideDto.setKey(RESULT.SUCCESS);
              }
            }
          } else {
            File fileExport = exportFileEx(listImportDto, lstHeaderSheet, "RESULT_IMPORT");
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
            resultInSideDto.setFilePath(fileExport.getPath());
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportFileEx(listImportDto, lstHeaderSheet, "RESULT_IMPORT");
          resultInSideDto.setFile(fileExport);
          resultInSideDto.setFilePath(fileExport.getPath());
          return resultInSideDto;
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(ex.getMessage());
    }
    return resultInSideDto;
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 5) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("common.STT"))
        .equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonCode") + "(*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonName") + "(*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonType") + "(*)")
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.waitingTime") + "(*)")
        .equalsIgnoreCase(obj[4].toString().trim())) {
      return false;
    }
    return true;
  }

  private MrCauseWoWasCompletedDTO validateImportInfo(MrCauseWoWasCompletedDTO importDTO,
      Map<String, String> mapImportDTO) {
    String resultImport = "";
    //Check null
    if (StringUtils.isStringNullOrEmpty(importDTO.getReasonCode())) {
      resultImport = resultImport.concat(
          I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonCode") + " " + I18n
              .getLanguage("common.notnull") + ";");
    } else {
      if (importDTO.getReasonCode().length() > 200) {
        resultImport = resultImport.concat(
            I18n.getValidation("mrCauseWoWasCompleted.reasonCode.max.length.invalid") + ";");
      }
      String regex = "[a-zA-Z0-9_.]+";
      if (!String.valueOf(importDTO.getReasonCode()).matches(regex)) {
        resultImport = resultImport.concat(
            I18n.getValidation("mrCauseWoWasCompleted.reasonCode.invalid") + ";");
      }
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getReasonName())) {
      resultImport = resultImport.concat(
          I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonName") + " " + I18n
              .getLanguage("common.notnull") + ";");
    } else {
      if (importDTO.getReasonName().length() > 500) {
        resultImport = resultImport.concat(
            I18n.getValidation("mrCauseWoWasCompleted.reasonName.max.length.invalid") + ";");
      }
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getReasonType())) {
      resultImport = resultImport.concat(
          I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.reasonType") + " " + I18n
              .getLanguage("common.notnull") + ";");
    } else {
      setMapReasonType();
      if (!mapReasonType.containsKey(importDTO.getReasonType())) {
        resultImport = resultImport.concat(
            I18n.getValidation("mrCauseWoWasCompleted.reasonType.invalid") + ";");
      }
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getWaitingTime())) {
      resultImport = resultImport.concat(
          I18n.getLanguage("mrCauseWoWasNotCompleted.export.grid.waitingTime") + " " + I18n
              .getLanguage("common.notnull") + ";");
    } else {
      if (importDTO.getWaitingTime().length() > 10) {
        resultImport = resultImport.concat(
            I18n.getValidation("mrCauseWoWasCompleted.waitingTime.max.length.invalid") + ";");
      }
      String regex = "[+-]?[0-9]+([.][0-9]{1,2})?";
      if (!String.valueOf(importDTO.getWaitingTime()).matches(regex)) {
        resultImport = resultImport.concat(
            I18n.getValidation("mrCauseWoWasCompleted.waitingTime.invalid") + ";");
      }
    }
    if (StringUtils.isStringNullOrEmpty(resultImport)) {
      ResultInSideDto resultInSideDto = mrCauseWoWasCompletedRepository
          .checkExisted(importDTO.getReasonCode(), importDTO.getReasonType(), null);
      if (resultInSideDto.getCheck()) {
        resultImport = resultImport.concat(
            I18n.getValidation("mrCauseWoWasCompleted.null.unique") + ";");
      }
    }
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      importDTO.setResultImport(resultImport);
      return importDTO;
    }
    String validateDuplicate = validateDuplicateImport(importDTO, mapImportDTO);
    if (StringUtils.isNotNullOrEmpty(validateDuplicate)) {
      importDTO.setResultImport(validateDuplicate);
      return importDTO;
    }

    return importDTO;
  }

  private String validateDuplicateImport(MrCauseWoWasCompletedDTO importDTO,
      Map<String, String> mapImportDTO) {
    String reasonCode = importDTO.getReasonCode();
    String reasonType = importDTO.getReasonType();

    String key = reasonCode + "_" + reasonType;
    String value = mapImportDTO.get(key);
    if (StringUtils.isNotNullOrEmpty(value)) {
      return I18n.getLanguage("mrCauseWoWasNotCompleted.err.dup-code-in-file").replaceAll("0", value);
    }
    return null;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    return mrCauseWoWasCompletedRepository.delete(id);
  }

  @Override
  public List<MrCauseWoWasCompletedDTO> getReasonWO(String reasonTypeId) {
    return mrCauseWoWasCompletedRepository.getReasonWO(reasonTypeId);
  }
}
