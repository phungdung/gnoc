package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CfgRoleDataDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.CfgRoleDataRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.io.File;
import java.io.IOException;
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

@Slf4j
@Service
@Transactional
public class CfgRoleDataBusinessImpl implements CfgRoleDataBusiness {

  @Autowired
  private CfgRoleDataRepository cfgRoleDataRepository;

  @Autowired
  private UnitRepository unitRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  CatLocationRepository catLocationRepository;

  @Autowired
  private TicketProvider ticketProvider;

  @Value("${application.temp.folder}")
  private String tempFolder;

  private int maxRecord = 1000;

  @Override
  public Datatable onSearchCfgRoleData(CfgRoleDataDTO dto) {
    Datatable datatable = cfgRoleDataRepository.onSearchCfgRoleData(dto);
    List<CfgRoleDataDTO> list = (List<CfgRoleDataDTO>) datatable.getData();
    List<CfgRoleDataDTO> listFinal = new ArrayList<>();
    for (CfgRoleDataDTO cfgRoleDataDTO : list) {
      if (cfgRoleDataDTO.getAuditUnitId() != null) {
        String array[] = cfgRoleDataDTO.getAuditUnitId().split(",");
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
          UnitDTO unit = unitRepository.findUnitById(Long.parseLong(array[i].trim()));
          if (unit != null) {
            if (!StringUtils.isStringNullOrEmpty(name)) {
              name.append("," + unit.getUnitName() + " (" + unit.getUnitCode() + ")");
            } else {
              name.append(unit.getUnitName() + " (" + unit.getUnitCode() + ")");
            }
          }
        }
        cfgRoleDataDTO.setUnitName(name.toString());
        listFinal.add(cfgRoleDataDTO);
      } else {
        listFinal.add(cfgRoleDataDTO);
      }

    }
    datatable.setData(listFinal);
    return datatable;
  }

  @Override
  public ResultInSideDto insertCfgRoleData(CfgRoleDataDTO dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    dto.setUpdatedUser(userToken.getUserName());
    dto.setUpdatedTime(new Date());
    if (dto.getType() != null && dto.getType() == 1L && StringUtils
        .isNotNullOrEmpty(dto.getUnitId())) {
      String[] arr = dto.getUnitId().split(",");
      if (arr.length > 0) {
        for (int i = 0; i < arr.length; i++) {
          UsersInsideDto objectSearch = new UsersInsideDto();
          objectSearch.setUnitId(Long.valueOf(arr[i]));
          objectSearch.setIsEnable(1L);
          List<UsersInsideDto> lstUser = userRepository.listUserByDTO(objectSearch);
          for (UsersInsideDto users : lstUser) {
            CfgRoleDataDTO cfgRoleDataDTO = dto;
            cfgRoleDataDTO.setUnitId(arr[i]);
            cfgRoleDataDTO.setUsername(users.getUsername());
            ResultInSideDto resultExists = cfgRoleDataRepository
                .checkExisted(users.getUsername(), dto.getSystem().toString(), null);
            if (resultExists.getCheck()) {
              CfgRoleDataDTO dtoExists = (CfgRoleDataDTO) resultExists.getObject();
              if (dtoExists != null) {
                cfgRoleDataDTO.setId(dtoExists.getId());
              }
            }
            resultInSideDto = cfgRoleDataRepository.insertCfgRoleData(dto);
          }
        }
      }
    } else if (dto.getType() != null && dto.getType() == 2L) {
      if (cfgRoleDataRepository.checkExisted(dto.getUsername(), dto.getSystem().toString(), null)
          .getCheck()) {
        resultInSideDto.setKey(RESULT.DUPLICATE);
        resultInSideDto.setMessage(I18n.getValidation("cfgRoleData.null.unique"));
        return resultInSideDto;
      }
      resultInSideDto = cfgRoleDataRepository.insertCfgRoleData(dto);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateCfgRoleData(CfgRoleDataDTO dto) {
    UserToken userToken = ticketProvider.getUserToken();
    dto.setUpdatedUser(userToken.getUserName());
    dto.setUpdatedTime(new Date());
    return cfgRoleDataRepository.updateCfgRoleData(dto);
  }

  @Override
  public CfgRoleDataDTO findCfgRoleDataById(Long id) {
    return cfgRoleDataRepository.findCfgRoleDataById(id);
  }

  @Override
  public String deleteCfgRoleData(Long id) {
    return cfgRoleDataRepository.deleteCfgRoleData(id);
  }

  @Override
  public UsersEntity getUserByUserName(String username) {
    return userRepository.getUserByUserName(username);
  }

  @Override
  public File exportData(CfgRoleDataDTO dto) throws Exception {
    log.debug("Request to getListProblemsSearchExport : {}", dto);
    String[] header = new String[]{"username", "systemName", "roleName",
        "unitName", "statusName"};
    List<CfgRoleDataDTO> lstData = cfgRoleDataRepository
        .onSearchExport(dto);
    List<CfgRoleDataDTO> listFinal = new ArrayList<>();
    for (CfgRoleDataDTO cfgRoleDataDTO : lstData) {
      if (cfgRoleDataDTO.getAuditUnitId() != null) {
        String array[] = cfgRoleDataDTO.getAuditUnitId().split(",");
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
          UnitDTO unit = unitRepository.findUnitById(Long.parseLong(array[i]));
          if (!StringUtils.isStringNullOrEmpty(name)) {
            name.append("," + unit.getUnitName() + " (" + unit.getUnitCode() + ")");
          } else {
            name.append(unit.getUnitName() + " (" + unit.getUnitCode() + ")");
          }
        }
        cfgRoleDataDTO.setUnitName(name.toString());
      }
      if (cfgRoleDataDTO.getSystem() != null) {
        if (cfgRoleDataDTO.getSystem() == 0) {
          cfgRoleDataDTO.setSystemName("COMMON");
        } else if (cfgRoleDataDTO.getSystem() == 1) {
          cfgRoleDataDTO.setSystemName("CR");
        } else if (cfgRoleDataDTO.getSystem() == 2) {
          cfgRoleDataDTO.setSystemName("MR");
        } else if (cfgRoleDataDTO.getSystem() == 3) {
          cfgRoleDataDTO.setSystemName("PT");
        } else if (cfgRoleDataDTO.getSystem() == 4) {
          cfgRoleDataDTO.setSystemName("TT");
        } else if (cfgRoleDataDTO.getSystem() == 5) {
          cfgRoleDataDTO.setSystemName("WO");
        } else if (cfgRoleDataDTO.getSystem() == 6) {
          cfgRoleDataDTO.setSystemName("SR");
        }
      }

      if (cfgRoleDataDTO.getRole() != null) {
        if (cfgRoleDataDTO.getRole() == 0) {
          cfgRoleDataDTO.setRoleName("Admin");
        } else if (cfgRoleDataDTO.getRole() == 1) {
          cfgRoleDataDTO.setRoleName("FO");
        } else if (cfgRoleDataDTO.getRole() == 2) {
          cfgRoleDataDTO.setRoleName("Audit");
        }
      }

      if (cfgRoleDataDTO.getStatus() != null) {
        if (cfgRoleDataDTO.getStatus() == 0) {
          cfgRoleDataDTO.setStatusName(I18n.getLanguage("woCdTemp.status.0"));
        } else if (cfgRoleDataDTO.getStatus() == 1) {
          cfgRoleDataDTO.setStatusName(I18n.getLanguage("woCdTemp.status.1"));
        }
      }
      listFinal.add(cfgRoleDataDTO);
    }
    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet(header);
    return exportFileEx(listFinal, lstHeaderSheet, "");

  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    List<CfgRoleDataDTO> listDto;
    List<CfgRoleDataDTO> listImportDto;
    Map<String, String> mapImportDTO;

    String[] header = new String[]{"typeName", "username", "unitId", "systemName", "roleName",
        "locationId", "auditUnitId", "statusName"};
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
            0, 8, 1000);
        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }

        List<Object[]> lstData = CommonImport.getDataFromExcelFileNew(fileImp, 0, 6,
            0, 8, 1000);
        if (lstData.size() > maxRecord) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(maxRecord));
          return resultInSideDto;
        }

        listDto = new ArrayList<>();
        listImportDto = new ArrayList<>();
        mapImportDTO = new HashMap<>();

        if (!lstData.isEmpty()) {
          int index = 0;
          int value = 1;
          for (Object[] obj : lstData) {
            CfgRoleDataDTO importDTO = new CfgRoleDataDTO();
            if (obj[1] != null) {
              List<ItemDataCRInside> list = itemData("CONFIG_TYPE");
              HashMap<String, Long> map = new HashMap<>();
              for (ItemDataCRInside itemDataCRInside : list) {
                map.put(itemDataCRInside.getDisplayStr(), itemDataCRInside.getValueStr());
              }
              importDTO.setType(map.get(obj[1].toString().trim()));
              importDTO.setTypeName(obj[1].toString().trim());
            }
            if (obj[2] != null) {
              importDTO.setUsername(obj[2].toString().trim().toLowerCase());
            }
            if (obj[3] != null) {
              importDTO.setUnitId(obj[3].toString().trim());
            }
            if (obj[4] != null) {
              List<ItemDataCRInside> list = itemData("SYSTEM");
              HashMap<String, Long> map = new HashMap<>();
              for (ItemDataCRInside itemDataCRInside : list) {
                map.put(itemDataCRInside.getDisplayStr(), itemDataCRInside.getValueStr());
              }
              importDTO.setSystem(map.get(obj[4].toString().trim()));
              importDTO.setSystemName(obj[4].toString().trim());
            }
            if (obj[5] != null) {
              List<ItemDataCRInside> list = itemData("ROLE");
              HashMap<String, Long> map = new HashMap<>();
              for (ItemDataCRInside itemDataCRInside : list) {
                map.put(itemDataCRInside.getDisplayStr(), itemDataCRInside.getValueStr());
              }
              importDTO.setRole(map.get(obj[5].toString().trim()));
              importDTO.setRoleName(obj[5].toString().trim());
            }
            if (obj[6] != null) {
              importDTO.setLocationId(obj[6].toString().trim());
            }
            if (obj[7] != null) {
              importDTO.setAuditUnitId(obj[7].toString().trim());
            }
            if (obj[8] != null) {
              List<ItemDataCRInside> list = itemData("STATUS");
              HashMap<String, Long> map = new HashMap<>();
              for (ItemDataCRInside itemDataCRInside : list) {
                map.put(itemDataCRInside.getDisplayStr(), itemDataCRInside.getValueStr());
              }
              importDTO.setStatus(map.get(obj[8].toString().trim()));
              importDTO.setStatusName(obj[8].toString().trim());
            }

            CfgRoleDataDTO tempImportDTO = validateImportInfo(importDTO, mapImportDTO);
            if (tempImportDTO.getResultImport() == null) {
              tempImportDTO
                  .setResultImport(I18n.getLanguage("export.cfgRoleData.grid.resultImport.ok"));
              listImportDto.add(tempImportDTO);
              listDto.add(tempImportDTO);
            } else {
              listImportDto.add(tempImportDTO);
              index++;
            }
            String type = String.valueOf(importDTO.getType());
            String userName = importDTO.getUsername();
            String unit = importDTO.getUnitId();
            String system = String.valueOf(importDTO.getSystem());
            String role = String.valueOf(importDTO.getRole());
            String location = importDTO.getLocationId();
            String auditUnit = importDTO.getAuditUnitId();
            String status = String.valueOf(importDTO.getStatus());

            String key =
                type + "_" + userName + "_" + unit + "_" + system + "_" + role + "_" + location
                    + "_" + auditUnit + "_" + status;
            mapImportDTO.put(key, key);
            value++;
          }
          if (index == 0) {
            if (!listDto.isEmpty()) {
              boolean check = false;
              for (CfgRoleDataDTO dto : listDto) {
                resultInSideDto = insertCfgRoleData(dto);
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

  @Override
  public File getTemplate() throws IOException {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);
    XSSFSheet sheetParam = workBook.createSheet("param");
    XSSFSheet sheetParam5 = workBook
        .createSheet(I18n.getLanguage("export.cfgRoleData.grid.unitName"));
    XSSFSheet sheetParam2 = workBook
        .createSheet(I18n.getLanguage("export.cfgRoleData.grid.locationName"));
    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetMain);
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("export.cfgRoleData.grid.type"),
        I18n.getLanguage("export.cfgRoleData.grid.username"),
        I18n.getLanguage("export.cfgRoleData.grid.unitName"),
        I18n.getLanguage("export.cfgRoleData.grid.systemName"),
        I18n.getLanguage("export.cfgRoleData.grid.roleName"),
        I18n.getLanguage("export.cfgRoleData.grid.locationName"),
        I18n.getLanguage("export.cfgRoleData.grid.auditUnitId"),
        I18n.getLanguage("export.cfgRoleData.grid.statusName")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("export.cfgRoleData.grid.type"),
        I18n.getLanguage("export.cfgRoleData.grid.username"),
        I18n.getLanguage("export.cfgRoleData.grid.unitName"),
        I18n.getLanguage("export.cfgRoleData.grid.systemName"),
        I18n.getLanguage("export.cfgRoleData.grid.roleName"),
        I18n.getLanguage("export.cfgRoleData.grid.locationName"),
        I18n.getLanguage("export.cfgRoleData.grid.auditUnitId"),
        I18n.getLanguage("export.cfgRoleData.grid.statusName")
    };
//    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int sttColumn = listHeader.indexOf(I18n.getLanguage("common.STT"));
    int usernameColumn = listHeader.indexOf(I18n.getLanguage("export.cfgRoleData.grid.username"));
    int systemNameColumn = listHeader
        .indexOf(I18n.getLanguage("export.cfgRoleData.grid.systemName"));
    int roleNameColumn = listHeader.indexOf(I18n.getLanguage("export.cfgRoleData.grid.roleName"));
    int unitNameColumn = listHeader.indexOf(I18n.getLanguage("export.cfgRoleData.grid.unitName"));
    int statusNameColumn = listHeader
        .indexOf(I18n.getLanguage("export.cfgRoleData.grid.statusName"));
    int typeColumn = listHeader
        .indexOf(I18n.getLanguage("export.cfgRoleData.grid.type"));
    int locationColumn = listHeader
        .indexOf(I18n.getLanguage("export.cfgRoleData.grid.locationName"));
    int auditUnitColumn = listHeader
        .indexOf(I18n.getLanguage("export.cfgRoleData.grid.auditUnitId"));

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
    mainCellTitle.setCellValue(I18n.getLanguage("export.cfgRoleData.grid.title"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(3, 3, 1, 7));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao header
    Row headerRow = sheetMain.createRow(5);
    Row headerUnit = sheetParam5.createRow(0);
    Row headerLocation = sheetParam2.createRow(0);
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

    Cell headerCellUnitId = headerUnit.createCell(0);
    Cell headerCellUnit = headerUnit.createCell(1);
    XSSFRichTextString unitId = new XSSFRichTextString(
        I18n.getLanguage("export.cfgRoleData.grid.unitIdC"));
    XSSFRichTextString unit = new XSSFRichTextString(
        I18n.getLanguage("export.cfgRoleData.grid.unitName"));
    headerCellUnit.setCellValue(unit);
    headerCellUnit.setCellStyle(styles.get("header"));
    headerCellUnitId.setCellValue(unitId);
    headerCellUnitId.setCellStyle(styles.get("header"));
    sheetParam5.setColumnWidth(0, 3000);
    sheetParam5.setColumnWidth(1, 20000);

    Cell headerCellLocationId = headerLocation.createCell(0);
    Cell headerCellLocationName = headerLocation.createCell(1);
    XSSFRichTextString locationId = new XSSFRichTextString(
        I18n.getLanguage("export.cfgRoleData.grid.locationIdC"));
    XSSFRichTextString locationName = new XSSFRichTextString(
        I18n.getLanguage("export.cfgRoleData.grid.locationName"));
    headerCellLocationId.setCellValue(locationId);
    headerCellLocationId.setCellStyle(styles.get("header"));
    headerCellLocationName.setCellValue(locationName);
    headerCellLocationName.setCellStyle(styles.get("header"));
    sheetParam2.setColumnWidth(0, 4000);
    sheetParam2.setColumnWidth(1, 10000);

    // tạo dropdown system
    int row = 5;
    List<ItemDataCRInside> listSystem = itemData("SYSTEM");
    for (ItemDataCRInside dto : listSystem) {
      ewu.createCell(sheetParam, 2, row++, dto.getDisplayStr(), styles.get("cell"));
    }
    Name systemName = workBook.createName();
    systemName.setNameName("systemName");
    systemName.setRefersToFormula("param!$C$3:$C$" + row);
    XSSFDataValidationConstraint marketCodeStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "systemName");
    CellRangeAddressList nationNameCreate = new CellRangeAddressList(6, 65000,
        systemNameColumn,
        systemNameColumn);
    XSSFDataValidation dataValidationnAtionName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            marketCodeStrConstraint, nationNameCreate);
    dataValidationnAtionName.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationnAtionName);

    // tạo dropdown quyen
    row = 5;
    List<ItemDataCRInside> listRole = itemData("ROLE");
    for (ItemDataCRInside dto : listRole) {
      ewu.createCell(sheetParam, 3, row++, dto.getDisplayStr(), styles.get("cell"));
    }
    Name roleName = workBook.createName();
    roleName.setNameName("roleName");
    roleName.setRefersToFormula("param!$D$4:$D$" + row);
    XSSFDataValidationConstraint marketCodeStrConstraint1 = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "roleName");
    CellRangeAddressList nationNameCreate1 = new CellRangeAddressList(6, 65000,
        roleNameColumn,
        roleNameColumn);
    XSSFDataValidation dataValidationnAtionName1 = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            marketCodeStrConstraint1, nationNameCreate1);
    dataValidationnAtionName1.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationnAtionName1);

    // tạo dropdown trang thai
    row = 5;
    List<ItemDataCRInside> listStatus = itemData("STATUS");
    for (ItemDataCRInside dto : listStatus) {
      ewu.createCell(sheetParam, 5, row++, dto.getDisplayStr(), styles.get("cell"));
    }
    Name statusName = workBook.createName();
    statusName.setNameName("statusName");
    statusName.setRefersToFormula("param!$F$6:$F$" + row);
    XSSFDataValidationConstraint marketCodeStrConstraint2 = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "statusName");
    CellRangeAddressList nationNameCreate2 = new CellRangeAddressList(6, 65000,
        statusNameColumn,
        statusNameColumn);
    XSSFDataValidation dataValidationnAtionName2 = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            marketCodeStrConstraint2, nationNameCreate2);
    dataValidationnAtionName2.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationnAtionName2);

    // tạo dropdown Loai cau hinh
    row = 5;
    List<ItemDataCRInside> listConfigType = itemData("CONFIG_TYPE");
    for (ItemDataCRInside dto : listConfigType) {
      ewu.createCell(sheetParam, 1, row++, dto.getDisplayStr(), styles.get("cell"));
    }
    Name configTypeName = workBook.createName();
    configTypeName.setNameName("configTypeName");
    configTypeName.setRefersToFormula("param!$B$2:$B$" + row);
    XSSFDataValidationConstraint marketCodeStrConstraint3 = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "configTypeName");
    CellRangeAddressList nationNameCreate3 = new CellRangeAddressList(6, 65000,
        typeColumn,
        typeColumn);
    XSSFDataValidation dataValidationnAtionName3 = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            marketCodeStrConstraint3, nationNameCreate3);
    dataValidationnAtionName3.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationnAtionName3);

    // tạo set unit
    row = 1;
    List<UnitDTO> unitNameList = unitRepository.getListUnit(null);
    for (UnitDTO dto : unitNameList) {
      ewu.createCell(sheetParam5, 0, row, dto.getUnitId().toString(), styles.get("cell"));
      ewu.createCell(sheetParam5, 1, row++, dto.getUnitName(), styles.get("cell"));
    }

    // tạo set location
    row = 1;
    List<ItemDataCRInside> listCountry = catLocationRepository
        .getListLocationByLevelCBB(null, 1L, null);
    for (ItemDataCRInside dto : listCountry) {
      ewu.createCell(sheetParam2, 0, row, dto.getValueStr().toString(), styles.get("cell"));
      ewu.createCell(sheetParam2, 1, row++, dto.getDisplayStr(), styles.get("cell"));
    }

    // set độ rông các cột
    sheetMain.setColumnWidth(sttColumn, 2000);
    sheetMain.setColumnWidth(usernameColumn, 6000);
    sheetMain.setColumnWidth(systemNameColumn, 6000);
    sheetMain.setColumnWidth(roleNameColumn, 6000);
    sheetMain.setColumnWidth(unitNameColumn, 6000);
    sheetMain.setColumnWidth(statusNameColumn, 6000);
    sheetMain.setColumnWidth(typeColumn, 6000);
    sheetMain.setColumnWidth(locationColumn, 6000);
    sheetMain.setColumnWidth(auditUnitColumn, 6000);

    workBook.setSheetHidden(1, true);
    workBook.setSheetName(0, I18n.getLanguage("export.cfgRoleData.grid.title"));

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_CFG_ROLE_DATA" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  private List<ConfigHeaderExport> renderHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }


  private File exportFileEx(List<CfgRoleDataDTO> lstData,
      List<ConfigHeaderExport> lstHeaderSheet, String code) throws Exception {
    String title = I18n.getLanguage("export.cfgRoleData.grid.title");
    String sheetName = title;
    String fileNameOut;
    ConfigFileExport configfileExport = null;
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet = null;
    ConfigHeaderExport columnSheet1 = null;
    if (Constants.RESULT_IMPORT.equals(code)) {
      fileNameOut = "CFG_ROLE_DATA_RESULT_IMPORT";
      columnSheet1 = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet.add(columnSheet1);
    } else {
      fileNameOut = "CFG_ROLE_DATA_RESULT_EXPORT";
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
        , "language.export.cfgRoleData.grid"
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

  private List<ItemDataCRInside> itemData(String code) {
    List<ItemDataCRInside> list = new ArrayList<>();
    if ("SYSTEM".equals(code)) {
      for (int i = 0; i < 7; i++) {
        ItemDataCRInside itemDataCRInside = new ItemDataCRInside();
        if (i == 0) {
          continue;
        } else if (i == 1) {
          itemDataCRInside.setValueStr(Long.parseLong(String.valueOf(i)));
          itemDataCRInside.setDisplayStr("CR");
        } else if (i == 2) {
          continue;
        } else if (i == 3) {
          itemDataCRInside.setValueStr(Long.parseLong(String.valueOf(i)));
          itemDataCRInside.setDisplayStr("PT");
        } else if (i == 4) {
          itemDataCRInside.setValueStr(Long.parseLong(String.valueOf(i)));
          itemDataCRInside.setDisplayStr("TT");
        } else if (i == 5) {
          itemDataCRInside.setValueStr(Long.parseLong(String.valueOf(i)));
          itemDataCRInside.setDisplayStr("WO");
        } else if (i == 6) {
          itemDataCRInside.setValueStr(Long.parseLong(String.valueOf(i)));
          itemDataCRInside.setDisplayStr("SR");
        }
        list.add(itemDataCRInside);
      }
    } else if ("ROLE".equals(code)) {
      for (int i = 0; i < 3; i++) {
        ItemDataCRInside itemDataCRInside = new ItemDataCRInside();
        if (i == 0) {
          itemDataCRInside.setValueStr(Long.parseLong(String.valueOf(i)));
          itemDataCRInside.setDisplayStr("Admin");
        } else if (i == 1) {
          itemDataCRInside.setValueStr(Long.parseLong(String.valueOf(i)));
          itemDataCRInside.setDisplayStr("FO");
        } else if (i == 2) {
          itemDataCRInside.setValueStr(Long.parseLong(String.valueOf(i)));
          itemDataCRInside.setDisplayStr("Audit");
        }
        list.add(itemDataCRInside);
      }
    } else if ("STATUS".equals(code)) {
      for (int i = 0; i < 2; i++) {
        ItemDataCRInside itemDataCRInside = new ItemDataCRInside();
        if (i == 0) {
          itemDataCRInside.setValueStr(Long.parseLong(String.valueOf(i)));
          itemDataCRInside.setDisplayStr(I18n.getLanguage("employee.isEnable.0"));
        } else if (i == 1) {
          itemDataCRInside.setValueStr(Long.parseLong(String.valueOf(i)));
          itemDataCRInside.setDisplayStr(I18n.getLanguage("employee.isEnable.1"));
        }
        list.add(itemDataCRInside);
      }
    } else if ("CONFIG_TYPE".equals(code)) {
      for (int i = 0; i < 2; i++) {
        ItemDataCRInside itemDataCRInside = new ItemDataCRInside();
        if (i == 0) {
          itemDataCRInside.setValueStr(Long.parseLong(String.valueOf(i + 1)));
          itemDataCRInside.setDisplayStr(I18n.getLanguage("export.cfgRoleData.grid.unitName"));
        } else if (i == 1) {
          itemDataCRInside.setValueStr(Long.parseLong(String.valueOf(i + 1)));
          itemDataCRInside.setDisplayStr(I18n.getLanguage("export.cfgRoleData.grid.username"));
        }
        list.add(itemDataCRInside);
      }
    }
    return list;
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 9) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("employee.stt"))
        .equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("export.cfgRoleData.grid.type") + "(*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("export.cfgRoleData.grid.username") + "(*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("export.cfgRoleData.grid.unitName") + "(*)")
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("export.cfgRoleData.grid.systemName") + "(*)")
        .equalsIgnoreCase(obj[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("export.cfgRoleData.grid.roleName") + "(*)")
        .equalsIgnoreCase(obj[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("export.cfgRoleData.grid.locationName") + "(*)")
        .equalsIgnoreCase(obj[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("export.cfgRoleData.grid.auditUnitId") + "(*)")
        .equalsIgnoreCase(obj[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("export.cfgRoleData.grid.statusName") + "(*)")
        .equalsIgnoreCase(obj[8].toString().trim())) {
      return false;
    }
    return true;
  }

  private CfgRoleDataDTO validateImportInfo(CfgRoleDataDTO importDTO,
      Map<String, String> mapImportDTO) {
    String resultImport = "";
    //Check null
    if (StringUtils.isStringNullOrEmpty(importDTO.getType())) {
      resultImport = resultImport.concat(
          I18n.getLanguage("export.cfgRoleData.grid.type") + " " + I18n
              .getLanguage("cfgRoleData.err.empty") + ";");
    }
    if (importDTO.getType() != null && importDTO.getType() == 2L && StringUtils
        .isStringNullOrEmpty(importDTO.getUsername())) {
      resultImport = resultImport.concat(
          I18n.getLanguage("export.cfgRoleData.grid.username") + " " + I18n
              .getLanguage("cfgRoleData.err.empty") + ";");
    }
    if (importDTO.getType() != null && importDTO.getType() == 1L && StringUtils
        .isStringNullOrEmpty(importDTO.getUnitId())) {
      resultImport = resultImport.concat(
          I18n.getLanguage("export.cfgRoleData.grid.unitId") + " " + I18n
              .getLanguage("cfgRoleData.err.empty") + ";");
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getSystem())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("export.cfgRoleData.grid.system") + " " + I18n
              .getLanguage("cfgRoleData.err.empty") + ";");
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getRole())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("export.cfgRoleData.grid.role") + " " + I18n
              .getLanguage("cfgRoleData.err.empty") + ";");
    }
    if (!StringUtils.isStringNullOrEmpty(importDTO.getLocationId()) && importDTO.getRole() == 1L) {
      if (StringUtils.isStringNullOrEmpty(importDTO.getAuditUnitId())) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("export.cfgRoleData.grid.locationId") + " " + I18n
                .getLanguage("cfgRoleData.err.empty") + ";");
      }
    } else if (!StringUtils.isStringNullOrEmpty(importDTO.getRole()) && importDTO.getRole() != 1L) {
      if (!StringUtils.isStringNullOrEmpty(importDTO.getAuditUnitId())) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgRoleData.roleFo"));
      }
    }
    if (!StringUtils.isStringNullOrEmpty(importDTO.getRole()) && importDTO.getRole() == 2L) {
      if (StringUtils.isStringNullOrEmpty(importDTO.getAuditUnitId())) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("export.cfgRoleData.grid.auditUnitId") + " " + I18n
                .getLanguage("cfgRoleData.err.empty") + ";");
      }
    } else if (!StringUtils.isStringNullOrEmpty(importDTO.getRole()) && importDTO.getRole() != 2L) {
      if (!StringUtils.isStringNullOrEmpty(importDTO.getAuditUnitId())) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("cfgRoleData.roleAdminAndFo"));
      }
    }

    if (StringUtils.isStringNullOrEmpty(importDTO.getStatus())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("export.cfgRoleData.grid.status") + " " + I18n
              .getLanguage("cfgRoleData.err.empty") + ";");
    }

    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      importDTO.setResultImport(resultImport);
      return importDTO;
    }

    //Check value
    if (importDTO.getUsername() != null) {
      if (importDTO.getUsername().length() > 50) {
        importDTO.setResultImport(I18n.getLanguage("employee.err.username.valid") + ";");
      } else {
        UsersEntity usersEntity = userRepository.getUserByUserName(importDTO.getUsername());
        if (usersEntity == null) {
          importDTO.setResultImport(
              I18n.getLanguage("export.cfgRoleData.grid.username") + " " + I18n
                  .getLanguage("export.cfgRoleData.grid.notExist") + ";");
        }
      }
    }

    if (importDTO.getUnitId() != null) {
      String[] array = importDTO.getUnitId().split(",");
      Boolean check = true;
      if (array.length > 0) {
        for (int i = 0; i < array.length; i++) {
          if (StringUtils.isInteger(array[i].trim())) {
            UnitDTO unit = unitRepository.findUnitById(Long.parseLong(array[i].trim()));
            if (unit.getUnitName() == null) {
              check = false;
              break;
            }
          } else {
            check = false;
          }
        }
      }
      if (check == false) {
        importDTO.setResultImport(I18n.getLanguage("employee.unit.isExits"));
      }
    }

    if (importDTO.getLocationId() != null) {
      List<ItemDataCRInside> listCountry = catLocationRepository
          .getListLocationByLevelCBB(null, 1L, null);
      Map<Long, Long> map = new HashMap<>();
      for (ItemDataCRInside dto : listCountry) {
        if (!map.containsKey(dto.getValueStr())) {
          map.put(dto.getValueStr(), dto.getValueStr());
        }
      }
      String[] array = importDTO.getLocationId().split(",");
      Boolean check = true;
      if (array.length > 0) {
        for (int i = 0; i < array.length; i++) {
          if (StringUtils.isInteger(array[i].trim())) {
            if (map.containsKey(Long.valueOf(array[i].trim()))) {
              check = true;
            }
          } else {
            check = false;
            break;
          }
        }
      }
      if (check == false) {
        importDTO.setResultImport(I18n.getLanguage("cfgRoleData.location.isExits"));
      }
    }

    if (importDTO.getAuditUnitId() != null) {
      String[] array = importDTO.getAuditUnitId().split(",");
      Boolean check = true;
      if (array.length > 0) {
        for (int i = 0; i < array.length; i++) {
          if (StringUtils.isInteger(array[i].trim())) {
            UnitDTO unit = unitRepository.findUnitById(Long.parseLong(array[i].trim()));
            if (unit.getUnitName() == null) {
              check = false;
              break;
            }
          } else {
            check = false;
          }
        }
      }
      if (check == false) {
        importDTO.setResultImport(I18n.getLanguage("cfgRoleData.auditUnit.isExits"));
      }
    }
//    if (!StringUtils.isStringNullOrEmpty(importDTO.getUsername()) && !StringUtils
//        .isStringNullOrEmpty(importDTO.getAuditUnitId()) && importDTO.getRole() != null
//        && importDTO.getSystem() != null && importDTO.getStatus() != null) {
//      CfgRoleDataDTO cfgRoleDataDTO = cfgRoleDataRepository.checkCreateExit(importDTO);
//      if (cfgRoleDataDTO != null) {
//        importDTO.setResultImport(I18n.getLanguage("cfgRoleData.err.dup-code") + ";");
//        return importDTO;
//      }
//    }
    String validateDuplicate = validateDuplicateImport(importDTO, mapImportDTO);
    if (StringUtils.isNotNullOrEmpty(validateDuplicate)) {
      importDTO.setResultImport(validateDuplicate);
      return importDTO;
    }

    return importDTO;
  }

  private String checkResultImport(String resultImport) {
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      resultImport = resultImport.concat("; \n");
    }
    return resultImport;
  }

  private String validateDuplicateImport(CfgRoleDataDTO importDTO,
      Map<String, String> mapImportDTO) {
    String type = String.valueOf(importDTO.getType());
    String userName = importDTO.getUsername();
    String unit = importDTO.getUnitId();
    String system = String.valueOf(importDTO.getSystem());
    String role = String.valueOf(importDTO.getRole());
    String location = importDTO.getLocationId();
    String auditUnit = importDTO.getAuditUnitId();
    String status = String.valueOf(importDTO.getStatus());

    String key =
        type + "_" + userName + "_" + unit + "_" + system + "_" + role + "_" + location + "_"
            + auditUnit + "_" + status;
    String value = mapImportDTO.get(key);
    if (StringUtils.isNotNullOrEmpty(value)) {
      return I18n.getLanguage("employee.err.dup-code-in-file").replaceAll("0", value);
    }
    return null;
  }
}
