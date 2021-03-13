package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.UserRepositoryImpl;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrHardDevicesCheckListDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrHardDevicesCheckListRepository;
import com.viettel.gnoc.mr.repository.MrSynItSoftDevicesRepository;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
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
public class MrHardDevicesCheckListBusinessImpl implements MrHardDevicesCheckListBusiness {

  @Autowired
  MrHardDevicesCheckListRepository mrHardDevicesCheckListRepository;

  @Autowired
  protected CatLocationBusiness catLocationBusiness;

  @Autowired
  MrDeviceBusiness mrDeviceBusiness;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;

  @Autowired
  UserRepositoryImpl userRepositoryImpl;

  @Autowired
  MrSynItSoftDevicesRepository mrSynItSoftDevicesRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;
  private List<ItemDataCRInside> lstCountry;
  public final static String REGEX_NUMBER = "([0-9])+";
  private final static String MR_HARD_DEVICES_CHECKLIST_RESULT_IMPORT = "MR_HARD_DEVICES_CHECKLIST_RESULT_IMPORT";
  private final static String MR_HARD_DEVICES_CHECKLIST_EXPORT = "MR_HARD_DEVICES_CHECKLIST_EXPORT";


  @Override
  public Datatable onSearch(MrHardDevicesCheckListDTO mrCheckListDTO) {
    log.debug("Request to getListMrHardDevicesCheckListPage: {}", mrCheckListDTO);
    setMapCountryName();
    Datatable datatable = mrHardDevicesCheckListRepository.onSearch(mrCheckListDTO);
    List<MrHardDevicesCheckListDTO> listDTO = (List<MrHardDevicesCheckListDTO>) datatable.getData();
    if (listDTO != null && listDTO.size() > 0) {
      for (MrHardDevicesCheckListDTO checkListDTO : listDTO) {
        if (checkListDTO.getMarketCode() != null) {
          checkListDTO.setMarketName(mapCountryName.get(checkListDTO.getMarketCode()));
        }
      }
    }
    datatable.setData(listDTO);
    return datatable;
  }

  @Override
  public ResultInSideDto insert(MrHardDevicesCheckListDTO mrCheckListDTO) {
    log.debug("Request to insert MrHardDevicesCheckList: {}", mrCheckListDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    if (mrCheckListDTO != null) {
      List<MrHardDevicesCheckListDTO> listDTOS = mrHardDevicesCheckListRepository
          .checkListDTOExisted(mrCheckListDTO);
      if (listDTOS != null && listDTOS.size() > 0) {
        resultInSideDto.setKey(RESULT.DUPLICATE);
        resultInSideDto.setMessage(I18n.getLanguage("mrCheckList.validate.checkExisted"));
      } else {
        mrCheckListDTO.setCreatedUser(userToken.getUserName());
        mrCheckListDTO.setCreatedTime(DateTimeUtils.getSysDateTime());
        mrCheckListDTO.setUpdatedTime(DateTimeUtils.getSysDateTime());
        resultInSideDto = mrHardDevicesCheckListRepository.insertOrUpdate(mrCheckListDTO);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto update(MrHardDevicesCheckListDTO mrCheckListDTO) {
    log.debug("Request to update MrHardDevicesCheckList: {}", mrCheckListDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    if (mrCheckListDTO != null) {
      List<MrHardDevicesCheckListDTO> listDTOS = mrHardDevicesCheckListRepository
          .checkListDTOExisted(mrCheckListDTO);
      if (listDTOS != null && listDTOS.size() > 0 && !mrCheckListDTO.getMrHardDevicesCheckListId().equals(listDTOS.get(0).getMrHardDevicesCheckListId())) {
        resultInSideDto.setKey(RESULT.DUPLICATE);
        resultInSideDto.setMessage(I18n.getLanguage("mrCheckList.validate.checkExisted"));
      } else {
        mrCheckListDTO.setUpdatedUser(userToken.getUserName());
        mrCheckListDTO.setUpdatedTime(DateTimeUtils.getSysDateTime());
        resultInSideDto = mrHardDevicesCheckListRepository.insertOrUpdate(mrCheckListDTO);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteMrCheckList(Long checkListId) {
    log.debug("Request to deleteMrCheckList : {}", checkListId);
    return mrHardDevicesCheckListRepository.deleteMrCheckList(checkListId);
  }

  @Override
  public MrHardDevicesCheckListDTO getDetail(Long checkListId) {
    log.debug("Request to getDetail : {}", checkListId);
    return mrHardDevicesCheckListRepository.getDetail(checkListId);
  }

  @Override
  public List<MrHardDevicesCheckListDTO> getListArrayDeviceTypeNetworkType() {
    log.debug("Request to getListArrayDeviceTypeNetworkType : {}");
    return mrHardDevicesCheckListRepository.getListArrayDeviceTypeNetworkType();
  }

  @Override
  public File exportSearchData(MrHardDevicesCheckListDTO mrCheckListDTO) throws Exception {
    lstCountry = catLocationBusiness.getListLocationByLevelCBB(null, 1L, null);
    setMapCountryName();
    List<MrHardDevicesCheckListDTO> mrCheckListDTOList = mrHardDevicesCheckListRepository
        .onSearchExport(mrCheckListDTO);
    if (mrCheckListDTOList != null && mrCheckListDTOList.size() > 0) {
      for (MrHardDevicesCheckListDTO checkListDTO : mrCheckListDTOList) {
        if (checkListDTO.getMarketCode() != null) {
          checkListDTO.setMarketName(mapCountryName.get(checkListDTO.getMarketCode()));
        }
      }
    }
    return exportFileEx(mrCheckListDTOList, "");
  }

  @Override
  public File getFileTemplate() throws Exception {
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
    //Tạo 1 mảng lưu header từng cột
    String[] header = new String[]{
        I18n.getLanguage("mrCheckList.list.stt"),
        I18n.getLanguage("mrCheckList.list.marketCode"),
//        I18n.getLanguage("mrCheckList.list.marketName"),
        I18n.getLanguage("mrCheckList.list.arrayCode"),
//        I18n.getLanguage("mrCheckList.list.networkType"),
        I18n.getLanguage("mrCheckList.list.deviceType"),
        I18n.getLanguage("mrCheckList.list.cycle"),
        I18n.getLanguage("mrCheckList.list.deviceTypeAll"),
        I18n.getLanguage("mrCheckList.list.content"),
        I18n.getLanguage("mrCheckList.list.target"),
//        I18n.getLanguage("mrCheckList.list.createdUser")
    };
    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("mrCheckList.list.marketCode"),
        I18n.getLanguage("mrCheckList.list.arrayCode"),
        I18n.getLanguage("mrCheckList.list.cycle"),
        I18n.getLanguage("mrCheckList.list.content")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    //Khai báo các dropdow cần sử dụng
//    int marketNameColumn = listHeader
//        .indexOf(I18n.getLanguage("mrCheckList.list.marketName"));
    int arrayCodeColumn = listHeader.indexOf(I18n.getLanguage("mrCheckList.list.arrayCode"));
    int cycleColumn = listHeader.indexOf(I18n.getLanguage("mrCheckList.list.cycle"));
    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(3, 3, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(3);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("mrCheckList.export.title"));
    titleCell.setCellStyle(style.get("title"));

    sheetParam1.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
    Row titleRowSheet1 = sheetParam1.createRow(1);
    titleRowSheet1.setHeightInPoints(25);
    Cell titleCellSheet1 = titleRowSheet1.createCell(0);
    titleCellSheet1.setCellValue(I18n.getLanguage("mrCheckList.export.titleSheetParam1"));
    titleCellSheet1.setCellStyle(style.get("title"));

    sheetParam2.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
    Row titleRowSheet2 = sheetParam2.createRow(1);
    titleRowSheet2.setHeightInPoints(25);
    Cell titleCellSheet2 = titleRowSheet2.createCell(0);
    titleCellSheet2.setCellValue(I18n.getLanguage("mrCheckList.export.titleSheetParam2"));
    titleCellSheet2.setCellStyle(style.get("title"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header
    Row headerRow = sheetOne.createRow(7);
    Row headerNation = sheetParam1.createRow(3);
    Row headerArray = sheetParam2.createRow(3);
    headerRow.setHeightInPoints(30);
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
      sheetOne.setColumnWidth(i, 7000);
    }
    Cell headerCellStt = headerNation.createCell(0);
    Cell headerCellMarketCode = headerNation.createCell(1);
    Cell headerCellMarketName = headerNation.createCell(2);
    XSSFRichTextString stt1 = new XSSFRichTextString(
        I18n.getLanguage("mrCheckList.list.stt"));
    XSSFRichTextString mkCode = new XSSFRichTextString(
        I18n.getLanguage("mrCheckList.list.marketCode"));
    XSSFRichTextString mkName = new XSSFRichTextString(
        I18n.getLanguage("mrCheckList.list.marketName"));
    headerCellStt.setCellValue(stt1);
    headerCellStt.setCellStyle(style.get("header"));
    headerCellMarketCode.setCellValue(mkCode);
    headerCellMarketCode.setCellStyle(style.get("header"));
    headerCellMarketName.setCellValue(mkName);
    headerCellMarketName.setCellStyle(style.get("header"));
    sheetParam1.setColumnWidth(0, 15000);
    sheetParam1.setColumnWidth(1, 15000);
    sheetParam1.setColumnWidth(2, 15000);

    headerCellStt = headerArray.createCell(0);
    Cell headerCellArray = headerArray.createCell(1);
//    Cell headerCellNetworkType = headerArray.createCell(2);
    Cell headerCellDeviceType = headerArray.createCell(2);
    XSSFRichTextString array = new XSSFRichTextString(
        I18n.getLanguage("mrCheckList.list.arrayCode"));
//    XSSFRichTextString networkType = new XSSFRichTextString(
//        I18n.getLanguage("mrCheckList.list.networkType"));
    XSSFRichTextString deviceType = new XSSFRichTextString(
        I18n.getLanguage("mrCheckList.list.deviceType"));
    headerCellStt.setCellValue(stt1);
    headerCellStt.setCellStyle(style.get("header"));
    headerCellArray.setCellValue(array);
    headerCellArray.setCellStyle(style.get("header"));
//    headerCellNetworkType.setCellValue(networkType);
//    headerCellNetworkType.setCellStyle(style.get("header"));
    headerCellDeviceType.setCellValue(deviceType);
    headerCellDeviceType.setCellStyle(style.get("header"));
    sheetParam2.setColumnWidth(0, 15000);
    sheetParam2.setColumnWidth(1, 15000);
    sheetParam2.setColumnWidth(2, 15000);
//    sheetParam2.setColumnWidth(3, 15000);
    sheetOne.setColumnWidth(0, 3000);

    // Set dữ liệu vào column dropdown
    int row = 8;
    int k = 1;
    List<ItemDataCRInside> lstMarketCode = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);

    row = 4;
    k = 1;
    for (ItemDataCRInside marketCode : lstMarketCode) {
      excelWriterUtils
          .createCell(sheetParam1, 0, row, String.valueOf(k), style.get("cell"));
      excelWriterUtils
          .createCell(sheetParam1, 1, row, marketCode.getValueStr().toString(), style.get("cell"));
      excelWriterUtils
          .createCell(sheetParam1, 2, row++, marketCode.getDisplayStr(), style.get("cell"));
      k++;
    }
    sheetParam1.autoSizeColumn(0);
    sheetParam1.autoSizeColumn(1);
    sheetParam1.autoSizeColumn(2);

    //dropdow Mảng
    row = 8;
    List<CatItemDTO> lstArrayCode = mrCfgProcedureCDBusiness.getMrSubCategory();
    for (CatItemDTO dto : lstArrayCode) {
      excelWriterUtils
          .createCell(sheetParam, 3, row++, dto.getItemName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(3);
    Name arrayCodeStr = workbook.createName();
    arrayCodeStr.setNameName("mrCheckList.list.arrayCode");
    arrayCodeStr.setRefersToFormula("param!$D$2:$D$" + row);
    XSSFDataValidationConstraint arrayCodeStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "mrCheckList.list.arrayCode");
    CellRangeAddressList arrayCodeStrCreate = new CellRangeAddressList(8, 65000, arrayCodeColumn,
        arrayCodeColumn);
    XSSFDataValidation dataValidationArrayCodeStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            arrayCodeStrConstraint, arrayCodeStrCreate);
    dataValidationArrayCodeStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationArrayCodeStr);

    row = 4;
    k = 1;
    for (CatItemDTO dto : lstArrayCode) {
    List<MrSynItDevicesDTO> list = mrSynItSoftDevicesRepository.getListDeviceTypeByArrayCode(dto.getItemCode());
      excelWriterUtils
          .createCell(sheetParam2, 0, row, String.valueOf(k), style.get("cell"));
      excelWriterUtils
          .createCell(sheetParam2, 1, row, dto.getItemCode(), style.get("cell"));
      if (!list.isEmpty()) {
        for (MrSynItDevicesDTO dto1 : list) {
          excelWriterUtils
              .createCell(sheetParam2, 2, row, dto1.getDeviceType(), style.get("cell"));
          row++;
        }
      }else {
        row++;
      }
      k++;
    }
    sheetParam2.autoSizeColumn(0);
    sheetParam2.autoSizeColumn(1);
    sheetParam2.autoSizeColumn(2);

    //dropdow Chu kỳ(Tháng) Cycle
    row = 8;
    String[] cycleList = {"1", "3", "6", "12"};
    for (String item : cycleList) {
      excelWriterUtils
          .createCell(sheetParam, 6, row++, item, style.get("cell"));
    }
    sheetParam.autoSizeColumn(6);
    Name cycleStr = workbook.createName();
    cycleStr.setNameName("mrCheckList.list.cycle");
    cycleStr.setRefersToFormula("param!$G$2:$G$" + row);
    XSSFDataValidationConstraint CycleConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "mrCheckList.list.cycle");
    CellRangeAddressList CycleStrCreate = new CellRangeAddressList(8, 65000, cycleColumn,
        cycleColumn);
    XSSFDataValidation dataValidationCycleStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            CycleConstraint, CycleStrCreate);
    dataValidationCycleStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationCycleStr);

    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("mrCheckList.export.nameSheetOne"));
    workbook.setSheetName(2, I18n.getLanguage("mrCheckList.export.nameSheetParam1"));
    workbook.setSheetName(3, I18n.getLanguage("mrCheckList.export.nameSheetParam2"));
    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName =
        "IMPORT_MR_HARD_DEVICES_CHECKLIST_TEMPLATE" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importMrCheckList(MultipartFile multipartFile) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    mapCountryName.clear();
    mapArray.clear();
    mapArrayByNetworkType.clear();
    mapNetworkTypeByDeviceType.clear();
    List<MrHardDevicesCheckListDTO> mrCheckListDTOList = new ArrayList<>();
    lstCountry = catLocationBusiness.getListLocationByLevelCBB(null, 1L, null);
    try {
      if (multipartFile == null) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        resultInSideDto.setMessage(I18n.getLanguage("mrCheckList.fileEmpty"));
        return resultInSideDto;
      } else {
        if (!(multipartFile.getOriginalFilename().toUpperCase().endsWith(".XLSX") || multipartFile
            .getOriginalFilename().toUpperCase().endsWith(".XLS"))) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }

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
            7,
            2
        );

        //Kiểm tra form header có đúng chuẩn
        if (headerList.size() == 0 || !validFileFormat(headerList)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORM);
          return resultInSideDto;
        }
        //Lấy dữ liệu import
        List<Object[]> dataImportList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            8,
            0,
            7,
            2
        );
        if (dataImportList.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          return resultInSideDto;
        }
        if (!dataImportList.isEmpty()) {
          int row = 7;
          int index = 0;
          setMapCountryName();
          setMapArray();
          setMapArrayByNetworkType();
          setMapNetworkTypeByDeviceType();
          for (Object[] obj : dataImportList) {
            MrHardDevicesCheckListDTO checkListDTO = new MrHardDevicesCheckListDTO();
            if (obj[1] != null) {
              checkListDTO.setMarketCode(obj[1].toString().trim());
            } else {
              checkListDTO.setMarketCode(null);
            }
            if (obj[2] != null) {
              checkListDTO.setArrayCode(obj[2].toString().trim());
            } else {
              checkListDTO.setArrayCode(null);
            }
//            if (obj[3] != null) {
//              checkListDTO.setNetworkType(obj[3].toString().trim());
//            } else {
//              checkListDTO.setNetworkType(null);
//            }
            if (obj[3] != null) {
              checkListDTO.setDeviceType(obj[3].toString().trim());
            } else {
              checkListDTO.setDeviceType(null);
            }
            if (obj[4] != null) {
              checkListDTO.setCycle(obj[4].toString().trim());
            } else {
              checkListDTO.setCycle(null);
            }
            if (obj[5] != null) {
              checkListDTO.setDeviceTypeAll(obj[5].toString().trim());
            } else {
              checkListDTO.setDeviceTypeAll(null);
            }
            if (obj[6] != null) {
              checkListDTO.setContent(obj[6].toString().trim());
            } else {
              checkListDTO.setContent(null);
            }
            if (obj[7] != null) {
              checkListDTO.setTarget(obj[7].toString().trim());
            } else {
              checkListDTO.setTarget(null);
            }
//            if (obj[9] != null) {
//              checkListDTO.setCreatedUser(obj[9].toString().trim());
//            } else {
//              checkListDTO.setCreatedUser(null);
//            }
            //Validate dữ liệu trong file
            MrHardDevicesCheckListDTO checkListDTOTmp = validateImportInfo(checkListDTO,
                mrCheckListDTOList);
            if (checkListDTOTmp.getResultImport() == null) {
              if (checkListDTOTmp.getMrHardDevicesCheckListId() == null) {
                checkListDTOTmp
                    .setResultImport(I18n.getLanguage("mrChecklist.result.import"));
                checkListDTOTmp.setCreatedTime(DateTimeUtils.getSysDateTime());
                checkListDTOTmp.setUpdatedTime(DateTimeUtils.getSysDateTime());
                String createUserName =
                    StringUtils.isStringNullOrEmpty(checkListDTO.getCreatedUser()) ? TicketProvider
                        .getUserToken().getUserName() : checkListDTO.getCreatedUser();
                checkListDTOTmp.setCreatedUser(createUserName);
              } else {
                checkListDTOTmp
                    .setResultImport(I18n.getLanguage("mrCheckList.update.importUpdate"));
                checkListDTOTmp.setUpdatedTime(DateTimeUtils.getSysDateTime());
                String updateUserName =
                    StringUtils.isStringNullOrEmpty(checkListDTO.getCreatedUser()) ? TicketProvider
                        .getUserToken().getUserName() : checkListDTO.getCreatedUser();
                checkListDTOTmp.setUpdatedUser(updateUserName);
              }
              mrCheckListDTOList.add(checkListDTOTmp);
            } else {
              mrCheckListDTOList.add(checkListDTOTmp);
              index++;
            }
            row++;
          }
          List<MrHardDevicesCheckListDTO> listDTOExport = mrCheckListDTOList;
          if (index != 0) {
            File fileExport = exportFileImport(listDTOExport,
                Constants.RESULT_IMPORT);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
            resultInSideDto.setFilePath(fileExport.getPath());
          } else {
            mrHardDevicesCheckListRepository.insertOrUpdateList(listDTOExport);
            //import thành công và trả ra file kết quả
            File fileExport = exportFileImport(listDTOExport,
                Constants.RESULT_IMPORT);
            resultInSideDto.setKey(RESULT.SUCCESS);
            resultInSideDto.setFile(fileExport);
            resultInSideDto.setFilePath(fileExport.getPath());
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          return resultInSideDto;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(e.getMessage());
    }
    return resultInSideDto;
  }

  private MrHardDevicesCheckListDTO validateImportInfo(MrHardDevicesCheckListDTO dto,
      List<MrHardDevicesCheckListDTO> list) {
    //Quốc gia
    if (StringUtils.isStringNullOrEmpty(dto.getMarketCode())) {
      dto.setResultImport(I18n.getLanguage("mrCheckList.check.marketCode") + " " + I18n
          .getLanguage("common.notnull"));
      return dto;
    } else if (!mapCountryName.containsKey(dto.getMarketCode())) {
      dto.setResultImport(I18n.getLanguage("mrCheckList.check.marketCode") + " " + I18n
          .getLanguage("mrCheckList.dataImport.notexist"));
      return dto;
    }
    //Mảng
    if (StringUtils.isStringNullOrEmpty(dto.getArrayCode())) {
      dto.setResultImport(I18n.getLanguage("mrCheckList.check.arrayCode") + " " + I18n
          .getLanguage("common.notnull"));
      return dto;
    } else if (!mapArray.containsKey(dto.getArrayCode())) {
      dto.setResultImport(I18n.getLanguage("mrCheckList.check.arrayCode") + " " + I18n
          .getLanguage("mrCheckList.dataImport.notexist"));
      return dto;
    } else {
//      if (!StringUtils.isStringNullOrEmpty(dto.getNetworkType())) {
//        boolean isNetworkType = true;
//        //Xử lý cho Loại Mạng
//        List<MrDeviceDTO> lstNetwork = mrHardDevicesCheckListRepository.getListNetworkType();
//        for (MrDeviceDTO deviceDTO : lstNetwork) {
//          if (dto.getNetworkType().equals(deviceDTO.getNetworkType())) {
//            isNetworkType = false;
//            break;
//          }
//        }
//        if (isNetworkType) {
//          dto.setResultImport(I18n.getLanguage("mrCheckList.check.networkType") + " " + I18n
//              .getLanguage("mrCheckList.dataImport.notexist"));
//          return dto;
//        }
//        //so sánh networkType nhap vao co dung voi mang hay ko?
//        if (isNetworkType == false) {
//          if (!dto.getArrayCode()
//              .equals(mapArrayByNetworkType.get(dto.getNetworkType() + "_" + dto.getArrayCode()))) {
//            dto.setResultImport(I18n.getLanguage("mrCheckList.networkType.falseArrayCode"));
//            return dto;
//          }
//        }

        //Xử lý cho Loại Thiết Bị
        if (!StringUtils.isStringNullOrEmpty(dto.getDeviceType())) {
          boolean isDevicekType = true;
          List<MrDeviceDTO> lstDevice = mrHardDevicesCheckListRepository.getListDeviceType();
          for (MrDeviceDTO dtotemp : lstDevice) {
            if (dto.getDeviceType().equals(dtotemp.getDeviceType())) {
              isDevicekType = false;
              break;
            }
          }
          if (isDevicekType) {
            dto.setResultImport(I18n.getLanguage("mrCheckList.check.deviceType") + " " + I18n
                .getLanguage("mrCheckList.dataImport.notexist"));
            return dto;
          }
          //so sánh DeviceType nhap vao co dung voi Loai Mang hay ko?
//          if (isDevicekType == false) {
//            if (!dto.getNetworkType().equals(
//                mapNetworkTypeByDeviceType.get(dto.getDeviceType() + "_" + dto.getNetworkType()))) {
//              dto.setResultImport(I18n.getLanguage("mrCheckList.deviceType.falseNetworkType"));
//              return dto;
//            }
//          }
        }
//      }
    }
//    if (!StringUtils.isStringNullOrEmpty(dto.getDeviceType())) {
//      if (StringUtils.isStringNullOrEmpty(dto.getNetworkType())) {
//        dto.setResultImport(I18n.getLanguage("mrCheckList.check.networkType") + " " + I18n
//            .getLanguage("common.notnull"));
//        return dto;
//      }
//    }

    //Điều kiện giữa DeviceType và DeviceTypeAll
    if (StringUtils.isStringNullOrEmpty(dto.getDeviceType()) && StringUtils
        .isStringNullOrEmpty(dto.getDeviceTypeAll())) {
      dto.setResultImport(
          I18n.getLanguage("mrChecklist.deviceTypeOrDeviceTypeAll.type1in2.required"));
      return dto;
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getDeviceType()) && !StringUtils
        .isStringNullOrEmpty(dto.getDeviceTypeAll())) {
      dto.setResultImport(I18n.getLanguage("mrChecklist.deviceTypeOrDeviceTypeAll.only.type1in2"));
      return dto;
    }

    //Chu kỳ
    if (StringUtils.isStringNullOrEmpty(dto.getCycle())) {
      dto.setResultImport(I18n.getLanguage("mrCheckList.check.cycle") + " " + I18n
          .getLanguage("common.notnull"));
      return dto;
    } else if (!dto.getCycle().matches(REGEX_NUMBER)) {
      dto.setResultImport(I18n.getLanguage("mrCheckList.check.cycle") + " " + I18n
          .getLanguage("mrChecklist.checkData.cycle"));
      return dto;
    } else if (!"1".equals(dto.getCycle()) && !"3".equals(dto.getCycle()) && !"6"
        .equals(dto.getCycle()) && !"12".equals(dto.getCycle())) {
      dto.setResultImport(I18n.getLanguage("mrChecklist.cycle.invalid"));
      return dto;
    }

    //Chỉ tiêu, quy định
    if (!StringUtils.isStringNullOrEmpty(dto.getDeviceTypeAll())) {
      if (dto.getDeviceTypeAll().length() > 200) {
        dto.setResultImport(I18n.getLanguage("mrChecklist.deviceTypeAll.overlength"));
        return dto;
      }
    }

    //Nội dung kiểm tra
    if (StringUtils.isStringNullOrEmpty(dto.getContent())) {
      dto.setResultImport(I18n.getLanguage("mrChecklist.check.content") + " " + I18n
          .getLanguage("common.notnull"));
      return dto;
    } else if (dto.getContent().length() > 1000) {
      dto.setResultImport(I18n.getLanguage("mrChecklist.content.overlength"));
      return dto;
    }

    //Mục tiêu
    if (!StringUtils.isStringNullOrEmpty(dto.getTarget())) {
      if (dto.getTarget().length() > 500) {
        dto.setResultImport(I18n.getLanguage("mrChecklist.target.overlength"));
        return dto;
      }
    }

    //User tạo
//    if (!StringUtils.isStringNullOrEmpty(dto.getCreatedUser())) {
//      try {
//        UsersEntity usersEntity = userRepositoryImpl.getUserByUserName(dto.getCreatedUser());
//        if (usersEntity == null) {
//          dto.setResultImport(I18n.getLanguage("mrChecklist.checkCreatedUser.notexist"));
//          return dto;
//        } else {
//          if (usersEntity.getIsEnable() != null && usersEntity.getIsEnable() == 1L) {
//
//          } else if (usersEntity.getIsEnable() == 0) {
//            dto.setResultImport(I18n.getLanguage("mrChecklist.checkCreatedUser.notexist"));
//            return dto;
//          }
//        }
//      } catch (Exception e) {
//        dto.setResultImport(I18n.getLanguage("mrChecklist.checkCreatedUser.notexist"));
//        return dto;
//      }
//    }

    //check nếu tồn tại bản ghi trong DB thì thực hiện update
    if (!StringUtils.isStringNullOrEmpty(dto.getMarketCode())
        && !StringUtils.isStringNullOrEmpty(dto.getArrayCode())
        && !StringUtils.isStringNullOrEmpty(dto.getCycle())) {
      List<MrHardDevicesCheckListDTO> listDTOS = mrHardDevicesCheckListRepository
          .checkListDTOExisted(dto);
      if (listDTOS != null && listDTOS.size() > 0) {
        MrHardDevicesCheckListDTO checkListDTO = listDTOS.get(0);
        if (checkListDTO != null) {
          dto.setMrHardDevicesCheckListId(checkListDTO.getMrHardDevicesCheckListId());
          dto.setCreatedTime(checkListDTO.getCreatedTime());
          dto.setCreatedUser(checkListDTO.getCreatedUser());
          return dto;
        }
      }
    }

    //Check trùng bản ghi trong 1 file import
    if (list != null && list.size() > 0 && StringUtils.isStringNullOrEmpty(dto.getResultImport())) {
      return validateDuplicate(list, dto);
    }
    return dto;
  }

  private MrHardDevicesCheckListDTO validateDuplicate(List<MrHardDevicesCheckListDTO> list,
      MrHardDevicesCheckListDTO dto) {
    for (int i = 0; i < list.size(); i++) {
      MrHardDevicesCheckListDTO mrCheckListDTOTmp = list.get(i);
      if (I18n.getLanguage("mrChecklist.result.import")
          .equals(mrCheckListDTOTmp.getResultImport())
          && String.valueOf(mrCheckListDTOTmp.getMarketCode())
          .equals(String.valueOf(dto.getMarketCode()))
          && String.valueOf(mrCheckListDTOTmp.getArrayCode())
          .equals(String.valueOf(dto.getArrayCode()))
//          && String.valueOf(mrCheckListDTOTmp.getNetworkType())
//          .equals(String.valueOf(dto.getNetworkType()))
          && String.valueOf(mrCheckListDTOTmp.getDeviceType())
          .equals(String.valueOf(dto.getDeviceType()))
          && String.valueOf(mrCheckListDTOTmp.getCycle()).equals(String.valueOf(dto.getCycle()))
          && String.valueOf(mrCheckListDTOTmp.getDeviceTypeAll())
          .equals(String.valueOf(dto.getDeviceTypeAll()))
          && String.valueOf(mrCheckListDTOTmp.getContent()).equals(String.valueOf(dto.getContent()))
          && String.valueOf(mrCheckListDTOTmp.getTarget()).equals(String.valueOf(dto.getTarget()))
//          && String.valueOf(mrCheckListDTOTmp.getCreatedUser())
//          .equals(String.valueOf(dto.getCreatedUser()))
      ) {
        dto.setResultImport(I18n.getLanguage("mrChecklist.err.dup-code-in-file")
            .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
    }
    return dto;
  }

  private File exportFileEx(List<MrHardDevicesCheckListDTO> mrCheckListDTOList,
      String key) throws Exception {
    String sheetName = I18n.getLanguage("mrCheckList.export.nameSheetOne");
    String title = I18n.getLanguage("mrCheckList.export.title");
    String fileNameOut = MR_HARD_DEVICES_CHECKLIST_EXPORT;
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();

    columnSheet = new ConfigHeaderExport("marketName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("arrayCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
//    headerExportList.add(columnSheet);
//    columnSheet = new ConfigHeaderExport("networkType", "LEFT", false, 0, 0, new String[]{}, null,
//        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("deviceType", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("cycle", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("deviceTypeAll", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("content", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("target", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
//    headerExportList.add(columnSheet);
//    columnSheet = new ConfigHeaderExport("createdUser", "LEFT", false, 0, 0, new String[]{}, null,
//        "STRING");
    headerExportList.add(columnSheet);

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        mrCheckListDTOList
        , sheetName
        , title
        , null
        , 7
        , 3
        , 7
        , true
        , "language.mrCheckList.list"
        , headerExportList
        , fieldSplit
        , ""
        , I18n.getLanguage("mrCheckList.export.firstLeftHeader")
        , I18n.getLanguage("mrCheckList.export.secondLeftHeader")
        , I18n.getLanguage("mrCheckList.export.firstRightHeader")
        , I18n.getLanguage("mrCheckList.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;

    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("mrCheckList.list.stt"),
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

  private File exportFileImport(List<MrHardDevicesCheckListDTO> mrCheckListDTOList,
      String key) throws Exception {
    String sheetName = I18n.getLanguage("mrCheckList.export.nameSheetOne");
    String title = I18n.getLanguage("mrCheckList.export.title");
    String fileNameOut = MR_HARD_DEVICES_CHECKLIST_RESULT_IMPORT;
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();

    columnSheet = new ConfigHeaderExport("marketCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("arrayCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
//    columnSheet = new ConfigHeaderExport("networkType", "LEFT", false, 0, 0, new String[]{}, null,
//        "STRING");
//    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("deviceType", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("cycle", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("deviceTypeAll", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("content", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("target", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
//    columnSheet = new ConfigHeaderExport("createdUser", "LEFT", false, 0, 0, new String[]{}, null,
//        "STRING");
//    headerExportList.add(columnSheet);
    //kiểm tra đầu vào
    if (Constants.RESULT_IMPORT.equals(key)) {
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
    }
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        mrCheckListDTOList
        , sheetName
        , title
        , null
        , 7
        , 3
        , 7
        , true
        , "language.mrCheckList.list"
        , headerExportList
        , fieldSplit
        , ""
        , I18n.getLanguage("mrCheckList.export.firstLeftHeader")
        , I18n.getLanguage("mrCheckList.export.secondLeftHeader")
        , I18n.getLanguage("mrCheckList.export.firstRightHeader")
        , I18n.getLanguage("mrCheckList.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;

    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("mrCheckList.list.stt"),
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

  Map<String, String> mapCountryName = new HashMap<>();

  public void setMapCountryName() {
    List<ItemDataCRInside> lstCountryName = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    if (lstCountryName != null && !lstCountryName.isEmpty()) {
      for (ItemDataCRInside itemDataCR : lstCountryName) {
        mapCountryName.put(String.valueOf(itemDataCR.getValueStr()), itemDataCR.getDisplayStr());
      }
    }
  }

  HashMap<String, String> mapArray = new HashMap<>();

  public void setMapArray() {
    List<CatItemDTO> listData = mrCfgProcedureCDBusiness.getMrSubCategory();
    if (listData != null && !listData.isEmpty()) {
      for (CatItemDTO dto : listData) {
        mapArray.put(String.valueOf(dto.getItemCode()), dto.getItemName());
      }
    }
  }

  HashMap<String, String> mapArrayByNetworkType = new HashMap<>();

  public void setMapArrayByNetworkType() {
    //Lấy ra ds Mảng, loại mạng, loại thiết bị
    List<MrHardDevicesCheckListDTO> checkListDTOList = mrHardDevicesCheckListRepository
        .getListArrayDeviceTypeNetworkType();
    if (checkListDTOList != null && !checkListDTOList.isEmpty()) {
      for (MrHardDevicesCheckListDTO dto : checkListDTOList) {
        mapArrayByNetworkType
            .put(dto.getNetworkType() + "_" + dto.getArrayCode(), dto.getArrayCode());
      }
    }
  }

  HashMap<String, String> mapNetworkTypeByDeviceType = new HashMap<>();

  public void setMapNetworkTypeByDeviceType() {
    //Lấy ra ds Mảng, loại mạng, loại thiết bị
    List<MrHardDevicesCheckListDTO> checkListDTOList = mrHardDevicesCheckListRepository
        .getListArrayDeviceTypeNetworkType();
    if (checkListDTOList != null && !checkListDTOList.isEmpty()) {
      for (MrHardDevicesCheckListDTO dto : checkListDTOList) {
        mapNetworkTypeByDeviceType
            .put(dto.getDeviceType() + "_" + dto.getNetworkType(), dto.getNetworkType());
      }
    }
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
    if (!I18n.getLanguage("mrCheckList.list.stt")
        .equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCheckList.list.marketCode") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
//    if (!I18n.getLanguage("mrCheckList.list.marketName")
//        .equalsIgnoreCase(objects[2].toString().trim())) {
//      return false;
//    }
    if (!(I18n.getLanguage("mrCheckList.list.arrayCode") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
//    if (!I18n.getLanguage("mrCheckList.list.networkType")
//        .equalsIgnoreCase(objects[3].toString().trim())) {
//      return false;
//    }
    if (!I18n.getLanguage("mrCheckList.list.deviceType")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCheckList.list.cycle") + "*")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrCheckList.list.deviceTypeAll")
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCheckList.list.content") + "*")
        .equalsIgnoreCase(objects[6].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("mrCheckList.list.target")
        .equalsIgnoreCase(objects[7].toString().trim())) {
      return false;
    }
//    if (!I18n.getLanguage("mrCheckList.list.createdUser")
//        .equalsIgnoreCase(objects[9].toString().trim())) {
//      return false;
//    }
    return true;
  }

}
