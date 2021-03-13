package com.viettel.gnoc.risk.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.model.GnocFileEntity;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
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
import com.viettel.gnoc.commons.utils.Constants.RISK_MASTER_CODE;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.risk.dto.*;
import com.viettel.gnoc.risk.repository.RiskSystemDetailRepository;
import com.viettel.gnoc.risk.repository.RiskSystemFileRepository;
import com.viettel.gnoc.risk.repository.RiskSystemHistoryRepository;
import com.viettel.gnoc.risk.repository.RiskSystemRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint.ValidationType;
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
public class RiskSystemBusinessImpl implements RiskSystemBusiness {

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

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  RiskSystemRepository riskSystemRepository;

  @Autowired
  RiskSystemDetailRepository riskSystemDetailRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  RiskSystemFileRepository riskSystemFileRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  RiskSystemHistoryRepository riskSystemHistoryRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  private int maxRecord = 500;

  Map<String, String> mapSystemPriority = new HashMap<>();
  Map<Long, String> mapCountry = new HashMap<>();

  @Override
  public Datatable getDataRiskSystemSearchWeb(RiskSystemDTO riskSystemDTO) {
    log.debug("Request to getDataRiskSystemSearchWeb: {}", riskSystemDTO);
    return riskSystemRepository.getDataRiskSystemSearchWeb(riskSystemDTO);
  }

  @Override
  public List<RiskSystemDTO> getListRiskSystem(RiskSystemDTO riskSystemDTO) {
    log.debug("Request to getListRiskSystem: {}", riskSystemDTO);
    return riskSystemRepository.getListRiskSystem(riskSystemDTO);
  }

  @Override
  public ResultInSideDto insertRiskSystemWeb(List<MultipartFile> fileAttacks,
      RiskSystemDTO riskSystemDTO) throws Exception {
    log.debug("Request to insertRiskSystemWeb: {}", riskSystemDTO);
    ResultInSideDto resultInSideDto;
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    resultInSideDto = riskSystemRepository.insertOrUpdateRiskSystem(riskSystemDTO);
    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      Long riskSystemId = resultInSideDto.getId();
      List<RiskSystemDetailDTO> listDetail = riskSystemDTO.getListRiskSystemDetail();
      if (listDetail != null && listDetail.size() > 0) {
        for (RiskSystemDetailDTO detailDTO : listDetail) {
          detailDTO.setSystemId(riskSystemId);
          detailDTO.setId(null);
          resultInSideDto = riskSystemDetailRepository.insertRiskSystemDetail(detailDTO);
          if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            throw new RuntimeException(RESULT.ERROR);
          }
        }
      }
      Date date = new Date();
      RiskSystemHistoryDTO riskSystemHistoryDTO = new RiskSystemHistoryDTO();
      riskSystemHistoryDTO.setUserId(userToken.getUserID());
      riskSystemHistoryDTO.setUnitId(userToken.getDeptId());
      riskSystemHistoryDTO.setUpdateTime(date);
      // File attachment
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      if (fileAttacks != null) {
        for (MultipartFile multipartFile : fileAttacks) {
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                  multipartFile.getBytes(), date);
          String fullPathOld = FileUtils
              .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                  uploadFolder, null);
          String fileName = multipartFile.getOriginalFilename();
          RiskSystemFileDTO riskSystemFileDTO = new RiskSystemFileDTO();
          riskSystemFileDTO.setSystemId(riskSystemId);
          riskSystemFileDTO.setFileName(
              fullPathOld
                  .substring(fullPathOld.lastIndexOf(File.separator) + 1, fullPathOld.length()));
          riskSystemFileDTO
              .setPath(uploadFolder + File.separator + FileUtils.createPathByDate(date));
          resultInSideDto = riskSystemFileRepository.insertRiskSystemFile(riskSystemFileDTO);
          if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            throw new RuntimeException(RESULT.ERROR);
          }

          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(fileName);
          gnocFileDto.setCreateUnitId(userToken.getDeptId());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(userToken.getUserID());
          gnocFileDto.setCreateUserName(userToken.getUserName());
          gnocFileDto.setCreateTime(date);
          gnocFileDto.setMappingId(riskSystemId);
          gnocFileDtos.add(gnocFileDto);
        }
        gnocFileRepository
            .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.RISK_SYSTEM, riskSystemId,
                gnocFileDtos);
      }
      riskSystemDTO.setId(riskSystemId);
      setFileName(riskSystemDTO);
      String fileNameTemp = riskSystemDTO.getFileName();
      riskSystemHistoryDTO.setNewFile(fileNameTemp);
      riskSystemHistoryDTO.setContent(
          riskSystemHistoryDTO.getContent() + "{listFileName: " + riskSystemHistoryDTO.getOldFile()
              + "-> " + riskSystemHistoryDTO.getNewFile() + "}");
      resultInSideDto = riskSystemHistoryRepository.insertRiskSystemHistory(riskSystemHistoryDTO);
      if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        throw new RuntimeException(RESULT.ERROR);
      }
      //Add history
      try {
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
        dataHistoryChange.setType("RISK_SYSTEMS");
        //Old Object History
        dataHistoryChange.setOldObject(new RiskSystemDTO());
        dataHistoryChange.setActionType("add");
        //New Object History
        dataHistoryChange.setNewObject(riskSystemDTO);
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    } else {
      throw new RuntimeException(RESULT.ERROR);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateRiskSystemWeb(List<MultipartFile> fileAttacks,
      RiskSystemDTO riskSystemDTO) throws Exception {
    log.debug("Request to updateRiskSystemWeb: {}", riskSystemDTO);
    ResultInSideDto resultInSideDto;
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    RiskSystemDTO oldHis =findRiskSystemByIdFromWeb(riskSystemDTO.getId());
    RiskSystemDTO riskSystemOld = riskSystemRepository.getRiskSystemOldById(riskSystemDTO.getId());
    resultInSideDto = riskSystemRepository.insertOrUpdateRiskSystem(riskSystemDTO);
    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      Long riskSystemId = resultInSideDto.getId();
      List<RiskSystemDetailDTO> listDetail = riskSystemDTO.getListRiskSystemDetail();
      if (listDetail != null && listDetail.size() > 0) {
        resultInSideDto = riskSystemDetailRepository.deleteRiskSystemDetailBySystemId(riskSystemId);
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          throw new RuntimeException(RESULT.ERROR);
        }
        for (RiskSystemDetailDTO detailDTO : listDetail) {
          detailDTO.setSystemId(riskSystemId);
          detailDTO.setId(null);
          resultInSideDto = riskSystemDetailRepository.insertRiskSystemDetail(detailDTO);
          if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            throw new RuntimeException(RESULT.ERROR);
          }
        }
      }
      Date date = new Date();
      RiskSystemHistoryDTO riskSystemHistoryDTO = new RiskSystemHistoryDTO();
      riskSystemHistoryDTO.setSystemId(riskSystemDTO.getId());
      String stt = compareObjectData(riskSystemDTO.tempDTO(), riskSystemOld.tempDTO());
      riskSystemHistoryDTO.setContent(stt);
      List<RiskSystemHistoryDTO> listHistory = riskSystemHistoryRepository
          .getListHistoryBySystemId(riskSystemDTO.getId());
      if (listHistory != null && listHistory.size() > 0) {
        RiskSystemHistoryDTO oldHistoryDTO = listHistory.get(0);
        riskSystemHistoryDTO.setOldFile(oldHistoryDTO.getNewFile());
      }
      riskSystemHistoryDTO.setUserId(userToken.getUserID());
      riskSystemHistoryDTO.setUnitId(userToken.getDeptId());
      riskSystemHistoryDTO.setUpdateTime(date);

      // File attachment
      //Start save file old
//      List<RiskSystemFileEntity> lstFileOld = riskSystemFileRepository
//          .getListRiskSystemFileBySystemId(riskSystemDTO.getId());
//      if (lstFileOld != null && lstFileOld.size() > 0) {
//        List<Long> listIdFileIdOld = lstFileOld.stream()
//            .map(RiskSystemFileEntity::getRiskSystemFileId).collect(Collectors.toList());
//        List<GnocFileDto> gnocFileDtosWeb = riskSystemDTO.getGnocFileDtos();
//        List<Long> listIdFileIdNew = gnocFileDtosWeb.stream()
//            .map(GnocFileDto::getMappingId).collect(Collectors.toList());
//        listIdFileIdOld.removeAll(listIdFileIdNew);
//      }
      for (Long aLong : riskSystemDTO.getIdDeleteList()) {
        riskSystemFileRepository.deleteRiskSystemFile(aLong);
      }
      //End save file old
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      if (fileAttacks != null) {
        for (MultipartFile multipartFile : fileAttacks) {
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                  multipartFile.getBytes(), date);
          String fullPathOld = FileUtils
              .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                  uploadFolder, null);
          String fileName = multipartFile.getOriginalFilename();
          //Start save file old
          RiskSystemFileDTO riskSystemFileDTO = new RiskSystemFileDTO();
          riskSystemFileDTO.setSystemId(riskSystemId);
          riskSystemFileDTO.setFileName(
              fullPathOld
                  .substring(fullPathOld.lastIndexOf(File.separator) + 1, fullPathOld.length()));
          riskSystemFileDTO
              .setPath(uploadFolder + File.separator + FileUtils.createPathByDate(date));
          ResultInSideDto resultFileDataOld = riskSystemFileRepository
              .insertRiskSystemFile(riskSystemFileDTO);
          //End save file old
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(fileName);
          gnocFileDto.setCreateUnitId(userToken.getDeptId());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(userToken.getUserID());
          gnocFileDto.setCreateUserName(userToken.getUserName());
          gnocFileDto.setCreateTime(new Date());
          gnocFileDto.setMappingId(resultFileDataOld.getId());
          gnocFileDtos.add(gnocFileDto);
        }
//        List<GnocFileDto> gnocFileDtosAll = new ArrayList<>();
//        gnocFileDtosAll.addAll(gnocFileDtos);
//        gnocFileDtosAll.addAll(riskSystemDTO.getGnocFileDtos());
        gnocFileRepository.deleteListGnocFile(GNOC_FILE_BUSSINESS.RISK_SYSTEM, riskSystemId,
            riskSystemDTO.getIdDeleteList());
        gnocFileRepository
            .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.RISK_SYSTEM, riskSystemId,
                gnocFileDtos);
      }

      setFileName(riskSystemDTO);
      String fileNameTemp = riskSystemDTO.getFileName();
      riskSystemHistoryDTO.setNewFile(fileNameTemp);
      riskSystemHistoryDTO.setContent(
          riskSystemHistoryDTO.getContent() + "{listFileName: " + riskSystemHistoryDTO.getOldFile()
              + " -> " + riskSystemHistoryDTO.getNewFile() + "}");
      resultInSideDto = riskSystemHistoryRepository.insertRiskSystemHistory(riskSystemHistoryDTO);
      if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        throw new RuntimeException(RESULT.ERROR);
      }
      //Add history
      try {
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(riskSystemDTO.getId().toString());
        dataHistoryChange.setType("RISK_SYSTEMS");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        dataHistoryChange.setActionType("update");
        //New Object History
        dataHistoryChange.setNewObject(riskSystemDTO);
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    } else {
      throw new RuntimeException(RESULT.ERROR);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteRiskSystem(Long id) {
    log.debug("Request to deleteRiskSystem: {}", id);
    ResultInSideDto resultInSideDto;
    RiskSystemDTO oldHis = findRiskSystemByIdFromWeb(id);
    resultInSideDto = riskSystemRepository.deleteRiskSystem(id);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(id.toString());
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(new RiskSystemDTO());
        dataHistoryChange.setType("RISK_SYSTEMS");
        dataHistoryChange.setActionType("delete");
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return  resultInSideDto;
  }

  @Override
  public RiskSystemDTO findRiskSystemByIdFromWeb(Long id) {
    log.debug("Request to findRiskSystemByIdFromWeb: {}", id);
    UserToken userToken = ticketProvider.getUserToken();
    Double offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    RiskSystemDTO riskSystemDTO = riskSystemRepository
        .findRiskSystemByIdFromWeb(id, offsetFromUser);
    List<RiskSystemDetailDTO> listDetail = riskSystemDetailRepository
        .getListRiskSystemDetailBySystemId(id);
    riskSystemDTO.setListRiskSystemDetail(listDetail);
    return riskSystemDTO;
  }

  @Override
  public List<GnocFileDto> getListFileFromRiskSystem(Long systemId) {
    log.debug("Request to getListFileFromRiskSystem: {}", systemId);
    GnocFileDto gnocFileDto = new GnocFileDto();
    gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.RISK_SYSTEM);
    gnocFileDto.setBusinessId(systemId);
    List<GnocFileDto> gnocFileDtos = gnocFileRepository.getListGnocFileByDto(gnocFileDto);
    for (GnocFileDto fileDto : gnocFileDtos) {
      fileDto.setFileName(FileUtils.getFileName(fileDto.getPath()));
    }
    return gnocFileDtos;
  }

  @Override
  public Datatable getListRiskSystemHistoryBySystemId(RiskSystemHistoryDTO riskSystemHistoryDTO) {
    log.debug("Request to getListRiskSystemHistoryBySystemId: {}", riskSystemHistoryDTO);
    UserToken userToken = ticketProvider.getUserToken();
    Double offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    riskSystemHistoryDTO.setOffset(offsetFromUser);
    return riskSystemHistoryRepository.getListRiskSystemHistoryBySystemId(riskSystemHistoryDTO);
  }

  @Override
  public File exportDataRiskSystem(RiskSystemDTO riskSystemDTO) throws Exception {
    log.debug("Request to exportDataRiskSystem: {}", riskSystemDTO);
    List<RiskSystemDTO> list = riskSystemRepository.getListRiskSystemExport(riskSystemDTO);
    if (list != null && list.size() > 0) {
      for (RiskSystemDTO dto : list) {
        List<RiskSystemDetailDTO> listDetail = riskSystemDetailRepository
            .getListRiskSystemDetailBySystemId(dto.getId());
        if (listDetail != null && listDetail.size() > 0) {
          String userUnit = "";
          for (RiskSystemDetailDTO detailDTO : listDetail) {
            userUnit +=
                detailDTO.getManageUserFullName() + " (" + detailDTO.getManageUserName() + ") - "
                    + detailDTO.getManageUnitName() + " (" + detailDTO.getManageUnitCode() + ")"
                    + "\r\n";
          }
          userUnit = userUnit.trim();
          dto.setUserUnit(userUnit);
        }
      }
    }
    String[] header = new String[]{"systemCode", "systemName", "schedule", "systemPriorityName",
        "userUnit", "countryName"};
    Date date = DateTimeUtils.convertDateOffset();
    return handleFileExport(list, header, DateUtil.date2ddMMyyyyHHMMss(date), "EXPORT_RISK_SYSTEM");
  }

  @Override
  public File getTemplateImport() throws IOException {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);
    XSSFSheet sheetOrther = workBook.createSheet("Orther");

    String[] header = new String[]{
        I18n.getLanguage("riskSystem.STT"),
        I18n.getLanguage("riskSystem.systemCode"),
        I18n.getLanguage("riskSystem.systemName"),
        I18n.getLanguage("riskSystem.scheduleStr"),
        I18n.getLanguage("riskSystem.systemPriorityName"),
        I18n.getLanguage("riskSystem.countryName"),
        I18n.getLanguage("riskSystem.lastUpdateTimeStr"),
        I18n.getLanguage("riskSystem.manageUserName"),
        I18n.getLanguage("riskSystem.actionName")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("riskSystem.systemCode"),
        I18n.getLanguage("riskSystem.systemName"),
        I18n.getLanguage("riskSystem.scheduleStr"),
        I18n.getLanguage("riskSystem.systemPriorityName"),
        I18n.getLanguage("riskSystem.countryName"),
        I18n.getLanguage("riskSystem.lastUpdateTimeStr"),
        I18n.getLanguage("riskSystem.manageUserName"),
        I18n.getLanguage("riskSystem.actionName")
    };
    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int sttColumn = listHeader.indexOf(I18n.getLanguage("riskSystem.STT"));
    int systemCodeColumn = listHeader.indexOf(I18n.getLanguage("riskSystem.systemCode"));
    int systemNameColumn = listHeader.indexOf(I18n.getLanguage("riskSystem.systemName"));
    int scheduleColumn = listHeader.indexOf(I18n.getLanguage("riskSystem.scheduleStr"));
    int systemPriorityNameColumn = listHeader
        .indexOf(I18n.getLanguage("riskSystem.systemPriorityName"));
    int countryNameColumn = listHeader.indexOf(I18n.getLanguage("riskSystem.countryName"));
    int lastUpdateTimeColumn = listHeader.indexOf(I18n.getLanguage("riskSystem.lastUpdateTimeStr"));
    int manageUserNameColumn = listHeader.indexOf(I18n.getLanguage("riskSystem.manageUserName"));
    int actionNameColumn = listHeader.indexOf(I18n.getLanguage("riskSystem.actionName"));

    String firstLeftHeaderTitle = I18n.getLanguage("riskSystem.export.firstLeftHeader");
    String secondLeftHeaderTitle = I18n.getLanguage("riskSystem.export.secondLeftHeader");
    String firstRightHeaderTitle = I18n.getLanguage("riskSystem.export.firstRightHeader");
    String secondRightHeaderTitle = I18n.getLanguage("riskSystem.export.secondRightHeader");

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
    mainCellTitle.setCellValue(I18n.getLanguage("riskSystem.template.title"));
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
          rt.append(" (*)", fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(cellStyleHeader);
    }

    int row = 1;
    Datatable dataSystemPriority = catItemRepository
        .getItemMaster(Constants.CATEGORY.RISK_SYSTEM_PRIORITY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> listSystemPriority = (List<CatItemDTO>) dataSystemPriority.getData();
    for (CatItemDTO dto : listSystemPriority) {
      ewu.createCell(sheetOrther, 0, row++, dto.getItemName(), styles.get("cell"));
    }
    Name systemPriorityName = workBook.createName();
    systemPriorityName.setNameName("systemPriorityName");
    systemPriorityName.setRefersToFormula("Orther!$A$2:$A$" + row);
    XSSFDataValidationConstraint systemPriorityConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "systemPriorityName");
    CellRangeAddressList cellRangeSystemPriority = new CellRangeAddressList(6, (maxRecord + 6),
        systemPriorityNameColumn, systemPriorityNameColumn);
    XSSFDataValidation dataValidationSystemPriority = (XSSFDataValidation) dvHelper
        .createValidation(systemPriorityConstraint, cellRangeSystemPriority);
    dataValidationSystemPriority.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationSystemPriority);

    row = 1;
    Datatable dataCountry = catItemRepository.getItemMaster(RISK_MASTER_CODE.GNOC_COUNTRY,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> listCountry = (List<CatItemDTO>) dataCountry.getData();
    for (CatItemDTO dto : listCountry) {
      ewu.createCell(sheetOrther, 1, row++, dto.getItemName(), styles.get("cell"));
    }
    Name countryName = workBook.createName();
    countryName.setNameName("countryName");
    countryName.setRefersToFormula("Orther!$B$2:$B$" + row);
    XSSFDataValidationConstraint countryConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "countryName");
    CellRangeAddressList cellRangeCountry = new CellRangeAddressList(6, (maxRecord + 6),
        countryNameColumn, countryNameColumn);
    XSSFDataValidation dataValidationCountry = (XSSFDataValidation) dvHelper.createValidation(
        countryConstraint, cellRangeCountry);
    dataValidationCountry.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationCountry);

    row = 1;
    ewu.createCell(sheetOrther, 2, row++, I18n.getLanguage("riskSystem.action.0"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 2, row++, I18n.getLanguage("riskSystem.action.1"),
        styles.get("cell"));
    Name actionName = workBook.createName();
    actionName.setNameName("actionName");
    actionName.setRefersToFormula("Orther!$C$2:$C$" + row);
    XSSFDataValidationConstraint actionConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "actionName");
    CellRangeAddressList cellRangeAction = new CellRangeAddressList(6, (maxRecord + 6),
        actionNameColumn, actionNameColumn);
    XSSFDataValidation dataValidationAction = (XSSFDataValidation) dvHelper.createValidation(
        actionConstraint, cellRangeAction);
    dataValidationAction.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationAction);

    sheetMain.setColumnWidth(sttColumn, 2000);
    sheetMain.setColumnWidth(systemCodeColumn, 8000);
    sheetMain.setColumnWidth(systemNameColumn, 8000);
    sheetMain.setColumnWidth(scheduleColumn, 7000);
    sheetMain.setColumnWidth(systemPriorityNameColumn, 6000);
    sheetMain.setColumnWidth(countryNameColumn, 4000);
    sheetMain.setColumnWidth(lastUpdateTimeColumn, 8000);
    sheetMain.setColumnWidth(manageUserNameColumn, 6000);
    sheetMain.setColumnWidth(actionNameColumn, 6000);

    sheetOrther.setSelected(false);
    workBook.setSheetHidden(1, true);
    workBook.setSheetName(0, I18n.getLanguage("riskSystem.import.title"));

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_RISK_SYSTEM" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importDataRiskSystem(MultipartFile multipartFile) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    List<RiskSystemDTO> listDto;
    List<RiskSystemDTO> listImportDto;
    Map<String, String> mapImportDTO;

    String[] header = new String[]{"systemCode", "systemName", "scheduleStr", "systemPriorityName",
        "countryName", "lastUpdateTimeStr", "manageUserName", "actionName", "resultImport"};

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
          setMapSystemPriority();
          setMapCountry();
          for (Object[] obj : lstData) {
            RiskSystemDTO importDTO = new RiskSystemDTO();
            if (obj[1] != null) {
              importDTO.setSystemCode(obj[1].toString().trim());
            }
            if (obj[2] != null) {
              importDTO.setSystemName(obj[2].toString().trim());
            }
            if (obj[3] != null) {
              importDTO.setScheduleStr(obj[3].toString().trim());
            }
            if (obj[4] != null) {
              importDTO.setSystemPriorityName(obj[4].toString().trim());
            }
            if (obj[5] != null) {
              importDTO.setCountryName(obj[5].toString().trim());
            }
            if (obj[6] != null) {
              importDTO.setLastUpdateTimeStr(obj[6].toString().trim());
            }
            if (obj[7] != null) {
              importDTO.setManageUserName(obj[7].toString().trim());
            }
            if (obj[8] != null) {
              importDTO.setActionName(obj[8].toString().trim());
            }
            RiskSystemDTO tempImportDTO = validateImportInfo(importDTO, mapImportDTO);
            if (tempImportDTO.getResultImport() == null) {
              tempImportDTO.setResultImport(I18n.getLanguage("riskSystem.result.import.ok"));
              if (tempImportDTO.getId() != null) {
                List<RiskSystemDetailDTO> listDetail = tempImportDTO.getListRiskSystemDetail();
                if (listDetail != null && listDetail.size() > 0) {
                  for (RiskSystemDetailDTO detailDTO : listDetail) {
                    detailDTO.setSystemId(tempImportDTO.getId());
                  }
                }
              }
              listImportDto.add(tempImportDTO);
              String systemCode = tempImportDTO.getSystemCode();
              mapImportDTO.put(systemCode, String.valueOf(value));
              listDto.add(tempImportDTO);
            } else {
              listImportDto.add(tempImportDTO);
              index++;
            }
            value++;
          }
          if (index == 0) {
            if (!listDto.isEmpty()) {
              for (RiskSystemDTO dto : listDto) {
                resultInSideDto = insertOrUpdateRiskSystem(dto);
              }
            }
          } else {
            File fileExport = handleFileExport(listImportDto, header, null, "RESULT_IMPORT");
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = handleFileExport(listImportDto, header, null, "RESULT_IMPORT");
          resultInSideDto.setFile(fileExport);
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
  public File getTemplateImportSystemDetail() throws IOException {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);
    XSSFSheet sheetOrther = workBook.createSheet("Orther");
    XSSFSheet sheetReference = workBook.createSheet("reference");

    String[] header = new String[]{
        I18n.getLanguage("riskSystem.STT"),
        I18n.getLanguage("riskSystem.systemCode"),
        I18n.getLanguage("riskSystem.manageUserName"),
        I18n.getLanguage("riskSystem.actionName")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("riskSystem.systemCode"),
        I18n.getLanguage("riskSystem.manageUserName"),
        I18n.getLanguage("riskSystem.actionName")
    };

    String[] header_reference = new String[]{
        I18n.getLanguage("riskSystem.systemCode"),
        I18n.getLanguage("riskSystem.systemName")
    };
    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);
    List<String> listHeaderReference = Arrays.asList(header_reference);

    Map<String, CellStyle> style = CommonExport.createStyles(workBook);

    int sttColumn = listHeader.indexOf(I18n.getLanguage("riskSystem.STT"));
    int systemCodeColumn = listHeader.indexOf(I18n.getLanguage("riskSystem.systemCode"));
    int manageUserNameColumn = listHeader.indexOf(I18n.getLanguage("riskSystem.manageUserName"));
    int actionNameColumn = listHeader.indexOf(I18n.getLanguage("riskSystem.actionName"));

    String firstLeftHeaderTitle = I18n.getLanguage("riskSystem.export.firstLeftHeader");
    String secondLeftHeaderTitle = I18n.getLanguage("riskSystem.export.secondLeftHeader");
    String firstRightHeaderTitle = I18n.getLanguage("riskSystem.export.firstRightHeader");
    String secondRightHeaderTitle = I18n.getLanguage("riskSystem.export.secondRightHeader");

    CellStyle cellStyleTopHeader = CommonExport.setCellStyleTopHeader(workBook);
    CellStyle cellStyleTopRightHeader = CommonExport.setCellStyleTopRightHeader(workBook);
    CellStyle cellStyleTitle = CommonExport.setCellStyleTitle(workBook);
    CellStyle cellStyleHeader = CommonExport.setCellStyleHeader(workBook);

    sheetReference
        .addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderReference.size() - 1));
    Row titleRow_Reference = sheetReference.createRow(1);
    titleRow_Reference.setHeightInPoints(25);
    Cell titleCellReference = titleRow_Reference.createCell(0);
    titleCellReference.setCellValue(I18n.getLanguage("riskSystem.sheetReference"));
    titleCellReference.setCellStyle(style.get("title"));

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
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, 1, 2));
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, sizeheaderTitle - 2,
        sizeheaderTitle + 1));
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, sizeheaderTitle - 2,
        sizeheaderTitle + 1));

    //Tao tieu de
    Row rowMainTitle = sheetMain.createRow(3);
    Row headerListReference = sheetReference.createRow(3);
    Cell mainCellTitle = rowMainTitle.createCell(1);
    mainCellTitle.setCellValue(I18n.getLanguage("riskSystemDetail.template.title"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(3, 3, 1, 2));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao header
    Cell headerCellSystemCode = headerListReference.createCell(0);
    Cell headerCellSystemName = headerListReference.createCell(1);

    XSSFRichTextString systemCode = new XSSFRichTextString(
        I18n.getLanguage("riskSystem.systemCode"));
    XSSFRichTextString systemName = new XSSFRichTextString(
        I18n.getLanguage("riskSystem.systemName"));

    headerCellSystemCode.setCellValue(systemCode);
    headerCellSystemCode.setCellStyle(style.get("header"));
    headerCellSystemName.setCellValue(systemName);
    headerCellSystemName.setCellStyle(style.get("header"));

    Row headerRow = sheetMain.createRow(5);
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

    int row = 1;
    ewu.createCell(sheetOrther, 2, row++, I18n.getLanguage("riskSystem.action.0"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 2, row++, I18n.getLanguage("riskSystem.action.2"),
        styles.get("cell"));
    Name actionName = workBook.createName();
    actionName.setNameName("actionName");
    actionName.setRefersToFormula("Orther!$C$2:$C$" + row);
    XSSFDataValidationConstraint actionConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "actionName");
    CellRangeAddressList cellRangeAction = new CellRangeAddressList(6, (maxRecord + 6),
        actionNameColumn, actionNameColumn);
    XSSFDataValidation dataValidationAction = (XSSFDataValidation) dvHelper.createValidation(
        actionConstraint, cellRangeAction);
    dataValidationAction.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationAction);

    sheetMain.setColumnWidth(sttColumn, 2000);
    sheetMain.setColumnWidth(systemCodeColumn, 8000);
    sheetMain.setColumnWidth(manageUserNameColumn, 6000);
    sheetMain.setColumnWidth(actionNameColumn, 6000);

    row = 4;
    List<RiskSystemDTO> lstRiskSystemDTO = getListRiskSystem(new RiskSystemDTO());
    if (lstRiskSystemDTO != null && !lstRiskSystemDTO.isEmpty()) {
      for (RiskSystemDTO dto : lstRiskSystemDTO) {
        ewu
            .createCell(sheetReference, 0, row, dto.getSystemCode(), style.get("cell"));
        ewu.createCell(sheetReference, 1, row, dto.getSystemName(), style.get("cell"));
        row++;
      }
    }
    sheetReference.setColumnWidth(0, 13000);
    sheetReference.setColumnWidth(1, 13000);

    sheetOrther.setSelected(false);
    workBook.setSheetHidden(1, true);
    workBook.setSheetName(0, I18n.getLanguage("riskSystemDetail.template.title"));
    workBook.setSheetName(2, I18n.getLanguage("riskSystem.sheetReference"));

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_RISK_SYSTEM_DETAIL" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importDataSystemDetail(MultipartFile fileImport) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    List<RiskSystemDetailImportDTO> listDto;
    List<RiskSystemDetailImportDTO> listExportDto;
    boolean isExistRecordNotOk = false;

    String[] header = new String[]{"systemCode", "manageUserName", "actionName", "resultImport"};

    try {
      if (fileImport == null || fileImport.isEmpty()) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDto;
      } else {
        String filePath = FileUtils
            .saveTempFile(fileImport.getOriginalFilename(), fileImport.getBytes(),
                tempFolder);
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          return resultInSideDto;
        }

        File fileImp = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 5,
            0, 3, 1000);
        if (lstHeader.size() == 0 || !validFileSystemDetailFormat(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }

        List<Object[]> lstData = CommonImport.getDataFromExcelFileNew(fileImp, 0, 6,
            0, 3, 1000);
        if (lstData.size() > maxRecord) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(maxRecord));
          return resultInSideDto;
        }

        listDto = new ArrayList<>();
        listExportDto = new ArrayList<>();

        if (!lstData.isEmpty()) {
          int index = 0;
          int value = 1;
          for (Object[] obj : lstData) {
            RiskSystemDetailImportDTO importDTO = new RiskSystemDetailImportDTO();
            if (obj[1] != null) {
              importDTO.setSystemCode(obj[1].toString().trim());
              RiskSystemDTO riskSystemDTO = new RiskSystemDTO();
              riskSystemDTO.setSystemCode(obj[1].toString().trim());
              List<RiskSystemDTO> lst = riskSystemRepository.getListRiskSystem(riskSystemDTO);
              if (lst != null && !lst.isEmpty()) {
                importDTO.setSystemId(String.valueOf(lst.get(0).getId()));
              } else {
                importDTO
                    .setResultImport(I18n.getLanguage("riskSystemDetail.systemCode.isNotExist"));
              }
            }
            if (obj[2] != null) {
              importDTO.setManageUserName(obj[2].toString().trim());
              UsersInsideDto users = userRepository.getUserDTOByUserName(obj[2].toString().trim());
              if (users != null) {
                importDTO.setManageUserId(String.valueOf(users.getUserId()));
                importDTO.setManageUnitId(String.valueOf(users.getUnitId()));
              } else {
                if (StringUtils.isNotNullOrEmpty(importDTO.getResultImport())) {
                  String resultImport = importDTO.getResultImport() + "," +
                      I18n.getLanguage("riskSystemDetail.managerUserName.isNotExist");
                  importDTO.setResultImport(resultImport);
                } else {
                  importDTO
                      .setResultImport(
                          I18n.getLanguage("riskSystemDetail.managerUserName.isNotExist"));
                }
              }
            }
            if (obj[3] != null) {
              importDTO.setActionName(obj[3].toString().trim());
              if (I18n.getLanguage("riskSystem.action.0").equals(obj[3].toString().trim())
                  && StringUtils.isStringNullOrEmpty(importDTO.getResultImport())) {
                List<RiskSystemDetailDTO> lst = riskSystemDetailRepository
                    .getRiskSystemDetailBySystemIdAndManageUserId(importDTO.getSystemId(),
                        importDTO.getManageUserId());
                if (lst != null && !lst.isEmpty()) {
                  if (StringUtils.isNotNullOrEmpty(importDTO.getResultImport())) {
                    String resultImport = importDTO.getResultImport() + "," +
                        I18n.getLanguage("riskType.err.dup-code");
                    importDTO.setResultImport(resultImport);
                  } else {
                    importDTO.setResultImport(I18n.getLanguage("riskType.err.dup-code"));
                  }
                }
              }
              if (I18n.getLanguage("riskSystem.action.2").equals(obj[3].toString().trim())
                  && StringUtils.isStringNullOrEmpty(importDTO.getResultImport())) {
                List<RiskSystemDetailDTO> lst = riskSystemDetailRepository
                    .getRiskSystemDetailBySystemIdAndManageUserId(importDTO.getSystemId(),
                        importDTO.getManageUserId());
                if (lst == null || lst.isEmpty()) {
                  if (StringUtils.isNotNullOrEmpty(importDTO.getResultImport())) {
                    String resultImport = importDTO.getResultImport() + "," +
                        I18n.getLanguage("riskType.err.notExist");
                    importDTO.setResultImport(resultImport);
                  } else {
                    importDTO.setResultImport(I18n.getLanguage("riskType.err.notExist"));
                  }
                }
              }
            }
            RiskSystemDetailImportDTO resultValidate = validateImportSystemDetail(importDTO);
            if (StringUtils.isStringNullOrEmpty(resultValidate.getResultImport())) {
              if (I18n.getLanguage("riskSystem.action.0").equals(obj[3].toString().trim())) {
                riskSystemDetailRepository
                    .insertRiskSystemDetail(resultValidate.toDTO());
              }
              if (I18n.getLanguage("riskSystem.action.2").equals(obj[3].toString().trim())) {
                riskSystemDetailRepository
                    .deleteRiskSystemDetailBySystemIdAndManageUserId(
                        Long.valueOf(resultValidate.getSystemId()),
                        Long.valueOf(resultValidate.getManageUserId()));
              }
              resultValidate.setResultImport(I18n.getLanguage("riskSystem.result.import.ok"));
              listExportDto.add(resultValidate);
              listDto.add(resultValidate);
            } else {
              listExportDto.add(resultValidate);
              isExistRecordNotOk = true;
            }
          }
          if (isExistRecordNotOk) {
            resultInSideDto.setKey(RESULT.ERROR);
            File fileExport = exportResultImport(listExportDto, header);
            resultInSideDto.setFile(fileExport);
            resultInSideDto.setFilePath(fileExport.getPath());
          } else {
            resultInSideDto.setMessage(RESULT.SUCCESS);
            File fileExport = exportResultImport(listExportDto, header);
            resultInSideDto.setFile(fileExport);
            resultInSideDto.setFilePath(fileExport.getPath());
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportResultImport(listExportDto, header);
          resultInSideDto.setFile(fileExport);
          resultInSideDto.setFilePath(fileExport.getPath());
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

  private RiskSystemDetailImportDTO validateImportSystemDetail(
      RiskSystemDetailImportDTO importDTO) {
    String resultImport = StringUtils.isStringNullOrEmpty(importDTO.getResultImport()) ? ""
        : importDTO.getResultImport();
    if (StringUtils.isStringNullOrEmpty(importDTO.getSystemCode())) {
      resultImport =
          StringUtils.isStringNullOrEmpty(resultImport) ? resultImport : resultImport + ",";
      resultImport = I18n.getLanguage("riskSystem.err.systemCode");
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getManageUserName())) {
      resultImport =
          StringUtils.isStringNullOrEmpty(resultImport) ? resultImport : resultImport + ",";
      resultImport += I18n.getLanguage("riskSystemDetail.managerUserName.error");
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getActionName())) {
      resultImport =
          StringUtils.isStringNullOrEmpty(resultImport) ? resultImport : resultImport + ",";
      resultImport += I18n.getLanguage("riskType.err.actionName");
    }

    if (StringUtils.isNotNullOrEmpty(importDTO.getResultImport())) {
      if (StringUtils.isNotNullOrEmpty(resultImport)) {
        importDTO.setResultImport(resultImport);
      }
    } else {
      importDTO.setResultImport(resultImport);
    }
    return importDTO;
  }

  private File exportResultImport(List<RiskSystemDetailImportDTO> list, String[] header)
      throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(header);
    String sheetName = "";
    String title = "";
    String fileNameOut = "";
    String headerPrefix = "language.riskSystem";
    String firstLeftHeader = I18n.getLanguage("riskSystem.export.firstLeftHeader");
    String secondLeftHeader = I18n.getLanguage("riskSystem.export.secondLeftHeader");
    String firstRightHeader = I18n.getLanguage("riskSystem.export.firstRightHeader");
    String secondRightHeader = I18n.getLanguage("riskSystem.export.secondRightHeader");
    int mergeTitleEndIndex = 6;
    int startRow = 7;
    int cellTitleIndex = 3;
    String subTitle = "Thời điểm xuất báo cáo : " + new Date();

    sheetName = I18n.getLanguage("riskSystemDetail.template.title");
    title = I18n.getLanguage("riskSystemDetail.template.title");
    fileNameOut = I18n.getLanguage("riskSystemDetail.template.title");

    ConfigFileExport configFileExport = new ConfigFileExport(
        list,
        sheetName,
        title,
        subTitle,
        startRow,
        cellTitleIndex,
        mergeTitleEndIndex,
        true,
        headerPrefix,
        lstHeaderSheet1,
        fieldSplit,
        "",
        firstLeftHeader,
        secondLeftHeader,
        firstRightHeader,
        secondRightHeader
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(startRow, 0, 0, 0,
        I18n.getLanguage("riskSystem.STT"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(fileTemplate, fileNameOut,
        fileExports, rootPath, null);
    return fileExport;
  }

  private boolean validFileSystemDetailFormat(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 4) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("riskSystem.STT"))
        .equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskSystem.systemCode") + " (*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskSystem.manageUserName") + " (*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskSystem.actionName") + " (*)")
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    return true;
  }

  private ResultInSideDto insertOrUpdateRiskSystem(RiskSystemDTO riskSystemDTO) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    riskSystemDTO.setSystemCode(riskSystemDTO.getSystemCode().toUpperCase());
    riskSystemDTO.setSystemName(riskSystemDTO.getSystemName().toUpperCase());
    if (riskSystemDTO.getAction().equals(0L)) {
      if (riskSystemDTO.getId() == null) {
        resultInSideDto = insertRiskSystemWeb(null, riskSystemDTO);
      } else {
        resultInSideDto.setKey(RESULT.ERROR);
      }
    }
    if (riskSystemDTO.getAction().equals(1L)) {
      if (riskSystemDTO.getId() != null) {
        resultInSideDto = updateRiskSystemWeb(null, riskSystemDTO);
      } else {
        resultInSideDto.setKey(RESULT.ERROR);
      }
    }
    return resultInSideDto;
  }

  private RiskSystemDTO validateImportInfo(RiskSystemDTO importDTO,
      Map<String, String> mapImportDTO) {
    String resultImport = "";
    if (StringUtils.isStringNullOrEmpty(importDTO.getSystemCode())) {
      resultImport = resultImport.concat(I18n.getLanguage("riskSystem.err.systemCode"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getSystemName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("riskSystem.err.systemName"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getScheduleStr())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("riskSystem.err.schedule"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getSystemPriorityName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("riskSystem.err.systemPriority"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getCountryName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("riskSystem.err.country"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getLastUpdateTimeStr())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("riskSystem.err.lastUpdateTime"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getManageUserName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("riskSystem.err.manageUser"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getActionName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("riskSystem.err.actionName"));
    }
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      importDTO.setResultImport(resultImport);
      return importDTO;
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getSystemCode())) {
      String systemCode = importDTO.getSystemCode();
      if (!isValidSystemCode(systemCode)) {
        importDTO.setResultImport(I18n.getLanguage("riskSystem.err.systemCode.notValid"));
        return importDTO;
      } else {
        char cf = systemCode.charAt(0);
        char ce = systemCode.charAt(systemCode.length() - 1);
        if (cf == '_') {
          importDTO.setResultImport(I18n.getLanguage("riskSystem.err.systemCode.underscoreFirst"));
          return importDTO;
        }
        if (ce == '_') {
          importDTO.setResultImport(I18n.getLanguage("riskSystem.err.systemCode.underscoreEnd"));
          return importDTO;
        }
        if (systemCode.length() > 100) {
          importDTO.setResultImport(I18n.getLanguage("riskSystem.err.systemCode.length"));
          return importDTO;
        }
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getSystemName())) {
      if (importDTO.getSystemName().length() > 100) {
        importDTO.setResultImport(I18n.getLanguage("riskSystem.err.systemName.length"));
        return importDTO;
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getScheduleStr())) {
      if (importDTO.getScheduleStr().length() > 10) {
        importDTO.setResultImport(I18n.getLanguage("riskSystem.err.schedule.lenght"));
        return importDTO;
      } else if (!StringUtils.isLong(importDTO.getScheduleStr())) {
        importDTO.setResultImport(I18n.getLanguage("riskSystem.err.schedule.valid"));
        return importDTO;
      } else if (Long.valueOf(importDTO.getScheduleStr()) < 0L) {
        importDTO.setResultImport(I18n.getLanguage("riskSystem.err.schedule.valid"));
        return importDTO;
      }
      importDTO.setSchedule(Long.valueOf(importDTO.getScheduleStr()));
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getSystemPriorityName())) {
      setMapSystemPriority();
      for (String systemPriority : mapSystemPriority.keySet()) {
        if (importDTO.getSystemPriorityName().equals(mapSystemPriority.get(systemPriority))) {
          importDTO.setSystemPriority(Long.valueOf(systemPriority));
        }
      }
    }
    if (importDTO.getSystemPriority() == null) {
      importDTO.setResultImport(I18n.getLanguage("riskSystem.err.systemPriority.valid"));
      return importDTO;
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getCountryName())) {
      setMapCountry();
      for (Long countryId : mapCountry.keySet()) {
        if (importDTO.getCountryName().equals(mapCountry.get(countryId))) {
          importDTO.setCountryId(countryId);
        }
      }
    }
    if (importDTO.getCountryId() == null) {
      importDTO.setResultImport(I18n.getLanguage("riskSystem.err.country.valid"));
      return importDTO;
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getLastUpdateTimeStr())) {
      try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = sdf.parse(importDTO.getLastUpdateTimeStr());
        if (!importDTO.getLastUpdateTimeStr().equals(sdf.format(date)) || StringUtils
            .isNotNullOrEmpty(
                DataUtil.validateDateTimeDdMmYyyy_HhMmSs(importDTO.getLastUpdateTimeStr()))) {
          importDTO.setResultImport(I18n.getLanguage("riskSystem.err.lastUpdateTime.valid"));
          return importDTO;
        } else {
          importDTO.setLastUpdateTime(date);
        }
      } catch (ParseException e) {
        log.error(e.getMessage(), e);
        importDTO.setResultImport(I18n.getLanguage("riskSystem.err.lastUpdateTime.valid"));
        return importDTO;
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getManageUserName())) {
      String[] arrUserName = importDTO.getManageUserName().split(",");
      List<RiskSystemDetailDTO> listDetail = new ArrayList<>();
      Map<Integer, String> mapUser = new HashMap<>();
      for (int i = 0; i < arrUserName.length; i++) {
        UsersEntity user = userRepository.getUserByUserName(arrUserName[i]);
        if (user != null) {
          RiskSystemDetailDTO detailDTO = new RiskSystemDetailDTO();
          detailDTO.setManageUserId(user.getUserId());
          detailDTO.setManageUnitId(user.getUnitId());
          listDetail.add(detailDTO);
          mapUser.put(i, arrUserName[i]);
        } else {
          importDTO.setResultImport(
              I18n.getLanguage("riskSystem.err.manageUser.valid") + " " + arrUserName[i]);
          return importDTO;
        }
      }
      if (!mapUser.isEmpty()) {
        String user = "";
        boolean existedUser = false;
        for (int key : mapUser.keySet()) {
          if (!user.equals(mapUser.get(key))) {
            user = mapUser.get(key);
          } else {
            existedUser = true;
            break;
          }
        }
        if (existedUser) {
          importDTO.setResultImport(I18n.getLanguage("riskSystem.err.manageUser.exist"));
          return importDTO;
        }
      }
      importDTO.setListRiskSystemDetail(listDetail);
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getActionName())) {
      if (I18n.getLanguage("riskSystem.action.0").equals(importDTO.getActionName())) {
        importDTO.setAction(0L);
      } else if (I18n.getLanguage("riskSystem.action.1").equals(importDTO.getActionName())) {
        importDTO.setAction(1L);
      } else {
        importDTO.setResultImport(I18n.getLanguage("riskSystem.err.action.valid"));
        return importDTO;
      }
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getSystemCode()) && StringUtils
        .isNotNullOrEmpty(importDTO.getSystemName())) {
      RiskSystemDTO dtoTmp = riskSystemRepository.checkRiskSystemExit(importDTO);
      if (dtoTmp != null) {
        if (I18n.getLanguage("riskSystem.action.0").equals(importDTO.getActionName())) {
          importDTO.setResultImport(I18n.getLanguage("riskSystem.err.dup-code"));
          return importDTO;
        }
        if (I18n.getLanguage("riskSystem.action.1").equals(importDTO.getActionName())) {
          importDTO.setId(dtoTmp.getId());
        }
      } else {
        if (I18n.getLanguage("riskSystem.action.1").equals(importDTO.getActionName())) {
          importDTO.setResultImport(I18n.getLanguage("riskSystem.err.notExist"));
          return importDTO;
        }
      }
    }
    String validateDuplicate = validateDuplicateImport(importDTO, mapImportDTO);
    if (StringUtils.isNotNullOrEmpty(validateDuplicate)) {
      importDTO.setResultImport(validateDuplicate);
      return importDTO;
    }
    return importDTO;
  }

  private boolean isValidSystemCode(String systemCode) {
    String regex = "([A-Za-z0-9_]+)";
    return systemCode.matches(regex);
  }

  private String validateDuplicateImport(RiskSystemDTO importDTO,
      Map<String, String> mapImportDTO) {
    String systemCode = importDTO.getSystemCode();
    String key = systemCode;
    String value = mapImportDTO.get(key);
    if (StringUtils.isNotNullOrEmpty(value)) {
      return I18n.getLanguage("riskSystem.err.dup-code-in-file").replaceAll("0", value);
    }
    return null;
  }

  private String checkResultImport(String resultImport) {
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      resultImport = resultImport.concat("; \n");
    }
    return resultImport;
  }

  public void setMapSystemPriority() {
    Datatable dataSystemPriority = catItemRepository
        .getItemMaster(Constants.CATEGORY.RISK_SYSTEM_PRIORITY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataSystemPriority.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapSystemPriority.put(dto.getItemValue(), dto.getItemName());
      }
    }
  }

  public void setMapCountry() {
    Datatable dataCountry = catItemRepository
        .getItemMaster(RISK_MASTER_CODE.GNOC_COUNTRY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataCountry.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapCountry.put(dto.getItemId(), dto.getItemName());
      }
    }
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
    if (!(I18n.getLanguage("riskSystem.STT"))
        .equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskSystem.systemCode") + " (*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskSystem.systemName") + " (*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskSystem.schedule") + " (*)")
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskSystem.systemPriorityName") + " (*)")
        .equalsIgnoreCase(obj[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskSystem.countryName") + " (*)")
        .equalsIgnoreCase(obj[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskSystem.lastUpdateTimeStr") + " (*)")
        .equalsIgnoreCase(obj[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskSystem.manageUserName") + " (*)")
        .equalsIgnoreCase(obj[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("riskSystem.actionName") + " (*)")
        .equalsIgnoreCase(obj[8].toString().trim())) {
      return false;
    }
    return true;
  }

  public File handleFileExport(List<RiskSystemDTO> list, String[] columnExport, String date,
      String code) throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = "";
    String title = "";
    String fileNameOut = "";
    String headerPrefix = "language.riskSystem";
    String firstLeftHeader = I18n.getLanguage("riskSystem.export.firstLeftHeader");
    String secondLeftHeader = I18n.getLanguage("riskSystem.export.secondLeftHeader");
    String firstRightHeader = I18n.getLanguage("riskSystem.export.firstRightHeader");
    String secondRightHeader = I18n.getLanguage("riskSystem.export.secondRightHeader");
    int mergeTitleEndIndex = 6;
    int startRow = 7;
    int cellTitleIndex = 3;
    String subTitle;
    if (StringUtils.isNotNullOrEmpty(date)) {
      subTitle = I18n.getLanguage("riskSystem.export.exportDate", date);
    } else {
      subTitle = "";
    }
    switch (code) {
      case "RESULT_IMPORT":
        sheetName = I18n.getLanguage("riskSystem.import.sheetname");
        title = I18n.getLanguage("riskSystem.import.title");
        fileNameOut = I18n.getLanguage("riskSystem.import.fileNameOut");
        break;
      case "EXPORT_RISK_SYSTEM":
        sheetName = I18n.getLanguage("riskSystem.export.sheetname");
        title = I18n.getLanguage("riskSystem.export.title");
        fileNameOut = I18n.getLanguage("riskSystem.export.fileNameOut");
        break;
      default:
        break;
    }
    ConfigFileExport configFileExport = new ConfigFileExport(
        list,
        sheetName,
        title,
        subTitle,
        startRow,
        cellTitleIndex,
        mergeTitleEndIndex,
        true,
        headerPrefix,
        lstHeaderSheet1,
        fieldSplit,
        "",
        firstLeftHeader,
        secondLeftHeader,
        firstRightHeader,
        secondRightHeader
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(startRow, 0, 0, 0,
        I18n.getLanguage("riskSystem.STT"), "HEAD", "STRING");
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

  public String compareObjectData(Object objSrc, Object objDes) {
    String content = "";
    try {
      Field[] k = objSrc.getClass().getDeclaredFields();
      for (int i = 0; i < k.length; i++) {
        try {
          String newO = (String) PropertyUtils.getSimpleProperty(objSrc, k[i].getName());
          String oldO = (String) PropertyUtils.getSimpleProperty(objDes, k[i].getName());
          if (!oldO.equals(newO) && !k[i].getName().equals("lstFileName")) {
            content += "{";
            content += k[i].getName() + ": " + (oldO == "" ? "null" : oldO)
                + " -> " + (newO == "" ? "null" : newO) + "}" + "\r\n";
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return content;
  }

  private void setFileName(RiskSystemDTO dto) {
    GnocFileDto gnocFileDto = new GnocFileDto();
    gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.RISK_SYSTEM);
    gnocFileDto.setBusinessId(dto.getId());
    List<GnocFileEntity> listEntity = gnocFileRepository.getLstGnocFileByDto(gnocFileDto);
    String fileName = null;
    if (listEntity != null && listEntity.size() > 0) {
      fileName = "";
      for (GnocFileEntity entity : listEntity) {
        String fullPath = entity.getPath();
        fileName +=
            fullPath.substring(fullPath.lastIndexOf(File.separator) + 1, fullPath.length()) + ", ";
      }
      fileName = fileName.trim();
      if (fileName.length() > 0) {
        fileName = fileName.substring(0, fileName.lastIndexOf(","));
      }
    }
    dto.setFileName(fileName);
  }

  //2020-10-03 hungtv add
  private List<String> getAllKeysDTO () {
    try {
      List<String> keys = Arrays.asList("id", "systemCode", "systemName", "schedule", "systemPriority", "lastUpdateTime",
          "countryId");
      return keys;
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }
  //end
}
