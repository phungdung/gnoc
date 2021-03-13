package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.DataHistoryChange;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.model.GnocFileEntity;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.SR_CONFIG;
import com.viettel.gnoc.commons.utils.Constants.SR_STATUS;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRCatalogChildDTO;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRFilesDTO;
import com.viettel.gnoc.sr.dto.SRFlowExecuteDTO;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import com.viettel.gnoc.sr.model.SRCatalogEntity;
import com.viettel.gnoc.sr.repository.SRCatalogChildRepository;
import com.viettel.gnoc.sr.repository.SRCatalogRepository;
import com.viettel.gnoc.sr.repository.SRConfigRepository;
import com.viettel.gnoc.sr.repository.SRRoleUserRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
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
public class SRCatalogBusinessImpl implements SRCatalogBusiness {

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
  protected SRCatalogRepository srCatalogRepository;

  @Autowired
  protected SRCatalogChildRepository srCatalogChildRepository;

  @Autowired
  protected UnitBusiness unitBusiness;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  protected CatLocationBusiness catLocationBusiness;

  @Autowired
  protected SRConfigBusiness srConfigBusiness;

  @Autowired
  protected SRRoleBusiness srRoleBusiness;

  @Autowired
  protected SRFlowExecuteBusiness srFlowExecuteBusiness;

  @Autowired
  protected SRRoleUserRepository srRoleUserRepository;

  @Autowired
  protected SRConfigRepository srConfigRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  List<SRCatalogChildDTO> srCatalogChildDTOS = new ArrayList<>();
  Map<String, String> mapCountryName = new HashMap<>();
  Map<String, String> mapSrToOutsideService = new HashMap<>();
  Map<String, String> mapOutsideServiceToSr = new HashMap<>();
  private final static String SR_CATALOG_CHILD_RESULT_IMPORT = "SR_CATALOG_CHILD_RESULT_IMPORT";
  private final static String SR_CATALOG_CHILD_EXPORT = "SR_CATALOG_CHILD_EXPORT";
  private final static String SR_CATALOG_RESULT_IMPORT = "SR_CATALOG_RESULT_IMPORT";
  private final static String SR_CATALOG_EXPORT = "SR_CATALOG_EXPORT";
  private final static String UNIT_RESULT_IMPORT = "UNIT_RESULT_IMPORT";
  private final static String UNIT_EXPORT = "UNIT_EXPORT";
  private Map<String, UnitDTO> mapExecutionUnit = new HashMap<>();
  private Map<String, SRRoleDTO> mapRoleCodeDuplicateInFile = new HashMap<>();

  @Override
  public Datatable getListSRCatalogPage(SRCatalogDTO srCatalogDTO) {
    Datatable datatable = srCatalogRepository.getListSRCatalogPage(srCatalogDTO);
    log.debug("Request to getListSRCatalogPage : {}", srCatalogDTO);
    //12/01/2020 dungpv edit lay cau hinh trang thai sr cho phep dich vu duoc phep xoa
    SRConfigDTO srConfigDTO = new SRConfigDTO();
    srConfigDTO.setConfigGroup(SR_CONFIG.CFG_STATUS_SR_DELETE_SR_CATALOG);
    List<SRConfigDTO> lstStatusConfig = srConfigBusiness.getByConfigGroup(srConfigDTO);
    List<String> lstStatus = new ArrayList<>();
    if (lstStatusConfig != null && !lstStatusConfig.isEmpty()) {
      for (SRConfigDTO item : lstStatusConfig) {
        lstStatus.add(item.getConfigCode());
      }
    } else {
      lstStatus.add(SR_STATUS.CLOSED);
    }
    srCatalogDTO.setLstStatusConfig(lstStatus);
    List<SRCatalogDTO> srCatalogNotUsingList = srCatalogRepository
        .getListSRCatalogNotUsing(srCatalogDTO);
    //end
    List<SRCatalogDTO> srCatalogDTOList = (List<SRCatalogDTO>) datatable.getData();
    for (SRCatalogDTO dto : srCatalogDTOList) {
      dto.setIsNotUsing("0");
      if (srCatalogNotUsingList != null && !srCatalogNotUsingList.isEmpty()) {
        for (SRCatalogDTO item : srCatalogNotUsingList) {
          if (item.getServiceId().equals(dto.getServiceId())) {
            dto.setIsNotUsing("1");
          }
        }
      }
    }
    datatable.setData(srCatalogDTOList);
    return datatable;
  }

  @Override
  public List<SRCatalogDTO> getListServiceChild(SRCatalogDTO srCatalogDTO) {
    log.debug("Request to getListServiceChild : {}", srCatalogDTO);
    return srCatalogRepository.getListServiceChild(srCatalogDTO);
  }

  @Override
  public List<SRCatalogDTO> getListSRCatalog(SRCatalogDTO srCatalogDTO) {
    log.debug("Request to getListSRCatalog : {}", srCatalogDTO);
    return srCatalogRepository.getListSRCatalog(srCatalogDTO);
  }

  @Override
  public ResultInSideDto delete(Long serviceId) {
    log.debug("Request to delete : {}", serviceId);
    ResultInSideDto resultInSideDto;
    SRCatalogDTO oldHis = getDetail(serviceId);
    resultInSideDto = srCatalogRepository.delete(serviceId);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(serviceId.toString());
        dataHistoryChange.setType("SR_CATALOG");
        //Old Object History
        dataHistoryChange.setOldObject(convertListName(oldHis));
        dataHistoryChange.setActionType("delete");
        //New Object History
        dataHistoryChange.setNewObject(convertListName(new SRCatalogDTO()));
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  @Override
  public SRCatalogDTO getDetail(Long serviceId) {
    log.debug("Request to getDetail : {}", serviceId);
    SRCatalogChildDTO srCatalogChildDTO = new SRCatalogChildDTO();

    SRRoleUserInSideDTO srRoleUserDTO = new SRRoleUserInSideDTO();
    List<SRRoleUserInSideDTO> userDTOList = new ArrayList<>();
    srCatalogChildDTO.setServiceId(serviceId);
    SRCatalogDTO srCatalogDTO = srCatalogRepository.getDetail(serviceId);
    GnocFileDto gnocFileDto = new GnocFileDto();
    gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.SR_CATALOG);
    gnocFileDto.setBusinessId(serviceId);
    srCatalogDTO.setGnocFileDtos(gnocFileRepository.getListGnocFileForSR(gnocFileDto));
    srCatalogDTO
        .setSrCatalogChildDTOList(srCatalogChildRepository.getListCatalogChild(srCatalogChildDTO));
    SRConfigDTO srToOutsideService = srConfigRepository
        .getConfigGroupByConfigCode(srCatalogDTO.getServiceCode(), "A", "1", serviceId);
    if (srToOutsideService != null) {
      srCatalogDTO.setSrConfig(srToOutsideService);
      srCatalogDTO.setSrToOutsideService(srToOutsideService.getConfigGroup());
    }
    SRConfigDTO outsideServiceToSr = srConfigRepository
        .getConfigGroupByConfigCode(srCatalogDTO.getServiceCode(), "A", "0", serviceId);
    if (outsideServiceToSr != null) {
      srCatalogDTO.setOutSideSrConfig(outsideServiceToSr);
      srCatalogDTO.setOutsideServiceToSr(outsideServiceToSr.getConfigGroup());
    }
    srRoleUserDTO.setCountry(srCatalogDTO.getCountry());
    srRoleUserDTO.setUnitId(Long.valueOf(srCatalogDTO.getExecutionUnit()));
    srRoleUserDTO.setRoleCode(srCatalogDTO.getRoleCode());
    List<SRRoleUserInSideDTO> srRoleUserDTOList = srRoleUserRepository
        .getListSRRoleUserByUnitId(srRoleUserDTO);
    String[] strAutoCreateSR = null;
    if (srCatalogDTO.getAutoCreateSR() != null) {
      strAutoCreateSR = srCatalogDTO.getAutoCreateSR().split(",");
      Map<String, String> mapAutoCreateSR = new HashMap<>();
      if (strAutoCreateSR != null) {
        for (int i = 0; i < strAutoCreateSR.length; i++) {
          mapAutoCreateSR.put(String.valueOf(i), strAutoCreateSR[i]);
        }
      }
      for (Map.Entry<String, String> item : mapAutoCreateSR.entrySet()) {
        for (int i = 0; i < srRoleUserDTOList.size(); i++) {
          SRRoleUserInSideDTO userDTO = srRoleUserDTOList.get(i);
          if (item.getKey().equals(String.valueOf(i))) {
            userDTO.setAutoCreateSR(item.getValue());
            userDTOList.add(userDTO);
          }
        }
      }
    } else {
      userDTOList.addAll(srRoleUserDTOList);
    }
    List<SRCatalogDTO> srCatalogDTOList = srCatalogRepository
        .getListRoleCodeByServiceCode(srCatalogDTO.getServiceCode());
    if (srCatalogDTOList != null && srCatalogDTOList.size() > 0) {
      String[] strServiceId = srCatalogDTOList.get(0).getServiceIdStr().split(",");
      Map<String, String> mapServiceId = new HashMap<>();
      if (strServiceId != null) {
        for (int i = 0; i < strServiceId.length; i++) {
          mapServiceId.put(String.valueOf(i), strServiceId[i]);
        }
      }
      for (Map.Entry<String, String> item : mapServiceId.entrySet()) {
        if (!(item.getValue().equals(serviceId.toString()))) {
          SRCatalogDTO srCatalog = srCatalogRepository.getDetail(Long.valueOf(item.getValue()));
          if (srCatalog != null) {
            SRRoleUserInSideDTO srRoleUser = new SRRoleUserInSideDTO();
            srRoleUser.setCountry(srCatalog.getCountry());
            srRoleUser.setUnitId(Long.valueOf(srCatalog.getExecutionUnit()));
            srRoleUser.setRoleCode(srCatalog.getRoleCode());
            List<SRRoleUserInSideDTO> srRoleUserList = srRoleUserRepository
                .getListSRRoleUserByUnitId(srRoleUser);
            if (StringUtils.isNotNullOrEmpty(srCatalog.getAutoCreateSR())) {
              String[] strAutoCreateSR1 = srCatalog.getAutoCreateSR().split(",");
              Map<String, String> mapAutoCreateSR1 = new HashMap<>();
              if (strAutoCreateSR != null) {
                for (int i = 0; i < strAutoCreateSR1.length; i++) {
                  mapAutoCreateSR1.put(String.valueOf(i), strAutoCreateSR1[i]);
                }
              }
              for (Map.Entry<String, String> item1 : mapAutoCreateSR1.entrySet()) {
                for (int i = 0; i < srRoleUserList.size(); i++) {
                  SRRoleUserInSideDTO userDTO = srRoleUserList.get(i);
                  if (item1.getKey().equals(String.valueOf(i))) {
                    userDTO.setAutoCreateSR(item1.getValue());
                    userDTOList.add(userDTO);
                  }
                }
              }
            } else {
              userDTOList.addAll(srRoleUserList);
            }
          }
        }
      }
    }

    srCatalogDTO.setSrRoleUserDTOList(userDTOList);
    return srCatalogDTO;
  }

  @Override
  public ResultInSideDto insert(List<MultipartFile> srFilesList,
      SRCatalogDTO srCatalogDTO) throws IOException {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    List<SRRoleUserInSideDTO> srRoleUserDTOList = srCatalogDTO.getSrRoleUserDTOList();
    Map<Long, SRRoleUserInSideDTO> mapData = new HashMap<>();
    if (srRoleUserDTOList != null) {
      srRoleUserDTOList.forEach(item -> {
        if (mapData.containsKey(item.getUnitId())) {
          SRRoleUserInSideDTO sr = mapData.get(item.getUnitId());
          sr.setRoleCode(sr.getRoleCode() + "," + item.getRoleCode());
          sr.setAutoCreateSR(sr.getAutoCreateSR() + "," + item.getAutoCreateSR());
          mapData.put(item.getUnitId(), sr);
        } else {
          mapData.put(item.getUnitId(), item);
        }
      });
    }
    srRoleUserDTOList = new ArrayList<>();
    for (Map.Entry<Long, SRRoleUserInSideDTO> entry : mapData.entrySet()) {
      SRRoleUserInSideDTO userRole = entry.getValue();
      srRoleUserDTOList.add(userRole);
    }
    // insert file
    for (GnocFileDto gnocFileDto : srCatalogDTO.getGnocFileDtos()) {
      if (gnocFileDto.getIndexFile() != null) {
        gnocFileDto.setMultipartFile(srFilesList.get(gnocFileDto.getIndexFile().intValue()));
      }
    }
    List<String> fileNames = new ArrayList<>();
    List<SRFilesDTO> srFilesDTOS = new ArrayList<>();
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    Date date = new Date();
    for (GnocFileDto gnocFileCatalogDto : srCatalogDTO.getGnocFileDtos()) {
      SRFilesDTO srFilesDTO = new SRFilesDTO();
      if (gnocFileCatalogDto.getMultipartFile() != null) {
        MultipartFile multipartFile = gnocFileCatalogDto.getMultipartFile();
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                multipartFile.getBytes(), null);
        String fullPathOld = FileUtils
            .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                uploadFolder, null);
        String fileName = gnocFileCatalogDto.getMultipartFile().getOriginalFilename();
        fileNames.add(FileUtils.getFileName(fullPath));
        //Start save file old
        srFilesDTO.setFilePath(fullPathOld);
        srFilesDTO.setFileName(FileUtils.getFileName(fullPathOld));
        srFilesDTO.setFileType(gnocFileCatalogDto.getFileType());
        srFilesDTO.setRequireCreateSR(gnocFileCatalogDto.getRequired());
        srFilesDTO.setObejctId(null);
        srFilesDTO.setFileGroup(SR_CONFIG.FILE_GROUP_SC);
        srFilesDTOS.add(srFilesDTO);
        //End save file old
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(fileName);
        gnocFileDto.setCreateUnitId(userToken.getDeptId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(userToken.getUserID());
        gnocFileDto.setCreateUserName(userToken.getUserName());
        gnocFileDto.setCreateTime(date);
        gnocFileDto.setRequired(gnocFileCatalogDto.getRequired());
        gnocFileDto.setFileType(gnocFileCatalogDto.getFileType());
        gnocFileDto.setMappingId(null);
        gnocFileDtos.add(gnocFileDto);
      } else {
        //Start save file old
        SRFilesDTO srFileUpdate = srCatalogRepository.findSRFile(gnocFileCatalogDto.getMappingId());
        srFilesDTO.setFilePath(srFileUpdate.getFilePath());
        srFilesDTO.setFileName(gnocFileCatalogDto.getFileName());
        srFilesDTO.setFileType(gnocFileCatalogDto.getFileType());
        srFilesDTO.setRequireCreateSR(gnocFileCatalogDto.getRequired());
        srFilesDTO.setObejctId(null);
        srFilesDTO.setFileGroup(SR_CONFIG.FILE_GROUP_SC);
        srFilesDTOS.add(srFilesDTO);
        //End save file old
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(gnocFileCatalogDto.getPath());
        gnocFileDto.setFileName(gnocFileCatalogDto.getFileName());
        gnocFileDto.setCreateUnitId(userToken.getDeptId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(userToken.getUserID());
        gnocFileDto.setCreateUserName(userToken.getUserName());
        gnocFileDto.setCreateTime(date);
        gnocFileDto.setRequired(gnocFileCatalogDto.getRequired());
        gnocFileDto.setFileType(gnocFileCatalogDto.getFileType());
        gnocFileDto.setMappingId(null);
        gnocFileDtos.add(gnocFileDto);
      }
    }

    //lay srCataLogId to insert
    String srCataLogServiceIds = "";
    //insert Catelog
    srCatalogDTO.setCreatedUser(userToken.getUserName());
    srCatalogDTO.setCreatedTime(date);
    for (SRRoleUserInSideDTO dto : srRoleUserDTOList) {
      srCatalogDTO.setAutoCreateSR(dto.getAutoCreateSR());
      srCatalogDTO.setRoleCode(dto.getRoleCode());
      srCatalogDTO.setExecutionUnit(dto.getUnitId().toString());
      if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getCreatedTimeCRWO())) {
        srCatalogDTO.setCreatedTimeCRWO(srCatalogDTO.getExecutionTime());
      }
      srCatalogDTO.setServiceCode(srCatalogDTO.getServiceCode().toUpperCase());
      srCatalogDTO.setCreatedUser(userToken.getUserName());
      srCatalogDTO.setCreatedTime(new Date(System.currentTimeMillis()));
      srCatalogDTO.setAttachFile(String.join(";", fileNames));
      resultInSideDto = srCatalogRepository.add(srCatalogDTO);
      if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
        //Add history
        try {
          List<String> keys = getAllKeysDTO();
          DataHistoryChange dataHistoryChange = new DataHistoryChange();
          dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
          dataHistoryChange.setType("SR_CATALOG");
          //Old Object History
          dataHistoryChange.setOldObject(convertListName(new SRCatalogDTO()));
          dataHistoryChange.setActionType("add");
          //New Object History
          dataHistoryChange.setNewObject(convertListName(srCatalogDTO));
          dataHistoryChange.setUserId(userToken.getUserID().toString());
          dataHistoryChange.setKeys(keys);
          commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
        } catch (Exception err) {
          log.error(err.getMessage());
        }
      }
      srCataLogServiceIds += "-" + resultInSideDto.getId();
      // insert file
      List<GnocFileDto> gnocFileDtosAdd = new ArrayList<>();
      for (int i = 0; i < srFilesDTOS.size(); i++) {
        SRFilesDTO srFilesDTO = srFilesDTOS.get(i);
        srFilesDTO.setObejctId(resultInSideDto.getId());
        ResultInSideDto resultFileDataOld = srCatalogRepository.addSRFile(srFilesDTO);
        GnocFileDto gnocFileDto = gnocFileDtos.get(i);
        gnocFileDto.setMappingId(resultFileDataOld.getId());
        gnocFileDtosAdd.add(gnocFileDto);
      }
      gnocFileRepository.saveListGnocFile(GNOC_FILE_BUSSINESS.SR_CATALOG, resultInSideDto.getId(),
          gnocFileDtosAdd);
      //insert Child
      List<SRCatalogChildDTO> srCatalogChildDTOList = srCatalogDTO.getSrCatalogChildDTOList();
      if (srCatalogChildDTOList != null && srCatalogChildDTOList.size() > 0) {
        for (SRCatalogChildDTO srCatalogChildDTO : srCatalogChildDTOList) {
          if (!StringUtils.isStringNullOrEmpty(srCatalogChildDTO.getChildId())) {
            srCatalogChildDTO.setChildId(null);
          }
          srCatalogChildDTO.setServiceId(resultInSideDto.getId());
          srCatalogChildDTO.setServiceCode(srCatalogDTO.getServiceCode());
          srCatalogChildRepository.add(srCatalogChildDTO);
        }
      }
    }

    //tiennv them xu ly add serviceId vao truong moi
    if (srCataLogServiceIds.startsWith("-")) {
      srCataLogServiceIds = srCataLogServiceIds.substring(1);
    }

    //insert SR goi he thong ngoai
    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getSrToOutsideService())) {
      SRConfigDTO srConfigDTO = new SRConfigDTO();
      srConfigDTO.setConfigCode(srCatalogDTO.getServiceCode());
      srConfigDTO.setConfigName(srCatalogDTO.getServiceName());
      srConfigDTO.setConfigGroup(srCatalogDTO.getSrToOutsideService());
      srConfigDTO.setStatus("A");
      srConfigDTO.setParentGroup("1");
      srConfigDTO.setCountry(srCatalogDTO.getCountry());
      //token
      srConfigDTO.setCreatedUser(userToken.getUserName());
      srConfigDTO.setCreatedTime(DateUtil
          .dateToStringWithPattern(new Date(System.currentTimeMillis()), "dd/MM/yyyy HH:mm:ss"));
//      String[] arrServiceIds = srCatalogDTO.getSrToOutsideService().split(",");
      //tiennv them
      srConfigDTO.setSrCfgServiceIds(
          getServiceIds(srCatalogDTO.getSrToOutsideService(), srCataLogServiceIds));
      if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        resultInSideDto = srConfigRepository.add(srConfigDTO);
      }
    }
    //insert He thong ngoai goi SR
    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getOutsideServiceToSr())) {
      SRConfigDTO srConfigDTO = new SRConfigDTO();
      srConfigDTO.setConfigCode(srCatalogDTO.getServiceCode());
      srConfigDTO.setConfigName(srCatalogDTO.getServiceName());
      srConfigDTO.setConfigGroup(srCatalogDTO.getOutsideServiceToSr());
      srConfigDTO.setStatus("A");
      srConfigDTO.setParentGroup("0");
      srConfigDTO.setCountry(srCatalogDTO.getCountry());
      //token
      srConfigDTO.setCreatedUser(userToken.getUserName());
      srConfigDTO.setCreatedTime(DateUtil
          .dateToStringWithPattern(new Date(System.currentTimeMillis()), "dd/MM/yyyy HH:mm:ss"));
      //tiennv them
      srConfigDTO.setSrCfgServiceIds(
          getServiceIds(srCatalogDTO.getOutsideServiceToSr(), srCataLogServiceIds));
      if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        resultInSideDto = srConfigRepository.add(srConfigDTO);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto update(List<MultipartFile> srFilesList,
      SRCatalogDTO srCatalogDTO) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    SRCatalogDTO oldHis = getDetail(srCatalogDTO.getServiceId());
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    List<Long> lstCatalogIdNew = new ArrayList<>();
    List<SRRoleUserInSideDTO> srRoleUserDTOList = srCatalogDTO.getSrRoleUserDTOList();
    Map<Long, SRRoleUserInSideDTO> mapData = new HashMap<>();
    Map<String, String> mapUnitServiceId = new HashMap<>();
    if (srRoleUserDTOList != null) {
      srRoleUserDTOList.forEach(item -> {
        if (mapData.containsKey(item.getUnitId())) {
          SRRoleUserInSideDTO sr = mapData.get(item.getUnitId());
          sr.setRoleCode(sr.getRoleCode() + "," + item.getRoleCode());
          sr.setAutoCreateSR(sr.getAutoCreateSR() + "," + item.getAutoCreateSR());
          mapData.put(item.getUnitId(), sr);
        } else {
          mapData.put(item.getUnitId(), item);
        }
      });
    }
    srRoleUserDTOList = new ArrayList<>();
    for (Map.Entry<Long, SRRoleUserInSideDTO> entry : mapData.entrySet()) {
      //lấy dược lits đã Map
      srRoleUserDTOList.add(entry.getValue());
    }
    List<SRCatalogDTO> srCatalogDTOList = new ArrayList<>();
    for (SRRoleUserInSideDTO dto : srRoleUserDTOList) {
      SRCatalogDTO srCa = new SRCatalogDTO();
      srCa.setAutoCreateSR(dto.getAutoCreateSR());
      srCa.setRoleCode(dto.getRoleCode());
      srCa.setExecutionUnit(dto.getUnitId().toString());
      srCatalogDTOList.add(srCa);
    }
    List<SRCatalogDTO> srCatalogList = srCatalogRepository
        .getListRoleCodeByServiceCode(srCatalogDTO.getServiceCode());
    if (srCatalogList != null && srCatalogList.size() > 0) {
      String[] strUnitExecution = srCatalogList.get(0).getExecutionUnit().split(",");
      Map<String, String> mapUnit = new HashMap<>();
      if (strUnitExecution != null) {
        for (int i = 0; i < strUnitExecution.length; i++) {
          mapUnit.put(String.valueOf(i), strUnitExecution[i]);
        }
      }
      String[] strServiceId = srCatalogList.get(0).getServiceIdStr().split(",");
      if (strUnitExecution != null && strServiceId != null) {
        for (int i = 0; i < strUnitExecution.length; i++) {
          for (int j = 0; j < strServiceId.length; j++) {
            if (i == j) {
              mapUnitServiceId.put(strUnitExecution[i], strServiceId[j]);
            }
          }
        }
      }
      //button save and update all start
      if (srCatalogDTO.getIsUpdateAllUnit().equals("0")) {
        for (SRCatalogDTO dto : srCatalogDTOList) {
          int check = 0;
          for (Map.Entry<String, String> item : mapUnitServiceId.entrySet()) {
            if (dto.getExecutionUnit().equals(item.getKey())) {
              check += 1;
            }
            if (dto.getExecutionUnit().equals(item.getKey()) && srCatalogDTO.getServiceId()
                .toString()
                .equals(item.getValue())) { //Nếu là con chính thì update lại
              if (srCatalogDTO != null) {
                srCatalogDTO.setUpdatedUser(userToken.getUserName());
                srCatalogDTO.setUpdatedTime(new Date(System.currentTimeMillis()));
                srCatalogDTO.setAutoCreateSR(dto.getAutoCreateSR());
                srCatalogDTO.setRoleCode(dto.getRoleCode());
                if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getCreatedTimeCRWO())) {
                  srCatalogDTO.setCreatedTimeCRWO(srCatalogDTO.getExecutionTime());
                }
                resultInSideDto = srCatalogRepository.add(srCatalogDTO);
                if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
                  //Add history
                  try {
                    List<String> keys = getAllKeysDTO();
                    DataHistoryChange dataHistoryChange = new DataHistoryChange();
                    dataHistoryChange.setRecordId(srCatalogDTO.getServiceId().toString());
                    dataHistoryChange.setType("SR_CATALOG");
                    //Old Object History
                    dataHistoryChange.setOldObject(convertListName(oldHis));
                    dataHistoryChange.setActionType("update");
                    //New Object History
                    dataHistoryChange.setNewObject(convertListName(srCatalogDTO));
                    dataHistoryChange.setUserId(userToken.getUserID().toString());
                    dataHistoryChange.setKeys(keys);
                    commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
                  } catch (Exception err) {
                    log.error(err.getMessage());
                  }
                }
              }
            }
          }
          if (check == 0) {
            //nếu không phải thì thêm mới
            if (srCatalogDTO != null) {
              SRCatalogDTO srCatalogNew = srCatalogDTO.clone();
              srCatalogNew.setServiceId(null);
              srCatalogNew.setCreatedUser(userToken.getUserName());
              srCatalogNew.setCreatedTime(new Date(System.currentTimeMillis()));
              srCatalogNew.setAutoCreateSR(dto.getAutoCreateSR());
              srCatalogNew.setRoleCode(dto.getRoleCode());
              srCatalogNew.setExecutionUnit(dto.getExecutionUnit());
              if (StringUtils.isStringNullOrEmpty(srCatalogNew.getCreatedTimeCRWO())) {
                srCatalogNew.setCreatedTimeCRWO(srCatalogNew.getExecutionTime());
              }
              resultInSideDto = srCatalogRepository.add(srCatalogNew);
              if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
                //Add history
                try {
                  List<String> keys = getAllKeysDTO();
                  DataHistoryChange dataHistoryChange = new DataHistoryChange();
                  dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
                  dataHistoryChange.setType("SR_CATALOG");
                  //Old Object History
                  dataHistoryChange.setOldObject(convertListName(new SRCatalogDTO()));
                  dataHistoryChange.setActionType("add");
                  //New Object History
                  dataHistoryChange.setNewObject(convertListName(srCatalogNew));
                  dataHistoryChange.setUserId(userToken.getUserID().toString());
                  dataHistoryChange.setKeys(keys);
                  commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
                } catch (Exception err) {
                  log.error(err.getMessage());
                }
              }
              lstCatalogIdNew.add(resultInSideDto.getId());
            }
          }
        }
      } else {
        for (SRCatalogDTO dto : srCatalogDTOList) {
          int check = 0;
          for (Map.Entry<String, String> item : mapUnitServiceId.entrySet()) {
            if (dto.getExecutionUnit().equals(item.getKey())) {
              check += 1;
            }
            if (dto.getExecutionUnit().equals(
                item.getKey()) && srCatalogDTO
                != null) { //nếu đã có unit đã dc thêm mới rồi thì update lại roleCode và autoSR
              SRCatalogDTO srCatalogUpdate = srCatalogDTO.clone();
              srCatalogUpdate.setServiceId(Long.valueOf(item.getValue()));
              srCatalogUpdate.setUpdatedUser(userToken.getUserName());
              srCatalogUpdate.setUpdatedTime(new Date(System.currentTimeMillis()));
              srCatalogUpdate.setAutoCreateSR(dto.getAutoCreateSR());
              srCatalogUpdate.setRoleCode(dto.getRoleCode());
              srCatalogUpdate.setExecutionUnit(dto.getExecutionUnit());
              if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getCreatedTimeCRWO())) {
                srCatalogUpdate.setCreatedTimeCRWO(srCatalogDTO.getExecutionTime());
              }
              resultInSideDto = srCatalogRepository.add(srCatalogUpdate);
              if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
                //Add history
                try {
                  List<String> keys = getAllKeysDTO();
                  DataHistoryChange dataHistoryChange = new DataHistoryChange();
                  dataHistoryChange.setRecordId(srCatalogDTO.getServiceId().toString());
                  dataHistoryChange.setType("SR_CATALOG");
                  //Old Object History
                  dataHistoryChange.setOldObject(convertListName(oldHis));
                  dataHistoryChange.setActionType("update");
                  //New Object History
                  dataHistoryChange.setNewObject(convertListName(srCatalogUpdate));
                  dataHistoryChange.setUserId(userToken.getUserID().toString());
                  dataHistoryChange.setKeys(keys);
                  commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
                } catch (Exception err) {
                  log.error(err.getMessage());
                }
              }
            }
          }
          if (check == 0) {
            //nếu không phải thì thêm mới
            if (srCatalogDTO != null) {
              SRCatalogDTO srCatalogNew = srCatalogDTO.clone();
              srCatalogNew.setServiceId(null);
              srCatalogNew.setCreatedUser(userToken.getUserName());
              srCatalogNew.setCreatedTime(new Date(System.currentTimeMillis()));
              srCatalogNew.setAutoCreateSR(dto.getAutoCreateSR());
              srCatalogNew.setRoleCode(dto.getRoleCode());
              srCatalogNew.setExecutionUnit(dto.getExecutionUnit());
              if (StringUtils.isStringNullOrEmpty(srCatalogNew.getCreatedTimeCRWO())) {
                srCatalogNew.setCreatedTimeCRWO(srCatalogNew.getExecutionTime());
              }
              resultInSideDto = srCatalogRepository.add(srCatalogNew);
              if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
                //Add history
                try {
                  List<String> keys = getAllKeysDTO();
                  DataHistoryChange dataHistoryChange = new DataHistoryChange();
                  dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
                  dataHistoryChange.setType("SR_CATALOG");
                  //Old Object History
                  dataHistoryChange.setOldObject(convertListName(new SRCatalogDTO()));
                  dataHistoryChange.setActionType("add");
                  //New Object History
                  dataHistoryChange.setNewObject(convertListName(srCatalogNew));
                  dataHistoryChange.setUserId(userToken.getUserID().toString());
                  dataHistoryChange.setKeys(keys);
                  commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
                } catch (Exception err) {
                  log.error(err.getMessage());
                }
              }
              lstCatalogIdNew.add(resultInSideDto.getId());
            }
          }
        }
      }
      //button save and update all end
    }
    for (Map.Entry<String, String> itemDelete : mapUnitServiceId.entrySet()) {
      int check = 0;
      for (SRCatalogDTO dto : srCatalogDTOList) {
        if (dto.getExecutionUnit().equals(itemDelete.getKey())) {
          check += 1;
        }
      }
      if (check == 0) {
        resultInSideDto = srCatalogRepository.delete(Long.valueOf(itemDelete.getValue()));
        if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
          //Add history
          try {
            List<String> keys = getAllKeysDTO();
            DataHistoryChange dataHistoryChange = new DataHistoryChange();
            dataHistoryChange.setRecordId(itemDelete.getValue());
            dataHistoryChange.setType("SR_CATALOG");
            //Old Object History
            dataHistoryChange.setOldObject(convertListName(oldHis));
            dataHistoryChange.setActionType("delete");
            //New Object History
            dataHistoryChange.setNewObject(convertListName(new SRCatalogDTO()));
            dataHistoryChange.setUserId(userToken.getUserID().toString());
            dataHistoryChange.setKeys(keys);
            commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
          } catch (Exception err) {
            log.error(err.getMessage());
          }
        }
      }
    }

    setOutsideService(srCatalogDTO, userToken, resultInSideDto);

    //tiennv them xu ly update file
    processFileAttachUpdate(srFilesList, srCatalogDTO, userToken, unitToken);

    //update catalogChild start
    List<SRCatalogChildDTO> srCatalogChildDTOList = srCatalogDTO.getSrCatalogChildDTOList();
    if (srCatalogDTO.getIsUpdateAllUnit().equals("0")) {
      SRCatalogChildDTO srCatalogChildDTO = new SRCatalogChildDTO();
      srCatalogChildDTO.setServiceId(srCatalogDTO.getServiceId());
      List<SRCatalogChildDTO> lstSRCatalogChildDelete = srCatalogChildRepository
          .getListCatalogChild(srCatalogChildDTO);
      if (lstSRCatalogChildDelete != null && lstSRCatalogChildDelete.size() > 0) {
        for (SRCatalogChildDTO srCatalogChild : lstSRCatalogChildDelete) {
          if (srCatalogChild.getChildId() != null) {
            resultInSideDto = srCatalogChildRepository.delete(srCatalogChild.getChildId());
          }
        }
      }
      if (srCatalogChildDTOList != null && srCatalogChildDTOList.size() > 0) {
        for (SRCatalogChildDTO dto : srCatalogChildDTOList) {
          dto.setServiceId(srCatalogDTO.getServiceId());
          dto.setServiceCode(srCatalogDTO.getServiceCode());
          resultInSideDto = srCatalogChildRepository.add(dto);
        }
      }

    } else {
      for (Map.Entry<String, String> item : mapUnitServiceId.entrySet()) {
        SRCatalogChildDTO srCatalogChildDTO = new SRCatalogChildDTO();
        srCatalogChildDTO.setServiceId(Long.valueOf(item.getValue()));
        List<SRCatalogChildDTO> lstSRCatalogChildDelete = srCatalogChildRepository
            .getListCatalogChild(srCatalogChildDTO);
        if (lstSRCatalogChildDelete != null && lstSRCatalogChildDelete.size() > 0) {
          for (SRCatalogChildDTO srCatalogChild : lstSRCatalogChildDelete) {
            if (srCatalogChild.getChildId() != null) {
              resultInSideDto = srCatalogChildRepository.delete(srCatalogChild.getChildId());
            }
          }
        }
        if (srCatalogChildDTOList != null && srCatalogChildDTOList.size() > 0) {
          for (SRCatalogChildDTO dto : srCatalogChildDTOList) {
            dto.setChildId(null);
            dto.setServiceId(Long.valueOf(item.getValue()));
            dto.setServiceCode(srCatalogDTO.getServiceCode());
            resultInSideDto = srCatalogChildRepository.add(dto);
          }
        }
      }
    }
    //update catalogChild end

    //insert new catalogchild start
    if (lstCatalogIdNew != null && lstCatalogIdNew.size() > 0) {
      for (Long newId : lstCatalogIdNew) {
        if (srCatalogChildDTOList != null && srCatalogChildDTOList.size() > 0) {
          for (SRCatalogChildDTO dto : srCatalogChildDTOList) {
            dto.setServiceId(newId);
            dto.setServiceCode(srCatalogDTO.getServiceCode());
            resultInSideDto = srCatalogChildRepository.add(dto);
          }
        }
      }
    }
    //insert new catalogchild end
    return resultInSideDto;
  }


  private void setOutsideService(SRCatalogDTO srCatalogDTO, UserToken userToken,
      ResultInSideDto resultInSideDto) {
    //lat tat ca srCatalog co serviceCode giong nhau
    String srCataLogServiceIdsAllUnit = "";
    List<SRCatalogEntity> srCatalogEntities = srCatalogRepository
        .findSRCatalogByServiceCode(srCatalogDTO.getServiceCode());
    if (srCatalogEntities != null && srCatalogEntities.size() > 0) {
      for (SRCatalogEntity entity : srCatalogEntities) {
        srCataLogServiceIdsAllUnit += "-" + entity.getServiceId();
      }
    }
    if (srCataLogServiceIdsAllUnit.startsWith("-")) {
      srCataLogServiceIdsAllUnit = srCataLogServiceIdsAllUnit.substring(1);
    }
    //cap nhat sr goi he thong ngoai
    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getSrToOutsideService())) {
      SRConfigDTO srConfigDTO = new SRConfigDTO();
      SRConfigDTO srConfigDTOOld = new SRConfigDTO();
      if (srCatalogDTO.getSrConfig() != null) {
        srConfigDTO = srCatalogDTO.getSrConfig();
        srConfigDTOOld = srConfigRepository.getDetail(srConfigDTO.getConfigId());
      } else {
        srConfigDTO.setCreatedUser(userToken.getUserName());
        srConfigDTO.setCreatedTime(DateUtil
            .dateToStringWithPattern(new Date(System.currentTimeMillis()), "dd/MM/yyyy HH:mm:ss"));
        srConfigDTO.setStatus("A");
        srConfigDTO.setCountry(srCatalogDTO.getCountry());
        srConfigDTO.setParentGroup("1");
      }
      srConfigDTO.setConfigCode(srCatalogDTO.getServiceCode());
      srConfigDTO.setConfigName(srCatalogDTO.getServiceName());
      srConfigDTO.setConfigGroup(srCatalogDTO.getSrToOutsideService());
      srConfigDTO.setUpdatedUser(userToken.getUserName());
      srConfigDTO.setUpdatedTime(DateUtil
          .dateToStringWithPattern(new Date(System.currentTimeMillis()), "dd/MM/yyyy HH:mm:ss"));

      //tiennv them update cho tat ca don vi
      if (srConfigDTOOld.getConfigId() != null) { // neu cau hinh da ton tai
        if ("1".equals(srCatalogDTO.getIsUpdateAllUnit())) {
          srConfigDTO.setSrCfgServiceIds(getServiceIds(srCatalogDTO.getSrToOutsideService(),
              srCataLogServiceIdsAllUnit)); //cap nhat cho tat ca don vi
        } else {
          List<String> arrReturn = getServiceIdsForCurrentUnit(srConfigDTOOld.getConfigGroup(),
              srCatalogDTO.getSrToOutsideService(),
              srCatalogDTO.getServiceId() == null ? null : srCatalogDTO.getServiceId().toString(),
              srConfigDTOOld.getSrCfgServiceIds());
          if (arrReturn != null && arrReturn.size() == 2) {
            srConfigDTO.setConfigGroup(arrReturn.get(0));
            srConfigDTO.setSrCfgServiceIds(arrReturn.get(1));
          }
        }
      } else { //neu la them moi cau hinh
        String[] serviceOtherIds = srCatalogDTO.getSrToOutsideService().split(",");
        String serviceIds = "";
        if ("1".equals(srCatalogDTO.getIsUpdateAllUnit())) {
          srConfigDTO.setSrCfgServiceIds(getServiceIds(srCatalogDTO.getSrToOutsideService(),
              srCataLogServiceIdsAllUnit)); //cap nhat cho tat ca don vi
        } else {
          String serviceId =
              (srCatalogDTO.getServiceId() == null) ? "" : srCatalogDTO.getServiceId().toString();
          for (int i = 0; i < serviceOtherIds.length; i++) {
            serviceIds += "," + serviceId;
          }
          if (serviceIds.startsWith(",")) {
            serviceIds = serviceIds.substring(1);
          }
          srConfigDTO.setSrCfgServiceIds(serviceIds); //cap nhat cho tat ca don vi
        }
      }
      if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        if (StringUtils.isNotNullOrEmpty(srConfigDTO.getConfigGroup())) {
          resultInSideDto = srConfigRepository.add(srConfigDTO);
        } else {
          if (srConfigDTO.getConfigId() != null) {
            resultInSideDto = srConfigRepository.delete(srConfigDTO.getConfigId());
          }
        }
      }
    } else {
      if (srCatalogDTO.getSrConfig() != null) {
        SRConfigDTO srConfigDTO = srCatalogDTO.getSrConfig();
        Long srConfigId = srConfigDTO.getConfigId();
        SRConfigDTO srConfigDTOOld = srConfigRepository.getDetail(srConfigId);
        if (srConfigDTOOld != null) {
          if ("1".equals(srCatalogDTO.getIsUpdateAllUnit())) {
            srConfigBusiness.delete(srConfigId);
          } else {
            List<String> arrReturn = getServiceIdsForCurrentUnit(srConfigDTOOld.getConfigGroup(),
                "",
                srCatalogDTO.getServiceId() == null ? null : srCatalogDTO.getServiceId().toString(),
                srConfigDTOOld.getSrCfgServiceIds());
            if (arrReturn != null && arrReturn.size() == 2) {
              if (StringUtils.isStringNullOrEmpty(arrReturn.get(0)) || StringUtils
                  .isStringNullOrEmpty(arrReturn.get(1))) {
                srConfigBusiness
                    .delete(srConfigId); //chi con mot minh cau hinh cho don vi nay, thi xoa luon
              } else {
                srConfigDTOOld.setConfigGroup(arrReturn.get(0));
                srConfigDTOOld.setSrCfgServiceIds(arrReturn.get(1));
                if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                  resultInSideDto = srConfigRepository.add(srConfigDTOOld);
                }
              }
            }
          }
        }
      }
    }
    //cap nhat he thong ngoai goi sr
    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getOutsideServiceToSr())) {
      SRConfigDTO srConfigDTO = new SRConfigDTO();
      SRConfigDTO srConfigDTOOld = new SRConfigDTO();
      if (srCatalogDTO.getOutSideSrConfig() != null) {
        srConfigDTO = srCatalogDTO.getOutSideSrConfig();
        srConfigDTOOld = srConfigRepository.getDetail(srConfigDTO.getConfigId());
      } else {
        srConfigDTO.setCreatedUser(userToken.getUserName());
        srConfigDTO.setCreatedTime(DateUtil
            .dateToStringWithPattern(new Date(System.currentTimeMillis()), "dd/MM/yyyy HH:mm:ss"));
        srConfigDTO.setStatus("A");
        srConfigDTO.setCountry(srCatalogDTO.getCountry());
        srConfigDTO.setParentGroup("0");
      }
      srConfigDTO.setConfigCode(srCatalogDTO.getServiceCode());
      srConfigDTO.setConfigName(srCatalogDTO.getServiceName());
      srConfigDTO.setConfigGroup(srCatalogDTO.getOutsideServiceToSr());
      srConfigDTO.setUpdatedUser(userToken.getUserName());
      srConfigDTO.setUpdatedTime(DateUtil
          .dateToStringWithPattern(new Date(System.currentTimeMillis()), "dd/MM/yyyy HH:mm:ss"));

      //tiennv them update cho tat ca don vi
      if (srConfigDTOOld.getConfigId() != null) { // neu cau hinh da ton tai
        if ("1".equals(srCatalogDTO.getIsUpdateAllUnit())) {
          srConfigDTO.setSrCfgServiceIds(getServiceIds(srCatalogDTO.getOutsideServiceToSr(),
              srCataLogServiceIdsAllUnit)); //cap nhat cho tat ca don vi
        } else {
          List<String> arrReturn = getServiceIdsForCurrentUnit(srConfigDTOOld.getConfigGroup(),
              srCatalogDTO.getOutsideServiceToSr(),
              srCatalogDTO.getServiceId() == null ? null : srCatalogDTO.getServiceId().toString(),
              srConfigDTOOld.getSrCfgServiceIds());
          if (arrReturn != null && arrReturn.size() == 2) {
            srConfigDTO.setConfigGroup(arrReturn.get(0));
            srConfigDTO.setSrCfgServiceIds(arrReturn.get(1));
          }
        }
      } else { //neu la them moi cau hinh
        String[] serviceOtherIds = srCatalogDTO.getOutsideServiceToSr().split(",");
        String serviceIds = "";
        if ("1".equals(srCatalogDTO.getIsUpdateAllUnit())) {
          srConfigDTO.setSrCfgServiceIds(getServiceIds(srCatalogDTO.getOutsideServiceToSr(),
              srCataLogServiceIdsAllUnit)); //cap nhat cho tat ca don vi
        } else {
          String serviceId =
              (srCatalogDTO.getServiceId() == null) ? "" : srCatalogDTO.getServiceId().toString();
          for (int i = 0; i < serviceOtherIds.length; i++) {
            serviceIds += "," + serviceId;
          }
          if (serviceIds.startsWith(",")) {
            serviceIds = serviceIds.substring(1);
          }
          srConfigDTO.setSrCfgServiceIds(serviceIds); //cap nhat cho tat ca don vi
        }
      }

      if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        if (StringUtils.isNotNullOrEmpty(srConfigDTO.getConfigGroup())) {
          resultInSideDto = srConfigRepository.add(srConfigDTO);
        } else {
          if (srConfigDTO.getConfigId() != null) {
            resultInSideDto = srConfigRepository.delete(srConfigDTO.getConfigId());
          }
        }
      }
    } else {
      if (srCatalogDTO.getOutSideSrConfig() != null) {
        SRConfigDTO srConfigDTO = srCatalogDTO.getOutSideSrConfig();
        Long srConfigId = srConfigDTO.getConfigId();
        SRConfigDTO srConfigDTOOld = srConfigRepository.getDetail(srConfigId);
        if (srConfigDTOOld != null) {
          if ("1".equals(srCatalogDTO.getIsUpdateAllUnit())) {
            srConfigBusiness.delete(srConfigId);
          } else {
            List<String> arrReturn = getServiceIdsForCurrentUnit(srConfigDTOOld.getConfigGroup(),
                "",
                srCatalogDTO.getServiceId() == null ? null : srCatalogDTO.getServiceId().toString(),
                srConfigDTOOld.getSrCfgServiceIds());
            if (arrReturn != null && arrReturn.size() == 2) {
              if (StringUtils.isStringNullOrEmpty(arrReturn.get(0)) || StringUtils
                  .isStringNullOrEmpty(arrReturn.get(1))) {
                srConfigBusiness
                    .delete(srConfigId); //chi con mot minh cau hinh cho don vi nay, thi xoa luon
              } else {
                srConfigDTO.setConfigGroup(arrReturn.get(0));
                srConfigDTO.setSrCfgServiceIds(arrReturn.get(1));
                if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                  resultInSideDto = srConfigRepository.add(srConfigDTOOld);
                }
              }
            }
          }
        }
      }
    }
  }

  public void setSrToOutsideServiceStr() {
    SRConfigDTO srConfig = new SRConfigDTO();
    srConfig.setConfigGroup("OTHER_SYS_SERVICE");
    srConfig.setParentGroup("1");
    List<SRConfigDTO> systemCallOut = srConfigBusiness.getByConfigGroup(srConfig);
    if (systemCallOut != null && !systemCallOut.isEmpty()) {
      for (SRConfigDTO item : systemCallOut) {
        mapSrToOutsideService.put(String.valueOf(item.getConfigCode()), item.getConfigName());
      }
    }
  }

  public void setOutsideServiceToSrStr() {
    SRConfigDTO srConfig = new SRConfigDTO();
    srConfig.setConfigGroup("OTHER_SYS_SERVICE");
    srConfig.setParentGroup("0");
    List<SRConfigDTO> callOutSystem = srConfigBusiness.getByConfigGroup(srConfig);
    if (callOutSystem != null && !callOutSystem.isEmpty()) {
      for (SRConfigDTO item : callOutSystem) {
        mapOutsideServiceToSr.put(String.valueOf(item.getConfigCode()), item.getConfigName());
      }
    }
  }

  public void setMapCountryName() {
    List<ItemDataCRInside> lstCountryName = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    if (lstCountryName != null && !lstCountryName.isEmpty()) {
      for (ItemDataCRInside itemDataCR : lstCountryName) {
        mapCountryName.put(String.valueOf(itemDataCR.getValueStr()), itemDataCR.getDisplayStr());
      }
    }
  }

  @Override
  public File getCatalogTemplate() throws Exception {
    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    //apache POI XSSF
    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workbook.getSheetAt(0);
    XSSFSheet sheetParam = workbook.createSheet("param");
    XSSFSheet sheetParam1 = workbook.createSheet("param1");
    XSSFSheet sheetParam2 = workbook.createSheet("param2");
    XSSFSheet sheetParam3 = workbook.createSheet("param3");
    XSSFSheet sheetParam4 = workbook.createSheet("param4");
    XSSFSheet sheetParam6 = workbook.createSheet("param6");
    //Tạo 1 mảng lưu header từng cột
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("srCatalog.countryName"),
        I18n.getLanguage("srCatalog.serviceArrayName"),
        I18n.getLanguage("srCatalog.serviceGroupName"),
        I18n.getLanguage("srCatalog.serviceCode"),
        I18n.getLanguage("srCatalog.serviceName"),
        I18n.getLanguage("srCatalog.executionUnit"),
        I18n.getLanguage("srCatalog.replyTime"),
        I18n.getLanguage("srCatalog.executionTime"),
        I18n.getLanguage("srCatalog.cr"),
        I18n.getLanguage("srCatalog.wo"),
        I18n.getLanguage("srCatalog.statusName"),
        I18n.getLanguage("srCatalog.roleCode"),
        I18n.getLanguage("srCatalog.flowExecute"),
        I18n.getLanguage("srCatalog.createdTimeCRWO"),
        I18n.getLanguage("srCatalog.isAddDay"),
        I18n.getLanguage("srCatalog.renewDay"),
        I18n.getLanguage("srCatalog.startAutoDate"),
        I18n.getLanguage("srCatalog.monthCycle"),
        I18n.getLanguage("srCatalog.srToOutsideServiceStr"),
        I18n.getLanguage("srCatalog.outsideServiceToSrStr"),
        I18n.getLanguage("srCatalog.timeClosedSR")
    };
    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("srCatalog.countryName"),
        I18n.getLanguage("srCatalog.serviceArrayName"),
        I18n.getLanguage("srCatalog.serviceGroupName"),
        I18n.getLanguage("srCatalog.serviceCode"),
        I18n.getLanguage("srCatalog.serviceName"),
        I18n.getLanguage("srCatalog.executionUnit"),
        I18n.getLanguage("srCatalog.replyTime"),
        I18n.getLanguage("srCatalog.executionTime"),
        I18n.getLanguage("srCatalog.statusName"),
        I18n.getLanguage("srCatalog.roleCode"),
        I18n.getLanguage("srCatalog.flowExecute"),
        I18n.getLanguage("srCatalog.timeClosedSR")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int countryNameColumn = listHeader.indexOf(I18n.getLanguage("srCatalog.countryName"));
    int statusNameColumn = listHeader.indexOf(I18n.getLanguage("srCatalog.statusName"));
    int crColumn = listHeader.indexOf(I18n.getLanguage("srCatalog.cr"));
    int woColumn = listHeader.indexOf(I18n.getLanguage("srCatalog.wo"));
    int isAddDayNameColumn = listHeader.indexOf(I18n.getLanguage("srCatalog.isAddDay"));
    int srToOutsideServiceStrColumn = listHeader
        .indexOf(I18n.getLanguage("srCatalog.srToOutsideServiceStr"));
    int outsideServiceToSrStrColumn = listHeader
        .indexOf(I18n.getLanguage("srCatalog.outsideServiceToSrStr"));
    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(2);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("srCatalog.title"));
    titleCell.setCellStyle(style.get("title"));

    Row firstLeftHeaderRow = sheetOne.createRow(0);
    Cell firstLeftHeaderCell = firstLeftHeaderRow.createCell(7);
    firstLeftHeaderRow.setHeightInPoints(16);
    firstLeftHeaderCell.setCellValue(I18n.getLanguage("common.export.firstLeftHeader"));
    firstLeftHeaderCell.setCellStyle(style.get("indexTitle"));
    Row secondLeftHeaderRow = sheetOne.createRow(1);
    Cell secondLeftHeaderCell = secondLeftHeaderRow.createCell(7);
    secondLeftHeaderRow.setHeightInPoints(16);
    secondLeftHeaderCell.setCellValue(I18n.getLanguage("common.export.secondLeftHeader"));
    secondLeftHeaderCell.setCellStyle(style.get("indexTitle"));
    Cell firstRightHeaderCell = firstLeftHeaderRow.createCell(11);
    firstLeftHeaderRow.setHeightInPoints(16);
    firstRightHeaderCell.setCellValue(I18n.getLanguage("common.export.firstRightHeader"));
    firstRightHeaderCell.setCellStyle(style.get("indexTitle"));
    Cell secondRightHeaderCell = secondLeftHeaderRow.createCell(11);
    secondLeftHeaderRow.setHeightInPoints(16);
    secondRightHeaderCell.setCellValue(I18n.getLanguage("common.export.secondRightHeader"));
    secondRightHeaderCell.setCellStyle(style.get("indexTitle"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header

    Row headerRow = sheetOne.createRow(4);
    Row headerArrayService = sheetParam1.createRow(0);
    Row headerUnitExecute = sheetParam2.createRow(0);
    Row headerRoleExecute = sheetParam3.createRow(0);
    Row headerFlowExecute = sheetParam4.createRow(0);
    Row headerSystemService = sheetParam6.createRow(0);
    headerRow.setHeightInPoints(16);
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
    Cell headerCellCountry2 = headerArrayService.createCell(0);
    Cell headerCellCodeArr = headerArrayService.createCell(1);
    Cell headerCellNameArr = headerArrayService.createCell(2);
    Cell headerCellCodeGr = headerArrayService.createCell(3);
    Cell headerCellNameGr = headerArrayService.createCell(4);

    Cell headerCellUnitId = headerUnitExecute.createCell(0);
    Cell headerCellUnitCode = headerUnitExecute.createCell(1);
    Cell headerCellUnitName = headerUnitExecute.createCell(2);

    Cell headerCellCountry = headerRoleExecute.createCell(0);
    Cell headerCellRoleCode = headerRoleExecute.createCell(1);
    Cell headerCellRoleName = headerRoleExecute.createCell(2);

    Cell headerCellCountry1 = headerFlowExecute.createCell(0);
    Cell headerCellFlowCode = headerFlowExecute.createCell(1);
    Cell headerCellFlowName = headerFlowExecute.createCell(2);

    Cell headerCellServiceCode = headerSystemService.createCell(0);
    Cell headerCellServiceName = headerSystemService.createCell(1);
    Cell headerCellSysName = headerSystemService.createCell(2);

    XSSFRichTextString serviceArrCode = new XSSFRichTextString(
        I18n.getLanguage("srCatalog.serviceArrCode"));
    XSSFRichTextString serviceArrName = new XSSFRichTextString(
        I18n.getLanguage("srCatalog.serviceArrName"));
    XSSFRichTextString ServiceGroupCode = new XSSFRichTextString(
        I18n.getLanguage("srCatalog.ServiceGroupCode"));
    XSSFRichTextString ServiceGroupName = new XSSFRichTextString(
        I18n.getLanguage("srCatalog.ServiceGroupName"));
    XSSFRichTextString unitId = new XSSFRichTextString(I18n.getLanguage("srCatalog.unitId"));
    XSSFRichTextString unitCode = new XSSFRichTextString(I18n.getLanguage("srCatalog.unitCode"));
    XSSFRichTextString unitName = new XSSFRichTextString(I18n.getLanguage("srCatalog.unitName"));
    XSSFRichTextString country = new XSSFRichTextString(I18n.getLanguage("srCatalog.countryName"));
    XSSFRichTextString roleCode = new XSSFRichTextString(I18n.getLanguage("srCatalog.roleCode"));
    XSSFRichTextString flowCode = new XSSFRichTextString(I18n.getLanguage("srCatalog.flowCode"));
    XSSFRichTextString flowName = new XSSFRichTextString(I18n.getLanguage("srCatalog.flowName"));
    XSSFRichTextString systemName = new XSSFRichTextString(
        I18n.getLanguage("srCatalog.systemName"));
    XSSFRichTextString serviceCode = new XSSFRichTextString(
        I18n.getLanguage("srCatalog.serviceCode"));
    XSSFRichTextString serviceName = new XSSFRichTextString(
        I18n.getLanguage("srCatalog.serviceName"));
    XSSFRichTextString roleName = new XSSFRichTextString(I18n.getLanguage("srCatalog.roleName"));

    headerCellCountry.setCellValue(country);
    headerCellCountry.setCellStyle(style.get("header"));
    headerCellCountry2.setCellValue(country);
    headerCellCountry2.setCellStyle(style.get("header"));
    headerCellCodeArr.setCellValue(serviceArrCode);
    headerCellCodeArr.setCellStyle(style.get("header"));
    headerCellNameArr.setCellValue(serviceArrName);
    headerCellNameArr.setCellStyle(style.get("header"));
    headerCellUnitId.setCellValue(unitId);
    headerCellUnitId.setCellStyle(style.get("header"));
    headerCellUnitCode.setCellValue(unitCode);
    headerCellUnitCode.setCellStyle(style.get("header"));
    headerCellCodeGr.setCellValue(ServiceGroupCode);
    headerCellCodeGr.setCellStyle(style.get("header"));
    headerCellNameGr.setCellValue(ServiceGroupName);
    headerCellNameGr.setCellStyle(style.get("header"));
    headerCellUnitName.setCellValue(unitName);
    headerCellUnitName.setCellStyle(style.get("header"));
    headerCellRoleCode.setCellValue(roleCode);
    headerCellRoleCode.setCellStyle(style.get("header"));
    headerCellRoleName.setCellValue(roleName);
    headerCellRoleName.setCellStyle(style.get("header"));
    headerCellCountry1.setCellValue(country);
    headerCellCountry1.setCellStyle(style.get("header"));
    headerCellFlowCode.setCellValue(flowCode);
    headerCellFlowCode.setCellStyle(style.get("header"));
    headerCellFlowName.setCellValue(flowName);
    headerCellFlowName.setCellStyle(style.get("header"));
    headerCellServiceCode.setCellValue(serviceCode);
    headerCellServiceCode.setCellStyle(style.get("header"));
    headerCellServiceName.setCellValue(serviceName);
    headerCellServiceName.setCellStyle(style.get("header"));
    headerCellSysName.setCellValue(systemName);
    headerCellSysName.setCellStyle(style.get("header"));

    sheetOne.setColumnWidth(0, 3000);

    // Set dữ liệu vào column dropdown
    int row = 5;

    List<ItemDataCRInside> countryNameList = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    for (ItemDataCRInside dto : countryNameList) {
      excelWriterUtils.createCell(sheetParam, 1, row++, dto.getDisplayStr(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(0);

    Name countryName = workbook.createName();
    countryName.setNameName("countryName");
    countryName.setRefersToFormula("param!$B$2:$B$" + row);

    XSSFDataValidationConstraint countryNameConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "countryName");
    CellRangeAddressList countryNameCreate = new CellRangeAddressList(5, 65000, countryNameColumn,
        countryNameColumn);
    XSSFDataValidation dataValidationCountryName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            countryNameConstraint, countryNameCreate);
    dataValidationCountryName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationCountryName);

    row = 1;
    for (ItemDataCRInside cou : countryNameList) {
      excelWriterUtils.createCell(sheetParam1, 0, row, cou.getDisplayStr(), style.get("cell"));
      SRConfigDTO srConfigDTO = new SRConfigDTO();
      srConfigDTO.setCountry(cou.getValueStr().toString());
      srConfigDTO.setConfigGroup("SERVICE_ARRAY");
      List<SRConfigDTO> serviceArrayList = srConfigBusiness.getByConfigGroup(srConfigDTO);
      for (SRConfigDTO dto : serviceArrayList) {
        excelWriterUtils.createCell(sheetParam1, 1, row, dto.getConfigCode(), style.get("cell"));
        excelWriterUtils.createCell(sheetParam1, 2, row, dto.getConfigName(), style.get("cell"));
        List<SRConfigDTO> serviceGroup = srConfigBusiness.getListConfigGroup(dto.getConfigCode());
        for (SRConfigDTO configDTO : serviceGroup) {
          excelWriterUtils
              .createCell(sheetParam1, 3, row, configDTO.getConfigCode(), style.get("cell"));
          excelWriterUtils
              .createCell(sheetParam1, 4, row++, configDTO.getConfigName(), style.get("cell"));
        }
      }
      if (serviceArrayList != null && !serviceArrayList.isEmpty()) {
        excelWriterUtils.createCell(sheetParam1, 0, row++, null, style.get("cell"));
        excelWriterUtils.createCell(sheetParam1, 0, row++, null, style.get("cell"));
      }
    }
    sheetParam1.autoSizeColumn(0);
    sheetParam1.autoSizeColumn(1);
    sheetParam1.autoSizeColumn(2);
    sheetParam1.autoSizeColumn(4);

    row = 1;
    List<UnitDTO> unitNameList = unitBusiness.getListUnit(null);
    for (UnitDTO dto : unitNameList) {
      excelWriterUtils
          .createCell(sheetParam2, 0, row, dto.getUnitId().toString(), style.get("cell"));
      excelWriterUtils.createCell(sheetParam2, 1, row, dto.getUnitCode(), style.get("cell"));
      excelWriterUtils.createCell(sheetParam2, 2, row++, dto.getUnitName(), style.get("cell"));
    }
    sheetParam2.autoSizeColumn(0);
    sheetParam2.autoSizeColumn(1);
    sheetParam2.autoSizeColumn(2);

    row = 1;
    for (ItemDataCRInside dto : countryNameList) {
      excelWriterUtils.createCell(sheetParam3, 0, row, dto.getDisplayStr(), style.get("cell"));
      SRRoleDTO srRoleDTO = new SRRoleDTO();
      srRoleDTO.setCountry(dto.getValueStr().toString());
      List<SRRoleDTO> srRoleDTOList = srRoleBusiness.getListSRRoleByLocationCBB(srRoleDTO);
      for (SRRoleDTO srRole : srRoleDTOList) {
        excelWriterUtils.createCell(sheetParam3, 1, row, srRole.getRoleCode(), style.get("cell"));
        excelWriterUtils.createCell(sheetParam3, 2, row++, srRole.getRoleName(), style.get("cell"));
      }
      excelWriterUtils.createCell(sheetParam3, 0, row++, null, style.get("cell"));
    }
    sheetParam3.autoSizeColumn(0);
    sheetParam3.autoSizeColumn(1);
    sheetParam3.autoSizeColumn(2);

    row = 1;
    for (ItemDataCRInside dto : countryNameList) {
      excelWriterUtils.createCell(sheetParam4, 0, row, dto.getDisplayStr(), style.get("cell"));
      SRFlowExecuteDTO srFlowExecuteDTO = new SRFlowExecuteDTO();
      srFlowExecuteDTO.setCountry(dto.getValueStr());
      List<SRFlowExecuteDTO> srFlowExecuteDTOList = srFlowExecuteBusiness
          .getListSRFlowExecuteCBB(srFlowExecuteDTO);
      for (SRFlowExecuteDTO srFlow : srFlowExecuteDTOList) {
        excelWriterUtils
            .createCell(sheetParam4, 1, row, srFlow.getFlowId().toString(), style.get("cell"));
        excelWriterUtils.createCell(sheetParam4, 2, row++, srFlow.getFlowName(), style.get("cell"));
      }
      excelWriterUtils.createCell(sheetParam4, 0, row++, null, style.get("cell"));
    }
    sheetParam4.autoSizeColumn(0);
    sheetParam4.autoSizeColumn(1);
    sheetParam4.autoSizeColumn(2);

    row = 1;
    SRConfigDTO srConfig1 = new SRConfigDTO();
    srConfig1.setConfigGroup("OTHER_SYS_SERVICE");
    List<SRConfigDTO> systemList = srConfigBusiness.getByConfigGroup(srConfig1);
    for (SRConfigDTO srSystem : systemList) {
      SRConfigDTO srConfig2 = new SRConfigDTO();
      srConfig2.setConfigGroup(srSystem.getConfigCode());
      List<SRConfigDTO> systemServiceList = srConfigBusiness.getByConfigGroup(srConfig2);
      for (SRConfigDTO dto : systemServiceList) {
        excelWriterUtils.createCell(sheetParam6, 0, row, dto.getConfigCode(), style.get("cell"));
        excelWriterUtils.createCell(sheetParam6, 1, row, dto.getConfigName(), style.get("cell"));
        excelWriterUtils.createCell(sheetParam6, 2, row++, dto.getConfigGroup(), style.get("cell"));
      }
    }
    sheetParam6.autoSizeColumn(0);
    sheetParam6.autoSizeColumn(1);
    sheetParam6.autoSizeColumn(2);

    row = 5;
    excelWriterUtils.createCell(sheetParam, 9, row++, I18n.getLanguage("srCatalog.cr.1")
        , style.get("cell"));
    excelWriterUtils.createCell(sheetParam, 9, row++, I18n.getLanguage("srCatalog.cr.0")
        , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name cr = workbook.createName();
    cr.setNameName("cr");
    cr.setRefersToFormula("param!$J$2:$J$" + row);
    XSSFDataValidationConstraint crConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "cr");
    CellRangeAddressList crCreate = new CellRangeAddressList(5, 65000, crColumn,
        crColumn);
    XSSFDataValidation dataValidationCr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            crConstraint, crCreate);
    dataValidationCr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationCr);

    row = 5;
    excelWriterUtils.createCell(sheetParam, 10, row++, I18n.getLanguage("srCatalog.wo.1")
        , style.get("cell"));
    excelWriterUtils.createCell(sheetParam, 10, row++, I18n.getLanguage("srCatalog.wo.0")
        , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name wo = workbook.createName();
    wo.setNameName("wo");
    wo.setRefersToFormula("param!$K$2:$K$" + row);
    XSSFDataValidationConstraint woConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "wo");
    CellRangeAddressList woCreate = new CellRangeAddressList(5, 65000, woColumn,
        woColumn);
    XSSFDataValidation dataValidationWo = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            woConstraint, woCreate);
    dataValidationWo.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationWo);

    row = 5;
    excelWriterUtils.createCell(sheetParam, 11, row++, I18n.getLanguage("srCatalog.status.A")
        , style.get("cell"));
    excelWriterUtils.createCell(sheetParam, 11, row++, I18n.getLanguage("srCatalog.status.I")
        , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name statusName = workbook.createName();
    statusName.setNameName("statusName");
    statusName.setRefersToFormula("param!$L$2:$L$" + row);
    XSSFDataValidationConstraint statusNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "statusName");
    CellRangeAddressList statusNameCreate = new CellRangeAddressList(5, 65000, statusNameColumn,
        statusNameColumn);
    XSSFDataValidation dataValidationStatusName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            statusNameConstraint, statusNameCreate);
    dataValidationStatusName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationStatusName);

    row = 5;
    excelWriterUtils.createCell(sheetParam, 15, row++, I18n.getLanguage("srCatalog.isAddDay.1")
        , style.get("cell"));
    excelWriterUtils.createCell(sheetParam, 15, row++, I18n.getLanguage("srCatalog.isAddDay.0")
        , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name isAddDayName = workbook.createName();
    isAddDayName.setNameName("isAddDay");
    isAddDayName.setRefersToFormula("param!$P$2:$P$" + row);
    XSSFDataValidationConstraint isAddDayNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "isAddDay");
    CellRangeAddressList isAddDayNameCreate = new CellRangeAddressList(5, 65000, isAddDayNameColumn,
        isAddDayNameColumn);
    XSSFDataValidation dataValidationIsAddDayName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            isAddDayNameConstraint, isAddDayNameCreate);
    dataValidationIsAddDayName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationIsAddDayName);

    row = 5;
    SRConfigDTO srConfig = new SRConfigDTO();
    srConfig.setConfigGroup("OTHER_SYS_SERVICE");
    srConfig.setParentGroup("1");
    List<SRConfigDTO> systemCallOut = srConfigBusiness.getByConfigGroup(srConfig);
    for (SRConfigDTO dto : systemCallOut) {
      excelWriterUtils.createCell(sheetParam, 19, row++, dto.getConfigName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(0);

    Name srToOutsideServiceStr = workbook.createName();
    srToOutsideServiceStr.setNameName("srToOutsideServiceStr");
    srToOutsideServiceStr.setRefersToFormula("param!$T$2:$T$" + row);

    XSSFDataValidationConstraint srToOutsideServiceStrConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "srToOutsideServiceStr");
    CellRangeAddressList srToOutsideServiceStrCreate = new CellRangeAddressList(5, 65000,
        srToOutsideServiceStrColumn,
        srToOutsideServiceStrColumn);
    XSSFDataValidation dataValidationSrToOutsideServiceStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            srToOutsideServiceStrConstraint, srToOutsideServiceStrCreate);
    dataValidationSrToOutsideServiceStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationSrToOutsideServiceStr);

    row = 5;
    srConfig.setParentGroup("0");
    List<SRConfigDTO> callOutSystem = srConfigBusiness.getByConfigGroup(srConfig);
    for (SRConfigDTO dto : callOutSystem) {
      excelWriterUtils.createCell(sheetParam, 20, row++, dto.getConfigName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(0);

    Name outsideServiceToSrStr = workbook.createName();
    outsideServiceToSrStr.setNameName("outsideServiceToSrStr");
    outsideServiceToSrStr.setRefersToFormula("param!$U$2:$U$" + row);

    XSSFDataValidationConstraint outsideServiceToSrStrConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "outsideServiceToSrStr");
    CellRangeAddressList outsideServiceToSrStrCreate = new CellRangeAddressList(5, 65000,
        outsideServiceToSrStrColumn,
        outsideServiceToSrStrColumn);
    XSSFDataValidation dataValidationOutsideServiceToSrStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            outsideServiceToSrStrConstraint, outsideServiceToSrStrCreate);
    dataValidationOutsideServiceToSrStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationOutsideServiceToSrStr);

    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("srCatalog.title"));
    workbook.setSheetName(2, I18n.getLanguage("srCatalog.serviceArrayGroup"));
    workbook.setSheetName(3, I18n.getLanguage("srCatalog.UnitExecute"));
    workbook.setSheetName(4, I18n.getLanguage("srCatalog.RoleExecute"));
    workbook.setSheetName(5, I18n.getLanguage("srCatalog.FlowExecute"));
    workbook.setSheetName(6, I18n.getLanguage("srCatalog.SystemService"));
    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_SR_CATALOG" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  private void mapExecutionUnit() {
    List<UnitDTO> unitDTOS = unitBusiness.getListUnit(null);
    if (unitDTOS != null && !unitDTOS.isEmpty()) {
      for (UnitDTO unitDTO : unitDTOS) {
        if (!mapExecutionUnit.containsKey(unitDTO.getUnitId().toString())) {
          mapExecutionUnit.put(unitDTO.getUnitId().toString(), unitDTO);
        }
      }
    }
  }

  private void mapRoleCodeDuplicateInFile() {
    List<SRRoleDTO> srRoleDTOList = srRoleBusiness.getListSRRoleByLocationCBB(new SRRoleDTO());
    for (SRRoleDTO dto : srRoleDTOList) {
      String key = dto.getCountry() + dto.getRoleCode();
      if (!mapRoleCodeDuplicateInFile.containsKey(key)) {
        mapRoleCodeDuplicateInFile.put(key, dto);
      }
    }
  }

  @Override
  public File getChildTemplate() throws Exception {
    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    //apache POI XSSF
    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workbook.getSheetAt(0);
    XSSFSheet sheetParam = workbook.createSheet("param");
    XSSFSheet sheetParam1 = workbook.createSheet("param1");
    //Tạo 1 mảng lưu header từng cột
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("srCatalogChild.serviceCode"),
        I18n.getLanguage("srCatalogChild.serviceCodeChild"),
        I18n.getLanguage("srCatalogChild.action")
    };
    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("srCatalogChild.serviceCode"),
        I18n.getLanguage("srCatalogChild.serviceCodeChild"),
        I18n.getLanguage("srCatalogChild.action")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);
    int actionColumn = listHeader.indexOf(I18n.getLanguage("srCatalogChild.action"));
    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(2);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("srCatalogChild.title"));
    titleCell.setCellStyle(style.get("title"));

    Row firstLeftHeaderRow = sheetOne.createRow(0);
    Cell firstLeftHeaderCell = firstLeftHeaderRow.createCell(1);
    firstLeftHeaderRow.setHeightInPoints(16);
    firstLeftHeaderCell.setCellValue(I18n.getLanguage("common.export.firstLeftHeader"));
    firstLeftHeaderCell.setCellStyle(style.get("indexTitle"));
    Row secondLeftHeaderRow = sheetOne.createRow(1);
    Cell secondLeftHeaderCell = secondLeftHeaderRow.createCell(1);
    secondLeftHeaderRow.setHeightInPoints(16);
    secondLeftHeaderCell.setCellValue(I18n.getLanguage("common.export.secondLeftHeader"));
    secondLeftHeaderCell.setCellStyle(style.get("indexTitle"));
    Cell firstRightHeaderCell = firstLeftHeaderRow.createCell(4);
    firstLeftHeaderRow.setHeightInPoints(16);
    firstRightHeaderCell.setCellValue(I18n.getLanguage("common.export.firstRightHeader"));
    firstRightHeaderCell.setCellStyle(style.get("indexTitle"));
    Cell secondRightHeaderCell = secondLeftHeaderRow.createCell(4);
    secondLeftHeaderRow.setHeightInPoints(16);
    secondRightHeaderCell.setCellValue(I18n.getLanguage("common.export.secondRightHeader"));
    secondRightHeaderCell.setCellStyle(style.get("indexTitle"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header

    Row headerRow = sheetOne.createRow(4);
    Row headerUnit = sheetParam1.createRow(0);
    headerRow.setHeightInPoints(16);
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
    Cell headerCellStt = headerUnit.createCell(0);
    Cell headerCellUnitCode = headerUnit.createCell(1);
    Cell headerCellUnit = headerUnit.createCell(2);
    XSSFRichTextString stt = new XSSFRichTextString(I18n.getLanguage("common.STT"));
    XSSFRichTextString unitCode = new XSSFRichTextString(
        I18n.getLanguage("srCatalogChild.service"));
    XSSFRichTextString unit = new XSSFRichTextString(
        I18n.getLanguage("srCatalogChild.serviceNameChild"));
    headerCellStt.setCellValue(stt);
    headerCellStt.setCellStyle(style.get("header"));
    headerCellUnitCode.setCellValue(unitCode);
    headerCellUnitCode.setCellStyle(style.get("header"));
    headerCellUnit.setCellValue(unit);
    headerCellUnit.setCellStyle(style.get("header"));

    sheetParam.setColumnWidth(1, 3000);
    sheetParam.setColumnWidth(2, 20000);
    sheetOne.setColumnWidth(0, 3000);

    // Set dữ liệu vào column dropdown

    int row = 5;
    excelWriterUtils.createCell(sheetParam, 3, row++, I18n.getLanguage("srCatalogChild.action.1")
        , style.get("cell"));
    excelWriterUtils.createCell(sheetParam, 3, row++, I18n.getLanguage("srCatalogChild.action.0")
        , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name actionName = workbook.createName();
    actionName.setNameName("action");
    actionName.setRefersToFormula("param!$D$2:$D$" + row);
    XSSFDataValidationConstraint actionNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "action");
    CellRangeAddressList actionNameCreate = new CellRangeAddressList(5, 65000, actionColumn,
        actionColumn);
    XSSFDataValidation dataValidationActionName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            actionNameConstraint, actionNameCreate);
    dataValidationActionName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationActionName);

    row = 1;
    SRCatalogDTO srCatalogDTO = new SRCatalogDTO();
    List<SRCatalogDTO> srCatalogDTOList = srCatalogRepository.getListSRCatalog(srCatalogDTO);
    for (SRCatalogDTO dto : srCatalogDTOList) {
      excelWriterUtils.createCell(sheetParam1, 0, row, String.valueOf(row), style.get("cell"));
      excelWriterUtils.createCell(sheetParam1, 1, row, dto.getServiceCode(), style.get("cell"));
      excelWriterUtils.createCell(sheetParam1, 2, row++, dto.getServiceName(), style.get("cell"));
    }
    sheetParam1.autoSizeColumn(1);
    sheetParam1.autoSizeColumn(2);

    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("srCatalogChild.title"));
    workbook.setSheetName(2, I18n.getLanguage("srCatalogChild.listService"));
    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(true);

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_CATALOG_CHILD" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importDataCatalogChild(MultipartFile multipartFile) throws Exception {
    List<SRCatalogChildDTO> srCatalogChildDTOList = new ArrayList<>();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
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
        File fileImport = new File(filePath);

        List<Object[]> headerList;
        headerList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            4,
            0,
            3,
            1000
        );
        //Kiểm tra form header có đúng chuẩn
        if (headerList.size() == 0 || !validCatalogChildFileFormat(headerList)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }
        //Lấy dữ liệu import
        List<Object[]> dataImportList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            5,
            0,
            3,
            1000
        );
        if (dataImportList.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          return resultInSideDto;
        }

        srCatalogChildDTOS = new ArrayList<>();

        if (!dataImportList.isEmpty()) {
          int row = 4;
          int index = 0;
          for (Object[] obj : dataImportList) {
            SRCatalogChildDTO srCatalogChildDTO = new SRCatalogChildDTO();
            if (obj[1] != null) {
              srCatalogChildDTO.setServiceCode(obj[1].toString().trim());
            } else {
              srCatalogChildDTO.setServiceCode(null);
            }

            if (obj[2] != null) {
              srCatalogChildDTO.setServiceCodeChild(obj[2].toString().trim());
            } else {
              srCatalogChildDTO.setServiceCodeChild(null);
            }

            if (obj[3] != null) {
              srCatalogChildDTO.setAction(obj[3].toString().trim());
              srCatalogChildDTO.setActionName(srCatalogChildDTO.getAction());
              if (!(I18n.getLanguage("srCatalogChild.action.1")
                  .equals(srCatalogChildDTO.getAction()) || I18n
                  .getLanguage("srCatalogChild.action.0")
                  .equals(srCatalogChildDTO.getAction()))) {
                srCatalogChildDTO.setAction(null);
              }
            } else {
              srCatalogChildDTO.setActionName(null);
            }
            SRCatalogChildDTO srCatalogChildExportDTOTmp = validateCatalogChildImportInfo(
                srCatalogChildDTO, srCatalogChildDTOList);

            if (srCatalogChildExportDTOTmp.getResultImport() == null) {
              srCatalogChildExportDTOTmp
                  .setResultImport(I18n.getLanguage("srCatalogChild.result.import"));
              srCatalogChildDTOList.add(srCatalogChildExportDTOTmp);
              srCatalogChildDTOS.add(srCatalogChildExportDTOTmp);
            } else {
              srCatalogChildDTOList.add(srCatalogChildExportDTOTmp);
              index++;
            }
            row++;
          }
          if (index == 0) {
            if (!srCatalogChildDTOList.isEmpty()) {
              List<SRCatalogChildDTO> listAdd = new ArrayList<>();
              List<Long> listDelete = new ArrayList<>();
              for (SRCatalogChildDTO dto : srCatalogChildDTOList) {
                if (I18n.getLanguage("srCatalogChild.action.1")
                    .equals(dto.getAction())) {
                  listAdd.add(dto);
                }
                if (I18n.getLanguage("srCatalogChild.action.0")
                    .equals(dto.getAction())) {
                  listDelete.add(dto.getChildId());
                }
              }
              if (listAdd != null && !listAdd.isEmpty()) {
                resultInSideDto = srCatalogChildRepository.insertList(listAdd);
              }
              if (listDelete != null && !listDelete.isEmpty()) {
                resultInSideDto = srCatalogChildRepository.deleteList(listDelete);
              }
            }
          } else {
            File fileExport = exportCatalogChildFileTemplate(srCatalogChildDTOList,
                Constants.RESULT_IMPORT);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportCatalogChildFileTemplate(srCatalogChildDTOList,
              Constants.RESULT_IMPORT);
          resultInSideDto.setFile(fileExport);
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

  @Override
  public ResultInSideDto importDataCatalog(MultipartFile multipartFile) throws Exception {
    List<SRCatalogDTO> srCatalogDTOList = new ArrayList<>();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
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
        File fileImport = new File(filePath);

        List<Object[]> headerList;
        headerList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            4,
            0,
            22,
            1000
        );
        //Kiểm tra form header có đúng chuẩn
        if (headerList.size() == 0 || !validCatalogFileFormat(headerList)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }
        //Lấy dữ liệu import
        List<Object[]> dataImportList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            5,
            0,
            22,
            1000
        );
        if (dataImportList.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          return resultInSideDto;
        }
        mapExecutionUnit.clear();
        mapRoleCodeDuplicateInFile.clear();
        mapRoleCodeDuplicateInFile();
        mapExecutionUnit();
        setMapCountryName();
        setSrToOutsideServiceStr();
        setOutsideServiceToSrStr();
        if (!dataImportList.isEmpty()) {
          int row = 4;
          int index = 0;
          for (Object[] obj : dataImportList) {
            SRCatalogDTO srCatalogDTO = new SRCatalogDTO();
            if (obj[1] != null) {
              srCatalogDTO.setCountryName(obj[1].toString().trim());
              for (Map.Entry<String, String> item : mapCountryName.entrySet()) {
                if (srCatalogDTO.getCountryName().equals(item.getValue())) {
                  srCatalogDTO.setCountry(item.getKey());
                  break;
                } else {
                  srCatalogDTO.setCountry(null);
                }
              }
            } else {
              srCatalogDTO.setCountryName(null);
            }
            if (obj[2] != null) {
              srCatalogDTO.setServiceArrayName(obj[2].toString().trim());
              srCatalogDTO.setServiceArray(srCatalogDTO.getServiceArrayName());
            } else {
              srCatalogDTO.setServiceArrayName(null);
            }
            if (obj[3] != null) {
              srCatalogDTO.setServiceGroupName(obj[3].toString().trim());
              srCatalogDTO.setServiceGroup(srCatalogDTO.getServiceGroupName());
            } else {
              srCatalogDTO.setServiceGroupName(null);
            }
            if (obj[4] != null) {
              srCatalogDTO.setServiceCode(obj[4].toString().trim().toUpperCase());
            } else {
              srCatalogDTO.setServiceCode(null);
            }
            if (obj[5] != null) {
              srCatalogDTO.setServiceName(obj[5].toString().trim());
            } else {
              srCatalogDTO.setServiceName(null);
            }
            if (obj[6] != null) {
              if (!DataUtil.isInteger(obj[6].toString().trim())) {
                if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getResultImport())) {
                  srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.errType.executionUnit"));
                  srCatalogDTO.setExecutionUnitDesc(obj[6].toString().trim());
                } else {
                  srCatalogDTO.setResultImport(srCatalogDTO.getResultImport() + "\n" + I18n
                      .getLanguage("srCatalog.errType.executionUnit"));
                  srCatalogDTO.setExecutionUnitDesc(obj[6].toString().trim());
                }
              } else {
                srCatalogDTO.setExecutionUnitDesc(obj[6].toString().trim());
                srCatalogDTO.setExecutionUnit(obj[6].toString().trim());
              }
            } else {
              srCatalogDTO.setExecutionUnitDesc(null);
            }
            if (obj[7] != null) {
              srCatalogDTO.setReplyTimeName(obj[7].toString().trim());
              srCatalogDTO.setReplyTime(obj[7].toString().trim());
            } else {
              srCatalogDTO.setReplyTimeName(null);
              srCatalogDTO.setReplyTime(null);
            }
            if (obj[8] != null) {
              if (!DataUtil.isNumber(obj[8].toString().trim())) {
                if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getResultImport())) {
                  srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.errType.executionTime"));
                  srCatalogDTO.setExecutionTimeName(obj[8].toString().trim());
                } else {
                  srCatalogDTO.setResultImport(srCatalogDTO.getResultImport() + "\n" + I18n
                      .getLanguage("srCatalog.errType.executionTime"));
                  srCatalogDTO.setExecutionTimeName(obj[8].toString().trim());
                }
              } else {
                srCatalogDTO.setExecutionTimeName(obj[8].toString().trim());
                srCatalogDTO.setExecutionTime(obj[8].toString().trim());
              }
            } else {
              srCatalogDTO.setExecutionTimeName(null);
            }
            if (obj[9] != null) {
              srCatalogDTO.setCrDesc(obj[9].toString().trim());
              if (I18n.getLanguage("srCatalog.cr.1").equals(srCatalogDTO.getCrDesc())) {
                srCatalogDTO.setCr("1");
              } else if (I18n.getLanguage("srCatalog.cr.0")
                  .equals(srCatalogDTO.getCrDesc())) {
                srCatalogDTO.setCr("0");
              } else {
                srCatalogDTO.setCr(null);
              }
            } else {
              srCatalogDTO.setCrDesc(null);
            }
            if (obj[10] != null) {
              srCatalogDTO.setWoDesc(obj[10].toString().trim());
              if (I18n.getLanguage("srCatalog.wo.1").equals(srCatalogDTO.getWoDesc())) {
                srCatalogDTO.setWo("1");
              } else if (I18n.getLanguage("srCatalog.wo.0")
                  .equals(srCatalogDTO.getWoDesc())) {
                srCatalogDTO.setWo("0");
              } else {
                srCatalogDTO.setWo(null);
              }
            } else {
              srCatalogDTO.setWoDesc(null);
            }
            if (obj[11] != null) {
              srCatalogDTO.setStatusName(obj[11].toString().trim());
              if (I18n.getLanguage("srCatalog.status.A").equals(srCatalogDTO.getStatusName())) {
                srCatalogDTO.setStatus("A");
              } else if (I18n.getLanguage("srCatalog.status.I")
                  .equals(srCatalogDTO.getStatusName())) {
                srCatalogDTO.setStatus("I");
              } else {
                srCatalogDTO.setStatus(null);
              }
            } else {
              srCatalogDTO.setStatusName(null);
            }
            if (obj[12] != null) {
              srCatalogDTO.setRoleCode(obj[12].toString().trim());
            } else {
              srCatalogDTO.setRoleCode(null);
            }
            if (obj[13] != null) {
              if (!DataUtil.isInteger(obj[13].toString().trim())) {
                if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getResultImport())) {
                  srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.errType.flowExecute"));
                  srCatalogDTO.setFlowExecuteName(obj[13].toString().trim());
                } else {
                  srCatalogDTO.setResultImport(srCatalogDTO.getResultImport() + "\n" + I18n
                      .getLanguage("srCatalog.errType.flowExecute"));
                  srCatalogDTO.setFlowExecuteName(obj[13].toString().trim());
                }
              } else {
                srCatalogDTO.setFlowExecuteName(obj[13].toString().trim());
                srCatalogDTO.setFlowExecute(Long.valueOf(obj[13].toString().trim()));
              }
            } else {
              srCatalogDTO.setFlowExecuteName(null);
            }
            if (obj[14] != null) {
              if (!DataUtil.isNumber(obj[14].toString().trim())) {
                if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getResultImport())) {
                  srCatalogDTO
                      .setResultImport(I18n.getLanguage("srCatalog.errType.createdTimeCRWO"));
                  srCatalogDTO.setCreatedTimeCRWOName(obj[14].toString().trim());
                } else {
                  srCatalogDTO.setResultImport(srCatalogDTO.getResultImport() + "\n" + I18n
                      .getLanguage("srCatalog.errType.createdTimeCRWO"));
                  srCatalogDTO.setCreatedTimeCRWOName(obj[14].toString().trim());
                }
              } else {
                srCatalogDTO.setCreatedTimeCRWO(obj[14].toString().trim());
                srCatalogDTO.setCreatedTimeCRWOName(obj[14].toString().trim());
              }
            } else {
              srCatalogDTO.setCreatedTimeCRWOName(null);
            }
            if (obj[15] != null) {
              srCatalogDTO.setIsAddDayName(obj[15].toString().trim());
              if (I18n.getLanguage("srCatalog.isAddDay.1").equals(srCatalogDTO.getIsAddDayName())) {
                srCatalogDTO.setIsAddDay(1L);
              } else if (I18n.getLanguage("srCatalog.isAddDay.0")
                  .equals(srCatalogDTO.getIsAddDayName())) {
                srCatalogDTO.setIsAddDay(0L);
              } else {
                srCatalogDTO.setIsAddDay(null);
              }
            } else {
              srCatalogDTO.setIsAddDayName(null);
            }
            if (obj[16] != null) {
              if (!DataUtil.isInteger(obj[16].toString().trim())) {
                if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getResultImport())) {
                  srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.errType.renewDay"));
                  srCatalogDTO.setRenewDayName(obj[16].toString().trim());
                } else {
                  srCatalogDTO.setResultImport(srCatalogDTO.getResultImport() + "\n" + I18n
                      .getLanguage("srCatalog.errType.renewDay"));
                  srCatalogDTO.setRenewDayName(obj[16].toString().trim());
                }
              } else {
                srCatalogDTO.setRenewDayName(obj[16].toString().trim());
                srCatalogDTO.setRenewDay(Long.valueOf(obj[16].toString().trim()));
              }
            } else {
              srCatalogDTO.setRenewDayName(null);
            }
            if (obj[17] != null) {
              srCatalogDTO.setStartAutoDateStr(obj[17].toString().trim());
              String checkDate = DataUtil.validateDateTimeDdMmYyyy(obj[17].toString().trim());
              if (StringUtils.isStringNullOrEmpty(checkDate)) {
                SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                  if (obj[17].toString().trim().contains(" ")) {
                    srCatalogDTO
                        .setResultImport(I18n.getLanguage("srCatalog.startAutoDate.invalid"));
                  }
                  Date startAutoDate = sf.parse(obj[17].toString().trim());
                  if (startAutoDate.compareTo(sf.parse(sf.format(new Date()))) >= 0) {
                    srCatalogDTO.setStartAutoDate(sf.parse(obj[17].toString().trim()));
                  } else {
                    srCatalogDTO
                        .setResultImport(I18n.getLanguage("srCatalog.startAutoDate.error"));
                  }
                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                  srCatalogDTO
                      .setResultImport(I18n.getLanguage("srCatalog.startAutoDate.invalid"));
                }
              } else {
                srCatalogDTO
                    .setResultImport(I18n.getLanguage("srCatalog.errType.startAutoDate"));
                srCatalogDTO.setStartAutoDate(null);
              }
            } else {
              srCatalogDTO.setStartAutoDateStr(null);
              srCatalogDTO.setStartAutoDate(null);
            }
            if (obj[18] != null) {
              srCatalogDTO.setMonthCycleStr(obj[18].toString().trim());
              if (DataUtil.isNumber(obj[18].toString().trim())) {
                String[] arrMonthCycle = srCatalogDTO.getMonthCycleStr().split("\\.");
                if ((arrMonthCycle != null && arrMonthCycle.length == 2
                    && arrMonthCycle[1].length() > 2)
                    || Double.parseDouble(srCatalogDTO.getMonthCycleStr()) <= 0) {
                  srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.errType.monthCycle"));
                } else {
                  srCatalogDTO.setMonthCycle(obj[18].toString().trim());
                }
              } else {
                srCatalogDTO
                    .setResultImport(I18n.getLanguage("srCatalog.errType.monthCycle"));
                srCatalogDTO.setMonthCycle(null);
              }
            } else {
              srCatalogDTO.setMonthCycleStr(null);
              srCatalogDTO.setMonthCycle(null);
            }
            if (obj[19] != null) {
              srCatalogDTO.setSrToOutsideServiceStr(obj[19].toString().trim());
              for (Map.Entry<String, String> item : mapSrToOutsideService.entrySet()) {
                if (srCatalogDTO.getSrToOutsideServiceStr().equals(item.getValue())) {
                  srCatalogDTO.setSrToOutsideService(item.getKey());
                  break;
                } else {
                  srCatalogDTO.setSrToOutsideService(null);
                }
              }
            } else {
              srCatalogDTO.setSrToOutsideServiceStr(null);
            }

            if (obj[20] != null) {
              srCatalogDTO.setOutsideServiceToSrStr(obj[20].toString().trim());
              for (Map.Entry<String, String> item : mapOutsideServiceToSr.entrySet()) {
                if (srCatalogDTO.getOutsideServiceToSrStr().equals(item.getValue())) {
                  srCatalogDTO.setOutsideServiceToSr(item.getKey());
                  break;
                } else {
                  srCatalogDTO.setOutsideServiceToSr(null);
                }
              }
            } else {
              srCatalogDTO.setOutsideServiceToSrStr(null);
            }
            if (obj[21] != null) {
              srCatalogDTO.setTimeClosedSR(obj[21].toString().trim());
            } else {
              srCatalogDTO.setTimeClosedSR(null);
            }

            srCatalogDTO.setAutoCreateSR("0");
            SRCatalogDTO srCatalogDTOTmp = validateCatalogImportInfo(
                srCatalogDTO, srCatalogDTOList);

            if (srCatalogDTOTmp.getResultImport() == null) {
              srCatalogDTOTmp
                  .setResultImport(I18n.getLanguage("srCatalog.result.import"));
              srCatalogDTOList.add(srCatalogDTOTmp);
            } else {
              srCatalogDTOList.add(srCatalogDTOTmp);
              index++;
            }
            row++;
          }
          if (index == 0) {
            if (!srCatalogDTOList.isEmpty()) {
              //Hàm thêm mới
              for (SRCatalogDTO dto : srCatalogDTOList) {
                resultInSideDto = insertOrUpdateImport(dto);
              }
            }
          } else {
            File fileExport = exportCatalogFileTemplate(srCatalogDTOList,
                Constants.RESULT_IMPORT);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportCatalogFileTemplate(srCatalogDTOList,
              Constants.RESULT_IMPORT);
          resultInSideDto.setFile(fileExport);
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

  private boolean validCatalogChildFileFormat(List<Object[]> headerList) {
    Object[] objects = headerList.get(0);
    if (objects == null) {
      return false;
    }
    int count = 0;
    for (Object data : objects) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 4) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }
    if (!I18n.getLanguage("common.STT").equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srCatalogChild.serviceCode") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srCatalogChild.serviceCodeChild") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srCatalogChild.action") + "*")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    return true;
  }

  private boolean validCatalogFileFormat(List<Object[]> headerList) {
    Object[] objects = headerList.get(0);
    if (objects == null) {
      return false;
    }
    int count = 0;
    for (Object data : objects) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 22) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }
    if (!I18n.getLanguage("common.STT").equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srCatalog.countryName") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srCatalog.serviceArrayName") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srCatalog.serviceGroupName") + "*")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srCatalog.serviceCode") + "*")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srCatalog.serviceName") + "*")
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srCatalog.executionUnit") + "*")
        .equalsIgnoreCase(objects[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srCatalog.replyTime") + "*")
        .equalsIgnoreCase(objects[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srCatalog.executionTime") + "*")
        .equalsIgnoreCase(objects[8].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("srCatalog.cr")
        .equalsIgnoreCase(objects[9].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("srCatalog.wo")
        .equalsIgnoreCase(objects[10].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srCatalog.statusName") + "*")
        .equalsIgnoreCase(objects[11].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srCatalog.roleCode") + "*")
        .equalsIgnoreCase(objects[12].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srCatalog.flowExecute") + "*")
        .equalsIgnoreCase(objects[13].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("srCatalog.createdTimeCRWO")
        .equalsIgnoreCase(objects[14].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("srCatalog.isAddDay")
        .equalsIgnoreCase(objects[15].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("srCatalog.renewDay")
        .equalsIgnoreCase(objects[16].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("srCatalog.startAutoDate")
        .equalsIgnoreCase(objects[17].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("srCatalog.monthCycle")
        .equalsIgnoreCase(objects[18].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("srCatalog.srToOutsideServiceStr")
        .equalsIgnoreCase(objects[19].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("srCatalog.outsideServiceToSrStr")
        .equalsIgnoreCase(objects[20].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("srCatalog.timeClosedSR") + "*")
        .equalsIgnoreCase(objects[21].toString().trim())) {
      return false;
    }
    return true;
  }

  private SRCatalogChildDTO validateCatalogChildImportInfo(SRCatalogChildDTO srCatalogChildDTO,
      List<SRCatalogChildDTO> list) {

    if (StringUtils.isStringNullOrEmpty(srCatalogChildDTO.getServiceCode())) {
      srCatalogChildDTO.setResultImport(I18n.getLanguage("srCatalogChild.err.serviceCode"));
      return srCatalogChildDTO;
    }

    if (StringUtils.isStringNullOrEmpty(srCatalogChildDTO.getServiceCodeChild())) {
      srCatalogChildDTO.setResultImport(I18n.getLanguage("srCatalogChild.err.serviceCodeChild"));
      return srCatalogChildDTO;
    }

    if (StringUtils.isStringNullOrEmpty(srCatalogChildDTO.getAction()) && !StringUtils
        .isStringNullOrEmpty(srCatalogChildDTO.getActionName())) {
      srCatalogChildDTO.setResultImport(I18n.getLanguage("srCatalogChild.err.action.exist"));
      return srCatalogChildDTO;
    }

    if (StringUtils.isStringNullOrEmpty(srCatalogChildDTO.getActionName())) {
      srCatalogChildDTO.setResultImport(I18n.getLanguage("srCatalogChild.err.action"));
      return srCatalogChildDTO;
    }

    if (StringUtils.isNotNullOrEmpty(srCatalogChildDTO.getServiceCode()) && StringUtils
        .isNotNullOrEmpty(srCatalogChildDTO.getServiceCodeChild())) {
      SRCatalogChildDTO dto = new SRCatalogChildDTO();
      dto.setServiceCode(srCatalogChildDTO.getServiceCode());
      List<SRCatalogChildDTO> srCatalogChildDTOList = srCatalogChildRepository
          .getListCatalogChild(dto);
      if (srCatalogChildDTOList != null && !srCatalogChildDTOList.isEmpty()) {
        if (I18n.getLanguage("srCatalogChild.action.1")
            .equals(srCatalogChildDTO.getAction())) {
          srCatalogChildDTO.setResultImport(I18n.getLanguage("srCatalogChild.err.create"));
          return srCatalogChildDTO;
        }
        if (I18n.getLanguage("srCatalogChild.action.0")
            .equals(srCatalogChildDTO.getAction())) {
          srCatalogChildDTO.setChildId(srCatalogChildDTOList.get(0).getChildId());
//          return srCatalogChildDTO;
        }
      } else {
        if (I18n.getLanguage("srCatalogChild.action.0")
            .equals(srCatalogChildDTO.getAction())) {
          srCatalogChildDTO.setResultImport(I18n.getLanguage("srCatalogChild.err.delete"));
          return srCatalogChildDTO;
        }
        if (I18n.getLanguage("srCatalogChild.action.1")
            .equals(srCatalogChildDTO.getAction())) {
          SRCatalogDTO srCatalogDTO = new SRCatalogDTO();
          srCatalogDTO.setServiceCode(srCatalogChildDTO.getServiceCode());
          List<SRCatalogDTO> dtoList = srCatalogRepository.getListSRCatalog(srCatalogDTO);
          if (dtoList != null && !dtoList.isEmpty()) {
            srCatalogChildDTO.setServiceId(dtoList.get(0).getServiceId());
          } else {
            srCatalogChildDTO
                .setResultImport(I18n.getLanguage("srCatalogChild.err.noDataServiceCode"));
            return srCatalogChildDTO;
          }
          srCatalogDTO.setServiceCode(srCatalogChildDTO.getServiceCodeChild());
          dtoList.clear();
          dtoList = srCatalogRepository.getListSRCatalog(srCatalogDTO);
          if (dtoList != null && !dtoList.isEmpty()) {
            srCatalogChildDTO.setServiceIdChild(dtoList.get(0).getServiceId());
          } else {
            srCatalogChildDTO
                .setResultImport(I18n.getLanguage("srCatalogChild.err.noDataServiceCodeChild"));
            return srCatalogChildDTO;
          }
        }
      }
    }

    if (list != null && list.size() > 0 && srCatalogChildDTO.getResultImport() == null) {
      srCatalogChildDTO = validateCatalogChildDuplicate(list, srCatalogChildDTO);
    }

    return srCatalogChildDTO;
  }

  private SRCatalogDTO validateCatalogImportInfo(SRCatalogDTO srCatalogDTO,
      List<SRCatalogDTO> list) {
    if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getCountryName())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.countryName"));
      return srCatalogDTO;
    }

    if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getCountry())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.countryName.exist"));
      return srCatalogDTO;
    }

    if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getServiceArrayName())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.serviceArray"));
      return srCatalogDTO;
    }

    if (!(StringUtils.isStringNullOrEmpty(srCatalogDTO.getServiceArray())) && !(StringUtils
        .isStringNullOrEmpty(srCatalogDTO.getCountry()))) {
      boolean check = false;
      SRConfigDTO srConfigDTO = new SRConfigDTO();
      srConfigDTO.setCountry(srCatalogDTO.getCountry());
      srConfigDTO.setConfigGroup("SERVICE_ARRAY");
      List<SRConfigDTO> serviceArrayList = srConfigBusiness.getByConfigGroup(srConfigDTO);
      for (SRConfigDTO dto : serviceArrayList) {
        if (dto.getConfigCode().equals(srCatalogDTO.getServiceArray())) {
          check = true;
          break;
        }
      }
      if (!check) {
        srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.serviceArray.exist"));
        return srCatalogDTO;
      }
    }

    if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getServiceGroupName())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.serviceGroup"));
      return srCatalogDTO;
    }

    if (!(StringUtils.isStringNullOrEmpty(srCatalogDTO.getServiceGroup())) && !(StringUtils
        .isStringNullOrEmpty(srCatalogDTO.getServiceArray()))) {
      boolean check = false;
      List<SRConfigDTO> lstServiceGroup = srConfigBusiness
          .getListConfigGroup(srCatalogDTO.getServiceArray());
      for (SRConfigDTO dto : lstServiceGroup) {
        if (dto.getConfigCode().equals(srCatalogDTO.getServiceGroup())) {
          check = true;
          break;
        }
      }
      if (!check) {
        srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.serviceGroup.exist"));
        return srCatalogDTO;
      }
    }

    if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getServiceCode())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.serviceCode"));
      return srCatalogDTO;
    }
    if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getServiceName())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.serviceName"));
      return srCatalogDTO;
    }

    if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getExecutionUnitDesc())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.executionUnit"));
      return srCatalogDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getExecutionUnit())) {
      if (!mapExecutionUnit.containsKey(srCatalogDTO.getExecutionUnit())) {
        srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.executionUnit.exist"));
        return srCatalogDTO;
      }
    }
    if (!(StringUtils.isStringNullOrEmpty(srCatalogDTO.getCountry())) && !(StringUtils
        .isStringNullOrEmpty(srCatalogDTO.getExecutionUnit()))) {
      SRRoleUserInSideDTO srRoleUserDTO = new SRRoleUserInSideDTO();
      srRoleUserDTO.setUnitId(Long.valueOf(srCatalogDTO.getExecutionUnit()));
      srRoleUserDTO.setCountry(srCatalogDTO.getCountry());
      SRRoleUserInSideDTO userDTO = srRoleUserRepository.checkRoleUserExistByUnitId(srRoleUserDTO);
      if (userDTO == null) {
        srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.executionUnit.noConfig"));
        return srCatalogDTO;
      }
    }
    if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getReplyTimeName())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.replyTime"));
      return srCatalogDTO;
    }

    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getReplyTime()) && DataUtil
        .isNumber(srCatalogDTO.getReplyTime())) {
      String[] arrReplyTime = srCatalogDTO.getReplyTime().split("\\.");
      if ((arrReplyTime != null && arrReplyTime.length == 2 && !"5".equals(arrReplyTime[1]) && !"0"
          .equals(arrReplyTime[1]))
          || Double.parseDouble(srCatalogDTO.getReplyTime()) <= 0) {
        srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.errType.replyTime"));
        return srCatalogDTO;
      }
    } else {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.errType.replyTime"));
      return srCatalogDTO;
    }

    if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getExecutionTimeName())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.executionTime"));
      return srCatalogDTO;
    } else if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getExecutionTime()) && DataUtil
        .isNumber(srCatalogDTO.getExecutionTime())) {
      String[] arrExecutionTime = srCatalogDTO.getExecutionTime().split("\\.");
      if ((arrExecutionTime != null && arrExecutionTime.length == 2
          && arrExecutionTime[1].length() > 2)
          || Double.parseDouble(srCatalogDTO.getExecutionTime()) <= 0) {
        srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.errType.executionTime"));
      }
    }
    if (!(StringUtils.isStringNullOrEmpty(srCatalogDTO.getCrDesc())) && StringUtils
        .isStringNullOrEmpty(srCatalogDTO.getCr())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.cr.exist"));
      return srCatalogDTO;
    }

    if (!(StringUtils.isStringNullOrEmpty(srCatalogDTO.getWoDesc())) && StringUtils
        .isStringNullOrEmpty(srCatalogDTO.getWo())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.wo.exist"));
      return srCatalogDTO;
    }

    if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getStatusName())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.statusName"));
      return srCatalogDTO;
    }

    if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getStatus())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.status.exist"));
      return srCatalogDTO;
    }

    if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getRoleCode())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.roleCode"));
      return srCatalogDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getRoleCode()) && !StringUtils
        .isStringNullOrEmpty(srCatalogDTO.getCountry())) {
      Map<String, String> mapCheck = new HashMap<>();
      String[] arrayRoleCode = srCatalogDTO.getRoleCode().split(",");
      if (arrayRoleCode != null && arrayRoleCode.length > 0) {
        for (int i = 0; i < arrayRoleCode.length; i++) {
          String key = srCatalogDTO.getCountry().trim() + arrayRoleCode[i].trim();
          if (!mapCheck.containsKey(key)) {
            mapCheck.put(key, arrayRoleCode[i]);
          } else {
            srCatalogDTO.setResultImport(
                arrayRoleCode[i] + " " + I18n.getLanguage("srCatalog.err.roleCode.record.exist"));
            return srCatalogDTO;
          }
        }
      }
      if (mapCheck != null && !mapCheck.entrySet().isEmpty()) {
        for (Map.Entry<String, String> entry : mapCheck.entrySet()) {
          if (!mapRoleCodeDuplicateInFile.containsKey(entry.getKey())) {
            srCatalogDTO.setResultImport(
                entry.getValue() + " " + I18n.getLanguage("srCatalog.err.roleCode.exist"));
            return srCatalogDTO;
          }
        }
      }
      if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getExecutionUnit())) {
        SRRoleUserInSideDTO srRoleUserDTO = new SRRoleUserInSideDTO();
        srRoleUserDTO.setUnitId(Long.valueOf(srCatalogDTO.getExecutionUnit()));
        srRoleUserDTO.setCountry(srCatalogDTO.getCountry());
        srRoleUserDTO.setRoleCode(srCatalogDTO.getRoleCode());
        List<SRRoleUserInSideDTO> userDTOList = srRoleUserRepository
            .getListSRRoleUserByUnitId(srRoleUserDTO);
        if (userDTOList != null && !userDTOList.isEmpty()) {
          Map<String, String> mapRoleUserByUnitId = new HashMap<>();
          userDTOList.stream().forEach(c -> {
            if (!mapRoleUserByUnitId
                .containsKey(srRoleUserDTO.getCountry().trim() + c.getRoleCode().trim())) {
              mapRoleUserByUnitId
                  .put(srRoleUserDTO.getCountry().trim() + c.getRoleCode().trim(), c.getRoleCode());
            }
          });
          for (Map.Entry<String, String> entry : mapCheck.entrySet()) {
            if (!mapRoleUserByUnitId.containsKey((entry.getKey()))) {
              srCatalogDTO.setResultImport(
                  entry.getValue() + " " + I18n.getLanguage("srCatalog.err.roleCode.noConfig"));
              return srCatalogDTO;
            }
          }
        } else {
          srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.roleCode.noConfig"));
          return srCatalogDTO;
        }
      }
    }
    if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getFlowExecuteName())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.flowExecute"));
      return srCatalogDTO;
    }

    if (!(StringUtils.isStringNullOrEmpty(srCatalogDTO.getFlowExecute())) && !(StringUtils
        .isStringNullOrEmpty(srCatalogDTO.getCountry()))) {
      boolean check = false;
      SRFlowExecuteDTO srFlowExecuteDTO = new SRFlowExecuteDTO();
      srFlowExecuteDTO.setCountry(Long.valueOf(srCatalogDTO.getCountry()));
      List<SRFlowExecuteDTO> srFlowExecuteDTOList = srFlowExecuteBusiness
          .getListSRFlowExecuteCBB(srFlowExecuteDTO);
      for (SRFlowExecuteDTO dto : srFlowExecuteDTOList) {
        if (String.valueOf(dto.getFlowId()).equals(String.valueOf(srCatalogDTO.getFlowExecute()))) {
          check = true;
          break;
        }
      }
      if (!check) {
        srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.flowExecute.exist"));
        return srCatalogDTO;
      }
    }

    if (!(StringUtils.isStringNullOrEmpty(srCatalogDTO.getIsAddDay())) && StringUtils
        .isStringNullOrEmpty(srCatalogDTO.getIsAddDay())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.isAddDay.exist"));
      return srCatalogDTO;
    }

    if (!(StringUtils.isStringNullOrEmpty(srCatalogDTO.getSrToOutsideServiceStr())) && StringUtils
        .isStringNullOrEmpty(srCatalogDTO.getSrToOutsideService())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.srToOutsideService.exist"));
      return srCatalogDTO;
    }

    if (!(StringUtils.isStringNullOrEmpty(srCatalogDTO.getOutsideServiceToSrStr())) && StringUtils
        .isStringNullOrEmpty(srCatalogDTO.getOutsideServiceToSr())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.outsideServiceToSr.exist"));
      return srCatalogDTO;
    }

    if (!(StringUtils.isStringNullOrEmpty(srCatalogDTO.getCreatedTimeCRWO())) && !(StringUtils
        .isStringNullOrEmpty(srCatalogDTO.getExecutionTime()))
        && (Double.valueOf(srCatalogDTO.getCreatedTimeCRWO()) < 0.0D
        || Double.valueOf(srCatalogDTO.getCreatedTimeCRWO()) > Double
        .valueOf(srCatalogDTO.getExecutionTime()))) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.createdTimeCRWO.tooLong"));
      return srCatalogDTO;
    }

    if (!(StringUtils.isStringNullOrEmpty(srCatalogDTO.getServiceCode()))
        && srCatalogDTO.getServiceCode().length() > 200) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.serviceCode.tooLong"));
      return srCatalogDTO;
    }
    if (!(StringUtils.isStringNullOrEmpty(srCatalogDTO.getServiceName()))
        && srCatalogDTO.getServiceName().length() > 300) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.serviceName.tooLong"));
      return srCatalogDTO;
    }

    if (!(StringUtils.isStringNullOrEmpty(srCatalogDTO.getRenewDay()))
        && srCatalogDTO.getRenewDay() > 99L) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.renewDay.tooLong"));
      return srCatalogDTO;
    }
    boolean check = true;

    if ((
        StringUtils.isStringNullOrEmpty(srCatalogDTO.getMonthCycleStr()) && StringUtils
            .isStringNullOrEmpty(srCatalogDTO.getStartAutoDateStr())) || (
        StringUtils.isNotNullOrEmpty(srCatalogDTO.getMonthCycleStr()) && StringUtils
            .isNotNullOrEmpty(srCatalogDTO.getStartAutoDateStr()))) {
      check = false;
    }
    if (check) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.autoGenerationCycles.notnull"));
      return srCatalogDTO;
    }

    if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getTimeClosedSR())) {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.err.timeClosedSRName.notnull"));
      return srCatalogDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getTimeClosedSR()) && DataUtil
        .isNumber(srCatalogDTO.getTimeClosedSR())) {
      String[] arrTimeClosedSR = srCatalogDTO.getTimeClosedSR().split("\\.");
      if ((arrTimeClosedSR != null && arrTimeClosedSR.length > 2)
          || Double.parseDouble(srCatalogDTO.getTimeClosedSR()) <= 0) {
        srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.errType.timeClosedSR"));
        return srCatalogDTO;
      }
    } else {
      srCatalogDTO.setResultImport(I18n.getLanguage("srCatalog.errType.timeClosedSR"));
      return srCatalogDTO;
    }

    if (list != null && list.size() > 0 && srCatalogDTO.getResultImport() == null) {
      srCatalogDTO = validateCatalogDuplicate(list, srCatalogDTO);
    }

    return srCatalogDTO;
  }

  private SRCatalogChildDTO validateCatalogChildDuplicate(List<SRCatalogChildDTO> list,
      SRCatalogChildDTO srCatalogChildDTO) {
    for (int i = 0; i < list.size(); i++) {
      SRCatalogChildDTO srCatalogChildDTOTmp = list.get(i);
      if (I18n.getLanguage("srCatalogChild.result.import")
          .equals(srCatalogChildDTOTmp.getResultImport()) && srCatalogChildDTOTmp.getServiceCode()
          .equals(srCatalogChildDTO.getServiceCode())
          && srCatalogChildDTOTmp.getServiceCodeChild()
          .equals(srCatalogChildDTO.getServiceCodeChild()) && srCatalogChildDTOTmp.getAction()
          .equals(srCatalogChildDTO.getAction())) {
        srCatalogChildDTO
            .setResultImport(I18n.getLanguage("srCatalogChild.err.dup-code-in-file")
                .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
    }
    return srCatalogChildDTO;
  }

  private SRCatalogDTO validateCatalogDuplicate(List<SRCatalogDTO> list,
      SRCatalogDTO srCatalogDTO) {
    for (int i = 0; i < list.size(); i++) {
      SRCatalogDTO srCatalogDTOTmp = list.get(i);
      if (I18n.getLanguage("srCatalogChild.result.import")
          .equals(srCatalogDTOTmp.getResultImport()) && srCatalogDTOTmp.getServiceCode()
          .equals(srCatalogDTO.getServiceCode()) && srCatalogDTOTmp.getExecutionUnit()
          .equals(srCatalogDTO.getExecutionUnit())) {
        srCatalogDTO
            .setResultImport(I18n.getLanguage("srCatalogChild.err.dup-code-in-file")
                .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
    }
    return srCatalogDTO;
  }

  @Override
  public List<SRCatalogDTO> getListServiceNameToMapping() {
    return srCatalogRepository.getListServiceNameToMapping();
  }

  @Override
  public List<SRCatalogDTO> getListSRCatalogDTO(SRCatalogDTO srCatalogDTO) {
    return srCatalogRepository.getListSRCatalogDTO(srCatalogDTO);
  }

  @Override
  public File exportData(SRCatalogDTO srCatalogDTO) throws Exception {
    List<SRCatalogDTO> srCatalogDTOList = srCatalogRepository.getListDataExport(srCatalogDTO);
    if (srCatalogDTOList != null && !srCatalogDTOList.isEmpty()) {
      for (int i = srCatalogDTOList.size() - 1; i > -1; i--) {
        SRCatalogDTO srCatalogExport = srCatalogDTOList.get(i);
        if ("1".equals(srCatalogExport.getCr())) {
          srCatalogExport.setCrDesc(I18n.getLanguage("srCatalog.cr.1"));
        } else if ("0".equals(srCatalogExport.getCr())) {
          srCatalogExport.setCrDesc(I18n.getLanguage("srCatalog.cr.0"));
        }
        if ("1".equals(srCatalogExport.getWo())) {
          srCatalogExport.setWoDesc(I18n.getLanguage("srCatalog.wo.1"));
        } else if ("0".equals(srCatalogExport.getWo())) {
          srCatalogExport.setWoDesc(I18n.getLanguage("srCatalog.wo.0"));
        }
        if ("1".equals(String.valueOf(srCatalogExport.getIsAddDay()))) {
          srCatalogExport.setIsAddDayName(I18n.getLanguage("srCatalog.isAddDay.1"));
        } else if ("0".equals(String.valueOf(srCatalogExport.getIsAddDay()))) {
          srCatalogExport.setIsAddDayName(I18n.getLanguage("srCatalog.isAddDay.0"));
        }
        if ("A".equals(srCatalogExport.getStatus())) {
          srCatalogExport.setStatusName(I18n.getLanguage("srCatalog.status.A"));
        } else if ("I".equals(srCatalogExport.getStatus())) {
          srCatalogExport.setStatusName(I18n.getLanguage("srCatalog.status.I"));
        }
      }
    }
    return exportFileTemplate(srCatalogDTOList, "");
  }

  @Override
  public List<SRCatalogChildDTO> getListCatalogChild(SRCatalogChildDTO srCatalogChildDTO) {
    log.debug("Request to getListServiceChild : {}", srCatalogChildDTO);
    return srCatalogChildRepository.getListCatalogChild(srCatalogChildDTO);
  }

  private File exportFileTemplate(List<SRCatalogDTO> dtoList, String key)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("srCatalog.export.title");
    String title = I18n.getLanguage("srCatalog.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("countryName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("serviceArrayName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("serviceGroupName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("serviceCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("serviceName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("executionUnitDesc", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("serviceNameChild", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("replyTime", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("executionTime", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("crDesc", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("woDesc", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("statusName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("roleCode", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("flowExecuteName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("createdTimeCRWO", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("isAddDayName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("renewDay", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("startAutoDateStr", "LEFT", false, 0, 0,
        new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("monthCycle", "LEFT", false, 0, 0,
        new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    //kiểm tra đầu vào
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = SR_CATALOG_RESULT_IMPORT;
      subTitle = I18n
          .getLanguage("srCatalog.export.exportDate", DateTimeUtils.convertDateOffset());
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
    } else {
      fileNameOut = SR_CATALOG_EXPORT;
      subTitle = I18n
          .getLanguage("srCatalog.export.exportDate", DateTimeUtils.convertDateOffset());
    }
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        dtoList
        , sheetName
        , title
        , subTitle
        , 7
        , 3
        , 9
        , true
        , "language.srCatalog"
        , headerExportList
        , fieldSplit
        , ""
        , I18n.getLanguage("common.export.firstLeftHeader")
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
    fileExportList.add(configFileExport);
    //Cấu hình đường dẫn
    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;
    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExportList
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  private File exportCatalogChildFileTemplate(List<SRCatalogChildDTO> dtoList, String key)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("srCatalogChild.export.title");
    String title = I18n.getLanguage("srCatalogChild.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("serviceCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("serviceCodeChild", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("actionName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    //kiểm tra đầu vào
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = SR_CATALOG_CHILD_RESULT_IMPORT;
      subTitle = I18n
          .getLanguage("srCatalogChild.export.exportDate", DateTimeUtils.convertDateOffset());
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
    } else {
      fileNameOut = SR_CATALOG_CHILD_EXPORT;
      subTitle = I18n
          .getLanguage("srCatalogChild.export.exportDate", DateTimeUtils.convertDateOffset());
    }
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        dtoList
        , sheetName
        , title
        , subTitle
        , 7
        , 3
        , 9
        , true
        , "language.srCatalogChild"
        , headerExportList
        , fieldSplit
        , ""
        , I18n.getLanguage("common.export.firstLeftHeader")
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
    fileExportList.add(configFileExport);
    //Cấu hình đường dẫn
    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;
    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExportList
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  private File exportCatalogFileTemplate(List<SRCatalogDTO> dtoList, String key)
      throws Exception {

    String fileNameOut;
    String subTitle = "";
    String sheetName = I18n.getLanguage("srCatalog.title");
    String title = I18n.getLanguage("srCatalog.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("countryName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("serviceArrayName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("serviceGroupName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("serviceCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("serviceName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("executionUnitDesc", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("replyTimeName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("executionTimeName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("crDesc", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("woDesc", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("statusName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("roleCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("flowExecuteName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("createdTimeCRWOName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("isAddDayName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("renewDayName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("startAutoDateStr", "LEFT", false, 0, 0,
        new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("monthCycleStr", "LEFT", false, 0, 0,
        new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("srToOutsideServiceStr", "LEFT", false, 0, 0,
        new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("outsideServiceToSrStr", "LEFT", false, 0, 0,
        new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("timeClosedSR", "LEFT", false, 0, 0,
        new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    fileNameOut = SR_CATALOG_RESULT_IMPORT;
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        dtoList
        , sheetName
        , title
        , subTitle
        , 7
        , 3
        , 9
        , true
        , "language.srCatalog"
        , headerExportList
        , fieldSplit
        , ""
        , I18n.getLanguage("common.export.firstLeftHeader")
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
    fileExportList.add(configFileExport);
    //Cấu hình đường dẫn
    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;
    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExportList
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  private ResultInSideDto insertOrUpdateImport(SRCatalogDTO srCatalogDTO) {
    ResultInSideDto resultInSideDto;
    UserToken userToken = ticketProvider.getUserToken();
    SRCatalogDTO dto = srCatalogRepository
        .checkSRCatalogExist(srCatalogDTO.getServiceCode(), srCatalogDTO.getExecutionUnit());
    if (dto == null) {
      if (StringUtils.isStringNullOrEmpty(srCatalogDTO.getCreatedTimeCRWO())) {
        srCatalogDTO.setCreatedTimeCRWO(srCatalogDTO.getExecutionTime());
      }
      srCatalogDTO.setApprove(0L);
      srCatalogDTO.setCreatedUser(userToken.getUserName());
      srCatalogDTO.setCreatedTime(new Date(System.currentTimeMillis()));
      resultInSideDto = srCatalogRepository.add(srCatalogDTO);
      String srCataLogServiceIds = "";
      srCataLogServiceIds += "-" + resultInSideDto.getId();
      if (srCataLogServiceIds.startsWith("-")) {
        srCataLogServiceIds = srCataLogServiceIds.substring(1);
      }
      if (srCatalogDTO.getSrToOutsideService() != null && RESULT.SUCCESS
          .equals(resultInSideDto.getKey())) {
        SRConfigDTO srConfigDTO = new SRConfigDTO();
        srConfigDTO.setCountry(srCatalogDTO.getCountry());
        srConfigDTO.setConfigCode(srCatalogDTO.getServiceCode());
        srConfigDTO.setConfigName(srCatalogDTO.getServiceName());
        srConfigDTO.setConfigGroup(srCatalogDTO.getSrToOutsideService());
        srConfigDTO.setStatus("A");
        srConfigDTO.setParentGroup("1");
        srConfigDTO.setCreatedUser(userToken.getUserName());
        srConfigDTO.setCreatedTime(DateUtil
            .dateToStringWithPattern(new Date(System.currentTimeMillis()), "dd/MM/yyyy HH:mm:ss"));
        srConfigDTO.setSrCfgServiceIds(
            getServiceIds(srCatalogDTO.getSrToOutsideService(), srCataLogServiceIds));
        resultInSideDto = srConfigRepository.add(srConfigDTO);
        if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getOutsideServiceToSr())) {
          srConfigDTO.setConfigGroup(srCatalogDTO.getOutsideServiceToSr());
          srConfigDTO.setParentGroup("0");
          srConfigDTO.setCountry(srCatalogDTO.getCountry());
          srConfigDTO.setCreatedTime(DateUtil
              .dateToStringWithPattern(new Date(System.currentTimeMillis()),
                  "dd/MM/yyyy HH:mm:ss"));
          srConfigDTO.setSrCfgServiceIds(
              getServiceIds(srCatalogDTO.getOutsideServiceToSr(), srCataLogServiceIds));
          if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            resultInSideDto = srConfigRepository.add(srConfigDTO);
          }
        }
      }
    } else {
      //cập nhật
//      dung add
      srCatalogDTO.setServiceId(dto.getServiceId());
      srCatalogDTO.setApprove(dto.getApprove());
      srCatalogDTO.setCreatedTime(dto.getCreatedTime());
      srCatalogDTO.setCommentAuto(dto.getCommentAuto());
      srCatalogDTO.setAttachFile(dto.getAttachFile());
      srCatalogDTO.setIsInputChecking(dto.getIsInputChecking());
      srCatalogDTO.setIsOutputChecking(dto.getIsOutputChecking());
      srCatalogDTO.setLastDate(dto.getLastDate());
      srCatalogDTO.setCreatedUser(dto.getCreatedUser());
      srCatalogDTO.setServiceDescription(dto.getServiceDescription());
      if (dto.getRoleCode() != null && srCatalogDTO.getRoleCode() != null) {
        List<String> roleCodes = new ArrayList<>();
        List<String> autoCreateSRs = new ArrayList<>();

        String[] auto = dto.getAutoCreateSR().split(",");
        String[] role = dto.getRoleCode().split(",");
        Map<String, SRCatalogDTO> mapRole = new HashMap<>();
        if (auto != null && auto.length > 0) {
          if (auto.length == role.length) {
            for (int i = 0; i < auto.length; i++) {
              SRCatalogDTO autoRole = new SRCatalogDTO();
              autoRole.setAutoCreateSR(auto[i]);
              autoRole.setRoleCode(role[i]);
              if (!mapRole.containsKey(role[i])) {
                mapRole.put(role[i], autoRole);
              }
            }
          }
        }
        String[] roleNew = srCatalogDTO.getRoleCode().split(",");
        if (roleNew != null && roleNew.length > 0) {
          for (int i = 0; i < roleNew.length; i++) {
            if (mapRole.containsKey(roleNew[i])) {
              roleCodes.add(mapRole.get(roleNew[i]).getRoleCode());
              autoCreateSRs.add(mapRole.get(roleNew[i]).getAutoCreateSR());
            } else {
              roleCodes.add(roleNew[i]);
              autoCreateSRs.add("0");
            }
          }
        }
        srCatalogDTO.setRoleCode(String.join(",", roleCodes));
        srCatalogDTO.setAutoCreateSR(String.join(",", autoCreateSRs));
      }
// end
      srCatalogDTO.setUpdatedUser(userToken.getUserName());
      srCatalogDTO.setUpdatedTime(new Date(System.currentTimeMillis()));
      resultInSideDto = srCatalogRepository.add(srCatalogDTO);
      if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        srCatalogDTO.setIsUpdateAllUnit("0");
        srCatalogDTO.setSrConfig(srConfigRepository
            .getConfigGroupByConfigCode(srCatalogDTO.getServiceCode(), "A", "1",
                srCatalogDTO.getServiceId()));
        srCatalogDTO.setOutSideSrConfig(srConfigRepository
            .getConfigGroupByConfigCode(srCatalogDTO.getServiceCode(), "A", "0",
                srCatalogDTO.getServiceId()));
        setOutsideService(srCatalogDTO, userToken, resultInSideDto);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto importDataUnit(MultipartFile multipartFile,
      SRRoleUserInSideDTO srRoleUserDTO)
      throws Exception {
    List<UnitDTO> unitDTOS = new ArrayList<>();
    List<UnitDTO> unitDTOList = new ArrayList<>();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
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
        File fileImport = new File(filePath);

        List<Object[]> headerList;
        headerList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            4,
            0,
            1,
            1000
        );
        //Kiểm tra form header có đúng chuẩn
        if (headerList.size() == 0 || !validUnitFileFormat(headerList)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }
        //Lấy dữ liệu import
        List<Object[]> dataImportList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            5,
            0,
            1,
            1000
        );
        if (dataImportList.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          return resultInSideDto;
        }

        unitDTOS = new ArrayList<>();

        if (!dataImportList.isEmpty()) {
          int row = 4;
          int index = 0;
          for (Object[] obj : dataImportList) {
            UnitDTO unitDTO = new UnitDTO();
            if (obj[1] != null) {
              unitDTO.setUnitId(Long.valueOf(obj[1].toString().trim()));
              unitDTO.setUnitCode(unitDTO.getUnitId().toString());
            } else {
              unitDTO.setUnitId(null);
            }
            UnitDTO unitExportDTOTmp = validateUnitImportInfo(
                unitDTO, unitDTOList, srRoleUserDTO);

            if (unitExportDTOTmp.getResultImport() == null) {
              unitExportDTOTmp
                  .setResultImport(I18n.getLanguage("unit.result.import"));
              unitDTOList.add(unitExportDTOTmp);
              unitDTOS.add(unitDTO);
            } else {
              unitDTOList.add(unitExportDTOTmp);
              index++;
            }
            row++;
          }
          if (index == 0) {
            if (!unitDTOList.isEmpty()) {
              List<Long> lsUnitId = new ArrayList<>();
              for (UnitDTO dto : unitDTOList) {
                lsUnitId.add(dto.getUnitId());
              }
              SRRoleUserInSideDTO userDTO = new SRRoleUserInSideDTO();
              userDTO.setLstUnitId(lsUnitId);
              userDTO.setCountry(srRoleUserDTO.getCountry());
              resultInSideDto = getListSRRoleUserByUnitId(userDTO);
            }
          } else {
            File fileExport = exportUnitFileTemplate(unitDTOList, Constants.RESULT_IMPORT);
            resultInSideDto.setFilePath(fileExport.getPath());
            resultInSideDto.setFileName(fileExport.getName());
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportUnitFileTemplate(unitDTOList, Constants.RESULT_IMPORT);
          resultInSideDto.setFile(fileExport);
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

  private boolean validUnitFileFormat(List<Object[]> headerList) {
    Object[] objects = headerList.get(0);
    if (objects == null) {
      return false;
    }
    int count = 0;
    for (Object data : objects) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 2) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }
    if (!I18n.getLanguage("common.STT").equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("unit.unitCode") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    return true;
  }

  private UnitDTO validateUnitImportInfo(UnitDTO unitDTO,
      List<UnitDTO> list, SRRoleUserInSideDTO srRoleUserDTO) {
    if (StringUtils.isStringNullOrEmpty(unitDTO.getUnitId())) {
      unitDTO.setResultImport(I18n.getLanguage("unit.err.unitCode"));
      return unitDTO;
    }

    if (!(StringUtils.isStringNullOrEmpty(unitDTO.getUnitId()))
        && srRoleUserDTO.getCountry() != null) {
      srRoleUserDTO.setUnitId(unitDTO.getUnitId());
      SRRoleUserInSideDTO userDTO = srRoleUserRepository.checkRoleUserExistByUnitId(srRoleUserDTO);
      if (userDTO == null) {
        unitDTO.setResultImport(I18n.getLanguage("unit.err.unitCode.noConfig"));
        return unitDTO;
      }
    }
    if (list != null && list.size() > 0) {
      unitDTO = validateDuplicate(list, unitDTO);
    }

    return unitDTO;
  }

  public ResultInSideDto getListSRRoleUserByUnitId(SRRoleUserInSideDTO srRoleUserDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    log.debug("Request to getListSRRoleUser : {}", srRoleUserDTO);
    List<SRRoleUserInSideDTO> srRoleUserDTOList = new ArrayList<>();
    if (srRoleUserDTO.getLstUnitId() != null) {
      for (Long id : srRoleUserDTO.getLstUnitId()) {
        srRoleUserDTO.setUnitId(id);
        SRRoleUserInSideDTO userDTO = srRoleUserRepository
            .checkRoleUserExistByUnitId(srRoleUserDTO);
        if (userDTO == null) {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setObject(srRoleUserDTO);
          return resultInSideDto;
        }
        List<SRRoleUserInSideDTO> userDTOList = srRoleUserRepository
            .getListSRRoleUserByUnitId(srRoleUserDTO);
        if (userDTOList != null) {
          for (SRRoleUserInSideDTO dto : userDTOList) {
            srRoleUserDTOList.add(dto);
          }
        }
      }
    }
    resultInSideDto.setObject(srRoleUserDTOList);
    return resultInSideDto;
  }

  private UnitDTO validateDuplicate(List<UnitDTO> list,
      UnitDTO unitDTO) {
    for (int i = 0; i < list.size(); i++) {
      UnitDTO unitDTOTmp = list.get(i);
      if (unitDTOTmp.getUnitId().equals(unitDTO.getUnitId())) {
        unitDTO
            .setResultImport(I18n.getLanguage("srCatalog.err.dup-code-in-file")
                .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
    }
    return unitDTO;
  }

  private File exportUnitFileTemplate(List<UnitDTO> dtoList, String key)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("unit.export.title");
    String title = I18n.getLanguage("unit.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("unitCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    //kiểm tra đầu vào
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = UNIT_RESULT_IMPORT;
      subTitle = I18n
          .getLanguage("unit.export.exportDate", DateTimeUtils.convertDateOffset());
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
    } else {
      fileNameOut = UNIT_EXPORT;
      subTitle = I18n
          .getLanguage("unit.export.exportDate", DateTimeUtils.convertDateOffset());
    }
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        dtoList
        , sheetName
        , title
        , subTitle
        , 7
        , 3
        , 9
        , true
        , "language.unit"
        , headerExportList
        , fieldSplit
        , ""
        , I18n.getLanguage("common.export.firstLeftHeader")
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
    fileExportList.add(configFileExport);
    //Cấu hình đường dẫn
    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;
    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExportList
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  @Override
  public File getUnitTemplate() throws Exception {
    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    //apache POI XSSF
    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workbook.getSheetAt(0);
    XSSFSheet sheetParam = workbook.createSheet("param");
    //Tạo 1 mảng lưu header từng cột
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("unit.unitCode")
    };
    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("unit.unitCode")
    };

    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, 5));
    Row titleRow = sheetOne.createRow(2);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("unit.title"));
    titleCell.setCellStyle(style.get("title"));

    Row firstLeftHeaderRow = sheetOne.createRow(0);
    Cell firstLeftHeaderCell = firstLeftHeaderRow.createCell(1);
    firstLeftHeaderRow.setHeightInPoints(16);
    firstLeftHeaderCell.setCellValue(I18n.getLanguage("common.export.firstLeftHeader"));
    firstLeftHeaderCell.setCellStyle(style.get("indexTitle"));
    Row secondLeftHeaderRow = sheetOne.createRow(1);
    Cell secondLeftHeaderCell = secondLeftHeaderRow.createCell(1);
    secondLeftHeaderRow.setHeightInPoints(16);
    secondLeftHeaderCell.setCellValue(I18n.getLanguage("common.export.secondLeftHeader"));
    secondLeftHeaderCell.setCellStyle(style.get("indexTitle"));
    Cell firstRightHeaderCell = firstLeftHeaderRow.createCell(4);
    firstLeftHeaderRow.setHeightInPoints(16);
    firstRightHeaderCell.setCellValue(I18n.getLanguage("common.export.firstRightHeader"));
    firstRightHeaderCell.setCellStyle(style.get("indexTitle"));
    Cell secondRightHeaderCell = secondLeftHeaderRow.createCell(4);
    secondLeftHeaderRow.setHeightInPoints(16);
    secondRightHeaderCell.setCellValue(I18n.getLanguage("common.export.secondRightHeader"));
    secondRightHeaderCell.setCellStyle(style.get("indexTitle"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header

    Row headerRow = sheetOne.createRow(4);
    Row headerUnit = sheetParam.createRow(0);
    headerRow.setHeightInPoints(16);
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
    Cell headerCellStt = headerUnit.createCell(0);
    Cell headerCellUnitCode = headerUnit.createCell(1);
    Cell headerCellUnit = headerUnit.createCell(2);
    XSSFRichTextString stt = new XSSFRichTextString(I18n.getLanguage("common.STT"));
    XSSFRichTextString unitCode = new XSSFRichTextString(I18n.getLanguage("unit.unitCode"));
    XSSFRichTextString unit = new XSSFRichTextString(I18n.getLanguage("unit.unitName"));
    headerCellStt.setCellValue(stt);
    headerCellStt.setCellStyle(style.get("header"));
    headerCellUnitCode.setCellValue(unitCode);
    headerCellUnitCode.setCellStyle(style.get("header"));
    headerCellUnit.setCellValue(unit);
    headerCellUnit.setCellStyle(style.get("header"));

    sheetParam.setColumnWidth(1, 3000);
    sheetParam.setColumnWidth(2, 20000);
    sheetOne.setColumnWidth(0, 3000);

    // Set dữ liệu vào column dropdown

    int row = 1;
    List<UnitDTO> unitNameList = unitBusiness.getListUnit(null);
    for (UnitDTO dto : unitNameList) {
      excelWriterUtils
          .createCell(sheetParam, 1, row, dto.getUnitId().toString(), style.get("cell"));
      excelWriterUtils.createCell(sheetParam, 2, row++, dto.getUnitName(), style.get("cell"));
    }
    for (row = 1; row <= unitNameList.size(); row++) {
      excelWriterUtils.createCell(sheetParam, 0, row, String.valueOf(row), style.get("cell"));
    }
    sheetParam.autoSizeColumn(0);
    sheetParam.autoSizeColumn(1);

    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("unit.title"));
    workbook.setSheetName(1, I18n.getLanguage("srCatalog.unit"));
    sheetParam.setSelected(true);

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_UNIT" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  public void processFileAttachUpdate(List<MultipartFile> srFilesList, SRCatalogDTO srCatalogDTO,
      UserToken userToken, UnitDTO unitToken) throws Exception {
    //Start get file old
    List<Long> lstRootServiceId = new ArrayList<>();
    lstRootServiceId.add(srCatalogDTO.getServiceId());
    List<SRFilesDTO> lstFile = srCatalogRepository.findSrFilesByServiceIds(lstRootServiceId);
    List<Long> lstServiceId = new ArrayList<>();
    List<SRFilesDTO> lstFileCoppy = null;
    if ("0".equals(srCatalogDTO.getIsUpdateAllUnit())) {
      lstServiceId.add(srCatalogDTO.getServiceId());
    } else {
      List<SRCatalogEntity> srCatalogEntities = srCatalogRepository
          .findSRCatalogByServiceCode(srCatalogDTO.getServiceCode());
      if (srCatalogEntities != null) {
        lstServiceId.addAll(srCatalogEntities.stream().map(SRCatalogEntity::getServiceId)
            .collect(Collectors.toList()));
      }
      lstFileCoppy = new ArrayList<>();
      lstFileCoppy.addAll(lstFile);
      lstFile = srCatalogRepository.findSrFilesByServiceIds(lstServiceId);
    }

    //start get File Exist
    List<GnocFileDto> coGnocFileDtos = srCatalogDTO.getGnocFileDtos();
    List<Long> listIdFileIdNew = coGnocFileDtos.stream()
        .map(GnocFileDto::getMappingId).collect(Collectors.toList());

    //xu ly coppy file
    if (lstFileCoppy != null) {
      lstFileCoppy.removeIf(c -> (!listIdFileIdNew.contains(c.getFileId())));
    }
    if (lstFile != null) {
      lstFile.removeIf(c -> ((listIdFileIdNew.contains(c.getFileId()))));
      //|| (c.getObejctId() != null && !c.getObejctId().equals(srCatalogDTO.getServiceId()))); //ko remove file cua don vi khac
      for (SRFilesDTO item : lstFile) {
        srCatalogRepository.deleteSRFile(item.getFileId());
        gnocFileRepository
            .deleteGnocFileByMapping(GNOC_FILE_BUSSINESS.SR_CATALOG, item.getFileId());
      }
    }

    if (lstServiceId != null) {
      for (Long item : lstServiceId) {
        saveFileAttach(item, srFilesList, srCatalogDTO, userToken, unitToken);
        if (item != null && !item.equals(srCatalogDTO.getServiceId())) {
          coppyFile(lstFileCoppy, srCatalogDTO.getServiceId(), item);
        }
      }
    }

  }

  private void saveFileAttach(Long serviceId, List<MultipartFile> srFilesList,
      SRCatalogDTO srCatalogDTO, UserToken userToken, UnitDTO unitToken) throws Exception {
    List<String> fileNames = new ArrayList<>();
    List<SRFilesDTO> srFilesDTOS = new ArrayList<>();
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    for (GnocFileDto gnocFileDto : srCatalogDTO.getGnocFileDtos()) {
      if (gnocFileDto.getIndexFile() != null) {
        gnocFileDto.setMultipartFile(srFilesList.get(gnocFileDto.getIndexFile().intValue()));
      }
    }
    for (GnocFileDto gnocFileCatalogDto : srCatalogDTO.getGnocFileDtos()) {
      SRFilesDTO srFilesDTO = new SRFilesDTO();
      Date date = new Date();
      MultipartFile multipartFile = gnocFileCatalogDto.getMultipartFile();
      if (multipartFile != null) {
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                multipartFile.getBytes(), null);
        String fullPathOld = FileUtils
            .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                uploadFolder, null);
        String fileName = gnocFileCatalogDto.getMultipartFile().getOriginalFilename();
        fileNames.add(FileUtils.getFileName(fullPath));
        //Start save file old
        srFilesDTO.setFilePath(fullPathOld);
        srFilesDTO.setFileName(FileUtils.getFileName(fullPathOld));
        srFilesDTO.setFileType(gnocFileCatalogDto.getFileType());
        srFilesDTO.setRequireCreateSR(gnocFileCatalogDto.getRequired());
        srFilesDTO.setObejctId(null);
        srFilesDTO.setFileGroup(SR_CONFIG.FILE_GROUP_SC);
        srFilesDTOS.add(srFilesDTO);
        //End save file old
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(fileName);
        gnocFileDto.setCreateUnitId(userToken.getDeptId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(userToken.getUserID());
        gnocFileDto.setCreateUserName(userToken.getUserName());
        gnocFileDto.setCreateTime(date);
        gnocFileDto.setRequired(gnocFileCatalogDto.getRequired());
        gnocFileDto.setFileType(gnocFileCatalogDto.getFileType());
        gnocFileDto.setMappingId(null);
        gnocFileDtos.add(gnocFileDto);
      } else {
        gnocFileRepository.updateGnocFile(gnocFileCatalogDto);
        SRFilesDTO srFileUpdate = srCatalogRepository.findSRFile(gnocFileCatalogDto.getMappingId());
        if (srFileUpdate != null) {
          srFileUpdate.setRequireCreateSR(gnocFileCatalogDto.getRequired());
          srCatalogRepository.addSRFile(srFileUpdate);
        }
      }
    }
    List<GnocFileDto> gnocFileDtosAdd = new ArrayList<>();
    for (int i = 0; i < srFilesDTOS.size(); i++) {
      SRFilesDTO srFilesDTO = srFilesDTOS.get(i);
      srFilesDTO.setObejctId(serviceId);
      ResultInSideDto resultFileDataOld = srCatalogRepository.addSRFile(srFilesDTO);
      GnocFileDto gnocFileDto = gnocFileDtos.get(i);
      gnocFileDto.setMappingId(resultFileDataOld.getId());
      gnocFileDtosAdd.add(gnocFileDto);
    }
    gnocFileRepository.saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SR_CATALOG, serviceId,
        gnocFileDtosAdd);
  }

  private void coppyFile(List<SRFilesDTO> srFilesDTOS, Long serviceRootId, Long serviceId)
      throws CloneNotSupportedException {
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    GnocFileDto gnocFileDto = new GnocFileDto();
    gnocFileDto.setBusinessId(serviceRootId);
    gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.SR_CATALOG);
    List<GnocFileEntity> lstFileEntity = gnocFileRepository.getLstGnocFileByDto(gnocFileDto);
    if (lstFileEntity == null) {
      return;
    }

    if (srFilesDTOS != null) {
      for (SRFilesDTO itemTemp : srFilesDTOS) {
        SRFilesDTO item = itemTemp.clone();
        Long currentFileId = item.getFileId();
        item.setFileId(null);
        item.setObejctId(serviceId);
        item.setFileGroup(SR_CONFIG.FILE_GROUP_SC);
        ResultInSideDto resultInSideDto = srCatalogRepository.addSRFile(item);
        for (GnocFileEntity itemEntity : lstFileEntity) {
          if (currentFileId.equals(itemEntity.getMappingId())) {
            GnocFileDto fileDto = itemEntity.toDTO();
            fileDto.setId(null);
            fileDto.setBusinessId(serviceId);
            fileDto.setCreateTime(new Date());
            fileDto.setMappingId(resultInSideDto.getId());
            gnocFileDtos.add(fileDto);
          }
        }
      }
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SR_CATALOG, serviceId, gnocFileDtos);
    }
  }

  private String getServiceIds(String serviceIdOthers, String srCataLogServiceIds) {
    String[] arrServiceIds = serviceIdOthers.split(",");
    String srCfgServiceIds = "";
    for (int i = 0; i < arrServiceIds.length; i++) {
      srCfgServiceIds += "," + srCataLogServiceIds;
    }
    if (srCfgServiceIds.startsWith(",")) {
      srCfgServiceIds = srCfgServiceIds.substring(1);
    }
    return srCfgServiceIds;
  }

  private List<String> getServiceIdsForCurrentUnit(String serviceOutSideIdOthers,
      String serviceOutSideIdOthersNew, String currentServiceId, String srCataLogServiceIdsOld) {
    List<String> serviceOtherIdsOld = new ArrayList<>();
    List<String> serviceIdsOld = new ArrayList<>();
    List<String> serviceOtherIds = new ArrayList<>();
    List<String> serviceOtherIdsOldToDel = new ArrayList<>();
    if (!StringUtils.isStringNullOrEmpty(serviceOutSideIdOthers)) {
      serviceOtherIdsOld.addAll(Arrays.asList(serviceOutSideIdOthers.split(",")));
    }

    if (!StringUtils.isStringNullOrEmpty(serviceOutSideIdOthersNew)) {
      serviceOtherIds = Arrays.asList(serviceOutSideIdOthersNew.split(","));
    }

    if (!StringUtils.isStringNullOrEmpty(srCataLogServiceIdsOld)) {
      serviceIdsOld.addAll(Arrays.asList(srCataLogServiceIdsOld.split(",")));
    }

    if (StringUtils.isNotNullOrEmpty(serviceOutSideIdOthersNew)) {
      for (String item : serviceOtherIdsOld) {
        if (!("," + serviceOutSideIdOthersNew.trim() + ",").contains("," + item.trim() + ",")) {
          serviceOtherIdsOldToDel.add(item);
        }
      }
    }

    if (serviceOtherIds.size() > 0) {
      //xoa het file cu
      for (int i = 0; i < serviceOtherIds.size(); i++) {
        if (serviceOtherIdsOld.size() > 0) {
          for (int j = 0; j < serviceOtherIdsOldToDel.size(); j++) {
            int indexDel = serviceOtherIdsOld.indexOf(serviceOtherIdsOldToDel.get(j));
            if (indexDel > -1) {
              if (serviceIdsOld.size() > 0) {
                String serviceIdProcessDel = serviceIdsOld.get(indexDel);
                List<String> serviceIdCut = Arrays.asList(serviceIdProcessDel.split("-"));
                serviceIdProcessDel = "";
                for (String item : serviceIdCut) {
                  if (StringUtils.isNotNullOrEmpty(item) && !item.contains(currentServiceId)) {
                    serviceIdProcessDel += "-" + item;
                  }
                }
                if (serviceIdProcessDel.startsWith("-")) {
                  serviceIdProcessDel = serviceIdProcessDel.substring(1);
                }
                if (serviceIdsOld.size() > 0 && serviceIdsOld.size() == serviceOtherIdsOld.size()) {
                  serviceIdsOld.set(indexDel, serviceIdProcessDel);
                }
              }
            }
          }
        }
      }

      if (serviceIdsOld.size() < 1) {
        for (int j = 0; j < serviceOtherIdsOldToDel.size(); j++) {
          int indexDel = serviceOtherIdsOld.indexOf(serviceOtherIdsOldToDel.get(j));
          if (indexDel > -1) {
            serviceOtherIdsOld.remove(indexDel);
          }
        }
      } else {
        for (int j = serviceIdsOld.size() - 1; j >= 0; j--) {
          if (StringUtils.isStringNullOrEmpty(serviceIdsOld.get(j))) {
            serviceIdsOld.remove(j);
            serviceOtherIdsOld.remove(j);
          }
        }
      }

      for (int i = 0; i < serviceOtherIds.size(); i++) {
        int indexId = serviceOtherIdsOld.indexOf(serviceOtherIds.get(i).trim());
        if (indexId < 0) { //ko co trong list
          if (StringUtils
              .isStringNullOrEmpty(srCataLogServiceIdsOld)) {//trong DB chua co serviceIds
            serviceOtherIdsOld.add(serviceOtherIds.get(i));
            serviceIdsOld.add(currentServiceId);
          } else {
            serviceOtherIdsOld.add(serviceOtherIds.get(i));
            serviceIdsOld.add(currentServiceId);
          }
        } else {//neu ma other nay da co truoc do, thi cap nhat lai
          if (serviceIdsOld.size() > 0) {
            String serviceIdC = serviceIdsOld.get(indexId);
            //neu other nay truoc do chua co cho don vi nao
            if (StringUtils.isNotNullOrEmpty(serviceIdC)) {
              List<String> lstServiceIdc = Arrays.asList(serviceIdC.split("-"));
              if (!lstServiceIdc
                  .contains(currentServiceId)) {//neu other nay truoc do chua co don vi nay
                serviceIdC += "-" + currentServiceId;
              }
            } else {
              serviceIdC = currentServiceId;
            }
            serviceIdsOld.set(indexId, serviceIdC);
          } else {
            serviceIdsOld.add(currentServiceId);
          }
        }
      }
    } else {
      if (serviceIdsOld.size() > 0) {
        for (int i = serviceOtherIdsOld.size() - 1; i > -1; i--) {
          String serviceIdOld = serviceIdsOld.get(i);
          if (serviceIdOld == null) {
            serviceIdOld = "";
          }
          if (serviceIdOld.contains(currentServiceId)) {
            serviceIdOld = serviceIdOld.replaceAll("-" + currentServiceId + "-", "-")
                .replaceAll(currentServiceId + "-", "")
                .replaceAll("-" + currentServiceId, "")
                .replaceAll(currentServiceId, "");
          }
          if (StringUtils.isStringNullOrEmpty(serviceIdOld)) {
            serviceOtherIdsOld.remove(i);
            serviceIdsOld.remove(i);
          } else {
            serviceIdsOld.set(i, serviceIdOld);
          }
        }
      }
    }
    String serviceOtherNewIds = "";
    for (int i = 0; i < serviceOtherIdsOld.size(); i++) {
      serviceOtherNewIds += "," + serviceOtherIdsOld.get(i);
    }
    if (serviceOtherNewIds.startsWith(",")) {
      serviceOtherNewIds = serviceOtherNewIds.substring(1);
    }

    String serviceNewIds = "";
    for (int i = 0; i < serviceIdsOld.size(); i++) {
      serviceNewIds += "," + serviceIdsOld.get(i);
    }
    if (serviceNewIds.startsWith(",")) {
      serviceNewIds = serviceNewIds.substring(1);
    }

    List<String> arrReturn = new ArrayList<>();
    arrReturn.add(serviceOtherNewIds);
    arrReturn.add(serviceNewIds);
    return arrReturn;
  }

  private SRCatalogDTO convertListName(SRCatalogDTO srCatalogDTO) {
    try {
      if (srCatalogDTO.getGnocFileDtos() != null && srCatalogDTO.getGnocFileDtos().size() > 0) {
        List<String> gnocFileDtosName = new ArrayList<>();
        List<GnocFileDto> lstFile = srCatalogDTO.getGnocFileDtos();
        for (GnocFileDto file : lstFile) {
          gnocFileDtosName.add(file.getFileName());
        }
        srCatalogDTO.setGnocFileDtosName(gnocFileDtosName);
      }
      if (srCatalogDTO.getSrCatalogChildDTOList() != null && srCatalogDTO.getSrCatalogChildDTOList().size() > 0) {
        List<String> srCatalogChildDTOListName = new ArrayList<>();
        List<SRCatalogChildDTO> srCatalogChildDTOList = srCatalogDTO.getSrCatalogChildDTOList();
        for (SRCatalogChildDTO srCatalogChildDTO : srCatalogChildDTOList) {
          srCatalogChildDTOListName.add(srCatalogChildDTO.getServiceCode());
        }
        srCatalogDTO.setSrRoleUserDTOListName(srCatalogChildDTOListName);
      }
      if (srCatalogDTO.getSrRoleUserDTOList() != null  && srCatalogDTO.getSrRoleUserDTOList().size() > 0) {
        List<String> srRoleUserDTOListName = new ArrayList<>();
        List<SRRoleUserInSideDTO> srRoleUserDTOList = srCatalogDTO.getSrRoleUserDTOList();
        for (SRRoleUserInSideDTO srRoleUserInSideDTO : srRoleUserDTOList) {
          srRoleUserDTOListName.add(srRoleUserInSideDTO.getRoleCode());
        }
        srCatalogDTO.setSrRoleUserDTOListName(srRoleUserDTOListName);
      }
    } catch (Exception err){
      log.error(err.getMessage());
    }
    return srCatalogDTO;
  };

  //2020-10-03 hungtv add
  private List<String> getAllKeysDTO() {
    try {
      List<String> keys = new ArrayList<>();
      Field[] fields = SRCatalogDTO.class.getDeclaredFields();
      for (Field key : fields) {
        key.setAccessible(true);
        keys.add(key.getName());
      }
      List<String> rmKeys = Arrays.asList("gnocFileDtos", "srCatalogChildDTOList", "srRoleUserDTOList");
      for (String rmKey : rmKeys) {
        keys.remove(rmKey);
      }
      return keys;
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }
  //end
}
