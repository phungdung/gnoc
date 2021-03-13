package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.LogChangeConfigBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.ShiftHandoverDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.TtServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ShiftCrDTO;
import com.viettel.gnoc.cr.dto.ShiftHandoverFileDTO;
import com.viettel.gnoc.cr.dto.ShiftItDTO;
import com.viettel.gnoc.cr.dto.ShiftItSeriousDTO;
import com.viettel.gnoc.cr.dto.ShiftStaftDTO;
import com.viettel.gnoc.cr.dto.ShiftWorkDTO;
import com.viettel.gnoc.cr.dto.ShiftWorkOtherDTO;
import com.viettel.gnoc.incident.dto.CommonFileDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.repository.ShiftCrRepository;
import com.viettel.gnoc.repository.ShiftHandoverRepository;
import com.viettel.gnoc.repository.ShiftItRepository;
import com.viettel.gnoc.repository.ShiftItSeriousRepository;
import com.viettel.gnoc.repository.ShiftStaftRepository;
import com.viettel.gnoc.repository.ShiftWorkOtherRepository;
import com.viettel.gnoc.repository.ShiftWorkRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
public class ShiftHandoverBusinessImpl implements ShiftHandoverBusiness {

  private static final String SHIFT_WORK_CREATE_RESULT_IMPORT = "SHIFT_WORK_CREATE_RESULT_IMPORT";
  private static final String SHIFT_WORK_CREATE_EXPORT = "SHIFT_WORK_CREATE_EXPORT";

  private static final String SHIFT_CR_CREATE_RESULT_IMPORT = "SHIFT_CR_CREATE_RESULT_IMPORT";
  private static final String SHIFT_CR_CREATE_EXPORT = "SHIFT_CR_CREATE_EXPORT";

  @Value("${application.ftp.server}")
  private String ftpServer;

  @Value("${application.ftp.port}")
  private int ftpPort;

  @Value("${application.ftp.user}")
  private String ftpUser;

  @Value("${application.ftp.pass}")
  private String ftpPass;

  @Value("${application.ftp.folder}")
  private String ftpFolder;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  ShiftHandoverRepository shiftHandoverRepository;

  @Autowired
  ShiftCrRepository shiftCrRepository;

  @Autowired
  ShiftWorkRepository shiftWorkRepository;

  @Autowired
  ShiftStaftRepository shiftStaftRepository;

  @Autowired
  ShiftItRepository shiftItRepository;

  @Autowired
  ShiftItSeriousRepository shiftItSeriousRepository;

  @Autowired
  ShiftWorkOtherRepository shiftWorkOtherRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  TtServiceProxy ttServiceProxy;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Autowired
  UserRepository userRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  LogChangeConfigBusiness logChangeConfigBusiness;

  @Autowired
  CatItemRepository catItemRepository;

  Map<String, CatItemDTO> mapShift = new HashMap<>();

  @Override
  public ShiftHandoverDTO findShiftHandoverById(Long id) {
    log.debug("Request to findShiftHandoverById : {}", id);
    return shiftHandoverRepository.findShiftHandoverById(id);
  }

  @Override
  public Datatable getListShiftHandover(ShiftHandoverDTO shiftHandoverDTO) {
    log.debug("Request to getListShiftHandover : {}", shiftHandoverDTO);
    Datatable datatable = shiftHandoverRepository.getListShiftHandover(shiftHandoverDTO);
    List<ShiftHandoverDTO> list = (List<ShiftHandoverDTO>) datatable.getData();
    if (list != null && list.size() > 0) {
      for (ShiftHandoverDTO dto : list) {
        List<ShiftStaftDTO> dtoList = shiftStaftRepository.getListShiftStaftById(dto.getId());
        dto.setShiftStaftDTOList(dtoList);
      }
    }
    datatable.setData(list);
    return datatable;
  }

  @Override
  public File exportData(ShiftHandoverDTO dto) throws Exception {
    List<ShiftHandoverDTO> list = shiftHandoverRepository.getListShiftHandoverExport(dto);
    String[] header = new String[]{
        "userName", "unitName",
        "createdTime", "shiftName", "statusName", "lastUpdateTime"
    };
    return handleFileExport(list, header);
  }

  @Override
  public File exportShiftWorkData(ShiftWorkDTO shiftWorkDTO) throws Exception {
    List<ShiftWorkDTO> list = shiftHandoverRepository.getListShiftWorkByShiftID(shiftWorkDTO);
    String[] header = new String[]{
        "workName", "startTime", "deadLine", "owner", "handle", "importantLevel", "result",
        "nextWork", "process", "contact", "opinion"
    };
    return handleFileExportShiftWork(list, header);
  }

  @Override
  public File exportShiftCrData(ShiftCrDTO shiftCrDTO) throws Exception {
    List<ShiftCrDTO> list = shiftHandoverRepository.getListShiftCrkByShiftID(shiftCrDTO);
    String[] header = new String[]{
        "crNumber", "crName", "userName", "result", "note"
    };
    return handleFileExportShiftCR(list, header);
  }

  private File handleFileExportShiftCR(List<ShiftCrDTO> listExport, String[] columnExport)
      throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = I18n.getLanguage("shiftHandover.exportShiftCR.sheetname");
    String title = I18n.getLanguage("shiftHandover.exportShiftCR.title");
    String fileNameOut = I18n.getLanguage("shiftHandover.exportShiftCR.title");
    String subTitle = I18n
        .getLanguage("shiftHandover.exportShiftCR.eportDate", DateTimeUtils.convertDateOffset());
    ConfigFileExport configFileExport = new ConfigFileExport(
        listExport,
        sheetName,
        title,
        subTitle,
        7,
        3,
        6,
        true,
        "language.shiftHandover",
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
        I18n.getLanguage("shiftHandover.stt"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(fileTemplate, fileNameOut,
        fileExports, rootPath, null);
    return fileExport;
  }

  private File handleFileExportShiftWork(List<ShiftWorkDTO> listExport, String[] columnExport)
      throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = I18n.getLanguage("shiftHandover.exportShiftWork.sheetname");
    String title = I18n.getLanguage("shiftHandover.exportShiftWork.title");
    String fileNameOut = I18n.getLanguage("shiftHandover.exportShiftWork.title");
    String subTitle = I18n
        .getLanguage("shiftHandover.exportShiftWork.eportDate", DateTimeUtils.convertDateOffset());
    ConfigFileExport configFileExport = new ConfigFileExport(
        listExport,
        sheetName,
        title,
        subTitle,
        7,
        3,
        6,
        true,
        "language.shiftHandover",
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
        I18n.getLanguage("shiftHandover.stt"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(fileTemplate, fileNameOut,
        fileExports, rootPath, null);
    return fileExport;
  }

  private File handleFileExport(List<ShiftHandoverDTO> listExport, String[] columnExport)
      throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = I18n.getLanguage("shiftHandover.export.sheetname");
    String title = I18n.getLanguage("shiftHandover.export.title");
    String fileNameOut = I18n.getLanguage("shiftHandover.export.title");
    String subTitle = I18n
        .getLanguage("shiftHandover.export.eportDate", DateTimeUtils.convertDateOffset());
    ConfigFileExport configFileExport = new ConfigFileExport(
        listExport,
        sheetName,
        title,
        subTitle,
        7,
        3,
        6,
        true,
        "language.shiftHandover",
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
        I18n.getLanguage("shiftHandover.stt"), "HEAD", "STRING");
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

  @Override
  public List<ShiftHandoverDTO> getListShiftID() {
    return shiftHandoverRepository.getListShiftID();
  }

  public ResultInSideDto insertOrUpdateCfgShiftHandoverFile(String type, List<MultipartFile> files,
      UserToken userToken,
      UnitDTO unitToken, ShiftHandoverDTO shiftHandoverDTO) throws IOException {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    for (MultipartFile multipartFile : files) {
      String fullPath = FileUtils
          .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
              multipartFile.getBytes(), null);
      String fullPathOld = FileUtils
          .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
              uploadFolder, null);
      String fileName = multipartFile.getOriginalFilename();
      //Start save file old
      CommonFileDTO commonFileDTO = new CommonFileDTO();
      commonFileDTO.setFileName(FileUtils.getFileName(fullPathOld));
      commonFileDTO.setPath(FileUtils.getFilePath(fullPathOld));
      commonFileDTO.setCreateTime(new Date());
      commonFileDTO.setCreateUserId(userToken.getUserID());
      commonFileDTO.setCreateUserName(userToken.getUserName());
      ResultInSideDto resultFileDataOld = shiftHandoverRepository.insertCommonFile(commonFileDTO);
      ShiftHandoverFileDTO shiftHandoverFileDTO = new ShiftHandoverFileDTO();
      shiftHandoverFileDTO.setShiftHandoverId(shiftHandoverDTO.getId());
      shiftHandoverFileDTO.setFileId(resultFileDataOld.getId());
      shiftHandoverRepository.insertShiftHandOverFile(shiftHandoverFileDTO);
      //End save file old
      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setPath(fullPath);
      gnocFileDto.setFileName(fileName);
      gnocFileDto.setCreateUnitId(userToken.getDeptId());
      gnocFileDto.setCreateUnitName(unitToken.getUnitName());
      gnocFileDto.setCreateUserId(userToken.getUserID());
      gnocFileDto.setCreateUserName(userToken.getUserName());
      gnocFileDto.setCreateTime(commonFileDTO.getCreateTime());
      gnocFileDto.setMappingId(resultFileDataOld.getId());
      gnocFileDtos.add(gnocFileDto);
    }
    if ("UPDATE".equals(type)) {
//      List<GnocFileDto> gnocFileDtosAll = new ArrayList<>();
//      gnocFileDtosAll.addAll(gnocFileDtos);
//      gnocFileDtosAll.addAll(shiftHandoverDTO.getGnocFileDtos());
      gnocFileRepository
          .deleteListGnocFile(GNOC_FILE_BUSSINESS.SHIFT_HANDOVER, shiftHandoverDTO.getId(),
              shiftHandoverDTO.getIdDeleteList());
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SHIFT_HANDOVER,
              shiftHandoverDTO.getId(),
              gnocFileDtos);
    } else {
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SHIFT_HANDOVER,
              shiftHandoverDTO.getId(),
              gnocFileDtos);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertCfgShiftHandover(List<MultipartFile> files,
      ShiftHandoverDTO shiftHandoverDTO) throws IOException {
    Date date1 = new Date();
    if (shiftHandoverDTO.getCreatedTime().before(date1) || shiftHandoverDTO.getCreatedTime()
        .equals(date1)) {
      UserToken userToken = ticketProvider.getUserToken();
      UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
      shiftHandoverDTO.setLastUpdateTime(new Date());
      shiftHandoverDTO.setUserId(userToken.getUserID());
      shiftHandoverDTO.setUserName(userToken.getUserName());
      shiftHandoverDTO.setUnitId(userToken.getDeptId());
      shiftHandoverDTO.setUnitName(
          unitToken.getUnitName() + "(" + unitToken.getUnitCode() + ")");
      ResultInSideDto resultInSideDto = new ResultInSideDto();
      resultInSideDto.setKey(RESULT.SUCCESS);
      if (checkDuplicateRecord(shiftHandoverDTO)) {
        resultInSideDto = shiftHandoverRepository
            .insertOrUpdateCfgShiftHandover(shiftHandoverDTO);
      } else {
        resultInSideDto.setMessage(I18n.getLanguage("shifthandover.duplicate"));
        resultInSideDto.setKey(RESULT.DUPLICATE);
        return resultInSideDto;
      }
      Long idTemp = resultInSideDto.getId();

      if (shiftHandoverDTO.getShiftStaftDTOList() != null) {
        List<ShiftStaftDTO> list = shiftHandoverDTO.getShiftStaftDTOList();
        if (checkDuplicateStaftLine(list)) {
          for (ShiftStaftDTO dto : list) {
            dto.setShiftHandoverId(idTemp);
            shiftStaftRepository.insertShiftUser(dto);
          }
        } else {
          resultInSideDto.setMessage(I18n.getLanguage("shifthandover.duplicate.staft"));
          resultInSideDto.setKey(RESULT.ERROR);
        }
      }
      if (shiftHandoverDTO.getShiftWorkDTOList() != null) {
        List<ShiftWorkDTO> list = shiftHandoverDTO.getShiftWorkDTOList();
        for (ShiftWorkDTO dto : list) {
          dto.setShiftHandoverId(idTemp);
          resultInSideDto = shiftWorkRepository.insertShiftWork(dto);
        }
      }
      if (shiftHandoverDTO.getShiftItSeriousDTOList() != null) {
        List<ShiftItSeriousDTO> list = shiftHandoverDTO.getShiftItSeriousDTOList();
        for (ShiftItSeriousDTO dto : list) {
          dto.setShiftHandoverId(idTemp);
          resultInSideDto = shiftItSeriousRepository.insertShiftItSerious(dto);
        }
      }
      if (shiftHandoverDTO.getShiftItDTOList() != null) {
        List<ShiftItDTO> list = shiftHandoverDTO.getShiftItDTOList();
        for (ShiftItDTO dto : list) {
          dto.setShiftHandoverId(idTemp);
          resultInSideDto = shiftItRepository.insertShiftIt(dto);
        }
      }
      if (shiftHandoverDTO.getShiftCrDTOList() != null) {
        List<ShiftCrDTO> list = shiftHandoverDTO.getShiftCrDTOList();
        for (ShiftCrDTO dto : list) {
          dto.setShiftHandoverId(idTemp);
          resultInSideDto = shiftCrRepository.insertShiftCr(dto);
        }
      }
      if (shiftHandoverDTO.getShiftWorkOtherDTOList() != null) {
        List<ShiftWorkOtherDTO> list = shiftHandoverDTO.getShiftWorkOtherDTOList();
        for (ShiftWorkOtherDTO dto : list) {
          dto.setShiftHandoverId(idTemp);
          resultInSideDto = shiftWorkOtherRepository.insertShiftWorkOther(dto);
        }
      }
      if (files != null && files.size() > 0 && RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        shiftHandoverDTO.setId(idTemp);
        insertOrUpdateCfgShiftHandoverFile("INSERT", files, userToken, unitToken, shiftHandoverDTO);
      }
      shiftHandoverDTO.setId(null);
      resultInSideDto = logChangeConfigBusiness
          .insertLog(new LogChangeConfigDTO(userToken.getUserName(),
              "InsertCfgShiftHandover", "Add InsertCfgShiftHandover ID: " + idTemp,
              shiftHandoverDTO, null));
      return resultInSideDto;
    } else {
      ResultInSideDto resultInSideDto = new ResultInSideDto();
      resultInSideDto.setMessage(I18n.getLanguage("shiftHandover.overCreatedTime"));
      resultInSideDto.setKey(RESULT.ERROR);
      return resultInSideDto;
    }
  }

  private boolean checkDuplicateRecord(ShiftHandoverDTO shiftHandoverDTO) {
    boolean check = shiftHandoverRepository.checkDuplicateRecord(shiftHandoverDTO);
    return check;
  }

  private boolean checkDuplicateStaftLine(List<ShiftStaftDTO> list) {
    for (int i = 0; i < list.size(); i++) {
      ShiftStaftDTO dto1 = list.get(i);
      for (int j = i + 1; j < list.size(); j++) {
        ShiftStaftDTO dto2 = list.get(j);
        if (dto1.getAssignUserId().equals(dto2.getAssignUserId()) && dto1.getReceiveUserId()
            .equals(dto2.getReceiveUserId())) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public ResultInSideDto updateCfgShiftHandover(List<MultipartFile> files,
      ShiftHandoverDTO shiftHandoverDTO) throws IOException {
    Date date1 = new Date();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<ShiftStaftDTO> shiftStaftDTOS = shiftHandoverDTO.getShiftStaftDTOList();
    if (shiftHandoverDTO.getCreatedTime().after(date1)) {
      resultInSideDto.setMessage(I18n.getLanguage("shiftHandover.overCreatedTime"));
      resultInSideDto.setKey(RESULT.ERROR);
      return resultInSideDto;
    }
    if (shiftHandoverDTO.getOpenToolTip() != null && shiftHandoverDTO.getOpenToolTip()
        .equals(1L)) {
      shiftHandoverDTO.setStatus(1L);
    }
    resultInSideDto = checkRole(shiftStaftDTOS);
    if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      return resultInSideDto;
    }
    shiftHandoverDTO.setLastUpdateTime(new Date());
    if (shiftHandoverDTO.getOpenToolTip() == null) {
      if (!DateTimeUtils.convertDateToString(shiftHandoverDTO.getCreatedTimeOld())
          .equals(DateTimeUtils.convertDateToString(shiftHandoverDTO.getCreatedTime()))) {
        if (!checkDuplicateRecord(shiftHandoverDTO)) {
          resultInSideDto.setMessage(I18n.getLanguage("shifthandover.duplicate"));
          resultInSideDto.setKey(RESULT.DUPLICATE);
          return resultInSideDto;
        }
      }
    }
    resultInSideDto = shiftHandoverRepository.updateCfgShiftHandover(shiftHandoverDTO);

    if (shiftHandoverDTO.getOpenToolTip() == null) {
      if (shiftHandoverDTO.getShiftStaftDTOList() != null) {
        List<ShiftStaftDTO> list = shiftHandoverDTO.getShiftStaftDTOList();
        for (ShiftStaftDTO dto : list) {
          if (dto.getIsDeleteShiftStaft() && dto.getId() != null) {
            resultInSideDto = shiftStaftRepository.deleteShiftUser(dto.getId());
          } else {
            if (dto.getId() != null) {
              resultInSideDto = shiftStaftRepository.updateShiftUser(dto);
            } else {
              dto.setShiftHandoverId(shiftHandoverDTO.getId());
              resultInSideDto = shiftStaftRepository.insertShiftUser(dto);
            }
          }
        }
      }
    }
    if (shiftHandoverDTO.getShiftWorkDTOList() != null) {
      List<ShiftWorkDTO> list = shiftHandoverDTO.getShiftWorkDTOList();
      for (ShiftWorkDTO dto : list) {
        if (dto.getIsDeleteShiftWork() && dto.getId() != null) {
          resultInSideDto = shiftWorkRepository.deleteShiftWork(dto.getId());
        } else {
          if (dto.getId() != null) {
            resultInSideDto = shiftWorkRepository.updateShiftWork(dto);
          } else {
            dto.setShiftHandoverId(shiftHandoverDTO.getId());
            resultInSideDto = shiftWorkRepository.insertShiftWork(dto);
          }
        }
      }
    }
    if (shiftHandoverDTO.getShiftCrDTOList() != null) {
      List<ShiftCrDTO> list = shiftHandoverDTO.getShiftCrDTOList();
      for (ShiftCrDTO dto : list) {
        if (dto.getIsDeleteShiftCr() && dto.getId() != null) {
          resultInSideDto = shiftCrRepository.deleteShiftCr(dto.getId());
        } else {
          if (dto.getId() != null) {
            resultInSideDto = shiftCrRepository.updateShiftCr(dto);
          } else {
            dto.setShiftHandoverId(shiftHandoverDTO.getId());
            resultInSideDto = shiftCrRepository.insertShiftCr(dto);
          }
        }
      }
    }
    UserToken userToken = ticketProvider.getUserToken();
    if (files != null && files.size() > 0 && RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
      //Start delete file old
//      List<ShiftHandoverFileEntity> lstFileOld = shiftHandoverRepository
//          .findShiftHandOverFile(shiftHandoverDTO.getId());
//      List<Long> listIdFileIdOld = lstFileOld.stream()
//          .map(ShiftHandoverFileEntity::getFileId).collect(Collectors.toList());
//      List<GnocFileDto> gnocFileDtosWeb = shiftHandoverDTO.getGnocFileDtos();
//      List<Long> listIdFileIdNew = gnocFileDtosWeb.stream()
//          .map(GnocFileDto::getMappingId).collect(Collectors.toList());
//      listIdFileIdOld.removeAll(listIdFileIdNew);
      for (Long aLong : shiftHandoverDTO.getIdDeleteList()) {
        ShiftHandoverFileDTO shiftHandoverFileDTO = new ShiftHandoverFileDTO();
        shiftHandoverFileDTO.setFileId(aLong);
        shiftHandoverFileDTO.setShiftHandoverId(shiftHandoverDTO.getId());
        shiftHandoverRepository.deleteShiftHandOverFile(shiftHandoverFileDTO);
        CommonFileDTO commonFileDTO = new CommonFileDTO();
        commonFileDTO.setFileId(aLong);
        shiftHandoverRepository.deleteCommonFile(commonFileDTO);
      }
      //End delete file old
      insertOrUpdateCfgShiftHandoverFile("UPDATE", files, userToken, unitToken, shiftHandoverDTO);
    }
    resultInSideDto = logChangeConfigBusiness
        .insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "UpdateCfgShiftHandover", "UpdateCfgShiftHandover ID: " + shiftHandoverDTO.getId(),
            shiftHandoverDTO, null));
    return resultInSideDto;
  }

  @Override
  public ShiftHandoverDTO findListShiftHandOverById(Long id) {
    ShiftHandoverDTO shiftHandoverDTO = shiftHandoverRepository.findListShiftHandOverById(id);
    List<ShiftCrDTO> lstCr = shiftHandoverDTO.getShiftCrDTOList();
    if (lstCr != null && lstCr.size() > 0) {
      for (ShiftCrDTO dto : lstCr) {
        try {
          String[] arrCrNumber = dto.getCrNumber().split("_");
          String crId = arrCrNumber[arrCrNumber.length - 1];
          CrInsiteDTO crDTO = crServiceProxy.findCrByIdProxy(Long.valueOf(crId.trim()));
          if (crDTO == null) {
            continue;
          }

          dto.setCrName(crDTO.getTitle());
          dto.setStatusName(I18n.getChangeManagement(
              Constants.CR_STATE.getGetStateName().get(Long.valueOf(crDTO.getState()))));
          dto.setNote(crDTO.getNotes());
          if (!StringUtils.isStringNullOrEmpty(crDTO.getChangeResponsible())) {
            try {
              UsersEntity u = userRepository
                  .getUserByUserId(Long.valueOf(crDTO.getChangeResponsible()));
              dto.setChangeResponsible(
                  u == null ? "" : u.getUsername() == null ? "" : u.getUsername());
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    }
    return shiftHandoverDTO;
  }

  @Override
  public List<ShiftItDTO> countTicketByShift(ShiftHandoverDTO shiftHandoverDTO) {
    TroublesInSideDTO troublesInSideDTO = new TroublesInSideDTO();
    List<ShiftItDTO> lstShiftIt = new ArrayList<>();
    Datatable datatable = catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> lstKpi = (List<CatItemDTO>) datatable.getData();
    if (!DataUtil.isNullOrEmpty(shiftHandoverDTO.getShiftValue()) && !"-1"
        .equals(shiftHandoverDTO.getShiftValue())) {
      SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");
      Long shiftBefore = null;
      Date startTimeBefore = shiftHandoverDTO.getCreatedTime();
      setMapShift();
      if ("CA_1".equals(shiftHandoverDTO.getShiftValue())) {
        troublesInSideDTO.setStartTime(sp.format(shiftHandoverDTO.getCreatedTime()) + " 07:00:00");
        troublesInSideDTO.setEndTime(sp.format(shiftHandoverDTO.getCreatedTime()) + " 14:00:00");
        startTimeBefore = DateUtil.addDay(startTimeBefore, -1);
        startTimeBefore = DateUtil.setHour(startTimeBefore, 21, 0, 0);
        shiftBefore = mapShift.get("CA_3").getItemId();
      } else if ("CA_2".equals(shiftHandoverDTO.getShiftValue())) {
        troublesInSideDTO.setStartTime(sp.format(shiftHandoverDTO.getCreatedTime()) + " 14:00:00");
        troublesInSideDTO.setEndTime(sp.format(shiftHandoverDTO.getCreatedTime()) + " 21:00:00");
        startTimeBefore = DateUtil.setHour(startTimeBefore, 7, 0, 0);
        shiftBefore = mapShift.get("CA_1").getItemId();
      } else if ("CA_3".equals(shiftHandoverDTO.getShiftValue())) {
        troublesInSideDTO.setStartTime(sp.format(shiftHandoverDTO.getCreatedTime()) + " 21:00:00");
        Date end = DateUtils.addDays(shiftHandoverDTO.getCreatedTime(), 1);
        troublesInSideDTO.setEndTime(sp.format(end) + " 07:00:00");
        startTimeBefore = DateUtil.setHour(startTimeBefore, 14, 0, 0);
        shiftBefore = mapShift.get("CA_2").getItemId();
      }
      ShiftHandoverDTO dtoBefore = new ShiftHandoverDTO();
      dtoBefore.setUnitId(shiftHandoverDTO.getUnitId());
      dtoBefore.setCreatedTime(startTimeBefore);
      dtoBefore.setShiftId(shiftBefore);
      List<ShiftHandoverDTO> lstBefore = shiftHandoverRepository.getListShiftHandoverNew(dtoBefore);
      if (lstBefore != null && !lstBefore.isEmpty()) {
        dtoBefore = lstBefore.get(0);
      } else {
        dtoBefore = null;
      }
      troublesInSideDTO.setUnitId(StringUtils.validString(shiftHandoverDTO.getUnitId()) ? String
          .valueOf(shiftHandoverDTO.getUnitId()) : null);
      List<TroublesInSideDTO> lstTicket = ttServiceProxy.countTicketByShift(troublesInSideDTO);
      List<TroublesInSideDTO> lstEmergency = new ArrayList<>();
      List<TroublesInSideDTO> lstSerious = new ArrayList<>();
      List<TroublesInSideDTO> lstMedium = new ArrayList<>();
      if (lstTicket != null && !lstTicket.isEmpty()) {
        for (TroublesInSideDTO trouble : lstTicket) {
          if ("1952".equals(String.valueOf(trouble.getPriorityId()))) {
            lstEmergency.add(trouble);
          } else if ("1000".equals(String.valueOf(trouble.getPriorityId()))) {
            lstSerious.add(trouble);
          } else if ("61".equals(String.valueOf(trouble.getPriorityId()))) {
            lstMedium.add(trouble);
          }
        }
      }
      Double eightS = 0D;
      Double eightE = 0D;
      Double eightM = 0D;
      for (CatItemDTO item : lstKpi) {
        ShiftItDTO dto = new ShiftItDTO();
        dto.setKpi(item.getItemId());
        dto.setKpiName(item.getItemName());
        dto.setItemValue(item.getItemValue());
        dto.setNote("");
        dto.setTotal(0D);
        if ("3".equals(item.getItemValue())) { //So ticket phat sinh trong ca truc (3)
          dto.setEmergency(lstEmergency.get(0) != null ? lstEmergency.get(0).getTroubleId() : 0D);
          dto.setSerious(lstSerious.get(0) != null ? lstSerious.get(0).getTroubleId() : 0D);
          dto.setMedium(lstMedium.get(0) != null ? lstMedium.get(0).getTroubleId() : 0D);
          dto.setTotal((lstEmergency.get(0) != null ? lstEmergency.get(0).getTroubleId() : 0D)
              + (lstSerious.get(0) != null ? lstSerious.get(0).getTroubleId() : 0D)
              + (lstMedium.get(0) != null ? lstMedium.get(0).getTroubleId() : 0D));
        } else if ("5".equals(item.getItemValue())) { //Tong so ticket da xu ly trong ca (5)
          Double emergencyTmp5 =
              lstEmergency.get(0) != null && DataUtil.isNumber(lstEmergency.get(0).getTroubleCode())
                  ? Long.valueOf(lstEmergency.get(0).getTroubleCode()) : 0D;
          Double seriousTmp5 =
              lstSerious.get(0) != null && DataUtil.isNumber(lstSerious.get(0).getTroubleCode())
                  ? Long.valueOf(lstSerious.get(0).getTroubleCode()) : 0D;
          Double mediumTmp5 =
              lstMedium.get(0) != null && DataUtil.isNumber(lstMedium.get(0).getTroubleCode())
                  ? Long.valueOf(lstMedium.get(0).getTroubleCode()) : 0D;
          dto.setEmergency(emergencyTmp5);
          eightE = emergencyTmp5;
          dto.setSerious(seriousTmp5);
          eightS = seriousTmp5;
          dto.setMedium(mediumTmp5);
          eightM = mediumTmp5;
          dto.setTotal(emergencyTmp5 + seriousTmp5 + mediumTmp5);
        } else if ("6"
            .equals(item.getItemValue())) { //So ticket qua han ca truoc da xu ly trong ca (6)
          Double emergencyTmp6 =
              lstEmergency.get(0) != null && DataUtil.isNumber(lstEmergency.get(0).getTroubleName())
                  ? Long.valueOf(lstEmergency.get(0).getTroubleName()) : 0D;
          Double seriousTmp6 =
              lstSerious.get(0) != null && DataUtil.isNumber(lstSerious.get(0).getTroubleName())
                  ? Long.valueOf(lstSerious.get(0).getTroubleName()) : 0D;
          Double mediumTmp6 =
              lstMedium.get(0) != null && DataUtil.isNumber(lstMedium.get(0).getTroubleName())
                  ? Long.valueOf(lstMedium.get(0).getTroubleName()) : 0D;
          dto.setEmergency(emergencyTmp6);
          eightE = eightE - emergencyTmp6;
          dto.setSerious(seriousTmp6);
          eightS = eightS - seriousTmp6;
          dto.setMedium(mediumTmp6);
          eightM = eightM - mediumTmp6;
          dto.setTotal(emergencyTmp6 + seriousTmp6 + mediumTmp6);
        } else if ("7".equals(item.getItemValue())) { //So ticket qua han phat sinh moi trong ca (7)
          Double emergencyTmp7 =
              lstEmergency.get(0) != null && DataUtil.isNumber(lstEmergency.get(0).getDescription())
                  ? Long.valueOf(lstEmergency.get(0).getDescription()) : 0D;
          Double seriousTmp7 =
              lstSerious.get(0) != null && DataUtil.isNumber(lstSerious.get(0).getDescription())
                  ? Long.valueOf(lstSerious.get(0).getDescription()) : 0D;
          Double mediumTmp7 =
              lstMedium.get(0) != null && DataUtil.isNumber(lstMedium.get(0).getDescription())
                  ? Long.valueOf(lstMedium.get(0).getDescription()) : 0D;
          dto.setEmergency(emergencyTmp7);
          eightE = eightE - emergencyTmp7;
          dto.setSerious(seriousTmp7);
          eightS = eightS - seriousTmp7;
          dto.setMedium(mediumTmp7);
          eightM = eightM - mediumTmp7;
          dto.setTotal(emergencyTmp7 + seriousTmp7 + mediumTmp7);
        } else if ("9".equals(item.getItemValue())) { //So ticket ton qua han trong ca (9)
          Double emergencyTmp9 =
              lstEmergency.get(0) != null ? lstEmergency.get(0).getSubCategoryId() : 0D;
          Double seriousTmp9 =
              lstSerious.get(0) != null ? lstSerious.get(0).getSubCategoryId() : 0D;
          Double mediumTmp9 = lstMedium.get(0) != null ? lstMedium.get(0).getSubCategoryId() : 0D;
          dto.setEmergency(emergencyTmp9);
          dto.setSerious(seriousTmp9);
          dto.setMedium(mediumTmp9);
          dto.setTotal(emergencyTmp9 + seriousTmp9 + mediumTmp9);
        }
        if ("1".equals(item.getItemValue()) || "2"
            .equals(item.getItemValue())) {//(1) va (2) fill du lieu tu ca truoc
          List<ShiftItDTO> lstDTO = new ArrayList<>();
          if (dtoBefore != null) {
            ShiftItDTO shiftIt = new ShiftItDTO();
            shiftIt.setShiftHandoverId(dtoBefore.getId());
            if ("1".equals(item.getItemValue())) {
              shiftIt.setKpi(1387L);
            } else if ("2".equals(item.getItemValue())) {
              shiftIt.setKpi(1388L);
            }
//            shiftIt.setKpi(item.getItemId());
            lstDTO = shiftItRepository.getListShiftItByShiftID(shiftIt);
          }
          if (lstDTO != null && !lstDTO.isEmpty()) {
            dto.setEmergency(
                lstDTO.get(0).getEmergency() == null ? 0D : lstDTO.get(0).getEmergency());
            dto.setMedium(lstDTO.get(0).getMedium() == null ? 0D : lstDTO.get(0).getMedium());
            dto.setNote("");
            dto.setSerious(lstDTO.get(0).getSerious() == null ? 0D : lstDTO.get(0).getSerious());
            dto.setTotal(lstDTO.get(0).getTotal() == null ? 0D : lstDTO.get(0).getTotal());
          } else {
            dto.setEmergency(0D);
            dto.setMedium(0D);
            dto.setEmergency(0D);
            dto.setNote((""));
            dto.setSerious(0D);
            dto.setTotal(0D);
          }
        }
        lstShiftIt.add(dto);
      }
      //duyet lan nua de tinh toan
      Double emergencyFour = 0D;
      Double seriousFour = 0D;
      Double mediumFour = 0D;

      Double emergencyFive = 0D;
      Double seriousFive = 0D;
      Double mediumFive = 0D;

      eightE = eightE < 0D ? 0D : eightE;
      eightS = eightS < 0D ? 0D : eightS;
      eightM = eightM < 0D ? 0D : eightM;

      for (ShiftItDTO dto : lstShiftIt) {
        if ("1".equals(dto.getItemValue())) { //(1)
          emergencyFour += dto.getEmergency();
          seriousFour += dto.getSerious();
          mediumFour += dto.getMedium();
          continue;
        }
        if ("3".equals(dto.getItemValue())) { //(3)
          emergencyFour += dto.getEmergency();
          seriousFour += dto.getSerious();
          mediumFour += dto.getMedium();
          continue;
        }
        if ("4".equals(dto.getItemValue())) { //(4) = (1) + (3)
          dto.setEmergency(emergencyFour < 0D ? 0D : emergencyFour);
          dto.setSerious(seriousFour < 0D ? 0D : seriousFour);
          dto.setMedium(mediumFour < 0D ? 0D : mediumFour);
          dto.setTotal(emergencyFour + seriousFour + mediumFour);
          continue;
        }
        if ("5".equals(dto.getItemValue())) { //(5)
          emergencyFive += dto.getEmergency();
          seriousFive += dto.getSerious();
          mediumFive += dto.getMedium();
          continue;
        }
        if ("8"
            .equals(dto.getItemValue())) { //So ticket dung han da xu ly trong ca (8) = (5)-(6)-(7)
          dto.setEmergency(eightE);
          dto.setSerious(eightS);
          dto.setMedium(eightM);
          dto.setTotal(eightE + eightS + eightM);
          continue;
        }
        if ("10".equals(dto.getItemValue())) { //(10) = (4) - (5)
          dto.setEmergency(
              (emergencyFour - emergencyFive) < 0D ? 0D : (emergencyFour - emergencyFive));
          dto.setSerious((seriousFour - seriousFive) < 0D ? 0D : (seriousFour - seriousFive));
          dto.setMedium((mediumFour - mediumFive) < 0D ? 0D : (mediumFour - mediumFive));
          dto.setTotal(dto.getEmergency() + dto.getSerious() + dto.getMedium());
          continue;
        }
        if ("11".equals(dto.getItemValue())) { //(11) = (8)/(4)
          dto.setEmergency(emergencyFour == 0 ? 0D
              : (eightE / emergencyFour < 0D ? 0D : (eightE / emergencyFour)));
          dto.setSerious(
              seriousFour == 0 ? 0D : (eightS / seriousFour < 0D ? 0D : (eightS / seriousFour)));
          dto.setMedium(
              mediumFour == 0 ? 0D : (eightM / mediumFour < 0D ? 0D : (eightM / mediumFour)));
          dto.setTotal(dto.getEmergency() + dto.getSerious() + dto.getMedium());
        }
      }

    } else {
      for (CatItemDTO item : lstKpi) {
        ShiftItDTO dto = new ShiftItDTO();
        dto.setKpi(item.getItemId());
        dto.setKpiName(item.getItemName());
        lstShiftIt.add(dto);
      }
    }

    return lstShiftIt;
  }

  @Override
  public File exportShiftRow(ShiftHandoverDTO shiftHandoverDTO) throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String pathFileTemplate = "templates" + File.separator + "TEMPLATE_HANDOVER_EXPORT_ROW.xlsx";

    pathFileTemplate = StringUtils.removeSeparator(pathFileTemplate);
    Resource resource = new ClassPathResource(pathFileTemplate);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);
    Sheet sheetOne = workBook.getSheetAt(0);

    //Tieu de
    sheetOne.getRow(5).getCell(3).setCellValue(sheetOne.getRow(5).getCell(3).getStringCellValue()
        .replace("DD/MM/YYYY",
            DateTimeUtils.date2ddMMyyyyHHMMss(shiftHandoverDTO.getCreatedTime())));
    sheetOne.getRow(6).getCell(5).setCellValue(sheetOne.getRow(6).getCell(5).getStringCellValue()
        .replace("Unit", shiftHandoverDTO.getUnitName()));
    sheetOne.getRow(7).getCell(5).setCellValue(sheetOne.getRow(7).getCell(5).getStringCellValue()
        .replace("Shift", shiftHandoverDTO.getShiftName()).replace("DD/MM/YYYY",
            DateTimeUtils.date2ddMMyyyyHHMMss(shiftHandoverDTO.getCreatedTime())));

    //Ngay
    sheetOne.getRow(11).getCell(3).setCellValue(sheetOne.getRow(11).getCell(3).getStringCellValue()
        .replace("DD/MM/YYYY",
            DateTimeUtils.date2ddMMyyyyHHMMss(shiftHandoverDTO.getCreatedTime())));

    if (!"CA_3".equals(shiftHandoverDTO.getShiftValue())) {
      sheetOne.getRow(11).getCell(7).setCellValue(
          sheetOne.getRow(11).getCell(7).getStringCellValue().replace("DD/MM/YYYY",
              DateTimeUtils.date2ddMMyyyyHHMMss(shiftHandoverDTO.getCreatedTime())));
    } else {
      Date d = DateUtil.addDay(shiftHandoverDTO.getCreatedTime(), 1);
      sheetOne.getRow(11).getCell(7).setCellValue(
          sheetOne.getRow(11).getCell(7).getStringCellValue()
              .replace("DD/MM/YYYY", DateTimeUtils.date2ddMMyyyyHHMMss(d)));
    }
    //Ca
    sheetOne.getRow(12).getCell(3).setCellValue(shiftHandoverDTO.getShiftName());

    //lay ra danh sach ca
    Datatable datatable = catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);

    Map<String, CatItemDTO> mapShift = new HashMap<>();
    List<CatItemDTO> lstShift = (List<CatItemDTO>) datatable.getData();
    for (CatItemDTO c : lstShift) {
      mapShift.put(c.getItemValue(), c);
    }

    String shiftNameAfter = "";
    if ("CA_1".equals(shiftHandoverDTO.getShiftValue()) || 1341L == (shiftHandoverDTO
        .getShiftId())) {
      shiftNameAfter = mapShift.get("CA_2").getItemName();
    } else if ("CA_2".equals(shiftHandoverDTO.getShiftValue()) || 1342L == (shiftHandoverDTO
        .getShiftId())) {
      shiftNameAfter = mapShift.get("CA_3").getItemName();
    } else if ("CA_3".equals(shiftHandoverDTO.getShiftValue()) || 1343L == (shiftHandoverDTO
        .getShiftId())) {
      shiftNameAfter = mapShift.get("CA_1").getItemName();
    }

    sheetOne.getRow(12).getCell(7).setCellValue(shiftNameAfter);

    exportDataFromRow(shiftHandoverDTO, ewu, sheetOne);

    Date date = new Date();
    String pathByDate = FileUtils.createPathByDate(date);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    String filePath = tempFolder
        + File.separator + "ExportResult"
        + File.separator + pathByDate
        + File.separator;
    String fileName = "Export_Report_Handover" + "_" + System.currentTimeMillis() + ".xlsx";

    ewu.saveToFileExcel(workBook, filePath, fileName);

    return new File(filePath + fileName);
  }

  public void exportDataFromRow(ShiftHandoverDTO dtoExportRow, ExcelWriterUtils ewu,
      Sheet sheetOne) {
    try {
      //Nhan vien
      List<ShiftStaftDTO> lstStaff = shiftStaftRepository
          .getListShiftStaftById(dtoExportRow.getId());
      int i = 14;
      if (lstStaff != null && !lstStaff.isEmpty()) {
        int j = 1;
        for (ShiftStaftDTO dto : lstStaff) {
          ewu.shiftRow(sheetOne, i, 1);
          sheetOne.createRow(i);
          ewu.createCell(sheetOne.getRow(i), 1, String.valueOf(j));
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 1, 2));
          ewu.createCell(sheetOne.getRow(i), 3, dto.getAssignUserName());
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 3, 6));
          ewu.createCell(sheetOne.getRow(i), 7, dto.getReceiveUserName());
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 7, 10));
          i++;
          j++;
        }
      } else {
        ewu.shiftRow(sheetOne, i, 1);
        sheetOne.createRow(i);
        ewu.createCell(sheetOne.getRow(i), 1, I18n.getLanguage("no.data"));
        sheetOne.addMergedRegion(new CellRangeAddress(i, i, 1, 14));
        i++;
      }

      //Cong viec ban giao ca
      i = i + 4;
      ShiftWorkDTO shiftWorkDTO = new ShiftWorkDTO();
      shiftWorkDTO.setShiftHandoverId(dtoExportRow.getId());
      List<ShiftWorkDTO> lstWork = shiftWorkRepository.getListShiftWorkByShiftID(shiftWorkDTO);
      if (lstWork != null && !lstWork.isEmpty()) {
        int j = 1;
        for (ShiftWorkDTO dto : lstWork) {
          ewu.shiftRow(sheetOne, i, 1);
          sheetOne.createRow(i);
          ewu.createCell(sheetOne.getRow(i), 1, String.valueOf(j));
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 2, 3));
          ewu.createCell(sheetOne.getRow(i), 2, dto.getDescription());
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 4, 6));
          ewu.createCell(sheetOne.getRow(i), 4,
              DateTimeUtils.date2ddMMyyyyHHMMss(dto.getStartTime()));
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 7, 8));
          ewu.createCell(sheetOne.getRow(i), 7,
              DateTimeUtils.date2ddMMyyyyHHMMss(dto.getDeadLine()));
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 9, 10));
          ewu.createCell(sheetOne.getRow(i), 9, dto.getOwner());
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 11, 12));
          ewu.createCell(sheetOne.getRow(i), 11, dto.getHandle());
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 13, 14));
          ewu.createCell(sheetOne.getRow(i), 13, dto.getImportantLevel());
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 15, 16));
          ewu.createCell(sheetOne.getRow(i), 15, dto.getResult());
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 17, 18));
          ewu.createCell(sheetOne.getRow(i), 17, dto.getNextWork());
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 19, 20));
          ewu.createCell(sheetOne.getRow(i), 19, dto.getContact());
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 21, 22));
          ewu.createCell(sheetOne.getRow(i), 21, dto.getWorkStatus());
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 23, 24));
          ewu.createCell(sheetOne.getRow(i), 23, dto.getOpinion());
          i++;
          j++;
        }
      } else {
        ewu.shiftRow(sheetOne, i, 1);
        sheetOne.createRow(i);
        ewu.createCell(sheetOne.getRow(i), 1, I18n.getLanguage("no.data"));
        sheetOne.addMergedRegion(new CellRangeAddress(i, i, 1, 23));
        i++;
      }

      //Tiep nhan va xu ly ticket
      i = i + 4;
      List<ShiftItDTO> lstIt = new ArrayList<>();
      Datatable datatable = catItemBusiness.getItemMaster(Constants.CATEGORY.GNOC_SHIFT_KPI,
          LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
          Constants.ITEM_ID, Constants.ITEM_NAME);
      List<CatItemDTO> lstKpi = (List<CatItemDTO>) datatable.getData();
      if (lstKpi != null && !lstKpi.isEmpty()) {
        for (CatItemDTO item : lstKpi) {
          ShiftItDTO dto = new ShiftItDTO();
          dto.setKpi(item.getItemId());
          dto.setKpiName(item.getItemName());
          dto.setShiftHandoverId(dtoExportRow.getId());
          List<ShiftItDTO> lstDTO = shiftItRepository.getListShiftItByShiftID(dto);
          if (lstDTO == null) {

          } else if (!lstDTO.isEmpty()) {
            dto.setEmergency(
                lstDTO.get(0).getEmergency() == null ? 0D : lstDTO.get(0).getEmergency());
            dto.setMedium(lstDTO.get(0).getMedium() == null ? 0D : lstDTO.get(0).getMedium());
            dto.setNote(lstDTO.get(0).getNote() == null ? "" : lstDTO.get(0).getNote());
            dto.setSerious(lstDTO.get(0).getSerious() == null ? 0D : lstDTO.get(0).getSerious());
            dto.setTotal(lstDTO.get(0).getTotal() == null ? 0D : lstDTO.get(0).getTotal());
          }
          lstIt.add(dto);
        }
      }
      if (!lstIt.isEmpty()) {
        int j = 1;
        for (ShiftItDTO dto : lstIt) {
          ewu.shiftRow(sheetOne, i, 1);
          sheetOne.createRow(i);
          ewu.createCell(sheetOne.getRow(i), 1, String.valueOf(j));
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 2, 5));
          ewu.createCell(sheetOne.getRow(i), 2, dto.getKpiName());
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 6, 7));
          ewu.createCell(sheetOne.getRow(i), 6,
              StringUtils.isDoubleNullOrEmpty(dto.getEmergency()) ? "0"
                  : dto.getEmergency().toString());
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 8, 9));
          ewu.createCell(sheetOne.getRow(i), 8,
              StringUtils.isDoubleNullOrEmpty(dto.getSerious()) ? "0"
                  : dto.getSerious().toString());
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 10, 11));
          ewu.createCell(sheetOne.getRow(i), 10,
              StringUtils.isDoubleNullOrEmpty(dto.getMedium()) ? "0" : dto.getMedium().toString());
          sheetOne.addMergedRegion(new CellRangeAddress(i, i, 12, 13));
          ewu.createCell(sheetOne.getRow(i), 12,
              StringUtils.isDoubleNullOrEmpty(dto.getTotal()) ? "0" : dto.getTotal().toString());

          ewu.createCell(sheetOne.getRow(i), 14, dto.getNote());
          i++;
          j++;
        }
      } else {
        ewu.shiftRow(sheetOne, i, 1);
        sheetOne.createRow(i);
        ewu.createCell(sheetOne.getRow(i), 1, I18n.getLanguage("no.data"));
        sheetOne.addMergedRegion(new CellRangeAddress(i, i, 1, 14));
        i++;
      }

      //CR
      i = i + 3;
      sheetOne.createRow(i);
      ShiftCrDTO shiftCrDTO = new ShiftCrDTO();
      shiftCrDTO.setShiftHandoverId(dtoExportRow.getId());
      List<ShiftCrDTO> lstCR = shiftCrRepository.getListShiftCrByShiftID(shiftCrDTO);
      if (lstCR != null && !lstCR.isEmpty()) {
        int j = 1;
        for (ShiftCrDTO dto : lstCR) {
          try {
            if (!StringUtils.isStringNullOrEmpty(dto.getCrNumber())) {
              ewu.shiftRow(sheetOne, i, 1);
              sheetOne.createRow(i);
              ewu.createCell(sheetOne.getRow(i), 1, String.valueOf(j));
              sheetOne.addMergedRegion(new CellRangeAddress(i, i, 2, 3));
              ewu.createCell(sheetOne.getRow(i), 2, dto.getCrNumber());

              String[] arrCrNumber = dto.getCrNumber().split("_");
              String crId = arrCrNumber[arrCrNumber.length - 1];
              CrInsiteDTO crDTO = crServiceProxy.findCrByIdProxy(Long.valueOf(crId.trim()));
              if (crDTO == null) {
                continue;
              }

              dto.setCrName(crDTO.getTitle());
              dto.setStatusName(I18n.getChangeManagement(
                  Constants.CR_STATE.getGetStateName().get(Long.valueOf(crDTO.getState()))));
              dto.setNote(crDTO.getNotes());
              if (!StringUtils.isStringNullOrEmpty(crDTO.getChangeResponsible())) {
                try {
                  UsersEntity u = userRepository
                      .getUserByUserId(Long.valueOf(crDTO.getChangeResponsible()));
                  dto.setChangeResponsible(
                      u == null ? "" : u.getUsername() == null ? "" : u.getUsername());
                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                }
              }

              sheetOne.addMergedRegion(new CellRangeAddress(i, i, 4, 5));
              ewu.createCell(sheetOne.getRow(i), 4, dto.getCrName());
              sheetOne.addMergedRegion(new CellRangeAddress(i, i, 6, 7));
              ewu.createCell(sheetOne.getRow(i), 6, dto.getChangeResponsible());
              sheetOne.addMergedRegion(new CellRangeAddress(i, i, 8, 9));
              ewu.createCell(sheetOne.getRow(i), 8, dto.getStatusName());
              sheetOne.addMergedRegion(new CellRangeAddress(i, i, 10, 11));
              ewu.createCell(sheetOne.getRow(i), 10, dto.getNote());
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
          i++;
          j++;
        }
      } else {
        ewu.shiftRow(sheetOne, i, 1);
        sheetOne.createRow(i);
        ewu.createCell(sheetOne.getRow(i), 1, I18n.getLanguage("no.data"));
        sheetOne.addMergedRegion(new CellRangeAddress(i, i, 1, 10));
//        i++;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      System.out.println("Exception while export row: " + e.getMessage());
    }
  }

  @Override
  public ResultInSideDto findCrByCrNumber(ShiftCrDTO shiftCrDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.ERROR);
    resultInSideDto.setMessage(Constants.RESULT.ERROR);
    if (shiftCrDTO == null) {
      resultInSideDto.setMessage(I18n.getLanguage("shifthandover.crRequired"));
      return resultInSideDto;
    }
    try {
      String[] arrCrNumber = shiftCrDTO.getCrNumber().split("_");
      String crId = arrCrNumber[arrCrNumber.length - 1];
      CrInsiteDTO crDTO = crServiceProxy.findCrByIdProxy(Long.valueOf(crId.trim()));
      if (crDTO == null) {
        resultInSideDto.setMessage(I18n.getLanguage("shifthandover.crNotFound"));
        return resultInSideDto;
      }

      Date crCreateDate = crDTO.getCreatedDate();
      Date now = new Date();
      long time = DateUtil.minusDate(now, crCreateDate);
      if ((time / (1000 * 60 * 60 * 24)) > 90) {
        //neu CR tao qua 90 ngay thi return
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getLanguage("shifthandover.crOver90Days"));
        return resultInSideDto;
      }

      shiftCrDTO.setCrName(crDTO.getTitle());
      shiftCrDTO.setStatusName(I18n.getChangeManagement(
          Constants.CR_STATE.getGetStateName().get(Long.valueOf(crDTO.getState()))));
      shiftCrDTO.setNote(crDTO.getNotes());
      if (!StringUtils.isStringNullOrEmpty(crDTO.getChangeResponsible())) {
        try {
          UsersEntity u = userRepository
              .getUserByUserId(Long.valueOf(crDTO.getChangeResponsible()));
          shiftCrDTO.setChangeResponsible(
              u == null ? "" : u.getUsername() == null ? "" : u.getUsername());
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
      resultInSideDto.setMessage(Constants.RESULT.SUCCESS);
      resultInSideDto.setObject(shiftCrDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setMessage(I18n.getLanguage("shifthandover.crNotFound"));
    }
    return resultInSideDto;
  }

  private ResultInSideDto checkRole(List<ShiftStaftDTO> shiftStaftDTOS) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    for (ShiftStaftDTO dto2 : shiftStaftDTOS) {
      if (!dto2.getIsDeleteShiftStaft()) {
        UsersEntity userAssign = userRepository
            .getUserByUserId(dto2.getAssignUserId());
        if (!userAssign.getUnitId().equals(userToken.getDeptId())) {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("shifthandover.check.AssignUser"));
          return resultInSideDto;
        }
        UsersEntity userReceive = userRepository
            .getUserByUserId(dto2.getReceiveUserId());
        if (!userReceive.getUnitId().equals(userAssign.getUnitId())) {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("shifthandover.check.ReceiveUser"));
          return resultInSideDto;
        }
      }
    }
    return resultInSideDto;
  }

  private File handleFile(List<ShiftWorkDTO> listExportDto, String key, Long check)
      throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    String sheetName = I18n.getLanguage("shiftwork.export.title");
    String title = I18n.getLanguage("shiftwork.export.title");
    String subTitle;
    int startRow = 8;
    String fileNameOut;
    String[] header;

    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = SHIFT_WORK_CREATE_RESULT_IMPORT;
      subTitle = I18n.getLanguage("shiftwork.export.import",
          DateUtil.date2ddMMyyyyHHMMss(DateTimeUtils.convertDateOffset()));
      header = new String[]{"description", (check == 1L) ? "startTime" : "startTimeError",
          (check == 1L) ? "deadLine" : "deadLineError", "owner", "handle", "importantLevel",
          "result",
          "nextWork", "contact", "workStatus", "opinion", "resultImport"};
    } else {
      fileNameOut = SHIFT_WORK_CREATE_EXPORT;
      subTitle = I18n
          .getLanguage("shiftwork.export.eportDate",
              DateUtil.date2ddMMyyyyHHMMss(DateTimeUtils.convertDateOffset()));
      header = new String[]{"description", (check == 1L) ? "startTime" : "startTimeError",
          (check == 1L) ? "deadLine" : "deadLineError", "owner", "handle", "importantLevel",
          "result",
          "nextWork", "contact", "workStatus", "opinion"};
    }
    List<ConfigHeaderExport> headerExportList = getListHeaderSheet(header);

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        listExportDto,
        sheetName,
        title,
        subTitle,
        startRow,
        4,
        6,
        true,
        "language.shiftwork",
        headerExportList,
        fieldSplit,
        "",
        null,
        null,
        null,
        null
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(startRow, 0, 0, 0,
        I18n.getLanguage("shiftwork.stt"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);

    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = doExportExcel(fileTemplate, fileNameOut,
        fileExports, rootPath, null);
    return fileExport;
  }

  public File doExportExcel(String pathTemplate, String fileNameOut, List<ConfigFileExport> config,
      String pathOut,
      String... exportChart
  ) throws Exception {
    File folderOut = new File(pathOut);
    if (!folderOut.exists()) {
      folderOut.mkdir();
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat();
    dateFormat.applyPattern("dd/MM/yyyy HH:mm:ss");
    String strCurTimeExp = dateFormat.format(new Date());
    strCurTimeExp = strCurTimeExp.replaceAll("/", "_");
    strCurTimeExp = strCurTimeExp.replaceAll(" ", "_");
    strCurTimeExp = strCurTimeExp.replaceAll(":", "_");
    pathOut = pathOut + fileNameOut + strCurTimeExp + (exportChart != null && exportChart.length > 0
        ? ".xlsm" : ".xlsx");
    HSSFWorkbook hwb = null;
    try {
      log.info("Start get template file!");
      pathTemplate = StringUtils.removeSeparator(pathTemplate);
      Resource resource = new ClassPathResource(pathTemplate);
      InputStream fileTemplate = resource.getInputStream();
      XSSFWorkbook workbook_temp = new XSSFWorkbook(fileTemplate);
      log.info("End get template file!");
      SXSSFWorkbook workbook = new SXSSFWorkbook(workbook_temp, 1000);
      hwb = new HSSFWorkbook();

      //<editor-fold defaultstate="collapsed" desc="Declare style">
      CellStyle cellStyleFormatNumber = workbook.createCellStyle();
      cellStyleFormatNumber.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
      cellStyleFormatNumber.setAlignment(HorizontalAlignment.RIGHT);
      cellStyleFormatNumber.setBorderLeft(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderBottom(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderRight(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderTop(BorderStyle.THIN);

      CellStyle cellStyle = workbook.createCellStyle();
      cellStyle.setAlignment(HorizontalAlignment.CENTER);
      cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
      cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyle.setWrapText(true);

      Font xSSFFont = workbook.createFont();
      xSSFFont.setFontName(HSSFFont.FONT_ARIAL);
      xSSFFont.setFontHeightInPoints((short) 20);
      xSSFFont.setBold(true);
      xSSFFont.setColor(IndexedColors.BLACK.index);

      CellStyle cellStyleTitle = workbook.createCellStyle();
      cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
      cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleTitle.setFillForegroundColor(IndexedColors.WHITE.index);
      cellStyleTitle.setFont(xSSFFont);

      Font xSSFFontHeader = workbook.createFont();
      xSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
      xSSFFontHeader.setFontHeightInPoints((short) 10);
      xSSFFontHeader.setColor(IndexedColors.BLUE.index);
      xSSFFontHeader.setBold(true);

      Font subTitleFont = workbook.createFont();
      subTitleFont.setFontName(HSSFFont.FONT_ARIAL);
      subTitleFont.setFontHeightInPoints((short) 10);
      subTitleFont.setColor(IndexedColors.BLACK.index);

      Font xssFontTopHeader = workbook.createFont();
      xssFontTopHeader.setFontName("Times New Roman");
      xssFontTopHeader.setFontHeightInPoints((short) 10);
      xssFontTopHeader.setColor(IndexedColors.BLACK.index);
      xssFontTopHeader.setBold(true);

      Font rowDataFont = workbook.createFont();
      rowDataFont.setFontName(HSSFFont.FONT_ARIAL);
      rowDataFont.setFontHeightInPoints((short) 10);
      rowDataFont.setColor(IndexedColors.BLACK.index);

      CellStyle cellStyleTopHeader = workbook.createCellStyle();
      cellStyleTopHeader.setAlignment(HorizontalAlignment.CENTER);
      cellStyleTopHeader.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleTopHeader.setFont(xssFontTopHeader);

      CellStyle cellStyleTopRightHeader = workbook.createCellStyle();
      cellStyleTopRightHeader.setAlignment(HorizontalAlignment.LEFT);
      cellStyleTopRightHeader.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleTopRightHeader.setFont(xssFontTopHeader);

      CellStyle cellStyleSubTitle = workbook.createCellStyle();
      cellStyleSubTitle.setAlignment(HorizontalAlignment.CENTER);
      cellStyleSubTitle.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleSubTitle.setFont(subTitleFont);

      CellStyle cellStyleHeader = workbook.createCellStyle();
      cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
      cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleHeader.setBorderLeft(BorderStyle.THIN);
      cellStyleHeader.setBorderBottom(BorderStyle.THIN);
      cellStyleHeader.setBorderRight(BorderStyle.THIN);
      cellStyleHeader.setBorderTop(BorderStyle.THIN);
      cellStyleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
      cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyleHeader.setWrapText(true);
      cellStyleHeader.setFont(xSSFFontHeader);

      CellStyle cellStyleLeft = workbook.createCellStyle();
      cellStyleLeft.setAlignment(HorizontalAlignment.LEFT);
      cellStyleLeft.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleLeft.setBorderLeft(BorderStyle.THIN);
      cellStyleLeft.setBorderBottom(BorderStyle.THIN);
      cellStyleLeft.setBorderRight(BorderStyle.THIN);
      cellStyleLeft.setBorderTop(BorderStyle.THIN);
      cellStyleLeft.setWrapText(true);
      cellStyleLeft.setFont(rowDataFont);
      CellStyle cellStyleRight = workbook.createCellStyle();
      cellStyleRight.setAlignment(HorizontalAlignment.RIGHT);
      cellStyleRight.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleRight.setBorderLeft(BorderStyle.THIN);
      cellStyleRight.setBorderBottom(BorderStyle.THIN);
      cellStyleRight.setBorderRight(BorderStyle.THIN);
      cellStyleRight.setBorderTop(BorderStyle.THIN);
      cellStyleRight.setWrapText(true);
      cellStyleRight.setFont(rowDataFont);

      CellStyle cellStyleCenter = workbook.createCellStyle();
      cellStyleCenter.setAlignment(HorizontalAlignment.CENTER);
      cellStyleCenter.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleCenter.setBorderLeft(BorderStyle.THIN);
      cellStyleCenter.setBorderBottom(BorderStyle.THIN);
      cellStyleCenter.setBorderRight(BorderStyle.THIN);
      cellStyleCenter.setBorderTop(BorderStyle.THIN);
      cellStyleCenter.setWrapText(true);
      cellStyleRight.setFont(rowDataFont);

      CellStyle right = workbook.createCellStyle();
      right.setAlignment(HorizontalAlignment.RIGHT);
      right.setVerticalAlignment(VerticalAlignment.CENTER);
      right.setWrapText(true);

      CellStyle left = workbook.createCellStyle();
      left.setAlignment(HorizontalAlignment.LEFT);
      left.setVerticalAlignment(VerticalAlignment.CENTER);
      left.setWrapText(true);

      CellStyle center = workbook.createCellStyle();
      center.setAlignment(HorizontalAlignment.CENTER);
      center.setVerticalAlignment(VerticalAlignment.CENTER);
      center.setWrapText(true);
      //</editor-fold>

      for (ConfigFileExport item : config) {

        Map<String, String> fieldSplit = item.getFieldSplit();
        SXSSFSheet sheet;
        if (exportChart != null && exportChart.length > 0) {
          sheet = workbook.getSheetAt(0);
        } else {
          sheet = workbook.createSheet(item.getSheetName());
        }

        if (item.getCellTitleIndex() >= 3) {
          //Tao quoc hieu
          String firstLeftHeaderTitle = I18n.getLanguage("shiftwork.temp.firstLeftHeader");
          String secondLeftHeaderTitle = I18n.getLanguage("shiftwork.temp.secondLeftHeader");
          String thirdLeftHeaderTitle = I18n.getLanguage("shiftwork.temp.thirdLeftHeader");
          String firstRightHeaderTitle = I18n.getLanguage("shiftwork.temp.firstRightHeader");
          String secondRightHeaderTitle = I18n.getLanguage("shiftwork.temp.secondRightHeader");

          Row headerFirstTitle = sheet.createRow(0);
          Row headerSecondTitle = sheet.createRow(1);
          Row headerThirdTitle = sheet.createRow(2);
          int sizeheaderTitle = 7;
          Cell firstLeftHeader = headerFirstTitle.createCell(1);
          firstLeftHeader.setCellStyle(cellStyleTopHeader);
          Cell secondLeftHeader = headerSecondTitle.createCell(1);
          secondLeftHeader.setCellStyle(cellStyleTopHeader);
          Cell thirdLeftHeader = headerThirdTitle.createCell(1);
          thirdLeftHeader.setCellStyle(cellStyleTopHeader);
          Cell firstRightHeader = headerFirstTitle.createCell(sizeheaderTitle - 2);
          firstRightHeader.setCellStyle(cellStyleTopRightHeader);
          Cell secondRightHeader = headerSecondTitle.createCell(sizeheaderTitle - 2);
          secondRightHeader.setCellStyle(cellStyleTopRightHeader);
          firstLeftHeader.setCellValue(firstLeftHeaderTitle);
          secondLeftHeader.setCellValue(secondLeftHeaderTitle);
          thirdLeftHeader.setCellValue(thirdLeftHeaderTitle);
          firstRightHeader.setCellValue(firstRightHeaderTitle);
          secondRightHeader.setCellValue(secondRightHeaderTitle);
          sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 3));
          sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 3));
          sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));
          sheet.addMergedRegion(new CellRangeAddress(0, 0, sizeheaderTitle - 2,
              sizeheaderTitle + 1));
          sheet.addMergedRegion(new CellRangeAddress(1, 1, sizeheaderTitle - 2,
              sizeheaderTitle + 1));
        }

        // Title
        Row rowMainTitle = sheet.createRow(item.getCellTitleIndex());

        Cell mainCellTitle;
        if (item.getCustomTitle() != null && item.getCustomTitle().length > 0) {
          mainCellTitle = rowMainTitle.createCell(0);
        } else {
          mainCellTitle = rowMainTitle.createCell(1);
        }
        mainCellTitle.setCellValue(item.getTitle() == null ? "" : item.getTitle());
        // Sub title
        int indexSubTitle =
            (StringUtils.isStringNullOrEmpty(item.getSubTitle())) ? item.getCellTitleIndex() + 1
                : item.getCellTitleIndex() + 2;
        mainCellTitle.setCellStyle(cellStyleTitle);
        sheet.addMergedRegion(
            new CellRangeAddress(item.getCellTitleIndex(), item.getCellTitleIndex(), 1,
                item.getMergeTitleEndIndex()));
        Row rowSubTitle = sheet.createRow(indexSubTitle);
        Cell cellTitle = rowSubTitle.createCell(1);
        cellTitle.setCellValue(item.getSubTitle() == null ? "" : item.getSubTitle());
        cellTitle.setCellStyle(cellStyleSubTitle);
        sheet.addMergedRegion(
            new CellRangeAddress(indexSubTitle, indexSubTitle, 1,
                item.getMergeTitleEndIndex()));

        int indexRowData = 0;
        //<editor-fold defaultstate="collapsed" desc="Build header">
        if (item.isCreatHeader()) {
          int index = -1;
          Cell cellHeader;
          Row rowHeader = sheet.createRow(item.getStartRow());
          rowHeader.setHeight((short) 900);
          Row rowHeaderSub = null;
          for (ConfigHeaderExport header : item.getHeader()) {
            if (fieldSplit != null) {
              if (fieldSplit.get(header.getFieldName()) != null) {
                String[] fieldSplitHead = fieldSplit.get(header.getFieldName())
                    .split(item.getSplitChar());
                for (String field : fieldSplitHead) {
                  cellHeader = rowHeader.createCell(index + 2);
                  cellHeader.setCellValue(field == null ? "" : field.replaceAll("\\<.*?>", " "));
                  if (header.isHasMerge()) {
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(
                        item.getStartRow(), item.getStartRow() + header.getMergeRow(),
                        index + 2, index + 2 + header.getMergeColumn());
                    sheet.addMergedRegion(cellRangeAddress);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress,
                        sheet);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress,
                        sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress,
                        sheet);
                    RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress,
                        sheet);

                    if (header.getMergeRow() > 0) {
                      indexRowData = header.getMergeRow();
                    }
                    if (header.getMergeColumn() > 0) {
                      index++;
                    }

                    if (header.getSubHeader().length > 0) {
                      if (rowHeaderSub == null) {
                        rowHeaderSub = sheet.createRow(item.getStartRow() + 1);
                      }

                      int k = index + 1;
                      int s = 0;
                      for (String sub : header.getSubHeader()) {
                        Cell cellHeaderSub1 = rowHeaderSub.createCell(k);
                        cellHeaderSub1.setCellValue(
                            I18n.getString(item.getHeaderPrefix() + "." + sub));
                        cellHeaderSub1.setCellStyle(cellStyleHeader);

                        k++;
                        s++;
                      }
                    }
                  }
                  cellHeader.setCellStyle(cellStyleHeader);
                  index++;
                }
              } else {
                cellHeader = rowHeader.createCell(index + 2);
                cellHeader.setCellValue(
                    I18n.getString(item.getHeaderPrefix() + "." + header.getFieldName()));
                if (header.isHasMerge()) {
                  CellRangeAddress cellRangeAddress = new CellRangeAddress(
                      item.getStartRow(), item.getStartRow() + header.getMergeRow(),
                      index + 2, index + 2 + header.getMergeColumn());
                  sheet.addMergedRegion(cellRangeAddress);
                  RegionUtil
                      .setBorderBottom(BorderStyle.THIN, cellRangeAddress,
                          sheet);
                  RegionUtil
                      .setBorderLeft(BorderStyle.THIN, cellRangeAddress,
                          sheet);
                  RegionUtil
                      .setBorderRight(BorderStyle.THIN, cellRangeAddress,
                          sheet);
                  RegionUtil
                      .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);

                  if (header.getMergeRow() > 0) {
                    indexRowData = header.getMergeRow();
                  }
                  if (header.getMergeColumn() > 0) {
                    index++;
                  }
                }
                cellHeader.setCellStyle(cellStyleHeader);
                index++;
              }
            } else {
              cellHeader = rowHeader.createCell(index + 2);
              cellHeader.setCellValue(
                  I18n.getString(item.getHeaderPrefix() + "." + header.getFieldName()));
              if (header.isHasMerge()) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(item.getStartRow(),
                    item.getStartRow() + header.getMergeRow(), index + 2,
                    index + 2 + header.getMergeColumn());
                sheet.addMergedRegion(cellRangeAddress);
                RegionUtil
                    .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil
                    .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil
                    .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);

                if (header.getMergeRow() > 0) {
                  indexRowData = header.getMergeRow();
                }
                if (header.getMergeColumn() > 0) {
                  index++;
                }
              }
              cellHeader.setCellStyle(cellStyleHeader);
              index++;
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Build other cell">
        if (item.getLstCreatCell() != null) {
          Row row;
          for (CellConfigExport cell : item.getLstCreatCell()) {
            row = sheet.getRow(cell.getRow());
            if (row == null) {
              row = sheet.createRow(cell.getRow());
            }
            //row.setHeight((short) -1);
            Cell newCell = row.createCell(cell.getColumn());
            if ("NUMBER".equals(cell.getStyleFormat())) {
              newCell.setCellValue(Double.valueOf(cell.getValue()));
            } else {
              newCell.setCellValue(cell.getValue() == null ? "" : cell.getValue());
            }

            if (cell.getRowMerge() > 0 || cell.getColumnMerge() > 0) {
              CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRow(),
                  cell.getRow() + cell.getRowMerge(), cell.getColumn(),
                  cell.getColumn() + cell.getColumnMerge());
              sheet.addMergedRegion(cellRangeAddress);
              RegionUtil
                  .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
            }

            if ("HEAD".equals(cell.getAlign())) {
              newCell.setCellStyle(cellStyleHeader);
            }
            if ("CENTER".equals(cell.getAlign())) {
              newCell.setCellStyle(cellStyleCenter);
            }
            if ("LEFT".equals(cell.getAlign())) {
              newCell.setCellStyle(cellStyleLeft);
            }
            if ("RIGHT".equals(cell.getAlign())) {
              newCell.setCellStyle(cellStyleRight);
            }
            if ("CENTER_NONE_BORDER".equals(cell.getAlign())) {
              newCell.setCellStyle(center);
            }
            if ("LEFT_NONE_BORDER".equals(cell.getAlign())) {
              newCell.setCellStyle(left);
            }
            if ("RIGHT_NONE_BORDER".equals(cell.getAlign())) {
              newCell.setCellStyle(right);
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Fill data">
        if (item.getLstData() != null && !item.getLstData().isEmpty()) {
          //init mapColumn
          Object firstRow = item.getLstData().get(0);
          Map<String, Field> mapField = new HashMap<String, Field>();
          for (ConfigHeaderExport header : item.getHeader()) {
            for (Field f : firstRow.getClass().getDeclaredFields()) {
              f.setAccessible(true);
              if (f.getName().equals(header.getFieldName())) {
                mapField.put(header.getFieldName(), f);
              }
              String[] replace = header.getReplace();
              if (replace != null) {
                if (replace.length > 2) {
                  for (int n = 2; n < replace.length; n++) {
                    if (f.getName().equals(replace[n])) {
                      mapField.put(replace[n], f);
                    }
                  }
                }
              }
            }
            if (firstRow.getClass().getSuperclass() != null) {
              for (Field f : firstRow.getClass()
                  .getSuperclass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.getName().equals(header.getFieldName())) {
                  mapField.put(header.getFieldName(), f);
                }
                String[] replace = header.getReplace();
                if (replace != null) {
                  if (replace.length > 2) {
                    for (int n = 2; n < replace.length; n++) {
                      if (f.getName().equals(replace[n])) {
                        mapField.put(replace[n], f);
                      }
                    }
                  }
                }
              }
            }
          }

          //fillData
          Row row;
          List lstData = item.getLstData();
          List<ConfigHeaderExport> lstHeader = item.getHeader();
          int startRow = item.getStartRow();
          String splitChar = item.getSplitChar();
          for (int i = 0; i < lstData.size(); i++) {
            row = sheet.createRow(i + startRow + 1 + indexRowData);
            row.setHeight((short) 250);
            //row.setHeight((short) -1);
            Cell cell;

            cell = row.createCell(0);
            cell.setCellValue(i + 1);
            cell.setCellStyle(cellStyleCenter);

            int j = 0;
            for (int e = 0; e < lstHeader.size(); e++) {
              ConfigHeaderExport head = lstHeader.get(e);
              String header = head.getFieldName();
              String align = head.getAlign();
              Object obj = lstData.get(i);

              Field f = mapField.get(header);

              if (fieldSplit != null && fieldSplit.containsKey(header)) {
                String[] arrHead = fieldSplit.get(header).split(splitChar);
                String value = "";
                Object tempValue = f.get(obj);
                if (tempValue instanceof Date) {
                  value = tempValue == null ? "" : DateUtil.date2ddMMyyyyHHMMss((Date) tempValue);
                } else {
                  value = tempValue == null ? "" : tempValue.toString();
                }

                String[] fieldSplitValue = value.split(splitChar);
                for (int m = 0; m < arrHead.length; m++) {
                  if (head.isHasMerge() && head.getSubHeader().length > 0) {
                    int s = 0;
                    for (String sub : head.getSubHeader()) {
                      cell = row.createCell(j + 1);
                      String[] replace = head.getReplace();
                      if (replace != null) {
                        List<String> more = new ArrayList<>();
                        if (replace.length > 2) {
                          for (int n = 2; n < replace.length; n++) {
                            Object objStr = mapField.get(replace[n]).get(obj);
                            String valueStr = objStr == null ? "" : objStr.toString();
                            more.add(valueStr);
                          }
                        }
                        if ("NUMBER".equals(head.getStyleFormat())) {
                          double numberValue = replaceNumberValue(replace[0], m,
                              more, s);
                          if (Double.compare(numberValue, -888) == 0) {
                            cell.setCellValue("*");
                          } else if (Double.compare(numberValue, -999) == 0) {
                            cell.setCellValue("-");
                          } else {
                            cell.setCellValue(numberValue);
                          }
                        } else {
                          cell.setCellValue(replaceStringValue(replace[0], m, more, s));
                        }
                        s++;
                      } else {
                        String subValue = "";
                        for (Field subf : firstRow.getClass().getDeclaredFields()) {
                          subf.setAccessible(true);
                          if (subf.getName().equals(sub)) {
                            String[] arrSub = (subf.get(obj) == null ? "" : subf.get(
                                obj).toString()).split(item.getSplitChar());
                            subValue = arrSub[m];
                          }
                        }
                        if ("NUMBER".equals(head.getStyleFormat())) {
                          if (StringUtils.isNotNullOrEmpty(subValue)) {
                            cell.setCellValue(Double.valueOf(subValue));
                          } else {
                            cell.setCellValue(subValue == null ? "" : subValue);
                          }
                        } else {
                          if (subValue == null) {
                            cell.setCellValue("");
                          } else if (subValue.length() > 32767) {
                            cell.setCellValue(subValue.substring(0, 32766));
                          } else {
                            cell.setCellValue(subValue);//cut cho nay
                          }
                        }
                      }

                      if ("CENTER".equals(align)) {
                        cell.setCellStyle(cellStyleCenter);
                      }
                      if ("LEFT".equals(align)) {
                        cell.setCellStyle(cellStyleLeft);
                      }
                      if ("RIGHT".equals(align)) {
                        cell.setCellStyle(cellStyleRight);
                      }
                      j++;
                    }
                  } else {
                    if (item.getCustomColumnWidth() != null
                        && item.getCustomColumnWidth().length > 0) {
                      if (j > 0) {
                        j++;
                      }
                      cell = row.createCell(j + 1);
                    } else {
                      cell = row.createCell(j + 1);
                    }
                    String[] replace = head.getReplace();
                    if (replace != null) {
                      Object valueReplace = mapField.get(replace[1]).get(obj);
                      List<String> more = new ArrayList<>();
                      if (replace.length > 2) {
                        for (int n = 2; n < replace.length; n++) {
                          Object objStr = mapField.get(replace[n]).get(obj);
                          String valueStr = objStr == null ? "" : objStr.toString();
                          more.add(valueStr);
                        }
                      }
                      if ("NUMBER".equals(head.getStyleFormat())) {
                        double numberValue = replaceNumberValue(replace[0],
                            valueReplace, more, m);
                        if (Double.compare(numberValue, -888) == 0) {
                          cell.setCellValue("*");
                        } else if (Double.compare(numberValue, -999) == 0) {
                          cell.setCellValue("-");
                        } else {
                          cell.setCellValue(numberValue);
                        }
                      } else {
                        cell.setCellValue(replaceStringValue(replace[0], valueReplace, more, m));
                      }
                    } else {
                      if ("NUMBER".equals(head.getStyleFormat())) {
                        if (StringUtils.isNotNullOrEmpty(fieldSplitValue[m])) {
                          cell.setCellValue(Double.valueOf(fieldSplitValue[m]));
                        } else {
                          cell.setCellValue(fieldSplitValue[m] == null ? "" : fieldSplitValue[m]);
                        }
                      } else {
                        cell.setCellValue(fieldSplitValue[m] == null ? "" : fieldSplitValue[m]);
                      }
                    }

                    if ("CENTER".equals(align)) {
                      cell.setCellStyle(cellStyleCenter);
                    }
                    if ("LEFT".equals(align)) {
                      cell.setCellStyle(cellStyleLeft);
                    }
                    if ("RIGHT".equals(align)) {
                      cell.setCellStyle(cellStyleRight);
                    }
                    j++;
                  }
                }
              } else {
                String value = "";
                if (f != null) {
                  Object tempValue = f.get(obj);
                  if (tempValue instanceof Date) {
                    value = tempValue == null ? "" : DateUtil.date2ddMMyyyyHHMMss((Date) tempValue);
                  } else {
                    value = tempValue == null ? "" : tempValue.toString();
                  }
                }
                if (item.getCustomColumnWidth() != null && item.getCustomColumnWidth().length > 0) {
                  if (j > 0) {
                    j++;
                  }
                  cell = row.createCell(j + 1);
                } else {
                  cell = row.createCell(j + 1);
                }

                String[] replace = head.getReplace();
                if (replace != null) {
                  Object valueReplace = mapField.get(replace[1]).get(obj);
                  List<String> more = new ArrayList<>();
                  if (replace.length > 2) {
                    for (int n = 2; n < replace.length; n++) {
                      Object objStr = mapField.get(replace[n]).get(obj);
                      String valueStr = objStr == null ? "" : objStr.toString();
                      more.add(valueStr);
                    }
                  }
                  if ("NUMBER".equals(head.getStyleFormat())) {
                    double numberValue = replaceNumberValue(replace[0], valueReplace, more,
                        i);
                    if (Double.compare(numberValue, -888) == 0) {
                      cell.setCellValue("*");
                    } else if (Double.compare(numberValue, -999) == 0) {
                      cell.setCellValue("-");
                    } else {
                      cell.setCellValue(numberValue);
                    }
                  } else {
                    cell.setCellValue(
                        replaceStringValue(replace[0], valueReplace, more, i));
                  }
                } else {
                  if ("NUMBER".equals(head.getStyleFormat())) {
                    if (StringUtils.isNotNullOrEmpty(value)) {
                      cell.setCellValue(Double.valueOf(value));
                    } else {
                      cell.setCellValue(value == null ? "" : value);
                    }
                  } else {
                    if (value == null) {
                      cell.setCellValue("");
                    } else if (value.length() > 32767) {
                      cell.setCellValue(value.substring(0, 32766));
                    } else {
                      cell.setCellValue(value);//cut cho nay
                    }
                  }
                }

                if ("CENTER".equals(align)) {
                  cell.setCellStyle(cellStyleCenter);
                }
                if ("LEFT".equals(align)) {
                  cell.setCellStyle(cellStyleLeft);
                }
                if ("RIGHT".equals(align)) {
                  cell.setCellStyle(cellStyleRight);
                }

                j++;
              }
            }
            if (item.getCustomColumnWidth() != null && item.getCustomColumnWidth().length > 0) {
              int b = 1;
              int size = 0;
              if (item.getCustomColumnWidth().length % 2 == 0) {
                size = item.getCustomColumnWidth().length / 2;
              } else {
                size = (item.getCustomColumnWidth().length / 2) + 1;
              }
              for (int a = 1; a < size; a++) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(row.getRowNum(),
                    row.getRowNum(), b, b + 1);
                if (b == 1) {
                  b = a + 2;
                } else {
                  b += 2;
                }
                sheet.addMergedRegion(cellRangeAddress);
                RegionUtil
                    .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil
                    .setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil
                    .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil
                    .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
              }
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Merge row">
        if (item.getLstCellMerge() != null) {
          for (CellConfigExport cell : item.getLstCellMerge()) {
            if (cell.getRowMerge() > 0 || cell.getColumnMerge() > 0) {
              CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRow(),
                  cell.getRow() + cell.getRowMerge(), cell.getColumn(),
                  cell.getColumn() + cell.getColumnMerge());
              sheet.addMergedRegion(cellRangeAddress);
              RegionUtil
                  .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Auto size column">
        sheet.trackAllColumnsForAutoSizing();
        for (int i = 0; i <= item.getHeader().size(); i++) {
          sheet.autoSizeColumn(i);
          if (sheet.getColumnWidth(i) > 20000) {
            sheet.setColumnWidth(i, 20000);
          }
        }
        //</editor-fold>
        if (item.getCustomColumnWidth() != null && item.getCustomColumnWidth().length > 0) {
          for (int i = 0; i <= item.getCustomColumnWidth().length - 1; i++) {
            sheet.autoSizeColumn(i);
            String[] columnWidth = item.getCustomColumnWidth();
            sheet.setColumnWidth(i, Integer.parseInt(columnWidth[i]));
          }
        }
        if (item.getMapCustomColumnWidth() != null) {
          for (int i : item.getMapCustomColumnWidth().keySet()) {
            sheet.setColumnWidth(i, item.getMapCustomColumnWidth().get(i));
          }
        }
        //tiennv hide column
        List<Integer> lsColumnHidden = item.getLsColumnHidden();
        if (lsColumnHidden != null) {
          for (Integer index : lsColumnHidden) {
            sheet.setColumnHidden(index, true);
          }
        }
      }

      if (exportChart == null || exportChart.length == 0) {
        workbook.removeSheetAt(0);
      }

      if (exportChart != null && exportChart.length > 0) {
        //<editor-fold defaultstate="collapsed" desc="Ve bieu do">
        ConfigFileExport item = config.get(0);
        Sheet sheetConf = workbook_temp.getSheet("conf");

        // Dong bat dau du lieu cua chart
        Row rowStartConf = sheetConf.getRow(0);
        Cell cellStartConf = rowStartConf.getCell(1);
        cellStartConf.setCellValue(item.getStartRow() + 1);

        // Dong ket thuc du lieu cua chart
        Row rowEndConf = sheetConf.getRow(1);
        Cell cellEndConf = rowEndConf.getCell(1);
        cellEndConf.setCellValue(item.getStartRow() + 1 + item.getLstData().size());

        // Cot bat dau du lieu cua chart
        String xStart = "";

        // Cot ket thuc du lieu cua chart
        String xEnd = "";

        // xAxis
        Row rowXvalue = sheetConf.getRow(2);
        Cell cellXvalue = rowXvalue.getCell(1);
        cellXvalue.setCellValue("=" + I18n.getLanguage("common.export.char.total",
            Locale.forLanguageTag("vi")) + "!$" + xStart + "${startRow}:$" + xEnd + "${startRow}");

        // Categories
        Row rowNameList = sheetConf.getRow(3);
        Cell cellNameList = rowNameList.getCell(1);
        cellNameList.setCellValue("=" + I18n.getLanguage("common.export.char.total",
            Locale.forLanguageTag("vi")) + "!$" + exportChart[0] + "${i}");

        // Data
        Row rowDataValue = sheetConf.getRow(4);
        Cell cellDataValue = rowDataValue.getCell(1);
        cellDataValue.setCellValue("=" + I18n.getLanguage("common.export.char.total",
            Locale.forLanguageTag("vi")) + "!$" + xStart + "${i}:$" + xEnd + "${i}");
        //</editor-fold>
      }

      try {
        FileOutputStream fileOut = new FileOutputStream(pathOut);
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    } catch (FileNotFoundException e) {
      log.error(e.getMessage(), e);
    } finally {
      if (hwb != null) {
        try {
          hwb.close();
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
        }
      }
    }
    return new File(pathOut);
  }

  public double replaceNumberValue(String modul, Object valueReplace, List<String> more,
      int index) {
    double valueReturn = 0;
//    String strValue = valueReplace == null ? "" : valueReplace.toString();
//    DecimalFormat df = new DecimalFormat("#.##");
    return valueReturn;
  }

  public String replaceStringValue(String modul, Object valueReplace, List<String> more,
      int index) {
    String strReturn = "";
//    String strValue = valueReplace == null ? "" : valueReplace.toString();
    return strReturn;
  }

  private ShiftWorkDTO validateImportShiftWorkInfo(ShiftWorkDTO exportDTO,
      List<ShiftWorkDTO> listExportDto) {
    if (StringUtils.isStringNullOrEmpty(exportDTO.getDescription())) {
      exportDTO.setResultImport(I18n.getLanguage("shiftWork.err.workName"));
      return exportDTO;
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getOwner())) {
      exportDTO.setResultImport(I18n.getLanguage("shiftWork.err.owner"));
      return exportDTO;
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getHandle())) {
      exportDTO.setResultImport(I18n.getLanguage("shiftWork.err.handle"));
      return exportDTO;
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getImportantLevel())) {
      exportDTO.setResultImport(I18n.getLanguage("shiftWork.err.importantLevel"));
      return exportDTO;
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getResult())) {
      exportDTO.setResultImport(I18n.getLanguage("shiftWork.err.result"));
      return exportDTO;
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getNextWork())) {
      exportDTO.setResultImport(I18n.getLanguage("shiftWork.err.nextWork"));
      return exportDTO;
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getContact())) {
      exportDTO.setResultImport(I18n.getLanguage("shiftWork.err.contact"));
      return exportDTO;
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getWorkStatus())) {
      exportDTO.setResultImport(I18n.getLanguage("shiftWork.err.workStatus"));
      return exportDTO;
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getOpinion())) {
      exportDTO.setResultImport(I18n.getLanguage("shiftWork.err.opinion"));
      return exportDTO;
    }
    if (exportDTO.getOwner().length() > 100) {
      exportDTO.setResultImport(I18n.getLanguage("shiftWork.err.owner.tooLong"));
      return exportDTO;
    }
    if (exportDTO.getHandle().length() > 100) {
      exportDTO.setResultImport(I18n.getLanguage("shiftWork.err.handle.tooLong"));
      return exportDTO;
    }
    if (exportDTO.getImportantLevel().length() > 100) {
      exportDTO.setResultImport(I18n.getLanguage("shiftWork.err.importantLevel.tooLong"));
      return exportDTO;
    }
    if (exportDTO.getResult().length() > 100) {
      exportDTO.setResultImport(I18n.getLanguage("shiftWork.err.result.tooLong"));
      return exportDTO;
    }
    if (exportDTO.getNextWork().length() > 100) {
      exportDTO.setResultImport(I18n.getLanguage("shiftWork.err.nextWork.tooLong"));
      return exportDTO;
    }
    if (exportDTO.getContact().length() > 1000) {
      exportDTO.setResultImport(I18n.getLanguage("shiftWork.err.contact.tooLong"));
      return exportDTO;
    }
    if (exportDTO.getWorkStatus().length() > 100) {
      exportDTO.setResultImport(I18n.getLanguage("shiftWork.err.workStatus.tooLong"));
      return exportDTO;
    }
    if (exportDTO.getOpinion().length() > 2000) {
      exportDTO.setResultImport(I18n.getLanguage("shiftWork.err.opinion.tooLong"));
      return exportDTO;
    }
    if (listExportDto != null && listExportDto.size() > 0) {
      exportDTO = validateDuplicateShiftWork(listExportDto, exportDTO);
    }
    return exportDTO;
  }

  private ShiftWorkDTO validateDuplicateShiftWork(List<ShiftWorkDTO> list,
      ShiftWorkDTO shiftWorkDTO) {
    try {
      for (int i = 0; i < list.size(); i++) {
        ShiftWorkDTO shiftWorkDTOTemp = list.get(i);
        if (shiftWorkDTOTemp.getOwner() == null) {
          shiftWorkDTOTemp.setOwner("");
        }
        if (shiftWorkDTOTemp.getHandle() == null) {
          shiftWorkDTOTemp.setHandle("");
        }
        if (shiftWorkDTOTemp.getImportantLevel() == null) {
          shiftWorkDTOTemp.setImportantLevel("");
        }
        if (shiftWorkDTOTemp.getResult() == null) {
          shiftWorkDTOTemp.setResult("");
        }
        if (shiftWorkDTOTemp.getNextWork() == null) {
          shiftWorkDTOTemp.setNextWork("");
        }
        if (shiftWorkDTOTemp.getContact() == null) {
          shiftWorkDTOTemp.setContact("");
        }
        if (shiftWorkDTOTemp.getWorkStatus() == null) {
          shiftWorkDTOTemp.setWorkStatus("");
        }
        if (shiftWorkDTOTemp.getOpinion() == null) {
          shiftWorkDTOTemp.setOpinion("");
        }
        if (shiftWorkDTOTemp.getOwner().equals(shiftWorkDTO.getOwner()) && shiftWorkDTOTemp
            .getHandle().equals(shiftWorkDTO.getHandle()) && shiftWorkDTOTemp.getImportantLevel()
            .equals(shiftWorkDTO.getImportantLevel()) && shiftWorkDTOTemp.getResult()
            .equals(shiftWorkDTO.getResult()) && shiftWorkDTOTemp.getNextWork()
            .equals(shiftWorkDTO.getNextWork()) && shiftWorkDTOTemp.getContact()
            .equals(shiftWorkDTO.getContact()) && shiftWorkDTOTemp.getWorkStatus()
            .equals(shiftWorkDTO.getWorkStatus()) && shiftWorkDTOTemp.getOpinion()
            .equals(shiftWorkDTO.getOpinion())) {
          shiftWorkDTO.setResultImport(I18n.getLanguage("shiftwork.err.dup-code-in-file")
              .replaceAll("0", String.valueOf((i) + 1)));
          break;
        }
      }
      return shiftWorkDTO;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  private boolean validFileFormatShiftWork(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 12) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("shiftwork.stt")).equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("shiftwork.workName") + " (*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("shiftwork.startTime")).equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("shiftwork.deadLine")).equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("shiftwork.owner") + " (*)")
        .equalsIgnoreCase(obj[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("shiftwork.handle") + " (*)")
        .equalsIgnoreCase(obj[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("shiftwork.importantLevel") + " (*)")
        .equalsIgnoreCase(obj[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("shiftwork.result") + " (*)")
        .equalsIgnoreCase(obj[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("shiftwork.nextWork") + " (*)")
        .equalsIgnoreCase(obj[8].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("shiftwork.contact") + " (*)")
        .equalsIgnoreCase(obj[9].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("shiftwork.workStatus") + " (*)")
        .equalsIgnoreCase(obj[10].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("shiftwork.opinion") + " (*)")
        .equalsIgnoreCase(obj[11].toString().trim())) {
      return false;
    }
    return true;
  }


  public Long getSequenseShiftHandover(String seqName, int size) {
    List<String> strings = shiftHandoverRepository.getSequenseShiftHandover(seqName, size);
    Long shiftHandoverId = Long.valueOf(strings.get(0));
    return shiftHandoverId;
  }

  @Override
  public File getTemplate() throws IOException {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workBook.getSheetAt(0);
    String[] header = new String[]{
        I18n.getLanguage("shiftwork.stt"),
        I18n.getLanguage("shiftwork.workName"),
        I18n.getLanguage("shiftwork.startTime"),
        I18n.getLanguage("shiftwork.deadLine"),
        I18n.getLanguage("shiftwork.owner"),
        I18n.getLanguage("shiftwork.handle"),
        I18n.getLanguage("shiftwork.importantLevel"),
        I18n.getLanguage("shiftwork.result"),
        I18n.getLanguage("shiftwork.nextWork"),
        I18n.getLanguage("shiftwork.contact"),
        I18n.getLanguage("shiftwork.workStatus"),
        I18n.getLanguage("shiftwork.opinion")};

    String[] headerStart = new String[]{
        I18n.getLanguage("shiftwork.workName"),
        I18n.getLanguage("shiftwork.owner"),
        I18n.getLanguage("shiftwork.handle"),
        I18n.getLanguage("shiftwork.importantLevel"),
        I18n.getLanguage("shiftwork.result"),
        I18n.getLanguage("shiftwork.nextWork"),
        I18n.getLanguage("shiftwork.contact"),
        I18n.getLanguage("shiftwork.workStatus"),
        I18n.getLanguage("shiftwork.opinion")
    };

    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStart);
    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    int sttColumn = listHeader.indexOf(I18n.getLanguage("shiftwork.stt"));
    int workNameColumn = listHeader.indexOf(I18n.getLanguage("shiftwork.workName"));
    int startTimeColumn = listHeader.indexOf(I18n.getLanguage("shiftwork.startTime"));
    int deadLineColumn = listHeader.indexOf(I18n.getLanguage("shiftwork.deadLine"));
    int ownerColumn = listHeader.indexOf(I18n.getLanguage("shiftwork.owner"));
    int handleColumn = listHeader.indexOf(I18n.getLanguage("shiftwork.handle"));
    int importantLevelColumn = listHeader.indexOf(I18n.getLanguage("shiftwork.importantLevel"));
    int resultColumn = listHeader.indexOf(I18n.getLanguage("shiftwork.result"));
    int nextWorkColumn = listHeader.indexOf(I18n.getLanguage("shiftwork.nextWork"));
    int contactColumn = listHeader.indexOf(I18n.getLanguage("shiftwork.contact"));
    int workStatusColumn = listHeader.indexOf(I18n.getLanguage("shiftwork.workStatus"));
    int opinionColumn = listHeader.indexOf(I18n.getLanguage("shiftwork.opinion"));

    String firstLeftHeaderTitle = I18n.getLanguage("shiftwork.temp.firstLeftHeader");
    String secondLeftHeaderTitle = I18n.getLanguage("shiftwork.temp.secondLeftHeader");
    String thirdLeftHeaderTitle = I18n.getLanguage("shiftwork.temp.thirdLeftHeader");
    String firstRightHeaderTitle = I18n.getLanguage("shiftwork.temp.firstRightHeader");
    String secondRightHeaderTitle = I18n.getLanguage("shiftwork.temp.secondRightHeader");

    CellStyle cellStyleTopHeader = CommonExport.setCellStyleTopHeader(workBook);
    CellStyle cellStyleTopRightHeader = CommonExport.setCellStyleTopRightHeader(workBook);
    CellStyle cellStyleTitle = CommonExport.setCellStyleTitle(workBook);
    CellStyle cellStyleHeader = CommonExport.setCellStyleHeader(workBook);

    //Tao quoc hieu
    Row headerFirstTitle = sheetOne.createRow(0);
    Row headerSecondTitle = sheetOne.createRow(1);
    Row headerThirdTitle = sheetOne.createRow(2);
    int sizeheaderTitle = 7;
    Cell firstLeftHeader = headerFirstTitle.createCell(1);
    firstLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell secondLeftHeader = headerSecondTitle.createCell(1);
    secondLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell thirdLeftHeader = headerThirdTitle.createCell(1);
    thirdLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell firstRightHeader = headerFirstTitle.createCell(sizeheaderTitle - 2);
    firstRightHeader.setCellStyle(cellStyleTopRightHeader);
    Cell secondRightHeader = headerSecondTitle.createCell(sizeheaderTitle - 2);
    secondRightHeader.setCellStyle(cellStyleTopRightHeader);
    firstLeftHeader.setCellValue(firstLeftHeaderTitle);
    secondLeftHeader.setCellValue(secondLeftHeaderTitle);
    thirdLeftHeader.setCellValue(thirdLeftHeaderTitle);
    firstRightHeader.setCellValue(firstRightHeaderTitle);
    secondRightHeader.setCellValue(secondRightHeaderTitle);
    sheetOne.addMergedRegion(new CellRangeAddress(0, 0, 1, 3));
    sheetOne.addMergedRegion(new CellRangeAddress(1, 1, 1, 3));
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));
    sheetOne.addMergedRegion(new CellRangeAddress(0, 0, sizeheaderTitle - 2,
        sizeheaderTitle + 1));
    sheetOne.addMergedRegion(new CellRangeAddress(1, 1, sizeheaderTitle - 2,
        sizeheaderTitle + 1));

    //Tao tieu de
    Row rowMainTitle = sheetOne.createRow(4);
    Cell mainCellTitle = rowMainTitle.createCell(1);
    mainCellTitle.setCellValue(I18n.getLanguage("shiftwork.template.title").toUpperCase());
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetOne.addMergedRegion(new CellRangeAddress(4, 4, 1, 7));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao header
    Row headerRow = sheetOne.createRow(6);
    headerRow.setHeight((short) 900);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeaderStar : listHeaderStar) {
        if (checkHeaderStar.equalsIgnoreCase(listHeader.get(i))) {
          rt.append(" (*)", fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(cellStyleHeader);
    }

    sheetOne.setColumnWidth(sttColumn, 2000);
    sheetOne.setColumnWidth(workNameColumn, 8000);
    sheetOne.setColumnWidth(startTimeColumn, 6000);
    sheetOne.setColumnWidth(deadLineColumn, 6000);
    sheetOne.setColumnWidth(ownerColumn, 6000);
    sheetOne.setColumnWidth(handleColumn, 6000);
    sheetOne.setColumnWidth(importantLevelColumn, 6000);
    sheetOne.setColumnWidth(resultColumn, 7000);
    sheetOne.setColumnWidth(nextWorkColumn, 8000);
    sheetOne.setColumnWidth(contactColumn, 5000);
    sheetOne.setColumnWidth(workStatusColumn, 6000);
    sheetOne.setColumnWidth(opinionColumn, 6000);

    workBook.setSheetName(0, I18n.getLanguage("shiftwork.temp.title"));

    String fileResult = tempFolder + File.separator;
    String fileName = "BM_IMPORT_SHIFT_WORK" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public File getTemplateCR() throws IOException {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workBook.getSheetAt(0);

    String[] header = new String[]{
        I18n.getLanguage("shiftcr.stt"),
        I18n.getLanguage("shiftcr.crNumber")};

    String[] headerStart = new String[]{
        I18n.getLanguage("shiftcr.crNumber")
    };

    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStart);
    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    int sttColumn = listHeader.indexOf(I18n.getLanguage("shiftcr.stt"));
    int crNumberColumn = listHeader.indexOf(I18n.getLanguage("shiftcr.crNumber"));

    String firstLeftHeaderTitle = I18n.getLanguage("shiftwork.temp.firstLeftHeader");
    String secondLeftHeaderTitle = I18n.getLanguage("shiftwork.temp.secondLeftHeader");
    String thirdLeftHeaderTitle = I18n.getLanguage("shiftwork.temp.thirdLeftHeader");
    String firstRightHeaderTitle = I18n.getLanguage("shiftwork.temp.firstRightHeader");
    String secondRightHeaderTitle = I18n.getLanguage("shiftwork.temp.secondRightHeader");

    CellStyle cellStyleTopHeader = CommonExport.setCellStyleTopHeader(workBook);
    CellStyle cellStyleTopRightHeader = CommonExport.setCellStyleTopRightHeader(workBook);
    CellStyle cellStyleTitle = CommonExport.setCellStyleTitle(workBook);
    CellStyle cellStyleSubTitle = CommonExport.setCellStyleSubTitle(workBook);
    CellStyle cellStyleHeader = CommonExport.setCellStyleHeader(workBook);

    //Tao quoc hieu
    Row headerFirstTitle = sheetOne.createRow(0);
    Row headerSecondTitle = sheetOne.createRow(1);
    Row headerThirdTitle = sheetOne.createRow(2);
    int sizeheaderTitle = 7;
    Cell firstLeftHeader = headerFirstTitle.createCell(1);
    firstLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell secondLeftHeader = headerSecondTitle.createCell(1);
    secondLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell thirdLeftHeader = headerThirdTitle.createCell(1);
    thirdLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell firstRightHeader = headerFirstTitle.createCell(sizeheaderTitle - 2);
    firstRightHeader.setCellStyle(cellStyleTopRightHeader);
    Cell secondRightHeader = headerSecondTitle.createCell(sizeheaderTitle - 2);
    secondRightHeader.setCellStyle(cellStyleTopRightHeader);
    firstLeftHeader.setCellValue(firstLeftHeaderTitle);
    secondLeftHeader.setCellValue(secondLeftHeaderTitle);
    thirdLeftHeader.setCellValue(thirdLeftHeaderTitle);
    firstRightHeader.setCellValue(firstRightHeaderTitle);
    secondRightHeader.setCellValue(secondRightHeaderTitle);
    sheetOne.addMergedRegion(new CellRangeAddress(0, 0, 1, 3));
    sheetOne.addMergedRegion(new CellRangeAddress(1, 1, 1, 3));
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));
    sheetOne.addMergedRegion(new CellRangeAddress(0, 0, sizeheaderTitle - 2,
        sizeheaderTitle + 1));
    sheetOne.addMergedRegion(new CellRangeAddress(1, 1, sizeheaderTitle - 2,
        sizeheaderTitle + 1));

    //Tao tieu de
    Row rowMainTitle = sheetOne.createRow(4);
    Cell mainCellTitle = rowMainTitle.createCell(1);
    mainCellTitle.setCellValue(I18n.getLanguage("shiftcr.template.title").toUpperCase());
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetOne.addMergedRegion(new CellRangeAddress(4, 4, 1, 7));

    // Tao note
    Row rowsubTitle = sheetOne.createRow(5);
    Cell subCellTitle = rowsubTitle.createCell(1);
    subCellTitle.setCellValue(I18n.getLanguage("shiftcr.template.note"));
    subCellTitle.setCellStyle(cellStyleSubTitle);
    sheetOne.addMergedRegion(new CellRangeAddress(5, 5, 1, 7));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao header
    Row headerRow = sheetOne.createRow(7);
    headerRow.setHeight((short) 600);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeaderStar : listHeaderStar) {
        if (checkHeaderStar.equalsIgnoreCase(listHeader.get(i))) {
          rt.append(" (*)", fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(cellStyleHeader);
    }

    sheetOne.setColumnWidth(sttColumn, 2000);
    sheetOne.setColumnWidth(crNumberColumn, 12000);

    workBook.setSheetName(0, I18n.getLanguage("shiftcr.export.nameSheet"));

    String fileResult = tempFolder + File.separator;
    String fileName = "BM_IMPORT_SHIFT_CR" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importShiftWorkData(MultipartFile multipartFile,
      ShiftWorkDTO shiftWorkDTO) {
    Long check = 1L;
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<ShiftWorkDTO> exportDTOList = new ArrayList<>();
    List<ShiftWorkDTO> shiftWorkInsideDTOArrayList = new ArrayList<>();
    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDto;
      }
      String filePath = FileUtils
          .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(), tempFolder);
      if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        return resultInSideDto;
      }
      File fileImp = new File(filePath);
      List<Object[]> lstHeader;
      lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 6,
          0, 11, 1000);
      if (lstHeader.size() == 0 || !validFileFormatShiftWork(lstHeader)) {
        resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
        return resultInSideDto;
      }
      List<Object[]> lstData = CommonImport.getDataFromExcelFile(fileImp, 0, 7,
          0, 11, 1000);

      if (lstData.size() > 1500) {
        resultInSideDto.setKey(RESULT.DATA_OVER);
        resultInSideDto.setMessage(String.valueOf(1500));
        return resultInSideDto;
      }
      if (!lstData.isEmpty()) {
        int row = 4;
        int index = 0;
        for (Object[] obj : lstData) {
          ShiftWorkDTO createDTO = new ShiftWorkDTO();
          if (obj[1] != null) {
            createDTO.setDescription(obj[1].toString().trim());
          } else {
            createDTO.setDescription(null);
            check = 0L;
          }
          if (obj[2] != null) {
            if ("".equals(DataUtil.validateDateTimeDdMmYyyy_HhMmSs(obj[2].toString().trim()))) {
              createDTO.setStartTime(DateTimeUtils.convertStringToDate(obj[2].toString().trim()));
              createDTO.setStartTimeError(obj[2].toString().trim());
            } else {
              createDTO.setResultImport(I18n
                  .getLanguage("shiftwork.errType.startTimeError"));
              createDTO.setStartTimeError(obj[2].toString().trim());
              check = 0L;
            }
          }
          if (obj[3] != null) {
            if ("".equals(DataUtil.validateDateTimeDdMmYyyy_HhMmSs(obj[3].toString().trim()))) {
              createDTO.setDeadLine(DateTimeUtils.convertStringToDate(obj[3].toString().trim()));
              createDTO.setDeadLineError(obj[3].toString().trim());
            } else {
              createDTO.setResultImport(I18n
                  .getLanguage("shiftwork.errType.deadLineError"));
              createDTO.setDeadLineError(obj[3].toString().trim());
              check = 0L;
            }

          }
          if (obj[4] != null) {
            createDTO.setOwner(obj[4].toString().trim());
          } else {
            createDTO.setOwner(null);
          }
          if (obj[5] != null) {
            createDTO.setHandle(obj[5].toString().trim());
          } else {
            createDTO.setHandle(null);
          }
          if (obj[6] != null) {
            createDTO.setImportantLevel(obj[6].toString().trim());
          } else {
            createDTO.setImportantLevel(null);
          }
          if (obj[7] != null) {
            createDTO.setResult(obj[7].toString().trim());
          } else {
            createDTO.setResult(null);
          }
          if (obj[8] != null) {
            createDTO.setNextWork(obj[8].toString().trim());
          } else {
            createDTO.setNextWork(null);
          }
          if (obj[9] != null) {
            createDTO.setContact(obj[9].toString().trim());
          } else {
            createDTO.setContact(null);
          }
          if (obj[10] != null) {
            createDTO.setWorkStatus(obj[10].toString().trim());
          } else {
            createDTO.setWorkStatus(null);
          }
          if (obj[11] != null) {
            createDTO.setOpinion(obj[11].toString().trim());
          } else {
            createDTO.setOpinion(null);
          }

          ShiftWorkDTO createExportDTO = validateImportShiftWorkInfo(createDTO,
              exportDTOList);
          if (StringUtils.isStringNullOrEmpty(createExportDTO.getResultImport())) {
            createExportDTO.setResultImport(I18n.getLanguage("shiftwork.result.import"));
            exportDTOList.add(createExportDTO);
            shiftWorkInsideDTOArrayList.add(createExportDTO);
          } else {
            exportDTOList.add(createExportDTO);
            index++;
          }
          row++;
        }
        if (index == 0) {
          if (!shiftWorkInsideDTOArrayList.isEmpty()) {
            resultInSideDto.setObject(shiftWorkInsideDTOArrayList);
            resultInSideDto.setKey(RESULT.SUCCESS);
          }
        } else {
          File fileExport = handleFile(exportDTOList, Constants.RESULT_IMPORT, check);
          String fileName =
              "SHIFT_WORK_CREATE_RESULT_IMPORT" + "_" + System.currentTimeMillis() + ".xlsx";
          String pathTemp = fileExport.getPath();
          String fileNameOut = "";
          if (pathTemp != null) {
            String path[] = pathTemp.split("\\\\");
            if (path != null && path.length > 0) {
              fileNameOut = path[path.length - 1];
            }
          }
          resultInSideDto.setFilePath(fileNameOut);
          resultInSideDto.setFileName(fileName);
          resultInSideDto.setKey(RESULT.ERROR);
        }
      } else {
        resultInSideDto.setKey(RESULT.NODATA);
        resultInSideDto.setMessage(Constants.FILE_NULL);
        return resultInSideDto;
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(ex.getMessage());
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto importShiftCRData(MultipartFile multipartFile, ShiftCrDTO shiftCrDTO) {
    Long check = 1L;
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<ShiftCrDTO> listDto = new ArrayList<>();
    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDto;
      }
      String filePath = FileUtils
          .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(), tempFolder);
      if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        return resultInSideDto;
      }
      File fileImp = new File(filePath);
      List<Object[]> lstHeader;
      lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 7,
          0, 1, 1000);
      if (lstHeader.size() == 0 || !validFileFormatShiftCR(lstHeader)) {
        resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
        return resultInSideDto;
      }
      List<Object[]> lstData = CommonImport.getDataFromExcelFile(fileImp, 0, 8,
          0, 1, 1000);

      if (lstData.size() > 1500) {
        resultInSideDto.setKey(RESULT.DATA_OVER);
        resultInSideDto.setMessage(String.valueOf(1500));
        return resultInSideDto;
      }
      if (!lstData.isEmpty()) {
        int index = 0;
        int row = 4;
        for (Object[] obj : lstData) {
          ShiftCrDTO createDTO = new ShiftCrDTO();
          if (obj[1] != null) {
            createDTO.setCrNumber(obj[1].toString().trim());
            createDTO = validateDuplicateShiftCr(listDto, createDTO);
            if (StringUtils.isStringNullOrEmpty(createDTO.getResultImport())) {
              ResultInSideDto resultCheckCrNumber = findCrByCrNumber(createDTO);
              if (RESULT.SUCCESS.equals(resultCheckCrNumber.getKey())) {
                createDTO = (ShiftCrDTO) resultCheckCrNumber.getObject();
              } else {
                createDTO.setResultImport(resultCheckCrNumber.getMessage());
              }
            }
          } else {
            createDTO.setResultImport(I18n.getLanguage("shifthandover.crRequired"));
          }
          if (StringUtils.isStringNullOrEmpty(createDTO.getResultImport())) {
            createDTO.setResultImport(I18n.getLanguage("shiftcr.result.import"));
          } else {
            index++;
          }
          listDto.add(createDTO);
          row++;
        }
        if (index == 0) {
          resultInSideDto.setObject(listDto);
          resultInSideDto.setKey(RESULT.SUCCESS);
        } else {
          File fileExport = handleFileShiftCR(listDto, Constants.RESULT_IMPORT, check);
          String fileName =
              "SHIFT_CR_CREATE_RESULT_IMPORT" + "_" + System.currentTimeMillis() + ".xlsx";
          String pathTemp = fileExport.getPath();
          String fileNameOut = "";
          if (pathTemp != null) {
            String path[] = pathTemp.split("\\\\");
            if (path != null && path.length > 0) {
              fileNameOut = path[path.length - 1];
            }
          }
          resultInSideDto.setFilePath(fileNameOut);
          resultInSideDto.setFileName(fileName);
          resultInSideDto.setKey(RESULT.ERROR);
        }
      } else {
        resultInSideDto.setKey(RESULT.NODATA);
        resultInSideDto.setMessage(Constants.FILE_NULL);
        return resultInSideDto;
      }

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(ex.getMessage());
    }
    return resultInSideDto;
  }

  private ShiftCrDTO validateDuplicateShiftCr(List<ShiftCrDTO> list, ShiftCrDTO shiftCrDTO) {
    if (list != null && list.size() > 0) {
      for (int i = 0; i < list.size(); i++) {
        ShiftCrDTO dto = list.get(i);
        if (dto.getCrNumber().equals(shiftCrDTO.getCrNumber())) {
          shiftCrDTO.setResultImport(I18n.getLanguage("shiftHandover.err.dup-code-in-file")
              .replaceAll("0", String.valueOf((i) + 1)));
          return shiftCrDTO;
        }
      }
    }
    return shiftCrDTO;
  }

  private File handleFileShiftCR(List<ShiftCrDTO> listExportDto, String key, Long check)
      throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    String sheetName = I18n.getLanguage("shiftcr.export.title");
    String title = I18n.getLanguage("shiftcr.export.title");
    String subTitle;
    String fileNameOut;
    int startRow = 8;
    String[] header;
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = SHIFT_CR_CREATE_RESULT_IMPORT;
      subTitle = I18n.getLanguage("shiftcr.export.import",
          DateUtil.date2ddMMyyyyHHMMss(DateTimeUtils.convertDateOffset()));
      header = new String[]{(check == 1L) ? "crNumber" : "crNumberError", "resultImport"};
    } else {
      fileNameOut = SHIFT_CR_CREATE_EXPORT;
      subTitle = I18n
          .getLanguage("shiftcr.export.eportDate",
              DateUtil.date2ddMMyyyyHHMMss(DateTimeUtils.convertDateOffset()));
      header = new String[]{(check == 1L) ? "crNumber" : "crNumberError"};
    }
    List<ConfigHeaderExport> headerExportList = getListHeaderSheet(header);

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        listExportDto,
        sheetName,
        title,
        subTitle,
        startRow,
        4,
        6,
        true,
        "language.shiftcr",
        headerExportList,
        fieldSplit,
        "",
        null,
        null,
        null,
        null
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(startRow, 0, 0, 0,
        I18n.getLanguage("shiftcr.stt"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    Map<Integer, Integer> mapCustomColumnWidth = new HashMap<>();
    mapCustomColumnWidth.put(0, 2000);
    mapCustomColumnWidth.put(1, 6000);
    configFileExport.setMapCustomColumnWidth(mapCustomColumnWidth);
    fileExports.add(configFileExport);

    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = doExportExcel(fileTemplate, fileNameOut,
        fileExports, rootPath, null);
    return fileExport;
  }

  private ShiftCrDTO validateImportShiftCRInfo(ShiftCrDTO exportDTO,
      List<ShiftCrDTO> listExportDto) {
    if (StringUtils.isStringNullOrEmpty(exportDTO.getCrNumber())) {
      exportDTO.setResultImport(I18n.getLanguage("shiftcr.err.crNumber"));
      return exportDTO;
    }

    if (listExportDto != null && listExportDto.size() > 0) {
      exportDTO = validateDuplicateShiftCR(listExportDto, exportDTO);
    }
    return exportDTO;
  }

  private ShiftCrDTO validateDuplicateShiftCR(List<ShiftCrDTO> list, ShiftCrDTO shiftCrDTO) {
    try {
      for (int i = 0; i < list.size(); i++) {
        ShiftCrDTO shiftCRDTOTemp = list.get(i);
        if (shiftCRDTOTemp.getCrNumber() == null) {
          shiftCRDTOTemp.setCrName("");
        }

        if (shiftCRDTOTemp.getCrNumber().equals(shiftCrDTO.getCrNumber())) {
          shiftCrDTO.setResultImport(I18n.getLanguage("shiftcr.err.dup-code-in-file")
              .replaceAll("0", String.valueOf((i) + 1)));
          break;
        }
      }
      return shiftCrDTO;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  private boolean validFileFormatShiftCR(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 2) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("shiftcr.stt")).equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("shiftcr.crNumber") + " (*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    return true;
  }

  public void setMapShift() {
    List<String> lstCategoryCode = new ArrayList<>();
    lstCategoryCode.add(Constants.GNOC_SHIFT);
    lstCategoryCode.add(Constants.GNOC_SHIFT_KPI);
    List<CatItemDTO> list = catItemRepository.getListCatItemDTO(lstCategoryCode, null);
    if (list != null && list.size() > 0) {
      for (CatItemDTO dto : list) {
        mapShift.put(dto.getItemValue(), dto);
      }
    }
  }
}
