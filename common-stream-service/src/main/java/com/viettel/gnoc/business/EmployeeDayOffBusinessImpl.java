package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.repository.LogChangeConfigRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.EmployeeDayOffDTO;
import com.viettel.gnoc.repository.EmployeeDayOffRepository;
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
@Transactional
@Service
public class EmployeeDayOffBusinessImpl implements EmployeeDayOffBusiness {

  private static final String EMPLOYEE_DAY_OFF_CREATE_RESULT_IMPORT = "EMPLOYEE_DAY_OFF_CREATE_RESULT_IMPORT";
  private static final String EMPLOYEE_DAY_OFF_CREATE_EXPORT = "EMPLOYEE_DAY_OFF_CREATE_EXPORT";
  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  private EmployeeDayOffRepository employeeDayOffRepository;

  @Autowired
  private UnitRepository unitRepository;

  @Autowired
  private LogChangeConfigRepository logChangeConfigRepository;


  @Override
  public Datatable getListEmployeeDayOff(EmployeeDayOffDTO employeeDayOffDTO) {
    log.debug("Request to getListEmployeeDayOff : {}", employeeDayOffDTO);
    return employeeDayOffRepository.getListEmployeeDayOff(employeeDayOffDTO);
  }

  @Override
  public EmployeeDayOffDTO findEmployeeDayOffById(Long id) {
    log.debug("Request to findEmployeeDayOffById : {}", id);
    return employeeDayOffRepository.findEmployeeDayOffById(id);
  }

  @Override
  public String deleteEmployeeDayOffById(Long id) {
    log.debug("Request to deleteEmployeeDayOffById : {}", id);
    String result = employeeDayOffRepository.deleteEmployeeDayOffById(id);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("deleteEmployeeDayOffById");
    logChangeConfigDTO.setContent("DeleteEmployeeDayOffById with ID: " + id);
    logChangeConfigRepository.insertLog(logChangeConfigDTO);
    return result;
  }

  @Override
  public ResultInSideDto insertEmployeeDayOff(List<EmployeeDayOffDTO> employeeDayOffDTOS) {
    log.debug("Request to insertEmployeeDayOff : {}", employeeDayOffDTOS);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (int i = 0; i < employeeDayOffDTOS.size(); i++) {
      EmployeeDayOffDTO dto1 = employeeDayOffDTOS.get(i);
      for (int j = i + 1; j < employeeDayOffDTOS.size(); j++) {
        EmployeeDayOffDTO dto2 = employeeDayOffDTOS.get(j);
        if (dto1.getEmpId().equals(dto2.getEmpId()) && dto1.getDayOff().equals(dto2.getDayOff())) {
          resultInSideDto.setMessage(I18n.getLanguage("employeeDayOff.vacation.duplicate"));
          resultInSideDto.setKey(RESULT.DUPLICATE);
          return resultInSideDto;
        }
      }
    }
    resultInSideDto = employeeDayOffRepository
        .insertEmployeeDayOff(employeeDayOffDTOS);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("insertEmployeeDayOff");
    logChangeConfigDTO.setContent("InsertEmployeeDayOff with ID: " + resultInSideDto.getId());
    logChangeConfigRepository.insertLog(logChangeConfigDTO);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateEmployeeDayOff(EmployeeDayOffDTO dto) {
    log.debug("Request to updateEmployeeDayOff : {}", dto);
    ResultInSideDto resultInSideDto;
    resultInSideDto = employeeDayOffRepository.updateEmployeeDayOff(dto);
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("updateEmployeeDayOff");
    logChangeConfigDTO.setContent("UpdateEmployeeDayOff with ID: " + dto.getIdDayOff());
    logChangeConfigRepository.insertLog(logChangeConfigDTO);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    Long check = 1L;
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    List<EmployeeDayOffDTO> listDto;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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
        lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 4,
            0, 8, 1000);
        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }
        List<Object[]> lstData = CommonImport.getDataFromExcelFile(fileImp, 0, 5,
            0, 8, 1000);

        if (lstData.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(1500));
          return resultInSideDto;
        }

        listDto = new ArrayList<>();
        if (!lstData.isEmpty()) {
          setMapNameEmployeess();
          setMapUnitIDEmployee();
          int index = 0;
          int value = 1;
          for (Object[] obj : lstData) {
            EmployeeDayOffDTO createDTO = new EmployeeDayOffDTO();
            createDTO.setResultImport(" ");
            if (obj[1] != null) {
              if (!DataUtil.isNumber(obj[1].toString().trim()) || !checkUserIsEnable(
                  obj[1].toString().trim()) || !checkUserInUnit(obj[1].toString().trim())) {
                createDTO.setResultImport(I18n
                    .getLanguage("employeeDayOff.vacation.errType.empIdError"));
                createDTO.setEmpIdError(obj[1].toString().trim());
                check = 0L;
              } else {
                createDTO.setEmpId(Long.valueOf(obj[1].toString().trim()));
                createDTO.setEmpIdError(obj[1].toString().trim());
                createDTO.setEmpUsername(setMapNameEmployeess().get(createDTO.getEmpId()));
                createDTO.setEmpUnit(setMapUnitIDEmployee().get(createDTO.getEmpId()));
              }
            } else {
              createDTO.setEmpId(null);
              createDTO.setEmpIdError(null);
            }
            if (obj[2] != null) {
              if (obj[2].toString().trim().matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
                Date ret = sdf.parse(obj[2].toString().trim());
                if (sdf.format(ret).equals(obj[2].toString().trim())) {
                  createDTO.setDayOff(ret);
                  createDTO.setDayOffError(obj[2].toString().trim());
                } else {
                  createDTO.setResultImport(I18n
                      .getLanguage("employeeDayOff.vacation.errType.dayOffError"));
                  createDTO.setDayOffError(obj[2].toString().trim());
                  check = 0L;
                }
              } else {
                createDTO.setResultImport(I18n
                    .getLanguage("employeeDayOff.vacation.errType.dayOffError"));
                createDTO.setDayOffError(obj[2].toString().trim());
                check = 0L;
              }
            } else {
              createDTO.setDayOff(null);
              createDTO.setDayOffError(null);
            }
            if (obj[3] != null) {
              if (obj[3].toString().trim() instanceof String) {
                createDTO.setVacation(obj[3].toString().trim());
                if ("MOR".equals(createDTO.getVacation())) {
                  createDTO.setVacation("MOR");
                } else if ("AFT".equals(createDTO.getVacation())) {
                  createDTO.setVacation("AFT");
                } else if ("FULL".equals(createDTO.getVacation())) {
                  createDTO.setVacation("FULL");
                } else {
                  createDTO.setResultImport(I18n
                      .getLanguage("employeeDayOff.vacation.errType.vacation"));
                  createDTO.setVacation(obj[3].toString().trim());
                }
              }
            } else {
              createDTO.setVacation(null);
            }

            EmployeeDayOffDTO createExportDTO = validateImportInfo(createDTO,
                listDto);
            if (createExportDTO.getIdDayOff() != null) {
              createDTO.setIdDayOff(createExportDTO.getIdDayOff());
            }
            if (createExportDTO.getResultImport() == null || createDTO.getResultImport() == " ") {
              createExportDTO.setResultImport(
                  I18n.getLanguage("employeeDayOff.result.import"));
              listDto.add(createExportDTO);
            } else {
              listDto.add(createExportDTO);
              index++;
            }
            value++;
          }
          if (index == 0) {
            if (!listDto.isEmpty()) {
              resultInSideDto = insertEmployeeDayOff(listDto);
              if (resultInSideDto.getKey().equals(RESULT.DUPLICATE)) {
                check = 0L;
                File fileExport = handleFile(listDto, Constants.RESULT_IMPORT, check);
                resultInSideDto.setMessage(I18n.getLanguage("employeeDayOff.existed"));
                resultInSideDto.setKey(RESULT.ERROR);
                resultInSideDto.setFile(fileExport);
              }
            }
          } else {
            File fileExport = handleFile(listDto, Constants.RESULT_IMPORT, check);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = handleFile(listDto, Constants.RESULT_IMPORT, check);
          resultInSideDto.setFile(fileExport);
          return resultInSideDto;
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(ex.getMessage());
    }
    logChangeConfigDTO.setFunctionCode("ImportEmployeeDayOff");
    logChangeConfigDTO.setContent("ImportEmployeeDayOff success");
    logChangeConfigRepository.insertLog(logChangeConfigDTO);
    return resultInSideDto;
  }

  private boolean checkUserInUnit(String userId) {
    UserToken userToken = TicketProvider.getUserToken();
    UnitDTO unitDTO = unitRepository.findUnitById(userToken.getDeptId());
    UnitDTO dto = new UnitDTO();
    dto.setUnitId(userToken.getDeptId());
    dto.setIsCommittee(unitDTO.getIsCommittee());
    List<UsersInsideDto> lstUserBean = employeeDayOffRepository.getListUserInUnit(dto);
    for (UsersInsideDto item : lstUserBean) {
      if (Long.valueOf(userId).equals(item.getUserId())) {
        return true;
      }
    }
    return false;
  }

  private boolean checkUserIsEnable(String userId) {
    List<UsersInsideDto> usersInsideDtos = employeeDayOffRepository.checkUserIsEnable(userId);
    if (usersInsideDtos != null && usersInsideDtos.size() > 0) {
      return false;
    }
    return true;
  }

  private EmployeeDayOffDTO validateImportInfo(EmployeeDayOffDTO exportDTO,
      List<EmployeeDayOffDTO> listExportDto) {
    if (StringUtils.isStringNullOrEmpty(exportDTO.getEmpIdError())) {
      exportDTO.setResultImport(I18n.getLanguage("employee.dayoff.err.empIdError"));
      return exportDTO;
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getDayOffError())) {
      exportDTO.setResultImport(I18n.getLanguage("employee.dayoff.err.empIdError"));
      return exportDTO;
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getVacation())) {
      exportDTO.setResultImport(I18n.getLanguage("employee.dayoff.err.vacation"));
      return exportDTO;
    }
    if (I18n.getLanguage("cfg.import.vacation.morningCode").equals(exportDTO.getVacation())) {
      exportDTO.setVacation("MOR");
    }
    if (I18n.getLanguage("cfg.import.vacation.afternoonCode").equals(exportDTO.getVacation())) {
      exportDTO.setVacation("AFT");
    }
    if (I18n.getLanguage("cfg.import.vacation.allDayCode").equals(exportDTO.getVacation())) {
      exportDTO.setVacation("FULL");
    }

    EmployeeDayOffDTO employeeDayOffDTO = null;
    if (!StringUtils.isStringNullOrEmpty(exportDTO.getEmpId()) && !StringUtils
        .isStringNullOrEmpty(exportDTO.getDayOff()) && !StringUtils
        .isStringNullOrEmpty(exportDTO.getVacation())) {
      employeeDayOffDTO = employeeDayOffRepository
          .checkEmployeeDayOffExist(exportDTO.getEmpId()
              , exportDTO.getDayOff()
              , exportDTO.getVacation());
    }
    if (employeeDayOffDTO != null) {
      exportDTO.setResultImport(I18n.getLanguage("employeeDayOff.existed"));
      return exportDTO;
    }

    if (listExportDto != null && listExportDto.size() > 0) {
      exportDTO = validateDuplicate(listExportDto, exportDTO);
    }
    return exportDTO;
  }

  private EmployeeDayOffDTO validateDuplicate(List<EmployeeDayOffDTO> list,
      EmployeeDayOffDTO employeeDayOffDTO) {
    try {
      for (int i = 0; i < list.size(); i++) {
        EmployeeDayOffDTO employeeDayOffDTOTemp = list.get(i);
        if (employeeDayOffDTOTemp.getEmpIdError() == null) {
          employeeDayOffDTOTemp.setEmpIdError("");
        }
        if (employeeDayOffDTOTemp.getDayOffError() == null) {
          employeeDayOffDTOTemp.setDayOffError("");
        }
        if (employeeDayOffDTOTemp.getVacation() == null) {
          employeeDayOffDTOTemp.setVacation("");
        }
        if (employeeDayOffDTOTemp.getEmpIdError().equals(employeeDayOffDTO.getEmpIdError())
            && employeeDayOffDTOTemp.getDayOffError().equals(employeeDayOffDTO.getDayOffError())) {
          employeeDayOffDTO.setResultImport(I18n.getLanguage("employeeDayOff.err.dup-code-in-file")
              .replaceAll("0", String.valueOf((i) + 1)));
          break;
        }
      }
      return employeeDayOffDTO;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  private String checkResultImport(String resultImport) {
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      resultImport = resultImport.concat("; \n");
    }
    return resultImport;
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    /*if (count != 8) {
      return false;
    }*/
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("employee.dayoff.stt")).equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfg.import.empId") + "(*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfg.import.dayOff") + "(*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("cfg.import.vacation") + "(*)")
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    return true;
  }

  private File handleFile(List<EmployeeDayOffDTO> listExportDto, String key, Long check)
      throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    String sheetName = I18n.getLanguage("employee.dayoff.export.title");
    String title = I18n.getLanguage("employee.dayoff.export.title");
    String subTitle;
    String fileNameOut;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    ConfigHeaderExport configHeaderExport;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    if (check == 1L) {
      configHeaderExport = new ConfigHeaderExport("empId", "LEFT", false, 0,
          0, new String[]{}, null, "STRING");
    } else {
      configHeaderExport = new ConfigHeaderExport("empIdError", "LEFT", false, 0,
          0, new String[]{}, null, "STRING");
    }
    headerExportList.add(configHeaderExport);
    if (check == 1L) {
      configHeaderExport = new ConfigHeaderExport("dayOff", "LEFT", false, 0,
          0, new String[]{}, null, "STRING");
    } else {
      configHeaderExport = new ConfigHeaderExport("dayOffError", "LEFT", false, 0,
          0, new String[]{}, null, "STRING");
    }

    headerExportList.add(configHeaderExport);
    configHeaderExport = new ConfigHeaderExport("vacation", "LEFT", false, 0,
        0, new String[]{}, null, "STRING");

    headerExportList.add(configHeaderExport);
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = EMPLOYEE_DAY_OFF_CREATE_RESULT_IMPORT;
      subTitle = I18n.getLanguage("employee.dayoff.export.import", dateFormat.format(new Date()));
      configHeaderExport = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0,
          new String[]{},
          null,
          "STRING");
      headerExportList.add(configHeaderExport);
    } else {
      fileNameOut = EMPLOYEE_DAY_OFF_CREATE_EXPORT;
      subTitle = I18n
          .getLanguage("employee.dayoff.export.eportDate", dateFormat.format(new Date()));
    }

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        listExportDto,
        sheetName,
        title,
        subTitle,
        7,
        3,
        6,
        true,
        "language.employeeDayOff",
        headerExportList,
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
        I18n.getLanguage("employee.dayoff.stt"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);

    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(fileTemplate, fileNameOut,
        fileExports, rootPath, null);
    return fileExport;
  }

  private Map<Long, String> setMapNameEmployeess() {
    Map<Long, String> mapNameEmployee = new HashMap<>();
    UserToken userToken = TicketProvider.getUserToken();
    UnitDTO unitDTO = unitRepository.findUnitById(userToken.getDeptId());
    UnitDTO dto = new UnitDTO();
    dto.setUnitId(userToken.getDeptId());
    dto.setIsCommittee(unitDTO.getIsCommittee());
    List<UsersInsideDto> lstUserBean = employeeDayOffRepository.getListUserInUnit(dto);
    if (lstUserBean.size() > 0 && lstUserBean != null) {
      for (UsersInsideDto usersInsideDto : lstUserBean) {
        mapNameEmployee
            .put(usersInsideDto.getUserId(),
                usersInsideDto.getFullname() + "(" + usersInsideDto.getUsername() + ")");
      }
    }
    return mapNameEmployee;
  }

  private Map<Long, Long> setMapUnitIDEmployee() {
    Map<Long, Long> mapUnitIDEmployee = new HashMap<>();
    UserToken userToken = TicketProvider.getUserToken();
    UnitDTO unitDTO = unitRepository.findUnitById(userToken.getDeptId());
    UnitDTO dto = new UnitDTO();
    dto.setUnitId(userToken.getDeptId());
    dto.setIsCommittee(unitDTO.getIsCommittee());
    List<UsersInsideDto> lstUserBean = employeeDayOffRepository.getListUserInUnit(dto);
    if (lstUserBean.size() > 0 && lstUserBean != null) {
      for (UsersInsideDto usersInsideDto : lstUserBean) {
        mapUnitIDEmployee.put(usersInsideDto.getUserId(), usersInsideDto.getUnitId());
      }
    }
    return mapUnitIDEmployee;
  }

  @Override
  public File getTemplate() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workBook.getSheetAt(0);
    XSSFSheet sheetParam = workBook.createSheet("param");
    XSSFSheet sheetParam2 = workBook.createSheet("NhanVien");
    XSSFSheet sheetParam3 = workBook.createSheet("CaNghi");
    String[] header = new String[]{I18n.getLanguage("employee.dayoff.stt"),
        I18n.getLanguage("cfg.import.empId"),
        I18n.getLanguage("cfg.import.dayOff"),
        I18n.getLanguage("cfg.import.vacation")};
    String[] headerStart = new String[]{
        I18n.getLanguage("cfg.import.empId"),
        I18n.getLanguage("cfg.import.dayOff"),
        I18n.getLanguage("cfg.import.vacation")
    };

    String[] headerStart2 = new String[]{
        I18n.getLanguage("cfg.import.empId"),
        I18n.getLanguage("cfg.import.username"),
        I18n.getLanguage("cfg.import.email")
    };

    String[] headerStart3 = new String[]{
        I18n.getLanguage("cfg.import.vacation"),
        I18n.getLanguage("cfg.import.vacationName")
    };

    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStart);

    List<String> listHeaderStar2 = Arrays.asList(headerStart2);

    List<String> listHeaderStar3 = Arrays.asList(headerStart3);

    int vacationColumn = listHeader.indexOf(I18n.getLanguage("cfg.import.vacation"));

    int employeeIDColumn = listHeader.indexOf(I18n.getLanguage("cfg.import.empId"));

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    // Tao tieu de
    sheetOne.addMergedRegion(new CellRangeAddress(0, 0, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(0);
    titleRow.setHeightInPoints(45);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("employee.dayoff.export.title"));
    titleCell.setCellStyle(styles.get("title"));

    // Tao note
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row noteRow = sheetOne.createRow(2);
    noteRow.setHeightInPoints(18);
    Cell noteCell = noteRow.createCell(0);
    noteCell.setCellValue(I18n.getLanguage("wocdgroup.importfile.template.excel.note"));
    noteCell.setCellStyle(styles.get("note"));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());
    //Tao header
    Row headerRow = sheetOne.createRow(4);
    headerRow.setHeightInPoints(18);
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

    Row headerRow2 = sheetParam2.createRow(0);
    for (int i = 0; i < listHeaderStar2.size(); i++) {
      Cell headerCell = headerRow2.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeaderStar2.get(i));
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(styles.get("header"));
      sheetParam2.setColumnWidth(i, 7000);
    }

    Row headerRow3 = sheetParam3.createRow(0);
    for (int i = 0; i < listHeaderStar3.size(); i++) {
      Cell headerCell = headerRow3.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeaderStar3.get(i));
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(styles.get("header"));
      sheetParam3.setColumnWidth(i, 7000);
    }

    sheetOne.setColumnWidth(0, 3000);
    int row = 5;
    ewu.createCell(sheetParam, 3, row++, I18n.getLanguage("cfg.import.vacation.morningCode"),
        styles.get("cell"));
    ewu.createCell(sheetParam, 3, row++, I18n.getLanguage("cfg.import.vacation.afternoonCode"),
        styles.get("cell"));
    ewu.createCell(sheetParam, 3, row, I18n.getLanguage("cfg.import.vacation.allDayCode"),
        styles.get("cell"));
    row = 5;
    int row2 = 1;

    UserToken userToken = TicketProvider.getUserToken();
    UnitDTO unitDTO = unitRepository.findUnitById(userToken.getDeptId());
    UnitDTO dto = new UnitDTO();
    dto.setUnitId(userToken.getDeptId());
    dto.setIsCommittee(unitDTO.getIsCommittee());
    List<UsersInsideDto> lstUserBean = employeeDayOffRepository.getListUserInUnit(dto);
    if (lstUserBean != null && !lstUserBean.isEmpty()) {
      for (UsersInsideDto item : lstUserBean) {
        ewu.createCell(sheetParam, 1, row, String.valueOf(item.getUserId()),
            styles.get("cell"));
        ewu.createCell(sheetParam, 2, row, item.getUsername(), styles.get("cell"));

        ewu.createCell(sheetParam2, 0, row2, String.valueOf(item.getUserId()),
            styles.get("cell"));
        ewu.createCell(sheetParam2, 1, row2, item.getUsername(), styles.get("cell"));
        ewu.createCell(sheetParam2, 2, row2, item.getEmail(), styles.get("cell"));
        row++;
        row2++;
      }
    }

    int rowSheet3 = 1;
    int rowrowSheet2 = 1;
    ewu.createCell(sheetParam3, 0, rowSheet3++,
        I18n.getLanguage("cfg.import.vacation.morningCode"));
    ewu.createCell(sheetParam3, 0, rowSheet3++,
        I18n.getLanguage("cfg.import.vacation.afternoonCode"));
    ewu.createCell(sheetParam3, 0, rowSheet3, I18n.getLanguage("cfg.import.vacation.allDayCode"));

    ewu.createCell(sheetParam3, 1, rowrowSheet2++, I18n.getLanguage("cfg.import.vacation.morning"),
        styles.get("cell"));
    ewu.createCell(sheetParam3, 1, rowrowSheet2++,
        I18n.getLanguage("cfg.import.vacation.afternoon"),
        styles.get("cell"));
    ewu.createCell(sheetParam3, 1, rowrowSheet2, I18n.getLanguage("cfg.import.vacation.allDay"),
        styles.get("cell"));

    sheetParam3.autoSizeColumn(1);
    Name vacationCode = workBook.createName();
    vacationCode.setNameName("vacationName");
    vacationCode.setRefersToFormula("CaNghi!$B$2:$B$" + row);

    sheetParam2.autoSizeColumn(4);
    Name employeeID = workBook.createName();
    employeeID.setNameName("empId2");
    employeeID.setRefersToFormula("NhanVien!$B$2:$B$" + row);

    sheetParam.autoSizeColumn(0);
    Name vacation = workBook.createName();
    vacation.setNameName("vacation");
    vacation.setRefersToFormula("param!$D$2:$D$" + 8);

    Name empID = workBook.createName();
    empID.setNameName("empId");
    empID.setRefersToFormula("param!$B$2:$B$" + row);

    XSSFDataValidationConstraint priorityConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "vacation");
    CellRangeAddressList cellRangePriority = new CellRangeAddressList(5, 65000, vacationColumn,
        vacationColumn);
    XSSFDataValidation dataValidationPriority = (XSSFDataValidation) dvHelper.createValidation(
        priorityConstraint, cellRangePriority);
    dataValidationPriority.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationPriority);

    XSSFDataValidationConstraint priorityConstraint2 = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "empId");
    CellRangeAddressList cellRangePriority2 = new CellRangeAddressList(5, 65000, employeeIDColumn,
        employeeIDColumn);
    XSSFDataValidation dataValidationPriority2 = (XSSFDataValidation) dvHelper.createValidation(
        priorityConstraint2, cellRangePriority2);
    dataValidationPriority2.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationPriority2);

    workBook.setSheetName(0, I18n.getLanguage("employee.dayoff.export.title"));
    workBook.setSheetHidden(1, true);
    workBook.setSheetHidden(2, false);
    sheetParam.setSelected(false);

    String fileResult = tempFolder + File.separator;
    String fileName = "BM_IMPORT_THOIGIANNGHI" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }


  @Override
  public File exportData(EmployeeDayOffDTO dto) throws Exception {
    String locale = I18n.getLocale();
    List<EmployeeDayOffDTO> list = employeeDayOffRepository.getListEmployeeDayOffExport(dto);
    List<EmployeeDayOffDTO> listExport = new ArrayList<>();
    if (list != null && !list.isEmpty()) {
      for (EmployeeDayOffDTO employeeDayOffDTO : list) {
        EmployeeDayOffDTO employeeDayOffDTO1 = new EmployeeDayOffDTO();
        employeeDayOffDTO1.setEmpUsername(employeeDayOffDTO.getEmpUsername());
        employeeDayOffDTO1.setUnitName(
            employeeDayOffDTO.getUnitName() + "( " + employeeDayOffDTO.getUnitCode() + " )");
        employeeDayOffDTO1.setDayOff(employeeDayOffDTO.getDayOff());
        if ("vi_VN".equals(locale)) {
          if ("MOR".equals(employeeDayOffDTO.getVacation())) {
            employeeDayOffDTO1.setVacationName(I18n.getLanguage("employeeDayOff.vacationName.MOR"));
          } else if ("AFT".equals(employeeDayOffDTO.getVacation())) {
            employeeDayOffDTO1.setVacationName(I18n.getLanguage("employeeDayOff.vacationName.AFT"));
          } else if ("FULL".equals(employeeDayOffDTO.getVacation())) {
            employeeDayOffDTO1
                .setVacationName(I18n.getLanguage("employeeDayOff.vacationName.FULL"));
          }
        } else {
          if ("MOR".equals(employeeDayOffDTO.getVacation())) {
            employeeDayOffDTO1.setVacationName(I18n.getLanguage("employeeDayOff.vacationName.MOR"));
          } else if ("AFT".equals(employeeDayOffDTO.getVacation())) {
            employeeDayOffDTO1.setVacationName(I18n.getLanguage("employeeDayOff.vacationName.AFT"));
          } else if ("FULL".equals(employeeDayOffDTO.getVacation())) {
            employeeDayOffDTO1
                .setVacationName(I18n.getLanguage("employeeDayOff.vacationName.FULL"));
          }
        }
        listExport.add(employeeDayOffDTO1);
      }
    }
    String[] header = new String[]{
        "empUsername", "unitName", "dayOff", "vacationName"
    };
    UserToken userToken = ticketProvider.getUserToken();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    logChangeConfigDTO.setFunctionCode("ExportEmployeeDayOff");
    logChangeConfigDTO.setContent("ExportEmployeeDayOff");
    logChangeConfigRepository.insertLog(logChangeConfigDTO);
    return handleFileExport(listExport, header, DateUtil.date2ddMMyyyyHHMMss(new Date()), "");
  }

  private File handleFileExport(List<EmployeeDayOffDTO> listExport, String[] columnExport,
      String date, String code) throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = I18n.getLanguage("employee.dayoff.export.sheetname");
    String title = I18n.getLanguage("employee.dayoff.export.title");
    String fileNameOut = I18n.getLanguage("employee.dayoff.export.title");
    String subTitle;
    if (date != null) {
      Date fromDateTmp = DataUtil.convertStringToDate(date);
      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      subTitle = I18n
          .getLanguage("employee.dayoff.export.eportDate", dateFormat.format(fromDateTmp));
    } else {
      subTitle = "";
    }
    ConfigFileExport configFileExport = new ConfigFileExport(
        listExport,
        sheetName,
        title,
        subTitle,
        7,
        3,
        6,
        true,
        "language.employeeDayOff",
        lstHeaderSheet1,
        fieldSplit,
        "",
        I18n.getLanguage("schedule.cr.report.export.firstLeftHeader"),
        I18n.getLanguage("schedule.cr.report.export.secondLeftHeader"),
        I18n.getLanguage("schedule.cr.report.export.firstRightHeader"),
        I18n.getLanguage("schedule.cr.report.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(7, 0, 0, 0,
        I18n.getLanguage("employee.dayoff.stt"), "HEAD", "STRING");
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
