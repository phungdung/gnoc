package com.viettel.gnoc.kedb.business;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.viettel.gnoc.commons.business.MessagesBusiness;
import com.viettel.gnoc.commons.business.RolesBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.DeviceTypeVersionDTO;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ReceiveUnitDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.proxy.PtServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.DeviceTypeVersionRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.ReceiveUnitRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.repository.UserSmsRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.KEDB_MASTER_CODE;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.kedb.dto.KedbActionLogsDTO;
import com.viettel.gnoc.kedb.dto.KedbDTO;
import com.viettel.gnoc.kedb.dto.KedbExportDTO;
import com.viettel.gnoc.kedb.dto.KedbFilesDTO;
import com.viettel.gnoc.kedb.dto.KedbRatingInsideDTO;
import com.viettel.gnoc.kedb.dto.UserSmsDTO;
import com.viettel.gnoc.kedb.model.KedbFilesEntity;
import com.viettel.gnoc.kedb.repository.KedbActionLogsRepository;
import com.viettel.gnoc.kedb.repository.KedbRepository;
import com.viettel.gnoc.pt.dto.ProblemActionLogsDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import com.viettel.security.PassTranformer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Whitelist;
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
public class KedbBusinessImpl implements KedbBusiness {

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

  private int maxRecord = 500;

  @Autowired
  KedbRepository kedbRepository;

  @Autowired
  KedbActionLogsRepository kedbActionLogsRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  DeviceTypeVersionRepository deviceTypeVersionRepository;

  @Autowired
  UserSmsRepository userSmsRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  MessagesBusiness messagesBusiness;

  @Autowired
  RolesBusiness rolesBusiness;

  @Autowired
  KedbRatingBusiness kedbRatingBusiness;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  ReceiveUnitRepository receiveUnitRepository;

  @Autowired
  PtServiceProxy ptServiceProxy;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  GnocFileRepository gnocFileRepository;

  Map<Long, String> mapItemId = new HashMap<>();
  Map<Long, String> mapTypeName = new HashMap<>();
  Map<Long, String> mapTypeCode = new HashMap<>();
  Map<Long, String> mapSubCategoryName = new HashMap<>();
  Map<Long, String> mapSubCategoryCode = new HashMap<>();
  Map<Long, String> mapVendorName = new HashMap<>();
  Map<Long, String> mapHardwareVersion = new HashMap<>();
  Map<Long, String> mapSoftwareVersion = new HashMap<>();
  Map<Long, String> mapKedbStateName = new HashMap<>();
  Map<Long, String> mapParentTypeName = new HashMap<>();
  Map<Long, String> mapUnitCheckName = new HashMap<>();

  private final static String IMPORT_KEDB_RESULT = "IMPORT_KEDB_RESULT";
  private final static String KEDB_EXPORT = "KEDB_EXPORT";
  private final static String KEDB_SEQ = "KEDB_SEQ";

  @Override
  public Datatable getListKedbDTO(KedbDTO kedbDTO) {
    log.debug("Request to getListKedbDTO: {}", kedbDTO);
    Datatable datatable;
    Double offset = TimezoneContextHolder.getOffsetDouble();
    kedbDTO.setOffset(offset);
    datatable = kedbRepository.getListKedbDTO(kedbDTO);
    return datatable;
  }

  @Override
  public KedbDTO findKedbById(Long kedbId) {
    log.debug("Request to findKedbById: {}", kedbId);
    UserToken userToken = ticketProvider.getUserToken();
    String userName = userToken.getUserName();
    KedbDTO kedbDTO = kedbRepository.findKedbById(kedbId, userName);
    setOffsetKedbDTO(kedbDTO);
    GnocFileDto gnocFileDto = new GnocFileDto();
    gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.KEDB);
    gnocFileDto.setBusinessId(kedbDTO.getKedbId());
    kedbDTO.setGnocFileDtos(gnocFileRepository.getListGnocFileByDto(gnocFileDto));
    return kedbDTO;
  }

  @Override
  public ResultInSideDto insertKedb(List<MultipartFile> files, KedbDTO kedbDTO) throws Exception {
    log.debug("Request to insertKedb: {}", kedbDTO);
    ResultInSideDto resultInSideDto;
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    kedbDTO.setCreateUserId(userToken.getUserID());
    kedbDTO.setCreateUserName(userToken.getUserName());
    kedbDTO.setCreateUnitId(userToken.getDeptId());
    kedbDTO.setCreateUnitName(unitToken.getUnitName());
    setMapItemId();
    SimpleDateFormat spd = new SimpleDateFormat("yyMMdd");
    Date date = new Date();
    Long kedbId = Long.valueOf(kedbRepository.getSeqTableKedb(KEDB_SEQ));
    kedbDTO.setKedbId(kedbId);
    String kedbCode =
        "KEDB_" + mapItemId.get(kedbDTO.getTypeId()) + "_" + spd.format(date) + "_" + kedbId;
    kedbDTO.setKedbCode(kedbCode);
    kedbDTO.setCreatedTime(date);
    List<CatItemDTO> listKedbState = catItemRepository
        .getListItemByCategory(KEDB_MASTER_CODE.KEDB_STATE, null);
    Long idClose = 0L;
    Long insertApprove = 0L;
    Long updateApprove = 0L;
    for (CatItemDTO dto : listKedbState) {
      if (KEDB_MASTER_CODE.KEDB_CLOSED.equals(dto.getItemCode())) {
        idClose = dto.getItemId();
      } else if (KEDB_MASTER_CODE.KEDB_UPDATE_APPROVE.equals(dto.getItemCode())) {
        updateApprove = dto.getItemId();
      } else if (KEDB_MASTER_CODE.KEDB_CREATE_APPROVE.equals(dto.getItemCode())) {
        insertApprove = dto.getItemId();
      }
    }
    if (kedbDTO.getKedbState().equals(idClose)) {
      kedbDTO.setCompleter(userToken.getUserName());
      kedbDTO.setCompletedTime(date);
    }
    resultInSideDto = kedbRepository.doInsertKedb(kedbDTO);
    if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      throw new RuntimeException();
    }
    String typeCode = mapItemId.get(kedbDTO.getTypeId());
    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      KedbActionLogsDTO actionLogsDTO = new KedbActionLogsDTO();
      String stateName = "";
      CatItemDTO stateItem = new CatItemDTO();
      stateItem.setItemId(kedbDTO.getKedbState());
      List<CatItemDTO> lstKedbState = kedbRepository.getListCatItemDTO(stateItem);
      if (lstKedbState != null && !lstKedbState.isEmpty()) {
        stateName = lstKedbState.get(0).getItemName();
      }
      actionLogsDTO.setContent(I18n.getLanguage("kedb.common.insert") + " " + stateName);
      actionLogsDTO.setCreateTime(kedbDTO.getCreatedTime());
      actionLogsDTO.setCreateUnitName(unitToken.getUnitName());
      actionLogsDTO.setCreateUserName(userToken.getUserName());
      actionLogsDTO.setKedbId((kedbId));
      actionLogsDTO.setStatus(I18n.getLanguage("kedb.common.insert"));
      resultInSideDto = kedbActionLogsRepository.insertKedbActionLogs(actionLogsDTO);
      if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        throw new RuntimeException();
      }
      // Save file
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      for (MultipartFile multipartFile : files) {
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                multipartFile.getBytes(), kedbDTO.getCreatedTime());
        String fileName = multipartFile.getOriginalFilename();
        String content = getFileContent(multipartFile.getBytes(), fileName);
        //Start save file old
        String fullPathOld = FileUtils
            .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                uploadFolder, kedbDTO.getCreatedTime());
        KedbFilesDTO kedbFilesDTO = new KedbFilesDTO();
        kedbFilesDTO.setKedbFileName(FileUtils.getFileName(fullPathOld));
        kedbFilesDTO.setContent(content);
        kedbFilesDTO.setCreateUnitId(kedbDTO.getCreateUnitId());
        kedbFilesDTO.setCreateUnitName(kedbDTO.getCreateUnitName());
        kedbFilesDTO.setCreateUserId(kedbDTO.getCreateUserId());
        kedbFilesDTO.setCreateUserName(kedbDTO.getCreateUserName());
        kedbFilesDTO.setCreateTime(kedbDTO.getCreatedTime());
        kedbFilesDTO.setKedbId(kedbId);
        ResultInSideDto resultFileDataOld = kedbRepository.insertKedbFiles(kedbFilesDTO);
        //End save file old
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(fileName);
        gnocFileDto.setContent(content);
        gnocFileDto.setCreateUnitId(userToken.getDeptId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(userToken.getUserID());
        gnocFileDto.setCreateUserName(userToken.getUserName());
        gnocFileDto.setCreateTime(kedbDTO.getCreatedTime());
        gnocFileDto.setMappingId(resultFileDataOld.getId());
        gnocFileDtos.add(gnocFileDto);
      }
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.KEDB, kedbId, gnocFileDtos);
      List<UserSmsDTO> lstUser2SendSMS = userSmsRepository
          .getUserReceiveSMSEmailByTypeCode(typeCode, Constants.SMS);
      String smsContent = "KEDB: " + kedbDTO.getKedbCode() + " (" + kedbDTO.getKedbName() + ") ";
      resultInSideDto = messagesBusiness
          .insertSMSMessageKEDBInst(smsContent, lstUser2SendSMS, "kedb.add");
      if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        throw new RuntimeException();
      }
      if (kedbDTO.getKedbState().equals(insertApprove) || kedbDTO.getKedbState()
          .equals(updateApprove)) {
        String sms = I18n.getLanguage("kedb.approve") + " " +
            kedbDTO.getKedbCode() + " (" + kedbDTO.getKedbName() + ")";
        String temp = Constants.KEDB_ALIAS;
        String alias = "";
        List<CatItemDTO> listCatItem = catItemRepository
            .getListItemByCategory(CATEGORY.GNOC_ALIAS, null);
        if (listCatItem != null && !listCatItem.isEmpty()) {
          for (CatItemDTO catItemDTO : listCatItem) {
            if (temp.equals(catItemDTO.getItemCode())) {
              alias = catItemDTO.getItemCode();
              break;
            }
          }
        }
        List<UsersInsideDto> listUsersInsideDto = userRepository
            .getListUsersDTO(String.valueOf(kedbDTO.getUnitCheckId()),
                "unit");
        messagesBusiness.insertMessageForUser(sms, alias, listUsersInsideDto);
      }
      try {
        if (kedbDTO.getProblemsInsideDTO() != null) {
          ProblemsInsideDTO problemsInsideDTO = kedbDTO.getProblemsInsideDTO();
          problemsInsideDTO.setInsertKedb("YES");
          problemsInsideDTO.setRelatedKedb(kedbCode);
          problemsInsideDTO.setContent(I18n.getLanguage("kedb.updateProblems"));
          problemsInsideDTO.setUserUpdateName(userToken.getUserName());
          problemsInsideDTO.setUserUpdateId(String.valueOf(userToken.getUserID()));
          problemsInsideDTO.setUnitUpdateId(String.valueOf(userToken.getDeptId()));
          problemsInsideDTO.setUnitUpdateName(unitToken.getUnitName());
          problemsInsideDTO.setCreateUnitId(userToken.getDeptId());
          problemsInsideDTO.setCreateUnitName(unitToken.getUnitName());
          ptServiceProxy.updateProblems(problemsInsideDTO);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        throw new RuntimeException(e.getMessage());
      }
    }
    resultInSideDto.setId(kedbId);
    resultInSideDto.setObject(kedbDTO);
    return resultInSideDto;
  }

  public void setMapItemId() {
    List<CatItemDTO> lstAllCatItem = catItemRepository.getListItemByCategory("", "");
    if (lstAllCatItem != null && !lstAllCatItem.isEmpty()) {
      for (CatItemDTO dto : lstAllCatItem) {
        mapItemId.put(dto.getItemId(), dto.getItemCode());
      }
    }
  }

  @Override
  public ResultInSideDto updateKedb(List<MultipartFile> files, KedbDTO kedbDTO) throws Exception {
    log.debug("Request to updateKedb: {}", kedbDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    Date date = new Date();
    setOffsetKedbDTO(kedbDTO);
    kedbDTO.setCompletedTime(kedbDTO.getCompletedTime());
    List<CatItemDTO> listKedbState = catItemRepository
        .getListItemByCategory(KEDB_MASTER_CODE.KEDB_STATE, null);
    Long idClose = 0L;
    Long idCancel = 0L;
    Long insertApprove = 0L;
    Long updateApprove = 0L;
    for (CatItemDTO dto : listKedbState) {
      if (KEDB_MASTER_CODE.KEDB_CLOSED.equals(dto.getItemCode())) {
        idClose = dto.getItemId();
      } else if (KEDB_MASTER_CODE.KEDB_UPDATE_APPROVE.equals(dto.getItemCode())) {
        updateApprove = dto.getItemId();
      } else if (KEDB_MASTER_CODE.KEDB_CREATE_APPROVE.equals(dto.getItemCode())) {
        insertApprove = dto.getItemId();
      } else if (KEDB_MASTER_CODE.KEDB_CANCELED.equals(dto.getItemCode())) {
        idCancel = dto.getItemId();
      }
    }
    if (kedbDTO.getKedbState().equals(idClose) && !kedbDTO.getKedbState()
        .equals(kedbDTO.getKedbStateBeforeUpdate())) {
      kedbDTO.setCompleter(userToken.getUserName());
      kedbDTO.setCompletedTime(date);
    } else {
      kedbDTO.setCompleter("");
    }
    List<RolesDTO> lstRole = rolesBusiness
        .getListRolePmByUserOfKedb(String.valueOf(userToken.getUserID()));
    if (lstRole == null || lstRole.isEmpty()) {
      resultInSideDto.setKey(RESULT.NOT_ACCESS);
      return resultInSideDto;
    }
    String admin = "";
    String subAdmin = "";
    String userAdmin = "";
    for (RolesDTO rdto : lstRole) {
      if (KEDB_MASTER_CODE.ADMIN_KEDB.equals(rdto.getRoleCode().toUpperCase())) {
        admin = KEDB_MASTER_CODE.ADMIN_KEDB;
      } else if (KEDB_MASTER_CODE.SUB_ADMIN_KEDB.equals(rdto.getRoleCode().toUpperCase())) {
        subAdmin = KEDB_MASTER_CODE.SUB_ADMIN_KEDB;
      } else if (KEDB_MASTER_CODE.USER_KEDB.equals(rdto.getRoleCode().toUpperCase())) {
        userAdmin = KEDB_MASTER_CODE.USER_KEDB;
      }
    }
    admin = "".equals(admin) == true ? ("".equals(subAdmin) == true ? ("".equals(userAdmin) == true
        ? ("") : userAdmin) : subAdmin) : admin;
    if (KEDB_MASTER_CODE.USER_KEDB.equals(admin)) {
      admin = KEDB_MASTER_CODE.USER_KEDB;
    } else if ((KEDB_MASTER_CODE.SUB_ADMIN_KEDB.equals(admin) && userToken.getDeptId()
        .equals(kedbDTO.getUnitCheckId())) || KEDB_MASTER_CODE.ADMIN_KEDB.equals(admin)) {
      admin = KEDB_MASTER_CODE.ADMIN_KEDB;
    }
    if (!KEDB_MASTER_CODE.ADMIN_KEDB.equals(admin) && (idClose.equals(kedbDTO.getKedbState()) ||
        idCancel.equals(kedbDTO.getKedbState()))) {
      resultInSideDto.setKey(RESULT.NOT_ACCESS);
      return resultInSideDto;
    } else {
      resultInSideDto = kedbRepository.doUpdateKedb(kedbDTO);
      if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        try {
          if (kedbDTO.getProblemsInsideDTO() != null) {
            ProblemsInsideDTO problemsInsideDTO = kedbDTO.getProblemsInsideDTO();
            ProblemActionLogsDTO problemActionLogsDTO = new ProblemActionLogsDTO();
            problemActionLogsDTO.setProblemActionLogsId(null);
            problemActionLogsDTO.setContent(I18n.getLanguage("kedb.updateProblems"));
            problemActionLogsDTO.setCreateTime((new Date()));
            problemActionLogsDTO.setCreateUnitId((userToken.getDeptId()));
            problemActionLogsDTO.setCreateUserId((userToken.getUserID()));
            problemActionLogsDTO.setType(problemsInsideDTO.getStateName());
            problemActionLogsDTO.setProblemId((problemsInsideDTO.getProblemId()));
            problemActionLogsDTO.setCreaterUnitName(unitToken.getUnitName());
            problemActionLogsDTO.setCreaterUserName(userToken.getUserName());
            ptServiceProxy.insertProblemActionLogs(problemActionLogsDTO);
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          throw new RuntimeException(e.getMessage());
        }
        String stateName = kedbRepository.setStateName(kedbDTO);
        KedbActionLogsDTO actionLogsDTO = new KedbActionLogsDTO();
        actionLogsDTO.setContent(I18n.getLanguage("kedb.common.update") + " " + stateName);
        actionLogsDTO.setCreateTime((new Date()));
        actionLogsDTO.setCreateUnitName(unitToken.getUnitName());
        actionLogsDTO.setCreateUserName(userToken.getUserName());
        actionLogsDTO.setKedbId((kedbDTO.getKedbId()));
        if (kedbDTO.getProblemsInsideDTO() != null) {
          actionLogsDTO.setStatus(I18n.getLanguage("kedb.common.update") + " PT");
        } else {
          actionLogsDTO.setStatus(I18n.getLanguage("kedb.common.update"));
        }
        resultInSideDto = kedbActionLogsRepository.insertKedbActionLogs(actionLogsDTO);
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          throw new RuntimeException();
        }
        setMapItemId();
        String typeCode = mapItemId.get(kedbDTO.getTypeId());
        List<UserSmsDTO> lstUser2SendSMS = userSmsRepository
            .getUserReceiveSMSEmailByTypeCode(typeCode, Constants.SMS);
        String smsContent =
            "KEDB: " + kedbDTO.getKedbCode() + " (" + kedbDTO.getKedbName() + ") ";
        if (kedbDTO.getKedbState().equals(idClose) && !kedbDTO.getKedbState()
            .equals(kedbDTO.getKedbStateBeforeUpdate())) {
          resultInSideDto = messagesBusiness.insertSMSMessageKEDBInst(smsContent, lstUser2SendSMS,
              I18n.getLanguage("kedb.closed"));
          if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            throw new RuntimeException();
          }
        } else if (kedbDTO.getKedbState().equals(idCancel) && !kedbDTO.getKedbState()
            .equals(kedbDTO.getKedbStateBeforeUpdate())) {
          resultInSideDto = messagesBusiness.insertSMSMessageKEDBInst(smsContent, lstUser2SendSMS,
              I18n.getLanguage("kedb.cancel"));
          if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            throw new RuntimeException();
          }
        } else if ((kedbDTO.getKedbState().equals(insertApprove) || kedbDTO.getKedbState()
            .equals(updateApprove))
            && !kedbDTO.getKedbState().equals(kedbDTO.getKedbStateBeforeUpdate())) {
          String sms =
              I18n.getLanguage("kedb.approve") + " " + kedbDTO.getKedbCode() + " (" + kedbDTO
                  .getKedbName() + ")";
          String temp = Constants.KEDB_ALIAS;
          String alias = "";
          List<CatItemDTO> lstCatItem = catItemRepository
              .getListItemByCategory(Constants.GNOC_ALIAS, null);
          if (lstCatItem != null && !lstCatItem.isEmpty()) {
            for (CatItemDTO catItemDTO : lstCatItem) {
              if (temp.equals(catItemDTO.getItemCode())) {
                alias = catItemDTO.getItemCode();
                break;
              }
            }
          }
          List<UsersInsideDto> lst = userRepository
              .getListUsersDTO(String.valueOf(kedbDTO.getUnitCheckId()), "unit");
          resultInSideDto = messagesBusiness.insertMessageForUser(sms, alias, lst);
          if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            throw new RuntimeException();
          }
        }
        //Start save file old
//        List<KedbFilesEntity> kedbFilesEntitiesOld = kedbRepository
//            .getListKedbFilesByKedbId(kedbDTO.getKedbId());
//        List<Long> listKedbFileIdOld = kedbFilesEntitiesOld.stream()
//            .map(KedbFilesEntity::getKedbFileId).collect(Collectors.toList());
//        List<GnocFileDto> gnocFileDtosWeb = kedbDTO.getGnocFileDtos();
//        List<Long> listIdFileIdNew = gnocFileDtosWeb.stream()
//            .map(GnocFileDto::getMappingId).collect(Collectors.toList());
//        listKedbFileIdOld.removeAll(listIdFileIdNew);
        for (Long aLong : kedbDTO.getIdDeleteList()) {
          KedbFilesEntity kedbFilesEntity = kedbRepository.findKedbFilesById(aLong);
          if (userToken.getUserID().equals(kedbFilesEntity.getCreateUserId())) {
            resultInSideDto = kedbRepository.deleteKedbFiles(kedbFilesEntity);
            if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
              throw new RuntimeException();
            }
          } else {
            resultInSideDto.setKey(RESULT.NO_CAN_DELETE);
            return resultInSideDto;
          }
        }
        //End save file old
        List<GnocFileDto> gnocFileDtos = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                  multipartFile.getBytes(), date);
          String fileName = multipartFile.getOriginalFilename();
          String content = getFileContent(multipartFile.getBytes(), fileName);
          //Start save file old
          String fullPathOld = FileUtils
              .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                  uploadFolder, date);
          KedbFilesDTO kedbFilesDTO = new KedbFilesDTO();
          kedbFilesDTO.setKedbFileName(FileUtils.getFileName(fullPathOld));
          kedbFilesDTO.setContent(content);
          kedbFilesDTO.setCreateUnitId(userToken.getDeptId());
          kedbFilesDTO.setCreateUnitName(unitToken.getUnitName());
          kedbFilesDTO.setCreateUserId(userToken.getUserID());
          kedbFilesDTO.setCreateUserName(userToken.getUserName());
          kedbFilesDTO.setCreateTime(date);
          kedbFilesDTO.setKedbId(kedbDTO.getKedbId());
          ResultInSideDto resultFileDataOld = kedbRepository.insertKedbFiles(kedbFilesDTO);
          //End save file old
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(fileName);
          gnocFileDto.setContent(content);
          gnocFileDto.setCreateUnitId(userToken.getDeptId());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(userToken.getUserID());
          gnocFileDto.setCreateUserName(userToken.getUserName());
          gnocFileDto.setCreateTime(date);
          gnocFileDto.setMappingId(resultFileDataOld.getId());
          gnocFileDtos.add(gnocFileDto);
        }
        gnocFileRepository.deleteListGnocFile(GNOC_FILE_BUSSINESS.KEDB, kedbDTO.getKedbId(),
            kedbDTO.getIdDeleteList());
        gnocFileRepository
            .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.KEDB, kedbDTO.getKedbId(),
                gnocFileDtos);
        resultInSideDto.setKey(RESULT.SUCCESS);
        if (kedbDTO.getKedbRatingInsideDTO() != null) {
          KedbRatingInsideDTO kedbRatingInsideDTO = kedbDTO.getKedbRatingInsideDTO();
          kedbRatingInsideDTO.setKedbId(kedbDTO.getKedbId());
          kedbRatingInsideDTO.setUserName(userToken.getUserName());
          kedbRatingInsideDTO = kedbRatingBusiness.insertOrUpdateKedbRating(kedbRatingInsideDTO);
          if (!RESULT.SUCCESS.equals(kedbRatingInsideDTO.getResult())) {
            throw new RuntimeException();
          }
        }
      } else {
        throw new RuntimeException();
      }
    }
    return resultInSideDto;
  }

  public static void setTimeZoneKedb(KedbDTO kedbDTO, Double offset) throws Exception {
    SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    if (kedbDTO.getCreatedTime() != null && !"".equals(kedbDTO.getCreatedTime())
        && kedbDTO.getKedbId() != null) {
      Date d = spd.parse(DateUtil.date2ddMMyyyyHHMMss(kedbDTO.getCreatedTime()));
      kedbDTO.setCreatedTime(
          spd.parse(DateUtil
              .date2ddMMyyyyHHMMss(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)))));
    }
    if (kedbDTO.getCompletedTime() != null && !"".equals(kedbDTO.getCompletedTime())) {
      Date d = spd.parse(DateUtil.date2ddMMyyyyHHMMss(kedbDTO.getCompletedTime()));
      kedbDTO.setCompletedTime(
          spd.parse(DateUtil
              .date2ddMMyyyyHHMMss(new Date(d.getTime() + (long) (offset * 60 * 60 * 1000)))));
    }
  }

  public String getFileContent(byte[] bytesContent, String fileName) throws Exception {
    String result = "";
    String tempPathFile = FileUtils.saveTempFile(fileName, bytesContent, tempFolder);
    File file = new File(tempPathFile);
    if (fileName.endsWith(".txt")) {
      BufferedReader br = null;
      FileReader temp = null;
      try {
        temp = new FileReader(file);
        br = new BufferedReader(temp);
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
          sb.append(line);
          sb.append("\n");
          line = br.readLine();
        }
        result = sb.toString();
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      } finally {
        if (br != null) {
          br.close();
        }
        if (temp != null) {
          temp.close();
        }
      }
    } else if (fileName.endsWith(".docx")) {
      FileInputStream fis = null;
      XWPFDocument xdoc = null;
      try {
        fis = new FileInputStream(file);
        xdoc = new XWPFDocument(OPCPackage.open(fis));
        List<XWPFParagraph> paragraphs = xdoc.getParagraphs();
        for (XWPFParagraph para : paragraphs) {
          result += para.getText();
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      } finally {
        if (xdoc != null) {
          xdoc.close();
        }
        if (fis != null) {
          fis.close();
        }
      }
    } else if (fileName.toLowerCase().endsWith(".doc")) {
      FileInputStream fis = null;
      HWPFDocument doc = null;
      WordExtractor extractor = null;
      try {
        fis = new FileInputStream(file);
        doc = new HWPFDocument(fis);
        extractor = new WordExtractor(doc);
        String[] paragraphs = extractor.getParagraphText();
        for (String value : paragraphs) {
          result += value;
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      } finally {
        if (extractor != null) {
          extractor.close();
        }
        if (doc != null) {
          doc.close();
        }
        if (fis != null) {
          fis.close();
        }
      }
    } else if (fileName.toLowerCase().endsWith(".xls")) {
      FileInputStream fis = null;
      HSSFWorkbook workbook = null;
      try {
        fis = new FileInputStream(file);
        workbook = new HSSFWorkbook(fis);
        int sheetNum = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetNum; i++) {
          HSSFSheet sheet = workbook.getSheetAt(i);
          Iterator rows = sheet.rowIterator();
          while (rows.hasNext()) {
            HSSFRow row = (HSSFRow) rows.next();
            Iterator cells = row.cellIterator();
            while (cells.hasNext()) {
              HSSFCell cell = (HSSFCell) cells.next();
              switch (cell.getCellType()) {
                case NUMERIC: {
                  result += cell.getNumericCellValue();
                  break;
                }
                case STRING: {
                  result += cell.getStringCellValue();
                  break;
                }
                case FORMULA: {
                  result += cell.getCellFormula();
                  break;
                }
                case BOOLEAN: {
                  result += cell.getBooleanCellValue();
                  break;
                }
              }
            }
          }
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      } finally {
        if (workbook != null) {
          workbook.close();
        }
        if (fis != null) {
          fis.close();
        }
      }
    } else if (fileName.toLowerCase().endsWith(".xlsx")) {
      FileInputStream fis = null;
      XSSFWorkbook wb = null;
      try {
        fis = new FileInputStream(file);
        wb = new XSSFWorkbook(fis);
        int sheetNum = wb.getNumberOfSheets();
        for (int i = 0; i < sheetNum; i++) {
          XSSFSheet sheet = wb.getSheetAt(i);
          for (Row row : sheet) {
            for (Cell cell : row) {
              switch (cell.getCellType()) {
                case NUMERIC: {
                  result += cell.getNumericCellValue();
                  break;
                }
                case STRING: {
                  result += cell.getRichStringCellValue().getString();
                  break;
                }
                case FORMULA: {
                  result += cell.getCellFormula();
                  break;
                }
                case BOOLEAN: {
                  result += cell.getBooleanCellValue();
                  break;
                }
              }
            }
          }
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      } finally {
        if (wb != null) {
          wb.close();
        }
        if (fis != null) {
          fis.close();
        }
      }
    } else if (fileName.toLowerCase().endsWith(".pdf")) {
      FileInputStream fis = null;
      PdfReader reader = null;
      try {
        fis = new FileInputStream(file);
        reader = new PdfReader(fis);
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
          result += PdfTextExtractor.getTextFromPage(reader, i);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      } finally {
        if (reader != null) {
          reader.close();
        }
        if (fis != null) {
          fis.close();
        }
      }
    }
    return result;
  }

  @Override
  public ResultInSideDto deleteKedb(Long kedbId) {
    log.debug("Request to deleteKedb: {}", kedbId);
    return kedbRepository.deleteKedb(kedbId);
  }

  @Override
  public ResultInSideDto deleteListKedb(List<Long> listKedbId) {
    log.debug("Request to deleteListKedb: {}", listKedbId);
    ResultInSideDto result = new ResultInSideDto();
    for (Long id : listKedbId) {
      result = deleteKedb(id);
    }
    return result;
  }

  @Override
  public List<String> getSequenseKedb() {
    log.debug("Request to getSequenseKedb: {}");
    return kedbRepository.getSequenseKedb(KEDB_SEQ, 1);
  }

  @Override
  public ResultInSideDto insertOrUpdateListKedb(List<KedbDTO> listKedbDTO) {
    log.debug("Request to insertOrUpdateListKedb: {}", listKedbDTO);
    return kedbRepository.insertOrUpdateListKedb(listKedbDTO);
  }

  @Override
  public String getOffset(UserTokenGNOCSimple userTokenGNOC) {
    log.debug("Request to getOffset: {}", userTokenGNOC);
    return kedbRepository.getOffset(userTokenGNOC);
  }

  @Override
  public List<KedbDTO> synchKedbByCreateTime(String fromDate, String toDate) {
    log.debug("Request to synchKedbByCreateTime: {}", fromDate, toDate);
    return kedbRepository.synchKedbByCreateTime(fromDate, toDate);
  }

  @Override
  public List<KedbDTO> getListKedbByCondition(List<ConditionBean> lstCondition, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    log.debug("Request to getListKedbByCondition: {}", lstCondition);
    return kedbRepository
        .getListKedbByCondition(lstCondition, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public File exportData(KedbDTO kedbDTO) throws Exception {
    List<KedbExportDTO> listExport = new ArrayList<>();
    Double offset = setOffsetKedbDTO(kedbDTO);
    kedbDTO.setOffset(offset);
    List<KedbDTO> list = kedbRepository.getListKedbExport(kedbDTO);
    if (list != null && !list.isEmpty()) {
      for (KedbDTO dto : list) {
        KedbExportDTO kedbExportDTO = new KedbExportDTO();
        kedbExportDTO.setKedbCode(dto.getKedbCode());
        kedbExportDTO.setKedbName(dto.getKedbName());
        kedbExportDTO.setParentTypeName(dto.getParentTypeName());
        String description = "";
        if (StringUtils.isNotNullOrEmpty(dto.getDescription())) {
          description = extractHtmlTag(dto.getDescription()).trim();
        }
        kedbExportDTO.setDescription(description);
        String influenceScope = "";
        if (StringUtils.isNotNullOrEmpty(dto.getInfluenceScope())) {
          influenceScope = extractHtmlTag(dto.getInfluenceScope()).trim();
        }
        kedbExportDTO.setInfluenceScope(influenceScope);
        kedbExportDTO.setCreateUserName(dto.getCreateUserName());
        kedbExportDTO.setReceiveUserName(dto.getReceiveUserName());
        kedbExportDTO.setPtTtRelated(dto.getPtTtRelated());
        kedbExportDTO.setTypeName(dto.getTypeName());
        kedbExportDTO.setSubCategoryName(dto.getSubCategoryName());
        kedbExportDTO.setVendorName(dto.getVendorName());
        kedbExportDTO.setSoftwareVersion(dto.getSoftwareVersion());
        String ttWa = "";
        if (StringUtils.isNotNullOrEmpty(dto.getTtWa())) {
          ttWa = extractHtmlTag(dto.getTtWa()).trim();
        }
        kedbExportDTO.setTtWa(ttWa);
        String rca = "";
        if (StringUtils.isNotNullOrEmpty(dto.getRca())) {
          rca = extractHtmlTag(dto.getRca()).trim();
        }
        kedbExportDTO.setRca(rca);
        String ptWa = "";
        if (StringUtils.isNotNullOrEmpty(dto.getPtWa())) {
          ptWa = extractHtmlTag(dto.getPtWa()).trim();
        }
        kedbExportDTO.setPtWa(ptWa);
        String solution = "";
        if (StringUtils.isNotNullOrEmpty(dto.getSolution())) {
          solution = extractHtmlTag(dto.getSolution()).trim();
        }
        kedbExportDTO.setSolution(solution);
        String worklog = "";
        if (StringUtils.isNotNullOrEmpty(dto.getWorklog())) {
          worklog = extractHtmlTag(dto.getWorklog()).trim();
        }
        kedbExportDTO.setWorklog(worklog);
        kedbExportDTO.setCreatedTime(DateUtil.date2ddMMyyyyHHMMss(dto.getCreatedTime()));
        kedbExportDTO.setKedbStateName(dto.getKedbStateName());
        kedbExportDTO.setHardwareVersion(dto.getHardwareVersion());
        kedbExportDTO.setCompleter(dto.getCompleter());
        kedbExportDTO.setCompletedTime(DateUtil.date2ddMMyyyyHHMMss(dto.getCompletedTime()));
        listExport.add(kedbExportDTO);
      }
    }
    String[] header = new String[]{"kedbCode", "kedbName", "parentTypeName", "description",
        "createUserName", "ptTtRelated", "typeName", "subCategoryName", "vendorName", "rca",
        "solution", "createdTime", "kedbStateName", "hardwareVersion", "softwareVersion",
        "completer", "completedTime"};
    return handleFileExport(listExport, header, kedbDTO.getFromDate(), kedbDTO.getToDate());
  }

  private Double setOffsetKedbDTO(KedbDTO kedbDTO) {
    Double offset;
    try {
      offset = TimezoneContextHolder.getOffsetDouble();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      offset = 0D;
    }
    try {
      setTimeZoneKedb(kedbDTO, offset);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return offset;
  }

  private String extractHtmlTag(String value) {
    Document docDes = Jsoup.parse(value);
    docDes.outputSettings(new OutputSettings().prettyPrint(false));
    docDes.select("br").after("\\n");
    docDes.select("p").before("\\n");
    String des = docDes.html().replaceAll("\\\\n", "\n");
    String field = (Jsoup.clean(des, "", Whitelist.none(), new OutputSettings().prettyPrint(false)))
        .replaceAll("&nbsp;", " ");
    return field;
  }

  private void setMaxRecord() {
    List<CatItemDTO> listImportKedb = catItemRepository
        .getListItemByCategory(KEDB_MASTER_CODE.MAX_IMPORT_KEDB, null);
    if (listImportKedb != null && !listImportKedb.isEmpty()) {
      maxRecord = Integer.parseInt(listImportKedb.get(0).getItemValue());
    } else {
      maxRecord = 500;
    }
  }

  @Override
  public File getTemplate() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetKedb = workBook.getSheetAt(0);
    XSSFSheet sheetOrther = workBook.createSheet("Orther");

    Map<String, List<String>> categoryTypeSub = new HashMap<>();
    Map<String, String> categoryTypeName = new HashMap<>();
    Datatable dataType = catItemRepository
        .getItemMaster(KEDB_MASTER_CODE.PT_TYPE,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> lstType = (List<CatItemDTO>) dataType.getData();
    List<String> lstTypeCode = new ArrayList<>();
    List<String> lstTypeName = new ArrayList<>();
    List<Long> lstTypeId = new ArrayList<>();
    for (int i = 0; i < lstType.size(); i++) {
      String typeCode = lstType.get(i).getItemCode();
      char c = typeCode.charAt(0);
      if (Character.isDigit(c)) {
        typeCode = "_" + typeCode;
      }
      lstTypeCode.add(typeCode);
      lstTypeName.add(lstType.get(i).getItemName());
      lstTypeId.add(lstType.get(i).getItemId());
      List<String> lstSubCategoryCode = new ArrayList<>();
      List<CatItemDTO> lstSubCategory = kedbRepository.getListSubCategory(lstTypeId.get(i));
      if (lstSubCategory != null && !lstSubCategory.isEmpty()) {
        for (CatItemDTO dto : lstSubCategory) {
          lstSubCategoryCode.add(dto.getItemCode());
        }
        categoryTypeSub.put(lstTypeCode.get(i), lstSubCategoryCode);
        categoryTypeName.put(lstTypeCode.get(i), lstTypeName.get(i));
      }
    }
    XSSFSheet sheetTypeSub = workBook.createSheet("SheetTypeSub");
    Row ro1;
    String col1;
    String ref1;
    int c1 = 0;
    for (String key : categoryTypeSub.keySet()) {
      int r1 = 0;
      ro1 = sheetTypeSub.getRow(r1);
      if (ro1 == null) {
        ro1 = sheetTypeSub.createRow(r1);
      }
      r1++;
      ro1.createCell(c1).setCellValue(key);
      List<String> items = categoryTypeSub.get(key);
      if (items.size() > 0) {
        for (String item : items) {
          ro1 = sheetTypeSub.getRow(r1);
          if (ro1 == null) {
            ro1 = sheetTypeSub.createRow(r1);
          }
          r1++;
          ro1.createCell(c1).setCellValue(item);
        }
      }
      col1 = CellReference.convertNumToColString(c1);
      Name type = workBook.createName();
      type.setNameName(key);
      ref1 = "SheetTypeSub!$" + col1 + "$2:$" + col1 + "$" + r1;
      type.setRefersToFormula(ref1);
      c1++;
    }
    col1 = CellReference.convertNumToColString((c1 - 1));
    Name subCategory = workBook.createName();
    subCategory.setNameName("Type");
    ref1 = "SheetTypeSub!$A$1:$" + col1 + "$1";
    subCategory.setRefersToFormula(ref1);
    sheetTypeSub.setSelected(false);
    workBook.setSheetHidden(2, true);
    workBook.setActiveSheet(0);

    String[] header = new String[]{
        I18n.getLanguage("kedb.STT"),
        I18n.getLanguage("kedb.temp.kedbName"),
        I18n.getLanguage("kedb.parentTypeName"),
        I18n.getLanguage("kedb.typeName"),
        I18n.getLanguage("kedb.subCategoryName"),
        I18n.getLanguage("kedb.vendorName"),
        I18n.getLanguage("kedb.hardwareVersion"),
        I18n.getLanguage("kedb.softwareVersion"),
        I18n.getLanguage("kedb.kedbStateName"),
        I18n.getLanguage("kedb.ptTtRelated"),
        I18n.getLanguage("kedb.description"),
        I18n.getLanguage("kedb.temp.rca"),
        I18n.getLanguage("kedb.solution"),
        I18n.getLanguage("kedb.unitCheckName")
    };

    String[] headerStar = new String[]{
        I18n.getLanguage("kedb.temp.kedbName"),
        I18n.getLanguage("kedb.parentTypeName"),
        I18n.getLanguage("kedb.typeName"),
        I18n.getLanguage("kedb.subCategoryName"),
        I18n.getLanguage("kedb.vendorName"),
        I18n.getLanguage("kedb.hardwareVersion"),
        I18n.getLanguage("kedb.softwareVersion"),
        I18n.getLanguage("kedb.kedbStateName"),
        I18n.getLanguage("kedb.description"),
        I18n.getLanguage("kedb.temp.rca"),
        I18n.getLanguage("kedb.solution"),
        I18n.getLanguage("kedb.unitCheckName")
    };

    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetKedb);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int parentTypeNameColumn = listHeader.indexOf(I18n.getLanguage("kedb.parentTypeName"));
    int typeNameColumn = listHeader.indexOf(I18n.getLanguage("kedb.typeName"));
    int subCategoryNameColumn = listHeader.indexOf(I18n.getLanguage("kedb.subCategoryName"));
    int vendorNameColumn = listHeader.indexOf(I18n.getLanguage("kedb.vendorName"));
    int hardwareVersionColumn = listHeader.indexOf(I18n.getLanguage("kedb.hardwareVersion"));
    int softwareVersionColumn = listHeader.indexOf(I18n.getLanguage("kedb.softwareVersion"));
    int kedbStateNameColumn = listHeader.indexOf(I18n.getLanguage("kedb.kedbStateName"));
    int kedbUnitCheckNameColumn = listHeader.indexOf(I18n.getLanguage("kedb.unitCheckName"));

    //Tao tieu de
    sheetKedb.addMergedRegion(new CellRangeAddress(0, 0, 0, listHeader.size() - 1));
    Row titleRow = sheetKedb.createRow(0);
    titleRow.setHeightInPoints(45);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("kedb.import.title"));
    titleCell.setCellStyle(styles.get("title"));
    titleCell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);

    //Tao note
    sheetKedb.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row noteRow = sheetKedb.createRow(2);
    noteRow.setHeightInPoints(18);
    Cell noteCell = noteRow.createCell(0);
    noteCell.setCellValue(I18n.getLanguage("importfile.template.excel.note"));
    noteCell.setCellStyle(styles.get("note"));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());
    //Tao header
    Row headerRow = sheetKedb.createRow(4);
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
      sheetKedb.setColumnWidth(i, 7000);
    }
    sheetKedb.setColumnWidth(0, 3000);

    ewu.createCell(sheetOrther, 0, 0, I18n.getLanguage("kedb.parentTypeName").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0, I18n.getLanguage("kedb.parentTypeCode").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 2, 0, I18n.getLanguage("kedb.typeName").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 3, 0, I18n.getLanguage("kedb.typeCode").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 4, 0, I18n.getLanguage("kedb.subCategoryName").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 5, 0, I18n.getLanguage("kedb.subCategoryCode").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 6, 0, I18n.getLanguage("kedb.vendorName").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 7, 0, I18n.getLanguage("kedb.hardwareVersion").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 8, 0, I18n.getLanguage("kedb.softwareVersion").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 9, 0, I18n.getLanguage("kedb.kedbStateName").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 10, 0, I18n.getLanguage("kedb.kedbStateCode").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 11, 0, I18n.getLanguage("kedb.unitCheckName").toUpperCase(),
        styles.get("header"));

    setMaxRecord();

    int row = 1;
    Datatable dataParentType = catItemRepository
        .getItemMaster(KEDB_MASTER_CODE.ARRAY_BHKN,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> listParentType = (List<CatItemDTO>) dataParentType.getData();
    for (CatItemDTO dto : listParentType) {
      ewu.createCell(sheetOrther, 0, row++, dto.getItemName(), styles.get("cell"));
    }
    Name parentTypeName = workBook.createName();
    parentTypeName.setNameName("parentTypeName");
    parentTypeName.setRefersToFormula("Orther!$A$2:$A$" + row);
    XSSFDataValidationConstraint parentTypeConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "parentTypeName");
    CellRangeAddressList cellRangeParentType = new CellRangeAddressList(5, (maxRecord + 5),
        parentTypeNameColumn, parentTypeNameColumn);
    XSSFDataValidation dataValidationParentType = (XSSFDataValidation) dvHelper.createValidation(
        parentTypeConstraint, cellRangeParentType);
    dataValidationParentType.setShowErrorBox(true);
    sheetKedb.addValidationData(dataValidationParentType);

    row = 1;
    for (CatItemDTO dto : listParentType) {
      ewu.createCell(sheetOrther, 1, row++, dto.getItemCode(), styles.get("cell"));
    }

    XSSFDataValidationConstraint typeNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "Type");
    CellRangeAddressList cellRangeType = new CellRangeAddressList(
        5, (maxRecord + 5), typeNameColumn, typeNameColumn);
    XSSFDataValidation dataValidationType = (XSSFDataValidation) dvHelper.createValidation(
        typeNameConstraint, cellRangeType);
    dataValidationType.setShowErrorBox(true);
    sheetKedb.addValidationData(dataValidationType);

    row = 1;
    for (CatItemDTO dto : lstType) {
      ewu.createCell(sheetOrther, 2, row++, dto.getItemName(), styles.get("cell"));
    }
    row = 1;
    for (CatItemDTO dto : lstType) {
      ewu.createCell(sheetOrther, 3, row++, dto.getItemCode(), styles.get("cell"));
    }

    Datatable dataSubCategory = catItemRepository
        .getItemMaster(KEDB_MASTER_CODE.PT_SUB_CATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> listSubCategory = (List<CatItemDTO>) dataSubCategory.getData();
    List<Long> listSubCategoryId = new ArrayList<>();
    row = 1;
    for (CatItemDTO dto : listSubCategory) {
      listSubCategoryId.add(dto.getItemId());
      ewu.createCell(sheetOrther, 4, row++, dto.getItemName(), styles.get("cell"));
    }
    row = 1;
    for (CatItemDTO dto : listSubCategory) {
      ewu.createCell(sheetOrther, 5, row++, dto.getItemCode(), styles.get("cell"));
    }
    XSSFDataValidationConstraint subCategoryNameConstraint;
    CellRangeAddressList cellRangeSubCategory;
    XSSFDataValidation dataValidationSubCategory;
    for (int i = 6; i <= (maxRecord + 6); i++) {
      subCategoryNameConstraint = new XSSFDataValidationConstraint(
          DataValidationConstraint.ValidationType.LIST, "INDIRECT($D$" + i + ")");
      cellRangeSubCategory = new CellRangeAddressList(
          i - 1, i - 1, subCategoryNameColumn, subCategoryNameColumn);
      dataValidationSubCategory = (XSSFDataValidation) dvHelper.createValidation(
          subCategoryNameConstraint, cellRangeSubCategory);
      dataValidationSubCategory.setShowErrorBox(true);
      sheetKedb.addValidationData(dataValidationSubCategory);
    }

    Datatable dataVendor = catItemRepository
        .getItemMaster(KEDB_MASTER_CODE.VENDOR,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> listVendor = (List<CatItemDTO>) dataVendor.getData();
    row = 1;
    List<Long> listVendorId = new ArrayList<>();
    for (CatItemDTO dto : listVendor) {
      listVendorId.add(dto.getItemId());
      ewu.createCell(sheetOrther, 6, row++, dto.getItemCode(), styles.get("cell"));
    }
    Name vendorName = workBook.createName();
    vendorName.setNameName("vendorName");
    vendorName.setRefersToFormula("Orther!$G$2:$G$" + row);
    XSSFDataValidationConstraint vendorNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "vendorName");
    CellRangeAddressList cellRangeVendor = new CellRangeAddressList(5, (maxRecord + 5),
        vendorNameColumn, vendorNameColumn);
    XSSFDataValidation dataValidationVendor = (XSSFDataValidation) dvHelper.createValidation(
        vendorNameConstraint, cellRangeVendor);
    dataValidationVendor.setShowErrorBox(true);
    sheetKedb.addValidationData(dataValidationVendor);

    DeviceTypeVersionDTO deviceTypeVersionDTO = new DeviceTypeVersionDTO();
    List<DeviceTypeVersionDTO> listDeviceTypeVersionDTO =
        deviceTypeVersionRepository.getListDeviceTypeVersion(deviceTypeVersionDTO);
    row = 1;
    List<String> listHardwareVersionAll = new ArrayList<>();
    List<String> listSoftwareVersionAll = new ArrayList<>();
    for (int i = 0; i < listDeviceTypeVersionDTO.size(); i++) {
      if (i == 0) {
        listHardwareVersionAll.add(listDeviceTypeVersionDTO.get(0).getHardwareVersion().trim());
        listSoftwareVersionAll.add(listDeviceTypeVersionDTO.get(0).getSoftwareVersion().trim());
      } else {
        if (!listHardwareVersionAll
            .contains(listDeviceTypeVersionDTO.get(i).getHardwareVersion().trim())) {
          listHardwareVersionAll.add(listDeviceTypeVersionDTO.get(i).getHardwareVersion());
        }
        if (!listSoftwareVersionAll
            .contains(listDeviceTypeVersionDTO.get(i).getSoftwareVersion().trim())) {
          listSoftwareVersionAll.add(listDeviceTypeVersionDTO.get(i).getSoftwareVersion());
        }
      }
    }
    for (String hv : listHardwareVersionAll) {
      ewu.createCell(sheetOrther, 7, row++, hv, styles.get("cell"));
    }
    Name hardwareVersion = workBook.createName();
    hardwareVersion.setNameName("hardwareVersion");
    hardwareVersion.setRefersToFormula("Orther!$H$2:$H$" + row);
    XSSFDataValidationConstraint hardwareVersionConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "hardwareVersion");
    CellRangeAddressList cellRangeHardwareVersion = new CellRangeAddressList(5, (maxRecord + 5),
        hardwareVersionColumn, hardwareVersionColumn);
    XSSFDataValidation dataValidationHardwareVersion = (XSSFDataValidation) dvHelper
        .createValidation(
            hardwareVersionConstraint, cellRangeHardwareVersion);
    dataValidationHardwareVersion.setShowErrorBox(true);
    sheetKedb.addValidationData(dataValidationHardwareVersion);

    row = 1;
    for (String sv : listSoftwareVersionAll) {
      ewu.createCell(sheetOrther, 8, row++, sv, styles.get("cell"));
    }
    Name softwareVersion = workBook.createName();
    softwareVersion.setNameName("softwareVersion");
    softwareVersion.setRefersToFormula("Orther!$I$2:$I$" + row);
    XSSFDataValidationConstraint softwareVersionConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "softwareVersion");
    CellRangeAddressList cellRangeSoftwareVersion = new CellRangeAddressList(5, (maxRecord + 5),
        softwareVersionColumn, softwareVersionColumn);
    XSSFDataValidation dataValidationSoftwareVersion = (XSSFDataValidation) dvHelper
        .createValidation(
            softwareVersionConstraint, cellRangeSoftwareVersion);
    dataValidationSoftwareVersion.setShowErrorBox(true);
    sheetKedb.addValidationData(dataValidationSoftwareVersion);

    Datatable dataKedbState = catItemRepository
        .getItemMaster(KEDB_MASTER_CODE.KEDB_STATE,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> listKedbState = (List<CatItemDTO>) dataKedbState.getData();
    row = 1;
    for (CatItemDTO dto : listKedbState) {
      ewu.createCell(sheetOrther, 9, row++, dto.getItemName(), styles.get("cell"));
    }
    row = 1;
    for (CatItemDTO dto : listKedbState) {
      ewu.createCell(sheetOrther, 10, row++, dto.getItemCode(), styles.get("cell"));
    }
    Name kedbState = workBook.createName();
    kedbState.setNameName("kedbState");
    kedbState.setRefersToFormula("Orther!$J$2:$J$" + row);
    XSSFDataValidationConstraint kedbStateConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "kedbState");
    CellRangeAddressList cellRangeKedbState = new CellRangeAddressList(5, (maxRecord + 5),
        kedbStateNameColumn, kedbStateNameColumn);
    XSSFDataValidation dataValidationKedbState = (XSSFDataValidation) dvHelper.createValidation(
        kedbStateConstraint, cellRangeKedbState);
    dataValidationKedbState.setShowErrorBox(true);
    sheetKedb.addValidationData(dataValidationKedbState);

    List<UnitDTO> listUnitCheckKedbAll = getAllListUnitCheckKedb();
    List<UnitDTO> listUnitCheckKedb = new ArrayList<>();
    List<String> listUnitCheckKedbCode = new ArrayList<>();
    row = 1;
    for (int i = 0; i < listUnitCheckKedbAll.size(); i++) {
      if (i == 0) {
        listUnitCheckKedbCode.add(listUnitCheckKedbAll.get(0).getUnitCode().trim());
        listUnitCheckKedb.add(listUnitCheckKedbAll.get(0));
      } else {
        if (!listUnitCheckKedbCode.contains(listUnitCheckKedbAll.get(i).getUnitCode().trim())) {
          listUnitCheckKedbCode.add(listUnitCheckKedbAll.get(i).getUnitCode());
          listUnitCheckKedb.add(listUnitCheckKedbAll.get(i));
        }
      }
    }
    for (UnitDTO dto : listUnitCheckKedb) {
      ewu.createCell(sheetOrther, 11, row++, dto.getUnitName() + "(" +
          dto.getUnitCode() + ")", styles.get("cell"));
    }
    Name unitCheckKedb = workBook.createName();
    unitCheckKedb.setNameName("unitCheckKedb");
    unitCheckKedb.setRefersToFormula("Orther!$L$2:$L$" + row);
    XSSFDataValidationConstraint unitCheckKedbConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "unitCheckKedb");
    CellRangeAddressList cellRangeUnitCheckKedb = new CellRangeAddressList(5, (maxRecord + 5),
        kedbUnitCheckNameColumn, kedbUnitCheckNameColumn);
    XSSFDataValidation dataValidationUnitCheckKedb = (XSSFDataValidation) dvHelper.createValidation(
        unitCheckKedbConstraint, cellRangeUnitCheckKedb);
    dataValidationUnitCheckKedb.setShowErrorBox(true);
    sheetKedb.addValidationData(dataValidationUnitCheckKedb);

    for (int i = 0; i <= 11; i++) {
      sheetOrther.autoSizeColumn(i);
    }

    sheetOrther.setSelected(false);
    workBook.setSheetName(0, I18n.getLanguage("kedb.import.title"));

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_KEDB" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public List<CatItemDTO> getListSubCategory(Long typeId) {
    log.debug("Request to getListSubCategory: {}", typeId);
    return kedbRepository.getListSubCategory(typeId);
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile, List<MultipartFile> files) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    List<KedbDTO> listKedbDto;
    List<KedbExportDTO> listKedbExportDto;

    String[] header = new String[]{"kedbName", "parentTypeName", "typeName", "subCategoryName",
        "vendorName", "hardwareVersion", "softwareVersion", "kedbStateName", "ptTtRelated",
        "description", "rca", "solution", "unitCheckName", "resultImport"};

    setMaxRecord();

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
            0, 13, 1000);

        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }

        List<Object[]> lstData = CommonImport.getDataFromExcelFile(fileImp, 0, 5,
            0, 13, 1000);

        if (lstData.size() > maxRecord) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(maxRecord));
          return resultInSideDto;
        }

        listKedbDto = new ArrayList<>();
        listKedbExportDto = new ArrayList<>();

        if (!lstData.isEmpty()) {
          int index = 0;
          setMapParentTypeName();
          setMapTypeName();
          setMapSubCategoryName();
          setMapVendorName();
          setMapHardwareVersion();
          setMapSoftwareVersion();
          setMapKedbStateName();
          setMapUnitCheckName();
          for (Object[] obj : lstData) {
            KedbDTO kedbDTO = new KedbDTO();
            KedbExportDTO kedbExportDTO = new KedbExportDTO();

            if (obj[1] != null) {
              kedbExportDTO.setKedbName(obj[1].toString().trim());
              kedbDTO.setKedbName(kedbExportDTO.getKedbName());
            } else {
              kedbExportDTO.setKedbName(null);
            }
            if (obj[2] != null) {
              kedbExportDTO.setParentTypeName(obj[2].toString().trim());
              for (Map.Entry<Long, String> item : mapParentTypeName.entrySet()) {
                if (kedbExportDTO.getParentTypeName().equals(item.getValue())) {
                  kedbDTO.setParentTypeId(item.getKey());
                  break;
                }
              }
            } else {
              kedbExportDTO.setParentTypeName(null);
            }
            if (obj[3] != null) {
              String typeName = obj[3].toString().trim();
              String c0 = "_";
              char c1 = typeName.charAt(1);
              if (c0.equals(String.valueOf(typeName.charAt(0))) && Character.isDigit(c1)) {
                typeName = typeName.substring(1);
              }
              kedbExportDTO.setTypeName(typeName);
              for (Map.Entry<Long, String> item : mapTypeName.entrySet()) {
                if (kedbExportDTO.getTypeName().equals(item.getValue())) {
                  kedbDTO.setTypeId(item.getKey());
                  break;
                }
              }
            } else {
              kedbDTO.setTypeId(null);
              kedbExportDTO.setTypeName(null);
            }
            if (obj[4] != null) {
              kedbExportDTO.setSubCategoryName(obj[4].toString().trim());
              for (Map.Entry<Long, String> item : mapSubCategoryName.entrySet()) {
                if (kedbExportDTO.getSubCategoryName().equals(item.getValue())) {
                  kedbDTO.setSubCategoryId(item.getKey());
                  break;
                }
              }
            } else {
              kedbExportDTO.setSubCategoryName(null);
            }
            if (obj[5] != null) {
              kedbExportDTO.setVendorName(obj[5].toString().trim());
              for (Map.Entry<Long, String> item : mapVendorName.entrySet()) {
                if (kedbExportDTO.getVendorName().equals(item.getValue())) {
                  kedbDTO.setVendor(String.valueOf(item.getKey()));
                  break;
                }
              }
            } else {
              kedbDTO.setVendor(null);
              kedbExportDTO.setVendorName(null);
            }
            if (obj[6] != null) {
              kedbExportDTO.setHardwareVersion(obj[6].toString().trim());
              for (Map.Entry<Long, String> item : mapHardwareVersion.entrySet()) {
                if (kedbExportDTO.getHardwareVersion().equals(item.getValue())) {
                  kedbDTO.setHardwareVersion(item.getValue());
                  break;
                }
              }
            } else {
              kedbExportDTO.setHardwareVersion(null);
            }
            if (obj[7] != null) {
              kedbExportDTO.setSoftwareVersion(obj[7].toString().trim());
              for (Map.Entry<Long, String> item : mapSoftwareVersion.entrySet()) {
                if (kedbExportDTO.getSoftwareVersion().equals(item.getValue())) {
                  kedbDTO.setSoftwareVersion(item.getValue());
                  break;
                }
              }
            } else {
              kedbExportDTO.setSoftwareVersion(null);
            }
            if (obj[8] != null) {
              kedbExportDTO.setKedbStateName(obj[8].toString().trim());
              for (Map.Entry<Long, String> item : mapKedbStateName.entrySet()) {
                if (kedbExportDTO.getKedbStateName().equals(item.getValue())) {
                  kedbDTO.setKedbState(item.getKey());
                  break;
                }
              }
            } else {
              kedbExportDTO.setKedbStateName(null);
            }
            if (obj[9] != null) {
              kedbExportDTO.setPtTtRelated(obj[9].toString().trim());
              kedbDTO.setPtTtRelated(kedbExportDTO.getPtTtRelated());
            } else {
              kedbExportDTO.setPtTtRelated(null);
            }
            if (obj[10] != null) {
              kedbExportDTO.setDescription(obj[10].toString().trim());
              kedbDTO.setDescription(kedbExportDTO.getDescription());
            } else {
              kedbExportDTO.setDescription(null);
            }
            if (obj[11] != null) {
              kedbExportDTO.setRca(obj[11].toString().trim());
              kedbDTO.setRca(kedbExportDTO.getRca());
            } else {
              kedbExportDTO.setRca(null);
            }
            if (obj[12] != null) {
              kedbExportDTO.setSolution(obj[12].toString().trim());
              kedbDTO.setSolution(kedbExportDTO.getSolution());
            } else {
              kedbExportDTO.setSolution(null);
            }
            if (obj[13] != null) {
              kedbExportDTO.setUnitCheckName(obj[13].toString().trim());
              for (Map.Entry<Long, String> item : mapUnitCheckName.entrySet()) {
                if (kedbExportDTO.getUnitCheckName().equals(item.getValue())) {
                  kedbDTO.setUnitCheckId(item.getKey());
                  kedbDTO.setUnitCheckName(kedbExportDTO.getUnitCheckName());
                  break;
                }
              }
            } else {
              kedbExportDTO.setUnitCheckName(null);
            }

            KedbExportDTO exportDTO = validateImportInfo(kedbExportDTO, kedbDTO);
            if (exportDTO.getResultImport() == null) {
              exportDTO.setResultImport(I18n.getLanguage("kedb.result.import.ok"));
              listKedbExportDto.add(exportDTO);
              listKedbDto.add(kedbDTO);
            } else {
              listKedbExportDto.add(exportDTO);
              index++;
            }
          }
          if (index == 0) {
            if (!listKedbDto.isEmpty()) {
              for (KedbDTO kedbDTO : listKedbDto) {
                resultInSideDto = insertKedb(files, kedbDTO);
              }
            }
          } else {
            File fileExport = handleFileImport(listKedbExportDto, header);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = handleFileImport(listKedbExportDto, header);
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
  public List<UnitDTO> getListUnitCheckKedb(KedbDTO kedbDTO) {
    List<UnitDTO> listUnitCheckKedb = new ArrayList<>();
    List<CatItemDTO> listUnitCheck = catItemRepository
        .getListItemByCategory(KEDB_MASTER_CODE.UNIT_CHECK_KEDB, null);
    setMapTypeCode();
    setMapSubCategoryCode();
    String itemCode = mapTypeCode.get(kedbDTO.getTypeId()) + "#" + mapSubCategoryCode
        .get(kedbDTO.getSubCategoryId());
    String description = "";
    for (CatItemDTO dto : listUnitCheck) {
      if (itemCode.equals(dto.getItemCode())) {
        description = dto.getDescription();
        break;
      }
    }
    if (StringUtils.isNotNullOrEmpty(description)) {
      List<String> listUnitCode = Arrays.asList(description.split(";"));
      for (String unitCode : listUnitCode) {
        UnitDTO unitDTO = unitRepository.getUnitDTOByUnitCode(unitCode);
        if (unitDTO != null) {
          listUnitCheckKedb.add(unitDTO);
        }
      }
    }
    return listUnitCheckKedb;
  }

  public List<UnitDTO> getAllListUnitCheckKedb() {
    List<UnitDTO> listUnitCheckKedb = new ArrayList<>();
    List<CatItemDTO> listUnitCheck = catItemRepository
        .getListItemByCategory(KEDB_MASTER_CODE.UNIT_CHECK_KEDB, null);
    List<String> listDes = new ArrayList<>();
    for (CatItemDTO dto : listUnitCheck) {
      listDes.add(dto.getDescription());
    }
    for (String description : listDes) {
      List<String> listUnitCode = Arrays.asList(description.split(";"));
      for (String unitCode : listUnitCode) {
        UnitDTO unitDTO = unitRepository.getUnitDTOByUnitCode(unitCode);
        if (unitDTO != null) {
          listUnitCheckKedb.add(unitDTO);
        }
      }
    }
    return listUnitCheckKedb;
  }

  private KedbExportDTO validateImportInfo(KedbExportDTO kedbExportDTO, KedbDTO kedbDTO) {
    String resultImport = "";
    if (StringUtils.isStringNullOrEmpty(kedbExportDTO.getKedbName())) {
      resultImport = resultImport.concat(I18n.getLanguage("kedb.err.kedbName"));
    }
    if (StringUtils.isStringNullOrEmpty(kedbExportDTO.getParentTypeName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("kedb.err.parentTypeName"));
    }
    if (kedbDTO.getParentTypeId() == null && StringUtils
        .isNotNullOrEmpty(kedbExportDTO.getParentTypeName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("kedb.err.parentTypeName.validate"));
    }
    if (StringUtils.isStringNullOrEmpty(kedbExportDTO.getTypeName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("kedb.err.typeName"));
    }
    if (StringUtils.isStringNullOrEmpty(kedbExportDTO.getSubCategoryName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("kedb.err.subCategoryName"));
    }
    if (kedbDTO.getSubCategoryId() == null && StringUtils
        .isNotNullOrEmpty(kedbExportDTO.getSubCategoryName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("kedb.err.subCategoryName.notExist"));
    }
    if (StringUtils.isStringNullOrEmpty(kedbExportDTO.getVendorName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("kedb.err.vendorName"));
    }
    if (StringUtils.isStringNullOrEmpty(kedbExportDTO.getHardwareVersion())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("kedb.err.hardwareVersion"));
    }
    if (StringUtils.isStringNullOrEmpty(kedbExportDTO.getSoftwareVersion())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("kedb.err.softwareVersion"));
    }
    if (StringUtils.isStringNullOrEmpty(kedbExportDTO.getKedbStateName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("kedb.err.kedbStateName"));
    }
    if (kedbDTO.getKedbState() == null && StringUtils
        .isNotNullOrEmpty(kedbExportDTO.getKedbStateName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("kedb.err.kedbStateName.validate"));
    }
    if (StringUtils.isStringNullOrEmpty(kedbExportDTO.getDescription())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("kedb.err.description"));
    }
    if (StringUtils.isStringNullOrEmpty(kedbExportDTO.getRca())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("kedb.err.rca"));
    }
    if (StringUtils.isStringNullOrEmpty(kedbExportDTO.getSolution())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("kedb.err.solution"));
    }
    if (StringUtils.isStringNullOrEmpty(kedbExportDTO.getUnitCheckName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("kedb.err.unitCheckName"));
    }
    if (StringUtils.isStringNullOrEmpty(kedbDTO.getTypeId())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("kedb.err.typeName.null"));
    }
    if (StringUtils.isStringNullOrEmpty(kedbDTO.getVendor())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("kedb.err.vendorName.null"));
    }
    if (StringUtils.isNotNullOrEmpty(kedbExportDTO.getKedbName())) {
      if (kedbExportDTO.getKedbName().length() > 500) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("kedb.err.kedbName.length"));
      }
    }
    if (StringUtils.isNotNullOrEmpty(kedbExportDTO.getPtTtRelated())) {
      if (kedbExportDTO.getPtTtRelated().length() > 500) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("kedb.err.ptTtRelated.length"));
      }
    }
    if (StringUtils.isNotNullOrEmpty(kedbExportDTO.getDescription())) {
      if (kedbExportDTO.getDescription().length() > 4000) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("kedb.err.description.length"));
      }
    }
    if (StringUtils.isNotNullOrEmpty(kedbExportDTO.getRca())) {
      if (kedbExportDTO.getRca().length() > 4000) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("kedb.err.rca.length"));
      }
    }
    if (StringUtils.isNotNullOrEmpty(kedbExportDTO.getSolution())) {
      if (kedbExportDTO.getSolution().length() > 4000) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("kedb.err.solution.length"));
      }
    }
    if (StringUtils.isStringNullOrEmpty(resultImport)) {
      List<CatItemDTO> listSubCategory = getListSubCategory(kedbDTO.getTypeId());
      if (listSubCategory == null) {
        resultImport = resultImport.concat(I18n.getLanguage("kedb.err.subCategoryName.validate"));
      }
      DeviceTypeVersionDTO deviceTypeVersionDTO = new DeviceTypeVersionDTO();
      deviceTypeVersionDTO.setTypeId(kedbDTO.getSubCategoryId());
      deviceTypeVersionDTO.setVendorId(Long.valueOf(kedbDTO.getVendor()));
      List<DeviceTypeVersionDTO> listDeviceTypeVersionDTO = deviceTypeVersionRepository
          .getListDeviceTypeVersion(deviceTypeVersionDTO);
      List<String> listHardwareVersion = new ArrayList<>();
      List<String> listSoftwareVersion = new ArrayList<>();
      if (listDeviceTypeVersionDTO != null && !listDeviceTypeVersionDTO.isEmpty()) {
        for (DeviceTypeVersionDTO dto : listDeviceTypeVersionDTO) {
          listHardwareVersion.add(dto.getHardwareVersion());
          listSoftwareVersion.add(dto.getSoftwareVersion());
        }
        if (listHardwareVersion != null && !listHardwareVersion.isEmpty()) {
          if (!listHardwareVersion.contains(kedbDTO.getHardwareVersion())) {
            String hardware = "";
            for (int i = 0; i < listHardwareVersion.size(); i++) {
              if (i != 0) {
                hardware = hardware.concat(",");
              }
              hardware = hardware.concat(listHardwareVersion.get(i));
            }
            resultImport = checkResultImport(resultImport).concat(
                I18n.getLanguage("kedb.err.hardwareVersion.validate") + "; "
                    + I18n.getLanguage("kedb.suggest") + " "
                    + hardware
            );
          }
        } else {
          resultImport = checkResultImport(resultImport).concat(
              I18n.getLanguage("kedb.err.hardwareVersion.validate"));
        }
        if (listSoftwareVersion != null && !listSoftwareVersion.isEmpty()) {
          if (!listSoftwareVersion.contains(kedbDTO.getSoftwareVersion())) {
            String software = "";
            for (int i = 0; i < listSoftwareVersion.size(); i++) {
              if (i != 0) {
                software = software.concat(",");
              }
              software = software.concat(listSoftwareVersion.get(i));
            }
            resultImport = checkResultImport(resultImport).concat(
                I18n.getLanguage("kedb.err.softwareVersion.validate") + "; "
                    + I18n.getLanguage("kedb.suggest") + " "
                    + software
            );
          }
        } else {
          resultImport = checkResultImport(resultImport)
              .concat(I18n.getLanguage("kedb.err.softwareVersion.validate"));
        }
      } else {
        resultImport = resultImport.concat(I18n.getLanguage("kedb.err.deviceTypeVersion"));
      }
      List<UnitDTO> listUnitCheckKedb = getListUnitCheckKedb(kedbDTO);
      List<Long> listUnitCheckId = new ArrayList<>();
      List<String> listUnitCheckNameCode = new ArrayList<>();
      if (listUnitCheckKedb != null && !listUnitCheckKedb.isEmpty()) {
        for (UnitDTO dto : listUnitCheckKedb) {
          listUnitCheckId.add(dto.getUnitId());
          listUnitCheckNameCode.add(dto.getUnitName() + "(" + dto.getUnitCode() + ")");
          if (!listUnitCheckId.contains(kedbDTO.getUnitCheckId())) {
            String unit = "";
            for (int i = 0; i < listUnitCheckNameCode.size(); i++) {
              if (i != 0) {
                unit = unit.concat(",");
              }
              unit = unit.concat(listUnitCheckNameCode.get(i));
            }
            resultImport = checkResultImport(resultImport).concat(
                I18n.getLanguage("kedb.err.unitCheckKedb") + "; "
                    + I18n.getLanguage("kedb.suggest") + " "
                    + unit
            );
          }
        }
      } else {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("kedb.err.unitCheckKedb"));
      }
    }
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      kedbExportDTO.setResultImport(resultImport);
      return kedbExportDTO;
    }
    return kedbExportDTO;
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
    if (count != 14) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("kedb.STT")).equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("kedb.temp.kedbName") + "(*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("kedb.parentTypeName") + "(*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("kedb.typeName") + "(*)")
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("kedb.subCategoryName") + "(*)")
        .equalsIgnoreCase(obj[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("kedb.vendorName") + "(*)")
        .equalsIgnoreCase(obj[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("kedb.hardwareVersion") + "(*)")
        .equalsIgnoreCase(obj[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("kedb.softwareVersion") + "(*)")
        .equalsIgnoreCase(obj[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("kedb.kedbStateName") + "(*)")
        .equalsIgnoreCase(obj[8].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("kedb.ptTtRelated"))
        .equalsIgnoreCase(obj[9].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("kedb.description") + "(*)")
        .equalsIgnoreCase(obj[10].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("kedb.temp.rca") + "(*)")
        .equalsIgnoreCase(obj[11].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("kedb.solution") + "(*)")
        .equalsIgnoreCase(obj[12].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("kedb.unitCheckName") + "(*)")
        .equalsIgnoreCase(obj[13].toString().trim())) {
      return false;
    }

    return true;
  }

  public void setMapTypeCode() {
    Datatable dataType = catItemRepository
        .getItemMaster(KEDB_MASTER_CODE.PT_TYPE,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataType.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapTypeCode.put(dto.getItemId(), dto.getItemCode());
      }
    }
  }

  public void setMapTypeName() {
    Datatable dataType = catItemRepository
        .getItemMaster(KEDB_MASTER_CODE.PT_TYPE,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataType.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapTypeName.put(dto.getItemId(), dto.getItemCode());
      }
    }
  }

  public void setMapSubCategoryCode() {
    Datatable dataSubCategory = catItemRepository
        .getItemMasterHasParent(KEDB_MASTER_CODE.PT_SUB_CATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataSubCategory.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapSubCategoryCode.put(dto.getItemId(), dto.getItemCode());
      }
    }
  }

  public void setMapSubCategoryName() {
    Datatable dataSubCategory = catItemRepository
        .getItemMasterHasParent(KEDB_MASTER_CODE.PT_SUB_CATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataSubCategory.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapSubCategoryName.put(dto.getItemId(), dto.getItemCode());
      }
    }
  }

  public void setMapVendorName() {
    Datatable dataVendor = catItemRepository
        .getItemMaster(KEDB_MASTER_CODE.VENDOR,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataVendor.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapVendorName.put(dto.getItemId(), dto.getItemCode());
      }
    }
  }

  public void setMapHardwareVersion() {
    List<DeviceTypeVersionDTO> list =
        deviceTypeVersionRepository.getListDeviceTypeVersion(new DeviceTypeVersionDTO());
    if (list != null && !list.isEmpty()) {
      for (DeviceTypeVersionDTO dto : list) {
        mapHardwareVersion.put(dto.getDeviceTypeVersionId(), dto.getHardwareVersion());
      }
    }
  }

  public void setMapSoftwareVersion() {
    List<DeviceTypeVersionDTO> list =
        deviceTypeVersionRepository.getListDeviceTypeVersion(new DeviceTypeVersionDTO());
    if (list != null && !list.isEmpty()) {
      for (DeviceTypeVersionDTO dto : list) {
        mapSoftwareVersion.put(dto.getDeviceTypeVersionId(), dto.getSoftwareVersion());
      }
    }
  }

  public void setMapKedbStateName() {
    Datatable dataKedbState = catItemRepository
        .getItemMaster(KEDB_MASTER_CODE.KEDB_STATE,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataKedbState.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapKedbStateName.put(dto.getItemId(), dto.getItemName());
      }
    }
  }

  public void setMapParentTypeName() {
    Datatable dataParentType = catItemRepository
        .getItemMaster(KEDB_MASTER_CODE.ARRAY_BHKN,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataParentType.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapParentTypeName.put(dto.getItemId(), dto.getItemName());
      }
    }
  }

  public void setMapUnitCheckName() {
    List<ReceiveUnitDTO> lstReceiveUnit = receiveUnitRepository.getListReceiveUnit();
    if (lstReceiveUnit != null && !lstReceiveUnit.isEmpty()) {
      for (ReceiveUnitDTO dto : lstReceiveUnit) {
        mapUnitCheckName.put(dto.getUnitId(), dto.getUnitName() + "(" + dto.getUnitCode() + ")");
      }
    }
  }

  private File handleFileExport(List<KedbExportDTO> listKedbExportDTO, String[] columnExport,
      String fromDate, String toDate) throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = I18n.getLanguage("kedb.sheetname");
    String title = I18n.getLanguage("kedb.export.title");
    String fileNameOut = KEDB_EXPORT;
    String subTitle;
    if (fromDate != null && toDate != null) {
      Date fromDateTmp = DataUtil.convertStringToDate(fromDate);
      Date toDateTmp = DataUtil.convertStringToDate(toDate);
      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      subTitle = I18n.getLanguage("kedb.export.eportDate", dateFormat.format(fromDateTmp),
          dateFormat.format(toDateTmp));
    } else {
      subTitle = "";
    }
    ConfigFileExport configFileExport = new ConfigFileExport(
        listKedbExportDTO,
        sheetName,
        title,
        subTitle,
        7,
        3,
        6,
        true,
        "language.kedb",
        lstHeaderSheet1,
        fieldSplit,
        "",
        I18n.getLanguage("kedb.export.firstLeftHeader"),
        I18n.getLanguage("kedb.export.secondLeftHeader"),
        I18n.getLanguage("kedb.export.firstRightHeader"),
        I18n.getLanguage("kedb.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(7, 0, 0, 0,
        I18n.getLanguage("kedb.STT"), "HEAD", "STRING");
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

  private File handleFileImport(List<KedbExportDTO> listKedbExportDTO, String[] columnExport)
      throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = "KEDB";
    String title = I18n.getLanguage("kedb.import.title");
    String fileNameOut = IMPORT_KEDB_RESULT;
    String subTitle = "";
    ConfigFileExport configFileExport = new ConfigFileExport(
        listKedbExportDTO,
        sheetName,
        title,
        subTitle,
        7,
        3,
        6,
        true,
        "language.kedb",
        lstHeaderSheet1,
        fieldSplit,
        "",
        I18n.getLanguage("kedb.export.firstLeftHeader"),
        I18n.getLanguage("kedb.export.secondLeftHeader"),
        I18n.getLanguage("kedb.export.firstRightHeader"),
        I18n.getLanguage("kedb.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(7, 0, 0, 0,
        I18n.getLanguage("kedb.STT"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(fileTemplate, fileNameOut,
        fileExports, rootPath, null);
    return fileExport;
  }

  @Override
  public List<com.viettel.gnoc.ws.dto.KedbDTO> synchKedbByCreateTime2(String fromDate,
      String toDate) {
    log.debug("Request to synchKedbByCreateTime: {}", fromDate, toDate);
    return kedbRepository.synchKedbByCreateTime2(fromDate, toDate);
  }
}
