/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.model.GnocFileEntity;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrVmsaUpdateMopHisDTO;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.MessageForm;
import com.viettel.gnoc.cr.dto.VMSAMopDetailDTO;
import com.viettel.gnoc.cr.model.CrFilesAttachEntity;
import com.viettel.security.PassTranformer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.xml.security.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * @author thanhlong
 */
@Repository
@Slf4j
public class CrDtRepositoryImpl extends BaseRepository implements CrDtRepository {

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

  @Value("${application.cmc_config.user_service:null}")
  private String userService;

  @Value("${application.cmc_config.pass_service:null}")
  private String passService;

  @Value("${application.cmc_config.salt_service:null}")
  private String saltService;

  @Value("${application.cmc_config.cr_close_CrReturnCodeId:null}")
  private String crReturnCodeId;

  @Value("${application.cmc_config.cr_close_ActionReturnCodeId:null}")
  private String crActionReturnCodeId;

  @Value("${application.cmc_config.system_user_id:null}")
  private String systemUserId;

  @Value("${application.cmc_config.system_unit_id:null}")
  private String systemUnitId;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Autowired
  CrDBRepository crDBRepository;

  @Autowired
  SmsDBRepository smsDBRepository;

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  CrFilesAttachRepository crFilesAttachRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UnitRepository unitRepository;

  SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

  public final Integer VMSA_SUCCESS_KEY = 0;
  public final Integer VMSA_FAIL_KEY = 1;

  private Long objectToLong(Object txt) {
    if (txt == null || txt.toString().trim().isEmpty()) {
      return null;
    }
    try {
      return Long.parseLong(txt.toString());
    } catch (NumberFormatException ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

  public ResultDTO updateWaitingStatusForCR(Long crId, Long waitingMopStatus) {
    ResultDTO resultDTO = new ResultDTO();
    resultDTO.setKey(RESULT.SUCCESS);
    try {
      String sql = "update open_pm.cr set WAITING_MOP_STATUS = :waitingMopStatus where cr_id = :crId ";
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("crId", crId);
      parameters.put("waitingMopStatus", waitingMopStatus);
      getNamedParameterJdbcTemplate().update(sql, parameters);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(e.getMessage());
    }
    return resultDTO;
  }

  @Override
  public ResultDTO insertVMSADT(Long crId, Long validateKey, String systemCode,
      int createMopSuccess, String createMopDetail,
      List<VMSAMopDetailDTO> mopDTOList, String nationCode, String locale) {
    Locale locale1 = new Locale(locale);
    if (locale != null && "en".equalsIgnoreCase(locale) || "en_us"
        .equalsIgnoreCase(locale)) {
      locale1 = new Locale("en", "US");
    } else if (locale != null && "vi".equalsIgnoreCase(locale) || "vi_vn"
        .equalsIgnoreCase(locale)) {
      locale1 = new Locale("vi", "VN");
    } else if (locale != null && "lo".equalsIgnoreCase(locale) || "lo_la"
        .equalsIgnoreCase(locale)) {
      locale1 = new Locale("lo", "LA");
    }
    log.info("crId : " + crId);
    log.info("validateKey : " + validateKey);
    System.out.println("systemCode : " + systemCode);
    System.out.println("createMopSuccess : " + createMopSuccess);
    System.out.println("createMopDetail : " + createMopDetail);
    System.out.println("nationCode : " + nationCode);
    ResultDTO resultDTO = new ResultDTO();
    resultDTO.setKey(RESULT.SUCCESS);
    HashMap<String, String> impactIp = new HashMap<>();
    List<CrImpactedNodesDTO> lstNodeImpact = new ArrayList<>();
    List<CrFilesAttachInsiteDTO> lstCrFileAttactDTO = new ArrayList<>();
    List<String> searchIpList = new ArrayList<>();
    List<Long> listUserId = new ArrayList<>();
    try {
      if (crId == null) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("CrId is not vaild!");
        return resultDTO;
      }
      if (validateKey == null) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("Validate Key Not vaild");
        return resultDTO;
      }
      CrInsiteDTO crDTO = getCrById(crId);
      if (crDTO == null) {
        // nếu chưa
        insertVMsaMopHis(crId, validateKey, systemCode, VMSA_FAIL_KEY);
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("Cr Not found!");
        return resultDTO;
      }
      if (!String.valueOf(validateKey).equals(crDTO.getVMSAValidateKey())) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("Validate Key is Not exactly");
        return resultDTO;
      }
      String crState = crDTO.getState();
      if ((!Constants.CR_STATE.DRAFT.toString().equals(crState)
          && !Constants.CR_STATE.OPEN.toString().equals(crState)
          && !Constants.CR_STATE.QUEUE.toString().equals(crState))
          && !Constants.CR_STATE.APPROVE.toString().equals(crState)) {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage(I18n.getLanguageByLocale(locale1, "qltn.crStateInvalid"));
        return resultDTO;
      }
      List<CrVmsaUpdateMopHisDTO> hisList = findHistory(crId, validateKey);
      if (hisList != null && !hisList.isEmpty()) {
        for (CrVmsaUpdateMopHisDTO o : hisList) {
          if (VMSA_SUCCESS_KEY.toString().equals(String.valueOf(o.getResultCode()))
              && o.getSystemCode() != null && o.getSystemCode().toUpperCase().contains("VMSA")) {
            // nếu VMSA đđã update trước đấy thành công thi thoat ra
            resultDTO.setKey(RESULT.SUCCESS);
            resultDTO.setMessage("Mop is updated susscessfully before");
            return resultDTO;
          }
        }
      }
      boolean updateHis = insertVMsaMopHis(crId, validateKey, systemCode, createMopSuccess);
      if (!updateHis) {
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("Update his fail");
        return resultDTO;
      }
      if (createMopSuccess != VMSA_SUCCESS_KEY) {
        List<CrVmsaUpdateMopHisDTO> newHisList = findHistory(crId, validateKey);
        if (newHisList.size() >= 2) {
          boolean gnocUploadFail = false;
          boolean vmsaUploadFail = false;
          for (CrVmsaUpdateMopHisDTO newHis : newHisList) {
            if (!VMSA_SUCCESS_KEY.toString().equals(String.valueOf(newHis.getResultCode()))) {
              if (newHis.getSystemCode() != null && newHis.getSystemCode().toUpperCase()
                  .contains("VMSA")) {
                gnocUploadFail = true;
              } else if (newHis.getSystemCode() != null && newHis.getSystemCode().toUpperCase()
                  .contains("GNOC")) {
                vmsaUploadFail = true;
              }
            }
          }
          if (gnocUploadFail && vmsaUploadFail) {
            // nếu như đã có ghi nhận update thất bại trước đấy  và lần này cũng thấy bại nốt ==> thực hiện đóng CR tự động vì lý do ko tạo đc mop
            crDTO.setActionType(String.valueOf(Constants.CR_ACTION_CODE.CLOSECR));
            String returnCodeId = crReturnCodeId;
            String actionReturnCodeId = crActionReturnCodeId;
            String userLogin = systemUserId;
            String unitLogin = systemUnitId;
            crDTO.setCrReturnCodeId(returnCodeId == null ? "27" : returnCodeId);
            crDTO.setActionReturnCodeId(actionReturnCodeId == null ? "42" : actionReturnCodeId);
            crDTO.setActionType(String.valueOf(Constants.CR_ACTION_CODE.CLOSECR));
            crDTO.setCloseCrAuto("1");
            crDTO.setUserLogin(userLogin);
            crDTO.setUserLoginUnit(unitLogin);
            crDTO.setCloseCrAuto("1");
            crDBRepository.updateCrStatusInCaseOfCloseCR(Constants.CR_ACTION_CODE.CLOSECR, Long
                .valueOf(crDTO.getCrId()), crDTO, locale);
            // Gửi tin nhắn đén người tạo
            Long changeOrginator = objectToLong(crDTO.getChangeOrginator());
            listUserId.add(changeOrginator);
            List<MessageForm> lst = smsDBRepository.getListUsersByUserId(listUserId, false);
            for (MessageForm msg : lst) {
              msg.setMessageContent(createMopDetail);
              smsDBRepository.actionInsertIntoMess(msg);
            }
          }
        }
        resultDTO.setKey(RESULT.SUCCESS);
        resultDTO.setMessage("Mop is cancel Successfully");
        return resultDTO;
      }
      if (mopDTOList == null || mopDTOList.isEmpty()) {
        resultDTO.setKey(RESULT.SUCCESS);
        resultDTO.setMessage("Mop is update Successfully");
        return resultDTO;
      }
      int count = 0;
      Date crCreateDate = null;
      try {
        crCreateDate = crDTO.getCreatedDate();
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      for (VMSAMopDetailDTO mopDTO : mopDTOList) {
        count++;
        boolean deleteFile = deleteDtFiles(crId, mopDTO.getMopId(), null, null);
        if (!deleteFile) {
          TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage("Can not delete Older Mop File!");
          return resultDTO;
        }
        boolean deleteImpactedNodes = deleteImpactedNodes(crId, mopDTO.getMopId(), crCreateDate);
        if (!deleteImpactedNodes) {
          TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage("Can not delete Older impacted Nodes!");
          return resultDTO;
        }
        if (mopDTO.getIpList() != null && !mopDTO.getIpList().isEmpty()) {
          for (String ip : mopDTO.getIpList()) {
            impactIp.put(ip.trim(), mopDTO.getMopId());
          }
        }
        String mopfileName = mopDTO.getMopFileName();
        String fileContent = mopDTO.getMopFileContent();
        byte[] content = Base64.decode(fileContent);

        String fileName = String.valueOf(count).concat("_").concat("DT_EXECUTE").concat("_")
            .concat(mopfileName);
        String fullPath = "";
        try {
          fullPath = FileUtils.saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), ftpFolder, fileName, content, null);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        if (StringUtils.isStringNullOrEmpty(fullPath)) {
          TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage("Can not save file : " + fileName);
          return resultDTO;
        }
        CrFilesAttachInsiteDTO dtexecuteDTO = new CrFilesAttachInsiteDTO();
        dtexecuteDTO.setFileName(fileName);
        dtexecuteDTO.setCrId(Long.valueOf(crDTO.getCrId()));
        if (crDTO.getChangeOrginator() != null) {
          dtexecuteDTO.setUserId(Long.valueOf(crDTO.getChangeOrginator()));
        }
        dtexecuteDTO.setDtCode(mopDTO.getMopId());
        dtexecuteDTO.setFilePath(fullPath);
        dtexecuteDTO.setTimeAttack(new Date());
        dtexecuteDTO.setFileType(Constants.CR_FILE_TYPE.DT_EXECUTE);
        dtexecuteDTO.setDtFileHistory("VIPA_DD_INSERT");
        lstCrFileAttactDTO.add(dtexecuteDTO);
        //Add File DT Rollback
        fileName = String.valueOf(count).concat("_").concat("DT_ROLLBACK").concat("_")
            .concat(mopfileName);
        fullPath = "";
        try {
          fullPath = FileUtils.saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), ftpFolder, fileName, content, null);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        if (StringUtils.isStringNullOrEmpty(fullPath)) {
          TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage("Can not save file : " + fileName);
          return resultDTO;
        }
        CrFilesAttachInsiteDTO dtRollbackDTO = new CrFilesAttachInsiteDTO();
        dtRollbackDTO.setFileName(fileName);
        dtRollbackDTO.setCrId(Long.valueOf(crDTO.getCrId()));
        if (crDTO.getChangeOrginator() != null) {
          dtRollbackDTO.setUserId(Long.valueOf(crDTO.getChangeOrginator()));
        }
        dtRollbackDTO.setDtCode(mopDTO.getMopId());
        dtRollbackDTO.setFilePath(fullPath);
        dtRollbackDTO.setTimeAttack(new Date());
        dtRollbackDTO.setFileType(Constants.CR_FILE_TYPE.DT_ROLLBACK);
        dtRollbackDTO.setDtFileHistory("VIPA_DD_INSERT");

        lstCrFileAttactDTO.add(dtRollbackDTO);

        //Add File DT Script
        fileName = String.valueOf(count).concat("_").concat("DT_SCRIPT").concat("_")
            .concat(mopfileName);

        fullPath = "";
        try {
          fullPath = FileUtils.saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), ftpFolder, fileName, content, null);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        if (StringUtils.isStringNullOrEmpty(fullPath)) {
          TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage("Can not save file : " + fileName);
          return resultDTO;
        }
        CrFilesAttachInsiteDTO dtScriptDTO = new CrFilesAttachInsiteDTO();
        dtScriptDTO.setFileName(fileName);
        dtScriptDTO.setCrId(Long.valueOf(crDTO.getCrId()));
        if (crDTO.getChangeOrginator() != null) {
          dtScriptDTO.setUserId(Long.valueOf(crDTO.getChangeOrginator()));
        }
        dtScriptDTO.setDtCode(mopDTO.getMopId());
        dtScriptDTO.setFilePath(fullPath);
        dtScriptDTO.setTimeAttack(new Date());
        dtScriptDTO.setFileType(Constants.CR_FILE_TYPE.DT_SCRIPT);
        dtScriptDTO.setDtFileHistory("VIPA_DD_INSERT");
        lstCrFileAttactDTO.add(dtScriptDTO);
      }
      if (impactIp.isEmpty()) {
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("Impacred Node can not be null!");
        return resultDTO;
      }

      searchIpList.clear();
      searchIpList.addAll(impactIp.keySet());

      List<InfraDeviceDTO> infraDevices = geInfraDeviceByIps(searchIpList, nationCode);
      if (infraDevices == null || infraDevices.isEmpty()) {
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("Can not get Impacted Nodes!");
        return resultDTO;
      }

      for (InfraDeviceDTO deviceDTO : infraDevices) {

        if (!impactIp.containsKey(deviceDTO.getIp())) {
          continue;
        }
        CrImpactedNodesDTO netNode = new CrImpactedNodesDTO();
        netNode.setCrId(crDTO.getCrId());
        netNode.setDeviceId(deviceDTO.getDeviceId());
        netNode.setIpId(deviceDTO.getIpId());
        netNode.setInsertTime(dateFormat.format(new Date()));
        netNode.setDtCode(impactIp.get(deviceDTO.getIp()));
        lstNodeImpact.add(netNode);
      }
      UsersEntity usersEntity = new UsersEntity();
      UnitDTO unitToken = new UnitDTO();
      if (StringUtils.isNotNullOrEmpty(crDTO.getChangeOrginator())) {
        try {
          usersEntity = userRepository.getUserByUserId(Long.valueOf(crDTO.getChangeOrginator()));
          unitToken = unitRepository.findUnitById(usersEntity.getUnitId());
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
      // thuwc hien insert file
      boolean doInsert = insertDtFiles(lstCrFileAttactDTO, usersEntity, unitToken);
      if (!doInsert) {
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        resultDTO.setKey(RESULT.FAIL);
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        resultDTO.setMessage("Can not insert Attacted Files !");
        return resultDTO;
      }

      doInsert = insertImpactedNodes(lstNodeImpact);
      if (!doInsert) {
        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage("Can not insert impacted Nodes !");
        return resultDTO;
      }

      List<CrVmsaUpdateMopHisDTO> newHisList = findHistory(crId, validateKey);
      boolean checkUpdateStatus = false;
      if (newHisList != null && newHisList.size() > 0) {
        for (CrVmsaUpdateMopHisDTO o : newHisList) {
          if (o.getSystemCode() != null && o.getSystemCode().toUpperCase().contains("VMSA")) {
            checkUpdateStatus = true;
          }
        }
      }

      if (checkUpdateStatus) {
        // nếu đã tồn tại bản ghi của VMSA ; hay VMSA đã cập nhập ==> cho phép cập nhập CR sang trạng thái bình thương
        ResultDTO updateStatusResult = this
            .updateWaitingStatusForCR(objectToLong(crDTO.getCrId()), 0L);
        if (updateStatusResult == null || RESULT.FAIL.equals(updateStatusResult.getKey())) {
          TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage("Update Cr Waiting Status fail !");
          return resultDTO;
        }
      }

      resultDTO.setKey(RESULT.SUCCESS);
      resultDTO.setMessage("Update Successfull !");
      return resultDTO;

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(e.getMessage());
    } finally {
      searchIpList.clear();
      impactIp.clear();
      lstNodeImpact.clear();
      lstCrFileAttactDTO.clear();
      listUserId.clear();

    }
    return resultDTO;
  }

  public CrInsiteDTO getCrById(Long crId) {
    try {
      if (crId != null) {
        String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_DETAIL,
            "get-cr-by-id");
        sql += " AND cr.cr_id = :cr_id";
        Map<String, Object> params = new HashMap<>();
        Double offset = 0D;
        params.put("cr_id", crId);
        params.put("offset", offset);
        List<CrInsiteDTO> list = getNamedParameterJdbcTemplate().query(sql, params,
            BeanPropertyRowMapper.newInstance(CrInsiteDTO.class));
        if (list != null && !list.isEmpty()) {
          return list.get(0);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  private boolean insertVMsaMopHis(Long crId, Long validateKey, String systemCode,
      Integer resultCode) {
    try {
      StringBuilder sql = new StringBuilder();
      sql.append(
          " INSERT INTO CR_VMSA_UDATE_MOP_HIS(CR_VMSA_UDATE_MOP_HIS_ID,CR_ID,VALDATE_KEY,SYSTEM_CCODE,RESULT_CODE,CREATE_TIME) ");
      sql.append(
          " VALUES ( CR_VMSA_UDATE_MOP_HIS_SEQ.nextval,:crId,:validateKey, :systemCode, :resultCode,:createTime) ");
      Map<String, Object> params = new HashMap<>();
      params.put("crId", crId);
      params.put("validateKey", validateKey);
      params.put("systemCode", systemCode);
      params.put("resultCode", resultCode);
      params.put("createTime", new Date());
      int status = getNamedParameterJdbcTemplate().update(sql.toString(), params);
      if (status > 0) {
        return true;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return false;
  }

  private List<CrVmsaUpdateMopHisDTO> findHistory(Long crId, Long validateKey) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_VMSA_UPDATE_MOP, "get-cr-vmsa-update-mop-his");
      Map<String, Object> params = new HashMap<>();
      params.put("crId", crId);
      params.put("validateKey", validateKey);
      List<CrVmsaUpdateMopHisDTO> list = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrVmsaUpdateMopHisDTO.class));
      return list;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  private boolean deleteDtFiles(Long crId, String dtCode, String fileType, Date timeAttact) {
    if (crId == null) {
      return false;
    }
    if (timeAttact == null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date());
      cal.add(Calendar.DATE, -30);
      timeAttact = cal.getTime();
    }
    try {
      Map<String, Object> params = new HashMap<>();
      StringBuilder sql = new StringBuilder();
      sql.append(" SELECT a.FILE_ID fileId FROM OPEN_PM.CR_FILES_ATTACH a WHERE a.CR_ID = :crId ");
      sql.append(" and a.TIME_ATTACK >= :timeAttact ");
      params.put("crId", crId);
      params.put("timeAttact", timeAttact);
      if (StringUtils.isNotNullOrEmpty(dtCode)) {
        sql.append("  and a.DT_CODE =:dtCode  ");
        params.put("dtCode", dtCode.trim());
      }
      if (StringUtils.isNotNullOrEmpty(fileType)) {
        sql.append("  and a.FILE_TYPE = :fileType  ");
        params.put("fileType", fileType.trim());
      }
      List<CrFilesAttachDTO> crFilesAttachDTOS = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(CrFilesAttachDTO.class));
      if (crFilesAttachDTOS != null && !crFilesAttachDTOS.isEmpty()) {
        for (CrFilesAttachDTO crFilesAttachDTO : crFilesAttachDTOS) {
          deleteByMultilParam(CrFilesAttachEntity.class, "fileId", crFilesAttachDTO.getFileId());
          deleteByMultilParam(GnocFileEntity.class,
              "businessCode", GNOC_FILE_BUSSINESS.CR,
              "businessId", crId,
              "mappingId", crFilesAttachDTO.getFileId());
        }
      }
      return true;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return false;
  }

  private boolean deleteImpactedNodes(Long crId, String dtCode, Date insertTime) {
    if (crId == null) {
      return false;
    }
    try {
      if (insertTime == null) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -30);
        insertTime = cal.getTime();
      }
      Map<String, Object> params = new HashMap<>();
      StringBuilder sql = new StringBuilder();
      sql.append(" DELETE FROM OPEN_PM.CR_IMPACTED_NODES a WHERE a.CR_ID = :crId ");
      sql.append(" and a.INSERT_TIME >= :insertTime ");
      params.put("crId", crId);
      params.put("insertTime", insertTime);
      if (StringUtils.isNotNullOrEmpty(dtCode)) {
        sql.append("  and a.DT_CODE =:dtCode  ");
        params.put("dtCode", dtCode.trim());
      }
      getNamedParameterJdbcTemplate().update(sql.toString(), params);
      return true;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return false;
  }

  public List<InfraDeviceDTO> geInfraDeviceByIps(List<String> lstIp, String nationCode) {

    if (nationCode == null || nationCode.trim().isEmpty()) {
      nationCode = "VNM";
    }
    nationCode = nationCode.trim().toUpperCase();

    List<InfraDeviceDTO> list = new ArrayList<>();
    if (lstIp == null || lstIp.isEmpty()) {
      return list;
    }
    List<String> newIps = new ArrayList<>();
    for (String ip : lstIp) {
      if (ip == null) {
        continue;
      }
      newIps.add(ip.trim());
    }
    try {
      StringBuilder sql = new StringBuilder();
      Map<String, Object> params = new HashMap<>();
      sql.append(
          " select data.ipId as ipId , data.deviceId as deviceId , data.ip as ip , data.deviceCode as deviceCode , data.deviceName as deviceName , data.deviceCodeOld as deviceCodeOld , data.networkType as networkType , decode(data.nationCode,null,'VNM',data.nationCode) as nationCode ");
      sql.append(" from ( ");

      sql.append(
          " select iip.ip_id ipId, iip.ip_id  ipIdOrgi , iip.ip ip, ide.device_id deviceId , ide.device_code deviceCode,  ");
      sql.append(
          " to_char(ide.device_name) deviceName, ide.device_code_old deviceCodeOld , ide.network_type networkType , ide.NATION_CODE as nationCode  ");
      sql.append(" from common_gnoc.infra_ip iip  ");
      sql.append(
          " left join common_gnoc.infra_device ide on ide.device_id = iip.device_id where iip.ip in (:newIps) ");
      if ("VNM".equals(nationCode)) {
        sql.append(" and ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null  ) and ( iip.STATUS = 1 or iip.STATUS is null ) ");
      } else {
        sql.append(" and  ide.NATION_CODE =:nationCode and iip.STATUS = 1 ");
      }
      sql.append(" UNION  ");
      sql.append(
          " select (1000000 + iisr.server_id) ipId, iisr.server_id ipIdOrgi, iisr.ip ip, ide.device_id deviceId, ide.device_code deviceCode, to_char(ide.device_name) deviceName, ide.device_code_old deviceCodeOld, ide.network_type networkType , ide.NATION_CODE as nationCode ");
      sql.append(" from common_gnoc.infra_it_server iisr  ");
      sql.append(
          " left join common_gnoc.infra_device ide on ide.device_id = iisr.server_id where iisr.ip in (:newIps)  ");
      if ("VNM".equals(nationCode)) {
        sql.append(" and ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null  ) ");
      } else {
        sql.append(" and  ide.NATION_CODE =:nationCode ");
      }
      sql.append(" UNION  ");
      sql.append(
          " select (2000000 + icmsr.server_id) ipId, icmsr.server_id ipIdOrgi, icmsr.dcn_ip ip, ide.device_id deviceId, ide.device_code deviceCode, to_char(ide.device_name) deviceName, ide.device_code_old deviceCodeOld, ide.network_type networkType , ide.NATION_CODE as nationCode  ");
      sql.append(" from common_gnoc.infra_core_mobile_server icmsr  ");
      sql.append(
          " left join common_gnoc.infra_device ide on ide.device_id = icmsr.device_id where dcn_ip is not null  and icmsr.dcn_ip in (:newIps) ");
      if ("VNM".equals(nationCode)) {
        sql.append(" and ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null  ) ");
      } else {
        sql.append(" and  ide.NATION_CODE =:nationCode ");
      }
      sql.append(" UNION  ");
      sql.append(
          " select (3000000 + icmsr.server_id) ipId, icmsr.server_id ipIdOrgi, icmsr.mpbn_ip ip, ide.device_id deviceId, ide.device_code deviceCode, to_char(ide.device_name) deviceName, ide.device_code_old deviceCodeOld, ide.network_type networkType , ide.NATION_CODE as nationCode ");
      sql.append(" from common_gnoc.infra_core_mobile_server icmsr  ");
      sql.append(
          " left join common_gnoc.infra_device ide on ide.device_id = icmsr.device_id where mpbn_ip is not null and icmsr.mpbn_ip in (:newIps) ");
      if ("VNM".equals(nationCode)) {
        sql.append(" and ( ide.NATION_CODE = 'VNM' or ide.NATION_CODE is null  ) ");
      } else {
        sql.append(" and  ide.NATION_CODE =:nationCode ");
      }
      sql.append(" UNION ");
      sql.append(
          " select (4000000 + ipsr.ip_id) ipId, ipsr.ip_id ipIdOrgi, ipsr.ip ip, ipsr.server_id deviceId, ipsr.server_code deviceCode, to_char(ipsr.server_code) deviceName, ipsr.server_code deviceCodeOld, ipsr.server_type networkType ,  ipsr.NATION_CODE as nationCode ");
      sql.append(" from common_gnoc.infra_pstn_server ipsr  ");
      sql.append(" where ipsr.ip in (:newIps)  ");
      if ("VNM".equals(nationCode)) {
        sql.append(" and  ( ipsr.NATION_CODE = 'VNM' or ipsr.NATION_CODE is null  ) ");
      } else {
        sql.append(" and  ipsr.NATION_CODE = :nationCode ");
      }
      sql.append(" ) data  ");
      params.put("newIps", newIps);
      if (!"VNM".equals(nationCode)) {
        params.put("nationCode", nationCode);
      }
      list = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(InfraDeviceDTO.class));
      return list;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  private boolean insertDtFiles(List<CrFilesAttachInsiteDTO> lstCrFileAttactDTO,
      UsersEntity usersEntity, UnitDTO unitToken) {
    if (lstCrFileAttactDTO == null || lstCrFileAttactDTO.isEmpty()) {
      return true;
    }
    try {
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      for (CrFilesAttachInsiteDTO crFilesAttachInsiteDTOAdd : lstCrFileAttactDTO) {
        ResultInSideDto resultFileDataOld = crFilesAttachRepository.add(crFilesAttachInsiteDTOAdd);
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(crFilesAttachInsiteDTOAdd.getFilePath());
        gnocFileDto.setFileName(crFilesAttachInsiteDTOAdd.getFileName());
        gnocFileDto.setFileType(crFilesAttachInsiteDTOAdd.getFileType());
        gnocFileDto.setTemplateId(crFilesAttachInsiteDTOAdd.getTempImportId());
        gnocFileDto.setCreateUnitId(usersEntity.getUnitId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(usersEntity.getUserId());
        gnocFileDto.setCreateUserName(usersEntity.getUsername());
        gnocFileDto.setCreateTime(new Date());
        gnocFileDto.setMappingId(resultFileDataOld.getId());
        gnocFileDtos.add(gnocFileDto);
      }
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.CR, lstCrFileAttactDTO.get(0).getCrId(),
              gnocFileDtos);
      return true;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return false;
  }

  private boolean insertImpactedNodes(List<CrImpactedNodesDTO> impactedNodes) {
    if (impactedNodes == null || impactedNodes.isEmpty()) {
      return true;
    }
    try {
      for (CrImpactedNodesDTO obj : impactedNodes) {
        getEntityManager().merge(obj.toEntity());
      }
      return true;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return false;
  }

  // anhlp add
  public List<ItemDataCR> getAllActiveAffectedService(String locale) {
    List<ItemDataCR> lst = new ArrayList<>();
    try {
      String sql = "select ass.affected_service_id valueStr, ass.service_name displayStr, ass.service_code secondValue from OPEN_PM.affected_services ass where is_active = 1";
      Map<String, String> parameters = new HashMap<>();
      lst = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(ItemDataCR.class));
      try {
        String language = locale;
        if (!StringUtils.isStringNullOrEmpty(locale)) {
          language = locale;
        }
        Map map = DataUtil.getSqlLanguageExchange("2", "2", language);
        String sqlLanguage = (String) map.get("sql");
        Map mapParam = (Map) map.get("mapParam");
        Map mapType = (Map) map.get("mapType");
        List lstLanguage = languageExchangeRepository
            .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
        lst = DataUtil.setLanguage(lst, lstLanguage, "valueStr", "displayStr");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  // anhlp add
  @Override
  public ResultDTO insertVMSADT(String userService, String passService, String systemCode,
      Long crId, Long validateKey, int createMopSuccess, String createMopDetail,
      List<VMSAMopDetailDTO> mopDTOList, String nationCode, String locale) {
    ResultDTO resultDTO = new ResultDTO();
    resultDTO.setKey(Constants.RESULT.SUCCESS);
    try {
      resultDTO = insertVMSADT(crId, validateKey, systemCode, createMopSuccess, createMopDetail,
          mopDTOList, nationCode, locale);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultDTO.setKey(Constants.RESULT.FAIL);
      resultDTO.setMessage(ex.getMessage());
    }
    return resultDTO;
  }
}
