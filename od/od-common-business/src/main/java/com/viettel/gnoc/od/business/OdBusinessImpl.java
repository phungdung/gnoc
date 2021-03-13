package com.viettel.gnoc.od.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.viettel.gnoc.commons.business.CfgWhiteListIpBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.SignVofficeDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.WoSearchWebDTO;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.OdCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.SrServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.MASTER_DATA;
import com.viettel.gnoc.commons.utils.Constants.OD_NEXT_ACTION;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.od.dto.*;
import com.viettel.gnoc.od.repository.OdRelationRepository;
import com.viettel.gnoc.od.repository.OdRepository;
import com.viettel.gnoc.od.wsclient.WSNIMS_HTPort;
import com.viettel.gnoc.od.wsclient.WS_VOFFICE_Port;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.wfm.dto.ConfigPropertyDTO;
import com.viettel.gnoc.wfm.dto.WoTypeTimeDTO;
import com.viettel.nims.webservice.ht.InputUpdateStationAuditedStatusForm;
import com.viettel.nims.webservice.ht.ResultUpdateStationAuditedStatusForm;
import com.viettel.security.PassTranformer;
import com.viettel.voffice.ws_autosign.service.FileAttachTranfer;
import com.viettel.voffice.ws_autosign.service.KttsVofficeCommInpuParam;
import com.viettel.voffice.ws_autosign.service.Vof2EntityUser;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

/**
 * @author NamTN
 */
@Service
@Transactional
@Slf4j
public class OdBusinessImpl implements OdBusiness {

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

  @Value("${application.SmsGatewayId:5}")
  private String smsGatewayId;

  @Value("${application.SenderId:400}")
  private String senderId;

  private final static String OD_SEQ = "OD_SEQ";
  private final static String OD_RESULT_IMPORT = "OD_RESULT_IMPORT";
  private final static String OD_EXPORT = "OD_EXPORT";
  private Map<String, String> mapOdStatus = new HashMap<>();

  @Autowired
  OdRepository odRepository;

  @Autowired
  OdHistoryBusiness odHistoryBusiness;

  @Autowired
  MessagesRepository messagesRepository;

  @Autowired
  OdRelationRepository odRelationRepository;

  @Autowired
  OdCategoryServiceProxy odCategoryServiceProxy;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  WS_VOFFICE_Port wsVofficePort;

  @Autowired
  WSNIMS_HTPort wsnimsHtPort;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  CommonRepository commonRepository;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Autowired
  SrServiceProxy srServiceProxy;

  @Autowired
  protected OdCommonBusiness odCommonBusiness;

  @Autowired
  CfgWhiteListIpBusiness userPassWordBusiness;

  @Override
  public Datatable getListDataSearch(OdSearchInsideDTO odSearchInsideDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    if (userToken == null) {
      log.error("Insert OD: UserToken is null");
    } else {
      odSearchInsideDTO.setUserId(String.valueOf(userToken.getUserID()));
    }
    log.debug("Request to getListDataSearch : {}", odSearchInsideDTO);
    return odRepository.getListDataSearch(odSearchInsideDTO);
  }

  @Override
  public OdDTO findOdById(Long odId) {
    log.debug("Request to findOdById : {}", odId);
    return odRepository.findOdById(odId);
  }

  @Override
  public ResultInSideDto add(OdDTO odDTO) {
    log.debug("Request to add : {}", odDTO);
    return odRepository.add(odDTO);
  }

  @Override
  public ResultInSideDto edit(OdDTO odDTO) {
    log.debug("Request to edit : {}", odDTO);
    return odRepository.edit(odDTO);
  }

  @Override
  public ResultInSideDto delete(Long odTypeId) {
    log.debug("Request to delete : {}", odTypeId);
    return odRepository.delete(odTypeId);
  }

  @Override
  public ResultInSideDto deleteListOd(List<OdDTO> odDTOList) {
    log.debug("Request to deleteListOd : {}", odDTOList);
    return odRepository.deleteListOd(odDTOList);
  }

  @Override
  public List<String> getSeqOd(int size) {
    log.debug("Request to getSeqOdType : {}", size);
    return odRepository.getSeqOd(OD_SEQ, size);
  }

  @Override
  public OdDTO getDetailOdDTOById(Long odId) {
    OdDTO odDTO = odRepository.getDetailOdDTOById(odId);
    List<OdHistoryDTO> odHistoryDTOS = odHistoryBusiness.getOdHistoryByOdId(odId);
//    List<OdRelationDTO> odRelationDTOS = odRelationRepository.getRelationsByOdId(odId);
//    if (odRelationDTOS != null) {
//      for (int i = 0; i < odRelationDTOS.size(); i++) {
//        if (odRelationDTOS.get(i).getSystem() != null && "WO"
//            .equals(odRelationDTOS.get(i).getSystem())) {
//          WoSearchWebDTO wo = woServiceProxy
//              .getWoSearchWebDTOByWoCode(odRelationDTOS.get(i).getSystemCode());
//          if (wo != null) {
//            odRelationDTOS.get(i).setStatus(convertWOStatusToString(wo.getStatus()));
//          }
//        }
//      }
//    }
    if (odDTO != null) {
      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setBusinessCode(Constants.GNOC_FILE_BUSSINESS.OD);
      gnocFileDto.setBusinessId(odId);
      odDTO.setGnocFileDtos(gnocFileRepository.getListGnocFileByDto(gnocFileDto));
      odDTO.setOdHistoryDTO(odHistoryDTOS);
//      odDTO.setLstOdRelation(odRelationDTOS);
    }
    return odDTO;
  }

  @Override
  public OdDTO getDetailOdDTOByIdForWS(Long odId) {
    OdDTO odDTO = odRepository.getDetailOdDTOById(odId);
    List<CatItemDTO> lstStatus = catItemRepository.getDataItem(CATEGORY.OD_STATUS);
    if (lstStatus != null && !lstStatus.isEmpty()) {
      lstStatus.stream().forEach(item -> {
        if (!StringUtils.isLongNullOrEmpty(odDTO.getOldStatus()) && StringUtils
            .isNotNullOrEmpty(item.getItemValue()) && odDTO.getOldStatus()
            .equals(Long.valueOf(item.getItemValue()))) {
          odDTO.setOldStatusName(item.getItemName());
        }
      });
    }
    List<OdHistoryDTO> odHistoryDTOS = odHistoryBusiness.getOdHistoryByOdId(odId);
    if (odDTO != null) {
      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setBusinessCode(Constants.GNOC_FILE_BUSSINESS.OD);
      gnocFileDto.setBusinessId(odId);
      List<GnocFileDto> litOdFiles = gnocFileRepository.getListGnocFileByDto(gnocFileDto);
      if (litOdFiles != null && !litOdFiles.isEmpty()) {
        for (GnocFileDto fileDto : litOdFiles) {
          if (StringUtils.isNotNullOrEmpty(fileDto.getPath())) {
            try {
              byte[] bFile = FileUtils
                  .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                      PassTranformer.decrypt(ftpPass), fileDto.getPath());
              fileDto.setContent(org.apache.xml.security.utils.Base64.encode(bFile));
            } catch (Exception ex) {
              log.error(ex.getMessage(), ex);
            }
          }
        }
        odDTO.setGnocFileDtos(litOdFiles);
      }
      if (odHistoryDTOS != null && !odHistoryDTOS.isEmpty()) {
        odDTO.setOdHistoryDTO(odHistoryDTOS);
      }
    }
    return odDTO;
  }

  @Override
  public String insertOrUpdateListOd(List<OdDTO> odDTOs) {
    try {
      for (OdDTO odDTO : odDTOs) {
        odRepository.insertOrUpdate(odDTO);
      }
      return RESULT.SUCCESS;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public File exportData(OdSearchInsideDTO odSearchInsideDTO) throws Exception {
    UserToken userToken = ticketProvider.getUserToken();
    if (userToken == null) {
      log.error("Insert OD: UserToken is null");
    } else {
      odSearchInsideDTO.setUserId(String.valueOf(userToken.getUserID()));
    }
    List<OdSearchInsideDTO> odSearchInsideDTOS = odRepository.getListDataExport(odSearchInsideDTO);
    return exportFileEx(odSearchInsideDTOS, "");
  }

  @Override
  public ResultInSideDto signToVoffice(SignVofficeDTO signVofficeDTO, List<MultipartFile> files)
      throws Exception {
    ResultInSideDto res = new ResultInSideDto();
    try {
      OdDTO odresult = odRepository.findOdById(signVofficeDTO.getOdId());
      if (odresult.getOdId() == null) {
        throw new RuntimeException(I18n.getValidation("od.signToVoffice.od.invalid"));
      }
      UsersEntity exeUs = odRepository.getUserByUserName(signVofficeDTO.getUserName());
      if (signVofficeDTO.getUserName() == null
          || signVofficeDTO.getUserName().trim().length() == 0) {
        throw new RuntimeException(I18n.getValidation("od.signToVoffice.userName.isRequired"));
      }
      if (signVofficeDTO.getPassword() == null
          || signVofficeDTO.getPassword().trim().length() == 0) {
        throw new RuntimeException(I18n.getValidation("od.signToVoffice.password.isRequired"));
      }
      if (signVofficeDTO.getEmailUser() == null
          || signVofficeDTO.getEmailUser().trim().length() == 0) {
        throw new RuntimeException(I18n.getValidation("od.signToVoffice.email.isRequired"));
      }
      if (signVofficeDTO.getTitle() == null || signVofficeDTO.getTitle().trim().length() == 0) {
        throw new RuntimeException(I18n.getValidation("od.signToVoffice.title.isRequired"));
      }
      if (exeUs == null) {
        throw new RuntimeException(I18n.getValidation("od.sign.exeUserIsNotExists"));
      }
      String input = getConfig("OD_VOFFICE_INPUT");
      //String input = CommunicateOdController.getValueFromConfig(mapConfigProperty, Constants.OD_PROPERTY_CODE.OD_VOFFICE_INPUT);
      PassTranformer.setInputKey(input);
      //PassTranformer passTr = new PassTranformer();
      //passTr.setInputKey(input);
      String passEncrypt = PassTranformer.encrypt(signVofficeDTO.getPassword());

      //WS_VOFFICE_Port port = new WS_VOFFICE_Port();
      List<String> lstUser = new ArrayList<String>();
      lstUser.add(signVofficeDTO.getEmailUser());
      List<Vof2EntityUser> lstUf = wsVofficePort.getListVof2UserByMail(lstUser);
      if (lstUf == null) {
        throw new RuntimeException(I18n.getValidation("od.sign.err.15"));
      }
      KttsVofficeCommInpuParam vofficeParam = new KttsVofficeCommInpuParam();
      vofficeParam.getLstUserVof2().addAll(lstUf);
      vofficeParam.setDocTitle(signVofficeDTO.getTitle());
      vofficeParam.setAccountName(signVofficeDTO.getUserName());
      vofficeParam.setAccountPass(passEncrypt);
      //
      List<Vof2EntityUser> lstUserSign = new ArrayList<>();
      for (Vof2EntityUser entityUser : vofficeParam.getLstUserVof2()) {
        if (entityUser.getSignImageIndex() != null) {
          lstUserSign.add(entityUser);
        }
      }
      List<FileAttachTranfer> lstTranfer = getListFilePlan(files);
      //
      vofficeParam.getLstFileAttach().addAll(lstTranfer);
      //
      vofficeParam.setAppCode(getConfig(Constants.OD_PROPERTY_CODE.OD_APP_CODE_VOFFICE));
      vofficeParam.setAppPass(getConfig(Constants.OD_PROPERTY_CODE.OD_APP_PASS_VOFFICE));
      vofficeParam.setSender(getConfig(Constants.OD_PROPERTY_CODE.OD_SENDER_VOFFICE));
      vofficeParam.setTransCode(odresult.getOdCode() + "_" + new Date().getTime());
      vofficeParam.setRegisterNumber(
          getConfig(Constants.OD_PROPERTY_CODE.OD_REGISTER_VOFFICE));// ma hieu van ban
      String form = getConfig(Constants.OD_PROPERTY_CODE.OD_FORM_DOCUMENT_VOFFICE);// hinh thuc vb
      vofficeParam.setHinhthucVanban(form == null ? null : Long.parseLong(form));
      String sector = getConfig(Constants.OD_PROPERTY_CODE.OD_AREA_VOFFICE);
      vofficeParam.setAreaId(sector == null ? null : Long.parseLong(sector));
//            vofficeParam.setCreateDate(mapProperty.get("INPUT_VOFFICE"));// set key

      Long signRes = wsVofficePort.vo2RegDigitalDocByEmail(vofficeParam);
      if (signRes == null || !signRes.equals(1L)) {
        res.setKey("FAIL");
        if (signRes == null) {
          throw new RuntimeException(
              "[GNOC] " + I18n.getValidation("haveSomeErrorWhenCallVoffice"));
        } else {
          String msg = "[VOFFICE-" + signRes + "] " + I18n.getValidation("od.sign.err." + signRes);
          if (msg.contains("od.sign.err.")) {
            msg = "[VOFFICE-" + signRes + "] " + I18n.getValidation("od.sign.err.OtherEr");
          }

          throw new RuntimeException(msg);
        }
      } else {
        try {
          odresult.setVofficeTransCode(vofficeParam.getTransCode());
          odresult.setLastUpdateTime(new Date());
          odRepository.insertOrUpdate(odresult);
          odHistoryBusiness.insertOdHistory(odresult, odresult.getStatus(), odresult.getStatus(),
              I18n.getLanguage("od.sign.SignToVoffice"), exeUs);
          res.setKey("SUCCESS");
          res.setMessage(vofficeParam.getTransCode());
          res.setId(1L);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          throw new RuntimeException(
              "[GNOC] " + I18n.getValidation("od.sign.err.HaveSomeErr") + ":" + e.getMessage());
        }
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return res;
  }

  @Override
  public ResultInSideDto insertOdFromWeb(OdSearchInsideDTO odDTO, List<MultipartFile> files)
      throws Exception {
    log.debug("Request to insertOdFromWeb: {}", odDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setMessage(RESULT.SUCCESS);
    List<String> lstUnit = odDTO.getLstReceiveUnitId();
    if (lstUnit == null || lstUnit.size() < 1) {
      throw new RuntimeException(I18n.getLanguage("wo.notify.receiveUnitIsNotNull"));
    }
    for (String unit : lstUnit) {
      odDTO.setReceiveUnitId(Long.parseLong(unit));
      OdDTO od = odDTO.toDTO();
      ResultInSideDto res = insertOd(od, files);
      if (!RESULT.SUCCESS.equals(res.getKey())) {
        throw new RuntimeException(I18n.getLanguage("wo.err.save.od"));
      }
    }
    return resultInSideDto;

  }

  @Override
  public ResultInSideDto updateOdFromWeb(OdSearchInsideDTO odSearchInsideDTO,
      List<MultipartFile> files,
      String userName) throws Exception {
    log.debug("Request to insertOdFromWeb: {}", odSearchInsideDTO);

    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setMessage(RESULT.SUCCESS);
    OdDTO result = odRepository.findOdById((odSearchInsideDTO.getOdId()));
    if (result != null && result.getWoId() != null) {
      odSearchInsideDTO.setWoId(result.getWoId());
    }
    OdDTO o = odSearchInsideDTO.toDTO();
    o.setGnocFileDtos(odSearchInsideDTO.getGnocFileDtos());
    UserToken userToken = ticketProvider.getUserToken();
    if (userToken != null && userToken.getUserName() != null) {
      userName = userToken.getUserName();
    } else {
      log.warn("Update OD: UserToken is null");
    }
    UsersEntity user = odRepository.getUserByUserName(userName);
    checkNextActionFinishTime(o, user, odSearchInsideDTO.getOldStatus());
    updateOdFileAttach(odSearchInsideDTO, files);
    //con phan gui service nim chua connect dc
    return updateOd(o, user, (odSearchInsideDTO.getOldStatus()), odSearchInsideDTO.getComment(),
        files);
  }

  public ResultInSideDto updateOdFileAttach(OdSearchInsideDTO odSearchInsideDTO,
      List<MultipartFile> files) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setMessage(RESULT.SUCCESS);
    try {
      UserToken userToken = ticketProvider.getUserToken();
      UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
      //Start save file old
//      List<OdFileEntity> lstFileOld = odRepository
//          .getListOdFileByOdId(odSearchInsideDTO.getOdId());
//      List<Long> listIdFileIdOld = lstFileOld.stream()
//          .map(OdFileEntity::getOdFileId).collect(Collectors.toList());
//      List<GnocFileDto> gnocFileDtosWeb = odSearchInsideDTO.getGnocFileDtos();
//      List<Long> listIdFileIdNew = gnocFileDtosWeb.stream()
//          .map(GnocFileDto::getMappingId).collect(Collectors.toList());
//      listIdFileIdOld.removeAll(listIdFileIdNew);
      if (odSearchInsideDTO.getIdDeleteList() != null) {
        for (Long aLong : odSearchInsideDTO.getIdDeleteList()) {
          odRepository.deleteOdFile(aLong);
        }
      }
      gnocFileRepository
          .deleteListGnocFile(GNOC_FILE_BUSSINESS.OD, odSearchInsideDTO.getOdId(),
              odSearchInsideDTO.getIdDeleteList());
      //End save file old
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      if (files != null && files.size() > 0) {
        for (MultipartFile multipartFile : files) {
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                  multipartFile.getBytes(), odSearchInsideDTO.getCreateTime());
          String fileName = multipartFile.getOriginalFilename();
          //Start save file old
          String fullPathOld = FileUtils
              .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                  uploadFolder, null);
          OdFileDTO odFileDTO = new OdFileDTO();
          odFileDTO.setOdId(odSearchInsideDTO.getOdId());
          odFileDTO.setPath(FileUtils.getFilePath(fullPathOld));
          odFileDTO.setFileName(FileUtils.getFileName(fullPathOld));
          ResultInSideDto resultFileDataOld = odRepository.insertOdFile(odFileDTO);
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
      }
//      List<GnocFileDto> gnocFileDtosAll = new ArrayList<>();
//      gnocFileDtosAll.addAll(gnocFileDtos);
//      gnocFileDtosAll.addAll(odSearchInsideDTO.getGnocFileDtos());
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.OD, odSearchInsideDTO.getOdId(),
              gnocFileDtos);
      return resultInSideDto;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(I18n.getLanguage("od.have.some.err.when.save.File"));
    }
  }

  public ResultInSideDto insertOd(OdDTO odDTO, List<MultipartFile> files) throws IOException {
    ResultInSideDto result;
    String message = validateAdd(odDTO);
    if (!StringUtils.isStringNullOrEmpty(message)) {
      throw new RuntimeException(message);
    }
    UserToken userToken = ticketProvider.getUserToken();
    Long createPersonId = odDTO.getCreatePersonId();
    if (userToken != null && userToken.getUserID() != null) {
      createPersonId = userToken.getUserID();
      odDTO.setCreatePersonId(createPersonId);
    } else {
      log.warn("Insert OD: UserToken is null");
    }

    UsersEntity creater = odRepository.getUserByUserId(createPersonId);
    if (creater == null) {
      throw new RuntimeException(I18n.getLanguage("od.notExistsCreatePersonId"));
    }

    String odId = odRepository.getSeqOd("OD_SEQ", 1).get(0);
    SimpleDateFormat dateNoSlash = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
    String odCode =
        "OD_" + odDTO.getInsertSource() + "_" + dateNoSlash.format(new Date()) + "_" + odId;
    odDTO.setOdCode(odCode);
    odDTO.setOdId(Long.parseLong(odId));
    odDTO.setStatus(Constants.OD_STATUS.NEW);
    odDTO.setLastUpdateTime(new Date());
    odDTO.setCreateTime(new Date());
    odDTO.setCreateUnitId(creater.getUnitId());
    ResultInSideDto dto = odRepository.insertOrUpdate(odDTO);
    if (!RESULT.SUCCESS.equals(dto.getMessage())) {
      throw new RuntimeException(I18n.getLanguage("wo.err.save.od"));
    }
    //luu lich su
    result = odHistoryBusiness
        .insertOdHistory(odDTO, null, odDTO.getStatus(), I18n.getLanguage("od.AddNewOd") + "("
            + DateTimeUtils
            .convertDateToString(odDTO.getStartTime(), Constants.formatterDateTimeText)
            + " - "
            + DateTimeUtils.convertDateToString(odDTO.getEndTime(), Constants.formatterDateTimeText)
            + ")", creater);

    if (result != null && StringUtils.isNotNullOrEmpty(result.getMessage()) && !RESULT.SUCCESS
        .equals(result.getMessage())) {
      throw new RuntimeException(I18n.getLanguage("wo.err.save.odHis"));
    }
    //luu file dinh kem
    result = insertFile(odDTO, files);
    if (result.getKey().equals(RESULT.ERROR)) {
      throw new RuntimeException(I18n.getLanguage("sr.notAttachFile"));
    }
    if (!RESULT.SUCCESS.equals(result.getMessage())) {
      throw new RuntimeException(I18n.getLanguage("wo.err.save.odFile"));
    }

    // luu thong tin lien ket
    result = insertLstRelation(odDTO);
    if (!Constants.RESULT.SUCCESS.equals(result.getMessage())) {
      throw new RuntimeException(I18n.getLanguage("wo.err.save.odRelation"));
    }

    // thuc hien nhan tin nhan vien tao va don vi xu ly
    sendMessageCreateOd(odDTO, creater);

    result.setId(Long.parseLong(odId));
    result.setKey(Constants.RESULT.SUCCESS);
    result.setMessage(Constants.RESULT.SUCCESS);

    return result;
  }

  public ResultInSideDto updateOd(OdDTO odDTO, UsersEntity user, Long oldStatus, String comment,
      List<MultipartFile> multipartFiles) throws Exception {
    ResultInSideDto result = new ResultInSideDto();
    result.setKey(Constants.RESULT.SUCCESS);
    result.setMessage(Constants.RESULT.SUCCESS);
    Map<String, String> mapConfigProperty = odRepository.getConfigProperty();
    odDTO.setLastUpdateTime(new Date());
    if ("1".equals(odDTO.getResultApproval())) {
      odDTO.setStatus(9L);
      //Insert approval pause
      try {
        OdPendingDTO odPendingDTO = new OdPendingDTO();
        odPendingDTO.setOdId(odDTO.getOdId().toString());
        odPendingDTO.setEndPendingTime(odDTO.getEndPendingTime());
        odPendingDTO.setInsertTime(new Date());
        odPendingDTO.setReasonPendingName(odDTO.getReasonPause());
        odRepository.insertApprovalPause(odPendingDTO);
        result.setCheck(true);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    else if ("0".equals(odDTO.getResultApproval())) {
      OdHistoryDTO odHistoryDTO = odRepository.getListOdHistory(odDTO.getOdId());
      if (odHistoryDTO != null) {
        odDTO.setStatus(odHistoryDTO.getOldStatus());
        result.setCheck(false);
      }
    }
    String message = validateUpdate(odDTO, multipartFiles);
    if (!StringUtils.isStringNullOrEmpty(message)) {
      throw new RuntimeException(message);
    }
    Date now = new Date();
    // luu lich su
    odHistoryBusiness.insertOdHistory(odDTO, oldStatus, odDTO.getStatus(), comment, user);
    // luu thong tin lien ket
    result = insertLstRelation(odDTO);
    if (!Constants.RESULT.SUCCESS.equals(result.getMessage())) {
      throw new RuntimeException(I18n.getLanguage("wo.err.save.odRelation"));
    }
    // thuc hien nhan tin
    sendMesseageUpdateOd(odDTO, oldStatus);
    // thuc hien cac nghiep vu bo sung
    doNextAction(odDTO, user, oldStatus, now, mapConfigProperty, comment);
    odRepository.insertOrUpdate(odDTO);
    return result;
  }

  public ResultInSideDto insertLstRelation(OdDTO odDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setMessage(RESULT.SUCCESS);
    if (odDTO.getLstOdRelation() != null && odDTO.getLstOdRelation().size() > 0) {
      for (OdRelationDTO o : odDTO.getLstOdRelation()) {
        o.setOdId(odDTO.getOdId());
        odRelationRepository.insertOrUpdate(o);
      }
    }
    return resultInSideDto;
  }

  @Override
  public List<OdRelationDTO> getRelationsByOdId(Long odId) {
    List<OdRelationDTO> odRelationDTOS = odRelationRepository.getRelationsByOdId(odId);
    if (odRelationDTOS != null) {
      for (int i = 0; i < odRelationDTOS.size(); i++) {
        if (odRelationDTOS.get(i).getSystem() != null && "WO"
            .equals(odRelationDTOS.get(i).getSystem())) {
          WoSearchWebDTO wo = woServiceProxy
              .getWoSearchWebDTOByWoCode(odRelationDTOS.get(i).getSystemCode());
          if (wo != null) {
            odRelationDTOS.get(i).setStatus(convertWOStatusToString(wo.getStatus()));
          }
        }
        if (odRelationDTOS.get(i).getSystem() != null && "CR"
            .equals(odRelationDTOS.get(i).getSystem())) {
          CrInsiteDTO cr = crServiceProxy.findCrByIdProxy(odRelationDTOS.get(i).getSystemId());
          if (cr != null && cr.getState() != null) {
            odRelationDTOS.get(i).setStatus(I18n.getLanguage("cr.state." + cr.getState()));
          }
        }

        if (odRelationDTOS.get(i).getSystem() != null && "SR"
            .equals(odRelationDTOS.get(i).getSystem())) {
          SrInsiteDTO sr = srServiceProxy
              .findSrFromOdByProxyId(odRelationDTOS.get(i).getSystemId());
          if (sr != null) {
            odRelationDTOS.get(i).setStatus(sr.getStatus());
          }
        }

        if (odRelationDTOS.get(i).getSystem() != null && "RDM"
            .equals(odRelationDTOS.get(i).getSystem())) {
          OdRelationDTO rdm = getRDMRelationById(odRelationDTOS.get(i).getSystemId());
          if (rdm != null) {
            odRelationDTOS.get(i).setStatus(rdm.getStatus());
            odRelationDTOS.get(i).setReceiveUnitName(rdm.getReceiveUnitName());
            odRelationDTOS.get(i).setCreatePersonName(rdm.getCreatePersonName());
          }
        }
      }

    }
    return odRelationDTOS;
  }

  @Override
  public Datatable getListRDMRelationToUpdate(String projectCode, String createDateFrom,
      String createDateTo, int page, int size, Double offSet) {
    List<OdRelationDTO> lst = new ArrayList<>();
    Datatable datatable = new Datatable();
    try {
      Gson gson = new Gson();
      Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();

      String urlRDM =
          mapConfigProperty.get("url_rdm") + "getListProjectByOdWS?timeZoneOffset=00:00";
      String urlParameters = "{\n"
          + "	\"page\": 1,\n"
          + "	\"pageSize\": 1000,\n"
          + "\"projectCode\": \"" + projectCode + "\",\n"
          + "\"createDateFrom\": \"" + createDateFrom + "\",\n"
          + "\"createDateTo\": \"" + createDateTo + "\"\n"
          + "}";

      URL url = new URL(urlRDM);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      //add reuqest header
      conn.setRequestMethod("POST");
      conn.setRequestProperty("CLIENT_NAME", "API_WS");
      conn.setRequestProperty("content-type", "application/json;  charset=utf-8");

      // Send post request
      conn.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
      wr.writeBytes(urlParameters);
      wr.flush();
      wr.close();

      int responseCode = conn.getResponseCode();
      if (responseCode != 200) {
        throw new RuntimeException("Failed : RDM error code : " + conn.getResponseCode());
      }

      BufferedReader input = new BufferedReader(
          new InputStreamReader(conn.getInputStream(), "UTF-8"));
      StringBuilder sb = new StringBuilder();
      String output;
      while ((output = input.readLine()) != null) {
        sb.append(output).append("\r\n");
      }
      if (sb != null) {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(sb.toString(), Map.class);
        if (map != null && map.get("data") != null) {
          for (Object obj : (List<Object>) map.get("data")) {
            LinkedHashMap<String, String> temp = (LinkedHashMap<String, String>) obj;
            OdRelationDTO odRelationDTO = new OdRelationDTO();
            odRelationDTO.setContent(temp.get("content"));
            if (StringUtils.validString(temp.get("startDate"))) {
              odRelationDTO
                  .setCreateTime(convertServerTimeToClientTime(temp.get("startDate"), offSet));
            }
            if (StringUtils.validString(temp.get("endDate"))) {
              odRelationDTO.setEndTime(convertServerTimeToClientTime(temp.get("endDate"), offSet));
            }
            odRelationDTO.setSystem("RDM");
            odRelationDTO.setSystemId(StringUtils.isNotNullOrEmpty(temp.get("projectId")) ? Long.parseLong(temp.get("projectId")) : null);
            odRelationDTO.setReceiveUnitName(temp.get("unitName"));
            odRelationDTO.setReceiveUnitId(StringUtils.isNotNullOrEmpty(temp.get("unitId")) ? Long.parseLong(temp.get("unitId")) : null);
            odRelationDTO.setStatus(temp.get("statusName"));
            odRelationDTO.setCreatePersonName(temp.get("createUser"));
            odRelationDTO.setCreatePersonId(StringUtils.isNotNullOrEmpty(temp.get("createUserId")) ? Long.parseLong(temp.get("createUserId")) : null);
            odRelationDTO.setSystemCode(temp.get("projectCode"));
            lst.add(odRelationDTO);
          }

          int totalSize = lst.size();
          int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
          List<OdRelationDTO> crSubList = (List<OdRelationDTO>) DataUtil
              .subPageList(lst, page, size);
          if (crSubList != null && crSubList.size() > 0) {
            crSubList.get(0).setPage(page);
            crSubList.get(0).setPageSize(pageSize);
            crSubList.get(0).setTotalRow(totalSize);
          }
          datatable.setData(crSubList);
          datatable.setTotal(totalSize);
          datatable.setPages(pageSize);
        }

      }
      conn.disconnect();
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return datatable;
  }

  public ResultInSideDto insertFile(OdDTO odDTO, List<MultipartFile> files) throws IOException {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    resultInSideDto.setMessage(Constants.RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    for (MultipartFile multipartFile : files) {
      String fullPath = FileUtils
          .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
              multipartFile.getBytes(), odDTO.getCreateTime());
      String fileName = multipartFile.getOriginalFilename();
      //Start save file old
      String fullPathOld = FileUtils
          .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
              uploadFolder, odDTO.getCreateTime());
      OdFileDTO odFileDTO = new OdFileDTO();
      odFileDTO.setOdId(odDTO.getOdId());
      odFileDTO.setPath(FileUtils.getFilePath(fullPathOld));
      odFileDTO.setFileName(FileUtils.getFileName(fullPathOld));
      ResultInSideDto resultFileDataOld = odRepository.insertOdFile(odFileDTO);
      //End save file old
      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setPath(fullPath);
      gnocFileDto.setFileName(fileName);
      gnocFileDto.setCreateUnitId(userToken.getDeptId());
      gnocFileDto.setCreateUnitName(unitToken.getUnitName());
      gnocFileDto.setCreateUserId(userToken.getUserID());
      gnocFileDto.setCreateUserName(userToken.getUserName());
      gnocFileDto.setCreateTime(odDTO.getCreateTime());
      gnocFileDto.setMappingId(resultFileDataOld.getId());
      gnocFileDtos.add(gnocFileDto);
    }
    try {
      if (GNOC_FILE_BUSSINESS.SR.equals(odDTO.getOtherSystemType())
          && odDTO.getOtherSystemId() != null) {
        GnocFileDto gnocFileSrDto = new GnocFileDto();
        gnocFileSrDto.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
        gnocFileSrDto.setBusinessId(odDTO.getOtherSystemId());
        List<GnocFileDto> gnocFileSrDtos = gnocFileRepository.getListGnocFileByDto(gnocFileSrDto);
        for (GnocFileDto gnocFileSrDtoAdd : gnocFileSrDtos) {
          if (StringUtils.isNotNullOrEmpty(gnocFileSrDtoAdd.getPath())) {
            String fileName = gnocFileSrDtoAdd.getFileName();
            byte[] bytes = FileUtils
                .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), gnocFileSrDtoAdd.getPath());
            String fullPath = FileUtils
                .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), ftpFolder, fileName, bytes,
                    odDTO.getCreateTime());
            //Start save file old
            String fullPathOld = FileUtils
                .saveUploadFile(fileName, bytes, uploadFolder, odDTO.getCreateTime());
            OdFileDTO odFileDTO = new OdFileDTO();
            odFileDTO.setOdId(odDTO.getOdId());
            odFileDTO.setPath(FileUtils.getFilePath(fullPathOld));
            odFileDTO.setFileName(FileUtils.getFileName(fullPathOld));
            ResultInSideDto resultFileDataOld = odRepository.insertOdFile(odFileDTO);
            //End save file old
            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setPath(fullPath);
            gnocFileDto.setFileName(fileName);
            gnocFileDto.setCreateUnitId(userToken.getDeptId());
            gnocFileDto.setCreateUnitName(unitToken.getUnitName());
            gnocFileDto.setCreateUserId(userToken.getUserID());
            gnocFileDto.setCreateUserName(userToken.getUserName());
            gnocFileDto.setCreateTime(odDTO.getCreateTime());
            gnocFileDto.setMappingId(resultFileDataOld.getId());
            gnocFileDtos.add(gnocFileDto);
          }
        }
      }
    } catch (Exception ex) {
      resultInSideDto.setKey(RESULT.ERROR);
      return resultInSideDto;
    }
    gnocFileRepository
        .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.OD, odDTO.getOdId(), gnocFileDtos);
    return resultInSideDto;
  }

  public void sendMessageCreateOd(OdDTO odDTO, UsersEntity u) {
    try {
      //nhan tin nhan vien tao
      String createSmsContent = replaceSmsContent(odDTO,
          getLang("2".equals(u.getUserLanguage()) ? new Locale("en_US") : null,
              "OdCreateContentSMS"));
      createMessage(u, createSmsContent);
    } catch (Exception e) {
      log.error("GNOC_OD: Co loi khi nhan tin cho nhan vien tao OD:" + odDTO.getOdCode(), e);
    }
    if (odDTO.getReceiveUnitId() != null) {
      List<UsersEntity> lstUs = odRepository.getListUserOfUnit(odDTO.getReceiveUnitId());
      for (UsersEntity i : lstUs) {
        try {
          String receiveUnitSmsContent = getLang(
              "2".equals(i.getUserLanguage()) ? new Locale("en_US") : null,
              "OdReceiveUnitSmsContent");
          receiveUnitSmsContent = replaceSmsContent(odDTO, receiveUnitSmsContent);
          createMessage(i, receiveUnitSmsContent);
        } catch (Exception e) {
          log.error("GNOC_OD: Co loi khi nhan tin cho nhan vien:" + i.getUsername(), e);
        }
      }
    }

  }

  public void sendMesseageUpdateOd(OdDTO odDTO, Long oldStatus) {
    try {
      OdChangeStatusDTO dtoS = new OdChangeStatusDTO(odDTO.getOdTypeId(), oldStatus,
          odDTO.getStatus(), odDTO.getPriorityId());
      List<OdChangeStatusDTO> lstChangeStatus = odCategoryServiceProxy.search(dtoS);
      OdChangeStatusDTO dtoChange = null;
      if (lstChangeStatus != null && lstChangeStatus.size() > 0) {
        dtoChange = lstChangeStatus.get(0);
      } else {
        dtoS.setOdTypeId(null);
        dtoS.setIsDefault(1L);
        lstChangeStatus = odCategoryServiceProxy.search(dtoS);
        if (lstChangeStatus != null && lstChangeStatus.size() > 0) {  // lay cau hinh mac dinh
          dtoChange = lstChangeStatus.get(0);
        }
      }
      if (dtoChange != null) {
        // nhan tin nhan vien tao
        try {
          if (dtoChange.getSendCreate() == 1 && !StringUtils
              .isStringNullOrEmpty(dtoChange.getCreateContent())) {
            UsersEntity usersEntity = odRepository.getUserByUserId(odDTO.getCreatePersonId());
            String createSmsContent = getSmsContenFromCfg(usersEntity,
                dtoChange.getCreateContent());
            createSmsContent = replaceSmsContent(odDTO, createSmsContent);
            createMessage(usersEntity, createSmsContent);
          }
        } catch (Exception e) {
          log.error("GNOC_OD: Co loi khi nhan tin cho nhan vien tao OD:" + odDTO.getOdCode(), e);
        }
        // nhan tin nhan vien thuc hien
        try {
          if (dtoChange.getSendReceiveUser() == 1
              && !StringUtils.isStringNullOrEmpty(dtoChange.getReceiveUserContent())
              && odDTO.getReceiveUserId() != null) {
            UsersEntity u = odRepository.getUserByUserId(odDTO.getReceiveUserId());
            String smsContent = getSmsContenFromCfg(u, dtoChange.getReceiveUserContent());
            smsContent = replaceSmsContent(odDTO, smsContent);
            createMessage(u, smsContent);
          }
        } catch (Exception e) {
          log.error("GNOC_OD: Co loi khi nhan tin cho nhan vien xu ly OD:" + odDTO.getOdCode(), e);
        }
        // nhan tin don vi thuc hien
        List<UsersEntity> lstUs = null;
        try {
          if (dtoChange.getSendReceiveUnit() == 1
              && !StringUtils.isStringNullOrEmpty(dtoChange.getReceiveUnitContent())
              && odDTO.getReceiveUnitId() != null) {
            lstUs = odRepository.getListUserOfUnit(odDTO.getReceiveUnitId());
          } else if (dtoChange.getSendReceiveUnit() == 2
              && !StringUtils.isStringNullOrEmpty(dtoChange.getReceiveUnitContent())
              && odDTO.getReceiveUnitId() != null) {
            lstUs = odRepository.getManagerReceiverUnit(odDTO.getReceiveUnitId());
          }
        } catch (Exception e) {
          log.error("GNOC_OD: Co loi khi nhan tin cho đơn vị thực hiện OD:" + odDTO.getOdCode(), e);
        }
        if (lstUs != null) {
          for (UsersEntity u : lstUs) {
            try {
              String smsContent = getSmsContenFromCfg(u, dtoChange.getReceiveUnitContent());
              smsContent = replaceSmsContent(odDTO, smsContent);
              createMessage(u, smsContent);
            } catch (Exception e) {
              log.error("GNOC_OD: Co loi khi nhan tin cho nhan vien:" + u.getUsername(), e);
            }
          }
        }
        // nhan tin nhan vien phe duyet
        try {
          if (dtoChange.getSendApprover() == 1 && !StringUtils
              .isStringNullOrEmpty(dtoChange.getApproverContent())
              && odDTO.getApproverId() != null) {
            UsersEntity usersEntity = odRepository.getUserByUserId(odDTO.getApproverId());
            String createSmsContent = getSmsContenFromCfg(usersEntity,
                dtoChange.getApproverContent());
            createSmsContent = replaceSmsContent(odDTO, createSmsContent);
            createMessage(usersEntity, "[GNOC_OD]" + createSmsContent);
          }
        } catch (Exception e) {
          log.error("GNOC_OD: Co loi khi nhan tin cho nhan vien phe duyet OD:" + odDTO.getOdCode(),
              e);
        }
      }
    } catch (Exception e) {
      log.error("GNOC_OD: Co loi khi nhan tin OD:" + odDTO.getOdCode(), e);
    }
  }


  public static String getLang(Locale locale, String key) {
    if (locale != null) {
      return I18n.getLanguageByLocale(locale, key);
    } else {
      return I18n.getLanguageByLocale(new Locale("vi", "VN"), key);
    }
  }

  public void createMessage(UsersEntity u, String smsContent) {
    MessagesDTO message = new MessagesDTO();
    message.setSmsGatewayId(smsGatewayId);  // fix code = 5
    message.setReceiverId(u.getUserId().toString());
    message.setReceiverUsername(u.getUsername());
    message.setReceiverFullName(u.getFullname());
    message.setSenderId(senderId);  // fix code = 400
    message.setReceiverPhone(u.getMobile());
    message.setStatus("0");
    message.setAlias("GNOC_OD");
    message.setCreateTime(
        DateTimeUtils.convertDateToString(new Date(), Constants.formatterDateTimeText));
    message.setContent(smsContent);
    messagesRepository.insertOrUpdateWfm(message);
  }

  public String replaceSmsContent(OdDTO odDTO, String content) {
    if (odDTO.getOdCode() != null) {
      content = content.replace("[odCode]", odDTO.getOdCode());
    }
    if (odDTO.getOdName() != null) {
      content = content.replace("[odName]", odDTO.getOdName());
    }
    if (odDTO.getCreateTime() != null) {
      content = content.replace("[createTime]", DateTimeUtils
          .convertDateToString(odDTO.getCreateTime(), Constants.formatterDateTimeText));
    }
    if (odDTO.getDescription() != null) {
      content = content.replace("[description]", odDTO.getDescription());
    }
    if (odDTO.getStartTime() != null) {
      content = content.replace("[startTime]",
          DateTimeUtils.convertDateToString(odDTO.getStartTime(), Constants.formatterDateTimeText));
    }
    if (odDTO.getEndTime() != null) {
      content = content.replace("[endTime]",
          DateTimeUtils.convertDateToString(odDTO.getEndTime(), Constants.formatterDateTimeText));
    }
    return content;
  }

  public String getSmsContenFromCfg(UsersEntity u, String content) {
    String[] c = content.split("####");
    if ("2".equals(u.getUserLanguage())) {
      return c.length == 2 ? c[1] : c[0];
    } else {
      return c[0];
    }
  }

  /*
      Nghiep vu bo sung khi cap nhat OD
       */
  public void doNextAction(OdDTO odDTO, UsersEntity user, Long oldStatus, Date now,
      Map<String, String> mapConfigProperty, String comment1) throws Exception {
    // lay danh sach nghiep vu tiep theo
    OdChangeStatusDTO cfg = getCfgBusiness(odDTO, oldStatus);
    if (cfg != null && cfg.getNextAction() != null) {
      String[] ne = cfg.getNextAction().split(",");
      for (int i = 0; i < ne.length; i++) {
        // dong WO
        if (ne[i].equalsIgnoreCase(Constants.OD_NEXT_ACTION.FORCE_CLOSED)) {
          String comment = "System autoClose";
          doCompleted(odDTO, user, odDTO.getStatus(), now, comment, null);
        }

        if (ne[i].equalsIgnoreCase(Constants.OD_NEXT_ACTION.CHECK_UNIT_IS_CREATE_UNIT)
            && odDTO.getReceiveUnitId() != null && odDTO.getCreateUnitId() != null) {
          if (odDTO.getReceiveUnitId().equals(odDTO.getCreateUnitId())) {
            String comment = "System autoClose";
            doCompleted(odDTO, user, odDTO.getStatus(), now, comment, null);
          }
        }

        if (ne[i].equalsIgnoreCase(Constants.OD_NEXT_ACTION.COMPLETE_NIMS_KDT_TK)) {
          Long resToNims = 0L;
          if (odDTO.getCloseCodeId() != null && odDTO.getCloseCodeId() == 1) {
            resToNims = 1L;
          }
          Long type = 1L;
          if (checkProperty(mapConfigProperty, odDTO.getOdTypeId().toString(),
              Constants.OD_TYPE_CODE.OD_TYPE_KDT_TK)
              || checkProperty(mapConfigProperty, odDTO.getOdTypeId().toString(),
              Constants.OD_TYPE_CODE.OD_TYPE_KDT_ND)
              || checkProperty(mapConfigProperty, odDTO.getOdTypeId().toString(),
              Constants.OD_TYPE_CODE.OD_TYPE_KDT_NP)) {
            type = Long.valueOf(Constants.AP_PARAM.NIMS_TYPE_STATION);

          } else if (checkProperty(mapConfigProperty, odDTO.getOdTypeId().toString(),
              Constants.OD_TYPE_CODE.OD_TYPE_UPDATE_VIBA)) {
            type = Long.valueOf(Constants.AP_PARAM.NIMS_TYPE_VIBA);
          }
          log.info("***Request doCompletedToNims ***" + odDTO);
          doCompletedToNims(odDTO, resToNims, type);
          log.info("***Result doCompletedToNims OK ***");
        }
        // check nims luong doc dao
        if (ne[i].equalsIgnoreCase(Constants.OD_NEXT_ACTION.CHECK_NIMS_DOC_DAO)) {
          log.info("***Request checkCloseOd ***" + odDTO.getOdCode());
          Boolean check = wsnimsHtPort.checkCloseOd(odDTO.getOdCode());
          log.info("***Result checkCloseOd ***" + check);
          if (check) { // neu dat dong luon od
            doCompleted(odDTO, user, odDTO.getStatus(), now, "Close after check NIMS Ok", null);
          } else { // neu ko dat giao OD ve trang thai dc cau hinh
            throw new Exception(" Bạn chưa nhập tuyến vu hồi trên NIMS");
          }
        }
        // cap nhat tuyen BKK
        if (ne[i].equalsIgnoreCase(Constants.OD_NEXT_ACTION.UPDATE_NIMS_DOC_DAO_BKK)) {
          log.info("***Request updateTuyenDocDao ***" + odDTO.getOdCode());
          Boolean check = wsnimsHtPort.updateTuyenDocDao(odDTO.getOdCode());
          log.info("***Result updateTuyenDocDao ***" + check);
          if (check) { // neu dat dong luon od
          } else { // neu ko dat giao OD ve trang thai dc cau hinh
            throw new Exception("Có lỗi khi cập nhật tuyến bất khả kháng sang NIMS");
          }
        }
        // cap nhat status
        if (ne[i].toUpperCase().contains(OD_NEXT_ACTION.CHANGE_STATUS_TO_X)) {
          if (mapOdStatus.size() == 0) {
            Datatable datatable = catItemRepository
                .getItemMaster(CATEGORY.OD_STATUS, MASTER_DATA.OD, MASTER_DATA.OD_STATUS,
                    Constants.ITEM_VALUE, Constants.ITEM_NAME);
            List<CatItemDTO> lstData = (List<CatItemDTO>) datatable.getData();
            for (CatItemDTO catItemDTO : lstData) {
              if (!mapOdStatus.containsKey(catItemDTO.getItemValue())) {
                mapOdStatus.put(catItemDTO.getItemValue(), catItemDTO.getItemName());
              }
            }
          }
          String[] lstStr = ne[i].split("_");
          String status = lstStr[lstStr.length - 1];
          if (StringUtils.isNotNullOrEmpty(status) && mapOdStatus.containsKey(status)) {
            // luu lich su
            odHistoryBusiness
                .insertOdHistory(odDTO, odDTO.getStatus(), Long.valueOf(status), comment1, user);
            odDTO.setStatus(Long.valueOf(status));
            odRepository.insertOrUpdate(odDTO);
            log.info(odDTO.getOdCode() + "***Change status to " + mapOdStatus.get(status) + " ***");
          }
        }
      }
    }
  }

  /*
    dong WO
     */
  public void doCompleted(OdDTO odDTO, UsersEntity user, Long oldStatus, Date now, String comment,
      String closeCodeId) throws Exception {
    odDTO.setCloseTime(now);
    odDTO.setStatus(Constants.OD_STATUS.CLOSE);
    if (!StringUtils.isStringNullOrEmpty(closeCodeId)) {
      odDTO.setCloseCodeId(Long.parseLong(closeCodeId));
    }
    odHistoryBusiness.insertOdHistory(odDTO, oldStatus, Constants.OD_STATUS.CLOSE, comment, user);
  }

  /*
    cap nhat trang thai nims luong kiem dinh tram goc
     */
  public void doCompletedToNims(OdDTO odDTO, Long status, Long type) throws Exception {
    try {
      InputUpdateStationAuditedStatusForm input = new InputUpdateStationAuditedStatusForm();
      input.setOrderCode(odDTO.getOdCode());
      input.setOrderStatus(status);
      input.setType(type);
//
      ResultUpdateStationAuditedStatusForm res = wsnimsHtPort.updateSubmittingOrderStatus(input);
      if (res == null) {
        throw new RuntimeException(I18n.getLanguage("haveSomeErrWhenCallNimsWS"));
      }
      if (!"OK".equals(res.getResult())) {
        throw new RuntimeException(res.getMessage());
      }
    } catch (Exception e) {
      log.error("[NIMS]" + e.getMessage(), e);
      throw new RuntimeException("[NIMS]" + e.getMessage());
    }
  }

  public Datatable getListStatusNext(Long odId, String userName) {
    try {
      UsersEntity users = odRepository.getUserByUserName(userName);
      return odRepository
          .getListStatusNext(odId, users.getUserId().toString(), users.getUnitId().toString());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public static Boolean checkProperty(Map<String, String> map, String id, String key) {
    try {
      String type = map.get(key);
      String[] arrType = type.split(",");
      List<String> lst = Arrays.asList(arrType);
      if (lst != null && lst.contains(id)) {
        return true;
      }
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return false;
    }
    return false;
  }

  public OdChangeStatusDTO getCfgBusiness(OdDTO odDTO, Long oldStatus) {
    try {
      OdChangeStatusDTO dtoS = new OdChangeStatusDTO();

      dtoS.setOdTypeId(odDTO.getOdTypeId());
      dtoS.setOldStatus(oldStatus);
      dtoS.setNewStatus(odDTO.getStatus());
      dtoS.setOdPriority(odDTO.getPriorityId());

      List<OdChangeStatusDTO> lstChangeStatus = odCategoryServiceProxy.search(dtoS);
      if (lstChangeStatus == null || lstChangeStatus.size() == 0) {
        dtoS.setIsDefault(1l);
        dtoS.setOdTypeId(null);
        lstChangeStatus = odCategoryServiceProxy.search(dtoS);
      }
      if (lstChangeStatus != null && lstChangeStatus.size() > 0) {
        return lstChangeStatus.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  private File exportFileEx(List<OdSearchInsideDTO> lstImport, String key) throws Exception {
    String title = "";
    String fileNameOut = "";
    String sheetName = "";
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet1;
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    columnSheet1 = new ConfigHeaderExport("odCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("odName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("statusName", "LEFT", false, 0, 0, new String[]{},
        null, "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("odTypeName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("priorityName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("createTime", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("remainTime", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("createUnitName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("receiveUnitName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("receiveUserName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = OD_RESULT_IMPORT;
      sheetName = OD_RESULT_IMPORT;
      title = OD_RESULT_IMPORT;
      lstHeaderSheet1.add(columnSheet1);
      columnSheet1 = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
    } else {
      fileNameOut = OD_EXPORT;
      title = I18n.getLanguage("od.titleExport") + " " + DateUtil.date2ddMMyyyyHHMMss(new Date());
      sheetName = OD_EXPORT;
    }
    lstHeaderSheet1.add(columnSheet1);
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configfileExport = new ConfigFileExport(
        lstImport
        , sheetName
        , ""
        , title
        , 7
        , 3
        , 9
        , true
        , "language.od"
        , lstHeaderSheet1
        , fieldSplit
        , ""
        , I18n.getLanguage("od.export.firstLeftHeader")
        , I18n.getLanguage("od.export.secondLeftHeader")
        , I18n.getLanguage("od.export.firstRightHeader")
        , I18n.getLanguage("od.export.secondRightHeader")
    );
    configfileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;

    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("od.export.stt"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
    fileExports.add(configfileExport);

 /*   String path = this.getClass().getClassLoader().getResource("").getPath();
    String fullPath = URLDecoder.decode(path, "UTF-8");*/
    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExports
        , rootPath
        , null
    );
    return fileExport;
  }

  public String validateAdd(OdDTO odDTO) {
    if (odDTO.getPlanCode() != null && odDTO.getPlanCode().trim().length() > 300) {
      return I18n.getValidation("wo.plan.code.300");
    }
    if (odDTO.getStartTime() == null) {
      return I18n.getValidation("wo.notify.fromDate");
    }
    if (odDTO.getEndTime() == null) {
      return I18n.getValidation("wo.notify.endDate");
    }
    if (odDTO.getStartTime().getTime() > odDTO.getEndTime().getTime()) {
      return I18n.getValidation("wo.notify.dateCompare");
    } else if (odDTO.getStartTime().getTime() + 300000 < new Date().getTime()) {
      return I18n.getValidation("wo.notify.dateNotLargeForNow");
    }

    List<WoTypeTimeDTO> lstTime = odRepository.getListWoTypeTimeDtosByWoTypeId(odDTO.getOdTypeId());
    if (lstTime != null && lstTime.size() > 0) {
      WoTypeTimeDTO woTypeTimeDTO = lstTime.get(0);
      if (woTypeTimeDTO.getDuration() != null) {
        Date stDate = odDTO.getEndTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(stDate);
        Double duration = woTypeTimeDTO.getDuration() * 24 * 60 * 60 * 1000;
        if (woTypeTimeDTO.getIsImmediate().equals(1L) && (cal.get(Calendar.HOUR_OF_DAY) > 17 || (
            cal.get(Calendar.HOUR_OF_DAY) == 17 && cal.get(Calendar.MINUTE) >= 30))) {
          Date tomorow = new Date(stDate.getTime() + 24 * 60 * 60 * 1000);
          cal.setTime(tomorow);
          cal.set(Calendar.HOUR_OF_DAY, 8);
          cal.set(Calendar.MINUTE, 0);
          cal.set(Calendar.SECOND, 0);
          stDate = cal.getTime();
        }
        Date enDate = new Date(stDate.getTime() + duration.longValue());
        if (odDTO.getEndTime().getTime() < enDate.getTime()) {
          return I18n.getValidation("wo.notify.dateLessThanConfig");
        }
      }
    }
    return "";
  }

  public String validateUpdate(OdDTO odDTO, List<MultipartFile> multipartFiles) {

      OdChangeStatusDTO odChangeStatusDTO = odCategoryServiceProxy
          .getOdChangeStatusDTOByParams(odDTO.getOldStatus().toString(), odDTO.getStatus().toString(),
              odDTO.getPriorityId().toString(),
              (odDTO.getOdTypeId() == null) ? null : odDTO.getOdTypeId().toString());
      List<OdCfgBusinessDTO> odCfgBusinessDTOS = null;
      if (odChangeStatusDTO == null) {
        return I18n.getValidation("od.notify.OdChangeStatusNull");
      } else {
        odCfgBusinessDTOS = odCategoryServiceProxy.getListOdCfgBusiness(odChangeStatusDTO);
        if (odCfgBusinessDTOS == null || odCfgBusinessDTOS.size() < 1) {
          return I18n.getValidation("od.notify.OdChangeStatusNull");
        }
      }
      Map<String, OdCfgBusinessDTO> mapColumn = new HashMap<>();
      if (odCfgBusinessDTOS != null) {
        for (OdCfgBusinessDTO odCfgBusinessDTO : odCfgBusinessDTOS) {
          mapColumn.put(odCfgBusinessDTO.getColumnName().toLowerCase(), odCfgBusinessDTO);
        }
      }
      OdCfgBusinessDTO odCfgBusinessDTO = null;
      if (mapColumn.containsKey("description")) {
        odCfgBusinessDTO = mapColumn.get("description");
        if (odCfgBusinessDTO.getIsRequired() != null && odCfgBusinessDTO.getIsRequired() == 1) {
          if (odDTO.getDescription() == null || odDTO.getDescription().trim().length() == 0) {
            return I18n.getValidation("wo.notify.odDescription.isNotNull");
          }
        }
      }
      //don vi xu ly
      if (mapColumn.containsKey("receiveunitid")) {
        odCfgBusinessDTO = mapColumn.get("receiveunitid");
        if (odCfgBusinessDTO.getIsRequired() != null && odCfgBusinessDTO.getIsRequired() == 1) {
          if (odDTO.getReceiveUnitId() == null) {
            return I18n.getValidation("wo.notify.receiveUnitIsNotNull");
          }
        }
      }
      //nhan vien xu ly
      if (mapColumn.containsKey("receiveuserid")) {
        odCfgBusinessDTO = mapColumn.get("receiveuserid");
        if (odCfgBusinessDTO.getIsRequired() != null && odCfgBusinessDTO.getIsRequired() == 1) {
          if (odDTO.getReceiveUserId() == null) {
            return I18n.getValidation("od.notify.receiveUserIsNotNull");
          }
        }
      }

      //ma Clear
      if (mapColumn.containsKey("clearcodeid")) {
        odCfgBusinessDTO = mapColumn.get("clearcodeid");
        if (odCfgBusinessDTO.getIsRequired() != null && odCfgBusinessDTO.getIsRequired() == 1) {
          if (odDTO.getClearCodeId() == null) {
            return I18n.getValidation("od.notify.clearCode.isNotNull");
          }
        }
      }

      //ma dong
      if (mapColumn.containsKey("closecodeid")) {
        odCfgBusinessDTO = mapColumn.get("closecodeid");
        if (odCfgBusinessDTO.getIsRequired() != null && odCfgBusinessDTO.getIsRequired() == 1) {
          if (odDTO.getCloseCodeId() == null) {
            return I18n.getValidation("od.notify.closeCode.isNotNull");
          }
        }
      }
      //thoi gian ket thuc tam dung
      if (mapColumn.containsKey("endpendingtime")) {
        odCfgBusinessDTO = mapColumn.get("endpendingtime");
        if (odCfgBusinessDTO.getIsRequired() != null && odCfgBusinessDTO.getIsRequired() == 1) {
          if (odDTO.getEndPendingTime() == null) {
            return I18n.getValidation("od.notify.endPendingTimeIsNotNull");
          }
        }
      }

      //file dinh kem
      if (mapColumn.containsKey("fileattach")) {
        odCfgBusinessDTO = mapColumn.get("fileattach");
        if (odCfgBusinessDTO.getIsRequired() != null && odCfgBusinessDTO.getIsRequired() == 1) {
          if ((odDTO.getGnocFileDtos() == null || odDTO.getGnocFileDtos().size() == 0) && (
              multipartFiles == null || multipartFiles.size() == 0)) {
            return I18n.getValidation("od.notify.fileAttachIsNotNull");
          }
        }
      }
    return "";
  }

  public String validateCommon(OdDTO odDTO) {
    if (odDTO.getOdName() == null || "".equals(odDTO.getOdName())) {
      return I18n.getValidation("wo.notify.woName");
    }
    if (odDTO.getOdName().length() > 750) {
      return I18n.getValidation("od.Content.750");
    }
    if (odDTO.getOdTypeId() == null) {
      return I18n.getValidation("wo.notify.woType");
    }
    if (odDTO.getPriorityId() == null) {
      return I18n.getValidation("wo.notify.priority");
    }
    return "";
  }

  public List<FileAttachTranfer> getListFilePlan(List<MultipartFile> files) {
    List<FileAttachTranfer> lst = new ArrayList<FileAttachTranfer>();
    try {
      if (files != null && files.size() > 0) {
        for (MultipartFile file : files) {
          FileAttachTranfer f = new FileAttachTranfer();
          f.setAttachBytes(file.getBytes());
          f.setFileName(file.getOriginalFilename());
          f.setFileSign(1);
          lst.add(f);
          break;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return lst;
  }

  public String getConfig(String key) throws Exception {
    return odRepository.getConfigPropertyValue(key);
  }

  public String convertWOStatusToString(String status) {
    if (StringUtils.isStringNullOrEmpty(status)) {
      return "";
    } else {
      return I18n.getLanguage("wo.status." + status);
    }
  }

  @Override
  public ConfigPropertyDTO getConfigPropertyOd(String key) {
    return odRepository.getConfigPropertyOd(key);
  }

  @Override
  public ResultDTO changeStatusOd(OdChangeStatusForm form) throws Exception {
    try {
      validateUpdateStatusForm(form);
      String odId = form.getOdCode()
          .substring(form.getOdCode().lastIndexOf("_") + 1, form.getOdCode().length());
      OdDTO od = odRepository.findOdById(Long.valueOf(odId));
      if (od == null && form.getOdId() != null) {
        od = odRepository.findOdById(Long.valueOf(form.getOdId()));
      }
      if (od != null) {
        Long oldStatus = od.getStatus();
        UsersEntity us = odRepository.getUserByUserName(form.getUserChange());
        if (us == null) {
          throw new Exception(I18n.getLanguage("wo.InvalidUserName") + form.getUserChange());
        }
        String comment =
            form.getSystemChange() + "_" + form.getUserChange() + ":" + form.getReasonChange();
//                // xoa ft khi chuyen ve cho cd tiep nhan hoac cd da tiep nhan
        if (Constants.OD_STATUS.CLOSE.equals(Long.valueOf(form.getStatus()))) {
          od.setStatus(Long.valueOf(form.getStatus()));
          od.setCloseTime(DateUtil.string2DateTime(form.getCloseTime()));
        }
        if ("1".equals(form.getIsClose())) {
          od.setStatus(Constants.OD_STATUS.CLOSE);
          od.setCloseTime(new Date());
          form.setStatus(Constants.OD_STATUS.CLOSE.toString());
        }
        if (form.getFinishTime() != null) {
          String msg = DataUtil.validateDateTimeDdMmYyyy_HhMmSs(form.getFinishTime());
          if ("".equalsIgnoreCase(msg)) {
            od.setFinishedTime(form.getFinishTime());
          } else {
            return new ResultDTO("0", RESULT.FAIL, I18n.getLanguage("od.FinishedTime.malformed"));
          }
        }
        od.setLastUpdateTime(new Date());
        odRepository.insertOrUpdate(od);
        odHistoryBusiness
            .insertOdHistory(od, oldStatus, Long.valueOf(form.getStatus()), comment, us);
      } else {
        throw new Exception(I18n.getLanguage("woIsNotExists"));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new Exception(e.getMessage());
    }
    return new ResultDTO("1", RESULT.SUCCESS, RESULT.SUCCESS);
  }

  public void validateUpdateStatusForm(OdChangeStatusForm form) throws Exception {
//    if (StringUtils.isStringNullOrEmpty(form.getCloseTime())) {
//      throw new Exception(I18n.getLanguage("finishTimeIsNotNull"));
//    }
//    if (!"".equals(DataUtil.validateDateTimeDdMmYyyy_HhMmSs(form.getCloseTime()))) {
//      throw new Exception(I18n.getLanguage("finishTimeIsNotValidFomat"));
//    }
    if (StringUtils.isStringNullOrEmpty(form.getOdCode())) {
      throw new Exception(I18n.getLanguage("OdCodeIsNotNull"));
    } else if (StringUtils.isStringNullOrEmpty(form.getUserChange())) {
      throw new Exception(I18n.getLanguage("userChangeIsNotNull"));
    } else if (StringUtils.isStringNullOrEmpty(form.getReasonChange())) {
      throw new Exception(I18n.getLanguage("reasonChangeIsNotNull"));
    } else if (StringUtils.isStringNullOrEmpty(form.getSystemChange())) {
      throw new Exception(I18n.getLanguage("systemChangeIsNotNull"));
    } else if (form.getStatus() == null || !DataUtil.isNumber(form.getStatus()) || (
        !Constants.OD_STATUS.CLOSE.equals(Long.valueOf(form.getStatus()))
            && !Constants.OD_STATUS.DEFERED.equals(Long.valueOf(form.getStatus()))
            && !Constants.OD_STATUS.NEW.equals(Long.valueOf(form.getStatus()))
            && !Constants.OD_STATUS.REJECT.equals(Long.valueOf(form.getStatus())))) {
      throw new Exception(I18n.getLanguage("newStatusIsNotValid"));
    } else if (Constants.OD_STATUS.CLOSE.equals(Long.valueOf(form.getStatus()))
        && StringUtils.isStringNullOrEmpty(form.getCloseTime())) {
      throw new Exception(I18n.getLanguage("finishTimeIsNotNull"));
    } else {
      try {
        DateUtil.string2DateTime(form.getCloseTime());
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        throw new Exception(I18n.getLanguage("finishTimeIsNotValidFomat"));
      }
    }
  }

  private Date convertServerTimeToClientTime(String serverTime, Double offSet) {
    Calendar cal = Calendar.getInstance();
    if (StringUtils.validString(serverTime)) {
      Date convertTime = DateTimeUtils
          .convertStringToDate(serverTime);
      cal.setTime(convertTime);
      cal.add(Calendar.HOUR_OF_DAY, offSet.intValue());
      return cal.getTime();
    }
    return null;
  }

  private OdRelationDTO getRDMRelationById(Long rdmId) {
    OdRelationDTO odRelationDTO = new OdRelationDTO();
    try {
      Gson gson = new Gson();
      Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();

      String urlRDM =
          mapConfigProperty.get("url_rdm") + "getListProjectByOdWS?timeZoneOffset=00:00";
      String urlParameters = "{\n"
          + "	\"page\": 1,\n"
          + "	\"pageSize\": 10,\n"
          + "\"projectCode\": \"" + rdmId + "\"\n"
          + "}";

      URL url = new URL(urlRDM);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      //add reuqest header
      conn.setRequestMethod("POST");
      conn.setRequestProperty("CLIENT_NAME", "API_WS");
      conn.setRequestProperty("content-type", "application/json;  charset=utf-8");

      // Send post request
      conn.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
      wr.writeBytes(urlParameters);
      wr.flush();
      wr.close();

      int responseCode = conn.getResponseCode();
      if (responseCode != 200) {
        throw new RuntimeException("Failed : RDM error code : " + conn.getResponseCode());
      }

      BufferedReader input = new BufferedReader(
          new InputStreamReader(conn.getInputStream(), "UTF-8"));
      StringBuilder sb = new StringBuilder();
      String output;
      while ((output = input.readLine()) != null) {
        sb.append(output).append("\r\n");
      }
      if (sb != null) {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(sb.toString(), Map.class);
        if (map != null && map.get("data") != null) {
          for (Object obj : (List<Object>) map.get("data")) {
            LinkedHashMap<String, String> temp = (LinkedHashMap<String, String>) obj;
            odRelationDTO.setContent(temp.get("content"));
            if (StringUtils.validString(temp.get("startDate"))) {
              odRelationDTO.setCreateTime(DateTimeUtils.convertStringToDate(temp.get("startDate")));
            }
            if (StringUtils.validString(temp.get("endDate"))) {
              odRelationDTO.setEndTime(DateTimeUtils.convertStringToDate(temp.get("endDate")));
            }
            odRelationDTO.setSystem("RDM");
            odRelationDTO.setSystemId(Long.parseLong(temp.get("projectId")));
            odRelationDTO.setReceiveUnitName(temp.get("unitName"));
            odRelationDTO.setStatus(temp.get("statusName"));
            odRelationDTO.setCreatePersonName(temp.get("createUser"));
            odRelationDTO.setCreatePersonId(
                commonRepository.getUserByUserName(temp.get("createUser")).getUserId());
            odRelationDTO.setSystemCode(temp.get("projectCode"));
          }
        }

      }
      conn.disconnect();
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return odRelationDTO;
  }

  private void checkNextActionFinishTime(OdDTO odDTO, UsersEntity user, Long oldStatus)
      throws Exception {
    OdChangeStatusDTO cfg = getCfgBusiness(odDTO, oldStatus);
    if (cfg != null && cfg.getNextAction() != null) {
      String[] nextAction = cfg.getNextAction().split(",");
      for (String s : nextAction) {
        if (s.equalsIgnoreCase(Constants.OD_NEXT_ACTION.CHECK_FINISHED_TIME)
            && odDTO.getFinishedTime() == null) {
          throw new Exception(
              "Bạn không được phép chuyển sang trạng thái này khi OD chưa có thời gian hoàn thành");
        }
      }
    }
  }

  @Override
  public ResultInSideDto updateOdOtherSystem(OdDTO odDTO) {
    return odRepository.updateOdOtherSystem(odDTO);
  }

  @Override
  public ResultDTO insertOdFromOtherSystem(OdDTOSearch odDTO) {
    ResultDTO result = new ResultDTO();
    result.setKey(RESULT.FAIL);
    //start time
    if (StringUtils.isStringNullOrEmpty(odDTO.getStartTime())) {
      result.setMessage(I18n.getLanguage("od.StartTime.isNotNull"));
      return result;
    } else if (!"".equals(DataUtil.validateDateTimeDdMmYyyy_HhMmSs(odDTO.getStartTime()))) {
      result.setMessage(I18n.getLanguage("od.StartTime.invalidFomat"));
      return result;
    }
    if (!StringUtils.isStringNullOrEmpty(odDTO.getEndTime()) && !""
        .equals(DataUtil.validateDateTimeDdMmYyyy_HhMmSs(odDTO.getEndTime()))) {
      result.setMessage(I18n.getLanguage("od.EndTime.invalidFomat"));
      return result;
    }
    // create time
    if (StringUtils.isStringNullOrEmpty(odDTO.getCreateTime())) {
      result.setMessage(I18n.getLanguage("od.CreateTime.isNotNull"));
      return result;
    } else if (!"".equals(DataUtil.validateDateTimeDdMmYyyy_HhMmSs(odDTO.getCreateTime()))) {
      result.setMessage(I18n.getLanguage("od.CreateTime.invalidFomat"));
      return result;
    }
    OdSearchInsideDTO odSearchInsideDTO = odDTO.toOdSearchInsideDTO();
    try {
      result = odCommonBusiness.insertOdFromOtherSystem(odSearchInsideDTO);
      if (result != null && RESULT.SUCCESS.equals(result.getKey()) && StringUtils
          .isNotNullOrEmpty(result.getId())) {
        String[] arrOdId = result.getId().split("_");
        if (arrOdId != null && arrOdId.length > 0) {
          OdDTO dto = findOdById(Long.valueOf(arrOdId[arrOdId.length - 1]));
          List<OdDTO> lstOd = new ArrayList<>();
          lstOd.add(dto);
          result.setLstResult(lstOd);
        }
      }
      return result;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result.setKey("");
      result.setMessage(e.getMessage());
      return result;
    }
  }

  @Override
  public OdTypeDTO getInforByODType(String odTypeCode) {
    return odCategoryServiceProxy.getInforByODType(odTypeCode);
  }
}
