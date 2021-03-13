package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFileAttachOutput;
import com.viettel.gnoc.cr.dto.CrFileAttachOutputWithContent;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrOutputForOCSDTO;
import com.viettel.gnoc.cr.dto.CrOutputForQLTNDTO;
import com.viettel.gnoc.cr.dto.CrOutputForSOCDTO;
import com.viettel.gnoc.cr.dto.SelectionResultDTO;
import com.viettel.gnoc.cr.repository.CrAffectedServiceDetailsRepository;
import com.viettel.gnoc.cr.repository.CrForOtherSystemRepository;
import com.viettel.gnoc.cr.repository.CrHisRepository;
import com.viettel.gnoc.cr.repository.CrImpactedNodesRepository;
import com.viettel.gnoc.cr.repository.CrRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class CrForOtherSystemBusinessImpl implements CrForOtherSystemBusiness {

  @Value("${application.conf.user_service: null}")
  private String userConf;

  @Value("${application.conf.pass_service: null}")
  private String passConf;

  @Value("${application.conf.salt_service: null}")
  private String saltConf;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  CrForOtherSystemRepository crForOtherSystemRepository;

  @Autowired
  CrAffectedServiceDetailsRepository crAffectedServiceDetailsRepository;

  @Autowired
  CrImpactedNodesRepository crImpactedNodesRepository;

  @Autowired
  CrHisRepository crHisRepository;

  @Autowired
  CrRepository crRepository;

  @Override
  public List<CrCreatedFromOtherSysDTO> getListDataByObjectId(Long objectId) {
    return crForOtherSystemRepository.getListDataByObjectId(objectId);
  }

  @Override
  public List<CrCreatedFromOtherSysDTO> getListData(Long crId, Long systemId, Long objectId) {
    return crForOtherSystemRepository.getListData(crId, systemId, objectId);
  }

  @Override
  public CrCreatedFromOtherSysDTO getCrCreatedFromOtherSysDTO(Long crId) {
    CrCreatedFromOtherSysDTO dto = crForOtherSystemRepository.getCrCreatedFromOtherSysDTO(crId);
    if (!StringUtils.isStringNullOrEmpty(dto)) {
      dto.setStatus("OK");
    }
    return dto;
  }

  @Override
  public boolean checkWoCloseAutoSetting(Long unitId, Long woTypeId) {
    return crForOtherSystemRepository.checkWoCloseAutoSetting(unitId, woTypeId);
  }

  @Override
  public CrOutputForQLTNDTO getCrForQLTN(String userService, String passService, String crNumber) {
    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (!userConf.equals(user) || !passConf.equals(pass)) {
        CrOutputForQLTNDTO rs = new CrOutputForQLTNDTO();
        rs.setResultCode("NOK");
        rs.setDescription(I18n.getChangeManagement("qltn.invalidUserNameOrPassWord"));
        return rs;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return crForOtherSystemRepository.getCrForQLTN(crNumber, "");
  }

  @Override
  public List<CrOutputForSOCDTO> getListDeviceAffectForSOC(String lastUpdateTime) {
    return crForOtherSystemRepository.getListDeviceAffectForSOC(lastUpdateTime);
  }

  @Override
  public List<CrFileAttachOutputWithContent> getCrFileDTAttachWithContent(String userService,
      String passService, String crNumber, String attachTime, String fileType) {
    try {

      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (!userConf.equals(user) || !passConf.equals(pass)) {
        CrFileAttachOutputWithContent rs = new CrFileAttachOutputWithContent();
        rs.setResultCode("NOK");
        rs.setDescription(I18n.getChangeManagement("qltn.invalidUserNameOrPassWord"));
        List<CrFileAttachOutputWithContent> lst = new ArrayList<>();
        lst.add(rs);
        return lst;
      }
      if (crNumber == null || "".equals(crNumber.trim())) {
        CrFileAttachOutputWithContent rs = new CrFileAttachOutputWithContent();
        rs.setResultCode("NOK");
        rs.setDescription("CrNumber is require");
        List<CrFileAttachOutputWithContent> lst = new ArrayList<>();
        lst.add(rs);
        return lst;
      }
      if (fileType == null || "".equals(fileType.trim())) {
        CrFileAttachOutputWithContent rs = new CrFileAttachOutputWithContent();
        rs.setResultCode("NOK");
        rs.setDescription("FileType is require");
        List<CrFileAttachOutputWithContent> lst = new ArrayList<>();
        lst.add(rs);
        return lst;
      } else {
        try {
          Long.valueOf(fileType.trim());
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          CrFileAttachOutputWithContent rs = new CrFileAttachOutputWithContent();
          rs.setResultCode("NOK");
          rs.setDescription("FileType invalid");
          List<CrFileAttachOutputWithContent> lst = new ArrayList<>();
          lst.add(rs);
          return lst;
        }
      }
      if (attachTime == null || "".equals(attachTime.trim())) {
        CrFileAttachOutputWithContent rs = new CrFileAttachOutputWithContent();
        rs.setResultCode("NOK");
        rs.setDescription("AttachTime is require");
        List<CrFileAttachOutputWithContent> lst = new ArrayList<>();
        lst.add(rs);
        return lst;
      } else {
        try {
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          CrFileAttachOutputWithContent rs = new CrFileAttachOutputWithContent();
          rs.setResultCode("NOK");
          rs.setDescription("AttachTime invalid");
          List<CrFileAttachOutputWithContent> lst = new ArrayList<>();
          lst.add(rs);
          return lst;
        }

      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return crForOtherSystemRepository.getCrFileDTAttachWithContent(crNumber, attachTime, fileType);
  }


  @Override
  public List<CrFileAttachOutput> getCrFileDTAttach(String crNumber, String attachTime) {
    return crForOtherSystemRepository.getCrFileDTAttach(crNumber, attachTime);
  }

  @Override
  public SelectionResultDTO getCrlistFromTimeInterval(String userService, String passService,
      String minute) {
    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (!userConf.equals(user) || !passConf.equals(pass)) {
        return new SelectionResultDTO("NOK",
            I18n.getChangeManagement("qltn.invalidUserNameOrPassWord"), null);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new SelectionResultDTO("NOK", "Invalid time input!", null);
    }
    Double min = null;
    try {
      min = Double.parseDouble(minute);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new SelectionResultDTO("NOK", "Invalid time input!", null);
    }
    return crForOtherSystemRepository.getCrlistFromTimeInterval(min);
  }

  @Override
  public String actionResolveCrOcs(String userService, String passService, String userName,
      String crNumber, String returnCode, String locale) {
    String rs = "";
    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (!userConf.equals(user) || !passConf.equals(pass)) {
        rs = I18n.getChangeManagement("qltn.invalidUserNameOrPassWord");
        return rs;
      }
      if (crNumber == null || "".equals(crNumber.trim())) {
        rs = "CrNumber is require";
        return rs;
      }
      if (userName == null || "".equals(userName.trim())) {
        rs = "userName is require";
        return rs;
      }
      if (returnCode == null || "".equals(returnCode.trim())) {
        rs = "returnCode is require";
        return rs;
      }
      if (!"39".equals(returnCode) && !"40".equals(returnCode) && !"41".equals(returnCode)) {
        rs = "returnCode invalid";
        return rs;
      }
      UsersDTO userDTO = crForOtherSystemRepository.getUserInfo(userName);
      if (userDTO == null) {
        rs = "user not exist on GNOC";
        return rs;
      }

      UserToken userTokenGNOC = new UserToken();
      userTokenGNOC.setUserName(userName);
      CrInsiteDTO crInsiteDTO = crRepository.findCrById(
          Long.valueOf(crNumber.substring(crNumber.lastIndexOf("_") + 1, crNumber.length())),
          userTokenGNOC);
      crInsiteDTO.setActionType("24");
      crInsiteDTO.setActionReturnCodeId(returnCode);
      crInsiteDTO.setUserLogin(userDTO.getUserId().toString());
      crInsiteDTO.setUserLoginUnit(userDTO.getUnitId().toString());
      rs = crRepository.actionResolveCr(crInsiteDTO, locale);
      if (Constants.CR_RETURN_MESSAGE.SUCCESS.equals(rs)) {
        crForOtherSystemRepository.createWorklogResolveCR(userName, "Other system auto resolve CR",
            Long.valueOf(crInsiteDTO.getCrId()), Long.valueOf(userDTO.getUserId()));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.ERROR;
    }
    return rs;
  }

  @Override
  public List<CrOutputForOCSDTO> getCrForOCS(String userService, String passService,
      String userName, String startTime) {
    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (!userConf.equals(user) || !passConf.equals(pass)) {
        CrOutputForOCSDTO rs = new CrOutputForOCSDTO();
        rs.setResultCode("NOK");
        rs.setDescription(I18n.getChangeManagement("qltn.invalidUserNameOrPassWord", ""));
        List<CrOutputForOCSDTO> lst = new ArrayList<>();
        lst.add(rs);
        return lst;
      }

      if (userName == null || "".equals(userName.trim())) {
        CrOutputForOCSDTO rs = new CrOutputForOCSDTO();
        rs.setResultCode("NOK");
        rs.setDescription("Username is require");
        List<CrOutputForOCSDTO> lst = new ArrayList<>();
        lst.add(rs);
        return lst;
      }

      if (startTime == null || "".equals(startTime.trim())) {
        CrOutputForOCSDTO rs = new CrOutputForOCSDTO();
        rs.setResultCode("NOK");
        rs.setDescription("StartTime is require");
        List<CrOutputForOCSDTO> lst = new ArrayList<>();
        lst.add(rs);
        return lst;
      } else {
        try {
          SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
          spd.parse(startTime);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          CrOutputForOCSDTO rs = new CrOutputForOCSDTO();
          rs.setResultCode("NOK");
          rs.setDescription("StartTime invalid");
          List<CrOutputForOCSDTO> lst = new ArrayList<>();
          lst.add(rs);
          return lst;
        }

      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return crForOtherSystemRepository.getCrForOCS(userName, startTime);
  }

  @Override
  public ResultDTO insertFile(String userName,
      String crNumber, String fileType, String fileName, String fileContent) {
    try {
      if (userName == null || "".equals(userName.trim())) {
        ResultDTO rs = new ResultDTO();
        rs.setKey("NOK");
        rs.setMessage("Username is require");
        return rs;
      }
      if (crNumber == null || "".equals(crNumber.trim())) {
        ResultDTO rs = new ResultDTO();
        rs.setKey("NOK");
        rs.setMessage("CrNumber is require");
        return rs;
      }
      if (fileType == null || "".equals(fileType.trim())) {
        ResultDTO rs = new ResultDTO();
        rs.setKey("NOK");
        rs.setMessage("FileType is require");
        return rs;
      }
      if (fileName == null || "".equals(fileName.trim())) {
        ResultDTO rs = new ResultDTO();
        rs.setKey("NOK");
        rs.setMessage("FileName is require");
        return rs;
      }
      if (fileName.length() > 500) {
        ResultDTO rs = new ResultDTO();
        rs.setKey("NOK");
        rs.setMessage("FileName is too long (less than 500 character)");
        return rs;
      }
      if (fileContent == null || "".equals(fileContent.trim())) {
        ResultDTO rs = new ResultDTO();
        rs.setKey("NOK");
        rs.setMessage("FileContent is require");
        return rs;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return crForOtherSystemRepository
        .insertFile(userName, crNumber, fileType, fileName, fileContent);
  }

  @Override
  public ResultDTO createCRTraceFileAttach(String userService, String passService, String username,
      String crId, String fileType, String fileName, String fileContent) {
    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (!userConf.equals(user) || !passConf.equals(pass)) {
        ResultDTO rs = new ResultDTO();
        rs.setKey("NOK");
        rs.setMessage(I18n.getChangeManagement("qltn.invalidUserNameOrPassWord"));
        return rs;
      }
      ResultDTO rs = new ResultDTO();
      String check = crForOtherSystemRepository
          .createMapFile(username, crId, fileType, fileName, fileContent);
      if ("".equals(check)) {
        rs.setKey("OK");
        rs.setMessage(I18n.getChangeManagement("cr.msg.success"));
      } else {
        rs.setKey("NOK");
        rs.setMessage(I18n.getChangeManagement(check));
      }
      return rs;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public ResultDTO updateDtInfo(String userService, String passService, String userName,
      String crNumber, String dtCode, List<String> lstIpImpact, List<String> lstIpAffect,
      String mopFile, String mopFileContent, String mopRollbackFile, String mopRollbackFileContent,
      List<String> lstAffectService, String nationCode) {
    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (!userConf.equals(user) || !passConf.equals(pass)) {
        ResultDTO rs = new ResultDTO();
        rs.setKey("NOK");
        rs.setMessage(I18n.getChangeManagement("qltn.invalidUserNameOrPassWord"));
        return rs;
      }
      return crForOtherSystemRepository.updateDtInfo(userName, crNumber, dtCode,
          lstIpImpact, lstIpAffect, mopFile, mopFileContent, mopRollbackFile,
          mopRollbackFileContent, lstAffectService, nationCode);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public ResultInSideDto createCRTrace(String userService, String passService, CrDTO crDTO) {
    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (!userConf.equals(user) || !passConf.equals(pass)) {
        ResultInSideDto rs = new ResultInSideDto();
        rs.setKey("NOK");
        rs.setMessage(I18n.getChangeManagement("qltn.invalidUserNameOrPassWord"));
        return rs;
      }
      ResultInSideDto rs = new ResultInSideDto();
      String check = crForOtherSystemRepository.setCrInfor(crDTO);
      if ("".equals(check)) {
        ResultInSideDto result = crForOtherSystemRepository.saveObject(crDTO);

        crAffectedServiceDetailsRepository.saveListDTONoIdSession(crDTO.getLstAffectedService());
        crImpactedNodesRepository.saveListDTONoIdSession(crDTO.getLstNetworkNodeId(), null);

        CrHisDTO crHis = new CrHisDTO();
        crHis.setCrId(crDTO.getCrId());
        crHis.setComments(I18n.getChangeManagement("cr.trace.crHis"));
        crHis.setStatus(crDTO.getState());
        crHis.setActionCode(Constants.CR_ACTION_CODE.ADDNEW.toString());
        crHis.setUnitId(crDTO.getChangeOrginatorUnit());
        crHis.setUserId(crDTO.getChangeOrginator());
        crHis.setEarliestStartTime(crDTO.getEarliestStartTime());
        crHis.setLatestStartTime(crDTO.getLatestStartTime());
        crHis.setChangeDate(DateUtil.date2ddMMyyyyHHMMss(new Date()));
        rs = crForOtherSystemRepository.saveObjectSession(crHis);
        rs.setId(Long.valueOf(result.getId()));
        rs.setKey("OK");
        rs.setMessage(crDTO.getCrNumber());
      } else {
        rs.setKey("NOK");
        //20201207 dungpv edit khung thoi gian tac dong cr
        if (check.contains("&DUTYTYPE&")) {
          String[] arrMess = check.split("&DUTYTYPE&");
          if (arrMess != null && arrMess.length == 2) {
            rs.setMessage(I18n.getChangeManagement(arrMess[0]) + " " + arrMess[1]);
          }
        } else {
          rs.setMessage(I18n.getChangeManagement(check));
        }
        //end
      }
      return rs;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      ResultInSideDto rs = new ResultInSideDto();
      rs.setKey("NOK");
      rs.setMessage(e.getMessage());
      return rs;
    }
  }

  @Override
  public CrOutputForQLTNDTO getCrForByCode(String userService, String passService,
      String crNumber) {
    try {
      String user = PassProtector.encrypt(userService, saltConf);
      String pass = PassProtector.encrypt(passService, saltConf);
      if (!userConf.equals(user) || !passConf.equals(pass)) {
        CrOutputForQLTNDTO rs = new CrOutputForQLTNDTO();
        rs.setResultCode("NOK");
        rs.setDescription(I18n.getChangeManagement("qltn.invalidUserNameOrPassWord"));
        return rs;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return crForOtherSystemRepository.getCrForQLTN(crNumber, "GET_IMPACT_TIME");
  }

  @Override
  public ResultInSideDto actionVerifyMrIT(CrInsiteDTO crDTO, String locale) {
    String msg = crRepository.actionVerifyMrIT(crDTO, locale);
    return new ResultInSideDto(null, msg, msg);
  }
}
