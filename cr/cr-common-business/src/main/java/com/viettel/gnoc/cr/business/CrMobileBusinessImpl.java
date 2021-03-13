package com.viettel.gnoc.cr.business;

import com.google.common.io.Files;
import com.viettel.aam.LinkCrResult;
import com.viettel.gnoc.commons.business.GnocFileBusiness;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.incident.provider.WSSecurityPort;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.MrCategoryProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CR_ACTION_CODE;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CoordinationSettingDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO.SYSTEM;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.CrProcessWoDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.ObjResponse;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.cr.repository.CoordinationSettingRepository;
import com.viettel.gnoc.cr.repository.CrDBRepository;
import com.viettel.gnoc.cr.repository.CrFilesAttachRepository;
import com.viettel.gnoc.cr.repository.CrMobileRepository;
import com.viettel.gnoc.cr.repository.CrProcessRepository;
import com.viettel.gnoc.cr.repository.CrProcessWoRepository;
import com.viettel.gnoc.cr.repository.CrRepository;
import com.viettel.gnoc.cr.thread.SendFailedWoThread;
import com.viettel.gnoc.cr.util.CrProcessFromClient;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoTestServiceMapDTO;
import com.viettel.gnoc.wo.dto.WoUpdateStatusForm;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import com.viettel.gnoc.ws.provider.WSTDTTPort;
import com.viettel.gnoc.ws.provider.WSVipaDdPort;
import com.viettel.gnoc.ws.provider.WSVipaIpPort;
import com.viettel.security.PassTranformer;
import com.viettel.security.service.CrsImpactForm;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.xml.security.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CrMobileBusinessImpl implements CrMobileBusiness {

  @Autowired
  CrMobileRepository crMobileRepository;

  @Autowired
  CrRepository crRepository;

  @Autowired
  CrDBRepository crDBRepository;

  @Autowired
  CrProcessFromClient crProcessFromClient;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  CrFilesAttachRepository crFilesAttachRepository;

  @Autowired
  CrProcessWoRepository crProcessWoRepository;

  @Autowired
  MrCategoryProxy mrCategoryProxy;

  @Autowired
  CrBusiness crBusiness;

  @Autowired
  GnocFileBusiness gnocFileBusiness;

  @Autowired
  UnitBusiness unitBusiness;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  CrImpactedNodesBusiness crImpactedNodesBusiness;

  @Autowired
  WSSecurityPort wsSecurityPort;

  @Autowired
  WSVipaIpPort wsVipaIpPort;

  @Autowired
  WSTDTTPort wstdttPort;

  @Autowired
  WSVipaDdPort wsVipaDdPort;

  @Autowired
  CrGeneralBusiness crGeneralBusiness;

  @Autowired
  CrProcessRepository crProcessRepository;

  @Autowired
  CrForOtherSystemBusiness crForOtherSystemBusiness;

  @Autowired
  CoordinationSettingRepository coordinationSettingRepository;

  @Autowired
  UnitRepository unitRepository;

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

  @Value("${application.temp.folder:null}")
  private String tempFolder;

  @Value("${application.conf.WO_FAIL_CODE:null}")
  private String woFailtCode;

  @Value("${application.conf.cr_resolve_success:null}")
  private String crResolveSuccess;

  @Value("${application.cmc_config.cr_close_CrReturnCodeId:null}")
  private String crCloseReturnCodeId;

  @Value("${application.cmc_config.cr_close_ActionReturnCodeId:null}")
  private String crCloseActionReturnCodeId;

  @Value("${application.cmc_config.system_user_name:null}")
  private String systemUserName;

  @Value("${application.cmc_config.wo_close_status_code:null}")
  private String woCloseStatusCodeConfig;

  @Override
  public List<CrDTO> getListCRBySearchTypeCount(CrDTO crDTO) {
    return crMobileRepository.getListCRBySearchTypeCount(crDTO);
  }

  @Override
  public ObjResponse getListCRBySearchTypePaggingMobile(CrDTO crDTO, int start, int maxResult,
      String locale) {
    return crMobileRepository.getListCRBySearchTypePaggingMobile(crDTO, start, maxResult, locale);
  }

  @Override
  public List<CrDTO> getListPreApprovedCrOutSide(CrDTO crDTO) {
    return crDBRepository.getListPreApprovedCrOutSide(crDTO);
  }

  @Override
  public void actionApproveCRAfter(CrDTO crDTO) {
    CrInsiteDTO formRoot = crRepository.findCrById(Long.valueOf(crDTO.getCrId()), null);
    if (Constants.CR_TYPE.STANDARD.toString().equals(formRoot.getCrType()) && !crProcessFromClient
        .checkCreateUserRoll(formRoot.getChangeOrginator())) {
      List<WoDTO> lstSubWo = getListSubWo(crDTO.getCrId(), null, formRoot.getCreatedDate(),
          crDTO.getUserLogin());
      crProcessFromClient.updateWoWhenChangeCr(lstSubWo, Constants.WO_STATUS_NEW.UNASSIGNED,
          I18n.getChangeManagement("woTest.woCrApprove"));
      if (lstSubWo != null && !lstSubWo.isEmpty()) {
        crProcessFromClient.updateWoByCrTime(lstSubWo, DateTimeUtils
                .date2ddMMyyyyHHMMss(formRoot.getEarliestStartTime()),
            DateTimeUtils.date2ddMMyyyyHHMMss(formRoot.getLatestStartTime()), null,
            formRoot.getProcessTypeId(), formRoot.getDutyType());
      }
    }
    crProcessFromClient.updateCrToMop(formRoot);
  }

  @Override
  public String actionAppraiseCRAfter(CrDTO crDTO) {
    try {
      CrInsiteDTO formRoot = crRepository.findCrById(Long.valueOf(crDTO.getCrId()), null);
      if (CR_ACTION_CODE.APPRAISE.toString().equals(crDTO.getActionType())) {
        try {
          List<WoDTO> lstSubWo = getListSubWo(formRoot.getCrId(),
              "WO_CR_TEST_SERVICE_AUTO_GENERATE", formRoot.getCreatedDate(), crDTO.getUserLogin());
          if (lstSubWo != null && !lstSubWo.isEmpty()) {
            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setCreateUserId(
                crDTO.getUserLogin() == null ? null : Long.valueOf(crDTO.getUserLogin()));
            gnocFileDto.setBusinessId(Long.valueOf(formRoot.getCrId()));
            gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.CR);
            List<GnocFileDto> lstAttachDTOs = gnocFileBusiness.getListGnocFileByDto(gnocFileDto);
            if (lstAttachDTOs != null && !lstAttachDTOs.isEmpty()) {
              String fileName = "";
              fileName = lstAttachDTOs.stream().map((dto) -> dto.getFileName() + ",")
                  .reduce(fileName, String::concat);
              if (fileName.endsWith(",")) {
                fileName = fileName.substring(0, fileName.length() - 1);
              }
              Map<String, WoTestServiceMapDTO> mapServiceMap = new HashMap<>();
              CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO = new CrCreatedFromOtherSysDTO();
              crCreatedFromOtherSysDTO.setCrId(formRoot.getCrId());
              crCreatedFromOtherSysDTO.setSystemId(SYSTEM.WO);
              List<CrCreatedFromOtherSysDTO> lstCrCreatedFromOtherSys = crRepository
                  .getCrCreatedFromOtherSys(crCreatedFromOtherSysDTO);
              if (lstCrCreatedFromOtherSys != null && !lstCrCreatedFromOtherSys.isEmpty()) {
                String woParentId = lstCrCreatedFromOtherSys.get(0).getObjectId();
                WoTestServiceMapDTO con = new WoTestServiceMapDTO();
                con.setWoId(woParentId);
                List<WoTestServiceMapDTO> lstMap = commonStreamServiceProxy
                    .getListWoTestServiceMapDTO(con, 0, Integer.MAX_VALUE, "", "");
                for (WoTestServiceMapDTO obj : lstMap) {
                  String woSubId = obj.getWoSubId();
                  if (!StringUtils.isStringNullOrEmpty(woSubId)) {
                    String[] woSubArr = woSubId.split(",");
                    for (String arr : woSubArr) {
                      mapServiceMap.put(arr, obj);
                    }
                  }
                }
              }

              for (WoDTO dto : lstSubWo) {
                try {
                  dto = woServiceProxy.findWoByIdWSProxy(Long.parseLong(dto.getWoId()));
                  WoTestServiceMapDTO mapDTO = mapServiceMap.get(dto.getWoId());
                  if (mapDTO != null && Constants.FILE_CR_ID.equals(mapDTO.getFileId())) {
                    dto.setFileName(fileName);
                    woServiceProxy.updateWoProxy(dto);
                    byte[] source = FileUtils
                        .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                            PassTranformer.decrypt(ftpPass), lstAttachDTOs.get(0).getPath());
                    FileUtils
                        .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                            PassTranformer.decrypt(ftpPass), ftpFolder,
                            lstAttachDTOs.get(0).getFileName(), source,
                            DateTimeUtils.convertStringToDate(dto.getCreateDate()));
                  }
                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                }
              }
            }

          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
      crProcessFromClient.updateCrToMop(formRoot);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.ERROR;
    }
    return RESULT.SUCCESS;
  }

  @Override
  public String actionVerifyMobile(CrDTO crDTO, String locale) {
    CrInsiteDTO formRoot = crRepository.findCrById(Long.valueOf(crDTO.getCrId()), null);
    try {
      crDTO.setCrType(formRoot.getCrType());
      crDTO.setProcessTypeId(formRoot.getProcessTypeId());
      crDTO.setCreatedDate(formRoot.getCreatedDate() == null ? null
          : DateTimeUtils.date2ddMMyyyyHHMMss(formRoot.getCreatedDate()));
      crDTO.setChangeOrginator(formRoot.getChangeOrginator());
      crDTO.setImpactSegment(formRoot.getImpactSegment());

      String validate = validateCR(crDTO);
      if (!Constants.CR_RETURN_MESSAGE.SUCCESS.equals(validate)) {
        return validate;
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return Constants.CR_RETURN_MESSAGE.ERROR;
    }
    CrInsiteDTO crInsiteDTO = crDTO.toModelInsiteDTO();
    String rs = crBusiness.actionVerify(crInsiteDTO, locale);
    try {
      if (RESULT.SUCCESS.equals(rs)) {
        crProcessFromClient.updateCrToMop(crInsiteDTO);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Constants.CR_RETURN_MESSAGE.ERROR;
    }
    return rs;
  }

  @Override
  public String actionReceiveCr(CrDTO crDTO) {
    CrInsiteDTO formRoot = crRepository.findCrById(Long.valueOf(crDTO.getCrId()), null);
    try {
      if (CR_ACTION_CODE.ACCEPT.toString().equals(crDTO.getActionType())) {
        UnitDTO unitDTO = unitBusiness.findUnitById(Long.parseLong(crDTO.getChangeOrginatorUnit()));
        UsersInsideDto userCreate = userBusiness
            .getUserDetailById(Long.parseLong(crDTO.getChangeOrginator()));
        UsersInsideDto userExecute = userBusiness
            .getUserDetailById(Long.parseLong(crDTO.getUserLogin()));
        CrsImpactForm crBO = new CrsImpactForm();
        crBO.setCrId(formRoot.getCrId());
        crBO.setCrNumber(formRoot.getCrNumber());
        crBO.setCreateDate(DateTimeUtils.date2ddMMyyyyHHMMss(formRoot.getCreatedDate()));
        crBO.setCreatorEmail(userCreate.getEmail());
        crBO.setCreatorUsername(userCreate.getUsername());
        crBO.setEndDate(crDTO.getLatestStartTime());
        crBO.setExecDepartmentCode(userExecute.getUnitName());
        crBO.setExecEmployeeEmail(userExecute.getEmail());
        crBO.setExecEmployeePhone(userExecute.getMobile());
        crBO.setExecEmployeeUsername(userExecute.getUsername());
        crBO.setOwnerDepartmentCode(unitDTO.getUnitCode());
        crBO.setProcessTypeCode(formRoot.getProcessTypeId());
        crBO.setStartDate(DateTimeUtils.date2ddMMyyyyHHMMss(formRoot.getEarliestStartTime()));
        crBO.setStatus(Constants.CR_STATE.ACCEPT.toString());
        crBO.setUpdateTime(DateTimeUtils.date2ddMMyyyyHHMMss(formRoot.getUpdateTime()));

        CrImpactedNodesDTO crImpactedNodesDTO0 = new CrImpactedNodesDTO();
        crImpactedNodesDTO0.setCrId(formRoot.getCrId());
        crImpactedNodesDTO0
            .setInsertTime(DateTimeUtils.date2ddMMyyyyHHMMss(formRoot.getCreatedDate()));
        crImpactedNodesDTO0.setType(Constants.CR_NODE_TYPE.EFFECT);
//        List<CrImpactedNodesDTO> lstCrImpactedNodesDTOs = crImpactedNodesBusiness
//            .onSearch(crImpactedNodesDTO0, 0, 100, "",
//                "");//crDTOMain.getLstNetworkNodeId();.search(crImpactedNodesDTO0, 0, 100, "", "");

//        ReceiveCrImpactedNodeThread thread = new ReceiveCrImpactedNodeThread(wsSecurityPort, crBO,
//            lstCrImpactedNodesDTOs);
//        thread.start();
        crProcessFromClient.senTraceInfo(formRoot, Constants.CR_STATE.ACCEPT);

      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.ERROR;
    }
    String rs = crBusiness.actionReceiveCr(crDTO.toModelInsiteDTO(), I18n.getLocale());
    try {

      if ("SUCCESS".equals(rs)) {
        //Neu o buoc tiep nhan ma wo van du thao thi day lai wo
        List<WoDTO> lstSubWo = getListSubWo(formRoot.getCrId(), null, formRoot.getCreatedDate(),
            crDTO.getUserLogin());
        if (lstSubWo != null && !lstSubWo.isEmpty()) {
          crProcessFromClient.updateWoWhenChangeCr(lstSubWo, Constants.WO_STATUS_NEW.UNASSIGNED,
              I18n.getLanguage("woTest.woCrSchedule"));
          if (lstSubWo != null && !lstSubWo.isEmpty()) {
            crProcessFromClient.updateWoByCrTime(lstSubWo,
                DateTimeUtils.date2ddMMyyyyHHMMss(formRoot.getEarliestStartTime()),
                DateTimeUtils.date2ddMMyyyyHHMMss(formRoot.getLatestStartTime()), null,
                formRoot.getProcessTypeId(), formRoot.getDutyType());
          }
        }
        crProcessFromClient.updateCrToMop(formRoot);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Constants.CR_RETURN_MESSAGE.ERROR;
    }
    return rs;
  }

  @Override
  public String actionScheduleCr(CrDTO crDTO) {
    CrInsiteDTO formRoot = crRepository.findCrById(Long.valueOf(crDTO.getCrId()), null);
    try {
      crDTO.setState(formRoot.getState());
      if (!Constants.CR_RETURN_MESSAGE.SUCCESS.equals(checkCrAuto(crDTO))) {
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      String rs = crBusiness.actionScheduleCr(crDTO.toModelInsiteDTO(), I18n.getLocale());
      try {
        if ("SUCCESS".equals(rs)) {

          if ((Constants.CR_TYPE.NORMAL.toString().equals(formRoot.getCrType())
              || Constants.CR_TYPE.EMERGENCY.toString().equals(formRoot.getCrType()))
              && Constants.CR_ACTION_CODE.SCHEDULE.toString()
              .equals(crDTO.getActionType())) {//cr thuong hoac khan thi day wo khi sap lich
            //chuyen trang thai wo
            List<WoDTO> lstSubWo = getListSubWo(formRoot.getCrId(), null, formRoot.getCreatedDate(),
                crDTO.getUserLogin());
            if (lstSubWo != null && !lstSubWo.isEmpty()) {
              crProcessFromClient.updateWoWhenChangeCr(lstSubWo, Constants.WO_STATUS_NEW.UNASSIGNED,
                  I18n.getLanguage("woTest.woCrSchedule"));
              //cap nhat thoi gian cua Wo theo CR
              crProcessFromClient.updateWoByCrTime(lstSubWo,
                  DateTimeUtils.date2ddMMyyyyHHMMss(formRoot.getEarliestStartTime()),
                  DateTimeUtils.date2ddMMyyyyHHMMss(formRoot.getLatestStartTime()), null,
                  formRoot.getProcessTypeId(), formRoot.getDutyType());
            }
          }
          crProcessFromClient.updateCrToMop(formRoot);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        return Constants.CR_RETURN_MESSAGE.ERROR;
      }
      return rs;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.ERROR;
    }
  }

  @Override
  public String actionResolveCr(CrDTO crDTO) {
    String rs = crBusiness.actionResolveCr(crDTO.toModelInsiteDTO(), I18n.getLocale());
    try {
      if (RESULT.SUCCESS.equals(rs)) {
        CrInsiteDTO formRoot = crRepository.findCrById(Long.valueOf(crDTO.getCrId()), null);
        try {
//          UsersInsideDto usersInsideDto = userBusiness
//              .getUserDetailById(Long.valueOf(formRoot.getChangeResponsible()));
//          CrImpactedNodesDTO crImpactedNodesDTO0 = new CrImpactedNodesDTO();
//          crImpactedNodesDTO0.setCrId(formRoot.getCrId());
//          crImpactedNodesDTO0
//              .setInsertTime(DateTimeUtils.date2ddMMyyyyHHMMss(formRoot.getCreatedDate()));
//          crImpactedNodesDTO0.setType(Constants.CR_NODE_TYPE.EFFECT);
//          List<CrImpactedNodesDTO> lstCrImpactedNodesDTOs = crImpactedNodesBusiness
//              .onSearch(crImpactedNodesDTO0, 0, 100, "", "");
//          ResetPasswordAfterCompleteCRImpactThread thread = new ResetPasswordAfterCompleteCRImpactThread(
//              wsSecurityPort, lstCrImpactedNodesDTOs, crDTO.getCrNumber(),
//              usersInsideDto.getEmail());
//          thread.start();
          crProcessFromClient.senTraceInfo(crDTO.toModelInsiteDTO(), Constants.CR_STATE.RESOLVE);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }

        try {
          List<ItemDataCRInside> lstCbbReturnCode = crGeneralBusiness
              .getListActionCodeByCode("RESOLVE_", I18n.getLocale());
          Map<String, ItemDataCRInside> lstReturnCodeAll = new HashMap<>();
          if (lstCbbReturnCode != null && !lstCbbReturnCode.isEmpty()) {
            for (ItemDataCRInside data : lstCbbReturnCode) {
              lstReturnCodeAll.put(String.valueOf(data.getValueStr()), data);
            }
          }
          String crReturnResolve = crDTO.getCrReturnResolve();
          ItemDataCRInside itemData = lstReturnCodeAll.get(crReturnResolve);
          if (itemData != null && itemData.getSecondValue() != null && itemData.getSecondValue()
              .contains(woFailtCode)) {
            UsersInsideDto userExecute = userBusiness
                .getUserDetailById(Long.valueOf(crDTO.getUserLogin()));
            String reason =
                userExecute.getUsername() + " " + I18n.getChangeManagement("fail.due.to.ft");
            com.viettel.gnoc.wo.dto.WoDTOSearch condition = new WoDTOSearch();
            condition.setWoSystem("CR");
            condition.setWoSystemId(crDTO.getCrId());//Code da insert khi tao WO
            condition.setUserId(crDTO.getUserLogin());
            condition.setPage(1);
            condition.setPageSize(Integer.MAX_VALUE);
            List<WoDTOSearch> lstWo = woServiceProxy.getListDataSearchProxy(condition);
            List<String> lst = new ArrayList<>();
            if (lstWo != null && !lstWo.isEmpty()) {
              for (WoDTOSearch dTOSearch : lstWo) {
                lst.add(dTOSearch.getWoCode());
              }
              SendFailedWoThread failedWoThread
                  = new SendFailedWoThread(userExecute.getUsername(), reason, lst, woServiceProxy);
              failedWoThread.start();
            }
          }

        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }

        Long resolveSusccessStatus = 39L;
        try {
          resolveSusccessStatus = Long.valueOf(crResolveSuccess);
        } catch (NumberFormatException e) {
          log.error(e.getMessage(), e);
        }
        if (String.valueOf(resolveSusccessStatus).equals(crDTO.getActionReturnCodeId())) {
          //Đóng Cr nếu hoàn thành thành công
          try {
            CrProcessInsideDTO process = crProcessRepository
                .findCrProcessById(Long.valueOf(crDTO.getProcessTypeId()));

            if (process != null && "1"
                .equals(String.valueOf(process.getCloseCrWhenResolveSuccess()))) {
              crDTO.setActionType(String.valueOf(Constants.CR_ACTION_CODE.CLOSECR));
              String crReturnCodeId = crCloseReturnCodeId;
              String actionReturnCodeId = crCloseActionReturnCodeId;
              crDTO.setCrReturnCodeId(crReturnCodeId == null ? "27" : crReturnCodeId);
              crDTO.setActionReturnCodeId(actionReturnCodeId == null ? "42" : actionReturnCodeId);
              crDTO.setActionType(String.valueOf(Constants.CR_ACTION_CODE.CLOSECR));
              crDTO.setCloseCrAuto("1");
              String closeResult = crBusiness
                  .actionCloseCr(crDTO.toModelInsiteDTO(), I18n.getLocale());
              if (!Constants.CR_RETURN_MESSAGE.SUCCESS.equals(closeResult)) {
                log.info("Resolve Cr Sucesssfully but can not close it ");
                return "Resolve Cr Sucesssfully but can not close it ";
              }
            }
          } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
          }

          //Đóng WO nếu hoàn thành thành công
          try {

            if (crDTO.getCrId() != null) {
              log.info("step 0");
              Long crId = Long.parseLong(crDTO.getCrId());
              CrCreatedFromOtherSysDTO dto = crForOtherSystemBusiness
                  .getCrCreatedFromOtherSysDTO(crId);

              if (dto != null && dto.getStatus() != null
                  && "OK".equalsIgnoreCase(dto.getStatus())
                  && "4".equalsIgnoreCase(dto.getSystemId())) { //check lai
                log.info("step 1");
                Long woId = crProcessFromClient.stringToLong(dto.getObjectId());
                WoDTO wo = woServiceProxy.findWoByIdWSProxy(woId);
                if (wo != null) {
                  Long unitId = Long.parseLong(crDTO.getChangeOrginatorUnit());
                  Long woTypeId = Long.parseLong(wo.getWoTypeId());
                  boolean checkAutoClose = false;
                  if (unitId != null && woTypeId != null) {
                    checkAutoClose = crForOtherSystemBusiness
                        .checkWoCloseAutoSetting(unitId, woTypeId);
                  }

                  if (checkAutoClose) {
                    log.info("step 3");
                    List<CrCreatedFromOtherSysDTO> crList = crForOtherSystemBusiness
                        .getListDataByObjectId(woId);
                    if (crList != null && !crList.isEmpty()) {
                      List<Long> crIds = new ArrayList<>();
                      for (CrCreatedFromOtherSysDTO otherDTO : crList) {
                        Long cId = Long.parseLong(otherDTO.getCrId());
                        if (cId != null) {
                          crIds.add(cId);
                        }
                      }
                      List<CrDTO> crData = crRepository
                          .getCrByIdAndResolveStatuṣ̣(crIds, resolveSusccessStatus);

                      if (crData != null && crData.size() == crList.size()) {
                        log.info("step 4");
                        Long woCloseStatusCode = 8L;
                        String reasonChange = "WO is closed by Cr System automatically";
                        String userChange = "system";
                        String systemChange = "CR";

                        try {
                          userChange = systemUserName;
                          woCloseStatusCode = crProcessFromClient
                              .stringToLong(woCloseStatusCodeConfig);
                          reasonChange = I18n.getLanguage("cr.detail.close.wo.auto");
                        } catch (Exception e) {
                          log.error(e.getMessage(), e);
                        }

                        WoUpdateStatusForm updateStatusForm = new WoUpdateStatusForm();
                        updateStatusForm.setWoCode(wo.getWoCode());
                        updateStatusForm.setNewStatus(woCloseStatusCode);
                        updateStatusForm.setReasonChange(reasonChange);
                        updateStatusForm.setUserChange(userChange);
                        updateStatusForm.setSystemChange(systemChange);
                        updateStatusForm
                            .setFinishTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
                        updateStatusForm.setResultClose(2L);
                        ResultDTO resultDTO = woServiceProxy.changeStatusWoProxy(updateStatusForm);
                        if (!RESULT.SUCCESS.equalsIgnoreCase(resultDTO.getMessage())) {
                          return resultDTO.getMessage();

                        }
                      }
                    }
                  }
                }
              }
            }

          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
        }
        crProcessFromClient.updateCrToMop(formRoot);
      } else if (Constants.CR_RETURN_MESSAGE.MUSTCLOSEALLWO.equalsIgnoreCase(rs)) {
        return I18n.getChangeManagement("cr.msg.must.be.close.all.wo");
      } else {
        return I18n.getChangeManagement("cr.msg.unsuccess") + ": " + rs;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.ERROR;
    }
    return rs;
  }

  @Override
  public String actionCloseCr(CrDTO crDTO) {
    CrInsiteDTO crInsiteDTO = crDTO.toModelInsiteDTO();
    String rs = crBusiness.actionCloseCr(crInsiteDTO, I18n.getLocale());
    try {
      if (RESULT.SUCCESS.equals(rs)) {
        CrInsiteDTO formRoot = crRepository.findCrById(Long.valueOf(crDTO.getCrId()), null);
        //hanh dong tao MR
        try {
          if (crDTO.getCrId() != null) {

            Long crId = Long.parseLong(crDTO.getCrId());
            CrCreatedFromOtherSysDTO dto = crForOtherSystemBusiness
                .getCrCreatedFromOtherSysDTO(crId);
            if (dto != null && dto.getStatus() != null
                && "OK".equalsIgnoreCase(dto.getStatus()) && "1".equals(dto.getSystemId())) {
              String reasonTypeId = crDTO.getActionReturnCodeId();
              if ("42".equalsIgnoreCase(reasonTypeId)) {
                mrCategoryProxy.reCreatedOrCloseCr(crDTO.getCrId(), "1");
              } else {
                mrCategoryProxy.reCreatedOrCloseCr(crDTO.getCrId(), "0");
              }
            }
          }

        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
        }

        //hanh dong tao WO
        StringBuilder wrongBuiDer = new StringBuilder();
        try {
          if (("42".equals(crDTO.getActionReturnCodeId()) || "43"
              .equals(crDTO.getActionReturnCodeId())) && crDTO.getProcessTypeId() != null) {
            List<CrProcessWoDTO> lstCrWo = crProcessWoRepository
                .getLstWoFromProcessId(crDTO.getProcessTypeId());
            if (lstCrWo != null && !lstCrWo.isEmpty()) {

              //System.out.println("START 1");
              for (CrProcessWoDTO woDTO : lstCrWo) {
                if (woDTO.getCreateWoWhenCloseCR() != null
                    && woDTO.getCreateWoWhenCloseCR().intValue() == 1) {

                  Long unitId = Long.parseLong(crDTO.getChangeResponsibleUnit());
                  CoordinationSettingDTO setting = coordinationSettingRepository
                      .getCoordinationSettingInfor(unitId, null);
                  if (setting == null) {
                    UnitDTO childUnit = unitRepository.findUnitById(unitId);
                    if (childUnit == null) {
                      continue;
                    }
                    setting = coordinationSettingRepository
                        .getCoordinationSettingInfor(childUnit.getParentUnitId(), null);
                  }

                  if (setting == null) {
                    continue;
                  }

                  WoDTO wdto = crProcessFromClient.createWoCMDB(crInsiteDTO, woDTO, setting);

                  if (wdto != null) {
                    ResultDTO reuslt = woServiceProxy.insertWoForSPMProxy(wdto);
                    if (!RESULT.SUCCESS.equalsIgnoreCase(reuslt.getMessage())) {
                      log.error(reuslt.getMessage());
                      return reuslt.getMessage();
                    }

                  }
                }
              }
            }
          }
        } catch (Exception e) {
          //
          log.error(e.getMessage(), e);
          return Constants.CR_RETURN_MESSAGE.ERROR;
        }
        if (wrongBuiDer.length() > 0) {
          return I18n.getChangeManagement("cr.cannot.create.WO.info")
              .concat(wrongBuiDer.toString());
        }
        crProcessFromClient.updateCrToMop(formRoot);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.ERROR;
    }

    return rs;
  }

  @Override
  public String actionCab(CrInsiteDTO crDTO) {
    String rs = crBusiness.actionCab(crDTO, I18n.getLocale());
    try {

      if ("SUCCESS".equals(rs)) {
        CrInsiteDTO dTO = crRepository.findCrById(Long.parseLong(crDTO.getCrId()), null);
        if (Constants.CR_TYPE.NORMAL.toString().equals(dTO.getCrType()) //cr thuong
            && ("1".equals(dTO.getRisk()) || "2".equals(dTO.getRisk()) || "3"
            .equals(dTO.getRisk()))) {
          //chuyen trang thai wo
          List<WoDTO> lstSubWo = getListSubWo(dTO.getCrId(), null, dTO.getCreatedDate(),
              crDTO.getUserLogin());
          if (lstSubWo != null && !lstSubWo.isEmpty()) {
            crProcessFromClient.updateWoWhenChangeCr(lstSubWo, Constants.WO_STATUS_NEW.UNASSIGNED,
                I18n.getLanguage("woTest.woCrCab"));
            //cap nhat thoi gian cua Wo theo CR
            crProcessFromClient.updateWoByCrTime(lstSubWo,
                DateTimeUtils.date2ddMMyyyyHHMMss(dTO.getEarliestStartTime()), DateTimeUtils
                    .date2ddMMyyyyHHMMss(dTO.getLatestStartTime()),
                null, dTO.getProcessTypeId(), dTO.getDutyType());
          }
        }
        crProcessFromClient.updateCrToMop(dTO);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Constants.CR_RETURN_MESSAGE.ERROR;
    }
    return rs;
  }

  @Override
  public String actionEditCr(CrDTO crDTO) {
    CrInsiteDTO crInsiteDTO = null;
    try {
      crInsiteDTO = crDTO.toModelInsiteDTO();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (crInsiteDTO == null) {
      return RESULT.ERROR;
    }
    String rs = crBusiness.actionEditCr(crInsiteDTO, I18n.getLocale());

    try {
      if (RESULT.SUCCESS.equals(rs)) {
        CrInsiteDTO dTO = crRepository.findCrById(Long.valueOf(crDTO.getCrId()), null);
        List<WoDTO> lstSubWo = getListSubWo(dTO.getCrId(), null, dTO.getCreatedDate(),
            crDTO.getUserLogin());
        if (lstSubWo != null && !lstSubWo.isEmpty()) {
          //cap nhat thoi gian cua Wo theo CR
          crProcessFromClient.updateWoByCrTime(lstSubWo,
              DateTimeUtils.date2ddMMyyyyHHMMss(dTO.getEarliestStartTime()),
              DateTimeUtils.date2ddMMyyyyHHMMss(dTO.getLatestStartTime()), null,
              dTO.getProcessTypeId(), dTO.getDutyType());
        }
        crProcessFromClient.updateCrToMop(dTO);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Constants.CR_RETURN_MESSAGE.ERROR;
    }
    return rs;
  }

  //longlt6 add funtion Close Cr End
  //tuanpv14_start add function start
  public List<WoDTO> getListSubWo(String crId, String type, Date crCreateDate, String userId) {
    List<WoDTO> lstSubWo = new ArrayList<WoDTO>();
    try {

      //Date crDate = DateUtil.string2DateTime(crCreateDate);
      Date crDate = crCreateDate;
      if (crDate == null) {
        crDate = new Date();
      }
      Calendar cal = Calendar.getInstance();
      cal.setTime(crDate);
      cal.add(Calendar.DATE, -3);

      WoDTOSearch condition = new WoDTOSearch();
      condition.setWoSystemId(crId);
      condition.setUserId(userId);
      condition.setWoSystem("CR");
      condition.setPage(1);
      condition.setPageSize(Integer.MAX_VALUE);
      List<WoDTOSearch> lstWo = woServiceProxy.getListDataSearchProxy(condition);
      if (lstWo != null && !lstWo.isEmpty()) {
        for (WoDTOSearch o : lstWo) {
          WoDTO dto = new WoDTO();
          dto.setWoId(o.getWoId());
          dto.setWoCode(o.getWoCode());
          dto.setWoTypeId(o.getWoTypeId());
          dto.setWoTypeCode(o.getWoTypeCode());
          dto.setCreateDate(o.getCreateDate());
          dto.setStatus(o.getStatus());
          dto.setStartTime(o.getStartTime());
          dto.setEndTime(o.getEndTime());
          dto.setWoContent(o.getWoContent());
          lstSubWo.add(dto);
        }
      }

      if (lstSubWo != null && !lstSubWo.isEmpty() && type != null) {
        lstSubWo.removeIf(c -> (!c.getWoCode().contains(type)));
//        for (int i = lstSubWo.size() - 1; i >= 0; i--) {
//          String woCode = lstSubWo.get(i).getWoCode();
//          if (!woCode.contains(type)) {
//            lstSubWo.remove(i);
//          }
//        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstSubWo;
  }

  private String validateCR(CrDTO crDTO) throws Exception {
    CrInsiteDTO crInsiteDTO = crDTO.toModelInsiteDTO();
    List<String> lstFileType = crProcessFromClient
        .genListFileTypeRequire(crInsiteDTO, crDTO.getActionRight(), crDTO.getActionType(), false,
            crDTO.getCrType(), "1".equals(crDTO.getIsTracingCr()));
    //validate cr lien ket den phe duyet truoc + phai load DT tu MOP
    if (Constants.CR_TYPE.NORMAL.toString().equals(crDTO.getCrType())) {
      String related = crDTO.getRelateToPreApprovedCr();
      if (!StringUtils.isStringNullOrEmpty(related)) {
        lstFileType.add(Constants.CR_FILE_TYPE.DT_SCRIPT);
      }
    }
    CrFilesAttachInsiteDTO crFile = new CrFilesAttachInsiteDTO();
    crFile.setCrId(Long.valueOf(crDTO.getCrId()));
    List<CrFilesAttachInsiteDTO> lstAttachment = crFilesAttachRepository
        .getListCrFilesSearch(crFile);
    String validateFileAttach = crProcessFromClient
        .validateFileAttach(crInsiteDTO, lstAttachment, lstFileType);
    if (StringUtils.isNotNullOrEmpty(validateFileAttach)) {
      return validateFileAttach;
    }
    List<CrFilesAttachDTO> lstFileImport = crFilesAttachRepository
        .getListFileImportByProcess(crFile);

    String validateFileImportByProcess = crProcessFromClient
        .validateFileImportByProcess(crInsiteDTO, lstFileImport, crDTO.getActionRight());
    if (StringUtils.isNotNullOrEmpty(validateFileImportByProcess)) {
      return validateFileImportByProcess;
    }

    //validate create Wo config
    String validateCreateWoConf = validateCreateWoConf(crInsiteDTO);
    if (StringUtils.isNotNullOrEmpty(validateCreateWoConf)) {
      return validateCreateWoConf;
    }

    return Constants.CR_RETURN_MESSAGE.SUCCESS;
  }

  private String validateCreateWoConf(CrInsiteDTO crDTO) {
    String processGeneral = crDTO.getProcessTypeId();
    if (StringUtils.isNotNullOrEmpty(crDTO.getProcessTypeLv3Id())) {
      processGeneral = crDTO.getProcessTypeLv3Id().trim();
    }
    List<CrProcessWoDTO> lstCrWo = crProcessWoRepository
        .getLstWoFromProcessId(processGeneral);
    if (lstCrWo != null && !lstCrWo.isEmpty()) {
      List<WoDTO> lstSubWo = getListSubWo(crDTO.getCrId(), null, crDTO.getCreatedDate(),
          crDTO.getChangeOrginator());
      List<String> lstCreated = new ArrayList<>();
      Map<String, Integer> addedMap = new HashMap<>();
      Map<String, HashSet<String>> addedName = new HashMap<>();
      Map<String, Integer> neededMap = new HashMap<>();

      if (lstSubWo != null && !lstSubWo.isEmpty()) {
        for (WoDTO dto : lstSubWo) {
          lstCreated.add(dto.getWoContent());
          if (dto.getWoTypeId() != null) {
            int count = 1;
            if (addedMap.containsKey(dto.getWoTypeId())) {
              count = addedMap.get(dto.getWoTypeId()) + 1;
              addedName.get(dto.getWoTypeId()).add(dto.getWoContent());
            } else {
              HashSet<String> set = new HashSet();
              set.add(dto.getWoContent());
              addedName.put(dto.getWoTypeId(), set);
            }
            addedMap.put(dto.getWoTypeId(), count);

          }
        }
      }
      //kiem tra WO bat buoc tao
      for (CrProcessWoDTO dto : lstCrWo) {
        String name = dto.getWoName();
        Long isRequire = dto.getIsRequire();
        Long createWoWhenCloseCr = dto.getCreateWoWhenCloseCR();
                /*if (!lstCreated.contains(name) && isRequire.equals(1L)) {
                 CommonCustomize.showNotification(BundleUtils.getChangeManageString("cr.wo.createRequire") + ": " + name);
                 return false;
                 }*/

        if (isRequire != null && isRequire.intValue() == 1) {
          if (createWoWhenCloseCr == null || createWoWhenCloseCr.intValue() != 1) {

            if (dto.getWoTypeId() != null) {
              String woTypeStr = String.valueOf(dto.getWoTypeId());
              int count = 1;
              if (addedMap.containsKey(woTypeStr)) {
                count = addedMap.get(woTypeStr) + 1;
              }
              neededMap.put(woTypeStr, count);

              if (!addedMap.containsKey(woTypeStr)
                  || (addedMap.containsKey(woTypeStr) && addedMap.get(woTypeStr) < count)) {
                if (!addedName.containsKey(woTypeStr)
                    || !addedName.get(woTypeStr).contains(dto.getWoName())) {
                  return I18n.getChangeManagement("cr.wo.createRequire") + ": " + name;
                }

              }

            }
          }
        }

      }
      //kiem tra WO ko bat buoc tao
      try {
        WorkLogCategoryInsideDTO wlCatCon = new WorkLogCategoryInsideDTO();
        wlCatCon.setPage(1);
        wlCatCon.setPageSize(2);
        wlCatCon.setWlayCode(Constants.WORKLOG_CAT.CR_NO_WO_CODE);
        List<WorkLogCategoryInsideDTO> lstWlCat = mrCategoryProxy
            .getListWorkLogCategoryDTO(wlCatCon);
        if (lstWlCat != null && !lstWlCat.isEmpty()) {
          WorkLogCategoryInsideDTO wlDto = lstWlCat.get(0);
          WorkLogInsiteDTO wlCon = new WorkLogInsiteDTO();
          wlCon.setWlgObjectId(
              DataUtil.isNullOrEmpty(crDTO.getCrId()) ? null : Long.valueOf(crDTO.getCrId()));
          wlCon.setWlayId(wlDto.getWlayId());
          wlCon.setUserGroupAction(wlDto.getWlayType());
          wlCon.setWlgObjectType(Long.valueOf(Constants.WORK_LOG_SYSTEM.CR));
          wlCon.setPage(0);
          wlCon.setPageSize(100);
          List<WorkLogInsiteDTO> lstWl = mrCategoryProxy.getListWorkLogDTO(wlCon);

          for (CrProcessWoDTO dto : lstCrWo) {
            String name = dto.getWoName();
            Long isRequire = dto.getIsRequire();
            Long createWoWhenCloseCr = dto.getCreateWoWhenCloseCR();
            if (!lstCreated.contains(name) && !isRequire.equals(1L) && (lstWl == null || lstWl
                .isEmpty()) && !"1".equals(String.valueOf(createWoWhenCloseCr))) {
              return I18n.getChangeManagement("common.confirmWo");
            }
          }

        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }

    }
    return "";
  }

  public String checkCrAuto(CrDTO crDTO) {
    try {
      CrFilesAttachInsiteDTO crFile = new CrFilesAttachInsiteDTO();
      crFile.setCrId(Long.valueOf(crDTO.getCrId()));
      List<CrFilesAttachInsiteDTO> lstAttachment = crFilesAttachRepository
          .getListCrFilesAttachDTO(crFile, 0, 1000, "", "");
      String dtCode = "";
      String dtHis = "";
      boolean temp = false;
      if (lstAttachment != null) {
        for (CrFilesAttachInsiteDTO dto : lstAttachment) {
          if (dto.getDtCode() != null) {
            dtCode = dto.getDtCode();
            dtHis = dto.getDtFileHistory();
            if (dtCode != null && dtHis != null) {
              temp = true;
              break;
            }
          }
        }
      }
      if (temp) {
        boolean checked = "1".equals(crDTO.getAutoExecute());
        Long typeConfirmGNOC;
        Long typeRunGNOC;
        String crLinkGNOC = crDTO.getRelateToPreApprovedCrNumber();
        if (StringUtils.isStringNullOrEmpty(crLinkGNOC)) {
          crLinkGNOC = crDTO.getRelateToPrimaryCrNumber();
          System.out.println("--checked-- " + crDTO.getCrNumber());
        }
        if (StringUtils.isNotNullOrEmpty(crDTO.getIsConfirmAction())) {//co can check tac dong
          typeConfirmGNOC = Long.parseLong(crDTO.getIsConfirmAction());
        } else {
          typeConfirmGNOC = 0L;
        }
        if ("1".equals(crDTO.getIsRunType())) {//chay tuan tu
          typeRunGNOC = 1L;
        } else {
          typeRunGNOC = 0L;//chay song song
        }

        if (dtHis != null && dtHis.contains("VIPA_IP")) {
          com.viettel.vipa.ResultDTO result = wsVipaIpPort
              .updateRunAutoStatus(crDTO, dtCode, checked, typeConfirmGNOC, typeRunGNOC,
                  crLinkGNOC);
          if (result == null) {
            return RESULT.ERROR;
          }
          Integer status = result.getResultCode();
          if (status != null && (status == 1 || status == 0)) {
            return RESULT.SUCCESS;
          }
          return RESULT.ERROR;
        } else if (dtHis != null && dtHis.contains("VIPA_DD")) {
          com.viettel.vmsa.ResultDTO result = wsVipaDdPort
              .updateRunAutoStatus(crDTO, dtCode, checked, typeConfirmGNOC, typeRunGNOC,
                  crLinkGNOC);
          if (result == null) {
            return RESULT.ERROR;
          }
          Integer status = result.getResultCode();
          if (status != null && (status == 1 || status == 0)) {
            return RESULT.SUCCESS;
          }
          return RESULT.ERROR;

        } else if (StringUtils.isNotNullOrEmpty(dtHis)) {
          LinkCrResult result = wstdttPort
              .updateRunAutoStatus(crDTO, dtCode, checked, typeConfirmGNOC, typeRunGNOC,
                  crLinkGNOC);
          if (result == null) {
            return RESULT.ERROR;
          }
          Integer status = result.getStatus();
          if (status != null && status == 1) {
            return RESULT.SUCCESS;
          }
          return RESULT.ERROR;
        }
      }
      return RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.ERROR;
    }
  }

  @Override
  public List<CrDTO> getCrByIdAndResolveStatuṣ̣(List<Long> crIds, Long resolveStatus) {
    return crRepository.getCrByIdAndResolveStatuṣ̣(crIds, resolveStatus);
  }

  @Override
  public CrFilesAttachDTO getFileByPath(String path) {
    CrFilesAttachDTO attachDTO = new CrFilesAttachDTO();
    try {
      byte[] bFile = FileUtils.getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
          PassTranformer.decrypt(ftpPass), path);
      attachDTO.setFilePath(path);
      attachDTO.setFileContent(Base64.encode(bFile));
      attachDTO.setFileType(Files.getFileExtension(path));
      attachDTO.setFileSize(bFile.length + "");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return attachDTO;
  }

  @Override
  public String actionAssignCabMobile(CrDTO crDTO, String locale) {
    CrInsiteDTO dTO = crRepository.findCrById(Long.valueOf(crDTO.getCrId()), null);
    try {
      crDTO.setCrType(dTO.getCrType());
      crDTO.setProcessTypeId(dTO.getProcessTypeId());
      crDTO.setCreatedDate(dTO.getCreatedDate() == null ? null
          : DateTimeUtils.date2ddMMyyyyHHMMss(dTO.getCreatedDate()));
      crDTO.setChangeOrginator(dTO.getChangeOrginator());
      crDTO.setImpactSegment(dTO.getImpactSegment());
      String validate = validateCR(crDTO);
      if (!Constants.CR_RETURN_MESSAGE.SUCCESS.equals(validate)) {
        return validate;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Constants.CR_RETURN_MESSAGE.ERROR;
    }

    String rs = crBusiness.actionAssignCab(crDTO.toModelInsiteDTO(), locale);
    try {

      if ("SUCCESS".equals(rs)) {
        if (Constants.CR_TYPE.NORMAL.toString().equals(dTO.getCrType()) //cr thuong
            && ("1".equals(dTO.getRisk()) || "2".equals(dTO.getRisk()) || "3"
            .equals(dTO.getRisk()))) {
          //chuyen trang thai wo
          List<WoDTO> lstSubWo = getListSubWo(dTO.getCrId(), null, dTO.getCreatedDate(),
              crDTO.getUserLogin());
          if (lstSubWo != null && !lstSubWo.isEmpty()) {
            crProcessFromClient.updateWoWhenChangeCr(lstSubWo, Constants.WO_STATUS_NEW.UNASSIGNED,
                I18n.getLanguage("woTest.woCrCab"));
            //cap nhat thoi gian cua Wo theo CR
            crProcessFromClient.updateWoByCrTime(lstSubWo,
                DateTimeUtils.date2ddMMyyyyHHMMss(dTO.getEarliestStartTime()), DateTimeUtils
                    .date2ddMMyyyyHHMMss(dTO.getLatestStartTime()),
                null, dTO.getProcessTypeId(), dTO.getDutyType());
          }
        }
        crProcessFromClient.updateCrToMop(dTO);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return Constants.CR_RETURN_MESSAGE.ERROR;
    }
    return rs;
  }

  @Override
  public CrDTO getCrByIdExtends(String crId) {
    return crMobileRepository.getCrByIdExtends(crId);
  }
}
