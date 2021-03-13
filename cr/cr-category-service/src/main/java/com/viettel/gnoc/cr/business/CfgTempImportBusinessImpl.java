package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.MethodParameterDTO;
import com.viettel.gnoc.cr.dto.TempImportColDTO;
import com.viettel.gnoc.cr.dto.TempImportDTO;
import com.viettel.gnoc.cr.repository.CfgTempImportColRepository;
import com.viettel.gnoc.cr.repository.CfgTempImportRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@Slf4j
@Transactional
@Service
public class CfgTempImportBusinessImpl implements CfgTempImportBusiness {

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
  TicketProvider ticketProvider;

  private final static String CR_TEMP_IMPORT_EXPORT = "Cr_Temp_Import_EXPORT";

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  CfgTempImportRepository cfgTempImportRepository;

  @Autowired
  CfgTempImportColRepository cfgTempImportColRepository;

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Override
  public Datatable getListTempImport(TempImportDTO dto) {
    log.debug("Request to getListTempImport : {}", dto);
    return cfgTempImportRepository.getListTempImport(dto);
  }

  @Override
  public TempImportDTO findTempImportById(Long tempImportId) {
    log.debug("Request to findTempImportById : {}", tempImportId);
    List<TempImportColDTO> tempImportColDTOS = null;
    if (tempImportId != null && tempImportId > 0) {
      tempImportColDTOS = cfgTempImportColRepository.findTempImportColByTempImportId(tempImportId);
    }
    TempImportDTO tempImportDTO = cfgTempImportRepository.findTempImportById(tempImportId);
    tempImportDTO.setTempImportColDTOS(tempImportColDTOS);
    List<LanguageExchangeDTO> languageExchangeDTOS = languageExchangeRepository
        .getListLanguageExchangeById("OPEN_PM", "OPEN_PM.TEMP_IMPORT", tempImportId, null);
    GnocFileDto gnocFileDto = new GnocFileDto();
    gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.CR_TEMP_IMPORT);
    gnocFileDto.setBusinessId(tempImportId);
    List<GnocFileDto> lstGnocFile = gnocFileRepository.getListGnocFileByDto(gnocFileDto);
    for (LanguageExchangeDTO languageExchangeDTO : languageExchangeDTOS) {
      for (GnocFileDto gnocFileDto1 : lstGnocFile) {
        if (gnocFileDto1.getMappingId().equals(languageExchangeDTO.getLeeId())) {
          languageExchangeDTO.setLeeValue(gnocFileDto1.getPath());
          languageExchangeDTO.setFileName(gnocFileDto1.getFileName());
          break;
        }
      }
    }
    tempImportDTO.setListName(languageExchangeDTOS);
    return tempImportDTO;
  }

  @Override
  public ResultInSideDto insertTempImport(List<MultipartFile> files, TempImportDTO tempImportDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    log.debug("Request to insertTempImport : {}", tempImportDTO);
    try {
      for (LanguageExchangeDTO languageExchangeDTO : tempImportDTO.getListName()) {
        if (languageExchangeDTO.getIndexFile() != null) {
          languageExchangeDTO.setFile(files.get(languageExchangeDTO.getIndexFile().intValue()));
        }
      }
      List<LanguageExchangeDTO> languageExchangeDTOS = saveMultipleUploadedFile(
          tempImportDTO.getListName(), uploadFolder);
      String locale = I18n.getLocale();
      for (LanguageExchangeDTO languageExchangeDTO : languageExchangeDTOS) {
        if (locale.equals(languageExchangeDTO.getLeeLocale())) {
          tempImportDTO.setName(FileUtils.getFileName(languageExchangeDTO.getFilePath()));
          tempImportDTO.setPath(languageExchangeDTO.getFilePath());
          break;
        }
      }
      tempImportDTO.setCreaterId(userToken.getUserID());
      tempImportDTO.setCreaterTime(DateTimeUtils.convertDateOffset());
      ResultInSideDto resultInSideDto = cfgTempImportRepository.insertTempImport(tempImportDTO);
      if (!tempImportDTO.getTempImportColDTOS().isEmpty()) {
        List<TempImportColDTO> tempImportColDTOS = tempImportDTO.getTempImportColDTOS();
        for (TempImportColDTO dto : tempImportColDTOS) {
          dto.setTempImportId(resultInSideDto.getId());
          dto.setTempImportColId(null);
          cfgTempImportColRepository.insertTempImportCol(dto);
        }
      }
      ResultInSideDto resultLanguageExchange = languageExchangeRepository
          .saveListLanguageExchange("OPEN_PM", "OPEN_PM.TEMP_IMPORT", resultInSideDto.getId(),
              languageExchangeDTOS);
      List<LanguageExchangeDTO> lstLanguageExchange = (List<LanguageExchangeDTO>) resultLanguageExchange
          .getObject();
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      for (LanguageExchangeDTO languageExchangeDTO : lstLanguageExchange) {
        for (LanguageExchangeDTO languageExchangeDTO1 : languageExchangeDTOS) {
          if (languageExchangeDTO.getLeeLocale().equals(languageExchangeDTO1.getLeeLocale())) {
            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setPath(languageExchangeDTO1.getFilePathNew());
            gnocFileDto.setFileName(FileUtils.getFileName(languageExchangeDTO1.getFilePathNew()));
            gnocFileDto.setCreateUnitId(userToken.getDeptId());
            gnocFileDto.setCreateUnitName(unitToken.getUnitName());
            gnocFileDto.setCreateUserId(userToken.getUserID());
            gnocFileDto.setCreateUserName(userToken.getUserName());
            gnocFileDto.setCreateTime(tempImportDTO.getCreaterTime());
            gnocFileDto.setMappingId(languageExchangeDTO.getLeeId());
            gnocFileDtos.add(gnocFileDto);
            break;
          }
        }
      }
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.CR_TEMP_IMPORT, resultInSideDto.getId(),
              gnocFileDtos);
      if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
        commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "Insert CfgTempImport",
            "Insert CfgTempImport ID: " + resultInSideDto.getId(),
            null, null));
      }
      return resultInSideDto;
    } catch (Exception e) {
      log.info(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public ResultInSideDto updateTempImport(List<MultipartFile> files, TempImportDTO tempImportDTO) {
    log.debug("Request to updateTempImport : {}", tempImportDTO);
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    TempImportDTO tempImportDTOOld = cfgTempImportRepository.findTempImportDtoById(tempImportDTO.getTempImportId());
    tempImportDTO.setPath(tempImportDTOOld.getPath());
    try {
      for (LanguageExchangeDTO languageExchangeDTO : tempImportDTO.getListName()) {
        if (languageExchangeDTO.getIndexFile() != null) {
          languageExchangeDTO.setFile(files.get(languageExchangeDTO.getIndexFile().intValue()));
        }
      }
      List<LanguageExchangeDTO> languageExchangeDTOS = saveMultipleUploadedFile(
          tempImportDTO.getListName(), uploadFolder);
      String locale = I18n.getLocale();
      for (LanguageExchangeDTO languageExchangeDTO : languageExchangeDTOS) {
        if (locale.equals(languageExchangeDTO.getLeeLocale())) {
          if (StringUtils.isNotNullOrEmpty(languageExchangeDTO.getFilePath())) {
            tempImportDTO.setName(FileUtils.getFileName(languageExchangeDTO.getFilePath()));
            tempImportDTO.setPath(languageExchangeDTO.getFilePath());
          } else {
            String pathNew = languageExchangeDTO.getFilePathNew();
            tempImportDTO.setName(FileUtils.getFileName(pathNew));
            tempImportDTO.setPath(uploadFolder + File.separator + FileUtils.getDateByPath(pathNew) + File.separator + FileUtils.getFileName(pathNew));
          }
          break;
        }
      }
      List<TempImportColDTO> tempImportColDTOS = tempImportDTO.getTempImportColDTOS();
      if (tempImportColDTOS != null && !tempImportColDTOS.isEmpty()) {
        for (TempImportColDTO dto : tempImportColDTOS) {
          dto.setTempImportId(tempImportDTO.getTempImportId());
          if (dto.getTempImportColId() != null) {
            cfgTempImportColRepository.updateTempImportCol(dto);
          } else {
            cfgTempImportColRepository.insertTempImportCol(dto);
          }
        }
      }
      if (tempImportDTO.getIdDeleteList() != null && !tempImportDTO.getIdDeleteList().isEmpty()) {
        List<TempImportColDTO> lstDelete = new ArrayList<>();
        for (Long id : tempImportDTO.getIdDeleteList()) {
          TempImportColDTO tempImportColDTO = new TempImportColDTO();
          tempImportColDTO.setTempImportId(id);
          lstDelete.add(tempImportColDTO);
        }
        cfgTempImportColRepository.deleteListTempImportColByTempImportId(lstDelete);
      }
      ResultInSideDto resultInSideDto = cfgTempImportRepository.updateTempImport(tempImportDTO);
      ResultInSideDto resultLanguageExchange = languageExchangeRepository
          .saveListLanguageExchange("OPEN_PM", "OPEN_PM.TEMP_IMPORT", resultInSideDto.getId(),
              languageExchangeDTOS);
      List<LanguageExchangeDTO> lstLanguageExchange = (List<LanguageExchangeDTO>) resultLanguageExchange
          .getObject();
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      for (LanguageExchangeDTO languageExchangeDTO : lstLanguageExchange) {
        for (LanguageExchangeDTO languageExchangeDTO1 : languageExchangeDTOS) {
          if (languageExchangeDTO.getLeeLocale().equals(languageExchangeDTO1.getLeeLocale())) {
            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setPath(languageExchangeDTO1.getFilePathNew());
            gnocFileDto.setFileName(FileUtils.getFileName(languageExchangeDTO1.getFilePathNew()));
            gnocFileDto.setCreateUnitId(userToken.getDeptId());
            gnocFileDto.setCreateUnitName(unitToken.getUnitName());
            gnocFileDto.setCreateUserId(userToken.getUserID());
            gnocFileDto.setCreateUserName(userToken.getUserName());
            gnocFileDto.setCreateTime(tempImportDTO.getCreaterTime());
            gnocFileDto.setMappingId(languageExchangeDTO.getLeeId());
            gnocFileDtos.add(gnocFileDto);
            break;
          }
        }
      }
      gnocFileRepository
          .saveListGnocFile(GNOC_FILE_BUSSINESS.CR_TEMP_IMPORT, resultInSideDto.getId(),
              gnocFileDtos);
      if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
        commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "Update CfgTempImport",
            "Update CfgTempImport ID: " + resultInSideDto.getId(),
            null, null));
      }
      return resultInSideDto;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public String deleteTempImportById(Long tempImportId) {
    log.debug("Request to deleteTempImportById : {}", tempImportId);
    if (tempImportId != null && tempImportId > 0) {
      List<TempImportColDTO> tempImportColDTOS = cfgTempImportColRepository
          .findTempImportColByTempImportId(tempImportId);
      for (TempImportColDTO dto : tempImportColDTOS) {
        cfgTempImportColRepository.deleteTempImportColById(dto.getTempImportColId());
      }
    }
    languageExchangeRepository
        .deleteListLanguageExchange("OPEN_PM", "OPEN_PM.TEMP_IMPORT", tempImportId);
    gnocFileRepository.deleteGnocFile(GNOC_FILE_BUSSINESS.CR_TEMP_IMPORT, tempImportId);
    return cfgTempImportRepository.deleteTempImportById(tempImportId);
  }

  public List<LanguageExchangeDTO> saveMultipleUploadedFile(
      List<LanguageExchangeDTO> languageExchangeDTOS, String uploadFolder) throws IOException {
    for (LanguageExchangeDTO languageExchangeDTO : languageExchangeDTOS) {
      MultipartFile multipartFile = languageExchangeDTO.getFile();
      if (multipartFile != null) {
        Date date = new Date();
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                multipartFile.getBytes(), date);
        //Start save file old
        String fullPathOld = FileUtils
            .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                uploadFolder, date);
        //End save file old
//        languageExchangeDTO.setBussinessCode(FileUtils.getFileName(fullPath));
        languageExchangeDTO.setLeeValue(FileUtils.getFileName(fullPath));
        languageExchangeDTO.setFilePath(fullPathOld);
        languageExchangeDTO.setFilePathNew(fullPath);
      } else {
        languageExchangeDTO.setFilePathNew(languageExchangeDTO.getLeeValue());
        languageExchangeDTO.setLeeValue(FileUtils.getFileName(languageExchangeDTO.getFilePathNew()));
      }
    }
    return languageExchangeDTOS;
  }

  @Override
  public List<MethodParameterDTO> getMethodPrameter() {
    log.debug("getMethodPrameter : {}");
    return cfgTempImportRepository.getMethodPrameter();
  }

  @Override
  public File exportData(TempImportDTO tempImportDTO) throws Exception {
    List<TempImportDTO> lstTempImportEx =
        (List<TempImportDTO>) cfgTempImportRepository
            .getListDataExport(tempImportDTO).getData();
    List<TempImportDTO> lstEx = new ArrayList<>();
    if (lstTempImportEx != null && !lstTempImportEx.isEmpty()) {
      for (TempImportDTO tempImportExport : lstTempImportEx) {
        if ("1".equals(String.valueOf(tempImportExport.getIsActive()))) {
          tempImportExport.setIsActiveStr(I18n.getLanguage("TempImport.isActive.1"));
        } else if ("0".equals(String.valueOf(tempImportExport.getIsActive()))) {
          tempImportExport.setIsActiveStr(I18n.getLanguage("TempImport.isActive.0"));
        }
        lstEx.add(tempImportExport);
      }
    }
    return exportFileEx(lstEx, "");
  }

  private File exportFileEx(List<TempImportDTO> lstTempImportEx, String key) throws Exception {
    String fileNameOut = "";
    String subTitle = "";
    String sheetName = I18n.getLanguage("TempImport.export.title");
    String title = I18n.getLanguage("TempImport.export.title");
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("code", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("name", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("path", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("title", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("isActiveStr", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    if (Constants.RESULT_IMPORT.equals(key)) {

    } else {
      fileNameOut = CR_TEMP_IMPORT_EXPORT;
      subTitle = I18n
          .getLanguage("TempImport.export.exportDate", DateTimeUtils.convertDateOffset());
    }

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstTempImportEx,
        sheetName,
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.TempImport",
        lstHeaderSheet,
        fieldSplit,
        "",
        I18n.getLanguage("common.export.firstLeftHeader")
        , I18n.getLanguage("common.export.secondLeftHeader")
        , I18n.getLanguage("common.export.firstRightHeader")
        , I18n.getLanguage("common.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;

    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("common.STT"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configFileExport.setLstCreatCell(lstCellSheet);
    fileExports.add(configFileExport);
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
}
