package com.viettel.gnoc.risk.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.RiskCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.*;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.risk.dto.RiskCfgBusinessDTO;
import com.viettel.gnoc.risk.dto.RiskChangeStatusDTO;
import com.viettel.gnoc.risk.dto.RiskChangeStatusRoleDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDTO;
import com.viettel.gnoc.risk.repository.RiskCfgBusinessRepository;
import com.viettel.gnoc.risk.repository.RiskChangeStatusRepository;
import com.viettel.gnoc.risk.repository.RiskChangeStatusRoleRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class RiskChangeStatusBusinessImpl implements RiskChangeStatusBusiness {

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

  @Autowired
  RiskChangeStatusRepository riskChangeStatusRepository;

  @Autowired
  RiskCfgBusinessRepository riskCfgBusinessRepository;

  @Autowired
  RiskChangeStatusRoleRepository riskChangeStatusRoleRepository;

  @Autowired
  RiskCategoryServiceProxy riskCategoryServiceProxy;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Override
  public Datatable getDataRiskChangeStatusSearchWeb(RiskChangeStatusDTO riskChangeStatusDTO) {
    log.debug("Request to getDataRiskChangeStatusSearchWeb: {}", riskChangeStatusDTO);
    Datatable datatable = riskChangeStatusRepository
        .getDataRiskChangeStatusSearchWeb(riskChangeStatusDTO);
    List<RiskChangeStatusDTO> list = (List<RiskChangeStatusDTO>) datatable.getData();
    Collections.sort(list, new Comparator<RiskChangeStatusDTO>() {
      @Override
      public int compare(RiskChangeStatusDTO o1, RiskChangeStatusDTO o2) {
        if (o1.getRiskTypeName() == null && o2.getRiskTypeName() == null) {
          return 0;
        }
        if (o1.getRiskTypeName() == null) {
          return -1;
        }
        if (o2.getRiskTypeName() == null) {
          return 1;
        }
        return o1.getRiskTypeName().compareToIgnoreCase(o2.getRiskTypeName());
      }
    });
    datatable.setData(list);
    return datatable;
  }

  @Override
  public List<RiskChangeStatusDTO> getListRiskChangeStatusDTO(
      RiskChangeStatusDTO riskChangeStatusDTO) {
    log.debug("Request to getListRiskChangeStatusDTO: {}", riskChangeStatusDTO);
    if (riskChangeStatusDTO != null) {
      List<RiskChangeStatusDTO> list = riskChangeStatusRepository
          .onSearch(riskChangeStatusDTO, 0, Integer.MAX_VALUE, "", "");
      if (riskChangeStatusDTO.getIsSearch() != null && !riskChangeStatusDTO.getIsSearch() && (
          list == null || list.size() == 0)) {
        riskChangeStatusDTO.setIsDefault(1L);
        riskChangeStatusDTO.setRiskTypeId(null);
        list = riskChangeStatusRepository
            .onSearch(riskChangeStatusDTO, 0, Integer.MAX_VALUE, "", "");
      }
      return list;
    }
    return null;
  }

  @Override
  public List<RiskCfgBusinessDTO> getListRiskCfgBusinessDTO(RiskCfgBusinessDTO riskCfgBusinessDTO) {
    log.debug("Request to getListRiskCfgBusinessDTO: {}", riskCfgBusinessDTO);
    if (riskCfgBusinessDTO != null) {
      return riskCfgBusinessRepository.onSearch(riskCfgBusinessDTO, 0, Integer.MAX_VALUE, "", "");
    }
    return null;
  }

  @Override
  public List<RiskChangeStatusRoleDTO> getListRiskChangeStatusRoleDTO(
      RiskChangeStatusRoleDTO riskChangeStatusRoleDTO) {
    log.debug("Request to getListRiskChangeStatusRoleDTO: {}", riskChangeStatusRoleDTO);
    if (riskChangeStatusRoleDTO != null) {
      return riskChangeStatusRoleRepository
          .onSearch(riskChangeStatusRoleDTO, 0, Integer.MAX_VALUE, "", "");
    }
    return null;
  }

  @Override
  public ResultInSideDto insertOrUpdateRiskChangeStatus(RiskChangeStatusDTO riskChangeStatusDTO,
      List<MultipartFile> lstMultipartFile) throws IOException {
    log.debug("Request to insertOrUpdateRiskChangeStatus : {}", riskChangeStatusDTO);
    ResultInSideDto resultInSideDto;
    RiskChangeStatusDTO oldHis = new RiskChangeStatusDTO();
    Long id = riskChangeStatusDTO.getId();
    if (riskChangeStatusDTO.getId() != null) {
      oldHis = findRiskChangeStatusByIdFromWeb(riskChangeStatusDTO.getId());
    }
    resultInSideDto = riskChangeStatusRepository
        .insertOrUpdateRiskChangeStatus(riskChangeStatusDTO);
    if (id == null) {
      id = resultInSideDto.getId();
    } else {
      riskCfgBusinessRepository.deleteListRiskCfgBusiness(id);
      riskChangeStatusRoleRepository.deleteListRiskChangeStatusRole(id);
    }
    List<RiskCfgBusinessDTO> lstCfgBusiness = riskChangeStatusDTO.getLstCfgBusiness();
    if (lstCfgBusiness != null && lstCfgBusiness.size() > 0) {
      for (RiskCfgBusinessDTO i : lstCfgBusiness) {
        i.setRiskChangeStatusId(id);
        resultInSideDto = riskCfgBusinessRepository.insertRiskCfgBusiness(i);
      }
    }
    List<RiskChangeStatusRoleDTO> lstRole = riskChangeStatusDTO.getLstRole();
    if (lstRole != null && lstRole.size() > 0) {
      for (RiskChangeStatusRoleDTO i : lstRole) {
        i.setRiskChangeStatusId(id);
        resultInSideDto = riskChangeStatusRoleRepository.insertRiskChangeStatusRole(i);
      }
    }

    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      UserToken userToken = ticketProvider.getUserToken();
      UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
      for (GnocFileDto gnocFileDto : riskChangeStatusDTO.getGnocFileDtos()) {
        if (gnocFileDto.getIndexFile() != null) {
          gnocFileDto.setMultipartFile(lstMultipartFile.get(gnocFileDto.getIndexFile().intValue()));
        }
      }
      List<GnocFileDto> lstGnocFile = new ArrayList<>();
      for (GnocFileDto gnocFileDto1 : riskChangeStatusDTO.getGnocFileDtos()) {
        MultipartFile multipartFile = gnocFileDto1.getMultipartFile();
        if (multipartFile != null) {
          Date date = new Date();
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                  multipartFile.getBytes(), date);
          String fileName = gnocFileDto1.getFileName();
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(fileName);
          gnocFileDto.setFileType(gnocFileDto1.getFileType());
          gnocFileDto.setCreateUnitId(userToken.getDeptId());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(userToken.getUserID());
          gnocFileDto.setCreateUserName(userToken.getUserName());
          gnocFileDto.setCreateTime(new Date());
          gnocFileDto.setRequired(gnocFileDto1.getRequired());
          gnocFileDto.setMappingId(id);
          lstGnocFile.add(gnocFileDto);
        }
//        else if (gnocFileDto1.getId() != null) {
//          lstGnocFile.add(gnocFileDto1);
//        }
      }
      gnocFileRepository.deleteListGnocFile(GNOC_FILE_BUSSINESS.RISK_CONFIG, id,
          riskChangeStatusDTO.getIdDeleteList());
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.RISK_CONFIG, id,
              lstGnocFile);
      //Add history
      try {
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
        dataHistoryChange.setType("RISK_CFG_BUSINESS");
        dataHistoryChange.setOldObject(oldHis);
        dataHistoryChange.setActionType(riskChangeStatusDTO.getId() != null ? "update" : "add");
        //New Object History
        dataHistoryChange.setNewObject(riskChangeStatusDTO);
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
  public RiskChangeStatusDTO findRiskChangeStatusByIdFromWeb(Long id) {
    log.debug("Request to findRiskChangeStatusByIdFromWeb: {}", id);
    RiskChangeStatusDTO riskChangeStatusDTO = riskChangeStatusRepository
        .findRiskChangeStatusById(id);
    if (riskChangeStatusDTO != null) {
      riskChangeStatusDTO
          .setLstCfgBusiness(riskCfgBusinessRepository.getListCfgByRiskChangeStatusId(id));
      riskChangeStatusDTO
          .setLstRole(riskChangeStatusRoleRepository.getListRoleByRiskChangeStatusId(id));
      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.RISK_CONFIG);
      gnocFileDto.setBusinessId(id);
      riskChangeStatusDTO
          .setGnocFileDtos(gnocFileRepository.getListGnocFileByDto(gnocFileDto));
    }
    return riskChangeStatusDTO;
  }

  @Override
  public File exportDataRiskChangeStatus(RiskChangeStatusDTO riskChangeStatusDTO) throws Exception {
    log.debug("Request to exportDataRiskChangeStatus: {}", riskChangeStatusDTO);
    List<RiskChangeStatusDTO> list = riskChangeStatusRepository
        .getListRiskChangeStatusExport(riskChangeStatusDTO);
    if (list != null && list.size() > 0) {
      Collections.sort(list, new Comparator<RiskChangeStatusDTO>() {
        @Override
        public int compare(RiskChangeStatusDTO o1, RiskChangeStatusDTO o2) {
          if (o1.getRiskTypeName() == null && o2.getRiskTypeName() == null) {
            return 0;
          }
          if (o1.getRiskTypeName() == null) {
            return -1;
          }
          if (o2.getRiskTypeName() == null) {
            return 1;
          }
          return o1.getRiskTypeName().compareToIgnoreCase(o2.getRiskTypeName());
        }
      });
      for (RiskChangeStatusDTO dto : list) {
        List<RiskChangeStatusRoleDTO> listRole = riskChangeStatusRoleRepository
            .getListRoleByRiskChangeStatusId(dto.getId());
        if (listRole != null && listRole.size() > 0) {
          String changeStatusRole = "";
          for (RiskChangeStatusRoleDTO roleDTO : listRole) {
            changeStatusRole += roleDTO.getRoleName() + ";\r\n";
          }
          changeStatusRole = changeStatusRole.trim();
          if (changeStatusRole.length() > 0) {
            changeStatusRole = changeStatusRole.substring(0, changeStatusRole.lastIndexOf(";"));
          }
          dto.setChangeStatusRole(changeStatusRole);
        }
      }
    }
    String[] header = new String[]{"riskTypeName", "riskPriorityName", "oldStatusName",
        "newStatusName",
        "changeStatusRole"};
    Date date = DateTimeUtils.convertDateOffset();
    return handleFileExport(list, header, DateUtil.date2ddMMyyyyHHMMss(date));
  }

  @Override
  public List<RiskTypeDTO> getListRiskTypeDTOCombobox(RiskTypeDTO riskTypeDTO) {
    log.debug("Request to getListRiskTypeDTOCombobox: {}", riskTypeDTO);
    return riskCategoryServiceProxy.getListRiskTypeDTO(riskTypeDTO);
  }

  @Override
  public ResultInSideDto deleteRiskChangeStatus(Long id) {
    log.debug("Request to deleteRiskChangeStatus: {}", id);
    ResultInSideDto resultInSideDto;
    riskCfgBusinessRepository.deleteListRiskCfgBusiness(id);
    riskChangeStatusRoleRepository.deleteListRiskChangeStatusRole(id);
    RiskChangeStatusDTO oldHis = findRiskChangeStatusByIdFromWeb(id);
    resultInSideDto = riskChangeStatusRepository.deleteRiskChangeStatus(id);
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
        dataHistoryChange.setNewObject(new RiskChangeStatusDTO());
        dataHistoryChange.setType("RISK_CFG_BUSINESS");
        dataHistoryChange.setActionType("delete");
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  public File handleFileExport(List<RiskChangeStatusDTO> list, String[] columnExport, String date)
      throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = I18n.getLanguage("riskChangeStatus.export.sheetname");
    String title = I18n.getLanguage("riskChangeStatus.export.title");
    String fileNameOut = I18n.getLanguage("riskChangeStatus.export.fileNameOut");
    String headerPrefix = "language.riskChangeStatus";
    String firstLeftHeader = I18n.getLanguage("riskChangeStatus.export.firstLeftHeader");
    String secondLeftHeader = I18n.getLanguage("riskChangeStatus.export.secondLeftHeader");
    String firstRightHeader = I18n.getLanguage("riskChangeStatus.export.firstRightHeader");
    String secondRightHeader = I18n.getLanguage("riskChangeStatus.export.secondRightHeader");
    int mergeTitleEndIndex = 6;
    int startRow = 7;
    int cellTitleIndex = 3;
    String subTitle = I18n.getLanguage("riskChangeStatus.export.exportDate", date);
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
        I18n.getLanguage("riskChangeStatus.STT"), "HEAD", "STRING");
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

  //2020-10-03 hungtv add
  private List<String> getAllKeysDTO () {
    try {
      List<String> keys = new ArrayList<>();
      Field[] fields = RiskChangeStatusDTO.class.getDeclaredFields();
      for (Field key : fields) {
        key.setAccessible(true);
        keys.add(key.getName());
      }
      if (keys != null && !keys.isEmpty()) {
        List<String> rmKeys = Arrays.asList("riskTypeName", "oldStatusName", "newStatusName", "isDefaultName", "riskPriorityName",
            "isSearch", "changeStatusRole", "idDeleteList");
        for (String rmKey : rmKeys) {
          keys.remove(rmKey);
        }
        return keys;
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }
  //end
}
