package com.viettel.gnoc.cr.business;

import com.viettel.aam.ModuleInfo;
import com.viettel.aam.MopFileResult;
import com.viettel.aam.MopInfo;
import com.viettel.aam.MopResult;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CfgRoleDataDTO;
import com.viettel.gnoc.commons.dto.CrModuleDraftDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.incident.provider.WSSPMProvidePort;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.CfgRoleDataRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CFG_ROLE_DATA_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.CR_ACTION_RIGHT;
import com.viettel.gnoc.commons.utils.Constants.CR_FILE_ATTACH;
import com.viettel.gnoc.commons.utils.Constants.CR_FILE_TYPE;
import com.viettel.gnoc.commons.utils.Constants.CR_STATE;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.cr.dto.AttachDtDTO;
import com.viettel.gnoc.cr.dto.CrCabUsersDTO;
import com.viettel.gnoc.cr.dto.CrFileObjectInsite;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachResultDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.TempImportDataDTO;
import com.viettel.gnoc.cr.dto.TemplateImportDTO;
import com.viettel.gnoc.cr.repository.CrDtRepositoryImpl;
import com.viettel.gnoc.cr.repository.CrFilesAttachRepository;
import com.viettel.gnoc.cr.repository.CrGeneralRepository;
import com.viettel.gnoc.cr.repository.CrProcessRepository;
import com.viettel.gnoc.cr.repository.CrRepository;
import com.viettel.gnoc.cr.repository.TemplateImportRepository;
import com.viettel.gnoc.cr.util.CrConfig;
import com.viettel.gnoc.cr.util.CrProcessFromClient;
import com.viettel.gnoc.ws.provider.WSTDTTPort;
import com.viettel.gnoc.ws.provider.WSVipaDdPort;
import com.viettel.gnoc.ws.provider.WSVipaIpPort;
import com.viettel.security.PassTranformer;
import com.viettel.service.PhoneNumberForm;
import com.viettel.service.ValidateTraceForm;
import com.viettel.vipa.MopDetailOutputDTO;
import com.viettel.vmsa.FileValidateDTO;
import com.viettel.vmsa.MopOutputDTO;
import com.viettel.vmsa.ResultFileValidateDTO;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.extern.slf4j.Slf4j;
import org.apache.xml.security.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;


@Service
@Transactional
@Slf4j
public class CrFileAttachBusinessImpl implements CrFileAttachBusiness {

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
  CrProcessRepository crProcessRepository;

  @Autowired
  CrFilesAttachRepository crFilesAttachRepository;

  @Autowired
  CrDtRepositoryImpl crDtRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  TemplateImportRepository templateImportRepository;

  @Autowired
  WSVipaDdPort vipaDdPort;

  @Autowired
  WSSPMProvidePort provideport;

  @Autowired
  WSTDTTPort wstdttPort;

  @Autowired
  WSVipaIpPort wsVipaIpPort;

  @Autowired
  CrProcessFromClient crProcessFromClient;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  CfgRoleDataRepository cfgRoleDataRepository;

  @Autowired
  CrRepository crRepository;

  @Autowired
  CrGeneralRepository crGeneralRepository;

  @Override
  public List<CrFilesAttachInsiteDTO> getListCrFilesSearch(
      CrFilesAttachInsiteDTO crFilesAttachDTO) {
    List<CrFilesAttachInsiteDTO> temp = new ArrayList<>();
    List<CrFilesAttachInsiteDTO> lst = crFilesAttachRepository
        .getListCrFilesSearch(crFilesAttachDTO);
    try {
      if (lst != null && !lst.isEmpty()) {
        for (CrFilesAttachInsiteDTO cr : lst) {
          cr.setFilePath(cr.getFilePath() != null ? PassProtector
              .encrypt(cr.getFilePath(), String.valueOf(cr.getCrId())) : cr.getFilePath());
          temp.add(cr);
        }
        lst = temp;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
    return lst;
  }

  @Override
  public ResultInSideDto getListCrFilesSearchCheckRole(CrFilesAttachInsiteDTO crFilesAttachDTO) {
    //truongnt nang cap check role
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, null);
    CrInsiteDTO crInsiteDTO = crRepository.findCrById(crFilesAttachDTO.getCrId());
    CfgRoleDataDTO objectSearch = new CfgRoleDataDTO();
    objectSearch.setUsername(userToken.getUserName());
    objectSearch.setSystem(CFG_ROLE_DATA_SYSTEM.CR);
    objectSearch.setStatus(1L);
    CfgRoleDataDTO cfgRoleDataDTO = cfgRoleDataRepository.getConfigByDto(objectSearch);
    if (crInsiteDTO != null) {
      if (cfgRoleDataDTO != null) {
        if (cfgRoleDataDTO.getRole() == 0L || cfgRoleDataDTO.getRole() == 1L) {
          resultInSideDto.setObject(getListFilesSearchDataTable(crFilesAttachDTO));
        } else {
          boolean check = false;
          List<String> lstUnit = Arrays.asList(cfgRoleDataDTO.getAuditUnitId().split(","));
          List<String> lstUnitChild = new ArrayList<>();
          for (String unitId : lstUnit) {
            List<UnitDTO> lst = unitRepository.getListUnitChildren(Long.valueOf(unitId));
            if (lst != null && !lst.isEmpty()) {
              lstUnitChild
                  .addAll(lst.stream().map(unitDTO -> String.valueOf(unitDTO.getUnitId())).collect(
                      Collectors.toList()));
            }
          }
          if ((StringUtils.isNotNullOrEmpty(crInsiteDTO.getChangeResponsibleUnit()) && lstUnitChild
              .contains(crInsiteDTO.getChangeResponsibleUnit())) || (
              StringUtils.isNotNullOrEmpty(crInsiteDTO.getChangeOrginatorUnit()) && lstUnitChild
                  .contains(crInsiteDTO.getChangeOrginatorUnit()))) {
            check = true;
          }

          List<UnitDTO> lstUnitUserLogin = unitRepository
              .getListUnitChildren(userToken.getDeptId());
          List<Long> lstUnitId = lstUnitUserLogin.stream().map(unitDTO -> unitDTO.getUnitId())
              .collect(
                  Collectors.toList());
          if (lstUnitUserLogin != null && !lstUnitUserLogin.isEmpty()) {
            try {
              if ((StringUtils.isNotNullOrEmpty(crInsiteDTO.getChangeOrginatorUnit()) && lstUnitId
                  .contains(Long.valueOf(crInsiteDTO.getChangeOrginatorUnit()))) ||
                  (StringUtils.isNotNullOrEmpty(crInsiteDTO.getChangeResponsibleUnit()) && lstUnitId
                      .contains(Long.valueOf(crInsiteDTO.getChangeResponsibleUnit()))) ||
                  (StringUtils.isNotNullOrEmpty(crInsiteDTO.getConsiderUnitId()) && lstUnitId
                      .contains(Long.valueOf(crInsiteDTO.getConsiderUnitId())))) {
                check = true;
              }
            } catch (NumberFormatException e) {
              log.error(e.getMessage(), e);
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto.setMessage(I18n.getLanguage("cr.error.permissionViewFile"));
            }
          }
          List<CrCabUsersDTO> lstUserCab = null;
          if (String.valueOf(CR_STATE.CAB).equals(crFilesAttachDTO.getActionRight())) {
            lstUserCab = crGeneralRepository.getListUserCab(null);
          } else {
            lstUserCab = crGeneralRepository.getListUserCab(crInsiteDTO.getImpactSegment());
          }
          if (lstUserCab != null && !lstUserCab.isEmpty()) {
            List<Long> lstUserId = lstUserCab.stream().map(userCab -> userCab.getUserID())
                .collect(Collectors.toList());
            if (lstUserId.contains(userToken.getUserID())) {
              check = true;
            }
          }
          if (check) {
            resultInSideDto.setObject(getListFilesSearchDataTable(crFilesAttachDTO));
          } else {
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(I18n.getLanguage("cr.error.permissionViewFile"));
          }
        }
      } else {
        boolean check = false;
        List<UnitDTO> lstUnit = unitRepository.getListUnitChildren(userToken.getDeptId());
        List<Long> lstUnitId = lstUnit.stream().map(unitDTO -> unitDTO.getUnitId()).collect(
            Collectors.toList());
        if (lstUnit != null && !lstUnit.isEmpty()) {
          try {
            if ((StringUtils.isNotNullOrEmpty(crInsiteDTO.getChangeOrginatorUnit()) && lstUnitId
                .contains(Long.valueOf(crInsiteDTO.getChangeOrginatorUnit()))) ||
                (StringUtils.isNotNullOrEmpty(crInsiteDTO.getChangeResponsibleUnit()) && lstUnitId
                    .contains(Long.valueOf(crInsiteDTO.getChangeResponsibleUnit()))) ||
                (StringUtils.isNotNullOrEmpty(crInsiteDTO.getConsiderUnitId()) && lstUnitId
                    .contains(Long.valueOf(crInsiteDTO.getConsiderUnitId())))) {
              check = true;
            }
          } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(I18n.getLanguage("cr.error.permissionViewFile"));
          }
        }
        List<CrCabUsersDTO> lstUserCab = null;
        if (String.valueOf(CR_STATE.CAB).equals(crFilesAttachDTO.getActionRight())) {
          lstUserCab = crGeneralRepository.getListUserCab(null);
        } else {
          lstUserCab = crGeneralRepository.getListUserCab(crInsiteDTO.getImpactSegment());
        }
        if (lstUserCab != null && !lstUserCab.isEmpty()) {
          List<Long> lstUserId = lstUserCab.stream().map(userCab -> userCab.getUserID())
              .collect(Collectors.toList());
          if (lstUserId.contains(userToken.getUserID())) {
            check = true;
          }
        }
        if (check) {
          resultInSideDto.setObject(getListFilesSearchDataTable(crFilesAttachDTO));
        } else {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("cr.error.permissionViewFile"));
        }
      }
    } else {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getLanguage("cr.error.permissionViewFile"));
    }
    return resultInSideDto;
  }

  @Override
  public Datatable getListFilesSearchDataTable(CrFilesAttachInsiteDTO crFilesAttachDTO) {
    return crFilesAttachRepository.getListFilesSearchDataTable(crFilesAttachDTO);
  }

  @Override
  public ResultInSideDto insertList(
      CrFilesAttachInsiteDTO crFilesAttachInsiteDTO,
      List<MultipartFile> lstMutilKPI, List<MultipartFile> lstMutilDT,
      List<MultipartFile> lstMutilTest, List<MultipartFile> lstMutilRoll,
      List<MultipartFile> lstMutilPlant, List<MultipartFile> lstMutilImpactScenario,
      List<MultipartFile> lstMutilForm, List<MultipartFile> lstMutilFile,
      List<MultipartFile> lstMutilTxt, List<MultipartFile> lstMutilFileOther,
      List<MultipartFile> lstMutilProcess, List<MultipartFile> lstMultilLogImpact)
      throws Exception {
    ResultInSideDto result = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    if (userToken != null && StringUtils
        .isNotNullOrEmpty(crFilesAttachInsiteDTO.getProxyLocale())) {
      userToken.setTelephone(crFilesAttachInsiteDTO.getProxyLocale());
    }
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    //xu ly file form
    ResultInSideDto resultInSideDto = updoadTemplateFiles(lstMutilProcess, crFilesAttachInsiteDTO,
        userToken, unitToken);
    if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      return resultInSideDto;
    } else {
      result.setKey(RESULT.SUCCESS);
      result.setMessage(I18n.getChangeManagement("cr.msg.import.success"));
    }
    //them moi xu ly voi cac file loadFileDT
    if (crFilesAttachInsiteDTO.getLstCustomerFile() != null
        && crFilesAttachInsiteDTO.getLstCustomerFile().size() > 0) {
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      for (CrFilesAttachInsiteDTO crFilesAttachInsiteDTODT : crFilesAttachInsiteDTO
          .getLstCustomerFile()) {
        Date timeCreated = new Date();
        byte[] bytes = FileUtils
            .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), crFilesAttachInsiteDTODT.getFilePath());
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, crFilesAttachInsiteDTODT.getFileName(),
                bytes, timeCreated);

        //start save file old
        String fullPathOld = FileUtils.saveUploadFile(crFilesAttachInsiteDTODT.getFileName(),
            bytes, uploadFolder, timeCreated);
        String dtFileHistory = crFilesAttachInsiteDTODT.getDtFileHistory();
        if ("1".equals(dtFileHistory)) {
          crFilesAttachInsiteDTODT.setDtFileHistory("VIPA_IP_INSERT");
        } else if ("2".equals(dtFileHistory)) {
          crFilesAttachInsiteDTODT.setDtFileHistory("VIPA_DD_INSERT");
        } else if (StringUtils.isNotNullOrEmpty(dtFileHistory)) {
          crFilesAttachInsiteDTODT.setDtFileHistory(I18n.getChangeManagement("qltn.insertFile"));
        } else {
          crFilesAttachInsiteDTODT.setDtFileHistory(null);
        }
        crFilesAttachInsiteDTODT.setFilePath(fullPathOld);
        crFilesAttachInsiteDTODT.setFileName(FileUtils.getFileName(fullPathOld));
        crFilesAttachInsiteDTODT.setUserId(userToken.getUserID());
        crFilesAttachInsiteDTODT.setCrId(crFilesAttachInsiteDTO.getCrId());
        crFilesAttachInsiteDTODT
            .setTimeAttack(DateTimeUtils.convertStringToDate(DateTimeUtils.getSysDateTime()));
        ResultInSideDto resultFileDataOld = crFilesAttachRepository.add(crFilesAttachInsiteDTODT);
        //end save file old
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(crFilesAttachInsiteDTODT.getFileName());
        gnocFileDto.setCreateUnitId(userToken.getDeptId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(userToken.getUserID());
        gnocFileDto.setCreateUserName(userToken.getUserName());
        gnocFileDto.setCreateTime(timeCreated);
        gnocFileDto.setMappingId(resultFileDataOld.getId());
        gnocFileDtos.add(gnocFileDto);
      }
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.CR, crFilesAttachInsiteDTO.getCrId(),
              gnocFileDtos);
    }
    //xoa file
    if (crFilesAttachInsiteDTO.getLstRemoveFileAttach() != null
        && crFilesAttachInsiteDTO.getLstRemoveFileAttach().size() > 0) {
      crFilesAttachRepository
          .deleteListAttachByListId(crFilesAttachInsiteDTO.getLstRemoveFileAttach(),
              crFilesAttachInsiteDTO.getCrId());

    }
    saveMultipleUploadedFile(lstMutilKPI, CR_FILE_ATTACH.FILE_KPI, crFilesAttachInsiteDTO,
        userToken, unitToken);
    saveMultipleUploadedFile(lstMutilDT, CR_FILE_ATTACH.FILE_DT, crFilesAttachInsiteDTO, userToken,
        unitToken);
    saveMultipleUploadedFile(lstMutilTest, CR_FILE_ATTACH.FILE_TEST, crFilesAttachInsiteDTO,
        userToken, unitToken);
    saveMultipleUploadedFile(lstMutilRoll, CR_FILE_ATTACH.FILE_ROLL, crFilesAttachInsiteDTO,
        userToken, unitToken);
    saveMultipleUploadedFile(lstMutilPlant, CR_FILE_ATTACH.FILE_PLANT, crFilesAttachInsiteDTO,
        userToken, unitToken);
    saveMultipleUploadedFile(lstMutilImpactScenario, CR_FILE_ATTACH.FILE_IMPACT,
        crFilesAttachInsiteDTO, userToken, unitToken);
    saveMultipleUploadedFile(lstMutilForm, CR_FILE_ATTACH.FILE_FORM, crFilesAttachInsiteDTO,
        userToken, unitToken);
    saveMultipleUploadedFile(lstMutilFile, CR_FILE_ATTACH.FILE_MUTIL_FILE, crFilesAttachInsiteDTO,
        userToken, unitToken);
    saveMultipleUploadedFile(lstMutilTxt, CR_FILE_ATTACH.FILE_TXT, crFilesAttachInsiteDTO,
        userToken, unitToken);
    saveMultipleUploadedFile(lstMutilFileOther, CR_FILE_ATTACH.FILE_OTHER, crFilesAttachInsiteDTO,
        userToken, unitToken);
    saveMultipleUploadedFile(lstMultilLogImpact, CR_FILE_ATTACH.LOG_IMPACT, crFilesAttachInsiteDTO,
        userToken, unitToken);
    return result;
  }

  public void saveMultipleUploadedFile(List<MultipartFile> files,
      String type, CrFilesAttachInsiteDTO crFilesAttachInsiteDTO, UserToken userToken,
      UnitDTO unitToken)
      throws Exception {
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    for (int i = 0; i < files.size(); i++) {
      Date timeCreated = new Date();
      MultipartFile multipartFile = files.get(i);
      String fullPath = FileUtils
          .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
              multipartFile.getBytes(), timeCreated);
      String fileName = files.get(i).getOriginalFilename();
      //Start save file old
      String fullPathOld = FileUtils
          .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
              uploadFolder, timeCreated);
      CrFilesAttachInsiteDTO crFilesAttachInsiteDTOAdd = new CrFilesAttachInsiteDTO();
      crFilesAttachInsiteDTOAdd.setFileName(FileUtils.getFileName(fullPathOld));
      crFilesAttachInsiteDTOAdd.setUserId(userToken.getUserID());
      crFilesAttachInsiteDTOAdd.setCrId(crFilesAttachInsiteDTO.getCrId());
      crFilesAttachInsiteDTOAdd
          .setTimeAttack(DateTimeUtils.convertStringToDate(DateTimeUtils.getSysDateTime()));
      crFilesAttachInsiteDTOAdd.setFileType(type);
      crFilesAttachInsiteDTOAdd.setFilePath(fullPathOld);
      if (CR_FILE_TYPE.IMPORT_BY_PROCESS_IN.equals(type)) {
        if (!StringUtils
            .isStringNullOrEmpty(
                crFilesAttachInsiteDTO.getCrFileObjects().get(i).getIdTemplate())) {
          crFilesAttachInsiteDTOAdd.setTempImportId(
              Long.valueOf(crFilesAttachInsiteDTO.getCrFileObjects().get(i).getIdTemplate()));
        }
      } else if (CR_FILE_TYPE.IMPORT_BY_PROCESS_OUT.equals(type)) {
        if (!StringUtils
            .isStringNullOrEmpty(
                crFilesAttachInsiteDTO.getCrFileObjects().get(i).getIdTemplate())) {
          crFilesAttachInsiteDTOAdd.setTempImportId(
              Long.valueOf(crFilesAttachInsiteDTO.getCrFileObjects().get(i).getIdTemplate()));
        }
      }
      ResultInSideDto resultFileDataOld = crFilesAttachRepository.add(crFilesAttachInsiteDTOAdd);
      //End save file old
      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setPath(fullPath);
      gnocFileDto.setFileName(fileName);
      gnocFileDto.setFileType(type);
      gnocFileDto.setTemplateId(crFilesAttachInsiteDTOAdd.getTempImportId());
      gnocFileDto.setCreateUnitId(userToken.getDeptId());
      gnocFileDto.setCreateUnitName(unitToken.getUnitName());
      gnocFileDto.setCreateUserId(userToken.getUserID());
      gnocFileDto.setCreateUserName(userToken.getUserName());
      gnocFileDto.setCreateTime(timeCreated);
      gnocFileDto.setMappingId(resultFileDataOld.getId());
      gnocFileDtos.add(gnocFileDto);
    }
    gnocFileRepository
        .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.CR, crFilesAttachInsiteDTO.getCrId(),
            gnocFileDtos);
  }

  @Override
  public List<AttachDtDTO> loadDtFromTDTT(String userName, String crProcess, String lstWork) {
    List<AttachDtDTO> lstAttachDtDTOs = new ArrayList<AttachDtDTO>();
    try {
      List<MopInfo> lstMopInfoAll = new ArrayList<>();
      Map<String, MopInfo> mapCheckDup = new HashMap<>();

//      List<CrProcessInsideDTO> lstCrPrcess = crProcessRepository
//          .findCrProcessIdByTDTT(lstWork);
//      List<String> lstId = new ArrayList<>();
//      for (CrProcessInsideDTO item : lstCrPrcess) {
//        if (item.getRequireMop() != null && item.getRequireMop() == 1L) {
//          lstId.add(item.getCrProcessId().toString());
//        }
//      }
//      String strId = split(lstId);
      MopResult mopResult = wstdttPort.getListMop(userName, crProcess, lstWork);
      if (!StringUtils.isStringNullOrEmpty(mopResult)) {
        List<MopInfo> lstMopInfo = mopResult.getMopInfos();
        Integer status = mopResult.getStatus();
        if (lstMopInfo != null && lstMopInfo.size() > 0) {
          lstMopInfo.forEach(c -> {
            mapCheckDup.put(String.valueOf(c.getCode()), c);
          });
        }
      }
      for (Map.Entry<String, MopInfo> entry : mapCheckDup.entrySet()) {
        lstMopInfoAll.add(entry.getValue());
      }

      for (com.viettel.aam.MopInfo info : lstMopInfoAll) {
        String dtCode = info.getCode();
        String dtname = info.getName();
        XMLGregorianCalendar d = info.getCreatedDate();
        Date createDate = new Date(d.getYear() - 1900, d.getMonth() - 1, d.getDay(), d.getHour(),
            d.getMinute(), d.getSecond());
        SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        List<String> lstIpImpact = info.getIps();
        List<String> lstAffectService = info.getAffectServices();
        List<String> lstIpAffect = info.getAffectIps();
        AttachDtDTO temp = new AttachDtDTO();
        temp.setDtCode(dtCode);
        temp.setUserName(userName);
        temp.setFileName(dtname);
        temp.setCreateDate(spd.format(createDate));
        temp.setLstAffectService(lstAffectService);
        temp.setLstIpAffect(lstIpAffect);
        temp.setLstIpImpact(lstIpImpact);
        if (info.getModuleInfos() != null && !info.getModuleInfos().isEmpty()) {
          temp.setLstModule(info.getModuleInfos());
        }
        temp.setNationCode(info.getNationCode());
        lstAttachDtDTOs.add(temp);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstAttachDtDTOs;
  }

  @Override
  public List<AttachDtDTO> loadDtFromVIPA(String userName, String system, String crProcess,
      String lstWork) {
    List<AttachDtDTO> lstAttachDtDTOs = new ArrayList<>();
    try {
      //Nếu system == 1
      if ("1".equals(system)) {
        Map<String, com.viettel.vipa.MopDTO> mapCheckDup = new HashMap<>();
        List<com.viettel.vipa.MopDTO> lstMopInfoAll = new ArrayList<>();
//        List<CrProcessInsideDTO> lstCrPrcess = crProcessRepository
//            .findCrProcessIdByTDTT(lstWork);
//        List<String> lstId = new ArrayList<>();
//        for (CrProcessInsideDTO item : lstCrPrcess) {
////          if (item.getRequireMop() != null && item.getRequireMop() == 1L) {
//            lstId.add(item.getCrProcessId().toString());
////          }
//        }
//        String strId = split(lstId);
        com.viettel.vipa.MopOutputDTO mopResult = wsVipaIpPort
            .getMopByUser(userName, crProcess, lstWork);
        if (!StringUtils.isStringNullOrEmpty(mopResult)) {
          List<com.viettel.vipa.MopDTO> lstMopInfo = mopResult.getMops();
          if (lstMopInfo != null && lstMopInfo.size() > 0) {
            lstMopInfo.forEach(c -> {
              mapCheckDup.put(String.valueOf(c.getMopId()), c);
            });
          }
        }
        for (Map.Entry<String, com.viettel.vipa.MopDTO> entry : mapCheckDup.entrySet()) {
          lstMopInfoAll.add(entry.getValue());
        }
        for (com.viettel.vipa.MopDTO info : lstMopInfoAll) {
          String dtCode = info.getMopId();
          String dtname = info.getMopName();
          String createDate = info.getCreateTime();
          AttachDtDTO temp = new AttachDtDTO();
          temp.setDtCode(dtCode);
          temp.setUserName(userName);
          temp.setFileName(dtname);
          temp.setCreateDate(createDate);
          lstAttachDtDTOs.add(temp);
        }
//        Nếu system == 2
      } else if ("2".equals(system)) {
        Map<String, com.viettel.vmsa.MopDTO> mapVmsaCheckDup = new HashMap<>();
        List<com.viettel.vmsa.MopDTO> lstMopInfoAll = new ArrayList<>();
//        List<CrProcessInsideDTO> lstCrPrcess = crProcessRepository
//            .findCrProcessIdByTDTT(lstWork);
//        List<String> lstId = new ArrayList<>();
//        for (CrProcessInsideDTO item : lstCrPrcess) {
////          if (item.getRequireMop() != null && item.getRequireMop() == 1L) {
//            lstId.add(item.getCrProcessId().toString());
////          }
//        }
//        String strId = split(lstId);
        com.viettel.vmsa.MopOutputDTO mopResult = vipaDdPort
            .getMopByUser(userName, crProcess, lstWork);
        if (!StringUtils.isStringNullOrEmpty(mopResult)) {
          List<com.viettel.vmsa.MopDTO> lstMopInfo = mopResult.getMops();
          if (lstMopInfo != null && lstMopInfo.size() > 0) {
            lstMopInfo.forEach(c -> {
              mapVmsaCheckDup.put(String.valueOf(c.getMopId()), c);
            });
          }
        }
        for (Map.Entry<String, com.viettel.vmsa.MopDTO> entry : mapVmsaCheckDup.entrySet()) {
          lstMopInfoAll.add(entry.getValue());
        }
        for (com.viettel.vmsa.MopDTO info : lstMopInfoAll) {
          String dtCode = info.getMopId();
          String dtname = info.getMopName();
          String createDate = info.getCreateTime();
          AttachDtDTO temp = new AttachDtDTO();
          temp.setDtCode(dtCode);
          temp.setUserName(userName);
          temp.setFileName(dtname);
          temp.setCreateDate(createDate);
          lstAttachDtDTOs.add(temp);
        }
      }
      return lstAttachDtDTOs;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public MopDetailOutputDTO getMopInfo(String dtCode) {
    MopDetailOutputDTO vipaIpMop = null;
    try {
      vipaIpMop = wsVipaIpPort.getMopInfo(dtCode);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return vipaIpMop;
  }

  @Override
  public List<InfraDeviceDTO> getNetworkNodeTDTTV2(List<String> lstIp, String nationCode) {
    List<InfraDeviceDTO> lstData = crDtRepository.geInfraDeviceByIps(lstIp, nationCode);
    return lstData;
  }

  @Override
  public MopFileResult getMopFile(String dtCode, String crNumber) {
    try {
      MopFileResult mopResult = wstdttPort.getMopFile(dtCode, crNumber);
      return mopResult;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public CrFilesAttachInsiteDTO findFileAttachById(Long id) {
    return crFilesAttachRepository.findFileAttachById(id);
  }

  @Override
  public List<CrFilesAttachInsiteDTO> getListCrFilesAttachDTO(
      CrFilesAttachInsiteDTO crFilesAttachDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return crFilesAttachRepository
        .getListCrFilesAttachDTO(crFilesAttachDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<CrFileObjectInsite> getListTemplateFileByProcess(String crProcessId,
      String actionRight) {
    String type = "";
    List<CrFileObjectInsite> lst = new ArrayList<>();
    List<CrFileObjectInsite> lstAll = new ArrayList<>();
    try {
      if (CR_ACTION_RIGHT.CAN_EDIT.equalsIgnoreCase(actionRight)) {
        type = CR_FILE_TYPE.IMPORT_BY_PROCESS_IN;
      } else if (Constants.CR_ACTION_RIGHT.CAN_RESOLVE.equalsIgnoreCase(actionRight)) {
        type = Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_OUT;
      } else {
        return lst;
      }
      Map<String, CrFileObjectInsite> mapProcess = new HashMap<>();
      if (crProcessId != null && !StringUtils.isStringNullOrEmpty(crProcessId)) {
        lst = crFilesAttachRepository.getListTemplateFileByProcess(crProcessId, type);
        if (lst != null) {
          for (CrFileObjectInsite item : lst) {
            if (mapProcess.get(item.getIdTemplate()) == null) {
              mapProcess.put(item.getIdTemplate(), item);
              lstAll.add(item);
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstAll;
  }

  public ResultInSideDto updoadTemplateFiles(List<MultipartFile> lstfiles,
      CrFilesAttachInsiteDTO crFilesAttachDTO, UserToken userToken, UnitDTO unitToken)
      throws Exception {
    Map<String, MultipartFile> mapCrFile = new HashMap<>();
    String[] paths;
    if (!StringUtils.isStringNullOrEmpty(crFilesAttachDTO.getCrFileObjects())) {
      int checkRow = 0;
      for (CrFileObjectInsite item : crFilesAttachDTO.getCrFileObjects()) {
        if (item.getLinkTemplate().contains("/")) {
          paths = item.getLinkTemplate().split("/");
        } else {
          paths = item.getLinkTemplate().split(Pattern.quote(File.separator));
        }
        item.setNameTemplate(paths[paths.length - 1]);
        if (lstfiles != null) {
          MultipartFile mFile = lstfiles.get(item.getIndexFile());
          if (mFile != null) {
            mapCrFile.put(item.getCrProcessId() + "_" + String.valueOf(checkRow++), mFile);
          }
        }
      }
    }
    ResultInSideDto result = new ResultInSideDto();
    result.setKey(RESULT.SUCCESS);
    int check = 0;
    if (crFilesAttachDTO.getCrFileObjects() != null && !crFilesAttachDTO.getCrFileObjects()
        .isEmpty()) {
      boolean isRemove = true;
      int checkRow = 0;
      for (CrFileObjectInsite crProcess : crFilesAttachDTO.getCrFileObjects()) {
        List<CrFilesAttachInsiteDTO> listAttachFilesDTO = new ArrayList<>();
        List<GnocFileDto> gnocFileDtos = new ArrayList<>();
        List<MultipartFile> files = new ArrayList<>();
        files.add(mapCrFile.get(crProcess.getCrProcessId() + "_" + String.valueOf(checkRow)));
        CrProcessInsideDTO processDTO = crProcessRepository
            .findCrProcessById(Long.valueOf(crProcess.getCrProcessId()));
        result.setKey(RESULT.SUCCESS);
        String type;
        if ("6".equals(crFilesAttachDTO.getStateCr())) {
          type = CR_FILE_TYPE.IMPORT_BY_PROCESS_OUT;
        } else {
          type = CR_FILE_TYPE.IMPORT_BY_PROCESS_IN;
        }
        for (int i = 0; i < files.size(); i++) {
          MultipartFile multipartFile = files.get(i);
          Date timeCreated = new Date();
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                  multipartFile.getBytes(), timeCreated);
//          String fileName = files.get(i).getOriginalFilename();
          //Start save file old
          String fullPathOld = FileUtils
              .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                  uploadFolder, timeCreated);
          CrFilesAttachInsiteDTO crFilesAttachInsiteDTOAdd = new CrFilesAttachInsiteDTO();
          crFilesAttachInsiteDTOAdd.setFileName(FileUtils.getFileName(fullPathOld));
          crFilesAttachInsiteDTOAdd.setUserId(userToken.getUserID());
          crFilesAttachInsiteDTOAdd.setCrId(crFilesAttachDTO.getCrId());
          crFilesAttachInsiteDTOAdd
              .setTimeAttack(DateTimeUtils.convertStringToDate(DateTimeUtils.getSysDateTime()));
          crFilesAttachInsiteDTOAdd.setFileType(type);
          crFilesAttachInsiteDTOAdd.setFilePath(fullPathOld);
          crFilesAttachInsiteDTOAdd.setFilePathFtp(fullPath);
          if (CR_FILE_TYPE.IMPORT_BY_PROCESS_IN.equals(type)) {
            if (!StringUtils
                .isStringNullOrEmpty(
                    crFilesAttachDTO.getCrFileObjects().get(checkRow).getIdTemplate())) {
              crFilesAttachInsiteDTOAdd.setTempImportId(
                  Long.valueOf(crFilesAttachDTO.getCrFileObjects().get(checkRow).getIdTemplate()));
            }
          } else if (CR_FILE_TYPE.IMPORT_BY_PROCESS_OUT.equals(type)) {
            if (!StringUtils
                .isStringNullOrEmpty(
                    crFilesAttachDTO.getCrFileObjects().get(checkRow).getIdTemplate())) {
              crFilesAttachInsiteDTOAdd.setTempImportId(
                  Long.valueOf(crFilesAttachDTO.getCrFileObjects().get(checkRow).getIdTemplate()));
            }
          }
          checkRow++;
          listAttachFilesDTO.add(crFilesAttachInsiteDTOAdd);
          //End save file old
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
          gnocFileDto.setFileType(type);
          gnocFileDto.setTemplateId(crFilesAttachInsiteDTOAdd.getTempImportId());
          gnocFileDto.setCreateUnitId(userToken.getDeptId());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(userToken.getUserID());
          gnocFileDto.setCreateUserName(userToken.getUserName());
          gnocFileDto.setCreateTime(timeCreated);
          gnocFileDto.setMappingId(null);
          gnocFileDtos.add(gnocFileDto);
        }
        List<TemplateImportDTO> resultTemp = new ArrayList<>();
        if (listAttachFilesDTO.size() < files.size()) {
          result.setKey(RESULT.ERROR);
          result.setMessage(I18n.getChangeManagement("cr.msg.must.be.attach.all"));
        } else {
          for (int i = listAttachFilesDTO.size() - 1; i >= 0; i--) {
            CrFilesAttachInsiteDTO fileImport = listAttachFilesDTO.get(i);
            if ("1500".equals(String.valueOf(fileImport.getTempImportId()))) {
              TemplateImportDTO rs = validateFile(fileImport, userToken, unitToken);
              resultTemp.add(rs);
              listAttachFilesDTO.remove(i);
            }
          }
          if (processDTO == null || !"1"
              .equals(String.valueOf(processDTO.getIsVmsaActiveCellProcess()))) {
            //Thực hiện validate file dối với loại quy trình thường
            List<TemplateImportDTO> rs2 = insertListNoIDForImport(listAttachFilesDTO, userToken,
                unitToken, isRemove);
            if (rs2 != null) {
              resultTemp.addAll(rs2);
            }
            String retError = "";//
            String pathFile = "";
            for (TemplateImportDTO lstTempImport1 : resultTemp) {
              if (!Constants.CR_RETURN_MESSAGE.SUCCESS.equals(lstTempImport1.getResult())) {
                if (lstTempImport1.getLinkOutput() == null || ""
                    .equals(lstTempImport1.getLinkOutput())) {
                  check = 2;
                  if (lstTempImport1.getMessage() != null && !""
                      .equals(lstTempImport1.getMessage())) {
                    retError += lstTempImport1.getMessage() + "\r\n";
                  }
                } else {
                  check = 1;
                  pathFile = lstTempImport1.getLinkOutput();
                }
              }
            }
            switch (check) {
              case 1:
                result.setMessage(
                    I18n.getChangeManagement("cr.msg.import.unsuccess") + ": " + retError);
                result.setKey(RESULT.ERROR);
                result.setFilePath(pathFile);
                return result;
              case 2:
                result.setMessage(
                    I18n.getChangeManagement("cr.msg.import.unsuccess1") + ": " + retError);
                result.setKey(RESULT.ERROR);
                result.setFilePath(pathFile);
                return result;
            }
          } else {
            //Thực hiện gọi sang VMSA để Validate file và tạo MOP
            if (StringUtils.isStringNullOrEmpty(processDTO.getVmsaActiveCellProcessKey())) {
              result.setKey(RESULT.ERROR);
              result.setMessage(I18n.getChangeManagement("not.found.vmsa.key"));
              return result;
            }
            String username = userToken.getUserName();
            List<FileValidateDTO> fileList = new ArrayList<>();
            for (CrFilesAttachInsiteDTO attactment : listAttachFilesDTO) {
              FileValidateDTO attact = new FileValidateDTO();
              if (attactment.getFileName() == null || !attactment.getFileName().trim()
                  .endsWith(".xlsx")) {
                result.setKey(RESULT.ERROR);
                result.setMessage(
                    I18n.getChangeManagement("xlsx.file.requeire")
                        .concat(attactment.getFileName()));
                return result;
              }

              attact.setFileName(attactment.getFileName());
              File file = new File(attactment.getFilePath());
              if (!file.exists()) {
                result.setKey(RESULT.ERROR);
                result.setMessage(
                    I18n.getChangeManagement("not.found.file.msg")
                        .concat(attactment.getFileName()));
                return result;
              }
              byte[] bytes = FileUtils.convertFileToByte(file);
              String fileContent = Base64.encode(bytes);
              attact.setFileContent(fileContent);
              attact.setFileType("xlsx");
              fileList.add(attact);
            }
            String crNumber = genCrNumber(crFilesAttachDTO.getCrType(),
                crFilesAttachDTO.getCrId().toString(), crFilesAttachDTO.getImpactSegment());
            ResultFileValidateDTO fileValidateDTO = new ResultFileValidateDTO();
            fileValidateDTO.setCrId(crFilesAttachDTO.getCrId());
            fileValidateDTO.setCrNumber(crNumber);
            fileValidateDTO.getFileValidateDTOs().addAll(fileList);
            ResultFileValidateDTO validateDTO = vipaDdPort
                .validateFiles(crFilesAttachDTO.getCrId(), crNumber, username,
                    processDTO.getVmsaActiveCellProcessKey().trim(), fileValidateDTO);
            if (validateDTO == null) {
              result.setKey(RESULT.ERROR);
              result.setMessage(I18n.getChangeManagement("cr.vipa.errCommunicate"));
              return result;
            }
            List<CrFilesAttachInsiteDTO> lstAttachment = new ArrayList<>();
            if (validateDTO.getResultCode() == CrConfig.VMSA_SUCCESS_KEY) {
              crFilesAttachRepository
                  .deleteCrFilesAttachByCrIdAndType(crFilesAttachDTO.getCrId().toString(),
                      Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_IN);
              gnocFileRepository.deleteGnocFile(GNOC_FILE_BUSSINESS.CR, crFilesAttachDTO.getCrId());
              for (CrFilesAttachInsiteDTO attactment : listAttachFilesDTO) {
                CrFilesAttachInsiteDTO inserFile = new CrFilesAttachInsiteDTO();
                inserFile.setCrId(crFilesAttachDTO.getCrId());
                inserFile.setFileType(Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_IN);
                inserFile.setFileName(attactment.getFileName());
                inserFile.setFilePath(attactment.getFilePath());
                inserFile.setUserId(userToken.getUserID());
                inserFile.setFileSize(attactment.getFileSize());
                inserFile.setTempImportId(attactment.getTempImportId());
                inserFile
                    .setTimeAttack(
                        DateTimeUtils.convertStringToDate(DateTimeUtils.getSysDateTime()));
                lstAttachment.add(inserFile);
              }
              result.setValidateKey(
                  validateDTO.getDtCreateId() != null ? String.valueOf(validateDTO.getDtCreateId())
                      : null);
              result.setProcessId(processDTO.getCrProcessId().toString());
              if (lstAttachment != null && !lstAttachment.isEmpty()) {
                //Save file
                List<GnocFileDto> gnocFileDtosAdd = new ArrayList<>();
                for (CrFilesAttachInsiteDTO crFilesAttachInsiteDTOAdd : lstAttachment) {
                  ResultInSideDto resultFileDataOld = crFilesAttachRepository
                      .add(crFilesAttachInsiteDTOAdd);
                  for (GnocFileDto gnocFileDto : gnocFileDtos) {
                    if (gnocFileDto.getPath().equals(crFilesAttachInsiteDTOAdd.getFilePath())) {
                      gnocFileDto.setMappingId(resultFileDataOld.getId());
                      gnocFileDtosAdd.add(gnocFileDto);
                      break;
                    }
                  }
                }
                gnocFileRepository.saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.CR,
                    crFilesAttachDTO.getCrId(), gnocFileDtosAdd);
                return result;
              }
            } else {
              if (validateDTO.getFileValidateDTOs() != null && !validateDTO.getFileValidateDTOs()
                  .isEmpty()) {
                List<File> filesZip = new ArrayList<>();
                for (FileValidateDTO validateFileOutput : validateDTO.getFileValidateDTOs()) {
                  byte[] fileContent = Base64.decode(validateFileOutput.getFileContent());
                  String fullPath = FileUtils
                      .saveTempFile(validateFileOutput.getFileName(), fileContent, tempFolder);
                  filesZip.add(new File(fullPath));
                }
                String zipFileName =
                    crFilesAttachDTO.getCrId() + "_" + crNumber + "_" + System.currentTimeMillis()
                        + ".zip";
                String fullPathZip = FileUtils.saveTempFileZip(filesZip, zipFileName, tempFolder);
                result.setFileName(FileUtils.getFileName(fullPathZip));
                result.setFilePath(fullPathZip);
              }
              result.setKey(RESULT.ERROR);
              result.setMessage(validateDTO.getResultMessage());
              return result;
            }
          }
        }
        isRemove = false;
      }
      if (check == 0) {
        result.setMessage(I18n.getChangeManagement("cr.msg.import.success"));
        result.setKey(RESULT.SUCCESS);
      }
    }
    return result;
  }

  public TemplateImportDTO validateFile(CrFilesAttachInsiteDTO fileImport, UserToken userToken,
      UnitDTO unitToken)
      throws Exception {
    TemplateImportDTO result = new TemplateImportDTO();
    List<PhoneNumberForm> lstPhone = getLstPhone(fileImport);
    ValidateTraceForm valid = provideport.validateUserPhoneNumber(lstPhone);

    if ("00".equals(valid.getErrorCode())) {
      result.setResult(Constants.CR_RETURN_MESSAGE.SUCCESS);
      List<TempImportDataDTO> lst = new ArrayList<>();
      for (int i = 0; i < valid.getLstPhoneNumber().size(); i++) {
        PhoneNumberForm form = valid.getLstPhoneNumber().get(i);
        TempImportDataDTO temp = new TempImportDataDTO();
        temp.setTempImportId(fileImport.getTempImportId().toString());
        temp.setCrId(fileImport.getCrId().toString());
        temp.setTempImportValue(form.getPhoneNumber());
        temp.setRowOrder("" + i);
        lst.add(temp);
      }
      TempImportDataDTO con = new TempImportDataDTO();
      con.setCrId(fileImport.getCrId().toString());

      List<TempImportDataDTO> lstDelete = crFilesAttachRepository.getListTempImportDataDTO(con);
      if (lstDelete != null && !lstDelete.isEmpty()) {
        crFilesAttachRepository.deleteListTempImportDataDTO(lstDelete);
      }
      crFilesAttachRepository.insertTempImportDataDTO(lst);

      //Xoa file cu
      CrFilesAttachInsiteDTO conFile = new CrFilesAttachInsiteDTO();
      conFile.setCrId(fileImport.getCrId());
      List<CrFilesAttachDTO> lstFile = crFilesAttachRepository
          .getListFileImportByProcess(conFile);
      if (lstFile != null) {
        for (CrFilesAttachDTO dto : lstFile) {
          if ("1500".equals(dto.getTempImportId())) {
            crFilesAttachRepository.deleteCrFilesAttach(Long.valueOf(dto.getFileId()));
            gnocFileRepository
                .deleteGnocFileByMapping(GNOC_FILE_BUSSINESS.CR, Long.valueOf(dto.getFileId()));
          }
        }
      }
      //insert file attach
      if (!StringUtils.isStringNullOrEmpty(fileImport)) {
        ResultInSideDto rsTemp = crFilesAttachRepository.add(fileImport);
        List<GnocFileDto> lstGnocFileDtos = new ArrayList<>();
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fileImport.getFilePathFtp());
        gnocFileDto.setFileName(fileImport.getFileName());
        gnocFileDto.setFileType(fileImport.getFileType());
        gnocFileDto.setTemplateId(fileImport.getTempImportId());
        try {
          gnocFileDto.setCreateUnitId(userToken.getDeptId());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(userToken.getUserID());
          gnocFileDto.setCreateUserName(userToken.getUserName());
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        gnocFileDto.setCreateTime(new Date());
        gnocFileDto.setMappingId(rsTemp.getId());
        lstGnocFileDtos.add(gnocFileDto);
        gnocFileRepository.saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.CR,
            fileImport.getCrId(), lstGnocFileDtos);
      }
    }
    return result;
  }

  private String genCrNumber(String crTypeLong, String crId, String impactSegment) {
    String crType = Constants.CR_TYPE.NORMAL.toString().equalsIgnoreCase(crTypeLong) ? "NORMAL"
        : Constants.CR_TYPE.EMERGENCY.toString().equalsIgnoreCase(crTypeLong) ? "EMERGENCY"
            : Constants.CR_TYPE.STANDARD.toString().equalsIgnoreCase(crTypeLong) ? "STANDARD" : "";
    String crNumber = "CR_" + crType + "_" + impactSegment + "_" + crId;
    return crNumber.toUpperCase(Locale.US);
  }

  public List<TemplateImportDTO> insertListNoIDForImport(List<CrFilesAttachInsiteDTO> lstUpload,
      UserToken userToken, UnitDTO unitToken, boolean isRemove) {
    log.info("Request to insertListNoIDForImport : {}", lstUpload, userToken, unitToken, isRemove);
    try {
      List<CrFilesAttachInsiteDTO> lstUpdateInsert =
          crFilesAttachRepository.getListCrFilesToUpdateOrInsertForList(lstUpload, true, isRemove);
      List<TemplateImportDTO> lstTempImport = new ArrayList<>();
      String userName = userToken.getUserName();
      UsersInsideDto usersInsideDto = userRepository.getUserDTOByUserName(userName);
      String userId = null;
      List<UsersInsideDto> lst = crFilesAttachRepository.actionGetUserByUserName(userName);
      if (lst != null && !lst.isEmpty()) {
        userId = lst.get(0).getUserId().toString();
      }
      if (lstUpdateInsert != null && !lstUpdateInsert.isEmpty()) {
        for (CrFilesAttachInsiteDTO lstUpdateInsert1 : lstUpdateInsert) {
          TemplateImportDTO tidto = new TemplateImportDTO();
          tidto.setCrId(lstUpdateInsert1.getCrId().toString());
          tidto.setLinkInput(lstUpdateInsert1.getFilePath());
          tidto.setLinkInputFtp(lstUpdateInsert1.getFilePathFtp());
          tidto.setTempImportId(lstUpdateInsert1.getTempImportId() == null ? null
              : lstUpdateInsert1.getTempImportId().toString());
          tidto.setFileName(lstUpdateInsert1.getFileName());
          tidto.setUserId(userId);
          tidto.setFileType(lstUpdateInsert1.getFileType());
          lstTempImport.add(tidto);
        }
      }
      //Validate && insert temp_import_data
      List<TemplateImportDTO> result =
          actionImportFile(lstTempImport, I18n.getLocale(),
              userToken.getTelephone(), isRemove);
      boolean check = true;
      for (TemplateImportDTO lstTempImport1 : result) {
        if (!Constants.CR_RETURN_MESSAGE.SUCCESS.equals(lstTempImport1.getResult())) {
          check = false;
        }
      }
      log.info("\n CHECK LƯU FILE PROCESS CR: \n" + check);
      if (check) {
        if (lst != null && !lst.isEmpty() && lstUpdateInsert != null && !lstUpdateInsert
            .isEmpty()) {
          List<GnocFileDto> gnocFileDtos = new ArrayList<>();
          for (CrFilesAttachInsiteDTO crFilesAttachInsiteDTOAdd : lstUpdateInsert) {
            crFilesAttachInsiteDTOAdd.setFileId(null);
            ResultInSideDto resultFileDataOld = crFilesAttachRepository
                .add(crFilesAttachInsiteDTOAdd);
            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setPath(crFilesAttachInsiteDTOAdd.getFilePathFtp());
            gnocFileDto.setFileName(crFilesAttachInsiteDTOAdd.getFileName());
            gnocFileDto.setFileType(crFilesAttachInsiteDTOAdd.getFileType());
            gnocFileDto.setTemplateId(crFilesAttachInsiteDTOAdd.getTempImportId());
            gnocFileDto.setCreateUnitId(usersInsideDto.getUnitId());
            gnocFileDto.setCreateUnitName(unitToken.getUnitName());
            gnocFileDto.setCreateUserId(usersInsideDto.getUserId());
            gnocFileDto.setCreateUserName(usersInsideDto.getUsername());
            gnocFileDto.setCreateTime(new Date());
            gnocFileDto.setMappingId(resultFileDataOld.getId());
            gnocFileDtos.add(gnocFileDto);
          }
          gnocFileRepository.saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.CR,
              lstUpdateInsert.get(0).getCrId(), gnocFileDtos);
        }
      }
      log.info("END request to insertListNoIDForImport : {}");
      return result;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      log.info("END request to insertListNoIDForImport : {}");
      return null;
    }
  }

  @Override
  public List<TemplateImportDTO> insertListNoIDForImportForSR(
      List<CrFilesAttachInsiteDTO> lstUpload, UserToken userToken, UnitDTO unitToken,
      boolean isRemove) {
    log.info("Request to insertListNoIDForImportForSR : {}", lstUpload, userToken, unitToken,
        isRemove);
    try {
      if (lstUpload != null && !lstUpload.isEmpty()) {
        for (CrFilesAttachInsiteDTO crFilesAttachInsiteDTO : lstUpload) {
          if (StringUtils.isNotNullOrEmpty(crFilesAttachInsiteDTO.getFilePathFtp())) {
            byte[] bytes = FileUtils
                .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), crFilesAttachInsiteDTO.getFilePathFtp());
            String filePath = FileUtils
                .saveTempFile(FileUtils.getFileName(crFilesAttachInsiteDTO.getFilePathFtp()), bytes,
                    tempFolder);
            crFilesAttachInsiteDTO.setFilePath(filePath);
          }
        }
      }
      List<TemplateImportDTO> lstResult = insertListNoIDForImport(lstUpload, userToken, unitToken,
          isRemove);
      if (lstResult != null && !lstResult.isEmpty()) {
        for (TemplateImportDTO templateImportDTO : lstResult) {
          if (StringUtils.isNotNullOrEmpty(templateImportDTO.getLinkOutput())) {
            File file = new File(templateImportDTO.getLinkOutput());
            if (file != null) {
              templateImportDTO.setBytesFileOut(FileUtils.convertFileToByte(file));
            }
          }
        }
      }
      return lstResult;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      log.info("END request to insertListNoIDForImport : {}");
      return null;
    }
  }

  public List<TemplateImportDTO> actionImportFile(
      List<TemplateImportDTO> listTemplateImportDTOs,
      String slocale,
      String countryId, boolean isRemove
  ) {
    String locale = slocale;
    if (locale == null || "".equals(locale.trim())) {
      locale = "vi_VN";
    }
    try {
      boolean isValidateOK = true;
      ExcelWriterUtils ewu = new ExcelWriterUtils();
      for (TemplateImportDTO templateImportDTO : listTemplateImportDTOs) {
        templateImportDTO.setLinkOutput(null);
        templateImportDTO.setMessage(null);
        templateImportDTO = templateImportRepository.validateTempImport(ewu,
            templateImportDTO,
            locale, countryId);
        if (templateImportDTO.getResult().equals(Constants.CR_RETURN_MESSAGE.NOK)) {
          isValidateOK = false;
        }
      }
      log.info("isValidateOK :" + isValidateOK);
      if (isValidateOK) {
        for (TemplateImportDTO templateImportDTO : listTemplateImportDTOs) {
          templateImportRepository.actionClearData(templateImportDTO);
          if (isRemove) {
            templateImportRepository.actionClearFileAttInput(templateImportDTO);
          }
          templateImportRepository.
              insertIntoTempImport(ewu,
                  templateImportDTO,
                  locale);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      for (TemplateImportDTO templateImportDTO : listTemplateImportDTOs) {
        templateImportDTO.setMessage(Constants.CR_RETURN_MESSAGE.NOK);
        templateImportDTO.setResult(I18n.getChangeManagement("cr.Error.spc", locale));
      }
    }
    return listTemplateImportDTOs;
  }

  public List<PhoneNumberForm> getLstPhone(CrFilesAttachInsiteDTO fileImport) {
    List<PhoneNumberForm> lstResult = new ArrayList<PhoneNumberForm>();
    List<Object[]> lstDataAll = new ArrayList<Object[]>();
    try {
      lstDataAll = CommonImport.readExcel(new File(fileImport.getFilePath()), 0, 0,
          0, 1, 2);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (lstDataAll != null && !lstDataAll.isEmpty()) {
      for (int i = 0; i < lstDataAll.size(); i++) {
        Object[] obj = lstDataAll.get(i);
        if (i == 0) {
          if (obj[0] == null
              || !I18n.getChangeManagement("cr.trace.importTitle")
              .equalsIgnoreCase(obj[0].toString().trim())) {
            I18n.getChangeManagement("cr.trace.importTitleInvalid");
            return null;
          }
        } else if (i == 4) {
          if (obj[0] == null
              || !I18n.getChangeManagement("cr.trace.importHeader")
              .equalsIgnoreCase(obj[0].toString().trim())) {
            I18n.getChangeManagement("cr.trace.importHeaderInvalid");
            return null;
          }
        } else if (i > 4) {
          if (obj[0] != null) {
            PhoneNumberForm form = new PhoneNumberForm();
            form.setPhoneNumber(obj[0].toString());
            lstResult.add(form);
            if (lstResult.size() > 50) {
              I18n.getChangeManagement("cr.trace.importMax");
              return null;
            }
          }
        }
      }
    } else {
      I18n.getChangeManagement("cr.trace.importFaile");
    }
    return lstResult;
  }

  @Override
  public String insertListNoID(List<CrFilesAttachInsiteDTO> lstUpload) {
    List<CrFilesAttachInsiteDTO> lstUpdateInsert = crFilesAttachRepository
        .getListCrFilesToUpdateOrInsert(lstUpload, false);
    if (lstUpdateInsert != null && !lstUpdateInsert.isEmpty()) {
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      Long userId = lstUpdateInsert.get(0).getUserId();
      UsersEntity usersEntity = new UsersEntity();
      UnitDTO unitToken = new UnitDTO();
      if (userId != null) {
        usersEntity = userRepository.getUserByUserId(userId);
        unitToken = unitRepository.findUnitById(usersEntity.getUnitId());
      }
      for (CrFilesAttachInsiteDTO crFilesAttachInsiteDTO : lstUpdateInsert) {
        ResultInSideDto resultFileDataOld = crFilesAttachRepository.add(crFilesAttachInsiteDTO);
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(crFilesAttachInsiteDTO.getFilePathFtp());
        gnocFileDto.setFileName(crFilesAttachInsiteDTO.getFileName());
        gnocFileDto.setFileType(crFilesAttachInsiteDTO.getFileType());
        gnocFileDto.setTemplateId(crFilesAttachInsiteDTO.getTempImportId());
        gnocFileDto.setCreateUnitId(usersEntity.getUnitId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(usersEntity.getUserId());
        gnocFileDto.setCreateUserName(usersEntity.getUsername());
        gnocFileDto.setCreateTime(new Date());
        gnocFileDto.setMappingId(resultFileDataOld.getId());
        gnocFileDtos.add(gnocFileDto);
      }
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.CR, lstUpdateInsert.get(0).getCrId(),
              gnocFileDtos);
    }
    return RESULT.SUCCESS;
  }

  @Override
  public ResultInSideDto selectDTFile(List<AttachDtDTO> lstAttachDtSelected, String system,
      String crNumber, String crId) {
    ResultInSideDto result = new ResultInSideDto();
    Map<String, Object> mapData = new HashMap<>();
    if (lstAttachDtSelected.isEmpty()) {
      result.setKey("FAIL");
      result.setMessage(I18n.getChangeManagement("cr.tdtt.choseOne"));
      return result;
    }
    int count = 0;
    //Neu la vipa thi goi lai ham lay IP va file
    List<InfraDeviceDTO> lstNodeAffectAll = new ArrayList<>();
    List<InfraDeviceDTO> lstNodeImpactAll = new ArrayList<>();
    List<ModuleInfo> lstModule = new ArrayList<>();
    try {
      com.viettel.vipa.MopDetailOutputDTO vipaIpMop = null;
      com.viettel.vmsa.MopDetailOutputDTO vipaDdMop = null;
      for (AttachDtDTO attachDtSelected : lstAttachDtSelected) {
        String item = system;

        String nationCode = attachDtSelected.getNationCode();
        if (item != null && !"0".equals(item)) {

          if ("1".equals(item)) {
            attachDtSelected.setSystemCode("1");
            vipaIpMop = wsVipaIpPort.getMopInfo(attachDtSelected.getDtCode());

            if (vipaIpMop != null && vipaIpMop.getResultCode() == 0) {
              if (vipaIpMop.getMopDetailDTO() == null) {
                result.setMessage(
                    I18n.getChangeManagement("Not found MopInfo ") + attachDtSelected.getDtCode());
                result.setKey("FAIL");
                return result;
              }
              nationCode = vipaIpMop.getMopDetailDTO().getNationCode();
              List<com.viettel.vipa.NodeDTO> lstNode = vipaIpMop.getMopDetailDTO().getNodes();
              if (lstNode != null && !lstNode.isEmpty()) {
                List<String> lstIp = new ArrayList<>();
                for (com.viettel.vipa.NodeDTO dto : lstNode) {
                  if (dto.getNodeIp() != null && !lstIp.contains(dto.getNodeIp())) {
                    lstIp.add(dto.getNodeIp());
                  }
                }
                attachDtSelected.setLstIpImpact(lstIp);
              }
            }

          } else {

            attachDtSelected.setSystemCode("2");
            vipaDdMop = vipaDdPort.getMopInfo(attachDtSelected.getDtCode());

            if (vipaDdMop != null && vipaDdMop.getResultCode() == 0) {
              if (vipaDdMop.getMopDetailDTO() == null) {
                result.setMessage(
                    I18n.getChangeManagement("Not found MopInfo :" + attachDtSelected.getDtCode()));
                result.setKey("FAIL");
                return result;
              }
              nationCode = vipaDdMop.getMopDetailDTO().getNationCode();

              List<com.viettel.vmsa.NodeDTO> lstNode = vipaDdMop.getMopDetailDTO().getNodes();
              if (lstNode != null && !lstNode.isEmpty()) {
                List<String> lstIp = new ArrayList<>();
                for (com.viettel.vmsa.NodeDTO dto : lstNode) {
                  if (dto.getNodeIp() != null && !lstIp.contains(dto.getNodeIp())) {
                    lstIp.add(dto.getNodeIp());
                  }
                }
                attachDtSelected.setLstIpImpact(lstIp);
              }
            }
          }

        } else {
          lstModule = attachDtSelected.getLstModule();
          attachDtSelected.setSystemCode("0");
        }
        //luu node mang, neu thoa man tat ca ip ton tai moi cho attach file
        if (nationCode == null || nationCode.trim().isEmpty()) {
          nationCode = "VNM";
        }

        String ipAffectInvalid = "";
        String ipImpactInvalid = "";
        List<InfraDeviceDTO> lstNodeAffect = new ArrayList<>();
        List<String> lstIpAffect = attachDtSelected.getLstIpAffect();
        if (lstIpAffect != null && !lstIpAffect.isEmpty()) {
          lstNodeAffect = getNetworkNodeTDTTV2(lstIpAffect, nationCode);
          ipAffectInvalid = crProcessFromClient.getLstIpInvalid(lstIpAffect, lstNodeAffect);
        }
        List<String> lstIpImpact = attachDtSelected.getLstIpImpact();
        if (lstIpImpact == null || lstIpImpact.isEmpty()) {
          result.setKey("FAIL");
          result.setMessage(I18n.getChangeManagement("cr.tdtt.ipImpactErr"));
          return result;
        }
        List<InfraDeviceDTO> lstNodeImpact = getNetworkNodeTDTTV2(lstIpImpact, nationCode);
        ipImpactInvalid = crProcessFromClient.getLstIpInvalid(lstIpImpact, lstNodeImpact);
        if ("".equals(ipAffectInvalid) && "".equals(ipImpactInvalid)) {
          for (InfraDeviceDTO node : lstNodeImpact) {
            boolean ch = true;
            if (!StringUtils.isStringNullOrEmpty(node.getDeviceId())) {
              for (InfraDeviceDTO node2 : lstNodeImpactAll) {
                if (node2.getIp().equals(node.getIp())) {
                  ch = false;
                }
              }
              if (ch) {
                node.setDtCode(attachDtSelected.getDtCode());
                lstNodeImpactAll.add(node);
              }
            }
          }

          for (InfraDeviceDTO node : lstNodeAffect) {
            boolean ch = true;
            if (!StringUtils.isStringNullOrEmpty(node.getDeviceId())) {
              for (InfraDeviceDTO node2 : lstNodeAffectAll) {
                if (node2.getIp().equals(node.getIp())) {
                  ch = false;
                }
              }
              if (ch) {
                node.setDtCode(attachDtSelected.getDtCode());
                lstNodeAffectAll.add(node);
              }
            }
          }
          List<CrModuleDraftDTO> lstModuleDraft = new ArrayList<>();
          if (lstModule != null && !lstModule.isEmpty()) {
            for (int i = 0; i < lstModule.size(); i++) {
              CrModuleDraftDTO bo = new CrModuleDraftDTO();
              bo.setSERVICE_CODE(lstModule.get(i).getServiceCode());
              bo.setSERVICE_NAME(lstModule.get(i).getServiceCode());
              bo.setMODULE_CODE(lstModule.get(i).getModuleCode());
              bo.setMODULE_NAME(lstModule.get(i).getModuleName());
              bo.setNationCode(nationCode);
              lstModuleDraft.add(bo);
            }
          }
          mapData.put("lstModule", lstModuleDraft);
          mapData.put("lstNodeImpactAll", lstNodeImpactAll);
          mapData.put("lstNodeAffectAll", lstNodeAffectAll);
          //luu file dt
          String dtCode = attachDtSelected.getDtCode();
          mapData.put("dtCode", dtCode);
          //tiennv them systemCode
          mapData.put("systemCode", attachDtSelected.getSystemCode());
          if (dtCode != null && !"".equals(dtCode)) {
            String mopFile = null;
            String mopFileContent = null;
            String mopFileRollback = null;
            String mopFileRollbackContent = null;
            String mopFileKpi = null;
            String mopFileKpiContent = null;
            if ("0".equals(item)) {
              com.viettel.aam.MopFileResult mopResult = wstdttPort.getMopFile(dtCode, crNumber);
              mopFile = mopResult.getMopFile();
              mopFileContent = mopResult.getMopFileContent();
              mopFileRollback = mopResult.getMopRollbackFile();
              mopFileRollbackContent = mopResult.getMopRollbackFileContent();
            } else if (vipaIpMop != null && vipaIpMop.getResultCode() == 0
                && "1".equals(item)) {
              attachDtSelected.setSystemCode("1");
              if (vipaIpMop.getMopDetailDTO() != null) {
                mopFile = vipaIpMop.getMopDetailDTO().getMopFileName();
                mopFileContent = vipaIpMop.getMopDetailDTO().getMopFileContent();
                mopFileRollback = vipaIpMop.getMopDetailDTO().getMopFileName();
                mopFileRollbackContent = vipaIpMop.getMopDetailDTO().getMopFileContent();
                mopFileKpi = vipaIpMop.getMopDetailDTO().getKpiFileName();
                mopFileKpiContent = vipaIpMop.getMopDetailDTO().getKpiFileContent();
              }
            } else if (vipaDdMop != null && vipaDdMop.getResultCode() == 0) {
              attachDtSelected.setSystemCode("2");
              if (vipaDdMop.getMopDetailDTO() != null) {
                mopFile = vipaDdMop.getMopDetailDTO().getMopFileName();
                mopFileContent = vipaDdMop.getMopDetailDTO().getMopFileContent();

                mopFileRollback = vipaDdMop.getMopDetailDTO().getMopFileName();
                mopFileRollbackContent = vipaDdMop.getMopDetailDTO().getMopFileContent();

                mopFileKpi = vipaDdMop.getMopDetailDTO().getKpiFileName();
                mopFileKpiContent = vipaDdMop.getMopDetailDTO().getKpiFileContent();
              }
            }
            if (mopFile != null && !"".equals(mopFile) && mopFileContent != null && !""
                .equals(mopFileContent)) {
              byte[] fileContent = Base64.decode(mopFileContent);
              String fullPath = FileUtils
                  .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                      PassTranformer.decrypt(ftpPass), ftpFolder, count + "_" + mopFile,
                      fileContent, null);
              mapData.put("fileNameDt", count + "_" + mopFile);
              mapData.put("filePathDt", fullPath);
              CrFilesAttachInsiteDTO crFileAttachDTTemp = new CrFilesAttachInsiteDTO();
              crFileAttachDTTemp.setFileName(count + "_" + mopFile);
              crFileAttachDTTemp.setFilePathFtp(fullPath);
              crFileAttachDTTemp.setDtCode(dtCode);
              if (mapData.containsKey("arrFileDt")) {
                List<CrFilesAttachInsiteDTO> lstFileTemp = (List<CrFilesAttachInsiteDTO>) mapData
                    .get("arrFileDt");
                List<CrFilesAttachInsiteDTO> lstDataTemp = new ArrayList<>();
                lstDataTemp.addAll(lstFileTemp);
                lstDataTemp.add(crFileAttachDTTemp);
                mapData.put("arrFileDt", lstDataTemp);
              } else {
                List<CrFilesAttachInsiteDTO> lstFileTemp = new ArrayList<>();
                lstFileTemp.add(crFileAttachDTTemp);
                mapData.put("arrFileDt", lstFileTemp);
              }

              mapData.put(count + "_" + mopFile + "_final_" + crId, attachDtSelected);
            } else {
              result.setMessage(
                  I18n.getChangeManagement("mop.is.not.Exist").replaceAll("@mopCode", mopFile));
              result.setKey("FAIL");
              return result;
            }
            if (item == null || !"2".equals(item)) {
              if (mopFileRollback != null && !"".equals(mopFileRollback)
                  && mopFileRollbackContent != null && !"".equals(mopFileRollbackContent)) {
                byte[] fileContentRollback = Base64.decode(mopFileRollbackContent);
                String fullPath = FileUtils
                    .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                        PassTranformer.decrypt(ftpPass), ftpFolder, count + "_" + mopFileRollback,
                        fileContentRollback, null);
                mapData.put("fileNameDtRollback", count + "_" + mopFileRollback);
                mapData.put("filePathDtRollback", fullPath);
                mapData.put(count + "_" + mopFileRollback + "_final_" + crId, attachDtSelected);
                CrFilesAttachInsiteDTO crFileAttachDTTemp = new CrFilesAttachInsiteDTO();
                crFileAttachDTTemp.setFileName(count + "_" + mopFileRollback);
                crFileAttachDTTemp.setFilePathFtp(fullPath);
                crFileAttachDTTemp.setDtCode(dtCode);
                if (mapData.containsKey("arrFileRollback")) {
                  List<CrFilesAttachInsiteDTO> lstFileTemp = (List<CrFilesAttachInsiteDTO>) mapData
                      .get("arrFileRollback");
                  List<CrFilesAttachInsiteDTO> lstDataTemp = new ArrayList<>();
                  lstDataTemp.addAll(lstFileTemp);
                  lstDataTemp.add(crFileAttachDTTemp);
                } else {
                  List<CrFilesAttachInsiteDTO> lstFileTemp = new ArrayList<>();
                  lstFileTemp.add(crFileAttachDTTemp);
                  mapData.put("arrFileRollback", lstFileTemp);
                }
              } else {
                result.setKey("FAIL");
                result.setMessage(
                    I18n.getChangeManagement("mop.is.not.Exist").replaceAll("mopCode", mopFile));
                return result;
              }
            }
            if (item == null || !"2".equals(item)) {
              if (mopFileKpi != null && !"".equals(mopFileKpi) && mopFileKpiContent != null && !""
                  .equals(mopFileKpiContent)) {
                byte[] fileContentKpi = Base64.decode(mopFileKpiContent);
                String fullPath = FileUtils
                    .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                        PassTranformer.decrypt(ftpPass), ftpFolder, count + "_" + mopFileKpi,
                        fileContentKpi,
                        null);
                mapData.put("fileNameKpi", count + "_" + mopFileKpi);
                mapData.put("filePathKpi", fullPath);
                CrFilesAttachInsiteDTO crFileAttachDTTemp = new CrFilesAttachInsiteDTO();
                crFileAttachDTTemp.setFileName(count + "_" + mopFileKpi);
                crFileAttachDTTemp.setFilePathFtp(fullPath);
                crFileAttachDTTemp.setDtCode(dtCode);
                if (mapData.containsKey("arrFileKPI")) {
                  List<CrFilesAttachInsiteDTO> lstFileTemp = (List<CrFilesAttachInsiteDTO>) mapData
                      .get("arrFileKPI");
                  List<CrFilesAttachInsiteDTO> lstDataTemp = new ArrayList<>();
                  lstDataTemp.addAll(lstFileTemp);
                  lstDataTemp.add(crFileAttachDTTemp);
                } else {
                  List<CrFilesAttachInsiteDTO> lstFileTemp = new ArrayList<>();
                  lstFileTemp.add(crFileAttachDTTemp);
                  mapData.put("arrFileKPI", lstFileTemp);
                }
                mapData.put(count + "_" + mopFileKpi + "_final_" + crId, attachDtSelected);
              }
            }
          }
          count++;

        } else {
          String ipInvalid = "".equals(ipImpactInvalid) ? ipAffectInvalid
              : ("".equals(ipAffectInvalid) ? ipImpactInvalid
                  : ipImpactInvalid + ", " + ipAffectInvalid);
          result.setMessage(I18n.getChangeManagement("cr.tdtt.ipNotExist") + " " + ipInvalid);
          result.setKey(RESULT.ERROR);
          break;
        }
        result.setKey(RESULT.SUCCESS);
      }
      mapData.put("attachDtSelected", lstAttachDtSelected);
      result.setMap(mapData);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  public String split(List<String> lstId) {
    String strId = "";
    if (lstId != null && !lstId.isEmpty()) {
      for (String item : lstId) {
        strId += item + ",";
      }
    }
    if (strId.endsWith(",")) {
      strId = strId.substring(0, strId.length() - 1);
    }
    return strId;
  }

  @Override
  public List<AttachDtDTO> loadDtTestFromVIPA(String userName) {
    List<AttachDtDTO> lstAttachDtDTOs = new ArrayList<>();
    try {
      MopOutputDTO mopResult = vipaDdPort.getMopTestByUser(userName, "5");
      List<com.viettel.vmsa.MopDTO> lstMopInfo = new ArrayList<>();
      if (mopResult != null && mopResult.getMops() != null) {
        lstMopInfo = mopResult.getMops();
      }

      for (com.viettel.vmsa.MopDTO info : lstMopInfo) {
        String dtCode = info.getMopId();
        String dtname = info.getMopName();
        String createDate = info.getCreateTime();
        AttachDtDTO temp = new AttachDtDTO();
        temp.setDtCode(dtCode);
        temp.setUserName(userName);
        temp.setFileName(dtname);
        temp.setCreateDate(createDate);
        lstAttachDtDTOs.add(temp);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstAttachDtDTOs;
  }


  @Override
  public ResultInSideDto selectDTTestFile(List<AttachDtDTO> lstAttachDtSelected, String system,
      String crNumber, String crId) {
    ResultInSideDto result = new ResultInSideDto();
    Map<String, Object> mapData = new HashMap<>();
    if (lstAttachDtSelected == null || lstAttachDtSelected.isEmpty()) {
      result.setKey("FAIL");
      result.setMessage(I18n.getChangeManagement("cr.tdtt.choseOne"));
      return result;
    }

    int count = 0;
    try {
      for (AttachDtDTO attachDtSelected : lstAttachDtSelected) {
        com.viettel.vmsa.MopDetailOutputDTO vipaDdMop = null;
        String nationCode = attachDtSelected.getNationCode();
        attachDtSelected.setSystemCode("2");
        vipaDdMop = vipaDdPort.getMopInfo(attachDtSelected.getDtCode());
        if (vipaDdMop != null && vipaDdMop.getResultCode() == 0) {
          if (vipaDdMop.getMopDetailDTO() == null) {
            result.setKey("FAIL");
            result.setMessage("Not found MopInfo : " + attachDtSelected.getDtCode());
            return result;
          }
        }

        //luu node mang, neu thoa man tat ca ip ton tai moi cho attach file
        if (StringUtils.isStringNullOrEmpty(nationCode)) {
          nationCode = "VNM";
        }

        //luu file dt
        String dtCode = attachDtSelected.getDtCode();
        mapData.put("dtCode", dtCode);
        if (StringUtils.isNotNullOrEmpty(dtCode)) {
          String mopFileTest = null;
          String mopFileTestCotent = null;

          if (vipaDdMop != null && vipaDdMop.getResultCode() == 0) {
            attachDtSelected.setSystemCode("2");
            if (vipaDdMop.getMopDetailDTO() != null) {
              mopFileTest = vipaDdMop.getMopDetailDTO().getMopFileName();
              mopFileTestCotent = vipaDdMop.getMopDetailDTO().getMopFileContent();
            }
          }
          if (StringUtils.isNotNullOrEmpty(mopFileTest) && StringUtils
              .isNotNullOrEmpty(mopFileTestCotent)) {
            byte[] fileContent = Base64.decode(mopFileTestCotent);
            String fullPath = FileUtils
                .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), ftpFolder, count + "_" + mopFileTest,
                    fileContent, null);
            String fileNameDt = FileUtils.getFileName(fullPath);
            mapData.put("fileNameDt", fileNameDt);
            mapData.put("filePathDt", fullPath);
            mapData.put(fileNameDt + "_final_" + crId, attachDtSelected);
          } else {
            return new ResultInSideDto(null, RESULT.FAIL,
                I18n.getChangeManagement("mop.is.not.Exist").replaceAll("@mopCode", mopFileTest));
          }
        }
        count++;
        result.setKey(RESULT.SUCCESS);
      }
      mapData.put("attachDtSelected", lstAttachDtSelected);
      result.setMap(mapData);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  @Override
  public List<CrFilesAttachDTO> search(CrFilesAttachDTO tDTO, int start, int maxResult,
      String sortType, String sortField) {
    return crFilesAttachRepository.search(tDTO, start, maxResult, sortType, sortField);
  }

  @Override
  public List<CrFilesAttachResultDTO> getListFileImportByProcess(CrFilesAttachDTO dto) {
    return crFilesAttachRepository.getListFileImportByProcessOutSide(dto);
  }

  @Override
  public ResultInSideDto deleteFileDT(String crId) {
    List<CrFilesAttachDTO> lstFile = crFilesAttachRepository.getCrFileDT(crId);
    List<String> fileIds = null;
    if (lstFile != null) {
      fileIds = lstFile.stream().map(CrFilesAttachDTO::getFileId).collect(Collectors.toList());
    }
    if (fileIds != null && fileIds.size() > 0) {
      crFilesAttachRepository.deleteFileDT(fileIds, Long.valueOf(crId));
    }
    return new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
  }

  @Override
  public List<CrFilesAttachDTO> getCrFileAttachForOutSide(CrFilesAttachDTO crFilesAttachDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return crFilesAttachRepository
        .getCrFileAttachForOutSide(crFilesAttachDTO, rowStart, maxRow, sortType, sortFieldList);
  }
}


