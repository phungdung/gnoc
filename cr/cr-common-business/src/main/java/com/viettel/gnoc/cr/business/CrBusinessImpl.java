package com.viettel.gnoc.cr.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.viettel.aam.AppGroup;
import com.viettel.aam.AppGroupResult;
import com.viettel.gnoc.commons.business.MessagesBusiness;
import com.viettel.gnoc.commons.business.RoleUserBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.incident.provider.WSNocprov4Port;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.PtServiceProxy;
import com.viettel.gnoc.commons.proxy.RiskServiceProxy;
import com.viettel.gnoc.commons.proxy.SrServiceProxy;
import com.viettel.gnoc.commons.proxy.TtServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CfgRoleDataRepository;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CR_ACTION_CODE;
import com.viettel.gnoc.commons.utils.Constants.CR_FILE_TYPE;
import com.viettel.gnoc.commons.utils.Constants.CR_STATE;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.INSERT_SOURCE;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.SR_STATUS;
import com.viettel.gnoc.commons.utils.Constants.WORK_LOG_CAT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.AppGroupInsite;
import com.viettel.gnoc.cr.dto.AttachDtDTO;
import com.viettel.gnoc.cr.dto.CrAffectedServiceDetailsDTO;
import com.viettel.gnoc.cr.dto.CrAlarmInsiteDTO;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentInsiteDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.CrProcessWoDTO;
import com.viettel.gnoc.cr.dto.CrUpdateMopStatusHisDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.ObjResponse;
import com.viettel.gnoc.cr.dto.RdmUpdateDTO;
import com.viettel.gnoc.cr.dto.UserCabCrForm;
import com.viettel.gnoc.cr.dto.UserReceiveMsgDTO;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.cr.repository.CrAffectedNodeRepository;
import com.viettel.gnoc.cr.repository.CrAffectedServiceDetailsRepository;
import com.viettel.gnoc.cr.repository.CrAlarmRepository;
import com.viettel.gnoc.cr.repository.CrApprovalDepartmentRepository;
import com.viettel.gnoc.cr.repository.CrCableRepository;
import com.viettel.gnoc.cr.repository.CrDBRepository;
import com.viettel.gnoc.cr.repository.CrFilesAttachRepository;
import com.viettel.gnoc.cr.repository.CrGeneralRepository;
import com.viettel.gnoc.cr.repository.CrHisRepository;
import com.viettel.gnoc.cr.repository.CrImpactedNodesRepository;
import com.viettel.gnoc.cr.repository.CrProcessRepository;
import com.viettel.gnoc.cr.repository.CrProcessWoRepository;
import com.viettel.gnoc.cr.repository.CrRepository;
import com.viettel.gnoc.cr.repository.UserReceiveMsgRepository;
import com.viettel.gnoc.cr.thread.SendFailedWoThread;
import com.viettel.gnoc.cr.util.CrConfig;
import com.viettel.gnoc.cr.util.CrGeneralUtil;
import com.viettel.gnoc.cr.util.CrProcessFromClient;
import com.viettel.gnoc.cr.util.MrCategoryUtil;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import com.viettel.gnoc.risk.dto.RiskDTOSearch;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.wfm.dto.WoSearchDTO;
import com.viettel.gnoc.wo.dto.UserGroupCategoryDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoPriorityDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.dto.WoUpdateStatusForm;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import com.viettel.gnoc.ws.provider.NocProWS;
import com.viettel.gnoc.ws.provider.WSGatePort;
import com.viettel.gnoc.ws.provider.WSTDTTPort;
import com.viettel.gnoc.ws.provider.WSVipaDdPort;
import com.viettel.gnoc.ws.provider.WSVipaIpPort;
import com.viettel.nms.nocpro.service.ResponseBO;
import com.viettel.security.PassTranformer;
import com.viettel.vmsa.MopDetailDTO;
import com.viettel.vmsa.MopDetailOutputDTO;
import com.viettel.vmsa.MopGnoc;
import com.viettel.vmsa.MopGnocDTO;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class CrBusinessImpl implements CrBusiness {

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

  @Value("${application.cmc_config.cr_close_CrReturnCodeId:null}")
  private String crCloseReturnCodeId;

  @Value("${application.cmc_config.cr_close_ActionReturnCodeId:null}")
  private String crCloseActionReturnCodeId;

  @Value("${application.cmc_config.system_user_id:null}")
  private String systemUserId;

  @Value("${application.cmc_config.system_user_name:null}")
  private String systemUserName;

  @Value("${application.cmc_config.wo_close_status_code:null}")
  private String woCloseStatusCodeConfig;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Value("${application.conf.user_service:null}")
  private String userName;

  @Value("${application.conf.pass_service:null}")
  private String pass;

  @Value("${application.conf.salt_service:null}")
  private String salt;

  @Value("${application.conf.cr_resolve_success:null}")
  private String crResolveSuccess;

  @Autowired
  CrRepository crRepository;

  @Autowired
  RoleUserBusiness roleUserBusiness;

  @Autowired
  CrDBRepository crDBRepository;

  @Autowired
  CrProcessFromClient crProcessFromClient;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  CrApprovalDepartmentRepository crApprovalDepartmentRepository;

  @Autowired
  CrImpactedNodesRepository crImpactedNodesRepository;

  @Autowired
  CrGeneralRepository crGeneralRepository;

  @Autowired
  CrAffectedNodeRepository crAffectedNodeRepository;

  @Autowired
  CrAffectedServiceDetailsRepository crAffectedServiceDetailsRepository;

  @Autowired
  CrAlarmRepository crAlarmRepository;

  @Autowired
  CrCableRepository crCableRepository;

  @Autowired
  CrHisRepository crHisRepository;

  @Autowired
  CrForOtherSystemBusiness crForOtherSystemBusiness;

  @Autowired
  SrServiceProxy srServiceProxy;

  @Autowired
  WSTDTTPort wstdttPort;

  @Autowired
  WSVipaDdPort wsVipaDdPort;

  @Autowired
  WSVipaIpPort wsVipaIpPort;

  @Autowired
  CrFilesAttachRepository crFilesAttachRepository;

  @Autowired
  RiskServiceProxy riskServiceProxy;

  @Autowired
  WoCategoryServiceProxy woCategoryServiceProxy;
  //
  @Autowired
  MrCategoryUtil mrCategoryUtil;

  @Autowired
  SmsDBBussiness smsDBBussiness;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  TtServiceProxy ttServiceProxy;

  @Autowired
  PtServiceProxy ptServiceProxy;

  @Autowired
  UserReceiveMsgRepository userReceiveMsgRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  MessagesBusiness messagesBusiness;

  @Autowired
  CrProcessRepository crProcessRepository;

  @Autowired
  CrProcessWoRepository crProcessWoRepository;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  CommonRepository commonRepository;

  @Autowired
  WSGatePort wsGatePort;

  @Autowired
  WSNocprov4Port wsNocprov4Port;

  @Autowired
  CfgRoleDataRepository cfgRoleDataRepository;

  @Override
  public Datatable getListCRBySearchTypePagging(CrInsiteDTO crDTO, String locale) {
    log.debug("Request to getListCRBySearchTypePagging : {}", crDTO);
    if (StringUtils.isStringNullOrEmpty(locale)) {
      locale = I18n.getLocale();
    }
    UserToken userToken = ticketProvider.getUserToken();
    if (StringUtils.isStringNullOrEmpty(crDTO.getUserLogin()) || StringUtils
        .isStringNullOrEmpty(crDTO.getUserLoginUnit())) {
      crDTO.setUserLogin(String.valueOf(userToken.getUserID()));
      crDTO.setUserLoginUnit(String.valueOf(userToken.getDeptId()));
    }

    List<List<Long>> searchCrIds = null;
    List<Long> allIps = null;

    if (crDTO.getSearchImpactedNodeIpIds() != null && !crDTO.getSearchImpactedNodeIpIds()
        .isEmpty()) {
      Date startDate = crDTO.getEarliestStartTime();
      Date endDate = null;
      try {
        endDate = DateTimeUtils.convertStringToDate(crDTO.getEarliestStartTimeTo());
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      if (startDate == null) {
        startDate = new Date();
      }
      if (endDate == null) {
        endDate = new Date();
      }
      Calendar cal = Calendar.getInstance();
      cal.setTime(startDate);
      cal.add(Calendar.DATE, -7);
      startDate = cal.getTime();

      allIps = crRepository
          .getlistCrIdsByNodeInfo(startDate, endDate, crDTO.getSearchImpactedNodeIpIds());
      if (allIps == null || allIps.isEmpty()) {
        return new Datatable();
      }

      int count = allIps.size() / 500;
      if (allIps.size() % 500 != 0) {
        count++;
      }

      List<Long> crids;
      searchCrIds = new ArrayList<>();
      for (int p = 1; p <= count; p++) {
        if (p < count) {
          crids = allIps.subList((p - 1) * 500, p * 500);
        } else {
          crids = allIps.subList((p - 1) * 500, allIps.size());
        }
        if (crids != null && !crids.isEmpty()) {
          searchCrIds.add(crids);
        }
      }

    }
    crDTO.setSearchCrIds(searchCrIds);
    //tiennv them xu ly userCab
    Datatable datatable = crRepository.getListCRBySearchType(crDTO, locale);
    if (INSERT_SOURCE.TT
        .equals(crDTO.getAction())) { //tiennv bo sung search tu TT thi bo qua get cac user ben duoi
      return datatable;
    }

    List<CrInsiteDTO> lstData = (List<CrInsiteDTO>) datatable.getData();

    if (lstData != null && lstData.size() > 0) {
      //tiennv them xu ly checkboxAction
      //get List UserMsg
      List<String> lstCrMsgAll = new ArrayList<>();
      try {
        UserReceiveMsgDTO msgDTO = new UserReceiveMsgDTO();
        msgDTO.setUserId(userToken.getUserID());
        List<UserReceiveMsgDTO> lstUserReceive = userReceiveMsgRepository
            .getListUserReceiveMsgDTO(msgDTO, 0, Integer.MAX_VALUE, "", "");
        Date startFrom = crDTO.getEarliestStartTime();
        Date startTo = DateTimeUtils.convertStringToDate(crDTO.getEarliestStartTimeTo());
        if (lstUserReceive != null) {
          for (UserReceiveMsgDTO dto : lstUserReceive) {
            Date crStartTime = dto.getCrStartTime();
            if (crStartTime.getTime() >= startFrom.getTime() && crStartTime.getTime() <= startTo
                .getTime()) {
              lstCrMsgAll.add(String.valueOf(dto.getCrId()).toUpperCase());
            }
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }

      List<UserCabCrForm> userCabCrForms = crGeneralRepository.getListUserCab(null, null);
      if (userCabCrForms == null) {
        userCabCrForms = new ArrayList<>();
      }

      Map<String, List<UserCabCrForm>> mapCap = new HashMap<>();
      for (UserCabCrForm form : userCabCrForms) {
        List<UserCabCrForm> lstTemp = mapCap.get(
            form.getImpactSegmentId() + "@" + form.getExecuteUnitId() + "@" + form
                .getCreateUnitId());
        if (lstTemp == null) {
          lstTemp = new ArrayList<>();
        }
        lstTemp.add(form);
        mapCap.put(
            form.getImpactSegmentId() + "@" + form.getExecuteUnitId() + "@" + form
                .getCreateUnitId(),
            lstTemp);
      }

      lstData.forEach(item -> {
        List<UserCabCrForm> lstCap = mapCap.get(item.getImpactSegment() + "@"
            + item.getChangeResponsibleUnit() + "@" + item.getChangeOrginatorUnit());
        if (lstCap == null) {
          lstCap = mapCap.get(item.getImpactSegment() + "@"
              + item.getChangeResponsibleUnit() + "@" + null);
        } else {
          if (mapCap.get(item.getImpactSegment() + "@"
              + item.getChangeResponsibleUnit() + "@" + null) != null) {
            lstCap.addAll(mapCap.get(item.getImpactSegment() + "@"
                + item.getChangeResponsibleUnit() + "@" + null));
          }
        }
        List<ItemDataCRDTO> lst = new ArrayList<>();
        boolean check = false;
        if (lstCap != null) {
          for (UserCabCrForm form : lstCap) {
            ItemDataCRDTO it = new ItemDataCRDTO();
            it.setValueStr(form.getUserCab());
            it.setDisplayStr(form.getUsername());
            lst.add(it);
            if (form.getUserCab() != null && form.getUserCab().equals(item.getUserCab())) {
              check = true;
            }
          }
        }
        if (!check) {
          try {
            if (StringUtils.isNotNullOrEmpty(item.getUserCab())) {
              UsersEntity us = userBusiness.getUserByUserId(Long.valueOf(item.getUserCab()));
              if (us != null) {
                ItemDataCRDTO it = new ItemDataCRDTO();
                it.setValueStr(item.getUserCab());
                it.setDisplayStr(us.getUsername());
                lst.add(it);
              }
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
        }
        item.setLstUserCab(lst);

        //them list Id su dung de checkaction phia duoi
        if (lstCrMsgAll.contains(item.getCrId().toUpperCase())) {
          item.setIsCheckAction(true);
        }
      });

      datatable.setData(lstData);
    }

    return datatable;
  }

  @Override
  public List<String> getSequenseCr(String seqName, int... size) {
    return crRepository.getSequenseCr(seqName, size);
  }

  @Override
  public List<CrInsiteDTO> getListSecondaryCr(CrInsiteDTO crInsiteDTO) {
    return crRepository.getListSecondaryCr(crInsiteDTO);
  }

  @Override
  public List<CrInsiteDTO> getListPreApprovedCr(CrInsiteDTO crInsiteDTO) {
    return crRepository.getListPreApprovedCr(crInsiteDTO);
  }

  @Override
  public ResultInSideDto createObject(CrInsiteDTO tForm) {
    ResultInSideDto retFail = new ResultInSideDto(null, RESULT.ERROR, RESULT.ERROR);
    Date crCreateDate = tForm.getCreatedDate();
    try {
      Boolean check = false;
      if (Constants.CR_STATE.OPEN.toString().equals(tForm.getState())) {
        if (tForm.getRelateToPreApprovedCr() != null) {
//          tForm.setState(Constants.CR_STATE.EVALUATE.toString()); coment nghiep vu cu -> chuyen sang trang thai cho kiem tra dau vao
          tForm.setState(CR_STATE.QUEUE.toString());
          check = true;
        }
        if (tForm.getCrType().equals(Constants.CR_TYPE.EMERGENCY.toString())) {
          tForm.setState(Constants.CR_STATE.APPROVE.toString());
          check = true;
        }
      }
      if (Constants.CR_STATE.QUEUE.toString().equals(tForm.getState()) && !check) {
        //daitt1 : neu la CR link to CR - PreApprove thi chuyen trang thai sang Evaluate
//        if (tForm.getRelateToPreApprovedCr() != null) {
////          tForm.setState(Constants.CR_STATE.EVALUATE.toString()); coment nghiep vu cu -> chuyen sang trang thai cho kiem tra dau vao
//
//        }
        //daitt1 : neu la CR link to CR - PreApprove thi chuyen trang thai sang Evaluate
        check = true;
      }
      //tuanpv14_483701_start
      //Neu la CR khan thi chuyen thang toi buoc cho sap lich
      if (!Constants.CR_STATE.DRAFT.toString().equals(tForm.getState())
          && Constants.CR_TYPE.EMERGENCY.toString().equals(tForm.getCrType())) {
        tForm.setState(Constants.CR_STATE.EVALUATE.toString());
        check = true;
      }

      if (check) {
        String responeTime = crDBRepository.getResponeTimeCR(tForm);
        tForm.setResponeTime(responeTime);
      }

      Date start = new Date();
      tForm.setOriginalEarliestStartTime(
          tForm.getEarliestStartTime()); //bo sung thay cho ham actionUpdateOriginalTime ben duoi
      tForm.setOriginalLatestStartTime(tForm.getLatestStartTime());
      ResultInSideDto ret = crRepository.insertCr(tForm);
      log.info("MESS=====" + ret.getMessage());
      if (!ret.getMessage().equalsIgnoreCase(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        return retFail;
      }
      //crRepository.actionUpdateOriginalTimeOfCR(tForm); khong goi ham nay nua. bo sung luon o tren bang insert truc tiep tu CR
      crRepository.actionUpdateRelatedCR(tForm);
      String retAppDept = crApprovalDepartmentRepository
          .saveListDTONoIdSession(tForm.getLstAppDept());
      if (!retAppDept.equalsIgnoreCase(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        return retFail;
      }

      String retNetNode = crImpactedNodesRepository
          .saveListDTONoIdSession(tForm.getLstNetworkNodeId(), null);
      if (!retNetNode.equalsIgnoreCase(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
        return retFail;
      }
      String retNetNodeAff = crAffectedNodeRepository
          .saveListDTONoIdSession(tForm.getLstNetworkNodeIdAffected(), null);
      if (!retNetNodeAff.equalsIgnoreCase(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        return retFail;
      }
      String retAffSer = crAffectedServiceDetailsRepository
          .saveListDTONoIdSession(tForm.getLstAffectedService());
      if (!retAffSer.equalsIgnoreCase(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        return retFail;
      }

      String retOtherSys = crRepository.actionCreateMappingCRwithOtherSys(tForm);
      if (!retOtherSys.equals(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        return retFail;
      }

      if (tForm.getIsClickedToAlarmTag() != null && tForm.getIsClickedToAlarmTag() == 1) {
        boolean saveAlarm = crAlarmRepository
            .saveOrUpdateList(tForm.getLstAlarn(), ObjectToLong(tForm.getCrId()), crCreateDate);
        if (!saveAlarm) {
          TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
          return retFail;
        }
      }

      if (tForm.getIsClickedToVendorTag() != null && tForm.getIsClickedToVendorTag() == 1) {

        boolean saveVendor = crAlarmRepository
            .saveOrUpdateVendorDetail(tForm.getLstVendorDetail(), ObjectToLong(tForm.getCrId()),
                crCreateDate);
        if (!saveVendor) {
          TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
          return retFail;
        }
      }

      if (tForm.getIsClickedToModuleTag() != null && tForm.getIsClickedToModuleTag() == 1) {

        boolean saveModule = crAlarmRepository
            .saveOrUpdateModuleDetail(tForm.getLstModuleDetail(), ObjectToLong(tForm.getCrId()),
                crCreateDate);
        if (!saveModule) {
          TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
          return retFail;
        }
      }

      if (tForm.getIsClickedToCableTag() != null && tForm.getIsClickedToCableTag() == 1) {

        boolean saveCable = crCableRepository
            .saveOrUpdateCableDetail(tForm.getLstCable(), ObjectToLong(tForm.getCrId()),
                crCreateDate);
        if (!saveCable) {
          TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
          return retFail;
        }
      }

      if (tForm.getRelateToPreApprovedCr() != null) {
        CrHisDTO crHis = new CrHisDTO();
        crHis.setCrId(ret.getId() == null ? null : String.valueOf(ret.getId()));
        crHis.setComments(tForm.getActionNotes());
        if (tForm.getCrCreatedFromOtherSysDTO() != null
            && tForm.getCrCreatedFromOtherSysDTO().getSystemId() != null) {
          String systemId = tForm.getCrCreatedFromOtherSysDTO().getSystemId();
          crHis.setComments(CrCreatedFromOtherSysDTO.SYSTEM.getGetText().get(systemId) + ". " + (
              (crHis.getComments() == null) ? "" : crHis.getComments()));
        }
        crHis.setNotes("");
        crHis.setStatus(tForm.getState());
        crHis.setActionCode(Constants.CR_ACTION_CODE.ADDNEW.toString());
        crHis.setUnitId(tForm.getUserLoginUnit());
        crHis.setUserId(tForm.getUserLogin());
        crHis.setEarliestStartTime(DateTimeUtils.date2ddMMyyyyHHMMss(tForm.getEarliestStartTime()));
        crHis.setLatestStartTime(DateTimeUtils.date2ddMMyyyyHHMMss(tForm.getLatestStartTime()));
        crHisRepository.insertOrUpdate(crHis);
        ResultInSideDto retResult = new ResultInSideDto();
        retResult.setId(ret.getId());
        retResult.setKey(Constants.CR_RETURN_MESSAGE.SUCCESS);
        //retResult.setMessage(ret.getMessage());
        retResult.setMessage(Constants.CR_RETURN_MESSAGE.SUCCESS);
        return retResult;
        //daitt1 : neu la CR link to CR - PreApprove thi chuyen trang thai sang Evaluate
      } else if (Constants.CR_STATE.OPEN.toString().equals(tForm.getState())) {
        ResultDTO retAutoApprove = crRepository
            .actionAutomaticApproveCrIncaseOfManagerCreateOrEditCR(tForm, null);
        if (retAutoApprove.getKey().equals(Constants.CR_RETURN_MESSAGE.SUCCESS)
            || retAutoApprove.getKey().equals(Constants.CR_RETURN_MESSAGE.APPROVEINFIRSTPLACE)
            || retAutoApprove.getKey().equals(Constants.CR_RETURN_MESSAGE.APPROVECRINFIRSTPLACE)) {
          CrHisDTO crHis = new CrHisDTO();
          crHis.setCrId(ret.getId() == null ? null : String.valueOf(ret.getId()));
          crHis.setComments(tForm.getActionNotes());
          if (tForm.getCrCreatedFromOtherSysDTO() != null
              && tForm.getCrCreatedFromOtherSysDTO().getSystemId() != null) {
            String systemId = tForm.getCrCreatedFromOtherSysDTO().getSystemId();
            crHis.setComments(CrCreatedFromOtherSysDTO.SYSTEM.getGetText().get(systemId) + ". " + (
                (crHis.getComments() == null) ? "" : crHis.getComments()));
          }
          crHis.setNotes("");
          crHis.setStatus(tForm.getState());
          crHis.setActionCode(Constants.CR_ACTION_CODE.ADDNEW.toString());
          crHis.setUnitId(tForm.getUserLoginUnit());
          crHis.setEarliestStartTime(
              DateTimeUtils.date2ddMMyyyyHHMMss(tForm.getEarliestStartTime()));
          crHis.setLatestStartTime(DateTimeUtils.date2ddMMyyyyHHMMss(tForm.getLatestStartTime()));
          crHis.setUserId(tForm.getUserLogin());
          crHisRepository.insertOrUpdate(crHis);
          ResultInSideDto retResult = new ResultInSideDto();
          retResult.setId(ret.getId());
          retResult.setKey(Constants.CR_RETURN_MESSAGE.SUCCESS);
          //retResult.setMessage(retAutoApprove.getMessage());
          retResult.setMessage(Constants.CR_RETURN_MESSAGE.SUCCESS);
          return retResult;
        }
        return retFail;
      } else {
        CrHisDTO crHis = new CrHisDTO();
        crHis.setCrId(ret.getId() == null ? null : String.valueOf(ret.getId()));
        crHis.setComments(tForm.getActionNotes());
        if (tForm.getCrCreatedFromOtherSysDTO() != null
            && tForm.getCrCreatedFromOtherSysDTO().getSystemId() != null) {
          String systemId = tForm.getCrCreatedFromOtherSysDTO().getSystemId();
          crHis.setComments(CrCreatedFromOtherSysDTO.SYSTEM.getGetText().get(systemId) + ". " + (
              (crHis.getComments() == null) ? "" : crHis.getComments()));
        }
        crHis.setNotes("");
        crHis.setStatus(tForm.getState());
        crHis.setActionCode(Constants.CR_ACTION_CODE.ADDNEW.toString());
        if (Constants.CR_STATE.DRAFT.toString().equals(tForm.getState())) {
          crHis.setActionCode(Constants.CR_ACTION_CODE.SAVEDRAFT.toString());
        }
        crHis.setEarliestStartTime(DateTimeUtils.date2ddMMyyyyHHMMss(tForm.getEarliestStartTime()));
        crHis.setLatestStartTime(DateTimeUtils.date2ddMMyyyyHHMMss(tForm.getLatestStartTime()));
        crHis.setUnitId(tForm.getUserLoginUnit());
        crHis.setUserId(tForm.getUserLogin());
        crHisRepository.insertOrUpdate(crHis);
        ResultInSideDto retResult = new ResultInSideDto();
        retResult.setId(ret.getId());
        retResult.setKey(Constants.CR_RETURN_MESSAGE.SUCCESS);
        //retResult.setMessage(ret.getMessage());
        retResult.setMessage(Constants.CR_RETURN_MESSAGE.SUCCESS);
        Date end = new Date();
        log.debug("dbTime", "" + (end.getTime() - start.getTime()));
        return retResult;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return retFail;
  }

  @Override
  public ResultInSideDto addNewCrClient(CrInsiteDTO crDTO) {
    StringUtils.printLogData("addNewCr", crDTO, CrInsiteDTO.class);
    UserToken userToken = ticketProvider.getUserToken();
    crDTO.setCrNumber(crDTO.getCrNumber().toUpperCase(Locale.US));
    getForm(crDTO, userToken);
    crDTO = crProcessFromClient.getFormForProcess(crDTO);
    crDTO.setCreatedDate(new Date());
    crDTO.setUpdateTime(new Date());
    getInsertSourcePrimaryCr(crDTO);
    String msg = crProcessFromClient.validateForm(crDTO);
    if (msg.length() > 0) {
      return new ResultInSideDto(null, RESULT.ERROR, msg);
    }
    ResultInSideDto retSaveCR = createObject(crDTO);
    CrCreatedFromOtherSysDTO ccfosdto = crDTO.getCrCreatedFromOtherSysDTO();
    if (RESULT.SUCCESS.equalsIgnoreCase(retSaveCR.getKey())) {
      try {
        List<AttachDtDTO> attachDtDTO = crDTO.getAttachDtDTO();
        if (attachDtDTO != null && !attachDtDTO.isEmpty()) {
          List<String> lstMop = new ArrayList<>();
          for (AttachDtDTO dto : attachDtDTO) {
            lstMop.add(dto.getDtCode());
          }
          if ("1".equals(attachDtDTO.get(0).getSystemCode())) {
            wsVipaIpPort.updateCrCodeForMops(lstMop, crDTO.getCrNumber());
            wsVipaIpPort.updateCrStatusWithMops(crDTO.getCrNumber(), crDTO.getState(), lstMop);
          } else if ("2".equals(attachDtDTO.get(0).getSystemCode())) {
            wsVipaDdPort.updateCrCodeForMops(lstMop, crDTO.getCrNumber());
            wsVipaDdPort.updateCrStatusWithMops(crDTO.getCrNumber(), crDTO.getState(), lstMop);

          } else {
            //link CR
            for (AttachDtDTO dto : attachDtDTO) {
              wstdttPort.linkCr(crDTO.getCrNumber(), dto.getDtCode(),
                  userToken.getUserName(), crDTO.getTitle(),
                  DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getEarliestStartTime()),
                  DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getLatestStartTime()),
                  Long.valueOf(crDTO.getState()));
            }
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      //tuanpv14_start
      if (ccfosdto != null) {
        ccfosdto.setCrId(String.valueOf(retSaveCR.getId()));
      }
      List<String> addedWoIdList = new ArrayList<>();
      //CR khan phai qua buoc sap lich moi day WO
      List<WoDTO> lstSubWo = crProcessFromClient
          .getListSubWo(String.valueOf(retSaveCR.getId()), null, crDTO.getCreatedDate(),
              addedWoIdList);
      if (Constants.CR_TYPE.STANDARD.toString().equals(crDTO.getCrType())) {//Cr chuan
        //Neu nguoi tao la TP hoac khong tich chon phe duyet thi day wo
        if (Constants.CR_STATE.APPROVE.toString().equals(crDTO.getState()) || (crProcessFromClient
            .checkCreateUserRoll(crDTO.getChangeOrginator()))) {
          crProcessFromClient
              .updateWoWhenChangeCr(lstSubWo, Constants.WO_STATUS_NEW.UNASSIGNED,
                  I18n.getLanguage("woTest.woCrCreateTP"));
        }
      }

      if (Constants.CR_STATE.QUEUE.toString().equals(crDTO.getState())
          && crDTO.getRelateToPreApprovedCr() != null) {
        crProcessFromClient.updateWoWhenChangeCr(lstSubWo, Constants.WO_STATUS_NEW.UNASSIGNED,
            I18n.getLanguage("woTest.woCrSchedule"));
      }
      //cap nhat thoi gian cua Wo theo CR
      if (lstSubWo != null && !lstSubWo.isEmpty()) {
        Map<String, Double> mapWoDuration = crProcessFromClient
            .getMapDurationByProcessId(crDTO.getProcessTypeId());
        crProcessFromClient.updateWoByCrTime(lstSubWo,
            DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getEarliestStartTime()),
            DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getLatestStartTime()), mapWoDuration,
            crDTO.getCrProcessId(),
            crDTO.getDutyType());
      }

      //nếu lưu CR thành công va Đang đợi Mop từ VMSA thì thự hiện load MOP
      if (CrConfig.WAITING_MOP_STATUS.equals(crDTO.getWaitingMopStatus())) {
        crProcessFromClient.loadVMSAMop(crDTO);
      }
      // Save file addition
      if (GNOC_FILE_BUSSINESS.SR.equals(crDTO.getOtherSystemType())
          && crDTO.getOtherSystemId() != null) {
        try {
          UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
          List<GnocFileDto> gnocFileDtos = new ArrayList<>();
          GnocFileDto gnocFileSrDto = new GnocFileDto();
          gnocFileSrDto.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
          gnocFileSrDto.setBusinessId(crDTO.getOtherSystemId());
          List<GnocFileDto> gnocFileSrDtos = gnocFileRepository.getListGnocFileByDto(gnocFileSrDto);
          for (GnocFileDto gnocFileSrDtoAdd : gnocFileSrDtos) {
            if (StringUtils.isNotNullOrEmpty(gnocFileSrDtoAdd.getPath())) {
              String fileName = gnocFileSrDtoAdd.getFileName();
              byte[] bytes = FileUtils
                  .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                      PassTranformer.decrypt(ftpPass), gnocFileSrDtoAdd.getPath());
              String fullPath = FileUtils
                  .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                      PassTranformer.decrypt(ftpPass), ftpFolder, fileName, bytes, null);
              //Start save file old
              String fullPathOld = FileUtils
                  .saveUploadFile(fileName, bytes, uploadFolder, new Date());
              CrFilesAttachInsiteDTO crFilesAttachInsiteDTOAdd = new CrFilesAttachInsiteDTO();
              crFilesAttachInsiteDTOAdd.setFileName(FileUtils.getFileName(fullPathOld));
              crFilesAttachInsiteDTOAdd.setUserId(userToken.getUserID());
              crFilesAttachInsiteDTOAdd.setCrId(retSaveCR.getId());
              crFilesAttachInsiteDTOAdd
                  .setTimeAttack(DateTimeUtils.convertStringToDate(DateTimeUtils.getSysDateTime()));
              crFilesAttachInsiteDTOAdd.setFileType(CR_FILE_TYPE.OTHER);
              crFilesAttachInsiteDTOAdd.setFilePath(fullPathOld);
              ResultInSideDto resultFileDataOld = crFilesAttachRepository
                  .add(crFilesAttachInsiteDTOAdd);
              //End save file old
              GnocFileDto gnocFileDto = new GnocFileDto();
              gnocFileDto.setPath(fullPath);
              gnocFileDto.setFileName(fileName);
              gnocFileDto.setFileType(CR_FILE_TYPE.OTHER);
              gnocFileDto.setTemplateId(crFilesAttachInsiteDTOAdd.getTempImportId());
              gnocFileDto.setCreateUnitId(userToken.getDeptId());
              gnocFileDto.setCreateUnitName(unitToken.getUnitName());
              gnocFileDto.setCreateUserId(userToken.getUserID());
              gnocFileDto.setCreateUserName(userToken.getUserName());
              gnocFileDto.setCreateTime(new Date());
              gnocFileDto.setMappingId(resultFileDataOld.getId());
              gnocFileDtos.add(gnocFileDto);
            }
          }
          gnocFileRepository
              .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.CR, retSaveCR.getId(),
                  gnocFileDtos);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          throw new RuntimeException(I18n.getChangeManagement("sr.notAttachFile"));
        }
      }
    } else {
      String ret = I18n.getChangeManagement("cr.msg.add.unsuccess");
      retSaveCR.setMessage(ret);
    }
    //lien ket sang Gate
    try {
      crRepository.flushSession();
      CrInsiteDTO form = crRepository.findCrById(Long.valueOf(crDTO.getCrId()));
      if (!Constants.CR_STATE.DRAFT.toString().equals(form.getState())
          && Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType())
          && form.getRankGate() != null) {
        wsGatePort.updateECR(userToken.getUserName(), crDTO.getCrNumber(), crDTO.getTitle(),
            Long.valueOf(form.getState()),
            StringUtils
                .convertKeyToValueByMap(Constants.CR_STATE.getGetStateName(), form.getState()),
            form.getRankGate(), "1");
      }
      updateToRDM(String.valueOf(retSaveCR.getId()), crDTO, userToken);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return retSaveCR;
  }

  private void getInsertSourcePrimaryCr(CrInsiteDTO crDTO) {
    if ("11".equals(crDTO.getSubcategory()) && StringUtils
        .isNotNullOrEmpty(crDTO.getRelateToPrimaryCr())) {
      crDTO.setLstCrCreatedFromOtherSysDTO(crForOtherSystemBusiness
          .getListData(Long.valueOf(crDTO.getRelateToPrimaryCr()), null, null));
    }
  }

  @Override
  public ResultInSideDto onUpdateCrClient(CrInsiteDTO crDTO) {
    StringUtils.printLogData("onUpdateCr", crDTO, CrInsiteDTO.class);
    CrInsiteDTO form = crProcessFromClient.getFormForProcess(crDTO);
    UserToken userToken = ticketProvider.getUserToken();
    getForm(crDTO, userToken);
    CrCreatedFromOtherSysDTO ccfosdto = crDTO.getCrCreatedFromOtherSysDTO();
    String crId = crDTO.getCrId();
    String msg = crProcessFromClient.validateForm(crDTO);
    if (msg.length() > 0) {
      return new ResultInSideDto(null, RESULT.ERROR, msg);
    }
    getInsertSourcePrimaryCr(crDTO);
    ResultDTO retUpdateCR = updateCrWithNoti(crDTO, I18n.getLocale());
    if (retUpdateCR != null && retUpdateCR.getKey()
        .equalsIgnoreCase(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
      try {
        CrFilesAttachInsiteDTO crFile = new CrFilesAttachInsiteDTO();
        crFile.setCrId(Long.valueOf(crDTO.getCrId()));
        List<CrFilesAttachInsiteDTO> lstAttachment = crFilesAttachRepository
            .getListCrFilesAttachDTO(crFile, 0, 1000, "", "");
        String dtCode = "";
        String dtHis = "";

        List<String> lstMop = new ArrayList<>();
        if (lstAttachment != null) {
          for (CrFilesAttachInsiteDTO dto : lstAttachment) {
            if (dto.getDtCode() != null) {
              dtCode = dto.getDtCode();
              dtHis = dto.getDtFileHistory();
              if (!lstMop.contains(dtCode)) {
                lstMop.add(dtCode);
              }
            }
          }
        }

        if (dtCode != null && !"".equals(dtCode)) {
          if (dtHis != null && dtHis.contains("VIPA_IP")) {
            wsVipaIpPort.updateCrStatusWithMops(crDTO.getCrNumber(), crDTO.getState(), lstMop);
          } else if (dtHis != null && dtHis.contains("VIPA_DD")) {
            CrInsiteDTO crTemp = findCrById(Long.parseLong(crDTO.getCrId()));
            if ("1".equals(crTemp.getIsLoadMop())) {
              lstMop = new ArrayList<>();
              if (lstAttachment != null) {
                for (CrFilesAttachInsiteDTO dto : lstAttachment) {
                  if (dto.getDtCode() != null) {
                    dtCode = dto.getDtCode();
                    if (!lstMop.contains(dtCode) && StringUtils
                        .isStringNullOrEmpty(dto.getIsRun())) {
                      lstMop.add(dtCode);
                    }
                  }
                }
              }
            } else if ("2".equals(crTemp.getIsLoadMop())) {
              crFile = new CrFilesAttachInsiteDTO();
              crFile.setCrId(Long.parseLong(crTemp.getRelateToPreApprovedCr()));
              crFile.setIsRun(Long.parseLong(crTemp.getCrId()));
              lstAttachment = crFilesAttachRepository
                  .getListCrFilesAttachDTO(crFile, 0, 1000, "", "");
              lstMop = new ArrayList<>();
              if (lstAttachment != null && !lstAttachment.isEmpty()) {
                for (CrFilesAttachInsiteDTO dto : lstAttachment) {
                  if (dto.getDtCode() != null) {
                    dtCode = dto.getDtCode();
                    if (!lstMop.contains(dtCode)) {
                      lstMop.add(dtCode);
                    }
                  }
                }
              }
            }
            if (lstMop != null && !lstMop.isEmpty()) {
              wsVipaDdPort.updateCrStatusWithMops(crDTO.getCrNumber(), crDTO.getState(), lstMop);
            }
          } else {
            //link CR sang CNTT
            if (form.getChangeOrginatorName() != null
                && form.getChangeOrginatorName().indexOf("(") > 0) {
              wstdttPort.linkCr(crDTO.getCrNumber(), dtCode,
                  form.getChangeOrginatorName()
                      .substring(0, form.getChangeOrginatorName().indexOf("(")).trim(),
                  crDTO.getTitle(), DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getEarliestStartTime()),
                  DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getLatestStartTime()),
                  Long.valueOf(crDTO.getState()));
            }
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);

      }
//                            }
      //tuannpv14_R517456_end link cr khi chuyen trang thai
      //tuanpv14_start
      if (ccfosdto != null) {
        ccfosdto.setCrId(crDTO.getCrId());
      }
      List<String> addedWoIdList = new ArrayList<>();
      if (StringUtils.isNotNullOrEmpty(crDTO.getListWoId())) {
        String[] woIds = crDTO.getListWoId().trim().split(",");
        addedWoIdList.addAll(Arrays.asList(woIds));
      }
      //cap nhat thoi gian cua Wo theo CR
      List<WoDTO> lstSubWo = crProcessFromClient
          .getListSubWo(crDTO.getCrId(), null, crDTO.getCreatedDate(), addedWoIdList);
      if (lstSubWo != null && !lstSubWo.isEmpty()) {
        if (Constants.CR_TYPE.STANDARD.toString().equals(crDTO.getCrType())) {//Cr chuan
          //Neu nguoi tao la TP hoac khong tich chon phe duyet thi day wo
          if (Constants.CR_STATE.APPROVE.toString().equals(crDTO.getState()) || (crProcessFromClient
              .checkCreateUserRoll(crDTO.getChangeOrginator()))) {
            crProcessFromClient.updateWoWhenChangeCr(lstSubWo, Constants.WO_STATUS_NEW.UNASSIGNED,
                I18n.getLanguage("woTest.woCrCreateTP"));
          }
        }
        Map<String, Double> mapWoDuration = crProcessFromClient
            .getMapDurationByProcessId(crDTO.getProcessTypeId());
        //cap nhat thoi gian cua Wo theo CR
        crProcessFromClient.updateWoByCrTime(lstSubWo,
            DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getEarliestStartTime()),
            DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getLatestStartTime()), mapWoDuration,
            crDTO.getCrProcessId(), crDTO.getDutyType());
      }

      //lien ket sang Gate
      crRepository.flushSession();
      CrInsiteDTO formRoot = crRepository
          .findCrById(Long.valueOf(crDTO.getCrId()), ticketProvider.getUserToken());
      if (!Constants.CR_STATE.DRAFT.toString().equals(formRoot.getState())
          && Constants.CR_TYPE.EMERGENCY.toString().equals(formRoot.getCrType())
          && form.getRankGate() != null) {
        wsGatePort.updateECR(userToken.getUserName(), crDTO.getCrNumber(), crDTO.getTitle(),
            Long.valueOf(formRoot.getState()),
            StringUtils
                .convertKeyToValueByMap(Constants.CR_STATE.getGetStateName(), formRoot.getState()),
            formRoot.getRankGate(), "1");
      }
    } else {
      retUpdateCR.setKey(RESULT.ERROR);
      retUpdateCR.setMessage(
          I18n.getChangeManagement("cr.msg.update.unsuccess") + ": " + retUpdateCR.getKey());
    }
    updateToRDM(crId, crDTO, userToken);
    return new ResultInSideDto(null, retUpdateCR.getKey(), retUpdateCR.getMessage());

  }

  private void updateToRDM(String crId, CrInsiteDTO crDTO, UserToken userToken) {
    try {
      if (!StringUtils.isStringNullOrEmpty(crId)) {
        List<CrCreatedFromOtherSysDTO> lstOther = crForOtherSystemBusiness
            .getListData(Long.parseLong(crId), 6L, null);
        if (lstOther != null && !lstOther.isEmpty()) {
          Gson gson = new Gson();
          Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();

          String urlRDM = mapConfigProperty.get("url_rdm") + "updateTaskWS?timeZoneOffset=0";
          List<RdmUpdateDTO> lst = new ArrayList<>();
          for (CrCreatedFromOtherSysDTO otherSysDTO : lstOther) {
            RdmUpdateDTO rdmDTO = new RdmUpdateDTO();
            rdmDTO.setGnocTaskCode(crDTO.getCrNumber());
            rdmDTO.setGnocTaskName(crDTO.getTitle());
            rdmDTO.setGnocTaskType("CR");
            rdmDTO.setGnocTaskStatus(crDTO.getState());
            rdmDTO
                .setGnocTaskUserPic(userToken.getUserName() + " (" + userToken.getFullName() + ")");
            rdmDTO.setTaskId(otherSysDTO.getObjectId());
            rdmDTO.setGnocId(crId);
            rdmDTO.setGnocTaskFromDate(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(crDTO.getEarliestStartTime()));
            rdmDTO.setGnocTaskToDate(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(crDTO.getLatestStartTime()));
            System.out.println(rdmDTO);
            lst.add(rdmDTO);
          }
          String urlParameters = gson.toJson(lst);

          URL url = new URL(urlRDM);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();

          //add reuqest header
          conn.setRequestMethod("POST");
          conn.setRequestProperty("CLIENT_NAME", "API_WS");
          conn.setRequestProperty("content-type", "application/json;  charset=utf-8");
          // Send post request
          conn.setDoOutput(true);
          //                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
          OutputStream os = conn.getOutputStream();
          OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
          osw.write(urlParameters);
          osw.flush();
          osw.close();

          int responseCode = conn.getResponseCode();
          if (responseCode != 200) {
            throw new RuntimeException("Failed : RDM error code : " + conn.getResponseCode());
          }

          BufferedReader br = new BufferedReader(
              new InputStreamReader(conn.getInputStream(), "utf-8"));
          StringBuilder sb = new StringBuilder();
          String line = null;
          while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
          }
          br.close();
          if (StringUtils.isStringNullOrEmpty(sb.toString()) || !sb.toString().toUpperCase()
              .contains("SUCCESS")) {
            throw new RuntimeException("Update to RDM fail : " + sb.toString());
          }
          conn.disconnect();
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  @Override
  public ResultDTO updateCrWithNoti(CrInsiteDTO tForm, String locale) {
    StringUtils.printLogData("updateCrWithNoti", tForm, CrInsiteDTO.class);
    ResultDTO returnResult = new ResultDTO();
    String retFail = Constants.CR_RETURN_MESSAGE.ERROR;
    try {

      Date crCreateDate = null;
      try {
        crCreateDate = tForm.getCreatedDate();
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }

      Boolean check = false;
      if (Constants.CR_STATE.OPEN.toString().equals(tForm.getState())) {
        //daitt1 : neu la CR link to CR - PreApprove thi chuyen trang thai sang Evaluate
        if (tForm.getRelateToPreApprovedCr() != null) {
//          tForm.setState(Constants.CR_STATE.EVALUATE.toString());
          tForm.setState(CR_STATE.QUEUE.toString());
          check = true;
        }
        if (Constants.CR_TYPE.EMERGENCY.toString().equals(tForm.getCrType())) {
          tForm.setState(Constants.CR_STATE.APPROVE.toString());
          check = true;
        }
        //daitt1 : neu la CR link to CR - PreApprove thi chuyen trang thai sang Evaluate
      }
      if (Constants.CR_STATE.QUEUE.toString().equals(tForm.getState()) && !check) {
        //daitt1 : neu la CR link to CR - PreApprove thi chuyen trang thai sang Evaluate
//        if (tForm.getRelateToPreApprovedCr() != null) {
//          tForm.setState(Constants.CR_STATE.EVALUATE.toString());
//        }
        //daitt1 : neu la CR link to CR - PreApprove thi chuyen trang thai sang Evaluate
        check = true;
      }
      //tuanpv14_483701_start
      //Neu la CR khan thi chuyen thang toi buoc cho sap lich
      if (!Constants.CR_STATE.DRAFT.toString().equals(tForm.getState())
          && Constants.CR_TYPE.EMERGENCY.toString().equals(tForm.getCrType())) {
        tForm.setState(Constants.CR_STATE.EVALUATE.toString());
      }
      //tuanpv14_483701_end
      String actionCode = Constants.CR_ACTION_CODE.UPDATE.toString();
      if (tForm.getActionType() != null) {
        actionCode = tForm.getActionType();
        if (Constants.CR_ACTION_CODE.UPDATE_CR_WHEN_RECEIVE_STD.toString().equals(actionCode)) {
          tForm.setState(Constants.CR_STATE.APPROVE.toString());
        }
      }
      if (check) {
        String responeTime = crDBRepository.getResponeTimeCR(tForm);
        tForm.setResponeTime(responeTime);
      }

      tForm.setOriginalEarliestStartTime(tForm.getEarliestStartTime());
      tForm.setOriginalLatestStartTime(tForm.getLatestStartTime());
      String ret = crRepository
          .updateCr(tForm); //To change body of generated methods, choose Tools | Templates.
      //crRepository.actionUpdateOriginalTimeOfCR(tForm);

      if (!ret.equalsIgnoreCase(RESULT.SUCCESS)) {
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        returnResult.setKey(retFail);
        return returnResult;
      }

      //crRepository.actionUpdateOriginalTimeOfCR(tForm);
      crRepository.actionUpdateRelatedCR(tForm);
      crApprovalDepartmentRepository.deleteAppDeptByCrId(tForm.getCrId());

      String retAppDept = crApprovalDepartmentRepository
          .saveListDTONoIdSession(tForm.getLstAppDept());
      if (!retAppDept.equalsIgnoreCase(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        returnResult.setKey(retFail);
        return returnResult;
      }

      String retNetNode = deleteAndUpdateNetworkNode(tForm);
      if (!retNetNode.equalsIgnoreCase(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        returnResult.setKey(retFail);
        return returnResult;
      }

      String retNetNodeAff = deleteAndUpdateNetworkNodeAffected(tForm);
      if (!retNetNodeAff.equalsIgnoreCase(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        returnResult.setKey(retFail);
        return returnResult;
      }

      crAffectedServiceDetailsRepository.deleteAffSerByCrId(tForm.getCrId());
      String retAffSer = crAffectedServiceDetailsRepository
          .saveListDTONoIdSession(tForm.getLstAffectedService());
      if (!retAffSer.equalsIgnoreCase(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        returnResult.setKey(retFail);
        return returnResult;
      }

      String retOtherSys = crRepository.actionCreateMappingCRwithOtherSys(tForm);
      if (!retOtherSys.equals(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        returnResult.setKey(retFail);
        return returnResult;
      }

      String retReset = crRepository.actionResetApproveCRIncaseOfEditCR(tForm);
      if (!retReset.equals(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        returnResult.setKey(retFail);
        return returnResult;
      }

      if (tForm.getIsClickedToAlarmTag() != null && tForm.getIsClickedToAlarmTag() == 1) {
        boolean saveAlarm = crAlarmRepository
            .saveOrUpdateList(tForm.getLstAlarn(), ObjectToLong(tForm.getCrId()), crCreateDate);
        if (!saveAlarm) {
          TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
          returnResult.setKey(retFail);
          return returnResult;
        }
      }

      if (tForm.getIsClickedToVendorTag() != null && tForm.getIsClickedToVendorTag() == 1) {
        boolean saveVendor = crAlarmRepository
            .saveOrUpdateVendorDetail(tForm.getLstVendorDetail(), ObjectToLong(tForm.getCrId()),
                crCreateDate);
        if (!saveVendor) {
          TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
          returnResult.setKey(retFail);
          return returnResult;
        }
      }

      if (tForm.getIsClickedToModuleTag() != null && tForm.getIsClickedToModuleTag() == 1) {
        boolean saveModule = crAlarmRepository
            .saveOrUpdateModuleDetail(tForm.getLstModuleDetail(), ObjectToLong(tForm.getCrId()),
                crCreateDate);
        if (!saveModule) {
          TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
          returnResult.setKey(retFail);
          return returnResult;
        }
      }

      if (tForm.getIsClickedToCableTag() != null && tForm.getIsClickedToCableTag() == 1) {

        boolean saveCable = crCableRepository
            .saveOrUpdateCableDetail(tForm.getLstCable(), ObjectToLong(tForm.getCrId()),
                crCreateDate);
        if (!saveCable) {
          TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
          returnResult.setKey(retFail);
          return returnResult;
        }
      }

      //daitt1 : neu la CR link to CR - PreApprove thi chuyen trang thai sang Evaluate
      if (tForm.getRelateToPreApprovedCr() != null) {
        CrHisDTO crHis = new CrHisDTO();
        crHis.setCrId(tForm.getCrId());
        crHis.setComments(tForm.getActionNotes());
        crHis.setNotes("");
        crHis.setStatus(tForm.getState());
        crHis.setActionCode(actionCode);
        crHis.setUnitId(tForm.getUserLoginUnit());
        crHis.setUserId(tForm.getUserLogin());
        crHis.setEarliestStartTime((tForm.getEarliestStartTime() == null) ? ""
            : DateTimeUtils.date2ddMMyyyyHHMMss(tForm.getEarliestStartTime()));
        crHis.setLatestStartTime((tForm.getLatestStartTime() == null) ? ""
            : DateTimeUtils.date2ddMMyyyyHHMMss(tForm.getLatestStartTime()));
        crHisRepository.insertOrUpdate(crHis);
        returnResult.setKey(Constants.CR_RETURN_MESSAGE.SUCCESS);
        return returnResult;
        //daitt1 : neu la CR link to CR - PreApprove thi chuyen trang thai sang Evaluate
      } else if (Constants.CR_STATE.OPEN.toString().equals(tForm.getState())) {
        ResultDTO retAutoApprove = crRepository
            .actionAutomaticApproveCrIncaseOfManagerCreateOrEditCR(tForm, locale);
        if (retAutoApprove.getKey().equals(Constants.CR_RETURN_MESSAGE.SUCCESS)
            || retAutoApprove.getKey().equals(Constants.CR_RETURN_MESSAGE.APPROVEINFIRSTPLACE)
            || retAutoApprove.getKey().equals(Constants.CR_RETURN_MESSAGE.APPROVECRINFIRSTPLACE)) {
          CrHisDTO crHis = new CrHisDTO();
          crHis.setCrId(tForm.getCrId());
          crHis.setComments(tForm.getActionNotes());
          if (tForm.getCrCreatedFromOtherSysDTO() != null
              && tForm.getCrCreatedFromOtherSysDTO().getSystemId() != null) {
            String systemId = tForm.getCrCreatedFromOtherSysDTO().getSystemId();
            crHis.setComments(CrCreatedFromOtherSysDTO.SYSTEM.getGetText().get(systemId) + ". " + (
                (crHis.getComments() == null) ? "" : crHis.getComments()));
          }
          crHis.setNotes("");
          crHis.setStatus(tForm.getState());
          crHis.setActionCode(actionCode);
          if (Constants.CR_STATE.DRAFT.toString().equals(tForm.getState())) {
            crHis.setActionCode(Constants.CR_ACTION_CODE.SAVEDRAFT.toString());
          }
          crHis.setUnitId(tForm.getUserLoginUnit());
          crHis.setUserId(tForm.getUserLogin());
          crHis.setEarliestStartTime(
              DateTimeUtils.date2ddMMyyyyHHMMss(tForm.getEarliestStartTime()));
          crHis.setLatestStartTime(DateTimeUtils.date2ddMMyyyyHHMMss(tForm.getLatestStartTime()));
          crHisRepository.insertOrUpdate(crHis);
          returnResult.setKey(Constants.CR_RETURN_MESSAGE.SUCCESS);
          returnResult.setMessage(retAutoApprove.getMessage());
          return returnResult;
        }
        returnResult.setKey(retFail);
        return returnResult;
      } else {
        CrHisDTO crHis = new CrHisDTO();
        crHis.setCrId(tForm.getCrId());
        crHis.setComments(tForm.getActionNotes());
        if (tForm.getCrCreatedFromOtherSysDTO() != null
            && tForm.getCrCreatedFromOtherSysDTO().getSystemId() != null) {
          String systemId = tForm.getCrCreatedFromOtherSysDTO().getSystemId();
          crHis.setComments(CrCreatedFromOtherSysDTO.SYSTEM.getGetText().get(systemId) + ". " + (
              (crHis.getComments() == null) ? "" : crHis.getComments()));
        }
        crHis.setNotes("");
        crHis.setStatus(tForm.getState());
        crHis.setActionCode(actionCode);
        if (Constants.CR_STATE.DRAFT.toString().equals(tForm.getState())) {
          crHis.setActionCode(Constants.CR_ACTION_CODE.SAVEDRAFT.toString());
        }
        crHis.setUnitId(tForm.getUserLoginUnit());
        crHis.setUserId(tForm.getUserLogin());
        crHis.setEarliestStartTime(
            DateTimeUtils.date2ddMMyyyyHHMMss(tForm.getEarliestStartTime()));
        crHis.setLatestStartTime(DateTimeUtils.date2ddMMyyyyHHMMss(tForm.getLatestStartTime()));
        crHisRepository.insertOrUpdate(crHis);
        returnResult.setKey(Constants.CR_RETURN_MESSAGE.SUCCESS);
        returnResult.setMessage(Constants.CR_RETURN_MESSAGE.SUCCESS);
        return returnResult;
      }

    } catch (Exception e) {
      TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
      log.error(e.getMessage(), e);
    }
    returnResult.setKey(retFail);
    return returnResult;
  }

  private String deleteAndUpdateNetworkNode(CrInsiteDTO cdto) {
    if ("1".equals(cdto.getIsClickedNode())) {
      //21042016_LinhVM_Bo sung dieu kien partition cho bang CR_IMPACTED_NODES
      if (cdto.getCreatedDate() == null) {
        //LinhVM: lay CR theo Id de xac dinh create_date
        CrInsiteDTO researchCR = crGeneralRepository.getCRByIDIn30Day(cdto.getCrId());
        if (researchCR == null) {
          crImpactedNodesRepository.deleteImpactNodeByCrId(cdto.getCrId(), null);
        } else {
          crImpactedNodesRepository
              .deleteImpactNodeByCrId(cdto.getCrId(), researchCR.getCreatedDate());
        }
      } else {
        crImpactedNodesRepository.deleteImpactNodeByCrId(cdto.getCrId(), cdto.getCreatedDate());
      }
      String retNetNode = crImpactedNodesRepository
          .saveListDTONoIdSession(cdto.getLstNetworkNodeId(), null);
      return retNetNode;
    } else {
      return Constants.CR_RETURN_MESSAGE.SUCCESS;
    }
  }

  private String deleteAndUpdateNetworkNodeAffected(CrInsiteDTO cdto) {
    if ("1".equals(cdto.getIsClickedNodeAffected())) {
      //21042016_LinhVM_Bo sung dieu kien partition cho lenh delete
      if (cdto.getCreatedDate() == null) {
        //LinhVM: lay CR theo Id de xac dinh create_date
        CrInsiteDTO researchCR = crGeneralRepository.getCRByIDIn30Day(cdto.getCrId());
        if (researchCR == null) {
          crAffectedNodeRepository.deleteAffectedNodeByCrId(cdto.getCrId(), null);
        } else {
          crAffectedNodeRepository
              .deleteAffectedNodeByCrId(cdto.getCrId(), researchCR.getCreatedDate());
        }
      } else {
        crAffectedNodeRepository.deleteAffectedNodeByCrId(cdto.getCrId(), cdto.getCreatedDate());
      }

      String retNetNode = crAffectedNodeRepository
          .saveListDTONoIdSession(cdto.getLstNetworkNodeIdAffected(), null);
      return retNetNode;
    } else {
      return Constants.CR_RETURN_MESSAGE.SUCCESS;
    }
  }

  private Long ObjectToLong(String txt) {
    if (StringUtils.isStringNullOrEmpty(txt)) {
      return null;
    }
    try {
      return Long.parseLong(txt.trim());
    } catch (NumberFormatException ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

  @Override
  public CrInsiteDTO findCrById(Long id) {
    try {
      if (id != null && id > 0) {
        CrInsiteDTO crInsiteDTO = crRepository.findCrById(id, ticketProvider.getUserToken());
        CrAffectedServiceDetailsDTO crAffectedServiceDetailsDTO = new CrAffectedServiceDetailsDTO();
        crAffectedServiceDetailsDTO.setCrId(id.toString());
        List<CrAffectedServiceDetailsDTO> lstCrAffectedServiceDetailsDTOs = crAffectedServiceDetailsRepository
            .search(crAffectedServiceDetailsDTO, 0, 100, "", "");
        crInsiteDTO.setLstAffectedService(lstCrAffectedServiceDetailsDTOs);
        return crInsiteDTO;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public CrInsiteDTO getCrById(Long id, UserToken userToken) {
    try {
      if (id != null && id > 0) {
        CrInsiteDTO crInsiteDTO = crRepository.findCrById(id, userToken);
        return crInsiteDTO;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<ItemDataCRInside> getListScopeOfUserForAllRole(CrInsiteDTO crInsiteDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    crInsiteDTO.setUserLogin(String.valueOf(userToken.getUserID()));
    crInsiteDTO.setUserLoginUnit(String.valueOf(userToken.getDeptId()));
    return crGeneralRepository.getListScopeOfUserForAllRole(crInsiteDTO, I18n.getLocale());
  }

  @Override
  public Datatable getNetworkNodeFromQLTN(AppGroupInsite appGroupInsite) {
    Datatable datatable = new Datatable();
    List<InfraDeviceDTO> lst = crGeneralRepository
        .getNetworkNodeFromQLTN(appGroupInsite.getLstAppGroup());
    int page = appGroupInsite.getPage();
    int size = appGroupInsite.getPageSize();
    size = (size > 0) ? size : 5;
    if (lst != null && !lst.isEmpty() && size > 0) {
      int totalSize = lst.size();
      int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
      datatable.setPages(page);
      datatable.setPages(pageSize);
      datatable.setTotal(totalSize);
      List<InfraDeviceDTO> dataSuList = (List<InfraDeviceDTO>) DataUtil
          .subPageList(lst, page, size);
      if (dataSuList != null && dataSuList.size() > 0) {
        dataSuList.get(0).setPage(page);
        dataSuList.get(0).setPageSize(pageSize);
        dataSuList.get(0).setTotalRow(totalSize);
      }
      datatable.setData(dataSuList);
    }
    return datatable;
  }

  @Override
  public Datatable searchParentCr(String system, String code, int page, int size) {
    Datatable data = new Datatable();
    try {
      UserToken userToken = ticketProvider.getUserToken();
      switch (system) {
        case "7":
          data = showRR(userToken, code, page, size);
          break;
        case "6":
          data = showRDM(code, page, size);
          break;
        case "5":
          data = showSR(userToken, code, page, size);
          break;

        case "4":
          data = showWo(userToken, code, page, size);
          break;
        case "3":
          data = showTT(userToken, code, page, size);
          break;
        case "2":
          data = showPT(userToken, code, page, size);
          break;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return data;
  }

  @Override
  public ResultInSideDto actionApproveCRGeneral(CrInsiteDTO crDTO) {
    Long crState = null;
    crDTO = crProcessFromClient.getFormForProcess(crDTO);
    String msg = crProcessFromClient.validateActionClick(crDTO);
    if (msg.length() > 0) {
      return new ResultInSideDto(null, RESULT.ERROR, msg);
    }
    String result = actionApproveCR(crDTO, I18n.getLocale());
    try {
      List<CrApprovalDepartmentInsiteDTO> lstAp = crDTO.getLstAppDept();
      if (lstAp != null && !lstAp.isEmpty()) {
        if (lstAp.size() == 1) {
          crState = CrGeneralUtil
              .generateStateForApproveCr(crDTO, Long.parseLong(crDTO.getActionType()));
        } else {
          CrApprovalDepartmentInsiteDTO app0 = lstAp.get(0);
          CrApprovalDepartmentInsiteDTO app1 = lstAp.get(1);
          if ((app0.getStatus() != null && "1".equals(app0.getStatus()))
              || (app1.getStatus() != null && "1".equals(app1.getStatus()))) {
            crState = CrGeneralUtil
                .generateStateForApproveCr(crDTO, Long.parseLong(crDTO.getActionType()));
          }
        }
      }
      ResultInSideDto resultInSideDto = new ResultInSideDto(null, result, result);
      crProcessFromClient.actionApproveCrAfter(resultInSideDto, crDTO);
      crRepository.flushSession();
      CrInsiteDTO formRoot = crRepository
          .findCrById(Long.valueOf(crDTO.getCrId()), ticketProvider.getUserToken());
      crProcessFromClient.actionSaveEnd(crDTO, formRoot, result, crState);
      return resultInSideDto;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      if (e.getMessage().contains(
          I18n.getMessages("common.errCode011") + " " + I18n.getMessages("common.errDes011"))) {
        throw new RuntimeException(e.getMessage());
      }
    }
    return new ResultInSideDto(null, result, result);
  }

  @Override
  public String actionApproveCR(CrInsiteDTO crDTO, String locale) {
    StringUtils.printLogData("actionApproveCR", crDTO, CrInsiteDTO.class);
    return crRepository.actionApproveCR(crDTO, locale);
  }

  @Override
  public ResultInSideDto actionAppraisCRGeneral(CrInsiteDTO crDTO) {
    Long crState = null;
    crDTO = crProcessFromClient.getFormForProcess(crDTO);
    String msg = crProcessFromClient.validateActionClick(crDTO);
    if (msg.length() > 0) {
      return new ResultInSideDto(null, RESULT.ERROR, msg);
    }
    String result = actionAppraiseCr(crDTO, I18n.getLocale());
    try {
      crState = CrGeneralUtil
          .generateStateForAppraisalCR(crDTO, Long.parseLong(crDTO.getActionType()));
      ResultInSideDto ret = new ResultInSideDto(null, result, result);
      crProcessFromClient.actionAppraiseCrAfter(ret, crDTO);
      crRepository.flushSession();
      CrInsiteDTO formRoot = crRepository
          .findCrById(Long.valueOf(crDTO.getCrId()), ticketProvider.getUserToken());
      crProcessFromClient.actionSaveEnd(crDTO, formRoot, result, crState);
      return ret;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ResultInSideDto(null, result, result);
  }

  @Override
  public String actionAppraiseCr(CrInsiteDTO crDTO, String locale) {
    StringUtils.printLogData("actionAppraiseCr", crDTO, CrInsiteDTO.class);
    String retFail = Constants.CR_RETURN_MESSAGE.ERROR;
    try {
      Date crCreateDate = crDTO.getCreatedDate();
      String ret = crRepository.actionAppraiseCr(crDTO, locale);
      if (Constants.CR_RETURN_MESSAGE.SUCCESS.equals(ret)) {
        if (Constants.CR_ACTION_CODE.APPRAISE.toString().equals(crDTO.getActionType())) {
          String retNetNode = deleteAndUpdateNetworkNode(crDTO);
          if (!retNetNode.equalsIgnoreCase(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return retFail;
          }

          String retNetNodeAff = deleteAndUpdateNetworkNodeAffected(crDTO);
          if (!retNetNodeAff.equalsIgnoreCase(Constants.CR_RETURN_MESSAGE.SUCCESS)) {
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return retFail;
          }

          if (crDTO.getIsClickedToAlarmTag() != null && crDTO.getIsClickedToAlarmTag() == 1) {
            boolean saveAlarm = crAlarmRepository
                .saveOrUpdateList(crDTO.getLstAlarn(), ObjectToLong(crDTO.getCrId()), crCreateDate);
            if (!saveAlarm) {
              TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
              return Constants.CR_RETURN_MESSAGE.ERROR;
            }
          }

          if (crDTO.getIsClickedToVendorTag() != null && crDTO.getIsClickedToVendorTag() == 1) {
            boolean saveVendor = crAlarmRepository
                .saveOrUpdateVendorDetail(crDTO.getLstVendorDetail(), ObjectToLong(crDTO.getCrId()),
                    crCreateDate);
            if (!saveVendor) {
              TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
              return Constants.CR_RETURN_MESSAGE.ERROR;
            }
          }

          if (crDTO.getIsClickedToModuleTag() != null && crDTO.getIsClickedToModuleTag() == 1) {
            boolean saveModule = crAlarmRepository
                .saveOrUpdateModuleDetail(crDTO.getLstModuleDetail(), ObjectToLong(crDTO.getCrId()),
                    crCreateDate);
            if (!saveModule) {
              TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
              return Constants.CR_RETURN_MESSAGE.ERROR;
            }
          }

          if (crDTO.getIsClickedToCableTag() != null && crDTO.getIsClickedToCableTag() == 1) {

            boolean saveCable = crCableRepository
                .saveOrUpdateCableDetail(crDTO.getLstCable(), ObjectToLong(crDTO.getCrId()),
                    crCreateDate);
            if (!saveCable) {
              TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
              return Constants.CR_RETURN_MESSAGE.ERROR;
            }
          }

          return ret;
        } else {
          return ret;
        }
      }
      return retFail;
    } catch (Exception e) {
      TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
      log.error(e.getMessage(), e);
    }
    return retFail;

  }

  @Override
  public String actionAppraiseCrForServer(CrInsiteDTO crDTO, String locale) {
    StringUtils.printLogData("actionAppraiseCrForServer", crDTO, CrInsiteDTO.class);
    String msg = crProcessFromClient.validateForService(crDTO);
    if (msg.length() > 0) {
      return RESULT.ERROR;
    }
    return actionAppraiseCr(crDTO, I18n.getLocale());
  }

  @Override
  public ResultInSideDto actionVerifyCRGeneral(CrInsiteDTO crDTOSave) {
    String ret;
    Long crState = null;
    crDTOSave = crProcessFromClient.getFormForProcess(crDTOSave);
    String msg = crProcessFromClient.validateActionClick(crDTOSave);
    if (msg.length() > 0) {
      return new ResultInSideDto(null, RESULT.ERROR, msg);
    }
    if (Constants.CR_ACTION_CODE.ASSIGN_TO_CAB.toString().equals(crDTOSave.getActionType())) {
      ret = actionAssignCab(crDTOSave, I18n.getLocale());
      crState = CrGeneralUtil
          .generateStateForAssignCabCR(Long.parseLong(crDTOSave.getActionType()));
      try {
        if (Constants.CR_RETURN_MESSAGE.SUCCESS.equals(ret)
            && Constants.CR_TYPE.NORMAL.toString().equals(crDTOSave.getCrType()) //cr thuong
            && ("1".equals(crDTOSave.getRisk()) || "2".equals(crDTOSave.getRisk()) || "3"
            .equals(crDTOSave.getRisk()))) {
          List<String> addedWoIdList = null;
          if (StringUtils.isNotNullOrEmpty(crDTOSave.getListWoId())) {
            addedWoIdList = Arrays.asList(crDTOSave.getListWoId().split(","));
          }
          List<WoDTO> lstSubWo = crProcessFromClient
              .getListSubWo(crDTOSave.getCrId(), null, crDTOSave.getCreatedDate(), addedWoIdList);
          if (lstSubWo != null && !lstSubWo.isEmpty()) {
            crProcessFromClient.updateWoWhenChangeCr(lstSubWo, Constants.WO_STATUS_NEW.UNASSIGNED,
                I18n.getLanguage("woTest.woCrCab"));
            //cap nhat thoi gian cua Wo theo CR
            Map<String, Double> mapWoDuration = crProcessFromClient
                .getMapDurationByProcessId(crDTOSave.getProcessTypeId());
            crProcessFromClient.updateWoByCrTime(lstSubWo,
                DateTimeUtils.date2ddMMyyyyHHMMss(crDTOSave.getEarliestStartTime()),
                DateTimeUtils.date2ddMMyyyyHHMMss(crDTOSave.getLatestStartTime()), mapWoDuration,
                crDTOSave.getCrProcessId(), crDTOSave.getDutyType());
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        if (e.getMessage().contains(
            I18n.getMessages("common.errCode011") + " " + I18n.getMessages("common.errDes011"))) {
          throw new RuntimeException(e.getMessage());
        }
      }
    } else {
      ret = actionVerify(crDTOSave, I18n.getLocale());
      crState = CrGeneralUtil
          .generateStateForVerifyCR(crDTOSave, Long.parseLong(crDTOSave.getActionType()));
    }
    crRepository.flushSession();
    CrInsiteDTO formRoot = crRepository
        .findCrById(Long.valueOf(crDTOSave.getCrId()), ticketProvider.getUserToken());
    crProcessFromClient.actionSaveEnd(crDTOSave, formRoot, ret, crState);
    return new ResultInSideDto(null, RESULT.SUCCESS, ret);
  }

  @Override
  public String actionScheduleCrForServer(CrInsiteDTO crDTO, String locale) {
    StringUtils.printLogData("actionScheduleCrForServer", crDTO, CrInsiteDTO.class);
    String msg = crProcessFromClient.validateForService(crDTO);
    if (msg.length() > 0) {
      return RESULT.ERROR;
    }
    String ret = crProcessFromClient
        .checkCrAuto(crDTO, crDTO.getSubDTCode(),
            "1".equals(crDTO.getAutoExecute()) ? true : false);
    if (!StringUtils.isStringNullOrEmpty(ret)) {
      return RESULT.ERROR;
    }
    return actionScheduleCr(crDTO, I18n.getLocale());
  }

  @Override
  public String actionVerify(CrInsiteDTO crDTO, String locale) {
    StringUtils.printLogData("actionVerify", crDTO, CrInsiteDTO.class);
    return crRepository.actionVerify(crDTO, locale);
  }

  @Override
  public String actionVerifyForServer(CrInsiteDTO crDTO, String locale) {
    String ret = "";
    if (!"4".equals(crDTO.getRisk()) && !"121".equals(crDTO.getImpactSegment())) {
      if (crDTO.getActionType() != null && CR_ACTION_CODE.CHANGE_TO_SCHEDULE.toString()
          .equals(crDTO.getActionType())) {
        return RESULT.ERROR;
      }
    } else {
      if (crDTO.getActionType() != null && CR_ACTION_CODE.CHANGE_TO_CAB.toString()
          .equals(crDTO.getActionType())) {
        return RESULT.ERROR;
      }
    }
    if ("4".equals(crDTO.getRisk()) && Constants.CR_TYPE.NORMAL.toString()
        .equals(crDTO.getCrType())) {
      if (crDTO.getActionType() != null && CR_ACTION_CODE.ASSIGN_TO_CAB.toString()
          .equals(crDTO.getActionType())) {
        return RESULT.ERROR;
      }
    }
    String msg = crProcessFromClient.validateForService(crDTO);
    if (msg.length() > 0) {
      return RESULT.ERROR;
    }
    if (Constants.CR_ACTION_CODE.ASSIGN_TO_CAB.toString().equals(crDTO.getActionType())) {
      ret = actionAssignCab(crDTO, I18n.getLocale());
    } else {
      ret = actionVerify(crDTO, I18n.getLocale());
    }
    return ret;
  }

  @Override
  public ResultInSideDto actionScheduleCRGeneral(CrInsiteDTO crDTO) {
    crDTO = crProcessFromClient.getFormForProcess(crDTO);
    String msg = crProcessFromClient.validateActionClick(crDTO);
    if (msg.length() > 0) {
      return new ResultInSideDto(null, RESULT.ERROR, msg);
    }
    String ret = crProcessFromClient
        .checkCrAuto(crDTO, crDTO.getSubDTCode(),
            "1".equals(crDTO.getAutoExecute()) ? true : false);
    if (!StringUtils.isStringNullOrEmpty(ret)) {
      return new ResultInSideDto(null, RESULT.ERROR, ret);
    }
    ret = actionScheduleCr(crDTO, I18n.getLocale());
    Long crState = CrGeneralUtil
        .generateStateForScheduleClient(Long.parseLong(crDTO.getActionType()));
    if (RESULT.SUCCESS.equalsIgnoreCase(ret)) {
      msg = crProcessFromClient.callMrAfterScheduler(crDTO);
      if (!RESULT.ERROR.equalsIgnoreCase(msg)) {
        if ((Constants.CR_TYPE.NORMAL.toString().equals(crDTO.getCrType())
            || Constants.CR_TYPE.EMERGENCY.toString().equals(crDTO.getCrType()))
            && Constants.CR_ACTION_CODE.SCHEDULE.toString()
            .equals(crDTO.getActionType())) {//cr thuong hoac khan thi day wo khi sap lich
          //chuyen trang thai wo
          crProcessFromClient.actionScheduleCRAfter(crDTO);
        }
      }
    }
    crRepository.flushSession();
    CrInsiteDTO formRoot = crRepository
        .findCrById(Long.valueOf(crDTO.getCrId()), ticketProvider.getUserToken());
    crProcessFromClient.actionSaveEnd(crDTO, formRoot, ret, crState);
    return new ResultInSideDto(null, RESULT.SUCCESS, ret);
  }

  @Override
  public String actionScheduleCr(CrInsiteDTO crDTO, String locale) {
    try {
      StringUtils.printLogData("actionScheduleCr", crDTO, CrInsiteDTO.class);
      Date crCreateDate = crDTO.getCreatedDate();
      String status = crRepository.actionScheduleCr(crDTO, locale);
      if (Constants.CR_RETURN_MESSAGE.SUCCESS.equalsIgnoreCase(status)) {
        if ("1".equals(crDTO.getServiceAffecting())) {
          crAffectedServiceDetailsRepository.deleteAffSerByCrId(crDTO.getCrId());
          crAffectedServiceDetailsRepository.saveListDTONoIdSession(crDTO.getLstAffectedService());
        } else {
          crAffectedServiceDetailsRepository.deleteAffSerByCrId(crDTO.getCrId());
        }

        if (crDTO.getIsClickedToAlarmTag() != null && crDTO.getIsClickedToAlarmTag() == 1) {
          boolean saveAlarm = crAlarmRepository
              .saveOrUpdateList(crDTO.getLstAlarn(), ObjectToLong(crDTO.getCrId()), crCreateDate);
          if (!saveAlarm) {
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return Constants.CR_RETURN_MESSAGE.ERROR;
          }
        }

        if (crDTO.getIsClickedToVendorTag() != null && crDTO.getIsClickedToVendorTag() == 1) {
          boolean saveVendor = crAlarmRepository
              .saveOrUpdateVendorDetail(crDTO.getLstVendorDetail(), ObjectToLong(crDTO.getCrId()),
                  crCreateDate);
          if (!saveVendor) {
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return Constants.CR_RETURN_MESSAGE.ERROR;
          }
        }

        if (crDTO.getIsClickedToModuleTag() != null && crDTO.getIsClickedToModuleTag() == 1) {
          boolean saveModule = crAlarmRepository
              .saveOrUpdateModuleDetail(crDTO.getLstModuleDetail(), ObjectToLong(crDTO.getCrId()),
                  crCreateDate);
          if (!saveModule) {
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return Constants.CR_RETURN_MESSAGE.ERROR;
          }
        }

        if (crDTO.getIsClickedToCableTag() != null && crDTO.getIsClickedToCableTag() == 1) {
          boolean saveCable = crCableRepository
              .saveOrUpdateCableDetail(crDTO.getLstCable(), ObjectToLong(crDTO.getCrId()),
                  crCreateDate);
          if (!saveCable) {
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return Constants.CR_RETURN_MESSAGE.ERROR;
          }
        }

      } else {
        return RESULT.ERROR;
      }
    } catch (Exception e) {
      TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
      //e.printStackTrace();
      log.error(e.getMessage(), e);
      return Constants.CR_RETURN_MESSAGE.ERROR;
    }

    return Constants.CR_RETURN_MESSAGE.SUCCESS;
  }

  @Override
  public ResultInSideDto actionReceiveCRGeneral(CrInsiteDTO crDTO) {
    Long crState = null;
    crDTO = crProcessFromClient.getFormForProcess(crDTO);
    String msg = crProcessFromClient.validateActionClick(crDTO);
    if (msg.length() > 0) {
      return new ResultInSideDto(null, RESULT.ERROR, msg);
    }
    crProcessFromClient.actionReceiveCrThreadBefore(crDTO);//bao gom send trace
    //bo sung nghiep vu neu la cho tiep nhan -> tiep nhan thi chuyen sang goi tool giong cr thuong
    if (CR_ACTION_CODE.ACCEPT.toString().equalsIgnoreCase(crDTO.getActionType())) {
      String retMop = crProcessFromClient
          .checkCrAuto(crDTO, crDTO.getSubDTCode(),
              "1".equals(crDTO.getAutoExecute()) ? true : false);
      if (!StringUtils.isStringNullOrEmpty(retMop)) {
        return new ResultInSideDto(null, RESULT.ERROR, retMop);
      }
    }

    String ret = actionReceiveCr(crDTO, I18n.getLocale());
    crState = CrGeneralUtil.generateStateForReceiveClient(Long.parseLong(crDTO.getActionType()));
    try {

      if (Constants.CR_ACTION_CODE.RESOLVE_WITH_FAILT_STATUS_DUE_TO_WO.toString()
          .equals(crDTO.getActionType())) {
        String username = ticketProvider.getUserToken().getUserName();
        String reason = username + I18n.getLanguage("isFailWO") + ": ";
        try {
          reason = username + " " + I18n.getChangeManagement("fail.due.to.ft");
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        SendFailedWoThread failedWoThread
            = new SendFailedWoThread(username, reason, crDTO.getFailedWoList(), woServiceProxy);
        failedWoThread.start();
      }

      //Neu o buoc tiep nhan ma wo van du thao thi day lai wo
      if (Constants.CR_RETURN_MESSAGE.SUCCESS.equals(ret)) {
        List<String> addedWoIdList = new ArrayList<>();
        if (StringUtils.isNotNullOrEmpty(crDTO.getListWoId())) {
          String[] woIds = crDTO.getListWoId().trim().split(",");
          addedWoIdList.addAll(Arrays.asList(woIds));
        }
        List<WoDTO> lstSubWo = crProcessFromClient
            .getListSubWo(crDTO.getCrId(), null, crDTO.getCreatedDate(), addedWoIdList);
        if (lstSubWo != null && !lstSubWo.isEmpty()) {
          crProcessFromClient.updateWoWhenChangeCr(lstSubWo, Constants.WO_STATUS_NEW.UNASSIGNED,
              I18n.getLanguage("woTest.woCrSchedule"));
          if (lstSubWo != null && !lstSubWo.isEmpty()) {
            Map<String, Double> mapWoDuration = crProcessFromClient
                .getMapDurationByProcessId(crDTO.getProcessTypeId());
            crProcessFromClient.updateWoByCrTime(lstSubWo,
                DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getEarliestStartTime()),
                DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getLatestStartTime()), mapWoDuration,
                crDTO.getCrProcessId(), crDTO.getDutyType());
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      if (e.getMessage().contains(
          I18n.getMessages("common.errCode011") + " " + I18n.getMessages("common.errDes011"))) {
        throw new RuntimeException(e.getMessage());
      }
    }
    crRepository.flushSession();
    CrInsiteDTO formRoot = crRepository
        .findCrById(Long.valueOf(crDTO.getCrId()), ticketProvider.getUserToken());
    crProcessFromClient.actionSaveEnd(crDTO, formRoot, ret, crState);
    return new ResultInSideDto(null, RESULT.SUCCESS, ret);
  }

  @Override
  public String actionReceiveCr(CrInsiteDTO crDTO, String locale) {
    StringUtils.printLogData("actionReceiveCr", crDTO, CrInsiteDTO.class);
    return crRepository.actionReceiveCr(crDTO, locale);
  }

  @Override
  public ResultInSideDto actionResolveCRGeneral(CrInsiteDTO crDTO) {
    Long crState = null;
    crDTO = crProcessFromClient.getFormForProcess(crDTO);
    String msg = crProcessFromClient.validateActionClick(crDTO);
    if (msg.length() > 0) {
      return new ResultInSideDto(null, RESULT.ERROR, msg);
    }
    String ret = actionResolveCr(crDTO, I18n.getLocale());
    crState = CrGeneralUtil.generateStateForResolveClient(Long.parseLong(crDTO.getActionType()));
    try {
      if (ret != null && Constants.CR_RETURN_MESSAGE.SUCCESS
          .equalsIgnoreCase(ret)) {//then save ApprovalDepartment
        crProcessFromClient.actionResolveCrAfter(crDTO);
        actionResolveCrAfterToCloseCr(crDTO);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    crRepository.flushSession();
    CrInsiteDTO formRoot = crRepository
        .findCrById(Long.valueOf(crDTO.getCrId()), ticketProvider.getUserToken());
    crProcessFromClient.actionSaveEnd(crDTO, formRoot, ret, crState);
    if (RESULT.SUCCESS.equalsIgnoreCase(ret)) {
      return new ResultInSideDto(null, ret, ret);
    } else {
      if (Constants.CR_RETURN_MESSAGE.MUSTCLOSEALLWO.equalsIgnoreCase(ret)) {
        return new ResultInSideDto(null, RESULT.ERROR, I18n.getLanguage("cr.wo.must.close"));
      } else {
        return new ResultInSideDto(null, RESULT.ERROR, ret);
      }
    }
  }

  @Override
  public String actionResolveCr(CrInsiteDTO crDTO, String locale) {
    StringUtils.printLogData("actionResolveCr", crDTO, CrInsiteDTO.class);
    return crRepository.actionResolveCr(crDTO, locale);
  }

  @Override
  public String actionResolveCrForService(CrInsiteDTO crDTO, String locale) {
    StringUtils.printLogData("actionResolveCrForService", crDTO, CrInsiteDTO.class);
    String ret = actionResolveCr(crDTO, I18n.getLocale());
    try {
      if (ret != null && Constants.CR_RETURN_MESSAGE.SUCCESS
          .equalsIgnoreCase(ret)) {//then save ApprovalDepartment
        crProcessFromClient.actionResolveCrAfter(crDTO);
        actionResolveCrAfterToCloseCr(crDTO);
        return Constants.CR_RETURN_MESSAGE.SUCCESS;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return ret;
  }

  @Override
  public String actionCloseCr(CrInsiteDTO crDTO, String locale) {
    StringUtils.printLogData("actionCloseCr", crDTO, CrInsiteDTO.class);
    return crRepository.actionCloseCr(crDTO, locale);
  }

  @Override
  public ResultInSideDto actionCloseGeneralCr(CrInsiteDTO crDTO, String locale) {
    UserToken userToken = ticketProvider.getUserToken();
    crProcessFromClient.converTimeFromClientToServer(crDTO, userToken.getUserName());
    String message = actionCloseCr(crDTO, locale);
    String msg = "";
    if (RESULT.SUCCESS.equalsIgnoreCase(message)) {
      msg = crProcessFromClient.actionCloseCrAfter(crDTO);
    }
    if (msg.length() > 0) {
      return new ResultInSideDto(null, RESULT.ERROR, msg);
    } else {
      return new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    }
  }


  @Override
  public ResultInSideDto actionAssignCabCRGeneral(CrInsiteDTO crDTO) {
    String ret;
    Long crState;
    crDTO = crProcessFromClient.getFormForProcess(crDTO);
    String msg = crProcessFromClient.validateActionClick(crDTO);
    if (msg.length() > 0) {
      return new ResultInSideDto(null, RESULT.ERROR, msg);
    }
    if (Constants.CR_ACTION_CODE.CAB.toString().equals(crDTO.getActionType())) {
      ret = actionCab(crDTO, I18n.getLocale());
    } else {
      ret = actionAssignCab(crDTO, I18n.getLocale());
    }
    crState = CrGeneralUtil.generateStateForAssignCabCR(Long.parseLong(crDTO.getActionType()));
    if (Constants.CR_RETURN_MESSAGE.SUCCESS.equals(ret)
        && Constants.CR_TYPE.NORMAL.toString().equals(crDTO.getCrType()) //cr thuong
        && ("1".equals(crDTO.getRisk()) || "2".equals(crDTO.getRisk()) || "3"
        .equals(crDTO.getRisk()))) {
      //chuyen trang thai wo
      crProcessFromClient.actionAssignCabAfter(crDTO);
    }
    crRepository.flushSession();
    CrInsiteDTO formRoot = crRepository
        .findCrById(Long.valueOf(crDTO.getCrId()), ticketProvider.getUserToken());
    crProcessFromClient.actionSaveEnd(crDTO, formRoot, ret, crState);
    return new ResultInSideDto(null, ret, ret);
  }

  @Override
  public String actionAssignCab(CrInsiteDTO crDTO, String locale) {
    StringUtils.printLogData("actionAssignCab", crDTO, CrInsiteDTO.class);
    return crRepository.actionAssignCab(crDTO, locale);
  }

  @Override
  public ResultInSideDto actionCabCRGeneral(CrInsiteDTO crDTO) {
    Long crState = null;
    crDTO = crProcessFromClient.getFormForProcess(crDTO);
    String msg = crProcessFromClient.validateActionClick(crDTO);
    if (msg.length() > 0) {
      return new ResultInSideDto(null, RESULT.ERROR, msg);
    }
    String ret = actionCab(crDTO, I18n.getLocale());
    crState = CrGeneralUtil.generateStateForCabCR(Long.parseLong(crDTO.getActionType()));
    crRepository.flushSession();
    CrInsiteDTO formRoot = crRepository
        .findCrById(Long.valueOf(crDTO.getCrId()), ticketProvider.getUserToken());
    crProcessFromClient.actionSaveEnd(crDTO, formRoot, ret, crState);
    return new ResultInSideDto(null, ret, ret);
  }

  @Override
  public String actionCab(CrInsiteDTO crDTO, String locale) {
    StringUtils.printLogData("actionCab", crDTO, CrInsiteDTO.class);
    return crRepository.actionCab(crDTO, locale);
  }

  @Override
  public String actionCabForServer(CrInsiteDTO crDTO, String locale) {
    StringUtils.printLogData("actionCabForServer", crDTO, CrInsiteDTO.class);
    String msg = crProcessFromClient.validateForService(crDTO);
    if (msg.length() > 0) {
      return RESULT.ERROR;
    }
    return actionCab(crDTO, locale);
  }

  @Override
  public ResultInSideDto actionEditCRGeneral(CrInsiteDTO crDTO) {
    Long crState = null;
    crDTO = crProcessFromClient.getFormForProcess(crDTO);
    String msg = crProcessFromClient.validateActionClick(crDTO);
    if (msg.length() > 0) {
      return new ResultInSideDto(null, RESULT.ERROR, msg);
    }
    String ret = actionEditCr(crDTO, I18n.getLocale());
    crState = CrGeneralUtil.generateStateForResolveClient(Long.parseLong(crDTO.getActionType()));
    crProcessFromClient.actionEditCrAfter(crDTO);

    crRepository.flushSession();
    CrInsiteDTO formRoot = crRepository
        .findCrById(Long.valueOf(crDTO.getCrId()), ticketProvider.getUserToken());
    crProcessFromClient.actionSaveEnd(crDTO, formRoot, ret, crState);
    return new ResultInSideDto(null, ret, ret);
  }

  @Override
  public String actionEditCr(CrInsiteDTO crDTO, String locale) {
    StringUtils.printLogData("actionEditCr", crDTO, CrInsiteDTO.class);
    return crRepository.actionEditCr(crDTO, locale);
  }

  @Override
  public String actionCancelCr(CrInsiteDTO crDTO) {
    StringUtils.printLogData("actionCancelCr", crDTO, CrInsiteDTO.class);
    String ret = crRepository.actionCancelCR(crDTO);
    CrHisDTO crHis = new CrHisDTO();
    crHis.setCrId(crDTO.getCrId());
    crHis.setComments(crDTO.getNotes());
    crHis.setNotes("");
    crHis.setStatus(Constants.CR_STATE.CANCEL.toString());
    crHis.setActionCode(Constants.CR_ACTION_CODE.CANCELCR.toString());
    crHis.setUnitId(crDTO.getUserLoginUnit());
    crHis.setUserId(crDTO.getUserLogin());
    crHis.setEarliestStartTime(DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getEarliestStartTime()));
    crHis.setLatestStartTime(DateTimeUtils.date2ddMMyyyyHHMMss(crDTO.getLatestStartTime()));
    crHisRepository.insertOrUpdate(crHis);
    return ret;
  }

  public Datatable showRR(UserToken userTokenGNOC, String rrCode, int page, int size) {
    RiskDTOSearch dTOSearch = new RiskDTOSearch();
    dTOSearch.setRiskCode(rrCode);
    dTOSearch.setUserName(userTokenGNOC.getUserName());
    Date dateTime = new Date();
    dTOSearch.setCreateTimeFrom(DateUtil.dateToStringWithPattern(DateUtil.addDay(dateTime, -365),
        DateTimeUtils.patternDateTimeMs));
    dTOSearch.setCreateTimeTo(DateUtil.dateToStringWithPattern(DateUtil.addMinute(dateTime, 10),
        DateTimeUtils.patternDateTimeMs));
    dTOSearch.setProxyLocale(I18n.getLocale());
    List<RiskDTOSearch> lst = riskServiceProxy
        .getListDataSearchForOther(dTOSearch, 0, Integer.MAX_VALUE, "", "");
    Datatable datatable = new Datatable();
    List<CatItemDTO> result = new ArrayList<>();
    if (lst != null && !lst.isEmpty() && size > 0) {
      int totalSize = lst.size();
      int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
      List<RiskDTOSearch> crSubList = (List<RiskDTOSearch>) DataUtil.subPageList(lst, page, size);
      if (crSubList != null && crSubList.size() > 0) {
        crSubList.forEach(c -> {
          CatItemDTO catItemDTO = new CatItemDTO();
          catItemDTO.setPage(page);
          catItemDTO.setPageSize(pageSize);
          catItemDTO.setTotalRow(totalSize);
          catItemDTO.setItemValue(c.getRiskId());
          catItemDTO.setItemName(c.getRiskCode());
          result.add(catItemDTO);
        });
      }
      datatable.setData(result);
    }
    return datatable;
  }

  public Datatable showSR(UserToken userTokenGNOC, String srCode, int page, int size) {
    List<SRDTO> lst = srServiceProxy.getListSRForLinkCR(userTokenGNOC.getUserName(), srCode);
    Datatable datatable = new Datatable();
    List<CatItemDTO> result = new ArrayList<>();
    if (lst != null && !lst.isEmpty() && size > 0) {
      lst.removeIf(c -> (!SR_STATUS.ASSIGNED_PLANNING.equalsIgnoreCase(c.getStatus())));
      int totalSize = lst.size();
      int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
      List<SRDTO> crSubList = (List<SRDTO>) DataUtil.subPageList(lst, page, size);
      if (crSubList != null && crSubList.size() > 0) {
        crSubList.forEach(c -> {
          if (!"0".equals(c.getKey())) {
            CatItemDTO catItemDTO = new CatItemDTO();
            catItemDTO.setPage(page);
            catItemDTO.setPageSize(pageSize);
            catItemDTO.setTotalRow(totalSize);
            catItemDTO.setItemValue(c.getSrId());
            catItemDTO.setItemName(c.getSrCode());
            result.add(catItemDTO);
          }
        });
      }
      datatable.setData(result);
    }
    return datatable;
  }

  private Datatable showRDM(String rdmCode, int page, int size) {
    List<CatItemDTO> lst = new ArrayList<>();
    Datatable datatable = new Datatable();
    try {
      Gson gson = new Gson();
      Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();

      String urlRDM = mapConfigProperty.get("url_rdm") + "getListTaskWS?timeZoneOffset=0";
      String urlParameters = "{\n"
          + "	\"page\": 1,\n"
          + "	\"pageSize\": 20,\n"
          + "\"projectCode\": \"\",\n"
          + "\"taskCode\": \"" + rdmCode + "\",\n"
          + "\"taskName\": \"\",\n"
          + "\"assignee\": \"\"\n"
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
            CatItemDTO catItemDTO = new CatItemDTO();
            catItemDTO.setItemValue(temp.get("taskId"));
            catItemDTO.setItemName(temp.get("taskCode"));
            lst.add(catItemDTO);
          }

          int totalSize = lst.size();
          int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
          List<CatItemDTO> crSubList = (List<CatItemDTO>) DataUtil.subPageList(lst, page, size);
          if (crSubList != null && crSubList.size() > 0) {
            crSubList.get(0).setPage(page);
            crSubList.get(0).setPageSize(pageSize);
            crSubList.get(0).setTotalRow(totalSize);
          }
          datatable.setData(crSubList);
        }

      }
      conn.disconnect();
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return datatable;
  }

  public Datatable showWo(UserToken userTokenGNOC, String woCode, int page, int size) {
    Datatable datatable = new Datatable();
    WoDTOSearch woDTOSearchCondition = new WoDTOSearch();
    woDTOSearchCondition.setUserId(userTokenGNOC.getUserID().toString());
    woDTOSearchCondition.setIsFt(Boolean.TRUE);
    woDTOSearchCondition.setWoCode(woCode);
    woDTOSearchCondition.setStatus(Constants.WO_STATUS_NEW.DISPATCH + ","
        + Constants.WO_STATUS_NEW.ACCEPT + "," + Constants.WO_STATUS_NEW.INPROCESS);
    woDTOSearchCondition.setPage(1);
    woDTOSearchCondition.setPageSize(50);
    List<WoDTOSearch> lstWo = woServiceProxy.getListDataSearchProxy(woDTOSearchCondition);
    List<CatItemDTO> result = new ArrayList<>();
    if (lstWo != null && !lstWo.isEmpty()) {
      int totalSize = lstWo.size();
      int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
      List<WoDTOSearch> woSubList = (List<WoDTOSearch>) DataUtil.subPageList(lstWo, page, size);
      if (woSubList != null && woSubList.size() > 0) {
        woSubList.forEach(c -> {
          CatItemDTO catItemDTO = new CatItemDTO();
          catItemDTO.setPage(page);
          catItemDTO.setPageSize(pageSize);
          catItemDTO.setTotalRow(totalSize);
          catItemDTO.setItemValue(c.getWoId());
          catItemDTO.setItemName(c.getWoCode());
          result.add(catItemDTO);
        });
        datatable.setData(result);
      }
    }
    return datatable;
  }

  public Datatable showTT(UserToken userTokenGNOC, String troubleCode, int page, int size) {
    TroublesInSideDTO troublesInSideDTO = new TroublesInSideDTO();
    troublesInSideDTO.setPage(page);
    troublesInSideDTO.setPageSize(size);
    troublesInSideDTO.setReceiveUserId(userTokenGNOC.getUserID());
    troublesInSideDTO.setTroubleCode(troubleCode);
    return ttServiceProxy.searchParentTTForCR(troublesInSideDTO);
  }

  public Datatable showPT(UserToken userTokenGNOC, String problemCode, int page, int size) {
    ProblemsInsideDTO problemsInsideDTO = new ProblemsInsideDTO();
    problemsInsideDTO.setPage(page);
    problemsInsideDTO.setPageSize(size);
    problemsInsideDTO.setReceiveUserId(userTokenGNOC.getUserID());
    problemsInsideDTO.setProblemCode(problemCode);
    return ptServiceProxy.searchParentPTForCR(problemsInsideDTO);
  }

  @Override
  public ResultInSideDto checkDuplicateCr(CrInsiteDTO crInsiteDTO) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    List<CrInsiteDTO> lstCR = crProcessFromClient.validateCheckImpact(userToken, crInsiteDTO);
    if (lstCR == null || lstCR.isEmpty()) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(
          I18n.getLanguage("cr.impactDuplicate.crNotFound") + crInsiteDTO.getCrNumber());
      return resultInSideDto;
    }
    List<InfraDeviceDTO> lstExport = crProcessFromClient.getDataExport(lstCR);
    if (lstExport == null || lstExport.size() < 1) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getLanguage("cr.impactDuplicate.notDuplicate"));
      return resultInSideDto;
    }
    String[] header = new String[]{
        "description", "deviceCode", "ip", "deviceName"};
    resultInSideDto.setFile(crProcessFromClient
        .exportFileEx(lstExport, renderHeaderSheet(header), "", "NETWORK_NODE",
            "NETWORK_NODE_EXPORT", "language.networknodeImpact.export",
            "networknodeImpact.export.stt"));
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto importCheckCr(MultipartFile multipartFile) {
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
        File fileImp = new File(filePath);
        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 4,
            0, 1, 1000);

        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }

        List<Object[]> lstData = CommonImport.getDataFromExcelFile(fileImp, 0, 5,
            0, 1, 1000);
        if (lstData.size() > 1000) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(1000));
          return resultInSideDto;
        }
        if (!lstData.isEmpty()) {
          String stCrNumber = "";
          for (Object[] obj : lstData) {
            if (obj[0] != null) {
              stCrNumber += obj[0] + ",";
            }
          }
          if (stCrNumber.endsWith(",")) {
            stCrNumber = stCrNumber.substring(0, stCrNumber.length() - 1);
          }
          resultInSideDto.setObject(stCrNumber);
        }
      }
    } catch (Exception e) {
      resultInSideDto.setKey(RESULT.ERROR);
      log.error(e.getMessage(), e);
    }
    return resultInSideDto;
  }

  @Override
  public File exportSearchData(CrInsiteDTO crDTO) {
    try {
      String header[] = new String[]{
          "crNumber", "title", "state", "crType",
          "crProcessName", "earliestStartTime", "latestStartTime", "priority", "risk",
          "totalAffectedCustomers", "totalAffectedMinutes",
          "subcategory", "deviceType", "impactSegment",
          "updateTime", "changeOrginatorName", "changeOrginatorUnitName", "changeResponsibleName",
          "considerUnitName", "considerUserName", "userCab",
          "affectedServiceList", "circleAdditionalInfo", "changeResponsibleUnitName",
          "disturbanceStartTime", "disturbanceEndTime",
          "description", "commentCAB", "commentZ78", "commentQLTD",
          "commentCreator", "dutyType", "relateCr", "relatedTt", "sentDate", "onTimeAmount",
          "allComment",
          "isPrimaryCr", "relateToPrimaryCrNumber", "relateToPreApprovedCrNumber",
          "resolveTitle", "closeTitle", "autoExecute", "isRunType", "isConfirmAction"
      };
      UserToken userToken = ticketProvider.getUserToken();
      if (StringUtils.isStringNullOrEmpty(crDTO.getUserLogin()) || StringUtils
          .isStringNullOrEmpty(crDTO.getUserLoginUnit())) {
        crDTO.setUserLogin(String.valueOf(userToken.getUserID()));
        crDTO.setUserLoginUnit(String.valueOf(userToken.getDeptId()));
      }
      List<CrInsiteDTO> lstExport = crProcessFromClient.getListCrDTO(crDTO, true);
      lstExport = crProcessFromClient.convertKey2StringListCR(lstExport);
      return crProcessFromClient
          .exportFileEx(lstExport, renderHeaderSheet(header), I18n.getLanguage("cr.export.title"),
              "Export", "CR_EXPORT", "language.cr.list", "cr.stt");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public Datatable loadWorkOrder(WoSearchDTO woSearchDTO) {
    String sortType = woSearchDTO.getSortType();
    String sortName = woSearchDTO.getSortName();
    Datatable datatable = new Datatable();
    int page = woSearchDTO.getPage();
    int size = woSearchDTO.getPageSize();
    size = (size > 0) ? size : 5;
    woSearchDTO.setSortName(null);
    woSearchDTO.setSortType(null);
    woSearchDTO.setPage(1);
    woSearchDTO.setPageSize(Integer.MAX_VALUE);
    List<WoDTOSearch> lstWo = new ArrayList<>();
    List<WoDTOSearch> lstWoParse = new ArrayList<>();
    HashSet<String> woIdMap = new HashSet<>();
    try {

      Date crDate = DateUtil.string2DateTime(woSearchDTO.getCrCreateDate());
      if (crDate == null) {
        crDate = new Date();
      }
      String fromDate = DateUtil.date2ddMMyyyyHHMMss(DateUtil.addDay(crDate, -3));
      WoDTOSearch condition = new WoDTOSearch();
      condition.setWoSystem("CR");
      condition.setWoSystemId(woSearchDTO.getCrId());//Code da insert khi tao WO
//                condition.setWoSystemId("CR_"+crId+"_WO");//Code da insert khi tao WO
      UserToken userToken = ticketProvider.getUserToken();
      condition.setUserId(userToken.getUserID().toString());
      condition.setStartTimeFrom(fromDate);
      condition.setPage(1);
      condition.setPageSize(Integer.MAX_VALUE);
      condition.setSortType(sortType);
      condition.setSortName(sortName);
      lstWo = woServiceProxy.getListDataSearchProxy(condition);
      if (lstWo != null && !lstWo.isEmpty()) {
        for (WoDTOSearch eDto : lstWo) {
          eDto.setStatus(convertStatus2String(eDto.getStatus()));
          lstWoParse.add(eDto);
          woIdMap.add(eDto.getWoId());
        }
      }
      //longlt6 add 2017-08-27 -- bo xung cac WO bi tu choi
      condition.setStatus("2");
      if (lstWo != null && !lstWo.isEmpty()) {
        lstWo.clear();
      }
      condition.setPage(1);
      condition.setPageSize(Integer.MAX_VALUE);
      lstWo = woServiceProxy.getListDataSearchProxy(condition);
      if (lstWo != null && !lstWo.isEmpty()) {
        for (WoDTOSearch eDto : lstWo) {
          if (woIdMap.contains(eDto.getWoId())) {
            continue;
          }
          eDto.setStatus(convertStatus2String(eDto.getStatus()));
          lstWoParse.add(eDto);

        }
      }
      if (lstWoParse != null) {
        for (WoDTOSearch eWoDto : lstWoParse) {
          eWoDto.setCreateDate(DateUtil
              .date2ddMMyyyyHHMMss(DateUtil.stringYYYYmmDDhhMMssToDate(eWoDto.getCreateDate())));
          eWoDto.setStartTime(DateUtil
              .date2ddMMyyyyHHMMss(DateUtil.stringYYYYmmDDhhMMssToDate(eWoDto.getStartTime())));
          eWoDto.setEndTime(
              DateUtil
                  .date2ddMMyyyyHHMMss(DateUtil.stringYYYYmmDDhhMMssToDate(eWoDto.getEndTime())));
          if (!DataUtil.isNullOrEmpty(eWoDto.getLastUpdateTime())) {
            eWoDto.setLastUpdateTime(DateUtil.date2ddMMyyyyHHMMss(
                DateUtil.stringYYYYmmDDhhMMssToDate(eWoDto.getLastUpdateTime())));
          }
          if (!DataUtil.isNullOrEmpty(eWoDto.getFinishTime())) {
            eWoDto.setFinishTime(DateUtil
                .date2ddMMyyyyHHMMss(DateUtil.stringYYYYmmDDhhMMssToDate(eWoDto.getFinishTime())));
          }
        }
        int totalSize = lstWoParse.size();
        int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
        datatable.setTotal(totalSize);
        datatable.setPages(pageSize);
        List<WoSearchDTO> crSubList = (List<WoSearchDTO>) DataUtil
            .subPageList(lstWoParse, page, size);
        crSubList.get(0).setPage(page);
        crSubList.get(0).setPageSize(pageSize);
        List<WoSearchDTO> lstWoDto = new ArrayList<>();
        for (WoSearchDTO woDTOSearch : crSubList) {
          WoSearchDTO woSearch = new WoSearchDTO();
          woSearch.setPage(page);
          woSearch.setPageSize(pageSize);
          woSearch.setTotalRow(totalSize);
          woSearch.setWoContent(woDTOSearch.getWoContent());
          woSearch.setWoCode(woDTOSearch.getWoCode());
          woSearch.setStatus(woDTOSearch.getStatus());
          woSearch.setWoTypeName(woDTOSearch.getWoTypeName());
          woSearch.setCdName(woDTOSearch.getCdName());
          woSearch.setStartTime(woDTOSearch.getStartTime());
          woSearch.setEndTime(woDTOSearch.getEndTime());
          lstWoDto.add(woSearch);
        }
        datatable.setData(lstWo);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return datatable;
  }

  @Override
  public List<WoTypeInsideDTO> getListWoTypeDTO(WoTypeInsideDTO woTypeDTO) {
    WoTypeInsideDTO typeS = new WoTypeInsideDTO();
    typeS.setCreateFromOtherSys(1L);
    return woCategoryServiceProxy.getListWoTypeDTO(typeS);
  }

  @Override
  public Datatable getListCrForRelateOrPreApprove(CrInsiteDTO crDTO) {
    return crDBRepository.getListCrForRelateOrPreApprove(crDTO);
  }


  public static String convertStatus2String(String status) {
    if (StringUtils.isStringNullOrEmpty(status)) {
      return "";
    } else {
      String result = ("UNASSIGNED".equals(status) || "0".equals(status)) ? I18n
          .getString("message.wo.status.UNASSIGNED")//
          : ("ASSIGNED".equals(status) || "1".equals(status)) ? I18n
              .getString("message.wo.status.ASSIGNED")//
              : ("REJECT".equals(status) || "2".equals(status)) ? I18n
                  .getString("message.wo.status.REJECT")//
                  : ("DISPATCH".equals(status) || "3".equals(status)) ? I18n
                      .getString("message.wo.status.DISPATCH")//
                      : ("ACCEPT".equals(status) || "4".equals(status)) ? I18n
                          .getString("message.wo.status.ACCEPT")//
                          : ("INPROCESS".equals(status) || "5".equals(status)) ? I18n
                              .getString("message.wo.status.INPROCESS")//
                              : ("CLOSED_FT".equals(status) || "6".equals(status)) ? I18n
                                  .getString("message.wo.status.CLOSED_FT")//
                                  : ("DRAFT".equals(status) || "7".equals(status)) ? I18n
                                      .getString("message.wo.status.DRAFT")//
                                      : ("CLOSED_CD".equals(status) || "8".equals(status)) ? I18n
                                          .getString("message.wo.status.CLOSED_CD")//
                                          : ("PENDING".equals(status) || "9".equals(status)) ? I18n
                                              .getString("message.wo.status.PENDING")//
                                              : status;
      return result;
    }

  }

  public String getLocale(String locale) {
    if (StringUtils.isNotNullOrEmpty(locale)) {
      if (locale.toLowerCase().contains("vi") || locale.toLowerCase().contains("vn")) {
        return "vi_VN";
      }
    }
    return "en_US";
  }

  void getForm(CrInsiteDTO crDTO, UserToken userToken) {
    if (!(
        Constants.CR_ACTION_CODE.UPDATE_CR_WHEN_RECEIVE_STD.toString().equals(crDTO.getActionType())
            || Constants.CR_ACTION_CODE.UPDATE_CR_WHEN_APPROVE_STD.toString()
            .equals(crDTO.getActionType()))) {
      crDTO.setChangeOrginator(userToken.getUserID().toString());
      crDTO.setChangeOrginatorUnit(userToken.getDeptId().toString());
    }
  }

  private List<ConfigHeaderExport> renderHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 1) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!"CR Number".equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    return true;
  }


  @Override
  public List<UserGroupCategoryDTO> getListUserGroupBySystem(UserGroupCategoryDTO lstCondition,
      String step) {
    List<UserGroupCategoryDTO> lstUG = new ArrayList<>();
    try {
      List<String> lstStep = StringUtils.isStringNullOrEmpty(step) ? new ArrayList<>()
          : Arrays.asList(step.split(","));
      lstCondition.setProxyLocale(I18n.getLocale());
      lstUG = mrCategoryUtil.getListUserGroupBySystem(lstCondition);
      if (Constants.WORK_LOG_SYSTEM.CR.equalsIgnoreCase(lstCondition.getUgcySystem().toString())
          && lstStep != null
          && !lstStep
          .isEmpty()) {
        if (lstUG != null) {
          for (int i = lstUG.size() - 1; i >= 0; i--) {
            if (!lstStep.contains(lstUG.get(i).getUgcyCode())) {
              lstUG.remove(i);
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstUG;
  }

  @Override
  public List<WorkLogCategoryInsideDTO> getListWorkLogCategoryDTO(
      WorkLogCategoryInsideDTO workLogCategoryDTO) {
    List<WorkLogCategoryInsideDTO> lst = new ArrayList<>();
    try {
      workLogCategoryDTO.setPage(1);
      workLogCategoryDTO.setPageSize(200);
      workLogCategoryDTO.setProxyLocale(I18n.getLocale());
      if (workLogCategoryDTO != null) {
        lst = mrCategoryUtil.getListWorkLogCategoryDTO(workLogCategoryDTO);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public boolean validate(WorkLogInsiteDTO workLogDTO) {

    boolean result = true;
    long maxlength = 2000;
    if (workLogDTO.getWlgObjectType() != null && String.valueOf(workLogDTO.getWlgObjectType())
        .equals(Constants.WORK_LOG_SYSTEM.CR)) {
      if (StringUtils.isStringNullOrEmpty(workLogDTO.getWlgText())) {
        I18n.getChangeManagement("workLog.list.wlgText.notnull");
        return false;
      }
      if (workLogDTO.getWlgText().length() > maxlength) {
        I18n.getChangeManagement("worklog.length.msg");
        return false;
      }
    }
    return result;
  }

  @Override
  public ResultInSideDto insertWorkLog(WorkLogInsiteDTO workLogDTO) {
    ResultInSideDto result = new ResultInSideDto();
    boolean validate = validate(workLogDTO);
    workLogDTO.setWlgObjectState(0L);
    if (validate) {
      String changeResponsible = "";
      if (Constants.WORK_LOG_SYSTEM.CR.equals(String.valueOf(workLogDTO.getWlgObjectType()))) {
        CrInsiteDTO crTemp = findCrById(Long.valueOf(workLogDTO.getWlgObjectId()));
        if (crTemp != null && crTemp.getState() != null) {
          workLogDTO.setWlgObjectState(Long.valueOf(crTemp.getState()));
        }
      }
      try {

        UserToken userToken = ticketProvider.getUserToken();
        changeResponsible = userToken.getUserName();
//        String id = getSequenseCr("WORK_LOG_SEQ", 1).get(0);
//        workLogDTO.setWorkLogId(Long.valueOf(id));
        workLogDTO.setUserId(userToken.getUserID());

        workLogDTO
            .setCreatedDate(DateTimeUtils.convertStringToDateTime(DateTimeUtils.getSysDateTime()));
        workLogDTO.setWlgEffortHours(0L);
        workLogDTO.setWlgEffortMinutes(0L);
        workLogDTO.setWlgAccessType(0L);
        result = crRepository.insertWorkLog(workLogDTO.toEntity());
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      if (RESULT.SUCCESS.equalsIgnoreCase(result.getMessage())) {
        result.setKey(RESULT.SUCCESS);
        try {
          if (WORK_LOG_CAT.WLAY_ID_HTTD.equalsIgnoreCase(String.valueOf(workLogDTO.getWlayId()))
              && "2".equalsIgnoreCase(String.valueOf(workLogDTO.getWlgObjectType()))) {
//          workLogDTO.getWlgObjectType() // 1: MR , 2: CR
            sendSMSToLstUserConfig(String.valueOf(workLogDTO.getWlgObjectId()), "1");
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        String resolveSuccessUserGroupCategoryId = "11";
        String resolveSuccessWorklogCategoryId = "79";

        if (resolveSuccessUserGroupCategoryId
            .equalsIgnoreCase(String.valueOf(workLogDTO.getUserGroupAction()))
            && resolveSuccessWorklogCategoryId
            .equalsIgnoreCase(String.valueOf(workLogDTO.getWlayId()))) {
          //Goi sang NOCPRO để cập nhập trạng thái CR
          try {
            log.info("Call Nocpro WS step 0");
            log.info(
                "Param{ crId_changeResponsible}: " + String.valueOf(workLogDTO.getWlgObjectId())
                    + "_" + changeResponsible);
            ResponseBO responseBO = NocProWS.getNocProWS()
                .updateCrToFinishImpact(workLogDTO.getWlgObjectId().toString(), changeResponsible);
            if (responseBO != null) {
              log.info(
                  "Nocpro Response : " + responseBO.getKey() + " : " + responseBO.getDescription());
            } else {
              log.info("Can not get Nocpro Response");
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }

        }
      }
    }
    return result;
  }

  @Override
  public String sendSMSToLstUserConfig(String crId, String contentType) {
    return smsDBBussiness.sendSMSToLstUserConfig(crId, contentType);
  }


  @Override
  public Datatable getListWorklogSearch(WorkLogInsiteDTO dto) {
    return mrCategoryUtil.getListWorklogSearch(dto);
  }

  @Override
  public List<WoCdGroupInsideDTO> getListCdGroupByUser(WoCdGroupTypeUserDTO woCdGroupTypeUserDTO) {
    return woCategoryServiceProxy.getListCdGroupByUser(woCdGroupTypeUserDTO);
  }

  @Override
  public List<WoCdGroupTypeDTO> getListWoCdGroupTypeDTO(WoCdGroupTypeDTO woCdGroupTypeDTO) {
    return woCategoryServiceProxy
        .getListWoCdGroupTypeDTO(woCdGroupTypeDTO);
  }

  @Override
  public List<WoPriorityDTO> getListWoPriorityDTO(WoPriorityDTO woPriorityDTO) {
    return woCategoryServiceProxy.getListWoPriorityDTO(woPriorityDTO);
  }

  @Override
  public Datatable getListDataSearch(WoSearchDTO woSearchDTO) {
    String sortType = woSearchDTO.getSortType();
    String sortName = woSearchDTO.getSortName();
    Datatable datatable = new Datatable();
    int page = woSearchDTO.getPage();
    int size = woSearchDTO.getPageSize();
    size = (size > 0) ? size : 5;
    woSearchDTO.setSortName(null);
    woSearchDTO.setSortType(null);
    woSearchDTO.setPage(1);
    woSearchDTO.setPageSize(Integer.MAX_VALUE);
    WoDTOSearch searchDTO = woSearchDTO.getWoDTOSearch();
    searchDTO.setSortType(sortType);
    searchDTO.setSortName(sortName);
    searchDTO.setPage(1);
    searchDTO.setPageSize(Integer.MAX_VALUE);
    searchDTO.setUserId(ticketProvider.getUserToken().getUserID().toString());
    List<WoDTOSearch> lst = woServiceProxy.getListDataSearchProxy(searchDTO);
    List<WoDTOSearch> listDataSet2Table = Lists.newArrayList();
    if (lst != null && !lst.isEmpty()/*lst.size() > 0*/) {
      List<WoPriorityDTO> listPriorityDTO = getListWoPriorityDTO(new WoPriorityDTO());
      for (WoDTOSearch eWoDto : lst) {
        String status = convertStatus2String(eWoDto.getStatus());
        eWoDto.setStatus(status);
        Map<String, String> mapPriority = new HashMap<>();
        for (WoPriorityDTO woTypeDto : listPriorityDTO) {
          mapPriority.put(String.valueOf(woTypeDto.getPriorityId()), woTypeDto.getPriorityName());
        }
        if (eWoDto.getPriorityId() != null && !"".equals(eWoDto.getPriorityId())
            && mapPriority.get(eWoDto.getPriorityId()) != null) {
          eWoDto.setPriorityName(mapPriority.get(eWoDto.getPriorityId()));
        }
        if (woSearchDTO.getStatus() == null || "".equals(woSearchDTO.getStatus())) {
          if (!"CLOSED".equals(eWoDto.getStatus())
              && !Constants.WO_STATUS.CLOSED_CD.equals(eWoDto.getStatus())
              && !Constants.WO_STATUS_NAME.CLOSED_CD.equals(eWoDto.getStatus())) {
            listDataSet2Table.add(eWoDto);
          }
        } else {
          listDataSet2Table.add(eWoDto);
        }
      }
    }
    if (listDataSet2Table != null) {
      for (WoDTOSearch eWoDto : listDataSet2Table) {
        eWoDto.setCreateDate(DateUtil
            .date2ddMMyyyyHHMMss(DateUtil.stringYYYYmmDDhhMMssToDate(eWoDto.getCreateDate())));
        eWoDto.setStartTime(DateUtil
            .date2ddMMyyyyHHMMss(DateUtil.stringYYYYmmDDhhMMssToDate(eWoDto.getStartTime())));
        eWoDto.setEndTime(
            DateUtil
                .date2ddMMyyyyHHMMss(DateUtil.stringYYYYmmDDhhMMssToDate(eWoDto.getEndTime())));
        if (!DataUtil.isNullOrEmpty(eWoDto.getLastUpdateTime())) {
          eWoDto.setLastUpdateTime(DateUtil.date2ddMMyyyyHHMMss(
              DateUtil.stringYYYYmmDDhhMMssToDate(eWoDto.getLastUpdateTime())));
        }
        if (!DataUtil.isNullOrEmpty(eWoDto.getFinishTime())) {
          eWoDto.setFinishTime(DateUtil
              .date2ddMMyyyyHHMMss(DateUtil.stringYYYYmmDDhhMMssToDate(eWoDto.getFinishTime())));
        }
      }
      int totalSize = listDataSet2Table.size();
      int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
      datatable.setTotal(totalSize);
      datatable.setPages(pageSize);
      List<WoSearchDTO> crSubList = (List<WoSearchDTO>) DataUtil
          .subPageList(listDataSet2Table, page, size);
      List<WoSearchDTO> lstWo = new ArrayList<>();
      crSubList.get(0).setPage(page);
      crSubList.get(0).setPageSize(pageSize);
      for (WoSearchDTO woDTOSearch : crSubList) {
        WoSearchDTO woSearch = new WoSearchDTO();
        woSearch.setPage(page);
        woSearch.setPageSize(pageSize);
        woSearch.setTotalRow(totalSize);
        woSearch.setWoContent(woDTOSearch.getWoContent());
        woSearch.setWoCode(woDTOSearch.getWoCode());
        woSearch.setStatus(woDTOSearch.getStatus());
        woSearch.setWoTypeName(woDTOSearch.getWoTypeName());
        woSearch.setCdName(woDTOSearch.getCdName());
        woSearch.setStartTime(woDTOSearch.getStartTime());
        woSearch.setEndTime(woDTOSearch.getEndTime());
        lstWo.add(woSearch);
      }
      datatable.setData(lstWo);
    }
    return datatable;
  }

  @Override
  public Datatable loadCRRelated(CrInsiteDTO crInsiteDTO) {
    return crDBRepository.loadCRRelated(crInsiteDTO);
  }

  @Override
  public ResultInSideDto loadMop(CrInsiteDTO crInsiteDTO) throws Exception {
    ResultInSideDto resultInSideDto = null;
    CrInsiteDTO crLevel1 = findCrById(Long.valueOf(crInsiteDTO.getCrId()));
    if ("1".equals(crLevel1.getIsLoadMop())) {
      List<ConditionBean> lstCondition = new ArrayList<>();
      String newDate = DateTimeUtils
          .date2ddMMyyyyString(DateUtils.addDays(crLevel1.getCreatedDate(), -2));
      lstCondition
          .add(new ConditionBean("createdDate", newDate, Constants.NAME_GREATER_EQUAL, "DATE"));
      lstCondition.add(
          new ConditionBean("relateToPreApprovedCr", crInsiteDTO.getCrId(), Constants.NAME_EQUAL,
              Constants.NUMBER));
      lstCondition.add(new ConditionBean("state", Constants.CR_STATE.RESOLVE.toString(),
          Constants.NAME_NOT_EQUAL, Constants.NUMBER));
      lstCondition.add(new ConditionBean("state", Constants.CR_STATE.CANCEL.toString(),
          Constants.NAME_NOT_EQUAL, Constants.NUMBER));
      lstCondition.add(new ConditionBean("state", Constants.CR_STATE.CLOSE.toString(),
          Constants.NAME_NOT_EQUAL, Constants.NUMBER));
      lstCondition
          .add(new ConditionBean("isLoadMop", "2", Constants.NAME_EQUAL, Constants.NUMBER));
      ConditionBeanUtil.sysToOwnListCondition(lstCondition);
      List<CrInsiteDTO> lstCr = crRepository.getListCrByCondition(lstCondition, 0, 1000, "", "");
      if (lstCr != null && !lstCr.isEmpty()) {
        resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
            I18n.getChangeManagement("cr.mop.is.run"));
        return resultInSideDto;
      }
    }
    if (!"7".equals(crLevel1.getState()) && !"9".equals(crLevel1.getState())) {
      resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
          I18n.getChangeManagement("cr.level1.loadDt"));
      return resultInSideDto;
    }

    Map<String, CrFilesAttachInsiteDTO> map = new HashMap<>();
    List<ConditionBean> lstCondition = new ArrayList<>();
    lstCondition.add(
        new ConditionBean("crId", crInsiteDTO.getCrId(), Constants.NAME_EQUAL, Constants.NUMBER));
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    List<CrFilesAttachInsiteDTO> lstFile = crFilesAttachRepository
        .getListCrFilesAttachByCondition(lstCondition, 0, 1000, "", "");
    if (lstFile != null && !lstFile.isEmpty()) {
      for (CrFilesAttachInsiteDTO attachDTO : lstFile) {
        if (!StringUtils.isStringNullOrEmpty(attachDTO.getDtCode())
            && "VIPA_DD_INSERT".equals(attachDTO.getDtFileHistory())
            && map.get(attachDTO.getDtCode()) == null) {
          map.put(attachDTO.getDtCode(), attachDTO);
        }
      }
    } else {
      resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
          I18n.getChangeManagement("cr.not.exist.mop"));
      return resultInSideDto;
    }
    //check cac mop chua chay tren VMSA
    Collection<CrFilesAttachInsiteDTO> collectionValues = map.values();
    if (collectionValues == null || collectionValues.isEmpty()) {
      resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
          I18n.getChangeManagement("cr.not.exist.mop"));
      return resultInSideDto;
    }

    List<String> lstDtCode = new ArrayList<>();
    for (CrFilesAttachInsiteDTO attachDTO : collectionValues) {
      lstDtCode.add(attachDTO.getDtCode());
    }
    MopDetailOutputDTO outputDTO = wsVipaDdPort.getMopNotRunnedForCR(lstDtCode);
    List<MopDetailDTO> lstMop = outputDTO.getMopDetails();
    if (lstMop == null || lstMop.isEmpty()) {
      return new ResultInSideDto(null, RESULT.ERROR, I18n.getChangeManagement("mop.is.run"));
    } else { //them cho CR nay
      return crProcessFromClient
          .initSelectDT(map, crInsiteDTO.getRelateToPreApprovedCr(), lstMop, crLevel1,
//bo sung load MOP gan lai bang cr hien tai
              crInsiteDTO.getActionType());
    }

  }

  @Override
  public ResultInSideDto actionAssignCabMulti(List<CrInsiteDTO> crInsiteDTOS, String locale) {
    UserToken userToken = ticketProvider.getUserToken();
    String cr = "";
    for (CrInsiteDTO crInsiteDTO : crInsiteDTOS) {
      crProcessFromClient.converTimeFromClientToServer(crInsiteDTO, userToken.getUserName());
      if (crInsiteDTO.getUserCab() != null && !"".equals(crInsiteDTO.getUserCab())) {
        crInsiteDTO.setUserLogin(String.valueOf(userToken.getUserID()));
        crInsiteDTO.setUserLoginUnit(userToken.getDeptId().toString());
        crInsiteDTO.setActionType(Constants.CR_ACTION_CODE.ASSIGN_TO_CAB.toString());
        String re = actionAssignCab(crInsiteDTO, locale);
        if (Constants.CR_RETURN_MESSAGE.SUCCESS.equals(re)) {
          cr += crInsiteDTO.getCrNumber() + ";";
        }
      }
    }
    return new ResultInSideDto(null, RESULT.SUCCESS, cr);
  }

  @Override
  public ResultInSideDto insertWoFromWeb(List<MultipartFile> fileAttacks,
      List<MultipartFile> fileCfgAttacks, WoInsideDTO woInsideDTO) {
    return null;
  }

  @Override
  public List<CrInsiteDTO> actionGetListDuplicateCRImpactedNode(CrInsiteDTO crDTO) {
    try {
      return crDBRepository.actionGetListDuplicateCRImpactedNode(crDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }
//
//  @Override
//  public WorkLogCategoryDTO findWorkLogCategoryById(Long id) {
//    return mrCategoryProxy.findWorkLogCategoryById(id);
//  }

  @Override
  public Datatable getListCRFromOtherSystem(CrInsiteDTO crDTO) {
    log.debug("Request to getListCRFromOtherSystem: {}", crDTO);
    return crRepository.getListCRFromOtherSystem(crDTO);
  }

  @Override
  public List<AppGroup> getListAppGroup() {
    try {
      AppGroupResult appGroupResult = wstdttPort.getListAppGroup();
      if (appGroupResult != null) {
        List<AppGroup> appGroups = appGroupResult.getAppGroups();
        appGroups
            .sort((s1, s2) -> s1.getApplicationName().compareToIgnoreCase(s2.getApplicationName()));
        return appGroups;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public ResultInSideDto changeCheckboxAction(CrInsiteDTO crDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    crProcessFromClient.converTimeFromClientToServer(crDTO, userToken.getUserName());
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS,
        I18n.getChangeManagement("cr.msg.save.success"));
    if (crDTO.getIsCheckAction() != null && crDTO.getIsCheckAction().booleanValue()) {
      UserReceiveMsgDTO temp = new UserReceiveMsgDTO();
      temp.setUserId(userToken.getUserID());
      temp.setUserName(userToken.getUserName());
      temp.setCrId(
          StringUtils.isStringNullOrEmpty(crDTO.getCrId()) ? null : Long.valueOf(crDTO.getCrId()));
      temp.setCrNumber(crDTO.getCrNumber());
      temp.setCrCreateTime(crDTO.getCreatedDate());
      temp.setInsertTime(new Date());
      temp.setCrStartTime(crDTO.getEarliestStartTime());
      temp.setCrEndTime(crDTO.getLatestStartTime());
      resultInSideDto = userReceiveMsgRepository.insertOrUpdate(temp);
      if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        resultInSideDto.setMessage(I18n.getChangeManagement("cr.msg.save.fail"));
      } else {
        resultInSideDto.setMessage(I18n.getChangeManagement("cr.msg.save.success"));
      }
    } else {
      //get List UserMsg
      List<UserReceiveMsgDTO> lstCrMsgAll = new ArrayList<>();
      try {
        UserReceiveMsgDTO msgDTO = new UserReceiveMsgDTO();
        msgDTO.setUserId(userToken.getUserID());
        List<UserReceiveMsgDTO> lstUserReceive = userReceiveMsgRepository
            .getListUserReceiveMsgDTO(msgDTO, 0, Integer.MAX_VALUE, "", "");
        Date startFrom = crDTO.getEarliestStartTime();
        Date startTo = DateTimeUtils.convertStringToDate(crDTO.getEarliestStartTimeTo());
        if (lstUserReceive != null) {
          for (UserReceiveMsgDTO dto : lstUserReceive) {
            Date crStartTime = dto.getCrStartTime();
            if (crStartTime.getTime() >= startFrom.getTime() && crStartTime.getTime() <= startTo
                .getTime()) {
              lstCrMsgAll.add(dto);
            }
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      for (UserReceiveMsgDTO dto : lstCrMsgAll) {
        if (crDTO.getCrId().equals(String.valueOf(dto.getCrId()))) {
          resultInSideDto = userReceiveMsgRepository
              .deleteUserReceiveMsg(dto.getUserReceiveMsgId());
          if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            resultInSideDto.setMessage(I18n.getChangeManagement("cr.msg.save.fail"));
          } else {
            resultInSideDto.setMessage(I18n.getChangeManagement("cr.msg.save.success"));
          }
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto doAssignHandoverCa(CrInsiteDTO crInsiteDTO, List<MultipartFile> lstFile)
      throws IOException {
    UserToken userToken = ticketProvider.getUserToken();
    Long crId = Long.valueOf(crInsiteDTO.getCrId());
    String handoverCa = crInsiteDTO.getHandoverCa();
    String workLog = crInsiteDTO.getWorkLog();
    Long userAssignId = Long.valueOf(handoverCa);
    CrInsiteDTO crDTO = crRepository.findCrById(crId);//tiennv bo sung su dung ham findId Base
    crDTO.setHandoverCa(handoverCa);
    crDTO.setIsHandoverCa("1");
    crRepository.updateCr(crDTO);
    //insert File dinh kem
    if (lstFile != null && lstFile.size() > 0) {
      UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      for (MultipartFile multipartFile : lstFile) {
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                multipartFile.getBytes(), null);
        String fileName = multipartFile.getOriginalFilename();
        //Start save file old
        String fullPathOld = FileUtils
            .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                uploadFolder, null);
        CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = new CrFilesAttachInsiteDTO();
        crFilesAttachInsiteDTO.setFileName(FileUtils.getFileName(fullPathOld));
        crFilesAttachInsiteDTO.setCrId(crId);
        crFilesAttachInsiteDTO.setFileType(Constants.CR_FILE_TYPE.OTHER);
        crFilesAttachInsiteDTO.setTimeAttack(new Date());
        crFilesAttachInsiteDTO.setUserId(userAssignId);
        crFilesAttachInsiteDTO.setFilePath(fullPathOld);
        ResultInSideDto resultFileDataOld = crFilesAttachRepository.add(crFilesAttachInsiteDTO);
        //End save file old
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(fileName);
        gnocFileDto.setFileType(Constants.CR_FILE_TYPE.OTHER);
        gnocFileDto.setCreateUnitId(userToken.getDeptId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(userToken.getUserID());
        gnocFileDto.setCreateUserName(userToken.getUserName());
        gnocFileDto.setCreateTime(new Date());
        gnocFileDto.setMappingId(resultFileDataOld.getId());
        gnocFileDtos.add(gnocFileDto);
      }
      gnocFileRepository.saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.CR, crId, gnocFileDtos);
    }
    //cap nhat worklog
    WorkLogInsiteDTO logDTO = new WorkLogInsiteDTO();
    logDTO.setWlgObjectId(crId);
    logDTO.setWlgObjectType(2L);
    logDTO.setUserGroupAction(11L);
    logDTO.setUserId(userToken.getUserID());
    logDTO.setWlgText(workLog);
    logDTO.setWlgEffortHours(0L);
    logDTO.setWlgEffortMinutes(0L);
    logDTO.setWlgAccessType(0L);
    logDTO.setCreatedDate(new Date());
    logDTO.setWlayId(151L);
    logDTO.setWlgObjectState(6L);
    ResultInSideDto ret = crRepository.insertWorkLog(logDTO.toEntity());
    String content = I18n.getChangeManagement("sms.cr.assign")
        .replaceAll("#username#", userToken.getUserName())
        .replaceAll("#crNumber#", crDTO.getCrNumber());
    UsersEntity usersEntity = userRepository.getUserByUserId(userAssignId);
    messagesBusiness.insertMessageForUserCR(content, usersEntity);
    return new ResultInSideDto(null, RESULT.SUCCESS, ret.getMessage());
  }

  @Override
  public ResultInSideDto delete(Long id) {
    return crRepository.delete(id);
  }

  @Override
  public String actionUpdateNotify(CrInsiteDTO crDTO, Long actionCode) {
    return crRepository.actionUpdateNotify(crDTO, actionCode);
  }

  @Override
  public UsersDTO getUserInfo(String userName) {
    return crRepository.getUserInfo(userName);
  }

  @Override
  public UnitDTO getUnitInfo(String unitId) {
    return crRepository.getUnitInfo(unitId);
  }

  @Override
  public CrProcessWoDTO processWOTabAdd(CrInsiteDTO crInsiteDTO) {
    return crProcessFromClient.processWOTabAdd(crInsiteDTO);
  }

  @Override
  public int insertMopUpdateHis(CrUpdateMopStatusHisDTO hisDTO) {
    return crRepository.insertMopUpdateHis(hisDTO);
  }

  @Override
  public ObjResponse getListCRBySearchTypePagging(CrDTO crDTO, int start, int maxResult,
      String locale) {
    return crRepository.getListCRBySearchTypePagging(crDTO, start, maxResult, locale);
  }

  @Override
  public List<WorkLogInsiteDTO> getListWorkLogDTO(WorkLogInsiteDTO workLogDTO) {
    workLogDTO.setPage(0);
    workLogDTO.setPageSize(100);
    return mrCategoryUtil.getListWorkLogDTO(workLogDTO);
  }

  @Override
  public List<ResultDTO> actionGetProvinceMonitoringParamFix(String userId, String unitId,
      String searchChild, String startDate, String endDate) {
    return crRepository
        .actionGetProvinceMonitoringParamFix(userId, unitId, searchChild, startDate, endDate);
  }

  @Override
  public Datatable getDataTableSecondaryCr(CrInsiteDTO crInsiteDTO) {
    return crDBRepository.getDataTableSecondaryCr(crInsiteDTO);
  }

  @Override
  public Datatable getDataTablePreApprovedCr(CrInsiteDTO crInsiteDTO) {
    return crDBRepository.getDataTablePreApprovedCr(crInsiteDTO);
  }

  public String actionResolveCrAfterToCloseCr(CrInsiteDTO crDTO) {
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

        if (process != null && "1".equals(String.valueOf(process.getCloseCrWhenResolveSuccess()))) {
          crDTO.setActionType(String.valueOf(Constants.CR_ACTION_CODE.CLOSECR));
          String crReturnCodeId = crCloseReturnCodeId;
          String actionReturnCodeId = crCloseActionReturnCodeId;
          crDTO.setCrReturnCodeId(crReturnCodeId == null ? "27" : crReturnCodeId);
          crDTO.setActionReturnCodeId(actionReturnCodeId == null ? "42" : actionReturnCodeId);
          crDTO.setActionType(String.valueOf(Constants.CR_ACTION_CODE.CLOSECR));
          crDTO.setCloseCrAuto("1");
          String closeResult = actionCloseCr(crDTO, I18n.getLocale());
          if (!Constants.CR_RETURN_MESSAGE.SUCCESS.equals(closeResult)) {
            log.info("Resolve Cr Sucesssfully but can not close it ");
            return "Resolve Cr Sucesssfully but can not close it ";
          }

        }

      } catch (NumberFormatException e) {
        log.error(e.getMessage(), e);
        //
      }

      //Đóng WO nếu hoàn thành thành công
      try {

        log.info("step 0");
        if (crDTO.getCrId() != null) {

          Long crId = Long.valueOf(crDTO.getCrId());
          CrCreatedFromOtherSysDTO dto = crForOtherSystemBusiness.getCrCreatedFromOtherSysDTO(crId);

          if (dto != null && dto.getStatus() != null
              && "OK".equalsIgnoreCase(dto.getStatus())
              && "4".equalsIgnoreCase(dto.getSystemId())) { //check lai
            log.info("step 1");

            Long woId = crProcessFromClient.stringToLong(dto.getObjectId());
            WoDTO wo = woServiceProxy.findWoByIdWSProxy(woId);
            if (wo != null) {
              log.info("step 2");

              Long unitId = crProcessFromClient.stringToLong(crDTO.getChangeOrginatorUnit());
              Long woTypeId = crProcessFromClient.stringToLong(wo.getWoTypeId());
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
                    Long cId = crProcessFromClient.stringToLong(otherDTO.getCrId());
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
                      woCloseStatusCode = crProcessFromClient.stringToLong(woCloseStatusCodeConfig);
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
                    updateStatusForm.setFinishTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
                    updateStatusForm.setResultClose(2L);
                    ResultDTO resultDTO = woServiceProxy.changeStatusWoProxy(updateStatusForm);
                    if (!"SUCCESS".equalsIgnoreCase(resultDTO.getMessage())) {
                      log.error(resultDTO.getMessage());
                      return resultDTO.getMessage();
                    }
                    log.info("Message : " + resultDTO.getMessage());

                  }

                }
              }

            }

          }
        }

      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
    return "";
  }

  @Override
  public ResultInSideDto checkCreateWOWhenCloserCr(CrInsiteDTO crDTO) {
    try {
      String processType = crDTO.getProcessTypeLv3Id();
      if (StringUtils.isStringNullOrEmpty(processType)) {
        processType = crDTO.getProcessTypeId();
      }
      List<CrProcessWoDTO> lstCrWo = crProcessWoRepository.getLstWoFromProcessId(processType);
      int checkSendWOAfterClose = 0;
      if (lstCrWo != null && !lstCrWo.isEmpty()) {
        for (CrProcessWoDTO crWO : lstCrWo) {
          if ("1".equals(String.valueOf(crWO.getCreateWoWhenCloseCR()))) {
            checkSendWOAfterClose = 1;
            break;
          }
        }
      }
      return new ResultInSideDto(null, RESULT.SUCCESS, String.valueOf(checkSendWOAfterClose));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ResultInSideDto(null, RESULT.ERROR, RESULT.ERROR);
  }

  @Override
  public List<CrDTO> getListCrInfo(CrInsiteDTO crDTO) {
    List<CrDTO> list = new ArrayList<>();
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      Date startDate = null;
      Date endDate = null;
      String startCreatedDate = crDTO.getCreatedDateFrom();
      String endCreatedDate = crDTO.getCreatedDateTo();
      String crNumber = crDTO.getCrNumber();
      if (StringUtils.isStringNullOrEmpty(crNumber)) {
        return list;
      }
      if (StringUtils.isNotNullOrEmpty(startCreatedDate)) {
        startDate = dateFormat.parse(startCreatedDate);
      }

      if (StringUtils.isNotNullOrEmpty(endCreatedDate)) {
        endDate = dateFormat.parse(endCreatedDate);
      }

      if (startDate == null || endDate == null) {
        return list;
      }
      list = crRepository.getListCrInfo(crNumber, startDate, endDate);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }


  @Override
  public String actionApproveServiceCR(CrInsiteDTO crDTO, String locale) {
    StringUtils.printLogData("actionApproveCR", crDTO, CrInsiteDTO.class);
    if (!isCheckCrOverdue(crDTO)) {
      if (CR_ACTION_CODE.APPROVE.toString().equals(crDTO.getActionType())) {
        return RESULT.ERROR;
      }
    }
    return crRepository.actionApproveCR(crDTO, locale);
  }

  @Override
  public String actionReceiveServiceCR(CrInsiteDTO crDTO, String locale) {
    StringUtils.printLogData("actionReceiveCr", crDTO, CrInsiteDTO.class);
    if (!isCheckCrOverdue(crDTO)) {
      if (CR_ACTION_CODE.ACCEPT.toString().equals(crDTO.getActionType())) {
        return RESULT.ERROR;
      }
    }
    return crRepository.actionReceiveCr(crDTO, locale);
  }

  @Override
  public String actionEditServiceCR(CrInsiteDTO crDTO, String locale) {
    StringUtils.printLogData("actionEditCr", crDTO, CrInsiteDTO.class);
    if (!crProcessFromClient.validateTime(crDTO, false, locale)) {
      return RESULT.ERROR;
    }
    return crRepository.actionEditCr(crDTO, locale);
  }

  @Override
  public String actionAssignCabServiceCR(CrInsiteDTO crDTO, String locale) {
    StringUtils.printLogData("actionAssignCab", crDTO, CrInsiteDTO.class);
    String msg = crProcessFromClient.validateForService(crDTO);
    if (msg.length() > 0) {
      return RESULT.ERROR;
    }
    return crRepository.actionAssignCab(crDTO, locale);
  }

  @Override
  public String updateWorkOrder(String crNumber, Long typeOperation, MopGnoc mopGnoc)
      throws Exception {
    return wsVipaDdPort.updateWorkOrder(crNumber, typeOperation, mopGnoc);
  }

  @Override
  public ResultInSideDto deletWoMopTest(WoInsideDTO woInsideDTO) {
    ResultDTO result = woServiceProxy
        .deleteWOForRollbackProxy(woInsideDTO.getWoCode(), "rollback CR", "CR");
    try {
      if (result != null && RESULT.SUCCESS.equalsIgnoreCase(result.getKey())) {
        //goi sang VMSA

        boolean check = false;
        com.viettel.vmsa.MopGnoc mopGnoc = new com.viettel.vmsa.MopGnoc();
        if (StringUtils.isNotNullOrEmpty(woInsideDTO.getWoSystemOutId())) {
          String[] mop = woInsideDTO.getWoSystemOutId().split(",");
          for (String mopId : mop) {
            if (StringUtils.isNotNullOrEmpty(mopId)) {
              MopGnocDTO mopGnocDTO = new MopGnocDTO();
              mopGnocDTO.setMopId(mopId);
              mopGnocDTO.setWorkOrderCode(woInsideDTO.getWoCode());
              mopGnoc.getMopGnoc().add(mopGnocDTO);
              check = true;
            }
          }
        }

        if (check) {
          String temp = wsVipaDdPort.updateWorkOrder(woInsideDTO.getCrNumber(), 0L, mopGnoc);
          if (!RESULT.SUCCESS.equalsIgnoreCase(temp)) {
            return new ResultInSideDto(null, RESULT.ERROR, temp);
          }
        }
        return new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ResultInSideDto(null, RESULT.ERROR, RESULT.ERROR);
  }

  boolean isCheckCrOverdue(CrInsiteDTO crDTOMain) {
    Date earliesStartTime = crDTOMain.getLatestStartTime();
    return (earliesStartTime != null && earliesStartTime.compareTo(new Date()) > 0);
  }

  @Override
  public List<CrDTO> getListCRForExportServiceV2(CrInsiteDTO crDTO, String lstCrId,
      Date earliestCrCreatedTime, Date earliestCrStartTime, Date lastestCrStartTime,
      Date latestCrUpdateTime, int start, int end, String locale) {
    return crRepository
        .getListCRForExportServiceV2(crDTO, lstCrId, earliestCrCreatedTime, earliestCrStartTime,
            lastestCrStartTime, latestCrUpdateTime, start, end, locale);
  }

  @Override
  public List<CrDTO> getListSecondaryCr(CrDTO crDTO) {
    return crRepository.getListSecondaryCrOutSide(crDTO);
  }

  @Override
  public List<CrInsiteDTO> getListCrByCondition(List<ConditionBean> lstCondition, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return crRepository
        .getListCrByCondition(lstCondition, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public ResultInSideDto approveAssign(CrInsiteDTO crDTOMain) {
    ResultInSideDto resultInSideDto;
    UserToken userToken = ticketProvider.getUserToken();
    CrInsiteDTO crDTO = crRepository.findCrById(Long.valueOf(crDTOMain.getCrId()));
    if (StringUtils.isStringNullOrEmpty(crDTO.getHandoverCa())) {
      if ("2".equals(crDTO.getIsHandoverCa())) {
        return new ResultInSideDto(null, RESULT.ERROR,
            I18n.getLanguage("cr.btn.approveAssign.exist"));
      } else {
        return new ResultInSideDto(null, RESULT.ERROR,
            I18n.getLanguage("cr.btn.approveAssign.fail"));
      }
    }
    String userId = crDTO.getChangeResponsible();
    crDTO.setIsHandoverCa("2");
    crDTO.setChangeResponsible(crDTO.getHandoverCa());
    crDTO.setHandoverCa("");
    String ret = crRepository.updateCr(crDTO);
    if (!RESULT.SUCCESS.equalsIgnoreCase(ret)) {
      return new ResultInSideDto(null, RESULT.ERROR, I18n.getLanguage("common.update.fail"));
    }

    //cap nhat worklog
    String content = I18n.getChangeManagement("sms.cr.accept.assign")
        .replaceAll("#username#", userToken.getUserName())
        .replaceAll("#crNumber#", crDTOMain.getCrNumber());
    resultInSideDto = insertWorkLog(crDTOMain, userToken, content);
    if (resultInSideDto != null && !RESULT.SUCCESS.equalsIgnoreCase(resultInSideDto.getKey())) {
      resultInSideDto.setKey(RESULT.FAIL);
    }

    //cap nhat sang NOC
    WorkLogInsiteDTO logDTO = new WorkLogInsiteDTO();
    logDTO.setWlgObjectId(Long.valueOf(crDTOMain.getCrId()));
    logDTO.setWlgObjectType(2L);
    logDTO.setUserGroupAction(11L);
    logDTO.setWlayId(79L);
    logDTO.setPage(0);
    logDTO.setPageSize(1);
    try {
      List<WorkLogInsiteDTO> lstWorks = mrCategoryUtil.getListWorkLogDTO(logDTO);
      List<CrAlarmInsiteDTO> lstAlarm = crAlarmRepository.getListAlarmByCr(crDTO);
      if (lstAlarm != null && !lstAlarm.isEmpty() && (lstWorks == null || lstWorks.isEmpty())) {
        String nationCode = "NOC";
        if ("1500289728".equals(crDTO.getCountry())) {//VTP
          nationCode = "NOC_VTP";
        } else if ("2000289729".equals(crDTO.getCountry())) {//haiti
          nationCode = "NOC_NAT";
        } else if ("3000289724".equals(crDTO.getCountry())) {//mozam
          nationCode = "NOC_MVT";
        } else if ("6000289723".equals(crDTO.getCountry())) {//timor
          nationCode = "NOC_VTL";
        } else if ("4500000001".equals(crDTO.getCountry())) {//myanmar
          nationCode = "NOC_MYT";
        } else if ("4001000000".equals(crDTO.getCountry())) {//tanz
          nationCode = "NOC_TZN";
        } else if ("5000289722".equals(crDTO.getCountry())) {//lao
          nationCode = "NOC_STL";
        } else if ("1000014581".equals(crDTO.getCountry())) {//cam
          nationCode = "NOC_VTC";
        } else if ("3500289726".equals(crDTO.getCountry())) {//burundi
          nationCode = "NOC_VTB";
        }

        String res = wsNocprov4Port
            .onExecuteMapQuery(crDTOMain.getCrNumber(), userToken, nationCode);
        if (!"SUCCESS".equals(res)) {
          resultInSideDto = new ResultInSideDto(null, RESULT.FAIL, I18n.getLanguage("cr.btn.update")
              + " NOC " + I18n.getLanguage("common.fail") + " : " + res);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto = new ResultInSideDto(null, RESULT.FAIL, I18n.getLanguage("cr.btn.update")
          + " NOC " + I18n.getLanguage("common.fail") + " #EXCEPTION");
    }

    //insert SMS
    try {
      sendSMS(content, userId);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto = new ResultInSideDto(null, RESULT.FAIL, I18n.getLanguage("cr.btn.update")
          + " SMS " + I18n.getLanguage("common.fail") + " #EXCEPTION");
    }
    if (resultInSideDto != null && RESULT.SUCCESS.equalsIgnoreCase(resultInSideDto.getKey())) {
      return new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto rejectAssign(CrInsiteDTO crDTOMain) {
    ResultInSideDto resultInSideDto;
    UserToken userToken = ticketProvider.getUserToken();
    CrInsiteDTO crDTO = crRepository.findCrById(Long.valueOf(crDTOMain.getCrId()));
    String userId = crDTO.getChangeResponsible();
    crDTO.setIsHandoverCa("-1");
    crDTO.setHandoverCa("");
    String ret = crRepository.updateCr(crDTO);
    if (!RESULT.SUCCESS.equalsIgnoreCase(ret)) {
      return new ResultInSideDto(null, RESULT.ERROR, I18n.getLanguage("common.update.fail"));
    }

    //cap nhat worklog
    String content = I18n.getChangeManagement("sms.cr.reject.assign")
        .replaceAll("#username#", userToken.getUserName())
        .replaceAll("#crNumber#", crDTOMain.getCrNumber());
    resultInSideDto = insertWorkLog(crDTOMain, userToken, content);
    if (!RESULT.SUCCESS.equalsIgnoreCase(resultInSideDto.getKey())) {
      return resultInSideDto;
    }

    //insert SMS
    try {
      sendSMS(content, userId);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultInSideDto(null, RESULT.ERROR, I18n.getLanguage("cr.btn.update")
          + " SMS " + I18n.getLanguage("common.fail") + " #EXCEPTION");
    }
    return new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);

  }

  private ResultInSideDto insertWorkLog(CrInsiteDTO crDTOMain, UserToken userToken,
      String content) {
    try {
      WorkLogInsiteDTO logDTO = new WorkLogInsiteDTO();
      logDTO.setWlgObjectId(Long.valueOf(crDTOMain.getCrId()));
      logDTO.setWlgObjectType(2L);
      logDTO.setUserGroupAction(11L);
      logDTO.setUserId(userToken.getUserID());
      logDTO.setWlgText(content);
      logDTO.setWlgEffortHours(0L);
      logDTO.setWlgEffortMinutes(0L);
      logDTO.setWlgAccessType(null);
      logDTO.setCreatedDate(new Date());
      logDTO.setWlayId(151L);
      logDTO.setWlgObjectState(6L);
      ResultInSideDto resultInSideDto = crRepository.insertWorkLog(logDTO.toEntity());
      if (!RESULT.SUCCESS.equalsIgnoreCase(resultInSideDto.getKey())) {
        return new ResultInSideDto(null, RESULT.ERROR, I18n.getLanguage("cr.actioncode.add")
            + " " + I18n.getLanguage("mrMngt.worklog") + " " + I18n.getLanguage("common.fail"));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultInSideDto(null, RESULT.ERROR, I18n.getLanguage("cr.actioncode.add")
          + " " + I18n.getLanguage("mrMngt.worklog") + " " + I18n.getLanguage("common.fail"));
    }
    return new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
  }

  private void sendSMS(String content, String userId) {
    UsersEntity usersEntity = userRepository.getUserByUserId(Long.valueOf(userId));
    messagesBusiness.insertMessageForUserCR(content, usersEntity);
  }

  public void processDayOff(CrInsiteDTO crDTO, String actionRight) {
    crRepository.processDayOff(crDTO, actionRight);
  }

  @Override
  public String actionCancelCrGeneral(CrInsiteDTO crDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    crProcessFromClient.converTimeFromClientToServer(crDTO, userToken.getUserName());
    return actionCancelCr(crDTO);
  }

  //TrungDuong them getList CR voi CR co IP tac đong = IP vua click tuong ung
  @Override
  public Datatable getListCrByIp(CrInsiteDTO crInsiteDTO) {
    return crRepository.getListCrByIp(crInsiteDTO);
  }

  @Override
  public ResultInSideDto updateCrTimeOverdue(CrInsiteDTO crDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    crDTO = crProcessFromClient.getFormForProcess(crDTO);
    if (StringUtils.isNotNullOrEmpty(crDTO.getCrId())) {
      CrInsiteDTO dtoToRollBack = crRepository.findCrById(Long.valueOf(crDTO.getCrId()));
      crDBRepository.updateCrTimeInCaseResolve(crDTO, false);
      resultInSideDto.setObject(dtoToRollBack);
    } else {
      resultInSideDto.setKey(RESULT.ERROR);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateCrTimeOverdueToMop(CrInsiteDTO crDTO, CrInsiteDTO crRollBack) {
    String resultMessage = RESULT.ERROR;
    try {
      resultMessage = crProcessFromClient.updateCrToMopInsite(crDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (!RESULT.SUCCESS.equals(resultMessage)) {
      crDBRepository.updateCrTimeInCaseResolve(crRollBack, true);
      if (RESULT.ERROR.equals(resultMessage) || StringUtils.isStringNullOrEmpty(resultMessage)) {
        return new ResultInSideDto(null, RESULT.ERROR, "Have some error when call Tools");
      } else {
        return new ResultInSideDto(null, RESULT.ERROR, resultMessage);
      }
    }
    return new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
  }

  @Override
  public ResultInSideDto insertWorkLogProxy(WorkLogInsiteDTO workLogInsiteDTO) {
    return crRepository.insertWorkLog(workLogInsiteDTO.toEntity());
  }
}
