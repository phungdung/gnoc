package com.viettel.gnoc.pt.business;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ErrorInfo;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrScheduleCdExportDTO;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import com.viettel.gnoc.pt.dto.ProblemConfigTimeDTO;
import com.viettel.gnoc.pt.repository.ProblemConfigTimeRepository;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
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
import org.apache.poi.ss.usermodel.DataValidationConstraint.ValidationType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
public class ProblemConfigTimeBusinessImpl implements ProblemConfigTimeBusiness{

  @Autowired
  ProblemConfigTimeRepository problemConfigTimeRepository;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Value("${application.temp.folder}")
  private String tempFolder;
  private Map<String, String> mapErrorResult = new HashMap<>();
  private List<ErrorInfo> cellErrorList = new ArrayList<>();
  HashMap<String, String> mapGroupReasonName = new HashMap<>();
  HashMap<String, String> mapSolutionTypeName = new HashMap<>();
  HashMap<String, String> mapTypeIdStr = new HashMap<>();
  HashMap<String, String> mapCategoryStr = new HashMap<>();
  private String pathTemplate = "templates" + File.separator + "maintenance" + File.separator;
  List<ProblemConfigTimeDTO> problemConfigTimeDTOList = new ArrayList<>();
  @Override
  public List<CatItemDTO> getListComboboxGroupReasonOrSolution(String codeCategory) {
    return catItemBusiness
        .getListItemByCategoryAndParent(codeCategory, null);
  }

  @Override
  public Datatable onSearchProbleConfigTime(ProblemConfigTimeDTO dto) {
    return problemConfigTimeRepository.onSearchProbleConfigTime(dto);
  }

  @Override
  public ResultInSideDto insertProblemConfigTime(ProblemConfigTimeDTO dto) {
    return problemConfigTimeRepository.insertProblemConfigTime(dto);
  }

  @Override
  public ResultInSideDto updateProblemConfigTime(ProblemConfigTimeDTO dto) {
    return problemConfigTimeRepository.updateProblemConfigTime(dto);
  }

  @Override
  public ProblemConfigTimeDTO findProblemConfigTimeById(Long id) {
    return problemConfigTimeRepository.findProblemConfigTimeById(id);
  }

  @Override
  public String deleteProblemConfigTime(Long id) {
    return problemConfigTimeRepository.deleteProblemConfigTime(id);
  }

  @Override
  public File getListProblemConfigTimeSearchExport(ProblemConfigTimeDTO dto) throws Exception {
    log.debug("Request to getListProblemsSearchExport : {}", dto);
    String[] header = new String[]{"reasonGroupName", "solutionTypeName", "typeIdStr", "subCategoryIdStr", "timeProcess"};
    List<ProblemConfigTimeDTO> lstData = problemConfigTimeRepository
        .onSearchExport(dto);
    setMapGroupReasonName();
    setMapSolutionTypeName();
    for(int i=0; i<lstData.size(); i++){
      for (Map.Entry<String, String> item : mapGroupReasonName.entrySet()) {
        if(lstData.get(i) != null && String.valueOf(lstData.get(i).getReasonGroupId()).equals(item.getKey())){
          lstData.get(i).setReasonGroupName(item.getValue());
          break;
        }
      }
      for (Map.Entry<String, String> item : mapSolutionTypeName.entrySet()) {
        if(lstData.get(i) != null && String.valueOf(lstData.get(i).getSolutionTypeId()).equals(item.getKey())){
          lstData.get(i).setSolutionTypeName(item.getValue());
          break;
        }
      }
    }
    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet(header);
    return exportFileEx(lstData, lstHeaderSheet, "");
  }

  @Override
  public ResultInSideDto importProblemConfigTime(MultipartFile multipartFile) throws Exception {
    List<ProblemConfigTimeDTO> lstoExportDTOS = new ArrayList<>();
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    //header
    String[] header = new String[]{"reasonGroupName", "solutionTypeName", "typeIdStr", "subCategoryIdStr", "timeProcess"};
    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet(header);
    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDTO.setKey(Constants.RESULT.FILE_IS_NULL);
        return resultInSideDTO;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        if (!Constants.RESULT.SUCCESS.equals(resultInSideDTO.getKey())) {
          return resultInSideDTO;
        }

        File fileImport = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImport,
            0,//sheet
            5,//begin row
            0,//from column
            5,//to column
            1000);

        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDTO.setKey(Constants.RESULT.FILE_INVALID_FORMAT);
          return resultInSideDTO;
        }

        // Lay du lieu import
        List<Object[]> lstData = CommonImport.getDataFromExcelFile(
            fileImport,
            0,//sheet
            6,//begin row
            0,//from column
            5,//to column
            1000
        );

        if (lstData.size() > 1500) {
          resultInSideDTO.setKey(Constants.RESULT.DATA_OVER);
          return resultInSideDTO;
        }

        problemConfigTimeDTOList = new ArrayList<>();

        if (!lstData.isEmpty()) {
          int row = 5;
          int index = 0;
          setMapGroupReasonName();
          setMapSolutionTypeName();
          setMapSubcategoryStr();
          setMapTypeStr();
          for (Object[] obj : lstData) {
            ProblemConfigTimeDTO dto = new ProblemConfigTimeDTO();
            if (obj[1] != null) {
              dto.setReasonGroupName(obj[1].toString().trim());
              for (Map.Entry<String, String> item : mapGroupReasonName.entrySet()) {
                if (dto.getReasonGroupName().equals(item.getValue())) {
                  dto.setReasonGroupId(Long.parseLong(item.getKey()));
                  break;
                } else {
                  dto.setReasonGroupId(null);
                }
              }
            } else {
              dto.setReasonGroupName("");
            }
            if (obj[2] != null) {
              dto.setSolutionTypeName(obj[2].toString().trim());
              for (Map.Entry<String, String> item : mapSolutionTypeName.entrySet()) {
                if (dto.getSolutionTypeName().equals(item.getValue())) {
                  dto.setSolutionTypeId(Long.parseLong(item.getKey()));
                  break;
                } else {
                  dto.setSolutionTypeId(null);
                }
              }
            } else {
              dto.setSolutionTypeName("");
            }
            if (obj[3] != null) {
              dto.setTypeIdStr(obj[3].toString().trim());
              for (Map.Entry<String, String> item : mapTypeIdStr.entrySet()) {
                if (dto.getTypeIdStr().equals(item.getValue())) {
                  dto.setTypeId(Long.parseLong(item.getKey()));
                  break;
                } else {
                  dto.setTypeId(null);
                }
              }
            } else {
              dto.setTypeIdStr("");
            }
            if (obj[4] != null) {
              dto.setSubCategoryIdStr(obj[4].toString().trim());
              for (Map.Entry<String, String> item : mapCategoryStr.entrySet()) {
                if (dto.getSubCategoryIdStr().equals(item.getValue())) {
                  dto.setSubCategoryId(Long.parseLong(item.getKey()));
                  break;
                } else {
                  dto.setSubCategoryId(null);
                }
              }
            } else {
              dto.setSubCategoryIdStr("");
            }
            if (obj[5] != null) {
              dto.setTimeProcess(Long.parseLong(obj[5].toString().trim()));
            } else {
              dto.setTimeProcess(null);
            }
            ProblemConfigTimeDTO dtoCheckResultImport = validateImportInfo(dto, problemConfigTimeDTOList);
            if (dtoCheckResultImport.getResultImport() == null) {
              dto.setResultImport(
                  I18n.getLanguage("problemConfigTime.resultImportSuccess"));
              lstoExportDTOS.add(dto);
            } else {
              dto.setResultImport(dtoCheckResultImport.getResultImport());
              lstoExportDTOS.add(dto);
              index++;
            }
            row++;
            problemConfigTimeDTOList.add(dto);
          }
          int change = 1;
          ResultInSideDto result = null;

          if (index == 0) {
            if (!problemConfigTimeDTOList.isEmpty()) {
              for(int i=0;i<problemConfigTimeDTOList.size();i++){
                result = problemConfigTimeRepository.insertProblemConfigTime(problemConfigTimeDTOList.get(i));
                if(result.getKey() != "SUCCESS" || result.getId() == null){
                  change = 0;
                }
              }
              if (result != null && change == 1) {
                resultInSideDTO = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
              } else {
                resultInSideDTO = new ResultInSideDto(null, RESULT.ERROR,
                    I18n.getLanguage("import.common.fail"));
              }
              File fileExport = exportFileEx(lstoExportDTOS,lstHeaderSheet, Constants.RESULT_IMPORT);
              resultInSideDTO.setFile(fileExport);
              resultInSideDTO.setFilePath(fileExport.getPath());
            }
          } else {
            File fileExport = exportFileEx(lstoExportDTOS,lstHeaderSheet, Constants.RESULT_IMPORT);
            resultInSideDTO.setKey(Constants.RESULT.ERROR);
            resultInSideDTO.setFile(fileExport);
            resultInSideDTO.setFilePath(fileExport.getPath());
          }
        } else {
          resultInSideDTO.setKey(Constants.RESULT.NODATA);
          resultInSideDTO.setMessage(Constants.FILE_NULL);
          return resultInSideDTO;
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultInSideDTO.setKey(Constants.RESULT.ERROR);
      resultInSideDTO.setMessage(ex.getMessage());
    }
    return resultInSideDTO;
  }


  private File exportFileEx(List<ProblemConfigTimeDTO> lstData,
      List<ConfigHeaderExport> lstHeaderSheet, String code) throws Exception {
    String title = I18n.getLanguage("problemConfigTime.export.grid.title");
    String sheetName = title;
    String fileNameOut;
    String subTitle;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    ConfigFileExport configfileExport = null;
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet = null;
    ConfigHeaderExport columnSheet1 = null;
    if (Constants.RESULT_IMPORT.equals(code)) {
      fileNameOut = "PROBLEM_CONFIG_TIME_RESULT_IMPORT";
//      subTitle = I18n
//          .getLanguage("odCfgScheduleCreate.export.import", dateFormat.format(new Date()));
      columnSheet1 = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet.add(columnSheet1);
    } else {
      fileNameOut = "PROBLEM_CONFIG_TIME_RESULT_EXPORT";
//      subTitle = I18n
//          .getLanguage("odCfgScheduleCreate.export.eportDate", dateFormat.format(new Date()));
    }
    configfileExport = new ConfigFileExport(
        lstData
        , sheetName
        , title
        , null
        , 7
        , 3
        , lstHeaderSheet.size()
        , true
        , "language.problemConfigTime.export.grid"
        , lstHeaderSheet
        , fieldSplit
        , ""
        , I18n.getLanguage("problems.export.firstLeftHeader")
        , I18n.getLanguage("problems.export.secondLeftHeader")
        , I18n.getLanguage("problems.export.firstRightHeader")
        , I18n.getLanguage("problems.export.secondRightHeader")
    );
    cellSheet = new CellConfigExport(7, 0, 0, 0,
        I18n.getLanguage("problemConfigTime.export.grid.stt"),
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

  private List<ConfigHeaderExport> renderHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }


  @Override
  public File getFileTemplate() throws Exception{
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);
    XSSFSheet sheetOrther = workBook.createSheet("Orther");

    String[] header = new String[]{
        I18n.getLanguage("problemConfigTime.export.grid.stt"),
        I18n.getLanguage("problemConfigTime.export.grid.reasonGroupId"),
        I18n.getLanguage("problemConfigTime.export.grid.solutionTypeId"),
        I18n.getLanguage("problemConfigTime.export.grid.typeId"),
        I18n.getLanguage("problemConfigTime.export.grid.subCategoryId"),
        I18n.getLanguage("problemConfigTime.export.grid.timeProcess"),
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("problemConfigTime.export.grid.reasonGroupId"),
        I18n.getLanguage("problemConfigTime.export.grid.solutionTypeId"),
        I18n.getLanguage("problemConfigTime.export.grid.typeId"),
        I18n.getLanguage("problemConfigTime.export.grid.subCategoryId"),
        I18n.getLanguage("problemConfigTime.export.grid.timeProcess")
    };
    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int sttColumn = listHeader.indexOf(I18n.getLanguage("problemConfigTime.export.grid.stt"));
    int systemCodeColumn = listHeader.indexOf(I18n.getLanguage("problemConfigTime.export.grid.reasonGroupId"));
    int systemNameColumn = listHeader.indexOf(I18n.getLanguage("problemConfigTime.export.grid.solutionTypeId"));
    int typeId = listHeader.indexOf(I18n.getLanguage("problemConfigTime.export.grid.typeId"));
    int subCategoryId = listHeader.indexOf(I18n.getLanguage("problemConfigTime.export.grid.subCategoryId"));
    int scheduleColumn = listHeader.indexOf(I18n.getLanguage("problemConfigTime.export.grid.timeProcess"));


    String firstLeftHeaderTitle = I18n.getLanguage("problems.export.firstLeftHeader");
    String secondLeftHeaderTitle = I18n.getLanguage("problems.export.secondLeftHeader");
    String firstRightHeaderTitle = I18n.getLanguage("problems.export.firstRightHeader");
    String secondRightHeaderTitle = I18n.getLanguage("problems.export.secondRightHeader");

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
    mainCellTitle.setCellValue(I18n.getLanguage("problemConfigTime.export.grid.title"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(3, 3, 1, 7));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao header
    Row headerRow = sheetMain.createRow(5);
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

    sheetMain.setColumnWidth(sttColumn, 2000);
    sheetMain.setColumnWidth(systemCodeColumn, 6000);
    sheetMain.setColumnWidth(systemNameColumn, 6000);
    sheetMain.setColumnWidth(scheduleColumn, 6000);
    sheetMain.setColumnWidth(typeId, 6000);
    sheetMain.setColumnWidth(subCategoryId, 6000);

    sheetOrther.setSelected(false);
    workBook.setSheetHidden(1, true);
    workBook.setSheetName(0, I18n.getLanguage("problemConfigTime.export.grid.title"));

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_PROBLEM_CONFIG_TIME" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }



  private ProblemConfigTimeDTO validateImportInfo(ProblemConfigTimeDTO dto, List<ProblemConfigTimeDTO> listExportDto) {
    String resultImport = "";
    // Nhóm nguyên nhân trống
    if (StringUtils.isStringNullOrEmpty(dto.getReasonGroupName())) {
      resultImport = resultImport.concat(I18n.getLanguage("problemConfigTime.err.reasonGroupId") + ";");
    }
    // Loại giải pháp trống
    if (StringUtils.isStringNullOrEmpty(dto.getSolutionTypeName())) {
      resultImport = resultImport.concat(I18n.getLanguage("problemConfigTime.err.solutionTypeId") + ";");
    }
    // Mảng kĩ thuật trống
    if (StringUtils.isStringNullOrEmpty(dto.getTypeIdStr())) {
      resultImport = resultImport.concat(I18n.getLanguage("problemConfigTime.err.typeId") + ";");
    }
    // Loại node mạng trống
    if (StringUtils.isStringNullOrEmpty(dto.getSubCategoryIdStr())) {
      resultImport = resultImport.concat(I18n.getLanguage("problemConfigTime.err.subCategoryId") + ";");
    }
    // Thời gian xử lý ngày trống
    if (StringUtils.isStringNullOrEmpty(dto.getTimeProcess())) {
      resultImport = resultImport.concat(I18n.getLanguage("problemConfigTime.err.timeProcess") + ";");
    }
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      dto.setResultImport(resultImport);
      return dto;
    }

    if ((dto.getReasonGroupId() != 0) && (dto.getSolutionTypeId() != 0)){
      ProblemConfigTimeDTO problemConfigTimeDTO = problemConfigTimeRepository
          .checkProblemConfigTimeCreateExit(dto);
      if (problemConfigTimeDTO != null) {
        problemConfigTimeDTO.setResultImport(I18n.getLanguage("problemConfigTime.err.dup-code") + ";");
        return problemConfigTimeDTO;
      }
    }
    String validateDuplicate = validateDuplicate(dto, listExportDto);
    if (StringUtils.isNotNullOrEmpty(validateDuplicate)) {
      dto.setResultImport(validateDuplicate);
      return dto;
    }

    if(StringUtils.isNotNullOrEmpty(dto.getReasonGroupId().toString().trim()) && StringUtils.isNotNullOrEmpty(dto.getSolutionTypeId().toString().trim()) && StringUtils.isNotNullOrEmpty(dto.getTypeId().toString().trim()) && StringUtils.isNotNullOrEmpty(dto.getSubCategoryId().toString().trim())){
      boolean check = problemConfigTimeRepository.checkExisted(dto.getReasonGroupId().toString(), dto.getSolutionTypeId().toString(), dto.getTypeIdStr(), dto.getSubCategoryIdStr(), null);
      if(check){
        dto.setResultImport(I18n.getLanguage("problemConfigTime.exist") + ";");
        return dto;
      }
    }
    return dto;
  }

  private String getValidateResult(ProblemConfigTimeDTO dto) {
    return mapErrorResult.get(dto.getId());
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
    if (!(I18n.getLanguage("problems.stt")).equalsIgnoreCase(obj0[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("problemConfigTime.export.grid.reasonGroupId") + "(*)")
        .equalsIgnoreCase(obj0[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("problemConfigTime.export.grid.solutionTypeId") + "(*)")
        .equalsIgnoreCase(obj0[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("problemConfigTime.export.grid.typeIdStr") + "(*)")
        .equalsIgnoreCase(obj0[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("problemConfigTime.export.grid.subCategoryIdStr") + "(*)")
        .equalsIgnoreCase(obj0[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("problemConfigTime.export.grid.timeProcess") + "(*)").equalsIgnoreCase(obj0[5].toString().trim())) {
      return false;
    }
    return true;
  }

  public void setMapGroupReasonName(){
    List<CatItemDTO> list = catItemBusiness.getListItemByCategoryAndParent("GROUP_REASON_PT", null);
    if(!list.isEmpty() && list != null){
      for(CatItemDTO catItemDTO : list){
        mapGroupReasonName.put(String.valueOf(catItemDTO.getItemId()), catItemDTO.getItemName());
      }
    }
  }

  public void setMapSolutionTypeName(){
    List<CatItemDTO> list = catItemBusiness.getListItemByCategoryAndParent("RADICAL_SOLUTION_PT", null);
    if(!list.isEmpty() && list != null){
      for(CatItemDTO catItemDTO : list){
        mapSolutionTypeName.put(String.valueOf(catItemDTO.getItemId()), catItemDTO.getItemName());
      }
    }
  }

  public void setMapTypeStr(){
    List<CatItemDTO> list = catItemBusiness.getListItemByCategoryAndParent("PT_TYPE", null);
    if(!list.isEmpty() && list != null){
      for(CatItemDTO catItemDTO : list){
        mapTypeIdStr.put(String.valueOf(catItemDTO.getItemId()), catItemDTO.getItemName());
      }
    }
  }

  public void setMapSubcategoryStr(){
    List<CatItemDTO> list = catItemBusiness.getListItemByCategoryAndParent("PT_SUB_CATEGORY", null);
    if(!list.isEmpty() && list != null){
      for(CatItemDTO catItemDTO : list){
        mapCategoryStr.put(String.valueOf(catItemDTO.getItemId()), catItemDTO.getItemName());
      }
    }
  }

  private String validateDuplicate(ProblemConfigTimeDTO dto,
      List<ProblemConfigTimeDTO> listExportDto) {
    String solutionTypeName = dto.getSolutionTypeName();
    String reasonGroupName = dto.getReasonGroupName();
    for (int i = 0; i < listExportDto.size(); i++) {
      ProblemConfigTimeDTO dto1 = listExportDto.get(i);
      if (solutionTypeName.equals(dto1.getSolutionTypeName()) &&
          reasonGroupName.equals(dto1.getReasonGroupName())) {
        return I18n.getLanguage("problemConfigTime.err.dup-code-in-file")
            .replaceAll("0", String.valueOf((i) + 1));
      }
    }
    return null;
  }
}
