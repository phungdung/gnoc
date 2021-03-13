package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
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
import com.viettel.gnoc.wo.dto.CfgFtOnTimeDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupUnitDTO;
import com.viettel.gnoc.wo.repository.CfgFtOnTimeRepository;
import com.viettel.gnoc.wo.repository.WoCdGroupUnitRepository;
import java.awt.Color;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
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
public class CfgFtOnTimeBusinessImpl implements CfgFtOnTimeBusiness {

  @Autowired
  protected CfgFtOnTimeRepository cfgFtOnTimeRepository;

  @Autowired
  protected CatItemRepository catItemRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  WoCdGroupBusiness woCdGroupBusiness;

  @Autowired
  UserRepository userRepository;

  @Autowired
  WoCdGroupUnitRepository woCdGroupUnitRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Override
  public List<UsersInsideDto> getListUserByCdGroupCBB(String cdGroupId) {
    if (StringUtils.isNotNullOrEmpty(cdGroupId)) {
      return cfgFtOnTimeRepository.getListUserByCdGroup(cdGroupId);
    }
    return new ArrayList<>();
  }

  @Override
  public List<CatItemDTO> getLstBusinessCBB() {
    List<CatItemDTO> catItemDTOS = catItemRepository
        .getListItemByCategory(Constants.CATEGORY.CFG_FT_ON_TIME_BUSINESS, null);
    if (catItemDTOS != null && !catItemDTOS.isEmpty()) {
      catItemRepository.setLan(catItemDTOS);
    }
    return catItemDTOS;
  }

  @Override
  public Datatable onSearch(CfgFtOnTimeDTO cfgFtOnTimeDTO) {
    return cfgFtOnTimeRepository.onSearch(cfgFtOnTimeDTO);
  }

  @Override
  public File exportSearchData(CfgFtOnTimeDTO cfgFtOnTimeDTO) {
    try {
      String header[] = new String[]{
          "cdGroupCode", "cdGroupName", "userName", "cfgTimeStr",
          "businessName"};
      UserToken userToken = ticketProvider.getUserToken();

      List<CfgFtOnTimeDTO> lstExport = cfgFtOnTimeRepository.getListCfgFtOnTimeDTO(cfgFtOnTimeDTO);
      return exportFileEx(lstExport, renderHeaderSheet(header),
          I18n.getLanguage("cfgFtOnTime.list"),
          I18n.getLanguage("cfgFtOnTime.list.export"), "CfgFtOnTimeExport",
          "language.cfgFtOnTime.list", "cfgFtOnTime.list.stt");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public ResultInSideDto insertOrUpdate(CfgFtOnTimeDTO cfgFtOnTimeDTO) {
    return cfgFtOnTimeRepository.insertOrUpdate(cfgFtOnTimeDTO);
  }

  @Override
  public CfgFtOnTimeDTO findById(Long id) {
    return cfgFtOnTimeRepository.findById(id);
  }

  @Override
  public ResultInSideDto importData(MultipartFile upLoadFile) throws Exception {
//    List<MapProvinceCdDTO> listMapProviceCd = new ArrayList<>();
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    List<CfgFtOnTimeDTO> lstCfgFtOnTimeDTO = new ArrayList<>();
    boolean hasErr = false;
    try {
      if (upLoadFile == null) {
        resultInSideDTO.setKey(Constants.RESULT.FILE_IS_NULL);
        return resultInSideDTO;
      } else {
        String filePath = FileUtils
            .saveTempFile(upLoadFile.getOriginalFilename(), upLoadFile.getBytes(),
                tempFolder);

        File fileImport = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImport,
            0,//sheet
            4,//begin row
            0,//from column
            4,//to column
            1000);

        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          return new ResultInSideDto(null, Constants.RESULT.ERROR,
              I18n.getLanguage("common.import.errorTemplate"));
        }

        // Lay du lieu import
        List<Object[]> lstData = CommonImport.getDataFromExcelFile(
            fileImport,
            0,//sheet
            5,//begin row
            1,//from column
            4,//to column
            1000
        );

        if (lstData != null && lstData.size() > 2500) {
          return new ResultInSideDto(null, RESULT.DATA_OVER,
              I18n.getLanguage("common.importing.maxrow"));
        }

        if (lstData != null && lstData.size() > 0) {
          WoCdGroupInsideDTO cdTmp = new WoCdGroupInsideDTO();
          Map<String, Long> mapCdGroup = new HashMap<>();
          Map<String, Long> mapBusiness = new HashMap<>();
          Map<String, String> mapDataImport = new HashMap<>();
          cdTmp.setGroupTypeId(4L);
          try {
            List<WoCdGroupInsideDTO> lstCd = woCdGroupBusiness
                .getListWoCdGroupActive(cdTmp, 0, Integer.MAX_VALUE, "", "woGroupName");
            for (WoCdGroupInsideDTO o : lstCd) {
              mapCdGroup.put(o.getWoGroupCode(), o.getWoGroupId());
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }

          List<CatItemDTO> lstBusiness = getLstBusinessCBB();
          for (CatItemDTO i : lstBusiness) {
            mapBusiness.put(i.getItemName(), i.getItemId());
          }

          int row = 0;
          for (Object[] obj : lstData) {
            CfgFtOnTimeDTO cfgFtOnTimeDTO = new CfgFtOnTimeDTO();
            StringBuilder errStr = new StringBuilder();
            //ma nhom dieu phoi
            if (!StringUtils.isStringNullOrEmpty(obj[0])) {
              cfgFtOnTimeDTO.setCdGroupCode(obj[0].toString().trim());
              Long cdId = mapCdGroup.get(obj[0].toString().trim());
              if (cdId != null) {
                cfgFtOnTimeDTO.setCdId(cdId == null ? null : cdId.toString());
              } else {
                errStr.append(I18n.getLanguage("woCdTemp.list.woGroupCode"))
                    .append(I18n.getLanguage("common.invalid")).append(";");
                hasErr = true;
              }
            } else {
              errStr.append(I18n.getLanguage("woCdTemp.list.woGroupCode"))
                  .append(I18n.getLanguage("common.notnull")).append(";");
              hasErr = true;
            }

            //nhan vien
            if (!StringUtils.isStringNullOrEmpty(obj[1])) {
              String us = obj[1].toString().trim();
              UsersEntity user = userRepository.getUserByUserName(us.trim());
              cfgFtOnTimeDTO.setUserName(us);
              if (user == null || user.getUsername() == null) {
                errStr.append(I18n.getLanguage("user.invalid")).append(";");
                hasErr = true;
              } else {
                cfgFtOnTimeDTO
                    .setUserId(user.getUserId() == null ? null : user.getUserId().toString());
              }
            } else {
              errStr.append(I18n.getLanguage("woCdTemp.list.userName"))
                  .append(I18n.getLanguage("common.notnull")).append(";");
              hasErr = true;
            }

            //start time
            Date st = null;
            if (!StringUtils.isStringNullOrEmpty(obj[2])) {
              if (obj[2].toString().trim().length() == 10) {
                try {
                  st = DateTimeUtils.convertStringToDate1(obj[2].toString().trim());
                  cfgFtOnTimeDTO.setCfgTime(st);
                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                  errStr.append(I18n.getLanguage("cfgFtOnTime.cfgTime.invalid")).append(";");
                  hasErr = true;
                }
              } else {
                errStr.append(I18n.getLanguage("cfgFtOnTime.cfgTime.invalid")).append(";");
                hasErr = true;
              }
              cfgFtOnTimeDTO.setCfgTimeStr(obj[2].toString().trim());
            } else {
              errStr.append(I18n.getLanguage("cfgFtOnTime.list.cfgTimeStr")).append(" ")
                  .append(I18n.getLanguage("common.notnull")).append(";");
              hasErr = true;
            }

            //nghiep vu
            if (!StringUtils.isStringNullOrEmpty(obj[3])) {
              if (mapBusiness.containsKey(obj[3].toString().trim())) {
                cfgFtOnTimeDTO
                    .setBusinessId(String.valueOf(mapBusiness.get(obj[3].toString().trim())));
              } else {
                errStr.append(I18n.getLanguage("cfgFtOnTime.list.businessName.invalid"));
                hasErr = true;
              }
              cfgFtOnTimeDTO.setBusinessName(obj[3].toString().trim());
            } else {
              errStr.append(I18n.getLanguage("cfgFtOnTime.list.businessName")).append(" ")
                  .append(I18n.getLanguage("common.notnull")).append(";");
              hasErr = true;
            }

            String keyCheckDataFile = String.valueOf(cfgFtOnTimeDTO.getCdGroupCode()) + String
                .valueOf(cfgFtOnTimeDTO.getCfgTimeStr()) + String
                .valueOf(cfgFtOnTimeDTO.getBusinessName()) + String
                .valueOf(cfgFtOnTimeDTO.getUserName());
            if (mapDataImport.containsKey(keyCheckDataFile)) {
              errStr.setLength(0);
              errStr.append(I18n.getLanguage("common.err.dup-code-in-file",
                  mapDataImport.get(keyCheckDataFile)));
              hasErr = true;

            }
            if (hasErr && !StringUtils.isStringNullOrEmpty(errStr.toString())) {
              hasErr = true;
              cfgFtOnTimeDTO.setResultImport(errStr.toString());
            } else {
              boolean isDupp = cfgFtOnTimeRepository.isDupplicate(cfgFtOnTimeDTO);
              if (isDupp) {
                hasErr = true;
                cfgFtOnTimeDTO.setResultImport(I18n.getValidation("cfgFtOnTime.isExist"));
              } else {
                cfgFtOnTimeDTO.setResultImport(I18n.getLanguage("common.import.validRecord"));
              }
            }
            lstCfgFtOnTimeDTO.add(cfgFtOnTimeDTO);
            mapDataImport.put(keyCheckDataFile, String.valueOf(++row));
          }

          if (!hasErr) {
            for (CfgFtOnTimeDTO item : lstCfgFtOnTimeDTO) {
              insertOrUpdate(item);
              item.setResultImport(I18n.getLanguage("common.insert.import.success"));
            }
          } else {
            resultInSideDTO.setKey(RESULT.ERROR);
            resultInSideDTO.setMessage(I18n.getLanguage("import.common.fail"));
          }
          File file = exportFileImport(lstCfgFtOnTimeDTO);
          resultInSideDTO.setFile(file);
          resultInSideDTO.setFilePath(file.getPath());
        } else {
          return new ResultInSideDto(null, RESULT.NODATA,
              "File " + I18n.getLanguage("common.searh.nodata"));
        }


      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return resultInSideDTO;
  }

  @Override
  public File getTemplate() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workbook.getSheetAt(0);
    XSSFSheet sheetParam = workbook.createSheet("param");

    String[] header = new String[]{I18n.getLanguage("cfgFtOnTime.list.stt"),
        I18n.getLanguage("cfgFtOnTime.list.cdGroupCode"),
        I18n.getLanguage("cfgFtOnTime.list.userName"),
        I18n.getLanguage("cfgFtOnTime.list.cfgTime2"),
        I18n.getLanguage("cfgFtOnTime.list.businessName")};

    String[] headerStar = new String[]{I18n.getLanguage("cfgFtOnTime.list.cdGroupCode"),
        I18n.getLanguage("cfgFtOnTime.list.userName"),
        I18n.getLanguage("cfgFtOnTime.list.cfgTime2"),
        I18n.getLanguage("cfgFtOnTime.list.businessName")};

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int businessNameColumn = listHeader
        .indexOf(I18n.getLanguage("cfgFtOnTime.list.businessName"));

    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tao tieu de
    Font xssFontTitle = workbook.createFont();
    xssFontTitle.setFontName("Times New Roman");
    xssFontTitle.setFontHeightInPoints((short) 22);
    xssFontTitle.setColor(IndexedColors.BLACK.index);
    //Tao font Note
    Font xssFontNote = workbook.createFont();
    xssFontNote.setFontName("Times New Roman");
    xssFontNote.setFontHeightInPoints((short) 11);
    xssFontNote.setColor(IndexedColors.RED.index);

    XSSFCellStyle styleTitle = workbook.createCellStyle();
    styleTitle.setBorderBottom(BorderStyle.THIN);
    styleTitle.setBorderLeft(BorderStyle.THIN);
    styleTitle.setBorderTop(BorderStyle.THIN);
    styleTitle.setBorderRight(BorderStyle.THIN);

    styleTitle.setAlignment(HorizontalAlignment.CENTER);
    styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
    styleTitle
        .setFillForegroundColor(
            new XSSFColor(new Color(155, 194, 230), new DefaultIndexedColorMap()));
    styleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleTitle.setFont(xssFontTitle);

    //Tao tieu de
    sheetOne.addMergedRegion(new CellRangeAddress(0, 0, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(0);
    titleRow.setHeightInPoints(45);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("cfgFtOnTime.import"));
    titleCell.setCellStyle(styleTitle);

    XSSFCellStyle styleNote = workbook.createCellStyle();

    styleNote.setAlignment(HorizontalAlignment.CENTER);
    styleNote.setVerticalAlignment(VerticalAlignment.CENTER);
    styleNote
        .setFillForegroundColor(
            new XSSFColor(new Color(221, 235, 247), new DefaultIndexedColorMap()));
    styleNote.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleNote.setFont(xssFontNote);
    //Tao note
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row noteRow = sheetOne.createRow(2);
    noteRow.setHeightInPoints(18);
    Cell noteCell = noteRow.createCell(0);
    noteCell.setCellValue(I18n.getLanguage("wocdgroup.importfile.template.excel.note"));
    noteCell.setCellStyle(styleNote);

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    Row headerRow = sheetOne.createRow(4);
    headerRow.setHeightInPoints(18);
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
    sheetOne.setColumnWidth(0, 3000);

    int row = 0;

    List<CatItemDTO> lstBusiness = getLstBusinessCBB();
    for (CatItemDTO i : lstBusiness) {
      ewu.createCell(sheetParam, 0, row++, i.getItemName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);

    Name businessName = workbook.createName();
    businessName.setNameName("businessName");
    businessName.setRefersToFormula("param!$A$1:$A$" + row + 1);

    XSSFDataValidationConstraint businessNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "businessName");
    CellRangeAddressList businessNameCreate = new CellRangeAddressList(5, 65000, businessNameColumn,
        businessNameColumn);
    XSSFDataValidation dataValidationBusinessName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            businessNameConstraint, businessNameCreate);
    dataValidationBusinessName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationBusinessName);

    workbook.setSheetName(0, I18n.getLanguage("cfgFtOnTime.import"));
    workbook.setSheetHidden(1, true);

    // danh sach nhom dieu phoi
    workbook.createSheet("Cd group");
    XSSFSheet sheetCd = workbook.getSheet("Cd group");
    row = 1;

    WoCdGroupInsideDTO cdTmp = new WoCdGroupInsideDTO();
//            cdTmp.setIsEnable("1");
    cdTmp.setGroupTypeId(4L);
    try {
      ewu.createCell(sheetCd, 0, 0, I18n.getLanguage("cfgFtOnTime.list.stt"), style.get("header"));
      ewu.createCell(sheetCd, 1, 0, I18n.getLanguage("grid.woCdGroupForm.woGroupName"),
          style.get("header"));
      ewu.createCell(sheetCd, 2, 0, I18n.getLanguage("grid.woCdGroupForm.woGroupCode"),
          style.get("header"));
      sheetCd.setColumnWidth(1, 20000);
      sheetCd.setColumnWidth(2, 15000);
      List<WoCdGroupInsideDTO> lstCd = woCdGroupBusiness
          .getListWoCdGroupActive(cdTmp, 0, Integer.MAX_VALUE, "", "woGroupName");
      for (WoCdGroupInsideDTO o : lstCd) {
        ewu.createCell(sheetCd, 0, row, String.valueOf(row), style.get("cell"));
        ewu.createCell(sheetCd, 1, row, o.getWoGroupName(), style.get("cell"));
        ewu.createCell(sheetCd, 2, row, o.getWoGroupCode(), style.get("cell"));
        row++;
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
//    workbook.setSheetHidden(2, true);

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_FT_ON_TIME" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    return cfgFtOnTimeRepository.delete(id);
  }

  @Override
  public ResultInSideDto getWoCdGroupByUnitCurrentSession() {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    WoCdGroupUnitDTO condition = new WoCdGroupUnitDTO();
    condition.setUnitId(userToken.getDeptId());
    List<WoCdGroupUnitDTO> lstWoCdGroupUnitDTO = woCdGroupUnitRepository
        .onSearchEntity(condition, 0, 10, "", "");
    if (lstWoCdGroupUnitDTO != null && !lstWoCdGroupUnitDTO.isEmpty()) {
      resultInSideDto.setObject(lstWoCdGroupUnitDTO.get(0).getCdGroupId().toString());
    }
    return resultInSideDto;
  }

  public File exportFileEx(List lstData, List<ConfigHeaderExport> lstHeaderSheet, String title,
      String sheetName, String fileNameOut, String headerPrefix, String sttKey) throws Exception {
    ConfigFileExport configfileExport;
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet;
    String subTitle = I18n.getLanguage("common.export.exportDate",
        DateTimeUtils.date2ddMMyyyyHHMMss(DateTimeUtils.convertDateOffset()));
    configfileExport = new ConfigFileExport(
        lstData
        , sheetName
        , title
        , subTitle
        ,
        7,
        3
        , lstHeaderSheet.size()
        , true
        , headerPrefix
        , lstHeaderSheet
        , fieldSplit
        , ""
        , I18n.getLanguage("common.export.firstLeftHeader")
        , I18n.getLanguage("common.export.secondLeftHeader")
        , I18n.getLanguage("common.export.firstRightHeader")
        , I18n.getLanguage("common.export.secondRightHeader")
    );
    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage(sttKey),
        "HEAD", "STRING");

    configfileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
//    String[] customTitle = new String[]{"0", "60", "0,128,0",
//        "1"}; //[1:0] co aligncenter ko, String alignLeft, String background, String numberRowMerge
//    configfileExport.setCustomTitle(customTitle);
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

  private List<ConfigHeaderExport> renderHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj0 = lstHeader.get(0);
    int count = 0;
    for (Object data : obj0) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 5) {
      return false;
    }

    return true;
  }

  public File exportFileImport(List<CfgFtOnTimeDTO> lstExport) {
    try {
      String header[] = new String[]{
          "cdGroupCode", "userName", "cfgTimeStr",
          "businessName", "resultImport"};
      return exportFileEx(lstExport, renderHeaderSheet(header),
          I18n.getLanguage("cfgFtOnTime.list"),
          I18n.getLanguage("cfgFtOnTime.list.export"), "IMPORT_CFG_FT_ON_TIME_RESULT",
          "language.cfgFtOnTime.list", "cfgFtOnTime.list.stt");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
