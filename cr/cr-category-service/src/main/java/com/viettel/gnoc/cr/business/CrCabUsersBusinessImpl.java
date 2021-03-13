package com.viettel.gnoc.cr.business;

import static com.viettel.gnoc.commons.repository.BaseRepository.getSqlLanguageExchange;
import static com.viettel.gnoc.commons.repository.BaseRepository.setLanguage;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.DataItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ObjectSearchDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrCabUsersDTO;
import com.viettel.gnoc.cr.dto.CrCabUsersExportDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.repository.CrCabUsersRepositoty;
import com.viettel.gnoc.ws.provider.WSCxfInInterceptor;
import java.io.File;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@Slf4j
@Transactional
@Service
public class CrCabUsersBusinessImpl implements CrCabUsersBusiness {

  @Autowired
  protected CrCabUsersRepositoty crCabUsersRepositoty;

  @Autowired
  protected CommonRepository commonRepository;

  @Autowired
  protected LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  TicketProvider ticketProvider;
  @Value("${application.temp.folder}")
  private String tempFolder;

  Map<String, String> mapSegmentName = new HashMap<>();
  Map<String, String> mapCabUnitName = new HashMap<>();
  Map<String, String> mapUnitName = new HashMap<>();

  public void setSegmentName() {
    List<ItemDataCRInside> list = getListImpactSegmentCBB();
    if (list != null && !list.isEmpty()) {
      for (ItemDataCRInside item : list) {
        mapSegmentName.put(item.getDisplayStr(), String.valueOf(item.getValueStr()));
      }
    }
  }

  public void setUnitName() {
    List<CrCabUsersDTO> list = crCabUsersRepositoty.getListUnitName();
    if (list != null && !list.isEmpty()) {
      for (CrCabUsersDTO item : list) {
        mapUnitName.put(item.getExecuteUnitName(), String.valueOf(item.getExecuteUnitId()));
      }
    }
  }

  public void setCabUnitName() {
    List<CrCabUsersDTO> list = crCabUsersRepositoty.getListUserFullName();
    if (list != null && !list.isEmpty()) {
      for (CrCabUsersDTO item : list) {
        mapCabUnitName.put(item.getUserFullName(), String.valueOf(item.getUserID()));
      }
    }
  }

  @Override
  public ResultInSideDto insertCrCabUsers(CrCabUsersDTO crCabUsersDTO) {
    ResultInSideDto resultInSideDto = crCabUsersRepositoty.insertCrCabUsers(crCabUsersDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Add crCabUsers", "Add crCabUsers ID: " + resultInSideDto.getId(),
          crCabUsersDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateCrCabUsers(CrCabUsersDTO crCabUsersDTO) {
    ResultInSideDto resultInSideDto = crCabUsersRepositoty.updateCrCabUsers(crCabUsersDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Update crCabUsers", "Update crCabUsers ID: " + resultInSideDto.getId(),
          crCabUsersDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteCrCabUsers(Long id) {
    ResultInSideDto resultInSideDto = crCabUsersRepositoty.deleteCrCabUsers(id);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Delete crCabUsers", "Delete crCabUsers ID: " + id,
          null, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListCrCabUsers(List<CrCabUsersDTO> crCabUsersDTOS) {
    return crCabUsersRepositoty.deleteListCrCabUsers(crCabUsersDTOS);
  }

  @Override
  public Datatable getAllInfoCrCABUsers(CrCabUsersDTO crCabUsersDTO) {
    return crCabUsersRepositoty.getAllInfoCrCABUsers(crCabUsersDTO);
  }

  @Override
  public List<UnitDTO> getAllUserInUnitCrCABUsers(Long deptId, Long userId, String userName,
      String fullName, String staffCode, String deptName, String deptCode) {
    return crCabUsersRepositoty
        .getAllUserInUnitCrCABUsers(deptId, userId, userName, fullName, staffCode, deptName,
            deptCode);
  }

  @Override
  public List<ItemDataCRInside> getListImpactSegmentCBB() {
    List<ItemDataCRInside> lstReturn = new ArrayList<>();
    try {
      lstReturn = crCabUsersRepositoty.getListImpactSegmentCBB();
      String myLanguage = WSCxfInInterceptor.getLanguage();
      String locale = I18n.getLocale();
      if (StringUtils.isNotNullOrEmpty(locale)) {
        myLanguage = locale;
      }
      Map<String, Object> map = getSqlLanguageExchange("2",
          "10", myLanguage);
      String sqlLanguage = (String) map.get("sql");
      Map mapParam = (Map) map.get("mapParam");
      Map mapType = (Map) map.get("mapType");
      List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
          .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
      lstReturn = setLanguage(lstReturn, lstLanguage, "valueStr", "displayStr");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstReturn;
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    List<CrCabUsersExportDTO> lstoExportDTOS;
    List<CrCabUsersDTO> crCabUsersDTOS;
    Map<String, String> mapExportDTO;
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    String[] header = new String[]{"segmentName", "executeUnitName", "cabUnitName", "userFullName",
        "creationUnitName"};
    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDTO.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDTO;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        if (!RESULT.SUCCESS.equals(resultInSideDTO.getKey())) {
          return resultInSideDTO;
        }
        File fileImport = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImport,
            0,//sheet
            4,//begin row
            0,//from column
            7,//to column
            1000);
        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDTO.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDTO;
        }
        // Lay du lieu import
        List<Object[]> lstData = CommonImport.getDataFromExcelFile(
            fileImport,
            0,//sheet
            5,//begin row
            0,//from column
            7,//to column
            1000
        );
        if (lstData.size() > 1500) {
          resultInSideDTO.setKey(RESULT.DATA_OVER);
          return resultInSideDTO;
        }
        crCabUsersDTOS = new ArrayList<>();
        lstoExportDTOS = new ArrayList<>();
        if (!lstData.isEmpty()) {
          int index = 0;
          int value = 1;
          setSegmentName();
          setUnitName();
          setCabUnitName();
          for (Object[] obj : lstData) {
            CrCabUsersExportDTO dto = new CrCabUsersExportDTO();
            CrCabUsersDTO crCabUsersDTO = new CrCabUsersDTO();
            if (obj[1] != null) {
              dto.setSegmentName(obj[1].toString().trim());
              if (mapSegmentName.get(dto.getSegmentName()) != null) {
                crCabUsersDTO
                    .setImpactSegmentId(Long.valueOf(mapSegmentName.get(dto.getSegmentName())));
                dto.setImpactSegmentId(Long.valueOf(mapSegmentName.get(dto.getSegmentName())));
                crCabUsersDTO.setSegmentName(dto.getSegmentName());
              } else {
                dto.setImpactSegmentId(null);
              }
            }
            if (obj[2] != null) {
              dto.setExecuteUnitName(obj[2].toString().trim());
              if (mapUnitName.get(dto.getExecuteUnitName()) != null) {
                crCabUsersDTO
                    .setExecuteUnitId(Long.valueOf(mapUnitName.get(dto.getExecuteUnitName())));
                dto.setExecuteUnitId(Long.valueOf(mapUnitName.get(dto.getExecuteUnitName())));
                crCabUsersDTO.setExecuteUnitName(dto.getExecuteUnitName());
              } else {
                dto.setExecuteUnitId(null);
              }
            }
            if (obj[3] != null) {
              dto.setCabUnitName(obj[3].toString().trim());
              if (mapUnitName.get(dto.getCabUnitName()) != null) {
                crCabUsersDTO.setCabUnitId(Long.valueOf(mapUnitName.get(dto.getCabUnitName())));
                dto.setCabUnitId(Long.valueOf(mapUnitName.get(dto.getCabUnitName())));
                crCabUsersDTO.setCabUnitName(dto.getCabUnitName());
              } else {
                dto.setCabUnitId(null);
              }
            }
            if (obj[4] != null) {
              dto.setUserFullName(obj[4].toString().trim());
              if (mapCabUnitName.get(dto.getUserFullName()) != null) {
                crCabUsersDTO.setUserID(Long.valueOf(mapCabUnitName.get(dto.getUserFullName())));
                dto.setUserID(Long.valueOf(mapCabUnitName.get(dto.getUserFullName())));
                crCabUsersDTO.setUserFullName(dto.getUserFullName());
              } else {
                dto.setUserID(null);
              }
            }
            if (obj[5] != null) {
              dto.setCreationUnitName(obj[5].toString().trim());
              if (mapUnitName.get(dto.getCreationUnitName()) != null) {
                crCabUsersDTO
                    .setCreationUnitId(Long.valueOf(mapUnitName.get(dto.getCreationUnitName())));
                dto.setCreationUnitId(Long.valueOf(mapUnitName.get(dto.getCreationUnitName())));
                crCabUsersDTO.setCreationUnitName(dto.getCreationUnitName());
              } else {
                dto.setCreationUnitId(null);
              }
            }
            CrCabUsersExportDTO tempExportDTO = validateImportInfo(dto, lstoExportDTOS,
                crCabUsersDTO);
            if (tempExportDTO.getResultImport() == null) {
              tempExportDTO.setResultImport(I18n.getLanguage("crCabUsers.result.import.ok"));
              crCabUsersDTOS.add(crCabUsersDTO);
              lstoExportDTOS.add(tempExportDTO);
            } else {
              lstoExportDTOS.add(tempExportDTO);
              index++;
            }
          }
          if (index == 0) {
            if (!crCabUsersDTOS.isEmpty()) {
              for (CrCabUsersDTO item : crCabUsersDTOS) {
                if (item.getUserID() == null) {
                  ObjectSearchDto objectSearchDto = new ObjectSearchDto();
                  objectSearchDto.setModuleName("USERS");
                  objectSearchDto.setParent(item.getCabUnitId().toString());
                  objectSearchDto.setIsHasChildren(0L);
                  objectSearchDto.setRownum(0L);
                  List<DataItemDTO> userFullNameCBB = commonRepository
                      .getListCombobox(objectSearchDto);
                  for (DataItemDTO dto : userFullNameCBB) {
                    item.setUserID(Long.valueOf(dto.getItemId()));
                    resultInSideDTO = insertCrCabUsers(item);
                  }
                } else {
                  resultInSideDTO = insertCrCabUsers(item);
                }
              }
            }
          } else {
            File fileExport = handleFileExport(lstoExportDTOS, header, null,
                Constants.RESULT_IMPORT);
            resultInSideDTO.setKey(RESULT.ERROR);
            resultInSideDTO.setFile(fileExport);
          }
        } else {
          resultInSideDTO.setKey(RESULT.NODATA);
          resultInSideDTO.setMessage(Constants.FILE_NULL);
          File fileExport = handleFileExport(lstoExportDTOS, header, null,
              Constants.RESULT_IMPORT);
          resultInSideDTO.setFile(fileExport);
          return resultInSideDTO;
        }
      }

    } catch (Exception e) {
      log.error("Exception:", e);
      resultInSideDTO.setKey(RESULT.ERROR);
      resultInSideDTO.setMessage(e.getMessage());
    }
    if (resultInSideDTO.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "importData", "import CrCabUsers",
          null, null));
    }
    return resultInSideDTO;
  }

  @Override
  public CrCabUsersDTO findById(Long id) {
    return crCabUsersRepositoty.findById(id);
  }

  @Override
  public File exportData(CrCabUsersDTO crCabUsersDTO) throws Exception {
    crCabUsersDTO.setPage(1);
    crCabUsersDTO.setPageSize(Integer.MAX_VALUE);
    List<CrCabUsersExportDTO> lisExportDTO = new ArrayList<>();
    Datatable data = crCabUsersRepositoty.getAllInfoCrCABUsers(crCabUsersDTO);
    List<CrCabUsersDTO> list = (List<CrCabUsersDTO>) data.getData();
    if (!list.isEmpty() && list != null) {
      for (CrCabUsersDTO item : list) {
        CrCabUsersExportDTO exportDTO = new CrCabUsersExportDTO();
        exportDTO.setSegmentName(item.getSegmentName());
        exportDTO.setExecuteUnitName(item.getExecuteUnitName());
        exportDTO.setUserFullName(item.getUserFullName());
        exportDTO.setCabUnitName(item.getCabUnitName());
        exportDTO.setCreationUnitName(item.getCreationUnitName());
        lisExportDTO.add(exportDTO);
      }
    }
    String[] header = new String[]{"segmentName", "executeUnitName", "cabUnitName", "userFullName",
        "creationUnitName"};
    File file = handleFileExport(lisExportDTO, header, DateUtil.date2ddMMyyyyHHMMss(new Date()),
        "");
    if (file != null) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Export CrCabUsers", "Export CrCabUsers ",
          null, null));
    }
    return handleFileExport(lisExportDTO, header, DateUtil.date2ddMMyyyyHHMMss(new Date()), "");
  }

  @Override
  public File getTemplate() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    org.springframework.core.io.Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workBook.getSheetAt(0);
    XSSFSheet sheetParam2 = workBook.createSheet("cabPersonnel");
    XSSFSheet sheetParam = workBook.createSheet("impactArray");
    XSSFSheet sheetParam1 = workBook.createSheet("cabUnit");
    String[] header = new String[]{I18n.getLanguage("crCabUsers.stt"),
        I18n.getLanguage("crCabUsers.segmentName"),
        I18n.getLanguage("crCabUsers.executeUnitName"),
        I18n.getLanguage("crCabUsers.cabUnitName"),
        I18n.getLanguage("crCabUsers.userFullName"),
        I18n.getLanguage("crCabUsers.creationUnitName")
    };

    String[] headerStar = new String[]{
        I18n.getLanguage("crCabUsers.segmentName"),
        I18n.getLanguage("crCabUsers.executeUnitName"),
        I18n.getLanguage("crCabUsers.cabUnitName"),
    };
    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);
    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    int impactSegmentCBBColumn = listHeader.indexOf(I18n.getLanguage("crCabUsers.segmentName"));
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(2);
    titleRow.setHeightInPoints(30);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("crCabUsers.title"));
    titleCell.setCellStyle(styles.get("title"));

    Row firstLeftHeaderRow = sheetOne.createRow(0);
    Cell firstLeftHeaderCell = firstLeftHeaderRow.createCell(1);
    firstLeftHeaderRow.setHeightInPoints(16);
    firstLeftHeaderCell.setCellValue(I18n.getLanguage("common.export.firstLeftHeader"));
    firstLeftHeaderCell.setCellStyle(styles.get("indexTitle"));
    Row secondLeftHeaderRow = sheetOne.createRow(1);
    Cell secondLeftHeaderCell = secondLeftHeaderRow.createCell(1);
    secondLeftHeaderRow.setHeightInPoints(16);
    secondLeftHeaderCell.setCellValue(I18n.getLanguage("common.export.secondLeftHeader"));
    secondLeftHeaderCell.setCellStyle(styles.get("indexTitle"));
    Cell firstRightHeaderCell = firstLeftHeaderRow.createCell(4);
    firstLeftHeaderRow.setHeightInPoints(16);
    firstRightHeaderCell.setCellValue(I18n.getLanguage("common.export.firstRightHeader"));
    firstRightHeaderCell.setCellStyle(styles.get("indexTitle"));
    Cell secondRightHeaderCell = secondLeftHeaderRow.createCell(4);
    secondLeftHeaderRow.setHeightInPoints(16);
    secondRightHeaderCell.setCellValue(I18n.getLanguage("common.export.secondRightHeader"));
    secondRightHeaderCell.setCellStyle(styles.get("indexTitle"));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao header
    Row headerRow = sheetOne.createRow(4);
    Row headerRowImpactArray = sheetParam.createRow(4);
    Row headerRowCabUnit = sheetParam1.createRow(0);
    Row headerRowCabPersonnel = sheetParam2.createRow(0);
    headerRow.setHeightInPoints(18);
    headerRowImpactArray.setHeightInPoints(18);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeader : listHeaderStar) {
        if (checkHeader.equalsIgnoreCase(listHeader.get(i))) {
          rt.append("(*)", fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(styles.get("header"));
      sheetOne.setColumnWidth(i, 7000);
    }
    Cell headerCellStt = headerRowCabUnit.createCell(0);
    Cell headerCellStt1 = headerRowCabPersonnel.createCell(0);
    Cell headerCellCabUnit = headerRowCabUnit.createCell(1);
    Cell headerCellCabPersonnel = headerRowCabPersonnel.createCell(1);
    Cell headerCellCabUnit1 = headerRowCabPersonnel.createCell(2);
    XSSFRichTextString stt = new XSSFRichTextString(I18n.getLanguage("crCabUsers.stt"));
    XSSFRichTextString cabUnit = new XSSFRichTextString(I18n.getLanguage("crCabUsers.unit"));
    XSSFRichTextString user = new XSSFRichTextString(I18n.getLanguage("crCabUsers.userFullName"));
    headerCellStt.setCellValue(stt);
    headerCellStt.setCellStyle(styles.get("header"));
    headerCellStt1.setCellValue(stt);
    headerCellStt1.setCellStyle(styles.get("header"));
    headerCellCabUnit.setCellValue(cabUnit);
    headerCellCabUnit.setCellStyle(styles.get("header"));
    headerCellCabPersonnel.setCellValue(user);
    headerCellCabPersonnel.setCellStyle(styles.get("header"));
    headerCellCabUnit1.setCellValue(cabUnit);
    headerCellCabUnit1.setCellStyle(styles.get("header"));
    sheetParam1.setColumnWidth(1, 20000);
    sheetParam2.setColumnWidth(1, 12000);
    sheetParam2.setColumnWidth(2, 20000);

    // Set dữ liệu vào column dropdown
    int row = 5;
    List<ItemDataCRInside> getListImpactSegmentCBB = getListImpactSegmentCBB();
    for (ItemDataCRInside dto : getListImpactSegmentCBB) {
      ewu.createCell(sheetParam, 1, row++, dto.getDisplayStr(), styles.get("cell"));
    }
    sheetParam.autoSizeColumn(0);

    Name segmentName = workBook.createName();
    segmentName.setNameName("segmentName");
    segmentName.setRefersToFormula("impactArray!$B$2:$B$" + row);

    XSSFDataValidationConstraint segmentNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "segmentName");
    CellRangeAddressList segmentNameCreate = new CellRangeAddressList(5, 65000,
        impactSegmentCBBColumn,
        impactSegmentCBBColumn);
    XSSFDataValidation dataValidationSegmentName = (XSSFDataValidation) dvHelper
        .createValidation(
            segmentNameConstraint, segmentNameCreate);
    dataValidationSegmentName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationSegmentName);

    row = 1;
    List<CrCabUsersDTO> cabUnitNameCBB = crCabUsersRepositoty.getListUnitName();
    for (CrCabUsersDTO dto : cabUnitNameCBB) {
      ewu.createCell(sheetParam1, 1, row++, dto.getExecuteUnitName(), styles.get("cell"));
    }
    for (row = 1; row <= cabUnitNameCBB.size(); row++) {
      ewu.createCell(sheetParam1, 0, row, String.valueOf(row), styles.get("cell"));
    }
    sheetParam1.autoSizeColumn(0);
    sheetParam1.autoSizeColumn(1);

    row = 1;
    List<DataItemDTO> userFullNameCBB = crCabUsersRepositoty.getLisFullNameMapUnitCab();
    for (DataItemDTO dataItemDTO : userFullNameCBB) {
      ewu.createCell(sheetParam2, 1, row, dataItemDTO.getItemName(), styles.get("cell"));
      ewu.createCell(sheetParam2, 2, row++, dataItemDTO.getParenItemName(), styles.get("cell"));
    }
    for (row = 1; row <= userFullNameCBB.size(); row++) {
      ewu.createCell(sheetParam2, 0, row, String.valueOf(row), styles.get("cell"));
    }
    sheetParam2.autoSizeColumn(0);

    sheetOne.setColumnWidth(0, 3000);
    workBook.setSheetName(0, I18n.getLanguage("crCabUsers.title"));
    workBook.setSheetName(1, I18n.getLanguage("crCabUsers.cabPersonnel"));
    workBook.setSheetName(3, I18n.getLanguage("crCabUsers.unit"));
    workBook.setSheetHidden(2, true);
    sheetParam.setSelected(false);
    sheetParam1.setSelected(false);
    sheetParam2.setSelected(false);
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_CR_CAB_USERS" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;

  }

  private CrCabUsersExportDTO validateImportInfo(CrCabUsersExportDTO dto,
      List<CrCabUsersExportDTO> list, CrCabUsersDTO crCabUsersDTO) {
    if (StringUtils.isStringNullOrEmpty(dto.getSegmentName())) {
      dto.setResultImport(I18n.getLanguage("crCabUsers.err.segmentName"));
      return dto;
    }
    if (StringUtils.isStringNullOrEmpty(dto.getExecuteUnitName())) {
      dto.setResultImport(I18n.getLanguage("crCabUsers.err.executeUnitName"));
      return dto;
    }
    if (StringUtils.isStringNullOrEmpty(dto.getCabUnitName())) {
      dto.setResultImport(I18n.getLanguage("crCabUsers.err.cabUnitName"));
      return dto;
    }
    if (StringUtils.isStringNullOrEmpty(dto.getImpactSegmentId())) {
      dto.setResultImport(I18n.getLanguage("crCabUsers.err.segmentName.exist"));
      return dto;
    }
    if (StringUtils.isStringNullOrEmpty(dto.getExecuteUnitId())) {
      dto.setResultImport(I18n.getLanguage("crCabUsers.err.executeUnitName.exist"));
      return dto;
    }
    if (StringUtils.isStringNullOrEmpty(dto.getCabUnitId())) {
      dto.setResultImport(I18n.getLanguage("crCabUsers.err.cabUnitName.exist"));
      return dto;
    }
    if (StringUtils.isStringNullOrEmpty(dto.getUserID()) && StringUtils
        .isNotNullOrEmpty(dto.getUserFullName())) {
      dto.setResultImport(I18n.getLanguage("crCabUsers.err.userFullName.exist"));
      return dto;
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getCreationUnitName()) && StringUtils
        .isStringNullOrEmpty(dto.getCreationUnitId())) {
      dto.setResultImport(I18n.getLanguage("crCabUsers.err.creationUnitName.exist"));
      return dto;
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getUserID())) {
      String validateDependent = validateDependent(dto, 0L);
      if (StringUtils.isNotNullOrEmpty(validateDependent)) {
        dto.setResultImport(validateDependent);
        return dto;
      }
    }
    if (StringUtils.isStringNullOrEmpty(dto.getUserFullName())) {
      String validateDependent = validateDependent(dto, 1L);
      if (StringUtils.isNotNullOrEmpty(validateDependent)) {
        dto.setResultImport(validateDependent);
        return dto;
      }
    }
    return dto;
  }


  private String validateDependent(CrCabUsersExportDTO dto, Long key) {
    ObjectSearchDto objectSearchDto = new ObjectSearchDto();
    objectSearchDto.setModuleName("USERS");
    objectSearchDto.setParent(dto.getCabUnitId().toString());
    objectSearchDto.setIsHasChildren(0L);
    objectSearchDto.setRownum(0L);
    List<DataItemDTO> userFullNameCBB = commonRepository
        .getListCombobox(objectSearchDto);
    if (key.equals(1L)) {
      if (userFullNameCBB.isEmpty() || userFullNameCBB.size() == 0) {
        return I18n.getLanguage("crCabUsers.err.userFullNameCBB.exist");
      } else {
        return null;
      }
    }
    for (DataItemDTO item : userFullNameCBB) {
      if (item.getItemId().equals(dto.getUserID().toString())) {
        return null;
      }
    }
    return I18n.getLanguage("crCabUsers.err.validateDependent");
  }


  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj0 = lstHeader.get(0);
    int count = 0;
    for (Object data : obj0) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 6) {
      return false;
    }
    if (obj0 == null) {
      return false;
    }
    if (obj0[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("crCabUsers.stt")).equalsIgnoreCase(obj0[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("crCabUsers.segmentName") + "(*)")
        .equalsIgnoreCase(obj0[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("crCabUsers.executeUnitName") + "(*)")
        .equalsIgnoreCase(obj0[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("crCabUsers.cabUnitName") + "(*)")
        .equalsIgnoreCase(obj0[3].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("crCabUsers.userFullName")
        .equalsIgnoreCase(obj0[4].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("crCabUsers.creationUnitName")
        .equalsIgnoreCase(obj0[5].toString().trim())) {
      return false;
    }

    return true;
  }

  private File handleFileExport(List<CrCabUsersExportDTO> listExport,
      String[] columnExport, String date, String code) throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName;
    String title;
    String fileNameOut;
    int startRow = 7;
    if (Constants.RESULT_IMPORT.equals(code)) {
      sheetName = I18n.getLanguage("crCabUsers.import.sheetname");
      title = I18n.getLanguage("crCabUsers.import.title");
      fileNameOut = I18n.getLanguage("crCabUsers.import.fileNameOutResult");
      ConfigHeaderExport columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0,
          new String[]{},
          null,
          "STRING");
      lstHeaderSheet1.add(columnSheet);
    } else {
      sheetName = I18n.getLanguage("crCabUsers.export.sheetname");
      title = I18n.getLanguage("crCabUsers.export.title");
      fileNameOut = I18n.getLanguage("crCabUsers.export.fileNameOut");
    }
    String subTitle;
    if (StringUtils.isNotNullOrEmpty(date)) {
      subTitle = I18n
          .getLanguage("crCabUsers.export.eportDate", date);
    } else {
      subTitle = "";
    }
    ConfigFileExport configFileExport = new ConfigFileExport(
        listExport,
        sheetName,
        title,
        subTitle,
        startRow,
        3,
        6,
        true,
        "language.crCabUsers",
        lstHeaderSheet1,
        fieldSplit,
        "",
        I18n.getLanguage("common.export.firstLeftHeader"),
        I18n.getLanguage("common.export.secondLeftHeader"),
        I18n.getLanguage("common.export.firstRightHeader"),
        I18n.getLanguage("common.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(7, 0, 0, 0,
        I18n.getLanguage("crCabUsers.stt"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(fileTemplate, fileNameOut,
        fileExports, rootPath, null);
    return fileExport;
  }

  private List<ConfigHeaderExport> getListHeaderSheet(String[] columnExport) {
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    for (int i = 0; i < columnExport.length; i++) {
      ConfigHeaderExport columnSheet1;
      columnSheet1 = new ConfigHeaderExport(columnExport[i], "LEFT", false,
          0, 0, new String[]{}, null, "STRING");
      lstHeaderSheet1.add(columnSheet1);
    }
    return lstHeaderSheet1;
  }

}
