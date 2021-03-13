package com.viettel.gnoc.sr.business;

import static com.viettel.gnoc.commons.repository.BaseRepository.setLanguage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.ws.client.BindingProviderProperties;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.CfgRoleDataDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.OdServiceProxy;
import com.viettel.gnoc.commons.proxy.SrCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.CfgRoleDataRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CFG_ROLE_DATA_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.SR_CATALOG;
import com.viettel.gnoc.commons.utils.Constants.SR_CONFIG;
import com.viettel.gnoc.commons.utils.Constants.SR_ROLE_UPDATE;
import com.viettel.gnoc.commons.utils.Constants.SR_STATUS;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ErrorInfo;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.cr.dto.CrAffectedServiceDetailsDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.InsertAutoCrForSrDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.TemplateImportDTO;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.sr.dto.InsertFileDTO;
import com.viettel.gnoc.sr.dto.SRActionCodeDTO;
import com.viettel.gnoc.sr.dto.SRApproveDTO;
import com.viettel.gnoc.sr.dto.SRCatalogChildDTO;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRChildAutoDTO;
import com.viettel.gnoc.sr.dto.SRConfig2DTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRCreateAutoCRDTO;
import com.viettel.gnoc.sr.dto.SRCreatedFromOtherSysDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SREvaluateDTO;
import com.viettel.gnoc.sr.dto.SRFilesDTO;
import com.viettel.gnoc.sr.dto.SRHisDTO;
import com.viettel.gnoc.sr.dto.SRMappingProcessCRDTO;
import com.viettel.gnoc.sr.dto.SRMopDTO;
import com.viettel.gnoc.sr.dto.SRParamDTO;
import com.viettel.gnoc.sr.dto.SRReasonRejectDTO;
import com.viettel.gnoc.sr.dto.SRRenewDTO;
import com.viettel.gnoc.sr.dto.SRRoleActionDTO;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import com.viettel.gnoc.sr.dto.SRWorkLogDTO;
import com.viettel.gnoc.sr.dto.SRWorklogTypeDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.sr.dto.SrWsToolCrDTO;
import com.viettel.gnoc.sr.dto.UnitSRCatalogDTO;
import com.viettel.gnoc.sr.model.SREntity;
import com.viettel.gnoc.sr.model.SRFilesEntity;
import com.viettel.gnoc.sr.model.SRParamEntity;
import com.viettel.gnoc.sr.model.SRRenewEntity;
import com.viettel.gnoc.sr.repository.SRApproveRepository;
import com.viettel.gnoc.sr.repository.SRCatalogChildRepository;
import com.viettel.gnoc.sr.repository.SRCatalogRepository2;
import com.viettel.gnoc.sr.repository.SRChildAutoRepository;
import com.viettel.gnoc.sr.repository.SRConfig2Repository;
import com.viettel.gnoc.sr.repository.SRConfigRepository;
import com.viettel.gnoc.sr.repository.SRCreateAutoCrRepository;
import com.viettel.gnoc.sr.repository.SRCreatedFromOtherSysRepository;
import com.viettel.gnoc.sr.repository.SREvaluateRepository;
import com.viettel.gnoc.sr.repository.SRHisRepository;
import com.viettel.gnoc.sr.repository.SRMopRepository;
import com.viettel.gnoc.sr.repository.SRParamRepository;
import com.viettel.gnoc.sr.repository.SRRenewRepository;
import com.viettel.gnoc.sr.repository.SRWorkLogRepository;
import com.viettel.gnoc.sr.repository.SrRepository;
import com.viettel.gnoc.wo.dto.CfgWoTickHelpDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.nims.infra.webservice.sr.DistributeIpResourceForm;
import com.viettel.security.PassTranformer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.xml.ws.BindingProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@Service
@Slf4j
@Transactional
public class SrBusinessImpl implements SrBusiness {

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

  @Value("${application.ws.requestTimeOutWS:300000}")
  private Integer requestTimeOut;

  @Value("${application.ws.connectTimeOutWS:60000}")
  private Integer connectTimeOut;

  @Value("${application.ws.gate_userNameService:null}")
  private String userNameGate;

  @Value("${application.ws.gate_passService:null}")
  private String passGate;

  @Value("${application.ws.saltService:null}")
  private String saltService;

  @Value("${application.ws.vmsa_userNameService:null}")
  private String userNameVMSA;

  @Value("${application.ws.vmsa_passService:null}")
  private String passVMSA;

  @Value("${application.ws.aam_userNameService:null}")
  private String userNameAAM;

  @Value("${application.ws.aam_passService:null}")
  private String passAAM;

  @Value("${application.ws.vipa_userNameService:null}")
  private String userNameVIPA;

  @Value("${application.ws.vipa_passService:null}")
  private String passVIPA;


  private final static String SR_REPORT = "SR_REPORT";
  Map<String, SRConfigDTO> mapStatus = new HashMap<>();

  private static String crSystemId = "5";

  @Autowired
  SrRepository srRepository;

  @Autowired
  SRConfigRepository srConfigRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  CatLocationRepository catLocationRepository;

  @Autowired
  SRHisRepository srHisRepository;

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  MessagesRepository messagesRepository;

  @Autowired
  SRWorkLogRepository srWorkLogRepository;

  @Autowired
  SRCatalogRepository2 srCatalogRepository2;

  @Autowired
  SRApproveRepository srApproveRepository;

  @Autowired
  SRRenewRepository srRenewRepository;

  @Autowired
  SRParamRepository srParamRepository;

  @Autowired
  SRMopRepository srMopRepository;

  @Autowired
  SREvaluateRepository srEvaluateRepository;

  @Autowired
  OdServiceProxy odServiceProxy;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Autowired
  SRCreatedFromOtherSysRepository srCreatedFromOtherSysRepository;

  @Autowired
  SRConfig2Repository srConfig2Repository;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Autowired
  CrCategoryServiceProxy crCategoryServiceProxy;

  @Autowired
  SrCategoryServiceProxy srCategoryServiceProxy;

  @Autowired
  SRCatalogChildRepository srCatalogChildRepository;

  @Autowired
  SRCreateAutoCrRepository srCreateAutoCrRepository;

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  UnitBusiness unitBusiness;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  CfgRoleDataRepository cfgRoleDataRepository;

  @Autowired
  SRChildAutoRepository srChildAutoRepository;

  com.viettel.nims.infra.webservice.sr.DistributeIpResourceOutput outputNims = null;

  Map<String, GnocFileDto> mapFileWo = new HashMap<>();

  @Override
  public Datatable getListSR(SrInsiteDTO srDTO) {
    log.info("Request to getListSR : {}", srDTO);
    UserToken userToken = ticketProvider.getUserToken();
    srDTO.setUsername(userToken.getUserName());
    return getLstSR(srRepository.getListSR(srDTO), srDTO, "getlist");
  }

  @Override
  public Datatable getListSRByUserLogin(SrInsiteDTO srDTO) {
    log.info("Request to getListSRByUserLogin : {}", srDTO);
    UserToken userToken = ticketProvider.getUserToken();
    srDTO.setCreatedUser(userToken.getUserName());
    srDTO.setSrUser(userToken.getUserName());
    srDTO.setUsername(userToken.getUserName());
    return getLstSR(srRepository.getListSRByUserLogin(srDTO), srDTO, "getlist");
  }

  @Override
  public ResultInSideDto insertSR(SrInsiteDTO srDTO) {
    log.info("Request to insertSR : {}", srDTO);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    ResultInSideDto resultInSideDto = srRepository.insertSR(srDTO);
    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      SRActionCodeDTO searchActionCode = new SRActionCodeDTO();
      searchActionCode.setNewStatus(srDTO.getStatus());
      List<SRActionCodeDTO> lsActionCode = srRepository
          .searchSrActionCode(searchActionCode, 0, 0, "asc", "acId");
      SRHisDTO srHisDTO = new SRHisDTO();
      srHisDTO.setCreatedUser(srDTO.getCreatedUser());
      srHisDTO.setCreatedTime(sdf.format(srDTO.getCreatedTime()));
      srHisDTO.setSrStatus(srDTO.getStatus());
      srHisDTO.setSrId(srDTO.getSrId().toString());
      if (lsActionCode != null && !lsActionCode.isEmpty()) {
        for (SRActionCodeDTO acDTO : lsActionCode) {
          if (acDTO.getOldStatus() == null) {
            srHisDTO.setActionCode(acDTO.getActionCode());
            srHisDTO.setComments(acDTO.getDefaultComment());
            break;
          }
        }
      }
      if (srHisDTO.getActionCode() == null && srHisDTO.getComments() == null) {
        srHisDTO.setActionCode("Add");
        srHisDTO.setComments("Add new SR");
      }
      srHisRepository.createSRHis(srHisDTO);

      if (srDTO.isOpenConnect()) {
        SRHisDTO srHisDTO2 = new SRHisDTO();
        srHisDTO2.setCreatedUser(srHisDTO.getCreatedUser());
        srHisDTO2.setCreatedTime(srHisDTO.getCreatedTime());
        srHisDTO2.setSrStatus(srHisDTO.getSrStatus());
        srHisDTO2.setSrId(srHisDTO.getSrId());
        srHisDTO2.setActionCode(srHisDTO.getActionCode());
        //20201218 dungpv edit sr mkn
        List<SRConfigDTO> vipaHisComment = null;
        if (SR_STATUS.DRAFT.equals(srDTO.getStatus())) {
          vipaHisComment = srConfigRepository
              .getByConfigGroup(Constants.SR_CONFIG.VIPA_HISTORY_COMMENTS_STATUS_DRAFT);
        } else {
          vipaHisComment = srConfigRepository
              .getByConfigGroup(Constants.SR_CONFIG.VIPA_HISTORY_COMMENTS);
        }
        if (vipaHisComment != null && vipaHisComment.size() > 0) {
          srHisDTO2.setComments(vipaHisComment.get(0).getConfigCode());
          srHisRepository.createSRHis(srHisDTO2);
        }
        //end
      }

      if (Constants.SR_STATUS.NEW.equals(srDTO.getStatus())) {
        SRRoleUserInSideDTO srRoleUserDTO = new SRRoleUserInSideDTO();
        srRoleUserDTO.setUnitId(srDTO.getSrUnit());
        srRoleUserDTO.setStatus("A");
        srRoleUserDTO.setCountry(srDTO.getCountry());
        srRoleUserDTO.setRoleCode(srDTO.getRoleCode());
        //nhan tin cho ng tao
        sendMessages(srDTO, srDTO.getCreatedUser(), null, srDTO.getStatus(), "", "", "");

        if (StringUtils.isStringNullOrEmpty(srDTO.getSrUser())) {
          //neu ko co ng xu ly, kiem tra co truong nhom hay ko
          srRoleUserDTO.setIsLeader(1L);
          List<SRRoleUserInSideDTO> lstUserUnit = srRepository
              .searchSRRoleUser(srRoleUserDTO);
          if (lstUserUnit == null || lstUserUnit.isEmpty()) {
            srRoleUserDTO.setIsLeader(null);
            lstUserUnit = srRepository.searchSRRoleUser(srRoleUserDTO);
          }
          sendMessages(srDTO, null, lstUserUnit, srDTO.getStatus(), "", "", "");
        } else {
          //neu co nguoi xu ly, kiem tra co truong nhom hay ko
          srRoleUserDTO.setIsLeader(1L);
          List<SRRoleUserInSideDTO> lstUserUnit = srRepository
              .searchSRRoleUser(srRoleUserDTO);
          List<SRRoleUserInSideDTO> srUser = new ArrayList<>();
          if (lstUserUnit != null && !lstUserUnit.isEmpty()) {
            for (SRRoleUserInSideDTO user : lstUserUnit) {
              if (!user.getUsername().equals(srDTO.getSrUser())) {
                srUser.add(new SRRoleUserInSideDTO(user.getUsername()));
              }
            }
          }
          srUser.add(new SRRoleUserInSideDTO(srDTO.getSrUser()));
          sendMessages(srDTO, null, srUser, srDTO.getStatus(), "", "", "");
        }
      }
      //20201218 dungpv edit sr mkn
      else if (SR_STATUS.DRAFT.equals(srDTO.getStatus()) && srDTO.isOpenConnect()) {
        sendMessages(srDTO, srDTO.getCreatedUser(), null, srDTO.getStatus(), "", "", "");
      }
      //end
      if (Constants.SR_STATUS.UNDER_APPROVAL.equals(srDTO.getStatus())) {
        SRCatalogDTO srCatalogDTO = srCatalogRepository2
            .findById(Long.parseLong(srDTO.getServiceId()));
        if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getApprove()) && !"0"
            .equals(String.valueOf(srCatalogDTO.getApprove()))) {
          String unit = srRepository
              .getUnitParentForApprove("1", srDTO.getCreatedUnit());
          //neu la SR can phe duyet, tao moi o trang thai under approval thi nhan tin cho lanh dao don vi
          List<String> lstTP = srRepository.getLeaderApprove("TP", unit);
          for (String tp : lstTP) {
            sendMessages(srDTO, tp, null, srDTO.getStatus(), "", "", "");
          }
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertSRDTO(SrInsiteDTO srDTO) {
    log.info("Request to insertSRDTO : {}", srDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultValidateSRDTO = validateSRDTO(true, srDTO);
    if (resultValidateSRDTO.getKey().equals(RESULT.FAIL)) {
      return resultValidateSRDTO;
    }
    Double offset = userRepository.getOffsetFromUser(userToken.getUserName());
    srDTO = convertSRDate2VietNamDate(srDTO, offset);
    srDTO.setCreatedTime(new Date());
    if (SR_STATUS.NEW.equals(srDTO.getStatus())) {
      srDTO.setSendDate(new Date());
    } else {
      srDTO.setSendDate(null);
    }
    resultInSideDto = insertSR(srDTO);
    if (!resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      resultInSideDto.setMessage(I18n.getLanguage("sr.button.add.fail"));
      if (srDTO.isAutoCreatCR() && StringUtils
          .isNotNullOrEmpty(resultValidateSRDTO.getReturnCode())) {
        String[] crNumberDelete = resultValidateSRDTO.getReturnCode().split(";");
        for (int i = 0; i < crNumberDelete.length; i++) {
          deleteCrAndWo(crNumberDelete[i], userToken);
        }
      }
    } else {
      SRCatalogDTO srCatalogDTO = srCatalogRepository2
          .findById(Long.parseLong(srDTO.getServiceId()));
      if (Constants.SR_STATUS.UNDER_APPROVAL.equals(srDTO.getStatus())
          && (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getApprove()) && !"0"
          .equals(String.valueOf(srCatalogDTO.getApprove())))) {
        UsersEntity usersEntity = userRepository.getUserByUserId(userToken.getUserID());
        String unitLevel1 = srRepository
            .getUnitParentForApprove("1", usersEntity.getUnitId().toString());
        if (!StringUtils.isStringNullOrEmpty(unitLevel1)) {
          SRApproveDTO dtoAprrove = new SRApproveDTO();
          dtoAprrove.setSrId(srDTO.getSrId());
          dtoAprrove.setApproveUnitLevel1(
              StringUtils.isStringNullOrEmpty(unitLevel1) ? null : Long.parseLong(unitLevel1));
          if ("2".equals(String.valueOf(srCatalogDTO.getApprove()))) {
            String unitLevel2 = srRepository.getUnitParentForApprove("2", unitLevel1);
            if (!StringUtils.isStringNullOrEmpty(unitLevel2)) {
              dtoAprrove.setApproveUnitLevel2(
                  StringUtils.isStringNullOrEmpty(unitLevel2) ? null : Long.parseLong(unitLevel2));
            }
          }
          srApproveRepository.insertSRApprove(dtoAprrove);
        }
      }
      resultInSideDto = autoCreateSR(srCatalogDTO, srDTO);
      if (srDTO.getLstMopTmp() != null && !srDTO.getLstMopTmp().isEmpty()) {
        srMopRepository.insertListSRMop(srDTO.getLstMopTmp());
      }
      actionSaveLinkCr(srDTO.getLstCr(), srDTO.getSrId());
    }
    if (srDTO.isOpenConnect() && SR_STATUS.DRAFT
        .equals(srDTO.getStatus())) {
      resultInSideDto.setMessage(I18n.getLanguage("sr.button.add.openconnect.success"));
    }
    return resultInSideDto;
  }

  private ResultInSideDto autoCreateSR(SRCatalogDTO srCatalogDTO, SrInsiteDTO srInsiteDTO) {
    log.info("Request to autoCreateSR : {}", srCatalogDTO, srInsiteDTO);
    ResultInSideDto rs = new ResultInSideDto();
    rs.setKey(RESULT.SUCCESS);
    try {
      //<editor-fold desc="tu dong tao SR cha khac don vi" defaultstate="collapsed">
      List<UnitSRCatalogDTO> lstUnitInService = srCatalogRepository2
          .getListUnitSRCatalog(new SRCatalogDTO(srCatalogDTO.getServiceCode()));
      if (lstUnitInService != null && lstUnitInService.size() > 1) {
        for (UnitSRCatalogDTO unit : lstUnitInService) {
          //bo qua don vi hien tai dang them moi
          if (unit.getUnitId().equals(String.valueOf(srInsiteDTO.getSrUnit()))) {
            continue;
          }
          if (!StringUtils.isStringNullOrEmpty(unit.getAutoCreateSR()) && "1"
              .equals(unit.getAutoCreateSR())) {
            SrInsiteDTO dtoUnit = srInsiteDTO;
            dtoUnit.setSrUnit(StringUtils.isStringNullOrEmpty(unit.getUnitId()) ? null
                : Long.parseLong(unit.getUnitId()));
            dtoUnit.setServiceId(unit.getServiceId());
            dtoUnit.setStatus(Constants.SR_STATUS.DRAFT);
            dtoUnit.setSrUser("");
            List<String> lsSequense = srRepository.getListSequenseSR("OPEN_PM.SR_SEQ", 1);
            dtoUnit.setSrId(lsSequense.get(0).isEmpty() ? null : Long.parseLong(lsSequense.get(0)));
            CatLocationDTO catLocationDTO = catLocationRepository
                .getNationByLocationId(Long.valueOf(srInsiteDTO.getCountry()));
            String srCode =
                "SR_" + (catLocationDTO != null ? catLocationDTO.getLocationCode() : "null")
                    + "_" + DateUtil.date2StringNoSlash(new Date()) + "_" + dtoUnit.getSrId();
            dtoUnit.setSrCode(srCode);
            rs = insertSR(dtoUnit);
            if (!"SUCCESS".equals(rs.getKey())) {
              rs.setMessage(I18n.getValidation("sr.auto.create.fail"));
              return rs;
            }
          }
        }
      }
      //</editor-fold>
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }


  private ResultInSideDto autoCreateSRChild(SRCatalogDTO srCatalogDTO, SrInsiteDTO srInsiteDTO) {
    log.info("Request to autoCreateSRChild : {}", srCatalogDTO, srInsiteDTO);
    ResultInSideDto rs = new ResultInSideDto();
    rs.setKey(RESULT.SUCCESS);
    boolean checkAuto = false;
    try {
      //<editor-fold desc="tu dong tao SR con" defaultstate="collapsed">
      SRCatalogChildDTO dtoChild = new SRCatalogChildDTO();
      dtoChild.setServiceCode(srCatalogDTO.getServiceCode());
      dtoChild.setStatus("A");
      dtoChild.setServiceId(srCatalogDTO.getServiceId());
      List<SRCatalogChildDTO> lstChildAll = srCatalogChildRepository
          .getListCatalogChild(dtoChild);
      Map<Long, String> mapSrChildAutoCheck = new HashMap<>();
      String check = srCatalogChildRepository
          .checkGenerateNo(dtoChild);
      List<SRCatalogChildDTO> lstChild = new ArrayList<>();
      if (StringUtils.isNotNullOrEmpty(check)) {
        if ("-1".equals(check)) {
          lstChild = srCatalogChildRepository
              .getListCatalogChild(dtoChild);
        } else {
          dtoChild.setGenerateNo(Long.parseLong(check));
          lstChild = srCatalogChildRepository
              .getListCatalogChild(dtoChild);
        }
      }
      //dungpv 08/09/2020 them moi SR Child
      if (lstChild != null && !lstChild.isEmpty()) {
        for (SRCatalogChildDTO child : lstChild) {
          mapSrChildAutoCheck
              .put(child.getServiceIdChild(), String.valueOf(child.getServiceIdChild()));
          rs = saveSRChild(child, srInsiteDTO, true);
          if (!RESULT.SUCCESS.equals(rs.getKey())) {
            return rs;
          }
          checkAuto = rs.getCheck();
        }
      }
      //end
      //dungpv 08/09/2020 them moi SRChildAuto
      if (lstChildAll != null && !lstChildAll.isEmpty()) {
        for (SRCatalogChildDTO srChildAuto : lstChildAll) {
          if (!mapSrChildAutoCheck.containsKey(srChildAuto.getServiceIdChild())) {
            mapSrChildAutoCheck.put(srChildAuto.getServiceIdChild(),
                String.valueOf(srChildAuto.getServiceIdChild()));
            rs = saveSRChild(srChildAuto, srInsiteDTO, false);
            if (!RESULT.SUCCESS.equals(rs.getKey())) {
              return rs;
            }
          }
        }
      }
//      end
      //</editor-fold>
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    rs.setCheck(checkAuto);
    return rs;
  }

  private ResultInSideDto saveSRChild(SRCatalogChildDTO child, SrInsiteDTO srInsiteDTO,
      boolean insertSRChild) {
    log.info("Request to saveSRChild : {}", child, srInsiteDTO, insertSRChild);
    ResultInSideDto rs = new ResultInSideDto();
    rs.setKey(RESULT.SUCCESS);
    boolean check = false;
    String commentAuto = null;
    if (!StringUtils.isStringNullOrEmpty(child.getAutoCreateSR())
        && child.getAutoCreateSR() == 1L) {
      SRCatalogDTO catalogChild = srCatalogRepository2
          .findById(child.getServiceIdChild());
      UserToken userToken = TicketProvider.getUserToken();
      Calendar calendar = Calendar.getInstance();

      //12-02-2020: namtn edit - ma nhom nao tich chon sinh tu dong thi moi dc sinh SR con
      if (!StringUtils.isStringNullOrEmpty(catalogChild.getRoleCode()) && !StringUtils
          .isStringNullOrEmpty(catalogChild.getAutoCreateSR())) {
        String[] arrRoleCode = catalogChild.getRoleCode().split(",");
        String[] arrCreateSr = catalogChild.getAutoCreateSR().split(",");
        for (int i = 0; i < arrRoleCode.length; i++) {
          String roleCode = arrRoleCode[i];
          String autoCreateSr = "";
          commentAuto = null;
          try {
            //try catch truong hop phan tu thu i cua autoCreateSR null
            autoCreateSr = arrCreateSr[i];
          } catch (Exception e) {
            log.error(e.getMessage());
          }
          if ("1".equals(autoCreateSr)) {
            SrInsiteDTO srChild = new SrInsiteDTO();
            srChild.setParentCode(srInsiteDTO.getSrCode());
            srChild.setStatus(Constants.SR_STATUS.NEW);
            srChild.setCreatedTime(calendar.getTime());
            srChild.setUpdatedTime(calendar.getTime());
            srChild.setSendDate(calendar.getTime());
            srChild.setCreatedUser(userToken.getUserName());
            srChild.setTitle(srInsiteDTO.getTitle());
            srChild.setDescription(
                StringUtils.isNotNullOrEmpty(catalogChild.getServiceDescription()) ? catalogChild
                    .getServiceDescription() : srInsiteDTO.getDescription());
            srChild.setServiceId(catalogChild.getServiceId().toString());
            srChild.setSrUnit(
                StringUtils.isStringNullOrEmpty(catalogChild.getExecutionUnit()) ? null
                    : Long.parseLong(catalogChild.getExecutionUnit()));
            srChild.setRoleCode(roleCode);
            srChild.setSrUser("");
            srChild.setServiceGroup(catalogChild.getServiceGroup());
            srChild.setServiceArray(catalogChild.getServiceArray());
            srChild.setCountry(catalogChild.getCountry());
            if (insertSRChild) {
              SrInsiteDTO endTimeSR = getChangeExecutionTimeAndFlowExecute(null,
                  srChild.getServiceId(), srChild.getCountry());
              srChild.setStartTime(endTimeSR.getStartTime());
              srChild.setEndTime(endTimeSR.getEndTime());
              List<String> lsSequense = srRepository.getListSequenseSR("OPEN_PM.SR_SEQ", 1);
              srChild.setSrId(
                  lsSequense.get(0).isEmpty() ? null : Long.parseLong(lsSequense.get(0)));
              CatLocationDTO catLocationDTO = catLocationRepository
                  .getNationByLocationId(Long.valueOf(catalogChild.getCountry()));
              String srCode =
                  "SR_" + (catLocationDTO != null ? catLocationDTO.getLocationCode() : "null")
                      + "_" + DateUtil.date2StringNoSlash(new Date()) + "_" + srChild.getSrId();
              srChild.setSrCode(srCode);
              rs = insertSR(srChild);
              commentAuto = rs.getKey();
              if (!"SUCCESS".equals(rs.getKey())) {
                rs.setMessage(I18n.getValidation("srChild.auto.create.fail"));
                return rs;
              }
              check = true;
            }
            if (srChild != null) {
              SRChildAutoDTO srChildAutoDTO = new SRChildAutoDTO();
              srChildAutoDTO.setCountry(srChild.getCountry());
              srChildAutoDTO.setSrParentCode(srChild.getParentCode());
              srChildAutoDTO.setCreateUser(srChild.getCreatedUser());
              srChildAutoDTO.setTitle(srChild.getTitle());
              srChildAutoDTO.setDescription(srChild.getDescription());
              srChildAutoDTO.setRoleCode(roleCode);
              srChildAutoDTO.setServiceGroup(catalogChild.getServiceGroup());
              srChildAutoDTO.setServiceArray(catalogChild.getServiceArray());
              srChildAutoDTO.setGenerateNo(child.getGenerateNo());
              srChildAutoDTO.setCreateSR(0L);
              if (StringUtils.isNotNullOrEmpty(srChild.getServiceId())) {
                srChildAutoDTO.setServiceId(Long.valueOf(srChild.getServiceId()));
              }
              if (srChild.getSrUnit() != null && srChild.getSrUnit() > 0L) {
                srChildAutoDTO.setSrUnit(srChild.getSrUnit());
              }
              if (StringUtils.isNotNullOrEmpty(srChild.getSrCode())) {
                srChildAutoDTO.setSrChildCode(srChild.getSrCode());
                srChildAutoDTO.setCreateSR(1L);
              }
              if (StringUtils.isNotNullOrEmpty(commentAuto)) {
                srChildAutoDTO.setCommentAuto(commentAuto);
              }
              rs = srChildAutoRepository.insertOrUpdateSRChildAuto(srChildAutoDTO);
              if (!"SUCCESS".equals(rs.getKey())) {
                rs.setMessage(I18n.getValidation("srChild.auto.create.fail"));
                return rs;
              }
            }
          }
        }
      }
    }
    rs.setCheck(check);
    return rs;
  }

  private ResultInSideDto autoCreateSRChildByGennerateNo(String srCode) {
    log.info("Request to autoCreateSRChildByGennerateNo : {}", srCode);
    ResultInSideDto rs = new ResultInSideDto();
    rs.setKey(RESULT.SUCCESS);
    if (StringUtils.isNotNullOrEmpty(srCode)) {
      List<SRChildAutoDTO> lstCheckSRChildClose = srChildAutoRepository
          .getListSRChildCheckClosed(srCode);
      if (lstCheckSRChildClose != null && !lstCheckSRChildClose.isEmpty()) {
        boolean checkSRAllClosed = false;
        Long generateNo = null;
        int count = 0;
        for (SRChildAutoDTO srChildAutoDTO : lstCheckSRChildClose) {
          if (SR_STATUS.CONCLUDED.equals(srChildAutoDTO.getStatus())) {
            count++;
          }
          if (SR_STATUS.CLOSED.equals(srChildAutoDTO.getStatus())) {
            count++;
          }
        }

        if (count == lstCheckSRChildClose.size()) {
          checkSRAllClosed = true;
          generateNo = lstCheckSRChildClose.get(0).getGenerateNo();
        }
        if (checkSRAllClosed && generateNo != null && generateNo > 0) {
          List<SrInsiteDTO> lstSRChildAuto = srChildAutoRepository
              .getListSRChildAutoByGennerateNo(lstCheckSRChildClose.get(0).getSrParentCode(),
                  generateNo);
          Calendar calendar = Calendar.getInstance();
          if (lstSRChildAuto != null && !lstSRChildAuto.isEmpty()) {
            for (SrInsiteDTO srChildAutoInsert : lstSRChildAuto) {
              srChildAutoInsert.setStatus(Constants.SR_STATUS.NEW);
              srChildAutoInsert.setCreatedTime(calendar.getTime());
              srChildAutoInsert.setUpdatedTime(calendar.getTime());
              srChildAutoInsert.setSendDate(calendar.getTime());
              srChildAutoInsert.setSrUser("");
              SrInsiteDTO endTimeSR = getChangeExecutionTimeAndFlowExecute(null,
                  srChildAutoInsert.getServiceId(), srChildAutoInsert.getCountry());
              srChildAutoInsert.setStartTime(endTimeSR.getStartTime());
              srChildAutoInsert.setEndTime(endTimeSR.getEndTime());
              List<String> lsSequense = srRepository.getListSequenseSR("OPEN_PM.SR_SEQ", 1);
              srChildAutoInsert.setSrId(
                  lsSequense.get(0).isEmpty() ? null : Long.parseLong(lsSequense.get(0)));
              CatLocationDTO catLocationDTO = catLocationRepository
                  .getNationByLocationId(Long.valueOf(srChildAutoInsert.getCountry()));
              String srChildCode =
                  "SR_" + (catLocationDTO != null ? catLocationDTO.getLocationCode() : "null")
                      + "_" + DateUtil.date2StringNoSlash(new Date()) + "_" + srChildAutoInsert
                      .getSrId();
              srChildAutoInsert.setSrCode(srChildCode);
              rs = insertSR(srChildAutoInsert);
              if (!"SUCCESS".equals(rs.getKey())) {
                rs.setMessage(I18n.getValidation("srChild.auto.create.fail"));
                return rs;
              } else {
                SRChildAutoDTO srChildAutoDTO = srChildAutoRepository
                    .getDetailSRChildAuto(srChildAutoInsert.getChildAutoId());
                if (srChildAutoDTO != null && srChildAutoDTO.getId() != null
                    && srChildAutoDTO.getId() > 0) {
                  srChildAutoDTO.setCreateSR(1L);
                  srChildAutoDTO.setSrChildCode(srChildAutoInsert.getSrCode());
                  srChildAutoDTO.setCommentAuto(rs.getKey());
                  rs = srChildAutoRepository.insertOrUpdateSRChildAuto(srChildAutoDTO);
                  if (!"SUCCESS".equals(rs.getKey())) {
                    rs.setMessage(I18n.getValidation("srChild.auto.create.fail"));
                    return rs;
                  }
                }
              }
            }
          }
        }
      }
    }
    return rs;
  }

  @Override
  public ResultInSideDto updateSR(SrInsiteDTO srDTO) {
    log.info("Request to updateSR : {}", srDTO);
    ResultInSideDto resultDTO = new ResultInSideDto();
    SrInsiteDTO objBefore = null;
    if (ticketProvider != null && ticketProvider.getUserToken() != null) {
      UserToken userToken = ticketProvider.getUserToken();
      objBefore = srRepository.getDetail(srDTO.getSrId(), userToken.getUserName());
    } else {
      objBefore = srRepository.getDetailNoOffset(srDTO.getSrId());
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    if (!objBefore.getStatus().equals(srDTO.getStatus()) && "New".equals(srDTO.getStatus())) {
      try {
        srDTO.setSendDate(new Date(System.currentTimeMillis()));

        //namtn edit on 27/06 chuyen trang thai khac sang new : thoi gian bat dau thay doi = sysdate + 30p
        if (DateTimeUtils.compareDateTime(new Date(), srDTO.getStartTime()) == 1) {
          SRCatalogDTO dtoCatalog = srCatalogRepository2
              .findById(Long.parseLong(srDTO.getServiceId()));
          setStartTimeAndEndTimeSR(srDTO, srDTO.getCountry(), dtoCatalog.getExecutionTime(),
              dtoCatalog.getIsAddDay().toString());
        }
        //namtn edit on 27/06 chuyen trang thai khac sang new : thoi gian bat dau thay doi = sysdate + 30p
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    resultDTO = srRepository.insertSR(srDTO);
    resultDTO.setDescription(objBefore.getStatus());//status cu
    if (RESULT.SUCCESS.equals(resultDTO.getKey())) {
      // Luu lich su SR
      SRActionCodeDTO searchActionCode = new SRActionCodeDTO();
      searchActionCode.setOldStatus(objBefore.getStatus());
      searchActionCode.setNewStatus(srDTO.getStatus());
      List<SRActionCodeDTO> lsActionCode = srRepository
          .searchSrActionCode(searchActionCode, 0, 0, "asc", "acId");

      SRHisDTO srHisDTO = new SRHisDTO();
      srHisDTO.setCreatedUser(srDTO.getUpdatedUser());
      srHisDTO.setCreatedTime(dateFormat.format(srDTO.getUpdatedTime()));
      srHisDTO.setSrStatus(srDTO.getStatus());
      srHisDTO.setSrId(srDTO.getSrId().toString());

      if (lsActionCode != null && lsActionCode.size() > 0) {
        srHisDTO.setActionCode(lsActionCode.get(0).getActionCode());
        srHisDTO.setComments(lsActionCode.get(0).getDefaultComment());
      }
      srHisRepository.createSRHis(srHisDTO);

      if (srDTO.isOpenConnect() && Constants.SR_STATUS.NEW
          .equals(objBefore.getStatus())) {
        List<SRConfigDTO> lstStatusCreateCRVipa = srConfigRepository
            .getByConfigGroup(Constants.SR_CONFIG.STATUS_CREATE_CR_DV_MKN);
        if (lstStatusCreateCRVipa == null || lstStatusCreateCRVipa.isEmpty()) {
          lstStatusCreateCRVipa = new ArrayList<>();
          SRConfigDTO statusConfig = new SRConfigDTO();
          statusConfig.setConfigCode(Constants.SR_STATUS.ASSIGNED_PLANNING);
          lstStatusCreateCRVipa.add(statusConfig);
        }
        if (lstStatusCreateCRVipa.get(0).getConfigCode().equals(srDTO.getStatus())) {
          SRHisDTO srHisDTO2 = new SRHisDTO();
          srHisDTO2.setCreatedUser(srHisDTO.getCreatedUser());
          srHisDTO2.setCreatedTime(srHisDTO.getCreatedTime());
          srHisDTO2.setSrStatus(srHisDTO.getSrStatus());
          srHisDTO2.setSrId(srHisDTO.getSrId());
          srHisDTO2.setActionCode(srHisDTO.getActionCode());
          List<SRConfigDTO> vipaHisComment = srConfigRepository
              .getByConfigGroup(Constants.SR_CONFIG.VIPA_HISTORY_COMMENTS);
          srHisDTO2.setComments(vipaHisComment.get(0).getConfigCode());
          srHisRepository.createSRHis(srHisDTO2);
        }
      }
      //20201218 dungpv edit sr mkn
      else if (srDTO.isOpenConnect() && (SR_STATUS.REJECTED.equals(objBefore.getStatus()) && (
          Constants.SR_STATUS.NEW.equals(srDTO.getStatus()) || SR_STATUS.DRAFT
              .equals(srDTO.getStatus())))) {
        SRHisDTO srHisDTO2 = new SRHisDTO();
        srHisDTO2.setCreatedUser(srHisDTO.getCreatedUser());
        srHisDTO2.setCreatedTime(srHisDTO.getCreatedTime());
        srHisDTO2.setSrStatus(srHisDTO.getSrStatus());
        srHisDTO2.setSrId(srHisDTO.getSrId());
        srHisDTO2.setActionCode(srHisDTO.getActionCode());
        List<SRConfigDTO> vipaHisComment = null;
        if (SR_STATUS.DRAFT.equals(srDTO.getStatus())) {
          vipaHisComment = srConfigRepository
              .getByConfigGroup(Constants.SR_CONFIG.VIPA_HISTORY_COMMENTS_STATUS_DRAFT);
        } else {
          vipaHisComment = srConfigRepository
              .getByConfigGroup(Constants.SR_CONFIG.VIPA_HISTORY_COMMENTS);
        }
        if (vipaHisComment != null && vipaHisComment.size() > 0) {
          srHisDTO2.setComments(vipaHisComment.get(0).getConfigCode());
          srHisRepository.createSRHis(srHisDTO2);
        }
      }
      //end
      //<editor-fold desc="namtn adding send message to unit">
      if (!Constants.SR_STATUS.DRAFT.equals(srDTO.getStatus())) {
        SRRoleUserInSideDTO srRoleUserDTO = new SRRoleUserInSideDTO();
        srRoleUserDTO.setUnitId(srDTO.getSrUnit());
        srRoleUserDTO.setStatus("A");
        srRoleUserDTO.setCountry(srDTO.getCountry());
        srRoleUserDTO.setRoleCode(srDTO.getRoleCode());
        //trang thai New nhan tin cho toan bo SRF trong don vi xu ly
        if (!objBefore.getStatus().equals(srDTO.getStatus())) {
          //nhan tin cho nguoi tao
          //namtn edit on November 09
          if (Constants.SR_STATUS.CANCELLED.equals(srDTO.getStatus())
              || Constants.SR_STATUS.REJECTED.equals(srDTO.getStatus())) {
            List<SrInsiteDTO> lstWorklog = srRepository.getWorkLog(srDTO, 0, 0, false);
            if (lstWorklog != null && !lstWorklog.isEmpty()) {
              String workLog = "";
              for (SrInsiteDTO dto : lstWorklog) {
                if (!StringUtils.isStringNullOrEmpty(dto.getWlText())) {
                  workLog += dto.getWlText() + ", ";
                }
              }
              if (workLog.trim().endsWith(",")) {
                workLog = workLog.trim().substring(0, workLog.trim().length() - 1);
              }
              srDTO.setWorkLog(workLog);
            }
          }
          //namtn edit on November 09
          sendMessages(srDTO, srDTO.getCreatedUser(), null, srDTO.getStatus(), "", "", "");

          if (Constants.SR_STATUS.NEW.equals(srDTO.getStatus())) {
            if (StringUtils.isStringNullOrEmpty(srDTO.getSrUser())) {
              //neu ko co ng xu ly, kiem tra co truong nhom hay ko
              srRoleUserDTO.setIsLeader(1L);
              List<SRRoleUserInSideDTO> lstUserUnit = srRepository
                  .searchSRRoleUser(srRoleUserDTO);
              if (lstUserUnit == null || lstUserUnit.isEmpty()) {
                srRoleUserDTO.setIsLeader(null);
                lstUserUnit = srRepository.searchSRRoleUser(srRoleUserDTO);
              }
              sendMessages(srDTO, null, lstUserUnit, srDTO.getStatus(), "", "", "");
            } else {
              //neu co nguoi xu ly, kiem tra co truong nhom hay ko
              srRoleUserDTO.setIsLeader(1L);
              List<SRRoleUserInSideDTO> lstUserUnit = srRepository
                  .searchSRRoleUser(srRoleUserDTO);
              List<SRRoleUserInSideDTO> srUser = new ArrayList<>();
              if (lstUserUnit != null && !lstUserUnit.isEmpty()) {
                for (SRRoleUserInSideDTO user : lstUserUnit) {
                  if (!user.getUsername().equals(srDTO.getSrUser())) {
                    srUser.add(new SRRoleUserInSideDTO(user.getUsername()));
                  }
                }
              }
              srUser.add(new SRRoleUserInSideDTO(srDTO.getSrUser()));
              sendMessages(srDTO, null, srUser, srDTO.getStatus(), "", "", "");
            }
          } else {
            List<SRRoleUserInSideDTO> lstTmp = new ArrayList();
            if (StringUtils.isNotNullOrEmpty(srDTO.getSrUser())) {
              lstTmp.add(new SRRoleUserInSideDTO(srDTO.getSrUser()));
              sendMessages(srDTO, null, lstTmp, srDTO.getStatus(), "", "", "");
            }
          }
        } else {
          if ((StringUtils.isStringNullOrEmpty(objBefore.getSrUser()) && !StringUtils
              .isStringNullOrEmpty(srDTO.getSrUser()))
              || (!StringUtils.isStringNullOrEmpty(objBefore.getSrUser()) && !objBefore.getSrUser()
              .equals(srDTO.getSrUser()))) {
            List<SRRoleUserInSideDTO> lstTmp = new ArrayList();
            lstTmp.add(new SRRoleUserInSideDTO(srDTO.getSrUser()));
            sendMessages(srDTO, null, lstTmp, srDTO.getStatus(), "", "", "");
          }
        }
      }
      //20201218 dungpv edit sr mkn
      else if (SR_STATUS.DRAFT.equals(srDTO.getStatus()) && srDTO.isOpenConnect()) {
        sendMessages(srDTO, srDTO.getCreatedUser(), null, srDTO.getStatus(), "", "", "");
      }
      //end
      //</editor-fold>

    }
    return resultDTO;
  }

  @Override
  public ResultInSideDto saveSrChild(SrInsiteDTO srChild) {
    log.info("Request to saveSrChild : {}", srChild);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    srChild.setStatus(Constants.SR_STATUS.DRAFT);
    srChild.setCreatedTime(new Date());
    srChild.setUpdatedTime(new Date());
    srChild.setCreatedUser(userToken.getUserName());
    try {
      srChild.setStartTime(DateTimeUtils.convertStringToDateTime(
          DateTimeUtils.convertDateTimeStampToString(DateUtil.addMinute(new Date(), 30))));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      srChild.setStartTime(new Date());
    }
    srChild.setEndTime(srChild.getEndTime());
//    Double offset = userRepository.getOffsetFromUser(userToken.getUserName());
    if (StringUtils.isStringNullOrEmpty(srChild.getTitle())) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getValidation("SrInsiteDTO.null.title"));
      return resultInSideDto;
    }
    if (StringUtils.isStringNullOrEmpty(srChild.getDescription())) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getValidation("SrInsiteDTO.null.description"));
      return resultInSideDto;
    }
    if (StringUtils.isStringNullOrEmpty(srChild.getDescription())) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getValidation("SrInsiteDTO.null.description"));
      return resultInSideDto;
    }
    if (StringUtils.isStringNullOrEmpty(srChild.getServiceId())) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getValidation("SrInsiteDTO.null.serviceId"));
      return resultInSideDto;
    }
    if (StringUtils.isStringNullOrEmpty(srChild.getSrUnit())) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getValidation("SrInsiteDTO.null.srUnit"));
      return resultInSideDto;
    }
    SRCatalogDTO srCatalogDTO = srCatalogRepository2
        .findById(Long.parseLong(srChild.getServiceId()));
    List<UnitSRCatalogDTO> lstUnitInService = srCatalogRepository2
        .getListUnitSRCatalog(new SRCatalogDTO(srCatalogDTO.getServiceCode()));
    if (lstUnitInService != null && lstUnitInService.size() > 1) {
      for (UnitSRCatalogDTO u : lstUnitInService) {
        if (u.getUnitId().equals(String.valueOf(srChild.getSrUnit()))) {
          srChild.setServiceId(u.getServiceId());
          break;
        }
      }
    }
    if (StringUtils.isStringNullOrEmpty(srChild.getRoleCode())) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getValidation("SrInsiteDTO.null.roleCode"));
      return resultInSideDto;
    }
    srChild.setServiceGroup(srCatalogDTO.getServiceGroup());
    srChild.setServiceArray(srCatalogDTO.getServiceArray());
    List<String> lsSequense = srRepository.getListSequenseSR("OPEN_PM.SR_SEQ", 1);
    srChild.setSrId(
        lsSequense.get(0).isEmpty() ? null : Long.parseLong(lsSequense.get(0)));
    CatLocationDTO catLocationDTO = catLocationRepository
        .getNationByLocationId(Long.valueOf(srCatalogDTO.getCountry()));
    String srCode =
        "SR_" + (catLocationDTO != null ? catLocationDTO.getLocationCode() : "null")
            + "_" + DateUtil.date2StringNoSlash(new Date()) + "_" + srChild.getSrId();
    srChild.setSrCode(srCode);
    srChild.setCountry(srCatalogDTO.getCountry());
//    srChild = convertSRDate2VietNamDate(srChild, offset);
    resultInSideDto = insertSR(srChild);
    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      // copy file from parent sr to child sr
      if (StringUtils.isNotNullOrEmpty(srChild.getParentCode())) {
        try {
          UsersInsideDto unitToken = userRepository.getUserDTOByUserName(userToken.getUserName());
          String[] arrayParentCode = srChild.getParentCode().split("_");
          Long parentId = Long.valueOf(arrayParentCode[arrayParentCode.length - 1]);
          GnocFileDto gnocFileDtoSr = new GnocFileDto();
          gnocFileDtoSr.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
          gnocFileDtoSr.setBusinessId(parentId);
          List<GnocFileDto> gnocFileDtosParent = gnocFileRepository
              .getListGnocFileForSR(gnocFileDtoSr);
          List<SRFilesEntity> srFilesDTOSParent = srRepository.getListSRFileByObejctId(parentId);
          Map<Long, SRFilesEntity> srFilesEntityMapParent = new HashMap<>();
          for (SRFilesEntity srFilesEntity : srFilesDTOSParent) {
            srFilesEntityMapParent.put(srFilesEntity.getFileId(), srFilesEntity);
          }
          List<GnocFileDto> gnocFileDtosChildInsert = new ArrayList<>();
          for (GnocFileDto dtoFileParentToChild : gnocFileDtosParent) {
            Date date = new Date();
            if (StringUtils.isNotNullOrEmpty(dtoFileParentToChild.getPath())) {
              byte[] bytes = FileUtils
                  .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                      PassTranformer.decrypt(ftpPass), dtoFileParentToChild.getPath());
              String fullPath = FileUtils
                  .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                      PassTranformer.decrypt(ftpPass), ftpFolder,
                      dtoFileParentToChild.getFileName(),
                      bytes, date);
              String fullPathOld = FileUtils.saveUploadFile(dtoFileParentToChild.getFileName(),
                  bytes, uploadFolder, date);
              SRFilesDTO srFilesDTO = srFilesEntityMapParent
                  .get(dtoFileParentToChild.getMappingId())
                  .toDTO();
              srFilesDTO.setObejctId(srChild.getSrId());
              srFilesDTO.setFilePath(fullPathOld);
              srFilesDTO.setFileName(FileUtils.getFileName(fullPathOld));
              srFilesDTO.setFileId(null);
              ResultInSideDto resultFileDataOld = srRepository.addSRFile(srFilesDTO);
              dtoFileParentToChild.setPath(fullPath);
              dtoFileParentToChild.setCreateUnitId(unitToken.getUnitId());
              dtoFileParentToChild.setCreateUnitName(unitToken.getUnitName());
              dtoFileParentToChild.setCreateUserId(userToken.getUserID());
              dtoFileParentToChild.setCreateUserName(userToken.getUserName());
              dtoFileParentToChild.setCreateTime(new Date());
              dtoFileParentToChild.setMappingId(resultFileDataOld.getId());
              dtoFileParentToChild.setBusinessId(srChild.getSrId());
              dtoFileParentToChild.setId(null);
              gnocFileDtosChildInsert.add(dtoFileParentToChild);
            }
          }
          gnocFileRepository
              .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SR, srChild.getSrId(),
                  gnocFileDtosChildInsert);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public SrInsiteDTO setStartTimeAndEndTimeSR(SrInsiteDTO srDTO, String locationId,
      String executionTime, String isAddDay) {
    log.info("Request to setStartTimeAndEndTimeSR : {}", srDTO, locationId, executionTime,
        isAddDay);
    try {
      Calendar start = Calendar.getInstance();
      start.setTime(new Date());
      start.add(Calendar.MINUTE, 30);
      srDTO.setStartTime(start.getTime());
      List<Date> lstDate = srRepository.getDayOffForExecutionTime(locationId);
      Calendar end = getEndTime(start, executionTime, lstDate, isAddDay);
      srDTO.setEndTime(end.getTime());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return srDTO;
  }

  @Override
  public ResultInSideDto getSRCode(String country, Long srId) {
    log.info("Request to getSRCode : {}", country, srId);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (srId != null && srId > 0) {
      resultInSideDto.setReturnCode(genSrNumber(country, srId));
    } else {
      List<String> lst = srRepository.getListSequenseSR("OPEN_PM.SR_SEQ", 1);
      resultInSideDto.setReturnCode(genSrNumber(country, Long.parseLong(lst.get(0))));
    }
    return resultInSideDto;
  }

  @Override
  public List<SRConfigDTO> getServiceArrayCBB(String srUser,
      String country) {
    log.info("Request to getServiceArrayCBB : {}", srUser, country);
    List<SRConfigDTO> lstServicesArray = srConfigRepository
        .getByConfigGroup(Constants.SR_CATALOG.SERVICE_ARRAY);
    List<SRConfigDTO> lstServiceArrayNew = new ArrayList<>();

    if (lstServiceArrayNew.isEmpty()) {
      lstServiceArrayNew.addAll(lstServicesArray);
    }
    if (StringUtils.isNotNullOrEmpty(country)) {
      int size = lstServiceArrayNew.size();
      for (int i = size - 1; i > -1; i--) {
        if (!country.equals(lstServiceArrayNew.get(i).getCountry())) {
          lstServiceArrayNew.remove(i);
        }
      }
    }
    return lstServiceArrayNew;
  }

  @Override
  public List<SRConfigDTO> getServiceGroupCBB(String configCode, String country) {
    log.info("Request to getServiceGroupCBB : {}", configCode, country);
    List<SRConfigDTO> lstServicesGroup = srConfigRepository.getConfigGroup(configCode);
    if (lstServicesGroup != null && lstServicesGroup.size() > 0) {
      int size = lstServicesGroup.size();
      if (!StringUtils.isStringNullOrEmpty(country)) {
        for (int i = size - 1; i > -1; i--) {
          if (!country.equals(lstServicesGroup.get(i).getCountry())) {
            lstServicesGroup.remove(i);
          }
        }
      }
    }
    return lstServicesGroup;
  }

  @Override
  public List<SRCatalogDTO> getListServicesCBB(String servicesGroup) {
    log.info("Request to getListServicesCBB : {}", servicesGroup);
    List<SRCatalogDTO> lstResult = new ArrayList<>();
    SRCatalogDTO srCatalogDTO = new SRCatalogDTO();
    if (StringUtils.isNotNullOrEmpty(servicesGroup)) {
      srCatalogDTO.setServiceGroup(servicesGroup);
    }
    List<SRCatalogDTO> lstServices = srCatalogRepository2.getListSRCatalogDTO(srCatalogDTO);
    if (lstServices != null && lstServices.size() > 0) {
      Map<String, SRCatalogDTO> map = new HashMap<>();
      for (SRCatalogDTO dto : lstServices) {
        if (map.get(dto.getServiceCode() + "_" + dto.getServiceName()) == null) {
          map.put(dto.getServiceCode() + "_" + dto.getServiceName(), dto);
          lstResult.add(dto);
        }
      }
    }
    return lstResult;
  }

  @Override
  public List<SRRoleDTO> getListSRRoleCBB(Long country) {
    log.info("Request to getListUnit : {}", country);
    if (country != null && country > 0) {
      SRRoleDTO dtoTemp = new SRRoleDTO();
      dtoTemp.setCountry(country.toString());
      List<SRRoleDTO> lsRoleCode = srRepository.getListSRRole(dtoTemp);
      List<SRRoleDTO> lstResult = new ArrayList<>();
      int size = lsRoleCode.size();
      if (lsRoleCode != null && !lsRoleCode.isEmpty()) {
        for (int i = size - 1; i > -1; i--) {
          if ("Viewer".equals(lsRoleCode.get(i).getRoleCode()) || "Orinirator"
              .equals(lsRoleCode.get(i).getRoleCode())) {
            lsRoleCode.remove(lsRoleCode.get(i));
            continue;
          }
          lstResult.add(lsRoleCode.get(i));
        }
      }
      return lstResult;
    }
    return null;
  }

  @Override
  public List<UnitDTO> getListUnitCBB() {
    log.info("Request to getListUnit : {}");
    return unitBusiness.getListUnit();
  }

  @Override
  public List<UnitDTO> getListUnitBySeviceCBB(String services, boolean isMoreUnit, String srUser) {
    log.info("Request to getListUnitBySeviceCBB : {}", services, isMoreUnit, srUser);
    List<UnitDTO> lsUnit = new ArrayList<>();
    if (isMoreUnit) {
      SRCatalogDTO srCatalogDTO = srCatalogRepository2.findById(Long.parseLong(services));
      if (StringUtils.isNotNullOrEmpty(services)) {
        SRCatalogDTO dtoUnit = new SRCatalogDTO();
        dtoUnit.setServiceCode(srCatalogDTO.getServiceCode());
//        dtoUnit.setServiceId(Long.parseLong(services));
        dtoUnit.setStatus("A");
        List<UnitSRCatalogDTO> lsUnitInService = srCatalogRepository2.getListUnitSRCatalog(dtoUnit);
        for (UnitSRCatalogDTO u : lsUnitInService) {
          UnitDTO dto = new UnitDTO();
          dto.setUnitId(Long.parseLong(u.getUnitId()));
          dto.setUnitName(u.getUnitName());
          lsUnit.add(dto);
        }
      }
    } else {
      SrInsiteDTO dto = new SrInsiteDTO();
      if (StringUtils.isNotNullOrEmpty(srUser)) {
        dto.setSrUser(srUser);
      }
      if (StringUtils.isNotNullOrEmpty(services)) {
        dto.setServiceId(services);
      }
      lsUnit = srRepository.getListSRUnitForDetail(dto);
    }
    return lsUnit;
  }

  @Override
  public SrInsiteDTO getChangeExecutionTimeAndFlowExecute(String startTime, String services,
      String countryId) {
    log.info("Request to getListUnitBySeviceCBB : {}", services, countryId);
    if (StringUtils.isNotNullOrEmpty(services) && StringUtils.isNotNullOrEmpty(countryId)) {
      SRCatalogDTO srCatalogDTO = new SRCatalogDTO();
      srCatalogDTO.setServiceId(Long.parseLong(services));
      srCatalogDTO.setCountry(countryId);
      List<SRCatalogDTO> lst = srCatalogRepository2.getListSRCatalogDTO(srCatalogDTO);
      srCatalogDTO = lst.isEmpty() ? null : lst.get(0);
      if (srCatalogDTO != null) {
        try {
          Calendar start = Calendar.getInstance();
          if (!StringUtils.isStringNullOrEmpty(startTime)) {
            start.setTime(DateUtil.convertStringToTime(startTime, "dd/MM/yyyy HH:mm:ss"));
          } else {
            start.setTime(new Date());
            start.add(Calendar.MINUTE, 30);
          }
          Date dateStartTime = start.getTime();
          List<Date> lstDate = srRepository.getDayOffForExecutionTime(countryId);
          Calendar end = getEndTime(start,
              srCatalogDTO.getExecutionTime().isEmpty() ? null : srCatalogDTO.getExecutionTime(),
              lstDate,
              srCatalogDTO.getIsAddDay() != null ? srCatalogDTO.getIsAddDay().toString() : "0");
          Date dateEndTime = end.getTime();
          SrInsiteDTO srInsiteDTO = new SrInsiteDTO();
          srInsiteDTO.setStartTime(dateStartTime);
          srInsiteDTO.setEndTime(dateEndTime);
          srInsiteDTO.setExecutionTime(srCatalogDTO.getExecutionTime());
          srInsiteDTO.setFlowExecute(
              srCatalogDTO.getFlowExecuteName() != null ? srCatalogDTO.getFlowExecuteName() : null);
          return srInsiteDTO;
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    }
    return null;
  }

  private Calendar getEndTime(Calendar start, String executionTime, List<Date> lstDayOff,
      String isAddDayAfterNine) {
    Calendar tempEnd = Calendar.getInstance();
    tempEnd.setTime(start.getTime());
    try {
      long tmp = 1;
      Double exeTime = Double.valueOf(executionTime);
      Double exeTimeTmp = exeTime - exeTime.longValue();
      while (tmp <= exeTime.longValue()) {
        if (!isDayOff(lstDayOff, tempEnd.getTime())) {
          tmp++;
        }
        tempEnd.add(Calendar.DAY_OF_MONTH, 1);
      }

      //ngay cuoi cung trung cua endtime theo ngay trung voi ngay nghi
      while (isDayOff(lstDayOff, tempEnd.getTime())) {
        tempEnd.add(Calendar.DAY_OF_MONTH, 1);
      }

      if (exeTimeTmp > 0.0) {
        exeTimeTmp = exeTimeTmp * 24 * 60 * 60;
        tempEnd.add(Calendar.SECOND, exeTimeTmp.intValue());
      }

      //ngay cuoi cung trung voi ngay nghi
      while (isDayOff(lstDayOff, tempEnd.getTime())) {
        tempEnd.add(Calendar.DAY_OF_MONTH, 1);
      }
      //ngay cuoi cung trung voi ngay nghi

      //namtn edit on April - yc thang 04 them checkBox + them 1 ngay
      if (!StringUtils.isStringNullOrEmpty(isAddDayAfterNine) && "1".equals(isAddDayAfterNine)) {
        Calendar nine = Calendar.getInstance();
        nine.setTime(start.getTime());
        nine.set(Calendar.HOUR_OF_DAY, 9);
        nine.set(Calendar.MINUTE, 0);
        nine.set(Calendar.SECOND, 0);

        if (start.getTime().getTime() - nine.getTime().getTime() > 0) {
          tempEnd.add(Calendar.DAY_OF_MONTH, 1);
          while (isDayOff(lstDayOff, tempEnd.getTime())) {
            tempEnd.add(Calendar.DAY_OF_MONTH, 1);
          }
        }
      }
      //namtn edit on April - yc thang 04 them checkBox + them 1 ngay

      return tempEnd;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      tempEnd.setTime(start.getTime());
      Double exeTime = Double.valueOf(executionTime) * 24 * 60 * 60;
      tempEnd.add(Calendar.SECOND, exeTime.intValue());
      return tempEnd;
    }
  }

  @Override
  public File exportData(SrInsiteDTO srDTO) throws Exception {
    log.info("Request to exportData : {}", srDTO);
    UserToken userToken = ticketProvider.getUserToken();
    srDTO.setUsername(userToken.getUserName());
    List<SrInsiteDTO> lstEx = srRepository.getListSRExport(srDTO);
    Datatable datatable = new Datatable();
    datatable.setData(lstEx);
    return exportFileEx(
        (List<SrInsiteDTO>) getLstSR(datatable, srDTO, Constants.RESULT_EXPORT).getData());
  }

  private String genSrNumber(String country, Long srId) {
    if (StringUtils.isStringNullOrEmpty(country)) {
      return "";
    } else {
      CatLocationDTO catLocationDTO = catLocationRepository
          .getNationByLocationId(Long.valueOf(country));
      return "SR_" + (catLocationDTO != null ? catLocationDTO.getLocationCode() : "null") + "_"
          + DateUtil.date2StringNoSlash(new Date()) + "_" + srId;
    }
  }

  private void sendMessages(SrInsiteDTO srDTO, String createdUser,
      List<SRRoleUserInSideDTO> lstSrUserInUnit, String status, String serviceArray,
      String serviceGroup,
      String serviceCode) {
    try {
      //nhan tin cho nguoi tao
      if (!StringUtils.isStringNullOrEmpty(createdUser)) {
        //20201218 dungpv edit sr mkn
        List<SRConfigDTO> lsSms = null;
        if (srDTO.isOpenConnect()) {
          if (SR_STATUS.DRAFT.equals(srDTO.getStatus())) {
            lsSms = srConfigRepository.getSmsContent(status,//next status
                serviceArray, serviceGroup, serviceCode, "SMS_SR_OPEN_CONNECT_DRAFT");
          } else if (SR_STATUS.NEW.equals(srDTO.getStatus())) {
            lsSms = srConfigRepository.getSmsContent(status,//next status
                serviceArray, serviceGroup, serviceCode, "SMS_SR_OPEN_CONNECT_NEW");
          }
        } else {
          lsSms = srConfigRepository.getSmsContent(status,//next status
              serviceArray, serviceGroup, serviceCode, "SMS");
        }
        //end
        //namtn edit da ngon ngu tin nhan
        Map<String, Object> map = DataUtil
            .getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.SR,
                Constants.APPLIED_BUSSINESS.CONFIG2_TYPE, I18n.getLocale());
        String sqlLanguage = (String) map.get("sql");
        Map mapParam = (Map) map.get("mapParam");
        Map mapType = (Map) map.get("mapType");
        List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
            .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
        lsSms = setLanguage(lsSms, lstLanguage, "configId", "configName");
        //namtn edit da ngon ngu tin nhan
        if (lsSms != null) {
          try {
            UsersInsideDto userExecute = userRepository.getUserDTOByUserName(createdUser);
            UnitDTO unitExecute = unitRepository.findUnitById(userExecute.getUnitId());
            List<MessagesDTO> lstMsg = new ArrayList<>();
            for (SRConfigDTO item : lsSms) {
              MessagesDTO messagesDTO = setMessagesExecuteDTO(
                  !StringUtils.isStringNullOrEmpty(item.getConfigName()) ? item.getConfigName()
                      .replace(Constants.SR_CONFIG2.SR_CODE,
                          srDTO.getSrCode() + " : (" + srDTO.getTitle() + ")")
                      .replace(Constants.SR_CONFIG2.WORKLOG_CONTENT,
                          StringUtils.isStringNullOrEmpty(srDTO.getWorkLog()) ? ""
                              : srDTO.getWorkLog()) : "",
                  userExecute.getMobile(),
                  createdUser,
                  unitExecute
              );
              lstMsg.add(messagesDTO);
            }
            if (!lstMsg.isEmpty()) {
              messagesRepository.insertOrUpdateListMessagesCommon(lstMsg);
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
        }
      }
      if (lstSrUserInUnit != null && lstSrUserInUnit.size() > 0) {
        //20201218 dungpv edit sr mkn
        List<SRConfigDTO> lsSmsSRF = null;
        if (srDTO.isOpenConnect()) {
          if (SR_STATUS.DRAFT.equals(srDTO.getStatus())) {
            lsSmsSRF = srConfigRepository.getSmsContent(status,//next status
                serviceArray, serviceGroup, serviceCode, "SMS_SR_OPEN_CONNECT_DRAFT");
          } else if (SR_STATUS.NEW.equals(srDTO.getStatus())) {
            lsSmsSRF = srConfigRepository.getSmsContent(status,//next status
                serviceArray, serviceGroup, serviceCode, "SMS_SR_OPEN_CONNECT_NEW");
          }
        } else {
          lsSmsSRF = srConfigRepository.getSmsContent(status,//next status
              serviceArray, serviceGroup, serviceCode, "SMS_SRF");
        }
        //end
        //namtn edit da ngon ngu tin nhan
        Map<String, Object> map = DataUtil
            .getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.SR,
                Constants.APPLIED_BUSSINESS.CONFIG2_TYPE, I18n.getLocale());
        String sqlLanguage = (String) map.get("sql");
        Map mapParam = (Map) map.get("mapParam");
        Map mapType = (Map) map.get("mapType");
        List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
            .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
        lsSmsSRF = DataUtil.setLanguage(lsSmsSRF, lstLanguage, "configId", "configName");
        //namtn edit da ngon ngu tin nhan
        if (lsSmsSRF != null) {
          List<MessagesDTO> lstMsgSRF = new ArrayList<>();
          for (SRRoleUserInSideDTO userTmp : lstSrUserInUnit) {
            try {
              UsersInsideDto userExecute = userRepository
                  .getUserDTOByUserName(userTmp.getUsername());
              UnitDTO unitExecute = unitRepository.findUnitById(userExecute.getUnitId());
              for (SRConfigDTO item : lsSmsSRF) {
                MessagesDTO messagesDTO = setMessagesExecuteDTO(
                    !StringUtils.isStringNullOrEmpty(item.getConfigName()) ? item.getConfigName()
                        .replace("SR_CODE", srDTO.getSrCode() + " : (" + srDTO.getTitle()) + ")"
                        : "",
                    userExecute.getMobile(),
                    userTmp.getUsername(),
                    unitExecute
                );
                lstMsgSRF.add(messagesDTO);
              }
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
          }
          if (!lstMsgSRF.isEmpty()) {
            messagesRepository.insertOrUpdateListMessagesCommon(lstMsgSRF);
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void sendMessages(SrInsiteDTO data, String createdUser,
      List<SRRoleUserInSideDTO> lstSrUserInUnit, SRConfigDTO configDTO) {
    try {
      //nhan tin cho nguoi tao
      if (!StringUtils.isStringNullOrEmpty(createdUser)) {
        List<SRConfigDTO> lsSms = srConfigRepository.getSmsContentByConfig(configDTO);
        Map<String, Object> map = DataUtil
            .getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.SR,
                Constants.APPLIED_BUSSINESS.CONFIG2_TYPE, I18n.getLocale());
        String sqlLanguage = (String) map.get("sql");
        Map mapParam = (Map) map.get("mapParam");
        Map mapType = (Map) map.get("mapType");
        List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
            .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
        lsSms = DataUtil.setLanguage(lsSms, lstLanguage, "configId", "configName");
        if (lsSms != null) {
          UsersInsideDto userExecute = userRepository.getUserDTOByUserName(createdUser);
          UnitDTO unitExecute = unitRepository.findUnitById(userExecute.getUnitId());
          List<MessagesDTO> lstMsg = new ArrayList<>();
          for (SRConfigDTO item : lsSms) {
            MessagesDTO messagesDTO = setMessagesExecuteDTO(
                !StringUtils.isStringNullOrEmpty(item.getConfigName()) ? item.getConfigName()
                    .replace("SR_CODE", data.getSrCode() + " : (" + data.getTitle()) + ")" : "",
                userExecute.getMobile(),
                createdUser,
                unitExecute
            );
            lstMsg.add(messagesDTO);
          }
          if (!lstMsg.isEmpty()) {
            messagesRepository.insertOrUpdateListMessagesCommon(lstMsg);
          }
        }
      }

      //nhan tin cho nguoi xu ly
      if (lstSrUserInUnit != null && lstSrUserInUnit.size() > 0) {
        List<SRConfigDTO> lsSmsSRF = srConfigRepository.getSmsContentByConfig(configDTO);
        Map<String, Object> map = DataUtil
            .getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.SR,
                Constants.APPLIED_BUSSINESS.CONFIG2_TYPE, I18n.getLocale());
        String sqlLanguage = (String) map.get("sql");
        Map mapParam = (Map) map.get("mapParam");
        Map mapType = (Map) map.get("mapType");
        List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
            .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
        lsSmsSRF = DataUtil.setLanguage(lsSmsSRF, lstLanguage, "configId", "configName");
        if (lsSmsSRF != null) {
          List<MessagesDTO> lstMsgSRF = new ArrayList<>();
          for (SRRoleUserInSideDTO userTmp : lstSrUserInUnit) {
            UsersInsideDto userExecute = userRepository.getUserDTOByUserName(userTmp.getUsername());
            UnitDTO unitExecute = unitRepository.findUnitById(userExecute.getUnitId());
            for (SRConfigDTO item : lsSmsSRF) {
              MessagesDTO messagesDTO = setMessagesExecuteDTO(
                  !StringUtils.isStringNullOrEmpty(item.getConfigName()) ? item.getConfigName()
                      .replace("SR_CODE", data.getSrCode() + " : (" + data.getTitle()) + ")" : "",
                  userExecute.getMobile(),
                  userTmp.getUsername(),
                  unitExecute
              );
              lstMsgSRF.add(messagesDTO);
            }
          }
          if (!lstMsgSRF.isEmpty()) {
            messagesRepository.insertOrUpdateListMessagesCommon(lstMsgSRF);
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private MessagesDTO setMessagesExecuteDTO(String content, String phone, String userName,
      UnitDTO unitExecute) {
    MessagesDTO messagesDTO = new MessagesDTO();
    messagesDTO.setSmsGatewayId(unitExecute.getSmsGatewayId().toString());
    messagesDTO.setContent(content);
    messagesDTO.setReceiverUsername(userName);
    messagesDTO.setCreateTime(date2ddMMyyyyHHMMss(new Date()));
    messagesDTO.setReceiverPhone(phone);
    messagesDTO.setAlias(SR_CONFIG.SR_ALIAS);
    messagesDTO.setStatus("0");
    return messagesDTO;
  }

  private String date2ddMMyyyyHHMMss(Date value) {
    if (value != null) {
      SimpleDateFormat dateTimeNoSlash = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      return dateTimeNoSlash.format(value);
    }
    return "";
  }

  private Datatable getLstSR(Datatable datatable, SrInsiteDTO srInsiteDTO, String export) {
    List<SrInsiteDTO> lst = (List<SrInsiteDTO>) datatable.getData();
    List<SrInsiteDTO> lstResult = new ArrayList<>();
    Map<String, String> mapSrChildCode = new HashMap<>();
    Map<Long, String> mapWorklog = new HashMap<>();
    Map<Long, String> mapWorklogReason = new HashMap<>();
    Map<String, List<Date>> mapCountryDayOff = new HashMap<>();
    Map<String, String> mapEvaluateDTO = new HashMap<>();
    Map<Long, String> mapTotalTime = new HashMap<>();
    SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    if (lst != null && lst.size() > 0) {
      if (export.equals(Constants.RESULT_EXPORT)) {
        SrInsiteDTO dtoSearch = new SrInsiteDTO();
        if (srInsiteDTO.getCreateFromDate() != null && !""
            .equals(sp.format(srInsiteDTO.getCreateFromDate()))) {
          dtoSearch.setCreateFromDate(srInsiteDTO.getCreateFromDate());
        }
        if (srInsiteDTO.getCreateToDate() != null && !""
            .equals(sp.format(srInsiteDTO.getCreateToDate()))) {
          dtoSearch.setCreateToDate(srInsiteDTO.getCreateToDate());
        }
        List<SrInsiteDTO> lstWorklog = srRepository.getWorkLog(dtoSearch, 0, 0, false);
        if (lstWorklog != null && !lstWorklog.isEmpty()) {
          int size = lstWorklog.size();
          for (int i = size - 1; i > -1; i--) {
            if (!StringUtils.isStringNullOrEmpty(lstWorklog.get(i).getCreatedTime())
                && !StringUtils.isStringNullOrEmpty(lstWorklog.get(i).getWlText())) {
              String workLog =
                  lstWorklog.get(i).getCreatedTime() + " : " + lstWorklog.get(i).getWlText()
                      + " ";
              String workLogReason = lstWorklog.get(i).getReason();
              if (mapWorklog.get(lstWorklog.get(i).getSrId()) != null) {
                mapWorklog.put(lstWorklog.get(i).getSrId(),
                    mapWorklog.get(lstWorklog.get(i).getSrId()) + workLog);
                mapWorklogReason.put(lstWorklog.get(i).getSrId(),
                    mapWorklogReason.get(lstWorklog.get(i).getSrId()) + "," + workLogReason);
              } else {
                mapWorklog.put(lstWorklog.get(i).getSrId(), workLog);
                mapWorklogReason.put(lstWorklog.get(i).getSrId(), workLogReason);
              }
              lstWorklog.remove(lstWorklog.get(i));
            }
          }
        }
        SREvaluateDTO srEvaluateDTO = new SREvaluateDTO();
        srEvaluateDTO
            .setSrId(srInsiteDTO.getSrId() != null ? String.valueOf(srInsiteDTO.getSrId()) : null);
        if (srInsiteDTO.getCreateFromDate() != null && !""
            .equals(sp.format(srInsiteDTO.getCreateFromDate()))) {
          srEvaluateDTO.setCreateFromDate(srInsiteDTO.getCreateFromDate());
        }
        if (srInsiteDTO.getCreateToDate() != null && !""
            .equals(sp.format(srInsiteDTO.getCreateToDate()))) {
          srEvaluateDTO.setCreateToDate(srInsiteDTO.getCreateToDate());
        }
        List<SREvaluateDTO> lstEvaTmp = srRepository
            .getListSREvaluateNew(srEvaluateDTO);
        if (lstEvaTmp != null && !lstEvaTmp.isEmpty()) {
          for (SREvaluateDTO ev : lstEvaTmp) {
            if (!mapEvaluateDTO.containsKey(ev.getSrId())) {
              mapEvaluateDTO.put(ev.getSrId(),
                  ev.getEvaluate() + (StringUtils.isStringNullOrEmpty(ev.getEvaluateReason())
                      ? "" : (": " + ev.getEvaluateReason())));
            }
          }
        }
      }
      List<Long> tempSrIdsTotalSR = new ArrayList<>();
      List<String> tempSrCode = new ArrayList<>();
      for (int i = 0; i < lst.size(); i++) {
        tempSrCode.add(lst.get(i).getSrCode());
        if (SR_STATUS.CLOSED.equals(lst.get(i).getStatus())) {
          tempSrIdsTotalSR.add(lst.get(i).getSrId());
        }
        if ((i != 0 && i % 500 == 0) || i == lst.size() - 1) {
          //dungpv them hien thi sr_con tren gird
          if (tempSrCode != null && !tempSrCode.isEmpty()) {
            List<SrInsiteDTO> lstSRChild = srRepository
                .getListSRChildCodeByParentCode(tempSrCode);
            if (lstSRChild != null && lstSRChild.size() > 0) {
              lstSRChild.forEach(item -> {
                if (mapSrChildCode.containsKey(item.getParentCode())) {
                  String temp =
                      mapSrChildCode.get(item.getParentCode()) + "," + item.getSrCode();
                  mapSrChildCode.put(item.getParentCode(), temp);
                } else {
                  mapSrChildCode.put(item.getParentCode(), item.getSrCode());
                }
              });
            }
            tempSrCode.clear();
          }
          //end
        }
        if (export.equals(Constants.RESULT_EXPORT)) {
          if ((i != 0 && i % 500 == 0) || i == lst.size() - 1) {
            if (tempSrIdsTotalSR != null && !tempSrIdsTotalSR.isEmpty()) {
              List<SrInsiteDTO> lstTemp = srRepository.getTotalSRProcessTime(tempSrIdsTotalSR);
              if (lstTemp != null && lstTemp.size() > 0) {
                lstTemp.forEach(item -> {
                  mapTotalTime.put(item.getSrId(), item.getTotalSRProcessTime());
                });
              }
              tempSrIdsTotalSR.clear();
            }
          }
        }
      }
      for (SrInsiteDTO dto : lst) {
        setSlaReceiveTimeForList(dto, mapCountryDayOff);
        try {
//          sét trạng thái gia hạm
          if (StringUtils.isNotNullOrEmpty(dto.getStatusRenew())) {
            if ("0".equals(dto.getStatusRenew())) {
              dto.setStatusRenew(I18n.getLanguage("SR.statusRenew0"));
            } else if ("1".equals(dto.getStatusRenew())) {
              dto.setStatusRenew(I18n.getLanguage("SR.statusRenew1"));
            } else if ("2".equals(dto.getStatusRenew())) {
              dto.setStatusRenew(I18n.getLanguage("SR.statusRenew2"));
            }
          }

          if (!Constants.SR_STATUS.DRAFT.equals(dto.getStatus())) {
            dto.setRemainExecutionTimeCheckStatus(dto.getRemainExecutionTime());
            if (Double.valueOf(dto.getRemainExecutionTime()) >= 0.0) {
              dto.setEvaluate(I18n.getLanguage("SR.list.evaluate.inDeadline"));
            } else {
              dto.setEvaluate(I18n.getLanguage("SR.list.evaluate.outDeadline"));
            }
            if (!Constants.SR_STATUS.CLOSED.equals(dto.getStatus())
                && !Constants.SR_STATUS.CONCLUDED.equals(dto.getStatus())
                && !Constants.SR_STATUS.REJECTED.equals(dto.getStatus())
                && !Constants.SR_STATUS.CANCELLED.equals(dto.getStatus())) {
              if (Double.valueOf(dto.getRemainExecutionTime()) >= 0.01) {
                if (dto.getRemainExecutionTime().endsWith(".0") || !dto.getRemainExecutionTime()
                    .contains(".")) {
                  dto.setRemainExecutionTime(Long.valueOf(dto.getRemainExecutionTime()).toString());
                } else {
                  dto.setRemainExecutionTime(
                      Double.valueOf(dto.getRemainExecutionTime()).toString());
                }
              } else {
                dto.setRemainExecutionTime("0");
              }
            } else {
              dto.setRemainExecutionTime("N/A");
            }
          } else {
            dto.setEvaluate("N/A");
            dto.setRemainExecutionTime("N/A");
          }
          if (Double.valueOf(dto.getExecutionTime()) >= 0.01) {
            if (dto.getExecutionTime().endsWith(".0") || !dto.getExecutionTime().contains(".")) {
              dto.setExecutionTime(Long.valueOf(dto.getExecutionTime()).toString());
            } else {
              dto.setExecutionTime(Double.valueOf(dto.getExecutionTime()).toString());
            }
          } else {
            dto.setExecutionTime("0");
          }
          if (export.equals(Constants.RESULT_EXPORT)) {
            dto.setWorkLog(mapWorklog.get(dto.getSrId()));
            dto.setReason(mapWorklogReason.get(dto.getSrId()));
            if (Constants.SR_STATUS.CLOSED.equals(dto.getStatus())) {
              dto.setEvaluateReason(mapEvaluateDTO.get(String.valueOf(dto.getSrId())));
              if (StringUtils.isNotNullOrEmpty(dto.getEvaluateReason())) {
                String[] arrEvaluateReason = dto.getEvaluateReason().split(":");
                if (arrEvaluateReason != null && arrEvaluateReason.length > 0) {
                  dto.setReviewViewsClosed(arrEvaluateReason[0]);
                }
              }
            }
            // set thời gian chuyển trạng thái từ new về closed
            String totalTimeStr = mapTotalTime.get(dto.getSrId());
            Double timeTemp = totalTimeStr != null ? Double
                .parseDouble(totalTimeStr) : 0D;
            if (timeTemp >= 0.01) {
              dto.setTotalSRProcessTime(totalTimeStr);
            }
            //end
          }
          if (mapSrChildCode.containsKey(dto.getSrCode())) {
            String srChildCode = mapSrChildCode.get(dto.getSrCode());
            dto.setSrChildCode(srChildCode == null ? "" : srChildCode);
          }
          if (!Constants.SR_STATUS.DRAFT.equals(dto.getStatus())) {
            if (StringUtils.isStringNullOrEmpty(dto.getEvaluateReplyTime())) {
              if (DateUtil
                  .compareDateTime(DateTimeUtils.convertStringToDateTime(dto.getSlaReceiveTime()),
                      DateUtil.sysDate()) == 1) {
                //thoi gian tinh duoc > thoi gian hien tai
                dto.setEvaluateReplyTimeDisplay(I18n.getLanguage("SR.list.evaluate.inDeadline"));
              } else {
                dto.setEvaluateReplyTimeDisplay(I18n.getLanguage("SR.list.evaluate.outDeadline"));
              }
            } else {
              if (DateUtil
                  .compareDateTime(DateTimeUtils.convertStringToDateTime(dto.getSlaReceiveTime()),
                      DateTimeUtils.convertStringToDateTime(dto.getEvaluateReplyTime())) == 1) {
                //thoi gian tinh duoc > thoi gian tiep nhan thuc te
                dto.setEvaluateReplyTimeDisplay(I18n.getLanguage("SR.list.evaluate.inDeadline"));
              } else {
                dto.setEvaluateReplyTimeDisplay(I18n.getLanguage("SR.list.evaluate.outDeadline"));
              }
            }
          } else {
            dto.setEvaluateReplyTimeDisplay("N/A");
          }

          dto.setActualExecutionTime(
              StringUtils.isStringNullOrEmpty(dto.getActualExecutionTime()) ? ""
                  : (Double.valueOf(dto.getActualExecutionTime()) < 0.0 ? "0"
                      : dto.getActualExecutionTime()) + " " + ("en_US".equals(
                      I18n.getLocale()) && Double.valueOf(dto.getActualExecutionTime()) > 1.0 ?
                      I18n.getLanguage("SR.unitDay") + "s"
                      : I18n.getLanguage("SR.unitDay"))
          );

          if (Constants.SR_STATUS.PLANNED.equals(dto.getStatus())) {
            String pattern = "dd/MM/yyyy HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            SRHisDTO dtoHis = new SRHisDTO();
            dtoHis.setSrId(dto.getSrId().toString());
            dtoHis.setSrStatus(dto.getStatus());
            List<SRHisDTO> lstHis = srHisRepository
                .getListSRHisDTO(dtoHis, 0, 0, "asc", "createdTime");
            if (lstHis != null && !lstHis.isEmpty()) {
              Date datePlanned = DateUtil
                  .string2DateByPattern(lstHis.get(0).getCreatedTime(), pattern);
              Date dateStart = DateUtil
                  .string2DateByPattern(sdf.format(dto.getStartTime()), "dd/MM/yyyy HH:mm");
              if (datePlanned != null && dateStart != null && dto.getCrWoCreatTime() != null) {
                Long timePlanned = datePlanned.getTime();
                Long timeStart = dateStart.getTime();
                if ((timePlanned - timeStart) / 86400000 >= dto.getCrWoCreatTime()) {
                  dto.setCreatCRWO(I18n.getLanguage("SR.list.creatCRWO.overdue"));
                } else {
                  dto.setCreatCRWO(I18n.getLanguage("SR.list.creatCRWO.indue"));
                }
              }
            }
          }

          lstResult.add(dto);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    }
    datatable.setData(lstResult);
    return datatable;
  }

  private void setSlaReceiveTimeForList(SrInsiteDTO srDTO,
      Map<String, List<Date>> mapCountryDayOff) {
    if (!mapCountryDayOff.containsKey(srDTO.getCountry())) {
      List<Date> lstDate = srRepository
          .getDayOffForExecutionTime(srDTO.getCountry());
      mapCountryDayOff.put(srDTO.getCountry(), lstDate);
    }
    try {
      List<Date> lstDate = mapCountryDayOff.get(srDTO.getCountry());
      Calendar start = Calendar.getInstance();
      String pattern = "dd/MM/yyyy HH:mm:ss";
      SimpleDateFormat sdf = new SimpleDateFormat(pattern);
      String startTime = sdf.format(srDTO.getStartTime());
      String dd = DateTimeUtils.convertDateToString(
          DateTimeUtils.convertStringToDateTime(startTime + ":00"), "dd");
      start.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dd));
      start.setTime(DateTimeUtils.convertStringToDateTime(startTime + ":00"));
      String slaTime = setReceiveSLATime(start, srDTO.getReplyTime(), lstDate);
      srDTO.setSlaReceiveTime(slaTime);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private String setReceiveSLATime(Calendar start, String replyTime, List<Date> lstDate) {
    Calendar receiveTime = Calendar.getInstance();
    receiveTime.setTime(start.getTime());
    if (StringUtils.isNotNullOrEmpty(replyTime)) {
      String[] arrReplyTime = replyTime.split("\\.");
      if (arrReplyTime != null && arrReplyTime.length > 0) {
        if (arrReplyTime.length == 1) {
          receiveTime.add(Calendar.DAY_OF_MONTH, Integer.valueOf(arrReplyTime[0]));
        } else if (arrReplyTime.length == 2) {
          if ("5".equals(arrReplyTime[1])) {
            //13h30
            Calendar oneThirty = Calendar.getInstance();
            oneThirty.setTime(start.getTime());
            oneThirty.set(Calendar.HOUR_OF_DAY, 13);
            oneThirty.set(Calendar.MINUTE, 30);
            oneThirty.set(Calendar.SECOND, 0);

            //17h30
            Calendar fiveThirty = Calendar.getInstance();
            fiveThirty.setTime(start.getTime());
            fiveThirty.set(Calendar.HOUR_OF_DAY, 17);
            fiveThirty.set(Calendar.MINUTE, 30);
            fiveThirty.set(Calendar.SECOND, 0);

            //0h
            Calendar zero = Calendar.getInstance();
            zero.setTime(start.getTime());
            zero.set(Calendar.HOUR_OF_DAY, 0);
            zero.set(Calendar.MINUTE, 0);
            zero.set(Calendar.SECOND, 0);

            //8h
            Calendar eight = Calendar.getInstance();
            eight.setTime(start.getTime());
            eight.set(Calendar.HOUR_OF_DAY, 8);
            eight.set(Calendar.MINUTE, 0);
            eight.set(Calendar.SECOND, 0);

            //12h
            Calendar twelve = Calendar.getInstance();
            twelve.setTime(start.getTime());
            twelve.set(Calendar.HOUR_OF_DAY, 12);
            twelve.set(Calendar.MINUTE, 0);
            twelve.set(Calendar.SECOND, 0);

            //24h
            Calendar twentyFour = Calendar.getInstance();
            twentyFour.setTime(start.getTime());
            twentyFour.set(Calendar.HOUR_OF_DAY, 24);
            twentyFour.set(Calendar.MINUTE, 0);
            twentyFour.set(Calendar.SECOND, 0);

            if (start.getTime().getTime() - zero.getTime().getTime() > 0
                && eight.getTime().getTime() - start.getTime().getTime() >= 0) {
              //0h < start_time <= 8h
              receiveTime.set(Calendar.HOUR_OF_DAY, 12);
              receiveTime.set(Calendar.MINUTE, 0);
              receiveTime.set(Calendar.SECOND, 0);
            } else if (start.getTime().getTime() - eight.getTime().getTime() > 0
                && twelve.getTime().getTime() - start.getTime().getTime() >= 0) {
              //8h < start_time <= 12h
              receiveTime.add(Calendar.HOUR_OF_DAY, 5);
              receiveTime.add(Calendar.MINUTE, 30);
              receiveTime.set(Calendar.SECOND, 0);
            } else if (start.getTime().getTime() - twelve.getTime().getTime() > 0
                && oneThirty.getTime().getTime() - start.getTime().getTime() >= 0) {
              //12h < start_time <= 13h30
              receiveTime.set(Calendar.HOUR_OF_DAY, 17);
              receiveTime.set(Calendar.MINUTE, 0);
              receiveTime.add(Calendar.MINUTE, 30);
              receiveTime.set(Calendar.SECOND, 0);
            } else if (start.getTime().getTime() - oneThirty.getTime().getTime() > 0
                && fiveThirty.getTime().getTime() - start.getTime().getTime() >= 0) {
              //13h30 < start_time <= 17h30
              receiveTime.set(Calendar.HOUR_OF_DAY, 12);
              receiveTime.set(Calendar.MINUTE, 0);
              receiveTime.add(Calendar.MINUTE,
                  -(int) (fiveThirty.getTime().getTime() - start.getTime().getTime()) / (1000
                      * 60));
              receiveTime.add(Calendar.DAY_OF_MONTH, 1);
              receiveTime.set(Calendar.SECOND, 0);
            } else if (start.getTime().getTime() - fiveThirty.getTime().getTime() > 0
                && twentyFour.getTime().getTime() - start.getTime().getTime() >= 0) {
              //17h30 < start_time <= 24h
              receiveTime.set(Calendar.HOUR_OF_DAY, 12);
              receiveTime.set(Calendar.MINUTE, 0);
              receiveTime.set(Calendar.SECOND, 0);
              receiveTime.add(Calendar.DAY_OF_MONTH, 1);
            }
          }
          if (!"0".equals(arrReplyTime[0])) {
            receiveTime.add(Calendar.DAY_OF_MONTH, Integer.valueOf(arrReplyTime[0]));
          }
        }
      }
    }
    //ngay cuoi cung trung voi ngay nghi
    while (isDayOff(lstDate, receiveTime.getTime())) {
      receiveTime.add(Calendar.DAY_OF_MONTH, 1);
    }
    return DateTimeUtils.convertDateToString(receiveTime.getTime(), "dd/MM/yyyy HH:mm:ss");
  }

  private boolean isDayOff(List<Date> lstDayOff, Date date) {
    try {
      if (lstDayOff != null && !lstDayOff.isEmpty()) {
        for (Date dayOff : lstDayOff) {
          if (DateTimeUtils.convertDateToString(dayOff)
              .equalsIgnoreCase(DateTimeUtils.convertDateToString(date))) {
            return true;
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return false;
    }
    return false;
  }

  private File exportFileEx(List<SrInsiteDTO> lstData) throws Exception {
    String title = I18n.getLanguage("SR.export.title");
    String fileNameOut = SR_REPORT;
    String subTitle = I18n.getLanguage("SR.export.eportDate", DateTimeUtils.convertDateOffset());
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet = readerHeaderSheet("srCode", "srChildCode", "title",
        "statusName",
        "srUser", "pathSrUnit",
        "statusRenew", "dayRenew", "creatCRWO", "executionTime", "remainExecutionTime", "evaluate",
        "evaluateReplyTimeDisplay", "serviceArrayName", "serviceGroupName", "serviceName",
        "createdUser", "checkingUnit", "countryName", "sendDate", "startTime", "endTime",
        "updatedTime", "crNumber", "actualExecutionTime", "workLog",
        "flowExecuteName", "evaluateReason", "totalSRProcessTime", "createCrSlow", "countNok",
        "reviewViews", "reviewViewsClosed", "reason", "insertSource");
    Map<String, String> filedSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstData,
        I18n.getLanguage("SR.export.title"),
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.SR",
        lstHeaderSheet,
        filedSplit,
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

  private List<ConfigHeaderExport> readerHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  private boolean isLstConfigContainsValue(final List<SRConfigDTO> lstConfig, final String value) {
    return lstConfig.stream().filter(o -> value.equals(o.getConfigCode())).findFirst().isPresent();
  }

  @Override
  public List<SRCatalogDTO> getListCBBSeviceInsert(SrInsiteDTO srInsiteDTO) {
    log.info("Request to getListCBBSeviceInsert : {}", srInsiteDTO);
    List<SRCatalogDTO> lstResult = new ArrayList<>();
    SRCatalogDTO srCatalog = new SRCatalogDTO();
    if (srInsiteDTO.getSrUnit() != null && srInsiteDTO.getSrUnit() > 0) {
      srCatalog.setExecutionUnit(srInsiteDTO.getSrUnit().toString());
    } else {
      if (StringUtils.isNotNullOrEmpty(srInsiteDTO.getSrUser())) {
        SrInsiteDTO dto = new SrInsiteDTO();
        dto.setSrUser(srInsiteDTO.getSrUser());
        List<UnitDTO> lsUnit = srRepository.getListSRUnitForDetail(dto);
        if (lsUnit != null && !lsUnit.isEmpty()) {
          String unitId = "";
          for (UnitDTO item : lsUnit) {
            unitId += item.getUnitId() + ",";
          }
          if (unitId.endsWith(",")) {
            unitId = unitId.substring(0, unitId.length() - 1);
          }
          srCatalog.setExecutionUnit(unitId);
        }
      }
    }
    if (StringUtils.isNotNullOrEmpty(srInsiteDTO.getServiceArray())) {
      srCatalog.setServiceArray(srInsiteDTO.getServiceArray());
    }

    if (StringUtils.isNotNullOrEmpty(srInsiteDTO.getServiceGroup())) {
      srCatalog.setServiceGroup(srInsiteDTO.getServiceGroup());
    }
    if (StringUtils.isNotNullOrEmpty(srInsiteDTO.getState())) {
      if ("A".equals(srInsiteDTO.getState())) {
        srCatalog.setStatus("A");//Active
      } else if ("I".equals(srInsiteDTO.getState())) {
        srCatalog.setStatus("I");
      }
    }
    List<SRCatalogDTO> lstServices = srCatalogRepository2.getListSRCatalogDTO(srCatalog);
    if (lstServices != null && !lstServices.isEmpty()) {
      if (StringUtils.isNotNullOrEmpty(srInsiteDTO.getCountry())) {
        for (int i = lstServices.size() - 1; i > -1; i--) {
          if (!srInsiteDTO.getCountry().equals(lstServices.get(i).getCountry())) {
            lstServices.remove(i);
          }
        }
      }
      if (lstServices != null && lstServices.size() > 0) {
        Map<String, SRCatalogDTO> map = new HashMap<>();
        if (StringUtils.isNotNullOrEmpty(srInsiteDTO.getIsUpdateAble()) && "1"
            .equals(srInsiteDTO.getIsUpdateAble())) {
          lstResult.addAll(lstServices);
        } else {
          for (SRCatalogDTO dto : lstServices) {
            if (map.get(dto.getServiceCode() + "_" + dto.getServiceName()) == null) {
              map.put(dto.getServiceCode() + "_" + dto.getServiceName(), dto);
              lstResult.add(dto);
            }
          }
        }
      }
    }
    if (!StringUtils.isStringNullOrEmpty(srInsiteDTO.getRoleCode()) && !StringUtils
        .isStringNullOrEmpty(srInsiteDTO.getParentCode())) {
      //neu la SR con
      Map<String, SRCatalogChildDTO> mapChild = new HashMap<>();
      SRCatalogChildDTO catalogChild = new SRCatalogChildDTO();
      catalogChild.setServiceIdChild(Long.parseLong(srInsiteDTO.getServiceId()));
      List<SRCatalogChildDTO> lstChild = srCatalogChildRepository.getListCatalogChild(catalogChild);
      if (lstChild != null && !lstChild.isEmpty()) {
        for (SRCatalogChildDTO child : lstChild) {
          mapChild.put(child.getServiceCodeChild(), child);
        }
        for (SRCatalogDTO dto : lstServices) {
          if (mapChild.get(dto.getServiceCode()) != null) {
            lstResult.add(dto);
          }
        }
      }
    }
    return lstResult;
  }

  @Override
  public List<SRRoleUserInSideDTO> getCmbRoleCode(String unit, String service) {
    log.info("Request to getCmbRoleCode : {}", unit, service);
    List<SRRoleUserInSideDTO> lstResult = new ArrayList<>();
    if (StringUtils.isNotNullOrEmpty(service)) {
      SRCatalogDTO srCatalogDTO = srCatalogRepository2.findById(Long.parseLong(service));
      if (srCatalogDTO != null && srCatalogDTO.getRoleCode() != null) {
        SRRoleUserInSideDTO dtoRole = new SRRoleUserInSideDTO();
        dtoRole.setRoleCode(srCatalogDTO.getRoleCode());
        dtoRole.setCountry(srCatalogDTO.getCountry());
        dtoRole.setStatus("A");
        dtoRole.setUnitId(StringUtils.isNotNullOrEmpty(unit) ? Long.parseLong(unit) : null);
        List<SRRoleUserInSideDTO> lst = srCategoryServiceProxy.getlistSRRoleUserDTO(dtoRole);
        Map<String, String> map = new HashMap<>();
        for (SRRoleUserInSideDTO dto : lst) {
          if (map.get(dto.getRoleCode()) == null) {
            lstResult.add(dto);
            map.put(dto.getRoleCode(), dto.getRoleName());
          }
        }
      }
    }
    return lstResult;
  }

  @Override
  public List<SRRoleUserInSideDTO> getCmbSrUser(SRRoleUserInSideDTO srRoleUserDTO) {
    log.info("Request to getCmbSrUser : {}", srRoleUserDTO);
    List<SRRoleUserInSideDTO> lstRoleUser = new ArrayList<>();
    List<SRRoleUserInSideDTO> lstResult = new ArrayList<>();
    if (srRoleUserDTO.isInsertSR()) {
      if (StringUtils.isNotNullOrEmpty(srRoleUserDTO.getStatus()) && !Constants.SR_STATUS.DRAFT
          .equals(srRoleUserDTO.getStatus())
          && !Constants.SR_STATUS.UNDER_APPROVAL.equals(srRoleUserDTO.getStatus())) {
        SRRoleUserInSideDTO dto = new SRRoleUserInSideDTO();
        dto.setStatus("A");
        if (StringUtils.isNotNullOrEmpty(srRoleUserDTO.getCountry())) {
          dto.setCountry(srRoleUserDTO.getCountry());
        }
        if (!StringUtils.isStringNullOrEmpty(srRoleUserDTO.getUnitId())) {
          dto.setUnitId(srRoleUserDTO.getUnitId());
        }
        if (StringUtils.isNotNullOrEmpty(srRoleUserDTO.getRoleCode())) {
          dto.setRoleCode(srRoleUserDTO.getRoleCode());
        }
        lstRoleUser = srCategoryServiceProxy.getlistSRRoleUserDTO(dto);
      }
    } else {
      SRRoleUserInSideDTO dto = new SRRoleUserInSideDTO();
      dto.setCountry(srRoleUserDTO.getCountry());
      dto.setStatus("A");
      dto.setUnitId(srRoleUserDTO.getUnitId());
      dto.setRoleCode(srRoleUserDTO.getRoleCode());
      lstRoleUser = srCategoryServiceProxy.getlistSRRoleUserDTO(dto);
      if (StringUtils.isNotNullOrEmpty(srRoleUserDTO.getUsername())) {
        if (!lstRoleUser.stream().filter(o -> srRoleUserDTO.getUsername().equals(o.getUsername()))
            .findFirst().isPresent()) {
          lstRoleUser.add(srRoleUserDTO);
        }
      }
    }
    if (lstRoleUser != null && !lstRoleUser.isEmpty()) {
      Map<String, String> map = new HashMap<>();
      for (SRRoleUserInSideDTO dto : lstRoleUser) {
        if (map.get(dto.getUsername()) == null) {
          lstResult.add(dto);
          map.put(dto.getUsername(), dto.getUsername());
        }
      }
    }
    return lstResult;
  }

  @Override
  public SrInsiteDTO findNationByUnitId(Long unitId) {
    log.info("Request to findNationByUnitId : {}", unitId);
    UnitDTO unitDTO = unitRepository.findUnitById(unitId);
    return srRepository.findNationByLocationId(unitDTO.getLocationId());
  }

  @Override
  public SrInsiteDTO getSRDetail(SrInsiteDTO dto) {
    log.info("Request to getSRDetail : {}", dto);
    UserToken userToken = ticketProvider.getUserToken();
    SrInsiteDTO srInsiteDTO = srRepository.getDetail(dto.getSrId(), userToken.getUserName());
    GnocFileDto gnocFileDto = new GnocFileDto();
    gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
    gnocFileDto.setBusinessId(dto.getSrId());
    srInsiteDTO.setGnocFileDtos(gnocFileRepository.getListGnocFileForSR(gnocFileDto));
    srInsiteDTO.setSrHisDTOList(srHisRepository.getListSRHisDTOBySrId(dto.getSrId()));
    srInsiteDTO.setSrWorkLogDTOList(srWorkLogRepository.getListSRWorklogWithUnit(dto.getSrId()));
    initSRRole(false, srInsiteDTO);
    if (StringUtils.isNotNullOrEmpty(dto.getRemainExecutionTimeCheckStatus())) {
      srInsiteDTO.setRemainExecutionTimeCheckStatus(dto.getRemainExecutionTimeCheckStatus());
    }
    if (srInsiteDTO.getIsForceClosed() != null && srInsiteDTO.getIsForceClosed() == 1L
        && !SR_STATUS.CLOSED.equals(srInsiteDTO.getStatus())
        && !SR_STATUS.NEW.equals(srInsiteDTO.getStatus())
        && !SR_STATUS.CANCELLED.equals(srInsiteDTO.getStatus())
        && !SR_STATUS.REJECTED.equals(srInsiteDTO.getStatus())
        && !SR_STATUS.CONCLUDED.equals(srInsiteDTO.getStatus())) {
      srInsiteDTO.setBtnClosedSR(true);
    }
    return srInsiteDTO;
  }

  @Override
  public List<SRConfigDTO> getByConfigGroup(String configGroup) {
    log.info("Request to getByConfigGroup : {}", configGroup);
    return srConfigRepository.getByConfigGroup(configGroup);
  }

  @Override
  public List<SRWorklogTypeDTO> getBySRStatus(String srStatus) {
    log.info("Request to srStatus : {}", srStatus);
    List<SRWorklogTypeDTO> lstWorkLog = new ArrayList<>();
    if (StringUtils.isNotNullOrEmpty(srStatus)) {
      SRWorklogTypeDTO searchWorlogType = new SRWorklogTypeDTO();
      searchWorlogType.setStatus("A");
      searchWorlogType.setSrStatus(srStatus);
      lstWorkLog = srWorkLogRepository.getBySRStatus(searchWorlogType);
      try {
        Map<String, Object> map = DataUtil
            .getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.SR,
                Constants.APPLIED_BUSSINESS.WORKLOG_TYPE, I18n.getLocale());
        String sqlLanguage = (String) map.get("sql");
        Map mapParam = (Map) map.get("mapParam");
        Map mapType = (Map) map.get("mapType");
        List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
            .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
        lstWorkLog = DataUtil.setLanguage(lstWorkLog, lstLanguage, "wlTypeId", "wlTypeName");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return lstWorkLog;
  }

  @Override
  public List<SRWorkLogDTO> getListSRWorklogWithUnit(Long srId) {
    log.info("Request to getListSRWorklogWithUnit : {}", srId);
    List<SRWorkLogDTO> lstResult = new ArrayList<>();
    Map<String, SRWorklogTypeDTO> mapWorklogType = new HashMap<>();
    List<SRWorklogTypeDTO> lstWorklogType = srWorkLogRepository
        .getListSRWorklogTypeDTO(new SRWorklogTypeDTO(), 0, 0, "asc", "wlTypeCode");
    for (SRWorklogTypeDTO dto : lstWorklogType) {
      mapWorklogType.put(dto.getWlTypeId().toString(), dto);
    }
    List<SRWorkLogDTO> lstWorklog = srWorkLogRepository.getListSRWorklogWithUnit(srId);
    if (lstWorklog != null) {
      for (SRWorkLogDTO item : lstWorklog) {
        if (mapWorklogType.get(item.getWlTypeId().toString()) != null) {
          item.setWlTypeName(mapWorklogType.get(item.getWlTypeId().toString()).getWlTypeName());
        }
        lstResult.add(item);
      }
    }
    return lstResult;
  }

  @Override
  public ResultInSideDto insertSRWorklog(SRWorkLogDTO srWorklogDTO) {
    log.info("Request to insertSRWorklog : {}", srWorklogDTO);
    UserToken userToken = ticketProvider.getUserToken();
    srWorklogDTO.setCreatedUser(userToken.getUserName());
    srWorklogDTO.setCreatedTime(new Date());
    return srWorkLogRepository.insertSRWorklog(srWorklogDTO);
  }

  @Override
  public List<SRReasonRejectDTO> getCmbReason(Long wlTypeId) {
    log.info("Request to getCmbReason : {}", wlTypeId);
    List<SRReasonRejectDTO> lstReuslt = new ArrayList<>();
    if (wlTypeId != null && wlTypeId > 0) {
      String reject = "";
      String cancel = "";
      List<SRWorklogTypeDTO> lstWorklogType = srWorkLogRepository
          .getListSRWorklogTypeDTO(new SRWorklogTypeDTO(), 0, 0, "asc", "wlTypeCode");
      Map<String, SRWorklogTypeDTO> mapWorklogType = new HashMap<>();
      for (SRWorklogTypeDTO dto : lstWorklogType) {
        mapWorklogType.put(dto.getWlTypeId().toString(), dto);
      }
      for (Map.Entry<String, SRWorklogTypeDTO> entry : mapWorklogType.entrySet()) {
        if (entry.getValue().getSrStatus().contains(Constants.SR_STATUS.REJECTED)) {
          reject = entry.getKey();
        }
        if (entry.getValue().getSrStatus().contains(Constants.SR_STATUS.CANCELLED)) {
          cancel = entry.getKey();
        }
      }
      if (reject.equals(String.valueOf(wlTypeId)) || cancel.equals(String.valueOf(wlTypeId))) {
        lstReuslt = srWorkLogRepository.getReasonByStatus(wlTypeId);
      }
    }
    return lstReuslt;
  }

  @Override
  public List<SRConfig2DTO> getCBBFileType(SrInsiteDTO srInsiteDTO) {
    log.info("Request to getCBBFileType : {}", srInsiteDTO);
    List<SRConfig2DTO> lstFileType = new ArrayList<>();
    List<SRConfig2DTO> lstFileQuery;
    SRConfig2DTO dto = new SRConfig2DTO();
    dto.setConfigGroup(Constants.SR_ROLE_UPDATE.FILE_TYPE);
    lstFileQuery = srConfig2Repository.getFile(dto);
    //bỏ file biểu mẫu
    if (lstFileQuery != null && !lstFileQuery.isEmpty()) {
      int size = lstFileQuery.size();
      for (int i = size - 1; i > -1; i--) {
        if (Constants.SR_ROLE_UPDATE.FILE_TYPE_FORM.equals(lstFileQuery.get(i).getConfigCode())) {
          lstFileQuery.remove(i);
        }
      }
    }
    lstFileType.addAll(lstFileQuery == null ? new ArrayList<>() : lstFileQuery);
    if (srInsiteDTO.isOpenConnect()) {
      dto.setServiceGroup(Constants.SR_ROLE_UPDATE.FILE_TYPE_OPEN_CONNECT);
      lstFileQuery = srConfig2Repository.getFile(dto);
      lstFileType.addAll(lstFileQuery == null ? new ArrayList<>() : lstFileQuery);
    }
    if (srInsiteDTO.isServiceNims()) {
      dto.setServiceGroup(Constants.SR_ROLE_UPDATE.NIMS);
      lstFileQuery = srConfig2Repository.getFile(dto);
      lstFileType.addAll(lstFileQuery == null ? new ArrayList<>() : lstFileQuery);
    }
    if (srInsiteDTO.isServiceAom()) {
      dto.setServiceGroup(Constants.SR_ROLE_UPDATE.AOM);
      lstFileQuery = srConfig2Repository.getFile(dto);
      lstFileType.addAll(lstFileQuery == null ? new ArrayList<>() : lstFileQuery);
    }
    if (srInsiteDTO.isAutoCreatCR()) {
      dto.setServiceGroup(Constants.SR_ROLE_UPDATE.FILE_TYPE_TOOL);
      lstFileQuery = srConfig2Repository.getFile(dto);
      lstFileType.addAll(lstFileQuery == null ? new ArrayList<>() : lstFileQuery);
    }
    SrWsToolCrDTO wsToolCrDTO = checkList(srInsiteDTO.getServiceId());
    if (lstFileType != null && !lstFileType.isEmpty()) {
      lstFileType.get(0).setSrWsToolCrDTO(wsToolCrDTO);
    }
    return lstFileType;
  }

  @Override
  public ResultInSideDto deleteSRFile(GnocFileDto gnocFileDto) {
    log.debug("Request to deleteSRFile: {}", gnocFileDto);
    if (gnocFileDto.isOpenConnect() && Constants.SR_ROLE_UPDATE.FILE_TYPE_OPEN_CONNECT
        .equals(gnocFileDto.getFileType())) {
      GnocFileDto fileDto = new GnocFileDto();
      fileDto.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
      fileDto.setBusinessId(gnocFileDto.getBusinessId());
      List<GnocFileDto> gnocFileKys = gnocFileRepository.getListGnocFileByDto(fileDto);
      gnocFileKys.forEach(f -> {
        //cac file tu VIPA tra ve co CONTENT
        if (!StringUtils.isStringNullOrEmpty(f.getComments()) && !StringUtils
            .isStringNullOrEmpty(f.getContent())) {
          f.setBusinessCode(GNOC_FILE_BUSSINESS.GNOC_FILE_SR_DEL);
          gnocFileRepository.updateGnocFile(f);
          srRepository.deleteSRFile(f.getMappingId());
        }
      });
    }
    //Start delete file old
    srRepository.deleteSRFile(gnocFileDto.getMappingId());
    //End delete file old
    gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.GNOC_FILE_SR_DEL);
    return gnocFileRepository.updateGnocFile(gnocFileDto);
  }

  @Override
  public ResultInSideDto insertSRFile(List<MultipartFile> srFileList, SrInsiteDTO srInsiteDTO)
      throws IOException {
    log.info("Request to insertSRFile : {}", srFileList, srInsiteDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    UsersInsideDto unitToken = userRepository.getUserDTOByUserName(userToken.getUserName());
    List<SRMopDTO> lstMopTmp = new ArrayList<>();
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    if (srInsiteDTO.getGnocFileDtosAdd() == null || srInsiteDTO.getGnocFileDtosAdd().isEmpty()) {
      resultInSideDto.setKey(RESULT.FAIL);
      resultInSideDto.setMessage(I18n.getValidation("common.caption.fileUpload") + " " + I18n
          .getValidation("common.notnull"));
      return resultInSideDto;
    }
    if (StringUtils.isStringNullOrEmpty(srInsiteDTO.getGnocFileDtosAdd().get(0).getFileType())) {
      resultInSideDto.setKey(RESULT.FAIL);
      resultInSideDto.setMessage(I18n.getValidation("sr.file.list.fileType.notnull"));
    } else {
      if (srInsiteDTO.getGnocFileDtosAdd() != null && !srInsiteDTO.getGnocFileDtosAdd().isEmpty()) {
        String pathFileImport1 = "";
        for (GnocFileDto gnocFileDtoAdd : srInsiteDTO.getGnocFileDtosAdd()) {
          if (gnocFileDtoAdd.getIndexFile() != null) {
            Date date = new Date();
            MultipartFile multipartFile = srFileList.get(gnocFileDtoAdd.getIndexFile().intValue());
            String fullPath = FileUtils
                .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                    multipartFile.getBytes(), date);
            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setBytes(multipartFile.getBytes());
            gnocFileDto.setPath(fullPath);
            if (StringUtils.isStringNullOrEmpty(gnocFileDtoAdd.getFileName())) {
              gnocFileDto.setFileName(multipartFile.getOriginalFilename());
            } else {
              gnocFileDto.setFileName(gnocFileDtoAdd.getFileName());
            }
            gnocFileDto.setCreateUnitId(unitToken.getUnitId());
            gnocFileDto.setCreateUnitName(unitToken.getUnitName());
            gnocFileDto.setCreateUserId(userToken.getUserID());
            gnocFileDto.setCreateUserName(userToken.getUserName());
            gnocFileDto.setCreateTime(date);
            gnocFileDto.setFileType(gnocFileDtoAdd.getFileType());
            gnocFileDto.setTemplateId(gnocFileDtoAdd.getTemplateId());
            gnocFileDto.setMappingId(null);
            gnocFileDtos.add(gnocFileDto);
          }
        }
        if (gnocFileDtos != null && !gnocFileDtos.isEmpty()) {
          pathFileImport1 = FileUtils
              .saveTempFile(gnocFileDtos.get(0).getFileName(), gnocFileDtos.get(0).getBytes(),
                  tempFolder);
        }
        GnocFileDto fileTmp = new GnocFileDto();
        fileTmp.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
        fileTmp.setBusinessId(srInsiteDTO.getSrId());
        List<GnocFileDto> lstFileTmp = gnocFileRepository.getListGnocFileForSR(fileTmp);
        if (lstFileTmp != null && lstFileTmp.size() > 0) {
          List<String> fileTypeOpen = new ArrayList<>();
          for (GnocFileDto item : lstFileTmp) {
            fileTypeOpen.add(item.getFileType());
          }
          if (fileTypeOpen.contains(gnocFileDtos.get(0).getFileType())
              && Constants.SR_ROLE_UPDATE.FILE_TYPE_OPEN_CONNECT
              .equals(gnocFileDtos.get(0).getFileType())) {
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(I18n.getValidation("sr.tab.attach.fileopen") + " " + I18n
                .getValidation("common.dupplicate"));
            return resultInSideDto;
          }
        }
        SRCatalogDTO srCatalogDTO = srCatalogRepository2
            .findById(Long.parseLong(srInsiteDTO.getServiceId()));
        GnocFileDto fileCatalog = new GnocFileDto();
        fileCatalog.setBusinessCode(GNOC_FILE_BUSSINESS.SR_CATALOG);
        fileCatalog.setBusinessId(srInsiteDTO.getSrId());
//        List<GnocFileDto> lstFileBieuMau = gnocFileRepository
//            .getListGnocFileForSR(fileTmp);//danh sach file bieu mau
        //<editor-fold desc="Dich vu mo ket noi" defaultstate="collapsed">
        if (srInsiteDTO.isOpenConnect()) {
          if (Constants.SR_ROLE_UPDATE.FILE_TYPE_FILE_KY
              .equals(gnocFileDtos.get(0).getFileType())) {
            if (gnocFileDtos.isEmpty()) {
              resultInSideDto.setKey(RESULT.FAIL);
              resultInSideDto.setMessage(
                  I18n.getValidation("sr.tab.attach.fileky.attach.notmultiple.and.notnull"));
              return resultInSideDto;
            } else {
              for (GnocFileDto filesDTO : gnocFileDtos) {
                if (!filesDTO.getFileName().toLowerCase().endsWith(".pdf")) {
                  resultInSideDto.setKey(RESULT.FAIL);
                  resultInSideDto.setMessage(
                      I18n.getValidation("sr.ui.upload.fileky.invalidExtension")
                          .replace("[0]", filesDTO.getFileName()));
                  return resultInSideDto;
                }
              }
            }
          } else if (Constants.SR_ROLE_UPDATE.FILE_TYPE_OPEN_CONNECT
              .equals(gnocFileDtos.get(0).getFileType())) {
            if (gnocFileDtos.isEmpty()) {
              resultInSideDto.setKey(RESULT.FAIL);
              resultInSideDto.setMessage(
                  I18n.getValidation("sr.tab.attach.fileopen.notnull"));
              return resultInSideDto;
            } else if (gnocFileDtos.size() > 1) {
              resultInSideDto.setKey(RESULT.FAIL);
              resultInSideDto.setMessage(
                  I18n.getValidation("sr.tab.attach.fileopen.attach.notmultiple.and.notnull"));
              return resultInSideDto;
            } else {
              ResultInSideDto validate = validateFileImportVipa(pathFileImport1);
              if (validate.getCheck()) {
                //dungpv edit 25/08/2020 SR MKN
                ResultInSideDto validateContentsFileOpenConnect = validateFileOpenConnect(
                    pathFileImport1);
                if (!RESULT.SUCCESS.equals(validateContentsFileOpenConnect.getKey())) {
                  return validateContentsFileOpenConnect;
                }
                //end
              } else {
                resultInSideDto.setKey(RESULT.FAIL);
                resultInSideDto.setMessage(validate.getMessage());
                return resultInSideDto;
              }
            }
          }
        } //</editor-fold>
        //<editor-fold desc="Dich vu khach hang trung ke" defaultstate="collapsed">
        else if (srInsiteDTO.isDvTrungKe() && Constants.SR_ROLE_UPDATE.FILE_TYPE_FORM
            .equals(gnocFileDtos.get(0).getFileType())) {
          //vaildate khong duoc dinh kem nhieu file FORM va file FORM ko dung dinh dang excel
          if (gnocFileDtos.size() > 1) {
            resultInSideDto.setKey(RESULT.FAIL);
            resultInSideDto.setMessage(I18n.getValidation("sr.tab.attach.fileForm.notmutiple"));
            return resultInSideDto;
          }
          if (!gnocFileDtos.get(0).getPath().toLowerCase().endsWith(".xlsx")) {
            resultInSideDto.setKey(RESULT.FAIL);
            resultInSideDto.setMessage(
                I18n.getValidation("sr.tab.attach.fileForm.dvTrungKe") + " " + I18n
                    .getValidation("sr.tab.attach.invalidExtension"));
            return resultInSideDto;
          }
          List<SRConfigDTO> lstMetro = srConfigRepository
              .getByConfigGroup(Constants.SR_CATALOG.DICH_VU_TRUNG_KE_METRO);
          ResultInSideDto validateFileFormResult = new ResultInSideDto();
          if (lstMetro != null && !lstMetro.isEmpty() && lstMetro.get(0).getConfigCode()
              .equalsIgnoreCase(srCatalogDTO.getServiceCode())) {
            //dich vu trung ke metro, input 3 sheet file form
            validateFileFormResult = validateFileForm(pathFileImport1,
                Constants.SR_CATALOG.DICH_VU_TRUNG_KE_METRO);
          } else {
            //dich vu trung ke sdh, input 2 sheet file form
            validateFileFormResult = validateFileForm(pathFileImport1,
                Constants.SR_CATALOG.DICH_VU_TRUNG_KE_SDH);
          }
          if (RESULT.FAIL.equals(validateFileFormResult.getKey())) {
            return validateFileFormResult;
          }
        } //</editor-fold>
        //<editor-fold desc="Dich vu nims" defaultstate="collapsed">
        else if (srInsiteDTO.isServiceNims()) {
          if (Constants.SR_ROLE_UPDATE.NIMS.equals(gnocFileDtos.get(0).getFileType())) {
            if (gnocFileDtos.size() > 1) {
              resultInSideDto.setKey(RESULT.FAIL);
              resultInSideDto.setMessage(I18n.getValidation("sr.tab.attach.fileForm.notmutiple"));
              return resultInSideDto;
            }
            if (!gnocFileDtos.get(0).getPath().toLowerCase().endsWith(".xlsx")) {
              resultInSideDto.setKey(RESULT.FAIL);
              resultInSideDto.setMessage(
                  I18n.getValidation("sr.tab.attach.fileForm.dvNims") + " " + I18n
                      .getValidation("sr.tab.attach.invalidExtension"));
              return resultInSideDto;
            }

            if (lstFileTmp != null && !lstFileTmp.isEmpty()) {
              boolean isHaveFileForm = lstFileTmp.stream()
                  .filter(item -> Constants.SR_ROLE_UPDATE.NIMS.equals(item.getFileType()))
                  .findAny().isPresent();
              if (isHaveFileForm) {
                resultInSideDto.setKey(RESULT.FAIL);
                resultInSideDto.setMessage(I18n.getValidation("sr.template.nims.notmultiple"));
                return resultInSideDto;
              }
            }
            if (!validateFileDataWithFileName(pathFileImport1,
                I18n.getLanguage("sr.template.nims"))) {
              resultInSideDto.setKey(RESULT.FAIL);
              resultInSideDto.setMessage(I18n.getValidation("srRoleAction.import.errorTemplate"));
              return resultInSideDto;
            }
            resultInSideDto = validateFileDataNims(pathFileImport1);
            if (!resultInSideDto.getCheck()) {
              resultInSideDto.setKey(RESULT.FAIL);
              return resultInSideDto;
            }
          }
        } //</editor-fold>
        //<editor-fold desc="Dich vu aom" defaultstate="collapsed">
        else if (srInsiteDTO.isServiceAom()) {
          if (Constants.SR_ROLE_UPDATE.FILE_TYPE_TRINH_KY
              .equals(gnocFileDtos.get(0).getFileType())) {
            if (gnocFileDtos.isEmpty()) {
              resultInSideDto.setKey(RESULT.FAIL);
              resultInSideDto.setMessage(I18n.getValidation("sr.tab.attach.fileAom") + " " + I18n
                  .getValidation("sr.fileky.attach.notmultiple.and.notnull"));
              return resultInSideDto;
            } else {
              for (GnocFileDto fileName : gnocFileDtos) {
                if (!fileName.getFileName().toLowerCase().endsWith(".pdf")) {
                  resultInSideDto.setKey(RESULT.FAIL);
                  resultInSideDto
                      .setMessage(I18n.getValidation("sr.upload.file.trinhky.invalidExtension")
                          .replace("[0]", fileName.getFileName()));
                  return resultInSideDto;
                }
              }
            }
          } else if (Constants.SR_ROLE_UPDATE.AOM.equals(gnocFileDtos.get(0).getFileType())) {
            if (gnocFileDtos.size() > 1) {
              resultInSideDto.setKey(RESULT.FAIL);
              resultInSideDto.setMessage(I18n.getValidation("sr.tab.attach.fileAom.notmultiple"));
              return resultInSideDto;
            }
            if (!gnocFileDtos.get(0).getPath().toLowerCase().endsWith(".xlsx")) {
              resultInSideDto.setKey(RESULT.FAIL);
              resultInSideDto.setMessage(I18n.getValidation("sr.tab.attach.fileAom") + " " + I18n
                  .getValidation("sr.tab.attach.invalidExtension"));
              return resultInSideDto;
            }
            if (!validateFileDataWithFileName(pathFileImport1,
                I18n.getLanguage("sr.template.aom"))) {
              resultInSideDto.setKey(RESULT.FAIL);
              resultInSideDto.setMessage(I18n.getValidation("srRoleAction.import.errorTemplate"));
              return resultInSideDto;
            }
            //neu co file aom roi, thi ko dc phep up len nua
            if (lstFileTmp != null && !lstFileTmp.isEmpty()) {
              if (lstFileTmp.stream()
                  .filter(f -> Constants.SR_ROLE_UPDATE.AOM.equals(f.getFileType())).findAny()
                  .isPresent()) {
                resultInSideDto.setKey(RESULT.FAIL);
                resultInSideDto
                    .setMessage(I18n.getValidation("sr.tab.attach.fileGate.notmultiple"));
                return resultInSideDto;
              }
            }

            //<editor-fold desc="duongnt: goi ws validate file tu GatePro doi vs dich vu GatePro" defaultstate="collapsed">
            //validate bat buoc nhap
            ResultInSideDto validateFileGateProResult = validateFileGatePro(pathFileImport1);
            if (validateFileGateProResult.getCheck()) {
              List<SRConfigDTO> lstLink = srConfigRepository
                  .getByConfigGroup(Constants.SR_CONFIG.WS_GATE_URL);
              if (lstLink == null || lstLink.isEmpty()) {
                resultInSideDto.setKey(RESULT.FAIL);
                resultInSideDto.setMessage(I18n.getValidation("gate.ws.url.notConfig"));
                return resultInSideDto;
              }
              List<SRConfigDTO> lstUsername = srConfigRepository
                  .getByConfigGroup(Constants.SR_CONFIG.WS_GATE_USERNAME);
              String usernameGate = stringDecrypt(userNameGate, saltService);
              if (lstUsername != null && !lstUsername.isEmpty()) {
                usernameGate = lstUsername.get(0).getConfigCode();
              }
              String pGate = stringDecrypt(passGate, saltService);
              List<SRConfigDTO> lstPassword = srConfigRepository
                  .getByConfigGroup(Constants.SR_CONFIG.WS_GATE_P);
              if (lstPassword != null && !lstPassword.isEmpty()) {
                pGate = lstPassword.get(0).getConfigCode();
              }
              try {
                String byteArrayString = "";
                try {
                  byte[] encoded = Base64.encodeBase64(gnocFileDtos.get(0).getBytes());
                  byteArrayString = new String(encoded, StandardCharsets.US_ASCII);
                } catch (Exception ex) {
                  log.error(ex.getMessage(), ex);
                }
                URL url = new URL(lstLink.get(0).getConfigCode());
                int nTry = 0;
                com.viettel.gate.webservice.ResultDTO outputGate = null;
                while (nTry < 3) {
                  try {
                    log.info("\n Bat dau goi WS Gate-pro: " + DateTimeUtils.getSysDateTime());
                    log.info("\n WS Gate-pro: " + url);
                    log.info("Bat dau goi service Gate-pro: ");
                    com.viettel.gate.webservice.WSGateProService_Service service = new com.viettel.gate.webservice.WSGateProService_Service(
                        url);
                    log.info("\n Call service Gate-pro: " + service);
                    log.info("Bat dau goi port Gate-pro: ");
                    com.viettel.gate.webservice.WSGateProService port = service
                        .getWSGateProServicePort();
                    StringBuilder logGatePro = new StringBuilder();
                    log.info("\n Call port Gate-pro: " + port);
                    logGatePro.append("<web:validateCreateSR>\n"
                        + "         <userService>" + usernameGate + "</userService>\n"
                        + "         <passService>" + pGate + "</passService>\n"
                        + "         <byteArrayString>" + byteArrayString + "</byteArrayString>\n"
                        + "      </web:validateCreateSR>\n");
                    log.info("\n ====input GatePro====\n" + logGatePro.toString());
                    log.info("Bat dau goi ham validateCreateSR Gate-pro: ");
                    outputGate = port.validateCreateSR(usernameGate, pGate, byteArrayString);
                    log.info("\n ResultDTO Gate-pro: " + outputGate);
                    break;
                  } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    nTry++;
                    try {
                      Thread.sleep(1500);
                    } catch (Exception ex) {
                      log.error(ex.getMessage(), ex);
                    }
                  }
                }
                if (outputGate != null) {
                  int resultCode = outputGate.getResultCode();
                  System.out.println("Result code: " + resultCode);
                  if (resultCode == 1) {
                    resultInSideDto.setKey(RESULT.SUCCESS);
                    resultInSideDto.setMessage(I18n.getValidation("gate.file.validate.success"));
                  } else {
                    resultInSideDto.setKey(RESULT.FAIL);
                    resultInSideDto
                        .setMessage(
                            I18n.getValidation("gate.file.validate.error") + ": " + outputGate
                                .getResultMessage());
                  }
                  if (!StringUtils.isStringNullOrEmpty(outputGate.getResultData())) {
                    byte[] decoded = Base64.decodeBase64(outputGate.getResultData());
                    if (decoded != null) {
                      String fullPath = FileUtils
                          .saveTempFile("ImportFormGate_Result.xlsx", decoded, tempFolder);
                      resultInSideDto.setFilePath(fullPath);
                      if (resultCode != 1) {
                        return resultInSideDto;
                      }
                    } else {
                      resultInSideDto.setKey(RESULT.FAIL);
                      resultInSideDto.setMessage(I18n.getValidation("common.error"));
                      return resultInSideDto;
                    }
                  } else {
                    if (resultCode != 1) {
                      return resultInSideDto;
                    }
                  }
                } else {
                  resultInSideDto.setKey(RESULT.FAIL);
                  resultInSideDto.setMessage(I18n.getValidation("gate.file.validate.error"));
                  return resultInSideDto;
                }
              } catch (Exception e) {
                resultInSideDto.setMessage(I18n.getValidation("common.error"));
                log.error(e.getMessage(), e);
                resultInSideDto.setKey(RESULT.ERROR);
                return resultInSideDto;
              }
            } else {
              return validateFileGateProResult;
            }
            //</editor-fold>
          }

        } //</editor-fold>
        //<editor-fold desc="dich vu VMSA, AAM, VIPA" defaultstate="collapsed">
        else if (srInsiteDTO.isAutoCreatCR() && Constants.SR_ROLE_UPDATE.FILE_TYPE_TOOL
            .equals(gnocFileDtos.get(0).getFileType())) {
//          List<SRDownloadFormDTO> lstUpLoadTable = null;
          if (srFileList.size() == 0) {
            resultInSideDto.setKey(RESULT.FAIL);
            resultInSideDto.setMessage(I18n.getValidation("sr.import.fileImpl.null"));
            return resultInSideDto;
          }
          if (Constants.SR_ROLE_UPDATE.FILE_TYPE_TOOL.equals(gnocFileDtos.get(0).getFileType())) {

            SRConfigDTO sRConfigDTO = new SRConfigDTO();
            sRConfigDTO.setConfigCode(srCatalogDTO.getCountry());
            sRConfigDTO.setConfigGroup(Constants.SR_CONFIG.MAP_COUNTRY);
            List<SRConfigDTO> lstDataCountry = srConfigRepository.getDataByConfigCode(sRConfigDTO);

            SRConfigDTO configDto = new SRConfigDTO();
            configDto.setConfigCode(srCatalogDTO.getServiceArray());
            configDto.setConfigGroup(Constants.SR_CATALOG.SERVICE_ARRAY);
            List<SRConfigDTO> lstData = srConfigRepository.getDataByConfigCode(configDto);
            //<editor-fold desc="VMSA tao MOP va luu file" defaultstate="collapsed">
            if (Constants.SR_CONFIG.VMSA.equals(lstData.get(0).getAutomation())) {
              List<com.viettel.vmsa2.DtObjDelete> lstDelete = new ArrayList<>();
              Map<String, GnocFileDto> mapFile = new HashMap<>();
              List<com.viettel.vmsa2.FlowTemplateGNOCObj> lstTemplate = new ArrayList<>();
              if (gnocFileDtos != null) {
                for (GnocFileDto sRDTO : gnocFileDtos) {
                  mapFile.put(sRDTO.getTemplateId().toString(), sRDTO);
                  com.viettel.vmsa2.FlowTemplateGNOCObj flowDto = new com.viettel.vmsa2.FlowTemplateGNOCObj();
                  flowDto.setTemplateId(sRDTO.getTemplateId().toString());
                  try {
                    byte[] encoded = Base64.encodeBase64(sRDTO.getBytes());
                    String byteArrayString = new String(encoded, StandardCharsets.US_ASCII);
                    flowDto.setTempFileContent(byteArrayString);
                    lstTemplate.add(flowDto);
                  } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                  }
                }
              }
              try {
                List<SRConfigDTO> lstLink = srConfigRepository
                    .getByConfigGroup(Constants.SR_CONFIG.WS_VMSA_URL);

                List<SRConfigDTO> lstUsername = srConfigRepository
                    .getByConfigGroup(Constants.SR_CONFIG.WS_VMSA_USERNAME);
                String usernameVMSA = stringDecrypt(userNameVMSA, saltService);
                if (lstUsername != null && !lstUsername.isEmpty()) {
                  usernameVMSA = lstUsername.get(0).getConfigCode();
                }
                String pVMSA = stringDecrypt(passVMSA, saltService);
                List<SRConfigDTO> lstPassword = srConfigRepository
                    .getByConfigGroup(Constants.SR_CONFIG.WS_VMSA_P);
                if (lstPassword != null && !lstPassword.isEmpty()) {
                  pVMSA = lstPassword.get(0).getConfigCode();
                }

                URL url = new URL(lstLink.get(0).getConfigCode());
                com.viettel.vmsa2.ResultCreateDtByFileInput createDtResult = null;
                log.info("---------Start WS VMSA : createDtByFileInput link " + lstLink.get(0)
                    .getConfigCode());
                int nTry = 0;
                while (nTry < 3) {
                  try {
                    com.viettel.vmsa2.WSForGNOCImplService serviceVMSA = new com.viettel.vmsa2.WSForGNOCImplService(
                        url);
                    com.viettel.vmsa2.WSForGNOC port = serviceVMSA.getWSForGNOCImplPort();
                    createDtResult = port.createDtByFileInput(
                        usernameVMSA,
                        pVMSA,
                        lstDataCountry.get(0).getConfigName(),
                        crSystemId, lstDelete, lstTemplate);

                    break;
                  } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    nTry++;
                    try {
                      Thread.sleep(1000);
                    } catch (Exception ex) {
                      log.error(ex.getMessage(), ex);
                    }
                  }
                }
                //<editor-fold desc="LOG PARAM getListTemplatesByProcedure VMSA" defaultstate="collapsed">
                log.info("\n ---------URL " + lstLink.get(0).getConfigCode());
                StringBuilder logVMSA = new StringBuilder();
                logVMSA.append("\n <userService>" + usernameVMSA + "</userService>"
                    + "\n <passService>" + pVMSA + "</passService>"
                    + "\n <countryCode>" + lstDataCountry.get(0).getConfigName() + "</countryCode>"
                    + "\n <systemCreateId>" + crSystemId + "</systemCreateId>");
                if (lstDelete != null && !lstDelete.isEmpty()) {
                  for (com.viettel.vmsa2.DtObjDelete dtObjDelete : lstDelete) {
                    logVMSA.append(
                        "\n <listDtDelete>" + "\n <deleteDtId>" + dtObjDelete.getDeleteDtId()
                            + "</deleteDtId>" + "\n </listDtDelete>");
                  }
                }
                if (lstTemplate != null && !lstTemplate.isEmpty()) {
                  for (com.viettel.vmsa2.FlowTemplateGNOCObj flowTemp : lstTemplate) {
                    logVMSA.append("\n <flowTemplatesObj>" + "\n <tempFileContent>" + flowTemp
                        .getTempFileContent()
                        + "</tempFileContent>"
                        + "\n <templateId>" + flowTemp.getTemplateId() + "</templateId>"
                        + "\n </flowTemplatesObj>");
                  }
                }
                log.info(
                    "\n ---------PARAM createDtByFileInput VMSA START \n" + logVMSA.toString());
                log.info("---------PARAM createDtByFileInput VMSA END");
                //</editor-fold>
                if (createDtResult == null) {
                  resultInSideDto.setKey(RESULT.FAIL);
                  resultInSideDto.setMessage(I18n.getValidation("sr.notData"));
                  return resultInSideDto;
                } else {
                  if (createDtResult.getResultCode() == 1) {
                    resultInSideDto.setKey(RESULT.FAIL);
                    if (createDtResult.getListDtCreate() == null
                        || createDtResult.getListDtCreate().size() == 0) {
                      resultInSideDto.setMessage(createDtResult.getResultMessage());
                      return resultInSideDto;
                    }
                    for (com.viettel.vmsa2.DtObjDTO dtoReturn : createDtResult.getListDtCreate()) {
                      if (!StringUtils.isStringNullOrEmpty(dtoReturn)) {
                        GnocFileDto srFormDto = mapFile
                            .get(dtoReturn.getTemplateId());
                        if (dtoReturn.getResultCode() == 1) {
                          resultInSideDto.setKey(RESULT.FAIL);
                          resultInSideDto.setMessage(
                              " " + srFormDto.getFileName() + " " + ": " + dtoReturn
                                  .getResultMessage());
                          return resultInSideDto;
                        }
                      } else {
                        resultInSideDto.setKey(RESULT.FAIL);
                        resultInSideDto.setMessage(createDtResult.getResultMessage());
                        return resultInSideDto;
                      }
                    }
                  } else {
                    if (srFileList != null && !srFileList.isEmpty()) {
                      for (GnocFileDto sRDTO : gnocFileDtos) {
                        if (lstFileTmp != null && !lstFileTmp.isEmpty()
                            && sRDTO.getTemplateId() != null) {
                          if (lstFileTmp.stream().filter(
                              o -> o.getTemplateId() != null && o.getTemplateId()
                                  .equals(sRDTO.getTemplateId())).findAny().isPresent()) {
                            resultInSideDto.setKey(RESULT.ERROR);
                            resultInSideDto
                                .setMessage(I18n.getValidation("sr.attach.formTool.duplicate")
                                    .replace("{0}", sRDTO.getFileName()));
                            return resultInSideDto;
                          }
                        }
                      }
                    }
                    //insert file
                    if (srFileList != null && !srFileList.isEmpty()) {
                      for (GnocFileDto dto : gnocFileDtos) {
                        //Start save file old
                        String fullPathOld = FileUtils
                            .saveUploadFile(dto.getFileName(), dto.getBytes(), uploadFolder,
                                dto.getCreateTime());
                        SRFilesDTO srFilesDTO = new SRFilesDTO();
                        srFilesDTO.setFilePath(fullPathOld);
                        if (Constants.SR_ROLE_UPDATE.FILE_TYPE_TOOL.equals(dto.getFileType())) {
                          srFilesDTO.setFileName(dto.getFileName());
                        } else {
                          srFilesDTO.setFileName(FileUtils.getFileName(fullPathOld));
                        }
                        srFilesDTO.setFileType(dto.getFileType());
                        srFilesDTO.setTemplateId(dto.getTemplateId());
                        srFilesDTO.setObejctId(srInsiteDTO.getSrId());
                        srFilesDTO.setFileGroup(SR_CONFIG.FILE_GROUP_SR);
                        srFilesDTO.setTypeWS(Constants.SR_CONFIG.VMSA);
                        ResultInSideDto resultFileDataOld = srRepository.addSRFile(srFilesDTO);
                        //End save file old
                        dto.setTypeWs(Constants.SR_CONFIG.VMSA);
                        dto.setMappingId(resultFileDataOld.getId());
                      }
                      gnocFileRepository.saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SR,
                          srInsiteDTO.getSrId(), gnocFileDtos);
                    }
                    List<com.viettel.vmsa2.DtObjDTO> listDtCreate = createDtResult
                        .getListDtCreate();
                    if (listDtCreate != null && !listDtCreate.isEmpty()) {
                      for (com.viettel.vmsa2.DtObjDTO dtDTO : listDtCreate) {
                        SRMopDTO mopDto = new SRMopDTO();
                        mopDto.setDtId(dtDTO.getDtId());
                        mopDto.setDtName(dtDTO.getDtName());
                        mopDto.setTemplateId(dtDTO.getTemplateId());
                        mopDto.setSrId(srInsiteDTO.getSrId().toString());
                        mopDto.setType(Constants.SR_CONFIG.VMSA);
                        log.info(
                            "\n ---------List<com.viettel.vmsa2.DtObjDTO> listDtCreate \n" + dtDTO
                                .getDtId()
                                + "\n TYPE: " + Constants.SR_CONFIG.VMSA);
                        lstMopTmp.add(mopDto);
                      }
                    }
                  }
                }
              } catch (Exception ex) {
                log.error(ex.getMessage());
              }
            } //</editor-fold>
            //<editor-fold desc="AAM tao MOP va luu file" defaultstate="collapsed">
            else if (Constants.SR_CONFIG.AAM.equals(lstData.get(0).getAutomation())) {
              boolean checkFindNodeIP = false;
              List<SRMappingProcessCRDTO> listSRMappingCheck = srCategoryServiceProxy
                  .getListSRMappingProcessCRDTO(
                      new SRMappingProcessCRDTO(srCatalogDTO.getServiceCode()));
              if (listSRMappingCheck != null && !listSRMappingCheck.isEmpty()) {
                if (StringUtils.isNotNullOrEmpty(listSRMappingCheck.get(0).getTypeFindNode()) && "1"
                    .equals(listSRMappingCheck.get(0).getTypeFindNode())) {
                  checkFindNodeIP = true;
                }
              }
              List<com.viettel.aam2.DtObjDelete> lstDelete = new ArrayList<>();
              List<com.viettel.aam2.FlowTemplateGNOCObj> lstTemplate = new ArrayList<>();
              Map<String, GnocFileDto> mapFile = new HashMap<>();
              if (srFileList != null) {
                for (GnocFileDto sRDTO : gnocFileDtos) {
                  mapFile.put(sRDTO.getTemplateId().toString(), sRDTO);
                  com.viettel.aam2.FlowTemplateGNOCObj flowDto = new com.viettel.aam2.FlowTemplateGNOCObj();
                  flowDto.setTemplateId(sRDTO.getTemplateId().toString());
                  if (checkFindNodeIP) {
                    flowDto.setTypeFindNode("1");
                  } else {
                    flowDto.setTypeFindNode("0");
                  }
                  try {
                    byte[] encoded = Base64.encodeBase64(sRDTO.getBytes());
                    String byteArrayString = new String(encoded, StandardCharsets.US_ASCII);
                    flowDto.setTempFileContent(byteArrayString);
                    lstTemplate.add(flowDto);
                  } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                  }
                }
              }
              List<SRConfigDTO> lstLink = srConfigRepository
                  .getByConfigGroup(Constants.SR_CONFIG.WS_AAM_URL);

              List<SRConfigDTO> lstUsername = srConfigRepository
                  .getByConfigGroup(Constants.SR_CONFIG.WS_AAM_USERNAME);
              String usernameAAM = stringDecrypt(userNameAAM, saltService);
              if (lstUsername != null && !lstUsername.isEmpty()) {
                usernameAAM = lstUsername.get(0).getConfigCode();
              }
              String pAAM = stringDecrypt(passAAM, saltService);
              List<SRConfigDTO> lstPassword = srConfigRepository
                  .getByConfigGroup(Constants.SR_CONFIG.WS_AAM_P);
              if (lstPassword != null && !lstPassword.isEmpty()) {
                pAAM = lstPassword.get(0).getConfigCode();
              }

              URL url = null;
              try {
                url = new URL(lstLink.get(0).getConfigCode());
                log.info("---------Start WS AAM : createDtByFileInput link " + lstLink.get(0)
                    .getConfigCode());
              } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
              }
              com.viettel.aam2.ResultCreateDtByFileInput createDtResult = null;
              int nTry = 0;
              while (nTry < 3) {
                try {
                  com.viettel.aam2.TdttWebserviceImplService serviceAAM = new com.viettel.aam2.TdttWebserviceImplService(
                      url);
                  com.viettel.aam2.TdttWebservice port = serviceAAM.getTdttWebserviceImplPort();
                  createDtResult = port.createDtByFileInput(
                      usernameAAM,
                      pAAM,
                      lstDataCountry.get(0).getConfigName(),
                      crSystemId, lstDelete, lstTemplate);
                  break;
                } catch (Exception e) {
                  log.error(e.getMessage());
                  nTry++;
                  try {
                    Thread.sleep(1000);
                  } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                  }
                }
              }
              //<editor-fold desc="LOG PARAM getListTemplatesByProcedure AAM" defaultstate="collapsed">
              log.info("\n ---------URL " + lstLink.get(0).getConfigCode());
              StringBuilder logAAM = new StringBuilder();
              logAAM.append("\n <userService>" + usernameAAM + "</userService>"
                  + "\n <passService>" + pAAM + "</passService>"
                  + "\n <countryCode>" + lstDataCountry.get(0).getConfigName() + "</countryCode>"
                  + "\n <systemCreateId>" + crSystemId + "</systemCreateId>");
              if (lstDelete != null && !lstDelete.isEmpty()) {
                for (com.viettel.aam2.DtObjDelete dtObjDelete : lstDelete) {
                  logAAM.append(
                      "\n <listDtDelete>" + "\n <deleteDtId>" + dtObjDelete.getDeleteDtId()
                          + "</deleteDtId>" + "\n </listDtDelete>");
                }
              }
              if (lstTemplate != null && !lstTemplate.isEmpty()) {
                for (com.viettel.aam2.FlowTemplateGNOCObj flowTemp : lstTemplate) {
                  logAAM.append("\n <flowTemplatesObj>" +
                      "\n <tempFileContent>" + flowTemp.getTempFileContent() + "</tempFileContent>"
                      + "\n <templateId>" + flowTemp.getTemplateId() + "</templateId>"
                      + "\n </flowTemplatesObj>");
                }
              }
              log.info("\n ---------PARAM createDtByFileInput AAM START \n " + logAAM.toString());
              log.info("---------PARAM createDtByFileInput AAM END");
              //</editor-fold>
              if (createDtResult == null) {
                resultInSideDto.setKey(RESULT.FAIL);
                resultInSideDto.setMessage(I18n.getValidation("sr.notData"));
                return resultInSideDto;
              } else {
                if (createDtResult.getResultCode() == 1) {
                  if (createDtResult.getListDtCreate() == null
                      || createDtResult.getListDtCreate().size() == 0) {
                    resultInSideDto.setKey(RESULT.FAIL);
                    resultInSideDto.setMessage(createDtResult.getResultMessage());
                    return resultInSideDto;
                  }
                  for (com.viettel.aam2.DtObjDTO dtoReturn : createDtResult.getListDtCreate()) {
                    if (!StringUtils.isStringNullOrEmpty(dtoReturn)) {
                      GnocFileDto srFormDto = mapFile
                          .get(dtoReturn.getTemplateId());
                      if (dtoReturn.getResultCode() == 1) {
                        resultInSideDto.setKey(RESULT.FAIL);
                        resultInSideDto.setMessage(
                            " " + srFormDto.getFileName() + " " + ": " + dtoReturn
                                .getResultMessage());
                        return resultInSideDto;
                      }
                    } else {
                      resultInSideDto.setKey(RESULT.FAIL);
                      resultInSideDto.setMessage(createDtResult.getResultMessage());
                      return resultInSideDto;
                    }
                  }
                } else {
                  if (gnocFileDtos != null && !gnocFileDtos.isEmpty()) {
                    for (GnocFileDto sRDTO : gnocFileDtos) {
                      if (lstFileTmp != null && !lstFileTmp.isEmpty()
                          && sRDTO.getTemplateId() != null) {
                        if (lstFileTmp.stream().filter(
                            o -> o.getTemplateId() != null && o.getTemplateId()
                                .equals(sRDTO.getTemplateId())).findAny().isPresent()) {
                          resultInSideDto.setKey(RESULT.ERROR);
                          resultInSideDto
                              .setMessage(I18n.getValidation("sr.attach.formTool.duplicate")
                                  .replace("{0}", sRDTO.getFileName()));
                          return resultInSideDto;
                        }
                      }
                    }
                  }
                  //insert file
                  if (gnocFileDtos != null && !gnocFileDtos.isEmpty()) {
                    for (GnocFileDto dto : gnocFileDtos) {
                      //Start save file old
                      String fullPathOld = FileUtils
                          .saveUploadFile(dto.getFileName(), dto.getBytes(), uploadFolder,
                              dto.getCreateTime());
                      SRFilesDTO srFilesDTO = new SRFilesDTO();
                      srFilesDTO.setFilePath(fullPathOld);
                      srFilesDTO.setFileName(FileUtils.getFileName(fullPathOld));
                      srFilesDTO.setFileType(dto.getFileType());
                      srFilesDTO.setTemplateId(dto.getTemplateId());
                      srFilesDTO.setObejctId(srInsiteDTO.getSrId());
                      srFilesDTO.setFileGroup(SR_CONFIG.FILE_GROUP_SR);
                      srFilesDTO.setTypeWS(Constants.SR_CONFIG.AAM);
                      ResultInSideDto resultFileDataOld = srRepository.addSRFile(srFilesDTO);
                      //End save file old
                      dto.setTypeWs(Constants.SR_CONFIG.AAM);
                      dto.setMappingId(resultFileDataOld.getId());
                    }
                    gnocFileRepository
                        .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SR, srInsiteDTO.getSrId(),
                            gnocFileDtos);
                  }
                  List<com.viettel.aam2.DtObjDTO> listDtCreate = createDtResult.getListDtCreate();
                  // insert mop
                  if (listDtCreate != null && !listDtCreate.isEmpty()) {
                    for (com.viettel.aam2.DtObjDTO dtDTO : listDtCreate) {
                      SRMopDTO mopDto = new SRMopDTO();
                      mopDto.setDtId(dtDTO.getDtId());
                      mopDto.setDtName(dtDTO.getDtName());
                      mopDto.setTemplateId(dtDTO.getTemplateId());
                      mopDto.setSrId(srInsiteDTO.getSrId().toString());
                      String nodeIp = null;
                      if (dtDTO.getListNode() != null && !dtDTO.getListNode().isEmpty()) {
                        nodeIp = dtDTO.getListNode().get(0).getNodeIp();
                      }
                      mopDto.setIpNode(nodeIp);
                      mopDto.setType(Constants.SR_CONFIG.AAM);
                      log.info(
                          "\n ---------List<com.viettel.vmsa2.DtObjDTO> listDtCreate \n" + dtDTO
                              .getDtId()
                              + "\n TYPE: " + Constants.SR_CONFIG.AAM);
                      lstMopTmp.add(mopDto);
                    }
                  }
                }
              }
            } //</editor-fold>
            //<editor-fold desc="VIPA tao MOP va luu file" defaultstate="collapsed">
            else if (Constants.SR_CONFIG.VIPA.equals(lstData.get(0).getAutomation())) {
              List<com.viettel.vipa2.DtObjDelete> lstDelete = new ArrayList<>();
//              List<SRDownloadFormDTO> lstFileUpLoad = itemContainerChooseFile.getItemIds();
              List<com.viettel.vipa2.FlowTemplateGNOCObj> lstTemplate = new ArrayList<>();
              Map<String, GnocFileDto> mapFile = new HashMap<>();

              if (srFileList != null) {
                for (GnocFileDto sRDTO : gnocFileDtos) {
                  //Dung kiểm tra xem sRDTO có null không
                  log.info("\n Vipa: " + sRDTO.getTemplateId().toString());
                  mapFile.put(sRDTO.getTemplateId().toString(), sRDTO);
                  com.viettel.vipa2.FlowTemplateGNOCObj flowDto = new com.viettel.vipa2.FlowTemplateGNOCObj();
                  flowDto.setTemplateId(sRDTO.getTemplateId().toString());
                  try {
                    byte[] encoded = Base64.encodeBase64(sRDTO.getBytes());
                    String byteArrayString = new String(encoded, StandardCharsets.US_ASCII);
                    flowDto.setTempFileContent(byteArrayString);
                    lstTemplate.add(flowDto);
                  } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                  }
                }
              }
              try {
                List<SRConfigDTO> lstLink = srConfigRepository
                    .getByConfigGroup(Constants.SR_CONFIG.WS_VIPA_URL);

                List<SRConfigDTO> lstUsername = srConfigRepository
                    .getByConfigGroup(Constants.SR_CONFIG.WS_VIPA_USERNAME);
                String usernameVIPA = stringDecrypt(userNameVIPA, saltService);
                if (lstUsername != null && !lstUsername.isEmpty()) {
                  usernameVIPA = lstUsername.get(0).getConfigCode();
                }
                String pVIPA = stringDecrypt(passVIPA, saltService);
                List<SRConfigDTO> lstPassword = srConfigRepository
                    .getByConfigGroup(Constants.SR_CONFIG.WS_VIPA_P);
                if (lstPassword != null && !lstPassword.isEmpty()) {
                  pVIPA = lstPassword.get(0).getConfigCode();
                }

                URL url = new URL(lstLink.get(0).getConfigCode());
                log.info("---------Start WS VIPA : createDtByFileInput link " + lstLink.get(0)
                    .getConfigCode());
                com.viettel.vipa2.ResultCreateDtByFileInput createDtResult = null;
                int nTry = 0;
                while (nTry < 3) {
                  try {
                    com.viettel.vipa2.WSForGNOCImplService serviceVIPA = new com.viettel.vipa2.WSForGNOCImplService(
                        url);
                    com.viettel.vipa2.WSForGNOC port = serviceVIPA.getWSForGNOCImplPort();
                    createDtResult = port.createDtByFileInput(
                        usernameVIPA,
                        pVIPA,
                        lstDataCountry.get(0).getConfigName(),
                        crSystemId, lstDelete, lstTemplate);

                    break;
                  } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    nTry++;
                    try {
                      Thread.sleep(1000);
                    } catch (Exception ex) {
                      log.error(ex.getMessage());
                    }
                  }
                }
                //<editor-fold desc="LOG PARAM getListTemplatesByProcedure VIPA" defaultstate="collapsed">

                log.info("\n ---------URL " + lstLink.get(0).getConfigCode());
                StringBuilder logVIPA = new StringBuilder();
                logVIPA.append("\n <userService>" + usernameVIPA + "</userService>"
                    + "\n <passService>" + pVIPA + "</passService>"
                    + "\n <countryCode>" + lstDataCountry.get(0).getConfigName() + "</countryCode>"
                    + "\n <systemCreateId>" + crSystemId + "</systemCreateId>");
                if (lstDelete != null && !lstDelete.isEmpty()) {
                  for (com.viettel.vipa2.DtObjDelete dtObjDelete : lstDelete) {
                    logVIPA
                        .append(
                            "\n <listDtDelete>" + "\n <deleteDtId>" + dtObjDelete.getDeleteDtId()
                                + "</deleteDtId>" + "\n </listDtDelete>");
                  }
                }
                if (lstTemplate != null && !lstTemplate.isEmpty()) {
                  for (com.viettel.vipa2.FlowTemplateGNOCObj flowTemp : lstTemplate) {
                    logVIPA.append("\n <flowTemplatesObj>" + "\n <tempFileContent>" + flowTemp
                        .getTempFileContent()
                        + "</tempFileContent>"
                        + "\n <templateId>" + flowTemp.getTemplateId() + "</templateId>"
                        + "\n </flowTemplatesObj>");
                  }
                }
                log.info(
                    "\n ---------PARAM createDtByFileInput VIPA START \n" + logVIPA.toString());
                log.info("---------PARAM createDtByFileInput VIPA END");
                //</editor-fold>
                if (createDtResult == null) {
                  resultInSideDto.setKey(RESULT.FAIL);
                  resultInSideDto.setMessage(I18n.getValidation("sr.notData"));
                  return resultInSideDto;
                } else {
                  if (createDtResult.getResultCode() == 1) {
                    if (createDtResult.getListDtCreate() == null
                        || createDtResult.getListDtCreate().size() == 0) {
                      resultInSideDto.setKey(RESULT.FAIL);
                      resultInSideDto.setMessage(createDtResult.getResultMessage());
                      return resultInSideDto;
                    }
                    for (com.viettel.vipa2.DtObjDTO dtoReturn : createDtResult.getListDtCreate()) {
                      if (!StringUtils.isStringNullOrEmpty(dtoReturn)) {
                        GnocFileDto srFormDto = mapFile
                            .get(dtoReturn.getTemplateId());
                        if (dtoReturn.getResultCode() == 1) {
                          log.info(
                              "\n srFormDtoVipa : " + (srFormDto != null ? srFormDto.getFileName()
                                  : null));
                          log.info("\n dtoReturnVipa : " + (dtoReturn != null ? dtoReturn
                              .getResultMessage() : null));
                          resultInSideDto.setKey(RESULT.FAIL);
                          resultInSideDto.setMessage(
                              " " + (srFormDto != null ? srFormDto.getFileName() : null) + " "
                                  + ": " + (dtoReturn != null ? dtoReturn.getResultMessage()
                                  : null));
                          return resultInSideDto;
                        }
                      } else {
                        resultInSideDto.setKey(RESULT.FAIL);
                        resultInSideDto.setMessage(createDtResult.getResultMessage());
                        return resultInSideDto;
                      }
                    }
                  } else {
                    if (srFileList != null && !srFileList.isEmpty()) {
                      for (GnocFileDto sRDTO : gnocFileDtos) {
                        if (lstFileTmp != null && !lstFileTmp.isEmpty()
                            && sRDTO.getTemplateId() != null) {
                          if (lstFileTmp.stream().filter(
                              o -> o.getTemplateId() != null && o.getTemplateId()
                                  .equals(sRDTO.getTemplateId())).findAny().isPresent()) {
                            resultInSideDto.setKey(RESULT.ERROR);
                            resultInSideDto.setMessage(
                                I18n.getValidation("sr.attach.formTool.duplicate")
                                    .replace("{0}", sRDTO.getFileName()));
                            return resultInSideDto;
                          }
                        }
                      }
                    }
                    //insert file
                    if (gnocFileDtos != null && !gnocFileDtos.isEmpty()) {
                      for (GnocFileDto dto : gnocFileDtos) {
                        //Start save file old
                        String fullPathOld = FileUtils
                            .saveUploadFile(dto.getFileName(), dto.getBytes(), uploadFolder,
                                dto.getCreateTime());
                        SRFilesDTO srFilesDTO = new SRFilesDTO();
                        srFilesDTO.setFilePath(fullPathOld);
                        srFilesDTO.setFileName(FileUtils.getFileName(fullPathOld));
                        srFilesDTO.setFileType(gnocFileDtos.get(0).getFileType());
                        srFilesDTO.setTemplateId(dto.getTemplateId());
                        srFilesDTO.setObejctId(srInsiteDTO.getSrId());
                        srFilesDTO.setFileGroup(SR_CONFIG.FILE_GROUP_SR);
                        srFilesDTO.setTypeWS(Constants.SR_CONFIG.VIPA);
                        ResultInSideDto resultFileDataOld = srRepository.addSRFile(srFilesDTO);
                        //End save file old
                        dto.setTypeWs(Constants.SR_CONFIG.VIPA);
                        dto.setFileType(gnocFileDtos.get(0).getFileType());
                        dto.setMappingId(resultFileDataOld.getId());
                      }
                      gnocFileRepository.saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SR,
                          srInsiteDTO.getSrId(), gnocFileDtos);
                    }
                    List<com.viettel.vipa2.DtObjDTO> listDtCreate = createDtResult
                        .getListDtCreate();
                    // insert mop
                    if (listDtCreate != null && !listDtCreate.isEmpty()) {
                      for (com.viettel.vipa2.DtObjDTO dtDTO : listDtCreate) {
                        SRMopDTO mopDto = new SRMopDTO();
                        mopDto.setDtId(dtDTO.getDtId());
                        mopDto.setDtName(dtDTO.getDtName());
                        mopDto.setTemplateId(dtDTO.getTemplateId());
                        mopDto.setSrId(srInsiteDTO.getSrId().toString());
                        mopDto.setType(Constants.SR_CONFIG.VIPA);
                        log.info(
                            "\n ---------List<com.viettel.vmsa2.DtObjDTO> listDtCreate \n" + dtDTO
                                .getDtId()
                                + "\n TYPE: " + Constants.SR_CONFIG.VIPA);
                        lstMopTmp.add(mopDto);
                      }
                    }
                  }
                }
              } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
              }
            }
            //</editor-fold>
          }
        }
        //</editor-fold>
        if (!Constants.SR_ROLE_UPDATE.FILE_TYPE_TOOL.equals(gnocFileDtos.get(0).getFileType())) {
          if (gnocFileDtos != null && gnocFileDtos.size() > 0) {
            for (GnocFileDto item : gnocFileDtos) {
              //Start save file old
              String fullPathOld = FileUtils
                  .saveUploadFile(item.getFileName(), item.getBytes(), uploadFolder,
                      item.getCreateTime());
              SRFilesDTO srFilesDTO = new SRFilesDTO();
              srFilesDTO.setFilePath(fullPathOld);
              srFilesDTO.setFileName(FileUtils.getFileName(fullPathOld));
              srFilesDTO.setFileType(item.getFileType());
              srFilesDTO.setTemplateId(item.getTemplateId());
              srFilesDTO.setObejctId(srInsiteDTO.getSrId());
              srFilesDTO.setFileGroup(SR_CONFIG.FILE_GROUP_SR);
              if (Constants.SR_ROLE_UPDATE.AOM.equals(gnocFileDtos.get(0).getFileType())) {
                try {
                  byte[] encoded = Base64.encodeBase64(item.getBytes());
                  String byteArrayString2 = new String(encoded, StandardCharsets.US_ASCII);
                  srFilesDTO.setFileContent(byteArrayString2);
                } catch (Exception ex) {
                  log.error(ex.getMessage());
                }
              }
              ResultInSideDto resultFileDataOld = srRepository.addSRFile(srFilesDTO);
              //End save file old
              item.setContent(srFilesDTO.getFileContent());
              item.setMappingId(resultFileDataOld.getId());
            }
            gnocFileRepository
                .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SR, srInsiteDTO.getSrId(),
                    gnocFileDtos);
          }
        }
      } else {
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage(I18n.getValidation("common.caption.fileUpload") + " " + I18n
            .getValidation("common.notnull"));
      }
    }
    if (lstMopTmp != null && !lstMopTmp.isEmpty()) {
      resultInSideDto.setLstResult(lstMopTmp);
      log.info("\n lstMopTmp" + lstMopTmp.get(0).getDtId());
      ResultInSideDto mopsFile = createFileTxt(lstMopTmp);
      if (mopsFile.getKey().equals(RESULT.SUCCESS)) {
        resultInSideDto
            .setFile(mopsFile.getFile() != null ? mopsFile.getFile() : null);
      } else {
        return mopsFile;
      }
    }
    return resultInSideDto;
  }

  private ResultInSideDto createFileTxt(List<SRMopDTO> srMopDTOS) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    FileWriter fileWriter = null;
    BufferedWriter writer = null;
    try {
      if (srMopDTOS != null && !srMopDTOS.isEmpty()) {
        String filePathTxt = tempFolder + File.separator + SR_CATALOG.MOP_FILE_TXT + ".txt";
        File file = new File(filePathTxt);
        ObjectMapper mapper = new ObjectMapper();
        String contents = mapper.writeValueAsString(srMopDTOS);
        log.info("\n lstMop :" + contents);
        if (file.exists()) {
          log.info("\n đã tồn tại file " + SR_CATALOG.MOP_FILE_TXT + ".txt");
          file.delete();
          file.createNewFile();
        }
        fileWriter = new FileWriter(file);
        writer = new BufferedWriter(fileWriter);
        writer.write(contents);
        writer.flush();
        log.info("\n CONTENTS FILE MOP " + SR_CATALOG.MOP_FILE_TXT + ".txt" + contents);
        writer.close();
        resultInSideDto.setKey(RESULT.SUCCESS);
        resultInSideDto.setFile(file);
        return resultInSideDto;
      }
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(e.getMessage());
      return resultInSideDto;
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
        if (fileWriter != null) {
          fileWriter.close();
        }
      } catch (IOException e) {
        log.error(e.getMessage());
      }
    }
    return resultInSideDto;
  }

  private ResultInSideDto validateFileGatePro(String path) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setCheck(true);
    // dungpv 25/08/2020 nang cap validate file AOM
    try {
      File fileImport = new File(path);
      List<Object[]> lstData = CommonImport.getDataFromExcelFile(
          fileImport,
          0,
          1,
          0,
          10,
          10);
      List<ErrorInfo> cellErrorList = new ArrayList<>();
      if (lstData == null || lstData.isEmpty()) {
        resultInSideDto.setMessage(I18n.getValidation("common.searh.nodata"));
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setCheck(false);
        return resultInSideDto;
      } else {
        int row = 1;
        int countEmpty = 0;
        for (Object[] obj : lstData) {
          if (StringUtils.isStringNullOrEmpty(obj[0]) && StringUtils.isStringNullOrEmpty(obj[1])
              && StringUtils.isStringNullOrEmpty(obj[2]) && StringUtils.isStringNullOrEmpty(obj[3])
              && StringUtils.isStringNullOrEmpty(obj[5]) && StringUtils.isStringNullOrEmpty(obj[7])
              && StringUtils.isStringNullOrEmpty(obj[8]) && StringUtils
              .isStringNullOrEmpty(obj[9]) || (!StringUtils.isStringNullOrEmpty(obj[0]) && I18n
              .getValidation("gate.file.Note").equals(obj[0].toString().trim()))) {
            countEmpty++;
            continue;
          }
          String tempErr = "";
          if (StringUtils.isStringNullOrEmpty(obj[7])) {
            cellErrorList
                .add(new ErrorInfo(row, I18n.getValidation("gate.file.action.notnull")));
            row++;
            continue;
          } else {
            if (!I18n.getLanguage("SR.FileAOM.actionNew").equals(obj[7].toString().trim()) && !I18n
                .getLanguage("SR.FileAOM.actionExtend").equals(obj[7].toString().trim()) && !I18n
                .getLanguage("SR.FileAOM.actionUpdate_IP_Client")
                .equals(obj[7].toString().trim())) {
              cellErrorList
                  .add(new ErrorInfo(row, I18n.getValidation("sr.gate.file.action.notExist")));
              row++;
              continue;
            }
          }
          if (StringUtils.isStringNullOrEmpty(obj[0])) {
            tempErr += I18n.getValidation("gate.file.username.notnull") + ",";
          }
          if (StringUtils.isStringNullOrEmpty(obj[2])) {
            tempErr += I18n.getValidation("gate.file.country.notnull") + ",";
          }
          if (I18n.getLanguage("SR.FileAOM.actionNew").equals(obj[7].toString().trim())) {
            if (StringUtils.isStringNullOrEmpty(obj[1])) {
              tempErr += I18n.getValidation("gate.file.ip.notnull") + ",";
            }
            if (StringUtils.isStringNullOrEmpty(obj[3])) {
              tempErr += I18n.getValidation("gate.file.firewall.notnull") + ",";
            }
            if (StringUtils.isStringNullOrEmpty(obj[5])) {
              tempErr += I18n.getValidation("gate.file.crView.notnull") + ",";
            }
            if (StringUtils.isStringNullOrEmpty(obj[8])) {
              tempErr += I18n.getValidation("gate.file.effectDate.notnull") + ",";
            }
            if (StringUtils.isStringNullOrEmpty(obj[9])) {
              tempErr += I18n.getValidation("gate.file.expiryDate.notnull") + ",";
            }
          } else if (I18n.getLanguage("SR.FileAOM.actionExtend").equals(obj[7].toString().trim())) {
            if (StringUtils.isStringNullOrEmpty(obj[6])) {
              tempErr += I18n.getValidation("gate.file.Policy_Code") + ",";
            }
            if (StringUtils.isStringNullOrEmpty(obj[8])) {
              tempErr += I18n.getValidation("gate.file.effectDate.notnull") + ",";
            }
            if (StringUtils.isStringNullOrEmpty(obj[9])) {
              tempErr += I18n.getValidation("gate.file.expiryDate.notnull") + ",";
            }
          } else if (I18n.getLanguage("SR.FileAOM.actionUpdate_IP_Client")
              .equals(obj[7].toString().trim())) {
            if (StringUtils.isStringNullOrEmpty(obj[10])) {
              tempErr += I18n.getValidation("gate.file.Update_IP_Client.notnull") + ",";
            }
          }
          if (StringUtils.isNotNullOrEmpty(tempErr) && tempErr.endsWith(",")) {
            tempErr = tempErr.substring(0, tempErr.length() - 1) + " " + I18n
                .getValidation("gate.file.null");
            cellErrorList
                .add(new ErrorInfo(row, tempErr));
          }
          row++;
        }
        // end
        if (countEmpty == lstData.size()) {
          resultInSideDto.setMessage(I18n.getValidation("sr.gate.fileFormEmpty"));
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto.setCheck(false);
          return resultInSideDto;
        }
        if (!cellErrorList.isEmpty()) {
          exportFileResult(lstData, I18n.getLanguage("sr.template.aom"), cellErrorList,
              resultInSideDto);
          resultInSideDto.setMessage(I18n.getValidation("vipa.error.message.importFail"));
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto.setCheck(false);
          return resultInSideDto;
        }
      }
    } catch (Exception e) {
      resultInSideDto.setMessage(e.getMessage());
      resultInSideDto.setKey(RESULT.FAIL);
      resultInSideDto.setCheck(false);
      return resultInSideDto;
    }
    return resultInSideDto;
  }

  private ResultInSideDto validateFileDataNims(String path) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setCheck(true);
    resultInSideDto.setKey(RESULT.SUCCESS);
    try {
      File fileImport = new File(path);
      // Lay du lieu import
      List<Object[]> lstData = ExcelWriterUtils.readExcelAddBlankRowXSSF(
          fileImport,
          0,//sheet
          1,//begin row
          0,//from column
          4,//to column
          30);
      List<ErrorInfo> cellErrorList = new ArrayList<>();
      if (lstData == null || lstData.isEmpty()) {
        resultInSideDto.setMessage("File " + I18n.getValidation("common.searh.nodata"));
        resultInSideDto.setCheck(false);
        return resultInSideDto;
      } else {
        if (lstData.size() <= 30) {
          int row = 1;
          for (Object[] obj : lstData) {
            if (obj[1] == null) {
              cellErrorList
                  .add(new ErrorInfo(row, I18n.getValidation("nims.file.location.notnull")));
              row++;
              continue;
            }
            if (obj[2] == null) {
              cellErrorList.add(new ErrorInfo(row, I18n.getValidation("nims.file.ip.notnull")));
              row++;
              continue;
            }
            if (obj[3] == null) {
              cellErrorList
                  .add(new ErrorInfo(row, I18n.getValidation("nims.file.system.notnull")));
              row++;
              continue;
            }
            if (obj[4] == null) {
              cellErrorList
                  .add(new ErrorInfo(row, I18n.getValidation("nims.file.purpose.notnull")));
              row++;
              continue;
            }
            row++;
          }
          if (!cellErrorList.isEmpty()) {
            exportFileResult(lstData, I18n.getLanguage("sr.template.nims"), cellErrorList,
                resultInSideDto);
            resultInSideDto.setMessage(I18n.getValidation("vipa.error.message.importFail"));
            resultInSideDto.setCheck(false);
            resultInSideDto.setKey(RESULT.FAIL);
            return resultInSideDto;
          }
        } else {
          exportFileResult(lstData, I18n.getLanguage("sr.template.nims"), cellErrorList,
              resultInSideDto);
          resultInSideDto.setMessage(I18n.getValidation("sr.attach.file.nims.validate20"));
          resultInSideDto.setCheck(false);
          resultInSideDto.setKey(RESULT.FAIL);
          return resultInSideDto;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setMessage(e.getMessage());
      resultInSideDto.setKey(RESULT.FAIL);
      resultInSideDto.setCheck(false);
      return resultInSideDto;
    }
    return resultInSideDto;
  }

  public ResultInSideDto validateFileForm(String pathFileForm, String type) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    try {
      String resVal = "";
      int typeNumber = Constants.SR_CATALOG.DICH_VU_TRUNG_KE_METRO.equals(type) ? 2 : 1;
      ExcelWriterUtils ewu = new ExcelWriterUtils();
      String templatePath = "templates"
          + File.separator;
      if (typeNumber == 2) {
        templatePath += I18n.getLanguage("sr.attach.file.formMetro");
      } else {
        templatePath += I18n.getLanguage("sr.attach.file.formOnlySDH");
      }
      //lay dinh dang file mau
      Workbook workBookTemplate = readFileExcel(templatePath);
      Sheet sheetTemplate1 = workBookTemplate.getSheetAt(0);
      Sheet sheetTemplate2 = workBookTemplate.getSheetAt(1);
      String arrHeaderTemplate3 = "";
      if (typeNumber == 2) {
        Sheet sheetTemplate3 = workBookTemplate.getSheetAt(2);
        Row rowTemplate3 = sheetTemplate3.getRow(0);
        Row row2Template3 = sheetTemplate3.getRow(1);
        int rT3 = rowTemplate3.getLastCellNum();
        for (int i = 0; i < rT3; i++) {
          if (rowTemplate3.getCell(i).getStringCellValue() != null) {
            arrHeaderTemplate3 += "," + rowTemplate3.getCell(i).getStringCellValue().trim();
          }
        }
        rT3 = row2Template3.getLastCellNum();
        for (int i = 0; i < rT3; i++) {
          if (row2Template3.getCell(i).getStringCellValue() != null) {
            arrHeaderTemplate3 += "," + row2Template3.getCell(i).getStringCellValue().trim();
          }
        }
      }
      Row rowTemplate1 = sheetTemplate1.getRow(0);
      Row rowTemplate2 = sheetTemplate2.getRow(0);

      String arrHeaderTemplate1 = "";
      String arrHeaderTemplate2 = "";

      int rT1 = rowTemplate1.getLastCellNum();
      for (int i = 0; i < rT1; i++) {
        if (rowTemplate1.getCell(i).getStringCellValue() != null) {
          arrHeaderTemplate1 += "," + rowTemplate1.getCell(i).getStringCellValue().trim();
        }
      }

      int rT2 = rowTemplate2.getLastCellNum();
      for (int i = 0; i < rT2; i++) {
        if (rowTemplate2.getCell(i).getStringCellValue() != null) {
          arrHeaderTemplate2 += "," + rowTemplate2.getCell(i).getStringCellValue().trim();
        }
      }

      //lay dinh dang file input
      Workbook workBook = ewu.readFileExcel(pathFileForm);
      int sheetNumber = workBook.getNumberOfSheets();
      if (typeNumber == 2) {
        if (sheetNumber != 3) {
          resVal = I18n.getValidation("sr.tab.attach.invalidFileInput");
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto.setMessage(resVal);
          return resultInSideDto;
        }
      } else {
        if (sheetNumber != 2) {
          resVal = I18n.getValidation("sr.tab.attach.invalidFileInput");
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto.setMessage(resVal);
          return resultInSideDto;
        }
      }
      Sheet sheetInput1 = workBook.getSheetAt(0);
      Sheet sheetInput2 = workBook.getSheetAt(1);
      String arrHeaderInput3 = "";
      if (typeNumber == 2) {
        //file input 2 dong dau header1 row0 va row1 theo file mau
        Sheet sheetInput3 = workBook.getSheetAt(2);
        Row rowInput3 = sheetInput3.getRow(0);
        Row row2Input3 = sheetInput3.getRow(1);
        int c3 = rowInput3.getLastCellNum();
        for (int i = 0; i < c3; i++) {
          if (rowInput3.getCell(i).getStringCellValue() != null) {
            arrHeaderInput3 += "," + rowInput3.getCell(i).getStringCellValue().trim();
          }
        }
        c3 = row2Input3.getLastCellNum();
        for (int i = 0; i < c3; i++) {
          if (row2Input3.getCell(i).getStringCellValue() != null) {
            arrHeaderInput3 += "," + row2Input3.getCell(i).getStringCellValue().trim();
          }
        }
      }
      Row rowInput1 = sheetInput1.getRow(0);
      Row rowInput2 = sheetInput2.getRow(0);

      String arrHeaderInput1 = "";
      String arrHeaderInput2 = "";

      if (rowInput1 != null) {
        int r1 = rowInput1.getLastCellNum();
        for (int i = 0; i < r1; i++) {
          if (rowInput1.getCell(i).getCellType() == CellType.STRING) {
            if (rowInput1.getCell(i).getStringCellValue() != null) {
              arrHeaderInput1 += "," + rowInput1.getCell(i).getStringCellValue().trim();
            } else {
              break;
            }
          }
        }
      } else {
        resVal = I18n.getValidation("sr.tab.attach.invalidFileInput");
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage(resVal);
        return resultInSideDto;
      }
      if (rowInput2 != null) {
        int r2 = rowInput2.getLastCellNum();
        for (int i = 0; i < r2; i++) {
          if (rowInput2.getCell(i).getCellType() == CellType.STRING) {
            if (rowInput2.getCell(i).getStringCellValue() != null) {
              arrHeaderInput2 += "," + rowInput2.getCell(i).getStringCellValue().trim();
            } else {
              break;
            }
          }
        }
      } else {
        resVal = I18n.getValidation("sr.tab.attach.invalidFileInput");
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage(resVal);
        return resultInSideDto;
      }
      if (!arrHeaderTemplate1.equals(arrHeaderInput1)) {
        resVal = I18n.getValidation("sr.tab.attach.invalidFileInput");
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage(resVal);
        return resultInSideDto;
      } else if (!arrHeaderTemplate2.equals(arrHeaderInput2)) {
        resVal = I18n.getValidation("sr.tab.attach.invalidFileInput");
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage(resVal);
        return resultInSideDto;
      }

      if (typeNumber == 2) {
        if (!arrHeaderTemplate3.equals(arrHeaderInput3)) {
          resVal = I18n.getValidation("sr.tab.attach.invalidFileInput");
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto.setMessage(resVal);
          return resultInSideDto;
        }
      }

      //validate noi dung file
      File fileImport = new File(pathFileForm);

      //sheet form 1 pstn
      Map<Integer, Object[]> mapRow = new HashMap<>();
      List<Object[]> lstDataSheet = ExcelWriterUtils.readExcelAddBlankRowXSSF(
          fileImport,
          0,//sheet
          1,//begin row
          0,//from column
          9,//to column
          2);
      if (lstDataSheet == null || lstDataSheet.isEmpty()) {
        resVal = "Sheet 1 " + I18n.getValidation("sr.tab.attach.form.dataSheet.invalidData");
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage(resVal);
        return resultInSideDto;
      } else {
        int size = lstDataSheet.size();
        for (int i = size - 1; i > -1; i--) {
          Object[] o = lstDataSheet.get(i);
          if (o[1] == null && o[2] == null && o[3] == null && o[4] == null && o[5] == null
              && o[6] == null
              && o[7] == null && o[8] == null) {
            resVal =
                "Sheet 1 " + I18n.getValidation("sr.tab.attach.form.dataSheet.dataSheet.emptyRow");
            resultInSideDto.setKey(RESULT.FAIL);
            resultInSideDto.setMessage(resVal);
            return resultInSideDto;
          } else {
            mapRow.put(i, o);
          }
        }
        if (lstDataSheet.isEmpty()) {
          resVal = "Sheet 1 " + I18n.getValidation("sr.tab.attach.form.dataSheet.invalidData");
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto.setMessage(resVal);
          return resultInSideDto;
        } else if (lstDataSheet.size() > 1) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheetForm1.notMultipleRow");
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto.setMessage(resVal);
          return resultInSideDto;
        } else {
          //validate cac cot trong sheet 1
          ResultInSideDto validate = validateContentSheet(1, pathFileForm, mapRow);
          if (!validate.getCheck()) {
            resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.dataSheet1InputIncorrect");
            validate.setMessage(resVal);
            return validate;
          }
        }
      }
      //sheet form 1 pstn

      //sheet form 2 sdh
      mapRow = new HashMap<>();
      lstDataSheet = ExcelWriterUtils.readExcelAddBlankRowXSSF(
          fileImport,
          1,//sheet
          1,//begin row
          0,//from column
          8,//to column
          2);
      if (lstDataSheet == null || lstDataSheet.isEmpty()) {
        resVal = "Sheet 2 " + I18n.getValidation("sr.tab.attach.form.dataSheet.invalidData");
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage(resVal);
        return resultInSideDto;
      } else {
        int size = lstDataSheet.size();
        for (int i = size - 1; i > -1; i--) {
          Object[] o = lstDataSheet.get(i);
          if (o[1] == null && o[2] == null && o[3] == null && o[4] == null && o[5] == null
              && o[6] == null
              && o[7] == null && o[0] == null) {
            resVal =
                "Sheet 2 " + I18n.getValidation("sr.tab.attach.form.dataSheet.dataSheet.emptyRow");
            resultInSideDto.setKey(RESULT.FAIL);
            resultInSideDto.setMessage(resVal);
            return resultInSideDto;
          } else {
            mapRow.put(i, o);
          }
        }
        if (lstDataSheet.isEmpty()) {
          resVal = "Sheet 2 " + I18n.getValidation("sr.tab.attach.form.dataSheet.invalidData");
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto.setMessage(resVal);
          return resultInSideDto;
        } else {
          ResultInSideDto validate = validateContentSheet(2, pathFileForm, mapRow);
          if (!validate.getCheck()) {
            resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.dataSheet2InputIncorrect");
            validate.setMessage(resVal);
            return validate;
          }
          String kv = lstDataSheet.get(0)[0].toString().trim();
          size = lstDataSheet.size();
          for (int i = 1; i < size; i++) {
            if (!kv.equalsIgnoreCase(lstDataSheet.get(i)[0].toString().trim())) {
              resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.sameKvError");
              break;
            }
          }
        }
      }

      //sheet form 3 metro
      if (typeNumber == 2) {
        mapRow = new HashMap<>();
        lstDataSheet = ExcelWriterUtils.readExcelAddBlankRowXSSF(
            fileImport,
            2,//sheet
            2,//begin row
            0,//from column
            17,//to column
            2);
        if (lstDataSheet == null || lstDataSheet.isEmpty()) {
          resVal = "Sheet 3 " + I18n.getValidation("sr.tab.attach.form.dataSheet.invalidData");
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto.setMessage(resVal);
          return resultInSideDto;
        } else {
          int size = lstDataSheet.size();
          for (int i = size - 1; i > -1; i--) {
            Object[] o = lstDataSheet.get(i);
            if (o[1] == null && o[2] == null && o[3] == null && o[4] == null && o[5] == null
                && o[6] == null
                && o[7] == null && o[8] == null && o[9] == null && o[10] == null && o[12] == null
                && o[13] == null && o[14] == null && o[15] == null && o[16] == null) {
              resVal = "Sheet 3 " + I18n
                  .getValidation("sr.tab.attach.form.dataSheet.dataSheet.emptyRow");
              resultInSideDto.setKey(RESULT.FAIL);
              resultInSideDto.setMessage(resVal);
              return resultInSideDto;
            } else {
              mapRow.put(i, o);
            }
          }
          if (lstDataSheet.isEmpty()) {
            resVal = "Sheet 3 " + I18n.getValidation("sr.tab.attach.form.dataSheet.invalidData");
            resultInSideDto.setKey(RESULT.FAIL);
            resultInSideDto.setMessage(resVal);
            return resultInSideDto;
          }
        }
        ResultInSideDto validate = validateContentSheet(3, pathFileForm, mapRow);
        if (!validate.getCheck()) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.dataSheet3InputIncorrect");
          validate.setMessage(resVal);
          return validate;
        }
        String kv = lstDataSheet.get(0)[1].toString().trim();
        int size = lstDataSheet.size();
        for (int i = 1; i < size; i++) {
          if (!kv.equalsIgnoreCase(lstDataSheet.get(i)[1].toString().trim())) {
            resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.sameKvError");
            resultInSideDto.setKey(RESULT.FAIL);
            break;
          }
        }
      }
      resultInSideDto.setMessage(resVal);
      return resultInSideDto;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return resultInSideDto;
  }

  @Override
  public List<SRConfigDTO> getDataByConfigCode(SRConfigDTO srConfigDTO) {
    return srConfigRepository.getDataByConfigCode(srConfigDTO);
  }

  @Override
  public ResultInSideDto updateSRDTO(SrInsiteDTO srInsiteDTO) {
    log.info("Request to updateSRDTO : {}", srInsiteDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultValidateSRDTO = validateSRDTO(false, srInsiteDTO);
    boolean checkStatusClosed = false;
    if (resultValidateSRDTO.getKey().equals(RESULT.FAIL)) {
      return resultValidateSRDTO;
    }
    String oldStatus = resultValidateSRDTO.getIdValue();
    Double offset = userRepository.getOffsetFromUser(userToken.getUserName());
    srInsiteDTO = convertSRDate2VietNamDate(srInsiteDTO, offset);
    srInsiteDTO.setUpdatedTime(new Date());
    if (srInsiteDTO.isServiceNims() && outputNims != null) {
      if ("OK".equals(outputNims.getResult())) {
        srInsiteDTO.setStatus(Constants.SR_STATUS.CONCLUDED);
      }
    }
    //dungpv edit 20/08/2020 delete file Result OPEN CONNECT khi sr o trang thai New sang cac trang thai khac
    if (srInsiteDTO.isOpenConnect()
        && ((SR_STATUS.NEW.equals(oldStatus) && !SR_STATUS.NEW.equals(srInsiteDTO.getStatus()))
        || (SR_STATUS.REJECTED.equals(oldStatus) && (SR_STATUS.NEW
        .equals(srInsiteDTO.getStatus()) || SR_STATUS.DRAFT
        .equals(srInsiteDTO.getStatus()))))) {
      GnocFileDto gnocFileSearch = new GnocFileDto();
      gnocFileSearch.setBusinessId(srInsiteDTO.getSrId());
      gnocFileSearch.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
      List<GnocFileDto> lstGNOCFileDtos = gnocFileRepository.getListGnocFileByDto(gnocFileSearch);
      lstGNOCFileDtos.forEach(f -> {
        //cac file tu VIPA tra ve co CONTENT
        if (!StringUtils.isStringNullOrEmpty(f.getComments()) && !StringUtils
            .isStringNullOrEmpty(f.getContent())) {
          f.setBusinessCode(GNOC_FILE_BUSSINESS.GNOC_FILE_SR_DEL);
          gnocFileRepository.updateGnocFile(f);
          srRepository.deleteSRFile(f.getMappingId());
        }
      });
    }
    //end
    SRApproveDTO approveDTO = srInsiteDTO.getApproveDTO();
    if (approveDTO != null) {
      SRCatalogDTO srCatalogDTO = srCatalogRepository2
          .findById(Long.valueOf(srInsiteDTO.getServiceId()));
      if (Constants.SR_STATUS.UNDER_APPROVAL.equals(srInsiteDTO.getStatus())
          && (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getApprove()) && !"0"
          .equals(String.valueOf(srCatalogDTO.getApprove())))) {
        if (!Constants.SR_STATUS.DRAFT.equals(oldStatus)) {
          if ("1".equals(String.valueOf(srCatalogDTO.getApprove()))) {
            //neu la duyet 1 cap
            if ("0".equals(String.valueOf(approveDTO.getApproveLevel1()))) {
              srInsiteDTO.setStatus(Constants.SR_STATUS.DRAFT);
            } else if ("1".equals(String.valueOf(approveDTO.getApproveLevel1()))) {
              srInsiteDTO.setStatus(Constants.SR_STATUS.NEW);
            }
          } else {
            //neu la duyet 2 cap
            if (StringUtils.isStringNullOrEmpty(approveDTO.getApproveUnitLevel2())) {
              //neu duyet 2 cap ma don vi duyet cap 2 is_commitee = null
              if ("1".equals(String.valueOf(approveDTO.getApproveLevel1()))) {
                srInsiteDTO.setStatus(Constants.SR_STATUS.NEW);
              } else if ("0".equals(String.valueOf(approveDTO.getApproveLevel1()))) {
                srInsiteDTO.setStatus(Constants.SR_STATUS.DRAFT);
              }
            } else {
              if ("1".equals(String.valueOf(approveDTO.getApproveLevel1()))) {
                //neu cap 1 duyet ma cap 2 tu choi thi chuyen ve draft
                if ("0".equals(String.valueOf(approveDTO.getApproveLevel2()))) {
                  srInsiteDTO.setStatus(Constants.SR_STATUS.DRAFT);
                } else if ("1".equals(String.valueOf(approveDTO.getApproveLevel2()))) {
                  srInsiteDTO.setStatus(Constants.SR_STATUS.NEW);
                }
              } else {
                //neu cap 1 ko duyet thi chuyen ve draft luon
                srInsiteDTO.setStatus(Constants.SR_STATUS.DRAFT);
              }
            }
          }
        } else {
          approveDTO.setApproveLevel1(null);
          approveDTO.setApproveDateLevel1(null);
          approveDTO.setApproveUserLevel1(null);

          approveDTO.setApproveLevel2(null);
          approveDTO.setApproveDateLevel2(null);
          approveDTO.setApproveUserLevel2(null);
        }
      }
    }
    //Khi SR tu choi thi user nguoi xu ly null
    if (SR_STATUS.REJECTED.equals(srInsiteDTO.getStatus()) && !oldStatus
        .equals(srInsiteDTO.getStatus())) {
      srInsiteDTO.setSrUser(null);
    }
    if (Constants.SR_STATUS.CLOSED.equals(srInsiteDTO.getStatus())) {
      checkStatusClosed = true;
      if (srInsiteDTO.getEvaluate().equals("NOK") && srInsiteDTO.isCheckStatusClosedNOK()) {
        SRRoleActionDTO dtoActionUpdate = new SRRoleActionDTO();
        dtoActionUpdate.setCountry(srInsiteDTO.getCountry());
        dtoActionUpdate.setFlowId(srInsiteDTO.getFlowExecuteId());
        dtoActionUpdate.setCurrentStatus(SR_STATUS.ASSIGNED_PLANNING);
        dtoActionUpdate.setRoleType("UPDATE_SR");
        dtoActionUpdate.setGroupRole(Constants.SR_ROLE.ORIGINATOR);
        List<SRRoleActionDTO> lstDataUpdateSR = srCategoryServiceProxy
            .getListSRRoleActionDTO(dtoActionUpdate);
        if (lstDataUpdateSR != null && !lstDataUpdateSR.isEmpty()) {
          srInsiteDTO.setStatus(SR_STATUS.ASSIGNED_PLANNING);
        } else {
          srInsiteDTO.setStatus(SR_STATUS.EVALUATED);
        }
      }
      if ("NOK".equals(srInsiteDTO.getEvaluate())) {
        SrInsiteDTO srNOk = srRepository.getDetailNoOffset(srInsiteDTO.getSrId());
        if (srNOk != null) {
          Long count = srNOk.getCountNok() != null ? srNOk.getCountNok() : 0L;
          srInsiteDTO.setCountNok(++count);
        }
      }
    }
    resultInSideDto = updateSR(srInsiteDTO);
    boolean checkResultAutoCreateSRChild = false;
    if (!resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      if (srInsiteDTO.isAutoCreatCR() && StringUtils
          .isNotNullOrEmpty(resultValidateSRDTO.getReturnCode())) {
        String[] crNumberDelete = resultValidateSRDTO.getReturnCode().split(";");
        for (int i = 0; i < crNumberDelete.length; i++) {
          deleteCrAndWo(crNumberDelete[i], userToken);
        }
      }
      resultInSideDto.setKey(RESULT.FAIL);
      resultInSideDto.setMessage(
          I18n.getValidation("common.button.update") + I18n.getValidation("common.fail") + " : "
              + resultInSideDto.getMessage());
      return resultInSideDto;
    } else {
      if (srInsiteDTO.getLstMopTmp() != null && !srInsiteDTO.getLstMopTmp().isEmpty()) {
        resultInSideDto = srMopRepository.insertListSRMop(srInsiteDTO.getLstMopTmp());
      }
      //04112020 dungpv edit SR chuyen trang thai duoc cau hinh thi sinh SR con
      List<SRConfigDTO> lstConfigSRChild = srConfigRepository
          .getByConfigGroup(SR_CONFIG.AUTO_CREATE_SR_CHILD);
      if (SR_STATUS.NEW.equals(oldStatus) && lstConfigSRChild != null && !lstConfigSRChild
          .isEmpty()) {
        for (SRConfigDTO srConfigDTO : lstConfigSRChild) {
          if (srInsiteDTO.getStatus().equals(srConfigDTO.getConfigCode())) {
            SRCatalogDTO srCatalogDTO = srCatalogRepository2
                .findById(Long.parseLong(srInsiteDTO.getServiceId()));
            resultInSideDto = autoCreateSRChild(srCatalogDTO, srInsiteDTO);
            checkResultAutoCreateSRChild = resultInSideDto.getCheck();
            if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
              return resultInSideDto;
            }
            break;
          }
        }
      }
      //end
      //tab SR - trang thai sr con = trang thai sr cha
      List<SrInsiteDTO> lstSrChild = srRepository.findByParenCode(srInsiteDTO.getSrCode());
      if (lstSrChild != null && !lstSrChild.isEmpty()) {
        for (SrInsiteDTO child : lstSrChild) {
          if (Constants.SR_STATUS.DRAFT.equals(child.getStatus())) {
            child = srRepository.getDetail(child.getSrId(), userToken.getUserName());
            child.setStatus(srInsiteDTO.getStatus());
            child.setUpdatedTime(new Date(System.currentTimeMillis()));
            child.setUpdatedUser(userToken.getUserName());
            updateSR(child);
          }
        }
      }
      if (SR_STATUS.CONCLUDED.equals(srInsiteDTO.getStatus())) {
        ResultInSideDto resultChildAuto = autoCreateSRChildByGennerateNo(srInsiteDTO.getSrCode());
        if (!RESULT.SUCCESS.equals(resultChildAuto.getKey())) {
          return resultChildAuto;
        }
      }
      if (!oldStatus.equals(srInsiteDTO.getStatus())) {
        //<editor-fold desc="namtn edit on May 2018 : SR tao tu QLCTKT hoac CM chuyen sang trang thai Concluded" defaultstate="collapsed">
        if (Constants.SR_STATUS.CONCLUDED.equals(srInsiteDTO.getStatus())) {
          //goi ws tra ket qua cho QLCTKT hoac CM vs 2 tham so SubOrderId va Thoi gian hoan thanh
          List<SrInsiteDTO> lstCheckSR = srRepository
              .checkSRCreatedFromOtherSys(srInsiteDTO.getSrCode());
          if (lstCheckSR != null && !lstCheckSR.isEmpty()) {
            //goi den server QLCTKT hoac CM tra ket qua subOrderId va concludedTime
          }
        }
        //</editor-fold>
        if (Constants.SR_STATUS.CONCLUDED.equals(srInsiteDTO.getStatus())) {
          SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = new SRCreatedFromOtherSysDTO();
          srCreatedFromOtherSysDTO.setSrCode(srInsiteDTO.getSrCode());
          srCreatedFromOtherSysDTO.setSystemName("WO");
          List<SRCreatedFromOtherSysDTO> lstWo = srCreatedFromOtherSysRepository
              .getListSRCreatedFromOtherSysDTO(srCreatedFromOtherSysDTO);
          if (lstWo != null && !lstWo.isEmpty()) {
            CfgWoTickHelpDTO woTHDTO = new CfgWoTickHelpDTO();
            woTHDTO.setWoId(lstWo.get(0).getObjectId().toString());
            woTHDTO.setSystemName("SR");
            woTHDTO.setTickHelpId(srInsiteDTO.getSrId().toString());
            try {
              woServiceProxy.updateCfgWoTickHelpVsmart(woTHDTO);
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
          }
        }
      }
      if (approveDTO != null && !StringUtils.isStringNullOrEmpty(approveDTO.getApproveId())) {
        resultInSideDto = srApproveRepository.updateSRApprove(approveDTO);//update neu duyet SR
        if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
          if (approveDTO.getApproveLevel1() != null && approveDTO.getApproveLevel1() == 1L) {
            if (!StringUtils.isStringNullOrEmpty(approveDTO.getApproveUnitLevel2()) && StringUtils
                .isStringNullOrEmpty(approveDTO.getApproveLevel2())) {
              //neu la SR can phe duyet cấp 2 cấp và phê duyệt cấp 1 rồi thì gửi tin nhắn cho đơn vị phê duyệt cấp 2
              List<String> lstTP = srRepository
                  .getLeaderApprove("TP", approveDTO.getApproveUnitLevel2().toString());
              for (String tp : lstTP) {
                sendMessages(srInsiteDTO, tp, null, srInsiteDTO.getStatus(), "", "", "");
              }
            }
          }
        }
        return resultInSideDto;
      }
      if (checkStatusClosed) {
        SREvaluateDTO dto = new SREvaluateDTO();
        dto.setEvaluate(srInsiteDTO.getEvaluate());
        dto.setEvaluateReason(srInsiteDTO.getEvaluateReason());
        dto.setSrId(srInsiteDTO.getSrId().toString());
        dto.setCreatedTime(new Date());
        dto.setCreatedUser(userToken.getUserName());
        if (StringUtils.isStringNullOrEmpty(dto.getEvaluateId())) {
          resultInSideDto = srEvaluateRepository.insertSREvaluate(dto);
        } else {
          resultInSideDto = srEvaluateRepository.updateSREvaluate(dto);
        }
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto.setMessage(I18n.getValidation("sr.evaluate.update.fail"));
          return resultInSideDto;
        }
      }
      if (!oldStatus.equals(srInsiteDTO.getStatus())) {
        if (Constants.SR_STATUS.CONCLUDED.equals(srInsiteDTO.getStatus())
            || Constants.SR_STATUS.EXECUTION_HALTED.equals(srInsiteDTO.getStatus())) {
          if (srInsiteDTO.isConcludedWithCr() || srInsiteDTO.isExecutionWithCr()) {
            List<CrInsiteDTO> lstCr = findListCrBySr(srInsiteDTO.getSrId());
            for (CrInsiteDTO crDTO : lstCr) {
              try {
                //update stepId cua CR = 2
                ResultInSideDto resStep = srRepository
                    .updateStepIdCr(crDTO.getCrId(), "2", srInsiteDTO.getSrId());
                if (!RESULT.SUCCESS.equals(resStep.getKey())) {
                  resultInSideDto.setKey(RESULT.FAIL);
                  resultInSideDto
                      .setMessage("Co loi xay ra khi update stepId CR: " + resStep.getMessage());
                  return resultInSideDto;
                }
              } catch (Exception e) {
                log.error(e.getMessage(), e);
                resultInSideDto.setKey(RESULT.FAIL);
                resultInSideDto.setMessage("Exception khi update stepId CR: " + e.getMessage());
                return resultInSideDto;
              }
            }
            srInsiteDTO.setConcludedWithCr(false);
            srInsiteDTO.setExecutionWithCr(false);
          }
        }
      }
      //cap nhat file ket qua tu nims va nhan tin cho ng tao + ng xu ly
      if (srInsiteDTO.isServiceNims() && outputNims != null) {
        if (Constants.WS_RESULT.OK.equals(outputNims.getResult())) {
          List<DistributeIpResourceForm> lstForm = outputNims.getListDataResponse();
          exportFileResultNims(lstForm, "SAVE_FILE", srInsiteDTO);

          List<SRRoleUserInSideDTO> lstSrUser = new ArrayList<>();
          lstSrUser.add(new SRRoleUserInSideDTO(srInsiteDTO.getSrUser()));

          SRConfigDTO configDTO = new SRConfigDTO();
          configDTO.setConfigGroup(Constants.SR_CONFIG.CONFIG_GROUP_SMS_SRF);
          configDTO.setConfigCode(Constants.SR_CONFIG.CONFIG_CODE_SMS_NIMS);

          sendMessages(srInsiteDTO, srInsiteDTO.getCreatedUser(), null, configDTO);
          sendMessages(srInsiteDTO, null, lstSrUser, configDTO);
          outputNims = null;
        }
      }
      actionSaveLinkCr(srInsiteDTO.getLstCr(), srInsiteDTO.getSrId());
    }
    //dungpv 03112020 add CrNumber khi tao tu dong CR
    List<SrInsiteDTO> lstCrNumbers = srRepository
        .getCrNumberCreatedFromSR(new SrInsiteDTO(srInsiteDTO.getSrId()), 0, 0, false);
    if (lstCrNumbers != null && !lstCrNumbers.isEmpty()) {
      String crNumber = "";
      for (SrInsiteDTO crInsiteDTO : lstCrNumbers) {
        crNumber += crInsiteDTO.getCrNumber() + ",";
      }
      if (StringUtils.isNotNullOrEmpty(crNumber) && crNumber.endsWith(",")) {
        crNumber = crNumber.substring(0, crNumber.length() - 1);
      }
      if (StringUtils.isNotNullOrEmpty(crNumber)) {
        srRepository.updateCrNumberForSR(crNumber, srInsiteDTO.getSrId());
      }
    }
    //end
    if (srInsiteDTO.isServiceNims() && outputNims != null) {
      resultInSideDto.setKey(RESULT.FAIL);
      resultInSideDto.setMessage(outputNims.getMessage());
    } else {
      resultInSideDto.setKey(RESULT.SUCCESS);
      if (resultValidateSRDTO.getKey().equals(RESULT.SUCCESS)
          && resultValidateSRDTO.getLstResult() != null && !resultValidateSRDTO.getLstResult()
          .isEmpty()) {
        resultInSideDto.setLstResult(resultValidateSRDTO.getLstResult());
      }
      resultInSideDto.setMessage(
          I18n.getLanguage("common.button.update") + " " + I18n.getLanguage("common.success"));
    }
    if (checkResultAutoCreateSRChild) {
      resultInSideDto.setMessage(
          resultInSideDto.getMessage() + "," + I18n.getLanguage("common.update.SrChild.succes"));
    }
    return resultInSideDto;
  }

  private ResultInSideDto validateContentSheet(int sheet, String pathFileForm,
      Map<Integer, Object[]> mapRow) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setCheck(true);
    String resVal = "";
    int row = 1;
    out:
    if (sheet == 1) {
      int size = mapRow.size();
      for (int j = size - 1; j > -1; j--) {
        row = j + 1;
        Object[] o = mapRow.get(j);
        if (o[1] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.kvIncorrect");
          resultInSideDto.setCheck(false);
          break out;
          //Khu vuc
        } else if (!"KV1".equalsIgnoreCase(o[1].toString().trim()) && !"KV2"
            .equalsIgnoreCase(o[1].toString().trim()) && !"KV3"
            .equalsIgnoreCase(o[1].toString().trim())) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.kvIncorrect");
          resultInSideDto.setCheck(false);
          break out;
          //Khu vuc
        }
        if (o[2] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.tenIncorrect");
          resultInSideDto.setCheck(false);
          break out;
          //Ten khach hang
        }
        if (o[3] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.diachiIncorrect");
          resultInSideDto.setCheck(false);
          break out;
          //Dia chi
        }
        if (o[4] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.maTinhIncorrect");
          resultInSideDto.setCheck(false);
          break out;
          //Ma tinh
        } else if (!StringUtils.isLong(o[4].toString().trim())) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.maTinhIncorrect");
          resultInSideDto.setCheck(false);
          break out;
          //Ma tinh
        }
        if (o[5] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.daySoIncorrect");
          resultInSideDto.setCheck(false);
          break out;
          //Day so
        } else {
          if (o[5].toString().trim().contains("-")) {
            String[] o5 = o[5].toString().trim().split("-");
            if (o5.length == 2) {
              for (String oo : o5) {
                if (!StringUtils.isLong(oo.trim())) {
                  resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.daySoIncorrect");
                  resultInSideDto.setCheck(false);
                  break out;
                }
              }
            } else {
              resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.daySoIncorrect");
              resultInSideDto.setCheck(false);
              break out;
            }
          } else {
            resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.daySoIncorrect");
            resultInSideDto.setCheck(false);
            break out;
          }
          //Day so
        }
        if (o[6] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.ptIncorrect");
          resultInSideDto.setCheck(false);
          break out;
          //Phuong thuc hien thi so
        } else {
          String[] pt = o[6].toString().trim().split(":");
          if (pt.length == 1) {
            if (!I18n.getValidation("sr.tab.attach.form.dataSheet.hienThiSoDocLap")
                .equalsIgnoreCase(pt[0].trim())) {
              if (I18n.getValidation("sr.tab.attach.form.dataSheet.hienThiSoChu")
                  .equalsIgnoreCase(pt[0].trim())) {
                resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.pt2Incorrect");
                resultInSideDto.setCheck(false);
                break out;
              } else {
                resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.ptIncorrect");
                resultInSideDto.setCheck(false);
                break out;
              }
            } else {
              if (o[7] != null && !I18n.getValidation("sr.tab.attach.form.dataSheet.tinhCuocDocLap")
                  .equalsIgnoreCase(o[7].toString().trim())) {
                resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.stcInvalid2");
                resultInSideDto.setCheck(false);
                break out;
              }
            }
          } else if (pt.length == 2) {
            if (!I18n.getValidation("sr.tab.attach.form.dataSheet.hienThiSoChu")
                .equalsIgnoreCase(pt[0].trim())) {
              resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.ptIncorrect");
              resultInSideDto.setCheck(false);
              break out;
            }
            if (!StringUtils.isLong(pt[1].trim())) {
              resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.pt2Incorrect");
              resultInSideDto.setCheck(false);
              break out;
            } else {
              if (o[7] != null && !StringUtils.isLong(o[7].toString().trim())) {
                resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.stcIncorrect");
                resultInSideDto.setCheck(false);
                break out;
              }
            }
          } else {
            resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.ptIncorrect");
            resultInSideDto.setCheck(false);
            break out;
          }
          if (o[7] == null) {
            resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.stcIncorrect");
            resultInSideDto.setCheck(false);
            break out;
          }
          //So tinh cuoc
        }
        if (o[8] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.cmqtIncorrect");
          resultInSideDto.setCheck(false);
          break out;
          //Chan mo QT
        } else if (!o[8].toString().trim().toLowerCase().equalsIgnoreCase(
            I18n.getValidation("sr.tab.attach.form.dataSheet.chan").toLowerCase())
            && !o[8].toString().trim().toLowerCase().equalsIgnoreCase(
            I18n.getValidation("sr.tab.attach.form.dataSheet.mo").toLowerCase())) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.cmqtIncorrect");
          resultInSideDto.setCheck(false);
          break out;
          //Chan mo QT
        }
//                }
      }
    } else if (sheet == 2) {
      int size = mapRow.size();
      for (int j = 0; j < size; j++) {
        row = j + 1;
        Object[] o = mapRow.get(j);
        if (o[0] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.kvIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Khu vuc
        } else if (!"KV1".equalsIgnoreCase(o[0].toString().trim()) && !"KV2"
            .equalsIgnoreCase(o[0].toString().trim()) && !"KV3"
            .equalsIgnoreCase(o[0].toString().trim())) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.kvIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Khu vuc
        }
        if (o[1] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.tenIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Ten khach hang
        }
        if (o[2] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.diachiIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Dia chi
        }
        if (o[3] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.maTinhIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Ma tinh
        } else if (!StringUtils.isLong(o[3].toString().trim())) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.maTinhIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Ma tinh
        }
        if (o[4] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.daySoIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Day so
        } else {
          if (o[4].toString().trim().contains("-")) {
            String[] o4 = o[4].toString().trim().split("-");
            for (String oo : o4) {
              if (!StringUtils.isLong(oo.trim())) {
                resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.daySoIncorrect");
                resultInSideDto.setCheck(false);
                break;
              }
            }
            if (!resultInSideDto.getCheck()) {
              break;
            }
          } else {
            resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.daySoIncorrect");
            resultInSideDto.setCheck(false);
            break;
          }
          //Day so
        }
        if (o[5] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.ptIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Phuong thuc hien thi so
        } else {
          String[] pt = o[5].toString().trim().split(":");
          if (pt.length == 1) {
            if (!I18n.getValidation("sr.tab.attach.form.dataSheet.hienThiSoDocLap")
                .equalsIgnoreCase(pt[0].trim())) {
              if (I18n.getValidation("sr.tab.attach.form.dataSheet.hienThiSoChu")
                  .equalsIgnoreCase(pt[0].trim())) {
                resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.pt2Incorrect");
                resultInSideDto.setCheck(false);
                break out;
              } else {
                resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.ptIncorrect");
                resultInSideDto.setCheck(false);
                break out;
              }
            } else {
              if (o[6] != null && !I18n.getValidation("sr.tab.attach.form.dataSheet.tinhCuocDocLap")
                  .equalsIgnoreCase(o[6].toString().trim())) {
                resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.stcInvalid2");
                resultInSideDto.setCheck(false);
                break out;
              }
            }
          } else if (pt.length == 2) {
            if (!I18n.getValidation("sr.tab.attach.form.dataSheet.hienThiSoChu")
                .equalsIgnoreCase(pt[0].trim())) {
              resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.ptIncorrect");
              resultInSideDto.setCheck(false);
              break out;
            }
            if (!StringUtils.isLong(pt[1].trim())) {
              resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.pt2Incorrect");
              resultInSideDto.setCheck(false);
              break out;
            } else {
              if (o[6] != null && !StringUtils.isLong(o[6].toString().trim())) {
                resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.stcIncorrect");
                resultInSideDto.setCheck(false);
                break out;
              }
            }
          } else {
            resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.ptIncorrect");
            resultInSideDto.setCheck(false);
            break out;
          }
          if (o[6] == null) {
            resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.stcIncorrect");
            resultInSideDto.setCheck(false);
            break out;
          }
          //So tinh cuoc
        }
        if (o[7] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.cmqtIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Chan mo QT
        } else if (!o[7].toString().trim().toLowerCase().equalsIgnoreCase(
            I18n.getValidation("sr.tab.attach.form.dataSheet.chan").toLowerCase())
            && !o[7].toString().trim().toLowerCase().equalsIgnoreCase(
            I18n.getValidation("sr.tab.attach.form.dataSheet.mo").toLowerCase())) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet.cmqtIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Chan mo QT
        }
//                }
      }
    } else if (sheet == 3) {
      int size = mapRow.size();
      for (int j = 0; j < size; j++) {
        row = j + 2;
        Object[] o = mapRow.get(j);

        if (o[1] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet3.kvIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Khu vuc
        } else if (!"KV1".equalsIgnoreCase(o[1].toString().trim()) && !"KV2"
            .equalsIgnoreCase(o[1].toString().trim()) && !"KV3"
            .equalsIgnoreCase(o[1].toString().trim())) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet3.kvIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Khu vuc
        }
        if (o[2] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet3.dvIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Dich vu
        }
        if (o[3] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet3.dvycIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //DV yeu cau
        }
        if (o[4] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet3.aIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Account
        }
        if (o[5] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet3.tenIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Ten KH
        }
        if (o[6] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet3.dcddIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Dia chi diem dau
        }
        if (o[7] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet3.dcldcIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Dia chi lap dat cuoi
        }
        if (o[8] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet3.tdIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Toc do Mbps
        } else if (!StringUtils.isLong(o[8].toString().trim())) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet3.tdIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Toc do Mbps
        }
        if (o[9] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet3.srtIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Ma SRT
        }
        if (o[10] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet3.tbIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Thiet bi
        }
        if (o[12] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet3.mtdtdIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Ma toa do truyen dan
        }
        if (o[13] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet3.tbIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Thiet bi
        }
        if (o[14] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet3.pIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //Port
        }
        if (o[15] == null) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet3.klmIncorrect");
          resultInSideDto.setCheck(false);
          break;
          //KLM
        } else if (!StringUtils.isLong(o[15].toString().trim())) {
          resVal = I18n.getValidation("sr.tab.attach.form.dataSheet3.klmIncorrect");
          resultInSideDto.setCheck(false);
          break;
        }
//                }
      }
    }
    if (!resultInSideDto.getCheck()) {
      ExcelWriterUtils ewu = new ExcelWriterUtils();
      Workbook workBook = ewu.readFileExcel(pathFileForm);
      if (sheet == 1) {
        Sheet sheetForm1 = workBook.getSheetAt(0);
        CellStyle cellStResult = sheetForm1.getRow(1).getCell(0) == null ? null
            : sheetForm1.getRow(1).getCell(0).getCellStyle();
        ewu.createCell(sheetForm1, 10, row, resVal, cellStResult);
        sheetForm1.setSelected(true);
      } else if (sheet == 2) {
        Sheet sheetForm2 = workBook.getSheetAt(1);
        CellStyle cellStResult = sheetForm2.getRow(1).getCell(0) == null ? null
            : sheetForm2.getRow(1).getCell(0).getCellStyle();
        ewu.createCell(sheetForm2, 9, row, resVal, cellStResult);
        sheetForm2.setSelected(true);
      } else if (sheet == 3) {
        Sheet sheetForm3 = workBook.getSheetAt(2);
        CellStyle cellStResult = sheetForm3.getRow(2).getCell(0) == null ? null
            : sheetForm3.getRow(2).getCell(0).getCellStyle();
        ewu.createCell(sheetForm3, 18, row, resVal, cellStResult);
        sheetForm3.setSelected(true);
      }
      Date date = new Date();
      String pathByDate = FileUtils.createPathByDate(date);
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      String fileResult = tempFolder
          + File.separator + "ImportResult"
          + File.separator + pathByDate
          + File.separator;
      String fileName = "File_import_error" + "_" + System.currentTimeMillis() + ".xlsx";
      ewu.saveToFileExcel(fileResult, fileName);
      resultInSideDto.setKey(RESULT.FAIL);
      resultInSideDto.setFilePath(fileResult + fileName);
    }
    return resultInSideDto;
  }

  private boolean getFileData(String filePathOut, String fileupload) {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = fileupload;//BIEU MAU
    if (templatePathOut.toLowerCase().endsWith(".xlsx") || templatePathOut.toLowerCase()
        .endsWith(".xls")) {
      Workbook workBook = ewu.readFileExcel(templatePathOut);
      if (workBook == null) {
        return false;
      }
      Sheet sheetTemplate = workBook.getSheetAt(0); //BIEU MAU

      Workbook workBook2 = ewu.readFileExcel(filePathOut);//FILE IMPORT VAO`
      Sheet sheetFileImport = workBook2.getSheetAt(0);
      int size = sheetTemplate.getLastRowNum();
      for (int t = 0; t <= size; t++) {
        if (sheetTemplate.getRow(t) != null) {
          if (sheetFileImport.getRow(t) != null) {
            Row rowTemplate = sheetTemplate.getRow(t);
            Row rowFileImport = sheetFileImport.getRow(t);
            String arrHeaderTemplate = "";
            String arrHeaderFileImport = "";
            int k = rowTemplate.getLastCellNum();
            outTemplate:
            for (int i = 0; i < k; i++) {
              if (rowTemplate.getCell(i) == null) {
                continue;
              }
              Cell cTemplate = rowTemplate.getCell(i);
              switch (cTemplate.getCellType()) {
                case STRING:
                  if (cTemplate.getStringCellValue() != null) {
                    if (cTemplate.getStringCellValue().contains("(*)")) {
                      arrHeaderTemplate += "," + cTemplate.getStringCellValue()
                          .substring(0, cTemplate.getStringCellValue().lastIndexOf(" "));
                    } else {
                      arrHeaderTemplate += "," + cTemplate.getStringCellValue().trim();
                    }
                  } else {
                    break outTemplate;
                  }
                  break;
                case NUMERIC:
                  arrHeaderTemplate += "," + cTemplate.getNumericCellValue();
                  break;
              }
            }
            int l = rowFileImport.getLastCellNum();
            outImport:
            for (int j = 0; j < l; j++) {
              if (rowFileImport.getCell(j) == null) {
                continue;
              }
              Cell cImport = rowFileImport.getCell(j);
              switch (cImport.getCellType()) {
                case STRING:
                  if (cImport.getStringCellValue() != null) {
                    if (cImport.getStringCellValue().contains("(*)")) {
                      arrHeaderFileImport += "," + cImport.getStringCellValue()
                          .substring(0, cImport.getStringCellValue().lastIndexOf(" "));
                    } else {
                      arrHeaderFileImport += "," + cImport.getStringCellValue().trim();
                    }
                  } else {
                    break outImport;
                  }
                  break;
                case NUMERIC:
                  arrHeaderTemplate += "," + cImport.getNumericCellValue();
                  break;
              }
            }
            if (!arrHeaderTemplate.equals(arrHeaderFileImport)) {
              return false;
            } else {
              return true;
            }
          } else {
            return false;
          }
        } else {
          return false;
        }
      }
    } else {
      return false;
    }
    return true;
  }

  //dungpv 25/08/2020 nang cap SR MKN
  private ResultInSideDto validateFileOpenConnect(String path) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    try {
      File fileImport = new File(path);
      // Lay du lieu import
      log.info("===============Mo ket noi=====================");
      log.info("Bat dau doc du lieu tu file excel");
      List<Object[]> lstData = CommonImport.getDataFromExcelFile(
          fileImport,
          0,//sheet
          1,//begin row
          0,//from column
          10,//to column
          1000);
      List<ErrorInfo> cellErrorList = new ArrayList<>();
      log.info("Data file excel: " + lstData.size() + " ban ghi");
      if (lstData == null || lstData.isEmpty()) {
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage("File " + I18n.getValidation("common.searh.nodata"));
      } else {
        int row = 1;
        for (Object[] obj : lstData) {
          validateContentFileDataVipa(obj, row, cellErrorList);
          row++;
        }
        if (!cellErrorList.isEmpty()) {
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto.setMessage(I18n.getValidation("vipa.error.message.importFail"));
          exportFileResult(lstData, I18n.getLanguage("sr.template.vipa"), cellErrorList,
              resultInSideDto);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return resultInSideDto;
  }
  //end

  private void exportFileResult(List<Object[]> lstImport, String fileNameInput,
      List<ErrorInfo> cellErrorList, ResultInSideDto resultInSideDto) {
    try {
      ExcelWriterUtils ewu = new ExcelWriterUtils();
      Date date = new Date();
      String pathByDate = FileUtils.createPathByDate(date);
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      String templatePathOut = "templates"
          + File.separator + fileNameInput + ".xlsx";
      Workbook workBook = readFileExcel(templatePathOut);
      Sheet sheet = workBook.getSheetAt(0);

      int i = 1;
      CellStyle cellSt1 = sheet.getRow(0).getCell(0).getCellStyle();
      if (I18n.getLanguage("sr.template.vipa").equals(fileNameInput)) {
        ewu.createCell(sheet, 11, 0, I18n.getLanguage("srCatalog.data.import.result"),
            cellSt1);
        for (Object[] row : lstImport) {
          ewu.createCell(sheet, 0, i, row[0] == null ? "" : row[0].toString().trim());
          ewu.createCell(sheet, 1, i, row[1] == null ? "" : row[1].toString().trim());
          ewu.createCell(sheet, 2, i, row[2] == null ? "" : row[2].toString().trim());
          ewu.createCell(sheet, 3, i, row[3] == null ? "" : row[3].toString().trim());
          ewu.createCell(sheet, 4, i, row[4] == null ? "" : row[4].toString().trim());
          ewu.createCell(sheet, 5, i, row[5] == null ? "" : row[5].toString().trim());
          ewu.createCell(sheet, 6, i, row[6] == null ? "" : row[6].toString().trim());
          ewu.createCell(sheet, 7, i, row[7] == null ? "" : row[7].toString().trim());
          ewu.createCell(sheet, 8, i, row[8] == null ? "" : row[8].toString().trim());
          ewu.createCell(sheet, 9, i, row[9] == null ? "" : row[9].toString().trim());
          ewu.createCell(sheet, 10, i, row[10] == null ? "" : row[10].toString().trim());
          i++;
        }
        for (ErrorInfo err : cellErrorList) {
          ewu.createCell(sheet, 11, err.getRow(), err.getMsg());
        }
      } else if (I18n.getLanguage("sr.template.nims").equals(fileNameInput)) {
        ewu.createCell(sheet, 5, 0, I18n.getLanguage("srCatalog.data.import.result"), cellSt1);
        for (Object[] row : lstImport) {
          ewu.createCell(sheet, 0, i, row[0] == null ? "" : row[0].toString().trim());
          ewu.createCell(sheet, 1, i, row[1] == null ? "" : row[1].toString().trim());
          ewu.createCell(sheet, 2, i, row[2] == null ? "" : row[2].toString().trim());
          ewu.createCell(sheet, 3, i, row[3] == null ? "" : row[3].toString().trim());
          ewu.createCell(sheet, 4, i, row[4] == null ? "" : row[4].toString().trim());
          i++;
        }
        for (ErrorInfo err : cellErrorList) {
          ewu.createCell(sheet, 5, err.getRow(), err.getMsg());
        }
      } else if (I18n.getLanguage("sr.template.aom").equals(fileNameInput)) {
        if (!cellErrorList.isEmpty()) {
          ewu.createCell(sheet, 11, 0, I18n.getLanguage("srCatalog.data.import.result"),
              cellSt1);
        }
        for (Object[] row : lstImport) {
          ewu.createCell(sheet, 0, i, row[0] == null ? "" : row[0].toString().trim());
          ewu.createCell(sheet, 1, i, row[1] == null ? "" : row[1].toString().trim());
          ewu.createCell(sheet, 2, i, row[2] == null ? "" : row[2].toString().trim());
          ewu.createCell(sheet, 3, i, row[3] == null ? "" : row[3].toString().trim());
          ewu.createCell(sheet, 4, i, row[4] == null ? "" : row[4].toString().trim());
          ewu.createCell(sheet, 5, i, row[5] == null ? "" : row[5].toString().trim());
          ewu.createCell(sheet, 6, i, row[6] == null ? "" : row[6].toString().trim());
          ewu.createCell(sheet, 7, i, row[7] == null ? "" : row[7].toString().trim());
          ewu.createCell(sheet, 8, i, row[8] == null ? "" : row[8].toString().trim());
          ewu.createCell(sheet, 9, i, row[9] == null ? "" : row[9].toString().trim());
          ewu.createCell(sheet, 10, i, row[10] == null ? "" : row[10].toString().trim());
          i++;
        }
        if (!cellErrorList.isEmpty()) {
          for (ErrorInfo err : cellErrorList) {
            ewu.createCell(sheet, 11, err.getRow(), err.getMsg());
          }
        }
      }

      String fileResult = tempFolder
          + File.separator + "ImportResult"
          + File.separator + pathByDate
          + File.separator;
      String fileName = "";
      if (I18n.getLanguage("sr.template.vipa").equals(fileNameInput)) {
        fileName = "ImportFormOpenConnectResult" + "_" + System.currentTimeMillis() + ".xlsx";
      } else if (I18n.getLanguage("sr.template.nims").equals(fileNameInput)) {
        fileName = "ImportFormNimsResult" + "_" + System.currentTimeMillis() + ".xlsx";
      } else if (I18n.getLanguage("sr.template.aom").equals(fileNameInput)) {
        fileName = "ImportFormGateResult" + "_" + System.currentTimeMillis() + ".xlsx";
      }
      ewu.setWorkbook(workBook);
      ewu.saveToFileExcel(fileResult, fileName);
      resultInSideDto.setFilePath(fileResult + fileName);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.ERROR);
    }
  }

  //20201218 dungpv edit sr mkn
  private ResultInSideDto sendToVipa(String srId, String contentFile) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.FAIL);
    String userName = srConfigRepository.getByConfigGroup(Constants.SR_ROLE_UPDATE.VIPA_WS_USER)
        .get(0).getConfigCode();
    String passWord = srConfigRepository.getByConfigGroup(Constants.SR_ROLE_UPDATE.VIPA_WS_PASS)
        .get(0).getConfigCode();
    String link = srConfigRepository.getByConfigGroup(Constants.SR_ROLE_UPDATE.VIPA_WS_LINK).get(0)
        .getConfigCode();
    try {
      URL url = new URL(link);
      log.info("\nLink Vipa: " + String.valueOf(link) + "\nuserName: " + String.valueOf(userName)
          + "\npassWord: " + String.valueOf(passWord) + "\nsr_id: " + String.valueOf(srId)
          + "\ncontentFile: " + String.valueOf(contentFile));
      com.viettel.sr.openconnect.WSForGNOCImplService vipa = new com.viettel.sr.openconnect.WSForGNOCImplService(
          url);
      com.viettel.sr.openconnect.WSForGNOC port = vipa.getWSForGNOCImplPort();
      setTimeOutWs(port);
      com.viettel.sr.openconnect.ResultPolicy rs = port
          .verifyInputOpenPolicy(userName, passWord, srId, contentFile);
      if (rs != null) {
        log.info(
            "********************* SR_ID senToVipa " + srId + "*********************************");
        log.info("Ket qua Vipa tra ve, Result Message: " + rs.getResultMessage());
        log.info("Ket qua Vipa tra ve, Result Code: " + rs.getResultCode());
      }
      if (rs == null) {
        resultInSideDto.setMessage(I18n.getValidation("sr.add.vipa.fail"));
      } else if (rs != null && rs.getResultCode() != 0) {
        resultInSideDto.setMessage(I18n.getValidation("sr.add.vipa.fail") + rs.getResultMessage());
      } else {
        int numberOk = 0;
        int countFail = 0;
        List<SRConfigDTO> lstCfgTimeOutSROpenConnect = srConfigRepository
            .getByConfigGroup(SR_CONFIG.CFG_TIME_OUT_SR_OPEN_CONNECT);
        int cfgTime = 240;
        if (lstCfgTimeOutSROpenConnect != null && !lstCfgTimeOutSROpenConnect.isEmpty()
            && StringUtils.isNotNullOrEmpty(lstCfgTimeOutSROpenConnect.get(0).getConfigCode())) {
          cfgTime = Integer.parseInt(lstCfgTimeOutSROpenConnect.get(0).getConfigCode());
        }
        log.info("cfgTime : " + cfgTime);
        GnocFileDto fileReturnFromVipa = null;
        GnocFileDto dtoFile = new GnocFileDto();
        dtoFile.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
        dtoFile.setBusinessId(Long.parseLong(srId));
        dtoFile.setFileType(Constants.RESULT_SR_VIPA.OTHER);
        while (numberOk == 0 && countFail < cfgTime) {
          log.info("Doi file ket qua tu Vipa tra ve ...");
          List<GnocFileDto> lstFile = gnocFileRepository.getListGnocFileForSR(dtoFile);
          if (lstFile != null && !lstFile.isEmpty()) {
            for (GnocFileDto file : lstFile) {
              if (!StringUtils.isStringNullOrEmpty(file.getComments())
                  && Constants.RESULT_SR_VIPA.OTHER.equals(file.getFileType())) {
                numberOk = 1;
                fileReturnFromVipa = file;
                log.info("VIPA DA TRA FILE " + file.getComments());
                break;
              }
            }
            countFail++;
            log.info("countFail : " + countFail);
          } else {
            try {
              countFail++;
              log.info("countFail : " + countFail);
              Thread.sleep(2000);
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
          }
        }
        if (fileReturnFromVipa != null && StringUtils
            .isNotNullOrEmpty(fileReturnFromVipa.getComments()) && fileReturnFromVipa.getComments()
            .toUpperCase()
            .contains("NOK") && StringUtils.isNotNullOrEmpty(fileReturnFromVipa.getPath())) {
          resultInSideDto.setMessage(I18n.getValidation("sr.result.vipa.error"));
          if (fileReturnFromVipa.getPath() != null) {
            byte[] bytesFileFromVipa = FileUtils
                .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), fileReturnFromVipa.getPath());
            String templatePathOut = FileUtils
                .saveTempFile(FileUtils.getFileName(fileReturnFromVipa.getPath()),
                    bytesFileFromVipa,
                    tempFolder);
            List<GnocFileDto> lstFileResult = new ArrayList<>();
            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setPath(templatePathOut);
            lstFileResult.add(gnocFileDto);
            resultInSideDto.setLstResult(lstFileResult);
          }
          log.info("FILE VIPA NOK");
          resultInSideDto.setCheck(false);
        } else if (fileReturnFromVipa != null) {
          log.info("FILE VIPA OK");
          resultInSideDto.setKey(RESULT.SUCCESS);
          resultInSideDto.setCheck(true);
        } else {
          log.info("FILE VIPA OFFLINE");
          resultInSideDto.setKey(RESULT.SUCCESS);
          resultInSideDto.setCheck(null);
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return resultInSideDto;
  }
  //end

  private void validateContentFileDataVipa(Object[] obj, int row, List<ErrorInfo> cellErrorList) {
    try {
      if (obj[1] == null) {
        cellErrorList.add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ipNguon")));
        return;
      }
      if (obj[2] == null) {
        cellErrorList.add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ipDich")));
        return;
      }
      if (obj[3] == null) {
        cellErrorList
            .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.portKetNoi")));
        return;
      }
      if (obj[4] == null) {
        cellErrorList.add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.giaoThuc")));
        return;
      } else {
        if (!I18n.getValidation("vipa.error.message.giaoThuc.tcp")
            .equalsIgnoreCase(obj[4].toString().trim())
            && !I18n.getValidation("vipa.error.message.giaoThuc.udp")
            .equalsIgnoreCase(obj[4].toString().trim())
            && !(I18n.getValidation("vipa.error.message.giaoThuc.udp") + "/" + I18n
            .getValidation("vipa.error.message.giaoThuc.tcp"))
            .equalsIgnoreCase(obj[4].toString().trim())
            && !(I18n.getValidation("vipa.error.message.giaoThuc.tcp") + "/" + I18n
            .getValidation("vipa.error.message.giaoThuc.udp"))
            .equalsIgnoreCase(obj[4].toString().trim())) {
          cellErrorList
              .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.giaoThuc")));
          return;
        }
      }
      if (obj[5] == null) {
        cellErrorList
            .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.tenHeThong")));
        return;
      }
      if (obj[6] == null) {
        cellErrorList
            .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ngayBatDau")));
        return;
      } else {
        if (obj[6].toString().trim().length() != 10) {
          cellErrorList
              .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ngayBatDau")));
          return;
        } else if (!obj[6].toString().trim().contains("/")) {
          cellErrorList
              .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ngayBatDau")));
          return;
        } else {
          try {
            DateUtil.string2Date(obj[6].toString().trim());
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            cellErrorList
                .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ngayBatDau")));
            return;
          }
          String[] arrDay = obj[6].toString().trim().split("/");
          if (arrDay.length != 3) {
            cellErrorList
                .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ngayBatDau")));
            return;
          }
          String dd = arrDay[0];
          if (Long.valueOf(dd) < 0 || Long.valueOf(dd) > 31) {
            cellErrorList
                .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ngayBatDau")));
            return;
          }
          String mm = arrDay[1];
          if (Long.valueOf(mm) < 0 || Long.valueOf(mm) > 12) {
            cellErrorList
                .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ngayBatDau")));
            return;
          }
          String yyyy = arrDay[2];
          if (Long.valueOf(yyyy) < 0) {
            cellErrorList
                .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ngayBatDau")));
            return;
          }
        }
      }
      if (obj[7] == null) {
        cellErrorList
            .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ngayKetThuc")));
        return;
      } else {
        if (obj[7].toString().trim().length() != 10) {
          cellErrorList
              .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ngayKetThuc")));
          return;
        } else if (!obj[7].toString().trim().contains("/")) {
          cellErrorList
              .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ngayKetThuc")));
          return;
        } else {
          try {
            DateUtil.string2Date(obj[7].toString().trim());
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            cellErrorList
                .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ngayKetThuc")));
            return;
          }
          String[] arrDay = obj[7].toString().trim().split("/");
          if (arrDay.length != 3) {
            cellErrorList
                .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ngayKetThuc")));
            return;
          }
          String dd = arrDay[0];
          if (Long.valueOf(dd) < 0 || Long.valueOf(dd) > 31) {
            cellErrorList
                .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ngayKetThuc")));
            return;
          }
          String mm = arrDay[1];
          if (Long.valueOf(mm) < 0 || Long.valueOf(mm) > 12) {
            cellErrorList
                .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ngayKetThuc")));
            return;
          }
          String yyyy = arrDay[2];
          if (Long.valueOf(yyyy) < 0) {
            cellErrorList
                .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.ngayKetThuc")));
            return;
          }
        }
      }
      if (DateTimeUtils.compareDateTime(DateUtil.string2Date(obj[6].toString().trim()),
          DateUtil.string2Date(obj[7].toString().trim())) == 1) {
        cellErrorList.add(
            new ErrorInfo(row, I18n.getValidation("vipa.error.message.startNotGreaterThanEnd")));
        return;
      }
      if (obj[8] == null) {
        cellErrorList.add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.hanhDong")));
        return;
      } else {
        if (!I18n.getValidation("vipa.error.message.hanhDong.mo")
            .equalsIgnoreCase(obj[8].toString().trim())
            && !I18n.getValidation("vipa.error.message.hanhDong.chan")
            .equalsIgnoreCase(obj[8].toString().trim()) && !I18n
            .getValidation("vipa.error.message.hanhDong.3")
            .equalsIgnoreCase(obj[8].toString().trim())) {
          cellErrorList
              .add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.hanhDong")));
          return;
        }
      }
      if (obj[9] == null) {
        cellErrorList.add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.mucDich")));
        return;
      }
      if (obj[10] == null) {
        cellErrorList.add(new ErrorInfo(row, I18n.getValidation("vipa.error.message.donVi")));
        return;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private ResultInSideDto validateFileImportVipa(String path) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setCheck(true);
    if (!path.toLowerCase().endsWith(".xlsx")) {
      resultInSideDto.setMessage(I18n.getValidation("sr.ui.upload.fileopen.invalidExtension"));
      resultInSideDto.setCheck(false);
    } else {
      boolean check = validateFileDataWithFileName(path, I18n.getLanguage("sr.template.vipa"));
      if (!check) {
        resultInSideDto.setMessage(I18n.getValidation("srRoleAction.import.errorTemplate"));
        resultInSideDto.setCheck(false);
      }
    }
    return resultInSideDto;
  }

  private boolean validateFileDataWithFileName(String fileImportPathOut, String fileName) {
    try {
      ExcelWriterUtils ewu = new ExcelWriterUtils();
      String templatePathOut = "templates" + File.separator + fileName + ".xlsx";
      Workbook workBook = readFileExcel(templatePathOut);
      Sheet sheetTemplate = workBook.getSheetAt(0);

      Workbook workBook2 = ewu.readFileExcel(fileImportPathOut);
      Sheet sheetFileImport = workBook2.getSheetAt(0);

      Row rowFileImport = sheetFileImport.getRow(0);
      Row rowTemplate = sheetTemplate.getRow(0);
      String arrHeaderTemplate = "";
      String arrHeaderFileImport = "";
      int k = rowTemplate.getLastCellNum();
      for (int i = 0; i < k; i++) {
        if (rowTemplate.getCell(i).getStringCellValue() != null) {
          arrHeaderTemplate += "," + rowTemplate.getCell(i).getStringCellValue().trim();
        } else {
          break;
        }
      }
      int l = rowFileImport.getLastCellNum();
      for (int j = 0; j < l; j++) {
        if (rowFileImport.getCell(j).getStringCellValue() != null) {
          arrHeaderFileImport += "," + rowFileImport.getCell(j).getStringCellValue().trim();
        } else {
          break;
        }
      }
      if (!arrHeaderTemplate.equals(arrHeaderFileImport)) {
        return false;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return false;
    }
    return true;
  }

  public Workbook readFileExcel(String filePathName) {
    InputStream inp = null;
    Workbook workbook = null;
    try {
      // R3292_EDIT_DUNGNV50_13122012_START
      Resource resource = new ClassPathResource(filePathName);
      inp = resource.getInputStream();
      // R3292_EDIT_DUNGNV50_13122012_END
      workbook = WorkbookFactory.create(inp);
    } catch (FileNotFoundException ex) {
      log.error(ex.getMessage(), ex);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    } finally {
      try {
        if (inp != null) {
          inp.close();
        }
      } catch (IOException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return workbook;
  }

  @Override
  public List<GnocFileDto> getListSRFile(GnocFileDto gnocFileDto) {
    gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
    List<GnocFileDto> lst = gnocFileRepository.getListGnocFileForSR(gnocFileDto);
    if (lst != null && !lst.isEmpty()) {
      List<GnocFileDto> lstResult = new ArrayList<>();
      for (GnocFileDto dto : lst) {
        if (StringUtils.isNotNullOrEmpty(dto.getFileType())) {
          SRConfig2DTO fileType = srConfig2Repository
              .getFileTypeByConfigCode(dto.getFileType().trim().toUpperCase(), "A",
                  Constants.SR_ROLE_UPDATE.FILE_TYPE);
          dto.setFileTypeName(fileType != null ? fileType.getConfigDes() : dto.getFileType());
        }
        lstResult.add(dto);
      }
      return lstResult;
    }
    return lst;
  }

  @Override
  public List<ResultInSideDto> getCBBCmbFileName(SrWsToolCrDTO srWsToolCrDTO) throws Exception {
    log.info("Request to getCBBCmbFileName : {}", srWsToolCrDTO.getFileType());
    List<ResultInSideDto> lstResult = new ArrayList<>();
    if (srWsToolCrDTO != null) {
      String fileType = srWsToolCrDTO.getFileType();
      List<com.viettel.vmsa2.FlowTemplateGNOCObjDTO> lstVmsa = new ArrayList<>();
      List<com.viettel.aam2.FlowTemplateGNOCObjDTO> lstAAM = new ArrayList<>();
      List<com.viettel.vipa2.FlowTemplateGNOCObjDTO> lstVIPA = new ArrayList<>();
      List<SRMappingProcessCRDTO> lstSRMappingProcess = srWsToolCrDTO.getLstSRMappingProcess();
      List<SRConfigDTO> lstDataCountry = srWsToolCrDTO.getLstDataCountry();
      List<SRConfigDTO> lstData = srWsToolCrDTO.getLstData();
      if (Constants.SR_ROLE_UPDATE.FILE_TYPE_TOOL.equals(fileType)) {
        String configName = lstDataCountry.isEmpty() ? null : lstDataCountry.get(0).getConfigName();
        String crProcessParentId = null;
        String crProcessId = null;
        List<Long> procedureWorkFlowIds = new ArrayList<>();
        if (lstSRMappingProcess != null && !lstSRMappingProcess.isEmpty()) {
          crProcessParentId =
              lstSRMappingProcess.get(0).getCrProcessParentId() != null ? lstSRMappingProcess.get(0)
                  .getCrProcessParentId().toString() : null;
          crProcessId = lstSRMappingProcess.get(0).getProcessTypeLv3Id();
        }
        //<editor-fold desc="file template VMSA" defaultstate="collapsed">
        if (Constants.SR_CONFIG.VMSA.equals(lstData.get(0).getAutomation())) {
          try {
            List<SRConfigDTO> lstLink = srConfigRepository
                .getByConfigGroup(Constants.SR_CONFIG.WS_VMSA_URL);

            List<SRConfigDTO> lstUsername = srConfigRepository
                .getByConfigGroup(Constants.SR_CONFIG.WS_VMSA_USERNAME);
            String usernameVMSA = stringDecrypt(userNameVMSA, saltService);
            if (lstUsername != null && !lstUsername.isEmpty()) {
              usernameVMSA = lstUsername.get(0).getConfigCode();
            }
            String pVMSA = stringDecrypt(passVMSA, saltService);
            List<SRConfigDTO> lstPassword = srConfigRepository
                .getByConfigGroup(Constants.SR_CONFIG.WS_VMSA_P);
            if (lstPassword != null && !lstPassword.isEmpty()) {
              pVMSA = lstPassword.get(0).getConfigCode();
            }
            //20201223 dungpv edit SR_CR_TOOL VMSA truyen nhieu dau viec
            com.viettel.vmsa2.ProcedureDTO procedureDTO = new com.viettel.vmsa2.ProcedureDTO();
            procedureDTO.setProcedureId(lstSRMappingProcess.get(0).getCrProcessParentId());
            if (StringUtils.isNotNullOrEmpty(crProcessId)) {
              String[] arrProcessTypeLv3Id = crProcessId.split(",");
              if (arrProcessTypeLv3Id != null && arrProcessTypeLv3Id.length > 0) {
                for (String process3Id : arrProcessTypeLv3Id) {
                  procedureWorkFlowIds.add(Long.parseLong(process3Id));
                }
              }
            }
            if (procedureWorkFlowIds != null && procedureWorkFlowIds.size() > 0) {
              procedureDTO.getProcedureWorkFlowIds().addAll(procedureWorkFlowIds);
            }
            //end
            URL url = new URL(lstLink.get(0).getConfigCode());
            com.viettel.vmsa2.ResultGetListTemplatesByProcedure vmsaResult = null;
            log.info("--------- getListTemplatesByProcedure VMSA START---------");
            //<editor-fold desc="LOG PARAM getListTemplatesByProcedure VMSA" defaultstate="collapsed">
            log.info("\n <userService>" + usernameVMSA + "</userService>"
                + "\n <passService>" + pVMSA + "</passService>"
                + "\n <countryCode>" + lstDataCountry.get(0).getConfigName() + "</countryCode>"
                + "\n <procedureDTO>"
                + "\n <procedureId>" + lstSRMappingProcess.get(0).getCrProcessParentId()
                + "</procedureId>"
                + "<\n procedureWorkFlowIds>" + lstSRMappingProcess.get(0).getProcessTypeLv3Id()
                + "</procedureWorkFlowIds>"
                + "<\n /procedureDTO>");
            //</editor-fold>
            try {
              com.viettel.vmsa2.WSForGNOCImplService serviceVMSA = new com.viettel.vmsa2.WSForGNOCImplService(
                  url);
              com.viettel.vmsa2.WSForGNOC port = serviceVMSA.getWSForGNOCImplPort();
              setTimeOutWs(port);
              vmsaResult = port
                  .getListTemplatesByProcedure(usernameVMSA, pVMSA, configName, procedureDTO);
            } catch (Exception e) {
              log.error(e.getMessage(), e);
              log.info("link VMSA timeout: " + lstLink.get(0).getConfigCode());
              lstResult.add(new ResultInSideDto(null, RESULT.ERROR,
                  I18n.getValidation("sr.error.vmsaWS")));
              return lstResult;
            }
            log.info("---------getListTemplatesByProcedure VMSA END");
            if (vmsaResult == null) {
              return null;
            } else {
              String logWS = "\n resultCode : " + vmsaResult.getResultCode()
                  + "\n resultMessage : " + vmsaResult.getResultMessage();
              if (vmsaResult.getResultCode() != 0) {
                ResultInSideDto resultInSideDto = new ResultInSideDto();
                resultInSideDto.setKey(RESULT.ERROR);
                resultInSideDto
                    .setMessage("From Automation :" + vmsaResult.getResultMessage());
                lstResult.add(resultInSideDto);
                log.info("RESULT getListTemplatesByProcedure: " + logWS);
                return lstResult;
              } else {
                lstVmsa = vmsaResult.getFlowTemplatesObj();
              }
              if (lstVmsa != null && !lstVmsa.isEmpty()) {
                for (com.viettel.vmsa2.FlowTemplateGNOCObjDTO dtoVMSA : lstVmsa) {
                  ResultInSideDto resultInSideDto = new ResultInSideDto();
                  resultInSideDto.setIdValue(dtoVMSA.getTemplateId());
                  resultInSideDto.setFileName(dtoVMSA.getTemplateName());
                  if (!StringUtils.isStringNullOrEmpty(dtoVMSA.getProcedureGnocId())) {
                    resultInSideDto.setProcessId(dtoVMSA.getProcedureGnocId().toString());
                  }
                  lstResult.add(resultInSideDto);
                  logWS += "\n flowTemplatesObj : " + "\n templateId : " + dtoVMSA.getTemplateId()
                      + "\n templateName : " + dtoVMSA.getTemplateName()
                      + "\n resultCode : " + dtoVMSA.getResultCode()
                      + "\n procedureGnocId : " + dtoVMSA.getProcedureGnocId();
                }
              }
              log.info("RESULT getListTemplatesByProcedure: " + logWS);
            }
          } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
          }
        } //</editor-fold>
        //<editor-fold desc="file template AAM" defaultstate="collapsed">
        else if (Constants.SR_CONFIG.AAM.equals(lstData.get(0).getAutomation())) {
          try {
            List<SRConfigDTO> lstLink = srConfigRepository
                .getByConfigGroup(Constants.SR_CONFIG.WS_AAM_URL);

            List<SRConfigDTO> lstUsername = srConfigRepository
                .getByConfigGroup(Constants.SR_CONFIG.WS_AAM_USERNAME);
            String usernameAAM = stringDecrypt(userNameAAM, saltService);
            if (lstUsername != null && !lstUsername.isEmpty()) {
              usernameAAM = lstUsername.get(0).getConfigCode();
            }
            String pAAM = stringDecrypt(passAAM, saltService);
            List<SRConfigDTO> lstPassword = srConfigRepository
                .getByConfigGroup(Constants.SR_CONFIG.WS_AAM_P);
            if (lstPassword != null && !lstPassword.isEmpty()) {
              pAAM = lstPassword.get(0).getConfigCode();
            }

            URL url = new URL(lstLink.get(0).getConfigCode());
            com.viettel.aam2.ResultGetListTemplatesByProcedure aamResult = null;
            log.info("--------- getListTemplatesByProcedure AAM START ---------");
            //<editor-fold desc="LOG PARAM getListTemplatesByProcedure AAM" defaultstate="collapsed">
            log.info("link AAM : " + lstLink.get(0).getConfigCode());
            log.info("\n <userService>" + usernameAAM + "</userService>"
                + "\n <passService>" + pAAM + "</passService>"
                + "\n <countryCode>" + configName + "</countryCode>"
                + "\n <procedureId>" + crProcessParentId
                + "</procedureId>"
                + "\n <workFlowId>" + crProcessId + "</workFlowId>"
            );
            log.info("--------- getListTemplatesByProcedure AAM END");
            //</editor-fold>
            try {
              com.viettel.aam2.TdttWebserviceImplService serviceAAM = new com.viettel.aam2.TdttWebserviceImplService(
                  url);
              com.viettel.aam2.TdttWebservice port = serviceAAM.getTdttWebserviceImplPort();
              setTimeOutWs(port);
              aamResult = port
                  .getListTemplatesByProcedure(usernameAAM, pAAM, configName, crProcessParentId,
                      crProcessId);
            } catch (Exception e1) {
              log.error(e1.getMessage(), e1);
              log.info("link AAM timeout: " + lstLink.get(0).getConfigCode());
              lstResult.add(new ResultInSideDto(null, RESULT.ERROR,
                  I18n.getValidation("sr.error.aamWS")));
              return lstResult;
            }

            if (aamResult == null) {
              return null;
            } else {
              if (aamResult.getResultCode() != 0) {
                ResultInSideDto resultInSideDto = new ResultInSideDto();
                resultInSideDto.setKey(RESULT.ERROR);
                resultInSideDto
                    .setMessage("From Automation :" + aamResult.getResultMessage());
                lstResult.add(resultInSideDto);
                return lstResult;
              } else {
                lstAAM = aamResult.getFlowTemplatesObj();
              }
              if (lstAAM != null && !lstAAM.isEmpty()) {
                for (com.viettel.aam2.FlowTemplateGNOCObjDTO dtoAAM : lstAAM) {
                  ResultInSideDto resultInSideDto = new ResultInSideDto();
                  resultInSideDto.setIdValue(dtoAAM.getTemplateId());
                  resultInSideDto.setFileName(dtoAAM.getTempFileName());
                  lstResult.add(resultInSideDto);
                }
              }
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
        }//</editor-fold>
        //<editor-fold desc="file template VIPA" defaultstate="collapsed">
        else if (Constants.SR_CONFIG.VIPA.equals(lstData.get(0).getAutomation())) {
          try {
            List<SRConfigDTO> lstLink = srConfigRepository
                .getByConfigGroup(Constants.SR_CONFIG.WS_VIPA_URL);

            List<SRConfigDTO> lstUsername = srConfigRepository
                .getByConfigGroup(Constants.SR_CONFIG.WS_VIPA_USERNAME);
            String usernameVIPA = stringDecrypt(userNameVIPA, saltService);
            if (lstUsername != null && !lstUsername.isEmpty()) {
              usernameVIPA = lstUsername.get(0).getConfigCode();
            }
            String pVIPA = stringDecrypt(passVIPA, saltService);
            List<SRConfigDTO> lstPassword = srConfigRepository
                .getByConfigGroup(Constants.SR_CONFIG.WS_VIPA_P);
            if (lstPassword != null && !lstPassword.isEmpty()) {
              pVIPA = lstPassword.get(0).getConfigCode();
            }

            //20201223 dungpv edit SR_CR_TOOL VMSA truyen nhieu dau viec
            com.viettel.vipa2.ProcedureDTO procedureDTO = new com.viettel.vipa2.ProcedureDTO();
            procedureDTO.setProcedureId(lstSRMappingProcess.get(0).getCrProcessParentId());
            if (StringUtils.isNotNullOrEmpty(crProcessId)) {
              String[] arrProcessTypeLv3Id = crProcessId.split(",");
              if (arrProcessTypeLv3Id != null && arrProcessTypeLv3Id.length > 0) {
                for (String process3Id : arrProcessTypeLv3Id) {
                  procedureWorkFlowIds.add(Long.parseLong(process3Id));
                }
              }
            }
            if (procedureWorkFlowIds != null && procedureWorkFlowIds.size() > 0) {
              procedureDTO.getProcedureWorkFlowIds().addAll(procedureWorkFlowIds);
            }
            //end

            URL url = new URL(lstLink.get(0).getConfigCode());
            com.viettel.vipa2.ResultGetListTemplatesByProcedure vipaResult = null;
            log.info("--------- getListTemplatesByProcedure VIPA START---------");
            //<editor-fold desc="LOG PARAM getListTemplatesByProcedure VIPA" defaultstate="collapsed">
            log.info("\n <userService>" + usernameVIPA + "</userService>"
                + "\n <passService>" + pVIPA + "</passService>"
                + "\n <countryCode>" + configName + "</countryCode>"
                + "\n <procedureDTO>"
                + "\n <procedureId>" + lstSRMappingProcess.get(0).getCrProcessParentId()
                + "</procedureId>"
                + "<\n procedureWorkFlowIds>" + lstSRMappingProcess.get(0).getProcessTypeLv3Id()
                + "</procedureWorkFlowIds>"
                + "<\n /procedureDTO>");
            //</editor-fold>
            try {
              com.viettel.vipa2.WSForGNOCImplService serviceVIPA = new com.viettel.vipa2.WSForGNOCImplService(
                  url);
              com.viettel.vipa2.WSForGNOC port = serviceVIPA.getWSForGNOCImplPort();
              setTimeOutWs(port);
              vipaResult = port
                  .getListTemplatesByProcedure(usernameVIPA, pVIPA, configName, procedureDTO);
            } catch (Exception e1) {
              log.error(e1.getMessage(), e1);
              log.info("link VIPA timeout: " + lstLink.get(0).getConfigCode());
              lstResult.add(new ResultInSideDto(null, RESULT.ERROR,
                  I18n.getValidation("sr.error.vipaWS")));
              return lstResult;
            }
            log.info("---------getListTemplatesByProcedure VIPA END");
            if (vipaResult == null) {
              return null;
            } else {
              String logWS = "\n resultCode : " + vipaResult.getResultCode()
                  + "\n resultMessage : " + vipaResult.getResultMessage();

              if (vipaResult.getResultCode() != 0) {
                ResultInSideDto resultInSideDto = new ResultInSideDto();
                resultInSideDto.setKey(RESULT.ERROR);
                resultInSideDto
                    .setMessage("From Automation :" + vipaResult.getResultMessage());
                lstResult.add(resultInSideDto);
                log.info("RESULT getListTemplatesByProcedure: " + logWS);
                return lstResult;
              } else {
                lstVIPA = vipaResult.getFlowTemplatesObj();
              }
              if (lstVIPA != null && !lstVIPA.isEmpty()) {
                for (com.viettel.vipa2.FlowTemplateGNOCObjDTO dtoVIPA : lstVIPA) {
                  ResultInSideDto resultInSideDto = new ResultInSideDto();
                  resultInSideDto.setIdValue(dtoVIPA.getTemplateId());
                  resultInSideDto.setFileName(dtoVIPA.getTemplateName());
                  if (!StringUtils.isStringNullOrEmpty(dtoVIPA.getProcedureGnocId())) {
                    resultInSideDto.setProcessId(dtoVIPA.getProcedureGnocId().toString());
                  }
                  lstResult.add(resultInSideDto);
                  logWS += "\n flowTemplatesObj : " + "\n templateId : " + dtoVIPA.getTemplateId()
                      + "\n templateName : " + dtoVIPA.getTemplateName()
                      + "\n resultCode : " + dtoVIPA.getResultCode()
                      + "\n procedureGnocId : " + dtoVIPA.getProcedureGnocId();
                }
              }
              log.info("RESULT getListTemplatesByProcedure: " + logWS);
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
        } //</editor-fold>
        if (lstAAM != null && !lstAAM.isEmpty()) {
          ObjectMapper mapper = new ObjectMapper();
          String dataAAM = mapper.writeValueAsString(lstAAM);
          if (StringUtils.isNotNullOrEmpty(dataAAM)) {
            srWsToolCrDTO.setLstAAM(dataAAM);
          }
        }
        if (lstResult != null && !lstResult.isEmpty()) {
          lstResult.get(0).setSrWsToolCrDTO(srWsToolCrDTO);
        }
      }
    }
    return lstResult;
  }

  @Override
  public SRApproveDTO findSRApproveBySrId(Long srId) {
    log.info("Request to findSRApproveBySrId : {}", srId);
    return srApproveRepository.findSRApproveBySrId(srId);
  }

  @Override
  public ResultInSideDto insertOrUpdateSRApprove(SRApproveDTO srApproveDTO) {
    log.info("Request to insertOrUpdateSRApprove : {}", srApproveDTO);
    if (!StringUtils.isLongNullOrEmpty(srApproveDTO.getApproveId())) {
      return srApproveRepository.updateSRApprove(srApproveDTO);
    } else {
      return srApproveRepository.insertSRApprove(srApproveDTO);
    }
  }

  @Override
  public SRRenewDTO findSRRenewBySrId(Long srId) {
    log.info("Request to findSRRenewBySrId : {}", srId);
    return srRenewRepository.findSRRenewBySrId(srId);
  }

  @Override
  public ResultInSideDto insertOrUpdateSRRenew(SRRenewDTO srRenewDTO) {
    log.info("Request to insertOrUpdateSRRenew : {}", srRenewDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    Double offset = userRepository.getOffsetFromUser(userToken.getUserName());
    srRenewDTO = converTimeFromClientToServer(srRenewDTO, offset);
    if (srRenewDTO == null || srRenewDTO.getSrId() == null) {
      resultInSideDto.setKey(RESULT.FAIL);
      resultInSideDto.setMessage(RESULT.FAIL);
      return resultInSideDto;
    }
    SrInsiteDTO srInsiteDTO = srRepository.getDetail(srRenewDTO.getSrId(), userToken.getUserName());
    srRenewDTO.setEndTime(srInsiteDTO.getEndTime());
    if (StringUtils.isLongNullOrEmpty(srRenewDTO.getRenewId())) {
      resultInSideDto = srRenewRepository.updateSRRenew(srRenewDTO);
      if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        //gui tin nhan cho ng tao
        SRConfigDTO configDTO = new SRConfigDTO();
        configDTO.setConfigGroup(Constants.SR_CONFIG.CONFIG_GROUP_SMS);
        configDTO.setConfigCode(Constants.SR_CONFIG.CONFIG_CODE_SMS_RENEW);
        sendMessages(srInsiteDTO, srInsiteDTO.getCreatedUser(), null, configDTO);
      }
    } else {
      resultInSideDto = srRenewRepository.insertSRRenew(srRenewDTO);
      if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        //gui tin nhan cho ng xu ly
        SRConfigDTO configDTO = new SRConfigDTO();
        configDTO.setConfigGroup(Constants.SR_CONFIG.CONFIG_GROUP_SMS_SRF);
        configDTO.setConfigCode(srRenewDTO.getStatusRenew() == 1L
            ? Constants.SR_CONFIG.CONFIG_CODE_SMS_RENEW_APPROVE
            : Constants.SR_CONFIG.CONFIG_CODE_SMS_RENEW_REJECT);
        List<SRRoleUserInSideDTO> lstSrUser = new ArrayList<>();
        lstSrUser.add(new SRRoleUserInSideDTO(srInsiteDTO.getSrUser()));
        sendMessages(srInsiteDTO, null, lstSrUser, configDTO);
      }
    }
    return resultInSideDto;
  }

  private void setMapStatus() {
    List<SRConfigDTO> statusList = srConfigRepository.getByConfigGroup("STATUS");
    if (statusList != null && !statusList.isEmpty()) {
      for (SRConfigDTO status : statusList) {
        mapStatus.put(String.valueOf(status.getConfigCode()), status);
      }
    }
  }

  private void initSRRole(boolean createNew, SrInsiteDTO srInsiteDTO) {
    String roleAction = ",R,";
    srInsiteDTO.setIsLeader("0");
    List<SRConfigDTO> lstStatus = new ArrayList<>();
    UserToken userToken = ticketProvider.getUserToken();
    SRCatalogDTO srCatalogDTO = srCatalogRepository2
        .findById(Long.valueOf(srInsiteDTO.getServiceId()));
    srInsiteDTO.setSrCatalogDTO(srCatalogDTO);
    setMapStatus();
    if (!createNew) { //nếu cập nhật
      List<SRRoleActionDTO> lstRoleUpdateSR = new ArrayList<>();
      SRRoleUserInSideDTO searchRoleUser = new SRRoleUserInSideDTO();
      searchRoleUser.setUsername(userToken.getUserName());
      searchRoleUser.setStatus("A");
      searchRoleUser.setCountry(srInsiteDTO.getCountry());
      searchRoleUser.setUnitId(srInsiteDTO.getSrUnit());
      searchRoleUser.setRoleCode(srInsiteDTO.getRoleCode());
      //tim username, trang thai Active, quoc gia, don vi, nhom xu ly => lay ra nhom quyen
      List<SRRoleUserInSideDTO> lsRoleUser = srCategoryServiceProxy
          .getlistSRRoleUserDTO(searchRoleUser);
      if (lsRoleUser == null) {
        lsRoleUser = new ArrayList<>();
      } else {
        for (SRRoleUserInSideDTO roleUser : lsRoleUser) {
          if ((!StringUtils.isStringNullOrEmpty(srInsiteDTO.getSrUser()) && userToken.getUserName()
              .equalsIgnoreCase(srInsiteDTO.getSrUser()))
              || (StringUtils.isStringNullOrEmpty(srInsiteDTO.getSrUser())) && srInsiteDTO
              .getSrUnit().equals(roleUser.getUnitId())) {

            //load quyen cap nhat SR
            SRRoleActionDTO dtoActionUpdate = new SRRoleActionDTO();

            if (!createNew) {
              dtoActionUpdate.setCountry(srInsiteDTO.getCountry());
              dtoActionUpdate.setFlowId(Long.valueOf(srCatalogDTO.getFlowExecute()));
              dtoActionUpdate.setCurrentStatus(srInsiteDTO.getStatus());
            }
            dtoActionUpdate.setRoleType("UPDATE_SR");
            dtoActionUpdate.setGroupRole(Constants.SR_ROLE.IMPLEMENTOR);

            //tim loai hanh dong, quoc gia, luong xu ly, trang thai hien tai => lay ra hanh dong va trang thai tiep theo
            List<SRRoleActionDTO> lstDataUpdateSR = srCategoryServiceProxy
                .getListSRRoleActionDTO(dtoActionUpdate);
            if (lstDataUpdateSR != null && !lstDataUpdateSR.isEmpty()) {
              lstRoleUpdateSR.addAll(lstDataUpdateSR);
            }
          }
          if ("1".equals(String.valueOf(roleUser.getIsLeader()))) {
            srInsiteDTO.setIsLeader("1");
            SRRoleActionDTO dtoActionUpdate = new SRRoleActionDTO();
            dtoActionUpdate.setCountry(srInsiteDTO.getCountry());
            dtoActionUpdate.setFlowId(srCatalogDTO.getFlowExecute());
            dtoActionUpdate.setCurrentStatus(srInsiteDTO.getStatus());
            dtoActionUpdate.setGroupRole(Constants.SR_ROLE.MANAGER);
            List<SRRoleActionDTO> lstDataUpdateSR = srCategoryServiceProxy
                .getListSRRoleActionDTO(dtoActionUpdate);
            if (lstDataUpdateSR != null && !lstDataUpdateSR.isEmpty()) {
              lstRoleUpdateSR.addAll(lstDataUpdateSR);
            }
          }
        }
      }
      //neu la nguoi tao thi load quyen originator
      if (userToken.getUserName().equalsIgnoreCase(srInsiteDTO.getCreatedUser())) {
        SRRoleActionDTO dtoActionUpdate = new SRRoleActionDTO();
        dtoActionUpdate.setCountry(srInsiteDTO.getCountry());
        dtoActionUpdate.setFlowId(srCatalogDTO.getFlowExecute());
        dtoActionUpdate.setCurrentStatus(srInsiteDTO.getStatus());
        dtoActionUpdate.setRoleType("UPDATE_SR");
        dtoActionUpdate.setGroupRole(Constants.SR_ROLE.ORIGINATOR);
        List<SRRoleActionDTO> lstDataUpdateSR = srCategoryServiceProxy
            .getListSRRoleActionDTO(dtoActionUpdate);
        lstRoleUpdateSR.addAll(lstDataUpdateSR != null ? lstDataUpdateSR : new ArrayList<>());
      }

      if (!lstRoleUpdateSR.isEmpty()) {
        for (SRRoleActionDTO updateRole : lstRoleUpdateSR) {
          String[] arrActions = updateRole.getActions().split(",");
          for (String action : arrActions) {
            if (!roleAction.contains("," + action + ",")) {
              roleAction += action + ",";
            }
          }
          if (!createNew) {
            String[] arrNextStatus = updateRole.getNextStatus().split(",");
            for (String nextStatus : arrNextStatus) {
              SRConfigDTO srConfigDTO = mapStatus.get(nextStatus);
              if (srConfigDTO != null && !lstStatus.stream()
                  .filter(s -> s.getConfigCode().equals(srConfigDTO.getConfigCode())).findAny()
                  .isPresent()) {
                lstStatus.add(srConfigDTO);
              }
            }
            srInsiteDTO.setLstStatus(lstStatus);
          }
        }
        srInsiteDTO.setRoleAction(roleAction);
      }
    }
    if (!createNew) {
      //neu la update thi load danh sach trang thai
      //add trang thai hien tai vao combobox neu chua co
      if (!lstStatus.stream().filter(
          s -> s.getConfigCode().equals(mapStatus.get(srInsiteDTO.getStatus()).getConfigCode()))
          .findAny().isPresent()) {
        lstStatus.add(mapStatus.get(srInsiteDTO.getStatus()));
        srInsiteDTO.setLstStatus(lstStatus);
      }
    }
    if (!createNew) {
      List<SRRoleUserInSideDTO> lstRoleUser = srCategoryServiceProxy
          .getListUser(srInsiteDTO.getSrUnit().toString(), srInsiteDTO.getCountry(), "");
      srInsiteDTO.setCheckUser(0L);
      if (lstRoleUser != null && !lstRoleUser.isEmpty()) {
        for (SRRoleUserInSideDTO item : lstRoleUser) {
          if (item.getRoleCode().equals(srInsiteDTO.getRoleCode())) {
            if (item.getUsername().equals(userToken.getUserName())) {
              srInsiteDTO.setCheckUser(1L);
            }
          }
        }
      }
      if (Constants.SR_STATUS.CONCLUDED.equals(srInsiteDTO.getStatus())
          || Constants.SR_STATUS.CLOSED.equals(srInsiteDTO.getStatus())
          || Constants.SR_STATUS.REJECTED.equals(srInsiteDTO.getStatus())
          || Constants.SR_STATUS.CANCELLED.equals(srInsiteDTO.getStatus())) {
        srInsiteDTO.setBtnSplitSR(0L);
      } else {
        SRCatalogChildDTO dtoChild = new SRCatalogChildDTO();
        dtoChild.setServiceCode(srCatalogDTO.getServiceCode());
        dtoChild.setStatus("A");
        List<SRCatalogChildDTO> lstChild = srCatalogChildRepository.getListCatalogChild(dtoChild);
        if (userToken.getUserName().equalsIgnoreCase(srInsiteDTO.getSrUser()) && (lstChild != null
            && !lstChild.isEmpty()) && roleAction
            .contains("," + Constants.SR_ROLE_UPDATE.U + ",")) {
          srInsiteDTO.setBtnSplitSR(1L);
        } else {
          srInsiteDTO.setBtnSplitSR(0L);
        }
        //<editor-fold desc="namtn edit yc thang 04 - xin gia han SR" defaultstate="collapsed">
        //neu dich vu duoc phep gia han
        if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getRenewDay())
            && Long.valueOf(srCatalogDTO.getRenewDay()) > 0) {
          SRRenewDTO srRenewDto = srRenewRepository
              .findSRRenewBySrId(Long.valueOf(srInsiteDTO.getSrId()));
          List<SRConfigDTO> lstConfigRenew = srConfigRepository
              .getByConfigGroup(Constants.SR_CONFIG.STATUS_RENEW);
          if (userToken.getUserName().equalsIgnoreCase(srInsiteDTO.getSrUser()) || userToken
              .getUserName().equalsIgnoreCase(srInsiteDTO.getCreatedUser())) {
            if (userToken.getUserName().equalsIgnoreCase(srInsiteDTO.getSrUser())) {
              //neu nguoi dang nhap la nguoi xu ly
              if (srRenewDto == null) {
                //neu chua gia han lan nao
                if (lstConfigRenew != null && !lstConfigRenew.isEmpty()) {
                  for (SRConfigDTO renewConfig : lstConfigRenew) {
                    if (renewConfig.getConfigCode().equals(srInsiteDTO.getStatus())) {
                      srInsiteDTO.setBtnRenew(1L);
                      break;
                    }
                  }
                }
              }
            }
            if (userToken.getUserName().equalsIgnoreCase(srInsiteDTO.getCreatedUser())) {
              //neu nguoi dang nhap la nguoi tao
              if (srRenewDto != null
                  && (!StringUtils.isStringNullOrEmpty(srRenewDto.getStatusRenew())
                  && "0".equals(String.valueOf(srRenewDto.getStatusRenew())))) {
                Date createdDateRenew = srRenewDto.getCreatedTime();

                //neu thoi gian hien tai - thoi gian tao xin gia han <= 1 ngay
                if (((new Date()).getTime() - createdDateRenew.getTime()) / (24 * 60 * 60 * 1000d)
                    <= 1) {
                  //neu trang thai xin gia han = 0 (dang cho duyet)
                  if (lstConfigRenew != null && !lstConfigRenew.isEmpty()) {
                    for (SRConfigDTO renewConfig : lstConfigRenew) {
                      if (renewConfig.getConfigCode().equals(srInsiteDTO.getStatus())) {
                        srInsiteDTO.setBtnApproveRenew(1L);
                        break;
                      }
                    }
                  }
                }
              }
            }
          }
        }
        //</editor-fold>

      }
    }
    //<editor-fold desc="Check user dang nhap la lanh dao don vi, duyet SR cap 1, cap 2" defaultstate="collapsed">
    if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getApprove()) && !"0"
        .equals(String.valueOf(srCatalogDTO.getApprove()))) {
      SRApproveDTO srApproveDTO = srApproveRepository.findSRApproveBySrId(srInsiteDTO.getSrId());
      srInsiteDTO.setApproveDTO(srApproveDTO);
      if (srApproveDTO != null) {
        UsersInsideDto createUser = userRepository
            .getUserDTOByUserName(srInsiteDTO.getCreatedUser());
        if (Constants.SR_STATUS.UNDER_APPROVAL.equals(srInsiteDTO.getStatus())) {
          if ("1".equals(String.valueOf(srCatalogDTO.getApprove()))) {
            //neu duyet 1 cap
            if (StringUtils.isStringNullOrEmpty(srApproveDTO.getApproveLevel1())) {
              //neu chua duyet
              if (srRepository.checkUserLoginIsLeader("TP", srInsiteDTO.getCreatedUser(),
                  userToken.getUserName(), createUser.getUnitId().toString(), "1")) {
                srInsiteDTO.setBtnApproveLevel1(1L);
                srInsiteDTO.setBtnUnApproveLevel1(1L);
              }
            }
          } else {
            //neu duyet 2 cap
            if (StringUtils.isStringNullOrEmpty(srApproveDTO.getApproveLevel1())) {
              //neu chua duyet cap 1
              if (srRepository.checkUserLoginIsLeader("TP", srInsiteDTO.getCreatedUser(),
                  userToken.getUserName(), createUser.getUnitId().toString(), "1")) {
                srInsiteDTO.setBtnApproveLevel1(1L);
                srInsiteDTO.setBtnUnApproveLevel1(1L);
              }
            } else {
              //neu duyet cap 1 roi nhung chua duyet cap 2
              if (StringUtils.isStringNullOrEmpty(srApproveDTO.getApproveLevel2())) {
                if (srRepository.checkUserLoginIsLeader("TP", srInsiteDTO.getCreatedUser(),
                    userToken.getUserName(), srApproveDTO.getApproveUnitLevel1().toString(), "2")) {
                  srInsiteDTO.setBtnApproveLevel2(1L);
                  srInsiteDTO.setBtnUnApproveLevel2(1L);
                }
              }
            }
          }
        }
      }
    }
    //</editor-fold>
    boolean checkWs = false;
    srInsiteDTO.setAutoCreatCR(false);
    SRConfigDTO sRConfigDTO = new SRConfigDTO();
    sRConfigDTO.setConfigCode(srCatalogDTO.getServiceArray());
    sRConfigDTO.setConfigGroup(Constants.SR_CATALOG.SERVICE_ARRAY);
    List<SRConfigDTO> lstData = srConfigRepository.getDataByConfigCode(sRConfigDTO);
    if (lstData != null) {
      if (lstData.get(0).getAutomation() != null) {
        checkWs = true;
      }
    }
    SRMappingProcessCRDTO srDTO = new SRMappingProcessCRDTO();
    srDTO.setServiceCode(srCatalogDTO.getServiceCode());
    List<SRMappingProcessCRDTO> listSRMappingProcessCRDTO = srCategoryServiceProxy
        .getListSRMappingProcessCRDTO(srDTO);
    if (checkWs && listSRMappingProcessCRDTO != null) {
      srInsiteDTO.setAutoCreatCR(true);
    }
  }

  @Override
  public ResultInSideDto deleteSR(SrInsiteDTO dto) {
    log.info("Request to deleteSR : {}", dto);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    SrInsiteDTO srInsiteDTO = srRepository.getDetail(dto.getSrId(), userToken.getUserName());
    String roleAction = ",";
    if (Constants.SR_STATUS.DRAFT.equals(srInsiteDTO.getStatus())) {

      SRRoleUserInSideDTO searchRoleUser = new SRRoleUserInSideDTO();
      searchRoleUser.setUsername(userToken.getUserName());
      searchRoleUser.setStatus("A");
      searchRoleUser.setUnitId(srInsiteDTO.getSrUnit());
      searchRoleUser.setCountry(srInsiteDTO.getCountry());
      searchRoleUser.setRoleCode(srInsiteDTO.getRoleCode());
      List<SRRoleUserInSideDTO> lsRoleUser = srCategoryServiceProxy
          .getlistSRRoleUserDTO(searchRoleUser);
      List<SRRoleActionDTO> lstRoleUpdateSR = new ArrayList<>();
      if (lsRoleUser != null && lsRoleUser.size() > 0) {
        for (SRRoleUserInSideDTO roleUser : lsRoleUser) {
          if ((!StringUtils.isStringNullOrEmpty(srInsiteDTO.getSrUser()) && userToken.getUserName()
              .equalsIgnoreCase(srInsiteDTO.getSrUser()))
              || (StringUtils.isStringNullOrEmpty(srInsiteDTO.getSrUser())) && srInsiteDTO
              .getSrUnit().equals(roleUser.getUnitId())) {
            //load quyen cap nhat SR
            SRRoleActionDTO dtoActionUpdate = new SRRoleActionDTO();
            dtoActionUpdate.setCountry(srInsiteDTO.getCountry());
            dtoActionUpdate.setFlowId(
                StringUtils.isNotNullOrEmpty(dto.getFlowExecute()) ? Long
                    .valueOf(dto.getFlowExecute())
                    : null);
            dtoActionUpdate.setCurrentStatus(srInsiteDTO.getStatus());
            dtoActionUpdate.setRoleType("UPDATE_SR");
            dtoActionUpdate.setGroupRole(Constants.SR_ROLE.IMPLEMENTOR);
            //tim loai hanh dong, nhom quyen, quoc gia, luong xu ly, trang thai hien tai => lay ra hanh dong va trang thai tiep theo
            List<SRRoleActionDTO> lstDataUpdateSR = srCategoryServiceProxy
                .getListSRRoleActionDTO(dtoActionUpdate);
            if (lstDataUpdateSR != null && !lstDataUpdateSR.isEmpty()) {
              lstRoleUpdateSR.addAll(lstDataUpdateSR);
            }
          }
          if ("1".equals(String.valueOf(roleUser.getIsLeader()))) {
            SRRoleActionDTO dtoActionUpdate = new SRRoleActionDTO();
            dtoActionUpdate.setCountry(srInsiteDTO.getCountry());
            dtoActionUpdate.setFlowId(
                StringUtils.isNotNullOrEmpty(dto.getFlowExecute()) ? Long
                    .valueOf(dto.getFlowExecute())
                    : null);
            dtoActionUpdate.setCurrentStatus(srInsiteDTO.getStatus());
            dtoActionUpdate.setGroupRole(Constants.SR_ROLE.MANAGER);
            List<SRRoleActionDTO> lstDataUpdateSR = srCategoryServiceProxy
                .getListSRRoleActionDTO(dtoActionUpdate);
            if (lstDataUpdateSR != null && !lstDataUpdateSR.isEmpty()) {
              lstRoleUpdateSR.addAll(lstDataUpdateSR);
            }
          }
        }
      }
      if (userToken.getUserName().equalsIgnoreCase(srInsiteDTO.getCreatedUser())) {
        SRRoleActionDTO dtoActionUpdate = new SRRoleActionDTO();
        dtoActionUpdate.setCountry(srInsiteDTO.getCountry());
        dtoActionUpdate.setFlowId(
            StringUtils.isNotNullOrEmpty(dto.getFlowExecute()) ? Long.valueOf(dto.getFlowExecute())
                : null);
        dtoActionUpdate.setCurrentStatus(srInsiteDTO.getStatus());
        dtoActionUpdate.setRoleType("UPDATE_SR");
        dtoActionUpdate.setGroupRole(Constants.SR_ROLE.ORIGINATOR);
        List<SRRoleActionDTO> lstDataUpdateSR = srCategoryServiceProxy
            .getListSRRoleActionDTO(dtoActionUpdate);
        lstRoleUpdateSR.addAll(lstDataUpdateSR != null ? lstDataUpdateSR : new ArrayList<>());
      }

      if (!lstRoleUpdateSR.isEmpty()) {
        for (SRRoleActionDTO rolAction : lstRoleUpdateSR) {
          String action = rolAction.getActions();
          if (!StringUtils.isStringNullOrEmpty(action)) {
            String[] arrAction = action.split(",");
            for (int i = 0; i < arrAction.length; i++) {
              if (!(roleAction).contains("," + arrAction[i] + ",")) {
                roleAction += arrAction[i] + ",";
              }
            }
          }
        }
      }
      // Neu khong co quyen xoa SR
      if (!(roleAction).contains("," + Constants.SR_ROLE_UPDATE.D + ",")) {
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getValidation("sr.right.delete.not"));
        return resultInSideDto;
      }
      resultInSideDto = srRepository.deleteSR(srInsiteDTO.getSrId());
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteSRChild(SrInsiteDTO srChild) {
    log.info("Request to deleteSRChild : {}", srChild);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.FAIL);
    UserToken userToken = ticketProvider.getUserToken();
    SRRoleUserInSideDTO searchRoleUser = new SRRoleUserInSideDTO();
    searchRoleUser.setUsername(userToken.getUserName());
    List<SRRoleUserInSideDTO> lsRoleUser = srCategoryServiceProxy
        .getlistSRRoleUserDTO(searchRoleUser);
    if (Constants.SR_STATUS.DRAFT.equals(srChild.getStatus()) || Constants.SR_STATUS.REJECTED
        .equals(srChild.getStatus())) {
      String roleAction = ",";
      if (lsRoleUser != null && lsRoleUser.size() > 0) {
        List<SRRoleUserInSideDTO> lstRoleUserCoppy = new ArrayList<SRRoleUserInSideDTO>(lsRoleUser);
        if (userToken.getUserName().equalsIgnoreCase(srChild.getCreatedUser())) {
          SRRoleUserInSideDTO objAdd = new SRRoleUserInSideDTO();
          objAdd.setRoleCode("Orinirator");
          lstRoleUserCoppy.add(objAdd);
        }
        if (lstRoleUserCoppy != null) {
          for (SRRoleUserInSideDTO item : lstRoleUserCoppy) {
            SRRoleActionDTO searchRoleAction = new SRRoleActionDTO();
            searchRoleAction.setRoleCode(item.getRoleCode());
            searchRoleAction.setRoleType("UPDATE_SR");
            List<SRRoleActionDTO> lstRoleAction = srCategoryServiceProxy
                .getListSRRoleActionDTO(searchRoleAction);
            if (lstRoleAction != null) {
              for (SRRoleActionDTO rolAction : lstRoleAction) {
                String action = rolAction.getActions();
                if (!StringUtils.isStringNullOrEmpty(action)) {
                  String[] arrAction = action.split(",");
                  int size = arrAction.length;
                  for (int i = 0; i < size; i++) {
                    if (!(roleAction).contains("," + arrAction[i] + ",")) {
                      roleAction += arrAction[i] + ",";
                    }
                  }
                }
              }
            }
          }
        }
      } else if ((lsRoleUser == null || lsRoleUser.isEmpty()) && userToken.getUserName()
          .equalsIgnoreCase(srChild.getCreatedUser())) {
        SRRoleActionDTO searchRoleAction = new SRRoleActionDTO();
        searchRoleAction.setRoleCode("Orinirator");
        searchRoleAction.setRoleType("UPDATE_SR");
        List<SRRoleActionDTO> lstRoleAction = srCategoryServiceProxy
            .getListSRRoleActionDTO(searchRoleAction);
        if (lstRoleAction != null) {
          for (SRRoleActionDTO rolAction : lstRoleAction) {
            String action = rolAction.getActions();
            if (!StringUtils.isStringNullOrEmpty(action)) {
              String[] arrAction = action.split(",");
              int size = arrAction.length;
              for (int i = 0; i < size; i++) {
                if (!(roleAction).contains("," + arrAction[i] + ",")) {
                  roleAction += arrAction[i] + ",";
                }
              }
            }
          }
        }
      }
      if (!(roleAction).contains("," + Constants.SR_ROLE_UPDATE.D + ",")) {
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage(I18n.getValidation("sr.right.delete.not"));
        return resultInSideDto;
      }
      resultInSideDto = srRepository.deleteSR(srChild.getSrId());
      if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
        resultInSideDto.setMessage(
            I18n.getLanguage("common.button.delete") + " " + I18n.getLanguage("common.success"));
        return resultInSideDto;
      } else {
        resultInSideDto.setMessage(
            I18n.getLanguage("common.button.delete") + " " + I18n.getLanguage("common.fail"));
        return resultInSideDto;
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto roleActionSRChild(String createdUser) {
    log.info("Request to roleActionSRChild : {}", createdUser);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    SRRoleUserInSideDTO searchRoleUser = new SRRoleUserInSideDTO();
    searchRoleUser.setUsername(userToken.getUserName());
    List<SRRoleUserInSideDTO> lsRoleUser = srCategoryServiceProxy
        .getlistSRRoleUserDTO(searchRoleUser);
    String roleAction = ",R,";
    if (lsRoleUser != null && lsRoleUser.size() > 0) {
      List<SRRoleUserInSideDTO> lstRoleUserCoppy = new ArrayList<>(lsRoleUser);
      if (userToken.getUserName().equalsIgnoreCase(createdUser)) {
        SRRoleUserInSideDTO objAdd = new SRRoleUserInSideDTO();
        objAdd.setRoleCode("Orinirator");
        lstRoleUserCoppy.add(objAdd);
      }
      if (lstRoleUserCoppy != null) {
        for (SRRoleUserInSideDTO item : lstRoleUserCoppy) {
          SRRoleActionDTO searchRoleAction = new SRRoleActionDTO();
          searchRoleAction.setRoleCode(item.getRoleCode());
          searchRoleAction.setRoleType("UPDATE_SR");
          List<SRRoleActionDTO> lstRoleAction = srCategoryServiceProxy
              .getListSRRoleActionDTO(searchRoleAction);
          if (lstRoleAction != null) {
            for (SRRoleActionDTO rolAction : lstRoleAction) {
              String action = rolAction.getActions();
              if (!StringUtils.isStringNullOrEmpty(action)) {
                String[] arrAction = action.split(",");
                int size = arrAction.length;
                for (int i = 0; i < size; i++) {
                  if (!(roleAction).contains("," + arrAction[i] + ",")) {
                    roleAction += arrAction[i] + ",";
                  }
                }
              }
            }
          }
        }
      }
    }
    if ((roleAction).contains("," + Constants.SR_ROLE_UPDATE.R + ",") || (roleAction)
        .contains("," + Constants.SR_ROLE_UPDATE.U + ",")) {
      resultInSideDto.setKey(RESULT.SUCCESS);
      resultInSideDto.setMessage(roleAction);
    } else {
      resultInSideDto.setKey(RESULT.FAIL);
      resultInSideDto.setMessage(I18n.getValidation("sr.right.view.not"));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertListSRParam(List<SRParamDTO> lsSrParam) {
    log.info("Request to insertListSRParam : {}", lsSrParam);
    return srParamRepository.insertListSRParam(lsSrParam);
  }

  @Override
  public List<SRParamEntity> findListSRParamBySrId(Long srId) {
    log.info("Request to findListSRParamBySrId : {}", srId);
    return srParamRepository.findListSRParamBySrId(srId);
  }

  @Override
  public List<SRMopDTO> getListSRMopDTO(SRMopDTO srMopDTO) {
    log.info("Request to getListSRMopDTO : {}", srMopDTO);
    return srMopRepository.getListSRMopDTO(srMopDTO);
  }

  @Override
  public List<SRMopDTO> getListSRMopNotSR(SRMopDTO srMopDTO) {
    return srMopRepository.getListSRMopNotSR(srMopDTO);
  }

  @Override
  public ResultInSideDto insertListSRMop(List<SRMopDTO> lsSrMop) {
    log.info("Request to insertListSRMop : {}", lsSrMop);
    return srMopRepository.insertListSRMop(lsSrMop);
  }

  @Override
  public ResultInSideDto updateListSRMop(List<SRMopDTO> lsSrMop) {
    log.info("Request to updateListSRMop : {}", lsSrMop);
    return srMopRepository.updateListSRMop(lsSrMop);
  }

  @Override
  public ResultInSideDto deleteListSRMop(List<SRMopDTO> lsSrMop) {
    log.info("Request to deleteListSRMop : {}", lsSrMop);
    return srMopRepository.deleteListSRMop(lsSrMop);
  }

  @Override
  public ResultInSideDto insertOrUpdateSREvaluate(SREvaluateDTO srEvaluateDTO) {
    log.info("Request to insertOrUpdateSREvaluate : {}", srEvaluateDTO);
    if (!StringUtils.isStringNullOrEmpty(srEvaluateDTO.getEvaluateId())) {
      return srEvaluateRepository.updateSREvaluate(srEvaluateDTO);
    } else {
      return srEvaluateRepository.insertSREvaluate(srEvaluateDTO);
    }
  }

  @Override
  public List<SREvaluateDTO> findSREvaluateBySrId(Long srId) {
    log.info("Request to findSREvaluateBySrId : {}", srId);
    return srEvaluateRepository.findSREvaluateBySrId(srId);
  }

  @Override
  public List<OdSearchInsideDTO> findListOdBySr(Long srId) {
    log.info("Request to findListOdBySr : {}", srId);
    UserToken userToken = ticketProvider.getUserToken();
    OdSearchInsideDTO odDTOSearch = new OdSearchInsideDTO();
    odDTOSearch.setUserId(userToken.getUserID().toString());
    odDTOSearch.setInsertSource("SR");
    odDTOSearch.setOtherSystemCode(srId.toString());
    odDTOSearch.setPage(1);
    odDTOSearch.setPageSize(Integer.MAX_VALUE);
    return odServiceProxy.getListDataSearchForOther(odDTOSearch);
  }

  @Override
  public List<WoDTOSearch> findListWoBySr(SrInsiteDTO srDto) {
    log.info("Request to findListWoBySr : {}", srDto);
    try {
      List<WoDTOSearch> lst = new ArrayList<>();
      UserToken userToken = ticketProvider.getUserToken();

      //tim WO tao tu SR
      WoDTOSearch dtoSearch = new WoDTOSearch();
      dtoSearch.setWoSystem("SR");
      dtoSearch.setWoSystemId(srDto.getSrId().toString());
      dtoSearch.setUserId(userToken.getUserID().toString());
      dtoSearch.setPage(1);
      dtoSearch.setPageSize(Integer.MAX_VALUE);
      List<WoDTOSearch> lstWoSearch = woServiceProxy.getListDataSearchProxy(dtoSearch);
      if (lstWoSearch != null && !lstWoSearch.isEmpty()) {
        lst.addAll(lstWoSearch);
      }

      //tim WO tick help tao tu WS SR
      SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO = new SRCreatedFromOtherSysDTO();
      srCreatedFromOtherSysDTO.setSrCode(srDto.getSrCode());
      srCreatedFromOtherSysDTO.setSystemName("WO");
      List<SRCreatedFromOtherSysDTO> lstWoTickHelp = srCreatedFromOtherSysRepository
          .getListSRCreatedFromOtherSysDTO(srCreatedFromOtherSysDTO);
      if (lstWoTickHelp != null && !lstWoTickHelp.isEmpty()) {
        for (SRCreatedFromOtherSysDTO item : lstWoTickHelp) {
          WoInsideDTO woInsideDTO = woServiceProxy.findWoByIdProxy(item.getObjectId());
          if (woInsideDTO != null && woInsideDTO.toWoDTOSearch() != null) {
            lst.add(woInsideDTO.toWoDTOSearch());
          }
        }
      }
      return lst;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<CrInsiteDTO> findListCrBySr(Long srId) {
    log.info("Request to findListCrBySr : {}", srId);
    CrInsiteDTO crInsiteDTO = new CrInsiteDTO();
    crInsiteDTO.setSystemId(5L);
    crInsiteDTO.setObjectId(srId);
    crInsiteDTO.setPage(1);
    crInsiteDTO.setPageSize(Integer.MAX_VALUE);
    return crServiceProxy.getListCRFromOtherSystemOfSR(crInsiteDTO);
  }

  @Override
  public List<UnitSRCatalogDTO> getListUnitSRCatalog(SRCatalogDTO dto) {
    log.info("Request to getListUnitSRCatalog : {}", dto);
    return srCatalogRepository2.getListUnitSRCatalog(dto);
  }

  @Override
  public String fileByPath(SrWsToolCrDTO srWsToolCrDTO) throws Exception {
    log.info("Request to fileByPath : {}", srWsToolCrDTO.getFileType(),
        srWsToolCrDTO.getTemplateId());
    String fileTemplate = "";
    if (StringUtils.isNotNullOrEmpty(srWsToolCrDTO.getFileType())
        && (Constants.SR_ROLE_UPDATE.FILE_TYPE_OPEN_CONNECT.equals(srWsToolCrDTO.getFileType())
        || Constants.SR_ROLE_UPDATE.NIMS.equals(srWsToolCrDTO.getFileType())
        || Constants.SR_ROLE_UPDATE.AOM.equals(srWsToolCrDTO.getFileType())
        || Constants.SR_ROLE_UPDATE.FILE_TYPE_TOOL.equals(srWsToolCrDTO.getFileType()))) {
      if (Constants.SR_ROLE_UPDATE.FILE_TYPE_OPEN_CONNECT.equals(srWsToolCrDTO.getFileType())) {
        fileTemplate =
            "templates" + File.separator + I18n.getLanguage("sr.template.vipa") + ".xlsx";
      } else if (Constants.SR_ROLE_UPDATE.NIMS.equals(srWsToolCrDTO.getFileType())) {
        fileTemplate =
            "templates" + File.separator + I18n.getLanguage("sr.template.nims") + ".xlsx";
      } else if (Constants.SR_ROLE_UPDATE.AOM.equals(srWsToolCrDTO.getFileType())) {
        fileTemplate = "templates" + File.separator + I18n.getLanguage("sr.template.aom") + ".xlsx";
      } else if (Constants.SR_ROLE_UPDATE.FILE_TYPE_TOOL.equals(srWsToolCrDTO.getFileType())) {
        fileTemplate = Constants.SR_ROLE_UPDATE.FILE_TYPE_TOOL;
      }
      if (!StringUtils.isStringNullOrEmpty(fileTemplate) && !fileTemplate
          .equals(Constants.SR_ROLE_UPDATE.FILE_TYPE_TOOL)) {
        return fileTemplate;
      } else if (fileTemplate.equals(Constants.SR_ROLE_UPDATE.FILE_TYPE_TOOL)) {
        List<SRMappingProcessCRDTO> lstSRMappingProcess = srWsToolCrDTO.getLstSRMappingProcess();
        List<SRConfigDTO> lstDataCountry = srWsToolCrDTO.getLstDataCountry();
        List<SRConfigDTO> lstData = srWsToolCrDTO.getLstData();
        ObjectMapper mapper = new ObjectMapper();
        List<com.viettel.aam2.FlowTemplateGNOCObjDTO> lstAAM = new ArrayList<>();
        if (StringUtils.isNotNullOrEmpty(srWsToolCrDTO.getLstAAM())) {
          List<Object> objectAAMS = (List<Object>) mapper
              .readValue(srWsToolCrDTO.getLstAAM(), ArrayList.class);
          if (objectAAMS != null && !objectAAMS.isEmpty()) {
            for (Object obj : (List<Object>) objectAAMS) {
              LinkedHashMap<String, Object> temp = (LinkedHashMap<String, Object>) obj;
              com.viettel.aam2.FlowTemplateGNOCObjDTO aamDTO = new com.viettel.aam2.FlowTemplateGNOCObjDTO();
              aamDTO.setResultCode(((Integer) temp.get("resultCode")) == null ? 0
                  : ((Integer) temp.get("resultCode")).intValue());
              aamDTO.setResultMessage((String) temp.get("resultMessag"));
              aamDTO.setTempFileContent((String) temp.get("tempFileContent"));
              aamDTO.setTempFileName((String) temp.get("tempFileName"));
              aamDTO.setTemplateId((String) temp.get("templateId"));
              lstAAM.add(aamDTO);
            }
          }
        }
        if (lstDataCountry == null || lstDataCountry.isEmpty()) {
          return null;
        }
        if (lstSRMappingProcess == null || lstSRMappingProcess.isEmpty()) {
          return null;
        }
        String configName = lstDataCountry.get(0).getConfigName();
        if (lstData != null && !lstData.isEmpty()) {
          String logWS = null;
          if (Constants.SR_CONFIG.VMSA.equals(lstData.get(0).getAutomation())) {
            if (StringUtils.isNotNullOrEmpty(srWsToolCrDTO.getTemplateId())) {
              List<SRConfigDTO> lstLink = srConfigRepository
                  .getByConfigGroup(Constants.SR_CONFIG.WS_VMSA_URL);

              List<SRConfigDTO> lstUsername = srConfigRepository
                  .getByConfigGroup(Constants.SR_CONFIG.WS_VMSA_USERNAME);
              String usernameVMSA = stringDecrypt(userNameVMSA, saltService);
              if (lstUsername != null && !lstUsername.isEmpty()) {
                usernameVMSA = lstUsername.get(0).getConfigCode();
              }
              String pVMSA = stringDecrypt(passVMSA, saltService);
              List<SRConfigDTO> lstPassword = srConfigRepository
                  .getByConfigGroup(Constants.SR_CONFIG.WS_VMSA_P);
              if (lstPassword != null && !lstPassword.isEmpty()) {
                pVMSA = lstPassword.get(0).getConfigCode();
              }
              URL url = new URL(lstLink.get(0).getConfigCode());
              com.viettel.vmsa2.FlowTemplateGNOCObjDTO vmsaResult = null;
              try {
                log.info("--------- getTempFileContentForTempId VMSA START ---------");
                com.viettel.vmsa2.WSForGNOCImplService serviceVMSA = new com.viettel.vmsa2.WSForGNOCImplService(
                    url);
                com.viettel.vmsa2.WSForGNOC port = serviceVMSA.getWSForGNOCImplPort();
                setTimeOutWs(port);
                logWS = null;
                logWS = "\n <userService>" + usernameVMSA + "</userService>"
                    + "\n <passService>" + pVMSA + "</passService>"
                    + "\n <countryCode>" + configName + "</countryCode>"
                    + "\n <templateId>" + srWsToolCrDTO.getTemplateId() + "</templateId>";
                log.info("CALL WS VMSA getTempFileContentForTempId :" + logWS);
                vmsaResult = port
                    .getTempFileContentForTempId(usernameVMSA, pVMSA, configName,
                        srWsToolCrDTO.getTemplateId());
                log.info("--------- getTempFileContentForTempId VMSA END ---------");
                if (vmsaResult != null) {
                  logWS = null;
                  logWS = "\n resultCode : " + vmsaResult.getResultCode()
                      + "\n resultMessage : " + vmsaResult.getResultMessage()
                      + "\n tempFileName : " + vmsaResult.getTempFileName()
                      + "\n templateId : " + vmsaResult.getTemplateId()
                      + "\n templateName : " + vmsaResult.getTemplateName();
                  if (vmsaResult.getResultCode() == 0 && StringUtils
                      .isNotNullOrEmpty(vmsaResult.getTempFileContent())) {
                    logWS += "\n tempFileContent : " + vmsaResult.getTempFileContent();
                    log.info("Result File template SR_CR_TOOL VMSA" + logWS);
                    byte[] decoded = Base64
                        .decodeBase64(vmsaResult.getTempFileContent());
                    if (decoded != null) {
                      String fullPath = FileUtils
                          .saveTempFile(vmsaResult.getTempFileName(), decoded, tempFolder);
                      return fullPath;
                    }
                  }
                  log.info("Result File template SR_CR_TOOL VMSA" + logWS);
                }
              } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.info("link VMSA timeout: " + lstLink.get(0).getConfigCode());
              }
            }
          } else if (Constants.SR_CONFIG.AAM.equals(lstData.get(0).getAutomation())) {
            if (StringUtils.isNotNullOrEmpty(srWsToolCrDTO.getTemplateId())) {
              for (com.viettel.aam2.FlowTemplateGNOCObjDTO flowDto : lstAAM) {
                if (srWsToolCrDTO.getTemplateId().equals(flowDto.getTemplateId())) {
                  if (flowDto.getResultCode() == 0
                      && flowDto.getTempFileContent() != null) {
                    byte[] decoded = Base64
                        .decodeBase64(flowDto.getTempFileContent());
                    if (decoded != null) {
                      String fullPath = FileUtils
                          .saveTempFile(flowDto.getTempFileName(), decoded, tempFolder);
                      return fullPath;
                    }
                  }
                }
              }
            }
          } else if (Constants.SR_CONFIG.VIPA.equals(lstData.get(0).getAutomation())) {
            if (StringUtils.isNotNullOrEmpty(srWsToolCrDTO.getTemplateId())) {
              List<SRConfigDTO> lstLink = srConfigRepository
                  .getByConfigGroup(Constants.SR_CONFIG.WS_VIPA_URL);

              List<SRConfigDTO> lstUsername = srConfigRepository
                  .getByConfigGroup(Constants.SR_CONFIG.WS_VIPA_USERNAME);
              String usernameVIPA = stringDecrypt(userNameVIPA, saltService);
              if (lstUsername != null && !lstUsername.isEmpty()) {
                usernameVIPA = lstUsername.get(0).getConfigCode();
              }
              String pVIPA = stringDecrypt(passVIPA, saltService);
              List<SRConfigDTO> lstPassword = srConfigRepository
                  .getByConfigGroup(Constants.SR_CONFIG.WS_VIPA_P);
              if (lstPassword != null && !lstPassword.isEmpty()) {
                pVIPA = lstPassword.get(0).getConfigCode();
              }

              URL url = new URL(lstLink.get(0).getConfigCode());
              com.viettel.vipa2.FlowTemplateGNOCObjDTO vipaResult = null;
              try {
                log.info("--------- getTempFileContentForTempId VIPA START ---------");
                com.viettel.vipa2.WSForGNOCImplService serviceVMSA = new com.viettel.vipa2.WSForGNOCImplService(
                    url);
                com.viettel.vipa2.WSForGNOC port = serviceVMSA.getWSForGNOCImplPort();
                setTimeOutWs(port);
                logWS = null;
                logWS = "\n <userService>" + usernameVIPA + "</userService>"
                    + "\n <passService>" + pVIPA + "</passService>"
                    + "\n <countryCode>" + configName + "</countryCode>"
                    + "\n <templateId>" + srWsToolCrDTO.getTemplateId() + "</templateId>";
                log.info("CALL WS VMSA getTempFileContentForTempId :" + logWS);
                vipaResult = port
                    .getTempFileContentForTempId(usernameVIPA, pVIPA, configName,
                        srWsToolCrDTO.getTemplateId());
                log.info("--------- getTempFileContentForTempId VIPA END ---------");
                if (vipaResult != null) {
                  logWS = null;
                  logWS = "\n resultCode : " + vipaResult.getResultCode()
                      + "\n resultMessage : " + vipaResult.getResultMessage()
                      + "\n tempFileName : " + vipaResult.getTempFileName()
                      + "\n templateId : " + vipaResult.getTemplateId()
                      + "\n templateName : " + vipaResult.getTemplateName();
                  if (vipaResult.getResultCode() == 0 && StringUtils
                      .isNotNullOrEmpty(vipaResult.getTempFileContent())) {
                    logWS += "\n tempFileContent : " + vipaResult.getTempFileContent();
                    log.info("Result File template SR_CR_TOOL VIPA" + logWS);
                    byte[] decoded = Base64
                        .decodeBase64(vipaResult.getTempFileContent());
                    if (decoded != null) {
                      String fullPath = FileUtils
                          .saveTempFile(vipaResult.getTempFileName(), decoded, tempFolder);
                      return fullPath;
                    }
                  }
                  log.info("Result File template SR_CR_TOOL VIPA" + logWS);
                }
              } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.info("link VIPA timeout: " + lstLink.get(0).getConfigCode());
              }
            }
          }
        }
      }
    }
    return fileTemplate;
  }

  @Override
  public List<GnocFileDto> getListSRDialogFile(GnocFileDto gnocFileDto) {
    log.info("Request to getListSRDialogFile : {}", gnocFileDto);
    gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.SR_CATALOG);
    return gnocFileRepository.getListGnocFileForSR(gnocFileDto);
  }

  @Override
  public ResultInSideDto insertSRDialogFile(List<MultipartFile> srFileList,
      SrInsiteDTO srInsiteDTO) {
    log.info("Request to insertSRDialogFile : {}", srFileList, srInsiteDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    try {
      if (srFileList != null && !srFileList.isEmpty()) {
        for (GnocFileDto gnocFileDtoAdd : srInsiteDTO.getGnocFileDtosAdd()) {
          if (gnocFileDtoAdd.getIndexFile() != null) {
            Date date = new Date();
            MultipartFile multipartFile = srFileList.get(gnocFileDtoAdd.getIndexFile().intValue());
            String fullPath = FileUtils
                .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                    multipartFile.getBytes(), date);
            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setPath(fullPath);
            gnocFileDto.setBytes(multipartFile.getBytes());
            gnocFileDto.setFileName(multipartFile.getOriginalFilename());
            gnocFileDto.setCreateUnitId(userToken.getDeptId());
            gnocFileDto.setCreateUnitName(unitToken.getUnitName());
            gnocFileDto.setCreateUserId(userToken.getUserID());
            gnocFileDto.setCreateUserName(userToken.getUserName());
            gnocFileDto.setCreateTime(date);
            gnocFileDto.setFileType(gnocFileDtoAdd.getFileType());
            gnocFileDto.setTemplateId(gnocFileDtoAdd.getTemplateId());
            gnocFileDto.setMappingId(null);
            if (gnocFileDtoAdd.getRequired() != null && gnocFileDtoAdd.getRequired() == 1L) {
              if (gnocFileDto.getFileName().isEmpty()) {
                resultInSideDto.setKey(RESULT.FAIL);
                resultInSideDto.setMessage(
                    I18n.getValidation("sr.file.not.attach") + " file " + gnocFileDtoAdd
                        .getFileType());
                return resultInSideDto;
              }
            }
            gnocFileDto.setRequired(null);
            byte[] bytes = FileUtils
                .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), gnocFileDtoAdd.getPathTemplate());
            String pathTemplate = FileUtils
                .saveTempFile(FileUtils.getFileName(gnocFileDtoAdd.getPathTemplate()), bytes,
                    tempFolder);
            String filePathUpload = FileUtils
                .saveTempFile(gnocFileDto.getFileName(), gnocFileDto.getBytes(), tempFolder);
            if (pathTemplate.substring(pathTemplate.lastIndexOf("."), pathTemplate.length())
                .equalsIgnoreCase(filePathUpload
                    .substring(filePathUpload.lastIndexOf("."), filePathUpload.length()))) {
              if (pathTemplate.toLowerCase().endsWith(".xlsx") || pathTemplate.toLowerCase()
                  .endsWith(".xls")) {
                if (!getFileData(pathTemplate, filePathUpload)) {
                  resultInSideDto.setKey(RESULT.FAIL);
                  resultInSideDto
                      .setMessage(I18n.getValidation("srRoleAction.import.errorTemplate") + " : "
                          + gnocFileDto.getFileName());
                  return resultInSideDto;
                }
              }
            } else {
              resultInSideDto.setKey(RESULT.FAIL);
              resultInSideDto
                  .setMessage(I18n.getValidation(
                      I18n.getValidation("srRoleAction.import.errorTemplate") + " : "
                          + gnocFileDto.getFileName()));
              return resultInSideDto;
            }
            gnocFileDtos.add(gnocFileDto);
          }
        }
        if (gnocFileDtos != null && gnocFileDtos.size() > 0) {
          for (GnocFileDto insertFile : gnocFileDtos) {
            String fullPathOld = FileUtils
                .saveUploadFile(insertFile.getFileName(), insertFile.getBytes(), uploadFolder,
                    insertFile.getCreateTime());
            SRFilesDTO srFilesDTO = new SRFilesDTO();
            srFilesDTO.setFilePath(fullPathOld);
            srFilesDTO.setFileName(FileUtils.getFileName(fullPathOld));
            srFilesDTO.setFileGroup(SR_CONFIG.FILE_GROUP_SR);
            srFilesDTO.setObejctId(srInsiteDTO.getSrId());
            srFilesDTO.setFileType(insertFile.getFileType());
            srFilesDTO.setRequireCreateSR(insertFile.getRequired());
            ResultInSideDto resultFileDataOld = srRepository.addSRFile(srFilesDTO);
            insertFile.setMappingId(resultFileDataOld.getId());
          }
          resultInSideDto = gnocFileRepository
              .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SR, srInsiteDTO.getSrId(),
                  gnocFileDtos);
        }
      } else {
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage(
            I18n.getValidation("sr.file.not.attach") + " file ");
        return resultInSideDto;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return resultInSideDto;
  }

  private SrWsToolCrDTO checkList(String service) {
    if (StringUtils.isNotNullOrEmpty(service)) {
      SrWsToolCrDTO wsToolCrDTO = new SrWsToolCrDTO();
      try {
        SRCatalogDTO srCatalogDTO = srCatalogRepository2.findById(Long.parseLong(service));
        if (srCatalogDTO != null) {
          SRMappingProcessCRDTO dto = new SRMappingProcessCRDTO();
          dto.setServiceCode(srCatalogDTO.getServiceCode());
          wsToolCrDTO.setLstSRMappingProcess(srCategoryServiceProxy
              .getListSRMappingProcessCRDTO(dto));

          SRConfigDTO sRConfigDTO = new SRConfigDTO();
          sRConfigDTO.setConfigCode(srCatalogDTO.getCountry());
          sRConfigDTO.setConfigGroup(Constants.SR_CONFIG.MAP_COUNTRY);
          wsToolCrDTO.setLstDataCountry(srConfigRepository
              .getDataByConfigCode(sRConfigDTO));

          sRConfigDTO = new SRConfigDTO();
          sRConfigDTO.setConfigCode(srCatalogDTO.getServiceArray());
          sRConfigDTO.setConfigGroup(Constants.SR_CATALOG.SERVICE_ARRAY);
          wsToolCrDTO.setLstData(srConfigRepository.getDataByConfigCode(sRConfigDTO));
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      return wsToolCrDTO;
    }
    return null;
  }

  public ResultInSideDto validateSRDTO(boolean createNew, SrInsiteDTO srInsiteDTO) {
    ResultInSideDto res = new ResultInSideDto();
    res.setKey(RESULT.FAIL);
    ResultInSideDto autoCreateCRResult = new ResultInSideDto();
    autoCreateCRResult.setKey(RESULT.FAIL);
    SrInsiteDTO oldData = createNew ? new SrInsiteDTO() : getSRDetail(srInsiteDTO);
    String oldStatus = oldData.getStatus();
    String newStatus = srInsiteDTO.getStatus();
    if (StringUtils.isStringNullOrEmpty(srInsiteDTO.getRoleCode())) {
      res.setMessage(I18n.getLanguage("SR.roleCode") + I18n.getLanguage("SR.notnull"));
      return res;
    }
    if (createNew || (!createNew && Constants.SR_STATUS.DRAFT.equals(oldStatus))
        && Constants.SR_STATUS.DRAFT.equals(newStatus)) {
      if (DateUtil.compareDateTime(DateUtil.sysDate(), srInsiteDTO.getStartTime()) == 1) {
        res.setMessage(I18n.getLanguage("SR.startTime") + I18n.getLanguage("SR.greater") + I18n
            .getLanguage("SR.currentTime"));
        return res;
      }
    }
    if (DateUtil.compareDateTime(srInsiteDTO.getStartTime(), srInsiteDTO.getEndTime()) == 1) {
      res.setMessage(I18n.getLanguage("SR.startTime") + I18n.getLanguage("SR.less") + " " + I18n
          .getLanguage("SR.endTime"));
      return res;
    }
    UserToken userToken = ticketProvider.getUserToken();
    if (Constants.SR_STATUS.CLOSED.equals(newStatus)) {
      if (StringUtils.isStringNullOrEmpty(srInsiteDTO.getEvaluate())) {
        res.setMessage(I18n.getLanguage("SR.evaluate.evaluate") + I18n.getLanguage("SR.notnull"));
        return res;
      }
      if (StringUtils.isStringNullOrEmpty(srInsiteDTO.getReviewId())) {
        res.setMessage(I18n.getLanguage("SR.comment") + I18n.getLanguage("SR.notnull"));
        return res;
      }
      //<editor-fold desc="namtn edit Phai dong toan bo CR, OD va WO truoc khi dong SR">
      //lay danh sach CR tuong ung
      List<CrInsiteDTO> lstCr = findListCrBySr(srInsiteDTO.getSrId());

      //lay danh sach WO tuong ung
      WoDTOSearch dtoSearch = new WoDTOSearch();
      dtoSearch.setUserId(userToken.getUserID().toString());
      dtoSearch.setWoSystem("SR");
      dtoSearch.setWoSystemId(srInsiteDTO.getSrId().toString());
      dtoSearch.setPageSize(Integer.MAX_VALUE);
      dtoSearch.setPage(1);

      List<WoDTOSearch> lstWo = woServiceProxy.getListDataSearchProxy(dtoSearch);

      List<OdSearchInsideDTO> lstOd = findListOdBySr(srInsiteDTO.getSrId());
      if (lstCr != null && !lstCr.isEmpty()) {
        for (CrInsiteDTO item : lstCr) {
          if (!"9".equals(item.getState())) {
            res.setMessage(I18n.getValidation("sr.cr.notClose"));
            return res;
          }
        }
      }
      if (lstWo != null && !lstWo.isEmpty()) {
        for (WoDTOSearch item : lstWo) {
          if (!"8".equals(item.getStatus())) {
            res.setMessage(I18n.getValidation("sr.wo.notClose"));
            return res;
          }
        }
      }
      //namtn adding OD
      if (lstOd != null && !lstOd.isEmpty()) {
        for (OdSearchInsideDTO item : lstOd) {
          if (!"7".equals(item.getStatus())) {
            res.setMessage(I18n.getValidation("sr.od.notClose"));
            return res;
          }
        }
      }
      //namtn adding OD
      //</editor-fold>
    }

    SRCatalogDTO srCatalogDTO = srCatalogRepository2
        .findById(Long.valueOf(srInsiteDTO.getServiceId()));
    srInsiteDTO.setFlowExecuteId(srCatalogDTO.getFlowExecute());
    if (!createNew) {
      //lay danh sach CR tuong ung
      List<CrInsiteDTO> lstCr = findListCrBySr(srInsiteDTO.getSrId());

      //lay danh sach WO tuong ung
      WoDTOSearch dtoSearch = new WoDTOSearch();
      dtoSearch.setUserId(userToken.getUserID().toString());
      dtoSearch.setWoSystem("SR");
      dtoSearch.setWoSystemId(srInsiteDTO.getSrId().toString());
      dtoSearch.setPageSize(Integer.MAX_VALUE);
      dtoSearch.setPage(1);

      List<WoDTOSearch> lstWo = woServiceProxy.getListDataSearchProxy(dtoSearch);

      List<OdSearchInsideDTO> lstOd = findListOdBySr(srInsiteDTO.getSrId());

      //<editor-fold desc="validate them moi file dinh kem, worklog, tao cr, tao wo">
      List<SRRoleActionDTO> lstRoleRequireSR = new ArrayList<>();

      SRRoleUserInSideDTO dtoRole = new SRRoleUserInSideDTO();
      dtoRole.setCountry(srInsiteDTO.getCountry());
      dtoRole.setRoleCode(srInsiteDTO.getRoleCode());
      dtoRole.setUsername(userToken.getUserName());
      dtoRole.setUnitId(srInsiteDTO.getSrUnit());
      List<SRRoleUserInSideDTO> lstRoleUserWithGroup = srCategoryServiceProxy
          .getlistSRRoleUserDTO(dtoRole);

      SRRoleActionDTO dtoActionUpdate = new SRRoleActionDTO();
      dtoActionUpdate.setCountry(srInsiteDTO.getCountry());
      dtoActionUpdate.setFlowId(srCatalogDTO.getFlowExecute());
      dtoActionUpdate.setCurrentStatus(oldStatus);
      dtoActionUpdate.setNextStatus(newStatus);//comboBox Status
      dtoActionUpdate.setRoleType("REQUIRED");

      if (lstRoleUserWithGroup != null && !lstRoleUserWithGroup.isEmpty()) {
        if ((!StringUtils.isStringNullOrEmpty(srInsiteDTO.getSrUser()) && userToken.getUserName()
            .equalsIgnoreCase(srInsiteDTO.getSrUser()))
            || StringUtils.isStringNullOrEmpty(srInsiteDTO.getSrUser())) {
          for (SRRoleUserInSideDTO role : lstRoleUserWithGroup) {
            dtoActionUpdate.setGroupRole(Constants.SR_ROLE.IMPLEMENTOR);
            List<SRRoleActionDTO> lstDataUpdateSR = srCategoryServiceProxy
                .getListSRRoleActionDTO(dtoActionUpdate);
            if (lstDataUpdateSR != null && !lstDataUpdateSR.isEmpty()) {
              lstRoleRequireSR.addAll(lstDataUpdateSR);
            } else {
              if (role.getIsLeader() != null && role.getIsLeader() == 1L) {
                dtoActionUpdate.setGroupRole(Constants.SR_ROLE.MANAGER);
                lstDataUpdateSR = srCategoryServiceProxy.getListSRRoleActionDTO(dtoActionUpdate);
                if (lstDataUpdateSR != null && !lstDataUpdateSR.isEmpty()) {
                  lstRoleRequireSR.addAll(lstDataUpdateSR);
                }
              }
            }
          }
        }
      }
      if (userToken.getUserName().equalsIgnoreCase(srInsiteDTO.getCreatedUser())) {
        dtoActionUpdate.setGroupRole(Constants.SR_ROLE.ORIGINATOR);
        List<SRRoleActionDTO> lstDataUpdateSR = srCategoryServiceProxy
            .getListSRRoleActionDTO(dtoActionUpdate);
        if (lstDataUpdateSR != null && !lstDataUpdateSR.isEmpty()) {
          lstRoleRequireSR.addAll(lstDataUpdateSR);
        }
      }
      if (!lstRoleRequireSR.isEmpty()) {
        for (SRRoleActionDTO roleRequired : lstRoleRequireSR) {
          String nextStatus = roleRequired.getNextStatus();
          if (!StringUtils.isStringNullOrEmpty(nextStatus)) {
            if (("," + nextStatus + ",").contains("," + srInsiteDTO.getStatus() + ",")) {
              String actionRequired = roleRequired.getActions();
              if (!StringUtils.isStringNullOrEmpty(actionRequired)) {
                if (("," + actionRequired + ",")
                    .contains("," + Constants.SR_ROLE_UPDATE.ADD_FILE + ",") && !srInsiteDTO
                    .isAddNewFile()) {
                  if (oldData.getGnocFileDtos() == null || oldData.getGnocFileDtos().size() == 0) {
                    res.setMessage(I18n.getLanguage("sr.required.file"));
                    res.setValidateKey("FILE");
                    return res;
                  }
                }
                if (("," + actionRequired + ",")
                    .contains("," + Constants.SR_ROLE_UPDATE.WORKLOG + ",") && !srInsiteDTO
                    .isAddNewWorklog()) {
                  if (oldData.getSrWorkLogDTOList() == null
                      || oldData.getSrWorkLogDTOList().size() == 0) {
                    res.setMessage(I18n.getLanguage("sr.required.worklog"));
                    res.setValidateKey("WORKLOG");
                    return res;
                  }
                  int checkInputWorklog = srWorkLogRepository
                      .checkInputWorklog(srInsiteDTO.getSrId());
                  if (oldData.getSrWorkLogDTOList().size() > 0 && checkInputWorklog == 0) {
                    res.setMessage(I18n.getLanguage("sr.required.worklog"));
                    res.setValidateKey("WORKLOG");
                    return res;
                  }
                }
                if ("1".equals(srCatalogDTO.getCr()) && ("," + actionRequired + ",")
                    .contains("," + Constants.SR_ROLE_UPDATE.CR + ",") && !srInsiteDTO
                    .isAddNewCr()) {
                  if (lstCr == null || lstCr.size() == 0) {
                    res.setMessage(I18n.getLanguage("sr.required.cr"));
                    res.setValidateKey("CR");
                    return res;
                  }
                  //14/10/2020 dungpv fix bat buoc nhap CR khi Sr bi chuyen lai trạng thái Assinged Planning
                  if (lstCr != null && !lstCr.isEmpty()) {
                    List<SRDTO> lstCrInfo = srRepository
                        .getCrInfoCreatedFromSR(srInsiteDTO.getSrId());
                    if (lstCrInfo != null && !lstCrInfo.isEmpty()) {
                      int count = 0;
                      for (CrInsiteDTO crInsiteDTO : lstCr) {
                        CrDTO dtoCr = crInsiteDTO.toCrDTO();
                        if (lstCrInfo.stream().filter(
                            s -> "2".equals(s.getStepId()) && dtoCr.getCrNumber()
                                .equals(s.getCrNumber())).findAny().isPresent()) {
                          count++;
                        }
                      }
                      if (lstCr.size() == count) {
                        res.setMessage(I18n.getLanguage("sr.required.cr"));
                        res.setValidateKey("CR");
                        return res;
                      }
                    }
                  }
                  // end
                }
                if ("1".equals(srCatalogDTO.getWo()) && ("," + actionRequired + ",")
                    .contains("," + Constants.SR_ROLE_UPDATE.WO + ",") && !srInsiteDTO
                    .isAddNewWo()) {
                  if (lstWo == null || lstWo.size() == 0) {
                    res.setMessage(I18n.getLanguage("sr.required.wo"));
                    res.setValidateKey("WO");
                    return res;
                  }
                }
                if (("," + actionRequired + ",").contains("," + Constants.SR_ROLE_UPDATE.OD + ",")
                    && !srInsiteDTO.isAddNewOd()) {
                  if (lstOd == null || lstOd.size() == 0) {
                    res.setMessage(I18n.getLanguage("sr.required.od"));
                    res.setValidateKey("OD");
                    return res;
                  }
                }
              }
            }
          }
        }
      }
      //</editor-fold>

      //<editor-fold desc="namtn Check CR sap lich : 5->9, WO tiep nhan : 4->6 khi SR chuyen sang trang thai bat ky sang Ready_Execution">
      if (Constants.SR_STATUS.READY_EXECUTION.equals(newStatus)) {
        if (lstCr != null && !lstCr.isEmpty()) {
          for (CrInsiteDTO dtoCr : lstCr) {
            if ("1".equals(dtoCr.getState())
                || "2".equals(dtoCr.getState())
                || "3".equals(dtoCr.getState())
                || "4".equals(dtoCr.getState())
                || "8".equals(dtoCr.getState())) {
              res.setMessage(I18n.getLanguage("SR.cr.state.required"));
              return res;
            }
          }
        }

        if (lstWo != null && !lstWo.isEmpty()) {
          for (WoDTOSearch dtoWo : lstWo) {
            if ("1".equals(dtoWo.getStatus())
                || "2".equals(dtoWo.getStatus())
                || "3".equals(dtoWo.getStatus())
                || "7".equals(dtoWo.getStatus())
                || "0".equals(dtoWo.getStatus())
                || "9".equals(dtoWo.getStatus())) {
              res.setMessage(I18n.getLanguage("SR.wo.status.required"));
              return res;
            }
          }
        }
      }
      //</editor-fold>

      //<editor-fold desc="namtn check CR dong : 9-CLOSE, WO dong : 8-CLOSED_CD , OD dong: 10-CLOSE khi SR chuyen trang thai tu Excuted sang Concluded">
      if (Constants.SR_STATUS.EXECUTED.equals(oldStatus) && Constants.SR_STATUS.CONCLUDED
          .equals(newStatus)) {
        if (lstCr != null && !lstCr.isEmpty()) {
          for (CrInsiteDTO dtoCr : lstCr) {
            if (!"9".equals(dtoCr.getState())) {
              res.setMessage(I18n.getLanguage("SR.cr.notClose"));
              return res;
            }
          }
        }

        if (lstWo != null && !lstWo.isEmpty()) {
          for (WoDTOSearch dtoWo : lstWo) {
            if (!"8".equals(dtoWo.getStatus())) {
              res.setMessage(I18n.getLanguage("SR.wo.notClose"));
              return res;
            }
          }
        }
        if (lstOd != null && !lstOd.isEmpty()) {
          for (OdSearchInsideDTO item : lstOd) {
            if (!"7".equals(item.getStatus())) {
              res.setMessage(I18n.getLanguage("SR.od.notClose"));
              return res;
            }
          }
        }
      }
      //</editor-fold>

      //<editor-fold desc="namtn check trang thai tu New -> trang thai khac bat buoc chon nguoi xu ly">
      if (StringUtils.isNotNullOrEmpty(oldStatus)) {
        if (!oldStatus.equals(newStatus)) {
          if (Constants.SR_STATUS.NEW.equals(oldStatus)) {
            if (!Constants.SR_STATUS.CANCELLED.equals(newStatus)) {
              if (StringUtils.isStringNullOrEmpty(srInsiteDTO.getSrUser())) {
                res.setMessage(
                    I18n.getLanguage("SR.srUser") + " " + I18n.getLanguage("SR.notnull"));
                return res;
              }
            }
          } else if (!Constants.SR_STATUS.DRAFT.equals(oldStatus)
              && !Constants.SR_STATUS.CANCELLED.equals(oldStatus)
              && !Constants.SR_STATUS.REJECTED.equals(oldStatus)) {
            if (StringUtils.isStringNullOrEmpty(srInsiteDTO.getSrUser())) {
              res.setMessage(I18n.getLanguage("SR.srUser") + " " + I18n.getLanguage("SR.notnull"));
              return res;
            }
          }
        } else if (!Constants.SR_STATUS.DRAFT.equals(oldStatus)
            && !Constants.SR_STATUS.CANCELLED.equals(oldStatus)
            && !Constants.SR_STATUS.REJECTED.equals(oldStatus)) {
          if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getApprove())
              && srCatalogDTO.getApprove() != 0L) {
            if (!Constants.SR_STATUS.UNDER_APPROVAL.equals(oldStatus)) {
              if (StringUtils.isStringNullOrEmpty(srInsiteDTO.getSrUser())) {
                res.setMessage(
                    I18n.getLanguage("SR.srUser") + " " + I18n.getLanguage("SR.notnull"));
                return res;
              }
            }
          } else {
            if (StringUtils.isStringNullOrEmpty(srInsiteDTO.getSrUser())) {
              res.setMessage(I18n.getLanguage("SR.srUser") + " " + I18n.getLanguage("SR.notnull"));
              return res;
            }
          }
        }
      } else {
        if (!StringUtils.isStringNullOrEmpty(srCatalogDTO.getApprove()) && !"0"
            .equals(String.valueOf(srCatalogDTO.getApprove()))) {
          if (!Constants.SR_STATUS.DRAFT.equals(oldStatus)
              && !Constants.SR_STATUS.CANCELLED.equals(oldStatus)
              && !Constants.SR_STATUS.REJECTED.equals(oldStatus)
              && !Constants.SR_STATUS.UNDER_APPROVAL.equals(oldStatus)) {
            if (StringUtils.isStringNullOrEmpty(srInsiteDTO.getSrUser())) {
              res.setMessage(
                  I18n.getLanguage("SR.srUser") + " " + I18n.getLanguage("SR.notnull"));
              return res;
            }
          }
        } else {
          if (!Constants.SR_STATUS.DRAFT.equals(oldStatus)
              && !Constants.SR_STATUS.CANCELLED.equals(oldStatus)
              && !Constants.SR_STATUS.REJECTED.equals(oldStatus)
              && !Constants.SR_STATUS.NEW.equals(oldStatus)) {
            if (StringUtils.isStringNullOrEmpty(srInsiteDTO.getSrUser())) {
              res.setMessage(
                  I18n.getLanguage("SR.srUser") + " " + I18n.getLanguage("SR.notnull"));
              return res;
            }
          }
        }
      }
      //</editor-fold>

      //<editor-fold desc="duongnt check khong cho phep SR Cha chuyen trang thai Concluded khi SR Con chua Concluded">
      if (!oldStatus.equals(newStatus)) {
        if (Constants.SR_STATUS.CONCLUDED.equals(newStatus)) {
          SrInsiteDTO srChild = new SrInsiteDTO();
          srChild.setParentCode(srInsiteDTO.getSrCode());
//          srChild.setPage(1);
//          srChild.setPageSize(Integer.MAX_VALUE);
          List<SREntity> lstSRChild = srRepository.getListSRChild(srChild);
//          if (datatable != null) {
//            List<SrInsiteDTO> lstSRChild = (List<SrInsiteDTO>) datatable.getData();
          if (lstSRChild != null && !lstSRChild.isEmpty()) {
            for (SREntity item : lstSRChild) {
              if (!Constants.SR_STATUS.CONCLUDED.equals(item.getStatus())
                  && !Constants.SR_STATUS.CLOSED.equals(item.getStatus())) {
                res.setMessage(I18n.getLanguage("SR.srChild.status.error"));
                return res;
              }
            }
          }
//          }
        }
      }
      //</editor-fold>

      //<editor-fold desc="namtn fix loi nhieu acc cung xu ly mot SR - Dec 20 2018" defaultstate="collapsed">
      SrInsiteDTO dataCheck = srInsiteDTO.getDataCheck();
      SRApproveDTO dataApproveDTO = dataCheck.getApproveDTO();
      if (oldData != null && dataCheck != null) {
        SRApproveDTO approveDTO = oldData.getApproveDTO();
        if (approveDTO == null) {
          approveDTO = new SRApproveDTO();
        }
        if (!oldData.getServiceId().equals(dataCheck.getServiceId())
            || !oldData.getStatus().equals(dataCheck.getStatus())
            || !oldData.getSrUnit().equals(dataCheck.getSrUnit())
            || (!StringUtils.isStringNullOrEmpty(oldData.getSrUser()) && !oldData.getSrUser()
            .equals(dataCheck.getSrUser()))
            || (!StringUtils.isStringNullOrEmpty(approveDTO.getApproveLevel1()) && !approveDTO
            .getApproveLevel1().equals(dataApproveDTO.getApproveLevel1()))
            || (!StringUtils.isStringNullOrEmpty(approveDTO.getApproveLevel2()) && !approveDTO
            .getApproveLevel2().equals(dataApproveDTO.getApproveLevel2()))) {
          res.setMessage(I18n.getLanguage("sr.update.error"));
          return res;
        }
      }
      //</editor-fold>

      //<editor-fold desc="duongnt check change status Assign Planning sang Planned ">
      if (Constants.SR_STATUS.ASSIGNED_PLANNING.equals(oldStatus) && Constants.SR_STATUS.PLANNED
          .equals(newStatus)) {
        if (lstCr != null && !lstCr.isEmpty()) {
          for (CrInsiteDTO item : lstCr) {
            if ("0".equals(item.getState())) {
              res.setMessage(I18n.getLanguage("SR.cr.crDraft").replace("{0}", item.getCrNumber()));
              return res;
            }
          }
        } else {
          if ("1".equals(srCatalogDTO.getCr())) {
            res.setMessage(I18n.getLanguage("SR.cr.crNotCreate"));
            return res;
          }
        }
      }
      //</editor-fold>

      //<editor-fold desc="namtn validate duyet cap 1 cap 2 khi SR quay ve trang thai draft, rejected hoac cancelled" defaultstate="collapsed">
      if (!StringUtils.isLongNullOrEmpty(srCatalogDTO.getApprove())
          && srCatalogDTO.getApprove() != 0) {
        if ((Constants.SR_STATUS.REJECTED.equals(oldStatus)
            || Constants.SR_STATUS.CANCELLED.equals(oldStatus)
            || Constants.SR_STATUS.DRAFT.equals(oldStatus))
            && !Constants.SR_STATUS.UNDER_APPROVAL.equals(newStatus)) {
          res.setMessage(I18n.getLanguage("SR.not.approve"));
          return res;
        }
      }
      //</editor-fold>

      //<editor-fold desc="namtn: goi ws validate file tu NIMS doi vs dich vu NIMS khi chuyen trang thai Cho tiep nhan => Tiep nhan" defaultstate="collapsed">
      if (srInsiteDTO.isServiceNims()) {
        if (srInsiteDTO.getGnocFileDtos().isEmpty()) {
          res.setMessage(I18n.getLanguage("sr.required.file"));
          res.setValidateKey("FILE");
          return res;
        }
        List<SRConfigDTO> lstWaitingNims = srConfigRepository
            .getByConfigGroup(Constants.SR_CONFIG.STATUS_WAITING_NIMS);
        List<SRConfigDTO> lstReceiveNims = srConfigRepository
            .getByConfigGroup(Constants.SR_CONFIG.STATUS_RECEIVE_NIMS);
        //neu trang thai hien tai la cho tiep nhan va trang thai tiep theo la tiep nhan
        if (isLstConfigContainsValue(lstWaitingNims, oldStatus)
            && isLstConfigContainsValue(lstReceiveNims, newStatus)) {
          try {
            List<SRConfigDTO> lstLink = srConfigRepository
                .getByConfigGroup(Constants.SR_CONFIG.WS_NIMS_URL);
            if (lstLink == null || lstLink.isEmpty()) {
              res.setMessage(I18n.getLanguage("sr.nims.ws.url.notConfig"));
              return res;
            }
            List<SRConfigDTO> lstUsername = srConfigRepository
                .getByConfigGroup(Constants.SR_CONFIG.WS_NIMS_USERNAME);
            String usernameNims = "gnoc_nims";
            if (lstUsername != null && !lstUsername.isEmpty()) {
              usernameNims = lstUsername.get(0).getConfigCode();
            }
            String pNims = "gnoc_nims";
            List<SRConfigDTO> lstPassword = srConfigRepository
                .getByConfigGroup(Constants.SR_CONFIG.WS_NIMS_P);
            if (lstPassword != null && !lstPassword.isEmpty()) {
              pNims = lstPassword.get(0).getConfigCode();
            }
            com.viettel.nims.infra.webservice.sr.DistributeIpResourceInput input = new com.viettel.nims.infra.webservice.sr.DistributeIpResourceInput();
            if (srInsiteDTO.getSrUser() != null) {
              input.setUserApprove(srInsiteDTO.getSrUser());
            } else {
              res.setMessage(I18n.getLanguage("SR.srUser") + " " + I18n.getLanguage("SR.notnull"));
              return res;
            }
            input.setSrCode(srInsiteDTO.getSrCode());
            input.setUserRequest(srInsiteDTO.getCreatedUser());

            GnocFileDto fileSr = new GnocFileDto();
            fileSr.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
            fileSr.setBusinessId(srInsiteDTO.getSrId());
            List<GnocFileDto> lstFileAttach = gnocFileRepository.getListGnocFileForSR(fileSr);
            if (lstFileAttach == null || lstFileAttach.isEmpty()) {
              res.setMessage(I18n.getLanguage("sr.nims.file.attachment.notfound"));
              return res;
            }
            String pathFileImport = "";
            for (GnocFileDto item : lstFileAttach) {
              if (item.getFileType().equals(Constants.SR_ROLE_UPDATE.NIMS)) {
                pathFileImport = item.getPath();
              }
            }
            byte[] bytes = FileUtils
                .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), pathFileImport);
            pathFileImport = FileUtils
                .saveTempFile(FileUtils.getFileName(pathFileImport), bytes,
                    tempFolder);
            try {
              File fileImport = new File(pathFileImport);
              List<Object[]> lstData = ExcelWriterUtils.readExcelAddBlankRowXSSF(
                  fileImport,
                  0,//sheet
                  1,//begin row
                  0,//from column
                  4,//to column
                  1000);

              int row = 1;
              for (Object[] obj : lstData) {
                if (obj != null) {
                  com.viettel.nims.infra.webservice.sr.DistributeIpResourceForm form = new com.viettel.nims.infra.webservice.sr.DistributeIpResourceForm();
                  form.setRow(row);
                  form.setLocation(obj[1].toString());
                  form.setRange(obj[2].toString());
                  form.setSystem(obj[3].toString());
                  form.setPurpose(obj[4].toString());
                  input.getListDataRequest().add(form);
                  row++;
                }
              }
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
            URL url = new URL(lstLink.get(0).getConfigCode());
            log.info("===== NIMS =====" + lstLink.get(0).getConfigCode());
            log.info("===== url NIMS =====" + url);
            int nTry = 0;
            while (nTry < 3) {
              try {
                log.info("===== Start port InfraWS NIMS =====");
                com.viettel.nims.infra.webservice.sr.InfraWS_Service service = new com.viettel.nims.infra.webservice.sr.InfraWS_Service(
                    url);
                log.info("===== Init InfraWS_Service NIMS =====");
                com.viettel.nims.infra.webservice.sr.InfraWS port = service.getInfraWSPort();
                log.info("===== Init port InfraWS NIMS =====");
                if (input != null) {
                  StringBuilder logNims = new StringBuilder();
                  logNims.append("\n<web:distributeIpResource>\n" +
                      "\n <username>" + usernameNims + "</username>\n" +
                      "<password>" + pNims + "</password> \n" +
                      "<input>" +
                      "<srCode>" + input.getSrCode() + "</srCode>\n" +
                      "<userApprove>" + input.getUserApprove() + "</userApprove>\n" +
                      " <userRequest>" + input.getUserRequest() + "</userRequest>\n"
                  );
                  if (input.getListDataRequest() != null && !input.getListDataRequest().isEmpty()) {
                    for (DistributeIpResourceForm inputForm : input.getListDataRequest()) {
                      logNims.append("\n<listDataRequest>\n"
                          + "               <location>" + inputForm.getLocation() + "</location>\n"
                          + "               <purpose>" + inputForm.getPurpose() + "</purpose>\n"
                          + "               <range>" + inputForm.getRange() + "</range>\n"
                          + "               <row>" + inputForm.getRow() + "</row>\n"
                          + "               <system>" + inputForm.getSystem() + "</system>\n"
                          + "            </listDataRequest>\n");
                    }
                  }
                  logNims.append(" </input>\n " + "</web:distributeIpResource>\n");
                  log.info(
                      "\n ===== input distributeIpResource NIMS ===== \n" + logNims.toString());
                }
                log.info("Bat dau goi ham distributeIpResource NIMS: ");
                outputNims = port.distributeIpResource(usernameNims, pNims, input);
                log.info("Kết thúc goi ham distributeIpResource NIMS: ");
                log.info("===== END port InfraWS NIMS ===== : ");
                break;
              } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.info("===== Exception khi goi WS NIMS =====");
                nTry++;
                try {
                  Thread.sleep(1000);
                } catch (Exception ex) {
                  log.error(e.getMessage(), e);
                }
              }
            }
            if (outputNims == null || nTry > 3) {
              res.setMessage(I18n.getLanguage("sr.nims.file.send.attachment.fail"));
              return res;
            } else {
              if (Constants.WS_RESULT.NOK.equals(outputNims.getResult())) {
                res.setMessage(I18n.getLanguage(outputNims.getMessage()));
                List<com.viettel.nims.infra.webservice.sr.DistributeIpResourceForm> lstForm = outputNims
                    .getListDataResponse();
                String filePath = exportFileResultNims(lstForm, "RETURN_FILE", srInsiteDTO);
                if (!StringUtils.isStringNullOrEmpty(filePath)) {
                  res.setFilePath(filePath);
                }
                outputNims = null;
                SRConfigDTO configDTO = new SRConfigDTO();
                configDTO.setConfigGroup(Constants.SR_CONFIG.CONFIG_GROUP_SMS_SRF);
                configDTO.setConfigCode(Constants.SR_CONFIG.CONFIG_CODE_SMS_NIMS);
                List<SRRoleUserInSideDTO> lstSrUser = new ArrayList<>();
                lstSrUser.add(new SRRoleUserInSideDTO(srInsiteDTO.getSrUser()));

                sendMessages(srInsiteDTO, null, lstSrUser, configDTO);
                sendMessages(srInsiteDTO, srInsiteDTO.getCreatedUser(), null, configDTO);
                List<GnocFileDto> lst = new ArrayList<>();
                GnocFileDto gnocFileDto = new GnocFileDto();
                gnocFileDto.setPath(filePath);
                lst.add(gnocFileDto);
                res.setLstResult(lst);
                return res;
              }
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
        }
      }
      //</editor-fold>

      //<editor-fold desc="namtn edit yc thang 08: ng dung chuyen trang thai SR bat buoc theo CR" defaultstate="collapsed">
      if (!oldStatus.equals(newStatus)) {
        if (lstCr != null && !lstCr.isEmpty()) {
          String statusExcutionHaled = Constants.SR_STATUS.EXECUTION_HALTED;
          String statusConcluded = Constants.SR_STATUS.CONCLUDED;
          String statusPlanned = Constants.SR_STATUS.PLANNED;
          List<SRConfigDTO> lstPlanned = srConfigRepository
              .getByConfigGroup(Constants.SR_CONFIG.STATUS_AUTO_PLANNED);
          List<SRConfigDTO> lstConcluded = srConfigRepository
              .getByConfigGroup(Constants.SR_CONFIG.STATUS_AUTO_CONCLUDED);
          List<SRConfigDTO> lstExcution = srConfigRepository
              .getByConfigGroup(Constants.SR_CONFIG.STATUS_AUTO_EXECUTION_HALTED);
          if (lstPlanned != null && !lstPlanned.isEmpty()) {
            statusPlanned = lstPlanned.get(0).getConfigCode();
          }
          if (lstConcluded != null && !lstConcluded.isEmpty()) {
            statusConcluded = lstConcluded.get(0).getConfigCode();
          }
          if (lstExcution != null && !lstExcution.isEmpty()) {
            statusExcutionHaled = lstExcution.get(0).getConfigCode();
          }
          List<SRDTO> lstCrInfo = srRepository.getCrInfoCreatedFromSR(srInsiteDTO.getSrId());
          if (lstCrInfo == null) {
            lstCrInfo = new ArrayList<>();
          }
          if (lstCr != null && !lstCr.isEmpty()) {
            for (int i = lstCr.size() - 1; i > -1; i--) {
              CrDTO dtoCr = lstCr.get(i).toCrDTO();
              if (lstCrInfo.stream()
                  .filter(
                      s -> "2".equals(s.getStepId()) && dtoCr.getCrNumber().equals(s.getCrNumber()))
                  .findAny()
                  .isPresent()) {
                //search danh sach CR co ban ghi stepId = 2 => remove khoi danh sach check
                lstCr.remove(i);
                continue;
              }
              Optional<SRDTO> optCr = lstCrInfo.stream()
                  .filter(s -> s.getCrNumber().equals(dtoCr.getCrNumber())).findAny();
              if (optCr.isPresent()) {
                SRDTO crHis = optCr.get();
                lstCr.get(i)
                    .setActionReturnCodeId(crHis.getActionCode() + "-" + crHis.getReturnCode());
              }
            }
            if (lstCr != null && !lstCr.isEmpty()) {
              if (statusExcutionHaled.equalsIgnoreCase(oldStatus) || statusConcluded
                  .equalsIgnoreCase(oldStatus)) {
                List<CrInsiteDTO> lstCrCheckConcluded = new ArrayList<>();
                List<CrInsiteDTO> lstCrExecutionHalted = new ArrayList<>();
                lstCrCheckConcluded.addAll(lstCr);
                lstCrExecutionHalted.addAll(lstCr);
                if (lstCr.stream()
                    .filter(c -> c.getState().contains("7") || c.getState().contains("9"))
                    .findAny().isPresent()) {
                  boolean isConcluded = false;
                  boolean isExecutionHalted = false;
                  //kiểm tra đầu vào Sr Planned chuyển sang Concluded
                  for (int i = lstCrCheckConcluded.size() - 1; i > -1; i--) {
                    CrDTO dataCr = lstCrCheckConcluded.get(i).toCrDTO();
                    if ("9".equals(dataCr.getState()) && dataCr.getActionReturnCodeId()
                        .contains("25-42")) {
                      isConcluded = true;
                      lstCrCheckConcluded.remove(i);
                      continue;
                    }
                    if ("7".equals(dataCr.getState()) && dataCr.getActionReturnCodeId()
                        .contains("24-39")) {
                      isConcluded = true;
                      lstCrCheckConcluded.remove(i);
                      continue;
                    }
                  }
                  //kiểm tra đầu vào Sr Planned chuyển sang ExecutionHalted
                  for (int i = lstCrExecutionHalted.size() - 1; i > -1; i--) {
                    CrDTO dataCr1 = lstCrExecutionHalted.get(i).toCrDTO();
                    if ("9".equals(dataCr1.getState()) && dataCr1.getActionReturnCodeId()
                        .contains("25")
                        && !dataCr1.getActionReturnCodeId().contains("25-42")) {
                      lstCrExecutionHalted.remove(i);
                      isExecutionHalted = true;
                      continue;
                    }
                  }
                  if (isExecutionHalted) {
                    //(9 - Khác đóng hoàn thành)
                    if (lstCrExecutionHalted.isEmpty()) {
                      if (!Constants.SR_STATUS.EXECUTION_HALTED.equals(newStatus)) {
                        res.setMessage(I18n.getLanguage("sr.changeStatus.executionHalted"));
                        return res;
                      }
                      srInsiteDTO.setExecutionWithCr(true);
                    } else {
                      if (Constants.SR_STATUS.PLANNED.equals(newStatus)) {
                        res.setMessage(I18n.getLanguage("sr.changeStatus.notPlanned"));
                        return res;
                      }
                      if (Constants.SR_STATUS.EXECUTION_HALTED.equals(newStatus)) {
                        res.setMessage(I18n.getLanguage("sr.changeStatus.notExecutionHalted"));
                        return res;
                      }
                      if (Constants.SR_STATUS.CONCLUDED.equals(newStatus)) {
                        res.setMessage(I18n.getLanguage("sr.changeStatus.notConcluded"));
                        return res;
                      }
                    }
                  } else {
                    if (isConcluded) {
                      if (lstCrCheckConcluded.isEmpty()) {
                        if (!Constants.SR_STATUS.CONCLUDED.equals(newStatus)) {
                          res.setMessage(I18n.getLanguage("sr.changeStatus.concluded"));
                          return res;
                        }
                        srInsiteDTO.setConcludedWithCr(true);
                      } else {
                        if (Constants.SR_STATUS.PLANNED.equals(newStatus)) {
                          res.setMessage(I18n.getLanguage("sr.changeStatus.notPlanned"));
                          return res;
                        }
                        if (Constants.SR_STATUS.EXECUTION_HALTED.equals(newStatus)) {
                          res.setMessage(I18n.getLanguage("sr.changeStatus.notExecutionHalted"));
                          return res;
                        }
                        if (Constants.SR_STATUS.CONCLUDED.equals(newStatus)) {
                          res.setMessage(I18n.getLanguage("sr.changeStatus.notConcluded"));
                          return res;
                        }
                      }
                    } else {
                      if (Constants.SR_STATUS.PLANNED.equals(newStatus)) {
                        res.setMessage(I18n.getLanguage("sr.changeStatus.notPlanned"));
                        return res;
                      }
                      if (Constants.SR_STATUS.EXECUTION_HALTED.equals(newStatus)) {
                        res.setMessage(I18n.getLanguage("sr.changeStatus.notExecutionHalted"));
                        return res;
                      }
                      if (Constants.SR_STATUS.CONCLUDED.equals(newStatus)) {
                        res.setMessage(I18n.getLanguage("sr.changeStatus.notConcluded"));
                        return res;
                      }
                    }
                  }
                }
                if ((lstCrCheckConcluded != null && !lstCrCheckConcluded.isEmpty()) && (
                    lstCrExecutionHalted != null && !lstCrExecutionHalted.isEmpty())
                    && !Constants.SR_STATUS.PLANNED
                    .equals(newStatus)) {
                  if (!Constants.SR_STATUS.CONCLUDED.equals(newStatus)
                      && !Constants.SR_STATUS.EXECUTION_HALTED.equals(newStatus)) {
                    res.setMessage(I18n.getLanguage("sr.changeStatus.concluded.executionHalted"));
                    return res;
                  } else {
                    res.setMessage(
                        I18n.getLanguage("sr.changeStatus.concluded.executionHalted.notIn"));
                    return res;
                  }
                }
              }
              if (statusPlanned.equalsIgnoreCase(oldStatus)) {
                if (lstCr.stream()
                    .filter(c -> c.getState().contains("5") || c.getState().contains("6")
                        || c.getState().contains("7") || c.getState().contains("9"))
                    .findAny()
                    .isPresent()) {
                  boolean isPlanned = true;
                  for (CrInsiteDTO dataCr : lstCr) {
                    if (Long.valueOf(dataCr.getState()) != 5L
                        && Long.valueOf(dataCr.getState()) != 6L
                        && Long.valueOf(dataCr.getState()) != 7L
                        && Long.valueOf(dataCr.getState()) != 9L) {
                      isPlanned = false;
                      break;
                    }
                  }
                  if (isPlanned) {
                    if (!Constants.SR_STATUS.PLANNED.equals(newStatus)) {
                      res.setMessage(I18n.getLanguage("sr.changeStatus.planned"));
                      return res;
                    }
                  } else {
                    if (Constants.SR_STATUS.PLANNED.equals(newStatus)) {
                      res.setMessage(I18n.getLanguage("sr.changeStatus.notPlanned"));
                      return res;
                    }
                    if (Constants.SR_STATUS.EXECUTION_HALTED.equals(newStatus)) {
                      res.setMessage(I18n.getLanguage("sr.changeStatus.notExecutionHalted"));
                      return res;
                    }
                    if (Constants.SR_STATUS.CONCLUDED.equals(newStatus)) {
                      res.setMessage(I18n.getLanguage("sr.changeStatus.notConcluded"));
                      return res;
                    }
                  }
                } else {
                  if (Constants.SR_STATUS.PLANNED.equals(newStatus)) {
                    res.setMessage(I18n.getLanguage("sr.changeStatus.notPlanned"));
                    return res;
                  }
                  if (Constants.SR_STATUS.EXECUTION_HALTED.equals(newStatus)) {
                    res.setMessage(I18n.getLanguage("sr.changeStatus.notExecutionHalted"));
                    return res;
                  }
                  if (Constants.SR_STATUS.CONCLUDED.equals(newStatus)) {
                    res.setMessage(I18n.getLanguage("sr.changeStatus.notConcluded"));
                    return res;
                  }
                }
              }
            }
          }
        }
      }
      //</editor-fold>

      //<editor-fold defaultstate="collapsed" desc="neu SR qua han thuc thi => khong cho phep chuyen trang thai">
      if (!StringUtils.isStringNullOrEmpty(srInsiteDTO.getRemainExecutionTimeCheckStatus())) {
        if (!Constants.SR_STATUS.DRAFT.equals(oldStatus)) {
          if (Double.valueOf(srInsiteDTO.getRemainExecutionTimeCheckStatus()) <= 0.0) {
            List<SRConfigDTO> lst = srConfigRepository
                .getByConfigGroup(Constants.SR_CONFIG.STATUS_NOT_EXECUTE);
            if (lst == null || lst.isEmpty()) {
              lst = new ArrayList<>();
              SRConfigDTO dtoTmp = new SRConfigDTO();
              dtoTmp.setConfigCode(Constants.SR_STATUS.CANCELLED);
              lst.add(dtoTmp);
            }
            if (isLstConfigContainsValue(lst, newStatus)) {
              res.setMessage(I18n.getValidation("sr.cannot.execute").replace("{0}", newStatus));
              return res;
            }
          }
        }
      }
      //</editor-fold>

      //<editor-fold defaultstate="collapsed" desc="dungpv SR MO KET NOI o trang thai DRAFT khong duoc phep cap nhat">
      if (srInsiteDTO.isOpenConnect()) {
        if (Constants.SR_STATUS.DRAFT.equals(oldStatus) && !oldStatus.equals(newStatus)) {
          res.setMessage(I18n.getLanguage("sr.changeStatus.draft"));
          return res;
        }
      }
      //</editor-fold>
    }

    res = validateFile(srInsiteDTO, srCatalogDTO);
    if (res.getKey().equals(RESULT.FAIL)) {
      return res;
    }
    boolean checkBCCS = res.getCheck();
    res.setKey(RESULT.FAIL);

    //<editor-fold desc="dungpv 25/08/2020 edit sendToVipa khi nhan nut Save Sr,check file MKN va file ky trong table file">
    if (srInsiteDTO.isOpenConnect()) {
      GnocFileDto fileTemp = new GnocFileDto();
      fileTemp.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
      fileTemp.setBusinessId(srInsiteDTO.getSrId());
      List<GnocFileDto> lstFileTemp = gnocFileRepository.getListGnocFileForSR(fileTemp);
      boolean checkMKN = false;
      boolean checkFK = false;
      String filePathFtp = null;
      if (lstFileTemp != null && !lstFileTemp.isEmpty()) {
        for (GnocFileDto item : lstFileTemp) {
          if (Constants.SR_ROLE_UPDATE.FILE_TYPE_OPEN_CONNECT.equals(item.getFileType())) {
            checkMKN = true;
            filePathFtp = item.getPath();
          }
          if (Constants.SR_ROLE_UPDATE.FILE_TYPE_FILE_KY.equals(item.getFileType())) {
            checkFK = true;
          }
        }
      }
      //20201218 dungpv edit sr mkn
      if (checkMKN) {
        if ((StringUtils.isStringNullOrEmpty(oldStatus) && SR_STATUS.NEW.equals(newStatus)) ||
            (SR_STATUS.REJECTED.equals(oldStatus) && SR_STATUS.NEW
                .equals(srInsiteDTO.getStatus()))) {
          if (StringUtils.isNotNullOrEmpty(filePathFtp)) {
            try {
              byte[] bytesFileOpenConnect = FileUtils
                  .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                      PassTranformer.decrypt(ftpPass), filePathFtp);
              log.info("===============Mo ket noi=====================");
              log.info("Bat dau goi den VIPA");
              ResultInSideDto resultSendToVipa = sendToVipa(
                  String.valueOf(srInsiteDTO.getSrId()),
                  org.apache.xml.security.utils.Base64.encode(bytesFileOpenConnect));
              if (!RESULT.SUCCESS.equals(resultSendToVipa.getKey())) {
                if (resultSendToVipa.getCheck() != null && !resultSendToVipa.getCheck()) {
                  resultSendToVipa
                      .setMessage(I18n.getValidation("sr.add.vipa.fileopenconnect.fail"));
                }
                return resultSendToVipa;
              } else {
                if (resultSendToVipa.getCheck() != null && resultSendToVipa.getCheck()) {
                  srInsiteDTO.setStatus(SR_STATUS.NEW);
                } else {
                  srInsiteDTO.setStatus(SR_STATUS.DRAFT);
                  srInsiteDTO.setTimeSave(new Date());
                  srInsiteDTO.setNote(I18n.getLanguage("sr.add.vipa.fileopenconnect.note"));
                  srInsiteDTO.setSrUser(null);
                }
                if (SR_STATUS.REJECTED.equals(oldStatus) && SR_STATUS.DRAFT
                    .equals(srInsiteDTO.getStatus())) {
                  srRepository.updateSRProcessMess(srInsiteDTO.getSrCode());
                }
              }
            } catch (Exception e) {
              log.error(e.getMessage());
            }
          }
        }
        //end
      } else {
        res.setMessage(I18n.getValidation("sr.file.not.attach") + " " + I18n
            .getValidation("sr.tab.attach.fileopen"));
        return res;
      }
      if (!checkFK) {
        res.setMessage(I18n.getValidation("sr.file.not.attach") + " " + I18n
            .getValidation("sr.tab.attach.fileky"));
        return res;
      }
    }
    //</editor-fold>

    //<editor-fold desc="namtn edit on May 2018 : Dich vu Only SDH hoac Metro">
    if (Constants.SR_STATUS.NEW.equals(newStatus) || Constants.SR_STATUS.CONCLUDED
        .equals(newStatus)) {
      if (Constants.SR_STATUS.NEW.equals(newStatus) && srInsiteDTO.isDvTrungKe()) {
        //vaildate neu chua dinh kem file hoac da dinh kem file nhung ko co file FORM
        GnocFileDto fileTemp = new GnocFileDto();
        fileTemp.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
        fileTemp.setBusinessId(srInsiteDTO.getSrId());
        List<GnocFileDto> lstFileTemp = gnocFileRepository.getListGnocFileForSR(fileTemp);
        if (lstFileTemp.isEmpty()) {
          res.setMessage(I18n.getValidation("sr.file.not.attach") + " " + I18n
              .getValidation("sr.tab.attach.fileForm.dvTrungKe"));
          return res;
        } else {
          boolean isHaveFileForm = false;
          for (GnocFileDto item : lstFileTemp) {
            if (Constants.SR_ROLE_UPDATE.FILE_TYPE_FORM.equals(item.getFileType())) {
              isHaveFileForm = true;
            }
          }
          if (!isHaveFileForm) {
            res.setMessage(I18n.getValidation("sr.file.not.attach") + " " + I18n
                .getValidation("sr.tab.attach.fileForm.dvTrungKe"));
            return res;
          }
        }
      }
      List<CrInsiteDTO> lstCr = findListCrBySr(srInsiteDTO.getSrId());
      if (Constants.SR_STATUS.CONCLUDED.equals(newStatus) && srInsiteDTO.isDvTrungKe()) {
        if (lstCr != null) {
          for (CrInsiteDTO item : lstCr) {
            if (!I18n.getLanguage("cr.state.close").equals(item.getState())) {
              res.setMessage(
                  I18n.getValidation("sr.cr.crNotClose").replace("CR", item.getCrNumber()));
              return res;
            }
          }
        }
        WoDTOSearch dtoSearch = new WoDTOSearch();
        dtoSearch.setUserId(userToken.getUserID().toString());
        dtoSearch.setWoSystem("SR");
        dtoSearch.setWoSystemId(srInsiteDTO.getSrId().toString());
        dtoSearch.setPageSize(Integer.MAX_VALUE);
        dtoSearch.setPage(1);
        List<WoDTOSearch> lstWo = woServiceProxy.getListDataSearchProxy(dtoSearch);
        if (lstWo != null) {
          if (lstWo != null && lstWo.size() > 1) {
            for (WoDTOSearch item : lstWo) {
              if (!"8".equals(item.getStatus())) {
                res.setMessage(
                    I18n.getValidation("sr.wo.woNotClose").replace("WO", item.getWoCode()));
                return res;
              }
            }
          } else if (lstWo == null || lstWo.size() == 1 || lstWo.size() == 0) {
            res.setMessage(
                I18n.getValidation("sr.wo.woNotCreate"));
            return res;
          }
        }
      }
    }
    //</editor-fold>

    //<editor-fold desc="lynv:check tao cr tu dong khi chuyen trang thai tu New sang Evaluated" defaultstate="collapsed">
    if (srInsiteDTO.isAutoCreatCR()) {
      List<SRConfigDTO> lsConfig = srConfigRepository
          .getCrInforByParentGroup(Constants.SR_CATALOG.PARENT_GROUP_AUTO_CREATE_CR_MAPPING);
      String evalutedStatus = Constants.SR_STATUS.EVALUATED;
      if (lsConfig != null && !lsConfig.isEmpty()) {
        evalutedStatus = lsConfig.get(0).getConfigCode();
      }
      if (!StringUtils.isStringNullOrEmpty(oldData.getSrUser()) && userToken.getUserName()
          .equals(srInsiteDTO.getSrUser())) {
        if (Constants.SR_STATUS.NEW.equals(oldStatus) && newStatus.equals(evalutedStatus)) {
          if (!checkBCCS) {
            SRCreateAutoCRDTO srDto = new SRCreateAutoCRDTO();
            srDto.setSrId(srInsiteDTO.getSrId());
            List<SRCreateAutoCRDTO> lstSrCreateAutoBySrId = srCreateAutoCrRepository
                .searchSRCreateAutoCR(srDto, 0, 0, "", "");
            if (lstSrCreateAutoBySrId == null || lstSrCreateAutoBySrId.isEmpty()) {
              res.setMessage(I18n.getLanguage("sr.not.autoCreate"));
              return res;
            } else {
              if (lstSrCreateAutoBySrId.get(0).getExecutionTime() == null
                  || lstSrCreateAutoBySrId.get(0).getExecutionEndTime() == null) {
                res.setMessage(I18n.getLanguage("sr.not.autoCreate2"));
                return res;
              }
            }
            autoCreateCRResult = autoCreateCR(srInsiteDTO);
            if (!RESULT.SUCCESS.equals(autoCreateCRResult.getKey())) {
              return autoCreateCRResult;
            }
          }
        }
      } else {
        if (Constants.SR_STATUS.NEW.equals(oldStatus) && newStatus.equals(evalutedStatus)) {
          res.setMessage(I18n.getLanguage("sr.srCreat.auto.notUser"));
          return res;
        }
      }
    }
    //</editor-fold>

    //    //<editor-fold defaultstate="collapsed" desc="validate phai dinh kem file dich vu">
    List<SRConfigDTO> lstStatusConfig = srConfigRepository
        .getByConfigGroup(Constants.SR_CONFIG.STATUS_ATTACH_FILE_SERVICE);
    if (lstStatusConfig != null && !lstStatusConfig.isEmpty()) {
      if (isLstConfigContainsValue(lstStatusConfig, srInsiteDTO.getStatus())) {
        List<UnitSRCatalogDTO> lstUnitInService = srCatalogRepository2
            .getListUnitSRCatalog(new SRCatalogDTO(srCatalogDTO.getServiceCode()));
        if (lstUnitInService != null && lstUnitInService.size() > 1) {
          Optional<UnitSRCatalogDTO> unit = lstUnitInService.stream()
              .filter(u -> u.getUnitId().equals(String.valueOf(srInsiteDTO.getSrUnit()))).findAny();
          if (unit.isPresent()) {
            srCatalogDTO = srCatalogRepository2.findById(Long.parseLong(unit.get().getServiceId()));
          }
        }
        GnocFileDto fileCatalog = new GnocFileDto();
        fileCatalog.setBusinessCode(GNOC_FILE_BUSSINESS.SR_CATALOG);
        fileCatalog.setBusinessId(srCatalogDTO.getServiceId());
        fileCatalog.setRequired(1L);
        List<GnocFileDto> lstFileBieuMau = gnocFileRepository
            .getListGnocFileForSR(fileCatalog);
        if (lstFileBieuMau != null && !lstFileBieuMau.isEmpty()) {
//          //neu co file bieu mau thi check tiep
          GnocFileDto fileTmp = new GnocFileDto();
          fileTmp.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
          fileTmp.setBusinessId(srInsiteDTO.getSrId());
          List<GnocFileDto> lstFileTmp = gnocFileRepository.getListGnocFileForSR(fileTmp);
          if (lstFileTmp.isEmpty()) {
            res.setKey(RESULT.FAIL);
            res.setMessage(I18n.getValidation("sr.required.file"));
            res.setValidateKey("FILE");
            return res;
          } else {
            if (lstFileTmp.size() < lstFileBieuMau.size()) {
//              //so luong file it hon thi return false luon
              res.setKey(RESULT.FAIL);
              res.setMessage(I18n.getValidation("sr.fileData.incomplete"));
              return res;
            } else {
//              //check da du so file bieu mau up len chua
              List<GnocFileDto> lstFileBieuMauTmp = new ArrayList<>(lstFileBieuMau);
              for (int i = lstFileBieuMauTmp.size() - 1; i > -1; i--) {
                GnocFileDto file = lstFileBieuMauTmp.get(i);
                Optional<GnocFileDto> optF = lstFileTmp.stream()
                    .filter(f -> f.getFileType().equalsIgnoreCase(file.getFileType())).findAny();
                if (!optF.isPresent()) {
                  res.setKey(RESULT.FAIL);
                  res.setMessage(I18n.getValidation("sr.fileData.fileNotEnough")
                      .replace("{0}", file.getFileType()));
                  return res;
                } else {
                  lstFileBieuMauTmp.remove(i);
                }
              }
            }
          }
        }
      }
    }
//    //</editor-fold>

    res.setKey(RESULT.SUCCESS);
    if (RESULT.SUCCESS.equals(autoCreateCRResult.getKey())) {
      res.setLstResult(autoCreateCRResult.getLstResult());
      res.setReturnCode(autoCreateCRResult.getReturnCode());
    }
    res.setIdValue(oldStatus);
    return res;
  }

  private ResultInSideDto validateFile(SrInsiteDTO srInsiteDTO, SRCatalogDTO srCatalogDTO) {
    boolean checkBCCS = false;
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.FAIL);
    if (srInsiteDTO.isAutoCreatCR()) {
      //26/10/2020 dungpv edit bo sung tao Cr Tool  BCCS_AUTO_CREATE_CR
      List<SRConfigDTO> lstCfgBCCS = srConfigRepository
          .getByConfigGroup(SR_CONFIG.BCCS_AUTO_CREATE_CR);
      List<CrInsiteDTO> lstCr = findListCrBySr(srInsiteDTO.getSrId());
      boolean bccsAutoCreateCR = false;
      if (lstCfgBCCS != null && !lstCfgBCCS.isEmpty() && lstCfgBCCS.stream()
          .filter(s -> srCatalogDTO.getServiceCode()
              .equals(s.getConfigCode())).findAny().isPresent()) {
        bccsAutoCreateCR = true;
      }
      //end
      SRCreateAutoCRDTO srcacrdto = new SRCreateAutoCRDTO();
      srcacrdto.setSrId(srInsiteDTO.getSrId());
      List<SRCreateAutoCRDTO> lstSrCreatAuto = srCreateAutoCrRepository
          .searchSRCreateAutoCR(srcacrdto, 0, 0, "", "");
      if (lstSrCreatAuto == null || lstSrCreatAuto.isEmpty()) {
        resultInSideDto.setMessage(I18n.getValidation("sr.not.autoCreate"));
        return resultInSideDto;
      }
      //26/10/2020 dungpv edit sr chuyen EVALUATED thi goi WS lay Mop
      if (bccsAutoCreateCR) {
        // WS BCCS tao SR_SR_TOOL kiem tra neu SR chuyen trang thai
        // EVALUATED va chua tao Cr, chua link CR thi goi WS BCCS lay MOP de chuyen trang thai
        if (SR_STATUS.EVALUATED.equals(srInsiteDTO.getStatus())
            && (lstCr == null || lstCr.size() == 0)
            && (srInsiteDTO.getLstCr() == null || srInsiteDTO.getLstCr().size() == 0)) {
          try {
            List<SRConfigDTO> lstLink = srConfigRepository
                .getByConfigGroup(Constants.SR_CONFIG.WS_BCCS_AUTO_CREATE_CR_URL);
            List<SRConfigDTO> lstUsername = srConfigRepository
                .getByConfigGroup(Constants.SR_CONFIG.WS_BCCS_AUTO_CREATE_CR_USERNAME);
            List<SRConfigDTO> lstPassword = srConfigRepository
                .getByConfigGroup(Constants.SR_CONFIG.WS_BCCS_AUTO_CREATE_CR_PASS);
            if ((lstLink == null || lstLink.size() == 0) || (lstUsername == null || lstUsername
                .isEmpty()) || (lstPassword == null || lstPassword.isEmpty())) {
              resultInSideDto.setMessage(I18n.getValidation("sr.mopwsbccs.noConfig"));
              return resultInSideDto;
            }
            log.info("\n Link WS WSForOtherSystem : " + lstLink.get(0).getConfigCode()
                + "\n user : " + lstUsername.get(0).getConfigCode()
                + "\n pass : " + lstPassword.get(0).getConfigCode());

            com.viettel.bccs.JsonResponseBO bccsResult = null;
            log.info("--------- getDataJson START---------");
            URL url = new URL(lstLink.get(0).getConfigCode());
            com.viettel.bccs.WSForOtherSystemService serviceBCCS = new com.viettel.bccs.WSForOtherSystemService(
                url);
            com.viettel.bccs.WSForOtherSystem port = serviceBCCS.getWSForOtherSystemPort();
            setTimeOutWs(port);
            com.viettel.bccs.AuthorityBO authorityBO = new com.viettel.bccs.AuthorityBO();
            authorityBO.setUserName(lstUsername.get(0).getConfigCode());
            authorityBO.setPassword(lstPassword.get(0).getConfigCode());
            com.viettel.bccs.RequestInputBO requestInputBO = new com.viettel.bccs.RequestInputBO();
            requestInputBO.setCode(SR_CONFIG.WS_BCCS_AUTO_CREATE_CR_CODE);
            List<com.viettel.bccs.ParameterBO> lstParams = new ArrayList<>();
            com.viettel.bccs.ParameterBO parameterBO = new com.viettel.bccs.ParameterBO();
            parameterBO.setName("SR");
            parameterBO.setValue(srInsiteDTO.getSrCode());
            lstParams.add(parameterBO);
            requestInputBO.getParams().add(parameterBO);
            bccsResult = port.getDataJson(authorityBO, requestInputBO);
            //<editor-fold desc="LOG PARAM getDataJson" defaultstate="collapsed">
            String logBCCS = "\n <authorityBO>"
                + "<userName>" + authorityBO.getUserName() + "</userName>\n"
                + "<password>" + authorityBO.getPassword() + "</password>\n"
                + "<requestId>" + authorityBO.getRequestId() + "</requestId>\n"
                + "</authorityBO>\n"
                + "requestInputBO\n"
                + "<code>" + requestInputBO.getCode() + "</code>\n";
            if (requestInputBO.getParams() != null && !requestInputBO.getParams().isEmpty()) {
              for (com.viettel.bccs.ParameterBO item : requestInputBO.getParams()) {
                logBCCS += "<name>" + item.getName() + "</name>\n"
                    + "<value>" + item.getValue() + "</value>\n";
              }
            }
            logBCCS += " </requestInputBO>\n";
            log.info(logBCCS);
            log.info("---------getListTemplatesByProcedure VIPA END");
            //</editor-fold>
            if (bccsResult != null && bccsResult.getTotalDataJson() == 1) {
              if (StringUtils.isNotNullOrEmpty(bccsResult.getDataJson())) {
                log.info(" DataJson : " + bccsResult.getDataJson());
                SRMopDTO dto = new SRMopDTO();
                dto.setSrId(srInsiteDTO.getSrId().toString());
                List<SRMopDTO> listMOP = srMopRepository.getListSRMopDTO(dto);
                Map<String, String> mapCheckMop = new HashMap<>();
                if (listMOP != null && !listMOP.isEmpty()) {
                  for (SRMopDTO srMopDTO : listMOP) {
                    if (!mapCheckMop.containsKey(srMopDTO.getDtId())) {
                      mapCheckMop.put(srMopDTO.getDtId(), srMopDTO.getDtId());
                    }
                  }
                }
                ObjectMapper mapper = new ObjectMapper();
                Object obj = mapper.readValue(bccsResult.getDataJson(), Object.class);
                List<Object> temp = (List<Object>) ((LinkedHashMap<String, Object>) obj)
                    .get("data");
                List<SRMopDTO> lstMopBCCS = new ArrayList<>();
                if (temp != null && !temp.isEmpty()) {
                  for (Object objTemp : temp) {
                    String mopName = (String) ((LinkedHashMap<String, Object>) objTemp)
                        .get("flow_run_name");
                    Integer mopId = (Integer) ((LinkedHashMap<String, Object>) objTemp)
                        .get("flow_run_id");
                    SRMopDTO mopDto = new SRMopDTO();
                    if (mopId != null && mopId > 0) {
                      mopDto.setDtId(mopId.toString());
                    }
                    mopDto.setDtName(mopName);
                    mopDto.setSrId(srInsiteDTO.getSrId().toString());
                    mopDto.setType(Constants.SR_CONFIG.VIPA);
                    if (!mapCheckMop.containsKey(mopDto.getDtId())) {
                      lstMopBCCS.add(mopDto);
                    }
                  }
                }
                if (lstMopBCCS != null && !lstMopBCCS.isEmpty()) {
                  resultInSideDto = srMopRepository.insertListSRMop(lstMopBCCS);
                  if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                    return resultInSideDto;
                  }
                } else {
                  if (mapCheckMop.isEmpty()) {
                    resultInSideDto.setMessage(I18n.getValidation("sr.mopwsbccs.noData"));
                    return resultInSideDto;
                  }
                }
              }
            } else {
              resultInSideDto.setMessage(I18n.getValidation("sr.mopwsbccs.noData"));
              if (StringUtils.isNotNullOrEmpty(bccsResult.getDetailError())) {
                resultInSideDto.setMessage(bccsResult.getDetailError());
              }
              return resultInSideDto;
            }
          } catch (Exception e1) {
            log.error(e1.getMessage(), e1);
            resultInSideDto.setMessage(e1.getMessage());
            return resultInSideDto;
          }
        }
        // WS BCCS tao SR_SR_TOOL kiem tra neu SR chuyen trang thai
        // EVALUATED va da tao Cr hoac link CR thi khong goi WS BCCS lay MOP nua cho chuyen trang thai
        else if (SR_STATUS.EVALUATED.equals(srInsiteDTO.getStatus())
            && ((lstCr != null && !lstCr.isEmpty())
            || (srInsiteDTO.getLstCr() != null
            && !srInsiteDTO.getLstCr().isEmpty()))) {
          checkBCCS = true;
        }
        //      end
      } else {
        GnocFileDto srFile = new GnocFileDto();
        srFile.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
        srFile.setBusinessId(srInsiteDTO.getSrId());
        srFile.setFileType(Constants.SR_ROLE_UPDATE.FILE_TYPE_TOOL);
        List<GnocFileDto> listSRFileDTO = gnocFileRepository.getListGnocFileForSR(srFile);
        if (listSRFileDTO == null || listSRFileDTO.isEmpty()) {
          resultInSideDto.setMessage(I18n.getValidation("sr.not.fileTool"));
          return resultInSideDto;
        }
      }
    }
    //<editor-fold desc="duongnt: validate bat buoc dinh kem file Aom và file Nims">
    // File Nims
    GnocFileDto fileTemp = new GnocFileDto();
    fileTemp.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
    fileTemp.setBusinessId(srInsiteDTO.getSrId());
    List<GnocFileDto> lstFileAttach = gnocFileRepository.getListGnocFileForSR(fileTemp);
    if (srInsiteDTO.isServiceNims()) {
      if (lstFileAttach == null || lstFileAttach.isEmpty()) {
        resultInSideDto.setMessage(I18n.getLanguage("sr.nims.file.attachment.notfound"));
        return resultInSideDto;
      } else {
        GnocFileDto dtoNims = new GnocFileDto();
        dtoNims.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
        dtoNims.setBusinessId(srInsiteDTO.getSrId());
        dtoNims.setFileType(Constants.SR_ROLE_UPDATE.NIMS);
        List<GnocFileDto> lstFileNims = gnocFileRepository.getListGnocFileForSR(dtoNims);
        if (lstFileNims == null || lstFileNims.isEmpty()) {
          resultInSideDto.setMessage(I18n.getLanguage("sr.nims.file.attachment.notfound"));
          return resultInSideDto;
        }
      }
    }
    if (srInsiteDTO.isServiceAom()) {
      if (lstFileAttach == null || lstFileAttach.isEmpty()) {
        resultInSideDto.setMessage(I18n.getValidation("sr.file.not.attach") + " " + I18n
            .getValidation("sr.tab.attach.fileAOM"));
        return resultInSideDto;
      }
      GnocFileDto fileGate = new GnocFileDto();
      fileGate.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
      fileGate.setBusinessId(srInsiteDTO.getSrId());
      List<GnocFileDto> lstFileGate = gnocFileRepository.getListGnocFileForSR(fileGate);
      if (lstFileGate == null || lstFileGate.isEmpty()) {
        resultInSideDto.setMessage(I18n.getValidation("sr.file.not.attach") + " " + I18n
            .getValidation("sr.tab.attach.fileAOM"));
        return resultInSideDto;
      }
      if (!lstFileGate.stream()
          .filter(o -> Constants.SR_ROLE_UPDATE.AOM.equalsIgnoreCase(o.getFileType())).findAny()
          .isPresent()) {
        resultInSideDto.setMessage(I18n.getValidation("sr.file.not.attach") + " " + I18n
            .getValidation("sr.tab.attach.fileAOM"));
        return resultInSideDto;
      }
      if (!lstFileGate.stream().filter(
          o -> Constants.SR_ROLE_UPDATE.FILE_TYPE_TRINH_KY.equalsIgnoreCase(o.getFileType()))
          .findAny().isPresent()) {
        resultInSideDto.setMessage(I18n.getValidation("gate.file.attachment.notfound.AOMTK"));
        return resultInSideDto;
      }
    }
//    //</editor-fold>
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setCheck(checkBCCS);
    return resultInSideDto;
  }

  @Override
  public String updateStatusSRForProcess(String srId, String status) {
    log.info("Request to updateStatusSRForProcess : {}", srId, status);
    String res = Constants.RESULT.FAIL;
    try {
      List<SRConfigDTO> lstConfigSRChild = srConfigRepository
          .getByConfigGroup(SR_CONFIG.AUTO_CREATE_SR_CHILD);
      String[] arrId = srId.split(",");
      for (String id : arrId) {
        SrInsiteDTO objBefore = srRepository.getDetailNoOffset(Long.valueOf(id.trim()));
        if (objBefore == null) {
          continue;
        }
        String oldStatus = objBefore.getStatus();
        objBefore.setStatus(status);
        objBefore.setUpdatedUser("system");
        objBefore.setUpdatedTime(new Date());
        ResultInSideDto resU = updateSR(objBefore);
        res = resU.getKey();
        if (!Constants.RESULT.SUCCESS.equals(res)) {
          return res;
        } else {
          //03/12/2020 dungpv edit
          //sr cha sinh sr con
          if (SR_STATUS.NEW.equals(oldStatus) && lstConfigSRChild != null && !lstConfigSRChild
              .isEmpty() && StringUtils.isNotNullOrEmpty(status) && StringUtils
              .isStringNullOrEmpty(objBefore.getParentCode())) {
            for (SRConfigDTO srConfigDTO : lstConfigSRChild) {
              if (status.equals(srConfigDTO.getConfigCode())) {
                SRCatalogDTO srCatalogDTO = srCatalogRepository2
                    .findById(Long.parseLong(objBefore.getServiceId()));
                resU = autoCreateSRChild(srCatalogDTO, objBefore);
                if (!RESULT.SUCCESS.equals(resU.getKey())) {
                  return resU.getMessage();
                }
                break;
              }
            }
          }
          //sr la sr con va trang thai moi la concluded thi kiem tra thu tu sinh de sinh sr con luot tiep theo
          if (SR_STATUS.CONCLUDED.equals(status) && StringUtils
              .isNotNullOrEmpty(objBefore.getParentCode())) {
            ResultInSideDto resultChildAuto = autoCreateSRChildByGennerateNo(objBefore.getSrCode());
            if (!RESULT.SUCCESS.equals(resultChildAuto.getKey())) {
              return resultChildAuto.getMessage();
            }
          }
          //end
        }
      }
      return res;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return res;
  }

  private String exportFileResultNims(List lstDataObject, String type, SrInsiteDTO data) {
    try {
      ExcelWriterUtils ewu = new ExcelWriterUtils();
      String templatePathOut = "";
      UserToken userToken = ticketProvider.getUserToken();
      UsersInsideDto unitToken = userRepository.getUserDTOByUserName(userToken.getUserName());
      GnocFileDto fileAttach = new GnocFileDto();
      fileAttach.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
      fileAttach.setBusinessId(data.getSrId());
      fileAttach.setFileType(Constants.SR_ROLE_UPDATE.NIMS);
      List<GnocFileDto> lstFileAttach = gnocFileRepository.getListGnocFileForSR(fileAttach);
      if (lstFileAttach != null && !lstFileAttach.isEmpty()) {
        for (GnocFileDto item : lstFileAttach) {
          templatePathOut = item.getPath();
        }
      }
      byte[] bytes = FileUtils
          .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), templatePathOut);
      templatePathOut = FileUtils
          .saveTempFile(FileUtils.getFileName(templatePathOut), bytes,
              tempFolder);
      Workbook workBook = ewu.readFileExcel(templatePathOut);
      Sheet sheet = workBook.getSheetAt(0);
      int i = 1;
      CellStyle cellSt1 = sheet.getRow(0).getCell(0).getCellStyle();
      ewu.createCell(sheet, 5, 0, I18n.getLanguage("srCatalog.data.import.result"), cellSt1);
      ewu.createCell(sheet, 6, 0, I18n.getLanguage("sr.nims.realIp"), cellSt1);
      for (Object row : lstDataObject) {
        DistributeIpResourceForm rowParse = (DistributeIpResourceForm) row;
        while (true) {
          Cell cellData = sheet.getRow(i) == null ? null : sheet.getRow(i).getCell(0);
          if (cellData == null) {
            i++;
          } else {
            break;
          }
        }
        ewu.createCell(sheet, 1, i, rowParse.getLocation() == null ? "" : rowParse.getLocation());
        ewu.createCell(sheet, 2, i, rowParse.getRange() == null ? "" : rowParse.getRange());
        ewu.createCell(sheet, 3, i, rowParse.getSystem() == null ? "" : rowParse.getSystem());
        ewu.createCell(sheet, 4, i, rowParse.getPurpose() == null ? "" : rowParse.getPurpose());
        ewu.createCell(sheet, 5, i, rowParse.getMessage() == null ? "" : rowParse.getMessage());
        ewu.createCell(sheet, 6, i, rowParse.getRealIp() == null ? "" : rowParse.getRealIp());
        i++;
      }

      String fileResult = templatePathOut.substring(0, (templatePathOut.lastIndexOf("/") == -1)
          ? templatePathOut.lastIndexOf(File.separator) : templatePathOut.lastIndexOf("/"));
      String fileName = "ImportFormNims_Result" + "_" + System.currentTimeMillis() + ".xlsx";
      ewu.saveToFileExcel(fileResult, fileName);
      byte[] byte1 = FileUtils.convertFileToByte(new File(fileResult + fileName));
      String fullPath = FileUtils
          .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), ftpFolder, fileName,
              byte1, null);
      String fullPathOld = FileUtils
          .saveUploadFile(fileName, byte1, uploadFolder);
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      //Start save file old
      SRFilesDTO srFileDto = new SRFilesDTO();
      srFileDto.setFileName(FileUtils.getFileName(fullPathOld));
      srFileDto.setFilePath(fullPathOld);
      srFileDto.setObejctId(data.getSrId());
      srFileDto.setFileGroup(SR_CONFIG.FILE_GROUP_SR);
      srFileDto.setFileType(SR_ROLE_UPDATE.FILE_TYPE_OTHER);
      ResultInSideDto resultFileDataOld = srRepository.addSRFile(srFileDto);
      //End save file old
      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setPath(fullPath);
      gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
      gnocFileDto.setCreateUnitId(unitToken.getUnitId());
      gnocFileDto.setCreateUnitName(unitToken.getUnitName());
      gnocFileDto.setCreateUserId(userToken.getUserID());
      gnocFileDto.setCreateUserName(userToken.getUserName());
      gnocFileDto.setCreateTime(new Date());
      gnocFileDto.setFileType(SR_ROLE_UPDATE.FILE_TYPE_OTHER);
      gnocFileDto.setMappingId(resultFileDataOld.getId());
      gnocFileDtos.add(gnocFileDto);
      gnocFileRepository.saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SR,
          data.getSrId(), gnocFileDtos);
      if ("RETURN_FILE".equals(type)) {
        return fileResult + fileName;
      } else {
        return RESULT.SUCCESS;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  //truongnt add start
  @Override
  public List<SRApproveDTO> getListSRApprove(Long srId) {
    log.info("Request to getListSRApprove : {}", srId);
    SRApproveDTO approve = srApproveRepository.findSRApproveBySrId(srId);
    List<SRApproveDTO> lstSRApprove = new ArrayList<>();
    if (approve != null) {
      SRApproveDTO approveDTO;
      if (!StringUtils.isLongNullOrEmpty(approve.getApproveUnitLevel1())) {
        approveDTO = new SRApproveDTO();
        approveDTO.setIsApproved(
            !StringUtils.isStringNullOrEmpty(approve.getApproveLevel1())
                ? (approve.getApproveLevel1() == 1L ? I18n.getLanguage("sr.approve.approved")
                : I18n.getLanguage("sr.approve.notApproved"))
                : I18n.getLanguage("sr.approve.waittingForApprove"));
        approveDTO.setApproveDate(approve.getApproveDateLevel1());
        approveDTO.setApproveUser(approve.getApproveUserLevel1());
        UnitDTO unit = unitRepository.findUnitById(approve.getApproveUnitLevel1());
        approveDTO.setApproveUnit(
            unit != null ? I18n.getLanguage("sr.approve.unit.level1") + " " + unit.getUnitName()
                : "");
        lstSRApprove.add(approveDTO);
      }
      if (!StringUtils.isStringNullOrEmpty(approve.getApproveUnitLevel2())) {
        approveDTO = new SRApproveDTO();
        approveDTO.setIsApproved(
            !StringUtils.isStringNullOrEmpty(approve.getApproveLevel2())
                ? ("1".equals(String.valueOf(approve.getApproveLevel2())) ? I18n
                .getLanguage("sr.approve.approved")
                : I18n.getLanguage("sr.approve.notApproved"))
                : I18n.getLanguage("sr.approve.waittingForApprove"));
        approveDTO.setApproveDate(approve.getApproveDateLevel2());
        approveDTO.setApproveUser(approve.getApproveUserLevel2());
        UnitDTO unit = unitRepository.findUnitById(approve.getApproveUnitLevel2());
        approveDTO.setApproveUnit(
            unit != null ? I18n.getLanguage("sr.approve.unit.level2") + " " + unit.getUnitName()
                : "");
        lstSRApprove.add(approveDTO);
      }
    }
    return lstSRApprove;
  }

  @Override
  public List<SRRenewDTO> getListSRRenewDTO(SrInsiteDTO srInsiteDTO) {
    log.info("Request to getListSRRenewDTO : {}", srInsiteDTO);
    UserToken userToken = ticketProvider.getUserToken();
    Double offset = userRepository.getOffsetFromUser(userToken.getUserName());
    ConditionBean conditionBean = new ConditionBean("srId", srInsiteDTO.getSrId().toString(),
        Constants.NAME_EQUAL, Constants.NUMBER);
    List<ConditionBean> lstConditionBeans = new ArrayList<>();
    lstConditionBeans.add(conditionBean);
    ConditionBeanUtil.sysToOwnListCondition(lstConditionBeans);
    List<SRRenewDTO> lstSRRenew = srRepository
        .getListSRRenewDTO(new SRRenewEntity(), lstConditionBeans, 0, 0, "", "");
    for (SRRenewDTO item : lstSRRenew) {
      item = converTimeFromClientToServer(item, offset);
      if (item.getStatusRenew() == null || item.getStatusRenew() == 0L) {
        item.setStatusRenewStr(I18n.getLanguage("sr.srRenew.waittingForApprove"));
      } else if (item.getStatusRenew() == 1L) {
        item.setStatusRenewStr(I18n.getLanguage("sr.srRenew.approved"));
        item.setUpdatedUser(
            srInsiteDTO.getCreatedUser() == null ? "" : srInsiteDTO.getCreatedUser());
      } else if (item.getStatusRenew() == 2L) {
        item.setStatusRenewStr(I18n.getLanguage("sr.srRenew.notApproved"));
        item.setUpdatedUser(
            srInsiteDTO.getCreatedUser() == null ? "" : srInsiteDTO.getCreatedUser());
      }
    }
    return lstSRRenew;
  }

  @Override
  public ResultInSideDto deleteMopFileWS(SrInsiteDTO srInsiteDTO) {
    log.info("Request to deleteMopFileWS : {}", srInsiteDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (srInsiteDTO.getLstMopTmp() != null && !srInsiteDTO.getLstMopTmp().isEmpty()) {
      List<SRMopDTO> lstMop = srInsiteDTO.getLstMopTmp();
      int size = srInsiteDTO.getLstMopTmp().size();
      for (int i = size - 1; i > -1; i--) {
        List<SRMopDTO> lstDelete = new ArrayList<>();
        List<SRMopDTO> listSRMOPDTO = srMopRepository.getListSRMopDTO(lstMop.get(i));
        if (listSRMOPDTO != null && !listSRMOPDTO.isEmpty()) {
          lstDelete.add(lstMop.get(i));
          srMopRepository.deleteListSRMop(lstDelete);
        }
      }
      for (SRMopDTO srmopdto : lstMop) {
        GnocFileDto file = new GnocFileDto();
        file.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
        file.setBusinessId(Long.parseLong(srmopdto.getSrId()));
        file.setTemplateId(Long.parseLong(srmopdto.getTemplateId()));
        file.setTypeWs(srmopdto.getType());
        List<GnocFileDto> lstFile = gnocFileRepository.getListGnocFileForSR(file);
        if (lstFile != null && !lstFile.isEmpty()) {
          for (GnocFileDto fileDto : lstFile) {
            gnocFileRepository.deleteGnocFileByDto(fileDto);
            srRepository.deleteSRFile(fileDto.getMappingId());
          }
        }
      }
      //<editor-fold desc="lynv delete WS_MOP VMSA" defaultstate="collapsed">
      if (Constants.SR_CONFIG.VMSA.equals(lstMop.get(0).getType())) {
        List<com.viettel.vmsa2.DtObjDelete> listDelete = new ArrayList<>();
        for (SRMopDTO srmopdto : lstMop) {
          com.viettel.vmsa2.DtObjDelete dtObj = new com.viettel.vmsa2.DtObjDelete();
          dtObj.setDeleteDtId(srmopdto.getDtId());
          listDelete.add(dtObj);
        }
        List<SRConfigDTO> lstLink = srConfigRepository
            .getByConfigGroup(Constants.SR_CONFIG.WS_VMSA_URL);

        List<SRConfigDTO> lstUsername = srConfigRepository
            .getByConfigGroup(Constants.SR_CONFIG.WS_VMSA_USERNAME);
        String usernameVMSA = stringDecrypt(userNameVMSA, saltService);
        if (lstUsername != null && !lstUsername.isEmpty()) {
          usernameVMSA = lstUsername.get(0).getConfigCode();
        }
        String pVMSA = stringDecrypt(passVMSA, saltService);
        List<SRConfigDTO> lstPassword = srConfigRepository
            .getByConfigGroup(Constants.SR_CONFIG.WS_VMSA_P);
        if (lstPassword != null && !lstPassword.isEmpty()) {
          pVMSA = lstPassword.get(0).getConfigCode();
        }

        URL url = null;
        try {
          url = new URL(lstLink.get(0).getConfigCode());

        } catch (Exception ex) {
          log.error(ex.getMessage());
        }
        com.viettel.vmsa2.ResultDeleteDt vmsaResult = null;
        int nTry = 0;
        while (nTry < 3) {
          try {
            com.viettel.vmsa2.WSForGNOCImplService serviceVMSA = new com.viettel.vmsa2.WSForGNOCImplService(
                url);
            com.viettel.vmsa2.WSForGNOC port = serviceVMSA.getWSForGNOCImplPort();
            vmsaResult = port.deleteDts(usernameVMSA, pVMSA, crSystemId, listDelete);
            break;
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            nTry++;
            try {
              Thread.sleep(1000);
            } catch (Exception ex) {
              log.error(ex.getMessage(), ex);
            }
          }
        }
        if (vmsaResult != null) {
          resultInSideDto.setKey(vmsaResult.getResultCode() == 0 ? RESULT.SUCCESS : RESULT.FAIL);
          resultInSideDto.setMessage(vmsaResult.getResultMessage());
        }
        //<editor-fold desc="LOG PARAM getListTemplatesByProcedure VMSA" defaultstate="collapsed">
        StringBuilder logVMSA = new StringBuilder();
        logVMSA.append("\n usernameVMSA: " + usernameVMSA
            + "\n passwordVMSA: " + pVMSA
            + "\n crSystemId: " + crSystemId
            + "\n <listDtDelete>");
        if (listDelete != null && !listDelete.isEmpty()) {
          for (com.viettel.vmsa2.DtObjDelete dtObjDelete : listDelete) {
            logVMSA.append("\n deleteDtId: " + dtObjDelete.getDeleteDtId());
          }
        }
        logVMSA.append("\n </listDtDelete>");
        log.info("\n---------PARAM deleteDts VMSA START \n" + logVMSA.toString());
        log.info("MESSAGE DELETE MOP VMSA: " + (vmsaResult != null ? (
            vmsaResult.getResultMessage() == null ? "" : vmsaResult.getResultMessage()) : ""));
        log.info("---------PARAM deleteDts VMSA END");
        //</editor-fold>
      } //</editor-fold>
      //<editor-fold desc="lynv delete WS_MOP AAM" defaultstate="collapsed">
      else if (Constants.SR_CONFIG.AAM.equals(lstMop.get(0).getType())) {
        List<com.viettel.aam2.DtObjDelete> listDelete = new ArrayList<>();
        for (SRMopDTO srmopdto : lstMop) {
          com.viettel.aam2.DtObjDelete dtObj = new com.viettel.aam2.DtObjDelete();
          dtObj.setDeleteDtId(srmopdto.getDtId());
          listDelete.add(dtObj);
        }
        List<SRConfigDTO> lstLink = srConfigRepository
            .getByConfigGroup(Constants.SR_CONFIG.WS_AAM_URL);

        List<SRConfigDTO> lstUsername = srConfigRepository
            .getByConfigGroup(Constants.SR_CONFIG.WS_AAM_USERNAME);
        String usernameAAM = stringDecrypt(userNameAAM, saltService);
        if (lstUsername != null && !lstUsername.isEmpty()) {
          usernameAAM = lstUsername.get(0).getConfigCode();
        }
        String pAAM = stringDecrypt(passAAM, saltService);
        List<SRConfigDTO> lstPassword = srConfigRepository
            .getByConfigGroup(Constants.SR_CONFIG.WS_AAM_P);
        if (lstPassword != null && !lstPassword.isEmpty()) {
          pAAM = lstPassword.get(0).getConfigCode();
        }

        URL url = null;
        try {
          url = new URL(lstLink.get(0).getConfigCode());

        } catch (MalformedURLException ex) {
          log.error(ex.getMessage(), ex);
        }
        com.viettel.aam2.ResultDeleteDt aamResult = null;
        int nTry = 0;
        while (nTry < 3) {
          try {
            com.viettel.aam2.TdttWebserviceImplService serviceAAM = new com.viettel.aam2.TdttWebserviceImplService(
                url);
            com.viettel.aam2.TdttWebservice port = serviceAAM.getTdttWebserviceImplPort();
            aamResult = port.deleteDts(usernameAAM, pAAM, crSystemId, listDelete);
            break;
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            nTry++;
            try {
              Thread.sleep(1000);
            } catch (Exception ex) {
              log.error(ex.getMessage());
            }
          }
        }
        resultInSideDto.setKey(aamResult.getResultCode() == 0 ? RESULT.SUCCESS : RESULT.FAIL);
        resultInSideDto.setMessage(aamResult.getResultMessage());
        //<editor-fold desc="LOG PARAM getListTemplatesByProcedure AAM" defaultstate="collapsed">
        StringBuilder logAAM = new StringBuilder();
        logAAM.append("\n usernameAAM: " + usernameAAM
            + "\n passwordAAM: " + pAAM
            + "\n crSystemId: " + crSystemId
            + "\n <listDtDelete>");
        if (listDelete != null && !listDelete.isEmpty()) {
          for (com.viettel.aam2.DtObjDelete dtObjDelete : listDelete) {
            logAAM.append("\n deleteDtId: " + dtObjDelete.getDeleteDtId());
          }
        }
        logAAM.append("\n </listDtDelete>");
        log.info("\n ---------PARAM deleteDts AAM START \n" + logAAM.toString());
        log.info("---------PARAM deleteDts AAM END");
        log.info("MESSAGE DELETE MOP AAM: " + aamResult.getResultMessage());
        //</editor-fold>
      }//</editor-fold>
      //<editor-fold desc="lynv delete WS_MOP VIPA" defaultstate="collapsed">
      else if (Constants.SR_CONFIG.VIPA.equals(lstMop.get(0).getType())) {
        List<com.viettel.vipa2.DtObjDelete> listDelete = new ArrayList<>();
        for (SRMopDTO srmopdto : lstMop) {
          com.viettel.vipa2.DtObjDelete dtObj = new com.viettel.vipa2.DtObjDelete();
          dtObj.setDeleteDtId(srmopdto.getDtId());
          listDelete.add(dtObj);
        }
        List<SRConfigDTO> lstLink = srConfigRepository
            .getByConfigGroup(Constants.SR_CONFIG.WS_VIPA_URL);

        List<SRConfigDTO> lstUsername = srConfigRepository
            .getByConfigGroup(Constants.SR_CONFIG.WS_VIPA_USERNAME);
        String usernameVIPA = stringDecrypt(userNameVIPA, saltService);
        if (lstUsername != null && !lstUsername.isEmpty()) {
          usernameVIPA = lstUsername.get(0).getConfigCode();
        }
        String pVIPA = stringDecrypt(passVIPA, saltService);
        List<SRConfigDTO> lstPassword = srConfigRepository
            .getByConfigGroup(Constants.SR_CONFIG.WS_VIPA_P);
        if (lstPassword != null && !lstPassword.isEmpty()) {
          pVIPA = lstPassword.get(0).getConfigCode();
        }

        URL url = null;
        try {
          url = new URL(lstLink.get(0).getConfigCode());

        } catch (MalformedURLException ex) {
          log.error(ex.getMessage(), ex);
        }
        com.viettel.vipa2.ResultDeleteDt vipaResult = null;
        int nTry = 0;
        while (nTry < 3) {
          try {
            com.viettel.vipa2.WSForGNOCImplService serviceVIPA = new com.viettel.vipa2.WSForGNOCImplService(
                url);
            com.viettel.vipa2.WSForGNOC port = serviceVIPA.getWSForGNOCImplPort();
            vipaResult = port.deleteDts(usernameVIPA, pVIPA, crSystemId, listDelete);
            break;
          } catch (Exception e) {
            log.error(e.getMessage());
            nTry++;
            try {
              Thread.sleep(1000);
            } catch (Exception ex) {
              log.error(ex.getMessage(), ex);
            }
          }
        }
        resultInSideDto.setKey(vipaResult.getResultCode() == 0 ? RESULT.SUCCESS : RESULT.FAIL);
        resultInSideDto.setMessage(vipaResult.getResultMessage());
        //<editor-fold desc="LOG PARAM getListTemplatesByProcedure VIPA" defaultstate="collapsed">
        StringBuilder logVIPA = new StringBuilder();
        logVIPA.append("\n usernameVIPA: " + usernameVIPA
            + "\n passwordVIPA: " + pVIPA
            + "\n crSystemId: " + crSystemId
            + "\n <listDtDelete>");
        if (listDelete != null && !listDelete.isEmpty()) {
          for (com.viettel.vipa2.DtObjDelete dtObjDelete : listDelete) {
            logVIPA.append("\n deleteDtId: " + dtObjDelete.getDeleteDtId());
          }
        }
        logVIPA.append("\n</listDtDelete>");
        log.info("\n ---------PARAM deleteDts VIPA START \n" + logVIPA.toString());
        log.info("---------PARAM deleteDts VIPA END");
        log.info("MESSAGE DELETE MOP VIPA: " + vipaResult.getResultMessage());
        //</editor-fold>
      }//</editor-fold>
    }
    return resultInSideDto;
  }

  @Override
  public List<SRDTO> getListSRForWO(SRDTO srdto) {
    return srRepository.getListSRForWO(srdto);
  }

  @Override
  public List<SrInsiteDTO> getListDataSearchForPt(SrInsiteDTO srInsiteDTO) {
    log.debug("Request to getListDataSearchForOther : {}", srInsiteDTO);
    UserToken userToken = ticketProvider.getUserToken();
    srInsiteDTO.setUserId(userToken.getUserID().toString());
    srInsiteDTO.setInsertSource("PT");
    srInsiteDTO.setPage(1);
    srInsiteDTO.setPageSize(Integer.MAX_VALUE);
    List<SrInsiteDTO> listDataSearch = (List<SrInsiteDTO>) getLstSR(
        srRepository.getListSR(srInsiteDTO), srInsiteDTO, "getlist").getData();
    return listDataSearch;
  }

  private SRRenewDTO converTimeFromClientToServer(SRRenewDTO dto, Double offset) {
    try {
      dto.setEndTime(converClientDateToServerDate(dto.getEndTime(), offset));
      dto.setEndTimeRenew(converClientDateToServerDate(dto.getEndTimeRenew(), offset));
      dto.setCreatedTime(converClientDateToServerDate(dto.getCreatedTime(), offset));
      dto.setUpdatedTime(converClientDateToServerDate(dto.getUpdatedTime(), offset));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return dto;
  }

  private SrInsiteDTO convertSRDate2VietNamDate(SrInsiteDTO oldDto, Double offset) {
    try {
      oldDto.setStartTime(converClientDateToServerDate(oldDto.getStartTime(), offset));
      oldDto.setEndTime(converClientDateToServerDate(oldDto.getEndTime(), offset));
      oldDto.setCreatedTime(converClientDateToServerDate(oldDto.getCreatedTime(), offset));
      oldDto.setSendDate(converClientDateToServerDate(oldDto.getSendDate(), offset));
      oldDto.setUpdatedTime(converClientDateToServerDate(oldDto.getUpdatedTime(), offset));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return oldDto;
  }

  private Date converClientDateToServerDate(Date clientTime, Double offset) {
    Date result = clientTime;
    if (offset == null || offset.equals(0.0)) {
      return result;
    }
    if (clientTime == null) {
      return result;
    }
    try {
      Calendar cal = Calendar.getInstance();
      Date date = result;
      cal.setTime(date);
      cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - offset.intValue());
      return cal.getTime();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }
  //truongnt add end

  private void actionSaveLinkCr(List<CrInsiteDTO> lstCr, Long srId) {
    if (lstCr != null && !lstCr.isEmpty()) {
      List<CrInsiteDTO> lstCrOld = findListCrBySr(srId);
      try {
        if (lstCrOld != null && !lstCrOld.isEmpty()) {
          for (int i = lstCr.size() - 1; i > -1; i--) {
            for (CrInsiteDTO dtoOld : lstCrOld) {
              if (dtoOld.getCrNumber().equalsIgnoreCase(lstCr.get(i).getCrNumber())) {
                //cac CR gan vs SR truoc do se ko gan cac file dinh kem tai thoi diem link CR
                lstCr.remove(i);
                break;
              }
            }
          }
        }
        for (CrInsiteDTO cr : lstCr) {
          UserToken userToken = ticketProvider.getUserToken();
          CrCreatedFromOtherSysDTO ccfosdto = new CrCreatedFromOtherSysDTO();
          ccfosdto.setSystemId(crSystemId);
          ccfosdto.setObjectId(srId.toString());
          ccfosdto.setIsActive("1");
          ccfosdto.setCrId(cr.getCrId());
          ResultInSideDto retSaveCR = crServiceProxy.insertCrCreatedFromOtherSystem(ccfosdto);
          if (Constants.CR_RETURN_MESSAGE.SUCCESS.equalsIgnoreCase(retSaveCR.getKey())) {
            GnocFileDto file = new GnocFileDto();
            file.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
            file.setBusinessId(srId);
            List<GnocFileDto> lsFile = gnocFileRepository.getListGnocFileForSR(file);
            if (lsFile != null) {
              InsertFileDTO insertFile = new InsertFileDTO();
              insertFile.setFileType("100");
              insertFile.setCrNumber(cr.getCrNumber());
              insertFile.setUserName(userToken.getUserName());
              for (GnocFileDto item : lsFile) {
                insertFile.setFileName(item.getFileName());
                File fileContent = new File(item.getPath());
                insertFile.setFileContent(DataUtil.getFileContent(fileContent));
                crServiceProxy.insertFile(insertFile);
              }
            }
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  private ResultInSideDto autoCreateCR(SrInsiteDTO srInsiteDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.FAIL, null);
    SRCatalogDTO srCatalogDTO = srCatalogRepository2
        .findById(Long.valueOf(srInsiteDTO.getServiceId()));
    boolean isCreateCRWO = true;
    boolean isCrNodes = false;
    boolean success = false;

    SRCreateAutoCRDTO srcacrdto = new SRCreateAutoCRDTO();
    srcacrdto.setSrId(srInsiteDTO.getSrId());
    List<SRCreateAutoCRDTO> lstCreate = srCreateAutoCrRepository
        .searchSRCreateAutoCR(srcacrdto, 0, 0, "", "");
    SRCreateAutoCRDTO srCreateAutoCRDTO =
        lstCreate.isEmpty() ? new SRCreateAutoCRDTO() : lstCreate.get(0);
    Map<String, GnocFileDto> gnocFilePathProceeMap = new HashMap<>();
    GnocFileDto gnocFileDto = new GnocFileDto();
    gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.SR_CREATE_AUTO_CR);
    gnocFileDto.setBusinessId(srCreateAutoCRDTO.getId());
    List<GnocFileDto> gnocFileWoList = gnocFileRepository.getListGnocFileForSR(gnocFileDto);
    UsersInsideDto userExecute = userRepository
        .getUserDTOByUserName(srInsiteDTO.getUpdatedUser());
    UnitDTO unitExecute = unitRepository.findUnitById(userExecute.getUnitId());
    String crProcessParentId = null;
    String crProcessId = null;
    List<SRMappingProcessCRDTO> listSRMapping = srCategoryServiceProxy
        .getListSRMappingProcessCRDTO(new SRMappingProcessCRDTO(srCatalogDTO.getServiceCode()));
    SRMappingProcessCRDTO srMappingProcessCRDTO = new SRMappingProcessCRDTO();
    if (listSRMapping != null && !listSRMapping.isEmpty()) {
      srMappingProcessCRDTO = listSRMapping.get(0);
      if (srMappingProcessCRDTO.getCrProcessParentId() != null) {
        crProcessParentId = srMappingProcessCRDTO.getCrProcessParentId().toString();
      }
      if (StringUtils.isNotNullOrEmpty(srMappingProcessCRDTO.getProcessTypeLv3Id())) {
        crProcessId = srMappingProcessCRDTO.getProcessTypeLv3Id();
      }
      if (srMappingProcessCRDTO.getIsCrNodes() != null
          && srMappingProcessCRDTO.getIsCrNodes() == 1L) {
        isCrNodes = true;
      }
    }
    String changeResponsibleUnitCR = "";
    if (srCreateAutoCRDTO.getUnitImplement() != null) {
      changeResponsibleUnitCR = srCreateAutoCRDTO.getUnitImplement().toString();
    } else {
      changeResponsibleUnitCR =
          srInsiteDTO.getSrUnit() != null ? srInsiteDTO.getSrUnit().toString() : null;
    }
    List<SRFilesDTO> lstFileResult = new ArrayList<>();
    String crNumberResult = "";
    UserToken userToken = ticketProvider.getUserToken();
    if (isCrNodes) {
      //<editor-fold desc="Dung edit  trường hợp 1 Cr sinh 1 note tương ứng">
      mapFileWo.clear();
      // lấy danh sách Ip và file trong file đánh dấu là file mapping
      List<SRFilesDTO> lstFileWo = resultFileMapping(gnocFileWoList);
      if (lstFileWo != null && !lstFileWo.isEmpty()) {
        for (SRFilesDTO srFilesDTO : lstFileWo) {
          try {
            CrDTO crDTO = new CrDTO();
            CrCreatedFromOtherSysDTO ccfosdto = new CrCreatedFromOtherSysDTO();
            ccfosdto.setSystemId(crSystemId);
            ccfosdto.setObjectId(srInsiteDTO.getSrId().toString());
            ccfosdto.setStepId(null);
            ResultInSideDto crNumber = crServiceProxy
                .getCrNumber(crProcessParentId);
            if (!crNumber.getKey().equals(RESULT.SUCCESS)) {
              return resultInSideDto;
            }
            String crNumberArr[] = crNumber.getIdValue().split("_");
            crDTO.setCrId(crNumberArr[crNumberArr.length - 1].trim());
            crDTO.setCrNumber(crNumber.getIdValue());
            SRMappingProcessCRDTO dutyTypeSeach = new SRMappingProcessCRDTO();
            dutyTypeSeach.setCrProcessParentId(srMappingProcessCRDTO.getCrProcessParentId());
            dutyTypeSeach.setProxyLocale(I18n.getLocale());
            List<ItemDataCRInside> lstDutyType = srCategoryServiceProxy
                .getDutyTypeByProcessIdProxy(srMappingProcessCRDTO);
            if (lstDutyType != null && !lstDutyType.isEmpty()
                && lstDutyType.get(0).getValueStr() != null) {
              crDTO.setDutyType(lstDutyType.get(0).getValueStr().toString());
            } else {
              if (srMappingProcessCRDTO.getDutyType() != null) {
                crDTO.setDutyType(srMappingProcessCRDTO.getDutyType().toString());
              }
            }
            CrProcessInsideDTO crProcessDTO = new CrProcessInsideDTO();
            crProcessDTO.setCrProcessId(srMappingProcessCRDTO.getCrProcessParentId());
            List<CrProcessInsideDTO> listCrProcess = crCategoryServiceProxy
                .getListCrProcessDTO(crProcessDTO);
            if (listCrProcess != null && !listCrProcess.isEmpty()) {
              // neu trang thai cr la khẩn thì k cho sinh CR
              if (Constants.CR_TYPE.EMERGENCY.toString()
                  .equals(String.valueOf(listCrProcess.get(0).getCrTypeId()))) {
                resultInSideDto.setMessage(I18n.getValidation("srAuto.create.cr.fail"));
                return resultInSideDto;
              }
              crDTO.setCrType(
                  listCrProcess.get(0).getCrTypeId() != null ? listCrProcess.get(0).getCrTypeId()
                      .toString() : null);
            }

            List<WoDTO> lstWo = new ArrayList<>();

            if (srCreateAutoCRDTO != null) {
              if (srCreateAutoCRDTO.getServiceAffecting() != null && 1L == srCreateAutoCRDTO
                  .getServiceAffecting()) {
                String[] arrAffecting = srCreateAutoCRDTO.getAffectingService().split(",");
                List<CrAffectedServiceDetailsDTO> lstAff = new ArrayList<>();
                for (String strAff : arrAffecting) {
                  CrAffectedServiceDetailsDTO crAffDTO = new CrAffectedServiceDetailsDTO();
                  crAffDTO.setAffectedServiceId(strAff);
                  crAffDTO.setCrId(crDTO.getCrId());
                  lstAff.add(crAffDTO);
                }
                crDTO.setLstAffectedService(lstAff);
              }
              crDTO.setEarliestStartTime(srCreateAutoCRDTO.getExecutionTime() == null ? null
                  : DateTimeUtils
                      .convertDateToString(srCreateAutoCRDTO.getExecutionTime(),
                          "dd/MM/yyyy HH:mm:ss"));
              crDTO.setLatestStartTime(srCreateAutoCRDTO.getExecutionEndTime() == null ? null
                  : DateTimeUtils.convertDateToString(srCreateAutoCRDTO.getExecutionEndTime(),
                      "dd/MM/yyyy HH:mm:ss"));
              // dich vu anh huong trong bang sr_mapping
              crDTO.setServiceAffecting(
                  srCreateAutoCRDTO.getServiceAffecting() != null ? srCreateAutoCRDTO
                      .getServiceAffecting().toString() : null);
              crDTO
                  .setTotalAffectedCustomers(
                      srCreateAutoCRDTO.getTotalAffectingCustomers() == null ? "0"
                          : srCreateAutoCRDTO.getTotalAffectingCustomers().toString());
              crDTO
                  .setTotalAffectedMinutes(
                      srCreateAutoCRDTO.getTotalAffectingMinutes() == null ? "0"
                          : srCreateAutoCRDTO.getTotalAffectingMinutes().toString());

              WoInsideDTO woDTO1 = new WoInsideDTO();
              WoInsideDTO woDTO2 = new WoInsideDTO();

              //create date WO
              woDTO1.setCreateDate(new Date(System.currentTimeMillis()));
              woDTO2.setCreateDate(new Date(System.currentTimeMillis()));
              woDTO1.setWoTypeId(srCreateAutoCRDTO.getWoTestTypeId());
              // thêm IP node mạng vào woContent
              woDTO1.setWoContent(
                  srCreateAutoCRDTO.getWoContentService() + "_" + srFilesDTO.getIpMop());
              woDTO2.setWoTypeId(srCreateAutoCRDTO.getWoFtTypeId());
              woDTO2.setWoContent(srCreateAutoCRDTO.getWoContentCDFT());
              woDTO1.setStartTime(srCreateAutoCRDTO.getWoTestStartTime());
              woDTO1.setEndTime(srCreateAutoCRDTO.getWoTestEndTime());
              woDTO1.setCdId(srCreateAutoCRDTO.getGroupCdFtService());
              woDTO1.setWoSystemId(crNumber.getIdValue());
              woDTO1.setCreatePersonId(userExecute.getUserId());
              woDTO1.setWoDescription(srCreateAutoCRDTO.getWoTestDescription());
              woDTO1.setPriorityId(srCreateAutoCRDTO.getWoTestPriority());
              woDTO1.setWoSystem("CR");
              List<String> lstName = new ArrayList<>();
              List<byte[]> lstByte = new ArrayList<>();
              if (srFilesDTO.getFileName() != null) {
                try {
                  GnocFileDto gnocFileWoTest = mapFileWo.get(srFilesDTO.getFileName());
                  byte[] bFile = FileUtils
                      .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                          PassTranformer.decrypt(ftpPass), gnocFileWoTest.getPath());
                  lstName.add(gnocFileWoTest.getFileName());
                  lstByte.add(bFile);
                  woDTO1.setFileArr(lstByte);
                  woDTO1.setListFileName(lstName);
                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                }
              }
              woDTO2.setStartTime(srCreateAutoCRDTO.getWoFtStartTime());
              woDTO2.setEndTime(srCreateAutoCRDTO.getWoFtEndTime());
              woDTO2.setCdId(srCreateAutoCRDTO.getGroupCDFT());
              woDTO2.setWoSystemId(crNumber.getIdValue());
              woDTO2.setCreatePersonId(userExecute.getUserId());
              woDTO2.setWoDescription(srCreateAutoCRDTO.getWoFtDescription());
              woDTO2
                  .setPriorityId(srCreateAutoCRDTO.getWoFtPriority());
              woDTO2.setWoSystem("CR");
              if (srCreateAutoCRDTO.getWoTestTypeId() != null) {
                lstWo.add(woDTO1.toModelOutSide());
              }
              if (srCreateAutoCRDTO.getWoFtTypeId() != null) {
                lstWo.add(woDTO2.toModelOutSide());
              }
            }
            crDTO.setCrCreatedFromOtherSysDTO(ccfosdto);

            crDTO.setTitle(srCreateAutoCRDTO.getCrTitle() + "_" + srFilesDTO.getIpMop());

            crDTO.setDescription(srCreateAutoCRDTO.getDescriptionCr());

            List<SRConfigDTO> lsConfig = srConfigRepository
                .getCrInforByParentGroup(Constants.SR_CATALOG.PARENT_GROUP_AUTO_CREATE_CR);
            Map<String, String> mapConfig = new HashMap<>();
            for (SRConfigDTO item : lsConfig) {
              mapConfig.put(item.getConfigGroup(), item.getConfigCode());
            }

            crDTO.setSubcategoryId(mapConfig.get(Constants.SR_CATALOG.AUTO_CR_SUBCATEGORY));

            //mức ưu tiên
            crDTO.setPriority(mapConfig.get(Constants.SR_CATALOG.AUTO_CR_PRIORITY));

            //mảng tác động
            crDTO.setImpactAffect(mapConfig.get(Constants.SR_CATALOG.AUTO_CR_IMPACT_AFFECT));
            // trạng thái
            crDTO.setState(String.valueOf(srCreateAutoCRDTO.getCrStatus()));
            //bo sung load  alarm
            crDTO.setIsClickedToAlarmTag(1);
            // cr_process_id
            crDTO.setCrProcessId(crProcessParentId);
            crDTO.setChangeOrginator(srInsiteDTO.getSrUser());
            crDTO.setChangeResponsibleUnit(changeResponsibleUnitCR);
            SrInsiteDTO location = srRepository.findNationByLocationId(unitExecute.getLocationId());
            if (location.getCountry() != null && !"".equals(location.getCountry())) {
              String arrLoction[] = location.getCountry().split("/");

              CatLocationDTO locationDTO = catLocationRepository
                  .getNationByLocationId(Long.valueOf(arrLoction[1].trim()));
              crDTO.setCountry(locationDTO.getLocationCode());
              if (arrLoction.length >= 3) {
                crDTO.setRegion(
                    catLocationRepository.getNationByLocationId(Long.valueOf(arrLoction[2].trim()))
                        .getLocationCode());
              } else {
                //neu k lay dc khu vuc thi set Region la KV1
                crDTO.setRegion("KV1");
              }
            }
            //get system
            SRConfigDTO sRConfigDTO = new SRConfigDTO();
            sRConfigDTO.setConfigCode(srInsiteDTO.getServiceArray());
            sRConfigDTO.setConfigGroup(Constants.SR_CATALOG.SERVICE_ARRAY);
            List<SRConfigDTO> lstData = srConfigRepository.getDataByConfigCode(sRConfigDTO);

            //get nationCode
            SRConfigDTO sRDTO = new SRConfigDTO();
            sRDTO.setConfigCode(srInsiteDTO.getCountry());
            sRDTO.setConfigGroup(Constants.SR_CONFIG.MAP_COUNTRY);
            List<SRConfigDTO> lstDataCountry = srConfigRepository.getDataByConfigCode(sRDTO);

            //get List mop
            SRMopDTO dto = new SRMopDTO();
            dto.setSrId(srInsiteDTO.getSrId().toString());
            List<SRMopDTO> listMOP = srMopRepository.getListSRMopDTO(dto);
            if (srInsiteDTO.getLstMopTmp() != null && !srInsiteDTO.getLstMopTmp().isEmpty()) {
              for (SRMopDTO mopDTO : srInsiteDTO.getLstMopTmp()) {
                if (mopDTO.getId() == null) {
                  listMOP.add(mopDTO);
                }
              }
            }
            List<String> lstMop = new ArrayList<>();
            List<String> lstNodeIp = new ArrayList<>();
            if (lstData == null) {
              resultInSideDto.setMessage(I18n.getValidation("common.error"));
              return resultInSideDto;
            }
            String processTypeLv3Id = "";
            if (listMOP != null && !listMOP.isEmpty()) {
              for (SRMopDTO srmopdto : listMOP) {
                if (Constants.SR_CONFIG.AAM.equals(lstData.get(0).getAutomation()) && !StringUtils
                    .isStringNullOrEmpty(srmopdto.getDtId()) && srmopdto.getIpNode()
                    .trim().equals(srFilesDTO.getIpMop())) {
                  srmopdto.setDtId("MOP_HT_" + srmopdto.getDtId());
                  lstMop.add(srmopdto.getDtId());
                  lstNodeIp.add(srFilesDTO.getIpMop());
                } else if (StringUtils.isNotNullOrEmpty(srmopdto.getIpNode()) && srmopdto
                    .getIpNode().trim().equals(srFilesDTO.getIpMop())) {
                  lstMop.add(srmopdto.getDtId());
                  lstNodeIp.add(srFilesDTO.getIpMop());
                }
                if ((SR_CONFIG.VIPA.equals(lstData.get(0).getAutomation()) || SR_CONFIG.VMSA
                    .equals(lstData.get(0).getAutomation())) && StringUtils
                    .isNotNullOrEmpty(srmopdto.getProcessId()) && !processTypeLv3Id
                    .contains(srmopdto.getProcessId())) {
                  processTypeLv3Id += srmopdto.getProcessId() + ",";
                }
              }
            }
            if (StringUtils.isNotNullOrEmpty(crProcessId)) {
              if (StringUtils.isNotNullOrEmpty(processTypeLv3Id)) {
                if (processTypeLv3Id.endsWith(",")) {
                  processTypeLv3Id = processTypeLv3Id.substring(0, processTypeLv3Id.length() - 1);
                }
                crDTO.setProcessTypeLv3Id(processTypeLv3Id);
              } else {
                crDTO.setProcessTypeLv3Id(crProcessId);
              }
            }
            String mess = "";
            if (srCreateAutoCRDTO.getPathFileProcess() != null
                && srCreateAutoCRDTO.getFileTypeId() != null) {
              mapFileProcess(srCreateAutoCRDTO.getId(),
                  gnocFilePathProceeMap);
              String[] arrPath = srCreateAutoCRDTO.getPathFileProcess().split(";");
              String[] arrTypeId = srCreateAutoCRDTO.getFileTypeId().split(";");
              List<CrFilesAttachInsiteDTO> lstCrFilesAttach = new ArrayList<>();
              for (int i = 0; i < arrPath.length; i++) {
                if (gnocFilePathProceeMap.get(arrTypeId[i]) != null) {
                  GnocFileDto dtoFileProcess = gnocFilePathProceeMap.get(arrTypeId[i]);
                  CrFilesAttachInsiteDTO crFilesAttachDTO = new CrFilesAttachInsiteDTO();
                  crFilesAttachDTO.setFileName(FileUtils.getFileName(dtoFileProcess.getPath()));
                  crFilesAttachDTO.setFilePathFtp(dtoFileProcess.getPath());
                  crFilesAttachDTO.setFilePath(arrPath[i]);
                  crFilesAttachDTO.setFileId(dtoFileProcess.getTemplateId());
                  crFilesAttachDTO.setFileType(dtoFileProcess.getFileType());
                  crFilesAttachDTO.setTempImportId(dtoFileProcess.getTemplateId());
                  crFilesAttachDTO.setTimeAttack(srCreateAutoCRDTO.getTimeAttach());
                  crFilesAttachDTO.setUserId(userToken.getUserID());
                  crFilesAttachDTO.setCrId(Long.parseLong(crDTO.getCrId()));
                  lstCrFilesAttach.add(crFilesAttachDTO);
                }
              }

              // dung đang làm
              SRDTO srFileUpLoad = new SRDTO();
              srFileUpLoad.setLstUpload(lstCrFilesAttach);
              srFileUpLoad.setUsername(userToken.getUserName());
              UsersInsideDto usersInsideDto = userRepository
                  .getUserDTOByUserName(userToken.getUserName());
              UnitDTO unitToken = unitRepository.findUnitById(usersInsideDto.getUnitId());
              srFileUpLoad.setUnitToken(unitToken);
              srFileUpLoad.setLocale(I18n.getLocale());
              ObjectMapper mapper = new ObjectMapper();
              String dataFileProcess = mapper.writeValueAsString(srFileUpLoad);
              StringUtils.printLogData("insertListNoIDForImport", srFileUpLoad, SRDTO.class);
              List<TemplateImportDTO> lstTemplateImport = crServiceProxy
                  .insertListNoIDForImportForSR(dataFileProcess);
              List<GnocFileDto> lstFileOutPut = new ArrayList<>();
              if (lstTemplateImport != null && !lstTemplateImport.isEmpty()) {
                int size = lstTemplateImport.size();
                for (int i = size - 1; i > -1; i--) {
                  if (!StringUtils.isStringNullOrEmpty(lstTemplateImport.get(i).getResult())
                      && "SUCCESS"
                      .equals(lstTemplateImport.get(i).getResult())) {
                    lstTemplateImport.remove(i);
                  }
                }
                for (int i = 0; i < lstTemplateImport.size(); i++) {
                  TemplateImportDTO templateDto = lstTemplateImport.get(i);
                  if ("NOK".equals(templateDto.getResult())) {
                    isCreateCRWO = false;
                  }
                  try {
                    if (!StringUtils.isStringNullOrEmpty(templateDto.getLinkOutput())) {
                      String fullPath = FileUtils
                          .saveTempFile("ImportResult_" + templateDto.getFileName(),
                              templateDto.getBytesFileOut(),
                              tempFolder);
                      GnocFileDto fileDto = new GnocFileDto();
                      fileDto.setPath(fullPath);
                      fileDto.setFileName(FileUtils.getFileName(fullPath));
                      fileDto.setTemplateId(Long.parseLong(templateDto.getTempImportId()));
                      lstFileOutPut.add(fileDto);
                    }
                  } catch (Exception e) {
                    log.error(e.getMessage(), e);
                  }
                  mess += templateDto.getMessage() + " " + templateDto.getFileName() + ";";
                }
              }
              if (!StringUtils.isStringNullOrEmpty(mess) && mess.endsWith(";")) {
                mess = mess.substring(0, mess.length() - 1);
              }
              if (lstFileOutPut != null && !lstFileOutPut.isEmpty()) {
                resultInSideDto.setLstResult(lstFileOutPut);
              }
            }

            ResultDTO result = new ResultDTO();
            if (isCreateCRWO) {
              InsertAutoCrForSrDTO insertAutoCrForSrDTO = new InsertAutoCrForSrDTO(crDTO, null,
                  lstData.get(0).getAutomation(), lstDataCountry.get(0).getConfigName(), lstWo,
                  lstMop, lstNodeIp, I18n.getLocale());
              result = crServiceProxy.insertAutoCrForSR(insertAutoCrForSrDTO);
              StringUtils.printLogData("insertAutoCrForSR", insertAutoCrForSrDTO,
                  InsertAutoCrForSrDTO.class);
            } else {
              resultInSideDto.setKey(RESULT.FAIL);
              resultInSideDto.setMessage(I18n.getValidation("sr.autoCreate.fileProcess.fail") + " "
                  + mess);
              return resultInSideDto;
            }
            if (RESULT.SUCCESS.equals(result.getKey())) {
              resultInSideDto.setKey(RESULT.SUCCESS);
              resultInSideDto.setMessage("OK" + ";" + crDTO.getCrNumber());
              success = true;
              crNumberResult += crDTO.getCrNumber() + ";";
              resultInSideDto.setReturnCode(crDTO.getCrNumber());
              SRFilesDTO fileResult = new SRFilesDTO();
              fileResult.setFileName(srFilesDTO.getFileName());
              fileResult.setIpMop(srFilesDTO.getIpMop());
              fileResult.setComments("OK");
              lstFileResult.add(fileResult);
            } else {
              SRFilesDTO fileResult = new SRFilesDTO();
              fileResult.setFileName(srFilesDTO.getFileName());
              fileResult.setIpMop(srFilesDTO.getIpMop());
              fileResult.setComments("NOK : " + result.getMessage());
              lstFileResult.add(fileResult);
              resultInSideDto.setKey(RESULT.FAIL);
              resultInSideDto.setMessage(
                  I18n.getValidation("sr.autoCreate.fail") + ": " + result != null ? result
                      .getMessage() : "");
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultInSideDto.setMessage(e.getMessage());
          }
        }
      }
      //</editor-fold>
    } else {
      //<editor-fold desc="Dung edit  trường hợp 1 Cr có nhiều node mạng">
      try {
        if (gnocFileWoList != null && gnocFileWoList.size() > 1) {
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto.setMessage(I18n.getValidation("sr.CR.WO.erro"));
          return resultInSideDto;
        }
        CrDTO crDTO = new CrDTO();
        CrCreatedFromOtherSysDTO ccfosdto = new CrCreatedFromOtherSysDTO();
        ccfosdto.setSystemId(crSystemId);
        ccfosdto.setObjectId(srInsiteDTO.getSrId().toString());
        ccfosdto.setStepId(null);
        ResultInSideDto crNumber = crServiceProxy
            .getCrNumber(crProcessParentId);
        if (!crNumber.getKey().equals(RESULT.SUCCESS)) {
          return resultInSideDto;
        }
        String crNumberArr[] = crNumber.getIdValue().split("_");
        crDTO.setCrId(crNumberArr[crNumberArr.length - 1].trim());
        crDTO.setCrNumber(crNumber.getIdValue());
        SRMappingProcessCRDTO dutyTypeSeach = new SRMappingProcessCRDTO();
        dutyTypeSeach.setCrProcessParentId(srMappingProcessCRDTO.getCrProcessParentId());
        dutyTypeSeach.setProxyLocale(I18n.getLocale());
        List<ItemDataCRInside> lstDutyType = srCategoryServiceProxy
            .getDutyTypeByProcessIdProxy(srMappingProcessCRDTO);
        if (lstDutyType != null && !lstDutyType.isEmpty()
            && lstDutyType.get(0).getValueStr() != null) {
          crDTO.setDutyType(lstDutyType.get(0).getValueStr().toString());
        } else {
          if (srMappingProcessCRDTO.getDutyType() != null) {
            crDTO.setDutyType(srMappingProcessCRDTO.getDutyType().toString());
          }
        }
        CrProcessInsideDTO crProcessDTO = new CrProcessInsideDTO();
        crProcessDTO.setCrProcessId(srMappingProcessCRDTO.getCrProcessParentId());
        List<CrProcessInsideDTO> listCrProcess = crCategoryServiceProxy
            .getListCrProcessDTO(crProcessDTO);
        if (listCrProcess != null && !listCrProcess.isEmpty()) {
          // neu trang thai cr la khẩn thì k cho sinh CR
          if (Constants.CR_TYPE.EMERGENCY.toString()
              .equals(String.valueOf(listCrProcess.get(0).getCrTypeId()))) {
            resultInSideDto.setMessage(I18n.getValidation("srAuto.create.cr.fail"));
            return resultInSideDto;
          }
          crDTO.setCrType(
              listCrProcess.get(0).getCrTypeId() != null ? listCrProcess.get(0).getCrTypeId()
                  .toString() : null);
        }

        List<WoDTO> lstWo = new ArrayList<>();

        if (srCreateAutoCRDTO != null) {
          if (srCreateAutoCRDTO.getServiceAffecting() != null && 1L == srCreateAutoCRDTO
              .getServiceAffecting()) {
            String[] arrAffecting = srCreateAutoCRDTO.getAffectingService().split(",");
            List<CrAffectedServiceDetailsDTO> lstAff = new ArrayList<>();
            for (String strAff : arrAffecting) {
              CrAffectedServiceDetailsDTO crAffDTO = new CrAffectedServiceDetailsDTO();
              crAffDTO.setAffectedServiceId(strAff);
              crAffDTO.setCrId(crDTO.getCrId());
              lstAff.add(crAffDTO);
            }
            crDTO.setLstAffectedService(lstAff);
          }
          crDTO.setEarliestStartTime(srCreateAutoCRDTO.getExecutionTime() == null ? null
              : DateTimeUtils
                  .convertDateToString(srCreateAutoCRDTO.getExecutionTime(),
                      "dd/MM/yyyy HH:mm:ss"));
          crDTO.setLatestStartTime(srCreateAutoCRDTO.getExecutionEndTime() == null ? null
              : DateTimeUtils.convertDateToString(srCreateAutoCRDTO.getExecutionEndTime(),
                  "dd/MM/yyyy HH:mm:ss"));
          // dich vu anh huong trong bang sr_mapping
          crDTO.setServiceAffecting(
              srCreateAutoCRDTO.getServiceAffecting() != null ? srCreateAutoCRDTO
                  .getServiceAffecting()
                  .toString() : null);
          crDTO
              .setTotalAffectedCustomers(
                  srCreateAutoCRDTO.getTotalAffectingCustomers() == null ? "0"
                      : srCreateAutoCRDTO.getTotalAffectingCustomers().toString());
          crDTO
              .setTotalAffectedMinutes(srCreateAutoCRDTO.getTotalAffectingMinutes() == null ? "0"
                  : srCreateAutoCRDTO.getTotalAffectingMinutes().toString());
          WoInsideDTO woDTO1 = new WoInsideDTO();
          if (srCreateAutoCRDTO.getWoTestTypeId() != null) {
            woDTO1.setCreateDate(new Date(System.currentTimeMillis()));
            woDTO1.setWoTypeId(srCreateAutoCRDTO.getWoTestTypeId());
            woDTO1.setWoContent(srCreateAutoCRDTO.getWoContentService());
            woDTO1.setStartTime(srCreateAutoCRDTO.getWoTestStartTime());
            woDTO1.setEndTime(srCreateAutoCRDTO.getWoTestEndTime());
            woDTO1.setCdId(srCreateAutoCRDTO.getGroupCdFtService());
            woDTO1.setWoSystemId(crNumber.getIdValue());
            woDTO1.setCreatePersonId(userExecute.getUserId());
            woDTO1.setWoDescription(srCreateAutoCRDTO.getWoTestDescription());
            woDTO1.setPriorityId(srCreateAutoCRDTO.getWoTestPriority());
            woDTO1.setWoSystem("CR");
            List<String> lstName = new ArrayList<>();
            List<byte[]> lstByte = new ArrayList<>();
            try {
              GnocFileDto gnocFile = gnocFileWoList.get(0);
              byte[] bFile = FileUtils
                  .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                      PassTranformer.decrypt(ftpPass), gnocFile.getPath());
              lstName.add(gnocFile.getFileName());
              lstByte.add(bFile);
              woDTO1.setFileArr(lstByte);
              woDTO1.setListFileName(lstName);
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
            lstWo.add(woDTO1.toModelOutSide());
          }

          WoInsideDTO woDTO2 = new WoInsideDTO();
          woDTO2.setCreateDate(new Date(System.currentTimeMillis()));
          woDTO2.setWoTypeId(srCreateAutoCRDTO.getWoFtTypeId());
          woDTO2.setWoContent(srCreateAutoCRDTO.getWoContentCDFT());
          woDTO2.setStartTime(srCreateAutoCRDTO.getWoFtStartTime());
          woDTO2.setEndTime(srCreateAutoCRDTO.getWoFtEndTime());
          woDTO2.setCdId(srCreateAutoCRDTO.getGroupCDFT());
          woDTO2.setWoSystemId(crNumber.getIdValue());
          woDTO2.setCreatePersonId(userExecute.getUserId());
          woDTO2.setWoDescription(srCreateAutoCRDTO.getWoFtDescription());
          woDTO2.setPriorityId(srCreateAutoCRDTO.getWoFtPriority());
          woDTO2.setWoSystem("CR");
          if (srCreateAutoCRDTO.getWoFtTypeId() != null) {
            lstWo.add(woDTO2.toModelOutSide());
          }
        }
        crDTO.setCrCreatedFromOtherSysDTO(ccfosdto);
        List<SRConfigDTO> lsConfig = srConfigRepository
            .getCrInforByParentGroup(Constants.SR_CATALOG.PARENT_GROUP_AUTO_CREATE_CR);
        Map<String, String> mapConfig = new HashMap<>();
        for (SRConfigDTO item : lsConfig) {
          mapConfig.put(item.getConfigGroup(), item.getConfigCode());
        }
        crDTO.setTitle(srCreateAutoCRDTO.getCrTitle());

        crDTO.setDescription(srCreateAutoCRDTO.getDescriptionCr());

        crDTO.setSubcategoryId(mapConfig.get(Constants.SR_CATALOG.AUTO_CR_SUBCATEGORY));

        //mức ưu tiên
        crDTO.setPriority(mapConfig.get(Constants.SR_CATALOG.AUTO_CR_PRIORITY));

        //mảng tác động
        crDTO.setImpactAffect(mapConfig.get(Constants.SR_CATALOG.AUTO_CR_IMPACT_AFFECT));
        // trạng thái
        crDTO.setState(String.valueOf(srCreateAutoCRDTO.getCrStatus()));
        //bo sung load  alarm
        crDTO.setIsClickedToAlarmTag(1);
        // cr_process_id
        crDTO.setCrProcessId(crProcessParentId);
        crDTO.setChangeOrginator(srInsiteDTO.getSrUser());
        crDTO.setChangeResponsibleUnit(changeResponsibleUnitCR);
        SrInsiteDTO location = srRepository.findNationByLocationId(unitExecute.getLocationId());
        if (location.getCountry() != null && !"".equals(location.getCountry())) {
          String arrLoction[] = location.getCountry().split("/");

          CatLocationDTO locationDTO = catLocationRepository
              .getNationByLocationId(Long.valueOf(arrLoction[1].trim()));
          crDTO.setCountry(locationDTO.getLocationCode());
          if (arrLoction.length >= 3) {
            crDTO.setRegion(
                catLocationRepository.getNationByLocationId(Long.valueOf(arrLoction[2].trim()))
                    .getLocationCode());
          } else {
            //neu k lay dc khu vuc thi set Region la KV1
            crDTO.setRegion("KV1");
          }
        }
        //get system
        SRConfigDTO sRConfigDTO = new SRConfigDTO();
        sRConfigDTO.setConfigCode(srInsiteDTO.getServiceArray());
        sRConfigDTO.setConfigGroup(Constants.SR_CATALOG.SERVICE_ARRAY);
        List<SRConfigDTO> lstData = srConfigRepository.getDataByConfigCode(sRConfigDTO);

        //get nationCode
        SRConfigDTO sRDTO = new SRConfigDTO();
        sRDTO.setConfigCode(srInsiteDTO.getCountry());
        sRDTO.setConfigGroup(Constants.SR_CONFIG.MAP_COUNTRY);
        List<SRConfigDTO> lstDataCountry = srConfigRepository.getDataByConfigCode(sRDTO);

        //get List mop
        SRMopDTO dto = new SRMopDTO();
        dto.setSrId(srInsiteDTO.getSrId().toString());
        List<SRMopDTO> listMOP = srMopRepository.getListSRMopDTO(dto);
        if (srInsiteDTO.getLstMopTmp() != null && !srInsiteDTO.getLstMopTmp().isEmpty()) {
          for (SRMopDTO mopDTO : srInsiteDTO.getLstMopTmp()) {
            if (mopDTO.getId() == null) {
              listMOP.add(mopDTO);
            }
          }
        }
        if (lstData == null) {
          resultInSideDto.setMessage(I18n.getValidation("common.error"));
          return resultInSideDto;
        }
        String processTypeLv3Id = "";
        if (listMOP != null && !listMOP.isEmpty()) {
          for (SRMopDTO srmopdto : listMOP) {
            if (Constants.SR_CONFIG.AAM.equals(lstData.get(0).getAutomation())) {
              if (!StringUtils.isStringNullOrEmpty(srmopdto.getDtId())) {
                srmopdto.setDtId("MOP_HT_" + srmopdto.getDtId());
              }
            }
            if ((SR_CONFIG.VIPA.equals(lstData.get(0).getAutomation()) || SR_CONFIG.VMSA
                .equals(lstData.get(0).getAutomation())) && StringUtils
                .isNotNullOrEmpty(srmopdto.getProcessId()) && !processTypeLv3Id
                .contains(srmopdto.getProcessId())) {
              processTypeLv3Id += srmopdto.getProcessId() + ",";
            }
          }
        }
        if (StringUtils.isNotNullOrEmpty(crProcessId)) {
          if (StringUtils.isNotNullOrEmpty(processTypeLv3Id)) {
            if (processTypeLv3Id.endsWith(",")) {
              processTypeLv3Id = processTypeLv3Id.substring(0, processTypeLv3Id.length() - 1);
            }
            crDTO.setProcessTypeLv3Id(processTypeLv3Id);
          } else {
            crDTO.setProcessTypeLv3Id(crProcessId);
          }
        }
        List<String> lstMop = listMOP.stream()
            .map(SRMopDTO::getDtId).distinct().collect(Collectors.toList());

        String mess = "";
        if (srCreateAutoCRDTO.getPathFileProcess() != null
            && srCreateAutoCRDTO.getFileTypeId() != null) {
          mapFileProcess(srCreateAutoCRDTO.getId(), gnocFilePathProceeMap);
          String[] arrPath = srCreateAutoCRDTO.getPathFileProcess().split(";");
          String[] arrTypeId = srCreateAutoCRDTO.getFileTypeId().split(";");
          List<CrFilesAttachInsiteDTO> lstCrFilesAttach = new ArrayList<>();
          for (int i = 0; i < arrPath.length; i++) {
            if (gnocFilePathProceeMap.get(arrTypeId[i]) != null) {
              GnocFileDto dtoFileProcess = gnocFilePathProceeMap.get(arrTypeId[i]);
              CrFilesAttachInsiteDTO crFilesAttachDTO = new CrFilesAttachInsiteDTO();
              crFilesAttachDTO.setFileName(FileUtils.getFileName(dtoFileProcess.getPath()));
              crFilesAttachDTO.setFilePath(arrPath[i]);
              crFilesAttachDTO.setFilePathFtp(dtoFileProcess.getPath());
              crFilesAttachDTO.setFileId(dtoFileProcess.getTemplateId());
              crFilesAttachDTO.setFileType(dtoFileProcess.getFileType());
              crFilesAttachDTO.setTempImportId(dtoFileProcess.getTemplateId());
              crFilesAttachDTO.setTimeAttack(srCreateAutoCRDTO.getTimeAttach());
              crFilesAttachDTO.setUserId(userToken.getUserID());
              crFilesAttachDTO.setCrId(Long.parseLong(crDTO.getCrId()));
              lstCrFilesAttach.add(crFilesAttachDTO);
            }
          }

          // dung đang làm
          SRDTO srFileUpLoad = new SRDTO();
          srFileUpLoad.setLstUpload(lstCrFilesAttach);
          srFileUpLoad.setUsername(userToken.getUserName());
          UsersInsideDto usersInsideDto = userRepository
              .getUserDTOByUserName(userToken.getUserName());
          UnitDTO unitToken = unitRepository.findUnitById(usersInsideDto.getUnitId());
          srFileUpLoad.setUnitToken(unitToken);
          srFileUpLoad.setLocale(I18n.getLocale());
          ObjectMapper mapper = new ObjectMapper();
          String dataFileProcess = mapper.writeValueAsString(srFileUpLoad);
          StringUtils.printLogData("insertListNoIDForImport", srFileUpLoad, SRDTO.class);
          List<TemplateImportDTO> lstTemplateImport = crServiceProxy
              .insertListNoIDForImportForSR(dataFileProcess);
          List<GnocFileDto> lstFileOutPut = new ArrayList<>();
          if (lstTemplateImport != null && !lstTemplateImport.isEmpty()) {
            int size = lstTemplateImport.size();
            for (int i = size - 1; i > -1; i--) {
              if (!StringUtils.isStringNullOrEmpty(lstTemplateImport.get(i).getResult())
                  && "SUCCESS"
                  .equals(lstTemplateImport.get(i).getResult())) {
                lstTemplateImport.remove(i);
              }
            }
            for (int i = 0; i < lstTemplateImport.size(); i++) {
              TemplateImportDTO templateDto = lstTemplateImport.get(i);
              if ("NOK".equals(templateDto.getResult())) {
                isCreateCRWO = false;
              }
              try {
                if (!StringUtils.isStringNullOrEmpty(templateDto.getLinkOutput())) {
                  String fullPath = FileUtils
                      .saveTempFile("ImportResult_" + templateDto.getFileName(),
                          templateDto.getBytesFileOut(),
                          tempFolder);
                  GnocFileDto fileDto = new GnocFileDto();
                  fileDto.setPath(fullPath);
                  fileDto.setFileName(FileUtils.getFileName(fullPath));
                  fileDto.setTemplateId(Long.parseLong(templateDto.getTempImportId()));
                  lstFileOutPut.add(fileDto);
                }
              } catch (Exception e) {
                log.error(e.getMessage(), e);
              }
              mess += templateDto.getMessage() + " " + templateDto.getFileName() + ";";
            }
          }
          if (!StringUtils.isStringNullOrEmpty(mess) && mess.endsWith(";")) {
            mess = mess.substring(0, mess.length() - 1);
          }
          if (lstFileOutPut != null && !lstFileOutPut.isEmpty()) {
            resultInSideDto.setLstResult(lstFileOutPut);
          }
        }

        ResultDTO result = new ResultDTO();
        if (isCreateCRWO) {
          InsertAutoCrForSrDTO insertAutoCrForSrDTO = new InsertAutoCrForSrDTO(crDTO, null,
              lstData.get(0).getAutomation(), lstDataCountry.get(0).getConfigName(), lstWo,
              lstMop, null, I18n.getLocale());
          result = crServiceProxy.insertAutoCrForSR(insertAutoCrForSrDTO);
          StringUtils
              .printLogData("insertAutoCrForSR", insertAutoCrForSrDTO, InsertAutoCrForSrDTO.class);
        } else {
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto
              .setMessage(I18n.getValidation("sr.autoCreate.fileProcess.fail") + " "
                  + mess);
          return resultInSideDto;
        }
        if (RESULT.SUCCESS.equals(result.getKey())) {
          resultInSideDto.setKey(RESULT.SUCCESS);
          resultInSideDto.setMessage("OK" + ";" + crDTO.getCrNumber());
          success = true;
          crNumberResult += crDTO.getCrNumber() + ";";
        } else {
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto.setMessage(
              I18n.getValidation("sr.autoCreate.fail") + ": " + result != null ? result.getMessage()
                  : "");
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        resultInSideDto.setMessage(e.getMessage());
      }
      //</editor-fold>
    }
    if (lstFileResult != null && !lstFileResult.isEmpty()) {
      String filePath = exportResultAutoCreateCr(lstFileResult);
      List<GnocFileDto> lstFileOutPut = new ArrayList<>();
      GnocFileDto fileResult = new GnocFileDto();
      fileResult.setPath(filePath);
      lstFileOutPut.add(fileResult);
      resultInSideDto.setLstResult(lstFileOutPut);
    }
    if (success) {
      resultInSideDto.setKey(RESULT.SUCCESS);
      if (StringUtils.isNotNullOrEmpty(crNumberResult)) {
        if (crNumberResult.endsWith(";")) {
          resultInSideDto.setReturnCode(crNumberResult.substring(0, crNumberResult.length() - 1));
        }
      }
    } else {
      if (StringUtils.isStringNullOrEmpty(resultInSideDto.getMessage())) {
        resultInSideDto.setMessage(I18n.getValidation("sr.CR.WO.erro"));
      }
    }
    return resultInSideDto;
  }

  private String exportResultAutoCreateCr(List<SRFilesDTO> lstFileResult) {
    try {
      ExcelWriterUtils ewu = new ExcelWriterUtils();
      String pathFileImport =
          "templates" + File.separator + I18n.getLanguage("sr.template.fileMapping");
      Workbook workBook = ewu.readFileExcelFromTemplate(pathFileImport);
      Sheet sheet = workBook.getSheetAt(0);
      int i = 1;
      CellStyle cellSt1 = sheet.getRow(0).getCell(0).getCellStyle();
      ewu.createCell(sheet, 3, 0, I18n.getLanguage("common.importError"), cellSt1);
      for (SRFilesDTO row : lstFileResult) {
        ewu.createCell(sheet, 1, i, row.getIpMop() == null ? "" : row.getIpMop());
        ewu.createCell(sheet, 2, i, row.getFileName() == null ? "" : row.getFileName());
        ewu.createCell(sheet, 3, i, row.getComments() == null ? "" : row.getComments());
        i++;
      }
      String fileResult = tempFolder + File.separator + "ResultImport" + "_" + System
          .currentTimeMillis() + I18n.getLanguage("sr.template.fileMapping");
      ewu.saveToFileExcel(fileResult);
      return fileResult;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  private List<SRFilesDTO> resultFileMapping(List<GnocFileDto> gnocFileWoList) {
    List<SRFilesDTO> lstResult = new ArrayList<>();
    for (GnocFileDto fileWo : gnocFileWoList) {
      // duyệt file mapping ip lấy dữ liệu
      if (fileWo.getRequired() != null && fileWo.getRequired() == 1L) {
        try {
          byte[] byteFileMappingId = FileUtils
              .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), fileWo.getPath());
          String fullPath = FileUtils
              .saveTempFile(fileWo.getFileName(), byteFileMappingId, tempFolder);
          File fileMapping = new File(fullPath);
          //Lấy dữ liệu file
          List<Object[]> dataImportList = CommonImport.getDataFromExcelFile(
              fileMapping,
              0,
              1,
              0,
              2,
              1000
          );
          if (dataImportList.isEmpty()) {
            return new ArrayList<>();
          } else {
            for (Object[] obj : dataImportList) {
              SRFilesDTO srFilesDTO = new SRFilesDTO();
              if (obj[1] != null) {
                srFilesDTO.setIpMop(obj[1].toString().trim());
              } else {
                srFilesDTO.setIpMop(null);
              }
              if (obj[2] != null) {
                srFilesDTO.setFileName(obj[2].toString().trim());
              } else {
                srFilesDTO.setFileName(null);
              }
              lstResult.add(srFilesDTO);
            }
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      } else {
        mapFileWo.put(fileWo.getFileName(), fileWo);
      }
    }
    return lstResult;
  }

  private void mapFileProcess(Long id, Map<String, GnocFileDto> gnocFilePathProceeMap) {
    GnocFileDto gnocFileSearch = new GnocFileDto();
    gnocFileSearch.setBusinessId(id);
    gnocFileSearch.setBusinessCode(GNOC_FILE_BUSSINESS.SR_CREATE_AUTO_CR_PROCESS);
    gnocFileSearch.setMappingId(id);
    List<GnocFileDto> lstGnocFileDto = gnocFileRepository.getListGnocFileByDto(gnocFileSearch);
    if (lstGnocFileDto != null && !lstGnocFileDto.isEmpty()) {
      for (GnocFileDto gnocFileDto : lstGnocFileDto) {
        String key = gnocFileDto.getFileType() + "_" + gnocFileDto.getTemplateId();
        gnocFilePathProceeMap.put(key, gnocFileDto);
      }
    }
  }

  private void deleteCrAndWo(String crNumberCreate, UserToken userToken) {
    try {
      Long crId = Long.valueOf(crNumberCreate.split("_")[crNumberCreate.split("_").length - 1]);
      crServiceProxy.deleteCR(crId);
      WoDTOSearch condition = new WoDTOSearch();
      condition.setWoSystem("CR");
      condition.setWoSystemId(crId.toString());
      condition.setUserId(userToken.getUserID().toString());
      condition.setPage(1);
      condition.setPageSize(Integer.MAX_VALUE);
      List<WoDTOSearch> lstWo = woServiceProxy
          .getListDataSearchProxy(condition);
      if (lstWo != null && !lstWo.isEmpty()) {
        for (WoDTOSearch woDTOSearch : lstWo) {
          woServiceProxy.deleteWo(Long.valueOf(woDTOSearch.getWoId()));
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public void setTimeOutWs(Object port) {
    ((BindingProvider) port).getRequestContext()
        .put(BindingProviderProperties.CONNECT_TIMEOUT, connectTimeOut);
    ((BindingProvider) port).getRequestContext()
        .put(BindingProviderProperties.REQUEST_TIMEOUT, requestTimeOut);
  }

  public String stringDecrypt(String input, String salt) {
    String outPut = null;
    try {
      outPut = PassProtector.decrypt(input, salt);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return outPut;
  }

  //tripm nang cap od
  @Override
  public SrInsiteDTO finSrFromOdByProxyId(Long srId) {
    SrInsiteDTO srInsiteDTO = srRepository.finSrFromOdByProxyId(srId);
    return srInsiteDTO;
  }

  //dungpv 18/08/2020 add SR != trang thai Concluded va userLogin = userCreate thi cho phep them moi SR
  @Override
  public boolean checkSRConcluded() {
    log.info("Request to checkSRConcluded : {}");
    UserToken userToken = TicketProvider.getUserToken();
    if (userToken != null && StringUtils.isNotNullOrEmpty(userToken.getUserName())) {
      int countConcluded = srRepository.checkSRConcluded(userToken.getUserName());
      if (countConcluded > 0) {
        return false;
      } else {
        return true;
      }
    }
    return false;
  }

  @Override
  public ResultInSideDto getListSRFileCheckRole(GnocFileDto gnocFileDto) {
    //truongnt nang cap check role
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, null);
    SrInsiteDTO srInsiteDTO = srRepository.getDetailNoOffset(gnocFileDto.getBusinessId());
    CfgRoleDataDTO objectSearch = new CfgRoleDataDTO();
    objectSearch.setUsername(userToken.getUserName());
    objectSearch.setSystem(CFG_ROLE_DATA_SYSTEM.SR);
    objectSearch.setStatus(1L);
    CfgRoleDataDTO cfgRoleDataDTO = cfgRoleDataRepository.getConfigByDto(objectSearch);
    if (srInsiteDTO != null) {
      if (cfgRoleDataDTO != null) {
        if (cfgRoleDataDTO.getRole() == 0L || cfgRoleDataDTO.getRole() == 1L) {
          resultInSideDto.setObject(getListSRFile(gnocFileDto));
        } else {
          boolean check = false;
          List<String> lstUnit = Arrays.asList(cfgRoleDataDTO.getAuditUnitId().split(","));
          List<Long> lstUnitChild = new ArrayList<>();
          for (String unitId : lstUnit) {
            List<UnitDTO> lst = unitRepository.getListUnitChildren(Long.valueOf(unitId));
            if (lst != null && !lst.isEmpty()) {
              lstUnitChild
                  .addAll(lst.stream().map(unitDTO -> unitDTO.getUnitId()).collect(
                      Collectors.toList()));
            }
          }
          if (srInsiteDTO.getSrUnit() != null && lstUnitChild.contains(srInsiteDTO.getSrUnit())) {
            check = true;
          }

          List<UnitDTO> lstUnitUserLogin = unitRepository
              .getListUnitChildren(userToken.getDeptId());
          UsersInsideDto createdUser =
              StringUtils.isNotNullOrEmpty(srInsiteDTO.getCreatedUser()) ? userRepository
                  .getUserDTOByUserName(srInsiteDTO.getCreatedUser()) : null;
          List<Long> lstUnitId = lstUnitUserLogin.stream().map(unitDTO -> unitDTO.getUnitId())
              .collect(
                  Collectors.toList());
          if (srInsiteDTO.getSrUnit() != null && StringUtils
              .isNotNullOrEmpty(srInsiteDTO.getRoleCode())) {
            SRRoleUserInSideDTO roleUserInSideDTO = new SRRoleUserInSideDTO();
            roleUserInSideDTO.setUnitId(srInsiteDTO.getSrUnit());
            roleUserInSideDTO.setCountry(srInsiteDTO.getCountry());
            roleUserInSideDTO.setRoleCode(srInsiteDTO.getRoleCode());
            List<SRRoleUserInSideDTO> lstUser = getCmbSrUser(roleUserInSideDTO);
            List<String> lstUsername = lstUser.stream().map(roleUser -> roleUser.getUsername())
                .collect(
                    Collectors.toList());
            if (lstUsername != null && !lstUsername.isEmpty() && lstUsername
                .contains(userToken.getUserName())) {
              check = true;
            }
          }
          if (StringUtils.isNotNullOrEmpty(srInsiteDTO.getSrUser()) && srInsiteDTO.getSrUser()
              .equals(userToken.getUserName())) {
            check = true;
          }
          if (lstUnitUserLogin != null && !lstUnitUserLogin.isEmpty() && createdUser != null
              && createdUser.getUnitId() != null && lstUnitId
              .contains(createdUser.getUnitId())) {
            check = true;
          }
          if (check) {
            resultInSideDto.setObject(getListSRFile(gnocFileDto));
          } else {
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(I18n.getLanguage("sr.error.permissionViewFile"));
          }
        }
      } else {
        boolean check = false;
        List<UnitDTO> lstUnit = unitRepository.getListUnitChildren(userToken.getDeptId());
        UsersInsideDto createdUser =
            StringUtils.isNotNullOrEmpty(srInsiteDTO.getCreatedUser()) ? userRepository
                .getUserDTOByUserName(srInsiteDTO.getCreatedUser()) : null;
        List<Long> lstUnitId = lstUnit.stream().map(unitDTO -> unitDTO.getUnitId()).collect(
            Collectors.toList());
        if (srInsiteDTO.getSrUnit() != null && StringUtils
            .isNotNullOrEmpty(srInsiteDTO.getRoleCode())) {
          SRRoleUserInSideDTO roleUserInSideDTO = new SRRoleUserInSideDTO();
          roleUserInSideDTO.setUnitId(srInsiteDTO.getSrUnit());
          roleUserInSideDTO.setCountry(srInsiteDTO.getCountry());
          roleUserInSideDTO.setRoleCode(srInsiteDTO.getRoleCode());
          List<SRRoleUserInSideDTO> lstUser = getCmbSrUser(roleUserInSideDTO);
          List<String> lstUsername = lstUser.stream().map(roleUser -> roleUser.getUsername())
              .collect(
                  Collectors.toList());
          if (lstUsername != null && !lstUsername.isEmpty() && lstUsername
              .contains(userToken.getUserName())) {
            check = true;
          }
        }
        if (StringUtils.isNotNullOrEmpty(srInsiteDTO.getSrUser()) && srInsiteDTO.getSrUser()
            .equals(userToken.getUserName())) {
          check = true;
        }
        if (lstUnit != null && !lstUnit.isEmpty() && createdUser != null
            && createdUser.getUnitId() != null && lstUnitId
            .contains(createdUser.getUnitId())) {
          check = true;
        }
        if (check) {
          resultInSideDto.setObject(getListSRFile(gnocFileDto));
        } else {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("sr.error.permissionViewFile"));
        }
      }
    } else {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getLanguage("sr.error.permissionViewFile"));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto closedSR(Long srId) {
    log.info("Request to closedSR : {}", srId);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    UserToken userToken = ticketProvider.getUserToken();
    SrInsiteDTO srClosed = srRepository.getDetailNoOffset(srId);
    if (srClosed != null && StringUtils.isNotNullOrEmpty(srClosed.getSrCode())) {
      if (StringUtils.isNotNullOrEmpty(srClosed.getCreatedUser()) && srClosed.getCreatedUser()
          .equals(userToken.getUserName())) {
        String statusOld = srClosed.getStatus();
        srClosed.setUpdatedTime(new Date());
        srClosed.setUpdatedUser(userToken.getUserName());
        srClosed.setStatus(SR_STATUS.CLOSED);
        resultInSideDto = srRepository.insertSR(srClosed);
        if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          SRActionCodeDTO searchActionCode = new SRActionCodeDTO();
          searchActionCode.setOldStatus(statusOld);
          searchActionCode.setNewStatus(srClosed.getStatus());
          List<SRActionCodeDTO> lsActionCode = srRepository
              .searchSrActionCode(searchActionCode, 0, 0, "asc", "acId");
          SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
          SRHisDTO srHisDTO = new SRHisDTO();
          srHisDTO.setCreatedUser(srClosed.getUpdatedUser());
          srHisDTO.setCreatedTime(dateFormat.format(srClosed.getUpdatedTime()));
          srHisDTO.setSrStatus(srClosed.getStatus());
          srHisDTO.setSrId(srClosed.getSrId().toString());
          if (lsActionCode != null && lsActionCode.size() > 0) {
            srHisDTO.setActionCode(lsActionCode.get(0).getActionCode());
            srHisDTO.setComments(lsActionCode.get(0).getDefaultComment());
          }
          srHisRepository.createSRHis(srHisDTO);
        }
      } else {
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage(I18n.getValidation("sr.validate.btnClosedSR"));
        return resultInSideDto;
      }
    }
    return resultInSideDto;
  }

  //03112020 dungpv add them CrNumber khi them moi,sao chep,link Cr trong tab cap nhat SR
  @Override
  public ResultInSideDto updateCrNumberForSR(String crNumber, Long srId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    if (!StringUtils.isLongNullOrEmpty(srId)) {
      List<SrInsiteDTO> lstCrNumbers = srRepository
          .getCrNumberCreatedFromSR(new SrInsiteDTO(srId), 0, 0, false);
      String crNumbers = "";
      if (lstCrNumbers != null && !lstCrNumbers.isEmpty()) {
        for (SrInsiteDTO crInsiteDTO : lstCrNumbers) {
          crNumbers += crInsiteDTO.getCrNumber() + ",";
        }
        if (!crNumbers.contains(crNumber)) {
          crNumbers += crNumber + ",";
        }
        if (StringUtils.isNotNullOrEmpty(crNumbers) && crNumbers.endsWith(",")) {
          crNumbers = crNumbers.substring(0, crNumbers.length() - 1);
        }
      } else if (StringUtils.isNotNullOrEmpty(crNumber)) {
        crNumbers += crNumber;
      }
      if (StringUtils.isNotNullOrEmpty(crNumbers)) {
        resultInSideDto = srRepository.updateCrNumberForSR(crNumbers, srId);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateCrNumberTabSRForCR(CrInsiteDTO crInsiteDTO) {
    log.info("Request to updateCrNumberTabSRForCR : {}", crInsiteDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    if (crInsiteDTO.getLstCrCreatedFromOtherSysDTO() != null
        && crInsiteDTO.getLstCrCreatedFromOtherSysDTO().size() > 0) {

      List<CrCreatedFromOtherSysDTO> lst = crInsiteDTO.getLstCrCreatedFromOtherSysDTO();
      for (CrCreatedFromOtherSysDTO dto : lst) {
        if ("5".equals(dto.getSystemId()) && StringUtils.isNotNullOrEmpty(dto.getObjectId())) {
          resultInSideDto = updateCrNumberForSR(crInsiteDTO.getCrNumber(),
              Long.parseLong(dto.getObjectId()));
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public Datatable getListTabSrChild(SrInsiteDTO srInsiteDTO) {
    log.info("Request to getListTabSrChild : {}", srInsiteDTO);
    return srRepository.getListTabSrChild(srInsiteDTO);
  }
}
