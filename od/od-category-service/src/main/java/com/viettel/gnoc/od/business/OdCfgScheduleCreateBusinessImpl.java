package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ReceiveUnitDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.ReceiveUnitRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.od.dto.OdCfgScheduleCreateDTO;
import com.viettel.gnoc.od.dto.OdCfgScheduleCreateExportDTO;
import com.viettel.gnoc.od.dto.OdFileDTO;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import com.viettel.gnoc.od.repository.OdCfgScheduleCreateRepository;
import com.viettel.security.PassTranformer;
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
import org.apache.poi.ss.util.CellReference;
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
public class OdCfgScheduleCreateBusinessImpl implements OdCfgScheduleCreateBusiness {

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
  OdCfgScheduleCreateRepository odCfgScheduleCreateRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  ReceiveUnitRepository receiveUnitRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UnitRepository unitRepository;

  Map<String, String> mapOdGroupTypeName = new HashMap<>();
  Map<Long, String> mapOdTypeName = new HashMap<>();
  Map<Long, String> mapPriorityName = new HashMap<>();
  Map<Long, String> mapScheduleName = new HashMap<>();
  Map<Long, String> mapReceiveUnitName = new HashMap<>();

  private final static String OD_CFG_SCHEDULE_CREATE_RESULT_IMPORT = "OD_CFG_SCHEDULE_CREATE_RESULT_IMPORT";
  private final static String OD_CFG_SCHEDULE_CREATE_EXPORT = "OD_CFG_SCHEDULE_CREATE_EXPORT";
  private final static String OD_CFG_SCHEDULE_CREATE_SEQ = "OD_CFG_SCHEDULE_CREATE_SEQ";

  @Override
  public Datatable getListOdCfgScheduleCreateDTOSearchWeb(
      OdCfgScheduleCreateDTO odCfgScheduleCreateDTO) {
    log.debug("Request to getListOdCfgScheduleCreateDTOSearchWeb: {}", odCfgScheduleCreateDTO);
    return odCfgScheduleCreateRepository
        .getListOdCfgScheduleCreateDTOSearchWeb(odCfgScheduleCreateDTO);
  }

  @Override
  public OdCfgScheduleCreateDTO findOdCfgScheduleCreateById(Long id) {
    log.debug("Request to findOdCfgScheduleCreateById: {}", id);
    return odCfgScheduleCreateRepository.findOdCfgScheduleCreateById(id);
  }

  @Override
  public ResultInSideDto insertOdCfgScheduleCreate(List<MultipartFile> files,
      OdCfgScheduleCreateDTO odCfgScheduleCreateDTO)
      throws IOException {
    log.debug("Request to insertOdCfgScheduleCreate: {}", odCfgScheduleCreateDTO);
    Map<String, Object> objectMap = saveFile(files);
    odCfgScheduleCreateDTO.setOdFileId(objectMap.get("odFileId").toString());
    ResultInSideDto resultInSideDto = odCfgScheduleCreateRepository
        .insertOdCfgScheduleCreate(odCfgScheduleCreateDTO);
    gnocFileRepository.saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.OD_CFG_SCHEDULE_CREATE,
        resultInSideDto.getId(), (List<GnocFileDto>) objectMap.get("gnocFileDtos"));
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateOdCfgScheduleCreate(List<MultipartFile> files,
      OdCfgScheduleCreateDTO odCfgScheduleCreateDTO)
      throws IOException {
    log.debug("Request to updateOdCfgScheduleCreate: {}", odCfgScheduleCreateDTO);
    Map<String, Object> objectMap = saveFile(files);
    List<GnocFileDto> gnocFileDtos = (List<GnocFileDto>) objectMap.get("gnocFileDtos");
    String odFileId = objectMap.get("odFileId").toString();
    List<String> lstTemp =
        StringUtils.isStringNullOrEmpty(odCfgScheduleCreateDTO.getOdFileId()) ? new ArrayList<>()
            : Arrays.asList(odCfgScheduleCreateDTO.getOdFileId().replaceAll(" ", "").split(","));
    List<String> lstOdFileIdOld = new ArrayList();
    if (lstTemp != null) {
      lstOdFileIdOld.addAll(lstTemp);
    }
    for (Long id : odCfgScheduleCreateDTO.getIdDeleteList()) {
      lstOdFileIdOld.remove(String.valueOf(id));
    }

    odCfgScheduleCreateDTO
        .setOdFileId(String.join(",", lstOdFileIdOld).concat(",").concat(odFileId));
    ResultInSideDto resultInSideDto = odCfgScheduleCreateRepository
        .updateOdCfgScheduleCreate(odCfgScheduleCreateDTO);
    odCfgScheduleCreateRepository.deleteListOdFile(odCfgScheduleCreateDTO.getIdDeleteList());
    gnocFileRepository
        .deleteListGnocFile(GNOC_FILE_BUSSINESS.OD_CFG_SCHEDULE_CREATE, resultInSideDto.getId(),
            odCfgScheduleCreateDTO.getIdDeleteList());
    gnocFileRepository
        .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.OD_CFG_SCHEDULE_CREATE,
            resultInSideDto.getId(),
            gnocFileDtos);
    return resultInSideDto;
  }

  public Map<String, Object> saveFile(List<MultipartFile> files) throws IOException {
    Date date = new Date();
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    Map<String, Object> objectMap = new HashMap<>();
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    String odFileId = "";
    for (MultipartFile multipartFile : files) {
      String fullPath = FileUtils.saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
          PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
          multipartFile.getBytes(), date);
      String fileName = multipartFile.getOriginalFilename();
      //Start save file old
      String fullPathOld = FileUtils
          .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
              uploadFolder, date);
      OdFileDTO odFileDTO = new OdFileDTO();
      odFileDTO.setPath(FileUtils.getFilePath(fullPathOld));
      odFileDTO.setFileName(FileUtils.getFileName(fullPathOld));
      ResultInSideDto resultFileDataOld = odCfgScheduleCreateRepository.insertOdFile(odFileDTO);
      if ("".equals(odFileId)) {
        odFileId = odFileId.concat(resultFileDataOld.getId().toString());
      } else {
        odFileId = odFileId.concat(",").concat(resultFileDataOld.getId().toString());
      }
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
    objectMap.put("odFileId", odFileId);
    objectMap.put("gnocFileDtos", gnocFileDtos);
    return objectMap;
  }

  @Override
  public ResultInSideDto deleteOdCfgScheduleCreate(Long id) {
    log.debug("Request to deleteOdCfgScheduleCreate: {}", id);
    return odCfgScheduleCreateRepository.deleteOdCfgScheduleCreate(id);
  }

  @Override
  public ResultInSideDto deleteListOdCfgScheduleCreate(List<Long> listId) {
    log.debug("Request to deleteListOdCfgScheduleCreate: {}", listId);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (Long id : listId) {
      resultInSideDto = deleteOdCfgScheduleCreate(id);
    }
    return resultInSideDto;
  }

  @Override
  public String getSequenseOdCfgScheduleCreate() {
    log.debug("Request to getSequenseOdCfgScheduleCreate: {}");
    return odCfgScheduleCreateRepository.getSequenseOdCfgScheduleCreate(OD_CFG_SCHEDULE_CREATE_SEQ);
  }

  @Override
  public File exportData(OdCfgScheduleCreateDTO odCfgScheduleCreateDTO) throws Exception {
    List<OdCfgScheduleCreateExportDTO> listExport = new ArrayList<>();
    List<OdCfgScheduleCreateDTO> scheduleCreateDTOList =
        (List<OdCfgScheduleCreateDTO>) odCfgScheduleCreateRepository
            .getListDataExport(odCfgScheduleCreateDTO).getData();
    if (scheduleCreateDTOList != null && !scheduleCreateDTOList.isEmpty()) {
      for (OdCfgScheduleCreateDTO item : scheduleCreateDTOList) {
        OdCfgScheduleCreateExportDTO exportDTO = new OdCfgScheduleCreateExportDTO();
        exportDTO.setOdName(item.getOdName());
        exportDTO.setOdDescription(item.getOdDescription());
        exportDTO.setOdGroupTypeName(item.getOdGroupTypeName());
        exportDTO.setOdTypeName(item.getOdTypeName());
        exportDTO.setOdPriorityName(item.getOdPriorityName());
        exportDTO.setScheduleName(item.getScheduleName());
        exportDTO.setReceiveUnitName(item.getReceiveUnitName());
        listExport.add(exportDTO);
      }
    }
    return handleFile(listExport, "");
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile, List<MultipartFile> files)
      throws IOException {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    Map<String, Object> objectMap = saveFile(files);
    List<GnocFileDto> gnocFileDtos = (List<GnocFileDto>) objectMap.get("gnocFileDtos");
    String odFileId = objectMap.get("odFileId").toString();

    List<OdCfgScheduleCreateDTO> listDto;
    List<OdCfgScheduleCreateExportDTO> listExportDto;

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
          return resultInSideDto;
        }

        listDto = new ArrayList<>();
        listExportDto = new ArrayList<>();

        if (!lstData.isEmpty()) {
          int row = 5;
          int index = 0;
          setMapOdGroupTypeName();
          setMapOdTypeName();
          setMapPriorityName();
          setMapScheduleName();
          setMapReceiveUnitName();
          for (Object[] obj : lstData) {
            OdCfgScheduleCreateExportDTO exportDTO = new OdCfgScheduleCreateExportDTO();
            OdCfgScheduleCreateDTO createDTO = new OdCfgScheduleCreateDTO();
            if (obj[1] != null) {
              exportDTO.setOdName(obj[1].toString().trim());
              createDTO.setOdName(exportDTO.getOdName());
            } else {
              exportDTO.setOdName(null);
            }
            if (obj[2] != null) {
              exportDTO.setOdDescription(obj[2].toString().trim());
              createDTO.setOdDescription(exportDTO.getOdDescription());
            } else {
              exportDTO.setOdDescription(null);
            }
            if (obj[3] != null) {
              exportDTO.setOdGroupTypeName(obj[3].toString().replaceAll("_", " ").trim());
              for (Map.Entry<String, String> item : mapOdGroupTypeName.entrySet()) {
                if (exportDTO.getOdGroupTypeName().equals(item.getValue())) {
                  createDTO.setOdGroupTypeName(item.getValue());
                  break;
                }
              }
            } else {
              exportDTO.setOdGroupTypeName(null);
            }
            if (obj[4] != null) {
              exportDTO.setOdTypeName(obj[4].toString().trim());
              for (Map.Entry<Long, String> item : mapOdTypeName.entrySet()) {
                if (exportDTO.getOdTypeName().equals(item.getValue())) {
                  createDTO.setOdTypeId(item.getKey());
                  break;
                }
              }
            } else {
              exportDTO.setOdTypeName(null);
            }
            if (obj[5] != null) {
              exportDTO.setOdPriorityName(obj[5].toString().trim());
              for (Map.Entry<Long, String> item : mapPriorityName.entrySet()) {
                if (exportDTO.getOdPriorityName().equals(item.getValue())) {
                  createDTO.setOdPriority(item.getKey());
                  break;
                }
              }
            } else {
              exportDTO.setOdPriorityName(null);
            }
            if (obj[6] != null) {
              exportDTO.setScheduleName(obj[6].toString().trim());
              for (Map.Entry<Long, String> item : mapScheduleName.entrySet()) {
                if (exportDTO.getScheduleName().equals(item.getValue())) {
                  createDTO.setSchedule(item.getKey());
                  break;
                }
              }
            } else {
              exportDTO.setScheduleName(null);
            }
            if (obj[7] != null) {
              exportDTO.setReceiveUnitName(obj[7].toString().trim());
              String[] arrReceiveUnitName = exportDTO.getReceiveUnitName().split(", \r\n");
              String receiveUnit = "";
              for (int i = 0; i < arrReceiveUnitName.length; i++) {
                if (i != 0) {
                  receiveUnit = receiveUnit.concat(",");
                }
                for (Map.Entry<Long, String> item : mapReceiveUnitName.entrySet()) {
                  if (arrReceiveUnitName[i].equals(item.getValue())) {
                    receiveUnit = receiveUnit.concat(String.valueOf(item.getKey()));
                    break;
                  }
                }
              }
              if (StringUtils.isNotNullOrEmpty(receiveUnit)) {
                createDTO.setReceiveUnit(receiveUnit);
              }
            } else {
              exportDTO.setReceiveUnitName(null);
            }
            OdCfgScheduleCreateExportDTO createExportDTO = validateImportInfo(exportDTO, createDTO,
                listExportDto);
            if (createExportDTO.getId() != null) {
              createDTO.setId(createExportDTO.getId());
            }
            if (createExportDTO.getResultImport() == null) {
              createExportDTO.setResultImport(
                  I18n.getLanguage("odCfgScheduleCreate.result.import.odCfgScheduleCreateCode"));
              listExportDto.add(createExportDTO);
              listDto.add(createDTO);
            } else {
              listExportDto.add(createExportDTO);
              index++;
            }
            row++;
          }
          if (index == 0) {
            if (!listDto.isEmpty()) {
              for (OdCfgScheduleCreateDTO dto : listDto) {
                dto.setOdFileId(odFileId);
                ResultInSideDto result = odCfgScheduleCreateRepository
                    .insertOdCfgScheduleCreate(dto);
                gnocFileRepository
                    .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.OD_CFG_SCHEDULE_CREATE,
                        result.getId(),
                        gnocFileDtos);
              }
              resultInSideDto.setKey(RESULT.SUCCESS);
            }
          } else {
            File fileExport = handleFile(listExportDto, Constants.RESULT_IMPORT);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = handleFile(listExportDto, Constants.RESULT_IMPORT);
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

  public void setMapOdGroupTypeName() {
    List<CatItemDTO> lstOdGroupType = catItemRepository
        .getDataItem(CATEGORY.OD_GROUP_TYPE);
    if (lstOdGroupType != null && !lstOdGroupType.isEmpty()) {
      for (CatItemDTO catItemDTO : lstOdGroupType) {
        mapOdGroupTypeName.put(String.valueOf(catItemDTO.getItemId()), catItemDTO.getItemName());
      }
    }
  }

  public void setMapOdTypeName() {
    List<OdTypeDTO> lstOdType = odCfgScheduleCreateRepository.getListOdType();
    if (lstOdType != null && !lstOdType.isEmpty()) {
      for (OdTypeDTO odTypeDTO : lstOdType) {
        mapOdTypeName.put(odTypeDTO.getOdTypeId(), odTypeDTO.getOdTypeName());
      }
    }
  }

  public void setMapPriorityName() {
    List<CatItemDTO> lstPriority = catItemRepository.getDataItem(CATEGORY.OD_PRIORITY);
    if (lstPriority != null && !lstPriority.isEmpty()) {
      for (CatItemDTO catItemPriority : lstPriority) {
        mapPriorityName.put(catItemPriority.getItemId(), catItemPriority.getItemName());
      }
    }
  }

  public void setMapScheduleName() {
    List<CatItemDTO> lstSchedule = catItemRepository.getDataItem(CATEGORY.OD_SCHEDULE);
    if (lstSchedule != null && !lstSchedule.isEmpty()) {
      for (CatItemDTO catItemSchedule : lstSchedule) {
        mapScheduleName
            .put(Long.valueOf(catItemSchedule.getItemValue()), catItemSchedule.getItemName());
      }
    }
  }

  public void setMapReceiveUnitName() {
    List<ReceiveUnitDTO> lstReceiveUnit = receiveUnitRepository.getListReceiveUnit();
    if (lstReceiveUnit != null && !lstReceiveUnit.isEmpty()) {
      for (ReceiveUnitDTO dto : lstReceiveUnit) {
        mapReceiveUnitName.put(dto.getUnitId(), dto.getUnitName() + "(" + dto.getUnitCode() + ")");
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
    if (count != 8) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("common.STT")).equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgScheduleCreate.odName") + "(*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgScheduleCreate.odDescription"))
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgScheduleCreate.odGroupTypeName"))
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgScheduleCreate.odTypeName") + "(*)")
        .equalsIgnoreCase(obj[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgScheduleCreate.odPriorityName") + "(*)")
        .equalsIgnoreCase(obj[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgScheduleCreate.scheduleName") + "(*)")
        .equalsIgnoreCase(obj[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgScheduleCreate.receiveUnitName") + "(*)")
        .equalsIgnoreCase(obj[7].toString().trim())) {
      return false;
    }
    return true;
  }

  private OdCfgScheduleCreateExportDTO validateImportInfo(OdCfgScheduleCreateExportDTO exportDTO,
      OdCfgScheduleCreateDTO createDTO, List<OdCfgScheduleCreateExportDTO> listExportDto) {
    String resultImport = "";
    if (StringUtils.isStringNullOrEmpty(exportDTO.getOdName())) {
      resultImport = resultImport.concat(I18n.getLanguage("odCfgScheduleCreate.err.odName"));
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getOdTypeName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("odCfgScheduleCreate.err.odTypeName"));
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getOdPriorityName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("odCfgScheduleCreate.err.odPriorityName"));
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getScheduleName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("odCfgScheduleCreate.err.scheduleName"));
    }
    if (StringUtils.isStringNullOrEmpty(exportDTO.getReceiveUnitName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("odCfgScheduleCreate.err.receiveUnitName"));
    }
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      exportDTO.setResultImport(resultImport);
      return exportDTO;
    }

    if ((createDTO.getOdTypeId() != 0) && (createDTO.getOdPriority() != 0) &&
        (StringUtils.isNotNullOrEmpty(createDTO.getReceiveUnit()))) {
      OdCfgScheduleCreateDTO dtoTmp = odCfgScheduleCreateRepository
          .checkOdCfgScheduleCreateExit(createDTO);
      if (dtoTmp != null) {
        exportDTO.setResultImport(I18n.getLanguage("odCfgScheduleCreate.err.dup-code"));
        return exportDTO;
      }
    }
    String validateDuplicate = validateDuplicate(exportDTO, listExportDto);
    if (StringUtils.isNotNullOrEmpty(validateDuplicate)) {
      exportDTO.setResultImport(validateDuplicate);
      return exportDTO;
    }
    return exportDTO;
  }

  private String checkResultImport(String resultImport) {
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      resultImport = resultImport.concat("; \n");
    }
    return resultImport;
  }

  private String validateDuplicate(OdCfgScheduleCreateExportDTO exportDTO,
      List<OdCfgScheduleCreateExportDTO> listExportDto) {
    String odTypeName = exportDTO.getOdTypeName();
    String odPriorityName = exportDTO.getOdPriorityName();
    String receiveUnitName = exportDTO.getReceiveUnitName();
    for (int i = 0; i < listExportDto.size(); i++) {
      OdCfgScheduleCreateExportDTO dtoCheck = listExportDto.get(i);
      if (odTypeName.equals(dtoCheck.getOdTypeName()) &&
          odPriorityName.equals(dtoCheck.getOdPriorityName()) &&
          receiveUnitName.equals(dtoCheck.getReceiveUnitName())) {
        return I18n.getLanguage("odCfgScheduleCreate.err.dup-code-in-file")
            .replaceAll("0", String.valueOf((i) + 1));
      }
    }
    return null;
  }

  private File handleFile(List<OdCfgScheduleCreateExportDTO> listExportDto, String key)
      throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    String sheetName = I18n.getLanguage("odCfgScheduleCreate.title");
    String title = I18n.getLanguage("odCfgScheduleCreate.title");
    String subTitle;
    String fileNameOut;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    ConfigHeaderExport configHeaderExport;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    configHeaderExport = new ConfigHeaderExport("odName", "LEFT", false, 0,
        0, new String[]{}, null, "STRING");
    headerExportList.add(configHeaderExport);
    configHeaderExport = new ConfigHeaderExport("odDescription", "LEFT", false, 0,
        0, new String[]{}, null, "STRING");
    headerExportList.add(configHeaderExport);
    configHeaderExport = new ConfigHeaderExport("odGroupTypeName", "LEFT", false, 0,
        0, new String[]{}, null, "STRING");
    headerExportList.add(configHeaderExport);
    configHeaderExport = new ConfigHeaderExport("odTypeName", "LEFT", false, 0,
        0, new String[]{}, null, "STRING");
    headerExportList.add(configHeaderExport);
    configHeaderExport = new ConfigHeaderExport("odPriorityName", "LEFT", false, 0,
        0, new String[]{}, null, "STRING");
    headerExportList.add(configHeaderExport);
    configHeaderExport = new ConfigHeaderExport("scheduleName", "LEFT", false, 0,
        0, new String[]{}, null, "STRING");
    headerExportList.add(configHeaderExport);
    configHeaderExport = new ConfigHeaderExport("receiveUnitName", "LEFT", false, 0,
        0, new String[]{}, null, "STRING");
    headerExportList.add(configHeaderExport);
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = OD_CFG_SCHEDULE_CREATE_RESULT_IMPORT;
      subTitle = I18n
          .getLanguage("odCfgScheduleCreate.export.import", dateFormat.format(new Date()));
      configHeaderExport = new ConfigHeaderExport("resultImport", "LEFT", false, 0,
          0, new String[]{}, null, "STRING");
      headerExportList.add(configHeaderExport);
    } else {
      fileNameOut = OD_CFG_SCHEDULE_CREATE_EXPORT;
      subTitle = I18n
          .getLanguage("odCfgScheduleCreate.export.eportDate", dateFormat.format(new Date()));
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
        "language.odCfgScheduleCreate",
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
        I18n.getLanguage("common.STT"), "HEAD", "STRING");
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
  public File getTemplate() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "VBA_TEMPLATE.xlsm";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);
    Map<String, List<String>> categoryItems = new HashMap<>();
    List<CatItemDTO> lstOdGroupType = catItemRepository.getDataItem(CATEGORY.OD_GROUP_TYPE);
    List<String> listGroupTypeName = new ArrayList<>();
    List<Long> listGroupId = new ArrayList<>();
    for (int i = 0; i < lstOdGroupType.size(); i++) {
      listGroupTypeName.add(lstOdGroupType.get(i).getItemName().trim().replaceAll("\\s+", "_"));
      listGroupId.add(lstOdGroupType.get(i).getItemId());
      List<String> listOdTypeName = new ArrayList<>();
      List<Long> listOdTypeId = new ArrayList<>();
      List<OdTypeDTO> odTypeDTOList = odCfgScheduleCreateRepository
          .getListOdTypeByGroupId(listGroupId.get(i));
      if (odTypeDTOList != null && !odTypeDTOList.isEmpty()) {
        for (OdTypeDTO dto : odTypeDTOList) {
          listOdTypeName.add(dto.getOdTypeName());
          listOdTypeId.add(dto.getOdTypeId());
        }
        categoryItems.put(listGroupTypeName.get(i), listOdTypeName);
//      } else {
//        categoryItems.put(listGroupTypeName.get(i), null);
      }
    }

    XSSFSheet sheetOne = workBook.createSheet("ListSheet");

    Row ro;
    String colLetter;
    String reference;

    int c = 0;
    for (String key : categoryItems.keySet()) {
      int r = 0;
      ro = sheetOne.getRow(r);
      if (ro == null) {
        ro = sheetOne.createRow(r);
      }
      r++;
      ro.createCell(c).setCellValue(key);
      List<String> items = categoryItems.get(key);
      if (items.size() > 0) {
        for (String item : items) {
          ro = sheetOne.getRow(r);
          if (ro == null) {
            ro = sheetOne.createRow(r);
          }
          r++;
          ro.createCell(c).setCellValue(item);
        }
//      } else {
//        ro.createCell(c).setCellValue(key.replaceAll("_", " "));
      }
      colLetter = CellReference.convertNumToColString(c);
      Name groupType = workBook.createName();
      groupType.setNameName(key);
      reference = "ListSheet!$" + colLetter + "$2:$" + colLetter + "$" + r;
      groupType.setRefersToFormula(reference);
      c++;
    }

    colLetter = CellReference.convertNumToColString((c - 1));
    Name odType = workBook.createName();
    odType.setNameName("Categories");
    reference = "ListSheet!$A$1:$" + colLetter + "$1";
    odType.setRefersToFormula(reference);

    sheetOne.setSelected(false);

    XSSFSheet sheetParam = workBook.createSheet("param");

    XSSFSheet sheetTwo = workBook.getSheetAt(0);

    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("odCfgScheduleCreate.odName"),
        I18n.getLanguage("odCfgScheduleCreate.odDescription"),
        I18n.getLanguage("odCfgScheduleCreate.odGroupTypeName"),
        I18n.getLanguage("odCfgScheduleCreate.odTypeName"),
        I18n.getLanguage("odCfgScheduleCreate.odPriorityName"),
        I18n.getLanguage("odCfgScheduleCreate.scheduleName"),
        I18n.getLanguage("odCfgScheduleCreate.receiveUnitName"),
    };

    String[] headerStar = new String[]{
        I18n.getLanguage("odCfgScheduleCreate.odName"),
        I18n.getLanguage("odCfgScheduleCreate.odTypeName"),
        I18n.getLanguage("odCfgScheduleCreate.odPriorityName"),
        I18n.getLanguage("odCfgScheduleCreate.scheduleName"),
        I18n.getLanguage("odCfgScheduleCreate.receiveUnitName"),
    };

    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetTwo);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int odGroupTypeColumn = listHeader
        .indexOf(I18n.getLanguage("odCfgScheduleCreate.odGroupTypeName"));
    int odTypeColumn = listHeader.indexOf(I18n.getLanguage("odCfgScheduleCreate.odTypeName"));
    int priorityColumn = listHeader.indexOf(I18n.getLanguage("odCfgScheduleCreate.odPriorityName"));
    int scheduleColumn = listHeader.indexOf(I18n.getLanguage("odCfgScheduleCreate.scheduleName"));
    int receiveUnitColumn = listHeader
        .indexOf(I18n.getLanguage("odCfgScheduleCreate.receiveUnitName"));

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    //Tao tieu de
    sheetTwo.addMergedRegion(new CellRangeAddress(0, 0, 0, listHeader.size() - 1));
    Row titleRow = sheetTwo.createRow(0);
    titleRow.setHeightInPoints(45);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("odCfgScheduleCreate.title"));
    titleCell.setCellStyle(styles.get("title"));

    //Tao note
    sheetTwo.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row noteRow = sheetTwo.createRow(2);
    noteRow.setHeightInPoints(18);
    Cell noteCell = noteRow.createCell(0);
    noteCell.setCellValue(I18n.getLanguage("wocdgroup.importfile.template.excel.note"));
    noteCell.setCellStyle(styles.get("note"));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());
    //Tao header
    Row headerRow = sheetTwo.createRow(4);
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
      sheetTwo.setColumnWidth(i, 7000);
    }

    sheetTwo.setColumnWidth(0, 3000);

    XSSFDataValidationConstraint odGroupTypeConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "Categories");
    CellRangeAddressList cellRangeOdGroupType = new CellRangeAddressList(
        5, 65000, odGroupTypeColumn, odGroupTypeColumn);
    XSSFDataValidation dataValidationOdGroupType = (XSSFDataValidation) dvHelper.createValidation(
        odGroupTypeConstraint, cellRangeOdGroupType);
    dataValidationOdGroupType.setShowErrorBox(true);
    sheetTwo.addValidationData(dataValidationOdGroupType);

    int row = 5;
    List<OdTypeDTO> list = odCfgScheduleCreateRepository.getListOdType();
    for (OdTypeDTO dto : list) {
      ewu.createCell(sheetParam, 4, row++, dto.getOdTypeName(), styles.get("cell"));
    }

    sheetParam.autoSizeColumn(0);

    Name odTypeName = workBook.createName();
    odTypeName.setNameName("odType");
    odTypeName.setRefersToFormula("param!$E$2:$E$" + row);

    XSSFDataValidationConstraint odTypeConstraint;
    CellRangeAddressList cellRangeOdType;
    XSSFDataValidation dataValidationOdType;
    for (int i = 6; i <= 1000; i++) {
      odTypeConstraint = new XSSFDataValidationConstraint(
          DataValidationConstraint.ValidationType.LIST,
          "IF(ISTEXT($D$" + i + "),INDIRECT($D$" + i + "),odType)");
      cellRangeOdType = new CellRangeAddressList(
          i - 1, i - 1, odTypeColumn, odTypeColumn);
      dataValidationOdType = (XSSFDataValidation) dvHelper.createValidation(
          odTypeConstraint, cellRangeOdType);
      dataValidationOdType.setShowErrorBox(true);
      sheetTwo.addValidationData(dataValidationOdType);
    }

    workBook.setSheetHidden(2, true);
    workBook.setActiveSheet(0);

    row = 5;

    List<CatItemDTO> lstPriority = catItemRepository.getDataItem(CATEGORY.OD_PRIORITY);
    for (CatItemDTO dto : lstPriority) {
      ewu.createCell(sheetParam, 5, row++, dto.getItemName(), styles.get("cell"));
    }

    sheetParam.autoSizeColumn(0);

    Name priority = workBook.createName();
    priority.setNameName("priority");
    priority.setRefersToFormula("param!$F$2:$F$" + row);

    XSSFDataValidationConstraint priorityConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "priority");
    CellRangeAddressList cellRangePriority = new CellRangeAddressList(5, 65000, priorityColumn,
        priorityColumn);
    XSSFDataValidation dataValidationPriority = (XSSFDataValidation) dvHelper.createValidation(
        priorityConstraint, cellRangePriority);
    dataValidationPriority.setShowErrorBox(true);
    sheetTwo.addValidationData(dataValidationPriority);

    row = 5;

    List<CatItemDTO> lstSchedule = catItemRepository.getDataItem(CATEGORY.OD_SCHEDULE);
    for (CatItemDTO dto : lstSchedule) {
      ewu.createCell(sheetParam, 6, row++, dto.getItemName(), styles.get("cell"));
    }

    sheetParam.autoSizeColumn(0);

    Name schedule = workBook.createName();
    schedule.setNameName("schedule");
    schedule.setRefersToFormula("param!$G$2:$G$" + row);

    XSSFDataValidationConstraint scheduleConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "schedule");
    CellRangeAddressList cellRangeSchedule = new CellRangeAddressList(5, 65000, scheduleColumn,
        scheduleColumn);
    XSSFDataValidation dataValidationSchedule = (XSSFDataValidation) dvHelper.createValidation(
        scheduleConstraint, cellRangeSchedule);
    dataValidationSchedule.setShowErrorBox(true);
    sheetTwo.addValidationData(dataValidationSchedule);

    row = 5;

    List<ReceiveUnitDTO> lstReceiveUnit = receiveUnitRepository.getListReceiveUnit();
    for (ReceiveUnitDTO dto : lstReceiveUnit) {
      ewu.createCell(sheetParam, 7, row++, dto.getUnitName() + "(" + dto.getUnitCode() + ")",
          styles.get("cell"));
    }

    sheetParam.autoSizeColumn(0);

    Name receiveUnit = workBook.createName();
    receiveUnit.setNameName("receiveUnit");
    receiveUnit.setRefersToFormula("param!$H$2:$H$" + row);

    XSSFDataValidationConstraint receiveUnitConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "receiveUnit");
    CellRangeAddressList cellRangeReceiveUnit = new CellRangeAddressList(5, 65000,
        receiveUnitColumn, receiveUnitColumn);
    XSSFDataValidation dataValidationReceiveUnit = (XSSFDataValidation) dvHelper.createValidation(
        receiveUnitConstraint, cellRangeReceiveUnit);
    dataValidationReceiveUnit.setShowErrorBox(true);
    sheetTwo.addValidationData(dataValidationReceiveUnit);

    sheetParam.setSelected(false);
    workBook.setSheetName(0, I18n.getLanguage("odCfgScheduleCreate.title"));
    workBook.setSheetHidden(1, true);

    String fileResult = tempFolder + File.separator;
//    String fileName = "IMPORT_OD_CFG_SCHEDULE_CREATE" + "_" + System.currentTimeMillis() + ".xlsx";
    String fileName = "IMPORT_OD_CFG_SCHEDULE_CREATE" + "_" + System.currentTimeMillis() + ".xlsm";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

}
