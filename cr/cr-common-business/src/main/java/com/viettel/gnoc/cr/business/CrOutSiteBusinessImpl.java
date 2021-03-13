package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.business.CommonBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.cr.dto.CrAffectedNodesDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.model.ImpactSegmentEntity;
import com.viettel.gnoc.cr.repository.CrGeneralRepository;
import com.viettel.gnoc.cr.repository.CrRepository;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.security.PassTranformer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.xml.security.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class CrOutSiteBusinessImpl implements CrOutSiteBusiness {

  @Value("${application.conf.user_service:null}")
  private String userName;

  @Value("${application.conf.pass_service:null}")
  private String password;

  @Value("${application.conf.salt_service:null}")
  private String saltConf;

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

  @Autowired
  CrFileAttachBusiness crFileAttachBusiness;

  @Autowired
  CrBusiness crBusiness;

  @Autowired
  CommonBusiness commonBusiness;

  @Autowired
  UserRepository userRepository;

  @Autowired
  CrProcessBusiness crProcessBusiness;

  @Autowired
  CrGeneralBusiness crGeneralBusiness;

  @Autowired
  CatLocationBusiness catLocationBusiness;

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  CrGeneralRepository crGeneralRepository;

  @Autowired
  CrRepository crRepository;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Override
  public ResultDTO insertAutoCr(CrDTO crDTO, List<CrFilesAttachDTO> lstFile, String system,
      String nationCode, String lstFtId, String userService, String passService) {
    ResultDTO rs = new ResultDTO();

    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (userName.equals(user) && password.equals(pass)) {
        String validate = validate(crDTO, nationCode, system, lstFtId);
        if (!"".equals(validate)) {
          rs.setMessage(validate);
          rs.setKey(Constants.CR_RETURN_MESSAGE.ERROR);
        } else {
          validate = validateAndSaveFileAttach(lstFile, crDTO);
          if (!"".equals(validate)) {
            rs.setMessage(validate);
            rs.setKey(Constants.CR_RETURN_MESSAGE.ERROR);
          } else {
            //luu thong tin db GNOC
            String result = crFileAttachBusiness.insertListNoID(convertCrFileAttachDTO(lstFile));
            if (result != null && result.equals(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
              String woCode = "";
              if (!"VMSA".equalsIgnoreCase(system)) {
                woCode = createWO(crDTO, lstFtId);
              }
              //luu Cr
              rs = crBusiness.createObject(crDTO.toModelInsiteDTO()).toResultDTO();
              if (!"VMSA".equalsIgnoreCase(system) && !"SUCCESS"
                  .equals(rs.getMessage().toUpperCase())) {
                //goi WO
                WoDTOSearch dTOSearch = new WoDTOSearch();
                if (!StringUtils.isStringNullOrEmpty(crDTO.getCrId())) {
                  dTOSearch.setUserId("256066");
                  dTOSearch.setWoSystem("CR");
                  dTOSearch.setWoSystemId(crDTO.getCrId());
                  dTOSearch.setPage(1);
                  dTOSearch.setPageSize(Integer.MAX_VALUE);
                  List<WoDTOSearch> lstWo = woServiceProxy.getListDataSearchProxy(dTOSearch);
                  if (lstWo != null && !lstWo.isEmpty()) {
                    for (WoDTOSearch search : lstWo) {
                      woServiceProxy
                          .deleteWOForRollbackProxy(search.getWoCode(), "rollback CR", "CR");
                    }
                  }
                }
              }
              rs.setId(woCode);
            } else {
              rs.setKey(result);
            }
//            getRequest().setAttribute("durationDB", LogDBHandler.getDbTime());
          }
        }

      } else {
        rs.setMessage(I18n.getLanguage("crmobile.alert.wrongaccount"));
        rs.setKey(Constants.CR_RETURN_MESSAGE.ERROR);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      rs.setMessage(ex.getMessage());
      //xoa CR
      crBusiness.delete(Long.parseLong(crDTO.getCrId()));
      //goi WO
      WoDTOSearch dTOSearch = new WoDTOSearch();
      if (!"VMSA".equalsIgnoreCase(system) && !StringUtils.isStringNullOrEmpty(crDTO.getCrId())) {
        dTOSearch.setUserId("256066");
        dTOSearch.setWoSystemId(crDTO.getCrId());
        dTOSearch.setWoSystem("CR");

        dTOSearch.setPage(1);
        dTOSearch.setPageSize(Integer.MAX_VALUE);
        List<WoDTOSearch> lstWo = woServiceProxy.getListDataSearchProxy(dTOSearch);
        if (lstWo != null && !lstWo.isEmpty()) {
          for (WoDTOSearch search : lstWo) {
            woServiceProxy.deleteWOForRollbackProxy(search.getWoCode(), "rollback CR", "CR");
          }
        }
      }
    }
    return rs;
  }

  @Override
  public String actionCloseAutoCr(CrDTO cr, String userService, String passService) {
    String result;
    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (userName.equals(user) && password.equals(pass)) {
        if (cr == null || cr.getCrId() == null) {
          return "crId " + I18n.getChangeManagement("cr.msg.must.be.not.null");
        }
        CrInsiteDTO crDTO = crBusiness.getCrById(Long.parseLong(cr.getCrId()), null);
        crDTO.setFailDueToFT("0");
        crDTO.setIsClickedNode("0");
        crDTO.setIsClickedNodeAffected("0");
        crDTO.setIsClickedToAlarmTag(0);
        crDTO.setIsClickedToModuleTag(0);
        crDTO.setIsClickedToVendorTag(0);
        crDTO.setActionReturnCodeId("42");
        crDTO.setActionRight("8");
        crDTO.setActionType("25");
        crDTO.setCrReturnCodeId("27");
        crDTO.setUserLogin("256066");//system
        crDTO.setUserLoginUnit("447917");//unit
        crDTO.setNotes(crDTO.getNotes() + " \n\r actionCloseAutoCr");

        result = crBusiness.actionCloseCr(crDTO, I18n.getLocale());
//        getRequest().setAttribute("durationDB", LogDBHandler.getDbTime());
      } else {
        return I18n.getChangeManagement("crmobile.alert.wrongaccount");
      }

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      result = ex.getMessage();
    }
    return result;
  }

  @Override
  public String actionResolveAutoCr(CrDTO cr, String userService, String passService) {
    String result;
    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (userName.equals(user) && password.equals(pass)) {
        if (cr == null || cr.getCrId() == null) {
          return "crId " + I18n.getChangeManagement("cr.msg.must.be.not.null");
        }
        CrInsiteDTO crDTO = crBusiness.getCrById(Long.parseLong(cr.getCrId()), null);
        crDTO.setFailDueToFT("0");
        crDTO.setIsClickedNode("0");
        crDTO.setIsClickedNodeAffected("0");
        crDTO.setIsClickedToAlarmTag(0);
        crDTO.setIsClickedToModuleTag(0);
        crDTO.setIsClickedToVendorTag(0);
        crDTO.setActionReturnCodeId("42");
        crDTO.setActionRight("7");
        crDTO.setActionType("24");
        crDTO.setCrReturnCodeId("39");
        crDTO.setCrReturnResolve("27");
        crDTO.setResolveCodeId("39");
        crDTO.setNotes(crDTO.getNotes() + " \n\r actionResolveAutoCr");
        crDTO.setUserLogin("256066");//system
        crDTO.setUserLoginUnit("447917");//unit

        result = crBusiness.actionResolveCr(crDTO, I18n.getLocale());
//        getRequest().setAttribute("durationDB", LogDBHandler.getDbTime());
      } else {
        return I18n.getLanguage("crmobile.alert.wrongaccount");
      }

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      result = ex.getMessage();
    }
    return result;

  }

  @Override
  public String getCrNumber(String crProcessId, String userService, String passService) {
    try {
      String user = PassProtector.encrypt(userService, saltConf); //cr_service
      String pass = PassProtector.encrypt(passService, saltConf);
      if (userName.equals(user) && password.equals(pass)) {
        List<String> lst = crBusiness.getSequenseCr("CR_SEQ", 1);
        if (StringUtils.isStringNullOrEmpty(crProcessId)) {
          return "CrProcessId " + I18n.getChangeManagement("cr.msg.must.be.not.null");
        }

        CrProcessInsideDTO crProcess = crProcessBusiness
            .findCrProcessById(Long.parseLong(crProcessId));
        String crType = "0".equalsIgnoreCase(String.valueOf(crProcess.getCrTypeId())) ? "NORMAL"
            : "1".equalsIgnoreCase(String.valueOf(crProcess.getCrTypeId())) ? "EMERGENCY"
                : "2".equalsIgnoreCase(String.valueOf(crProcess.getCrTypeId())) ? "STANDARD" : "";
        ImpactSegmentEntity segmentDTO = crGeneralRepository
            .findImpactSegmentById(crProcess.getImpactSegmentId());

        return "CR_" + crType + "_" + segmentDTO.getImpactSegmentCode() + "_" + lst.get(0);
      } else {
        return I18n.getChangeManagement("crmobile.alert.wrongaccount");

      }

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

  public String validate(CrDTO crDTO, String nationCode, String system, String lstFtId)
      throws Exception {
    String result = "";

    if (StringUtils.isStringNullOrEmpty(system)) {
      return "System " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }

    if (!"VMSA".equalsIgnoreCase(system) && StringUtils.isStringNullOrEmpty(lstFtId)) {
      return "FtId " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }
    if (StringUtils.isNotNullOrEmpty(lstFtId)) {
      String ftId[] = lstFtId.split(",");
      if (ftId != null && ftId.length > 0) {
        for (String ft : ftId) {
          UsersEntity usersEntity = userRepository.getUserByUserName(ft);
          if (usersEntity == null || usersEntity.getUserId() == null) {
            return "ft not exist";
          }
        }
      } else {
        return "FtId " + I18n.getChangeManagement("cr.msg.must.be.not.null");
      }
    }

    if (StringUtils.isStringNullOrEmpty(crDTO.getCrId())) {
      return "CrId " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }

    if (StringUtils.isStringNullOrEmpty(crDTO.getCrNumber())) {
      return "CrNumber " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }

    if (StringUtils.isStringNullOrEmpty(crDTO.getState())) {
      return "State " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }
    if ("12".equals(crDTO.getState())) {
      if (StringUtils.isStringNullOrEmpty(crDTO.getUserCab())) {
        return "UserCab " + I18n.getChangeManagement("cr.msg.must.be.not.null");
      }
      UsersDTO usersDTO = crRepository.getUserInfo(crDTO.getUserCab());
      if (usersDTO == null || StringUtils.isStringNullOrEmpty(usersDTO.getUserId())) {
        return "UserCab " + I18n.getChangeManagement("qltn.userNotExist");
      }
      crDTO.setUserCab(String.valueOf(usersDTO.getUserId()));
    }

    if (crDTO.getLstNetworkNodeId() != null && crDTO.getLstNetworkNodeId().size() > 1000L) {
      return I18n.getChangeManagement("cr.msg.node.over") + 1000L;
    }
    if (crDTO.getLstNetworkNodeIdAffected() != null
        && crDTO.getLstNetworkNodeIdAffected().size() > 1000L) {
      return I18n.getChangeManagement("cr.msg.node.over") + 1000L;
    }

    if (StringUtils.isStringNullOrEmpty(crDTO.getTitle())) {
      return "Title " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }
    if (!StringUtils.checkMaxlength(255L, crDTO.getTitle())) {
      return "Title " + String.format(I18n.getChangeManagement("cr.msg.must.be.length.over"), 255);

    }
    if (StringUtils.isStringNullOrEmpty(crDTO.getDescription())) {
      return "Description " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }
    if (!StringUtils.checkMaxlength(2000L, crDTO.getDescription())) {
      return "Description " + String
          .format(I18n.getChangeManagement("cr.msg.must.be.length.over"), 2000);
    }

    if (StringUtils.isStringNullOrEmpty(crDTO.getCrProcessId())) {
      return "CrProcessId " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }

    if (StringUtils.isStringNullOrEmpty(crDTO.getSubcategoryId())) {
      return "SubcategoryId " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }
    crDTO.setSubcategory(crDTO.getSubcategoryId());
    if (StringUtils.isStringNullOrEmpty(crDTO.getPriority())) {
      return "Priority " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }

    CrProcessInsideDTO crProcess = crProcessBusiness
        .findCrProcessById(Long.parseLong(crDTO.getCrProcessId()));
    crDTO.setCrType(crProcess.getCrTypeId().toString());
    crDTO.setRisk(crProcess.getRiskLevel().toString());
    crDTO.setImpactSegment(crProcess.getImpactSegmentId().toString());
    crDTO.setDeviceType(crProcess.getDeviceTypeId().toString());
    crDTO.setProcessTypeId(crProcess.getCrProcessId().toString());
    crDTO.setDutyType(crProcess.getImpactType().toString());
    crDTO.setTotalAffectedCustomers("0");
    crDTO.setTotalAffectedMinutes("0");
    crDTO.setServiceAffecting("0");//ko anh huong
    crDTO.setImpactAffect("142");//muc do trung binh
    crDTO.setRelateCr("0");//ko co Cr lien quan
    crDTO.setCreatedDate(DateTimeUtils.getSysDateTime());

    if (StringUtils.isStringNullOrEmpty(crDTO.getEarliestStartTime())) {
      return "EarliestStartTime " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }
    if (StringUtils.isStringNullOrEmpty(crDTO.getLatestStartTime())) {
      return "LatestStartTime " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }

    String temp = validateTime(crDTO, crProcess.getImpactType());
    if (!"".equals(temp)) {
      return temp;
    }

    if (!StringUtils.isStringNullOrEmpty(crDTO.getChangeResponsible())) {
      UsersDTO udto = crRepository.getUserInfo(crDTO.getChangeResponsible());
      if (udto != null && udto.getUserId() != null) {
      } else {
        return "ChangeResponsible not exist";
      }
      crDTO.setChangeResponsible(String.valueOf(udto.getUserId()));
      crDTO.setChangeResponsibleUnit(String.valueOf(udto.getUnitId()));

    } else if (StringUtils.isStringNullOrEmpty(crDTO.getChangeResponsibleUnit())) {
      return "ChangeResponsibleUnit " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }

    if (StringUtils.isStringNullOrEmpty(crDTO.getChangeOrginator())) {
      Map<String, String> mapConfigProperty = commonBusiness.getConfigProperty();
      String crUserOrg = mapConfigProperty.get("cr_user_org");
      crDTO.setChangeOrginator(crUserOrg);

      String crUnitOrg = mapConfigProperty.get("cr_unit_org");
      crDTO.setChangeOrginatorUnit(crUnitOrg);
    } else {
      UsersDTO udto = crRepository.getUserInfo(crDTO.getChangeOrginator());
      if (udto != null && udto.getUserId() != null) {
      } else {
        return "Users creating CR does not exist";
      }
      crDTO.setChangeOrginator(String.valueOf(udto.getUserId()));
      crDTO.setChangeOrginatorUnit(String.valueOf(udto.getUnitId()));

      crDTO.setUserLogin(String.valueOf(udto.getUserId()));
      crDTO.setUserLoginUnit(String.valueOf(udto.getUnitId()));
    }

    if (crDTO.getNotes() != null && !StringUtils.checkMaxlength(1000L, crDTO.getNotes())) {
      return "Notes " + String.format(I18n.getChangeManagement("cr.msg.must.be.length.over"), 1000);
    }

    if (StringUtils.isStringNullOrEmpty(crDTO.getCountry())) {
      return "Country " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }
    List<ConditionBean> lstCondition = new ArrayList<>();
    lstCondition.add(new ConditionBean("locationCode", crDTO.getCountry(), "NAME_EQUAL", "STRING"));
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    List<CatLocationDTO> lstLocation = catLocationBusiness
        .searchByConditionBean(lstCondition, 0, 1, "", "");
    if (lstLocation != null && !lstLocation.isEmpty()) {
      crDTO.setCountry(lstLocation.get(0).getLocationId());
    } else {
      return I18n.getChangeManagement("cr.trace.countryInvalid");
    }

    if ("281".equals(crDTO.getCountry()) && StringUtils.isStringNullOrEmpty(crDTO.getRegion())) {
      return "Region " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }
    if (!StringUtils.isStringNullOrEmpty(crDTO.getRegion())) {
      lstCondition = new ArrayList<>();
      lstCondition
          .add(new ConditionBean("locationCode", crDTO.getRegion(), "NAME_EQUAL", "STRING"));
      lstCondition.add(new ConditionBean("parentId", crDTO.getCountry(), "NAME_EQUAL", "NUMBER"));
      ConditionBeanUtil.sysToOwnListCondition(lstCondition);
      lstLocation = catLocationBusiness.searchByConditionBean(lstCondition, 0, 1, "", "");
      if (lstLocation != null && !lstLocation.isEmpty()) {
        crDTO.setRegion(lstLocation.get(0).getLocationId());
      } else {
        return I18n.getChangeManagement("cr.trace.regionInvalid");
      }
    }
    temp = validateDeviceIp(crDTO, nationCode);
    if (!"".equals(temp)) {
      return temp;
    }

    return result;
  }

  public String validateAndSaveFileAttach(List<CrFilesAttachDTO> lstFilesAttach, CrDTO crDTO)
      throws Exception {
    String result = "";

    if (lstFilesAttach != null && !lstFilesAttach.isEmpty()) {
      for (CrFilesAttachDTO attachDTO : lstFilesAttach) {
        if (StringUtils.isStringNullOrEmpty(attachDTO.getFileContent())) {
          return "File content " + I18n.getChangeManagement("cr.msg.must.be.not.null");
        }

        if (StringUtils.isStringNullOrEmpty(attachDTO.getFileType())) {
          return "File type " + I18n.getChangeManagement("cr.msg.must.be.not.null");
        }

        if (StringUtils.isStringNullOrEmpty(attachDTO.getFileName())) {
          return "File name " + I18n.getChangeManagement("cr.msg.must.be.not.null");
        }
      }
      for (CrFilesAttachDTO attachDTO : lstFilesAttach) {
        String fileName = attachDTO.getFileName();
        byte[] fileContent = Base64.decode(attachDTO.getFileContent());
        String fullPath = FileUtils.saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
            PassTranformer.decrypt(ftpPass), ftpFolder, fileName, fileContent, null);
        attachDTO.setFileName(FileUtils.getFileName(fullPath));
        attachDTO.setFilePath(fullPath);
        attachDTO.setTimeAttack(DateUtil.date2ddMMyyyyHHMMss(new Date()));
        attachDTO.setCrId(crDTO.getCrId());
      }
    } else {
      return "File attach " + I18n.getChangeManagement("cr.msg.must.be.not.null");
    }

    return result;
  }

  public List<CrFilesAttachInsiteDTO> convertCrFileAttachDTO(
      List<CrFilesAttachDTO> crFilesAttachDTOS) {
    if (crFilesAttachDTOS == null) {
      return null;
    }
    List<CrFilesAttachInsiteDTO> lstInsite = new ArrayList<>();
    for (CrFilesAttachDTO dto : crFilesAttachDTOS) {
      lstInsite.add(dto.toModelOutSide());
    }
    return lstInsite;
  }

  public String createWO(CrDTO crDTO, String lstFtId) throws Exception {
    WoDTO woDTO = new WoDTO();
    woDTO.setWoSystem("CR");
    woDTO.setWoSystemId(crDTO.getCrId());
    woDTO.setCreatePersonId(crDTO.getChangeOrginator());

    Map<String, String> mapConfigProperty = commonBusiness.getConfigProperty();
//    Type type = new TypeToken<Map<String, String>>() {
//    }.getType();
//    Gson gson = new Gson();
//    Map<String, String> mapConfigProperty = gson.fromJson(strConfigProperty, type);

    woDTO.setWoTypeId(String.valueOf(mapConfigProperty.get("cr_wo_type")));

    woDTO.setWoContent(crDTO.getCrId() + " " + crDTO.getTitle());
    woDTO.setWoDescription(crDTO.getTitle());
    Date sysDate = new Date();
    woDTO.setStartTime(crDTO.getEarliestStartTime());
    woDTO.setEndTime(crDTO.getLatestStartTime());

    woDTO.setCreateDate(DateUtil.date2ddMMyyyyHHMMss(sysDate));
    woDTO.setPriorityId(mapConfigProperty.get("cr_wo_priority"));//muc uu tien
    String woCode = "";
    String ftId[] = lstFtId.split(",");
    for (String ft : ftId) {
      woDTO.setFtId(ft);
      ResultDTO result = woServiceProxy.createWoProxy(woDTO);
      if (!"SUCCESS".equalsIgnoreCase(result.getKey())) {
        log.error(result.getKey(), new Exception(result.getMessage()));
      }
      woCode = woCode + "," + result.getId();
    }

    //luu WO
    return woCode;
  }

  public String validateTime(CrDTO crDTO, Long dutyType) {

    Date startDate = null;
    Date endDate = null;
    try {
      if (!StringUtils.isStringNullOrEmpty(crDTO.getEarliestStartTime())) {
        startDate = DateTimeUtils
            .convertStringToTime(crDTO.getEarliestStartTime(), DateTimeUtils.patternDateTime);
      }
      if (!StringUtils.isStringNullOrEmpty(crDTO.getEarliestStartTime())) {
        endDate = DateTimeUtils
            .convertStringToTime(crDTO.getLatestStartTime(), DateTimeUtils.patternDateTime);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (startDate != null) {
      if (startDate.compareTo(new Date()) < 0) {
        return "EarliestStartTime " + I18n.getChangeManagement("msg.must.be.greater.than") + " "
            + I18n.getChangeManagement("cr.sysdate");
      }
    }
    if (endDate != null) {
      if (endDate.compareTo(startDate) < 0) {
        return "LatestStartTime " + I18n.getChangeManagement("msg.must.be.greater.than")
            + " EarliestStartTime";
      }
    }
    CrImpactFrameInsiteDTO form = new CrImpactFrameInsiteDTO();
    form.setImpactFrameId(dutyType);
    List<ItemDataCRInside> lstFrame = crGeneralBusiness.getListDutyTypeCBB(form);
    ItemDataCRInside dataCR = lstFrame.get(0);
    String[] startendarray = dataCR.getSecondValue().split(",");
    if (startendarray.length > 1) {
      String[] startDuty = startendarray[0].split(":");
      String[] endDuty = startendarray[1].split(":");
      if (startDuty.length > 2 && endDuty.length > 2) {
        Calendar startDutyCal = Calendar.getInstance();
        startDutyCal.clear();
        startDutyCal.setTime(startDate);
        if (startDate != null) {
          startDutyCal.set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate(),
              Integer.valueOf(startDuty[0]), Integer.valueOf(startDuty[1]),
              Integer.valueOf(startDuty[2]));
        }
        Date startDutyDate = startDutyCal.getTime();
        Calendar endDutyCal = Calendar.getInstance();
        endDutyCal.clear();
        if (startDate != null) {
          endDutyCal.set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate(),
              Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
              Integer.valueOf(endDuty[2]));
        }
        Date endDutyDate;
        if (Integer.valueOf(startDuty[0]) > Integer.valueOf(endDuty[0])) {//tac dong dem
          Calendar startDutyCalCheck = Calendar.getInstance();
          startDutyCalCheck.clear();
          if (startDate != null) {
            startDutyCalCheck
                .set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate(),
                    0, 0, 0);
          }
          Date checkstartDutyDate = startDutyCalCheck.getTime();
          Calendar endDutyCalCheck = startDutyCalCheck;
          endDutyCalCheck.clear();
          if (endDate != null) {
            endDutyCalCheck.set(endDate.getYear() + 1900, endDate.getMonth(), endDate.getDate(),
                0, 0, 0);
          }
          Date checkendDutyDate = endDutyCalCheck.getTime();//1445014800000 | 1445014800000
          if (startDate != null) {
            if (endDate != null && checkstartDutyDate.equals(checkendDutyDate)) {//Cung ngay
              if (endDate.getHours() <= (Integer.valueOf(endDuty[0]) + 1)) {//sang hom sau (00h-5h)
                startDutyCal
                    .set(endDate.getYear() + 1900, endDate.getMonth(), endDate.getDate() - 1,
                        Integer.valueOf(startDuty[0]), Integer.valueOf(startDuty[1]),
                        Integer.valueOf(startDuty[2]));
                startDutyDate = startDutyCal.getTime();
                endDutyCal.set(endDate.getYear() + 1900, endDate.getMonth(), endDate.getDate(),
                    Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                    Integer.valueOf(endDuty[2]));
//                            endDutyDate = endDutyCal.getTime();
              } else {//dem hom truoc (23h-24h)
                startDutyCal
                    .set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate(),
                        Integer.valueOf(startDuty[0]), Integer.valueOf(startDuty[1]),
                        Integer.valueOf(startDuty[2]));
                startDutyDate = startDutyCal.getTime();
                endDutyCal
                    .set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate() + 1,
                        Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                        Integer.valueOf(endDuty[2]));
//                            endDutyDate = endDutyCal.getTime();
              }
            } else {
              endDutyCal
                  .set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate() + 1,
                      Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                      Integer.valueOf(endDuty[2]));
//                        endDutyDate = endDutyCal.getTime();
            }
          }
        }
//        endDutyCal.add(Calendar.MINUTE, 1);
        endDutyDate = endDutyCal.getTime();
        if (startDate != null && endDate != null) {
          if (startDate.compareTo(startDutyDate) < 0
              || endDate.compareTo(endDutyDate) > 0) {
            return I18n.getChangeManagement("cr.msg.timecr.not.in.duty.date");
          }
        }
      }
    }
    return "";
  }

  public String validateDeviceIp(CrDTO crDTO, String nationCode) throws Exception {
    if (crDTO.getLstNetworkNodeId() == null || crDTO.getLstNetworkNodeId().isEmpty()) {
      return "";
    }
    String impact = "";
    List<CrImpactedNodesDTO> lstImpact = new ArrayList<CrImpactedNodesDTO>();
    if (!crDTO.getLstNetworkNodeId().isEmpty()) {
      for (CrImpactedNodesDTO impactedNodesDTO : crDTO.getLstNetworkNodeId()) {
        if (!StringUtils.isStringNullOrEmpty(impactedNodesDTO.getIp())) {
          InfraDeviceDTO infraDeviceDTO = new InfraDeviceDTO();
          infraDeviceDTO.setIp(impactedNodesDTO.getIp());
          infraDeviceDTO.setNationCode(nationCode);
          List<InfraDeviceDTO> lst = commonStreamServiceProxy
              .getListInfraDeviceIpV2(infraDeviceDTO);
          Boolean check = true;
          if (lst != null && !lst.isEmpty()) {
            for (InfraDeviceDTO deviceDTO : lst) {
              if (!StringUtils.isStringNullOrEmpty(deviceDTO.getDeviceCode())) {
                CrImpactedNodesDTO nodesDTO = new CrImpactedNodesDTO();
                nodesDTO.setCrId(crDTO.getCrId());
                nodesDTO.setIpId(lst.get(0).getIpId());
                nodesDTO.setIp(lst.get(0).getIp());
                nodesDTO.setDeviceId(lst.get(0).getDeviceId());
                nodesDTO.setDeviceCode(lst.get(0).getDeviceCode());
                nodesDTO.setDeviceName(lst.get(0).getDeviceName());
                nodesDTO.setInsertTime(DateTimeUtils.getSysDateTime());
                lstImpact.add(nodesDTO);
                check = false;
              }
            }
            if (check) {
              impact = impact + "," + impactedNodesDTO.getIp();
            }

          } else {
            impact = impact + "," + impactedNodesDTO.getIp();
          }
        } else {
          return "Ip in LstNetworkNodeId " + I18n.getChangeManagement("cr.msg.must.be.not.null");
        }
      }
    }
    crDTO.getLstNetworkNodeId().clear();
    crDTO.getLstNetworkNodeId().addAll(lstImpact);

    String affect = "";
    List<CrAffectedNodesDTO> lstAffect = new ArrayList<>();
    if (crDTO.getLstNetworkNodeIdAffected() != null && !crDTO.getLstNetworkNodeIdAffected()
        .isEmpty()) {
      for (CrAffectedNodesDTO affectedNodesDTO : crDTO.getLstNetworkNodeIdAffected()) {
        if (!StringUtils.isStringNullOrEmpty(affectedNodesDTO.getIp())) {
          InfraDeviceDTO infraDeviceDTO = new InfraDeviceDTO();
          infraDeviceDTO.setIp(affectedNodesDTO.getIp());
          infraDeviceDTO.setNationCode(nationCode);
          List<InfraDeviceDTO> lst = commonStreamServiceProxy
              .getListInfraDeviceIpV2(infraDeviceDTO);

          if (lst != null && !lst.isEmpty()) {
            Boolean check = true;
            for (InfraDeviceDTO deviceDTO : lst) {
              if (!StringUtils.isStringNullOrEmpty(deviceDTO.getDeviceCode())) {
                CrAffectedNodesDTO nodesDTO = new CrAffectedNodesDTO();
                nodesDTO.setCrId(crDTO.getCrId());
                nodesDTO.setIpId(lst.get(0).getIpId());
                nodesDTO.setIp(lst.get(0).getIp());
                nodesDTO.setDeviceId(lst.get(0).getDeviceId());
                nodesDTO.setDeviceCode(lst.get(0).getDeviceCode());
                nodesDTO.setDeviceName(lst.get(0).getDeviceName());
                nodesDTO.setInsertTime(DateTimeUtils.getSysDateTime());
                lstAffect.add(nodesDTO);
                check = false;
              }
            }
            if (check) {
              impact = impact + "," + affectedNodesDTO.getIp();
            }
          } else {
            affect = affect + "," + affectedNodesDTO.getIp();
          }
        } else {
          return "Ip in LstNetworkNodeIdAffected " + I18n
              .getChangeManagement("cr.msg.must.be.not.null");
        }
      }
    }
    crDTO.getLstNetworkNodeIdAffected().clear();
    crDTO.getLstNetworkNodeIdAffected().addAll(lstAffect);

    if (!"".equals(impact)) {
      return I18n.getChangeManagement("import.node.doesnot.exists") + " Ip in LstNetworkNodeId "
          + impact.substring(1);
    }

    if (!"".equals(affect)) {
      return I18n.getChangeManagement("import.node.doesnot.exists")
          + " Ip in LstNetworkNodeIdAffected " + affect.substring(1);
    }
    return "";
  }
}
